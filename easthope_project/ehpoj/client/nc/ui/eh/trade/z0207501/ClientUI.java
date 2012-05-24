package nc.ui.eh.trade.z0207501;

import nc.bs.eh.trade.z0207501.ClientUICheckRuleGetter;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**销售发票
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientUI extends AbstractClientUI {
	public static UFDate makedate=null;
	public static String billTyped=null;
	public static BillCodeObjValueVO objVOd=null;
	public static String corp=null;
	

    public ClientUI() {   	
        super();
        makedate=_getDate();
        billTyped=getUIControl().getBillType(); 	
        objVOd = new BillCodeObjValueVO();
        objVOd.setAttributeValue(billTyped, getUIControl().getBillType());
        corp=_getCorp().getPrimaryKey();
        ButtonObject[] buttons = getButtonManager().getButton(IBillButton.Busitype).getChildButtonGroup();
        getButtonManager().getButton(IBillButton.Busitype).setChildButtonGroup(new ButtonObject[]{buttons[0]});
    }
   
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

 
    @Override
	protected AbstractManageController createController() {
        return new ClientCtrl();
    }

    @Override
	public  ManageEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }

    @Override
	public void setDefaultData() throws Exception { 
        //表头设置单
    	super.setDefaultData();
        
        getBillCardPanel().setHeadItem("def_1", "N");  //add by zqy 
        
        getBillCardPanel().setHeadItem("invoicedate", _getDate());  	
        getBillCardPanel().execHeadEditFormulas();
    }

    @Override
	protected void initSelfData(){
    	super.initSelfData();
         getBillCardWrapper().initHeadComboBox("inouttype", ICombobox.STR_invice,true);
         getBillListWrapper().initHeadComboBox("inouttype", ICombobox.STR_invice,true);
         getBillCardWrapper().initHeadComboBox("invoicetype", ICombobox.STR_invice,true);
         getBillListWrapper().initHeadComboBox("invoicetype", ICombobox.STR_invice,true);
    	
    }
	@Override
	public void afterEdit(BillEditEvent e) {
		//对公式的编辑
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
		//对数据增加的处理
		if(strKey.equals("amount")||strKey.equals("price")){
            int rows=getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);
            for(int i=0;i<rows;i++){
                UFDouble jgf=new UFDouble(getBillCardPanel().getBodyValueAt(i,"hsprice")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"hsprice").toString());
                ze=ze.add(jgf);
            }
            getBillCardPanel().setHeadItem("totalprice",ze);
            
        }
		super.afterEdit(e);
	}

	@Override
	public Object getUserObject() {

		return new ClientUICheckRuleGetter();
	}

 
    @Override
    protected void initPrivateButton() {
    	nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn);
    	super.initPrivateButton();
    }

}

   
    

