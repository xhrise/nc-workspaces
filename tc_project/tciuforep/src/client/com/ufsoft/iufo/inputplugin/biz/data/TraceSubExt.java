package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Component;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellPosition;
import com.ufsoft.iufo.resource.StringResource;

public class TraceSubExt extends AbsIufoOpenedRepBizMenuExt {
	private boolean m_bCanTrace=false;
	
	public TraceSubExt(UfoReport ufoReport){
		super(ufoReport);
	}
	
	@Override
	protected String getGroup() {
		return "traceDataGroup";
	}
	
    protected String[] getPaths() {
        return doGetDataMenuPaths();
    }

    /**
	 * @i18n uiuforep00094=查看汇总下级
     * @i18n miufohbbb00101=查看汇总下级
	 */
    protected String getMenuName() {
        return StringResource.getStringResource("miufohbbb00101");
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new TraceSubCmd(ufoReport);
    }
    
    public Object[] getParams(UfoReport container) {
    	CellPosition[] cells=container.getCellsModel().getSelectModel().getSelectedCells();
    	return cells;
    }

    public boolean isEnabled(Component focusComp) {
        return m_bCanTrace && super.isEnabled(focusComp);
    }

	public boolean isCanTrace() {
		return m_bCanTrace;
	}

	public void setCanTrace(boolean canTrace) {
		m_bCanTrace = canTrace;
	}
}
  