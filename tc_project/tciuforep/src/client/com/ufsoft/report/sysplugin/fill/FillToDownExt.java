package com.ufsoft.report.sysplugin.fill;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class FillToDownExt extends FillExt{
	/**
	 * @param report
	 */
	public FillToDownExt(UfoReport report) {
		super(report);
	}
	/* @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("uiuforep0000502");//"ÏòÏÂÌî³ä";
	}
	
	/* @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		return "todown.gif";
	}
    /*
     * @see com.ufsoft.report.bsplugin.fill.FillExt#getFillType()
     */
    protected int getFillType() {
        return FillCmd.FillToDown;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(MultiLang.getString("uiuforep0000502"));
        uiDes1.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("uiuforep0000500")});
        uiDes1.setGroup("insertAndFill");
        
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes2.setPopup(true);
        uiDes2.setPaths(new String[]{MultiLang.getString("uiuforep0000500")});
        uiDes2.setGroup("insertAndFill");
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
}