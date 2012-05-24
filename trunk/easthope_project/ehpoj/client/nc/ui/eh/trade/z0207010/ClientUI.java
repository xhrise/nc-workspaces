
package nc.ui.eh.trade.z0207010;
/**
 * ���� �����˻�
 */
import nc.bs.eh.trade.z0207010.ClientUICheckRuleGetter;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.refpub.SBthRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDouble;



public class ClientUI extends AbstractClientUI {

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
		super.initSelfData();
		//����Ĵ���ʽ�����˵�
		getBillCardWrapper().initBodyComboBox("dealtype", ICombobox.STR_dealtype,true);
		getBillListWrapper().initBodyComboBox("dealtype", ICombobox.STR_dealtype,true);

	}

	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
		getBillCardPanel().setHeadItem("ladingdate",_getDate());
		getBillCardPanel().execHeadEditFormulas();
		

	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey=e.getKey();
		
		 if(e.getPos()==HEAD){
	            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
	            getBillCardPanel().execHeadFormulas(formual);
	        }else if (e.getPos()==BODY){
	            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
	            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
	        }else{
	            getBillCardPanel().execTailEditFormulas();
	        }
		 
		//�ѱ�����µļӹ��Ѹ��ϼ�,�ŵ���ͷ�ļӹ����ܶ���
		if(strKey.equals("jgf")||strKey.equals("realbackamount")){
            int rows=getBillCardPanel().getBillTable().getRowCount();
            UFDouble jgfze=new UFDouble(0);
            UFDouble backpriceze=new UFDouble(0);
            for(int i=0;i<rows;i++){
            	UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"realbackamount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"realbackamount").toString());
            	UFDouble jgf=new UFDouble(getBillCardPanel().getBodyValueAt(i,"jgf")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"jgf").toString());
                UFDouble backprice=new UFDouble(getBillCardPanel().getBodyValueAt(i,"backprice")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"backprice").toString());
                jgfze=jgfze.add(amount.multiply(jgf));
                backpriceze=backpriceze.add(backprice);
            }
            getBillCardPanel().setHeadItem("jgfze",jgfze);
            getBillCardPanel().setHeadItem("def_6",backpriceze);// Ӧ���ܶ�
        }
        
		super.afterEdit(e);
	}
	 @Override
	    protected void initPrivateButton() {
		 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
	        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	        addPrivateButton(btn1);
	    	super.initPrivateButton();
	    }
	
	@Override
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String strKey=e.getKey();
		if(strKey.equals("pk_sbbill")){
            String pk_cubasdoc = pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();              // �ͻ�
            SBthRefModel.pk_cubasdoc = pk_cubasdoc;
        }
		return super.beforeEdit(e);
	}
	
	
		
}