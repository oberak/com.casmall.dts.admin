package com.casmall.dts.admin.print.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.casmall.dts.admin.print.edit.BoxElementPart;
import com.casmall.dts.admin.print.edit.tree.BoxElementTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.PropertyDescription;

/**
 * Box Element
 * 
 * @author oberak
 */
@GEFEditPart(editPartType=BoxElementPart.class)
@GEFTreeEditPart(editPartType=BoxElementTreeEditPart.class)
public class BoxElement extends BaseElement {
	@PropertyDescription(label = "배경 색상", category = "Box", field = "backgroundColor", type=Color.class)
	public static final String PROPERTY_BACKCOLOR = "PROPERTY_BACKCOLOR";
	@PropertyDescription(label = "라인 색상", category = "Box", field = "borderColor", type=Color.class)
	public static final String PROPERTY_BORDERCOLOR = "PROPERTY_BORDERCOLOR";
	@PropertyDescription(label = "라인 두께", category = "Box", field = "border")
	public static final String PROPERTY_BORDER = "PROPERTY_BORDER";
	@PropertyDescription(label = "가로", category = "위치", field = "posX")
	public static final String PROPERTY_POS_X = "PROPERTY_POS_X";
	@PropertyDescription(label = "세로", category = "위치", field = "posY")
	public static final String PROPERTY_POS_Y = "PROPERTY_POS_Y";
	@PropertyDescription(label = "너비", category = "크기", field = "width")
	public static final String PROPERTY_WIDTH = "PROPERTY_WIDTH";
	@PropertyDescription(label = "높이", category = "크기", field = "height")
	public static final String PROPERTY_HEIGHT = "PROPERTY_HEIGHT";
	@PropertyDescription(label = "인쇄 여부", category = "Etc", field = "prtYn", value={"예","아니오"})
	public static final String PROPERTY_PRT_YN = "PROPERTY_PRT_YN";
	@PropertyDescription(label = "설명", category = "Name", field = "name")
	public static final String PROPERTY_NAME = "PROPERTY_NAME";

	private Color backgroundColor;
	private Color borderColor;
	private double border;
	private int prtYn=0;

	public BoxElement() {
		super();
		setBackgroundColor(ColorConstants.white);
		setBorderColor(ColorConstants.black);
		setBorder(0.1);
		
		setName("Box -");
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		Color oldColor = this.backgroundColor;
		this.backgroundColor = backgroundColor;
		getListeners().firePropertyChange(PROPERTY_BACKCOLOR, oldColor, backgroundColor);
	}
	
	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		Color oldColor = this.borderColor;
		this.borderColor = borderColor;
		getListeners().firePropertyChange(PROPERTY_BORDERCOLOR, oldColor, borderColor);
	}

	public double getBorder() {
		return border;
	}

	public void setBorder(double border) {
		double oldBorder = this.border;
		this.border = border;
		getListeners().firePropertyChange(PROPERTY_BORDERCOLOR, oldBorder, border);
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
		// Copy 용
		BoxElement elem = new BoxElement();
		elem.setParent(getParent());
		elem.setName(getName());

		elem.setBackgroundColor(getBackgroundColor());
		elem.setBorder(getBorder());
		elem.setBorderColor(getBorderColor());
		elem.setPrtYn(getPrtYn());
		
		Rectangle rect = getLayout().getCopy();
		rect.x += 10;
		rect.y += 10;
		elem.setLayout(rect);
		
		return elem;
	}
}