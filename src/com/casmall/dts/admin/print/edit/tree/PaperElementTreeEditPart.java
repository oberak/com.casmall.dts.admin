package com.casmall.dts.admin.print.edit.tree;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

import com.casmall.dts.admin.print.common.PrintConstants;
import com.casmall.dts.admin.print.edit.policies.ElementDeletePolicy;
import com.casmall.dts.admin.print.model.BaseElement;

public class PaperElementTreeEditPart extends BaseElementTreeEditPart {
	private static Image ICON_PAPER_ELEMENT;

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
		if (ICON_PAPER_ELEMENT == null)
			ICON_PAPER_ELEMENT = createImage(PrintConstants.IMG_PAPER);
		return ICON_PAPER_ELEMENT;
	}
}