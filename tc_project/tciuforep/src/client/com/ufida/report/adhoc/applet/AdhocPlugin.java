package com.ufida.report.adhoc.applet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JOptionPane;

import nc.ui.pub.beans.UILabel;
import nc.vo.bi.query.measquery.QueryModelVODef;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;

import com.ufida.bi.base.BIException;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.model.AdhocArea;
import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufida.report.rep.applet.BIPlugIn;
import com.ufida.report.rep.applet.BIReportApplet;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BIRepParams;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufida.report.rep.model.IBIField;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.FontFactory;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.re.DefaultSheetCellEditor;
import com.ufsoft.table.re.DoubleEditor;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * Adhoc������
 * 
 * @author caijie
 */
public class AdhocPlugin extends BIPlugIn implements PropertyChangeListener, UserActionListner, IUfoContextKey {
	private AdhocModel lnkAdhocModel;

	private String pk_report = null;

	private boolean m_isFormat = false;

	/**
	 * Ϊ��������ƶ�����ק�����任
	 */
	public void startup() {
		try {
			getReport().getTable().getCells().getReanderAndEditor().registEditor("ConditionFormat", new DoubleEditor());

			// ��ʼ��ģ��
			FreeQueryContextVO context = (FreeQueryContextVO) getReport().getContextVo();
				
			pk_report = context.getAttribute(REPORT_PK) == null ? null : (String)context.getAttribute(REPORT_PK);
			if (context instanceof BIContextVO)
				lnkAdhocModel = (AdhocModel) ((BIContextVO) context).getBaseReportModel();
			m_isFormat = (getReport().getOperationState() == UfoReport.OPERATION_FORMAT);
			if (lnkAdhocModel == null) {
				lnkAdhocModel = new AdhocModel("");
				lnkAdhocModel.setCellsModel(this.getReport().getCellsModel());
				lnkAdhocModel.initCellsModel();
				lnkAdhocModel.addChangeListener(this);
			} else {
				//session�м��ص�ģ�ͣ���Ϊ���������л���ģ��״̬��˲ʱ���ݶ�ʧ����Ҫ��������
				if(((BIContextVO)context).isLoadedFromSession()){
					lnkAdhocModel.chageStateFlagOnly(false);
					lnkAdhocModel.changeState(true, false);
					lnkAdhocModel.changeState(false, true);
				}
				getReport().getTable().setCurCellsModel(lnkAdhocModel.getCellsModel());
				
			}

			// ��鶨�������ͼ����еĵ�Ԫ�������Ԫ���ֵ���Ա����˹�ʽ��Ϣ�����ÿ�;
			clearOldFmtValue();

			getReport().getTable().addUserActionListener(lnkAdhocModel);
			// ����δѡ���ѯ��Ϣ��������
			if (lnkAdhocModel.getDataCenter().getCurrQuery() == null) {
				// doSelectQueryModel(context.getCurUserId());
			}

			// ���Adhoc����ģ�Ͳ��Ǵ�Session�м��أ���ִ�д�͸;
			// �˴���Adhoc����ģ�ʹ�Session�м�����ָ�ɱ���web�鿴ת������̬�ı����߲鿴
			// if (!context.isLoadedFromSession())
			// lnkAdhocModel.setOperationState(getReport().getOperationState(),
			// null, ((BIContextVO) getReport()
			// .getContextVo()).getCurUserId());

		} catch (Exception e1) {
			AppDebug.debug(e1);
			return;
		}

		// ���õ�ԪȨ��ģ��
		this.getReport().getTable().getCells().setCellsAuthorization(new AdhocAuth(this));
		//�����޸ĵ�Ԫ�����ݵļ�����
		this.getReport().getCellsModel().addCellsModelListener(new AdhocCellListener(this));
		
		// ������ק֧��
		AdhocCellsPaneDnD dnd2 = new AdhocCellsPaneDnD(this.getReport().getTable().getCells(), this);
		AdhocCellsPaneUIMouseActionHandler hander = new AdhocCellsPaneUIMouseActionHandler(this.getReport().getTable()
				.getCells());
		this.getReport().getTable().getCells().getUI().setCellsPaneUIMouseActionHandler(hander);
		//Ҫ��֤�Ҽ��¼������Ӧ,��״̬�й�
		this.getReport().resetGlobalPopMenuSupport();
		// ���ڵĸ�ʽ̬����ʾ����
		// setOperationState(ContextVO.OPERATION_INPUT);
	}

	public boolean isFormat() {
		return m_isFormat;
	}

	public int doSelectQueryModel(String userID) {
		SelectQueryModelDlg dlg = new SelectQueryModelDlg(getReport(), userID);
		dlg.setLocationRelativeTo(getReport());
		dlg.setResizable(false);
		dlg.setModal(true);
		dlg.setAlwaysOnTop(true);
		dlg.show();
		if ((dlg.getResult() == UfoDialog.ID_OK) && (dlg.getQueryModel() != null)) {
			// lnkAdhocModel.setQueryModel(dlg.getQueryModel());
			lnkAdhocModel.getDataCenter().addQuery(new QueryModelVODef(dlg.getSelQueryID()));
			return lnkAdhocModel.getDataCenter().getAllQuerys().length - 1;
		} else {
			AppDebug.debug("AdhocPlugin error when startup");
			return -1;
			// SwingUtilities.getWindowAncestor(getReport()).dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
	 */
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#store()
	 */
	public void store() {
		boolean local = false;// ���л��ļ��Ƿ��ڱ���

		int oriStatus = lnkAdhocModel.getOperationState();
		if (oriStatus != UfoReport.OPERATION_FORMAT) {
			lnkAdhocModel.setOperationState(UfoReport.OPERATION_FORMAT, ((BIContextVO) getReport().getContextVo())
					.getReportPK(), ((BIContextVO) getReport().getContextVo()).getCurUserID());
		}
		if (local) {
			String filePath = "C:\\AdhocReport.rep";
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
				out.writeObject(lnkAdhocModel);
				out.close();
			} catch (Exception e) {
				AppDebug.debug(e);// @devTools AppDebug.debug(e);
				showErrorMessage(StringResource.getStringResource("mbiadhoc00030"));
			} finally {
				if (oriStatus != lnkAdhocModel.getOperationState()) {
					lnkAdhocModel.setOperationState(oriStatus,
							((BIContextVO) getReport().getContextVo()).getReportPK(), ((BIContextVO) getReport()
									.getContextVo()).getCurUserID());
				}
			}
			setDirty(false);
		} else {
			ReportSrv srv = new ReportSrv();
			ReportVO vo = null;
			ValueObject[] vo2 = null;
			try {

				if (pk_report != null) {
					vo = ((ReportVO) (srv.getByIDs(new String[] { pk_report }))[0]);
					vo.setDefinition(lnkAdhocModel);
					srv.update(new ReportVO[] { vo });
				} else {
					vo = new ReportVO();
					vo.setDefinition(lnkAdhocModel);
					vo.setType(new Integer(ReportResource.INT_REPORT_ADHOC));
					vo2 = srv.create(new ReportVO[] { vo });
				}
				if (vo2 != null) {
					pk_report = vo2[0].getPrimaryKey();
				}
			} catch (BusinessException e) {
				AppDebug.debug(e);
				showErrorMessage(e.getMessage());
			} finally {
				if (oriStatus != lnkAdhocModel.getOperationState()) {
					lnkAdhocModel.setOperationState(oriStatus,
							((BIContextVO) getReport().getContextVo()).getReportPK(), ((BIContextVO) getReport()
									.getContextVo()).getCurUserID());
				}
			}
			setDirty(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		return new String[] { AdhocPublic.EXT_FMT_SELECTED_FIELD, AdhocPublic.EXT_FMT_PAGE_DIM_FIELD,
				AdhocPublic.EXT_FMT_ADHOC_DYN_FIELD, AdhocPublic.EXT_FMT_CALCULATE_COLUMN,
				AdhocPublic.EXT_FMT_DRILL_THROUGH_CELL, AdhocPublic.EXT_FMT_FUNCTION_FIELD };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
	 */
	public SheetCellRenderer getDataRender(String extFmtName) {
		if (extFmtName == null)
			return null;
		if (extFmtName.equals(AdhocPublic.EXT_FMT_ADHOC_DYN_FIELD) || // �ֶοɱ���
				extFmtName.equals(AdhocPublic.EXT_FMT_FUNCTION_FIELD)) { // ������

			return new SheetCellRenderer() {
				Color lineColor = Color.RED; // ������ɫ

				int LineBorderWidth = 2;// ������Χ�Ŀհ׿��

				public Component getCellRendererComponent(CellsPane cellsPane,  Object value,  boolean isSelected,
						boolean hasFocus, int row, int column, Cell cell) {
					
					// ��ʼ������ͼ����е�Ԫ��Ļ���
					CellPosition cellPos = CellPosition.getInstance(row, column);
					Object objExtFmt = lnkAdhocModel.getCellsModel().getBsFormat(cellPos,
							AdhocPublic.EXT_FMT_FUNCTION_FIELD);
					if (objExtFmt != null) {
						String strContent = "";
						if (value instanceof IMetaData) {
							strContent = ((IMetaData) value).getName();
							if (value instanceof IBIField)
								strContent = lnkAdhocModel.getFieldLabel((IMetaData) value, ((IBIField) value)
										.getBIFieldType());
						}
						UILabel lblFuncCell = new UILabel(strContent);
						lblFuncCell.setFont(new Font("Dialog", Font.PLAIN, 14));
						return lblFuncCell;
					} else if (lnkAdhocModel.getOperationState() == ContextVO.OPERATION_FORMAT) {
						String strContent = "";
						if (value instanceof IMetaData) {
							strContent = ((IMetaData) value).getName();
							if (value instanceof IBIField)
								strContent = lnkAdhocModel.getFieldLabel((IMetaData) value, ((IBIField) value)
										.getBIFieldType());
						}

						return new AdhocDynFmtRenderer(strContent);
					} else {
						return AdhocPublic.createHorizontalArrows(lineColor, LineBorderWidth);
					}

				}
			};
		}
		return null;
	}

	/**
	 * ����Adhoc ϸ������ʽ״̬����Ⱦ chxiaowei 2006-01-25
	 */
	class AdhocDynFmtRenderer extends UILabel {
		private static final long serialVersionUID = -3580876870736741297L;

		private int lineBorderWidth = 2; // ˮƽ����ֱ�߿��

		private Color lineColor = Color.RED; // ������ɫ

		public AdhocDynFmtRenderer(java.lang.String p0) {
			super(p0);
			super.setFont(new Font("Dialog", Font.PLAIN, 14));
		}

		/**
		 * Adhoc ϸ������ʽ״̬����������ʱ����Ҫ���ƹ�ʽ���ݣ���Ҫ���ƹ�ʽ��ʾ ͼ��(��ɫˮƽ����)
		 */
		public void paintComponent(Graphics g) {
			// ����ϸ������ʽ�ı����ƻ���
			int width = this.getWidth();
			int height = this.getHeight();
			int fontSize = DefaultFormatValue.FONT_SIZE;
			int fontStyle = DefaultFormatValue.FONT_STYLE;
			String fontName = DefaultFormatValue.FONTNAME;

			Font font = FontFactory.createFont(fontName, fontStyle, fontSize);
			this.setFont(font);

			Color foreGround = Color.BLACK;
			this.setForeground(foreGround);

			this.setHorizontalAlignment(TableConstant.HOR_LEFT);
			this.setVerticalAlignment(DefaultFormatValue.VALIGN);

			// ����ϸ������ʽ���ƻ���
			Graphics2D g2d = (Graphics2D) g;
			Point2D startPoint = null;// ������ʼ��
			Point2D endPoint = null;// �����յ�

			Paint oriPaint = g2d.getPaint();
			g2d.setPaint(lineColor);

			if (width < 2 * lineBorderWidth) {// ����Ŀ�Ⱥ�С,ֻ�ܻ�һ����,���ұ��뻭һ����
				startPoint = new Point2D.Double(0, (int) (height / 2));
				endPoint = new Point2D.Double(width, (int) (height / 2));
				drawLine(g2d, startPoint, endPoint);
			} else {
				int lineCount = 0;
				while ((2 * lineCount + 1) * lineBorderWidth < height) {
					startPoint = new Point2D.Double(0, (2 * lineCount + 1) * lineBorderWidth);
					endPoint = new Point2D.Double(width, (2 * lineCount + 1) * lineBorderWidth);
					drawLine(g2d, startPoint, endPoint);
					lineCount++;
				}
			}

			g2d.setPaint(oriPaint);
			super.paintComponent(g);
		}

		/**
		 * ����ֱ��
		 * 
		 * @param g2d
		 * @param fromPoint
		 * @param toPoint
		 */
		private void drawLine(Graphics2D g2d, Point2D fromPoint, Point2D toPoint) {
			AffineTransform originTransform = g2d.getTransform();
			g2d.translate(toPoint.getX(), toPoint.getY());
			g2d.rotate((fromPoint.getY() > toPoint.getY() ? -1 : 1)
					* Math.acos((toPoint.getX() - fromPoint.getX()) / fromPoint.distance(toPoint)));
			g2d.setTransform(originTransform);
			g2d.draw(new Line2D.Double(fromPoint, toPoint));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
	 */
	public SheetCellEditor getDataEditor(String extFmtName) {
		if (extFmtName == null)
			return null;

		if (extFmtName.equals(AdhocPublic.EXT_FMT_FUNCTION_FIELD)) {
			return new FunctionFieldEditor();
		}
		if (extFmtName.equals(AdhocPublic.EXT_FMT_CALCULATE_COLUMN)) {
			return new CalcColumnEditor();
		}
		// if(extFmtName.equals(AdhocPublic.EXT_FMT_CALCULATE_COLUMN)) {
		// return new drilllThroughAction();
		// }

		return null;
	}

	/**
	 * ������ʱ�ı༭��
	 */
	private class FunctionFieldEditor extends DefaultSheetCellEditor {
		private FunctionFieldEditor() {
			super(new nc.ui.pub.beans.UILabel());
		}

		public Component getTableCellEditorComponent(CellsPane table, Object value, boolean isSelected, int row,
				int column) {
			FunctionFieldExt ext = new FunctionFieldExt(AdhocPlugin.this);
			ext.getParams(null);
			return null;
		}

		public int getEditorPRI() {
			return 2;
		}

		public boolean isCellEditable(EventObject anEvent) {
			if (super.isCellEditable(anEvent) && anEvent != null && anEvent instanceof MouseEvent) {
				return true;
			}
			return false;
		}

		public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
			return getModel().getOperationState() == UfoReport.OPERATION_FORMAT;
		}
	}

	/**
	 * ������ʱ�ı༭��
	 */
	private class CalcColumnEditor extends DefaultSheetCellEditor {
		private CalcColumnEditor() {
			super(new nc.ui.pub.beans.UILabel());
		}

		public Component getTableCellEditorComponent(CellsPane table, Object value, boolean isSelected, int row,
				int column) {
			CalcColumnExt ext = new CalcColumnExt(AdhocPlugin.this);
			ext.getParams(null);
			return null;
		}

		public int getEditorPRI() {
			return 2;
		}

		public boolean isCellEditable(EventObject anEvent) {
			if (super.isCellEditable(anEvent) && anEvent != null && anEvent instanceof MouseEvent) {
				return true;
			}
			return false;
		}

		public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
			return getModel().getOperationState() == UfoReport.OPERATION_FORMAT;
		}
	}

	// /**
	// * ������ʱ�ı༭��
	// */
	// private class drilllThroughAction extends DefaultSheetCellEditor {
	// private drilllThroughAction() {
	// super(new nc.ui.pub.beans.UILabel());
	// }
	// public Component getTableCellEditorComponent(CellsPane table, Object
	// value,
	// boolean isSelected, int row, int column) {
	// Cell cell = table.getCell(row, column);
	// if((cell != null) &&
	// (cell.getExtFmt(AdhocPublic.EXT_FMT_DRILL_THROUGH_CELL) != null)) {
	// drillThrough(AdhocPlugin.this, (HashMap)
	// cell.getExtFmt(AdhocPublic.EXT_FMT_DRILL_THROUGH_CELL));
	// }
	//        	
	// return null;
	// }
	// public int getEditorPRI() {
	// return 1;
	// }
	// public boolean isCellEditable(EventObject anEvent){
	// return true;
	// }
	// public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
	// return getModel().getOperationState() == UfoReport.OPERATION_INPUT;
	// }
	// }
	//	
	/**
	 * @i18n uibiadhoc00005=Ŀ�걨��û�ж����ʽ
	 */
	protected static void drillThrough(UfoReport report, BaseReportModel newModel, int targetOperationState,
			BIRepParams repParams) {
		if (newModel == null) {
			JOptionPane.showMessageDialog(report, StringResource.getStringResource("uibiadhoc00005"), "",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			newModel.filterByParams(repParams);
			BIReportApplet.drillThrough(newModel, report, targetOperationState);
		} catch (BIException ex) {
			AppDebug.debug(ex);
			JOptionPane.showMessageDialog(report, ex.getMessage());
		}
	}

	public void actionPerform(UserUIEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		if (e instanceof UserUIEvent) {
			UserUIEvent ee = (UserUIEvent) e;
			if (ee.getEventType() == UserUIEvent.COMBINECELL) {
				return isSupportCombineCell(ee);
			}
		}
		return null;
	}

	/**
	 * @i18n uibiadhoc00006=���ܿ��������
	 * @i18n uibiadhoc00007=ѡ�������ڴ��ڶ�����ݵ�Ԫ���������
	 */
	private String isSupportCombineCell(UserUIEvent event) throws ForbidedOprException {
		AreaPosition ccArea = (AreaPosition) event.getOldValue();
		ArrayList areaList = getModel().getDataCenter().getAreaList();
		AdhocArea firstArea = null;
		for (int i = 0; i < areaList.size(); i++) {
			AdhocArea area = (AdhocArea) areaList.get(i);
			for (int j = 0; j < area.getHeight(); j++) {
				if (ccArea.contain(j + area.getStartRow(), ccArea.getStart().getColumn())) {
					if (firstArea == null) {
						firstArea = area;
					}
					if (firstArea.equals(area) == false) {
						throw new ForbidedOprException(StringResource.getStringResource("uibiadhoc00006"));
					}
				}
			}
		}
		ArrayList<CellPosition> cellList = getCellsModel().getSeperateCellPos(ccArea);
		Iterator it = cellList.iterator();
		boolean isExistField = false;
		while (it.hasNext()) {
			CellPosition pos = (CellPosition) it.next();
			if (getCellsModel().getBsFormat(pos, AdhocPublic.EXT_FMT_SELECTED_FIELD) != null
					|| getCellsModel().getBsFormat(pos, AdhocPublic.EXT_FMT_CALCULATE_COLUMN) != null
					|| getCellsModel().getBsFormat(pos, AdhocPublic.EXT_FMT_FUNCTION_FIELD) != null) {
				if (isExistField) {
					throw new ForbidedOprException(StringResource.getStringResource("uibiadhoc00007"));
				} else {
					isExistField = true;
				}
			}
		}
		return null;
	}

	protected IPluginDescriptor createDescriptor() {
		return new AdhocDescriptor(this);
	};

	public AdhocModel getModel() {
		return this.lnkAdhocModel;
	}

	public void showErrorMessage(String error) {
		JOptionPane.showMessageDialog(getReport(), error, "Adhoc Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * ��ȡ����ǰ�Ĳ���״̬
	 * 
	 * @return
	 */
	public int getOperationState() {
		return this.getModel().getOperationState();
		// return UfoReport.OPERATION_FORMAT;
	}

	public void setOperationState(int operationState) {
		FreeQueryContextVO context = (FreeQueryContextVO) getReport().getContextVo();
		String strRepId = context.getAttribute(REPORT_PK) == null ? null : (String)context.getAttribute(REPORT_PK);
		
		this.getModel().setOperationState(operationState, strRepId, context.getCurUserID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt == null)
			return;
		if (evt.getSource() == this.lnkAdhocModel) {
			if (evt.getPropertyName().equals("areaChanged")) {// �ı�����֪ͨ�������»���
				this.getReport().getTable().getCells().invalidate();
				this.getReport().getTable().getCells().validate();
				this.getReport().getTable().getCells().repaint();
			}
		}

	}

	public void userActionPerformed(UserUIEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * ��鶨�������ͼ����еĵ�Ԫ�������Ԫ���ֵ���Ա����˹�ʽ��Ϣ�����ÿ�; (�����ϸ��汾Adhoc����ģ��)
	 */
	private void clearOldFmtValue() {
		Hashtable<CellPosition, Object> htExtFmtDyn = lnkAdhocModel.getCellsModel().getBsFormats(
				AdhocPublic.EXT_FMT_ADHOC_DYN_FIELD);
		Enumeration enumeration = htExtFmtDyn.keys();
		CellPosition cellPos = null;
		Cell cell = null;
		while (enumeration.hasMoreElements()) {
			cellPos = (CellPosition) enumeration.nextElement();
			cell = lnkAdhocModel.getCellsModel().getCell(cellPos);
			if (cell.getValue() != null) {
				cell.setValue(null);
			}
		}

		Hashtable<CellPosition, Object> htExtFmtFunc = lnkAdhocModel.getCellsModel().getBsFormats(
				AdhocPublic.EXT_FMT_FUNCTION_FIELD);
		enumeration = htExtFmtFunc.keys();
		cellPos = null;
		cell = null;
		while (enumeration.hasMoreElements()) {
			cellPos = (CellPosition) enumeration.nextElement();
			cell = lnkAdhocModel.getCellsModel().getCell(cellPos);
			if (cell.getValue() != null) {
				cell.setValue(null);
			}
		}
	}
}
