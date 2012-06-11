package com.ufsoft.iufo.inputplugin.key;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.iufo.inputplugin.editor.RepDataDoubleEditor;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DefaultSheetCellEditor;




/**
 * 行号关键字的编辑器。
 * 控制输入定长度的数字。
 * @author zzl 2005-9-5
 */
public class RowNumKeyEditor extends DefaultSheetCellEditor{
    /**
     *  
     */
    public RowNumKeyEditor() {
        super();
		JTextField txt=new UITextField();
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
				RowNumKeyEditor.this.stopCellEditing();
				
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
    
    /*
     * @see com.ufsoft.table.re.DefaultSheetCellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        //去掉前面的0。
        String text = (String) super.getCellEditorValue();
        if(text == null || "".equals(text.trim())){
            return text;
        }
        text = new Integer(text).toString();
        return text;
    }

    /**
     * 设置字符串允许的长度
     * @param charLength void
     */
    public void setCharLength(int charLength){
        JTextField textField = (JTextField) super.editorComponent;
        textField.setDocument(new LengthAndNumCtrlDoc(charLength));
    }
    /**
     * 控制长度和输入数字
     * @author zzl 2005-9-5
     */
    private class LengthAndNumCtrlDoc extends PlainDocument {
        private static final long serialVersionUID = 9034450056497639620L;
        private int charLength = 64;
        public LengthAndNumCtrlDoc(int charLength) {
            this.charLength = charLength;
        }
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            String name = getText(0, offs) + str
                        + getText(offs, getLength() - offs);            
            if (name.length() > charLength || !isInteger(name)) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.insertString(offs, str, a);
        }
        private boolean isInteger(String text){
            try{
                Integer.parseInt(text);
                return true;
            }catch(Exception e){
                return false;
            }
        }
    }
}
