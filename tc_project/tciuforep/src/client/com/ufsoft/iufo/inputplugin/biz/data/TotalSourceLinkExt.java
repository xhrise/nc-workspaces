package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Component;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.TotalSourceEditor;

import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellPosition;

public class TotalSourceLinkExt extends AbsIufoOpenedRepBizMenuExt {

	public TotalSourceLinkExt(UfoReport ufoReport){
		super(ufoReport);
	}

    protected String[] getPaths() {
        return doGetDataMenuPaths();
    }

    protected String getMenuName() {
        return MultiLangInput.getString("miufotableinput0011");//"联查报表";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new TotalSourceLinkCmd(ufoReport);
    }
    
    public Object[] getParams(UfoReport container) {
    	CellPosition cell=container.getCellsModel().getSelectModel().getAnchorCell();
    	return new Object[]{cell};
    }

    public boolean isEnabled(Component focusComp) {
    	return true;
    }
}
