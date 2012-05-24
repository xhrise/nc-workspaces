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

public class AreaclRefModel extends AbstractRefTreeModel   {
	ClientEnvironment ce = ClientEnvironment.getInstance();
    @Override
	public String getChildField() {
        // TODO Auto-generated method stub
        return "pk_areacl";
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
        return "fatherpk";
    }

    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[] {"areacode","areaname", "pk_areacl","fatherpk"};
    }

    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[] {"Æ¬Çø±àºÅ","Æ¬ÇøÃû³Æ", "Ö÷¼ü","¸¸¼ü"};
    }

    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_areacl";
    }

    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "Æ¬Çø²ÎÕÕ";
    }

    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "eh_areacl";
    }
    @Override
	public String getWherePart() {
        return " pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(stop_flag,'N')='N' and isnull(dr,0)=0";
    }
}

