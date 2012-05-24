package nc.ui.eh.stock.z0151001;


import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.pub.PubTool;
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.stock.z0151001.StockReceiptBVO;
import nc.vo.eh.stock.z0151001.StockReceiptVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：收货通知单
 * @author 王明
 * 2008-03-24 下午04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {
	
	public static int flag = 0;            // 增加单据标记(自制 1,从采购合同 2)
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	 @Override
	protected void onBoElse(int intBtn) throws Exception {
		 switch (intBtn)
	        {
	            case IEHButton.CONGEAL:    //批量终止单据
	                onBoConGenal();
	                break;
	        }
	        super.onBoElse(intBtn);
	    }
	    
	
	 @SuppressWarnings("static-access")
		private void onBoConGenal() {
			CalcDialog calcDialog = new CalcDialog();
			calcDialog.showModal();
			String pk_period = calcDialog.pk_period;
			String pk_store = calcDialog.pk_store;
	        if(pk_period!=null&&pk_period.length()>0){
				IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
//					StordocVO storVO = (StordocVO) iUAPQueryBS.retrieveByPK(StordocVO.class, pk_store);
					int ret = getBillUI().showYesNoMessage("你确认要重新计算"+perVO.getNyear()+"年"+perVO.getNmonth()+"月原料库存月报表吗？");
			        if (ret ==UIDialog.ID_YES){
			        	CalcKcybbVO kcVO = new CalcKcybbVO();
			        	kcVO.setPk_corp(_getCorp().getPk_corp());
			        	kcVO.setPk_period(pk_period);
			        	kcVO.setPk_store(pk_store);
			        	kcVO.setCoperatorid(_getOperator());
			        	kcVO.setCalcdate(_getDate());
			        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
			        	pubItf.calcYLKCYBB(kcVO);
			        	//将数据显示到界面
			        	String whereSql = "  pk_period = '"+pk_period+"' and invtype = 'Y'  and pk_corp = '"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0 ";
			        	nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
		                SuperVO[] supervo = business.queryByCondition(CalcKcybbVO.class, whereSql);
		                getBufferData().clear();
		     	       // 增加数据到Buffer
		     	       addDataToBuffer(supervo);
		     	
		     	       updateBuffer();
		                 
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
			
		}
	@SuppressWarnings("unchecked")
	@Override
    public void onBoSave() throws Exception {
		//前台校验
		   BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
           
           int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","vsourcebillid"});
           if(res==1){
               getBillUI().showErrorMessage("物料有重复");
               return;
           }
        //对非空验证
   		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
   		
   	 //如果表体是否司磅全选的时候，给表头回写个标记yjsb_flag add by zqy 2008-6-13 11:30:50
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        StockReceiptBVO[] sbvo = (StockReceiptBVO[]) aggvo.getChildrenVO();
        int length = sbvo.length;
        ArrayList alY=new ArrayList();
        ArrayList alN=new ArrayList();
        ArrayList sbN=new ArrayList();
        ArrayList sbY=new ArrayList();
        ArrayList isallx=new ArrayList();
        StringBuilder tips=new StringBuilder("第");//add by houcq 2010-12-23
        for(int i=0;i<length;i++){
        	//add by houcq begini 2010-12-02判断该物料是否已经维护过仓库
        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        	String pk_invbasdoc=sbvo[i].getPk_invbasdoc()==null?"":sbvo[i].getPk_invbasdoc();
        	String sql="select def1 from bd_invmandoc  where pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and pk_invmandoc='"+pk_invbasdoc+"'";
        	ArrayList al= (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			if(al.size()==1){
				HashMap hm= (HashMap) al.get(0);
				if (null==hm.get("def1"))
				{
//					getBillUI().showErrorMessage("此物料仓库未维护，请先维护仓库再操作!");
//					return;		
					tips.append(i+1).append(",");
				}				
			}
			//add by houcq end 
            String issb = new UFBoolean(sbvo[i].getIssb()==null?"":sbvo[i].getIssb().toString()).toString();
            if(issb.equals("N")){
            	sbN.add("N");
            }
            if(issb.equals("Y")){
            	sbY.add("Y");
            }
        	 String allcheck=new UFBoolean(sbvo[i].getAllcheck()==null?"":sbvo[i].getAllcheck().toString()).toString();
             if(allcheck.equals("N")){
             	alN.add("N");
             }
             if(allcheck.equals("Y")){
             	alY.add("Y");
             }
             
             if((issb.equals("N")&&allcheck.equals("N")) ){
            	 isallx.add("Y");
             }
        }
        if (!"第".equals(tips.toString()))
        {
        	getBillUI().showErrorMessage(tips.append("行物料仓库未维护,请先维护仓库再操作!").toString());
        	return;
        }
        //检测的判断
		 if(sbY.size()==length){
	     	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("yjsb_flag",0 );
	     }else if(sbN.size()==length){
	     	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("yjsb_flag",2 );
	     }else{
	     	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("yjsb_flag",1 );
	     }
		 //司磅的判断
        if(alY.size()==length){
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("allcheck", 0);//全部是免检的
        }else if(alN.size()==length){
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("allcheck", 2);//全部是不要免检的
        }else{//同时存在两种情况的
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("allcheck", 1);//全部是不要免检的
        }
        //交叉情况的判断
        if(isallx.size()>0){
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("isallx", "Y");
        }else{
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("isallx", "N");
        }
        
        
        //当数量一致时对合同的关闭
        //vsourcebillrowid 存放上有主表PK
        //vsourcebillid 存放上游子表PK
		CircularlyAccessibleValueObject[] bvos=getBillUI().getVOFromUI().getChildrenVO();
		StockReceiptVO reVO = (StockReceiptVO)getBillUI().getVOFromUI().getParentVO();
		String vsourcebillid=reVO.getVsourcebillid()==null?"":reVO.getVsourcebillid().toString();
		StockReceiptBVO[] reBVO=(StockReceiptBVO[])getBillUI().getVOFromUI().getChildrenVO();
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());			                                                                           
		String vsourcebilltype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcetype").getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcetype").getValueObject().toString();//add by houcq 2011-03-16
		//if(!vsourcebillid.equals(""))
		if (!vsourcebillid.equals(""))
		{
			if(vsourcebilltype.equals("ZA17")){
//				数据库中的已有的数量（不包含本身）
		    	HashMap hm=new HashMap();//存放的是（上游子表PK，库中的数量）结合
		    	String [] pk_contact_b=new String [reBVO.length]; //上游子表主键的集合
		    	String [] pk_receipt_b=new String [reBVO.length];
		    	for(int i=0;i<reBVO.length;i++){
		    		pk_contact_b[i]=reBVO[i].getVsourcebillid()==null?" ":reBVO[i].getVsourcebillid().toString();
		    		pk_receipt_b[i]=reBVO[i].getPk_receipt_b()==null?" ":reBVO[i].getPk_receipt_b().toString();
		    	}
		    	String pk_contract_bs=PubTool.combinArrayToString(pk_contact_b);  
		    	String pk_receipt_bs=PubTool.combinArrayToString(pk_receipt_b);  
		    	
		    	String sql6="select vsourcebillid pk,sum(NVL(inamount,0)) outamount from  eh_stock_receipt_b " +
			        		"where vsourcebillid in "+pk_contract_bs+" and pk_receipt_b not in "+pk_receipt_bs+" and " +
			        				"NVL(dr,0)=0 group by  vsourcebillid";
		    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql6, new MapListProcessor());
		    	for(int i=0;i<al.size();i++){
		    		HashMap hmone=(HashMap) al.get(i);
		    		String pk=hmone.get("pk")==null?"":hmone.get("pk").toString();
		    		if(!pk.equals("")){
		    			UFDouble outamount=new UFDouble(hmone.get("outamount")==null?"":hmone.get("outamount").toString());
			    		hm.put(pk, outamount);
		    		}
		    		
		    	}
		    	for(int i=0;i<reBVO.length;i++){
		    		String vsourcebillid2=reBVO[i].getVsourcebillid()==null?"":reBVO[i].getVsourcebillid().toString();
		    		if(!vsourcebillid2.equals("")){
		    			UFDouble amount=new UFDouble(hm.get(vsourcebillid2)==null?"":hm.get(vsourcebillid2).toString());
		    			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amount, i, "ysamount");

		    			getBillUI().updateUI();
		    		}	
		    	}
		
		  
		        PubTools.fullChange(bvos, "ysamount","inamount","amount","vsourcebillrowid","vsourcebillid", "eh_stock_contract","eh_stock_contract_b","lock_flag","sh_flag","pk_contract","pk_contract_b",hm);
			}
			// add by houcq 2011-03-16回写标记lock_flag
			else
			{
				
				String cgjc ="update eh_stock_decision set lock_flag='Y' where pk_decision='"+vsourcebillid+"'" ;
				PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
				pubitf.updateSQL(cgjc);
			}
		}
		
	  	
		super.onBoSave();
		
   		
   		
   		
   		
   		
  // 上下游判断时候的处理及数量的校验(弃用)------------------------------------------------------------------------------- 		
		//对由采购合同生成的收货通知单收货数量进行验证 add by wb at 2008年5月14日18:31:29
//        StockReceiptVO reVO = (StockReceiptVO)getBillUI().getChangedVOFromUI().getParentVO();
//        String vsourcebillid = reVO.getVsourcebillid()==null?"":reVO.getVsourcebillid().toString(); //来源单据主键
//        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//        
//		if(vsourcebillid.length()>0){
//			StockReceiptBVO [] reBVOs=(StockReceiptBVO[]) getBillUI().getChangedVOFromUI().getChildrenVO();
//	        String [] pk_receit_b=new String [reBVOs.length];//本子表主键PK
//	        String [] pk_contract_b=new String [reBVOs.length];//上游子表的PK
//	        for(int i=0;i<reBVOs.length;i++){
//	        	pk_receit_b[i]=reBVOs[i].getPk_receipt_b()==null?"":reBVOs[i].getPk_receipt_b().toString();
//	        	pk_contract_b[i]=reBVOs[i].getVsourcebillid()==null?"":reBVOs[i].getVsourcebillid().toString();//::注意这的上游子表与主表的PK 带到下游的字段是相反的
//	        }
//	        String pk_receit_bs=PubTool.combinArrayToString(pk_receit_b);     
//	        String pk_contract_bs=PubTool.combinArrayToString(pk_contract_b);  
//	        String sql="select vsourcebillid pk,sum(isnull(inamount,0)) outamount from  eh_stock_receipt_b " +
//	        		"where vsourcebillid in  "+pk_contract_bs+" and pk_receipt_b not in "+pk_receit_bs+" and " +
//	        				"isnull(dr,0)=0 group by  vsourcebillid";
//	        ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
//	        HashMap hm=new HashMap();
//	        for(int i=0;i<al.size();i++){
//	        	HashMap hmone=(HashMap) al.get(i);
//	        	String opk_contract_b=hmone.get("pk")==null?"":hmone.get("pk").toString();
//	        	UFDouble outamount=new UFDouble(hmone.get("outamount")==null?"0":hmone.get("outamount").toString());
//	        	hm.put(opk_contract_b, outamount);
//	        }
//			int row=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
//			for(int i=0; i<row; i++){
//				UFDouble inamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "inamount")==null?"0":
//						            getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "inamount").toString()); // 本次收货数量
////				UFDouble ysamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ysamount")==null?"0":
////		            getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ysamount").toString());                 // 已收货数量
//				UFDouble amount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "amount")==null?"0":
//					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "amount").toString()); // 合同数量
//				String onepk_contract_b=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid")==null?"0":
//		            getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString(); // 本次收货数量
//				UFDouble newyscamount=new UFDouble(hm.get(onepk_contract_b)==null?"0":hm.get(onepk_contract_b).toString());
//				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(newyscamount, i, "ysamount");    	 
//				double subamount = inamount.add(newyscamount).sub(amount).doubleValue();   // 本次收货数量+已收货数量-合同数量
//			    if(subamount>0){
//			    	getBillUI().showErrorMessage("第"+(i+1)+"行收货数量超过合同数量,请确认本次收货数量!");
//			    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("", i, "inamount");
//                    return;			    	
//			    }
//			}
////			对合同的关闭 by add wm 2008年5月24日15:31:29
////			int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
//			StockReceiptBVO[] bvo= (StockReceiptBVO[]) getBillUI().getVOFromUI().getChildrenVO();
//			String pk_contract= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid")==null?"" :
//				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
//			//合同中的数量（表体所有的物料数量的和）
//			String sql2="select  sum(isnull(amount,0)) sumamount from eh_stock_contract_b where pk_contract='"+pk_contract+"' " +
//					"and isnull(dr,0)=0 group by pk_contract";
//			UFDouble sumamount=null;//合同中的数量
//			 
//			ArrayList alt=(ArrayList) iUAPQueryBS.executeQuery(sql2, new MapListProcessor());
//			if(alt!=null&&alt.size()!=0){
//				HashMap hmt=(HashMap) alt.get(0);
//				sumamount=new UFDouble(hmt.get("sumamount")==null?"0":hmt.get("sumamount").toString());
//			}
//			UFDouble suminadd=new UFDouble(0);//自己库的总值
//			UFDouble sumoutadd=new UFDouble(0);	//界面上的值
////			String pk_receipt=bvo[0].getPk_receipt_b()==null?"":bvo[0].getPk_receipt_b().toString();
//			String [] pk_contract_m=new String [bvo.length];//合同主表主键
//			String [] pk_receipt_b=new String [bvo.length];//收货通知单子表主键
//			for(int i=0;i<bvo.length;i++){
//				pk_contract_m[i]=bvo[i].getVsourcebillrowid()==null?"":bvo[i].getVsourcebillrowid().toString();
//				pk_receipt_b[i]=bvo[i].getPk_receipt_b()==null?"":bvo[i].getPk_receipt_b().toString();
//			}
//			String billids = Toolkits.combinArrayToString(pk_contract_m);//合同主表主键('','','')
//			String pk_rece_b=Toolkits.combinArrayToString(pk_receipt_b);//收货通知单子表主键('','','')
////			if(pk_receipt.equals("")&&al!=null&&al.size()!=0){
//				String sqlt="select sum(inamount) amount from eh_stock_receipt_b where vsourcebillrowid in "+billids+"  and isnull(dr,0)=0 and pk_receipt_b not in "+pk_rece_b+" group by vsourcebillid"; 
//				ArrayList alamount=(ArrayList) iUAPQueryBS.executeQuery(sqlt, new MapListProcessor());
//				for(int i=0;i<alamount.size();i++){
//					HashMap  hmy=(HashMap) alamount.get(i);
//					UFDouble amount=new UFDouble(hmy.get("amount")==null?"0":hmy.get("amount").toString());
//					suminadd=suminadd.add(amount);
//				}
//				for(int i=0;i<row;i++){
//					UFDouble inamount=bvo[i].getInamount()==null?new UFDouble():bvo[i].getInamount();
//					sumoutadd=sumoutadd.add(inamount);
//				}
//				UFDouble sum=suminadd.add(sumoutadd);
//				double  info=sumamount.sub(sum).doubleValue();//判断是否是已经收完
//				PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());			 
//				 pubitf.changeFlag("eh_stock_contract", "sh_flag", "pk_contract", pk_contract, 1);
//				if(info==0){		 
//					 pubitf.changeFlag("eh_stock_contract", "sh_flag", "pk_contract", pk_contract, 0); //	修改合同的标记
//				}
//			}	
////		}
   		//end_--------------------------------------------------------------------------------------------------
	}
	
	//add wangming 08.05.10 判断是否来自上下游 对字段的属性的编辑
	@Override
	public void onButton_N(ButtonObject bo, BillModel model) {
        super.onButton_N(bo, model);
 
        String bocode=bo.getCode()==null?"":bo.getCode().toString();
        if(bocode.equals("自制单据")){
          flag = 1;
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(true);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(true);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("summoney").setEnabled(true);
          
        } else if(bocode.equals("采购合同")){
        	flag = 2;
        	getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
//        	getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
        	getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
        	getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
        	getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
        	
        	try {
				AggregatedValueObject aggvo = this.getBillCardPanelWrapper().getBillVOFromUI();
				StockReceiptVO  srvo=(StockReceiptVO) aggvo.getParentVO();
				String retail_flag=srvo.getRetail_flag().toString();
				if(retail_flag.equals("Y")){
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("retailinfo").setEnabled(true);
				}else{
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("retailinfo").setEnabled(false);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else if (bocode.equals("修改")){
        	
        	 String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
             if(!coperatorid.equals(_getOperator())){
//                 getBillUI().showErrorMessage("不允许修改他人申请！");
                 return;
             }
    
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(true);
            String retail_flag=(String) (getBillCardPanelWrapper().getBillCardPanel().getHeadItem("retail_flag")==null?"":
            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("retail_flag").getValueObject());
            if(retail_flag.equals("true")){
            	 getBillCardPanelWrapper().getBillCardPanel().getHeadItem("retailinfo").setEnabled(true);
            }
            int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            
            for(int i=0;i<rows;i++){
            	getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvcode", true);
            } 
        }else if(bocode.equals("增行")){
        	int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(row,"vinvcode", true);
        }
        getBillUI().updateUI();
	}
	   @Override
	protected void onBoDelete() throws Exception {
		   
	   String pk_contract= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid")==null?"" :
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
	   int res = onBoDeleteN(); // 1为删除 0为取消删除
    	if(res==0){
    		return;
    	}
    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
   	 	//add by houcq 2011-03-16 begin
		 String vsourcebilltype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcetype").getValueObject().toString();
		 if (!"".equals(pk_contract))
		 {
			 if ("".equals(vsourcebilltype))
			 {
				 String cgjc ="update eh_stock_decision set lock_flag='N' where pk_decision='"+pk_contract+"'" ;
				 pubitf.updateSQL(cgjc); 
			 }
			 else
			 {
				//对表头标记的回写		 
				 pubitf.changeFlag("eh_stock_contract", "lock_flag", "pk_contract", pk_contract, 1); 
				 //对表体标记的回写
				 int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
				 String[] pk_contract_bs=new String[rows]; 
				 for(int i=0;i<rows;i++){
					 pk_contract_bs[i]=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid")
					 ==null?"":getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString();
				 }
				 String pk_stock_contract = Toolkits.combinArrayToString(pk_contract_bs);
				 String sql="update eh_stock_contract_b set sh_flag='N' where pk_contract_b in"+pk_stock_contract;
				 pubitf.updateSQL(sql);
			 }			 
		 }  
		 //add by houcq end 
		 super.onBoTrueDelete();
	   }
	   
	   @Override
	protected void onBoEdit() throws Exception {
	   	getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		super.onBoEdit();
	}   
   }