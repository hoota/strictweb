package ru.yandex.strictweb.ajaxtools.exception;

public class NoArgumentException extends AjaxException {
	private static final long serialVersionUID = -8840530771336364604L;

	String argName;
	
	public NoArgumentException(String name) {
		this.argName = name;
	}
	
	@Override
	public String getMessage() {
		return "No argument found in request: " + argName;
	}
}
