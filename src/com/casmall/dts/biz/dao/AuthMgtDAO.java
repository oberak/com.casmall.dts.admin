package com.casmall.dts.biz.dao;

import java.util.ArrayList;

import com.casmall.dts.biz.domain.TsAuthMgtDTO;

/**
 * 인증관리 DAO
 * @author OBERAK
 */
public interface AuthMgtDAO {
	/**
	 * 인증정보 조회
	 * @return
	 */
	public ArrayList<TsAuthMgtDTO> selectTsAuthMgt(TsAuthMgtDTO dto);
	
	/**
	 * 인증일련번호 채번
	 * @return
	 */
	public long selectTsAuthMgtKey();
	
	/**
	 * 인증정보 Insert
	 * @param dto
	 * @return
	 */
	public int insertTsAuthMgt(TsAuthMgtDTO dto);
	
	/**
	 * 인증정보 Update
	 * @param dto
	 * @return
	 */
	public int updateTsAuthMgt(TsAuthMgtDTO dto);
}
