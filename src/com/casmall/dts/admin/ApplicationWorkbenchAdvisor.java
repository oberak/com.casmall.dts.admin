package com.casmall.dts.admin;

import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "com.casmall.dts.print.perspective"; //$NON-NLS-1$

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    @Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		
		// window size ���� ����
		configurer.setSaveAndRestore(true);
		
		// Tab�� � ó��
		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
	}
	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
}
