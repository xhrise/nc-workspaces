package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import nc.ui.bd.def.DefaultDefdocRefModel;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IBusiType;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.querymodel.BaseRefEditor;
import nc.ui.pub.querymodel.ChooseQmdDlg;
import nc.ui.pub.querymodel.QERefModel;
import nc.ui.pub.querymodel.UIUtil;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.querymodel.ParamConst;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ��������Ի��� �������ڣ�(2003-8-8 14:41:00)
 * 
 * @author���쿡��
 */
public class ParamDefDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//��������Դ
	private String m_defDsName = null;

	//����ö�����ı���
	private UITextField m_tfRef = null;

	//��ѯִ������Դ
	private String m_dsName = null;

	//����
	public final static int COL_PARAMCODE = 0;

	public final static int COL_PARAMNAME = 1;

	public final static int COL_OPR = 2;

	public final static int COL_DATATYPE = 3;

	public final static int COL_CONSULTCODE = 4;

	public final static int COL_DEFAULTVALUE = 5;

	public final static int COL_IFMUST = 6;

	public final static int COL_REFDEPEND = 7;

	public final static int COL_IFVISIBLE = 8;

	private UIPanel ivjPnSouth = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBnAdd = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnDel = null;

	private UIButton ivjBnOK = null;

	private UIPanel ivjPnNorth = null;

	private UITablePane ivjTablePn = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, MouseListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ParamDefDlg.this.getBnAdd())
				connEtoC1(e);
			if (e.getSource() == ParamDefDlg.this.getBnDel())
				connEtoC2(e);
			if (e.getSource() == ParamDefDlg.this.getBnCancel())
				connEtoC3(e);
			if (e.getSource() == ParamDefDlg.this.getBnOK())
				connEtoC4(e);
		};

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == ParamDefDlg.this.getPnSouth())
				connEtoC5(e);
		};

		public void mouseEntered(MouseEvent e) {
		};

		public void mouseExited(MouseEvent e) {
		};

		public void mousePressed(MouseEvent e) {
		};

		public void mouseReleased(MouseEvent e) {
		};
	};

	/**
	 * ParamDefDlg ������ע�⡣
	 * @deprecated
	 */
	public ParamDefDlg() {
		super();
		initialize();
	}

	/**
	 * ParamDefDlg ������ע�⡣
	 * 
	 * @param parent
	 *            Container
	 */
	public ParamDefDlg(Container parent, String defDsName) {
		super(parent);
		m_defDsName = defDsName;
		initialize();
	}

	/**
	 * ���� �������ڣ�(2003-8-8 15:14:24)
	 */
	private void addLine() {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//�������
		int iIndex = getTM().getRowCount();
		Object[] objrows = new Object[] { "", "", "",
				new ParamConst().DATATYPE_NOTE[0], "", "", new Boolean(false),
				"", new Boolean(false) };
		getTM().addRow(objrows);
		//ѡ��������
		getTable().getSelectionModel().setSelectionInterval(iIndex, iIndex);
	}

	/**
	 * ���Ӳ���VO���� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public void addParamVOs(ParamVO[] params) {
		int iLen = (params == null) ? 0 : params.length;

		ParamConst paramConst = new ParamConst();
		//��������
		for (int i = 0; i < iLen; i++) {
			//����
			String strDataType = "";
			if (params[i].getDataType() == null)
				strDataType = paramConst.DATATYPE_NOTE[0];
			else
				strDataType = paramConst.DATATYPE_NOTE[params[i].getDataType()
						.intValue()];
			//����
			Object[] objrows = new Object[] { params[i].getParamCode(),
					params[i].getParamName(), params[i].getOperaCode(),
					strDataType, params[i].getConsultCode(),
					params[i].getValue(),
					new Boolean(params[i].getIfMust().booleanValue()),
					params[i].getRefDepend(),
					new Boolean(params[i].isInvisible()) };
			getTM().addRow(objrows);
		}
	}

	/**
	 * ֹͣ�༭�����ò��ս������ʾֵ �������ڣ�(2001-7-31 13:02:41)
	 * 
	 * @param value
	 *            java.lang.Object
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void afterEdit(TableCellEditor editor, int row, int col) {

		Object value = editor.getCellEditorValue();
		getTable().setValueAt(value, row, col);

		if ((editor instanceof UIRefCellEditor)) {
			Object temp = ((UIRefCellEditor) editor).getComponent();
			if (temp instanceof UIRefPane) {
				UIRefPane pane = (UIRefPane) temp;
				//��������
				String strDataType = getTable().getValueAt(row, COL_DATATYPE)
						.toString();
				//���շ���ֵ����
				int iReturnType = 0;
				if (strDataType
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-001150")/* @res "�������" */))
					iReturnType = 1;
				else if (strDataType
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-001151")/* @res "���Ʋ���" */))
					iReturnType = 2;
				//��������ֵ
				String strOpr = getTable().getValueAt(row, 2).toString().trim();
				if (strOpr.equals("like")) {
					String strResult = getRefResult(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable().setValueAt(strResult + "%", row, col);
				} else if (strOpr.equals("in")) {
					String strResult = getRefResult_in(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable().setValueAt(strResult, row, col);
				} else {
					String strResult = getRefResult(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable().setValueAt(strResult, row, col);
				}
			}
		}
	}

	/**
	 * ����
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		addLine();
	}

	/**
	 * ȡ��
	 */
	public void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * ɾ��
	 */
	public void bnDel_ActionPerformed(ActionEvent actionEvent) {
		delLine();
	}

	/**
	 * ȷ��
	 */
	public void bnOK_ActionPerformed(ActionEvent actionEvent) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		String strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */, strErr);
			return;
		}
		closeOK();
	}

	/**
	 * �Ϸ���У�� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public String check() {
		Hashtable<String, String> hashParamCode = new Hashtable<String, String>();
		int iLen = getTM().getRowCount();
		for (int i = 0; i < iLen; i++) {
			String paramCode = (getTM().getValueAt(i, COL_PARAMCODE) == null) ? ""
					: getTM().getValueAt(i, COL_PARAMCODE).toString().trim();
			if (paramCode.equals(""))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001152", null,
						new String[] { "" + (i + 1) })/* @res "��{0}�в�����Ϊ��" */;
			String paramName = (getTM().getValueAt(i, COL_PARAMNAME) == null) ? ""
					: getTM().getValueAt(i, COL_PARAMNAME).toString().trim();
			if (paramName.equals(""))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001153", null,
						new String[] { "" + (i + 1) })/* @res "��{0}����ʾ��Ϊ��" */;
			if (hashParamCode.containsKey(paramCode))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001154")/* @res "�����������ظ���" */
						+ paramCode;
			else
				hashParamCode.put(paramCode, paramName);
			//��������
			String paramType = (getTM().getValueAt(i, COL_DATATYPE) == null) ? ""
					: getTM().getValueAt(i, COL_DATATYPE).toString();
			String refDepend = (getTM().getValueAt(i, COL_REFDEPEND) == null) ? ""
					: getTM().getValueAt(i, COL_REFDEPEND).toString();
			if (!refDepend.equals("")
					&& !paramType
							.endsWith(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("10241201",
											"UPP10241201-000056")/* @res "����" */))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001155")/* @res "�����Ͳ�������Ҫ���ò�����������" */;
		}
		return null;
	}

	/**
	 * connEtoC1: (BnAdd.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnAdd_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC1(ActionEvent arg1) {
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
	 * connEtoC2: (BnDel.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnDel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC2(ActionEvent arg1) {
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
	 * connEtoC3: (BnCancel.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnCancel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnCancel_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (BnOK.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnOK_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC4(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnOK_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (PnSouth.mouse.mouseClicked(MouseEvent) -->
	 * ParamDefDlg.pnSouth_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC5(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.pnSouth_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * ɾ�� �������ڣ�(2003-8-8 15:14:24)
	 */
	private void delLine() {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//����ˢ��
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable().getRowCount() != 0)
				getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	/**
	 * ����Ĭ��ֵ�в���ģ�� ����������� �������ڣ�(2004-4-28 15:55:07)
	 */
	private void doRefDepend(ParamVO[] params, TableCellEditor editor, int row,
			int column) {

		try {
			UIRefCellEditor refCellEditor = (UIRefCellEditor) editor;
			//�滻����
			boolean bRefParam = getTable().getValueAt(row, COL_DATATYPE)
					.toString().endsWith(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-000056")/*
																	  * @res
																	  * "����"
																	  */);
			boolean bUserCond = false;
			String strDepend = null;
			if (bRefParam) {
				//���������ϵ
				strDepend = params[row].getRefDepend();
				if (strDepend != null && !strDepend.trim().equals("")) {
					bUserCond = true;
					//���ݲ���������̬�滻��������
					int[][] iRefs = BIModelUtil.getParamRefDepends(params);
					int iLen = (iRefs == null) ? 0 : iRefs.length;
					for (int i = 0; i < iLen; i++) {
						if (iRefs[i][0] == row) {
							//���������
							int iDependedRow = iRefs[i][1];
							//�������ֵ
							Object obj = getTable().getValueAt(iDependedRow,
									COL_DEFAULTVALUE);
							String strDependValue = (obj == null) ? "" : obj
									.toString();
							if (strDependValue.equals(""))
								continue;
							String strDependParam = getTable().getValueAt(
									iDependedRow, COL_PARAMCODE).toString();
							//�滻������ϵ
							strDepend = StringUtil.replaceAllString(strDepend,
									"[" + strDependParam + "]", strDependValue);
						}
					}
				}
			}
			//��ò������
			UIRefPane refPn = (UIRefPane) refCellEditor.getComponent();
			refPn.getUITextField().setSingleQuoteInputEnabled(true);
			if (!bRefParam)
				refPn.setButtonVisible(false);
			else {
				refPn.setButtonVisible(true);
				//ö����
				String strConsult = params[row].getConsultCode();
				//�ȽϷ�
				String strOpr = params[row].getOperaCode();
				//��ѡ
				refPn.setMultiSelectedEnabled(strOpr.equals("in"));
				//�Լ�
				//refPn.setAutoCheck(!strOpr.equals("like") &&
				// !strOpr.equals("in"));
				refPn.setAutoCheck(false);
				//��󳤶�
				refPn.setMaxLength(200);
				//���û���
				refPn.setCacheEnabled(false);
				//��̬�������ģ��
				AbstractRefModel rm = procRefModel(strConsult, strOpr);
				if (rm == null) {
					//ϵͳ����
					refPn.setRefNodeName(strConsult);
					rm = refPn.getRefModel();
				} else {
					//��ĩ������ѡ���ƹ���ѧ�������壩
					refPn.setNotLeafSelectedEnabled(rm
							.isNotLeafSelectedEnabled());
					//�Զ������
					refPn.setRefModel(rm);
				}
				//��������Դ
				rm.setDataSource(m_dsName);
				if (bUserCond) {
					//rm.setUseDataPower(false);
					//rm.setWherePart(rm.getWherePart() + " and " + strDepend);
					AppDebug.debug("�滻��������" + strDepend);//@devTools System.out.println("�滻��������" + strDepend);
					String[] subConds = strDepend.split(";");//���鳤������Ϊ1
					if (subConds[0].trim().length() > 0) {
						rm.setWherePart(subConds[0]);
					}
					//�������п�����Ҫ����setClassWherePart����
					if (subConds.length > 1 && subConds[1].trim().length() > 0
							&& rm instanceof AbstractRefTreeModel) {
						((AbstractRefTreeModel) rm)
								.setClassWherePart(subConds[1]);
					}

					//ǿ��ˢ�£��ѵ�û�б�ķ�������
					rm.reloadData();
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * ���� BnAdd ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnAdd() {
		if (ivjBnAdd == null) {
			try {
				ivjBnAdd = new nc.ui.pub.beans.UIButton();
				ivjBnAdd.setName("BnAdd");
				ivjBnAdd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000648")/* @res "����" */);
				// user code begin {1}
				ivjBnAdd.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnAdd, ivjBnAdd.getText());
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
	 * ���� BnCancel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000000")/* @res "ȡ��" */);
				// user code begin {1}
				ivjBnCancel.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnCancel, ivjBnCancel.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnCancel;
	}

	/**
	 * ���� BnDel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnDel() {
		if (ivjBnDel == null) {
			try {
				ivjBnDel = new nc.ui.pub.beans.UIButton();
				ivjBnDel.setName("BnDel");
				ivjBnDel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000649")/* @res "ɾ��" */);
				// user code begin {1}
				ivjBnDel.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnDel, ivjBnDel.getText());
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
	 * ���� BnOK ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnOK() {
		if (ivjBnOK == null) {
			try {
				ivjBnOK = new nc.ui.pub.beans.UIButton();
				ivjBnOK.setName("BnOK");
				ivjBnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000001")/* @res "ȷ��" */);
				// user code begin {1}
				ivjBnOK.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnOK, ivjBnOK.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnOK;
	}

	/**
	 * ��ò���VO���� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public ParamVO[] getParamVOs() {
		ParamVO[] params = null;
		int iLen = getTM().getRowCount();
		if (iLen != 0) {
			params = new ParamVO[iLen];
			for (int i = 0; i < iLen; i++)
				params[i] = getSelParamVO(i);
		}
		return params;
	}

	/**
	 * ���� PnNorth ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new nc.ui.pub.beans.UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setPreferredSize(new Dimension(10, 5));
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
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new nc.ui.pub.beans.UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setLayout(getPnSouthFlowLayout());
				getPnSouth().add(getBnAdd(), getBnAdd().getName());
				getPnSouth().add(getBnDel(), getBnDel().getName());
				getPnSouth().add(getBnOK(), getBnOK().getName());
				getPnSouth().add(getBnCancel(), getBnCancel().getName());
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
	 * ���� PnSouthFlowLayout ����ֵ��
	 * 
	 * @return FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private FlowLayout getPnSouthFlowLayout() {
		FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* �������� */
			ivjPnSouthFlowLayout = new FlowLayout();
			ivjPnSouthFlowLayout.setVgap(8);
			ivjPnSouthFlowLayout.setHgap(20);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * ���ݷ���ֵ���ͻ�ò��շ���ֵ �������ڣ�(02-3-19 17:29:56)
	 * 
	 * @return java.lang.String
	 * @param refPn
	 *            nc.ui.pub.beans.UIRefPane
	 * @param iReturnType
	 *            int
	 */
	private String getRefResult(UIRefPane refPn, int iReturnType) {
		String strResult = null;
		switch (iReturnType) {
		case 0: {
			strResult = refPn.getRefPK();
			break;
		}
		case 1: {
			strResult = refPn.getRefCode();
			break;
		}
		case 2: {
			strResult = refPn.getRefName();
			break;
		}
		}
		return strResult;
	}

	/**
	 * ���ݷ���ֵ���ͻ�ö�ѡ���շ���ֵ������in�� �������ڣ�(02-3-19 17:29:56)
	 * 
	 * @return java.lang.String
	 * @param refPn
	 *            nc.ui.pub.beans.UIRefPane
	 * @param iReturnType
	 *            int
	 */
	private String getRefResult_in(UIRefPane refPn, int iReturnType) {
		String[] strResults = null;
		switch (iReturnType) {
		case 0: {
			strResults = refPn.getRefPKs();
			break;
		}
		case 1: {
			strResults = refPn.getRefCodes();
			break;
		}
		case 2: {
			strResults = refPn.getRefNames();
			break;
		}
		}
		String strInResult = null;
		if (strResults != null) {
			strInResult = "(";
			for (int i = 0; i < strResults.length; i++) {
				strInResult += "'" + strResults[i] + "'";
				if (i < strResults.length - 1)
					strInResult += ",";
			}
			strInResult += ")";
		}
		return strInResult;
	}

	/**
	 * ���ѡ�еĲ���VO �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public ParamVO getSelParamVO() {
		int iSelIndex = getTable().getSelectedRow();
		return getSelParamVO(iSelIndex);
	}

	/**
	 * ���ѡ�еĲ���VO �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	private ParamVO getSelParamVO(int iSelIndex) {
		ParamVO param = null;
		if (iSelIndex != -1) {
			param = new ParamVO();
			//�������
			param.setParamCode(getTM().getValueAt(iSelIndex, COL_PARAMCODE)
					.toString());
			param.setParamName(getTM().getValueAt(iSelIndex, COL_PARAMNAME)
					.toString());
			param.setOperaCode(getTM().getValueAt(iSelIndex, COL_OPR)
					.toString());
			param.setValue(getTM().getValueAt(iSelIndex, COL_DEFAULTVALUE)
					.toString());

			//��������
			String strDataType = getTM().getValueAt(iSelIndex, COL_DATATYPE)
					.toString();
			int iDataType = ParamConst.getIndex(strDataType);
			param.setDataType(new Integer(iDataType));

			param.setConsultCode(getTM().getValueAt(iSelIndex, COL_CONSULTCODE)
					.toString());
			Object obj = getTM().getValueAt(iSelIndex, COL_IFMUST);
			if (obj == null)
				param.setIfMust(UFBoolean.valueOf(false));
			else
				param.setIfMust(UFBoolean.valueOf(obj.toString()));
			obj = getTM().getValueAt(iSelIndex, COL_REFDEPEND);
			param.setRefDepend((obj == null) ? "" : obj.toString());
			obj = getTM().getValueAt(iSelIndex, COL_IFVISIBLE);
			if (obj == null || obj.toString().equals(""))
				param.setInvisible(false);
			else
				param.setInvisible(((Boolean) obj).booleanValue());
		}
		return param;
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public UITable getTable() {
		return (UITable) getTablePn().getTable();
	}

	/**
	 * ���� TablePn ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* ���棺�˷������������ɡ� */
	@SuppressWarnings("serial")
	private nc.ui.pub.beans.UITablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new nc.ui.pub.beans.UITablePane();
				ivjTablePn.setName("TablePn");
				// user code begin {1}

				UITable table = new UITable() {
					public Component prepareEditor(TableCellEditor editor,
							int row, int column) {
						Component comp = super.prepareEditor(editor, row,
								column);
						if (column == COL_DEFAULTVALUE) {
							//�����������
							doRefDepend(getParamVOs(), editor, row, column);
						}
						return comp;
					}

					public void editingStopped(ChangeEvent e) {
						int row = editingRow;
						int col = editingColumn;
						TableCellEditor editor = getCellEditor();
						//�����༭״̬
						if (editor != null && row != -1) {
							//��þ�ֵ
							Object obj = getValueAt(row, col);
							String oldValue = (obj == null) ? "" : obj
									.toString();
							//������ֵ
							removeEditor();
							if (col == COL_DEFAULTVALUE)
								afterEdit(editor, row, col);
							else {
								setValueAt(editor.getCellEditorValue(), row,
										col);
								if (col == COL_DATATYPE) {
									String strType = getValueAt(row,
											COL_DATATYPE).toString();
									if (strType.endsWith(nc.ui.ml.NCLangRes
											.getInstance().getStrByID(
													"10241201",
													"UPP10241201-000056")/*
																		  * @res
																		  * "����"
																		  */)
											&& !oldValue
													.endsWith(nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"10241201",
																	"UPP10241201-000056")/*
																						  * @res
																						  * "����"
																						  */)) {
										setValueAt(IBusiType.BUSITYPE[0], row,
												COL_CONSULTCODE);
										setValueAt("", row, COL_DEFAULTVALUE);
									}
									//else
									//if (!strType.endsWith("����") &&
									// oldValue.endsWith("����"))
									//setValueAt("", row, COL_CONSULTCODE);
								} else if (col == COL_CONSULTCODE) {
									String strConsult = getValueAt(row,
											COL_CONSULTCODE).toString();
									if (!strConsult.equals(oldValue))
										setValueAt("", row, COL_DEFAULTVALUE);
								}
							}
						}
					}
				};
				ivjTablePn.setTable(table);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn;
	}

	/**
	 * ��ñ�ģ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
	}

	/**
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				getUIDialogContentPane().add(getPnSouth(), "South");
				getUIDialogContentPane().add(getPnNorth(), "North");
				getUIDialogContentPane().add(getTablePn(), "Center");
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
		getBnCancel().addActionListener(ivjEventHandler);
		getBnOK().addActionListener(ivjEventHandler);
		getPnSouth().addMouseListener(ivjEventHandler);
	}

	/**
	 * �����б༭�� �������ڣ�(2003-9-17 14:50:41)
	 */
	public void initEditorValue() {
		//���������б༭��
		JComboBox cbbOperator = new UIComboBox(new ParamConst().DATATYPE_NOTE);
		TableColumn tc = getTable().getColumn(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UC000-0002279")/* @res "��������" */);
		tc.setCellEditor(new UIRefCellEditor(cbbOperator));
		//ö�����б༭��
		m_tfRef = new UITextField("");
		//m_tfRef.addKeyListener(this);
		tc = getTable().getColumn(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001156")/* @res "ö����" */);
		//tc.setCellEditor(new UIRefCellEditor(m_tfRef));
		BaseRefEditor editorandRenderer = new BaseRefEditor(m_tfRef);
		tc.setCellEditor(editorandRenderer);
		tc.setCellRenderer(editorandRenderer);
		//Ĭ��ֵ
		tc = getTable().getColumn(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001159")/* @res "Ĭ��ֵ" */);
		tc.setCellEditor(new UIRefCellEditor(new UIRefPane()));
		//��������
		tc = getTable().getColumn(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001161")/* @res "��������" */);
		tc.setCellEditor(new DefaultCellEditor(new UITextField()));
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ParamDefDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(640, 360);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-001157")/* @res "��������" */);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initTable();
		initEditorValue();
		// user code end
	}

	/**
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable() {
		//��ģ��
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000974")/* @res "������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000893")/* @res "��ʾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001158")/* @res "��������ʾ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UC000-0002279")/* @res "��������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001156")/* @res "ö����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001159")/* @res "Ĭ��ֵ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001160")/* @res "�Ƿ��ѡ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001161")/* @res "��������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001162") /* @res "�Ƿ�����" */}, 0) {
			public int getColumnCount() {
				return 9;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				if (col == COL_IFMUST || col == COL_IFVISIBLE)
					return Boolean.class;
				else
					return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				return true;
			}
		};
		getTable().setModel(tm);
		//���ñ�����
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(
				new int[] { 80, 100, 88, 100, 88, 172, 80, 160, 80 });
		UIUtil.autoSizeTableColumn(getTable());
		getTable().sizeColumnsToFit(-1);
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
	}

	/**
	 * �ϲ��������Ӧ
	 */
	public void pnSouth_MouseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.isAltDown()) {
			//��ʾ������������
			int[][] iRefDepends = BIModelUtil.getParamRefDepends(getParamVOs());
			//���챨��
			StringBuffer strBuf = new StringBuffer();
			int iLen = (iRefDepends == null) ? 0 : iRefDepends.length;
			for (int i = 0; i < iLen; i++) {
				int iParam1 = iRefDepends[i][0] + 1;
				int iParam2 = iRefDepends[i][1] + 1;
				strBuf.append(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-001163", null,
						new String[] { "" + iParam1, "" + iParam2 })/*
																	 * @res
																	 * "����{0}�����ڲ���{1}"
																	 */);
				if (i < iLen - 1)
					strBuf.append(",");
			}
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-001164")/* @res "������������" */, strBuf
							.toString());
		} else {
			//���ò���
			ChooseQmdDlg dlg = new ChooseQmdDlg(this);
			dlg.initTree(m_defDsName);
			dlg.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-001165")/* @res "���ò���" */);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				//���ѡ��QMD
				QueryModelDef qmd = dlg.getSelQmd();
				//������ò���
				addParamVOs(qmd.getParamVOs());
			}
		}
		return;
	}

	/**
	 * ���ɲ���ģ�� �������ڣ�(2003-9-17 15:24:54)
	 * @i18n miufo00307=�Զ��������
	 * @i18n miufo00308=һ���Զ������
	 */
	private AbstractRefModel procRefModel(String strConsult, String strOpr) {

		AbstractRefModel rm = null;
		try {
			if (strConsult.toLowerCase().startsWith("<html>"))
				return null;
			int iBeginIndex = strConsult.indexOf("<");
			int iEndIndex = strConsult.indexOf(">");
			if (iBeginIndex != -1 && iEndIndex != -1) {
				//�Զ������
				String className = strConsult.substring(iBeginIndex + 1,
						iEndIndex);
				//����
				int iLen = strConsult.length();
				if (iEndIndex != iLen - 1) {
					AppDebug.debug("�Զ��������");//@devTools System.out.println("�Զ��������");
					//�Զ��������
					String pkDefDef = strConsult.substring(iEndIndex + 1, iLen);
					rm = (DefaultDefdocRefModel) Class.forName(className)
							.newInstance();
					((DefaultDefdocRefModel) rm).setPkdefdef(pkDefDef);
				} else {
					AppDebug.debug("һ���Զ������");//@devTools System.out.println("һ���Զ������");
					//һ���Զ������
					rm = (AbstractRefModel) Class.forName(className)
							.newInstance();
				}
			} else {
				iBeginIndex = strConsult.indexOf("[");
				iEndIndex = strConsult.indexOf("]");
				if (iBeginIndex != -1 && iEndIndex != -1) {
					//��ѯ�Ƶ�����
					String info = strConsult.substring(iBeginIndex + 1,
							iEndIndex);
					//ȡ����ѯID�����ձ��⣬������ţ�������ţ��������
					StringTokenizer st = new StringTokenizer(info, ",");
					String queryId = st.nextToken();
					String refTitle = st.nextToken();
					String idIndex = st.nextToken();
					String codeIndex = st.nextToken();
					String nameIndex = st.nextToken();
					//
					String[] strRefInfos = new String[] { queryId, m_defDsName,
							refTitle };
					int[] iFldIndices = new int[] { Integer.parseInt(idIndex),
							Integer.parseInt(codeIndex),
							Integer.parseInt(nameIndex) };
					//���ò�ѯ�Ƶ�����ģ��
					rm = new QERefModel();
					((QERefModel) rm).setRefInfo(strRefInfos, iFldIndices);
					((QERefModel) rm).setUseDataPower(false);
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		return rm;
	}

	/**
	 * ���ò���VO���� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public void setParamVOs(ParamVO[] params, String dsName) {
		getTM().setNumRows(0);
		addParamVOs(params);
		m_dsName = dsName;
	}

	/**
	 * ���þ��ж��� �������ڣ�(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		for (int i = 0; i < getTable().getColumnCount(); i++) {
			TableColumn tc = getTable().getColumnModel().getColumn(i);
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}
	}
}   