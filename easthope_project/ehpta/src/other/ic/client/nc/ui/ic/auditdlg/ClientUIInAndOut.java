package nc.ui.ic.auditdlg;

import java.util.ArrayList;

import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.GeneralButtonManager;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pub.AccreditLoginDialog;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.exp.RightcheckException;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.pub.SCMEnv;

/**
 * ����������/��ⵥ����

   ��ʾһ��ͬʱ��������/��ⵥ �Ľ��棬����������ȶ�����


   ֧�������档

   ֧�ֳ�/��ⵥ�ֱ𱣴档

   �������ⵥ�����������ִ������޸ġ�

 * �������ڣ�(2001-6-6 9:40:32)
 * @author������

   �޸ģ�2003-08-19:wnj:����淶����
 */
public class ClientUIInAndOut extends nc.ui.pub.beans.UIDialog {
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	//������ⵥ����
	private nc.ui.ic.ic207.ClientUI ivjChldClientUIIn = null;
	//�������ⵥ����
	private nc.ui.ic.ic217.ClientUI ivjChldClientUIOut = null;
	//״̬��
	private nc.ui.pub.beans.UITextField ivjUITxtFldStatus = null;
	//��ť
	private nc.ui.pub.beans.UIButton ivjUIBtnCancel = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSave = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSaveSign = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSerials = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSpace = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSwitch = null;
	private nc.ui.pub.beans.UIButton ivjbtnClose = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnDelRow = null;
	//tabpanel
	private nc.ui.pub.beans.UIPanel ivjUIPaneIn = null;
	private nc.ui.pub.beans.UIPanel ivjUIPaneOut = null;
	private nc.ui.pub.beans.UITabbedPane ivjUITabPane = null;

	private final int TabIn = 0; //��ⵥtab����
	private final int TabOut = 1; //���ⵥtab����
	public int m_TabFlag = TabIn; //��ʾ�ĸ�tabpanel
	public int m_SwitchFlag = BillMode.List; //��Ƭ�����б����

	private ArrayList m_inVOs = null; //������ⵥ
	private ArrayList m_outVOs = null; //���г��ⵥ
	private boolean m_bIsTabInSaved = false; //�Ƿ�������ⵥ������
	private boolean m_bIsTabOutSaved = false; //�Ƿ����г��ⵥ������
	private boolean m_bIsTabInHaveValue = false; //�Ƿ�����ⵥ
	private boolean m_bIsTabOutHaveValue = false; //�Ƿ��г��ⵥ

	private String m_sSrcBillTypeCode = null; //��Դ��������
	private final String m_sBillTypeCodeIn = nc.vo.ic.pub.BillTypeConst.m_otherIn;
	//������ⵥ
	private final String m_sBillTypeCodeOut = nc.vo.ic.pub.BillTypeConst.m_otherOut;
	//�������ⵥ

	private String m_sOldPK = null; //��pk
	private String m_sCorpPK = null; //��˾pk
	private String m_sUserID = null; //�û�pk

	private boolean m_bIsOK = false; //�Ƿ񱣴���

	private IvjEventHandler ivjEventHandler;
	private java.awt.event.ActionListener actionListenerIn;
	private java.awt.event.ActionListener actionListenerOut;
	
	protected abstract class ActionListenerInAndOut extends
			GeneralButtonManager implements java.awt.event.ActionListener {
		public ActionListenerInAndOut(GeneralBillClientUI clientUI) throws BusinessException {
			super(clientUI);
		}
		

		/**
		 * ���洦��

		 * �����ߣ�����
		 * ���ܣ�
		 * ������
		 * ���أ�
		 * ���⣺
		 * ���ڣ�(2001-5-16 15:27:06)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 *
		 */
		protected boolean onSave() {
			return onSave(false);
		}

		protected boolean onSave(Boolean isSign) {			
			try {
				// ��ó�������ͨ����VO

				GeneralBillVO[] voaAllBill = null;
				voaAllBill = onSaveGetAllBillVO();

				// ���濪ʼʱ��
				Timer t = new Timer();
				t.start(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
						"UPP4008busi-000329")/* @res "���濪ʼ" */);
				onSaveBaseKernel(voaAllBill, m_sUserID, m_sBillDate,isSign);
				t.stopAndShow(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000330")/* @res "�������" */);

				// �������洦��
				onSaveSetUI(isSign);

				return true;
			} catch (Exception e) {
				/*nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(),
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000157") @res "�������" , e
								.getMessage());*/
				nc.ui.ic.pub.tools.GenMethod.handleException(getClientUI(), null, e);
				return false;
			}
		}
	}

	protected class ActionListenerIn extends ActionListenerInAndOut {
		public ActionListenerIn(GeneralBillClientUI clientUI)
				throws BusinessException {
			super(clientUI);
		}

		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ClientUIInAndOut.this.getUIBtnSwitch())
				onSwitch();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSpace())
				onSpaceAssign();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSerials())
				onSNAssign();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSave())
				onSave();
			if (e.getSource() == ClientUIInAndOut.this.getUIBtnSaveSign())
				onSave(true);
			else if (e.getSource() == ClientUIInAndOut.this.getbtnClose())
				ClientUIInAndOut.this.closeOK();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnCancel())
				ClientUIInAndOut.this.closeCancel();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnDelRow())
				onDeleteRow(true);
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnPickAuto())
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000163")/*@res "�����������ⵥ�����Զ������"*/);
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnBarcode())
				onBarCodeEdit();
		}

		/**
		 * ��Ƭ/�б� �л�����

		 * �����ߣ�����
		 * ���ܣ�
		 * ������
		 * ���أ�
		 * ���⣺
		 * ���ڣ�(2001-5-16 15:27:06)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 *
		 */
		protected void onSwitch() {
			if (nc.vo.scm.constant.ic.BillMode.List == getChldClientUIIn()
					.getCurPanel()) {
				super.onSwitch();
				getUIBtnSwitch()
						.setText(
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"scmcommon", "UPPSCMCommon-000146")/*
																			 * @res
																			 * "�б�"
																			 */+ "(L)");
				getUIBtnSwitch().setMnemonic('l');
				getUIBtnSwitch().setBounds(792, 229, 97, 25);
				getUIBtnSpace().setEnabled(true);
				getUIBtnSerials().setEnabled(true);
				getUIBtnPickAuto().setEnabled(false);
				if (!m_bIsOK)// û�б��������
					getUIBtnBarcode().setEnabled(true);
				else
					getUIBtnBarcode().setEnabled(false);

			} else {
				GeneralBillVO billVO = getInVO();
				// billVO.setIDItems(getChldClientUIIn().getBillVO());

				int row = getChldClientUIIn().getLastSelListHeadRow();
				m_inVOs.set(row, billVO);
				getChldClientUIIn().setAllData(m_inVOs);
				// getChldClientUIIn().setLastSelListHeadRow(row);
				super.onSwitch();
				getUIBtnSwitch().setText(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000346")/* @res "��" */+ "(L)");
				getUIBtnSwitch().setBounds(792, 229, 97, 25);
				getUIBtnSwitch().setMnemonic('l');
				getUIBtnSpace().setEnabled(false);
				getUIBtnSerials().setEnabled(false);
				getUIBtnPickAuto().setEnabled(false);
				getUIBtnBarcode().setEnabled(false);

			}
			getUIBtnSave().setEnabled(getUIBtnSwitch().getText().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "�б�"*/+ "(L)")&& !m_bIsTabInSaved);
			getUIBtnSaveSign().setEnabled(
					getUIBtnSwitch().getText().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "�б�"*/) && !m_bIsTabInSaved);
		}

		/**
		 * Comment
		 */
		protected void onSpaceAssign() {
			if (getChldClientUIIn().isLocatorMgt()) {
				super.onSpaceAssign();
			} else
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000159")/*@res "���ǻ�λ����ֿ⡣"*/);
		}

		/**
		 * Comment
		 */
		protected void onSNAssign() {
			if (getChldClientUIIn().isSNmgt()) {
				super.onSNAssign();
			} else
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000158")/*@res "���в������кŹ���"*/);
		}

		/**
		 * Comment
		 */
		protected void onDeleteRow(boolean isevent) {
      if (isevent && nc.vo.ic.pub.BillTypeConst.m_transfer.equals(m_sSrcBillTypeCode) && m_bIsDirectOut){
        if (getChldClientUIIn().getCardTableRowNum() > 1 && 
            getChldClientUIIn().getCardTableRowNum()==getChldClientUIOut().getCardTableRowNum() && actionListenerOut!=null) {
          
            int[] rows = getChldClientUIIn().getBillCardPanel().getBillTable().getSelectedRows();
            if(rows==null || rows.length<=0)
              return;
            super.onDeleteRow();
            getChldClientUIOut().getBillCardPanel().getBillTable().getSelectionModel().clearSelection();
            for(int i=0;i<rows.length;i++){
              getChldClientUIOut().getBillCardPanel().getBillTable().getSelectionModel().addSelectionInterval(rows[i], rows[i]);
            }
            ((ActionListenerOut)actionListenerOut).onDeleteRow(false);
        }
      }else{
  			if (getChldClientUIIn().getCardTableRowNum() > 1) {
  				super.onDeleteRow();
  			}
      }
		}
	}

	protected class ActionListenerOut extends ActionListenerInAndOut {
		public ActionListenerOut(GeneralBillClientUI clientUI)
				throws BusinessException {
			super(clientUI);
		}

		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ClientUIInAndOut.this.getUIBtnSwitch())
				onSwitch();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSpace())
				onSpaceAssign();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSerials())
				onSNAssign();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSave())
				onSave();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSaveSign())
				onSave(true);
			else if (e.getSource() == ClientUIInAndOut.this.getbtnClose())
				ClientUIInAndOut.this.closeOK();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnCancel())
				ClientUIInAndOut.this.closeCancel();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnDelRow())
				onDeleteRow(true);
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnPickAuto())
				onPickAuto();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnBarcode())
				onBarCodeEdit();
		}
		
		protected void onSwitch() {
			if (BillMode.List == getChldClientUIOut().getCurPanelParam()) {
				super.onSwitch();
				getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "�б�"*/+ "(L)");
				getUIBtnSwitch().setBounds(792, 229, 97, 25);
				getUIBtnSwitch().setMnemonic('l');
				getUIBtnSpace().setEnabled(true);
				getUIBtnSerials().setEnabled(true);
				getUIBtnPickAuto().setEnabled(false);
				if (!m_bIsOK)//û�б��������
					getUIBtnBarcode().setEnabled(true);
				else
					getUIBtnBarcode().setEnabled(false);

			} else {
				GeneralBillVO billVO = getOutVO();
				//GeneralBillVO billVOd= getChldClientUIOut().getm_VOBill();
				//billVO.setIDItems(getChldClientUIIn().getBillVO());

				int row = getChldClientUIOut().getLastSelListHeadRow();
				m_outVOs.set(row, billVO);
				getChldClientUIOut().setAllData(m_outVOs);
				getChldClientUIOut().setLastSelListHeadRow(row);
				super.onSwitch();
				getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000346")/*@res "��"*/+ "(L)");
				getUIBtnSwitch().setBounds(792, 229, 97, 25);
				getUIBtnSwitch().setMnemonic('l');
				getUIBtnSpace().setEnabled(false);
				getUIBtnSerials().setEnabled(false);
				getUIBtnBarcode().setEnabled(false);

			}

			getUIBtnSave().setEnabled(
				getUIBtnSwitch().getText().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "�б�"*/+ "(L)") && !m_bIsTabOutSaved);
			getUIBtnSaveSign().setEnabled(
					getUIBtnSwitch().getText().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "�б�"*/+ "(L)") && !m_bIsTabOutSaved);
		getUIBtnPickAuto().setEnabled(getUIBtnSave().isEnabled());
		}
		
		protected void onSpaceAssign() {
			if (getChldClientUIOut().isLocatorMgt()) {
				super.onSpaceAssign();
			} else
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000159")/*@res "���ǻ�λ����ֿ⡣"*/);
		}
		
		protected void onSNAssign() {
			if (getChldClientUIOut().isSNmgt()) {
				super.onSNAssign();
			} else
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000158")/*@res "���в������кŹ���"*/);
		}
		
		protected void onDeleteRow(boolean isevent) {
      if (isevent && nc.vo.ic.pub.BillTypeConst.m_transfer.equals(m_sSrcBillTypeCode) && m_bIsDirectOut){
        if (getChldClientUIOut().getCardTableRowNum() > 1 && 
            getChldClientUIIn().getCardTableRowNum()==getChldClientUIOut().getCardTableRowNum() && actionListenerIn!=null) {
          
            int[] rows = getChldClientUIOut().getBillCardPanel().getBillTable().getSelectedRows();
            if(rows==null || rows.length<=0)
              return;
            super.onDeleteRow();
            getChldClientUIIn().getBillCardPanel().getBillTable().getSelectionModel().clearSelection();
            for(int i=0;i<rows.length;i++){
              getChldClientUIIn().getBillCardPanel().getBillTable().getSelectionModel().addSelectionInterval(rows[i], rows[i]);
            }
            ((ActionListenerIn)actionListenerIn).onDeleteRow(false);
        }
      }else{
  			if (getChldClientUIOut().getCardTableRowNum() > 1) 
  				super.onDeleteRow();
			}
		}
	}
	
	//��ť�¼�����
	private class IvjEventHandler implements javax.swing.event.ChangeListener {
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			if (e.getSource() == ClientUIInAndOut.this.getUITabPane())
				TabStateChanged();
		}
	}
	
/**
 * ClientUIInAndOut ������ע�⡣
 */
public ClientUIInAndOut() {
	super();
	initialize();
}
/**
 * ClientUIInAndOut ������ע�⡣
 * @param parent java.awt.Container
 */
private ClientUIInAndOut(java.awt.Container parent) {
	super(parent);
}
/**
 * ClientUIInAndOut ������ע�⡣
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public ClientUIInAndOut(java.awt.Container parent, String title) {
	super(parent, title);
	initialize();
	}
/**
 * ClientUIInAndOut ������ע�⡣
 * @param parent java.awt.Container
 * @param title java.lang.String
 * sBillTypeCode ��������
 */
public ClientUIInAndOut(
	java.awt.Container parent,
	String title,
	ArrayList invos,
	ArrayList outvos,
	String sBillTypeCode,
	String sOldPK,
	String sCorpPK,
	String sUserID) {

	super(parent, title);

	if (((invos == null) && (outvos == null))
		|| (sBillTypeCode == null)
		|| (sBillTypeCode.trim().length() == 0)
		|| (sCorpPK == null)
		|| (sCorpPK.trim().length() == 0)
		|| (sUserID == null)
		|| (sUserID.trim().length() == 0))
		return;

	m_sSrcBillTypeCode= sBillTypeCode.trim();
	m_sCorpPK= sCorpPK.trim();
	m_sUserID= sUserID.trim();
	m_sOldPK= sOldPK;

	initialize();

	if (invos != null) {
		m_inVOs= (ArrayList) invos.clone();
		getChldClientUIIn().onAdd(true, null);
		getChldClientUIIn().setAllData(invos);
		//getChldClientUIIn().setBillVO((GeneralBillVO) invos.get(0));
		getUITabPane().setEnabledAt(0, true);
		getUITabPane().setSelectedIndex(0);
		m_bIsTabInHaveValue= true;
	} else {
		getUIPaneIn().setEnabled(false);
		getUITabPane().setEnabledAt(0, false);
		m_bIsTabInHaveValue= false;
	}
	if (outvos != null) {
		m_outVOs= (ArrayList) outvos.clone();
		getChldClientUIOut().onAdd(true, null);
		getChldClientUIOut().setAllData(outvos);
		//getChldClientUIOut().setBillVO((GeneralBillVO) outvos.get(0));
		getUITabPane().setEnabledAt(1, true);
		getUITabPane().setSelectedIndex(1);
		m_bIsTabOutHaveValue= true;
	} else {
		getUIPaneOut().setEnabled(false);
		getUITabPane().setEnabledAt(1, false);
		m_bIsTabOutHaveValue= false;
	}

}
/**
 * ClientUIInAndOut ������ע�⡣
 * @param owner java.awt.Frame
 */
private ClientUIInAndOut(java.awt.Frame owner) {
	super(owner);
}
/**
 * ClientUIInAndOut ������ע�⡣
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
private ClientUIInAndOut(java.awt.Frame owner, String title) {
	super(owner, title);
}
/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-10-25 15:15:41)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void afterInit() {
	//û����
	m_bIsTabInSaved = false;
	m_bIsTabOutSaved = false;
	//ydy
	m_bIsOK = false;
	m_sLogDate=nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();
	//getUIBtnSwitch().setEnabled(false);
	getbtnClose().setEnabled(false);
	getUIBtnCancel().setEnabled(true);

//	TabStateChanged();
	getChldClientUIIn().setBodyMenuShow(false);
	getChldClientUIOut().setBodyMenuShow(false);
	//��ɾ�а�ť
	if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)) {






		//ת��
		getUIBtnDelRow().setEnabled(true);
	} else if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_assembly)) {





		//��װ
		getUIBtnDelRow().setEnabled(false);
	} else if (
		m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_disassembly)
			|| m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_saleOut)
			|| m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_purchaseIn)) {
		//��ж//���۳��ⵥ��ɹ���ⵥ������
		getUIBtnDelRow().setEnabled(false);
	} else if (
		m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transform)) { //��ת
		getUIBtnDelRow().setEnabled(false);
	} else if (
		m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_check)) { //�̵�
		getUIBtnDelRow().setEnabled(true);
	} else if (
		m_sSrcBillTypeCode.equals(
			nc.vo.ic.pub.BillTypeConst.m_AllocationOrder)) //��������
		getUIBtnDelRow().setEnabled(true);
}

/**
 * ���� btnClose ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnClose() {
	if (ivjbtnClose == null) {
		try {
			ivjbtnClose = new nc.ui.pub.beans.UIButton();
			ivjbtnClose.setName("btnClose");
			ivjbtnClose.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000119")/*@res "�ر�"*/+ "(G)");
			//ivjbtnClose.setBounds(584, 14, 97, 25);
			ivjbtnClose.setBounds(792, 340, 97, 25);
			ivjbtnClose.setMnemonic('g');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjbtnClose;
}


/**
 * ���� ChldClientUI ����ֵ��
 * @return nc.ui.ic.ic207.ClientUI
 */
/* ���棺�˷������������ɡ� */
public nc.ui.ic.ic207.ClientUI getChldClientUIIn() {
	if (ivjChldClientUIIn == null) {
		try {
			ivjChldClientUIIn = new nc.ui.ic.ic207.ClientUI();
			ivjChldClientUIIn.setName("ChldClientUIIn");
			ivjChldClientUIIn.setBounds(1, 0, 770, 428);
			// user code begin {1}
			// 0605 by zhx remove the right menu button
			ivjChldClientUIIn.setBodyMenuShow(false);

			//�������������
			ivjChldClientUIIn.getBarcodeCtrl().setAddNewInvLine(false);
			//ivjChldClientUIIn.getBarcodeCtrl().setModifyBillUINum(false);
			
			//�޸��ˣ������� �޸����ڣ�2007-05-22 �޸�ԭ����Ϊ�������ᵼ���������ⵥ��������ⵥ���ݶ�Ӧ����������ʱ�����
			ivjChldClientUIIn.getBillCardPanel().getBodyPanel().getTable().setSortEnabled(false);
			ivjChldClientUIIn.getBillListPanel().getHeadTable().setSortEnabled(false);
			ivjChldClientUIIn.getBillListPanel().getBodyTable().setSortEnabled(false);

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjChldClientUIIn;
}
/**
 * ���� ChldClientUIOut ����ֵ��
 * @return nc.ui.ic.ic217.ClientUI
 */
/* ���棺�˷������������ɡ� */
public nc.ui.ic.ic217.ClientUI getChldClientUIOut() {
	if (ivjChldClientUIOut == null) {
		try {
			ivjChldClientUIOut = new nc.ui.ic.ic217.ClientUI();
			ivjChldClientUIOut.setName("ChldClientUIOut");
			ivjChldClientUIOut.setBounds(2, 0, 770, 428);
			// user code begin {1}
			// 0605 by zhx remove the right menu button

			ivjChldClientUIOut.getBarcodeCtrl().setAddNewInvLine(false);
			//ivjChldClientUIIn.getBarcodeCtrl().setModifyBillUINum(false);

			ivjChldClientUIOut.setBodyMenuShow(false);
			
			//�޸��ˣ������� �޸����ڣ�2007-05-22 �޸�ԭ����Ϊ�������ᵼ���������ⵥ��������ⵥ���ݶ�Ӧ����������ʱ�����
			ivjChldClientUIOut.getBillCardPanel().getBodyPanel().getTable().setSortEnabled(false);
			ivjChldClientUIOut.getBillListPanel().getHeadTable().setSortEnabled(false);
			ivjChldClientUIOut.getBillListPanel().getBodyTable().setSortEnabled(false);
			
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjChldClientUIOut;
}
/**
 *
   �õ���������ʾ��vo,����Ϊ�������ݡ�

 * �������ڣ�(2001-6-19 10:16:12)
 * @return nc.vo.ic.ic207.GeneralHVO
 */
protected GeneralBillVO getInVO() {
	GeneralBillVO inVO = null;
	//��ⵥ����ʱ�Ŷ�֮
	if (getUITabPane().isEnabledAt(0)) {
		inVO = getChldClientUIIn().getBillVO();
		if (inVO != null
			&& inVO.getChildrenVO() != null
			&& inVO.getChildrenVO().length > 0) {
			inVO.setStatus(VOStatus.NEW);

		}
	}
	
	return inVO;
}
/**

	�õ���������ʾ��vo,����Ϊ�������ݡ�

   * �������ڣ�(2001-6-23 0:22:07)
 * @return nc.vo.ic.pub.bill.GeneralBillVO[]
 */
protected GeneralBillVO getOutVO() {
	GeneralBillVO outVO = null;
	//��ⵥ����ʱ�Ŷ�֮
	if (getUITabPane().isEnabledAt(1)) {
		outVO = getChldClientUIOut().getBillVO();
		if (outVO != null
			&& outVO.getChildrenVO() != null
			&& outVO.getChildrenVO().length > 0) {
			outVO.setStatus(VOStatus.NEW);
		}
	}
	return outVO;
}
/**
 * ���� UIButton21 ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnCancel() {
	if (ivjUIBtnCancel == null) {
		try {
			ivjUIBtnCancel = new nc.ui.pub.beans.UIButton();
			ivjUIBtnCancel.setName("UIBtnCancel");
			ivjUIBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "ȡ��"*/ + "(C)");
      ivjUIBtnCancel.setMnemonic('c');
			ivjUIBtnCancel.setBounds(792, 373, 97, 25);

			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnCancel;
}
/**
 * ���� UIBtnDelRow ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnDelRow() {
	if (ivjUIBtnDelRow == null) {
		try {
			ivjUIBtnDelRow = new nc.ui.pub.beans.UIButton();
			ivjUIBtnDelRow.setName("UIBtnDelRow");
			ivjUIBtnDelRow.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000013")/*@res "ɾ��"*/+ "(D)");
			ivjUIBtnDelRow.setBounds(792, 130, 97, 25);
			ivjUIBtnDelRow.setMnemonic('d');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnDelRow;
}
/**
 * ���� UIButton1 ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnSave() {
	if (ivjUIBtnSave == null) {
		try {
			ivjUIBtnSave = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSave.setName("UIBtnSave");
			ivjUIBtnSave.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res "����"*/ + "(S)");
      //modified by liuzy 2008-01-03 Ԫ�� ���ÿ�ݼ� 200712271315098077
      ivjUIBtnSave.setMnemonic('s');
			ivjUIBtnSave.setBounds(792, 64, 97, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSave;
}


private nc.ui.pub.beans.UIButton getUIBtnSaveSign() {
	if (ivjUIBtnSaveSign == null) {
		try {
			ivjUIBtnSaveSign = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSaveSign.setName("UIBtnSaveSign");
			ivjUIBtnSaveSign.setText(NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000000", null, new String[]{})/*���棦ǩ��(A)*/);
      //modified by liuzy 2008-01-03 Ԫ�� ���ÿ�ݼ� 200712271315098077
			ivjUIBtnSaveSign.setMnemonic('a');
			ivjUIBtnSaveSign.setBounds(792, 31, 97, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSaveSign;
}

/**
 * ���� UIButton41 ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnSerials() {
	if (ivjUIBtnSerials == null) {
		try {
			ivjUIBtnSerials = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSerials.setName("UIBtnSerials");
			ivjUIBtnSerials.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001819")/*@res "���к�"*/+ "(X)");
			ivjUIBtnSerials.setBounds(792, 196, 97, 25);
			ivjUIBtnSerials.setMnemonic('x');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSerials;
}
/**
 * ���� UIButton31 ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnSpace() {
	if (ivjUIBtnSpace == null) {
		try {
			ivjUIBtnSpace = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSpace.setName("UIBtnSpace");
			ivjUIBtnSpace.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003830")/*@res "��λ"*/+ "(H)");
			ivjUIBtnSpace.setBounds(792, 163, 97, 25);
			ivjUIBtnSpace.setMnemonic('h');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSpace;
}

/**
 * �������е���Ϊ����״̬
 * @param al
 */
private void setVOStatusNew(ArrayList al) {
    if (al == null||al.size()<=0)
        return;
    for (int i=0;i<al.size();i++) {
        GeneralBillVO vo = (GeneralBillVO) al.get(i);
        vo.setStatus(VOStatus.NEW);
    }
}



/**
 * ���� UIBtnSwitch ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnSwitch() {
	if (ivjUIBtnSwitch == null) {
		try {
			ivjUIBtnSwitch = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSwitch.setName("UIBtnSwitch");
			ivjUIBtnSwitch.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "�б�"*/+ "(L)");
			ivjUIBtnSwitch.setBounds(792, 229, 97, 25);
			ivjUIBtnSwitch.setMnemonic('l');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSwitch;
}
/**
 * ���� UIDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getUIDialogContentPane() {
	if (ivjUIDialogContentPane == null) {
		try {
			ivjUIDialogContentPane = new javax.swing.JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(null);
			ivjUIDialogContentPane.setMinimumSize(new java.awt.Dimension(100, 100));
			
			getUIDialogContentPane().add(getUIBtnSave(), getUIBtnSave().getName());
			getUIDialogContentPane().add(getUIBtnSaveSign(), getUIBtnSaveSign().getName());
			getUIDialogContentPane().add(getUIBtnPickAuto(), getUIBtnPickAuto().getName());
			getUIDialogContentPane().add(getUIBtnSerials(), getUIBtnSerials().getName());
			getUIDialogContentPane().add(getUIBtnSpace(), getUIBtnSpace().getName());
			getUIDialogContentPane().add(getUIBtnSwitch(), getUIBtnSwitch().getName());
			getUIDialogContentPane().add(getbtnClose(), getbtnClose().getName());
			getUIDialogContentPane().add(getUIBtnDelRow(), getUIBtnDelRow().getName());
			getUIDialogContentPane().add(getUIBtnBarcode(), getUIBtnBarcode().getName());			
			getUIDialogContentPane().add(getUIBtnCancel(), getUIBtnCancel().getName());
			
			getUIDialogContentPane().add(getUITabPane(), getUITabPane().getName());
			getUIDialogContentPane().add(getUITxtFldStatus(), getUITxtFldStatus().getName());
			
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIDialogContentPane;
}
/**
 * ���� UIPanel1 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIPanel getUIPaneIn() {
	if (ivjUIPaneIn == null) {
		try {
			ivjUIPaneIn = new nc.ui.pub.beans.UIPanel();
			ivjUIPaneIn.setName("UIPaneIn");
			ivjUIPaneIn.setLayout(null);
			ivjUIPaneIn.add(getChldClientUIIn(), getChldClientUIIn().getName());
//			getUIPaneIn().add(getChldClientUIIn(), getChldClientUIIn().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIPaneIn;
}
/**
 * ���� UIPanel2 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIPanel getUIPaneOut() {
	if (ivjUIPaneOut == null) {
		try {
			ivjUIPaneOut = new nc.ui.pub.beans.UIPanel();
			ivjUIPaneOut.setName("UIPaneOut");
//			ivjUIPaneOut.setLayout(null);
			ivjUIPaneOut.add(getChldClientUIOut(), getChldClientUIOut().getName());
//			getUIPaneOut()
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIPaneOut;
}
/**
 * ���� UITabbedPane1 ����ֵ��
 * @return nc.ui.pub.beans.UITabbedPane
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITabbedPane getUITabPane() {
	if (ivjUITabPane == null) {
		try {
			ivjUITabPane = new nc.ui.pub.beans.UITabbedPane();
			ivjUITabPane.setName("UITabPane");
			ivjUITabPane.setBounds(5, 14, 782, 463);
			ivjUITabPane.insertTab(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPT40080608-000002")/*@res "������ⵥ"*/, null, getUIPaneIn(), null, 0);
			ivjUITabPane.insertTab(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPT40080808-000002")/*@res "�������ⵥ"*/, null, getUIPaneOut(), null, 1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUITabPane;
}
/**
 * ���� UITextField1 ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField getUITxtFldStatus() {
	if (ivjUITxtFldStatus == null) {
		try {
			ivjUITxtFldStatus = new nc.ui.pub.beans.UITextField();
			ivjUITxtFldStatus.setName("UITxtFldStatus");
			ivjUITxtFldStatus.setBorder(new nc.ui.pub.style.TableHeaderBorder());
			ivjUITxtFldStatus.setText("");
			ivjUITxtFldStatus.setBounds(10, 498, 782, 21);
			ivjUITxtFldStatus.setEditable(false);
			ivjUITxtFldStatus.setMaxLength(200);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUITxtFldStatus;
}
/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	// nc.vo.scm.pub.SCMEnv.out("--------- δ��׽�����쳣 ---------");
	// nc.vo.scm.pub.SCMEnv.error(exception);
}
/**
 * ��ʼ������
 * @exception java.lang.Exception �쳣˵����
 */
/* ���棺�˷������������ɡ� */
private void initConnections() {
	if (ivjEventHandler == null) {
		ivjEventHandler = new IvjEventHandler();
		getUITabPane().addChangeListener(ivjEventHandler);
	}
	try {
		if (actionListenerIn == null) {
			actionListenerIn = new ActionListenerIn(getChldClientUIIn());
		}
		if (actionListenerOut == null) {
			actionListenerOut = new ActionListenerOut(getChldClientUIOut());
		}
	}
	catch (Exception e) {
		nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
							"UPP4008busi-000157")/* @res "�������" */, e
							.getMessage());
		return;
	}
	
	// ɾ��In�Ķ�������
	getUIBtnSwitch().removeActionListener(actionListenerIn);
	getUIBtnSpace().removeActionListener(actionListenerIn);
	getUIBtnSerials().removeActionListener(actionListenerIn);
	getUIBtnSave().removeActionListener(actionListenerIn);
	getUIBtnSaveSign().removeActionListener(actionListenerIn);
	getbtnClose().removeActionListener(actionListenerIn);
	getUIBtnCancel().removeActionListener(actionListenerIn);
	getUIBtnDelRow().removeActionListener(actionListenerIn);
	getUIBtnPickAuto().removeActionListener(actionListenerIn);
	getUIBtnBarcode().removeActionListener(actionListenerIn);
	// ɾ��Out�Ķ�������
	getUIBtnSwitch().removeActionListener(actionListenerOut);
	getUIBtnSpace().removeActionListener(actionListenerOut);
	getUIBtnSerials().removeActionListener(actionListenerOut);
	getUIBtnSave().removeActionListener(actionListenerOut);
	getUIBtnSaveSign().removeActionListener(actionListenerOut);
	getbtnClose().removeActionListener(actionListenerOut);
	getUIBtnCancel().removeActionListener(actionListenerOut);
	getUIBtnDelRow().removeActionListener(actionListenerOut);
	getUIBtnPickAuto().removeActionListener(actionListenerOut);
	getUIBtnBarcode().removeActionListener(actionListenerOut);
	
	if (m_TabFlag == TabIn) {
		// ���In�Ķ�������
		getUIBtnSwitch().addActionListener(actionListenerIn);
		getUIBtnSpace().addActionListener(actionListenerIn);
		getUIBtnSerials().addActionListener(actionListenerIn);
		getUIBtnSave().addActionListener(actionListenerIn);
		getUIBtnSaveSign().addActionListener(actionListenerIn);
		getbtnClose().addActionListener(actionListenerIn);
		getUIBtnCancel().addActionListener(actionListenerIn);
		getUIBtnDelRow().addActionListener(actionListenerIn);
		getUIBtnPickAuto().addActionListener(actionListenerIn);
		getUIBtnBarcode().addActionListener(actionListenerIn);
	}
	else {
		// ���Out�Ķ�������
		getUIBtnSwitch().addActionListener(actionListenerOut);
		getUIBtnSpace().addActionListener(actionListenerOut);
		getUIBtnSerials().addActionListener(actionListenerOut);
		getUIBtnSave().addActionListener(actionListenerOut);
		getUIBtnSaveSign().addActionListener(actionListenerOut);
		getbtnClose().addActionListener(actionListenerOut);
		getUIBtnCancel().addActionListener(actionListenerOut);
		getUIBtnDelRow().addActionListener(actionListenerOut);
		getUIBtnPickAuto().addActionListener(actionListenerOut);
		getUIBtnBarcode().addActionListener(actionListenerOut);
	}
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		m_sLogDate=nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();
		m_sCorpName=nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname();
		setCorpID(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp());
		setName("ClientUIInAndOut");
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(900, 560);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000328")/*@res "������������"*/);
		setContentPane(getUIDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}

}
/**

 * ����˵���������Ƿ񱣴��˵���

 * �������ڣ�(2003-1-16 11:40:31)
 * ���ߣ����˾�
 * �޸����ڣ�
 * �޸��ˣ�
 * �޸�ԭ��
 * �㷨˵����
 * @return boolean
 */
public boolean isOK() {
	return m_bIsOK;
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		ClientUIInAndOut aClientUIInAndOut;
		aClientUIInAndOut = new ClientUIInAndOut();
		aClientUIInAndOut.setModal(true);
		aClientUIInAndOut.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aClientUIInAndOut.show();
		java.awt.Insets insets = aClientUIInAndOut.getInsets();
		aClientUIInAndOut.setSize(aClientUIInAndOut.getWidth() + insets.left + insets.right, aClientUIInAndOut.getHeight() + insets.top + insets.bottom);
		aClientUIInAndOut.setVisible(true);
	} catch (Throwable exception) {
		nc.vo.scm.pub.SCMEnv.out(NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000001")/*nc.ui.pub.beans.UIDialog �� main() �з����쳣*/);
		nc.vo.scm.pub.SCMEnv.error(exception);
	}
}

/**
 * ����˵���� ���á��ѱ��桱״̬��
 * �������ڣ�(2003-1-16 11:40:31)
 * ���ߣ����˾�
 * �޸����ڣ�
 * �޸��ˣ�
 * �޸�ԭ��
 * �㷨˵����
 * @return boolean
 */
public void setIsOK(boolean isok) {
	m_bIsOK = isok;
}

/**
 * ��״̬����ʾ��Ϣ
 * �������ڣ�(2001-6-6 14:26:29)
 */
public void showStatus(String msg) {
	getUITxtFldStatus().setText(msg);

}
/**
 * �����ߣ����˾�
 * ���ܣ���ʾ���ĵ�ʱ��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-11-23 18:11:18)
 *  �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected void showTime(long lStartTime, String sTaskHint) {
	long lTime = System.currentTimeMillis() - lStartTime;
	nc.vo.scm.pub.SCMEnv.out(
		NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000002")/*ִ��<*/
			+ sTaskHint
			+ NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000003")/*>���ĵ�ʱ��Ϊ��*/
			+ (lTime / 60000)
			+ NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000004")/*��*/
			+ ((lTime / 1000) % 60)
			+ NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000005")/*��*/
			+ (lTime % 1000)
			+ NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000006")/*����*/);

}
/**
 * Comment
 */
public void TabStateChanged() {
	int i = getUITabPane().getSelectedIndex();
	switch (i) {
		case 0 :
			{
				m_TabFlag = TabIn;
				//�ñ�����л�״̬
				getUIBtnSwitch().setEnabled(true);
				getUIBtnPickAuto().setEnabled(false);

				//���л���ʾ��
				if (BillMode.List == getChldClientUIIn().getCurPanel()) {
					getUIBtnSave().setEnabled(false);
					getUIBtnSaveSign().setEnabled(false);

					getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000346")/*@res "��"*/+ "(L)");
					getUIBtnSwitch().setBounds(792, 229, 97, 25);
					getUIBtnSwitch().updateUI();
				
					getUIBtnSpace().setEnabled(false);
					getUIBtnSerials().setEnabled(false);
					getUIBtnBarcode().setEnabled(false);
				} else {
					getUIBtnSave().setEnabled(!m_bIsTabOutSaved);
					getUIBtnSaveSign().setEnabled(!m_bIsTabOutSaved);
					
					getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "�б�"*/+ "(L)");
					getUIBtnSwitch().setBounds(792, 229, 97, 25);
				 	getUIBtnSwitch().updateUI();

					getUIBtnSpace().setEnabled(true);
					getUIBtnSerials().setEnabled(true);
					if (!m_bIsOK)//û�б��������
						getUIBtnBarcode().setEnabled(true);
					else
						getUIBtnBarcode().setEnabled(false);	

					//ֱ��ת����ⵥ���ܱ༭����
					if (m_bIsDirectOut) {
    	                getUIBtnBarcode().setEnabled(false);

    	                //getUIBtnDelRow().setEnabled(false);
                      
						//getChldClientUIIn().getBillCardPanel().setEnabled(alse);
						//getChldClientUIIn().getBillCardPanel().getBillModel().setEnabledAllItems(false);


						String[] keys=new String[]{"cdispatcherid","cproviderid","vnote","cwhsmanagerid","cbizid","cdptid"};
						for(int j=0;j<keys.length;j++){
						if(getChldClientUIIn().getBillCardPanel().getHeadItem(keys[j])!=null){
							getChldClientUIIn().getBillCardPanel().getHeadItem(keys[j]).setEnabled(true);
							getChldClientUIIn().getBillCardPanel().getHeadItem(keys[j]).setEdit(true);

							}
						}
						
						//getChldClientUIIn().setEnabled(false);
						synOut2InBill();
					}
					else {
		                getUIBtnBarcode().setEnabled(true);
						//getChldClientUIIn().getBillCardPanel().getBillModel().setEnabledAllItems(true);
					}

				}

				//�ÿɱ༭״̬
				if(m_bIsOK)
					getChldClientUIIn().setCardPanelEnable(false);
				break;
			}
		case 1 :
			{
				m_TabFlag = TabOut;
				getUIBtnSwitch().setEnabled(true);

				//���л���ʾ��
				if (BillMode.List == getChldClientUIOut().getCurPanelParam()) {
					getUIBtnSave().setEnabled(false);
					getUIBtnSaveSign().setEnabled(false);
					getUIBtnPickAuto().setEnabled(false);

					getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000346")/*@res "��"*/+ "(L)");
					getUIBtnSwitch().setBounds(792, 229, 97, 25);
	    			getUIBtnSwitch().updateUI();
		
					getUIBtnSpace().setEnabled(false);
					getUIBtnSerials().setEnabled(false);
					getUIBtnBarcode().setEnabled(false);
				} else {
					getUIBtnSave().setEnabled(!m_bIsTabInSaved);
					getUIBtnSaveSign().setEnabled(!m_bIsTabInSaved);
					getUIBtnPickAuto().setEnabled(getUIBtnSave().isEnabled());

					getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "�б�"*/+ "(L)");
					getUIBtnSwitch().setBounds(792, 229, 97, 25);
					getUIBtnSwitch().updateUI();

	    			getUIBtnSpace().setEnabled(true);
	    			getUIBtnSerials().setEnabled(true);
	    			if (!m_bIsOK)//û�б��������
	    				getUIBtnBarcode().setEnabled(true);
	    			else
	    				getUIBtnBarcode().setEnabled(false);

					if (m_bIsDirectOut) {
						getUIBtnSerials().setEnabled(true);
						getUIBtnSpace().setEnabled(true);
            
						//getUIBtnDelRow().setEnabled(false);
					}
				}

				break;
			}
	};
	initConnections();
	return;
}

	private nc.ui.pub.beans.UIButton ivjUIBtnBarcode = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnPickAuto = null;
	//�û���������У��UI
	protected nc.ui.scm.pub.AccreditLoginDialog m_AccreditLoginDialog;
	private boolean m_bIsDirectOut = false; // �Ƿ�ֱ�ӳ���: ��Ҫͬʱ����������, ������ⵥ.
	private String m_sBillDate = null; //��½����
	private boolean m_bIsSignOK = false;
	public java.lang.String m_sCorpID;
	public String m_sCorpName;
	private String m_sLogDate = null; //��½����
	//���ⵥ VO add by hanwei 2003-11-03
	protected AggregatedValueObject m_voSpBill;
	public static final nc.vo.pub.lang.UFDouble UFDZERO = new nc.vo.pub.lang.UFDouble(0.0);

/**
 * �
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2005-1-19 11:39:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 */
private void exeAllFormulas(nc.ui.ic.pub.bill.GeneralBillClientUI ui,ArrayList alVOs) {

  if (alVOs == null || alVOs.size()<=0) return;
  ui.setAlistDataByFormula(-1, alVOs);
  GeneralBillHeaderVO[] voaHeads = new GeneralBillHeaderVO[alVOs.size()];
  ArrayList<GeneralBillItemVO> listitemvos = new ArrayList<GeneralBillItemVO>();
  GeneralBillItemVO[] itemvos = null;
  GeneralBillVO billvo = null;
  for (int i = 0; i < alVOs.size(); i++) {
    billvo = (GeneralBillVO)alVOs.get(i);
    voaHeads[i] = billvo.getHeaderVO();
    //  (GeneralBillHeaderVO) ((AggregatedValueObject) alVOs.get(i)).getParentVO();
    itemvos = billvo.getItemVOs();
    for(GeneralBillItemVO itemvo :itemvos)
      listitemvos.add(itemvo);
  }
  ui.setListHeadData(voaHeads);
  if(listitemvos.size()>0){
    itemvos = listitemvos.toArray(new GeneralBillItemVO[itemvos.length]);
//  ִ�����κŵ�����ʽ
    try {
      BatchCodeDefSetTool.execFormulaForBatchCode(itemvos);
    }catch(Exception e) {
      nc.vo.scm.pub.SCMEnv.error(e);
    }
  }
}

/**
 * �˴����뷽��˵����
   ���Ȩ����֤UI
 * �������ڣ�(2004-4-19 14:11:06)
 * @return nc.ui.scm.pub.AccreditLoginDialog
 */
public nc.ui.scm.pub.AccreditLoginDialog getAccreditLoginDialog() {
	if (m_AccreditLoginDialog==null)
	m_AccreditLoginDialog=new AccreditLoginDialog();
	return m_AccreditLoginDialog;
}

/**
 *

 * �����ߣ����˾�
 * ���ܣ� �õ�����ĵ�����

 * ���������󽫱��޸ġ�
 * ���أ�vo[] or null
 * ���⣺
 * ���ڣ�(2001-5-16 15:27:06)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 */
private GeneralBillVO[] getCheckedInBills() throws Exception {
	GeneralBillVO[] voaIn = null;
	//==============================================
	//VOУ��
	if (m_inVOs != null && m_inVOs.size() != 0) {

		//��ȥ����ʽ�µĿ���
		getChldClientUIIn().filterNullLine();

		//VOУ��׼������
		GeneralBillVO voNowBill = getInVO();
		
		if (voNowBill != null && (voNowBill.getItemVOs() == null || voNowBill.getItemVOs().length == 0))
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000160")/*@res "����Ϊ�գ������Թرձ����棬Ȼ�����½���..."*/);
		int iNowSelBill = getChldClientUIIn().getLastSelListHeadRow();
		//ˢ��list�еĵ���
		m_inVOs.set(iNowSelBill, voNowBill);
		getChldClientUIIn().setAllData(m_inVOs);
		//�ѵ���д�������ϣ�������Ҫ�Ľ����鷽����
		GeneralBillVO voTempBill = null;
		GeneralBillHeaderVO voHead=null;
		for (int i = 0; i < m_inVOs.size(); i++) {
            setVOStatusNew(m_inVOs);
			voTempBill =(GeneralBillVO) m_inVOs.get(i);
			//ͨ��ƽ̨ʵ�ֱ��漴ǩ�֣�����ǩ�����ֶΣ��ڳ���ⵥ����ʱ���֮��
			//֧��ƽ̨��cloneһ�����Ա����Ժ�Ĵ���ͬʱ��ֹ�޸���m_voBill
			voHead =voTempBill.getHeaderVO();
			voHead.setPk_corp(m_sCorpPK);
			//ǩ����
		//	voHead.setCregister(m_sUserID);
			//��Ϊ��¼���ں͵��������ǿ��Բ�ͬ�ģ����Ա���Ҫ��¼���ڡ�
		//	voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			//vo����Ҫ����ƽ̨������Ҫ���ɺ�ǩ�ֺ�ĵ���
		//	voHead.setFbillflag(new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
			voHead.setCoperatoridnow(m_sUserID); //��ǰ����Ա2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); //��ǰ��¼����2003-01-05

			voTempBill = (GeneralBillVO) ((GeneralBillVO) m_inVOs.get(i)).clone();
			getChldClientUIIn().setBillVO(voTempBill, false);
			getChldClientUIIn().setLastSelListHeadRow(i);
			if (!getChldClientUIIn().setBillCodeAuto()) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000161")/*@res "���ݺŻ�ȡʧ��!"*/);
			}
			//ֱ�ӳ�����Ҫ���������ⵥ���Ƿ��λ�ֿ�,�Ƿ���д��λ.
			//if (m_bIsDirectOut) {
				//try {
					//nc.vo.ic.pub.check.VOCheck.checkLocatorInput(voTempBill, new Integer(InOutFlag.IN));
				//} catch (ICLocatorException ex) {
					////��ʾ��ʾ
					////String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

					////return null;
					//throw new Exception("ֱ��ת��������������ⵥ,"+ex.getHint()+" �������ⵥ��λ! ");
				//}

			//} else {

				if (!getChldClientUIIn().checkVO()) {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000162")/*@res "������������Ϣ!"*/);
				}
			//}
			//������wnj m_inVOs.set(i, (GeneralBillVO) getinVOs().clone());
		}
		//ת�ⵥֻ֧��һ��
		if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)) {

			voaIn = new GeneralBillVO[1];
			voaIn[0] = voNowBill;
		} else {
			voaIn = new GeneralBillVO[m_inVOs.size()];
			for (int i = 0; i < m_inVOs.size(); i++) {
				voaIn[i] = (GeneralBillVO) m_inVOs.get(i);
			}
		}
		
		for(GeneralBillVO voaInVO : voaIn )
			voaInVO.setSaveBadBarcode(getChldClientUIIn().getbBadBarcodeSave());
	}
	return voaIn;
}

/**

 * �����ߣ����˾�
 * ���ܣ� �õ�����ĵ�����

 * ���������󽫱��޸ġ�
 * ���أ�ArrayList:
	 0:  �Ƿ�ɹ�
	 1:	 vo[]:��� 0 �� false,���ؿգ����򷵻�vo[]

 * ���⣺
 * ���ڣ�(2001-5-16 15:27:06)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 */
private GeneralBillVO[] getCheckedOutBills() throws Exception {
	//==============================================
	//VOУ��
	GeneralBillVO[] voaOut = null;
	if ((m_outVOs != null) && (m_outVOs.size() != 0)) {


//  VOУ��׼������
    GeneralBillVO voNowBill = getOutVO();
    
		//��ȥ����ʽ�µĿ���
		getChldClientUIOut().filterNullLine();


		if (voNowBill != null
			&& (voNowBill.getItemVOs() == null || voNowBill.getItemVOs().length == 0))
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000160")/*@res "����Ϊ�գ������Թرձ����棬Ȼ�����½���..."*/);
    
    voNowBill.getChildrenVO()[0].getAttributeValue("cinventorycode");
    voNowBill.getChildrenVO()[0].getAttributeValue("cinventoryid");

		int iNowSelBill = getChldClientUIOut().getLastSelListHeadRow();
		//ˢ��list�еĵ���
		m_outVOs.set(iNowSelBill, voNowBill);
		getChldClientUIOut().setAllData(m_outVOs);
		//�ѵ���д�������ϣ�������Ҫ�Ľ����鷽����
		GeneralBillVO voTempBill = null;
		GeneralBillHeaderVO voHead=null;
		for (int i = 0; i < m_outVOs.size(); i++) {
			setVOStatusNew(m_outVOs);
			voTempBill =(GeneralBillVO) m_outVOs.get(i);
			//ͨ��ƽ̨ʵ�ֱ��漴ǩ�֣�����ǩ�����ֶΣ��ڳ���ⵥ����ʱ���֮��
			//֧��ƽ̨��cloneһ�����Ա����Ժ�Ĵ���ͬʱ��ֹ�޸���m_voBill
			voHead =voTempBill.getHeaderVO();
			voHead.setPk_corp(m_sCorpPK);
			//ǩ����
		//	voHead.setCregister(m_sUserID);
			//��Ϊ��¼���ں͵��������ǿ��Բ�ͬ�ģ����Ա���Ҫ��¼���ڡ�
		//	voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			//vo����Ҫ����ƽ̨������Ҫ���ɺ�ǩ�ֺ�ĵ���
		//	voHead.setFbillflag(new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
			voHead.setCoperatoridnow(m_sUserID); //��ǰ����Ա2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); //��ǰ��¼����2003-01-05

			voTempBill = (GeneralBillVO) ((GeneralBillVO) m_outVOs.get(i)).clone();

			getChldClientUIOut().setBillVO(voTempBill, false);
			getChldClientUIOut().setLastSelListHeadRow(i);
			if (!getChldClientUIOut().setBillCodeAuto()) {

				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000161")/*@res "���ݺŻ�ȡʧ��!"*/);
			}
			if (!getChldClientUIOut().checkVO()) {

				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000162")/*@res "������������Ϣ!"*/);

			}
			//������wnj m_outVOs.set(i, (GeneralBillVO) getoutVOs().clone());
		}
		//ת�ⵥֻ֧��һ��
		if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)) {

			voaOut = new GeneralBillVO[1];
			voaOut[0] = voNowBill;
		} else {
			voaOut = new GeneralBillVO[m_outVOs.size()];
			for (int i = 0; i < m_outVOs.size(); i++) {
				voaOut[i] = (GeneralBillVO) m_outVOs.get(i);
			}
		}
		
		for(GeneralBillVO voaOutVO :voaOut)
			voaOutVO.setSaveBadBarcode(getChldClientUIOut().getbBadBarcodeSave());
	}
	return voaOut;
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2004-4-19 20:26:37)
 * @return java.lang.String
 */
public java.lang.String getCorpID() {
	return m_sCorpID;
}

/**
 * �˴����뷽��˵����
   ������ⵥVO
 * �������ڣ�(2003-11-3 16:50:29)
 * @return nc.vo.ic.pub.bill.SpecialBillVO
 */
public AggregatedValueObject getSpBill() {
	return m_voSpBill;
}

/**
 * ���� UIBtnBarcode ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnBarcode() {
	if (ivjUIBtnBarcode == null) {
		try {
			ivjUIBtnBarcode = new nc.ui.pub.beans.UIButton();
			ivjUIBtnBarcode.setName("UIBtnBarcode");
			ivjUIBtnBarcode.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402","UPT40080402-000027")/*@res "����༭"*/+ "(B)");
			ivjUIBtnBarcode.setBounds(792, 262, 97, 25);
			ivjUIBtnBarcode.setMnemonic('b');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnBarcode;
}

/**
 * ���� UIButton1 ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnPickAuto() {
	if (ivjUIBtnPickAuto == null) {
		try {
			ivjUIBtnPickAuto = new nc.ui.pub.beans.UIButton();
			ivjUIBtnPickAuto.setName("UIBtnPickAuto");
			ivjUIBtnPickAuto.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","UPT40080802-000032")/*@res "�Զ����"*/ + "(A)");
      ivjUIBtnPickAuto.setMnemonic('a');      
			ivjUIBtnPickAuto.setBounds(792, 97, 97, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnPickAuto;
}

/**
 * �����ߣ�������������еĺ��ķ���
 * ���ܣ�ȷ�ϣ����棩����
 * ��������
 * ���⣺
 * ���ڣ�(2004-4-1 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   2004-4-1   ����
 */
protected void onSaveBaseKernel(GeneralBillVO[] voaAllBill,String sAccreditUserID, String sBillDate,Boolean isSign)
	throws Exception {

	try {
		nc.vo.sm.log.OperatelogVO log = new nc.vo.sm.log.OperatelogVO();
		log.setCompanyname(m_sCorpName);
		if (!nc.ui.pub.ClientEnvironment.getInstance().isInDebug())
		log.setEnterip(nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
		log.setPKCorp(m_sCorpID);
		if (voaAllBill!=null && voaAllBill.length>0)
		{
			for (int i=0;i<voaAllBill.length;i++)
			{
				voaAllBill[i].setAccreditUserID(sAccreditUserID);
				voaAllBill[i].setOperatelogVO(log);
			}
		}
		
		m_voSpBill.getParentVO().setAttributeValue("coperatoridnow", m_sUserID);

		ArrayList osPrimaryKey = null;
		if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_check)) {
			//�̵�
			nc.ui.ic.ic261.SpecialHHelper.checkIsAlreadySigned(m_sOldPK);
			osPrimaryKey =
				(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("ADJUST", m_sSrcBillTypeCode, sBillDate, m_voSpBill, voaAllBill);
		} else if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)) {
			//�����ֱ�ӳ���,�������ֿ��ǻ�λ����,����Ҫ�ò��� m_voSpBill �� null ,
			//��̨�����ű��и���getVo() �Ŀշ�? �����Զ����
			//ת�ⵥ
			osPrimaryKey =
				(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVEOTHER", m_sSrcBillTypeCode, sBillDate, m_voSpBill, voaAllBill);
		
		} else if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder)) {
			if (voaAllBill != null) {
				osPrimaryKey =
					(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVEOTHER", m_sSrcBillTypeCode, sBillDate, null, voaAllBill);
			}

		} else if (
			m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_saleOut)
				|| m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_purchaseIn)) {

			//2004-8-03 ydy �ֲ����棬����ͬһ������
			nc.ui.pub.pf.PfUtilClient.processAction("SAVEOTHER", nc.vo.ic.pub.BillTypeConst.m_purchaseIn, sBillDate, m_voSpBill, voaAllBill);

			//if (voaAllBill != null) {
				//for (int i = 0; i < voaAllBill.length; i++) {
					//nc.ui.pub.pf.PfUtilClient.processAction("SAVE", nc.vo.ic.pub.BillTypeConst.m_otherIn, sBillDate, voaAllBill[i]);
				//}
			//}

		} else {
			//��װ����ж����̬ת����
			nc.ui.ic.ic231.SpecialHHelper.checkIsAlreadySigned(m_sOldPK);
			osPrimaryKey =
				(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVEOTHER", m_sSrcBillTypeCode, sBillDate, m_voSpBill, voaAllBill);
		}
		
		m_bIsSignOK = false;
		if (isSign){
			//�޸��ˣ������� �޸�ʱ�䣺2008-11-20 ����03:07:21 �޸�ԭ�򣺰�����ˢ�µ�Ҫ������һ��������VO�С�
			if (null != osPrimaryKey)
				for(int i = 0 ;i < voaAllBill.length ; i++){
					if (null == osPrimaryKey.get(i) || !(osPrimaryKey.get(i) instanceof ArrayList))
						continue;
					ArrayList alPK =(ArrayList) osPrimaryKey.get(i); 
					GeneralBillVO billVO = voaAllBill[i];
					if (alPK == null || alPK.size() < 3 || alPK.get(1) == null
							|| alPK.get(2) == null) {
					} else {
						int iRowCount = billVO.getItemCount();
						ArrayList alMyPK = (ArrayList) alPK.get(1);
						if (alMyPK == null || alMyPK.size() < (iRowCount + 1)
								|| alMyPK.get(0) == null || alMyPK.get(1) == null) {
	
						} else {
							// ��ͷ��OID
							String m_sCurBillOID = (String) alMyPK.get(0);
		
							billVO.getParentVO().setPrimaryKey(m_sCurBillOID);
							SMGeneralBillVO smbillvo = null;
							smbillvo = (SMGeneralBillVO) alPK.get(2);
						
							billVO.setSmallBillVO(smbillvo);
	
						}
					}
				}
			
			try{
				/*ArrayList<GeneralBillVO> newvoaAllBill = 	(java.util.ArrayList<GeneralBillVO>)nc.ui.ic.pub.tools.GenMethod.callICEJBService("nc.bs.ic.pub.bill.GeneralBillDMO", "queryBillBySourcePks", 
				          new Class[]{String.class,String[].class}, new Object[]{m_sSrcBillTypeCode,new String[]{m_voSpBill.getParentVO().getPrimaryKey()}});*/
			ArrayList<GeneralBillVO> listInBills = new ArrayList<GeneralBillVO>();
			ArrayList<GeneralBillVO> listOutBills = new ArrayList<GeneralBillVO>();
			for(GeneralBillVO vo:voaAllBill){
				setBillBCVOStatus(vo,nc.vo.pub.VOStatus.UNCHANGED);
				vo = getAuditVO(vo,null);
				if (vo.getCbilltypecode() != null && "4A".equals(vo.getCbilltypecode()))
					listInBills.add(vo);
				else if (vo.getCbilltypecode() != null && "4I".equals(vo.getCbilltypecode()))
					listOutBills.add(vo);
			}
			
			/*if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_disassembly)
					||m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_disassembly)){*/
				if (listOutBills.size() > 0){
					GeneralBillVO[] voOutBills = new GeneralBillVO[listOutBills.size()];
					listOutBills.toArray(voOutBills);
					nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4I",
							m_sLogDate,voOutBills);
				}
				if (listInBills.size() > 0){
					GeneralBillVO[] voInBills = new GeneralBillVO[listInBills.size()];
					listInBills.toArray(voInBills);
					nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4A",
							m_sLogDate,voInBills);
				}

			/*}
			else{
				if (listInBills.size() > 0){
					GeneralBillVO[] voInBills = new GeneralBillVO[listInBills.size()];
					listInBills.toArray(voInBills);
					nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4A",
							m_sLogDate,voInBills);
				}
				if (listOutBills.size() > 0){
					GeneralBillVO[] voOutBills = new GeneralBillVO[listOutBills.size()];
					listOutBills.toArray(voOutBills);
					nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4I",
							m_sLogDate,voOutBills);
				}
			}*/
			
			m_bIsSignOK = true;
			}catch(Exception e){
				m_bIsSignOK = false;
				if (e != null && e.getClass() == nc.vo.ic.pub.exp.OtherOut4MException.class)
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000401")/** @res "��ж�����ɵ�������ⵥ���µ�����ʵ�������Ӽ�����������ʵ�ʳ����׼�������ֶ�Ӧ���Ӽ�����"*/ + e.getMessage());
				else
					showErrorMessage(e.getMessage());	
			}
			
		}

	} catch (RightcheckException e) {
		showErrorMessage(e.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000164")/*@res "��"*/+"\n"+nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000165")/*@res "Ȩ��У�鲻ͨ��������ʧ�ܡ�"*/);
		getAccreditLoginDialog().setCorpID(m_sCorpID);
		getAccreditLoginDialog().clearPassWord();
		if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
			String sUserID = getAccreditLoginDialog().getUserID();
			if (sUserID == null) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000165")/*@res "Ȩ��У�鲻ͨ��������ʧ�ܡ�"*/);
			} else {
				for(GeneralBillVO voaBill :voaAllBill)
					voaBill.setAccreditBarcodeUserID(e.getFunCodeForRightCheck(), sUserID);
				//onSaveBaseKernel(voaAllBill,sUserID, sBillDate);
				onSaveBaseKernel(voaAllBill,m_sUserID, sBillDate,isSign);
			}
		} else {
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000165")/*@res "Ȩ��У�鲻ͨ��������ʧ�ܡ�"*/);

		}

	} catch (Exception e) {
		throw e;
	}

}

public void setBillBCVOStatus(GeneralBillVO bvo, int Status) {

	if (bvo != null && bvo.getItemVOs() != null) {
		GeneralBillItemVO itemVO = null;
		BarCodeVO[] bcvos = null;
		BarCodeVO[] bcvosTemp = null;
		java.util.ArrayList alBarcodeVO = null;
		for (int i = 0; i < bvo.getItemVOs().length; i++) {
			itemVO = bvo.getItemVOs()[i];
			bcvos = itemVO.getBarCodeVOs();
			if (bcvos != null) {
				alBarcodeVO = new java.util.ArrayList();
				for (int j = 0; j < bcvos.length; j++) {
					if (bcvos[j].getStatus() != nc.vo.pub.VOStatus.DELETED) {
						bcvos[j].setStatus(Status);
						alBarcodeVO.add(bcvos[j]);
					}
				}
				if (alBarcodeVO.size() > 0) {
					bcvosTemp = new BarCodeVO[alBarcodeVO.size()];
					alBarcodeVO.toArray(bcvosTemp);
					itemVO.setBarCodeVOs(bcvosTemp);
				} else {
					itemVO.setBarCodeVOs(null);
				}
			}

		}

	}

}

  public GeneralBillVO getAuditVO(GeneralBillVO voAudit,UFDateTime sysdatetime) {
    
//  ��������״̬δû�б��޸�
    setBillBCVOStatus(voAudit, nc.vo.pub.VOStatus.UNCHANGED);
    // ֧��ƽ̨��cloneһ�����Ա����Ժ�Ĵ���ͬʱ��ֹ�޸���m_voBill
    GeneralBillHeaderVO voHead = voAudit.getHeaderVO();

    // ǩ����
    voHead.setCregister(m_sUserID);
    // ���Բ��ǵ�ǰ��¼��λ�ĵ��ݣ����Բ���Ҫ�޸ĵ��ݡ�
    // voHead.setPk_corp(m_sCorpID);
    voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
    //voHead.setAttributeValue("taccounttime", sysdatetime.toString()); // ǩ��ʱ��//zhy2005-06-15ǩ��ʱ��=��½����+ϵͳʱ��

    // vo����Ҫ����ƽ̨������Ҫ���ɺ�ǩ�ֺ�ĵ���
    // voHead.setFbillflag(new
    // Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
    voHead.setCoperatoridnow(m_sUserID); // ��ǰ����Ա2002-04-10.wnj
    voHead.setAttributeValue("clogdatenow", m_sLogDate); // ��ǰ��¼����

    voAudit.setParentVO(voHead);

    // ���ݲֿ������òֿ��Ƿ����������� add by hanwei 2004-4-30
    getGenBillUICtl().setBillIscalculatedinvcost(voAudit);

    // ƽ̨����Ҫ�������ͷPK
    GeneralBillItemVO voaItem[] = voAudit.getItemVOs();
    int iRowCount = voAudit.getItemCount();
    for (int i = 0; i < iRowCount; i++) {
      // ��ͷPK
      voaItem[i].setCgeneralhid(voHead.getPrimaryKey());
      // set delete flag------- obligatory for ts test.
    }
    voAudit.setChildrenVO(voaItem);

    voAudit.setStatus(nc.vo.pub.VOStatus.UNCHANGED);
    setBillBCVOStatus(voAudit, nc.vo.pub.VOStatus.UNCHANGED);
    
    voAudit.setIsCheckCredit(true);
    voAudit.setIsCheckPeriod(true);
    voAudit.setIsCheckAtp(true);
    
    //nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();
    voAudit.setAccreditUserID(m_sUserID);
   // voAudit.setOperatelogVO(log);
    
    //���ڡ�������Ϣ
    voAudit.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OTHER;
    voAudit.m_sActionCode = "SIGN";
    
    return voAudit;

  }
  public GeneralBillUICtl m_GenBillUICtl;
	public GeneralBillUICtl getGenBillUICtl() {
		if (m_GenBillUICtl == null)
			m_GenBillUICtl = new GeneralBillUICtl();
		return m_GenBillUICtl;
	}  

/**
 * �˴����뷽��˵����
 * �������ڣ�(2004-4-19 20:35:29)
 * @return java.lang.String
 * @param voaAllBill nc.vo.ic.pub.bill.GeneralBillVO[]
 * 
 * �޸��ˣ������� �޸�ʱ�䣺2008-11-17 ����04:08:40 �޸�ԭ���ȳ��ⵥ����ⵥ��
 */
protected GeneralBillVO[]  onSaveGetAllBillVO() throws Exception {
	//��������
		//VOУ��
		GeneralBillVO[] voaAllBill=null;
		GeneralBillVO[] voaOutBill = getCheckedOutBills();
		//�д�������
		if (m_outVOs != null && m_outVOs.size() != 0 && (voaOutBill == null || voaOutBill.length == 0))
			return voaAllBill;
		GeneralBillVO[] voaInBill = null;
		//�������ֱ�ӳ�����
		if (!m_bIsDirectOut) {
			try {
				voaInBill = getCheckedInBills();
			} catch (Exception e) {
				
				SCMEnv.error(e);
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000167")/*@res "����д������������ⵥ��"*/ + e.getMessage());

			}
			//�д� �� ����
			if (m_inVOs != null && m_inVOs.size() != 0 && (voaInBill == null || voaInBill.length == 0))
				return voaAllBill;
		} else {
			//���=����
			synOut2InBill();//�������ʱͬ�����ͳ���,��ô����ⵥ���޸ĵ���Ϣ���ܱ�ͬ���ɳ��ⵥ�ϵ���Ϣ,�������ݴ���
			try {
				voaInBill = getCheckedInBills();
			} catch (Exception e1) {
				nc.vo.scm.pub.SCMEnv.error(e1);
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000167")/*@res "����д������������ⵥ��"*/ + e1.getMessage());

			}
			if (voaInBill == null || voaInBill.length == 0)
				return voaAllBill;
		}

		//���vo����
		int iOutBillCount = voaOutBill == null ? 0 : voaOutBill.length;
		//����

		if (m_bIsDirectOut) {

			//--
			voaAllBill = new GeneralBillVO[iOutBillCount + (voaInBill == null ? 0 : voaInBill.length)];
			if (voaOutBill != null) {
				for (int i = 0; i < voaOutBill.length; i++) {
					//�ó���������
					if (m_sBillDate == null) {
						if (voaOutBill[i].getHeaderVO().getDbilldate() != null) {
							m_sBillDate = voaOutBill[i].getHeaderVO().getDbilldate().toString().trim();
							if (m_sBillDate.length() == 0)
								m_sBillDate = null;
						}

					}
					voaAllBill[i] = voaOutBill[i];
					voaAllBill[i].getHeaderVO().setCoperatoridnow(m_sUserID);
				}
			}
			if (voaInBill != null) {
				for (int i = 0; i < voaInBill.length; i++) {
					//���뵥������
					if (m_sBillDate == null) {
						if (voaInBill[i].getHeaderVO().getDbilldate() != null) {
							m_sBillDate = voaInBill[i].getHeaderVO().getDbilldate().toString().trim();
							if (m_sBillDate.length() == 0)
								m_sBillDate = null;
						}

					}
					for (int j = 0; j < voaInBill[i].getItemCount(); j++) {
						//�������
						voaInBill[i].setItemValue(j, "ninnum", voaOutBill[i].getItemValue(j, "noutnum"));
						//��⸨����
						voaInBill[i].setItemValue(j, "ninassistnum", voaOutBill[i].getItemValue(j, "noutassistnum"));
						nc.vo.ic.pub.sn.SerialVO[] svo = voaOutBill[i].getItemVOs()[j].getSerial();
						nc.vo.ic.pub.sn.SerialVO[] svobak = voaInBill[i].getItemVOs()[j].getSerial();
						if (svo != null && voaInBill[i].getHeaderVO().getIsLocatorMgt().intValue()!=1) {
							svobak = new nc.vo.ic.pub.sn.SerialVO[svo.length];
							for (int k = 0; k < svobak.length; k++) {
								if (svobak[k] == null)
									svobak[k] = new nc.vo.ic.pub.sn.SerialVO();
								svobak[k].setStatus(2);
								svobak[k].setSnStatus(new Integer(-1));
								svobak[k].setCserialid(null);
								svobak[k].setVserialcode(svo[k].getVserialcode());
								svobak[k].setDbillindate(svo[k].getDbilloutdate());
								//svobak[k].setDbilloutdate(null);

							}
						voaInBill[i].getItemVOs()[j].setSerial(svobak);
						}


						//ֱ��ת�⣬������ֱ��ת�� add by hanwei 2004-04-12
						nc.vo.ic.pub.bc.BarCodeVO[] outBarcodeVOs = voaOutBill[i].getItemVOs()[j].getBarCodeVOs();
						if (outBarcodeVOs != null) {
							voaInBill[i].getItemVOs()[j].setBarCodeVOs(outBarcodeVOs);
						}
					}
					if (m_inVOs != null) {
						voaInBill[i].setHeaderValue("cwarehouseid", ((GeneralBillVO) m_inVOs.get(i)).getHeaderValue("cwarehouseid"));
					}
					//���������ⵥ��VOת����������ⵥ��VO
					//voaAllBill[i + iInBillCount] = transferOut2InVO(voaInBill[i]);
					voaAllBill[i + iOutBillCount] = voaInBill[i];
					//
					voaAllBill[i + iOutBillCount].getHeaderVO().setCoperatoridnow(m_sUserID);
				}
			}

		}
		//����ֱ�ӳ���
		else {
			voaAllBill = new GeneralBillVO[iOutBillCount + (voaInBill == null ? 0 : voaInBill.length)];
			
			if (voaOutBill != null) {
				for (int i = 0; i < voaOutBill.length; i++) {
					//���뵥������
					if (m_sBillDate == null) {
						if (voaOutBill[i].getHeaderVO().getDbilldate() != null) {
							m_sBillDate = voaOutBill[i].getHeaderVO().getDbilldate().toString().trim();
							if (m_sBillDate.length() == 0)
								m_sBillDate = null;
						}

					}
					voaAllBill[i] = voaOutBill[i];
					voaAllBill[i].getHeaderVO().setCoperatoridnow(m_sUserID);
				}
			}
			
			
			if (voaInBill != null) {
				for (int i = 0; i < voaInBill.length; i++) {
					//���뵥������
					if (m_sBillDate == null) {
						if (voaInBill[i].getHeaderVO().getDbilldate() != null) {
							m_sBillDate = voaInBill[i].getHeaderVO().getDbilldate().toString().trim();
							if (m_sBillDate.length() == 0)
								m_sBillDate = null;
						}

					}
					voaAllBill[i + iOutBillCount] = voaInBill[i];
					voaAllBill[i + iOutBillCount].getHeaderVO().setCoperatoridnow(m_sUserID);
				}
			}


		}

	return voaAllBill;
}

/**
 * �˴����뷽��˵����
   ���ñ�����״̬
 * �������ڣ�(2004-4-19 20:30:17)
 */
protected void onSaveSetUI(Boolean isSign) {
		m_bIsTabInSaved = true;
		m_bIsTabOutSaved = true;
		TabStateChanged();

	    //�ñ��桢��λ�����кš�ɾ�����л���ť
		getUIBtnSave().setEnabled(false);
		getUIBtnSaveSign().setEnabled(false);
		//getUIBtnSwitch().setEnabled(true);
		getUIBtnSpace().setEnabled(true);
		getUIBtnSerials().setEnabled(true);
		getUIBtnDelRow().setEnabled(false);
		getUIBtnBarcode().setEnabled(false);

		//�ùرհ�ť
		getbtnClose().setEnabled(
			!(((m_bIsTabInHaveValue) && (m_bIsTabInSaved == false)) || ((m_bIsTabOutHaveValue) && (m_bIsTabOutSaved == false))));
		getUIBtnCancel().setEnabled((m_bIsTabInSaved == false) && (m_bIsTabOutSaved == false));

		getChldClientUIIn().setCardPanelEnable(!m_bIsTabOutSaved);
		getChldClientUIOut().setCardPanelEnable(!m_bIsTabInSaved);
		getChldClientUIIn().setCardMode(BillMode.Browse);
		getChldClientUIOut().setCardMode(BillMode.Browse);

		//ljun���ݱ���󣬲�������༭��ֱ�ӱ��治�ܻ�õ���ID
		getUIBtnBarcode().setEnabled(false);
		getUIBtnBarcode().setEnabled(false);
		if (isSign){
			if (m_bIsSignOK)
				nc.ui.pub.beans.MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000007")/*����ɹ�&ǩ�ֳɹ�*/, NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000008")/*���е��ݾ��ѱ��沢��ǩ�ֳɹ���*/);
			else
				nc.ui.pub.beans.MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000009")/*����ɹ�&ǩ��ʧ��*/, NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000010")/*���е��ݾ��ѱ��浫��ǩ��ʧ�ܣ�*/);
		}
		else
			nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000006")/*@res "����ɹ�"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000168")/*@res "���е��ݾ��ѱ��棡"*/);

		m_bIsOK = true;
		
		//����ɹ��󼴹رմ˶Ի���
		closeOK();
	}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2004-4-19 20:26:37)
 * @param newCorpID java.lang.String
 */
public void setCorpID(java.lang.String newCorpID) {
	m_sCorpID = newCorpID;
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-12-04 19:00:33)
 * @param newIsDirectOut boolean
 */
public void setIsDirectOut(boolean newIsDirectOut) {
	m_bIsDirectOut = newIsDirectOut;
}

/**
 * �˴����뷽��˵����
   ���õ���״̬Ϊֱ��ת��
   ic_general_h bdirecttranflag��true
 * �������ڣ�(2004-4-19 10:23:01)
 * @param albillVOs java.util.ArrayList
 */
public void setIsDirectOutFlag(ArrayList albillVOs) {
	if (albillVOs != null && albillVOs.size() > 0) {
		nc.vo.ic.pub.bill.GeneralBillVO billvo = null;
		nc.vo.ic.pub.bill.GeneralBillHeaderVO billheadvo = null;
		UFBoolean ufbTrue = new UFBoolean(true);
		for (int i = 0; i < albillVOs.size(); i++) {
			billvo = (GeneralBillVO) albillVOs.get(i);
			if (billvo != null) {
				billheadvo = billvo.getHeaderVO();
				if (billheadvo != null) {
					billheadvo.setBdirecttranflag(ufbTrue);
				}
			}
		}

	}

}

/**
 * �˴����뷽��˵����
   �������ⵥVO
 * �������ڣ�(2003-11-3 16:50:29)
 * @param newSpBill nc.vo.ic.pub.bill.SpecialBillVO
 */
public void setSpBill(AggregatedValueObject newSpBill) {
	m_voSpBill = newSpBill;
	//if (getChldClientUIIn().getBillCardPanel().getBodyItems() != null)
		//for (int i = 0; i < getChldClientUIIn().getBillCardPanel().getBodyItems().length; i++) {
			//getChldClientUIIn().getBillCardPanel().getBodyItems()[i].setEdit(true);
		//}
}

/**
 * ClientUIInAndOut ������ע�⡣
 * @param parent java.awt.Container
 * @param title java.lang.String
 * sBillTypeCode ��������
 AggregatedValueObject:���ⵥVO(Դ����)
 */
public void setVO(
	AggregatedValueObject voBill,
	ArrayList invos,
	ArrayList outvos,
	String sSrcBillTypeCode,
	String sOldPK,
	String sCorpPK,
	String sUserID) {
    long lTime = System.currentTimeMillis();
	if (((invos == null) && (outvos == null))
		|| (sSrcBillTypeCode == null)
		|| (sSrcBillTypeCode.trim().length() == 0)
		|| (sCorpPK == null)
		|| (sCorpPK.trim().length() == 0)
		|| (sUserID == null)
		|| (sUserID.trim().length() == 0)
		|| (voBill == null)) {
//		nc.ui.ic.pub.tools.ICEnv.out("in/out data err.ret...");
		return;
	}

	m_sSrcBillTypeCode = sSrcBillTypeCode.trim();
	m_sCorpPK = sCorpPK.trim();
	m_sUserID = sUserID.trim();
	m_sOldPK = sOldPK;
	m_voSpBill = voBill;

	afterInit();

	if (invos != null) {
    GeneralBillVO billvo = null;
    ArrayList<GeneralBillItemVO[]> itemVos = new ArrayList<GeneralBillItemVO[]> ();
    
    for(int i=0;i<invos.size();i++){
      billvo = (GeneralBillVO)invos.get(i);
      if(billvo==null)
        continue;
      
      itemVos.add(billvo.getItemVOs());
      
      if(GenMethod.isSEmptyOrNull((String)billvo.getHeaderValue("clastmodiid"))){
        billvo.setHeaderValue("clastmodiid", ClientEnvironment.getInstance().getUser().getPrimaryKey());
        billvo.setHeaderValue("clastmodiname", ClientEnvironment.getInstance().getUser().getUserName());
      }
    }
		getChldClientUIIn().setUITxtFldStatus(getUITxtFldStatus());
		getChldClientUIIn().onAdd(true, null);
		//excecute formulas
		exeAllFormulas(getChldClientUIIn(),invos);
		//added by lirr 2009-01-12
		if(null != itemVos && itemVos.size() > 0){
			for(GeneralBillItemVO[] item : itemVos){
				getChldClientUIIn().getBillCardPanel().getBillModel().execFormulasWithVO(item, new String []{"nplannedmny->ninnum*nplannedprice;"});
				
			}
			
		}//end
		getChldClientUIIn().setScaleOfListData(invos);

		getChldClientUIIn().setAllData(invos);
		
		//��¡֮����ֹ���޸ġ�
		m_inVOs = (ArrayList) invos.clone();
		
		//���չ���
		BillItem bi = getChldClientUIIn().getBillCardPanel().getHeadItem("cotherwhid");
        RefFilter.filtWh(bi,m_sCorpID,null);

		if (getChldClientUIIn().m_htBItemEditFlag != null) {
			UFBoolean bEdit = new UFBoolean(true);
			for (int i = 0; i < getChldClientUIIn().getBillCardPanel().getBodyItems().length; i++) {
				if (getChldClientUIIn().getBillCardPanel().getBodyItems()[i].isEdit())
					getChldClientUIIn().m_htBItemEditFlag.put(getChldClientUIIn().getBillCardPanel().getBodyItems()[i].getKey(), bEdit);
			}
		}
		//zhy2005-05-27Ч�����⣬��getChldClientUIOut().setAllData(outvos);ʱ�Ѿ�ָ���0�������ˣ������ڴ˲���Ҫ����һ�飬ע�͵�����һ��
//		getChldClientUIIn().selectListBill(0);
		getChldClientUIIn().removeListHeadMouseListener();
		//getChldClientUIIn().setBillVO((GeneralBillVO) invos.get(0));
		getUITabPane().setEnabledAt(0, true);
		getUITabPane().setSelectedIndex(0);
		m_bIsTabInHaveValue = true;
	} else {
		   
		m_inVOs = null;
		getUIPaneIn().setEnabled(false);
		getUITabPane().setEnabledAt(0, false);
		m_bIsTabInHaveValue = false;
	}
	if (outvos != null) {
		ArrayList<GeneralBillItemVO[]> itemVos = new ArrayList<GeneralBillItemVO[]> ();
		
    GeneralBillVO billvo = null;
    for(int i=0;i<outvos.size();i++){
      billvo = (GeneralBillVO)outvos.get(i);
      if(billvo==null)
        continue;
      itemVos.add(billvo.getItemVOs());
      if(GenMethod.isSEmptyOrNull((String)billvo.getHeaderValue("clastmodiid"))){
        billvo.setHeaderValue("clastmodiid", ClientEnvironment.getInstance().getUser().getPrimaryKey());
        billvo.setHeaderValue("clastmodiname", ClientEnvironment.getInstance().getUser().getUserName());
      }
    }
    
		getChldClientUIOut().setUITxtFldStatus(getUITxtFldStatus());
		getChldClientUIOut().onAdd(true, null);
		//zhy2005-05-26��ע�ͣ��˴����Ա�ͷ�����е��ݱ�ͷ������ִ�й�ʽ
		//excecute formulas
		exeAllFormulas(getChldClientUIOut(),outvos);
//		added by lirr 2009-01-12
		if(null != itemVos && itemVos.size() > 0){
			for(GeneralBillItemVO[] item : itemVos){
				getChldClientUIIn().getBillCardPanel().getBillModel().execFormulasWithVO(item, new String []{"nplannedmny->noutnum*nplannedprice;"});
				
			}
			
		}//end
		getChldClientUIOut().setScaleOfListData(outvos);
		getChldClientUIOut().setAllData(outvos);
	
		//��¡֮����ֹ���޸ġ�
		m_outVOs = (ArrayList) outvos.clone();
		
		//���չ���
		BillItem bi = getChldClientUIOut().getBillCardPanel().getHeadItem("cotherwhid");
        RefFilter.filtWh(bi,m_sCorpID,null);

        //zhy2005-05-27Ч�����⣬��getChldClientUIOut().setAllData(outvos);ʱ�Ѿ�ָ���0�������ˣ������ڴ˲���Ҫ����һ�飬ע�͵�����һ��
//		getChldClientUIOut().selectListBill(0);
		//getChldClientUIOut().setBillVO((GeneralBillVO) outvos.get(0));
		getUITabPane().setEnabledAt(1, true);
		getUITabPane().setSelectedIndex(1);
		m_bIsTabOutHaveValue = true;
		//zhy2005-05-27���Ч�����⣬���еķ�����Ҫ�Ǵ������θı���Զ�����ʧЧ���ڵ���Ϣ���˴���ҪΪ��ʼ�����̣���δ�Ķ����Σ��ʴ˳��ɲ����е��ã�ע��������
//		getChldClientUIOut().setAllLotRefAuto();
		getChldClientUIOut().removeListHeadMouseListener();
	} else {
		m_outVOs = null;
		getUIPaneOut().setEnabled(false);
		getUITabPane().setEnabledAt(1, false);
		m_bIsTabOutHaveValue = false;
	}

	
	TabStateChanged();
}

/**
 * ClientUIInAndOut ������ע�⡣
 * @param parent java.awt.Container
 * @param title java.lang.String
 * sBillTypeCode ��������
   ֱ��ת��ʹ��
 */
public void setVO4Direct(
	ArrayList invos,
	ArrayList outvos,
	String sSrcBillTypeCode,
	String sOldPK,
	String sCorpPK,
	String sUserID) {

	if (((invos == null) && (outvos == null))
		|| (sSrcBillTypeCode == null)
		|| (sSrcBillTypeCode.trim().length() == 0)
		|| (sCorpPK == null)
		|| (sCorpPK.trim().length() == 0)
		|| (sUserID == null)
		|| (sUserID.trim().length() == 0)) {
//		nc.ui.ic.pub.tools.ICEnv.out("in/out data err.ret...");
		return;
	}

	m_sSrcBillTypeCode = sSrcBillTypeCode.trim();
	m_sCorpPK = sCorpPK.trim();
	m_sUserID = sUserID.trim();
	m_sOldPK = sOldPK;

	afterInit();
	m_bIsDirectOut = true;
	if (invos != null) {
		//��¡֮����ֹ���޸ġ�
		m_inVOs = (ArrayList) invos.clone();
		getChldClientUIIn().setUITxtFldStatus(getUITxtFldStatus());
		getChldClientUIIn().onAdd(true, null);

//		excecute formulas
		exeAllFormulas(getChldClientUIIn(),invos);
		getChldClientUIIn().setAllData(invos);
//		//���չ���
//		BillItem bi = getChldClientUIIn().getBillCardPanel().getHeadItem("cotherwhid");
//        RefFilter.filtWh(bi,m_sCorpID,null);

        //zhy2005-05-27Ч�����⣬��getChldClientUIOut().setAllData(outvos);ʱ�Ѿ�ָ���0�������ˣ������ڴ˲���Ҫ����һ�飬ע�͵�����һ��
//		getChldClientUIIn().selectListBill(0);
		if (getChldClientUIIn().m_htBItemEditFlag != null) {
			UFBoolean bEdit = new UFBoolean(false);
			nc.ui.pub.bill.BillItem bBI = null;

			for (int i = 0; i < getChldClientUIIn().getBillCardPanel().getBodyItems().length; i++) {
				bBI = getChldClientUIIn().getBillCardPanel().getBodyItems()[i];
				if (bBI.isEdit())
					getChldClientUIIn().m_htBItemEditFlag.put(bBI.getKey(), bEdit);
			}
		}
		getChldClientUIIn().removeListHeadMouseListener();
		getUITabPane().setEnabledAt(0, true);
		m_bIsTabInHaveValue = true;
	} else {
		m_inVOs = null;
		getUIPaneIn().setEnabled(false);
		getUITabPane().setEnabledAt(0, false);
		m_bIsTabInHaveValue = false;
	}
	if (outvos != null) {
		//��¡֮����ֹ���޸ġ�
		m_outVOs = (ArrayList) outvos.clone();
		getChldClientUIOut().setUITxtFldStatus(getUITxtFldStatus());
		getChldClientUIOut().onAdd(true, null);

        //excecute formulas
		exeAllFormulas(getChldClientUIOut(),outvos);
		getChldClientUIOut().setAllData(outvos);

		//���չ���
		BillItem bi = getChldClientUIOut().getBillCardPanel().getHeadItem("cotherwhid");
        RefFilter.filtWh(bi,m_sCorpID,null);

        //zhy2005-05-27Ч�����⣬��getChldClientUIOut().setAllData(outvos);ʱ�Ѿ�ָ���0�������ˣ������ڴ˲���Ҫ����һ�飬ע�͵�����һ��
//		getChldClientUIOut().selectListBill(0);
		getUITabPane().setEnabledAt(1, true);
		getUITabPane().setSelectedIndex(1);
		m_bIsTabOutHaveValue = true;
		//zhy2005-05-27���Ч�����⣬���еķ�����Ҫ�Ǵ������θı���Զ�����ʧЧ���ڵ���Ϣ���˴���ҪΪ��ʼ�����̣���δ�Ķ����Σ��ʴ˳��ɲ����е��ã�ע��������
//		getChldClientUIOut().setAllLotRefAuto();
		getChldClientUIOut().removeListHeadMouseListener();
	} else {
		m_outVOs = null;
		getUIPaneOut().setEnabled(false);
		getUITabPane().setEnabledAt(1, false);
		m_bIsTabOutHaveValue = false;
	}
	TabStateChanged();

}

/**
 * �����ߣ����˾�
 * ���ܣ����ص���ʾ��ʾ��Ϣ�Ի�����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void showErrorMessage(String sMsg) {
		nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, sMsg);

}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-12-06 13:50:22)
 * �޸��ˣ������� �޸����ڣ�2007-12-26����03:12:24 �޸�ԭ�򣺳��ⵥ����ͬ��"Ƥ��������"����ⵥ
 * �޸��ˣ������� �޸�ʱ�䣺2008-11-20 ����02:14:58 �޸�ԭ��������ⵥ�ϵ��ǿյĻ����Ͳ�Ҫͬ���ˡ�
 */
private void synOut2In() {
	String[] saOutBillItemKey =
		new String[] {
			"castunitid",
			"vfree0","vfree1","vfree2","vfree3","vfree4","vfree5",
			"vbatchcode",
			"scrq",
			"dvalidate",
			"noutnum",
			"noutassistnum",
			"noutgrossnum",
			"ntarenum",
			"nprice",
			"nmny","dbizdate" ,"cvendorid","cprojectid","cprojectphaseid","vnote","hsl","cinvbasid"};

	String[] saInBillItemKey =
		new String[] {
			"castunitid",
			"vfree0","vfree1","vfree2","vfree3","vfree4","vfree5",
			"vbatchcode",
			"scrq",
			"dvalidate",
			"ninnum",
			"ninassistnum",
			"ningrossnum",
			"ntarenum",
			"nprice",
			"nmny" ,"dbizdate","cvendorid","cprojectid","cprojectphaseid","vnote","hsl","cinvbasid"};

	
	GeneralBillVO voInBill=getChldClientUIIn().getBillVO();
	GeneralBillVO voOutBill=getChldClientUIOut().getBillVO();
	
	if (getChldClientUIOut() != null && getChldClientUIOut() != null) {
		//���к�
		if (getChldClientUIOut().getSerialData() != null) {
			ArrayList alSNOut = getChldClientUIOut().getSerialData();
			ArrayList alSNIN = getChldClientUIIn().getSerialData();
			if (voInBill.getHeaderVO().getIsLocatorMgt().intValue()
				== 1
				&& alSNIN != null&& alSNIN .size()>0) {
				//���Ϊ��λ����,�Ҵ������кţ�����ͬ�����кţ������λ��ʧ

			} else {
				//�ж��������к�
				alSNIN = new ArrayList(alSNOut.size());
				nc.vo.ic.pub.sn.SerialVO[] voSNs = null;
				for (int i = 0; i < alSNOut.size(); i++) {
					voSNs = (nc.vo.ic.pub.sn.SerialVO[]) alSNOut.get(i);
					if (voSNs != null) {
						nc.vo.ic.pub.sn.SerialVO[] voSNins = new nc.vo.ic.pub.sn.SerialVO[voSNs.length];
						for (int j = 0; j < voSNs.length; j++) {
							voSNins[j] = (nc.vo.ic.pub.sn.SerialVO) voSNs[j].clone();
							voSNins[j].setCcustomerid(null);
							voSNins[j].setCfreezeid(null);
							voSNins[j].setCgeneralbid(null);
							voSNins[j].setCinbillbodyid(null);
							voSNins[j].setCinbillheadid(null);
							voSNins[j].setCserialid(null);
							voSNins[j].setCspaceid(null);
							voSNins[j].setCwarehouseid(null);
							voSNins[j].setVinbillcode(null);
							voSNins[j].setDbillindate(null);
							voSNins[j].setCinbilltypecode(null);
							voSNins[j].setSnStatus(new Integer(VOStatus.NEW));

						}
						alSNIN.add(voSNins);
					} else
						alSNIN.add(null);

				}
				getChldClientUIIn().setSerialData(alSNIN);
			}
		}
        
		getChldClientUIOut().getBillCardPanel().getBillModel().setNeedCalculate(false);
		String sOutRowNO = null;
		String sInRowNO = null;
	//	GeneralBillVO vo = (GeneralBillVO)m_inVOs.get(getChldClientUIIn().getLastSelListHeadRow());
		GeneralBillVO vo = getChldClientUIIn().getBillVO();//(GeneralBillVO)m_inVOs.get(getChldClientUIIn().getLastSelListHeadRow());
		int iLocatorMgt = vo.getHeaderVO().getIsLocatorMgt().intValue();
		
		int iOutRows =getChldClientUIOut().getBillCardPanel().getRowCount();
		for (int i = 0;i < iOutRows;i++) {
		    //����������ⵥ���ʱ�в��У�������ⵥ������Ӧ���������������С�
		    //���к��Ƿ�ƥ������
		    sOutRowNO = (String)getChldClientUIOut().getBillCardPanel().getBodyValueAt(i,"crowno");
		    sInRowNO = (String)getChldClientUIIn().getBillCardPanel().getBodyValueAt(i,"crowno");
		    if(sOutRowNO!=null&&sInRowNO!=null&&!sOutRowNO.equals(sInRowNO)){//�������
		        GeneralBillItemVO outvo = (GeneralBillItemVO)voOutBill.getItemVOs()[i].clone();
		        outvo.setAttributeValue("ccorrespondcode",null);
		        outvo.setAttributeValue("ccorrespondtype",null);
		        outvo.setAttributeValue("ccorrespondhid",null);
		        outvo.setAttributeValue("vcorrespondrowno",null);
		        outvo.setAttributeValue("ccorrespondbid",null);
		        //�����Ӧ����Ӧ������Ӧ��
		        outvo.setNshouldinnum(outvo.getNshouldoutnum());
		        outvo.setNneedinassistnum(outvo.getNshouldoutassistnum());
		        outvo.setNshouldoutnum(null);
		        outvo.setNneedinassistnum(null);

		        outvo.setCgeneralhid(null);
		        outvo.setCgeneralbid(null);
		        outvo.setLocator(null);
		        getChldClientUIIn().getBillCardPanel().getBillTable().setRowSelectionInterval(i,i);
		        getChldClientUIIn().getBillCardPanel().insertLine();//
		        getChldClientUIIn().getBillCardPanel().getBillModel().setBodyRowVO(outvo,i);
		        getChldClientUIIn().getMVOBill().setItem(i,outvo);

		    }else if(sInRowNO==null&&i<iOutRows){//����׷����
		        GeneralBillItemVO outvo = (GeneralBillItemVO)voOutBill.getItemVOs()[i].clone();
		        outvo.setAttributeValue("ccorrespondcode",null);
		        outvo.setAttributeValue("ccorrespondtype",null);
		        outvo.setAttributeValue("ccorrespondhid",null);
		        outvo.setAttributeValue("vcorrespondrowno",null);
		        outvo.setAttributeValue("ccorrespondbid",null);
		        outvo.setAttributeValue("ccorrespondbid",null);
		        outvo.setNshouldinnum(outvo.getNshouldoutnum());

		        outvo.setCgeneralhid(null);
		        outvo.setCgeneralbid(null);
		        outvo.setLocator(null);
		        getChldClientUIIn().getBillCardPanel().addLine();//
		        getChldClientUIIn().getBillCardPanel().getBillTable().setRowSelectionInterval(i,i);
		        getChldClientUIIn().getBillCardPanel().getBillModel().setBodyRowVO(outvo,i);
		        getChldClientUIIn().getMVOBill().setItem(i,outvo);

		    }
		        
			for (int j = 0; j < saOutBillItemKey.length; j++){
				if (null != getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, saOutBillItemKey[j])){
				    getChldClientUIIn().getBillCardPanel().setBodyValueAt(getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, saOutBillItemKey[j]),i,saInBillItemKey[j]);
				    getChldClientUIIn().getMVOBill().setItemValue(i,saInBillItemKey[j],getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, saOutBillItemKey[j]));
				}
			}
			for (int j = 1; j <= 20; j++){
				String def="pk_defdoc"+String.valueOf(j);
				if (null != getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def)){
				    getChldClientUIIn().getBillCardPanel().setBodyValueAt(getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def),i,def);
				    getChldClientUIIn().getMVOBill().setItemValue(i,def,getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def));
				}
			    def="vuserdef"+String.valueOf(j);
			    if (null != getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def)){
				    getChldClientUIIn().getBillCardPanel().setBodyValueAt(getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def),i,def);
				    getChldClientUIIn().getMVOBill().setItemValue(i,def,getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def));
			    }
			
			}
			
			
		//	vo.setItem(i, (GeneralBillItemVO)getChldClientUIIn().getBillCardPanel().getBillModel().getBodyValueRowVO(i,GeneralBillItemVO.class.getName()));
	
		}	
		
			voInBill=getChldClientUIIn().getBillVO();
			m_inVOs.set(getChldClientUIIn().getLastSelListHeadRow(),voInBill);
			
//			getChldClientUIIn().setAllData(m_inVOs);//�˴����Ӱ��Ч��,����ѭ����
			
			
			
			
			
			LocatorVO[] voaLoc = null;
			GeneralBillItemVO[] voInItems=voInBill.getItemVOs();
			for(int i=0;i<voInItems.length;i++){
				//��λͬ������,���ⵥ�޸���������ⵥ�Ļ�λ����Ҫ�޸� 05-05-14 lj
				//�����λ���ݲ���һ�У�������ջ�λ���ݣ����Ϊһ�У��޸Ļ�λ���ݵ�����Ϊͬ������
				if (iLocatorMgt == 1){
					voaLoc =voInItems[i].getLocator();
				
					if (voaLoc!=null && voaLoc.length>1){
						for (int m=0;m<voaLoc.length;m++){
							voaLoc[m] = null;
						}
	
						getChldClientUIIn().getLocatorData().set(i,voaLoc);
					}
					if (voaLoc!=null && voaLoc.length == 1 && voaLoc[0]!=null ){
						voaLoc[0].setAttributeValue( "ninspacenum", getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, "noutnum") );	
						voaLoc[0].setAttributeValue( "ninspaceassistnum",getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, "noutassistnum") );
						((LocatorVO[])getChldClientUIIn().getLocatorData().get(i))[0].setAttributeValue( "ninspacenum", getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, "noutnum") );
						((LocatorVO[])getChldClientUIIn().getLocatorData().get(i))[0].setAttributeValue( "ninspaceassistnum", getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, "noutassistnum") );
						
					}
				}
				//ͬ������
				voInItems[i].setBarCodeVOs(voOutBill.getItemVOs()[i].getBarCodeVOs());

				
		}

		getChldClientUIIn().setAllData(m_inVOs);
		getChldClientUIIn().getBillCardPanel().getBillModel().setNeedCalculate(true);
		getChldClientUIIn().getBillCardPanel().getBillModel().reCalcurateAll();
		
		
	}

}

/**
 * �˴����뷽��˵����
   ͬ�����ݵ�����VO
 * �������ڣ�(2004-4-22 11:47:23)
 * @param billItemPnlIn nc.vo.ic.pub.bill.GeneralBillItemVO[]
 * @param billItemPnlOut nc.vo.ic.pub.bill.GeneralBillItemVO[]
 */
private void synOut2InBarcode(GeneralBillItemVO[] billItemPnlIn, GeneralBillItemVO[] billItemPnlOut) {

	if (billItemPnlIn != null
		&& billItemPnlIn.length > 0
		&& billItemPnlOut != null
		&& billItemPnlOut.length > 0
		&& billItemPnlOut.length == billItemPnlIn.length) {
		nc.vo.ic.pub.bc.BarCodeVO[] barcodevos = null;
		for (int i = 0; i < billItemPnlIn.length; i++) {
			barcodevos = billItemPnlOut[i].getBarCodeVOs();
			billItemPnlIn[i].setBarCodeVOs(barcodevos);
		}
	}
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-12-06 13:50:22)
 */
private void synOut2InBill() {
	if (getChldClientUIOut() != null && getChldClientUIOut() != null 
      && getChldClientUIOut().getCardTableRowNum()>0 && getChldClientUIIn().getCardTableRowNum()==getChldClientUIOut().getCardTableRowNum() ) {
	//	nc.ui.pub.bill.BillCardPanel pnlIn=getChldClientUIIn().getBillCardPanel();

//		GeneralBillItemVO[] billItemPnlIn= (GeneralBillItemVO[])getChldClientUIIn().getBillVO().getChildrenVO();
//		GeneralBillItemVO[] billItemPnlOut= (GeneralBillItemVO[])getChldClientUIOut().getBillVO().getChildrenVO();
//		//ͬ����������
//	 	synOut2InBarcode(billItemPnlIn,billItemPnlOut);

		synOut2In();
	

	}

}
}