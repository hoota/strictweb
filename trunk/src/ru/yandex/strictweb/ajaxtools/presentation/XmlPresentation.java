package ru.yandex.strictweb.ajaxtools.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class XmlPresentation extends AbstractPresentation {	
	
    public XmlPresentation() {
    }
    
    public XmlPresentation(Appendable writer) {
        buf = writer;
    }
    
	public static String present(Object o) throws Exception {
		XmlPresentation xml = new XmlPresentation();
		return xml.toString(o);
	}
	
	@Override
    boolean hashBegin(String key, Object x) throws IOException {
        if(key == null) buf.append("<").append(x.getClass().getSimpleName()).append(">");
        else if(isXmlName(key)) buf.append("<").append(key).append(">");
        else buf.append("<object name=\"").append(safe(key)).append("\">");
        return true;
    }

	@Override
    void hashEnd(String key, Object x) throws IOException {
        if(isXmlName(key)) buf.append("</").append(key).append(">");
        else buf.append("</").append(x.getClass().getSimpleName()).append(">");
    }

	@Override
    boolean listBegin(String key, Object x) throws IOException {
        if(key == null) buf.append("<list>");
        else if(isXmlName(key)) buf.append("<").append(key).append(">");
        else buf.append("<list name=\"").append(safe(key)).append("\">");
        return true;
    }

	@Override
    void listEnd(String key) throws IOException {
        if(isXmlName(key)) buf.append("</").append(key).append(">");
        else buf.append("</list>");
    }

	@Override
	void addSeparator() {
	}

	@Override
    void presentString(String key, String val, boolean forceItem) throws IOException {
        if(key == null) buf.append("<str>").append(safe(val)).append("</str>");
        else if(isXmlName(key) && !forceItem) buf.append("<").append(key).append(">").append(safe(val)).append("</").append(key).append(">");
        else buf.append("<str name=\"").append(safe(key)).append("\">").append(safe(val)).append("</str>");
    }
    
    @Override
    void presentNull(String key, boolean forceItem) throws IOException {
        if(key == null) buf.append("<null/>");
        else if(isXmlName(key) && !forceItem) buf.append("<").append(key).append(" null=\"1\"/>");
        else buf.append("<null name=\"").append(safe(key)).append("\"/>");
    }

	@Override
    void presentNumber(String key, String val, boolean forceItem) throws IOException {
        if(key == null) buf.append("<num>").append(safe(val)).append("</num>");
        else if(isXmlName(key) && !forceItem) buf.append("<").append(key).append(">").append(safe(val)).append("</").append(key).append(">");
        else buf.append("<num name=\"").append(safe(key)).append("\">").append(safe(val)).append("</num>");
    }
	
	static final Pattern xmlNamePattern = Pattern.compile("[a-z0-9][a-z0-9_]*", Pattern.CASE_INSENSITIVE);
	static final Pattern ampPattern = Pattern.compile("&");
	static final Pattern quotePattern = Pattern.compile("\"");
	static final Pattern ltPattern = Pattern.compile("<");
	static final Pattern gtPattern = Pattern.compile(">");

	public static boolean isXmlName(String s) {
		return s==null ? false : xmlNamePattern.matcher(s).matches();
	}
	
	public static String safe(String s) {
		s = quotePattern.matcher(s).replaceAll("&quot;");
		s = ltPattern.matcher(s).replaceAll("&lt;");
		s = ampPattern.matcher(s).replaceAll("&amp;");
		return gtPattern.matcher(s).replaceAll("&gt;");
	}
}

