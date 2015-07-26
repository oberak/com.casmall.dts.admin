package com.casmall.dts.admin.print.edit.tree;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

import com.casmall.dts.admin.print.common.PrintConstants;
import com.casmall.dts.admin.print.edit.policies.ElementDeletePolicy;
import com.casmall.dts.admin.print.model.BaseElement;


public class BoxElementTreeEditPart extends BaseElementTreeEditPart {
	
	private static Image ICON_BOX_ELEMENT;

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementDeletePolicy());
	}

	@Override
	protected void refreshVisuals() {
		BaseElement model = (BaseElement) getModel();
		setWidgetText(model.getName());
		
		setWidgetImage(getWidgetImage());
	}
	
	private Image getWidgetImage() {
		if (ICON_BOX_ELEMENT == null)
			ICON_BOX_ELEMENT = createImage(PrintConstants.IMG_BOX);
		return ICON_BOX_ELEMENT;
	}
}