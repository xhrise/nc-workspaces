package nc.ui.eh.stock.z06005;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.z06005.SbbillBVO;
import nc.vo.eh.stock.z06005.SbbillVO;
import nc.vo.eh.trade.z0600301.WeighbridgeconfigVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ���ܣ�˾����
 * ���ߣ�newyear
 * ���ڣ�2008-4-17 ����11:11:15
 */
public class ClientEventHandler extends AbstractEventHandler {
    private static ReadComm readComm ;
    private static WeighbridgeconfigVO wbVO ;
    UIRefPane ref=null;
    IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    ClientEnvironment ce = ClientEnvironment.getInstance();
	@SuppressWarnings("deprecation")
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
		//�Թرհ�ť�ļ�С�����ı����,��ֹ�����  add by wb at 2008-10-6 18:44:43
		ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			ButtonObject bolock = new ButtonObject("�ر�");
			bolock.setTag(String.valueOf(IEHButton.SBCLOSE));
			bolock.setCode("�ر�");
			bo.addChildButton(bolock);
			bo.removeChildButton(bo.getChildButtonGroup()[0]);
		}
	}
	
     /* 
     * ���ܣ��Զ��尴ť
     * @date 2008-4-14
     */
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.FIRSTREADDATE:    //��һ�ζ���  ���ظ�Ϊһ�ζ��� �ճ����س����Ǵ˰�ť
            	onBoFirstRead();
                break;
            case IEHButton.SBCLOSE: //�ر�˾��
            	onBoLockBill();
                break;
        }   
        super.onBoElse(intBtn);
    }
    
    
    /**
     * ���ܣ���ȡ˾����������Ϣ
     */
    private WeighbridgeconfigVO getWeighbridgeconfigVO() throws Exception{
        if(wbVO==null){
            StringBuffer br=new StringBuffer();
            br.append("isnull(dr,0)=0 and pk_corp = '"+_getCorp().getPk_corp()+"'");
            nc.ui.trade.bsdelegate.BusinessDelegator bd=new nc.ui.trade.bsdelegate.BusinessDelegator();
            WeighbridgeconfigVO[] queryVos = (WeighbridgeconfigVO[]) bd.queryByCondition(WeighbridgeconfigVO.class,br.toString());
            if(queryVos!=null && queryVos.length>1){
            	//������Ϊ������˾����ȡ��Ӧ����Ӧ����Ϣ���������
            	//����û������ ���нӿ�
            	//ȡ�����ļ�����Ϣ
//            	String choosekey=getChooseConfig();
//            	for(int i=0;i<queryVos.length;i++){
//            		String key=queryVos[i].getWbcode()==null?"":queryVos[i].getWbcode().toString();
//            		if(choosekey.equals(key)){
//            			wbVO=queryVos[i];
//            		}
//            	}
            }else if(queryVos!=null && queryVos.length==1){
            	wbVO=queryVos[0];
            }
        }
        return wbVO;
    }
    
    /**
     * ѡ�������ļ�
     * @return
     * @throws IOException 
     * @throws URISyntaxException 
     */
    public String getChooseConfig() throws IOException, URISyntaxException{
    	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/weightmanconfig.txt"))));
    	String line=null;
    	String[] lines=null;
    	while((line=in.readLine())!=null){
    		lines=line.split("/");
    	}
    	if(lines!=null){
    		return lines[1];
    	}else{
    		return "XXXXXX";
    	}
    }
    
  
    
     /*
     * �ܿڶ���
     * @return
     */
    @SuppressWarnings("static-access")
    private ReadComm getReadComm(){
        if(this.readComm==null){
            this.readComm=new ReadComm();
        }
        return this.readComm;
    }
 
    
   /**
   ���ܣ��ֹ��رյ�ǰ˾����
   ���ߣ�newyear
   ���ڣ�2008-6-4 ����02:16:58
   @throws BusinessException
    */
    protected void onBoLockBill() throws Exception{
    	SuperVO parentvo = (SuperVO)getBillUI().getVOFromUI().getParentVO();
    	String lock_flag=parentvo.getAttributeValue("close_flag")==null?"":parentvo.getAttributeValue("close_flag").toString();
    	String primaryKey = parentvo.getPrimaryKey();
    	if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("�õ����Ѿ��ر�!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ�����йرղ���?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            	IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                parentvo.setAttributeValue("close_flag", new UFBoolean(true));
                ivoPersistence.updateVO(parentvo);
            	getBillUI().showWarningMessage("�Ѿ��رճɹ�");
                onBoRefresh();
            }
            else{
            	return;
            }
        }
    }
    /*
     * ��ȡ˾����Ϣ
     * @����:ţұ
     * @ʱ��:2008-4-12
     */
    @SuppressWarnings("static-access")
	private void onBoFirstRead() throws Exception{
//    	getWeighbridgeconfigVO();
        if(!isCheckType())
            return;
        Object close_flag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject();
        if(close_flag!=null && ("true".equalsIgnoreCase(close_flag.toString()))){
            getBillUI().showErrorMessage("˾�����ѹر�!!");
            return;  
        }
        
      String weight = getReadComm().readWeight(getWeighbridgeconfigVO());
      if(weight!=null && (weight.equals("-2"))){
      		getBillUI().showErrorMessage("COM�ڱ�ռ��,�����Ƿ��������豸����ʹ��!");
      		return;
      }
      if(weight!=null && (weight.equals("-3"))){
        	getBillUI().showErrorMessage("˾������ʧ��,����!");
        	return;
        }
      if(weight!=null && (!weight.equals("-1"))){
      	Object FirstWeight = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject();
      	String pk = getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getPrimaryKey();
	    UFDateTime ts = ce.getServerTime();
      	if(pk!=null&&pk.length()>0&&FirstWeight!=null && (!FirstWeight.equals(""))){      // ��һ��ȡ���Ѿ���ֵ�ˣ�������ڶ���ȡ����
	        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_7", new UFDouble(weight)); 
	        getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_3", ts); 
	        getBillCardPanelWrapper().getBillCardPanel().setTailItem("endperson", _getOperator()); 
	    }
	    else{
	        this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6", new UFDouble(weight));
	        getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_2", ts); 	
	    }
        String[] formual=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sumsuttle").getEditFormulas();//��ȡ���ر༭��ʽ
        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(formual);                         //ִ�б༭��ʽ
      }else{
      	getBillUI().showErrorMessage("˾��ȡ��ʧ��,�����¶�ȡ!");
      }
    }


    /*
     * ˵����˾�����Զ�ȡ��
     * @����:ţұ
     * @ʱ��:2008-4-12
     */
    private boolean isCheckType() {
        //�ж�ȡ����ʽ�Ƿ�Ϊ�Զ�ȡ��
        String type = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").getValueObject()==null?
                "0":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").getValueObject().toString() ;
        if("0".equalsIgnoreCase(type)){
            getBillUI().showErrorMessage("ȡ����ʽ��ƥ��!");
            return false;
        }
        return true;
    }
    
    public void onBoAdd(ButtonObject bo) throws Exception {
        super.onBoAdd(bo);
        //�п���״̬
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").setEnabled(true);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(true);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(true);
        
        getBillUI().updateButtonUI();
    }
    
    
    
    @SuppressWarnings("unchecked")
	protected void onBoEdit() throws Exception {    	
    	String emptyload=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getValueObject()==null?""
   			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getValueObject().toString();
      	 String fullload=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getValueObject()==null?""
      			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getValueObject().toString();
         
    	ref=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").getComponent();
        Object closeFlag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject();
        if (closeFlag!=null && ("true".equals(closeFlag))){
            this.getBillUI().showErrorMessage("˾�����ѹر�!");
            return;
        }

        super.onBoEdit3();		//˾�����ǿ��Է��Ƶ���������޸ĵ� add by zqy  2010-11-17 11:30:19 
        
        //add by houcq begin
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").setEnabled(false);
     	  IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
          Object billno = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject();
          String oldsql = " select readflag from eh_sbbill  where pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and isnull(dr,0)=0 and billno='"+billno.toString()+"'";
      	  try {
  	           ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(oldsql.toString(), new MapListProcessor());
  	           if(all.size()>0)
  	           { 
  	 	             HashMap oldhm = (HashMap) all.get(0);
  	 	             String oldreadflag = oldhm.get("readflag")==null?"":oldhm.get("readflag").toString();
  	 	             if(oldreadflag.equals("Y"))
  	 	             {
  	 	            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("readflag").setEnabled(false);
  	 	            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").setEnabled(true);
  	 	             	  
  	 	             }
  	           }                
  	       } catch (BusinessException e1) 
  	       {
  	           e1.printStackTrace();
  	       } 
          //add by houcq end
        String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
        ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
        if(!sbtype.equals("0")){
        	
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
            if(sbtype.equals("1")){
            	sbtype = ICombobox.STR_SBBILLTYPE[1]; 
            }
            if(sbtype.equals("2")){
//            	String cppks=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject()==null?"":
//                	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject().toString();
//            	if(!cppks.equals("")){
//            		 String sqlupdate="update eh_icout set sb_flag='N' where pk_icout in "+cppks;
//                     PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//                     pubitf.updateSQL(sqlupdate);
//            	}
            	sbtype = ICombobox.STR_SBBILLTYPE[2]; 
            }
//            if(sbtype.equals("3")){//tuihuo 
//            	sbtype = ICombobox.STR_SBBILLTYPE[3]; 
//            }
        	changeRefModel(sbtype);
        }
        //������Զ���ȡ�������س��������ճ�������������༭
        String getType = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").getValueObject()==null?
                "0":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").getValueObject().toString() ;
        //getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").setEnabled(false);				//�������޸�˾������
        if(getType.equalsIgnoreCase("1")){
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").setEnabled(false);
        }else{
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").setEnabled(true);
        }
        getBillUI().updateButtonUI();
    	 if(!emptyload.equals("")&&!fullload.equals("")){
       	    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").setEnabled(false);
        }
    	 //add by houcq begin 2011-01-05���ζ���֮�󣬲��������¶���
    	 String firstNumber = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?
                 "":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString() ; 
       	String secondNumber = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?
                   "":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString() ;
       	if (!"".equals(secondNumber)&&!"".equals(firstNumber))
       	{
       	   getButtonManager().getButton(IEHButton.FIRSTREADDATE).setEnabled(false);
       	   getBillUI().updateButtonUI();
       	}    	
       	//add by houcq end 
    	/*
    	 * 1��˾�������ɼ�����뵥��˾���������Ӽ�������ǣ����У���
		 * 2�����м�������ǵ�˾�����еģ�˾�����͡�ѡ��˾�����͡��ͻ������ֶ����κ�����£�����ֹ�޸�
		 *	��Ŀǰ�ڵ�һ��ѡ��ʱ�ͻ��ֶβ����޸ģ��������ٵ��޸�ʱ���ͻ��ֶ�������޸��ˣ�Ӧ��Ҳ�����޸ģ���
    	 */
    	//add by houcq 2011-06-22 begin
       	if(sbtype.equals("ԭ��˾��"))
       	{
       		SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	String sql ="select ycy_flag from eh_sbbill where pk_sbbill= '"+hvo.getPk_sbbill()+"' and ycy_flag='Y'";
        	ArrayList cy = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        	if (cy!=null&& cy.size()>0)
        	{
        		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").setEnabled(false);
               	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(false);
               	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
        	}
        	getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
        	getBillUI().updateButtonUI();
       	}    	
    	if(sbtype.equals("��Ʒ˾��"))
    	{
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").setEnabled(false);
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(false);
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
    	}
    	//end
    }
	

    @SuppressWarnings("unchecked")
	protected void onBoDelete() throws Exception {
    	//add by houcq 2011-06-22 begin �Ѿ�����������ɾ��
    	 String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
         ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
    	if("1".equals(sbtype))
       	{
       		SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	String sql ="select ycy_flag from eh_sbbill where pk_sbbill= '"+hvo.getPk_sbbill()+"' and ycy_flag='Y'";
        	ArrayList cy = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        	if (cy!=null&& cy.size()>0)
        	{
        		getBillUI().showErrorMessage("�õ�������ԭ�ϼ������,��ֹɾ��!");
                return;
        	}
       	}  
    	//end
    	 String emptyload=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getValueObject()==null?""
    			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getValueObject().toString();
       	 String fullload=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getValueObject()==null?""
       			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getValueObject().toString();
         if(!emptyload.equals("")&&!fullload.equals("")){
        	 getBillUI().showErrorMessage("��˾�����ݲ�����ɾ����");
        	return;
         }
         
        Object closeFlag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject();
        if (closeFlag!=null && ("true".equals(closeFlag))){
            this.getBillUI().showErrorMessage("˾�����ѹر�!");
            return;
        }
        super.onBoDelete2();
    }
    
    public void changeRefModel(String objtype){
		if(objtype.equals(ICombobox.STR_SBBILLTYPE[0])){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(false);
		}
		if(objtype.equals(ICombobox.STR_SBBILLTYPE[1])){   //�ջ�֪ͨ��
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
			nc.ui.eh.businessref.ReceiptRefModel invRefModel = new nc.ui.eh.businessref.ReceiptRefModel();
    	    ref.setRefModel(invRefModel);
    	    ref.setMultiSelectedEnabled(false);
            ref.setTreeGridNodeMultiSelected(false);
    	    String fumual = "vsourcebilltype->getColValue(eh_stock_receipt, vbilltype, pk_receipt, getColValue(eh_stock_receipt_b, pk_receipt, pk_receipt_b, vbillno));";
    	    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEditFormula(new String[]{fumual} );//�ӱ�����
    	    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setAddSelectedListener(true);
    	    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
    	}
    	if(objtype.equals(ICombobox.STR_SBBILLTYPE[2])){        // ��Ʒ����
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
    		nc.ui.eh.businessref.IcoutRefModel IcoutRefModel = new nc.ui.eh.businessref.IcoutRefModel();
    		
    		ref.setRefModel(IcoutRefModel);
    		ref.setMultiSelectedEnabled(true);
            ref.setTreeGridNodeMultiSelected(true);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0255001);
    	}
    	
//    	if(objtype.equals(ICombobox.STR_SBBILLTYPE[3])){         // ԭ���˻�����
//    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
//    		nc.ui.eh.businessref.BackRefModel CkdRefModel = new nc.ui.eh.businessref.BackRefModel();
//    		ref.setRefModel(CkdRefModel);
//            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0502515);
//    	}
	}
    
    @SuppressWarnings({ "static-access", "unchecked" })
	public void onBoSave() throws Exception {
        //�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        //����ճ��������س��������ݶ��У����Զ��رո���˾����
        Object FirstWeight = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject();
        Object SecondWeight = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject();
        //����ճ����س���˾�����а�װ���ؼ���װ�������м���
        if(FirstWeight!=null && SecondWeight!=null && (!FirstWeight.equals("")) && (!SecondWeight.equals(""))){
        	 //�Կճ��Ե����غͰ�װ���صļ���
            UFDouble packnum = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("packnum").
            		getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("packnum").
            				getValueObject().toString());
            String pk_sbbill=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").
    		getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").
    				getValueObject().toString();
            if(!pk_sbbill.equals("")){
                	UFDouble suttle = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("suttle").
                    		getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("suttle").
                    				getValueObject().toString());
                	
                	UFDouble singleweight=suttle.div(packnum);
                	
                	UFDouble wrapperweight = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("wrapperweight").
                    		getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("wrapperweight").
                    				getValueObject().toString());    	
                	UFDouble bzkz=wrapperweight.multiply(packnum);
                	
                	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("singleweight", singleweight);//��������
                	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("bzkz", bzkz);//��װ������
                }
	            UFDouble FirstWeights = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?
						"0":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
				UFDouble SecondWeights = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?
									"0":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString());
				if (SecondWeights.compareTo(FirstWeights)<0){
				//˵����һ�αȵڶ�����
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("emptyload", SecondWeight);//�ճ�
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("fullload", FirstWeight);//�س�
				}else{
				//˵����һ�αȵڶ�����
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("fullload", SecondWeight);//�س�
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("emptyload", FirstWeight);//�ճ�
				}  
            }
        //ִ�й�ʽ  
		String[] formulas = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getEditFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(formulas);
        String[] fullload= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getEditFormulas();
        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(fullload);
        String [] packnum=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("packnum").getEditFormulas();
        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(packnum);
        String[] wrapperweight=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("wrapperweight").getEditFormulas();
        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(wrapperweight);
        //ֻ�е�һ�ζ��ص�ʱ�����ִ�й�ʽ �������ʱ������ ���澻�� by wm 2008��9��28��11:16:19
        String first=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?""
   			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString();
      	 String second=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?""
      			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString();
        if(!first.equals("") && second.equals("")){
        	String[] sformulas = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sumsuttle").getEditFormulas();
    		getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(sformulas);
        }
     
        String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
        ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
        /** modify by houcq 2011-05-12����ʱ�����޸Ķ���ʱ��
        //���޸��˺���
        UFDouble dyi=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?"0":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
        UFDouble dye=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?"0":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString());
        String deperson=getBillCardPanelWrapper().getBillCardPanel().getTailItem("endperson").getValueObject()==null?"":
        	getBillCardPanelWrapper().getBillCardPanel().getTailItem("endperson").getValueObject().toString();
        
        UFDateTime ts = ce.getServerTime();
        if(dyi.toDouble()>0 && dye.toDouble()>0 && deperson.equals("")){
        	 getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_3", ts); 
 	         getBillCardPanelWrapper().getBillCardPanel().setTailItem("endperson", _getOperator()); 
        }else if(dyi.toDouble()>0 && dye.toDouble()==0){
        	getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_2", ts); 
        }else if(dyi.toDouble()==0 && dye.toDouble()>0){
        	getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_2", ts); 
        }
        */
        
        if(sbtype.equals("2")){
        	//add by houcq 2010-10-18 begin
        	//�жϱ��岻��Ϊ��
   		 	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
   	        BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
   	        if (model != null) {
   	            int rowCount = model.getRowCount();
   	            if (rowCount < 1) {
   	                NCOptionPane.showMessageDialog(getBillUI(), "��Ʒδ���⣬�������棬���Ƚ��г�Ʒ����!");
   	                return;
   	            }
   	        }
   	        //add by houcq end
            String pk_sbbill=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").
      		 getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").
      		 getValueObject().toString();
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
              
        	if(!pk_sbbill.equals("")){
           	 String sqlht="select sum(rkmount) amount from eh_sc_cprkd a,eh_sc_cprkd_b b where a.pk_rkd=b.pk_rkd and a.pk_sbbill='"+pk_sbbill+"'";
           	 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sqlht, new MapListProcessor());
           	 for(int i=0;i<al.size();i++){
           		 HashMap hm=(HashMap) al.get(i);
           		 UFDouble rkmount=new UFDouble(hm.get("amount")==null?"":hm.get("amount").toString()); 
           		 getBillCardPanelWrapper().getBillCardPanel().setHeadItem("thamount", rkmount);
           	 }
            }	
        	//add by houcq begin 2011-03-02�ڳ��ⵥ�л�дsb_flag
        	String cppks=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject()==null?"":
            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject().toString();
        	String sqlupdate="update eh_icout set sb_flag='Y' where pk_icout in "+cppks;
            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            pubitf.updateSQL(sqlupdate);
        	//add by houcq end
        }
        if(sbtype.equals("1")){
        	//add by houcq 2010-10-18 begin
        	//�жϱ��岻��Ϊ��
   		 	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
   	        BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
   	        if (model != null) {
   	            int rowCount = model.getRowCount();
   	            if (rowCount < 1) {
   	                NCOptionPane.showMessageDialog(getBillUI(), "������Դ����Ϊ�գ���ѡ���ջ�֪ͨ��!");
   	                return;
   	            }
   	        }
        }
   	        //add by houcq end
        /**��ԭ���˻�˾����˾�����ľ����ڸ����˻�֪ͨ�����˻�����ʱ�������� add by wb 2008-10-31 10:10:26**/
        if(sbtype.equals("3")){
        	SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	SbbillBVO[] bvos = (SbbillBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        	if(bvos!=null&&bvos.length>0){
        		String[] pk_back_bs = new String[bvos.length]; 
        		for(int i=0;i<bvos.length;i++){
        			pk_back_bs[i] = bvos[i].getDef_6();				//ԭ���˻��ӱ�����
        		}
        		String pk_back_b = PubTools.combinArrayToString(pk_back_bs);
        		UFDouble sumsuttle = hvo.getSumsuttle()==null?new UFDouble(0):hvo.getSumsuttle();				//˾��������
        		String pk_sbbill = hvo.getPk_sbbill();
        		StringBuffer sql = new StringBuffer()
        		.append(" select a.sbamount,b.backamount")
        		.append(" from ")
        		.append(" (select sum(distinct isnull(a.sumsuttle,0)) sbamount")
        		.append(" from eh_sbbill a,eh_sbbill_b b")
        		.append(" where a.pk_sbbill = b.pk_sbbill")
        		.append(" and b.def_6 in "+pk_back_b+"")
        		.append(" and a.pk_sbbill <> '"+pk_sbbill+"'")
        		.append(" and isnull(a.dr,0)=0")
        		.append(" and isnull(b.dr,0)=0")
        		.append(" ) a,")
        		.append(" (select sum(isnull(b.amount,0))*1000 backamount")			//�˻����Ƕ���,ת��Ϊǧ��
        		.append(" from eh_stock_back a,eh_stock_back_b b")
        		.append(" where a.pk_back = b.pk_back")
        		.append(" and b.pk_back_b in "+pk_back_b+"")
        		.append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0")
        		.append(" ) b");
        		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
                ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                if(arr!=null&&arr.size()==1){
                	HashMap hm = (HashMap)arr.get(0);
                	UFDouble sbamount = new UFDouble(hm.get("sbamount")==null?"0":hm.get("sbamount").toString());
                	UFDouble backamount = new UFDouble(hm.get("backamount")==null?"0":hm.get("backamount").toString());
                	UFDouble subamount = new UFDouble(sbamount.add(sumsuttle).sub(backamount).toDouble(),0);
                	if(subamount.toDouble()>0){
                		getBillUI().showErrorMessage("ԭ���˻�˾�����ܾ��س���ʵ���˻�����,����"+subamount+"kg,���ܱ���,����!");
                		return;
                	}
                }
        	}
        }
        
        
        //Ҫ˾�����˻�֪ͨ��˾����ϣ��������Ȱ�װ���ػ�д���˻�֪ͨ����ȥ
        UFDouble def_6=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?"0":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
        UFDouble def_7=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?"0":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString());
        
        if(sbtype.equals("3") && def_6.toDouble()>0 && def_7.toDouble()>0){
        	SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	String cpah=hvo.getCarnumber()==null?"":hvo.getCarnumber().toString();
        	String cpih=hvo.getTranno()==null?"":hvo.getTranno().toString();
        	UFDouble fye=new UFDouble(0);
        	UFDouble taxmoney=new UFDouble(0);
        	UFDouble Suttle=hvo.getSuttle()==null?new UFDouble(0):hvo.getSuttle().div(1000); //���أ�ǧ��)/1000
        	UFDouble bzkz=hvo.getBzkz()==null?new UFDouble(0):hvo.getBzkz().div(1000);       //��װ������ǧ�ˣ�/1000
        	String pk_back_b=hvo.getVsourcebillrowid()==null?"":hvo.getVsourcebillrowid().toString();
        	String pk_back=hvo.getVsourcebillid()==null?"":hvo.getVsourcebillid().toString();
        	String updatesql="update eh_stock_back_b set packagweight="+bzkz+" ,spweight="+Suttle+" ,weight="+Suttle+" where pk_back_b='"+pk_back_b+"'";
        	String updatesql2="update eh_stock_back set isrk='Y' ,carnumber='"+cpah+"',tranno='"+cpih+"' where pk_back='"+pk_back+"'";
        	String selsql="select fye,taxmoney from eh_stock_back_b where pk_back_b='"+pk_back_b+"'";
        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(selsql, new MapListProcessor());
        	for(int i=0;i<al.size();i++){
        		HashMap hm=(HashMap) al.get(i);
        		fye=new UFDouble(hm.get("fye")==null?"0":hm.get("fye").toString());
        		taxmoney=new UFDouble(hm.get("taxmoney")==null?"0":hm.get("taxmoney").toString());
        	}
        	UFDouble fyeze=fye.multiply(Suttle);
        	UFDouble ze=taxmoney.add(fyeze);
        	String sqlupdate3="update eh_stock_back set fymoney="+fyeze+" where pk_back='"+pk_back+"'";
        	String sqlupdate4="update eh_stock_back set summoney="+ze+" where pk_back='"+pk_back+"'";
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	pubitf.updateSQL(updatesql);
        	pubitf.updateSQL(updatesql2);
        	pubitf.updateSQL(sqlupdate3);
        	pubitf.updateSQL(sqlupdate4);
        }
        
       
        
        /******************************** end *******************************************/
        
        super.onBoSave2_whitBillno(); 
       //add by houcq 2011-06-01 begin
        SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        new PubTools().recordTime(hvo.getVbilltype(), hvo.getPk_sbbill(), hvo.getPk_corp(), hvo.getCoperatorid());
        //add by houcq end
        setBoEnabled();
    }
    
    public void onButton(ButtonObject bo){
        onButton_N(bo,null);
    }

    protected void onBoPrint() throws Exception {
    	int i=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
//    	String pk_sbbill=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill")==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").toString();
//    	if(i==0){
//    		onBoLineAdd();
//    		super.onBoPrint();
//    	}else{
//    		super.onBoPrint();
//    	} 
    	//add by houcq begin 2010-09-26 ���´�ӡ����
    	
    	if(i==0)
    	{
    		onBoLineAdd();    		
    	}
    	int num=0;
    	String billno = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject().toString();
    	String old = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_10").getValueObject().toString();
    	if (null==old||"".equals(old))
    	{
    		old="-1";
    	}
    	else 
    	{    		
        	try {
            	num= Integer.valueOf(old).intValue()+1;
        	} catch (Exception e) {
        		getBillUI().showErrorMessage("def_10�ֶε�ֵ����ת��Ϊ����");
        		return;
        	}
		}
    	
    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName()); 
    	String sql = " update eh_sbbill set def_10='"+num+"' where pk_corp='"+ce.getCorporation().getPk_corp()+"' and billno='"+billno+"'";
    	pubitf.updateSQL(sql);
    	onBoRefresh();
    	super.onBoPrint();
    	
    }    
    private QueryConditionClient dlg = null;
    protected QueryConditionClient getQueryDLG()
    {        
        if(dlg == null){
            dlg = createQueryDLG();
        }
        return dlg;
    }
    
   protected void onBoQuery() throws Exception {
   	ClientEnvironment ce = ClientEnvironment.getInstance();
   	int type = getQueryDLG().showModal();
       if(type==1){
			String sqlWhere = getQueryDLG().getWhereSQL()==null?"":getQueryDLG().getWhereSQL();
			sqlWhere = sqlWhere.replaceFirst("������ͨ��", String.valueOf(IBillStatus.NOPASS));
			sqlWhere = sqlWhere.replaceFirst("����ͨ��", String.valueOf(IBillStatus.CHECKPASS));
			sqlWhere = sqlWhere.replaceFirst("������", String.valueOf(IBillStatus.CHECKGOING));
			sqlWhere = sqlWhere.replaceFirst("�ύ̬", String.valueOf(IBillStatus.COMMIT));
			sqlWhere = sqlWhere.replaceFirst("����", String.valueOf(IBillStatus.DELETE));
			sqlWhere = sqlWhere.replaceFirst("����", String.valueOf(IBillStatus.CX));
			sqlWhere = sqlWhere.replaceFirst("��ֹ", String.valueOf(IBillStatus.ENDED));
			sqlWhere = sqlWhere.replaceFirst("����״̬", String.valueOf(IBillStatus.FREEZE));
			sqlWhere = sqlWhere.replaceFirst("����̬", String.valueOf(IBillStatus.FREE));
			if(sqlWhere==null||sqlWhere.equals("")){
				sqlWhere =" 1=1 ";
			}
			if(addCondtion()!=null&&addCondtion().length()>0){
				sqlWhere = sqlWhere + " and "+addCondtion();
			}
			SuperVO[] queryVos = queryHeadVOs(sqlWhere+" and pk_corp = '"+ce.getCorporation().getPk_corp()+"'");
			
	       getBufferData().clear();
	       // �������ݵ�Buffer
	       addDataToBuffer(queryVos);
	
	       updateBuffer();
       }
       setBoEnabled();
	}
   
   //��ѯģ���м��ϲ�ѯ����Ϊ�Ƶ�һ����֮�ڵĵ���
   protected QueryConditionClient createQueryDLG() {
   	QueryConditionClient dlg = new QueryConditionClient();
   	String billtype = getUIController().getBillType();           // ��������
       String sql = "select nodecode from bd_billtype where pk_billtypecode = '"+billtype+"' and isnull(dr,0)=0"; //ȡ�ڵ��
       IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
       try {
			String nodecode = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:
							iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
            String lastdate = _getDate().getDateBefore(1).toString();
	        dlg.setTempletID(_getCorp().getPk_corp(), nodecode, null, null); 
	        dlg.setDefaultValue("dmakedate",lastdate,null);
	        addQueryDefaultValue();
	        dlg.setNormalShow(false);
       }catch (BusinessException e) {
			e.printStackTrace();
		}
       return dlg;
   }
   
   /**��ѯʱ�������� " vbilltype = "" and ..."
    * @return
    */
   public String addCondtion(){
   	return null;
   }
   
   public void addQueryDefaultValue(){

   }
  
    
    //���пͻ�
    @SuppressWarnings("unchecked")
    public static HashMap getAllCubasdoc(){
        HashMap hmcubasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql = "select pk_cubasdoc,custcode from bd_cubasdoc where isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null&&arr.size()>0){
                String pk_cubasdoc = null;
                String custcode = null;
                for(int i=0; i<arr.size(); i++){
                    HashMap hmarr = (HashMap)arr.get(i);
                    pk_cubasdoc = hmarr.get("pk_cubasdoc")==null?"":hmarr.get("pk_cubasdoc").toString();
                    custcode = hmarr.get("custcode")==null?"":hmarr.get("custcode").toString();
                    hmcubasdoc.put(custcode, pk_cubasdoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hmcubasdoc;
    }
    
    //��������
    @SuppressWarnings("unchecked")
	public static HashMap getAllInvbasdoc(){
        HashMap hminvbasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql ="select pk_invbasdoc,invcode from eh_invbasdoc where isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                String pk_invbasdoc = null;
                String invcode = null;
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                    invcode = hm.get("invcode")==null?"":hm.get("invcode").toString();
                    hminvbasdoc.put(invcode, pk_invbasdoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hminvbasdoc;
    }
    
    
//  ���ð�ť�Ŀ���״̬
    protected void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
        Integer vbillstatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
        if (vbillstatus==null){
        }
        else{   
            switch (vbillstatus.intValue()){
                //free
                case IBillStatus.FREE:
                        getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
                        getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
                        getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[0].setEnabled(true);
                        break;
                //commit
                case IBillStatus.COMMIT:
                    getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[0].setEnabled(false);
                    getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[1].setEnabled(true);
                    break;
                //CHECKGOING
                case IBillStatus.CHECKGOING:
                    getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                    break;
                //CHECKPASS
                case IBillStatus.CHECKPASS:
                	getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[0].setEnabled(false);
                	getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[1].setEnabled(false);
                //NOPASS
                case IBillStatus.NOPASS:
                        getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                        getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                        break;
                
            }
        }
        // ���йرհ�ťʱ�Թرհ�ť�Ŀ��� add by wb at 2008-6-20 14:30:23
            String[] keys = aggvo.getParentVO().getAttributeNames();
            if(keys!=null && keys.length>0){
                for(int i=0;i<keys.length;i++){
                    if(keys[i].endsWith("close_flag")){ 
                    	String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject()==null?"N":
                            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject().toString();
                        if(lock_flag.equals("false")){
                        	if(getButtonManager().getButton(IEHButton.SBCLOSE)!=null){
                        		getButtonManager().getButton(IEHButton.SBCLOSE).setEnabled(true);
                        	}
                        	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
//                        		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(true);	
                        	}
                        }else{
                        	if(getButtonManager().getButton(IEHButton.SBCLOSE)!=null){
                        		getButtonManager().getButton(IEHButton.SBCLOSE).setEnabled(false);
                        	}
                        	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                        		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(false);	//��ҵ������µ�һ����ť��Ϊ���ɲ���
                        	}
                        	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                            getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                        }
                        break;
                    }
                    
                }
            }
        super.setBoEnabled();
        getBillUI().updateButtonUI();
    }
    
    @Override
    public void onBoAudit() throws Exception {
    	super.onBoAudit();
    	setBoEnabled();
    }

	/**
	 * ���ܣ����ύʱ�Գ��ⵥ��SB_FLAG��дY���´�˾��ʱ���ܲ��ճ���
	 * ʱ��:2009-12-16
	 * ���ߣ���־Զ
	 */
	public void onBoCommit() throws Exception {
		/** add by houcq ע�� 2011-03-02����д��Ǹ��ڱ���ʱ����
		//ȡ��˾������
		String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
        ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
		//ȡ��˾�������г��ⵥ��
        String cppks=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject()==null?"":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject().toString();
        //ȡ����ѡ��ĳ��ⵥ����PK�ͱ�ͷSB_FLAG
        StringBuffer str = new StringBuffer()
		.append(" SELECT DISTINCT b.pk_icout_b,c.sb_flag FROM eh_icout_b b,eh_icout c ")
		.append(" WHERE b.pk_icout = c.pk_icout ")
		.append(" AND c.pk_icout in "+cppks+" AND NVL(c.dr,0)=0 ");
        //�г��ⵥ�ĳ�Ʒ˾��
        if(sbtype.equals("2")&&!cppks.equals("")){
        	//�����ѡ���ⵥ�����б���PK��SB_FLAG
        	HashMap pkHm = new HashMap();
        	IUAPQueryBS iq = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	ArrayList arr = (ArrayList) iq.executeQuery(str.toString(), new MapListProcessor());
        	if(arr!=null&&arr.size()>0){
        		for(int i=0; i<arr.size();i++){
        			HashMap hm = (HashMap) arr.get(i);
        			String pk_icout = hm.get("pk_icout_b")==null?"":hm.get("pk_icout_b").toString();
        			String sb_flag = hm.get("sb_flag")==null?"N":hm.get("sb_flag").toString();
        			pkHm.put(pk_icout,sb_flag);
        		}
        		int rows = this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();//��������
        		StringBuffer mess = new StringBuffer().append("��");
        		for(int i=0;i<rows;i++){
        			//def_6������ǳ��ⵥ���ӱ�PK
        			String pk_icout = this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_6").toString();
        			if(pkHm.containsKey(pk_icout)){
        				String sb_flag = pkHm.get(pk_icout).toString();
            			if("Y".equals(sb_flag)){
            				mess.append(" "+(i+1)+"");
            			}
        			}
        		}
        		if(!mess.toString().equals("��")){
        			mess.append(" ���ⵥ�ѱ�˾����������ѡ��˾�����ͣ�");
        			this.getBillUI().showErrorMessage(mess.toString());
        			return;
        		}
        	}
        	//����ѡ�ĳ��ⵥ��дSB_FLAG���Y
        	 String sqlupdate="update eh_icout set sb_flag='Y' where pk_icout in "+cppks;
             PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
             pubitf.updateSQL(sqlupdate);
        }
        */
		//super.onBoCommit();
		super.onBoCommit2();//modify by houcq 2011-01-05
	}
    
//	protected void onBoLineDel() throws Exception {
//    	//add by houcq begin 2011-06-20ɾ��ʱȡ�����ⵥ˾�����sb_flag
//      String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
//      ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
//      if ("2".equals(sbtype))
//      {  
//      	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//      	int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRowCount();    
//  		for (int i = 0; i < row; i++) {					
//  			String vsourcebillrowno = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_2")==null?"":
//  		                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_2").toString();  		            
//  			String Sql = " update eh_icout set sb_flag='N' where pk_corp='"+this._getCorp().getPk_corp()+"' and def_2='"+vsourcebillrowno+"'";
//  		            pubitf.updateSQL(Sql);
//  		}
//      }
//   }
}
