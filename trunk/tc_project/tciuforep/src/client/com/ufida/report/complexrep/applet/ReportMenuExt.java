package com.ufida.report.complexrep.applet;

import java.awt.event.KeyEvent;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
/**
 * �����˵�����չһ��Report�˵���
 * @author zzl 2005-7-13
 */
public class ReportMenuExt extends AbsActionExt {

    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(StringResource.getStringResource(StringResConst.STR_MENU_MAIN));
        uiDes.setMnemonic(KeyEvent.VK_R);
        uiDes.setDirectory(true);
        uiDes.setPaths(null);
        return new ActionUIDes[]{uiDes};
    }

    public UfoCommand getCommand() {
        return null;
    }

    public Object[] getParams(UfoReport container) {
        return null;
    }

}
