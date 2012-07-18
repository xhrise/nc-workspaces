package nc.vo.so.so001;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import nc.itf.scm.pub.ICoopwithForVO;
import nc.ui.pub.bill.BillStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IGetBusiDataForFlow;
import nc.vo.pub.pf.IPfBackCheck2;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.field.pu.FieldDBValidate;
import nc.vo.scm.pub.IscmDefCheckVO;
import nc.vo.scm.pub.bill.IExamAVO;
import nc.vo.scm.pub.redun.ISourceRedunVO;
import nc.vo.sp.service.PriceAskResultVO;
import nc.vo.sp.service.SalePriceVO;

/**
 * 此处插入类型说明。
 * 
 * 创建日期：(2001-4-20 16:07:20)
 * 
 * @author：宋杰
 */
public class SaleOrderVO extends AggregatedValueObject implements IExamAVO, IGetBusiDataForFlow,
		IPfBackCheck2, IscmDefCheckVO, ICoopwithForVO {

	private SaleorderHVO m_headVO = null;

	private SaleorderBVO[] m_bodyVOs = null;

	private Boolean m_bModPriceTag = null;// yb add 8.20 净价修改通过标志

	private int m_status = nc.vo.pub.VOStatus.UNCHANGED;

	private boolean m_firsttime = false;

	// // 是否强行删除日计划
	// private boolean isDel7D = false;

	// 当前操作人
	private String curuserid = null;

	// curdate
	private UFDate dcurdate = null;

	// 公司名称 写后台日志用
	private String ccorpname = null;

	// 用户名称 写后台日志用
	private String cusername = null;

	private String cnodename = null;

	private UFDateTime logdatetime = null;

	private String logip = null;

	private String slockKey = null;

	private boolean bIsCoop = false;

	/**
	 * 是协同过来的订单
	 * 
	 * @param value
	 */
	public boolean getIsCoop() {
		return bIsCoop;
	}

	public void setIsCoop(boolean value) {
		bIsCoop = value;
	}

	/**
	 * @return 返回 logdatetime。
	 */
	public UFDateTime getLogdatetime() {
		return logdatetime;
	}

	/**
	 * @param logdatetime
	 *            要设置的 logdatetime。
	 */
	public void setLogdatetime(UFDateTime logdatetime) {
		this.logdatetime = logdatetime;
	}

	/**
	 * @return 返回 logip。
	 */
	public String getLogip() {
		return logip;
	}

	/**
	 * @param logip
	 *            要设置的 logip。
	 */
	public void setLogip() {
		try {
			this.logip = InetAddress.getLocalHost().getHostAddress();
		} catch (Throwable ee) {

		}
	}

	/**
	 * @return 返回 dcurdate。
	 */
	public UFDate getDcurdate() {
		return dcurdate;
	}

	/**
	 * @param dcurdate
	 *            要设置的 dcurdate。
	 */
	public void setDcurdate(UFDate dcurdate) {
		this.dcurdate = dcurdate;
	}

	/**
	 * SaleOrderVO 构造子注解。
	 */
	public SaleOrderVO() {
		super();
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:36:56)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
		return m_bodyVOs;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-4-11 14:48:58)
	 * 
	 * @return int
	 */
	public boolean getFirstTime() {

		return this.m_firsttime;
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return java.lang.Boolean
	 */
	public Boolean getModPriceTag() {
		return m_bModPriceTag;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject getParentVO() {
		return m_headVO;
	}

	/**
	 * 返回对象标识，用来唯一定位对象。
	 * 
	 * 创建日期：(2001-2-15 9:43:38)
	 * 
	 * @return nc.vo.pub.PrimaryKey
	 */
	public String getPrimaryKey() throws nc.vo.pub.BusinessException {
		return getParentVO().getPrimaryKey();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-4-11 14:48:58)
	 * 
	 * @return int
	 */
	public int getStatus() {

		return this.m_status;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:36:56)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public void setChildrenVO(nc.vo.pub.CircularlyAccessibleValueObject[] children) {
		m_bodyVOs = (SaleorderBVO[]) children;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-4-11 14:48:58)
	 * 
	 * @return int
	 */
	public boolean setFirstTime(boolean firsttime) {

		return m_firsttime = firsttime;
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @param newvalue
	 *            java.lang.Boolean
	 */
	public void setModPriceTag(Boolean newvalue) {
		m_bModPriceTag = newvalue;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public void setParentVO(nc.vo.pub.CircularlyAccessibleValueObject parent) {
		m_headVO = (SaleorderHVO) parent;
	}

	/**
	 * 返回对象标识，用来唯一定位对象。
	 * 
	 * 创建日期：(2001-2-15 9:43:38)
	 * 
	 * @return nc.vo.pub.PrimaryKey
	 */
	public void setPrimaryKey(String key) throws nc.vo.pub.BusinessException {
		getParentVO().setPrimaryKey(key);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-4-11 14:48:58)
	 * 
	 * @return int
	 */
	public void setStatus(int status) {
		this.m_status = status;
		if (status == nc.vo.pub.VOStatus.NEW && m_bodyVOs != null) {
			for (int i = 0; i < m_bodyVOs.length; i++) {
				m_bodyVOs[i].setStatus(status);
			}
		}
	}
	
    public void validateForBS() throws nc.vo.pub.ValidationException {
		
		/**====1.进行最大最小边界值判断======================================*/
		FieldDBValidate.validate(this) ;
		
		/**====2.表头数据校验===============================================*/
		getParentVO().validate();

		ArrayList<Integer> rowList = new ArrayList<Integer>();
		if (getChildrenVO() == null || getChildrenVO().length == 0)
			if (m_status == nc.vo.pub.VOStatus.NEW)
				throw new nc.vo.pub.ValidationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("40060301", "UPP40060301-000089")/*
																		 * @res
																		 * "单据体不能为空！"
																		 */);
		
		/**====3.表体校验===================================================*/
		for (int i = 0; i < getChildrenVO().length; i++)
			if (getChildrenVO()[i].getStatus() != nc.vo.pub.VOStatus.DELETED) {
				try {
					getChildrenVO()[i].validate();
				} catch (nc.vo.pub.ValidationException e) {
					rowList.add(new Integer(i));
					String err = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("40060301",
							"UPP40060301-000090", null, new String[] { (i + 1) + "" })
							+ e.getMessage();
					// String err = "第" + nc.vo.pub.CommonConstant.BEGIN_MARK
					// + (i + 1) + nc.vo.pub.CommonConstant.END_MARK + "行"
					// + e.getMessage();
					throw new nc.vo.pub.ValidationException(err);
				}
			}
	}


	/**
	 * 验证对象各属性之间的数据逻辑正确性。
	 * 
	 * 创建日期：(2001-2-15 11:47:35)
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败，抛出 ValidationException，对错误进行解释。
	 */
	public void validate() throws nc.vo.pub.ValidationException {
		validateForBS();
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO getOrdVOOfSaleCorp() {
		SaleorderBVO[] bvos = (SaleorderBVO[]) getChildrenVO();
		SaleorderHVO hvo = (SaleorderHVO) getParentVO();
		if (hvo == null || bvos == null)
			return null;
		java.util.ArrayList bvolist = new java.util.ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++)
			if (bvos[i].getCconsigncorpid() == null
					|| bvos[i].getCconsigncorpid().trim().length() <= 0
					|| bvos[i].getCconsigncorpid().equals(hvo.getPk_corp()))
				bvolist.add(bvos[i]);
		int count = bvolist.size();
		if (count < 0)
			return null;
		SaleOrderVO vo = new SaleOrderVO();
		vo.setParentVO(hvo);
		vo.setChildrenVO((SaleorderBVO[]) bvolist.toArray(new SaleorderBVO[count]));
		return vo;
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO[] getOrdVOsOfOtherCorp() {

		SaleorderBVO[] bvos = (SaleorderBVO[]) getChildrenVO();
		if (bvos == null)
			return null;
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getBdericttrans() == null) {
				bvos[i].setBdericttrans(new nc.vo.pub.lang.UFBoolean(false));
			}
		}
		// 按发货公司,是否直运分单
		SaleOrderVO[] ordvos = (SaleOrderVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs.getSplitVO(
				"nc.vo.so.so001.SaleOrderVO", "nc.vo.so.so001.SaleorderHVO",
				"nc.vo.so.so001.SaleorderBVO", this, null, new String[] { "cconsigncorpid",
						"bdericttrans" });
		if (ordvos == null)
			return null;

		SaleorderHVO hvo = (SaleorderHVO) getParentVO();
		java.util.ArrayList ordvolist = new java.util.ArrayList();

		for (int i = 0, loop = ordvos.length; i < loop; i++) {
			bvos = (SaleorderBVO[]) ordvos[i].getChildrenVO();
			if (bvos == null || bvos.length <= 0)
				continue;
			if (bvos[0].getCconsigncorpid() != null
					&& !bvos[0].getCconsigncorpid().equals(hvo.getPk_corp())) {
				ordvolist.add(ordvos[i]);
			}
		}
		int count = ordvolist.size();
		if (count <= 0)
			return null;

		return (SaleOrderVO[]) ordvolist.toArray(new SaleOrderVO[count]);
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO[] splitOrdVOByCorp() {
		// 按发货公司分单
		return (SaleOrderVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs.getSplitVO(
				"nc.vo.so.so001.SaleOrderVO", "nc.vo.so.so001.SaleorderHVO",
				"nc.vo.so.so001.SaleorderBVO", this, null, new String[] { "cconsigncorpid" });

	}

	// private String bizTypeid=null;
	// private String operatorid=null;
	// private String pk_corp=null;

	private nc.vo.so.so016.OrdBalanceVO allordbalvo = null;

	private SaleOrderVO allSaleOrderVO = null;// 订单修改时传整个修改完成的VO

	private boolean bCheckATP = true;// ATP检查标志

	// private boolean bCheckOverCredit = true;//信用占用检查标志
	//
	// private boolean bCheckOverPeriod = true;//超帐期检查标志

	// 信用检查需要的字段
	// private String actionCode=null;
	// private int actionInt=-1;
	private String billTypeCode = "30";

	private int billTypeInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_BILL_ORDER;

	private nc.vo.so.so016.OrdBalanceVO chgordbalvo = null;

	nc.vo.scm.pub.session.ClientLink clientLink = null;

	private String errMsg = "";

	private String hintMsg = "";

	private int iAction = nc.vo.so.so001.ISaleOrderAction.A_DEFAULT;// 将要对订单采取的动作

	private SaleOrderVO oldSaleOrderVO = null;// 订单修改时传修改前的VO

	// 为发货单使用
	private String actioncode;
	
	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 9:03:50) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getActionCode() {
		switch (iAction) {
		case ISaleOrderAction.A_ADD:
			return "PreKeep";
		case ISaleOrderAction.A_EDIT:
			return "PreModify";
		case ISaleOrderAction.A_MODIFY:
			return "OrderAlter";
		case ISaleOrderAction.A_SPECIALADD:
			return "SpecialSave";
		case ISaleOrderAction.A_BLANKOUT:
			return "SoBlankout";
		case ISaleOrderAction.A_UNAUDIT:
			return "SoUnApprove";
		case ISaleOrderAction.A_AUDIT:
			return "APPROVE";
		case ISaleOrderAction.A_FREEZE:
			return "OrderFreeze";
		case ISaleOrderAction.A_UNFREEZE:
			return "OrderUnFreeze";
		case ISaleOrderAction.A_CLOSE:
			return "OrderFinish";
		case ISaleOrderAction.A_SENDAUDIT:
			return "SAVE";

		}
		return actioncode;
	}

	public void setActionCode(String ac) {
		actioncode = ac;
	}
	
	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return int
	 */
	public int getActionInt() {
		switch (iAction) {
		case ISaleOrderAction.A_ADD:
			return nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_ADD;
		case ISaleOrderAction.A_EDIT:
			return nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_MODIFY;
		case ISaleOrderAction.A_MODIFY:
			return nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_MODIFY;
		case ISaleOrderAction.A_SPECIALADD:
			return nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_ADD;
		case ISaleOrderAction.A_BLANKOUT:
			return nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL;
		case ISaleOrderAction.A_UNAUDIT:
			return -1;
		case ISaleOrderAction.A_AUDIT:
			return -1;
		case ISaleOrderAction.A_FREEZE:
			return -1;
		case ISaleOrderAction.A_UNFREEZE:
			return -1;
		case ISaleOrderAction.A_CLOSE:
			return nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_CLOSE;
		case ISaleOrderAction.A_OPEN:
			return nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_UNCLOSE;

		}
		return -1;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-9-27 14:41:11) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.so.so016.OrdBalanceVO
	 */
	public nc.vo.so.so016.OrdBalanceVO getAllordbalvo() {
		return allordbalvo;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-2 9:23:09)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO getAllSaleOrderVO() {
		return allSaleOrderVO;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillTypeCode() {
		return billTypeCode;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return int
	 */
	public int getBillTypeInt() {
		return billTypeInt;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBizTypeid() {
		if (getHeadVO() == null)
			return null;
		return getHeadVO().getCbiztype();
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleorderBVO[] getBodysNoInludeOOSAndSupLine() {
		SaleorderBVO[] bvos = getBodyVOs();
		if (bvos == null)
			return null;
		java.util.ArrayList bvolist = new java.util.ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getBoosflag() != null && bvos[i].getBoosflag().booleanValue())
				continue;
			if (bvos[i].getBsupplyflag() != null && bvos[i].getBsupplyflag().booleanValue())
				continue;
			bvolist.add(bvos[i]);
		}
		int count = bvolist.size();
		if (count < 0)
			return null;
		return (SaleorderBVO[]) bvolist.toArray(new SaleorderBVO[count]);
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleorderBVO[] getBodysNoInludeOOSLine() {
		SaleorderBVO[] bvos = getBodyVOs();
		if (bvos == null)
			return null;
		java.util.ArrayList bvolist = new java.util.ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++)
			if (bvos[i].getBoosflag() == null || !bvos[i].getBoosflag().booleanValue())
				bvolist.add(bvos[i]);
		int count = bvolist.size();
		if (count < 0)
			return null;
		return (SaleorderBVO[]) bvolist.toArray(new SaleorderBVO[count]);
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public SaleorderBVO[] getBodyVOs() {
		return m_bodyVOs;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-9-27 14:41:11) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.so.so016.OrdBalanceVO
	 */
	public nc.vo.so.so016.OrdBalanceVO getChgordbalvo() {
		return chgordbalvo;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-27 11:21:27)
	 * 
	 * @return nc.vo.scm.pub.session.ClientLink
	 */
	public nc.vo.scm.pub.session.ClientLink getClientLink() {
		return clientLink;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-15 16:22:51) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getErrMsg() {
		return errMsg;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public SaleorderHVO getHeadVO() {
		return m_headVO;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-15 16:22:51) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getHintMsg() {
		return hintMsg;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-1 15:45:23)
	 * 
	 * @return int
	 */
	public int getIAction() {
		return iAction;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:58:14) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getModifiedVO() {
		return getAllSaleOrderVO();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-1 13:38:24)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO getOldSaleOrderVO() {
		return oldSaleOrderVO;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:59:35) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getOldVO() {
		return getOldSaleOrderVO();
	}

	private String operatorid = null;

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOperatorid() {
		if (operatorid == null) {
			operatorid = getCuruserid();
			if (operatorid == null) {
				operatorid = getHeadVO().getCoperatorid();
			}
		}
		return operatorid;
	}

	/**
	 * 设置操作员主键
	 * 
	 * @param optid
	 */
	public void setLockOperatorid(String optid) {
		if (operatorid == null) {
			operatorid = optid;
		}
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO getOrdVOFor20() {
		SaleorderBVO[] bvos = (SaleorderBVO[]) getChildrenVO();
		SaleorderHVO hvo = (SaleorderHVO) getParentVO();
		if (hvo == null || bvos == null)
			return null;
		java.util.ArrayList bvolist = new java.util.ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			// 是否劳务
			if (bvos[i].getLaborflag() != null && bvos[i].getLaborflag().booleanValue())
				continue;

			// 是否折扣
			if (bvos[i].getDiscountflag() != null && bvos[i].getDiscountflag().booleanValue())
				continue;

			bvolist.add(bvos[i]);
		}
		int count = bvolist.size();
		if (count < 0)
			return null;
		SaleOrderVO vo = new SaleOrderVO();
		vo.setParentVO(hvo);
		vo.setChildrenVO((SaleorderBVO[]) bvolist.toArray(new SaleorderBVO[count]));
		return vo;
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO getOrdVOOfOtherCorp() {

		SaleorderBVO[] bvos = getOtherCorpOrdBvos();
		if (bvos == null)
			return null;

		SaleOrderVO retvo = new SaleOrderVO();
		retvo.setParentVO(getParentVO());
		retvo.setChildrenVO(bvos);

		return retvo;
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleorderBVO[] getOtherCorpOrdBvos() {

		SaleorderBVO[] bvos = getBodyVOs();
		if (bvos == null)
			return null;
		java.util.ArrayList list = new java.util.ArrayList();
		String cconsigncorpid = null;
		String pk_corp = null;

		for (int i = 0, loop = bvos.length; i < loop; i++) {
			cconsigncorpid = bvos[i].getCconsigncorpid();
			pk_corp = bvos[i].getPkcorp();

			if (cconsigncorpid != null && cconsigncorpid.trim().length() > 0
					&& !cconsigncorpid.equals(pk_corp)) {
				list.add(bvos[i]);
			}
		}

		if (list.size() <= 0)
			return null;

		return (SaleorderBVO[]) list.toArray(new SaleorderBVO[list.size()]);

	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-7-14 17:17:38) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPfAssMoney() {
		return null;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-7-14 16:42:42) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public String getPfCurrency() {
		if (getBodyVOs() == null || getBodyVOs().length <= 0)
			return null;
		return getBodyVOs()[0].getCcurrencytypeid();
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-7-14 17:17:38) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPfLocalMoney() {
		return getTotalcursummny("nsummny");
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-7-14 17:17:38) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPfMoney() {
		return getTotalcursummny("noriginalcursummny");
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_corp() {
		if (getHeadVO() == null)
			return null;
		return getHeadVO().getPk_corp();
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleorderBVO[] getSelfCorpOrdBvos() {

		SaleorderBVO[] bvos = getBodyVOs();
		if (bvos == null)
			return null;
		java.util.ArrayList list = new java.util.ArrayList();
		String cconsigncorpid = null;
		String pk_corp = null;

		for (int i = 0, loop = bvos.length; i < loop; i++) {
			cconsigncorpid = bvos[i].getCconsigncorpid();
			pk_corp = bvos[i].getPkcorp();

			if (cconsigncorpid == null || cconsigncorpid.equals(pk_corp)
					|| cconsigncorpid.trim().length() <= 0) {
				list.add(bvos[i]);
			}
		}

		if (list.size() <= 0)
			return null;

		return (SaleorderBVO[]) list.toArray(new SaleorderBVO[list.size()]);

	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO getSelfOrdVO() {

		SaleorderHVO hvo = getHeadVO();
		SaleorderBVO[] bvos = getBodyVOs();
		if (hvo == null || bvos == null || bvos.length <= 0)
			return null;

		java.util.ArrayList bvoslist = new java.util.ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getCconsigncorpid() == null
					|| bvos[i].getCconsigncorpid().equals(hvo.getPk_corp())) {
				bvoslist.add(bvos[i]);
			}
		}
		if (bvoslist.size() > 0) {
			SaleOrderVO retordvo = new SaleOrderVO();
			retordvo.setParentVO(hvo);
			retordvo.setChildrenVO((SaleorderBVO[]) bvoslist.toArray(new SaleorderBVO[bvoslist
					.size()]));
			return retordvo;
		}

		return null;

	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-7-14 17:08:47) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param key
	 *            java.lang.String
	 */
	public nc.vo.pub.lang.UFDouble getTotalcursummny(String key) {
		if (getBodyVOs() == null || getBodyVOs().length <= 0 || key == null)
			return null;
		// 缺货
		nc.vo.pub.lang.UFBoolean boosflag = null;
		// 赠品
		nc.vo.pub.lang.UFBoolean blargessflag = null;
		nc.vo.pub.lang.UFDouble tempmny = null;
		nc.vo.pub.lang.UFDouble mny = null;
		SaleorderBVO[] ordbvos = getBodyVOs();

		int itype = -1;

		if ("noriginalcursummny".equals(key))
			itype = 0;
		else if ("nassistcursummny".equals(key))
			itype = 1;
		else if ("nsummny".equals(key))
			itype = 2;

		if (itype < 0)
			return null;

		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			boosflag = ordbvos[i].getBoosflag();
			if (boosflag != null && boosflag.booleanValue())
				continue;
			blargessflag = ordbvos[i].getBlargessflag();
			if (blargessflag != null && blargessflag.booleanValue())
				continue;
			if (itype == 0) {
				tempmny = ordbvos[i].getNoriginalcursummny();
			} else if (itype == 2) {
				tempmny = ordbvos[i].getNsummny();
			}

			if (tempmny == null)
				continue;
			if (mny == null)
				mny = tempmny;
			else
				mny = mny.add(tempmny);

		}
		return mny;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-15 16:22:51) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 */
	public boolean isBCheckATP() {
		return bCheckATP;
	}

	/**
	 * 检查订单是否多公司订单。 创建日期：(2004-4-22 13:03:12)
	 * 
	 * @return boolean
	 */
	public boolean isMultCorpOrd() {
		if (getHeadVO() == null)
			return false;
		SaleorderBVO[] bvos = getBodyVOs();
		if (bvos == null || bvos.length <= 0)
			return false;
		String pkcorp = getHeadVO().getPk_corp();
		if (pkcorp == null)
			return false;
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getCconsigncorpid() == null
					|| bvos[i].getCconsigncorpid().trim().length() <= 0)
				continue;
			if (!pkcorp.equals(bvos[i].getCconsigncorpid())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-9-27 14:41:11) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newAllordbalvo
	 *            nc.vo.so.so016.OrdBalanceVO
	 */
	public void setAllordbalvo(nc.vo.so.so016.OrdBalanceVO newAllordbalvo) {
		allordbalvo = newAllordbalvo;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-2 9:23:09)
	 * 
	 * @param newAllSaleOrderVO
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	public void setAllSaleOrderVO(SaleOrderVO newAllSaleOrderVO) {
		allSaleOrderVO = newAllSaleOrderVO;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-15 16:22:51) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newBCheckATP
	 *            boolean
	 */
	public void setBCheckATP(boolean newBCheckATP) {
		bCheckATP = newBCheckATP;
		SaleorderBVO[] bvos = getBodyVOs();
		if (bvos != null) {
			for (int i = 0, loop = bvos.length; i < loop; i++) {
				if (bvos[i] != null)
					bvos[i].setBCheckATP(newBCheckATP);
			}
		}
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newBillTypeCode
	 *            java.lang.String
	 */
	public void setBillTypeCode(java.lang.String newBillTypeCode) {
		billTypeCode = newBillTypeCode;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newBillTypeInt
	 *            int
	 */
	public void setBillTypeInt(int newBillTypeInt) {
		billTypeInt = newBillTypeInt;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-9-27 14:41:11) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newChgordbalvo
	 *            nc.vo.so.so016.OrdBalanceVO
	 */
	public void setChgordbalvo(nc.vo.so.so016.OrdBalanceVO newChgordbalvo) {
		chgordbalvo = newChgordbalvo;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-27 11:21:27)
	 * 
	 * @param newClientLink
	 *            nc.vo.scm.pub.session.ClientLink
	 */
	public void setClientLink(nc.vo.scm.pub.session.ClientLink newClientLink) {
		clientLink = newClientLink;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-15 16:22:51) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newErrMsg
	 *            java.lang.String
	 */
	public void setErrMsg(java.lang.String newErrMsg) {
		if (errMsg == null || errMsg.trim().length() <= 0)
			errMsg = newErrMsg;
		else
			errMsg += newErrMsg;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-15 16:22:51) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newHintMsg
	 *            java.lang.String
	 */
	public void setHintMsg(java.lang.String newHintMsg) {
		if (hintMsg == null || hintMsg.trim().length() <= 0)
			hintMsg = newHintMsg;
		else
			hintMsg += newHintMsg;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-1 15:45:23)
	 * 
	 * @param newIAction
	 *            int
	 */
	public void setIAction(int newIAction) {
		iAction = newIAction;
		SaleorderBVO[] bvos = getBodyVOs();
		if (bvos != null) {
			for (int i = 0, loop = bvos.length; i < loop; i++) {
				if (bvos[i] != null)
					bvos[i].setIAction(newIAction);
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-1 13:38:24)
	 * 
	 * @param newOldSaleOrderVO
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	public void setOldSaleOrderVO(SaleOrderVO newOldSaleOrderVO) {
		oldSaleOrderVO = newOldSaleOrderVO;
	}

	/**
	 * 
	 * 创建日期：(2004-3-28 14:50:54) 作者：马万钧 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO[] getOrdVOsOfOtherCorpFor5D() {

		SaleorderBVO[] bvos = (SaleorderBVO[]) getChildrenVO();
		if (bvos == null)
			return null;
		for (int i = 0, loop = bvos.length; i < loop; i++) {

			if (bvos[i].getCconsigncorpid() != null
					&& bvos[i].getCconsigncorpid().trim().length() <= 0)
				bvos[i].setCconsigncorpid(null);

			if (bvos[i].getBdericttrans() == null) {
				bvos[i].setBdericttrans(new nc.vo.pub.lang.UFBoolean(false));
			}

			if (bvos[i].getCadvisecalbodyid() != null
					&& bvos[i].getCadvisecalbodyid().trim().length() <= 0)
				bvos[i].setCadvisecalbodyid(null);
			if (bvos[i].getCreccalbodyid() != null
					&& bvos[i].getCreccalbodyid().trim().length() <= 0)
				bvos[i].setCreccalbodyid(null);
		}
		// 按发货公司,发货库存组织,收货库存组织,是否直运分单
		SaleOrderVO[] ordvos = (SaleOrderVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs.getSplitVO(
				"nc.vo.so.so001.SaleOrderVO", "nc.vo.so.so001.SaleorderHVO",
				"nc.vo.so.so001.SaleorderBVO", this, null, new String[] { "cconsigncorpid",
						"bdericttrans", "cadvisecalbodyid", "creccalbodyid" });
		if (ordvos == null)
			return null;

		SaleorderHVO hvo = (SaleorderHVO) getParentVO();
		java.util.ArrayList ordvolist = new java.util.ArrayList();

		for (int i = 0, loop = ordvos.length; i < loop; i++) {
			bvos = (SaleorderBVO[]) ordvos[i].getChildrenVO();
			if (bvos == null || bvos.length <= 0)
				continue;
			if (bvos[0].getCconsigncorpid() != null
					&& !bvos[0].getCconsigncorpid().equals(hvo.getPk_corp())) {
				ordvolist.add(ordvos[i]);
			}
		}
		int count = ordvolist.size();
		if (count <= 0)
			return null;

		return (SaleOrderVO[]) ordvolist.toArray(new SaleOrderVO[count]);
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-5-21 8:56:54) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newBillTypeCode
	 *            java.lang.String
	 */
	public void setOldVOToHeadBody(SaleOrderVO oldvo) {
		if (oldvo == null || this.getHeadVO() == null)
			return;
		this.getHeadVO().setOldhvo(oldvo.getHeadVO());
		SaleorderBVO[] bvos = this.getBodyVOs();
		if (bvos == null)
			return;
		SaleorderBVO[] oldbvos = oldvo.getBodyVOs();
		if (oldbvos == null || oldbvos.length <= 0)
			return;
		java.util.HashMap hskey = new java.util.HashMap(oldbvos.length * 3 / 2);
		for (int i = 0, loop = oldbvos.length; i < loop; i++) {
			if (oldbvos[i].getCorder_bid() != null)
				hskey.put(oldbvos[i].getCorder_bid(), oldbvos[i]);
		}
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getCorder_bid() != null) {
				bvos[i].setOldbvo((SaleorderBVO) hskey.get(bvos[i].getCorder_bid()));
			}
		}
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-1-12 18:53:46) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCuruserid() {
		return curuserid;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-1-12 18:53:46) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param newCuruserid
	 *            java.lang.String
	 */
	public void setCuruserid(java.lang.String newCuruserid) {
		curuserid = newCuruserid;
		setLogip();
	}

	/**
	 * @return 返回 ccorpname。
	 */
	public String getCcorpname() {
		return ccorpname;
	}

	/**
	 * @param ccorpname
	 *            要设置的 ccorpname。
	 */
	public void setCcorpname(String ccorpname) {
		this.ccorpname = ccorpname;
	}

	/**
	 * @return 返回 cusername。
	 */
	public String getCusername() {
		return cusername;
	}

	/**
	 * @param cusername
	 *            要设置的 cusername。
	 */
	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	/**
	 * @return 返回 cnodename。
	 */
	public String getCnodename() {
		return cnodename;
	}

	/**
	 * @param cnodename
	 *            要设置的 cnodename。
	 */
	public void setCnodename(String cnodename) {
		this.cnodename = cnodename;
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-9-27 14:41:11) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.so.so016.OrdBalanceVO
	 */
	public void processVOForTrans() {
		java.util.HashMap hsvalues = new java.util.HashMap();
		filterVO(this, hsvalues);
		filterVO(getOldSaleOrderVO(), hsvalues);
		filterVO(getAllSaleOrderVO(), hsvalues);
	}

	public static void filterVO(SaleOrderVO vo, java.util.HashMap hsvalue) {

		if (vo == null || hsvalue == null)
			return;
		SaleorderHVO headvo = vo.getHeadVO();
		SaleorderBVO[] bodyvos = vo.getBodyVOs();

		String skey = null;
		Object value = null, okey = null;
		String[] keys = null;
		if (headvo != null) {
			keys = headvo.getAttributeNames();
			if (keys != null) {
				for (int i = 0, loop = keys.length; i < loop; i++) {
					okey = headvo.getAttributeValue(keys[i]);
					if (okey == null)
						continue;
					if (okey.getClass() == String.class) {
						if (okey.toString().length() <= 0) {
							headvo.setAttributeValue(keys[i], null);
							continue;
						}
						skey = "S" + okey.toString();
					} else if (okey.getClass() == nc.vo.pub.lang.UFDouble.class) {
						skey = "UF" + okey.toString();
					} else if (okey.getClass() == nc.vo.pub.lang.UFDate.class) {
						skey = "UD" + okey.toString();
					} else if (okey.getClass() == nc.vo.pub.lang.UFDateTime.class) {
						skey = "UT" + okey.toString();
					} else if (okey.getClass() == nc.vo.pub.lang.UFBoolean.class) {
						skey = "UB" + okey.toString();
					} else if (okey.getClass() == nc.vo.pub.lang.UFTime.class) {
						skey = "UTM" + okey.toString();
					} else if (okey.getClass() == Integer.class) {
						skey = "I" + okey.toString();
					} else if (okey.getClass() == Double.class) {
						skey = "D" + okey.toString();
					} else if (okey.getClass() == Long.class) {
						skey = "L" + okey.toString();
					} else {
						skey = okey.getClass() + okey.toString();
					}
					// skey = okey.getClass()+okey.toString();
					value = hsvalue.get(skey);
					if (value != null) {
						headvo.setAttributeValue(keys[i], value);
					} else {
						hsvalue.put(skey, okey);
					}
				}
			}
		}

		if (bodyvos != null && bodyvos.length > 0) {
			keys = bodyvos[0].getAttributeNames();
			if (keys != null) {
				for (int i = 0, loop = bodyvos.length; i < loop; i++) {
					// //清除不用传输的数据
					// bodyvos[i].clearNoTransField();
					for (int j = 0, loopj = keys.length; j < loopj; j++) {
						okey = bodyvos[i].getAttributeValue(keys[j]);
						if (okey == null)
							continue;
						if (okey.getClass() == String.class) {
							if (okey.toString().length() <= 0) {
								bodyvos[i].setAttributeValue(keys[j], null);
								continue;
							}
							skey = "S" + okey.toString();
						} else if (okey.getClass() == nc.vo.pub.lang.UFDouble.class) {
							skey = "UF" + okey.toString();
						} else if (okey.getClass() == nc.vo.pub.lang.UFDate.class) {
							skey = "UD" + okey.toString();
						} else if (okey.getClass() == nc.vo.pub.lang.UFDateTime.class) {
							skey = "UT" + okey.toString();
						} else if (okey.getClass() == nc.vo.pub.lang.UFBoolean.class) {
							skey = "UB" + okey.toString();
						} else if (okey.getClass() == nc.vo.pub.lang.UFTime.class) {
							skey = "UTM" + okey.toString();
						} else if (okey.getClass() == Integer.class) {
							skey = "I" + okey.toString();
						} else if (okey.getClass() == Double.class) {
							skey = "D" + okey.toString();
						} else if (okey.getClass() == Long.class) {
							skey = "L" + okey.toString();
						} else {
							skey = okey.getClass() + okey.toString();
						}
						// skey = okey.getClass()+okey.toString();
						value = hsvalue.get(skey);
						if (value != null) {
							bodyvos[i].setAttributeValue(keys[j], value);
						} else {
							hsvalue.put(skey, okey);
						}
					}
				}
			}
		}

		return;
	}

	public String getLockKey() {
		return slockKey;
	}

	public void setLockKey(String slock) {
		slockKey = slock;
	}

	/**
	 * 返回登陆日期
	 * 
	 * @return
	 */
	public UFDate getLoginDate() {
		// return getClientLink().getLogonDate();
		if (getClientLink() == null && getDcurdate() == null)
			return getHeadVO().getDmakedate();
		else if (getClientLink() == null)
			return getDcurdate();
		else
			return getClientLink().getLogonDate();
	}

	// 是否需要账期检查
	boolean m_bCheckPeriod = true;

	// 是否需要信用检查
	boolean m_bCheckCredit = true;

	/**
	 * 返回是否需要账期检查
	 * 
	 * @return
	 */
	public boolean isCheckPeriod() {
		return m_bCheckPeriod;
	}

	/**
	 * 返回是否需要信用检查
	 * 
	 * @return
	 */
	public boolean isCheckCredit() {
		return m_bCheckCredit;
	}

	/**
	 * 返回是否需要账期检查
	 * 
	 * @return
	 */
	public void setCheckPeriod(boolean b) {
		m_bCheckPeriod = b;
	}

	/**
	 * 返回是否需要信用检查
	 * 
	 * @return
	 */
	public void setCheckCredit(boolean b) {
		m_bCheckCredit = b;
	}

	public static String getBillState(Object o) {
		if (o == null)
			return null;

		int state = (new Integer(o.toString())).intValue();

		switch (state) {
		case BillStatus.FREE: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000340")/* @res "自由" */;
		}
		case BillStatus.AUDIT: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000027")/*
																									 * @res
																									 * "审批"
																									 */;
		}
		case BillStatus.FREEZE: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000030")/*
																									 * @res
																									 * "冻结"
																									 */;
		}
		case BillStatus.CLOSE: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000119")/* @res "关闭" */;
		}
		case BillStatus.BLANKOUT: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000005")/*
																									 * @res
																									 * "作废"
																									 */;
		}
		case BillStatus.FINISH: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000128")/* @res "结束" */;
		}
		case BillStatus.AUDITING: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000320")/* @res "正在审批中" */;
		}
		case BillStatus.NOPASS: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000242")/* @res "审批未通过" */;
		}
		}

		return null;
	}

	public Object[] getBodyDefValues(int iserial) {
		if (m_bodyVOs == null)
			return null;
		Object[] o = new Object[m_bodyVOs.length];
		for (int i = 0; i < m_bodyVOs.length; i++) {
			o[i] = m_bodyVOs[i].getAttributeValue("vdef" + iserial);
		}
		return o;
	}

	public String getCbilltypecode() {
		return nc.vo.scm.constant.ScmConst.SO_Order;
	}

	public Object getHeadDefValue(int iserial) {
		if (m_headVO == null)
			return null;
		else
			return m_headVO.getAttributeValue("vdef" + iserial);
	}

	public String getCbilltypedef() {
		return ScmConst.SO_Order;
	}

	/**
	 * 重新计算表头价税合计
	 * 
	 */
	public void calNHeadSummny() {
		if (m_headVO == null)
			return;

		if (m_bodyVOs == null || m_bodyVOs.length == 0)
			return;

		UFDouble sum = new UFDouble(0);
		for (SaleorderBVO body : m_bodyVOs) {
			sum = sum.add(body.getNoriginalcursummny());
		}

		// m_headVO.m_nheadsummny = sum;
		m_headVO.setNheadsummny(sum);
	}

	/**
	 * 重算表头价税合计，去掉缺货行的影响
	 * 
	 * 解决以前数据对缺货行处理错误的问题
	 * 
	 * 保存调用
	 * 
	 */
	public void reCalHeadSummny() {

		if (m_headVO == null)
			return;

		if (m_bodyVOs == null || m_bodyVOs.length == 0)
			return;

		boolean errflag = false;
		// 首先根据表体去校验表头
		/** 必须保证表体的价税合计正确* */
		try {
			checkSummny();
		} catch (Exception e) {
			errflag = true;
		}

		if (!errflag)
			return;

		for (SaleorderBVO body : m_bodyVOs) {
			if (body.getBoosflag() != null && body.getBoosflag().booleanValue()) {
				m_headVO.setNheadsummny(m_headVO.getNheadsummny().sub(body.getNoriginalcurmny()));
			}
		}

	}

	/**
	 * 校验表头和表体的价税合计
	 * 
	 * @throws ValidationException
	 */
	public void checkSummny() throws ValidationException {		
		UFDouble sum = new UFDouble(0);
		for (SaleorderBVO body : getBodyVOs()) {
			// 此处逻辑与编辑时的控制一样
			// 非赠品、非缺货
			if ((body.getBlargessflag() == null || !body.getBlargessflag().booleanValue())
					&& (body.getBoosflag() == null || !body.getBoosflag().booleanValue())) {
				sum = sum.add(body.getNoriginalcursummny() == null ? new UFDouble(0) : body
						.getNoriginalcursummny());
			}
		}

		// 当表体数据合法时，表头价税合计一定不是空值，此处不做校验
		if (getHeadVO().getNheadsummny() != null && sum.compareTo(getHeadVO().getNheadsummny()) != 0)
			throw new ValidationException("表头价税合计与表体总和不等");
	}

	/**
	 * 满足补货直运要求的数据
	 * 
	 * 补货直运下游增行过滤使用
	 * 
	 * @param sourceRedunVO
	 * @return 只要有一行表体满足要求就返回数据 否则为null
	 * 
	 * @see OrderToBillRunSourceCtrl.check(RedunVO rvo)
	 */
	public SaleOrderVO getBillRedunVO(ISourceRedunVO sourceRedunVO) {
		if (m_headVO == null || m_bodyVOs == null || m_bodyVOs.length == 0)
			return null;

		ArrayList<SaleorderBVO> al_item = new ArrayList();
		for (int i = 0, len = m_bodyVOs.length; i < len; i++) {
			// 构建表头和表体关系
			m_bodyVOs[i].setMyHead(m_headVO);

			if (m_bodyVOs[i].checkItem4BillRedun(sourceRedunVO))
				al_item.add(m_bodyVOs[i]);
		}

		if (al_item.size() == 0)
			return null;
		else {
			SaleOrderVO vo = new SaleOrderVO();
			vo.setParentVO(m_headVO);
			vo.setChildrenVO(al_item.toArray(new SaleorderBVO[0]));
			return vo;
		}

	}

	public String getBIDItemkey() {
		// TODO 自动生成方法存根
		return "corder_bid";
	}

	public String getBilltypecode() {
		// TODO 自动生成方法存根
		return "30";
	}

	public String getBusitypePK() {
		// TODO 自动生成方法存根
		return getHeadVO().getCbiztype();
	}

	public int getCoopwithType() {
		// TODO 自动生成方法存根
		return 1;
	}

	public String getCustomerID() {
		// TODO 自动生成方法存根
		return getHeadVO().getPk_corp();
	}

	public String getOrgid() {
		// TODO 自动生成方法存根
		return getHeadVO().getCsalecorpid();
	}

	public String getSourBIDItemkey() {
		// TODO 自动生成方法存根
		return "csourcebillbodyid";
	}

	public String getCooppohid() {
		if (this.getParentVO() == null
				|| this.getParentVO().getAttributeValue("ccooppohid") == null
				|| "".equals((String) this.getParentVO().getAttributeValue("ccooppohid"))) {
			return null;

		}
		return (String) this.getParentVO().getAttributeValue("ccooppohid");
	}
	public boolean isCoopped(){
		if (this.getParentVO() == null)return false;
		return ((SaleorderHVO)this.getParentVO()).isCoopped();
		
	}
	
	public SalePriceVO[] getPriceParam(){
		if(m_headVO==null || m_bodyVOs==null||m_bodyVOs.length==0)
			return null;
		
		SalePriceVO[] spvos = new SalePriceVO[m_bodyVOs.length];
		for(int i=0,len=m_bodyVOs.length;i<len;i++){
			spvos[i]=getPriceParam(m_headVO,m_bodyVOs[i]);
		}
		
		return spvos;
	}
	
	private SalePriceVO getPriceParam(SaleorderHVO hvo, SaleorderBVO bvo) {

		SalePriceVO salepriceVO = new SalePriceVO();

		// 公司ID(当前公司)
		salepriceVO.setCropID(hvo.getPk_corp());

		// 业务类型
		salepriceVO.setBizTypeID(hvo.getCbiztype());

		// 客户ID
		salepriceVO.setCustomerID(hvo.getCcustomerid());

		// 客户分组
		salepriceVO.setCustomerClass(bvo.getCchantypeid());

		salepriceVO.setBuseBaseStrict(Boolean.FALSE);

		// 部门ID
		salepriceVO.setDeptid(hvo.getCdeptid());

		// 销售组织
		salepriceVO.setSaleStrucid(hvo.getCsalecorpid());

		// 系统日期
		salepriceVO.setSystemData(hvo.getDbilldate());

		// 存货ID
		salepriceVO.setInventoryID(bvo.getCinventoryid());

		// 存货基础ID
		salepriceVO.setInventoryBaseID(bvo.getCinvbasdocid());

		// 币种ID
		salepriceVO.setCurrencyID(bvo.getCcurrencytypeid());

		// 数量
		salepriceVO.setNumber(bvo.getNquoteunitnum());

		// 自由项1
		salepriceVO.setFree1(bvo.getVfree1());

		// 自由项2
		salepriceVO.setFree2(bvo.getVfree2());

		// 自由项3
		salepriceVO.setFree3(bvo.getVfree3());

		// 自由项4
		salepriceVO.setFree4(bvo.getVfree4());

		// 自由项5
		salepriceVO.setFree5(bvo.getVfree5());

		// 询价计量单位
		salepriceVO.setMeasdocid(bvo.getCquoteunitid());

		// 价格项
		salepriceVO.setPriceTypeid(bvo.getCpriceitemid());

		// 价目表
		salepriceVO.setPricetariffid(bvo.getCpriceitemtable());

		// 价格策略
		salepriceVO.setPricePolicyid(bvo.getCpricepolicyid());

		// 收货地区
		salepriceVO.setReceiptAreaid(bvo.getCreceiptareaid());

		return salepriceVO;
	}
	
	/**
	 * 获得合并后的询价参数
	 * 
	 * @return
	 */
	public Object[] getUnitePriceParam() {
		SalePriceVO[] spvos = getPriceParam();

		if (spvos == null || spvos.length == 0)
			return null;

		HashMap<String, SalePriceVO> hm_unite = new HashMap();
		HashMap<String, ArrayList<Integer>> hm_row = new HashMap();
		String key;

		for (int i = 0, len = spvos.length; i < len; i++) {
			key = getSalePriceVOKey(spvos[i]);
			if (hm_unite.get(key) == null)
				hm_unite.put(key, spvos[i]);
			else
				hm_unite.get(key).setNumber(hm_unite.get(key).getNumber().add(spvos[i].getNumber()));

			if (hm_row.get(key) == null)
				hm_row.put(key, new ArrayList());

			hm_row.get(key).add(i);
		}

		return new Object[] { hm_unite.values().toArray(new SalePriceVO[0]), hm_row };
	}
	
	private String getSalePriceVOKey(SalePriceVO vo) {
		if (vo == null)
			return null;

		// { "存货管理档案", 询价计量单位,价格项,价目表,价格策略,收货地区,"free1", "free2", "free3",
		// "free4", "free5" };

		String key = "";

		key += vo.getInventoryID();

		// 询价计量单位
		key += vo.getMeasdocid();
		// 价格项
		key += vo.getPriceTypeid();
		// 价目表
		key += vo.getPricetariffid();
		// 价格策略
		key += vo.getPricePolicyid();
		// 收货地区
		key += vo.getReceiptAreaid();

		key += vo.getFree1();
		key += vo.getFree2();
		key += vo.getFree3();
		key += vo.getFree4();
		key += vo.getFree5();

		// 客户分组
		key += vo.getCustomerClass();

		return key;
	}
	
	public void updatePirceValue(SalePriceVO[] pricevos, PriceAskResultVO[] resultvos,
			HashMap<String, ArrayList<Integer>> hm_row, UFBoolean SA02) {
		int errflag = 1;
		int findrow = -1;
		boolean sa02 = SA02.booleanValue();
		String key;
		ArrayList rowlist;

		for (int i = 0, loop = resultvos.length; i < loop; i++) {
			key = getSalePriceVOKey(pricevos[i]);
			rowlist = (ArrayList) hm_row.get(key);
			if (rowlist == null)
				continue;

			errflag = resultvos[i].getErrFlag() == null ? 0 : resultvos[i].getErrFlag().intValue();
			for (int k = 0, loopk = rowlist.size(); k < loopk; k++) {
				findrow = ((Integer) rowlist.get(k)).intValue();
				if (errflag != 0) {
					for (int m = 0, loopm = pricekeys.length; m < loopm; m++) {
						m_bodyVOs[findrow].setAttributeValue(pricekeys[m], null);
					}
				} else {
					if (sa02) {
						// 报价含税单价
						m_bodyVOs[findrow].setNorgqttaxprc(resultvos[i].getNum());
						// 原始询价含税单价
						m_bodyVOs[findrow].setNqtorgtaxprc(resultvos[i].getNum());
					} else {
						// 报价无税单价
						m_bodyVOs[findrow].setNorgqtprc(resultvos[i].getNum());
						// 原始询价无税单价
						m_bodyVOs[findrow].setNqtorgprc(resultvos[i].getNum());
					}
					m_bodyVOs[findrow].setNitemdiscountrate(resultvos[i].getDiscount());
				}
			}
		}
	}
	
	/**
	 * 寻价失败清除相关项
	 * 
	 */
	public void resumeValueWhenFindPriceFailed(){
		for (int i = 0, loop = m_bodyVOs.length; i < loop; i++) {
			for (int k = 0, loopk = pricekeys.length; k < loopk; k++) {
				m_bodyVOs[i].setAttributeValue(pricekeys[k], null);
			}
		}
	}
	
	private static String[] pricekeys = { "norgviaprice", "norgviapricetax",
		"noriginalcurprice", "noriginalcurtaxprice",
		"noriginalcurnetprice", "noriginalcurtaxnetprice",
		"noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny",
		"noriginalcurdiscountmny",

		"nprice", "ntaxprice", "nnetprice", "ntaxnetprice", "ntaxmny",
		"nmny", "nsummny", "ndiscountmny",

		"norgqttaxprc", //报价单位含税单价
		"norgqtprc", //报价单位无税单价
		"norgqttaxnetprc", //报价单位含税净价
		"norgqtnetprc", //报价单位无税净价

		"nqttaxnetprc", //报价单位本币含税净价
		"nqtnetprc", //报价单位本币无税净价
		"nqttaxprc", //报价单位本币含税单价
		"nqtprc", //报价单位本币无税单价

		"cpricepolicyid", "cpricepolicy", "cpriceitemid", "cpriceitem",
		"cpriceitemtable", "cpriceitemtablename", "cpricecalproc" };
	public boolean hasRowClosed(){
		for(SaleorderBVO bodyVO:m_bodyVOs){
			if(bodyVO.isClosed()){
				return true;
			}
		}
		return false;
	}
}
