package com.casmall.dts.admin.print.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;

import com.casmall.dts.admin.print.edit.FixBoxElementPart;
import com.casmall.dts.admin.print.edit.tree.FixBoxElementTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.PropertyDescription;
import com.casmall.dts.common.DTSConstants;

/**
 * TextBox Element
 * 
 * @author oberak
 */
@GEFEditPart(editPartType=FixBoxElementPart.class)
@GEFTreeEditPart(editPartType=FixBoxElementTreeEditPart.class)
public class FixBoxElement extends BaseElement {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@PropertyDescription(label = "인쇄 항목", category = "Text", field = "attr", 
			value={"인쇄일시", "고객사 명", "고객사 주소", "고객사 연락처", "고객사 팩스",
			"공급사 명", "공급사 주소", "공급사 연락처", "공급사 팩스"})
	public static final String PROPERTY_TEXT = "PROPERTY_TEXT";
	@PropertyDescription(label = "인쇄 Format", category = "Text", field = "format")
	public static final String PROPERTY_FORMAT = "PROPERTY_FORMAT";
	@PropertyDescription(label = "폰트", category = "Text", field = "fontData", type=FontData.class)
	public static final String PROPERTY_FONT = "PROPERTY_FONT";
	@PropertyDescription(label = "폰트 색상", category = "Text", field = "foregroundColor", type=Color.class)
	public static final String PROPERTY_FORECOLOR = "PROPERTY_FORECOLOR";
	@PropertyDescription(label = "정렬", category = "Text", field = "style", value={"좌측","중앙","우측"})
	public static final String PROPERTY_STYLE = "PROPERTY_STYLE";

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

	@PropertyDescription(label = "인쇄 여부", category = "Etc", field = "prtYn", value={"예","아니오"})
	public static final String PROPERTY_PRT_YN = "PROPERTY_PRT_YN";
	@PropertyDescription(label = "설명", category = "Etc", field = "name")
	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	
	private String[] dataAttrs = {"sysdate","cust_nm","cust_add","cust_tel", "cust_fax",
			"sup_nm", "sup_add", "sup_tel", "sup_fax"};
	private String[] dataNames = {"인쇄일시", "고객사 명", "고객사 주소", "고객사 연락처", "고객사 팩스",
	"공급사 명", "공급사 주소", "공급사 연락처", "공급사 팩스"};
	private String[] dataTypes = {DTSConstants.CD_DATA_TYPE_DATE,DTSConstants.CD_DATA_TYPE_STR,DTSConstants.CD_DATA_TYPE_STR,DTSConstants.CD_DATA_TYPE_STR,DTSConstants.CD_DATA_TYPE_STR,
			DTSConstants.CD_DATA_TYPE_STR,DTSConstants.CD_DATA_TYPE_STR,DTSConstants.CD_DATA_TYPE_STR,DTSConstants.CD_DATA_TYPE_STR};
	private String[] dataFormats = {"yyyy-MM-dd HH:mm:ss", "%s", "%s", "%s", "%s",
			"%s","%s","%s","%s"};
	private String[] dataSamples = {"2010-11-22 16:30:42", "(주)달구지", "서울시 강남구 역삼2동 123-45", "02)777-7777", "02)777-7778",
			"(주) 카스메카트로닉스", "경기도 평택시 세교동 406-14", "031)618-3500", "031)618-3501"};
	
	private Color foregroundColor;
	private Color backgroundColor;
	private Color borderColor;
	private double border;
	private String text;
	private FontData fontData;
	private int style;
	private int prtYn=0;
	private int attr;
	private String format;

	public FixBoxElement() {
		super();
		setForegroundColor(ColorConstants.black);
		setBackgroundColor(ColorConstants.white);
		setBorderColor(ColorConstants.black);
		setFontData(new FontData("굴림", 14, SWT.BOLD));
		setBorder(0.0);
		setAttr(0);
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
	
	public int getAttr() {
    	return attr;
    }

	public String getAttrCd() {
    	return dataAttrs[attr];
    }

	public void setAttrByName(String attr) {
		for(int i=0;i<dataAttrs.length;i++){
			if(dataAttrs[i].equals(attr)){
				setAttr(i);
				break;
			}
		}
    }
	
	public void setAttr(int attr) {
    	int oldAttr = this.attr;
    	this.attr = attr;
		setText(dataNames[attr]);
		setFormat(dataFormats[attr]);
		setName(getText());
		getListeners().firePropertyChange(PROPERTY_TEXT, oldAttr, attr);
    }

	public String getFormat() {
    	return format;
    }

	public void setFormat(String format) {
    	
    	if(!"".equals(format)){
    		if(DTSConstants.CD_DATA_TYPE_DATE.equals(getDataType())){
    			try {
        				SimpleDateFormat sdf = new SimpleDateFormat(format);
    	            	sdf.format(new Date());
                } catch(IllegalArgumentException e){
                	if (logger.isWarnEnabled()) {
    	                logger.warn("Date format error - "+e.getMessage());
                    }
                	return ;
                }
	    	}else if(DTSConstants.CD_DATA_TYPE_NUM.equals(getDataType())){
	    		try {
		    		NumberFormat nf = new DecimalFormat(format);
	                nf.format(123456.78);
                } catch (Exception e) {
	                if (logger.isWarnEnabled()) {
	                    logger.warn("Number format error - "+e.getMessage());
                    }
	                return;
                }
	    	}else if(DTSConstants.CD_DATA_TYPE_STR.equals(getDataType())){
	    		try {
		    		String.format(format, dataSamples[attr]);
                } catch (Exception e) {
	                if (logger.isWarnEnabled()) {
	                    logger.warn("String format error - "+e.getMessage());
                    }
	                return;
                }
	    	}
		}
    	
		String oldFormat = this.format;
    	this.format = format;
		getListeners().firePropertyChange(PROPERTY_FORMAT, oldFormat, format);
    }
	
	public String getDataType(){
		return dataTypes[attr];
	}
	
	public String getSampleData(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(DTSConstants.CD_DATA_TYPE_DATE.equals(getDataType())){
			try {
	            Date dt = sdf.parse(dataSamples[attr]);
	            if(!"".equals(format)){
	            	sdf = new SimpleDateFormat(format);
	            	return sdf.format(dt);
	            }
            } catch (ParseException e) {
	            if (logger.isWarnEnabled()) {
	                logger.warn("Date parse error - "+e.getMessage());
                }
            } catch(IllegalArgumentException e){
            	if (logger.isWarnEnabled()) {
	                logger.warn("Date format error - "+e.getMessage());
                }
            	return "#N/A";
            }
		}else if(DTSConstants.CD_DATA_TYPE_NUM.equals(getDataType())){
			Double val = Double.parseDouble(dataSamples[attr]);
			if(!"".equals(format)){
				NumberFormat nf = new DecimalFormat(format);
				return nf.format(val);
			}
		}else if(DTSConstants.CD_DATA_TYPE_STR.equals(getDataType())){
			if(!"".equals(format)){
				return String.format(format, dataSamples[attr]);
			}
		}
		
		return dataSamples[attr];
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// Copy 용
		FixBoxElement elem = new FixBoxElement();
		elem.setBackgroundColor(getBackgroundColor());
		elem.setParent(getParent());

		elem.setBorder(getBorder());
		elem.setFontData(getFontData());
		elem.setForegroundColor(getForegroundColor());
		elem.setBorderColor(getBorderColor());
		elem.setText(getText());
		elem.setName(getName());
		elem.setStyle(getStyle());
		elem.setPrtYn(getPrtYn());
		elem.setAttr(getAttr());
		elem.setFormat(getFormat());
		
		Rectangle rect = getLayout().getCopy();
		rect.x += 10;
		rect.y += 10;
		elem.setLayout(rect);
		
		return elem;
	}
}