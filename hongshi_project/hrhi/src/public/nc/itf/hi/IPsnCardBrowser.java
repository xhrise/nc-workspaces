package nc.itf.hi;

import nc.vo.hi.hi_301.HRMainVO;
import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;

public interface IPsnCardBrowser {
	/**
	 * ��ѯ����
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
	 * ������Աpk,��Ϣ���ֵ�vo�����Ա��Ϣ����Ϣ
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
	 * ͨ����˾�����Ͳ���������Ա������Χ,����һ��VO����,�������Ϊ��,�򷵻ظù�˾�µ�����vo
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
