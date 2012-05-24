package nc.ui.eh.stock.h0150605;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBillStatus;



/**
 * ����˵�����ɹ���Ʊ
 * @author ����
 * 2008-05-28 ����04:03:18
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
		getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));	
		getBillCardPanel().setHeadItem("invoicerq", _getDate());             //��Ʊ����
		getBillCardPanel().setHeadItem("pdrq",_getDate());                   //Ʊ������
		
		super.setDefaultData();
	}
	@Override
	protected void initSelfData() {
		//������
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     //��Ʊ���
	     getBillCardWrapper().initHeadComboBox("invoicetype", ICombobox.INVOICETYPE,true);
	     getBillListWrapper().initHeadComboBox("invoicetype", ICombobox.INVOICETYPE,true);
	     super.initSelfData();
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey=e.getKey();
		super.afterEdit(e);
		//���̴������ŵ�һЩ��Ϣ 
		if(strKey.equals("pk_cubasdoc")){
     		String pk_cubasdoc=(String) (getBillCardPanel().getHeadItem("pk_cubasdoc")==null?"":
     			getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject());
     		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
            //<�޸�>����:��ӿ��̹����.ʱ��:2009-08-14 ����16:03:18.����:��־Զ 
     		StringBuffer sql = new StringBuffer()
             .append(" select a.pk_psndoc,a.psnname,b.freecustflag,d.pk_deptdoc,d.deptname from bd_psndoc a,bd_cubasdoc b,eh_custyxdb c,bd_deptdoc d,bd_cumandoc e  ")
             .append(" where a.pk_psndoc=c.pk_psndoc  and a.pk_deptdoc=d.pk_deptdoc ")
             .append(" and e.pk_cumandoc = c.pk_cubasdoc ")
             .append(" and e.pk_cubasdoc = b.pk_cubasdoc")
             .append(" and e.pk_cumandoc ='"+pk_cubasdoc+"' and c.ismain ='Y' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and NVL(c.dr,0)=0 ");
             try {
             	Vector vector =(Vector) iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
             	if(vector!=null&&vector.size()!=0){
             		Vector ve =(Vector) vector.get(0);
             		String pk_psndoc=ve.get(0)==null?"":ve.get(0).toString();  //��ԱPK
             		String vpnsdocname=ve.get(1)==null?"":ve.get(1).toString(); //��Ա���
//             		String freecustflag=ve.get(2)==null?"":ve.get(3).toString();
             		String pk_deptdoc=ve.get(3)==null?"":ve.get(3).toString();  //����PK
                 	String deptname=ve.get(4)==null?"":ve.get(4).toString();    //��������         		
     				getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
     				getBillCardPanel().setHeadItem("vcaiperson", vpnsdocname);
     				getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", pk_deptdoc);
					getBillCardWrapper().getBillCardPanel().setHeadItem("vdept", deptname);
             	}	
     		} catch (BusinessException e1) {
     			e1.printStackTrace();
     		}
     		String freecustflag=getBillCardPanel().getHeadItem("issh")==null?"":
     			getBillCardPanel().getHeadItem("issh").getValueObject().toString();
     		if(freecustflag.equals("true")){
     			getBillCardPanel().getHeadItem("shinfo").setEnabled(true);
     		}else{
     			getBillCardPanel().setHeadItem("shinfo", "");
     			getBillCardPanel().getHeadItem("shinfo").setEnabled(false);
     		}
         }
		
// 		//˰��ļ���
// 		if(strKey.equals("fpmount") || strKey.equals("taxinprice") || strKey.equals("taxrate")){
// 			int row=e.getRow();
// 			UFDouble fpmount=new UFDouble(getBillCardPanel().getBodyValueAt(row, "fpmount")==null?"0":
// 				getBillCardPanel().getBodyValueAt(row, "fpmount").toString());
// 			UFDouble taxinprice=new UFDouble(getBillCardPanel().getBodyValueAt(row, "taxinprice")==null?"0":
// 				getBillCardPanel().getBodyValueAt(row, "taxinprice").toString());
// 			UFDouble taxrate=new UFDouble(getBillCardPanel().getBodyValueAt(row, "taxrate")==null?"0":
// 				getBillCardPanel().getBodyValueAt(row, "taxrate").toString());
// 			getBillCardPanel().setBodyValueAt(fpmount.multiply(taxinprice).multiply(taxrate), row, "taxmny");
// 		}
 		
 		 // ��д���η�Ʊ������ʵʱ�����ѿ�Ʊ��(���������ο�Ʊ) add by wb at 2008-5-15 10:36:30
        int flag = ClientEventHandler.flag;          // �������ӱ��
//        if(e.getKey().equals("fpmount")&&flag==2){  // ���Ӳɹ���ͬ����ʱ�������ջ���
//       	 int row = getBillCardPanel().getBillTable().getSelectedRow();
//       	 String vsourcebillid = getBillCardPanel().getBodyValueAt(row, "vsourcebillid")==null?null:
//       		                   getBillCardPanel().getBodyValueAt(row, "vsourcebillid").toString();
//       	 String pk_stockinvoices_b = getBillCardPanel().getBodyValueAt(row, "pk_stockinvoices_b")==null?null:
//                                  getBillCardPanel().getBodyValueAt(row, "pk_stockinvoices_b").toString();  // �ӱ�����
//            //ʵʱ�����ջ���(�����������ջ���)
//       	 UFDouble amount = PubTools.calTotalamount("eh_arap_stockinvoices_b", "fpmount", vsourcebillid, "pk_stockinvoices_b", pk_stockinvoices_b);
//       	 getBillCardPanel().setBodyValueAt(amount, row, "kpmount");
//        }
	}

	/*
	 * 	���ӵİ�ť
	 * (non-Javadoc) @����˵�����Զ��尴ť
	 */
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(
				IEHButton.SUREMONEY, "����ȷ��", "����ȷ��");
		btn.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
		addPrivateButton(btn);
		
		super.initPrivateButton();
	}
	

}
