package nc.ui.eh.cw.h1101015;


import nc.ui.eh.button.IEHButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.SuperVO;

/**
 * 功能说明：付款确认
 * @author 王明
 * 2008-05-28 下午04:03:18
 */

public class ClientEventHandler extends ManageEventHandler {
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IEHButton.SUREMONEY: 
			sureMoney();
			break;
		}
	}

	@Override
    public void onBoSave() throws Exception {
		super.onBoSave();
	}
       //	add wangming 08.05.10
	   @Override
	protected void onBoQuery() throws Exception {
			StringBuffer sbWhere = new StringBuffer();
			if(askForQueryCondition(sbWhere)==false) 
				return;		
			String sqlWhere = sbWhere.toString();
			sqlWhere = sqlWhere.replaceFirst("审批不通过", "0");
			sqlWhere = sqlWhere.replaceFirst("审批通过", "1");
			sqlWhere = sqlWhere.replaceFirst("审批中", "2");
			sqlWhere = sqlWhere.replaceFirst("提交态", "3");
			sqlWhere = sqlWhere.replaceFirst("作废", "4");
			sqlWhere = sqlWhere.replaceFirst("冲销", "5");
			sqlWhere = sqlWhere.replaceFirst("终止", "6");
			sqlWhere = sqlWhere.replaceFirst("冻结状态", "7");
			sqlWhere = sqlWhere.replaceFirst("自由态", "8");
			
			SuperVO[] queryVos = queryHeadVOs(sqlWhere);
			
	       getBufferData().clear();
	       // 增加数据到Buffer
	       addDataToBuffer(queryVos);

	       updateBuffer();
		}
	   //付款确认按钮的处理动作
	   public void sureMoney() throws Exception{
		   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_flag", "true");
		   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_psndoc", _getOperator());      //确认人
		   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_rq", _getDate());  	         //确认日期
		  
 		   onBoSave();
 		   setBoEnabled();
	   }
	   protected void setBoEnabled() throws Exception {
//	        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
//	        aggvo.getParentVO()
		   String qr_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qr_flag").getValueObject().toString();
		   if(qr_flag.equals("false")){
			   getButtonManager().getButton(IEHButton.SUREMONEY).setEnabled(true);
		   }else{
			   getButtonManager().getButton(IEHButton.SUREMONEY).setEnabled(false);
		   }
	        getBillUI().updateButtonUI();
	    }
	   @Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		setBoEnabled();
	}
	   
	   
}
