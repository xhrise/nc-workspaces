package nc.ui.ehpta.hq010201;

import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ehpta.hq010201.StorContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author author
 * @version tempProject version
 */

public class EventHandler extends ManageEventHandler {

	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	@Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		
	}

	protected void onBoElse(int intBtn) throws Exception {
		
		switch(intBtn) {
			case DefaultBillButton.DISABLED :
				setDisabled(true);
				break;
			case DefaultBillButton.ENABLED : 
				setDisabled(false);
				break;
				
			
		}
		
	}
	
	private void setDisabled(Boolean bool) throws Exception {
		StorContractVO contractVO = (StorContractVO) getBufferData().getCurrentVO().getParentVO();
		
		if(contractVO != null && contractVO.getAttributeValue("pk_storagedoc") != null) {
			if(bool) {
				contractVO.setStopdate(_getDate());
				contractVO.setTy_flag(new UFBoolean(bool));
			} else {
				contractVO.setStopdate(null);
				contractVO.setTy_flag(new UFBoolean(bool));
			}
				
			HYPubBO_Client.update(contractVO);
			
		}
		
		Integer currRow = getBufferData().getCurrentRow();
		updateBuffer();
		getBufferData().setCurrentRow(currRow);
		
		getBillUI().updateButtons();
	}

}