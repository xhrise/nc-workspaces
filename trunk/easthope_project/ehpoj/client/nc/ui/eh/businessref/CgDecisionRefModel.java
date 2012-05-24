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
 * ���ܣ��ɹ�����
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
				"�Ƶ�����","���ݺ�","�ͻ�����","���ϱ���","��������","����"
		};
	}

	@Override
	public String getPkFieldCode() {
		return "pk_decision";
	}

	@Override
	public String getRefTitle() {
		return "�ɹ�����";
	}

	//<�޸�>��ͼ��eh_invbasdoc->bd_invbasdoc���ڣ�2009-08-11 09:38:54���ߣ���־Զ
	public String getTableName() {
		return "eh_view_decision";
	}
	
	@Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and NVL(lock_flag,'N')<>'Y'";
    } 
	
	
	


}

