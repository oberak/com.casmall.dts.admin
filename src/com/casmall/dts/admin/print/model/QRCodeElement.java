package com.casmall.dts.admin.print.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.casmall.dts.admin.print.edit.QRCodeElementPart;
import com.casmall.dts.admin.print.edit.tree.QRCodeElementTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.PropertyDescription;

/**
 * QRCode Element
 * 
 * @author oberak
 */
@GEFEditPart(editPartType=QRCodeElementPart.class)
@GEFTreeEditPart(editPartType=QRCodeElementTreeEditPart.class)
public class QRCodeElement extends BaseElement {
	@PropertyDescription(label = "���� ����", category = "QRCode", field = "borderColor", type=Color.class)
	public static final String PROPERTY_BORDERCOLOR = "PROPERTY_BORDERCOLOR";
	@PropertyDescription(label = "����", category = "��ġ", field = "posX")
	public static final String PROPERTY_POS_X = "PROPERTY_POS_X";
	@PropertyDescription(label = "����", category = "��ġ", field = "posY")
	public static final String PROPERTY_POS_Y = "PROPERTY_POS_Y";
	@PropertyDescription(label = "�ʺ�", category = "ũ��", field = "width")
	public static final String PROPERTY_WIDTH = "PROPERTY_WIDTH";
	@PropertyDescription(label = "����", category = "ũ��", field = "height")
	public static final String PROPERTY_HEIGHT = "PROPERTY_HEIGHT";
	@PropertyDescription(label = "�μ� ����", category = "Etc", field = "prtYn", value={"��","�ƴϿ�"})
	public static final String PROPERTY_PRT_YN = "PROPERTY_PRT_YN";
	@PropertyDescription(label = "����", category = "Name", field = "name")
	public static final String PROPERTY_NAME = "PROPERTY_NAME";

	private Color borderColor;
	private int prtYn=0;

	public QRCodeElement() {
		super();
		setBorderColor(ColorConstants.black);
		
		setName("QRCode -");
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		Color oldColor = this.borderColor;
		this.borderColor = borderColor;
		getListeners().firePropertyChange(PROPERTY_BORDERCOLOR, oldColor, borderColor);
	}

	public int getPrtYn() {
    	return prtYn;
    }

	public void setPrtYn(int prtYn) {
    	double oldPrtYn = this.prtYn;
		this.prtYn = prtYn;
		getListeners().firePropertyChange(PROPERTY_PRT_YN, oldPrtYn, prtYn);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// Copy ��
		QRCodeElement elem = new QRCodeElement();
		elem.setParent(getParent());
		elem.setName(getName());

		elem.setBorderColor(getBorderColor());
		elem.setPrtYn(getPrtYn());
		
		Rectangle rect = getLayout().getCopy();
		rect.x += 10;
		rect.y += 10;
		elem.setLayout(rect);
		
		return elem;
	}
}