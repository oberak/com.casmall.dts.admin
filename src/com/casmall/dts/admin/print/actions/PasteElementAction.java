package com.casmall.dts.admin.print.actions;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.casmall.dts.admin.print.commands.ElementPasteCommand;

public class PasteElementAction extends SelectionAction {

	public PasteElementAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("Paste");
		setId(ActionFactory.PASTE.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(true);
	}
	
	private Command createPasteCommand() {
		return new ElementPasteCommand();
	}

	@Override
	protected boolean calculateEnabled() {
		Command cmd = createPasteCommand();
		return cmd != null && cmd.canExecute();
	}

	@Override
	public void run() {
		Command cmd = createPasteCommand();
		if (cmd != null && cmd.canExecute())
			execute(cmd);
	}
}