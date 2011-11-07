package ru.yandex.strictweb.scriptjava.base.ajax;

import ru.yandex.strictweb.scriptjava.base.VoidDelegate;

public class AjaxRequest {
    public final String clazz;
    public final String method;
    public final Object[] args;
    final VoidDelegate<Object> callBack;
    final VoidDelegate<Throwable> errorHandler;
    
    public AjaxRequest(String clazz, String method, Object[] args,  VoidDelegate<Object> callBack, VoidDelegate<Throwable> errorHandler) {
        this.clazz = clazz;
        this.method = method;
        this.args = args;
        this.callBack = callBack;
        this.errorHandler = errorHandler;
    }
}
