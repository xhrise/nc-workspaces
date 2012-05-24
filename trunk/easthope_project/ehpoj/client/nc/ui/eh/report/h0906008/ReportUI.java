package nc.ui.eh.report.h0906008;


import nc.vo.eh.ipub.Iinvtype;

 

/**
 * 功能:其他销售品种结构变动(集团) </br>
 * 报表节点编码：H090600207
 * 作者:XMQ 时间:2009年09月28日09:22:17
 */
public class ReportUI extends nc.ui.eh.report.h0906004.ReportUI{
	@Override
	protected String getNodeCode() {
		// TODO Auto-generated method stub
		return "H090600207";
	}
	@Override
	protected int getBBType() {
		// TODO Auto-generated method stub
		return Iinvtype.QTINT;
	}
}
