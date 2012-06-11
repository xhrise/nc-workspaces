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
 * 数据录入报表单元的数据权限管理。
 */
public class TableInputAuth extends ReportAuth {
	/**数据态下汇总结果是否允许手工修改*/
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
    	
		//如果是汇总数据则检查系统参数汇总结果是否允许手工修改
		if(!isCanModifyTotalResult()){
			return false;
		}
		
		//是否是可直接编辑的报表
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
	 * 如果是汇总数据则检查系统参数汇总结果是否允许手工修改
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
	 * 是否是可直接编辑的报表
	 */
	public static boolean isRepCanModifyData(CellsModel cellsModel){
		DynAreaModel dynModel = DynAreaModel.getInstance(cellsModel);
	    DynAreaVO[] dynAreas = dynModel.getDynAreaVOs();
	     
	    //如果没有动态区，则可以录入
	    if (dynAreas == null || dynAreas.length <= 0)
	        return true;
	    
	    //检查每一个动态区
	    for(DynAreaVO dynArea : dynAreas){
	    	//如果动态区含有数据处理，不可录入
	        if (InputActionUtil.isDynProcessedAndNotCanModifyData(dynModel, dynArea.getDynamicAreaPK()))
	        	return false;
		     
		    //判断动态区所在的行是否定义了固定区指标
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
	 * 检查报表数据是否上报
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
