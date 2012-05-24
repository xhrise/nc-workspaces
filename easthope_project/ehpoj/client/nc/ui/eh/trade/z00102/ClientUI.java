
package nc.ui.eh.trade.z00102;

import nc.bs.eh.trade.z00102.ClientUICheckRuleGetter;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IChildMenuController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.BillTableCreateTreeTableTool;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 功能：存货分类
 * @author 张起源
 * 日期：2008-4-24
 */

public class ClientUI extends BillTreeCardUI{

	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new ClientManageData();
	}

	@Override
	public void afterInit() throws java.lang.Exception{
		modifyRootNodeShowName("存货分类");
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
		nc.ui.trade.pub.VOTreeNode node = getBillTreeSelectNode();
		if(node==null ){
			this.getBillCardPanel().setHeadItem("pk_father", null);
			this.getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		}else{
			String fatherpk = node.getData().getPrimaryKey();
			this.getBillCardPanel().setHeadItem("pk_father",fatherpk);
			this.getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		}
     }
	
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey = e.getKey();
		if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
            getBillCardPanel().execHeadFormulas(formual);

        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
		super.afterEdit(e);
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
