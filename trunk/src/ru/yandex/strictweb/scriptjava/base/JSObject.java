package ru.yandex.strictweb.scriptjava.base;


@Native
public class JSObject {
	
	/**
	 * this is a magic method for compiler plugin
	 * @return method or field name. Example:<br>
	 * ajaxName(obj.getLastName()) - becomes "lastName"<br>
	 * ajaxName(obj.middleName) - becomes "middleName"<br>
	 * ajaxName(MyClass.class) - becomes "myClass"
	 */
	@Native
	public final static String ajaxName(Object o) {
		return null;
	}
	
	/**
	 * this is a magic method for compiler plugin
	 * It makes ajax async call
	 */
	@Native
	public final static <T> void ajaxAsyncCall(T a, VoidDelegate<T> callback) {
		return;
	}

    /**
     * this is a magic method for compiler plugin
     * It makes ajax async call
     */
    @Native
    public final static <T> void ajaxAsyncCallWithErrors(T a, VoidDelegate<T> callback, VoidDelegate<Throwable> errorHandler) {
        return;
    }
    
//	public Object getProperty(String key) {
//		// obj[key]
//		return null;
//	}
//
//	public void putProperty(String key, Object property) {
//		// obj[key] = property
//	}
//
//	public Set<String> propertiesKeys() {
//		return null;
//	}	
}
