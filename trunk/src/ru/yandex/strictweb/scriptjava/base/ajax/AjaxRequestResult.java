package ru.yandex.strictweb.scriptjava.base.ajax;

import ru.yandex.strictweb.ajaxtools.annotation.AjaxTransient;
import ru.yandex.strictweb.ajaxtools.annotation.Presentable;

@Presentable
public class AjaxRequestResult {
	@AjaxTransient
	public static final Object NO_DATA = new Object();
	
	Object data = null;
	Throwable error = null;
	
	public Throwable getError() {
		return error;
	}
	public void setError(Throwable error) {
		this.error = error;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
