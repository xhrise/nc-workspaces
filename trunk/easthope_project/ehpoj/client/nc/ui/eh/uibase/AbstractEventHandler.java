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
 * 说明:基础事件处理类,一些常用方法已在此重写,管理型界面应继承此类
 * @author LiuYuan
 * 2007-9-27 下午02:44:49
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
        //以下代码由houcq注释，取消终止单据功能2010-11-01
        //对关闭按钮的减小操作的便捷性,防止误操作  add by wb at 2008-10-6 18:44:43
		ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			ButtonObject bolock = new ButtonObject("终止单据");
			bolock.setTag(String.valueOf(IEHButton.LOCKBILL));
			bolock.setCode("终止单据");
			bo.addChildButton(bolock);
		}
    }
    
    /* （非 Javadoc）
     * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
     */
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.DOCMANAGE:    //文档管理
                onBoDocManage();
                break;
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
    
    /**
     * 单据的关闭 add by wb 
     * @throws Exception
     */
    protected void onBoLockBill() throws Exception{
    	SuperVO parentvo = (SuperVO)getBillUI().getVOFromUI().getParentVO();
    	String lock_flag = parentvo.getAttributeValue("lock_flag")==null?"N":parentvo.getAttributeValue("lock_flag").toString();
    	String primaryKey = parentvo.getPrimaryKey();
    	if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("该单据已经终止!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("是否确定进行终止单据操作?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            	IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                parentvo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(parentvo);
            	getBillUI().showWarningMessage("操作成功");
                onBoRefresh();
                setBoEnabled();
            }
            else{
            	return;
            }
        }
    }
    
    // 上下页的浏览 add by wb at 2008-6-20 14:31:03
    @SuppressWarnings("static-access")
	private void onBoBrows(int intBtn) throws java.lang.Exception {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	UFDateTime begints = ce.getServerTime();
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
		int ts = ce.getServerTime().compareTo(begints);
		getBillUI().showHintMessage("转换第"+(getBufferData().getCurrentRow()+1)+"页完成,耗时"+ts+"(ms)");
        setBoEnabled();
	}

    /**
     * 功能:保存前判断
     * @throws Exception
     * @author LiuYuan
     * 2007-9-27 下午02:43:49
     */
    public void onBoSave() throws Exception {
    	onBoSave_withBillno();
    	setBoEnabled();
    }
    
    /***
     * 在保存时获取单据号 
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
        	   throw new Exception("表体行为空，不能保存！");//add by zhuxb 2011-01-19
//               NCOptionPane.showMessageDialog(getBillUI(), "表体行不能为空!");
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
     * 功能:提交前判断 
     * @throws Exception
     * @author LiuYuan
     * 2007-9-27 下午02:42:41
     */
    public void onBoCommit() throws Exception {
        int currentRow = this.getBillUI().getBufferData().getCurrentRow();
        if (currentRow == -1) {
            getBillUI().showErrorMessage("没有单据选中,无法提交!");
            return;
        }
        //add by houcq  begin 2011-01-05所有单据只能由本人提交
        String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("不允许提交他人申请！");
            return;
        }
        //add by houcq end
        super.onBoCommit();
        setBoEnabled();
    }
    //onBoCommit2方法由houcq增加2011-01-05，司磅单提交时调用
    public void onBoCommit2() throws Exception {
        int currentRow = this.getBillUI().getBufferData().getCurrentRow();
        if (currentRow == -1) {
            getBillUI().showErrorMessage("没有单据选中,无法提交!");
            return;
        }        
        super.onBoCommit();
        setBoEnabled();
    }
    /**
     * 功能: 做上下游单据时,调用setDefaultData()
     * @param bo
     * @author LiuYuan
     * 2007-10-12 下午10:07:29
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
            	   String code=bo.getParent().getCode();			//此得到的结果都是 "增加" 在下面的调用中都会调用两次 setDefaultData(),使得单据号出现间1现象
            	**/
            	String parentcode = bo.getParent().getCode();			
            	String code = bo.getCode();      // 得到点击的按钮的名称 解决上面的问题 edit by wb at 2008-10-6 13:03:08 
                if(parentcode!=null&&parentcode.equals("增加")){
                	UFDate nowdate = _getDate();
        	    	PeriodVO[] periVOs =  (PeriodVO[])getBusiDelegator().queryByCondition(PeriodVO.class, " nyear = "+nowdate.getYear()+" and nmonth = "+nowdate.getMonth()+" and pk_corp = '"+_getCorp().getPk_corp()+"'");
        	    	if(periVOs==null||periVOs.length==0){
        	    		getBillUI().showErrorMessage("本月期间档案没有维护,请维护期间档案!");
        	    		return;
        	    	}
        		    PeriodVO perVO = periVOs[0];
        	    	boolean jz_flag =  perVO.getJz_flag()==null?false:perVO.getJz_flag().booleanValue();
        		    if(jz_flag){
        		    	getBillUI().showErrorMessage(perVO.getNyear()+"年"+perVO.getNmonth()+"月的会计期间已结帐，不允许增加!");
        		        return;
        		    }
        		}
                super.onButton(bo);
                if(isAdding()){ 
                    if(parentcode.equals("增加")&&!"自制单据".equalsIgnoreCase(code)){
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
    	UFDate nowdate = ce.getDate();			//当前登录日期不能小于制单日期
    	String pk = getBillUI().getChangedVOFromUI().getParentVO().getPrimaryKey();
    	if(pk==null||pk.length()==0){
    		getBillUI().showErrorMessage("请选择一张单据!");
    		return;
    	}
    	UFDate dmkedate = new UFDate(getBillUI().getChangedVOFromUI().getParentVO().getAttributeValue("dmakedate")==null?
    			nowdate.toString():getBillUI().getChangedVOFromUI().getParentVO().getAttributeValue("dmakedate").toString());	//制单日期
    	if(nowdate.before(dmkedate)){
    		getBillUI().showErrorMessage("审批日期不能早于制单日期!");
    		return;
    	}
    	super.onBoAudit();
//    	new PubTools().sendMessageByAggVO(getBillUI().getChangedVOFromUI(), _getCorp().getPk_corp(),getUIController().getBillType());
    	setBoEnabled();
    }   
 
    /**
     * 功能:初始化UFDouble型数据
     * * @author honghai
     *  2007-11-7 上午11:25:02
     */
    protected void onBoLineAdd() throws Exception
    {
        super.onBoLineAdd();
//        initUFDouble();//初始化UFDouble型数据
    }
    /**
     * 功能: 增行时字段为UFDouble型的初始为0
     * @author honghai
     * 2007-11-7 上午11:25:02
     */
    private void initUFDouble(){
        BillItem [] items=getBillCardPanelWrapper().getBillCardPanel().getBodyItems();//获得列数
        int curruntrow=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();//返回当前行
        for(int j=0;j<items.length;j++){
            if(items[j].getDataType()==2){
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(new UFDouble(0),curruntrow,items[j].getKey());
            }
        }
    }
    
    /*
     * 功能：文档管理功能
     */
    protected void onBoDocManage() throws Exception{
        // 文档管理
        String billid = getBillUI().getVOFromUI().getParentVO().getPrimaryKey();
        String billno = getBillUI().getVOFromUI().getParentVO().getAttributeValue("vbillcode")==null?""
                :getBillUI().getVOFromUI().getParentVO().getAttributeValue("vbillcode").toString();
        if (billid ==null || billid.equals("")){
            this.getBillUI().showWarningMessage("项目保存以后才可以使用文档功能！");
            return;
        }
        FileManagerDialog dlg = new FileManagerDialog(this.getBillUI(),
                new String[] { billid }, new String[] { billno });
        dlg.setTitle( "单据文档管理");
        dlg.setShowStyle(FileManagerDialog.SHOW_FILE_LOCAL);
        if (dlg.showModal() == nc.ui.pub.beans.UIDialog.ID_CANCEL)
            return;
        String dir = dlg.getDir();
        String fileName = dlg.getSelectedFileName();
        dlg.dispose();
        FileManagerUtil.showFileLocal(dir, fileName);
    
    }
    
    
    //设置按钮的可用状态
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
       
             //上一页 下一页的按钮状态  add by wb at 2008-6-20 14:30:23
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
             //以下代码由houcq 2010-11-01 注释，取消单据终止按钮的处理
             // 在有关闭按钮时对关闭按钮的控制 add by wb at 2008-6-20 14:30:23
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
                         		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(false);	//在业务操作下第一个按钮设为不可操作
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
            getBillUI().showErrorMessage("不允许修改他人申请！");
            return;
        }
        super.onBoEdit();
    }
    
    protected void onBoEdit2() throws Exception {
    	String coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){		//增加了不允许修改别人单据的控制 add by zqy 2010年11月16日15:04:01
            getBillUI().showErrorMessage("不允许修改他人申请！");
            return;
        }
        super.onBoEdit();
    }
    
    /**
     * 此处增加的修改只为司磅单调用(onBoEdit2()被多张单据引用多次，所以新增此方法 add by zqy 2010年11月17日9:10:44)
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
            getBillUI().showErrorMessage("不允许删除他人申请！");
            return;
        }
        super.onBoDelete();
    }
    
    /**
     * 此处增加的删除只为司磅单调用(onBoEdit2()被多张单据引用多次，所以新增此方法 add by zqy 2010年11月17日9:10:44)
     * @throws Exception
     */
    protected void onBoDelete2() throws Exception {
        
        super.onBoDelete();
    }
    
    /* （非 Javadoc）
     * @see nc.ui.trade.manage.ManageEventHandler#onBoReturn()
     */
    protected void onBoReturn() throws Exception {
        // TODO 自动生成方法存根
        super.onBoReturn();
    }
    protected void onBoReturn2() throws Exception {
        // TODO 自动生成方法存根
        super.onBoReturn();
    }
    
    /* （非 Javadoc）
     * @see nc.ui.trade.manage.ManageEventHandler#onBoCard()
     */
    protected void onBoCard() throws Exception {
        // TODO 自动生成方法存根
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
    /* （非 Javadoc）
     * @see nc.ui.trade.manage.ManageEventHandler#onBoRefresh()
     */
    protected void onBoRefresh() throws Exception {
        // TODO 自动生成方法存根
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
    		getBillUI().showErrorMessage("本月期间档案没有维护,请维护期间档案!");
    		return;
    	}
	    PeriodVO perVO = periVOs[0];
    	boolean jz_flag =  perVO.getJz_flag()==null?false:perVO.getJz_flag().booleanValue();
	    if(jz_flag){
	    	getBillUI().showErrorMessage(perVO.getNyear()+"年"+perVO.getNmonth()+"月的会计期间已结帐，不允许增加!");
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
                getBillUI().showErrorMessage("该人员无审批该单据权限");
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
            
            getBillUI().showHintMessage("操作已完成！");
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        onBoRefresh();
     }
    
    
    /**
     * 功能: 在删除前判断,子类如需处理删除操作请务必覆盖本方法
     * @return
     * @throws Exception
     * @return:int 0-未删除,1-已删除
     * @author LiuYuan
     * 2007-10-7 上午09:08:35
     */
    public int onBoDeleteN() throws Exception{
    	String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("不允许删除他人申请！");
            return 0;
        }
    	if (getBillUI().showYesNoMessage("是否确认删除该单据?") != UIDialog.ID_YES) {
            return 0;
        }
        return 1;
    }
    
    /**在做上下游单据回写做处理时确定后的删除,先要通过onboDeleteN()判断是不是确定删除,返回1后在子类中调用此方法
     * @author 王兵 调用例子见 nc.ui.eh.iso.z0501505.ClientEventHandler.java
     * 2008-6-12 10:25:10
     * @throws Exception
     */
    protected void onBoTrueDelete() throws Exception {
    	AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		getBusinessAction().delete(modelVo, getUIController().getBillType(),
				getBillUI()._getDate().toString(), getBillUI().getUserObject());
		if (PfUtilClient.isSuccess()) {
			// 清除界面数据
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
     * 功能：查询对话框显示
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
			sqlWhere = sqlWhere.replaceFirst("审批不通过", String.valueOf(IBillStatus.NOPASS));
			sqlWhere = sqlWhere.replaceFirst("审批通过", String.valueOf(IBillStatus.CHECKPASS));
			sqlWhere = sqlWhere.replaceFirst("审批中", String.valueOf(IBillStatus.CHECKGOING));
			sqlWhere = sqlWhere.replaceFirst("提交态", String.valueOf(IBillStatus.COMMIT));
			sqlWhere = sqlWhere.replaceFirst("作废", String.valueOf(IBillStatus.DELETE));
			sqlWhere = sqlWhere.replaceFirst("冲销", String.valueOf(IBillStatus.CX));
			sqlWhere = sqlWhere.replaceFirst("终止", String.valueOf(IBillStatus.ENDED));
			sqlWhere = sqlWhere.replaceFirst("冻结状态", String.valueOf(IBillStatus.FREEZE));
			sqlWhere = sqlWhere.replaceFirst("自由态", String.valueOf(IBillStatus.FREE));
			if(sqlWhere==null||sqlWhere.equals("")){
				sqlWhere =" 1=1 ";
			}
			if(addCondtion()!=null&&addCondtion().length()>0){
				sqlWhere = sqlWhere + " and "+addCondtion();
			}
			//pk_corp条件
			StringBuffer corp = new StringBuffer()
			.append(" and pk_corp = '"+ce.getCorporation().getPk_corp()+"' ");
			
			AggregatedValueObject aggvo=getBillCardPanelWrapper().getBillVOFromUI();
			String[] keys = aggvo.getParentVO().getAttributeNames();
			//将查询出有BILLNO字段的数据按其排序2009-12-08
			if(keys!=null && keys.length>0){
                for(int i=0;i<keys.length;i++){
                    if(keys[i].equals("billno")){ 
                    	corp.append(" ORDER BY billno ");
                    }
                }
			}
			
			SuperVO[] queryVos = queryHeadVOs(sqlWhere+ corp.toString());
			
			
	       getBufferData().clear();
	       // 增加数据到Buffer
	       addDataToBuffer(queryVos);
	
	       updateBuffer();
        }
        setBoEnabled();//add by houcq 2010-11-24解决查询出来时提交单据可以修改，删除
	}
    
    //查询模板中加上查询条件为制单一个月之内的单据
    protected QueryConditionClient createQueryDLG() {
    	QueryConditionClient dlg = new QueryConditionClient();
    	String billtype = getUIController().getBillType();           // 单据类型
        String sql = "select nodecode from bd_billtype where pk_billtypecode = '"+billtype+"' and isnull(dr,0)=0"; //取节点号
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
			String nodecode = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:
							iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
	        dlg.setTempletID(_getCorp().getPk_corp(), nodecode, null, null); 
	        String lastdate = _getDate().toString().substring(0, 7)+"-01";			//改成当月的1号
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
    
    /**查询时增加条件 " vbilltype = "" and ..."
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

    //  审批意见
	public static void onAppMind(AbstractBillUI ui,String billtype,Object billid) {
		try {
			if (billid == null) {
				ui.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("60310401", "UPP60310401-000029")/* @res "请选择一个单据!" */);
			} else {
				nc.ui.pub.workflownote.FlowStateDlg dlg = new nc.ui.pub.workflownote.FlowStateDlg(ui, billtype, billid.toString());
				dlg.show();
				dlg.closeCancel();
			}
		} catch (Exception e) {
			ui.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("60310401", "UPP60310401-000030")/* @res "查询审批意见失败!" */);
			ui.showErrorMessage(e.getMessage());
		}
	}
	
	/***
	 * 判断eh_perioddiscount_h中是否存在当前月份的折扣，
	 * 时间：2009-12-9 19:30:13
	 * 张志远
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
	 * 判断eh_perioddiscount_h中是否存在该公司的折扣，
	 * 时间：2009-12-15 19:30:13
	 * 张志远
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
