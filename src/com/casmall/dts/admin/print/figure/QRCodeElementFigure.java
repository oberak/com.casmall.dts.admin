package com.casmall.dts.admin.print.figure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * QRCodeElement ȭ�� ó��
 * 
 * @author oberak
 */
public class QRCodeElementFigure extends Figure {

	public QRCodeElementFigure() {
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		
		// ����ó��
		setOpaque(true);
	}

	public void setLayout(Rectangle rect) {
		getParent().setConstraint(this, rect);
	}
}