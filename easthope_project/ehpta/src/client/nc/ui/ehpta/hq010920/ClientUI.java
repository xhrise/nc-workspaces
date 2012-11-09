package nc.ui.ehpta.hq010920;

import nc.bs.logging.Logger;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class ClientUI extends nc.ui.trade.manage.BillManageUI implements
		ILinkQuery {
	
	protected QueryConditionClient condition = null;
	
	protected final String[] bodyFamulas = new String[] {
			"rises->int(abs(dieselprice / def7 - 1 ) * 100 / def8)",
			"fee->def9 * (1 + (rises / 100))",	
			"transmny->recnum * fee",
			"paymny-> transmny - outmny",
	};
	
	protected AbstractManageController createController() {
		return new ClientUICtrl();
	}

	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类
	 * 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new Delegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < listButns.length; i++) {
			if (listButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (listButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		if (hasCommit) {
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}

		if (hasAudit) {
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}

		if (hasCancelAudit) {
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);
		}
		
		addPrivateButton(DefaultBillButton.getMaintainButtonVO());
		addPrivateButton(DefaultBillButton.getStatisticsButtonVO());
		addPrivateButton(DefaultBillButton.getMarkButtonVO());
		addPrivateButton(DefaultBillButton.getSelAllButtonVO());
		addPrivateButton(DefaultBillButton.getSelNoneButtonVO());
		addPrivateButton(DefaultBillButton.getSettleButtonVO());
		addPrivateButton(DefaultBillButton.getCancleSettleButtonVO());
		
	}

	@Override
	protected boolean isSetRowNormalState() {
		return false;
	}
	
	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
		if (billId != null) {
			try {
				setCurrentPanel(BillTemplateWrapper.CARDPANEL);
				AggregatedValueObject vo = loadHeadData(billId);
				getBufferData().addVOToBuffer(vo);
				setListHeadData(new CircularlyAccessibleValueObject[] { vo
						.getParentVO() });
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	protected ManageEventHandler createEventHandler() {
		return new EventHandler(this, getUIControl());
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
		
		getBillListPanel().setMultiSelect(true);
		getBillCardPanel().setBodyMultiSelect(true);
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus() , "dmakedate" };
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString() , _getDate() };

		for (int i = 0; i < itemkeys.length; i++) {
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if (item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if (item != null)
				item.setValue(values[i]);
		}
	}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {

//		if (vo != null && vo.getParentVO() != null
//				&& vo.getParentVO().getAttributeValue("settleflag") != null) {
//
//			if (((UFBoolean) vo.getParentVO().getAttributeValue("settleflag"))
//					.booleanValue()) {
//				getButtonManager().getButton(DefaultBillButton.Confirm)
//						.setEnabled(false);
//				getButtonManager().getButton(DefaultBillButton.Cancelconfirm)
//						.setEnabled(true);
//			} else {
//				getButtonManager().getButton(DefaultBillButton.Confirm)
//						.setEnabled(true);
//				getButtonManager().getButton(DefaultBillButton.Cancelconfirm)
//						.setEnabled(false);
//			}
//
//		}

		return super.getExtendStatus(vo);
	}
	
	/**
	 * 实例化查询模版。 创建日期：(2001-8-28 16:18:43)
	 * 
	 * @return nc.bs.pub.query.QueryCondition
	 */
	protected QueryConditionClient getConditionClient() {
		if (condition == null) {
			try {
				
				condition = new QueryConditionClient(this);
				condition.setName("QueryConditionClient1");
				condition.setSize(700, 400);
				
				condition.setTempletID(_getCorp().getPk_corp(), "HQ010920", _getOperator(), getBusinessType());
				
				condition.setCurPKCorp(_getCorp().getPk_corp());
				condition.setCurUserID(_getOperator());
				condition.setCurFunCode("HQ010920");
				
				// 隐藏常规页签
				condition.setNormalShow(false);
				
			} catch (Exception e) {
				
			}
		
		}
		
		return condition;
	}
	
	@Override
	protected String getBillNo() throws Exception {
		return GeneraterBillNO.getInstanse().build(getUIControl().getBillType(), _getCorp().getPk_corp());
	}
	
	@Override
	public void updateButtons() {
		
		if(getBillOperate() == IBillOperate.OP_ADD || getBillOperate() == IBillOperate.OP_EDIT || getBillOperate() == IBillOperate.OP_INIT) {
			getButtonManager().getButton(DefaultBillButton.Settle).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.CancleSettle).setEnabled(false);
			
			getButtonManager().getButton(DefaultBillButton.Statistics).setEnabled(true);
			getButtonManager().getButton(DefaultBillButton.Mark).setEnabled(true);
		} else {
			getButtonManager().getButton(DefaultBillButton.Settle).setEnabled(true);
			getButtonManager().getButton(DefaultBillButton.CancleSettle).setEnabled(true);
			
			getButtonManager().getButton(DefaultBillButton.Maintain).setEnabled(true);
			getButtonManager().getButton(DefaultBillButton.Statistics).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.Mark).setEnabled(false);
		}

		super.updateButtons();
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {

		super.afterEdit(e);
		
		try {
			if(e.getSource() instanceof BillCellEditor) {
				
				if("dieselprice".equals(e.getKey()) || "rises".equals(e.getKey()) || "outmny".equals(e.getKey())) 
					afterSetBodyMny(e);
					
				if("def10".equals(e.getKey()))
					afterSetSettlement(e);
			}
			
			
		} catch(Exception ex) {
			Logger.error(ex.getMessage(), ex, this.getClass(), "afterEdit");
		}
	}
	
	protected final void afterSetSettlement(BillEditEvent e) throws Exception {
		
		Boolean def10 = e.getValue() == null ? false : (Boolean) e.getValue();
		if(def10)
			getBillCardPanel().setBodyValueAt(_getDate().toString(), e.getRow(), "def11");
		else
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "def11");
		
		
	}
	
	protected final void afterSetBodyMny(BillEditEvent e) throws Exception {
		
		UFDouble rises = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "rises");
		UFDouble dieselprice = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "dieselprice");
		UFDouble def9 = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "def9");
		UFDouble recnum = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "recnum");
		UFDouble outmny = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "outmny");
		UFDouble def7 = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "def7");
		UFDouble def8 = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "def8");
		
		rises = rises == null ? new UFDouble("0") : rises;
		def9 = def9 == null ? new UFDouble("0") : def9;
		recnum = recnum == null ? new UFDouble("0") : recnum;
		outmny = outmny == null ? new UFDouble("0") : outmny;
		def7 = def7 == null ? new UFDouble("0") : def7;
		def8 = def8 == null ? new UFDouble("0") : def8;
		dieselprice = dieselprice == null ? def7 : dieselprice;
		
		if("rises".equals(e.getKey())) {
			
			UFDouble fee = def9.multiply(rises.div(100).add(1));
			getBillCardPanel().setBodyValueAt(fee, e.getRow(), "fee");
			
			dieselprice = def7.multiply(rises.div(100).add(1));
			getBillCardPanel().setBodyValueAt(dieselprice, e.getRow(), "dieselprice");
			
			UFDouble transmny = recnum.multiply(fee);
			getBillCardPanel().setBodyValueAt(transmny, e.getRow(), "transmny");
			
			getBillCardPanel().setBodyValueAt(transmny.sub(outmny), e.getRow(), "paymny");
			
		} else if("dieselprice".equals(e.getKey())) {
			
			UFDouble newRises = def8.doubleValue() == 0 ? def8 : new UFDouble(new UFDouble(dieselprice.div(def7).sub(1).multiply(100).intValue() / def8.doubleValue()).intValue());
			getBillCardPanel().setBodyValueAt(newRises, e.getRow(), "rises");
			
			UFDouble fee = def9.multiply(newRises.div(100).add(1));
			getBillCardPanel().setBodyValueAt(fee, e.getRow(), "fee");
			
			UFDouble transmny = recnum.multiply(fee);
			getBillCardPanel().setBodyValueAt(transmny, e.getRow(), "transmny");
			
			getBillCardPanel().setBodyValueAt(transmny.sub(outmny), e.getRow(), "paymny");
			
		} else if("outmny".equals(e.getKey())) {
			
			UFDouble fee = def9.multiply(rises.div(100).add(1));
			UFDouble transmny = recnum.multiply(fee);
			getBillCardPanel().setBodyValueAt(transmny.sub(outmny), e.getRow(), "paymny");
			
		}
		
	}
}
