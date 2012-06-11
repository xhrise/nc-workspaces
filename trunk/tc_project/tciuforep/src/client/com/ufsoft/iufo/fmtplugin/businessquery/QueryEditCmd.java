package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.CellsModel;

public class QueryEditCmd extends UfoCommand {

	public void execute(Object[] params) {
		UfoReport report = (UfoReport) params[0];
		String strGUID = (String) params[1];
		CellsModel cellsModel = report.getCellsModel();
        ReportBusinessQuery reportBusinessQuery = ReportBusinessQuery.getInstance(cellsModel);
       
        ReportQueryVO reportQueryVO = null;
        if(strGUID != null){
            //�õ�Ҫ�޸ĵı����ѯ(�ڲ�ѯ�༭�����ֻ���ǡ�ȷ����������޸ĸö���)
            reportQueryVO = ReportQueryUtil.cloneReportQueryVO(reportBusinessQuery.getReportQuery(strGUID),
                reportBusinessQuery);
        }
        if(reportQueryVO == null){
            reportQueryVO = new ReportQueryVO(reportBusinessQuery);
        }

        BQueryPropertyDlg dlg = new BQueryPropertyDlg(report, cellsModel, report.getContextVo(), reportQueryVO);
        dlg.setLocationRelativeTo(report);
        dlg.setModal(true);
        dlg.show();
        if(dlg.getResult() == UfoDialog.ID_OK){
            ReportQueryVO newReportQueryVO = dlg.getReportQueryVO();
            if(reportQueryVO != null){
                //���µ������ѯ���������
                reportBusinessQuery.createReportQuery(newReportQueryVO);

                //��������
                cellsModel.setDirty(true);
            }

        }
	}

}
