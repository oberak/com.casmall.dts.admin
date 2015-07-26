package com.casmall.dts.admin.print.actions;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightRetargetAction;
import org.eclipse.gef.ui.actions.MatchWidthRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

/**
 * Action, ToolBar 설정
 * 
 * @author oberak
 * 
 */
@SuppressWarnings("restriction")
public class PrintWizardEditorActionBarContributor extends ActionBarContributor {
	@Override
	protected void declareGlobalActionKeys() {

	}

    @Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());

		// Zoom
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());

		// Ruler 와 Guide 용
		addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY,
		        GEFMessages.ToggleRulerVisibility_Label, IAction.AS_CHECK_BOX));

		// Grid
		addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, GEFMessages.ToggleGrid_Label,
		        IAction.AS_CHECK_BOX));

		addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY,
		        GEFMessages.ToggleSnapToGeometry_Label, IAction.AS_CHECK_BOX));

		// Copy
		IWorkbenchWindow iww = getPage().getWorkbenchWindow();

		addRetargetAction((RetargetAction) ActionFactory.COPY.create(iww));
		addRetargetAction((RetargetAction) ActionFactory.CUT.create(iww));
		addRetargetAction((RetargetAction) ActionFactory.PASTE.create(iww));
		
//		addRetargetAction((RetargetAction) ActionFactory.SAVE.create(iww));
		
		// 정렬
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
		addRetargetAction(new MatchWidthRetargetAction());
		addRetargetAction(new MatchHeightRetargetAction());
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
//		toolBarManager.add(getAction(ActionFactory.SAVE.getId()));
//		toolBarManager.add(new Separator());

		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));

		// Zoom
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
		toolBarManager.add(new ZoomComboContributionItem(getPage()));
		
		// Copy 
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(ActionFactory.COPY.getId()));
		toolBarManager.add(getAction(ActionFactory.CUT.getId()));
		toolBarManager.add(getAction(ActionFactory.PASTE.getId()));
		
		// 정렬
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ALIGN_LEFT));
		toolBarManager.add(getAction(GEFActionConstants.ALIGN_CENTER));
		toolBarManager.add(getAction(GEFActionConstants.ALIGN_RIGHT));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ALIGN_TOP));
		toolBarManager.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
		toolBarManager.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
		toolBarManager.add(new Separator());	
		toolBarManager.add(getAction(GEFActionConstants.MATCH_WIDTH));
		toolBarManager.add(getAction(GEFActionConstants.MATCH_HEIGHT));
	}

	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		super.contributeToMenu(menuManager);

		// Ruler 와 Guide 용
		MenuManager viewMenu = new MenuManager("View");
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));

		// Grid
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		menuManager.add(viewMenu);
	}
}