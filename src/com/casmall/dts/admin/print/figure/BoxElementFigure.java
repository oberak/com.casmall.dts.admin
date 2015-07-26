package com.casmall.dts.admin.print.figure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * BoxElement ȭ�� ó��
 * 
 * @author oberak
 */
public class BoxElementFigure extends Figure {

	public BoxElementFigure() {
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		
		// ����ó��
		setOpaque(true);
	}

	public void setLayout(Rectangle rect) {
		getParent().setConstraint(this, rect);
	}
}