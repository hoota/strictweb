package ru.yandex.strictweb.ajaxtools;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.yandex.strictweb.ajaxtools.exception.NoArgumentException;
import ru.yandex.strictweb.ajaxtools.exception.RePresentationException;
import ru.yandex.strictweb.ajaxtools.presentation.ClassMethodsInfo;


public class RePresentation {
	public static interface EntityFinder {
		Object find(Class<?> clazz, Object primaryKey);
	}
	
	EntityFinder ef;
	
	public RePresentation(EntityFinder ef) {
		this.ef = ef;
	}
	
	/**
	 * 
	 * @param in строка времени в формате DD.MM.YYYY HH:MM:SS разделители какие угодно. 
	 * Пробел разделяет время от даты. Многие значения можно опускать
	 * @return
	 */
	public static long parseDate(String in) {
		if(in == null || in.isEmpty()) return -1;

		String dateTime[] = in.replaceAll("\\s+", " ").trim().split(" ");
		String date[] = dateTime[0].split("[^0-9]");
		String time[] = dateTime.length > 1 ? dateTime[1].split("[^0-9]") : null;
		
		Calendar c = Calendar.getInstance();
		
		if(date.length == 3) {
		    if(date[2].length() == 4) {
		        c.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1, Integer.parseInt(date[0]));
		    } else {
		        c.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));
		    }
		}

		c.set(Calendar.HOUR_OF_DAY, time!=null && time.length > 0 ? Integer.parseInt(time[0]) : 0);
		c.set(Calendar.MINUTE, time!=null && time.length > 1 ? Integer.parseInt(time[1]) : 0);
		c.set(Calendar.SECOND, time!=null && time.length > 2 ? Integer.parseInt(time[2]) : 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTimeInMillis();		
	}
	
	public static Object getObjectSimple(String value, Class clazz) {
		if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
			try {
				return Integer.parseInt(value);
			} catch (RuntimeException e) {
				throw new RePresentationException("Invalid int: " + value);
			}
		} else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
			try {
				return Long.parseLong(value);
			} catch (RuntimeException e) {
				throw new RePresentationException("Invalid long: " + value);
			}			
		} else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
			try {
				return Double.parseDouble(value);
			} catch (RuntimeException e) {
				throw new RePresentationException("Invalid double: " + value);
			}
        } else if(clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            try {
                return Byte.parseByte(value);
            } catch (RuntimeException e) {
                throw new RePresentationException("Invalid byte: " + value);
            }
		} else if(clazz.equals(String.class)) {
			return value;
		} else if(clazz.isEnum()) {
		    return Enum.valueOf(clazz, value);
		} else if(clazz.equals(Timestamp.class)) {
			long ts = parseDate(value);
            return ts==-1 ? null : new Timestamp(ts);
        } else if(clazz.equals(Date.class)) {
            long ts = parseDate(value);
            return ts==-1 ? null : new Date(ts);
		} else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return (boolean)(null!=value && value.equals("1"));
		}
		throw new RePresentationException(clazz);
	}
	
	public Object getObject(Node node, Class clazz, Type type) throws Exception {
		if(node.getNodeName().equals("null")) return null;
		
		if(Map.class.isAssignableFrom(clazz)) {
			Map map = HashMap.class.isAssignableFrom(clazz) ? new HashMap() : new TreeMap();
			
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
			
			for(int i=0; i<node.getChildNodes().getLength(); i++) {
				Node n = node.getChildNodes().item(i);
				String id = n.getAttributes().getNamedItem("id").getNodeValue();
				if(n.getNodeType()==Node.ELEMENT_NODE) map.put(getObjectSimple(id, keyClass), getObject(n, valClass, valType));
			}
			return map;
		} else if(Set.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)) {
			if(type==null || !(type instanceof ParameterizedType))
				throw new RePresentationException(clazz);

			boolean isList = List.class.isAssignableFrom(clazz);
			Collection<Object> col = isList ? new ArrayList<Object>() : 
				(HashSet.class.isAssignableFrom(clazz) ? new HashSet<Object>() : new TreeSet<Object>());
			
			ParameterizedType pType = (ParameterizedType) type;
			Type t = pType.getActualTypeArguments()[0];
			Class c = null;
			if(t instanceof Class) c = (Class)t;
			if(t instanceof ParameterizedType) c = (Class)((ParameterizedType)t).getRawType();
			if(c==null) throw new RePresentationException("Cant find parameter for Collection : " + t.toString());
			
			for(int i=0; i<node.getChildNodes().getLength(); i++) {
				Node n = node.getChildNodes().item(i);
				if(n.getNodeType()==Node.ELEMENT_NODE) {
				    Node id = isList ? null : n.getAttributes().getNamedItem("id");
				     
				    col.add(getObject(id!=null ? id : n, c, t));
				}
			}
			return col;
		} else if(ClassMethodsInfo.isPresentableOrEntity(clazz)) {
			if(null == node.getFirstChild()) return null;
			
			boolean isEntity = ClassMethodsInfo.isEntity(clazz);
//			System.out.println("isEntity " +clazz+ " :: " + isEntity);
			
			
			if(node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
				String text = node.getFirstChild().getNodeValue();
				if("new".equals(text)) return clazz.newInstance();
				
				if(clazz.isEnum()) {
				    return Enum.valueOf(clazz, text);
				}
			
				if(!isEntity) throw new RePresentationException("Cant create new instance of Presentable " + clazz.getCanonicalName()+"("+text+")");

				return ef.find(clazz, getObjectSimple(text, ClassMethodsInfo.getEntityIdClass(clazz)));
			}
			
			Object result = null;
			
			if(isEntity) {
				ClassMethodsInfo.Property idInfo = ClassMethodsInfo.getEntityIdProperty(clazz);
				Class idClass = ClassMethodsInfo.getEntityIdClass(clazz);
				
				for(int i=0; i<node.getChildNodes().getLength(); i++) {
					Node n = node.getChildNodes().item(i);
					String id = n.getAttributes().getNamedItem("id").getNodeValue();
					if(idInfo.getName().equals(id)) {
						if(null == n.getFirstChild()) break;
						result = ef.find(clazz, getObjectSimple(n.getFirstChild().getNodeValue(), idClass));
						break;
					}
				}				
			}			
			
			if(null == result) result = clazz.newInstance();
			
			boolean skipIncoming = ClassMethodsInfo.getSkipIncoming(clazz);
			
			for(int i=0; i<node.getChildNodes().getLength(); i++) {
				Node n = node.getChildNodes().item(i);
				String mName = n.getAttributes().getNamedItem("id").getNodeValue();
				
				ClassMethodsInfo.Property mInfo = ClassMethodsInfo.getProperty(clazz, mName);
				if(mInfo == null) {
					if(skipIncoming) continue;
					throw new RePresentationException("Unknown property `"+mName+"` in class " + clazz);
				}
				if(!mInfo.canSet()) {
					if(skipIncoming) continue;
					throw new RePresentationException("Unknown setter property `"+mName+"` in class " + clazz);
				}
				
				Object value = getObject(n, mInfo.getReturnType(), mInfo.getGenericReturnType());
				//System.out.println("CALL SETTER: " + setMethod.getName()+"("+getMethod.getGenericReturnType()+") - "+value);
				if(Collection.class.isAssignableFrom(mInfo.getReturnType())) {
					Collection col = (Collection)mInfo.getObject(result);
					if(null!=col) {
						col.clear();
						col.addAll((Collection)value);
						value = col;
					}
				}
				
				mInfo.setObject(result, value);
			}	
			return result;
		} else return getObjectSimple(node.getTextContent(), clazz);
		
	}
	
	public Object getParameterValue(Document doc, String argName, Class<?> clas, Type type) throws Exception {
		NodeList properties = doc.getFirstChild().getChildNodes();
		
		for(int i = 0; i < properties.getLength(); i++) {
			Node node = properties.item(i);
			String nodeId = node.getAttributes().getNamedItem("id").getNodeValue();
			//System.out.println(nodeId);
			if(nodeId.equals(argName)) {
				return getObject(node, clas, type);
			}
		}
		
		throw new NoArgumentException(argName);
		
		//return null;
	}
}
