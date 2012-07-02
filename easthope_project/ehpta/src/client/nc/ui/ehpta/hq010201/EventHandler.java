package nc.ui.ehpta.hq010201;

import nc.ui.ehpta.pub.btn.DisabledBtn;
import nc.ui.ehpta.pub.btn.EnabledBtn;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ehpta.hq010201.StorContractVO;
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

	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == DisabledBtn.NO) {
			setDisabled(true);
		} else if(intBtn == EnabledBtn.NO) {
			setDisabled(false);
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
		
		updateBuffer();
		
	}

}