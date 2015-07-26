package com.casmall.dts.admin.print.rulers.commands;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.rulers.ElementGuide;
import com.casmall.dts.admin.print.rulers.ElementRuler;

public class DeleteGuideCommand extends Command {

	private ElementRuler parent;
	private ElementGuide guide;
	private Map<?, ?> oldParts;

	public DeleteGuideCommand(ElementGuide guide, ElementRuler parent) {
		super("Delete guide");
		this.guide = guide;
		this.parent = parent;
	}

	public boolean canUndo() {
		return true;
	}

    public void execute() {
		oldParts = new HashMap<Object, Object>(guide.getMap());
		Iterator<?> iter = oldParts.keySet().iterator();
		while (iter.hasNext()) {
			guide.detachElement((BaseElement) iter.next());
		}
		parent.removeGuide(guide);
	}

	public void undo() {
		parent.addGuide(guide);
		Iterator<?> iter = oldParts.keySet().iterator();
		while (iter.hasNext()) {
			BaseElement element = (BaseElement) iter.next();
			guide.attachElement(element, ((Integer) oldParts.get(element)).intValue());
		}
	}
}