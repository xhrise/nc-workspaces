package com.ufsoft.report.sysplugin.edit;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.UFOTable;
/**
 * <pre>
 * </pre>
 * @deprecated by 2008-4-28 王宇光 复制，剪切，等编辑功能统一接口：EditExt
 */
public class EditCopyContentExt extends EditCopyExt{
    public EditCopyContentExt(UfoReport report) {
		super(report);
	}
	
    /* @see com.ufsoft.report.plugin.ICommandExt#getName()
     */
    public String getName() {
        return MultiLang.getString("uiuforep0001001");//"仅内容";
    }
    protected int getCopyType() {
        return UFOTable.CELL_CONTENT;
    }   
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0001001"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000653")});
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        return new ActionUIDes[]{uiDes};
    }
}
