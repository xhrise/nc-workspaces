package nc.ui.eh.report.h0906005;

import nc.vo.eh.ipub.Iinvtype;

/**
 * 功能:猪料销售品种结构变动(集团) </br>
 * 报表节点编码：H090600204
 * 作者:XMQ 时间:2009年09月28日09:22:17
 */
public class ReportUI extends nc.ui.eh.report.h0906004.ReportUI{
	@Override
	protected String getNodeCode() {
		// TODO Auto-generated method stub
		return "H090600204";
	}
	@Override
	protected int getBBType() {
		// TODO Auto-generated method stub
		return Iinvtype.ZLINT;
	}
}
