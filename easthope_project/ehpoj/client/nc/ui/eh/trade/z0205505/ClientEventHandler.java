/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.z0205505;


import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0205505.FirstdiscountBVO;
import nc.vo.eh.trade.z0205505.FirstdiscountVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;
/**
 * ���� һ���ۿ۹���
 * @author �麣
 * 2008-04-08
 */

public class ClientEventHandler extends AbstractEventHandler {
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    String pk = null;
    FirstdiscountVO headvo = null;
    AggregatedValueObject aggvo = null;
    boolean iscopy=false;                                  //�Ƿ���Ŀ����
    //String pk_firstdiscount="";
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }


    protected void onBoEdit() throws Exception {
        String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("�������޸��������룡");
            return;
        }
 
        super.onBoEdit();
        getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(true);
        getBillUI().updateButtonUI();

    }

    protected void onBoDelete() throws Exception {
        String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("������ɾ���������룡");
            return;
        }
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        FirstdiscountVO hvo = (FirstdiscountVO) aggvo.getParentVO();
        nc.itf.eh.trade.pub.PubItf itf = (nc.itf.eh.trade.pub.PubItf)NCLocator.getInstance().lookup(nc.itf.eh.trade.pub.PubItf.class.getName());
        String uppk_firstdiscount = hvo.getDef_5()==null?"":hvo.getDef_5().toString();
        
        super.onBoDelete();
		if (!"".equals(uppk_firstdiscount))
		{
			/**ɾ��һ���ۿ۵��ݵ�ʱ�򣬰Ѹõ��ݱ��֮ǰ����Դ���ݵ����±�Ǹ�����Ϊ���±����ȡ����ֹ״̬ add by zqy 2010��11��29��17:09:54**/
	        String upsql = " update eh_firstdiscount set def_1 = 'Y',lock_flag = 'N' where pk_firstdiscount = '"+uppk_firstdiscount+"' ";
	            
	        itf.updateSQL(upsql);
	        String sql = " update eh_firstdiscount_b set lock_flag='N' where pk_firstdiscount='"+uppk_firstdiscount+"'";
	        itf.updateSQL(sql);
		}
    }
    
////      �ύ
        public void onBoCommit() throws Exception {
            // TODO �Զ����ɷ������
//            String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
//            if(!coperatorid.equals(_getOperator())){
//                getBillUI().showErrorMessage("�������������������ύ!");
//                return;
//            }
        	//add by houcq 2011-03-16 begin
            String  pk_firstdiscount=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_firstdiscount").getValueObject().toString();
//            if (!"".equals(vsourcebillid))
//            {            	
            nc.itf.eh.trade.pub.PubItf itf = (nc.itf.eh.trade.pub.PubItf)NCLocator.getInstance().lookup(nc.itf.eh.trade.pub.PubItf.class.getName());
            //������houcqע�͵�2011-06-07
//            AggregatedValueObject vo= getBillCardPanelWrapper().getBillVOFromUI(); 
//            FirstdiscountBVO[] vos=(FirstdiscountBVO[])vo.getChildrenVO();
//            StringBuilder sb = new StringBuilder("(");
//            for(int i=0;i<vos.length;i++)
//            {
//               String pk_invbasdoc =vos[i].getPk_invbasdoc();
//               String pk_cubasdoc =vos[i].getPk_cubasdoc();
//               sb.append("('"+pk_cubasdoc+"','"+pk_invbasdoc+"'),");
//             }   
//             sb.append("('',''))");
             StringBuilder sql = new StringBuilder();
             sql.append("update eh_firstdiscount_b set lock_flag='Y' where substr(pk_firstdiscount_b,0,4)='")
            .append(_getCorp().getPk_corp())
            .append("' and pk_firstdiscount<>'")
            .append(pk_firstdiscount)
            //.append("' and lock_flag<>'Y'")
            .append("' and nvl(lock_flag,'N')<>'Y'")//modify by houcq 2011-06-07
            .append("  and nvl(dr,0)=0")//add by houcq 2011-06-07
            .append(" and (pk_invbasdoc, pk_cubasdoc) in")
            //add by houcq 2011-06-07 begin
            //.append(sb.toString());
             .append(" (select pk_invbasdoc, pk_cubasdoc from eh_firstdiscount_b")
             .append(" where pk_firstdiscount='"+pk_firstdiscount+"')");
             //end 
            itf.updateSQL(sql.toString());
            super.onBoCommit();
            
        }
        

        /* ���� Javadoc��
         * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
         */
        protected void onBoElse(int intBtn) throws Exception {
            switch (intBtn)
            {
                case IEHButton.GENRENDETAIL:    //������ϸ
                    onBoGENRENDETAIL();
                    break;
                case IEHButton.CARDAPPROVE:    //ˢ�����
                    onBoCARDAPPROVE();
                    break;
                case IEHButton.STOCKCHANGE:     //�ۿ۱��
                    onBozkbg();
                    break;
            }  
            super.onBoElse(intBtn);
        }
        

    
    @SuppressWarnings("unused")
	private void onlockbill() throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        FirstdiscountVO ivo = (FirstdiscountVO) aggvo.getParentVO();
        String lock_flag = ivo.getLock_flag()==null?"N":ivo.getLock_flag().toString();
        String primaryKey = ivo.getPrimaryKey();
        if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("�õ����Ѿ��ر�!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ�����йرղ���?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                ivo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(ivo);
                getBillUI().showWarningMessage("�Ѿ��رճɹ�");
                onBoRefresh();
            }
            else{
                return;
            }
        }
        setBoEnabled();
    }
    @Override
    protected void setBoEnabled() throws Exception {
        super.setBoEnabled();
    }


    private void onBozkbg() {
    	 try {
    		 aggvo = getBillUI().getVOFromUI();
    		 //aggvo = getBillUI().getChangedVOFromUI();//modify by houcq 2011-04-28
             headvo=(FirstdiscountVO)aggvo.getParentVO();
             FirstdiscountBVO[] bvos = (FirstdiscountBVO[])aggvo.getChildrenVO(); 
             pk=headvo.getPrimaryKey();
             int  billstatus=headvo.getVbillstatus().intValue();
             String new_flag = headvo.getDef_1()==null?"N":headvo.getDef_1();            //���±��
                //���û��ѡ����Ŀ��ʾ����
                if(pk==null||pk.equals("")){
                    getBillUI().showErrorMessage("��ѡ��һ����Ŀ��");
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
                	String pk_firstdiscount=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_firstdiscount").getValueObject()==null?" ":
                         getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_firstdiscount").getValueObject().toString();
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
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("modifyprice").setValue(null);//modify by houcq 2011-05-05
                    /**��������(�汾�����ѱ��֮ǰ�ĵ���PK���ӵ���ͷ��def_5�ֶ���,����ɾ����ʱ�����֮ǰ���ݵ����±�Ǽ��رձ��) add by zqy 2010��11��29��16:55:42**/
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_5").setValue(""+pk_firstdiscount+"");
                    //add by houcq 2011-03-15 begin
                    String[] hformual=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_5").getEditFormulas();
                    getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(hformual);
                  //add by houcq 2011-03-15 end
                    /*********end********/
                    //add by houcq 2011-03-24 begin �ۿ۱��ʱִ�����ں���Ч������Ҫʹ�õ�ǰ�ģ���������ǰ��
//                    UFDate zxdate = headvo.getZxdate();
//                    UFDate yxdate = headvo.getYxdate();
                    UFDate zxdate = _getDate();
                    UFDate yxdate = PubTools.getDateAfter(_getDate(), 91);
                    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("zxdate", zxdate);
                    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("yxdate", yxdate);
                   
                  //add by houcq 2011-03-24 end
                    //���ʱ�������ۿ۶�����ԭ�ۿ۶�,��������µ��Ƽ� add by wb at 2008-8-28 15:35:21
                    if(bvos!=null&&bvos.length>0){
                    	for(int i=0;i<bvos.length;i++){
                    		String[] formual= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvbascode").getEditFormulas();//��ȡ�༭��ʽ
                    		getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
                    		//modify by houcq 2011-04-28
                    		aggvo = getBillUI().getVOFromUI();
                           FirstdiscountBVO[] mybvos = (FirstdiscountBVO[])aggvo.getChildrenVO(); 
                    		
                    		UFDouble olddiscount = mybvos[i].getNewdiscount();
                    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(olddiscount, i, "olddiscount");
                    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "tzdiscount");
                    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(zxdate, i, "zxdate");
                    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(yxdate, i, "yxdate");
                    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "lock_flag");//add by houcq 2011-03-15 �ۿ۱��ʱ��ȡ��������ֹ���
                    		//ȡ�ø��º���Ƽۣ�������ۡ�ʱ�䣺2010-01-14.���ߣ���־Զ
                    		//UFDouble paijia =mybvos[i].getOldprice(); 
                    		//UFDouble paijia =bvos[i].getOldprice();//modify by houcq 2011-04-28
                    		String pk_invbasdoc=mybvos[i].getPk_invbasdoc();//modify by houcq 2011-05-04
                    		UFDouble paijia = new PubTools().getInvPrice(pk_invbasdoc,_getDate());//modify by houcq 2011-05-04
                    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(paijia, i, "oldprice");//add by houcq 2011-05-06
                    		UFDouble jinjia = paijia.sub(olddiscount);
                    		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(jinjia, i, "newprice");
                    		
                    	}
                    }
                    //���ø��Ʊ��Ϊtrue
                    iscopy=true;
                    getBillUI().updateUI();
                    
                }else{
                	return;
                }
                  
         } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
	}




	@Override
    public void onBoAdd(ButtonObject arg0) throws Exception {
        super.onBoAdd(arg0);
         getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(true);
         getBillUI().updateButtonUI();
    }
  
    @Override
    protected void onBoCancel() throws Exception {
        // TODO Auto-generated method stub
        super.onBoCancel();
         getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(false);
         iscopy=false;
         pk=null;
         getBillUI().updateUI();
         getBillUI().updateButtonUI();
    }
    
  
 
  
        @SuppressWarnings("unchecked")
        @Override
        public void onBoSave() throws Exception {
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
            BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
             if (model != null) {
                 int rowCount = model.getRowCount();
                 if (rowCount < 1) {
                     NCOptionPane.showMessageDialog(getBillUI(), "�����в���Ϊ��!");
                     return;
                 }
             }
           
        	//���� 05.05
            //�Ƽ۲���Ϊ�յ��ж�
            int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for(int i=0;i<rows;i++){
            	String  price=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "oldprice")==null?"":getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "oldprice").toString();
            	if(price.equals("0.00") || price.equals("") || price==null){
            		getBillUI().showErrorMessage("��"+(i+1)+"���Ƽ�û��¼��!");
            		return;
            	}
            }
            //���� 05.05
            //Ψһ��У��
            BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
            int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","pk_cubasdoc"});
            if(res==1){
                getBillUI().showErrorMessage("���Ͽ������ظ�,����");
                return;
            }
            //add by houcq 2011-03-14 ����Դ����ʱ������Դ���ݵ���ֹ��Ǵ��ϣ���ȡ����Դ���ݵ����±��
            AggregatedValueObject vo= getBillCardPanelWrapper().getBillVOFromUI();    
            FirstdiscountVO fvo = (FirstdiscountVO) vo.getParentVO();
            String vsourcebillid = fvo.getDef_5()==null?"":fvo.getDef_5().toString();
            if (!"".equals(vsourcebillid))
            {
            	String sql1 = "update eh_firstdiscount set lock_flag='Y',def_1='N' where pk_firstdiscount='"+fvo.getDef_5()+"'";
            	String sql2 = "update eh_firstdiscount_b set lock_flag='Y' where substr(pk_firstdiscount_b,0,4)='"+_getCorp().getPk_corp()+"' and pk_firstdiscount='"+fvo.getDef_5()+"'";
            	nc.itf.eh.trade.pub.PubItf itf = (nc.itf.eh.trade.pub.PubItf)NCLocator.getInstance().lookup(nc.itf.eh.trade.pub.PubItf.class.getName());
                itf.updateSQL(sql1);
                itf.updateSQL(sql2);
            }
            //add by houcq 2011-03-14 end
            //�ж���ִ���ڼ����Ч�ڼ��е������Ƿ�����һ���ۿ��趨                 
            String zxdate=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zxdate").getValueObject().toString();
            String yxdate=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("yxdate").getValueObject().toString();
            IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
            //add by houcq 2011-03-15
            //����ͷ�ͻ������ϣ�Ƭ���Ƿ���δ��ֹ��һ���ۿۣ�������ʾ"�ÿͻ�(���ϣ�Ƭ��)�����ۿ����ߣ�������ۿ۱��"
            Object h_custom=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("custom").getValueObject();
            Object h_pk_areacl=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_areacl").getValueObject();
            Object h_pk_invbasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject();
            
            StringBuffer hsql=new StringBuffer()
            .append(" select custom,pk_areacl,pk_invbasdoc")
            .append(" from eh_firstdiscount ")
            .append(" where (('"+zxdate+"' > zxdate and '"+zxdate+"' <yxdate) or  ")
            .append(" ('"+yxdate+"'>zxdate and '"+yxdate+"'<yxdate) or ('"+zxdate+"'<=zxdate and '"+yxdate+"'>=yxdate)) ")
            .append(" and isnull(def_1,'Y')='Y'  and isnull(lock_flag,'N')='N' ")
            .append(" and isnull(dr,0)=0 and pk_corp='"+_getCorp().getPk_corp()+"' ");
            //add by houcq 2011-04-11 begin
            String pk_firstdiscount=fvo.getPk_firstdiscount();
            if (pk_firstdiscount!=null)
            {
            	hsql.append(" and pk_firstdiscount<>'"+pk_firstdiscount+"'");
            }
            //end
            if (h_custom!=null)
            {
            	hsql.append(" and custom ='"+h_custom.toString()+"'");
            	ArrayList<HashMap> arr=(ArrayList)iUAPQueryBS.executeQuery(hsql.toString(),new MapListProcessor());
            	if (arr!=null&&arr.size()>0)
            	{
            		getBillUI().showErrorMessage("�ÿͻ������ۿ����ߣ�������ۿ۱��!");
                    return;
            	}
            }
            if (h_pk_areacl!=null)
            {
            	hsql.append(" and pk_areacl='"+h_pk_areacl.toString()+"'");
            	ArrayList<HashMap> arr=(ArrayList)iUAPQueryBS.executeQuery(hsql.toString(),new MapListProcessor());
            	if (arr!=null&&arr.size()>0)
            	{
            		getBillUI().showErrorMessage("��Ƭ�������ۿ����ߣ�������ۿ۱��!");
                    return;
            	}
            }
            if (h_pk_invbasdoc!=null)
            {
            	hsql.append(" and pk_invbasdoc='"+h_pk_invbasdoc.toString()+"'");
            	ArrayList<HashMap> arr=(ArrayList)iUAPQueryBS.executeQuery(hsql.toString(),new MapListProcessor());
            	if (arr!=null&&arr.size()>0)
            	{
            		getBillUI().showErrorMessage("�����������ۿ����ߣ�������ۿ۱��!");
                    return;
            	}
            }
            
            /*************************end            * ***********************************/
            
            if(iscopy){
            	IVOPersistence  iVOPersistence =   (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
                headvo.setDef_1("N");        //���ɰ汾�����±��ȥ��
                headvo.setLock_flag(new UFBoolean(true));
            	iVOPersistence.updateVO(headvo);   //��ͷ
                iscopy = false;
                pk=null;
            }
            
            //getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(false);
            
            super.onBoSave();
            setBoEnabled();
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
        String pk_invbasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?"%":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
        String pk_cubasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("custom").getValueObject()==null?"%":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("custom").getValueObject().toString();
        String pk_areacl=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_areacl").getValueObject()==null?"%":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_areacl").getValueObject().toString();
        FirstdiscountVO dvo = (FirstdiscountVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        UFDate zxdate = dvo.getZxdate();
        UFDate yxdate = dvo.getYxdate();

      //add by houcq 2011-04-25 begin
        if (pk_invbasdoc.equals("%") && pk_cubasdoc.equals("%") && pk_areacl.equals("%"))
        {
        	getBillUI().showErrorMessage("��Ʒ,�ͻ�,Ƭ����ѡһ��!");
        	return;
        }
        //end
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        StringBuffer sql=new StringBuffer();
        sql.append(" select a.*,b.newprice from (");//add by houcq 2011-04-25
        sql.append(" select aa.pk_cumandoc pk_cubasdoc, b.pk_invbasdoc, c.pk_measdoc ") 
        //.append(" ,c.invcode,c.invname,c.invspec,c.invtype,h.measname,c.def1,g.brandname,a.custname,c.def3 ")
        .append(" ,c.invcode,c.invname,c.invspec,c.invtype,h.measname,c.def1,g.brandname,a.custname ")
        .append("   from bd_cubasdoc  a, ") 
        .append("        bd_cumandoc  aa, ") 
        .append("        eh_custkxl   b, ") 
        .append("        bd_invbasdoc c, ") 
        .append("        bd_invmandoc cc ") 
        .append(" ,eh_brand g ")
        .append(" ,bd_measdoc h ")
        .append("  where a.pk_cubasdoc = aa.pk_cubasdoc ") 
        .append("    and aa.pk_cumandoc = b.pk_cubasdoc ") 
        .append("    and b.pk_invbasdoc = cc.pk_invmandoc ") 
        .append("    and c.pk_invbasdoc = cc.pk_invbasdoc ") 
        .append(" and g.pk_brand = c.invpinpai ")
        .append(" and h.pk_measdoc = c.pk_measdoc ")
        .append("    and aa.pk_cumandoc like '"+pk_cubasdoc+"' ") 
        .append("    and b.pk_invbasdoc like '"+pk_invbasdoc+"' ") 
        .append("    and nvl(aa.def5, ' ') like '"+pk_areacl+"' ") 
        .append("    and aa.pk_corp = '"+_getCorp().getPk_corp()+"' ") 
        .append("    and nvl(aa.dr, 0) = 0 ") 
        .append("    and nvl(b.dr, 0) = 0 ") 
        .append("    and nvl(cc.dr, 0) = 0 ")
        .append(" and nvl(h.dr,0) = 0 and nvl(g.dr,0) = 0 ")
        .append(" ) a,(select * from (select rank() over(partition by pk_invbasdoc order by dapprovetime desc) rk,aaaa.* from ")
        .append(" (select to_date(nvl(a.dapprovetime,a.dapprovedate),'yyyy-mm-dd:hh24:mi:ss') dapprovetime,b.pk_invbasdoc,b.newprice from eh_price a , eh_price_b b")
        .append(" where a.pk_price=b.pk_price  and a.vbillstatus=1  and nvl(a.dr,0)=0   and nvl(b.dr,0)=0")
        .append(" and a.pk_corp='"+_getCorp().getPk_corp()+"'")
        .append(" and '"+_getDate()+"' between a.zxdate and a.yxdate)aaaa) t")
        .append(" where t.rk<2) b where a.pk_invbasdoc= b.pk_invbasdoc(+)");
        ArrayList<HashMap> arr=(ArrayList<HashMap>)iUAPQueryBS.executeQuery(sql.toString(),
                new MapListProcessor());
             
        int iRet = getBillUI().showYesNoMessage("������ϸǰҪ��ձ��壬�Ƿ�ȷ��?");
        
        if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            int rowcount=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
             int[] rows=new int[rowcount];
            for(int i=rowcount - 1;i>=0;i--){
                rows[i]=i;
            }
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
            this.getBillUI().updateUI();
        }else{
            return;
        }
            
        
        
        for(int i=0;i<arr.size();i++){
            onBoLineAdd();
            HashMap hm=(HashMap)arr.get(i);
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString(), i, "pk_invbasdoc");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString(), i, "pk_cubasdoc");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(zxdate, i, "zxdate");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(yxdate, i, "yxdate");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invcode"), i, "vinvbascode");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invname"), i, "vinvbasname");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invspec"), i, "vguige");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invtype"), i, "vtype");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("measname"), i, "vmeasdoc");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("def1"), i, "vcolor");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("brandname"), i, "vbrand");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("custname"), i, "vcubasdoc");
            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("newprice"), i, "oldprice");
//            String[] loadformual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_invbasdoc").getLoadFormula();//��ȡ��ʾ��ʽ
//            getBillCardPanelWrapper().getBillCardPanel().getBillModel().execFormula(i, loadformual);
//            String[] custformual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_cubasdoc").getLoadFormula();//��ȡ��ʾ��ʽ
//            getBillCardPanelWrapper().getBillCardPanel().getBillModel().execFormula(i, custformual);
        }
      
        getBillUI().updateUI();
    }
    
    
    //������ϸ
    protected void onBoCARDAPPROVE() throws Exception{
       String pk_firstdiscount=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_firstdiscount").getValueObject()==null?
               "":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_firstdiscount").getValueObject().toString();
       checkflow(pk_firstdiscount);
    }
    
    @Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    	FirstdiscountVO dvo = (FirstdiscountVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	UFDate zxdate = dvo.getZxdate();
    	UFDate yxdate = dvo.getYxdate();
    	int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(zxdate, row, "zxdate");
    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(yxdate, row, "yxdate");
    }   
    
    
}

