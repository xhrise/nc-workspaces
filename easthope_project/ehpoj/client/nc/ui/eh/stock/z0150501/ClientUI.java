package nc.ui.eh.stock.z0150501;

/**
 * �ɹ���ͬ
 * @author ����
 * �������� 2008-4-1 16:09:43
 */



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractMultiChildClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.stock.z0150501.StockContractBVO;
import nc.vo.eh.stock.z0150501.StockContractTermsVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


public class ClientUI extends AbstractMultiChildClientUI {
	
	public static String caopk_unit = null;
	public static String unitname=null;

	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBaseBD();
	}
	
	protected void initSelfData() {
		//�Ա�ͷ��ʼ��	(��ͬ״̬)   
		getBillCardWrapper().initHeadComboBox("contstatus", ICombobox.STR_CONTSTYPE,true);
		getBillListWrapper().initHeadComboBox("contstatus", ICombobox.STR_CONTSTYPE,true);
		super.initSelfData();
	}
	
	public void setDefaultData() throws Exception {		
		//�Ա�ͷ��ʼ��	  (��ͬ״̬)   
		getBillCardWrapper().initHeadComboBox("contstatus", ICombobox.STR_CONTSTYPE,true);
		getBillListWrapper().initHeadComboBox("contstatus", ICombobox.STR_CONTSTYPE,true);
		//ʱ��ĳ�ʼ��
		getBillCardPanel().setHeadItem("writedate", _getDate());
		getBillCardPanel().setHeadItem("startdate", _getDate());
		getBillCardPanel().setHeadItem("enddate", _getDate());
		getBillCardPanel().setHeadItem("ver", new Integer(1));	
		getBillCardPanel().setHeadItem("address", this.getAddr());//����PK_CORPȡ��˾Ŀ¼�е�Ӫҵ��ַ
		super.setDefaultData();
	}
	
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		
	}
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	/*
	 * (non-Javadoc) @����˵�����Զ��尴ť
	 */
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.STOCKCOPE, "��ͬ����", "��ͬ����");
		nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.STOCKCHANGE, "��ͬ���", "��ͬ���");
		btn.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		addPrivateButton(btn);
		addPrivateButton(btn2);
		super.initPrivateButton();
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent arg0) {
		//��ȡ����ǰ�ĵ�λ
		 if(arg0.getKey().equals("vunit")){
 			int row=getBillCardPanel().getBillTable().getSelectedRow();
 			caopk_unit= getBillCardPanel().getBodyValueAt(row,"pk_unit")==null?"":
                         getBillCardPanel().getBodyValueAt(row,"pk_unit").toString(); //��λ
 			unitname=getBillCardPanel().getBodyValueAt(row,"vunit")==null?"":
                getBillCardPanel().getBodyValueAt(row,"vunit").toString(); //��λ
 		}
		 
		return super.beforeEdit(arg0);
	}
	 public void  afterEdit(BillEditEvent e) {
        String strKey=e.getKey();
        //���ݿͻ��ѿ��̵���������Ӫ����������� 2008-05-14 add by wm
         if(strKey.equals("pk_cubasdoc")){
     		String pk_cubasdoc=(String) (getBillCardPanel().getHeadItem("pk_cubasdoc")==null?"":
     			getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject());
     		try {
     		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
             StringBuffer sql = new StringBuffer()
             //<�޸�>��ӿ��̵�����������ڣ�2009-08-14 16:09:43�����ߣ���־Զ
//             .append(" select a.pk_psndoc,a.psnname,b.freecustflag,d.pk_deptdoc,d.deptname from bd_psndoc a,bd_cubasdoc b,eh_custyxdb c,bd_deptdoc d  ,bd_cumandoc e  ")
//             .append(" where a.pk_psndoc=c.pk_psndoc  and a.pk_deptdoc=d.pk_deptdoc ")
//             .append(" and e.pk_cumandoc = c.pk_cubasdoc ")
//             .append(" and e.pk_cubasdoc = b.pk_cubasdoc")
//             .append(" and e.pk_cumandoc ='"+pk_cubasdoc+"' and c.ismain ='Y' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and NVL(c.dr,0)=0 ");
             .append("   select a.pk_psndoc,d.psnname,e.pk_deptdoc,e.deptname")
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
             		String vpnsdocname=ve.get(1)==null?"":ve.get(1).toString();
             		String pk_deptdoc=ve.get(2)==null?"":ve.get(2).toString();
                 	String deptname=ve.get(3)==null?"":ve.get(3).toString();             		
     				getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
     				getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", pk_deptdoc);
             	}else{
             		getBillCardPanel().setHeadItem("pk_psndoc", null);
     				getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", null);
             	}	 
     		} catch (Exception e1) {
     			e1.printStackTrace();
     		}
         }
        //2008-05-14 add by wm  ǰ̨ʵʱ�жϱ������ظ�              
         if(e.getKey().equals("vcode")){
        	 int all=isCanAdd();
        	 if(all==1){
        		 int delrows = getBillCardPanel().getBillModel("eh_stock_contract_te").getRowCount();
        		 int[] dels = new int[delrows];
        		 getBillCardPanel().getBillModel("eh_stock_contract_te").delLine(dels);
        		//int row =  this.getBillCardPanel().getBillTable().getSelectedColumn();
        		String pk_invbasdoc = this.getBillCardPanel().getBodyItem("vcode").getValueObject().toString();
        		StringBuffer str = new StringBuffer()
        		.append(" select a.def4,a.def5 from bd_invbasdoc a,bd_invmandoc b where ")
        		.append(" a.pk_invbasdoc = b.pk_invbasdoc ")
        		.append(" and b.pk_invmandoc ='"+pk_invbasdoc+"' ")
        		.append(" and NVL(b.dr,0) = 0 ");
        		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        		try {
					ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(str.toString(), new MapListProcessor());
					if(arr!=null&&arr.size()!=0){
	             			//ֻ��һ������
	             			HashMap hm = (HashMap) arr.get(0);
	             			String ggxz = hm.get("def4")==null?"":hm.get("def4").toString();
	             			String lhzb = hm.get("def5")==null?"":hm.get("def5").toString();
	             			
	             			StockContractTermsVO cvo = new StockContractTermsVO();
	        				cvo.setItemno(1);
	        				cvo.setItemname("�й���״");
	        				cvo.setItemcontent(ggxz);
	        				getBillCardPanel().getBillModel("eh_stock_contract_te").addLine();
	        				getBillCardPanel().getBillModel("eh_stock_contract_te").setBodyRowVO(cvo, 0);
	        				
	        				StockContractTermsVO cvo2 = new StockContractTermsVO();
	        				cvo2.setItemno(2);
	        				cvo2.setItemname("��ָ��");
	        				cvo2.setItemcontent(lhzb);
	        				getBillCardPanel().getBillModel("eh_stock_contract_te").addLine();
	        				getBillCardPanel().getBillModel("eh_stock_contract_te").setBodyRowVO(cvo2, 1);
					}
					
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
        		
        		 super.afterEdit(e); 
        	 }else if(all==99){//���ϸı��NULLʱ
        		 int delrows = getBillCardPanel().getBillModel("eh_stock_contract_te").getRowCount();
        		 int[] dels = new int[delrows];
        		 getBillCardPanel().getBillModel("eh_stock_contract_te").delLine(dels);
        		 super.afterEdit(e); 
        	 }
        	 else{
        		 showErrorMessage("���Ĵ������¼�����ظ�");
        	 }      	 
         }
         //�ܽ�����
         if(e.getKey().equals("amount")||e.getKey().equals("taxinprice")){
        	getBillCardPanel().setHeadItem("csum", new UFDouble());
 			int rows = getBillCardPanel().getBillTable().getRowCount();
 			if (rows != 0) {
 				UFDouble mofdifyprice=new UFDouble(0);
 				for (int i = 0; i < rows; i++) {
 					UFDouble taxinprice = getBillCardPanel().getBodyValueAt(i, "taxinprice") == null ? new UFDouble()
						: new UFDouble(getBillCardPanel().getBodyValueAt(i, "taxinprice").toString());
 					UFDouble amount = getBillCardPanel().getBodyValueAt(i, "amount") == null ? new UFDouble()
						: new UFDouble(getBillCardPanel().getBodyValueAt(i, "amount").toString());
 					UFDouble vsum=taxinprice.multiply(amount);
 					 mofdifyprice =mofdifyprice.add(vsum)==null?new UFDouble():mofdifyprice.add(vsum);
 				}
 				getBillCardPanel().setHeadItem("csum", mofdifyprice);
 			} else {
 				showErrorMessage("����û������");
 			} 
         }
         //2008-05-14 add by wm   ��λ�Ļ���
         if(e.getKey().equals("vunit")){      	 
        	 int row=e.getRow();
        	 String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
        		 getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
        	 String pk_unit=getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"":
        		 getBillCardPanel().getBodyValueAt(row, "pk_unit").toString();
        	 HashMap hm=new PubTools().canChange(pk_invbasdoc);
        	 if(!(hm.containsKey(pk_unit))){
        		 showErrorMessage("����ѡ�ĵ�λû���������д���");
        		 getBillCardPanel().setBodyValueAt(caopk_unit, row, "pk_unit");
        		 getBillCardPanel().setBodyValueAt(unitname, row, "vunit");
        		 return; 
        	 }
         }
         
         //ѡ��ɹ����ߵ����� ��Ӧ��,�������ϼ������Ϣ add by wb 2009��2��9��18:07:25
         if(e.getKey().equals("def_1")){
        	 getBillCardPanel().setHeadItem("pk_cubasdoc", null);
        	 getBillCardPanel().setHeadItem("pk_cubasdoc", null);
        	 //����ͬ�ӱ�ǩ����ɾ��
    		 int rowcount = getBillCardPanel().getBillModel("eh_stock_contract_b").getRowCount();
             int[] rows=new int[rowcount];
             for(int i=rowcount - 1;i>=0;i--){
                 rows[i]=i;
             }
             getBillCardPanel().getBillModel("eh_stock_contract_b").delLine(rows);
             //����ͬ��������ɾ��
    		 int rowcount2 = getBillCardPanel().getBillModel("eh_stock_contract_te").getRowCount();
             int[] rows2 = new int[rowcount2];
             for(int i=rowcount2 - 1;i>=0;i--){
                 rows2[i]=i;
             }
             getBillCardPanel().getBillModel("eh_stock_contract_te").delLine(rows2);
             //����ͬ���¼�����ɾ��
    		 int rowcount3 = getBillCardPanel().getBillModel("eh_stock_contract_ev").getRowCount();
             int[] rows3 = new int[rowcount3];
             for(int i=rowcount3 - 1;i>=0;i--){
                 rows3[i]=i;
             }
             getBillCardPanel().getBillModel("eh_stock_contract_ev").delLine(rows3);
        	 String pk_decision = getBillCardPanel().getHeadItem("def_1").getValueObject()==null?null:
        		 					getBillCardPanel().getHeadItem("def_1").getValueObject().toString();
        	 if(pk_decision!=null){
        		 try {
					setCTvalue(pk_decision);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	 }
        			 
         }
        super.afterEdit(e);
    }
	 
	 /***
	  * ѡ��ɹ����ߵ����� ��Ӧ��,�������ϼ������Ϣ
	  * wb 2009��2��9��18:07:25
	  * @param pk_decision
	 * @throws Exception 
	  */
	 private void setCTvalue(String pk_decision) throws Exception {
		 UFDouble rate = new UFDouble(0);
		 
		 
		 StringBuffer ratesql = new StringBuffer()
		 .append(" SELECT NVL(cgrate,0)/100 rate from eh_rate where pk_corp = '"+_getCorp().getPk_corp()+"' and NVL(dr,0)=0 ");
		 IUAPQueryBS rateiUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList ratearr = (ArrayList) rateiUAPQueryBS.executeQuery(ratesql.toString(), new MapListProcessor());
			if(ratearr!=null&&ratearr.size()>0){
					HashMap hm = (HashMap)ratearr.get(0);
					rate = new UFDouble(hm.get("rate")==null?"0":hm.get("rate").toString());
			}else{
					this.showErrorMessage("��ά������,�ɹ�˰������");
					return;
		 }
         StringBuffer sql = new StringBuffer()											//b.rate,(a.xzcgamount*a.hsprice)/(1+b.rate)*b.rate
		.append(" SELECT a.pk_cubasdoc,a.jhdate,a.pk_invbasdoc,a.xzcgamount,a.hsprice,a.xzcgamount*a.hsprice taxmon,a.xzcgamount*a.hsprice vsum,a.pk_pricetype,a.ggxz,a.lhzb,c.pk_sw")
		.append(" FROM eh_stock_decision a LEFT JOIN eh_stock_decision_d c ON a.pk_decision = c.pk_decision AND a.pk_cubasdoc = c.pk_cubasdoc")
//		.append("  LEFT JOIN (SELECT NVL(cgrate,0)/100 rate from eh_rate where pk_corp = '"+_getCorp().getPk_corp()+"' ")
//		.append(" and NVL(dr,0)=0) b on 1=1")
		.append(" WHERE a.pk_decision = '"+pk_decision+"'");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				String pk_cubasdoc = hmA.get("pk_cubasdoc")==null?"":hmA.get("pk_cubasdoc").toString();
				
				/**Ӫ������ ���� add by wb 2009-11-4 17:12:53**/
				 StringBuffer psnsql = new StringBuffer()
				 .append("   select a.pk_psndoc,e.pk_deptdoc")
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
	             	Vector vector =(Vector) iUAPQueryBS.executeQuery(psnsql.toString(), new VectorProcessor());
	             	if(vector!=null&&vector.size()!=0){
	             		Vector ve =(Vector) vector.get(0);
	             		String pk_psndoc=ve.get(0)==null?"":ve.get(0).toString();
	             		String pk_deptdoc=ve.get(1)==null?"":ve.get(1).toString();
	     				getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
	     				getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", pk_deptdoc);
	             	}else{
	             		getBillCardPanel().setHeadItem("pk_psndoc", null);
	     				getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", null);
	             	}
				
				UFDate jhdate = new UFDate(hmA.get("jhdate")==null?_getDate().toString():hmA.get("jhdate").toString());		 		//��������
				String pk_sw = hmA.get("pk_sw")==null?"":hmA.get("pk_sw").toString();
				getBillCardPanel().setHeadItem("pk_cubasdoc", pk_cubasdoc);
				getBillCardPanel().setHeadItem("enddate", jhdate);
				getBillCardPanel().setHeadItem("pk_sendtype", pk_sw);
				
				String pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
				String pk_contracttype = hmA.get("pk_pricetype")==null?"":hmA.get("pk_pricetype").toString();
				UFDouble amount = new UFDouble(hmA.get("xzcgamount")==null?"":hmA.get("xzcgamount").toString());					//�ɹ�����
				UFDouble hsprice = new UFDouble(hmA.get("hsprice")==null?"":hmA.get("hsprice").toString());							//��˰����
//				UFDouble rate = new UFDouble(hmA.get("rate")==null?"":hmA.get("rate").toString());									//˰��
				UFDouble taxmon = new UFDouble(hmA.get("taxmon")==null?"":hmA.get("taxmon").toString());						//˰��
				UFDouble vsum = new UFDouble(hmA.get("vsum")==null?"":hmA.get("vsum").toString());									//�ܽ��
				
				UFDouble taxmoney = taxmon.div(rate.add(1)).multiply(rate) ;
				
				StockContractBVO bvo = new StockContractBVO();
				bvo.setPk_invbasdoc(pk_invbasdoc);
				bvo.setPk_contracttype(pk_contracttype);
				bvo.setAmount(amount);
				bvo.setTaxinprice(hsprice);
				bvo.setRatrate(rate);
				bvo.setTaxmoney(taxmoney);
				getBillCardPanel().getBillModel("eh_stock_contract_b").addLine();
				getBillCardPanel().getBillModel("eh_stock_contract_b").setBodyRowVO(bvo, 0);
				getBillCardPanel().getBillModel("eh_stock_contract_b").execEditFormulasByKey(0, "vcode");
//				getBillCardPanel().getBillModel("eh_stock_contract_b").execEditFormulasByKey(0, "amount");
				getBillCardPanel().getBillModel("eh_stock_contract_b").execEditFormulasByKey(0, "vcontracttype");
				getBillCardPanel().setBodyValueAt(hsprice, 0, "taxinprice");
				getBillCardPanel().setBodyValueAt(vsum, 0, "vsum");
				String ggxz = hmA.get("ggxz")==null?"":hmA.get("ggxz").toString();						//�й���״
				StockContractTermsVO cvo = new StockContractTermsVO();
				cvo.setItemno(1);
				cvo.setItemname("�й���״");
				cvo.setItemcontent(ggxz);
				getBillCardPanel().getBillModel("eh_stock_contract_te").addLine();
				getBillCardPanel().getBillModel("eh_stock_contract_te").setBodyRowVO(cvo, 0);
				
				String lhzb = hmA.get("lhzb")==null?"":hmA.get("lhzb").toString();						//��ָ��
				StockContractTermsVO cvo2 = new StockContractTermsVO();
				cvo2.setItemno(2);
				cvo2.setItemname("��ָ��");
				cvo2.setItemcontent(lhzb);
				getBillCardPanel().getBillModel("eh_stock_contract_te").addLine();
				getBillCardPanel().getBillModel("eh_stock_contract_te").setBodyRowVO(cvo2, 1);
			}
		 
	 }
	 
		//ǰ̨У��
		private  int isCanAdd(){  
			int all=0;
			int row= getBillCardPanel().getBillTable().getSelectedRow();
			String pkinvbasdoc=(String) getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc");
		int rows=getBillCardPanel().getBillModel().getColumnCount();
		for(int i=0;i<rows;i++){
			String pk_invbasdocs=(String) getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc");
//				if(pkinvbasdoc.equals(pk_invbasdocs))
//				{
//					all=all+1;
//				}	
			if(pkinvbasdoc!=null){
				
			if(pkinvbasdoc.equals(pk_invbasdocs))
				{
					all=all+1;
				}		
			}else{
				all=99;//�Զ���ѡ�����ϱ��NULL�Ĵ���ʱ��2010-01-07���ߣ���־Զ
			}
		}
			return all;	 
		 }
		
		//����PK_CORPȡ��˾Ŀ¼�е�Ӫҵ��ַ���������ص�
		 public String getAddr(){
			 String addr = null;
			 String pk_corp = _getCorp().getPrimaryKey();
			 StringBuffer sql = new StringBuffer()
			 .append(" SELECT bdcorp.saleaddr FROM BD_CORP bdcorp WHERE bdcorp.PK_CORP = '"+ pk_corp +"' and NVL(bdcorp.dr,0) = 0 ");
			 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			 try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
				if(arr!=null&&arr.size()>0){
					HashMap hm = (HashMap)arr.get(0);
					addr = hm.get("saleaddr") == null?"":hm.get("saleaddr").toString();
				}
			 }catch (Exception e) {
				e.printStackTrace();
			}
			 return addr;
		 }
	
}