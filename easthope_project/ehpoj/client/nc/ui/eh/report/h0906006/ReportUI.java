package nc.ui.eh.report.h0906006;


import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.eh.ipub.Iinvtype;
import nc.vo.eh.report.h0906004.JTXspzjgbdVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;


/**
 * 功能:禽料销售品种结构变动(集团) </br>
 * 报表节点编码：H090600205
 * 作者:XMQ 时间:2009年09月28日09:22:17
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
