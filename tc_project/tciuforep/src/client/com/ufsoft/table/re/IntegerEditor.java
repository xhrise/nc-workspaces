/**
 * 
 */
package com.ufsoft.table.re;

import javax.swing.JTextField;

import com.ufsoft.report.util.IntegerDocument;

/**
 * @author guogang
 * @created at Jan 16, 2009,9:41:46 AM
 *
 */
public class IntegerEditor extends DefaultSheetCellEditor{

	public IntegerEditor(){
		super(new JTextField());
		JTextField editorComponent = (JTextField) getComponent();
		editorComponent.setHorizontalAlignment(JTextField.RIGHT);
		editorComponent.setDocument(new IntegerDocument());
	}

	@Override
	public Object getCellEditorValue() {
		JTextField editorComponent = (JTextField) getComponent();
		String text=editorComponent.getText();
		if("".equals(text)){
			return null;
		}
		Integer value=new Integer(text);
		return value;
	}
	
	
}
