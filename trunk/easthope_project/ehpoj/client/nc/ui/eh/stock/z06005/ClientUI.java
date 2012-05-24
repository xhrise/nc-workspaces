
package nc.ui.eh.stock.z06005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.stock.z06005.SbbillBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
功能：司磅单
作者：newyear
日期：2008-4-11 下午04:36:39
 */

@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {
	UIRefPane ref=null;
	private String vbilltype=null;
	public ClientUI(){
		super();
		ref=(UIRefPane) getBillCardPanel().getHeadItem("vbillno").getComponent();
	}

	public AbstractManageController createController() {
		return new ClientCtrl();
	}

	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
	

	@SuppressWarnings("deprecation")
	protected void initSelfData() {
		super.initSelfData();
	    //显示数据库中的0
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        // 设置千分位
        getBillCardWrapper().getBillCardPanel().getBodyPanel().setShowThMark(true);
		//司磅单类型
		getBillCardWrapper().initHeadComboBox("sbtype", ICombobox.STR_SBBILLTYPE,true);
		getBillListWrapper().initHeadComboBox("sbtype", ICombobox.STR_SBBILLTYPE,true);
		//司磅单取数方式
        getBillCardWrapper().initHeadComboBox("numbertype", ICombobox.STR_SBGETDATETYPE,true);
        getBillListWrapper().initHeadComboBox("numbertype", ICombobox.STR_SBGETDATETYPE,true);
        //单据状态(挂起)
//        getBillCardWrapper().initHeadComboBox("vbillstatus",ICombobox.STR_SBSTATUS, true);
//        getBillListWrapper().initHeadComboBox("vbillstatus",ICombobox.STR_SBSTATUS, true);
        
        //把表体Table设为不可见
//        getBillListPanel().getBodyUIPanel().setVisible(false);
	}

	public void setDefaultData() throws Exception {
		super.setDefaultData();
        //取数方式,默认为自动
        getBillCardPanel().getHeadItem("numbertype").setValue(new Integer(1));
        getBillCardPanel().getHeadItem("sbtype").setValue(new Integer(0));
        getBillCardPanel().getHeadItem("fullload").setEnabled(false);
        getBillCardPanel().getHeadItem("emptyload").setEnabled(false);
        this.getBillCardPanel().getHeadItem("numbertype").setEnabled(false);//将取数方式设置为不可编辑2009-12-08
//        getBillCardPanel().getHeadItem("vbillstatus").setValue(new Integer(0));
        
        getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false); 
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public void afterEdit(BillEditEvent e) {
		String strKey =e.getKey();
    	 if(e.getPos()==HEAD){
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
                getBillCardPanel().execHeadFormulas(formual);
            }else if (e.getPos()==BODY){
                String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            }else{
                getBillCardPanel().execTailEditFormulas();
           }
    	 //功能：根据是否取数设置取数方式的状态。时间：2009-12-08
		if("readflag".equals(strKey)){
			String readflag = this.getBillCardPanel().getHeadItem("readflag").getValueObject()==null?"false":
				this.getBillCardPanel().getHeadItem("readflag").getValueObject().toString();
			if("true".equals(readflag)){
				this.getBillCardPanel().getHeadItem("numbertype").setEnabled(true);
				this.getBillCardPanel().getHeadItem("readflag").setEnabled(false);
			}
		}
		UFDateTime ts = ClientEnvironment.getInstance().getServerTime();
		//add by houcq  begin 2011-05-12
		if("def_6".equals(strKey))
		{
			String qstype = getBillCardPanel().getHeadItem("numbertype").getValueObject()==null?
	                "0":getBillCardPanel().getHeadItem("numbertype").getValueObject().toString() ;
	         if("0".equalsIgnoreCase(qstype)){	
	        	 getBillCardPanel().setTailItem("def_2", ts); 
	         }
		}
		if("def_7".equals(strKey))
		{
			String qstype = getBillCardPanel().getHeadItem("numbertype").getValueObject()==null?
	                "0":getBillCardPanel().getHeadItem("numbertype").getValueObject().toString() ;
	         if("0".equalsIgnoreCase(qstype)){	
	        	 getBillCardPanel().setTailItem("def_3", ts); 
	 	         getBillCardPanel().setTailItem("endperson", _getOperator()); 
	         }
		}
	    //end     
         //取数方式:自动取数的时候净重与重车字段不可编辑。
         if("numbertype".equals(strKey)){
             String type = getBillCardPanel().getHeadItem(strKey).getValueObject()==null?
                    "0":getBillCardPanel().getHeadItem(strKey).getValueObject().toString() ;
             if("0".equalsIgnoreCase(type)){
            	 
            	 UFDouble def_6=new UFDouble(getBillCardPanel().getHeadItem("def_6").getValueObject()==null?"0"
               			 :getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
            	 UFDouble def_7=new UFDouble(getBillCardPanel().getHeadItem("def_7").getValueObject()==null?""
                  			 :getBillCardPanel().getHeadItem("def_7").getValueObject().toString());
                  	 if(def_6.toDouble()>0 && def_7.toDouble()>0){
                  		 getBillCardPanel().getHeadItem("def_6").setEnabled(false);
                         getBillCardPanel().getHeadItem("def_7").setEnabled(false);
                  	 }else{
                  		getBillCardPanel().getHeadItem("def_6").setEnabled(true);
                        getBillCardPanel().getHeadItem("def_7").setEnabled(true);
                  	 }
             }else{
                 getBillCardPanel().getHeadItem("def_6").setEnabled(false);
                 getBillCardPanel().getHeadItem("def_7").setEnabled(false);
             }
         }
         
         //司磅单类型{"原材料","成品","其它"};
         if ("sbtype".equals(strKey)){
        	 clearHead();
             String sbtype = e.getValue()==null?"":e.getValue().toString();
             changeRefModel(sbtype);
             ref.getRef().getRefModel().reloadData();					//每选择司磅类型后重新加载参照中数据 add by wb at 2008-10-7 16:26:01
             //清空表体
             int rowcount=getBillCardPanel().getRowCount();
             int[] rowss=new int[rowcount];
             for(int i=rowcount - 1;i>=0;i--){
                 rowss[i]=i;
             }
             getBillCardPanel().getBillModel().delLine(rowss);
             
             if("原料司磅".equals(sbtype)){
            	 clearHead();
                 getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
                 getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
                 getBillCardPanel().getBodyItem("vcubasdoc").setEnabled(false);
                 getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
             }
             if("成品司磅".equals(sbtype)){
            	 clearHead();
                 getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
                 getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
                 getBillCardPanel().getBodyItem("vcubasdoc").setEnabled(false);
             }
             if("其他司磅".equals(sbtype)){
            	 clearHead();
                 getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
                 getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
                 getBillCardPanel().getBodyItem("vcubasdoc").setEnabled(false);
             }
         } 
         if("vbillno".equals(strKey)){//原料 
        	 String vsourcebillrowid=getBillCardPanel().getHeadItem("vbillno").getValueObject()==null?"":
            	 getBillCardPanel().getHeadItem("vbillno").getValueObject().toString();
        	 if(IBillType.eh_z0151001.equals(vbilltype)){//收货通知单
        		 getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0151001);
        		 //String sql2="select b.pk_receipt_b,a.pk_receipt,a.vbilltype,b.pk_invbasdoc,a.pk_cubasdoc,b.packagweight,a.carnumber,a.tranno,a.retailinfo,b.item,b.packagweight*b.item sum,b.allcheck" +
        		 //modify by houcq 2011-03-07带出收货通知单时增加含税单价
        		 String sql2="select b.taxinprice,b.pk_receipt_b,a.pk_receipt,a.vbilltype,b.pk_invbasdoc,a.pk_cubasdoc,b.packagweight,a.carnumber,a.tranno,a.retailinfo,b.item,b.packagweight*b.item sum,b.allcheck" +
        		 		" from eh_stock_receipt a,eh_stock_receipt_b b where a.pk_receipt=b.pk_receipt and pk_receipt_b='"+vsourcebillrowid+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0";
        		 //String sql="select b.pk_invbasdoc,a.pk_cubasdoc,b.pk_receipt_b pk ,a.billno,b.pk_unit pk_measdoc ," +
        		 String sql="select b.taxinprice,b.pk_invbasdoc,a.pk_cubasdoc,b.pk_receipt_b pk ,a.billno,b.pk_unit pk_measdoc ," +
        		 		"  b.inamount outamount  from eh_stock_receipt_b b,eh_stock_receipt a " +
        		 		" where a.pk_receipt=b.pk_receipt and pk_receipt_b='"+vsourcebillrowid+"' and " +
        		 		" isnull(a.dr,0)=0 and isnull(b.dr,0)=0";
        		 try {
        			ZA20setvo(sql2);
					intobody2(sql);
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
        	 }
        	 if(IBillType.eh_z0255001.equals(vbilltype)){//成品
        		 getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0255001);
        		 
        		 String[] refpks=ref.getRefPKs();
        		 String pk_icouts = PubTools.combinArrayToString(refpks);
        		 getBillCardPanel().setHeadItem("cppks", pk_icouts);
        		 String sql="select  a.carnumber,b.pk_invbasdoc,a.pk_cubasdoc,b.pk_icout_b pk , a.billno,b.outamount,b.pk_measdoc from eh_icout a, eh_icout_b b " +
        		 		" where a.pk_icout=b.pk_icout and  a.pk_icout in  "+pk_icouts+" and isnull(a.dr,0)=0 " +
        		 				" and isnull(b.dr,0)=0";
        		 try {
					intobody3(sql);
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
				getBillCardPanel().setHeadItem("vbillno", "");
        	 }
        	 if(IBillType.eh_z0502515.equals(vbilltype)){
        		 getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0502515);
        		
        		 String[] refpks=ref.getRefPKs();
        		 
        		 String pk_rkds = PubTools.combinArrayToString(refpks);
        		 String sql="select  b.pk_invbasdoc,a.pk_cubasdoc  pk_cubasdoc,b.pk_back_b pk , a.billno,amount outamount,b.pk_unit pk_measdoc from eh_stock_back a, eh_stock_back_b b " +
 		 		" where a.pk_back=b.pk_back and  a.pk_back in  "+pk_rkds+" and isnull(a.dr,0)=0 " +
 		 				" and isnull(b.dr,0)=0";
        		 String sql3="select b.pk_back_b,a.pk_back,a.retailinfo ,a.pk_cubasdoc, a.tranno ,a.carnumber from eh_stock_back a, eh_stock_back_b b where a.pk_back=b.pk_back and a.pk_back in"+pk_rkds;
		 		 try {
		 			 	ZA21setvo(sql3);
		 			 	intobody4(sql);
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					getBillCardPanel().setHeadItem("vbillno", "");
		        	 }
         }
         if("pk_cubasdoc".equals(strKey)){
             IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
             String pk_cubasdoc=getBillCardWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                 getBillCardWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
              try {
				 String custname = getCustname(pk_cubasdoc," = '"+pk_cubasdoc+"'");
				 getBillCardPanel().setHeadItem("def_14", custname);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
             String SQL = " select freecustflag from bd_cubasdoc a,bd_cumandoc b where a.pk_cubasdoc = b.pk_cubasdoc and b.pk_cumandoc='"+pk_cubasdoc+"' and isnull(dr,0)=0 ";
             try {
                ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
                if(all!=null && all.size()>0){
                    for(int i=0;i<all.size();i++){
                        HashMap hm = (HashMap) all.get(i);
                        String custprop = hm.get("freecustflag")==null?"":hm.get("freecustflag").toString();
                        if(custprop.equals("Y")){
                            getBillCardPanel().getHeadItem("shname").setEnabled(true);
                        }else{
                            getBillCardPanel().getHeadItem("shname").setEnabled(false);
                        }
                    }                   
                }                
            } catch (BusinessException e1) {
                e1.printStackTrace();
            }             
         }
		super.afterEdit(e);
	}
	
	public void changeRefModel(String objtype){
		if(objtype.equals(ICombobox.STR_SBBILLTYPE[0])){
			getBillCardPanel().getHeadItem("vbillno").setEnabled(false);
		}
		if(objtype.equals(ICombobox.STR_SBBILLTYPE[1])){   //收货通知单
			getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
			nc.ui.eh.businessref.ReceiptRefModel invRefModel = new nc.ui.eh.businessref.ReceiptRefModel();
    	    ref.setRefModel(invRefModel);
    	    ref.setMultiSelectedEnabled(false);
            ref.setTreeGridNodeMultiSelected(false);
    	    String fumual = "vsourcebilltype->getColValue(eh_stock_receipt, vbilltype, pk_receipt, getColValue(eh_stock_receipt_b, pk_receipt, pk_receipt_b, vbillno));";
    	    getBillCardPanel().getHeadItem("vbillno").setEditFormula(new String[]{fumual} );//子表主键
    	    getBillCardPanel().getHeadItem("vbillno").setAddSelectedListener(true);
    	    vbilltype=IBillType.eh_z0151001;
    	}
    	if(objtype.equals(ICombobox.STR_SBBILLTYPE[2])){        // 成品出库
    		getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
    		nc.ui.eh.businessref.IcoutRefModel IcoutRefModel = new nc.ui.eh.businessref.IcoutRefModel();
    		ref.setRefModel(IcoutRefModel);
    		ref.setMultiSelectedEnabled(true);
            ref.setTreeGridNodeMultiSelected(true);
            getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0255001);
            vbilltype=IBillType.eh_z0255001;
    	}
    	
//    	if(objtype.equals(ICombobox.STR_SBBILLTYPE[3])){         // 其他材料出库
//    		getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
//    		nc.ui.eh.businessref.BackRefModel CkdRefModel = new nc.ui.eh.businessref.BackRefModel();
//    		ref.setRefModel(CkdRefModel);
//    		ref.setMultiSelectedEnabled(true);
//            ref.setTreeGridNodeMultiSelected(true);
//            getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0502515);
//            vbilltype=IBillType.eh_z0502515;
//    	}
	}
	
    /*
     * (non-Javadoc) @功能说明：自定义按钮
     */
    protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.SBCLOSE, "关闭司磅单", "关闭笥磅单");
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.FIRSTREADDATE, "司磅读数", "司磅读数");
        nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.BusinesBtn,"业务操作","业务操作");
        btnBus.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        btn.setOperateStatus(new int[]{nc.ui.trade.base.IBillOperate.OP_NOTEDIT});
        btn1.setOperateStatus(new int[]{nc.ui.trade.base.IBillOperate.OP_ADD,OP_EDIT});         
        nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"上一页","上一页");
   	 	btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"下一页","下一页");
        btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        
        addPrivateButton(btnNext);
        addPrivateButton(btn);
        addPrivateButton(btn1);
        addPrivateButton(btnBus);
        addPrivateButton(btnPrev);
        
        updateButtons();
    }
    
    //增加表体物料
	@SuppressWarnings("unchecked")
	public void intobody(String sql) throws BusinessException{
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		 ArrayList alinvbasdoc=new ArrayList();
		 for(int i=0;i<al.size();i++){
			 HashMap hm=(HashMap) al.get(i);
			 String pk_invbasdoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
			 alinvbasdoc.add(pk_invbasdoc);
		 }
		 //清空表体
		 int rowcount=getBillCardPanel().getRowCount();
         int[] rowss=new int[rowcount];
         for(int i=rowcount - 1;i>=0;i--){
             rowss[i]=i;
         }
         getBillCardPanel().getBillModel().delLine(rowss);
         //改变表体并执行公式
		 SuperVO[] vos=new SuperVO[alinvbasdoc.size()];
		 for(int i=0;i<alinvbasdoc.size();i++){
			 SbbillBVO bvo=new SbbillBVO();
			 bvo.setDef_1(alinvbasdoc.get(i).toString());
			 vos[i]=bvo;
			 getBillCardPanel().getBillModel().addLine();
			 getBillCardPanel().getBillModel().setBodyRowVO(bvo, i);
		     String[] formual=getBillCardPanel().getBodyItem("def_1").getEditFormulas();//获取编辑公式
             getBillCardPanel().execBodyFormulas(i,formual);
		 }
		 
		 String[] formual=getBillCardPanel().getHeadItem("fullload").getEditFormulas();//获取编辑公式
         getBillCardPanel().execHeadFormulas(formual);
         
         String[] formual2=getBillCardPanel().getHeadItem("packnum").getEditFormulas();//获取编辑公式
         getBillCardPanel().execHeadFormulas(formual2);
	}
	
	@SuppressWarnings("unchecked")
	public void intobody2(String sql) throws BusinessException{
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		 
		 ArrayList alhm=new ArrayList();
		 
		 for(int i=0;i<al.size();i++){
			 ArrayList alinvbasdoc=new ArrayList();
			 HashMap hm=(HashMap) al.get(i);
			 String pk_invbasdoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
			 String billno=hm.get("billno")==null?"":hm.get("billno").toString();
			 String pk_cubasdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
			 UFDouble outamount=new UFDouble(hm.get("outamount")==null?"":hm.get("outamount").toString());
			 UFDouble taxinprice=new UFDouble(hm.get("taxinprice")==null?"":hm.get("taxinprice").toString());//add by houcq 2011-03-07
			 String pk_masdoc=hm.get("pk_measdoc")==null?"":hm.get("pk_measdoc").toString();
			 String pk=hm.get("pk")==null?"":hm.get("pk").toString();
			 alinvbasdoc.add(pk_invbasdoc);			 
			 alinvbasdoc.add(billno);
			 alinvbasdoc.add(pk_cubasdoc);
			 alinvbasdoc.add(outamount);
			 alinvbasdoc.add(pk_masdoc);
			 alinvbasdoc.add(pk);
			 alhm.add(alinvbasdoc);
			 alinvbasdoc.add(taxinprice);//add by houcq 2011-03-07
		 }
		 //清空表体
		 int rowcount=getBillCardPanel().getRowCount();
        int[] rowss=new int[rowcount];
        for(int i=rowcount - 1;i>=0;i--){
            rowss[i]=i;
        }
        getBillCardPanel().getBillModel().delLine(rowss);
        //改变表体并执行公式
		 SuperVO[] vos=new SuperVO[alhm.size()];
		 for(int i=0;i<alhm.size();i++){
			 SbbillBVO bvo=new SbbillBVO();
			 ArrayList als=(ArrayList) alhm.get(i);
			 bvo.setDef_1(als.get(0).toString());//物料
			 bvo.setDef_2(als.get(1).toString());//billno
			 bvo.setDef_3(als.get(2).toString());//客商
			 bvo.setDef_4(new UFDouble(als.get(3).toString()));//数量
			 bvo.setDef_5(als.get(4).toString());//单位
			 bvo.setDef_6(als.get(5).toString());//pk
			 bvo.setDef_10(new UFDouble(als.get(6).toString()));//含税单价 add by houcq 2011-03-07
			 vos[i]=bvo;
			 getBillCardPanel().getBillModel().addLine();
			 getBillCardPanel().getBillModel().setBodyRowVO(bvo, i);
		     String[] formual=getBillCardPanel().getBodyItem("def_1").getEditFormulas();//获取编辑公式
             getBillCardPanel().execBodyFormulas(i,formual);
             String[] formual2=getBillCardPanel().getBodyItem("def_3").getLoadFormula();
             getBillCardPanel().execBodyFormulas(i,formual2);
             String[] formual3=getBillCardPanel().getBodyItem("def_5").getLoadFormula();
             getBillCardPanel().execBodyFormulas(i,formual3);
		 }
		 
		 String[] formual=getBillCardPanel().getHeadItem("fullload").getEditFormulas();//获取编辑公式
        getBillCardPanel().execHeadFormulas(formual);
        
        String[] formual2=getBillCardPanel().getHeadItem("packnum").getEditFormulas();//获取编辑公式
        getBillCardPanel().execHeadFormulas(formual2);
	}
	
	@SuppressWarnings("unchecked")
	public void intobody4(String sql) throws BusinessException{
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		 
		 ArrayList alhm=new ArrayList();
		 
		 for(int i=0;i<al.size();i++){
			 ArrayList alinvbasdoc=new ArrayList();
			 HashMap hm=(HashMap) al.get(i);
			 String pk_invbasdoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
			 String billno=hm.get("billno")==null?"":hm.get("billno").toString();
			 String pk_cubasdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
			 UFDouble outamount=new UFDouble(hm.get("outamount")==null?"":hm.get("outamount").toString());
			 String pk_masdoc=hm.get("pk_measdoc")==null?"":hm.get("pk_measdoc").toString();
			 String pk=hm.get("pk")==null?"":hm.get("pk").toString();
			 alinvbasdoc.add(pk_invbasdoc);
			 alinvbasdoc.add(billno);
			 alinvbasdoc.add(pk_cubasdoc);
			 alinvbasdoc.add(outamount);
			 alinvbasdoc.add(pk_masdoc);
			 alinvbasdoc.add(pk);
			 alhm.add(alinvbasdoc);
		 }
		 //清空表体
		 int rowcount=getBillCardPanel().getRowCount();
       int[] rowss=new int[rowcount];
       for(int i=rowcount - 1;i>=0;i--){
           rowss[i]=i;
       }
       getBillCardPanel().getBillModel().delLine(rowss);
       //改变表体并执行公式
		 SuperVO[] vos=new SuperVO[alhm.size()];
		 for(int i=0;i<alhm.size();i++){
			 SbbillBVO bvo=new SbbillBVO();
			 ArrayList als=(ArrayList) alhm.get(i);
			 bvo.setDef_1(als.get(0).toString());//物料
			 bvo.setDef_2(als.get(1).toString());//billno
			 bvo.setDef_3(als.get(2).toString());//客商
			 bvo.setDef_4(new UFDouble(als.get(3).toString()));//数量
			 bvo.setDef_5(als.get(4).toString());//单位
			 bvo.setDef_6(als.get(5).toString());//pk
			 vos[i]=bvo;
			 getBillCardPanel().getBillModel().addLine();
			 getBillCardPanel().getBillModel().setBodyRowVO(bvo, i);
		     String[] formual=getBillCardPanel().getBodyItem("def_1").getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(i,formual);
            String[] formual2=getBillCardPanel().getBodyItem("def_3").getLoadFormula();
            getBillCardPanel().execBodyFormulas(i,formual2);
            String[] formual3=getBillCardPanel().getBodyItem("def_5").getLoadFormula();
            getBillCardPanel().execBodyFormulas(i,formual3);
		 }
		 
		 String[] formual=getBillCardPanel().getHeadItem("fullload").getEditFormulas();//获取编辑公式
       getBillCardPanel().execHeadFormulas(formual);
       
       String[] formual2=getBillCardPanel().getHeadItem("packnum").getEditFormulas();//获取编辑公式
       getBillCardPanel().execHeadFormulas(formual2);
	}
	
	@SuppressWarnings("unchecked")
	public void intobody3(String sql) throws BusinessException{
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		 
		 ArrayList alhm=new ArrayList();
		 HashMap hm2=new HashMap();
		 String carnumber=null;
		 String[] pk_cubasdocs = new String[al.size()];
		 for(int i=0;i<al.size();i++){
			 ArrayList alinvbasdoc=new ArrayList();
			 HashMap hm=(HashMap) al.get(i);
			 carnumber=hm.get("carnumber")==null?"":hm.get("carnumber").toString();
			 if(hm2.size()==0){
				 hm2.put(carnumber, carnumber);
			 }
			 if(!hm2.containsKey(carnumber)){
				 showErrorMessage("你没有选同一车号！");
				 return;
			 }
			 String pk_invbasdoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
			 String billno=hm.get("billno")==null?"":hm.get("billno").toString();
			 String pk_cubasdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
			 UFDouble outamount=new UFDouble(hm.get("outamount")==null?"":hm.get("outamount").toString());
			 String pk_masdoc=hm.get("pk_measdoc")==null?"":hm.get("pk_measdoc").toString();
			 String pk=hm.get("pk")==null?"":hm.get("pk").toString();
			 pk_cubasdocs[i] = pk_cubasdoc; 
			 alinvbasdoc.add(pk_invbasdoc);
			 alinvbasdoc.add(billno);
			 alinvbasdoc.add(pk_cubasdoc);
			 alinvbasdoc.add(outamount);
			 alinvbasdoc.add(pk_masdoc);
			 alinvbasdoc.add(pk);
			 alhm.add(alinvbasdoc);
		 }
		 //清空表体
	   int rowcount=getBillCardPanel().getRowCount();
       int[] rowss=new int[rowcount];
       for(int i=rowcount - 1;i>=0;i--){
           rowss[i]=i;
       }
       getBillCardPanel().getBillModel().delLine(rowss);
       //改变表体并执行公式
		 SuperVO[] vos=new SuperVO[alhm.size()];
		 for(int i=0;i<alhm.size();i++){
			 SbbillBVO bvo=new SbbillBVO();
			 ArrayList als=(ArrayList) alhm.get(i);
			 bvo.setDef_1(als.get(0).toString());//物料
			 bvo.setDef_2(als.get(1).toString());//billno
			 bvo.setDef_3(als.get(2).toString());//客商
			 bvo.setDef_4(new UFDouble(als.get(3).toString()));//数量
			 bvo.setDef_5(als.get(4).toString());//单位
			 bvo.setDef_6(als.get(5).toString());//pk
			 vos[i]=bvo;
			 getBillCardPanel().getBillModel().addLine();
			 getBillCardPanel().getBillModel().setBodyRowVO(bvo, i);
		     String[] formual=getBillCardPanel().getBodyItem("def_1").getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(i,formual);
            String[] formual2=getBillCardPanel().getBodyItem("def_3").getLoadFormula();
            getBillCardPanel().execBodyFormulas(i,formual2);
            String[] formual3=getBillCardPanel().getBodyItem("def_5").getLoadFormula();
            getBillCardPanel().execBodyFormulas(i,formual3);
		 }
	 
	   String[] formual=getBillCardPanel().getHeadItem("fullload").getEditFormulas();//获取编辑公式
       getBillCardPanel().execHeadFormulas(formual);
       
       String[] formual2=getBillCardPanel().getHeadItem("packnum").getEditFormulas();//获取编辑公式
       getBillCardPanel().execHeadFormulas(formual2);
        
       /**成品多个客户司磅时同时显示 add by wb 2009-2-23 20:03:00*/
       String pk_cubas = PubTools.combinArrayToString2(pk_cubasdocs);
       getBillCardPanel().setHeadItem("pk_cubasdoc", pk_cubas);
       try {
			String custnames = getCustname(pk_cubas," in ("+pk_cubas+")");
			getBillCardPanel().setHeadItem("def_14", custnames);
		} catch (Exception e) {
			e.printStackTrace();
		}
       /******************** end ******************************/
       
	}
	
	
	@SuppressWarnings("unchecked")
	private void ZA20setvo(String sql) throws BusinessException{
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		 for(int i=0;i<al.size();i++){
			 HashMap hm=(HashMap) al.get(0);
			 String pk_receipt=hm.get("pk_receipt")==null?"":hm.get("pk_receipt").toString();
			 UFBoolean allcheck=new UFBoolean(hm.get("allcheck")==null?"":hm.get("allcheck").toString());
			 String pk_receipt_b=hm.get("pk_receipt_b")==null?"":hm.get("pk_receipt_b").toString();
			 String pk_invbasdoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
			 String pk_cubasdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
			 String retailinfo=hm.get("retailinfo")==null?"":hm.get("retailinfo").toString();
			 UFDouble packagweight=new UFDouble(hm.get("packagweight")==null?"0":hm.get("packagweight").toString());
			 UFDouble item=new UFDouble(hm.get("item")==null?"":hm.get("item").toString());
			 UFDouble sum=new UFDouble(hm.get("sum")==null?"":hm.get("sum").toString());
			 String carnumber=hm.get("carnumber")==null?"":hm.get("carnumber").toString();
			 String tranno=hm.get("tranno")==null?"":hm.get("tranno").toString();
			 String carnumbertrue=getBillCardPanel().getHeadItem("carnumber").getValueObject()==null?"":
				 getBillCardPanel().getHeadItem("carnumber").getValueObject().toString();
			 String trannoture=getBillCardPanel().getHeadItem("tranno").getValueObject()==null?"":
				 getBillCardPanel().getHeadItem("tranno").getValueObject().toString();
			 if(carnumbertrue.equals("")){
				 getBillCardPanel().setHeadItem("carnumber", carnumber);
			 }
			 if(trannoture.equals("")){
				 getBillCardPanel().setHeadItem("tranno", tranno);
			 }
			 getBillCardPanel().setHeadItem("vsourcebillid", pk_receipt);
			 getBillCardPanel().setHeadItem("vsourcebillrowid", pk_receipt_b);
			 getBillCardPanel().setHeadItem("pk_invbasdoc", pk_invbasdoc);
			 getBillCardPanel().setHeadItem("pk_cubasdoc", pk_cubasdoc);
			 try {
				 String custname = getCustname(pk_cubasdoc," = '"+pk_cubasdoc+"'");
				 getBillCardPanel().setHeadItem("def_14", custname);
			} catch (Exception e) {
				e.printStackTrace();
			}
			 getBillCardPanel().setHeadItem("wrapperweight", packagweight);
			 getBillCardPanel().setHeadItem("packnum", item);
			 getBillCardPanel().setHeadItem("bzkz", sum);
			 getBillCardPanel().setHeadItem("shname", retailinfo);
			 getBillCardPanel().setHeadItem("ischeck", allcheck);
		 }
	}    
	
	/**
	 * 退货
	 * @param sql
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private void ZA21setvo(String sql) throws BusinessException{
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		 String[] pk_cubasdocs = new String[al.size()] ;
		 for(int i=0;i<al.size();i++){
			 HashMap hm=(HashMap) al.get(0);
			 String pk_receipt=hm.get("pk_back")==null?"":hm.get("pk_back").toString();
			 String pk_receipt_b=hm.get("pk_back_b")==null?"":hm.get("pk_back_b").toString();
			 String pk_invbasdoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
			 String pk_cubasdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
			 pk_cubasdocs[i] = pk_cubasdoc;
			 String retailinfo=hm.get("retailinfo")==null?"":hm.get("retailinfo").toString();
			 String tranno=hm.get("tranno")==null?"":hm.get("tranno").toString();
			 String carnumber=hm.get("carnumber")==null?"":hm.get("carnumber").toString();
			 getBillCardPanel().setHeadItem("tranno", tranno);
			 getBillCardPanel().setHeadItem("carnumber", carnumber);
			 getBillCardPanel().setHeadItem("vsourcebillid", pk_receipt);
			 getBillCardPanel().setHeadItem("vsourcebillrowid", pk_receipt_b);
			 getBillCardPanel().setHeadItem("pk_invbasdoc", pk_invbasdoc);
			 getBillCardPanel().setHeadItem("pk_cubasdoc", pk_cubasdoc);
			 getBillCardPanel().setHeadItem("shname", retailinfo);
		 }
		 	/**退货多个客户司磅时同时显示 add by wb 2009-2-23 20:03:00*/
	       String pk_cubas = PubTools.combinArrayToString2(pk_cubasdocs);
	       getBillCardPanel().setHeadItem("pk_cubasdoc", pk_cubas);
	       try {
				String custnames = getCustname(pk_cubas," in ("+pk_cubas+")");
				getBillCardPanel().setHeadItem("def_14", custnames);
			} catch (Exception e) {
				e.printStackTrace();
			}
	       /******************** end ******************************/
	}    
	//清空表头
	public void clearHead(){
		getBillCardPanel().setHeadItem("vsourcebillrowid", "");
		getBillCardPanel().setHeadItem("shname", "");
		getBillCardPanel().setHeadItem("vcustname", "");
		getBillCardPanel().setHeadItem("pk_cubasdoc", "");
		getBillCardPanel().setHeadItem("vinvname", "");
		getBillCardPanel().setHeadItem("pk_invbasdoc", "");
		getBillCardPanel().setHeadItem("suttle", "");
		getBillCardPanel().setHeadItem("packnum", "");
		getBillCardPanel().setHeadItem("singleweight", "");
		getBillCardPanel().setHeadItem("wrapperweight", "");
		getBillCardPanel().setHeadItem("vsourcebilltype", "");
		getBillCardPanel().setHeadItem("def_1", "");
		getBillCardPanel().setHeadItem("def_10", "");
		getBillCardPanel().setHeadItem("def_4", "");
		getBillCardPanel().setHeadItem("def_5", "");
		getBillCardPanel().setHeadItem("def_8", "");
		getBillCardPanel().setHeadItem("def_9", "");
		getBillCardPanel().setHeadItem("sumsuttle", "");
		getBillCardPanel().setHeadItem("close_flag", "");
		getBillCardPanel().setHeadItem("reason", "");
		getBillCardPanel().setHeadItem("def_11", "");
		getBillCardPanel().setHeadItem("def_12", "");
		getBillCardPanel().setHeadItem("def_14", "");
		//getBillCardPanel().setHeadItem("memo", "");
	}
	
	/***
	 * 根据pk得到客户名称
	 * @param pk_cubasdocs
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getCustname(String pk_cubasdocs,String addwhere) throws Exception{
		String sql = "select custname from bd_cubasdoc a,bd_cumandoc b where a.pk_cubasdoc = b.pk_cubasdoc and b.pk_cumandoc "+addwhere+"";
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		 String custnames = null;
		 if(al!=null&&al.size()>0){
			String[] cunames = new String[al.size()];
			 for(int i=0;i<al.size();i++){
				 HashMap hm=(HashMap) al.get(i);
				 String custname =hm.get("custname")==null?"":hm.get("custname").toString();
				 cunames[i] = custname;
			 }
			 custnames = PubTools.combinArrayToString3(cunames);
		 }
		 return custnames;
	} 
}