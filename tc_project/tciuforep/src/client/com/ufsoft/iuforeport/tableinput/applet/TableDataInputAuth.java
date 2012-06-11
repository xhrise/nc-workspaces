package com.ufsoft.iuforeport.tableinput.applet;

import java.util.Map;

import nc.ui.iufo.input.InputActionUtil;
import nc.vo.iufo.measure.MeasureVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iuforeport.tableinput.TableInputOperUtil;
import com.ufsoft.report.ReportAuth;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IVerify.VerifyType;

public class TableDataInputAuth extends ReportAuth {
	public TableDataInputAuth(ReportDesigner editor) {
		super(editor.getCellsPane(),editor.getContext());
	}
	
	/**
	 * @see com.ufsoft.table.CellsAuthorization#isWritable(int, int)
	 */
	public boolean isWritable(int row, int col) {
		if(((Integer)getContext().getAttribute(IUfoContextKey.DATA_RIGHT))!=IUfoContextKey.RIGHT_DATA_WRITE)
			return false;
		
		//�Ƿ��ǿ�ֱ�ӱ༭�ı���
		if(!isRepCanModifyData(getCellsPane().getDataModel())){
			return false;
		}
		
		Cell cell = getCellsPane().getCell(row,col);
		if(cell != null && getCellsPane().getDataModel().isVerify(
				CellPosition.getInstance(row,col), VerifyType.WRITABLE_AT_INPUTSTAT,false)){
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * �Ƿ��ǿ�ֱ�ӱ༭�ı���
	 */
	public static boolean isRepCanModifyData(CellsModel cellsModel){
		DynAreaModel dynModel = DynAreaModel.getInstance(cellsModel);
	    DynAreaVO[] dynAreas = dynModel.getDynAreaVOs();
	     
	    //���û�ж�̬���������¼��
	    if (dynAreas == null || dynAreas.length <= 0)
	        return true;
	    
	    //���ÿһ����̬��
	    for(DynAreaVO dynArea : dynAreas){
	    	//�����̬���������ݴ�������¼��
	        if (InputActionUtil.isDynProcessedAndNotCanModifyData(dynModel, dynArea.getDynamicAreaPK()))
	        	return false;
		     
		    //�ж϶�̬�����ڵ����Ƿ����˹̶���ָ��
		    AreaPosition area = dynArea.getOriArea();
		    AreaPosition allArea=null;
		    if(dynArea.getDirection()==DynAreaVO.DIRECTION_ROW){
		    	 allArea= AreaPosition.getInstance(area.getStart().getRow(),0, cellsModel.getColNum(),1);
		    }else{
		    	 allArea= AreaPosition.getInstance(0,area.getStart().getColumn(), 1,cellsModel.getRowNum());
		    }
		   
		    MeasureModel measModel = MeasureModel.getInstance(cellsModel);
		    Map<CellPosition, MeasureVO> hashMeas = measModel.getMainMeasureVOByArea(allArea);
		    if(hashMeas != null && hashMeas.size() > 0)
		        return false;
	    }
	    return true;
	}
}

