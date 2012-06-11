package com.ufsoft.iufo.fmtplugin.measure;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import nc.vo.iufo.measure.MeasureVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.format.TableConstant;
/**
 * 指标管理
 */
public class MeasureMgtCmd extends UfoCommand {

	private UfoReport m_report;

	
	/*
	 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {
		m_report = (UfoReport) params[0];
	   

		Hashtable list = getMeasureModel().getMeasureVOPosByAll();
	    Vector oldMeasureVectors = new Vector(list.size());	   
	    Hashtable oldMeasureList = new Hashtable();//key: MeasureVO value:CellPosition  	    
	    Enumeration enumeration = list.keys();
	    CellPosition pos = null;
	    MeasurePosVO posVO = null;
	    MeasureVO mvo = null;	    
	    while(enumeration.hasMoreElements()){
	        pos = (CellPosition) enumeration.nextElement();
	        mvo = (MeasureVO) list.get(pos);
	        posVO = new MeasurePosVO();
	        posVO.setActPos(pos.toString());
	        posVO.setMeasureVO(mvo);
	        oldMeasureVectors.add(posVO);	       
	        oldMeasureList.put(mvo, pos);
	    }
	  
	   
	    
	    MeasureMgtTableModel model = new MeasureMgtTableModel(m_report.getCellsModel(),oldMeasureVectors, isDataProcessed());
	    MeasureMgtDialog dialog = new MeasureMgtDialog(m_report, model);
		dialog.setVisible(true);
		//保存数据.
		if(dialog.getResult() == UfoDialog.ID_OK){
		    //修改CellsModel中的指标数据.并修改单元属性使其与指标数据类型一致.
		    Vector vecMeasureUpdate = dialog.getMeasureMgtTableModel().getUpdatedMeasures();

		    String oldPos = null;
		    String newPos = null;
		    for (Iterator iterUpdate = vecMeasureUpdate.iterator();iterUpdate.hasNext();) {
		        MeasurePosVO mtvoUpdate = (MeasurePosVO)iterUpdate.next();
		        newPos = mtvoUpdate.getActPos();
		        oldPos = oldMeasureList.get(mtvoUpdate.getMeasureVO()).toString();
		        if(!newPos.equals(oldPos)){
		        	DynAreaModel dynAreaModel = DynAreaModel.getInstance(getCellsModel());
		        	DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(CellPosition.getInstance(newPos));
		        	MeasureVO measureVO = getMeasureModel().getMeasureVOByPos(CellPosition.getInstance(oldPos));
		        	if(dynAreaCell != null){
		        		getMeasureModel().setDynAreaMeasureVO(dynAreaCell.getDynAreaVO().getDynamicAreaPK(),
		        				CellPosition.getInstance(newPos),measureVO);
		        	}else{
		        		getMeasureModel().setMainMeasureVO(CellPosition.getInstance(newPos),measureVO);
		        	}
		        	getMeasureModel().removeMeasureVOByPos(CellPosition.getInstance(oldPos));
		        }
		        modifyFormat(newPos,mtvoUpdate.getMeasureVO());
            }
		}
	}

	
private void modifyFormat(String newPos, MeasureVO measureVO) {
		int cellType = TableConstant.CELLTYPE_STRING;
		if (measureVO.getType() == MeasureVO.TYPE_NUMBER) {
			cellType = TableConstant.CELLTYPE_NUMBER;
		}
		IufoFormat format = (IufoFormat) getCellsModel()
				.getCellFormatIfNullNew(CellPosition.getInstance(newPos));
		format.setCellType(cellType);
	}


	//	//key: MeasureVO, value: CellPosition
//	private Hashtable createNewMeasurePosMap(Vector vector){	  
//	    Hashtable result = new Hashtable(vector.size());	  
//	    
//	    CellPosition pos = null;
//	    MeasurePosVO vo = null;
//	    MeasureVO mvo = null;
//	    int i = 0;
//	    for(Iterator iterOld = vector.iterator();iterOld.hasNext();){
//            MeasurePosVO mtvoOld = (MeasurePosVO)iterOld.next();
//            result.put(mtvoOld.getMeasureVO(), mtvoOld.getActPos());         
//        }
//	   return result;
//	}

    private MeasureModel getMeasureModel(){   	
    	return MeasureModel.getInstance(getCellsModel());	
    }
    
    /**
     * 报表是否已经经过数据处理
     * @return
     */
    private boolean isDataProcessed(){
        boolean result = false;
        try {
        	DynAreaModel dynAreaModel = DynAreaModel.getInstance(getCellsModel());
//            DynAreaDefPlugIn plug =  (DynAreaDefPlugIn) m_report.getPluginManager().getPlugin(DynAreaDefPlugIn.class.getName());
            DynAreaCell[] dyns = dynAreaModel.getDynAreaCells();
            if(dyns != null){
                for(int i = 0; i < dyns.length; i++){
                    if(dyns[i].getDynAreaVO().isProcessed()){
                        result = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
           
        }
    	return result;	
    }
    private CellsModel getCellsModel(){
    	return m_report.getCellsModel();
    }
}