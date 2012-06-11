package com.ufsoft.iufo.fmtplugin.key;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JTextField;

import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DefaultSheetCellEditor;

public class KeyDefEditor extends DefaultSheetCellEditor{
	
	KeyDefEditor(JTextField field) {
		super(field);
	}

	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		final CellsPane cellsPane = table;

		final CellsModel cellsModel = table.getDataModel();
		final DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		KeywordModel keyModel = CellsModelOperator.getKeywordModel(cellsModel);
		
		final CellPosition pos = CellPosition.getInstance(row,column);
		KeyVO vo = keyModel.getKeyVOByPos(pos);
		if(vo == null) return null;
		JButton btn = new nc.ui.pub.beans.UIButton(MultiLang.getString("edit"));
		btn.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {			
				AbsEditorAction editAction = null;
				if(dynAreaModel.isInDynArea(pos)){
					editAction = new DynAreaKeyMngEditorAction(cellsPane);
				}else{					
					editAction = new KeyWordSetEditorAction(cellsPane);					
				}
				editAction.execute(editAction.getParams());
			}
		});
		return btn;
	}
	
	/*
	 * @see com.ufsoft.table.re.DefaultSheetCellEditor#getEditorPRI()
	 */
	public int getEditorPRI() {
	    return 1;
	}

	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		return dynAreaModel.getKeyVOAfterDataProcess(cellPos.getRow(),cellPos.getColumn()) != null;
	}
	
}
