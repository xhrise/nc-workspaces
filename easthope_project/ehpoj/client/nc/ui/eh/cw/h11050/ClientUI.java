/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.cw.h11050;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.refpub.DeptdocRefModel;
import nc.ui.eh.refpub.InvbasdocRefModel;
import nc.ui.eh.refpub.WorkshopRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * ˵��: �ɱ�����
 * @author ����
 * 2008-5-28 14:29:08
 */
public class ClientUI extends BillManageUI {

	
	UIRefPane ref = null;
	UIRefPane defaultref = null;     //��������ΪĬ�ϲ���
	public ClientUI() {
		super();
        initvar();
	}
	
	private void initvar(){
        try {
         ref=(UIRefPane)getBillCardPanel().getBodyItem("refcode").getComponent();
         defaultref = (UIRefPane)getBillCardPanel().getBodyItem("refcode").getComponent();
     } catch (Exception e) {
         e.printStackTrace();
     }
    }

	/**
	 * @param arg0
	 */
	public ClientUI(Boolean arg0) {
		super(arg0);
		// TODO �Զ����ɹ��캯�����
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
	{
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	/* ���� Javadoc��
	 * @see nc.ui.trade.manage.BillManageUI#createController()
	 */
	@Override
	protected AbstractManageController createController() {
		// TODO �Զ����ɷ������
		return new ClientCtrl();
	}

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	protected ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
	/* ���� Javadoc��
	 * @see nc.ui.trade.manage.BillManageUI#setBodySpecialData(nc.vo.pub.CircularlyAccessibleValueObject[])
	 */
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception {
		// TODO �Զ����ɷ������

	}

	/* ���� Javadoc��
	 * @see nc.ui.trade.manage.BillManageUI#setHeadSpecialData(nc.vo.pub.CircularlyAccessibleValueObject, int)
	 */
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception {
		// TODO �Զ����ɷ������

	}

	/* ���� Javadoc��
	 * @see nc.ui.trade.manage.BillManageUI#setTotalHeadSpecialData(nc.vo.pub.CircularlyAccessibleValueObject[])
	 */
	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception {
		// TODO �Զ����ɷ������

	}
	
	/* ���� Javadoc��
	 * @see nc.ui.trade.base.AbstractBillUI#setDefaultData()
	 */
	@Override
	public void setDefaultData() throws Exception {
//		��ͷ���õ�
        String pk_corp = _getCorp().getPrimaryKey();
        String billNo = BillcodeRuleBO_Client.getBillCode(getUIControl().getBillType(), pk_corp,null, null);
        getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
        getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
        getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);
        getBillCardPanel().getHeadItem("costobjcode").setValue(billNo);
	}

	/*
	 * ���� Javadoc��;
	 * 
	 * @see nc.ui.trade.base.AbstractBillUI#initSelfData()
	 */
	@Override
	protected void initSelfData(){
		getBillCardWrapper().initBodyComboBox("objtype",ICombobox.STR_OBJTYPE, true);
		getBillListWrapper().initBodyComboBox("objtype",ICombobox.STR_OBJTYPE, true);
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String strKey=e.getKey();
		if(strKey.equals("refcode")){
			int row=getBillCardPanel().getBillTable().getSelectedRow();
			String objtype = getBillCardPanel().getBodyValueAt(row,"objtype")==null?null:
                             getBillCardPanel().getBodyValueAt(row,"objtype").toString();               //��������
        	if(objtype==null){
        		showErrorMessage("����ѡ���������!");
        	}
        	else{
        		changeRefModel(objtype);
        	}
		}
		return super.beforeEdit(e);
	}
//	@Override
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey=e.getKey();
		if(strKey.equals("objtype")){       // ѡ��������ͺ�ı����
        	int row=getBillCardPanel().getBillTable().getSelectedRow();
        	getBillCardPanel().setBodyValueAt("", row, "refcode");
        	getBillCardPanel().setBodyValueAt("", row, "refname");
        	getBillCardPanel().setBodyValueAt("", row, "refpk");
        	String objtype = getBillCardPanel().getBodyValueAt(row,"objtype")==null?"":
                getBillCardPanel().getBodyValueAt(row,"objtype").toString();               //��������
        	changeRefModel(objtype);
	    }
		if(strKey.equals("refcode")){
			String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
		}
		if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execHeadFormulas(formual);
        }
		super.afterEdit(e);
	}
	
	public void changeRefModel(String objtype){
		String fumual = null;
		if(objtype.equals("��Ʒ")){         // ��Ʒ
    		InvbasdocRefModel invRefModel = new InvbasdocRefModel();
    		ref.setRefModel(invRefModel);
    		fumual = "refname->getColValue(bd_invbasdoc, invname, pk_invbasdoc," +
    				"getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,refpk));";
			getBillCardPanel().getBodyItem("refcode").setEditFormula(new String[]{fumual});
    	}
    	if(objtype.equals("����")){         // ����
    		WorkshopRefModel workRefModel = new WorkshopRefModel();
    		ref.setRefModel(workRefModel);
    		fumual = "refname->getColValue(eh_workshop, workshopname, pk_contracttype, refpk);";
			getBillCardPanel().getBodyItem("refcode").setEditFormula(new String[]{fumual});
    	}
    	if(objtype.equals("����")){         // ����
    		DeptdocRefModel deptRefModel = new DeptdocRefModel();
    		ref.setRefModel(deptRefModel);
    		fumual = "refname->getColValue(bd_deptdoc, deptname,pk_deptdoc , refpk);";
			getBillCardPanel().getBodyItem("refcode").setEditFormula(new String[]{fumual});
    	}
	}

	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"��һҳ","��һҳ");
   	 	btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnPrev);
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"��һҳ","��һҳ");
        btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnNext);
        super.initPrivateButton();
    }
}

   
	

