
package nc.ui.eh.voucher.h10120;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
 * ˵������Ӧ����������ƾ֤
 * �������ͣ�ZB38
 * @author ���� 
 * ʱ�䣺2009-11-5 11:02:34
 */
public class ClientUI extends BillCardUI {
	
	public ClientUI() {
	    super();
	}
	
	

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ICardController createController() {
		return new ClientCtrl();
	}

	  @Override
	public CardEventHandler createEventHandler() {
	        return new ClientEventHandler(this,this.getUIControl());
	    }
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	
}