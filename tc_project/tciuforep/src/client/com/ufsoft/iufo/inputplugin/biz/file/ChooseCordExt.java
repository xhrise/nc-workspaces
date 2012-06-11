package com.ufsoft.iufo.inputplugin.biz.file;

import javax.swing.JPanel;

import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractNavExt;
import com.ufsoft.report.util.MultiLang;

public class ChooseCordExt extends AbstractNavExt {
	private UfoReport m_ufoReport = null;

	public ChooseCordExt(UfoReport ufoReport) {
		m_ufoReport = ufoReport;
	}

	/**
	 * @i18n uiuforep00008=��ѯ����
	 */
	public String getName() {
		return MultiLang.getString("uiuforep00008");
	}

	public int getNavPanelPos() {
		return ReportNavPanel.NORTH_NAV;
	}

	
	
	/**
	 * �õ��ؼ������ݶ���
	 * @param ufoReport
	 * @return
	 */
	public static ChangeKeywordsData[] doGetKeywordsDatas(UfoReport ufoReport){
		ChangeKeywordsData[] changeKeywordsDatas = null;
		Object returnObj = InputBizOper.doLinkServletTask(ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDATA, ufoReport, false);
		returnObj = getBizReturnObj(returnObj);
		if(returnObj != null && returnObj instanceof ChangeKeywordsData[]){
			changeKeywordsDatas = (ChangeKeywordsData[])returnObj;
		}

		return changeKeywordsDatas;
	}

	protected static Object getBizReturnObj(Object returnObj) {
		if(returnObj != null && returnObj instanceof Object[] && ((Object[])returnObj).length >=2){
			return ((Object[])returnObj)[1];
		}  
		return null;
	}

	@Override
	/**
	 * ���ݵ�ǰѡ��ؼ��֣���ʼ���ۺϲ�ѯ������壬�ۺϲ�ѯ�ڸñ�����ؼ��ֻ����ϣ�
	 * ���Ӱ汾��ѯ��������֧�ָ��屨�����ܱ����ϲ������
	 * 
	 * @param �ۺϲ�ѯ�������
	 */
	protected JPanel createPanel() {
		// TODO Auto-generated method stub
		return  new ChooseCordPanel(m_ufoReport, doGetKeywordsDatas(m_ufoReport));
	}

}
 