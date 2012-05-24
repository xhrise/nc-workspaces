package nc.ui.eh.kc.h0252005;

import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.kc.h0252005.ScDbdBVO;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵�������ϵ�����
 * @author ����
 * 2008-05-07 ����04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
    public void onBoSave() throws Exception {
	   // �Էǿպ�0��֤
	   getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
	   //�����ߵ������ж��Ƿ���ͬ
	   int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
	   ScDbdBVO[] bvo=(ScDbdBVO[]) getBillUI().getVOFromUI().getChildrenVO();
	   UFDouble sumDcmount = new UFDouble();
	   UFDouble sumDrrmount = new UFDouble();
	   StringBuilder tip= new StringBuilder();
	   for(int i=0;i<rows;i++){
		   UFDouble dcmount=bvo[i].getDcmount();		   
 		   UFDouble drrmount=bvo[i].getDrrmount();
		   sumDcmount=sumDcmount.add(dcmount);
		   sumDrrmount=sumDrrmount.add(drrmount);
		   UFDouble pcmount=bvo[i].getPcamount();
		   if (pcmount==null)
		   {
			   pcmount=new UFDouble(0);
		   }
		   if (pcmount.doubleValue()>0)
		   {
			   if (dcmount.doubleValue()==0 ||dcmount.doubleValue()>pcmount.doubleValue())
			   {
				   tip.append("��"+(i+1)+"���ϵ�������Ϊ�����ڿ������,���������!\r\n");
			   }
		   }
		   else if (pcmount.doubleValue()<0)
		   {
			   if (dcmount.doubleValue()>=0 ||dcmount.doubleValue()<pcmount.doubleValue())
			   {
				   tip.append("��"+(i+1)+"���ϵ�������Ϊ�����ڿ������,���������!\r\n");
			   }
		   }else
		   {
			   tip.append("��"+(i+1)+"���Ͽ������Ϊ��,���������!\r\n");
		   }
		   
	   }
	   if (!"".equals(tip.toString()))
	   {
		   getBillUI().showErrorMessage(tip.toString());
		   return; 
	   }
	   if(!(sumDcmount.equals(sumDrrmount))){
		   getBillUI().showErrorMessage("��ĳ��������������������ͬ�����飡����");
		   return;
	   }
		super.onBoSave();
	}
	
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), row, "vmanager");
        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), row, "pk_manage");
        String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_manage").getEditFormulas();//��ȡ�༭��ʽ
        getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row,formual);
	}
	
}
