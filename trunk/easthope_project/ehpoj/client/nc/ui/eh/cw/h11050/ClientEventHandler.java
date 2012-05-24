/**
 * @(#)MyEventHandler.java	V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.cw.h11050;

import java.util.HashMap;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;

/**
 * ˵��: �ɱ�����
 * @author ����
 * 2008-5-28 14:29:08
 */

public class ClientEventHandler extends ManageEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"refpk"});
        if(res==1){
            getBillUI().showErrorMessage("���ն����Ѿ����ڣ������������");
            return;
        } 
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        HashMap hm = new HashMap();
        String objtype = null;
        for(int i=0;i<row;i++){
        	objtype = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "objtype")==null?"":
        					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "objtype").toString();
        	hm.put(objtype, objtype);
        }
        if(hm.size()>1){
        	getBillUI().showErrorMessage("�ɱ�����ֻ��ѡ��һ������, ��鿴!");
        	return;
        }
        int type = -1;
        if(objtype==ICombobox.STR_OBJTYPE[0]) {
        	type = 0;
        }
        if(objtype==ICombobox.STR_OBJTYPE[1]) {
        	type = 1;
        }
        if(objtype==ICombobox.STR_OBJTYPE[2]) {
        	type = 2;
        }
        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("objtype", type);
        getBillUI().updateUI();
		super.onBoSave();
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("�������޸��������룡");
            return;
        }
        super.onBoEdit();
     }

	@Override
	protected void onBoDelete() throws Exception {
		String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("������ɾ���������룡");
            return;
        }
        super.onBoDelete();
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
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
     
//  ���ð�ť�Ŀ���״̬
    protected void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
        String pk_bom = aggvo.getParentVO().getPrimaryKey();
        if (pk_bom==null){
        }
        else{   
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
            // ���йرհ�ťʱ�Թرհ�ť�Ŀ��� add by wb at 2008-6-20 14:30:23
            String[] keys = aggvo.getParentVO().getAttributeNames();
            if(keys!=null && keys.length>0){
                for(int i=0;i<keys.length;i++){
                    if(keys[i].endsWith("lock_flag")){ 
                    	String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
                            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
                        if(lock_flag.equals("false")){
                        	getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(true);
                        }else{
                        	getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(false);
                        	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                            getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                        }
                        break;
                    }
                       
                }
            }
        }
        getBillUI().updateButtonUI();
    }
    
    @Override
   protected void onBoCard() throws Exception {
   	super.onBoCard();
   	setBoEnabled();
    }
}
