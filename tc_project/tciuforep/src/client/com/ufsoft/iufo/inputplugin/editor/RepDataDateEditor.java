package com.ufsoft.iufo.inputplugin.editor;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DateEditor;

public class RepDataDateEditor extends DateEditor {
	public RepDataDateEditor(){
		super();
		delegate = new EditorDelegate() {
			private static final long serialVersionUID = 1L;

			public void setValue(Object newValue) {
				((JTextField)editorComponent).setText((newValue != null) ? newValue.toString() : "");
			}

			public Object getCellEditorValue() {
				return ((JTextField)editorComponent).getText();
			}
			
			public void actionPerformed(ActionEvent e) {
				//编辑事件结束的时候会移除编辑组件
				CellsPane cp=null;
				if(editorComponent.getParent() instanceof CellsPane){
					cp=(CellsPane)editorComponent.getParent();
				}
				RepDataDateEditor.this.stopCellEditing();
				
				RepDataEditor editor=RepDataDoubleEditor.getRepDataEditor(cp);
	            if(editor!=null)
	            	editor.processEnterAction(false);
	            else if (cp!=null)
	            	cp.getSelectionModel().setAnchorCell((CellPosition) cp.getSelectionModel().getAnchorCell().getMoveArea(1,0));
			}
		};
		((JTextField)editorComponent).addActionListener(delegate);
		m_textField = (JTextField)getComponent();
	}
}
