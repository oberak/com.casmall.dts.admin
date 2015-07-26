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
 * ������� ����
 * 
 * @author oberak
 */
@GEFEditPart(editPartType=PaperElementPart.class)
@GEFTreeEditPart(editPartType=PaperElementTreeEditPart.class)
public class PaperElement extends BaseElement {
	/** property ���� ���� */
	@PropertyDescription(label = "���� ��", category = "��Ÿ", field = "name")
	public static final String PROPERTY_PAPER_NAME = "PROPERTY_PAPER_NAME";
	@PropertyDescription(label = "���� �ʺ�", category = "�μ�", field = "paperWidth")
	public static final String PROPERTY_PAPER_WIDTH = "PROPERTY_PAPER_WIDTH";
	@PropertyDescription(label = "���� ����", category = "�μ�", field = "paperHeight")
	public static final String PROPERTY_PAPER_HEIGHT = "PROPERTY_PAPER_HEIGHT";
	@PropertyDescription(label = "����", category = "������", field = "bssCdntX")
	public static final String PROPERTY_BSS_CDNT_X = "PROPERTY_BSS_CDNT_X";
	@PropertyDescription(label = "����", category = "������", field = "bssCdntY")
	public static final String PROPERTY_BSS_CDNT_Y = "PROPERTY_BSS_CDNT_Y";
	@PropertyDescription(label = "��¹���", category = "�μ�", field = "wdtPrtYn", value={"����","����"})
	public static final String PROPERTY_WDT_PRT_YN = "PROPERTY_WDT_PRT_YN";
	@PropertyDescription(label = "�⺻��Ʈ", category = "��Ÿ", field = "bssFont", type=FontData.class)
	public static final String PROPERTY_BSS_FONT = "PROPERTY_BSS_FONT";
	
	/** ���� �ʺ� */
	private double paperWidth;
	/** ���� ���� */
	private double paperHeight;
	/** ������ǥ x :���� mm*/
	private double bssCdntX;
	/** ������ǥ y : ���� mm*/
	private double bssCdntY;
	/** ���� ��� ���� */
	private int wdtPrtYn = 0;
	/** �⺻ ��Ʈ */
	private FontData bssFont;
	
	public PaperElement(){
		super();
		setBssFont(new FontData("����", 10, SWT.NONE));
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