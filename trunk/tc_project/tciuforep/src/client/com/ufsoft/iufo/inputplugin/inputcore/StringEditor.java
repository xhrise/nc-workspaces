/*
 * 创建日期 2004-11-9
 */
package com.ufsoft.iufo.inputplugin.inputcore;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.ufsoft.table.re.DefaultSheetCellEditor;
/**
 * IUFO对应的数值型数据编辑器.
 */
public class StringEditor extends DefaultSheetCellEditor {
    private final char[] forbidChars = new char[]{'&'}; 
	/**
	 *  
	 */
	public StringEditor() {
		super(new JTextField());
		((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
	}
    /**
     * 设置字符串允许的长度
     * @param charLength void
     */
    public void setCharLength(int charLength){
        JTextField textField = (JTextField) super.editorComponent;
        textField.setDocument(new LengthCtrlDoc(charLength));
    }
    private class LengthCtrlDoc extends PlainDocument {
        private static final long serialVersionUID = 5507933420106543610L;
        private int charLength = 64;
        public LengthCtrlDoc(int charLength) {
            this.charLength = charLength;
        }
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            for (int i = 0; i < forbidChars.length; i++) {
                if(str != null && str.indexOf(forbidChars[i]) != -1){
                    return;
                }
            }
            String name = getText(0, offs) + str
                        + getText(offs, getLength() - offs);            
            if (name.length() > charLength) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.insertString(offs, str, a);
        }
    }
}