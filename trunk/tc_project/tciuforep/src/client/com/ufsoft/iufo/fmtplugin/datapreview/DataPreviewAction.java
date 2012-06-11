package com.ufsoft.iufo.fmtplugin.datapreview;

import java.awt.event.ActionEvent;

import nc.ui.iufo.input.table.TableInputParam;
import nc.vo.iufo.datasource.DataSourceVO;

import com.ufida.dataset.IContext;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.util.MultiLang;

public class DataPreviewAction extends AbstractPluginAction implements
		IUfoContextKey {
	private static final String GROUP = "dataStatePreview";

	@Override
	public void execute(ActionEvent e) {

		ReportDesigner view = (ReportDesigner) getCurrentView();
		String reportId = (String) view.getContext().getAttribute(REPORT_PK);
		String dataPreviewViewId = createDataPreviewId(reportId);
		ReportDataPreviewDesigner dataDesigner = (ReportDataPreviewDesigner) view
				.getView(dataPreviewViewId);
		if (dataDesigner == null) {
			dataDesigner = (ReportDataPreviewDesigner) view.openView(
					ReportDataPreviewDesigner.class.getName(),
					createDataPreviewId(reportId), view.getId());

			dataDesigner.setTitle(createDataPreviewTitle(view.getTitle()));
			// ����PK
			dataDesigner.getContext().setAttribute(REPORT_PK, reportId);
			
			dataDesigner.getContext().setAttribute(TABLE_INPUT_TRANS_OBJ,
					geneTransObj(dataDesigner.getContext()));

			dataDesigner.getContext().setAttribute(CREATE_UNIT_ID,
					view.getContext().getAttribute(CREATE_UNIT_ID));
		}
		dataDesigner.initCellsModel(view.getCellsModel(),reportId);

	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(MultiLang.getString("file"), GROUP);
		// uiiufofmt00068=����̬���
		descriptor.setName(StringResource.getStringResource("uiiufofmt00068"));
		descriptor.setExtensionPoints(new XPOINT[] { XPOINT.MENU });
		descriptor.setShowDialog(true);
		return descriptor;
	}

	private String createDataPreviewId(String reportId) {
		final String DATA_PREVIEW_SUFFIX = "data_preview";
		StringBuilder sb = new StringBuilder();
		sb.append(reportId).append('_').append(DATA_PREVIEW_SUFFIX);
		return sb.toString();
	}

	/**
	 * @i18n miufo1000875=Ԥ��
	 */
	private String createDataPreviewTitle(String reportTitle) {
		StringBuilder sb = new StringBuilder();
		sb.append(reportTitle).append('_').append(StringResource.getStringResource("miufo1000875"));
		return sb.toString();
	}

	/**
	 * �õ����ؼ�������Ϣ����
	 * 
	 * @return
	 */
	private TableInputTransObj geneTransObj(IContext context) {
		TableInputTransObj m_oTransObj = new TableInputTransObj();
		IRepDataParam oRepDataParam = new RepDataParam();

		// ��������operType
		String strOperType = TableInputParam.OPERTYPE_REPDATA_INPUT;
		oRepDataParam.setOperType(strOperType);
		// ��������operType
		oRepDataParam.setIsNeedFilterDataRight(true);
		// AloneID
		String strAloneID = null;
		oRepDataParam.setAloneID(strAloneID);
		// ������λPK
		oRepDataParam.setOperUnitPK((String) context
				.getAttribute(IUfoContextKey.CUR_UNIT_ID));
		// �����û�PK
		oRepDataParam.setOperUserPK((String) context.getAttribute(CUR_USER_ID));
		// ��֯��ϵ
		oRepDataParam.setOrgPK((String) context.getAttribute(ORG_PK));

		// ������������PK
//		oRepDataParam.setTaskPK((String) context.getAttribute(TASK_PK));
		// �Ƿ�ϲ�����
		oRepDataParam.setIsHBBBData(false);
		// �Ƿ������
		oRepDataParam.setAnaRep("2".equals(context.getAttribute("repType")));
		// ����IUFO���ݵ�ImportAloneID
		String strImportAloneID = null;
		oRepDataParam.setImportAloneID(strImportAloneID);

		oRepDataParam.setReportPK((String) context.getAttribute(REPORT_PK));
		// ����Դ��Ϣ
		DataSourceVO dataSourceVo = (DataSourceVO) context
				.getAttribute(DATA_SOURCE);
		if (dataSourceVo != null) {
			String strDSID = dataSourceVo.getId();
			String strDSUnitPK = dataSourceVo.getUnitId();
			String strDSUserPK = dataSourceVo.getLoginName();
			String strDSPwd = dataSourceVo.getLoginPassw();
			String pass = nc.bs.iufo.toolkit.Encrypt.encode(strDSPwd, strDSID);
			String strDSDate = dataSourceVo.getLoginDate();
			DataSourceInfo curDSInfo = new DataSourceInfo(strDSID, strDSUnitPK,
					strDSUserPK, pass, strDSDate);
			oRepDataParam.setDSInfo(curDSInfo);
		}
		// ������
		m_oTransObj.setRepDataParam(oRepDataParam);
		m_oTransObj.setType(TableInputTransObj.TYPE_REPDATA);

		// ��ǰ��¼ʱ��
		String strCurLoginDate = (String) context.getAttribute(LOGIN_DATE);
		m_oTransObj.setCurLoginDate(strCurLoginDate);
		m_oTransObj.setLoginUnit((String) context.getAttribute(CUR_UNIT_CODE));

		// �û�ѡ������ֱ���
		m_oTransObj.setLangCode((String) context.getAttribute(CURRENT_LANG));
		m_oTransObj.setDataExplore(true);

		return m_oTransObj;
	}
	
	public boolean isEnabled() {
		return !(REPORT_TYPE_MODEL+"").equals(getMainboard().getContext().getAttribute(REPORT_TYPE));
	}

}
 