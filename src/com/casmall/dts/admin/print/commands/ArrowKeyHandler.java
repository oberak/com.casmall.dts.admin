package com.casmall.dts.admin.print.commands;
import java.util.List;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

public class ArrowKeyHandler extends GraphicalViewerKeyHandler {

	private ISelectionProvider selectionProvider;
	
	private CommandStack commandStack;

	public ArrowKeyHandler(GraphicalViewer viewer, ISelectionProvider selectionProvider, CommandStack commandStack) {
		super(viewer);
		setSelectionProvider(selectionProvider);
		setCommandStack(commandStack);
	}

	public void setSelectionProvider(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
	}

	public void setCommandStack(CommandStack commandStack) {
		this.commandStack = commandStack;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (selectionProvider != null
				&& (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_LEFT || event.keyCode == SWT.ARROW_RIGHT)) {
			ISelection selection = selectionProvider.getSelection();

			if (selection instanceof StructuredSelection) {
				StructuredSelection structuredSelection = (StructuredSelection) selection;

				commandStack.execute(createChangeLayoutCommand(structuredSelection.toList(), event));
			}

			return false;
		}
		return super.keyPressed(event);
	}

	/**
	 * �̺�Ʈ�� ���� ���ο� Command ��ü�� �����մϴ�.
	 * 
	 * @param selectedEditPartList
	 * @param event
	 * @return
	 */
	private Command createChangeLayoutCommand(List<?> selectedEditPartList, KeyEvent event) {
		int differenceWidthOrX = 0;
		int differenceHeightOrY = 0;

		switch (event.keyCode) {
		case SWT.ARROW_UP:
			differenceHeightOrY = -1;
			break;
		case SWT.ARROW_DOWN:
			differenceHeightOrY = 1;
			break;
		case SWT.ARROW_LEFT:
			differenceWidthOrX = -1;
			break;
		case SWT.ARROW_RIGHT:
			differenceWidthOrX = 1;
			break;
		}

		if (SWT.CTRL == (event.stateMask & SWT.CTRL)) {
			differenceWidthOrX *= 12;
			differenceHeightOrY *= 12;
		}

		Command command = new ArrowKeyChangeLayoutCommand(selectedEditPartList, differenceWidthOrX,
				differenceHeightOrY, SWT.SHIFT == (event.stateMask & SWT.SHIFT));

		return command;
	}

}