package ru.yandex.strictweb.ajaxtools.presentation;

public class SimpleToStringPresentation implements Presentation {

    Appendable buf;
    
    public SimpleToStringPresentation(Appendable buf) {
        this.buf = buf == null ? new StringBuilder() : buf;
    }
    
    @Override
    public void forceEnumsAsClasses(boolean forceEnumsAsClasses) {
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
    public void setDateFormat(DateTimeFormat dateFormat) {
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
