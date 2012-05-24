/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.sc.h0471005;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.eh.refpub.SbwxXMByPksbRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 功能:设备维修计划
 * ZB29
 * @author WB
 * 2008-12-24 15:45:31
 *
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
    	getBillCardPanel().setHeadItem("plandate", _getDate());
        super.setDefaultData_withNObillno();
    }
   
    @Override
    protected void initSelfData() {
         super.initSelfData();
    }

    @Override
    public void afterEdit(BillEditEvent e) {
    	String strKey=e.getKey();
        if(e.getPos()==BODY){
        	if(strKey.equals("vsbcode")){
        		 String pk_equipment = getBillCardPanel().getBodyValueAt(e.getRow(),"pk_equipment")==null?"":
 							getBillCardPanel().getBodyValueAt(e.getRow(),"pk_equipment").toString();
        		 SbwxXMByPksbRefModel.pk_sb = pk_equipment;
        		 StringBuffer sql = new StringBuffer()
        		 .append(" SELECT a.pk_sb,b.pk_sb fpk_sb,c.pk_sb spk_sb")
    			 .append(" FROM eh_sc_sbbasdoc a LEFT JOIN eh_sc_sbbasdoc b ON a.pk_father = b.pk_sb")
    			 .append(" LEFT JOIN eh_sc_sbbasdoc c ON b.pk_father = c.pk_sb")
    			 .append(" join eh_sc_sbbasdoc_b bb on a.pk_sb = bb.pk_sb WHERE a.pk_corp = '"+_getCorp().getPk_corp()+"'")
    			.append("  and a.pk_sb = '"+pk_equipment+"' AND ISNULL(a.dr,0)=0 and isnull(bb.dr,0)=0");
        		 IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	     try {
        	    	 getBillCardPanel().setBodyValueAt(null, e.getRow(), "spk_sb");		
        	    	 HashMap hm = (HashMap)iUAPQueryBS.executeQuery(sql.toString(), new MapProcessor());
        	    	 if(hm!=null&&hm.size()>0){
        	    		 String fpk_sb = hm.get("fpk_sb")==null?null:hm.get("fpk_sb").toString();
        	    		 String spk_sb = hm.get("spk_sb")==null?null:hm.get("spk_sb").toString();
        	    		 if(spk_sb==null){
        	    			 spk_sb = fpk_sb;
        	    		 }
        	    		 getBillCardPanel().setBodyValueAt(spk_sb, e.getRow(), "spk_sb");		//所属设备
        	    	 }
        	     }catch (Exception ex) {
        	    	 ex.printStackTrace();
        	     }
        	}
        }
        super.afterEdit(e);
    }
}

   
    

