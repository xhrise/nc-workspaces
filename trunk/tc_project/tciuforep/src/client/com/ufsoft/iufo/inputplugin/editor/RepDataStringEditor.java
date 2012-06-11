package com.ufsoft.iufo.inputplugin.editor;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufsoft.iufo.inputplugin.inputcore.StringEditor;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;

public class RepDataStringEditor extends StringEditor {
	public RepDataStringEditor(){
		super();
		JTextField txt=new JTextField();
		editorComponent = txt;
		
		delegate = new EditorDelegate() {
			private static final long serialVersionUID = 6234181259639446513L;
	
			public void setValue(Object newValue) {
				((JTextField)editorComponent).setText((newValue != null) ? newValue.toString() : "");
			}
	
			public void actionPerformed(ActionEvent e) {
				//编辑事件结束的时候会移除编辑组件
				CellsPane cp=null;
				if(editorComponent.getParent() instanceof CellsPane){
					cp=(CellsPane)editorComponent.getParent();
				}
				RepDataStringEditor.this.stopCellEditing();
				
				RepDataEditor editor=RepDataDoubleEditor.getRepDataEditor(cp);
	            if(editor!=null)
	            	editor.processEnterAction(false);
	            else if (cp!=null)
	            	cp.getSelectionModel().setAnchorCell((CellPosition) cp.getSelectionModel().getAnchorCell().getMoveArea(1,0));
			}
	
			public Object getCellEditorValue() {
				return ((JTextField)editorComponent).getText();
			}
		};
		((JTextField)editorComponent).addActionListener(delegate);
		((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
	}
}
