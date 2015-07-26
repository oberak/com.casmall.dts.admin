package com.casmall.dts.admin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.casmall.dts.admin.ui.action.PrintWizardAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;

	private Action openPrintAction;
	private IAction saveAction;
	private IAction saveAsAction;
	private IAction saveAllAction;
	private IContributionItem showViewItem;
	private IWorkbenchAction exportAction;
	private IWorkbenchAction importAction;
	private IWorkbenchAction printAction;
	
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		openPrintAction = new PrintWizardAction(window);
		register(openPrintAction);
		
		saveAction = ActionFactory.SAVE.create(window);
		saveAsAction = ActionFactory.SAVE_AS.create(window);
		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAction);
		register(saveAsAction);
		register(saveAllAction);

		exportAction = ActionFactory.EXPORT.create(window);
		importAction = ActionFactory.IMPORT.create(window);
		register(exportAction);
		register(importAction);

		printAction = ActionFactory.PRINT.create(window);
		register(printAction);

		// Copy
		register(ActionFactory.UNDO.create(window));
		register(ActionFactory.REDO.create(window));
		register(ActionFactory.COPY.create(window));
		register(ActionFactory.CUT.create(window));
		register(ActionFactory.PASTE.create(window));

		register(ActionFactory.SHOW_VIEW_MENU.create(window));
		showViewItem = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File", "File");
		fileMenu.add(openPrintAction);
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.add(saveAllAction);
		
		fileMenu.add(importAction);
		fileMenu.add(exportAction);
		
		fileMenu.add(printAction);
		
		fileMenu.add(exitAction);

		IMenuManager menuShow = new MenuManager("Show &View", ActionFactory.SHOW_VIEW_MENU.getId());
		menuShow.add(showViewItem);

		MenuManager helpMenu = new MenuManager("&Help", "Help");
		helpMenu.add(menuShow);
		helpMenu.add(aboutAction);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
	}
}
