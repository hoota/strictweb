package ru.yandex.strictweb.scriptjava.base.ajax;

public interface AjaxUrlFormer {
	public String getUrl(String clazz, String method);
	public String getQueryString(String clazz, String method);
}
