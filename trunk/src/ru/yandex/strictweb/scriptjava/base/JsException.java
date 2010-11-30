package ru.yandex.strictweb.scriptjava.base;

public class JsException extends RuntimeException {
	public String message;
	
	public JsException(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
