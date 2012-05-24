
package nc.ui.eh.trade.z00120;

import nc.bs.eh.trade.z00120.ClientUICheckRuleGetter;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.base.IChildMenuController;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.BillTableCreateTreeTableTool;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 功能：物料档案
 * @author 张起源
 * 日期：2008-3-25
 */
public class ClientUI extends BillTreeManageUI{

	private static final long serialVersionUID = 1L;

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
		modifyRootNodeShowName("物料类别");
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
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
		//物料大类
		getBillCardWrapper().initHeadComboBox("invgentype", ICombobox.STR_BIGTYPE,true);
		getBillListWrapper().initHeadComboBox("invgentype", ICombobox.STR_BIGTYPE,true);
	}
    
	@Override
	public void setDefaultData() throws Exception {
		//在表头设置公司
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		
		//取得制单日期
        getBillCardPanel().setTailItem("dmakedate",_getDate());
        getBillCardPanel().setTailItem("editdate",_getDate());
        
        //取得制单人名称
        getBillCardPanel().setTailItem("coperatorid",_getOperator());
        getBillCardPanel().setTailItem("editcoperid",_getOperator());
        
        nc.ui.trade.pub.VOTreeNode selectNode = this.getBillTreeSelectNode();
        if (selectNode != null) {
            nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) selectNode.getData();
           
			String pk_invcl = vo.getAttributeValue("pk_invcl").toString();
			getBillCardPanel().setHeadItem("pk_invcl", pk_invcl);
			String[] formual=getBillCardPanel().getHeadItem("pk_invcl").getEditFormulas();//获取编辑公式
	        getBillCardPanel().execHeadFormulas(formual);
        }
     }

	@Override
	public String getRefBillType() {
		return null;
	}
    
	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,getUIControl());
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
    
       @Override
	protected void initPrivateButton() {
             nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"上一页","上一页");
             btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btnPrev);
             nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"下一页","下一页");
             btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btnNext);
             nc.vo.trade.button.ButtonVO btnlock = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"停用","停用");
             btnlock.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btnlock);
            super.initPrivateButton();
        }
       
       @Override
	public void afterEdit(BillEditEvent e) {
           String strKey=e.getKey();

            if(e.getPos()==HEAD){
                   String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
                   getBillCardPanel().execHeadFormulas(formual);
               }else if (e.getPos()==BODY){
                   String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
                   getBillCardPanel().execBodyFormulas(e.getRow(),formual);
               }else{
                   getBillCardPanel().execTailEditFormulas();
               }
           super.afterEdit(e);
       }
}
