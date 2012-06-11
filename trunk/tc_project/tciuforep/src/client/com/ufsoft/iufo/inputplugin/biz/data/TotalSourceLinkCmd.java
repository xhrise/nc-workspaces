package com.ufsoft.iufo.inputplugin.biz.data;

import javax.swing.JOptionPane;

import nc.ui.iufo.input.control.RepDataControler;
import nc.vo.iufo.datasource.DataSourceVO;

import com.ufida.dataset.IContext;
import com.ufida.zior.view.Editor;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.measure.MeasKeyValFmt;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class TotalSourceLinkCmd extends AbsIufoBizCmd implements IUfoContextKey {
	public TotalSourceLinkCmd(UfoReport ufoReport){
		super(ufoReport);
	}
	
	protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
		if (params==null || params.length<1 || params[0]==null){
            String strAlert = MultiLangInput.getString("miufotableinput0004");//请选择一个单元格
            JOptionPane.showMessageDialog(ufoReport,strAlert);
            return;
		}
	//	doTraceTotalSource(false, getUfoReport().getContextVo(),ufoReport.getCellsModel(),ufoReport,(CellPosition)params[0]);
	}
	
	public static void doTraceTotalSource(boolean bZior,IContext inputContextVO,CellsModel cellsModel,Editor container,CellPosition cellPos){
		Cell cell=cellsModel.getCell(cellPos);
		if (cell==null || cell.getExtFmt(MeasKeyValFmt.EXT_FMT_MEASKEYVAL)==null){
			JOptionPane.showMessageDialog(container,MultiLangInput.getString("miufotableinput0012"));
			return;
		}
		
		MeasKeyValFmt measKeyVal=(MeasKeyValFmt)cell.getExtFmt(MeasKeyValFmt.EXT_FMT_MEASKEYVAL);
		MeasTraceInfo traceInfo=new MeasTraceInfo(measKeyVal.getMeasurePK(),measKeyVal.getKeyVals(),measKeyVal.getReportPK(),measKeyVal.getAloneID(),false);
		
		if(inputContextVO!=null){
			Object showRepTreeObj = inputContextVO.getAttribute(SHOW_REP_TREE);
			boolean isShowRepTree = showRepTreeObj == null ? false : Boolean.parseBoolean(showRepTreeObj.toString());
			
			Object genralQueryObj = inputContextVO.getAttribute(GENRAL_QUERY);
			boolean isgenralQuery = genralQueryObj == null ? false : Boolean.parseBoolean(genralQueryObj.toString());
						
			traceInfo.setGeneryQuery(isgenralQuery);
			traceInfo.setShowRepTree(isShowRepTree);
			traceInfo.setStrLoginUnitID(RepDataControler.getInstance(container.getMainboard()).getCurUserInfo(container.getMainboard()).getUnitId());
			traceInfo.setStrOrgPK((String)inputContextVO.getAttribute(ORG_PK));
			
			String strOperUserId = (String)inputContextVO.getAttribute(CUR_USER_ID);
			traceInfo.setStrOperUserPK(strOperUserId);
			String taskId=(String)inputContextVO.getAttribute(TASK_PK);
			String langCode=(String)inputContextVO.getAttribute(CURRENT_LANG);
			traceInfo.setStrLangCode(langCode);
			traceInfo.setStrTaskId(taskId);
			if(inputContextVO.getAttribute(DATA_SOURCE) instanceof DataSourceVO){
				 traceInfo.setDataSource((DataSourceVO)inputContextVO.getAttribute(DATA_SOURCE));
			} else if(inputContextVO.getAttribute(DATA_SOURCE) instanceof DataSourceInfo){
				 traceInfo.setDataSource((DataSourceInfo)inputContextVO.getAttribute(DATA_SOURCE));
			}
		}
		
		if (bZior)
			WindowNavUtil.traceZiorMeasure(container,traceInfo,false);
		else
			WindowNavUtil.traceMeasure(traceInfo);
	}
	
	
    protected  boolean isNeedCheckAloneID(){
        return false;        
    }
    protected boolean isNeedCheckReportPK(){
        return false;
    }
}
