package com.ufsoft.iufo.inputplugin.measure;

import java.awt.Component;
import java.util.EventObject;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 
 * @author zzl 2005-6-15
 */
public class MeasureInputPlugin extends AbstractPlugIn{
    
	static{
		CellRenderAndEditor.registEditor(MeasureFmt.EXT_FMT_MEASUREINPUT, new MeasureInputEditor());
	}
	
    /*
     * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
     */
    protected IPluginDescriptor createDescriptor() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {        
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {        
    }



    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        return new String[]{MeasureFmt.EXT_FMT_MEASUREINPUT};
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        return new SheetCellRenderer(){
            public Component getCellRendererComponent(CellsPane cellsPane,Object value, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
                return null;
            }
            
        };
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        return new MeasureInputEditor();
    }

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
        // TODO 自动生成方法存根
        
    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e) throws ForbidedOprException {
        // TODO 自动生成方法存根
        return null;
    }

}
