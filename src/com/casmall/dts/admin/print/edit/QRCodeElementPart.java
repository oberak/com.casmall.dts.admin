package com.casmall.dts.admin.print.edit;
import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Color;

import com.casmall.dts.admin.print.edit.policies.ElementDeletePolicy;
import com.casmall.dts.admin.print.figure.QRCodeElementFigure;
import com.casmall.dts.admin.print.model.QRCodeElement;

/**
 * QRCodeElementFigure 를 생성하고 QRCodeElement 를 통해서 그려주는 로직을 처리
 * 
 * @author oberak
 */
public class QRCodeElementPart extends BaseElementPart {

	@Override
	protected IFigure createFigure() {
		IFigure figure = new QRCodeElementFigure();
		return figure;
	}

	@Override
	protected void refreshVisuals() {
		QRCodeElementFigure figure = (QRCodeElementFigure) getFigure();
		QRCodeElement model = (QRCodeElement) getModel();
		
		figure.setForegroundColor(model.getBorderColor());
		figure.setOpaque(false);
		figure.setLayout(model.getLayout());
		figure.setBorder(new LineBorder(new Color(null, 0,0,255),2,Graphics.LINE_DOT));
		
		if(model.getPrtYn() == 1){
			figure.setBorder(new LineBorder(new Color(null, 255,0,0),2,Graphics.LINE_DASHDOTDOT));
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		
		if (evt.getPropertyName().equals(QRCodeElement.PROPERTY_BORDERCOLOR)) refreshVisuals();
		if (evt.getPropertyName().equals(QRCodeElement.PROPERTY_PRT_YN)) refreshVisuals();
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementDeletePolicy());
	}
}