package com.ufsoft.iufo.fmtplugin.key;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import nc.ui.pub.beans.UITextField;

import com.ufida.zior.event.EventManager;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * 关键字插件
 * @author zzl
 * @version 5.0
 * Create on 2004-9-20
 */

public class KeyDefPlugin extends AbstractPlugIn implements HeaderModelListener,UserActionListner {
 	
	// @edit by wangyga at 2009-3-3,上午09:57:58
	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new KeyDefRenderer());
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new KeyDefDisPlayRenderer());
		CellRenderAndEditor.getInstance().registExtSheetEditor(new KeyDefEditor(new UITextField()));
	}
	
	public void startup() {
		EventManager eventManager = getReport().getEventManager();
		if(eventManager != null){
			eventManager.addListener(this);
		}
	}
	
	/* @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		return new KeywordDescriptor(this);
	}
    public KeywordModel getKeyModel(){
        return CellsModelOperator.getKeywordModel(getCellsModel());
    }    
    private DynAreaModel getDynAreaModel(){
    	return DynAreaModel.getInstance(getCellsModel());
    }
	public void headerCountChanged(HeaderEvent e) {
		getKeyModel().headerCountChanged(e);
	}
	public void headerPropertyChanged(PropertyChangeEvent e) {		
	}    
    /**
	 * @i18n uiiufofmt00061=删除的区域中含有指标或关键字，不允许删除！
	 */
    public String isSupport(int source, EventObject e) throws ForbidedOprException {
    	//有关键字的区域不允许删除。        
        if(e == null || !(e instanceof UserUIEvent)) return null;
       
        switch (((UserUIEvent)e).getEventType()){
        case UserUIEvent.DELETECELL:
	        AreaPosition toDelArea = getDeleteAreaByEvent(e, getCellsModel());
	        // @edit by wangyga at 2009-6-1,下午02:34:43
	        
			if (toDelArea != null) {
				if (getKeyModel().getKeyVOByArea(toDelArea).size() > 0) {
					throw new ForbidedOprException(StringResource
							.getStringResource("uiiufofmt00061"));
				}
			}
			break;
        case UserUIEvent.INSERTCELL:
        	break;
			
        default:
        	break;
		
        }
        return null;
        
    }
	public void userActionPerformed(UserUIEvent e) {		
		//处理事件。
	    switch (e.getEventType()){
	        //删除区域包含关键字不允许删除
	        case UserUIEvent.DELETECELL:
	    		processDeleteCellEvent(e);
	    		break;
	    	case UserUIEvent.INSERTCELL:
	    		processInsertCellEvent(e);
	    		break;
	    }
	}
	private void processInsertCellEvent(UserUIEvent e) {
		int insertType = (Integer)e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		boolean hor = insertType == DeleteInsertDialog.CELL_MOVE_RIGHT;
		AreaPosition extendDirArea = FormulaModel.getExtendDirectionArea(false, getCellsModel(), aimArea, hor);
		int dRow = hor ? 0 : aimArea.getHeigth();
		int dCol = hor ? aimArea.getWidth() : 0;
		getKeyModel().moveKeyImpl(extendDirArea,dRow,dCol);
	}
	private void processDeleteCellEvent(UserUIEvent e) {
		int delType = (Integer)e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		boolean hor = delType == DeleteInsertDialog.CELL_MOVE_LEFT;
		AreaPosition extendDirArea = FormulaModel.getExtendDirectionArea(false, getCellsModel(), aimArea, hor);
		int dRow = hor ? 0 : -aimArea.getHeigth();
		int dCol = hor ? -aimArea.getWidth() : 0;
		getKeyModel().moveKeyImpl(extendDirArea,dRow,dCol);
	}
	/**
	 * 不是删除单元格和删除行列事件时，返回null。
	 * @param e
	 * @param cellsModel
	 * @return
	 */
	public static AreaPosition getDeleteAreaByEvent(EventObject e,CellsModel cellsModel){
		  if(e instanceof UserUIEvent){
	        	UserUIEvent userUIEvent = (UserUIEvent) e;
	        	if(userUIEvent.getEventType() == UserUIEvent.DELETECELL){
	        		return (AreaPosition) userUIEvent.getNewValue();
	        	}
	        }else if(e instanceof HeaderEvent){
	        	HeaderEvent headerEvent = (HeaderEvent) e;
	        	if(!headerEvent.isHeaderAdd()){
	        		if(headerEvent.isRow()){
	        			int width = cellsModel.getColNum();
	        			return AreaPosition.getInstance(headerEvent.getStartIndex(),0,width,headerEvent.getCount());
	        		}else{
	        			int height = cellsModel.getRowNum();
	        			return AreaPosition.getInstance(0,headerEvent.getStartIndex(),headerEvent.getCount(),height);
	        		}
	        	}
	        }
		   	return null;
	}
	public int getPriority(){
		  return HeaderModelListener.MAX_PRIORITY;
	  }
}
 