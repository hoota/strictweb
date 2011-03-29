package ru.yandex.strictweb.ajaxtools.presentation;

public class SimpleToStringPresentation implements Presentation {

    Appendable buf;
    
    public SimpleToStringPresentation(Appendable buf) {
        this.buf = buf == null ? new StringBuilder() : buf;
    }
    
    @Override
    public Presentation forceEnumsAsClasses(boolean forceEnumsAsClasses) {
    	return this;
    }

    @Override
    public DateTimeFormat getDateFormat() {
        return null;
    }

    @Override
    public boolean isEnumsAsClasses() {
        return false;
    }

    @Override
    public Presentation setDateFormat(DateTimeFormat dateFormat) {
    	return this;
    }

    @Override
    public String toString(String rootKey, Object o) throws Exception {
       buf.append(o.toString());
       return buf instanceof StringBuilder ? buf.toString() : null;
    }

    @Override
    public String toString(Object o) throws Exception {
        return toString(null, o);
    }    
}
