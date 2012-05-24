
package nc.ui.eh.stock.z00155;

import nc.bs.eh.stock.z00155.ClientUICheckRuleGetter;
import nc.ui.pub.beans.UITree;
import nc.ui.trade.base.IChildMenuController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.BillTableCreateTreeTableTool;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * 车间维护
 * @throws Exception
 * @author 王明
 * 2008-05-01 下午04:03:18
 */
public class ClientUI extends BillTreeCardUI{
	
    public static String pk_corp = null;
    
	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
        pk_corp = _getCorp().getPrimaryKey();
		return new ClientManageData();
	}

	@Override
	public void afterInit() throws java.lang.Exception{
		modifyRootNodeShowName("车间");
	}

	@Override
	protected ITreeCardController createController() {
		return new ClientCtrl();
	}

	@Override
	protected CardEventHandler createEventHandler() {
		return new ClientEventHandler(this,getUIControl());
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	@Override
	protected void initSelfData() {   

	}
    
	@Override
	public void setDefaultData() throws Exception {
		VOTreeNode node = getBillTreeSelectNode();
		if(node==null ){
			this.getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		}else{
			@SuppressWarnings("unused")
            String fatherpk = node.getData().getPrimaryKey();
			this.getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		}	
     }

	@Override
	public String getRefBillType() {
		return null;
	}

    @Override
	protected IChildMenuController createChildMenuController() {
        return super.createChildMenuController();
    }

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return super.createBusinessDelegator();
	}
	
	@Override
	protected UITree getBillTree() {
		return super.getBillTree();
	}

	@Override
	protected BillTableCreateTreeTableTool getBillTableTreeData() {
		return super.getBillTableTreeData();
	}
    
    @Override
	public Object getUserObject() {
        return new ClientUICheckRuleGetter();
    }

}