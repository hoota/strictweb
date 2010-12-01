package ru.yandex.strictweb.ajaxtools.presentation;

import java.util.regex.Pattern;


public class JsonPresentation extends AbstractPresentation {
	static Pattern hashKeyPattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
	
	public static String present(Object o) throws Exception {
		JsonPresentation js = new JsonPresentation();
		return js.toString(o);
	}
	
	@Override
	boolean hashBegin(String key, Object x) {
		if(key == null) buf.append("{");
		else buf.append(safeKey(key) + "{");
		return true;
	}

	@Override
	void hashEnd(String key) {
		buf.append("}");
	}

	@Override
	boolean listBegin(String key, Object x) {
		if(key == null) buf.append("[");
		else buf.append(safeKey(key)+"[");
		return true;
	}

	@Override
	void listEnd(String key) {
		buf.append("]");
	}

	@Override
	void addSeparator() {
		buf.append(',');
	}

	@Override
	void presentString(String key, String val, boolean forceItem) {
		if(key == null) buf.append(safe(val));
		else buf.append(safeKey(key)+safe(val));
	}
	
	@Override
	void presentNull(String key, boolean forceItem) {
		if(key == null) buf.append("null");
		else buf.append(safeKey(key)+"null");		
	}

	@Override
	void presentNumber(String key, String val, boolean forceItem) {
		if(key == null) buf.append(val);
		else buf.append(safeKey(key) + val);
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
