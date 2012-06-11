package com.ufsoft.iufo.fmtplugin.dataexplorer;

import java.awt.Container;
import java.awt.Frame;

import javax.swing.JFrame;

import nc.ui.iufo.input.table.TableInputParam;
import nc.vo.iufo.datasource.DataSourceVO;

import com.ufida.dataset.Context;
import com.ufsoft.iufo.fmtplugin.ReportFormatApplet;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UIUtilities;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class DataExplorerExt extends AbsActionExt implements IUfoContextKey {

	/**
	 * @i18n uiiufofmt00068=����̬���
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setPaths(new String[] { MultiLang.getString("file") });
		uiDes.setName(StringResource.getStringResource("uiiufofmt00068"));
		uiDes.setGroup("dataStatePreview");
		return new ActionUIDes[] { uiDes };
	}

	public UfoCommand getCommand() {
		return null;
	}

	/**
	 * @i18n uiiufofmt00069=����̬Ԥ��
	 */
	public Object[] getParams(final UfoReport container) {

		JFrame frame = new JFrame() {
			{
				UfoReport newReport = getDataExplorerReport(container);
				UIUtilities.ufoReport2JRootPane(newReport, getRootPane());

			}

		};

		String title = StringResource.getStringResource("uiiufofmt00069");
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setTitle(title);
		frame.setVisible(true);
		return null;
	}

	private UfoReport getDataExplorerReport(UfoReport container) {
		// TODO ���������̬ģ��Ӧ��ֱ��ȡ��ʽģ�ͽ������á������Ǵ����ݿ���ȡ����
		ReportFormatApplet formatApplet = getAppletBySubCommponent(container);
		DataExplorerReport report = DataExplorerReport.getInstance(
				geneInitContext(formatApplet), formatApplet);
		return report;
	}

	private static ReportFormatApplet getAppletBySubCommponent(
			UfoReport container) {
		Container c = container;
		while (c != null) {
			if (c instanceof ReportFormatApplet) {
				return (ReportFormatApplet) c;
			}
			c = c.getParent();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * ��ñ���Ļ�����Ϣ
	 * 
	 * @return
	 */
	private UfoContextVO geneInitContext(ReportFormatApplet formatApplet) {
		UfoReport formatReport = formatApplet.getUfoReport();
		UfoContextVO context = (UfoContextVO) formatReport.getContextVo();
		context.setAttribute(TABLE_INPUT_TRANS_OBJ, geneTransObj(context,
				formatApplet));
		context
				.setAttribute(CUR_UNIT_CODE, context
						.getAttribute(CUR_UNIT_CODE));
		return context;
	}


	/**
	 * �õ����ؼ�������Ϣ����
	 * 
	 * @return
	 */
	private TableInputTransObj geneTransObj(Context context,
			ReportFormatApplet formatApplet) {
		TableInputTransObj m_oTransObj = new TableInputTransObj();
		IRepDataParam oRepDataParam = new RepDataParam();
		// ����PK
		oRepDataParam.setReportPK((String) context.getAttribute(REPORT_PK));

		// ��������operType
		String strOperType = TableInputParam.OPERTYPE_REPDATA_INPUT;
		oRepDataParam.setOperType(strOperType);
		// ��������operType
		oRepDataParam.setIsNeedFilterDataRight(true);
		// AloneID
		String strAloneID = null;
		;
		oRepDataParam.setAloneID(strAloneID);
		// ������λPK
		oRepDataParam.setOperUnitPK((String) context
				.getAttribute(LOGIN_UNIT_ID));
		// �����û�PK
		oRepDataParam.setOperUserPK((String) context.getAttribute(CUR_USER_ID));
		// ��֯��ϵ
		oRepDataParam.setOrgPK((String) context.getAttribute(ORG_PK));

		// ������������PK
	//	String strTaskPK = formatApplet.getParameter("taskId");
	//	oRepDataParam.setTaskPK(strTaskPK);
		// �Ƿ�ϲ�����
		oRepDataParam.setIsHBBBData(false);
		// �Ƿ������
		oRepDataParam.setAnaRep("2"
				.equals(formatApplet.getParameter("repType")));
		// ����IUFO���ݵ�ImportAloneID
		String strImportAloneID = null;
		oRepDataParam.setImportAloneID(strImportAloneID);
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
		m_oTransObj.setLoginUnit(formatApplet.getParameter("OPER_UNIT_CODE"));

		// �û�ѡ������ֱ���
		String strLangCode = (String) context.getAttribute(CURRENT_LANG);
		m_oTransObj.setLangCode(strLangCode);
		m_oTransObj.setDataExplore(true);

		return m_oTransObj;
	}

}
