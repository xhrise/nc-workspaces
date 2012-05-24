
package nc.ui.eh.sc.h0451505;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IVOPersistence;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.sc.h0451505.ScPgdVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 功能说明：派工单
 * @author 王兵
 * 2008-5-7 11:36:45
 */

public class ClientEventHandler extends AbstractEventHandler {
    
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
    
     @Override
	protected void onBoElse(int intBtn) throws Exception {
            switch (intBtn)
            {
                case IEHButton.SENDFA:    //下达
                    onBoSendFa();
                    break;
            }   
            super.onBoElse(intBtn);
        }
     
        
        
		private void onBoSendFa() throws Exception {
			String xdflag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("xdflag").getValueObject()==null?"N":
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("xdflag").getValueObject().toString();
			String lockflag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lockflag").getValueObject()==null?"N":
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lockflag").getValueObject().toString();
			try {
				ScPgdVO pgdVO = (ScPgdVO)getBillUI().getChangedVOFromUI().getParentVO();
				if(pgdVO==null){
					getBillUI().showErrorMessage("请选择一张单据!");
			       return;
				}
				if(lockflag.equals("true")){
				       getBillUI().showErrorMessage("该单据已经关闭,不能下达!");
				       return;
				}
				if(xdflag.equals("true")){
			       getBillUI().showErrorMessage("该单据已经下达!");
			       return;
			    }
			   else if(pgdVO!=null){
			       int iRet = getBillUI().showYesNoMessage("是否确定下达此计划单?");
			       if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
			    	    pgdVO.setXdflag(new UFBoolean(true));
				       	IVOPersistence ivopersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
				       	ivopersistence.updateVO(pgdVO);
				       	//在下达的时候关闭上游生产任务单 add by wb at 2008-9-3 11:20:44
				       	String vsourcebillid = pgdVO.getVsourcebillid();
				       	if(vsourcebillid!=null&&vsourcebillid.length()>0){
					       	String pk_pgd = pgdVO.getPk_pgd();
					       	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
					       	pubItf.closePosmBill(pk_pgd);
				       	}
				       	getBillUI().showWarningMessage("已经下达成功");
				        onBoRefresh();
			       }
			   }
			} catch (Exception e) {
				e.printStackTrace();
			}
			setBoEnabled();
	   }
        
        @Override
		public void onBoSave() throws Exception {
    		//非空判断
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();  
            //表体项次唯一性校验
            BillModel bm1=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b");
            int res1=new PubTools().uniqueCheck(bm1, new String[]{"pk_invbasdoc"});
            String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?null:
            		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
            if(vsourcebillid==null&&res1==1){    // 自制时判断是否有同一物料
                getBillUI().showErrorMessage("子表中有相同物料，不允许操作！");
                return;
            }        
            BillModel bm2=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_psn");
            int res2=new PubTools().uniqueCheck(bm2, new String[]{"pk_psndoc"});
            if(res2==1){
                getBillUI().showErrorMessage("班组人员子表中有相同人员，不允许操作！");
                return;
            } 
            ScPgdVO hvo = (ScPgdVO) (getBillCardPanelWrapper().getBillVOFromUI()).getParentVO();
            boolean cd_flag = hvo.getCd_flag()==null?false:hvo.getCd_flag().booleanValue();
            if(!cd_flag){
            	if (getBillUI().showYesNoMessage("该计划单不是插单,是否继续保存该单据?") != UIDialog.ID_YES) {
                    return;
                }
            }
            int ret= getBillUI().getBillOperate();
            //编辑保存时不处理
			if (ret==3)
			{
				if(vsourcebillid!=null&&vsourcebillid.length()>0){		
		   			//如果班次和使用次数相等,则打上sc_flag标记,使用次数+1
		    			String updateSql = " update eh_sc_mrp set ysycs = nvl(ysycs,0)+1 where pk_mrp in ("+vsourcebillid+")";
		    			String updateSql2 = " update eh_sc_mrp set sc_flag = 'Y' where pk_mrp in ("+vsourcebillid+") and nvl(bc,1)=nvl(ysycs,0)" ;
		    			PubItf pubItf2 = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		    			pubItf2.updateSQL(updateSql);
		    			pubItf2.updateSQL(updateSql2);
		    		}
		            setBoEnabled();
			}
			super.onBoSave();
    	}
        

        @Override
        protected void onBoDelete() throws Exception {
        	int res = onBoDeleteN(); // 1为删除 0为取消删除
        	if(res==0){
        		return;
        	}
        	ScPgdVO hvo = (ScPgdVO) (getBillCardPanelWrapper().getBillVOFromUI()).getParentVO();
    		String vsourcebillid = hvo.getVsourcebillid()==null?null:hvo.getVsourcebillid().toString();
    		if(vsourcebillid!=null&&vsourcebillid.length()>0){		//关闭上游生产订单
//    			String updateSql = " update eh_sc_posm set pg_flag = 'N' where pk_posm in ("+vsourcebillid+")";
//    			PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//    			pubItf.updateSQL(updateSql);
//    			
    			String updateSql2 = " update eh_sc_mrp set sc_flag = 'N' where pk_mrp in ("+vsourcebillid+")";
    			PubItf pubItf2 = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
    			pubItf2.updateSQL(updateSql2);
    		}
        	super.onBoTrueDelete();
        	
    	}
        
        @Override
		protected void onBoLineAdd() throws Exception {
        	super.onBoLineAdd();
        	String currentcode = getBillCardPanelWrapper().getBillCardPanel().getCurrentBodyTableCode();
        	if(currentcode!=null&&("eh_sc_pgd_b".equals(currentcode))){
	        	int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
	        	String starttime = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("starttime").getValueObject()
	        					==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("starttime").getValueObject().toString();
	        	String endtime = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("endtime").getValueObject()
	        					==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("endtime").getValueObject().toString();
	        	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(starttime, row, "starttime");
	        	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(endtime, row, "endtime");
	        	
	        	/***加上默认机组  wb 2009年4月1日10:59:023**/
	        	String[] devices = ((ClientUI)getBillUI()).getDefaultdevice();
	        	if(devices!=null&&devices.length==2){
	        		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(devices[0], row, "vdevicename");
	        		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(devices[1], row, "def_1");
	        	}
	        	/*************** end *******************/
        	}
        }
    	
    	@Override
		public void onButton_N(ButtonObject bo, BillModel model) {
            
            super.onButton_N(bo, model);
            String bocode=bo.getCode()==null?"":bo.getCode();
//            String pk_corp = _getCorp().getPk_corp();
            //当从抽样单生成检测报告单时候，抽样单号是不允许编辑，同时表体不能进行行操作
            if(bocode.equals("生产订单")){
              int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
              for(int i=0;i<row;i++){
                  getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b").setCellEditable(i,"vinvbascode", false);
                  getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b").setCellEditable(i,"vhjl", false);
//                  String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
//                	  getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
//                  UFDouble kcamount = new PubTools().getKCamountByinv(pk_invbasdoc, pk_corp);
//                  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kcamount, i, "kcamount");
//                  UFDouble pgamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pgamount")==null?"":
//  					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pgamount").toString());		//本次派工数量 = 生产任务数量-已派工数量 
//                  /**加上生产未入库数量及车间未生产数量 add by wb at 2008-10-21 17:36:39*/
//                  UFDouble[] unrksc = ((ClientUI)getBillUI()).getUnrkUnsc(pk_invbasdoc);		//得到生产未入库数量及车间未生产数量
//	          	  if(unrksc!=null&&unrksc.length==2){
//	          			UFDouble scunrk  = unrksc[0];				//生产未入库数量
//	          			UFDouble cjunsc  = unrksc[1];				//车间未生产数量
//	          			UFDouble needpg = pgamount.sub(scunrk).add(cjunsc);			//派工数量=本次派工数量-生产未入库数量+车间未生产数量
//	          			//如果派工数量<零，派工数量取零不安排生产并可修改；如果派工数量〉零，派工数量取正数安排生产并可修改
//	          			if(needpg.toDouble()<0){
//	          				pgamount = new UFDouble(0);
//	          				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("本物料不需要做派工", i, "memo");
//	          				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", i, "def_1");
//	                   		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "pgamount", false);
//	          			}
//	          			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(scunrk, i, "scunrk"); 
//	          			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(cjunsc, i, "cjunsc");
//	          			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pgamount, i, "pgamount");
//	          	  }
                  String[] formual= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvbascode").getEditFormulas();//获取编辑公式
                  getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
              	}
              getBillUI().updateUI();
            }
            
            if(bocode.equals("MRP运算")){
                int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
                for(int i=0;i<row;i++){
                    getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b").setCellEditable(i,"vinvbascode", false);
                    //getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b").setCellEditable(i,"vhjl", false);
                    String[] formual= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvbascode").getEditFormulas();//获取编辑公式
                    getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
                	}
                getBillUI().updateUI();
              }
            
            
        }
        
    	
    	 //设置按钮的可用状态
        @SuppressWarnings("null")
		@Override
		protected void setBoEnabled() throws Exception {
            ScPgdVO pgVO = (ScPgdVO)getBillUI().getVOFromUI().getParentVO();
//            if (pgVO==null||pgVO.getPrimaryKey().length()==0){
//            	
//            }
//            else{  
            if(pgVO != null || pgVO.getPrimaryKey().length()!=0 ){
            	super.setBoEnabled();
            	boolean xdflag = pgVO.getXdflag()==null?null:pgVO.getXdflag().booleanValue();// 下达标记
                if(xdflag){
                	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                    getButtonManager().getButton(IEHButton.SENDFA).setEnabled(false);
                }
                if(!xdflag){
                    getButtonManager().getButton(IEHButton.SENDFA).setEnabled(true);
                }
                getBillUI().updateButtonUI();
            }
            
        }
        
        
        @Override
		protected void onBoCard() throws Exception {
        	super.onBoCard();
        	setBoEnabled();
        }
       
        
      
        
}
