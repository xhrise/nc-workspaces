package com.ufida.report.adhoc.applet;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTable;

import nc.vo.bi.integration.dimension.DimMemberVO;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.MoveTableModel;;

public class CrossHeaderTableModel extends MoveTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int COLUMN_NAME = 0; //成员
    public static final int COLUMN_DISC = 1; //显示名称
        
    //排序所需记录的行号
    private int[] indexes;
    
    public CrossHeaderTableModel(){
    	super();
    }
    
	public CrossHeaderTableModel(String[] headNames) {
		this();
		setHead(headNames);
	}
	
    public ArrayList<DimMemberVO> getFixedField(){
    	ArrayList<DimMemberVO> flds = null;
    	Object vo=null;
		if (getRowCount() > 0) {
			flds = new ArrayList<DimMemberVO>();
			for (int i = 0; i < getRowCount(); i++) {
				vo=getObjectByRow(i);
				if(vo instanceof DimMemberVO){
					flds.add((DimMemberVO)vo);
				}
			}
		}
    	return flds;
    }
    @Override
    public void removeFromTable(int row,JTable sourceTable){
    	int rowIndex = row;
	    if (indexes != null) {
	      rowIndex = indexes[row];
	    }
	    super.removeFromTable(rowIndex, sourceTable);
    }
	@Override
	public Object getValueAt(int r, int c) {
		int rowIndex = r;
	    if (indexes != null) {
	      rowIndex = indexes[r];
	    }
		Object returnObj = null;
		Object item=getObjectByRow(rowIndex);
		 if(item==null){
			 return returnObj;
		 }
		if(item instanceof DimMemberVO){
			DimMemberVO vo=(DimMemberVO)item;
			 switch (c) {
	            case COLUMN_NAME :
	                returnObj = vo.getMemname();
	                break;
	            case COLUMN_DISC :
	                returnObj = vo.getMemcode();
	                break;
	        }
		}	 
		return returnObj;
	}

	@Override
	public boolean isCellEditable(int r, int c) {

	  return true;

	}

	@Override
	public void setValueAt(Object obj, int r, int c) {
		int rowIndex = r;
	    if (indexes != null) {
	      rowIndex = indexes[r];
	    }
		Object item=getObjectByRow(rowIndex);
		 if(item==null){
			 return ;
		 }
		 if(item instanceof DimMemberVO){
			 DimMemberVO vo=(DimMemberVO)item;
			 if(c==COLUMN_NAME){
				 vo.setMemname(obj.toString());
				 vo.setMemcode(obj.toString());
			 }
			 if(c==COLUMN_DISC){
				 vo.setMemcode(obj.toString());
			 }
		 }
	}
	 public void sortByColumn(int column, boolean isAscent) {
		sort(column, isAscent);
		fireTableDataChanged();
	}

	private int[] getIndexes() {
		int n = getRowCount();
		if (indexes != null) {
			if (indexes.length == n) {
				return indexes;
			}
		}
		indexes = new int[n];
		for (int i = 0; i < n; i++) {
			indexes[i] = i;
		}
		return indexes;
	}

	private void sort(int column, boolean isAscent) {
		int n = getRowCount();
		int[] indexes = getIndexes();

		for (int i = 0; i < n - 1; i++) {
			int k = i;
			for (int j = i + 1; j < n; j++) {
				if (isAscent) {
					if (compare(column, j, k) < 0) {
						k = j;
					}
				} else {
					if (compare(column, j, k) > 0) {
						k = j;
					}
				}
			}
			int tmp = indexes[i];
			indexes[i] = indexes[k];
			indexes[k] = tmp;
		}
	}

	private int compare(int column, int row1, int row2) {
		Object o1 = getValueAt(row1, column);
		Object o2 = getValueAt(row2, column);
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		} else {
			Class type = getColumnClass(column);
			if (type.getSuperclass() == Number.class) {
				return compare((Number) o1, (Number) o2);
			} else if (type == String.class) {
				return ((String) o1).compareTo((String) o2);
			} else if (type == Date.class) {
				return compare((Date) o1, (Date) o2);
			} else if (type == Boolean.class) {
				return compare((Boolean) o1, (Boolean) o2);
			} else {
				return ((String) o1).compareTo((String) o2);
			}
		}
	}

	private int compare(Number o1, Number o2) {
		double n1 = o1.doubleValue();
		double n2 = o2.doubleValue();
		if (n1 < n2) {
			return -1;
		} else if (n1 > n2) {
			return 1;
		} else {
			return 0;
		}
	}

	private int compare(Date o1, Date o2) {
		long n1 = o1.getTime();
		long n2 = o2.getTime();
		if (n1 < n2) {
			return -1;
		} else if (n1 > n2) {
			return 1;
		} else {
			return 0;
		}
	}

	private int compare(Boolean o1, Boolean o2) {
		boolean b1 = o1.booleanValue();
		boolean b2 = o2.booleanValue();
		if (b1 == b2) {
			return 0;
		} else if (b1) {
			return 1;
		} else {
			return -1;
		}
	}

}
