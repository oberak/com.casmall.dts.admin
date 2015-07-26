package com.casmall.dts.admin.print.edit;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.casmall.dts.admin.print.model.ModelHelper;

public class AnnotatedEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = ModelHelper.createEditPart(model.getClass());
		
		if (part != null)
			part.setModel(model);
		
		return part;
	}

}