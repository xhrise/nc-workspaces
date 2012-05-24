package nc.ui.eh.sc.h0452005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.warehouseinfo.StordocVO;
import nc.vo.eh.sc.h0452005.ScCprkdBVO;
import nc.vo.eh.sc.h0452005.ScCprkdVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵������Ʒ��ⵥ
 * @author ����
 * 2008-05-08 ����02:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {
		
	@SuppressWarnings("deprecation")
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		
		ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			bo.removeAllChildren();
			ButtonObject boUncheck = new ButtonObject("���岻���");
			boUncheck.setTag(String.valueOf(IEHButton.UnCheck));
			boUncheck.setCode("���岻���");
			bo.addChildButton(boUncheck);
		}
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		 switch (intBtn)
	        {
	            case IEHButton.UnCheck:    //���岻���
	                onBoUnCheck();
	                break;
	        }
	}

	//���岻���
    @SuppressWarnings("unchecked")
	private void onBoUnCheck() throws Exception {
    	ScCprkdVO hvo = (ScCprkdVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	String pk_rkd = hvo.getPk_rkd();
    	ScCprkdBVO[] bvos = (ScCprkdBVO[])getBillCardPanelWrapper().getSelectedBodyVOs(); 
    	 if(bvos==null||bvos.length==0){
			 getBillUI().showErrorMessage("��ѡ���������!");
			 return;
		 }
		for(int i=0;i<bvos.length;i++){
			bvos[i].setJcflag("C");
		}
		IVOPersistence  ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
		ivoPersistence.updateVOArray(bvos);
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql2="select count(*) A from eh_sc_cprkd_b where pk_rkd='"+pk_rkd+"' and isnull(dr,0)=0 ";
        ArrayList arrr =(ArrayList)iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor());
        UFDouble A =null;
        if(arrr!=null && arrr.size()>0){
            for(int m=0;m<arrr.size();m++){
                HashMap hmm = (HashMap)arrr.get(m);
                A=new UFDouble(hmm.get("a")==null?"":hmm.get("a").toString());
            }
        }
        String sql3="select count(*) B from eh_sc_cprkd_b where pk_rkd='"+pk_rkd+"' and (jcflag='Y' or jcflag='C') and isnull(dr,0)=0 ";
        ArrayList hr=(ArrayList)iUAPQueryBS.executeQuery(sql3.toString(), new MapListProcessor());
        UFDouble B = null;
        if(hr!=null && hr.size()>0){
            for(int n=0;n<hr.size();n++){
                HashMap hrr = (HashMap)hr.get(n);
                B=new UFDouble(hrr.get("b")==null?"":hrr.get("b").toString());
            }
        }
        if(A.compareTo(B)==0){
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());   
            String SQL2=" update eh_sc_cprkd set jc_falg='Y' where pk_rkd='"+pk_rkd+"' and isnull(dr,0)=0  ";
            pubitf.updateSQL(SQL2);
        }
		onBoRefresh();
	}
    
    
	@Override
	public void onBoSave() throws Exception {
		
		// �Էǿ���֤
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//��д�ĵ��ɹ���
		 AggregatedValueObject agg = getBillUI().getVOFromUI();
		 ScCprkdVO scrpd=(ScCprkdVO) agg.getParentVO();
		 //add by houcq begin 2010-12-02 ���ز���Ϊ0
		 ScCprkdBVO[] sbv = (ScCprkdBVO[]) agg.getChildrenVO();
		 for (int i=0;i<sbv.length;i++)
		 {
			 ScCprkdBVO bv=sbv[i];
			 if ("0.000".equals(bv.getRkmount().toString()))
			 {
				 getBillUI().showErrorMessage("���ر��������");
				 return;
			 }
		 }
	        String pk_pgd=scrpd.getVsourcebillid()==null?"":scrpd.getVsourcebillid().toString();
	        if(!pk_pgd.equals("")){
	        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
	        	
//	        	pubitf.changeFlag("eh_sc_pgd", "rk_flag", "pk_pgd", pk_pgd, 0);
	        	String updateSQL = "update eh_sc_pgd set rk_flag = 'Y' where pk_pgd in "+pk_pgd+"";   //edit by wb at 2008-7-16 19:31:01
	        	pubitf.updateSQL(updateSQL);
	        }else{
	        	// ǰ̨У��
	 		   BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();           
	            int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
	            if(res==1){
	                getBillUI().showErrorMessage("�������ظ���");
	                return;
	            }
	        }
	        
		super.onBoSave();
		
	}
    
    @Override
	public String addCondtion() {
    	// TODO Auto-generated method stub
    	return " vbilltype = '" + IBillType.eh_h0452005 + "' ";
    }
    
	@Override
	protected void onBoDelete() throws Exception {
//		 ��д�ĵ��ɹ���
    	int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
    	if(res==0){
    		return;
    	}
		 AggregatedValueObject agg = getBillUI().getVOFromUI();
		 ScCprkdVO scrpd=(ScCprkdVO) agg.getParentVO();
         String pk_pgd=scrpd.getVsourcebillid()==null?"":scrpd.getVsourcebillid().toString();
	        if(!(pk_pgd==null||pk_pgd.equals(""))){
	        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//	        	pubitf.changeFlag("eh_sc_pgd", "rk_flag", "pk_pgd", pk_pgd, 1);
	        	String updateSQL = "update eh_sc_pgd set rk_flag = 'N' where pk_pgd in "+pk_pgd+"";   //edit by wb at 2008-7-16 19:31:01
	        	pubitf.updateSQL(updateSQL);
	        }
        super.onBoTrueDelete();
	}
    
    @Override
	protected void onBoLineAdd() throws Exception {
        super.onBoLineAdd();
        //����Ʒ��ⵥΪ���Ƶ�ʱ��ѡ���˱�ͷ�Ĳֿ⣬���е�ʱ�Զ��ѱ�ͷ�Ĳֿ���������� add by zqy 2008��10��13��11:24:52
        ScCprkdVO scvo = (ScCprkdVO) getBillUI().getVOFromUI().getParentVO();
        String pk_store = scvo.getPk_store()==null?"":scvo.getPk_store().toString();//�õ���ͷ�Ĳֿ�
        if(pk_store!=null&&pk_store.length()>0){
	        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
	        StordocVO stvo = (StordocVO) iUAPQueryBS.retrieveByPK(StordocVO.class, pk_store);
	        String storname = stvo==null?"":stvo.getStorname()==null?"":stvo.getStorname().toString();
	        int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
	        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_store, row, "pk_store");
	        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(storname, row, "vck");
        }
    }
    @SuppressWarnings("unchecked")
	public void onBoCommit() throws Exception
    {
    	AggregatedValueObject agg = getBillUI().getVOFromUI();
		 ScCprkdVO scrpd=(ScCprkdVO) agg.getParentVO();
		 if (!(scrpd.getPk_rkd()==null|| "".equals(scrpd.getPk_rkd())))
		 {  
			 String pk_rkd = scrpd.getPk_rkd();  ;
			 StringBuilder sb = new StringBuilder()
			 .append(" select e.invcode,e.invname from eh_bom_b a, eh_bom b, eh_sc_cprkd_b c, eh_sc_cprkd d,bd_invbasdoc e,bd_invmandoc f")
			 .append(" where c.pk_invbasdoc = b.pk_invbasdoc and a.pk_invbasdoc=f.pk_invmandoc")
			 .append(" and f.pk_invbasdoc=e.pk_invbasdoc and nvl(e.def8,'N')='Y'")
			 .append(" and a.pk_bom = b.pk_bom and c.pk_rkd = d.pk_rkd")
			 .append(" and d.pk_rkd = '"+pk_rkd+"' and b.ver = c.ver")
			 .append(" and b.pk_corp = d.pk_corp  and nvl(a.dr, 0) = 0")
			 .append(" and nvl(b.dr, 0) = 0 and nvl(c.dr, 0) = 0  and nvl(d.dr, 0) = 0");
			 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			 ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sb.toString(), new MapListProcessor());
			 if (arr.size()>0)
			 {
				 getBillUI().showErrorMessage("ԭ�Ϻ������ѷ������ϣ��޷���⣬���������!");
	             return;
			 }
		 }
    	super.onBoCommit();
    }
    
}
