
package nc.ui.eh.cw.h10112;

import java.util.Vector;

import nc.bs.eh.cw.h10112.ClientUICheckRuleGetter;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明: 电费基础
 * @author zqy
 * 时间：2008-9-10 14:58:09
 */

public class ClientUI extends BillManageUI {

	public ClientUI() {
		super();
	}

	public ClientUI(Boolean arg0) {
		super(arg0);
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId){
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
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
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception {
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception {
	}

	@Override
	public void setDefaultData() throws Exception {
	    //表头设置单
        String pk_corp = _getCorp().getPrimaryKey();
        getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);
	}

	@Override
	protected void initSelfData(){
        getBillCardWrapper().initBodyComboBox("invtype", ICombobox.STR_INVTYPE,true);
        getBillListWrapper().initBodyComboBox("invtype", ICombobox.STR_INVTYPE,true);
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
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
        
        if(strKey.equals("pk_period")){
            String pk_period =getBillCardPanel().getHeadItem("pk_period").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_period").getValueObject().toString();
            String pk_corp = _getCorp().getPk_corp();
            StringBuffer sql = new StringBuffer()
            .append(" select nyear,nmonth from eh_period where pk_period ='"+pk_period+"' and pk_corp ='"+pk_corp+"' and isnull(dr,0)=0 ");
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            String nyear = null;
            String nmonth = null;
            try {
                Vector vector = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
                if(vector!=null && vector.size()>0){
                    Vector ve =(Vector) vector.get(0);
                    nyear = ve.get(0)==null?"":ve.get(0).toString();
                    nmonth = ve.get(1)==null?"":ve.get(1).toString();
                }                
            } catch (BusinessException e1) {
                e1.printStackTrace();
            }
            getBillCardPanel().setHeadItem("nyear", nyear);
            getBillCardPanel().setHeadItem("nmonth", nmonth);
            
        }
        
        
        if(strKey.equals("elecamount") || strKey.equals("elecprice")){
            int row = getBillCardPanel().getBillTable().getSelectedRow();
            UFDouble elecamount = new UFDouble(getBillCardPanel().getBodyValueAt(row, "elecamount")==null?"0":getBillCardPanel()
                        .getBodyValueAt(row, "elecamount").toString());
            UFDouble elecprice = new UFDouble(getBillCardPanel().getBodyValueAt(row, "elecprice")==null?"0":getBillCardPanel()
                        .getBodyValueAt(row, "elecprice").toString());
            UFDouble totalprice = elecprice.multiply(elecamount);
            getBillCardPanel().setBodyValueAt(totalprice, row, "totalprice");
        }
          
        super.afterEdit(e);
    }
    
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"上一页","上一页");
   	 	btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnPrev);
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"下一页","下一页");
        btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnNext);
        super.initPrivateButton();
    }
    
    @Override
	public Object getUserObject() {
        return new ClientUICheckRuleGetter();
    }
    
}

   
	

