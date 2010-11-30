package ru.yandex.strictweb.ajaxtools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.yandex.strictweb.ajaxtools.presentation.DateTimeFormat;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Inherited
public @interface Presentable {
	int level() default 0;
	int incLevel() default 0;
	int setLevel() default Integer.MAX_VALUE;
	DateTimeFormat dateFormat() default DateTimeFormat.UNDEF;
	boolean skipIncoming() default false;
}
