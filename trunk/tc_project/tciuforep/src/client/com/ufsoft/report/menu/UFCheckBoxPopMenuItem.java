package com.ufsoft.report.menu;

import javax.swing.Icon;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.resource.ResConst;

/**
 * @author zzl 2005-6-28
 */
public class UFCheckBoxPopMenuItem extends UFPopMenuItem{
	private static final long serialVersionUID = 1137673680526870779L;
	Icon selectIcon = ResConst.getImageIcon("reportcore/select.gif");
    Icon unSelectIcon = ResConst.getImageIcon("reportcore/blank.gif");
    /**
     * @param name
     * @param b
     */
    public UFCheckBoxPopMenuItem(IActionExt extension, ActionUIDes uiDes, UfoReport report) {
        super(extension,uiDes,report);
    }
    
    /**
     * @param b
     * @see javax.swing.AbstractButton#setSelected(boolean)
     */
    public void setSelected(boolean b) {
        super.setSelected(b);
        if(b){
            setIcon(selectIcon);
        }else{
            setIcon(unSelectIcon);
        }
    }
}