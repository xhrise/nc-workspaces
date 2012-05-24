package nc.ui.eh.kc.h0256005;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：退貨入库单
 * @author 張起源
 * 時間：2008-5-27 10:00:17
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
		//入库日期
		getBillCardPanel().setHeadItem("rkdate", _getDate());  
//        getBillCardPanel().setTailItem("dmakedate",_getDate());
//        String pc = PubTools.getPC("eh_sc_cprkd", _getDate()); //获得批次  在后期取发票的批次
//        getBillCardPanel().setHeadItem("pc", pc);
   
		super.setDefaultData();
	}
    
	@Override
	protected void initSelfData() {        
	     super.initSelfData();
	}
    
    @Override
	public void afterEdit(BillEditEvent e) {
        String strKey = e.getKey();
        int row = getBillCardPanel().getBillTable().getRowCount();
        //表体金额等于入库数量*单价
        if(strKey.equals("rkmount")|| strKey.equals("price")){            
            for(int i=0;i<row;i++){
                UFDouble rkmount = new UFDouble(getBillCardPanel().getBodyValueAt(i, "rkmount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "rkmount").toString());
                UFDouble price = new UFDouble(getBillCardPanel().getBodyValueAt(i, "price")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "price").toString());
                UFDouble sum = rkmount.multiply(price);
                getBillCardPanel().setBodyValueAt(sum, i, "summoney");
            }
            
            
        } 
        super.afterEdit(e);
    }
    @Override
    protected void initPrivateButton() {
	 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.UnCheck,"表体不检测","表体不检测");
        btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn2);
    	super.initPrivateButton();
    }


}
