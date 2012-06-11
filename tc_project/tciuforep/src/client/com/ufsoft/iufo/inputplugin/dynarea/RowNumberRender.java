package com.ufsoft.iufo.inputplugin.dynarea;
import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.JLabel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DefaultSheetCellRenderer;

public class RowNumberRender extends DefaultSheetCellRenderer{
	private static final long serialVersionUID = 2991000829604225131L;
	private JLabel label = null;
	public Component getCellRendererComponent(CellsPane table, Object obj,boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
		
		// @edit by ll at 2009-5-14,上午10:25:08
		// @edit by zhaopq at 2009-4-22,上午10:51:41  此版暂时这样处理
		if(table.getOperationState() == ReportContextKey.OPERATION_FORMAT||table.getOperationState() == ReportContextKey.OPERATION_REF){
			return getPaintComponent();
		}
		Component render = super.getCellRendererComponent(table,obj,isSelected,hasFocus,row,column, cell);
		CellPosition cellPos = CellPosition.getInstance(row,column);
		CellsModel cellsModel = table.getDataModel();
		int index = RowNumber.getIndex(cellsModel, cellPos);
		try {
			Method m = render.getClass().getMethod("setText",new Class[]{String.class});
			m.invoke(render,new Object[]{""+index});
		} catch (Exception e) {
			AppDebug.debug(e);
		} 
		return render;
	}
	
	private JLabel getPaintComponent(){
		if(label==null){
			new nc.ui.pub.beans.UILabel(StringResource.getStringResource("uiiufofmt00003"));
		}
		return label;
	}
}