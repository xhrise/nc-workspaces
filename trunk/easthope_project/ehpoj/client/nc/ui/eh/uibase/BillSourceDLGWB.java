package nc.ui.eh.uibase;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pub.pf.AbstractReferQueryUI;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.querytemplate.IBillReferQuery;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * ������Դ �������նԻ���
 *
 * @author ���ھ� 2001-7-6
 * @modifier �׾� 2005-2-22 ����getUIDialogContentPane()����,���ڶ�̬
 * @modifier leijun 2006-7-4 ��ӦV5����ģ����޸ģ�����һЩ���÷���
 * @modifier leijun 2007-5-19 �滻Ϊ�µĲ�ѯģ��
 */
public class BillSourceDLGWB extends AbstractReferQueryUI implements ActionListener,
		BillEditListener, BillTableMouseListener, ListSelectionListener {
	protected BillListPanel ivjbillListPanel = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjbtnCancel = null;

	private UIButton ivjbtnOk = null;
	
	private UIButton selectall = null;
	
	private UIButton cancelselect = null;

	private UIButton ivjbtnQuery = null;

	private UIPanel ivjPanlCmd = null;

	//����vo,����vo,�ӱ�vo
	protected String m_billVo = null;

	protected String m_billHeadVo = null;

	protected String m_billBodyVo = null;

	//��ѯ�������
	protected String m_whereStr = null;

	//���صļ���Vo
	protected AggregatedValueObject retBillVo = null;

	//���ؼ���VO����
	protected AggregatedValueObject[] retBillVos = null;

	protected boolean isRelationCorp = true;

	//����������С��λ��
	protected Integer m_BD501 = null;

	//����������С��λ��
	protected Integer m_BD502 = null;

	//����С��λ��
	protected Integer m_BD505 = null;

	//������С��λ��
	protected Integer m_BD503 = null;

	private class HeadRowStateListener implements IBillModelRowStateChangeEventListener {

		public void valueChanged(RowStateChangeEvent e) {
			if (e.getRow() != getbillListPanel().getHeadTable().getSelectedRow()) {
				headRowChange(e.getRow());
			}

			BillModel model = getbillListPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();
			if (e.isSelectState()) {
				getbillListPanel().getChildListPanel().selectAllTableRow();
			} else {
				getbillListPanel().getChildListPanel().cancelSelectAllTableRow();
			}
			model.addRowStateChangeEventListener(l);

			getbillListPanel().updateUI();
		}

	}

	/**
	 * ���������ƣ�where��乹����ս���
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param parent
	 */
	public BillSourceDLGWB(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, parent);
		m_whereStr = getQueryWhere();
		initialize();
	}

	/**
	 * ���������ƣ�where��乹����ս���
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param nodeKey
	 * @param userObj
	 * @param parent
	 */
	public BillSourceDLGWB(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, nodeKey, userObj, parent);
		m_whereStr = getQueryWhere();
		initialize();
	}

	/* (non-Javadoc)
	 * @see event.ActionListener#actionPerformed(event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getbtnOk()) {
			onOk();
		} else if (e.getSource() == getbtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getbtnQuery()) {
			onQuery();
		} else if (e.getSource() == getbtnSelectAll()) {
			onSelectAll();
		}else if (e.getSource() == getbtnCancelSelect()) {
			try {
				onCancleSelectAll();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}

	/**
	 * ���ӵ���ģ��
	 * <li>�÷�����PfUtilClient.childButtonClicked()����
	 */
	@Override
	public void addBillUI() {
		//����ģ�����
		getUIDialogContentPane().add(getbillListPanel(), BorderLayout.CENTER);
		//���ӶԿؼ�����
		addListenerEvent();
	}

	/**
	 * �¼�����
	 */
	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getbtnQuery().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);
		getbillListPanel().getHeadBillModel().addRowStateChangeEventListener(new HeadRowStateListener());
		getbtnSelectAll().addActionListener(this);
		getbtnCancelSelect().addActionListener(this);
		
		//��ͷ�б� ���л��¼�������
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	/* (non-Javadoc)
	 * @see BillEditListener#afterEdit(BillEditEvent)
	 */
	public void afterEdit(BillEditEvent e) {
	}

	/**
	 * ֻ�Ա�ͷ���д���
	 * <li>���л� �¼�
	 * <li>˫�� �¼�
	 * <li>WARN::���л��¼�������˫���¼�֮ǰ
	 * @param iNewRow
	 */
	private synchronized void headRowChange(int iNewRow) {
		if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow, getpkField()) != null) {
			if (!getbillListPanel().setBodyModelData(iNewRow)) {
				//1.���������������
				loadBodyData(iNewRow);
				//2.���ݵ�ģ����
				getbillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getbillListPanel().repaint();
	}

	/* (non-Javadoc)
	 * @see BillEditListener#bodyRowChange(BillEditEvent)
	 */
	public void bodyRowChange(BillEditEvent e) {
	}

	/**
	 * @return
	 */
	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				//�����ʾλ��ֵ
				//װ��ģ��
				nc.vo.pub.bill.BillTempletVO vo = ivjbillListPanel.getDefaultTemplet(getBillType(), null,
				/*getBusinessType(),*/getOperator(), getPkCorp(), getNodeKey());

				BillListData billDataVo = new BillListData(vo);

				//�����������ʾλ��
				String[][] tmpAry = getHeadShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}
				//�����ӱ����ʾλ��
				tmpAry = getBodyShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsBody(billDataVo, tmpAry);
				}

				ivjbillListPanel.setListData(billDataVo);

				//�������������е��ж�
				if (getHeadHideCol() != null) {
					for (int i = 0; i < getHeadHideCol().length; i++) {
						ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
					}
				}
				if (getBodyHideCol() != null) {
					for (int i = 0; i < getBodyHideCol().length; i++) {
						ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
					}
				}

				ivjbillListPanel.setMultiSelect(true);
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	/**
	 * ��õ�������VO����Ϣ
	 *
	 * <li>����[0]=���ݾۺ�Vo;����[1]=��������Vo;����[2]=�����ӱ�Vo;
	 */
	public void getBillVO() {
		try {
//			String[] retString = PfUIDataCache.getStrBillVo(getBillType());
//			//MatchTableBO_Client.querybillVo(getBillType());
//			//0--����vo;1-����Vo;2-�ӱ�Vo;
//			m_billVo = retString[0];
//			m_billHeadVo = retString[1];
//			m_billBodyVo = retString[2];
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * �ӱ��������
	 * @return
	 */
	public String getBodyCondition() {
		return null;
	}

	/**
	 * �ӱ������ֶ�
	 * @return
	 */
	public String[] getBodyHideCol() {
		return null;
	}

	/**
	 * �����ӱ��ά���飬��һάΪ����Ϊ2���ڶ�ά���ޣ�
	 * ��һ��Ϊ�ֶ����ԣ��ڶ���Ϊ��ʾ��λ����
	 * {{"����A","����B","����C","����D"},{"3","4","5","6"}}
	 * ע��:���뱣֤���еĳ�����ȡ�����ϵͳ��Ĭ��ֵ2ȡ��
	 * �������ڣ�(2001-12-25 10:19:03)
	 * @return java.lang.String[][]
	 */
	public String[][] getBodyShowNum() {
		return null;
	}

	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {

			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "ȡ��"*/);
		}
		return ivjbtnCancel;
	}

	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "ȷ��"*/);
		}
		return ivjbtnOk;
	}
	
	private UIButton getbtnSelectAll() {
		if (selectall == null) {
			selectall = new UIButton();
			selectall.setName("selectAll");
			selectall.setText("ȫѡ");
		}
		return selectall;
	}

	private UIButton getbtnCancelSelect() {
		if (cancelselect == null) {
			cancelselect = new UIButton();
			cancelselect.setName("cancelselect");
			cancelselect.setText("��ѡ");
		}
		return cancelselect;
	}
	
	private UIButton getbtnQuery() {
		if (ivjbtnQuery == null) {

			ivjbtnQuery = new UIButton();
			ivjbtnQuery.setName("btnQuery");
			ivjbtnQuery
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*@res "��ѯ"*/);
		}
		return ivjbtnQuery;
	}

	/**
	 * �����ѯ����
	 * @return
	 */
	public String getHeadCondition() {
		return null;
	}

	/**
	 * ���������ֶ�
	 * @return
	 */
	public String[] getHeadHideCol() {
		return null;
	}

	/**
	 * ���������ά���飬��һάΪ����Ϊ2���ڶ�ά���ޣ�
	 * ��һ��Ϊ�ֶ����ԣ��ڶ���Ϊ��ʾ��λ����
	 * {{"����A","����B","����C","����D"},{"3","4","5","6"}}
	 * ע��:���뱣֤���еĳ�����ȡ�����ϵͳ��Ĭ��ֵ2ȡ��
	 * �������ڣ�(2001-12-25 10:19:03)
	 * @return java.lang.String[][]
	 */
	public String[][] getHeadShowNum() {
		return null;
	}

	/**
	 * �����Ƿ���ҵ�������йأ�
	 * �����ҵ�������޹أ���ϵͳ������ҵ������������ƴ��
	 * ע��:������ȷ��֤�Ƿ���ҵ�����͵Ĺ�ϵ
	 * @return
	 */
	public boolean getIsBusinessType() {
		return true;
	}

	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getbtnSelectAll(), getbtnSelectAll().getName());
			ivjPanlCmd.add(getbtnCancelSelect(), getbtnCancelSelect().getName());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
			ivjPanlCmd.add(getbtnQuery(), getbtnQuery().getName());
		}
		return ivjPanlCmd;
	}

	@Override
	public AggregatedValueObject getRetVo() {
		return retBillVo;
	}

	@Override
	public AggregatedValueObject[] getRetVos() {
		return retBillVos;
	}

	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {

			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			//2003-05-12ƽ̨������ʾ����
			//getUIDialogContentPane().add(getbillListPanel(), "Center");
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}

	private void initialize() {

		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000114")/*@res "���ݵĲ��ս���"*/);
		setContentPane(getUIDialogContentPane());

		//��ȡ���ݶ�Ӧ�ĵ���vo����
		getBillVO();
	}

	/**
	 * ���������ȡ�ӱ�����
	 * @param row ѡ�еı�ͷ��
	 */
	public void loadBodyData(int row) {
		try {
			//�������ID
			String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField()).toString();
			//��ѯ�ӱ�VO����
			CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client.queryBodyAllData(getBillType(),
					id, getBodyCondition());
			getbillListPanel().setBodyValueVO(tmpBodyVo);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

	}


	@Override
	public void loadHeadData() {
		try {
			//���ò�Ʒ�鴫��������뵱ǰ��ѯ�������������������ѯ����
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and (" + getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}
			String businessType = null;
			if (getIsBusinessType()) {
				businessType = getBusinessType();
			}
			CircularlyAccessibleValueObject[] tmpHeadVo = PfUtilBO_Client.queryHeadAllData(getBillType(),
					businessType, tmpWhere);

			getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();

			//lj+ 2005-4-5
			//selectFirstHeadRow();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"pfworkflow", "UPPpfworkflow-000490")/*@res "���ݼ���ʧ�ܣ�"*/);
		}
	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.bill.BillTableMouseListener#mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent)
	 */
	public void mouse_doubleclick(BillMouseEnent e) {
//		  if (e.getPos() == BillItem.HEAD) {
//		   //WARN::ֻ�Ա�ͷ��˫���¼�������Ӧ,�����˫���¼���BillListPanel.BodyMouseListener����Ӧ
//		   final int headRow = e.getRow();
//		
//		   // leijun 2006-7-4 ��ʱ��������ʾ�ȴ��Ի���
//		   new Thread(new Runnable() {
//		    public void run() {
//		     BannerDialog dialog = new BannerDialog(BillSourceDLGWB.this);
//		     // FIXME::i18n
//		     dialog.setStartText(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
//		       "UPPpfworkflow-000491")/*@res "��ȡ�����У����Ե�..."*/);
//		     try {
//		      dialog.start();
//		
//		      headRowDoubleClicked(headRow);
//		     } finally {
//		      dialog.end();
//		     }
//		    }
//		
//		   }).start();
//		
//		  }
	}
	/**
	 * ȫѡ��ť����Ӧ���ӽ����ȡ��ѡ����VO
	 * wb 2009-5-5 10:20:37
	 */
	public void onSelectAll() {
		int headrow = getbillListPanel().getHeadTable().getRowCount();
		for(int i=0;i<headrow;i++){
			int rowstate = getbillListPanel().getHeadBillModel().getRowState(i);
			if(rowstate!=BillModel.SELECTED){
				getbillListPanel().getHeadBillModel().setRowState(i, BillModel.SELECTED);
				try {
					getBodyData(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
				getbillListPanel().getChildListPanel().selectAllTableRow();
			}
		}
		getbillListPanel().updateUI();
	}


	/**
	 * ��ѡ��ť����Ӧ���ӽ����ȡ��ѡ����VO
	 * wb 2009-5-5 10:20:42
	 * @throws Exception 
	 */
	public void onCancleSelectAll() throws Exception {
		int headrow = getbillListPanel().getHeadTable().getRowCount();
		
		for(int i=0;i<headrow;i++){
			int rowstate = getbillListPanel().getHeadBillModel().getRowState(i);
			if(rowstate!=BillModel.SELECTED){
				getbillListPanel().getHeadBillModel().setRowState(i, BillModel.SELECTED);
				getBodyData(i);
				getbillListPanel().getChildListPanel().selectAllTableRow();
			}else{	//ѡ�е���Ҫȥ��ѡ�б��
				getbillListPanel().getHeadBillModel().setRowState(i, BillModel.UNSTATE);
				getBodyData(i);
				getbillListPanel().getChildListPanel().cancelSelectAllTableRow();
			}
		}
		getbillListPanel().updateUI();
	}
	
	public void getBodyData(int row) throws Exception{
		if (getbillListPanel().getHeadBillModel().getValueAt(row, getpkField()) != null) {
			if (!getbillListPanel().setBodyModelData(row)) {
				//1.���������������
				String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField()).toString();
				//��ѯ�ӱ�VO����
				CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client.queryBodyAllData(getBillType(),id, getBodyCondition());
				getbillListPanel().setBodyValueVO(tmpBodyVo);
			}else{
				Object h = getbillListPanel().getHeadBillModel().getRowAttributeObject(row,"BILLLIST");
				String tablecode = getbillListPanel().getBillListData().getBodyTableCodes()[0];
		        if(h!=null){
		        	Vector copydata = (Vector)((Hashtable)h).get(tablecode);
		        	getbillListPanel().getBodyBillModel(tablecode).setBillModelData(copydata);
		        }
			}
			//2.���ݵ�ģ����
			getbillListPanel().setBodyModelDataCopy(row);
		}
	}
	/**
	 * "ȷ��"��ť����Ӧ���ӽ����ȡ��ѡ����VO
	 */
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel().getMultiSelectedVOs(m_billVo,
					m_billHeadVo, m_billBodyVo);
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
		}
		this.closeOK();
	}
	
	
	/**
	 * �ڸý����Ͻ����ٴβ�ѯ
	 */
	public void onQuery() {
		IBillReferQuery queryCondition = getQueyDlg();
		if (queryCondition.showModal() == UIDialog.ID_OK) {
			//���ز�ѯ����
			m_whereStr = queryCondition.getWhereSQL();
			loadHeadData();
			//fgj@ 2001-11-06 �޸��ÿձ�������
			getbillListPanel().setBodyValueVO(null);
			//hxr@ 2005-3-31 ��ʼѡ��һ��
			JTable table = getbillListPanel().getParentListPanel().getTable();
			int iRowCount = table.getRowCount();
			if (iRowCount > 0 && table.getSelectedRow() < 0) {
				table.changeSelection(0, 0, false, false);
			}
		}
	}

	/**
	 * �����������ʾλ�������ݲ�Ʒ�ķ��أ�
	 * @param billDataVo
	 * @param strShow
	 * @throws Exception
	 */
	public void setVoDecimalDigitsBody(BillListData billDataVo, String[][] strShow) throws Exception {
		if (strShow.length < 2)
			return;

		if (strShow[0].length != strShow[1].length)
			throw new Exception(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000115")/*@res "��ʾλ�����һ�����в�ƥ��"*/);

		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = billDataVo.getBodyItem(attrName);
			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	/**
	 * �����������ʾλ�������ݲ�Ʒ�ķ��أ�
	 * @param billDataVo
	 * @param strShow
	 * @throws Exception
	 */
	public void setVoDecimalDigitsHead(BillListData billDataVo, String[][] strShow) throws Exception {
		if (strShow.length < 2) { return; }
		if (strShow[0].length != strShow[1].length)
			throw new Exception(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000115")/*@res "��ʾλ�����һ�����в�ƥ��"*/);

		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = billDataVo.getHeadItem(attrName);
			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	/* (non-Javadoc)
	 * @see event.ListSelectionListener#valueChanged(event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int headRow = ((ListSelectionModel) e.getSource()).getAnchorSelectionIndex();
			if (headRow >= 0) {
				headRowChange(headRow);
			}
		}
	}
}