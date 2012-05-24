
package nc.ui.eh.trade.z0205502;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.constant.ct.OperationState;

/**
 * 说明：期间折扣计算
 * 类型：ZA66
 * 作者：wb
 * 时间：2008-6-10 16:01:45
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
    	 getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
    }
   
    /*
     * 注册自定义按钮
     * 2008年5月7日13:42:54
     */
    @Override
	protected void initPrivateButton() {
    	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.GenNextData,"生成下月数据","生成下月数据");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.CALCKCYBB,"试算","试算");
        btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn2);
        nc.vo.trade.button.ButtonVO btn3 = ButtonFactory.createButtonVO(IEHButton.ConfirmSC,"启用","启用");
        btn3.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn3);
        nc.vo.trade.button.ButtonVO btn4 = ButtonFactory.createButtonVO(IEHButton.FirDayOfMonCut, "本月折扣期初建帐", "本月折扣期初建帐");
        btn4.setOperateStatus(new int[]{IBillOperate.OP_INIT});
        this.addPrivateButton(btn4);
    }




}

   
    

