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
     * ���ݴ���Ľ�����塣
     *
     * �������ڣ�(2003-8-12 12:18:52)
     * @author������Ƽ
     * @return boolean - ���ǣ����޸��򷵻�ture,���򷵻�false
     * @param dynAreaVO com.ufsoft.iuforeport.reporttool.temp.DynamicAreaVO
     * @param vecAllDynAreaDPFlds java.util.Vector - Ԫ��ΪDataProcessFld
     */
    private boolean generateCrossTable(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds){
    	DynAreaVO dynAreaVO = dynAreaCell.getDynAreaVO();
        //׼�����������ݴ���������Ա:���ݴ�����,���ݴ�������
        AreaDataProcess areaDataProcess = getDynAreaModel().getDataProcess(dynAreaCell.getDynAreaVO().getDynamicAreaPK());
        boolean bHasAreaDataProcess = true;
        if(areaDataProcess == null){
            areaDataProcess = new AreaDataProcess();
            bHasAreaDataProcess = false;
        }
        if(!areaDataProcess.isUserDefined()){
            //ˢ���������ݴ�����������п������������ɸѡ�ȶ��壩
            AreaPosition dynRelateArea = DataProcessUtil.setAreaRange(dynAreaCell.getArea(),//getDynAreaVO().getOriArea(),
            		DataProcessUtil.STRHASPOS_ABSOLUTE);
            areaDataProcess.setDynRelateArea(dynRelateArea);            
        }
        //�������ݴ����壬�����ݴ��������ʽ
        //�����
        Hashtable<String, Integer> hashDataTypes = DataProcessUtil.getMeasTypes(getMeasureModel(), dynAreaCell, vecAllDynAreaDPFlds);
        boolean bDirty = DataProcessSetter.generateDefAndFormat(dynAreaCell,
            IDataProcessType.PROCESS_CROSSTABLE, areaDataProcess, vecAllDynAreaDPFlds, hashDataTypes, getReport());
        BaseDataArea dataArea = areaDataProcess.getTableArea();
        if(!bHasAreaDataProcess){
            //û�оɵ��������ݴ��������û�û�ж����µģ���ԭΪnull
            if(dataArea == null){
                areaDataProcess = null;
            }
        }

        if(bDirty){
        	//�����������ݴ���
            getDynAreaModel().setDataProcess(dynAreaVO.getDynamicAreaPK(),areaDataProcess);
            
            //��̬����Ĵ�С��ΧС�����ɵ����ݴ��������С��Χʱ��
            //��Ҫ�ڶ�̬����������л������У������л���
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
        	
        	//���ý�����ӳ��Ŀ��PK����ά�ȡ���ά�ȡ������ֶ���ʾλ��(CellPosition)
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
        	
        	//��ӳ����������ָ����߹ؼ��֣����ұ�ӳ��������ý����ǰ�ĵ�Ԫλ��
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
     * ���ݶ�̬����Ԫ������λ�ã�����ʵ��λ�á�
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
