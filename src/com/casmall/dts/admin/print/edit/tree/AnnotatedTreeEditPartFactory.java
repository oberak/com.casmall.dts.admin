package com.casmall.dts.admin.print.edit.tree;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.casmall.dts.admin.print.model.ModelHelper;

public class AnnotatedTreeEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = ModelHelper.createTreeEditPart(model.getClass());
		
		if (part != null)
			part.setModel(model);
		
		return part;
	}

}