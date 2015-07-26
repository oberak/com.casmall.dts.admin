package com.casmall.dts.admin.print.model;
import java.lang.annotation.Annotation;

import org.eclipse.gef.EditPart;

import com.casmall.dts.admin.print.model.annotations.GEFEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFTreeEditPart;

public class ModelHelper {
	
	/**
	 * 상속 구조에서 어노테이션을 찾습니다.
	 * @param modelClass
	 * @param annotationClass
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Annotation getHierachyAnnotation(Class<?> modelClass, Class annotationClass) {
		Annotation result = null;
		result = modelClass.getAnnotation(annotationClass);
		
		if (result == null) {
			Class<?> superClass = modelClass.getSuperclass();
			if (superClass != null) {
				return getHierachyAnnotation(superClass, annotationClass);
			}
		}
		return result;
	}

	public static EditPart createEditPart(Class<?> modelClass) {
		GEFEditPart editPart = (GEFEditPart) 
				getHierachyAnnotation(modelClass, GEFEditPart.class);
		try {
			if (editPart != null)
				return editPart.editPartType().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static EditPart createTreeEditPart(Class<?> modelClass) {
		GEFTreeEditPart treeEditPart = (GEFTreeEditPart) 
				getHierachyAnnotation(modelClass, GEFTreeEditPart.class);
		try {
			if (treeEditPart != null)
				return treeEditPart.editPartType().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static String getModelIconImagePath(Class clazz) {
//		GEFModelIcon modelIcon = (GEFModelIcon) clazz.getAnnotation(GEFModelIcon.class);
//		if (modelIcon != null)
//			return modelIcon.imageFilePath();
//		return null;
//	}
}