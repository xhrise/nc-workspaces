package nc.ui.eh.cw.h1102010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.cw.h1102005.ArapQuerymnyBVO;
import nc.vo.eh.cw.h1102005.ArapQuerymnyVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 说明：查款单(审批)
 * 
 * @author 张起源 时间：2008-5-28 10:24:48
 */
public class ClientCtrl extends AbstractCtrl {

	@Override
	public String getBillType() {
		return IBillType.eh_h1102005;
	}

	@Override
	public String[] getBillVoName() {
		return new String[] { PubBillVO.class.getName(),
				ArapQuerymnyVO.class.getName(), ArapQuerymnyBVO.class.getName() };
	}

	@Override
	public String getChildPkField() {
		return "pk_querymny_b";
	}

	@Override
	public String getPkField() {
		return "pk_querymny";
	}

	@Override
	public int[] getListButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPLButton();
	}

	@Override
	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}
}
