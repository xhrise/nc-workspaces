package nc.ui.eh.stock.z0502505;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDouble;



/**
 * 功能说明：成品检测申请
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientUI extends AbstractClientUI {

	static UIRefPane ref =null;
	public ClientUI(){
		super();
		ref=(UIRefPane) getBillCardPanel().getBodyItem("vinvcode").getComponent();
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
		super.setDefaultData(); 
	}
    
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
        super.initPrivateButton();
    }

    @Override
    public void afterEdit(BillEditEvent e) {
    	String strKey=e.getKey();
    	//盘点时根据物料取出 当月的库存
    	if(strKey.equals("vinvcode")){
    		int row=e.getRow();
    		String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
        							getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();					//物料
    		try {
	    		UFDouble kcamount = new PubTools().getKCamountByinv_Back(pk_invbasdoc,_getCorp().getPk_corp(),_getDate());			//库存数量
	    		getBillCardPanel().setBodyValueAt(kcamount, row, "amount");
	    		getBillCardPanel().setBodyValueAt(kcamount, row, "checkamount");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//在自制的条件下数量和批次可以修改。时间：2010-01-05作者：张志远
    		this.getBillCardWrapper().getBillCardPanel().getBodyItem("amount").setEnabled(true);
    		this.getBillCardWrapper().getBillCardPanel().getBodyItem("instalment").setEnabled(true);
    	}
    	super.afterEdit(e);
    }
}
