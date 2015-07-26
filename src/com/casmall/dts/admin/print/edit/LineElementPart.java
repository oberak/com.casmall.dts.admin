package com.casmall.dts.admin.print.edit;
import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Color;

import com.casmall.dts.admin.print.edit.policies.ElementDeletePolicy;
import com.casmall.dts.admin.print.figure.LineElementFigure;
import com.casmall.dts.admin.print.model.LineElement;

/**
 * LineElementFigure 를 생성하고 BoxElement 를 통해서 그려주는 로직을 처리
 * 
 * @author oberak
 */
public class LineElementPart extends BaseElementPart {

	@Override
	protected IFigure createFigure() {
		IFigure figure = new LineElementFigure();
		return figure;
	}

	@Override
	protected void refreshVisuals() {
		LineElementFigure figure = (LineElementFigure) getFigure();
		LineElement model = (LineElement) getModel();
		
		figure.setForegroundColor(model.getBorderColor());
		figure.setLayout(model.getLayout());
		if(model.getBorder() <= 0){
			figure.setBorder(new LineBorder(new Color(null, 0,0,255),2,Graphics.LINE_DOT));
		}else{
			figure.setBorder(new LineBorder((int)(model.getBorder()*10)));
		}
		
		if(model.getPrtYn() == 1){
			figure.setBorder(new LineBorder(new Color(null, 255,0,0),(int)(model.getBorder()*10.0),Graphics.LINE_DASHDOTDOT));
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		
		if (evt.getPropertyName().equals(LineElement.PROPERTY_BORDER)) refreshVisuals();
		if (evt.getPropertyName().equals(LineElement.PROPERTY_BORDERCOLOR)) refreshVisuals();
		if (evt.getPropertyName().equals(LineElement.PROPERTY_PRT_YN)) refreshVisuals();
		if (evt.getPropertyName().equals(LineElement.PROPERTY_LENGTH)) refreshVisuals();
		if (evt.getPropertyName().equals(LineElement.PROPERTY_STYLE)) refreshVisuals();
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementDeletePolicy());
	}
}