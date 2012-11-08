package nc.ui.ehpta.hq010610;

import nc.bs.logging.Logger;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.ehpta.hq010610.SaleMnymodifyVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

public class ClientUI extends BillCardUI implements ILinkQuery{

	@Override
	protected ICardController createController() {
		return new ClientUICtrl();
	}
	
	@Override
	protected CardEventHandler createEventHandler() {
		return new EventHandler(this , createController());
	}
	
	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
		
		addPrivateButton(DefaultBillButton.getMaintainButtonVO());
		addPrivateButton(DefaultBillButton.getStatisticsButtonVO());
		addPrivateButton(DefaultBillButton.getMarkButtonVO());
		addPrivateButton(DefaultBillButton.getSelAllButtonVO());
		addPrivateButton(DefaultBillButton.getSelNoneButtonVO());
		addPrivateButton(DefaultBillButton.getConfirmButtonVO());
		
	}
	
	@Override
	public String getRefBillType() {
		return null;
	}

	@Override
	protected void initSelfData() {
		getBillCardPanel().setBodyMultiSelect(true);
		
		getButtonManager().getButton(DefaultBillButton.Confirm).setEnabled(false);
	}

	@Override
	public void setDefaultData() throws Exception {

	}

	public void doQueryAction(ILinkQueryData querydata) {
		
	}
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		return super.getExtendStatus(vo);
	}
	
	@Override
	public void updateButtons() {
		
		if(getBillOperate() == IBillOperate.OP_ADD || getBillOperate() == IBillOperate.OP_EDIT) {
			getButtonManager().getButton(DefaultBillButton.Confirm).setEnabled(true);
			getButtonManager().getButton(DefaultBillButton.Mark).setEnabled(true);
			getButtonManager().getButton(DefaultBillButton.SelAll).setEnabled(true);
			getButtonManager().getButton(DefaultBillButton.SelNone).setEnabled(true);
		} else { 
			getButtonManager().getButton(DefaultBillButton.Confirm).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.Mark).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.SelAll).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.SelNone).setEnabled(false);
		
		}
		
		try {
			SaleMnymodifyVO[] bodyVOs = (SaleMnymodifyVO[]) getBillCardWrapper().getBillVOFromUI().getChildrenVO();
			if(bodyVOs != null && bodyVOs.length > 0 && !(getBillOperate() == IBillOperate.OP_ADD || getBillOperate() == IBillOperate.OP_EDIT))  
				getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			 else 
				getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			
		} catch (Exception e) {
			Logger.info(e.getMessage(), this.getClass(), "updateButtons");
		}
		
		super.updateButtons();
		
	}
	
	@Override
	protected void setTotalUIState(int intOpType) throws Exception {
		
		//设置按钮状态
		getButtonManager().setButtonByOperate(intOpType);
		updateButtons();
		//根据操作类型设置UI状态
		switch (intOpType) {
			case OP_ADD :
				getBillCardPanel().setEnabled(true);
				getBillCardPanel().addNew();
				setDefaultData();
				setBillNo();
				getBillCardPanel().transferFocusToFirstEditItem();
				break;

			case OP_EDIT :
				getBillCardPanel().setEnabled(true);
//				getBillCardWrapper().setRowStateToNormal();
				getBillCardPanel().transferFocusToFirstEditItem();
				break;
				
			case OP_REFADD :
				getBillCardPanel().setEnabled(true);
				//getBillCardPanel().addNew();
				setDefaultData();
				setBillNo();
				break;

			case OP_INIT :
				getBillCardWrapper().setCardData(null);
				
			case OP_NOTEDIT :
				getBillCardPanel().setEnabled(false);
					break;
				
			default :
				break;
				
		}
		
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		try {
		if(SaleMnymodifyVO.NTAXPRICE.equals(e.getKey())) 
			afterSetMnys(e.getRow());
		
		} catch(Exception ex) {
			Logger.error(ex.getMessage(), ex, this.getClass(), "afterEdit");
		}
	}
	
	protected final void afterSetMnys(int row) throws Exception {
		
		UFDouble ntaxprice = (UFDouble) getBillCardPanel().getBodyValueAt(row , SaleMnymodifyVO.NTAXPRICE);
		UFDouble ntaxrate = (UFDouble) getBillCardPanel().getBodyValueAt(row , SaleMnymodifyVO.NTAXRATE);
		UFDouble nnumber = (UFDouble) getBillCardPanel().getBodyValueAt(row , SaleMnymodifyVO.NNUMBER);
		
		if(ntaxprice == null)
			ntaxprice = new UFDouble("0" , 2);
		
		if(ntaxrate == null)
			ntaxrate = new UFDouble("0" , 2);
		
		if(nnumber == null)
			nnumber = new UFDouble("0" , 2);
		
		UFDouble price = ntaxprice.div(ntaxrate.div(100).add(1));
		UFDouble summny = ntaxprice.multiply(nnumber);
		UFDouble taxmny = ntaxprice.sub(price).multiply(nnumber);
		
		String[] formulas = new String[] {
			// 含税单价
			"ntaxprice->" + ntaxprice.setScale(2, 0) ,
			"norgqttaxnetprc->ntaxprice" , 
			"norgqttaxprc->ntaxprice" ,
			"noriginalcurtaxnetprice->ntaxprice" ,
			"noriginalcurtaxprice->ntaxprice" ,
			"nqttaxnetprc->ntaxprice" ,
			"nqttaxprc->ntaxprice" ,
			"ntaxnetprice->ntaxprice" ,
			
			// 无税单价
			"nprice->" + price.setScale(2 , 0) ,
			"nnetprice->nprice",
			"norgqtnetprc->nprice",
			"norgqtprc->nprice",
			"noriginalcurnetprice->nprice",
			"noriginalcurprice->nprice",
			"nqtnetprc->nprice",
			"nqtprc->nprice",
			
			// 加税合计
			"nsummny->" + summny.setScale(2, 0),
			"noriginalcursummny->nsummny" ,
			
			// 税金
			"ntaxmny->" + taxmny.setScale(2, 0),
			"noriginalcurtaxmny",
			
			// 无税金额
			"nmny->" + summny.sub(taxmny).setScale(2, 0),
			"noriginalcurmny->nmny",
			
		};
		
		getBillCardPanel().execBodyFormulas(row, formulas);
		
	}

}
