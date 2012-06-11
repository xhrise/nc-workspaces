package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.util.ArrayList;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;

import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.AreaDataRelation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

/**
 * 分析表中扩展区域的交叉设置
 * 
 * @author ll
 * 
 */
public class DataRelationExt extends AbsAnaReportExt {
	/**
	 * @i18n miufo00185=启用依赖关系
	 */
	static final String RESID_ENABLED_DATA_RELATION = "miufo00185";
	/**
	 * @i18n miufo00186=数据依赖
	 */
	static final String RESID_SET_DATA_RELATION = "miufo00186";
	/**
	 * @i18n miufo00187=删除数据依赖
	 */
	static final String RESID_DEL_DATA_RELATION = "miufo00187";
	/**
	 * @i18n miufo00188=区域设置
	 */
	static final String RESID_AREA_DATA = "miufo00188";

	private boolean setRelation = true;

	public DataRelationExt(AnaReportPlugin plugin, boolean setRela) {
		super();
		setPlugIn(plugin);
		setRelation = setRela;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	/**
	 * @i18n miufo00189=确认删除数据依赖关系？
	 */
	@Override
	public Object[] getParams(UfoReport container) {
		AnaReportModel model = m_plugin.getModel();
		CellPosition pos = model.getCellsModel().getSelectModel()
				.getAnchorCell();
		if (!model.isFormatState()) {
			CellPosition[] dataStateCells = model.getFormatPoses(model
					.getDataModel(), new AreaPosition[] { AreaPosition
					.getInstance(pos, pos) });
			if (dataStateCells != null && dataStateCells.length > 0)
				pos = dataStateCells[0];
		}
		// 当前选择点必须是一个数据区域
		AreaDataModel aModel = getSelAreaModel(true);
		if (aModel == null || aModel.getDSTool() == null)
			return null;
		if (!aModel.getDSTool().isSupport(DescriptorType.FilterDescriptor)) {// 对于不支持筛选的数据集，此功能不可用
			return null;
		}
		String areaPK = aModel.getAreaPK();

		// 从报表格式中获取其他数据区域
		ArrayList<ExAreaCell> al_areas = new ArrayList<ExAreaCell>();

		ExAreaModel exModel = ExAreaModel.getInstance(m_plugin.getModel()
				.getFormatModel());
		ExAreaCell[] cells = exModel.getExAreaCells();
		if (cells != null && cells.length > 1) {
			for (ExAreaCell exArea : cells) {
				if (exArea.getExAreaPK().equals(areaPK))
					continue;
				if (m_plugin.getModel().getAreaData(exArea.getExAreaPK())
						.getDSPK() == null)
					continue;
				al_areas.add(exArea);
			}
		}
		if (al_areas.size() == 0) {
			MessageDialog.showHintDlg(container, null, StringResource
					.getStringResource("miufo00281"));
			return null;
		}

		AreaDataRelation oldRela = m_plugin.getModel().getDataRelation(areaPK);
		if (setRelation) {
			DataRelationDlg dlg = new DataRelationDlg(container, al_areas,
					m_plugin.getModel(), aModel);
			dlg.setDataRelation(oldRela);
			if (dlg.showModal() == UfoDialog.ID_OK) {
				AreaDataRelation relation = dlg.getDataRelation();
				m_plugin.getModel().setDataRelation(areaPK, relation);
				m_plugin.setDirty(true);
				if (!model.isFormatState()) {
					m_plugin.refreshDataModel(true);
				}
			}
		} else {
			if (oldRela == null)
				return null;
			if (MessageDialog.showYesNoDlg(container, null, StringResource
					.getStringResource("miufo00189")) == UIDialog.ID_YES) {
				m_plugin.getModel().setDataRelation(areaPK, null);
				m_plugin.setDirty(true);
				if (!model.isFormatState()) {
					m_plugin.refreshDataModel(true);
				}
			}
		}
		return null;
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		if (!StateUtil.isAreaSel(m_plugin.getReport(), focusComp))
			return false;
		// 当前选择点必须是一个数据区域
		AreaDataModel aModel = getSelAreaModel(true);
		if (aModel == null || aModel.getDSTool() == null)
			return false;
		if (!aModel.getDSTool().isSupport(DescriptorType.FilterDescriptor)) {// 对于不支持筛选的数据集，此功能不可用
			return false;
		}
		ExAreaModel exModel = ExAreaModel.getInstance(m_plugin.getModel()
				.getFormatModel());
		ExAreaCell[] exCells = exModel.getExAreaCells();
		if (exCells == null || exCells.length < 2)
			return false;
		return true;
	}

	/**
	 * @i18n miufo00178=字段设置
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(true);
		uiDes.setGroup(StringResource.getStringResource("miufo00178"));
		uiDes.setPaths(new String[] {});
		if (setRelation) {
			uiDes.setName(StringResource
					.getStringResource(RESID_SET_DATA_RELATION));
			uiDes.setTooltip(StringResource
					.getStringResource(RESID_SET_DATA_RELATION));
			uiDes.setImageFile("reportcore/data_relation.gif");
		} else {
			uiDes.setName(StringResource
					.getStringResource(RESID_DEL_DATA_RELATION));
			uiDes.setTooltip(StringResource
					.getStringResource(RESID_DEL_DATA_RELATION));
			uiDes.setImageFile("reportcore/unfold.gif");
		}
		return new ActionUIDes[] { uiDes };

	}

}
