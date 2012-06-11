package com.ufsoft.table.re;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.ufida.iufo.pub.tools.DateUtil;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.UnifyDateFormat;

/**
 * �������͵ı༭��.��������ָ����ʱ��String��ʽ����.
 * @author chxiaowei 2007-3-27
 */
public class DateEditor extends AbstractRefEditor {
	protected JTextField m_textField;
	/**ʹ���Զ���ĸ�ʽ����*/
	private UnifyDateFormat udf = null;
	/**�༭����Ӧ�ĵ�Ԫ��*/
	private Cell m_cell;
	/**
	 * ���캯��
	 */
	public DateEditor() {
		super();
		m_textField = (JTextField)getComponent();
	}
	
	/**
	 * �μ����� ��װ����Ķ�����Date.
	 * 
	 * @param table
	 * @param value Date����
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return Component
	 */
	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		super.getTableCellEditorComponent(table, value, isSelected, row, column);
		
		//���ԭ���Ĳ���Ϊ�ջ��߲�ΪString���ͣ��򷵻ؿ�ֵ
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		if (value == null || !(value instanceof String)) {
			value = null;
		}
		m_cell = table.getCell(row,column);
        if(m_cell.getFormat() != null){
        	 IufoFormat format = (IufoFormat) m_cell.getFormat();
        	 if(format.getDateType()==1){
        		 udf = UnifyDateFormat.getDateFormatInstance(8); 
        	 }else{
        		 udf = UnifyDateFormat.getDateFormatInstance(7);
        	 }
        }
		setValue(value);
        return getComponent();
	}
	
	/*
     * @see com.ufsoft.table.re.DefaultSheetCellEditor#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        if(value == null || value.equals("")){
            m_textField.setText("");
            return;
        }
        m_textField.setText(value.toString());
//        Date d = DateUtil.convertStrToDate((String)value);
//        //modify by ����� ������ص�����Ϊnull�����׳��쳣
//        if (d != null) {
//			String text = udf.format(d);
//			m_textField.setText(text);
//		}
       
    }
    
	@Override
	public boolean stopCellEditing() {
		String editorValue=m_textField.getText();
		setValue(editorValue);
		return super.stopCellEditing();
	}

	@Override
	protected IRefComp getRefComp(CellsPane table, Object value, int row, int col) {
		return new TimeRefComp(DateUtil.convertStrToDate((String)value),(RefTextField) getComponent());
	}
	
	public int getEditorPRI() {
		return super.getEditorPRI()+1;
	}
}
