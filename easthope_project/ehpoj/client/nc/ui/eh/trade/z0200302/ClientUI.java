
package nc.ui.eh.trade.z0200302;

import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * @author 王明
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class ClientUI extends BillManageUI {

    public ClientUI() {
        super();
    }
    

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

    /* （非 Javadoc）
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO 自动生成方法存根
        return new ClientCtrl();
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
    	//唯一单号   	  
    	  String billType=getUIControl().getBillType();
    	  BillCodeObjValueVO objVO = new BillCodeObjValueVO();
          objVO.setAttributeValue(billType, getUIControl().getBillType());
          String billNo = BillcodeRuleBO_Client.getBillCode(billType, _getCorp().getPrimaryKey(), null,
                                                            objVO);
          getBillCardPanel().setHeadItem("billno",billNo);
          
          //日期 等 初始化
	      getBillCardPanel().setTailItem("coperatorid", _getOperator());
	      getBillCardPanel().setTailItem("dmakedate", _getDate());   
	      getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());   
	      
	      //审批状态
	        getBillCardPanel().setHeadItem("vbillstatus",Integer.valueOf(String.valueOf(IBillStatus.FREE)));
    	
    }

	@Override
	protected void initSelfData() {
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
         getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
		
	}

	@Override
	public ManageEventHandler createEventHandler() {
    	return new AbstractSPEventHandler(this, this.getUIControl());
    }


}

   
    

