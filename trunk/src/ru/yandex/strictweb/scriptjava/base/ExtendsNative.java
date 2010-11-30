package ru.yandex.strictweb.scriptjava.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface ExtendsNative {
	String value();
}
