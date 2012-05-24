/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.trade.h0205615;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDate;

/**
 * 功能:销售旬计划
 * ZB14
 * @author WB
 * 2008-12-22 13:29:43
 *
 */
public class ClientUI extends AbstractClientUI{
	
	String pk_cubasdoc = null;
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
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    @Override
	public void setDefaultData() throws Exception {
    	UFDate nowdate = _getDate();
    	int day = nowdate.getDay();
    	String xun_flag = null;
    	if(day>0&&day<=10) xun_flag = "0";
    	if(day>10&&day<=20) xun_flag = "1";
    	if(day>20) xun_flag = "2";
    	getBillCardPanel().setHeadItem("plandate", _getDate());
    	getBillCardPanel().setHeadItem("xun_flag", xun_flag);
    	 getBillCardPanel().setHeadItem("new_flag", "Y");             // 最新标记
         getBillCardPanel().setHeadItem("ver", 1);             // 版本
        super.setDefaultData_withNObillno();
    }
   
    /*
     * 注册自定义按钮
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
    	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"生成产品明细","生成产品明细");
    	btn1.setOperateStatus(new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD});
    	addPrivateButton(btn1);
    	nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.STOCKCHANGE,"版本变更","版本变更");
    	btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
    	addPrivateButton(btn2);
    	super.initPrivateButton();
    	
    }
 
    @Override
    protected void initSelfData() {
    	getBillCardWrapper().initHeadComboBox("xun_flag",ICombobox.Period_flag, true);
        getBillListWrapper().initHeadComboBox("xun_flag",ICombobox.Period_flag, true);
    	super.initSelfData();
    }
    
    @Override
    public void afterEdit(BillEditEvent e) {
    	String strKey = e.getKey();
    	super.afterEdit(e);
    	if(strKey.equals("plandate")){
    		UFDate plandate = getBillCardPanel().getHeadItem("plandate").getValueObject()==null||getBillCardPanel().getHeadItem("plandate").getValueObject().toString().length()==0?_getDate():new UFDate(getBillCardPanel().getHeadItem("plandate").getValueObject().toString());
        	int day = plandate.getDay();
        	String xun_flag = null;
        	if(day>0&&day<=10) xun_flag = "0";
        	if(day>10&&day<=20) xun_flag = "1";
        	if(day>20) xun_flag = "2";
        	getBillCardPanel().setHeadItem("xun_flag", xun_flag);
    	}
    }
}

   
    

