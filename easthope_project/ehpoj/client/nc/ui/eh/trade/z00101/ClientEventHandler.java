package nc.ui.eh.trade.z00101;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;

/**
 * 功能：片区管理
 * @author 张起源
 * 日期：2008-3-25
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
        //非空的有效性判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        //片区编码必须是2的位数
        String areacode = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("areacode").getValueObject().toString().trim();
        
        if(areacode.length()%2!=0){
        	getBillUI().showErrorMessage("片区编码必须以'XX-XX-XX-XX-XX'格式!");
        	return;
        }
        
        super.onBoSave();
        onBoRefresh();
	}

	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
	}
 
    @Override
	protected void onBoDelete() throws Exception {
        super.onBoDelete();
        ((ClientUI)getBillUI()).getBillTreeSelectNode();
        ((ClientUI)getBillUI()).getBillTree().setSelectionRow(0);
        onBoRefresh();
        
    }

    @Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
        super.onBoAdd(arg0);
        VOTreeNode node=((ClientUI)getBillUI()).getBillTreeSelectNode();
        if(node==null){
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("areacode", "");
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("areacode").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("areaname").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("stop_flag").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memo").setEnabled(true);
            getBillUI().updateUI();
        }else{
            String code=node.getData().getAttributeValue("areacode").toString();
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("areacode", code);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("areacode").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("areaname").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("stop_flag").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memo").setEnabled(true);
            getBillUI().updateUI();
        }
        
    }
    
}
