package nc.ui.eh.cw.h1103015;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.SuperVO;

/**
 * ˵�����տȷ��
 * @author ����Դ 
 * ʱ�䣺2008-5-28 14:36:14
 */
public class ClientEventHandler extends AbstractEventHandler {
    
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
    
     @Override
	public void onBoSave() throws Exception {
         //�ǿ��ж�
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         super.onBoSave();
         
     }

     @Override
	protected void onBoElse(int intBtn) throws Exception {
            switch (intBtn) {
            case IEHButton.CONFIRMBUG:  //ȷ�ϰ�ť
                setEditionChange();
                break;
            }
        }
        
        private void setEditionChange() throws Exception {
            
            //���ȷ�ϰ�ť֮��ȷ��״̬��ȷ���ˣ�ȷ�������Զ����
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_flag", "Y");
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_psndoc", _getOperator());
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_rq", _getDate());
            
            onBoSave();
            
        }
        
        @Override
		protected void onBoCard() throws Exception {
            super.onBoCard();
        }
        
        
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
            
            //��ѯ����
            String billtype = IBillType.eh_h1103005;
            SuperVO[] queryVos = queryHeadVOs(sqlWhere.toString()+" and (vbilltype = '"+billtype+"') ");

            getBufferData().clear();
            // �������ݵ�Buffer
            addDataToBuffer(queryVos);
            updateBuffer(); 
        }
        
}