package nc.ui.eh.sc.h0451005;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.sc.h0451005.ScPosmBVO;
import nc.vo.eh.sc.h0451005.ScPosmVO;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵������������
 * @author ����
 * 2008-05-07 ����04:03:18
 * ��Ϊ �������� edit by wb 2009-2-7 15:29:12
 */


public class ClientEventHandler extends AbstractEventHandler {
	
	public static ScPosmBVO[] eidtBVO = null;
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn){
         case IEHButton.LOCKBILL:    //�رյ���
             onBoLockBill();
             break;
		}
		super.onBoElse(intBtn);
	}
	
    @Override
	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
    	//�Էǿ���֤
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//���Ƶ�ʱ��ǰ̨У��
		String vsourcebilltype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype")==null?"":
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject().toString();
		if(vsourcebilltype.equals("")){
	        BillModel bm = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
	        int res = new PubTools().uniqueCheck(bm, new String[] { "pk_invbasdoc"});
	        if (res == 1) {
	            getBillUI().showErrorMessage("��Ʒ���ظ��������������");
	            return;
	        }
		}
		super.onBoSave();
		ScPosmVO re=(ScPosmVO) (getBillCardPanelWrapper().getBillVOFromUI()).getParentVO();
		String vsourcebillid=re.getVsourcebillid()==null?"":re.getVsourcebillid().toString();
		if(!vsourcebillid.equals("")){		//�ر�����MRP����
			String updateSql = " update eh_sc_mrp set sc_flag = 'Y' where pk_mrp in ("+vsourcebillid+")";
			PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			pubItf.updateSQL(updateSql);
		}
		
	}
	 
	// add by zqy 2008-5-20 11:25:07
    @Override
	public void onButton_N(ButtonObject bo, BillModel model) {
        super.onButton_N(bo, model);
        String bocode = bo.getCode();
        // �������۶���������������ʱ���������ڲ����޸�
        if(bocode.equals("���۶���")) {
        	HashMap hm = new PubTools().getInvSafeKC(null);		//��ȫ����� 
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scdate").setEnabled(false);
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for (int i = 0; i < row; i++) {
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "vinvcode", false);
                //���ϰ�ȫ��� add by wb at 2008-10-21 13:34:48
                String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
                							getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
                UFDouble safekc =  new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
                UFDouble scamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "scmount")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "scmount").toString());		//������������ = ��������-����������
                UFDouble truekc = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kc")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kc").toString());			//ʵ�ʿ��
                UFDouble scrwamount = scamount.sub(truekc).add(safekc);				//������������=��������-ʵ�ʿ����+��ȫ�����
                //���������������<�㣬������������ȡ�㲻�������������޸ģ�������������������㣬������������ȡ�����������������޸�
                if(scrwamount.toDouble()<0){
                	scamount = new UFDouble(0);
                	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("����Ҫ��������", i, "memo");
                }
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(safekc, i, "safekc");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(scamount, i, "scmount");
            } 
        }
        /****************************���Ӵ�MRP->��������(����)����**************************************************/
        if(bocode.equals("MRP����")) {
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scdate").setEnabled(false);
        }
        getBillUI().updateUI();
    }
    
    @Override
    protected void onBoDelete() throws Exception {
    	int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
    	if(res==0){
    		return;
    	}
    	ScPosmVO re=(ScPosmVO) (getBillCardPanelWrapper().getBillVOFromUI()).getParentVO();
		String vsourcebillid=re.getVsourcebillid()==null?"":re.getVsourcebillid().toString();
		if(!vsourcebillid.equals("")){		//��������MRP����
			String updateSql = " update eh_sc_mrp set sc_flag = 'N' where pk_mrp in ("+vsourcebillid+")";
			PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			pubItf.updateSQL(updateSql);
		}
    	super.onBoTrueDelete();
    	
	}
    
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    }
    
    
}
