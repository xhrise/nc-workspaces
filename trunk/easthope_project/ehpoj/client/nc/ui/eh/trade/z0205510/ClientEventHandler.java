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
 * ����˵���������ۿ�
 * 2008-4-8 16:43:39
 */
public class ClientEventHandler extends AbstractEventHandler {
    SeconddiscountVO headvo = null;
    String pk = null;
    boolean iscopy=false;                                  //�Ƿ���Ŀ����
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
     protected void onBoElse(int intBtn) throws Exception {
            switch (intBtn)
            {
                case IEHButton.GENRENDETAIL:    //������ϸ
                    onBoGENRENDETAIL();
                    break;
                case IEHButton.STOCKCHANGE:     //�ۿ۱��
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
                getBillUI().showErrorMessage("�������޸��������룡");
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
                getBillUI().showErrorMessage("������ɾ���������룡");
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
                   
//            ���ð�ť�Ŀ���״̬
                protected void setBoEnabled() throws Exception {
                   super.setBoEnabled();
                }
        
                @SuppressWarnings("unchecked")
				public void onBoSave() throws Exception {
                    getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
                    AggregatedValueObject vo= getBillCardPanelWrapper().getBillVOFromUI();
                    SeconddiscountVO hvo = (SeconddiscountVO)vo.getParentVO();
                    int invjstype = hvo.getInvjstype();			//��Ʒ���㷽ʽ
                    BillModel model1 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("range");
                    if (model1 != null&&invjstype==1) {
                        int rowCount = model1.getRowCount();
                        if (rowCount < 1) {
                        	getBillUI().showErrorMessage("��Ʒ��Χ�����в���Ϊ��!");
                            return;
                        }
                    }
                    BillModel model2 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("polic");
                    if (model2 != null) {
                        int rowCount = model2.getRowCount();
                        if (rowCount < 1) {
                        	getBillUI().showErrorMessage("�ۿ����߱����в���Ϊ��!");
                            return;
                        }
                    }
                    BillModel model3 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("checkinv");
                    if (model3 != null) {
                        int rowCount = model3.getRowCount();
                        if (rowCount < 1) {
                        	getBillUI().showErrorMessage("�����Ʒ�����в���Ϊ��!");
                            return;
                        }
                    }
                    
                    //�����޸���ϸ���ɵ����ߣ��ֽ�����2��У����ȥ  zqy 2010��6��29��10:54:57
                    
                    //Ψһ��У��
                    BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel("checkinv");
                    int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","pk_cubasdoc"});
                    if(res==1){
                        getBillUI().showErrorMessage("�����Ʒҳǩ�����Ͽ������ظ�!");
                        return;
                    }
                    
                    BillModel bm2=getBillCardPanelWrapper().getBillCardPanel().getBillModel("range");
                    int res2=new PubTools().uniqueCheck(bm2, new String[]{"pk_invbasdoc"});
                    if(res2==1){
                        getBillUI().showErrorMessage("��Ʒ��Χҳǩ���������ظ�!");
                        return;
                    }
                    
                    //�ж���ִ���ڼ����Ч�ڼ��е������Ƿ�����һ���ۿ��趨
                    String pk_jscubasdoc = hvo.getPk_cubasdoc();			//����ͻ�
                    String pk_supcubasdoc = hvo.getPk_subcubasdoc();		//�ӿͻ�
                    String startdate=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("startdate").getValueObject().toString();
                    String enddate=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("enddate").getValueObject().toString();
                    String policetype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("policetype").getValueObject().toString();		//��������
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
                        headvo.setDef_1("N");                     //���ɰ汾�����±��ȥ��
                        headvo.setLock_flag(new UFBoolean(true)); //�رվɰ汾
                    	iVOPersistence.updateVO(headvo);          //��ͷ
                        iscopy = false;
                        pk=null;
                        headvo = null;
                    }
                    getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(false);
                    getBillUI().updateButtonUI();
                }
            
            /* ���� Javadoc��
             * @see nc.ui.trade.manage.ManageEventHandler#onBoReturn()
             */
            protected void onBoReturn() throws Exception {
                // TODO �Զ����ɷ������
                super.onBoReturn();
                setBoEnabled();
            }
            
            /* ���� Javadoc��
             * @see nc.ui.trade.manage.ManageEventHandler#onBoCard()
             */
            protected void onBoCard() throws Exception {
                // TODO �Զ����ɷ������
                super.onBoCard();
                setBoEnabled();
               
            }
            /* ���� Javadoc��
             * @see nc.ui.trade.manage.ManageEventHandler#onBoRefresh()
             */
            protected void onBoRefresh() throws Exception {
                // TODO �Զ����ɷ������
                super.onBoRefresh();
                setBoEnabled();
            }
            
            
            /**
             * ���ܣ�
             * <p>������ϸ</p>
             */
            @SuppressWarnings("unchecked")
            protected void onBoGENRENDETAIL() throws Exception{
                String pk_invbasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?"":	//��Ʒ
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
                String pk_subcubasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_subcubasdoc").getValueObject()==null?""://�ͻ�
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_subcubasdoc").getValueObject().toString();                  
                String pk_areacl=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_areacl").getValueObject()==null?"":			//Ƭ��
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_areacl").getValueObject().toString();
                
//                if(pk_cubasdoc.equals("%")){
//                    getBillUI().showErrorMessage("��ѡ��ͻ���");
//                    return;
//                }
                //add by houcq 2011-04-23 begin
                if (pk_invbasdoc.equals("") && pk_subcubasdoc.equals("") && pk_areacl.equals(""))
                {
                	getBillUI().showErrorMessage("��Ʒ,�ͻ�,Ƭ����ѡһ��!");
                	return;
                }
                //end
                if(!pk_invbasdoc.equals("") && !pk_subcubasdoc.equals("") && !pk_areacl.equals("")){
                	getBillUI().showErrorMessage("��Ʒ,�ͻ�,Ƭ������ͬʱѡ��,ֻ����ѡ������һ������!");
                	return;
                }else if(!pk_invbasdoc.equals("") && !pk_subcubasdoc.equals("")){
                	getBillUI().showErrorMessage("��Ʒ,�ͻ�����ͬʱѡ��,ֻ����ѡ������һ������!");
                	return;
                }else if(!pk_invbasdoc.equals("") && !pk_areacl.equals("")){
                	getBillUI().showErrorMessage("��Ʒ,Ƭ������ͬʱѡ��,ֻ����ѡ������һ������!");
                	return;
                }else if(!pk_subcubasdoc.equals("") && !pk_areacl.equals("")){
                	getBillUI().showErrorMessage("�ͻ�,Ƭ������ͬʱѡ��,ֻ����ѡ������һ������!");
                	return;
                }
                
                String currentModel = getBillCardPanelWrapper().getBillCardPanel().getCurrentBodyTableCode();		//ҳǩ
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
                if(!pk_subcubasdoc.equals("") && pk_subcubasdoc.length()>0){	//�ͻ� 
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
                if(!pk_subcubasdoc.equals("") && pk_subcubasdoc.length()>0){	//�ͻ� 
                   
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
	                int iRet = getBillUI().showYesNoMessage("������ϸǰҪ��ձ��壬�Ƿ�ȷ��?");
	                if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
	                	//��ղ�Ʒ��Χ
	                	if(currentModel.equals("range")){
		                    int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).getRowCount();
		                    int[] rows=new int[rowcount];
		                    for(int i=rowcount - 1;i>=0;i--){
		                        rows[i]=i;
		                    }
		                    getBillCardPanelWrapper().getBillCardPanel().getBillModel(currentModel).delLine(rows);
	                	}
	                    //��ս����Ʒ
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
                        //-------��Ʒ��Χ������ϸ
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
                        //------�����Ʒ������ϸ            add by wb 2008-9-8 16:27:40 
                    	if(currentModel.equals("checkinv")){
	                        UFDouble firstdscount = new UFDouble(hm.get("newdiscount")==null?"0":hm.get("newdiscount").toString());		//һ���ۿ�
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
                    String new_flag = headvo.getDef_1()==null?"N":headvo.getDef_1();            //���±��
                       //���û��ѡ����Ŀ��ʾ����
                       if(pk==null||pk.equals("")){
                           getBillUI().showErrorMessage("��ѡ��һ�ŵ���!");
                           return;
                       }
                       //�������ظ�����
                       if(!new_flag.equals("Y")){
                           getBillUI().showErrorMessage("�������°汾,���ܱ��!");
                           return;
                       }
                       //ֻ������ͨ������Ŀ���������
                       if(billstatus!=IBillStatus.CHECKPASS){
                           getBillUI().showErrorMessage("ֻ������ͨ������Ŀ����������");
                           return;
                       }
                       
                       int ok=getBillUI().showYesNoMessage("�Ƿ�ȷ�Ͻ�����Ŀ���?");
                       if(ok==MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                           onBoCopy();
                           getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(true);
                           getBillUI().updateButtonUI();
                           String billNo = BillcodeRuleBO_Client.getBillCode(getUIController().getBillType(),_getCorp().getPk_corp(),null, null);
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(billNo);
                           //���汾�Ž��иı���ԭ�����ϼ�1
                           Integer ver= Integer.parseInt(headvo.getDef_2());
                           ver=ver+1;
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_2").setValue(String.valueOf(ver));
                           getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
                           getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapproveid").setValue(null);
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dapprovedate").setValue(null);
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapprovenote").setValue(null);
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(new Integer(8));
                           getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_1").setValue("Y");            //���±��
                           
                           //���ʱ�������ۿ۶��������µ��Ƽ�,һ���ۿ� add by wb at 2008��10��20��18:20:21
                           if(checkVOS!=null&&checkVOS.length>0){
                           	for(int i=0;i<checkVOS.length;i++){
                           		String[] formual= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").getEditFormulas();//��ȡ�༭��ʽ
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
                           //���ø��Ʊ��Ϊtrue
                           iscopy=true;
                           getBillUI().updateUI();
                        }
                       }
                } catch (Exception e) {
                    e.printStackTrace();
                }
       	}
            
            //���ö�ҳǩ�Ĵ�ӡģ�� 
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
            	if ("����".equals(bo.getCode()))
            	{
            		//���ۿ��ڼ�����������������ݡ����ù��ܣ��κ�һ�����ܱ�Ǻ󣬾Ͳ��������ڸ��¶���¼���ۿ۵�������add by houcq 2011-07-07
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
            				getBillUI().showErrorMessage("�����ۿ��ڼ������ѽ�ת����ֹ�µ��Ƶ�����!");
            				return;
            	        }
            		} catch (BusinessException e) {
            			e.printStackTrace();
            		}
            	}
            	
       		 //�Թ�˾�ڳ����˽��п���ʱ�䣺2009-12-15���ߣ���־Զ
                if(!super.getEveDiscount()){
               	 this.getBillUI().showErrorMessage("�����ڼ��ۿ�δ���ʣ������ۿ��ڼ����ڵ㣬���б����ۿ��ڳ�����");
               	 return;
                }else{
               	//���ܣ��Ա����ڳ��ۿ۵Ŀ��� ʱ�䣺2009-12-15���ߣ���־Զ
               	 if(!super.getDiscount()){
                   	 this.getBillUI().showErrorMessage("�����ڼ��ۿ�δ���㣬�����ۿ��ڼ����ڵ㣬�����������ݣ�");
                   	 return;
                    }
                }                
       		 super.onButton_N(bo, model);
       	 }
}
