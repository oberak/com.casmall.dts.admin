package com.casmall.dts.admin.print.edit.tree;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import com.casmall.dts.admin.print.editor.PrintWizardEditor;
import com.casmall.dts.admin.print.model.BaseElement;

public abstract class BaseElementTreeEditPart extends AbstractTreeEditPart
		implements PropertyChangeListener {

	@Override
	protected List<BaseElement> getModelChildren() {
		return ((BaseElement) getModel()).getChildrenArray();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_ADD)) refreshChildren();
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_REMOVE)) refreshChildren();
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_NAME)) refreshVisuals();
	}


	@Override
	public void activate() {
		super.activate();
		((BaseElement) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		((BaseElement) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}
	
	protected static Image createImage(String name) {
		InputStream stream = PrintWizardEditor.class.getResourceAsStream(name);
		Image image = new Image(null, stream);
		try {
			stream.close();
		} catch (IOException ioe) {
		}
		return image;
	}
}