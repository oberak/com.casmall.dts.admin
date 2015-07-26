package com.casmall.dts.admin.print.commands;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

public class PrintWizardContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	public PrintWizardContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		setActionRegistry(registry);
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);

		IAction action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		
		// Copy ¿ë
		action = getActionRegistry().getAction(ActionFactory.COPY.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
		
		action = getActionRegistry().getAction(ActionFactory.CUT.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
		
		action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
	}

	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	public void setActionRegistry(ActionRegistry registry) {
		this.actionRegistry = registry;
	}

}