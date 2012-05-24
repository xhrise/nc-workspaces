
package nc.ui.eh.cwitf.h10104;



import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * U8数据源配置
 * @author 王兵
 * 2008-7-8 14:44:46
 */
public class ClientUI extends BillManageUI {

    public ClientUI() {
        super();
        initilize();
    }

    public ClientUI(String arg0, String arg1, String arg2, String arg3, String arg4) {
        super(arg0, arg1, arg2, arg3, arg4);
        initilize();
    }

    @Override
	protected AbstractManageController createController() {
		// TODO 自动生成方法存根
		return new ClientCtrl();
	}

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	protected ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    

    @Override
	public String getRefBillType() {
        return null;
    }

    @Override
	protected void initSelfData() {
    }
   
    @Override
	public void setDefaultData() throws Exception {
    	//表头设置单
    	getBillCardPanel().getHeadItem("pk_corp").setValue(_getCorp().getPk_corp());
        getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
        getBillCardPanel().getTailItem("dmakedate").setValue(_getDate()); 
    }
    
    private void initilize() {
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo, int intRow) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	  @Override
	protected void initPrivateButton() {
			nc.vo.trade.button.ButtonVO bt1 = ButtonFactory.createButtonVO(IEHButton.CREATEVOUCHER,"测试连接","测试连接");
			bt1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	  	 	addPrivateButton(bt1);
	       super.initPrivateButton();
	   }
    
    
	

}