package nc.ui.eh.report.h0906012;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.ipub.Iinvtype;
import nc.vo.pub.SuperVO;
import nc.vo.pub.query.ConditionVO;

/**
 * 片区增减对比报表(集团)
 * @author XMQ 2009-10-09
 */
public class ReportUI extends AbstractReportUI {

    @Override
    protected String getNodeCode() {
	// TODO Auto-generated method stub
	return "H090600209";
    }

    @Override
    protected SuperVO[] getVos(ConditionVO[] conditionVOs) {
	// TODO Auto-generated method stub
	String startDate = conditionVOs[0].getValue();
	String endDate = conditionVOs[1].getValue();
	PubItf itf = (PubItf)NCLocator.getInstance().lookup(PubItf.class);
	SuperVO[] vos = itf.calcJTQYzjb(new String[]{startDate,endDate}, Iinvtype.PQZJDB);
	return vos;
    }
    
    @Override
    protected void setQueryDlgValue(QueryConditionClient uidialog) {
        // TODO Auto-generated method stub
        super.setQueryDlgValue(uidialog);
	uidialog.setDefaultValue("startdate",getCe().getDate().toString(), "");
	uidialog.setDefaultValue("enddate", getCe().getDate().toString(), ""); 
    }
    
    @Override
    protected void dealReport(ReportBaseClass report) {
        // TODO Auto-generated method stub
        super.dealReport(report);
        report.getHeadItem("calcdate").setValue(getCe().getDate().toString());
        report.getHeadItem("calcpsn").setValue(getCe().getUser().getUserName());
    }
}
