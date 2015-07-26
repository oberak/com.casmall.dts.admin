package com.casmall.dts.admin.print.edit.tree;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

import com.casmall.dts.admin.print.common.PrintConstants;
import com.casmall.dts.admin.print.edit.policies.ElementDeletePolicy;
import com.casmall.dts.admin.print.model.BaseElement;


public class LineElementTreeEditPart extends BaseElementTreeEditPart {
	
	private static Image ICON_LINE_ELEMENT;

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
		if (ICON_LINE_ELEMENT == null)
			ICON_LINE_ELEMENT = createImage(PrintConstants.IMG_LINE);
		return ICON_LINE_ELEMENT;
	}
}