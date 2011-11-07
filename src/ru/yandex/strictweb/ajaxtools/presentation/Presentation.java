package ru.yandex.strictweb.ajaxtools.presentation;

public interface Presentation {

    public String toString(String rootKey, Object o) throws Exception;
    public String toString(Object o) throws Exception;

    public void present(Appendable out, String rootKey, Object o) throws Exception;
    public void present(Appendable out, Object o) throws Exception;
    
	public Presentation setDateFormat(DateTimeFormat dateFormat);
	public DateTimeFormat getDateFormat();

	public Presentation forceEnumsAsClasses(boolean forceEnumsAsClasses);
	public boolean isEnumsAsClasses();
}