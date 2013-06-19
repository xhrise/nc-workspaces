package nc.itf.hr.trn;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.vo.hi.hi_301.BDFormuleVO;
import nc.vo.hi.hi_301.HRMainVO;
import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.hr.tools.pub.HRAggVO;
import nc.vo.hrsm.hrsm_301.ApplyBillItemVO;
import nc.vo.hrsm.hrsm_301.StapplybHVO;
import nc.vo.pub.BusinessException;
import nc.vo.smtm.pub.PerfromPropVO;

public interface IStapplybH {

	/**
	 * ��������������� �������ڣ�(2003-7-30 8:33:01)
	 */
	public abstract void addToBlackName(String pk_psn, String cause,
			String userid, String logDate, String pk_corp,
			BusinessFuncParser_sql funcParser) throws BusinessException;

	/**
	 * ��VO���������ֵ�������ݿ⡣
	 * 
	 * �������ڣ�(2001-11-23)
	 * 
	 * @param overtimeh
	 *            nc.vo.tbm.tbm_013.OvertimehVO
	 * @exception java.rmi.BusinessException
	 *                �쳣˵����
	 */
	public abstract String approveLocalBillForSMTM(String billpk,
			String censor, String opinion) throws BusinessException;

	/**
	 * �÷������ڼ��������censor�Ƿ�Ե���billId������Ȩ�ޣ��������й�����ʱ����
	 * 
	 * �������ڣ�(2004-02-16)
	 * 
	 * @param true
	 *            ��˵��censor��Ȩ����������billId
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract boolean chkWorkflow(String censor, String billId)
			throws BusinessException;

	/**
	 * �������������ݿ���ɾ��һ��VO����
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract void deleteStapplybH(StapplybHVO vo)
			throws BusinessException;

	/**
	 * ����ͨ���� �������ڣ�(2002-10-18 19:09:43)
	 * 
	 * @return java.lang.String
	 * @param vo
	 *            nc.vo.trm.trm_003_1.LayoutAggVO
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract String doApprove(nc.vo.hr.comp.pf.PFAggVO vo) throws BusinessException;

	/**
	 * �ύ�� �������ڣ�(2002-10-18 19:10:11)
	 * 
	 * @return java.lang.String
	 * @param vo
	 *            nc.vo.trm.trm_003_1.LayoutAggVO
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract String doCommit(StapplybHVO vo) throws BusinessException;

	/**
	 * ���� �������ڣ�(2002-10-18 19:09:43)
	 * 
	 * @return java.lang.String
	 * @param vo
	 *            nc.vo.trm.trm_003_1.LayoutAggVO
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract String doUnApprove(nc.vo.hr.comp.pf.PFAggVO vo) throws BusinessException;

	/**
	 * �Ƿ���ڸ���Ա��Ϣ���������ݡ� �������ڣ�(2003-7-30 8:33:01)
	 */
	public abstract Boolean existApprovedPsnBill(String pk_psn, String billType)
			throws BusinessException;

	/**
	 * �Ƿ���ڸ���Ա��δ��ɵ���
	 * @param billType
	 * @param billno
	 * @param pk_psn
	 * @return
	 * @throws BusinessException
	 */
	public abstract String existThePsnBill(String billType,
			String billno,String... pk_psn) throws BusinessException;

	/**
	 * ��������Ա����Ա�������¹�˾�Ƿ��ظ��� �������ڣ�(2006-4-14)
	 */
	public abstract HRMainVO[] isExitRepeatedPsncode(
			String[] pk_psndocs, String pk_corp) throws BusinessException;

	/**
	 * �Ƿ���ں������� �������ڣ�(2006-4-14)
	 */
	public abstract String isExistPsndocBad(String pk_psndoc)
			throws BusinessException;

	/**
	 * �Ƿ���ں������� �������ڣ�(2006-4-14)
	 */
	public abstract String isExistPsndocBads(String[] pk_psndocs)
			throws BusinessException;
    /**
	 * ͨ������������õ�����ϸ��Ϣ_��ӡ�� �������ڣ�(2006-10-25)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract StapplybHVO findBillInfByMainPK(String key)
			throws BusinessException;
	/**
	 * ͨ���������VO����
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract StapplybHVO findStapplybHByPrimaryKey(String key)
			throws BusinessException;

	/**
	 * ���ͳ������(������bd_psndoc)
	 * 
	 * @param pk_corp
	 * @param itempk
	 * @param anatype
	 * @return
	 * @throws BusinessException
	 */
	public abstract Integer[] getTotalResult(String pk_corp, String[] itempk,
			Integer anatype) throws BusinessException;

	/**
	 * ��ǰ��Ա�Ƿ񱾹�˾��Ч��Ա
	 * 				������Ա���ߵ��䵽������˾����Ա
	 * 	
	 * �������ڣ�(2006-3-30 13:38:24)
	 * 
	 * @return boolean
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract boolean isEditNative(String pk_psndoc, String pk_corp)
			throws BusinessException;

	/**
	 * �����ݿ��в���һ��VO����
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @param stapplybH
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @return java.lang.String ������VO����������ַ�����
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract String insertStapplybH(StapplybHVO stapplybH,
			boolean isAutoGenBillNo,
			nc.vo.pub.billcodemanage.BillCodeObjValueVO vo)
			throws BusinessException;

	/**
	 * ִ�е���
	 * @param pk_corp ��½��˾
	 * @param currDate ��ǰ����
	 * @param billpks ��������
	 * @param hashPsncode ��Ա��������
	 * @param prop ִ�в���
	 * @throws BusinessException
	 */
	public abstract Map<String,String> perfromStaff(String pk_corp,
			nc.vo.pub.lang.UFDate currDate, String[] billpks,
			Hashtable<String,String> hashPsncode, PerfromPropVO prop,
			BusinessFuncParser_sql funcParser) throws BusinessException;
	/**
	 * ִ�е���(��������)��
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @param stapplybH
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract void performStaff(String pk_corp,
			nc.vo.pub.lang.UFDate currDate, nc.vo.pub.lang.UFDate effectdate,
			Boolean ifPerformIn, Boolean ifSyncDate, Boolean ifGeneWaBill,
			String[] billpks, boolean isInPhase, Hashtable hashPsncode,
			BusinessFuncParser_sql funcParser) throws BusinessException;

	/**
	 * ִ����ְ����
	 * 
	 * @param pk_corp
	 *            ��˾
	 * @param currDate
	 *            ��½����
	 * @param effectDate
	 *            ��ְ����
	 * @param pk_psndoc
	 *            ��Ա��������Ϊ��ʱ��ʾѡ��ĳ��ִ����ְ��
	 * @param ifSyncDate
	 *            �Ƿ�ͬ����ְ����
	 * @param bAddToBlackNames
	 *            �Ƿ���������
	 * @param billpks
	 *            ��������
	 * @throws BusinessException
	 */
	public abstract Map<String,String> performTurnOver(String pk_corp,
			nc.vo.pub.lang.UFDate currDate, nc.vo.pub.lang.UFDate effectDate,
			Boolean ifSyncDate, Boolean bAddToBlackNames, String[] billpks,
			String cause, String userid, BusinessFuncParser_sql funcParser)
			throws BusinessException;

	/**
	 * ͨ���������VO����
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract Hashtable psnDeptOrPsnClChange(String[] psnpks,
			String pk_corp) throws BusinessException;

	//��ѯ��Ҫִ�е����е����뵥
	public abstract StapplybHVO[] queryApplyBillByBillPKs(String[] billpks)
			throws BusinessException;

	//�õ�������ĵ���
	public abstract StapplybHVO[] queryApplyBillForStat(String pk_corp,
			String[] pk_psndocs) throws BusinessException;

	public abstract StapplybHVO[] queryApplyBillByPkpsndocs(String pk_corp,
			String billtype, nc.vo.pub.lang.UFDate currDate, String[] pk_psndocs)
			throws BusinessException;

	/**
	 * ͨ���������VO����
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract ApplyBillItemVO[] queryBillItem(String pk_corp,
			String billtype, String sttype) throws BusinessException;

	/**
	 * �˴����뷽�������� �������ڣ�(2003-7-31 17:02:32)
	 */
	public abstract nc.vo.hi.hi_301.HRMainVO[] queryByPerfCondAndQueryCond(
			String pk_corp, String billtype, nc.vo.pub.lang.UFDate currDate,
			String whereSql,String[] tableNames, String deptPower)
			throws BusinessException;

	/**
	 * ��ѯ��ϸ��Ϣ
	 * 
	 * �������ڣ�(2002-11-26 14:09:06)
	 * 
	 * @return nc.vo.hi.hi_304.PsnInfReportVO[]
	 */
	public abstract nc.vo.hi.hi_304.PsnInfReportVO[] queryDetailForStat(
			String pk_corp, String projType, String strWhereSql,
			nc.vo.hi.hi_304.ReportConditionVO conditionvo, String itemPk,
			String contentPk, String analysisType,
			String[] tableNames,
			BusinessFuncParser_sql funcParser)
			throws BusinessException;

	/**
	 * ��ѯ�Զ��嵵����ϸ��Ϣ�� �������ڣ�(2003-7-2 22:00:21)
	 */
	public abstract nc.vo.bd.def.DefdocVO[] queryDocsForStapply(
			String unitCode, String docListKey) throws BusinessException;

	/**
	 * ͨ����˾PK,pk_billtype��whereSql��ѯ���� 
	 * 
	 * TODO To change the template for this generated type comment go to Window -
	 * Preferences - Java - Code Style - Code Templates
	 */
	public abstract StapplybHVO[] queryStapplybHVOBy(String pk_corp,
			String pk_billtype, String whereSql,String[] tableNames, boolean isIN)
			throws BusinessException;

	/**
	 * ͨ���������VO����
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract nc.vo.bd.def.DefdocVO[] querySettedSTType(
			nc.vo.bd.def.DefdocVO[] defDoc) throws BusinessException;

	/**
	 * ͨ���������VO����
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract StapplybHVO[] queryStapplybHVOs(String sBilltype,
			String strWhere, String strTable, Integer nodeType, String sttype,
			String userid, String deptIds, String pk_corp)
			throws BusinessException;

	/**
	 * ��VO���������ֵ�������ݿ⡣
	 * 
	 * �������ڣ�(2001-11-23)
	 * 
	 * @param overtimeh
	 *            nc.vo.tbm.tbm_013.OvertimehVO
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract String rejectLocalBill(String billpk) throws BusinessException;

	/**
	 * ��VO���������ֵ�������ݿ⡣
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @param vo
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract void updateStapplybH(StapplybHVO vo)
			throws BusinessException;

	/**
	 * У�����ݵ���Ч�ԡ� �������ڣ�(2003-7-26 14:31:50)
	 */
	public abstract boolean validataFromBO(StapplybHVO applyVO, String jobRank,
			String jobSeries, String duty, String dutySeries)
			throws BusinessException;

	/**
	 * ����������ҵ�����͡� �������ڣ�(2004-10-24 ���� 12:30:03)
	 * 
	 * @return java.lang.String[]
	 * @param pk_corp
	 *            java.lang.String
	 * @param docname
	 *            java.lang.String
	 */
	public abstract String[] queryRelatedPkBusitypes(String pk_corp,
			String pk_billtype, String docname) throws BusinessException;

	/**
	 * ������������ͳ�Ʒ���
	 * @param pk_corp
	 * @param strWhereSql
	 * @param item
	 *            ͳ�Ʒ����еĶԱ���Ŀ
	 * @param content
	 *            ͳ�Ʒ����е�ͳ������
	 * @param anaType
	 * @param currUIClass
	 * @param currProjType
	 * @param deptPk
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws BusinessException
	 */
	public abstract Integer[][] statAnalyByCon(String pk_corp,
			String strWhereSql, String[] item, String content[],
			Integer anaType, Integer currUIClass, String currProjType,
			String deptPk, String beginDate, String endDate)
			throws BusinessException;

	/**
	 * �����ݿ��в���һ��VO����
	 * 
	 * �������ڣ�(2003-5-6)
	 * 
	 * @param stapplybH
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @return nc.vo.hi.hi_401.PsnDataVO��
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract nc.vo.hi.hi_401.PsnDataVO insertPsnData(PsnDataVO psndataVO,int iSetType,
			String pk_corp, boolean isInPhase) throws BusinessException;

	/**
	 * ��VO���������ֵ�������ݿ⡣
	 * 
	 * �������ڣ�(2003-5-6)
	 * @param isBefore ��ְ��ʼ����Ӧ�Ƿ���������һ�����������Ŀ�ʼ���� fengwei 2010-09-08
	 * @param stapplybH
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO 
	 * 
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract void updatePsnData(PsnDataVO psndataVO, int iSetType, PsnDataVO psndataVOname,
			boolean isInPhase, boolean isBefore) throws BusinessException;

	/**
	 * ͨ��������������Ϣ��ѯ��Ա��Ϣ�� �������ڣ�(2002-4-14 14:20:52)
	 * 
	 * @return nc.vo.hi.hi_301.PsndocMainVO
	 * @param pk_cop
	 *            java.lang.String
	 * @param userID
	 *            java.lang.String
	 * @param pk_psnDept
	 *            java.lang.String
	 * @param psnclscode
	 *            int
	 */
	public abstract HRMainVO[] queryAllByCorpOperScopeName(
			nc.vo.hi.hi_301.Global301VarVO gloVar, int psnclscode,
			nc.vo.pub.lang.UFBoolean b, String whereSql,String[] tableNames,
			String DepartId, String psnclIds, BusinessFuncParser_sql funcParser)
			throws BusinessException;

	/**
	 * ͨ��������ѯ��Ա��Ϣ�� �������ڣ�(2002-4-14 14:20:52)
	 * 
	 * @return nc.vo.hi.hi_301.PsndocMainVO
	 * @param pk_cop
	 *            java.lang.String
	 * @param userID
	 *            java.lang.String
	 * @param pk_psnDept
	 *            java.lang.String
	 * @param psnclscode
	 *            int
	 */
	public abstract HRMainVO[] queryAllByCorpOperScopeName(
			nc.vo.hi.hi_301.Global301VarVO gloVar, int psnclscode,
			nc.vo.pub.lang.UFBoolean b, String whereSql,String[] tableNames,
			BusinessFuncParser_sql funcParser) throws BusinessException;

    /**
	 * @author chexz
	 * �������������һ��BillDataModel�е�onDelete,onSave ��һ��������:)
	 */
	public Object onSave(Object data, String checkClassMethodName)
			throws Exception ;
	
	/**
	 * @author chexz
	 * �ڿ���в�ѯ�õ�
	 */
	public StapplybHVO[] new_queryStapplybHVOs(String strWhere,String currentCompany,String deptIds) throws BusinessException;
	/**
	 * ȡ����,��λ�ķ���
	 * @param typeKeyLsHash
	 * @return ���� ���� ����
	 * @throws Exception
	 */
	public Map<String,String> getTranslateName(Map<String,List<String>> typeKeyLsHash)throws Exception;
	/**
	 * @author chexz��������״̬
	 * @param billPK
	 * @param effectDate
	 * @throws Exception
	 */
	public void  upBillStat2ByBillPk(String billPK, int stat ,int stat2)throws Exception ;
	
	/**
	 * ͨ����˾������ѯ��˾����(added by gaox) ��������(2007-8-17 ����10:37:26)
	 * @param pk_corp(��˾����)
	 * @return String(��˾����)
	 * @throws BusinessException
	 */
	public String getCorpCodeByPK(String pk_corp) throws BusinessException;
	
	/**
	 * @author chexz
	 * ��������������
	 */
	public HRAggVO[] queryHeadByBodyVOs(String[] pk_hi_stapplyb_h)throws BusinessException;
	
	/**
	 * @author chexz
	 * ��������������
	 */
	public HRAggVO[] queryPersitenceDelegatorHeadByBodyVOs(Map<String,String> billWhereHash)throws BusinessException;
	
	/**
	 * * @
	 * ȡ��ְʱ��������ְʱ�����
	 */
	public java.util.Map performTurnOverValidate(String[] billpks,
			nc.vo.pub.lang.UFDate effectDate,String pk_corp) throws BusinessException ;

	public abstract String getTranslateName(String pk_corp, String pk_psndoc,String string) throws BusinessException;

	/**
	 * �õ�ĳ��λ��ֱ���ϼ�
	 * @author lianglj 2010-07-20
	 * @param om_job
	 * @return
	 */
	public HashSet<String> getDirectSuperiors(String om_job)throws BusinessException;
	
	
	/**
	 * ����Ȩ���е���Աpk�ҵ�Ȩ����ֱ���ϼ���Ա
	 * @author lianglj 2010-07-20
	 * @param curserid
	 * @return 
	 * @throws BusinessException
	 */
	public  HashSet<String> getPsnOm(String psnbasdoc,String corp)throws BusinessException;
	
	/**
	 * ����hr��id�ҵ�Ȩ����Ա��pk
	 * @author lianglj 2010-07-20
     * @param pk_psnbasdoc hr��Աid
     * @param curserid Ȩ����Աid
	 * @return 
	 * @throws BusinessException
	 */
	public  String getuserid(String psnbasdoc,String corp)throws BusinessException;
	
	/**
	 * ����Ȩ�޵�id�ҵ���Ӧҵ��Ա�����Ĺ�˾
	 * @author lianglj 2010-07-20
     * @param userid Ȩ����Աid
     * @param curserid Ȩ����Աid
	 * @return 
	 * @throws BusinessException
	 */
	public  String getBelongCorp(String userid)throws BusinessException;
	/**
	 * ����Ȩ����Ա��pk�ҵ�hr��id
	 * @author lianglj 2010-07-20
     * @param pk_psnbasdoc hr��Աid
     * @param curserid Ȩ����Աid
	 * @return 
	 * @throws BusinessException
	 */
	public String getpsnbasdoc(String userid,String corp) throws BusinessException;
	
	/**
	 * ���ݹ�˾�������ҵ���˾������
	 * @author lianglj 2010-08-26
     * @param pk_corp ��˾pk
	 * @return 
	 * @throws BusinessException
	 */
	public  String getCorpNameByPK(String pk_corp)throws BusinessException;
	
	
	
	/**
	 * ������Ա�Ƿ���ĳ�����µ���Ա
	 * @author lianglj 2010-08-26
     * @param pk_psnbasdoc hr��Աid
     * @param curserid Ȩ����Աid
	 * @return 
	 * @throws BusinessException
	 */
	public  Boolean isDeptPsndoc(String psnbasdoc,String dept)throws BusinessException;
	
	/**
	 * ���ҹ�˾���ϼ���˾
	 * @author lianglj 2010-08-28
     * @param corp ��˾id
	 * @return 
	 * @throws BusinessException
	 */
	public  String getCorpSuperiors(String corp)throws BusinessException;
	/**
	 * ������Ա�Ƿ���ĳ��˾�µ���Ա
	 * @author lianglj 2010-08-26
     * @param pk_psnbasdoc hr��Աid
     * @param corp 
	 * @return 
	 * @throws BusinessException
	 */
	public  Boolean isCorpPsndoc(String psnbasdoc,String corp)throws BusinessException;
	
	/**
	 * ����Ȩ�޵�id�ҵ���Ӧҵ��Ա�����ļ�ְ��˾
	 * @author lianglj 2010-07-20
     * @param userid Ȩ����Աid
     * @param curserid Ȩ����Աid
	 * @return 
	 * @throws BusinessException
	 */
	public  Vector getDeptPartCorp(String userid)throws BusinessException;

}