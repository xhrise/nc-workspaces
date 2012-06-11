package com.ufsoft.iufo.inputplugin.dynarea;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.util.MultiLang;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class InsertOneRowBelow extends AbsDynAreaAddRowExt {

    /**
     * @param plugin
     */
    public InsertOneRowBelow(DynAreaInputPlugin plugin) {
        super(plugin);
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaAddRowExt#isAboveRow()
     */
    protected boolean isAboveRow() {
        return false;
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
        return MultiLangInput.getString("insert_one_group_below");
    }

}
