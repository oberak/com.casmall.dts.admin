package com.casmall.dts.admin.print.commands;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.model.PaperElement;

public class ElementCreateCommand extends Command {
	
	private PaperElement parent;
	
	private BaseElement element;

	public void setParent(Object parent) {
		this.parent = (PaperElement) parent;
	}

	public void setElement(Object element) {
		this.element = (BaseElement)element;
	}

	public void setLayout(Rectangle rect) {
		if (element == null)
			return;
		element.setLayout(rect);
	}

	@Override
	public boolean canExecute() {
		if (element == null || parent == null)
			return false;
		return true;
	}

	@Override
	public void execute() {
		parent.addChlid(element);
	}

	@Override
	public boolean canUndo() {
		if (element == null || parent == null)
			return false;
		return parent.contains(element);
	}

	@Override
	public void undo() {
		parent.removeChild(element);
	}
	
}