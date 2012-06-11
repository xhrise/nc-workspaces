package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.util.ArrayList;

import nc.ui.pub.beans.MessageDialog;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.IRankFuncSet;
import com.ufida.report.anareport.applet.AnaFieldTypeExt;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
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
public class RankFunctionExt extends AbsActionExt {
	/**
	 * @i18n miufo00156=排名函数
	 */
	public static final String RESID_RANK_DESIGN = "miufo00156";
	/**
	 * @i18n miufo00157=排名顺序
	 */
	public static final String RESID_RANK_ORDER = "miufo00157";
	/**
	 * @i18n miufo00158=从大到小
	 */
	public static final String RESID_RANK_DESCEDING = "miufo00158";
	/**
	 * @i18n miufo00159=允许并列
	 */
	public static final String RESID_RANK_SAME = "miufo00159";
	/**
	 * @i18n miufo00160=排名范围
	 */
	public static final String RESID_RANK_RANGE = "miufo00160";
	private AnaReportPlugin m_plugin = null;

	public RankFunctionExt(AnaReportPlugin plugin) {
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
		if (fmtcells == null || fmtcells.length > 1)// 不能对多个单元同时设置Rank
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
		String canSetRank = check(exCell, areaModel, anaFld);
		if (canSetRank != null) {
			MessageDialog.showErrorDlg(container, null, canSetRank);
			return null;
		}
		boolean isCrossMeasure=false;
		Field[] rangFlds=null;
		if (anaFld != null
				&& (anaFld.getFieldType() == AnaRepField.Type_CROSS_MEASURE || anaFld
						.getFieldType() == AnaRepField.Type_CROSS_SUBTOTAL)
				&& areaModel.getCrossSet() != null){
			isCrossMeasure=true;
			String rangFld=null;
			if(anaFld.getFieldCountDef()!=null){
				rangFld=anaFld.getFieldCountDef().getRangeFld();
			}
			Field[] rows=areaModel.getCrossSet().getCrossFlds(AnaRepField.Type_CROSS_ROW);
			Field[] cols=areaModel.getCrossSet().getCrossFlds(AnaRepField.Type_CROSS_COLUMN);
			if(rows!=null||cols!=null){
				int rowLen=rows==null?0:rows.length;
				int colLen=cols==null?0:cols.length;
				ArrayList<Field> latiFlds=new ArrayList<Field>();
				for(int i=0;i<rowLen;i++){
					if(rows[i].getFldname().equals(rangFld)){
						break;
					}
					latiFlds.add(rows[i]);
				}
				for(int j=0;j<colLen;j++){
					if(cols[j].getFldname().equals(rangFld)){
						break;
					}
					latiFlds.add(cols[j]);
				}
				rangFlds=latiFlds.toArray(new Field[0]);
			}
		}else{
			rangFlds=areaModel.getDSTool().getFields(true);
			isCrossMeasure=false;
		}
		
		RankFunctionDlg dlg = new RankFunctionDlg(container, rangFlds,isCrossMeasure);
		dlg.setRankInfo(anaFld.getRankInfo());
		if (dlg.showModal() == UfoDialog.ID_OK) {
			IRankFuncSet rank = dlg.getRankInfo();
			setRankInfo(anaFld, rank);
			if(isCrossMeasure){
				areaModel.getCrossSet().setDirty(true);
			}
			if (!m_plugin.getModel().isFormatState()) {// 数据态的设置
				// 3。更新数据浏览态的表格模型
				m_plugin.refreshDataModel(true);
			}
			m_plugin.setDirty(true);
			
		}
		return null;
	}
    
	private String check(ExAreaCell exCell, AreaDataModel areaModel, AnaRepField anaFld) {
		// TODO 检查在同一个分组级别上只能有一个字段设置topN分析
		return null;
	}
    /**
     * 
     * @param anaFld
     * @param rank
     * @param areaModel
     */
	private void setRankInfo(AnaRepField anaFld, IRankFuncSet rank) {
		anaFld.setRankInfo(rank);
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		return TopNDesignExt.isFieldExtEnabled(m_plugin, focusComp);
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(true);
		uiDes.setGroup(StringResource.getStringResource(AnaFieldTypeExt.RESID_SETFIELD));
		uiDes.setPaths(new String[] {});
		uiDes.setName(StringResource.getStringResource(RESID_RANK_DESIGN));
		uiDes.setTooltip(StringResource.getStringResource(RESID_RANK_DESIGN));
		uiDes.setImageFile("reportcore/rank_func.gif");
		return new ActionUIDes[] { uiDes };

	}

}
 