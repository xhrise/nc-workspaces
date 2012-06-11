package com.ufsoft.report.fmtplugin.formula;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**
 * 简化单元公式定义
 * 
 * @author chxw
 * 2008-4-16
 */
public class AreaFormulaDefExt extends AbsFormulaExt{
	public AreaFormulaDefExt(AreaFormulaPlugin formulaPlugin){
		super(formulaPlugin);
	}
	
	public String getName() {
		return MultiLang.getString("miufo1000909");//"单元公式";
	}

    public String[] getPath() {
        return new String[]{MultiLang.getString("format")};
    }	
    
	public UfoCommand getCommand() {
		return new AreaFormulaDefCmd();
	}
	
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getName());
        uiDes.setPaths(getPath());
        uiDes.setGroup("formulaExt");
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,0));
        //是否加入公式定义右键菜单
        if(this.getFormulaPlugin().isFmlDefPopMenuVisible()){
        	ActionUIDes uiDes1 = new ActionUIDes();
            uiDes1.setName(getName());
            uiDes1.setPopup(true);
            return new ActionUIDes[]{uiDes, uiDes1};
        } else{
        	return new ActionUIDes[]{uiDes};
        }
    }
    
}
