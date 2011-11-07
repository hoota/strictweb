package ru.yandex.strictweb.ajaxtools;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

public interface AuthorityProvider {

	/**
	 * Must throw RuntimeException with message is access is not allowed
	 */
    public void checkMethodRequest(HttpServletRequest request, Method method, Object[] params);

	/**
	 * Must throw RuntimeException with message is access is not allowed
	 */
	public void checkRequest(HttpServletRequest request);
}
