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
 * ����˵������Ʒ�������
 * @author ����
 * 2008-03-24 ����04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

    @Override
	public void onBoSave() throws Exception {
		// �Էǿ���֤
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
//		ǰ̨У��
//		   BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
//        
//        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","vsourcebillid"});
//        if(res==1){
//            getBillUI().showErrorMessage("�������ظ���");
//            return;
//        }
		//��д��ǵ����Σ���Ʒ��ⵥ��
//		String vsourcebillid=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid")==null?"":
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
//		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//		pubitf.changeFlag("eh_sc_cprkd", "jc_falg", "pk_rkd", vsourcebillid, 0);
        
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        ProcheckapplyVO pvo = (ProcheckapplyVO) aggvo.getParentVO();
        String vsourcebilltype = pvo.getVsourcebilltype() == null ? "" : pvo.getVsourcebilltype();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        //���γ�Ʒ��������ʱ�򣬵�����ֻ����һ��ʱ�򣬽���ʵʩ�ж�
         if(vsourcebilltype.equals(IBillType.eh_h0452005)||vsourcebilltype.equals(IBillType.eh_h0256005)){
             int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
             PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());            
//             String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()
//             ==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
             
             for(int i=0;i<row;i++){
                 String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid")==null?"":
                     getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid").toString();//����Դ����PK                
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
             //<�޸�>pk_cprkds��NULL�ж�ΪNULLʱ�޸ĳ�''�����ڣ�2009-08-12 ����18:10:18�����ߣ���־Զ
             String pk_cprkds = pvo.getPk_cprkds()==null?"''":pvo.getPk_cprkds().toString();//��ͷ��Դ������                        
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
        //���γ�Ʒ��������ʱ�򣬵�����ֻ����һ��ʱ�򣬽���ʵʩ�ж�
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
            UFDouble mount =null;//�����Ѿ���д������
            if(arr!=null && arr.size()>0){
                for(int j=0;j<arr.size();j++){
                    HashMap hm = (HashMap)arr.get(j);
                    mount=new UFDouble(hm.get("mount")==null?"0":hm.get("mount").toString());
                }
            }
            
            String sql2 =" select count(*) mount1 from eh_sc_cprkd_b where pk_rkd in ("+pk_cprkds+") and NVL(dr,0)=0 ";
            ArrayList all =(ArrayList)iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
            UFDouble mount1 =null;//���������
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
            // ���ӳ�Ʒ��ⵥ���ɳ�Ʒ���ʱ�����ϱ����ǲ�����༭��,����������в���
            if (bocode.equals("��Ʒ��ⵥ")) {
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
            if(bocode.equals("���Ƶ���")){
            	try {
					crateKCDATA();		//����������� add by wb 2009-9-24 15:04:42
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
             case IEHButton.LOCKBILL:    //�رյ���
                 onBoLockBill();
                 break;
             case IEHButton.Prev:    //��һҳ ��һҳ
                 onBoBrows(intBtn);
                 break;
             case IEHButton.Next:    //��һҳ ��һҳ
                 onBoBrows(intBtn);
                 break;
         }   
     }
     
     private void onBoBrows(int intBtn) throws java.lang.Exception {
         // ����ִ��ǰ����
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
         // ����ִ�к���
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
                                                                      * "ת����:" +
                                                                      * getBufferData().getCurrentRow() +
                                                                      * "ҳ���)"
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
            getBillUI().showErrorMessage("�õ����Ѿ��ر�!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ�����йرղ���?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                ivo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(ivo);
                getBillUI().showWarningMessage("�Ѿ��رճɹ�");
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
     * ������������ ������ʱʹ��
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
        ivoPersistence.deleteByClause(CPKcVO.class, "pk_corp = '"+pk_corp+"'");		//��ɾ�����ڵ�����
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
