/**
 * @(#)ClientEventHandler.java	V3.1 2007-3-11
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.z0205510;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.CubasBySubcuRefModel;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0205510.SeconddiscountCheckinvVO;
import nc.vo.eh.trade.z0205510.SeconddiscountVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;
/**
 * 功能说明：二次折扣
 * 2008-4-8 16:43:39
 */
public class ClientEventHandler extends AbstractEventHandler {
    SeconddiscountVO headvo = null;
    String pk = null;
    boolean iscopy=false;                                  //是否项目调整
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
     protected void onBoElse(int intBtn) throws Exception {
            switch (intBtn)
            {
                case IEHButton.GENRENDETAIL:    //生成明细
                    onBoGENRENDETAIL();
                    break;
                case IEHButton.STOCKCHANGE:     //折扣变更
                    onBozkbg();
                    break;
                  
                    
            }  
            super.onBoElse(intBtn);
        }
        
     
        @Override
        protected void onBoEdit() throws Exception {
            // TODO Auto-generated method stub
            String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
            if(!coperatorid.equals(_getOperator())){
                getBillUI().showErrorMessage("不允许修改他人申请！");
                return;
            }
            super.onBoEdit();
            AggregatedValueObject aggVO = getBillCardPanelWrapper().getBillVOFromUI();
			String pk_subcubasdoc = ((SeconddiscountVO)aggVO.getParentVO()).getPk_subcubasdoc();
			CubasBySubcuRefModel.pk_subcubasdoc = pk_subcubasdoc;
            getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(true);
            getBillUI().updateButtonUI();

        }
        @Override
        protected void onBoDelete() throws Exception {
            // TODO Auto-generated method stub
            String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
            if(!coperatorid.equals(_getOperator())){
                getBillUI().showErrorMessage("不允许删除他人申请！");
                return;
            }
            super.onBoDelete();
        }
        
        @Override
        protected void onBoLineAdd() throws Exception {
        	super.onBoLineAdd();
        }
        
       
            @Override
            public void onBoAdd(ButtonObject arg0) throws Exception {
                // TODO Auto-generated method stub
                super.onBoAdd(arg0);
                ClientUI.pk_subcubasdoc = null;
                getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(true);
                getBillUI().updateButtonUI();
            }
          
            @Override
            protected void onBoCancel() throws Exception {
                // TODO Auto-generated method stub
                super.onBoCancel();
                if(headvo!=null){
                    headvo = null;
                }
                getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(false);
                getBillUI().updateButtonUI();
            }
                   
//            设置按钮的可用状态
                protected void setBoEnabled() throws Exception {
                   super.setBoEnabled();
                }
        
                @SuppressWarnings("unchecked")
				public void onBoSave() throws Exception {
                    getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
                    AggregatedValueObject vo= getBillCardPanelWrapper().getBillVOFromUI();
                    SeconddiscountVO hvo = (SeconddiscountVO)vo.getParentVO();
                    int invjstype = hvo.getInvjstype();			//产品计算方式
                    BillModel model1 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("range");
                    if (model1 != null&&invjstype==1) {
                        int rowCount = model1.getRowCount();
                        if (rowCount < 1) {
                        	getBillUI().showErrorMessage("产品范围表体行不能为空!");
                            return;
                        }
                    }
                    BillModel model2 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("polic");
                    if (model2 != null) {
                        int rowCount = model2.getRowCount();
                        if (rowCount < 1) {
                        	getBillUI().showErrorMessage("折扣政策表体行不能为空!");
                            return;
                        }
                    }
                    BillModel model3 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("checkinv");
                    if (model3 != null) {
                        int rowCount = model3.getRowCount();
                        if (rowCount < 1) {
                        	getBillUI().showErrorMessage("结算产品表体行不能为空!");
                            return;
                        }
                    }
                    
                    //鉴于修改明细生成的政策，现将以下2个校验舍去  zqy 2010年6月29日10:54:57
                    
                    //唯一性校验
                    BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel("checkinv");
                    int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","pk_cubasdoc"});
                    if(res==1){
                        getBillUI().showErrorMessage("结算产品页签中物料客商有重复!");
                        return;
                    }
                    
                    BillModel bm2=getBillCardPanelWrapper().getBillCardPanel().getBillModel("range");
                    int res2=new PubTools().uniqueCheck(bm2, new String[]{"pk_invbasdoc"});
                    if(res2==1){
                        getBillUI().showErrorMessage("产品范围页签中物料有重复!");
                        return;
                    }
                    
                    //判断在执行期间和有效期间中的物料是否已有一次折扣设定
                    String pk_jscubasdoc = hvo.getPk_cubasdoc();			//结算客户
                    String pk_supcubasdoc = hvo.getPk_subcubasdoc();		//子客户
                    String startdate=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("startdate").getValueObject().toString();
                    String enddate=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("enddate").getValueObject().toString();
                    String policetype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("policetype").getValueObject().toString();		//政策类型
                    String pk_seconddiscount=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_seconddiscount").getValueObject()==null?"":
                            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_seconddiscount").getValueObject().toString();
                    IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
                    StringBuffer sql=new StringBuffer("select b.pk_invbasdoc,b.pk_cubasdoc from eh_seconddiscount a,eh_seconddiscount_checkinv b ")
                    .append(" where a.pk_seconddiscount=b.pk_seconddiscount and(('"+startdate+"' > startdate and '"+startdate+"' <enddate) or  ")
                    .append("('"+enddate+"'>startdate and '"+enddate+"'<enddate) or ('"+startdate+"'<=startdate and '"+enddate+"'>=enddate)) ")
                    .append(" and a.pk_cubasdoc = '"+pk_jscubasdoc+"' and pk_subcubasdoc = '"+pk_supcubasdoc+"' and a.policetype='"+policetype+"' and a.discounttype= "+hvo.getDiscounttype()+" and a.jstype = "+hvo.getJstype()+" and a.jsmethod = "+hvo.getJsmethod()+" and a.invjstype = "+hvo.getInvjstype()+"  and isnull(a.dr,0)=0 and isnull(b.dr,0)=0")
                    .append(" and isnull(a.def_1,'Y')='Y'  and isnull(a.lock_flag,'N')='N' and a.pk_corp='"+_getCorp().getPk_corp()+"' and a.pk_seconddiscount<>nvl('"+pk_seconddiscount+"',' ') ");
                    
                    if(headvo!=null&&headvo.getPk_seconddiscount()!=null){
                    	sql.append(" and a.pk_seconddiscount<>'"+headvo.getPk_seconddiscount()+"'");
                    }
                    ArrayList<HashMap> arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                    HashMap hm=new HashMap();
                    for(HashMap al:arr){
                        hm.put(al.get("pk_invbasdoc").toString()+al.get("pk_cubasdoc").toString(),al.get("pk_invbasdoc").toString());
                    }
                    super.onBoSave2_whitBillno();
                    if(headvo!=null&&headvo.getPk_seconddiscount()!=null){
                    	IVOPersistence  iVOPersistence =   (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
                        headvo.setDef_1("N");                     //将旧版本的最新标记去掉
                        headvo.setLock_flag(new UFBoolean(true)); //关闭旧版本
                    	iVOPersistence.updateVO(headvo);          //表头
                        iscopy = false;
                        pk=null;
                        headvo = null;
                    }
                    getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(false);
                    getBillUI().updateButtonUI();
                }
            
            /* （非 Javadoc）
             * @see nc.ui.trade.manage.ManageEventHandler#onBoReturn()
             */
            protected void onBoReturn() throws Exception {
                // TODO 自动生成方法存根
                super.onBoReturn();
                setBoEnabled();
            }
            
            /* （非 Javadoc）
             * @see nc.ui.trade.manage.ManageEventHandler#onBoCard()
             */
            protected void onBoCard() throws Exception {
                // TODO 自动生成方法存根
                super.onBoCard();
                setBoEnabled();
               
            }
            /* （非 Javadoc）
             * @see nc.ui.trade.manage.ManageEventHandler#onBoRefresh()
             */
            protected void onBoRefresh() throws Exception {
                // TODO 自动生成方法存根
                super.onBoRefresh();
                setBoEnabled();
            }
            
            
            /**
             * 功能：
             * <p>生成明细</p>
             */
            @SuppressWarnings("unchecked")
            protected void onBoGENRENDETAIL() throws Exception{
                String pk_invbasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?"":	//产品
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
                String pk_subcubasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_subcubasdoc").getValueObject()==null?""://客户
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_subcubasdoc").getValueObject().toString();                  
                String pk_areacl=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_areacl").getValueObject()==null?"":			//片区
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_areacl").getValueObject().toString();
                
//                if(pk_cubasdoc.equals("%")){
//                    getBillUI().showErrorMessage("请选择客户！");
//                    return;
//                }
                //add by houcq 2011-04-23 begin
                if (pk_invbasdoc.equals("") && pk_subcubasdoc.equals("") && pk_areacl.equals(""))
                {
                	getBillUI().showErrorMessage("产品,客户,片区必选一个!");
                	return;
                }
                //end
                if(!pk_invbasdoc.equals("") && !pk_subcubasdoc.equals("") && !pk_areacl.equals("")){
                	getBillUI().showErrorMessage("产品,客户,片区不能同时选择,只允许选择其中一个条件!");
                	return;
                }else if(!pk_invbasdoc.equals("") && !pk_subcubasdoc.equals("")){
                	getBillUI().showErrorMessage("产品,客户不能同时选择,只允许选择其中一个条件!");
                	return;
                }else if(!pk_invbasdoc.equals("") && !pk_areacl.equals("")){
                	getBillUI().showErrorMessage("产品,片区不能同时选择,只允许选择其中一个条件!");
                	return;
                }else if(!pk_subcubasdoc.equals("") && !pk_areacl.equals("")){
                	getBillUI().showErrorMessage("客户,片区不能同时选择,只允许选择其中一个条件!");
                	return;
                }
                
                String currentModel = getBillCardPanelWrapper().getBillCardPanel().getCurrentBodyTableCode();		//页签
                IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
                //modify by houcq 2011-04-23
                StringBuffer sql=new StringBuffer();
                sql.append("select * from (select rank() over(partition by pk_invbasdoc order by dmakedate desc) rk,a.* from (");
            	sql.append("  SELECT b.dmakedate,a.pk_cubasdoc,a.pk_invbasdoc,nvl(b.newdiscount,0) newdiscount");
                sql.append(" ,a.invcode,a.invname,a.invspec,a.invtype,a.def1,a.brandname,a.custcode,a.custname,a.newprice ");
                sql.append("  FROM (select b.newprice,a.* from ");
                sql.append("  (select a.pk_cumandoc pk_cubasdoc,b.pk_invbasdoc ");
                sql.append(" ,c.invcode,c.invname,c.invspec,c.invtype,c.def1,g.brandname,aa.custcode,aa.custname");
                sql.append("  from bd_cumandoc a ,bd_cubasdoc aa, eh_custkxl b ");
                sql.append("        ,bd_invbasdoc c ") ;
                sql.append("        ,bd_invmandoc cc ") ;
                sql.append(" ,eh_brand g ");
                sql.append("  where a.pk_cumandoc = b.pk_cubasdoc ");
                sql.append("  and aa.pk_cubasdoc=a.pk_cubasdoc  ");
                sql.append(" and cc.pk_invmandoc = b.pk_invbasdoc ");
                sql.append(" and cc.pk_invbasdoc = c.pk_invbasdoc ");
                sql.append(" and g.pk_brand = c.invpinpai ");
                if(!pk_areacl.equals("") && pk_areacl.length()>0){	
                    sql.append("  and nvl(a.def5,' ') like '"+pk_areacl+"'  ");                    
                }
                if(!pk_subcubasdoc.equals("") && pk_subcubasdoc.length()>0){	//客户 
                    sql.append("  AND a.pk_cumandoc like '"+pk_subcubasdoc+"' ");                    
                }
                if(!pk_invbasdoc.equals("") && pk_invbasdoc.length()>0){
                    sql.append(" and b.pk_invbasdoc like '"+pk_invbasdoc+"' ") ;
                }
                sql.append(" and nvl(cc.dr,0) = '0'  and nvl(g.dr,0) = '0' ");
                sql.append("  and nvl(a.dr,0)=0 and nvl(b.dr,0)=0");
                sql.append(" ) a,(select * from (select rank() over(partition by pk_invbasdoc order by dapprovetime desc) rk,aaaa.* from");
                sql.append(" (select to_date(nvl(a.dapprovetime,a.dapprovedate),'yyyy-mm-dd:hh24:mi:ss') dapprovetime,b.pk_invbasdoc,b.newprice from eh_price a , eh_price_b b");
                sql.append(" where a.pk_price=b.pk_price  and a.vbillstatus=1  and nvl(a.dr,0)=0   and nvl(b.dr,0)=0");
                sql.append(" and a.pk_corp='"+_getCorp().getPk_corp()+"'");
                sql.append(" and '"+_getDate()+"' between a.zxdate and a.yxdate)aaaa) t");
                sql.append(" where t.rk<2) b where a.pk_invbasdoc= b.pk_invbasdoc(+) ");
                sql.append("  ) a LEFT JOIN ");
                sql.append("  (SELECT b.pk_cubasdoc,b.pk_invbasdoc,b.newdiscount,a.dmakedate");
                sql.append("  FROM eh_firstdiscount a,eh_firstdiscount_b b");
                sql.append("  WHERE a.pk_firstdiscount = b.pk_firstdiscount"); 
                if(!pk_subcubasdoc.equals("") && pk_subcubasdoc.length()>0){	//客户 
                   
                    sql.append("  AND b.pk_cubasdoc LIKE '"+pk_subcubasdoc+"'");
                }                
                sql.append("  AND a.def_1 = 'Y'");
                sql.append("  and '"+_getDate()+"' between a.zxdate and a.yxdate");
                sql.append("  and nvl(a.lock_flag,'N')<>'Y'");
                sql.append("  and a.pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(a.dr,0)=0 and nvl(b.dr,0)=0");
                sql.append("  ) b ON a.pk_cubasdoc = b.pk_cubasdoc AND a.pk_invbasdoc = b.pk_invbasdoc");
                sql.append(")a ) t where t.rk<2");//add by houcq 2011-10-12
                int rowss= getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).getRowCount();
                ArrayList<HashMap> arr=(ArrayList<HashMap>)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                if(rowss>0){
	                int iRet = getBillUI().showYesNoMessage("生成明细前要清空表体，是否确定?");
	                if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
	                	//清空产品范围
	                	if(currentModel.equals("range")){
		                    int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).getRowCount();
		                    int[] rows=new int[rowcount];
		                    for(int i=rowcount - 1;i>=0;i--){
		                        rows[i]=i;
		                    }
		                    getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).delLine(rows);
	                	}
	                    //清空结算产品
	                	if(currentModel.equals("checkinv")){
		                    int checkinvount = getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).getRowCount();
		                    int[] checkrows = new int[checkinvount];
		                    for(int i=checkinvount - 1;i>=0;i--){
		                    	checkrows[i]=i;
		                    }
		                    getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).delLine(checkrows);
	                	}
	                    this.getBillUI().updateUI();
	                }else{
	                	return;
	                }
                }  
                    for(int i=0;i<arr.size();i++){
                    	HashMap hm=(HashMap)arr.get(i);
                        //-------产品范围生成明细
                    	if(currentModel.equals("range")){
	                    	getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).addLine();
	                        getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).setValueAt(hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString(), i, "pk_invbasdoc");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invcode"), i, "vinvcode");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invname"), i, "vinvname");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invspec"), i, "vguige");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invtype"), i, "vtype");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("def1"), i, "vcolor");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("brandname"), i, "vbrand");
                    	}
                        //------结算产品生成明细            add by wb 2008-9-8 16:27:40 
                    	if(currentModel.equals("checkinv")){
	                        UFDouble firstdscount = new UFDouble(hm.get("newdiscount")==null?"0":hm.get("newdiscount").toString());		//一次折扣
	                        getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).addLine();
	                        getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).setValueAt(hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString(), i, "pk_invbasdoc");
	                        getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).setValueAt(hm.get("pk_cubasdoc"), i, "pk_cubasdoc");
	                        getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).setValueAt(firstdscount, i, "def_7");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invcode"), i, "vinvcode");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invname"), i, "vinvname");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invspec"), i, "vguige");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invtype"), i, "vtype");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("def1"), i, "vcolor");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("brandname"), i, "vbrand");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("custcode"), i, "vcustcode");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("custname"), i, "vcustname");
	                        //this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("def3"), i, "def_6");
	                        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("newprice"), i, "def_6");//modify by houcq 2011-04-23
	                        UFDouble pprice = new UFDouble(hm.get("newprice")==null?"0":hm.get("newprice").toString());
	                        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pprice.sub(firstdscount), i, "jprice");
	                        String pk_invmandoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
	                        UFDouble bjj =pprice.sub(firstdscount).multiply(getRate(pk_invmandoc));
	                        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bjj, i, "bjj");
                    	}
                    }
                getBillUI().updateUI();
            }
            
            private void onBozkbg() {
           	 try {
                    AggregatedValueObject aggvo = getBillUI().getVOFromUI();
                    headvo=(SeconddiscountVO)aggvo.getParentVO();
                    SeconddiscountCheckinvVO[] checkVOS = (SeconddiscountCheckinvVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel("checkinv").getBodyValueVOs("nc.vo.eh.trade.z0205510.SeconddiscountCheckinvVO");
                    pk = headvo.getPrimaryKey();
                    int  billstatus=headvo.getVbillstatus().intValue();
                    String new_flag = headvo.getDef_1()==null?"N":headvo.getDef_1();            //最新标记
                       //如果没有选择项目提示错误
                       if(pk==null||pk.equals("")){
                           getBillUI().showErrorMessage("请选择一张单据!");
                           return;
                       }
                       //不允许重复调整
                       if(!new_flag.equals("Y")){
                           getBillUI().showErrorMessage("不是最新版本,不能变更!");
                           return;
                       }
                       //只有审批通过的项目才允许调整
                       if(billstatus!=IBillStatus.CHECKPASS){
                           getBillUI().showErrorMessage("只有审批通过的项目才允许变更！");
                           return;
                       }
                       
                       int ok=getBillUI().showYesNoMessage("是否确认进行项目变更?");
                       if(ok==MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                           onBoCopy();
                           getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(true);
                           getBillUI().updateButtonUI();
                           String billNo = BillcodeRuleBO_Client.getBillCode(getUIController().getBillType(),_getCorp().getPk_corp(),null, null);
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(billNo);
                           //将版本号进行改变在原基础上加1
                           Integer ver= Integer.parseInt(headvo.getDef_2());
                           ver=ver+1;
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_2").setValue(String.valueOf(ver));
                           getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
                           getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapproveid").setValue(null);
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dapprovedate").setValue(null);
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapprovenote").setValue(null);
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(new Integer(8));
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_1").setValue("Y");            //最新标记
                           
                           //变更时将表体折扣额带入表体新的牌价,一次折扣 add by wb at 2008年10月20日18:20:21
                           if(checkVOS!=null&&checkVOS.length>0){
                           	for(int i=0;i<checkVOS.length;i++){
                           		String[] formual= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").getEditFormulas();//获取编辑公式
                           		getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
                           		String pk_cubasdoc = checkVOS[i].getPk_cubasdoc();
					            String pk_invbasdoc = checkVOS[i].getPk_invbasdoc();
					             StringBuffer sql = new StringBuffer()
					             .append(" select newdiscount ")
					             .append(" from eh_firstdiscount a,eh_firstdiscount_b b")
					             .append(" where a.pk_firstdiscount = b.pk_firstdiscount")
					             .append(" and a.def_1 = 'Y'")
					             .append(" and '"+_getDate()+"' between b.zxdate and b.yxdate")
					             .append(" and isnull(b.lock_flag,'N')<>'Y'")
					             .append(" and a.custom = '"+pk_cubasdoc+"' ")
					             .append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"'")
					             .append(" and isnull(a.dr,0)=0")
					             .append(" and isnull(b.dr,0)=0");
					             IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
					             Object objdis = iUAPQueryBS.executeQuery(sql.toString(),new ColumnProcessor());
					             UFDouble fisdiscount = new UFDouble(objdis==null?"0":objdis.toString());
					             getBillCardPanelWrapper().getBillCardPanel().getBillModel("checkinv").setValueAt(fisdiscount, i, "def_7");
                           }
                           //设置复制标记为true
                           iscopy=true;
                           getBillUI().updateUI();
                        }
                       }
                } catch (Exception e) {
                    e.printStackTrace();
                }
       	}
            
            //设置多页签的打印模板 
            protected void onBoPrint() throws Exception {
                nc.ui.pub.print.IDataSource dataSource = new ClientCardPanelPRTS(getBillUI()
                        ._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),getUIController().getPkField());
                nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
                        dataSource);
                print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
                        ._getModuleCode(), getBillUI()._getOperator(), getBillUI()
                        .getBusinessType(), getBillUI().getNodeKey());
                print.selectTemplate();
                print.preview();
            }  
            @SuppressWarnings("unchecked")
        	public UFDouble getRate(String pk_invbasdoc) throws BusinessException{
             	String sql="select mainmeasrate changerate from bd_convert " +
             			" where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc " +
             			" where pk_invmandoc='"+pk_invbasdoc+"') " +
             			" and nvl(dr,0)=0";
             	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
             	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
             	UFDouble changerate=new UFDouble(-1000000);
             	if(al!=null&&al.size()>0){
         	    	for(int i=0;i<al.size();i++){
         	    		HashMap hm=(HashMap) al.get(i);
         	    		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
         	    	}
             	}
             	return changerate;
             	
             }
            @SuppressWarnings("unchecked")
			public void onButton_N(ButtonObject bo, BillModel model) {
            	if ("增加".equals(bo.getCode()))
            	{
            		//当折扣期间计算中生成下月数据、启用功能，任何一个功能标记后，就不允许再在该月度内录入折扣调整单。add by houcq 2011-07-07
            		StringBuffer str = new StringBuffer()
            		.append(" SELECT * FROM eh_perioddiscount_h ")
            		.append(" WHERE nyear = "+_getDate().getYear())
            		.append(" AND nmonth = "+_getDate().getMonth())
            		.append(" and pk_corp = '"+_getCorp().getPrimaryKey()+"'")
            		.append(" AND NVL(dr,0)=0 ")
            		.append(" and qy_flag='Y'");
            		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            		try {
            			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(str.toString(), new MapListProcessor());
            			if(arr!=null && arr.size()>0){
            				getBillUI().showErrorMessage("本期折扣期间数据已结转，禁止新的制单操作!");
            				return;
            	        }
            		} catch (BusinessException e) {
            			e.printStackTrace();
            		}
            	}
            	
       		 //对公司期初建账进行控制时间：2009-12-15作者：张志远
                if(!super.getEveDiscount()){
               	 this.getBillUI().showErrorMessage("本月期间折扣未建帐，请至折扣期间计算节点，进行本月折扣期初建帐");
               	 return;
                }else{
               	//功能：对本月期初折扣的控制 时间：2009-12-15作者：张志远
               	 if(!super.getDiscount()){
                   	 this.getBillUI().showErrorMessage("本月期间折扣未计算，请至折扣期间计算节点，生成下月数据！");
                   	 return;
                    }
                }                
       		 super.onButton_N(bo, model);
       	 }
}
