package ru.yandex.strictweb.ajaxtools.presentation;

import java.util.regex.Pattern;

public class JsonParanoidPresentation extends JsonPresentation {
	static Pattern dblQuotePattern = Pattern.compile("\"");
	
	@Override
	public String safe(String s) {
		s = slashPattern.matcher(s).replaceAll("\\\\\\\\");
		s = dblQuotePattern.matcher(s).replaceAll("\\\\\"");
		s = nPattern.matcher(s).replaceAll("\\\\n");
		return '"'+rPattern.matcher(s).replaceAll("\\\\r")+'"';
	}
	
	@Override
	public String safeKey(String key) {
		return safe(key) + ":";
	}
}
