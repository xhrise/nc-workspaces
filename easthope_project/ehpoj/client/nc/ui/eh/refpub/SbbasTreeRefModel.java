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

/***
 * 设备档案
 * @author wangbing
 *
 */
public class SbbasTreeRefModel extends AbstractRefTreeModel {
	
	@Override
	public int getDefaultFieldCount() {
        return 3;
    }
	
	public SbbasTreeRefModel(){
	}
	
	
    @Override
	public String getChildField() {
        // TODO Auto-generated method stub
        return "pk_sb";
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

    @Override
	public String getFatherField() {
        // TODO Auto-generated method stub
        return "pk_father";
    }

    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[] {"code","name","typename","pk_sb","pk_father"};
    }

    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[] {"编码", "名称","型号","主键","父键"};
    }

    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_sb";
    }

    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "设备档案";
    }

    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "eh_sc_sbbasdoc";
    }
    @Override
	public String getWherePart() {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	String pk_corp = ce.getCorporation().getPk_corp();
    	String whereSQL = " isnull(dr,0)=0 and pk_corp = '"+pk_corp+"'";
    	return whereSQL;
    }
}

