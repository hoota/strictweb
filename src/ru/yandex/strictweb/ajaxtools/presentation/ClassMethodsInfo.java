package ru.yandex.strictweb.ajaxtools.presentation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import ru.yandex.strictweb.ajaxtools.annotation.AjaxTransient;
import ru.yandex.strictweb.ajaxtools.annotation.Presentable;

public class ClassMethodsInfo {
	public final static Set<Method> excludedMethods = new HashSet<Method>();
	
	static {
		fillExcludedMethods();
	}
	
	public abstract static class Property {
		final Class<?> clazz;
		AnnotatedElement ael;
		final DateTimeFormat dateFormat;
		final int fractionDigits;
		
		public Property(Class<?> clazz, AnnotatedElement ael, Property cli, Presentable pr) {
			this.clazz = clazz;
			this.ael = ael;
			
			int fd = pr!=null ? pr.fractionDigits() : 6;
			if(fd<0) fd = 0;
			if(fd>19) fd = 19;
			this.fractionDigits = fd;
			
			this.dateFormat = pr!=null ? pr.dateFormat() : (cli!=null ? cli.dateFormat : DateTimeFormat.UNDEF);			
		}
		
		public abstract String getName();
		public abstract Object getObject(Object obj) throws Exception;
		public abstract boolean canSet();
		public abstract void setObject(Object obj, Object value) throws Exception;
		public abstract Class<?> getReturnType();
		public abstract Type getGenericReturnType();
	}
	
	public static class FieldProperty extends Property {
		public final Field field;
		
		public FieldProperty(Class<?> clazz, Field field, Property cli, Presentable pr) {
			super(clazz, field, cli, pr);
			
			this.field = field;
			if(field != null) field.setAccessible(true);
		}

		@Override
		public String getName() {
			return field.getName();
		}

		@Override
		public Object getObject(Object obj) throws Exception {
			return field.get(obj);
		}

		@Override
		public void setObject(Object obj, Object value) throws Exception {
			field.set(obj, value);
		}

		@Override
		public boolean canSet() {
			return true;
		}

		@Override
		public Type getGenericReturnType() {
			return field.getGenericType();
		}

		@Override
		public Class<?> getReturnType() {
			return field.getType();
		}
	}
	
	public static class MethodProperty extends Property {
		public final String name;

		public final Method getter;
		public final Method setter;
		
		public MethodProperty(Class<?> clazz, Method getter, Property cli, Presentable pr) {
			super(clazz, getter, cli, pr);
			
			this.getter = getter;
			
			String gName = null == getter ? null : getter.getName();
			
			if(null == gName) name = null;
			else if(gName.startsWith("is")) name = Character.toLowerCase(gName.charAt(2)) + gName.substring(3);
			else if(gName.startsWith("get")) name = Character.toLowerCase(gName.charAt(3)) + gName.substring(4);
			else throw new RuntimeException("Invalid getter method: " + getter);

			
			setter = getSetter();
			if(getter != null) getter.setAccessible(true);
			if(setter != null) setter.setAccessible(true);
//			System.out.println(name + " setter: " + setter);
		}
		
		private Method getSetter() {
			if(null == name) return null;
			try {
				return clazz.getMethod("set" + Character.toUpperCase(name.charAt(0)) + name.substring(1), getter.getReturnType());
			} catch(Throwable e) {
				return null;
			}
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Object getObject(Object obj) throws Exception {
			return getter.invoke(obj);
		}

		@Override
		public void setObject(Object obj, Object value) throws Exception {
			setter.invoke(obj, value);
		}

		@Override
		public boolean canSet() {
			return setter != null;
		}
				
		public Class<?> getReturnType() {
			return getter.getReturnType();
		}

		public Type getGenericReturnType() {
			return getter.getGenericReturnType();
		}
	}
	
	static Map<Class<?>, List<Property>> sortedClassProperties = new ConcurrentHashMap<Class<?>, List<Property>>();
	private static Map<Class<?>, Class<?>> classesIds = new ConcurrentHashMap<Class<?>, Class<?>>();
	private static Map<Class<?>, Property> classIdInfos = new ConcurrentHashMap<Class<?>, Property>();
	private static Map<String, Property> propertiesInfos = new ConcurrentHashMap<String, Property>();
	private static Map<Class<?>, Boolean> presentableClasses = new ConcurrentHashMap<Class<?>, Boolean>();
	private static Map<Class<?>, Boolean> skipIncomingClasses = new ConcurrentHashMap<Class<?>, Boolean>();
	private static Map<Class<?>, Boolean> entityClasses = new ConcurrentHashMap<Class<?>, Boolean>();
	
	static {
	    presentableClasses.put(Boolean.class, false);
        presentableClasses.put(Byte.class, false);
        presentableClasses.put(Short.class, false);
	    presentableClasses.put(Integer.class, false);
        presentableClasses.put(Long.class, false);
        presentableClasses.put(Float.class, false);
        presentableClasses.put(Double.class, false);
        presentableClasses.put(String.class, false);

        presentableClasses.put(boolean.class, false);
        presentableClasses.put(byte.class, false);
        presentableClasses.put(short.class, false);
        presentableClasses.put(int.class, false);
        presentableClasses.put(long.class, false);
        presentableClasses.put(float.class, false);
        presentableClasses.put(double.class, false);
	}
	
	public static List<Property> getPresentableProperties(Class<?> cl) {
		List<Property> res = sortedClassProperties.get(cl);			

		if(null == res) {
			Property clInfo = new MethodProperty(cl, null, null, cl.getAnnotation(Presentable.class));
			
			res = new ArrayList<Property>();
			
			for(Method m : cl.getMethods()) 
				if(isGetter(m) 
				    && (m.getModifiers()&Modifier.STATIC) == 0
					&& !excludedMethods.contains(m)
					&& !m.isAnnotationPresent(AjaxTransient.class)
					&& (!m.isAnnotationPresent(Transient.class) || m.isAnnotationPresent(Presentable.class))) {
				 res.add(new MethodProperty(cl, m, clInfo, getPresentableFromHierarchy(m)));
			}

			for(Field f : cl.getFields()) 
				if(!f.isAnnotationPresent(AjaxTransient.class)
				    && (f.getModifiers()&Modifier.STATIC) == 0
					&& (!f.isAnnotationPresent(Transient.class) || f.isAnnotationPresent(Presentable.class))) {
				 res.add(new FieldProperty(cl, f, clInfo, getPresentableFromHierarchy(f)));
			}

			Collections.sort(res, new Comparator<Property>() {
				public int compare(Property a, Property b) {
					return a.getName().compareToIgnoreCase(b.getName());
				}}
			);
			
			sortedClassProperties.put(cl, res);
		}
		
		return res;
	}

	private static void fillExcludedMethods() {
		try {
			excludedMethods.add(Object.class.getMethod("getClass"));
		} catch(Exception e) {
		}
	}

	private static <T extends Member&AnnotatedElement> Presentable getPresentableFromHierarchy(T m) {
		Presentable p = m.getAnnotation(Presentable.class);
		if(p != null) return p;
		
		try {
			return getPresentableFromHierarchy(
				m.getDeclaringClass().getSuperclass().getMethod(m.getName())
			);
		} catch(Throwable e) {}
			
		return null;
	}

	public static boolean isGetter(Method m) {
		String n = m.getName();
		
		if(m.getParameterTypes().length != 0) return false;
		
		int mod = m.getModifiers();
		if(!Modifier.isPublic(mod) || Modifier.isStatic(mod)) return false;
		
		return (n.startsWith("is") && n.length() > 2) || 
			(n.startsWith("get") && n.length() > 3)
		;
	}

	public static Class<?> getEntityIdClass(Class<?> clazz) {
		Class<?> classId = classesIds.get(clazz);			
		
		if(classId == null) {
			for(Method m : clazz.getMethods()) {
				if(m.isAnnotationPresent(Id.class)) {
					classesIds.put(clazz, classId = m.getReturnType());
					break;
				}
			}
			
			for(Field f : clazz.getFields()) {
				if(f.isAnnotationPresent(Id.class)) {
					classesIds.put(clazz, classId = f.getType());
					break;
				}
			}
		}
		
		return classId;
	}

	public static Property getEntityIdProperty(Class<?> clazz) {
		Property idInfo = classIdInfos.get(clazz);
		
		if(idInfo == null) {
			for(Property mi : getPresentableProperties(clazz)) {
				if(mi.ael.isAnnotationPresent(Id.class)) {
					classIdInfos.put(clazz, idInfo = mi);
					break;
				}
			}
		}
		
		return idInfo;      
	}

	public static Property getProperty(Class<?> clazz, String mName) {
		String key = getPropertyKey(clazz, mName);
		
		Property prop = propertiesInfos.get(key);
		
		if(prop == null) {
			for(Property p : getPresentableProperties(clazz)) {
				propertiesInfos.put(getPropertyKey(clazz, p.getName()), p);
				if(p.getName().equals(mName)) prop = p;
			}			
		}
		
		return prop;
	}

	private static String getPropertyKey(Class<?> clazz, String mName) {
		return clazz.getCanonicalName() + "::" + mName;
	}

	public static boolean isPresentableOrEntity(Class<?> cls) {
		Boolean b = presentableClasses.get(cls);			
		
		if(b==null) {
			b = cls.isAnnotationPresent(Entity.class) || 
				cls.isAnnotationPresent(Presentable.class) ||
				isPojoBean(cls) ||
				(cls.getSuperclass()!=null && cls.getSuperclass().isAnnotationPresent(Entity.class));
			presentableClasses.put(cls, b);			
		}
		
		return b.booleanValue();
	}

	public static boolean isPojoBean(Class<?> cls) {
		int publicProp = 0;
		if(cls.isArray()) return false;
		
		for(Method m : cls.getMethods()) {
			int mod = m.getModifiers();
			if(!Modifier.isPublic(mod) || Modifier.isStatic(mod)) continue;
			String name = m.getName();
			if("getClass".equals(name)) continue;
			if(name.startsWith("get") || name.startsWith("is")) {
				if(m.getParameterTypes().length > 0) return false;
				publicProp ++;
//				System.out.println(m);
			}
		}
		
		for(Field f : cls.getFields()) {
			int mod = f.getModifiers();
			if(!Modifier.isPublic(mod) || Modifier.isStatic(mod)) continue;
			publicProp ++;			
//			System.out.println(f);
		}
		
//		System.out.println(cls + " :: " + publicProp);
		return publicProp > 0;
	}

	public static boolean isEntity(Class cls) {
		Boolean b = entityClasses.get(cls);			
		
		if(b==null) {
			b = cls.isAnnotationPresent(Entity.class);
			entityClasses.put(cls, b);			
		}
		
		return b.booleanValue();
	}

	public static boolean getSkipIncoming(Class<?> cls) {
		Boolean b = skipIncomingClasses.get(cls);			
		
		if(b==null) {
			Presentable ann = cls.getAnnotation(Presentable.class);
			b = ann != null && ann.skipIncoming();
			skipIncomingClasses.put(cls, b);			
		}
		
		return b.booleanValue();
	}
}
