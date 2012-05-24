/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.stock.h0150325;


import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;
import nc.vo.eh.trade.z00115.CubasdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能:五金采购入库
 * ZB24
 * @author WB
 * 2009-1-8 13:48:06
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @SuppressWarnings("unchecked")
	@Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	//唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("表体有相同物料存在，不允许操作！");
            return;
        }
//      表头金额的累加
        UFDouble summoney=new UFDouble(0);
        StockInBVO[] BVO=(StockInBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		 for(int i=0;i<BVO.length;i++){
			 StockInBVO vo=BVO[i];
			//add by houcq 2011-09-01 begin
				if (vo.getInamount().doubleValue()==0 || vo.getInprice().doubleValue()==0)
				{
					getBillUI().showErrorMessage("入库数量或入库价格不能为零!");
					return;
				}
				//add by houcq 2011-09-01 begin
			 UFDouble def_6= vo.getDef_6()==null?new UFDouble(0):vo.getDef_6();
			 summoney=summoney.add(def_6);
		 }
		 getBillCardPanelWrapper().getBillCardPanel().setHeadItem("summoney", summoney);
		 

         super.onBoSave_withBillno();	
         StockInVO hvo =(StockInVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
         //add by houcq 2011-03-31 begin回写来源单据主子表标记
         String vsourcebillid = hvo.getVsourcebillid();
	     if(vsourcebillid!=null&&vsourcebillid.length()>0){
	    	String vsourcebilltype=hvo.getVsourcebilltype();
	    	int rows = BVO.length;
	    	StringBuilder sb = new StringBuilder("(");
	    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
	    	for (int i=0;i<rows;i++)
    		{
    			StockInBVO vo=BVO[i];
    			sb.append("'"+vo.getVsourcebillid()+"',");
    		}
    		sb.append("'')");
	    	if ("ZB23".equals(vsourcebilltype))//五金采购决策
	    	{	    	
	    		String udb= "update eh_stock_wjdecision_c set rk_flag = 'Y' where pk_wjdecision_c in "+sb.toString();
	    		pubitf.updateSQL(udb);
	    		String sql = "select * from eh_stock_wjdecision_c where pk_wjdecision='"+vsourcebillid+"' and dr=0 minus select *   from eh_stock_wjdecision_c where pk_wjdecision='"+vsourcebillid+"' and dr=0 and rk_flag='Y'";
	    		Vector vector = (Vector) iUAPQueryBS.executeQuery(sql, new VectorProcessor());	          	    		
				if (vector!=null&&vector.size()==0)
				{					
				  	String updateSQL = "update eh_stock_wjdecision set rk_flag = 'Y' where pk_wjdecision = '"+vsourcebillid+"' ";   
				   	pubitf.updateSQL(updateSQL);					    				
				}
	    	}
	    	else
	    	{
	    		String udb= "update eh_stock_apply_b set rk_flag = 'Y' where pk_apply_b in"+sb.toString();
	    		pubitf.updateSQL(udb);
	    		String sql = "select * from eh_stock_apply_b where pk_apply='"+vsourcebillid+"' and dr=0 minus select *   from eh_stock_apply_b where pk_apply='"+vsourcebillid+"' and dr=0 and rk_flag='Y'";
	    		Vector vector = (Vector) iUAPQueryBS.executeQuery(sql, new VectorProcessor());	          	    		
				if (vector!=null&&vector.size()==0)
				{
					String updateSQL = "update eh_stock_apply set rk_flag = 'Y' where pk_apply = '"+vsourcebillid+"' ";   
				    pubitf.updateSQL(updateSQL);
				}
	    	}
	     }
	     //end
    }
    //add by houcq 2011-04-08
    @Override 
    protected void onBoEdit() throws Exception 
    {
    	 super.onBoEdit();
         StockInVO hvo =(StockInVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
         String vsourcebillid = hvo.getVsourcebillid();
	     if(vsourcebillid!=null&&vsourcebillid.length()>0){
	    	//getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
	    	String vsourcebilltype=hvo.getVsourcebilltype();
	    	if ("ZB23".equals(vsourcebilltype))//五金采购决策
	    	{	    	
	    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
	    	}
	    	else
	    	{
	    		String pk_cubasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
	    			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
	    		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    		try {
	    			StringBuffer str = new StringBuffer()
	    			.append("SELECT * FROM bd_cubasdoc cubas WHERE cubas.pk_cubasdoc IN(SELECT cuman.pk_cubasdoc FROM bd_cumandoc cuman WHERE cuman.pk_cumandoc = '"+pk_cubasdoc+"')");    			
	    			CubasdocVO cuvo = (CubasdocVO)iUAPQueryBS.executeQuery(str.toString(), new BeanProcessor(CubasdocVO.class));
	    			boolean sf_flag = cuvo.getFreecustflag()==null?false:cuvo.getFreecustflag().booleanValue();   //散户标记
	    			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("psninfo").setEnabled(sf_flag);
	    		} catch (BusinessException e1) {
	    			e1.printStackTrace();
	    		}
	    	}
	     }
    	 
    }
    @Override
	protected void onBoDelete() throws Exception {
	   	int res = onBoDeleteN(); // 1为删除 0为取消删除
	   	if(res==0){
	   		return;
	   	}
	   
	   	StockInVO hvo =(StockInVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
	   	StockInBVO[] BVO=(StockInBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        String vsourcebillid = hvo.getVsourcebillid();
        String vsourcebilltype = hvo.getVsourcebilltype();
        if(vsourcebillid!=null&&vsourcebillid.length()>0)
        {
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	StringBuilder sb = new StringBuilder("(");
	    	for (int i=0;i<BVO.length;i++)
    		{
    			StockInBVO vo=BVO[i];
    			sb.append("'"+vo.getVsourcebillid()+"',");
    		}
    		sb.append("'')");
	    	if ("ZB23".equals(vsourcebilltype))//五金采购决策
	    	{	    	
	    		String udb= "update eh_stock_wjdecision_c set rk_flag = 'N' where pk_wjdecision_c in "+sb.toString();
	    		pubitf.updateSQL(udb);
	    	    String updateSQL = "update eh_stock_wjdecision set rk_flag = 'N' where pk_wjdecision = '"+vsourcebillid+"' ";   
				pubitf.updateSQL(updateSQL);
	    	}
	    	else
	    	{
	    		//String udb= "update eh_stock_apply_b set rk_flag = 'N' where pk_apply_b ="+sb.toString();
	    		String udb= "update eh_stock_apply_b set rk_flag = 'N' where pk_apply_b in"+sb.toString();//modify by houcq 2011-10-28
	    		pubitf.updateSQL(udb);
				String updateSQL = "update eh_stock_apply set rk_flag = 'N' where pk_apply = '"+vsourcebillid+"' ";   
				pubitf.updateSQL(updateSQL);
	    	}	
        }
    	super.onBoTrueDelete();
	}
    
    public void onButton_N(ButtonObject bo, BillModel model) {
		super.onButton_N(bo, model);
	    String code = bo.getCode();
		if (code!=null)
		{
			if ("五金采购决策".equals(code))
			{
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);	
			}	
			//来源单据不是自制的时候也可以进行行删除操作
			//getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);			
		}
	}
    
    @Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    }
    //add by houcq 2011-06-30行删除时回写标记
    @Override
    protected void onBoLineDel() throws Exception {
    	 int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRowCount();
         PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName()); 
         Object vosurcebilltype = getBillCardPanelWrapper().getBillCardPanel()
			.getHeadItem("vsourcebilltype").getValueObject() ;         
        if(vosurcebilltype!=null)
        {
              for(int i=0;i<row;i++){
                  String vsourcebillrowid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid")==null?"":
                      getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid").toString();
                  if ("ZB23".equals(vosurcebilltype.toString()))//五金采购决策
      	    	{	    	
      	    		String udb= "update eh_stock_wjdecision_c set rk_flag = 'N' where pk_wjdecision_c ='"+vsourcebillrowid+"'";
      	    		pubitf.updateSQL(udb);
      	    	    String updateSQL = "update eh_stock_wjdecision set rk_flag = 'N' where pk_wjdecision = '"+vsourcebillrowid+"' ";   
      				pubitf.updateSQL(updateSQL);
      	    	}
      	    	else
      	    	{
      	    		String udb= "update eh_stock_apply_b set rk_flag = 'N' where pk_apply_b ='"+vsourcebillrowid+"'";
      	    		pubitf.updateSQL(udb);
      				String updateSQL = "update eh_stock_apply set rk_flag = 'N' where pk_apply = '"+vsourcebillrowid+"'";   
      				pubitf.updateSQL(updateSQL);
      	    	}	
              } 
	    	
        }
    	super.onBoLineDel();
    }
    @Override
	public String addCondtion(){
    	return " vbilltype = '"+IBillType.eh_h0150325+"'";
    }
    
}

