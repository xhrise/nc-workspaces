package com.ufsoft.report.sysplugin.edit;

import java.util.EventObject;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.combinecell.CombineCellCmd;
import com.ufsoft.report.sysplugin.insertdelete.DeleteCmd;
import com.ufsoft.report.sysplugin.insertdelete.InsertCellCmd;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.BorderPlayRender;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * 编辑插件
 * @author zzl 2005-5-24
 */
public class EditPlugin extends AbstractPlugIn implements UserActionListner{

    private CellsModel m_cellsModel;
 
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#setReport(com.ufsoft.report.UfoReport)
     */
    public void setReport(UfoReport report) {
        super.setReport(report);
        m_cellsModel = getReport().getCellsModel();
    }

    static{
    	CellRenderAndEditor.getInstance().registRender(BorderPlayRender.class, new BorderPlayRender());//注册渲染器		
    }
    
    /* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
//		getReport().getTable().getCells().getReanderAndEditor().registRender(BorderPlayRender.class, new BorderPlayRender());//注册渲染器		
	}
	
    /*
     * 此处的事件不要删除，还是有用的
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void userActionPerformed(UserUIEvent e) {
	    if(e instanceof UserUIEvent){
	        UserUIEvent ee = (UserUIEvent) e;
	        if(ee.getEventType() == UserUIEvent.INSERTCELL){
	            int insertType = ((Integer)(ee.getOldValue())).intValue();
	            AreaPosition aimArea = (AreaPosition) ee.getNewValue();
	            AreaPosition toMoveArea = InsertCellCmd.getToMoveArea(aimArea,insertType,m_cellsModel);	            
//	            CombineCellCmd.delCombineCell(toMoveArea, getReport().getTable());
	        }else if(ee.getEventType() == UserUIEvent.DELETECELL){
	            int deleteType = ((Integer)(ee.getOldValue())).intValue();
	            AreaPosition aimArea = (AreaPosition) ee.getNewValue();
	            AreaPosition toMoveArea = DeleteCmd.getToMoveArea(aimArea,deleteType,m_cellsModel);
	            CombineCellCmd.delCombineCell(toMoveArea, getReport().getTable());
	        }
	    }

    }

  
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
	     
		return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){
            protected IExtension[] createExtensions() {
            	//modify by 王宇光 2008-4-28 剪切，复制使用统一接口

            	ICommandExt extCutAll = EditExt.getInstance(getReport(),EditParameter.CUT,EditParameter.CELL_ALL);
            	ICommandExt extCutContent = EditExt.getInstance(getReport(),EditParameter.CUT,EditParameter.CELL_CONTENT);
            	ICommandExt extCutFormat = EditExt.getInstance(getReport(),EditParameter.CUT,EditParameter.CELL_FORMAT);
            	
            	ICommandExt extCopyAll = EditExt.getInstance(getReport(),EditParameter.COPY,EditParameter.CELL_ALL);
            	ICommandExt extCopyContent = EditExt.getInstance(getReport(),EditParameter.COPY,EditParameter.CELL_CONTENT);
            	ICommandExt extCopyFormat = EditExt.getInstance(getReport(),EditParameter.COPY,EditParameter.CELL_FORMAT);
            		            	
        		ICommandExt extPaste = new EditPasteExt(getReport());
        		ICommandExt extClearAll = new EditClearAllExt(getReport());
        		ICommandExt extClearContent = new EditClearContentExt(getReport());
        		ICommandExt extClearFormat = new EditClearFormatExt(getReport());
//        		ICommandExt formatBrushExt = new FormatBrushExt(getReport());
                return new IExtension[] { extCopyAll, extCopyContent,
						extCopyFormat, extCutAll, extCutContent, extCutFormat,
						extPaste, extClearAll, extClearContent, extClearFormat,};
//						formatBrushExt };
            }
            
        };
    }
   
}


