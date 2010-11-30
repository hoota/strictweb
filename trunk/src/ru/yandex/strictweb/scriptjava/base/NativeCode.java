package ru.yandex.strictweb.scriptjava.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Use this annotation to mark methods this native JavaScript code. 
 */
@Target(ElementType.METHOD)
public @interface NativeCode {
	String value();
}
