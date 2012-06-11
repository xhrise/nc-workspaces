package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.insertdelete.DeleteCmd;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.report.sysplugin.insertdelete.DeleteExt;
import com.ufsoft.report.sysplugin.insertdelete.InsertCellCmd;
import com.ufsoft.report.sysplugin.insertdelete.InsertCellExt;
import com.ufsoft.report.sysplugin.insertdelete.InsertCmd;
import com.ufsoft.report.sysplugin.insertdelete.InsertColumnsExt;
import com.ufsoft.report.sysplugin.insertdelete.InsertDeletePlugin;
import com.ufsoft.report.sysplugin.insertdelete.InsertExt;
import com.ufsoft.report.sysplugin.insertdelete.InsertRowsExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.table.event.HeaderModelListener;

/**
 * 分析报表插入行或者删除行插件
 * 
 * @author wangyga 2008-9-2
 * 
 */
public class AnaInsertDeletePlugin extends InsertDeletePlugin {

//	private class AnaInsertCellExt extends InsertCellExt {
//
//		public AnaInsertCellExt(UfoReport rep) {
//			super(rep);
//		}
//
//		@Override
//		public UfoCommand getCommand() {
//
//			return new AnaInsertCellCmd(getReport());
//		}
//
//		@Override
//		public boolean isEnabled(Component focusComp) {
//			return StateUtil.isCellsPane(getReport(), focusComp);
//		}
//
//		private class AnaInsertCellCmd extends InsertCellCmd {
//
//			public AnaInsertCellCmd(UfoReport rep) {
//				super(rep);
//			}
//
//			@Override
//			public void execute(Object[] params) {
//
//				super.execute(params);
//				if (!getAnaRepModel().isFormatState())
//					getAanPlugin().refreshDataModel(false);
//			}
//
//			@Override
//			protected CellsModel getCellsModel() {
//
//				return getAnaRepModel().getFormatModel();
//			}
//
//			@Override
//			protected AreaPosition[] getSelectArea() {
//				return getFormatSelectedAreas();
//			}
//
//		}
//	}

//	private class AnaInsertRowsExt extends InsertRowsExt {
//
//		public AnaInsertRowsExt(UfoReport rep) {
//			super(rep);
//		}
//
//		@Override
//		public UfoCommand getCommand() {
//			return new AnaInsertRowsCmd(getReport(), getAanPlugin());
//		}
//
//		@Override
//		public boolean isEnabled(Component focusComp) {
//			AreaPosition[] areas = getFormatSelectedAreas();
//			CellPosition pos = areas != null && areas.length > 0 ? areas[0].getStart() : null;
//			CellsModel formatModel = getAnaRepModel().getFormatModel();
//			if (pos == null)
//				return true;
//			Cell cell = formatModel.getCellIfNullNew(pos.getRow(), pos.getColumn());
//			if (cell == null)
//				return true;
//			if (cell.getValue() instanceof IChartModel && !getAnaRepModel().isFormatState())
//				return false;
//
//			return StateUtil.isCPane1THeader(null, focusComp)
//					&& getReport().getCellsModel().getSelectModel().getSelectedCol() == null
//					&& !getReport().getCellsModel().getSelectModel().isSelectAll() && isSelectRow();
//		}
//
//		private boolean isSelectRow() {
//			AreaPosition areaSel = getReport().getCellsModel().getSelectModel().getSelectedArea();
//			if (areaSel == null) {
//				return false;
//			}
//			return areaSel.getStart().getColumn() == 0 ? true : false;
//		}
//
//	}

//	private class AnaInsertColumnsExt extends InsertColumnsExt {
//
//		public AnaInsertColumnsExt(UfoReport rep) {
//			super(rep);
//		}
//
//		@Override
//		public UfoCommand getCommand() {
//			return new AnaInsertColumnsCmd(getReport(), getAanPlugin());
//		}
//
//		@Override
//		public boolean isEnabled(Component focusComp) {
//			return StateUtil.isCPane1THeader(null, focusComp)
//					&& getReport().getCellsModel().getSelectModel().getSelectedRow() == null
//					&& !getReport().getCellsModel().getSelectModel().isSelectAll() && isSelectCol();
//		}
//
//		private boolean isSelectCol() {
//			AreaPosition areaSel = getReport().getCellsModel().getSelectModel().getSelectedArea();
//			if (areaSel == null) {
//				return false;
//			}
//			return areaSel.getStart().getRow() == 0 ? true : false;
//
//		}
//
//	}

	private class AnaInsertExt extends InsertExt {

		public AnaInsertExt(UfoReport report) {
			super(report);
			// TODO Auto-generated constructor stub
		}
        
		@Override
		public UfoCommand getCommand() {
			// TODO Auto-generated method stub
			return new AnaInsertCmd(getReport());
		}

		@Override
		protected CellsModel getCellsModel() {
			// TODO Auto-generated method stub
			return getAnaRepModel().getFormatModel();
		}

		@Override
		protected AreaPosition getSelectedArea() {
			AreaPosition[] areas = getFormatSelectedAreas();
			if (areas.length > 0) {
				return areas[0];
			}
			return null;
		}

		@Override
		protected AreaPosition[] getSelectedAreas() {

			return getFormatSelectedAreas();
		}

		@Override
		protected int[] getSelectedCols() {
			if (getAnaRepModel().getCellsModel().getSelectModel()
					.getSelectedCol() != null) {
				AnaReportModel anaRepModel = getAnaRepModel();
				CellsModel fmtModel = anaRepModel.getFormatModel();
				int[] iSelectCols = fmtModel.getSelectModel().getSelectedCol();
				if (!getAnaRepModel().isFormatState()) {
					CellsModel dataModel = anaRepModel.getDataModel();
					AreaPosition[] selectAreas = anaRepModel.getFormatAreas(
							dataModel, dataModel.getSelectModel()
									.getSelectedAreas());
					if (selectAreas == null || selectAreas.length == 0)
						return new int[] { 0 };
					ArrayList<Integer> selectColsList = new ArrayList<Integer>();
					for (AreaPosition area : selectAreas) {
						CellPosition[] cellPos = area.split();
						if (cellPos == null || cellPos.length == 0)
							return new int[] { 0 };
						for (CellPosition pos : cellPos) {
							int iCol = pos.getColumn();
							if (!selectColsList.contains(iCol))
								selectColsList.add(iCol);
						}
					}
					Integer[] intCols = selectColsList.toArray(new Integer[0]);
					iSelectCols = new int[intCols.length];
					for (int i = 0; i < intCols.length; i++)
						iSelectCols[i] = intCols[i].intValue();
				}
				return iSelectCols;
			}
			return null;
		}

		@Override
		protected int[] getSelectedRows() {
			if (getAnaRepModel().getCellsModel().getSelectModel()
					.getSelectedRow() != null) {
				AnaReportModel anaRepModel = getAnaRepModel();
				CellsModel fmtModel = anaRepModel.getFormatModel();
				int[] iSelectRows = fmtModel.getSelectModel().getSelectedRow();
				if (!getAnaRepModel().isFormatState()) {
					CellsModel dataModel = anaRepModel.getDataModel();
					AreaPosition[] selectAreas = anaRepModel.getFormatAreas(
							dataModel, dataModel.getSelectModel()
									.getSelectedAreas());
					if (selectAreas == null || selectAreas.length == 0)
						return new int[] { 0 };
					ArrayList<Integer> selectColsList = new ArrayList<Integer>();
					for (AreaPosition area : selectAreas) {
						CellPosition[] cellPos = area.split();
						if (cellPos == null || cellPos.length == 0)
							return new int[] { 0 };
						for (CellPosition pos : cellPos) {
							int iRow = pos.getRow();
							if (!selectColsList.contains(iRow))
								selectColsList.add(iRow);
						}
					}
					Integer[] intRows = selectColsList.toArray(new Integer[0]);
					iSelectRows = new int[intRows.length];
					for (int i = 0; i < intRows.length; i++)
						iSelectRows[i] = intRows[i].intValue();

				}
				return iSelectRows;
			}
			return null;
		}
		
	}
	
	private class AnaInsertCmd extends InsertCmd {

		public AnaInsertCmd(UfoReport rep) {
			super(rep);
			// TODO Auto-generated constructor stub
		}
        
		
		@Override
		public void execute(Object[] params) {
			if (!getAnaRepModel().isFormatState() && !checkHeaderModelListener())
				getCellsModel().getRowHeaderModel().addHeaderModelListener(getAanPlugin().getFormatListener());
			super.execute(params);
			if (!getAnaRepModel().isFormatState())
				getAanPlugin().refreshDataModel(false);
		}


		@Override
		protected CellsModel getCellsModel() {
			return getAnaRepModel().getFormatModel();
		}
		
		
	}
	private class AnaDeleteExt extends DeleteExt {

		public AnaDeleteExt(UfoReport rep) {
			super(rep);
		}

		@Override
		public UfoCommand getCommand() {
			return new AnaDeleteCmd(getReport());
		}

		@Override
		public boolean isEnabled(Component focusComp) {
			return StateUtil.isCPane1THeader(getReport(), focusComp);
		}

		@Override
		public Object[] getParams(UfoReport container) {
			return super.getParams(container);
		}

		@Override
		protected CellsModel getCellsModel() {

			return super.getCellsModel();
		}

		@Override
		protected AreaPosition getSelectedArea() {
			AreaPosition[] areas = getFormatSelectedAreas();
			if (areas.length > 0) {
				return areas[0];
			}
			return null;
		}

		@Override
		protected AreaPosition[] getSelectAreas() {

			return getFormatSelectedAreas();
		}

		@Override
		protected int[] getSelectedCols() {
			AnaReportModel anaRepModel = getAnaRepModel();
			CellsModel fmtModel = anaRepModel.getFormatModel();
			int[] iSelectCols = fmtModel.getSelectModel().getSelectedCol();
			if (!getAnaRepModel().isFormatState()) {
				CellsModel dataModel = anaRepModel.getDataModel();
				AreaPosition[] selectAreas = anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel()
						.getSelectedAreas());
				if (selectAreas == null || selectAreas.length == 0)
					return new int[] { 0 };
				ArrayList<Integer> selectColsList = new ArrayList<Integer>();
				for (AreaPosition area : selectAreas) {
					CellPosition[] cellPos = area.split();
					if (cellPos == null || cellPos.length == 0)
						return new int[] { 0 };
					for (CellPosition pos : cellPos) {
						int iCol = pos.getColumn();
						if (!selectColsList.contains(iCol))
							selectColsList.add(iCol);
					}
				}
				Integer[] intCols = selectColsList.toArray(new Integer[0]);
				iSelectCols = new int[intCols.length];
				for (int i = 0; i < intCols.length; i++)
					iSelectCols[i] = intCols[i].intValue();
			}
			return iSelectCols;
		}

		@Override
		protected int[] getSelectedRows() {
			AnaReportModel anaRepModel = getAnaRepModel();
			CellsModel fmtModel = anaRepModel.getFormatModel();
			int[] iSelectRows = fmtModel.getSelectModel().getSelectedRow();
			if (!getAnaRepModel().isFormatState()) {
				CellsModel dataModel = anaRepModel.getDataModel();
				AreaPosition[] selectAreas = anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel()
						.getSelectedAreas());
				if (selectAreas == null || selectAreas.length == 0)
					return new int[] { 0 };
				ArrayList<Integer> selectColsList = new ArrayList<Integer>();
				for (AreaPosition area : selectAreas) {
					CellPosition[] cellPos = area.split();
					if (cellPos == null || cellPos.length == 0)
						return new int[] { 0 };
					for (CellPosition pos : cellPos) {
						int iRow = pos.getRow();
						if (!selectColsList.contains(iRow))
							selectColsList.add(iRow);
					}
				}
				Integer[] intRows = selectColsList.toArray(new Integer[0]);
				iSelectRows = new int[intRows.length];
				for (int i = 0; i < intRows.length; i++)
					iSelectRows[i] = intRows[i].intValue();

			}
			return iSelectRows;
		}

		private class AnaDeleteCmd extends DeleteCmd {

			public AnaDeleteCmd(UfoReport rep) {
				super(rep);

			}

			@Override
			public void execute(Object[] params) {
				if ((params == null) || (params.length == 0))
					return;

//				try {
					int deleteMethod = ((Integer) params[0]).intValue();
					CellsModel cModel = getCellsModel();
					cModel.getRowHeaderModel().addHeaderModelListener(getHeaderModelListener());
					cModel.getRowHeaderModel().addHeaderModelListener(getAanPlugin().getFormatListener());
					cModel.getColumnHeaderModel().addHeaderModelListener(getAanPlugin().getFormatListener());
					if (cModel == null)
						return;
                    boolean isOk=true;
					switch (deleteMethod) {
					case DeleteInsertDialog.CELL_MOVE_LEFT:// 考虑动态区域,组合单元.
						AreaPosition aimArea1 = (AreaPosition) params[1];
						isOk=deleteCells(aimArea1, DeleteInsertDialog.CELL_MOVE_LEFT);
						break;
					case DeleteInsertDialog.CELL_MOVE_UP:
						AreaPosition aimArea2 = (AreaPosition) params[1];
						isOk=deleteCells(aimArea2, DeleteInsertDialog.CELL_MOVE_UP);
						break;
					case DeleteInsertDialog.DELETE_ROW:
						int[] selectRow = (int[]) params[1];
						if ((selectRow != null) && (selectRow.length > 0)) {
							Arrays.sort(selectRow);
							// 目前不支持多选区域。
							cModel.getRowHeaderModel().removeHeader(selectRow[0], selectRow.length);
							// 清除选中区域
							cModel.getSelectModel().clear();
						}

						break;
					case DeleteInsertDialog.DELTE_COLUMN:
						int[] selectCol = (int[]) params[1];
						if ((selectCol != null) && (selectCol.length > 0)) {
							Arrays.sort(selectCol);
							cModel.getColumnHeaderModel().removeHeader(selectCol[0], selectCol.length);
							// 清除选中区域
							cModel.getSelectModel().clear();
						}
						break;
					}
//				} 
//				 catch (Exception e) {
//					AppDebug.debug(e);
//					
////					IUFOLogger.getLogger(this).fatal(MultiLang.getString("uiuforep0000861"));// 删除失败
////					UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000861"), getReport(), e);// 删除失败
//				}
			    if(isOk){
			    	getReport().setFocusComp(getReport().getTable().getCells());
					if (!getAnaRepModel().isFormatState())
						getAanPlugin().refreshDataModel(true);
			    }
				
			}

			private boolean deleteCells(AreaPosition aimArea, int deleteType) {
				if (aimArea == null)
					return false;
				UserUIEvent event = new UserUIEvent(this, UserUIEvent.DELETECELL, new Integer(deleteType), aimArea);
				UFOTable table = getReport().getTable();
				if (table.checkEvent(event)) {// 检查动态区域,组合单元
					table.fireEvent(event);// 删除组合单元
					CellsModel cellsModel = getCellsModel();
					table.clear(UFOTable.CELL_ALL);
					CellPosition newAnchorPos = aimArea.getStart();
					AreaPosition toMoveArea = DeleteCmd.getToMoveArea(aimArea, deleteType, cellsModel);
					cellsModel.moveCells(toMoveArea, newAnchorPos);
					return true;
				}
				return false;
			}

			private CellsModel getCellsModel() {
				return getAnaRepModel().getFormatModel();
			}

			private HeaderModelListener getHeaderModelListener() {
				return (HeaderModelListener) getReport().getPluginManager().getPlugin(AnaExAreaPlugin.class.getName());
			}
		}

	}
 
	/**
	 * 检查是否有AnaFormatModelListener，如果没有就添加上
	 * 
	 * @return
	 */
	private boolean checkHeaderModelListener() {
		AnaReportModel anaModel = getAnaRepModel();
		CellsModel formatModel = anaModel.getFormatModel();
		HeaderModelListener[] headerListeners = formatModel.getRowHeaderModel().getListenerList();
		if (headerListeners == null || headerListeners.length == 0)
			return false;
		for (HeaderModelListener listener : headerListeners) {
			if (listener instanceof AnaFormatModelListener)
				return true;
		}

		return false;
	}

	private AnaReportModel getAnaRepModel() {
		return getAanPlugin().getModel();
	}

	private AnaReportPlugin getAanPlugin() {
		return (AnaReportPlugin) getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}

	private AreaPosition[] getFormatSelectedAreas() {
		AnaReportModel anaRepModel = getAnaRepModel();
		AreaPosition[] selectAreas = anaRepModel.getFormatModel().getSelectModel().getSelectedAreas();
		if (!anaRepModel.isFormatState()) {
			CellsModel dataModel = anaRepModel.getDataModel();
			selectAreas = anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
		}
		return selectAreas;
	}

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(AnaInsertDeletePlugin.this) {

			protected IExtension[] createExtensions() {
				ICommandExt extFillExt = new AbsActionExt() {
					public UfoCommand getCommand() {
						return null;
					}

					@Override
					public Object[] getParams(UfoReport container) {
						return null;
					}

					@Override
					public ActionUIDes[] getUIDesArr() {
						ActionUIDes uiDes = new ActionUIDes();
						uiDes.setName(MultiLang.getString("uiuforep0000877"));
						uiDes.setPaths(new String[] { MultiLang.getString("edit") });
						uiDes.setDirectory(true);
						uiDes.setGroup("insertAndFill");
						return new ActionUIDes[] { uiDes };
					}
				};
//				ICommandExt extInsertCell = new InsertCellExt(getReport());// 插入单元格
//				ICommandExt extInsertRows = new AnaInsertRowsExt(getReport());// 插入行
//				ICommandExt extInsertCols = new AnaInsertColumnsExt(getReport());// 插入列
				ICommandExt extInsert = new AnaInsertExt(getReport());
				ICommandExt extDelete = new AnaDeleteExt(getReport());// 删除
				return new IExtension[] { extFillExt,
//						extInsertCell, extInsertRows, extInsertCols, 
						extInsert,
						extDelete };
			}

		};
	}

}
