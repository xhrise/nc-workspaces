/**
 * @(#)SkfsRefModel.java	V3.1 2008-5-29
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class SkfsRefModel extends AbstractRefModel{

	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    public SkfsRefModel(){
        super();
    }
    
    @Override
	public int getDefaultFieldCount() {
        return 3;
    }

    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[]{
                "sfkfscode","sfkfsname","pk_sfkfs"
        };
    }

    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[]{
                "��ʽ����","��ʽ����","����"
        };
    }

    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_sfkfs";
    }

    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "�ո��ʽ";
    }
 
    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "eh_arap_sfkfs";
    }
    
    @Override
	public String getWherePart() {
    	//�޸ĳɼ���ά����ʱ�䣺2010-02-24.���ߣ���־Զ
        //return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    	return " isnull(dr,0)=0 ";
    }

}

