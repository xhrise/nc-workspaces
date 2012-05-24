
package nc.ui.eh.cwitf.h10106;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.base.IChildMenuController;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.BillTableCreateTreeTableTool;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IBillStatus;

/**
 * ���ܣ�ƾ֤ģ�嶨��
 * @author ����Դ
 * ���ڣ�2008-7-10 15:28:46
 */

public class ClientUI extends BillTreeManageUI{

	private static final long serialVersionUID = 1L;

	protected IVOTreeData createTableTreeData() {
		return null;
	}

	protected IVOTreeData createTreeData() {
		return new ClientManageData();
	}

	public void afterInit() throws java.lang.Exception{
		modifyRootNodeShowName("ƾ֤��������");
	}

	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}
	
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}
    
	public void setDefaultData() throws Exception {
		//�ڱ�ͷ���ù�˾
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		//ȡ���Ƶ�����
        getBillCardPanel().setTailItem("dmakedate",_getDate());
        //ȡ���Ƶ�������
        getBillCardPanel().setTailItem("coperatorid",_getOperator());
        nc.ui.trade.pub.VOTreeNode selectNode = this.getBillTreeSelectNode();
        if (selectNode != null) {
            nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) selectNode.getData();
            String pk_billtype = vo.getAttributeValue("pk_billtype").toString();
			getBillCardPanel().setHeadItem("pk_billtype", pk_billtype);
        }
        
        
     }
	public String getRefBillType() {
		return null;
	}  
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,getUIControl());
	}

    protected IChildMenuController createChildMenuController() {
        return super.createChildMenuController();
    }
    
	protected BusinessDelegator createBusinessDelegator() {
		return super.createBusinessDelegator();
	}
	
	protected UITree getBillTree() {
		return super.getBillTree();
	}

	protected BillTableCreateTreeTableTool getBillTableTreeData() {
		return super.getBillTableTreeData();
	}
	
	 protected void initSelfData() {
	        getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	        getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	        getBillCardWrapper().initBodyComboBox("direction", ICombobox.STR_DIRECTION, true);
	        getBillListWrapper().initBodyComboBox("direction", ICombobox.STR_DIRECTION, true);
	        getBillCardWrapper().initHeadComboBox("cbhs",ICombobox.CW_CBHS, true);
	        getBillListWrapper().initHeadComboBox("cbhs",ICombobox.CW_CBHS, true);
	        getBillCardWrapper().initHeadComboBox("clckpzxz",ICombobox.CW_CLCKPZ, true);
		    getBillListWrapper().initHeadComboBox("clckpzxz",ICombobox.CW_CLCKPZ, true);
	    }

   @Override
   public void afterEdit(BillEditEvent e) {
	   String strKey=e.getKey();
	   int i=e.getRow();
	   //ִ�й�ʽ
	   if(e.getPos()==HEAD){
           String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
           getBillCardPanel().execHeadFormulas(formual);
       }else if (e.getPos()==BODY){
           String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
           getBillCardPanel().execBodyFormulas(e.getRow(),formual);
       }else{
           getBillCardPanel().execTailEditFormulas();
       }
	   if(strKey.equals("pk_code")){
		   getBillCardPanel().setBodyValueAt("", i, "vendor");
		   getBillCardPanel().setBodyValueAt("", i, "vendorfield");
		   getBillCardPanel().setBodyValueAt("", i, "pk_vendorfield");
		   getBillCardPanel().setBodyValueAt("", i, "vcust");
		   getBillCardPanel().setBodyValueAt("", i, "custfield");
		   getBillCardPanel().setBodyValueAt("", i, "pk_custfield");
		   getBillCardPanel().setBodyValueAt("", i, "invname");
		   getBillCardPanel().setBodyValueAt("", i, "invfile");
		   getBillCardPanel().setBodyValueAt("", i, "pk_invfield");  
	   }
	   //����pub_billtemplet_b��pk���ҳ����Ͻ������ֶ�����
	   if(strKey.equals("invname")){
		   String pk_billtemtlpet_b=getBillCardPanel().getBodyValueAt(i, "pk_invfield")==null?"":
			   getBillCardPanel().getBodyValueAt(i, "pk_invfield").toString();
		   String sql="select table_code,itemkey from pub_billtemplet_b where pk_billtemplet_b='"+pk_billtemtlpet_b+"'";
		   IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		   try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int j=0;j<al.size();j++){
				HashMap hm=(HashMap) al.get(j);
				String table_code=hm.get("table_code")==null?"":hm.get("table_code").toString();
				String itemkey=hm.get("itemkey")==null?"":hm.get("itemkey").toString();
				getBillCardPanel().setBodyValueAt(table_code+"."+itemkey, i, "invfile");
			}
		} catch (BusinessException e1) {
			e1.printStackTrace();
		} 
	   }
       //����pub_billtemplet_b��pk���ҳ��ͻ��������ֶ�����
	   if(strKey.equals("vcust")){
		   //�Թ�Ӧ�̵����
		   getBillCardPanel().setBodyValueAt("", i, "vendor");
		   getBillCardPanel().setBodyValueAt("", i, "vendorfield");
		   getBillCardPanel().setBodyValueAt("", i, "pk_vendorfield");
		   
		   String pk_billtemtlpet_b=getBillCardPanel().getBodyValueAt(i, "pk_custfield")==null?"":
			   getBillCardPanel().getBodyValueAt(i, "pk_custfield").toString();
		   String sql="select table_code,itemkey from pub_billtemplet_b where pk_billtemplet_b='"+pk_billtemtlpet_b+"'";
		   IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		   try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int j=0;j<al.size();j++){
				HashMap hm=(HashMap) al.get(j);
				String table_code=hm.get("table_code")==null?"":hm.get("table_code").toString();
				String itemkey=hm.get("itemkey")==null?"":hm.get("itemkey").toString();
				getBillCardPanel().setBodyValueAt(table_code+"."+itemkey, i, "custfield");
			}
		} catch (BusinessException e1) {
			e1.printStackTrace();
		}
	   }
       //����pub_billtemplet_b��pk���ҳ���Ӧ�̽������ֶ�����
	   if(strKey.equals("vendor")){
           //�Կͻ������
		   getBillCardPanel().setBodyValueAt("", i, "vcust");
		   getBillCardPanel().setBodyValueAt("", i, "custfield");
		   getBillCardPanel().setBodyValueAt("", i, "pk_custfield");
		   
		   String pk_billtemtlpet_b=getBillCardPanel().getBodyValueAt(i, "pk_vendorfield")==null?"":
			   getBillCardPanel().getBodyValueAt(i, "pk_vendorfield").toString();
		   String sql="select table_code,itemkey from pub_billtemplet_b where pk_billtemplet_b='"+pk_billtemtlpet_b+"'";
		   IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		   try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int j=0;j<al.size();j++){
				HashMap hm=(HashMap) al.get(j);
				String table_code=hm.get("table_code")==null?"":hm.get("table_code").toString();
				String itemkey=hm.get("itemkey")==null?"":hm.get("itemkey").toString();
				getBillCardPanel().setBodyValueAt(table_code+"."+itemkey, i, "vendorfield");
			}
		}catch (BusinessException e1) {
			e1.printStackTrace();
		 } 
	   }
	   //����pub_billtemplet_b��pk���ҳ����Ž������ֶ�����
	   if(strKey.equals("dept")){
		   String pk_billtemtlpet_b=getBillCardPanel().getBodyValueAt(i, "pk_deptfield")==null?"":
			   getBillCardPanel().getBodyValueAt(i, "pk_deptfield").toString();
		   String sql="select table_code,itemkey from pub_billtemplet_b where pk_billtemplet_b='"+pk_billtemtlpet_b+"'";
		   IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		   try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int j=0;j<al.size();j++){
				HashMap hm=(HashMap) al.get(j);
				String table_code=hm.get("table_code")==null?"":hm.get("table_code").toString();
				String itemkey=hm.get("itemkey")==null?"":hm.get("itemkey").toString();
				getBillCardPanel().setBodyValueAt(table_code+"."+itemkey, i, "deptfield");
			}
		} catch (BusinessException e1) {
			e1.printStackTrace();
		} 
	   }
	   
	   //����pub_billtemplet_b��pk���ҳ����ˣ��������������ֶ�����
	   if(strKey.equals("amount")){
		   String pk_billtemtlpet_b=getBillCardPanel().getBodyValueAt(i, "pk_amount")==null?"":
			   getBillCardPanel().getBodyValueAt(i, "pk_amount").toString();
		   String sql="select table_code,itemkey from pub_billtemplet_b where pk_billtemplet_b='"+pk_billtemtlpet_b+"'";
		   IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		   try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int j=0;j<al.size();j++){
				HashMap hm=(HashMap) al.get(j);
				String table_code=hm.get("table_code")==null?"":hm.get("table_code").toString();
				String itemkey=hm.get("itemkey")==null?"":hm.get("itemkey").toString();
				getBillCardPanel().setBodyValueAt(table_code+"."+itemkey, i, "amountfield");
			}
		} catch (BusinessException e1) {
			e1.printStackTrace();
		} 
	   }
	   
	   //����pub_billtemplet_b��pk���ҳ����������ֶ�����
	   if(strKey.equals("vje")){
		   String pk_billtemtlpet_b=getBillCardPanel().getBodyValueAt(i, "pk_jefield")==null?"":
			   getBillCardPanel().getBodyValueAt(i, "pk_jefield").toString();
		   String sql="select pos,table_code,itemkey from pub_billtemplet_b where pk_billtemplet_b='"+pk_billtemtlpet_b+"'";
		   IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		   try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int j=0;j<al.size();j++){
				HashMap hm=(HashMap) al.get(j);
				String table_code=hm.get("table_code")==null?"":hm.get("table_code").toString();
				String itemkey=hm.get("itemkey")==null?"":hm.get("itemkey").toString();
				String pos=hm.get("pos")==null?"":hm.get("pos").toString();
				getBillCardPanel().setBodyValueAt(table_code+"."+itemkey, i, "jefield");
				//����Ƿ��������
				if(pos.equals("1")){
					getBillCardPanel().setBodyValueAt("B", i, "body_flag");
				}else{
					getBillCardPanel().setBodyValueAt("", i, "body_flag");
				}
			}
		} catch (BusinessException e1) {
			e1.printStackTrace();
		} 
	   }
	   if(strKey.equals("pk_code")){
		   cleanBody(i);//�����Ӧ����
//		   String ccode=getBillCardPanel().getBodyValueAt(i, "ccode")==null?"":
//			   getBillCardPanel().getBodyValueAt(i, "ccode").toString();
//		   PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
//		   StringBuffer br=new StringBuffer();
//		    br.append("select ");
//	    	br.append("ccode,");  					//��Ŀ����              
//	    	br.append("ccode_name,");  		 		//��Ŀ����                  
//	    	br.append("ccode_engl,");  		 		//��ĿӢ������              
//	    	br.append("igrade,");  				    //���뼶��                  
//	    	br.append("bproperty,");		  	    //��Ŀ����                  
//	    	br.append("cbook_type,"); 	   			//��ҳ��ʽ                  
//	    	br.append("cbook_type_engl,"); 			//��ҳ��ʽӢ������               
//	    	br.append("chelp,");				    //��Ŀ������            
//	    	br.append("cexch_name,");			    //��������                    
////	    	br.append("cmeasure,");		            //������λ                  
//	    	br.append("bperson,");                  //�Ƿ������������       
//	    	br.append("bcus ,");                    //�Ƿ�ͻ���������    
//	    	br.append("bsup ,");                    //�Ƿ�Ӧ���������� 
//	    	br.append("bdept,");                    //�Ƿ��ź���     
//	    	br.append("bitem,");                    //�Ƿ���Ŀ����      
//	    	br.append("cass_item ,");               //��Ŀ�������          
//	    	br.append("br ");                		//�Ƿ��ռ��� 
//	    	br.append(" from code where ccode='"+ccode+"'");
//	    	try {
//				CodeVO[] codes=pubItf.U8Select(br.toString());
//				UFBoolean bcus=new UFBoolean(codes[0].getBcus()==null?"false":codes[0].getBcus().toString());
//				UFBoolean bsup=new UFBoolean(codes[0].getBsup()==null?"false":codes[0].getBsup().toString());
//				UFBoolean bdept=new UFBoolean(codes[0].getBdept()==null?"false":codes[0].getBdept().toString());
//				UFBoolean bitem=new UFBoolean(codes[0].getBitem()==null?"false":codes[0].getBitem().toString());
//				UFBoolean bperson=new UFBoolean(codes[0].getBperson()==null?"false":codes[0].getBperson().toString());
//				if(bsup.toString()=="Y"){
//					getBillCardPanel().getBillModel().setCellEditable(i,"vendor", true);
//				}else{
//					getBillCardPanel().getBillModel().setCellEditable(i,"vendor", false);
//				}
//				if(bperson.toString()=="Y"){
//					getBillCardPanel().getBillModel().setCellEditable(i,"individual", true);
//				}else{
//					getBillCardPanel().getBillModel().setCellEditable(i,"individual", false);
//				}
//				if(bcus.toString()=="Y"){
//					getBillCardPanel().getBillModel().setCellEditable(i,"vcust", true);
//				}else{
//					getBillCardPanel().getBillModel().setCellEditable(i,"vcust", false);
//				}
//				if(bdept.toString()=="Y"){
//					getBillCardPanel().getBillModel().setCellEditable(i,"dept", true);
//				}else{
//					getBillCardPanel().getBillModel().setCellEditable(i,"dept", false);
//				}
//				if(bitem.toString()=="Y"){
//					getBillCardPanel().getBillModel().setCellEditable(i,"invname", true);
//				}else{
//					getBillCardPanel().getBillModel().setCellEditable(i,"invname", false);
//				}
//			} catch (BusinessException e1) {
//				e1.printStackTrace();
//			}
	   }
	   super.afterEdit(e);
   }
   protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO bt1 = ButtonFactory.createButtonVO(IEHButton.CREATEVOUCHER,"����ƾ֤","����ƾ֤");
		bt1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
  	 	addPrivateButton(bt1);
  	 	nc.vo.trade.button.ButtonVO bt2 = ButtonFactory.createButtonVO(IEHButton.INSERTCDOE,"��Ŀ����","��Ŀ����");
		bt2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
  	 	addPrivateButton(bt2);
       super.initPrivateButton();
   }
   
   /**
    * ���༭��Ŀʱ���һЩ��������
    * @param row ��ǰ����
    */
   public void cleanBody(int row){
	   //��Ӧ��
	   getBillCardPanel().setBodyValueAt("", row, "vendor");
	   getBillCardPanel().setBodyValueAt("", row, "vendorfield");
	   getBillCardPanel().setBodyValueAt("", row, "pk_vendorfield");
	  
	   //�ͻ�
	   getBillCardPanel().setBodyValueAt("", row, "vcust");
	   getBillCardPanel().setBodyValueAt("", row, "custfield");
	   getBillCardPanel().setBodyValueAt("", row, "pk_custfield");
	   //����
	   getBillCardPanel().setBodyValueAt("", row, "dept");
	   getBillCardPanel().setBodyValueAt("", row, "deptfield");
	   getBillCardPanel().setBodyValueAt("", row, "pk_deptfield");
	   //����
	   getBillCardPanel().setBodyValueAt("", row, "invname");
	   getBillCardPanel().setBodyValueAt("", row, "invfile");
	   getBillCardPanel().setBodyValueAt("", row, "pk_invfield");
	   
	   //����
	   getBillCardPanel().setBodyValueAt("", row, "amount");
	   getBillCardPanel().setBodyValueAt("", row, "amountfield");
	   getBillCardPanel().setBodyValueAt("", row, "pk_amount");
	   	    
   }
}
