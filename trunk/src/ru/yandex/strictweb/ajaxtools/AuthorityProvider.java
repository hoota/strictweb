package ru.yandex.strictweb.ajaxtools;

import javax.servlet.http.HttpServletRequest;

public interface AuthorityProvider {

	/**
	 * Must throw RuntimeException with message is access is not allowed
	 */
    public void checkRequest(HttpServletRequest request, String[] roles);

	/**
	 * Must throw RuntimeException with message is access is not allowed
	 */
	public void checkRequest(HttpServletRequest request);
}
