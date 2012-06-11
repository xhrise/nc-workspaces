package com.ufsoft.iufo.fmtplugin.businessquery;

import java.util.Vector;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;

public class QueryMngCmd extends UfoCommand {

	public void execute(Object[] params) {
        //�õ��������Ѷ���ı����ѯ
		UfoReport report = (UfoReport) params[0];
		CellsModel cellsModel = report.getCellsModel();
        ReportBusinessQuery businessQuery = ReportBusinessQuery.getInstance(cellsModel);
        Vector vecAllRQ = null;
        if(businessQuery != null){
            vecAllRQ = businessQuery.getAllReportQuery();
        }
        if(vecAllRQ == null || vecAllRQ.size() <= 0){
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001741"),report);  //"�ñ���û�б����ѯ��"
            return;
        }
        //�����ѯ���е��б����
        ReportQueryMngDlg dlg = new ReportQueryMngDlg(report, vecAllRQ);
        dlg.setModal(true);
        dlg.show();
        int nDlgResult = dlg.getResult();
        if(nDlgResult == ReportQueryMngDlg.ID_UPDATE){ //�õ�ѡ���޸ĵı����ѯ��GUID
            String[] strGUIDs = dlg.getSelectGUIDs();
            if(strGUIDs[0] != null){
                //��֤���õĲ�ѯ���������ѯ�����Ƿ񻹴���
                ReportQueryVO repQueryVO = businessQuery.getReportQuery(strGUIDs[0]);
                nc.vo.pub.core.ObjectNode objNode = ReportQueryUtil.getQueryEngineObjectNode(strGUIDs[0],
                    repQueryVO.getQEDSName());
                if(objNode != null){
                    //���ñ༭�����ѯ��˽�з���
                    new QueryEditCmd().execute(new Object[]{report,strGUIDs[0]});
                } else{
                    UfoPublic.sendWarningMessage(repQueryVO.getQuerydef().getDisplayName() + StringResource.getStringResource("miufo1001742"),report);  //"��Ӧ��ҵ���ѯ�еĲ�ѯ�����ѱ�ɾ�����ñ����ѯҲ����ɾ����"
                    businessQuery.removeReportQueryVO(strGUIDs[0]);
                    cellsModel.setDirty(true);
                }
            }
        } else if(nDlgResult == ReportQueryMngDlg.ID_DELETE){
            String[] strGUIDs = dlg.getSelectGUIDs();
            int iLen = strGUIDs != null ? strGUIDs.length : 0;
                       for(int i = 0; i < iLen; i++){
                //ɾ��
                businessQuery.removeReportQueryVO(strGUIDs[i]);
                //��������
                cellsModel.setDirty(true);
            }
        }
	}

}
