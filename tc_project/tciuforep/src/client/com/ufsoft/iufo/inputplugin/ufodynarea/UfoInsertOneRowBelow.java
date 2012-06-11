package com.ufsoft.iufo.inputplugin.ufodynarea;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class UfoInsertOneRowBelow extends AbsUfoDynAreaAddRowExt {
	
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
    
	protected String getIconName() {
		return "images/reportcore/addrow.gif";
	}

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaActionExt#getMenuName()
     */
    protected String getMenuName() {
        return MultiLangInput.getString("insert_one_group_below");
    }

	protected KeyStroke getActionKeyStroke() {
		return KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_MASK);
	}

	protected int getMemonic() {
		return 'B';
	}
}
