package com.ufsoft.table.re;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsPane;

/**
 * ���ǲ����͵����ڱ༭��,��Ҫ�Ը�ʽΪ����ʱ������У��
 * @author wangyga 2008-7-1
 *
 */
public abstract class AbstractDateEditor extends DefaultSheetCellEditor{
    private JTextField m_TextField =  null;
   	
    /**
     * ����JTextField�͵����ڱ༭��
     */
	public AbstractDateEditor(){
		super(new JTextField());
		m_TextField = (JTextField)getComponent();
	}
	
	/**
	 * ��ֹ�༭. ��֤���������Ƿ���Ч.
	 * 
	 * @return boolean
	 * @i18n report00103=�������ݲ�����Ч�������ݣ�
	 *  
	 */
	public boolean stopCellEditing() {
		if(!checkBeforStopEditing()){
			return false;
		}
	    return super.stopCellEditing();
	}
	public Object getCellEditorValue() {
	   return m_TextField.getText();
	}
    /*
     * @see com.ufsoft.table.re.SheetCellEditor#getTableCellEditorComponent(com.ufsoft.table.CellsPane, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(CellsPane table, Object value,
            boolean isSelected, int row, int column) {
        m_TextField.setText(value==null?"":value.toString());
        if(value != null && value instanceof IDName){
            m_TextField.setText(((IDName)value).getID());  
        }
        return m_TextField;
    }
    /**
     * ��ֹ�༭ǰ��֤��������¼���Ƿ���Ч
     * 
     * @return boolean ����������֤��Ч/��֤ͨ��
     */
    public boolean checkBeforStopEditing() {
    	if(!"".equals(m_TextField.getText()) && getCellEditorValue() == null){
    		JOptionPane.showMessageDialog(m_TextField, MultiLang.getString("report00103"));
    		return false;
    	}
    	return true;
    }

    protected abstract IRefComp getRefComp(CellsPane table,Object value,int row,int col);
	
}
