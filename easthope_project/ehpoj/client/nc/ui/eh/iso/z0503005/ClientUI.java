
package nc.ui.eh.iso.z0503005;

import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFBoolean;

/**
 * ˵�������ؿۼ۹�ʽ
 * @author ����
 * ʱ�䣺2008��10��9��15:02:48 
 */
public class ClientUI extends AbstractClientUI {
	public ClientUI() {
	     super();
	 }
   
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
	
	@Override
	protected void initSelfData() {
		 getBillCardWrapper().initHeadComboBox("istopstandard",ICombobox.CW_SXZB, true);
	     getBillListWrapper().initHeadComboBox("istopstandard",ICombobox.CW_SXZB, true);
		 getBillCardWrapper().initHeadComboBox("iskzkj",ICombobox.CW_KZKJ, true);
	     getBillListWrapper().initHeadComboBox("iskzkj",ICombobox.CW_KZKJ, true);
	     super.initSelfData();
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		String Str=e.getKey();
		int row=e.getRow();
		//�Լ۸��Ƿ��ܱ༭���ж�(���Ǳ���۸�ʱ���ܱ༭)
		if(Str.equals("ismain")){
			UFBoolean ismain=new UFBoolean(getBillCardPanel().getBodyValueAt(row, "ismain")==null?"":getBillCardPanel().
					getBodyValueAt(row, "ismain").toString());
			if(ismain.toString() == "Y"){
				getBillCardPanel().getBillModel().setCellEditable(row,"price", true);
			}else{
				getBillCardPanel().getBillModel().setCellEditable(row,"price", false);
			}
		}
         super.afterEdit(e);
	}

	@Override
	public void setDefaultData() throws Exception {
		//��˾����ĳ�ʼ��
		String pk_corp = _getCorp().getPrimaryKey();
		getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);
		//�Ƶ��˵ĳ�ʼ��
		BillItem oper = getBillCardPanel().getTailItem("coperatorid");
        if (oper != null)
            oper.setValue(_getOperator());
        else
            getBillCardPanel().getHeadItem("coperatorid").setValue(_getOperator());
        //�Ƶ����ڵĳ�ʼ��
        BillItem date = getBillCardPanel().getTailItem("dmakedate");
        if (date != null)
            date.setValue(_getDate());
        else
            getBillCardPanel().getHeadItem("dmakedate").setValue(_getDate());
	}
}