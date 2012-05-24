/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.stock.h0150335;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能:五金采购出库
 * ZB25
 * @author WB
 * 2009-1-9 10:42:36
 *
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI{
	static UIRefPane ref =null;//add by houcq 2011-02-24
	public ClientUI() {
        super();
        ref=(UIRefPane) getBillCardPanel().getBodyItem("vinvcode").getComponent();
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
        ref=(UIRefPane) getBillCardPanel().getBodyItem("vinvcode").getComponent();
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
        ref=(UIRefPane) getBillCardPanel().getBodyItem("vinvcode").getComponent();
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
    	getBillCardPanel().setHeadItem("ckdate", _getDate());
        super.setDefaultData_withNObillno();
    }
   
 
    @SuppressWarnings("unchecked")
	@Override
    public void afterEdit(BillEditEvent e) {
    	super.afterEdit(e);
    	String strKey = e.getKey();
    	//<修改>功能：表体仓库改变时取对应的库存数量。 时间：2009-10-21    作者：张志远	
    	/*
    	 * 根据仓库取某物料的库存数量
    	 */
        if(strKey.equals("vstore")||strKey.equals("vinvcode")){
            int row = e.getRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_store = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store")==null?"":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store").toString();
           HashMap hm =new PubTools().getInvKcAmountByStore(_getCorp().getPk_corp(),_getDate(), pk_store);
			try {
    			UFDouble kcamount = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
	            getBillCardPanel().setBodyValueAt(kcamount, row, "kcamount");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            
        }
        
        /**选择仓库后，判断是否是末级仓库(花召滨要求) add by zqy 2010年11月23日10:38:19**/
        if(strKey.equals("pk_outtype") && e.getPos()==HEAD){
        	String pk_outtype = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_outtype").getValueObject()==null?"":
        		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_outtype").getValueObject().toString();
        	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	String sql = " select * from bd_rdcl where pk_frdcl = '"+pk_outtype+"' and nvl(dr,0)=0 ";
        	try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				if(arr!=null && arr.size()>0){
					showErrorMessage("请选择末级出库类型!");
					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_outtype", null);
					return;
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}        	
        }
    }
}

   
    

