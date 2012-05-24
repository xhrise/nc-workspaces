
package nc.ui.eh.stock.z00155;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
/**
 * ����ά��
 * @throws Exception
 * @author ����
 * 2008-05-01 ����04:03:18
 */
public class ClientEventHandler extends TreeCardEventHandler {
	
	public ClientEventHandler(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean isAllowDelNode(nc.ui.trade.pub.TableTreeNode node) {
		return true;
	}
		
    @Override
	protected void onBoSave() throws Exception {
            //    	���ù�˾����
//            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
//            for (int i = 0; i < row; i++) {               
//                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");  
    	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
//            }
            //�ǿյ���Ч���ж�
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
            super.onBoSave();
            onBoRefresh();
    }
    
    @Override
    protected void onBoDelete() throws Exception {
    	super.onBoDelete();
    	onBoRefresh();
    }
}
