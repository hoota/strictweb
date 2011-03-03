package ru.yandex.strictweb.scriptjava.base.util;

import ru.yandex.strictweb.scriptjava.base.CommonElements;
import ru.yandex.strictweb.scriptjava.base.Node;
import ru.yandex.strictweb.scriptjava.base.ScriptJava;

public class Log extends CommonElements {
	public static String logDivId = "log-output";
	
	public static void info(Object msg) {
		if(ScriptJava.console!=null) ScriptJava.console.info(msg);
		Node ld = $$(logDivId);
		if(ld == null) return;
		ld.appendChild($DIV().className("info").innerHTML(msg.toString()).node);
	}

	public static void error(Object msg) {
		if(ScriptJava.console!=null) ScriptJava.console.error(msg);
        Node ld = $$(logDivId);
        if(ld == null) return;
        ld.appendChild($DIV().className("error").innerHTML(msg.toString()).node);
	}

	public static void warn(Object msg) {
		if(ScriptJava.console!=null) ScriptJava.console.warn(msg);
        Node ld = $$(logDivId);
        if(ld == null) return;
        ld.appendChild($DIV().className("warn").innerHTML(msg.toString()).node);
	}
	
	public static void debug(Object msg) {
		if(ScriptJava.console!=null) ScriptJava.console.debug(msg);
        Node ld = $$(logDivId);
        if(ld == null) return;
        ld.appendChild($DIV().className("debug").innerHTML(msg.toString()).node);
	}

	public static void makeLogDiv() {
		ScriptJava.document.write("<div id='"+logDivId+"' style='border: 1px solid green; margin-top: 100px; height: 100px; overflow: auto;'/>");
	}
}
