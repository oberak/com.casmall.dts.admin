package com.casmall.dts.biz.domain;

import com.casmall.common.BaseObject;

/**
 * 인증관리
 * @author OBERAK
 */
public class TsAuthMgtDTO extends BaseObject {
    private static final long serialVersionUID = 1L;
    /** 일련번호 */
    private long auth_seq;
    /** 인증 key */
    private String auth_key;
    /** 인증번호 */
    private String auth_num;
    /** 거래처명 */
    private String cst_nm;
    /** 거래처 전화번호 */
    private String cst_tel;
    /** 비고 */
    private String nt;
    /** 검색어 */
    private String search;
    
	public long getAuth_seq() {
    	return auth_seq;
    }
	public void setAuth_seq(long auth_seq) {
    	this.auth_seq = auth_seq;
    }
	public String getAuth_key() {
    	return auth_key;
    }
	public void setAuth_key(String auth_key) {
    	this.auth_key = auth_key;
    }
	public String getAuth_num() {
    	return auth_num;
    }
	public void setAuth_num(String auth_num) {
    	this.auth_num = auth_num;
    }
	public String getCst_nm() {
    	return cst_nm;
    }
	public void setCst_nm(String cst_nm) {
    	this.cst_nm = cst_nm;
    }
	public String getCst_tel() {
    	return cst_tel;
    }
	public void setCst_tel(String cst_tel) {
    	this.cst_tel = cst_tel;
    }
	public String getNt() {
    	return nt;
    }
	public void setNt(String nt) {
    	this.nt = nt;
    }
	public String getSearch() {
    	return search;
    }
	public void setSearch(String search) {
    	this.search = search;
    }
}
