package com.ufida.report.anareport.applet;

import java.util.ArrayList;

import nc.vo.bi.report.manager.ReportResource;

import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.edit.AnaFieldOrderExt;
import com.ufida.report.anareport.edit.AnaReportFindExt;
import com.ufida.report.anareport.edit.AnaReportOrderMngExt;
import com.ufida.report.anareport.edit.AnaReportRedoExt;
import com.ufida.report.anareport.edit.DataRelationExt;
import com.ufida.report.anareport.edit.InsertTotalRowExt;
import com.ufida.report.anareport.edit.RankFunctionExt;
import com.ufida.report.anareport.edit.ReportDrillExt;
import com.ufida.report.anareport.edit.TopNDesignExt;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.rep.applet.BINavigationExt;
import com.ufida.report.rep.applet.BIReportPreViewExt;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;

/**
 * 
 * 分析报表的设计器插件
 * 
 */
public class AnaReportDesc extends AbstractPlugDes {

	private AnaReportPlugin m_plugin = null;

	public AnaReportDesc(AnaReportPlugin plugin) {
		super(plugin);
		m_plugin = plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbstractPlugDes#createExtensions()
	 */
	protected IExtension[] createExtensions() {
		ArrayList<IExtension> al_extensions = new ArrayList<IExtension>();

		if (m_plugin.isAegisFormat()) {
			al_extensions.add(new BIReportPreViewExt(m_plugin));// 数据预览
			Integer status=fromWhere();
			if(!(status!=null&&status>0))
				al_extensions.add(new AnaReportSaveExt(m_plugin));// 保存
		}else{
			al_extensions.add(new AnaReportRedoExt(m_plugin));//恢复
		}
		// @edit by ll at 2009-11-21,下午03:47:47 增加打印设置功能
		al_extensions.add(new AnaReportPrintSettingExt(m_plugin));//分析表打印设置
		
		al_extensions.add(new AnaReportPrintPreViewExt(m_plugin));//分析表打印预览
		al_extensions.add(new AnaReportPrintExt(m_plugin));//分析表打印
		al_extensions.add(new AnaReportFindExt(m_plugin));//查找
		
		al_extensions.add(new BINavigationExt(ReportNavPanel.EAST_NAV, m_plugin.getQueryPanel())); // 数据源导航面板
		// 数据集的选择和删除
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_EDIT));
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_ADD));
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_REMOVE));
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_REFRESH));
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_PREVIEW));

		// 字段设置为升序、降序和不排序
		al_extensions.add(new AnaFieldOrderExt(m_plugin, AnaRepField.ORDERTYPE_ASCENDING));
		al_extensions.add(new AnaReportOrderMngExt(m_plugin));//排序管理

		// 单元格是否自动合并的设置
		al_extensions.add(new AnaCellCombineExt(m_plugin, true));
		
		// 字段类型的设置（细节、分组、统计、交叉小计）
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.TYPE_GROUP_FIELD));
		
		// @edit by ll at 2009-11-21,下午03:47:40 增加分组单元的强制分页和取消分页功能
		al_extensions.add(new AnaInsertPaginationExt());
		al_extensions.add(new AnaInsertCellPaginationExt(m_plugin));
		al_extensions.add(new AnaDeletePaginationExt(m_plugin));
		
		
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.TYPE_CALC_FIELD));
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.TYPE_CANCEL_CALC_FIELD));
		//插入小计...（包括5种统计类型）
		for (int i = 0; i < IFldCountType.ALL_TYPE_COUNT; i++) {
			al_extensions.add(new InsertTotalRowExt(m_plugin, i));
		}
		
		// 排名函数
		al_extensions.add(new RankFunctionExt(m_plugin));
		// 固定成员设置
		al_extensions.add(new AnaFixFieldSetExt(m_plugin));

		// 数据依赖关系的设置
		al_extensions.add(new DataRelationExt(m_plugin, true));
		//报表穿透的设置和执行
		al_extensions.add(new ReportDrillExt(m_plugin, true));
		al_extensions.add(new ReportDrillExt(m_plugin, false));

		// topN分析
		al_extensions.add(new TopNDesignExt(m_plugin));

		// 交叉区域的设置
		al_extensions.add(new AnaCrossAreaExt(m_plugin, true));
		al_extensions.add(new AnaCrossAreaExt(m_plugin, false));
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.Type_CROSS_SUBTOTAL));
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL));
		
		al_extensions.add(new AnaExAreaFilterSetExt(m_plugin,AnaExAreaFilterSetExt.FILTER_TYPE_ENUM));
		al_extensions.add(new AnaExAreaFilterSetExt(m_plugin,AnaExAreaFilterSetExt.FILTER_TYPE_MANAGER));
		al_extensions.add(new AnaExAreaFilterSetExt(m_plugin,AnaExAreaFilterSetExt.FILTER_TYPE_SHORTCUT));
		al_extensions.add(new AnaExAreaFilterSetExt(m_plugin,AnaExAreaFilterSetExt.DATASETFILTER_TYPE_ENUM));
		
		al_extensions.add(new AnaExAreaParamSetExt(m_plugin));

		al_extensions.add(new AnaReportExpExt(m_plugin));// 导出
		al_extensions.add(new BINavigationExt(ReportNavPanel.NORTH_NAV, m_plugin.getConditionPanelDropTarget().getDropPanel())); // 页维度导航面板
		return al_extensions.toArray(new IExtension[0]);
	}
	
	/**
	 * @return null or -1:IUFO;0:NC管理节点;1:NC设计节点
	 */
	private Integer fromWhere(){
		ContextVO context = m_plugin.getReport().getContextVo();
		return  (Integer)context.getAttribute(ReportResource.OPEN_IN_MODAL_DIALOG);
	}

}
