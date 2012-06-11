package com.ufsoft.iufo.inputplugin.ufodynarea;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class UfoInsertMultiRowBelow extends AbsUfoDynAreaAddRowExt {

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
        while((count = getInputCount(true)) > 500){
            JOptionPane.showMessageDialog(getReportDesigner(),MultiLangInput.getString("insert_count_one_time_less_than_500"));
        }
        return count;
    }
    
	protected String getIconName() {
		return "images/reportcore/linkage.gif";
	}

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaActionExt#getMenuName()
     */
    protected String getMenuName() {
        return MultiLangInput.getString("insert_multi_group_below");
    }

	protected KeyStroke getActionKeyStroke() {
		return KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, KeyEvent.CTRL_MASK);
	}

	protected int getMemonic() {
		return 'M';
	}
	
	protected boolean isShowDialog(){
		return true;
	}
}
