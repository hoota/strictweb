package ru.yandex.strictweb.ajaxtools.presentation;

import java.io.IOException;
import java.util.regex.Pattern;

public class XmlPresentation extends AbstractPresentation {	
	
	public static String present(Object o) throws Exception {
		XmlPresentation xml = new XmlPresentation();
		return xml.toString(o);
	}
	
	@Override
    boolean hashBegin(String key, Object x) throws IOException {
        if(key == null) out.append("<").append(x.getClass().getSimpleName()).append(">");
        else if(isXmlName(key)) out.append("<").append(key).append(">");
        else out.append("<object name=\"").append(safe(key)).append("\">");
        return true;
    }

	@Override
    void hashEnd(String key, Object x) throws IOException {
        if(isXmlName(key)) out.append("</").append(key).append(">");
        else out.append("</").append(x.getClass().getSimpleName()).append(">");
    }

	@Override
    boolean listBegin(String key, Object x) throws IOException {
        if(key == null) out.append("<list>");
        else if(isXmlName(key)) out.append("<").append(key).append(">");
        else out.append("<list name=\"").append(safe(key)).append("\">");
        return true;
    }

	@Override
    void listEnd(String key) throws IOException {
        if(isXmlName(key)) out.append("</").append(key).append(">");
        else out.append("</list>");
    }

	@Override
	void addSeparator() {
	}

	@Override
    void presentString(String key, String val, boolean forceItem) throws IOException {
        if(key == null) out.append("<str>").append(safe(val)).append("</str>");
        else if(isXmlName(key) && !forceItem) out.append("<").append(key).append(">").append(safe(val)).append("</").append(key).append(">");
        else out.append("<str name=\"").append(safe(key)).append("\">").append(safe(val)).append("</str>");
    }
    
    @Override
    void presentNull(String key, boolean forceItem) throws IOException {
        if(key == null) out.append("<null/>");
        else if(forceItem) {
            if(isXmlName(key)) out.append("<null name=\"").append(safe(key)).append("\"/>");
            else out.append("<null name=\"").append(safe(key)).append("\"/>");
        }
    }

	@Override
    void presentNumber(String key, String val, boolean forceItem) throws IOException {
        if(key == null) out.append("<num>").append(safe(val)).append("</num>");
        else if(isXmlName(key) && !forceItem) out.append("<").append(key).append(">").append(safe(val)).append("</").append(key).append(">");
        else out.append("<num name=\"").append(safe(key)).append("\">").append(safe(val)).append("</num>");
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
	    int len = s.length();
	    StringBuilder bb = new StringBuilder(len);
	    for(int i=0; i<len; i++) {
	        char c = s.charAt(i);
	        if(c == '"') bb.append("&quot;");
	        else if(c == '&') bb.append("&amp;");
            else if(c == '<') bb.append("&lt;");
            else if(c == '>') bb.append("&gt;");
            else bb.append(c);
	    }
	    return bb.toString();
//		s = quotePattern.matcher(s).replaceAll("&quot;");
//		s = ampPattern.matcher(s).replaceAll("&amp;");
//		s = ltPattern.matcher(s).replaceAll("&lt;");
//		return gtPattern.matcher(s).replaceAll("&gt;");
	}
}

