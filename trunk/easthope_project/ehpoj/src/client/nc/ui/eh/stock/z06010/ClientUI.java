package nc.ui.eh.stock.z06010;

import java.util.Observable;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.stock.z06005.SbbillVO;
import nc.vo.pub.AggregatedValueObject;

/**
 * 功能：司磅单 作者：newyear 日期：2008-4-11 下午04:36:39
 */

public class ClientUI extends AbstractClientUI {

	public ClientUI() {
		super();
		try {
			new AbstractSPEventHandler(this, this.getUIControl())
					.setBoEnabled();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ClientEventHandler.clientui = this;
	}

	public ClientUI(Boolean arg0) {
		super(arg0);
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	public ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		// return new AbstractSPEventHandler(this, this.getUIControl());
		return new ClientEventHandler(this, this.getUIControl());
	}
	
	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
		
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		if (modelVo != null) {
			SbbillVO sbvo = (SbbillVO) modelVo
					.getParentVO();
			String vapproveid = sbvo.getVapproveid();
			if (vapproveid == null)
				vapproveid = "";

			String pk_user = nc.ui.eh.stock.z06010.ClientUI.getCE().getUser()
					.getPrimaryKey();

			if ((sbvo.getVbillstatus() == 1 || sbvo.getVbillstatus() == 0)
					&& vapproveid.equals(pk_user)) {
				getButtonManager().getButton(IBillButton.CancelAudit)
						.setEnabled(true);
			} else {
				getButtonManager().getButton(IBillButton.CancelAudit)
						.setEnabled(false);
			}
		}

		this.updateButtonUI();
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
			SbbillVO sivo = (SbbillVO)modelVo.getParentVO();
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
