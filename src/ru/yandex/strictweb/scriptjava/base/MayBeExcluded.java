package ru.yandex.strictweb.scriptjava.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
/** Помечает метод, который может не войти в JS, если его никто не вызывает */
public @interface MayBeExcluded {
}
