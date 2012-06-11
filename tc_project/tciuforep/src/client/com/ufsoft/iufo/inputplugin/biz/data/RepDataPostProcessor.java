package com.ufsoft.iufo.inputplugin.biz.data;

import nc.ui.iufo.input.ExportDataUtil;
import nc.vo.iufo.data.MeasurePubDataVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.service.ReportFormatSrv;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.excel.IPostProcessor;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class RepDataPostProcessor implements IPostProcessor, IUfoContextKey {
	//��ǰ����ؼ�
	private UfoReport report=null;
	
	//ϵͳ������������ָ��ؼ����Ƿ���Ҫ��ʾΪ��������
	private boolean bConvertCode=true;
	
	//Ϊ�˱������ɲ���Ҫ��ReportFormatSrv�����õ���ԭ���ı���
	private ReportFormatSrv reportFormatSrv=null;
	private CellsModel oldCellsModel=null;
	
	
	public void setUfoReport(Object report){
		//�����µı���ؼ�ʱ�����õ���ReportFormatSrv�������
		if (report instanceof UfoReport==false){
			report=null;
			return;
		}
		this.report=(UfoReport)report;
		reportFormatSrv=null;
		oldCellsModel=null;
		
		//ȡ������ָ���Ƿ���ʾ�����ϵͳ����
		SysPropVO sysProp = SysPropMng.getSysProp(ISysProp.CODE_VALUE);
		if(sysProp != null){
			String code_value = sysProp.getValue();
			if (code_value!= null && code_value.equals("true")){
				bConvertCode =false;
			}
		}
	}
	
	public boolean isShowZero(){
		return false;
	}
	
	public Object postProcessValue(CellsModel cellsModel, int rowIndex, int colIndex, Object value, IufoFormat format) {
		//ֻ�ж�Ӧ�����ĳһ��������ݣ�����Ҫ������������
		if (report==null || report.getContextVo()==null || report.getContextVo() instanceof TableInputContextVO==false)
			return value;
		
		TableInputContextVO context=(TableInputContextVO)report.getContextVo();
		Object pubDataObj = context.getAttribute(MEASURE_PUB_DATA_VO);
		MeasurePubDataVO measurePubDataVo = pubDataObj != null && (pubDataObj instanceof MeasurePubDataVO) ? (MeasurePubDataVO)pubDataObj : null;
		Object repIdObj = context.getAttribute(REPORT_PK);
		String strRepPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null; 
		
		if (strRepPK==null || strRepPK.trim().length()<=0 || pubDataObj==null)
			return value;
		
		//���cellsModelû�з����仯�������õ�ԭ����ReportFormatSrv
		if (reportFormatSrv==null || cellsModel!=oldCellsModel){
			Object objTrans=context.getAttribute(context.TABLE_INPUT_TRANS_OBJ);
			context.removeAttribute(context.TABLE_INPUT_TRANS_OBJ);
			reportFormatSrv=new ReportFormatSrv(new UfoContextVO(context),cellsModel);
			if (objTrans!=null)
				context.setAttribute(context.TABLE_INPUT_TRANS_OBJ, objTrans);
			oldCellsModel=cellsModel;
		}
		
		try{
			return ExportDataUtil.processForExcel(reportFormatSrv,CellPosition.getInstance(rowIndex,colIndex),value,measurePubDataVo,bConvertCode);
		}catch(Exception e){
			AppDebug.debug(e);
			return value;
		}
	}
}
