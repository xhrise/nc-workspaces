/*
 * �������� 2005-5-24
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.iuforeport.businessquery.CondCellRenderer;
import nc.ui.iuforeport.businessquery.CondExpCellRenderer;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryModelTree;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.iuforeport.businessquery.WhereCondVO;
import nc.vo.pub.ValueObject;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * ɸѡ�������ý���
 */
public class SetCondPanel extends AbstractQueryDesignSetPanel implements
		KeyListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0052";//"ɸѡ����";

	//�༭������
	private UIRefCellEditor[] m_refEditors = null;

	//���ɱ��ʽ�Ի���ʵ��
	private FldGenDlg m_fldGenDlg = null;

	//����
	public final static int COL_RELATION = 1;

	public final static int COL_TABLE = 2;

	public final static int COL_FLD = 3;

	public final static int COL_OPERATOR = 4;

	public final static int COL_VALUE = 5;

	public final static int COL_UNKNOWN = 6;

	private UIPanel ivjPnEast = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private UIPanel ivjPnWest = null;

	private UIPanel ivjPnEast1 = null;

	private UIPanel ivjPnEast2 = null;

	private UIButton ivjBnAdd = null;

	private UIButton ivjBnDel = null;

	private UIRadioButton ivjRadioBnHand = null;

	private UIRadioButton ivjRadioBnChoose = null;

	private ButtonGroup ivjBnGroup = null;

	private UITablePane ivjTablePn1 = null;

	private UITablePane ivjTablePn2 = null;

	private UIButton ivjBnAddUnknown = null;

	private UIPanel ivjPnEast3 = null;

	private UIButton ivjBnModify = null;

	private UIPanel ivjPnEast4 = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetCondPanel.this.getBnAdd())
				connEtoC3(e);
			if (e.getSource() == SetCondPanel.this.getBnDel())
				connEtoC4(e);
			if (e.getSource() == SetCondPanel.this.getRadioBnChoose())
				connEtoC6(e);
			if (e.getSource() == SetCondPanel.this.getRadioBnHand())
				connEtoC7(e);
			if (e.getSource() == SetCondPanel.this.getBnAddUnknown())
				connEtoC1(e);
			if (e.getSource() == SetCondPanel.this.getBnModify())
				connEtoC2(e);
		};
	};

	/**
	 * SetTablePanel ������ע�⡣
	 */
	public SetCondPanel() {
		super();
		initialize();
	}

	/**
	 * ֹͣ�༭���� �������ڣ�(2001-7-31 13:02:41)
	 * 
	 * @param value
	 *            java.lang.Object
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void afterEdit(TableCellEditor editor, int row, int col) {
		//���õ�Ԫֵ
		Object value = editor.getCellEditorValue();
		getTable1().setValueAt(value, row, col);

		if (col == COL_TABLE) {
			//�����ѡ�еı�
			FromTableVO ft = (FromTableVO) getTM1().getValueAt(row, COL_TABLE);
			//����ֶ���
			TableColumn tc = getTable1().getColumnModel().getColumn(COL_FLD);
			//��������ֵ�ʵ��
			ObjectTree dd = getTabPn().getDatadict();
			//��ö�������Դ
			//String dsName =
			// getQueryDefTabbedPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//��ѯ��Ӧ���е��ֶ�
			ObjectTree tree = (BIModelUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
					ft.getTablealias(), tree);
			//�����ֶ��еı༭��(����Դ�л����ܵ��¿�)
			JComboBox cbbFld = (sfs == null) ? new UIComboBox() : new UIComboBox(
					sfs);
			tc.setCellEditor(new UIRefCellEditor(cbbFld));
			//���õ�Ԫֵ
			getTM1().setValueAt(cbbFld.getItemAt(0), row, COL_FLD);
		}
	}

	/**
	 * add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		if (isSimple()) {
			if (m_refEditors == null)
				return;
			//���ȱʡ�ؼ�
			JComboBox cbbTable = (JComboBox) m_refEditors[1].getComponent();
			JComboBox cbbFld = (JComboBox) m_refEditors[2].getComponent();
			//�������
			int iIndex = getTM1().getRowCount();
			getTM1().addRow(
					new Object[] { "", "and", cbbTable.getItemAt(0),
							cbbFld.getItemAt(0), "=", "", new Boolean(false) });
			//ѡ��������
			getTable1().getSelectionModel()
					.setSelectionInterval(iIndex, iIndex);
		} else {
			//�����Ի���
			FldGenDlg dlg = getFldGenDlg();
			//���
			dlg.doClear();
			dlg.setBnAddEnabled(false);
			dlg.setSomeInvisibled();
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				//��������ֶ�
				String strWhere = dlg.getExp();
				//�������
				int iIndex = getTM2().getRowCount();
				getTM2()
						.addRow(
								new Object[] { "", "and", strWhere,
										new Boolean(false) });
				//ѡ��������
				getTable2().getSelectionModel().setSelectionInterval(iIndex,
						iIndex);
			}
		}
	}

	/**
	 * ���Ӵ�������
	 */
	public void bnAddUnknown_ActionPerformed(ActionEvent actionEvent) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		if (isSimple()) {
			if (m_refEditors == null)
				return;
			//���ȱʡ�ؼ�
			JComboBox cbbTable = (JComboBox) m_refEditors[1].getComponent();
			JComboBox cbbFld = (JComboBox) m_refEditors[2].getComponent();
			//�������
			int iIndex = getTM1().getRowCount();
			getTM1().addRow(
					new Object[] { "", "and", cbbTable.getItemAt(0),
							cbbFld.getItemAt(0), "=", "", new Boolean(true) });
			//ѡ��������
			getTable1().getSelectionModel()
					.setSelectionInterval(iIndex, iIndex);
		} else {
			//������ڱ༭�Ľڵ�
			//			ObjectNode node = getTabPn().getQueryDefDlg()
			//					.getEditNode();
			//			QueryModelDef qmd = (QueryModelDef) node.getObject();
			QueryModelDef qmd = getTabPn().getQueryModelDef().getBaseModel();
			ParamVO[] params = qmd.getParamVOs();
			int iLen = (params == null) ? 0 : params.length;
			if (iLen == 0) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/*
													  * @res "��ѯ����"
													  */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000865")/*
											  * @res "��ǰ��ѯ����δ����������������Ӵ�������"
											  */);
				return;
			}
			//����
			UnKnownCondGenDlg dlg = new UnKnownCondGenDlg(this);
			dlg.setQueryBaseDef(getTabPn().getQueryBaseDef(), params);
			dlg.doSetInfo(new Object[] { "", "", "" });
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				String strExp = dlg.getExp();
				String strRela = dlg.getRela();
				String strVal = dlg.getVal();
				String strCondExp = strExp + QueryConst.CROSS_SEPERATOR
						+ strRela + QueryConst.CROSS_SEPERATOR + strVal;
				//�������
				int iIndex = getTM2().getRowCount();
				getTM2()
						.addRow(
								new Object[] { "", "and", strCondExp,
										new Boolean(true) });
				//ѡ��������
				getTable2().getSelectionModel().setSelectionInterval(iIndex,
						iIndex);
			}
		}
	}

	/**
	 * del
	 */
	public void bnDel_ActionPerformed(ActionEvent actionEvent) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//����ˢ��
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable1().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable1().getRowCount() != 0)
				getTable1().getSelectionModel().setSelectionInterval(0, 0);

			/*
			 * ����Ϊһ���Դ��� QueryBaseDef qbd = m_parent.getQueryBaseDef(); //�ϲ�VO����
			 * ValueObject[] vos =
			 * QueryDefTabbedPn.delFromVOs(qbd.getSelectFlds(), iSelIndex);
			 * SelectFldVO[] newsfs = new SelectFldVO[vos.length]; for (int i =
			 * 0; i < vos.length; i++) newsfs[i] = (SelectFldVO) vos[i];
			 * qbd.setSelectFlds(newsfs);
			 */
		}
		return;
	}

	/**
	 * �޸�
	 */
	public void bnModify_ActionPerformed(ActionEvent actionEvent) {
		//��ֹ�༭̬
		if (getTable2().getCellEditor() != null)
			getTable2().getCellEditor().stopCellEditing();
		int iSelIndex = getTable2().getSelectedRow();
		if (iSelIndex != -1) {
			boolean bUnknown = ((Boolean) getTable2().getValueAt(iSelIndex, 3))
					.booleanValue();
			if (bUnknown) {
				//�޸Ĵ�������
				String strCondExp = getTable2().getValueAt(iSelIndex, 2)
						.toString();
				String[] strExpRelaVals = BIModelUtil.delimString(strCondExp,
						QueryConst.CROSS_SEPERATOR);
				String strExp = strExpRelaVals[0];
				String strRela = strExpRelaVals[1];
				String strVal = strExpRelaVals[2];
				//������ڱ༭�Ľڵ�
				QueryModelDef qmd = getTabPn().getQueryModelDef().getBaseModel();
				ParamVO[] params = qmd.getParamVOs();
				//����
				UnKnownCondGenDlg dlg = new UnKnownCondGenDlg(this);
				dlg.setQueryBaseDef(getTabPn().getQueryBaseDef(), params);
				dlg.doSetInfo(new Object[] { strExp, strRela, strVal });
				dlg.showModal();
				dlg.destroy();
				if (dlg.getResult() == UIDialog.ID_OK) {
					strExp = dlg.getExp();
					strRela = dlg.getRela();
					strVal = dlg.getVal();
					strCondExp = strExp + QueryConst.CROSS_SEPERATOR + strRela
							+ QueryConst.CROSS_SEPERATOR + strVal;
					//�����޸�
					getTM2().setValueAt(strCondExp, iSelIndex, 2);
				}
			} else {
				//�޸Ĺ̶�����
				String strCondExp = getTable2().getValueAt(iSelIndex, 2)
						.toString();
				//�����Ի���
				FldGenDlg dlg = getFldGenDlg();
				//���
				dlg.doSetInfo(new Object[] { "", "", strCondExp, "" });
				dlg.setBnAddEnabled(false);
				dlg.setSomeInvisibled();
				dlg.showModal();
				dlg.destroy();
				if (dlg.getResult() == UIDialog.ID_OK) {
					//��������ֶ�
					strCondExp = dlg.getExp();
					//�����޸�
					getTM2().setValueAt(strCondExp, iSelIndex, 2);
				}
			}
		}
		return;
	}

	/**
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();

		int iLen = getTM().getRowCount();
		if (iLen != 0 && isSimple()) {
			//׼������
			ParamVO[] params = getTabPn().getQueryModelDef().getBaseModel().getParamVOs();
			//			try {
			//				params = BIModelUtil.getParams(getTabPn().getQueryBaseDef()
			//						.getID(), getTabPn().getDefDsName());
			//			} catch (Exception e) {
			//				System.out.println(e);
			//			}
			int iLenParam = (params == null) ? 0 : params.length;

			//if (iLenParam != 0) {
			//�޲���ҲҪ���
			Hashtable<String, ParamVO> hashParamKey = new Hashtable<String, ParamVO>();
			for (int i = 0; i < iLenParam; i++)
				hashParamKey.put(params[i].getParamCode(), params[i]);
			//�����������Ҳ�����
			for (int i = 0; i < iLen; i++) {
				Object objRight = getTM().getValueAt(i, COL_VALUE);
				String strRight = (objRight == null) ? "" : objRight
						.toString();
				Object objUnknown = getTM().getValueAt(i, COL_UNKNOWN);
				boolean bUnknown = ((Boolean) objUnknown).booleanValue();
				if (bUnknown) {
					if (!hashParamKey.containsKey(strRight))
						return nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000866")/*
																  * @res
																  * "�����������Ҳ������������ڲ����б���"
																  */;
				}else{
					//����ǷǴ�����������Ҫ����������ֵ����Ϊ��
					if( strRight.length() == 0){
						return StringResource.getStringResource("mbiquery0128");//�����Ҳ���������Ϊ��
					}
				}
			}
			//}
		}
		return null;
	}

	/**
	 * connEtoC1: (BnAddUnknown.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnAddUnknown_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC1(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAddUnknown_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (BnModify.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnModify_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC2(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnModify_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnDel.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnDel_ActionPerformed1(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAdd_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (BnDel1.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnDel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC4(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDel_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC6: (RadioBnChoose.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.radioBnChoose_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC6(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnChoose_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC7: (RadioBnHand.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.radioBnHand_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC7(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnHand_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * ���ô��������� �������ڣ�(2003-11-13 9:05:55)
	 * 
	 * @return java.lang.String
	 * @param oldParam
	 *            java.lang.String
	 */
	private String fillParam(int row, int col) {
		String strParam = "";
		//������ڱ༭�Ľڵ�
		//ObjectNode node = getTabPn().getQueryDefDlg().getEditNode();
		QueryModelDef qmd = getTabPn().getQueryModelDef().getBaseModel();
		ParamVO[] params = qmd.getParamVOs();
		//����
		ParamDefDlg dlg = new ParamDefDlg(this, getTabPn().getDefDsName());
		dlg.setParamVOs(params, qmd.getDsName());
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//�洢
			params = dlg.getParamVOs();
			qmd.setParamVOs(params);
			//node.saveObject(qmd);
			//node.setObject(qmd);
			//���ѡ�в���
			ParamVO selParam = dlg.getSelParamVO();
			if (selParam != null) {
				strParam = selParam.getParamCode();
			}
			if (row != -1) {
				getTable1().setValueAt(strParam, row, col);
			}
		}
		return strParam;
	}

	/**
	 * ���� BnDel ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnAdd() {
		if (ivjBnAdd == null) {
			try {
				ivjBnAdd = new UIButton();
				ivjBnAdd.setName("BnAdd");
				ivjBnAdd.setPreferredSize(new Dimension(70, 22));
				ivjBnAdd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000867")/* @res "�̶�" */);
				//ivjBnAdd.setText(StringResource.getStringResource("miufo1000080"));//����
				ivjBnAdd.setMinimumSize(new Dimension(88, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAdd;
	}

	/**
	 * ���� BnAddUnknown ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnAddUnknown() {
		if (ivjBnAddUnknown == null) {
			try {
				ivjBnAddUnknown = new UIButton();
				ivjBnAddUnknown.setName("BnAddUnknown");
				ivjBnAddUnknown.setPreferredSize(new Dimension(70, 22));
				ivjBnAddUnknown
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000868")/* @res "����" */);
				ivjBnAddUnknown.setMinimumSize(new Dimension(88, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAddUnknown;
	}

	/**
	 * ���� BnDel1 ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnDel() {
		if (ivjBnDel == null) {
			try {
				ivjBnDel = new UIButton();
				ivjBnDel.setName("BnDel");
				ivjBnDel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000091")/* @res "ɾ��" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDel;
	}

	/**
	 * ���� BnGroup ����ֵ��
	 * 
	 * @return javax.swing.ButtonGroup
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.ButtonGroup getBnGroup() {
		if (ivjBnGroup == null) {
			try {
				ivjBnGroup = new javax.swing.ButtonGroup();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnGroup;
	}

	/**
	 * ���� BnDel1 ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnModify() {
		if (ivjBnModify == null) {
			try {
				ivjBnModify = new UIButton();
				ivjBnModify.setName("BnModify");
				ivjBnModify
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000401")/* @res "�޸�" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnModify;
	}

	/**
	 * ������ɱ��ʽ�Ի���ʵ�� �������ڣ�(2003-4-3 9:47:03)
	 * 
	 * @return nc.ui.iuforeport.businessquery.FldGenDlg
	 */
	public FldGenDlg getFldGenDlg() {
		if (m_fldGenDlg == null) {
			m_fldGenDlg = new FldGenDlg(this);
			m_fldGenDlg.setQueryBaseDef(getTabPn().getQueryBaseDef());
		}
		return m_fldGenDlg;
	}

	/**
	 * ���� PnEast ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnEast() {
		if (ivjPnEast == null) {
			try {
				ivjPnEast = new UIPanel();
				ivjPnEast.setName("PnEast");
				ivjPnEast.setPreferredSize(new Dimension(100, 10));
				ivjPnEast.setLayout(getPnEastGridLayout());
				ivjPnEast.setMinimumSize(new Dimension(100, 171));
				getPnEast().add(getPnEast1(), getPnEast1().getName());
				getPnEast().add(getPnEast2(), getPnEast2().getName());
				getPnEast().add(getPnEast3(), getPnEast3().getName());
				getPnEast().add(getPnEast4(), getPnEast4().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast;
	}

	/**
	 * ���� PnEast1 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnEast1() {
		if (ivjPnEast1 == null) {
			try {
				ivjPnEast1 = new UIPanel();
				ivjPnEast1.setName("PnEast1");
				ivjPnEast1.setLayout(new UIButtonLayout());
				getPnEast1().add(getBnAdd(), getBnAdd().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast1;
	}

	/**
	 * ���� PnEast2 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnEast2() {
		if (ivjPnEast2 == null) {
			try {
				ivjPnEast2 = new UIPanel();
				ivjPnEast2.setName("PnEast2");
				ivjPnEast2.setLayout(new UIButtonLayout());
				getPnEast2()
						.add(getBnAddUnknown(), getBnAddUnknown().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast2;
	}

	/**
	 * ���� PnEast3 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnEast3() {
		if (ivjPnEast3 == null) {
			try {
				ivjPnEast3 = new UIPanel();
				ivjPnEast3.setName("PnEast3");
				ivjPnEast3.setLayout(new UIButtonLayout());
				getPnEast3().add(getBnModify(), getBnModify().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast3;
	}

	/**
	 * ���� PnEast2 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnEast4() {
		if (ivjPnEast4 == null) {
			try {
				ivjPnEast4 = new UIPanel();
				ivjPnEast4.setName("PnEast4");
				ivjPnEast4.setLayout(new UIButtonLayout());
				getPnEast4().add(getBnDel(), getBnDel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast4;
	}

	/**
	 * ���� PnEastGridLayout ����ֵ��
	 * 
	 * @return GridLayout
	 */
	/* ���棺�˷������������ɡ� */
	private GridLayout getPnEastGridLayout() {
		GridLayout ivjPnEastGridLayout = null;
		try {
			/* �������� */
			ivjPnEastGridLayout = new GridLayout(4, 1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnEastGridLayout;
	}

	/**
	 * ���� PnNorth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setLayout(getPnNorthFlowLayout());
				getPnNorth().add(getRadioBnChoose(),
						getRadioBnChoose().getName());
				getPnNorth().add(getRadioBnHand(), getRadioBnHand().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnNorth;
	}

	/**
	 * ���� PnNorthFlowLayout ����ֵ��
	 * 
	 * @return FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private FlowLayout getPnNorthFlowLayout() {
		FlowLayout ivjPnNorthFlowLayout = null;
		try {
			/* �������� */
			ivjPnNorthFlowLayout = new FlowLayout();
			ivjPnNorthFlowLayout.setHgap(10);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
	}

	/**
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setPreferredSize(new Dimension(10, 10));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnSouth;
	}

	/**
	 * ���� PnWest ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnWest() {
		if (ivjPnWest == null) {
			try {
				ivjPnWest = new UIPanel();
				ivjPnWest.setName("PnWest");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnWest;
	}

	/**
	 * ���� RadioBnChoose ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnChoose() {
		if (ivjRadioBnChoose == null) {
			try {
				ivjRadioBnChoose = new UIRadioButton();
				ivjRadioBnChoose.setName("RadioBnChoose");
				ivjRadioBnChoose.setSelected(false);
				ivjRadioBnChoose.setPreferredSize(new Dimension(80, 22));
				ivjRadioBnChoose
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000869")/* @res "����ģʽ" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnChoose;
	}

	/**
	 * ���� RadioBnHand ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnHand() {
		if (ivjRadioBnHand == null) {
			try {
				ivjRadioBnHand = new UIRadioButton();
				ivjRadioBnHand.setName("RadioBnHand");
				ivjRadioBnHand.setPreferredSize(new Dimension(80, 22));
				ivjRadioBnHand
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000870")/* @res "�߼�ģʽ" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnHand;
	}

	/**
	 * ���where�������� �������ڣ�(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.WhereCondVO[]
	 * @i18n miufo00363=��������
	 * @i18n miufo00364=ȷ������
	 */
	public WhereCondVO[] getResultFromCond() {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		WhereCondVO[] wcs = null;
		if (isSimple()) {
			int iRowCount = getTable1().getRowCount();
			wcs = new WhereCondVO[iRowCount];
			for (int i = 0; i < iRowCount; i++) {
				wcs[i] = new WhereCondVO();
				wcs[i].setTypeflag("C");
				wcs[i].setrelationflag(getTM1().getValueAt(i, COL_RELATION)
						.toString());
				//��ñ���
				FromTableVO ft = (FromTableVO) getTM1()
						.getValueAt(i, COL_TABLE);
				String tableCode = ft.getTablecode();
				String tableAlias = ft.getTablealias();
				//����ֶ���
				String fldCode = ((SelectFldVO) getTM1().getValueAt(i, COL_FLD))
						.getFldalias();
				wcs[i].setLeftfld(tableCode + " as " + tableAlias + "."
						+ fldCode);
				wcs[i].setOperator(getTM1().getValueAt(i, COL_OPERATOR)
						.toString());
				wcs[i].setRightfld(getTM1().getValueAt(i, COL_VALUE));
				//���ȷ��������־
				boolean bUnknown = ((Boolean) getTM1().getValueAt(i,
						COL_UNKNOWN)).booleanValue();
				wcs[i].setCertain(bUnknown ? StringResource.getStringResource("miufo00363") : StringResource.getStringResource("miufo00364"));//���跭��
			}
		} else {
			int iRowCount = getTable2().getRowCount();
			wcs = new WhereCondVO[iRowCount];
			for (int i = 0; i < iRowCount; i++) {
				wcs[i] = new WhereCondVO();
				wcs[i].setTypeflag("C");
				Object relaValue = getTM2().getValueAt(i, COL_RELATION);
				wcs[i].setrelationflag(relaValue== null ?"":relaValue.toString());
				//���ȷ��������־
				boolean bUnknown = ((Boolean) getTM2().getValueAt(i, 3))
						.booleanValue();
				wcs[i].setCertain(bUnknown ? StringResource.getStringResource("miufo00363") : StringResource.getStringResource("miufo00364"));//���跭��
				//��ñ��ʽ
				String condExp = getTM2().getValueAt(i, 2).toString();
				if (bUnknown) {
					String[] strExpOprVals = BIModelUtil.delimString(condExp,
							QueryConst.CROSS_SEPERATOR);
					wcs[i].setLeftfld(strExpOprVals[0]);
					wcs[i].setOperator(strExpOprVals[1]);
					wcs[i].setRightfld(strExpOprVals[2]);
					wcs[i].setExpression0(strExpOprVals[0] + " "
							+ strExpOprVals[1] + " " + strExpOprVals[2]);
				} else
					wcs[i].setExpression0(condExp);
			}
		}
		return wcs;
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public UITable getTable() {
		if (isSimple())
			return getTable1();
		else
			return getTable2();
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private UITable getTable1() {
		return (UITable) getTablePn1().getTable();
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private UITable getTable2() {
		return (UITable) getTablePn2().getTable();
	}

	/**
	 * ���� TablePn ����ֵ��
	 * 
	 * @return UITablePane
	 */
	/* ���棺�˷������������ɡ� */
	@SuppressWarnings("serial")
	private UITablePane getTablePn1() {
		if (ivjTablePn1 == null) {
			try {
				ivjTablePn1 = new UITablePane();
				ivjTablePn1.setName("TablePn1");
				// user code begin {1}
				UITable table = new UITable() {
					public void editingStopped(ChangeEvent e) {
						int row = editingRow;
						int col = editingColumn;
						TableCellEditor editor = getCellEditor();
//						Object oldValue = getValueAt(row, col);
						//�����༭״̬
						if (editor != null) {
							//ת�Ƶ�afterEdit(editor, row,
							// col)��ȥ�ˣ���Ϊ����û��ֹͣ�༭ǰ��ȡֵ�����Ǵ�ġ�
							removeEditor();
							afterEdit(editor, row, col);

						}
					}
				};
				ivjTablePn1.setTable(table);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn1;
	}

	/**
	 * ���� TablePn2 ����ֵ��
	 * 
	 * @return UITablePane
	 */
	/* ���棺�˷������������ɡ� */
	private UITablePane getTablePn2() {
		if (ivjTablePn2 == null) {
			try {
				ivjTablePn2 = new UITablePane();
				ivjTablePn2.setName("TablePn2");
				ivjTablePn2.setBounds(157, 394, 239, 92);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn2;
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public DefaultTableModel getTM() {
		if (isSimple())
			return getTM1();
		else
			return getTM2();
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private DefaultTableModel getTM1() {
		return (DefaultTableModel) getTable1().getModel();
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private DefaultTableModel getTM2() {
		return (DefaultTableModel) getTable2().getModel();
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getBnAdd().addActionListener(ivjEventHandler);
		getBnDel().addActionListener(ivjEventHandler);
		getRadioBnChoose().addActionListener(ivjEventHandler);
		getRadioBnHand().addActionListener(ivjEventHandler);
		getBnAddUnknown().addActionListener(ivjEventHandler);
		getBnModify().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ����Ԫ�༭�� �������ڣ�(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	public void initEditorValue() {
		m_refEditors = new UIRefCellEditor[5];
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//���ù�ϵ���б༭��
		JComboBox cbbRelation = new UIComboBox(new Object[] { "and", "or" });
		m_refEditors[0] = new UIRefCellEditor(cbbRelation);
		//���ñ��б༭��
		FromTableVO[] fts = qbd.getFromTables();

		if (fts == null) {
			fts = new FromTableVO[0];
		}

		JComboBox cbbTable = new UIComboBox(fts);
		m_refEditors[1] = new UIRefCellEditor(cbbTable);
		//��������ֵ�ʵ��
		ObjectTree dd = getTabPn().getDatadict();
		//���ִ������Դ
		//String dsName = getTabPn().getQueryBaseDef().getDsName();
		String dsName = getTabPn().getDefDsName();
		//�����ֶ��б༭��
		SelectFldVO[] sfs = null;
		if (fts.length != 0) {
			ObjectTree tree = (BIModelUtil.isTempTable(fts[0].getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			sfs = BIModelUtil.getFldsFromTable(fts[0].getTablecode(), fts[0]
					.getTablealias(), tree);
		}
		//����Դ�л����ܵ��¿�
		JComboBox cbbFld = (sfs == null) ? new UIComboBox() : new UIComboBox(sfs);
		m_refEditors[2] = new UIRefCellEditor(cbbFld);
		//���ò������б༭��
		JComboBox cbbOperator = new UIComboBox(new Object[] { "=", ">", "<",
				"<>", ">=", "<=", "like", "in", "is", "not like", "not in" });
		m_refEditors[3] = new UIRefCellEditor(cbbOperator);

		//�Ҳ��������ȼ�����
		JTextField tf = new JTextField();
		tf.addKeyListener(this);
		tf.addMouseListener(this);
		m_refEditors[4] = new UIRefCellEditor(tf);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetTablePanel");
			setLayout(new BorderLayout());
			setSize(400, 240);
			add(getPnSouth(), "South");
			add(getPnWest(), "West");
			add(getPnNorth(), "North");
			add(getPnEast(), "East");
			add(getTablePn1(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getBnGroup().add(getRadioBnChoose());
		getBnGroup().add(getRadioBnHand());
		getRadioBnChoose().setSelected(true);
		getBnModify().setEnabled(false);
		//��ʼ�����
		initTable1();
		initTable2();
		setTableCell();
		getTable1().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable1().setColumnWidth(new int[] { 20, 62, 96, 108, 72, 124, 60 });
		getTable2().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable2().setColumnWidth(new int[] { 20, 62, 380, 80 });
		// user code end
	}

	/**
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable1() {
		//��ģ��
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				"",
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000864")/* @res "��ϵ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000048")/* @res "��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000873")/* @res "�ֶ�" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000874")/* @res "�ȽϷ�" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000875")/* @res "�Ҳ�����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000871") /* @res "��������" */}, 0) {
			public int getColumnCount() {
				return 7;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				if (col == COL_VALUE)
					return String.class;
				else if (col == COL_UNKNOWN)
					return Boolean.class;
				else
					return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				if (col == 0)
					return false;
				else if (row == 0 && col == COL_RELATION)
					return false;
				else if (col == COL_UNKNOWN)
					return false;
				else {
					//���ñ༭��
					setValueEditor(row, col);
					//�������������
					boolean bUnknown = ((Boolean) getValueAt(row,
					 COL_UNKNOWN)).booleanValue();
					if (bUnknown && col == COL_VALUE){
//						String strParam = fillParam(row, col);
					}
					return true;
				}
			}
		};
		getTable1().setModel(tm);
//		TableColumn	 tc = getTable1().getColumnModel().getColumn(COL_UNKNOWN);
//		if( tc != null ){
//			getTable1().removeColumn(tc);
//		}
		//���ñ�����
		getTable1().getTableHeader()
				.setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable1().getTableHeader()
				.setForeground(QueryConst.HEADER_FORE_COLOR);
		makeMultiHeader();
	}

	/**
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable2() {
		//��ģ��
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				"",
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000864")/* @res "��ϵ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000876")/* @res "�������ʽ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000871") /* @res "��������" */}, 0) {
			public int getColumnCount() {
				return 4;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				if (col == 2)
					return String.class;
				else if (col == 3)
					return Boolean.class;
				else
					return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				if (row == 0 && col == COL_RELATION)
					return false;
				else if (col == COL_RELATION) {
					setValueEditor(row, COL_RELATION);
					return true;
				} else
					return false;
			}
		};
		getTable2().setModel(tm);
//		TableColumn	 tc = getTable2().getColumnModel().getColumn(3);
//		if( tc != null ){
//			getTable2().removeColumn(tc);
//		}
		//���ñ�����
		getTable2().getTableHeader()
				.setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable2().getTableHeader()
				.setForeground(QueryConst.HEADER_FORE_COLOR);
	}

	/**
	 * �Ƿ�Ϊ����ģʽ �������ڣ�(2003-11-13 14:35:00)
	 * 
	 * @return boolean
	 */
	public boolean isSimple() {
		return getRadioBnChoose().isSelected();
	}

	/**
	 * Invoked when a key has been pressed.
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Invoked when a key has been released.
	 */
	public void keyReleased(KeyEvent e) {
		int iSelIndex = getTable1().getSelectedRow();
		if (iSelIndex != -1) {
			//�Ƿ��������
			boolean bUnknown = ((Boolean) getTable1().getValueAt(iSelIndex,
					COL_UNKNOWN)).booleanValue();
			JTextField tf = (JTextField) m_refEditors[4].getComponent();
			if (e.getKeyCode() == KeyEvent.VK_F12) {
				if (bUnknown) {
					//����¼��
					String str = fillParam(-1, -1);
					tf.setText(str);
				} else {
					/*
					 * //�������տ��¼�� QryCondRefDlg dlg = getTabPn().getRefDlg(); if
					 * (dlg != null) { dlg.showModal(); dlg.destroy(); if
					 * (dlg.getResult() == UIDialog.ID_OK) //������ս��
					 * tf.setText(dlg.getValue()); }
					 */
				}
			} else if (e.getKeyCode() == KeyEvent.VK_F11) {
				if (!bUnknown) {
					//��������¼��
					String str = fillEnvParam();
					if (str != null)
						tf.setText(str);
				}
			}
		}
	}

	/**
	 * Invoked when a key has been typed. This event occurs when a key press is
	 * followed by a key release.
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * ���ͷ �������ڣ�(2003-4-2 14:45:10)
	 */
	private void makeMultiHeader() {
		TableColumnModel cm = getTable1().getColumnModel();
		GroupableTableHeader header = (GroupableTableHeader) getTable1()
				.getTableHeader();
		ColumnGroup cg = new ColumnGroup(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("10241201", "UPP10241201-000877")/* @res "�������" */);
		cg.add(cm.getColumn(COL_TABLE));
		cg.add(cm.getColumn(COL_FLD));
		header.addColumnGroup(cg);
	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int iSelIndex = getTable1().getSelectedRow();
			if (iSelIndex != -1) {
				//�Ƿ��������
				boolean bUnknown = ((Boolean) getTable1().getValueAt(iSelIndex,
						COL_UNKNOWN)).booleanValue();
				if (bUnknown) {
					JTextField tf = (JTextField) m_refEditors[4].getComponent();
					String str = fillParam(-1, -1);
					if (!str.equals(""))
						tf.setText(str);
				}
			}
		}
	}

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * ѡ��¼��
	 */
	public void radioBnChoose_ActionPerformed(ActionEvent actionEvent) {

		remove(getTablePn2());
		add(getTablePn1(), BorderLayout.CENTER);
		getBnModify().setEnabled(false);
		validate();
		repaint();
	}

	/**
	 * �ֹ�¼��
	 */
	public void radioBnHand_ActionPerformed(ActionEvent actionEvent) {

		remove(getTablePn1());
		add(getTablePn2(), BorderLayout.CENTER);
		getBnModify().setEnabled(true);
		validate();
		repaint();
	}

	/**
	 * ����where�������� �������ڣ�(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 * @i18n miufo00363=��������
	 */
	public void setResultToCond(WhereCondVO[] wcs) {
		int iLen = (wcs == null) ? 0 : wcs.length;
		if (iLen == 0)
			return;
		boolean bSimple = (wcs[0].getExpression0() == null);
		if (!bSimple) {
			//���²���
			remove(getTablePn1());
			add(getTablePn2(), BorderLayout.CENTER);
			getBnModify().setEnabled(true);
			validate();
			repaint();
			//�ֹ�¼��ģʽ
			getRadioBnHand().setSelected(true);
			for (int i = 0; i < iLen; i++) {
				//���ȷ����־
				boolean bCertain = true;
				if (StringResource.getStringResource("miufo00363").equals(wcs[i].getCertain()))//���跭��
					bCertain = false;
				//�����������ʽ
				String condExp = null;
				if (bCertain)
					condExp = wcs[i].getExpression0();
				else
					condExp = wcs[i].getLeftfld() + QueryConst.CROSS_SEPERATOR
							+ wcs[i].getOperator() + QueryConst.CROSS_SEPERATOR
							+ wcs[i].getRightfld();
				//���
				Object[] objrows = new Object[] { "", wcs[i].getRelationflag(),
						condExp, new Boolean(!bCertain) };
				getTM2().addRow(objrows);
			}
		} else {
			//�����-����ʾ����ϣ��
			Hashtable hashTableId = getTabPn().getSetPanel(0).getHashTableId();
			for (int i = 0; i < iLen; i++) {
				String tablefld = wcs[i].getLeftfld();
				int iIndex = tablefld.indexOf(".");

				//ת����ѯ�ֶα��ʽ
				String table = tablefld.substring(0, iIndex);
				int iIndexAs = table.indexOf(" as ");
				String tableCode = null;
				String tableName = null;
				String tableAlias = null;
				if (iIndexAs == -1) {
					tableCode = table;
					tableAlias = table;
				} else {
					tableCode = table.substring(0, iIndexAs);
					tableAlias = table.substring(iIndexAs + 4);
				}
				//���ƣ�ͬ�����ѯʱ�����б仯��
				if (hashTableId.containsKey(tableAlias))
					tableName = hashTableId.get(tableAlias).toString();
				//�ֶ�
				String fldCode = tablefld.substring(iIndex + 1);

				//��������ֵ�ʵ��
				ObjectTree dd = getTabPn().getDatadict();
				//���ִ������Դ
				//String dsName =
				// getTabPn().getQueryBaseDef().getDsName();
				String dsName = getTabPn().getDefDsName();
				//��ö�Ӧ��������
				ObjectTree tree = (BIModelUtil.isTempTable(tableCode)) ? BIQueryModelTree
						.getInstance(dsName)
						: dd;
				//��ñ���ֶ���ϸ��Ϣ
				ValueObject[] vos = BIModelUtil.getTableFldVO(tableCode,
						tableName, tableAlias, fldCode, tree);
				if (vos == null) {
					AppDebug.debug("vosWhere == null!");//@devTools System.out.println("vosWhere == null!");
					return;
				}
				FromTableVO ft = (FromTableVO) vos[0];
				SelectFldVO sf = (SelectFldVO) vos[1];
				//���ȷ����־
				boolean bCertain = true;
				if (StringResource.getStringResource("miufo00363").equals(wcs[i].getCertain()))
					bCertain = false;

				//���
				Object[] objrows = new Object[] { "", wcs[i].getRelationflag(),
						ft, sf, wcs[i].getOperator(), wcs[i].getRightfld(),
						new Boolean(!bCertain) };
				getTM1().addRow(objrows);
			}
		}
		//�õ����һ�У�ɾ��
		
	}

	/**
	 * ���þ��ж��� �������ڣ�(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		DefaultTableCellRenderer renderer = null;
		for (int i = 0; i < getTable1().getColumnCount() - 1; i++) {
			TableColumn tc = getTable1().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNumCellRenderer();
			else if (i == 1)
				renderer = new CondCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}

		for (int i = 0; i < getTable2().getColumnCount() - 1; i++) {
			TableColumn tc = getTable2().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNumCellRenderer();
			else if (i == 1)
				renderer = new CondCellRenderer();
			else if (i == 2)
				renderer = new CondExpCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}
	}

	/**
	 * ����ֵ��CellEditor �������ڣ�(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	private void setValueEditor(int iRow, int iCol) {
		TableColumn tc = getTable().getColumnModel().getColumn(iCol);
		//��ö���
//		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		if (iCol != COL_FLD) {
			//���ù�ϵ�������������б༭��
			tc.setCellEditor(m_refEditors[iCol - 1]);
		} else {
			//�����ֶ��б༭��
			FromTableVO ft = (FromTableVO) getTM().getValueAt(iRow, COL_TABLE);
			//��������ֵ�ʵ��
			ObjectTree dd = getTabPn().getDatadict();
			//���ִ������Դ
			//String dsName =
			// getTabPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//��ѯ��Ӧ���е��ֶ�
			ObjectTree tree = (BIModelUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
					ft.getTablealias(), tree);
			//����Դ�л����ܵ��¿�
			JComboBox cbbFld = (sfs == null) ? new UIComboBox() : new UIComboBox(
					sfs);
			tc.setCellEditor(new UIRefCellEditor(cbbFld));
		}
	}

	/**
	 * ���û��������� �������ڣ�(2003-11-13 9:05:55)
	 * 
	 * @return java.lang.String
	 * @param oldParam
	 *            java.lang.String
	 */
	private String fillEnvParam() {
		String strParam = null;
		/*
		 * //���ȫ������������ȱʡ+�Զ���ӿ�ʵ�֣� IEnvParam iEnvParam =
		 * getTabPn().getQueryDefDlg().getIEnvParam(); String[][] envParams =
		 * ModelUtil.getEnvParams(iEnvParam); //���� ChooseEnvParamDlg dlg = new
		 * ChooseEnvParamDlg(this); dlg.setEnvParams(envParams);
		 * dlg.showModal(); dlg.destroy(); if (dlg.getResult() ==
		 * UIDialog.ID_OK) { //���ѡ�в����� strParam = dlg.getEnvParams(); }
		 */
		return strParam;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//����
		qmd.getQueryBaseVO().setWhereConds(getResultFromCond());
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setResultToCond(qmd.getQueryBaseVO().getWhereConds());
	}

	/**
	 * ˢ�²�ѯ��������
	 */
	public void refreshQbd() {
		//���ɸѡ��������
		WhereCondVO[] wcs = getResultFromCond();
		//ˢ��
		getTabPn().getQueryBaseDef().setWhereConds(wcs);
	}
	private class RowNumCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * CondCellRenderer ������ע�⡣
		 */
		public RowNumCellRenderer() {
			super();
		}
//		 implements javax.swing.table.TableCellRenderer
		public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {

			if (isSelected) {
				super.setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				super.setForeground(table.getForeground());
				super.setBackground(table.getBackground());
			}
			setFont(table.getFont());
			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					super.setForeground(UIManager.getColor("Table.focusCellForeground"));
					super.setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(noFocusBorder);
			}

			setValue(String.valueOf(row + 1));
			return this;
		}
		}
}  