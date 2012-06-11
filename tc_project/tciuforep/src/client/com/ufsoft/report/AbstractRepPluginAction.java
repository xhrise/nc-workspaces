/**
 * 
 */
package com.ufsoft.report;

import com.ufida.dataset.IContext;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.view.Viewer;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.UFOTable;


/**
 * @author wangyga
 * 报表插件Action基类
 * @created at 2009-9-3,下午01:22:02
 *
 */
public abstract class AbstractRepPluginAction extends AbstractPluginAction{

	protected CellsModel getCellsModel(){
		ReportDesigner editor = getEditor();
		return editor == null ? null : editor.getCellsModel();
	}
	
	protected CellsPane getCellsPane(){
		ReportDesigner editor = getEditor();
		return editor == null ? null : editor.getCellsPane();
	}
	
	protected UFOTable getTable(){
		ReportDesigner editor = getEditor();
		return editor == null ? null : editor.getTable();
	}
	
	protected AreaPosition[] getSelectedAreas(){
		return getCellsModel().getSelectModel().getSelectedAreas();
	}
	
	protected boolean isFormatState(){
		IContext context = getContext();
		if(context == null){
			throw new IllegalArgumentException();
		}
		int state = new Integer("" + context.getAttribute(ReportContextKey.OPERATION_STATE));
		return state == ReportContextKey.OPERATION_FORMAT ? true : false;
	}
	
	protected IContext getContext(){
		ReportDesigner editor = getEditor();
		return editor == null ? null : editor.getContext();
	}
	
	private ReportDesigner getEditor(){
		if(getPlugin() == null){
    		return null;
    	}

		Viewer viewer = getMainboard().getCurrentView();
		if(!(viewer instanceof ReportDesigner)){
			return null;
		}
		return (ReportDesigner)viewer;
	}

}
