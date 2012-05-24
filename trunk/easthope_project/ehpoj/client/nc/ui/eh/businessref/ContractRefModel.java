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
 * 功能：采购合同单
 * @author zqy
 * @date  2008-8-14 10:58:49
 */

public class ContractRefModel  extends AbstractRefModel{
	
	public static String pk_cubasdoc = null;    
	ClientEnvironment ce = ClientEnvironment.getInstance();
    
	public ContractRefModel(){
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
        return 2;
    }

	@Override
	public String[] getFieldCode() {
		return new String[]{
				"contractname","billno","writedate","pk_contract"
		};
	}

	@Override
	public String[] getFieldName() {
		return new String[]{
				"合同名称","合同号","采购日期","主键"
		};
	}

	@Override
	public String getPkFieldCode() {
		return "pk_contract";
	}

	@Override
	public String getRefTitle() {
		return "采购合同";
	}

	@Override
	public String getTableName() {
		return "eh_stock_contract ";
	}
	
	@Override
	public String getWherePart() {
//        pk_cubasdoc=nc.ui.eh.cw.h1100505.ClientUI.pk_cubasdoc;
        return " pk_corp = '"+ce.getCorporation().getPk_corp()+"' and pk_cubasdoc='"+pk_cubasdoc+"' and isnull(dr,0)=0 ";
    }
}

