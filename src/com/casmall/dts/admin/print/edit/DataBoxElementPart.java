package com.casmall.dts.admin.print.edit;
import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.casmall.dts.admin.print.edit.policies.ElementDeletePolicy;
import com.casmall.dts.admin.print.figure.DataBoxElementFigure;
import com.casmall.dts.admin.print.model.DataBoxElement;
import com.casmall.dts.admin.print.model.TextBoxElement;

/**
 * TextBoxElementFigure 를 생성하고 BoxElement 를 통해서 그려주는 로직을 처리
 * 
 * @author oberak
 */
public class DataBoxElementPart extends BaseElementPart {

	@Override
	protected IFigure createFigure() {
		IFigure figure = new DataBoxElementFigure();
		return figure;
	}

	@Override
	protected void refreshVisuals() {
		DataBoxElementFigure figure = (DataBoxElementFigure) getFigure();
		DataBoxElement model = (DataBoxElement) getModel();
		
		figure.setForegroundColor(model.getForegroundColor());
		figure.setBackgroundColor(model.getBackgroundColor());
		if(model.getBackgroundColor().getRGB().equals(new RGB(255,255,255))){
			figure.setOpaque(false);
		}else{
			figure.setOpaque(true);
		}
		figure.setBorderColor(model.getBorderColor());
		figure.setLayout(model.getLayout());
		figure.setStyle(model.getStyle());
		if(model.getBorder() <= 0)
			figure.setBorder(new LineBorder(new Color(null, 0,0,255),2,Graphics.LINE_DOT));
		else
			figure.setBorder(new LineBorder((int)(model.getBorder()*10.0)));
		
		// Sample 표시 부분 수정
		figure.setText(model.getSampleData());
		figure.setFontData(model.getFontData());

		if(model.getPrtYn() == 1){
			figure.setBorder(new LineBorder(new Color(null, 255,0,0),2,Graphics.LINE_DASHDOTDOT));
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);

		if (evt.getPropertyName().equals(DataBoxElement.PROPERTY_BORDER)) refreshVisuals();
		if (evt.getPropertyName().equals(DataBoxElement.PROPERTY_FORECOLOR)) refreshVisuals();
		if (evt.getPropertyName().equals(DataBoxElement.PROPERTY_BACKCOLOR)) refreshVisuals();
		if (evt.getPropertyName().equals(DataBoxElement.PROPERTY_BORDERCOLOR)) refreshVisuals();
		if (evt.getPropertyName().equals(DataBoxElement.PROPERTY_TEXT)) refreshVisuals();	
		if (evt.getPropertyName().equals(DataBoxElement.PROPERTY_FONT)) refreshVisuals();	
		if (evt.getPropertyName().equals(DataBoxElement.PROPERTY_STYLE)) refreshVisuals();
		if (evt.getPropertyName().equals(DataBoxElement.PROPERTY_FORMAT)) refreshVisuals();
		if (evt.getPropertyName().equals(TextBoxElement.PROPERTY_PRT_YN)) refreshVisuals();
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementDeletePolicy());
	}
}