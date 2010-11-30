package ru.yandex.strictweb.scriptjava.base.ajax;

import ru.yandex.strictweb.scriptjava.base.Native;
import ru.yandex.strictweb.scriptjava.base.VoidDelegate;

/**
 * https://developer.mozilla.org/en/XMLHttpRequest 
 * @author hoota
 */
@Native
public class XMLHttpRequest {

	/**
	 * Aborts the request if it has already been sent.
	 */
	public void abort() {}
	
	/**
	 * Returns all the response headers as a string. 
	 */
	public String getAllResponseHeaders() {return null;}
	
	/**
	 * Returns the text of a specified header. 
	 */
	public String getResponseHeader(String header) {return null;}
	
	/**
	 * Initializes a request. async[=true], user[=null] & password[=null] are optional 
	 */
	public void open(String method, String url, boolean async, String user, String password) {}
	
	/**
	 * Overrides the MIME type returned by the server. 
	 */
	public void overrideMimeType(String mimetype) {}
	
	/**
	 * Sends the request.  If the request is asynchronous (which is the default),
	 * this method returns as soon as the request is sent.  If the request is
	 * synchronous, this method doesn't return until the response has arrived.
	 * 
	 *  body[=null] is optional
	 */
	public void send(Object body) {}
	
	/**
	 * Sets the value of an HTTP request header.
	 */
	public void setRequestHeader(String header, String value) {}

	public final boolean multipart = false; // readonly
	public VoidDelegate<XMLHttpRequest> onreadystatechange;
	public final long readyState = 0; // readonly
	public final String responseText = null; // readonly
	public final Object responseXML = null; // readonly
	public final long status = 0; // readonly
	public final String statusText = null; // readonly
	
}
