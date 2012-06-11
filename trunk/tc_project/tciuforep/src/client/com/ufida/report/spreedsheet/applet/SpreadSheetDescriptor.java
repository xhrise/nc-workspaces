package com.ufida.report.spreedsheet.applet;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;
import nc.vo.bi.dataauth.DataAuthException;
import nc.vo.bi.integration.dimension.MeasureVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.applet.SelectQueryModelDlg;
import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.MultiReportFormatUtil;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufida.report.rep.applet.BINavigationExt;
import com.ufida.report.rep.applet.BINavigationPanel;
import com.ufida.report.rep.applet.BIReportPreViewExt;
import com.ufida.report.rep.applet.BIReportSaveExt;
import com.ufida.report.rep.applet.PageDimNavigationPanel;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportUtil;
import com.ufida.report.rep.model.MetaDataVOSelection;
import com.ufida.report.spreedsheet.model.ISpreadSheetConstants;
import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufida.report.spreedsheet.model.SpreadQueryCache;
import com.ufida.report.spreedsheet.model.SpreadSheetModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.combinecell.CombineCellCmd;
import com.ufsoft.report.sysplugin.print.HeaderFooterExt;
import com.ufsoft.report.sysplugin.print.PrintExt;
import com.ufsoft.report.sysplugin.print.PrintPreViewExt;
import com.ufsoft.report.sysplugin.print.PrintSettingExt;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * 
 * @author zzl 2005-4-27
 */
public class SpreadSheetDescriptor implements IPluginDescriptor,
		PropertyChangeListener, UIDialogListener {

	/** ���ݱ༭ */
	private static class WriteDataExt extends AbsActionExt {
		private SpreadSheetPlugin m_dimPlugin = null;

		private class WriteDataCommand extends UfoCommand {
			public void execute(Object[] params) {
				int currOption = getPlugIn().getOperationState();
				if (currOption == UfoReport.OPERATION_FORMAT) {
					return;
				}

				try {
					getPlugIn().getModel().setDataEdit(true);
				} catch (DataAuthException ex) {
					MessageDialog.showErrorDlg(getPlugIn().getReport(), null,
							ex.getMessage());
				}
			}
		}

		public WriteDataExt(SpreadSheetPlugin plugin) {
			m_dimPlugin = plugin;
		}

		private SpreadSheetPlugin getPlugIn() {
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
			desc
					.setName(StringResource
							.getStringResource("ubispreadsheet0006"));
			desc.setPaths(new String[] { StringResource
					.getStringResource("miufo1003928") });
			// desc.setPopup(true);

			return new ActionUIDes[] { desc };
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);
			return superEnabled
					&& (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT);

		}

	}

	/** ���ݻ�д */
	private static class WriteBackDataExt extends AbsActionExt {
		private SpreadSheetPlugin m_dimPlugin = null;

		private class WriteBackDataCommand extends UfoCommand {
			/**
			 * @i18n miufopublic391=����ɹ�
			 */
			public void execute(Object[] params) {
				int currOption = getPlugIn().getOperationState();
				if (currOption == UfoReport.OPERATION_FORMAT) {
					return;
				}

				// TODO �Ƿ�Ӧ�����ж��������ݸı�
				String msg = getPlugIn().getModel().writeDataBack();
				if (msg != null)
					MessageDialog.showErrorDlg(getPlugIn().getReport(), null,
							msg);
				else
					MessageDialog.showHintDlg(getPlugIn().getReport(), null,
							StringResource.getStringResource("miufopublic391"));
			}
		}

		public WriteBackDataExt(SpreadSheetPlugin plugin) {
			m_dimPlugin = plugin;
		}

		private SpreadSheetPlugin getPlugIn() {
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
			desc
					.setName(StringResource
							.getStringResource("ubispreadsheet0005"));
			desc.setPaths(new String[] { StringResource
					.getStringResource("miufo1003928") });
			// desc.setPopup(true);

			return new ActionUIDes[] { desc };
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);

			return superEnabled
					&& (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT)
					&& getPlugIn().getModel().isEdite();
		}

	}

	/** �����ѯ */
	private class SelQueryExt extends AbsActionExt {

		private class SelQueryCommand extends UfoCommand {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
			 */
			public void execute(Object[] params) {
				UfoReport m_report = (UfoReport) params[0];
				execAddQuery(m_report);
			}
		}

		private SpreadSheetPlugin m_dimPlugin = null;

		public SelQueryExt(SpreadSheetPlugin plugin) {
			super();
			m_dimPlugin = plugin;
		}

		public UfoCommand getCommand() {
			return new SelQueryCommand();
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
			mainMenu.setName(StringResource
					.getStringResource("ubispreadsheet0002"));
			mainMenu.setGroup(StringResource
					.getStringResource("ubispreadsheet0002"));
			mainMenu.setPaths(new String[] { StringResource
					.getStringResource("ubispreadsheet0001") });

			ActionUIDes popupMenu = new ActionUIDes();
			popupMenu.setName(StringResource
					.getStringResource("ubispreadsheet0002"));
			popupMenu.setGroup(StringResource
					.getStringResource("ubispreadsheet0002"));
			popupMenu.setPopup(true);

			return new ActionUIDes[] { mainMenu, popupMenu };
		}
	}

	/** ɾ����ѯ */
	private class RemoveQueryExt extends AbsActionExt {

		private class RemoveQueryCommand extends UfoCommand {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
			 */
			public void execute(Object[] params) {
				UfoReport m_report = (UfoReport) params[0];
				execRemoveQuery(m_report);
			}
		}

		private SpreadSheetPlugin m_dimPlugin = null;

		public RemoveQueryExt(SpreadSheetPlugin plugin) {
			super();
			m_dimPlugin = plugin;
		}

		public UfoCommand getCommand() {
			return new RemoveQueryCommand();
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
			mainMenu.setName(StringResource
					.getStringResource("ubispreadsheet0003"));
			mainMenu.setGroup(StringResource
					.getStringResource("ubispreadsheet0003"));
			mainMenu.setPaths(new String[] { StringResource
					.getStringResource("ubispreadsheet0001") });

			ActionUIDes popupMenu = new ActionUIDes();
			popupMenu.setName(StringResource
					.getStringResource("ubispreadsheet0003"));
			popupMenu.setGroup(StringResource
					.getStringResource("ubispreadsheet0003"));
			popupMenu.setPopup(true);

			return new ActionUIDes[] { mainMenu, popupMenu };
		}
	}

	// /** ѡ���ѯ���뵽���ӱ�� */
	// private class AddQueryAreaExt extends AbsActionExt {
	//
	// private class AddQueryAreaCommand extends UfoCommand {
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	// */
	// public void execute(Object[] params) {
	// try {
	// UfoReport m_report = (UfoReport) params[0];
	// execSelQueryArea(m_report);
	// } catch (Exception ex) {
	// AppDebug.debug(ex);
	// }
	// }
	// }
	//
	// private SpreadSheetPlugin m_dimPlugin = null;
	//
	// public AddQueryAreaExt(SpreadSheetPlugin plugin) {
	// super();
	// m_dimPlugin = plugin;
	// }
	//
	// public UfoCommand getCommand() {
	// return new AddQueryAreaCommand();
	// }
	//
	// public Object[] getParams(UfoReport container) {
	// return new Object[] { container, m_dimPlugin };
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	// */
	// public ActionUIDes[] getUIDesArr() {
	// ActionUIDes mainMenu = new ActionUIDes();
	// mainMenu.setName(SpreadSheetResource.MENU_SEL_QUERY_AREA);
	// mainMenu.setGroup(SpreadSheetResource.MENU_SEL_QUERY_AREA);
	// mainMenu.setPaths(new String[] { SpreadSheetResource.MENU_MAIN });
	//
	// ActionUIDes popupMenu = new ActionUIDes();
	// popupMenu.setName(SpreadSheetResource.MENU_SEL_QUERY_AREA);
	// popupMenu.setGroup(SpreadSheetResource.MENU_SEL_QUERY_AREA);
	// popupMenu.setPopup(true);
	//
	// return new ActionUIDes[] { mainMenu, popupMenu };
	// }
	// }

	// /** ѡ��ĳ����ѯָ����뵽���ӱ�� */
	// private class AddQueryMeasureExt extends AbsActionExt {
	//
	// private class AddQueryMeasureCommand extends UfoCommand {
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	// */
	// public void execute(Object[] params) {
	// try {
	// UfoReport m_report = (UfoReport) params[0];
	// execSelQueryMeasure(m_report, m_dimPlugin);
	// } catch (Exception ex) {
	// AppDebug.debug(ex);
	// }
	// }
	// }
	//
	// private SpreadSheetPlugin m_dimPlugin = null;
	//
	// public AddQueryMeasureExt(SpreadSheetPlugin plugin) {
	// super();
	// m_dimPlugin = plugin;
	// }
	//
	// public UfoCommand getCommand() {
	// return new AddQueryMeasureCommand();
	// }
	//
	// public Object[] getParams(UfoReport container) {
	// return new Object[] { container, m_dimPlugin };
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	// */
	// public ActionUIDes[] getUIDesArr() {
	// ActionUIDes mainMenu = new ActionUIDes();
	// mainMenu.setName(SpreadSheetResource.MENU_SEL_QUERY_MEASURE);
	// mainMenu.setGroup(SpreadSheetResource.MENU_SEL_QUERY_MEASURE);
	// mainMenu.setPaths(new String[] { SpreadSheetResource.MENU_MAIN });
	//
	// ActionUIDes popupMenu = new ActionUIDes();
	// popupMenu.setName(SpreadSheetResource.MENU_SEL_QUERY_MEASURE);
	// popupMenu.setGroup(SpreadSheetResource.MENU_SEL_QUERY_MEASURE);
	// popupMenu.setPopup(true);
	//
	// return new ActionUIDes[] { mainMenu, popupMenu };
	// }
	// }

	/** ��ѯ��ָ���ֶε���ק���� */
	private class QueryMeasureDragTarget implements DropTargetListener {

		private SpreadSheetPlugin m_plugin = null;

		private UfoReport m_report = null;

		public QueryMeasureDragTarget(UfoReport report,
				SpreadSheetPlugin spreadPlugin) {
			m_plugin = spreadPlugin;
			m_report = report;

			@SuppressWarnings("unused")
			DropTarget dropTarget = new DropTarget(
					report.getTable().getCells(),
					java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE, this);

		}

		public void dragEnter(DropTargetDragEvent dtde) {
		}

		public void dragOver(DropTargetDragEvent dtde) {
		}

		public void dropActionChanged(DropTargetDragEvent dtde) {
		}

		public void drop(DropTargetDropEvent dtde) {
			try {

				Transferable tr = dtde.getTransferable();
				QueryModelVO qm = null;
				MetaDataVO md = null;
				dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
				if (tr
						.isDataFlavorSupported(MetaDataVOSelection.QueryModelVOFlavor)) {
					qm = (QueryModelVO) tr
							.getTransferData(MetaDataVOSelection.QueryModelVOFlavor);
				}
				if (tr
						.isDataFlavorSupported(MetaDataVOSelection.MetaDataVOFlavor)) {
					md = (MetaDataVO) tr
							.getTransferData(MetaDataVOSelection.MetaDataVOFlavor);

				}
				if (qm == null) {
					return;
				}
				CellsPane cellsPane = m_report.getTable().getCells();
				int row = cellsPane.rowAtPoint(dtde.getLocation());
				int col = cellsPane.columnAtPoint(dtde.getLocation());
				CellPosition cellPos = CellPosition.getInstance(row, col);
				if (md == null) {// �����ѯ��������
					execSelQueryArea(m_report, qm, cellPos);
				} else {// ����ָ������
					execSelQueryMeasure(m_report, m_plugin, md, cellPos);
				}

			} catch (InvalidDnDOperationException e) {
				return;
			} catch (Exception e) {
				AppDebug.debug(e);// @devTools AppDebug.debug(e);
				dtde.rejectDrop();
			}
		}

		public void dragExit(DropTargetEvent dte) {
		}
	}

	private SpreadSheetPlugin m_spreadPlugin = null;

	private PageDimNavigationPanel m_pageDimPanel = null;

	private BINavigationPanel m_queryPanel = null;

	public SpreadSheetDescriptor(SpreadSheetPlugin plugin) {
		super();
		m_spreadPlugin = plugin;
		m_spreadPlugin.addChangeListener(this);

	}

	void applyQueryMeasureDragTarget() {
		@SuppressWarnings("unused")
		QueryMeasureDragTarget dragTarget = new QueryMeasureDragTarget(
				m_spreadPlugin.getReport(), m_spreadPlugin);
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
			return new IExtension[] { getPageDimNavigationExt(),
					getQueryNavigationExt(),
					new BIReportSaveExt(m_spreadPlugin),
					new BIReportPreViewExt(m_spreadPlugin),
					new WriteDataExt(m_spreadPlugin),
					new WriteBackDataExt(m_spreadPlugin),
					new SelQueryExt(m_spreadPlugin),
					new RemoveQueryExt(m_spreadPlugin),
//					new PrintSettingExt(m_spreadPlugin.getReport()),// ҳ������
//					new PrintPreViewExt(m_spreadPlugin.getReport()),// ��ӡԤ��
//					new PrintExt(m_spreadPlugin.getReport())// ��ӡ

			};
	}

	/*
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
	 */
	public String getHelpNode() {
		return null;
	}

	/** ҳά���ֶε��� */
	private INavigationExt getPageDimNavigationExt() {
		INavigationExt ext = new BINavigationExt(ReportNavPanel.NORTH_NAV,
				getPageDimPanel());
		return ext;
	}

	/** ��ѯ��ʾ��嵼�� */
	private INavigationExt getQueryNavigationExt() {
		INavigationExt ext = new BINavigationExt(ReportNavPanel.WEST_NAV,
				getQueryPanel());
		return ext;
	}

	private void execAddQuery(UfoReport report) {

		SelectQueryModelDlg dlg = m_spreadPlugin.getAddQueryDlg();
		String userID = ((BIContextVO) report.getContextVo()).getCurUserID();
		dlg.show();
		if (dlg.getResult() == UfoDialog.ID_OK) {
			Object[] selModels = dlg.getSelQueryModels();
			ArrayList<Object> al_vos = new ArrayList<Object>();
			String[] queryIDs = m_spreadPlugin.getModel().getQueryIDs();
			int alLength = queryIDs == null ? 0 : queryIDs.length;
			for (int i = 0; i < selModels.length; i++) {
				boolean alreadySel = false;
				for (int j = 0; j < alLength; j++) {
					if (queryIDs[j].equals(((QueryModelVO) selModels[i])
							.getPrimaryKey())) {
						alreadySel = true;
						break;
					}
				}

				if (!alreadySel)
					al_vos.add(selModels[i]);
			}
			String[] newQueryIDs = new String[alLength + al_vos.size()];
			if (alLength > 0)
				System.arraycopy(queryIDs, 0, newQueryIDs, 0, alLength);

			for (int i = 0; i < al_vos.size(); i++) {
				QueryModelVO queryModel = (QueryModelVO) al_vos.get(i);
				newQueryIDs[alLength + i] = queryModel.getPrimaryKey();
				MetaDataVO[] flds = QueryModelSrv.getSelectFlds(queryModel
						.getID());
				getQueryPanel().addFieldNode(queryModel,
						BaseReportUtil.convertMetaDataVOToRepField(queryModel
								.getID(), flds));
			}

			getQueryPanel().getQueryModelTree().expandRow(0);

			m_spreadPlugin.getModel().setQueryIDs(newQueryIDs);
			m_spreadPlugin
					.getModel()
					.setOperationState(
							report.getOperationState(),
							((BIContextVO) report.getContextVo()).getReportPK(),
							userID);

		}
	}

	private void execRemoveQuery(UfoReport report) {

		QueryModelVO selQuery = getSelectedQuery();
		if (selQuery == null)
			return;

		if (MessageDialog.showYesNoDlg(report, null, StringResource
				.getStringResource("mbispreadsheet0003")
				+ selQuery.getQueryname()) == UIDialog.ID_YES) {

			if (isUsed(report.getCellsModel(), selQuery.getPrimaryKey())) {
				MessageDialog.showErrorDlg(report, null, StringResource
						.getStringResource("mbispreadsheet0002"));
				return;
			}

			// ɾ�����ϵĽڵ�
			DefaultMutableTreeNode selNode = getSelectedNode();
			((DefaultTreeModel) getQueryPanel().getQueryModelTree().getModel())
					.removeNodeFromParent(selNode);
			getQueryPanel().getQueryModelTree().expandRow(0);

			// ��Ҫɾ��ģ���еĲ�ѯ
			String[] queryIDs = m_spreadPlugin.getModel().getQueryIDs();
			int nIndex = -1;
			for (int i = 0; i < queryIDs.length; i++) {
				if (queryIDs[i].equals(selQuery.getPrimaryKey())) {
					nIndex = i;
					break;
				}
			}
			if (nIndex > -1) {
				String[] strNewIDs = new String[queryIDs.length - 1];
				System.arraycopy(queryIDs, 0, strNewIDs, 0, nIndex);
				if (nIndex < strNewIDs.length) {
					System.arraycopy(queryIDs, nIndex + 1, strNewIDs, nIndex,
							strNewIDs.length - nIndex);
				}
				m_spreadPlugin.getModel().setQueryIDs(strNewIDs);
				if (strNewIDs.length == 0) {
					// ɾ��ģ���е�ҳά���ֶ�
					getPageDimPanel().removeAllPageDim();
					getPageDimPanel().repaint();
				}
			}
		}
	}

	private DefaultMutableTreeNode getSelectedNode() {
		TreePath selPath = getQueryPanel().getQueryModelTree()
				.getSelectionPath();
		if (selPath == null)
			return null;

		DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selPath
				.getLastPathComponent();

		return selNode;
	}

	private QueryModelVO getSelectedQuery() {
		DefaultMutableTreeNode selNode = getSelectedNode();
		if (selNode == null)
			return null;

		QueryModelVO selQuery = null;
		if (selNode.getUserObject() instanceof MetaDataVO) {
			DefaultMutableTreeNode queryNode = (DefaultMutableTreeNode) selNode
					.getParent();
			selQuery = (QueryModelVO) queryNode.getUserObject();
		} else if (selNode.getUserObject() instanceof QueryModelVO) {
			selQuery = (QueryModelVO) selNode.getUserObject();
		}
		return selQuery;
	}

	private boolean isUsed(CellsModel cells, String queryID) {
		if (queryID == null || queryID.length() == 0)
			return false;
		// ��鵥Ԫ�����Ƿ��Ѿ������˸ò�ѯ
		int rows = cells.getRowNum();
		int cols = cells.getColNum();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell cell = cells.getCell(i, j);
				if (cell != null) {
					SpreadCellPropertyVO cellVO = (SpreadCellPropertyVO) cell
							.getExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP);
					if (cellVO != null && cellVO.getQueryID() != null
							&& cellVO.getQueryID().equals(queryID))
						return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	private void execSelQueryArea(UfoReport report, QueryModelVO qm,
			CellPosition selPos) {

		String[] queryIDs = m_spreadPlugin.getModel().getQueryIDs();
		if (queryIDs == null || queryIDs.length == 0)
			return;

		SelQueryAreaDlg dlg = m_spreadPlugin.getSelQueryAreaDlg();
		String userID = ((BIContextVO) report.getContextVo()).getCurUserID();
		QueryModelVO selQuery = qm;// getSelectedQuery();
		// if (selQuery == null) ��ǰû��ѡ�еĲ�ѯҲû�й�ϵ
		// return;
		QueryModelVO[] alModels = (QueryModelVO[]) QueryModelSrv
				.getByIDs(queryIDs);
		CellsModel cellsMode = report.getCellsModel();

		dlg.setShowParams(alModels, selQuery, cellsMode, selPos, userID,
				m_spreadPlugin.getModel());

		dlg.removeUIDialogListener(this);
		dlg.addUIDialogListener(this);
		dlg.setModal(false);
		dlg.show();
	}

	private void processQueryArea(SpreadSheetModel spreadModel,
			SelQueryAreaInfo queryInfo) {
		String queryID = queryInfo.getSelQuery().getPrimaryKey();
		SelDimModel dimModel = queryInfo.getSelDimModel();

		// ����ҳά�ȱ仯����Ϣ,����ȡ���ͱ���ѯ��ص�ҳγ�ȳ�Ա
		IMember[] pageMems = processQueryPageDims(dimModel, queryID,
				m_spreadPlugin.getModel());// �͵�ǰ��ѯ��ص�ҳγ�ȳ�Ա

		// ��ָ��λ�ý��е�Ԫ���չ��
		DimMemberCombinationVO[] columns = MultiDimensionUtil
				.getAllCombination(dimModel
						.getSelDimVOs(IMultiDimConst.POS_COLUMN));
		DimMemberCombinationVO[] rows = MultiDimensionUtil
				.getAllCombination(dimModel
						.getSelDimVOs(IMultiDimConst.POS_ROW));
		int rowHeaderLength = rows[0].getMembers().length;
		int colHeaderLength = columns[0].getMembers().length; // �б���

		// ���Ŀ������ĺϲ���Ԫ��
		CellPosition pos = queryInfo.getCellPosition();
		AreaPosition selArea = AreaPosition.getInstance(pos.getRow(), pos
				.getColumn(), rowHeaderLength + columns.length, colHeaderLength
				+ rows.length);
		CombineCellCmd.delCombineCell(selArea, m_spreadPlugin.getReport()
				.getTable());

		// ���������н��紦�ĵ�Ԫ��
		setCellValue(spreadModel, null, pos.getRow(), pos.getColumn(), null,
				null, null, null, false);

		// ��������Ҫ�Ե�Ԫ�Ƿ񳬹�����Χ���ж�
		int nRow, nCol;
		for (int i = 0; i < rows.length; i++) {
			IMember[] headers = rows[i].getMembers();
			for (int j = 0; j < headers.length; j++) {
				nRow = pos.getRow() + colHeaderLength + i;
				nCol = pos.getColumn() + j;
				if (nRow <= Short.MAX_VALUE && nCol <= CellsModel.MAX_COL_NUM) {
					setCellValue(spreadModel, queryID, nRow, nCol, headers[j]
							.getName(), pageMems, null, null, false);
				}
			}
		} // �б���
		for (int i = 0; i < columns.length; i++) {
			IMember[] headers = columns[i].getMembers();
			for (int j = 0; j < headers.length; j++) {
				nRow = pos.getRow() + j;
				nCol = pos.getColumn() + rowHeaderLength + i;
				if (nRow <= Short.MAX_VALUE && nCol <= CellsModel.MAX_COL_NUM) {
					setCellValue(spreadModel, queryID, nRow, nCol, headers[j]
							.getName(), pageMems, null, null, false);
				}
			}
		} // ���ݵ�Ԫ
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < columns.length; j++) {
				nRow = pos.getRow() + colHeaderLength + i;
				nCol = pos.getColumn() + rowHeaderLength + j;
				if (nRow <= Short.MAX_VALUE && nCol <= CellsModel.MAX_COL_NUM) {
					setCellValue(spreadModel, queryID, nRow, nCol, null,
							pageMems, rows[i].getMembers(), columns[j]
									.getMembers(), true);
				}
			}
		} // �Զ�����ϲ���Ԫ��
		AreaPosition[] combineArea = MultiDimensionUtil.getCombineArea(pos
				.getRow(), pos.getColumn(), dimModel
				.getSelDimVOs(IMultiDimConst.POS_ROW), dimModel
				.getSelDimVOs(IMultiDimConst.POS_COLUMN));
		if (combineArea != null) {
			CellsModel cellsModel = spreadModel.getCellsModel();
			for (int i = 0; i < combineArea.length; i++) {
				try {
					cellsModel.setCombinedValue(combineArea[i], cellsModel
							.getCell(combineArea[i].getStart()).getValue());
				} catch (NullPointerException ex) { // process it later
				}
			}
		}

	}

	private IMember[] processQueryPageDims(SelDimModel dimModel,
			String queryID, SpreadSheetModel spreadModel) {
		Hashtable<String, PageDimField> ht_pages = new Hashtable<String, PageDimField>();// ��¼����ģ�͵�ҳά�ȣ������ظ����
		PageDimField[] old_pages = spreadModel.getPageDimFields();
		int pages_len = (old_pages == null) ? 0 : old_pages.length;
		for (int i = 0; i < pages_len; i++) {
			ht_pages.put(old_pages[i].getPageDimID(), old_pages[i]);
		}

		Hashtable<String, String> ht_dimPks = new Hashtable<String, String>();// ��¼����ѯ���õ���γ�����������ڴ���ҳά�ȵ�������ɾ��
		String[] dimPKs = spreadModel.getQueryCache().getDimPKs(queryID);
		for (int i = 0; i < dimPKs.length; i++) {
			ht_dimPks.put(dimPKs[i], dimPKs[i]);
		}

		SelDimensionVO[] pageDims = dimModel
				.getSelDimVOs(IMultiDimConst.POS_PAGE);// �û�ѡ���ҳγ����Ϣ

		IMember[] pageMems = null;// �ͱ���ѯ��ص�ҳά�ȳ�Ա
		ArrayList<PageDimField> al_fields = new ArrayList<PageDimField>();
		if (pageDims != null && pageDims.length > 0) {
			pageMems = new IMember[pageDims.length];
			for (int i = 0; i < pageDims.length; i++) {
				String dimID = pageDims[i].getDimDef().getDimID();
				ht_dimPks.remove(dimID);// ����¼�û�ѡ���˵�γ������
				if (ht_pages.containsKey(dimID)) {// ��ͬҳά��Ҳ�п��ܸı��˳�Ա��Χ��ѡ��
					ht_pages.put(dimID, pageDims[i].createPageDimField());
				} else {
					al_fields.add(pageDims[i].createPageDimField());
				}
				pageMems[i] = pageDims[i].getAllMembers()[0];
			}
		}
		for (int i = pages_len - 1; i >= 0; i--) {
			if (ht_dimPks.containsKey(old_pages[i].getPageDimID()))// ԭ��ҳά���еı���ѯ���õ�γ�ȣ�δ���û�ѡ����ͬɾ��ҳά��
				continue;
			al_fields.add(0, ht_pages.get(old_pages[i].getPageDimID()));
		}

		PageDimField[] newPageFields = null;
		if (al_fields.size() > 0) {
			newPageFields = new PageDimField[al_fields.size()];
			newPageFields = (PageDimField[]) al_fields
					.toArray(new PageDimField[0]);
		}

		// ����ģ�͵�ҳγ����Ϣ��ˢ�½���
		spreadModel.setPageDimFields(newPageFields);
		refreshPageDim();

		return pageMems;

	}

	private void execSelQueryMeasure(UfoReport report,
			SpreadSheetPlugin spreadPlugin, MetaDataVO selMeasure,
			CellPosition aimPos) {

		QueryModelVO selQuery = getSelectedQuery();
		SpreadSheetModel spreadModel = spreadPlugin.getModel();
		SpreadCellPropertyVO cellVO = createCellPropertyVO(selQuery
				.getPrimaryKey(), selMeasure, spreadModel);

		if (cellVO == null)
			return;

		IMember[] mems = cellVO.getMembers();
		PageDimField[] pageDims = spreadModel.getPageDimFields();
		int pageLength = (pageDims == null) ? 0 : pageDims.length;
		boolean hasOtherDims = false;
		if (mems.length > pageLength + 1) {
			hasOtherDims = true;
		} else {
			for (int i = 0; i < mems.length; i++) {
				if (mems[i].isMeasure())
					continue;
				boolean isPage = false;
				for (int j = 0; j < pageDims.length; j++) {
					if (mems[i].getDimID().equals(pageDims[j].getName())) {
						isPage = true;
						break;
					}
				}
				if (!isPage) {
					hasOtherDims = true;
					break;
				}
			}
		}
		if (hasOtherDims) {
			SelMeasureDlg dlg = m_spreadPlugin.getSelMeasureDlg();
			dlg.setCellDimProperty(cellVO, spreadModel);
			if (dlg.showModal() == UIDialog.ID_OK) {
				cellVO = dlg.getCellProperty();
			} else
				cellVO = null;
		}
		if (cellVO != null) {
			Cell cell = report.getCellsModel().getCell(aimPos);
			if (cell == null) {
				cell = new Cell();
				report.getCellsModel().setCell(aimPos.getRow(),
						aimPos.getColumn(), cell);
			}
			Format cellFormat = cell.getFormat();
			if (cellFormat == null)
				cellFormat = new IufoFormat();
			MultiReportFormatUtil.setLine2Format(cellFormat);
//			cellFormat.setBackgroundColor(Color.WHITE);
			spreadModel.getCellsModel().setCellFormat(aimPos.getRow(),
					aimPos.getColumn(), cellFormat);

			cell.setExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP, cellVO);
		}
	}

	private SpreadCellPropertyVO createCellPropertyVO(String queryID,
			MetaDataVO selMetaData, SpreadSheetModel spreadModel) {
		String[] dimPKs = spreadModel.getQueryCache().getDimPKs(queryID);
		if (dimPKs == null || dimPKs.length == 0)
			return null;

		SpreadCellPropertyVO cellVO = new SpreadCellPropertyVO();
		cellVO.setQueryID(queryID);
		cellVO.setIsMeasure(true);

		SpreadQueryCache queryCache = spreadModel.getQueryCache();
		PageDimField[] pageFields = spreadModel.getPageDimFields();
		int pageLength = (pageFields == null) ? 0 : pageFields.length;
		IMember[] mems = new IMember[dimPKs.length];
		for (int i = 0; i < dimPKs.length; i++) {
			if (SpreadQueryCache.isMeasure(dimPKs[i])) {
				MeasureVO measure = new MeasureVO(selMetaData);
				mems[i] = measure;
			} else {
				PageDimField samePage = null;
				for (int j = 0; j < pageLength; j++) {
					if (dimPKs[i].equals(pageFields[j].getPageDimID())) {
						samePage = pageFields[j];
						break;
					}
				}
				if (samePage != null) {
					mems[i] = samePage.getSelectedValue();
				}
				if (mems[i] == null) {
					mems[i] = queryCache.getRootMember(dimPKs[i]);
				}
			}
		}

		cellVO.setMembers(mems);
		return cellVO;

	}

	/**
	 * Ϊ��չ�������õ�Ԫ������,��Ϊָ�������ͷ(����)��������
	 * 
	 * @param spreadModel
	 * @param queryID
	 * @param row
	 * @param col
	 * @param text
	 * @param pageMems
	 * @param rowHeaders
	 * @param colHeaders
	 * @param isData
	 */
	private void setCellValue(SpreadSheetModel spreadModel, String queryID,
			int row, int col, String text, IMember[] pageMems,
			IMember[] rowHeaders, IMember[] colHeaders, boolean isData) {

		Cell cell = spreadModel.getCellsModel().getCell(row, col);
		if (cell == null) {
			cell = new Cell();
			spreadModel.getCellsModel().setCell(row, col, cell);
		}
		Format cellFormat = cell.getFormat();
		if (cellFormat == null)
			cellFormat = new IufoFormat();
		MultiReportFormatUtil.setLine2Format(cellFormat);
//		cellFormat.setBackgroundColor(Color.WHITE);
		spreadModel.getCellsModel().setCellFormat(row, col, cellFormat);

		if (isData) {// ��ָ������
			if (rowHeaders == null || colHeaders == null)
				return;
			// cell.setValue("@");
			cell.getFormat().setCellType(TableConstant.CELLTYPE_NUMBER);

			SpreadCellPropertyVO extProp = new SpreadCellPropertyVO();
			extProp.setQueryID(queryID);
			extProp.setIsMeasure(true);
			Hashtable<String, IMember> ht_mems = new Hashtable<String, IMember>();
			int pageLen = (pageMems == null) ? 0 : pageMems.length;
			for (int i = 0; i < pageLen; i++) {
				ht_mems.put(pageMems[i].getDimID(), pageMems[i]);
			}
			for (int i = 0; i < rowHeaders.length; i++) {
				ht_mems.put(rowHeaders[i].getDimID(), rowHeaders[i]);
			}
			for (int i = 0; i < colHeaders.length; i++) {
				ht_mems.put(colHeaders[i].getDimID(), colHeaders[i]);
			}
			// ���ղ�ѯ������γ�ȵ�˳��,����ָ�굥Ԫ����չ��Ϣ
			String[] dimPKs = spreadModel.getQueryCache().getDimPKs(queryID);
			IMember[] mems = new IMember[dimPKs.length];
			for (int i = 0; i < dimPKs.length; i++) {
				if (ht_mems.containsKey(dimPKs[i])) {
					mems[i] = (IMember) ht_mems.get(dimPKs[i]);
				} else {// δѡ��γ�����¼����Ա
					mems[i] = spreadModel.getQueryCache().getRootMember(
							dimPKs[i]);
				}
			}

			extProp.setMembers(mems);
			cell.setExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP, extProp);

		} else {// ��ָ�굥Ԫ
			cell.getFormat().setCellType(TableConstant.CELLTYPE_DEFAULT);
			// ���ԭ������ܵ�ָ����Ϣ
			cell.removeExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP);
			// ���ñ�������
			cell.setValue(text);

			// Ĭ�ϱ�����ʽ
			cellFormat
					.setBackgroundColor(ISpreadSheetConstants.DefaultColor_of_headers);
			spreadModel.getCellsModel().setCellFormat(row, col, cellFormat);
		}
	}

	/**
	 * ҳγ�ȵ������
	 * 
	 * @return
	 */
	private PageDimNavigationPanel getPageDimPanel() {
		if (m_pageDimPanel == null) {
			m_pageDimPanel = new PageDimNavigationPanel();

			// ����άģ���е�ҳγ�����ñ仯ʱ����ҳά�����
			refreshPageDim();
		}
		return m_pageDimPanel;
	}

	/**
	 * ��ѯ�������
	 * 
	 * @return
	 */
	private BINavigationPanel getQueryPanel() {
		if (m_queryPanel == null) {
			m_queryPanel = new BINavigationPanel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void applyQueryModelDragSource() {
					@SuppressWarnings("unused")
					SpreadQueryTreeDragSource source = new SpreadQueryTreeDragSource(
							getQueryModelTree());
				}
			};

			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) m_queryPanel
					.getQueryModelTree().getModel().getRoot();
			rootNode.setUserObject(StringResource
					.getStringResource("miufo1001590"));

			// ��ѡ���ѯ�仯ʱ���²�ѯ���
			String[] queryIDs = m_spreadPlugin.getModel().getQueryIDs();
			if (queryIDs != null && queryIDs.length > 0) {
				QueryModelVO[] vos = QueryModelSrv.getByIDs(queryIDs);
				if (vos != null && vos.length > 0) {
					ArrayList<QueryModelVO> al_vos = new ArrayList<QueryModelVO>();
					for (int i = 0; i < vos.length; i++) {
						if (vos[i] != null)
							al_vos.add(vos[i]);
					}
					if (al_vos.size() > 0) {
						for (int i = 0; i < al_vos.size(); i++) {
							QueryModelVO queryModel = (QueryModelVO) al_vos
									.get(i);
							MetaDataVO[] flds = QueryModelSrv
									.getSelectFlds(queryModel.getID());
							// TODO Ӧ�ý�˽��γ���ֶι��˵�!
							getQueryPanel().addFieldNode(
									queryModel,
									BaseReportUtil
											.convertMetaDataVOToRepField(queryModel.getID(), flds));

						}
						getQueryPanel().getQueryModelTree().expandRow(0);
					}
				}
			}
		}
		return m_queryPanel;
	}

	/**
	 * ����ҳγ�����
	 * 
	 */
	private void refreshPageDim() {

		m_spreadPlugin.getModel().removeChangeListener(getPageDimPanel());
		m_spreadPlugin.getModel().addChangeListener(getPageDimPanel());

		getPageDimPanel().removeAllPageDim();

		PageDimField[] fields = m_spreadPlugin.getModel().getPageDimFields();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				getPageDimPanel().addPageDim(fields[i]);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		// ���������ģ���л��¼�
		if (evt.getPropertyName().equals(
				IMultiDimConst.PROPERTY_PLUGIN_MODEL_CHANGED)) {
			refreshPageDim();

		}
	}

	public void dialogClosed(UIDialogEvent event) {
		if (event.getSource() == m_spreadPlugin.getSelQueryAreaDlg()) {
			SelQueryAreaDlg dlg = (SelQueryAreaDlg) event.getSource();
			if (dlg.getResult() == UfoDialog.ID_OK) {
				SelQueryAreaInfo queryInfo = dlg.getSelInfo();

				processQueryArea(m_spreadPlugin.getModel(), queryInfo);
				m_spreadPlugin.getModel()
						.setOperationState(
								m_spreadPlugin.getReport().getOperationState(),
								((BIContextVO) m_spreadPlugin.getReport()
										.getContextVo()).getReportPK(),
								m_spreadPlugin.getModel().getReportEnv()
										.getStrUserID());
			}
		}

	}

}