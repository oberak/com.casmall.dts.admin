package com.casmall.dts.admin.print.commands;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.casmall.dts.admin.print.edit.PaperElementPart;
import com.casmall.dts.admin.print.model.BaseElement;

public class ArrowKeyChangeLayoutCommand extends Command {
	
	private List<?> selectedEditPartList;
	
	private Integer differenceWidthOrX;
	
	private Integer differenceHeightOrY;
	
	private boolean bTranslate = true;
	
	private boolean bChanged;

	public ArrowKeyChangeLayoutCommand(List<?> selectedEditPartList, Integer differenceWidthOrX, Integer differenceHeightOrY) {
		super();
		this.selectedEditPartList = selectedEditPartList;
		this.differenceWidthOrX = differenceWidthOrX;
		this.differenceHeightOrY = differenceHeightOrY;
	}

	public ArrowKeyChangeLayoutCommand(List<?> selectedEditPartList, Integer differenceWidthOrX, Integer differenceHeightOrY, boolean bTranslate) {
		this(selectedEditPartList, differenceWidthOrX, differenceHeightOrY);
		this.bTranslate = bTranslate;
	}

	@Override
	public boolean canExecute() {
		return selectedEditPartList != null && differenceWidthOrX != null && differenceHeightOrY != null;
	}

	@Override
	public void execute() {
		changeLayout();
	}
	
	/**
	 * 레이아웃을 변경합니다.
	 */
	private void changeLayout() {
		changeLayout(false);
	}
	
	/**
	 * 레이아웃을 변경합니다.
	 * @param bUndo
	 */
	private void changeLayout(boolean bUndo) {
		for (Object selectedObject : selectedEditPartList) {

			if (selectedObject instanceof PaperElementPart) {
				PaperElementPart BodyElementPart = (PaperElementPart) selectedObject;

				Object model = BodyElementPart.getModel();

				if (model instanceof BaseElement) {
					BaseElement ElementBase = (BaseElement) model;
					
					int wx = bUndo ? -differenceWidthOrX : differenceWidthOrX;
					int hy = bUndo ? -differenceHeightOrY : differenceHeightOrY;
					
					if (bTranslate) {
						ElementBase.setLayout(ElementBase.getLayout().getTranslated(wx, hy));
					} else {
						ElementBase.setLayout(ElementBase.getLayout().getResized(wx, hy));
					}
				}
			}
		}
		
		bChanged = !bUndo;
	}

	@Override
	public void redo() {
		changeLayout();
	}

	@Override
	public boolean canUndo() {
		return bChanged;
	}

	@Override
	public void undo() {
		changeLayout(true);
	}

}