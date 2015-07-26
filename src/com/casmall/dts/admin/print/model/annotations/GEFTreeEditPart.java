package com.casmall.dts.admin.print.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.gef.EditPart;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GEFTreeEditPart {

	Class<? extends EditPart> editPartType();

}
