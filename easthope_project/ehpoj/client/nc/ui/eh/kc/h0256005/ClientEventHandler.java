package nc.ui.eh.kc.h0256005;

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
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.kc.h0256005.ScCprkdBVO;
import nc.vo.eh.kc.h0256005.ScCprkdVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：退货入库单
 * @author 张起源
 * 时间:2008-5-27 9:56:12
 */

public class ClientEventHandler extends AbstractEventHandler {
    
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
	ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
	if(bo!=null){
		ButtonObject boUncheck = new ButtonObject("表体不检测");
		boUncheck.setTag(String.valueOf(IEHButton.UnCheck));
		boUncheck.setCode("表体不检测");
		bo.addChildButton(boUncheck);
	}
    }
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		/**
		 * 增减表体不检测功能
		 * 
		 */
		if(IEHButton.UnCheck == intBtn){
		   onBoUnCheck(); 
		}
	}
	
     /**
      * 表体不检测
      * @author 徐命全 创建于： 2009-10-15
      */
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

        // 前台校验
        BillModel bm = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res = new PubTools().uniqueCheck(bm,new String[] { "pk_invbasdoc" });
        if (res == 1) {
            getBillUI().showErrorMessage("退货物料有重复，请核实后操作！");
            return;
        }
        
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        ScCprkdVO svo = (ScCprkdVO) aggvo.getParentVO();
        String vsourcebilltype = svo.getVsourcebilltype();
        if(vsourcebilltype.equals(IBillType.eh_z0207501)){ //来源单据为销售发票时的实施判断
        //判断退货入库数量应小于销售发票上的数量
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
            for (int i = 0; i < row; i++){
                UFDouble rkmount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "rkmount")==null?"0":
                    getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "rkmount").toString());
                
                UFDouble pgmount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pgmount")==null?"0":
                    getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pgmount").toString()); //发票上的数量
                
                if(rkmount.compareTo(pgmount)>0){
                    getBillUI().showErrorMessage("第（"+(i+1)+"行）实际入库数量大于发票数量，不允许操作！");
                    return;
                }
            }            
            
            //判断退货入库单中的实际入库数量不能大于销售发票上的数量
            ScCprkdBVO[]  cpckdBVOs = (ScCprkdBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
            if(cpckdBVOs!=null&&cpckdBVOs.length>0){
                PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                String info = pubItf.checkMount(cpckdBVOs);
                if(info!=null&&info.length()>0){
                    getBillUI().showErrorMessage(info);
                    return ;
                }
            }
        }
        
        if(vsourcebilltype.equals(IBillType.eh_z0207010)){ //来源单据为销售退回时，保存的时候回写个标记到def_2中
            AggregatedValueObject vo = getBillUI().getVOFromUI();
            ScCprkdVO scvo = (ScCprkdVO) vo.getParentVO();
            String vsourcebillid = scvo.getVsourcebillid()==null?"":scvo.getVsourcebillid().toString();
            String SQL = " update eh_backbill set def_2='Y' where pk_backbill='"+vsourcebillid+"' and isnull(dr,0)=0 ";
            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            pubitf.updateSQL(SQL);            
        }
           
        super.onBoSave();
        
    }
    

    @Override
	public String addCondtion() {
    	return " vbilltype = '" + IBillType.eh_h0256005 + "' ";
    }
    
    
    @Override
	public void onBoCommit() throws Exception {
        super.onBoCommit();
        super.setBoEnabled();
    }

    @Override
	public void onButton_N(ButtonObject bo, BillModel model) {

        super.onButton_N(bo, model);
        String bocode = bo.getCode();
        // 当从销售发票生成退货入库单时候，物料名称是不允许编辑的,不允许进行行操作
        if (bocode.equals("销售发票")||bocode.equals("销售退回")) {
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for (int i = 0; i < row; i++) {
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "vinvcode", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "vunit", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "pgmount", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "price", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "def_1", false);
            }
            getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
            getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
            getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
            getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
            getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
        }
        getBillUI().updateUI();
    }
    
    @Override
	protected void onBoDelete() throws Exception {
    	// 回写的到销售退回
	   	int res = onBoDeleteN(); // 1为删除 0为取消删除
	   	if(res==0){
	   		return;
	   	}
	   	AggregatedValueObject aggvo = getBillUI().getVOFromUI();
	    ScCprkdVO svo = (ScCprkdVO) aggvo.getParentVO();
	    String vsourcebilltype = svo.getVsourcebilltype();
	    if(vsourcebilltype.equals(IBillType.eh_z0207010)){ //来源单据为销售退回时，保存的时候回写个标记到def_2中
            AggregatedValueObject vo = getBillUI().getVOFromUI();
            ScCprkdVO scvo = (ScCprkdVO) vo.getParentVO();
            String vsourcebillid = scvo.getVsourcebillid()==null?"":scvo.getVsourcebillid().toString();
            String SQL = " update eh_backbill set def_2='N' where pk_backbill='"+vsourcebillid+"' and isnull(dr,0)=0 ";
            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            pubitf.updateSQL(SQL);            
        }
       super.onBoTrueDelete();
	}  
}
