package com.ufsoft.report.sysplugin.cellattr;


import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.undo.CellUndo;
import com.ufsoft.report.util.DeepCopyUtil;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
/**
 * ���õ�Ԫ����
 * 
 * @author caijie
 * @since 3.1
 */
public class SetCellAttrCmd extends UfoCommand {

    //  ������
    private UfoReport m_RepTool = null;
    //ģ��
    private CellsModel m_cellsModel;
    
    /**
     * @param rep
     *            UfoReport - ������
     */
    public SetCellAttrCmd(UfoReport rep) {
        super();
        if (rep == null) {
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000824"));//�Ƿ��ı����߲��� 
        }
        this.m_RepTool = rep;
        m_cellsModel = m_RepTool.getCellsModel();
    }

    /**
     * ����ȫ���С��к������Ԫ������ֵ
     * params[1]:���÷�Χ t/c/r/a  t��ʾȫ��table c��ʾ��column r��ʾ��row a��ʾ����area
     * params[0]:���ö���  ����params[0] = 't' ��ֵ��ʾ�������Ե�Hashtable
     *                    ����params[0] = 'c' ��ֵ��ʾ����������
     * 					  ����params[0] = 'r' ��ֵ��ʾ����������
     * 					  ����params[0] = 'a' ��ֵ��ʾ������������
     * params[2]:����HashTable 
     * ˵�� ��������ȫ��ʱ��ֻ��Ҫ����������
     */
    public void execute(Object[] params) {
        if ((params == null ) || (params.length == 0))
            return;     
        if(getCellsModel() == null)
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000825"), getReport(), null);//ģ�Ͳ���Ϊ��
        
        int paramIndex = 1;
      
        try {    
        	//���Ч�ʣ���ֹ�����¼� liuyy.
    		getCellsModel().setEnableEvent(false);
    		
            Hashtable<Integer,Integer> propertyCache = null;//(Hashtable)params[0];//���Ի���
            propertyCache =  (Hashtable<Integer,Integer>) params[0];
            if((propertyCache == null) || propertyCache.isEmpty()){
            	return;
            }
            while(params[paramIndex] != null){
                char scope = ((Character)params[paramIndex++]).charValue();
                switch (scope) {
                case 't': //ȫ��  
                    setTableFormat(propertyCache);
                    break;
                case 'r'://��
                    int[] rows = (int[])params[paramIndex++];
                    if((rows == null) || (rows.length == 0)) return;  
                    
                    setRowsFormat(rows, propertyCache);
                    
                    
                    break;
                case 'c'://��
                    int[] cols = (int[])params[paramIndex++];
                    if((cols == null) || (cols.length == 0)) return;                   
                    setColumnsFormat(cols, propertyCache);
                    break;
                case 'a'://����
                    CellPosition[] cells = (CellPosition[])params[paramIndex++];
                    if((cells == null) || (cells.length == 0)) return;                  
                    setCellsFormat(cells, propertyCache);
                    break;
                default:
                    UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000826"), getReport(), null);//�������ԵĲ�������
                }                      
            }
            
        } catch (Exception e) {   
            AppDebug.debug(e);
            
        } finally {
        	getCellsModel().setEnableEvent(true);
//        	//ǿ��ˢ����ѡ����
//        	getCellsModel().getSelectModel().fireSelectedChanged(getCellsModel().getSelectModel().getSelectedAreas());
        	
        }
        
        
        
    }

//    private void setProperty(Hashtable propertyCache){
//        // ��cacheSetProperty�е�ֵ���õ�ģ���С�
//        Enumeration enum = propertyCache.keys();
//        while (enum.hasMoreElements()) {
//            Integer tmpType = (Integer) enum.nextElement();
//            int nType = tmpType.intValue();
//            int nValue = ((Integer) propertyCache.get(tmpType))
//                    .intValue();
//            setModel(nType, nValue);
//        }
//    }
//    /**
//     * �������ԡ������ڱ߿�����ʱ������ѡ�����������ж������õ�����
//     * @param nType  int ���� �ο�PropertyType
//     * @param nValue int ֵ
//     */
//    private void setProperty(int nType, int nValue) {//Ϊ�˲��ԣ���Ϊpublic������
//        switch (m_AreaType) {
//        case 0: //�������� 0 һ������
//            
//            return;
//        case 1: //��������1 ��������
//            int[] tmpRowHeaders = getAimRows(nType);
//            switch (nType) {
//            case PropertyType.HLType:
//                nType = PropertyType.BLType;
//                break;
//            case PropertyType.HLColor:
//                nType = PropertyType.BLColor;
//                break;
//            }
//            int rowIndex = 0;
//            while (rowIndex < tmpRowHeaders.length) {
//                getUfoTableModel().setRowProperty(tmpRowHeaders[rowIndex], nType,
//                        nValue);
//                rowIndex++;
//            }
//            break;
//        case 2: //��������2 ��������
//            int[] tmpColumnHeaders = getAimColumns(nType);
//            switch (nType) {
//            case PropertyType.VLType:
//                nType = PropertyType.RLType;
//                break;
//            case PropertyType.VLColor:
//                nType = PropertyType.RLColor;
//                break;
//            }
//            int colIndex = 0;
//            while (colIndex < tmpColumnHeaders.length) {
//                getUfoTableModel().setColProperty(tmpColumnHeaders[colIndex], nType,
//                        nValue);
//                colIndex++;
//            }
//            break;
//        case 3: //��������3 ��������
//            getUfoTableModel().setTableProperty(nType, nValue);
//            break;
//        default:
//            break;
//        }
//
//    }
    /**
     * �����������ԡ������ڱ߿�����ʱ������ѡ�����������ж������õ�����
     */
    private void setTableFormat(Hashtable<Integer,Integer> propertyCache){
        Enumeration<Integer> enums = propertyCache.keys();
        while (enums.hasMoreElements()) {
            Integer tmpType = (Integer) enums.nextElement();
            int nType = tmpType.intValue();
            int nValue = ((Integer) propertyCache.get(tmpType)).intValue();
            getCellsModel().setTableProperty(nType, nValue);
        }
    }
    /**
     * ���������ԡ������ڱ߿�����ʱ������ѡ�����������ж������õ�����
     * @param nType  int ���� �ο�PropertyType
     * @param nValue int ֵ
     */
    public void setColumnsFormat(int[] targetCols,Hashtable<Integer,Integer> propertyCache){
        Enumeration<Integer> enums = propertyCache.keys();
        while (enums.hasMoreElements()) {
            Integer tmpType = (Integer) enums.nextElement();
            int nType = tmpType.intValue();
            int nValue = ((Integer) propertyCache.get(tmpType)).intValue();

            int[] actionCols = null;
            switch (nType) {
            case PropertyType.TLType:
            case PropertyType.TLColor: 
                setColsProperty(targetCols, nType, nValue);
            	break;
            case PropertyType.BLType:
            case PropertyType.BLColor:
                setColsProperty(targetCols, nType, nValue);
                break;
            case PropertyType.LLType:               
            case PropertyType.LLColor:  
                actionCols = getAimCols(targetCols, Format.LEFTLINE);
	            if (actionCols != null) {
	                setColsProperty(actionCols, nType, nValue);//���������ڵ�Ԫ
	                clearCircumjacentColBorder(actionCols, nType);//������иñ߿�����ⵥԪ�Ĵ������ԣ������������
	            }
                break;
            case PropertyType.RLType:              
            case PropertyType.RLColor:  
                actionCols = getAimCols(targetCols, Format.RIGHTLINE);
                if (actionCols != null) {
                    setColsProperty(actionCols, nType, nValue);//���������ڵ�Ԫ
                    clearCircumjacentColBorder(actionCols, nType);//������иñ߿�����ⵥԪ�Ĵ������ԣ������������
                }
                break;    
            case PropertyType.HLType:              
            case PropertyType.HLColor:
                setColsProperty(targetCols, nType, nValue);
                break;
            case PropertyType.VLType:
                actionCols = getAimCols(targetCols, SetCellAttrExt.MIDDLE_LEFT);//��ֱ�߿���Ҫͬʱ���õ�Ԫ�߿�Ǳ�Ե�߿����߿�
                if (actionCols != null) {
                    setColsProperty(actionCols, PropertyType.LLType, nValue);
                }

                actionCols = getAimCols(targetCols, SetCellAttrExt.MIDDLE_RIGHT);//ˮƽ�߿���Ҫͬʱ���õ�Ԫ�߿�Ǳ�Ե�߿���ұ߿�
                if (actionCols != null) {
                    setColsProperty(actionCols, PropertyType.RLType, nValue);
                }
                break;
            case PropertyType.VLColor:                
                actionCols = getAimCols(targetCols, SetCellAttrExt.MIDDLE_LEFT);//ˮƽ�߿���Ҫͬʱ���õ�Ԫ�߿�Ǳ�Ե�߿����߿�
                if (actionCols != null) {
                    setColsProperty(actionCols, PropertyType.LLColor, nValue);
                }
                actionCols = getAimCols(targetCols, SetCellAttrExt.MIDDLE_RIGHT);//ˮƽ�߿���Ҫͬʱ���õ�Ԫ�߿�Ǳ�Ե�߿���ұ߿�
                if (actionCols != null) {
                    setColsProperty(actionCols, PropertyType.RLColor, nValue);
                }
                break;   
            default: //�Ǳ߿�����
                    setColsProperty(targetCols, nType, nValue);           
                break;
            }

        }

    }
    /**
     * ���������ԡ������ڱ߿�����ʱ������ѡ�����������ж������õ�����
     * @param nType  int ���� �ο�PropertyType
     * @param nValue int ֵ
     */
    public void setRowsFormat(int[] targetRows, Hashtable<Integer,Integer> propertyCache) {
        Enumeration<Integer> enums = propertyCache.keys();
        while (enums.hasMoreElements()) {
            Integer tmpType = (Integer) enums.nextElement();
            int nType = tmpType.intValue();
            int nValue = ((Integer) propertyCache.get(tmpType)).intValue();

            int[] actionRows = null;
            switch (nType) {
            case PropertyType.HLType:
                actionRows = getAimRows(targetRows, SetCellAttrExt.MIDDLE_BOTTOM);//ˮƽ�߿���Ҫͬʱ���õ�Ԫ�߿�Ǳ�Ե�߿���±߿�
                if (actionRows != null) {
                    setRowsProperty(actionRows, PropertyType.BLType, nValue);
                }

                actionRows = getAimRows(targetRows, SetCellAttrExt.MIDDLE_TOP);//ˮƽ�߿���Ҫͬʱ���õ�Ԫ�߿�Ǳ�Ե�߿���ϱ߿�
                if (actionRows != null) {
                    setRowsProperty(actionRows, PropertyType.TLType, nValue);
                }
                break;
            case PropertyType.HLColor:                
                actionRows = getAimRows(targetRows, SetCellAttrExt.MIDDLE_BOTTOM);//ˮƽ�߿���Ҫͬʱ���õ�Ԫ�߿�Ǳ�Ե�߿���±߿�
                if (actionRows != null) {
                    setRowsProperty(actionRows, PropertyType.BLColor, nValue);
                }
                actionRows = getAimRows(targetRows, SetCellAttrExt.MIDDLE_TOP);//ˮƽ�߿���Ҫͬʱ���õ�Ԫ�߿�Ǳ�Ե�߿���ϱ߿�
                if (actionRows != null) {
                    setRowsProperty(actionRows, PropertyType.TLColor, nValue);
                }
                break;
            case PropertyType.TLType:               
            case PropertyType.TLColor:                
            case PropertyType.BLType:              
            case PropertyType.BLColor:  
                actionRows = getAimRows(targetRows, nType);
                if (actionRows != null) {
                    setRowsProperty(actionRows, nType, nValue);//���������ڵ�Ԫ
                    clearCircumjacentRowBorder(actionRows, nType);//������иñ߿�����ⵥԪ�Ĵ������ԣ������������
                }
            break;
            default:
                //���ڷ���getAimRows()���ص�actionRows���ص�����Ҫ��������,
                //��ֱ�����þͿ���,����Ҫ���ǵ�Ԫ��֮��Ĺ�ϵ
                actionRows = getAimRows(targetRows, nType);
                if (actionRows != null) {
                    setRowsProperty(actionRows, nType, nValue);
                }
                break;
            }

        }

    }
    /**
     * ���õ�Ԫ����
     * ԭ�򣺶��ڴ��ڱ�Ե�ı߿����ԣ��������������������ڵ�Ԫ�б߿����Եģ������ͻ�ı߿�����ȥ��
     * ����������������ı߿����ԣ���ÿ�߿򶼱�����Ԫ���У�������Ԫ����������  
     * @param poses ��Ҫ���õĵ�Ԫ����
     */
    public void setCellsFormat(CellPosition[] poses, Hashtable<Integer, Integer> propertyCache){ 
    	if(poses == null || poses.length < 1){
    		return;
    	}
    	
    	Cell[] oldCells = null;
    	CellUndo edit = null;
    	//����1000������undoredo����
    	if(poses.length <= 1000){
    		oldCells = getCellsModel().getCells(poses);
    		Object[] objs = DeepCopyUtil.getDeepCopy(oldCells);
    		for (int i = 0; i < objs.length; i++) {
    			oldCells[i] = (Cell) objs[i];
    		} 

    		edit = new CellUndo();
    	}
    	
    	setCellsFormat0(poses, propertyCache);
    	 
    	if(edit != null){
	    	Cell[] newCells = getCellsModel().getCells(poses);
	    	Object[] objs = DeepCopyUtil.getDeepCopy(newCells);
	    	for (int i = 0; i < objs.length; i++) {
	    		newCells[i] = (Cell) objs[i];
			} 
	        
	    	for (int i = 0; i < newCells.length; i++) {
				edit.addCell(poses[i], oldCells[i], newCells[i]);
			}
	
			getCellsModel().fireUndoHappened(edit);
    	}   
    }

	private void setCellsFormat0(CellPosition[] poses,
			Hashtable<Integer, Integer> propertyCache) {
		Enumeration<Integer> enums = propertyCache.keys();
        CellsModel m_cellsModel = getCellsModel();
        while (enums.hasMoreElements()) {
            Integer tmpType = (Integer) enums.nextElement();
            int nType = tmpType.intValue();
            int nValue = ((Integer) propertyCache.get(tmpType)).intValue();  
                
            CellPosition[] tmpCells = null;   
            switch (nType) {     
	            case PropertyType.HLType:                 
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.HLType);//ˮƽ�߿���Ҫͬʱ���÷Ǳ�Ե��Ԫ�߿����������
	               
                    setCellsProperty(tmpCells, PropertyType.TLType, nValue);
                    setCellsProperty(tmpCells, PropertyType.BLType, nValue);
	               	
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.TLType);//ˮƽ�߿���Ҫͬʱ�����������Ե��Ԫ�߿���±߿�
	                setCellsProperty(tmpCells, PropertyType.BLType, nValue);
//	                if (tmpCells != null) {
//	                    setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.BLType), PropertyType.BLType, nValue);
//	                }
	
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.BLType);//ˮƽ�߿���Ҫͬʱ�����������Ե��Ԫ�߿���ϱ߿�
	                setCellsProperty(tmpCells, PropertyType.TLType, nValue);
//	                if (tmpCells != null) {
//	                    setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.TLType), PropertyType.TLType, nValue);                                      
//	                }
	                break;
	            case PropertyType.HLColor:
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.HLColor);//ˮƽ�߿���Ҫͬʱ���÷Ǳ�Ե��Ԫ�߿����������
	                
                    setCellsProperty(tmpCells, PropertyType.TLColor, nValue);
                    setCellsProperty(tmpCells, PropertyType.BLColor, nValue);
	                
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.TLColor);//ˮƽ�߿���Ҫͬʱ�����������Ե��Ԫ�߿���±߿�
	                setCellsProperty(tmpCells, PropertyType.BLColor, nValue);
	 
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.BLColor);//ˮƽ�߿���Ҫͬʱ�����������Ե��Ԫ�߿���ϱ߿�
	                setCellsProperty(tmpCells, PropertyType.TLColor, nValue);
 
	                break;
	            case PropertyType.VLType:
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.VLType);//��ֱ�߿���Ҫͬʱ���÷Ǳ�Ե��Ԫ�߿����������
	                
                    setCellsProperty(tmpCells, PropertyType.RLType, nValue);
                    setCellsProperty(tmpCells, PropertyType.LLType, nValue);
	                
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.LLType);//��ֱ�߿���Ҫͬʱ�����������Ե��Ԫ�߿���ұ߿�
	                setCellsProperty(tmpCells, PropertyType.RLType, nValue);
//	                if (tmpCells != null) {                   
//	                     setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.RLType), PropertyType.RLType, nValue);                                    
//	                }
	                
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.RLType);//��ֱ�߿���Ҫͬʱ�����������Ե��Ԫ�߿����߿�
	                setCellsProperty(tmpCells, PropertyType.LLType, nValue);
//	                if (tmpCells != null) {                   
//	                      setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.LLType), PropertyType.LLType, nValue);                        
//	                }
	                break;
	            case PropertyType.VLColor:
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.VLColor);//��ֱ�߿���Ҫͬʱ���÷Ǳ�Ե��Ԫ�߿����������	                
                    setCellsProperty(tmpCells, PropertyType.RLColor, nValue);
                    setCellsProperty(tmpCells, PropertyType.LLColor, nValue);
	                
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.LLColor);//��ֱ�߿���Ҫͬʱ�����������Ե��Ԫ�߿���ұ߿�
	                setCellsProperty(tmpCells, PropertyType.RLColor, nValue);                 
//	                     setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.RLColor), PropertyType.RLColor, nValue);                                       
	              
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.RLColor);//��ֱ�߿���Ҫͬʱ�����������Ե��Ԫ�߿����߿�
	                setCellsProperty(tmpCells, PropertyType.LLColor, nValue);
//	                    setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.LLColor), PropertyType.LLColor, nValue);
	               
	                break;    
	            case PropertyType.TLType:               
	            case PropertyType.TLColor:      
	            case PropertyType.BLType:              
	            case PropertyType.BLColor:                
	            case PropertyType.LLType:                
	            case PropertyType.LLColor:               
	            case PropertyType.RLType:                
	            case PropertyType.RLColor:

	                tmpCells = m_cellsModel.filterCells(poses, nType);	 
	                setCellsProperty(tmpCells, nType, nValue);//���������ڵ�Ԫ
	                if(nValue == TableConstant.UNDEFINED){
	                	clearCircumjacentCellsBorder(tmpCells, nType, nValue);//������иñ߿�����ⵥԪ�Ĵ������ԣ������������
	                }
                     
	                break;	                
	            default: //�Խ����ߴ˷�֧.
	                //���ڷ���m_cellsModel.filterCells()���ص�tmpCells���ص�����Ҫ�����ĵ�Ԫ��,
	                //��ֱ�����þͿ���,����Ҫ���ǵ�Ԫ��֮��Ĺ�ϵ
	                tmpCells =  m_cellsModel.filterCells(poses, nType);	            	
	                setCellsProperty(tmpCells, nType, nValue);
	                
	            break;
            }
           
        }
	}
    
    /**
     * ��cells���ҳ�����nType�߿�ĵ�Ԫ
    */
    private CellPosition[] getMiddleBorderCells(CellPosition[] checkCells, CellPosition[] allCells ,int nType){
        if(checkCells == null || checkCells.length == 0)
            return null;
        if(allCells == null || allCells.length == 0)
            return null;
        ArrayList<CellPosition> tmpList = new ArrayList<CellPosition>();
        switch(nType){
        case PropertyType.TLType:               
        case PropertyType.TLColor: 
            for(int i = 0; i < checkCells.length; i++){
                if(checkCells[i].getRow() == 0)
                    continue;
                if(isInSelCellPos(allCells, CellPosition.getInstance(checkCells[i].getRow() - 1,checkCells[i].getColumn())))
                    tmpList.add(checkCells[i]);
            }
            break;
        case PropertyType.BLType:              
        case PropertyType.BLColor:  
            for(int i = 0; i < checkCells.length; i++){
                if(checkCells[i].getRow() == getCellsModel().getRowNum()-1)
                    continue;
                if(isInSelCellPos(allCells, CellPosition.getInstance(checkCells[i].getRow() + 1,checkCells[i].getColumn())))
                    tmpList.add(checkCells[i]);
            }
            break;
        case PropertyType.LLType:                
        case PropertyType.LLColor:
            for(int i = 0; i < checkCells.length; i++){
	            if(checkCells[i].getColumn() == 0)
	                continue;
	            if(isInSelCellPos(allCells, CellPosition.getInstance(checkCells[i].getRow(),checkCells[i].getColumn() - 1)))
	                tmpList.add(checkCells[i]);
	        }    
            break;
        case PropertyType.RLType:                
        case PropertyType.RLColor:          
	        for(int i = 0; i < checkCells.length; i++){
	            if(checkCells[i].getColumn() == getCellsModel().getColNum()-1)
	                continue;
	            if(isInSelCellPos(allCells, CellPosition.getInstance(checkCells[i].getRow(),checkCells[i].getColumn() + 1)))
	                tmpList.add(checkCells[i]);
	        }        
            break;     
        default:
            break;
        }
        if(tmpList.isEmpty()){//Ҫȥ���±ߵ�Ԫ���������еĵ�Ԫ            
            return null;
        } else{
           return (CellPosition[])tmpList.toArray(new CellPosition[0]);
        }
       
    }
    /**
     * ���õ�Ԫ����
     * @param arr ��Ԫλ������
     * @param nType ��������
     * @param nValue ����ֵ
     */
    protected void setCellsProperty(CellPosition[] arr, int nType, int nValue) {
		if (arr == null || arr.length < 1)
			return;

		for (CellPosition pos : arr) {
			getCellsModel().setCellProperty(pos, nType, nValue);
		}
		 

	}
    /**
     * ���ñ���������
     * @param targetRows ������
     * @param nType ��������
     * @param nValue ����ֵ
     */
    private void setRowsProperty(int[] targetRows ,int nType, int nValue){
        if(targetRows == null) return;
        int cellIndex = 0;    	
        while (cellIndex < targetRows.length) {            
        	getCellsModel().setRowProperty(targetRows[cellIndex], nType, nValue);            
            cellIndex++;
        }
 
        //add by wangyga  2008-9-11�����������ø�ʽʱ�Ļ�������
        m_RepTool.getTable().getCells().repaint(getCellsModel().getSelectModel().getSelectedArea(),true);
    }
    /**
     * ����������
     * @param targetCols ����������
     * @param nType ��������
     * @param nValue ����ֵ
     */
    private void setColsProperty(int[] targetCols ,int nType, int nValue){
        if(targetCols == null) return;
        int cellIndex = 0;    	
        while (cellIndex < targetCols.length) {            
        	getCellsModel().setColProperty(targetCols[cellIndex], nType, nValue);            
            cellIndex++;
        }
        //add by wangyga  2008-9-11�����������ø�ʽʱ�Ļ�������
        m_RepTool.getTable().getCells().repaint(getCellsModel().getSelectModel().getSelectedArea(),true);
    }
    
    /**
     * ȡ����Ԫ����
     * @param row Ҫȡ���ĵ�Ԫ����
     * @param col Ҫȡ���ĵ�Ԫ����
     * @param type Ҫȡ������������
     */
    private void clearCellBorder(int row, int col, int type) {
		if (row < 0 || row >= getCellsModel().getRowNum() || col < 0
				|| col >= getCellsModel().getColNum()) {
			return;
		}
		CellPosition tmpPos = CellPosition.getInstance(row, col);
		IArea tmpArea = getCellsModel().getArea(tmpPos);
		if (tmpArea == null) {
			return;
		}
		Format tmpFormat = getCellsModel().getFormat(tmpArea.getStart());
		if (tmpFormat != null) {
			PropertyType.setPropertyByType(tmpFormat, type,
					TableConstant.UNDEFINED);
		}
	}
    
        /**
		 * ���ѡ��Ԫ��cells�����ڵ�Ԫ�߿�ֵ
		 * 
		 * @param cells
		 * @param nType
		 */
    private void clearCircumjacentCellsBorder(CellPosition[] sourceCells, int nType, int nValue){        
        if(sourceCells == null || sourceCells.length == 0){
        	return;
        }
        for(int i = 0; i < sourceCells.length; i++) {
        	CellPosition pos = sourceCells[i];
        	 IArea area =  getCellsModel().getArea(pos);
        	 if(area == null){
        		 continue;
        	 }
             int row = area.getStart().getRow();           
             int col = area.getStart().getColumn();
             int row2 = area.getEnd().getRow(); 
             int col2 = area.getEnd().getColumn();
             
             int type = nType;
             switch(nType){
	 			case PropertyType.TLColor://�Ϸ���Ԫ��	
	 				type = PropertyType.BLColor;    	  
	 			    break;	 				
	 			case PropertyType.TLType:
	 				type = PropertyType.BLType;	  
	 			    break;
	 			    
	 			case PropertyType.BLColor://�·���Ԫ 
	 				type = PropertyType.TLColor;	 				 
	 			    break;
	 			case PropertyType.BLType: 
	 				type = PropertyType.TLType;	 				 
	 			    break;	  
	 			case PropertyType.LLColor://��ߵ�Ԫ 
	 				type = PropertyType.RLColor;	 				 
	 			    break;	 
	 			case PropertyType.LLType: 
	 				type = PropertyType.RLType;	 				 
	 			    break;
	 			case PropertyType.RLColor://�ұߵ�Ԫ 
	 				type = PropertyType.LLColor;	 				 
	 			    break;	 
	 			case PropertyType.RLType: 
	 				type = PropertyType.LLType;	 				 
	 			    break;	 
	 			default:
	 				continue;	 			    
		    }
         
            if(nType == PropertyType.TLColor || nType == PropertyType.TLType){
            	row -= 1;
            	row2 = row;
            } else if(nType == PropertyType.BLColor || nType == PropertyType.BLType){
            	row2 += 1;
            	row = row2;                	
            } else if(nType == PropertyType.LLColor || nType == PropertyType.LLType){
            	col -= 1;
            	col2 = col;               	
            } else if(nType == PropertyType.RLColor || nType == PropertyType.RLType){
            	col2 += 1;
            	col = col2;
             }
            
            if(row == row2){
				for(int j = col; j <= col2; j++){
 					clearCellBorder(row, j, type);	             
 				}	 				 
            } else if(col == col2){
				for(int j = row; j <= row2; j++){
 					clearCellBorder(j, col, type);	             
 				}              	
            }
             
		}
	
    }
    /**
     * ȡ��ѡ������������±߿���������
     * @param sourceRows ������
     * @param nType �߿���������
     */
    private void clearCircumjacentRowBorder(int[] sourceRows, int nType){        
//        Format tmpFormat;       
        int row;
        if(sourceRows == null || sourceRows.length == 0) return;
        for(int i = 0; i < sourceRows.length; i++) {
            row = sourceRows[i];
             switch(nType){
 			case PropertyType.TLColor://��һ�У�	
 			    if(row != 0){ 			        			       
 			    	getCellsModel().setRowProperty(row - 1, PropertyType.BLColor, TableConstant.UNDEFINED); 			        
 			    }
 			    break;
 			case PropertyType.TLType:
 			   if(row != 0){ 			        			       
 				  getCellsModel().setRowProperty(row - 1, PropertyType.BLType, TableConstant.UNDEFINED); 			        
			    }
			    break;
 			case PropertyType.BLColor://��һ��
 			    if(row != getCellsModel().getRowNum()-1){ 			       
 			    	getCellsModel().setRowProperty(row + 1, PropertyType.TLColor, TableConstant.UNDEFINED); 			
 			    }
 			    break;
 			case PropertyType.BLType:
 			    if(row != getCellsModel().getRowNum()-1){
 			    	getCellsModel().setRowProperty(row + 1, PropertyType.TLType, TableConstant.UNDEFINED); 			
 			    }
 			    break; 			
 			default:
 			    break;
 		}
            }
		
    }
    /**
     * ȡ��ѡ������������ұ߿���������
     * @param sourceCols ������
     * @param nType �߿���������
     */
    private void clearCircumjacentColBorder(int[] sourceCols, int nType){        
        Format tmpFormat;       
        int col;
        if(sourceCols == null || sourceCols.length == 0) return;
        for(int i = 0; i < sourceCols.length; i++) {
            col = sourceCols[i];
             switch(nType){
 			case PropertyType.LLColor://����У�	
 			    if(col != 0){
 			        tmpFormat = getCellsModel().getColFormat(col - 1);
 			        if(tmpFormat != null){
 			            PropertyType.setPropertyByType(tmpFormat, PropertyType.RLColor, TableConstant.UNDEFINED);
 			        }
 			    }
 			    break;
 			case PropertyType.LLType:
 			    if(col != 0){
 			       tmpFormat = getCellsModel().getColFormat(col - 1);
 			        if(tmpFormat != null){
 			            PropertyType.setPropertyByType(tmpFormat, PropertyType.RLType, TableConstant.UNDEFINED);
 			        }
 			    }
 			    break;
 			case PropertyType.RLColor://�ұ���
 			    if(col != getCellsModel().getColNum()-1){
 			       tmpFormat = getCellsModel().getColFormat(col + 1);
 			        if(tmpFormat != null){
 			            PropertyType.setPropertyByType(tmpFormat, PropertyType.LLColor, TableConstant.UNDEFINED);
 			        }
 			    }
 			    break;
 			case PropertyType.RLType:
 			    if(col != getCellsModel().getColNum()-1){
 			       tmpFormat = getCellsModel().getColFormat(col + 1);
 			        if(tmpFormat != null){
 			            PropertyType.setPropertyByType(tmpFormat, PropertyType.LLType, TableConstant.UNDEFINED);
 			        }
 			    }
 			    break; 			
 			default:
 			    break;
 		}
            }
		
    }
    /**
     * ��ȡ�������ı���
     * @return ����UfoReport��
     */
    public UfoReport getReport() {
        return m_RepTool;
    }
     
    

   /**
    * ������߿���Ϣʱ������������������Ҫ�������еļ��ϡ�
    * �������������������ߺ���ɫ:������Щ������������ߵ���
    * ����������������ұ��ߺ���ɫ:������Щ�����������ұߵ���
    * ��������������Ĵ�ֱ�ߺ���ɫ:������Щ�������з����ұߵ���
    * caijie  2004-11-25
    * @param nType ��ʽ���Ͳμ�PropertyType
    * @return ��Ҫ����������
    */
    public int[] getAimCols(final int[] selectedCols, final int direction) {////Ϊ�˲��ԣ���Ϊpublic������
        Vector<Integer> tmp = new Vector<Integer>();
        switch (direction) {
        case Format.LEFTLINE://������Щ������������ߵ���        
            for (int i = 0; i < selectedCols.length; i++) {
                if (!isInSelectedRowCols(selectedCols, selectedCols[i] - 1)) {
                    tmp.add(new Integer(selectedCols[i]));
                }
            }
            break;
        case Format.RIGHTLINE://���������������ұߵ���        
            for (int i = 0; i < selectedCols.length; i++) {
                if (!isInSelectedRowCols(selectedCols, selectedCols[i] + 1)) {
                    tmp.add(new Integer(selectedCols[i]));
                }
            }
            break;
        case SetCellAttrExt.MIDDLE_LEFT ://�й�������:���������г�ȥ������е�������				
            for (int i = 0; i < selectedCols.length; i++) {
                if (isInSelectedRowCols(selectedCols, selectedCols[i] - 1)) {
                    tmp.add(new Integer(selectedCols[i]));
                }
            }
            break;        	
		case SetCellAttrExt.MIDDLE_RIGHT://�й�����ұ߿�:���������г�ȥ���Ҷ��е�������				
		    for (int i = 0; i < selectedCols.length; i++) {
                if (isInSelectedRowCols(selectedCols, selectedCols[i] + 1)) {
                    tmp.add(new Integer(selectedCols[i]));
                }
            }
            break;	
        default: //��������·���������
            throw new IllegalArgumentException();
        }
        int[] array = new int[tmp.size()];
        int i = 0;
        while (i < tmp.size()) {
            array[i] = ((Integer) tmp.elementAt(i)).intValue();
            i++;
        }
        return array;
    }

    /**
     * ������߿���Ϣʱ������������������Ҫ�������еļ��ϡ�
     * ����������������ϱ��ߺ���ɫ:������Щ�����������ϱߵ���
     * ����������������±��ߺ���ɫ:������Щ�����������±ߵ���
     * ���������������ˮƽ�ߺ���ɫ:������Щ�������з����±ߵ���
     * caijie  2004-11-25
     * @param nType ��ʽ���Ͳμ�PropertyType
     * @return ��Ҫ����������
     */
    public int[] getAimRows(final int[] selectedRows, final int nType) {//Ϊ�˲��ԣ���Ϊpublic������
        Vector tmp = new Vector();
        switch (nType) {
        case PropertyType.TLType://���������������Ϸ�����
        case PropertyType.TLColor:
            for (int i = 0; i < selectedRows.length; i++) {
                if(selectedRows[i] == 0){
                    tmp.add(new Integer(selectedRows[i]));
                }else if (!isInSelectedRowCols(selectedRows, selectedRows[i] - 1)) {
                    tmp.add(new Integer(selectedRows[i]));
                }
            }
            break;
        case PropertyType.BLType://���������������·�����
        case PropertyType.BLColor:
            for (int i = 0; i < selectedRows.length; i++) {
                if(selectedRows[i] == getCellsModel().getRowNum()-1){
                    tmp.add(new Integer(selectedRows[i]));
                }else if (!isInSelectedRowCols(selectedRows, selectedRows[i] + 1)) {
                    tmp.add(new Integer(selectedRows[i]));
                }
            }
            break;        
        case SetCellAttrExt.MIDDLE_TOP ://�й�����ϱ߿�:���������г�ȥ���϶��е�������				
            for (int i = 0; i < selectedRows.length; i++) {
                if(selectedRows[i] == 0){
                    
                }else if (isInSelectedRowCols(selectedRows, selectedRows[i] - 1)) {
                    tmp.add(new Integer(selectedRows[i]));
                }
            }
        	break;
		case SetCellAttrExt.MIDDLE_BOTTOM://�й�����±߿�:���������г�ȥ���¶��е�������				
		    for (int i = 0; i < selectedRows.length; i++) {
                if(selectedRows[i] == getCellsModel().getRowNum()-1){
                    
                }else if (isInSelectedRowCols(selectedRows, selectedRows[i] + 1)) {
                    tmp.add(new Integer(selectedRows[i]));
                }
            }
            break;	
        default: //��������·���������
            for (int i = 0; i < selectedRows.length; i++) {
                tmp.add(new Integer(selectedRows[i]));
            }
            break;
        }
        int[] array = new int[tmp.size()];
        int i = 0;
        while (i < tmp.size()) {
            array[i] = ((Integer) tmp.elementAt(i)).intValue();
            i++;
        }
        return array;
    }
    
    /**
	 * ����������ת��Ϊ�ٽ���Ԫ�����������.ʹ���߽���ȫ��Ϊ�������� �������ڣ�(2004-5-26 16:47:10)
	 * add by wangyga 2008-09-04 �˷�����Ҫ��Ϊ�����������õ�Ԫ������������ʱ���������Ԫ��value
	 * ������������õ�Ԫ���Բ��������дsetCellsProperty()
	 * @param pos
	 *            com.ufsoft.table.CellPosition
	 * @param nType
	 *            int
	 * @param nValue
	 *            int
	 */
	protected void setCellProperty(CellPosition pos, int nType, int nValue) {
		// ��ʼ��������
		Format format = getCellsModel().getFormat(pos);
		if (format == null) {
			format = new IufoFormat();
		}
//		if (nType == PropertyType.DataType) {// ��������µ��������ͣ��������Ԫ��ԭֵ��
//			int oldDataType = PropertyType.getPropertyByType(format,
//					PropertyType.DataType);
//			if (oldDataType != nValue) {
//				Cell cell = getCellsModel().getCell(pos);
//				if (cell != null) {
//					cell.setValue(null);
//				}
//			}
//		}
		PropertyType.setPropertyByType(format, nType, nValue);
		getCellsModel().setCellFormat(pos.getRow(), pos.getColumn(), format);// ���ô˷���������ʽ�ı��¼���
		// ��������кʹ���������ʱ,�Զ������и�.
		if (nType == PropertyType.ChangeLine || nType == PropertyType.FontSize) {
			getCellsModel().headerPropertyChanged(new PropertyChangeEvent(getCellsModel().getRowHeaderModel(),
					Header.HEADER_AUTORESIZE, Boolean.FALSE, new Integer(pos
							.getRow())));
		}
	}
	
    /**
     * �ж�һ����Ԫλ���Ƿ�����ѡ������������
     * �������ڣ�(2004-5-19 13:28:09)
     * @param cell
     * @return boolean
     */
    public boolean isInSelCellPos(final CellPosition[] selCellPos,final CellPosition cell) {
        if(selCellPos == null) return false;
        for (int i = 0; i < selCellPos.length; i++) {
            if (selCellPos[i].compareTo(cell) == 0) {
                return true;
            }
        }
        return false;

    }
    /**
     * �ж�һ������λ���Ƿ�����ѡ���������� �������ڣ�(2004-5-19 13:28:09)
     * 
     * @return boolean
     */
    public boolean isInSelectedRowCols(final int[] rowOrCols, final int index) {
        if(rowOrCols == null) return false;
        for (int i = 0; i < rowOrCols.length; i++) {
            if (index == rowOrCols[i]) {
                return true;
            }
        }
        return false;
    }

	protected CellsModel getCellsModel() {
		return m_RepTool.getCellsModel();
	}

}