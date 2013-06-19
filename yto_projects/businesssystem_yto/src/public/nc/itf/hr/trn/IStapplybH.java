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
	 * 插入黑名单操作。 创建日期：(2003-7-30 8:33:01)
	 */
	public abstract void addToBlackName(String pk_psn, String cause,
			String userid, String logDate, String pk_corp,
			BusinessFuncParser_sql funcParser) throws BusinessException;

	/**
	 * 用VO对象的属性值更新数据库。
	 * 
	 * 创建日期：(2001-11-23)
	 * 
	 * @param overtimeh
	 *            nc.vo.tbm.tbm_013.OvertimehVO
	 * @exception java.rmi.BusinessException
	 *                异常说明。
	 */
	public abstract String approveLocalBillForSMTM(String billpk,
			String censor, String opinion) throws BusinessException;

	/**
	 * 该方法用于检查审批人censor是否对单据billId有审批权限（适用于有工作流时）。
	 * 
	 * 创建日期：(2004-02-16)
	 * 
	 * @param true
	 *            ：说明censor有权限审批单据billId
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract boolean chkWorkflow(String censor, String billId)
			throws BusinessException;

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract void deleteStapplybH(StapplybHVO vo)
			throws BusinessException;

	/**
	 * 审批通过。 创建日期：(2002-10-18 19:09:43)
	 * 
	 * @return java.lang.String
	 * @param vo
	 *            nc.vo.trm.trm_003_1.LayoutAggVO
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract String doApprove(nc.vo.hr.comp.pf.PFAggVO vo) throws BusinessException;

	/**
	 * 提交。 创建日期：(2002-10-18 19:10:11)
	 * 
	 * @return java.lang.String
	 * @param vo
	 *            nc.vo.trm.trm_003_1.LayoutAggVO
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract String doCommit(StapplybHVO vo) throws BusinessException;

	/**
	 * 弃审。 创建日期：(2002-10-18 19:09:43)
	 * 
	 * @return java.lang.String
	 * @param vo
	 *            nc.vo.trm.trm_003_1.LayoutAggVO
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract String doUnApprove(nc.vo.hr.comp.pf.PFAggVO vo) throws BusinessException;

	/**
	 * 是否存在该人员信息的审批单据。 创建日期：(2003-7-30 8:33:01)
	 */
	public abstract Boolean existApprovedPsnBill(String pk_psn, String billType)
			throws BusinessException;

	/**
	 * 是否存在该人员的未完成单据
	 * @param billType
	 * @param billno
	 * @param pk_psn
	 * @return
	 * @throws BusinessException
	 */
	public abstract String existThePsnBill(String billType,
			String billno,String... pk_psn) throws BusinessException;

	/**
	 * 待调入人员的人员编码在新公司是否重复。 创建日期：(2006-4-14)
	 */
	public abstract HRMainVO[] isExitRepeatedPsncode(
			String[] pk_psndocs, String pk_corp) throws BusinessException;

	/**
	 * 是否存在黑名单。 创建日期：(2006-4-14)
	 */
	public abstract String isExistPsndocBad(String pk_psndoc)
			throws BusinessException;

	/**
	 * 是否存在黑名单。 创建日期：(2006-4-14)
	 */
	public abstract String isExistPsndocBads(String[] pk_psndocs)
			throws BusinessException;
    /**
	 * 通过主表主键获得单据详细信息_打印用 创建日期：(2006-10-25)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract StapplybHVO findBillInfByMainPK(String key)
			throws BusinessException;
	/**
	 * 通过主键获得VO对象。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract StapplybHVO findStapplybHByPrimaryKey(String key)
			throws BusinessException;

	/**
	 * 获得统计总数(不包括bd_psndoc)
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
	 * 当前人员是否本公司有效人员
	 * 				引用人员或者调配到其它公司的人员
	 * 	
	 * 创建日期：(2006-3-30 13:38:24)
	 * 
	 * @return boolean
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract boolean isEditNative(String pk_psndoc, String pk_corp)
			throws BusinessException;

	/**
	 * 向数据库中插入一个VO对象。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @param stapplybH
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @return java.lang.String 所插入VO对象的主键字符串。
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract String insertStapplybH(StapplybHVO stapplybH,
			boolean isAutoGenBillNo,
			nc.vo.pub.billcodemanage.BillCodeObjValueVO vo)
			throws BusinessException;

	/**
	 * 执行调配
	 * @param pk_corp 登陆公司
	 * @param currDate 当前日期
	 * @param billpks 单据主键
	 * @param hashPsncode 人员工作主键
	 * @param prop 执行参数
	 * @throws BusinessException
	 */
	public abstract Map<String,String> perfromStaff(String pk_corp,
			nc.vo.pub.lang.UFDate currDate, String[] billpks,
			Hashtable<String,String> hashPsncode, PerfromPropVO prop,
			BusinessFuncParser_sql funcParser) throws BusinessException;
	/**
	 * 执行调配(复制数据)。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @param stapplybH
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract void performStaff(String pk_corp,
			nc.vo.pub.lang.UFDate currDate, nc.vo.pub.lang.UFDate effectdate,
			Boolean ifPerformIn, Boolean ifSyncDate, Boolean ifGeneWaBill,
			String[] billpks, boolean isInPhase, Hashtable hashPsncode,
			BusinessFuncParser_sql funcParser) throws BusinessException;

	/**
	 * 执行离职操作
	 * 
	 * @param pk_corp
	 *            公司
	 * @param currDate
	 *            登陆日期
	 * @param effectDate
	 *            离职日期
	 * @param pk_psndoc
	 *            人员主键（不为空时表示选定某人执行离职）
	 * @param ifSyncDate
	 *            是否同步到职日期
	 * @param bAddToBlackNames
	 *            是否加入黑名单
	 * @param billpks
	 *            单据主键
	 * @throws BusinessException
	 */
	public abstract Map<String,String> performTurnOver(String pk_corp,
			nc.vo.pub.lang.UFDate currDate, nc.vo.pub.lang.UFDate effectDate,
			Boolean ifSyncDate, Boolean bAddToBlackNames, String[] billpks,
			String cause, String userid, BusinessFuncParser_sql funcParser)
			throws BusinessException;

	/**
	 * 通过主键获得VO对象。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract Hashtable psnDeptOrPsnClChange(String[] psnpks,
			String pk_corp) throws BusinessException;

	//查询需要执行的所有的申请单
	public abstract StapplybHVO[] queryApplyBillByBillPKs(String[] billpks)
			throws BusinessException;

	//得到待调入的单据
	public abstract StapplybHVO[] queryApplyBillForStat(String pk_corp,
			String[] pk_psndocs) throws BusinessException;

	public abstract StapplybHVO[] queryApplyBillByPkpsndocs(String pk_corp,
			String billtype, nc.vo.pub.lang.UFDate currDate, String[] pk_psndocs)
			throws BusinessException;

	/**
	 * 通过主键获得VO对象。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract ApplyBillItemVO[] queryBillItem(String pk_corp,
			String billtype, String sttype) throws BusinessException;

	/**
	 * 此处插入方法描述。 创建日期：(2003-7-31 17:02:32)
	 */
	public abstract nc.vo.hi.hi_301.HRMainVO[] queryByPerfCondAndQueryCond(
			String pk_corp, String billtype, nc.vo.pub.lang.UFDate currDate,
			String whereSql,String[] tableNames, String deptPower)
			throws BusinessException;

	/**
	 * 查询详细信息
	 * 
	 * 创建日期：(2002-11-26 14:09:06)
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
	 * 查询自定义档案详细信息。 创建日期：(2003-7-2 22:00:21)
	 */
	public abstract nc.vo.bd.def.DefdocVO[] queryDocsForStapply(
			String unitCode, String docListKey) throws BusinessException;

	/**
	 * 通过公司PK,pk_billtype和whereSql查询单据 
	 * 
	 * TODO To change the template for this generated type comment go to Window -
	 * Preferences - Java - Code Style - Code Templates
	 */
	public abstract StapplybHVO[] queryStapplybHVOBy(String pk_corp,
			String pk_billtype, String whereSql,String[] tableNames, boolean isIN)
			throws BusinessException;

	/**
	 * 通过主键获得VO对象。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract nc.vo.bd.def.DefdocVO[] querySettedSTType(
			nc.vo.bd.def.DefdocVO[] defDoc) throws BusinessException;

	/**
	 * 通过主键获得VO对象。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @return nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract StapplybHVO[] queryStapplybHVOs(String sBilltype,
			String strWhere, String strTable, Integer nodeType, String sttype,
			String userid, String deptIds, String pk_corp)
			throws BusinessException;

	/**
	 * 用VO对象的属性值更新数据库。
	 * 
	 * 创建日期：(2001-11-23)
	 * 
	 * @param overtimeh
	 *            nc.vo.tbm.tbm_013.OvertimehVO
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract String rejectLocalBill(String billpk) throws BusinessException;

	/**
	 * 用VO对象的属性值更新数据库。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @param vo
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract void updateStapplybH(StapplybHVO vo)
			throws BusinessException;

	/**
	 * 校验数据的有效性。 创建日期：(2003-7-26 14:31:50)
	 */
	public abstract boolean validataFromBO(StapplybHVO applyVO, String jobRank,
			String jobSeries, String duty, String dutySeries)
			throws BusinessException;

	/**
	 * 获得相关联的业务类型。 创建日期：(2004-10-24 下午 12:30:03)
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
	 * 根据条件进行统计分析
	 * @param pk_corp
	 * @param strWhereSql
	 * @param item
	 *            统计分析中的对比项目
	 * @param content
	 *            统计分析中的统计内容
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
	 * 向数据库中插入一个VO对象。
	 * 
	 * 创建日期：(2003-5-6)
	 * 
	 * @param stapplybH
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO
	 * @return nc.vo.hi.hi_401.PsnDataVO。
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract nc.vo.hi.hi_401.PsnDataVO insertPsnData(PsnDataVO psndataVO,int iSetType,
			String pk_corp, boolean isInPhase) throws BusinessException;

	/**
	 * 用VO对象的属性值更新数据库。
	 * 
	 * 创建日期：(2003-5-6)
	 * @param isBefore 任职开始日期应是否早于最新一条工作履历的开始日期 fengwei 2010-09-08
	 * @param stapplybH
	 *            nc.vo.hrsm.hrsm_201.StapplybHVO 
	 * 
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract void updatePsnData(PsnDataVO psndataVO, int iSetType, PsnDataVO psndataVOname,
			boolean isInPhase, boolean isBefore) throws BusinessException;

	/**
	 * 通过条件、部门信息查询人员信息。 创建日期：(2002-4-14 14:20:52)
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
	 * 通过条件查询人员信息。 创建日期：(2002-4-14 14:20:52)
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
	 * 用这个方法包裹一个BillDataModel中的onDelete,onSave 到一个事务中:)
	 */
	public Object onSave(Object data, String checkClassMethodName)
			throws Exception ;
	
	/**
	 * @author chexz
	 * 在框架中查询用到
	 */
	public StapplybHVO[] new_queryStapplybHVOs(String strWhere,String currentCompany,String deptIds) throws BusinessException;
	/**
	 * 取部门,岗位的翻译
	 * @param typeKeyLsHash
	 * @return 主键 名称 对照
	 * @throws Exception
	 */
	public Map<String,String> getTranslateName(Map<String,List<String>> typeKeyLsHash)throws Exception;
	/**
	 * @author chexz更新主表状态
	 * @param billPK
	 * @param effectDate
	 * @throws Exception
	 */
	public void  upBillStat2ByBillPk(String billPK, int stat ,int stat2)throws Exception ;
	
	/**
	 * 通过公司主键查询公司编码(added by gaox) 创建日期(2007-8-17 上午10:37:26)
	 * @param pk_corp(公司主键)
	 * @return String(公司编码)
	 * @throws BusinessException
	 */
	public String getCorpCodeByPK(String pk_corp) throws BusinessException;
	
	/**
	 * @author chexz
	 * 根据主键查数据
	 */
	public HRAggVO[] queryHeadByBodyVOs(String[] pk_hi_stapplyb_h)throws BusinessException;
	
	/**
	 * @author chexz
	 * 根据主键查数据
	 */
	public HRAggVO[] queryPersitenceDelegatorHeadByBodyVOs(Map<String,String> billWhereHash)throws BusinessException;
	
	/**
	 * * @
	 * 取离职时间早于任职时间的人
	 */
	public java.util.Map performTurnOverValidate(String[] billpks,
			nc.vo.pub.lang.UFDate effectDate,String pk_corp) throws BusinessException ;

	public abstract String getTranslateName(String pk_corp, String pk_psndoc,String string) throws BusinessException;

	/**
	 * 得到某岗位的直接上级
	 * @author lianglj 2010-07-20
	 * @param om_job
	 * @return
	 */
	public HashSet<String> getDirectSuperiors(String om_job)throws BusinessException;
	
	
	/**
	 * 根据权限中的人员pk找到权限中直接上级人员
	 * @author lianglj 2010-07-20
	 * @param curserid
	 * @return 
	 * @throws BusinessException
	 */
	public  HashSet<String> getPsnOm(String psnbasdoc,String corp)throws BusinessException;
	
	/**
	 * 根据hr的id找到权限人员的pk
	 * @author lianglj 2010-07-20
     * @param pk_psnbasdoc hr人员id
     * @param curserid 权限人员id
	 * @return 
	 * @throws BusinessException
	 */
	public  String getuserid(String psnbasdoc,String corp)throws BusinessException;
	
	/**
	 * 根据权限的id找到对应业务员所属的公司
	 * @author lianglj 2010-07-20
     * @param userid 权限人员id
     * @param curserid 权限人员id
	 * @return 
	 * @throws BusinessException
	 */
	public  String getBelongCorp(String userid)throws BusinessException;
	/**
	 * 根据权限人员的pk找到hr的id
	 * @author lianglj 2010-07-20
     * @param pk_psnbasdoc hr人员id
     * @param curserid 权限人员id
	 * @return 
	 * @throws BusinessException
	 */
	public String getpsnbasdoc(String userid,String corp) throws BusinessException;
	
	/**
	 * 根据公司的主键找到公司的名称
	 * @author lianglj 2010-08-26
     * @param pk_corp 公司pk
	 * @return 
	 * @throws BusinessException
	 */
	public  String getCorpNameByPK(String pk_corp)throws BusinessException;
	
	
	
	/**
	 * 查找人员是否是某部门下的人员
	 * @author lianglj 2010-08-26
     * @param pk_psnbasdoc hr人员id
     * @param curserid 权限人员id
	 * @return 
	 * @throws BusinessException
	 */
	public  Boolean isDeptPsndoc(String psnbasdoc,String dept)throws BusinessException;
	
	/**
	 * 查找公司的上级公司
	 * @author lianglj 2010-08-28
     * @param corp 公司id
	 * @return 
	 * @throws BusinessException
	 */
	public  String getCorpSuperiors(String corp)throws BusinessException;
	/**
	 * 查找人员是否是某公司下的人员
	 * @author lianglj 2010-08-26
     * @param pk_psnbasdoc hr人员id
     * @param corp 
	 * @return 
	 * @throws BusinessException
	 */
	public  Boolean isCorpPsndoc(String psnbasdoc,String corp)throws BusinessException;
	
	/**
	 * 根据权限的id找到对应业务员所属的兼职公司
	 * @author lianglj 2010-07-20
     * @param userid 权限人员id
     * @param curserid 权限人员id
	 * @return 
	 * @throws BusinessException
	 */
	public  Vector getDeptPartCorp(String userid)throws BusinessException;

}