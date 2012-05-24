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
 * ����˵��������ʱ���õ�EventHandler ��,��ԭ�̳����кܶఴť�����������е��´���
 * ������UI�� createEventHandler()������ʵ��������
 * @author ����
 * 2008-5-7 11:36:45
 */
public class AbstractSPEventHandler extends ManageEventHandler {
	public AbstractSPEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	
	/*
     * ���ܣ���ѯ�Ի�����ʾ
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
			sqlWhere = sqlWhere.replaceFirst("������ͨ��", String.valueOf(IBillStatus.NOPASS));
			sqlWhere = sqlWhere.replaceFirst("����ͨ��", String.valueOf(IBillStatus.CHECKPASS));
			sqlWhere = sqlWhere.replaceFirst("������", String.valueOf(IBillStatus.CHECKGOING));
			sqlWhere = sqlWhere.replaceFirst("�ύ̬", String.valueOf(IBillStatus.COMMIT));
			sqlWhere = sqlWhere.replaceFirst("����", String.valueOf(IBillStatus.DELETE));
			sqlWhere = sqlWhere.replaceFirst("����", String.valueOf(IBillStatus.CX));
			sqlWhere = sqlWhere.replaceFirst("��ֹ", String.valueOf(IBillStatus.ENDED));
			sqlWhere = sqlWhere.replaceFirst("����״̬", String.valueOf(IBillStatus.FREEZE));
			sqlWhere = sqlWhere.replaceFirst("����̬", String.valueOf(IBillStatus.FREE));
			
			if(sqlWhere==null||sqlWhere.equals("")){
				sqlWhere =" 1=1 ";	
			}
			if(addCondtion()!=null&&addCondtion().length()>0){
				sqlWhere = sqlWhere + " and "+addCondtion();
			}
			SuperVO[] queryVos = queryHeadVOs(sqlWhere+" and pk_corp = '"+_getCorp().getPk_corp()+"'");
			
			
	       getBufferData().clear();
	       // �������ݵ�Buffer
	       addDataToBuffer(queryVos);
	
	       updateBuffer();
        }
	}
    
    //��ѯģ����ѡ��Ϊ���±��
    protected QueryConditionClient createQueryDLG() {
    	QueryConditionClient dlg = new QueryConditionClient();
    	String billtype = getUIController().getBillType();           // ��������
        String sql = "select nodecode from bd_billtype where pk_billtypecode = '"+billtype+"' and isnull(dr,0)=0"; //ȡ�ڵ��
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
    
    /**��ѯʱ�������� " vbilltype = "" and ..."
     * @return
     */
    public String addCondtion(){
    	return null;
    }
    
    @Override
    public void onBoAudit() throws Exception {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	UFDate nowdate = ce.getDate();			//��ǰ��¼���ڲ���С���Ƶ�����
    	String pk = getBillUI().getChangedVOFromUI().getParentVO().getPrimaryKey();
    	if(pk==null||pk.length()==0){
    		getBillUI().showErrorMessage("��ѡ��һ�ŵ���!");
    		return;
    	}
    	UFDate dmkedate = new UFDate(getBillUI().getChangedVOFromUI().getParentVO().getAttributeValue("dmakedate")==null?
    			nowdate.toString():getBillUI().getChangedVOFromUI().getParentVO().getAttributeValue("dmakedate").toString());	//�Ƶ�����
    	if(nowdate.before(dmkedate)){
    		getBillUI().showErrorMessage("�������ڲ��������Ƶ�����!");
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
    
    //  ���ð�ť�Ŀ���״̬
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