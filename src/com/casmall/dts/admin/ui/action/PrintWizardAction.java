package com.casmall.dts.admin.ui.action;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;

import com.casmall.dts.admin.ui.dialog.PaperSelectDialog;

public class PrintWizardAction extends Action{
	private IWorkbenchWindow window;
	public final static String ID = PrintWizardAction.class.getName();
	
	public PrintWizardAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Open");
		setToolTipText("설정정보 Open");
//		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(DTSConstants.PLUGIN_ID, ImageRepository.MENU_BASIS_CSTMGT));
	}

	@Override
	public void run() {
		super.run();
		try {
			PaperSelectDialog dialog = new PaperSelectDialog(window.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
			dialog.open();
			
//			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//			page.openEditor(new PrintWizardEditorInput("PrintSetupMgr"), PrintWizardEditor.ID, false);
//			window.getActivePage().showEditor(ref);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
