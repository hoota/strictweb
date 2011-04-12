package ru.yandex.strictweb.scriptjava.base.ajax;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ru.yandex.strictweb.ajaxtools.exception.AjaxException;
import ru.yandex.strictweb.scriptjava.base.ActiveXObject;
import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.DOMEvent;
import ru.yandex.strictweb.scriptjava.base.DOMEventCallback;
import ru.yandex.strictweb.scriptjava.base.JsException;
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
	private static final String MICROSOFT_XMLHTTP = "Microsoft.XMLHTTP";
	private static final String EV_ONREADYSTATECHANGE = "onreadystatechange";
	public static String XML_DATA_PARAM = "xml-data";
	public static String BEAN_NAME_PARAM = "bean";
	public static String METHOD_NAME_PARAM = "action";
	public static Ajax helper;
	
	boolean autoRequest = true;
	List<AjaxRequest> requestQueue = StrictWeb.newJsList();

	public String getRequestUrl(List<AjaxRequest> requests) {
        return "/ajax";
    }
	
    public String getQueryString(List<AjaxRequest> requests) {
        String query = "_rnd="+Math.random();
        
        for(int i=0; i<requests.size(); i++) {
            AjaxRequest r = requests.get(i);
            query += "&" + BEAN_NAME_PARAM+i+"="+r.clazz + "&"+METHOD_NAME_PARAM+i+"="+r.method;
        }
        
        return query;
    }
    
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
	    Log.error(exception.getMessage());
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
	        makeRequest(true, requestQueue);
	        requestQueue = StrictWeb.newJsList();
	    }
	    autoRequest = true;
	}
    
	public Object call(String clazz, final String method, Object args, final VoidDelegate<Object> callBack, final VoidDelegate<Throwable> errorHandler) throws Throwable {
        if(errorHandler != null) errorHandler.voidDelegate(null); else onError(null);
        
        boolean async = callBack != null;
	    if(!async || autoRequest) {
	        List<AjaxRequest> requests = StrictWeb.newJsList();
	        requests.add(new AjaxRequest(clazz, method, args, callBack, errorHandler));
	        return makeRequest(async, requests);
	    } else {
	        requestQueue.add(new AjaxRequest(clazz, method, args, callBack, errorHandler));
            return null;
	    }
    }
    
	Object makeRequest(boolean async, final List<AjaxRequest> requests) {
	    onError(null);
	    
        XMLHttpRequest request = getHttpRequest();
        String postXml = "";
        
        for(int i=0; i<requests.size(); i++) {
            AjaxRequest r = requests.get(i);
	        if(r.errorHandler != null) r.errorHandler.voidDelegate(null);
	        postXml += "&"+XML_DATA_PARAM+i+"="
	            + objectToXml(r.args, null).replaceAll("%", "%25").replaceAll("&", "%26").replaceAll(";", "%3B").replaceAll("\\+", "%2B");
	    }
	   
		final String url = getRequestUrl(requests);
		
		final Node[] eventTargetNodes = new Node[3];
		
		eventTargetDisable(eventTargetNodes);
		
		if(async) {
			StrictWeb.setVoidEventCallback(request, EV_ONREADYSTATECHANGE, new VoidDelegate<XMLHttpRequest>() {
				public void voidDelegate(XMLHttpRequest request) {
					if(request.readyState == 4) {
					    AjaxRequestResult[] results = parseRequestResult(request, url, eventTargetNodes, requests);
					    for(int i=0; i<results.length; i++) {
					        AjaxRequestResult res = results[i];
                            AjaxRequest req = requests.get(i);
					        Throwable error = res.getError();
					        if(error != null) {
					            throwError("Ajax.call("+req.method+"): server-side exception"
					                , error
					                , eventTargetNodes
					                , req.errorHandler
					            );
					        } else {
					            req.callBack.voidDelegate(res.data);
					        }
					    }
					}
				}
			});
		}
		
		postXml = getQueryString(requests) + postXml;
		request.open("POST", url, async, null, null);
		request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		request.send(postXml);
		
		if(async) return null;
				
		// doing sync parse
		AjaxRequestResult res = parseRequestResult(request, url, eventTargetNodes, requests)[0];
		AjaxRequest req = requests.get(0);
		Throwable error = res.getError();
		if(error != null) {
            throwError("Ajax.call("+req.method+"): server-side exception"
                , error
                , eventTargetNodes
                , req.errorHandler
            );
        }
        return res.data;
	}

	private AjaxRequestResult[] parseRequestResult(XMLHttpRequest request, String url, Node[] eventTargetNodes, List<AjaxRequest> requests) {
		long status = request.status;
		String responseText = request.responseText;
		String statusText = request.statusText;
		request = null;
		AjaxRequestResult[] result = null;
		
		if (status == 200) {
			try {
				if(responseText.charAt(0) == 'v') {
					result = (AjaxRequestResult[]) StrictWeb.evalFunction(responseText + "\nreturn o._a;");					
				} else {
					result = (AjaxRequestResult[]) StrictWeb.evalFunction("return " + responseText);
				}
			}catch(Throwable e) {
				throwError("Ajax.call("+getMethodsNames(requests)+"): eval error", e, eventTargetNodes, null);
			}
		} else {
			throwError("Ajax.call("+getMethodsNames(requests)+"): http error:\n" +
				"URL: "+url+"\nCode: "+status+"\nMessage: "+statusText
				, null
				, eventTargetNodes
				, null
			);
		}
		
		if(result == null) {
			throwError("Ajax.call("+getMethodsNames(requests)+"): result is null: " + responseText, null, eventTargetNodes, null);
		}
		
        if(result.length != requests.size()) {
            throwError("Ajax.call("+getMethodsNames(requests)+"): results count in not equal requests count: " + result.length +"!=" + requests.size(), null, eventTargetNodes, null);
        }
		
		eventTargetEnable(eventTargetNodes);
		
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
	
	void eventTargetDisable(Node[] eventTargetNodes) {
		DOMEvent ev = StrictWeb.globalEvent;

		Node el = StrictWeb.swTarget;
		if(el==null) {
		    if(null==ev) return;
		    el = ev.target;
		}
		if(el==null) el = ev.srcElement;
		if(el==null) el = ev.fromElement;

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
	
	void eventTargetEnable(Node[] eventTargetNodes) {
		if(null == eventTargetNodes || eventTargetNodes.length!=3) return;
		
		eventTargetNodes[1].insertBefore(eventTargetNodes[0], eventTargetNodes[2]);
		eventTargetNodes[1].removeChild(eventTargetNodes[2]);
		
		eventTargetNodes[0] = eventTargetNodes[1] = eventTargetNodes[2] = null;
	}
	
	void throwError(String shortMsg, Throwable th, Node[] eventTargetNodes, VoidDelegate<Throwable> errorHandler) {
		eventTargetEnable(eventTargetNodes);

		if(th == null) th = new JsException(shortMsg);
		
		if(errorHandler != null) {
		    errorHandler.voidDelegate(th);
		} else {
		    onError(th);
		}
	}

	@NativeCode("{throw th;}")
	private static void nativeJsThrow(Object th) {
	}

	public static String objectToXml(Object obj, String _id) {
		String id = (_id!=null?" id=\""+_id+"\"":"");
		if(StrictWeb.typeOf(obj) == "undefined") return "<null"+id+"/>";
		if(StrictWeb.typeOf(obj) == "string") return "<s"+id+">"+StrictWeb.toHTML((String)obj)+"</s>";
		if(StrictWeb.typeOf(obj) == "boolean") return ((Boolean)obj) ? "<b"+id+">1</b>" : "<b"+id+">0</b>";
		if(StrictWeb.typeOf(obj) == "number") return "<n"+id+">"+obj+"</n>";
		if(StrictWeb.typeOf(obj) == "object") {
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
				if(StrictWeb.typeOf(val) != "function")
					xml += objectToXml(val, key);
			}
			return xml+"</o>";
		}
		
		return "<"+StrictWeb.typeOf(obj) + "/>";
	}

	public static String formToXml(Node start) {
		String xml = "";
		
		if(start.field == DOMBuilder.DISABLED) return xml;
		
		if(start.field != null) {
			if(StrictWeb.typeOf(start.field) == "string") {
				xml = "<f id=\"" + start.field + "\">";
			} else xml = "<f>";
		}
		
		for(Node el : start.childNodes) {
		    if(el.field == DOMBuilder.DISABLED) continue;
			if((el.id!=null||el.name!=null) && (el.tagName=="INPUT" || el.tagName=="SELECT" || el.tagName=="TEXTAREA")) {
				if(el.type == "radio" && !el.checked) continue;
				
				xml += "<f id=\"" + (el.id!=""?el.id:el.name) + "\">";
				
				if(el.type == "checkbox") xml += el.checked ? "1" : "0";
				else xml += StrictWeb.toHTML((String)el.value);
				
				xml += "</f>";
				
			} else if(el.className=="field.multiselect") {
				final List<String> val = new Vector<String>();
				NodeBuilder.wrap(el).forEachSubchild(new DOMEventCallback() {
					public boolean delegate(Node n) {
						if(/*n.isChecked && */n.checked) val.add((n.id!=null?n.id:n.name));
						return true;
					}
				});
				
				xml += "<ms id=\"" + (el.id!=null?el.id:el.name) + "\">"
				+ (val.size() > 0 ? "<q>" : "")
				+ StrictWeb.listJoin(val, "</q><q>")
				+ (val.size() > 0 ? "</q>" : "")				
				+ "</ms>";
			} else xml += formToXml(el);
		}
		
		if(start.field != null) xml += "</f>";
		
		return xml;
	}

	public static String arrayToXml(Object[] a, String id) {
		String xml = "<a"+id+">";
		for(int i=0; i < a.length; i++) {
			if(StrictWeb.typeOf(a[i]) != "function")
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
		
		.parseClass(AjaxException.class)
		.parseClass(AjaxRequest.class)
		.parseClass(AjaxRequestResult.class)
		.parseClass(ActiveXObject.class)
		.parseClass(Log.class)
		.parseClass(XMLHttpRequest.class)
		.parseClass(Ajax.class);
	}
}
