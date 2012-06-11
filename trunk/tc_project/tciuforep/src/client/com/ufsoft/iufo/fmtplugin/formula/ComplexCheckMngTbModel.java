package com.ufsoft.iufo.fmtplugin.formula;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.ufsoft.iufo.resource.StringResource;


/**
 * @author zyjun
 *
 * ��˹�ʽ���ģ��
 */
public class ComplexCheckMngTbModel extends AbstractTableModel {

    /**
     * ��˹�ʽ�б��б��еĶ���ΪRepCheckVO
     */
    private Vector m_vecData = null; 
    /**
     * ������,��ţ� ���ƣ�����
     */
    private  final String[] COLNAMES = {/*"miufopublic359",*/"uiufochk002","miufopublic467"," "};
 
    /**
     * ��ʽ������
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
                    //��������������﷨���
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
     * ��������
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
            // ģ�͸ı�
            fireTableRowsUpdated(0, m_vecData.size()-1);
        }
    }
    /**
     * ������˹�ʽ�б�
     * @return
     */
    public Vector getComplexCheckFms()
    {
        return m_vecData;
    }
    /**
     * ������
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
     * �޸�ָ���е�����
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
     * ɾ����
     * @param nSelected ��Ҫɾ�����У�
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


