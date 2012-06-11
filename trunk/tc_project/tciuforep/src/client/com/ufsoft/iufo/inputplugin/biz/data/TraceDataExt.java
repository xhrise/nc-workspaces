package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Component;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellPosition;

public class TraceDataExt extends AbsIufoOpenedRepBizMenuExt {
	private boolean m_bCanTrace=false;
	private boolean m_bTotal=false;
	
	public TraceDataExt(UfoReport ufoReport){
		super(ufoReport);
	}
    
    public void setCanTrace(boolean bCanTrace) {
        this.m_bCanTrace = bCanTrace;
    }
    
    @Override
	protected String getGroup() {
		return "traceDataGroup";
	}
    
    protected String[] getPaths() {
        return doGetDataMenuPaths();
    }

    protected String getMenuName() {
        return StringResource.getStringResource("miufotableinput0003");//"º∆À„";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new TraceDataCmd(ufoReport);
    }
    
    public Object[] getParams(UfoReport container) {
    	CellPosition[] cells=container.getCellsModel().getSelectModel().getSelectedCells();
    	return new Object[]{Boolean.valueOf(m_bTotal),cells};
    }

    public boolean isEnabled(Component focusComp) {
        return m_bCanTrace && super.isEnabled(focusComp);
    }

	public boolean isTotal() {
		return m_bTotal;
	}

	public void setTotal(boolean total) {
		m_bTotal = total;
	}
}
