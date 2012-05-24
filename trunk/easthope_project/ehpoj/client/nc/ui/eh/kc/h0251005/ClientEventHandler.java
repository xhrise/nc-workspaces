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
 * 说明：材料出库单 
 * @author 张起源 
 * 时间：2008-5-08
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn){
         case IEHButton.LOCKBILL:    //关闭单据
             onBoLockBill(); 
             break;
		}
		super.onBoElse(intBtn);
	}

	@Override
	public void onBoSave() throws Exception {
		// 非空判断
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		
		 //唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("原料编码已经存在，不允许操作！");
            return;
        }
               
        AggregatedValueObject agg = getBillUI().getVOFromUI();
        ScCkdVO svo = (ScCkdVO) agg.getParentVO();
        String vsourcebilltype  = svo.getVsourcebilltype();
        //从成品入库单来的单据在保存的时候回写标记到is_fenj中 add by zqy 2008-6-12 10:39:49
        if(vsourcebilltype!=null&&vsourcebilltype.equals(IBillType.eh_h0452005)){
        	PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String pk_rkd = svo.getVsourcebillid();               // 入库单主键
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
            .append(" select count(*) amount,'A' flag from eh_fjbom where pk_rkd = '"+pk_rkd+"' and isnull(dr,0)=0 ")  // 入库单分解的原料总数
            .append(" union all")
            .append(" select count(*) amount,'B' flag from eh_fjbom where pk_rkd = '"+pk_rkd+"' and isnull(ck_flag,'N')='Y' and isnull(dr,0)=0 ");  // 已经出库的总数
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
    				if(hma.get("A").equals(hma.get("B"))){  //全部出库时将成品入库单
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
        //从成品入库单来的单据在保存的时候回写标记到is_fenj中 add by zqy 2008-6-12 10:39:56
        if(vsourcebilltype!=null&&vsourcebilltype.equals(IBillType.eh_h0452005)){
        	PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String pk_rkd = svo.getVsourcebillid();               // 入库单主键
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
        //当从成品入库单生成生产材料出库单时
        int row = getBillCardPanelWrapper().getBillCardPanel().getRowCount();
        if(bocode.equals("成品入库单")){
        	try {
				HashMap hm = new PubTools().getDateinvKC(null, null, _getDate(), "0", _getCorp().getPk_corp());
	        	for(int i=0;i<row;i++){
	        		 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvcode", false);
	                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"blmount", false);
	                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vunit", false);
	                 //从成品入库带出仓库,库存
	                 String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").getEditFormulas();//获取编辑公式
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
       //add by houcq 2010-12-07材料出库单生成之后只允许行删除操作
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
		//add by houcq 2010-12-07材料出库单生成之后只允许行删除操作
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
    
