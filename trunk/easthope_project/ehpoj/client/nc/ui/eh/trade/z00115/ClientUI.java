/**
 * 客商可销料营销代表维护
 * @author 王明
 * 创建日期 2008-4-1 16:09:43
 */
package nc.ui.eh.trade.z00115;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.eh.trade.z00115.ClientUICheckRuleGetter;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractMultiChildClientUI;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;


@SuppressWarnings("serial")
public class ClientUI extends AbstractMultiChildClientUI {
	
	
	public ClientUI() {
		super();
        initvar();
	}
	
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBaseBD();
	}
	@Override
	protected void initSelfData() {
		//加入下拉选项
		getBillCardWrapper().initHeadComboBox("custprop", ICombobox.STR_custprop,true);
		getBillListWrapper().initHeadComboBox("custprop", ICombobox.STR_custprop,true);		
        //供应商性质
        getBillCardWrapper().initHeadComboBox("gysxz", ICombobox.Gys_xz, true);
        getBillListWrapper().initHeadComboBox("gysxz",ICombobox.Gys_xz,true);
	}
	@Override
	public void setDefaultData() throws Exception {	
		super.setDefaultData();
		getBillCardPanel().setTailItem("editcoperid", _getOperator());
		getBillCardPanel().setTailItem("editdate", _getDate());
	}
	@Override
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
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

     private void initvar(){
           try {
            UIRefPane ref=(UIRefPane)getBillCardPanel().getBillModel("kxl").getItemByKey("vcode").getComponent();
            ref.setMultiSelectedEnabled(true);
            ref.setTreeGridNodeMultiSelected(true);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
        }
     //参照修改
    
    @Override
    public boolean beforeEdit(BillEditEvent e) {
    	String tablename=getBillCardPanel().getCurrentBodyTableCode();
        if(e.getKey().equals("vcode")&&tablename.equals("kxl")){
        	String pk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?null:
				getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
        	if(pk_cubasdoc==null||pk_cubasdoc.length()==0){
        		showErrorMessage("请先选择客商!");
        		return false;
        	}
        	UIRefPane ref=(UIRefPane)getBillCardPanel().getBillModel("kxl").getItemByKey("vcode").getComponent();
        	ref.setPKs(null);
        }
    	return super.beforeEdit(e);
    }
    
     @SuppressWarnings("unchecked")
    @Override
    public void afterEdit(BillEditEvent e) {
        super.afterEdit(e);
        String tablename=getBillCardPanel().getCurrentBodyTableCode();
        if(e.getKey().equals("vcode")&&tablename.equals("kxl")){
        	String pk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?null:
				getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
        	if(pk_cubasdoc==null||pk_cubasdoc.length()==0){
        		showErrorMessage("请先选择客商!");
        		return;
        	}
            UIRefPane ref=(UIRefPane)getBillCardPanel().getBillModel("kxl").getItemByKey("vcode").getComponent();
            String[] dd=ref.getRefPKs();
            int returncode=ref.getReturnButtonCode();
            if(returncode==UIDialog.ID_OK&&dd!=null&&dd.length>0){
                int selectedRow=getBillCardPanel().getBillTable().getSelectedRow();
                int rows=getBillCardPanel().getRowCount();
                
                ArrayList arr=new ArrayList();
                for(int i=0;i<rows;i++){
                    if(i!=selectedRow){
                        String pk_invbasdoc=getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc")==null?"":
                            getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc").toString();
                        arr.add(pk_invbasdoc);
                    }
                }
                for(int i=0;i<dd.length;i++){
                    String pk_invbasdoc=dd[i];
                    arr.add(selectedRow++,pk_invbasdoc);
                }
//                getBillCardPanel().getBillModel().clearBodyData();
                int rowcount=getBillCardPanel().getRowCount();
                int[] rowss=new int[rowcount];
                for(int i=rowcount - 1;i>=0;i--){
                    rowss[i]=i;
                }
                getBillCardPanel().getBillModel().delLine(rowss);
                
                for(int i=0;i<arr.size();i++){
                    String pk_invbasdoc=arr.get(i).toString();
                    getBillCardPanel().getBillModel().addLine();
                    getBillCardPanel().setBodyValueAt(pk_invbasdoc, i, "pk_invbasdoc");
                    getBillCardPanel().setBodyValueAt(pk_cubasdoc, i, "pk_cubasdoc");
                    getBillCardPanel().getBillModel("kxl").execEditFormulaByKey(i, "vcode");
                }
            }
 
    	}
        //客商编码
        if(e.getKey().equals("pk_areacl")&&e.getPos()==HEAD){
            UIRefPane ref=(UIRefPane)getBillCardPanel().getHeadItem("pk_areacl").getComponent();
            String refcode=ref.getRefCode();
            if(refcode!=null&&refcode.length()>0){
            	if(refcode.equals("999")){
            		getBillCardPanel().getHeadItem("custcode").setEnabled(true);
            	}
            	String pk_corp = _getCorp().getPk_corp();
            	int length = refcode.length();
                StringBuffer sql=new StringBuffer("")
                .append("  SELECT CASE WHEN a.zccode IS NULL THEN b.oldcode ")
                .append("              ELSE a.zccode ")
                .append("         END custcode")
                .append("   FROM ")
                .append("  (SELECT MAX(custcode) zccode FROM bd_cubasdoc WHERE custcode LIKE  '"+refcode+"%' and pk_corp = '"+pk_corp+"' AND LEN(custcode) = "+length+"+4) a,")
                .append("  (SELECT MAX(custcode) oldcode FROM bd_cubasdoc WHERE custcode LIKE  '"+refcode+"%' and pk_corp = '"+pk_corp+"' AND LEN(custcode) = "+length+"+3) b");

                IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                try {
                    HashMap hm=(HashMap)iUAPQueryBS.executeQuery(sql.toString(),new MapProcessor());
                    String code="";
                    DecimalFormat df = new DecimalFormat("0000");
    
                    if(hm.get("custcode")!=null){
                        String custcode=hm.get("custcode").toString().trim();
                        code=refcode+df.format((Integer.parseInt(custcode.substring(custcode.length()-4))+1));
                    }else{
                        code=refcode+"0001";
                    }
                    getBillCardPanel().setHeadItem("custcode",code);
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                } catch (BusinessException e1) {
                    e1.printStackTrace();
                }      
            }else{
                getBillCardPanel().setHeadItem("custcode",null);
            }
            updateUI();
        }
        
        if(e.getKey().equals("pk_custleve")&&e.getPos()==HEAD){
            UIRefPane ref=(UIRefPane)getBillCardPanel().getHeadItem("pk_custleve").getComponent();
            String refname=ref.getRefName();
            if(!refname.equals("一级客户")){
                getBillCardPanel().getHeadItem("def12").setEnabled(true);
            }else{
                getBillCardPanel().getHeadItem("def12").setEnabled(false);
                getBillCardPanel().getHeadItem("def12").setValue(null);
            }           
        }
        
        if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(e.getKey()).getEditFormulas();//获取编辑公式
            getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(e.getKey()).getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
        
            
        }
     @Override
	protected void initPrivateButton() {
    	 nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"上一页","上一页");
    	 btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnPrev);
         nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"下一页","下一页");
         btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnNext);
         nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"停用","停用");
         btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btn1);
         nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.THAW,"启用","启用");
         btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btn2);
         nc.vo.trade.button.ButtonVO btn3 = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"营销代表批量修改","营销代表批量修改");
         btn3.setOperateStatus(new int[]{IBillOperate.OP_ALL});
         addPrivateButton(btn3);
         nc.vo.trade.button.ButtonVO btn4 = ButtonFactory.createButtonVO(IEHButton.DOCMANAGE,"片区批量修改","片区批量修改");
         btn4.setOperateStatus(new int[]{IBillOperate.OP_ALL});
         addPrivateButton(btn4);
    	super.initPrivateButton();
    }
    
}