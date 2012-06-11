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
 * �����������������
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
			al_extensions.add(new BIReportPreViewExt(m_plugin));// ����Ԥ��
			Integer status=fromWhere();
			if(!(status!=null&&status>0))
				al_extensions.add(new AnaReportSaveExt(m_plugin));// ����
		}else{
			al_extensions.add(new AnaReportRedoExt(m_plugin));//�ָ�
		}
		// @edit by ll at 2009-11-21,����03:47:47 ���Ӵ�ӡ���ù���
		al_extensions.add(new AnaReportPrintSettingExt(m_plugin));//�������ӡ����
		
		al_extensions.add(new AnaReportPrintPreViewExt(m_plugin));//�������ӡԤ��
		al_extensions.add(new AnaReportPrintExt(m_plugin));//�������ӡ
		al_extensions.add(new AnaReportFindExt(m_plugin));//����
		
		al_extensions.add(new BINavigationExt(ReportNavPanel.EAST_NAV, m_plugin.getQueryPanel())); // ����Դ�������
		// ���ݼ���ѡ���ɾ��
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_EDIT));
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_ADD));
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_REMOVE));
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_REFRESH));
		al_extensions.add(new AnaDataSourceExt(m_plugin, AnaDataSourceExt.DATASOURCE_PREVIEW));

		// �ֶ�����Ϊ���򡢽���Ͳ�����
		al_extensions.add(new AnaFieldOrderExt(m_plugin, AnaRepField.ORDERTYPE_ASCENDING));
		al_extensions.add(new AnaReportOrderMngExt(m_plugin));//�������

		// ��Ԫ���Ƿ��Զ��ϲ�������
		al_extensions.add(new AnaCellCombineExt(m_plugin, true));
		
		// �ֶ����͵����ã�ϸ�ڡ����顢ͳ�ơ�����С�ƣ�
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.TYPE_GROUP_FIELD));
		
		// @edit by ll at 2009-11-21,����03:47:40 ���ӷ��鵥Ԫ��ǿ�Ʒ�ҳ��ȡ����ҳ����
		al_extensions.add(new AnaInsertPaginationExt());
		al_extensions.add(new AnaInsertCellPaginationExt(m_plugin));
		al_extensions.add(new AnaDeletePaginationExt(m_plugin));
		
		
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.TYPE_CALC_FIELD));
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.TYPE_CANCEL_CALC_FIELD));
		//����С��...������5��ͳ�����ͣ�
		for (int i = 0; i < IFldCountType.ALL_TYPE_COUNT; i++) {
			al_extensions.add(new InsertTotalRowExt(m_plugin, i));
		}
		
		// ��������
		al_extensions.add(new RankFunctionExt(m_plugin));
		// �̶���Ա����
		al_extensions.add(new AnaFixFieldSetExt(m_plugin));

		// ����������ϵ������
		al_extensions.add(new DataRelationExt(m_plugin, true));
		//����͸�����ú�ִ��
		al_extensions.add(new ReportDrillExt(m_plugin, true));
		al_extensions.add(new ReportDrillExt(m_plugin, false));

		// topN����
		al_extensions.add(new TopNDesignExt(m_plugin));

		// �������������
		al_extensions.add(new AnaCrossAreaExt(m_plugin, true));
		al_extensions.add(new AnaCrossAreaExt(m_plugin, false));
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.Type_CROSS_SUBTOTAL));
		al_extensions.add(new AnaFieldTypeExt(m_plugin, AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL));
		
		al_extensions.add(new AnaExAreaFilterSetExt(m_plugin,AnaExAreaFilterSetExt.FILTER_TYPE_ENUM));
		al_extensions.add(new AnaExAreaFilterSetExt(m_plugin,AnaExAreaFilterSetExt.FILTER_TYPE_MANAGER));
		al_extensions.add(new AnaExAreaFilterSetExt(m_plugin,AnaExAreaFilterSetExt.FILTER_TYPE_SHORTCUT));
		al_extensions.add(new AnaExAreaFilterSetExt(m_plugin,AnaExAreaFilterSetExt.DATASETFILTER_TYPE_ENUM));
		
		al_extensions.add(new AnaExAreaParamSetExt(m_plugin));

		al_extensions.add(new AnaReportExpExt(m_plugin));// ����
		al_extensions.add(new BINavigationExt(ReportNavPanel.NORTH_NAV, m_plugin.getConditionPanelDropTarget().getDropPanel())); // ҳά�ȵ������
		return al_extensions.toArray(new IExtension[0]);
	}
	
	/**
	 * @return null or -1:IUFO;0:NC����ڵ�;1:NC��ƽڵ�
	 */
	private Integer fromWhere(){
		ContextVO context = m_plugin.getReport().getContextVo();
		return  (Integer)context.getAttribute(ReportResource.OPEN_IN_MODAL_DIALOG);
	}

}
