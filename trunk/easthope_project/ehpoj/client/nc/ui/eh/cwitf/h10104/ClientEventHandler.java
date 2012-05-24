package nc.ui.eh.cwitf.h10104;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.cwitf.h10104.ItfDatasourceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * U8����Դ����
 * @author ����
 * 2008-7-8 14:44:46
 */
public class ClientEventHandler extends ManageEventHandler {

	
	
	 public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onBoSave() throws Exception {
         //����ʱ������Ϊ��
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         String dataname = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dataname").getValueObject()==null?"":
			   				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dataname").getValueObject().toString();
         PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		 int res = pubItf.getU8Connection(dataname);
		 if(res==0){
			 getBillUI().showErrorMessage("U8���ݿ�����ʧ��,��鿴NC��Ʒnchome\bin��ncSysConfig.bat����Դ����!\r\nU8���ݿ����Ʊ���������Դ���õ�(���ݿ�/ODBC����)һ��!");
			 return;
		 }else if(res==1){
    		 getBillUI().showWarningMessage("U8���ݿ����ӳɹ�!");
		 }
         //�õ��޸��˵����ƺ��޸ĵ�����
         getBillCardPanelWrapper().getBillCardPanel().setTailItem("editcoperid", _getOperator());
         getBillCardPanelWrapper().getBillCardPanel().setTailItem("editdate", _getDate());
         super.onBoSave();
         
         setBoEnabled();
	 }

	 @Override
	protected void onBoElse(int intBtn) throws Exception {
	        switch (intBtn)
	        {
	            case IEHButton.CREATEVOUCHER:    // ��������
	                onBoCreateVoucher(intBtn);
	                break;
	        }   
	    }
	 
     private void onBoCreateVoucher(int intBtn) throws Exception{
         String dataname = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dataname").getValueObject()==null?"":
			   				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dataname").getValueObject().toString();
         PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		 int res = pubItf.getU8Connection(dataname);
		 if(res==0){
			 getBillUI().showErrorMessage("U8���ݿ�����ʧ��,��鿴NC��Ʒnchome\bin��ncSysConfig.bat����Դ����!\r\nU8���ݿ����Ʊ���������Դ���õ�(���ݿ�/ODBC����)һ��!");
			 return;
		 }else if(res==1){
    		 getBillUI().showWarningMessage("U8���ݿ����ӳɹ�!");
		 }
	}

	@Override
	protected void onBoQuery() throws Exception {
    	 nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
         SuperVO[] supervo;
         supervo=business.queryByCondition(ItfDatasourceVO.class, " pk_corp='"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0");
         getBufferData().clear();
         addDataToBuffer(supervo);
         updateBuffer(); 
         setBoEnabled();
        }
        
     @Override
    protected void onBoCard() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoCard();
    	setBoEnabled();
    }
     
     //���ð�ť�Ŀ���״̬
     protected void setBoEnabled() throws Exception {
         AggregatedValueObject aggvo=getBillUI().getVOFromUI();
         String pk = aggvo.getParentVO().getPrimaryKey();
         if (pk==null){
         }
         else{   
             getButtonManager().getButton(IBillButton.Add).setEnabled(false);
 	      }
          
         getBillUI().updateButtonUI();
     }
     
     @Override
    public void onBoAdd(ButtonObject bo) throws Exception {
    	 nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
         SuperVO[] supervo = business.queryByCondition(ItfDatasourceVO.class, " pk_corp='"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0");
         if(supervo!=null&&supervo.length>0){
        	 getBillUI().showErrorMessage("�Ѿ��������ӷ�ʽ,���޸�!");
        	 onBoQuery();
        	 return;
         }
    	super.onBoAdd(bo);
    }
}
