package nc.ui.eh.trade.z0255001;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.businessref.YsContractRefModel;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0255001.IcoutBVO;
import nc.vo.eh.trade.z0255001.IcoutVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明：出库单 
 * 单据类型：ZA11
 * @author 王兵 
 * 时间：2008-4-8 19:43:27
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn){
         case IEHButton.LOCKBILL:    //关闭单据
//             onBoLockBill();
             break;
		}
		super.onBoElse(intBtn);
	}

	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
		//非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();  
        int rows = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < rows; i++) 
        {	
        	UFDouble maxckamount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_8")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_8").toString());	               
            UFDouble ladingamount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().
            			getBodyValueAt(i, "ladingamount").toString());	            
            UFDouble outamount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "outamount")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().
            			getBodyValueAt(i, "outamount").toString());
            UFDouble yckamount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_6")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().
            			getBodyValueAt(i, "def_6").toString());
           
            
            if (outamount.toDouble()>0)
            {
            	 if(maxckamount.doubleValue()<outamount.doubleValue())
            	 {
                 	getBillUI().showErrorMessage("第("+(i+1)+"行)出库数量超过最大出库量,不允许出库!");
                 	return ;
                 }
            	 else
            	 {
            		 if(ladingamount.toDouble()>0)
            		 {      
            			 if (ladingamount.sub(yckamount).sub(outamount).toDouble()<0)
            			 {
            				 getBillUI().showErrorMessage("第("+(i+1)+"行)实际出库量有误，请检查!");
                          	return ;
            			 }
                     }
                     else if (ladingamount.toDouble()<0)
                     {	
                    	 if (ladingamount.sub(yckamount).sub(outamount).toDouble()>0 ||outamount.toDouble()>0)
            			 {
            				getBillUI().showErrorMessage("第("+(i+1)+"行)实际出库量有误，请检查!");
                          	return ;
            			 }
                     }
            	 }
            }else if (outamount.toDouble()<0)
            {
            	 if(ladingamount.toDouble()>0)
        		 {      
        			 if (ladingamount.sub(yckamount).sub(outamount).toDouble()<0  ||outamount.toDouble()<0)
        			 {
        				 getBillUI().showErrorMessage("第("+(i+1)+"行)实际出库量有误，请检查!");
                      	return ;
        			 }
                 }
                 else if (ladingamount.toDouble()<0)
                 {	
                	 if (ladingamount.sub(yckamount).sub(outamount).toDouble()>0 ||outamount.toDouble()>0)
        			 {
        				 getBillUI().showErrorMessage("第("+(i+1)+"行)实际出库量有误，请检查!");
                      	return ;
        			 }
                 }
            }else
            {
            	getBillUI().showErrorMessage("第("+(i+1)+"行)实际出库量为零，不允许出库!");
              	return ;
            }
           
        }
        PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//        /*---------------------------------------------------------
        
		Object vSourceBillType = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject();
		if(vSourceBillType!=null && vSourceBillType.equals(IBillType.eh_z0206510)){
	        IcoutVO icVO = (IcoutVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
	        String pk_ladingbill = icVO.getVsourcebillid();
	        /**当出库单上游单据提货单不是本人并且没有运输合同时不允许出库 add by wb at 2008-11-18 16:08:00*/
	        StringBuffer sql = new StringBuffer()
	        .append(" select pk_yscontracts,self_flag from eh_ladingbill where pk_ladingbill = '"+pk_ladingbill+"' ");
	        IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()==1){
				HashMap hm = (HashMap)arr.get(0);
				String pk_yscontracts = hm.get("pk_yscontracts")==null?null:hm.get("pk_yscontracts").toString();
				String self_flag = hm.get("self_flag")==null?"N":hm.get("self_flag").toString();
				if(self_flag.equals("N")&&(pk_yscontracts==null||pk_yscontracts.length()==0)){
					getBillUI().showErrorMessage("上游提货通知单不是本人,没有做运输合同不允许保存!");
					return;
				}
			}
	        /************************** end ***********************/
	        
	        IcoutBVO[] icoutBVOs = (IcoutBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
	        if(icoutBVOs!=null&&icoutBVOs.length>0){
	        	
				String info = pubItf.checkData(icoutBVOs);
				if(info!=null&&info.length()>0){
					getBillUI().showErrorMessage(info);
	            	return ;
				}
	        }
	       }
		//表头累计的金额
		IcoutBVO[] bvos=(IcoutBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		UFDouble summony=new UFDouble(0);
		for(int i=0;i<bvos.length;i++){
			UFDouble taxinmony=bvos[i].getDef_7()==null?new UFDouble(0):bvos[i].getDef_7();
			summony=taxinmony.add(summony);
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("summoney",summony);
		
//		************************出库数量和入库数量的PC比较处理
		for(int i=0;i<bvos.length;i++){
			String pk_cprk_bs= getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"pk_cprk_b")==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"pk_cprk_b").toString();
			String pk_invbasdoc=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc")==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc").toString();
			UFDouble outamount=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"outamount")==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"outamount").toString());
			if(pk_cprk_bs!=null&&pk_cprk_bs.length()>0){
				String sql= "select sum(isnull(rkmount,0))-sum(isnull(ckamount,0)) wcamount from eh_sc_cprkd_b where pk_invbasdoc='"+pk_invbasdoc+"' and  pk_rkd_b in ("+pk_cprk_bs+")";
				UFDouble wcamount = new UFDouble(0);
				IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					Object wcamountstr = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
					wcamount = wcamountstr==null?new UFDouble(0):new UFDouble(wcamountstr.toString());			//所选批次未出库数量
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(outamount.sub(wcamount).toDouble()>0){
					getBillUI().showErrorMessage("第"+(i+1)+"行所选入库单批次未出库量不能满足当前出库数量,请再添加批次!");
					return;
				}
			}
		}
		
//		//************************************************************************end
		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		IcoutVO icVO = (IcoutVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
	    String vsourcebillid = icVO.getVsourcebillid();
	    if(vSourceBillType!=null && vSourceBillType.equals(IBillType.eh_z0206510)){
	    	String sql ="select * from eh_ladingbill where pk_ladingbill ='"+vsourcebillid+"' and NVL(dr,0)=0 and ck_flag = 'Y'";
        	
        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
        	if (al.size()>0)
        	{
        		getBillUI().showErrorMessage("来源单据已被使用,请取消操作!");
                return;
        	}
	    }
	    if(vSourceBillType!=null &&vSourceBillType.equals(IBillType.eh_z0502005))
	    {
	    	String sql ="select * from eh_stock_checkreport where pk_checkreport='"+vsourcebillid+"' and NVL(dr,0)=0 and rk_flag='Y'";
        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
        	if (al.size()>0)
        	{
        		getBillUI().showErrorMessage("来源单据已被使用,请取消操作!");
                return;
        	}
	    }
		 // 先进行保存
		super.onBoSave();
		 // 关闭提货通知单子表
		
		if(vSourceBillType!=null && vSourceBillType.equals(IBillType.eh_z0206510)){
//			IcoutVO icVO = (IcoutVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		    String pk_ladingbill = icVO.getVsourcebillid();
			int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
	        for (int i = 0; i < row; i++) {	
	        	String pk_ladingbill_b=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid")==null?"":
	            						getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString();	
		        StringBuffer hxsql = new StringBuffer()
		        .append(" select sum(isnull(zamount,0)) amount,'A' flag from eh_ladingbill_b where pk_ladingbill_b = '"+pk_ladingbill_b+"' and isnull(dr,0)=0 ")  // 提货通知单子表提货量
		        .append(" union all")
		        .append(" select sum(isnull(outamount,0)) amount,'B' flag from eh_icout_b where vsourcebillid = '"+pk_ladingbill_b+"'  and isnull(dr,0)=0 ");  // 已经出库的总数
		        ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(hxsql.toString(),new MapListProcessor());
		        if(arr!=null&&arr.size()>0){
    				HashMap hma = new HashMap();
    				for(int j=0; j<arr.size(); j++){
    					HashMap hm=(HashMap)arr.get(j);
    					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
    					UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
    					hma.put(flag, amount);
    				}
    				String updateSQL = null;
    				if(hma.get("A").equals(hma.get("B"))){  //全部出库时将提货通知单关闭
    					updateSQL = "update eh_ladingbill_b set isfull = 'Y' where pk_ladingbill_b = '"+pk_ladingbill_b+"' and isnull(dr,0)=0";
    				}
    				else{
    					updateSQL = "update eh_ladingbill_b set isfull = 'N' where pk_ladingbill_b = '"+pk_ladingbill_b+"' and isnull(dr,0)=0";
    				}
    				pubItf.updateSQL(updateSQL);
    			}
	        }
        	//关闭提货通知单
            StringBuffer hxsql = new StringBuffer()
            .append(" select count(*) amount,'A' flag from eh_ladingbill_b where pk_ladingbill = '"+pk_ladingbill+"' and isnull(dr,0)=0 ")  // 提货通知单子表数量
            .append(" union all")
            .append(" select count(*) amount,'B' flag from eh_ladingbill_b where pk_ladingbill = '"+pk_ladingbill+"' and isnull(isfull,'N')='Y' and isnull(dr,0)=0 ");  // 已经出库的总数
        	try {
        		ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(hxsql.toString(),new MapListProcessor());
    			if(arr!=null&&arr.size()>0){
    				HashMap hma = new HashMap();
    				for(int i=0; i<arr.size(); i++){
    					HashMap hm=(HashMap)arr.get(i);
    					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
    					UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
    					hma.put(flag, amount);
    				}
    				String updateSQL = null;
    				if(hma.get("A").equals(hma.get("B"))){  //全部出库时将提货通知单关闭
    					updateSQL = "update eh_ladingbill set ck_flag = 'Y' where pk_ladingbill = '"+pk_ladingbill+"' and isnull(dr,0)=0";
    				}
    				else{
    					updateSQL = "update eh_ladingbill set ck_flag = 'N' where pk_ladingbill = '"+pk_ladingbill+"' and isnull(dr,0)=0";
    				}
    				pubItf.updateSQL(updateSQL);
    			}
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        	
        	/*********对销售订单的关闭  对MRP运算的处理，上游为未完全出库的销售订单 add by wb 2009-2-18 13:17:45**************/
        	if(isFromOrderbill(pk_ladingbill)){
        		colseOrderBill(icVO);
        	}
        	/********************end****************/
        }
		if(vSourceBillType.equals(IBillType.eh_z0502005)){			//检测报告
            //回写 
			//IcoutVO icVO = (IcoutVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		    String pk_checkreport = icVO.getVsourcebillid();
            if(pk_checkreport!=null&&pk_checkreport.length()>0){
            	String sql="update eh_stock_checkreport set rk_flag='Y' where pk_checkreport =  '"+pk_checkreport+"'";
            	pubItf.updateSQL(sql);
            }
       }
		onBoRefresh();
	}
        
	protected void onBoDelete() throws Exception {
        if(onBoDeleteN()==0){
           return;
        }       
    	AggregatedValueObject agg = getBillUI().getVOFromUI();
        IcoutVO svo = (IcoutVO) agg.getParentVO();
        //add by houcq 2010-11-26解决删除报错问题
        //String vsourcebilltype  = svo.getVsourcebilltype();
        String vsourcebilltype = svo.getVsourcebilltype()==null?"":svo.getVsourcebilltype();
        
        PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        //从成品入库单来的单据在保存的时候回写标记到is_fenj中 add by zqy 2008-6-12 10:39:56
        if(vsourcebilltype.equals(IBillType.eh_z0206510)){
        	
        	String pk_ladingbill = svo.getVsourcebillid();               // 入库单主键
            IcoutBVO[] lBVOs = (IcoutBVO[])agg.getChildrenVO();
            if(lBVOs!=null&&lBVOs.length>0){
            	int length = lBVOs.length;
            	String[] pk_ladingbill_bs = new String[length]; 
            	String pk_ladingbill_b = null;
            	for(int i=0; i<length; i++){
            		IcoutBVO lVO = lBVOs[i];
            		pk_ladingbill_b = lVO.getVsourcebillid();
            		pk_ladingbill_bs[i] = pk_ladingbill_b;
            	}
            	pk_ladingbill_b = PubTools.combinArrayToString(pk_ladingbill_bs);
            	String sql = "update eh_ladingbill_b set isfull = 'N' where pk_ladingbill_b in "+pk_ladingbill_b;
	            pubItf.updateSQL(sql);
            }
            String updateSQL = "update eh_ladingbill set ck_flag = 'N' where pk_ladingbill = '"+pk_ladingbill+"' and isnull(dr,0)=0";
    		pubItf.updateSQL(updateSQL);
    	 }
        if(vsourcebilltype.equals(IBillType.eh_z0502005)){			//检测报告
            //回写 
			IcoutVO icVO = (IcoutVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		    String pk_checkreport = icVO.getVsourcebillid();
            if(pk_checkreport!=null&&pk_checkreport.length()>0){
            	String sql="update eh_stock_checkreport set rk_flag='N' where pk_checkreport =  '"+pk_checkreport+"'";
            	pubItf.updateSQL(sql);
            }
       }
        super.onBoTrueDelete();
    }
     
	
	public void onButton_N(ButtonObject bo, BillModel model) {
        super.onButton_N(bo, model);
        String bocode=bo.getCode()==null?"":bo.getCode();
        //当从提货通知单生成出库单时，客户和发票号不允许编辑，同时表体不能进行行操作
        if(bocode.equals("提货通知单")){
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_ladingbill").setEnabled(false);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").setEnabled(false);
          int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
          for(int i=0;i<row;i++){
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vcode", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"secondcount", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"firstcount", false); 
              UFDouble ladingamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount")==null?"0":
            	  						getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount").toString());
              UFDouble youtamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_6")==null?"0":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_6").toString());
              UFDouble price = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "price")==null?"0":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "price").toString());
              UFDouble fist=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vfirst")==null?"0":
            	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vfirst").toString());
          	  UFDouble sec=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vsecond")==null?"0":
          		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vsecond").toString());
	          UFDouble outamout = ladingamount.sub(youtamount);
	          UFDouble rate = outamout.div(ladingamount);
	          fist = fist.multiply(rate);
	          sec = sec.multiply(rate);
	          getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(outamout, i, "outamount");
	          getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(fist, i, "firstdiscount");
	          getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(sec, i, "seconddiscount");
	          getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(outamout.multiply(price).sub(fist).sub(sec), i, "def_7");
          }
          getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
          getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
          getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
          getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
          getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
          
          String[] formual = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getEditFormulas();//获取编辑公式
          getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(formual);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_yscontracts").setEnabled(false);
          getBillUI().updateUI();
        }
        if(bocode.equals("检测报告")){
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_ladingbill").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").setEnabled(false);
            int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for(int i=0;i<row;i++){
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vcode", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"secondcount", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"firstcount", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"outamount", false); 
            }
            getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
            getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
            getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
            getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
            getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
            
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_yscontracts").setEnabled(false);
            getBillUI().updateUI();
          }
    }
	
	@Override
	protected void onBoEdit() throws Exception {
		 String pk_cubasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
	        YsContractRefModel.pk_cubasdoc = pk_cubasdoc;  //将客商传到参照中
		super.onBoEdit();
	}
	
	
	/***
	 * 关闭订单
	 * wb 2009-2-18 14:59:06
	 * @param icVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void colseOrderBill(IcoutVO icVO) throws Exception{
		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
	    String pk_ladingbill = icVO.getVsourcebillid();			//提货通知单主键
	    String pk_order = getPk_order(pk_ladingbill);			//销售订单主键
	    HashMap hmOrder = getPk_order_bs(pk_ladingbill);
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < row; i++) {	
        	String pk_ladingbill_b=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid")==null?"":
            						getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString();	
	        String pk_order_b = hmOrder.get(pk_ladingbill_b)==null?"":hmOrder.get(pk_ladingbill_b).toString();
        	StringBuffer hxsql = new StringBuffer()
	        .append(" select sum(isnull(fzamount,0)) amount,'A' flag from eh_order_b where pk_order_b = '"+pk_order_b+"' and isnull(dr,0)=0 ")  			//销售订单子表数量
	        .append(" union all")
	        .append(" select sum(isnull(outamount,0)) amount,'B' flag from eh_icout_b where vsourcebillid in (select pk_ladingbill_b from eh_ladingbill_b where vsourcebillid = '"+pk_order_b+"' and isnull(dr,0)=0)  and isnull(dr,0)=0 ");  // 已经出库的总数
	        ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(hxsql.toString(),new MapListProcessor());
	        if(arr!=null&&arr.size()>0){
				HashMap hma = new HashMap();
				for(int j=0; j<arr.size(); j++){
					HashMap hm=(HashMap)arr.get(j);
					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
					UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
					hma.put(flag, amount);
				}
				UFDouble orderamount = new UFDouble(hma.get("A")==null?"0":hma.get("A").toString());			//销售订单数量
				UFDouble outamount = new UFDouble(hma.get("B")==null?"0":hma.get("B").toString());				//出库数量
				String updateSQL = null;
				if(outamount.sub(orderamount).toDouble()>=0){  //全部出库时将订单子表关闭
					updateSQL = "update eh_order_b set ck_flag = 'Y' where pk_order_b = '"+pk_order_b+"' and isnull(dr,0)=0";
				}
				else{
					updateSQL = "update eh_order_b set ck_flag = 'N' where pk_order_b = '"+pk_order_b+"' and isnull(dr,0)=0";
				}
				pubItf.updateSQL(updateSQL);
			}
        }
    	//关闭提货通知单
        StringBuffer hxsql = new StringBuffer()
        .append(" select count(*) amount,'A' flag from eh_order_b where pk_order = '"+pk_order+"' and isnull(dr,0)=0 ")  // 订单子表数量
        .append(" union all")
        .append(" select count(*) amount,'B' flag from eh_order_b where pk_order = '"+pk_order+"' and isnull(ck_flag,'N')='Y' and isnull(dr,0)=0 ");  //已经出库的总数
    	try {
    		ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(hxsql.toString(),new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				HashMap hma = new HashMap();
				for(int i=0; i<arr.size(); i++){
					HashMap hm=(HashMap)arr.get(i);
					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
					UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
					hma.put(flag, amount);
				}
				String updateSQL = null;
				if(hma.get("A").equals(hma.get("B"))){  //全部出库时将订单关闭
					updateSQL = "update eh_order set lock_flag = 'Y' where pk_order = '"+pk_order+"' and isnull(dr,0)=0";
				}
				else{
					updateSQL = "update eh_order set lock_flag = 'N' where pk_order = '"+pk_order+"' and isnull(dr,0)=0";
				}
				pubItf.updateSQL(updateSQL);
			}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
	}
	
	/***
	 * 根据提货通知单主键得到对应的销售订单子表主键
	 * wb 2009-2-18 13:44:31
	 * @param pk_ladingbill
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap getPk_order_bs(String pk_ladingbill) throws Exception{
		HashMap hm = new HashMap();
		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		StringBuffer sql = new StringBuffer()
		.append("  SELECT pk_ladingbill_b,vsourcebillid FROM eh_ladingbill_b ")
		.append("  WHERE pk_ladingbill = '"+pk_ladingbill+"'")
		.append("  AND ISNULL(dr,0) = 0");
		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		if(arr!=null&&arr.size()>0){
			String pk_ladingbill_b = null;			//提货通知单子表主键
			String pk_order_b = null;				//销售订单子表主键
			for(int i=0;i<arr.size();i++){
				HashMap hmA = (HashMap)arr.get(i);
				pk_ladingbill_b = hmA.get("pk_ladingbill_b")==null?"":hmA.get("pk_ladingbill_b").toString();
				pk_order_b = hmA.get("vsourcebillid")==null?"":hmA.get("vsourcebillid").toString();
				hm.put(pk_ladingbill_b, pk_order_b);
			}
		}
		return hm;
	}
	
	/***
	 * 判断上游提货单是不是从订单过来
	 * wb 2009-2-18 13:32:13
	 * @param pk_ladingbill
	 * @return
	 * @throws Exception
	 */
	public boolean isFromOrderbill(String pk_ladingbill) throws Exception{
		String sql = "SELECT vsourcebilltype  FROM eh_ladingbill WHERE pk_ladingbill = '"+pk_ladingbill+"'";
		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		Object obj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
		if(obj!=null&&obj.toString().length()>0){
			return true;
		}
		return false;
	}
	
	/***
	 * 根据提货通知单主键找到上游订单主键
	 * wb 2009-2-18 13:32:13
	 * @param pk_ladingbill
	 * @return
	 * @throws Exception
	 */
	public String getPk_order(String pk_ladingbill) throws Exception{
		String sql = "SELECT vsourcebillid  FROM eh_ladingbill WHERE pk_ladingbill = '"+pk_ladingbill+"'";
		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		Object obj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
		if(obj!=null&&obj.toString().length()>0){
			return obj.toString();
		}
		return null;
	}
}