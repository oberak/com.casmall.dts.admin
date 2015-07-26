package com.casmall.dts.admin.print.figure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * FixBoxElement 拳搁 贸府
 * 
 * @author oberak
 */
public class FixBoxElementFigure extends Figure {

	private Label label;
	private int margin = 2;
	public FixBoxElementFigure() {
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		
		label = new Label();
		
		label.setLabelAlignment(Label.LEFT);
		add(label, ToolbarLayout.ALIGN_CENTER);
		setConstraint(label, new Rectangle(margin, margin, -1, -1));

		// 捧疙贸府
		setOpaque(true);
	}

	public void setLayout(Rectangle rect) {
		getParent().setConstraint(this, rect);
		
		setConstraint(label, new Rectangle(margin, margin, rect.width-margin*2, rect.height-margin*2));
	}
	
	public void setStyle(int style){
		if( style == 0){
			label.setLabelAlignment(Label.LEFT);
		}else if( style == 1){
			label.setLabelAlignment(Label.CENTER);
		} else if( style == 2){
			label.setLabelAlignment(Label.RIGHT);
		}
	}
	
	public void setText(String text) {
		label.setText(text);
	}
	
	public void setFontData(FontData fontData) {
		label.setFont(new Font(null, fontData));
	}

	@Override
    public void setForegroundColor(Color fg) {
		label.setForegroundColor(fg);
    }

	public void setBorderColor(Color borderColor) {
		super.setForegroundColor(borderColor);
    }
}