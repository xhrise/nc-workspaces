package nc.ui.eh.trade.z00102;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;

/**
 * ���ܣ��������
 * @author ����Դ
 * ���ڣ�2008-4-24
 */
public class ClientEventHandler extends TreeCardEventHandler {
	
	public ClientEventHandler(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	public boolean isAllowDelNode(nc.ui.trade.pub.TableTreeNode node) {
		return true;
	}
		
	@Override
	protected void onBoSave() throws Exception {
        //�ǿյ���Ч���ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
            
        super.onBoSave();
        onBoRefresh();
	}

	@Override
	protected void onBoEdit() throws Exception {
		
		super.onBoEdit();
	}
	
	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		String endflag = (String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("endflag").getValueObject();
		if(endflag.equals("true")){
			getBillUI().showErrorMessage("�ü�����ĩ���������ٽ������ӣ�");
			return;
		}
		super.onBoAdd(arg0);
        VOTreeNode node=((ClientUI)getBillUI()).getBillTreeSelectNode();
        if(node!=null){
            String code=node.getData().getAttributeValue("invclasscode").toString();
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("invclasscode", code);
            getBillUI().updateUI();
        }
	}
    
    @Override
    protected void onBoDelete() throws Exception {
        // TODO Auto-generated method stub
        super.onBoDelete();
        ((ClientUI)getBillUI()).getBillTreeSelectNode();
        ((ClientUI)getBillUI()).getBillTree().setSelectionRow(0);
        onBoRefresh();
        
    }
    
	
}
