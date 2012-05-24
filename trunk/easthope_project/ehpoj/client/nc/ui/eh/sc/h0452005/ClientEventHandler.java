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
 * 功能说明：成品入库单
 * @author 王明
 * 2008-05-08 下午02:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {
		
	@SuppressWarnings("deprecation")
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		
		ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			bo.removeAllChildren();
			ButtonObject boUncheck = new ButtonObject("表体不检测");
			boUncheck.setTag(String.valueOf(IEHButton.UnCheck));
			boUncheck.setCode("表体不检测");
			bo.addChildButton(boUncheck);
		}
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		 switch (intBtn)
	        {
	            case IEHButton.UnCheck:    //表体不检测
	                onBoUnCheck();
	                break;
	        }
	}

	//表体不检测
    @SuppressWarnings("unchecked")
	private void onBoUnCheck() throws Exception {
    	ScCprkdVO hvo = (ScCprkdVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	String pk_rkd = hvo.getPk_rkd();
    	ScCprkdBVO[] bvos = (ScCprkdBVO[])getBillCardPanelWrapper().getSelectedBodyVOs(); 
    	 if(bvos==null||bvos.length==0){
			 getBillUI().showErrorMessage("请选择表体内容!");
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
		
		// 对非空验证
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//回写的到派工单
		 AggregatedValueObject agg = getBillUI().getVOFromUI();
		 ScCprkdVO scrpd=(ScCprkdVO) agg.getParentVO();
		 //add by houcq begin 2010-12-02 吨重不能为0
		 ScCprkdBVO[] sbv = (ScCprkdBVO[]) agg.getChildrenVO();
		 for (int i=0;i<sbv.length;i++)
		 {
			 ScCprkdBVO bv=sbv[i];
			 if ("0.000".equals(bv.getRkmount().toString()))
			 {
				 getBillUI().showErrorMessage("吨重必须大于零");
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
	        	// 前台校验
	 		   BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();           
	            int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
	            if(res==1){
	                getBillUI().showErrorMessage("物料有重复！");
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
//		 回写的到派工单
    	int res = onBoDeleteN(); // 1为删除 0为取消删除
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
        //当成品入库单为自制的时候，选择了表头的仓库，增行的时自动把表头的仓库带到表体中 add by zqy 2008年10月13日11:24:52
        ScCprkdVO scvo = (ScCprkdVO) getBillUI().getVOFromUI().getParentVO();
        String pk_store = scvo.getPk_store()==null?"":scvo.getPk_store().toString();//得到表头的仓库
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
				 getBillUI().showErrorMessage("原料耗用有已封存的物料，无法入库，请检查后再做!");
	             return;
			 }
		 }
    	super.onBoCommit();
    }
    
}
