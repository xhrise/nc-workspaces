package nc.ui.eh.report.h0906006;


import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.eh.ipub.Iinvtype;
import nc.vo.eh.report.h0906004.JTXspzjgbdVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;


/**
 * ����:��������Ʒ�ֽṹ�䶯(����) </br>
 * ����ڵ���룺H090600205
 * ����:XMQ ʱ��:2009��09��28��09:22:17
 */
public class ReportUI extends nc.ui.eh.report.h0906004.ReportUI{
	@Override
	protected String getNodeCode() {
		// TODO Auto-generated method stub
		return "H090600205";
	}
	@Override
	protected int getBBType() {
		// TODO Auto-generated method stub
		return Iinvtype.QLINT;
	}
}
