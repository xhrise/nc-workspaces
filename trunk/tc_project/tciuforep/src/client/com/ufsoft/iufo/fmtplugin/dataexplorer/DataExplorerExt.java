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
	 * @i18n uiiufofmt00068=数据态浏览
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
	 * @i18n uiiufofmt00069=数据态预览
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
		// TODO 这里的数据态模型应该直接取格式模型进行设置。而不是从数据库中取出。
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
	 * 获得报表的环境信息
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
	 * 得到表格控件传输信息对象
	 * 
	 * @return
	 */
	private TableInputTransObj geneTransObj(Context context,
			ReportFormatApplet formatApplet) {
		TableInputTransObj m_oTransObj = new TableInputTransObj();
		IRepDataParam oRepDataParam = new RepDataParam();
		// 报表PK
		oRepDataParam.setReportPK((String) context.getAttribute(REPORT_PK));

		// 操作类型operType
		String strOperType = TableInputParam.OPERTYPE_REPDATA_INPUT;
		oRepDataParam.setOperType(strOperType);
		// 操作类型operType
		oRepDataParam.setIsNeedFilterDataRight(true);
		// AloneID
		String strAloneID = null;
		;
		oRepDataParam.setAloneID(strAloneID);
		// 操作单位PK
		oRepDataParam.setOperUnitPK((String) context
				.getAttribute(LOGIN_UNIT_ID));
		// 操作用户PK
		oRepDataParam.setOperUserPK((String) context.getAttribute(CUR_USER_ID));
		// 组织体系
		oRepDataParam.setOrgPK((String) context.getAttribute(ORG_PK));

		// 报表所属任务PK
	//	String strTaskPK = formatApplet.getParameter("taskId");
	//	oRepDataParam.setTaskPK(strTaskPK);
		// 是否合并数据
		oRepDataParam.setIsHBBBData(false);
		// 是否分析表
		oRepDataParam.setAnaRep("2"
				.equals(formatApplet.getParameter("repType")));
		// 导入IUFO数据的ImportAloneID
		String strImportAloneID = null;
		oRepDataParam.setImportAloneID(strImportAloneID);
		// 数据源信息
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
		// ＃设置
		m_oTransObj.setRepDataParam(oRepDataParam);
		m_oTransObj.setType(TableInputTransObj.TYPE_REPDATA);

		// 当前登录时间
		String strCurLoginDate = (String) context.getAttribute(LOGIN_DATE);
		m_oTransObj.setCurLoginDate(strCurLoginDate);
		m_oTransObj.setLoginUnit(formatApplet.getParameter("OPER_UNIT_CODE"));

		// 用户选择的语种编码
		String strLangCode = (String) context.getAttribute(CURRENT_LANG);
		m_oTransObj.setLangCode(strLangCode);
		m_oTransObj.setDataExplore(true);

		return m_oTransObj;
	}

}
