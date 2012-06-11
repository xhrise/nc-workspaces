/*
 * �������� 2004-11-10
 */
package com.ufsoft.report.sysplugin.help;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.help.CSH;
import javax.help.DefaultHelpBroker;
import javax.help.HelpBroker;
import javax.swing.FocusManager;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * �����˵�
 * @author wupeng
 * @version 3.1
 */
public class HelpExt extends AbsActionExt{// implements IMainMenuExt {

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand(){
            /**
			 * @i18n report00048=û�а���ϵͳ�����ϵͳ·�����ò���
			 */
            public void execute(Object[] params) {
                HelpBroker hb = ResConst.getHelpBroker();
                Window window = FocusManager.getCurrentManager().getFocusedWindow();
                if(hb != null){                                       
                    //���õ�ǰ�����Ӧ�İ�������.
                    Component component = FocusManager.getCurrentManager().getFocusOwner(); 
                    if(component instanceof RootPaneContainer){
                        component = ((RootPaneContainer)component).getRootPane();
                    }
                    hb.setCurrentID(CSH.getHelpIDString(component));
                    //��ģ̬�Ի����ý���ʱ,ʹ�����Ի����Ϊ������Ӵ���.
                    ((DefaultHelpBroker)hb).setActivationWindow(window);
//                    hb.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
                    hb.setDisplayed(true);
                }else{
                    UfoPublic.sendErrorMessage(MultiLang.getString("report00048"), window,null);
                }
            }		    
		};
	}
	/**
	 * @param container
	 * @return
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return null;
	}


    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("miufo1000651"));
        uiDes.setPaths(new String[]{MultiLang.getString("help")});
        //xulm 2009-09-03 ���ð������ܷ���
        uiDes.setGroup("help");
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
        uiDes.setImageFile("reportcore/help.jpg");
        return new ActionUIDes[]{uiDes};
    }
}
  