package ru.yandex.strictweb.scriptjava.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandex.strictweb.scriptjava.base.util.Console;
import ru.yandex.strictweb.scriptjava.base.util.IntervalIdentifier;
import ru.yandex.strictweb.scriptjava.base.util.TimeoutIdentifier;

public class ScriptJava extends JSObject {
	public static DOMEvent globalEvent;
	public static Node swTarget;
	
	public final static Window window;
	public final static Document document;
	public final static Console console;
	
	static {
		window = getWindow();
		document = getDocument();
		console = getConsole();
	}
	
	@NativeCode("{var f = new Function(code); return f();}" +
		";String.prototype.trim = function() {" +
		"return this.replace(/^\\s+/, '').replace(/\\s+$/, '');" +
		"}" +
		";String.prototype.toHTML = function() {return %ScriptJava%.%toHTML%(this);}"
	)
	public static Object evalFunction(String code) {
		return null;
	}
	
	@NativeCode("{return str.replace(/&/g, '&amp;').replace(/</g, '&lt;')" +
		".replace(/>/g, '&gt;').replace(/\"/g, '&quot;');}")
	public static String toHTML(String str) {
		return null;
	}

	@NativeCode("{if(typeof val != 'number') return '';" +
		"val = '00'+Math.round(val*100.0);" +
		"return val.replace(/([0-9]{2})$/, '.$1').replace(/\\.0+$/, '').replace(/^0+([^\\.])/, '$1');}")
	public static String round2(double val) {
		return null;
	}
	
	@NativeCode("{return document;}")
	public static Document getDocument() {
		return null;
	};

	@NativeCode("{return typeof console!='undefined' && typeof console.debug == 'function' ? console : null;}")
	public static Console getConsole() {
		return null;
	};
	
	@NativeCode("{var n = document.getElementById(id); return null==n?null:%NodeBuilder%.%wrap%(n);}")
	public static NodeBuilder $(String id) {return null;}

	@NativeCode("{return document.getElementById(id);}")
    public static Node $$(String id) {return null;}
	
	@NativeCode("{return window;}")
	public static Window getWindow() {
		return null;
	};

	@NativeCode("{if(tagName!=null) return document.createElement(tagName);}")
	final public static Node createNode(String tagName) {
		return null;
	}

	@NativeCode("{return document.createTextNode(s);}")
	final public static Node textNode(String s) {
		return null;
	}
	
	final public static NodeBuilder EL(String tagName) {
		return new NodeBuilder(tagName);
	}
	
	public static final NodeBuilder $CHECKBOX(String name, boolean checked) {
		return EL("input").className("cb").type("checkbox").name(name).checked(checked);
	}

	public static final NodeBuilder $RADIO(String name, boolean checked) {
        return EL("input").className("cb").type("radio").name(name).checked(checked);
    }
	
	@NativeCode("{var nb = %ScriptJava%.%EL%('b');nb.node = %ScriptJava%.%$CHECKBOX%(name, checked, text, checkedUrl, uncheckedUrl);return nb;}")
	public static final NodeBuilder $CHECKBOXcustom(String name, boolean checked, String text, String checkedUrl, String uncheckedUrl) {
		return null;
	}

	@NativeCode("{for(var k in src) if(k!='prototype')dst[k] = src[k];return dst;}")
	public static void extend(JSObject dst, JSObject src) {
	}
	
	@NativeCode("{e = %ScriptJava%.%globalEvent%;if(!e) return;" +
//			"try{if(document.dispatchEvent)document.dispatchEvent(e);}catch(qq){}" +
			"e.stopPropagation?e.stopPropagation():e.cancelBubble=true;" +
			"return e;}")
	public static DOMEvent stopEvent() {
		return null;
	}

	@NativeCode("{if(cb==null){obj[name]=null;return;};obj[name] = typeof cb=='function'?cb:function(ev, nullNode) {%ScriptJava%.%swTarget%=null;%ScriptJava%.%globalEvent%=ev||window.event;if(cb&&cb.%delegate%)return cb.%delegate%(nullNode ? null : this);return false;}}")
	public static void setDOMEventCallback(Object obj, String name, DOMEventCallback cb) {
	}
	
	@NativeCode("{if(cb==null){obj[name]=null;return;};obj[name] = typeof cb=='function'?cb:function(ev) {if(cb&&cb.%voidDelegate%) cb.%voidDelegate%(this);return false;}}")
	public static void setVoidEventCallback(Object obj, String name, VoidDelegate<?> cb) {
	}

	/**
	 * https://developer.mozilla.org/en/DOM/element.removeEventListener
	 * @return the function, that can be used to call
	 */
	@NativeCode("{obj.removeEventListener(event, func, useCapture);}")
	public static void removeEventListener(Object obj, String event, JavaScriptFunction func, boolean useCapture) {
	}
	
	/**
	 * https://developer.mozilla.org/en/DOM/element.addEventListener
	 * @return the function, that can be used to call
	 */
	@NativeCode("{if(cb==null)return null;var f = function(ev, nullNode) {%ScriptJava%.%swTarget%=null;%ScriptJava%.%globalEvent%=ev||window.event;return cb.%delegate%(nullNode ? null : this);};obj.addEventListener(event, f, useCapture); return f;}")
	public static JavaScriptFunction addEventListener(Object obj, String event, DOMEventCallback cb, boolean useCapture) {
		return null;
	}
	
	public static void onLoadWindow(DOMEventCallback cb) {
		setDOMEventCallback(window, "onload", cb);
	}
	
	@NativeCode("{return new RegExp(base, flags);}")
	public static String regExp(String base, String flags) {
		return null;
	}
	
	/**
	 * Compare dates
	 * @return result eq 0, if dates are equal. <br> 
	 *   &lt;0 if d1&lt;d2 <br>
	 *   &gt;0 if d1&gt;d2
	 */
	public static int compareDates(Date d1, Date d2) {
		int t1 = d1.getYear(), t2 = d2.getYear();
		if(t1<t2) return -1; if(t1>t2) return 1;
		t1 = d1.getMonth(); t2 = d2.getMonth();
		if(t1<t2) return -1; if(t1>t2) return 1;
		t1 = d1.getDate(); t2 = d2.getDate();
		if(t1<t2) return -1; if(t1>t2) return 1;

		return 0;
	}

	@NativeCode("{if(d1<d2)return -1;if(d1>d2)return 1;return 0;}")
	public static int compareTimes(Date d1, Date d2) {
		return 0;
	}
	
	@NativeCode("{if(str1<str2)return -1;if(str1>str2)return 1;return 0;}")
	public static int compareStrings(String str1, String str2) {
		return 0;
	}
	
	@NativeCode("{if(n1<n2)return -1;if(n1>n2)return 1;return 0;}")
	public static int compareNumbers(Object n1, Object n2) {
		return 0;
	}
	
	@NativeCode("{return list.sort(comparator.%compare%);}")
	public static <T> List<T> jsSortList(List<T> list, Comparator<T> comparator) {
		return null;
	}
	
	@NativeCode("{return isNaN(o);}")
	public static boolean isNaN(Object o) {
		return false;
	}
	
	/**
	 * removes an obj from the list
	 * @return new list
	 */
	public static <T> List<T> arrayRemove(List<T> list, T obj) {
		List<T> res = new ArrayList<T>();
		for(T o : list) if(o!=obj) res.add(o);
		return res;
	}
	
	public static String getCookie(String name) {
	    String dc = document.cookie;
	    String prefix = name + "=";
	    int begin = dc.indexOf("; " + prefix);
	    if (begin == -1) {
	        begin = dc.indexOf(prefix);
	        if (begin != 0) return null;
	    } else {
	        begin += 2;
	    }
	    int end = dc.indexOf(";", begin);
	    if (end == -1) {
	        end = dc.length();
	    }
	    return window.unescape(dc.substring(begin + prefix.length(), end));
	}
	
	public static void setCookie(String name, String value) {
		setCookieFull(name, value, null, null, null, false);
	}
	
	public static void setCookieFull(String name, String value, Date expires, String path, String domain, boolean secure) {
		if(null==expires) {
			expires = new Date();
			expires.setYear(expires.getYear() + 10);
		}
//		alertWindow(getDocument().cookie);
		document.cookie = name + "=" + window.escape(value) +
			("; expires=" + expires.toGMTString()) +
			(path!=null ? "; path=" + path : "") +
			(domain!=null ? "; domain=" + domain : "") +
			(secure ? "; secure" : "");

	}
	
	@NativeCode("{var iid=setInterval(function(){cb.%voidDelegate%(iid);}, millis);return iid;};")
	public static IntervalIdentifier setInterval(VoidDelegate<IntervalIdentifier> cb, int millis) {
		return null;
	}

	@NativeCode("{var iid=setTimeout(function(){cb.%voidDelegate%(iid);}, millis);return iid;};")
	public static TimeoutIdentifier setTimeout(VoidDelegate<TimeoutIdentifier> cb, int millis) {
		return null;
	}

	public static void clearInterval(IntervalIdentifier intervalId) {
		window.clearInterval(intervalId);
	}

	public static void clearTimeout(TimeoutIdentifier timeoutId) {
		window.clearTimeout(timeoutId);
	}
	
	@NativeCode("{return typeof obj;}")
	public static String typeOf(Object obj) {
		return null;
	}

	@NativeCode("{return obj._isEnum;}")
	public static boolean isEnum(Object obj) {
		return false;
	}

	@NativeCode("{return obj instanceof Date;}")
	public static boolean isInstanceOfDate(Object obj) {
		return false;
	}

	@NativeCode("{return obj instanceof Date || (typeof obj.length != 'undefined' && typeof obj['0'] != 'undefined');}")
	public static boolean isInstanceOfArray(Object obj) {
		return false;
	}
	
	@NativeCode("{return obj.tagName;}")
	public static boolean isInstanceOfNode(Object obj) {
		return false;
	}
	
	public static String dateToString(Date d) {
		if(null==d) return "";
		return (d.getDate()<10?"0":"") + d.getDate() + '.' + (d.getMonth()<9?"0":"") + (d.getMonth()+1) +'.' + d.getYear();
	}

	public static String dateTimeToString(Date d) {
		if(null==d) return "";
		return dateToString(d) + " " + (d.getHours()<10?"0":"") + d.getHours()+":"+(d.getMinutes()<10?"0":"")+d.getMinutes();
	}
	
	public static String dateToStringSmart(Date d) {
		if(null==d) return "";
		return d.getHours()==0 && d.getMinutes()==0 ? 
			dateToString(d) : dateTimeToString(d);
	}

	@NativeCode("{return list.join(sep);}")
	public static String listJoin(Object list, String sep) {
		return null;
	}

	public static void scrollToVisible(Node n) {
	    int top=0;
        for(; n!=null;n = n.offsetParent) {
            top  += n.offsetTop;
        }
        window.scroll(0, top);
	}
	
	@NativeCode("{return {};}")
	public static <K, V> Map<K, V> newJsMap() {return null;}
	
	@NativeCode("{delete map[key];}")
	public static <K, V> void removeFromMap(Map<K, V> map, K key) {}
	
    @NativeCode("{return {};}")
    public static <V> Set<V> newJsSet() {return null;}

    @NativeCode("{return [];}")
    public static <V> List<V> newJsList() {return null;}
}
