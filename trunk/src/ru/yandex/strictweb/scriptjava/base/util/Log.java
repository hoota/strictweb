package ru.yandex.strictweb.scriptjava.base.util;

import ru.yandex.strictweb.scriptjava.base.CommonElements;
import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.ScriptJava;

public class Log extends CommonElements {
	public static String logDivId = "log-output";
	
	public static void info(String msg) {
		if(ScriptJava.console!=null) ScriptJava.console.info(msg);
		DOMBuilder ld = $(logDivId);
		if(ld == null) return;
		ld.add($DIV().className("info").innerHTML(msg));
	}

	public static void error(String msg) {
		if(ScriptJava.console!=null) ScriptJava.console.error(msg);
		DOMBuilder ld = $(logDivId);
		if(ld == null) return;
		ld.add($DIV().className("error").innerHTML(msg));
	}

	public static void warn(String msg) {
		if(ScriptJava.console!=null) ScriptJava.console.warn(msg);
		DOMBuilder ld = $(logDivId);
		if(ld == null) return;
		ld.add($DIV().className("warn").innerHTML(msg));
	}
	
	public static void debug(String msg) {
		if(ScriptJava.console!=null) ScriptJava.console.debug(msg);
		DOMBuilder ld = $(logDivId);
		if(ld == null) return;
		ld.add($DIV().className("debug").innerHTML(msg));
	}

	public static void makeLogDiv() {
		ScriptJava.document.write("<div id='"+logDivId+"' style='border: 1px solid green; margin-top: 100px; height: 100px; overflow: auto;'/>");
	}
}
