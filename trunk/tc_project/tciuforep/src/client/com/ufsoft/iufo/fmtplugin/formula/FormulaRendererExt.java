package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.Component;

import nc.ui.pub.beans.UIMenu;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaDefPlugIn;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.UFCheckBoxPopMenuItem;
import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.report.util.MultiLang;

/**公式扩展属性渲染标志是否显示**/
public class FormulaRendererExt extends AbsActionExt{
    private final FormulaDescriptor descriptor;
    private UFCheckBoxPopMenuItem _menuItemMeasFlag = null;
    
    FormulaRendererExt(FormulaDescriptor descriptor) {
        this.descriptor = descriptor;
    }

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return null;
	}
	
	@Override
	public boolean isEnabled(Component focusComp) {
		setSelectedByMeasPlugIn(getFmlFlagMenuItem());
		return true;
	}
	
	private void setSelectedByMeasPlugIn(UFCheckBoxPopMenuItem stateChangeComp){
		stateChangeComp.setSelected(FormulaDefRenderer.isFmlRendererVisible());
	}
	
	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		FormulaDefRenderer.setFmlRendererVisible(!FormulaDefRenderer.isFmlRendererVisible());
		container.getCellsModel().fireExtPropChanged(null);
	    return new Object[]{container};
	}
	
	/**
	 * @i18n uiuforep0000790=格式
	 * @i18n miufo1004045=显示扩展属性
	 * @i18n uiufo20015=公式
	 */
    public ActionUIDes[] getUIDesArr() {
    	ToggleMenuUIDes uiDes = new ToggleMenuUIDes();
		uiDes.setName(StringResource.getStringResource("uiufo20015"));
		uiDes.setPaths(new String[]{MultiLang.getString("uiuforep0000790"), StringResource.getStringResource("miufo1004045")});
		uiDes.setCheckBox(true);
		uiDes.setGroup(DynAreaDefPlugIn.MENU_GROUP);
		return new ActionUIDes[]{uiDes};
    }		

    /**
	 * @i18n uiuforep0000790=格式
	 * @i18n miufo1004045=显示扩展属性
	 * @i18n uiufo20015=公式
	 * @return
	 */
	private UFCheckBoxPopMenuItem getFmlFlagMenuItem(){
		if(_menuItemMeasFlag == null){
			ReportMenuBar memuBar = (ReportMenuBar)getReport().getJMenuBar();
			int nMemuCount = memuBar.getMenuCount();
			for(int i = 0; i < nMemuCount; i++) {
				UFMenu menu = (UFMenu)memuBar.getMenu(i);
				if(menu.getName().equals(MultiLang.getString("uiuforep0000790"))) {
					Component[] menuItems = menu.getMenuComponents();
					for(int j = 0, size = menuItems.length; j < size; j++) {
						if(menuItems[j] instanceof UIMenu && 
								menuItems[j].getName().equals(StringResource.getStringResource("miufo1004045"))){
							Component[] subMenuItems = ((UIMenu)menuItems[j]).getMenuComponents();
							for(int k = 0, nsize = subMenuItems.length; k < nsize; k++) {
								if(subMenuItems[k] instanceof UFCheckBoxPopMenuItem && 
										subMenuItems[k].getName().equals(StringResource.getStringResource("uiufo20015"))) {
									return (_menuItemMeasFlag = (UFCheckBoxPopMenuItem)subMenuItems[k]);
								}
							}
						}    	                
					}
				}
			} 	
		} 
		return _menuItemMeasFlag;
	}
	
	private UfoReport getReport(){
		return descriptor.getReport();
	}
}
