/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.stock.h0150335;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.InvdocYLKCRefModel;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.h08003.YLKcVO;
import nc.vo.pub.lang.UFDouble;

/**
 * ����:���ɹ�����
 * ZB25
 * @author WB
 * 2009-1-9 10:42:36
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	//Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("��������ͬ���ϴ��ڣ������������");
            return;
        }
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < row; i++) {	
        	UFDouble kcamount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kcamount")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kcamount").toString());	               
            UFDouble blmount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "blmount")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "blmount").toString());	 
            if (kcamount.doubleValue()==0)
            {
            	getBillUI().showErrorMessage("��"+(i+1)+"�п�����Ϊ�㣬���������!");
            	return ;
            }else if (kcamount.doubleValue()>0)
            {
            	if (blmount.doubleValue()<=0||blmount.doubleValue()>kcamount.doubleValue())
            	{
            		getBillUI().showErrorMessage("��������ֻ�ܴ���0����С�ڵ���"+kcamount);
                	return ;
            	}
            }else
            {
            	if (blmount.doubleValue()>=0||blmount.doubleValue()<kcamount.doubleValue())
            	{
            		getBillUI().showErrorMessage("��������ֻ�ܴ��ڵ���"+kcamount+"����С����");
                	return ;
            	}
            }            
        }	        
         super.onBoSave_withBillno();
    }
    
    @Override
	public void onButton_N(ButtonObject bo, BillModel model) {
            super.onButton_N(bo, model);
            try {
					crateKCDATA();		//����������� add by houcq 2011-02-24 15:04:42
			} catch (Exception e) {
				e.printStackTrace();
			}
			InvdocYLKCRefModel kcref = new InvdocYLKCRefModel();
           ClientUI.ref.setRefModel(kcref);
           getBillUI().updateUI();
     }
    
    @Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    }
    
    @Override
	public String addCondtion(){
    	return " vbilltype = '"+IBillType.eh_h0150335+"'";
    }
    //add by houcq 2011-02-24
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();    	
    	InvdocYLKCRefModel kcref = new InvdocYLKCRefModel();
        ClientUI.ref.setRefModel(kcref);
    }
    /**
     * ������������ ������ʱʹ��
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    //add by houcq 2011-02-24
	public void crateKCDATA() throws Exception{
    	String pk_corp = _getCorp().getPk_corp();
    	HashMap hmkc = new PubTools().getAllInvKcAmount(pk_corp,_getDate());
    	String key = null;
        Iterator iter = hmkc.keySet().iterator();
        ArrayList arr = new ArrayList();
        UFDouble amount = new UFDouble();
        IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
        ivoPersistence.deleteByClause(YLKcVO.class, "pk_corp = '"+pk_corp+"'");		//��ɾ�����ڵ�����
        while(iter.hasNext()){
        	Object o = iter.next();
            key =o.toString();
            amount = new UFDouble(hmkc.get(key)==null?"0":hmkc.get(key).toString());
            
            YLKcVO kcvo = new YLKcVO();
            kcvo.setPk_invbasdoc(key);
            kcvo.setAmount(amount);
            kcvo.setPk_corp(_getCorp().getPk_corp());
            
            arr.add(kcvo);
        }
        YLKcVO[] kcvos = (YLKcVO[])arr.toArray(new YLKcVO[arr.size()]);
        ivoPersistence.insertVOArray(kcvos);
    }
}

