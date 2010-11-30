package ru.yandex.strictweb.ajaxtools.exception;

public class RePresentationException extends AjaxException {
	private static final long serialVersionUID = -3378727616521608978L;

	public RePresentationException(Class<?> type) {
		super("Unknown re-presentation type: " + type.getCanonicalName());
	}
	
	public RePresentationException(String msg) {
		super(msg);
	}
}
