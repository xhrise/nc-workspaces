package com.ufsoft.iufo.inputplugin.ufodynarea;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class UfoDeleteMultiRow extends AbsUfoDynAreaDelRowExt {

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaDelRowExt#getDelCount()
     */
    protected int getDelCount() {
       return getInputCount(false);
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaActionExt#getMenuName()
     */
    protected String getMenuName() {
        return MultiLangInput.getString("del_multi_group");
    }

	protected int getMemonic() {
		return 'Y';
	}
	
	protected boolean isShowDialog(){
		return true;
	}
}
