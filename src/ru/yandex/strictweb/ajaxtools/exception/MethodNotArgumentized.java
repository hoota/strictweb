package ru.yandex.strictweb.ajaxtools.exception;

import java.lang.reflect.Method;

public class MethodNotArgumentized extends AjaxException {
	private static final long serialVersionUID = -2861073914301726391L;

	Method method;
	public MethodNotArgumentized(Method m) {
		this.method = m;
	}
	@Override
	public String getMessage() {
		return "Method is not annotated with @Arguments: " + method.getName();
	}
}
