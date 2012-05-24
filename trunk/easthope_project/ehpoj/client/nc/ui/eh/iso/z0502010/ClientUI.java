
package nc.ui.eh.iso.z0502010;

import nc.ui.eh.iso.z0502005.ClientBaseBD;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 说明：检测报告单（审批）
 * @author 张起源
 * 时间：2008-4-11 
 */
public class ClientUI extends MultiChildBillManageUI {
	public ClientUI() {
	     super();
	 }
   
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	
	@Override
	public ManageEventHandler createEventHandler() {
		return new AbstractSPEventHandler(this,this.getUIControl());
	}

	@Override
	protected void initSelfData() {
		//表头的抽样单类型下拉菜单
		getBillCardWrapper().initHeadComboBox("resulst", ICombobox.STR_RESULE,true);
		getBillListWrapper().initHeadComboBox("resulst", ICombobox.STR_RESULE,true);
        //表体的检测结果类型下拉菜单
        getBillCardWrapper().initBodyComboBox("result", ICombobox.STR_PASS_FLAG,true);
        getBillListWrapper().initBodyComboBox("result", ICombobox.STR_PASS_FLAG,true);
        //表体的是扣重扣价下拉
		getBillCardWrapper().initBodyComboBox("iskzkj", ICombobox.CW_KZKJ,true);
		getBillListWrapper().initBodyComboBox("iskzkj", ICombobox.CW_KZKJ,true);
		 //表体的分组下拉
		getBillCardWrapper().initBodyComboBox("groupitem", ICombobox.CW_GROUP,true);
		getBillListWrapper().initBodyComboBox("groupitem", ICombobox.CW_GROUP,true);
		 //表体的是否最高下拉
		getBillCardWrapper().initBodyComboBox("ishigh", ICombobox.CW_HIGH,true);
		getBillListWrapper().initBodyComboBox("ishigh", ICombobox.CW_HIGH,true);
	}

	@Override
	public void setDefaultData() throws Exception {
	}
	
	@Override
	protected BusinessDelegator createBusinessDelegator() {
        return new ClientBaseBD();
    }

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}