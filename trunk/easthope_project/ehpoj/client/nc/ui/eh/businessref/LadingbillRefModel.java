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
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ClientEnvironment;

/**
 * 功能：提货通知单参照
 * 
 * @author wb
 * @date 2008-10-13 8:54:13
 */

public class LadingbillRefModel extends AbstractRefModel {

    public static String pk_cubasdoc = null;
    String[] sDatas = null;
    ClientEnvironment ce = ClientEnvironment.getInstance();

    public LadingbillRefModel() {
	super();
    }

    @Override
    public int getDefaultFieldCount() {
	return 2;
    }

    @Override
    public String[] getFieldCode() {
	return new String[] { "ladingdate", "billno", "pk_ladingbill" };
    }

    @Override
    public String[] getFieldName() {
	return new String[] { "提货日期", "单据号", "主键" };
    }

    @Override
    public String getPkFieldCode() {
	return "pk_ladingbill";
    }

    @Override
    public String getRefTitle() {
	return "提货通知单";
    }

    @Override
    public String getTableName() {
	return "eh_ladingbill";
    }

    @Override
    public String getWherePart() {
	// pk_cubasdoc=nc.ui.eh.cw.h1100505.ClientUI.pk_cubasdoc;
	return " pk_corp = '"
		+ ce.getCorporation().getPk_corp()
		+ "' and pk_cubasdoc = '"
		+ pk_cubasdoc
		+ "' and isnull(ysht_flag,'N')<>'Y' and isnull(self_flag,'N')= 'N' and isnull(dr,0)=0 ";
    }

    // /**
    // * 改掉参照取值 add by wb
    // */
    // @Override
    // public String getPkValue() {
    // String strValue = null;
    // Object objValue = getValue(getPkFieldCode());
    // String[] pkvaluses = getPkValues();
    // if (objValue != null) {
    // strValue = objValue.toString();
    // }
    // if (pkvaluses != null)
    // strValue = PubTools.combinArrayToString2(pkvaluses);
    //		
    // pkvaluses = super.getPkValues();
    //
    // return strValue;
    // }
    //
    // /**
    // * 通过参照重新组装sql add by wb
    // */
    // @Override
    // protected String getWherePartByFieldsAndValues(String[] fields, String[]
    // values)
    // {
    // String wherePart = "";
    // StringBuffer sb = new StringBuffer();
    // if(fields != null)
    // {
    // int length = fields.length;
    // if(values==null) return null;
    // String pks = values[0];
    // for(int i = 0; i < length; i++)
    // {
    // if(pks.length()==20)
    // sb.append(fields[i]).append(" = '").append(pks).append("'");
    // else
    // sb.append(fields[i]).append(" in (").append(pks).append(")");
    // if(i != length - 1)
    // sb.append(" or ");
    // }
    //
    // } else
    // {
    // return null;
    // }
    // wherePart = sb.toString();
    // return wherePart;
    // }
    /**
     * 改掉参照取值 add by wb
     */
    @Override
    public String getPkValue() {
	String strValue = null;
	Object objValue = getValue(getPkFieldCode());
	String[] pkvaluses = getPkValues();
	if (objValue != null) {
	    strValue = objValue.toString();
	}
	if (pkvaluses != null)
	    strValue = PubTools.combinArrayToString2(pkvaluses);

	pkvaluses = super.getPkValues();

	return strValue;
    }

    /**
     * 通过参照重新组装sql add by wb
     */
    @Override
    public String getWherePartByFieldsAndValues(String[] fields, String[] values) {
	String wherePart = "";
	StringBuffer sb = new StringBuffer();
	if (fields != null) {
	    int length = fields.length;
	    if (values == null)
		return null;
	    String pks = values[0];
	    for (int i = 0; i < length; i++) {
		if (pks.length() == 20)
		    sb.append(fields[i]).append(" = '").append(pks).append("'");
		else
		    sb.append(fields[i]).append(" in (").append(pks)
			    .append(")");
		if (i != length - 1)
		    sb.append(" or ");
	    }

	} else {
	    return null;
	}
	wherePart = sb.toString();
	return wherePart;
    }

}
