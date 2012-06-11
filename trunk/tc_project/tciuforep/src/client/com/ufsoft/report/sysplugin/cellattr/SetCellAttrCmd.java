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
 * 设置单元属性
 * 
 * @author caijie
 * @since 3.1
 */
public class SetCellAttrCmd extends UfoCommand {

    //  报表工具
    private UfoReport m_RepTool = null;
    //模型
    private CellsModel m_cellsModel;
    
    /**
     * @param rep
     *            UfoReport - 报表工具
     */
    public SetCellAttrCmd(UfoReport rep) {
        super();
        if (rep == null) {
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000824"));//非法的报表工具参数 
        }
        this.m_RepTool = rep;
        m_cellsModel = m_RepTool.getCellsModel();
    }

    /**
     * 设置全表、行、列和区域或单元的属性值
     * params[1]:作用范围 t/c/r/a  t表示全表table c表示列column r表示行row a表示区域area
     * params[0]:作用对象  对于params[0] = 't' 此值表示作用属性的Hashtable
     *                    对于params[0] = 'c' 此值表示作用列数组
     * 					  对于params[0] = 'r' 此值表示作用行数组
     * 					  对于params[0] = 'a' 此值表示作用区域数组
     * params[2]:属性HashTable 
     * 说明 当作用在全表时，只需要两个参数。
     */
    public void execute(Object[] params) {
        if ((params == null ) || (params.length == 0))
            return;     
        if(getCellsModel() == null)
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000825"), getReport(), null);//模型不能为空
        
        int paramIndex = 1;
      
        try {    
        	//提高效率，禁止绘制事件 liuyy.
    		getCellsModel().setEnableEvent(false);
    		
            Hashtable<Integer,Integer> propertyCache = null;//(Hashtable)params[0];//属性缓存
            propertyCache =  (Hashtable<Integer,Integer>) params[0];
            if((propertyCache == null) || propertyCache.isEmpty()){
            	return;
            }
            while(params[paramIndex] != null){
                char scope = ((Character)params[paramIndex++]).charValue();
                switch (scope) {
                case 't': //全表  
                    setTableFormat(propertyCache);
                    break;
                case 'r'://行
                    int[] rows = (int[])params[paramIndex++];
                    if((rows == null) || (rows.length == 0)) return;  
                    
                    setRowsFormat(rows, propertyCache);
                    
                    
                    break;
                case 'c'://列
                    int[] cols = (int[])params[paramIndex++];
                    if((cols == null) || (cols.length == 0)) return;                   
                    setColumnsFormat(cols, propertyCache);
                    break;
                case 'a'://区域
                    CellPosition[] cells = (CellPosition[])params[paramIndex++];
                    if((cells == null) || (cells.length == 0)) return;                  
                    setCellsFormat(cells, propertyCache);
                    break;
                default:
                    UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000826"), getReport(), null);//设置属性的参数错误
                }                      
            }
            
        } catch (Exception e) {   
            AppDebug.debug(e);
            
        } finally {
        	getCellsModel().setEnableEvent(true);
//        	//强行刷新所选区域
//        	getCellsModel().getSelectModel().fireSelectedChanged(getCellsModel().getSelectModel().getSelectedAreas());
        	
        }
        
        
        
    }

//    private void setProperty(Hashtable propertyCache){
//        // 将cacheSetProperty中的值设置到模型中。
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
//     * 设置属性。当属于边框设置时，根据选定区域类型判断需设置的区域。
//     * @param nType  int 类型 参考PropertyType
//     * @param nValue int 值
//     */
//    private void setProperty(int nType, int nValue) {//为了测试，改为public方法。
//        switch (m_AreaType) {
//        case 0: //区域类型 0 一般区域
//            
//            return;
//        case 1: //区域类型1 整行区域
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
//        case 2: //区域类型2 整列区域
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
//        case 3: //区域类型3 整表区域
//            getUfoTableModel().setTableProperty(nType, nValue);
//            break;
//        default:
//            break;
//        }
//
//    }
    /**
     * 设置整表属性。当属于边框设置时，根据选定区域类型判断需设置的区域。
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
     * 设置列属性。当属于边框设置时，根据选定区域类型判断需设置的区域。
     * @param nType  int 类型 参考PropertyType
     * @param nValue int 值
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
	                setColsProperty(actionCols, nType, nValue);//设置区域内单元
	                clearCircumjacentColBorder(actionCols, nType);//如果共有该边框的区外单元的存在属性，则清楚此属性
	            }
                break;
            case PropertyType.RLType:              
            case PropertyType.RLColor:  
                actionCols = getAimCols(targetCols, Format.RIGHTLINE);
                if (actionCols != null) {
                    setColsProperty(actionCols, nType, nValue);//设置区域内单元
                    clearCircumjacentColBorder(actionCols, nType);//如果共有该边框的区外单元的存在属性，则清楚此属性
                }
                break;    
            case PropertyType.HLType:              
            case PropertyType.HLColor:
                setColsProperty(targetCols, nType, nValue);
                break;
            case PropertyType.VLType:
                actionCols = getAimCols(targetCols, SetCellAttrExt.MIDDLE_LEFT);//垂直边框需要同时设置单元边框非边缘边框的左边框
                if (actionCols != null) {
                    setColsProperty(actionCols, PropertyType.LLType, nValue);
                }

                actionCols = getAimCols(targetCols, SetCellAttrExt.MIDDLE_RIGHT);//水平边框需要同时设置单元边框非边缘边框的右边框
                if (actionCols != null) {
                    setColsProperty(actionCols, PropertyType.RLType, nValue);
                }
                break;
            case PropertyType.VLColor:                
                actionCols = getAimCols(targetCols, SetCellAttrExt.MIDDLE_LEFT);//水平边框需要同时设置单元边框非边缘边框的左边框
                if (actionCols != null) {
                    setColsProperty(actionCols, PropertyType.LLColor, nValue);
                }
                actionCols = getAimCols(targetCols, SetCellAttrExt.MIDDLE_RIGHT);//水平边框需要同时设置单元边框非边缘边框的右边框
                if (actionCols != null) {
                    setColsProperty(actionCols, PropertyType.RLColor, nValue);
                }
                break;   
            default: //非边框属性
                    setColsProperty(targetCols, nType, nValue);           
                break;
            }

        }

    }
    /**
     * 设置行属性。当属于边框设置时，根据选定区域类型判断需设置的区域。
     * @param nType  int 类型 参考PropertyType
     * @param nValue int 值
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
                actionRows = getAimRows(targetRows, SetCellAttrExt.MIDDLE_BOTTOM);//水平边框需要同时设置单元边框非边缘边框的下边框
                if (actionRows != null) {
                    setRowsProperty(actionRows, PropertyType.BLType, nValue);
                }

                actionRows = getAimRows(targetRows, SetCellAttrExt.MIDDLE_TOP);//水平边框需要同时设置单元边框非边缘边框的上边框
                if (actionRows != null) {
                    setRowsProperty(actionRows, PropertyType.TLType, nValue);
                }
                break;
            case PropertyType.HLColor:                
                actionRows = getAimRows(targetRows, SetCellAttrExt.MIDDLE_BOTTOM);//水平边框需要同时设置单元边框非边缘边框的下边框
                if (actionRows != null) {
                    setRowsProperty(actionRows, PropertyType.BLColor, nValue);
                }
                actionRows = getAimRows(targetRows, SetCellAttrExt.MIDDLE_TOP);//水平边框需要同时设置单元边框非边缘边框的上边框
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
                    setRowsProperty(actionRows, nType, nValue);//设置区域内单元
                    clearCircumjacentRowBorder(actionRows, nType);//如果共有该边框的区外单元的存在属性，则清楚此属性
                }
            break;
            default:
                //由于方法getAimRows()返回的actionRows返回的是需要操作的行,
                //故直接设置就可以,不需要考虑单元格之间的关系
                actionRows = getAimRows(targetRows, nType);
                if (actionRows != null) {
                    setRowsProperty(actionRows, nType, nValue);
                }
                break;
            }

        }

    }
    /**
     * 设置单元属性
     * 原则：对于处于边缘的边框属性，如果在设置区域外的相邻单元有边框属性的，将其冲突的边框属性去除
     * 对于设置区域中央的边框属性，因每边框都被两单元格共有，两个单元都设置属性  
     * @param poses 需要设置的单元数组
     */
    public void setCellsFormat(CellPosition[] poses, Hashtable<Integer, Integer> propertyCache){ 
    	if(poses == null || poses.length < 1){
    		return;
    	}
    	
    	Cell[] oldCells = null;
    	CellUndo edit = null;
    	//超过1000，不做undoredo处理
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
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.HLType);//水平边框需要同时设置非边缘单元边框的上下两边
	               
                    setCellsProperty(tmpCells, PropertyType.TLType, nValue);
                    setCellsProperty(tmpCells, PropertyType.BLType, nValue);
	               	
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.TLType);//水平边框需要同时设置最上面边缘单元边框的下边框
	                setCellsProperty(tmpCells, PropertyType.BLType, nValue);
//	                if (tmpCells != null) {
//	                    setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.BLType), PropertyType.BLType, nValue);
//	                }
	
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.BLType);//水平边框需要同时设置最下面边缘单元边框的上边框
	                setCellsProperty(tmpCells, PropertyType.TLType, nValue);
//	                if (tmpCells != null) {
//	                    setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.TLType), PropertyType.TLType, nValue);                                      
//	                }
	                break;
	            case PropertyType.HLColor:
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.HLColor);//水平边框需要同时设置非边缘单元边框的上下两边
	                
                    setCellsProperty(tmpCells, PropertyType.TLColor, nValue);
                    setCellsProperty(tmpCells, PropertyType.BLColor, nValue);
	                
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.TLColor);//水平边框需要同时设置最上面边缘单元边框的下边框
	                setCellsProperty(tmpCells, PropertyType.BLColor, nValue);
	 
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.BLColor);//水平边框需要同时设置最下面边缘单元边框的上边框
	                setCellsProperty(tmpCells, PropertyType.TLColor, nValue);
 
	                break;
	            case PropertyType.VLType:
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.VLType);//垂直边框需要同时设置非边缘单元边框的左右两边
	                
                    setCellsProperty(tmpCells, PropertyType.RLType, nValue);
                    setCellsProperty(tmpCells, PropertyType.LLType, nValue);
	                
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.LLType);//垂直边框需要同时设置最左面边缘单元边框的右边框
	                setCellsProperty(tmpCells, PropertyType.RLType, nValue);
//	                if (tmpCells != null) {                   
//	                     setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.RLType), PropertyType.RLType, nValue);                                    
//	                }
	                
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.RLType);//垂直边框需要同时设置最右面边缘单元边框的左边框
	                setCellsProperty(tmpCells, PropertyType.LLType, nValue);
//	                if (tmpCells != null) {                   
//	                      setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.LLType), PropertyType.LLType, nValue);                        
//	                }
	                break;
	            case PropertyType.VLColor:
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.VLColor);//垂直边框需要同时设置非边缘单元边框的左右两边	                
                    setCellsProperty(tmpCells, PropertyType.RLColor, nValue);
                    setCellsProperty(tmpCells, PropertyType.LLColor, nValue);
	                
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.LLColor);//垂直边框需要同时设置最左面边缘单元边框的右边框
	                setCellsProperty(tmpCells, PropertyType.RLColor, nValue);                 
//	                     setCellsProperty(getMiddleBorderCells(tmpCells, cells, PropertyType.RLColor), PropertyType.RLColor, nValue);                                       
	              
	                tmpCells = m_cellsModel.filterCells(poses, PropertyType.RLColor);//垂直边框需要同时设置最右面边缘单元边框的左边框
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
	                setCellsProperty(tmpCells, nType, nValue);//设置区域内单元
	                if(nValue == TableConstant.UNDEFINED){
	                	clearCircumjacentCellsBorder(tmpCells, nType, nValue);//如果共有该边框的区外单元的存在属性，则清楚此属性
	                }
                     
	                break;	                
	            default: //对角线走此分支.
	                //由于方法m_cellsModel.filterCells()返回的tmpCells返回的是需要操作的单元格,
	                //故直接设置就可以,不需要考虑单元格之间的关系
	                tmpCells =  m_cellsModel.filterCells(poses, nType);	            	
	                setCellsProperty(tmpCells, nType, nValue);
	                
	            break;
            }
           
        }
	}
    
    /**
     * 从cells中找出共享nType边框的单元
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
        if(tmpList.isEmpty()){//要去除下边单元不在区域中的单元            
            return null;
        } else{
           return (CellPosition[])tmpList.toArray(new CellPosition[0]);
        }
       
    }
    /**
     * 设置单元属性
     * @param arr 单元位置数组
     * @param nType 属性类型
     * @param nValue 属性值
     */
    protected void setCellsProperty(CellPosition[] arr, int nType, int nValue) {
		if (arr == null || arr.length < 1)
			return;

		for (CellPosition pos : arr) {
			getCellsModel().setCellProperty(pos, nType, nValue);
		}
		 

	}
    /**
     * 设置报表行属性
     * @param targetRows 行数组
     * @param nType 属性类型
     * @param nValue 属性值
     */
    private void setRowsProperty(int[] targetRows ,int nType, int nValue){
        if(targetRows == null) return;
        int cellIndex = 0;    	
        while (cellIndex < targetRows.length) {            
        	getCellsModel().setRowProperty(targetRows[cellIndex], nType, nValue);            
            cellIndex++;
        }
 
        //add by wangyga  2008-9-11处理整行设置格式时的绘制问题
        m_RepTool.getTable().getCells().repaint(getCellsModel().getSelectModel().getSelectedArea(),true);
    }
    /**
     * 设置列属性
     * @param targetCols 报表列数组
     * @param nType 属性类型
     * @param nValue 属性值
     */
    private void setColsProperty(int[] targetCols ,int nType, int nValue){
        if(targetCols == null) return;
        int cellIndex = 0;    	
        while (cellIndex < targetCols.length) {            
        	getCellsModel().setColProperty(targetCols[cellIndex], nType, nValue);            
            cellIndex++;
        }
        //add by wangyga  2008-9-11处理整行设置格式时的绘制问题
        m_RepTool.getTable().getCells().repaint(getCellsModel().getSelectModel().getSelectedArea(),true);
    }
    
    /**
     * 取消单元属性
     * @param row 要取消的单元的行
     * @param col 要取消的单元的列
     * @param type 要取消的属性类型
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
		 * 清除选择单元组cells的相邻单元边框值
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
	 			case PropertyType.TLColor://上方单元；	
	 				type = PropertyType.BLColor;    	  
	 			    break;	 				
	 			case PropertyType.TLType:
	 				type = PropertyType.BLType;	  
	 			    break;
	 			    
	 			case PropertyType.BLColor://下方单元 
	 				type = PropertyType.TLColor;	 				 
	 			    break;
	 			case PropertyType.BLType: 
	 				type = PropertyType.TLType;	 				 
	 			    break;	  
	 			case PropertyType.LLColor://左边单元 
	 				type = PropertyType.RLColor;	 				 
	 			    break;	 
	 			case PropertyType.LLType: 
	 				type = PropertyType.RLType;	 				 
	 			    break;
	 			case PropertyType.RLColor://右边单元 
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
     * 取消选择行数组的上下边框线条属性
     * @param sourceRows 行数组
     * @param nType 边框属性类型
     */
    private void clearCircumjacentRowBorder(int[] sourceRows, int nType){        
//        Format tmpFormat;       
        int row;
        if(sourceRows == null || sourceRows.length == 0) return;
        for(int i = 0; i < sourceRows.length; i++) {
            row = sourceRows[i];
             switch(nType){
 			case PropertyType.TLColor://上一行；	
 			    if(row != 0){ 			        			       
 			    	getCellsModel().setRowProperty(row - 1, PropertyType.BLColor, TableConstant.UNDEFINED); 			        
 			    }
 			    break;
 			case PropertyType.TLType:
 			   if(row != 0){ 			        			       
 				  getCellsModel().setRowProperty(row - 1, PropertyType.BLType, TableConstant.UNDEFINED); 			        
			    }
			    break;
 			case PropertyType.BLColor://下一行
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
     * 取消选择列数组的左右边框线条属性
     * @param sourceCols 列数组
     * @param nType 边框属性类型
     */
    private void clearCircumjacentColBorder(int[] sourceCols, int nType){        
        Format tmpFormat;       
        int col;
        if(sourceCols == null || sourceCols.length == 0) return;
        for(int i = 0; i < sourceCols.length; i++) {
            col = sourceCols[i];
             switch(nType){
 			case PropertyType.LLColor://左边列；	
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
 			case PropertyType.RLColor://右边列
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
     * 获取所操作的报表
     * @return 返回UfoReport。
     */
    public UfoReport getReport() {
        return m_RepTool;
    }
     
    

   /**
    * 当加入边框信息时，从列数组中挑出需要操作的列的集合。
    * 对于设置列数组的左边线和颜色:返回那些连续列中最左边的列
    * 对于设置列数组的右边线和颜色:返回那些连续列中最右边的列
    * 对于设置列数组的垂直线和颜色:返回那些连续列中非最右边的列
    * caijie  2004-11-25
    * @param nType 格式类型参见PropertyType
    * @return 需要操作的数组
    */
    public int[] getAimCols(final int[] selectedCols, final int direction) {////为了测试，改为public方法。
        Vector<Integer> tmp = new Vector<Integer>();
        switch (direction) {
        case Format.LEFTLINE://返回那些连续列中最左边的列        
            for (int i = 0; i < selectedCols.length; i++) {
                if (!isInSelectedRowCols(selectedCols, selectedCols[i] - 1)) {
                    tmp.add(new Integer(selectedCols[i]));
                }
            }
            break;
        case Format.RIGHTLINE://返回连续列中最右边的列        
            for (int i = 0; i < selectedCols.length; i++) {
                if (!isInSelectedRowCols(selectedCols, selectedCols[i] + 1)) {
                    tmp.add(new Integer(selectedCols[i]));
                }
            }
            break;
        case SetCellAttrExt.MIDDLE_LEFT ://列共享的左框:返回数组中除去最左端列的其余行				
            for (int i = 0; i < selectedCols.length; i++) {
                if (isInSelectedRowCols(selectedCols, selectedCols[i] - 1)) {
                    tmp.add(new Integer(selectedCols[i]));
                }
            }
            break;        	
		case SetCellAttrExt.MIDDLE_RIGHT://列共享的右边框:返回数组中除去最右端列的其余列				
		    for (int i = 0; i < selectedCols.length; i++) {
                if (isInSelectedRowCols(selectedCols, selectedCols[i] + 1)) {
                    tmp.add(new Integer(selectedCols[i]));
                }
            }
            break;	
        default: //其他情况下返回所有列
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
     * 当加入边框信息时，从列数组中挑出需要操作的行的集合。
     * 对于设置行数组的上边线和颜色:返回那些连续行中最上边的列
     * 对于设置行数组的下边线和颜色:返回那些连续行中最下边的列
     * 对于设置行数组的水平线和颜色:返回那些连续行中非最下边的列
     * caijie  2004-11-25
     * @param nType 格式类型参见PropertyType
     * @return 需要操作的数组
     */
    public int[] getAimRows(final int[] selectedRows, final int nType) {//为了测试，改为public方法。
        Vector tmp = new Vector();
        switch (nType) {
        case PropertyType.TLType://返回连续行中最上方的行
        case PropertyType.TLColor:
            for (int i = 0; i < selectedRows.length; i++) {
                if(selectedRows[i] == 0){
                    tmp.add(new Integer(selectedRows[i]));
                }else if (!isInSelectedRowCols(selectedRows, selectedRows[i] - 1)) {
                    tmp.add(new Integer(selectedRows[i]));
                }
            }
            break;
        case PropertyType.BLType://返回连续行中最下方的行
        case PropertyType.BLColor:
            for (int i = 0; i < selectedRows.length; i++) {
                if(selectedRows[i] == getCellsModel().getRowNum()-1){
                    tmp.add(new Integer(selectedRows[i]));
                }else if (!isInSelectedRowCols(selectedRows, selectedRows[i] + 1)) {
                    tmp.add(new Integer(selectedRows[i]));
                }
            }
            break;        
        case SetCellAttrExt.MIDDLE_TOP ://行共享的上边框:返回数组中除去最上端行的其余行				
            for (int i = 0; i < selectedRows.length; i++) {
                if(selectedRows[i] == 0){
                    
                }else if (isInSelectedRowCols(selectedRows, selectedRows[i] - 1)) {
                    tmp.add(new Integer(selectedRows[i]));
                }
            }
        	break;
		case SetCellAttrExt.MIDDLE_BOTTOM://行共享的下边框:返回数组中除去最下端行的其余行				
		    for (int i = 0; i < selectedRows.length; i++) {
                if(selectedRows[i] == getCellsModel().getRowNum()-1){
                    
                }else if (isInSelectedRowCols(selectedRows, selectedRows[i] + 1)) {
                    tmp.add(new Integer(selectedRows[i]));
                }
            }
            break;	
        default: //其他情况下返回所有行
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
	 * 将上左属性转换为临近单元格的下右属性.使除边界外全部为下右属性 创建日期：(2004-5-26 16:47:10)
	 * add by wangyga 2008-09-04 此方法主要是为分析报表设置单元属性数据类型时，不清楚单元的value
	 * 分析报表的设置单元属性插件可以重写setCellsProperty()
	 * @param pos
	 *            com.ufsoft.table.CellPosition
	 * @param nType
	 *            int
	 * @param nValue
	 *            int
	 */
	protected void setCellProperty(CellPosition pos, int nType, int nValue) {
		// 开始保存数据
		Format format = getCellsModel().getFormat(pos);
		if (format == null) {
			format = new IufoFormat();
		}
//		if (nType == PropertyType.DataType) {// 如果设置新的数据类型，将清除单元中原值。
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
		getCellsModel().setCellFormat(pos.getRow(), pos.getColumn(), format);// 调用此方法激发格式改变事件。
		// 如果是折行和大字体设置时,自动设置行高.
		if (nType == PropertyType.ChangeLine || nType == PropertyType.FontSize) {
			getCellsModel().headerPropertyChanged(new PropertyChangeEvent(getCellsModel().getRowHeaderModel(),
					Header.HEADER_AUTORESIZE, Boolean.FALSE, new Integer(pos
							.getRow())));
		}
	}
	
    /**
     * 判断一个单元位置是否在所选定的区域里里
     * 创建日期：(2004-5-19 13:28:09)
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
     * 判断一个行列位置是否在所选定的行列里 创建日期：(2004-5-19 13:28:09)
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