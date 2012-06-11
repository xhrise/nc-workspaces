package com.ufsoft.iufo.fmtplugin.formula;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.ufsoft.iufo.resource.StringResource;


/**
 * @author zyjun
 *
 * 审核公式表格模型
 */
public class ComplexCheckMngTbModel extends AbstractTableModel {

    /**
     * 审核公式列表，列表中的对象为RepCheckVO
     */
    private Vector m_vecData = null; 
    /**
     * 列名称,序号， 名称，内容
     */
    private  final String[] COLNAMES = {/*"miufopublic359",*/"uiufochk002","miufopublic467"," "};
 
    /**
     * 公式处理器
     */
    private UfoFmlExecutor m_fmlExecutor;
    
    public ComplexCheckMngTbModel(Vector vecCheckFm, UfoFmlExecutor fmlExecutor){
        if( vecCheckFm != null){
            m_vecData = (Vector)vecCheckFm.clone();
        }else{
            m_vecData = new Vector();
        }
        sort();
        m_fmlExecutor = fmlExecutor;
    }
    public int getColumnCount() {
        return COLNAMES.length;
    }
    public String getColumnName(int nCol){
        if( nCol >=0 && nCol <=2){
            return StringResource.getStringResource(COLNAMES[nCol]);
        }
        return null;
    }
    public Class getColumnClass(int nCol){
//        if( nCol == 0 ){
//            return Integer.class;
//        }else{
//            return String.class;
//        }
    	return String.class;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return m_vecData.size();
    }
    

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int nRow, int nCol) 
    {
        if( nRow < m_vecData.size()){
            RepCheckVO repCheckVO = (RepCheckVO)m_vecData.get(nRow);
            if( repCheckVO != null ){
                switch( nCol ){
                case 0:
//                    return new Integer(nRow+1);
//                case 1:
                   return repCheckVO.getName();
                case 1:
                    //调用批命令进行语法检查
                	String strFormula = repCheckVO.getFormula();
    
                    try{
                    	strFormula=m_fmlExecutor.parseRepCheckFormula(strFormula,false);
                       
                    } catch(Exception e){
                    	e.printStackTrace(System.out);
                    } 
                    return strFormula;
                }
            }
        }
        return " ";
    }
    public boolean isCellEditable(int nRow, int nCol){
        return false;
    }
    
    /**
     * 升序排列
     */
    public void sort()
    {
        if( m_vecData != null){
            Collections.sort(m_vecData, new Comparator() {
                public int compare(Object obj1, Object obj2) {
                    String strName1 = ((RepCheckVO)obj1).getName();
                    String strName2 = ((RepCheckVO)obj2).getName();
                    return nc.util.iufo.pub.UFOString.compareHZString(strName1, strName2);
                  }
                });
            // 模型改变
            fireTableRowsUpdated(0, m_vecData.size()-1);
        }
    }
    /**
     * 返回审核公式列表
     * @return
     */
    public Vector getComplexCheckFms()
    {
        return m_vecData;
    }
    /**
     * 增加行
     * @param repCheckVO
     */
    public void addRow(RepCheckVO repCheckVO){
        m_vecData.add(repCheckVO);
        int nLastRow = m_vecData.size()-1;
        fireTableRowsInserted(nLastRow, nLastRow);
    }
    public void addRows(Vector vecRepChecks){
    	if(vecRepChecks==null || vecRepChecks.size()==0)
    		return;
    	int iLastRow=m_vecData.size();
    	m_vecData.addAll(vecRepChecks);
    	fireTableRowsInserted(iLastRow,m_vecData.size()-1);
    }
    /**
     * 修改指定行的内容
     * @param nRow
     * @param repCheckVO
     */
    public void updateRow( int nRow, RepCheckVO repCheckVO){
        if(nRow < m_vecData.size() ){
            m_vecData.setElementAt(repCheckVO, nRow);
            fireTableRowsUpdated(nRow, nRow);
        }
    }
    /**
     * 删除行
     * @param nSelected 需要删除的行，
     */
    public void deleteRows(int[] nSelected){
        if( nSelected != null){
            for( int i=nSelected.length-1; i>=0; i--){
                m_vecData.remove(nSelected[i]);
                fireTableRowsDeleted(nSelected[i], nSelected[i]);
            }
        }
    }
    public Vector getDatas(){
    	return m_vecData;
    }
    public void updateDatas(Vector vecCheckFm){
    	if(m_vecData.size()>0)
    		fireTableRowsDeleted(0,m_vecData.size()-1);
    	if( vecCheckFm != null){
            m_vecData = (Vector)vecCheckFm.clone();
        }else{
            m_vecData = new Vector();
        }
    	fireTableRowsInserted(0,m_vecData.size()-1);
    	sort();
    	
    }
}


