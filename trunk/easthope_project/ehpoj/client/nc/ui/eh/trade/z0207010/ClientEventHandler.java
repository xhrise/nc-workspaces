package nc.ui.eh.trade.z0207010;
/**
 * ���� �����˻�
 */
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0207010.BackbillVO;
import nc.vo.eh.trade.z0207010.BackbillBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn){
         case IEHButton.LOCKBILL:    //�رյ���
//             onBoLockBill();
             break;
		}
		super.onBoElse(intBtn);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
        //�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        //���۷�Ʊ��
		Object pk_invoice = this.getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getAttributeValue("pk_invoice");
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < row; i++) {
            UFDouble realbackamount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "realbackamount")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "realbackamount").toString());
           
            UFDouble backamount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "backamount")==null?new UFDouble(0):
                new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "backamount").toString());
            
            
            if((backamount.compareTo(realbackamount)<0)){
            	getBillUI().showErrorMessage("�ڣ�"+(i+1)+"���б����˻�������������ڷ�Ʊ������");
            	return ;
	         }
	     }
        PubItf  pubitf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        AggregatedValueObject aggvo=getBillCardPanelWrapper().getBillVOFromUI();
        BackbillBVO[] bvos=(BackbillBVO[])aggvo.getChildrenVO();
        String info=pubitf.checkBackamount(bvos);
        if(!info.equals("")){
            getBillUI().showErrorMessage(info);
            return ;
        }
        UFDouble faceamount = new UFDouble(0);                                              //��Ž����ϵ�ʵ���˻�����
        String pk_invoce  = null;
        for(int i=0;i<bvos.length;i++){
            BackbillBVO bvo=(bvos[i]);
            UFDouble amount= bvo.getRealbackamount();
            faceamount = faceamount.add(amount);
            pk_invoce = bvo.getVsourcebillid();
        }
        super.onBoSave();
        //����Ʊ�ϵ��������˻����ϵ������ó����Ƚ�
        StringBuffer sql=new StringBuffer("")
        .append(" select pk_invoice pk,sum(isnull(amount,0)) amount,'A' flag from eh_invoice_b where ")
        .append(" pk_invoice = '"+pk_invoce+"' and isnull(dr,0)=0 group by pk_invoice")
        .append(" union all ")
        .append(" select vsourcebillid pk,sum(isnull(realbackamount,0)) amount,'B' flag from eh_backbill_b where ")
        .append(" vsourcebillid = '"+pk_invoce+"' and isnull(dr,0)=0")
        .append(" group by vsourcebillid ");
        
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
        if(arr!=null&&arr.size()>0){
			HashMap hma = new HashMap();
			for(int i=0; i<arr.size(); i++){
				HashMap hm=(HashMap)arr.get(i);
				String flag = hm.get("flag")==null?"":hm.get("flag").toString();
				UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
				hma.put(flag, amount);
			}
			String updateSQL = null;
			if(hma.get("A").equals(hma.get("B"))){  //ȫ���˻�ʱ�����۷�Ʊ�ر�
				updateSQL = "update eh_invoice set th_flag = 'Y' where pk_invoice = '"+pk_invoce+"'";
			}
			else{
				updateSQL = "update eh_invoice set th_flag = 'N' where pk_invoice = '"+pk_invoce+"'";
			}
			pubitf.updateSQL(updateSQL);
		}
      
//        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").setShow(false);
//        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vpk_sbbill").setShow(t);
//        getBillUI().updateUI();
	}

	@Override
	public void onButton_N(ButtonObject bo, BillModel model) {
        // TODO Auto-generated method stub
        super.onButton_N(bo, model);
        String bocode=bo.getCode();
        //�������۷�Ʊ���������˻�ʱ���ͻ��ͷ�Ʊ�Ų�����༭��ͬʱ���岻�ܽ����в���
        if(bocode.equals("���۷�Ʊ")){
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invoice").setEnabled(false);
          int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
          for(int i=0;i<row;i++){
             
                  getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"firstcount", false);
                  getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"secondcount", false);
                  getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"backprice", false);
                  getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"def_1", false);
          }
          getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
          getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
          getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
          getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
          getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
          getBillUI().updateUI();
        }
    }
    
	@Override
	protected void onBoDelete() throws Exception {
		int res = onBoDeleteN();
		if(res==0){
			return;
		}
		String pk_invoice = ((BackbillVO)getBillUI().getChangedVOFromUI().getParentVO()).getVsourcebillid();
		String updateSQL = "update eh_invoice set th_flag = 'N' where pk_invoice = '"+pk_invoice+"'";
		PubItf  pubitf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		pubitf.updateSQL(updateSQL);
		super.onBoTrueDelete();
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		 getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
         getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
         getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
         getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
         getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
	}
}
