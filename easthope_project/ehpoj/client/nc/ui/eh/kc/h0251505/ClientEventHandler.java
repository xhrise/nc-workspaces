package nc.ui.eh.kc.h0251505;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * ˵�����������ϵ�
 * @author ����Դ 
 * ʱ�䣺2008-5-07
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn){
         case IEHButton.LOCKBILL:    //�رյ���
//             onBoLockBill();
             break;
		}
		super.onBoElse(intBtn);
	}

	@Override
	public void onBoSave() throws Exception {
		//�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
        //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("ԭ�ϱ����Ѿ����ڣ������������");
            return;
        } 
        
        super.onBoSave();
		}
	
	@Override
	public void onButton_N(ButtonObject bo, BillModel model) {      
        super.onButton_N(bo, model);
        String bocode=bo.getCode();
        //�����������������������ϵ�ʱ���������񵥺Ų������޸�
        if(bocode.equals("��������")){
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_posm").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getBodyItem("blmount").setEnabled(false);
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for(int i=0;i<row;i++){
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"kc", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvcode", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vunit", false);
            }
	          getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
	          getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
	          getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
	          getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
	          getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
            }
            getBillUI().updateUI();
         }

    @Override
	protected void onBoEdit() throws Exception {
//        getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").setEnabled(false);
        getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vunit").setEnabled(false);
        getBillCardPanelWrapper().getBillCardPanel().getBodyItem("blmount").setEnabled(true);
        super.onBoEdit();
    }
    
}