package com.ufsoft.iufo.inputplugin.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DoubleEditor;

public class RepDataDoubleEditor extends DoubleEditor {
	public RepDataDoubleEditor() {
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
				RepDataDoubleEditor.this.stopCellEditing();
				
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
		
		JTextField editorComponent = (JTextField) getComponent();
		editorComponent.setHorizontalAlignment(JTextField.RIGHT);
		m_textField = (JTextField) getComponent();
		m_textField.setDocument(new FloatDoc());
		fmt.setGroupingUsed(false);
        fmt.setMaximumFractionDigits(10);
	}
	
	public static RepDataEditor getRepDataEditor(CellsPane cp){
		Component comp=cp;
		while (comp!=null && comp instanceof RepDataEditor==false){
			comp=comp.getParent();
		}
		return (RepDataEditor)comp;
	}

}
