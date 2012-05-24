/**
 * @(#)ReceiptRefModel.java	V3.1 2008-6-27
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.businessref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * 功能：采购决策
 * @author wb
 * @date  2009-2-9 15:38:54
 */ 

public class CgDecisionRefModel  extends AbstractRefModel{
	
	ClientEnvironment ce = ClientEnvironment.getInstance();
    
	public CgDecisionRefModel(){
		super();
		reloadData();
	}
	
	@Override
	public int getDefaultFieldCount() {
        return 5;
    }

	@Override
	public String[] getFieldCode() {
		return new String[]{
				"dmakedate"," billno","custname","invcode","invname","pk_decision"
		};
	}

	@Override
	public String[] getFieldName() {
		return new String[]{
				"制单日期","单据号","客户名称","物料编码","物料名称","主键"
		};
	}

	@Override
	public String getPkFieldCode() {
		return "pk_decision";
	}

	@Override
	public String getRefTitle() {
		return "采购决策";
	}

	//<修改>视图中eh_invbasdoc->bd_invbasdoc日期：2009-08-11 09:38:54作者：张志远
	public String getTableName() {
		return "eh_view_decision";
	}
	
	@Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and NVL(lock_flag,'N')<>'Y'";
    } 
	
	
	


}

