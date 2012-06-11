
package com.ufsoft.report.sysplugin.print;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 打印设置实现
 * @author guogang
 *
 */
public class PrintSettingCmd extends UfoCommand{

	private UfoReport m_Rep = null;
	 
	public PrintSettingCmd(UfoReport rep) {
		super();
		this.m_Rep = rep;
	}
 
	public void execute(Object[] params) {
	//	m_Rep.getTable().pageFromat();
		PrintSettingDlg dlg = new PrintSettingDlg(m_Rep);
		dlg.setVisible(true);
//		dlg.showModal();
	}


}
