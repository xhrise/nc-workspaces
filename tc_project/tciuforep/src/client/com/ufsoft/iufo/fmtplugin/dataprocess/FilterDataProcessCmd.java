package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.util.Vector;

import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.BaseDataArea;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFilterData;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessSetter;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.IDataProcessType;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.service.DataProcessSrv;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.table.AreaPosition;

public class FilterDataProcessCmd extends AbsDataProcessCmd implements IUfoContextKey {

	protected boolean excuteImpl(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds) {
		//modify by ljhua 2006-9-22 �˴�Ӧ�ô��붯̬����������Ĺؼ��֣����ɸѡ������������
		KeyVO[] dynAreaKeyVOs = getKeywordModel().getDynKeyVOsContainsMainTable(dynAreaCell.getDynAreaPK());
         return generateFilterDef(dynAreaCell, vecAllDynAreaDPFlds, dynAreaKeyVOs);
	}

	private boolean generateFilterDef(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds, KeyVO[] dynAreaKeyVOs) {
	        //׼�����������ݴ���������Ա : ���ݴ�����,���ݴ�������
	        AreaDataProcess areaDataProcess = getDynAreaModel().getDataProcess(dynAreaCell.getDynAreaPK());
	        boolean bHasAreaDataProcess = true;
	        if(areaDataProcess == null){
	            //���ڴ����������ݶ����Լ�ϸ������Χ��Ϣ��
	            areaDataProcess = new AreaDataProcess();
	            bHasAreaDataProcess = false;
	        }
	        if(!areaDataProcess.isUserDefined()){
	            //ˢ���������ݴ�����������п������������ɸѡ�ȶ��壩
	            AreaPosition dynRelateArea = DataProcessUtil.setAreaRange(dynAreaCell.getArea(), DataProcessUtil.STRHASPOS_ABSOLUTE);
	            areaDataProcess.setDynRelateArea(dynRelateArea);            
	        }
	        //�������ݴ����壬�����ݴ��������ʽ
	        //ɸѡ
	        Vector<DataProcessFilterData> vecDPFilterData = new Vector<DataProcessFilterData>();
	        Vector<DataProcessFld> vecDPFld = new Vector<DataProcessFld>();
	        Object repIdObj = getContextVO().getAttribute(REPORT_PK);
			String strRepPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null;
			
//	        String strRepPK = getContextVO().getContextId();
	        boolean bOnServer = false;
	        DataProcessUtil.calFilterDatas(dynAreaCell.getDynAreaPK(),new DataProcessSrv(getContextVO(),getCellsModel(),true), vecAllDynAreaDPFlds, strRepPK, bOnServer, vecDPFilterData, vecDPFld);

	        boolean bDirty = DataProcessSetter.getFilterDefAndFormat(getReport(), vecDPFilterData, vecDPFld, areaDataProcess,
	            dynAreaKeyVOs,dynAreaCell);

	        BaseDataArea dataArea = areaDataProcess.getTableArea();
	        if(!bHasAreaDataProcess){
	            //û�оɵ��������ݴ��������û�û�ж����µģ���ԭΪnull
	            if(dataArea == null){
	                areaDataProcess = null;
	            }
	        }

	        //�����������ݴ���
	        getDynAreaModel().setDataProcess(dynAreaCell.getDynAreaPK(),areaDataProcess);

	        return bDirty;
	    
	}
	
	@Override
	protected void adjustDynAreaFormat(DynAreaCell dynAreaCell,
			AreaPosition oldArea) {
		// TODO Auto-generated method stub
	}

	@Override
	protected int getDataProcessType() {
		return IDataProcessType.PROCESS_FILTER;
	}

}
