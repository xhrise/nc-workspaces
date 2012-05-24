
package nc.ui.eh.trade.z0207001;

import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

public class ClientUI extends BillManageUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
    public ManageEventHandler createEventHandler() {
    	return new AbstractSPEventHandler(this, this.getUIControl());
    }
	
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {

	}

	
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo, int intRow) throws Exception {
	
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {

	}

	@Override
	protected void initSelfData() {
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
		 getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);

	}

	@Override
	public void setDefaultData() throws Exception {
		//表头设置退货单号
		ClientCtrl a = new ClientCtrl();
		String no = a.getBillType();
        BillCodeObjValueVO objVO = new BillCodeObjValueVO();
        objVO.setAttributeValue(no, getUIControl().getBillType());
        String billNo = BillcodeRuleBO_Client.getBillCode(no, _getCorp().getPrimaryKey(), null,
                                                          objVO);
        getBillCardPanel().setHeadItem("billno",billNo);  
		
		//在表头设置公司
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		
		getBillCardPanel().setTailItem("coperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setHeadItem("ladingdate",_getDate());
		
	}
	
	protected String getBodyWherePart() {
        return " pk_corp='" + _getCorp().getPk_corp() + "' order by dealtype ";
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
		
		//把表体更新的加工费给合计,放到表头的加工费总额中
		if(strKey.equals("jgf")){
            int rows=getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);
            for(int i=0;i<rows;i++){
                UFDouble jgf=new UFDouble(getBillCardPanel().getBodyValueAt(i,"jgf")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"jgf").toString());
                ze=ze.add(jgf);
            }
            getBillCardPanel().setHeadItem("jgfze",ze);   
        }
		super.afterEdit(e);
	}
	
}