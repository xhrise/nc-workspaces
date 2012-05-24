/**
 * @(#)TradeclassRefModel.java	V3.1 2007-8-29
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.pub.ClientEnvironment;

public class DeptdocRefModel extends AbstractRefTreeModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
    @Override
	public String getChildField() {
        // TODO Auto-generated method stub
        return getPkFieldCode();
    }

    @Override
	public String getCodingRule() {
        // TODO Auto-generated method stub
        return null;
    }
   
    @Override
	public String getEndField() {
        // TODO Auto-generated method stub
        return null;
    }
    /**
	 * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
	 */
	@Override
	public int getDefaultFieldCount() {
		return 2;
	}
	
    @Override
	public String getFatherField() {
        // TODO Auto-generated method stub
        return "pk_fathedept";
    }

    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[] {"deptcode", "deptname","pk_deptdoc","pk_fathedept"};
    }

    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[] {"���ű��","��������","����","�����"};
    }

    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_deptdoc";
    }

    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "���ŵ���";
    }

    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "bd_deptdoc";
    }
    
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0";
    }
}

