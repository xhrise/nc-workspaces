package nc.ui.eh.stock.z0151001;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;




/**
 * ����˵�����ջ�֪ͨ��
 * @author ����
 * 2008-03-24 ����04:03:18
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
	protected void initPrivateButton() {
	        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
	        btn.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	        addPrivateButton(btn);
	        
	        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CONGEAL,"������ֹ����","������ֹ����");
	        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT,IBillOperate.OP_INIT});
	        addPrivateButton(btn1);
	        super.initPrivateButton();
	    }
	  @Override
	protected void initSelfData() {
			 //������
			 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
		     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
		     //��ʼɢ����Ϣ���ܱ༭
		     getBillCardPanel().getHeadItem("retailinfo").setEdit(false);
		     //˾�������ĳ�ʼ��
		     getBillCardWrapper().initHeadComboBox("yjsb_flag",ICombobox.STR_YJSB, true);
		     getBillListWrapper().initHeadComboBox("yjsb_flag",ICombobox.STR_YJSB, true);
		     //��������ĳ�ʼ��
		     getBillCardWrapper().initHeadComboBox("allcheck",ICombobox.STR_ALLCHECK, true);
		     getBillListWrapper().initHeadComboBox("allcheck",ICombobox.STR_ALLCHECK, true);
		     
		     super.initSelfData();
		}

	@Override
	public void setDefaultData() throws Exception {
		 getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));
		getBillCardPanel().setHeadItem("receiptdate", _getDate());		
		super.setDefaultData();
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {       
			// ���ݿͻ��ѿ��̵���������Ӫ����������� 2008-05-14 add by wm
	         if(e.getKey().equals("pk_cubasdoc")){
	        	 getBillCardPanel().getHeadItem("retailinfo").setEnabled(false);
	        	 //5.17
	        	 if(e.getPos()==HEAD){
	                 String[] formual=getBillCardPanel().getHeadItem(e.getKey()).getEditFormulas();//��ȡ�༭��ʽ
	                 getBillCardPanel().execHeadFormulas(formual);
	             }else if (e.getPos()==BODY){
	                 String[] formual=getBillCardPanel().getBodyItem(e.getKey()).getEditFormulas();//��ȡ�༭��ʽ
	                 getBillCardPanel().execBodyFormulas(e.getRow(),formual);
	             }else{
	                 getBillCardPanel().execTailEditFormulas();
	             }
	        	 String  retail_flag=(String) (getBillCardPanel().getHeadItem("retail_flag")==null?"P":getBillCardPanel().getHeadItem("retail_flag").getValueObject());
	        	 if(retail_flag.equals("true")){
	        		 getBillCardPanel().getHeadItem("retailinfo").setEnabled(true);
	        	 }else{
	        		 getBillCardPanel().getHeadItem("retailinfo").setEnabled(false);
	        	 }
	        	 
	        
	        	 
	        	 //5.17
	     		String pk_cubasdoc=(String) (getBillCardPanel().getHeadItem("pk_cubasdoc")==null?"":
	     			getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject());
	     		 try {
		     		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		            //<�޸�>����:��ӿͻ���������ʱ�䣺2009-08-14 ����16:03:18�����ߣ���־Զ
		     		StringBuffer sql = new StringBuffer()
	//	             .append(" select a.pk_psndoc,a.psnname,b.freecustflag,d.pk_deptdoc,d.deptname from bd_psndoc a,bd_cubasdoc b,eh_custyxdb c,bd_deptdoc d  ,bd_cumandoc e  ")
	//	             .append(" where a.pk_psndoc=c.pk_psndoc  and a.pk_deptdoc=d.pk_deptdoc ")
	//	             .append(" and e.pk_cumandoc = c.pk_cubasdoc ")
	//	             .append(" and e.pk_cubasdoc = b.pk_cubasdoc")
	//	             .append(" and e.pk_cumandoc ='"+pk_cubasdoc+"' and c.ismain ='Y' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and NVL(c.dr,0)=0 ");
		     		.append("   select a.pk_psndoc,c.freecustflag,e.pk_deptdoc,e.deptname")
		             .append("   from eh_custyxdb a,bd_cumandoc b,bd_cubasdoc c,bd_psndoc d,bd_deptdoc e")
		             .append("   where a.pk_cubasdoc = b.pk_cumandoc")
		             .append("   and b.pk_cubasdoc = c.pk_cubasdoc")
		             .append("   and a.pk_psndoc = d.pk_psndoc")
		             .append("   and d.pk_deptdoc = e.pk_deptdoc")
		             .append("   and c.pk_cubasdoc = '"+new PubTools().getPk_cubasdoc(pk_cubasdoc)+"'")
		             .append("   and b.pk_corp = '"+_getCorp().getPk_corp()+"'")
		             .append("   and a.ismain ='Y'")
		             .append("   and nvl(a.dr,0)=0 ")
		             .append("   and nvl(b.dr,0)=0 ");
	             	Vector vector =(Vector) iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
	             	if(vector!=null&&vector.size()!=0){
	             		Vector ve =(Vector) vector.get(0);
	             		String pk_psndoc=ve.get(0)==null?"":ve.get(0).toString();
//	             		String vpnsdocname=ve.get(1)==null?"":ve.get(1).toString();
	             		String  vfree=(ve.get(1)==null?"A":ve.get(1).toString()).equals("N")?"false":"true";
	             		String pk_deptdoc=ve.get(2)==null?"":ve.get(2).toString();
	                 	String deptname=ve.get(3)==null?"":ve.get(3).toString();
	     				getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
//	     				getBillCardPanel().setHeadItem("vpnsdocname", vpnsdocname);
//	     				getBillCardPanel().setHeadItem("retail_flag", vfree);
	     				getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", pk_deptdoc);
						getBillCardWrapper().getBillCardPanel().setHeadItem("vdeptdoc", deptname);
						if(vfree.equals("true")){
							getBillCardPanel().setHeadItem("retail_flag", "Y");
							getBillCardPanel().getHeadItem("retailinfo").setEnabled(true);
						}else{
							getBillCardPanel().setHeadItem("retail_flag", "N");
							getBillCardPanel().getHeadItem("retailinfo").setEnabled(false);
						}
	             	}else{
	             		getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", null);
						getBillCardWrapper().getBillCardPanel().setHeadItem("vdeptdoc", null);
	             	}
	     			
	     		} catch (Exception e1) {
	     			e1.printStackTrace();
	     		}
	         }  
	         //���ı༭
				if(e.getKey().equals("carnumber")||e.getKey().equals("amount")||e.getKey().equals("taxinprice")||e.getKey().equals("ratrate")){
		            int rows=getBillCardPanel().getBillTable().getRowCount();
		            UFDouble ze=new UFDouble(0);
		            for(int i=0;i<rows;i++){
		                UFDouble jgf=new UFDouble(getBillCardPanel().getBodyValueAt(i,"vallmoney")==null?"0":
		                    getBillCardPanel().getBodyValueAt(i,"vallmoney").toString());
		                ze=ze.add(jgf);
		            }
		            getBillCardPanel().setHeadItem("summoney",ze);
		            
		        }
                
//                //���Ƶ��ݵ�ʱ�򣬸�����ѡ�������Զ��ж��Ƿ�Ϊ������ϣ������Ƿ�����д��ϱ�� 2008-7-18 14:44:39
//                if(e.getKey().equals("vinvcode")){
//                    int row = getBillCardWrapper().getBillCardPanel().getBillTable().getRowCount();
//                    String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
//                            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
//                        String SQL = " select ischeck from eh_invbasdoc where pk_invbasdoc='"+pk_invbasdoc+"' " +
//                                " and (isnull(lock_flag,'N')='N' or lock_flag='' ) and isnull(dr,0)=0 ";
//                        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//                        UFBoolean ischeck = null;
//                        try {
//                            ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
//                            if(all!=null && all.size()>0){
//                                for(int j=0;j<all.size();j++){
//                                    HashMap hm = (HashMap) all.get(j);
//                                    ischeck = new UFBoolean(hm.get("ischeck")==null?"":hm.get("ischeck").toString());
//                                }                               
//                            }
//                        } catch (BusinessException e1) {
//                            e1.printStackTrace();
//                        }
//                        if((ischeck.toString()).equals("Y")){
//                            getBillCardWrapper().getBillCardPanel().setBodyValueAt("Y", row, "allcheck");
//                        }
//                    
//                    
//                }
//                
                
	         //�޸ĵ�ʱ���������У�飨���ã�---------------------------------------------------------
	         // ��д�����ջ�������ʵʱ�������ջ���(�����������ջ���) add by wb at 2008-5-15 10:36:30
//	         int flag = ClientEventHandler.flag;          // �������ӱ��
//	         if(e.getKey().equals("inamount")&&flag==2){  // ���Ӳɹ���ͬ����ʱ�������ջ���
//	        	 int row = getBillCardPanel().getBillTable().getSelectedRow();
//	        	 String vsourcebillid = getBillCardPanel().getBodyValueAt(row, "vsourcebillid")==null?null:
//	        		                   getBillCardPanel().getBodyValueAt(row, "vsourcebillid").toString();
//	        	 String pk_receipt_b = getBillCardPanel().getBodyValueAt(row, "pk_receipt_b")==null?null:
//	                                   getBillCardPanel().getBodyValueAt(row, "pk_receipt_b").toString();  // �ӱ�����
//                 //ʵʱ�����ջ���(�����������ջ���)
//	        	 UFDouble amount = PubTools.calTotalamount("eh_stock_receipt_b", "inamount", vsourcebillid, "pk_receipt_b", pk_receipt_b);
//	        	 getBillCardPanel().setBodyValueAt(amount, row, "ysamount");
//	         }
	         //end--------------------------------------------------------------------------------
	         	
			
			/*--------------------------------------------------------------------//��ǰ�İ�װ�����ж�(����)
			 //ͨ���Ƿ�˾�����жϰ�װ�����Ƿ�ɱ༭
       	 if(e.getKey().equals("issb")){
       		 int i=e.getRow();
       		 String issb=getBillCardPanel().getBodyValueAt(i, "issb").toString();
       		 if(issb.equals("true")){
       			 getBillCardPanel().getBillModel().setCellEditable(i,"packagweight", false); 
       		 }else{
       			 getBillCardPanel().getBillModel().setCellEditable(i,"packagweight", true);
       		 }
   		  }
       		 ----------------------------------------------------*/
       	
		super.afterEdit(e);
	}
	

}
