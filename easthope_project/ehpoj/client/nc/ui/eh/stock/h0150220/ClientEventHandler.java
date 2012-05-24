package nc.ui.eh.stock.h0150220;

/**
 * 采购决策
 * ZB19	
 * @author wangbing
 * 2008-12-29 8:57:25
 */

import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.h0150220.StockDecisionVO;
import nc.vo.pub.lang.UFDouble;

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	
	@Override
	public void onBoSave() throws Exception {
     	//当是大众原料采购决策时,包装物标签页签不需填数据
     	StockDecisionVO hvo = (StockDecisionVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
     	int invtype = hvo.getInvtype();			//0 原料 1 标签包装
     	if(invtype==0){
     		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
     	}else{						//在标签包装物时对洽谈记录页签值进行非空校验
     		BillItem[] headitems = getBillCardPanelWrapper().getBillCardPanel().getHeadItems();
     		StringBuffer errMessage = new StringBuffer();
        	for(int i=0;i<headitems.length;i++){
            	String tablecode = headitems[i].getTableCode();
            	Object value = headitems[i].getValueObject();
            	boolean canNull = headitems[i].isNull();
            	if(tablecode.equals("qtjl")&&(canNull&&(value==null||value.equals("")))){
            		errMessage.append("["+headitems[i].getName()+"] ");
            	}
            }
        	if(errMessage.toString().length()>0){
        		getBillUI().showErrorMessage("表头<洽谈记录>中"+errMessage.toString()+"不能为空!");
        		return;
        	}
     	}
     	int row1 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_b").getRowCount();
     	int row2 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_c").getRowCount();
     	int row3 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_d").getRowCount();
     	int row4 = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_e").getRowCount();
     	String[] invtypes = ICombobox.CG_DECISION;
     	if(invtype==0){
     		if(row4>0){
     			getBillUI().showErrorMessage("在 "+invtypes[0]+" 采购决策时不需填<包装物标签>页签中数据!");
     			return;
     		}
     		if(row1<=0){
     			getBillUI().showErrorMessage("在 "+invtypes[0]+" 采购决策时必须填入<最优采购点>页签数据!");
     			return;
     		}
     		if(row2<=0){
     			getBillUI().showErrorMessage("在 "+invtypes[0]+" 采购决策时必须填入<集团比价>页签数据!");
     			return;
     		}
     		if(row3<=0){
     			getBillUI().showErrorMessage("在 "+invtypes[0]+" 采购决策时必须填入<其他供应商比价>页签数据!");
     			return;
     		}
     	}
     	if(invtype==1){
     		if(row4<=0){
     			getBillUI().showErrorMessage("在 "+invtypes[1]+" 采购决策必须填<包装物标签>页签中数据!");
     			return;
     		}
     		if(row1>0||row2>0||row3>0){
     			getBillUI().showErrorMessage("在 "+invtypes[1]+" 采购决策只须填<包装物标签>页签中数据!");
     			return;
     		}
     	}
     	//前台校验    功能：取消重复判断（原因：询价单中采购点和供应商信息相同时不允许保存）时间：2009-12-15作者：张志远
//		BillModel bm1=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_b");
//     	int res1=new PubTools().uniqueCheck(bm1, new String[]{"pk_areal"});
//     	if(res1==1){
//             getBillUI().showErrorMessage("<最优采购点>页签有重复数据,不允许操作!");
//             return;
//     	}
     	
     	BillModel bm2=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_c");
     	int res2=new PubTools().uniqueCheck(bm2, new String[]{"bjcorpname"});
     	if(res2==1){
             getBillUI().showErrorMessage("<集团比价>页签有重复数据,不允许操作!");
             return;
     	}
     	
     	BillModel bm3=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_d");
     	int res3=new PubTools().uniqueCheck(bm3, new String[]{"pk_cubasdoc","qzcust"});
     	if(res3==1){
             getBillUI().showErrorMessage("<其他供应商比价>页签有重复数据,不允许操作!");
             return;
     	}
     	
     	UFDouble cjkcuseday = hvo.getCjkcuseday()==null?new UFDouble("0"):hvo.getCjkcuseday();		//差距库存使用天数>5
     	UFDouble cgcguseday = hvo.getCgcguseday()==null?new UFDouble("0"):hvo.getCgcguseday();		//采购使用天数>0
     	String cgreason = hvo.getCgreason();		//差距原因
     	String improve = hvo.getImprove();			//实施改善
     	if((cjkcuseday.toDouble()>5||cgcguseday.toDouble()>0)&&(cgreason==null||cgreason.length()==0||improve==null||improve.length()==0)){
     		getBillUI().showErrorMessage("库存使用天数差值＞5天或本次采购使用天数差值＞0时必须填写差距原因及实施改善!");
     		return;
     	}
     	super.onBoSave2_whitBillno();
	}
	
	
    
    //设置多页签打印 add by zqy
    @Override
    protected void onBoPrint() throws Exception {
        nc.ui.pub.print.IDataSource dataSource = new ClientCardPanelPRTS(getBillUI()
                ._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),getUIController().getPkField());
        nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
                dataSource);
        print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
                ._getModuleCode(), getBillUI()._getOperator(), getBillUI()
                .getBusinessType(), getBillUI().getNodeKey());
        print.selectTemplate();
        print.preview();
    }
   
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    	String  invtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("invtype").getValueObject()==null?null:
    							getBillCardPanelWrapper().getBillCardPanel().getHeadItem("invtype").getValueObject().toString();
    	BillItem[] headitems = getBillCardPanelWrapper().getBillCardPanel().getHeadItems();
    	for(int i=0;i<headitems.length;i++){
        	String name = headitems[i].getKey();
        	boolean isEnabled = headitems[i].isEdit();
        	String tablecode = headitems[i].getTableCode();
        	if(invtype.equals("0")&&!name.endsWith("invtype")&&isEnabled&&!tablecode.equals("qtjl")){
        		headitems[i].setEnabled(true);
        	}
        	if(invtype.equals("1")&&!name.endsWith("invtype")&&isEnabled&&!tablecode.equals("qtjl")){
        		headitems[i].setEnabled(false);
        	}
        }
    }
}
