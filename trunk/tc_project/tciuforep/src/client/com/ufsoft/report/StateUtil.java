package com.ufsoft.report;

import java.awt.Component;

import javax.swing.JPanel;

import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.SelectAllCmp;
import com.ufsoft.table.header.TableHeader;

/**
 * 
 * @author zzl 2005-6-14
 */
public class StateUtil {
    /**
     * ��CellsPane��
     * @param report
     * @param focusComp
     * @return boolean
     */
    public static boolean isCellsPane(UfoReport report,Component focusComp){
        return focusComp instanceof CellsPane;
    }
    /**
     * �Ƿ��ڹ�������.
     * @param focusComp
     * @return
     */
    public static boolean isToolBar(Component focusComp){
    	if(focusComp instanceof ReportToolBar){
    		return true;
    	}
    	if(focusComp instanceof JPanel){
    		JPanel panel = (JPanel) focusComp;
    		if(panel.getComponents().length > 0 && 
    				panel.getComponent(0) instanceof ReportToolBar){
    			return true;
    		}
    	}
    	return false;
    	
    }
    /**
     * �Ƿ���״̬����.
     * @param focusComp
     * @return
     */
    public static boolean isStatusBar(Component focusComp){
    	return focusComp instanceof ReportStatusBar || 
    			((focusComp.getParent() != null && focusComp.getParent() instanceof ReportStatusBar));
    }
    /**
     * ��ʽ״̬��
     * @param report
     * @param focusComp
     * @return boolean
     */
    public static boolean isFormatState(UfoReport report,Component focusComp){
        return report.getOperationState() == UfoReport.OPERATION_FORMAT;
    }
    /**
     * ��ʽ״̬�£���CellsPane�ϡ�
     * @param report
     * @param focusComp
     * @return boolean
     */
    public static boolean isFormat_CPane(UfoReport report,Component focusComp){
        return isCellsPane(report,focusComp)&&
                isFormatState(report,focusComp);
    }
    /**
     * ��CellsPane�ϻ�����TableHeader��
     * @param report
     * @param focusComp
     * @return boolean
     */
    public static boolean isCPane1THeader(UfoReport report,Component focusComp){
        return focusComp instanceof CellsPane ||
        		focusComp instanceof TableHeader || 
        		focusComp instanceof SelectAllCmp;
    }
    /**
     * ��ʽ״̬�£���CellsPane�ϻ�����TableHeader�ϡ�
     * @param report
     * @param focusComp
     * @return boolean
     */
    public static boolean isFormat_CPane1THeader(UfoReport report,Component focusComp){
        return isFormatState(report,focusComp) &&
                isCPane1THeader(report,focusComp);
    }
    public static boolean isAreaSel(UfoReport report,Component focusComp){
    	if(report.getCellsModel() == null){
    		return false;
    	}
        return report.getCellsModel().getSelectModel().getSelectedCells().length > 0;
    }
    
    public static boolean isCellSel(UfoReport report,Component focusComp){
        return report.getCellsModel().getSelectModel().getSelectedCells().length==1;
    }
    
    public static boolean isFormat_AreaSel(UfoReport report,Component focusComp){
        return isFormatState(report,focusComp) &&
                isAreaSel(report,focusComp);
    }
    /**
     * ���㵥Ԫ�Ƿ���Ա༭
     * @param cellsModel
     * @return boolean
     */
    public static boolean isAnchorEditable(CellsModel cellsModel){
    	if(cellsModel == null){
    		return false;
    	}
    	if(cellsModel.getCellsAuth() != null){
    		CellPosition cellPos = cellsModel.getSelectModel().getAnchorCell();
    		return cellsModel.getCellsAuth().isWritable(cellPos.getRow(),cellPos.getColumn());
    	}else{
    		return true;
    	} 
    }
}
