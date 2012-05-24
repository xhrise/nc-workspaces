/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.h0205615;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.trade.h0205605.TradePlanBVO;
import nc.vo.eh.trade.h0205605.TradePlanVO;
import nc.vo.eh.trade.h0205615.TradePeriodplanVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能:销售旬计划
 * ZB14
 * @author WB
 * 2008-12-22 13:29:43
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
	TradePeriodplanVO hvo = null;
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	 //唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("表体有相同物料存在，不允许操作！");
            return;
        }
        if(hvo!=null){
        	IVOPersistence  iVOPersistence =   (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            hvo.setNew_flag(new UFBoolean(false));        //将旧版本的最新标记去掉
            hvo.setLock_flag(new UFBoolean(true));
        	iVOPersistence.updateVO(hvo);   // 表头
        	hvo = null;
        }
        BusinessDelegator db = new BusinessDelegator();
        TradePeriodplanVO hvos = (TradePeriodplanVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        UFDate plandate = hvos.getPlandate();
        int day = plandate.getDay();
    	String strxun_flag = null;
    	String[] strs = ICombobox.Period_flag;
    	int xun_flag = 0;
    	if(day>0&&day<=10){
    		xun_flag = 0;strxun_flag = strs[0];
    	}
    	if(day>10&&day<=20){ 
    		xun_flag = 1;strxun_flag = strs[1];
    	}
    	if(day>20){
    		xun_flag = 2;strxun_flag = strs[2];
    	}
        TradePeriodplanVO[] vos = (TradePeriodplanVO[])db.queryByCondition(TradePeriodplanVO.class, " substring(plandate,1,7) = '"+hvos.getPlandate().toString().substring(0,7)+"' and xun_flag = "+xun_flag+" and isnull(new_flag,'N')='Y' and pk_corp = '"+_getCorp().getPk_corp()+"' and pk_periodplan <> '"+hvos.getPk_periodplan()+"'");
    	if(vos!=null&&vos.length>0){
    		getBillUI().showErrorMessage("在"+plandate.getYear()+"年度"+plandate.getMonth()+"月份"+strxun_flag+"已有最新的销售旬计划,不可以再进行增加,请变更!");
    		return;
    	}
        super.onBoSave_withBillno();
    }
    
    @Override
    protected void onBoElse(int intBtn) throws Exception {
    	 switch (intBtn)
         {
             case IEHButton.GENRENDETAIL:   
                 onBoGENRENDETAIL();
                 break;
             case IEHButton.STOCKCHANGE:   
                 onBobg();
                 break;
         }
    	super.onBoElse(intBtn);
    }

    /**
     * 功能：
     * <p>按可销料生成明细</p>
     */
	private void onBoGENRENDETAIL() {
		//先清空表体
		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(rows>0){
        	int res = getBillUI().showOkCancelMessage("生成产品明细需要先清空表体,是否确认清空?");
        	if(res==1){
        		int[] rowcount=new int[rows];
                for(int i=rows - 1;i>=0;i--){
                	rowcount[i]=i;
                }
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rowcount);
        	}else{
        		return;
        	}
        }
        
        //新版SQL
        String sql = " select distinct a.pk_invmandoc pk_invbasdoc "+
        			 " ,c.invcode,c.invname,c.invspec,c.invtype,h.measname,c.def1,g.brandname "+
        			 " from bd_invmandoc a,eh_stordoc b,bd_stordoc bb "+
        			 " ,bd_invbasdoc c,eh_brand g,bd_measdoc h "+
        			 " where a.def1=bb.pk_stordoc  "+
        			 " and b.pk_bdstordoc=bb.pk_stordoc "+
        			 " and a.pk_invbasdoc = c.pk_invbasdoc "+
        			 " and c.invpinpai = g.pk_brand "+
        			 " and c.pk_measdoc = h.pk_measdoc "+
        			 " and nvl(b.is_flag,0)=1 and a.pk_corp='"+_getCorp().getPk_corp()+"' "+ 
        			 " and nvl(a.dr,0)=0 and nvl(g.dr, 0) = 0 and nvl(h.dr, 0) = 0 "+
        			 " and ( a.sealflag = 'N' or a.sealflag is null ) ";
		try{
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				String pk_invbasdoc = null;			//物料
				for(int i=0;i<arr.size();i++){
					HashMap hm = (HashMap)arr.get(i);
					pk_invbasdoc = hm.get("pk_invbasdoc")==null?null:hm.get("pk_invbasdoc").toString();
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_invbasdoc, i, "pk_invbasdoc");
					this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invcode"), i, "vinvbascode");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invname"), i, "vinvname");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invspec"), i, "invspec");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invtype"), i, "invtype");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("measname"), i, "dw");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("def1"), i, "color");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("brandname"), i, "vbrand");
//					String[] formulas = getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_invbasdoc").getLoadFormula();
//					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i, formulas);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	 //变更旬计划
	 private void onBobg() {
    	 try {
             hvo = (TradePeriodplanVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
             int  billstatus = hvo.getVbillstatus().intValue();
             UFBoolean new_flag = new UFBoolean(hvo.getNew_flag()==null?false:hvo.getNew_flag().booleanValue());            //最新标记
                //不是最新版本
                if(!new_flag.booleanValue()){
                    getBillUI().showErrorMessage("不是最新版本,不能变更!");
                    return;
                }
                //只有审批通过的项目才允许调整
                if(billstatus!=IBillStatus.CHECKPASS){
                    getBillUI().showErrorMessage("只有审批通过的项目才允许变更！");
                    return;
                }
                int ok=getBillUI().showYesNoMessage("是否确认进行项目变更?");
                if(ok==MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                    onBoCopy();
//                    getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(true);
                    getBillUI().updateButtonUI();
                    //将版本号进行改变在原基础上加1
                    Integer ver= hvo.getVer();
                    ver=ver+1; 
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ver").setValue(ver);
                    getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
                    getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapproveid").setValue(null);
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dapprovedate").setValue(null);
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapprovenote").setValue(null);
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(new Integer(8));
                    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("new_flag").setValue("Y");            //最新标记
                    getBillUI().updateUI();
                }else{
                	return;
                }
         } catch (Exception e) {
             e.printStackTrace();
         }
	}
	 
	 
	//Excel导入销售计划
    public void onBoStockCope(){ 
    	//先清空表体
		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(rows>0){
        	int res = getBillUI().showOkCancelMessage("导入Excel需要先清空表体,是否确认清空?");
        	if(res==1){
        		int[] rowcount=new int[rows];
                for(int i=rows - 1;i>=0;i--){
                	rowcount[i]=i;
                }
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rowcount);
        	}else{
        		return;
        	}
        }
        try {
            nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(true);
            int res = fileChooser.showOpenDialog(getBillUI());
            if (res == 0) {
                File txtFile = fileChooser.getSelectedFile();
                String filename = txtFile.getName();
                filename = filename.substring(filename.indexOf("."));
                if(!filename.equals(".xls")){
                	getBillUI().showErrorMessage("请导入符合规则的Excel文件!");
                	return;
                }
                String filepath = txtFile.getAbsolutePath();
                if(!ExcelImport.hasData(0, filepath)){
                	getBillUI().showErrorMessage("所选Excel中没有数据,请重新选择!");
                	return;
                }
                TradePlanBVO[] bvos = ExcelImport.readData("", 0, 0, 0,filepath);
                if(bvos!=null && bvos.length>0){
                	for(int i=0;i<bvos.length;i++){
                		getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
                		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(bvos[i], i);
                	}
                    String pk_period = ExcelImport.pk_period;			//期间PK
                    int nyear = ExcelImport.nyear;
                    int nmonth = ExcelImport.nmonth;
                    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_period",pk_period);
                    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("nyear",nyear);
                    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("nmonth",nmonth);
                    ((ClientUI)getBillUI()).setDefaultData();
//                    BillScrollPane bsp = this.getBillUI().getb
                }
            } else {
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }         

    @Override
    public void onBoAdd(ButtonObject bo) throws Exception {
    	UFDate date = _getDate();
    	BusinessDelegator db = new BusinessDelegator();
    	TradePlanVO[] vos = (TradePlanVO[])db.queryByCondition(TradePlanVO.class, " and nyear = "+date.getYear()+" and nmonth = "+date.getMonth()+" and pk_corp = '"+_getCorp().getPk_corp()+"'");
    	if(vos!=null&&vos.length>0){
    		getBillUI().showErrorMessage("在"+date.getYear()+"年度"+date.getMonth()+"月份已有销售任务,不可以在进行增加!");
    	}
    	super.onBoAdd(bo);
    }
    @Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    }
    
    @Override
	public void onButton_N(ButtonObject bo,BillModel model){
    	try {
            ButtonObject parent=bo.getParent();
            if(!Toolkits.isEmpty(parent)){
            	String parentcode = bo.getParent().getCode();			
            	String code = bo.getCode();     
	            if(parentcode.equals("增加")&&"自制单据".equalsIgnoreCase(code)){
            		UFDate nowdate = _getDate();
            		int day = nowdate.getDay();
                	String strxun_flag = null;
                	String[] strs = ICombobox.Period_flag;
                	int xun_flag = 0;
                	if(day>0&&day<=10){
                		xun_flag = 0;strxun_flag = strs[0];
                	}
                	if(day>10&&day<=20){ 
                		xun_flag = 1;strxun_flag = strs[1];
                	}
                	if(day>20){
                		xun_flag = 2;strxun_flag = strs[2];
                	}
                	BusinessDelegator db = new BusinessDelegator();
                	TradePeriodplanVO[] vos = (TradePeriodplanVO[])db.queryByCondition(TradePeriodplanVO.class, " substring(plandate,0,8) = '"+nowdate.toString().substring(0,7)+"' and xun_flag = "+xun_flag+" and isnull(new_flag,'N')='Y'  and pk_corp = '"+_getCorp().getPk_corp()+"'");
                	if(vos!=null&&vos.length>0){
                		getBillUI().showErrorMessage("在"+nowdate.getYear()+"年度"+nowdate.getMonth()+"月份"+strxun_flag+"已有最新的销售旬计划,不可以再进行增加,请变更!");
                		return;
                	}
	            }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onButton_N(bo, model);
    }
   
    @Override
    protected void onBoCopy() throws Exception {
    	super.onBoCopy();
    	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("new_flag","Y");
        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("lock_flag","N");
        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("billno",null);
        getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
        getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("plandate").setValue(null);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("xun_flag").setValue(null);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(8);
    }
    
    @Override
    protected void onBoLockBill() throws Exception {
    	super.onBoLockBill();
        TradePeriodplanVO hvos = (TradePeriodplanVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	IVOPersistence  iVOPersistence =   (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
    	hvos.setNew_flag(new UFBoolean(false));        //将最新标记去掉
    	iVOPersistence.updateVO(hvos);   // 表头
    	onBoRefresh();
    }
}

