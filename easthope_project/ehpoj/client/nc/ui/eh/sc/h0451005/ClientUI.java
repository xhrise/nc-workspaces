package nc.ui.eh.sc.h0451005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ����˵������������
 * @author ����
 * 2008-05-07 ����04:03:18
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
		 //��������
		 getBillCardPanel().setHeadItem("scdate", _getDate()); 
		super.setDefaultData();
	}
    
	@Override
	protected void initSelfData() {
		//������
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
	     super.initSelfData();
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		//ȡBOM�����°汾
		if(e.getKey().equals("vinvcode")){
			int row=getBillCardPanel().getBillTable().getSelectedRow();
			String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?""
					:getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			String sql="select ver from eh_bom where  sc_flag='Y' and pk_invbasdoc='"+pk_invbasdoc+"' and pk_corp='"+_getCorp().getPrimaryKey()+"' and isnull(dr,0)=0 ";
			try {
				ArrayList al= (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				Integer ver=null;
				if(al==null||al.size()==0){
//					showErrorMessage("���BOM��û��ά��");
				}else{
					HashMap hm=(HashMap) al.get(0);
					ver=hm.get("ver")==null?new Integer(0):new Integer(hm.get("ver").toString());
				}
				getBillCardPanel().setBodyValueAt(ver, row, "ver");
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
		}
		//���ƶԿ�����ļ���
		if(e.getKey().equals("vinvcode")){
			int row=e.getRow();
			String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
				getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			String sql="select sum(isnull(amount,0)) amount from eh_kc where pk_invbasdoc='"+pk_invbasdoc+"' and isnull(dr,0)=0";
			String sqlkc="select safeamount from eh_sc_safekc where pk_invbasdoc='"+pk_invbasdoc+"' and isnull(dr,0)=0";
			try {
				Object kcstr = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
				Object sqfekcstr = iUAPQueryBS.executeQuery(sqlkc, new ColumnProcessor());   	//��ȫ���
				UFDouble kc = new UFDouble(kcstr==null?"0":kcstr.toString());
				UFDouble safekc = new UFDouble(sqfekcstr==null?"0":sqfekcstr.toString());
				getBillCardPanel().setBodyValueAt(kc, row, "kc");
				getBillCardPanel().setBodyValueAt(safekc, row, "safekc");
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
		}
		//�������ж�
//		if(e.getKey().equals("scmount")){
//			int row=e.getRow();
//			String vsourcebillrowid=getBillCardPanel().getBodyValueAt(row, "vsourcebillrowid")==null?"":
//				getBillCardPanel().getBodyValueAt(row, "vsourcebillrowid").toString();
//			if(!vsourcebillrowid.equals("")){
//				UFDouble ordermount=new UFDouble(getBillCardPanel().getBodyValueAt(row, "ordermount")==null?"0":
//					getBillCardPanel().getBodyValueAt(row, "ordermount").toString());			
//				UFDouble yscmount=new UFDouble(getBillCardPanel().getBodyValueAt(row, "yscmount")==null?"0":
//					getBillCardPanel().getBodyValueAt(row, "yscmount").toString());			
//				UFDouble scmount=new UFDouble(getBillCardPanel().getBodyValueAt(row, "scmount")==null?"0":
//					getBillCardPanel().getBodyValueAt(row, "scmount").toString());
//				UFDouble sum=yscmount.add(scmount);
//				if(ordermount.sub(sum).toDouble()<0){
//					showErrorMessage("��Ҫ�����������Ѿ����ڶ�����������");
//					getBillCardPanel().setBodyValueAt(new UFDouble(0), row, "scmount");
//				}
//			}
//		}
//		
		
		
		
		
		
//		if(e.getKey().equals("vunit")){
//			int row=e.getRow();
//			String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
//				getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();//��������
//			String pk_unit=getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"":
//				getBillCardPanel().getBodyValueAt(row, "pk_unit").toString();//��λ����
//			UFDouble ordermount=new UFDouble(getBillCardPanel().getBodyValueAt(row, "ordermount")==null?"0":
//					getBillCardPanel().getBodyValueAt(row, "ordermount").toString());//��������
//			UFDouble kc=new UFDouble(getBillCardPanel().getBodyValueAt(row, "kc")==null?"0":
//				getBillCardPanel().getBodyValueAt(row, "kc").toString());//�����
//			
//			HashMap hmall=new PubTools().canChange(pk_invbasdoc);
//			if(hmall.containsKey(pk_unit)){
//				HashMap hmMain=new PubTools().getMainMeasdoc(pk_invbasdoc);
//				if(!(hmMain.containsKey(pk_unit))){
//					HashMap hm=new PubTools().getMeasdoc(pk_invbasdoc);
//					UFDouble changerate=new UFDouble(hm.get(pk_unit)==null?"0":hm.get(pk_unit).toString());
//					kc=kc.div();
//					
//				}
//			}else{
//				getBillCardPanel().setBodyValueAt(pk_units, row, "pk_unit");
//				getBillCardPanel().setBodyValueAt(unitname, row, "vunit");
//			}
//		}
			
	}
	 @Override
	    protected void initPrivateButton() {
		 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
	        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	        addPrivateButton(btn1);
	    	super.initPrivateButton();
	    }
     
     public nc.vo.pub.lang.UFDate getDate(){
         return _getDate();
     }
	

}
