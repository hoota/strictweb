package ru.yandex.strictweb.ajaxtools.presentation;

public interface Presentation {

    public String toString(String rootKey, Object o) throws Exception;
    public String toString(Object o) throws Exception;
    
	public void setDateFormat(DateTimeFormat dateFormat);
	public DateTimeFormat getDateFormat();

	public void forceEnumsAsClasses(boolean forceEnumsAsClasses);
	public boolean isEnumsAsClasses();
}