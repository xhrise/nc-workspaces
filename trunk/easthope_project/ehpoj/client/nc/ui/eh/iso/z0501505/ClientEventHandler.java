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
 * ˵���������� 
 * @author ����Դ 
 * ʱ�䣺2008-4-11
 */
@SuppressWarnings("deprecation")
public class ClientEventHandler extends AbstractEventHandler {
    int addflag = 0;                    //����������Դ 0 ����  1 �ջ�֪ͨ�� 2 ˾����
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@SuppressWarnings({ "unchecked" })
    public void onBoSave() throws Exception {
		
		// �ǿ��ж�
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//�жϱ��岻��Ϊ��
		 getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
	        BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
	        if (model != null) {
	            int rowCount = model.getRowCount();
	            if (rowCount < 1) {
	                NCOptionPane.showMessageDialog(getBillUI(), "�����в���Ϊ��!");
	                return;
	            }
	        }
		 //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_project"});
        if(res==1){
            getBillUI().showErrorMessage("�����Ŀ�Ѿ����ڣ����������!");
            return;
        } 
        
        /*********** Editor houcq 2011-11-03 Start ***********/
        AggregatedValueObject agg = getBillUI().getVOFromUI();
        StockSampleVO report=(StockSampleVO) agg.getParentVO();
		//��������ֹͬʱ����������
		try {
			
			if(!Toolkits.isEmpty(report.getVsourcebillid())){
				String sourcebillid = report.getVsourcebillid().toString();
				boolean bLockSuccess = LockBO_Client.lockPK(sourcebillid, _getOperator(),null);
				if (!bLockSuccess) {					
					getBillUI().showWarningMessage("��Դ�����ѱ�ʹ��,��ȡ������!");					
					return;
				}
			}
			/*//��ʼ����ť����
				public static final int OP_INIT=4;
				//�༭ʱ��ť����
				public static final int OP_EDIT = 0;
				//��������״̬
				public static final int OP_ADD = 1;
				//�Ǳ༭ʱ��ť����
				public static final int OP_NOTEDIT = 2;
				//���յ���ʱ��������״̬
				public static final int OP_REFADD = 3;
				//�������ӡ�ֻ���޸ķǱ༭ʱ��ť����
				public static final int OP_NOADD_NOTEDIT = 5;
				//�������ӡ��޸�ʱ��ť����
				public static final int OP_NO_ADDANDEDIT = 6;
				//���еĹ��ܶ�����
				public static final int OP_ALL=7;
			*/
			int ret= getBillUI().getBillOperate();
			if (ret!=0)
			{
				UFDouble rkamount = new UFDouble(report.getRkamount()==null?"0":report.getRkamount().toString());
	            if(rkamount.equals(0)){
	                getBillUI().showErrorMessage("������������Ϊ�գ����ʵ��");
	                return;
	            }				
				//<�޸�>���ܣ�vsourcebilltype��NULL�ж�if�����á����ڣ�2009-08-11���ߣ���־Զ��
		        String vsourcebilltype  = report.getVsourcebilltype()==null?"":report.getVsourcebilltype();  
		        //��������˾������������ʱ��˾��������˾����Ǽ���   add by wb at 2008-6-7 21:06:45
		        if(vsourcebilltype.equals(IBillType.eh_z06005)){
		        	StockSampleVO smpleVO = (StockSampleVO)getBillUI().getChangedVOFromUI().getParentVO();
		        	String pk_sbbills = smpleVO.getPk_sbbills();       // ˾������������
		        	String sql ="select * from eh_sbbill where pk_sbbill in ("+pk_sbbills+") and NVL(dr,0)=0 and ycy_flag = 'Y'";
		        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		        	if (al.size()>0)
		        	{
		        		getBillUI().showErrorMessage("��Դ�����ѱ�ʹ��,��ȡ������!");
		                return;
		        	}
		        	String updateSQL = "update eh_sbbill set ycy_flag = 'Y' where pk_sbbill in ("+pk_sbbills+") and NVL(dr,0)=0";
		        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		        	pubItf.updateSQL(updateSQL);
		        }
		          
		             
		        //���ջ�֪ͨ���б��������ѱ��������󣬻�д����ǵ��ջ�֪ͨ�����Ƿ�����ֶ���sfcy_flag  add by zqy 2008-6-8 15:12:51
		        if(vsourcebilltype.equals(IBillType.eh_z0151001)){
		        	//�����д
		        	 String pk_receipt_b = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_receipt_b").getValueObject()==null?"":
		                 getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_receipt_b").getValueObject().toString(); //���ε��ݵ��ֱ�
		        	PubItf pubitf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		        	String sql1 ="select * from eh_stock_receipt_b where pk_receipt_b = '"+pk_receipt_b+"' and NVL(dr,0)=0 and rk_flag = 'Y'";
		        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		        	ArrayList al1=(ArrayList) iUAPQueryBS.executeQuery(sql1, new MapListProcessor());
		        	if (al1.size()>0)
		        	{
		        		getBillUI().showErrorMessage("��Դ�����ѱ�ʹ��,��ȡ������!");
		                return;
		        	}
		        	
		        	 String sql2="update eh_stock_receipt_b set rk_flag = 'Y' where pk_receipt_b ='"+pk_receipt_b+"'";
		        	 pubitf.updateSQL(sql2);
		        	//��ͷ��д
		        	
		            String pk_receipt = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?"":
		                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString(); //���ε��ݵ�����
		        	
		            String sql4 ="select * from eh_stock_receipt where pk_receipt = '"+pk_receipt+"' and NVL(dr,0)=0 and yjcy_flag = 'Y'";
		        	ArrayList al2=(ArrayList) iUAPQueryBS.executeQuery(sql4, new MapListProcessor());
		        	if (al2.size()>0)
		        	{
		        		getBillUI().showErrorMessage("��Դ�����ѱ�ʹ��,��ȡ������!");
		                return;
		        	}
		        	String sql="select count(*) amount,'A' flag  from eh_stock_receipt_b where (NVL(issb,'N')='N' or " +
		        			" issb='') and (NVL(sfcy_flag,'N')='N' or sfcy_flag ='') and NVL(dr,0)=0 and allcheck='Y' " +
		        			" and pk_receipt='"+pk_receipt+"' union all select count(*) amount,'B' flag  from eh_stock_receipt_b " +
		        			" where (NVL(issb,'N')='N' or issb='') and (NVL(sfcy_flag,'N')='N' or sfcy_flag ='') " +
		        			"  and NVL(dr,0)=0 and allcheck='Y' and rk_flag='Y' and  pk_receipt='"+pk_receipt+"' "; 
		           ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		           HashMap hm1=new HashMap();//�ŵ�����ǵĸ�
			       HashMap hm2=new HashMap();//�ŵ�һ���ĸ���
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
        //�����ջ�֪ͨ�����ɳ�����ʱ�����������ǲ�����༭��,����������в���
        if(bocode.equals("�ջ�֪ͨ��")){
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
        //����ԭ��˾�������ɳ�����ʱ���������ƺ�ԭ��˾�������ǲ�����༭�ģ�����������в���
        else if(bocode.equals("˾����")){
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
        }else if(bocode.equals("��ⵥ")){
        	addflag = 2;
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);	 
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("carnum").setEnabled(true);	 
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("type_flag", "1");
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("th_flag", "R");
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("rkamount").setEnabled(false);
            
//            //�������
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
//    	   String[] formual=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("custname").getLoadFormula();//��ȡ�༭��ʽ
//    	   getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
        } else if(bocode.equals("���Ƶ���")){
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
    	int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
    	if(res==0){
    		return;
    	}
		StockSampleVO smpleVO = (StockSampleVO)getBillUI().getChangedVOFromUI().getParentVO();
    	String vbilltype = smpleVO.getVsbbilltype()==null?"":smpleVO.getVsbbilltype();
    	//��������˾�����ĳ�����ɾ��ʱ��д˾�������ѳ������  add by wb at 2008-6-7 22:06:22
    	if(vbilltype.equals(IBillType.eh_z06005)){
    	String pk_sbbills = smpleVO.getPk_sbbills();       // ˾������������
    	String updateSQL = "update eh_sbbill set ycy_flag = 'N' where pk_sbbill in ("+pk_sbbills+") and NVL(dr,0)=0";
    	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
    	pubItf.updateSQL(updateSQL);
    	}
        
         //�����������֪ͨ���ĳ�����ɾ��ʱ��д����ǵ��Ƿ�����ֶ���sfcy_flag add by zqy 2008-6-8 15:32:09
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
//                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString(); //���ε��ݵ�����
//            
//            PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
//            pubItf.deletesfcyFlag(pk_invbasdoc,pk_receipt);
//            
//            //ɾ���������֪ͨ���ĳ�����ʱ���Ա�������������������жϣ�����ȶԱ�ͷ��д add by zqy 2008-6-11 15:06:04
//            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//            StringBuffer sql2 = new StringBuffer()
//            .append(" select count(*) mount1 from eh_stock_receipt_b where pk_receipt='"+pk_receipt+"' and isnull(dr,0)=0 ");
//            UFDouble mount1 = null;
//            try {
//                ArrayList arr2 = (ArrayList) iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
//                if(arr2!=null && arr2.size()>0){
//                    for(int j=0;j<arr2.size();j++){
//                        HashMap hm2 = (HashMap) arr2.get(j);
//                        mount1 = new UFDouble(hm2.get("mount1")==null?"0":hm2.get("mount1").toString()); //�����������
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
//                        mount = new UFDouble(hm1.get("mount")==null?"0":hm1.get("mount").toString()); //�Ѿ���д��ǵ�����
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
            case IEHButton.LOCKBILL:    //�رյ���
                onBoLockBill();
                break;
            case IEHButton.Prev:    //��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:    //��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
        }   
    }
    
    private void onBoBrows(int intBtn) throws java.lang.Exception {
        // ����ִ��ǰ����
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
        // ����ִ�к���
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
                                                                     * "ת����:" +
                                                                     * getBufferData().getCurrentRow() +
                                                                     * "ҳ���)"
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
   }
    
}

    
