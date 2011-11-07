package ru.yandex.strictweb.scriptjava.base.ajax;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.yandex.strictweb.scriptjava.base.ActiveXObject;
import ru.yandex.strictweb.scriptjava.base.CommonDelegate;
import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.DOMEvent;
import ru.yandex.strictweb.scriptjava.base.JsException;
import ru.yandex.strictweb.scriptjava.base.MayBeExcluded;
import ru.yandex.strictweb.scriptjava.base.Native;
import ru.yandex.strictweb.scriptjava.base.NativeCode;
import ru.yandex.strictweb.scriptjava.base.Node;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;
import ru.yandex.strictweb.scriptjava.base.StrictWeb;
import ru.yandex.strictweb.scriptjava.base.VoidDelegate;
import ru.yandex.strictweb.scriptjava.base.util.Log;
import ru.yandex.strictweb.scriptjava.compiler.Compiler;
import ru.yandex.strictweb.scriptjava.plugins.AjaxServiceHelperCompilerPlugin;
import ru.yandex.strictweb.scriptjava.plugins.EntityCompilerPlugin;

public class Ajax {
    public static final String FIELD_MULTISELECT = "field.multiselect";
    private static final String MICROSOFT_XMLHTTP = "Microsoft.XMLHTTP";
	private static final String EV_ONREADYSTATECHANGE = "onreadystatechange";
	public static Ajax helper;
	
	boolean autoRequest = true;
	List<AjaxRequest> requestQueue = StrictWeb.jsNewList();

	public String getRequestUrl(List<AjaxRequest> requests) {
        return "/ajax";
    }
	
//    public String getQueryString(List<AjaxRequest> requests) {
//        String query = "_rnd="+Math.random();
//        
//        for(int i=0; i<requests.size(); i++) {
//            AjaxRequest r = requests.get(i);
//            query += "&" + BEAN_NAME_PARAM+i+"="+r.clazz + "&"+METHOD_NAME_PARAM+i+"="+r.method;
//        }
//        
//        return query;
//    }
    
    public String getLoadingImageUrl() {
        return null;
    }
	
	public XMLHttpRequest getHttpRequest() {
		if(StrictWeb.window.XMLHttpRequest != null) {
			return new XMLHttpRequest();
		}
		
		return (XMLHttpRequest)(Object)new ActiveXObject(MICROSOFT_XMLHTTP);
	}
	
	/** this method handles error events. If null is passed - this means reset errors */
	public void onError(Throwable exception) {
	    if(exception == null) return;
	    Log.error("Ajax call exception: " + exception.getMessage());
	    if(exception.getStackTrace() != null) for(StackTraceElement ste : exception.getStackTrace()) {
	        Log.error(" at " + ste.getClassName()+"."+ste.getMethodName()+" : " + ste.getLineNumber());
	    }
	    if(exception.getCause() != null) {
	        Log.error("Caused by:");
	        onError(exception.getCause());
	    }
	}
	
	public void beginBatch() {
	    autoRequest = false;
	}
	
	public void makeBatchRequests() {
	    if(requestQueue.size() > 0) {
	        List<AjaxRequest> queue = requestQueue; 
	        requestQueue = StrictWeb.jsNewList();
	        makeRequest(true, queue, null);
	    }
	    autoRequest = true;
	}
    
	public Object call(String clazz, final String method, Object[] args, final VoidDelegate<Object> callBack, final VoidDelegate<Throwable> errorHandler) throws Throwable {
        if(errorHandler != null) errorHandler.voidDelegate(null); else onError(null);
        
        boolean async = callBack != null;
	    if(!async || autoRequest) {
	        List<AjaxRequest> requests = StrictWeb.jsNewList();
	        requests.add(new AjaxRequest(clazz, method, args, callBack, errorHandler));
	        return makeRequest(async, requests, errorHandler);
	    } else {
	        requestQueue.add(new AjaxRequest(clazz, method, args, callBack, errorHandler));
            return null;
	    }
    }
    
	Object makeRequest(boolean async, final List<AjaxRequest> requests, final VoidDelegate<Throwable> errorHandler) {
	    onError(null);
	    
        XMLHttpRequest request = getHttpRequest();
//        String postXml = "";
//        
//        for(int i=0; i<requests.size(); i++) {
//            AjaxRequest r = requests.get(i);
//	        if(r.errorHandler != null) r.errorHandler.voidDelegate(null);
//	        postXml += "&"+XML_DATA_PARAM+i+"="
//	            + objectToXml(r.args, null).replaceAll("%", "%25").replaceAll("&", "%26").replaceAll(";", "%3B").replaceAll("\\+", "%2B");
//	    }

        String postJson = "[";
        for(int i=0; i<requests.size(); i++) {
            AjaxRequest r = requests.get(i);
            postJson += objectToJson(r.clazz) + "," + objectToJson(r.method) + "," + arrayToJson(r.args); 
            if(i != requests.size()-1) postJson += ",";
        }
        postJson += "]";
	   
		final String url = getRequestUrl(requests);
		
		final Node[] eventTargetNodes = new Node[3];
		
		eventTargetDisable(eventTargetNodes);
		
		if(async) {
			StrictWeb.setVoidEventCallback(request, EV_ONREADYSTATECHANGE, new VoidDelegate<XMLHttpRequest>() {
				public void voidDelegate(XMLHttpRequest request) {
					if(request.readyState == 4) {
					    try {
					    	StrictWeb.setVoidEventCallback(request, EV_ONREADYSTATECHANGE, null);
					    }catch(Exception e){/*StrictWeb.window.alert(e);*/}
				        eventTargetEnable(eventTargetNodes);
				        
				        if(request.status == 200) {
    					    AjaxRequestResult[] results = parseRequestResult(request, url, requests);
    					    for(int i=0; i<results.length; i++) {
    					        AjaxRequestResult res = results[i];
                                AjaxRequest req = requests.get(i);
    					        Throwable error = res.getError();
    					        if(error != null) {
    					            throwError("Ajax.call("+req.method+"): server-side exception"
    					                , error
    					                , req.errorHandler
    					            );
    					        } else {
    					            req.callBack.voidDelegate(res.data);
    					        }
    					    }
				        } else {
				            throwError("Error status: " + request.status, null, errorHandler);
				        }
					}
				}
			});
		}
		
//		postXml = getQueryString(requests) + postXml;
		request.open("POST", url, async, null, null);

		Log.info("POST URL : " + url);
		Log.info("POST JSON: " + postJson);
        Log.info("POST args:");
        Log.info(requests);
        
		setRequestHeaders(request);
		request.send(postJson);
		
		if(async) return null;
				
		// doing sync parse
        eventTargetEnable(eventTargetNodes);
		AjaxRequestResult res = parseRequestResult(request, url, requests)[0];
		AjaxRequest req = requests.get(0);
		Throwable error = res.getError();
		if(error != null) {
            throwError("Ajax.call("+req.method+"): server-side exception"
                , error
                , req.errorHandler
            );
        }
        return res.data;
	}

	public void setRequestHeaders(XMLHttpRequest request) {
//		request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        request.setRequestHeader("Content-type", "application/json");
	}

	private AjaxRequestResult[] parseRequestResult(XMLHttpRequest request, String url, List<AjaxRequest> requests) {
		long status = request.status;
		String responseText = request.responseText;
		String statusText = request.statusText;
		request = null;
		AjaxRequestResult[] result = null;
		
		if (status == 200) {
			try {
			    Object evalResult;
				if(responseText.charAt(0) == 'v') {
				    evalResult = StrictWeb.evalFunction(responseText + "\nreturn o._a;");					
				} else {
				    evalResult = StrictWeb.evalFunction("return " + responseText);
				}
				result = (AjaxRequestResult[])evalResult;
				Log.debug("Ajax result:");
				Log.debug(result);
			}catch(Throwable e) {
				throwError("Ajax.call("+getMethodsNames(requests)+"): eval error", e, null);
			}
		} else {
			throwError("Ajax.call("+getMethodsNames(requests)+"): http error:\n" +
				"URL: "+url+"\nCode: "+status+"\nMessage: "+statusText, null, null
			);
		}
		
		if(result == null) {
			throwError("Ajax.call("+getMethodsNames(requests)+"): result is null: " + responseText, null, null);
		}
		
        if(result.length != requests.size()) {
            throwError("Ajax.call("+getMethodsNames(requests)+"): results count in not equal requests count: " + result.length +"!=" + requests.size(), null, null);
        }
				
		return result;
	}
	
	public String getMethodsNames(List<AjaxRequest> requests) {
	    String methods = "";
	    for(int i=0; i<requests.size(); i++) {
            AjaxRequest r = requests.get(i);
            methods += (i>0?",":"") + r.method;
	    }
	    return methods;
	}
	
	public void eventTargetDisable(Node[] eventTargetNodes) {
		DOMEvent ev = StrictWeb.globalEvent;

		Node el = StrictWeb.swTarget;
		if(el==null) {
		    if(null==ev) return;
		    el = ev.target;
		}
		try {
			if(el==null) el = ev.srcElement;
			if(el==null) el = ev.fromElement;
		}catch(Exception e) {/* NOP */}

		if(el==null || null==el.tagName || el.parentNode==null) return;
		
		// node
		eventTargetNodes[0] = el;
		// parent
		eventTargetNodes[1] = el.parentNode;
		// loading
		String loadingImgUrl = getLoadingImageUrl();
		eventTargetNodes[2] = (
			null != loadingImgUrl ? 
				StrictWeb.EL("span").styleDisplay("inline-block")
				    .styleWidth(el.offsetWidth+"px").styleHeight(el.offsetHeight+"px")
				    .styleBackground("center center no-repeat url(" + loadingImgUrl + ")")
				: StrictWeb.EL("span").text("loading...")
			).node;
		
		
		eventTargetNodes[1].insertBefore(eventTargetNodes[2], eventTargetNodes[0]);
		eventTargetNodes[1].removeChild(eventTargetNodes[0]);
	}
	
	public void eventTargetEnable(Node[] eventTargetNodes) {
		if(null == eventTargetNodes || eventTargetNodes.length!=3) return;
		
		eventTargetNodes[1].insertBefore(eventTargetNodes[0], eventTargetNodes[2]);
		eventTargetNodes[1].removeChild(eventTargetNodes[2]);
		
		eventTargetNodes[0] = eventTargetNodes[1] = eventTargetNodes[2] = null;
	}
	
	void throwError(String shortMsg, Throwable th, VoidDelegate<Throwable> errorHandler) {
		if(th == null) th = new JsException(shortMsg);
		
		if(errorHandler != null) {
		    errorHandler.voidDelegate(th);
		} else {
		    onError(th);
		}
	}

	@NativeCode("{throw th;}")
	private static <T> T nativeJsThrow(Object th) {
	    return null;
	}

    @MayBeExcluded
    public static String objectToJson(Object obj) {
        if(StrictWeb.jsTypeOf(obj) == "undefined") return "null";
        if(StrictWeb.jsTypeOf(obj) == "string") return '"' + StrictWeb.toJSON((String)obj) + '"';
        if(StrictWeb.jsTypeOf(obj) == "boolean") return ((Boolean)obj) ? "1": "0";
        if(StrictWeb.jsTypeOf(obj) == "number") return obj.toString();
        if(StrictWeb.jsTypeOf(obj) == "object") {
            if(obj == null) return "null";
            if(StrictWeb.isEnum(obj)) return obj.toString();
            if(StrictWeb.isInstanceOfDate(obj)) return StrictWeb.dateToStringSmart((Date)obj); 
            if(StrictWeb.isInstanceOfArray(obj)) return arrayToJson((Object[])obj); 
            if(StrictWeb.isInstanceOfNode(obj)) {
                String json = formToJson((Node)obj);
                if(json!=null&&json.length()>0&&json.charAt(0)=='{') return "["+json+"]";
                return "{"+json+"}";
            }
            Map<String, String> map = (Map<String, String>) obj;
            String json = "";
            for(String key : map.keySet()) {
                String val = map.get(key);
                if(StrictWeb.jsTypeOf(val) == "function") continue;
                if(json.length() > 0) json += ",";
                json += objectToJson(key) + ":" + objectToJson(val);
            }
            return "{" + json + "}";            
        }
        
        return nativeJsThrow("Unknown object type: " + StrictWeb.jsTypeOf(obj));
    }

    @MayBeExcluded
    public static String arrayToJson(Object[] a) {
        if(a == null) return "null";
        String json = "";
        for(int i=0; i < a.length; i++) {
            Object val = a[i];
            if(StrictWeb.jsTypeOf(val) == "function") continue;
            
            if(json.length() > 0) json += ",";
            json += objectToJson(val);
        }
        return "[" + json + "]";
    }

    @MayBeExcluded
    public static String formToJson(Node start) {
        if(start.field == DOMBuilder.DISABLED) return "";

        String json = "";
        boolean asArray = false;
        
        for(Node el : start.childNodes) {
            if(el.field == DOMBuilder.DISABLED) continue;
            if(((Boolean)(Object)el.id || (Boolean)(Object)el.name) && (el.tagName=="INPUT" || el.tagName=="SELECT" || el.tagName=="TEXTAREA")) {
                if(el.type == "radio" && !el.checked) continue;
                if(json.length()>0) json += ",";
                if(asArray) throw new RuntimeException("Form returned as array, not at map");
                json += objectToJson((Boolean)(Object)el.id || (Boolean)(Object)el.name) + ":";
                
                if(el.type == "checkbox") json += el.checked ? "1" : "0";
                else json += objectToJson(el.value);
                
            } else if(el.className!=null && el.className.indexOf(FIELD_MULTISELECT) >= 0) {
                final List<Object> val = new ArrayList<Object>();
                NodeBuilder.wrap(el).forEachSubchild(new CommonDelegate<Boolean, Node>() {
                    public Boolean delegate(Node n) {
                        if(n.field == DOMBuilder.DISABLED) return false;
                        if(n.tagName=="INPUT" || n.tagName=="SELECT") {
                            if(n.type == "checkbox") {
                                if(n.checked) val.add((Boolean)(Object)n.id || (Boolean)(Object)n.name);
                            } else {
                                val.add((String)n.value);
                            }
                        }
                        return true;
                    }
                });
                
                if(json.length()>0) json += ",";
                if(asArray) throw new RuntimeException("Form returned as array, not at map");
                json += objectToJson(el.field) + ":" + arrayToJson((Object[])(Object)val);
            } else {
                String childJson = formToJson(el);
                if(childJson != null && childJson.length() > 0) {
                    if(childJson.charAt(0) == '{') asArray = true;
                    else if(asArray) throw new RuntimeException("Form returned as array, not at map");

                    if(json.length()>0) json += ",";
                    json += childJson;
                }
            }
        }
        
        if(start.field != null) {
            if(StrictWeb.jsTypeOf(start.field) == "string") {
                return objectToJson(start.field) + ":{" + json + "}";
            } else return "{" + json + "}";
        }
        
        if(asArray) return "[" + json + "]";
        
        return json;
    }
    
    
    @MayBeExcluded
	public static String objectToXml(Object obj, String _id) {
		String id = (_id!=null?" id=\""+_id+"\"":"");
		if(StrictWeb.jsTypeOf(obj) == "undefined") return "<null"+id+"/>";
		if(StrictWeb.jsTypeOf(obj) == "string") return "<s"+id+">"+StrictWeb.toHTML((String)obj)+"</s>";
		if(StrictWeb.jsTypeOf(obj) == "boolean") return ((Boolean)obj) ? "<b"+id+">1</b>" : "<b"+id+">0</b>";
		if(StrictWeb.jsTypeOf(obj) == "number") return "<n"+id+">"+obj+"</n>";
		if(StrictWeb.jsTypeOf(obj) == "object") {
			if(obj == null) return "<null"+id+"/>";
			if(StrictWeb.isEnum(obj)) return "<e"+id+">"+obj.toString()+"</e>";
			if(StrictWeb.isInstanceOfDate(obj)) return "<d"+id+">"+StrictWeb.dateToStringSmart((Date)obj)+"</d>"; 
			if(StrictWeb.isInstanceOfArray(obj)) return arrayToXml((Object[])obj, id); 
			if(StrictWeb.isInstanceOfNode(obj)) {
                return "<form"+id+">" + formToXml((Node)obj) + "</form>";
			}
			Map<String, String> map = (Map<String, String>) obj;
			String xml = "<o"+id+">";
			for(String key : map.keySet()) {
				String val = map.get(key);
				if(StrictWeb.jsTypeOf(val) != "function")
					xml += objectToXml(val, key);
			}
			return xml+"</o>";
		}
		
		return "<"+StrictWeb.jsTypeOf(obj) + "/>";
	}

    @MayBeExcluded
	public static String formToXml(Node start) {
		String xml = "";
		
		if(start.field == DOMBuilder.DISABLED) return xml;
		
		if(start.field != null) {
			if(StrictWeb.jsTypeOf(start.field) == "string") {
				xml = "<f id=\"" + start.field + "\">";
			} else xml = "<f>";
		}
		
		for(Node el : start.childNodes) {
		    if(el.field == DOMBuilder.DISABLED) continue;
			if(((Boolean)(Object)el.id || (Boolean)(Object)el.name) && (el.tagName=="INPUT" || el.tagName=="SELECT" || el.tagName=="TEXTAREA")) {
				if(el.type == "radio" && !el.checked) continue;
				
				xml += "<f id=\"" + ((Boolean)(Object)el.id || (Boolean)(Object)el.name) + "\">";
				
				if(el.type == "checkbox") xml += el.checked ? "1" : "0";
				else xml += StrictWeb.toHTML((String)el.value);
				
				xml += "</f>";
				
			} else if(el.className!=null && el.className.indexOf(FIELD_MULTISELECT) >= 0) {
				final List<Object> val = new ArrayList<Object>();
				NodeBuilder.wrap(el).forEachSubchild(new CommonDelegate<Boolean, Node>() {
					public Boolean delegate(Node n) {
					    if(n.field == DOMBuilder.DISABLED) return false;
					    if(n.tagName=="INPUT" || n.tagName=="SELECT") {
					        if(n.type == "checkbox") {
					            if(n.checked) val.add((Boolean)(Object)n.id || (Boolean)(Object)n.name);
					        } else {
					            val.add((String)n.value);
					        }
					    }
						return true;
					}
				});
				
				xml += "<ms id=\"" + el.field + "\">"
				+ (val.size() > 0 ? "<q>" : "")
				+ StrictWeb.jsJoinList(val, "</q><q>")
				+ (val.size() > 0 ? "</q>" : "")				
				+ "</ms>";
			} else xml += formToXml(el);
		}
		
		if(start.field != null) xml += "</f>";
		
		return xml;
	}

    @MayBeExcluded
	public static String arrayToXml(Object[] a, String id) {
		String xml = "<a"+id+">";
		for(int i=0; i < a.length; i++) {
			if(StrictWeb.jsTypeOf(a[i]) != "function")
				xml += objectToXml(a[i], null);
		}
		return xml + "</a>";
	}

	/**
	 * Use this method to append all Ajax specific classes to StrictWeb compier 
	 */
	@Native
	public static void prepareCompiler(Compiler compiler) throws Exception {
		compiler
		.addPlugin(new EntityCompilerPlugin())
		.addPlugin(new AjaxServiceHelperCompilerPlugin())
		
//		.parseClass(AjaxException.class)
		.parseClass(AjaxRequest.class)
		.parseClass(AjaxRequestResult.class)
		.parseClass(ActiveXObject.class)
		.parseClass(Log.class)
		.parseClass(XMLHttpRequest.class)
		.parseClass(Ajax.class);
	}
}
