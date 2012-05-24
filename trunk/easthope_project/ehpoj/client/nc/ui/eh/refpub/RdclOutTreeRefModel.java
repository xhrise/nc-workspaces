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
/**
 * 
 * 功能：出库类型参照
 * 时间：2009-08-21
 * 作者：张志远
 *
 */
public class RdclOutTreeRefModel extends AbstractRefTreeModel {
	
	public RdclOutTreeRefModel(){
	}

    @Override
	public String getChildField() {
        // TODO Auto-generated method stub
        return "pk_rdcl";
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
        return "pk_frdcl";
    }

    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[] {"rdcode","rdname","pk_rdcl","pk_frdcl"};
    }

    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[] {"分类编码", "分类名称","主键","父键"};
    }

    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_rdcl";
    }

    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "出库类型";
    }

    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "bd_rdcl";
    }
    @Override
	public String getWherePart() {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	String pk_corp = ce.getCorporation().getPk_corp();
    	String whereSQL = " NVL(rdflag,1) = 1 AND NVL(dr,0) = 0";
    	return whereSQL;
    }
}

