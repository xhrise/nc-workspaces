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

import nc.ui.bd.ref.AbstractRefModel;

public class NewAreaclRefModel extends AbstractRefModel   {
	

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
        return " pk_corp = '"+getPk_corp()+"' and nvl(stop_flag,'N')='N' and nvl(dr,0)=0";
    }
}

