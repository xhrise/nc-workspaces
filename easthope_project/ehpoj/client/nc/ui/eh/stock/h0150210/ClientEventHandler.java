package nc.ui.eh.stock.h0150210;


import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.h0150206.StockQuerypriceVO;

/**
 * 功能说明： 询价单
 * @author 王明
 * 2008年12月11日15:38:52
 */

public class ClientEventHandler extends AbstractEventHandler {
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	@Override
    public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		super.onBoSave_withBillno();
	}
	   
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		StockQuerypriceVO hvo = (StockQuerypriceVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		Integer method = hvo.getMethod();		//询价方式 (0 采购点 1 供应商)
		if(method==null){
			getBillUI().showErrorMessage("请先选择询价方式!");
			return;
		}
		if(method==0){
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vcustcode").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("qzcust").setEnabled(false);
//			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("areaname").setEnabled(true);
		}else{
//			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("areaname").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vcustcode").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("qzcust").setEnabled(true);
		}
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		StockQuerypriceVO hvo = (StockQuerypriceVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		Integer method = hvo.getMethod();		//询价方式 (0 采购点 1 供应商)
		if(method==null){
			getBillUI().showErrorMessage("请先选择询价方式!");
			return;
		}
		super.onBoLineAdd();
		if(method==0){
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vcustcode").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("qzcust").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("areaname").setEnabled(true);
		}else{
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("areaname").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vcustcode").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("qzcust").setEnabled(true);
		}
	}
	
	
	
}