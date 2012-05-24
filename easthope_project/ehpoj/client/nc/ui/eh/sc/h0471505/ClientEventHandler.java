/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.sc.h0471505;


import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.sc.h0471505.ScSbserviceBVO;
import nc.vo.eh.sc.h0471505.ScSbserviceVO;
import nc.vo.eh.trade.h0205615.TradePeriodplanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能:设备维修
 * ZB30
 * @author WB
 * 2008-12-20 12:38:05
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
	TradePeriodplanVO hvo = null;
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @Override
	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	Object vSourceBillType = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject();
    	// 关闭上游设备维修计划子表
		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		if(vSourceBillType!=null && vSourceBillType.equals(IBillType.eh_h0471005)){
			ScSbserviceVO hvo = (ScSbserviceVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		    String pk_plan = hvo.getVsourcebillid();	//上游主表主键
			int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
	        for (int i = 0; i < row; i++) {	
	        	String pk_plan_b = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsoucebillrowid")==null?"":
	            						getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsoucebillrowid").toString();	
		        String updateSQL =  "update eh_sc_sbplan_b set wx_flag = 'Y' where pk_plan_b = '"+pk_plan_b+"' and isnull(dr,0)=0";
				pubItf.updateSQL(updateSQL);
			}
		//关闭设备维修计划主表
            StringBuffer hxsql = new StringBuffer()
            .append(" select count(*) amount,'A' flag from eh_sc_sbplan_b where pk_plan = '"+pk_plan+"' and isnull(dr,0)=0 ")  //设备维护计划子表个数 
            .append(" union all")
            .append(" select count(*) amount,'B' flag from eh_sc_sbplan_b where pk_plan = '"+pk_plan+"' and isnull(wx_flag,'N')='Y' and isnull(dr,0)=0 ");  //已维修总数
        	try {
        		ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(hxsql.toString(),new MapListProcessor());
    			if(arr!=null&&arr.size()>0){
    				HashMap hma = new HashMap();
    				for(int i=0; i<arr.size(); i++){
    					HashMap hm=arr.get(i);
    					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
    					UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
    					hma.put(flag, amount);
    				}
    				String updateSQL = null;
    				if(hma.get("A").equals(hma.get("B"))){  //全部出库时将提货通知单关闭
    					updateSQL = "update eh_sc_sbplan set wx_flag = 'Y' where pk_plan = '"+pk_plan+"' and isnull(dr,0)=0";
    				}
    				else{
    					updateSQL = "update eh_sc_sbplan set wx_flag = 'N' where pk_plan = '"+pk_plan+"' and isnull(dr,0)=0";
    				}
    				pubItf.updateSQL(updateSQL);
    			}
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
		}
        super.onBoSave_withBillno();
    }
    
    
    @Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    }
    
    @Override
    public void onButton_N(ButtonObject bo, BillModel model) {
    	super.onButton_N(bo, model);
    	 String bocode=bo.getCode();
         if(bocode.equals("设备维修计划")){
           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").setValue(IBillType.eh_h0471005);
           int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
           for(int i=0;i<row;i++){
               getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vsbcode", false);
               getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vsbproject", false);
           }
           getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
           getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
           getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
           getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
           getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
           getBillUI().updateUI();
         }
    }
    
    @Override
	protected void onBoDelete() throws Exception {
        if(onBoDeleteN()==0){
           return;
        }       
    	AggregatedValueObject agg = getBillUI().getVOFromUI();
        ScSbserviceVO svo = (ScSbserviceVO) agg.getParentVO();
        String vsourcebilltype  = svo.getVsourcebilltype();
        PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        if(vsourcebilltype.equals(IBillType.eh_h0471005)){
        	String pk_plan = svo.getVsourcebillid();               //维修计划主键
            ScSbserviceBVO[] bvos = (ScSbserviceBVO[])agg.getChildrenVO();
            if(bvos!=null&&bvos.length>0){
            	int length = bvos.length;
            	String[] pk_plan_bs = new String[length]; 
            	String pk_plan_b = null;
            	for(int i=0; i<length; i++){
            		ScSbserviceBVO bvo = bvos[i];
            		pk_plan_b = bvo.getVsoucebillrowid();
            		pk_plan_bs[i] = pk_plan_b;
            	}
            	pk_plan_b = PubTools.combinArrayToString(pk_plan_bs);
            	String sql = "update eh_sc_sbplan_b set wx_flag = 'N' where pk_plan_b in "+pk_plan_b;
	            pubItf.updateSQL(sql);
            }
            String updateSQL = "update eh_sc_sbplan set wx_flag = 'N' where pk_plan = '"+pk_plan+"' and isnull(dr,0)=0";
    		pubItf.updateSQL(updateSQL);
    	 }
        super.onBoTrueDelete();
    }
}

