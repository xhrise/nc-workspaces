package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.awt.Container;

import nc.ui.pub.beans.MessageDialog;

import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.applet.AnaFieldTypeExt;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.TopNSetInfo;
import com.ufida.report.crosstable.CrossMeasureTopNSet;
import com.ufida.report.crosstable.CrossTableSet;
import com.ufida.report.crosstable.CrossTopNSet;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

/**
 * 分析表中扩展区域的交叉设置
 * 
 * @author ll
 * 
 */
public class TopNDesignExt extends AbsActionExt {
	public static final String RESID_TOPN_DESIGN = "miufo00422";
	public static final String RESID_TOPN_DESCEDING = "miufo00423";
	public static final String RESID_TOPN_EXTEND = "miufo00424";
	public static final String RESID_TOPN_SHOWELSE = "miufo00425";
	private AnaReportPlugin m_plugin = null;

	public TopNDesignExt(AnaReportPlugin plugin) {
		super();
		m_plugin = plugin;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(final UfoReport container) {
		CellPosition[] fmtcells = m_plugin.getModel().getFormatPoses(container.getCellsModel(),
				container.getCellsModel().getSelectModel().getSelectedAreas());
		if (fmtcells == null || fmtcells.length > 1)// 不能对多个单元同时设置TopN分析
			return null;

		CellsModel fmtModel = m_plugin.getModel().getFormatModel();
		CellPosition pos = fmtcells[0];
		Cell cell = fmtModel.getCell(pos);
		if (cell == null || cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) == null) {
			return null;// 请选择要设置的字段单元
		}
		ExAreaModel exModel = ExAreaModel.getInstance(fmtModel);
		ExAreaCell exCell = exModel.getExArea(pos);
		final AreaDataModel areaModel = (AreaDataModel) exCell.getModel();

		if (areaModel == null)
			return null;

		AnaRepField anaFld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		String canSetTopN = check(exCell, areaModel, anaFld);
		if (canSetTopN != null) {
			MessageDialog.showErrorDlg(container, null, canSetTopN);
			return null;
		}
		if (anaFld != null
				&& (anaFld.getFieldType() == AnaRepField.Type_CROSS_MEASURE || anaFld
						.getFieldType() == AnaRepField.Type_CROSS_SUBTOTAL)
				&& areaModel.getCrossSet() != null) {
			doCrossTopN(container,anaFld,areaModel.getCrossSet() );
		} else {
			ListTopNDefDlg dlg = new ListTopNDefDlg(container, (TopNSetInfo)anaFld
					.getTopNInfo(), anaFld.getField(), areaModel.getDSDef()
					.getMetaData().getFields(true));
			if (dlg.showModal() == UfoDialog.ID_OK) {
				TopNSetInfo topN = dlg.getTopNInfo();
				setTopN(anaFld, topN);
				if (!m_plugin.getModel().isFormatState()) {// 数据态的设置
					// 3。更新数据浏览态的表格模型
					m_plugin.refreshDataModel(true);
				}
				m_plugin.setDirty(true);
			}
		}
		return null;
	}
    /**
     * 处理交叉指标的TopN
     * @param container
     * @param anaFld
     * @param crosstab
     */
	private void doCrossTopN(Container container,AnaRepField anaFld,AnaCrossTableSet crosstab){

		Field[] rowField = crosstab.getCrossSet().genHeaderField(true);
		Field[] colField = crosstab.getCrossSet().genHeaderField(false);
		CrossMeasureTopNSet rowSet = getInitCrossMeasureTopNSet(true,
				rowField,colField, anaFld, crosstab.getCrossSet());
		CrossMeasureTopNSet colSet = getInitCrossMeasureTopNSet(false,
				colField,rowField, anaFld, crosstab.getCrossSet());
		if (rowSet != null || colSet != null) {
			CrossTopNDefDlg dlg = new CrossTopNDefDlg(
					container, rowSet, rowField, colSet, colField,crosstab.getDsTool(),crosstab.getCrossSet().getMeasureFlds());
			dlg.show();
			if (dlg.getResult() == UfoDialog.ID_OK) {
				rowSet=dlg.getTopNSetInfo(true);
				colSet=dlg.getTopNSetInfo(false);
				if(rowSet!=null||colSet!=null){
					CrossTopNSet topNInfo=null;
					if(anaFld.getDimInfo().getTopNInfo() instanceof CrossTopNSet){
						topNInfo=(CrossTopNSet)anaFld.getDimInfo().getTopNInfo();
					}
					if(topNInfo==null){
						topNInfo=new CrossTopNSet();
					}
					topNInfo.setRowTopNSet(rowSet);
					topNInfo.setColTopNSet(colSet);
					anaFld.getDimInfo().setTopNInfo(topNInfo);
					m_plugin.setDirty(true);
					crosstab.setDirty(true);
				}
				if (!m_plugin.getModel().isFormatState()) {// 数据态的设置
					// 3。更新数据浏览态的表格模型
					m_plugin.refreshDataModel(true);
				}
			}
		}

	
	}
	private String check(ExAreaCell exCell, AreaDataModel areaModel, AnaRepField anaFld) {
		// TODO 检查在同一个分组级别上只能有一个字段设置topN分析
		return null;
	}

	private void setTopN(AnaRepField anaFld, TopNSetInfo topN) {
		anaFld.setTopNInfo(topN);
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		return isFieldExtEnabled(m_plugin, focusComp);

	}
	static boolean isFieldExtEnabled(AnaReportPlugin plugin,Component focusComp ) {
		if (!StateUtil.isAreaSel(plugin.getReport(), focusComp))
			return false;
		AnaReportModel model = plugin.getModel();
		CellPosition anchorCellPos = model.getCellsModel().getSelectModel().getAnchorCell();
		if (!model.isFormatState()) {
			CellPosition[] dataStateCells = model.getFormatPoses(model.getDataModel(),
					new AreaPosition[] { AreaPosition.getInstance(anchorCellPos, anchorCellPos) });
			if (dataStateCells != null && dataStateCells.length > 0)
				anchorCellPos = dataStateCells[0];
		}

		Cell cell = model.getFormatModel().getCell(anchorCellPos);
		if (cell == null)
			return false;
		AnaRepField anaFld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		if (anaFld== null||anaFld.getFieldType()==AnaRepField.Type_CROSS_ROW||anaFld.getFieldType()==AnaRepField.Type_CROSS_COLUMN)
			return false;
		ExAreaModel exArea = ExAreaModel.getInstance(model.getFormatModel());
		if (exArea == null || exArea.getExArea(anchorCellPos) == null)
			return false;
		AreaDataModel areaModel = model.getAreaData(exArea.getExArea(anchorCellPos).getExAreaPK());
		if (areaModel == null || areaModel.getDSTool() == null)
			return false;
		if(!areaModel.getDSTool().isSupport(DescriptorType.FilterDescriptor))//对于不支持筛选的数据集，此功能不可用
			return false;
		
		return true;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(true);
		uiDes.setGroup(StringResource.getStringResource(AnaFieldTypeExt.RESID_SETFIELD));
		uiDes.setPaths(new String[] {});
		uiDes.setName(StringResource.getStringResource(RESID_TOPN_DESIGN));
		uiDes.setTooltip(StringResource.getStringResource(RESID_TOPN_DESIGN));
		uiDes.setImageFile("reportcore/topN.gif");
		return new ActionUIDes[] { uiDes };

	}
	/**
     * 根据指标TopN的规则,获取指标TopN设置
     * @create by guogang at Feb 28, 2009,7:31:42 PM
     *
     * @param isRow
     * @param measure
     * @param crossset
     * @return
     */
	private CrossMeasureTopNSet getInitCrossMeasureTopNSet(boolean isRow,Field[] flds,Field[] otherFlds,AnaRepField measureFld,CrossTableSet crossset){
		CrossMeasureTopNSet topNSet=null;
		CrossTopNSet topNInfo=null;
		if(measureFld.getDimInfo().getTopNInfo() instanceof CrossTopNSet){
			topNInfo=(CrossTopNSet)measureFld.getDimInfo().getTopNInfo();
		}
		if(topNInfo!=null){
			if(isRow){
				topNSet=topNInfo.getRowTopNSet();
			}else{
				topNSet=topNInfo.getColTopNSet();
			}
			if(topNSet!=null&&topNSet.isChange(flds)){
				topNSet=null;
				if(isRow){
					topNInfo.setRowTopNSet(topNSet);
				}else{
					topNInfo.setColTopNSet(topNSet);
				}
			}
		}
		if(topNSet==null){
			topNSet=(CrossMeasureTopNSet)crossset.geneCrossAnalyseSet(isRow,flds,otherFlds,measureFld.getFieldCountDef(),CrossTableSet.CROSSANALYSETYPE_TOPN);
		}
		return topNSet;
	}
}
