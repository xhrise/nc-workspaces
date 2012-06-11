package com.ufsoft.iufo.inputplugin.ufodynarea;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;

public class UfoInsertMultiRowAbove extends AbsUfoDynAreaAddRowExt {

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
        int count;
        while((count = getInputCount(true)) > 500){
            JOptionPane.showMessageDialog(getReportDesigner(),MultiLangInput.getString("insert_count_one_time_less_than_500"));
        }
        return count;
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaActionExt#getMenuName()
     */
    protected String getMenuName() {
        return MultiLangInput.getString("insert_multi_group_above");
    }

	protected int getMemonic() {
		return 'I';
	}
	
	protected boolean isShowDialog(){
		return true;
	}
}
