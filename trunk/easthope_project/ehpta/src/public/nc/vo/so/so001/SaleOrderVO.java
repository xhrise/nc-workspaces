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
 * �˴���������˵����
 * 
 * �������ڣ�(2001-4-20 16:07:20)
 * 
 * @author���ν�
 */
public class SaleOrderVO extends AggregatedValueObject implements IExamAVO, IGetBusiDataForFlow,
		IPfBackCheck2, IscmDefCheckVO, ICoopwithForVO {

	private SaleorderHVO m_headVO = null;

	private SaleorderBVO[] m_bodyVOs = null;

	private Boolean m_bModPriceTag = null;// yb add 8.20 �����޸�ͨ����־

	private int m_status = nc.vo.pub.VOStatus.UNCHANGED;

	private boolean m_firsttime = false;

	// // �Ƿ�ǿ��ɾ���ռƻ�
	// private boolean isDel7D = false;

	// ��ǰ������
	private String curuserid = null;

	// curdate
	private UFDate dcurdate = null;

	// ��˾���� д��̨��־��
	private String ccorpname = null;

	// �û����� д��̨��־��
	private String cusername = null;

	private String cnodename = null;

	private UFDateTime logdatetime = null;

	private String logip = null;

	private String slockKey = null;

	private boolean bIsCoop = false;

	/**
	 * ��Эͬ�����Ķ���
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
	 * @return ���� logdatetime��
	 */
	public UFDateTime getLogdatetime() {
		return logdatetime;
	}

	/**
	 * @param logdatetime
	 *            Ҫ���õ� logdatetime��
	 */
	public void setLogdatetime(UFDateTime logdatetime) {
		this.logdatetime = logdatetime;
	}

	/**
	 * @return ���� logip��
	 */
	public String getLogip() {
		return logip;
	}

	/**
	 * @param logip
	 *            Ҫ���õ� logip��
	 */
	public void setLogip() {
		try {
			this.logip = InetAddress.getLocalHost().getHostAddress();
		} catch (Throwable ee) {

		}
	}

	/**
	 * @return ���� dcurdate��
	 */
	public UFDate getDcurdate() {
		return dcurdate;
	}

	/**
	 * @param dcurdate
	 *            Ҫ���õ� dcurdate��
	 */
	public void setDcurdate(UFDate dcurdate) {
		this.dcurdate = dcurdate;
	}

	/**
	 * SaleOrderVO ������ע�⡣
	 */
	public SaleOrderVO() {
		super();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:36:56)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
		return m_bodyVOs;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-4-11 14:48:58)
	 * 
	 * @return int
	 */
	public boolean getFirstTime() {

		return this.m_firsttime;
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 * 
	 * @return java.lang.Boolean
	 */
	public Boolean getModPriceTag() {
		return m_bModPriceTag;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject getParentVO() {
		return m_headVO;
	}

	/**
	 * ���ض����ʶ������Ψһ��λ����
	 * 
	 * �������ڣ�(2001-2-15 9:43:38)
	 * 
	 * @return nc.vo.pub.PrimaryKey
	 */
	public String getPrimaryKey() throws nc.vo.pub.BusinessException {
		return getParentVO().getPrimaryKey();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-4-11 14:48:58)
	 * 
	 * @return int
	 */
	public int getStatus() {

		return this.m_status;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:36:56)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public void setChildrenVO(nc.vo.pub.CircularlyAccessibleValueObject[] children) {
		m_bodyVOs = (SaleorderBVO[]) children;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-4-11 14:48:58)
	 * 
	 * @return int
	 */
	public boolean setFirstTime(boolean firsttime) {

		return m_firsttime = firsttime;
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 * 
	 * @param newvalue
	 *            java.lang.Boolean
	 */
	public void setModPriceTag(Boolean newvalue) {
		m_bModPriceTag = newvalue;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public void setParentVO(nc.vo.pub.CircularlyAccessibleValueObject parent) {
		m_headVO = (SaleorderHVO) parent;
	}

	/**
	 * ���ض����ʶ������Ψһ��λ����
	 * 
	 * �������ڣ�(2001-2-15 9:43:38)
	 * 
	 * @return nc.vo.pub.PrimaryKey
	 */
	public void setPrimaryKey(String key) throws nc.vo.pub.BusinessException {
		getParentVO().setPrimaryKey(key);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-4-11 14:48:58)
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
		
		/**====1.���������С�߽�ֵ�ж�======================================*/
		FieldDBValidate.validate(this) ;
		
		/**====2.��ͷ����У��===============================================*/
		getParentVO().validate();

		ArrayList<Integer> rowList = new ArrayList<Integer>();
		if (getChildrenVO() == null || getChildrenVO().length == 0)
			if (m_status == nc.vo.pub.VOStatus.NEW)
				throw new nc.vo.pub.ValidationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("40060301", "UPP40060301-000089")/*
																		 * @res
																		 * "�����岻��Ϊ�գ�"
																		 */);
		
		/**====3.����У��===================================================*/
		for (int i = 0; i < getChildrenVO().length; i++)
			if (getChildrenVO()[i].getStatus() != nc.vo.pub.VOStatus.DELETED) {
				try {
					getChildrenVO()[i].validate();
				} catch (nc.vo.pub.ValidationException e) {
					rowList.add(new Integer(i));
					String err = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("40060301",
							"UPP40060301-000090", null, new String[] { (i + 1) + "" })
							+ e.getMessage();
					// String err = "��" + nc.vo.pub.CommonConstant.BEGIN_MARK
					// + (i + 1) + nc.vo.pub.CommonConstant.END_MARK + "��"
					// + e.getMessage();
					throw new nc.vo.pub.ValidationException(err);
				}
			}
	}


	/**
	 * ��֤���������֮��������߼���ȷ�ԡ�
	 * 
	 * �������ڣ�(2001-2-15 11:47:35)
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                �����֤ʧ�ܣ��׳� ValidationException���Դ�����н��͡�
	 */
	public void validate() throws nc.vo.pub.ValidationException {
		validateForBS();
	}

	/**
	 * 
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
		// ��������˾,�Ƿ�ֱ�˷ֵ�
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO[] splitOrdVOByCorp() {
		// ��������˾�ֵ�
		return (SaleOrderVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs.getSplitVO(
				"nc.vo.so.so001.SaleOrderVO", "nc.vo.so.so001.SaleorderHVO",
				"nc.vo.so.so001.SaleorderBVO", this, null, new String[] { "cconsigncorpid" });

	}

	// private String bizTypeid=null;
	// private String operatorid=null;
	// private String pk_corp=null;

	private nc.vo.so.so016.OrdBalanceVO allordbalvo = null;

	private SaleOrderVO allSaleOrderVO = null;// �����޸�ʱ�������޸���ɵ�VO

	private boolean bCheckATP = true;// ATP����־

	// private boolean bCheckOverCredit = true;//����ռ�ü���־
	//
	// private boolean bCheckOverPeriod = true;//�����ڼ���־

	// ���ü����Ҫ���ֶ�
	// private String actionCode=null;
	// private int actionInt=-1;
	private String billTypeCode = "30";

	private int billTypeInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_BILL_ORDER;

	private nc.vo.so.so016.OrdBalanceVO chgordbalvo = null;

	nc.vo.scm.pub.session.ClientLink clientLink = null;

	private String errMsg = "";

	private String hintMsg = "";

	private int iAction = nc.vo.so.so001.ISaleOrderAction.A_DEFAULT;// ��Ҫ�Զ�����ȡ�Ķ���

	private SaleOrderVO oldSaleOrderVO = null;// �����޸�ʱ���޸�ǰ��VO

	// Ϊ������ʹ��
	private String actioncode;
	
	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 9:03:50) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-27 14:41:11) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.so.so016.OrdBalanceVO
	 */
	public nc.vo.so.so016.OrdBalanceVO getAllordbalvo() {
		return allordbalvo;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-2 9:23:09)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO getAllSaleOrderVO() {
		return allSaleOrderVO;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillTypeCode() {
		return billTypeCode;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return int
	 */
	public int getBillTypeInt() {
		return billTypeInt;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
	 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public SaleorderBVO[] getBodyVOs() {
		return m_bodyVOs;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-27 14:41:11) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.so.so016.OrdBalanceVO
	 */
	public nc.vo.so.so016.OrdBalanceVO getChgordbalvo() {
		return chgordbalvo;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-27 11:21:27)
	 * 
	 * @return nc.vo.scm.pub.session.ClientLink
	 */
	public nc.vo.scm.pub.session.ClientLink getClientLink() {
		return clientLink;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-15 16:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getErrMsg() {
		return errMsg;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public SaleorderHVO getHeadVO() {
		return m_headVO;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-15 16:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getHintMsg() {
		return hintMsg;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-1 15:45:23)
	 * 
	 * @return int
	 */
	public int getIAction() {
		return iAction;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:58:14) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getModifiedVO() {
		return getAllSaleOrderVO();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-1 13:38:24)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public SaleOrderVO getOldSaleOrderVO() {
		return oldSaleOrderVO;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:59:35) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getOldVO() {
		return getOldSaleOrderVO();
	}

	private String operatorid = null;

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ���ò���Ա����
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
			// �Ƿ�����
			if (bvos[i].getLaborflag() != null && bvos[i].getLaborflag().booleanValue())
				continue;

			// �Ƿ��ۿ�
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-7-14 17:17:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPfAssMoney() {
		return null;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-7-14 16:42:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public String getPfCurrency() {
		if (getBodyVOs() == null || getBodyVOs().length <= 0)
			return null;
		return getBodyVOs()[0].getCcurrencytypeid();
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-7-14 17:17:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPfLocalMoney() {
		return getTotalcursummny("nsummny");
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-7-14 17:17:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPfMoney() {
		return getTotalcursummny("noriginalcursummny");
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-7-14 17:08:47) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param key
	 *            java.lang.String
	 */
	public nc.vo.pub.lang.UFDouble getTotalcursummny(String key) {
		if (getBodyVOs() == null || getBodyVOs().length <= 0 || key == null)
			return null;
		// ȱ��
		nc.vo.pub.lang.UFBoolean boosflag = null;
		// ��Ʒ
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-15 16:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 */
	public boolean isBCheckATP() {
		return bCheckATP;
	}

	/**
	 * ��鶩���Ƿ�๫˾������ �������ڣ�(2004-4-22 13:03:12)
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-27 14:41:11) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newAllordbalvo
	 *            nc.vo.so.so016.OrdBalanceVO
	 */
	public void setAllordbalvo(nc.vo.so.so016.OrdBalanceVO newAllordbalvo) {
		allordbalvo = newAllordbalvo;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-2 9:23:09)
	 * 
	 * @param newAllSaleOrderVO
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	public void setAllSaleOrderVO(SaleOrderVO newAllSaleOrderVO) {
		allSaleOrderVO = newAllSaleOrderVO;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-15 16:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newBillTypeCode
	 *            java.lang.String
	 */
	public void setBillTypeCode(java.lang.String newBillTypeCode) {
		billTypeCode = newBillTypeCode;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newBillTypeInt
	 *            int
	 */
	public void setBillTypeInt(int newBillTypeInt) {
		billTypeInt = newBillTypeInt;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-27 14:41:11) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newChgordbalvo
	 *            nc.vo.so.so016.OrdBalanceVO
	 */
	public void setChgordbalvo(nc.vo.so.so016.OrdBalanceVO newChgordbalvo) {
		chgordbalvo = newChgordbalvo;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-27 11:21:27)
	 * 
	 * @param newClientLink
	 *            nc.vo.scm.pub.session.ClientLink
	 */
	public void setClientLink(nc.vo.scm.pub.session.ClientLink newClientLink) {
		clientLink = newClientLink;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-15 16:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-15 16:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �˴����뷽��˵���� �������ڣ�(2004-4-1 15:45:23)
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
	 * �˴����뷽��˵���� �������ڣ�(2004-4-1 13:38:24)
	 * 
	 * @param newOldSaleOrderVO
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	public void setOldSaleOrderVO(SaleOrderVO newOldSaleOrderVO) {
		oldSaleOrderVO = newOldSaleOrderVO;
	}

	/**
	 * 
	 * �������ڣ�(2004-3-28 14:50:54) ���ߣ������ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
		// ��������˾,���������֯,�ջ������֯,�Ƿ�ֱ�˷ֵ�
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-5-21 8:56:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-1-12 18:53:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCuruserid() {
		return curuserid;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-1-12 18:53:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newCuruserid
	 *            java.lang.String
	 */
	public void setCuruserid(java.lang.String newCuruserid) {
		curuserid = newCuruserid;
		setLogip();
	}

	/**
	 * @return ���� ccorpname��
	 */
	public String getCcorpname() {
		return ccorpname;
	}

	/**
	 * @param ccorpname
	 *            Ҫ���õ� ccorpname��
	 */
	public void setCcorpname(String ccorpname) {
		this.ccorpname = ccorpname;
	}

	/**
	 * @return ���� cusername��
	 */
	public String getCusername() {
		return cusername;
	}

	/**
	 * @param cusername
	 *            Ҫ���õ� cusername��
	 */
	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	/**
	 * @return ���� cnodename��
	 */
	public String getCnodename() {
		return cnodename;
	}

	/**
	 * @param cnodename
	 *            Ҫ���õ� cnodename��
	 */
	public void setCnodename(String cnodename) {
		this.cnodename = cnodename;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-27 14:41:11) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
					// //������ô��������
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
	 * ���ص�½����
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

	// �Ƿ���Ҫ���ڼ��
	boolean m_bCheckPeriod = true;

	// �Ƿ���Ҫ���ü��
	boolean m_bCheckCredit = true;

	/**
	 * �����Ƿ���Ҫ���ڼ��
	 * 
	 * @return
	 */
	public boolean isCheckPeriod() {
		return m_bCheckPeriod;
	}

	/**
	 * �����Ƿ���Ҫ���ü��
	 * 
	 * @return
	 */
	public boolean isCheckCredit() {
		return m_bCheckCredit;
	}

	/**
	 * �����Ƿ���Ҫ���ڼ��
	 * 
	 * @return
	 */
	public void setCheckPeriod(boolean b) {
		m_bCheckPeriod = b;
	}

	/**
	 * �����Ƿ���Ҫ���ü��
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
					"UPPSCMCommon-000340")/* @res "����" */;
		}
		case BillStatus.AUDIT: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000027")/*
																									 * @res
																									 * "����"
																									 */;
		}
		case BillStatus.FREEZE: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000030")/*
																									 * @res
																									 * "����"
																									 */;
		}
		case BillStatus.CLOSE: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000119")/* @res "�ر�" */;
		}
		case BillStatus.BLANKOUT: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000005")/*
																									 * @res
																									 * "����"
																									 */;
		}
		case BillStatus.FINISH: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000128")/* @res "����" */;
		}
		case BillStatus.AUDITING: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000320")/* @res "����������" */;
		}
		case BillStatus.NOPASS: {
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000242")/* @res "����δͨ��" */;
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
	 * ���¼����ͷ��˰�ϼ�
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
	 * �����ͷ��˰�ϼƣ�ȥ��ȱ���е�Ӱ��
	 * 
	 * �����ǰ���ݶ�ȱ���д�����������
	 * 
	 * �������
	 * 
	 */
	public void reCalHeadSummny() {

		if (m_headVO == null)
			return;

		if (m_bodyVOs == null || m_bodyVOs.length == 0)
			return;

		boolean errflag = false;
		// ���ȸ��ݱ���ȥУ���ͷ
		/** ���뱣֤����ļ�˰�ϼ���ȷ* */
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
	 * У���ͷ�ͱ���ļ�˰�ϼ�
	 * 
	 * @throws ValidationException
	 */
	public void checkSummny() throws ValidationException {		
		UFDouble sum = new UFDouble(0);
		for (SaleorderBVO body : getBodyVOs()) {
			// �˴��߼���༭ʱ�Ŀ���һ��
			// ����Ʒ����ȱ��
			if ((body.getBlargessflag() == null || !body.getBlargessflag().booleanValue())
					&& (body.getBoosflag() == null || !body.getBoosflag().booleanValue())) {
				sum = sum.add(body.getNoriginalcursummny() == null ? new UFDouble(0) : body
						.getNoriginalcursummny());
			}
		}

		// ���������ݺϷ�ʱ����ͷ��˰�ϼ�һ�����ǿ�ֵ���˴�����У��
		if (getHeadVO().getNheadsummny() != null && sum.compareTo(getHeadVO().getNheadsummny()) != 0)
			throw new ValidationException("��ͷ��˰�ϼ�������ܺͲ���");
	}

	/**
	 * ���㲹��ֱ��Ҫ�������
	 * 
	 * ����ֱ���������й���ʹ��
	 * 
	 * @param sourceRedunVO
	 * @return ֻҪ��һ�б�������Ҫ��ͷ������� ����Ϊnull
	 * 
	 * @see OrderToBillRunSourceCtrl.check(RedunVO rvo)
	 */
	public SaleOrderVO getBillRedunVO(ISourceRedunVO sourceRedunVO) {
		if (m_headVO == null || m_bodyVOs == null || m_bodyVOs.length == 0)
			return null;

		ArrayList<SaleorderBVO> al_item = new ArrayList();
		for (int i = 0, len = m_bodyVOs.length; i < len; i++) {
			// ������ͷ�ͱ����ϵ
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
		// TODO �Զ����ɷ������
		return "corder_bid";
	}

	public String getBilltypecode() {
		// TODO �Զ����ɷ������
		return "30";
	}

	public String getBusitypePK() {
		// TODO �Զ����ɷ������
		return getHeadVO().getCbiztype();
	}

	public int getCoopwithType() {
		// TODO �Զ����ɷ������
		return 1;
	}

	public String getCustomerID() {
		// TODO �Զ����ɷ������
		return getHeadVO().getPk_corp();
	}

	public String getOrgid() {
		// TODO �Զ����ɷ������
		return getHeadVO().getCsalecorpid();
	}

	public String getSourBIDItemkey() {
		// TODO �Զ����ɷ������
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

		// ��˾ID(��ǰ��˾)
		salepriceVO.setCropID(hvo.getPk_corp());

		// ҵ������
		salepriceVO.setBizTypeID(hvo.getCbiztype());

		// �ͻ�ID
		salepriceVO.setCustomerID(hvo.getCcustomerid());

		// �ͻ�����
		salepriceVO.setCustomerClass(bvo.getCchantypeid());

		salepriceVO.setBuseBaseStrict(Boolean.FALSE);

		// ����ID
		salepriceVO.setDeptid(hvo.getCdeptid());

		// ������֯
		salepriceVO.setSaleStrucid(hvo.getCsalecorpid());

		// ϵͳ����
		salepriceVO.setSystemData(hvo.getDbilldate());

		// ���ID
		salepriceVO.setInventoryID(bvo.getCinventoryid());

		// �������ID
		salepriceVO.setInventoryBaseID(bvo.getCinvbasdocid());

		// ����ID
		salepriceVO.setCurrencyID(bvo.getCcurrencytypeid());

		// ����
		salepriceVO.setNumber(bvo.getNquoteunitnum());

		// ������1
		salepriceVO.setFree1(bvo.getVfree1());

		// ������2
		salepriceVO.setFree2(bvo.getVfree2());

		// ������3
		salepriceVO.setFree3(bvo.getVfree3());

		// ������4
		salepriceVO.setFree4(bvo.getVfree4());

		// ������5
		salepriceVO.setFree5(bvo.getVfree5());

		// ѯ�ۼ�����λ
		salepriceVO.setMeasdocid(bvo.getCquoteunitid());

		// �۸���
		salepriceVO.setPriceTypeid(bvo.getCpriceitemid());

		// ��Ŀ��
		salepriceVO.setPricetariffid(bvo.getCpriceitemtable());

		// �۸����
		salepriceVO.setPricePolicyid(bvo.getCpricepolicyid());

		// �ջ�����
		salepriceVO.setReceiptAreaid(bvo.getCreceiptareaid());

		return salepriceVO;
	}
	
	/**
	 * ��úϲ����ѯ�۲���
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

		// { "���������", ѯ�ۼ�����λ,�۸���,��Ŀ��,�۸����,�ջ�����,"free1", "free2", "free3",
		// "free4", "free5" };

		String key = "";

		key += vo.getInventoryID();

		// ѯ�ۼ�����λ
		key += vo.getMeasdocid();
		// �۸���
		key += vo.getPriceTypeid();
		// ��Ŀ��
		key += vo.getPricetariffid();
		// �۸����
		key += vo.getPricePolicyid();
		// �ջ�����
		key += vo.getReceiptAreaid();

		key += vo.getFree1();
		key += vo.getFree2();
		key += vo.getFree3();
		key += vo.getFree4();
		key += vo.getFree5();

		// �ͻ�����
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
						// ���ۺ�˰����
						m_bodyVOs[findrow].setNorgqttaxprc(resultvos[i].getNum());
						// ԭʼѯ�ۺ�˰����
						m_bodyVOs[findrow].setNqtorgtaxprc(resultvos[i].getNum());
					} else {
						// ������˰����
						m_bodyVOs[findrow].setNorgqtprc(resultvos[i].getNum());
						// ԭʼѯ����˰����
						m_bodyVOs[findrow].setNqtorgprc(resultvos[i].getNum());
					}
					m_bodyVOs[findrow].setNitemdiscountrate(resultvos[i].getDiscount());
				}
			}
		}
	}
	
	/**
	 * Ѱ��ʧ����������
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

		"norgqttaxprc", //���۵�λ��˰����
		"norgqtprc", //���۵�λ��˰����
		"norgqttaxnetprc", //���۵�λ��˰����
		"norgqtnetprc", //���۵�λ��˰����

		"nqttaxnetprc", //���۵�λ���Һ�˰����
		"nqtnetprc", //���۵�λ������˰����
		"nqttaxprc", //���۵�λ���Һ�˰����
		"nqtprc", //���۵�λ������˰����

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
