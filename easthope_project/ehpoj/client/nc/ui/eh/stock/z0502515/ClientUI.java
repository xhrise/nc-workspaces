package nc.ui.eh.stock.z0502515;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：原料退货通知单
 * @author 王兵
 * 2008-7-24 10:35:03
 */
public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {	
        getBillCardPanel().setHeadItem("receiptdate", _getDate()); //初始化收货日期
		super.setDefaultData();
	}
    
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
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
        
//        if(e.getPos()==BODY && strKey.equals("invcode")){ //选择物料把含税单价给带出来
//            int row = getBillCardPanel().getBillTable().getRowCount();
//            for(int i=0;i<row;i++){
//                String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
//                    getBillCardWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
//                IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());                 
//                String sql = " select price from eh_invbasdoc where pk_invbasdoc='"+pk_invbasdoc+"' and isnull(dr,0)=0 ";
//                UFDouble price = null;
//                try {
//                    ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
//                    if(all!=null && all.size()>0){
//                        for(int j=0;j<all.size();j++){
//                            HashMap hm = (HashMap) all.get(j);
//                            price = new UFDouble(hm.get("price")==null?"0":hm.get("price").toString());
//                        }
//                    }
//                } catch (BusinessException e1) {
//                    e1.printStackTrace();
//                }     
//                getBillCardPanel().setBodyValueAt(price, i, "taxinprice");
//            }
//
//        }
        
        if(strKey.equals("fye")){
            int row = getBillCardPanel().getBillTable().getRowCount();
            UFDouble yfeze=new UFDouble(0);
            UFDouble taxmoneyze=new UFDouble(0);
            for(int i=0;i<row;i++){
                UFDouble weight = new UFDouble(getBillCardPanel().getBodyValueAt(i, "weight")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "weight").toString()); //数量
                UFDouble yfe = new UFDouble(getBillCardPanel().getBodyValueAt(i, "fye")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "fye").toString()); //退货费
                UFDouble taxmoney = new UFDouble(getBillCardPanel().getBodyValueAt(i, "taxmoney")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "taxmoney").toString()); //金额
                yfeze=yfeze.add(weight.multiply(yfe));
                taxmoneyze=taxmoneyze.add(taxmoney);
            }
            getBillCardPanel().setHeadItem("summoney",taxmoneyze.add(yfeze));
            getBillCardPanel().setHeadItem("fymoney",yfeze);// 应退总额
        }
        
        super.afterEdit(e);
    }

}
