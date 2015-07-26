package com.casmall.dts.biz.mgr;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.casmall.common.CryptoUtil;
import com.casmall.common.db.DBManager;
import com.casmall.dts.biz.dao.AuthMgtDAO;
import com.casmall.dts.biz.domain.TsAuthMgtDTO;
import com.casmall.dts.common.DTSConstants;

public class TsAuthManager {
	protected static Log logger = LogFactory.getLog(TsMstManager.class);
	
	private DBManager dbm = DBManager.getInstance();
	
	/**
	 * 인증정보 조회
	 * @param car
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<TsAuthMgtDTO> selectTsAuthMgt(TsAuthMgtDTO dto) throws Exception{
		SqlSession session = dbm.openSession();
		AuthMgtDAO dao = session.getMapper(AuthMgtDAO.class);
		try{
			ArrayList<TsAuthMgtDTO> list = dao.selectTsAuthMgt(dto);
			for(TsAuthMgtDTO au : list){
				au.setAuth_num(CryptoUtil.decrypt3DES(au.getAuth_num()));
			}
			return list;
		}finally{
			session.close();
		} // try
	}
	
	/**
	 * 인증일련번호 채번
	 * @return
	 */
	public long selectTsAuthMgtKey(){
		SqlSession session = dbm.openSession();
		AuthMgtDAO dao = session.getMapper(AuthMgtDAO.class);
		try{
			return dao.selectTsAuthMgtKey();
		}finally{
			session.close();
		} // try
	}
	
	/**
	 * 인증정보 Insert
	 * @param dto
	 * @return
	 * @throws IOException 
	 */
	public int insertTsAuthMgt(TsAuthMgtDTO dto) throws IOException{
		SqlSession session = dbm.openSession();
		AuthMgtDAO dao = session.getMapper(AuthMgtDAO.class);
		try{
			dto.setAuth_num(CryptoUtil.encrypt3DES(dto.getAuth_num()));
			int chk = dao.insertTsAuthMgt(dto);
			if(chk == 0){
				throw new IOException("인증정보 등록 건수 오류");
			}
			session.commit();
			return chk;
		}catch(Exception e){
			throw new IOException(e);
		}finally{
			session.close();
		} // try
	}
	
	/**
	 * 인증정보 Update
	 * @param dto
	 * @return
	 * @throws IOException 
	 */
	public int updateTsAuthMgt(TsAuthMgtDTO dto) throws IOException{
		SqlSession session = dbm.openSession();
		AuthMgtDAO dao = session.getMapper(AuthMgtDAO.class);
		try{
			dto.setAuth_num(CryptoUtil.encrypt3DES(dto.getAuth_num()));
			int chk = dao.updateTsAuthMgt(dto);
			if(chk == 0){
				throw new IOException("인증정보 수정 건수 오류");
			}
			session.commit();
			return chk;
		}catch(Exception e){
			throw new IOException(e);
		}finally{
			session.close();
		} // try
	}
	
	/**
	 *  인증정보 삭제
	 * @param dto
	 * @return
	 * @throws IOException 
	 */
	public int deleteTsAuthMgt(TsAuthMgtDTO dto) throws IOException{
		SqlSession session = dbm.openSession();
		AuthMgtDAO dao = session.getMapper(AuthMgtDAO.class);
		try{
			dto.setAuth_num(CryptoUtil.encrypt3DES(dto.getAuth_num()));
			dto.setDel_yn(DTSConstants.FLAG_Y);
			int chk = dao.updateTsAuthMgt(dto);
			if(chk == 0){
				throw new IOException("인증정보 삭제 건수 오류");
			}
			session.commit();
			return chk;
		}catch(Exception e){
			throw new IOException(e);
		}finally{
			session.close();
		} // try
	}
}// class
