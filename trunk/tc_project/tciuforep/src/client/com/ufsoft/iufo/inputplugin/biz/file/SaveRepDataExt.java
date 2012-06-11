/*
 * 创建日期 2006-4-11
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class SaveRepDataExt extends AbsIufoOpenedRepBizMenuExt{
    private boolean m_bRepCanModify = true;
    private boolean m_bCommited = false;

    public SaveRepDataExt(UfoReport ufoReport) {
        super(ufoReport);
    }
    private boolean isRepCanModify(){
        return m_bRepCanModify;
    }
    public void setRepCanModify(boolean bRepCanModify){
        this.m_bRepCanModify = bRepCanModify;
    }
    
    public boolean isCommeted(){
        return m_bCommited;
    }
    public void setCommeted(boolean bCommited){
        this.m_bCommited = bCommited;
    }

    protected String[] getPaths() {
        return new String[]{MultiLang.getString("file")};
    }

    protected String getMenuName() {
        return MultiLang.getString("uiuforep0000884");
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new SaveRepDataCmd(ufoReport);
    }
    
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setImageFile("reportcore/savefile.gif");
        uiDes1.setName(getMenuName());
        uiDes1.setPaths(getPaths());
        uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes2.setPaths(new String[]{});
        uiDes2.setToolBar(true);
        uiDes2.setGroup(MultiLang.getString("file"));
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return super.isEnabled(focusComp) && isEnabledSelf();
    }
    public boolean isEnabledSelf(){
    	return isRepOpened() && isRepCanModify() && !isCommeted();
    }
}
