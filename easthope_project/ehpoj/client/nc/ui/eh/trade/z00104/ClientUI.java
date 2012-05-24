
package nc.ui.eh.trade.z00104;

import java.text.DecimalFormat;
import java.util.HashMap;

import nc.bs.eh.trade.z00104.ClientUICheckRuleGetter;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.eh.pub.ICombobox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class ClientUI extends BillManageUI {

	@Override
	public AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
	

	@Override
	protected void initSelfData() {
	    //仓库大类
        getBillCardWrapper().initHeadComboBox("is_flag", ICombobox.STR_IS_FLAG,true);
        getBillListWrapper().initHeadComboBox("is_flag", ICombobox.STR_IS_FLAG,true);

	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());

	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey=e.getKey();
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		
		 if(e.getPos()==HEAD){
	            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
	            getBillCardPanel().execHeadFormulas(formual);
	        }else if (e.getPos()==BODY){
	            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
	            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
	        }else{
	            getBillCardPanel().execTailEditFormulas();
	        }
		 //编码的维护(流水号+地区号)
		 if(strKey.equals("pk_storetype")){
	        UIRefPane ref=(UIRefPane)getBillCardPanel().getHeadItem("pk_storetype").getComponent();
	        String refcode=ref.getRefCode();
	        if(refcode!=null){
	            String sql="select max(storcode) storcode from eh_stordoc where pk_corp='"+_getCorp().getPrimaryKey()+"' and " +
		 		" storcode like '"+refcode+"%'and  isnull(dr,0)=0";
	            try {
	                HashMap hm=(HashMap)iUAPQueryBS.executeQuery(sql,new MapProcessor());
	                String code="";
	                DecimalFormat df = new DecimalFormat("000");
	
	                if(hm.get("storcode")!=null){
	                    String storcode=hm.get("storcode").toString().trim();
	                    code=refcode+df.format((Integer.parseInt(storcode.substring(storcode.length()-3))+1));
//	                    code=refcode+df.format((Integer.parseInt(storcode)+1));
	                }else{
	                    code=refcode+"001";
	                }
	                getBillCardPanel().setHeadItem("storcode",code);
	            } catch (NumberFormatException e1) {
	                e1.printStackTrace();
	            } catch (BusinessException e1) {
	                e1.printStackTrace();
	            }      
	        }else{
	            getBillCardPanel().setHeadItem("storcode",null);
	        }
	        updateUI(); 
		 }
		super.afterEdit(e);
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	
     protected String getBodyWherePart() {
            return " pk_corp='" + _getCorp().getPk_corp() + "' order by storcode ";
        }
	
     @Override
	public Object getUserObject() {
        return new ClientUICheckRuleGetter();
    }
 }
		
