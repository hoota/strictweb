package ru.yandex.strictweb.scriptjava.compiler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface NoInnerObfuscation {
}