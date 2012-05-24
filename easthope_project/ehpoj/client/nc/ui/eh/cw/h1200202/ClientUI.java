package nc.ui.eh.cw.h1200202;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 回机料核算
 * @author houcq
 * 2011-06-10 10:41:21 
 */
public class ClientUI extends AbstractClientUI {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public ClientUI() {
		     super();
		 }
	   
		@Override
		protected AbstractManageController createController() {
			return new ClientCtrl();
		}

		@Override
		public ManageEventHandler createEventHandler() {
			return new ClientEventHandler(this,this.getUIControl());
		}
		
		@Override
		protected void initSelfData() {
			super.initSelfData();
		}		

		@Override
		public void setDefaultData() throws Exception {			
			super.setDefaultData();

		}
		 @Override
		protected void initPrivateButton() {
			 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"生成明细","生成明细");
		        btn1.setOperateStatus(new int[]{IBillOperate.OP_ADD});
		        addPrivateButton(btn1);
		    	super.initPrivateButton();
		}
		
		 public void afterEdit(BillEditEvent e) 
		 {		      
			 String strKey=e.getKey();
			 if("unitprice".equalsIgnoreCase(strKey))
			 {
				String[] formual=getBillCardPanel().getBodyItem("unitprice").getEditFormulas();//获取编辑公式
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);			
             }
		     super.afterEdit(e);
		 }
	    
}