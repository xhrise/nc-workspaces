package com.ufsoft.report.sysplugin.edit;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.UFOTable;
/**
 * <pre>
 * </pre>
 * @deprecated by 2008-4-28 王宇光 复制，剪切，等编辑功能统一接口：EditExt
 */
public class EditCopyFormatExt extends EditCopyExt{
	public EditCopyFormatExt(UfoReport report) {
		super(report);
	}
	
    /* @see com.ufsoft.report.plugin.ICommandExt#getName()
     */
    public String getName() {
        return MultiLang.getString("uiuforep0001002");//"仅格式";
    }
    
    protected int getCopyType() {
        return UFOTable.CELL_FORMAT;
    }   
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0001002"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000653")});
        return new ActionUIDes[]{uiDes};
    }
}