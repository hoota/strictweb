package ru.yandex.strictweb.ajaxtools.presentation;

import java.util.Date;

public enum DateTimeFormat {
	UNDEF("", true),
	DATE("%1$td.%1$tm.%1$tY", true),
	TIME("%1$tR", true),
	DATETIME("%1$td.%1$tm.%1$tY %1$tR", true),
	DATE_YMD("%1$tY.%1$tm.%1$td", true),
	EPOCH("new Date(%1$tQ)", false);
	
	public final String format;
	public final boolean quote;
	
	private DateTimeFormat(String format, boolean q) {
		this.format = format;
		this.quote  = q;
	}
	
	public String make(Date d) {
		return String.format(format, d);
	}
	
	public static String smartFormat(Date d) {
		if(d.getHours()==0 && d.getMinutes()==0) {
			return DATE.make(d);
		} else {
			return DATETIME.make(d);
		}
	}
}