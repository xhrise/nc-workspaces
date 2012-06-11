package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.util.Vector;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.IDataProcessType;
import com.ufsoft.iufo.fmtplugin.service.DataProcessSrv;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.table.AreaPosition;

public class DelDataProcessCmd extends AbsDataProcessCmd {

	protected boolean excuteImpl(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds) {
		return generateDeleteDPFormat(dynAreaCell);
	}
    private boolean generateDeleteDPFormat(DynAreaCell dynAreaCell){
    	return DataProcessSrv.delDynAreaDataProcess(getCellsModel(),dynAreaCell);
    }
//    /**
//     * if contains measure,key or fomula ,then return true.
//     * @param eachCol
//     * @return
//     */
//	private static boolean hasBusinessData(CellsModel cellsModel,String dynAreaPK, AreaPosition area) {
//		return MeasureModel.getInstance(cellsModel).getMeasureVOByArea(area).size() > 0 ||
//		       KeywordModel.getInstance(cellsModel).getKeyVOByArea(area).size() > 0 ||
//		       FormulaModel.getInstance(cellsModel).getRelatedDynFmlAreas(dynAreaPK,area,true).length > 0 ||
//		       FormulaModel.getInstance(cellsModel).getRelatedDynFmlAreas(dynAreaPK,area,false).length > 0;
//	}
    
    @Override
	protected void adjustDynAreaFormat(DynAreaCell dynAreaCell,
			AreaPosition oldArea) {
		// TODO Auto-generated method stub
	}
    
    @Override
    protected int getDataProcessType() {
		return IDataProcessType.PROCESS_DELETE;
	}
}
