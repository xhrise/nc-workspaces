/*
 * 创建日期 2004-11-9
 */
package com.ufsoft.table.re;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.util.UfoPublic1;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.TableConstant;
/**
 * 数据类型的编辑器.支持的数据类型为UfoDouble.
 */
public class DoubleEditor extends DefaultSheetCellEditor {
	protected JTextField m_textField;
	
	//xulm 2009-08-26 使纯小数的个位数0能够在编辑态正常显示
	protected DecimalFormat fmt = new DecimalFormat("0.##########");
    
    private boolean isPercent = false;
    private Cell m_cell;
	
	/**
	 *  
	 */
	public DoubleEditor() {
		super(new JTextField());
		JTextField editorComponent = (JTextField) getComponent();
		editorComponent.setHorizontalAlignment(JTextField.RIGHT);
		m_textField = (JTextField) getComponent();
		m_textField.setDocument(new FloatDoc());
		fmt.setGroupingUsed(false);
        fmt.setMaximumFractionDigits(10);
	}

	/**
	 * 参见父类 包装放入的对象都是Double.
	 * 
	 * @param table
	 * @param value
	 *            UfoDouble类型
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return Component
	 *  
	 */
	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		if(value instanceof Integer){
			value = new Double(value.toString());
		}		
		int digitNumber = TableConstant.UNDEFINED;
        m_cell = table.getCell(row,column);
        if(m_cell.getFormat() != null){
            IufoFormat format = (IufoFormat) m_cell.getFormat();
            if(format.isHasPercent() == TableConstant.TRUE){
                isPercent = true;
            }else{
                isPercent = false;
            }
            digitNumber = format.getDecimalDigits();
            if (digitNumber<0)
            	digitNumber=2;
            if (isPercent)
            	digitNumber+=2;
        }
		//如果原来的数据为空或者是Class类型的数据,那么构建一个String类型的构造器;否则构造一个参数为String的该类型的构造器.
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		if (value == null||value.equals("")||!(value instanceof Double)) {
			value = null;
		}
        setValue(value);
        if(digitNumber != TableConstant.UNDEFINED && value instanceof Double){
        	setValue(UfoPublic1.roundDouble((Double)value, digitNumber));
        }
		return getComponent();
	}
	/**
	 * 终止编辑. 如果constructor非空,那么以输入框中的String为构造参数构造返回值.
	 * 
	 * @return boolean
	 *  
	 */
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}
	/**
	 * 返回编辑结果
	 * 
	 * @return Object UfoDouble
	 */
	public Object getCellEditorValue() {
	    if("-".equals(m_textField.getText()) || "".equals(m_textField.getText())){
	        return null;
	    }
        Double d = new Double(m_textField.getText());       
        //根据有效位数截断，目的是与web录入保持一致。
        IufoFormat cellFmt = (IufoFormat) m_cell.getFormat();
        int decimalDigits = cellFmt == null ? 2 : cellFmt.getDecimalDigits();
        if(decimalDigits == TableConstant.UNDEFINED){
            decimalDigits = 2;
        }
        /*****************百分号的与小数位数处理**************************************/
        if(isPercent){
            d = new Double(d.doubleValue()/100.0);
            decimalDigits = decimalDigits + 2;
        }
 /**********************************************************/
//        String text = fmt.format(d.doubleValue());
//        int index = text.indexOf('.');
//        if(index != -1){
//            index = index + decimalDigits + 1;
//            index = index < text.length() ? index : text.length();
//            text = text.substring(0,index);
//        }
//
//		return new Double(text);
        return UfoPublic1.roundDouble(d, decimalDigits);
	}
    /*
     * @see com.ufsoft.table.re.DefaultSheetCellEditor#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        if(value == null || value.toString().equals("")){
            m_textField.setText("");
            return;
        }
        Double d = (Double) value;
        if(isPercent && value.toString() != null && !value.toString().equals("")){
            d = new Double(d.doubleValue()*100);
            d = new Double(Math.round(d.doubleValue()*10E10)/10E10);
        }
        String text = fmt.format(d.doubleValue());
        m_textField.setText(text);
    }
	/**
	 * 
	 * 控制文本输入
	 * 
	 * @author wupeng
	 * @version 3.1
	 */
    public class FloatDoc extends PlainDocument {
		private static final long serialVersionUID = -7311614824761254680L;

		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			String name = getText(0, offs) + str + getText(offs, getLength() - offs);
			Pattern pa = Pattern.compile("(^(\\-){1}$)|(^(\\-){0,1}(\\d+)((\\.){0,1})(\\d*)$)");
			Matcher ma = pa.matcher(name);
			if (!ma.matches())
				return;
			super.insertString(offs, str, a);
		}
		
	    public void replace(int offset, int length, String text,
                AttributeSet attrs) throws BadLocationException {
			if (length == 0 && (text == null || text.length() == 0)) {
			    return;
			}
			String name = getText(0, offset) + text+ getText(offset, getLength() - offset);
			Pattern pa = Pattern.compile("(^(\\-){0,1})(\\d*)((\\.){0,1})(\\d*)$"); 
	        Matcher ma = pa.matcher(name); 
	        if(!ma.find())
	        	return;	
				
		        if (length > 0) {
		            remove(offset, length);
		        }
		        if (text != null && text.length() > 0) {
		            insertString(offset, text, attrs);
		        }
		}
	}
	public int getEditorPRI() {
		return super.getEditorPRI()+1;
	}



}
