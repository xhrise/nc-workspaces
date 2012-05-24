package nc.ui.pub.pf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.wfengine.engine.ActivityInstance;
import nc.itf.uap.pf.IWorkflowDefine;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.pub.DapCall;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.workflownote.WorkitemColumnInfo;
import nc.ui.wfengine.PfWorkflowBO_Client;
import nc.ui.wfengine.designer.ProcessGraph;
import nc.ui.wfengine.flowchart.FlowChart;
import nc.ui.wfengine.flowchart.UfWGraphModel;
import nc.vo.pf.pub.IDapType;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.AssignableInfo;
import nc.vo.pub.pf.CurrencyInfo;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.uap.wfmonitor.ActivityRouteRes;
import nc.vo.wfengine.core.XpdlPackage;
import nc.vo.wfengine.core.parser.UfXPDLParser;
import nc.vo.wfengine.core.parser.XPDLNames;
import nc.vo.wfengine.core.parser.XPDLParserException;
import nc.vo.wfengine.core.util.CoreUtilities;
import nc.vo.wfengine.core.util.DurationUnit;
import nc.vo.wfengine.core.workflow.BasicWorkflowProcess;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.definition.WfProcessParentMap;
import nc.vo.wfengine.pub.WFTask;

/**
 * ���������Ի���,��PfUtilClient���ñ�ҵ��ť��������
 *
 * @author ���ھ� 2001-4-24
 * @modifier leijun 2004-03-03 ���Ӳ鿴�õ�����ʷ������Ϣ�����
 * @modifier leijun 2005-1-4 ָ��ʹ���µ�ģʽ;��ʷ������Ϣͼ�λ�չ��
 * @modifier leijun 2005-2-24 ���ﻯi18n
 * @modifier leijun 2005-6-1 ��ʷ��¼�н��ʹ�ñ��ҽ��,�ҿ��ƾ���
 * @modifier leijun 2007-5-28 ��ʷ��¼������"��ʱ"��
 * @modifier leijun 2008-4-18 ����ָ�ɺ󣬲ſɵ��ȷ�������س���
 * @modifier leijun 2008-12 ָ��ʱԤ���ж�ת��·���Ƿ����㣻��������׼�Ͳ���׼��ʾ��ͬ��ָ�ɻ
 */
public class WorkFlowCheckDlg_EH extends UIDialog implements ActionListener {

	private String[] COLUMN_NAMES = new String[] { WorkitemColumnInfo.CHECKDATE.toString(),
			WorkitemColumnInfo.DURATION.toString(), WorkitemColumnInfo.APPROVENOTE.toString(),
			WorkitemColumnInfo.APPROVESTATUS.toString(), WorkitemColumnInfo.APPROVERESULT.toString(),
			WorkitemColumnInfo.CHECKER.toString(), WorkitemColumnInfo.LOCALMONEY.toString() };

	private UIButton _btnDispatch = null;

	private UIButton _btnHistoryGraph = null;

	//"��ʷ"������Ϣ��ť
	private UIButton _btnHistoryNote = null;

	/**
	 * ����ͼ�Ļ���
	 */
	private Hashtable _hashGraphCache = new Hashtable();

	private UITablePane _historyNoteTable = null;

	//"��ʷ"������Ϣ���
	private UIPanel _pnlHistoryNote = null;

	private int _localDigest = 2;

	private boolean m_isShowMoney = false;

	private UILabel ivjlblAssMny = null;

	private UILabel ivjlblLocalMny = null;

	private UILabel ivjlblMny = null;

	private UILabel ivjlblShowStatus = null;

	private UIButton ivjonCancel = null;

	private UIButton ivjonOk = null;

	private UIPanel ivjpnlBtn = null;

	private UIPanel ivjpnlCheck = null;

	private UIPanel ivjpnlMoney = null;

	private UIPanel ivjpnlRadio = null;

	private UIPanel ivjpnlShowState = null;

	private UIPanel ivjpnlState = null;

	private UIRadioButton ivjrbNoPass = null;

	private UIRadioButton ivjrbPass = null;

	private UIRefPane ivjrefRemark = null;

	private UIScrollPane ivjscpPanel = null;

	private UITextArea ivjtaCheckNote = null;

	private UIRefPane ivjtxtAssMny = null;

	private UIRefPane ivjtxtLocalMny = null;

	private UIRefPane ivjtxtMny = null;

	private UIPanel ivjUIDialogContentPane = null;

	private ButtonGroup m_bgGroup = new ButtonGroup();

	private UIRadioButton m_rbRejectFirst = null;

	private PfUtilWorkFlowVO m_workFlow = null;

	private DispatchDialog m_dispatchDlg;

	/**
	 * @param parent
	 * @param workFlow
	 * @param isShowMoney
	 */
	public WorkFlowCheckDlg_EH(Container parent, PfUtilWorkFlowVO workFlow, boolean isShowMoney) {
		super(parent);
		this.m_workFlow = workFlow;
		this.m_isShowMoney = isShowMoney;
		//����һ���߳�,��ʼ�������
		CoreUtilities.dummyThread4Performance();
		//��ʼ������
		initialize();

		//Ĭ��ѡ��"���ص��Ƶ���" // ԭΪ����׼��
		getRbRejectFirst().doClick();
	}

	public void hideHistoryAndGraph() {
		//����������ʷ���
		getUIDialogContentPane().remove(getPnlHistoryNote());

		//����������ť
		getpnlBtn().remove(getBtnHistoryCheckNote());
		getpnlBtn().remove(getBtnHistoryGraph());
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == getonOk()) {
			onOk();
		} else if (obj == getonCancel()) {
			this.closeCancel();
		} else if (obj == getBtnHistoryCheckNote()) {
			onBtnHistoryNote();
		} else if (obj == getBtnHistoryGraph()) {
			onBtnGraphClicked();
		} else if (obj == getBtnDispatch()) {
			onBtnDispatchClicked();
		} else if (obj == getrbPass()) {
			onRadioPassClicked();
		} else if (obj == getrbNoPass()) {
			onRadioNopassClicked();
		} else if (obj == getRbRejectFirst()) {
			onRadioRejectClicked();
		}
	}

	public void onRadioRejectClicked() {
		getonOk().setEnabled(true);
		gettaCheckNote().setText(getRbRejectFirst().getText());

		//����ʱ����ָ��
		getBtnDispatch().setVisible(false);
	}

	private void onRadioNopassClicked() {
		boolean isNeedDispatch = isExistAssignableInfoWhenNopass();
		if (isNeedDispatch)
			getonOk().setEnabled(isAssignFinished() ? true : false);
		else
			getonOk().setEnabled(true);
		gettaCheckNote().setText(getrbNoPass().getText());

		getBtnDispatch().setVisible(isNeedDispatch);
	}

	private void onRadioPassClicked() {
		boolean isNeedDispatch = isExistAssignableInfoWhenPass();
		if (isNeedDispatch)
			getonOk().setEnabled(isAssignFinished() ? true : false);
		else
			getonOk().setEnabled(true);
		gettaCheckNote().setText(getrbPass().getText());

		getBtnDispatch().setVisible(isNeedDispatch);
	}

	/**
	 * �¼������� ע��
	 */
	public void addEventListener() {
		getrbPass().addActionListener(this);
		getrbNoPass().addActionListener(this);
		getRbRejectFirst().addActionListener(this);
		getonOk().addActionListener(this);
		getonCancel().addActionListener(this);
		gettaCheckNote().addKeyListener(this);
		getBtnHistoryCheckNote().addActionListener(this);
		//lj+
		getBtnHistoryGraph().addActionListener(this);
		getBtnDispatch().addActionListener(this);
	}

	/**
	 * ��������ͼҳǩ
	 * @param lhm
	 * @return
	 */
	private JComponent constructGraphTab(ActivityRouteRes activityRoute) {
		UITabbedPane tabPane = new UITabbedPane();
		//����һ����ʱ��
		XpdlPackage pkg = new XpdlPackage("unknown", "unknown", null);
		pkg.getExtendedAttributes().put(XPDLNames.MADE_BY, "UFW");

		String def_xpdl = null;
		ActivityRouteRes currentRoute = activityRoute;
		while (currentRoute != null) {
			WfProcessParentMap ppm = (WfProcessParentMap) currentRoute.getProcessParentMap();
			def_xpdl = ppm.getXpdlString().toString();

			WorkflowProcess wp = null;
			try {
				//ǰ̨����XML��Ϊ����
				wp = UfXPDLParser.getInstance().parseProcess(def_xpdl);
			} catch (XPDLParserException e) {
				Logger.error(e.getMessage(), e);
				continue;
			}
			wp.setPackage(pkg);

			//��ʼ��Graph
			FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
			//���ù�����ʾ
			ToolTipManager.sharedInstance().registerComponent(auditChart);
			//auditChart.setEnabled(false);
			auditChart.populateByWorkflowProcess(wp, false);
			//auditChart.setBorder(BorderFactory.createEtchedBorder());

			//��ǰ����ʵ���е����л
			ActivityInstance[] acts = currentRoute.getActivityInstance();
			String[] startedActivityDefIds = new String[acts.length];
			for (int i = 0; i < acts.length; i++) {
				startedActivityDefIds[i] = acts[i].getActivityID();
			}

			//��ǰ�
			HashSet hsRunningActivityDefIds = new HashSet();
			hsRunningActivityDefIds.add(ppm.getActivityDefId());
			auditChart.setActivityRouteHighView(hsRunningActivityDefIds, startedActivityDefIds,
					currentRoute.getActivityRelations(), Color.RED, Color.BLUE);

			UIScrollPane graphScrollPane = new UIScrollPane(auditChart);
			JViewport vport = graphScrollPane.getViewport();
			vport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

			String title = auditChart.getWorkflowProcess().getName() + " "
					+ ((BasicWorkflowProcess) auditChart.getWorkflowProcess()).getVersion();
			//graphScrollPane.setBorder(BorderFactory.createEtchedBorder());
			tabPane.addTab(title, graphScrollPane);

			currentRoute = currentRoute.getParentActivityRoute();//ȡ�����̣�����ѭ��
		}

		return tabPane;
	}

	/**
	 * �½�һ����ָ�ɡ���ť
	 */
	private UIButton getBtnDispatch() {
		if (_btnDispatch == null) {
			_btnDispatch = new UIButton();
			_btnDispatch.setName("btnDispatch");
			_btnDispatch.setText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000253")/*@res "ָ��"*/
					+ ">>");
		}
		return _btnDispatch;
	}

	/**
	 * ����"��ʷ>>"��ť
	 * @return UIButton
	 */
	private UIButton getBtnHistoryCheckNote() {
		if (_btnHistoryNote == null) {
			_btnHistoryNote = new UIButton();
			_btnHistoryNote.setName("historyNote");
			_btnHistoryNote
					.setText(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000155")/*@res "��ʷ>>"*/);
		}
		return _btnHistoryNote;
	}

	private UIButton getBtnHistoryGraph() {
		if (_btnHistoryGraph == null) {
			_btnHistoryGraph = new UIButton();
			_btnHistoryGraph.setName("historyGraph");
			_btnHistoryGraph.setText(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000252")/*@res "����>>"*/);
		}
		return _btnHistoryGraph;
	}

	/**
	 * ��ʷ������Ϣ��
	 * @return UITablePane
	 */
	private UITablePane getHistoryTable() {
		if (_historyNoteTable == null) {
			_historyNoteTable = new UITablePane();
			_historyNoteTable.setName("historyTable");
			_historyNoteTable.setAutoscrolls(true);
			_historyNoteTable.setPreferredSize(new Dimension(10, 150));

			//lj+
			//���ñ�ͷ��ɫ
			_historyNoteTable.getTable().getTableHeader().setBackground(new Color(47, 104, 159));
			_historyNoteTable.getTable().getTableHeader().setForeground(new Color(255, 255, 255));

			//���ñ��ģ��
			DefaultTableModel dtm = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			for (int i = 0; i < COLUMN_NAMES.length; i++) {
				dtm.addColumn(COLUMN_NAMES[i]);
			}
			_historyNoteTable.getTable().setModel(dtm);
		}
		return _historyNoteTable;
	}

	private UILabel getlblAssMny() {
		if (ivjlblAssMny == null) {
			ivjlblAssMny = new UILabel();
			ivjlblAssMny.setName("lblAssMny");
			ivjlblAssMny
					.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0003969")/*@res "���ҽ��"*/);
		}
		return ivjlblAssMny;
	}

	private UILabel getlblLocalMny() {
		if (ivjlblLocalMny == null) {
			ivjlblLocalMny = new UILabel();
			ivjlblLocalMny.setName("lblLocalMny");
			ivjlblLocalMny
					.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0002615")/*@res "���ҽ��"*/);
		}
		return ivjlblLocalMny;
	}

	private UILabel getlblMny() {
		if (ivjlblMny == null) {
			ivjlblMny = new UILabel();
			ivjlblMny.setName("lblMny");
			ivjlblMny
					.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0000926")/*@res "ԭ�ҽ��"*/);
		}
		return ivjlblMny;
	}

	private UILabel getlblShowStatus() {
		if (ivjlblShowStatus == null) {
			ivjlblShowStatus = new UILabel();
			ivjlblShowStatus.setName("lblShowStatus");
			ivjlblShowStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
			ivjlblShowStatus
					.setText(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000154")/*@res "��ʾ���ݣ�"*/);
			ivjlblShowStatus.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return ivjlblShowStatus;
	}

	private UIButton getonCancel() {
		if (ivjonCancel == null) {
			ivjonCancel = new UIButton();
			ivjonCancel.setName("onCancel");
			ivjonCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "ȡ��"*/);
		}
		return ivjonCancel;
	}

	private UIButton getonOk() {
		if (ivjonOk == null) {
			ivjonOk = new UIButton();
			ivjonOk.setName("onOk");
			ivjonOk.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "ȷ��"*/);
			ivjonOk.setEnabled(true);
		}
		return ivjonOk;
	}

	private UIPanel getpnlBtn() {
		if (ivjpnlBtn == null) {
			ivjpnlBtn = new UIPanel();
			ivjpnlBtn.setName("pnlBtn");
			ivjpnlBtn.setLayout(new FlowLayout());
			getpnlBtn().add(getonOk(), getonOk().getName());
			getpnlBtn().add(getonCancel(), getonCancel().getName());
			//getpnlBtn().add(getrefRemark(), getrefRemark().getName());
			//lj+
			getpnlBtn().add(getBtnHistoryCheckNote(), getBtnHistoryCheckNote().getName());
			getpnlBtn().add(getBtnHistoryGraph(), getBtnHistoryGraph().getName());
		}
		return ivjpnlBtn;
	}

	private UIPanel getpnlCheck() {
		if (ivjpnlCheck == null) {
			ivjpnlCheck = new UIPanel();
			ivjpnlCheck.setName("pnlCheck");
			ivjpnlCheck.setLayout(new BorderLayout());
			getpnlCheck().add(getpnlBtn(), "South");
			getpnlCheck().add(getpnlState(), "Center");
		}
		return ivjpnlCheck;
	}

	/**
	 * ��ʷ������Ϣ���
	 * @return UIPanel
	 */
	private UIPanel getPnlHistoryNote() {
		if (_pnlHistoryNote == null) {
			_pnlHistoryNote = new UIPanel();
			_pnlHistoryNote.setName("historyNotePanel");
			_pnlHistoryNote.setLayout(new BorderLayout());
			_pnlHistoryNote.add(new UILabel(NCLangRes.getInstance().getStrByID("102220",
					"UPP102220-000156")/*@res "��ʷ������Ϣ:"*/), BorderLayout.NORTH);
			_pnlHistoryNote.add(getHistoryTable(), BorderLayout.CENTER);
		}
		return _pnlHistoryNote;
	}

	private UIPanel getpnlMoney() {
		if (ivjpnlMoney == null) {
			ivjpnlMoney = new UIPanel();
			ivjpnlMoney.setName("pnlMoney");
			ivjpnlMoney.setPreferredSize(new Dimension(10, 30));
			ivjpnlMoney.add(getlblMny(), getlblMny().getName());
			ivjpnlMoney.add(gettxtMny(), gettxtMny().getName());
			ivjpnlMoney.add(getlblLocalMny(), getlblLocalMny().getName());
			ivjpnlMoney.add(gettxtLocalMny(), gettxtLocalMny().getName());
			ivjpnlMoney.add(getlblAssMny(), getlblAssMny().getName());
			ivjpnlMoney.add(gettxtAssMny(), gettxtAssMny().getName());
		}
		return ivjpnlMoney;
	}

	private UIPanel getpnlRadio() {
		if (ivjpnlRadio == null) {
			ivjpnlRadio = new UIPanel();
			ivjpnlRadio.setName("pnlRadio");
			//ivjpnlRadio.setMinimumSize(new Dimension(0, 30));
			//ivjpnlRadio.setBorder(new border.EtchedBorder());
			ivjpnlRadio.setLayout(new FlowLayout());
			ivjpnlRadio.add(getrbPass());
			ivjpnlRadio.add(getrbNoPass());
			//ivjpnlRadio.add(getRbRejectLast() );
			ivjpnlRadio.add(getRbRejectFirst());
			//ivjpnlRadio.setMaximumSize(new Dimension(10, 50));
		}
		return ivjpnlRadio;
	}

	private UIPanel getpnlShowState() {
		if (ivjpnlShowState == null) {
			ivjpnlShowState = new UIPanel();
			ivjpnlShowState.setName("pnlShowState");
			ivjpnlShowState.setLayout(new BorderLayout());
			ivjpnlShowState.add(getlblShowStatus(), "Center");

			//lj+
			//NOTE::�Ƿ���ʾ�����ť,��Ҫ�ж��Ƿ����"��ָ��"�ĺ�̻
			if (isExistAssignableInfo())
				ivjpnlShowState.add(getBtnDispatch(), "East");
		}
		return ivjpnlShowState;
	}

	/**
	 * �Ƿ���ڿ�ָ�ɵĺ�̻
	 * @return
	 */
	private boolean isExistAssignableInfo() {
		Vector assignInfos = m_workFlow.getTaskInfo().getAssignableInfos();
		if (assignInfos != null && assignInfos.size() > 0)
			return true;
		return false;
	}

	/**
	 * ��׼ʱ���Ƿ���ڿ�ָ�ɵĺ�̻
	 * @return
	 */
	private boolean isExistAssignableInfoWhenPass() {
		Vector<AssignableInfo> assignInfos = m_workFlow.getTaskInfo().getAssignableInfos();
		if (assignInfos != null && assignInfos.size() > 0) {
			String strCriterion = null;
			for (AssignableInfo ai : assignInfos) {
				strCriterion = ai.getCheckResultCriterion();
				if (AssignableInfo.CRITERION_NOTGIVEN.equals(strCriterion)
						|| AssignableInfo.CRITERION_PASS.equals(strCriterion))
					return true;
			}
		}
		return false;
	}

	/**
	 * ����׼ʱ���Ƿ���ڿ�ָ�ɵĺ�̻
	 * @return
	 */
	private boolean isExistAssignableInfoWhenNopass() {
		Vector<AssignableInfo> assignInfos = m_workFlow.getTaskInfo().getAssignableInfos();
		if (assignInfos != null && assignInfos.size() > 0) {
			String strCriterion = null;
			for (AssignableInfo ai : assignInfos) {
				strCriterion = ai.getCheckResultCriterion();
				if (AssignableInfo.CRITERION_NOTGIVEN.equals(strCriterion)
						|| AssignableInfo.CRITERION_NOPASS.equals(strCriterion))
					return true;
			}
		}
		return false;
	}

	private UIPanel getpnlState() {
		if (ivjpnlState == null) {
			ivjpnlState = new UIPanel();
			ivjpnlState.setName("pnlState");
			ivjpnlState.setLayout(new BorderLayout());
			ivjpnlState.add(getscpPanel(), "Center");
			ivjpnlState.add(getpnlShowState(), "North");
			if (m_isShowMoney) {
				ivjpnlState.add(getpnlMoney(), "South");
			}

			ivjpnlState.setPreferredSize(new Dimension(10, 150));
		}
		return ivjpnlState;
	}

	private UIRadioButton getrbNoPass() {
		if (ivjrbNoPass == null) {
			ivjrbNoPass = new UIRadioButton();
			ivjrbNoPass.setName("rbNoPass");
			ivjrbNoPass.setSelected(true);
			ivjrbNoPass
					.setText(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000160")/*@res "����׼"*/);
		}
		return ivjrbNoPass;
	}

	private UIRadioButton getrbPass() {
		if (ivjrbPass == null) {
			ivjrbPass = new UIRadioButton();
			ivjrbPass.setName("rbPass");
			ivjrbPass
					.setText(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000161")/*@res "��׼"*/);
		}
		return ivjrbPass;
	}

	private UIRadioButton getRbRejectFirst() {
		if (m_rbRejectFirst == null) {
			m_rbRejectFirst = new UIRadioButton();
			m_rbRejectFirst.setName("m_rbRejectFirst");
			m_rbRejectFirst.setText(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000146")/*@res "���ص��Ƶ���"*/);
		}
		return m_rbRejectFirst;
	}

	private UIRefPane getrefRemark() {
		if (ivjrefRemark == null) {
			ivjrefRemark = new UIRefPane();
			ivjrefRemark.setName("refRemark");
			ivjrefRemark.setVisible(false);
			ivjrefRemark.setRefNodeName("����ժҪ");
		}
		return ivjrefRemark;
	}

	private UIScrollPane getscpPanel() {
		if (ivjscpPanel == null) {
			ivjscpPanel = new UIScrollPane();
			ivjscpPanel.setName("scpPanel");
			getscpPanel().setViewportView(gettaCheckNote());
		}
		return ivjscpPanel;
	}

	private UITextArea gettaCheckNote() {
		if (ivjtaCheckNote == null) {
			ivjtaCheckNote = new UITextArea();
			ivjtaCheckNote.setName("taCheckNote");
			ivjtaCheckNote.setLineWrap(true);
			ivjtaCheckNote.setMaxLength(1024);
			ivjtaCheckNote.setBounds(-1, 23, 377, 157);
		}
		return ivjtaCheckNote;
	}

	private UIRefPane gettxtAssMny() {
		if (ivjtxtAssMny == null) {
			ivjtxtAssMny = new UIRefPane();
			ivjtxtAssMny.setName("txtAssMny");
			ivjtxtAssMny.setButtonVisible(false);
			ivjtxtAssMny.setTextType("TextDbl");
			ivjtxtAssMny.setEditable(false);
		}
		return ivjtxtAssMny;
	}

	private UIRefPane gettxtLocalMny() {
		if (ivjtxtLocalMny == null) {
			ivjtxtLocalMny = new UIRefPane();
			ivjtxtLocalMny.setName("txtLocalMny");
			ivjtxtLocalMny.setButtonVisible(false);
			ivjtxtLocalMny.setTextType("TextDbl");
			ivjtxtLocalMny.setEditable(false);
		}
		return ivjtxtLocalMny;
	}

	private UIRefPane gettxtMny() {
		if (ivjtxtMny == null) {
			ivjtxtMny = new UIRefPane();
			ivjtxtMny.setName("txtMny");
			ivjtxtMny.setButtonVisible(false);
			ivjtxtMny.setTextType("TextDbl");
			ivjtxtMny.setEnabled(true);
			ivjtxtMny.setEditable(false);
		}
		return ivjtxtMny;
	}

	private UIPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new UIPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			ivjUIDialogContentPane.add(getpnlRadio(), "Center");
			ivjUIDialogContentPane.add(getpnlCheck(), "South");
			//lj+
			ivjUIDialogContentPane.add(getPnlHistoryNote(), "North");
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * @return nc.vo.pub.pf.PfUtilWorkFlowVO
	 */
	public PfUtilWorkFlowVO getWorkFlow() {
		return m_workFlow;
	}

	protected void hotKeyPressed(KeyStroke hotKey, KeyEvent e) {
		if (hotKey.getKeyCode() == KeyEvent.VK_F2) {
			getrefRemark().getRef().showModal();
			int pos = gettaCheckNote().getCaretPosition();
			gettaCheckNote().insert("\n" + getrefRemark().getRefName(), pos);
			//gettaCheckNote().append("\n" + getrefRemark().getRefName());
			e.consume();
		}
	}

	private void initialize() {
		setResizable(true);
		setName("PfWorkFlowCheck");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 450);
		setTitle(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000163")/*@res "�����������"*/);
		setContentPane(getUIDialogContentPane());

		addEventListener();
		m_bgGroup.add(getrbNoPass());
		m_bgGroup.add(getrbPass());
		//m_bgGroup.add(getRbRejectLast());
		m_bgGroup.add(getRbRejectFirst());

		//lj+
		getpnlRadio().setBorder(
				new TitledBorder(BorderFactory.createEtchedBorder(), NCLangRes.getInstance().getStrByID(
						"102220", "UPP102220-000194")/*@res "������"*/, TitledBorder.CENTER, TitledBorder.TOP,
						new Font("Dialog", 0, 12)));

		///���ý��
		initCurrency();
	}

	private void initCurrency() {

		nc.vo.bd.b20.CurrtypeVO currVo = null;
		//����
		String localCurr = null;
		try {
			localCurr = SysInitBO_Client.getPkValue(DapCall.getPkcorp(), "BD301");
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		if (localCurr != null && localCurr.trim().length() != 0) {
			currVo = PfUIDataCache.getCurrType(localCurr);
		} else {
			currVo = null;
		}
		if (currVo != null) {
			_localDigest = currVo.getCurrdigit().intValue();
		} else {
			_localDigest = 2;
		}

		//�������ʾ���
		if (!m_isShowMoney) {
			//�����ı������踳ֵ����������ʷ������Ϣ�Ľ����
			TableColumnModel tcm = getHistoryTable().getTable().getColumnModel();
			int iColIndexLocalMoney = PfUtilUITools.getColumnIndex(WorkitemColumnInfo.LOCALMONEY
					.toString(), tcm);
			if (iColIndexLocalMoney > -1)
				tcm.removeColumn(tcm.getColumn(iColIndexLocalMoney));
			return;
		}

		//�����ʾ���
		CurrencyInfo cinfo = m_workFlow.getFirstCurrency();

		gettxtLocalMny().setNumPoint(_localDigest);
		gettxtLocalMny().setText(String.valueOf(cinfo.getLocalMoney()));

		int digest = 0; //����
		//ԭ��
		if (cinfo.getCurrency() != null && cinfo.getCurrency().trim().length() != 0) {
			currVo = PfUIDataCache.getCurrType(cinfo.getCurrency());
		} else {
			currVo = null;
		}
		if (currVo != null) {
			digest = currVo.getCurrdigit().intValue();
		} else {
			digest = 2;
		}
		gettxtMny().setNumPoint(digest);
		gettxtMny().setText(String.valueOf(cinfo.getMoney()));

		//����
		UFBoolean isAss = UFBoolean.FALSE;
		try {
			isAss = SysInitBO_Client.getParaBoolean(DapCall.getPkcorp(), "BD302");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		if (isAss != null && isAss.booleanValue()) {
			//��ʾ���ң���ѯ���ұ���
			String assCurr = null;
			try {
				assCurr = SysInitBO_Client.getPkValue(DapCall.getPkcorp(), "BD303");
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
			if (assCurr != null) {
				currVo = PfUIDataCache.getCurrType(assCurr);
				if (currVo != null) {
					digest = currVo.getCurrdigit().intValue();
				} else {
					digest = 2;
				}
				gettxtAssMny().setNumPoint(digest);
				gettxtAssMny().setText(String.valueOf(cinfo.getAssMoney()));
			}
		} else {
			//���ظ���
			getpnlMoney().remove(getlblAssMny());
			getpnlMoney().remove(gettxtAssMny());
		}

	}

	/**
	 * "����>>"��ť�¼���Ӧ
	 * <li>�����Ի�������ʾ����ͼ
	 * <li>����������,������ʾ�����̵Ļ
	 */
	private void onBtnGraphClicked() {
		WFTask task = m_workFlow.getTaskInfo().getTask();
		String defPK = task.getWfProcessDefPK();
		String procInstancePK = task.getWfProcessInstancePK();

		JComponent centerComp = null;
		Object obj = _hashGraphCache.get(defPK);
		if (obj == null) {
			//�ҵ�WFTask�����Ļ�������̶��壬���������̶���
			ActivityRouteRes activityRoute = null;
			IWorkflowDefine wfDefine = (IWorkflowDefine) NCLocator.getInstance().lookup(
					IWorkflowDefine.class.getName());
			try {
				activityRoute = wfDefine.queryActivityRoute(procInstancePK, task.getActivityID());
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000237")/*@res "����"*/, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000494")/*��ѯ���ݵ�����ͼ�����쳣��"*/
						+ e.getMessage());
				return;
			}
			centerComp = constructGraphTab(activityRoute);

			//����
			_hashGraphCache.put(defPK, centerComp);
		} else {
			//auditChart = (FlowChart) obj;
			centerComp = (JComponent) obj;
		}

		//��ʾ
		PfUtilUITools.showDialog(this, NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000138")/*@res "����ͼ"*/, centerComp);
	}

	/**
	 * ʹ�û�ȡ����������ģ��
	 * <li>ֻ��ȡ6����Ϣ:�������� ���� ����״�� ������ ������ ���
	 * <li>��������һ�У���ʱ
	 */
	private void onBtnHistoryNote() {
		DefaultTableModel dtm = (DefaultTableModel) getHistoryTable().getTable().getModel();
		//���ȣ���ձ�ģ��
		int count = dtm.getRowCount();
		for (int i = 0; i < count; i++) {
			dtm.removeRow(0);
		}

		//��ѯ���ݣ�����䵽��ģ��
		String sql = "select dealdate,checknote,b.user_name,localmoney,approvestatus,approveresult,senddate,actiontype "
				+ "from pub_workflownote a, sm_user b "
				+ "where a.checkman=b.cuserid and approveresult is not null and billid='"
				+ m_workFlow.getBillId() + "' order by a.dealdate";

		java.util.Vector vecApproveHistory = null;
		try {
			vecApproveHistory = PfWorkflowBO_Client.queryDataBySQL(sql, 8, new int[] { IDapType.UFDATE,
					IDapType.STRING, IDapType.STRING, IDapType.UFDOUBLE, IDapType.INTEGER, IDapType.STRING,
					IDapType.UFDATE, IDapType.STRING });
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return;
		}

		for (int i = 0; i < (vecApproveHistory == null ? 0 : vecApproveHistory.size()); i++) {
			Vector rowData = (Vector) vecApproveHistory.get(i);
			//XXX:��һ������Ϊ�к�

			//���ù������Ƿ�Ϊ�޵�
			String strActiontype = String.valueOf(rowData.get(8));
			boolean isMakebill = false;
			if (WorkflownoteVO.WORKITEM_TYPE_MAKEBILL.equalsIgnoreCase(strActiontype))
				isMakebill = true;

			//��������״��
			int status = ((Integer) rowData.get(5)).intValue();
			String strStatus = WFTask.resolveApproveStatus(status, isMakebill);

			//����������
			String strResultI18N = WFTask.resolveApproveResult(isMakebill ? null : rowData.get(6));

			//Ϊ���ҽ�����þ��� lj@2005-6-2
			UFDouble localMoney = (UFDouble) rowData.get(4);
			UFDouble newLM = null;
			if (localMoney != null)
				newLM = new UFDouble(localMoney.getDouble(), _localDigest);

			//�����������ڼ�����ʱ
			String strSendTime = String.valueOf(rowData.get(7));
			String strApproveTime = String.valueOf(rowData.get(1));
			//UFDateTime serverTime = ClientEnvironment.getServerTime();
			String ellapsed = DurationUnit.getElapsedTime(new UFDateTime(strSendTime), new UFDateTime(
					strApproveTime));

			//��ӵ���ģ��
			dtm.addRow(new Object[] { rowData.get(1), ellapsed, rowData.get(2), strStatus, strResultI18N,
					rowData.get(3), newLM });
		}
	}

	private DispatchDialog getDispatchDlg() {
		if (m_dispatchDlg == null) {
			m_dispatchDlg = new DispatchDialog(this);
		}
		return m_dispatchDlg;
	}

	/**
	 * "ָ��"��ť�¼���Ӧ
	 */
	private void onBtnDispatchClicked() {
		//��ʾָ�ɶԻ����ռ�ʵ��ָ����Ϣ
		boolean isPass = getrbPass().isSelected();
		getDispatchDlg().initByWorkflowVO(m_workFlow,
				isPass ? AssignableInfo.CRITERION_PASS : AssignableInfo.CRITERION_NOPASS);
		getDispatchDlg().showModal();

		//XXX:��̻ֻҪ��һ��ָ�ɺ�Ϳ�ʹ��ȷ����ť
		getonOk().setEnabled(getRbRejectFirst().isSelected() ? true : isAssignFinished());
	}

	/**
	 * ����Ƿ������ָ��
	 * <li>����ж�����Ҫָ�ɣ���ֻ��ָ��һ�����ɡ�
	 * @return
	 */
	private boolean isAssignFinished() {
		Vector assignInfos = m_workFlow.getTaskInfo().getAssignableInfos();
		if (assignInfos.size() == 0)
			return true;

		boolean dispatchFinished = false;
		boolean isPass = getrbPass().isSelected();
		for (Iterator iter = assignInfos.iterator(); iter.hasNext();) {
			AssignableInfo ainfo = (AssignableInfo) iter.next();
			String s = ainfo.getCheckResultCriterion();
			if (isPass) {
				if (AssignableInfo.CRITERION_NOPASS.equals(s))
					continue;
			} else if (AssignableInfo.CRITERION_PASS.equals(s))
				continue;

			if (ainfo.getAssignedOperatorPKs().size() > 0) {
				dispatchFinished = true;
				break;
			}
		}
		return dispatchFinished;
	}

	/**
	 * ����Ƿ������ָ��
	 * @return
	 * @deprecated ���û��ָ�ɣ��������Ϊȫ����Ա��
	 */
	private boolean checkAssigned() {
		Vector assignInfos = m_workFlow.getTaskInfo().getAssignableInfos();
		if (assignInfos != null && assignInfos.size() > 0) {
			for (Iterator iter = assignInfos.iterator(); iter.hasNext();) {
				AssignableInfo ainfo = (AssignableInfo) iter.next();
				if (ainfo.getAssignedOperatorPKs().size() == 0)
					return false;
			}
		}
		return true;
	}

	/**
	 * "ȷ��"��ť���¼���Ӧ,��Ϊ������VO���������Ϣ
	 */
	public void onOk() {

		// ����Ƿ�ָ����
		//if (!checkAssigned()) {
		//	MessageDialog.showHintDlg(this, "��ʾ", "��Ϊ�ָ�ɲ����ߣ�");
		//	return;
		//}

		//��ȡ����
		m_workFlow.setCheckNote(gettaCheckNote().getText());
		//��ȡ�������-ͨ��/��ͨ��/����
		if (getrbPass().isSelected()) {
			m_workFlow.setIsCheckPass(true);
		} else if (getrbNoPass().isSelected()) {
			m_workFlow.setIsCheckPass(false);
		} else if (getRbRejectFirst().isSelected()) {
			m_workFlow.getTaskInfo().getTask().setTaskType(WFTask.TYPE_BACKWARD);
			m_workFlow.getTaskInfo().getTask().setBackToFirstActivity(true);
		}
		//    else if (getRbRejectLast().isSelected()) {
		//      m_workFlow.getTaskInfo().getTask().setTaskType(Task.TYPE_BACKWARD);
		//      m_workFlow.getTaskInfo().getTask().setBackToFirstActivity(false);
		//    }
		//��ȡ����ʱ��
		m_workFlow.setDealDate(ClientEnvironment.getServerTime());

		this.closeOK();
	}

}