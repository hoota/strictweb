package ru.yandex.strictweb.ajaxtools.presentation;

import java.io.IOException;
import java.util.regex.Pattern;


public class JsonPresentation extends AbstractPresentation {
	static Pattern hashKeyPattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
	
    public JsonPresentation() {
    }
    
    public JsonPresentation(Appendable writer) {
        buf = writer;
    }	
	
	public static String present(Object o) throws Exception {
		JsonPresentation js = new JsonPresentation();
		return js.toString(o);
	}
	
	public String toString(String rootKey, Object o) throws Exception {
	    boolean returnStr = buf == null;
	    if(buf == null) buf = new StringBuilder();

	    if(rootKey != null) buf.append('{');
	    presentOne(rootKey, o, false);
	    if(rootKey != null) buf.append('}');

	    return returnStr ? buf.toString() : null;
	}
	
	@Override
	boolean hashBegin(String key, Object x) throws IOException {
		if(key == null) buf.append("{");
		else safeKey(key).append("{");
		return true;
	}

	@Override
    void hashEnd(String key, Object x) throws IOException {
        buf.append("}");
    }

	@Override
    boolean listBegin(String key, Object x) throws IOException {
        if(key == null) buf.append("[");
        else safeKey(key).append("[");
        return true;
    }

	@Override
    void listEnd(String key) throws IOException {
        buf.append("]");
    }

	@Override
    void addSeparator() throws IOException {
        buf.append(',');
    }

	@Override
    void presentString(String key, String val, boolean forceItem) throws IOException {
        if(key == null) safe(val);
        else {
            safeKey(key);
            safe(val);
        }
    }
	
	@Override
    void presentNull(String key, boolean forceItem) throws IOException {
        if(key == null) buf.append("null");
        else safeKey(key).append("null");       
    }

	@Override
    void presentNumber(String key, String val, boolean forceItem) throws IOException {
        if(key == null) buf.append(val);
        else safeKey(key).append(val);
    }

	static Pattern slashPattern = Pattern.compile("\\\\");
	static Pattern quotePattern = Pattern.compile("'");
	static Pattern nPattern = Pattern.compile("\n");
	static Pattern rPattern = Pattern.compile("\r");
	
	public static Appendable staticSafe(Appendable b, String s) throws IOException {
		s = slashPattern.matcher(s).replaceAll("\\\\\\\\");
		s = quotePattern.matcher(s).replaceAll("\\\\'");
		s = nPattern.matcher(s).replaceAll("\\\\n");
		return b.append('\'').append(rPattern.matcher(s).replaceAll("\\\\r")).append('\'');		
	}
	
	public static Appendable staticSafeKey(Appendable b, String key) throws IOException {
	    if(hashKeyPattern.matcher(key).matches()) b.append(key);
	    else staticSafe(b, key);
	    return b.append(':');
	}
	
	public Appendable safe(String s) throws IOException {
		return staticSafe(buf, s);
	}
	
	public Appendable safeKey(String key) throws IOException {
	    return staticSafeKey(buf, key);
	}
}
