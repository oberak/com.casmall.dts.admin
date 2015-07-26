package com.casmall.dts.admin.print.commands;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.casmall.dts.admin.print.model.BaseElement;

/**
 * ElementBase의 layout(위치, 크기) 의 변경 명령
 * 
 * @author oberak
 */
public class ElementChangeLayoutCommand extends Command {
	private BaseElement model;
	
	private Rectangle layout;
	
	private Rectangle oldLayout;
	
	public void execute() {
		model.setLayout(layout);
	}
	
	public void setConstraint(Rectangle rect) {
		this.layout = rect;
	}

	public void setModel(Object model) {
		this.model = (BaseElement) model;
		this.oldLayout = this.model.getLayout();
	}
	
	public void undo() {
		model.setLayout(oldLayout);
	}
}