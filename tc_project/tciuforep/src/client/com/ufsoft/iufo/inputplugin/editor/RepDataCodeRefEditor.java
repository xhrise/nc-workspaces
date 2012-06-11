package com.ufsoft.iufo.inputplugin.editor;

import java.awt.event.ActionEvent;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufsoft.iufo.inputplugin.inputcore.AbsCodeRefEditor;
import com.ufsoft.iufo.inputplugin.inputcore.RefInfo;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.RefTextField;

public class RepDataCodeRefEditor extends AbsCodeRefEditor {
	public RepDataCodeRefEditor(){
		super();
		editorComponent = new RefTextField();
		
		delegate = new EditorDelegate() {
			private static final long serialVersionUID = 1L;

			public void setValue(Object newValue) {
				((RefTextField)editorComponent).setText((newValue != null) ? newValue.toString() : "");
			}

			public Object getCellEditorValue() {
				return ((RefTextField)editorComponent).getText();
			}
			
			public void actionPerformed(ActionEvent e) {
				//编辑事件结束的时候会移除编辑组件
				CellsPane cp=null;
				if(editorComponent.getParent() instanceof CellsPane){
					cp=(CellsPane)editorComponent.getParent();
				}
				RepDataCodeRefEditor.this.stopCellEditing();
				
				RepDataEditor editor=RepDataDoubleEditor.getRepDataEditor(cp);
	            if(editor!=null)
	            	editor.processEnterAction(false);
	            else if (cp!=null)
	            	cp.getSelectionModel().setAnchorCell((CellPosition) cp.getSelectionModel().getAnchorCell().getMoveArea(1,0));
			}
		};
		((RefTextField)editorComponent).addActionListener(delegate);
		 m_refTextField = (RefTextField) getComponent();
	}

    protected RefInfo getRefInfo(CellsPane table, int row, int col) {
        MeasureFmt fmt = (MeasureFmt) table.getDataModel().getBsFormat(
                CellPosition.getInstance(row,col),
                MeasureFmt.EXT_FMT_MEASUREINPUT);
        return new RefInfo(fmt.getRefCodePK());
     }
}
