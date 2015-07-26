package com.casmall.dts.admin.print.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;

import com.casmall.dts.admin.print.edit.TextBoxElementPart;
import com.casmall.dts.admin.print.edit.tree.TextBoxElementTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.PropertyDescription;

/**
 * TextBox Element
 * 
 * @author oberak
 */
@GEFEditPart(editPartType=TextBoxElementPart.class)
@GEFTreeEditPart(editPartType=TextBoxElementTreeEditPart.class)
public class TextBoxElement extends BaseElement {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@PropertyDescription(label = "문자 색상", category = "Text", field = "foregroundColor", type=Color.class)
	public static final String PROPERTY_FORECOLOR = "PROPERTY_FORECOLOR";
	@PropertyDescription(label = "인쇄 문자", category = "Text", field = "text")
	public static final String PROPERTY_TEXT = "PROPERTY_TEXT";
	@PropertyDescription(label = "인쇄 Format", category = "Text", field = "format")
	public static final String PROPERTY_FORMAT = "PROPERTY_FORMAT";
	@PropertyDescription(label = "Font", category = "Text", field = "fontData", type=FontData.class)
	public static final String PROPERTY_FONT = "PROPERTY_FONT";
	@PropertyDescription(label = "라인 색상", category = "Box", field = "borderColor", type=Color.class)
	public static final String PROPERTY_BORDERCOLOR = "PROPERTY_BORDERCOLOR";
	@PropertyDescription(label = "배경 색상", category = "Box", field = "backgroundColor", type=Color.class)
	public static final String PROPERTY_BACKCOLOR = "PROPERTY_BACKCOLOR";
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
	@PropertyDescription(label = "정렬", category = "Text", field = "style", value={"좌측","중앙","우측"})
	public static final String PROPERTY_STYLE = "PROPERTY_STYLE";
	@PropertyDescription(label = "인쇄 여부", category = "Etc", field = "prtYn", value={"예","아니오"})
	public static final String PROPERTY_PRT_YN = "PROPERTY_PRT_YN";
	@PropertyDescription(label = "설명", category = "Name", field = "name")
	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	
	private Color foregroundColor;
	private Color backgroundColor;
	private Color borderColor;
	private double border;
	private String text;
	private FontData fontData;
	private int style;
	private int prtYn=0;
	private String format;

	public TextBoxElement() {
		super();
		setForegroundColor(ColorConstants.black);
		setBackgroundColor(ColorConstants.white);
		setBorderColor(ColorConstants.black);
		setFontData(new FontData("굴림", 14, SWT.BOLD));
		setBorder(0.0);
		setText("Text");
		setName("Text - ");
		setFormat("%s");
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		Color oldColor = this.foregroundColor;
		this.foregroundColor = foregroundColor;
		getListeners().firePropertyChange(PROPERTY_FORECOLOR, oldColor, foregroundColor);
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		String oldText = this.text;
		this.text = text;
		getListeners().firePropertyChange(PROPERTY_TEXT, oldText, text);
	}
	
	public FontData getFontData() {
		return fontData;
	}

	public void setFontData(FontData fontData) {
		FontData oldFontData = this.fontData;
		this.fontData = fontData;
		getListeners().firePropertyChange(PROPERTY_FONT, oldFontData, fontData);
	}
	
	public int getStyle() {
    	return style;
    }

	public void setStyle(int style) {
    	int oldStyle = this.style;
    	this.style = style;
		getListeners().firePropertyChange(PROPERTY_STYLE, oldStyle, style);
    }
	
	public int getPrtYn() {
    	return prtYn;
    }

	public void setPrtYn(int prtYn) {
    	double oldPrtYn = this.prtYn;
		this.prtYn = prtYn;
		getListeners().firePropertyChange(PROPERTY_PRT_YN, oldPrtYn, prtYn);
	}
	
	public String getFormat() {
    	return format;
    }

	public void setFormat(String format) {
		try {
    		String.format(format, text);
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("String format error - "+e.getMessage());
            }
            return;
        }
		String oldFormat = this.format;
    	this.format = format;
		getListeners().firePropertyChange(PROPERTY_FORMAT, oldFormat, format);
    }
	
	public String getSampleData(){
		return String.format(format, text);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// Copy 용
		TextBoxElement elem = new TextBoxElement();
		elem.setParent(getParent());
		elem.setName(getName());

		elem.setBorder(getBorder());
		elem.setFontData(getFontData());
		elem.setBackgroundColor(getBackgroundColor());
		elem.setForegroundColor(getForegroundColor());
		elem.setBorderColor(getBorderColor());
		elem.setText(getText());
		elem.setStyle(getStyle());
		elem.setPrtYn(getPrtYn());
		elem.setFormat(getFormat());
		
		Rectangle rect = getLayout().getCopy();
		rect.x += 10;
		rect.y += 10;
		elem.setLayout(rect);
		
		return elem;
	}
}