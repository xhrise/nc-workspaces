/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.trade.z0205515;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 功能 一次折扣管理（审批）
 * @author 洪海
 * 2008-04-08
 */
public class ClientUI extends AbstractClientUI {

    


    /**
     * 
     */
    public ClientUI() {
        super();
        // TODO 自动生成构造函数存根
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
   
    @Override
    public ManageEventHandler createEventHandler() {
    	// TODO Auto-generated method stub
    	return new ClientEventHandler(this, this.getUIControl());
    }
 
    @Override
	protected void initSelfData() {
    	
    }
}

   
    

