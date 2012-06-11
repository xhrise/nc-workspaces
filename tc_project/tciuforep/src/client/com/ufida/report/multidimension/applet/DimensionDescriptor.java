package com.ufida.report.multidimension.applet;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.vo.pub.BusinessException;

import com.ufida.bi.base.BIException;
import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.multidimension.model.AnalyzeUtil;
import com.ufida.report.multidimension.model.AnalyzerSet;
import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.DrillThroughSet;
import com.ufida.report.multidimension.model.IAnalyzerSet;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiDimWriteBackUtil;
import com.ufida.report.multidimension.model.MultiDimemsionModel;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufida.report.rep.applet.BINavigationExt;
import com.ufida.report.rep.applet.BIReportPreViewExt;
import com.ufida.report.rep.applet.BIReportSaveExt;
import com.ufida.report.rep.applet.PageDimNavigationPanel;
import com.ufida.report.rep.model.BIContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.print.HeaderFooterExt;
import com.ufsoft.report.sysplugin.print.PrintExt;

import com.ufsoft.report.sysplugin.print.PrintPreViewExt;
import com.ufsoft.report.sysplugin.print.PrintSettingExt;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * 
 * @author zzl 2005-4-27
 */
public class DimensionDescriptor implements IPluginDescriptor, PropertyChangeListener {
	/** 数据导航（维度/成员设置） */
	private class SelDimExt extends AbsActionExt {

		private class SelDimCommand extends UfoCommand {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
			 */
			public void execute(Object[] params) {
				UfoReport m_report = (UfoReport) params[0];
				execSelDim(m_report);
			}
		}

		private DimensionPlugin m_dimPlugin = null;

		public SelDimExt(DimensionPlugin plugin) {
			super();
			m_dimPlugin = plugin;
		}

		public UfoCommand getCommand() {
			return new SelDimCommand();
		}

		public Object[] getParams(UfoReport container) {
			return new Object[] { container, m_dimPlugin };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("ubimultidim0032"));
			mainMenu.setGroup(StringResource.getStringResource("ubimultidim0032"));
			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo1001692") });

			ActionUIDes popupMenu = new ActionUIDes();
			popupMenu.setName(StringResource.getStringResource("ubimultidim0032"));
			popupMenu.setGroup(StringResource.getStringResource("ubimultidim0032"));
			popupMenu.setPopup(true);

			return new ActionUIDes[] { mainMenu, popupMenu };
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);
			if (superEnabled && m_dimPlugin.getModel().isDataEditable() == false) {
				return true;
			}
			return false;
		}
	}

	private class AnalyzerCommand extends UfoCommand {

		DimensionPlugin m_plugin = null;

		// 分析设置类型，-1表示进行分析管理
		private int m_type = -1;

		public AnalyzerCommand(DimensionPlugin plugin, int analyzerType) {
			m_plugin = plugin;
			m_type = analyzerType;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
		 */
		public void execute(Object[] params) {
			UfoReport report = (UfoReport) params[0];

			String selDimPK = null;
			int selPos = IMultiDimConst.COMBINE_COLUMN;
			
			SelDimModel selModel = m_plugin.getModel().getSelDimModel();

			CellPosition pos = report.getCellsModel().getSelectModel().getSelectedArea().getStart();
			if (pos.getRow() >= selModel.getSelDimVOs(IMultiDimConst.COMBINE_COLUMN).length)
				if (pos.getColumn() <selModel.getSelDimVOs(IMultiDimConst.COMBINE_ROW).length)
					selPos = IMultiDimConst.COMBINE_ROW;

			IMember selMember = MultiDimensionUtil.getSelectedDimMember(m_plugin.getModel(), pos);
			if (selMember != null)
				selDimPK = selMember.getDimID();
			if (selDimPK == null) {
				// 如果未选择维度（含指标），默认取行/列上的第一个维度
				selDimPK =selModel.getSelDimVOs(IMultiDimConst.COMBINE_COLUMN)[0]
						.getDimDef().getDimID();

				if (selDimPK.equals(IMultiDimConst.PK_MEASURE_DIMDEF)) {
					selDimPK = selModel.getSelDimVOs(IMultiDimConst.COMBINE_ROW)[0]
							.getDimDef().getDimID();
					selPos = IMultiDimConst.COMBINE_ROW;
				}
			}

			AnalyzerDialog dlg = m_plugin.getAnalyzerDlg();
			dlg.setCurrentInfo(selDimPK, selPos);
			if (m_type < 0) {
				dlg.showModal();
			} else {
				AnalyzerSet anaModel = m_plugin.getModel().getAnalyzerSet();
				int oldIndex = AnalyzeUtil.getDefinedAnalyzerSetIndex(anaModel, m_type, selPos);
				IAnalyzerSet oldSet = (oldIndex == -1) ? null : anaModel.getAnalyzer(oldIndex);

				IAnalyzerSet newSet = dlg.editAnalyzer(m_type, oldSet);
				if (newSet != null) {
					if (oldIndex >= 0) {
						anaModel.setAnalyzer(oldIndex, newSet);
						dlg.afterEditAnalyzer(IAnalyzerDialog.EDIT, m_type, oldSet);
					} else {
						anaModel.addAnalyzer(null, newSet);
						dlg.afterEditAnalyzer(IAnalyzerDialog.ADD, m_type, newSet);
					}
				}
			}
		}
	}

	/** 分析管理器 */
	private class AnalyzerExt extends AbsDimActionExt {

		public AnalyzerExt(DimensionPlugin plugin) {
			super(plugin);
		}

		public UfoCommand getCommand() {
			return new AnalyzerCommand(getPlugIn(), -1);
		}

		public Object[] getParams(UfoReport container) {
			return new Object[] { container };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("ubimultidim0033"));
			mainMenu.setGroup(StringResource.getStringResource("ubimultidim0033"));
			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo1001692") });

			ActionUIDes popupMenu = new ActionUIDes();
			popupMenu.setName(StringResource.getStringResource("ubimultidim0033"));
			popupMenu.setGroup(StringResource.getStringResource("ubimultidim0033"));
			popupMenu.setPopup(true);

			return new ActionUIDes[] { mainMenu, popupMenu };
		}

	}

	/** 分析管理细节，包括5种类型 */
	private class AnalyzerDetailExt extends AbsDimActionExt {

		private int m_type = IAnalyzerSet.TYPE_FILTER;

		public AnalyzerDetailExt(DimensionPlugin plugin, int analyzerType) {
			super(plugin);
			m_type = analyzerType;
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo1001692") });
			mainMenu.setName(getMenuName(m_type));
			mainMenu.setGroup("AnalyzerDetail");

			// ActionUIDes popupMenu = new ActionUIDes();
			// popupMenu.setPaths(new String[] { "数据" });
			// popupMenu.setName(getMenuName(m_type));
			// popupMenu.setPopup(true);
			return new ActionUIDes[] { mainMenu };
		}

		private String getMenuName(int type) {
			switch (type) {
			case IAnalyzerSet.TYPE_FILTER:
				return StringResource.getStringResource("miufo1000807");
			case IAnalyzerSet.TYPE_SORT:
				return StringResource.getStringResource("miufo1001595");
			case IAnalyzerSet.TYPE_LIMITROWS:
				return StringResource.getStringResource("ubimultidim0034");
			case IAnalyzerSet.TYPE_FORMULAR:
				return StringResource.getStringResource("miufo1000033");
			case IAnalyzerSet.TYPE_HIDDEN:
				return StringResource.getStringResource("ubimultidim0024");

			default:
				break;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			return new AnalyzerCommand(getPlugIn(), m_type);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			return new Object[] { container };
		}

	}

	/** 预警 */
	@SuppressWarnings("unused")
	private class ALARMExt extends AbsDimActionExt {
		/**
		 * @param plugin
		 */
		public ALARMExt(DimensionPlugin plugin) {
			super(plugin);
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("ubimultidim0035"));
			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo1001692") });

			return new ActionUIDes[] { mainMenu };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			return new Object[] { container };
		}

	}

	/** 穿透路径 */
	private class DrillThroughExt extends AbsDimActionExt {

		private class DrillThroughCommand extends UfoCommand {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
			 */
			public void execute(Object[] params) {

				DrillThroughSet setModel = m_dimPlugin.getModel().getDrillTroughSet();
				DrillThroughSetDialog dlg = m_dimPlugin.getDrillDlg();
				if (!dlg.setDrillThroughSet(setModel)) {
					MessageDialog.showHintDlg(m_dimPlugin.getReport(), null, StringResource
							.getStringResource("mbimultidim0005"));
					return;
				}
				if (dlg.showModal() == UIDialog.ID_OK) {
					m_dimPlugin.getModel().setDrillTroughSet(dlg.getDrillThroughSet());
				}

			}
		}

		private DimensionPlugin m_dimPlugin = null;

		public DrillThroughExt(DimensionPlugin plugin) {
			super(plugin);
			m_dimPlugin = plugin;
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("ubimultidim0036"));
			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo1001692") });

			return new ActionUIDes[] { mainMenu };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			return new DrillThroughCommand();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			return new Object[] { container };
		}

	}

	private class ReportFormatCommand extends UfoCommand {

		DimensionPlugin m_plugin = null;

		public ReportFormatCommand(DimensionPlugin plugin) {
			m_plugin = plugin;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
		 */
		public void execute(Object[] params) {
			BIContextVO conVO = (BIContextVO) ((UfoReport) params[0]).getContextVo();
			String reportID = conVO.getReportPK();
			String userID = conVO.getCurUserID();

			ReportFormatDialog dlg = m_plugin.getFormatDlg();

			dlg.setFormatModel(m_plugin.getModel().getFormatSetModel());
			if (dlg.showModal() == UIDialog.ID_OK) {
				m_plugin.getModel().setFormatSetModel(dlg.getFormatModel());
				m_plugin.getModel().setOperationState(m_plugin.getOperationState(), reportID, userID);
			}
		}
	}

	/** 多维报表格式 */
	private class ReportFormatExt extends AbsDimActionExt {

		public ReportFormatExt(DimensionPlugin plugin) {
			super(plugin);
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("ubimultidim0037"));
			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo1000877") });

			return new ActionUIDes[] { mainMenu };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			return new ReportFormatCommand(getPlugIn());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			return new Object[] { container };
		}

	}

	private class ReportRowFormatCommand extends UfoCommand {

		DimensionPlugin m_plugin = null;

		public ReportRowFormatCommand(DimensionPlugin plugin) {
			m_plugin = plugin;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
		 */
		public void execute(Object[] params) {
			UfoReport m_report = (UfoReport) params[0];
			BIContextVO conVO = (BIContextVO) m_report.getContextVo();
			String reportID = conVO.getReportPK();
			String userID = conVO.getCurUserID();

			CellPosition pos = m_report.getCellsModel().getSelectModel().getSelectedArea().getStart();
			MultiDimemsionModel model = m_plugin.getModel();

			SelDimensionVO[] rowDims = model.getSelDimModel().getSelDimVOs(SelDimModel.POS_ROW);
			SelDimensionVO[] colDims = model.getSelDimModel().getSelDimVOs(SelDimModel.POS_COLUMN);

			boolean isRow = true;
			int selIndex = -1;
			if (pos.getRow() >= 0 && pos.getRow() < colDims.length) {
				if (pos.getColumn() >= rowDims.length) {
					isRow = false;
					selIndex = pos.getRow();
				}
			} else if (pos.getColumn() >= 0 && pos.getColumn() < rowDims.length) {
				if (pos.getRow() >= colDims.length) {
					selIndex = pos.getColumn();
				}
			}

			ColumnFormatDialog dlg = m_plugin.getRowFormatDlg();

			dlg.setFormatModel(model.getFormatSetModel(), isRow ? rowDims : colDims, isRow, selIndex);
			if (dlg.showModal() == UIDialog.ID_OK) {
				if (isRow) {
					model.getFormatSetModel().setAl_rowFormat(dlg.getAl_header());
				} else {
					model.getFormatSetModel().setAl_colFormat(dlg.getAl_header());
				}
				m_plugin.getModel().setOperationState(m_plugin.getOperationState(), reportID, userID);

			}
		}
	}

	/** 多维报表行列格式 */
	private class ReportRowFormatExt extends AbsDimActionExt {

		public ReportRowFormatExt(DimensionPlugin plugin) {
			super(plugin);
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("ubimultidim0038"));
			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo1000877") });

			return new ActionUIDes[] { mainMenu };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			return new ReportRowFormatCommand(getPlugIn());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			return new Object[] { container };
		}

	}

	private class ReportDataFormatCommand extends UfoCommand {

		DimensionPlugin m_plugin = null;

		public ReportDataFormatCommand(DimensionPlugin plugin) {
			m_plugin = plugin;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
		 */
		public void execute(Object[] params) {
			UfoReport m_report = (UfoReport) params[0];
			MultiDimemsionModel model = m_plugin.getModel();

			CellPosition pos = m_report.getCellsModel().getSelectModel().getSelectedArea().getStart();
			DimMemberCombinationVO header = MultiDimensionUtil
					.getHeadersByDataPos(model, pos.getRow(), pos.getColumn());

			if (header != null) {
				Format oldFormat = m_report.getCellsModel().getCellFormat(pos);
				Format newFormat = ReportFormatDialog.getUserSetFormat(m_report, oldFormat, false);
				if (newFormat != null) {
					newFormat.setCellType(TableConstant.CELLTYPE_NUMBER);
					m_plugin.getModel().getFormatSetModel().setCellFormat(header, newFormat);
					m_report.getCellsModel().setCellFormat(pos.getRow(), pos.getColumn(), newFormat);
				}
			}
		}
	}

	/** 数据格式 */
	private class DataFormatExt extends AbsDimActionExt {

		private DimensionPlugin m_dimPlugin = null;

		public DataFormatExt(DimensionPlugin plugin) {
			super(plugin);
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("ubimultidim0039"));
			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo1000877") });

			return new ActionUIDes[] { mainMenu };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			return new ReportDataFormatCommand(getPlugIn());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			return new Object[] { container, m_dimPlugin };
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);
			CellPosition pos = getPlugIn().getReport().getCellsModel().getSelectModel().getSelectedArea().getStart();
			DimMemberCombinationVO header = MultiDimensionUtil.getHeadersByDataPos(getPlugIn().getModel(),
					pos.getRow(), pos.getColumn());
			if (superEnabled && (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_FORMAT)
					&& ((header != null))) {// 格式态的数据单元才可用
				return true;
			}
			return false;
		}

	}

	/** 数据编辑 */
	private class WriteDataExt extends AbsActionExt {
		private DimensionPlugin m_dimPlugin = null;

		private class WriteDataCommand extends UfoCommand {
			public void execute(Object[] params) {
				int currOption = getPlugIn().getOperationState();
				if (currOption == UfoReport.OPERATION_FORMAT) {
					return;
				}
				try {
					getPlugIn().getModel().setDataEditable(true);
				} catch (BusinessException e) {
					MessageDialog.showErrorDlg(getPlugIn().getReport(), null, e.getMessage());
				}
			}
		}

		public WriteDataExt(DimensionPlugin plugin) {
			m_dimPlugin = plugin;
		}

		private DimensionPlugin getPlugIn() {
			return m_dimPlugin;
		}

		public UfoCommand getCommand() {
			return new WriteDataCommand();
		}

		public Object[] getParams(UfoReport container) {
			return null;
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes desc = new ActionUIDes();
			desc.setName(StringResource.getStringResource("ubispreadsheet0006"));
			desc.setPaths(new String[] { StringResource.getStringResource("miufo1003928") });
			// desc.setPopup(true);
			desc.setGroup(StringResource.getStringResource("ubispreadsheet0006"));

			return new ActionUIDes[] { desc };
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);
			if (superEnabled && (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT)) {
				if (getPlugIn().getModel().isDataEditable() == false
						&& MultiDimWriteBackUtil.isPageEditable(m_dimPlugin.getModel())) {
					return true;
				}
			}
			return false;
		}

	}

	/** 数据回写 */
	private class WriteBackDataExt extends AbsActionExt {
		private DimensionPlugin m_dimPlugin = null;

		private class WriteBackDataCommand extends UfoCommand {
			/**
			 * @i18n miufopublic391=保存成功
			 */
			public void execute(Object[] params) {
				int currOption = getPlugIn().getOperationState();
				if (currOption == UfoReport.OPERATION_FORMAT) {
					return;
				}

				// TODO 是否应该先判断有无数据改变
				String msg = getPlugIn().getModel().writeDataBack();
				if (msg != null)
					MessageDialog.showErrorDlg(getPlugIn().getReport(), null, msg);
				else
					MessageDialog.showHintDlg(getPlugIn().getReport(), null, StringResource
							.getStringResource("miufopublic391"));
			}
		}

		public WriteBackDataExt(DimensionPlugin plugin) {
			m_dimPlugin = plugin;
		}

		private DimensionPlugin getPlugIn() {
			return m_dimPlugin;
		}

		public UfoCommand getCommand() {
			return new WriteBackDataCommand();
		}

		public Object[] getParams(UfoReport container) {
			return null;
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes desc = new ActionUIDes();
			desc.setName(StringResource.getStringResource("ubispreadsheet0005"));
			desc.setPaths(new String[] { StringResource.getStringResource("miufo1003928") });
			// desc.setPopup(true);
			desc.setGroup(StringResource.getStringResource("ubispreadsheet0006"));

			return new ActionUIDes[] { desc };
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);

			return superEnabled && (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT)
					&& getPlugIn().getModel().isDataEditable();
		}

	}

	private DimensionPlugin m_dimensionPlugin = null;

	private PageDimNavigationPanel m_pageDimPanel = null;

	// private BINavigationPanel biNavigationPanel = null;

	public DimensionDescriptor(DimensionPlugin plugin) {
		super();
		m_dimensionPlugin = plugin;
		m_dimensionPlugin.addChangeListener(this);
	}

	/*
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getName()
	 */
	public String getName() {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getNote()
	 */
	public String getNote() {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getPluginPrerequisites()
	 */
	public String[] getPluginPrerequisites() {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getExtensions()
	 */
	public IExtension[] getExtensions() {
		IExtension[] mainExt = getMainExtensions();
		IExtension[] popupExt = DimensionPopupMenuDes.getPopupMenuExts(m_dimensionPlugin);

		IExtension[] allExt = new IExtension[mainExt.length + popupExt.length];
		System.arraycopy(mainExt, 0, allExt, 0, mainExt.length);
		System.arraycopy(popupExt, 0, allExt, mainExt.length, popupExt.length);

		return allExt;
	}

	private IExtension[] getMainExtensions() {
		return new IExtension[] { getPageDimNavigationExt(), new SelDimExt(m_dimensionPlugin),
				new AnalyzerExt(m_dimensionPlugin), new AnalyzerDetailExt(m_dimensionPlugin, IAnalyzerSet.TYPE_FILTER),
				new AnalyzerDetailExt(m_dimensionPlugin, IAnalyzerSet.TYPE_LIMITROWS),
				new AnalyzerDetailExt(m_dimensionPlugin, IAnalyzerSet.TYPE_SORT),
				new AnalyzerDetailExt(m_dimensionPlugin, IAnalyzerSet.TYPE_FORMULAR),
				new AnalyzerDetailExt(m_dimensionPlugin, IAnalyzerSet.TYPE_HIDDEN), //new ALARMExt(m_dimensionPlugin),//TODO
				new DrillThroughExt(m_dimensionPlugin),

				new ReportFormatExt(m_dimensionPlugin), new ReportRowFormatExt(m_dimensionPlugin),
				new DataFormatExt(m_dimensionPlugin) ,
				
				new BIReportSaveExt(m_dimensionPlugin),
				new BIReportPreViewExt(m_dimensionPlugin),
				new WriteDataExt(m_dimensionPlugin),
				new WriteBackDataExt(m_dimensionPlugin), 
		
//				new PrintSettingExt(m_dimensionPlugin.getReport()),//页面设置
//	       		 new PrintPreViewExt(m_dimensionPlugin.getReport()),//打印预览		
//	       		new PrintExt(m_dimensionPlugin.getReport())//打印
				};
	}

	/*
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
	 */
	public String getHelpNode() {
		return null;
	}

	/** 页维度字段导航 */
	private INavigationExt getPageDimNavigationExt() {
		INavigationExt ext = new BINavigationExt(ReportNavPanel.NORTH_NAV, getPageDimPanel());
		return ext;
	}

	private void execSelDim(UfoReport report) {
		// if (m_dimensionPlugin.SelectQuery()) {
		// m_dimensionPlugin.getModel().addChangeListener(getPageDimPanel());
		// } else {
		SelDimSetDialog dlg = m_dimensionPlugin.getSelDimDlg();
		String userID = ((BIContextVO) report.getContextVo()).getCurUserID();
		dlg.setSelModel(m_dimensionPlugin.getModel().getSelDimModel(), userID);
		if (dlg.showModal() == UIDialog.ID_OK) {
			SelDimModel selDim = dlg.getSelModel();
			m_dimensionPlugin.getModel().setSelDimModel(selDim);

			try {
				m_dimensionPlugin.getModel().setOperationState(report.getOperationState(),
						((BIContextVO) report.getContextVo()).getReportPK(), userID);
			} catch (BIException ex) {
				JOptionPane.showMessageDialog(m_dimensionPlugin.getReport(), ex.getMessage());
			}

		}

		m_dimensionPlugin.getModel().removeChangeListener(getPageDimPanel());
		m_dimensionPlugin.getModel().addChangeListener(getPageDimPanel());
		// }
	}

	/**
	 * 页纬度导航面板
	 * 
	 * @return
	 */
	private PageDimNavigationPanel getPageDimPanel() {
		if (m_pageDimPanel == null) {
			m_pageDimPanel = new PageDimNavigationPanel();

			// 当多维模型中的页纬度设置变化时更新页维度面板
			refreshPageDim();
		}
		return m_pageDimPanel;
	}

	private void refreshPageDim() {

		m_dimensionPlugin.getModel().removeChangeListener(getPageDimPanel());
		m_dimensionPlugin.getModel().addChangeListener(getPageDimPanel());

		getPageDimPanel().removeAllPageDim();

		SelDimModel dimModel = m_dimensionPlugin.getModel().getSelDimModel();
		if (dimModel != null) {
			PageDimField[] fields = dimModel.getPageDimfield();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					getPageDimPanel().addPageDim(fields[i]);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		// 监听插件的模型切换事件
		if (evt.getPropertyName().equals(IMultiDimConst.PROPERTY_PLUGIN_MODEL_CHANGED)) {
			refreshPageDim();

		}
	}

}