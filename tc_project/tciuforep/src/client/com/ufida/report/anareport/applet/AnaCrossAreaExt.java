/**
 * 
 */
package com.ufida.report.anareport.applet;

import java.awt.Component;

import nc.ui.pub.beans.UIDialog;
import nc.vo.bi.report.manager.ReportResource;

import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.exarea.IExData;

/**
 * 分析报表交叉区域设置、取消功能设置
 * 
 * @author guogang
 * 
 */
public class AnaCrossAreaExt extends AbsActionExt {

	/**
	 * @i18n miufo00263=交叉区域
	 */
	private static final String RESID_CROSS_AREA = "miufo00263";
	/**
	 * @i18n miufo00264=取消交叉区域
	 */
	private static final String RESID_DEL_CROSS_AREA = "miufo00264";
	/**
	 * @i18n miufo00178=字段设置
	 */
	private static final String RESID_SETFIELD = "miufo00178";
	private AnaReportPlugin m_plugin = null;
	private boolean setCross = true;

	/**
	 * 
	 */
	public AnaCrossAreaExt(AnaReportPlugin plugin, boolean isSetCross) {
		super();
		m_plugin = plugin;
		setCross = isSetCross;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
	 */
	@Override
	public UfoCommand getCommand() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
	 */
	/**
	 * @i18n miufo00265=只能对单个扩展区域的区域设置交叉信息
	 */
	@Override
	public Object[] getParams(UfoReport container) {
		CellsModel fmtModel = m_plugin.getModel().getFormatModel();
		ExAreaCell selectExCell = m_plugin.getModel().getSelectedExAreaCell();
		if (selectExCell == null) {
			UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo00265"), container);
			return null;
		}

		
		if (setCross) {
			AreaDataModel areaModel = (AreaDataModel) selectExCell.getModel();
			AnaCrossTableSet oldCrossSet = null;
			if (areaModel != null && areaModel.getCrossSet() != null) {
				oldCrossSet = areaModel.getCrossSet();
			}
			AnaCrossAreaSetDlg dlg = new AnaCrossAreaSetDlg(container, m_plugin.getModel(), selectExCell, oldCrossSet);
			procDlgShow(container,dlg);
		} else {
			AreaDataModel areaModel = (AreaDataModel) selectExCell.getModel();
			AreaPosition[] selectAreas = fmtModel.getSelectModel().getSelectedAreas();
			AreaPosition crossArea = selectAreas[0].interArea(selectExCell.getArea());
			if (areaModel != null && areaModel.getCrossSet() != null) {
				AreaPosition innerArea = crossArea.interArea(areaModel.getCrossSet().getCrossArea());
				if (innerArea != null) {
					areaModel.setCrossInfo(null);
					container.getTable().getCells().repaint(selectExCell.getArea(), true);
				}
			}
		}
		return null;
	}
	private boolean procDlgShow(UfoReport report,UfoDialog dlg) {
		ContextVO context = report.getContextVo();
		Integer obj = (Integer)context.getAttribute(ReportResource.OPEN_IN_MODAL_DIALOG);
		boolean isModal = (obj==null||obj <0) ? false :true;
		if (isModal) {
			return (dlg.showModal() == UIDialog.ID_OK);
		} else {
			dlg.show();
			return true;
		}
	}
	@Override
	public boolean isEnabled(Component focusComp) {
		AnaReportModel anaModel = m_plugin.getModel();
		if (!anaModel.isFormatState()) {
			return false;
		}
		CellPosition anchorCell = anaModel.getCellsModel().getSelectModel().getAnchorCell();
		ExAreaModel exAreaModel = ExAreaModel.getInstance(anaModel.getFormatModel());
		ExAreaCell exAreaCell = exAreaModel.getExArea(anchorCell);
		if (exAreaCell == null){
			if(this.setCross)
			   return true;
			else
			   return false;
		}else {// @edit by ll at 2009-1-4,下午03:37:17 增加数据集是否支持描述器的判断
			IExData model = exAreaCell.getModel();
			if (model != null && model instanceof AreaDataModel) {
				AreaDataModel areaModel = (AreaDataModel) model;
				String dsPK = areaModel.getDSPK();
				AnaDataSetTool dsTool = anaModel.getDataSetTool(dsPK);
				if (dsTool != null && !dsTool.isSupport(DescriptorType.AggrDescriptor)) {
					// 对于不支持汇总的数据集，此功能不可用
					return false;
				}
				if(!this.setCross&&!areaModel.isCross()){
					return false;
				}
			}
		}
		if (exAreaCell.getExAreaType() == ExAreaCell.EX_TYPE_CHART)
			return false;
		return anaModel.isFormatState() && StateUtil.isFormat_AreaSel(m_plugin.getReport(), focusComp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(true);
		uiDes.setGroup(StringResource.getStringResource(RESID_SETFIELD));
		uiDes.setPaths(new String[] {});
		if (setCross) {
			uiDes.setName(StringResource.getStringResource(RESID_CROSS_AREA));
			uiDes.setTooltip(StringResource.getStringResource(RESID_CROSS_AREA));
			uiDes.setImageFile("reportcore/set_cross_area.gif");
		} else {
			uiDes.setName(StringResource.getStringResource(RESID_DEL_CROSS_AREA));
			uiDes.setTooltip(StringResource.getStringResource(RESID_DEL_CROSS_AREA));
			uiDes.setImageFile("reportcore/cancel_cross_area.gif");
		}
		return new ActionUIDes[] { uiDes };

	}

}
