package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.util.Hashtable;
import java.util.Vector;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.BaseDataArea;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessSetter;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.IDataProcessType;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.table.AreaPosition;

public class SortDataProcessCmd extends AbsDataProcessCmd {

	protected boolean excuteImpl(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds) {
		return generateSortDef(dynAreaCell, vecAllDynAreaDPFlds);
	}

	private boolean generateSortDef(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds) {

        //准备好区域数据处理对象及其成员 : 数据处理定义,数据处理区域
        AreaDataProcess areaDataProcess = getDynAreaModel().getDataProcess(dynAreaCell.getDynAreaPK());
        boolean bHasAreaDataProcess = true;
        if(areaDataProcess == null){
            //便于传递区域数据对象、以及细节区域范围信息用
            areaDataProcess = new AreaDataProcess();
            bHasAreaDataProcess = false;
        }
        if(!areaDataProcess.isUserDefined()){
            //刷新设置数据处理相对区域（有可能做过排序和筛选等定义）
            AreaPosition dynRelateArea = DataProcessUtil.setAreaRange(dynAreaCell.getArea(), DataProcessUtil.STRHASPOS_ABSOLUTE);
            areaDataProcess.setDynRelateArea(dynRelateArea);            
        }
        //设置数据处理定义，和数据处理区域格式
        //排序
        Hashtable<String, Integer> hashDataTypes = DataProcessUtil.getMeasTypes(getMeasureModel(),dynAreaCell, vecAllDynAreaDPFlds);
        boolean bDirty = DataProcessSetter.generateDefAndFormat(dynAreaCell, IDataProcessType.PROCESS_SORT,
            areaDataProcess, vecAllDynAreaDPFlds, hashDataTypes, getReport());

        BaseDataArea dataArea = areaDataProcess.getTableArea();
        if(!bHasAreaDataProcess){
            //没有旧的区域数据处理，并且用户没有定义新的，则还原为null
            if(dataArea == null){
                areaDataProcess = null;
            }
        }

        //回设区域数据处理
        getDynAreaModel().setDataProcess(dynAreaCell.getDynAreaPK(),areaDataProcess);

        return bDirty;
    
	}
	
	@Override
	protected void adjustDynAreaFormat(DynAreaCell dynAreaCell, AreaPosition oldArea) {
		// TODO Auto-generated method stub		
	}
	
	@Override
    protected int getDataProcessType() {
		return IDataProcessType.PROCESS_SORT;
	}
}
