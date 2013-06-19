package nc.ui.ic.ic221;

import java.util.ArrayList;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.pub.InvOnHandDialog;
import nc.ui.ic.pub.PageCtrlBtn;
import nc.ui.ic.pub.bill.QueryDlgHelpForSpec;
import nc.ui.ic.pub.bill.SpecialBillBaseUI;
import nc.ui.ic.pub.bill.SpecialBillHelper;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.query.QueryDlgUtil;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pub.BD_TYPE;
import nc.vo.scm.pub.BillRowNoVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.query.DataPowerCtl;

/*
 * �����ߣ�������
 * �������ڣ�2001-04-20
 * ���ܣ�ת�ⵥ����
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public class ClientUI extends SpecialBillBaseUI {
	/**
	 * ClientUI ������ע�⡣
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * � ���ܣ���ת�������ͨ����VO�����к� ������ ���أ� ���⣺ ���ڣ�(2005-1-18 14:45:22)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 */
	private void setRowNo(GeneralBillVO voBill) {

		if (voBill == null || voBill.getChildrenVO().length <= 0)
			return;
		BillRowNoVO.setVOsRowNoByRule(voBill.getItemVOs(), "4I", "crowno");
		// int len = voBill.getChildrenVO().length;
		// for (int i=1;i<=len;i++){
		// voBill.getChildrenVO()[i-1].setAttributeValue("crowno",(new
		// Integer(i)).toString());
		// }

	}
	public void afterWhOutEdit(nc.ui.pub.bill.BillEditEvent e) {
		super.afterWhOutEdit(e);
		
		WhVO voWh = getWhInfoByRef("coutwarehouseid");
		// �����֯����
		nc.ui.pub.bill.BillItem biCalBody = getBillCardPanel()
				.getHeadItem("pk_calbody_out");
		if (biCalBody != null) {
			if (voWh != null){
				biCalBody.setValue(voWh.getPk_calbody());
				m_voBill.setHeaderValue("pk_calbody_out", voWh.getPk_calbody());
			}
			else{
				biCalBody.setValue(null);
				m_voBill.setHeaderValue("pk_calbody_out", null);
			}
			
			
			String sNewID = null;
			if (voWh != null)
				sNewID = voWh.getPk_calbody();
			// ����˿����֯
			// �����ǰ�Ĳֿⲻ����
			// ���˲ֿ����
			String sConstraint[] = null;
			String sConstraint1[] = null;
			if (sNewID != null) {
				sConstraint = new String[1];
				sConstraint[0] = " AND isdirectstore = 'N' AND pk_calbody='"
						+ sNewID + "'";
				sConstraint1 = new String[1];
				sConstraint1[0] = " and mm_jldoc.gcbm='" + sNewID + "'";
			}
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					"coutwarehouseid");
			RefFilter.filtWh(bi, m_sCorpID, sConstraint);
			
			bi = getBillCardPanel().getHeadItem(
			"cinwarehouseid");
	RefFilter.filtWh(bi, m_sCorpID, sConstraint);
		}
		nc.ui.pub.bill.BillItem biCalBodyname = getBillCardPanel()
				.getHeadItem("vcalbodyname_out");
		if (biCalBodyname != null) {
			if (voWh != null){
				biCalBodyname.setValue(voWh.getVcalbodyname());
				m_voBill.setHeaderValue("vcalbodyname_out", voWh.getVcalbodyname());
			}
			else{
				biCalBodyname.setValue(null);
				m_voBill.setHeaderValue("vcalbodyname_out", null);
			}
		}
	}
	
	public void afterCalbodyOutEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			String sNewID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem(e.getKey()).getComponent()).getRefPK();
			// ����˿����֯
			// �����ǰ�Ĳֿⲻ����
			// ���˲ֿ����
			String sConstraint[] = null;
			String sConstraint1[] = null;
			if (sNewID != null) {
				sConstraint = new String[1];
				sConstraint[0] = " AND isdirectstore = 'N' AND pk_calbody='"
						+ sNewID + "'";
				sConstraint1 = new String[1];
				sConstraint1[0] = " and mm_jldoc.gcbm='" + sNewID + "'";
			}
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					"coutwarehouseid");
			RefFilter.filtWh(bi, m_sCorpID, sConstraint);
			// ����:clear warehouse
			WhVO whvo = m_voBill.getWhOut();
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(
					"coutwarehouseid");
			if (biWh != null
					&& (whvo == null || (sNewID != null && !sNewID.equals(whvo
							.getPk_calbody())))){
				biWh.setValue(null);
				m_voBill.setWhOut(null);
			}
			
			bi = getBillCardPanel().getHeadItem(
			"cinwarehouseid");
	RefFilter.filtWh(bi, m_sCorpID, sConstraint);
	// ����:clear warehouse
	whvo = m_voBill.getWhIn();
	biWh = getBillCardPanel().getHeadItem(
			"cinwarehouseid");
	if (biWh != null
			&& (whvo == null || (sNewID != null && !sNewID.equals(whvo
					.getPk_calbody())))){
		biWh.setValue(null);
		m_voBill.setWhOut(null);
	}		
			

			// ���˳ɱ�����
			// filterCostObject();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}
	
	public void afterWhInEdit(nc.ui.pub.bill.BillEditEvent e) {
		super.afterWhInEdit(e);
		WhVO voWh = getWhInfoByRef("cinwarehouseid");
		// �����֯����
		nc.ui.pub.bill.BillItem biCalBody = getBillCardPanel()
				.getHeadItem("pk_calbody_out");
		if (biCalBody != null) {
			if (voWh != null){
				biCalBody.setValue(voWh.getPk_calbody());
				m_voBill.setHeaderValue("pk_calbody_out", voWh.getPk_calbody());
			}
			else{
				biCalBody.setValue(null);
				m_voBill.setHeaderValue("pk_calbody_out", null);
			}
			
			String sNewID = null;
			if (voWh != null)
				sNewID = voWh.getPk_calbody();
			// ����˿����֯
			// �����ǰ�Ĳֿⲻ����
			// ���˲ֿ����
			String sConstraint[] = null;
			String sConstraint1[] = null;
			if (sNewID != null) {
				sConstraint = new String[1];
				sConstraint[0] = " AND isdirectstore = 'N' AND pk_calbody='"
						+ sNewID + "'";
				sConstraint1 = new String[1];
				sConstraint1[0] = " and mm_jldoc.gcbm='" + sNewID + "'";
			}
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					"cinwarehouseid");
			RefFilter.filtWh(bi, m_sCorpID, sConstraint);
			
			bi = getBillCardPanel().getHeadItem(
			"coutwarehouseid");
	RefFilter.filtWh(bi, m_sCorpID, sConstraint);
		}
		nc.ui.pub.bill.BillItem biCalBodyname = getBillCardPanel()
				.getHeadItem("vcalbodyname_out");
		if (biCalBodyname != null) {
			if (voWh != null){
				biCalBodyname.setValue(voWh.getVcalbodyname());
				m_voBill.setHeaderValue("vcalbodyname_out", voWh.getVcalbodyname());
			}
			else{
				biCalBodyname.setValue(null);
				m_voBill.setHeaderValue("vcalbodyname_out", null);
			}
		}
	}
	
	public void afterCalbodyInEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			String sNewID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem(e.getKey()).getComponent()).getRefPK();
			// ����˿����֯
			// �����ǰ�Ĳֿⲻ����
			// ���˲ֿ����
			String sConstraint[] = null;
			String sConstraint1[] = null;
			if (sNewID != null) {
				sConstraint = new String[1];
				sConstraint[0] = " AND isdirectstore = 'N' AND pk_calbody='"
						+ sNewID + "'";
				sConstraint1 = new String[1];
				sConstraint1[0] = " and mm_jldoc.gcbm='" + sNewID + "'";
			}
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					"cinwarehouseid");
			RefFilter.filtWh(bi, m_sCorpID, sConstraint);
			// ����:clear warehouse
			WhVO whvo = m_voBill.getWhIn();
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(
					"cinwarehouseid");
			if (biWh != null
					&& (whvo == null || (sNewID != null && !sNewID.equals(whvo
							.getPk_calbody())))){
				biWh.setValue(null);
			m_voBill.setWhIn(null);
			}

			// ���˳ɱ�����
			// filterCostObject();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * �����ߣ������� ���ܣ������������޸�ʱ ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("restriction")
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {

		super.afterEdit(e);
		
		// add by river for 2012-11-12
		if("vbatchcode".equals(e.getKey())) {
			try {
				
				Object coutwarehouseid = getBillCardPanel().getHeadItem("coutwarehouseid").getValueObject();
				Object vbatchcode = getBillCardPanel().getBodyValueAt(e.getRow(), "vbatchcode");
				Object cinventoryid = getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid");
				
				Object nonhandnum = UAPQueryBS.getInstance().executeQuery("select nonhandnum from ic_onhandnum where vlot = '"+vbatchcode+"' and cwarehouseid = '"+coutwarehouseid+"' and cinventoryid = '"+cinventoryid+"'", new ColumnProcessor());
				getBillCardPanel().setBodyValueAt(nonhandnum, e.getRow(), "dshldtransnum");
				
			} catch(Exception ex) {
				Logger.error(ex.getMessage(), ex, this.getClass(), "afterEdit");
			}
		}
		
		if (e.getKey().equals("pk_calbody_out")) { //����ֿ�
			afterCalbodyOutEdit(e);
		} else if (e.getKey().equals("pk_calbody_in")) { //���ֿ�
			afterCalbodyInEdit(e);
		}

		// �Զ�����VO������
		// ********************************************************************//
		String strColName = e.getKey().trim();
		int rownum = e.getRow();
		if (strColName.equals("coutwarehouseid")) {
			// ��������մ�����
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getBodyItem(
					"cinventorycode");
			nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) bi
					.getComponent();
			nc.ui.bd.ref.AbstractRefModel m = invRef.getRefModel();
			nc.ui.pub.bill.BillItem bi1 = getBillCardPanel().getHeadItem(
					"coutwarehouseid");
			nc.ui.pub.beans.UIRefPane invRef1 = (nc.ui.pub.beans.UIRefPane) bi1
					.getComponent();

			String[] o = new String[] { m_sCorpID, invRef1.getRefPK() };
			m.setUserParameter(o);

		}
		// ��������
		if (rownum >= 0) {
			if (strColName.equals(m_sNumItemKey)) {
				calculateByHsl(rownum, m_sNumItemKey, m_sAstItemKey, 0);
			} else if (strColName.equals(m_sAstItemKey)) {
				// �������޸Ĵ���������
				afterNshldtransastnumEdit(e);
			}

		}

	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	protected void initialize() {
		try {
			initVariable();
			super.initialize();

			super.setQuryPlanprice(true);


		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * � ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-1-14 15:13:27) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO[]
	 * @param voSp
	 *            nc.vo.ic.pub.bill.SpecialBillVO[]
	 */
	private GeneralBillVO[] pfVOConvert(SpecialBillVO[] voSp,
			String sSrcBillType, String sDesBillType) {
		GeneralBillVO[] gbvo = null;
		try {
			gbvo = (GeneralBillVO[]) nc.ui.pub.change.PfChangeBO_Client
					.pfChangeBillToBillArray(voSp, sSrcBillType, sDesBillType);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		return gbvo;
	}

	/**
	 * onButtonClicked ����ע�⡣
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) { // finished

		showHintMessage(bo.getName());
		if (bo == m_boOut)
			onOut();
		else if (bo == m_boIn)
			onIn();
		else if (bo == m_boDirectOut)
			onDirectOut();
		else
			super.onButtonClicked(bo);

	}

	public void onAdd() {

		super.onAdd();

		getBillCardPanel().setHeadItem("cshlddiliverdate", m_sLogDate);
		getBillCardPanel().setHeadItem("vshldarrivedate", m_sLogDate);
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-1-25 14:33:10) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setButtonState() {
		super.setButtonState();

		m_boRowQuyQty.setEnabled(false);
		// /////////////
		switch (m_iMode) {
		case BillMode.New: // ����

			m_boDirectOut.setEnabled(false);
			m_boRowQuyQty.setEnabled(true);

			break;
		case BillMode.Update: // �޸�

			m_boDirectOut.setEnabled(false);
			m_boRowQuyQty.setEnabled(true);
			break;
		case BillMode.Browse: // ���

			if (!isCanPressCopyButton()) {
				m_boCopyBill.setEnabled(false);

			}
			m_boDirectOut.setEnabled(m_iTotalListHeadNum > 0);

			break;
		case BillMode.List: // �б�״̬
			if (!isCanPressCopyButton()) {
				m_boCopyBill.setEnabled(false);

			}

			m_boDirectOut.setEnabled(false);
			break;
		}
		// ʹ������Ч
		if (m_aryButtonGroup != null) {
			updateButtons();
		}

		// //////////////
	}

	/**
	 * �����ߣ������� ���ܣ�ת�� ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 2:50) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onIn() {
		if (m_alListData == null || m_alListData.size() < m_iLastSelListHeadRow
				|| m_iLastSelListHeadRow < 0) {

			return;
		}

		ArrayList alInGeneralVO = new ArrayList();
		SpecialBillVO voSp = (SpecialBillVO) m_alListData
				.get(m_iLastSelListHeadRow);
		// GeneralBillVO gbvo = changeFromSpecialVOtoGeneralVO(voSp,
		// InOutFlag.IN);
		// �����ⵥ���VOͨ��VO����ת��Ϊ��ͨ��VO
		GeneralBillVO[] gbvo = pfVOConvert(new SpecialBillVO[] { voSp }, "4K",
				nc.vo.ic.pub.BillTypeConst.m_otherIn);
		if (gbvo == null || gbvo[0] == null) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000118")/* @res "ת��ʧ�ܣ�" */);
			return;
		}
		// ����ʱ��Ӧת��������ϵΪ����������nshouldoutnum ����dshldtransnum - nchecknum ,
		// ����������nshouldoutassistnum����dshldtransnum - nchecknum/hsl
		// ���ʱ��Ӧת��������ϵΪ����������nshouldinnum��>nchecknum-nadjustnum
		// ,����������nneedinassistnum����nchecknum-nadjustnum/hsl
		// if (gbvo[0].getChildrenVO()==null||gbvo[0].getChildrenVO()[0]==null)
		// return;
		// if ( gbvo[0].getChildrenVO()[0].getAttributeValue("nshouldinnum")
		// ==null ||
		// ((UFDouble)gbvo[0].getChildrenVO()[0].getAttributeValue("nshouldinnum")).doubleValue()<=0)
		// {
		// showWarningMessage("��ת��������");
		// return;
		// }
		// ��ȡ�ֿ�����
		WhVO voWh = voSp.getWhIn();

		if (voWh == null) {
			nc.vo.scm.pub.SCMEnv.out("no wh ,data err.");
			return;
		}
		// ��Ҫ�����֯
		String sCalBodyID = voWh.getPk_calbody();
		String sCalBodyName = voWh.getVcalbodyname();
		// -------------- û�еĻ�����һ�� ------------
		if (sCalBodyID == null || sCalBodyID.trim().length() == 0) {
			voWh = getWhInfoByID(voWh.getCwarehouseid());
			if (voWh != null) {
				sCalBodyID = voWh.getPk_calbody();
				sCalBodyName = voWh.getVcalbodyname();
				// fresh bill
				voSp.setWhIn(voWh);
				m_alListData.set(m_iLastSelListHeadRow, voSp);
			}
		}

		gbvo[0].setHeaderValue("pk_calbody", sCalBodyID);
		gbvo[0].setHeaderValue("vcalbodyname", sCalBodyName);

		// �����к�
		// setRowNo(gbvo[0]);
		BillRowNoVO.setVOsRowNoByRule(gbvo, "4A", "crowno");
		alInGeneralVO.add(gbvo[0]);
		// m_dlgInOut= null;
		filterZeroBill(gbvo[0]);
		if (gbvo[0].getItemVOs() == null || gbvo[0].getItemCount() == 0) {
			return;
		}

		getAuditDlg().setIsOK(false); // ����δ����
		getAuditDlg().setSpBill(m_voBill);
		int ret = getAuditDlg(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000119")/* @res "ת�뵥" */, alInGeneralVO,
				null).showModal();
		// 2003-05-09.01 wnj:�����ж�isOKˢ�£���Ϊ�������Ե�X�˳���
		if (ret == nc.ui.pub.beans.UIDialog.ID_OK || getAuditDlg().isOK()) {
			try {
				// ����µı���
				SpecialBillVO voMyTempBill = null; // һ���ı�ͷ��������
				QryConditionVO qcvo = new nc.vo.ic.pub.bill.QryConditionVO();
				qcvo.setQryCond("cbilltypecode='" + m_sBillTypeCode
						+ "' and cspecialhid='"
						+ m_voBill.getPrimaryKey().trim() + "'");
				voMyTempBill = (SpecialBillVO) ((ArrayList) SpecialBillHelper
						.queryBills(m_sBillTypeCode, qcvo)).get(0);
				// ���ⵥû�б��滻���ʣ��䶯hsl���������������
				GenMethod mthod = new GenMethod();
				mthod.calAllConvRate(voMyTempBill.getChildrenVO(), "fixedflag",
						"hsl", "dshldtransnum", "nshldtransastnum", "", "");
				execFormula(voMyTempBill);
				m_alListData.set(m_iLastSelListHeadRow, voMyTempBill.clone());

				m_iFirstSelListHeadRow = -1;
				switchListToBill();
				setButtonState();
				setBillState();
				// ����ת�봰��
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPT40081002-000021")
						+ ResBase.getSuccess()/* @res "ת��ɹ�" */);

			} catch (Exception e) {
				handleException(e);
				nc.ui.pub.beans.MessageDialog
						.showErrorDlg(
								this,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"SCMCOMMON", "UPPSCMCommon-000059")/*
																			 * @res
																			 * "����"
																			 */,
								e.getMessage());
			}
		}
	}

	private void execFormula(SpecialBillVO vo) {
		if (vo == null)
			return;
		ArrayList<SpecialBillVO> al = new ArrayList<SpecialBillVO>();
		al.add(vo);
		getFormulaBillContainer().formulaBill(al, getFormulaItemHeader(),
				getFormulaItemBody());
		// SpecialBillHeaderVO hvo=vo.getHeaderVO();
		// SpecialBillItemVO[] bvos=vo.getItemVOs();
		// if(hvo!=null)
		// getFormulaBillContainer().formulaHeaders(getFormulaItemHeader(),
		// hvo);
		// if(bvos!=null&&bvos.length>0)
		// getFormulaBillContainer().formulaBodys(getFormulaItemBody(), bvos);

	}

	/**
	 * �����ߣ������� ���ܣ�ת�� ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 2:50) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onOut() {
		if (m_alListData == null || m_alListData.size() < m_iLastSelListHeadRow
				|| m_iLastSelListHeadRow < 0) {

			return;
		}

		ArrayList alOutGeneralVO = new ArrayList();
		SpecialBillVO voSp = (SpecialBillVO) m_alListData
				.get(m_iLastSelListHeadRow);
		// GeneralBillVO gbvo = changeFromSpecialVOtoGeneralVO(voSp,
		// InOutFlag.OUT);
		// �����ⵥ���VOͨ��VO����ת��Ϊ��ͨ��VO
		GeneralBillVO[] gbvo = pfVOConvert(new SpecialBillVO[] { voSp }, "4K",
				"4I");
		if (gbvo == null || gbvo[0] == null) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000118")/* @res "ת��ʧ�ܣ�" */);
			return;
		}
		// ��ȡ�ֿ�����
		WhVO voWh = voSp.getWhOut();

		if (voWh == null) {
			nc.vo.scm.pub.SCMEnv.out("no wh ,data err.");
			return;
		}
		// ��Ҫ�����֯
		String sCalBodyID = voWh.getPk_calbody();
		String sCalBodyName = voWh.getVcalbodyname();
		// -------------- û�еĻ�����һ�� ------------
		if (sCalBodyID == null || sCalBodyID.trim().length() == 0) {
			voWh = getWhInfoByID(voWh.getCwarehouseid());
			if (voWh != null) {
				sCalBodyID = voWh.getPk_calbody();
				sCalBodyName = voWh.getVcalbodyname();
				// fresh bill
				voSp.setWhOut(voWh);
				m_alListData.set(m_iLastSelListHeadRow, voSp);
			}
		}
		gbvo[0].setHeaderValue("pk_calbody", sCalBodyID);
		gbvo[0].setHeaderValue("vcalbodyname", sCalBodyName);
		gbvo[0].setHeaderValue("vshldarrivedate", voSp
				.getHeaderValue("vshldarrivedate"));

		// �����к�
		// setRowNo(gbvo[0]);
		BillRowNoVO.setVOsRowNoByRule(gbvo, "4I", "crowno");
		alOutGeneralVO.add(gbvo[0]);

		filterZeroBill(gbvo[0]);
		if (gbvo[0].getItemVOs() == null || gbvo[0].getItemCount() == 0) {
			return;
		}
		getAuditDlg().setIsOK(false); // ����δ����
		getAuditDlg().setSpBill(m_voBill);
		int ret = getAuditDlg(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000120")/* @res "ת����" */, null,
				alOutGeneralVO).showModal();
		// 2003-05-09.01 wnj:�����ж�isOKˢ�£���Ϊ�������Ե�X�˳���
		if (ret == nc.ui.pub.beans.UIDialog.ID_OK || getAuditDlg().isOK()) {
			try {
				// ����µı���
				SpecialBillVO voMyTempBill = null; // һ���ı�ͷ��������
				QryConditionVO qcvo = new nc.vo.ic.pub.bill.QryConditionVO();
				qcvo.setQryCond("cbilltypecode='" + m_sBillTypeCode
						+ "' and cspecialhid='"
						+ m_voBill.getPrimaryKey().trim() + "'");
				// voMyTempBill= (SpecialBillVO)
				// SpecialHBO_Client.queryBills(qcvo).get(0);
				voMyTempBill = (SpecialBillVO) ((ArrayList) SpecialBillHelper
						.queryBills(m_sBillTypeCode, qcvo)).get(0);

				// ���ⵥû�б��滻���ʣ��䶯hsl���������������
				GenMethod mthod = new GenMethod();
				mthod.calAllConvRate(voMyTempBill.getChildrenVO(), "fixedflag",
						"hsl", "dshldtransnum", "nshldtransastnum", "", "");
				execFormula(voMyTempBill);
				m_alListData.set(m_iLastSelListHeadRow, voMyTempBill.clone());

				m_iFirstSelListHeadRow = -1;
				switchListToBill();
				setButtonState();
				setBillState();
				// ����ת������
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPT40081002-000022")
						+ ResBase.getSuccess()/* @res "ת���ɹ�" */);

			} catch (Exception e) {
				handleException(e);
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, null, e
						.getMessage());
			}
		}
		// }
	}

	/**
	 * �����ߣ������� ���ܣ���ʼ����ť ������ ���أ� ���⣺ ���ڣ�(2001-5-15 ���� 3:12) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * �޸��ˣ������� �޸����ڣ�2008-4-3����10:44:56 �޸�ԭ�������������ҳ����ҳ����ҳ��ĩҳ
	 */
	protected void initButtons() {
		m_boAdd = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"common", "UC001-0000002")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000308")/* @res "���ӵ���" */, 0, "����"); /*-=notranslate=-*/
		m_boChange = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000045")/* @res "�޸�" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000291")/* @res "�޸ĵ���" */, 0, "�޸�"); /*-=notranslate=-*/
		m_boDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000039")/* @res "ɾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000504")/* @res "ɾ������" */, 0, "ɾ��"); /*-=notranslate=-*/
		m_boCopyBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000043")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000505")/* @res "���Ƶ���" */, 0, "����"); /*-=notranslate=-*/
		m_boJointAdd = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000003")/* @res "ҵ������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000506")/* @res "ѡ���������ݷ�ʽ" */, 0, "ҵ������"); /*-=notranslate=-*/
		m_boSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000001")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000001")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000008")/* @res "ȡ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000008")/* @res "ȡ��" */, 0, "ȡ��"); /*-=notranslate=-*/

		m_boAddRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000012")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000012")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boDeleteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000013")/* @res "ɾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000013")/* @res "ɾ��" */, 0, "ɾ��"); /*-=notranslate=-*/
		m_boInsertRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000016")/* @res "������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000016")/* @res "������" */, 0, "������"); /*-=notranslate=-*/
		m_boCopyRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000014")/* @res "������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000014")/* @res "������" */, 0, "������"); /*-=notranslate=-*/
		m_boPasteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000015")/* @res "ճ����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000015")/* @res "ճ����" */, 0, "ճ����"); /*-=notranslate=-*/
		m_boPasteRowTail = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008bill", "UPP4008bill-000556")/* @res "ճ���е���β" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
						"UPP4008bill-000556")/* @res "ճ���е���β" */, 0, "ճ���е���β"); /*-=notranslate=-*/
		m_boNewRowNo = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008bill", "UPP4008bill-000551")/* @res "�����к�" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
						"UPP4008bill-000551")/* @res "�����к�" */, 0, "�����к�"); /*-=notranslate=-*/
    m_boLineCardEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "SCMCOMMONIC55YB002")/* @res "��Ƭ�༭" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
            "SCMCOMMONIC55YB002")/* @res "��Ƭ�༭" */, 0, "��Ƭ�༭"); /*-=notranslate=-*/


		m_boAuditBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000027")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000027")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boCancelAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000028")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000028")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000006")/* @res "��ѯ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000006")/* @res "��ѯ" */, 0, "��ѯ"); /*-=notranslate=-*/
		// 2003-05-03����
		m_boJointCheck = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("SCMCOMMON", "UPPSCMCommon-000145")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000145")/* @res "����" */, 0, "����"); /*-=notranslate=-*/

		m_boLocate = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPPSCMCommon-000089")/* @res "��λ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000089")/* @res "��λ" */, 0, "��λ"); /*-=notranslate=-*/
		m_PrintMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000007")/* @res "��ӡ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000007")/* @res "��ӡ" */, 0, "��ӡ����"); /*-=notranslate=-*/
		m_boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000007")/* @res "��ӡ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000007")/* @res "��ӡ" */, 0, "��ӡ"); /*-=notranslate=-*/
		m_boPreview = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPPSCMCommon-000305")/* @res "Ԥ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000305")/* @res "Ԥ��" */, 0, "Ԥ��"); /*-=notranslate=-*/
		{
			m_PrintMng.addChildButton(m_boPrint);
			m_PrintMng.addChildButton(m_boPreview);
		}
		m_boList = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("SCMCOMMON", "UPPSCMCommon-000186")/* @res "�л�" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000186")/* @res "�л�" */, 0, "�л�"); /*-=notranslate=-*/

		m_boOut = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4008spec", "UPT40081002-000022")/* @res "ת��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081002-000022")/* @res "ת��" */, 0, "ת��"); /*-=notranslate=-*/
		m_boIn = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4008spec", "UPT40081002-000021")/* @res "ת��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081002-000021")/* @res "ת��" */, 0, "ת��"); /*-=notranslate=-*/

		m_billMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPT40080816-000037")/* @res "����ά��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000074")/* @res "����ά������" */, 0, "����ά��"); /*-=notranslate=-*/
		m_billRowMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000011")/* @res "�в���" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000011")/* @res "�в���" */, 0, "�в���"); /*-=notranslate=-*/

		m_boRowQuyQty = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000481")/* @res "����ת��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000481")/* @res "����ת��" */, 0, "����ת��"); /*-=notranslate=-*/
		m_boDirectOut = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPT40081002-000024")/* @res "ֱ��ת��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000507")/* @res "�Զ�������������ⵥ" */, 0, "ֱ��ת��"); /*-=notranslate=-*/
		// ���·�ҳ�Ŀ���
		m_pageBtn = new PageCtrlBtn(this);
		m_boBrowse = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000021")/* @res "���" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000021")/* @res "���" */, 0, "���"); /*-=notranslate=-*/
		/*
		 * m_boTop = new
		 * ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000059")@res
		 * "��ҳ",
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000059")@res
		 * "��ҳ", 0,"��ҳ"); -=notranslate=- m_boPrevious = new
		 * ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","SCMCOMMON000000163")@res
		 * "��ҳ",
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","SCMCOMMON000000163")@res
		 * "��ҳ", 0,"��ҳ"); -=notranslate=- m_boNext = new
		 * ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000023")@res
		 * "��ҳ",
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000023")@res
		 * "��ҳ", 0,"��ҳ"); -=notranslate=- m_boBottom = new
		 * ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000062")@res
		 * "ĩҳ",
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000062")@res
		 * "ĩҳ", 0,"ĩҳ"); -=notranslate=-
		 *//*m_boTop = m_pageBtn.getFirst();
		m_boPrevious = m_pageBtn.getPre();
		m_boNext = m_pageBtn.getNext();
		m_boBottom = m_pageBtn.getLast();*/

		m_billMng.addChildButton(m_boAdd);
		m_billMng.addChildButton(m_boDelete);
		m_billMng.addChildButton(m_boChange);
		m_billMng.addChildButton(m_boCopyBill);
		m_billMng.addChildButton(m_boSave);
		m_billMng.addChildButton(m_boCancel);

		m_billRowMng.addChildButton(m_boAddRow);
		m_billRowMng.addChildButton(m_boDeleteRow);
		m_billRowMng.addChildButton(m_boInsertRow);
		m_billRowMng.addChildButton(m_boCopyRow);
		m_billRowMng.addChildButton(m_boPasteRow);
		m_billRowMng.addChildButton(m_boPasteRowTail);
		m_billRowMng.addChildButton(m_boNewRowNo);
    m_billRowMng.addChildButton(m_boLineCardEdit);

		m_boBrowse.addChildButton(m_pageBtn.getFirst());
		m_boBrowse.addChildButton(m_pageBtn.getPre());
		m_boBrowse.addChildButton(m_pageBtn.getNext());
		m_boBrowse.addChildButton(m_pageBtn.getLast());

		m_aryButtonGroup = new ButtonObject[] { m_billMng, m_billRowMng,
				m_boOut, m_boIn, m_boDirectOut, m_boQuery, m_boBrowse,
				m_boJointCheck, m_boRowQuyQty, m_boLocate, m_PrintMng, m_boList };

	}

	/**
	 * �����ߣ������� ���ܣ���ʼ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 6:27) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void initVariable() {
		// ����
		/** ת�� */
		m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_transfer;
		m_sBillCode = "IC_BILL_TEMPLET_004K";
		m_sPNodeCode = "40081002";
		// sSpecialHBO_Client = "nc.ui.ic.ic221.SpecialHBO_Client";
		m_iFirstAddRows = 1;

	}

	/**
	 * �����ߣ������� ���ܣ�VOУ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean checkVO() {
		return super.checkVO();
	}

	/**
	 * �˴����뷽��˵���� �����ߣ������� ���ܣ���ʼ����ͷ�����еĲ��� ������ ���أ� ���⣺ ���ڣ�(2001-7-17 10:33:20)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void filterRef(String cropid) {
		super.filterRef(cropid);
		// ���嵥λ������˴����Ϊ�Ƿ����,�������������ۿ۴��
		BillItem bi = getBillCardPanel().getBodyItem("cinventorycode");
		if (bi != null && bi.getComponent() != null) {
			// ���˴������

			nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) bi
					.getComponent();
			invRef.setTreeGridNodeMultiSelected(true);
			invRef.setMultiSelectedEnabled(true);
			// ���������ʾ�ִ���
			invRef.getRefModel().setIsDynamicCol(true);
			invRef.getRefModel().setDynamicColClassName(
					"nc.ui.ic.ic221.RefOnhandDynamic");

			invRef
					.setWhereString(" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'  "
							+ "and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='"
							+ cropid + "'");
		}

	}

	/**
	 * �����ߣ������� ���ܣ��ɴ���ĵ������͡��ֶΣ���õ����ֶθı��Ӧ�ı�������ֶ��б� ������iBillFlag
	 * �������ͣ���Ϊ��ͨ���ݣ�����0����Ϊ���ⵥ�ݣ�����1 ���� ��� cinventorycode�� ����ֿ� cwarehousename�� ������
	 * vfree0�� ��ͷ����ֿ� coutwarehouseid�� ��ͷ�ֿ� cwarehouseid ���أ� ���⣺ ���ڣ�(2001-7-18
	 * ���� 9:20) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String[]
	 * @param sWhatChange
	 *            java.lang.String
	 * 
	 */
	// protected String[] getClearIDs(int iBillFlag, String sWhatChange) {
	// if (sWhatChange == null)
	// return null;
	//  
	// String[] sReturnString = null;
	// sWhatChange = sWhatChange.trim();
	// if (sWhatChange.equals("cinventorycode")) {
	// //���
	// sReturnString = new String[6];
	// sReturnString[0] = "vbatchcode";
	// sReturnString[1] = "vfree0";
	// sReturnString[2] = m_sNumItemKey;
	// sReturnString[3] = m_sAstItemKey;
	// //sReturnString[4]= "castunitid";
	// //sReturnString[5]= "castunitname";
	// //sReturnString[6]= "hsl";
	// sReturnString[4] = "scrq";
	// sReturnString[5] = "dvalidate";
	// } else if ((sWhatChange.equals("cwarehousename")) && (iBillFlag == 1)) {
	// //���ⵥ�ı������ڲֿ�
	// sReturnString = new String[6];
	// sReturnString[0] = "vbatchcode";
	// sReturnString[2] = m_sNumItemKey;
	// sReturnString[3] = m_sAstItemKey;
	// sReturnString[4] = "scrq";
	// sReturnString[5] = "dvalidate";
	// //showWarningMessage("������ȷ�����κţ�");
	// return null;
	// } else if (sWhatChange.equals("vfree0")) {
	// //������
	// sReturnString = new String[3];
	// sReturnString[0] = "vbatchcode";
	// //sReturnString[1]= m_sNumItemKey;
	// //sReturnString[2]= m_sAstItemKey;
	// sReturnString[1] = "scrq";
	// sReturnString[2] = "dvalidate";
	// //showWarningMessage("������ȷ�����κţ�");
	// //�޸��ˣ������� �޸����ڣ�2007-8-21����02:45:36 �޸�ԭ���޸�������ʱ�����κ�
	// //return null;
	// } else if (sWhatChange.equals("coutwarehouseid")) {
	// sReturnString = new String[5];
	// sReturnString[0] = "vbatchcode";
	// sReturnString[1] = m_sNumItemKey;
	// sReturnString[2] = m_sAstItemKey;
	// sReturnString[3] = "scrq";
	// sReturnString[4] = "dvalidate";
	// //showWarningMessage("������ȷ�����κţ�");
	// return null;
	// } else if ((sWhatChange.equals("cwarehouseid")) && (iBillFlag == 0)) {
	// sReturnString = new String[5];
	// sReturnString[0] = "vbatchcode";
	// sReturnString[1] = m_sNumItemKey;
	// sReturnString[2] = m_sAstItemKey;
	// sReturnString[3] = "scrq";
	// sReturnString[4] = "dvalidate";
	// //showWarningMessage("������ȷ�����κţ�");
	// return null;
	// }
	// return sReturnString;
	// }
	ButtonObject m_boDirectOut = null;

	/**
	 * ClientUI ������ע�⡣ nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
	 * 
	 */
	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super();
		// ��ʼ������
		initialize(pk_corp, operator, "jc", businessType, "0001", "2003-04-17");
		// �鵥��
		SpecialBillVO voBill = qryBill(pk_corp, billType, businessType,
				operator, billID);
		if (voBill == null)
			nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "��ʾ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000121")/* @res "û�з��ϲ�ѯ�����ĵ��ݣ�" */);
		else
			// ��ʾ����
			setBillValueVO(voBill);

	}

	/**
	 * �༭ǰ����,ֻ�ڵ���ģ���ʼ��ʱ���ã�������������billitemkey�༭���ԡ� �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		return true;
		// nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("beforeEdit:" + e.getKey());
		// boolean isEditable = super.beforeEdit(e);
		// BillItem bi = getBillCardPanel().getBodyItem(e.getKey());
		// if (isEditable) {
		// if (e.getKey().equals("cinventorycode")) {
		//
		// nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane)
		// bi.getComponent();
		// nc.ui.bd.ref.AbstractRefModel m = invRef.getRefModel();
		// if (getBillCardPanel().getHeadItem("coutwarehouseid") != null) {
		// String[] o =
		// new String[] { m_sCorpID,
		// getBillCardPanel().getHeadItem("coutwarehouseid").getValue()};
		// m.setUserParameter(o);
		// }
		// }
		// //�����κŴ��ݲ���
		// else if (e.getKey().equals("vbatchcode")) {
		// WhVO wvo = m_voBill.getWhOut();
		// getLotNumbRefPane().setParameter(wvo,
		// m_voBill.getItemInv(e.getRow()));
		// } else if (e.getKey().equals("dvalidate") ||
		// e.getKey().equals("scrq")) {
		//
		// isEditable = false;
		//
		// }
		// }
		// bi.setEnabled(isEditable);
		// return isEditable;

	}

	/**
	 * ÿѡ��һ��BillItemʱ���ã���billModel�Զ�����
	 */

	public boolean isCellEditable(boolean value, int row, String itemkey) {
		boolean isEditable = super.isCellEditable(value, row, itemkey);
		BillItem bi = getBillCardPanel().getBodyItem(itemkey);
		if (isEditable) {
			if (itemkey.equals("cinventorycode")) {

				nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) bi
						.getComponent();
				nc.ui.bd.ref.AbstractRefModel m = invRef.getRefModel();
				if (getBillCardPanel().getHeadItem("coutwarehouseid") != null) {
					String[] o = new String[] {
							m_sCorpID,
							getBillCardPanel().getHeadItem("coutwarehouseid")
									.getValue() };
					m.setUserParameter(o);
				}
			}
			// �����κŴ��ݲ���
			else if (itemkey.equals("vbatchcode")) {
				WhVO wvo = m_voBill.getWhOut();
				getLotNumbRefPane().setParameter(wvo, m_voBill.getItemInv(row));
			}
		}
		bi.setEnabled(isEditable);
		return isEditable;

	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-8-16 12:53:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voBillHeader
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param svo
	 *            nc.vo.ic.pub.bill.SpecialBillVO
	 * @param iInOutFlag
	 *            int
	 */
	protected GeneralBillHeaderVO changeFromSpecialVOtoGeneralVOAboutHeader(
			GeneralBillVO gvo, SpecialBillVO svo, int iInOutFlag) {

		if (iInOutFlag == InOutFlag.OUT) {
			gvo.setWh(svo.getWhOut());
		} else {
			gvo.setWh(svo.getWhIn());
		}

		GeneralBillHeaderVO voBillHeader = super
				.changeFromSpecialVOtoGeneralVOAboutHeader(gvo, svo, iInOutFlag);
		SpecialBillHeaderVO voSpHeader = svo.getHeaderVO();
		// ���Ի�����
		if (iInOutFlag == InOutFlag.OUT) {
			voBillHeader.setCdptid(voSpHeader.getCoutdeptid());
		} else {
			voBillHeader.setCdptid(voSpHeader.getCindeptid());
		}
		if (iInOutFlag == InOutFlag.OUT) {
			voBillHeader.setCdptname(voSpHeader.getCoutdeptname());
		} else {
			voBillHeader.setCdptname(voSpHeader.getCindeptname());
		}
		if (iInOutFlag == InOutFlag.OUT) {
			voBillHeader.setCbizid(voSpHeader.getCoutbsor());
		} else {
			voBillHeader.setCbizid(voSpHeader.getCinbsrid());
		}
		if (iInOutFlag == InOutFlag.OUT) {
			voBillHeader.setCbizname(voSpHeader.getCoutbsorname());
		} else {
			voBillHeader.setCbizname(voSpHeader.getCinbsrname());
		}
		return voBillHeader;
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-8-16 12:53:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param gbvo
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sbvo
	 *            nc.vo.ic.pub.bill.SpecialBillVO
	 * @param iInOutFlag
	 *            int
	 */
	protected GeneralBillItemVO changeFromSpecialVOtoGeneralVOAboutItem(
			GeneralBillVO gbvo, SpecialBillVO sbvo, int iInOutFlag, int j) {

		// GeneralBillItemVO voItem =
		super
				.changeFromSpecialVOtoGeneralVOAboutItem(gbvo, sbvo,
						iInOutFlag, j);
		// ������ֵ
		UFDouble ufdTotal = (sbvo.getItemValue(j, "dshldtransnum") == null ? ZERO
				: (UFDouble) sbvo.getItemValue(j, "dshldtransnum"));
		// ����ֵ
		UFDouble ufdAlreadyIn = (sbvo.getItemValue(j, "nadjustnum") == null ? ZERO
				: (UFDouble) sbvo.getItemValue(j, "nadjustnum"));
		// �۳�ֵ
		UFDouble ufdAlreadyOut = (sbvo.getItemValue(j, "nchecknum") == null ? ZERO
				: (UFDouble) sbvo.getItemValue(j, "nchecknum"));
		// ������
		UFDouble ufdHsl = (sbvo.getItemValue(j, "hsl") == null
				|| sbvo.getItemValue(j, "hsl").toString().trim().length() == 0 ? ZERO
				: (UFDouble) sbvo.getItemValue(j, "hsl"));

		// ����ʱ��Ӧת��������ϵΪ����������nshouldoutnum ����dshldtransnum - nchecknum ,
		// ����������nshouldoutassistnum����dshldtransnum - nchecknum/hsl
		// ���ʱ��Ӧת��������ϵΪ����������nshouldinnum��>nchecknum-nadjustnum
		// ,����������nneedinassistnum����nchecknum-nadjustnum/hsl
		if (iInOutFlag == InOutFlag.OUT) {
			gbvo.setItemValue(j, "nshouldoutnum", ufdTotal.sub(ufdAlreadyOut));
			if (sbvo.getItemValue(j, "nshldtransastnum") != null
					&& sbvo.getItemValue(j, "nshldtransastnum").toString()
							.trim().length() != 0 && ufdHsl.doubleValue() != 0) {
				gbvo.setItemValue(j, "nshouldoutassistnum", ufdTotal.sub(
						ufdAlreadyOut).div(ufdHsl));
			}
			// 2004-10-13 ����Ҫ����ʵ������
			// gbvo.setItemValue(j, "noutnum", ufdTotal.sub(ufdAlreadyOut));

			gbvo.setItemValue(j, "ninnum", null);
			if (sbvo.getItemValue(j, "nshldtransastnum") != null
					&& sbvo.getItemValue(j, "nshldtransastnum").toString()
							.trim().length() != 0 && ufdHsl.doubleValue() != 0) {
				gbvo.setItemValue(j, "noutassistnum", ufdTotal.sub(
						ufdAlreadyOut).div(ufdHsl));
				gbvo.setItemValue(j, "ninassistnum", null);
			}

		} else {
			// gbvo.setItemValue(j, sBodyItemKeyName[i],
			// ufdTotal.sub(ufdAlreadyIn));
			// �����������Ǵ�������:ת��,�������������Ϊ��;����(�ۼƳ�-�ۼ���)
			gbvo.setItemValue(j, "nshouldinnum", ufdAlreadyOut
					.sub(ufdAlreadyIn));
			if (sbvo.getItemValue(j, "nshldtransastnum") != null
					&& sbvo.getItemValue(j, "nshldtransastnum").toString()
							.trim().length() != 0 && ufdHsl.doubleValue() != 0) {
				// gbvo.setItemValue(j, sBodyItemKeyName[i],
				// ufdTotal.sub(ufdAlreadyIn).div(ufdHsl));
				// �����������Ǵ�������:ת��,�������������Ϊ��;����(�ۼƳ�-�ۼ���)
				gbvo.setItemValue(j, "nneedinassistnum", ufdAlreadyOut.sub(
						ufdAlreadyIn).div(ufdHsl));

			}

			// gbvo.setItemValue(j, sBodyItemKeyName[i],
			// ufdTotal.sub(ufdAlreadyIn));
			// �����������Ǵ�������:ת��,�������������Ϊ��;����(�ۼƳ�-�ۼ���)
			// 2004-10-13 ����Ҫ����ʵ������
			// gbvo.setItemValue(j, "ninnum", ufdAlreadyOut.sub(ufdAlreadyIn));
			gbvo.setItemValue(j, "noutnum", null);
			if (sbvo.getItemValue(j, "nshldtransastnum") != null
					&& sbvo.getItemValue(j, "nshldtransastnum").toString()
							.trim().length() != 0 && ufdHsl.doubleValue() != 0) {

				// gbvo.setItemValue(j, sBodyItemKeyName[i],
				// ufdTotal.sub(ufdAlreadyIn).div(ufdHsl));
				// �����������Ǵ�������:ת��,�������������Ϊ��;����(�ۼƳ�-�ۼ���)
				gbvo.setItemValue(j, "ninassistnum", ufdAlreadyOut.sub(
						ufdAlreadyIn).div(ufdHsl));
				gbvo.setItemValue(j, "noutassistnum", null);
			}
		}
		// 2004-10-13 ����Ҫ����ʵ������
		gbvo.setItemValue(j, "dbizdate", null);
		return (GeneralBillItemVO) gbvo.getItemVOs()[j];
	}

	/**
	 * �򵥳�ʼ���ࡣ����������������������õĲ���Ա����˾�ȡ�
	 */
	/* ���棺�˷������������ɡ� */
	protected void initialize(String pk_corp, String sOperatorid,
			String sOperatorname, String sBiztypeid, String sGroupid,
			String sLogDate) {
		try {
			initVariable();
			super.initialize(pk_corp, sOperatorid, sOperatorname, sBiztypeid,
					sGroupid, sLogDate);
			// �޸��ˣ������� �޸����ڣ�2007-12-26����11:05:02 �޸�ԭ���Ҽ�����"�����к�"
			UIMenuItem[] oldUIMenuItems = getBillCardPanel().getBodyMenuItems();
			if (oldUIMenuItems.length > 0) {
				ArrayList<UIMenuItem> newMenuList = new ArrayList<UIMenuItem>();
				for (UIMenuItem oldUIMenuItem : oldUIMenuItems)
					newMenuList.add(oldUIMenuItem);
				newMenuList.add(getAddNewRowNoItem());
        newMenuList.add(getLineCardEditItem());
				getAddNewRowNoItem().removeActionListener(this);
				getAddNewRowNoItem().addActionListener(this);
        getLineCardEditItem().removeActionListener(this);
        getLineCardEditItem().addActionListener(this);
				UIMenuItem[] newUIMenuItems = new UIMenuItem[newMenuList.size()];
				m_Menu_AddNewRowNO_Index = newMenuList.size() - 1;
				newMenuList.toArray(newUIMenuItems);
				// getBillCardPanel().setBodyMenu(newUIMenuItems);
				getBillCardPanel().getBodyPanel().setMiBody(newUIMenuItems);
				getBillCardPanel().getBodyPanel().setBBodyMenuShow(true);
				getBillCardPanel().getBodyPanel().addTableBodyMenu();

			}
			
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}

	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-1-25 14:38:12) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 */
	protected boolean isCanPressCopyButton() {

		if (m_alListData != null && m_iLastSelListHeadRow >= 0
				&& m_iLastSelListHeadRow < m_alListData.size()
				&& m_alListData.get(m_iLastSelListHeadRow) != null) {
			SpecialBillVO vo = (SpecialBillVO) m_alListData
					.get(m_iLastSelListHeadRow);
			SpecialBillItemVO voaItems[] = vo.getItemVOs();
			if (voaItems != null && voaItems.length > 0 && voaItems[0] != null) {
				Object obj = voaItems[0].getCsourcebillhid();
				if (obj == null || obj.toString().trim().length() == 0)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	/**
	 * �����ߣ������� ���ܣ�ָ��Ϊ�Ǹ��� ������ ���أ� ���⣺ ���ڣ�(2001-12-5 14:31:47) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param sFieldCode
	 *            java.lang.String
	 * @param bcp
	 *            nc.ui.pub.bill.BillCardPanel
	 * @param vo
	 *            nc.vo.ic.pub.bill.SpecialBillVO
	 */
	protected void mustNoNegative(String sFieldCode, int iRow,
			BillCardPanel bcp, SpecialBillVO vo) {
		// 2002-10-18.01:���˾�:ת�ⵥ��Ϊ����
		// do nothing here.
	}

	/**
	 * � ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-5-12 11:13:49) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voHead
	 *            nc.vo.ic.pub.bill.SpecialBillHeaderVO
	 */
	protected void qryCalbodyByWhid(SpecialBillHeaderVO voHead) {

		String sWhID = voHead.getCoutwarehouseid();
		String sCalID = null;
		try {
			if (sCalID == null && sWhID != null) {
				sCalID = (String) ((Object[]) nc.ui.scm.pub.CacheTool
						.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody",
								sWhID))[0];
				((SpecialBillHeaderVO) voHead).setPk_calbody_in(sCalID);
				((SpecialBillHeaderVO) voHead).setPk_calbody_out(sCalID);

			}
		} catch (Exception e1) {
			nc.vo.scm.pub.SCMEnv.error(e1);
		}

	}

	/**
	 * �����ߣ������� ���ܣ�ֱ��ת�� ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 2:50) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 2004-01-05 hanwei ֱ��ת������ת�����������кŴ��
	 */
	public void onDirectOut() {
		if (m_alListData == null || m_alListData.size() < m_iLastSelListHeadRow
				|| m_iLastSelListHeadRow < 0 || m_iMode != BillMode.Browse) {

			return;
		}

		// ����ת������
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPT40081002-000022")/* @res "ת��" */);

		ArrayList alOutGeneralVO = new ArrayList();
		SpecialBillVO voSp = (SpecialBillVO) m_alListData
				.get(m_iLastSelListHeadRow);
		// �����ⵥ���VOͨ��VO����ת��Ϊ��ͨ��VO
		GeneralBillVO[] gbvo = pfVOConvert(new SpecialBillVO[] { voSp }, "4K",
				"4I");
		if (gbvo == null || gbvo[0] == null) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000118")/* @res "ת��ʧ�ܣ�" */);
			return;
		}
		// //�����к�
		// setRowNo(gbvo[0]);
		// alOutGeneralVO.add(gbvo[0]);

		// ���
		ArrayList alInGeneralVO = new ArrayList();
		GeneralBillVO[] gbvo1 = pfVOConvert(new SpecialBillVO[] { voSp }, "4K",
				"4A");

		if (gbvo1 == null || gbvo1[0] == null) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000118")/* @res "ת��ʧ�ܣ�" */);
			return;
		}

		// ������ĵ���ת������ⵥ����
		for (int i = 0; i < gbvo[0].getItemCount(); i++) {
			gbvo1[0].setItemValue(i, "ninnum", gbvo[0].getItemValue(i,
					"noutnum"));
			gbvo1[0].setItemValue(i, "ninassistnum", gbvo[0].getItemValue(i,
					"noutassistnum"));
			gbvo1[0].setItemValue(i, "nshouldinnum", gbvo[0].getItemValue(i,
					"nshouldoutnum"));
			gbvo1[0].setItemValue(i, "nneedinassistnum", gbvo[0].getItemValue(
					i, "nshouldoutassistnum"));

		}

		// ����ת��
		filterZeroBill(gbvo[0]);
		if (gbvo[0].getItemVOs() == null || gbvo[0].getItemCount() == 0) {
			return;
		}
		// �����к�
		setRowNo(gbvo[0]);
		alOutGeneralVO.add(gbvo[0]);

		// ����ת��
		filterZeroBill(gbvo1[0]);
		if (gbvo1[0].getItemVOs() == null || gbvo1[0].getItemCount() == 0) {
			return;
		}
		// �����к�
		setRowNo(gbvo1[0]);
		alInGeneralVO.add(gbvo1[0]);

		getAuditDlg().setIsOK(false); // ����δ����
		getAuditDlg().setSpBill(m_voBill);
		getAuditDlg().setVO4Direct(alInGeneralVO, alOutGeneralVO,
				BillTypeConst.m_transfer, m_voBill.getPrimaryKey().trim(),
				m_sCorpID, m_sUserID);
		// ֱ��ת������ת�����������кŴ�� 2004-01-05 hanwei
		if (nc.ui.ic.pub.bill.GeneralBillUICtl.isSeriaoInvNegative(gbvo[0])) {
			nc.ui.pub.beans.MessageDialog
					.showErrorDlg(
							this,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"SCMCOMMON", "UPPSCMCommon-000059")/*
																		 * @res
																		 * "����"
																		 */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000122")/*
																		 * @res
																		 * "ֱ��ת������£����кŹ����Ĵ����Ӧ����������С��0!"
																		 */);
			return;
		}

		int ret = getAuditDlg().showModal();
		// 2003-05-09.01 wnj:�����ж�isOKˢ�£���Ϊ�������Ե�X�˳���
		if (ret == nc.ui.pub.beans.UIDialog.ID_OK || getAuditDlg().isOK()) {
			try {
				// ����µı���
				SpecialBillVO voMyTempBill = null; // һ���ı�ͷ��������
				QryConditionVO qcvo = new nc.vo.ic.pub.bill.QryConditionVO();
				qcvo.setQryCond("cbilltypecode='" + m_sBillTypeCode
						+ "' and cspecialhid='"
						+ m_voBill.getPrimaryKey().trim() + "'");
				voMyTempBill = (SpecialBillVO) ((ArrayList) SpecialBillHelper
						.queryBills(m_sBillTypeCode, qcvo)).get(0);
				// ���ⵥû�б��滻���ʣ��䶯hsl���������������
				GenMethod mthod = new GenMethod();
				mthod.calAllConvRate(voMyTempBill.getChildrenVO(), "fixedflag",
						"hsl", "dshldtransnum", "nshldtransastnum", "", "");
				execFormula(voMyTempBill);
				// �޸��ˣ������� �޸����ڣ�2007-11-14����04:53:00 �޸�ԭ��ִ�����κŵ�����ʽ
				BatchCodeDefSetTool.execFormulaForBatchCode(voMyTempBill
						.getItemVOs());

				m_alListData.set(m_iLastSelListHeadRow, voMyTempBill.clone());

				m_iFirstSelListHeadRow = -1;
				switchListToBill();
				setButtonState();
				setBillState();
			} catch (Exception e) {
				getAuditDlg().setIsDirectOut(false);
				handleException(e);
				nc.ui.pub.beans.MessageDialog
						.showErrorDlg(
								this,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"SCMCOMMON", "UPPSCMCommon-000059")/*
																			 * @res
																			 * "����"
																			 */,
								e.getMessage());
			}
		}
		getAuditDlg().setIsDirectOut(false);

	}

	/**
	 * V501�������ܣ���������Ϊ��ļ�¼ zhanghaiyan 2006-12-29
	 */
	private void filterZeroBill(GeneralBillVO vo) {
		GeneralBillItemVO[] voItems = vo.getItemVOs();

		Vector<GeneralBillItemVO> v = new Vector<GeneralBillItemVO>();
		if (voItems != null && voItems.length > 0) {
			for (int i = 0; i < voItems.length; i++) {
				if (!GenMethod.isNotEqualsZero(voItems[i].getNshouldinnum())
						&& !GenMethod.isNotEqualsZero(voItems[i]
								.getNshouldoutnum()))
					continue;
				v.add(voItems[i]);
			}
		}
		voItems = null;
		if (v != null || v.size() > 0) {
			for (int i = 0; i < v.size(); i++) {
				voItems = new GeneralBillItemVO[v.size()];
				v.copyInto(voItems);
			}
		}
		vo.setChildrenVO(voItems);
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-29 ����11:17:20 ����ԭ�� ֧��ѡ���������Ĺ���
	 * @param selectVOs
	 */
	public void afterSelectOnhandVOs(nc.vo.ic.pub.InvOnHandVO[] selectVOs){
		if (null != selectVOs && 0 < selectVOs.length){
			Integer iCurRow = getBillCardPanel().getBillTable().getSelectedRow();
			/*if (1 < selectVOs.length)
				nc.ui.scm.pub.report.BillRowNo.addLineRowNos(
						getBillCardPanel(),
						m_sBillTypeCode,
						m_sBillRowNo,selectVOs.length -1);*/
			ArrayList<String> refPkList = new ArrayList<String>();
			for(nc.vo.ic.pub.InvOnHandVO selectVO:selectVOs)
				refPkList.add(selectVO.getCinventoryid());
			String[] refPks = new String[refPkList.size()];
			refPkList.toArray(refPks);
			try{
				//�ֿ�Ϳ����֯��Ϣ
				String sWhID = null;
				String sCalID = null;
				if (getBillCardPanel().getHeadItem(m_sMainWhItemKey) != null) {
					sWhID = getBillCardPanel().getHeadItem(m_sMainWhItemKey).getValue();
				}
				long ITime = System.currentTimeMillis();
			
				if (isQuryPlanprice())
				{
				if (sCalID == null && sWhID != null) {
					
				    sCalID  = (String) ((Object[]) nc.ui.scm.pub.CacheTool.getCellValue(
							"bd_stordoc",
							"pk_stordoc",
							"pk_calbody",
							sWhID))[0];
					
				}
				}
			
				SCMEnv.showTime(ITime, "���������������:");
			
				ITime = System.currentTimeMillis();
				//�������
				InvVO[] invVOs = null;
				if (isQuryPlanprice())
				    invVOs = getInvoInfoBYFormula().getInvParseWithPlanPrice(refPks, sWhID, sCalID,true,true);
				 else
				    invVOs =getInvoInfoBYFormula().getBillQuryInvVOs(refPks,true,true);
			
				SCMEnv.showTime(ITime, "�������:");
			
			
				ITime = System.currentTimeMillis();
				
				for(int i=0;i<invVOs.length;i++){
					invVOs[i].setCastunitid(selectVOs[i].getCastunitid());
					invVOs[i].setVbatchcode(selectVOs[i].getVbatchcode());
					invVOs[i].setCspaceid(selectVOs[i].getCspaceid());
					if (null != invVOs[i].getFreeItemVO()){
						for (int j = 1; j < 10; j++) {
							invVOs[i].setAttributeValue(
							"vfree" + j,
							selectVOs[i].getAttributeValue("vfree" + j));
						}
						invVOs[i].setAttributeValue(
								"vfree0" ,
								invVOs[i].getAttributeValue("vfree0"));
					}
					
				}
				
				afterInvMutiEdit(invVOs,iCurRow);
				
				for (int i = 0 ; i< selectVOs.length;i++){
					if (null != selectVOs[i].getNmaster())
						getBillCardPanel().setBodyValueAt(selectVOs[i].getNmaster(), iCurRow + i, "dshldtransnum");
					if (null != selectVOs[i].getNassist())
						getBillCardPanel().setBodyValueAt(selectVOs[i].getNassist(), iCurRow + i, "nshldtransastnum");
				}

			}catch(Exception e1){
				showErrorMessage(e1.getMessage());
			
			}
			
		}
	}
	
	protected nc.ui.ic.pub.InvOnHandDialog getIohdDlg() {
		if (null == m_iohdDlg) {
			m_iohdDlg= new InvOnHandDialog(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
			"UPPSCMCommon-000481")/* @res "����ת��" */,m_sBillTypeCode);
		}
		return m_iohdDlg;
	}
  
  protected QueryDlgHelpForSpec getQueryHelp() {
    if(this.m_queryHelp==null){
      this.m_queryHelp = new QueryDlgHelpForSpec(this){
        protected void init() {
          super.init();
          getQueryDlg().setLogFields(new String[]{
              "qbillstatus",
              "boutnumnull",
              "freplenishflag",
              "dbilldate.from",
              "dbilldate.end",
              "head.pk_calbody",
              "pk_calbody",
              "head.cwarehouseid",
              "cwarehouseid",
              "pk_corp",
              "head.pk_corp",
              "coutwarehouseid",
              "cinwarehouseid",
              "coutdeptid",
              "cindeptid"
          });
        }
        
        protected DataPowerCtl getDataPowerCtl() {
          if(datapowerctl==null){
            datapowerctl = new DataPowerCtl(ClientEnvironment.getInstance().getUser().getPrimaryKey(),
                ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
            QueryDlgUtil.setDataPowerCtlFields(datapowerctl,new String[]{
                "head.coutwarehouseid",
                "head.coutdeptid",
                "head.coutbsor",
                "head.cinwarehouseid",
                "head.cindeptid",
                "head.cinbsrid",
                
//                "body.cwarehouseid",
                "body.cinventoryid",
                "body.cinventoryid",
                "body.cvendorid",
                "body.cspaceid",
            }, 
            new BD_TYPE[]{
                BD_TYPE.Warehouse,
                BD_TYPE.Dept,
                BD_TYPE.Psn,
                BD_TYPE.Warehouse,
                BD_TYPE.Dept,
                BD_TYPE.Psn,
                
//                BD_TYPE.Warehouse,          
                BD_TYPE.InvCl,
                BD_TYPE.Inv,
                BD_TYPE.Cust,
                BD_TYPE.CargoDoc,
            });
          }
          return datapowerctl;
        }
      };
    }
    return this.m_queryHelp;
  }
  
  
}