package nc.ui.eh.stock.z0502505;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.InvdocKCRefModel;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.h08003.CPKcVO;
import nc.vo.eh.kc.h0257005.CalcKcybbBVO;
import nc.vo.eh.stock.z0502505.ProcheckapplyVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：成品检测申请
 * @author 王明
 * 2008-03-24 下午04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

    @Override
	public void onBoSave() throws Exception {
		// 对非空验证
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
//		前台校验
//		   BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
//        
//        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","vsourcebillid"});
//        if(res==1){
//            getBillUI().showErrorMessage("物料有重复！");
//            return;
//        }
		//回写标记到上游（成品入库单）
//		String vsourcebillid=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid")==null?"":
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
//		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//		pubitf.changeFlag("eh_sc_cprkd", "jc_falg", "pk_rkd", vsourcebillid, 0);
        
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        ProcheckapplyVO pvo = (ProcheckapplyVO) aggvo.getParentVO();
        String vsourcebilltype = pvo.getVsourcebilltype() == null ? "" : pvo.getVsourcebilltype();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        //上游成品入库抽样的时候，当表体只抽样一个时候，进行实施判断
         if(vsourcebilltype.equals(IBillType.eh_h0452005)||vsourcebilltype.equals(IBillType.eh_h0256005)){
             int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
             PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());            
//             String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()
//             ==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
             
             for(int i=0;i<row;i++){
                 String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid")==null?"":
                     getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString();//行来源单据PK                
                 String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
                     getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
                 
                 String SQL = " update eh_sc_cprkd_b set jcflag='Y' where pk_invbasdoc='"+pk_invbasdoc+"' and pk_rkd_b='"+vsourcebillid+"'" +
                        " and NVL(dr,0)=0 ";
                 pubitf.updateSQL(SQL);
                 String Sql =" select pk_rkd from eh_sc_cprkd_b where pk_rkd_b='"+vsourcebillid+"' and NVL(dr,0)=0 ";
                 ArrayList ar =(ArrayList)iUAPQueryBS.executeQuery(Sql.toString(), new MapListProcessor());
                 String pk_rkd = null;
                 if(ar!=null && ar.size()>0){
                     for(int j=0;j<ar.size();j++){
                         HashMap hm = (HashMap)ar.get(j);
                         pk_rkd = hm.get("pk_rkd")==null?"":hm.get("pk_rkd").toString();
                     }
                 }
                 String sql2="select count(*) A from eh_sc_cprkd_b where pk_rkd='"+pk_rkd+"' and NVL(dr,0)=0 ";
                 ArrayList arrr =(ArrayList)iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
                 UFDouble A =null;
                 if(arrr!=null && arrr.size()>0){
                     for(int m=0;m<arrr.size();m++){
                         HashMap hmm = (HashMap)arrr.get(m);
                         A=new UFDouble(hmm.get("a")==null?"":hmm.get("a").toString());
                     }
                 }
                 String sql3="select count(*) B from eh_sc_cprkd_b where pk_rkd='"+pk_rkd+"' and  (jcflag='Y' or jcflag='C') and NVL(dr,0)=0 ";
                 ArrayList hr=(ArrayList)iUAPQueryBS.executeQuery(sql3.toString(), new MapListProcessor());
                 UFDouble B = null;
                 if(hr!=null && hr.size()>0){
                     for(int n=0;n<hr.size();n++){
                         HashMap hrr = (HashMap)hr.get(n);
                         B=new UFDouble(hrr.get("b")==null?"":hrr.get("b").toString());
                     }
                 }
                 if(A.compareTo(B)==0){
                     String SQL2=" update eh_sc_cprkd set jc_falg='Y' where pk_rkd='"+pk_rkd+"' and NVL(dr,0)=0  ";
                     pubitf.updateSQL(SQL2);
                 }
             }
             //<修改>pk_cprkds的NULL判断为NULL时修改成''。日期：2009-08-12 下午18:10:18。作者：张志远
             String pk_cprkds = pvo.getPk_cprkds()==null?"''":pvo.getPk_cprkds().toString();//表头来源单据组                        
             String sql = " select count(*) mount from eh_sc_cprkd_b where pk_rkd in ("+pk_cprkds+") and (jcflag='Y' or jcflag='C') and NVL(dr,0)=0 ";
             ArrayList arr =(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
             UFDouble mount =null;
             if(arr!=null && arr.size()>0){
                 for(int j=0;j<arr.size();j++){
                     HashMap hm = (HashMap)arr.get(j);
                     mount=new UFDouble(hm.get("mount")==null?"0":hm.get("mount").toString());
                 }
             }
             String sql2 =" select count(*) mount1 from eh_sc_cprkd_b where pk_rkd in ("+pk_cprkds+") and NVL(dr,0)=0 ";
             ArrayList all =(ArrayList)iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
             UFDouble mount1 =null;
             if(all!=null && all.size()>0){
                 for(int m=0;m<all.size();m++){
                     HashMap hm2 = (HashMap)all.get(m);
                     mount1=new UFDouble(hm2.get("mount1")==null?"0":hm2.get("mount1").toString());
                 }
             }
             if(mount.compareTo(mount1)==0){
                 String SQL2=" update eh_sc_cprkd set jc_falg='Y' where pk_rkd in ("+pk_cprkds+") and NVL(dr,0)=0 "; 
                 pubitf.updateSQL(SQL2);
             }
          
         }
  
		super.onBoSave();
	}

	@Override
	protected void onBoDelete() throws Exception {
//		String vsourcebillid=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid")==null?"":
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
//		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//		pubitf.changeFlag("eh_sc_cprkd", "jc_falg", "pk_rkd", vsourcebillid, 1);
        
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        ProcheckapplyVO pvo = (ProcheckapplyVO) aggvo.getParentVO();
        String vsourcebilltype = pvo.getVsourcebilltype()==null?"":pvo.getVsourcebilltype();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        //上游成品入库抽样的时候，当表体只抽样一个时候，进行实施判断
        if(vsourcebilltype.equals(IBillType.eh_h0452005)||vsourcebilltype.equals(IBillType.eh_h0256005)){
            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());           
//            String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()
//            ==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();           
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            String pk_invbasdoc = null;
            for(int i=0;i<row;i++){
                String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid")==null?"":
                    getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString();
                pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
                    getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
                
                String SQL = " update eh_sc_cprkd_b set jcflag='N' where pk_invbasdoc='"+pk_invbasdoc+"' and pk_rkd_b='"+vsourcebillid+"'" +
                       " and NVL(dr,0)=0 ";
                pubitf.updateSQL(SQL);            
                String Sql =" select pk_rkd from eh_sc_cprkd_b where pk_rkd_b='"+vsourcebillid+"' and NVL(dr,0)=0 ";
                ArrayList ar =(ArrayList)iUAPQueryBS.executeQuery(Sql.toString(), new MapListProcessor());
                String pk_rkd = null;
                if(ar!=null && ar.size()>0){
                    for(int j=0;j<ar.size();j++){
                        HashMap hm = (HashMap)ar.get(j);
                        pk_rkd = hm.get("pk_rkd")==null?"":hm.get("pk_rkd").toString();
                    }
                }
                String sql2="select count(*) A from eh_sc_cprkd_b where pk_rkd='"+pk_rkd+"' and NVL(dr,0)=0 ";
                ArrayList arrr =(ArrayList)iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
                UFDouble A =null;
                if(arrr!=null && arrr.size()>0){
                    for(int m=0;m<arrr.size();m++){
                        HashMap hmm = (HashMap)arrr.get(m);
                        A=new UFDouble(hmm.get("a")==null?"":hmm.get("a").toString());
                    }
                }
                String sql3="select count(*) B from eh_sc_cprkd_b where pk_rkd='"+pk_rkd+"' and jcflag='Y' and NVL(dr,0)=0 ";
                ArrayList hr=(ArrayList)iUAPQueryBS.executeQuery(sql3.toString(), new MapListProcessor());
                UFDouble B = null;
                if(hr!=null && hr.size()>0){
                    for(int n=0;n<hr.size();n++){
                        HashMap hrr = (HashMap)hr.get(n);
                        B=new UFDouble(hrr.get("b")==null?"":hrr.get("b").toString());
                    }
                }
                if(A.compareTo(B)!=0){
                    String SQL2=" update eh_sc_cprkd set jc_falg='N' where pk_rkd='"+pk_rkd+"' and NVL(dr,0)=0  ";
                    pubitf.updateSQL(SQL2);
                }
                
            }
            
            String pk_cprkds=pvo.getPk_cprkds()==null?"":pvo.getPk_cprkds().toString();           
            String sql = " select count(*) mount from eh_sc_cprkd_b where pk_rkd in ("+pk_cprkds+") and (jcflag='Y' or jcflag = 'C') and NVL(dr,0)=0 ";
            ArrayList arr =(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            UFDouble mount =null;//表体已经回写的数量
            if(arr!=null && arr.size()>0){
                for(int j=0;j<arr.size();j++){
                    HashMap hm = (HashMap)arr.get(j);
                    mount=new UFDouble(hm.get("mount")==null?"0":hm.get("mount").toString());
                }
            }
            
            String sql2 =" select count(*) mount1 from eh_sc_cprkd_b where pk_rkd in ("+pk_cprkds+") and NVL(dr,0)=0 ";
            ArrayList all =(ArrayList)iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
            UFDouble mount1 =null;//表体的数量
            if(all!=null && all.size()>0){
                for(int m=0;m<all.size();m++){
                    HashMap hm2 = (HashMap)all.get(m);
                    mount1=new UFDouble(hm2.get("mount1")==null?"0":hm2.get("mount1").toString());
                }
            }
            if(mount1.compareTo(mount)>0){
                String SQL2=" update eh_sc_cprkd set jc_falg='N' where pk_rkd in ("+pk_cprkds+") and NVL(dr,0)=0 "; 
                pubitf.updateSQL(SQL2);
            }       
        }     
		super.onBoDelete();
	}

     @Override
	public void onButton_N(ButtonObject bo, BillModel model) {
            super.onButton_N(bo, model);
            String bocode = bo.getCode();
            // 当从成品入库单生成成品检测时候，物料编码是不允许编辑的,不允许进行行操作
            if (bocode.equals("成品入库单")) {
                int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
                for (int i = 0; i < row; i++) {
                    getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "vinvcode", false);
                }
                  getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
                  getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
                  getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
                  getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
                  getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
            }
            if(bocode.equals("自制单据")){
            	try {
					crateKCDATA();		//创建库存数据 add by wb 2009-9-24 15:04:42
				} catch (Exception e) {
					e.printStackTrace();
				}
            	InvdocKCRefModel kcref = new InvdocKCRefModel();
            	ClientUI.ref.setRefModel(kcref);
            }
            getBillUI().updateUI();
     }
     
     @Override
	protected void onBoElse(int intBtn) throws Exception {
         switch (intBtn)
         {
             case IEHButton.LOCKBILL:    //关闭单据
                 onBoLockBill();
                 break;
             case IEHButton.Prev:    //上一页 下一页
                 onBoBrows(intBtn);
                 break;
             case IEHButton.Next:    //上一页 下一页
                 onBoBrows(intBtn);
                 break;
         }   
     }
     
     private void onBoBrows(int intBtn) throws java.lang.Exception {
         // 动作执行前处理
         buttonActionBefore(getBillUI(), intBtn);
         switch (intBtn) {
         case IEHButton.Prev: {
             getBufferData().prev();
             break;
         }
         case IEHButton.Next: {
             getBufferData().next();
             break;
         }
         }
         // 动作执行后处理
         buttonActionAfter(getBillUI(), intBtn);
         getBillUI().showHintMessage(
                 nc.ui.ml.NCLangRes.getInstance()
                         .getStrByID(
                                 "uifactory",
                                 "UPPuifactory-000503",
                                 null,
                                 new String[] { nc.vo.format.Format
                                         .indexFormat(getBufferData()
                                                 .getCurrentRow()+1) })/*
                                                                      * @res
                                                                      * "转换第:" +
                                                                      * getBufferData().getCurrentRow() +
                                                                      * "页完成)"
                                                                      */
                         );
           setBoEnabled();
     }
      
      @Override
	protected void onBoLockBill() throws Exception{
//        SuperVO parentvo = (SuperVO)getBillUI().getChangedVOFromUI().getParentVO();
//        String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
//             getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        ProcheckapplyVO ivo = (ProcheckapplyVO) aggvo.getParentVO();
        String lock_flag = ivo.getLock_flag()==null?"N":ivo.getLock_flag().toString();
        String primaryKey = ivo.getPrimaryKey();
        if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("该单据已经关闭!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("是否确定进行关闭操作?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                ivo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(ivo);
                getBillUI().showWarningMessage("已经关闭成功");
                onBoRefresh();
            }
            else{
                return;
            }
        }
    }
      
     @Override
    protected void onBoLineAdd() throws Exception {
    	super.onBoLineAdd();
    }
     
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    	AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        ProcheckapplyVO pvo = (ProcheckapplyVO) aggvo.getParentVO();
        String vsourcebilltype = pvo.getVsourcebilltype();
    	if(vsourcebilltype==null||vsourcebilltype.length()==0){
        	InvdocKCRefModel kcref = new InvdocKCRefModel();
        	ClientUI.ref.setRefModel(kcref);
        }
    }
    
    /**
     * 创建库存的数据 供参照时使用
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void crateKCDATA() throws Exception{
    	String pk_corp = _getCorp().getPk_corp();
    	HashMap hmkc = new PubTools().getDateinvKC(null, null,_getDate(), "1",pk_corp);
    	String key = null;
        Iterator iter = hmkc.keySet().iterator();
        ArrayList arr = new ArrayList();
        UFDouble amount = new UFDouble();
        IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
        ivoPersistence.deleteByClause(CPKcVO.class, "pk_corp = '"+pk_corp+"'");		//先删除存在的数据
        while(iter.hasNext()){
        	Object o = iter.next();
            key =o.toString();
            amount = new UFDouble(hmkc.get(key)==null?"0":hmkc.get(key).toString());
            
            CPKcVO kcvo = new CPKcVO();
            kcvo.setPk_invbasdoc(key);
            kcvo.setAmount(amount);
            kcvo.setPk_corp(_getCorp().getPk_corp());
            
            arr.add(kcvo);
        }
        CPKcVO[] kcvos = (CPKcVO[])arr.toArray(new CPKcVO[arr.size()]);
        ivoPersistence.insertVOArray(kcvos);
    }
    
}
