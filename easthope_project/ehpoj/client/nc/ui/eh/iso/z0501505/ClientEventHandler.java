package nc.ui.eh.iso.z0501505;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.lock.LockBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.iso.z0501505.StockSampleVO;
import nc.vo.eh.pub.Toolkits;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明：抽样单 
 * @author 张起源 
 * 时间：2008-4-11
 */
@SuppressWarnings("deprecation")
public class ClientEventHandler extends AbstractEventHandler {
    int addflag = 0;                    //单据增加来源 0 自制  1 收货通知单 2 司磅单
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@SuppressWarnings({ "unchecked" })
    public void onBoSave() throws Exception {
		
		// 非空判断
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//判断表体不能为空
		 getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
	        BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
	        if (model != null) {
	            int rowCount = model.getRowCount();
	            if (rowCount < 1) {
	                NCOptionPane.showMessageDialog(getBillUI(), "表体行不能为空!");
	                return;
	            }
	        }
		 //唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_project"});
        if(res==1){
            getBillUI().showErrorMessage("检测项目已经存在，不允许操作!");
            return;
        } 
        
        /*********** Editor houcq 2011-11-03 Start ***********/
        AggregatedValueObject agg = getBillUI().getVOFromUI();
        StockSampleVO report=(StockSampleVO) agg.getParentVO();
		//加锁，防止同时操作并发。
		try {
			
			if(!Toolkits.isEmpty(report.getVsourcebillid())){
				String sourcebillid = report.getVsourcebillid().toString();
				boolean bLockSuccess = LockBO_Client.lockPK(sourcebillid, _getOperator(),null);
				if (!bLockSuccess) {					
					getBillUI().showWarningMessage("来源单据已被使用,请取消操作!");					
					return;
				}
			}
			/*//初始化按钮处理
				public static final int OP_INIT=4;
				//编辑时按钮处理
				public static final int OP_EDIT = 0;
				//单据新增状态
				public static final int OP_ADD = 1;
				//非编辑时按钮处理
				public static final int OP_NOTEDIT = 2;
				//参照单据时新增单据状态
				public static final int OP_REFADD = 3;
				//不能增加、只能修改非编辑时按钮处理
				public static final int OP_NOADD_NOTEDIT = 5;
				//不能增加、修改时按钮处理
				public static final int OP_NO_ADDANDEDIT = 6;
				//所有的功能都可用
				public static final int OP_ALL=7;
			*/
			int ret= getBillUI().getBillOperate();
			if (ret!=0)
			{
				UFDouble rkamount = new UFDouble(report.getRkamount()==null?"0":report.getRkamount().toString());
	            if(rkamount.equals(0)){
	                getBillUI().showErrorMessage("代表数量不能为空，请核实！");
	                return;
	            }				
				//<修改>功能：vsourcebilltype做NULL判断if条件用。日期：2009-08-11作者：张志远。
		        String vsourcebilltype  = report.getVsourcebilltype()==null?"":report.getVsourcebilltype();  
		        //当来自于司磅单保存数据时将司磅单中已司磅标记加上   add by wb at 2008-6-7 21:06:45
		        if(vsourcebilltype.equals(IBillType.eh_z06005)){
		        	StockSampleVO smpleVO = (StockSampleVO)getBillUI().getChangedVOFromUI().getParentVO();
		        	String pk_sbbills = smpleVO.getPk_sbbills();       // 司磅单的主键组
		        	String sql ="select * from eh_sbbill where pk_sbbill in ("+pk_sbbills+") and NVL(dr,0)=0 and ycy_flag = 'Y'";
		        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		        	if (al.size()>0)
		        	{
		        		getBillUI().showErrorMessage("来源单据已被使用,请取消操作!");
		                return;
		        	}
		        	String updateSQL = "update eh_sbbill set ycy_flag = 'Y' where pk_sbbill in ("+pk_sbbills+") and NVL(dr,0)=0";
		        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		        	pubItf.updateSQL(updateSQL);
		        }
		          
		             
		        //当收货通知单中表体物料已被抽样过后，回写个标记到收货通知单中是否抽样字段中sfcy_flag  add by zqy 2008-6-8 15:12:51
		        if(vsourcebilltype.equals(IBillType.eh_z0151001)){
		        	//表体回写
		        	 String pk_receipt_b = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_receipt_b").getValueObject()==null?"":
		                 getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_receipt_b").getValueObject().toString(); //上游单据的字表
		        	PubItf pubitf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		        	String sql1 ="select * from eh_stock_receipt_b where pk_receipt_b = '"+pk_receipt_b+"' and NVL(dr,0)=0 and rk_flag = 'Y'";
		        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		        	ArrayList al1=(ArrayList) iUAPQueryBS.executeQuery(sql1, new MapListProcessor());
		        	if (al1.size()>0)
		        	{
		        		getBillUI().showErrorMessage("来源单据已被使用,请取消操作!");
		                return;
		        	}
		        	
		        	 String sql2="update eh_stock_receipt_b set rk_flag = 'Y' where pk_receipt_b ='"+pk_receipt_b+"'";
		        	 pubitf.updateSQL(sql2);
		        	//表头回写
		        	
		            String pk_receipt = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?"":
		                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString(); //上游单据的主键
		        	
		            String sql4 ="select * from eh_stock_receipt where pk_receipt = '"+pk_receipt+"' and NVL(dr,0)=0 and yjcy_flag = 'Y'";
		        	ArrayList al2=(ArrayList) iUAPQueryBS.executeQuery(sql4, new MapListProcessor());
		        	if (al2.size()>0)
		        	{
		        		getBillUI().showErrorMessage("来源单据已被使用,请取消操作!");
		                return;
		        	}
		        	String sql="select count(*) amount,'A' flag  from eh_stock_receipt_b where (NVL(issb,'N')='N' or " +
		        			" issb='') and (NVL(sfcy_flag,'N')='N' or sfcy_flag ='') and NVL(dr,0)=0 and allcheck='Y' " +
		        			" and pk_receipt='"+pk_receipt+"' union all select count(*) amount,'B' flag  from eh_stock_receipt_b " +
		        			" where (NVL(issb,'N')='N' or issb='') and (NVL(sfcy_flag,'N')='N' or sfcy_flag ='') " +
		        			"  and NVL(dr,0)=0 and allcheck='Y' and rk_flag='Y' and  pk_receipt='"+pk_receipt+"' "; 
		           ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		           HashMap hm1=new HashMap();//放的做标记的个
			       HashMap hm2=new HashMap();//放的一共的个数
			       	 for(int i=0;i<al.size();i++){
			       		 HashMap hm=(HashMap) al.get(i);
			       		 UFDouble amountc =new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
			       		 String flagc=hm.get("flag")==null?"":hm.get("flag").toString();
			       		 if(flagc.equals("B")){
			       			 hm1.put(pk_receipt, amountc);
			       		 }
			       		 if(flagc.equals("A")){
			       			 hm2.put(pk_receipt, amountc);
			       		 }
			       	 }
			       	 double amountflag=new UFDouble(hm1.get(pk_receipt)==null?"-1000":hm1.get(pk_receipt).toString()).doubleValue();
			   		 double amount=new UFDouble(hm2.get(pk_receipt)==null?"-2000":hm2.get(pk_receipt).toString()).doubleValue();
			   		 if(amountflag==amount){
		    			 String sql3="update eh_stock_receipt set yjcy_flag='Y' where pk_receipt='"+pk_receipt+"'";
		    			 pubitf.updateSQL(sql3);
		    		 }			          
		        }	
		        super.onBoSave();
			}
			else
			{
				super.onBoSave();
			}
	        
		} finally {
			if(!Toolkits.isEmpty(report.getVsourcebillid())){
				String sourcebillid = report.getVsourcebillid().toString();
				LockBO_Client.freePK(sourcebillid, _getOperator(), null);
			}
		}
		/*********** Editor houcq 2011-11-03 end ***********/
	}
	
	public void onButton_N(ButtonObject bo, BillModel model) {
        
        super.onButton_N(bo, model);
        String bocode=bo.getCode();
        //当从收货通知单生成抽样单时候，物料名称是不允许编辑的,不允许进行行操作
        if(bocode.equals("收货通知单")){
          addflag = 1 ;
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
          getBillCardPanelWrapper().getBillCardPanel().setHeadItem("type_flag", "1");
          getBillCardPanelWrapper().getBillCardPanel().setHeadItem("th_flag", "S");
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("rkamount").setEnabled(true);
          int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
          for(int i=0;i<row;i++){
        	  getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"itemno", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vitemname", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"ll_ceil", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"ll_limit", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"rece_ceil", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"rece_limit", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"def_2", false);
          }
        }
        //当从原料司磅单生成抽样单时候，物料名称和原料司磅单号是不允许编辑的，不允许进行行操作
        else if(bocode.equals("司磅单")){
        	addflag = 2;
	        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);	        	
                getBillCardPanelWrapper().getBillCardPanel().setHeadItem("type_flag", "1");
                getBillCardPanelWrapper().getBillCardPanel().setHeadItem("th_flag", "S");
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("rkamount").setEnabled(false);
	        	int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        	   for(int i=0;i<row;i++){
        		 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"itemno", false);
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"pk_project", false);
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"ll_ceil", false);
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"ll_limit", false);
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"rece_ceil", false);
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"rece_limit", false);
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"def_2", false);
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vitemname", false);
        	}
        }else if(bocode.equals("入库单")){
        	addflag = 2;
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);	 
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("carnum").setEnabled(true);	 
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("type_flag", "1");
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("th_flag", "R");
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("rkamount").setEnabled(false);
            
//            //入库数量
//            String pk_in=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?"":
//            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
//            
//            String pk_in=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?"":
//            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
//            String sql
            
            
            
            
        	int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
    	   for(int i=0;i<row;i++){
    		 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"itemno", false);
             getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"pk_project", false);
             getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"ll_ceil", false);
             getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"ll_limit", false);
             getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"rece_ceil", false);
             getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"rece_limit", false);
             getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"def_2", false);
             getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vitemname", false);
    	   }
//    	   String[] formual=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("custname").getLoadFormula();//获取编辑公式
//    	   getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
        } else if(bocode.equals("自制单据")){
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("th_flag", "Z");
        }
          getBillUI().updateUI();
        }
	
	public void onBoCommit() throws Exception {
		super.onBoCommit();
		super.setBoEnabled();
	}
	
    @Override
    protected void onBoDelete() throws Exception {
    	int res = onBoDeleteN(); // 1为删除 0为取消删除
    	if(res==0){
    		return;
    	}
		StockSampleVO smpleVO = (StockSampleVO)getBillUI().getChangedVOFromUI().getParentVO();
    	String vbilltype = smpleVO.getVsbbilltype()==null?"":smpleVO.getVsbbilltype();
    	//对来自于司磅单的抽样单删除时回写司磅单中已抽样标记  add by wb at 2008-6-7 22:06:22
    	if(vbilltype.equals(IBillType.eh_z06005)){
    	String pk_sbbills = smpleVO.getPk_sbbills();       // 司磅单的主键组
    	String updateSQL = "update eh_sbbill set ycy_flag = 'N' where pk_sbbill in ("+pk_sbbills+") and NVL(dr,0)=0";
    	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
    	pubItf.updateSQL(updateSQL);
    	}
        
         //对于来自提货通知单的抽样单删除时回写个标记到是否抽样字段中sfcy_flag add by zqy 2008-6-8 15:32:09
        AggregatedValueObject agg = getBillUI().getVOFromUI();
        StockSampleVO report=(StockSampleVO) agg.getParentVO();
        String vsourcebilltype  = report.getVsourcebilltype()==null?"":report.getVsourcebilltype();
        if(vsourcebilltype.equals(IBillType.eh_z0151001)){
        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        	String pk_receipt=report.getPk_receipt()==null?"":report.getPk_receipt().toString();
        	String pk_receipt_b=report.getPk_receipt_b()==null?"":report.getPk_receipt_b().toString();
        	String sql2="update eh_stock_receipt set yjcy_flag='N' where pk_receipt='"+pk_receipt+"'";
        	String sql3="update eh_stock_receipt_b set rk_flag='N' where pk_receipt_b='"+pk_receipt_b+"'";
        	pubItf.updateSQL(sql2);
        	pubItf.updateSQL(sql3);
//            String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?"":
//                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
//            String pk_receipt = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?"":
//                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString(); //上游单据的主键
//            
//            PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
//            pubItf.deletesfcyFlag(pk_invbasdoc,pk_receipt);
//            
//            //删除来自提货通知单的抽样单时，对标记数量和总数量进行判断，不相等对表头回写 add by zqy 2008-6-11 15:06:04
//            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//            StringBuffer sql2 = new StringBuffer()
//            .append(" select count(*) mount1 from eh_stock_receipt_b where pk_receipt='"+pk_receipt+"' and isnull(dr,0)=0 ");
//            UFDouble mount1 = null;
//            try {
//                ArrayList arr2 = (ArrayList) iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
//                if(arr2!=null && arr2.size()>0){
//                    for(int j=0;j<arr2.size();j++){
//                        HashMap hm2 = (HashMap) arr2.get(j);
//                        mount1 = new UFDouble(hm2.get("mount1")==null?"0":hm2.get("mount1").toString()); //表体的总数量
//                    }
//                }               
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            
//            StringBuffer sql = new StringBuffer() 
//            .append(" select count(*) mount from eh_stock_receipt_b where pk_receipt='"+pk_receipt+"' " +
//                    "and sfcy_flag ='Y' and isnull(dr,0)=0 ");            
//            UFDouble mount =null;
//            try {
//                ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
//                if(arr!=null && arr.size()>0){
//                    for(int i=0;i<arr.size();i++){
//                        HashMap hm1 = (HashMap) arr.get(i);
//                        mount = new UFDouble(hm1.get("mount")==null?"0":hm1.get("mount").toString()); //已经回写标记的数量
//                    }
//                }                
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            
//            if(mount1.compareTo(mount)>0){
//                String sql3 = "update eh_stock_receipt set yjcy_flag='N' where pk_receipt='"+pk_receipt+"' and isnull(dr,0)=0 ";
//                pubItf.updateSQL(sql3);
//            }
        }
    	super.onBoTrueDelete();
    }    	
    
    protected void onBoElse(int intBtn) throws Exception {
        // TODO Auto-generated method stub
        switch (intBtn)
        {
            case IEHButton.LOCKBILL:    //关闭单据
                onBoLockBill();
                break;
            case IEHButton.Prev:    //上一页 下一页
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:    //上一页 下一页
                onBoBrows(intBtn);
                break;
        }   
    }
    
    private void onBoBrows(int intBtn) throws java.lang.Exception {
        // 动作执行前处理
        buttonActionBefore(getBillUI(), intBtn);
        switch (intBtn) {
        case IEHButton.Prev: {
            getBufferData().prev();
            break;
        }
        case IEHButton.Next: {
            getBufferData().next();
            break;
        }
        }
        // 动作执行后处理
        buttonActionAfter(getBillUI(), intBtn);
        getBillUI().showHintMessage(
                nc.ui.ml.NCLangRes.getInstance()
                        .getStrByID(
                                "uifactory",
                                "UPPuifactory-000503",
                                null,
                                new String[] { nc.vo.format.Format
                                        .indexFormat(getBufferData()
                                                .getCurrentRow()+1) })/*
                                                                     * @res
                                                                     * "转换第:" +
                                                                     * getBufferData().getCurrentRow() +
                                                                     * "页完成)"
                                                                     */
                        );
          setBoEnabled();
    }
     
     protected void onBoLockBill() throws Exception{
//       SuperVO parentvo = (SuperVO)getBillUI().getChangedVOFromUI().getParentVO();
//       String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
//            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
       AggregatedValueObject aggvo = getBillUI().getVOFromUI();
       StockSampleVO ivo = (StockSampleVO) aggvo.getParentVO();
       String lock_flag = ivo.getLock_flag()==null?"N":ivo.getLock_flag().toString();
       String primaryKey = ivo.getPrimaryKey();
       if(lock_flag.equals("Y")){
           getBillUI().showErrorMessage("该单据已经关闭!");
           return;
       }
       else if(!primaryKey.equals("")){
           int iRet = getBillUI().showYesNoMessage("是否确定进行关闭操作?");
           if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
               IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
               ivo.setAttributeValue("lock_flag", new UFBoolean(true));
               ivoPersistence.updateVO(ivo);
               getBillUI().showWarningMessage("已经关闭成功");
               onBoRefresh();
           }
           else{
               return;
           }
       }
   }
    
}

    
