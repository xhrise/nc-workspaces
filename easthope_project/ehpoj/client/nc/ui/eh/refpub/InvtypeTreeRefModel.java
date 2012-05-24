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

public class InvtypeTreeRefModel extends AbstractRefTreeModel {
	
	public InvtypeTreeRefModel(){
	}

    @Override
	public String getChildField() {
        // TODO Auto-generated method stub
        return "pk_invcl";
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
        return new String[] {"invclasscode","invclassname","pk_invcl","pk_father"};
    }

    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[] {"分类编码", "分类名称","主键","父键"};
    }

    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_invcl";
    }

    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "物料分类";
    }

    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "bd_invcl";
    }
    @Override
	public String getWherePart() {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	String pk_corp = ce.getCorporation().getPk_corp();
    	String whereSQL = " isnull(dr,0)=0";
    	return whereSQL;
    }
}

