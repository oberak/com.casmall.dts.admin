package com.casmall.dts.admin;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	private static final String ID_TABS_FOLDER = "tabs folder";

	public void createInitialLayout(IPageLayout layout) {
		// Outline 추가
		String editArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		// view 삭제/이동 불가 처리
//		layout.setFixed(true);

		IFolderLayout tabs = layout.createFolder(ID_TABS_FOLDER, IPageLayout.LEFT, .3f, editArea);
		tabs.addView(IPageLayout.ID_OUTLINE);
//		tabs.addPlaceholder(IPageLayout.ID_PROP_SHEET);
//		tabs.addView(IPageLayout.ID_PROP_SHEET);
		// Property sheet
		layout.addView(IPageLayout.ID_PROP_SHEET,IPageLayout.BOTTOM, .7f, editArea);

		// show view 등록
		layout.addPerspectiveShortcut("com.casmall.dts.print.perspective");
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		
	}
}
