package nc.ui.eh.kc.h0256005;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵������؛��ⵥ
 * @author ����Դ
 * �r�g��2008-5-27 10:00:17
 */

public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
    
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {		 
		//�������
		getBillCardPanel().setHeadItem("rkdate", _getDate());  
//        getBillCardPanel().setTailItem("dmakedate",_getDate());
//        String pc = PubTools.getPC("eh_sc_cprkd", _getDate()); //�������  �ں���ȡ��Ʊ������
//        getBillCardPanel().setHeadItem("pc", pc);
   
		super.setDefaultData();
	}
    
	@Override
	protected void initSelfData() {        
	     super.initSelfData();
	}
    
    @Override
	public void afterEdit(BillEditEvent e) {
        String strKey = e.getKey();
        int row = getBillCardPanel().getBillTable().getRowCount();
        //����������������*����
        if(strKey.equals("rkmount")|| strKey.equals("price")){            
            for(int i=0;i<row;i++){
                UFDouble rkmount = new UFDouble(getBillCardPanel().getBodyValueAt(i, "rkmount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "rkmount").toString());
                UFDouble price = new UFDouble(getBillCardPanel().getBodyValueAt(i, "price")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "price").toString());
                UFDouble sum = rkmount.multiply(price);
                getBillCardPanel().setBodyValueAt(sum, i, "summoney");
            }
            
            
        } 
        super.afterEdit(e);
    }
    @Override
    protected void initPrivateButton() {
	 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.UnCheck,"���岻���","���岻���");
        btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn2);
    	super.initPrivateButton();
    }


}
