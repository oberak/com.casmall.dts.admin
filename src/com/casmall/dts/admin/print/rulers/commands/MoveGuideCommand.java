package com.casmall.dts.admin.print.rulers.commands;
import java.util.Iterator;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.rulers.ElementGuide;

public class MoveGuideCommand extends Command {

	private int pDelta;
	private ElementGuide guide;

	public MoveGuideCommand(ElementGuide guide, int positionDelta) {
		super("Move guide");
		this.guide = guide;
		pDelta = positionDelta;
	}

	public void execute() {
		guide.setPosition(guide.getPosition() + pDelta);
		Iterator<?> iter = guide.getParts().iterator();
		while (iter.hasNext()) {
			BaseElement element = (BaseElement) iter.next();
			Rectangle layout = element.getLayout().getCopy();
			if (guide.isHorizontal()) {
				layout.y += pDelta;
			} else {
				layout.x += pDelta;
			}
			element.setLayout(layout);
		}
	}

	public void undo() {
		guide.setPosition(guide.getPosition() - pDelta);
		Iterator<?> iter = guide.getParts().iterator();
		while (iter.hasNext()) {
			BaseElement element = (BaseElement) iter.next();
			Rectangle layout = element.getLayout().getCopy();
			if (guide.isHorizontal()) {
				layout.y -= pDelta;
			} else {
				layout.x -= pDelta;
			}
			element.setLayout(layout);
		}
	}

}