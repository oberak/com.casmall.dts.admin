package com.casmall.dts.admin.export;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class PrintExportWizard extends Wizard implements IExportWizard {

	public PrintExportWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		return false;
	}
}
