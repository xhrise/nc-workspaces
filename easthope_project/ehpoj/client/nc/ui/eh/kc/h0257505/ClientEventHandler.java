/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.kc.h0257505;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.IPubInterface;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.kc.h0257505.StoreCheckBVO;
import nc.vo.eh.kc.h0257505.StoreCheckVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


/**
 * ����:ԭ���̵㵥
 * ZB01
 * @author WB
 * 2008-10-15 16:30:09
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.GENRENDETAIL:  //������ϸ
            	onBoGENRENDETAIL();
                break;
        }
        super.onBoElse(intBtn);
    }
   
    //���ݲֿ����ɸòֿ����ϸ
    @SuppressWarnings("unchecked")
	private void onBoGENRENDETAIL() throws Exception{
    	StoreCheckVO hvo = (StoreCheckVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	String pk_store = hvo.getPk_store();
    	if(pk_store==null||pk_store.length()==0){
    		getBillUI().showErrorMessage("��ѡ��ֿ�!");
    		return;
    	}
    	int rowss = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        if(rowss>0){
            int iRet = getBillUI().showYesNoMessage("������ϸǰҪ��ձ��壬�Ƿ�ȷ��?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            	//��ղ�Ʒ��Χ
                int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
                int[] rows=new int[rowcount];
                for(int i=rowcount - 1;i>=0;i--){
                    rows[i]=i;
                }
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
            }
        }
        HashMap hm = new PubTools().getInvKcAmountByStore( _getCorp().getPk_corp(),hvo.getCheckdate(), pk_store);
        if (hm.size()==0)
        {
        	getBillUI().showErrorMessage("û�����ϣ�");
            return;
        }
        Iterator iter = hm.keySet().iterator();
        String pk_invbasdoc = null;
        int i= 0;
        while(iter.hasNext()){ 					//�ɱ�����Ϊ����,����ļ���
        	getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
            Object o = iter.next();
            pk_invbasdoc =o.toString();
            UFDouble kc  = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(pk_invbasdoc, i, "pk_invbasdoc");
    		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(kc, i, "totalnum");
            String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getItemByKey("vinvcode").getLoadFormula();//��ȡ��ʾ��ʽ
            getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
            i++;
        }        
           
      }
    	

	@Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	 //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("��������ͬ���ϴ��ڣ������������");
            return;
        }
        StoreCheckBVO[] bvos = (StoreCheckBVO[])getBillUI().getVOFromUI().getChildrenVO();
        if(bvos!=null&&bvos.length>0){
        	UFDouble pdbl = new UFDouble(IPubInterface.PDRATE.toDouble(),2);					//�̵�������
            for(int i=0;i<bvos.length;i++){
            	UFDouble kcamount = bvos[i].getTotalnum();			//�������
            	UFDouble pdamount = bvos[i].getInum();				//�̵�����
            	String memo = bvos[i].getMemo();					//��ע
            	boolean isOver = isOverPdbl(kcamount, pdamount, pdbl);
            	if(isOver){
            		if(memo==null||memo.length()==0){
	            		getBillUI().showErrorMessage("��"+(i+1)+"�������̵�������"+(pdbl.multiply(100).toDouble())+"%,����д��ע!");
	            		return;
            		}
            		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("overstep", "Y");		//���г�������ʱ������������
            	}else{
            		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("overstep", null);
            	}
            }
        }
        super.onBoSave();
    }
 
    @Override
    public String addCondtion() {
    	// TODO Auto-generated method stub
    	return " vbilltype = '" + IBillType.eh_h0257505 + "' ";
    }
    
    /**
     * �ҵ���Ӧ�ڼ������̵������
     * @param pk_period
     * @param pk_check
     * @return
     */
    @SuppressWarnings("unchecked")
	public HashMap getInvChecked(String pk_period,String pk_check){
    	HashMap hm = new HashMap();
    	StringBuffer sql = new StringBuffer()
    	.append(" select b.pk_invbasdoc from eh_store_check a,eh_store_check_b b")
    	.append(" where a.pk_check = b.pk_check")
    	.append(" and a.pk_period = '"+pk_period+"' and a.pk_check <> '"+pk_check+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0");
    	try{
	    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
	    	ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	    	if(arr!=null&&arr.size()>0){
	    		UFDouble kcamount = new UFDouble(0);				//�������
	    		for(int i=0;i<arr.size();i++){
	    			HashMap hmA = (HashMap)arr.get(i);
	    			String pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
	    			hm.put(pk_invbasdoc, kcamount);
	    		}
	    	}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return hm;
    }
    
    /**
     * �Ƿ񳬳��̵����
     * @param kcamount
     * @param pdamount
     * @param pdbl
     * @return
     */
    public boolean isOverPdbl(UFDouble kcamount,UFDouble pdamount,UFDouble pdbl){
    	boolean isOver = false;
    	UFDouble cy = kcamount.sub(pdamount).abs();
    	UFDouble bl = cy.div(pdamount);
    	isOver = bl.sub(pdbl).toDouble()>0;
    	return isOver;
    }
    
    /***
     * ԭ��
     * @return
     */
    public String getIntype(){
    	return "0";
    }
    /*
     * "1�����������ϸ����
     *  2������ȡ����ʽ�� ���ݱ�ͷ�Ĳֿ��ֶΣ����ҵ�¼�����·ݵ����µĴ����δ������е���δ�����еĲ�
     *  ��Ϊ��ͷ�ֿ������+�ɹ��������⡢��Ʒ��ⵥ�����ϵ����������ͷ�Ĳֿ�=�̵���ͷ�Ĳֿ������+���ϳ��ⵥ��
     *  ��Ʒ���ⵥ�����ϵ�����������ͷ�Ĳֿ�=�̵���ͷ�Ĳֿ������" 
     */   
    public String getInvKc(String pk_corp,UFDate calcdate,String pk_store){ 
    	int nmonth = calcdate.getMonth();
		int nyear = calcdate.getYear();
		int pyear = nyear;
		int pmonth =0;
        if(nmonth==1){
            pmonth=12;
            pyear = nyear - 1;
        }else
            pmonth = nmonth - 1;
        String beginDate = calcdate.toString().substring(0,8)+"01";
		StringBuffer sb  =  new StringBuffer()
		.append(" select distinct pk_invbasdoc,pk_invbasdoc from (" )
		//�ڳ��������
		.append(" select b.pk_invbasdoc pk_invbasdoc")
		.append(" from eh_calc_kcybb a,eh_calc_kcybb_b b")	
		.append(" where a.pk_kcybb = b.pk_kcybb and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")		
		.append(" and a.nyear="+pyear)
		.append(" and a.nmonth="+pmonth)
		.append(" and b.pk_store='"+pk_store+"'")
		//--��Ӫ�ɹ����
		.append(" union all")
		.append(" select b.pk_invbasdoc pk_invbasdoc")
		.append(" from eh_stock_in a, eh_stock_in_b b")
		.append(" where a.pk_in = b.pk_in")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.pk_stock='"+pk_store+"'")
		.append(" union all")	
		//--��Ʒ���
		.append(" select b.pk_invbasdoc pk_invbasdoc")
		.append(" from eh_sc_cprkd a, eh_sc_cprkd_b b")        
		.append(" where a.pk_rkd = b.pk_rkd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.pk_store='"+pk_store+"'")
		.append(" union all")
		//--���ϵ���������
		.append(" select b.pk_dr_inv pk_invbasdoc")
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")		
		.append(" where a.pk_dbd = b.pk_dbd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.vbillstatus = 1")
		.append(" and a.pk_dr_store='"+pk_store+"'")
		.append(" union all")
		//--���ϵ���������
		.append(" select b.pk_dc_inv pk_invbasdoc")
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")
		.append(" where a.pk_dbd = b.pk_dbd")		
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.vbillstatus = 1")
		.append(" and a.pk_dc_store='"+pk_store+"'")
		.append(" union all")
		//--���ϳ���
		.append(" select b.pk_invbasdoc pk_invbasdoc")	
		.append(" from eh_sc_ckd a, eh_sc_ckd_b b")
		.append(" where a.pk_ckd = b.pk_ckd").append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.pk_store='"+pk_store+"'")
		.append(" union all")	
		//-- ��Ʒ����
		.append(" select b.pk_invbasdoc pk_invbasdoc")
		.append(" from eh_icout a, eh_icout_b b")
		.append(" where a.pk_icout = b.pk_icout")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.pk_stock='"+pk_store+"'")
		.append(" )");		 
		return sb.toString();
    }
    
}

