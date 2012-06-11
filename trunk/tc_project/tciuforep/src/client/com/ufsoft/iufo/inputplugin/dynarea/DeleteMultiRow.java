package com.ufsoft.iufo.inputplugin.dynarea;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.util.MultiLang;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class DeleteMultiRow extends AbsDynAreaDelRowExt {

    /**
     * @param plugin
     */
    public DeleteMultiRow(DynAreaInputPlugin plugin) {
        super(plugin);
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaDelRowExt#getDelCount()
     */
    protected int getDelCount() {
       return getInputCount();
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaActionExt#getMenuName()
     */
    protected String getMenuName() {
        return MultiLangInput.getString("del_multi_group");
    }

}
