package nc.ui.eh.cw.a0001;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;

public class ClientUI extends BillManageUI {
	
	public ClientUI(){
		//getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
	}

	protected AbstractManageController createController() {
		return new  ClientCtrl();
	}
	
	
	
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl() );
	}



	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {

	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	protected void initSelfData() {
		getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
		
		
	}
	
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("coperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate", _getDate().toString());
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("price")){
//			int row = e.getRow();
//			this.getBillCardPanel().setBodyValueAt(0, row, "price");
			//getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(true);
		}
		super.afterEdit(e);
		
	}
	
	


}
