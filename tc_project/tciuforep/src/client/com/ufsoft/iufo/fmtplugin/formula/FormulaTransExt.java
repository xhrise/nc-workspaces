/*
 * �������� 2006-4-10

 */
package com.ufsoft.iufo.fmtplugin.formula;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;

/**
 * @author ljhua
 * ��ʽ����
 */
public class FormulaTransExt extends AbsActionExt {

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.IActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(StringResource.getStringResource("miufo1001691"));
        uiDes.setPaths(new String[]{StringResource.getStringResource("miufo1001692"),
//                StringResource.getStringResource("miufo1001615")
                });
        uiDes.setGroup("formulaDefExt");
        return new ActionUIDes[]{uiDes};
	}

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new FormulaTransCmd();
	}

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

}
