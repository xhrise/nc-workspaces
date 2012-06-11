package com.ufsoft.iufo.fmtplugin.businessquery;

import java.util.Vector;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;

public class QueryMngCmd extends UfoCommand {

	public void execute(Object[] params) {
        //得到报表里已定义的报表查询
		UfoReport report = (UfoReport) params[0];
		CellsModel cellsModel = report.getCellsModel();
        ReportBusinessQuery businessQuery = ReportBusinessQuery.getInstance(cellsModel);
        Vector vecAllRQ = null;
        if(businessQuery != null){
            vecAllRQ = businessQuery.getAllReportQuery();
        }
        if(vecAllRQ == null || vecAllRQ.size() <= 0){
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001741"),report);  //"该报表没有报表查询！"
            return;
        }
        //报表查询已有的列表界面
        ReportQueryMngDlg dlg = new ReportQueryMngDlg(report, vecAllRQ);
        dlg.setModal(true);
        dlg.show();
        int nDlgResult = dlg.getResult();
        if(nDlgResult == ReportQueryMngDlg.ID_UPDATE){ //得到选中修改的报表查询的GUID
            String[] strGUIDs = dlg.getSelectGUIDs();
            if(strGUIDs[0] != null){
                //验证引用的查询引擎基础查询定义是否还存在
                ReportQueryVO repQueryVO = businessQuery.getReportQuery(strGUIDs[0]);
                nc.vo.pub.core.ObjectNode objNode = ReportQueryUtil.getQueryEngineObjectNode(strGUIDs[0],
                    repQueryVO.getQEDSName());
                if(objNode != null){
                    //调用编辑报表查询的私有方法
                    new QueryEditCmd().execute(new Object[]{report,strGUIDs[0]});
                } else{
                    UfoPublic.sendWarningMessage(repQueryVO.getQuerydef().getDisplayName() + StringResource.getStringResource("miufo1001742"),report);  //"对应的业务查询中的查询定义已被删除，该报表查询也将被删除！"
                    businessQuery.removeReportQueryVO(strGUIDs[0]);
                    cellsModel.setDirty(true);
                }
            }
        } else if(nDlgResult == ReportQueryMngDlg.ID_DELETE){
            String[] strGUIDs = dlg.getSelectGUIDs();
            int iLen = strGUIDs != null ? strGUIDs.length : 0;
                       for(int i = 0; i < iLen; i++){
                //删除
                businessQuery.removeReportQueryVO(strGUIDs[i]);
                //设置脏标记
                cellsModel.setDirty(true);
            }
        }
	}

}
