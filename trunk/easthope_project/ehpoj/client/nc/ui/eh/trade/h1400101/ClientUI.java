
package nc.ui.eh.trade.h1400101;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 说明：客商余额结算
 * 类型：ZB41
 * 作者：张志远
 * 时间：2010年01月26日
 */
public class ClientUI extends BillManageUI {

    
    
    public ClientUI() {
        super();        
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

	protected AbstractManageController createController() {
        return new ClientCtrl();
    }

	protected ManageEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }
    
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
            throws Exception {

    }

	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
            int arg1) throws Exception {
    }

	protected void setTotalHeadSpecialData(
            CircularlyAccessibleValueObject[] arg0) throws Exception {
    }
    
	public void setDefaultData() throws Exception {

        // 表头设置公司代码        
        getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());
        // 表头设删除标记  
        getBillCardPanel().setHeadItem("dr",new Integer(0));
       

    }

    protected void initSelfData() {
      // 显示数据库中的0.
      getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);  
    }
   
    /*
     * 注册自定义按钮
     * 2008年5月7日13:42:54
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CALCKCYBB,"计算客商余额表","计算客商余额表");
        addPrivateButton(btn1);
    }

}

   
    

