package nc.ui.eh.stock.h0150210;


import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.h0150206.StockQuerypriceVO;

/**
 * ����˵���� ѯ�۵�
 * @author ����
 * 2008��12��11��15:38:52
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
		Integer method = hvo.getMethod();		//ѯ�۷�ʽ (0 �ɹ��� 1 ��Ӧ��)
		if(method==null){
			getBillUI().showErrorMessage("����ѡ��ѯ�۷�ʽ!");
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
		Integer method = hvo.getMethod();		//ѯ�۷�ʽ (0 �ɹ��� 1 ��Ӧ��)
		if(method==null){
			getBillUI().showErrorMessage("����ѡ��ѯ�۷�ʽ!");
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