package com.ufsoft.iufo.inputplugin.dynarea;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.plugin.ActionUIDes;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class InsertOneRowAbove extends AbsDynAreaAddRowExt {

    /**
     * @param plugin
     */
    public InsertOneRowAbove(DynAreaInputPlugin plugin) {
        super(plugin);
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaAddRowExt#isAboveRow()
     */
    protected boolean isAboveRow() {
        return true;
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaAddRowExt#getAddCount()
     */
    protected int getAddCount() {
        return 1;
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaActionExt#getMenuName()
     */
    protected String getMenuName() {
        return MultiLangInput.getString("insert_one_group_above");
    }
    
    /* add by 2008-4-8 王宇光 重写父类的此方法，把此功能加入工具栏
     * @see com.ufsoft.report.plugin.IActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setImageFile("reportcore/addrow.gif");
        uiDes.setName(getMenuName());
        uiDes.setToolBar(true);
        uiDes.setGroup(MultiLangInput.getString("miufotableinput0014"));
        
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(getMenuName());
        uiDes1.setPopup(true);
        return new ActionUIDes[]{uiDes,uiDes1};
    }
    
}
