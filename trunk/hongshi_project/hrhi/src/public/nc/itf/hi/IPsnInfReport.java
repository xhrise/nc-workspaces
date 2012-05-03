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
	 * ������Ϣ�������ж��Ƿ�ͬ�ڼ�¼����
	 * 
	 * @return boolean
	 * @param setcode
	 *            �����
	 * @exception BusinessException
	 */
	public abstract boolean isSametimeSet(String setcode)
			throws BusinessException;

	/**
	 * ���ݹ�˾PK��UserID�õ���Ȩ�޵�ģ��IDs
	 * 
	 * @param pk_corp
	 * @param userid
	 * @return
	 * @throws BusinessException
	 */
	public String[] getTemplateIDs(String pk_corp, String userid)
			throws BusinessException;

	/**
	 * ��ѯ��������Ϣ
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
	 * ���ݲ���Ȩ�ޣ������Ա��������Ϣ�� ע�⣺����powerWithCorp����Ϊ�գ������ܲ������;
	 * ������ܲ���Ȩ������,�����HashMap��ʽΪ��HashMap(key = pk_corp,value = null )
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
	 * �µ���Ա��ѯʵ���࣬�������µĲ�ѯģ��
	 * @param selCorp
	 * @param loginCorp
	 * @param userid
	 * @param cvo
	 * @param wheresql ����ѯģ�巵�ص�sql��䡣
	 * @param tables ����ѯģ�巵�ص�ʹ�ñ����顣
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
	 * ��ѯ��������Ϣ
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
	 * ��ѯ��������Ϣ
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
	 * ��ѯ��������Ϣ
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