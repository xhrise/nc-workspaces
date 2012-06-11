package com.ufsoft.iufo.inputplugin.ufobiz;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.view.Viewer;

public abstract class AbsUfoOpenedRepBizMenuExt extends AbsUfoBizMenuExt{
    public boolean isEnabled() {
        return isRepOpened();
    }
	
    protected boolean isRepOpened(){
    	Viewer curView=getCurrentView();
    	if (curView==null || curView instanceof RepDataEditor==false)
    		return false;
    	
    	RepDataEditor editor=(RepDataEditor)curView;
    	return editor.getAloneID()!=null;
    }
}
