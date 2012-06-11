package com.ufsoft.table.re;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsPane;

/**
 * 不是参照型的日期编辑器,主要对格式为日期时的输入校验
 * @author wangyga 2008-7-1
 *
 */
public abstract class AbstractDateEditor extends DefaultSheetCellEditor{
    private JTextField m_TextField =  null;
   	
    /**
     * 构建JTextField型的日期编辑器
     */
	public AbstractDateEditor(){
		super(new JTextField());
		m_TextField = (JTextField)getComponent();
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
     * 终止编辑前验证参照内容录入是否有效
     * 
     * @return boolean 输入内容验证无效/验证通过
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
