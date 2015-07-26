package com.casmall.dts.admin.print.rulers.commands;
import org.eclipse.gef.commands.Command;

import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.rulers.ElementGuide;

public class ChangeGuideCommand extends Command {

	private BaseElement element;
	private ElementGuide oldGuide, newGuide;
	private int oldAlign, newAlign;
	private boolean horizontal;

	public ChangeGuideCommand(BaseElement element, boolean horizontalGuide) {
		super();
		this.element = element;
		horizontal = horizontalGuide;
	}

	protected void changeGuide(ElementGuide oldGuide, ElementGuide newGuide,
			int newAlignment) {
		if (oldGuide != null && oldGuide != newGuide) {
			oldGuide.detachElement(element);
		}
		// You need to re-attach the part even if the oldGuide and the newGuide
		// are the same
		// because the alignment could have changed
		if (newGuide != null) {
			newGuide.attachElement(element, newAlignment);
		}
	}

	public void execute() {
		// Cache the old values
		oldGuide = horizontal ? element.getHorizontalGuide() : element
				.getVerticalGuide();
		if (oldGuide != null)
			oldAlign = oldGuide.getAlignment(element);

		redo();
	}

	public void redo() {
		changeGuide(oldGuide, newGuide, newAlign);
	}

	public void setNewGuide(ElementGuide guide, int alignment) {
		newGuide = guide;
		newAlign = alignment;
	}

	public void undo() {
		changeGuide(newGuide, oldGuide, oldAlign);
	}

}