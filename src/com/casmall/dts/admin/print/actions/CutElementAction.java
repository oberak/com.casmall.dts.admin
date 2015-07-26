package com.casmall.dts.admin.print.actions;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.casmall.dts.admin.print.commands.ElementCutCommand;
import com.casmall.dts.admin.print.model.BaseElement;

public class CutElementAction extends SelectionAction {

	public CutElementAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("Cut");
		setId(ActionFactory.CUT.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(true);
	}
	
	private Command createCutCommand(List<Object> selectedObjects) {
		if (selectedObjects == null || selectedObjects.isEmpty())
			return null;
		
		ElementCutCommand cmd = new ElementCutCommand();
		for (Iterator<Object> it=selectedObjects.iterator(); it.hasNext(); ) {
			EditPart ep = (EditPart) it.next();
			BaseElement element = (BaseElement) ep.getModel();
			if (!cmd.isCopyableElement(element))
				return null;
			cmd.addElement(element);
		}
		
		return cmd;
	}

	@Override
	protected boolean calculateEnabled() {
		@SuppressWarnings("unchecked")
        Command cmd = createCutCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}

	@Override
	public void run() {
		@SuppressWarnings("unchecked")
        Command cmd = createCutCommand(getSelectedObjects());
		if (cmd != null && cmd.canExecute())
			cmd.execute();
	}
}