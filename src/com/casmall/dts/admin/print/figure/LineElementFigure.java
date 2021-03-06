package com.casmall.dts.admin.print.figure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * LineElement 화면 처리
 * 
 * @author oberak
 */
public class LineElementFigure extends Figure {

	public LineElementFigure() {
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		
		// 투명처리
		setOpaque(true);
	}

	public void setLayout(Rectangle rect) {
		getParent().setConstraint(this, rect);
	}
}