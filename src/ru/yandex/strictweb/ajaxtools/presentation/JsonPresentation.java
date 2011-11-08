package ru.yandex.strictweb.ajaxtools.presentation;

import java.io.IOException;
import java.util.regex.Pattern;


public class JsonPresentation extends AbstractPresentation {
	static Pattern hashKeyPattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
	
	public static String present(Object o) throws Exception {
		JsonPresentation js = new JsonPresentation();
		return js.toString(o);
	}
	
	@Override
	public void present(Appendable out, String rootKey, Object o) throws Exception {
	    if(rootKey != null) out.append('{');
	    super.present(out, rootKey, o);
	    if(rootKey != null) out.append('}');
	}
	
	@Override
	boolean hashBegin(String key, Object x) throws IOException {
		if(key == null) out.append("{");
		else safeKey(key).append("{");
		return true;
	}

	@Override
    void hashEnd(String key, Object x) throws IOException {
        out.append("}");
    }

	@Override
    boolean listBegin(String key, Object x) throws IOException {
        if(key == null) out.append("[");
        else safeKey(key).append("[");
        return true;
    }

	@Override
    void listEnd(String key) throws IOException {
        out.append("]");
    }

	@Override
    void addSeparator() throws IOException {
        out.append(',');
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
        if(key == null) out.append("null");
        else safeKey(key).append("null");       
    }

	@Override
    void presentNumber(String key, String val, boolean forceItem) throws IOException {
        if(key == null) out.append(val);
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
		return staticSafe(out, s);
	}
	
	public Appendable safeKey(String key) throws IOException {
	    return staticSafeKey(out, key);
	}
}
