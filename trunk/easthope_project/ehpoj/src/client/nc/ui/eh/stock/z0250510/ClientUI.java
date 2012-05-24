package nc.ui.eh.stock.z0250510;


import java.util.Observable;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.stock.z0250505.StockInVO;
import nc.vo.pub.AggregatedValueObject;



/**
 * 功能说明：入库单
 * @author 王明
 * 2008-03-24 下午04:03:18
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
	protected void initSelfData() {
		super.initSelfData();
	}
	
	@Override
	public void setDefaultData() throws Exception {		
//		super.setDefaultData();
		
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
			StockInVO sivo = (StockInVO)modelVo.getParentVO();
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
