package nc.ui.eh.cw.h1101010;

import java.util.Observable;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.cw.h1101005.ArapFkVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能说明：付款单审批
 * 
 * @author 王明 2008-05-28 下午02:03:18
 */
public class ClientUI extends AbstractClientUI {
	public ClientUI() {
		super();
		ClientEventHandler.clientui = this;
	}
	
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
		getBillCardPanel().getHeadItem("vbillstatus").setValue(
				Integer.toString(IBillStatus.FREE));
		super.setDefaultData();
		
	}


	@Override
	protected void initSelfData() {
		// 审批流
		getBillCardWrapper().initHeadComboBox("vbillstatus",
				IBillStatus.strStateRemark, true);
		getBillListWrapper().initHeadComboBox("vbillstatus",
				IBillStatus.strStateRemark, true);
		super.initSelfData();
		
		
	}
	
	public static ClientEnvironment getCE() {
		
		ClientEnvironment ceVO = ClientEnvironment.getInstance();
		
		return ceVO;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		super.update(o, arg);
		
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		if(modelVo != null) {
			ArapFkVO sivo = (ArapFkVO)modelVo.getParentVO();
			String vapproveid = sivo.getVapproveid();
	    	if(vapproveid == null)
	    		vapproveid = "";
	    	
	    	String pk_user = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	    	
	    	if((sivo.getVbillstatus() == 1 || sivo.getVbillstatus() == 0) && vapproveid.equals(pk_user)) {
	    		getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(true);
	    	} else {
	    		getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(false);
	    	}
		}
		
		try {
			this.updateButtonUI();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
}
