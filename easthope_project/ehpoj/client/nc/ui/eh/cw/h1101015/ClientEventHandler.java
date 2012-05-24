package nc.ui.eh.cw.h1101015;


import nc.ui.eh.button.IEHButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.SuperVO;

/**
 * ����˵��������ȷ��
 * @author ����
 * 2008-05-28 ����04:03:18
 */

public class ClientEventHandler extends ManageEventHandler {
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IEHButton.SUREMONEY: 
			sureMoney();
			break;
		}
	}

	@Override
    public void onBoSave() throws Exception {
		super.onBoSave();
	}
       //	add wangming 08.05.10
	   @Override
	protected void onBoQuery() throws Exception {
			StringBuffer sbWhere = new StringBuffer();
			if(askForQueryCondition(sbWhere)==false) 
				return;		
			String sqlWhere = sbWhere.toString();
			sqlWhere = sqlWhere.replaceFirst("������ͨ��", "0");
			sqlWhere = sqlWhere.replaceFirst("����ͨ��", "1");
			sqlWhere = sqlWhere.replaceFirst("������", "2");
			sqlWhere = sqlWhere.replaceFirst("�ύ̬", "3");
			sqlWhere = sqlWhere.replaceFirst("����", "4");
			sqlWhere = sqlWhere.replaceFirst("����", "5");
			sqlWhere = sqlWhere.replaceFirst("��ֹ", "6");
			sqlWhere = sqlWhere.replaceFirst("����״̬", "7");
			sqlWhere = sqlWhere.replaceFirst("����̬", "8");
			
			SuperVO[] queryVos = queryHeadVOs(sqlWhere);
			
	       getBufferData().clear();
	       // �������ݵ�Buffer
	       addDataToBuffer(queryVos);

	       updateBuffer();
		}
	   //����ȷ�ϰ�ť�Ĵ�����
	   public void sureMoney() throws Exception{
		   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_flag", "true");
		   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_psndoc", _getOperator());      //ȷ����
		   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_rq", _getDate());  	         //ȷ������
		  
 		   onBoSave();
 		   setBoEnabled();
	   }
	   protected void setBoEnabled() throws Exception {
//	        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
//	        aggvo.getParentVO()
		   String qr_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qr_flag").getValueObject().toString();
		   if(qr_flag.equals("false")){
			   getButtonManager().getButton(IEHButton.SUREMONEY).setEnabled(true);
		   }else{
			   getButtonManager().getButton(IEHButton.SUREMONEY).setEnabled(false);
		   }
	        getBillUI().updateButtonUI();
	    }
	   @Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		setBoEnabled();
	}
	   
	   
}
