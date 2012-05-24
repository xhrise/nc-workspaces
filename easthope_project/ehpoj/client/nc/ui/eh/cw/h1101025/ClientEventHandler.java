package nc.ui.eh.cw.h1101025;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：运费支付单
 * @author wb
 * 2009-4-15 14:21:34
 */

public class ClientEventHandler extends AbstractEventHandler {
    
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

    @Override
	public void onBoSave() throws Exception {
         //对非空验证 
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
       //add by houcq 2011-07-01
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		StringBuilder tip = new StringBuilder("");
        for(int i=0;i<row;i++){
        	UFDouble amount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "amount")==null?"0":
                getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "amount").toString());
        	//add by houcq 2011-02-09 begin
        	if (amount.equals(new UFDouble(0)))
        	{
        		tip.append((i+1)+",");
        	}
        }
        if (!"".equals(tip.toString()))
        {
        	String tmp=tip.toString();
        	getBillUI().showWarningMessage("第"+tip.toString().substring(0,tmp.length()-1)+"行无购进或者期初数量，禁止分摊运费！!");
    		return;
        }
        //end 
        //super.onBoSave_withBillno();
        super.onBoSave();//add by houcq 2011-07-01
	}
    
    @Override
    public String addCondtion() {
    	return  " vbilltype = '" + IBillType.eh_h1101025 + "' ";
    }


}
