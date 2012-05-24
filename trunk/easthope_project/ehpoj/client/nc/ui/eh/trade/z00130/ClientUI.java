package nc.ui.eh.trade.z00130;

import nc.bs.eh.trade.z00130.ClientUICheckRuleGetter;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 功能说明：折扣类型
 * @author 王兵
 * 2008年4月15日16:11:07
 */
public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {
		 getBillCardPanel().getHeadItem("pk_corp").setValue(_getCorp().getPk_corp());
		 getBillCardPanel().setTailItem("coperatorid", _getOperator());
		 getBillCardPanel().setTailItem("dmakedate",_getDate());
	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
        getBillCardWrapper().getBillCardPanel().getBodyPanel().setShowThMark(false);
		
	}
    
    @Override
	public Object getUserObject() {
        // TODO Auto-generated method stub
        return new ClientUICheckRuleGetter();
    }
}

