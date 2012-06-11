package com.ufida.report.anareport.edit;

import java.awt.Component;

import javax.swing.JLabel;

import nc.ui.pub.beans.UILabel;

import com.ufida.report.anareport.model.AnaRepField;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;


public class AnaFieldFilterRender implements SheetCellRenderer {
	private JLabel flterFieldComp = null;
	public AnaFieldFilterRender() {
		
	}

	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		if (cell == null
				|| !(cellsPane.getOperationState() == ReportContextKey.OPERATION_FORMAT))
			return null;
		AnaRepField field = null;
		if (cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) instanceof AnaRepField) {
			field = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		}
		if (field == null)
			return null;
		
		if(field.getDimInfo()!=null&&field.getDimInfo().isFilter()){
			return getPaintCompnent();
		}
			
		return null;
	}
	
	private JLabel getPaintCompnent(){
		if(flterFieldComp==null){
			flterFieldComp=new UILabel();
			flterFieldComp.setIcon(AnaFieldFixFieldRender.getBackImage("reportcore/filterField_back.gif",1));
			flterFieldComp.setOpaque(false);
			flterFieldComp.setVerticalAlignment(JLabel.TOP);
		}
		return flterFieldComp;
	}
}