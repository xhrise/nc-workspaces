/*
 * �������� 2006-4-24
 */
package com.ufsoft.iufo.fmtplugin.formula;
import com.ufida.iufo.pub.tools.AppDebug;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.exception.ParseException;

/**
 * @author ljhua
 */
public class SimpleCheckMngTbModel extends AbstractTableModel {

    /**
     * ��˹�ʽ�б��б��еĶ���ΪRepCheckVO
     */
    private Vector m_vecData = null; 
    /**
     * ������,���ƣ� ��ʽ���ݣ�������Ϣ
     * TODO ������Ϣ
     * @i18n uiiufofmt00026=������Ϣ
     */
    private  final String[] COLNAMES = {"uiufochk002","miufopublic467",StringResource.getStringResource("uiiufofmt00026")};//TODO 
 
    /**
     * ��ʽ������
     */
    private UfoFmlExecutor m_fmlExecutor;
    
    public SimpleCheckMngTbModel(Vector vecCheckFm, UfoFmlExecutor fmlExecutor){
        if( vecCheckFm != null){
            m_vecData = (Vector)vecCheckFm.clone();
        }else{
            m_vecData = new Vector();
        }
        sort();
        m_fmlExecutor = fmlExecutor;
    }
    public SimpleCheckFmlVO getSimpleCheckVOByRow(int iRow){
    	return iRow>=m_vecData.size()?null:(SimpleCheckFmlVO) m_vecData.get(iRow);
    }
	/* ���� Javadoc��
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return m_vecData==null?1:m_vecData.size()+1;
	}
	public int getActualRowCount(){
		return getRowCount()-1;
	}

	/* ���� Javadoc��
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return COLNAMES.length;
	}

	/* ���� Javadoc��
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
	       if( rowIndex < m_vecData.size()){
	       	SimpleCheckFmlVO repCheckVO = (SimpleCheckFmlVO)m_vecData.get(rowIndex);
            if( repCheckVO != null ){
                switch( columnIndex ){
	                case 0:
	                   return repCheckVO.getFmlName();
	                case 1:
	                	String strFormula=null;
	                    if(repCheckVO.getParsedExpr()!=null){
	                    	strFormula=m_fmlExecutor.getStrFmlByLet(repCheckVO.getParsedExpr(),true);
	                    }else{
							try {
								IParsed parsed = m_fmlExecutor.parseLogicExpr(repCheckVO.getCheckCond(),false);
								repCheckVO.setParsedExpr(parsed);
		                    	strFormula=m_fmlExecutor.getStrFmlByLet(parsed,true);
							} catch (ParseException e) {
								AppDebug.debug(e);
								strFormula= repCheckVO.getCheckCond();
							}
	                    }
	                    return strFormula;
	                 case 2:
	                 	return repCheckVO.getErrMsg();
	                    
                }
            }
        }
        return null;
	}
	 /**
     * ������
     * @param repCheckVO
     */
    public void addRow(SimpleCheckFmlVO repCheckVO){
        m_vecData.add(repCheckVO);
        int nLastRow = m_vecData.size()-1;
        fireTableRowsInserted(nLastRow, nLastRow);
    }
    public void updateRow(SimpleCheckFmlVO repCheckVO, int iRow){
    	if(repCheckVO==null || iRow>=m_vecData.size())
    		return;
    	SimpleCheckFmlVO oldCheckVO=(SimpleCheckFmlVO)m_vecData.get(iRow);
    	if (oldCheckVO!=null)
    		repCheckVO.setID(oldCheckVO.getID());
    	
    	m_vecData.set(iRow,repCheckVO);
    	fireTableRowsUpdated(iRow,iRow);
    	
    }
    public void addRows(Vector repCheckVOs){
    	if(repCheckVOs!=null && repCheckVOs.size()>0){
	    	int nLastRow=m_vecData.size();
	    	m_vecData.addAll(repCheckVOs);
	    	fireTableRowsInserted(nLastRow, m_vecData.size()-1);
    	}
    }
    /**
     * ɾ����
     * @param nSelected ��Ҫɾ�����У�
     */
    public void deleteRows(int[] nSelected){
        if( nSelected != null){
        	int iLastEmptyRow=m_vecData.size();
            for( int i=nSelected.length-1; i>=0; i--){
            	if(nSelected[i]==iLastEmptyRow)
            		continue;
                m_vecData.remove(nSelected[i]);
                fireTableRowsDeleted(nSelected[i], nSelected[i]);
            }
        }
    }
    public Class getColumnClass(int nCol){
    	return String.class;
    }
    public String getColumnName(int nCol){
        if( nCol >=0 && nCol <=2){
            return StringResource.getStringResource(COLNAMES[nCol]);
        }
        return null;
    }
    /**
     * ������˹�ʽ�б�
     * @return
     */
    public Vector getSimpleCheckFms()
    {
        return m_vecData;
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
                	if(obj1!=null && obj2!=null){
	                    String strName1 = ((SimpleCheckFmlVO)obj1).getFmlName();
	                    String strName2 = ((SimpleCheckFmlVO)obj2).getFmlName();
	                    return nc.util.iufo.pub.UFOString.compareHZString(strName1, strName2);
                	}
                	else if (obj1==null){
                		return -1;
                	}else
                		return 1;
                  }
                });
            // ģ�͸ı�
            fireTableRowsUpdated(0, m_vecData.size()-1);
        }
    }
    

}
 