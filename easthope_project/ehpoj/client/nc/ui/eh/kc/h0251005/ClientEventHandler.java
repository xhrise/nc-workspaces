package nc.ui.eh.kc.h0251005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.kc.h0251005.ScCkdBVO;
import nc.vo.eh.kc.h0251005.ScCkdVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * ˵�������ϳ��ⵥ 
 * @author ����Դ 
 * ʱ�䣺2008-5-08
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
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
	public void onBoSave() throws Exception {
		// �ǿ��ж�
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		
		 //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("ԭ�ϱ����Ѿ����ڣ������������");
            return;
        }
               
        AggregatedValueObject agg = getBillUI().getVOFromUI();
        ScCkdVO svo = (ScCkdVO) agg.getParentVO();
        String vsourcebilltype  = svo.getVsourcebilltype();
        //�ӳ�Ʒ��ⵥ���ĵ����ڱ����ʱ���д��ǵ�is_fenj�� add by zqy 2008-6-12 10:39:49
        if(vsourcebilltype!=null&&vsourcebilltype.equals(IBillType.eh_h0452005)){
        	PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String pk_rkd = svo.getVsourcebillid();               // ��ⵥ����
            ScCkdBVO[] scBVOs = (ScCkdBVO[])agg.getChildrenVO();
            if(scBVOs!=null&&scBVOs.length>0){
            	int length = scBVOs.length;
            	String[] pk_fjboms = new String[length]; 
            	String pk_fjbom = null;
            	for(int i=0; i<length; i++){
            		ScCkdBVO scbVO = scBVOs[i];
            		pk_fjbom = scbVO.getPk_fjbom();
            		pk_fjboms[i] = pk_fjbom;
            	}
            	pk_fjbom = PubTools.combinArrayToString(pk_fjboms);
            	String sql = "update eh_fjbom set ck_flag = 'Y' where pk_fjbom in "+pk_fjbom;
	            pubItf.updateSQL(sql);
            }
            StringBuffer hxsql = new StringBuffer()
            .append(" select count(*) amount,'A' flag from eh_fjbom where pk_rkd = '"+pk_rkd+"' and isnull(dr,0)=0 ")  // ��ⵥ�ֽ��ԭ������
            .append(" union all")
            .append(" select count(*) amount,'B' flag from eh_fjbom where pk_rkd = '"+pk_rkd+"' and isnull(ck_flag,'N')='Y' and isnull(dr,0)=0 ");  // �Ѿ����������
            IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        	try {
        		ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(hxsql.toString(),new MapListProcessor());
    			if(arr!=null&&arr.size()>0){
    				HashMap hma = new HashMap();
    				for(int i=0; i<arr.size(); i++){
    					HashMap hm=arr.get(i);
    					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
    					UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
    					hma.put(flag, amount);
    				}
    				String updateSQL = null;
    				if(hma.get("A").equals(hma.get("B"))){  //ȫ������ʱ����Ʒ��ⵥ
    					updateSQL = "update eh_sc_cprkd set is_fenj = 'Y' where pk_rkd = '"+pk_rkd+"' and isnull(dr,0)=0";
    				}
    				else{
    					updateSQL = "update eh_sc_cprkd set is_fenj = 'N' where pk_rkd = '"+pk_rkd+"' and isnull(dr,0)=0";
    				}
    				pubItf.updateSQL(updateSQL);
    			}
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
    			
            	
        }
                
		super.onBoSave();
	}
    
    @Override
	protected void onBoDelete() throws Exception {
        if(onBoDeleteN()==0){
           return;
        }       
    	AggregatedValueObject agg = getBillUI().getVOFromUI();
        ScCkdVO svo = (ScCkdVO) agg.getParentVO();
        String vsourcebilltype  = svo.getVsourcebilltype();
        //�ӳ�Ʒ��ⵥ���ĵ����ڱ����ʱ���д��ǵ�is_fenj�� add by zqy 2008-6-12 10:39:56
        if(vsourcebilltype!=null&&vsourcebilltype.equals(IBillType.eh_h0452005)){
        	PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String pk_rkd = svo.getVsourcebillid();               // ��ⵥ����
            ScCkdBVO[] scBVOs = (ScCkdBVO[])agg.getChildrenVO();
            if(scBVOs!=null&&scBVOs.length>0){
            	int length = scBVOs.length;
            	String[] pk_fjboms = new String[length]; 
            	String pk_fjbom = null;
            	for(int i=0; i<length; i++){
            		ScCkdBVO scbVO = scBVOs[i];
            		pk_fjbom = scbVO.getPk_fjbom();
            		pk_fjboms[i] = pk_fjbom;
            	}
            	pk_fjbom = PubTools.combinArrayToString(pk_fjboms);
            	String sql = "update eh_fjbom set ck_flag = 'N' where pk_fjbom in "+pk_fjbom;
	            pubItf.updateSQL(sql);
            }
            String updateSQL = "update eh_sc_cprkd set is_fenj = 'N' where pk_rkd = '"+pk_rkd+"' and isnull(dr,0)=0";
    		pubItf.updateSQL(updateSQL);
    	 }
        super.onBoTrueDelete();
    }
	
	@Override
	public void onBoCommit() throws Exception {
		super.onBoCommit();
		super.setBoEnabled();
	}
	
	@Override
	public void onButton_N(ButtonObject bo, BillModel model) {      
        super.onButton_N(bo, model);
        String bocode=bo.getCode();
        //���ӳ�Ʒ��ⵥ�����������ϳ��ⵥʱ
        int row = getBillCardPanelWrapper().getBillCardPanel().getRowCount();
        if(bocode.equals("��Ʒ��ⵥ")){
        	try {
				HashMap hm = new PubTools().getDateinvKC(null, null, _getDate(), "0", _getCorp().getPk_corp());
	        	for(int i=0;i<row;i++){
	        		 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvcode", false);
	                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"blmount", false);
	                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vunit", false);
	                 //�ӳ�Ʒ�������ֿ�,���
	                 String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").getEditFormulas();//��ȡ�༭��ʽ
	     	        getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
	     	        String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
	     	        	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
	                 String pk_store = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_store")==null?null:
	                 	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_store").toString(); 
	                 UFDouble kcamount = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
	                 getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kcamount, i, "kcamount");
	        	}
        	} catch (Exception e) {
				e.printStackTrace();
			}
        }
       //add by houcq 2010-12-07���ϳ��ⵥ����֮��ֻ������ɾ������
        getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.PasteLinetoTail).setEnabled(false);
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		//getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
		//add by houcq 2010-12-07���ϳ��ⵥ����֮��ֻ������ɾ������
		int row = getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		for(int i=0;i<row;i++){   		 
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"blmount", false);
		}
        getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.PasteLinetoTail).setEnabled(false);
	}
	
	@Override
	public String addCondtion() {
		// TODO Auto-generated method stub
		return "vbilltype = '"+IBillType.eh_h0251005+"'";
	}
	
 }
    
