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
import ru.yandex.strictweb.scriptjava.base.NativeCode;
import ru.yandex.strictweb.scriptjava.base.Node;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;
import ru.yandex.strictweb.scriptjava.base.ScriptJava;
import ru.yandex.strictweb.scriptjava.base.VoidDelegate;
import ru.yandex.strictweb.scriptjava.base.util.Log;
import ru.yandex.strictweb.scriptjava.compiler.Compiler;
import ru.yandex.strictweb.scriptjava.plugins.AjaxServiceHelperCompilerPlugin;
import ru.yandex.strictweb.scriptjava.plugins.EntityCompilerPlugin;

public class Ajax {
	private static final String MICROSOFT_XMLHTTP = "Microsoft.XMLHTTP";
	private static final String EV_ONREADYSTATECHANGE = "onreadystatechange";
	public static String XML_DATA_PARAM = "xml-data";
	public static String DEFAULT_LOADING_IMG = null;

	public static AjaxUrlFormer defaultUrlFormer = new AjaxUrlFormer() {
		public String getUrl(String clazz, String method) {
			return "/ajax";
		}
		public String getQueryString(String clazz, String method) {
			return "_rnd="+Math.random()+"&bean="+clazz+"&action="+method;
		}
	};
	
	public static final VoidDelegate<Throwable> baseErrorHandler = new VoidDelegate<Throwable>() {
		public void voidDelegate(Throwable exc) {
			if(exc == null) return;
			Log.error(exc.getMessage());
			if(exc.getStackTrace() != null) for(StackTraceElement ste : exc.getStackTrace()) {
				Log.error(" at " + ste.getClassName()+"."+ste.getMethodName()+" : " + ste.getLineNumber());
			}
			if(exc.getCause() != null) {
				Log.error("Caused by:");
				voidDelegate(exc.getCause());
			}
		}
	};
	
	public static VoidDelegate<Throwable> defaultErrorHandler = baseErrorHandler;

	public static XMLHttpRequest getHttpRequest() {
		if(ScriptJava.window.XMLHttpRequest != null) {
			return new XMLHttpRequest();
		}
		
		return (XMLHttpRequest)(Object)new ActiveXObject(MICROSOFT_XMLHTTP);
	}
	
	// , ValidatorHelperBase validator, AjaxUrlFormer urlFormer
	public static Object syncCall(String clazz, final String method, Object args, final VoidDelegate<Object> callBack, final VoidDelegate<Throwable> errorHandler) throws Throwable {
		defaultErrorHandler.voidDelegate(null);
		boolean async = callBack != null;
		Log.info("<b>DOING ASYNC CALL</b>");
//		validate(args, validator);
		
		final XMLHttpRequest request = getHttpRequest();
		
//		if(null == urlFormer) urlFormer = defaultUrlFormer;
		final String url = defaultUrlFormer.getUrl(clazz, method);
		Log.debug("<br><b>Request:</b> " + url);
		
//		String postXml = args==null ? null : ("<request>" + objectToXml(args, null) + "</request>");		
		String postXml = args==null ? null : (objectToXml(args, null));	
		
//		ScriptJava.alertWindow(postXml);
		
		final Node[] eventTargetNodes = new Node[3];
		
		eventTargetDisable(eventTargetNodes);
		
		if(async) {
			ScriptJava.setVoidEventCallback(request, EV_ONREADYSTATECHANGE, new VoidDelegate<XMLHttpRequest>() {
				public void voidDelegate(XMLHttpRequest r) {
//					Log.info("<b>readyState</b> = " + r.readyState);
					if(r.readyState == 4) callBack.voidDelegate(parseRequestResult(r, url, method, eventTargetNodes, errorHandler));
				}
			});
		}
		
		if(null == postXml) {
			request.open("GET", url, async, null, null);
			request.send(null);
		} else {
			postXml = defaultUrlFormer.getQueryString(clazz, method) + "&"+XML_DATA_PARAM+"=" + postXml.replaceAll("%", "%25").replaceAll("&", "%26").replaceAll(";", "%3B").replaceAll("\\+", "%2B");
			Log.debug(postXml.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
			request.open("POST", url, async, null, null);
//			request.setRequestHeader("Connection", "close");
			request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
//			request.setRequestHeader("Content-length", postXml.length()+"");
			request.send(postXml);
		}
				
		return async ? null : parseRequestResult(request, url, method, eventTargetNodes, errorHandler);
	}

	private static Object parseRequestResult(XMLHttpRequest request, String url, String method, Node[] eventTargetNodes, VoidDelegate<Throwable> errorHandler) {
		long status = request.status;
		String responseText = request.responseText;
		String statusText = request.statusText;
		request = null;
		AjaxRequestResult result = null;
		
		if (status == 200) {
			Log.debug("<br><b>results:</b>");
			Log.debug(responseText);
			
			try {
//				ScriptJava.alertWindow(responseText);
				if(responseText.charAt(0) == 'v') {
					result = (AjaxRequestResult) ScriptJava.evalFunction(responseText + "\nreturn o._a;");					
				} else {
					result = (AjaxRequestResult) ScriptJava.evalFunction("return " + responseText);
				}
			}catch(Throwable e) {
				throwError("Ajax.syncCall("+method+"): eval error", e, eventTargetNodes, errorHandler);
			}
		} else {
			throwError("Ajax.syncCall("+method+"): http error:\n" +
				"URL: "+url+"\nCode: "+status+"\nMessage: "+statusText
				, null
				, eventTargetNodes
				, errorHandler
			);
		}
		
		if(result == null) {
			throwError("Ajax.syncCall("+method+"): result is null: " + responseText, null, eventTargetNodes, errorHandler);
		}
		
		Throwable error = result.getError();
		if(error != null) {
			throwError("Ajax.syncCall("+method+"): server-side exception"
					, error
					, eventTargetNodes
					, errorHandler
			);
		}

		eventTargetEnable(eventTargetNodes);
		
		return result.getData();
	}
	
	private static void eventTargetDisable(Node[] eventTargetNodes) {
		DOMEvent ev = ScriptJava.globalEvent;
//		ScriptJava.alertWindow(ev);

		Node el = ScriptJava.swTarget;
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
		eventTargetNodes[2] = (
			null != DEFAULT_LOADING_IMG ? 
				ScriptJava.EL("span").add(ScriptJava.EL("img").src(DEFAULT_LOADING_IMG)) 
				: ScriptJava.EL("span").text("loading...")
			).node;
		
		
		eventTargetNodes[1].insertBefore(eventTargetNodes[2], eventTargetNodes[0]);
		eventTargetNodes[1].removeChild(eventTargetNodes[0]);
	}
	
	private static void eventTargetEnable(Node[] eventTargetNodes) {
		if(null == eventTargetNodes || eventTargetNodes.length!=3) return;
//		if(null==eventTarget || eventTargetParent==null || eventTargetLoading==null) return;
		
		eventTargetNodes[1].insertBefore(eventTargetNodes[0], eventTargetNodes[2]);
		eventTargetNodes[1].removeChild(eventTargetNodes[2]);
		
		eventTargetNodes[0] = eventTargetNodes[1] = eventTargetNodes[2] = null;
	}
	
	static void throwError(String shortMsg, Throwable th, Node[] eventTargetNodes, VoidDelegate<Throwable> errorHandler) {
		eventTargetEnable(eventTargetNodes);

		if(th == null) th = new JsException(shortMsg);
		
		if(errorHandler != null) {
		    errorHandler.voidDelegate(th);
		} else if(defaultErrorHandler != null) {
			defaultErrorHandler.voidDelegate(th);
		}
		
		nativeJsThrow(th);
//		ScriptJava.alertWindow(type + " \n " + message);
	}

	@NativeCode("{throw th;}")
	private static void nativeJsThrow(Object th) {
	}

	public static String objectToXml(Object obj, String _id) {
		String id = (_id!=null?" id=\""+_id+"\"":"");
		if(ScriptJava.typeOf(obj) == "undefined") return "<null"+id+"/>";
		if(ScriptJava.typeOf(obj) == "string") return "<s"+id+">"+ScriptJava.toHTML((String)obj)+"</s>";
		if(ScriptJava.typeOf(obj) == "boolean") return ((Boolean)obj) ? "<b"+id+">1</b>" : "<b"+id+">0</b>";
		if(ScriptJava.typeOf(obj) == "number") return "<n"+id+">"+obj+"</n>";
		if(ScriptJava.typeOf(obj) == "object") {
			if(obj == null) return "<null"+id+"/>";
			if(ScriptJava.isEnum(obj)) return "<e"+id+">"+obj.toString()+"</e>";
			if(ScriptJava.isInstanceOfDate(obj)) return "<d"+id+">"+ScriptJava.dateToStringSmart((Date)obj)+"</d>"; 
			if(ScriptJava.isInstanceOfArray(obj)) return arrayToXml((Object[])obj, id); 
			if(ScriptJava.isInstanceOfNode(obj)) {
				return (_id!=null?"<form"+id+">":"")
					+ formToXml((Node)obj)
					+ (_id!=null?"</form>":"");
			}
			Map<String, String> map = (Map<String, String>) obj;
			String xml = "<o"+id+">";
			for(String key : map.keySet()) {
				String val = map.get(key);
				if(ScriptJava.typeOf(val) != "function")
					xml += objectToXml(val, key);
			}
			return xml+"</o>";
		}
		
		return "<"+ScriptJava.typeOf(obj) + "/>";
	}

	public static String formToXml(Node start) {
		String xml = "";
		
		if(start.field == DOMBuilder.DISABLED) return xml;
		
		if(start.field != null) {
			if(ScriptJava.typeOf(start.field) == "string") {
				xml = "<f id=\"" + start.field + "\">";
			} else xml = "<f>";
		}
		
		for(Node el : start.childNodes) {
			if((el.id!=null||el.name!=null) && (el.tagName=="INPUT" || el.tagName=="SELECT" || el.tagName=="TEXTAREA")) {
				if(el.type == "radio" && !el.checked) continue;
				
				xml += "<f id=\"" + (el.id!=""?el.id:el.name) + "\">";
				
				if(el.type == "checkbox") xml += el.checked ? "1" : "0";
				else xml += ScriptJava.toHTML((String)el.value);
				
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
				+ ScriptJava.listJoin(val, "</q><q>")
				+ (val.size() > 0 ? "</q>" : "")				
				+ "</ms>";
			} else xml += formToXml(el);
		}
		
		if(start.field != null) xml += "</f>";
		
		return xml;
	}

	private static String arrayToXml(Object[] a, String id) {
//		ScriptJava.window.alert("arrayToXml: " + a + " :: " + a.length);
		String xml = "<a"+id+">";
		for(int i=0; i < a.length; i++) {
			if(ScriptJava.typeOf(a[i]) != "function")
				xml += objectToXml(a[i], null);
		}
		return xml + "</a>";
	}

//	public static void validate(Object args, ValidatorHelperBase validator) {
//		if(null == args || null == validator) return;
//		
//	}

	/**
	 * Use this method to append all Ajax specific classes to ScriptJava compier 
	 */
	@NativeCode("{}")
	public static void prepareCompiler(Compiler compiler) throws Exception {
		compiler
		.addPlugin(new EntityCompilerPlugin())
		.addPlugin(new AjaxServiceHelperCompilerPlugin())
		
		.parseClass(AjaxException.class)
		.parseClass(AjaxRequestResult.class)
		.parseClass(ActiveXObject.class)
		.parseClass(Log.class)
		.parseClass(XMLHttpRequest.class)
		.parseClass(AjaxUrlFormer.class)
		.parseClass(Ajax.class);
	}
}
