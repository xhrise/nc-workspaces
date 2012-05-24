/**
 * @(#)ClientUI.java	V3.1 2007-3-9
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.z0205520;

import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.trade.pub.IBillStatus;


public class ClientUI extends MultiChildBillManageUI {
	

	public ClientUI() {
		// TODO Auto-generated constructor stub
		super();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.manage.BillManageUI#createController()
	 */
	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.manage.BillManageUI#setBodySpecialData(nc.vo.pub.CircularlyAccessibleValueObject[])
	 */
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.manage.BillManageUI#setHeadSpecialData(nc.vo.pub.CircularlyAccessibleValueObject,
	 *      int)
	 */
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.manage.BillManageUI#setTotalHeadSpecialData(nc.vo.pub.CircularlyAccessibleValueObject[])
	 */
	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.trade.base.AbstractBillUI#createBusinessDelegator()
	 */
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		// TODO �Զ����ɷ������
		return new ClientBaseBD();
	}

	

	@Override
	protected void initSelfData() {
		getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
        getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
        getBillCardWrapper().initHeadComboBox("discounttype",ICombobox.STR_discounttype,true);		//�ۿ۶�����
        getBillListWrapper().initHeadComboBox("discounttype",ICombobox.STR_discounttype,true);
        getBillCardWrapper().initHeadComboBox("policetype",ICombobox.STR_polictype,true);			//��������
        getBillListWrapper().initHeadComboBox("policetype",ICombobox.STR_polictype,true);
        getBillCardWrapper().initHeadComboBox("jsmethod",ICombobox.STR_JSMETHOD,true);				//���㷽ʽ
        getBillListWrapper().initHeadComboBox("jsmethod",ICombobox.STR_JSMETHOD,true);
        getBillCardWrapper().initHeadComboBox("jstype",ICombobox.STR_JSTYPE,true);					//��������
        getBillListWrapper().initHeadComboBox("jstype",ICombobox.STR_JSTYPE,true);
        getBillCardWrapper().initHeadComboBox("invjstype",ICombobox.STR_INVJSTYPE,true);					//��������
        getBillListWrapper().initHeadComboBox("invjstype",ICombobox.STR_INVJSTYPE,true);
	}

	@Override
	public void setDefaultData() throws Exception {
//    ��ͷ���õ�
        BillCodeObjValueVO objVO = new BillCodeObjValueVO();
        objVO.setAttributeValue("ZA08", getUIControl().getBillType());
        String billNo = BillcodeRuleBO_Client.getBillCode("ZA08", _getCorp().getPrimaryKey(), null,
                                                          objVO);
        getBillCardPanel().setHeadItem("billno",billNo);  
        
        //��ͷ���ù�˾����        
        getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());
        
        //��ͷ��ɾ�����  
        getBillCardPanel().setHeadItem("dr",new Integer(0));
      
        //�ڱ�β�����Ƶ���
        getBillCardPanel().setTailItem("coperatorid",_getOperator());   
       
        //�ڱ�β�����Ƶ�����
        getBillCardPanel().setTailItem("dmakedate",getClientEnvironment().getDate());

        //����״̬
        getBillCardPanel().setHeadItem("vbillstatus",Integer.valueOf(String.valueOf(IBillStatus.FREE)));
   
	}
    
	@Override
    public ManageEventHandler createEventHandler() {
    	// TODO Auto-generated method stub
    	return new AbstractSPEventHandler(this, this.getUIControl());
    }
	
}
