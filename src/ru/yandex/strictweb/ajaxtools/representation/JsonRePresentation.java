package ru.yandex.strictweb.ajaxtools.representation;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.yandex.strictweb.ajaxtools.exception.RePresentationException;
import ru.yandex.strictweb.ajaxtools.presentation.ClassMethodsInfo;
import ru.yandex.strictweb.ajaxtools.presentation.ClassMethodsInfo.Property;


public class JsonRePresentation {
	XmlRePresentation.EntityFinder ef;
	public final Yylex lexer = new Yylex((Reader)null);
	
	public JsonRePresentation(XmlRePresentation.EntityFinder ef) {
		this.ef = ef;
	}
	
	public JsonRePresentation reset(Reader input) {
		lexer.yyreset(input);
		return this;
	}
	
	public Object getObjectSimple(Class clazz) {
	    if(lexer.type == Yytoken.TYPE_EOF) {
	    	throw new RePresentationException("Bad json: eof instead of object " + clazz.getCanonicalName());
	    }
	    
	    if(lexer.type != Yytoken.TYPE_VALUE) {
	    	throw new RePresentationException("Bad json: not simple value for object " + clazz.getCanonicalName() + " : " + lexer.type);
	    }
	    
	    if(lexer.value == null) return null;
	    
		if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
			try {
				return Integer.parseInt(lexer.value);
			} catch (RuntimeException e) {
				throw new RePresentationException("Invalid int: " + lexer.value);
			}
		} else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
			try {
				return Long.parseLong(lexer.value);
			} catch (RuntimeException e) {
				throw new RePresentationException("Invalid long: " + lexer.value);
			}			
		} else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
			try {
				return Double.parseDouble(lexer.value);
			} catch (RuntimeException e) {
				throw new RePresentationException("Invalid double: " + lexer.value);
			}
        } else if(clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            try {
                return Byte.parseByte(lexer.value);
            } catch (RuntimeException e) {
                throw new RePresentationException("Invalid byte: " + lexer.value);
            }
        } else if(clazz.equals(Short.class) || clazz.equals(short.class)) {
            try {
                return Short.parseShort(lexer.value);
            } catch (RuntimeException e) {
                throw new RePresentationException("Invalid short: " + lexer.value);
            }
		} else if(clazz.equals(String.class)) {
			return lexer.value;
		} else if(clazz.isEnum()) {
		    return Enum.valueOf(clazz, lexer.value);
		} else if(clazz.equals(Timestamp.class) || clazz.equals(Date.class)) {
			long ts = XmlRePresentation.parseDate(lexer.value);
            return ts==-1 ? null : (clazz.equals(Date.class) ? new Date(ts) : new Timestamp(ts));
		} else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			String v = lexer.value;
		    return v.equals("1") || v.equalsIgnoreCase("y") || v.equalsIgnoreCase("true"); 
		}
		throw new RePresentationException(clazz);
	}
	
	public Object getObject(Class clazz, Type type) throws Exception {
	    lexer.yylex();
	    
	    if(lexer.type == Yytoken.TYPE_VALUE && lexer.value == null) return null;
		
		if(Map.class.isAssignableFrom(clazz)) {
		    return parseMap(clazz, type);
		} else if(Set.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)) {
			return parseListOrSet(clazz, type);
		} else if(ClassMethodsInfo.isPresentableOrEntity(clazz)) {
			return parseBean(clazz);
		} else return getObjectSimple(clazz);
		
	}

	private Object parseBean(Class clazz) throws InstantiationException, IllegalAccessException, IOException, ParseException, Exception {
		boolean isEntity = ClassMethodsInfo.isEntity(clazz);
//			System.out.println("isEntity " +clazz+ " :: " + isEntity);
		
		if(lexer.type == Yytoken.TYPE_VALUE) {
			if("new".equals(lexer.value)) return clazz.newInstance();
			
			if(clazz.isEnum()) {
			    return Enum.valueOf(clazz, lexer.value);
			}
		
			if(!isEntity) throw new RePresentationException("Cant create new instance of Presentable " + clazz.getCanonicalName()+"("+lexer.value+")");

			Class<?> entityIdClass = ClassMethodsInfo.getEntityIdClass(clazz);
			if(entityIdClass == null) throw new RePresentationException("Unknown @Id for Entity " + clazz.getCanonicalName());
			
			return ef.find(clazz, getObjectSimple(entityIdClass));
		}
		
		if(lexer.type != Yytoken.TYPE_LEFT_BRACE) throw new RePresentationException("Bad json: object is not map");
		
		Object result = null;
		List propertiesAndValues = new ArrayList(ClassMethodsInfo.getPresentableProperties(clazz).size());
		boolean skipIncoming = ClassMethodsInfo.getSkipIncoming(clazz);

		ClassMethodsInfo.Property idInfo = null;
		Class idClass = null;
		
		if(isEntity) {
			idInfo = ClassMethodsInfo.getEntityIdProperty(clazz);
			idClass = ClassMethodsInfo.getEntityIdClass(clazz);
		}
		
		for(;;) {
			lexer.yylex();
			if(lexer.type == Yytoken.TYPE_COMMA) continue;
			if(lexer.type == Yytoken.TYPE_RIGHT_BRACE) break;
			if(lexer.type != Yytoken.TYPE_VALUE) throw new RePresentationException("Bad json: map key expected " + lexer.type);
			String mName = getObjectSimple(String.class).toString();
            lexer.yylex();
            if(lexer.type != Yytoken.TYPE_COLON) throw new RePresentationException("Bad json: key:value separator expected");

			ClassMethodsInfo.Property mInfo = ClassMethodsInfo.getProperty(clazz, mName);
			if(mInfo == null) {
				if(skipIncoming) {lexer.yylex();continue;}
				throw new RePresentationException("Unknown property `"+mName+"` in class " + clazz);
			}
			if(!mInfo.canSet()) {
                if(skipIncoming) {lexer.yylex();continue;}
				throw new RePresentationException("Unknown setter property `"+mName+"` in class " + clazz);
			}

			Object value = getObject(mInfo.getReturnType(), mInfo.getGenericReturnType());
			propertiesAndValues.add(mInfo);
			propertiesAndValues.add(value);
			
			if(idInfo != null && idInfo.getName().equals(mName) && value != null) {
				result = ef.find(clazz, value);
			}
		}

		if(null == result) result = clazz.newInstance();
		
		for(int i=0, len=propertiesAndValues.size(); i<len; i+=2) {
			ClassMethodsInfo.Property mInfo = (Property) propertiesAndValues.get(i);
			Object value = propertiesAndValues.get(i+1);
			
			if(Collection.class.isAssignableFrom(mInfo.getReturnType())) {
				Collection col = (Collection)mInfo.getObject(result);
				if(null!=col) {
					col.clear();
					col.addAll((Collection)value);
					value = col;
				}
			}
			
			//System.out.println("CALL SETTER: " + setMethod.getName()+"("+getMethod.getGenericReturnType()+") - "+value);
			mInfo.setObject(result, value);
		}
		
		return result;
	}

	private Collection parseListOrSet(Class clazz, Type type) throws Exception {
		if(type==null || !(type instanceof ParameterizedType))
			throw new RePresentationException(clazz);

		boolean isList = List.class.isAssignableFrom(clazz);
		
		if(isList) {
		    if(lexer.type != Yytoken.TYPE_LEFT_SQUARE) new RePresentationException("Bad json: list is not list");
		} else if(lexer.type != Yytoken.TYPE_LEFT_BRACE && lexer.type != Yytoken.TYPE_LEFT_SQUARE) {
		    new RePresentationException("Bad json: set is not map or list");
		}
		
		Collection<Object> col = isList ? new ArrayList<Object>() : 
			(SortedSet.class.isAssignableFrom(clazz) ? new TreeSet<Object>(): new LinkedHashSet<Object>());
		
		ParameterizedType pType = (ParameterizedType) type;
		Type valType = pType.getActualTypeArguments()[0];
		Class valClass = null;
		if(valType instanceof Class) valClass = (Class)valType;
		if(valType instanceof ParameterizedType) valClass = (Class)((ParameterizedType)valType).getRawType();
		if(valClass==null) throw new RePresentationException("Cant find parameter for Collection : " + valType.toString());
		
		if(lexer.type == Yytoken.TYPE_LEFT_BRACE) {
			// это мапа {}
			for(;;) {
				lexer.yylex();
				if(lexer.type == Yytoken.TYPE_COMMA) continue;
				if(lexer.type == Yytoken.TYPE_RIGHT_BRACE) break;
				if(lexer.type != Yytoken.TYPE_VALUE) throw new RePresentationException("Bad json: map key expected");
				Object key = getObjectSimple(valClass);
				lexer.yylex();
				if(lexer.type != Yytoken.TYPE_COLON) throw new RePresentationException("Bad json: key:value separator expected");
				lexer.yylex();
				if(Boolean.TRUE == getObjectSimple(Boolean.class)) {
					col.add(key);
				}
			}
		} else {
			// это маccив []
			for(;;) {
			    col.add(getObject(valClass, valType));
			    lexer.yylex();
			    if(lexer.type == Yytoken.TYPE_COMMA) continue;
			    if(lexer.type == Yytoken.TYPE_RIGHT_SQUARE) break;
			    throw new RePresentationException("Bad json: expected , or ]");
			}			
		}
		
		return col;
	}

	private Map parseMap(Class clazz, Type type) throws IOException, ParseException, Exception {
		if(lexer.type != Yytoken.TYPE_LEFT_BRACE) throw new RePresentationException("Bad json: map is not map");
		
		final Map map = SortedMap.class.isAssignableFrom(clazz) ? new TreeMap() : new LinkedHashMap();
		
		ParameterizedType pType = (ParameterizedType) type;
		Type keyType = pType.getActualTypeArguments()[0];
		Class keyClass = null;
		if(keyType instanceof Class) keyClass = (Class)keyType;
		if(keyType instanceof ParameterizedType) keyClass = (Class)((ParameterizedType)keyType).getRawType();
		if(keyClass==null) throw new RePresentationException("Cant find key parameter for Map : " + keyType.toString());			
		Type valType = pType.getActualTypeArguments()[1];
		Class valClass = null;
		if(valType instanceof Class) valClass = (Class)valType;
		if(valType instanceof ParameterizedType) valClass = (Class)((ParameterizedType)valType).getRawType();
		if(valClass==null) throw new RePresentationException("Cant find value parameter for Map : " + valType.toString());			
		
		for(;;) {
			lexer.yylex();
			if(lexer.type == Yytoken.TYPE_COMMA) continue;
			if(lexer.type == Yytoken.TYPE_RIGHT_BRACE) break;
			if(lexer.type != Yytoken.TYPE_VALUE) throw new RePresentationException("Bad json: map key expected");
			Object key = getObjectSimple(keyClass);
			lexer.yylex();
			if(lexer.type != Yytoken.TYPE_COLON) throw new RePresentationException("Bad json: key:value separator expected");
			map.put(key, getObject(valClass, valType));
		}
		
		return map;
	}
}
