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
        //����
        Hashtable<String, Integer> hashDataTypes = DataProcessUtil.getMeasTypes(getMeasureModel(),dynAreaCell, vecAllDynAreaDPFlds);
        boolean bDirty = DataProcessSetter.generateDefAndFormat(dynAreaCell, IDataProcessType.PROCESS_SORT,
            areaDataProcess, vecAllDynAreaDPFlds, hashDataTypes, getReport());

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
	protected void adjustDynAreaFormat(DynAreaCell dynAreaCell, AreaPosition oldArea) {
		// TODO Auto-generated method stub		
	}
	
	@Override
    protected int getDataProcessType() {
		return IDataProcessType.PROCESS_SORT;
	}
}
