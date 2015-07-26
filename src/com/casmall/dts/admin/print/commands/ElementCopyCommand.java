package com.casmall.dts.admin.print.commands;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.model.PaperElement;

public class ElementCopyCommand extends Command {

	private List<BaseElement> list = new ArrayList<BaseElement>();
	
	public boolean addElement(BaseElement element) {
		if (!list.contains(element)) {
			return list.add(element);
		}
		return false;
	}

	@Override
	public boolean canExecute() {
		if (list == null || list.isEmpty())
			return false;
		for (Iterator<BaseElement> it=list.iterator(); it.hasNext(); ) {
			if (!isCopyableElement(it.next()))
				return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if (canExecute())
			Clipboard.getDefault().setContents(list);
	}

	@Override
	public boolean canUndo() {
		return false;
	}
	
	public boolean isCopyableElement(BaseElement element) {
		if(element instanceof PaperElement)
			return false;
		
		return true;
	}
	
	protected List<BaseElement> getSelectedList() {
		return list;
	}
}