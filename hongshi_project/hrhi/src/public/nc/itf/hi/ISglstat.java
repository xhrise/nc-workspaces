package nc.itf.hi;

import nc.vo.hi.hi_305.QueryBsVO;
import nc.vo.hi.hi_305.SglstatHItemVO;
import nc.vo.hi.hi_305.SglstatHVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.pub.BusinessException;

public interface ISglstat {
	/**
	 * 根据主键在数据库中删除一个VO对象
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	public abstract void delete(SglstatHVO vo) throws BusinessException;

	/**
	 * 向数据库中插入一个VO对象
	 * 
	 * @param sglstatH
	 * @return
	 * @throws BusinessException
	 */
	public abstract String insert(SglstatHVO sglstatH) throws BusinessException;

	/**
	 * 向数据库中插入一个VO对象。
	 * 
	 * @param sglstatH
	 * @return
	 * @throws BusinessException
	 */
	public abstract String insertAs(SglstatHVO sglstatH)
			throws BusinessException;

	/**
	 * 处理子表(增、删、改)
	 * 
	 * @param itemvo
	 * @param querybsvos
	 * @param manageMode
	 * @return
	 * @throws BusinessException
	 */
	public abstract String manageItem(SglstatHItemVO itemvo,
			QueryBsVO[] querybsvos, int manageMode) throws BusinessException;// throws

	/**
	 * 根据公司查找VO
	 * 
	 * @param pkcorp
	 * @return
	 * @throws BusinessException
	 */
	public abstract SglstatHVO[] querySglstatVOsByCorp(String pkcorp)
			throws BusinessException;

	/**
	 * 根据公司查找VO
	 * 
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	public abstract SglstatHVO querySglstatVOsByPk(String pk)
			throws BusinessException;

	/**
	 * 查询有权限的子公司PKs
	 * 
	 * @param pk_corp
	 * @param userid
	 * @return
	 * @throws BusinessException
	 */
	public abstract String[] getChildCorpPowered(String pk_corp, String userid)
			throws BusinessException;

	/**
	 * 查询详细信息
	 * 
	 * @param itemid
	 * @param rcvo
	 * @param qvos
	 * @param sCorpWhereStr
	 * @param iPsnScope
	 * @param indocflag
	 * @param deptStr
	 * @param normalwheresql
	 * @return
	 * @throws BusinessException
	 */
	public abstract nc.vo.hi.hi_304.PsnInfReportVO[] queryDetailInfo(
			String itemid, nc.vo.hi.hi_304.ReportConditionVO rcvo,
			String order, nc.vo.pub.query.ConditionVO[] qvos,
			String sCorpWhereStr, int iPsnScope, String indocflag,
			String deptStr, String normalwheresql, String powerPsncl,
			BusinessFuncParser_sql funcParser,String pk_rptid,String[] tablesFromQueryDLG)
			throws BusinessException;

	/**
	 * 查询详细信息
	 * 
	 * @param itemid
	 * @param rcvo
	 * @param qvos
	 * @param corpScope
	 * @param iScope
	 * @param isMgr
	 * @param deptPowerMgr
	 * @return
	 * @throws BusinessException
	 */
	public abstract nc.vo.hi.hi_304.PsnInfReportVO[] queryDetailInfo(
			String itemid, nc.vo.hi.hi_304.ReportConditionVO rcvo,String order,
			nc.vo.pub.query.ConditionVO[] qvos, String[] corpScope, int iScope,
			boolean isMgr, String deptPowerMgr, BusinessFuncParser_sql funcParser,String pk_rptid) throws BusinessException;

	/**
	 * 
	 * @param headerid
	 * @param qvos
	 * @param scopeCond
	 * @param isMgr
	 * @param deptPowerMgr
	 * @param logDate
	 * @param scope
	 * @return
	 * @throws BusinessException
	 */
	public abstract Object[][] queryStatResultsNotFromView(String headerid,
			QueryBsVO[] qvos, String[] scopeCond, boolean isMgr,
			String deptPowerMgr, String logDate, int scope)
			throws BusinessException;

}