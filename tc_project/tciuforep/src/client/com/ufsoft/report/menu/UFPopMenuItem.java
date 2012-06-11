package com.ufsoft.report.menu;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;

public class UFPopMenuItem extends UFMenuItem{
	private int aimComp ;
	
    /**
     * @param extension
     * @param report
     */
    public UFPopMenuItem(IActionExt extension, ActionUIDes uiDes, UfoReport report) {
        super(extension, uiDes,report);
        aimComp = uiDes.getPopupAimComp();
    }

	public int getAimComp() {
		return aimComp;
	}
}