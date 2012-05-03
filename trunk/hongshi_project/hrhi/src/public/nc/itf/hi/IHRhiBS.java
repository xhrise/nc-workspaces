package nc.itf.hi;


import nc.vo.hi.hi_301.BDFormuleVO;
import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.pub.BusinessException;

public interface IHRhiBS {

	/** --解决V30中问题编码为200507111204423395 和 200507210913444548中的同步问题
	 * 插入子表一条记录
	 * 创建日期：(2002-4-5 14:50:05)
	 * @return java.lang.String
	 * @param psndataVO nc.vo.hi.hi_401.PsnDataVO
	 */
	public PsnDataVO insertPsnData(PsnDataVO psndataVO,
			BDFormuleVO[] tongbuFields, int iSetType) throws BusinessException;

	/** --解决V30中问题编码为200507111204423395 和 200507210913444548中的同步问题
	 * 用VO对象的属性值更新数据库。
	 * 创建日期：(2002-3-27)
	 * @param psndocMain nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.rmi.RemoteException 异常说明。
	 */
	public void updatePsnData(PsnDataVO psndataVO, BDFormuleVO[] tongbuFields,
			int itype) throws BusinessException;

	/**
	 * 用VO对象的属性值更新数据库。 --解决V30中问题编码为200507111204423395 和 200507210913444548中的同步问题
	 *
	 * 创建日期：(2002-3-27)
	 * @param psndocMain nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.rmi.RemoteException 异常说明。
	 */
	public void updatePsnData(PsnDataVO psndataVO, BDFormuleVO[] tongbuFields,
			int itype, PsnDataVO psndataVOname) throws BusinessException;

	/**
	 * 同步辅助信息。----解决V30中问题编码为200507111204423395 和 200507210913444548中的同步问题
	 * 创建日期：(2005-7-15 11:21:48)
	 * @param psnDataVO nc.vo.hi.hi_401.PsnDataVO
	 * @param tongbuFields nc.vo.hi.hi_301.BDFormuleVO[]
	 */
	public void updateBDAcc(PsnDataVO psnDataVO, BDFormuleVO[] tongbuFields)
			throws BusinessException;
	public void updateTimecard(String pk_psndoc,String value) throws BusinessException;
	public int getPsnclscopeByPK(String pk_psndoc) throws BusinessException;
	/**
	 * 调配到新公司后更新bd_psnbasdoc。
	 *
	 * 创建日期：(2002-3-27)
	 * @param node nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.sql.SQLException 异常说明。
	 */
	public void updateBDPsnBas(nc.vo.hi.hi_301.HRMainVO hrMainVO)throws BusinessException;
	/**
	 * 根据hi_psndoc_deptchg的VO更新bd_psndoc。
	 *
	 * 创建日期：(2002-3-27)
	 * @param node nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.sql.SQLException 异常说明。
	 */
	public String updateBDForDeptchg(nc.vo.hi.hi_401.PsnDataVO psndocMain,nc.vo.hi.hi_401.PsnDataVO namevo)throws BusinessException;
	/**
	 * 根据指定的数据的VO更新bd_accpsndoc。
	 *
	 * 创建日期：(2002-3-27)
	 * @param node nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.sql.SQLException 异常说明。
	 */
	public String updateBDbasdoc(nc.vo.hi.hi_401.PsnDataVO psndocMain,BDFormuleVO[] vos)throws BusinessException;
}