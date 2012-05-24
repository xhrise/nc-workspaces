
package nc.ui.eh.sc.h0470515;

import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IChildMenuController;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.BillTableCreateTreeTableTool;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 功能：设备档案
 * ZB27
 * @author 王兵
 * 2009-1-9 15:28:11
 */
public class ClientUI extends BillTreeManageUI{

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
		modifyRootNodeShowName("设备档案");
	}
	
	 @Override
	protected AbstractManageController createController() {
	        return new ClientCtrl();
	    }
	    
	 @Override
	protected ManageEventHandler createEventHandler() {
	        return new ClientEventHandler(this,this.getUIControl());
	 }

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
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
		BillItem oper = getBillCardPanel().getTailItem("coperatorid");
        if (oper != null)
            oper.setValue(_getOperator());
        else
            getBillCardPanel().getHeadItem("coperatorid").setValue(_getOperator());
        BillItem date = getBillCardPanel().getTailItem("dmakedate");
        if (date != null)
            date.setValue(_getDate());
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
		return null;
	}
	

}
