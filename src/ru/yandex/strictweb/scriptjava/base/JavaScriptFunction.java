package ru.yandex.strictweb.scriptjava.base;

@Native
abstract public class JavaScriptFunction {
	abstract public Object call(Object instance, Object ... arguments);
}
