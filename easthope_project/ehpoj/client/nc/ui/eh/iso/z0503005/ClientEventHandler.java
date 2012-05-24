package nc.ui.eh.iso.z0503005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.iso.z0503005.KzkjBVO;

/**
 * ˵�������ؿۼ۹�ʽ
 * @author ����
 * ʱ�䣺2008��10��9��15:02:48 
 */
public class ClientEventHandler extends AbstractEventHandler {
   
	
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@Override
	public void onBoSave() throws Exception {
		//�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
        //û�����ϵ���������������ϵ����֮��
        KzkjBVO [] bvos=(KzkjBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        for(int i=0;i<bvos.length;i++){
        	KzkjBVO bvo=bvos[i];
        	String pk_invbasdoc = bvo.getPk_invbasdoc()==null?"":bvo.getPk_invbasdoc().toString();
        	if(pk_invbasdoc.equals("")){
        		for(int j=0;j<bvos.length-i-1;j++){
            		KzkjBVO bvo2=bvos[j+i+1];
                	String pk_invbasdoc2 = bvo2.getPk_invbasdoc()==null?"":bvo2.getPk_invbasdoc().toString();
                	if(!pk_invbasdoc2.equals("")){
                		getBillUI().showErrorMessage("û�����ϵ��������ά�������");
                		return;
                	}
            	}
        	}
        }
        super.onBoSave();
	}
	
    @Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		switch (intBtn)
        {
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
                nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000503",null,new String[] { 
                		nc.vo.format.Format.indexFormat(getBufferData().getCurrentRow()+1) }));
          setBoEnabled();
    }
     
     
     /**
      * ��������޸� add by zqy 2010��11��23��15:56:54
      */
     protected void onBoEdit() throws Exception {
     	super.onBoEdit3();
     }
   
    /**
     * �������ɾ�� add by zqy 2010��11��23��15:57:13
     */
    protected void onBoDelete() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoDelete2();
    }
     
}