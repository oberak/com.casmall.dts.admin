package com.casmall.dts.admin.print.figure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * BodyElement 화면 처리
 * @author oberak
 */
public class PaperElementFigure extends Figure {
	// figure 는 화면에 그려지는 역할
	// model 별로 figure 클래스를 생성: 
	private XYLayout layout;

	public PaperElementFigure() {
		layout = new XYLayout();
		setLayoutManager(layout);
		
//		setForegroundColor(ColorConstants.gray);
//		setBorder(new LineBorder(2));
	}
	
	public void setLayout(Rectangle rect) {
		setBounds(rect);
	}
	
	
}