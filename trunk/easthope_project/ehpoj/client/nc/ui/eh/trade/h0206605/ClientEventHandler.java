/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.h0206605;


import java.io.File;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.ui.eh.businessref.LadingbillRefModel;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.h0206605.TradeTransportcontractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能:货物运输合同
 * ZA95
 * @author WB
 * 2008-10-10 11:35:11 
 *
 */

public class ClientEventHandler extends AbstractEventHandler {
    
	TradeTransportcontractVO oldheadvo = null;
	String oldpk = null;						//变更前的PK
	private int res = 0;
	private File txtFile = null;
	private nc.ui.pub.beans.UITextField txtfFileUrl = null; // 文本框,用于显示文件路径
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
        ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			ButtonObject boCONGEAL = new ButtonObject("作废");
			boCONGEAL.setTag(String.valueOf(IEHButton.CONGEAL));
			boCONGEAL.setCode("作废");
			bo.addChildButton(boCONGEAL);
		}
    }

   
    @Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
			case IEHButton.STOCKCHANGE:// 合同变更
				onBoStockChange();
				break;
			case IEHButton.CONGEAL:		// 合同作废
				onBoCONGEAL();
				break;
			case IEHButton.CONFIRMBUG: //上传照片
				this.onBoConfirm();
				break;
		}
		super.onBoElse(intBtn);
	}
    
    //合同作废
    private void onBoCONGEAL()  throws Exception {
    	TradeTransportcontractVO vo = (TradeTransportcontractVO)getBillUI().getVOFromUI().getParentVO();
    	int ok=getBillUI().showYesNoMessage("是否确认作废此合同?");
        if(ok==MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
        	IVOPersistence  iVOPersistence =   (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
        	vo.setZf_flag(new UFBoolean(true));
        	iVOPersistence.updateVO(vo);
        	setBoEnabled();
        }
	}


	//合同变更具体实现法方
	private void onBoStockChange() {
        try {
            AggregatedValueObject aggvo = getBillUI().getVOFromUI();
            oldheadvo = (TradeTransportcontractVO)aggvo.getParentVO();
            oldpk = oldheadvo.getPrimaryKey();
            int ok=getBillUI().showYesNoMessage("是否确认进行合同变更?");
               if(ok==MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                   onBoCopy();
                   String billno=oldheadvo.getBillno();
                   String[] ss=billno.split("\\-");
                   if(ss.length==1){
                      getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(billno+"-01");
                   }
                   else{
                       int code=Integer.parseInt(ss[1]);
                       String newreqcode="";
                       if(code<9){
                           newreqcode=ss[0]+"-0"+(code+1);
                       }else{
                           newreqcode=ss[0]+"-"+(code+1);
                       }
                      getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(newreqcode);
                   }
               }
               
               //将版本号进行改变在原基础上加1
               Integer ver=oldheadvo.getVer();
               ver=ver+1;
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ver").setValue(ver);
               getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
               getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapproveid").setValue(null);
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dapprovedate").setValue(null);
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapprovenote").setValue(null);
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(new Integer(8));
               String pk_cubasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
   				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
			   	LadingbillRefModel.pk_cubasdoc = pk_cubasdoc;  			//将客商传到参照中
               getBillUI().updateUI();
               
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}
	
	/**
	 * 功能：读取文件地址，将文件地址存放到驾驶员照片项目
	 * 时间：2009-12-09
	 * 作者：张志远
	 */
	public void onBoConfirm(){
		File[] files = null;
		String filepath = "";
		try {
            nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
            
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setMultiSelectionEnabled(true);
            res = fileChooser.showOpenDialog(getBillUI());
            if (res == 0) {
                
                files = fileChooser.getSelectedFiles();
                if(files.length>0){
                	for(int i=0;i<files.length;i++){
                		getTFLocalFile().setText(fileChooser.getSelectedFile().getAbsolutePath());
                		txtFile = fileChooser.getSelectedFile();
                		filepath = filepath+txtFile.getAbsolutePath();
                	}
                }
                this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_2", filepath);
                
            } else {
                return;
            }
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
		
		
	}
	
	private nc.ui.pub.beans.UITextField getTFLocalFile() {
        if (txtfFileUrl == null) {
            try {
                txtfFileUrl = new nc.ui.pub.beans.UITextField();
                txtfFileUrl.setName("txtfFileUrl");
                txtfFileUrl.setBounds(270, 160, 230, 26);
                txtfFileUrl.setMaxLength(2000);
                txtfFileUrl.setEditable(false);

            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        }
        return txtfFileUrl;
    }
	
	private void handleException(java.lang.Throwable exception) {
        System.out.println("--------- 未捕捉到的异常 ---------");
        exception.printStackTrace(System.out);
    }
	
    @Override
	public void onBoSave() throws Exception {
    	TradeTransportcontractVO vo = (TradeTransportcontractVO)getBillUI().getVOFromUI().getParentVO();
        if(oldheadvo!=null){
        	oldheadvo.setLock_flag(new UFBoolean(true));
        	IVOPersistence  iVOPersistence =   (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
        	iVOPersistence.updateVO(oldheadvo);			//将旧版本的合同关闭
        	oldheadvo = null;
        }
    	super.onBoSave();
    }

    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    	String pk_cubasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
    				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
    	LadingbillRefModel.pk_cubasdoc = pk_cubasdoc;  			//将客商传到参照中
    }
   
    @Override
    protected void onBoQuery() throws Exception {
    	super.onBoQuery();
    }
    
    @Override
    public void onBoAudit() throws Exception {
    	super.onBoAudit();
    	setBoEnabled();
    }
    @Override
    protected void setBoEnabled() throws Exception {
    	super.setBoEnabled();
    	 AggregatedValueObject aggvo=getBillUI().getVOFromUI();
         Integer vbillstatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
         if (vbillstatus==null){
         }
         else{   
             switch (vbillstatus.intValue()){
                 //free
                 case IBillStatus.FREE:
                         getButtonManager().getButton(IEHButton.STOCKCHANGE).setEnabled(false);
                         break;
                 //commit
                 case IBillStatus.COMMIT:
                     getButtonManager().getButton(IEHButton.STOCKCHANGE).setEnabled(false);
                     break;
                 //CHECKGOING
                 case IBillStatus.CHECKGOING:
                     getButtonManager().getButton(IEHButton.STOCKCHANGE).setEnabled(false);
                     break;
                 //CHECKPASS
                 case IBillStatus.CHECKPASS:
                	 getButtonManager().getButton(IEHButton.STOCKCHANGE).setEnabled(true);
                     break;
                 //NOPASS
                 case IBillStatus.NOPASS:
                	 getButtonManager().getButton(IEHButton.STOCKCHANGE).setEnabled(false);
                     break;
             }
         }
             
             // 在有关闭按钮时对作废按钮的控制 
             String[] keys = aggvo.getParentVO().getAttributeNames();
             if(keys!=null && keys.length>0){
                 for(int i=0;i<keys.length;i++){
                     if(keys[i].endsWith("zf_flag")){ 
                     	String zf_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zf_flag").getValueObject()==null?"N":
                             getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zf_flag").getValueObject().toString();
                         if(zf_flag.equals("false")){
                         	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                         		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[1].setEnabled(true);	
                         	}
                         }else{
                         	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                         		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[1].setEnabled(false);	//在业务操作下第二个按钮设为不可操作
                         	}
                         	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                            getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                            getButtonManager().getButton(IEHButton.STOCKCHANGE).setEnabled(false);
                         }
                         break;
                     }
                     
                 }
             }
         getBillUI().updateButtonUI();
    }
}

