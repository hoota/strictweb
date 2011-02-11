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
		else buf.append(safeKey(key)).append("{");
		return true;
	}

	@Override
    void hashEnd(String key, Object x) throws IOException {
        buf.append("}");
    }

	@Override
    boolean listBegin(String key, Object x) throws IOException {
        if(key == null) buf.append("[");
        else buf.append(safeKey(key)).append("[");
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
        if(key == null) buf.append(safe(val));
        else buf.append(safeKey(key)).append(safe(val));
    }
	
	@Override
    void presentNull(String key, boolean forceItem) throws IOException {
        if(key == null) buf.append("null");
        else buf.append(safeKey(key)).append("null");       
    }

	@Override
    void presentNumber(String key, String val, boolean forceItem) throws IOException {
        if(key == null) buf.append(val);
        else buf.append(safeKey(key)).append(val);
    }

	static Pattern slashPattern = Pattern.compile("\\\\");
	static Pattern quotePattern = Pattern.compile("'");
	static Pattern nPattern = Pattern.compile("\n");
	static Pattern rPattern = Pattern.compile("\r");
	
	public static String _safe(String s) {
		s = slashPattern.matcher(s).replaceAll("\\\\\\\\");
		s = quotePattern.matcher(s).replaceAll("\\\\'");
		s = nPattern.matcher(s).replaceAll("\\\\n");
		return '\''+rPattern.matcher(s).replaceAll("\\\\r")+'\'';
	}
	
	public static String _safeKey(String key) {
		return (hashKeyPattern.matcher(key).matches() ? key : _safe(key)) + ":";
	}
	
	public String safe(String s) {
		return _safe(s);
	}
	
	public String safeKey(String key) {
		return _safe(key) + ":";
	}
}
