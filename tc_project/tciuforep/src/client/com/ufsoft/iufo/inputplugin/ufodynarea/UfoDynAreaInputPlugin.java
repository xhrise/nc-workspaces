package com.ufsoft.iufo.inputplugin.ufodynarea;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.inputplugin.dynarea.RowNumber;
import com.ufsoft.iufo.inputplugin.dynarea.RowNumberRender;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * 
 * @author zzl 2005-6-20
 */
public class UfoDynAreaInputPlugin extends AbstractPlugin{
	static{
		CellRenderAndEditor.registRender(RowNumber.class,new RowNumberRender());
	}
	
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoInsertOneRowBelow(),new UfoCopyInsertOneRowBelow(),new UfoInsertOneRowAbove(),
				new UfoInsertMultiRowBelow(),new UfoInsertMultiRowAbove(),
				new UfoDeleteOneRow(),new UfoDeleteMultiRow()};
	}

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup(){
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
    }
}
