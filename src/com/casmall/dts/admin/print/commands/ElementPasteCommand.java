package com.casmall.dts.admin.print.commands;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import com.casmall.dts.admin.print.model.BaseElement;

public class ElementPasteCommand extends Command {
	
	private Map<BaseElement, BaseElement> datas = new HashMap<BaseElement, BaseElement>();

	@Override
	public boolean canExecute() {
		@SuppressWarnings("unchecked")
        List<BaseElement> list = (List<BaseElement>) Clipboard.getDefault().getContents();
		if (list == null || list.isEmpty())
			return false;
		
		for (Iterator<BaseElement> it=list.iterator(); it.hasNext(); ) {
			BaseElement element = it.next();
			if (isPastableElement(element))
				datas.put(element, null);
		}

		return true;
	}

	@Override
	public void execute() {
		if (!canExecute())
			return;
		
		for (Iterator<BaseElement> it=datas.keySet().iterator(); it.hasNext(); ) {
			BaseElement element = it.next();
			try {
				BaseElement clone = (BaseElement)element.clone();
				datas.put(element, clone);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		redo();
	}

	@Override
	public void redo() {
		for (Iterator<BaseElement> it=datas.values().iterator(); it.hasNext(); ) {
			BaseElement element = it.next();
			if (isPastableElement(element))
				element.getParent().addChlid(element);
		}
	}

	@Override
	public boolean canUndo() {
		return !(datas.isEmpty());
	}
	
	@Override
	public void undo() {
		for (Iterator<BaseElement> it=datas.keySet().iterator(); it.hasNext(); ) {
			BaseElement element = it.next();
			if (isPastableElement(element))
				element.getParent().removeChild(element);
		}
	}

	public boolean isPastableElement(BaseElement element) {
		return true;
	}
}