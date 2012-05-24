/**
 * @(#)ClientEventHandler.java	V3.1 2007-3-11
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.uibase;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能说明：审批时公用的EventHandler 类,因原继承类中很多按钮在审批界面中导致错误
 * 可以在UI中 createEventHandler()方法中实例化此类
 * @author 王兵
 * 2008-5-7 11:36:45
 */
public class AbstractSPEventHandler extends ManageEventHandler {
	public AbstractSPEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	
	/*
     * 功能：查询对话框显示
     */
	 private QueryConditionClient dlg = null;
     protected QueryConditionClient getQueryDLG()
     {        
         if(dlg == null){
             dlg = createQueryDLG();
         }
         return dlg;
     }
     
    @Override
	protected void onBoQuery() throws Exception {
    	int type = getQueryDLG().showModal();
        if(type==1){
			String sqlWhere = getQueryDLG().getWhereSQL()==null?"":getQueryDLG().getWhereSQL();
			sqlWhere = sqlWhere.replaceFirst("审批不通过", String.valueOf(IBillStatus.NOPASS));
			sqlWhere = sqlWhere.replaceFirst("审批通过", String.valueOf(IBillStatus.CHECKPASS));
			sqlWhere = sqlWhere.replaceFirst("审批中", String.valueOf(IBillStatus.CHECKGOING));
			sqlWhere = sqlWhere.replaceFirst("提交态", String.valueOf(IBillStatus.COMMIT));
			sqlWhere = sqlWhere.replaceFirst("作废", String.valueOf(IBillStatus.DELETE));
			sqlWhere = sqlWhere.replaceFirst("冲销", String.valueOf(IBillStatus.CX));
			sqlWhere = sqlWhere.replaceFirst("终止", String.valueOf(IBillStatus.ENDED));
			sqlWhere = sqlWhere.replaceFirst("冻结状态", String.valueOf(IBillStatus.FREEZE));
			sqlWhere = sqlWhere.replaceFirst("自由态", String.valueOf(IBillStatus.FREE));
			
			if(sqlWhere==null||sqlWhere.equals("")){
				sqlWhere =" 1=1 ";	
			}
			if(addCondtion()!=null&&addCondtion().length()>0){
				sqlWhere = sqlWhere + " and "+addCondtion();
			}
			SuperVO[] queryVos = queryHeadVOs(sqlWhere+" and pk_corp = '"+_getCorp().getPk_corp()+"'");
			
			
	       getBufferData().clear();
	       // 增加数据到Buffer
	       addDataToBuffer(queryVos);
	
	       updateBuffer();
        }
	}
    
    //查询模板中选择为最新标记
    protected QueryConditionClient createQueryDLG() {
    	QueryConditionClient dlg = new QueryConditionClient();
    	String billtype = getUIController().getBillType();           // 单据类型
        String sql = "select nodecode from bd_billtype where pk_billtypecode = '"+billtype+"' and isnull(dr,0)=0"; //取节点号
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
			String nodecode = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:
							iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
            String lastdate = _getDate().toString().substring(0, 7)+"-01";
	        dlg.setTempletID(_getCorp().getPk_corp(), nodecode, null, null); 
	        dlg.setDefaultValue("dmakedate",lastdate,null);
	        dlg.setNormalShow(false);
        }catch (BusinessException e) {
			e.printStackTrace();
		}
        return dlg;
    }
    
    /**查询时增加条件 " vbilltype = "" and ..."
     * @return
     */
    public String addCondtion(){
    	return null;
    }
    
    @Override
    public void onBoAudit() throws Exception {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	UFDate nowdate = ce.getDate();			//当前登录日期不能小于制单日期
    	String pk = getBillUI().getChangedVOFromUI().getParentVO().getPrimaryKey();
    	if(pk==null||pk.length()==0){
    		getBillUI().showErrorMessage("请选择一张单据!");
    		return;
    	}
    	UFDate dmkedate = new UFDate(getBillUI().getChangedVOFromUI().getParentVO().getAttributeValue("dmakedate")==null?
    			nowdate.toString():getBillUI().getChangedVOFromUI().getParentVO().getAttributeValue("dmakedate").toString());	//制单日期
    	if(nowdate.before(dmkedate)){
    		getBillUI().showErrorMessage("审批日期不能早于制单日期!");
    		return;
    	}
    	super.onBoAudit();
    	
    	setBoEnabled();
    }        
       
    @Override
    protected void onBoCard() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoCard();
    	setBoEnabled();
    }
    
    //  设置按钮的可用状态
    public void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
        Integer vbillstatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
        
        ButtonObject bo = getButtonManager().getButton(IBillButton.Audit);
        if(bo!=null){
	        if (vbillstatus==null){
	        	getButtonManager().getButton(IBillButton.Audit).setEnabled(true);
	        	getButtonManager().getButton(IBillButton.Print).setEnabled(true);
	        }
	        else{   
	            switch (vbillstatus.intValue()){
	                case IBillStatus.CHECKPASS:
	                	getButtonManager().getButton(IBillButton.Audit).setEnabled(false);
	                	getButtonManager().getButton(IBillButton.Print).setEnabled(true);
	                	break;
	                case IBillStatus.COMMIT:
	                	getButtonManager().getButton(IBillButton.Audit).setEnabled(true);
	                	getButtonManager().getButton(IBillButton.Print).setEnabled(true);
	                	break;
	                case IBillStatus.FREE:
	                	getButtonManager().getButton(IBillButton.Audit).setEnabled(false);
	                	getButtonManager().getButton(IBillButton.Print).setEnabled(true);
	                	break;
	                case IBillStatus.CHECKGOING:
	                	getButtonManager().getButton(IBillButton.Audit).setEnabled(true);
	                	getButtonManager().getButton(IBillButton.Print).setEnabled(true);
	                	break;
	                case IBillStatus.NOPASS:
	                	getButtonManager().getButton(IBillButton.Audit).setEnabled(false);
	                	getButtonManager().getButton(IBillButton.Print).setEnabled(true);
	                	break;
	            }
	        }
        }
        getBillUI().updateButtonUI();
    }
    
    @Override
    protected void onBoRefresh() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoRefresh();
    	setBoEnabled();
    }
    
    
}