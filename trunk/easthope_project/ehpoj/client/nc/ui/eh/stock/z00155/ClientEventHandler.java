
package nc.ui.eh.stock.z00155;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
/**
 * 车间维护
 * @throws Exception
 * @author 王明
 * 2008-05-01 下午04:03:18
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
            //    	设置公司编码
//            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
//            for (int i = 0; i < row; i++) {               
//                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");  
    	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
//            }
            //非空的有效性判断
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
