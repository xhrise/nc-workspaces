package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SizeSequence;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.style.Style;
import nc.vo.iufo.data.MeasureTraceVO;

import com.ufida.dataset.IContext;
import com.ufida.dataset.Provider;
import com.ufida.dataset.tracedata.ITraceDataResult;
import com.ufida.dataset.tracedata.TraceDataOperator;
import com.ufida.dataset.tracedata.TraceDataParam;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iufo.inputplugin.biz.data.TraceDataCmd;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedData;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedCalInfo;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedTraceInfo;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.script.extfunc.MeasFuncDriver;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

/**
 * ��ʽ׷�ٵ�������UI���
 * 
 * @author liulp
 * 
 */
public class FormulaTraceNavPanel extends UIPanel implements MouseListener, IUfoContextKey {
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPaneContent = null;
	private JPanel m_jContentPanel = null;

	private JTextArea uiTAreaFormulaDisContent = null;
	private JScrollPane uiScrollPane = null;
	private UITable uiParsedItemDataTable = null;

	private UfoReport m_ufoReport = null;
	/**
	 * ��ʽ����������
	 */
	private FormulaParsedData m_oFormulaParsedData = null;
	private int m_nCurSelectedRow;
	private IArea[] m_curTracedPos = null;
	private IArea[] m_haveTracedPos = null;
	private int m_nTracedTarget = TRACE_TARGET_NO;
	private int m_nHaveTracedTarget = TRACE_TARGET_NO;
	private static int TRACE_TARGET_NO = -1;
	private static int TRACE_TARGET_SINGLE = 0;
	private static int TRACE_TARGET_ALL = 1;
	private FormulaParsedTableModel m_oFormulaParsedTableModel = null;

	/**
	 * �����µĹ�ʽ׷�ٵ�����λ��,�����»���
	 * 
	 * @param curTracedPos
	 */
	public void setCurTracedPos(IArea[] curTracedPos) {
		setCurTracedPos(curTracedPos, true);
	}

	/**
	 * �����µĹ�ʽ׷�ٵ�����λ��
	 * 
	 * @param curTracedPos
	 * @param isPaint
	 *            �Ƿ������ػ�,ͨ����ͬһ�������anchor_changed��ʱ�������µĹ�ʽ׷��ʱ����
	 */
	public void setCurTracedPos(IArea[] curTracedPos, boolean isPaint) {

		IArea[] oldTracedPos = this.m_curTracedPos;
		this.m_curTracedPos = curTracedPos;
		
		if (isPaint) {
			refreshUfoTable(oldTracedPos);
			
		}
		m_haveTracedPos = m_curTracedPos;
	}

	/**
	 * This method initializes
	 * 
	 */
	public FormulaTraceNavPanel(UfoReport ufoReport) {
		super();
		m_ufoReport = ufoReport;
		initialize();
	}

	public UfoReport getReport() {
		return m_ufoReport;
	}

	public IArea[] getCurTracedPos() {
		return m_curTracedPos;
	}

	public int getCurSelectedRow() {
		return m_nCurSelectedRow;
	}

	public FormulaParsedData getFormulaParsedData() {
		return this.m_oFormulaParsedData;
	}

	public void clear() {
		setFormulaParedData(null);
	}
    /**
     * 
     * @param oFormulaParsedData
     * modify by guogang 2008-1-24
     */
	public void setFormulaParedData(FormulaParsedData oFormulaParsedData) {
		//�����µ�׷������ǰ������͵ĸ���
		m_nHaveTracedTarget = TRACE_TARGET_NO;
		this.setCurTracedPos(null);
		
		if (oFormulaParsedData == null) {
			getJTAreaFormulaDisContent().setText("");
			m_oFormulaParsedTableModel.resetModel(new FormulaParsedDataItem[0]);
			return;
		}
		m_oFormulaParsedData = oFormulaParsedData;
		
		// ���ù�ʽ��������
		String strDisContent = oFormulaParsedData.getTracePos().toString()
				+ "=" + oFormulaParsedData.getFormulaDisContent();
		getJTAreaFormulaDisContent().setText(strDisContent);
		// ���ù�ʽ���������������
		
		m_oFormulaParsedTableModel.resetModel(oFormulaParsedData
				.getFormulaParedDataItems());
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getJScrollPaneContent(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneContent() {
		if (jScrollPaneContent == null) {
			jScrollPaneContent = new UIScrollPane();
			jScrollPaneContent.setPreferredSize(new Dimension(488, 86));
			jScrollPaneContent.setViewportView(getJContentPanel());
		}
		return jScrollPaneContent;
	}

	private JPanel getJContentPanel() {
		if (m_jContentPanel == null) {
			m_jContentPanel = new UIPanel();
			m_jContentPanel.setLayout(new BorderLayout());
			m_jContentPanel.add(getJTAreaFormulaDisContent(),
					BorderLayout.CENTER);
			m_jContentPanel.add(getJScrollPane(), BorderLayout.SOUTH);
		}
		return m_jContentPanel;
	}

	/**
	 * This method initializes uiTextArea
	 * 
	 * @return UITextArea
	 */
	private JTextArea getJTAreaFormulaDisContent() {
		if (uiTAreaFormulaDisContent == null) {
			uiTAreaFormulaDisContent = new UITextArea();
			uiTAreaFormulaDisContent.setLineWrap(true);
			uiTAreaFormulaDisContent.setWrapStyleWord(true);

			uiTAreaFormulaDisContent.addMouseListener(this);
		}
		return uiTAreaFormulaDisContent;
	}

	/**
	 * This method initializes UIScrollPane
	 * 
	 * @return UIScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (uiScrollPane == null) {
			uiScrollPane = new UIScrollPane();
			uiScrollPane.setViewportView(getJTableParsedItemData());
		}
		return uiScrollPane;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTableParsedItemData() {
		if (uiParsedItemDataTable == null) {

			uiParsedItemDataTable = new nc.ui.pub.beans.UITable();
			uiParsedItemDataTable.setAutoCreateColumnsFromModel(false);
			initParsedItemDataTable();

			// ������
			String[] colStrs = FormulaParsedTableModel.S_HEADS;
			int[] nWidths = new int[] { 390, 98, 80 };

			for (int k = 0; k < colStrs.length; k++) {
				TableCellEditor editor = null;// new DefaultCellEditor(new
				// JTextField());
				TableColumn column = null;
				if (k == FormulaParsedTableModel.COL_CALCULATE) {
					TableCellRenderer renderer = new TableCellCalRenderer();
					column = new TableColumn(k, nWidths[k], renderer, editor);
				} else if (k == FormulaParsedTableModel.COL_TRACE) {
					TableCellRenderer renderer = new TableCellTraceRenderer();
					column = new TableColumn(k, nWidths[k], renderer, editor);
				} else {
					FlowQryTableCellRenderer render = new FlowQryTableCellRenderer();
					column = new TableColumn(k, nWidths[k], render, editor);
				}
				column.setResizable(true);
				uiParsedItemDataTable.addColumn(column);
			}
			uiParsedItemDataTable.setColumnWidth(nWidths);
			uiParsedItemDataTable.addMouseListener(this);
		}
		return uiParsedItemDataTable;
	}

	/**
	 * ��ʼ����ʽ������Ĺ�ʽ�����ģ�͡�
	 * 
	 * �������ڣ�(2003-9-16 13:51:17)
	 * 
	 * @author������Ƽ
	 */
	private void initParsedItemDataTable() {
		m_oFormulaParsedTableModel = new FormulaParsedTableModel(null);
		uiParsedItemDataTable.setModel(m_oFormulaParsedTableModel);
		// ���ñ�����
		uiParsedItemDataTable.setCellSelectionEnabled(true);
	}

	private void refreshUfoTable(IArea[] oldTracedPos) {
		if (m_curTracedPos != null && m_curTracedPos.length > 0) {
			for(int index=0;index< m_curTracedPos.length;index++){
				FormulaTraceBizUtil.setView2HighlightArea(m_ufoReport.getTable(),
						m_curTracedPos[index],index==0);
			}
			
		}
		if(oldTracedPos != null && oldTracedPos.length > 0){
			for(int i = 0,nOldCount = oldTracedPos.length;i < nOldCount;i++){
				FormulaTraceBizUtil.repaintHighlightArea(m_ufoReport.getTable(), oldTracedPos[i]);
			}
		}
	}

	/**
	 * @i18n uiuforep00110=û�п�׷�����ݡ�
	 * @i18n uiuforep00111=��������
	 */
	public void mouseClicked(MouseEvent e) {
		m_curTracedPos = null;
		m_nTracedTarget = TRACE_TARGET_NO;
		if(m_oFormulaParsedData==null){
			return ;
		}
		if (e.getSource() == uiParsedItemDataTable) {
			int nSelectedRow = uiParsedItemDataTable.getSelectedRow();
			m_nCurSelectedRow = nSelectedRow;
			int nSelectedCol = uiParsedItemDataTable.getSelectedColumn();
			if (nSelectedCol == FormulaParsedTableModel.COL_TRACE) {
				IFormulaParsedTraceInfo formulaParsedTraceInfo = (IFormulaParsedTraceInfo) uiParsedItemDataTable
						.getValueAt(nSelectedRow, nSelectedCol);
				if (formulaParsedTraceInfo.isCanTrace()) {
					if (formulaParsedTraceInfo.isTraceSelf()) {
						System.out.println("----��ʼ׷��----");
						IArea[] tracedPos = formulaParsedTraceInfo
								.getTracedPos();
						// ���ø���׷������
						m_nTracedTarget = TRACE_TARGET_SINGLE;
						m_curTracedPos = tracedPos;
						
					} else {
						try {
							// trace single value to other task-report
							IFormulaParsedDataItem formulaParsedDataItem = m_oFormulaParsedData
									.getFormulaParedDataItems()[nSelectedRow];
							
							if (formulaParsedDataItem.getTraceDataParam() != null) {// �����ݼ�����
								TraceDataParam dataParam = formulaParsedDataItem.getTraceDataParam();
								
								JApplet applet=InputBizOper.getReportApplet(this.getReport());
								if (applet!=null){
									dataParam.addParam("SCHEME", applet.getParameter("SCHEME"));
									dataParam.addParam("SERVER_PORT",applet.getParameter("SERVER_PORT"));
									dataParam.addParam("localCode",applet.getParameter("localCode"));
								}
								
									// @edit by ll at 2008-12-25,����10:48:31 ���ݼ�������������ʱ����Ҫ����������Ϣ
								formulaParsedDataItem.setIsInTraceNow(true);
								IFormulaTraceBizLink formulaTraceBizLink = FormulaTraceBizHelper.getIFormulaTraceBizLink();
								formulaTraceBizLink.getCalulatedValue(m_ufoReport, formulaParsedDataItem);
								formulaParsedDataItem = m_oFormulaParsedData.getFormulaParedDataItems()[nSelectedRow];
								formulaParsedDataItem.setIsInTraceNow(false);
								dataParam = formulaParsedDataItem.getTraceDataParam();
								try {
									Provider provider=dataParam.getDataSet().getProvider();
//									if(provider.getContext() instanceof UfoCalcEnvContext){
//										UfoCalcEnvContext providerContext=(UfoCalcEnvContext)provider.getContext();
//										if(m_ufoReport.getContextVo() instanceof TableInputContextVO){
//											TableInputContextVO reportContext=(TableInputContextVO)m_ufoReport.getContextVo();
//											Object tableInputTransObj = reportContext.getAttribute(TABLE_INPUT_TRANS_OBJ);
//											TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
//											providerContext.getUfoCalcEnv().setTaskPK(inputTransObj.getRepDataParam().getTaskPK());
//											providerContext.setAttribute(IUfoContextKey.CUR_USER_ID, reportContext.getAttribute(IUfoContextKey.CUR_USER_ID));
//											
//										}
//									}
									provider.setContext(m_ufoReport.getContextVo());
									ITraceDataResult result = provider.traceData(dataParam);
									if (result == null) {
										UfoPublic.sendWarningMessage(MultiLang
												.getString("uiuforep00110"),
												m_ufoReport);
										return;
									}
									String operName = result
											.getOperatorClzName();
									Class operClz = Class.forName(operName);
									Constructor<? extends TraceDataOperator> constructor = operClz
											.getConstructor(new Class[] {});
									TraceDataOperator oper = constructor
											.newInstance(new Object[] {});
									oper.trace(m_ufoReport, result);

								} catch (Throwable ee) {
									AppDebug.debug(ee);
									UfoPublic.showErrorDialog(m_ufoReport, ee
											.getMessage(), "");
								}
								
							}else if (formulaParsedDataItem.isNCFunc()) {// �����ҵ����(���ʻ�HR����)�����������ӿ�
								// ����weixl������ӿ�
								try {
									// @edit by wangyga at 2009-3-3,����02:12:12 �����쳣��Ϣ
									TraceDataCmd.doTraceOneNCFormula(
											formulaParsedDataItem.getNCFuncStr(),
											m_ufoReport, formulaParsedDataItem
													.getRelaCell());
								} catch (Throwable e2) {								
									AppDebug.debug(e2);
									UfoPublic.showErrorDialog(m_ufoReport, e2
											.getMessage(), "");
								}

							} else {
							//ָ��׷��
								measureTrace(formulaParsedDataItem);
								
							}
								
							return;
								
						} catch (Throwable e1) {
							AppDebug.debug(e1);
							UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00111")
									+ e1.getMessage(), m_ufoReport);
						}

					}
				}

			} else if (nSelectedCol == FormulaParsedTableModel.COL_CALCULATE) {
				IFormulaParsedCalInfo formulaParsedCalInfo = (IFormulaParsedCalInfo) uiParsedItemDataTable
						.getValueAt(nSelectedRow, nSelectedCol);
				if (formulaParsedCalInfo.isNeedToCal()) {
					IFormulaParsedDataItem formulaParsedDataItem = m_oFormulaParsedData
							.getFormulaParedDataItems()[nSelectedRow];
					IFormulaTraceBizLink formulaTraceBizLink = FormulaTraceBizHelper
							.getIFormulaTraceBizLink();
					formulaTraceBizLink.getCalulatedValue(m_ufoReport,
							formulaParsedDataItem);
				}
			}
		} else if (e.getSource() == getJTAreaFormulaDisContent()) {
			m_nTracedTarget = TRACE_TARGET_ALL;
			FormulaParsedDataItem[] formulaParedDataItems = m_oFormulaParsedData
					.getFormulaParedDataItems();

			List<IArea> listTracedSelfPos = new ArrayList<IArea>();
			if (formulaParedDataItems != null
					&& formulaParedDataItems.length > 0) {
				for (int i = 0, nItemCount = formulaParedDataItems.length; i < nItemCount; i++) {
					if (formulaParedDataItems[i].isTraceSelf()) {
						IArea[] tracedPos = formulaParedDataItems[i]
								.getTracedPos();
						if (tracedPos != null) {
							for (int j = 0; j < tracedPos.length; j++) {
								listTracedSelfPos.add(tracedPos[j]);
							}
						}
					}
				}
				if (listTracedSelfPos.size() > 0) {
					m_curTracedPos = new IArea[listTracedSelfPos.size()];
					listTracedSelfPos.toArray(m_curTracedPos);
				}
			} else {
				m_curTracedPos = null;
			}
		}

		// ���ݸ������������Ƿ�ı䣬���н���ˢ����ʾ
		boolean bChangedHihgLight = false;
		if (m_nHaveTracedTarget != m_nTracedTarget) {
			// ׷�ٵĶ���һ�����϶�������ʾ��һ��
			bChangedHihgLight = true;
		} else {
			// ׷�ٵĶ���һ��ʱ��ֻ�е�ֵ׷�ٲſ��ܱ仯��������
			if (m_nTracedTarget == TRACE_TARGET_SINGLE) {
				if (m_haveTracedPos != null && (m_curTracedPos.length != m_haveTracedPos.length
						||(m_haveTracedPos[0] != null
						&& m_haveTracedPos.length == m_curTracedPos.length
						&& !m_haveTracedPos[0].equals(m_curTracedPos[0])))) {
					bChangedHihgLight = true;
				}
			}
		}
		
		if (bChangedHihgLight) {
			refreshUfoTable(m_haveTracedPos);
			m_haveTracedPos = m_curTracedPos;
			m_nHaveTracedTarget = m_nTracedTarget;
		}


	}
		
	/**
	 * ����ָ��׷��
	 * @param formulaParsedDataItem
	 */
		private void measureTrace(IFormulaParsedDataItem formulaParsedDataItem){

			CellsModel cellsModel = m_ufoReport.getCellsModel();
			MeasureTraceVO[] tracevos = new MeasureTraceProcessor()
					.trace(m_ufoReport.getContextVo(),
							cellsModel,
							formulaParsedDataItem,
							m_oFormulaParsedData
									.getTracePos());
			if (tracevos == null || tracevos.length < 1 ||
					(tracevos.length ==1 && tracevos[0]==null)) {
				UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00110"), m_ufoReport);
				return;
			}
			
			UfoExpr expr  = formulaParsedDataItem.getTracedExpr();
			String funcName = MeasureTraceProcessor.getMeasFuncName(expr);
			
			MeasureTraceVO tracevo = null;
			if (tracevos.length == 1) {
				tracevo = tracevos[0];
			} else {
			
				int index = formulaParsedDataItem.getRelaCellInAreaFml();
				
				//mselecta, msels׷��ʱһ����һ����
				if(MeasFuncDriver.MSELECTA.equals(funcName) || MeasFuncDriver.MSELECTS.equals(funcName)){
					// @edit by wangyga at 2009-1-9,����12:31:38
                    if(!formulaParsedDataItem.isAreaFunc() && index == -1){//��Ԫ��ʽ���˶��ָ�꣬�������ֻȡ��һ��
                    	index = 0;
                    }
					if(tracevos.length <= index || tracevos[index] == null){
						UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00110"), m_ufoReport);
						return;
					}
					tracevo = tracevos[index];
					tracevos = new MeasureTraceVO[]{tracevo};
				}
				
				// msuma�� mselecta������ʽ�������˷�׷��ָ�ꡣ
				if (formulaParsedDataItem.isAreaFunc() && formulaParsedDataItem.getRelaCell() != null) {
					ArrayList<String> mpkList = new ArrayList<String>();
					for (int i = 0; i < tracevos.length; i++) {
						String mpk = (tracevos[i]!=null)?tracevos[i].getMeasurePK():null;
						if (mpk!=null && !mpkList.contains(mpk)) {
							mpkList.add(mpk);
						}  
					}

					if (mpkList.size() > index) {
						String mpk = mpkList.get(index);
						ArrayList<MeasureTraceVO> list = new ArrayList<MeasureTraceVO>();
						for (int i = 0; i < tracevos.length; i++) {
							if (tracevos[i]!=null &&tracevos[i].getMeasurePK()
									.equals(mpk)) {
								list.add(tracevos[i]);
							}
						}

						tracevos = list.toArray(new MeasureTraceVO[list.size()]);
					}
				}
				
				//�ų�null
				ArrayList<MeasureTraceVO> list = new ArrayList<MeasureTraceVO>();
				for (int i = 0; i < tracevos.length; i++) {
					if (tracevos[i] != null) {
						list.add(tracevos[i]);
					}
				}
				tracevos = list
						.toArray(new MeasureTraceVO[list
								.size()]);
				
				if (tracevos == null || tracevos.length < 1) {
					UfoPublic.sendWarningMessage(
							MultiLang
									.getString("uiuforep00110"), m_ufoReport);
					return;
				}
				

				if (tracevos.length == 1) {
					tracevo = tracevos[0];
				} else {
					MeasureTraceSelectDlg dlg = new MeasureTraceSelectDlg(
							m_ufoReport, tracevos);
					dlg.show();
					if (dlg.getResult() == UfoDialog.ID_OK) {
						tracevo = dlg
								.getSelectedValueItem();
					}
				}

			}

			if (tracevo == null) {
				return;
			}

			MeasTraceInfo traceInfo = new MeasTraceInfo(
					tracevo.getMeasurePK(), tracevo
							.getKeyvalues(), tracevo
							.getReportpk(), tracevo
							.getAloneID(), true);
			 
				IContext context = m_ufoReport
						.getContextVo();
		 
			if (context != null) {
				String strOrgPK = context.getAttribute(ORG_PK) == null ? null : (String)context.getAttribute(ORG_PK);
				String unitId = context.getAttribute(LOGIN_UNIT_ID) == null ? null : (String)context.getAttribute(LOGIN_UNIT_ID);
				Object showRepTreeObj = context.getAttribute(SHOW_REP_TREE);
				boolean isShowRepTree = showRepTreeObj == null ? false : Boolean.parseBoolean(showRepTreeObj.toString());
				
				Object genralQueryObj = context.getAttribute(GENRAL_QUERY);
				boolean isgenralQuery = genralQueryObj == null ? false : Boolean.parseBoolean(genralQueryObj.toString());
				
				traceInfo.setGeneryQuery(isgenralQuery);
				traceInfo.setShowRepTree(isShowRepTree);
				traceInfo.setStrLoginUnitID(unitId);
				traceInfo.setStrOrgPK(strOrgPK);
				
				Object tableInputTransObj = context.getAttribute(TABLE_INPUT_TRANS_OBJ);
				TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
			
				if (inputTransObj != null) {
					traceInfo.setStrLangCode(inputTransObj
							.getLangCode());
					if (inputTransObj
							.getRepDataParam() != null) {
						traceInfo
								.setStrOperUserPK(inputTransObj
										.getRepDataParam()
										.getOperUserPK());
						traceInfo
								.setStrTaskId(inputTransObj
										.getRepDataParam()
										.getTaskPK());
						traceInfo
								.setDataSource(inputTransObj
										.getRepDataParam()
										.getDSInfo());
					}
				}
			}
			WindowNavUtil.traceMeasure(traceInfo);
		 
		}

	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == uiParsedItemDataTable) {
			Point point = uiParsedItemDataTable.getMousePosition();
			if (point == null) {
				return;
			}
			int nCurColumn = uiParsedItemDataTable.columnAtPoint(point);
			if (nCurColumn == FormulaParsedTableModel.COL_CONTENT) {
				return;
			}
			int nCurRow = uiParsedItemDataTable.rowAtPoint(point);
			if (nCurColumn == FormulaParsedTableModel.COL_TRACE) {
				// �κι�ʽ�������׷�ٲ��������Թ����ʾ
				this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			} else if (nCurColumn == FormulaParsedTableModel.COL_CALCULATE) {
				IFormulaParsedCalInfo formulaParsedCalInfo = (IFormulaParsedCalInfo) uiParsedItemDataTable
						.getValueAt(nCurRow, nCurColumn);
				if (formulaParsedCalInfo.isNeedToCal()) {
					// ������Ĺ�ʽ����ɲ��������Թ����ʾ
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				return;
			}
		}

	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	private static JLabel jLableCalUnderLine = new UnderLineLabel();
	private JLabel jLableValue = new UILabel();
	{
		jLableCalUnderLine.setText(FormulaParsedDataItem.ID_STR_CALULATE);
		jLableCalUnderLine.setForeground(Color.RED);
		jLableCalUnderLine.setHorizontalAlignment(SwingConstants.CENTER);

		jLableValue.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private class TableCellCalRenderer implements TableCellRenderer,
			Serializable {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			IFormulaParsedCalInfo formulaParsedCalInfo = (IFormulaParsedCalInfo) table
					.getValueAt(row, column);
			if (formulaParsedCalInfo!=null&&formulaParsedCalInfo.isNeedToCal()) {
				// ĿǰUNDERLINE����������֧�֣�Ҳ���������·�ʽ�ɹ�����
				// String fontName = DefaultFormatValue.FONTNAME;
				// int fontSize = DefaultFormatValue.FONT_SIZE;
				// int fontStyle =
				// TableConstant.FS_UNDERLINE;//Font.HANGING_BASELINE;
				// fontSize = (int) (fontSize * TablePane.getViewScale());
				// Map<TextAttribute, Object> attributes = new
				// HashMap<TextAttribute, Object>();
				// attributes.put(UNDERLINE, UNDERLINE_LOW_ONE_PIXEL);
				// Font font = new
				// Font(attributes);//FontFactory.createFont(fontName,
				// fontStyle, fontSize);
				return jLableCalUnderLine;
			} else {
				Object fmlValue = "";
				if(formulaParsedCalInfo!=null){
					fmlValue=formulaParsedCalInfo.getFormulaValue();
				}
					
				if(fmlValue != null && fmlValue instanceof Double){
					jLableValue.setText(IufoFormat.doGetString(((Double)fmlValue).doubleValue()) + "");
				} else{
					jLableValue.setText(fmlValue + "");
				}
				return jLableValue;
			}
		}
	}

	private static JLabel jLableTraceNo = new UILabel();
	private static JLabel jLableTraceOther = new UnderLineLabel();
	private JLabel jLableTraceSelf = new UnderLineLabel();
	{
		jLableTraceNo.setText(" ");
		jLableTraceOther.setText(FormulaParsedDataItem.ID_STR_TRACESELF);
		jLableTraceOther.setForeground(Color.BLUE);
		jLableTraceOther.setHorizontalAlignment(SwingConstants.CENTER);

		jLableTraceSelf.setForeground(Color.BLUE);
		jLableTraceSelf.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private class TableCellTraceRenderer implements TableCellRenderer,
			Serializable {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			IFormulaParsedTraceInfo formulaParsedTraceInfo = (IFormulaParsedTraceInfo) table
					.getValueAt(row, column);
			if (formulaParsedTraceInfo!=null&&formulaParsedTraceInfo.isCanTrace()) {
				if (formulaParsedTraceInfo.isTraceSelf()) {
					String strCurText = FormulaParsedDataItem.ID_STR_TRACESELF;
					if (formulaParsedTraceInfo.getTracedPos() != null) {
						strCurText = formulaParsedTraceInfo.toString();
					}
					jLableTraceSelf.setText(strCurText);
					return jLableTraceSelf;
				} else {
					return jLableTraceOther;
				}
			}
			return jLableTraceNo;

		}
	}

	/**
	 * ��������ѯ��������Ⱦ��
	 * <li>�ṩ���ı��Զ����й���
	 * 
	 * @author leijun 2007-3-12
	 */
	private class FlowQryTableCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Font tableDefaultFont;

		public FlowQryTableCellRenderer() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
		 *      java.lang.Object, boolean, boolean, int, int)
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);

			// ����������ʾ�ؼ�
			UITextArea ta = new UITextArea();
			ta.setBorder(null);
			ta.setText(value == null ? "" : String.valueOf(value));
			int columnWidth = table.getColumnModel().getColumn(column)
					.getWidth();
			ta.setSize(columnWidth, ta.getHeight());
			ta.setPreferredSize(new Dimension(columnWidth, ta.getHeight()));
			ta.setLineWrap(true);

			// ��ñ���ı�����
			Graphics g = table.getGraphics();
			FontMetrics fontm = g.getFontMetrics(getTableDefaultFont());

			// �������ʾ�ı����
			int strWidth = SwingUtilities.computeStringWidth(fontm, String
					.valueOf(value));
			int rows = strWidth / columnWidth + 1;
			int rowHeight = rows * table.getRowHeight();

			// �ı����и�
			setRowHeightOfTable(table, row, rowHeight);

			if (isSelected)
				ta.setBackground(table.getSelectionBackground());

			return ta;
		}

		/**
		 * �õ���ǰ�����±��Ĭ������
		 * 
		 * @return
		 */
		public Font getTableDefaultFont() {
			if (tableDefaultFont == null) {
				tableDefaultFont = new Font(Style.getFontname(), Font.PLAIN, 12);
			}
			return tableDefaultFont;
		}

		private void setRowHeightOfTable(JTable table, int row, int h) {
			Class cls = JTable.class;
			try {
				Method m = cls.getDeclaredMethod("getRowModel", new Class[] {});
				m.setAccessible(true);
				SizeSequence s = (SizeSequence) m.invoke(table, null);
				s.setSize(row, h);
				table.revalidate();
			} catch (Exception e) {
				// Logger.error(e.getMessage(), e);//
				AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			}
		}
	}
}
  