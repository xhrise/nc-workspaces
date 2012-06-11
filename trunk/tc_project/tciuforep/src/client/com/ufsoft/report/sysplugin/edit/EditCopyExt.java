package com.ufsoft.report.sysplugin.edit;

import java.awt.Component;

import javax.swing.KeyStroke;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.util.MultiLang;
/**
 * <pre>
 * </pre>
 * @deprecated by 2008-4-28 王宇光 复制，剪切，等编辑功能统一接口：EditExt
 */
public abstract class EditCopyExt extends AbsActionExt{// IMainMenuExt{
	private UfoReport _report;

	public EditCopyExt(UfoReport report) {
		super();
		_report  = report;
	}
	
	/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand(){
			public void execute(Object[] params) {
				UfoReport rep = (UfoReport)params[0];
				//modify by 2008-4-29 王宇光 ，关掉此方法：剪切，复制，粘贴等行为由各个业务插件完成
//				rep.getTable().copy(getCopyType());
			}
			
		};
	}
//	/* @see com.ufsoft.report.plugin.ICommandExt#getName()
//	 */
//	abstract public String getName(); 
	/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return new String[]{MultiLang.getString("miufo1000653")};
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
     */
    public String getImageFile() {
        return "copy.gif";
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
     */
    public KeyStroke getAccelerator() {
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.IExtension#getHint()
     */
    public String getHint() {
        return null;
    }
    protected abstract int getCopyType();
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return StateUtil.isAnchorEditable(_report.getCellsModel());//StateUtil.isCellsPane(null,focusComp);
    }
}



