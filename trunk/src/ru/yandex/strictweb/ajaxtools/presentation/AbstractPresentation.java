package ru.yandex.strictweb.ajaxtools.presentation;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandex.strictweb.ajaxtools.presentation.ClassMethodsInfo.Property;

public abstract class AbstractPresentation implements Presentation {
	
	static NumberFormat[] numberFormats = new NumberFormat[20];

	static {
		for(int i=0; i<numberFormats.length; i++) {
			numberFormats[i] = NumberFormat.getInstance(java.util.Locale.US);
			numberFormats[i].setGroupingUsed(false);
			numberFormats[i].setMaximumFractionDigits(i);
		}
	}
	
	NumberFormat numberFormat = numberFormats[6];
	boolean forceEnumsAsClasses = false;
	DateTimeFormat dateFormat = DateTimeFormat.DATE;
	Appendable out;

	boolean presentEntity(Object o, boolean first) throws Exception {

		List<Property> properties = ClassMethodsInfo.getPresentableProperties(o.getClass());
		
		for(Property prop : properties) {
			if(!first) addSeparator();
			DateTimeFormat dFormat = dateFormat;
			
			if(prop.dateFormat != DateTimeFormat.UNDEF) {
				dateFormat = prop.dateFormat;
			}
			numberFormat = numberFormats[prop.fractionDigits];
			presentOne(
				prop.getName(), 
				prop.getObject(o),
				false
			);
			dateFormat = dFormat;
			first = false;
		}
		return first;
	}
	
	@Override
	public void present(Appendable out, Object o) throws Exception {
		present(out, null, o);
	}
	
	@Override
	public void present(Appendable out, String rootKey, Object o) throws Exception {
		this.out = out;
        presentOne(rootKey, o, false);		
	}
	
    public final String toString(Object o) throws Exception {
        return toString(null, o);
    }
    
    public final String toString(String rootKey, Object o) throws Exception {
    	StringBuilder buf = new StringBuilder();
    	present(buf, rootKey, o);
    	return buf.toString();
    }   
	
	void presentOne(String key, Object o, boolean forceItem) throws Exception {
		boolean first = true;
		if(o == null) {
			presentNull(key, forceItem);
		} else if(o instanceof Number) {
			presentNumber(key, numberFormat.format(o), forceItem);
		} else if(o instanceof String) {
			presentString(key, o.toString(), forceItem);
		} else if(o  instanceof java.util.Date) {
			String val = String.format(dateFormat.format, o);
			if(dateFormat.quote) presentString(key, val, forceItem);
			else presentNumber(key, val, forceItem);
		} else if(o instanceof Object[]) {
			listBegin(key, o);
			for(Object a : (Object[])o) {
				if(!first) addSeparator();
				presentOne(null, a, true);
				first = false;
			}
			listEnd(key);
		} else if(o instanceof Set) {
			if(!hashBegin(key, o)) return;
			for(Object a : (Set)o) {
				if(!first) addSeparator();
				presentNumber(a.getClass().isEnum() ? ((Enum)a).name() : a.toString(), "1", true);
				first = false;
			}
			hashEnd(key, o);
		} else if(o instanceof Iterable) {
			listBegin(key, o);
			for(Object a : (Iterable)o) {
				if(!first) addSeparator();
				presentOne(null, a, true);
				first = false;
			}
			listEnd(key);
		} else if(o instanceof Map) {
			if(!hashBegin(key, o)) return;
			for(Iterator<Map.Entry> i = ((Map)o).entrySet().iterator(); i.hasNext();) {
				Map.Entry e = i.next();
				if(!first) addSeparator();
				Object a = e.getKey();
                presentOne(a.getClass().isEnum() ? ((Enum)a).name() : a.toString(), e.getValue(), true);
				first = false;
			}
			hashEnd(key, o);
		} else if(o instanceof Boolean) {
			presentNumber(key, ((Boolean)o).booleanValue() ? "1" : "0", forceItem);
		} else if(o instanceof Enum) {
			if(forceEnumsAsClasses) {
				presentNumber(key, o.getClass().getSimpleName()+"."+((Enum)o).name(), forceItem);
			} else {
				presentString(key, ((Enum)o).name(), forceItem);				
			}
		} else if(ClassMethodsInfo.isPresentableOrEntity(o.getClass())) {
            if(!hashBegin(key, o)) return;
            presentEntity(o, true);
            hashEnd(key, o);
        } else {
			presentString(key, o.toString(), forceItem);
		}				
	}
	
    abstract boolean hashBegin(String key, Object x) throws IOException;
    abstract void hashEnd(String key, Object x) throws IOException;
    
    abstract boolean listBegin(String key, Object x) throws IOException;
    abstract void listEnd(String key) throws IOException;
    
    abstract void addSeparator() throws IOException;
    
    abstract void presentString(String key, String val, boolean forceItem) throws IOException;
    abstract void presentNull(String key, boolean forceItem) throws IOException;
    abstract void presentNumber(String key, String val, boolean forceItem) throws IOException;

	public DateTimeFormat getDateFormat() {
		return dateFormat;
	}

	public Presentation setDateFormat(DateTimeFormat dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public boolean isEnumsAsClasses() {
		return forceEnumsAsClasses;
	}

	public Presentation forceEnumsAsClasses(boolean forceEnumsAsClasses) {
		this.forceEnumsAsClasses = forceEnumsAsClasses;
		return this;
	}
}
