package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.BaseDataArea;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.CrossTabDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.CrossTableFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessSetter;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.IDataProcessType;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.format.Format;


public class CrossTableDataProcessCmd extends AbsDataProcessCmd {
	
	protected boolean excuteImpl(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds) {
		return generateCrossTable(dynAreaCell,vecAllDynAreaDPFlds);
	}
    
	/**
     * 数据处理的交叉表定义。
     *
     * 创建日期：(2003-8-12 12:18:52)
     * @author：刘良萍
     * @return boolean - 脏标记，有修改则返回ture,否则返回false
     * @param dynAreaVO com.ufsoft.iuforeport.reporttool.temp.DynamicAreaVO
     * @param vecAllDynAreaDPFlds java.util.Vector - 元素为DataProcessFld
     */
    private boolean generateCrossTable(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds){
    	DynAreaVO dynAreaVO = dynAreaCell.getDynAreaVO();
        //准备好区域数据处理对象及其成员:数据处理定义,数据处理区域
        AreaDataProcess areaDataProcess = getDynAreaModel().getDataProcess(dynAreaCell.getDynAreaVO().getDynamicAreaPK());
        boolean bHasAreaDataProcess = true;
        if(areaDataProcess == null){
            areaDataProcess = new AreaDataProcess();
            bHasAreaDataProcess = false;
        }
        if(!areaDataProcess.isUserDefined()){
            //刷新设置数据处理相对区域（有可能做过排序和筛选等定义）
            AreaPosition dynRelateArea = DataProcessUtil.setAreaRange(dynAreaCell.getArea(),//getDynAreaVO().getOriArea(),
            		DataProcessUtil.STRHASPOS_ABSOLUTE);
            areaDataProcess.setDynRelateArea(dynRelateArea);            
        }
        //设置数据处理定义，和数据处理区域格式
        //交叉表
        Hashtable<String, Integer> hashDataTypes = DataProcessUtil.getMeasTypes(getMeasureModel(), dynAreaCell, vecAllDynAreaDPFlds);
        boolean bDirty = DataProcessSetter.generateDefAndFormat(dynAreaCell,
            IDataProcessType.PROCESS_CROSSTABLE, areaDataProcess, vecAllDynAreaDPFlds, hashDataTypes, getReport());
        BaseDataArea dataArea = areaDataProcess.getTableArea();
        if(!bHasAreaDataProcess){
            //没有旧的区域数据处理，并且用户没有定义新的，则还原为null
            if(dataArea == null){
                areaDataProcess = null;
            }
        }

        if(bDirty){
        	//回设区域数据处理
            getDynAreaModel().setDataProcess(dynAreaVO.getDynamicAreaPK(),areaDataProcess);
            
            //动态区域的大小范围小于生成的数据处理区域大小范围时，
            //需要在动态区域的最下行或最右列，增加行或类
            AreaPosition dynRelateUfoArea = DataProcessUtil.setAreaRange(dynAreaCell.getArea(), DataProcessUtil.STRHASPOS_ABSOLUTE);
            if(dataArea != null){
	            if( (dataArea.isContainCol(dynRelateUfoArea.getEnd().getColumn()) || dataArea.isContainRow(dynRelateUfoArea.getEnd().getRow())) &&
	               !dataArea.equals(dynRelateUfoArea)){
	                adjustDynAreaRange(dynAreaCell, dataArea.getWholeAreaPos(), dynRelateUfoArea);
	            }
            }
        }

        return bDirty;
    }
    
    @Override
    protected void adjustDynAreaFormat(DynAreaCell dynAreaCell,
    		AreaPosition oldArea) {
    	AreaDataProcess areaDataProcess = getDynAreaModel().getDataProcess(dynAreaCell.getDynAreaVO().getDynamicAreaPK());
        if(areaDataProcess == null || areaDataProcess.getDataProcessDef() == null){
            return;
        }
        
        CrossTabDef dataProcessDef = (CrossTabDef)areaDataProcess.getDataProcessDef();
        if(dataProcessDef != null){
        	CrossTableFld[] colDimFlds = dataProcessDef.getColDimFlds();
        	CrossTableFld[] rowDimFlds = dataProcessDef.getRowDimFlds();
        	CrossTableFld[] sumDimFlds = dataProcessDef.getSummaryFlds();
        	
        	//设置交叉表后，映射目标PK与行维度、列维度、汇总字段显示位置(CellPosition)
        	Hashtable<String, CellPosition> cellPos2MapPK = new Hashtable<String, CellPosition>();
        	for(CrossTableFld colDimFld : colDimFlds){
        		CellPosition dimFldCell = colDimFld.getFormatRelatCell();
        		dimFldCell = getDynAbsoluteCell(dynAreaCell, dimFldCell);
        		cellPos2MapPK.put(colDimFld.getMapPK(), dimFldCell);
        	}
        	for(CrossTableFld rowDimFld : rowDimFlds){
        		CellPosition dimFldCell = rowDimFld.getFormatRelatCell();
        		dimFldCell = getDynAbsoluteCell(dynAreaCell, dimFldCell);
        		cellPos2MapPK.put(rowDimFld.getMapPK(), dimFldCell);
        	}
        	for(CrossTableFld sumDimFld : sumDimFlds){
        		CellPosition dimFldCell = sumDimFld.getFormatRelatCell();
        		dimFldCell = getDynAbsoluteCell(dynAreaCell, dimFldCell);
        		cellPos2MapPK.put(sumDimFld.getMapPK(), dimFldCell);
        	}
        	
        	//被映射对象可能是指标或者关键字，查找被映射对象设置交叉表前的单元位置
        	KeywordModel keywordModel = this.getKeywordModel();
        	MeasureModel measureModel = this.getMeasureModel();
        	
        	Enumeration<String> enumDataPK = cellPos2MapPK.keys();
        	while(enumDataPK.hasMoreElements()){
        		String strDataPK = enumDataPK.nextElement();
        		CellPosition fmtCellPos = keywordModel.getCellPosByPK(dynAreaCell.getDynAreaPK(), strDataPK);
        		if(fmtCellPos == null)
        			fmtCellPos = measureModel.getMeasurePosByPK(dynAreaCell.getDynAreaPK(), strDataPK);
        		
        		if(fmtCellPos != null){
        			Format cellFormat = getCellsModel().getFormat(fmtCellPos);
        			
        			CellPosition disCellPos = cellPos2MapPK.get(strDataPK);
        			Format disCellFormat = getCellsModel().getFormat(disCellPos);
        			disCellFormat = (disCellFormat == null)?new IufoFormat():disCellFormat;
        			if(cellFormat != null){
        				PropertyType.setPropertyByType(disCellFormat, PropertyType.DataType, cellFormat.getCellType());
                    	getCellsModel().setCellFormat(disCellPos.getRow(), disCellPos.getColumn(), disCellFormat);
        			}        			
        		}
        	}  
        }
    }
    
    /**
     * 根据动态区单元格的相对位置，返回实际位置。
     * @param dynAreaCell
     * @param relateCell
     * @return
     */
    private CellPosition getDynAbsoluteCell(DynAreaCell dynAreaCell, CellPosition relateCell){
    	if(dynAreaCell == null && relateCell == null)
    		return null;
    	
    	CellPosition absoluteCellPos = null;
    	if(dynAreaCell.getDirection() == DynAreaVO.DIRECTION_ROW){
    		absoluteCellPos = CellPosition.getInstance(
    				dynAreaCell.getArea().getStart().getRow() + relateCell.getRow(),
    				relateCell.getColumn()
    				
    		);
    	} else{
    		absoluteCellPos = CellPosition.getInstance(
    				relateCell.getRow(),
    				dynAreaCell.getArea().getStart().getColumn() + relateCell.getColumn()
    				
    		);
    	}
    	
    	return absoluteCellPos;
    }
    
    @Override
    protected int getDataProcessType() {
		return IDataProcessType.PROCESS_CROSSTABLE;
	}
}
