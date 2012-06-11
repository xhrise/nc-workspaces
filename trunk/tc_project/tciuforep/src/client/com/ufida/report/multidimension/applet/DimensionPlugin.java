package com.ufida.report.multidimension.applet;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;

import com.ufida.bi.base.BIException;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiDimemsionModel;
import com.ufida.report.multidimension.model.MultiReportEnv;
import com.ufida.report.rep.applet.BIPlugIn;
import com.ufida.report.rep.model.BIContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 
 * @author zzl 2005-4-27
 */
public class DimensionPlugin extends BIPlugIn{

	private MultiDimemsionModel m_model = null;

	private boolean m_isNewModel = false;

	private String pk_report = null;

	private String m_userID = null;

	private SelDimSetDialog m_selDimDlg = null;

	private AnalyzerDialog m_analyzerDlg = null;

	private DrillThroughSetDialog m_drillDlg = null;

	private ReportFormatDialog m_formatDlg = null;

	private ColumnFormatDialog m_rowFormatDlg = null;

	private DimensionDescriptor m_desc = null;

	private int m_initState = UfoReport.OPERATION_INPUT;

	private transient EventListenerList listenerList = new EventListenerList();

	private class AnalyzerDlgClose implements UIDialogListener {

		public void dialogClosed(UIDialogEvent event) {
			int state = getModel().getOperationState();
			if (state == UfoReport.OPERATION_INPUT) {
				MultiReportEnv env = getModel().getReportEnv();
				try {
					getModel().setOperationState(state, env.getStrReportID(), env.getStrUserID());
				} catch (BIException ex) {
					JOptionPane.showMessageDialog(getReport(), ex.getMessage());
				}
			}
		}

	}

	/**
	 * @return Returns the m_analyzerDlg.
	 */
	public AnalyzerDialog getAnalyzerDlg() {
		if (m_analyzerDlg == null) {
			m_analyzerDlg = new AnalyzerDialog(getReport(), getModel());
			m_analyzerDlg.addUIDialogListener(new AnalyzerDlgClose());
			getModel().addChangeListener(m_analyzerDlg);
		}
		return m_analyzerDlg;
	}

	/**
	 * @return Returns the m_drillDlg.
	 */
	public DrillThroughSetDialog getDrillDlg() {
		if (m_drillDlg == null) {
			m_drillDlg = new DrillThroughSetDialog(getReport(), getModel().getSelDimModel(), m_userID);
		}
		return m_drillDlg;
	}

	/**
	 * @return Returns the m_formatDlg.
	 */
	public ReportFormatDialog getFormatDlg() {
		if (m_formatDlg == null) {
			m_formatDlg = new ReportFormatDialog(getReport());
		}
		return m_formatDlg;
	}

	/**
	 * @return Returns the m_formatDlg.
	 */
	public ColumnFormatDialog getRowFormatDlg() {
		if (m_rowFormatDlg == null) {
			m_rowFormatDlg = new ColumnFormatDialog(getReport());
		}
		return m_rowFormatDlg;
	}

	/**
	 * @return Returns the m_selDimDlg.
	 */
	public SelDimSetDialog getSelDimDlg() {
		if (m_selDimDlg == null) {
			m_selDimDlg = new SelDimSetDialog(getReport());
			m_selDimDlg.setLocationRelativeTo(getReport());
		}
		return m_selDimDlg;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
		try {
			m_initState = getReport().getOperationState();

			// 初始化模型
			BIContextVO context = (BIContextVO) getReport().getContextVo();
			pk_report = context.getReportPK();
			m_userID = context.getCurUserID();

			m_model = (MultiDimemsionModel) context.getBaseReportModel();

			if (m_model == null || m_model.getSelDimModel().getQueryModel() == null) {// 新建报表直接弹出查询和维度定义
				getSelDimDlg().setSelModel(getModel().getSelDimModel(), m_userID);
				if (getSelDimDlg().showModal() == UIDialog.ID_OK) {
					getModel().setSelDimModel(getSelDimDlg().getSelModel());
				}
			}else{
				getModel().getCalcSrv();//此调用用于重建公式链，并且设置各个公式项的有效性
			}
			// if (m_model != null) {
			// m_model.setCellsModel(getReport().getCellsModel());
			getReport().getTable().setCurCellsModel(getModel().getCellsModel());
			m_model.setOperationState(m_initState, pk_report, m_userID);

			getReport().getTable().getCells().setCellsAuthorization(
					new MultiDimensionReportAuth(this, getModel().getReportEnv()));

		} catch (Exception e1) {
			AppDebug.debug(e1);
			return;
		}

	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
	 */
	public void shutdown() {
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#store()
	 */
	/**
	 */
	public void store() {

		ReportSrv srv = new ReportSrv();
		ReportVO vo = null;
		ValueObject[] vo2 = null;
		try {

			// UfoReport tmp_report = getReport();
			// setReport(null);
			// getModel().setCellsModel(null);
			//			
			if (pk_report != null) {
				ValueObject[] vos = srv.getByIDs(new String[] { pk_report });
				if (vos != null && vos.length > 0)
					vo = (ReportVO) vos[0];
				if (vo == null)
					vo = new ReportVO();
				vo.setDefinition(getModel());
				srv.update(new ReportVO[] { vo });
			} else {
				vo = new ReportVO();
				vo.setDefinition(getModel());
				vo.setType(new Integer(ReportResource.INT_REPORT_MULTI));
				vo2 = srv.create(new ReportVO[] { vo });
			}
			if (vo2 != null) {
				pk_report = vo2[0].getPrimaryKey();
			}
			// AppDebug.debug();//@devTools System.out.println();
			AppDebug.debug("保存MultiDimension报表，保存主键为：" + pk_report);// @devTools
																	// System.out.println("保存MultiDimension报表，保存主键为："
																	// +
																	// pk_report);
			// AppDebug.debug();//@devTools System.out.println();

			// setReport(tmp_report);
			// getModel().setCellsModel(tmp_report.getCellsModel());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
		}
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		m_desc = new DimensionDescriptor(this);
		return m_desc;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
	 */
	public SheetCellRenderer getDataRender(String extFmtName) {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
	 */
	public SheetCellEditor getDataEditor(String extFmtName) {
		return null;
	}

	/*
	 * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void actionPerform(UserUIEvent e) {
	}

	/*
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		return null;
	}

	/**
	 * 获取报表当前的操作状态
	 * 
	 * @return
	 */
	public int getOperationState() {
		return this.getModel().getOperationState();
	}

	public void setOperationState(int operationState) throws BIException {
		if (getModel() != null) {
			getModel().setOperationState(operationState, ((BIContextVO) getReport().getContextVo()).getReportPK(),
					((BIContextVO) getReport().getContextVo()).getCurUserID());
		}
		getSelDimDlg().setShowStep(operationState == UfoReport.OPERATION_FORMAT);
		getReport().setOperationState(operationState);
	}

	public MultiDimemsionModel getModel() {
		if (m_model == null) {
			m_model = new MultiDimemsionModel("");
		}
		return m_model;
	}

	public void setModel(MultiDimemsionModel newModel) {
		m_model = newModel;
		this
				.notifyListeners(new PropertyChangeEvent(this, IMultiDimConst.PROPERTY_PLUGIN_MODEL_CHANGED, null,
						newModel));
	}

	public boolean isNewModel() {
		return m_isNewModel;
	}

	public void setReport(UfoReport report) {
		super.setReport(report);
		// report.getTable().getCells().addMouseListener(
		// new DrillMouseListener(this));
	}

	public int getInitState() {
		return m_initState;
	}

	/**
	 * Registers an object for notification of changes to the dataset.
	 * 
	 * @param listener
	 *            the object to register.
	 */
	public void addChangeListener(PropertyChangeListener listener) {
		if (this.listenerList == null)
			listenerList = new EventListenerList();
		this.listenerList.add(PropertyChangeListener.class, listener);
	}

	/**
	 * Deregisters an object for notification of changes to the dataset.
	 * 
	 * @param listener
	 *            the object to deregister.
	 */
	public void removeChangeListener(PropertyChangeListener listener) {
		if (this.listenerList == null)
			listenerList = new EventListenerList();
		this.listenerList.remove(PropertyChangeListener.class, listener);
	}

	/**
	 * Notifies all registered listeners that the dataset has changed.
	 * 
	 * @param event
	 *            contains information about the event that triggered the
	 *            notification.
	 */
	private void notifyListeners(final PropertyChangeEvent event) {
		if (this.listenerList == null)
			listenerList = new EventListenerList();
		final Object[] listeners = this.listenerList.getListenerList();
		if (listeners == null)
			return;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PropertyChangeListener.class) {
				((PropertyChangeListener) listeners[i + 1]).propertyChange(event);
			}
		}
	}

}  