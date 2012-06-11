package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.util.Hashtable;
import java.util.Vector;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.BaseDataArea;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessSetter;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.IDataProcessType;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.TableArea;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.format.Format;

public class GroupDataProcessCmd extends AbsDataProcessCmd {

	protected boolean excuteImpl(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds) {
		return generateGroupFormat(dynAreaCell, vecAllDynAreaDPFlds);
	}

	private boolean generateGroupFormat(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds) {
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
            IDataProcessType.PROCESS_GROUP, areaDataProcess, vecAllDynAreaDPFlds, hashDataTypes, getReport());
        BaseDataArea dataArea = areaDataProcess.getTableArea();
        if(!bHasAreaDataProcess){
            //没有旧的区域数据处理，并且用户没有定义新的，则还原为null
            if(dataArea == null){
                areaDataProcess = null;
            }
        }
        
        if(dataArea != null && bDirty){
        	//回设区域数据处理
            getDynAreaModel().setDataProcess(dynAreaVO.getDynamicAreaPK(),areaDataProcess);
            
            //动态区域的大小范围小于生成的数据处理区域大小范围时，
            //需要在动态区域的最下行或最右列，增加行或类
            AreaPosition dynRelateUfoArea = DataProcessUtil.setAreaRange(dynAreaCell.getArea(), DataProcessUtil.STRHASPOS_ABSOLUTE);
            if(dataArea.getWholeAreaPos().contain(dynRelateUfoArea) && !dataArea.equals(dynRelateUfoArea)){
                adjustDynAreaRange(dynAreaCell, dataArea.getWholeAreaPos(), dynRelateUfoArea);
            }
        }

        return bDirty;
	}
	
	@Override
	protected void adjustDynAreaFormat(DynAreaCell dynAreaCell, AreaPosition oldArea) {
		AreaDataProcess areaDataProcess = getDynAreaModel().getDataProcess(dynAreaCell.getDynAreaVO().getDynamicAreaPK());
        if(areaDataProcess == null || areaDataProcess.getTableArea()==null || oldArea == null){
        	return;
        }
        
        TableArea dataArea = (TableArea)areaDataProcess.getTableArea();
        //modified by liulp,2008-07-21,老版数据处理3.4移植到新版报表工具时(since 5.0)的错误：分组标题或分层的则有多个TableArea嵌套而不是直接的DetailArea
//      AreaPosition detailArea = dataArea.getDetailArea().getWholeAreaPos();        
        AreaPosition detailArea = dataArea.containsDetailArea().getWholeAreaPos();
      
        AreaPosition dynArea = dynAreaCell.getArea();
        AreaPosition absoluteDetailArea = AreaPosition.getInstance(
        		CellPosition.getInstance(
        				detailArea.getStart().getRow()+dynArea.getStart().getRow(),
        				detailArea.getStart().getColumn()+dynArea.getStart().getColumn()
        				
        		),
        		CellPosition.getInstance(
        				detailArea.getEnd().getRow()+dynArea.getStart().getRow(),
        				detailArea.getEnd().getColumn()+dynArea.getStart().getColumn()
        		)                        				
        );
        CellPosition[] formatCells = (CellPosition[]) getCellsModel().getSeperateCellPos(oldArea).toArray(new CellPosition[0]);
        CellPosition[] cells = (CellPosition[]) getCellsModel().getSeperateCellPos(absoluteDetailArea).toArray(new CellPosition[0]);
        
        int index = 0;
        for(CellPosition pos:cells){
        	Format format = getCellsModel().getFormat(pos);
        	if (format == null) {
    			format = new IufoFormat();			
    		}
        	
        	Format oldFormat = getCellsModel().getFormat(formatCells[index++]);
        	if(oldFormat != null){
        		int cellType = oldFormat.getCellType();
        		PropertyType.setPropertyByType(format, PropertyType.DataType, cellType);
        		getCellsModel().setCellFormat(pos.getRow(), pos.getColumn(), format);
        	}
        }        
	}
	
	@Override
	protected int getDataProcessType() {
		return IDataProcessType.PROCESS_GROUP;
	}
}
