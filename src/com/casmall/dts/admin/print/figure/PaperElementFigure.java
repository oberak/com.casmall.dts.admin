package com.casmall.dts.admin.print.figure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * BodyElement ȭ�� ó��
 * @author oberak
 */
public class PaperElementFigure extends Figure {
	// figure �� ȭ�鿡 �׷����� ����
	// model ���� figure Ŭ������ ����: 
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