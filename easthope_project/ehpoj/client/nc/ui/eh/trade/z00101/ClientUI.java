
package nc.ui.eh.trade.z00101;

import nc.bs.eh.trade.z00101.ClientUICheckRuleGetter;
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
 * 功能：片区管理
 * @author 张起源
 * 日期：2008-3-25
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
		modifyRootNodeShowName("片区");
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
			this.getBillCardPanel().setHeadItem("fatherpk", null);
			this.getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		}else{
			String fatherpk = node.getData().getPrimaryKey();
			this.getBillCardPanel().setHeadItem("fatherpk", fatherpk);
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
	public UITree getBillTree() {
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
