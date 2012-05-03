package nc.itf.hi;

//import java.rmi.RemoteException;

import java.rmi.RemoteException;
import java.util.Hashtable;

import nc.vo.hi.hi_304.PsnInfReportVO;
import nc.vo.hi.hi_304.ReportConditionVO;
import nc.vo.hr.bd.setdict.FlddictVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.hr.pub.carddef.RptDefVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;

public interface IPsnInfReport {
	/**
	 * 根据信息集表名判断是否同期记录集。
	 * 
	 * @return boolean
	 * @param setcode
	 *            表编码
	 * @exception BusinessException
	 */
	public abstract boolean isSametimeSet(String setcode)
			throws BusinessException;

	/**
	 * 根据公司PK和UserID得到有权限的模板IDs
	 * 
	 * @param pk_corp
	 * @param userid
	 * @return
	 * @throws BusinessException
	 */
	public String[] getTemplateIDs(String pk_corp, String userid)
			throws BusinessException;

	/**
	 * 查询花名册信息
	 * 
	 * @param cvo
	 * @param qvos
	 * @param corpID
	 * @param iscope
	 * @param deptStr
	 * @param indocflag
	 * @param normalwheresql
	 * @param powerPsncl
	 * @return
	 * @throws BusinessException
	 */
	public abstract PsnInfReportVO[] queryData(ReportConditionVO cvo,
			String order, nc.vo.pub.query.ConditionVO[] qvos, String corpID,
			int iscope, String deptStr, String indocflag,
			String normalwheresql, String powerPsncl,
			BusinessFuncParser_sql funcParser,String pk_rptid,String[] tablesFromQueryDLG) throws BusinessException;

	/**
	 * 根据部门权限，获得人员的所有信息。 注意：参数powerWithCorp不能为空，否则不能查出数据;
	 * 如果不受部门权限限制,传入的HashMap格式为：HashMap(key = pk_corp,value = null )
	 * 
	 * @param powerWithCorp
	 * @param cvo
	 * @param qvos
	 * @param iscope
	 * @param bShowHis
	 * @param normalwheresql
	 * @return
	 * @throws BusinessException
	 */
	public abstract nc.vo.hr.pub.pageSplit.PagerResult queryDataByPower(
			String selCorp, String loginCorp, String userid,
			ReportConditionVO cvo, ConditionVO[] qvos, int iscope,
			boolean bShowHis, String normalwheresql, int pageNo,
			String moduleCode, int pageSize) throws BusinessException;

	/**
	 * 新的人员查询实现类，适用于新的查询模板
	 * @param selCorp
	 * @param loginCorp
	 * @param userid
	 * @param cvo
	 * @param wheresql ：查询模板返回的sql语句。
	 * @param tables ：查询模板返回的使用表数组。
	 * @param iscope
	 * @param bShowHis
	 * @param normalwheresql
	 * @param pageNo
	 * @param moduleCode
	 * @param pageSize
	 * @return
	 * @throws BusinessException
	 */
	public nc.vo.hr.pub.pageSplit.PagerResult queryDataByPowerByNewQueryDLG(
			String selCorp, String loginCorp, String userid,
			ReportConditionVO cvo, String wheresql, String[] tables,
			int iscope, boolean bShowHis, String normalwheresql, int pageNo,
			String moduleCode, int pageSize,
			BusinessFuncParser_sql bfpsBusinessFuncParser_sql,String pk_rptid,boolean includesub)
			throws BusinessException;
	/**
	 * 查询花名册信息
	 * 
	 * @param powerWithCorp
	 * @param cvo
	 * @param qvos
	 * @param iscope
	 * @param bShowHis
	 * @param normalwheresql
	 * @return
	 * @throws RemoteException
	 */
	public abstract PsnInfReportVO[] queryDataByPowerForExport(
			java.util.HashMap powerWithCorp, ReportConditionVO cvo,
			ConditionVO[] qvos, int iscope, boolean bShowHis,
			String normalwheresql, int[] exportpara) throws BusinessException;

	/**
	 * 查询花名册信息
	 * 
	 * @param powerWithCorp
	 * @param cvo
	 * @param qvos
	 * @param iscope
	 * @param bShowHis
	 * @param normalwheresql
	 * @return
	 * @throws RemoteException
	 */
	public abstract int queryDataCount(java.util.HashMap powerWithCorp,
			ReportConditionVO cvo, ConditionVO[] qvos, int iscope,
			boolean bShowHis, String normalwheresql) throws BusinessException;

	/**
	 * 查询花名册信息
	 * 
	 * @param cvo
	 * @param qvos
	 * @param corpCond
	 * @param iscope
	 * @param deptStr
	 * @param powerPsncl
	 * @return
	 * @throws BusinessException
	 */
	public abstract PsnInfReportVO[] queryDataForStat(ReportConditionVO cvo,String order,
			nc.vo.pub.query.ConditionVO[] qvos, String[] corpCond, int iscope,
			String deptStr, String powerPsncl,BusinessFuncParser_sql funcParser,String pk_rptid) throws BusinessException;

	/**
	 * 
	 * @param sql
	 * @param fields
	 * @return
	 * @throws BusinessException
	 */
	public PsnInfReportVO[] queryReportDatas(String sql, String[] fields)
			throws BusinessException;
	
	/**
	 * 
	 * @param pkCorp
	 * @param tableCodes
	 * @return
	 * @throws BusinessException
	 */
	public Hashtable queryFldRefType(String pkCorp, String[] tableCodes)
			throws BusinessException;
	
	/**
	 * 
	 * @param pkRptDef
	 * @return
	 * @throws BusinessException
	 */
	public FlddictVO[] queryOrderedFlddict(String pkRptDef) throws BusinessException;
	
	/**
	 * 
	 * @param pkRptDef
	 * @param flddictVOs
	 * @return
	 * @throws BusinessException
	 */
	public String[] insertOrderedFlddict(String pkRptDef,String userid ,FlddictVO[] flddictVOs) throws BusinessException;
	
	/**
	 * 
	 * @param userid
	 * @param funcode
	 * @throws BusinessException
	 */
	public void deleteDeptTmp (String userid,String funcode) throws BusinessException ;
	
	/**
	 * 
	 * @param userid
	 * @param funcode
	 * @param corpsUsedDataPower
	 * @param corpsNoUsedDataPower
	 * @throws BusinessException
	 */
	public void insertDeptTmp (String userid,String funcode,String[] corpsUsedDataPower,String[] corpsNoUsedDataPower) throws BusinessException ;
	
	/**
	 * 
	 * @param userid
	 * @param funcode
	 * @param deptsPower
	 * @throws BusinessException
	 */
	public void insertDeptTmp (String userid,String funcode,String[] deptsPower) throws BusinessException ;
	
	/**
	 * 
	 * @param rptDefVO
	 * @param copyFromPkRptDef
	 * @throws BusinessException
	 */
	public String copyPsnRpt(RptDefVO rptDefVO,String userid,String copyFromPkRptDef) throws BusinessException ;
}