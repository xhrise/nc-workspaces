package com.ufsoft.iuforeport.tableinput.applet;

import java.util.Map;

import nc.ui.iufo.input.InputActionUtil;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.biz.InputFilePlugIn;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.biz.file.SaveRepDataExt;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.ReportAuth;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IVerify.VerifyType;

/**
 * @author chxw
 *
 * ����¼�뱨��Ԫ������Ȩ�޹���
 */
public class TableInputAuth extends ReportAuth {
	/**����̬�»��ܽ���Ƿ������ֹ��޸�*/
    private static boolean s_bCanModifyTotalResult = false;
    public static void setCanModifyTotalResult(boolean bCanModifyTotalResult){
    	s_bCanModifyTotalResult = bCanModifyTotalResult;
    }
    private UfoReport report;
	public TableInputAuth(UfoReport report) {
		super(report.getTable().getCells(),report.getContext());
		this.report = report;
	}
	
	/**
	 * @see com.ufsoft.table.CellsAuthorization#isWritable(int, int)
	 */
	public boolean isWritable(int row, int col) {
		if(isCommit()){
			return false;
		}
    	
		//����ǻ�����������ϵͳ�������ܽ���Ƿ������ֹ��޸�
		if(!isCanModifyTotalResult()){
			return false;
		}
		
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
	 * ����ǻ�����������ϵͳ�������ܽ���Ƿ������ֹ��޸�
	 * @return
	 */
	private boolean isCanModifyTotalResult(){
		if(s_bCanModifyTotalResult && 
				GeneralQueryUtil.isGeneralQuery(getContext()) && 
				GeneralQueryUtil.isTotalDataVer(getContext())){
			return false;
		}
		return true;
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
		    Map hashMeas = measModel.getMainMeasureVOByArea(allArea);
		    if(hashMeas != null && hashMeas.size() > 0)
		        return false;
	    }
	    return true;
	}

	/**
	 * ��鱨�������Ƿ��ϱ�
	 */
	public boolean isCommit() {
		InputFilePlugIn plugIn = (InputFilePlugIn)report.getPluginManager().getPlugin(InputFilePlugIn.class.getName());
		if(plugIn != null){
			IExtension[] exts = plugIn.getDescriptor().getExtensions();
			SaveRepDataExt saveRepDataExt = (SaveRepDataExt)exts[0];
	    	return !saveRepDataExt.isEnabledSelf();
		}
		return false;
	}
	
}
