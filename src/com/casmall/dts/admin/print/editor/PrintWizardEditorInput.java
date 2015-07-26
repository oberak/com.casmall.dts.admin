package com.casmall.dts.admin.print.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.casmall.common.StringUtil;
import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.model.BoxElement;
import com.casmall.dts.admin.print.model.DataBoxElement;
import com.casmall.dts.admin.print.model.FixBoxElement;
import com.casmall.dts.admin.print.model.LineElement;
import com.casmall.dts.admin.print.model.PaperElement;
import com.casmall.dts.admin.print.model.TextBoxElement;
import com.casmall.dts.biz.domain.TsPrtAttrDTO;
import com.casmall.dts.biz.domain.TsPrtInfDTO;
import com.casmall.dts.biz.mgr.TsPrtManager;
import com.casmall.dts.common.DTSConstants;
import com.casmall.dts.common.ObjectUtil;
import com.casmall.dts.common.PrintUtil;
import com.casmall.usr.mgr.SessionManager;

public class PrintWizardEditorInput implements IEditorInput{
	protected final Log logger = LogFactory.getLog(getClass());
	/** 인쇄설정 일련번호 */
    private String name;
    /** 인쇄 항목 */
    private PaperElement paper;
    private TsPrtManager pm;
    
    private TsPrtInfDTO prtDto;
    ArrayList<TsPrtAttrDTO> attrList;
    
	public PrintWizardEditorInput(String name) {
	    super();
	    this.name = name;
	    pm = new TsPrtManager();
	    loadData(name);

    }

	private void loadData(String name){
//    	신규 생성
	    paper = new PaperElement();
	    if("".equals(name)){
	    	paper.setName("New");
	    	return;
	    }

	    prtDto = new TsPrtInfDTO();
	    prtDto.setPrt_seq(Long.parseLong(name));
	    ArrayList<TsPrtInfDTO> piList = pm.selectTsPrtInf(prtDto );
	    if(piList != null && piList.size() == 1){
	    	TsPrtInfDTO pi = piList.get(0);
	    	
	    	paper.setBssCdntX(pi.getBss_cdnt_x());
	    	paper.setBssCdntY(pi.getBss_cdnt_y());
	    	paper.setBssFont(new FontData(pi.getBss_font()));
	    	paper.setId(""+pi.getPrt_seq());
	    	
	    	paper.setName(pi.getPrt_nm());
	    	paper.setPaperHeight(pi.getPaper_height());
	    	paper.setPaperWidth(pi.getPaper_width());
	    	if("N".equals(pi.getWdt_prt_yn())){
	    		paper.setWdtPrtYn(0);
	    	}else{
	    		paper.setWdtPrtYn(1);
	    	}
	    	TsPrtAttrDTO pad = new TsPrtAttrDTO();
	    	pad.setPrt_seq(prtDto.getPrt_seq());
			attrList = pm.selectTsPrtAttr(pad);
			for(TsPrtAttrDTO pa : attrList){
				ArrayList<Double> area = StringUtil.splitWithPipe(pa.getArea());
				Rectangle layout = new Rectangle(pixelX(area.get(0) + paper.getBssCdntX()), pixelY(area.get(1)+paper.getBssCdntY()), pixelX(area.get(2)), pixelY(area.get(3)));

				if(DTSConstants.CD_ATTR_FLAG_BOX.equals(pa.getAttr_flg_cd())){
					BoxElement be = new BoxElement();
					be.setBackgroundColor(new Color(null, PrintUtil.getRGB(pa.getBg_color())));
					be.setBorderColor(new Color(null, PrintUtil.getRGB(pa.getLine_color())));
					be.setBorder(pa.getTkn());
					be.setId(""+pa.getAttr_seq());
					
					be.setLayout(layout );
					be.setName(pa.getAttr_nm());
					if(DTSConstants.FLAG_Y.equals(pa.getPrt_yn())){
						be.setPrtYn(0);
					}else{
						be.setPrtYn(1);
					}
					paper.addChlid(be);
				}else if(DTSConstants.CD_ATTR_FLAG_LINE.equals(pa.getAttr_flg_cd())){
					LineElement le = new LineElement();

					le.setBorder(pa.getTkn());
					le.setBorderColor(new Color(null, PrintUtil.getRGB(pa.getLine_color())));
					le.setId(""+pa.getAttr_seq());
					if( (pa.getStyle() & SWT.HORIZONTAL) > 0 ){
						le.setStyle(0);
					}else if( (pa.getStyle() & SWT.VERTICAL) > 0 ){
						le.setStyle(1);
					}
					le.setLayout(layout );
					le.setName(pa.getAttr_nm());
					if(DTSConstants.FLAG_Y.equals(pa.getPrt_yn())){
						le.setPrtYn(0);
					}else{
						le.setPrtYn(1);
					}
					paper.addChlid(le);
				}else if(DTSConstants.CD_ATTR_FLAG_TEXT.equals(pa.getAttr_flg_cd())){
					TextBoxElement tbe = new TextBoxElement();

					tbe.setBorder(pa.getTkn());
					tbe.setFontData(new FontData(pa.getFont()));
					tbe.setForegroundColor(new Color(null, PrintUtil.getRGB(pa.getFont_color())));
					tbe.setBorderColor(new Color(null, PrintUtil.getRGB(pa.getLine_color())));
					tbe.setBackgroundColor(new Color(null, PrintUtil.getRGB(pa.getBg_color())));
					tbe.setId(""+pa.getAttr_seq());
					if( (pa.getStyle() & SWT.LEFT) > 0 ){
						tbe.setStyle(0);
					}else if( (pa.getStyle() & SWT.CENTER) > 0 ){
						tbe.setStyle(1);
					}else if( (pa.getStyle() & SWT.RIGHT) > 0 ){
						tbe.setStyle(2);
					}
					tbe.setLayout(layout );
					tbe.setName(pa.getAttr_nm());
					tbe.setText(pa.getAttr_cd());
					if(DTSConstants.FLAG_Y.equals(pa.getPrt_yn())){
						tbe.setPrtYn(0);
					}else{
						tbe.setPrtYn(1);
					}
					if(pa.getData_fmt() != null)
						tbe.setFormat(pa.getData_fmt());
					paper.addChlid(tbe);
				}else if(DTSConstants.CD_ATTR_FLAG_DB.equals(pa.getAttr_flg_cd())){
					DataBoxElement dbe = new DataBoxElement();

					dbe.setBorder(pa.getTkn());
					dbe.setFontData(new FontData(pa.getFont()));
					dbe.setForegroundColor(new Color(null, PrintUtil.getRGB(pa.getFont_color())));
					dbe.setBorderColor(new Color(null, PrintUtil.getRGB(pa.getLine_color())));
					dbe.setBackgroundColor(new Color(null, PrintUtil.getRGB(pa.getBg_color())));
					dbe.setId(""+pa.getAttr_seq());

					// attr 변경 시 format,name,text 모두 변경 됨
					dbe.setAttrByName(pa.getAttr_cd());
					
					// format 변경 시 sample 변경 됨
					if(pa.getData_fmt() != null)
						dbe.setFormat(pa.getData_fmt());
					dbe.setName(pa.getAttr_nm());
					dbe.setText(pa.getAttr_cd());
					
					if( (pa.getStyle() & SWT.LEFT) > 0 ){
						dbe.setStyle(0);
					}else if( (pa.getStyle() & SWT.CENTER) > 0 ){
						dbe.setStyle(1);
					}else if( (pa.getStyle() & SWT.RIGHT) > 0 ){
						dbe.setStyle(2);
					}

					dbe.setLayout(layout );
					
					if(DTSConstants.FLAG_Y.equals(pa.getPrt_yn())){
						dbe.setPrtYn(0);
					}else{
						dbe.setPrtYn(1);
					}

					paper.addChlid(dbe);
				}else if(DTSConstants.CD_ATTR_FLAG_FIX.equals(pa.getAttr_flg_cd())){
					FixBoxElement dbe = new FixBoxElement();

					dbe.setBorder(pa.getTkn());
					dbe.setFontData(new FontData(pa.getFont()));
					dbe.setForegroundColor(new Color(null, PrintUtil.getRGB(pa.getFont_color())));
					dbe.setBorderColor(new Color(null, PrintUtil.getRGB(pa.getLine_color())));
					dbe.setBackgroundColor(new Color(null, PrintUtil.getRGB(pa.getBg_color())));
					dbe.setId(""+pa.getAttr_seq());

					// attr 변경 시 format,name,text 모두 변경 됨
					dbe.setAttrByName(pa.getAttr_cd());
					
					// format 변경 시 sample 변경 됨
					if(pa.getData_fmt() != null)
						dbe.setFormat(pa.getData_fmt());
					dbe.setName(pa.getAttr_nm());
					dbe.setText(pa.getAttr_cd());
					
					if( (pa.getStyle() & SWT.LEFT) > 0 ){
						dbe.setStyle(0);
					}else if( (pa.getStyle() & SWT.CENTER) > 0 ){
						dbe.setStyle(1);
					}else if( (pa.getStyle() & SWT.RIGHT) > 0 ){
						dbe.setStyle(2);
					}

					dbe.setLayout(layout );
					
					if(DTSConstants.FLAG_Y.equals(pa.getPrt_yn())){
						dbe.setPrtYn(0);
					}else{
						dbe.setPrtYn(1);
					}

					paper.addChlid(dbe);
				}else if(DTSConstants.CD_ATTR_FLAG_IMAGE.equals(pa.getAttr_flg_cd())){
				
				}else{
					if (logger.isErrorEnabled()) {
	                    logger.error("Attr type error!! - " + pa.getAttr_flg_cd());
                    }
				}
			}
	    }else{
	    	if (logger.isErrorEnabled()) {
	            logger.error("인쇄설정정보 조회 오류!!");
            }
	    }		
	}
	
	@Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
	    return null;
    }

	@Override
    public boolean equals(Object o) {
		 if (!(o instanceof PrintWizardEditorInput))
			 return false;
		 if(this.getName()==null)
			 return false;
	    return this.name.equals(((PrintWizardEditorInput)o).getName());
    }

	@Override
    public boolean exists() {
		return (this.name != null);
    }

	@Override
    public ImageDescriptor getImageDescriptor() {
		 return ImageDescriptor.getMissingImageDescriptor();
    }

	@Override
    public String getName() {
	    return name;
    }

	@Override
    public IPersistableElement getPersistable() {
	    return null;
    }

	@Override
    public String getToolTipText() {
	    return paper.getName();
    }

	public PaperElement getPaper() {
    	return paper;
    }

	public void save(boolean saveas, String name) throws Exception{
		
		ArrayList<TsPrtAttrDTO> saveList = new ArrayList<TsPrtAttrDTO>();
		
		if(prtDto == null || saveas){
			prtDto = (TsPrtInfDTO)ObjectUtil.getDefaultObject(TsPrtInfDTO.class.getName());
			// 출력정보 save
			try {
				// Insert
				prtDto.setBss_cdnt_x(paper.getBssCdntX());
				prtDto.setBss_cdnt_y(paper.getBssCdntY());
				prtDto.setBss_font(paper.getBssFont().toString());
				prtDto.setPaper_height(paper.getPaperHeight());
				prtDto.setPaper_width(paper.getPaperWidth());
				prtDto.setPrt_nm(paper.getName());
				if(saveas)
					prtDto.setPrt_nm(name);
				prtDto.setPrt_seq(pm.selectTsPrtInfKey());
				if(paper.getWdtPrtYn() == 0)
					prtDto.setWdt_prt_yn(DTSConstants.FLAG_N);
				else
					prtDto.setWdt_prt_yn(DTSConstants.FLAG_Y);
	            pm.insertTsPrtInf(prtDto );
            } catch (IOException e) {
            	if (logger.isErrorEnabled()) {
	                logger.error("인쇄설정 insert 오류 - "+e.getMessage());
                }
	            throw e;
            }
		}else{
			// Update
			prtDto.setBss_cdnt_x(paper.getBssCdntX());
			prtDto.setBss_cdnt_y(paper.getBssCdntY());
			prtDto.setBss_font(paper.getBssFont().toString());
			prtDto.setPaper_height(paper.getPaperHeight());
			prtDto.setPaper_width(paper.getPaperWidth());
			prtDto.setPrt_nm(paper.getName());
			if(paper.getWdtPrtYn() == 0)
				prtDto.setWdt_prt_yn(DTSConstants.FLAG_N);
			else
				prtDto.setWdt_prt_yn(DTSConstants.FLAG_Y);
			prtDto.setEdt_dt(new Date());
			prtDto.setEdt_id(SessionManager.getInstance().getUsr().getLgn_id());
            try {
	            pm.updateTsPrtInf(prtDto );
            } catch (IOException e) {
            	if (logger.isErrorEnabled()) {
	                logger.error("인쇄설정 update 오류 - "+e.getMessage());
                }
	            throw e;
            }
		}
        // 출력항목  Insert/Update
		List<BaseElement> childs = paper.getChildrenArray();
		if (logger.isDebugEnabled()) {
	        logger.debug("Child:"+childs.size());
        }
		for(BaseElement e : childs){
			TsPrtAttrDTO dto = findById(e.getId());
			if(dto == null || saveas){
				// Insert
				dto = (TsPrtAttrDTO)ObjectUtil.getDefaultObject(TsPrtAttrDTO.class.getName());
				dto.setPrt_seq(prtDto.getPrt_seq());
			}else{
				// Update
				dto.setEdt_dt(new Date());
				dto.setEdt_id(SessionManager.getInstance().getUsr().getLgn_id());
			}
			dto.setArea((e.getPosX()-prtDto.getBss_cdnt_x()) +"|"+(e.getPosY()-prtDto.getBss_cdnt_y())+"|"+e.getWidth()+"|"+e.getHeight());
			dto.setAttr_nm(e.getName());
			if(e instanceof BoxElement){
				BoxElement be = (BoxElement)e;

				dto.setAttr_flg_cd(DTSConstants.CD_ATTR_FLAG_BOX);
				dto.setAttr_cd("");
				dto.setBg_color(convert(be.getBackgroundColor()));
				dto.setData_fmt("");
				dto.setData_type_cd("");
				dto.setFont("");
				dto.setFont_color("");
				dto.setLine_color(convert(be.getBorderColor()));
				dto.setTkn(be.getBorder());
				if(be.getPrtYn()==0)
					dto.setPrt_yn(DTSConstants.FLAG_Y);
				else
					dto.setPrt_yn(DTSConstants.FLAG_N);
				dto.setStyle(0);
			}else if(e instanceof LineElement){
				LineElement te = (LineElement)e;
				dto.setAttr_flg_cd(DTSConstants.CD_ATTR_FLAG_LINE);
				dto.setLine_color(convert(te.getBorderColor()));
				dto.setTkn(te.getBorder());
				if(te.getPrtYn()==0)
					dto.setPrt_yn(DTSConstants.FLAG_Y);
				else
					dto.setPrt_yn(DTSConstants.FLAG_N);
				if(te.getStyle()==0)
					dto.setStyle(SWT.HORIZONTAL);
				else if(te.getStyle()==1)
					dto.setStyle(SWT.VERTICAL);
			}else if(e instanceof TextBoxElement){
				TextBoxElement te = (TextBoxElement)e;
				dto.setAttr_flg_cd(DTSConstants.CD_ATTR_FLAG_TEXT);
				dto.setAttr_cd(te.getText());
				dto.setBg_color(convert(te.getBackgroundColor()));
				dto.setData_fmt(te.getFormat());
				dto.setData_type_cd("");
				dto.setFont(te.getFontData().toString());
				dto.setFont_color(convert(te.getForegroundColor()));
				dto.setLine_color(convert(te.getBorderColor()));
				dto.setTkn(te.getBorder());
				if(te.getPrtYn()==0)
					dto.setPrt_yn(DTSConstants.FLAG_Y);
				else
					dto.setPrt_yn(DTSConstants.FLAG_N);
				if(te.getStyle()==0)
					dto.setStyle(SWT.LEFT);
				else if(te.getStyle()==1)
					dto.setStyle(SWT.CENTER);
				else if(te.getStyle()==2)
					dto.setStyle(SWT.RIGHT);
			}else if(e instanceof DataBoxElement){
				DataBoxElement dbe = (DataBoxElement)e;
				dto.setAttr_flg_cd(DTSConstants.CD_ATTR_FLAG_DB);
				dto.setAttr_cd(dbe.getAttrCd());
				dto.setBg_color(convert(dbe.getBackgroundColor()));
				dto.setData_fmt(dbe.getFormat());
				dto.setData_type_cd(dbe.getDataType());
				dto.setFont(dbe.getFontData().toString());
				dto.setFont_color(convert(dbe.getForegroundColor()));
				dto.setLine_color(convert(dbe.getBorderColor()));
				dto.setTkn(dbe.getBorder());
				if(dbe.getPrtYn()==0)
					dto.setPrt_yn(DTSConstants.FLAG_Y);
				else
					dto.setPrt_yn(DTSConstants.FLAG_N);
				if(dbe.getStyle()==0)
					dto.setStyle(SWT.LEFT);
				else if(dbe.getStyle()==1)
					dto.setStyle(SWT.CENTER);
				else if(dbe.getStyle()==2)
					dto.setStyle(SWT.RIGHT);
			}else if(e instanceof FixBoxElement){
				FixBoxElement fbe = (FixBoxElement)e;
				dto.setAttr_flg_cd(DTSConstants.CD_ATTR_FLAG_FIX);
				dto.setAttr_cd(fbe.getAttrCd());
				dto.setBg_color(convert(fbe.getBackgroundColor()));
				dto.setData_fmt(fbe.getFormat());
				dto.setData_type_cd(fbe.getDataType());
				dto.setFont(fbe.getFontData().toString());
				dto.setFont_color(convert(fbe.getForegroundColor()));
				dto.setLine_color(convert(fbe.getBorderColor()));
				dto.setTkn(fbe.getBorder());
				if(fbe.getPrtYn()==0)
					dto.setPrt_yn(DTSConstants.FLAG_Y);
				else
					dto.setPrt_yn(DTSConstants.FLAG_N);
				if(fbe.getStyle()==0)
					dto.setStyle(SWT.LEFT);
				else if(fbe.getStyle()==1)
					dto.setStyle(SWT.CENTER);
				else if(fbe.getStyle()==2)
					dto.setStyle(SWT.RIGHT);
			}
			saveList.add(dto);
		}
		
		// delete 처리..
		if(attrList !=null && !saveas){
			boolean isDel = true;
			for(TsPrtAttrDTO attr : attrList){
				isDel = true;
				for(TsPrtAttrDTO s : saveList){
					if(attr.getAttr_seq() == s.getAttr_seq()){
						isDel = false;
						break;
					}
				}
				if(isDel){
					attr.setDel_yn(DTSConstants.FLAG_Y);
					saveList.add(attr);
				}
			}
		}
		
        // 출력항목 save
		for(TsPrtAttrDTO dto : saveList){
			if(dto.getAttr_seq() > 0){
				try {
	                pm.updateTsPrtAttr(dto);
                } catch (IOException e) {
                	if (logger.isErrorEnabled()) {
    	                logger.error("인쇄항목 update 오류 - "+e.getMessage());
                    }
	                throw e;
                }
			}else{
				dto.setAttr_seq(pm.selectTsPrtAttrKey(dto));
				try {
	                pm.insertTsPrtAttr(dto);
                } catch (IOException e) {
                	if (logger.isErrorEnabled()) {
    	                logger.error("인쇄설정 insert 오류 - "+e.getMessage());
                    }
	                throw e;
                }
			}
		}
		
		// 재조회
		loadData(""+prtDto.getPrt_seq());
		this.name = ""+prtDto.getPrt_seq();
    }
	
	private TsPrtAttrDTO findById(String id){
		if(id == null || "".equals(id) || attrList == null)
			return null;
		
		for(TsPrtAttrDTO dto : attrList){
			if(dto.getAttr_seq() == Integer.valueOf(id))
				return dto;
		}
		return null;
	}
	
	private int pixelX(double mm){
		return (int) Math.round(mm * 10);
	}
	
	private int pixelY(double mm){
		return (int) Math.round(mm * 10);
	}
	
	private String convert(Color color){
		RGB rgb = color.getRGB();
		return rgb.red +"|"+rgb.green +"|"+rgb.blue;
	}
}
