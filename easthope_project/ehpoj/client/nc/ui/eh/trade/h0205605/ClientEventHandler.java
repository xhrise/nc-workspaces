/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.h0205605;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.trade.h0205605.TradePlanBVO;
import nc.vo.eh.trade.h0205605.TradePlanVO;
import nc.vo.pub.lang.UFDate;

/**
 * 功能:销售计划 
 * ZA96
 * @author WB
 * 2008-10-10 14:35:32
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	 //唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_cubasdoc","pk_psndoc","pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("表体同一营销代表针对同一客户有相同物料存在，不允许操作！");
            return;
        }
        TradePlanBVO[] bvos = (TradePlanBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        if(bvos!=null&&bvos.length>0){
        	for(int i=0;i<bvos.length;i++){
        		TradePlanBVO bvo = bvos[i];
        		String errorinfo = bvo.getErrorinfo();
        		if(errorinfo!=null&&errorinfo.length()>0){
        			getBillUI().showErrorMessage("第"+(i+1)+"行有导入错误信息,不允许保存!");
        			return;
        		}
        	}
        }
        super.onBoSave();
    }
    
    @Override
    protected void onBoElse(int intBtn) throws Exception {
    	 switch (intBtn)
         {
             case IEHButton.GENRENDETAIL:   
                 onBoGENRENDETAIL();
                 break;
             case IEHButton.ExcelImport:
            	 onBoStockCope();
         }
    	super.onBoElse(intBtn);
    }

    /**
     * 功能：
     * <p>生成可销料明细</>
     */
	private void onBoGENRENDETAIL() {
		//先清空表体
		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(rows>0){
        	int res = getBillUI().showOkCancelMessage("按可销料生成明细需要先清空表体,是否确认清空?");
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
		
//		StringBuffer sql = new StringBuffer()
//		.append(" select c.pk_psndoc,w.psnname,a.pk_cubasdoc,a.custname,b.pk_invbasdoc,d.invcode,d.invname")
//		.append(" from bd_cubasdoc a,eh_custkxl b,eh_custyxdb c,eh_invbasdoc d,bd_psndoc w")
//		.append(" where a.pk_cubasdoc = b.pk_cubasdoc")
//		.append(" and a.pk_cubasdoc = c.pk_cubasdoc")
//		.append(" and b.pk_invbasdoc = d.pk_invbasdoc")
//		.append(" and c.pk_psndoc = w.pk_psndoc")
//		.append(" and a.custprop in (0,1)")
//		.append(" and a.lock_flag<>'Y'")
//		.append(" and d.lock_flag<>'Y'")
//		.append(" and c.ismain = 'Y'")
//		.append(" and a.pk_corp = '"+_getCorp().getPk_corp()+"'")
//		.append(" and isnull(a.dr,0)=0 ")
//		.append(" and isnull(b.dr,0)=0")
//		.append(" and isnull(c.dr,0)=0")
//		.append(" and isnull(d.dr,0)=0");
        //新版SQL
        String sql = " select c.pk_psndoc,w.psnname,b.pk_cubasdoc ,a.custname,b.pk_invbasdoc,d.invcode,d.invname "+
        			 " from bd_cubasdoc a,bd_cumandoc aa,eh_custkxl b,eh_custyxdb c, "+
        			 " bd_invbasdoc d,bd_invmandoc dd,bd_psndoc w "+
        			 " where "+
        			 " a.pk_cubasdoc=aa.pk_cubasdoc "+
        			 " and aa.pk_cumandoc = b.pk_cubasdoc "+
        			 " and aa.pk_cumandoc = c.pk_cubasdoc "+
        			 " and d.pk_invbasdoc = dd.pk_invbasdoc "+
        			 " and b.pk_invbasdoc = dd.pk_invmandoc "+
        			 " and c.pk_psndoc = w.pk_psndoc "+
        			 " and a.custprop in (0,1) "+
        			 " and c.ismain = 'Y' "+
        			 " and a.pk_corp = '"+_getCorp().getPk_corp()+"' "+
        			 " and nvl(aa.dr,0)=0 "+
        			 " and nvl(b.dr,0)=0 "+
        			 " and nvl(c.dr,0)=0 "+
        			 " and nvl(dd.dr,0)=0 " +
        			 " and nvl(aa.sealflag,'')='' ";
		try{
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				String pk_psndoc = null;			//营销代表PK
				String psnname = null;				
				String pk_cubasdoc = null;			//客户
				String custname = null;				
				String pk_invbasdoc = null;			//物料
				String invcode = null;
				String invname = null;
				for(int i=0;i<arr.size();i++){
					HashMap hm = (HashMap)arr.get(i);
					pk_psndoc = hm.get("pk_psndoc")==null?null:hm.get("pk_psndoc").toString();
					psnname = hm.get("psnname")==null?null:hm.get("psnname").toString();
					pk_cubasdoc = hm.get("pk_cubasdoc")==null?null:hm.get("pk_cubasdoc").toString();
					custname = hm.get("custname")==null?null:hm.get("custname").toString();
					pk_invbasdoc = hm.get("pk_invbasdoc")==null?null:hm.get("pk_invbasdoc").toString();
					invcode = hm.get("invcode")==null?null:hm.get("invcode").toString();
					invname = hm.get("invname")==null?null:hm.get("invname").toString();
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_psndoc, i, "pk_psndoc");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_cubasdoc, i, "pk_cubasdoc");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_invbasdoc, i, "pk_invbasdoc");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(psnname, i, "vpsndoc");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(custname, i, "vcust");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invcode, i, "vinvcode");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invname, i, "vinvname");
//					String[] formual1= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vcust").getLoadFormula();//获取编辑公式
//					String[] formual2= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vpsndoc").getLoadFormula();	//获取编辑公式
//					String[] formual3= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").getLoadFormula();//获取编辑公式
//					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual1);
//					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual2);
//					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual3);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 功能：
	 * <p>Excel导入销售计划</p>
	 */
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
            	String code = bo.getCode();      // 得到点击的按钮的名称 解决上面的问题 edit by wb at 2008-10-6 13:03:08 
                if(parentcode.equals("增加")&&"自制单据".equalsIgnoreCase(code)){
	                		UFDate date = _getDate();
	                    	BusinessDelegator db = new BusinessDelegator();
	                    	TradePlanVO[] vos = (TradePlanVO[])db.queryByCondition(TradePlanVO.class, "  nyear = "+date.getYear()+" and nmonth = "+date.getMonth()+" and pk_corp = '"+_getCorp().getPk_corp()+"'");
	                    	if(vos!=null&&vos.length>0){
	                    		getBillUI().showErrorMessage("在"+date.getYear()+"年度"+date.getMonth()+"月份已有销售任务,不可以在进行增加!");
	                    		return;
	                    	}
	            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onButton_N(bo, model);
    }
   
}

