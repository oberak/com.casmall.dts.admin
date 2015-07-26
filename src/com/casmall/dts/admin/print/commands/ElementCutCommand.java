package com.casmall.dts.admin.print.commands;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.casmall.dts.admin.print.model.BaseElement;


public class ElementCutCommand extends ElementCopyCommand {

	@Override
	public void execute() {
		if (!canExecute())
			return;
		
		List<BaseElement> list = getSelectedList();
		for (Iterator<BaseElement> it=list.iterator(); it.hasNext(); ) {
			BaseElement element = it.next();
			if (isCopyableElement(element))
				element.getParent().removeChild(element);
		}
		
		Clipboard.getDefault().setContents(list);
	}

	@Override
	public boolean canUndo() {
		return getSelectedList() != null && !getSelectedList().isEmpty();
	}

	@Override
	public void undo() {		
		List<BaseElement> list = getSelectedList();
		for (Iterator<BaseElement> it=list.iterator(); it.hasNext(); ) {
			BaseElement element = it.next();
			if (isCopyableElement(element))
				element.getParent().addChlid(element);
		}
	}
}