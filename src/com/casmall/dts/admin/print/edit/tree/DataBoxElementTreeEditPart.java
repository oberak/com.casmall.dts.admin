package com.casmall.dts.admin.print.edit.tree;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

import com.casmall.dts.admin.print.common.PrintConstants;
import com.casmall.dts.admin.print.edit.policies.ElementDeletePolicy;
import com.casmall.dts.admin.print.model.BaseElement;

public class DataBoxElementTreeEditPart extends BaseElementTreeEditPart {
	private static Image ICON_DATA_ELEMENT;

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
		if (ICON_DATA_ELEMENT == null)
			ICON_DATA_ELEMENT = createImage(PrintConstants.IMG_DATA);
		return ICON_DATA_ELEMENT;
	}
}