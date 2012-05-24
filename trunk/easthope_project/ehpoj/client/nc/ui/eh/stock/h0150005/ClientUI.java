/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.stock.h0150005;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.stock.h0150005.CalcDialog;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 
 * @author 
 功能：供应商评价
 作者：zqy
 日期：2009-3-9 下午02:42:09
 */

public class ClientUI extends AbstractClientUI{

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
        getBillCardPanel().setTailItem("dmakedate",_getDate());
        getBillCardPanel().setTailItem("coperatorid", _getOperator());
        getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
        getBillCardPanel().setHeadItem("pk_period", this.getPk_period());//取年月的PK_PERIOD
        getBillCardPanel().setHeadItem("vyear", _getDate().getYear());
        getBillCardPanel().setHeadItem("vmonth", _getDate().getMonth());
        
//        super.setDefaultData();
    }
  
	protected void initSelfData() {
        
    }
   
    protected void initPrivateButton() {
    	nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"生成明细","生成明细");
        btnBus.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT});
        addPrivateButton(btnBus);
    	super.initPrivateButton();
    }
    
    //取时间PK
    protected String getPk_period(){
    	String pk_period = null;
    	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql = "select pk_period FROM eh_period WHERE nyear = "+_getDate().getYear()+" and nmonth = "+_getDate().getMonth()+" and pk_corp = '"+_getCorp().getPk_corp()+"'";
        try {
        	pk_period = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
        }catch(Exception e){
        	e.printStackTrace();
        }
    	return pk_period;
    	
    }
}

   
    

