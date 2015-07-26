package com.casmall.dts.admin.print.model;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import com.casmall.dts.admin.print.edit.PaperElementPart;
import com.casmall.dts.admin.print.edit.tree.PaperElementTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFEditPart;
import com.casmall.dts.admin.print.model.annotations.GEFTreeEditPart;
import com.casmall.dts.admin.print.model.annotations.PropertyDescription;

/**
 * 출력정보 메인
 * 
 * @author oberak
 */
@GEFEditPart(editPartType=PaperElementPart.class)
@GEFTreeEditPart(editPartType=PaperElementTreeEditPart.class)
public class PaperElement extends BaseElement {
	/** property 변경 대응 */
	@PropertyDescription(label = "설정 명", category = "기타", field = "name")
	public static final String PROPERTY_PAPER_NAME = "PROPERTY_PAPER_NAME";
	@PropertyDescription(label = "용지 너비", category = "인쇄", field = "paperWidth")
	public static final String PROPERTY_PAPER_WIDTH = "PROPERTY_PAPER_WIDTH";
	@PropertyDescription(label = "용지 높이", category = "인쇄", field = "paperHeight")
	public static final String PROPERTY_PAPER_HEIGHT = "PROPERTY_PAPER_HEIGHT";
	@PropertyDescription(label = "가로", category = "기준점", field = "bssCdntX")
	public static final String PROPERTY_BSS_CDNT_X = "PROPERTY_BSS_CDNT_X";
	@PropertyDescription(label = "세로", category = "기준점", field = "bssCdntY")
	public static final String PROPERTY_BSS_CDNT_Y = "PROPERTY_BSS_CDNT_Y";
	@PropertyDescription(label = "출력방향", category = "인쇄", field = "wdtPrtYn", value={"세로","가로"})
	public static final String PROPERTY_WDT_PRT_YN = "PROPERTY_WDT_PRT_YN";
	@PropertyDescription(label = "기본폰트", category = "기타", field = "bssFont", type=FontData.class)
	public static final String PROPERTY_BSS_FONT = "PROPERTY_BSS_FONT";
	
	/** 용지 너비 */
	private double paperWidth;
	/** 용지 높이 */
	private double paperHeight;
	/** 기준좌표 x :단위 mm*/
	private double bssCdntX;
	/** 기준좌표 y : 단위 mm*/
	private double bssCdntY;
	/** 가로 출력 여부 */
	private int wdtPrtYn = 0;
	/** 기본 폰트 */
	private FontData bssFont;
	
	public PaperElement(){
		super();
		setBssFont(new FontData("굴림", 10, SWT.NONE));
	}
	
	public double getPaperWidth() {
    	return paperWidth;
    }
	public void setPaperWidth(double paperWidth) {
		double old = this.paperWidth;
    	this.paperWidth = paperWidth;
    	getListeners().firePropertyChange(PROPERTY_PAPER_WIDTH, old, paperHeight);
    }
	public double getPaperHeight() {
    	return paperHeight;
    }
	public void setPaperHeight(double paperHeight) {
		double old = this.paperHeight;
    	this.paperHeight = paperHeight;
    	
    	getListeners().firePropertyChange(PROPERTY_PAPER_HEIGHT, old, paperHeight);
    }
	public double getBssCdntX() {
    	return bssCdntX;
    }
	public void setBssCdntX(double bssCdntX) {
		double old = this.bssCdntX;
    	this.bssCdntX = bssCdntX;
    	List<BaseElement> childs = this.getChildrenArray();
    	for(BaseElement el : childs){
    		el.setPosX(el.getPosX()-old+bssCdntX);
    	}
    	
    	getListeners().firePropertyChange(PROPERTY_BSS_CDNT_X, old, bssCdntX);
    }
	public double getBssCdntY() {
    	return bssCdntY;
    }
	public void setBssCdntY(double bssCdntY) {
		double old = this.bssCdntY;
    	this.bssCdntY = bssCdntY;
    	
    	List<BaseElement> childs = this.getChildrenArray();
    	for(BaseElement el : childs){
    		el.setPosY(el.getPosY()-old+bssCdntY);
    	}
    	
    	getListeners().firePropertyChange(PROPERTY_BSS_CDNT_Y, old, bssCdntY);
    }
	public int getWdtPrtYn() {
    	return wdtPrtYn;
    }
	public void setWdtPrtYn(int wdtPrtYn) {
		int old = this.wdtPrtYn;
    	this.wdtPrtYn = wdtPrtYn;
    	
    	getListeners().firePropertyChange(PROPERTY_WDT_PRT_YN, old, wdtPrtYn);
    }
	public FontData getBssFont() {
    	return bssFont;
    }
	public void setBssFont(FontData bssFont) {
		FontData old = this.bssFont;
    	this.bssFont = bssFont;
    	
    	getListeners().firePropertyChange(PROPERTY_BSS_FONT, old, bssFont);
    }
}