package ru.yandex.strictweb.ajaxtools.presentation;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandex.strictweb.ajaxtools.presentation.ClassMethodsInfo.Property;

/**
 * Сериализация в JSON, учитывающая ссылки.
 * Может сериализовывать объекты, рекурсивно ссылающиеся друг на друга.
 * @author Andrey Stroganov (hoota@)
 *
 */
public class JsonRefPresentation implements Presentation {
	boolean enumsAsClasses = true;
	DateTimeFormat dateFormat = DateTimeFormat.EPOCH;
	
	int objectIndex = 0;
	Map<Object, Integer> references = new IdentityHashMap<Object, Integer>();
	
	LinkedList<Integer> stack = new LinkedList<Integer>();
	
	NumberFormat numberFormat;
	
	private int currentIndex;
	private Appendable out;
	private StringBuilder assign;
	
	public String toString(Object o) throws Exception {
        return toString(null, o);
    }
	
	@Override
	public void present(Appendable out, Object o) throws Exception {
		present(out, null, o);
	}
	
	@Override
	public void present(Appendable out, String rootKey, Object o) throws Exception {
	    this.out = out;
	    
        out.append("var o={};\n");
        
        assign = new StringBuilder();
        
        presentOne(rootKey, o, -1);
        
        out.append(";\n").append(assign);
	}
    
    @Override
    public String toString(String rootKey, Object o) throws Exception {
    	StringBuilder buf = new StringBuilder();
    	present(buf, rootKey, o);
    	return buf.toString();
    }
	
	void objectBegin(String key, Object x, char bracket) throws IOException {
		int ci = currentIndex;
		currentIndex = objectIndex++;
		
		references.put(x, currentIndex);
		
		if(key == null) {
			objectName(out, currentIndex);
		} else {
			JsonPresentation.staticSafeKey(out, key);
			objectName(out, currentIndex);
		}

		stack.addLast(ci);

		out.append('=').append(bracket);
	}
	
	void objectEnd(String key, char bracket) throws IOException {
		out.append(bracket);
		currentIndex = stack.pollLast();
	}
	
	boolean presentEntity(Object o, boolean first) throws Exception {

		List<Property> properties = ClassMethodsInfo.getPresentableProperties(o.getClass());
		
		for(Property prop : properties) {
			if(!first) addSeparator();
			DateTimeFormat dFormat = dateFormat;
			
			if(prop.dateFormat != DateTimeFormat.UNDEF) {
				dateFormat = prop.dateFormat;
			}
			
			numberFormat = AbstractPresentation.numberFormats[prop.fractionDigits];
			
			presentOne(
				prop.getName(), 
				prop.getObject(o),
				-1
			);
			dateFormat = dFormat;
			first = false;
		}
		return first;
	}
	
	boolean presentSimpleOne(String key, Object o) throws IOException {
		if(o == null) {
			presentNull(key);
			return true;
		} else if(o instanceof Number) {
		    if(o instanceof Double) {
                Double d = (Double)o;
                if(d.isNaN()) {presentNumber(key, "NaN");return true;}
                if(d.isInfinite()) {presentNumber(key, (d<0?"-":"") + "Infinite"); return true;}
            } else if(o instanceof Float) {
                Float f = (Float)o;
                if(f.isNaN()) {presentNumber(key, "NaN");return true;}
                if(f.isInfinite()) {presentNumber(key, (f<0?"-":"") + "Infinite"); return true;}
            }
	        
			presentNumber(key, numberFormat.format(o));
			return true;
		} else if(o instanceof String) {
			presentString(key, o.toString());
			return true;
		} else if(o instanceof java.util.Date) {
			String val = String.format(dateFormat.format, o);
			if(dateFormat.quote) presentString(key, val);
			else presentNumber(key, val);
			return true;
		} else if(o instanceof Boolean) {
			presentNumber(key, ((Boolean)o).booleanValue() ? "1" : "0");
			return true;
		} else if(o instanceof Enum) {
			if(enumsAsClasses) {
				presentNumber(key, o.getClass().getSimpleName()+"."+((Enum)o).name());
			} else {
				presentString(key, ((Enum)o).name());				
			}
			return true;
		}	
		
		return false;
	}

	void presentOne(String key, Object o, int arrayIndex) throws Exception {
//		System.out.println(key + " :: " + o);
		boolean first = true;
		
		if(presentSimpleOne(key, o)) return;
		if(objectExists(key, o, arrayIndex)) return;
		
		if(o instanceof Object[]) {
			objectBegin(key, o, '[');
			int ai = 0;
			for(Object a : (Object[])o) {
				if(!first) addSeparator();
				presentOne(null, a, ai++);
				first = false;
			}
			objectEnd(key, ']');
		} else if(o instanceof Set) {
			objectBegin(key, o, '{');
			for(Object a : (Set)o) {
				if(!first) addSeparator();				
				presentNumber(a.getClass().isEnum() ? ((Enum)a).name() : a.toString(), "1");
				first = false;
			}
			objectEnd(key, '}');
		} else if(o instanceof Iterable) {
			objectBegin(key, o, '[');
			int ai = 0;
			for(Object a : (Iterable)o) {
				if(!first) addSeparator();
				presentOne(null, a, ai++);
				first = false;
			}
			objectEnd(key, ']');
		} else if(o instanceof Map) {
			objectBegin(key, o, '{');
			for(Iterator<Map.Entry> i = ((Map)o).entrySet().iterator(); i.hasNext();) {
				Map.Entry e = i.next();
				if(!first) addSeparator();
				Object a = e.getKey();
                presentOne(a.getClass().isEnum() ? ((Enum)a).name() : a.toString(), e.getValue(), -1);
				first = false;
			}
			objectEnd(key, '}');
		} else if(o.getClass().isArray()) {
			// TODO: сделать вынос в переменную
			if(o instanceof int[]) {
				presentNumber(key, Arrays.toString((int[])o));
			} else if(o instanceof double[]) {
				presentNumber(key, Arrays.toString((double[])o));
			} else if(o instanceof long[]) {
				presentNumber(key, Arrays.toString((long[])o));
			} else {
				throw new RuntimeException("Arrays of this simple type are not yet implemented :(");
			}
		} else if(ClassMethodsInfo.isPresentableOrEntity(o.getClass())) {
            objectBegin(key, o, '{');
            presentEntity(o, true);
            objectEnd(key, '}');
        } else {
			presentString(key, o.toString());
		}				
	}

	private boolean objectExists(String key, Object o, int arrayIndex) throws IOException {
		Integer n = references.get(o);
		
		if(n!=null) {
			if(key != null) {
				if(currentIndex < n) {
					JsonPresentation.staticSafeKey(out, key);
					objectName(out, n);
					return true;
				}
				
				objectName(assign, currentIndex);
				if(JsonPresentation.hashKeyPattern.matcher(key).matches()) {
					assign.append('.').append(key);
				} else {
					JsonPresentation.staticSafe(assign.append("['"), key).append("']");
				}
			} else {
				if(currentIndex < n) {
					objectName(out, n);
					return true;
				}

				out.append("null");
				objectName(assign, currentIndex);
				assign.append("[").append(arrayIndex).append("]");
			}
			
			assign.append('=');
			objectName(assign, n);
			assign.append(";");

//			if(out.length() > 0 && out.charAt(out.length()-1)==',') out.setLength(out.length() - 1);
			return true;
		}

		
		return false;
	}

	void presentString(String key, String val) throws IOException {
		if(key == null) JsonPresentation.staticSafe(out, val);
		else JsonPresentation.staticSafe(JsonPresentation.staticSafeKey(out, key), val);
	}
	
	void presentNull(String key) throws IOException {
		if(key == null) out.append("null");
		else JsonPresentation.staticSafeKey(out, key).append("null");		
	}

	void presentNumber(String key, String val) throws IOException {
		if(key == null) out.append(val);
		else JsonPresentation.staticSafeKey(out, key).append(val);
	}
	
	void addSeparator() throws IOException {
//		if(out.length() > 0) {
//			char c = out.charAt(out.length() - 1);
//			if(c==',' || c=='[' || c=='{') return;
//		}
		out.append(',');
	}
	
	public boolean isEnumsAsClasses() {
		return enumsAsClasses;
	}

	public Presentation setDateFormat(DateTimeFormat dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public DateTimeFormat getDateFormat() {
		return dateFormat;
	}

	public Presentation forceEnumsAsClasses(boolean forceEnumsAsClasses) {
		this.enumsAsClasses = forceEnumsAsClasses;
		return this;
	}
	
	final static char[] digits = {
		'a' , 'b' ,
		'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
		'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
		'o' , 'p' , 'q' , 'r' , 's' , 't' ,
		'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
		'A' , 'B' ,
		'C' , 'D' , 'E' , 'F' , 'G' , 'H' ,
		'I' , 'J' , 'K' , 'L' , 'M' , 'N' ,
		'O' , 'P' , 'Q' , 'R' , 'S' , 'T' ,
		'U' , 'V' , 'W' , 'X' , 'Y' , 'Z' ,
	};
	
	void objectName(Appendable b, int index) throws IOException {
		b.append("o._");
		do {
			b.append(digits[index%digits.length]);
			index /= digits.length;
		} while(index > 0);
	}
}
