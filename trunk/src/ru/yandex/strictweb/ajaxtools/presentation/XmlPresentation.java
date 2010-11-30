package ru.yandex.strictweb.ajaxtools.presentation;

import java.util.regex.Pattern;

public class XmlPresentation extends AbstractPresentation {	
	
	public static String present(Object o) throws Exception {
		XmlPresentation xml = new XmlPresentation();
		return xml.toString(o);
	}
	
	@Override
	boolean hashBegin(String key, Object x) {
		if(key == null) buf.append("<object>");
		else if(isXmlName(key)) buf.append("<" + key + ">");
		else buf.append("<object name=\""+safe(key)+"\">");
		return true;
	}

	@Override
	void hashEnd(String key) {
		if(isXmlName(key)) buf.append("</" + key + ">");
		else buf.append("</object>");
	}

	@Override
	boolean listBegin(String key, Object x) {
		if(key == null) buf.append("<list>");
		else if(isXmlName(key)) buf.append("<" + key + ">");
		else buf.append("<list name=\""+safe(key)+"\">");
		return true;
	}

	@Override
	void listEnd(String key) {
		if(isXmlName(key)) buf.append("</" + key + ">");
		else buf.append("</list>");
	}

	@Override
	void addSeparator() {
	}

	@Override
	void presentString(String key, String val, boolean forceItem) {
		if(key == null) buf.append("<str>"+safe(val)+"</str>");
		else if(isXmlName(key) && !forceItem) buf.append("<"+key+">"+safe(val)+"</"+key+">");
		else buf.append("<str name=\""+safe(key)+"\">"+safe(val)+"</str>");
	}
	
	@Override
	void presentNull(String key, boolean forceItem) {
		if(key == null) buf.append("<null/>");
		else if(isXmlName(key) && !forceItem) buf.append("<"+key+" null=\"1\"/>");
		else buf.append("<null name=\""+safe(key)+"\"/>");		
	}

	@Override
	void presentNumber(String key, String val, boolean forceItem) {
		if(key == null) buf.append("<num>"+safe(val)+"</num>");
		else if(isXmlName(key) && !forceItem) buf.append("<"+key+">"+safe(val)+"</"+key+">");
		else buf.append("<num name=\""+safe(key)+"\">"+safe(val)+"</num>");
	}
	
	static final Pattern xmlNamePattern = Pattern.compile("[a-z0-9][a-z0-9_]*", Pattern.CASE_INSENSITIVE);
	static final Pattern quotePattern = Pattern.compile("\"");
	static final Pattern ltPattern = Pattern.compile("<");
	static final Pattern gtPattern = Pattern.compile(">");

	public static boolean isXmlName(String s) {
		return s==null ? false : xmlNamePattern.matcher(s).matches();
	}
	
	public static String safe(String s) {
		s = quotePattern.matcher(s).replaceAll("&quot;");
		s = ltPattern.matcher(s).replaceAll("&lt;");
		return gtPattern.matcher(s).replaceAll("&gt;");
	}
}

