package com.ufsoft.iufo.inputplugin.ufodynarea;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.iufo.resource.StringResource;

public class UfoCopyInsertOneRowBelow extends AbsUfoDynAreaAddRowExt{
	protected int getAddCount() {
		return 1;
	}

	protected boolean isAboveRow() {
		return false;
	}

	protected String getMenuName() {
		return StringResource.getStringResource("uiufotask00078");
	}
	
    protected boolean isAddCopy(){
    	return true;
    }

	protected KeyStroke getActionKeyStroke() {
		return KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,KeyEvent.SHIFT_MASK+KeyEvent.CTRL_MASK);
	}

	protected int getMemonic() {
		return 'E';
	}
	
	protected String getIconName() {
		return "images/reportcore/FilterOne.gif";
	}
}
