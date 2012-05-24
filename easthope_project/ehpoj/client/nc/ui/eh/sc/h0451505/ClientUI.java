/**
 * @(#)ClientUI.java	V3.1 2007-3-9
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.sc.h0451505;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.eh.sc.h0451505.ScPgdBVO;
import nc.vo.eh.sc.h0451505.ScPgdPsnVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ����:�ɹ���
 * @author ����
 * 2008��5��7��9:42:04
 */
public class ClientUI extends MultiChildBillManageUI {
	
	public static String pk_unit = null;
	public ClientUI() {
		super();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
   
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBaseBD();
	}


	@Override
	public void setDefaultData() throws Exception {
       //��ͷ���õ�
//        BillCodeObjValueVO objVO = new BillCodeObjValueVO();
//        objVO.setAttributeValue(IBillType.eh_h0451505, getUIControl().getBillType());
//        String billno = BillcodeRuleBO_Client.getBillCode(IBillType.eh_h0451505, _getCorp().getPrimaryKey(), null,
//                                                          objVO);
//        getBillCardPanel().setHeadItem("billno",billno);  
        //�ɹ�����
        getBillCardPanel().setHeadItem("pgdate",_getDate());  
        //��������
        getBillCardPanel().setHeadItem("scdate",_getDate()); 
        //�嵥���
        getBillCardPanel().setHeadItem("cd_flag","N");
        //��ͷ���ù�˾����        
        getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());
        
        //��ͷ��ɾ�����  
        getBillCardPanel().setHeadItem("dr",new Integer(0));
      
        //�ڱ�β�����Ƶ���
        getBillCardPanel().setTailItem("coperatorid",_getOperator());   
       
        //�ڱ�β�����Ƶ�����
        getBillCardPanel().setTailItem("dmakedate",getClientEnvironment().getDate());

        //����״̬
        getBillCardPanel().setHeadItem("vbillstatus",Integer.valueOf(String.valueOf(IBillStatus.FREE)));
        //ҵ������
        BillItem busitype = getBillCardPanel().getHeadItem("pk_busitype");
        if (busitype != null)
            getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
        //��������
        getBillCardPanel().setHeadItem("vbilltype", this.getUIControl().getBillType());
        
        if(ZA44TOZA43DLG.pgdBVOs!=null&&ZA44TOZA43DLG.pgdBVOs.length>0){
        	setBodyData(ZA44TOZA43DLG.pgdBVOs);       // ����������  
        }
        ZA44TOZA43DLG.pgdBVOs = null;
        
        if(ZB32TOZA43DLG.pgdBVOs!=null&&ZB32TOZA43DLG.pgdBVOs.length>0){
        	setBodyData(ZB32TOZA43DLG.pgdBVOs);       // ����������  
        }
        ZB32TOZA43DLG.pgdBVOs = null;
	}
      
	   @Override
	public boolean beforeEdit(BillEditEvent e) {
		String strKey=e.getKey();
		if(strKey.equals("dw")){
			int row=getBillCardPanel().getBillTable().getSelectedRow();
			pk_unit= getBillCardPanel().getBodyValueAt(row,"pk_unit")==null?"":
                        getBillCardPanel().getBodyValueAt(row,"pk_unit").toString();            //��λ
		}
		return super.beforeEdit(e);
	}
	   @SuppressWarnings("unchecked")
	   @Override
        public void afterEdit(BillEditEvent e) {
            // TODO Auto-generated method stub
            String strKey=e.getKey();
            if(e.getPos()==HEAD){
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execHeadFormulas(formual);
            }else if (e.getPos()==BODY){
                String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            }else{
                getBillCardPanel().execTailEditFormulas();
            }
            if(e.getPos()==HEAD&&strKey.equals("pk_team")){                         // ѡ���������������Ա
            	getBillCardPanel().getBillModel("eh_sc_pgd_psn").clearBodyData();
            	String pk_team = getBillCardPanel().getHeadItem("pk_team").getValueObject()==null?null:
            		             getBillCardPanel().getHeadItem("pk_team").getValueObject().toString();
                StringBuffer sql = new StringBuffer()
                .append(" select pk_psndoc,pk_job from eh_bd_team_b ")
                .append(" where pk_team = '").append(pk_team).append("'")
                .append(" and isnull(dr,0)=0");
                try {
                   IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
				   ArrayList<HashMap> arr=(ArrayList<HashMap>)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
				   if(arr!=null&&arr.size()>0){
					   for(int i=0; i<arr.size(); i++){
						   getBillCardPanel().getBillModel("eh_sc_pgd_psn").addLine();
						   HashMap hm=(HashMap)arr.get(i);
						   String pk_job = hm.get("pk_job")==null?"":hm.get("pk_job").toString();
						   String pk_psndoc = hm.get("pk_psndoc").toString();
						   ScPgdPsnVO psnVO = new ScPgdPsnVO();
						   psnVO.setPk_team(pk_team);
						   psnVO.setPk_psndoc(pk_psndoc);
						   psnVO.setPk_job(pk_job);
						   getBillCardPanel().getBillModel("eh_sc_pgd_psn").setBodyRowVO(psnVO, i);
						   String[] formual = getBillCardPanel().getBillModel("eh_sc_pgd_psn").getBodyItems()[1].getEditFormulas();
						   getBillCardPanel().getBillModel("eh_sc_pgd_psn").execFormula(i, formual);
					   }
				   }
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
            }
//            // ѡ��λʱ�Ĵ���
//            if(strKey.equals("dw")){
//            	int row=getBillCardPanel().getBillTable().getSelectedRow();
//            	String pk_unitchange = getBillCardPanel().getBodyValueAt(row,"pk_unit")==null?"":
//                    getBillCardPanel().getBodyValueAt(row,"pk_unit").toString();               //��λ
//            	String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
//                    getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();          //��Ʒ
//            	UFDouble amount = new UFDouble(getBillCardPanel().getBodyValueAt(row,"scmount")==null?"0":
//                    getBillCardPanel().getBodyValueAt(row,"scmount").toString());              //����
//            	StringBuffer sql = new StringBuffer()
//                .append(" select c.pk_measdoc,a.price,b.changerate from eh_invbasdoc a,eh_invbasdoc_b b,bd_measdoc c")
//                .append(" where a.pk_invbasdoc = b.pk_invbasdoc")
//                .append(" and b.pk_measdoc = c.pk_measdoc")
//                .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 ")
//                .append(" union all ")
//                .append(" select c.pk_measdoc,a.price,1 changerate")
//                .append(" from  eh_invbasdoc a,bd_measdoc c")
//                .append(" where a.pk_measdoc = c.pk_measdoc")
//                .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 and isnull(c.dr,0)=0");
//
//                IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//                try {
//        		    HashMap hm = new HashMap();
//                	Vector vc = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
//        		    if(vc!=null&&vc.size()>0){
//        		    	for(int i=0; i<vc.size(); i++){
//        		    		Vector vcc = (Vector)vc.get(i);
//        		    		String pk_measdoc = vcc.get(0)==null?"":vcc.get(0).toString();
//        		    		UFDouble oldprice = new UFDouble(vcc.get(1)==null?"0":vcc.get(1).toString());
//        		    		UFDouble changerate = new UFDouble(vcc.get(2)==null?"0":vcc.get(2).toString());
//        		    		OrderBVO orderVO = new OrderBVO();
//        		    		orderVO.setPrice(oldprice);
//        		    		orderVO.setAmount(changerate);
//        		    		hm.put(pk_measdoc, orderVO);
//        		    	}
//        		      if(hm.containsKey(pk_unitchange)){                 // �д˵�λʱ���л����Ƽ�
//        		    	  OrderBVO orVOchange = (OrderBVO)hm.get(pk_unitchange);
//        		    	  UFDouble rate1 = orVOchange.getAmount();           //Ҫ����ĵ�λ�Ļ���
//        		    	  OrderBVO orVO = (OrderBVO)hm.get(pk_unit);
//        		    	  UFDouble rate2 = orVO.getAmount();                 //����֮ǰ�ĵ�λ�����������������λ
//        		    	  UFDouble rate = rate1.div(rate2);
//        		    	  UFDouble amount2 = amount.multiply(rate);          // ����֮�������
//        		    	  getBillCardPanel().setBodyValueAt(amount2, row, "scmount");
//        		      }else{
//        		    	  showErrorMessage("��"+(row+1)+"��������û�д˸�����λ!");
//        		    	  getBillCardPanel().setBodyValueAt(pk_unit, row, "pk_unit");
//        		    	  String[] formual=getBillCardPanel().getBodyItem("dw").getLoadFormula();//��ȡ��ʾ��ʽ
//        	              getBillCardPanel().execBodyFormulas(row,formual); 
//        		      }
//        		    }else{
//        		    	showErrorMessage("��"+(row+1)+"�����ϵ�����û�д˸�����λ!");
//        		    	getBillCardPanel().setBodyValueAt(pk_unit, row, "pk_unit");
//      		    	    String[] formual=getBillCardPanel().getBodyItem("dw").getLoadFormula();//��ȡ��ʾ��ʽ
//      	                getBillCardPanel().execBodyFormulas(row,formual);
//        		    }
//                } catch (BusinessException e1) {
//        			e1.printStackTrace();
//        		}
//            }
            
            if(strKey.equals("vinvbascode")){  // ѡ���Ʒʱ�����BOM�汾
            	int row=getBillCardPanel().getBillTable().getSelectedRow();
            	String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                    getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();          //��Ʒ
            	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        		String sql="select ver from eh_bom where pk_invbasdoc='"+pk_invbasdoc+"' and isnull(dr,0)=0 and sc_flag='Y' ";
        		UFDouble[] unrksc = getUnrkUnsc(pk_invbasdoc);		//�õ�����δ�������������δ��������
        		if(unrksc!=null&&unrksc.length==2){
        			UFDouble scunrk  = unrksc[0];				//����δ�������
        			UFDouble cjunsc  = unrksc[1];				//����δ��������
        			getBillCardPanel().setBodyValueAt(scunrk, row, "scunrk"); 
        			getBillCardPanel().setBodyValueAt(cjunsc, row, "cjunsc");
        		}
        		try {
					Object verObj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
				    if(verObj!=null){
				    	String ver = verObj.toString();
				    	getBillCardPanel().setBodyValueAt(ver, row, "ver");  // BOM�汾
				    }
				    //���
				    //UFDouble kcamount = new PubTools().getKCamountByinv_Back(pk_invbasdoc,  _getCorp().getPk_corp(),_getDate());
				  //modify by houcq 2011-06-20�޸�ȡ��淽��
		    		UFDouble kcamount = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);
	                getBillCardPanel().setBodyValueAt(kcamount, row, "kcamount");
        		} catch (BusinessException e1) {
					e1.printStackTrace();
				}
            }
            
//            // ��д�����ջ�������ʵʱ�������ջ���(�����������ջ���) add by wm at 2008-5-26 10:36:30
//	         int flag = ClientEventHandler.flag;          // �������ӱ��
//	         if(e.getKey().equals("pgamount")&&flag==2){  // ���Ӳɹ���ͬ����ʱ�������ջ���
//	        	 int row = e.getRow();
//	        	 String vsourcebillid = getBillCardPanel().getBodyValueAt(row, "vsourcebillid")==null?null:
//	        		                   getBillCardPanel().getBodyValueAt(row, "vsourcebillid").toString();
//	        	 String pk_pgd_b = getBillCardPanel().getBodyValueAt(row, "pk_pgd_b")==null?null:
//	                                   getBillCardPanel().getBodyValueAt(row, "pk_pgd_b").toString();  // �ӱ�����
//                //ʵʱ�����ջ���(�����������ջ���)
//	        	 UFDouble amount = PubTools.calTotalamount("eh_sc_pgd_b", "pgamount", vsourcebillid, "pk_pgd_b", pk_pgd_b);
//	        	 getBillCardPanel().setBodyValueAt(amount, row, "ysamount");
//	         }
	         //end
	         
	         //��ʱ�䣨��ʼʱ�䣩
	         if(strKey.equals("starttime")&&e.getPos()==HEAD){
	        	 String starttime=getBillCardPanel().getHeadItem("starttime").getValueObject()==null?"":
	        		 getBillCardPanel().getHeadItem("starttime").getValueObject().toString();
	        	 int rows=getBillCardPanel().getBillTable().getRowCount();
	        	 for(int i=0;i<rows;i++){
	        		 getBillCardPanel().setBodyValueAt(starttime, i, "starttime");
	        		 
	        	 }
	         }
	         //��ʱ�䣨����ʱ�䣩
	         if(strKey.equals("endtime")&&e.getPos()==HEAD){
	        	 String endtime=getBillCardPanel().getHeadItem("endtime").getValueObject()==null?"":
	        		 getBillCardPanel().getHeadItem("endtime").getValueObject().toString();
	        	 int rows=getBillCardPanel().getBillTable().getRowCount();
	        	 for(int i=0;i<rows;i++){
	        		 getBillCardPanel().setBodyValueAt(endtime, i, "endtime");
	        	 }
	         }
            
            super.afterEdit(e);
        }
     
      /*
       * ע���Զ��尴ť
       * 2008��5��7��13:42:54
       */
      protected void initPrivateButton() {
          nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.SENDFA,"�´�","�´�");
          btn1.setOperateStatus(new int[]{IBillOperate.OP_NOADD_NOTEDIT});
          addPrivateButton(btn1);
          nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"��һҳ","��һҳ");
          btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
          addPrivateButton(btnPrev);
          nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"��һҳ","��һҳ");
          btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
          addPrivateButton(btnNext);
          nc.vo.trade.button.ButtonVO btn4 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
          btn4.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
          addPrivateButton(btn4);
          nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.BusinesBtn,"ҵ�����","ҵ�����");
          btnBus.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
          addPrivateButton(btnBus);
      }

	@Override
	protected void initSelfData() {
         //������
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	    
	}
	
	/**
	 * ˵�������δ����ı�������(�����Ƕ�ҳǩ�������޷�ֱ��ֱ��ͨ�����δ�������)
	 * @param pgdBVOs
	 */
	public void setBodyData(ScPgdBVO[] pgdBVOs){
		String[] devices = getDefaultdevice();
		for(int i=0; i<pgdBVOs.length; i++){
            getBillCardPanel().getBillModel("eh_sc_pgd_b").addLine();
			ScPgdBVO pgdBVO = pgdBVOs[i];
			getBillCardPanel().getBillModel("eh_sc_pgd_b").setBodyRowVO(pgdBVO, i);
			String[] formual=getBillCardPanel().getBodyItem("pk_invbasdoc").getEditFormulas();//��ȡ�༭��ʽ
        	getBillCardPanel().execBodyFormulas(i,formual);
//        	String[] formual2=getBillCardPanel().getBodyItem("hjinvbasdoc").getEditFormulas();//��ȡ�༭��ʽ
//        	getBillCardPanel().execBodyFormulas(i,formual2);
//        	String[] loadformual=getBillCardPanel().getBodyItem("vposmcode").getLoadFormula();//��ȡ�༭��ʽ
//        	getBillCardPanel().execBodyFormulas(i,loadformual);
        	
        	/***����Ĭ�ϻ���  wb 2009��4��1��10:59:023**/
        	if(devices!=null&&devices.length==2){
        		getBillCardPanel().setBodyValueAt(devices[0], i, "vdevicename");
        		getBillCardPanel().setBodyValueAt(devices[1], i, "def_1");
        	}
        	/*************** end *******************/
       }
		 updateUI();
	 }
	
	/***
	 * �õ����ϵ�����δ�������������δ��������
	 * @param pk_invbasdoc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UFDouble[] getUnrkUnsc(String pk_invbasdoc){
		UFDouble[] unrksc = new UFDouble[2];
		String pk_corp = _getCorp().getPk_corp();
		StringBuffer sql = new StringBuffer()
		.append(" select sum(isnull(a.pgamount,0)) pgamount,sum(isnull(a.scamount,0)) scamount")
		.append(" from ")
//		.append(" ---����δ�������")
		.append(" (select b.pk_invbasdoc,sum(isnull(b.pgamount,0)) pgamount,0 scamount")
		.append(" from eh_sc_pgd a,eh_sc_pgd_b b")
		.append(" where a.pk_pgd = b.pk_pgd")
		.append(" and isnull(a.lock_flag,'N') <> 'Y'")
		.append(" and isnull(a.rk_flag,'N')<>'Y'")
		.append(" and isnull(a.xdflag,'N')='Y'")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		.append(" and isnull(a.dr,0) = 0")
		.append(" and isnull(b.dr,0) = 0")
		.append(" group by b.pk_invbasdoc")
		.append(" union all ")
//		.append(" ---����δ��������")
		.append(" select b.pk_invbasdoc,0 pgamount,sum(isnull(b.scmount,0)) scamount")
		.append(" from eh_sc_posm a,eh_sc_posm_b b")
		.append(" where a.pk_posm = b.pk_posm")
		.append(" and isnull(a.pg_flag,'N') <> 'Y'")
		.append(" and isnull(b.pg_flag,'N') <> 'Y'")
		.append(" and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		.append(" and isnull(a.dr,0) = 0")
		.append(" and isnull(b.dr,0) = 0")
		.append(" group by b.pk_invbasdoc")
		.append(" ) a");
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
    		ArrayList  arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
    		if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				unrksc[0] = new UFDouble(hmA.get("pgamount")==null?"0":hmA.get("pgamount").toString());			//����δ�������
				unrksc[1] = new UFDouble(hmA.get("scamount")==null?"0":hmA.get("scamount").toString());			//����δ��������
    		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return unrksc;
	}
	
	//��Ĭ�ϻ��� add by wb 2009-4-1 10:51:23
    @SuppressWarnings("unchecked")
	public  String[] getDefaultdevice(){
    	String[]  device = new String[2];
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql = " select deviname,pk_device from eh_bd_device where isdefault = 'Y' and pk_corp = '"+_getCorp().getPk_corp()+"' and  isnull(dr,0)=0 ";
        try {
            ArrayList arr =(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                HashMap hm = (HashMap)arr.get(0);
                device[0] = hm.get("deviname")==null?"":hm.get("deviname").toString();
                device[1] = hm.get("pk_device")==null?"":hm.get("pk_device").toString();
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }       
        return device;        
    }
    
}
