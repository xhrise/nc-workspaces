package com.ufsoft.table.re;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsPane;


/**
 * 
 * @author zzl 2005-6-17
 */
public abstract class AbstractRefEditor extends DefaultSheetCellEditor{
    protected RefTextField m_refTextField;
    private IRefComp m_refComp;
    /**
     */
    public AbstractRefEditor() {
        super(new RefTextField());	
        m_refTextField = (RefTextField) getComponent();
    }
    /**
	 * 终止编辑. 验证参照类型是否有效.
	 * 
	 * @return boolean
	 * @i18n report00103=输入内容不是有效参照内容！
	 *  
	 */
	public boolean stopCellEditing() {
		if(!checkBeforStopEditing()){
			return false;
		}
	    return super.stopCellEditing();
	}
	public Object getCellEditorValue() {
	   return m_refComp.getValidateValue(m_refTextField.getText());
	}
    /*
     * @see com.ufsoft.table.re.SheetCellEditor#getTableCellEditorComponent(com.ufsoft.table.CellsPane, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(CellsPane table, Object value,
            boolean isSelected, int row, int column) {
        m_refComp = getRefComp(table,value,row,column);
        m_refTextField.setRefComp(m_refComp,value);
        m_refTextField.setText(value==null?"":value.toString());
        if(value != null && value instanceof IDName){
            m_refTextField.setText(((IDName)value).getID());  
        }
        return m_refTextField;
    }
    /**
     * 终止编辑前验证参照内容录入是否有效
     * 
     * @return boolean 输入内容验证无效/验证通过
     */
    public boolean checkBeforStopEditing() {
    	if(!"".equals(m_refTextField.getText()) && getCellEditorValue() == null){
    		JOptionPane.showMessageDialog(m_refTextField, MultiLang.getString("report00103"));
    		return false;
    	}
    	return true;
    }

    protected abstract IRefComp getRefComp(CellsPane table,Object value,int row,int col);
}
  