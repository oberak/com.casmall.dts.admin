package com.casmall.dts.admin.print.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.casmall.dts.admin.print.edit.LineElementPart;
import com.casmall.dts.admin.print.edit.tree.LineElementTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.PropertyDescription;

/**
 * Box Element
 * 
 * @author oberak
 */
@GEFEditPart(editPartType=LineElementPart.class)
@GEFTreeEditPart(editPartType=LineElementTreeEditPart.class)
public class LineElement extends BaseElement {
	@PropertyDescription(label = "라인 색상", category = "Line", field = "borderColor", type=Color.class)
	public static final String PROPERTY_BORDERCOLOR = "PROPERTY_BORDERCOLOR";
	@PropertyDescription(label = "라인 두께", category = "Line", field = "border")
	public static final String PROPERTY_BORDER = "PROPERTY_BORDER";
	@PropertyDescription(label = "길이", category = "Line", field = "length")
	public static final String PROPERTY_LENGTH = "PROPERTY_LENGTH";
	@PropertyDescription(label = "스타일", category = "Line", field = "style", value={"가로","세로"})
	public static final String PROPERTY_STYLE = "PROPERTY_STYLE";
	@PropertyDescription(label = "가로", category = "위치", field = "posX")
	public static final String PROPERTY_POS_X = "PROPERTY_POS_X";
	@PropertyDescription(label = "세로", category = "위치", field = "posY")
	public static final String PROPERTY_POS_Y = "PROPERTY_POS_Y";
	@PropertyDescription(label = "인쇄 여부", category = "Etc", field = "prtYn", value={"예","아니오"})
	public static final String PROPERTY_PRT_YN = "PROPERTY_PRT_YN";
	@PropertyDescription(label = "설명", category = "Name", field = "name")
	public static final String PROPERTY_NAME = "PROPERTY_NAME";

	private Color borderColor;
	private double border;
	private int prtYn=0;
	private int style = 0;
	private double length;

	public LineElement() {
		super();
		setBorderColor(ColorConstants.black);
		setBorder(0.5);
		setLength(10.0);
		setName("Line -");
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
		setLayout(getLayout());
	}

	public int getPrtYn() {
    	return prtYn;
    }

	public void setPrtYn(int prtYn) {
    	double oldPrtYn = this.prtYn;
		this.prtYn = prtYn;
		getListeners().firePropertyChange(PROPERTY_PRT_YN, oldPrtYn, prtYn);
	}
	
	public int getStyle() {
    	return style;
    }

	public void setStyle(int style) {
    	int oldStyle = this.style;
    	this.style = style;
		getListeners().firePropertyChange(PROPERTY_STYLE, oldStyle, style);
		setLength(getLength());
    }

	public double getLength() {
    	return length;
    }

	public void setLength(double length) {
    	double oldLength = this.length;
    	this.length = length;
		getListeners().firePropertyChange(PROPERTY_LENGTH, oldLength, length);

		Rectangle newLayout = getLayout();
		if(getStyle() ==0){
			// 가로선
			newLayout.height = (int)(getBorder()*10);
			newLayout.width = (int)(length*10);
		}else{
			// 세로선
			newLayout.width = (int)(getBorder()*10);
			newLayout.height = (int)(length*10);
		}
		super.setLayout(newLayout);
    }
	
	public void setLayout(Rectangle newLayout) {
		if(getStyle() ==0){
			// 가로선
			length = newLayout.width/10.0;
			newLayout.height = (int)(getBorder()*10);
			newLayout.width = (int)(length*10);
		}else{
			// 세로선
			length = newLayout.height/10.0;
			newLayout.width = (int)(getBorder()*10);
			newLayout.height = (int)(length*10);
		}
		super.setLayout(newLayout);
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// Copy 용
		LineElement elem = new LineElement();
		elem.setParent(getParent());

		elem.setBorder(getBorder());
		elem.setBorderColor(getBorderColor());
		elem.setName(getName());
		elem.setPrtYn(getPrtYn());
		elem.setStyle(getStyle());
		elem.setLength(getLength());
		
		Rectangle rect = getLayout().getCopy();
		rect.x += 10;
		rect.y += 10;
		elem.setLayout(rect);
		
		return elem;
	}
}