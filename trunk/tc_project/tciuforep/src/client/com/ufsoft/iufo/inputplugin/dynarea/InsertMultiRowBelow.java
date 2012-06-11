package com.ufsoft.iufo.inputplugin.dynarea;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.util.MultiLang;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class InsertMultiRowBelow extends AbsDynAreaAddRowExt {

    /**
     * @param plugin
     */
    public InsertMultiRowBelow(DynAreaInputPlugin plugin) {
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
        int count;
        while((count = getInputCount()) > 500){
            JOptionPane.showMessageDialog(getPlugin().getReport(),MultiLangInput.getString("insert_count_one_time_less_than_500"));
        }
        return count;
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaActionExt#getMenuName()
     */
    protected String getMenuName() {
        return MultiLangInput.getString("insert_multi_group_below");
    }


}
