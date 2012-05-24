package nc.ui.eh.businessref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ClientEnvironment;

/**
 * 功能：批次
 * 
 * @author wm
 * @date 2009年1月9日14:27:38
 */

public class PCRefModel extends AbstractRefModel {

    public static String pk_invbasdoc = null;
    ClientEnvironment ce = ClientEnvironment.getInstance();

    public PCRefModel() {
	super();
    }

    @Override
    public int getDefaultFieldCount() {
	return 6;
    }

    @Override
    public String[] getFieldCode() {
	return new String[] { "pc", "invname", "billno", "dmakedate",
		"rkmount", "ISNULL(rkmount,0) - ISNULL(ckamount,0) ckamount",
		"pk_rkd_b" };
    }

    @Override
    public String[] getFieldName() {
	return new String[] { "批次", "物料名称", "单据号", "制单日期", "数量", "未出库数量",
		"入库单子表PK" };
    }

    @Override
    public String getPkFieldCode() {
	return "pk_rkd_b";
    }

    @Override
    public String getRefTitle() {
	return "批次参照";
    }

    @Override
    public String getTableName() {
	return "eh_pctable";
    }

    @Override
    public String getWherePart() {
	return " pk_corp = '" + ce.getCorporation().getPk_corp()
		+ "'   and pk_invbasdoc='" + pk_invbasdoc + "'";
    }

    /**
     * 改掉参照取值 add by wb
     */
    @Override
    public String getPkValue() {
	String strValue = null;
	String[] pkvaluses = getPkValues();
	if (pkvaluses != null)
	    strValue = PubTools.combinArrayToString2(pkvaluses);
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
