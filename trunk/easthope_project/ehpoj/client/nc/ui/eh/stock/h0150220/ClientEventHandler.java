package nc.ui.eh.stock.h0150220;

/**
 * �ɹ�����
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
     	//���Ǵ���ԭ�ϲɹ�����ʱ,��װ���ǩҳǩ����������
     	StockDecisionVO hvo = (StockDecisionVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
     	int invtype = hvo.getInvtype();			//0 ԭ�� 1 ��ǩ��װ
     	if(invtype==0){
     		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
     	}else{						//�ڱ�ǩ��װ��ʱ��Ǣ̸��¼ҳǩֵ���зǿ�У��
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
        		getBillUI().showErrorMessage("��ͷ<Ǣ̸��¼>��"+errMessage.toString()+"����Ϊ��!");
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
     			getBillUI().showErrorMessage("�� "+invtypes[0]+" �ɹ�����ʱ������<��װ���ǩ>ҳǩ������!");
     			return;
     		}
     		if(row1<=0){
     			getBillUI().showErrorMessage("�� "+invtypes[0]+" �ɹ�����ʱ��������<���Ųɹ���>ҳǩ����!");
     			return;
     		}
     		if(row2<=0){
     			getBillUI().showErrorMessage("�� "+invtypes[0]+" �ɹ�����ʱ��������<���űȼ�>ҳǩ����!");
     			return;
     		}
     		if(row3<=0){
     			getBillUI().showErrorMessage("�� "+invtypes[0]+" �ɹ�����ʱ��������<������Ӧ�̱ȼ�>ҳǩ����!");
     			return;
     		}
     	}
     	if(invtype==1){
     		if(row4<=0){
     			getBillUI().showErrorMessage("�� "+invtypes[1]+" �ɹ����߱�����<��װ���ǩ>ҳǩ������!");
     			return;
     		}
     		if(row1>0||row2>0||row3>0){
     			getBillUI().showErrorMessage("�� "+invtypes[1]+" �ɹ�����ֻ����<��װ���ǩ>ҳǩ������!");
     			return;
     		}
     	}
     	//ǰ̨У��    ���ܣ�ȡ���ظ��жϣ�ԭ��ѯ�۵��вɹ���͹�Ӧ����Ϣ��ͬʱ�������棩ʱ�䣺2009-12-15���ߣ���־Զ
//		BillModel bm1=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_b");
//     	int res1=new PubTools().uniqueCheck(bm1, new String[]{"pk_areal"});
//     	if(res1==1){
//             getBillUI().showErrorMessage("<���Ųɹ���>ҳǩ���ظ�����,���������!");
//             return;
//     	}
     	
     	BillModel bm2=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_c");
     	int res2=new PubTools().uniqueCheck(bm2, new String[]{"bjcorpname"});
     	if(res2==1){
             getBillUI().showErrorMessage("<���űȼ�>ҳǩ���ظ�����,���������!");
             return;
     	}
     	
     	BillModel bm3=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_decision_d");
     	int res3=new PubTools().uniqueCheck(bm3, new String[]{"pk_cubasdoc","qzcust"});
     	if(res3==1){
             getBillUI().showErrorMessage("<������Ӧ�̱ȼ�>ҳǩ���ظ�����,���������!");
             return;
     	}
     	
     	UFDouble cjkcuseday = hvo.getCjkcuseday()==null?new UFDouble("0"):hvo.getCjkcuseday();		//�����ʹ������>5
     	UFDouble cgcguseday = hvo.getCgcguseday()==null?new UFDouble("0"):hvo.getCgcguseday();		//�ɹ�ʹ������>0
     	String cgreason = hvo.getCgreason();		//���ԭ��
     	String improve = hvo.getImprove();			//ʵʩ����
     	if((cjkcuseday.toDouble()>5||cgcguseday.toDouble()>0)&&(cgreason==null||cgreason.length()==0||improve==null||improve.length()==0)){
     		getBillUI().showErrorMessage("���ʹ��������ֵ��5��򱾴βɹ�ʹ��������ֵ��0ʱ������д���ԭ��ʵʩ����!");
     		return;
     	}
     	super.onBoSave2_whitBillno();
	}
	
	
    
    //���ö�ҳǩ��ӡ add by zqy
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
