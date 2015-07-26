package com.casmall.dts.admin.print.rulers.commands;
import org.eclipse.gef.commands.Command;

import com.casmall.dts.admin.print.rulers.ElementGuide;
import com.casmall.dts.admin.print.rulers.ElementRuler;

public class CreateGuideCommand extends Command {

	private ElementGuide guide;
	private ElementRuler parent;
	private int position;

	public CreateGuideCommand(ElementRuler parent, int position) {
		super("Create guide");
		this.parent = parent;
		this.position = position;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		if (guide == null)
			guide = new ElementGuide(!parent.isHorizontal());
		guide.setPosition(position);
		parent.addGuide(guide);
	}

	public void undo() {
		parent.removeGuide(guide);
	}

}