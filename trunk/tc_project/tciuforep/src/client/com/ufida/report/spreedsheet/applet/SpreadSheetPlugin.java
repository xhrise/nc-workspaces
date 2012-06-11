package com.ufida.report.spreedsheet.applet;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.event.EventListenerList;

import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.applet.SelectQueryModelDlg;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.rep.applet.BIPlugIn;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufida.report.spreedsheet.model.SpreadSheetModel;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 
 * @author zzl 2005-4-27
 */
public class SpreadSheetPlugin extends BIPlugIn implements UserActionListner,HeaderModelListener {

	private SpreadSheetModel m_model = null;

	private boolean m_isNewModel = false;

	private String pk_report = null;

	private String m_userID = null;

	private SpreadSheetDescriptor m_desc = null;

	private int m_initState = UfoReport.OPERATION_INPUT;

	private transient EventListenerList listenerList = new EventListenerList();

	private SelectQueryModelDlg m_selQueryDlg = null;

	private SelQueryAreaDlg m_selQueryAreaDlg = null;

	private SelMeasureDlg m_selMeasureDlg = null;

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

			m_model = (SpreadSheetModel) context.getBaseReportModel();

			getReport().getTable().setCurCellsModel(getModel().getCellsModel());
			m_model.setOperationState(m_initState, pk_report, m_userID);

			// 设置单元格的权限控制
			getReport().getTable().getCells().setCellsAuthorization(
					new SpreadSheetReportAuth(this, m_model.getReportEnv()));

			m_desc.applyQueryMeasureDragTarget();
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
	 * @i18n miufo00329=保存SpreadSheet报表，保存主键为：
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
				vo.setType(new Integer(ReportResource.INT_REPORT_SPREADSHT));
				vo2 = srv.create(new ReportVO[] { vo });
			}
			if (vo2 != null) {
				pk_report = vo2[0].getPrimaryKey();
			}
			// AppDebug.debug();//@devTools System.out.println();
			AppDebug.debug("保存SpreadSheet报表，保存主键为：" + pk_report);// @devTools
																	// System.out.println("保存SpreadSheet报表，保存主键为："
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
		m_desc = new SpreadSheetDescriptor(this);
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

	public void setOperationState(int operationState) {
		if (getModel() != null) {
			getModel().setOperationState(operationState, ((BIContextVO) getReport().getContextVo()).getReportPK(),
					((BIContextVO) getReport().getContextVo()).getCurUserID());
		}
		getReport().setOperationState(operationState);
		getReport().getTable().getCells().repaint(); // TODO
		// 是否有更好的刷新方式？

	}

	public SpreadSheetModel getModel() {
		if (m_model == null) {
			m_model = new SpreadSheetModel("");
		}
		return m_model;
	}

	public void setModel(SpreadSheetModel newModel) {
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

	@SuppressWarnings("unused")
	private class DrillMouseListener implements MouseListener {

		SpreadSheetPlugin m_plugin = null;

		public DrillMouseListener(SpreadSheetPlugin plugin) {
			m_plugin = plugin;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			// System.out.println("e.getClickCount(): " + e.getClickCount());
			if (e.isPopupTrigger() || e.getClickCount() < 2)
				return;

			// int drilltype = getModel().getDateDrillSet().getDrillType();
			//
			// //TODO 应该处理右键菜单弹出位置，并减去表头行数
			// CellPosition pos = getReport().getCellsModel().getSelectModel()
			// .getSelectedArea().getStart();
			//
			// DimensionPopupMenuDes.execDrill(getReport(), m_plugin, pos,
			// drilltype);

			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

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

	public SelectQueryModelDlg getAddQueryDlg() {
		if (m_selQueryDlg == null) {
			BIContextVO context = (BIContextVO) getReport().getContextVo();
			m_selQueryDlg = new SelectQueryModelDlg(getReport(), context.getCurUserID());

		}
		return m_selQueryDlg;
	}

	public SelQueryAreaDlg getSelQueryAreaDlg() {
		if (m_selQueryAreaDlg == null) {
			m_selQueryAreaDlg = new SelQueryAreaDlg(getReport());
			getReport().getCellsModel().getSelectModel().addSelectModelListener(m_selQueryAreaDlg);
		}
		return m_selQueryAreaDlg;
	}

	public SelMeasureDlg getSelMeasureDlg() {
		// if(m_selMeasureDlg == null){
		m_selMeasureDlg = new SelMeasureDlg(getReport());
		// }
		return m_selMeasureDlg;
	}

	public void userActionPerformed(UserUIEvent e) {
		// 处理事件。
		switch (e.getEventType()) {
		case UserUIEvent.PASTE:
			Cell[][] cellss = (Cell[][]) e.getNewValue();
			processPasteEvent(cellss);
			break;
		case UserUIEvent.CLEAR:
			processClearEvent();
			break;
		case UserUIEvent.COMBINECELL:
			// AreaPosition area1 = (AreaPosition) e.getOldValue();
			// TODO processCombineCellEvent(area1);
			break;
		case UserUIEvent.UNCOMBINECELL:
			// AreaPosition area2 = (AreaPosition) e.getOldValue();
			// TODO processUnCombineCellEvent(area2);
			break;
		case UserUIEvent.INSERTCELL:
			processInsertCellEvent(e);
			break;

		}
	}

	/**
	 * 处理新增事件.
	 * 
	 * @param cellss
	 * 
	 */
	private void processInsertCellEvent(UserUIEvent e) {
//		AreaPosition aimArea = (AreaPosition) e.getNewValue();
//		Cell[][] cells = getCellsModel().getCells(aimArea);
		//此时清除扩展格式有问题，将会造成移动的单元格丢掉扩展格式（事件是先发出来的，可能需要底层修改）
		//		clearCellSpreadProp(cells);
	}

	/**
	 * 处理粘贴事件.
	 * 
	 * @param cellss
	 * 
	 */
	private void processPasteEvent(Cell[][] cellss) {
		if (cellss == null || cellss.length == 0 || cellss[0] == null || cellss[0].length == 0)
			return;

		// 数据状态的粘贴不处理维度和指标信息，以免修改报表格式定义
		if (getOperationState() == ContextVO.OPERATION_INPUT)
			return;

		// 得到当前选中表页的焦点单元。
		CellPosition target = getCellsModel().getSelectModel().getAnchorCell();

		// 粘贴单元维度和指标信息
		for (int i = 0; i < cellss.length; i++) {
			if (cellss[i] != null)
				for (int j = 0; j < cellss[i].length; j++) {
					Cell cell = cellss[i][j];
					SpreadCellPropertyVO cellVO = (SpreadCellPropertyVO) cell
							.getExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP);
					if (cellVO != null) {
						Cell targetCell = getCellsModel().getCell(target.getRow() + i, target.getColumn() + j);
						if (targetCell == null) {
							targetCell = new Cell();
						}
						targetCell.setExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP, cellVO.clone());
						getCellsModel().setCell(target.getRow() + i, target.getColumn() + j, targetCell);
					}

				}
		}

	}

	/**
	 * 处理清除事件.
	 */
	private void processClearEvent() {
		// 得到当前选中的单元。
		AreaPosition selArea = getCellsModel().getSelectModel().getSelectedArea();
		if (selArea == null) {
			return;
		}
		// 数据状态的清除不处理维度和指标信息，以免修改报表格式定义
		if (getOperationState() == ContextVO.OPERATION_INPUT)
			return;

		Cell[][] cells = getCellsModel().getCells(selArea);
		clearCellSpreadProp(cells);
	}

	private void clearCellSpreadProp(Cell[][] cells){
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null) {
					for (int j = 0; j < cells[i].length; j++) {
						Cell cell = cells[i][j];
						if (cell != null)
							cell.removeExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP);
					}
				}
			}
		}

	}
	public int getPriority() {
		return HeaderModelListener.NORM_PRIORITY;
	}

	public void headerCountChanged(HeaderEvent e) {
		if(e.isHeaderAdd()){
			int index = e.getStartIndex();
			AreaPosition area = null;
			if(e.isRow())
				area = AreaPosition.getInstance(index, 0, getCellsModel().getColNum(), 1);
			else
				area = AreaPosition.getInstance(0, index, 1, getCellsModel().getRowNum());
			
			Cell[][] cells = getCellsModel().getCells(area);
			clearCellSpreadProp(cells);
		}
	}

	public void headerPropertyChanged(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

}  