package com.casmall.dts.admin.print.model;

import org.eclipse.gef.requests.CreationFactory;

public class ElementCreationFactory implements CreationFactory {

	private Class<?> template;

	public ElementCreationFactory(Class<?> template) {
		this.template = template;
	}

	@Override
	public Object getNewObject() {
		// Object »ý¼º
		if (template == null)
			return null;
		try {
	        return template.newInstance();
        } catch (InstantiationException e) {
	        e.printStackTrace();
        } catch (IllegalAccessException e) {
	        e.printStackTrace();
        }
        return null;
	}

	@Override
	public Object getObjectType() {
		return template;
	}

}