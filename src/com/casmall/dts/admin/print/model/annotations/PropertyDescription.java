package com.casmall.dts.admin.print.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertyDescription {

	String field();
	String category();
	String label();
	Class<?> type() default String.class;
	String[] value() default "";
}
