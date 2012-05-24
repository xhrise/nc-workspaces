
package nc.ui.eh.kc.h0257005;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 说明：原料库存月报表
 * 类型：ZA48
 * 作者：wb
 * 时间：2008年5月8日16:34:56
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
        // TODO 自动生成构造函数存根
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

    /* （非 Javadoc）
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
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
    
    /* （非 Javadoc）
     * @see nc.ui.trade.manage.BillManageUI#setBodySpecialData(nc.vo.pub.CircularlyAccessibleValueObject[])
     */
    @Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
            throws Exception {
        // TODO 自动生成方法存根

    }

    /* （非 Javadoc）
     * @see nc.ui.trade.manage.BillManageUI#setHeadSpecialData(nc.vo.pub.CircularlyAccessibleValueObject, int)
     */
    @Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
            int arg1) throws Exception {
        // TODO 自动生成方法存根

    }

    /* （非 Javadoc）
     * @see nc.ui.trade.manage.BillManageUI#setTotalHeadSpecialData(nc.vo.pub.CircularlyAccessibleValueObject[])
     */
    @Override
	protected void setTotalHeadSpecialData(
            CircularlyAccessibleValueObject[] arg0) throws Exception {
        // TODO 自动生成方法存根

    }
    
    /* （非 Javadoc）
     * @see nc.ui.trade.base.AbstractBillUI#setDefaultData()
     */
    @Override
	public void setDefaultData() throws Exception {

        // 表头设置公司代码        
        getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());
        // 表头设删除标记  
        getBillCardPanel().setHeadItem("dr",new Integer(0));
       

    }

    @Override
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
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CALCKCYBB,"计算月报表","计算月报表");
        addPrivateButton(btn1);
    }




}

   
    

