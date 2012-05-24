package nc.ui.eh.trade.z0206005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IPubInterface;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0206005.OrderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能 销售订单
 * 
 * @author 洪海 2008-04-08
 */

public class ClientEventHandler extends AbstractEventHandler {
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}

	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData()
				.dataNotNullValidate();
		// 唯一性校验
		BillModel bm = getBillCardPanelWrapper().getBillCardPanel()
				.getBillModel();

		int res = new PubTools().uniqueCheck(bm,
				new String[] { "pk_invbasdoc" });
		if (res == 1) {
			getBillUI().showErrorMessage("物料有重复！");
			return;
		}

		super.onBoSave();
		setBoEnabled();
	}


	@Override
	protected void onBoQuery() throws Exception {
		super.onBoQuery();
	}

	@Override
	protected void onBoLineDel() throws Exception {
		super.onBoLineDel();
		// 表体中的本次应收金额合计后写入表头中的应收款总额
		int rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getRowCount();
		UFDouble ze = new UFDouble(0); // 应收总额
		UFDouble jgze = new UFDouble(0); // 货款总额
		UFDouble firstdiscze = new UFDouble(0); // 一次折扣总额
		UFDouble seccountze = new UFDouble(0); // 二次折扣总额
		for (int i = 0; i < rows; i++) {
			UFDouble bcysje = new UFDouble(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "bcysje") == null ? "0"
							: getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "bcysje").toString());
			UFDouble totalprice = new UFDouble(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "totalprice") == null ? "0"
							: getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "totalprice").toString());
			UFDouble firstdis = new UFDouble(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "firstcount") == null ? "0"
							: getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "firstcount").toString());
			UFDouble scount = new UFDouble(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "secondcount") == null ? "0"
							: getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "secondcount")
									.toString());
			seccountze = seccountze.add(scount);
			//add by houcq 2011-03-08 begin
			bcysje=totalprice.sub(firstdis);
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
			//add by houcq 2011-03-08 end
			ze = ze.add(bcysje);
			jgze = jgze.add(totalprice);
			firstdiscze = firstdiscze.add(firstdis);
		}
		try {
			OrderVO orVO = (OrderVO) getBillCardPanelWrapper()
					.getChangedVOFromUI().getParentVO();
			UFDouble lastzk = new UFDouble(orVO.getSecondamount() == null ? "0"
					: orVO.getSecondamount().toString());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_7",
					lastzk.sub(seccountze)); // 二次折扣余额 =
												// 二次折扣金额(上月折扣)-本次所用二次折扣
			getBillCardPanelWrapper().getBillCardPanel()
					.setHeadItem("yfze", ze);
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_9",
					jgze);
			// getBillCardPanel().setHeadItem("zkye",jgze.multiply(IPubInterface.DISCOUNTRATE));
			// //edit by wb at 2008-7-4 16:16:16
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("zkye",
					jgze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze)); // 理论二次折扣
																					// (金额*40%-一次折扣总额)
																					// edit
																					// by
																					// wb
																					// at
																					// 2008-7-10
																					// 12:25:07);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int rowcount = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		for (int i = 0; i < rowcount; i++) {
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null,
					i, "secondcount");
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6", null);
	}
	
	
//	设置按钮的可用状态
    @Override
	public void setBoEnabled() throws Exception {
    	 AggregatedValueObject aggvo=getBillUI().getVOFromUI();
             //上一页 下一页的按钮状态  add by wb at 2008-6-20 14:30:23
             if(getButtonManager().getButton(IEHButton.Prev)!=null){
 	            if(!getBufferData().hasPrev()){
 	    			getButtonManager().getButton(IEHButton.Prev).setEnabled(false);
 	    		}
 	            else{
 	            	getButtonManager().getButton(IEHButton.Prev).setEnabled(true);
 	            }
 	    		if(!getBufferData().hasNext()){
 	    			getButtonManager().getButton(IEHButton.Next).setEnabled(false);
 	    		}
 	    		else{
 	            	getButtonManager().getButton(IEHButton.Next).setEnabled(true);
 	            }
             }
             getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
             getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
             // 在有关闭按钮时对关闭按钮的控制 add by wb at 2008-6-20 14:30:23
             String[] keys = aggvo.getParentVO().getAttributeNames();
             if(keys!=null && keys.length>0){
                 for(int i=0;i<keys.length;i++){
                     if(keys[i].endsWith("lock_flag")){ 
                     	String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
                             getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
                         if(lock_flag.equals("false")){
                         	if(getButtonManager().getButton(IEHButton.LOCKBILL)!=null){
                         		getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(true);
                         	}
                         	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                         		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(true);	
                         	}
                         }else{
                         	if(getButtonManager().getButton(IEHButton.LOCKBILL)!=null){
                         		getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(false);
                         	}
                         	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                         		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(false);	//在业务操作下第一个按钮设为不可操作
                         	}
                         	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                            getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                         }
                         break;
                     }
                 }
             }
            OrderVO orVO = (OrderVO) getBillCardPanelWrapper().getChangedVOFromUI().getParentVO();
         	boolean sc_flag = orVO.getScrw_flag()==null?false:orVO.getScrw_flag().booleanValue();		//已有生产订单时不能修改
         	if(sc_flag){
         		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
         	}
             
         getBillUI().updateButtonUI();
    }
    
    @Override
    protected void onBoCard() throws Exception {
    	// TODO Auto-generated method stub
    	onBoCard2();
    	setBoEnabled();
    }
    
    @Override
    protected void onBoEdit() throws Exception {
    	OrderVO orVO = (OrderVO) getBillCardPanelWrapper().getChangedVOFromUI().getParentVO();
    	boolean sc_flag = orVO.getScrw_flag()==null?false:orVO.getScrw_flag().booleanValue();		//已有生产订单时不能修改
    	String th_flag = orVO.getTh_flag()==null?"N":orVO.getTh_flag().toString();								//已有提货时不能修改
    	if(sc_flag){
    		getBillUI().showErrorMessage("此销售订单已做生产订单,不能修改!");
    		return;
    	}
    	if(th_flag.equals("Y")){
    		getBillUI().showErrorMessage("此销售订单已提货,不能修改!");
    		return;
    	}
    	super.onBoEdit();
    }
 
   
}
