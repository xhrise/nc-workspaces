package com.ufsoft.report.sysplugin.edit;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.UFOTable;

/**
 * <pre>
 * </pre>
 * @deprecated by 2008-4-28 ����� ���ƣ����У��ȱ༭����ͳһ�ӿڣ�EditExt
 */
public class EditCopyAllExt extends EditCopyExt{
	public EditCopyAllExt(UfoReport report) {
		super(report);
	}
	
    /* @see com.ufsoft.report.plugin.ICommandExt#getName()
     */
    public String getName() {
        return MultiLang.getString("miufo1000362");//"ȫ��";
    }

    /*
     * @see com.ufsoft.report.sysplugin.EditCopyExt#getCopyType()
     */
    protected int getCopyType() {
        return UFOTable.CELL_ALL;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("miufo1000362"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000653")});
        return new ActionUIDes[]{uiDes};
    }
}
