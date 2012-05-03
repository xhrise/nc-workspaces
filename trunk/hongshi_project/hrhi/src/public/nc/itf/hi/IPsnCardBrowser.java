package nc.itf.hi;

import nc.vo.hi.hi_301.HRMainVO;
import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;

public interface IPsnCardBrowser {
	/**
	 * 查询人数
	 * 
	 * @param pk_corp
	 * @param deptdoclist
	 * @param iPsnScope
	 * @param conditions
	 * @param powerSql
	 * @param normalwheresql
	 * @return
	 * @throws BusinessException
	 */
	public int queryPersonCount(String pk_corp, String[] deptdoclist,
			int iPsnScope, ConditionVO[] conditions, String powerSql,
			String normalwheresql) throws BusinessException;

	/**
	 * 根据人员pk,信息集字典vo获得人员信息集信息
	 * 
	 * @param pk_psndoc
	 * @param tablecode
	 * @param corpID
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	public PsnDataVO[][] queryCorpPsndataBySet(String pk_psndoc,
			String[] tablecode, String corpID, int condition)
			throws BusinessException;

	/**
	 * 通过公司主键和部门主键人员归属范围,返回一个VO数组,如果部门为空,则返回该公司下的所有vo
	 * 
	 * @param pk_corp
	 * @param pk_psndoc
	 * @return
	 * @throws BusinessException
	 */
	public HRMainVO queryPsnByPsnIdScope(String pk_corp, String pk_psndoc,
			BusinessFuncParser_sql funcParser) throws BusinessException;

	/**
	 * 
	 * @param pk_corp
	 * @param pk_psndoc
	 * @return
	 * @throws BusinessException
	 */
	public HRMainVO queryPsnByPsnIdScopeNotTransPk(String pk_corp,
			String pk_psndoc, BusinessFuncParser_sql funcParser)
			throws BusinessException;


	/**
	 * 
	 * @param pk_corp
	 * @param deptdoclist
	 * @param iPsnScope
	 * @param conditions
	 * @param powerSql
	 * @param normalwheresql
	 * @return
	 * @throws BusinessException
	 */
	public HRMainVO[] queryAllByCorpDeptScopeIncludeChild(String pk_corp,
			String[] deptdoclist, int iPsnScope, ConditionVO[] conditions,
			String powerSql, String normalwheresql,
			BusinessFuncParser_sql funcParser) throws BusinessException;

	/**
	 * 
	 * @param pk_corp
	 * @param deptdoclist
	 * @param iPsnScope
	 * @param defFieldNames
	 * @param defFieldTypes
	 * @param sPsnPkFilter
	 * @param printpara
	 * @return
	 * @throws BusinessException
	 */
	public HRMainVO[] queryAllByCorpDeptScopeIncludeChild(String pk_corp,
			String[] deptdoclist, int iPsnScope, ConditionVO[] conditions,
			String powerSql, String normalwheresql, int[] printpara,
			BusinessFuncParser_sql funcParser) throws BusinessException;
}
