package ru.yandex.strictweb.ajaxtools.exception;

public class NoSuchMethodException extends AjaxException {
	private static final long serialVersionUID = -8281172473705059337L;

	String methodName;
	
	public NoSuchMethodException(String name) {
		this.methodName = name;
	}
	
	@Override
	public String getMessage() {
		return "No such method: " + methodName;
	}
}
