/**
 * @(#)CubgysRefModel.java	V3.1 2008-6-21
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * ���ܣ���Ӧ��
 * @author zqy
 * @date 2008-6-21 13:35:24
 */

public class CubgysRefModel extends AbstractRefModel {
    ClientEnvironment ce = ClientEnvironment.getInstance();
    public CubgysRefModel() {
        super();
    }
    
    @Override
	public int getDefaultFieldCount() {
            return 3;
    }
        
    
    @Override
	public String[] getFieldCode() {
        return new String[]{
        		"mnecode","custname","custcode","custshortname","taxpayerid","conaddr","phone2","pk_cubasdoc"
        };
    }

    @Override
	public String[] getFieldName() {
        return new String[]{
        		"������","��Ӧ������","��Ӧ�̱���","��Ӧ�̼��","��˰�˵ǼǺ�","ͨ�ŵ�ַ","�绰","����"
            };
    }

    
    @Override
	public String getRefTitle() {
        return "��Ӧ�̵���";
    }

    @Override
	public String getTableName() {
        return "bd_cubasdoc";
    }

    @Override
	public String getPkFieldCode() {
        return "pk_cubasdoc";
    }

    
    @Override
	public String getWherePart() {
        StringBuffer sql = new StringBuffer()
        .append(" isnull(dr,0)=0 and custprop in (1,2) and pk_corp='"+ce.getCorporation().getPk_corp()+"' and isnull(lock_flag,'N')='N' ");
        return sql.toString();
    }

}

