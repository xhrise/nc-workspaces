package com.ufida.report.anareport.applet;

import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import nc.bs.iufo.datasetmanager.DataSetDirDMO;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.bi.query.manager.RptProvider;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.dsmanager.DatasetTree;
import nc.util.iufo.pub.IDMaker;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.pub.ValueObject;
import nc.vo.pub.dsmanager.exception.DSNotFoundException;

import com.borland.dx.dataset.Variant;
import com.ufida.dataset.DataSet;
import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.dataset.descriptor.FilterDescriptor;
import com.ufida.dataset.descriptor.FilterItem;
import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.edit.AnaFieldFilterRender;
import com.ufida.report.anareport.edit.AnaFieldFixFieldRender;
import com.ufida.report.anareport.edit.AnaFieldOrderRenderer;
import com.ufida.report.anareport.edit.RankFunctionRender;
import com.ufida.report.anareport.edit.TopNRenderer;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufida.report.anareport.model.AnaPropertyType;
import com.ufida.report.anareport.model.AnaRepDataSource;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportCondition;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufida.report.anareport.model.ReportParameter;
import com.ufida.report.free.IRptProviderCreator;
import com.ufida.report.free.RptProviderCreator;
import com.ufida.report.free.applet.FreeQuerySaveDlg;
import com.ufida.report.rep.applet.BIPlugIn;
import com.ufida.report.rep.applet.PageDimNavigationDropTarget;
import com.ufida.report.rep.applet.PageDimNavigationPanel;
import com.ufida.report.rep.model.AnaConstants;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.IBIContextKey;
import com.ufida.report.rep.model.IBIRepParams;
import com.ufida.zior.exception.ForbidedOprException;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.dnd.DndHandler;
import com.ufsoft.report.sysplugin.dnd.IDndAdapter;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * 分析报表插件
 * 
 */
public class AnaReportPlugin extends BIPlugIn implements PropertyChangeListener, IUfoContextKey {
	private AnaReportModel repModel;

	private String pk_report = null;
	private String pk_user = null;
	private boolean m_isFormat = false;// 是否支持格式设计
	private boolean fromReport = false;
	private boolean m_isSetParam=true;//是否设置参数
	
	private PageDimNavigationDropTarget target;
	private AnaDataSetPanel m_queryPanel = null;

	private SelDataSourceDlg m_selDataSourceDlg = null;

	private FreeQueryContextVO m_context = null;
	private DataModelListener m_dataListener = null;
	private AnaFormatModelListener m_formatListener = null;

	// private JFrame m_dataFrame = null;

	static{
		// add by wangyga 2008-8-21 注册全局绘制器
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new AnaFieldOrderRenderer());
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new TopNRenderer());
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new RankFunctionRender());
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new AnaFieldFixFieldRender());
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new AnaFieldFilterRender());
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new AnaCellPaginalRender());
	}
	
	/**
	 * 为报表面板制定了拖拽的鼠标变换
	 */
	public void startup() {
		try {
			
			// 初始化模型
			m_context = (FreeQueryContextVO) getReport().getContextVo();
			Object repIdObj = m_context.getAttribute(REPORT_PK);
			pk_report = repIdObj != null && (repIdObj instanceof String) ? (String) repIdObj : null;

			// pk_report = m_context.getContextId();
			pk_user = m_context.getCurUserID();
			if (m_context instanceof BIContextVO) {// 说明直接从分析报表进来,否则可能是从综合查询进来
				m_context.setTaskID(null);
				if (((BIContextVO) m_context).getBaseReportModel() instanceof AnaReportModel) {
					repModel = (AnaReportModel) ((BIContextVO) m_context).getBaseReportModel();
					m_context.removeAttribute(BIContextVO.REPORTMODEL);
				}
			}

			m_isFormat = (getReport().getOperationState() == UfoReport.OPERATION_FORMAT);
			if (repModel == null) {
				repModel = new AnaReportModel("");
				repModel.setFormatCellsModel(this.getReport().getCellsModel());
				repModel.initCellsModel();
			} else {
				getReport().getTable().setCurCellsModel(repModel.getCellsModel());
				this.setDirty(false);//避免设置模型的过程中引起dirty
			}
			repModel.setContextVO(m_context);
			repModel.addPropertyChangeListener(AnaPropertyType.REPSTATE_CHANGED, this);

		
			Object obj = m_context.getAttribute(FreeQueryContextVO.FREEQEURY_MODEL_OBJ);
			if (obj instanceof IFreeQueryModel) {
				IFreeQueryModel queryDef =(IFreeQueryModel)obj;
				if (queryDef != null) {// 从综合查询而来，直接创建报表内容
					fromReport = true;
					m_isFormat = true;// 并且支持格式设计
					initAnaReport(getReport(), queryDef, m_context, true, true);
					m_context.removeAttribute(FreeQueryContextVO.FREEQEURY_MODEL_OBJ);
				}
			}
			
			if (fromReport || !m_isFormat){
				//如果有报表数据限制条件，则执行之
				IBIRepParams repParams = (IBIRepParams)m_context.getAttribute(IBIContextKey.BIDRILLREPORTPARAM);
				if(repParams != null)
					repModel.filterByParams(repParams);
			}
			// getReport().getTable().addUserActionListener(repModel);
			// // 若尚未选择查询信息，弹出向导
			// if (repModel.getDataCenter().getCurrQuery() == null) {
			// // doSelectQueryModel(context.getCurUserId());
			// }

		} catch (Exception e1) {
			AppDebug.debug(e1);
			return;
		}

		// 设置单元权限模型
		this.getReport().getTable().getCells().setCellsAuthorization(new AnaReportAuth(this));

		// 要保证右键事件最后响应,与状态有关
		this.getReport().resetGlobalPopMenuSupport();
		AnaFormatModelListener anaListener = getFormatListener();
		this.getModel().getCellsModel().getRowHeaderModel().addHeaderModelListener(anaListener);
		this.getModel().getCellsModel().getColumnHeaderModel().addHeaderModelListener(anaListener);
		this.getReport().getTable().addUserActionListener(anaListener);
		
	}

	/**
	 * 是否支持格式设计
	 * 
	 * @return
	 */
	public boolean isAegisFormat() {
		return m_isFormat;
	}
	public boolean isFromQueryReport() {
		return fromReport;
	}
	public boolean isSetParam() {
		return m_isSetParam;
	}

	public void setSetParam(boolean showParam) {
		m_isSetParam = showParam;
	}

	public void shutdown() {
	}

	public void store() {
		anaReportSave();
	}

	/**
	 * @i18n miufo00190=保存分析报表内的私有数据集，保存主键为：
	 * @i18n miufo00191=保存分析报表，保存主键为：
	 * @i18n miufo00192=报表保存成功
	 * @i18n miufotableinput000005=保存失败
	 * @i18n iufobi00090=取消保存
	 */
	public String anaReportSave() {
		getReport().stopCellEditing();

		if (!getModel().getFormatModel().isDirty() && getReportPK() != null)
			return "";
		String pk_report = getReportPK();
		String pk_user = getUserPK();
		ReportSrv srv = new ReportSrv();
		try {
			ReportVO saveReport = null;
			if (pk_report != null) {
				ValueObject[] vos = srv.getByIDs(new String[] { pk_report });
				if (vos != null && vos.length > 0)
					saveReport = (ReportVO) vos[0];
			}
			FreeQueryContextVO fContext = (FreeQueryContextVO) getReport().getContextVo();
			boolean bCreate = false;

			DataSetDefVO[] privateDS = getModel().getDataSource().getPrivateDS();
			boolean hasDS = (privateDS != null && privateDS.length == 1);
			if (saveReport == null) {
				FreeQuerySaveDlg dlg = new FreeQuerySaveDlg(getReport(), fContext.getCurUserID(), fContext.getCurUnitID());
				if (hasDS)
					dlg.setShowDataSet(hasDS, privateDS[0].getCode(), privateDS[0].getName());
				if (dlg.showModal() != UfoDialog.ID_OK)
					return StringResource.getStringResource("iufobi00090");
				saveReport = createNewReportVO(dlg.getReportInfo(), pk_user); // 构建报表基本信息
				pk_report = IDMaker.makeID(20);
				saveReport.setID(pk_report);
				bCreate = true;
				if (hasDS) {
					String[] dsInfo = dlg.getDataSetInfo();
					privateDS[0].setCode(dsInfo[0]);
					privateDS[0].setName(dsInfo[1]);
				}
			}

			if (hasDS) {
				String[] dsPKs = new String[privateDS.length];
				int i = 0;
				for (DataSetDefVO vo : privateDS) {
					vo.setOwner(pk_report);
					vo.setCreator(pk_user);
					try {
						vo = DataSetDefBO_Client.createDataSetDef(vo);
						DatasetTree.clearInstance(); // 清空数据集树缓存 yza+
						// 2008-10-8
					} catch (UFOSrvException ex) {
						AppDebug.debug(ex);
						continue;
					}
					dsPKs[i++] = vo.getPk_datasetdef();
					getModel().getDataSource().removePrivateDS(true, dsPKs);
				}
				AppDebug.debug(StringResource.getStringResource("miufo00190") + dsPKs[0]);
			}
			saveReport.setDefinition(getModel()); // 更新报表模型
			saveReport.setType(ReportResource.INT_REPORT_ANALYZE);

			// 然后进行报表模型的保存
			if (bCreate) {
				ValueObject[] vo2 = srv.create(new ReportVO[] { saveReport });// 报表的物理保存（创建）
				if (vo2 != null && vo2.length > 0) {
					pk_report = vo2[0].getPrimaryKey();
					getReport().getContextVo().setContextId(pk_report);
				}
			} else {
				srv.update(new ReportVO[] { saveReport });// 报表的物理保存（修改）
			}
			if (!pk_report.equals(getReportPK())) {
				setReportPK(pk_report);
			}
			AppDebug.debug(StringResource.getStringResource("miufo00191") + pk_report);
			setDirty(false);
			if (getModel().getDataModel() != null) {
				getModel().getDataModel().setDirty(false);
			}
			this.fromReport=false;
		} catch (Exception e) {
			AppDebug.debug(e);
			return StringResource.getStringResource("miufotableinput000005");
		}

		return StringResource.getStringResource("miufopublic391");
	}

	private ReportVO createNewReportVO(String[] repInfo, String userID) {
		ReportVO saveReport = new ReportVO();
		saveReport.setReportcode(repInfo[0]);
		saveReport.setReportname(repInfo[1]);
		saveReport.setType(ReportResource.INT_REPORT_ANALYZE);
		saveReport.setNote(repInfo[2]);
		saveReport.setPk_folderID(repInfo[3]);
		saveReport.setOwnerid(userID);
		return saveReport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		if(!isAegisFormat())
			return false;
		return getModel().getFormatModel().isDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		return new String[] { AnaRepField.EXKEY_FIELDINFO };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
	 */
//	public SheetCellEditor getDataEditor(String extFmtName) {
//		
//		return null;
//	}
//
//	public SheetCellRenderer getDataRender(String extFmtName) {
//		if (extFmtName == null)
//			return null;
//		if (extFmtName.equals("iufo_fmt_exarea")) {
//			return new SheetCellRenderer() {
//				public Component getCellRendererComponent(final CellsPane cellsPane, Object value, boolean isSelected, boolean hasFocus,
//						int row, int column, Cell cell) {
//
//					if (value == null) {
//						return null;
//					}
//					final ExAreaCell exCell = (ExAreaCell) value;
//					JComponent comp = null;
//					if (exCell.getModel() instanceof AreaDataModel) {
//						AreaDataModel areaModel = (AreaDataModel) exCell.getModel();
//						if (areaModel.getCrossSet() != null && areaModel.getCrossSet().getCrossArea() != null
//								&& areaModel.getCrossSet().getCrossPoint() != null) {
//							comp = new ExAreaComponent(exCell, cellsPane, areaModel.getCrossSet().getCrossArea(), areaModel.getCrossSet()
//									.getCrossPoint());
//						}
//					}
//					if (comp == null) {
//						comp = new ExAreaComponent(exCell, cellsPane);
//					}
//
//					return comp;
//
//				}
//			};
//		}
//		return null;
//	}

	protected IPluginDescriptor createDescriptor() {
		return new AnaReportDesc(this);
	};

	public AnaReportModel getModel() {
		return repModel;
	}

	void showErrorMessage(String error) {
		NCOptionPane.showMessageDialog(getReport(), error, null, NCOptionPane.ERROR_MESSAGE);
	}

	public void setOperationState(int operationState) {
		FreeQueryContextVO context = (FreeQueryContextVO) getReport().getContextVo();
		getReport().stopCellEditing();
		if (operationState == UfoReport.OPERATION_INPUT) {
			ReportParameter repParams = getModel().getReportParams(true);
			if (repParams.getSize() > 0&&m_isSetParam) {
				//@edit by yza at 2008-2-24 无效NC数据源异常提示
				try{
					ReportParamDlg dlg = new ReportParamDlg(getReport(), repParams, getModel());
					if (dlg.showModal() == UIDialog.ID_OK) {
						repParams = dlg.getReportParams();
						getModel().setReportParamValues(repParams);
					} else
						getModel().setReportParamValues(null);
				}catch(DSNotFoundException ex){
					AppDebug.debug(ex);
					MessageDialog.showErrorDlg(getReport(),MultiLang.getString("error"), ex.getMessage());
				}
			}
		}
		Object repIdObj = m_context.getAttribute(REPORT_PK);
		String reportID = repIdObj != null && (repIdObj instanceof String) ? (String) repIdObj : null;

		getModel().setOperationState(operationState, reportID, context.getCurUserID());
		changeUIState(operationState);

	}

	/**
	 * 更改模型状态，切换界面模型 liuyy+
	 * 
	 * @param operationState
	 */
	public void changeUIState(int operationState) {

		CellsModel newCM = getModel().getCellsModel();
		newCM.setEnableEvent(false);
		CellsModel oldCM = getReport().getTable().getCellsModel();
		if (operationState == UfoReport.OPERATION_FORMAT) {
			oldCM.removeCellsModelListener(getDataModelListener());
		}

		getReport().getTable().setCurCellsModel(newCM);
		getReport().setOperationState(operationState);

		// //liuyy+, 触发可扩展区分页数据加载。 AnaReportModel.doCreateDataModel()已处理。
		// if (operationState == UfoReport.OPERATION_INPUT) {
		// // getReport().getTable().getCells().paginalData();
		// }
		// liuyy end.
		// 设置单元权限模型
		this.getReport().getTable().getCells().setCellsAuthorization(new AnaReportAuth(this));

		newCM.setEnableEvent(true);

		if (operationState == UfoReport.OPERATION_INPUT)
			newCM.addCellsModelListener(getDataModelListener());// 加监听器必须在上面一句代码之后，否则会被清除！

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt == null)
			return;
		if (evt.getSource() == this.repModel) {
			if (evt.getPropertyName().equals(AnaPropertyType.REPSTATE_CHANGED)) {// 报表状态改变，需要更新CellsModel
				// getReport().getTable().setCurCellsModel(repModel.getCellsModel());
			}
		}

		if (evt.getPropertyName().equals(AnaConstants.PAGE_DIM_CHANGED)) {
			refreshDataModel(true, true);
			setDirty(true);
		}

	}

	@Override
	public int getOperationState() {
		return (getModel().isFormatState() ? UfoReport.OPERATION_FORMAT : UfoReport.OPERATION_INPUT);
	}

	public void userActionPerformed(UserUIEvent e) {
		// TODO Auto-generated method stub

	}

	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		// TODO Auto-generated method stub
		return null;
	}

	public AnaDataSetPanel getQueryPanel() {
		if (m_queryPanel == null) {
			m_queryPanel = new AnaDataSetPanel() {
				private static final long serialVersionUID = 1L;

				public void applyQueryModelDragSource() {
				}
			};
			refreshDataSource(true);
		}
		return m_queryPanel;
	}

	/**
	 * 重新生成数据源树
	 * 
	 * @i18n miuforepcalc0010=数据源
	 * @i18n miufo00193=引用的数据集无法正确获取，是否从报表中删除?
	 */
	public void refreshDataSource(boolean bFirst) {
		getQueryPanel().setName(StringResource.getStringResource("miufo00241"));

		m_queryPanel.getQueryModelTree().setModel(
				new DefaultTreeModel(new DefaultMutableTreeNode(StringResource.getStringResource("mbirep00013"))));

		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) m_queryPanel.getQueryModelTree().getModel().getRoot();
		// rootNode.setUserObject(StringResource.getStringResource("miufopublic128"));//
		// =数据源
		rootNode.setUserObject(StringResource.getStringResource("miufo00241"));

		ArrayList<String> rmvDSPK = new ArrayList<String>();
		String[] allQuerys = getModel().getDataSource().getAllDSPKs();
		if (allQuerys != null && allQuerys.length > 0) {
			for (String dsPK : allQuerys) {
				DataSetDefVO dsDef = getModel().getDataSource().getDataSetDefVO(dsPK);
				if (dsDef != null) {
					Field[] flds = dsDef.getDataSetDef().getMetaData().getFields(true);
					m_queryPanel.addFieldNode(dsDef, flds);
				} else
					rmvDSPK.add(dsPK);
			}
		}
		if (bFirst) {
			JTree queryTree = m_queryPanel.getQueryModelTree();
			DndHandler.enableDndDrag(queryTree, new AnaRepAdapter(queryTree), DnDConstants.ACTION_COPY);
			DndHandler.enableDndDrop(queryTree, new AnaRepAdapter(queryTree));
			CellsPane cPane = getReport().getTable().getCells();

			IDndAdapter anaRepAdapter = new AnaRepAdapter(cPane, this);// modify
			// by
			// wangyga
			// 2008-9-6
			// 保证是同一个对象
			DndHandler.enableDndDrag(cPane, anaRepAdapter, DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(cPane, anaRepAdapter);
		}
		if (rmvDSPK.size() > 0) {
			boolean remove = (MessageDialog.showYesNoDlg(getReport(), null, StringResource.getStringResource("miufo00193")) == MessageDialog.ID_YES);
			if (remove) {
				getModel().removeDataSourcePK(rmvDSPK.toArray(new String[0]));
				setDirty(true);
			}
		}
		// m_queryPanel.updateUI();
	}
	
	public PageDimNavigationDropTarget getConditionPanelDropTarget(){
		if(target==null){
			this.target = new PageDimNavigationDropTarget(new PageDimNavigationPanel());
			loadConditonPanelData(true);
			getConditionPanelDropTarget().getDropPanel().addPropertyChangeListener(this);
		}
		return target;
	}
	
	public void loadConditonPanelData(boolean beFirest){
		if(!beFirest){
			getConditionPanelDropTarget().getDropPanel().removeAllPageDim();
		}
		// 先初始化PageDimNavigationDropTarget.再PageDimNavigationPanel.addPageDim(),以使componentAdded事件被PageDimNavigationDropTarget处理
		ArrayList<AnaReportCondition> conditions = getModel().getReportCondition();
		AnaReportCondition condition = null;
		if (conditions != null) {
			for (int i = 0; i < conditions.size(); i++) {
				condition = conditions.get(i);
				condition.setReportModel(getModel());
				if(condition.getDsTool().getContext()==null){
					condition.getDsTool().setContext(getModel().getContextVO());
				}	
				condition.checkSelectedValue();
				getConditionPanelDropTarget().getDropPanel().addPageDim(condition);
			}
		}
		// 当页纬度变化时更新面板
		getModel().addPropertyChangeListener(AnaPropertyType.REPCONDITION_CHANGED, getConditionPanelDropTarget().getDropPanel());
        // 用户使用拖拽进行页纬度操作时通知模型
		getConditionPanelDropTarget().addChangeListener(getModel());
	}
	public void removeFromConditionPanel(String removeId) {
		ArrayList<AnaReportCondition> removeConditions = new ArrayList<AnaReportCondition>();
		AnaReportCondition condition = null;
		if (removeId != null && getModel().getReportCondition().size() > 0) {
			for (int i = 0; i < getModel().getReportCondition().size(); i++) {
				condition = getModel().getReportCondition().get(i);
				if (removeId.equals(condition.getDatasetPK())) {
					removeConditions.add(condition);
				}
			}
		}

		for (int i = 0; i < removeConditions.size(); i++) {
			condition = removeConditions.get(i);
			getModel().propertyChange(new PropertyChangeEvent(getConditionPanelDropTarget().getDropPanel(), AnaConstants.PAGE_DIMENSION_REMOVE, condition, null));
		}
	}

	public SelDataSourceDlg getSelDataSourceDlg() {
		if (m_selDataSourceDlg == null) {
			m_selDataSourceDlg = new SelDataSourceDlg(getReport(), pk_user);
		}
		return m_selDataSourceDlg;
	}

	public void addDataSource(DataSetDefVO[] defs, boolean isPrivateDS) {
		ArrayList<DataSetDefVO> al_vos = new ArrayList<DataSetDefVO>();
		String[] queryIDs = getModel().getDataSource().getAllDSPKs();
		for (DataSetDefVO ds : defs) {
			if (queryIDs == null || !Arrays.asList(queryIDs).contains(ds.getPk_datasetdef()))
				al_vos.add(ds);
		}
		String[] newQueryIDs = new String[al_vos.size()];
		for (int i = 0; i < al_vos.size(); i++) {
			DataSetDefVO dsVO = al_vos.get(i);

			newQueryIDs[i] = dsVO.getPk_datasetdef();
			Field[] flds = dsVO.getDataSetDef().getMetaData().getFields(true);
			getQueryPanel().addFieldNode(dsVO, flds);
		}

		getQueryPanel().getQueryModelTree().expandRow(0);
		getModel().getDataSource().addDataSourcePK(newQueryIDs);
		getModel().getDataSource().addDateSetDefVOs(defs, isPrivateDS);

	}

	/**
	 * @i18n miufo00194=合计:
	 */
	public void initAnaReport(UfoReport report, IFreeQueryModel queryDef, FreeQueryContextVO context, boolean bCreateReport,
			boolean addQuery) {
		MeasureQueryModelDef mqdf = null;
		// 1。根据环境中的查询定义创建私有数据集
		if (!(queryDef instanceof MeasureQueryModelDef)) {// 目前只认识指标查询对象
			return;
		} else {
			mqdf = (MeasureQueryModelDef) queryDef;
		}
		Object repIdObj = m_context.getAttribute(REPORT_PK);
		String reportID = repIdObj != null && (repIdObj instanceof String) ? (String) repIdObj : null;

		// String reportID = report.getContextVo().getContextId();
		if (reportID == null) {
			reportID = IDMaker.makeID(20);
			report.getContextVo().setContextId(reportID);
		}

		// 创建过程参考了NewDataSetExt.getParams的内容
		// 1.1 默认的私有数据集命名规则
		DataSetDefVO vo = new DataSetDefVO();
		vo.setPk_datasetdir(DataSetDirDMO.PrivateDataSetPK);
		vo.setOwner(reportID); // 将私有数据拥有者ID写入数据集定义
		vo.setCode(AnaRepDataSource.REPORT_CODE_DEFAULT_PRIVATE);
		vo.setName(AnaRepDataSource.REPORT_NAME_DEFAULT_PRIVATE);
		vo.setKind("R");

		// 1.2 数据集对象的生成、
		Object[] params = new Object[2];
		params[0] = mqdf;
		params[1] = context;
		RptProvider provider = new RptProvider(params);
		DataSet ds = vo.getDataSetDef();
		ds.setProvider(provider);

		vo.setDataSetDef(ds);
		// 1.3 数据集的存储
		String dsPK = IDMaker.makeID(20);
		vo.setPk_datasetdef(dsPK);

		try {
			// 更新元数据信息
			provider.provideMetaData(ds, provider.getContext());
			// vo = DataSetDefBO_Client.createDataSetDef(vo);
		} catch (Exception ex) {
			AppDebug.debug(ex);
			return;
		}

		// 2。执行“数据集插入”，加入到数据集树上
		addDataSource(new DataSetDefVO[] { vo }, true);

		// 3。将所有字段加入到一个扩展区域中，并对数值类型的字段加合计行
		AnaRepField[] flds = AnaRepField.convertFieldToRepFlds(dsPK, ds.getMetaData().getFields(true));
		CellPosition beginPos = CellPosition.getInstance(0, 0);
		appendFields(beginPos, dsPK, flds, true);

		// 4.增加数据集条件和页纬度
		initAnaFilters(ds, dsPK, mqdf);

		boolean hasTotalFld = false;
		for (int i = 0; i < flds.length; i++) {
			AnaRepField fld = flds[i];
			if (fld.getField().getExtType() == RptProvider.MEASURE && DataTypeConstant.isNumberType(fld.getFieldDataType())) {// 对于数值类型的字段自动添加合计
				FieldCountDef tFld = new FieldCountDef(fld.getField(), IFldCountType.TYPE_SUM, null, null, null);
				AnaRepField totalFld = new AnaRepField(fld.getField(), AnaRepField.TYPE_CALC_FIELD, dsPK);
				totalFld.setCountFieldDef(tFld);
				appendFields(CellPosition.getInstance(2, i), dsPK, new AnaRepField[] { totalFld }, false);
				hasTotalFld = true;
			}
		}
		if (hasTotalFld) {
			CellPosition pos = CellPosition.getInstance(2, 0);
			Cell cell = getCellsModel().getCell(pos);
			if (cell == null || cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) == null)
				getCellsModel().setCellValue(pos, StringResource.getStringResource("miufo00194"));
		}
		this.setDirty(true);
	}

	/**
	 * 
	 * @param areaData
	 * @param ds
	 * @param dsPK
	 * @param mqdf
	 * @i18n uiufotask00012=下级单位
	 */
	private void initAnaFilters(DataSet ds, String dsPK, MeasureQueryModelDef mqdf) {

		KeyVO[] keys = mqdf.getKeyGroupVO().getKeys();
		String keyValue = null;
		Hashtable<String, String> filters = mqdf.getFilterMap();
		FilterDescriptor fDes = new FilterDescriptor();// 扩展区域过滤条件
		ArrayList<AnaReportCondition> reportConditions = getModel().getReportCondition();
		AnaReportCondition reportCondition = null;
		DimMemberVO defaultvalue = null;
		Field conField = null;

		if (mqdf.getQueryType() == MeasureQueryModelDef.QUERYBY_CORP) {// 下级数据
			keyValue = filters.get(IRptProviderCreator.COLUMN_ORGPK);// 当前查询单位
			if (keyValue != null) {
				Field countFld = ds.getField(IRptProviderCreator.COLUMN_ORGPK);
				FilterItem item = new FilterItem();
				item.setOperation(FilterItem.Filter_Operation[6]);
				item.setFieldInfo(countFld);
				item.setValue(keyValue + "%");
				fDes.addFilter(item);

			}
			for (int i = 0; i < keys.length; i++) {
				String strAccPeriod = null;
				if (keys[i].isAccPeriodKey() || keys[i].isTimeKeyVO()) {
					if (keys[i].isAccPeriodKey()) {
						strAccPeriod = keys[i].getKeywordPK();
					}
					keyValue = filters.get(RptProviderCreator.getDataSetColumnName(keys[i]));
					if (keyValue != null) {
						String yearfld = RptProviderCreator.getTimeValue(IRptProviderCreator.YEARFLD, keyValue);
						conField = ds.getMetaData().getField(IRptProviderCreator.YEARFLD);
						if (conField != null && yearfld != null) {
							defaultvalue = new DimMemberVO();
							defaultvalue.setMemberID(yearfld);// DimMemberVO.equals()以ID为比较的
							defaultvalue.setMemcode(yearfld);
							defaultvalue.setMemname(yearfld);
							reportCondition = new AnaReportCondition(dsPK, conField);
							reportCondition.setReportModel(getModel());
							reportCondition.setSelectedValue(defaultvalue);
							reportConditions.add(reportCondition);

						}

						if (strAccPeriod == null || KeyVO.ACC_MONTH_PK.equals(strAccPeriod)) {
							String monthfld = RptProviderCreator.getTimeValue(IRptProviderCreator.MONTHFLD, keyValue);
							conField = ds.getMetaData().getField(IRptProviderCreator.MONTHFLD);
							if (conField != null && monthfld != null) {
								defaultvalue = new DimMemberVO();
								defaultvalue.setMemberID(monthfld);// DimMemberVO.equals()以ID为比较的
								defaultvalue.setMemcode(monthfld);
								defaultvalue.setMemname(monthfld);
								reportCondition = new AnaReportCondition(dsPK, conField);
								reportCondition.setReportModel(getModel());
								reportCondition.setSelectedValue(defaultvalue);
								reportConditions.add(reportCondition);
							}
						}

					}

					break;
				}
			}

		}
		if (mqdf.getQueryType() == MeasureQueryModelDef.QEURY_NEXTCORP) {// 直接下级数据
			keyValue = filters.get(IRptProviderCreator.COLUMN_ORGPK);// 当前查询单位

			if (keyValue != null) {
				Field countFld = ds.getField(IRptProviderCreator.COLUMN_ORGPK);
				FilterItem item = new FilterItem();
				item.setOperation(FilterItem.Filter_Operation[6]);
				item.setFieldInfo(countFld);
				item.setValue(keyValue + "%");
				fDes.addFilter(item);

				countFld = new Field();
				countFld.setDataType(Variant.INT);
				countFld.setFldname(StringResource.getStringResource("uiufotask00012"));
				countFld.setCaption(StringResource.getStringResource("uiufotask00012"));
				countFld.setExpression("len(" + IRptProviderCreator.COLUMN_ORGPK + ")");
				countFld.setEdit(false);
				ds.getMetaData().addField(countFld);
				item = new FilterItem();
				item.setOperation(FilterItem.Filter_Operation[0]);
				item.setFieldInfo(countFld);
				item.setValue(new Integer(RptProviderCreator.getNextUnitLevel(keyValue)).toString());
				fDes.addFilter(item);

			}

			for (int i = 0; i < keys.length; i++) {
				String strAccPeriod = null;
				if (keys[i].isAccPeriodKey() || keys[i].isTimeKeyVO()) {
					if (keys[i].isAccPeriodKey()) {
						strAccPeriod = keys[i].getKeywordPK();
					}
					keyValue = filters.get(RptProviderCreator.getDataSetColumnName(keys[i]));
					if (keyValue != null) {
						String yearfld = RptProviderCreator.getTimeValue(IRptProviderCreator.YEARFLD, keyValue);
						conField = ds.getMetaData().getField(IRptProviderCreator.YEARFLD);
						if (conField != null && yearfld != null) {
							defaultvalue = new DimMemberVO();
							defaultvalue.setMemberID(yearfld);// DimMemberVO.equals()以ID为比较的
							defaultvalue.setMemcode(yearfld);
							defaultvalue.setMemname(yearfld);
							reportCondition = new AnaReportCondition(dsPK, conField);
							reportCondition.setReportModel(getModel());
							reportCondition.setSelectedValue(defaultvalue);
							reportConditions.add(reportCondition);

						}
						if (strAccPeriod == null || KeyVO.ACC_MONTH_PK.equals(strAccPeriod)) {
							String monthfld = RptProviderCreator.getTimeValue(IRptProviderCreator.MONTHFLD, keyValue);
							conField = ds.getMetaData().getField(IRptProviderCreator.MONTHFLD);
							if (conField != null && monthfld != null) {
								defaultvalue = new DimMemberVO();
								defaultvalue.setMemberID(monthfld);// DimMemberVO.equals()以ID为比较的
								defaultvalue.setMemcode(monthfld);
								defaultvalue.setMemname(monthfld);
								reportCondition = new AnaReportCondition(dsPK, conField);
								reportCondition.setReportModel(getModel());
								reportCondition.setSelectedValue(defaultvalue);
								reportConditions.add(reportCondition);
							}
						}

					}
					break;
				}
			}

		}
		if (mqdf.getQueryType() == MeasureQueryModelDef.QUERYBY_YEAR) {// 本年数据
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].isAccPeriodKey() || keys[i].isTimeKeyVO()) {
					keyValue = filters.get(RptProviderCreator.getDataSetColumnName(keys[i]));
					if (keyValue != null) {
						FilterItem item = new FilterItem();
						item.setOperation(FilterItem.Filter_Operation[0]);
						item.setFieldInfo(ds.getField(IRptProviderCreator.YEARFLD));
						item.setValue(RptProviderCreator.getTimeValue(IRptProviderCreator.YEARFLD, keyValue));
						fDes.addFilter(item);
					}
				}
			}

			for (int i = 0; i < keys.length; i++) {
				if (KeyVO.isUnitKeyVO(keys[i])) {
					keyValue = filters.get(IRptProviderCreator.COLUMN_UNITCODE);
					if (keyValue != null) {

						conField = ds.getMetaData().getField(IRptProviderCreator.COLUMN_UNITCODE);
						if (conField != null && keyValue != null) {
							defaultvalue = new DimMemberVO();
							defaultvalue.setMemberID(keyValue);// DimMemberVO.equals()以ID为比较的
							defaultvalue.setMemcode(keyValue);
							defaultvalue.setMemname(keyValue);
							reportCondition = new AnaReportCondition(dsPK, conField);
							reportCondition.setReportModel(getModel());
							reportCondition.setSelectedValue(defaultvalue);
							reportConditions.add(reportCondition);

						}

					}
				}

				if (keys[i].isAccPeriodKey() || keys[i].isTimeKeyVO()) {
					keyValue = filters.get(RptProviderCreator.getDataSetColumnName(keys[i]));
					if (keyValue != null) {
						String yearfld = RptProviderCreator.getTimeValue(IRptProviderCreator.YEARFLD, keyValue);
						conField = ds.getMetaData().getField(IRptProviderCreator.YEARFLD);
						if (conField != null && yearfld != null) {
							defaultvalue = new DimMemberVO();
							defaultvalue.setMemberID(yearfld);// DimMemberVO.equals()以ID为比较的
							defaultvalue.setMemcode(yearfld);
							defaultvalue.setMemname(yearfld);
							reportCondition = new AnaReportCondition(dsPK, conField);
							reportCondition.setReportModel(getModel());
							reportCondition.setSelectedValue(defaultvalue);
							reportConditions.add(reportCondition);
						}
					}
				}

			}

		}
		if (fDes.getFilterItemCount() > 0) {
			ds.addDescriptor(fDes);
		}

	}

	public FreeQueryContextVO getContextVO() {
		return m_context;
	}

	/**
	 * 分析表中增加字段
	 * 
	 * @param pos
	 * @param flds
	 * @return
	 * @i18n miufo00195=对不起,没有对应格式态位置!
	 */
	public boolean appendFields(CellPosition pos, String dsPK, AnaRepField[] flds, boolean addTitle) {
		AnaReportModel model = getModel();
		ExAreaModel areaModel = ExAreaModel.getInstance(model.getFormatModel());
		String msg = checkAppendField(model, areaModel, pos, dsPK, flds, addTitle);
		if (msg != null) {
			JOptionPane.showMessageDialog(getReport(), msg);
			return false;
		}

		// 目标位置有字段时，处理成交换？
		AnaRepField aimFld = doAppendField(model, areaModel, pos, dsPK, flds, addTitle);
		setDirty(true);
		return true;
	}

	public AnaRepField doAppendField(AnaReportModel model, ExAreaModel areaModel, CellPosition pos, String dsPK, AnaRepField[] flds,
			boolean addTitle) {
		AreaPosition area = AreaPosition.getInstance(pos.getRow(), pos.getColumn(), flds.length, addTitle ? 2 : 1);
		ExAreaCell exCell = areaModel.getExArea(area);
		if (exCell == null) {// 设置目标区域为扩展区域
			// 看相邻单元有没有可加入（同一数据集）的区域
			int stepCol = 0;
			CellPosition prePos = pos.getStart().getMoveArea(0, -1);// 看左侧单元
			ExAreaCell preArea = areaModel.getExArea(prePos);
			// @edit by wangyga at 2009-2-12,下午02:26:53 在扩展区域中,字段和图表互斥
            if(preArea != null && preArea.getExAreaType() == ExAreaCell.EX_TYPE_CHART)
            	return null;
			if (canExpandArea(model, areaModel, preArea, dsPK)) {
				stepCol = flds.length;
				if (areaModel.extendArea(preArea, 0, stepCol))
					exCell = preArea;
				addTitle = false;
			} else {
				prePos = pos.getStart().getMoveArea(-1, 0);// 看上边单元
				preArea = areaModel.getExArea(prePos);
				// @edit by wangyga at 2009-2-12,下午02:26:53 在扩展区域中,字段和图表互斥
	            if(preArea != null && preArea.getExAreaType() == ExAreaCell.EX_TYPE_CHART)
	            	return null;
				if (canExpandArea(model, areaModel, preArea, dsPK)) {
					stepCol = flds.length - (preArea.getArea().getEnd().getColumn() - pos.getColumn() + 1);
					if (stepCol < 0) {
						stepCol = 0;
					}
					if (areaModel.extendArea(preArea, 1, stepCol))
						exCell = preArea;
					addTitle = false;
				}
			}
			if (exCell == null)
				exCell = areaModel.addExArea(area);
		}
		if (exCell == null)
			return null;

		String areaPK = exCell.getExAreaPK();
		AreaDataModel areaData = model.getAreaData(areaPK);
		if (areaData.getDSPK() == null) {// 新增区域
			exCell.setExMode(ExAreaCell.EX_MODE_Y);// 默认设置成纵向扩展
			areaData.setDSPK(dsPK);
		}
		exCell.setExAreaType(ExAreaCell.EX_TYPE_SAMPLE);
		model.setAreaData(exCell, areaData);

		// modify by wangyga //添加数据态时，数据集字段的添加
		CellsModel cells = model.getFormatModel();

		// 放置字段到目标位置
		for (int i = 0; i < flds.length; i++) {
			// 字段名称
			CellPosition aimPos = pos.getMoveArea(0, i);
			if (addTitle) {
				cells.setCellProperty(aimPos, PropertyType.DataType, TableConstant.CELLTYPE_SAMPLE);
				cells.setCellValue(aimPos, flds[i].getFieldShowName());

				aimPos = pos.getMoveArea(1, i);
			} else {// 隐含的意思是在加入的字段是在表格内移动而来的
				AnaRepField aimFld = cells.getCell(aimPos) == null ? null : (AnaRepField) cells.getCell(aimPos).getExtFmt(
						AnaRepField.EXKEY_FIELDINFO);
				// if (aimFld != null) {
				// return aimFld;
				// }

			}
			AnaReportFieldAction.addFlds(cells, aimPos, flds[i]);

		}
		getReport().getTable().getCells().repaint(exCell.getArea(), true);
		return null;
	}

	/**
	 * 校验拖动区域是否符合规则
	 * 
	 * @param model
	 * @param areaModel
	 * @param pos
	 * @param dsPK
	 * @param flds
	 * @param addTitle
	 * @return
	 * @i18n miufo00196=扩展区域和已有区域重叠。
	 * @i18n miufo00197=此位置已经有扩展区域，不能覆盖。
	 * @i18n miufo00198=数据集不支持汇总，无法放入交叉区域
	 * @i18n miufo00199=交叉区域无法容纳所有数据
	 */
	private String checkAppendField(AnaReportModel model, ExAreaModel areaModel, CellPosition pos, String dsPK, AnaRepField[] flds,
			boolean addTitle) {
		// if (!model.isFormatState())
		// return "数据态暂时不能执行此操作";
		AreaPosition area = AreaPosition.getInstance(pos.getRow(), pos.getColumn(), flds.length, addTitle ? 2 : 1);
		ExAreaCell exCell = areaModel.getExArea(area);
		if (exCell == null) {
			if (!areaModel.check(new ExAreaCell(area), area))
				return StringResource.getStringResource("miufo00196");
			return null;
		} else if (!canExpandArea(model, areaModel, exCell, dsPK)) {
			return StringResource.getStringResource("miufo00197");
		} else if (exCell.getModel() instanceof AreaDataModel) {
			AreaDataModel areaDataModel = (AreaDataModel) exCell.getModel();
			if (areaDataModel.getCrossSet() != null && areaDataModel.getCrossSet().getCrossArea().contain(area)) {
				AnaDataSetTool preDS = null;
				if (areaDataModel.getDSTool() == null) {
					preDS = getModel().getDataSetTool(dsPK);
				} else {
					preDS = areaDataModel.getDSTool();
				}

				if (preDS != null && !preDS.isSupport(DescriptorType.AggrDescriptor)) {// 对于不支持汇总的数据集，此功能不可用
					return StringResource.getStringResource("miufo00198");
				}

				AnaCrossTableSet cross = areaDataModel.getCrossSet();
				AreaPosition aimCrossArea = cross.getCrossTypeArea(pos);
				if (aimCrossArea != null && !aimCrossArea.contain(area)) {
					return StringResource.getStringResource("miufo00199");
				}

			}
		}
		return null;
	}

	// 判断能否将新的数据集加入到指定区域
	private static boolean canExpandArea(AnaReportModel model, ExAreaModel areaModel, ExAreaCell area, String dsPK) {
		if (area == null)
			return false;
		String areaDSPK = model.getAreaData(area.getExAreaPK()).getDSPK();
		if (areaDSPK == null || areaDSPK.equals(dsPK))
			return true;
		return false;
	}

	// 更新数据模型
	public void refreshDataModel(boolean reloadData) {
		refreshDataModel(reloadData, true);
	}

	/*
	 * 禁止调用getReport().stopCellEditing();如需要在外围调用
	 */
	private void refreshDataModel(boolean reloadData, boolean doModelChange) {
		if (getModel().isFormatState())
			return;

		CellPosition[] selArea = getReport().getCellsModel().getSelectModel().getSelectedCells();
		// @edit by ll at 2009-1-9,上午11:04:01
		// 改为调用CellsPane的paginalData()，因为原来的代码只能加载第一页
		if (doModelChange) {
			getModel().changeState(false, reloadData);
			changeUIState(UfoReport.OPERATION_INPUT);
		} else {
			getModel().setShouldLoadData(reloadData);
		}
		getReport().getTable().getCells().paginalData();
		if (selArea != null && selArea.length > 0)
			getReport().getCellsModel().getSelectModel().setAnchorCell(selArea[0]);
	}

	public void setDirty(boolean dirty) {
		super.setDirty(dirty);
		getModel().setDirty(dirty);
	}

	private DataModelListener getDataModelListener() {
		if (m_dataListener == null)
			m_dataListener = new DataModelListener(this);
		return m_dataListener;
	}

	public AnaFormatModelListener getFormatListener() {
		if (m_formatListener == null) {
			m_formatListener = new AnaFormatModelListener(this);
		}
		return m_formatListener;
	}

	public String getReportPK() {
		return pk_report;
	}

	public void setReportPK(String reportPk) {
		this.pk_report = reportPk;
	}

	public String getUserPK() {
		return pk_user;
	}
    
	/**
	 * add by wangyga 
	 * 获得选中的区域，数据态时需要转化为格式态的字段对应的区域
	 * @return AreaPosition[]
	 */
	public AreaPosition[] getFormatSelected(){
		AnaReportModel anaModel =getModel();
		CellsModel cells = anaModel.getFormatModel();
		AreaPosition[] area = cells.getSelectModel().getSelectedAreas();
		if(!anaModel.isFormatState()){//add by wangyga 2008-9-17 获得数据态对应字段位置			
			CellsModel dataModel = anaModel.getDataModel();
			area = anaModel.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
		}
		return area;
	}
	
	public CellPosition getFormatAnchorPos(){
		AnaReportModel anaModel =getModel();
		CellPosition anchorCellPos = anaModel.getCellsModel().getSelectModel().getAnchorCell();
		if(!anaModel.isFormatState()){
			CellPosition[] dataStateCells = anaModel.getFormatPoses(anaModel.getDataModel(), new AreaPosition[]{AreaPosition.getInstance(anchorCellPos, anchorCellPos)});
			if(dataStateCells != null && dataStateCells.length > 0)
				anchorCellPos = dataStateCells[0];
		}
		return anchorCellPos;
	}
	/**
	 * 获取格式态选中的单元
	 * @return
	 */
	public CellPosition[] getSelectedPos(CellsModel model,AreaPosition selectedArea) {
		ArrayList<CellPosition> list = new ArrayList<CellPosition>();
        if(selectedArea!=null){
        	list.addAll(model.getSeperateCellPos(selectedArea));
        }
		
		return list.toArray(new CellPosition[0]);
	}
	
	/**
	 * 获取选中区域对应的数据模型
	 * @param model
	 * @param area
	 * @return
	 */
	public AreaDataModel getExAreaModel(CellsModel model, AreaPosition area) {
		ExAreaCell ex = ExAreaModel.getInstance(model).getExArea(area);
		if (ex != null && ex.getModel() instanceof AreaDataModel) {
			return (AreaDataModel) ex.getModel();
		}
		return null;
	}
}
 