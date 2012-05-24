
package nc.ui.eh.stock.h0150205;

import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.h0150205.StockRwcarriageVO;

/**
 * 
功能：铁路运费
作者：wb
日期：2008-12-12 上午09:55:54
 */

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	@Override
	public void onBoSave() throws Exception {
		 ClientEnvironment ce = ClientEnvironment.getInstance();
         getBillCardPanelWrapper().getBillCardPanel().setTailItem("editcoperid", ce.getUser().getPrimaryKey());
         getBillCardPanelWrapper().getBillCardPanel().setTailItem("editdate", _getDate());
		 //非空的有效性判断
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         StockRwcarriageVO hvo = (StockRwcarriageVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
         BusinessDelegator db = new BusinessDelegator();
         String sql = " pk_invbasdoc = '"+hvo.getPk_invbasdoc()+"' and pk_areacl = '"+hvo.getPk_areacl()+"' and pk_corp = '"+_getCorp().getPk_corp()+"' and pk_rwcarriage <> '"+hvo.getPk_rwcarriage()+"'";
         StockRwcarriageVO[] vos = (StockRwcarriageVO[])db.queryByCondition(StockRwcarriageVO.class, sql);
         if(vos!=null&&vos.length>0){
     		getBillUI().showErrorMessage("在运费设置中已有此物料此地区的设置,不可以再增加,请修改!");
     		return;
     	}
         super.onBoSave();
	 }
	 
	@Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit2();
    }
}
