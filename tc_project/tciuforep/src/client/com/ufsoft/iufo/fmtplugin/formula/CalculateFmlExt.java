package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.UFMenuItem;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;

/**
 * 计算公式（即时公式和汇总公式）的定义
 * @author zzl 2005-12-15
 */
public class CalculateFmlExt extends AbsActionExt{//implements IMainMenuExt,IPopupMenuExt{

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getName()
	 */
	public String getName() {
		return StringResource.getStringResource("miufo1000909");//"单元公式";
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getHint()
	 */
	public String getHint() {
	    return StringResource.getStringResource("miufo1000909");//"单元公式";
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		return null;
	}


	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getAccelerator()
	 */
	public KeyStroke getAccelerator() {
		return null;
	}


	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new CalcFmlEditCmd();
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}
    /*
     * @see com.ufsoft.report.plugin.IMenuExt#getPath()
     */
    public String[] getPath() {
        return new String[]{StringResource.getStringResource("miufo1001615")};
    }

    
    @Override
	public void initListenerByComp(Component stateChangeComp) {
    	if(stateChangeComp instanceof UFMenuItem){
    		((UFMenuItem)stateChangeComp).putClientProperty("nc.hotkey.display","=");
    	}
	}

	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getName());
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,0));
        uiDes.setPaths(new String[]{StringResource.getStringResource("miufo1001692"),
//                StringResource.getStringResource("miufo1001615")
                });
        uiDes.setGroup("formulaDefExt");
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(getName());
        uiDes1.setPopup(true);
        return new ActionUIDes[]{uiDes,uiDes1};
    }	
}