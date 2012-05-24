package nc.ui.eh.iso.z0502010;

import java.util.Observable;

import nc.ui.eh.iso.z0502005.ClientBaseBD;
import nc.ui.eh.pub.ICombobox;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.eh.iso.z0502005.StockCheckreportVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 说明：检测报告单（审批）
 * 
 * @author 张起源 时间：2008-4-11
 */
public class ClientUI extends MultiChildBillManageUI {
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
		return new ClientEventHandler(this, this.getUIControl()); // AbstractSPEventHandler
	}

	@Override
	protected void initSelfData() {
		// 表头的抽样单类型下拉菜单
		getBillCardWrapper().initHeadComboBox("resulst", ICombobox.STR_RESULE,
				true);
		getBillListWrapper().initHeadComboBox("resulst", ICombobox.STR_RESULE,
				true);
		// 表体的检测结果类型下拉菜单
		getBillCardWrapper().initBodyComboBox("result",
				ICombobox.STR_PASS_FLAG, true);
		getBillListWrapper().initBodyComboBox("result",
				ICombobox.STR_PASS_FLAG, true);
		// 表体的是扣重扣价下拉
		getBillCardWrapper()
				.initBodyComboBox("iskzkj", ICombobox.CW_KZKJ, true);
		getBillListWrapper()
				.initBodyComboBox("iskzkj", ICombobox.CW_KZKJ, true);
		// 表体的分组下拉
		getBillCardWrapper().initBodyComboBox("groupitem", ICombobox.CW_GROUP,
				true);
		getBillListWrapper().initBodyComboBox("groupitem", ICombobox.CW_GROUP,
				true);
		// 表体的是否最高下拉
		getBillCardWrapper()
				.initBodyComboBox("ishigh", ICombobox.CW_HIGH, true);
		getBillListWrapper()
				.initBodyComboBox("ishigh", ICombobox.CW_HIGH, true);
	}

	@Override 
	public void setDefaultData() throws Exception {
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
 		if(modelVo != null) {
 			StockCheckreportVO scbvo = (StockCheckreportVO)modelVo.getParentVO();
 		   	String vapproveid = scbvo.getVapproveid();
 	    	if(vapproveid == null)
 	    		vapproveid = "";
 	    	
 	    	String pk_user = nc.ui.eh.iso.z0502010.ClientUI.getCE().getUser().getPrimaryKey();
 	    	
 	    	if((scbvo.getVbillstatus() == 1 || scbvo.getVbillstatus() == 0) && vapproveid.equals(pk_user)) {
 	    		getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(true);
 	    	} else {
 	    		getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(false);
 	    	}
 		}
 		
 		this.updateButtonUI();
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBaseBD();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception {
		// TODO Auto-generated method stub

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
			StockCheckreportVO sivo = (StockCheckreportVO)modelVo.getParentVO();
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