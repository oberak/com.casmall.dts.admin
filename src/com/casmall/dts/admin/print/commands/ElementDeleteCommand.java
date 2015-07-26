package com.casmall.dts.admin.print.commands;
import org.eclipse.gef.commands.Command;

import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.model.PaperElement;

public class ElementDeleteCommand extends Command {
	
	private BaseElement model;
	
	private BaseElement parentModel;
	
	public void execute() {
		parentModel.removeChild(model);
	}

	public void setModel(Object model) {
		this.model = (BaseElement) model;
	}

	public void setParentModel(Object parentModel) {
		this.parentModel = (BaseElement) parentModel;
	}
	
	public void undo() {
		parentModel.addChlid(model);
	}

	@Override
    public boolean canExecute() {
		if(model instanceof PaperElement){
			return false;
		}
		return true;
    }
}