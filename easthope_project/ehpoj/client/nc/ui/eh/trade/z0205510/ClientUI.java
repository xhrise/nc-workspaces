/**
 * @(#)ClientUI.java	V3.1 2007-3-9
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.z0205510;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.CubasBySubcuRefModel;
import nc.ui.eh.refpub.InvdocByCusdocRefModel;
import nc.ui.eh.uibase.AbstractMultiChildClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z0205510.SeconddiscountVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ���� �����ۿ۹���
 * @author �麣
 * 2008-04-08
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractMultiChildClientUI {
	
    public static String pk_subcubasdoc = null;    //�ϼ��ͻ�
    public static String pk_cubasdoc = null;       // ����
	
	public ClientUI() {
		// TODO Auto-generated constructor stub
		super();
		pk_subcubasdoc = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.manage.BillManageUI#createController()
	 */
	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}
 

   
	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
	 */
	@Override
	protected ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		// TODO �Զ����ɷ������
		return new ClientBaseBD();
	}

	@Override
	protected void initSelfData() {
        getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
        getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
        getBillCardWrapper().initHeadComboBox("discounttype",ICombobox.STR_discounttype,true);		//�ۿ۶�����
        getBillListWrapper().initHeadComboBox("discounttype",ICombobox.STR_discounttype,true);
        getBillCardWrapper().initHeadComboBox("policetype",ICombobox.STR_polictype,true);			//��������
        getBillListWrapper().initHeadComboBox("policetype",ICombobox.STR_polictype,true);
        getBillCardWrapper().initHeadComboBox("jsmethod",ICombobox.STR_JSMETHOD,true);				//���㷽ʽ
        getBillListWrapper().initHeadComboBox("jsmethod",ICombobox.STR_JSMETHOD,true);
        getBillCardWrapper().initHeadComboBox("jstype",ICombobox.STR_JSTYPE,true);					//��������
        getBillListWrapper().initHeadComboBox("jstype",ICombobox.STR_JSTYPE,true);
        getBillCardWrapper().initHeadComboBox("invjstype",ICombobox.STR_INVJSTYPE,true);					//��������
        getBillListWrapper().initHeadComboBox("invjstype",ICombobox.STR_INVJSTYPE,true);
	}

	@Override
	public void setDefaultData() throws Exception {
        getBillCardPanel().setHeadItem("startdate", _getDate());
        getBillCardPanel().setHeadItem("enddate", PubTools.getDateAfter(_getDate(), 91));
        getBillCardPanel().setHeadItem("def_1", "Y");             // ���±��
        getBillCardPanel().setHeadItem("def_2", "1");             // �汾
        super.setDefaultData();
	}

	 @Override
	public boolean beforeEdit(BillEditEvent e) {
	    	String strKey=e.getKey();
	        if (e.getPos()==BODY){
	            if("vinvcode".equalsIgnoreCase(strKey)){
	                pk_cubasdoc=getBillCardPanel().getHeadItem("pk_subcubasdoc").getValueObject()==null?"":
	                			getBillCardPanel().getHeadItem("pk_subcubasdoc").getValueObject().toString();            //�ͻ�
                   if(pk_cubasdoc==null||pk_cubasdoc.length()<=0){
	            		showErrorMessage("�ͻ�����Ϊ��,����ѡ��ͻ�!");
	            	}
	            	InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;  //�����̴���������
	            }
	        }
	        return super.beforeEdit(e);
	 }
	 
	   /* (non-Javadoc)
	 * @see nc.ui.eh.uibase.AbstractMultiChildClientUI#afterEdit(nc.ui.pub.bill.BillEditEvent)
	 */
	@Override
        public void afterEdit(BillEditEvent e) {
            // TODO Auto-generated method stub
            String strKey=e.getKey();
            if(e.getPos()==HEAD){
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execHeadFormulas(formual);
                if(strKey.equals("pk_subcubasdoc")){
                 try {
					AggregatedValueObject aggVO = getChangedVOFromUI();
					pk_subcubasdoc = ((SeconddiscountVO)aggVO.getParentVO()).getPk_subcubasdoc();
					CubasBySubcuRefModel.pk_subcubasdoc = pk_subcubasdoc;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
   		      }
            }else if (e.getPos()==BODY){
                String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            }else{
                getBillCardPanel().execTailEditFormulas();
            }
            UFDate startdate=new UFDate(getBillCardPanel().getHeadItem("startdate").getValueObject().toString());
            UFDate enddate=new UFDate(getBillCardPanel().getHeadItem("enddate").getValueObject().toString());
            //��д��ͷ���ۿ۽��ʱͬʱ�������Ľ��
            if(strKey.equals("discountprice")&&e.getPos()==HEAD){
                UFDouble price =new UFDouble(getBillCardPanel().getHeadItem("discountprice").getValueObject()==null?"0":
                                    getBillCardPanel().getHeadItem("discountprice").getValueObject().toString());
                int row=getBillCardPanel().getBillModel("polic").getRowCount();
                for(int i=0;i<row;i++){
                    getBillCardPanel().setBodyValueAt(price, i,"discountprice");
                }
             }   
//            //05.01 ����
//            //ѡ���������е�����
//            if(strKey.equals("vcustcode"))
//            {
//            	int row=getBillCardPanel().getBillTable().getSelectedRow();
//            	String vcustcode=getBillCardPanel().getBodyValueAt(row, "vcustcode")==null?"":getBillCardPanel().getBodyValueAt(row, "vcustcode").toString();
//            	String sql="select a.custcode,a.custname,a.pk_cubasdoc,b.pk_invbasdoc,b.invcode,b.invname  from bd_cubasdoc a,eh_invbasdoc b,eh_custkxl c where  a.pk_cubasdoc=c.pk_cubasdoc and b.pk_invbasdoc=c.pk_invbasdoc and a.custcode='"+vcustcode+"'";
//            	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//            	try {
//					Vector ves=(Vector) iUAPQueryBS.executeQuery(sql, new VectorProcessor());
//					for(int i=0;i<ves.size();i++){	
//						getBillCardPanel().getBillModel().addLine();
//						Vector ve=(Vector) ves.get(i);
//						getBillCardPanel().setBodyValueAt(ve.get(0), row, "vcustcode");
//			            getBillCardPanel().setBodyValueAt(ve.get(1),row, "vcustname");
//			            getBillCardPanel().setBodyValueAt(ve.get(2), row, "pk_cubasdoc");
//			            getBillCardPanel().setBodyValueAt(ve.get(3),row, "pk_invbasdoc");
//			            getBillCardPanel().setBodyValueAt(ve.get(4), row, "vinvcode");
//			            getBillCardPanel().setBodyValueAt(ve.get(5),row, "vinvname");
//			            row++;
//					}
//				} catch (BusinessException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//            	
//            }
//           // 05.01 ����
          
             //���Ƚ�ִ�����ں���Ч����
             if(strKey.equals("startdate")){
               if(enddate.before(startdate)){
                   showErrorMessage("�ۿ۽������ڲ��������ۿۿ�ʼ����!");
                   getBillCardPanel().setHeadItem("startdate","");
                   return;
               }
               if(startdate.before(_getDate())){
                   showErrorMessage("�ۿۿ�ʼ���ڲ������ڵ�ǰ����!");
                   getBillCardPanel().setHeadItem("startdate","");
                   return;
               }
             }
             //���Ƚ�ִ�����ں���Ч����
             if(strKey.equals("enddate")){
                 if(enddate.before(startdate)){
                     showErrorMessage("�ۿ۽������ڲ��������ۿۿ�ʼ����");
                     getBillCardPanel().setHeadItem("enddate","");
                     return;
                 }
             }
             //ѡ��ͻ���Ʒ����һ���ۿ� add by wb at 2008-10-20 18:16:39
             String billmodel = getBillCardPanel().getCurrentBodyTableCode();
             if(billmodel.equals("checkinv")&&strKey.equals("vinvcode")){
                 String pk_cubasdoc = getBillCardPanel().getBodyValueAt(e.getRow(), "pk_cubasdoc")==null?"":
                	 					getBillCardPanel().getBodyValueAt(e.getRow(), "pk_cubasdoc").toString();
                 String pk_invbasdoc = getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc")==null?"":
	 					getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc").toString();
                 //add by houcq 2011-04-26
                 if ("".equals(pk_cubasdoc))
                 {                	 
                	 showErrorMessage("����ѡ�����ͻ�!");                	 
                	 getBillCardPanel().getBillModel("checkinv").clearRowData(e.getRow(),null);
                     return;
                 }
                 StringBuffer sql = new StringBuffer()
                 .append(" select newdiscount ")
                 .append(" from eh_firstdiscount a,eh_firstdiscount_b b")
                 .append(" where a.pk_firstdiscount = b.pk_firstdiscount")
                 .append(" and a.def_1 = 'Y'")
                 .append(" and '"+_getDate()+"' between b.zxdate and b.yxdate")
                 .append(" and isnull(b.lock_flag,'N')<>'Y'")
                 .append(" and a.custom = '"+pk_cubasdoc+"' ")
                 .append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"'")
                 .append(" and isnull(a.dr,0)=0")
                 .append(" and isnull(b.dr,0)=0");
                 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
                 try {
					Object objdis = iUAPQueryBS.executeQuery(sql.toString(),new ColumnProcessor());
					UFDouble fisdiscount = new UFDouble(objdis==null?"0":objdis.toString());
					getBillCardPanel().getBillModel("checkinv").setValueAt(fisdiscount, e.getRow(), "def_7");
					UFDouble price = new PubTools().getInvPrice(pk_invbasdoc,_getDate());
	     			getBillCardPanel().getBillModel("checkinv").setValueAt(price, e.getRow(), "def_6");
	     			getBillCardPanel().getBillModel("checkinv").setValueAt(price.sub(fisdiscount), e.getRow(), "jprice");
	     			getBillCardPanel().getBillModel("checkinv").setValueAt(price.sub(fisdiscount).multiply(this.getRate(pk_invbasdoc)), e.getRow(), "bjj");
                 } catch (Exception ex) {
						ex.printStackTrace();
                 }
             }
            super.afterEdit(e);
        }
      /*
       * ע���Զ��尴ť
       * 2008-04-02
       */
      @Override
	protected void initPrivateButton() {
          nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"������ϸ","������ϸ");
//          btn.setOperateStatus(new int[]{OperationState.EDIT,OperationState.FREE});
          addPrivateButton(btn);
          nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.STOCKCHANGE,"�ۿ۱��","�ۿ۱��");
          btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
          addPrivateButton(btn2);
          super.initPrivateButton();
      }

     @Override
      public void mouse_doubleclick(BillMouseEnent arg0) {
          // TODO Auto-generated method stub
          super.mouse_doubleclick(arg0);
          getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(false);
          updateButtons();
      }
     @SuppressWarnings("unchecked")
	public UFDouble getRate(String pk_invbasdoc) throws BusinessException{
     	String sql="select mainmeasrate changerate from bd_convert " +
     			" where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc " +
     			" where pk_invmandoc='"+pk_invbasdoc+"') " +
     			" and nvl(dr,0)=0";
     	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
     	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
     	UFDouble changerate=new UFDouble(-1000000);
     	if(al!=null&&al.size()>0){
 	    	for(int i=0;i<al.size();i++){
 	    		HashMap hm=(HashMap) al.get(i);
 	    		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
 	    	}
     	}
     	return changerate;
     	
     }
}
