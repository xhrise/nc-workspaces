/*
 * �������� 2004-11-2
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.ufsoft.report.sysplugin.log;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**
 * ��־���ܵ�
 * @author wupeng
 */
public class LogExt extends AbsActionExt{// implements IMainMenuExt,IToolBarExt {
	/**
	 * ��־�Ի����ʵ��.
	 */
	private LogWindow m_LogWin ;
	private UfoReport parent = null;

	public LogExt(UfoReport report) {
		parent = report;
	}
	
    LogWindow getLogWindow(){
    	if(m_LogWin == null){
    		m_LogWin = new LogWindow(parent);
    	}
    	return m_LogWin;
    }
	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand(){
			public void execute(Object[] params) {}			
		};
	}

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {		
		//��ʾ��־�Ի���.
		getLogWindow().setVisible(true);
		return null;
	}
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    /**
	 * @i18n report00102=��־
	 * @i18n help=����
	 */
    public ActionUIDes[] getUIDesArr() {
    	ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setImageFile("reportcore/log.png");
        uiDes1.setName(MultiLang.getString("report00102"));
        uiDes1.setPaths(new String[]{MultiLang.getString("miufo1001692")});
        uiDes1.setGroup("formulaDefExt");
        return new ActionUIDes[]{uiDes1};
    }
}
  