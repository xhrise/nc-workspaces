package nc.ui.ic.ic211;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.pub.RefMsg;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.IButtonManager;
import nc.ui.ic.pub.bill.ICBcurrArithUI;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.pf.QryInBillDlg;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.qc.inter.CertService;
import nc.ui.scm.pub.redunmulti.ISourceRedunUI;
import nc.ui.scm.pub.redunmulti.PubRedunMultiDlg;
import nc.ui.scm.pub.redunmulti.PubTransBillPaneVO;
import nc.vo.bd.b15.GroupInventoryVO;
import nc.vo.ic.ic700.IntList;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.ICConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.check.CheckTools;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ProductCode;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.so.RushLinkQueryVO;

/**
 * ���۳��ⵥ �������ڣ�(2001-11-23 15:39:43)
 * 
 * @author������
 */
@SuppressWarnings({ "restriction", "serial", "rawtypes", "unchecked",
		"deprecation", "unused" })
public class ExtClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
	// �γɴ��ܶԻ���
	private FormMemoDlg ivjFormMemoDlg1 = null;

	private nc.ui.ic.auditdlg.ClientUIInAndOut m_dlgInOut = null;

	// �����תҵ������id
	private ArrayList m_alBrwLendBusitype = null;

	// ҵ������itemkey
	private final String m_sBusiTypeItemKey = "cbiztype";

	// �Ƿ����������������Ʒ
	private boolean m_isQCstartup = false;

	// �Ƿ��Ѿ�У��������Ʒ����
	private boolean m_isCheckQCstartup = false;

	private ICBcurrArithUI clsCurrArith;

	private IButtonManager m_buttonManager;

	/**
	 * ClientUI2 ������ע�⡣
	 */
	public ExtClientUI() {
		super();
		initialize();
		
	}

	/**
	 * ClientUI ������ע�⡣ add by liuzy 2007-12-18 ���ݽڵ�����ʼ������ģ��
	 */
	public ExtClientUI(FramePanel fp) {
		super(fp);
		initialize();
	}

	/**
	 * ClientUI ������ע�⡣ nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
	 */
	public ExtClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	@Override
	protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		// nc.vo.scm.pub.SCMEnv.out("haha,bill edit/.");
	}

	/**
	 * �����ߣ����˾� ���ܣ���������ѡ��ı� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	@Override
	protected void afterBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,sel chged!");

	}

	/**
	 * �����ߣ����˾� ���ܣ�����¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */

	public void afterInvEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.SCMEnv.out("inv chg");
		try {
			// �ֶ�itemkey
			String sItemKey = e.getKey();
			int row = e.getRow();

			// ����������������������,������β��ʾ
			if (e.getValue().toString().trim().length() == 0) {
				clearRowData(row);
				// ��Ӧ����
				// ͬ��vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
				// ��β
				setTailValue(null);
			} else {

				String sTempID1 = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getBodyItem(sItemKey).getComponent()).getRefPK();
				if (sTempID1 != null && sTempID1.trim().length() != 0) {
					String sTempID2 = null;
					if (getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE) != null)
						sTempID2 = getBillCardPanel().getHeadItem(
								IItemKey.WAREHOUSE).getValue();
					ArrayList alIDs = new ArrayList();
					alIDs.add(sTempID2);
					alIDs.add(sTempID1);
					alIDs.add(getEnvironment().getUserID());
					alIDs.add(getEnvironment().getCorpID());
					// ��ѯ�����Ϣ
					InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(
							new Integer(QryInfoConst.INV), alIDs);

					// invokeClient(
					// "queryInfo",
					// new Class[] { Integer.class, Object.class },
					// new Object[] { new Integer(QryInfoConst.INV), alIDs });
					if (getM_voBill() != null) {

						getM_voBill().setItemInv(row, voInv);
						getM_voBill().setItemValue(
								row,
								IItemKey.NAME_HEADID,
								getM_voBill().getHeaderValue(
										IItemKey.NAME_HEADID));
					}
					// ����
					setBodyInvValue(row, voInv);
					// ��β
					// setTailValue1(row);
					// ������/�����������
					clearRowData(0, row, sItemKey);
					//
					execEditFomulas(0, "cinventorycode");
					//
					// ���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
					// showHintMessage("����޸ģ�������ȷ�����Ρ�������");
				}
			}
			// by zhx 0311 not use
			// if (getSourBillTypeCode() != null
			// && !getSourBillTypeCode().startsWith("4")
			// && e.getValue().toString().trim().length() == 0) {
			// /* �����ô��û���滻�����򽫴���������������ûص��ݵĴ�����뵥Ԫ **/
			// getBillCardPanel().setBodyValueAt(
			// m_voBill.getItemValue(e.getRow(), "cinventorycode"),
			// e.getRow(),
			// "cinventorycode");
			// }

			// 2002-07-26 use it to clear should number when no source id
			if (getSourBillTypeCode() == null
					|| getSourBillTypeCode().trim().length() == 0) {
				// ��Ӧ����
				// ͬ��vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
			}

			// �������к��Ƿ����
			setBtnStatusSN(e.getRow(), true);
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ�����¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void afterInvEditforBarCode(String sItemKey, int row) {
		nc.vo.scm.pub.SCMEnv.out("inv chg");
		try {
			// �ֶ�itemkey
			// String sItemKey = e.getKey();
			// int row = e.getRow();

			// ����������������������,������β��ʾ

			String sTempID1 = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem(sItemKey).getComponent()).getRefPK();
			if (sTempID1 != null && sTempID1.trim().length() != 0) {
				String sTempID2 = null;
				if (getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE) != null)
					sTempID2 = getBillCardPanel().getHeadItem(
							IItemKey.WAREHOUSE).getValue();
				ArrayList alIDs = new ArrayList();
				alIDs.add(sTempID2);
				alIDs.add(sTempID1);
				alIDs.add(getEnvironment().getUserID());
				alIDs.add(getEnvironment().getCorpID());
				// ��ѯ�����Ϣ
				InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(new Integer(
						QryInfoConst.INV), alIDs);
				if (getM_voBill() != null)
					getM_voBill().setItemInv(row, voInv);
				// ����
				setBodyInvValue(row, voInv);
				// ��β
				// setTailValue1(row);
				// ������/�����������
				clearRowData(0, row, sItemKey);
				// ���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
				// showHintMessage("����޸ģ�������ȷ�����Ρ�������");
			}

			// by zhx 0311 not use
			// if (getSourBillTypeCode() != null
			// && !getSourBillTypeCode().startsWith("4")
			// && e.getValue().toString().trim().length() == 0) {
			// /* �����ô��û���滻�����򽫴���������������ûص��ݵĴ�����뵥Ԫ **/
			// getBillCardPanel().setBodyValueAt(
			// m_voBill.getItemValue(e.getRow(), "cinventorycode"),
			// e.getRow(),
			// "cinventorycode");
			// }

			// 2002-07-26 use it to clear should number when no source id
			if (getSourBillTypeCode() == null
					|| getSourBillTypeCode().trim().length() == 0) {
				// ��Ӧ����
				// ͬ��vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
			}

			// �������к��Ƿ����
			// setBtnStatusSN(e.getRow());
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}
	
	/**
	 * �����ߣ����˾� ���ܣ�����¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void afterInvEditforBarCode(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.SCMEnv.out("inv chg");
		try {
			// �ֶ�itemkey
			String sItemKey = e.getKey();
			int row = e.getRow();

			// ����������������������,������β��ʾ
			if (e.getValue().toString().trim().length() == 0) {
				clearRowData(row);
				// ��Ӧ����
				// ͬ��vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
				// ��β
				setTailValue(null);
			} else {

				String sTempID1 = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getBodyItem(sItemKey).getComponent()).getRefPK();
				if (sTempID1 != null && sTempID1.trim().length() != 0) {
					String sTempID2 = null;
					if (getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE) != null)
						sTempID2 = getBillCardPanel().getHeadItem(
								IItemKey.WAREHOUSE).getValue();
					ArrayList alIDs = new ArrayList();
					alIDs.add(sTempID2);
					alIDs.add(sTempID1);
					alIDs.add(getEnvironment().getUserID());
					alIDs.add(getEnvironment().getCorpID());
					// ��ѯ�����Ϣ
					InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(
							new Integer(QryInfoConst.INV), alIDs);
					if (getM_voBill() != null)
						getM_voBill().setItemInv(row, voInv);
					// ����
					setBodyInvValue(row, voInv);
					// ��β
					// setTailValue1(row);
					// ������/�����������
					clearRowData(0, row, sItemKey);
					// ���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
					// showHintMessage("����޸ģ�������ȷ�����Ρ�������");
				}
			}
			// by zhx 0311 not use
			// if (getSourBillTypeCode() != null
			// && !getSourBillTypeCode().startsWith("4")
			// && e.getValue().toString().trim().length() == 0) {
			// /* �����ô��û���滻�����򽫴���������������ûص��ݵĴ�����뵥Ԫ **/
			// getBillCardPanel().setBodyValueAt(
			// m_voBill.getItemValue(e.getRow(), "cinventorycode"),
			// e.getRow(),
			// "cinventorycode");
			// }

			// 2002-07-26 use it to clear should number when no source id
			if (getSourBillTypeCode() == null
					|| getSourBillTypeCode().trim().length() == 0) {
				// ��Ӧ����
				// ͬ��vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
			}

			// �������к��Ƿ����
			setBtnStatusSN(e.getRow(), true);
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
		return true;
	}

	/**
	 * �����ߣ����˾� ���ܣ���������ѡ��ı� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void beforeBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,before sel");

	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷�������ǰ��VO��� �����������浥�� ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
		try {
			VOCheck.checkOutRetVO(voBill);
		} catch (Exception e) {
			GenMethod.handleException(this, null, e);
			return false;
		}
		return this.checkVO();
	}

	/**
	 * ������ ���ܣ����ظ���ķ���.ִ�����⹫ʽ:������. ������ ���أ� ���⣺ ���ڣ�(2004-7-20 17:27:02)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param alListData
	 *            java.util.ArrayList
	 */
	protected void execExtendFormula(ArrayList alListData) {
		if (alListData == null || alListData.get(0) == null)
			return;
		int iLen = alListData.size();
		CircularlyAccessibleValueObject[] headVO = new CircularlyAccessibleValueObject[iLen];
		for (int i = 0; i < iLen; i++) {
			headVO[i] = ((AggregatedValueObject) alListData.get(i))
					.getParentVO();
		}

		nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(headVO,
				new String[] { "pk_cubasdoctran" }, "dm_trancust",
				"pk_trancust", new String[] { "pkcusmandoc" },
				"cwastewarehouseid");

		nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(headVO,
				new String[] { "vcustname" }, "bd_cubasdoc", "pk_cubasdoc",
				new String[] { "custname" }, "pk_cubasdoctran");
	}

	/**
	 * �����ߣ����Ӣ ���ܣ��õ���ǰ����vo,������λ/���кźͽ��������е�����,������ɾ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */

	public GeneralBillVO getBillVO() {
		GeneralBillVO billVO = super.getBillVO();
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null)
			billVO.setHeaderValue("vdiliveraddress",
					((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem("vdiliveraddress").getComponent())
							.getText());
		return billVO;

	}

	/**
	 * �����ߣ����˾� ���ܣ��õ���ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	// protected QueryConditionDlgForBill getConditionDlg() {
	// if (ivjQueryConditionDlg == null) {
	// ivjQueryConditionDlg = super.getConditionDlg();
	// ivjQueryConditionDlg.setCombox("freplenishflag", new String[][] {
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLNORMAL,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	// "UPP4008busi-000368")
	// /*
	// * @res "����"
	// */},
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLSENDBACK,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	// "UPT40080602-000014")
	// /*
	// * @res "�˿�"
	// */},
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLALL,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	// "UPPSCMCommon-000217")
	// /*
	// * @res "ȫ��"
	// */}
	// });
	// // zhy2005-04-23���۳��ⵥ��Ҫ���˿ͻ�Ȩ��
	// // �Ȼ����һ���ͻ�
	// ivjQueryConditionDlg.setCorpRefs("head.pk_corp", new String[] {
	// "head.ccustomerid"
	// });
	// }
	// return ivjQueryConditionDlg;
	// }

	/**
	 * ���� ReturnDlg1 ����ֵ��
	 * 
	 * @return nc.ui.ic.auditdlg.ClientUIInAndOut
	 */
	/* ���棺�˷������������ɡ� */
	protected nc.ui.ic.auditdlg.ClientUIInAndOut getDispenseDlg(String sTitle,
			ArrayList alInVO, ArrayList alOutVO) {
		if (m_dlgInOut == null) {
			try {
				// user code begin {1}
				m_dlgInOut = new ClientUIInAndOut(this, sTitle);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		// if (m_voBill == null)
		setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData().get(
				getM_iLastSelListHeadRow())).clone());
		m_dlgInOut.setVO(getM_voBill(), alInVO, alOutVO, getBillType(),
				getM_voBill().getPrimaryKey().trim(), getEnvironment()
						.getCorpID(), getEnvironment().getUserID());
		m_dlgInOut.setName("BillDlg");
		// m_dlgInOut.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		return m_dlgInOut;
	}

	/**
	 * ���� FormMemoDlg1 ����ֵ��
	 * 
	 * @return nc.ui.ic.ic211.FormMemoDlg
	 */
	/* ���棺�˷������������ɡ� */
	private FormMemoDlg getFormMemoDlg1() {
		if (ivjFormMemoDlg1 == null) {
			try {
				ivjFormMemoDlg1 = new nc.ui.ic.ic211.FormMemoDlg(this);
				ivjFormMemoDlg1.setName("FormMemoDlg1");
				// ivjFormMemoDlg1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				// user code begin {1}
				// ivjFormMemoDlg1.setParent(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormMemoDlg1;
	}

	/**
	 * ����˵���� �������ڣ�(2002-11-28 14:12:13) ���ߣ�����IC �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	public void initialize() {

		// �������ػ����initialize����
		super.initialize();

		try {
			// �ɿ�Ʊ����
			BillItem item = getBillCardPanel().getBodyItem("navlinvoicenum");
			if (item != null) {
				item.setShow(false);
				getBillCardPanel().getBodyPanel()
						.hideTableCol("navlinvoicenum");
			}
			getBillCardPanel()
					.getBodyPanel()
					.getTable()
					.setSelectionMode(
							javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			getBillListPanel()
					.getChildListPanel()
					.getTable()
					.setSelectionMode(
							javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e.getMessage());
		} finally {
			getBillCardPanel().addBillEditListenerHeadTail(this);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ���ʼ��ϵͳ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void initPanel() {
		// ��Ҫ���ݲ���
		super.setNeedBillRef(true);

		// ���γɴ��ܡ���ť
		getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG)
				.setEnabled(false);
		// �����ס���ť
		getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
				.setEnabled(true);
	}

	public String getBillType() {
		return nc.vo.ic.pub.BillTypeConst.m_saleOut;
	}

	public String getFunctionNode() {
		return "40080802";
	}

	public int getInOutFlag() {
		return InOutFlag.OUT;
	}

	/**
	 * �����ߣ����˾� ���ܣ��Ƿ��ת���� ��һ����Ҫ���⡣ ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean isBrwLendBiztype() {
		try {
			GeneralBillVO voMyBill = null;
			// ҵ������
			String sBusitypeid = null;
			if (getM_iCurPanel() == BillMode.List) { // �б���ʽ��
				if (getM_alListData() != null
						&& getM_iLastSelListHeadRow() >= 0
						&& getM_alListData().size() > getM_iLastSelListHeadRow()
						&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
					voMyBill = ((GeneralBillVO) getM_alListData().get(
							getM_iLastSelListHeadRow()));
					sBusitypeid = (String) voMyBill
							.getHeaderValue(m_sBusiTypeItemKey);
				}
			} else { // ��
				if (getBillCardPanel().getHeadItem(m_sBusiTypeItemKey) != null
						&& getBillCardPanel().getHeadItem(m_sBusiTypeItemKey)
								.getComponent() != null) {
					nc.ui.pub.beans.UIRefPane ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(m_sBusiTypeItemKey).getComponent());
					// ��pk
					sBusitypeid = ref.getRefPK();
				}
			}
			// ҵ�����Ͳ�Ϊ��ʱ
			// ��һ����Ҫ���⡣
			if (sBusitypeid != null && m_alBrwLendBusitype == null) {
				ArrayList alParam = new ArrayList();
				alParam.add(getEnvironment().getCorpID());
				m_alBrwLendBusitype = (ArrayList) GeneralBillHelper
						.queryInfo(new Integer(
								QryInfoConst.QRY_BRW_LEND_BIZTYPE), alParam);
				// ������ؿգ���ʼ��֮����־�Ѿ������ˣ���û�н�ת���Ͱ�!
				if (m_alBrwLendBusitype == null)
					m_alBrwLendBusitype = new ArrayList();
			}
			// �ǽ�ת���͵ģ����ء��ǡ�
			if (sBusitypeid != null && m_alBrwLendBusitype != null
					&& m_alBrwLendBusitype.contains(sBusitypeid))
				return true;
		} catch (Exception e) {
			SCMEnv.error(e);
		}
		return false;
	}

	/**
	 * �����ߣ����ݷ������� v55 add �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void onAdd4331() {
		try {

			PubTransBillPaneVO[] sourecVO = new PubTransBillPaneVO[1];
			sourecVO[0] = new PubTransBillPaneVO(
					"4331",
					"������",
					(ISourceRedunUI) Class
							.forName(
									"nc.ui.so.soreceive.redun.ReceiveTo4CRedunSourceCtrl")
							.newInstance(), null, "SO", null, null);

			PubTransBillPaneVO[] curVO = new PubTransBillPaneVO[1];
			curVO[0] = new PubTransBillPaneVO("4C", "���۳��ⵥ", null, null, "IC",
					null, null);

			HashMap<String, String> haparam = new HashMap<String, String>();
			haparam.put("cbiztype", getSelBusiType());

			PubRedunMultiDlg refdlg = new PubRedunMultiDlg(this, sourecVO,
					curVO, true, haparam);
			if (refdlg.showModal() != UIDialog.ID_OK)
				return;
			AggregatedValueObject[] sourcebillvos = refdlg.getRetBillVos();
			if (sourcebillvos == null || sourcebillvos.length <= 0)
				return;

			AggregatedValueObject[] vos = null;
			try {
				// sourcebillvos =
				// nc.vo.ic.pub.GenMethod.splitSourceVOs(sourcebillvos, "4331",
				// getBillType());
				vos = PfChangeBO_Client.pfChangeBillToBillArray(sourcebillvos,
						"4331", getBillType());
				vos = nc.vo.ic.pub.GenMethod.splitTargetVOs(vos, "4331",
						getBillType());
				if (vos != null && vos.length > 0)
					// ��鵥���Ƿ���Դ���µĲ��ս���
					if (!ICConst.IsFromNewRef.equals(vos[0].getParentVO()
							.getAttributeValue(ICConst.IsFromNewRef))) {
						// �����Ĭ�Ϸ�ʽ�ֵ�
						vos = nc.vo.ic.pub.GenMethod.splitGeneralBillVOs(
								(GeneralBillVO[]) vos, getBillType(),
								getBillListPanel().getHeadBillModel()
										.getFormulaParse());
						// ���������ݵĵ�λת��Ϊ���Ĭ�ϵ�λ.
						nc.vo.ic.pub.GenMethod.convertICAssistNumAtUI(
								(GeneralBillVO[]) vos, GenMethod.getIntance());
					}
			} catch (Exception e) {
				GenMethod.handleException(this, null, e);
				nc.vo.scm.pub.SCMEnv.out(e);
				return;
			}

			GeneralBillUICtl.procOrdEndAfterRefAdd(
					(GeneralBillItemVO[]) SmartVOUtilExt.getBodyVOs(vos),
					getBillCardPanel(), getBillType());

			// v5 lj ֧�ֶ��ŵ��ݲ�������
			if (vos != null && vos.length == 1) {
				setRefBillsFlag(false);
				setBillRefResultVO(((GeneralBillVO) vos[0]).getHeaderVO()
						.getCbiztypeid(), vos);
			} else {
				setRefBillsFlag(true);// �ǲ������ɶ���
				setBillRefMultiVOs(((GeneralBillVO) vos[0]).getHeaderVO()
						.getCbiztypeid(), (GeneralBillVO[]) vos);
			}
			GeneralBillUICtl.setItemEdit(getBillCardPanel(), BillItem.HEAD,
					IItemKey.freplenishflag, false);
			// end v5

		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000297")/* @res "��������:" */
					+ e.getMessage());
		}
	}

	protected class ButtonManager211 extends
			nc.ui.ic.pub.bill.GeneralButtonManager {

		public ButtonManager211(GeneralBillClientUI clientUI)
				throws BusinessException {
			super(clientUI);
		}

		/**
		 * ����ʵ�ָ÷�������Ӧ��ť�¼���
		 * 
		 * @version (00-6-1 10:32:59)
		 * @param bo
		 *            ButtonObject
		 */
		public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
			showHintMessage(bo.getName());
			if (bo.getTag() != null && bo.getTag().indexOf(":") > 0) {
				String billtype = bo.getTag().substring(0,
						bo.getTag().indexOf(":"));
				if (billtype.equals("4331")) {
					onAdd4331();
					return;
				}
			}
			if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_XCDG))
				// ����"�γɴ���"
				onFormMemo();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_DISPENSE))
				onDispense();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_PRINT_SUM_BATCH))
				onPrintLot();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_PRINT_CERTIFICATION))
				onPrintCert();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_ASSEMBLY))
				onAdd4L();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_REFER_IN))
				onRefInBill();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_RUSH_QUERY))
				onRushQuery();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_OUT_RETURN))
				onOutReturn();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_REFER_DIRECTIN))
				onRefDirectInBill();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_COOP_45))
				onCoop45Save();
			else {
				
				String boName = bo.getName();
				Boolean check = true;
				// �޸����ȡ��ǩ�ְ�ť��BEFORE����
				// modify by river for 2012-09-19
				try {
					if("ȡ��ǩ��".equals(bo.getName())) {
						check = beforeOnCancleSign();
						
					}
				} catch(Exception e ) {
					Logger.error(e);
				}
				
				if(check)
					super.onButtonClicked(bo);
				
				if("��Ƭ��ʾ".equals(boName) || "��ҳ".equals(boName) || "��ҳ".equals(boName) || "��ҳ".equals(boName) || "ĩҳ".equals(boName) || "ȡ��".equals(boName)) {
					getBillCardPanel().execHeadFormulas(new String[]{
							"contracttype->getColValue(ic_general_h , contracttype , cgeneralhid , cgeneralhid)",
							"storprice->getColValue(ic_general_h , pk_storcontract_b , cgeneralhid , cgeneralhid)",
							"transprice->getColValue(ic_general_h , pk_transport_b , cgeneralhid , cgeneralhid)",
							
						});
					
				}
					
			}

		}
		
		/**
		 * ���� �� ȡ��ǩ��ǰ����
		 * 
		 * @author river
		 * 
		 * Create Date : 2012-09-19
		 * 
		 */
		protected final Boolean beforeOnCancleSign() throws Exception {
			
			GeneralBillVO billVO = getCurVO();
			Boolean check = true;
			Map retMap = (Map) UAPQueryBS.getInstance().executeQuery("select vuserdef3 , vuserdef4 from ic_general_h where cgeneralhid = '"+billVO.getPrimaryKey()+"'", new MapProcessor());
			
			if(retMap != null) {
				if("Y".equals(retMap.get("vuserdef3"))) {
					showErrorMessage("�����˷��Ѿ����㣬���ܽ������������");
					check = false;
				}
				
				if("Y".equals(retMap.get("vuserdef4"))) {
					showErrorMessage("�ִ��Ѽ�װж���Ѿ����㣬���ܽ������������");
					check = false;
				}
					
			}
			
			return check;
			
		}

		/**
		 * �����ߣ����˾� ���ܣ��������� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 */
		public void onAdd() {
			super.onAdd();
			// ȡ�÷�����ʱ���
			// try {
			// m_dTime = RecordTimeHelper.getTimeStamp();
			// }
			// catch (Exception e) {
			// SCMEnv.error(e);
			// }
		}

		/**
		 * �����ߣ����˾� ���ܣ���ѯ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * ���� 2003-06-24 ע�ͣ� �����Ѿ���
		 * nc.ui.ic.pub.bill.GeneralBillClientUI.setListHeadData
		 * (GeneralBillHeaderVO
		 * nc.ui.ic.pub.bill.GeneralBillClientUI.setAlistDataByFormula(int,
		 * ArrayList) ִ���˱�ͷ�����幫ʽ�������в����ڴ������ظ�ִ�����´��룺
		 * getBillListPanel().getHeadBillModel().execLoadFormula();
		 * getBillListPanel().getBodyBillModel().execLoadFormula();
		 * 
		 */
		public void onQuery() {
			try {
				nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
				boolean isFrash = !(getClientUI().isBQuery() || !getClientUI()
						.getQryDlgHelp().isBEverQry());
				int cardOrList = getM_iCurPanel();
				Object[] ret = getQryDlgHelp().queryData(isFrash);
				if (ret == null || ret[0] == null
						|| !((UFBoolean) ret[0]).booleanValue())
					return;
				ArrayList<GeneralBillVO> alListData = (ArrayList<GeneralBillVO>) ret[1];
				if (!isFrash) {
					// ˢ�°�ť ��������ѯ����
					setButtonStatus(true);
				}

				// m_sBnoutnumnull = null;
				//
				// timer.start(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
				// "UPP4008busi-000277")/* @res "@@��ѯ��ʼ��" */);
				//
				// // DUYONG ���ӡ�ˢ�¡��Ĺ���
				// // ��ԭ�������ͣ���Ƭ/�б���¼����������ڿ�Ƭ����ִ����ˢ�£��������л����б����
				// int cardOrList = getM_iCurPanel();
				// // �����[(1)���в�ѯ(2)����û�н��в�ѯ�����ǵ����ˢ�°�ť]������ʾ��ѯģ����в�ѯ
				// // ���������ѯ�������ҵ����ˢ�°�ť���������˶δ���
				// QryConditionVO voCond = null;
				// //�޸��ˣ������� �޸����ڣ�2007-9-24����09:52:28 �޸�ԭ��ˢ��ʱ���ô洢�Ĳ�ѯVO
				// if (m_bQuery || !m_bEverQry) {
				// getConditionDlg().showModal();
				// timer.showExecuteTime("@@getConditionDlg().showModal()��");/*-=notranslate=-*/
				//
				// if (getConditionDlg().getResult() !=
				// nc.ui.pub.beans.UIDialog.ID_OK)
				// // ȡ������
				// return;
				//
				// // ����ǽ��в�ѯ���򽫡�������ѯ�����ı�ʶ���ó�true�����������ܹ����С�ˢ�¡��Ĳ�����
				// m_bEverQry = true;
				// // ˢ�°�ť
				// setButtonStatus(true);
				//
				// // ���qrycontionVO�Ĺ���
				// voCond = getQryConditionVO();
				//
				// // ��¼��ѯ��������
				// m_voLastQryCond = voCond;
				// }else
				// voCond = m_voLastQryCond;
				//
				// //addied by liuzy 2008-03-31 �����������ڡ��ӡ����������ֶ���
				// String str_where = voCond.getQryCond();
				// if(null != str_where && !"".equals(str_where)){
				// if(str_where.indexOf("head.dbilldate.from") > -1)
				// str_where = str_where.replace("head.dbilldate.from",
				// "head.dbilldate");
				// if(str_where.indexOf("head.dbilldate.end") > -1)
				// str_where = str_where.replace("head.dbilldate.end",
				// "head.dbilldate");
				// voCond.setQryCond(str_where);
				// }
				//
				// // ���ʹ�ù�ʽ����봫voaCond ����̨. �޸� zhangxing 2003-03-05
				// nc.vo.pub.query.ConditionVO[] voaCond = getConditionDlg()
				// .getConditionVO();
				// // /����voaCond:���˿��־��ֵ��"ȫ��"ʱ,��Ҫ�滻��.
				//
				// // voaCond = dealCondVO(voaCond); //�����Ѿ�����
				//
				// voCond.setParam(QryConditionVO.QRY_CONDITIONVO, voaCond);
				//
				// voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY_PURE);
				// if (m_sBnoutnumnull != null) {
				// // �Ƿ����ʵ������
				// voCond.setParam(33, m_sBnoutnumnull);
				// }
				//
				// voCond.setParam(QryConditionVO.QRY_LOGCORPID,
				// getEnvironment().getCorpID());
				// voCond.setParam(QryConditionVO.QRY_LOGUSERID,
				// getEnvironment().getUserID());
				//
				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
				// "UPP4008busi-000250")/* @res "���ڲ�ѯ�����Ժ�..." */);
				// timer.showExecuteTime("Before ��ѯ����");/*-=notranslate=-*/
				// ArrayList alListData = (ArrayList)
				// GeneralBillHelper.queryBills(
				// getBillType(), voCond);

				timer.showExecuteTime("��ѯʱ�䣺");/* -=notranslate=- */

				// ��ʽ��� ��һ�������¼��ʽ��ѯ�������� �޸� hanwei 2003-03-05
				try {

					setAlistDataByFormula(GeneralBillVO.QRY_FIRST_ITEM_NUM,
							alListData);
					timer.showExecuteTime("@@setAlistDataByFormula��ʽ����ʱ�䣺");/*
																			 * -=
																			 * notranslate
																			 * =
																			 * -
																			 */
					nc.vo.scm.pub.SCMEnv.out("0�����ʽ�����ɹ���");

				} catch (Exception e) {
				}
				execExtendFormula(alListData);
				if (alListData != null && alListData.size() > 0) {
					
					alListData = setContractManager(alListData);
					
					setScaleOfListData(alListData);
					setM_alListData(alListData);
					setListHeadData();
					// ��ǰ�Ǳ���ʽʱ�������л����б���ʽ
					if (BillMode.Card == getM_iCurPanel())
						onSwitch();

					// ȱʡ��ͷָ���һ�ŵ���
					selectListBill(0);
					// ���õ�ǰ�ĵ�������/��ţ����ڰ�ť����
					setM_iLastSelListHeadRow(0);
					// ��ʼ����ǰ������ţ��л�ʱ�õ������������������ñ������ݡ�
					m_iCurDispBillNum = -1;
					// ��ǰ������
					m_iBillQty = getM_alListData().size();

					if (m_iBillQty > 0)
						showHintMessage(nc.ui.ml.NCLangRes
								.getInstance()
								.getStrByID(
										"4008busi",
										"UPP4008busi-000290",
										null,
										new String[] { (new Integer(m_iBillQty))
												.toString() })/* @res "���鵽{0}�ŵ��ݣ�" */);
					else
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008busi", "UPP4008busi-000243")/*
																			 * @res
																			 * "δ�鵽���������ĵ��ݡ�"
																			 */);
					// ��������Դ���ݵİ�ť���˵��ȵ�״̬��
					ctrlSourceBillUI(false);
					timer.showExecuteTime("@@��������ʾ��ҳ��ʱ�䣺");/* -=notranslate=- */

				} else {
					dealNoData();
				}

				setButtonStatus(true);

				// DUYONG ��ִ��ˢ�²��������ҵ�ǰ����Ϊ��Ƭ����ʱ����Ӧ���л����б����͵Ľ�����
				if (!m_bQuery && getM_iCurPanel() != cardOrList) {
					onSwitch();
				}
			} catch (Exception e) {
				handleException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000251")/* @res "��ѯ����" */);
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000252")/* @res "��ѯ����" */
						+ e.getMessage());
			}
			
			getBillCardPanel().execHeadFormulas(new String[]{
				"contracttype->getColValue(ic_general_h , contracttype , cgeneralhid , cgeneralhid)",
				"storprice->getColValue(ic_general_h , pk_storcontract_b , cgeneralhid , cgeneralhid)",
				"transprice->getColValue(ic_general_h , pk_transport_b , cgeneralhid , cgeneralhid)",
				
			});
		}
		
		/**
		 * ���� : ����PTA���۶������չ�������غ�ͬ�ֶε�ֵ
		 * 
		 * Author �� river
		 * 
		 * Create �� 2012-09-13
		 * 
		 * @param alListData
		 * @throws Exception
		 */
		protected final ArrayList<GeneralBillVO> setContractManager(ArrayList<GeneralBillVO> alListData) throws Exception {
			
			List<String> pkList = new ArrayList<String>();
			for(GeneralBillVO alData : alListData) {
				pkList.add("'" + alData.getPrimaryKey() + "'");
			}
			
			String[] fieldArr = new String[]{
					"cgeneralhid" ,
					"concode" ,
					"pk_transport" ,
					"transprice" ,
					"storprice" ,
					"contracttype" ,
					"salecode" ,
					"pk_storcontract_b" ,
					"pk_transport_b" ,
					"pk_contract" ,
			};
			
			String sqlField = ConvertFunc.change(fieldArr);
			
			List<Map> retList = (List<Map>) UAPQueryBS.getInstance().executeQuery("select "+sqlField+" from ic_general_h where cgeneralhid in ("+ConvertFunc.change(pkList.toArray(new String[0]))+")", new MapListProcessor());
			
			if(retList != null && retList.size() > 0) {
				for(GeneralBillVO alData : alListData) 
					for(Map map : retList) 
						if(alData.getPrimaryKey().equals(map.get("cgeneralhid"))) 
							for(String filed : fieldArr)
								alData.getParentVO().setAttributeValue(filed, map.get(filed));
			}
			
			
			return alListData;
			
		}

		/**
		 * �����ߣ����˾� ���ܣ����棬����ǽ�ת���͵ģ����λ�����кš� ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 */
		public boolean onSave() {
			if (isBrwLendBiztype()) {
				// ����ǰ���λ����
				m_alLocatorDataBackup = getM_alLocatorData();
				setM_alLocatorData(null);
				// ����ǰ�����к�����
				m_alSerialDataBackup = getM_alSerialData();
				setM_alSerialData(null);
			}
			return super.onSave();
		}

		/**
		 * �����ߣ����˾� ���ܣ����кŷ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 */
		public void onSNAssign() {
			// �����ģʽ�£�����ǽ�ת���͵ģ�����Ҫ�˲�����
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
														 * @res
														 * "����ҵ�����ͣ�����Ҫִ�д˲������������״̬�²鿴��"
														 */);
				return;
			} else {
				if (isBrwLendBiztype()) {
					GeneralBillVO voMyBill = null;
					if (getM_alListData() != null
							&& getM_iLastSelListHeadRow() >= 0
							&& getM_alListData().size() > getM_iLastSelListHeadRow()
							&& getM_alListData()
									.get(getM_iLastSelListHeadRow()) != null) {
						voMyBill = ((GeneralBillVO) getM_alListData().get(
								getM_iLastSelListHeadRow()));
						String sBillPK = (String) voMyBill.getItemValue(0,
								"cfirstbillhid");
						if (sBillPK == null || sBillPK.trim().length() == 0) {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000274")/*
																 * @res
																 * "û�ж�Ӧ�Ľ���/�������޷���ѯ����λ�����к����ݡ����鵥����Դ��"
																 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000275")/*
																 * @res
																 * "��쿴��ؽ���/����������ݡ�"
																 */);
					}
					return;
				}
			}
			super.onSNAssign();
		}

		/**
		 * �����ߣ����˾� ���ܣ���λ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 */
		public void onSpaceAssign() {
			// �����ģʽ�£�����ǽ�ת���͵ģ�����Ҫ�˲�����
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
														 * @res
														 * "����ҵ�����ͣ�����Ҫִ�д˲������������״̬�²鿴��"
														 */);
				return;
			} else {
				if (isBrwLendBiztype()) {
					GeneralBillVO voMyBill = null;
					if (getM_alListData() != null
							&& getM_iLastSelListHeadRow() >= 0
							&& getM_alListData().size() > getM_iLastSelListHeadRow()
							&& getM_alListData()
									.get(getM_iLastSelListHeadRow()) != null) {
						voMyBill = ((GeneralBillVO) getM_alListData().get(
								getM_iLastSelListHeadRow()));
						String sBillPK = (String) voMyBill.getItemValue(0,
								"cfirstbillhid");
						if (sBillPK == null || sBillPK.trim().length() == 0) {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000274")/*
																 * @res
																 * "û�ж�Ӧ�Ľ���/�������޷���ѯ����λ�����к����ݡ����鵥����Դ��"
																 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000275")/*
																 * @res
																 * "��쿴��ؽ���/����������ݡ�"
																 */);
					}
					return;
				}
			}
			super.onSpaceAssign();
		}

		/**
		 * �˴����뷽��˵���� �������ڣ�(2001-9-10 11:34:31)
		 */
		private void onFormMemo() {
			if (getM_iLastSelListHeadRow() >= 0
					&& getM_alListData() != null
					&& getM_alListData().size() > getM_iLastSelListHeadRow()
					&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
				if (((GeneralBillVO) getM_alListData().get(
						getM_iLastSelListHeadRow())).getChildrenVO().length == 0) {

					return;
				}

				GeneralBillVO voBill = (GeneralBillVO) ((GeneralBillVO) getM_alListData()
						.get(getM_iLastSelListHeadRow())).clone();
				/** ����ǰ�Ĳ���ԱID����voBill, �����γɴ��ܶԻ��� */
				voBill.setHeaderValue("coperatorid", getEnvironment()
						.getUserID());
				getFormMemoDlg1().setBillVO(voBill);
				getFormMemoDlg1().showModal();
			}
		}

		/**
		 * ���׹��ܰ�ť������ ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 */
		private void onDispense() {
			if (BillMode.Browse == getM_iMode() && isSigned() != SIGNED
			// && getSourBillTypeCode() != null
			// && !getSourBillTypeCode().startsWith("4")
			) {

			} else
				return;

			if (getBillCardPanel().getBillTable().getSelectedRows().length >= 1) {

				if (nc.ui.pub.beans.UIDialog.ID_CANCEL == nc.ui.pub.beans.MessageDialog
						.showOkCancelDlg(
								getClientUI(),
								null,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008busi", "UPP4008busi-000268")/*
																		 * @res
																		 * "���Ա����ݵ����г��׼����Զ����״�����������������ⵥ��?"
																		 */)) {
					return;

				}

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "4008busi", "UPT40080602-000007")/* @res "����" */);

				GeneralBillVO voBill = (GeneralBillVO) ((GeneralBillVO) getM_alListData()
						.get(getM_iLastSelListHeadRow())).clone();

				try {
					// ������˻ء��˻����������������Ƿ��и������еĻ�����
					VOCheck.checkOutRetAssemblyVO(voBill);
				} catch (Exception e) {
					// ��־�쳣
					nc.vo.scm.pub.SCMEnv.out(e);
					showHintMessage(e.getMessage());
					return;
				}

				GeneralBillVO voBillclone = (GeneralBillVO) voBill.clone();

				ArrayList alOutGeneralVO = new ArrayList();
				ArrayList alInGeneralVO = new ArrayList();

				ArrayList aloutitem = new ArrayList();
				ArrayList alinitem = new ArrayList();
				int[] rownums = getBillCardPanel().getBillTable()
						.getSelectedRows();

				for (int i = 0; i < rownums.length; i++) {

					if (!isSetInv(voBill, rownums[i])
							|| isDispensedBill(voBill, rownums[i]))
						continue;

					// ������
					GeneralBillItemVO voParts = voBill.getItemVOs()[rownums[i]];// searchInvKit(voBill.getItemVOs()[rownums[i]]);
					UFDouble ufSetNum = null;

					ufSetNum = voParts.getNoutnum();
					voParts.setAttributeValue("nshouldinnum",
							voParts.getNoutnum());
					voParts.setAttributeValue("nneedinassistnum",
							voParts.getNoutassistnum());
					voParts.setAttributeValue("ninnum", voParts.getNoutnum());
					voParts.setAttributeValue("ninassistnum",
							voParts.getNoutassistnum());
					// after set null to noutnum and noutassistnum zhx 030616
					voParts.setAttributeValue("noutnum", null);
					voParts.setAttributeValue("noutassistnum", null);
					voParts.setAttributeValue("nshouldoutnum", null);
					voParts.setAttributeValue("nshouldoutassistnum", null);
					// soucebill
					voParts.setAttributeValue("csourcetype", voBill
							.getHeaderVO().getCbilltypecode());
					voParts.setAttributeValue("csourcebillhid", voBill
							.getHeaderVO().getPrimaryKey());
					voParts.setAttributeValue("csourcebillbid",
							voBill.getItemVOs()[rownums[i]].getPrimaryKey());
					voParts.setAttributeValue("vsourcebillcode", voBill
							.getHeaderVO().getVbillcode());
					voParts.setCgeneralbid(null);
					voParts.setCgeneralbb3(null);
					voParts.setCsourceheadts(null);
					voParts.setCsourcebodyts(null);
					voParts.setDbizdate(new nc.vo.pub.lang.UFDate(
							getEnvironment().getLogDate()));

					alinitem.add(voParts);
					// alOutGeneralVO.add(gbvoOUT);

					// ���������VO��Ӧ�ǲɹ���ⵥ�ݵ���������

					voParts.setLocator(null);// zhy
					GeneralBillItemVO[] tempItemVO = splitInvKit(voParts,
							voBillclone.getHeaderVO(), ufSetNum);
					if (tempItemVO != null && tempItemVO.length > 0) {
						for (int j = 0; j < tempItemVO.length; j++) {
							aloutitem.add(tempItemVO[j]);

						}

					} else {
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008busi", "UPP4008busi-000270")/*
																			 * @res
																			 * "�������׼�û�ж���������붨��������ٽ������ף�"
																			 */);
						return;
					}
				}

				if (aloutitem.size() == 0 || alinitem.size() == 0)

					return;

				GeneralBillVO gbvoIn = new GeneralBillVO();
				voBill.getHeaderVO().setCoperatorid(
						getEnvironment().getUserID());
				voBill.getHeaderVO()
						.setDbilldate(
								new nc.vo.pub.lang.UFDate(getEnvironment()
										.getLogDate()));
				gbvoIn.setParentVO(voBill.getParentVO());
				gbvoIn.getHeaderVO().setPrimaryKey(null);
				gbvoIn.getHeaderVO().setVbillcode(null);
				gbvoIn.getHeaderVO().setCbilltypecode(
						nc.vo.ic.pub.BillTypeConst.m_otherIn);
				gbvoIn.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
				gbvoIn.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

				GeneralBillItemVO[] inbodys = new GeneralBillItemVO[alinitem
						.size()];
				alinitem.toArray(inbodys);
				gbvoIn.setChildrenVO(inbodys);
				alInGeneralVO.add(gbvoIn);

				// �ó���VO

				GeneralBillVO gbvoOut = new GeneralBillVO();
				gbvoOut.setParentVO(voBillclone.getParentVO());
				gbvoOut.getHeaderVO().setPrimaryKey(null);
				gbvoOut.getHeaderVO().setVbillcode(null);
				gbvoOut.getHeaderVO().setCbilltypecode(
						nc.vo.ic.pub.BillTypeConst.m_otherOut);
				gbvoOut.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
				gbvoOut.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

				GeneralBillItemVO[] outbodys = new GeneralBillItemVO[aloutitem
						.size()];
				aloutitem.toArray(outbodys);

				gbvoOut.setChildrenVO(outbodys);

				// ���۳������ɵ�������ⵥ��Ҫ���õ��ݺ�
				nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(gbvoOut,
						nc.vo.ic.pub.BillTypeConst.m_otherOut, "crowno");

				alOutGeneralVO.add(gbvoOut);

				getDispenseDlg(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000269")/*
													 * @res "�������ɣ�������/�������ⵥ"
													 */, alInGeneralVO,
						alOutGeneralVO).showModal();
				if (m_dlgInOut.isOK()) {
					try { // ���±�β
						// setAuditBillFlag();
						filterNullLine();

						setDispenseFlag(
								(GeneralBillVO) ((GeneralBillVO) getM_alListData()
										.get(getM_iLastSelListHeadRow())),
								rownums);
						setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData()
								.get(getM_iLastSelListHeadRow())).clone());
						super.setButtonStatus(false);
						// ���׳ɹ�����Ҫ���õ��ݵİ�ť��ɾ�����޸ģ����ư�ť�����ã���

						// setBillState();
						// can not dispense the inv more over, after create
						// the other in and out bill!
						// getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);
						ctrlSourceBillButtons(true);

					} catch (Exception e) {
						handleException(e);
						nc.ui.pub.beans.MessageDialog.showErrorDlg(
								getClientUI(), null, e.getMessage());
					}
				}
			}
		}

		/**
		 * �����ߣ����� ���ܣ����λ��ܴ�ӡ�������ṩ�����ķ��� ������ ���أ� ���⣺ ���ڣ�(2003-12-18 ���� 4:16)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2005-02-18 л���ϡ����˾��������֮��
		 */
		public void onPrintLot() {

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000248")/* @res "���ڴ�ӡ�����Ժ�..." */);
			nc.vo.scm.pub.SCMEnv.out("��ӡ���λ��ܿ�ʼ!\n");
			try {
				// ������ӡ����
				// ����ǰ���б��Ǳ�������ӡ����
				if (getM_iMode() == BillMode.Browse
						&& getM_iCurPanel() == BillMode.Card) { // ���
					nc.vo.scm.pub.SCMEnv.out("��ӡ���λ��ܿ�ʼ!����ӡ!\n");
					// ׼������
					GeneralBillVO vo = null;

					if (getM_iLastSelListHeadRow() != -1
							&& null != getM_alListData()
							&& getM_alListData().size() != 0) {
						vo = (GeneralBillVO) getM_alListData().get(
								getM_iLastSelListHeadRow());
						if (getBillCardPanel().getHeadItem("vcustname") != null)
							vo.setHeaderValue("vcustname", getBillCardPanel()
									.getHeadItem("vcustname").getValue());
					}

					if (null == vo) {
						vo = new GeneralBillVO();
					}
					if (null == vo.getParentVO()) {
						vo.setParentVO(new GeneralBillHeaderVO());
					}
					if ((null == vo.getChildrenVO())
							|| (vo.getChildrenVO().length == 0)
							|| (vo.getChildrenVO()[0] == null)) {
						GeneralBillItemVO[] ivo = new GeneralBillItemVO[1];
						ivo[0] = new GeneralBillItemVO();
						vo.setChildrenVO(ivo);
					}

					if (getPrintEntry().selectTemplate() < 0)
						return;
					GeneralBillVO gvobak = (GeneralBillVO) vo.clone();
					// /
					nc.vo.scm.merge.DefaultVOMerger dvomerger = new nc.vo.scm.merge.DefaultVOMerger();
					dvomerger.setGroupingAttr(new String[] { "cinventoryid",
							"castunitid" });
					dvomerger.setSummingAttr(new String[] { "nshouldoutnum",
							"nshouldoutassistnum", "noutnum", "noutassistnum",
							"nmny" });
					nc.vo.ic.pub.bill.GeneralBillItemVO[] itemvosnew = (nc.vo.ic.pub.bill.GeneralBillItemVO[]) dvomerger
							.mergeByGroup(gvobak.getItemVOs());
					if (itemvosnew != null) {
						UFDouble udNum = null;
						UFDouble udMny = null;
						for (int k = 0; k < itemvosnew.length; k++) {
							udNum = itemvosnew[k].getNoutnum();
							udMny = itemvosnew[k].getNmny();
							if (udNum != null && udMny != null) {
								itemvosnew[k].setNprice(udMny.div(udNum));
							}
							nc.vo.scm.pub.SCMEnv.out("cinventoryid:"
									+ itemvosnew[k].getCinventoryid() + "\n");
							nc.vo.scm.pub.SCMEnv.out("castunitid:"
									+ itemvosnew[k].getCastunitid() + "\n");
							nc.vo.scm.pub.SCMEnv.out("Vbatchcode:"
									+ itemvosnew[k].getVbatchcode() + "\n");
							nc.vo.scm.pub.SCMEnv.out("noutnum:" + udNum + "\n");

						}

					}
					gvobak.setChildrenVO(itemvosnew);

					// /
					getDataSource().setVO(gvobak);

					// ���ӡ��������Դ�����д�ӡ
					getPrintEntry().setDataSource(getDataSource());
					nc.vo.scm.pub.SCMEnv.out("��ӡ���λ��ܿ�ʼ!����ӡ����!\n");
					getPrintEntry().preview();

				} else if (getM_iCurPanel() == BillMode.List) {
					// �б�

					nc.vo.scm.pub.SCMEnv.out("�б��ӡ��ʼ!\n");
					if (null == getM_alListData()
							|| getM_alListData().size() == 0) {
						return;
					}
					if (getPrintEntry().selectTemplate() < 0)
						return;
					ArrayList alBill = getSelectedBills();
					// ��С������
					setScaleOfListData(alBill);
					nc.vo.scm.pub.SCMEnv.out("�б��ӡ:�õ�ѡ�еĵ��ݲ�������������!\n");
					if (alBill == null)
						return;
					nc.vo.scm.merge.DefaultVOMerger dvomerger = null;
					for (int i = 0; i < alBill.size(); i++) {
						nc.vo.scm.pub.SCMEnv.out("�б��ӡ:��ʼ�ϲ�������!\n");
						GeneralBillVO gvobak = (GeneralBillVO) alBill.get(i);
						// /
						dvomerger = new nc.vo.scm.merge.DefaultVOMerger();
						dvomerger.setGroupingAttr(new String[] {
								"cinventoryid", "castunitid" });
						dvomerger.setSummingAttr(new String[] {
								"nshouldoutnum", "nshouldoutassistnum",
								"noutnum", "noutassistnum", "nmny" });
						nc.vo.ic.pub.bill.GeneralBillItemVO[] itemvosnew = (nc.vo.ic.pub.bill.GeneralBillItemVO[]) dvomerger
								.mergeByGroup(gvobak.getItemVOs());
						nc.vo.scm.pub.SCMEnv.out("�б��ӡ:�õ��ϲ���ı�����!\n");
						if (itemvosnew != null) {
							UFDouble udNum = null;
							UFDouble udMny = null;
							for (int k = 0; k < itemvosnew.length; k++) {
								udNum = itemvosnew[k].getNoutnum();
								udMny = itemvosnew[k].getNmny();
								if (udNum != null && udMny != null) {
									itemvosnew[k].setNprice(udMny.div(udNum));
								}
								nc.vo.scm.pub.SCMEnv.out("cinventoryid:"
										+ itemvosnew[k].getCinventoryid()
										+ "\n");
								nc.vo.scm.pub.SCMEnv.out("castunitid:"
										+ itemvosnew[k].getCastunitid() + "\n");
								nc.vo.scm.pub.SCMEnv.out("Vbatchcode:"
										+ itemvosnew[k].getVbatchcode() + "\n");
								nc.vo.scm.pub.SCMEnv.out("noutnum:" + udNum
										+ "\n");

							}
							gvobak.setChildrenVO(itemvosnew);
							alBill.set(i, gvobak);
						}

					}
					//
					nc.vo.scm.pub.SCMEnv.out("�б��ӡ:�õ��ϲ���ĵ���!\n");
					getDataSource().setListVOs(alBill);
					getDataSource().setTotalLinesInOnePage(
							getPrintEntry().getBreakPos());
					// ���ӡ��������Դ�����д�ӡ
					getPrintEntry().setDataSource(getDataSource());
					getPrintEntry().preview();

				} else
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008busi", "UPP4008busi-000249")/*
																		 * @res
																		 * "ֻ�������״̬�´�ӡ"
																		 */);
			} catch (Exception e) {
				SCMEnv.error(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPPSCMCommon-000061")/* @res "��ӡ����" */
						+ e.getMessage());
			}
		}

		/**
		 * ��ӡ��֤��
		 */
		private void onPrintCert() {
			Object obj = null;
			try {
				if (!m_isCheckQCstartup) {
					m_isQCstartup = nc.ui.ic.pub.tools.GenMethod
							.isProductEnabled(getEnvironment().getCorpID(),
									nc.vo.pub.ProductCode.PROD_QC);
					m_isCheckQCstartup = true;
				}
				if (!m_isQCstartup) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008other", "UPP4008other-000492")/*
																			 * @res
																			 * "��������û�����ã�"
																			 */);
					return;
				}
				// obj = InterServUI.getInterInstance(ProductCode.PROD_QC,
				// InterRegister.QC0002);
				// obj =
				// (CertService)NCLocator.getInstance().lookup(CertService.class.getName());

				// obj = (ICertInter)
				// NewObjectService.newInstance("qc",
				// "nc.ui.qc.inter.CertService.printCert");
				Class cl = Class.forName("nc.ui.qc.inter.CertService");
				obj = cl.newInstance();

			} catch (Exception e) {
				SCMEnv.error(e);
			}
			if (obj == null)
				return;
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
					.getInstance();
			ClientLink client = new ClientLink(ce);
			GeneralBillVO voBill = null;
			if (getM_voBill() != null && getM_iCurPanel() == BillMode.Card) {
				// �����״̬�´�ӡ��ǰ���ݵ���֤��
				voBill = (GeneralBillVO) getM_voBill().clone();
				// ((IQcToIc_CertService) obj).printCert(this, voBill, client);
				((CertService) obj).printCert(getClientUI(), voBill, client);

			} else if (getM_iLastSelListHeadRow() != -1
					&& null != getM_alListData()
					&& getM_alListData().size() != 0) {
				voBill = (GeneralBillVO) getM_alListData().get(
						getM_iLastSelListHeadRow());
				// ((IQcToIc_CertService) obj).printCert(this, voBill, client);
				((CertService) obj).printCert(getClientUI(), voBill, client);
			}
		}

		/**
		 * ����������������װ
		 * <p>
		 * <b>examples:</b>
		 * <p>
		 * ʹ��ʾ��
		 * <p>
		 * <b>����˵��</b>
		 * <p>
		 * 
		 * @author liuzy
		 * @time 2007-7-5 ����01:25:44
		 */
		private void onAdd4L() {
			showHintMessage("");
			GeneralBillVO billVO4C = null;
			GeneralBillItemVO item4C = null;
			String sInvbasID = null;
			UFBoolean sSetpartsflag = null;
			try {
				if (getM_iCurPanel() == BillMode.List) {
					// �б����
					int seleHeadRow = getBillListPanel().getHeadTable()
							.getSelectedRow();
					if (seleHeadRow < 0) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40080802", "UPT40080802-000310")/*
																			 * @res
																			 * "����ѡ��һ�ŵ��ݺ�Ҫ������װ���ĳ��׼�"
																			 */);
						return;
					}
					/*
					 * billVO4C = (GeneralBillVO)
					 * getBillListPanel().getSelectedVO(
					 * nc.vo.ic.pub.bill.GeneralBillVO.class.getName(),
					 * nc.vo.ic.pub.bill.GeneralBillHeaderVO.class.getName(),
					 * nc.vo.ic.pub.bill.GeneralBillItemVO.class.getName());
					 */

					billVO4C = (GeneralBillVO) getM_alListData().get(
							getM_iLastSelListHeadRow());

					// ����ѡ�������
					int seleBodyRowCount = getBillListPanel().getBodyTable()
							.getSelectedRowCount();
					// ���ǵ����򷵻�
					if (seleBodyRowCount != 1)
						return;
					// �����ѡ�к�
					int seleBodyRow = getBillListPanel().getBodyTable()
							.getSelectedRow();
					if (seleBodyRow > -1) {
						// �����кŻ����ѡ��VO
						if (null != billVO4C.getChildrenVO()
								&& billVO4C.getChildrenVO().length > 0)
							item4C = (GeneralBillItemVO) billVO4C
									.getChildrenVO()[seleBodyRow];
					}
				} else {
					// billVO4C = (GeneralBillVO) m_voBill.clone();
					billVO4C = new GeneralBillVO();
					getBillCardPanel().getBillValueVO(billVO4C);
					if (null == billVO4C.getHeaderValue("cwarehousename")) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40080802", "UPT40080802-000318")/*
																			 * @res
																			 * "����ѡ���ͷ�ֿ�"
																			 */);
						return;
					}
					int seleBodyRow = getBillCardPanel().getBodyPanel()
							.getTable().getSelectedRow();
					if (seleBodyRow > -1) {
						CircularlyAccessibleValueObject[] caVO = getM_voBill()
								.getChildrenVO();
						if (null != caVO && caVO.length > 0) {
							item4C = (GeneralBillItemVO) caVO[seleBodyRow];
						}
					} else {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40080802", "UPT40080802-000312")/*
																			 * @res
																			 * "��ѡ����Ϊ���׼�����"
																			 */);
						return;
					}
				}

				if (null != item4C)
					// ��ô����������ID
					sInvbasID = item4C.getCinvbasid();
				if (null != sInvbasID && !"".equals(sInvbasID.trim())) {

					nc.itf.uap.bd.inv.IInventoryQry invQry = (nc.itf.uap.bd.inv.IInventoryQry) nc.bs.framework.common.NCLocator
							.getInstance().lookup(
									nc.itf.uap.bd.inv.IInventoryQry.class
											.getName());
					if (null != invQry) {
						GroupInventoryVO gInvVo = (GroupInventoryVO) invQry
								.findGroupInvHeaderByPK(sInvbasID);
						if (null == gInvVo)
							return;
						// ȡ���׼���־
						sSetpartsflag = gInvVo.getSetpartsflag();
						if (null != sSetpartsflag
								&& !sSetpartsflag.booleanValue()) {
							showHintMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("40080802",
											"UPT40080802-000314")/*
																 * @res
																 * "ѡ���Ĵ�����ǳ��׼�"
																 */);
							// ������ǳ��׼��򷵻�
							return;
						}
						if (null != billVO4C) {
							// ׼������
							billVO4C.setChildrenVO(new GeneralBillItemVO[] { item4C });
							try {
								// ������˻ء��˻����������������Ƿ��и������еĻ�����
								VOCheck.checkOutRetAssemblyVO(billVO4C);
							} catch (Exception e) {
								// ��־�쳣
								nc.vo.scm.pub.SCMEnv.out(e);
								showHintMessage(e.getMessage());
								return;
							}
							// VO���ݽ���
							SpecialBillVO specBillVO4L = (SpecialBillVO) nc.ui.pub.change.PfChangeBO_Client
									.pfChangeBillToBill(billVO4C, "4C", "4L");
							if (null == specBillVO4L) {
								showHintMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("40080802",
												"UPT40080802-000316")/*
																	 * @res
																	 * "���ݽ���ʧ��"
																	 */);
								return;
							}

							RefMsg msg = new RefMsg(this);
							msg.setBillVos(new SpecialBillVO[] { specBillVO4L });
							msg.setIBillOperate(ILinkType.LINK_TYPE_ADD);
							// this.setVisible(false);
							nc.ui.ic.ic231.ClientUI.openNodeAsDlg(
									getClientUI(), msg);
						}
					}
				}
			} catch (BusinessException e) {
				// ��־�쳣
				nc.vo.scm.pub.SCMEnv.out(e);
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40080802", "UPT40080802-000320")/* @res "��ʾ��װ���������" */
						+ e.getMessage());
			}

		}

		protected void onRefInBill() {
			try {
				QryInBillDlg dlgBill = new QryInBillDlg("cgeneralhid",
						getEnvironment().getCorpID(), getEnvironment()
								.getUserID(), "40089907", "1=1", "45", null,
						null, "4C", getClientUI());

				if (dlgBill == null)
					return;

				nc.ui.ic.pub.pf.ICBillQuery dlgQry = new nc.ui.ic.pub.pf.ICBillQuery(
						getClientUI());
				// ����Դ��ѯģ��
				// dlgQry.setParent(this);
				dlgQry.setTempletID(getEnvironment().getCorpID(), "40080608",
						getEnvironment().getUserID(), null, "40089907");
				dlgQry.initData(getEnvironment().getCorpID(), getEnvironment()
						.getUserID(), "40089907", null, "4C", "45", null);
				// �޸��ˣ������� �޸�ʱ�䣺2008-9-22 ����04:59:35 �޸�ԭ��ҵ�����Ͱ���ֱ�˹��ˡ�
				dlgQry.setRefInitWhereClause("head.cbiztype", "ҵ������",
						" verifyrule != 'Z' and (pk_corp='"
								+ getEnvironment().getCorpID()
								+ "' or pk_corp='@@@@') ", null);
				if (dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

					// ��Ҫע��
					nc.vo.pub.query.ConditionVO[] voCons = dlgQry
							.getConditionVO();

					// ��ȡ��ѯ����
					StringBuffer sWhere = new StringBuffer(" 1=1 ");
					if (voCons != null && voCons.length > 0
							&& voCons[0] != null) {
						sWhere.append(" and " + dlgQry.getWhereSQL(voCons));
					}
					sWhere.append(" and head.cbiztype in (select pk_busitype from bd_busitype where verifyrule != 'Z' and (pk_corp='"
							+ getEnvironment().getCorpID()
							+ "' or pk_corp='@@@@')) ");

					// ����Դ���նԻ���
					dlgBill.initVar("cgeneralhid",
							getEnvironment().getCorpID(), getEnvironment()
									.getUserID(), null, sWhere.toString(),
							"45", null, null, "4C", null, this);

					dlgBill.setStrWhere(sWhere.toString());
					dlgBill.getBillVO();
					dlgBill.loadHeadData();
					dlgBill.addBillUI();
					dlgBill.setQueyDlg(dlgQry);

					nc.vo.scm.pub.ctrl.GenMsgCtrl
							.printHint("will load qrybilldlg");
					if (dlgBill.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg closeok");
						// ��ȡ��ѡVO
						nc.vo.pub.AggregatedValueObject[] vos = dlgBill
								.getRetVos();
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos");

						if (vos == null) {
							nc.vo.scm.pub.ctrl.GenMsgCtrl
									.printHint("qrybilldlg getRetVos null");
							return;
						}

						// //
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos is not null");
						nc.vo.pub.AggregatedValueObject[] voRetvos = (nc.vo.pub.AggregatedValueObject[]) nc.ui.pub.change.PfChangeBO_Client
								.pfChangeBillToBillArray(vos, "45", "4C");
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok");

						// ���ƽ���
						String cbiztype = null;
						if (voRetvos != null && voRetvos.length > 0)
							cbiztype = (String) voRetvos[0].getParentVO()
									.getAttributeValue("cbiztype");
						setBillRefResultVO(cbiztype, voRetvos);
						if (getM_voBill().getItemVOs().length > 0
								&& getM_voBill().getItemVOs()[0] != null
								&& getM_voBill().getItemVOs()[0].getNoutnum() != null) {
							setM_alSerialData(getM_voBill().getSNs());
							setM_alLocatorData(getM_voBill().getLocators());
						}

						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok setBillRefResultVO ok");

					}
				}
			} catch (Exception e) {

				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * �޸��ˣ������� �޸�ʱ�䣺2008-6-26 ����08:03:02 �޸�ԭ�򣺲���ֱ�˲ɹ���ⵥ�������۳��ⵥ���Զ�����ֱ�����۶�����
		 * 
		 * 
		 */
		protected void onRefDirectInBill() {
			try {
				QryInBillDlg dlgBill = new QryInBillDlg("cgeneralhid",
						getEnvironment().getCorpID(), getEnvironment()
								.getUserID(), "40089907", "1=1", "45", null,
						null, "4C", getClientUI());

				if (dlgBill == null)
					return;

				nc.ui.ic.pub.pf.ICBillQuery dlgQry = new nc.ui.ic.pub.pf.ICBillQuery(
						getClientUI());
				// ����Դ��ѯģ��
				// dlgQry.setParent(this);
				dlgQry.setTempletID(getEnvironment().getCorpID(), "40080608",
						getEnvironment().getUserID(), null, "40089907");
				dlgQry.initData(getEnvironment().getCorpID(), getEnvironment()
						.getUserID(), "40089907", null, "4C", "45", null);

				// �޸��ˣ������� �޸�ʱ�䣺2008-9-22 ����04:59:35 �޸�ԭ��ҵ�����Ͱ�ֱ�˹��ˡ�
				dlgQry.setRefInitWhereClause("head.cbiztype", "ҵ������",
						" verifyrule = 'Z' and (pk_corp='"
								+ getEnvironment().getCorpID()
								+ "' or pk_corp='@@@@') ", null);

				if (dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

					// ��Ҫע��
					nc.vo.pub.query.ConditionVO[] voCons = dlgQry
							.getConditionVO();

					// ��ȡ��ѯ����
					StringBuffer sWhere = new StringBuffer(" 1=1 ");
					if (voCons != null && voCons.length > 0
							&& voCons[0] != null) {
						sWhere.append(" and " + dlgQry.getWhereSQL(voCons));
					}
					sWhere.append(" and head.cbiztype in (select pk_busitype from bd_busitype where verifyrule = 'Z' and (pk_corp='"
							+ getEnvironment().getCorpID()
							+ "' or pk_corp='@@@@')) ");

					// ����Դ���նԻ���
					dlgBill.initVar("cgeneralhid",
							getEnvironment().getCorpID(), getEnvironment()
									.getUserID(), null, sWhere.toString(),
							"45", null, null, "4C", null, this);

					dlgBill.setStrWhere(sWhere.toString());
					dlgBill.getBillVO();
					dlgBill.loadHeadData();
					dlgBill.addBillUI();
					dlgBill.setQueyDlg(dlgQry);

					nc.vo.scm.pub.ctrl.GenMsgCtrl
							.printHint("will load qrybilldlg");
					if (dlgBill.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg closeok");
						// ��ȡ��ѡVO
						nc.vo.pub.AggregatedValueObject[] vos = dlgBill
								.getRetVos();
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos");

						if (vos == null || 0 == vos.length
								|| null == vos[0].getChildrenVO()) {
							nc.vo.scm.pub.ctrl.GenMsgCtrl
									.printHint("qrybilldlg getRetVos null");
							return;
						}

						// //
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos is not null");
						nc.vo.pub.AggregatedValueObject[] voRetvos = (nc.vo.pub.AggregatedValueObject[]) nc.ui.pub.change.PfChangeBO_Client
								.pfChangeBillToBillArray(vos, "45", "4C");
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok");
						// ����ֱ�����۶��������һ�ȡ�����Ϣ�����۳��ⵥ��
						voRetvos = (GeneralBillVO[]) GenMethod.callICService(
								"nc.bs.ic.ic211.GeneralHBO",
								"fillDirectSaleOrderInfo",
								new Class[] { voRetvos.getClass() },
								new Object[] { (GeneralBillVO[]) voRetvos });
						for (GeneralBillVO voRetvo : (GeneralBillVO[]) voRetvos)
							voRetvo.getHeaderVO().setBdirecttranflag(
									UFBoolean.TRUE);
						// ���ƽ���
						String cbiztype = null;
						if (voRetvos != null && voRetvos.length > 0)
							cbiztype = (String) voRetvos[0].getParentVO()
									.getAttributeValue("cbiztype");
						setBillRefResultVO(cbiztype, voRetvos);
						if (getM_voBill().getItemVOs().length > 0
								&& getM_voBill().getItemVOs()[0] != null
								&& getM_voBill().getItemVOs()[0].getNoutnum() != null) {
							setM_alSerialData(getM_voBill().getSNs());
							setM_alLocatorData(getM_voBill().getLocators());
						}

						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok setBillRefResultVO ok");

					}
				}
			} catch (Exception e) {

				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * ���۳���Գ����� v52�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 */
		private void onRushQuery() {
			try {
				if (!GenMethod.isProductEnabled(getEnvironment().getCorpID(),
						ProductCode.PROD_SO)) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008busi", "UPP4008busi-000069")/*
																		 * @res
																		 * "���۲�Ʒû������"
																		 */);
					return;
				}
				if (BillMode.New == getM_iMode()
						|| BillMode.Update == getM_iMode()) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008other", "UPP4008other-000506")/*
																			 * @res
																			 * "�뵥�ݱ���֮���ٲ�ѯ��"
																			 */);
					return;
				}
				String cgeneralhid = null;
				if (BillMode.List == getM_iCurPanel()) {
					if (getBillListPanel().getHeadTable().getSelectedRowCount() <= 0) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("scmcommon", "UPPSCMCommon-000167")/*
																				 * @
																				 * res
																				 * "û��ѡ�񵥾�"
																				 */);
						return;
					}
					cgeneralhid = (String) getBillListPanel()
							.getHeadBillModel().getValueAt(
									getBillListPanel().getHeadTable()
											.getSelectedRow(),
									IItemKey.CGENERALHID);
				} else {
					if (getM_voBill() != null
							&& getM_voBill().getHeaderVO() != null)
						cgeneralhid = getM_voBill().getHeaderVO()
								.getCgeneralhid();
				}
				if (nc.vo.ic.pub.GenMethod.isSEmptyOrNull(cgeneralhid)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("scmcommon", "UPPSCMCommon-000167")/*
																			 * @res
																			 * "û��ѡ�񵥾�"
																			 */);
					return;
				}

				RushLinkQueryVO[] rushvos = (RushLinkQueryVO[]) GenMethod
						.callICEJBService("nc.bs.ic.ic211.GeneralHBO",
								"queryRushLinkQueryVO", new Class[] {
										String.class, String.class },
								new Object[] { cgeneralhid,
										getEnvironment().getCorpID() });

				UIDialog uidlg = (UIDialog) Class
						.forName("nc.ui.scm.so.RushLinkQueryDlg")
						.getConstructor(
								new Class[] { Container.class,
										RushLinkQueryVO[].class })
						.newInstance(new Object[] { getClientUI(), rushvos });
				uidlg.showModal();

			} catch (Exception e) {
				GenMethod.handleException(getClientUI(), null, e);
			}
		}

		/**
		 * �������������������˻� v52add
		 * <p>
		 * <b>examples:</b>
		 * <p>
		 * ʹ��ʾ��
		 * <p>
		 * <b>����˵��</b>
		 * <p>
		 * 
		 * @author yangb
		 * @time 2007-7-5 ����01:25:44
		 */
		private void onOutReturn() {

			try {

				if (getM_iMode() != BillMode.Browse) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008other", "UPP4008other-000522")/*
																			 * @res
																			 * "δ����ĵ���,�����˻�"
																			 */);
					return;
				}

				GeneralBillVO vo = getCurVO();
				if (vo == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("scmcommon", "UPPSCMCommon-000167")/*
																			 * @res
																			 * "û��ѡ�񵥾�"
																			 */);
					return;
				}

				int[] selrows = null;
				if (BillMode.List == getM_iCurPanel())
					selrows = getBillListPanel().getChildListPanel().getTable()
							.getSelectedRows();
				else
					selrows = getBillCardPanel().getBodyPanel().getTable()
							.getSelectedRows();
				if (selrows == null || selrows.length <= 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008other", "UPP4008other-000515")/*
																			 * @res
																			 * "û��ѡ�еı����С�"
																			 */);
					return;
				}
				if (vo.getItemValue(0, IItemKey.CSOURCETYPE) != null
						&& vo.getItemValue(0, IItemKey.CSOURCETYPE).toString()
								.trim().equals(BillTypeConst.m_allocationOut)) {// startsWith("3")){
					showHintMessage(NCLangRes.getInstance().getStrByID(
							"40080802", "ClientUI-000000")/* ֱ��ҵ���γɵ����۳��ⵥ�����˻� */);
					return;

				}

				GeneralBillUICtl.getNaccumoutbacknumFromBB3(vo);

				vo = (GeneralBillVO) vo.clone();

				GeneralBillItemVO[] itemvos = vo.getItemVOs();
				GeneralBillItemVO[] selitemvos = new GeneralBillItemVO[selrows.length];
				for (int i = 0; i < selrows.length; i++)
					selitemvos[i] = itemvos[selrows[i]];
				vo.setChildrenVO(selitemvos);

				VOCheck.checkCanOutRet(vo);

				GeneralBillUICtl.processBillVOFor(vo,
						GeneralBillUICtl.Action.RETURN, m_bIsInitBill);

				// ��ǰ���б���ʽʱ�������л�������ʽ
				if (BillMode.List == getM_iCurPanel())
					onSwitch();

				setM_voBill(vo);

				// ���õ�ǰ������������ ���� 2004-04-05
				if (getM_utfBarCode() != null)
					getM_utfBarCode().setCurBillItem(null);

				getBillCardPanel().getBillModel().setNeedCalculate(false);

				// ����
				getBillCardPanel().addNew();
				setBillVO(getM_voBill());

				GeneralBillUICtl.processUIWhenRetOut(getBillCardPanel());

				for (int row = 0; row < getBillCardPanel().getBillModel()
						.getRowCount(); row++) {
					// ������״̬Ϊ����
					getBillCardPanel().getBillModel().setRowState(row,
							nc.ui.pub.bill.BillModel.ADD);
					getBillCardPanel().getBillModel().setValueAt(null, row,
							IItemKey.NAME_BODYID);
				}
				// �����������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
				setNewBillInitData();
				getBillCardPanel().setEnabled(true);
				setM_iMode(BillMode.New);

				GeneralBillUICtl.procFlagAndCalcNordcanoutnumAfterRefAdd(
						getM_voBill(), getBillCardPanel(), getBillType());

				// ���õ��ݺ��Ƿ�ɱ༭
				if (getBillCardPanel().getHeadItem("vbillcode") != null)
					getBillCardPanel().getHeadItem("vbillcode").setEnabled(
							m_bIsEditBillCode);

				setButtonStatus(true);

			} catch (Exception e) {
				GenMethod.handleException(getClientUI(), null, e);
			} finally {
				getBillCardPanel().getBillModel().setNeedCalculate(true);
			}

		}

	}

	public IButtonManager getButtonManager() {
		if (m_buttonManager == null) {
			try {
				m_buttonManager = new ButtonManager211(this);
			} catch (BusinessException e) {
				// ��־�쳣
				nc.vo.scm.pub.SCMEnv.error(e);
				showErrorMessage(e.getMessage());
			}
		}
		return m_buttonManager;
	}

	/**
	 * �ӵ��ݱ����У����ҳ����ǳ��׼��Ĵ�������ع�һ���µı�����VO[] ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18
	 * 11:29:23) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	GeneralBillItemVO searchInvKit(GeneralBillItemVO cvos) {
		ArrayList alInvKit = null;

		if (cvos != null) {
			if (cvos.getIsSet() != null && cvos.getIsSet().intValue() == 1)
				return cvos;
		}
		return null;
	}

	/**
	 * �����ߣ����˾� ���ܣ����б�ʽ��ѡ��һ�ŵ��� ������ ������alListData�е����� ���أ��� ���⣺ ���ڣ�(2001-11-23
	 * 18:11:18) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void selectBillOnListPanel(int iBillIndex) {
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ���ݵĴ���״̬����ǩ��/ȡ��ǩ���Ǹ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setBtnStatusSign() {
		setBtnStatusSign(true);
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ���ݵĴ���״̬����ǩ��/ȡ��ǩ���Ǹ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setBtnStatusSign(boolean bUpdateButtons) {
		// ֻ�����״̬�²��ҽ������е���ʱ����
		if (BillMode.Browse != getM_iMode() || getM_iLastSelListHeadRow() < 0
				|| m_iBillQty <= 0) {
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			return;
		}
		int iSignFlag = isSigned();
		if (SIGNED == iSignFlag) {
			// ��ǩ�֣��������ð�ť״̬,ǩ�ֲ����ã�ȡ��ǩ�ֿ���
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(true);
			// ����ɾ����
			getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
					.setEnabled(false);
			getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
					.setEnabled(false);
			getButtonManager()
					.getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
					.setEnabled(false);

		} else if (NOTSIGNED == iSignFlag) {
			// δǩ�֣��������ð�ť״̬,ǩ�ֿ��ã�ȡ��ǩ�ֲ�����
			// �ж��Ƿ���������������Ϊ�����������ģ�����ֻҪ����һ�о����ˡ�

			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					true);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			// ��ɾ����
			if (isCurrentTypeBill()) {

				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(true);
			} else {

				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
			}
			// if (isSetInv(m_voBill, m_iFirstSelectRow) &&
			// !isDispensedBill(null))
			if (BillMode.Card == getM_iCurPanel()) {
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
						.setEnabled(true);
			}
			// else
			// getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);

		} else { // ����ǩ�ֲ���
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			// ��ɾ����
			if (isCurrentTypeBill()) {
				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(true);
			} else {
				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
			}
		}
		// ʹ������Ч
		if (bUpdateButtons)
			updateButtons();

	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á� ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setButtonsStatus(int iBillMode) {

		// ���ģʽ�£��е��ݲ����Ѿ�ǩ�ֲſ���
		if (getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG) != null) {
			if (iBillMode == BillMode.Browse && m_iBillQty > 0
					&& isSigned() == SIGNED)
				getButtonManager()
						.getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG)
						.setEnabled(true);
			else
				getButtonManager()
						.getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG)
						.setEnabled(false);
		}
		if (getButtonManager().getButton(ICButtonConst.BTN_ASSIST_COOP_45) != null) {
			if (iBillMode == BillMode.Browse
					&& m_iBillQty > 0
					&& isSigned() == SIGNED
					&& null != getCurVO()
					&& (null == getCurVO().getHeaderVO().getBsalecooppur() || (null != getCurVO()
							.getHeaderVO().getBsalecooppur() && !getCurVO()
							.getHeaderVO().getBsalecooppur().booleanValue()))) {
				getButtonManager().getButton(ICButtonConst.BTN_ASSIST_COOP_45)
						.setEnabled(true);
			} else
				getButtonManager().getButton(ICButtonConst.BTN_ASSIST_COOP_45)
						.setEnabled(false);
		}
		// in card browser status, can use dispense button.
		if (getButtonManager()
				.getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE) != null) {
			if (getM_iCurPanel() == BillMode.Card
					&& iBillMode == BillMode.Browse && m_iBillQty > 0
					&& isSigned() != SIGNED)
				// if (isSetInv(m_voBill, m_iFirstSelectRow)
				// && !isDispensedBill(null))
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
						.setEnabled(true);
			// else
			// getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);
			else
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(
						false);
		}
		//
		// if (m_boVerify != null) {
		// if (iBillMode == BillMode.Browse
		// && m_iBillQty > 0)
		// m_boVerify.setEnabled(false);
		// else
		// m_boVerify.setEnabled(false);
		// }
		if (getButtonManager()
				.getButton(ICButtonConst.BTN_ASSIST_FUNC_REFER_IN) != null) {
			if (iBillMode == BillMode.Browse)
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_REFER_IN)
						.setEnabled(true);
			else
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_REFER_IN).setEnabled(
						false);

		}
		updateButtons();
		// ��Ҫ�������ð�ť��ˢ�����ఴť��״̬��
		// super.initButtonsData();
		// m_vTopMenu.addElement(getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG));
		// m_vBillMngMenu.addElement(getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE));
		// m_vBillMngMenu.addElement(m_boVerify);
		// super.setButtons();
	}

	/**
	 * ��������֮����Ҫ�������׵ı�־�û�VO�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-06-03 14:39:46)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	void setDispenseFlag(GeneralBillVO gvo, int[] rownums) {
		if (gvo == null || gvo.getItemCount() == 0)
			return;
		ArrayList alBid = null;
		GeneralBillItemVO[] resultvos = gvo.getItemVOs();
		if (resultvos != null) {
			alBid = new ArrayList();

			for (int i = 0; i < rownums.length; i++) {
				if (!isSetInv(gvo, rownums[i]))
					continue;
				resultvos[rownums[i]].setFbillrowflag(new Integer(
						nc.vo.ic.pub.BillRowType.afterConvert));
				alBid.add(resultvos[rownums[i]].getPrimaryKey());

			}

		}

	}

	/**
	 * �����ߣ�zhx ���ܣ����׼��������Ĵ������� ���������׼��Ĵ������ID�����׼����������ڼ������������ ���أ� ���⣺
	 * ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public GeneralBillItemVO[] splitInvKit(GeneralBillItemVO itemvo,
			GeneralBillHeaderVO headervo, UFDouble nsetnum) {

		if (itemvo == null)
			return null;
		String sInvSetID = itemvo.getCinventoryid();

		if (sInvSetID != null) {
			ArrayList alInvvo = new ArrayList();
			try {
				alInvvo = GeneralBillHelper.queryPartbySetInfo(sInvSetID);
			} catch (Exception e2) {
				nc.vo.scm.pub.SCMEnv.error(e2);
			}
			if (alInvvo == null) {
				nc.vo.scm.pub.SCMEnv.out("�ó��׼�û��������������ݿ�...");
				return null;
			}
			int rowcount = alInvvo.size();
			GeneralBillItemVO[] voParts = new GeneralBillItemVO[rowcount];
			nc.vo.pub.lang.UFDate db = new nc.vo.pub.lang.UFDate(
					getEnvironment().getLogDate());
			for (int i = 0; i < rowcount; i++) {
				voParts[i] = new GeneralBillItemVO();
				voParts[i].setInv((InvVO) alInvvo.get(i));
				voParts[i].setDbizdate(db);
				UFDouble nchildnum = ((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum") == null ? new UFDouble(
						0) : new UFDouble(((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum").toString());
				UFDouble ntotalnum = null;
				if (nsetnum != null)
					ntotalnum = nchildnum.multiply(nsetnum);
				else
					ntotalnum = nchildnum;
				UFDouble hsl = ((InvVO) alInvvo.get(i))
						.getAttributeValue("hsl") == null ? null
						: new UFDouble(((InvVO) alInvvo.get(i))
								.getAttributeValue("hsl").toString());
				UFDouble ntotalastnum = null;
				if (hsl != null && hsl.doubleValue() != 0) {
					ntotalastnum = ntotalnum.div(hsl);
				}

				voParts[i].setAttributeValue("nshouldoutnum", ntotalnum);
				voParts[i].setAttributeValue("nshouldoutassistnum",
						ntotalastnum);
				voParts[i].setAttributeValue("noutnum", ntotalnum);
				voParts[i].setAttributeValue("noutassistnum", ntotalastnum);
				voParts[i].setCsourceheadts(null);
				voParts[i].setCsourcebodyts(null);
				//
				voParts[i].setAttributeValue("csourcetype",
						headervo.getCbilltypecode());
				voParts[i].setAttributeValue("csourcebillhid",
						headervo.getPrimaryKey());
				voParts[i].setAttributeValue("csourcebillbid",
						itemvo.getPrimaryKey());
				voParts[i].setAttributeValue("vsourcebillcode",
						headervo.getVbillcode());
				voParts[i].setAttributeValue("creceieveid",
						itemvo.getCreceieveid());
				voParts[i].setAttributeValue("cprojectid",
						itemvo.getCprojectid());
				String s = "vuserdef";
				String ss = "pk_defdoc";
				for (int j = 0; j < 20; j++) {

					voParts[i]
							.setAttributeValue(
									s + String.valueOf(j + 1),
									itemvo.getAttributeValue(s
											+ String.valueOf(j + 1)));
					voParts[i]
							.setAttributeValue(
									ss + String.valueOf(j + 1),
									itemvo.getAttributeValue(ss
											+ String.valueOf(j + 1)));
				}
				voParts[i].setCgeneralhid(null);
				voParts[i].setCgeneralbid(null);
				voParts[i].setStatus(nc.vo.pub.VOStatus.NEW);
			}
			return voParts;
		}
		return null;

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-3-26 13:55:23)
	 * 
	 * @return nc.ui.bd.b21.CurrArith
	 * @param pk_corp
	 *            java.lang.String
	 */
	public ICBcurrArithUI getCurrArith() {

		if (clsCurrArith == null) {
			try {
				clsCurrArith = new ICBcurrArithUI(ClientEnvironment
						.getInstance().getCorporation().getPrimaryKey());
			} catch (Exception e) {
				nc.ui.ic.pub.tools.GenMethod.handleException(this, null, e);
			}
		}

		return clsCurrArith;
	}

	/**
	 * �����ߣ����˾� ���ܣ�ѡ���б���ʽ�µĵ�sn�ŵ��� ������sn ������� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void setListBodyData(GeneralBillItemVO voi[]) {
		getBB3Info(voi, getBillListPanel().getBodyBillModel());
		if (voi != null && voi.length > 0) {
			setCurrDigit(BillMode.List,
					(String) voi[0].getAttributeValue("cquotecurrency"));
		}
		super.setListBodyData(voi);
	}

	/**
	 * �����ߣ����˾� ���ܣ��ڱ�������ʾVO ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton,
			boolean bExeFormule) {
		if (bvo != null && bvo.getChildrenVO() != null
				&& bvo.getChildrenVO().length > 0) {
			getBB3Info(bvo.getItemVOs(), getBillCardPanel().getBillModel());
			setCurrDigit(BillMode.Card,
					(String) bvo.getChildrenVO()[0]
							.getAttributeValue("cquotecurrency"));
		}
		super.setBillVO(bvo, bUpdateBotton, bExeFormule);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-3-26 13:55:23)
	 * 
	 * @return nc.ui.bd.b21.CurrArith
	 * @param pk_corp
	 *            java.lang.String
	 */
	public void setCurrDigit(int ipanelsatus, String cquotecurrency) {

		if (cquotecurrency == null || cquotecurrency.trim().length() <= 0)
			return;

		int iDigit = 2;
		try {
			ICBcurrArithUI currtype = getCurrArith();

			Integer Digit = currtype.getBusiCurrDigit(cquotecurrency);

			if (Digit != null)
				iDigit = Digit.intValue();

			if (ipanelsatus == BillMode.Card) {
				BillItem item = getBillCardPanel().getBodyItem("nquotemny");
				if (item != null)
					item.setDecimalDigits(iDigit);
			} else {
				BillItem item = getBillListPanel().getBodyBillModel()
						.getItemByKey("nquotemny");
				if (item != null)
					item.setDecimalDigits(iDigit);
			}

		} catch (Exception e) {
			SCMEnv.error(e);
		}

	}

	/**
	 * UAP�ṩ�ı༭ǰ����
	 * 
	 * @param value
	 * @param row
	 * @param itemkey
	 * @return
	 */
	public boolean isCellEditable(
			boolean value/* BillModel��isCellEditable�ķ���ֵ */,
			int row/* ��������� */, String itemkey/* ��ǰ�е�itemkey */) {

		if (getM_iMode() == BillMode.Browse)
			return false;

		if (itemkey != null) {
			if (itemkey.equals(getEnvironment().getShouldAssistNumItemKey())) {
				InvVO voInv = getM_voBill().getItemInv(row);
				if (voInv != null && voInv.getIsAstUOMmgt() != null
						&& voInv.getIsAstUOMmgt().intValue() == 1)
					return true;
				else
					return false;
			}
			if (itemkey.equals(getEnvironment().getShouldNumItemKey()))
				return true;
		}
		return super.isCellEditable(value, row, itemkey);

	}

	/**
	 * ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void getBB3Info(GeneralBillItemVO[] voi, BillModel md) {
		if (voi == null || voi.length <= 0 || md == null)
			return;
		UFBoolean bget_naccumoutbacknum = CheckTools.toUFBoolean(voi[0]
				.getAttributeValue(IItemKey.bget_naccumoutbacknum));
		if (bget_naccumoutbacknum != null
				&& bget_naccumoutbacknum.booleanValue())
			return;
		ArrayList<String> bb3field = new ArrayList<String>(4);
		ArrayList<String> vofield = new ArrayList<String>(4);
		IntList type = new IntList(4);
		String[] uikeys = new String[] { "naccinvoicenum", "naccountnum1",
				"nrushnum", IItemKey.naccumoutbacknum };
		String[] vofieldkeys = new String[] { "nsignnum", "naccountnum1",
				"nrushnum", IItemKey.naccumoutbacknum };
		BillItem item = md.getItemByKey(IItemKey.naccumoutbacknum);
		for (int i = 0; i < uikeys.length; i++) {
			item = md.getItemByKey(uikeys[i]);
			if (item != null && item.isShow()) {
				vofield.add(uikeys[i]);
				bb3field.add(vofieldkeys[i]);
				type.add(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			}
		}
		if (vofield.size() > 0) {
			try {
				GeneralBillUICtl.fillBodyVOFromBB3(voi,
						vofield.toArray(new String[vofield.size()]),
						bb3field.toArray(new String[bb3field.size()]),
						type.toIntArray());
				for (int i = 0; i < voi.length; i++) {
					voi[i].setAttributeValue(IItemKey.bget_naccumoutbacknum,
							UFBoolean.TRUE);
				}
			} catch (Exception e) {
				GenMethod.handleException(null, null, e);
			}
		}
	}

	/**
	 * ���˵��ݲ��� �����ߣ����� ���ܣ���ʼ�����չ��� ������ ���أ� ���⣺ ���ڣ�(2001-7-17 10:33:20)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void filterRef(String sCorpID) {
		try {
			super.filterRef(sCorpID);
			// ���˲ֿ����
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			RefFilter.filtWh(bi, sCorpID, null);
			// if(getM_iMode()==BillMode.Browse){
			// RefFilter.filtWh(bi, sCorpID,null);
			// }else{
			// RefFilter.filtWh(bi, sCorpID,
			// new String[] { "and isdirectstore = 'N'" });
			// }
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	@Override
	public QueryDlgHelp getQryDlgHelp() {
		if (m_qryDlgHelp == null) {
			m_qryDlgHelp = new QueryDlgHelp(this);
		}
		return (QueryDlgHelp) m_qryDlgHelp;
	}

	public void onCoop45Save() {
		showHintMessage("");
		GeneralBillVO currBillVO = getCurVO();

		if (null == currBillVO || null == currBillVO.getItemVOs()
				|| currBillVO.getItemVOs().length == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40080802", "UPT40080802-000321"));// ��ѡ��һ�ŵ���
			return;
		}
		String pk_cusmandoc = (String) currBillVO
				.getHeaderValue(IItemKey.ccustomerid);
		if (null == pk_cusmandoc) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40080802", "UPT40080802-000322"));// "��ͷ�ͻ�Ϊ�գ�����Эͬ")
			return;
		}
		// �޸��ˣ������� �޸�ʱ�䣺2008-10-14 ����08:15:21 �޸�ԭ�򣺷�Эͬ�ͻ�Ҳ��Эͬ --�ԶԶԣ� by liuzy
		// Object[][] objResult =
		// nc.ui.scm.pub.cache.CacheTool.getMultiColValue(
		// "bd_cumandoc", "pk_cumandoc", new String[] {
		// "cooperateflag"
		// }, new String[] {
		// pk_cusmandoc
		// });
		// if (null != objResult && objResult.length > 0 && null != objResult[0]
		// && objResult.length > 0) {
		// UFBoolean cooperateflag = nc.vo.scm.ic.bill.SwitchObject
		// .switchObjToUFBoolean(objResult[0][0]);
		// if (!cooperateflag.booleanValue()) {
		// showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","UPT40080802-000323"));//"�ͻ���Эͬ�ͻ�������Эͬ");
		// return;
		// }
		// }
		currBillVO.getHeaderVO().setAttributeValue("coperatoridnow",
				getEnvironment().getUserID());
		ArrayList<GeneralBillVO> al_splitBills = filterAndSplitVOs((GeneralBillVO) currBillVO
				.clone());// ��¡һ�����󣬷���Эͬδ�ɹ�����־Ҳ��������
		if (null == al_splitBills)
			return;
		if (null != al_splitBills && al_splitBills.size() > 0) {
			GeneralBillVO[] vos = new GeneralBillVO[al_splitBills.size()];
			al_splitBills.toArray(vos);
			// �����һ��VO��ͷ��Эͬ��־
			((GeneralBillHeaderVO) (vos[vos.length - 1].getParentVO()))
					.setBsalecooppur(UFBoolean.TRUE);
			try {
				Object[] obj = nc.ui.pub.pf.PfUtilClient.runBatch(this,
						"COOPPO", ScmConst.m_saleOut, getEnvironment()
								.getLogDate(), vos, vos, null, null);
				if (null != obj && obj.length > 0) {
					int count = obj.length;
					ArrayList<String> al_billcodes = new ArrayList<String>();
					for (int i = 0; i < obj.length; i++) {
						al_billcodes.add((String) obj[i]);
					}
					if (count > 0 && al_billcodes.size() > 0) {
						String msg = nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40080802", "UPT40080802-000324",
										null,
										new String[] { String.valueOf(count) });// "Эͬ����"
																				// +
																				// count
																				// +
																				// "�Ųɹ���ⵥ�����ݺ����£�";
						for (int i = 0; i < al_billcodes.size(); i++) {
							if (i % 2 == 0)
								msg += "\n";
							msg += al_billcodes.get(i) + ",";
						}
						getCurVO().getHeaderVO()
								.setBsalecooppur(UFBoolean.TRUE);
						if (BillMode.List == getM_iCurPanel()) {
							int selrow = getBillListPanel().getHeadTable()
									.getSelectedRow();
							getBillListPanel().getHeadBillModel().setValueAt(
									UFBoolean.TRUE, selrow,
									IItemKey.bsalecooppur);
						} else
							getBillCardPanel().getHeadItem(
									IItemKey.bsalecooppur).setValue(
									UFBoolean.TRUE);
						// showYesNoMessage(msg);
						nc.ui.pub.beans.MessageDialog
								.showHintDlg(
										this,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("SCMCOMMON",
														"UPPSCMCommon-000270")/*
																			 * @res
																			 * "��ʾ"
																			 */,
										msg);
						setButtonsStatus(getM_iMode());
					}
				}
			} catch (Exception e) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40080802", "UPT40080802-000329")
						+ "\n" + e.getMessage());
				SCMEnv.out(e.getMessage());
			}
		}
	}

	private ArrayList<GeneralBillVO> filterAndSplitVOs(GeneralBillVO currBillVO) {
		// ���˲����������ı��壬�Լ�������֯���ݣ�����Դ���ݱ���ID��ͬ����������֯��һ������VO��
		GeneralBillItemVO[] items = currBillVO.getItemVOs();
		ArrayList<String> al_30bids = new ArrayList<String>();
		ArrayList<GeneralBillItemVO> al_repeatitems = new ArrayList<GeneralBillItemVO>();
		ArrayList<GeneralBillItemVO> al_items = new ArrayList<GeneralBillItemVO>();
		ArrayList<GeneralBillVO> al_splitBills = new ArrayList<GeneralBillVO>();

		do {
			if (al_repeatitems.size() > 0) {
				items = new GeneralBillItemVO[al_repeatitems.size()];
				al_repeatitems.toArray(items);
				al_repeatitems.clear();
				al_30bids.clear();
				al_items.clear();
			}
			int i = 0;
			// modified by liuzy 2008-10-31 ����09:05:29
			// ���۶��������۳��ⵥ֮����ܻ����������ݣ��磺�����������۷�Ʊ
			// ������л�������������ȷ�ϣ�һ������¿���Ϊ���۳��ⵥ��Դͷ���������۶���
			String sCfirstbillbid = null;
			for (GeneralBillItemVO item : items) {
				sCfirstbillbid = item.getCfirstbillbid();
				if (!ScmConst.SO_Order.equals(item.getCfirsttype())
						|| null == sCfirstbillbid || "".equals(sCfirstbillbid)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40080802", "UPT40080802-000325"));// "�õ��ݲ�����Эͬ���ɲɹ���ⵥ����������Դ���ݱ��������۶���");
					return null;
				}

				if (!al_30bids.contains(sCfirstbillbid)) {
					al_30bids.add(sCfirstbillbid);
					al_items.add(item);
				} else
					al_repeatitems.add(item);
				i++;
			}
			GeneralBillVO vo = new GeneralBillVO();
			GeneralBillItemVO[] itemvos = new GeneralBillItemVO[al_items.size()];
			itemvos = al_items.toArray(itemvos);
			vo.setParentVO(currBillVO.getParentVO());
			vo.setChildrenVO(itemvos);
			al_splitBills.add(vo);

		} while (al_repeatitems.size() > 0);
		return al_splitBills;
	}
	
	@Override
	public boolean beforeEdit(BillItemEvent e) {
		
		if("storprice".equals(e.getItem().getKey())) {
			Object cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();
			//��0001A810000000000JB9��-> ����
			if("0001A810000000000JB9".equals(getBillCardPanel().getHeadItem("cdilivertypeid").getValueObject()))
				((UIRefPane)e.getItem().getComponent()).setWhereString(" 1 = 0 ");
			else 
				((UIRefPane)e.getItem().getComponent()).setWhereString(" 1 = 1 and pk_stordoc = '"+cwarehouseid+"' ");
		} else if("transprice".equals(e.getItem().getKey())) {
			// 0001A810000000000JB9 -> ����
			if("0001A810000000000JB9".equals(getBillCardPanel().getHeadItem("cdilivertypeid").getValueObject()))
				((UIRefPane)e.getItem().getComponent()).setWhereString(" 1 = 0 ");
			else 
				((UIRefPane)e.getItem().getComponent()).setWhereString(" 1 = 1 and nvl(dr,0)=0 and pk_corp = '"+getCorpPrimaryKey()+"' and vbillstatus = 1 and pk_transport = '"+getBillCardPanel().getHeadItem("pk_transport").getValueObject()+"'");
		}
		
		return super.beforeEdit(e);
	}
	
	/**
	 * ��д��ͷ�༭����
	 */
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		try {
			if("transprice".equals(e.getKey()))
				afterTransprice(e);
		
			else if("storprice".equals(e.getKey()))
				afterStorprice(e);
			
			else if("cdilivertypeid".equals(e.getKey()))
				afterCdilivertypeid(e);
				
		} catch(Exception e1) {
			Logger.error(e1.getMessage());
		}
			
	}
	
	protected final void afterTransprice(BillEditEvent e) throws Exception {
		getBillCardPanel().getHeadItem("pk_transport_b").setValue(((UIRefPane)e.getSource()).getRefPK());
		
	}
	
	protected final void afterStorprice(BillEditEvent e) throws Exception {
		getBillCardPanel().getHeadItem("pk_storcontract_b").setValue(((UIRefPane)e.getSource()).getRefPK());
		
	}
	
	protected final void afterCdilivertypeid(BillEditEvent e) throws Exception {
		
		if("����".equals(((UIRefPane)e.getSource()).getRefName())) {
			((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).setPK(null);
			((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).setValue(null);
			getBillCardPanel().getHeadItem("pk_storcontract_b").setValue(null);
			((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).setEnabled(false);
			
			((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).setPK(null);
			((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).setValue(null);
			getBillCardPanel().getHeadItem("pk_transport_b").setValue(null);
			((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).setEnabled(false);
		} else {
			((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).setEnabled(true);
			((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).setEnabled(true);
		}
		
	}
	
	/**
	 * ��д���淽���������Զ�����ӵļ����ֶε�ֵ
	 * 
	 * Overrider �� river
	 * 
	 * Date �� 2012-09-13
	 * 
	 */
	@Override
	public boolean onSave() {
		boolean retType = false;
		
		GeneralBillVO voInputBill = getBillVO();
		
		/* �ڴ������֤ �� ������䷽ʽΪ����ʱ���䵥�ۼ�װж�Ѳ���Ҫ��д �� ������2���ֶ�Ϊ����  by river */
		if(voInputBill != null && voInputBill.getParentVO() != null) {
			
			// ��ȡ���۶����е� [ �Ƿ����� ] �����ж����䵥���Ƿ���
			CircularlyAccessibleValueObject[] bodyVOs = voInputBill.getChildrenVO();
			if(bodyVOs != null && bodyVOs.length > 0) {
				Object csourcebillhid = bodyVOs[0].getAttributeValue("csourcebillhid");
				Object issince = null;
				try { issince = UAPQueryBS.getInstance().executeQuery("select issince from so_sale where csaleid = '"+csourcebillhid+"' ", new ColumnProcessor()); } catch(Exception e) { Logger.error(e.getMessage(), e, this.getClass(), "onSave"); }
				if(!(issince != null && "Y".equals(issince))) {
					if(((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).getRefName() == null) {
						showErrorMessage("��ͷ [ ���䵥�� ] ����Ϊ�ա�");
						return false;
					}
				}
			
			}
			
		}
		
		retType = super.onSave();
		
		afterOnSave(voInputBill , retType);
		
		return retType;
	}
	
	/**
	 * ���� �� �����������
	 * 
	 * @author river
	 * 
	 * Create date : 2012-09-19
	 * 
	 * @param voInputBill
	 * @param retType
	 */
	protected final void afterOnSave(GeneralBillVO voInputBill , boolean retType) {
		
		GeneralBillVO newBill = getBillVO();
		
		if(retType && newBill != null && newBill.getPrimaryKey() != null) {
			
			Object concode = voInputBill.getParentVO().getAttributeValue("concode");
			Object pk_transport = voInputBill.getParentVO().getAttributeValue("pk_transport");
			Object transprice = ((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).getRefName();
			Object storprice = ((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).getRefName();
			Object contracttype = voInputBill.getParentVO().getAttributeValue("contracttype");
			Object salecode = voInputBill.getParentVO().getAttributeValue("salecode");
			Object pk_storcontract_b = getBillCardPanel().getHeadItem("pk_storcontract_b").getValueObject();
			Object pk_transport_b = getBillCardPanel().getHeadItem("pk_transport_b").getValueObject();
			Object pk_contract = voInputBill.getParentVO().getAttributeValue("pk_contract");
			
			newBill.getParentVO().setAttributeValue("transprice", transprice);
			newBill.getParentVO().setAttributeValue("storprice", storprice);
			newBill.getParentVO().setAttributeValue("pk_storcontract_b", pk_storcontract_b);
			newBill.getParentVO().setAttributeValue("pk_transport_b", pk_transport_b);
			
			try { 
				
				String sqlField = ConvertFunc.change(new String[]{
						" concode = '" + (concode == null ? "" : concode) + "' " ,
						" pk_transport = '" + (pk_transport == null ? "" : pk_transport) + "' " ,
						" transprice = '" + (transprice == null ? "" : transprice) + "' " ,
						" storprice = '" + (storprice == null ? "" : storprice) + "' " ,
						" contracttype = '" + (contracttype == null ? "" : contracttype) + "' " ,
						" salecode = '" + (salecode == null ? "" : salecode) + "' " ,
						" pk_storcontract_b = '" + (pk_storcontract_b == null ? "" : pk_storcontract_b) + "' " ,
						" pk_transport_b = '" + (pk_transport_b == null ? "" : pk_transport_b) + "' " ,
						" pk_contract = '"+(pk_contract == null ? "" : pk_contract)+"' " ,
				});
				
				UAPQueryBS.getInstance().executeQuery("update ic_general_h set "+sqlField+" where cgeneralhid = '"+newBill.getPrimaryKey()+"'", null); 
				
			} catch(Exception ex) { }
			
			// �������ݺ��TS�ֶ�����ȡֵ���������ǩ����ʾ���޸ġ�ɾ��
			try {
				Object ts = UAPQueryBS.getInstance().executeQuery("select ts from ic_general_h where cgeneralhid = '"+newBill.getPrimaryKey()+"'", new ColumnProcessor());
				newBill.getParentVO().setAttributeValue("ts", ts);
				
				updateBillToList(newBill);
				
			} catch(Exception e) {}
		}
		
	}
	
}