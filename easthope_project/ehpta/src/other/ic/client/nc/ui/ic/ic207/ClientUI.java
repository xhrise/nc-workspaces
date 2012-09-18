package nc.ui.ic.ic207;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.pub.ValidationException;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICPriceException;

/**
 * �˴���������˵���� �������ڣ�(2001-11-23 15:39:43)
 * 
 * @author�����˾�
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
	// ȡ��浥��
	// private ButtonObject m_boQueryPrice ;
	private boolean m_bIsOutBill = false;
	private UITextField m_uitfHinttext = new UITextField();

	/**
	 * ClientUI2 ������ע�⡣
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * ClientUI ������ע�⡣ add by liuzy 2007-12-18 ���ݽڵ�����ʼ������ģ��
	 */
	public ClientUI(FramePanel fp) {
		super(fp);
		initialize();
	}

	/**
	 * ClientUI ������ע�⡣ nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
	 * 
	 */
	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);

	}

	public nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
		return super.getBillCardPanel();
	}

	public nc.ui.pub.bill.BillListPanel getBillListPanel() {
		return super.getBillListPanel();
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		// nc.vo.scm.pub.SCMEnv.out("haha,bill edit/.");

	}

	/**
	 * �����ߣ����˾� ���ܣ���������ѡ��ı� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void afterBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,sel chged!");

	}

	/**
	 * �����ߣ�����
	 * ���ܣ�ר��Ϊ����������ⵥ����setAllData�е��òֿ�ı��¼����������ֿ����Info�Ϳ����֯Info���������ԭ�еĿ����֯��
	 * ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterWhEditNoClearCalbody(nc.ui.pub.bill.BillEditEvent e) {
		// �ֿ�
		try {
			// String sNewWhName =
			// ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			// .getHeadItem(IItemKey.WAREHOUSE)
			// .getComponent())
			// .getRefName();
			// String sNewWhID =
			// ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			// .getHeadItem(IItemKey.WAREHOUSE)
			// .getComponent())
			// .getRefPK();
			// zhy2005-05-26���Ч�����⣬���ӽ���ȡ����ֱ�Ӵ�vo��ȡ
			String sNewWhID = ((GeneralBillHeaderVO) getM_voBill()
					.getParentVO()).getCwarehouseid();
			// ����˲ֿ�
			if (sNewWhID == null) {
				// ������κŲ��յĲֿ�
				getLotNumbRefPane().setWHParams(null);
				// if (m_voBill != null)
				// m_voBill.setWh(null);
			} else {

				// �������������б���ʽ����ʾ��
				// ��ѯ�ֿ���Ϣ
				// ��ѯ��ʽ������Ѿ�¼���˴������Ҫͬʱ��ƻ��ۡ�
				int iQryMode = QryInfoConst.WH;
				// ����
				Object oParam = sNewWhID;
				// ��ǰ��¼��Ĵ������
				ArrayList alAllInvID = new ArrayList();
				boolean bHaveInv = getCurInvID(alAllInvID);

				// �ֿ�
				WhVO voWh = null;
				// ������κŲ��յĲֿ�
				getLotNumbRefPane().setWHParams(null);
				if (getM_voBill() != null)
					getM_voBill().setWh(null);

				if (bHaveInv) {
					// �������ֿ�ID,ԭ�����֯ID,��λID,���ID
					ArrayList alParam = new ArrayList();
					alParam.add(sNewWhID);
					iQryMode = QryInfoConst.WH_PLANPRICE;
					// ��ǰ�Ŀ����֯,����û�вֿ�������
					if (getM_voBill() != null && getM_voBill().getWh() != null)
						alParam.add(getM_voBill().getWh().getPk_calbody());
					else
						alParam.add(null);
					// ��˾
					alParam.add(getEnvironment().getCorpID());
					// ��ǰ�Ĵ��
					alParam.add(alAllInvID);
					oParam = alParam;
				}

				Object oRet = GeneralBillHelper.queryInfo(
						new Integer(iQryMode), oParam);

				// ��ǰ��¼��Ĵ������,�����޸��˿����֯�ŷ���һ��ArrayList
				if (oRet instanceof ArrayList) {
					ArrayList alRetValue = (ArrayList) oRet;
					if (alRetValue != null && alRetValue.size() >= 2) {
						voWh = (WhVO) alRetValue.get(0);
						// ˢ�¼ƻ���
						freshPlanprice((ArrayList) alRetValue.get(1));
					}
				} else
					// ���򷵻� WhVO
					voWh = (WhVO) oRet;
				// �����֯����
				nc.ui.pub.bill.BillItem biCalBody = getBillCardPanel()
						.getHeadItem("pk_calbody");
				if (biCalBody != null) {
					if (voWh != null)
						biCalBody.setValue(voWh.getPk_calbody());
					else
						biCalBody.setValue(null);
				}
				nc.ui.pub.bill.BillItem biCalBodyname = getBillCardPanel()
						.getHeadItem("vcalbodyname");
				if (biCalBodyname != null) {
					if (voWh != null)
						biCalBodyname.setValue(voWh.getVcalbodyname());
					else
						biCalBodyname.setValue(null);
				}

				if (getM_voBill() != null) {
					getM_voBill().setWh(voWh);
					// ���β�ִ���
					getM_voBill().clearInvQtyInfo();
					getLotNumbRefPane().setWHParams(voWh);
				}

				// ���λ�����к����ݷ���afterEdit()��clearLocSn��
				// int iRowCount = getBillCardPanel().getRowCount();
				// for (int row = 0; row < iRowCount; row++)
				// clearRowData(0, row, IItemKey.WAREHOUSE);
				// ˢ���ִ�����ʾ
				// setTailValue(0);
				// ���û�λ���䰴ť�Ƿ���á�
				setBtnStatusSpace(true);
			}

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.out(e2);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
		return true;
	}

	/**
	 * �����ߣ����˾� ���ܣ���������ѡ��ı� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void beforeBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,before sel");

	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷�������ǰ��VO��� �����������浥�� ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
		String sSourceBillType = getSourBillTypeCode();
		try {
			boolean bCheck = true;
			bCheck = checkVO();
			// ���������ⵥ����������Ϊ��
			if (sSourceBillType != null
					&& (sSourceBillType
							.equals(nc.vo.ic.pub.BillTypeConst.m_assembly)
							|| sSourceBillType
									.equals(nc.vo.ic.pub.BillTypeConst.m_disassembly)
							|| sSourceBillType
									.equals(nc.vo.ic.pub.BillTypeConst.m_transform)
							|| sSourceBillType
									.equals(nc.vo.ic.pub.BillTypeConst.m_check)

					// 2002-10-19.wnj ת�ⵥ�����븺��||
					// sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)
					|| sSourceBillType
								.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
				// ��Դ����������transfer,...check
				// ����>=0���
				nc.vo.ic.pub.check.VOCheck.checkGreaterThanZeroInput(
						voBill.getChildrenVO(),
						getEnvironment().getNumItemKey(),
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0002282")/* @res "����" */);
			}
			return bCheck;

		} catch (ICPriceException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ValidationException e) {
			nc.vo.scm.pub.SCMEnv.out("У���쳣������δ֪����...");
			handleException(e);
			return false;
		}
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-5-28 16:56:37) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param sItemKey
	 *            java.lang.String
	 * @param sCond
	 *            java.lang.String
	 */
	public void filterWhRef(String sPk_calbody) {
		// ����˿����֯
		// �����ǰ�Ĳֿⲻ����
		// ���˲ֿ����
		String sConstraint[] = null;
		if (sPk_calbody != null) {
			sConstraint = new String[1];
			sConstraint[0] = " AND pk_calbody='" + sPk_calbody + "'";
		}
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
				IItemKey.WAREHOUSE);
		nc.ui.ic.pub.bill.initref.RefFilter.filtWh(bi, getEnvironment()
				.getCorpID(), sConstraint);

	}

	/**
	 * zrq ��Ҫ�ĵõ����ı������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-15 14:02:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public int getCardTableRowNum() {
		return getBillCardPanel().getRowCount();
	}

	public GeneralBillVO getMVOBill() {
		return getM_voBill();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-28 19:07:32)
	 */
	public int getLastSelListHeadRow() {
		return getM_iLastSelListHeadRow();
	}

	/**
	 * �����ߣ������� ���ܣ��ⲿ���뵥�ݺ� ������ ���أ� ���⣺ ���ڣ�(2001-11-26 14:50:38)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 */
	public ArrayList getSerialData() {
		return getM_alSerialData();
	}

	public ArrayList getLocatorData() {
		return getM_alLocatorData();
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-29 19:46:17) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param hinttext
	 *            nc.ui.pub.beans.UITextField
	 */
	public UITextField getUITxtFldStatus() {
		return m_uitfHinttext;
	}

	/**
	 * ����˵���� �������ڣ�(2002-11-28 14:12:13) ���ߣ�����IC �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	public void initialize() {
		// �������ػ����initialize������,
		super.initialize();
		// ���չ���
		BillItem bi = getBillCardPanel().getHeadItem("cotherwhid");
		RefFilter.filtWh(bi, getEnvironment().getCorpID(), null);

	}

	/**
	 * �����ߣ����˾� ���ܣ���ʼ��ϵͳ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void initPanel() {
		// ��Ҫ���ݲ���
		super.setNeedBillRef(false);
	}

	public String getBillType() {
		return nc.vo.ic.pub.BillTypeConst.m_otherIn;
	}

	public String getFunctionNode() {
		return "40080608";
	}

	public int getInOutFlag() {
		return InOutFlag.IN;
	}

	/**
	 * ���ܣ�ȡ����ǰ�����ϵĴ���ĵ�ǰ���½�浥�� �������ڣ�(2002-12-25 16:34:26) ���ߣ����˾� �޸����ڣ� �޸��ˣ� �޸�ԭ��
	 * �㷨˵����
	 */
	private void onQueryPrice() {
		try {
			// ȡ��ǰ������ʾ������
			GeneralBillVO voCurBill = getBillVO();
			if (voCurBill != null && voCurBill.getHeaderVO() != null
					&& voCurBill.getItemVOs() != null
					&& voCurBill.getItemVOs().length > 0) {
				GeneralBillHeaderVO voHead = voCurBill.getHeaderVO();
				GeneralBillItemVO[] voaItem = voCurBill.getItemVOs();
				// ׼���������ݡ�
				// String pk_corp = getEnvironment().getCorpID(); //��˾
				String pk_calbody = voHead.getPk_calbody(); // �����֯
				String cwarehouseid = voHead.getCwarehouseid(); // �ֿ�
				Hashtable htInvBatch = new Hashtable();
				if (cwarehouseid != null && cwarehouseid.trim().length() > 0) {
					String[] cinventoryids = null; // �����
					Vector vInv = new Vector();
					String[] sLots = null; // ������
					Vector vLot = new Vector();
					String sKey = null;// invid+batchcode
					for (int i = 0; i < voaItem.length; i++) {
						// ���vector��û�д˴������id�����д˴����û�д˴�������Σ��Ļ����ͼӽ�ȥ��
						if (voaItem[i] != null
								&& voaItem[i].getCinventoryid() != null) {
							sKey = voaItem[i].getCinventoryid() + "&"
									+ voaItem[i].getVbatchcode();
							if (!htInvBatch.containsKey(sKey)) {
								vInv.addElement(voaItem[i].getCinventoryid());
								// ����
								vLot.addElement(voaItem[i].getVbatchcode());
								htInvBatch.put(sKey, "");
							}
						}
					}
					// �д����
					if (vInv.size() > 0) {
						cinventoryids = new String[vInv.size()];
						vInv.copyInto(cinventoryids);
						sLots = new String[vLot.size()]; // ���Ⱥʹ����ͬ��
						vLot.copyInto(sLots);

						ArrayList alParam = new ArrayList();
						alParam.add(getEnvironment().getCorpID());
						alParam.add(pk_calbody);
						alParam.add(cwarehouseid);
						alParam.add(cinventoryids);
						alParam.add(sLots);
						// ִ�в�ѯ
						ArrayList alRet = (ArrayList) ICReportHelper.queryInfo(
								new Integer(QryInfoConst.SETTLEMENT_PRICE),
								alParam);
						if (alRet != null && alRet.size() > 0) {
							// ���õ�������
							setPrice(alRet);
							showHintMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000285")/*
																 * @res
																 * "��ȡ��浥����ϡ�"
																 */);
						} else {
							showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000286")/*
																 * @res
																 * "û�в�ѯ����ǰ����Ľ�浥�ۣ�"
																 */);
						}
					} else {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008busi", "UPP4008busi-000287")/*
																			 * @res
																			 * "����¼������Ȼ�����ԡ�"
																			 */);
					}
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008busi", "UPP4008busi-000288")/*
																		 * @res
																		 * "����¼��ֿ⣬Ȼ�����ԡ�"
																		 */);
				}
			} else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000289")/*
														 * @res
														 * "��ǰû�п��õ��ݣ����ѯ���������ݡ�"
														 */);
		} catch (Exception e) {
			showWarningMessage(e.getMessage());
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ���ѯ����ָ�������ĵ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 */

	public void queryBills(nc.vo.ic.pub.bill.QryConditionVO voQryCond) {
		try {

			setM_alListData(GeneralBillHelper.queryBills(getBillType(),
					voQryCond));
			if (getM_alListData() != null && getM_alListData().size() > 0) {

				GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[getM_alListData()
						.size()];
				for (int i = 0; i < getM_alListData().size(); i++) {
					if (getM_alListData().get(i) != null)
						voh[i] = (GeneralBillHeaderVO) ((GeneralBillVO) getM_alListData()
								.get(i)).getParentVO();
					else
						nc.vo.scm.pub.SCMEnv.out("list data error!-->" + i);

				}
				setListHeadData(voh);
				// ��ǰ�Ǳ���ʽʱ�������л����б���ʽ
				if (BillMode.Card == getM_iCurPanel())
					getButtonManager().onButtonClicked(
							getButtonManager().getButton(
									ICButtonConst.BTN_SWITCH));

				// ȱʡ��ͷָ���һ�ŵ���
				selectListBill(0);
				// ���õ�ǰ�ĵ�������/��ţ����ڰ�ť����
				setM_iLastSelListHeadRow(0);
				// ��ʼ����ǰ������ţ��л�ʱ�õ������������������ñ������ݡ�
				m_iCurDispBillNum = -1;
				// ��ǰ������
				m_iBillQty = getM_alListData().size();

				setButtonStatus(true);
				if (m_iBillQty > 0)
					showHintMessage(nc.ui.ml.NCLangRes
							.getInstance()
							.getStrByID("4008busi", "UPP4008busi-000290", null,
									new String[] { String.valueOf(m_iBillQty) })/*
																				 * @
																				 * res
																				 * "���鵽{0}�ŵ��ݣ�"
																				 */);
				else
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008busi", "UPP4008busi-000291")/*
																		 * @res
																		 * "δ�鵽���ݡ�"
																		 */);

			} else {
				// ?????
			}

		} catch (Exception e) {
			handleException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000292")/* @res "��ѯ����" */);
		}
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-07-19 14:05:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void removeListHeadMouseListener() {
		// if (getBillListPanel().getHeadTable() != null)
		// getBillListPanel().getHeadTable().removeMouseListener(this);
	}

	/**
	 * �����ߣ����˾� ���ܣ����б�ʽ��ѡ��һ�ŵ��� ������ ������alListData�е����� ���أ��� ���⣺ ���ڣ�(2001-11-23
	 * 18:11:18) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void selectBillOnListPanel(int iBillIndex) {
	}

	/**
	 * �����ߣ����˾� ���ܣ����ý���ĵ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 */

	public void setAllData(ArrayList<GeneralBillVO> alListData) {
		try {
			m_bIsOutBill = true;
			setM_alListData(alListData);
			if (getM_alListData() != null && getM_alListData().size() > 0) {

				GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[getM_alListData()
						.size()];
				for (int i = 0; i < getM_alListData().size(); i++) {
					if (getM_alListData().get(i) != null)
						voh[i] = (GeneralBillHeaderVO) ((GeneralBillVO) getM_alListData()
								.get(i)).getParentVO();
					else
						nc.vo.scm.pub.SCMEnv.out("list data error!-->" + i);

				}
				setListHeadData(voh);
				// ��ǰ�Ǳ���ʽʱ�������л����б���ʽ
				/*
				 * if (BillMode.Card == m_iCurPanel) onSwitch();
				 */

				// ȱʡ��ͷָ���һ�ŵ���
				selectListBill(0);
				// ���õ�ǰ�ĵ�������/��ţ����ڰ�ť����
				setM_iLastSelListHeadRow(0);
				// ��ʼ����ǰ������ţ��л�ʱ�õ������������������ñ������ݡ�
				m_iCurDispBillNum = -1;
				// ��ǰ������
				m_iBillQty = getM_alListData().size();

				if (getM_iLastSelListHeadRow() >= 0
						&& m_iCurDispBillNum != getM_iLastSelListHeadRow()
						&& getM_alListData() != null
						&& getM_alListData().size() > getM_iLastSelListHeadRow()
						&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
					for (int i = 0; i < getM_alListData().size(); i++) {
						setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData()
								.get(i)));
						// zhy2005-05-26���Ч�����⣬��Ϊ������Ĵ����л�ִ�����е�setBillVO(m_voBill)�������ڴ˲���Ҫ�ظ�ִ��
						if (i != 0)
							setBillVO(getM_voBill());
						this.afterWhEditNoClearCalbody(null);
						getM_alListData().set(i, getM_voBill());
					}
					// zhy2005-05-27���Ч�ʣ���ִ��setBillVO(m_voBill);ʱ���vo����clone,���Դ˴�����Ҫclone
					setM_voBill((GeneralBillVO) getM_alListData().get(
							getM_iLastSelListHeadRow()));

					setBillVO(getM_voBill());

				}

				setButtonStatus(true);

			} else {
				// ?????
			}

		} catch (Exception e) {
			handleException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000292")/* @res "��ѯ����" */);
		}
	}

	/**
	 * �����ߣ������� ���ܣ��ⲿ���뵥�ݺ� ������ ���أ� ���⣺ ���ڣ�(2001-11-26 14:50:38)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 */
	public boolean setBillCodeAuto() {
		nc.ui.pub.bill.BillItem bi = getBillCardPanel()
				.getHeadItem("vbillcode");
		if (bi != null
				&& (bi.getValue() == null || bi.getValue().trim().length() == 0)) {
			if (!m_bIsEditBillCode) {
				getM_voBill().setVBillCode(getEnvironment().getCorpID());
				bi.setValue(getEnvironment().getCorpID());
			}

		}

		return true;
	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO, ������״̬(updateValue()) ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBillValueVO(GeneralBillVO bvo) {
		getBillCardPanel().getBillModel().removeTableModelListener(this);
		try {
			getBillCardPanel().setBillValueVO(bvo);
			// ִ�й�ʽ
			getBillCardPanel().getBillModel().execLoadFormula();
			execHeadTailFormulas();
			//
			bvo.clearInvQtyInfo();
			// ѡ�е�һ��
			// getBillCardPanel().getBillTable().setRowSelectionAllowed();
			getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
			// ˢ���ִ�����ʾ
			// setTailValue(0);

		} catch (Exception e) {
			// try��Ŀ���Ǳ�֤addListener��ִ�С�
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}

		getBillCardPanel().getBillModel().addTableModelListener(this);
		/** �����ݵ���Դ����Ϊת�ⵥʱ, ���н������ */
		ctrlSourceBillUI(true);
	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO, ������ΪTrueʱ��ֻ��m_voBill,������޲�����setBillVO() ������ ���أ� ���⣺
	 * ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void setBillVO(GeneralBillVO bvo, boolean bIsOnlySet) {
		try {
			if (bIsOnlySet) {
				getBillCardPanel().addNew();
				setBillValueVO(bvo);
				setM_voBill(bvo);
			} else {
				setBillVO(bvo);
			}
			// ִ�й�ʽ
			getBillCardPanel().getBillModel().execLoadFormula();
			execHeadTailFormulas();

			//
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-7-19 ���� 10:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param bShowFlag
	 *            boolean
	 */
	public void setBodyMenuShow(boolean bShowFlag) {
		getBillCardPanel().setBodyMenuShow(bShowFlag);
	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á� ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setButtonsStatus(int iBillMode) {
		// ���ö���浥���Ƿ����
		switch (iBillMode) {
		case BillMode.New:
		case BillMode.Update:
			getBoQueryPrice().setEnabled(true);
			break;
		case BillMode.Browse:
			getBoQueryPrice().setEnabled(false);
			break;
		}
		updateButton(getBoQueryPrice());
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-25 13:48:13) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void setCardMode(int NewCardMode) {
		setM_iMode(NewCardMode);
	}

	/**
	 * �˴����뷽��˵���� �����ߣ����� ���ܣ����ñ���ʽ�Ƿ�ɱ༭��
	 * 
	 * �����ⲿ���ã���Ҫ�������Լ�ά����ǰ��New,Update,Browse��״̬��
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2001-6-26 15:04:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newM_iCurPanel
	 *            int
	 */
	public void setCardPanelEnable(boolean bEnable) {
		// ���ɱ༭
		getBillCardPanel().setEnabled(bEnable);
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-5-28 16:56:01) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param sItemKey
	 *            java.lang.String
	 */
	public void setHeadItemEnable(String sItemKey, boolean bCan) {
		// ����:clear warehouse
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(sItemKey);
		if (bi != null)
			bi.setEnabled(bCan);

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-28 19:07:32)
	 */
	public void setLastSelListHeadRow(int lastrow) {
		setM_iLastSelListHeadRow(lastrow);
		selectListBill(lastrow);
	}

	/**
	 * ���ܣ��õ�ǰ�����ϵĴ���ĵ�ǰ���½�浥�� �������ڣ�(2002-12-25 16:34:26) ���ߣ����˾� �޸����ڣ� �޸��ˣ� �޸�ԭ��
	 * �㷨˵����
	 */
	private void setPrice(ArrayList alData) {

		final String IK_INV = "cinventoryid"; // ��������itemkey
		final String IK_BATCH_CODE = "vbatchcode"; // �����������κ�
		final String IK_PRICE = "nprice"; // ��������itemkey

		if (alData != null && alData.size() > 0) {
			// ����HASHTABLE:KEY=INVID+VBATCHCODE,VALUE=PRICE����߲�ѯЧ��

			getBillCardPanel().getBillModel().setNeedCalculate(false);

			Hashtable htData = new Hashtable();
			ArrayList alTempData = null;
			String sKey = null; // ==invid+vlot
			for (int i = 0; i < alData.size(); i++) {
				if (alData.get(i) != null) {
					alTempData = (ArrayList) alData.get(i);
					if (alTempData.size() >= 3 && alTempData.get(0) != null
							&& alTempData.get(2) != null) {
						// invid+&+vlot,price
						sKey = alTempData.get(0).toString() + "&"
								+ alTempData.get(1);
						if (!htData.containsKey(sKey))
							htData.put(sKey, alTempData.get(2));
					}
				}
			}
			int rowcount = getBillCardPanel().getRowCount();
			String cinventoryid = null, vlot = null;
			for (int i = 0; i < rowcount; i++) {
				cinventoryid = (String) getBillCardPanel().getBodyValueAt(i,
						IK_INV);
				vlot = (String) getBillCardPanel().getBodyValueAt(i,
						IK_BATCH_CODE);
				// invid+&+vlot,price
				sKey = cinventoryid + "&" + vlot;
				if (sKey != null && htData.containsKey(sKey))
					getBillCardPanel().setBodyValueAt(htData.get(sKey), i,
							IK_PRICE);
			}
			// ͬ��vo
			// ����Ҫ
			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();

		} else {
			nc.vo.scm.pub.SCMEnv.out("no price to be set");
		}
	}

	/**
	 * �����ߣ������� ���ܣ��ⲿ���뵥�ݺ� ������ ���أ� ���⣺ ���ڣ�(2001-11-26 14:50:38)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 */
	public void setSerialData(ArrayList alSN) {
		setM_alSerialData(alSN);
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-29 19:46:17) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param hinttext
	 *            nc.ui.pub.beans.UITextField
	 */
	public void setUITxtFldStatus(nc.ui.pub.beans.UITextField hinttext) {
		m_uitfHinttext = hinttext;
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-29 18:42:59) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param sMessage
	 *            java.lang.String
	 */
	public void showErrorMessage(String sMessage) {
		nc.ui.pub.beans.MessageDialog.showErrorDlg(
				this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
						"UPPSCMCommon-000059")/* @res "����" */, sMessage);
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-29 18:42:59) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param sMessage
	 *            java.lang.String
	 */
	public void showHintMessage(String sMessage) {
		if (m_bIsOutBill) {
			// nc.ui.pub.beans.MessageDialog.showHintDlg(this, "ע��", sMessage);
			getUITxtFldStatus().setText(sMessage);
		} else
			super.showHintMessage(sMessage);
	}

	/**
	 * @return the m_boQueryPrice
	 */
	private ButtonObject getBoQueryPrice() {
		return getButtonManager().getButton("ȡ��浥��");
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		if("pk_transport".equals(e.getKey())) {
			try {
				afterTransPrice(e);
			} catch (Exception e1) {
				Logger.error(e1.getMessage());
			}
		}
	}
	
	protected final void afterTransPrice(nc.ui.pub.bill.BillEditEvent e) throws Exception {
		
		getBillCardPanel().getHeadItem("vuserdef1").setValue(((UIRefPane)e.getSource()).getRefName());
		getBillCardPanel().getHeadItem("vuserdef2").setValue(((UIRefPane)e.getSource()).getRefPK());
		
	}
	
	@Override
	public void onButtonClicked(ButtonObject bo) {
		// ȡ��ǩ��ǰ����
		// add by river for 2012-09-18
		try {
			Boolean check = true;
			if("ȡ��ǩ��".equals(bo.getName())) 
				check = beforeOnCancleAudit(); 
		
			if(check)
				super.onButtonClicked(bo);
				
		} catch(Exception e) {
			Logger.error(e.getMessage());
		}
	}
	
	/**
	 *  ���� �� ������ⵥ - ȡ��ǩ��ǰ����
	 *  
	 *  @author river
	 *  
	 *  Create Date : 2012-09-18
	 *  
	 * @return 
	 * @throws Exception
	 */
	protected final Boolean beforeOnCancleAudit() throws Exception {
		
		List<GeneralBillVO> list = getSelectedBills();
		if(list == null || list.size() == 0)
			return true;

		for(GeneralBillVO billVO : list) {
			Object vuserdef3 = ((GeneralBillVO )list.get(0)).getParentVO().getAttributeValue("vuserdef3");
			
			if(vuserdef3 == null || "".equals(vuserdef3)) {
				vuserdef3 = UAPQueryBS.iUAPQueryBS.executeQuery("select decode(vuserdef3 , 'Y' , 'Y' , 'N' , 'N' , 'N') from ic_general_h where cgeneralhid = '"+((GeneralBillVO )list.get(0)).getPrimaryKey()+"'", new ColumnProcessor());
			}
			
			if("Y".equals(vuserdef3)) {
				
				showErrorMessage("ѡ�еļ�¼�д��������˷��Ѿ������˵ļ�¼�����ܽ���ȡ��ǩ�ֲ�����");
				return false;
			}
				
		
		}
		
		return true;
	}

}