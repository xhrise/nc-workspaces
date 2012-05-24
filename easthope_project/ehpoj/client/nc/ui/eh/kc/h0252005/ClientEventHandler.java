package nc.ui.eh.kc.h0252005;

import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.kc.h0252005.ScDbdBVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：材料调拨单
 * @author 王明
 * 2008-05-07 下午04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
    public void onBoSave() throws Exception {
	   // 对非空和0验证
	   getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
	   //对两边的数量判段是否相同
	   int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
	   ScDbdBVO[] bvo=(ScDbdBVO[]) getBillUI().getVOFromUI().getChildrenVO();
	   UFDouble sumDcmount = new UFDouble();
	   UFDouble sumDrrmount = new UFDouble();
	   StringBuilder tip= new StringBuilder();
	   for(int i=0;i<rows;i++){
		   UFDouble dcmount=bvo[i].getDcmount();		   
 		   UFDouble drrmount=bvo[i].getDrrmount();
		   sumDcmount=sumDcmount.add(dcmount);
		   sumDrrmount=sumDrrmount.add(drrmount);
		   UFDouble pcmount=bvo[i].getPcamount();
		   if (pcmount==null)
		   {
			   pcmount=new UFDouble(0);
		   }
		   if (pcmount.doubleValue()>0)
		   {
			   if (dcmount.doubleValue()==0 ||dcmount.doubleValue()>pcmount.doubleValue())
			   {
				   tip.append("第"+(i+1)+"物料调出数量为零或大于库存数量,不允许调出!\r\n");
			   }
		   }
		   else if (pcmount.doubleValue()<0)
		   {
			   if (dcmount.doubleValue()>=0 ||dcmount.doubleValue()<pcmount.doubleValue())
			   {
				   tip.append("第"+(i+1)+"物料调出数量为零或大于库存数量,不允许调出!\r\n");
			   }
		   }else
		   {
			   tip.append("第"+(i+1)+"物料库存数量为零,不允许调出!\r\n");
		   }
		   
	   }
	   if (!"".equals(tip.toString()))
	   {
		   getBillUI().showErrorMessage(tip.toString());
		   return; 
	   }
	   if(!(sumDcmount.equals(sumDrrmount))){
		   getBillUI().showErrorMessage("你的出库数量和入库数量不相同，请检查！！！");
		   return;
	   }
		super.onBoSave();
	}
	
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), row, "vmanager");
        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), row, "pk_manage");
        String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_manage").getEditFormulas();//获取编辑公式
        getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row,formual);
	}
	
}
