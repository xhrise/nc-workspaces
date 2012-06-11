package com.ufsoft.iufo.inputplugin.ufodynarea;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class UfoInsertOneRowAbove extends AbsUfoDynAreaAddRowExt {

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

	protected int getMemonic() {
		return 'F';
	}
}
