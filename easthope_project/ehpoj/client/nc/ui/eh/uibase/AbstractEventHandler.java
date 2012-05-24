/**
 * Module BgtEventHandler.java
 * @author chen cp (tom)
 * @date 2007-7-2
 */
package nc.ui.eh.uibase;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.eh55.FileManagerDialog;
import nc.ui.eh.eh55.FileManagerUtil;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.pub.Toolkits;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵��:�����¼�������,һЩ���÷������ڴ���д,�����ͽ���Ӧ�̳д���
 * @author LiuYuan
 * 2007-9-27 ����02:44:49
 */
public class AbstractEventHandler extends ManageEventHandler {


    /**
     * 
     * @param billUI
     * @param control
     */
    public AbstractEventHandler(nc.ui.trade.manage.BillManageUI billUI,
            nc.ui.trade.controller.IControllerBase control) {
        super(billUI, control);
        //���´�����houcqע�ͣ�ȡ����ֹ���ݹ���2010-11-01
        //�Թرհ�ť�ļ�С�����ı����,��ֹ�����  add by wb at 2008-10-6 18:44:43
		ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			ButtonObject bolock = new ButtonObject("��ֹ����");
			bolock.setTag(String.valueOf(IEHButton.LOCKBILL));
			bolock.setCode("��ֹ����");
			bo.addChildButton(bolock);
		}
    }
    
    /* ���� Javadoc��
     * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
     */
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.DOCMANAGE:    //�ĵ�����
                onBoDocManage();
                break;
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
    
    /**
     * ���ݵĹر� add by wb 
     * @throws Exception
     */
    protected void onBoLockBill() throws Exception{
    	SuperVO parentvo = (SuperVO)getBillUI().getVOFromUI().getParentVO();
    	String lock_flag = parentvo.getAttributeValue("lock_flag")==null?"N":parentvo.getAttributeValue("lock_flag").toString();
    	String primaryKey = parentvo.getPrimaryKey();
    	if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("�õ����Ѿ���ֹ!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ��������ֹ���ݲ���?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            	IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                parentvo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(parentvo);
            	getBillUI().showWarningMessage("�����ɹ�");
                onBoRefresh();
                setBoEnabled();
            }
            else{
            	return;
            }
        }
    }
    
    // ����ҳ����� add by wb at 2008-6-20 14:31:03
    @SuppressWarnings("static-access")
	private void onBoBrows(int intBtn) throws java.lang.Exception {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	UFDateTime begints = ce.getServerTime();
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
		int ts = ce.getServerTime().compareTo(begints);
		getBillUI().showHintMessage("ת����"+(getBufferData().getCurrentRow()+1)+"ҳ���,��ʱ"+ts+"(ms)");
        setBoEnabled();
	}

    /**
     * ����:����ǰ�ж�
     * @throws Exception
     * @author LiuYuan
     * 2007-9-27 ����02:43:49
     */
    public void onBoSave() throws Exception {
    	onBoSave_withBillno();
    	setBoEnabled();
    }
    
    /***
     * �ڱ���ʱ��ȡ���ݺ� 
     * add by wb 2008-12-22 14:28:08
     * @throws Exception
     */
    public void onBoSave_withBillno() throws Exception {
       getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
       BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
       if (model != null) {
           int rowCount = model.getRowCount();
           if (rowCount < 1) {
//        	   getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);//add by houcq 2011-01-17
        	   throw new Exception("������Ϊ�գ����ܱ��棡");//add by zhuxb 2011-01-19
//               NCOptionPane.showMessageDialog(getBillUI(), "�����в���Ϊ��!");
//               return;
           }
       }
       String pk_corp = _getCorp().getPrimaryKey();
       BillItem billnoitem = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno");
       if (billnoitem != null){
	       Object billnoobj = getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getAttributeValue("billno");
	       if(billnoobj==null||billnoobj.toString().length()==0){
	    	   String billNo = BillcodeRuleBO_Client.getBillCode(getUIController().getBillType(), pk_corp,
	    			   	null, null);
	    	   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("billno", billNo);
	       }
       }
       super.onBoSave();
   }
    
    public void onBoSave2() throws Exception{
        super.onBoSave();
    }
    
    public void onBoSave2_whitBillno() throws Exception{
    	 String pk_corp = _getCorp().getPrimaryKey();
         BillItem billnoitem = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno");
         if (billnoitem != null){
  	       Object billnoobj = getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getAttributeValue("billno");
  	       if(billnoobj==null||billnoobj.toString().length()==0){
//  	    	   String billNo = BillcodeRuleBO_Client.getBillCode(getUIController().getBillType(), pk_corp,null, null);
  	    	   String billNo = HYPubBO_Client.getBillNo(getUIController().getBillType(), pk_corp,null, null);
  	    	   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("billno", billNo);
  	       }
         }
         super.onBoSave();
    }

    
    /**
     * ����:�ύǰ�ж� 
     * @throws Exception
     * @author LiuYuan
     * 2007-9-27 ����02:42:41
     */
    public void onBoCommit() throws Exception {
        int currentRow = this.getBillUI().getBufferData().getCurrentRow();
        if (currentRow == -1) {
            getBillUI().showErrorMessage("û�е���ѡ��,�޷��ύ!");
            return;
        }
        //add by houcq  begin 2011-01-05���е���ֻ���ɱ����ύ
        String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("�������ύ�������룡");
            return;
        }
        //add by houcq end
        super.onBoCommit();
        setBoEnabled();
    }
    //onBoCommit2������houcq����2011-01-05��˾�����ύʱ����
    public void onBoCommit2() throws Exception {
        int currentRow = this.getBillUI().getBufferData().getCurrentRow();
        if (currentRow == -1) {
            getBillUI().showErrorMessage("û�е���ѡ��,�޷��ύ!");
            return;
        }        
        super.onBoCommit();
        setBoEnabled();
    }
    /**
     * ����: �������ε���ʱ,����setDefaultData()
     * @param bo
     * @author LiuYuan
     * 2007-10-12 ����10:07:29
     */
    public void onButton(ButtonObject bo){
        onButton_N(bo,null);
    }
    
    public void onButton_N(ButtonObject bo,BillModel model){
    	try {
		   
            ButtonObject parent = bo.getParent();
            if(parent==null){
            	super.onButton(bo);
            }
            if(!Toolkits.isEmpty(parent)){
            	/**
            	   String code=bo.getParent().getCode();			//�˵õ��Ľ������ "����" ������ĵ����ж���������� setDefaultData(),ʹ�õ��ݺų��ּ�1����
            	**/
            	String parentcode = bo.getParent().getCode();			
            	String code = bo.getCode();      // �õ�����İ�ť������ ������������ edit by wb at 2008-10-6 13:03:08 
                if(parentcode!=null&&parentcode.equals("����")){
                	UFDate nowdate = _getDate();
        	    	PeriodVO[] periVOs =  (PeriodVO[])getBusiDelegator().queryByCondition(PeriodVO.class, " nyear = "+nowdate.getYear()+" and nmonth = "+nowdate.getMonth()+" and pk_corp = '"+_getCorp().getPk_corp()+"'");
        	    	if(periVOs==null||periVOs.length==0){
        	    		getBillUI().showErrorMessage("�����ڼ䵵��û��ά��,��ά���ڼ䵵��!");
        	    		return;
        	    	}
        		    PeriodVO perVO = periVOs[0];
        	    	boolean jz_flag =  perVO.getJz_flag()==null?false:perVO.getJz_flag().booleanValue();
        		    if(jz_flag){
        		    	getBillUI().showErrorMessage(perVO.getNyear()+"��"+perVO.getNmonth()+"�µĻ���ڼ��ѽ��ʣ�����������!");
        		        return;
        		    }
        		}
                super.onButton(bo);
                if(isAdding()){ 
                    if(parentcode.equals("����")&&!"���Ƶ���".equalsIgnoreCase(code)){
                		getBillUI().setDefaultData();
                		if(!Toolkits.isEmpty(model))
                			UIUtil.execBodyFormulas(model);
                	}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    
    @Override
    public void onBoAudit() throws Exception {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	UFDate nowdate = ce.getDate();			//��ǰ��¼���ڲ���С���Ƶ�����
    	String pk = getBillUI().getChangedVOFromUI().getParentVO().getPrimaryKey();
    	if(pk==null||pk.length()==0){
    		getBillUI().showErrorMessage("��ѡ��һ�ŵ���!");
    		return;
    	}
    	UFDate dmkedate = new UFDate(getBillUI().getChangedVOFromUI().getParentVO().getAttributeValue("dmakedate")==null?
    			nowdate.toString():getBillUI().getChangedVOFromUI().getParentVO().getAttributeValue("dmakedate").toString());	//�Ƶ�����
    	if(nowdate.before(dmkedate)){
    		getBillUI().showErrorMessage("�������ڲ��������Ƶ�����!");
    		return;
    	}
    	super.onBoAudit();
//    	new PubTools().sendMessageByAggVO(getBillUI().getChangedVOFromUI(), _getCorp().getPk_corp(),getUIController().getBillType());
    	setBoEnabled();
    }   
 
    /**
     * ����:��ʼ��UFDouble������
     * * @author honghai
     *  2007-11-7 ����11:25:02
     */
    protected void onBoLineAdd() throws Exception
    {
        super.onBoLineAdd();
//        initUFDouble();//��ʼ��UFDouble������
    }
    /**
     * ����: ����ʱ�ֶ�ΪUFDouble�͵ĳ�ʼΪ0
     * @author honghai
     * 2007-11-7 ����11:25:02
     */
    private void initUFDouble(){
        BillItem [] items=getBillCardPanelWrapper().getBillCardPanel().getBodyItems();//�������
        int curruntrow=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();//���ص�ǰ��
        for(int j=0;j<items.length;j++){
            if(items[j].getDataType()==2){
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(new UFDouble(0),curruntrow,items[j].getKey());
            }
        }
    }
    
    /*
     * ���ܣ��ĵ�������
     */
    protected void onBoDocManage() throws Exception{
        // �ĵ�����
        String billid = getBillUI().getVOFromUI().getParentVO().getPrimaryKey();
        String billno = getBillUI().getVOFromUI().getParentVO().getAttributeValue("vbillcode")==null?""
                :getBillUI().getVOFromUI().getParentVO().getAttributeValue("vbillcode").toString();
        if (billid ==null || billid.equals("")){
            this.getBillUI().showWarningMessage("��Ŀ�����Ժ�ſ���ʹ���ĵ����ܣ�");
            return;
        }
        FileManagerDialog dlg = new FileManagerDialog(this.getBillUI(),
                new String[] { billid }, new String[] { billno });
        dlg.setTitle( "�����ĵ�����");
        dlg.setShowStyle(FileManagerDialog.SHOW_FILE_LOCAL);
        if (dlg.showModal() == nc.ui.pub.beans.UIDialog.ID_CANCEL)
            return;
        String dir = dlg.getDir();
        String fileName = dlg.getSelectedFileName();
        dlg.dispose();
        FileManagerUtil.showFileLocal(dir, fileName);
    
    }
    
    
    //���ð�ť�Ŀ���״̬
    protected void setBoEnabled() throws Exception {
    	 super.onBoRefresh();
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
                         break;
                 //commit
                 case IBillStatus.COMMIT:
                     getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                     getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                     break;
                 //CHECKGOING
                 case IBillStatus.CHECKGOING:
                     getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                     getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                     break;
                 //CHECKPASS
                 case IBillStatus.CHECKPASS:
                 	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                     getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                     break;
                 //NOPASS
                 case IBillStatus.NOPASS:
                         getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                         getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                         break;
                 
             }
         }
       
             //��һҳ ��һҳ�İ�ť״̬  add by wb at 2008-6-20 14:30:23
             if(getButtonManager().getButton(IEHButton.Prev)!=null){
 	            if(!getBufferData().hasPrev()){
 	    			getButtonManager().getButton(IEHButton.Prev).setEnabled(false);
 	    		}
 	            else{
 	            	getButtonManager().getButton(IEHButton.Prev).setEnabled(true);
 	            }
 	    		if(!getBufferData().hasNext()){
 	    			getButtonManager().getButton(IEHButton.Next).setEnabled(false);
 	    		}
 	    		else{
 	            	getButtonManager().getButton(IEHButton.Next).setEnabled(true);
 	            }
             }
             //���´�����houcq 2010-11-01 ע�ͣ�ȡ��������ֹ��ť�Ĵ���
             // ���йرհ�ťʱ�Թرհ�ť�Ŀ��� add by wb at 2008-6-20 14:30:23
             String[] keys = aggvo.getParentVO().getAttributeNames();
             if(keys!=null && keys.length>0){
                 for(int i=0;i<keys.length;i++){
                     if(keys[i].endsWith("lock_flag")){ 
                     	String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
                             getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
                         if(lock_flag.equals("false")){
                         	if(getButtonManager().getButton(IEHButton.LOCKBILL)!=null){
                         		getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(true);
                         	}
                         	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                         		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(true);	
                         	}
                         }else{
                         	if(getButtonManager().getButton(IEHButton.LOCKBILL)!=null){
                         		getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(false);
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
         getBillUI().updateButtonUI();
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
    }
    
    protected void onBoEdit2() throws Exception {
    	String coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){		//�����˲������޸ı��˵��ݵĿ��� add by zqy 2010��11��16��15:04:01
            getBillUI().showErrorMessage("�������޸��������룡");
            return;
        }
        super.onBoEdit();
    }
    
    /**
     * �˴����ӵ��޸�ֻΪ˾��������(onBoEdit2()�����ŵ������ö�Σ����������˷��� add by zqy 2010��11��17��9:10:44)
     * @throws Exception
     */
    protected void onBoEdit3() throws Exception {
    
        super.onBoEdit();
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
    
    /**
     * �˴����ӵ�ɾ��ֻΪ˾��������(onBoEdit2()�����ŵ������ö�Σ����������˷��� add by zqy 2010��11��17��9:10:44)
     * @throws Exception
     */
    protected void onBoDelete2() throws Exception {
        
        super.onBoDelete();
    }
    
    /* ���� Javadoc��
     * @see nc.ui.trade.manage.ManageEventHandler#onBoReturn()
     */
    protected void onBoReturn() throws Exception {
        // TODO �Զ����ɷ������
        super.onBoReturn();
    }
    protected void onBoReturn2() throws Exception {
        // TODO �Զ����ɷ������
        super.onBoReturn();
    }
    
    /* ���� Javadoc��
     * @see nc.ui.trade.manage.ManageEventHandler#onBoCard()
     */
    protected void onBoCard() throws Exception {
        // TODO �Զ����ɷ������
        super.onBoCard();
        nc.vo.pub.bill.BillRendererVO voCell = new nc.vo.pub.bill.BillRendererVO();
		voCell.setShowThMark(true);
		voCell.setShowZeroLikeNull(false);//add by houcq 2011-02-14
		voCell.setShowRed(true);//add by houcq 2011-02-12
		getBillCardPanelWrapper().getBillCardPanel().setBodyShowFlags(voCell);
		getBillCardPanelWrapper().getBillCardPanel().setShowThMark(true);
        setBoEnabled();
    }
    
    protected void onBoCard2() throws Exception {
        super.onBoCard();
        setBoEnabled();
    }
    /* ���� Javadoc��
     * @see nc.ui.trade.manage.ManageEventHandler#onBoRefresh()
     */
    protected void onBoRefresh() throws Exception {
        // TODO �Զ����ɷ������
        super.onBoRefresh();
//        AggregatedValueObject aggvo = getBillCardPanelWrapper().getBillVOFromUI();
//        new PubTools().sendMessageByAggVO(aggvo, _getCorp().getPk_corp(),getUIController().getBillType());
        setBoEnabled();
    }
    
    protected void onBoRefresh2() throws Exception {
        super.onBoRefresh();
        setBoEnabled();
    }
    
    @Override
    public void onBoAdd(ButtonObject bo) throws Exception {
    	UFDate nowdate = _getDate();
    	PeriodVO[] periVOs = (PeriodVO[])getBusiDelegator().queryByCondition(PeriodVO.class, " nyear = "+nowdate.getYear()+" and nmonth = "+nowdate.getMonth()+" and pk_corp = '"+_getCorp().getPk_corp()+"'");
    	if(periVOs==null||periVOs.length==0){
    		getBillUI().showErrorMessage("�����ڼ䵵��û��ά��,��ά���ڼ䵵��!");
    		return;
    	}
	    PeriodVO perVO = periVOs[0];
    	boolean jz_flag =  perVO.getJz_flag()==null?false:perVO.getJz_flag().booleanValue();
	    if(jz_flag){
	    	getBillUI().showErrorMessage(perVO.getNyear()+"��"+perVO.getNmonth()+"�µĻ���ڼ��ѽ��ʣ�����������!");
	        return;
	    }
        super.onBoAdd(bo);
    }
    
    @Override
    protected void onBoCancel() throws Exception {
        // TODO Auto-generated method stub
        super.onBoCancel();
        setBoEnabled();
    }
    
    protected void checkflow(String primarykey) throws Exception{
        ApproveDialog dialog=new ApproveDialog();
        dialog.showModal();
        
        try {
            String cuserid=dialog.cuserid;
            IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            StringBuffer sql=new StringBuffer("")
            .append(" select count(1) count from pub_workflownote where billid ='"+primarykey+"' and checkman='"+cuserid+"' and ")
            .append(" isnull(dealdate,'N')='N' and isnull(dr,0)=0");
            HashMap hm=(HashMap)iUAPQueryBS.executeQuery(sql.toString(),
                    new MapProcessor());
            int count=Integer.parseInt(hm.get("count").toString());
            if(count==0){
                getBillUI().showErrorMessage("����Ա�������õ���Ȩ��");
                return;
            }
            AggregatedValueObject billVO=getBillCardPanelWrapper().getBillVOFromUI();
            billVO.getParentVO().setAttributeValue("vapproveid", cuserid);
            billVO.getParentVO().setAttributeValue("dapprovedate",_getDate() );
            
            PfUtilClient.processActionFlow(
                 getBillUI(),
                 IPFACTION.APPROVE,
                 getUIController().getBillType(),
                 _getDate().toString(),
                 billVO,
                 "");
            
            getBillUI().showHintMessage("��������ɣ�");
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        onBoRefresh();
     }
    
    
    /**
     * ����: ��ɾ��ǰ�ж�,�������账��ɾ����������ظ��Ǳ�����
     * @return
     * @throws Exception
     * @return:int 0-δɾ��,1-��ɾ��
     * @author LiuYuan
     * 2007-10-7 ����09:08:35
     */
    public int onBoDeleteN() throws Exception{
    	String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("������ɾ���������룡");
            return 0;
        }
    	if (getBillUI().showYesNoMessage("�Ƿ�ȷ��ɾ���õ���?") != UIDialog.ID_YES) {
            return 0;
        }
        return 1;
    }
    
    /**���������ε��ݻ�д������ʱȷ�����ɾ��,��Ҫͨ��onboDeleteN()�ж��ǲ���ȷ��ɾ��,����1���������е��ô˷���
     * @author ���� �������Ӽ� nc.ui.eh.iso.z0501505.ClientEventHandler.java
     * 2008-6-12 10:25:10
     * @throws Exception
     */
    protected void onBoTrueDelete() throws Exception {
    	AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		getBusinessAction().delete(modelVo, getUIController().getBillType(),
				getBillUI()._getDate().toString(), getBillUI().getUserObject());
		if (PfUtilClient.isSuccess()) {
			// �����������
			getBillUI().removeListHeadData(getBufferData().getCurrentRow());
			if (getUIController() instanceof ISingleController) {
				ISingleController sctl = (ISingleController) getUIController();
				if (!sctl.isSingleDetail())
					getBufferData().removeCurrentRow();
			} else {
				getBufferData().removeCurrentRow();
			}

		}
		if (getBufferData().getVOBufferSize() == 0)
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		else
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
    }
    
    /*
     * ���ܣ���ѯ�Ի�����ʾ
     */
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
			//pk_corp����
			StringBuffer corp = new StringBuffer()
			.append(" and pk_corp = '"+ce.getCorporation().getPk_corp()+"' ");
			
			AggregatedValueObject aggvo=getBillCardPanelWrapper().getBillVOFromUI();
			String[] keys = aggvo.getParentVO().getAttributeNames();
			//����ѯ����BILLNO�ֶε����ݰ�������2009-12-08
			if(keys!=null && keys.length>0){
                for(int i=0;i<keys.length;i++){
                    if(keys[i].equals("billno")){ 
                    	corp.append(" ORDER BY billno ");
                    }
                }
			}
			
			SuperVO[] queryVos = queryHeadVOs(sqlWhere+ corp.toString());
			
			
	       getBufferData().clear();
	       // �������ݵ�Buffer
	       addDataToBuffer(queryVos);
	
	       updateBuffer();
        }
        setBoEnabled();//add by houcq 2010-11-24�����ѯ����ʱ�ύ���ݿ����޸ģ�ɾ��
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
	        dlg.setTempletID(_getCorp().getPk_corp(), nodecode, null, null); 
	        String lastdate = _getDate().toString().substring(0, 7)+"-01";			//�ĳɵ��µ�1��
	        dlg.setDefaultValue("dmakedate",lastdate,null);
	        addQueryDefaultValue();
	        dlg.setNormalShow(false);
        }catch (BusinessException e) {
			e.printStackTrace();
		}
        return dlg;
    }
    
    protected void onBoQuery2() throws Exception {
    	System.out.println("#####");
    	super.onBoQuery();
    }
    
    /**��ѯʱ�������� " vbilltype = "" and ..."
     * @return
     */
    public String addCondtion(){
    	return null;
    }
    
    public void addQueryDefaultValue(){

    }
   
    @Override
    public void onBoApproveInfo() throws Exception {
    		String billId = (String) getBufferData().getCurrentVO().getParentVO().getPrimaryKey();
    		onAppMind(getBillUI(),getUIController().getBillType(),billId);
    }

    //  �������
	public static void onAppMind(AbstractBillUI ui,String billtype,Object billid) {
		try {
			if (billid == null) {
				ui.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("60310401", "UPP60310401-000029")/* @res "��ѡ��һ������!" */);
			} else {
				nc.ui.pub.workflownote.FlowStateDlg dlg = new nc.ui.pub.workflownote.FlowStateDlg(ui, billtype, billid.toString());
				dlg.show();
				dlg.closeCancel();
			}
		} catch (Exception e) {
			ui.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("60310401", "UPP60310401-000030")/* @res "��ѯ�������ʧ��!" */);
			ui.showErrorMessage(e.getMessage());
		}
	}
	
	/***
	 * �ж�eh_perioddiscount_h���Ƿ���ڵ�ǰ�·ݵ��ۿۣ�
	 * ʱ�䣺2009-12-9 19:30:13
	 * ��־Զ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean getDiscount(){
		boolean flag = false;
		StringBuffer str = new StringBuffer()
		.append(" SELECT a.pk_perioddiscount_h FROM eh_perioddiscount_h a WHERE a.vyear = '"+_getDate().getYear()+"' AND a.vmonth = '"+_getDate().getMonth()+"' and a.pk_corp = '"+_getCorp().getPrimaryKey()+"' AND NVL(a.dr,0)=0 ");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(str.toString(), new MapListProcessor());
			if(arr!=null && arr.size()>0){
	        	flag = true;
	        }
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/***
	 * �ж�eh_perioddiscount_h���Ƿ���ڸù�˾���ۿۣ�
	 * ʱ�䣺2009-12-15 19:30:13
	 * ��־Զ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean getEveDiscount(){
		boolean flag = false;
		StringBuffer str = new StringBuffer()
		.append(" SELECT a.pk_perioddiscount_h FROM eh_perioddiscount_h a WHERE a.pk_corp = '"+_getCorp().getPrimaryKey()+"' AND NVL(a.dr,0)=0 ");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(str.toString(), new MapListProcessor());
			if(arr!=null && arr.size()>0){
	        	flag = true;
	        }
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
}
