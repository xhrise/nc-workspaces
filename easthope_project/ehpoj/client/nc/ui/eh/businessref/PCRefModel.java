package nc.ui.eh.businessref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ClientEnvironment;

/**
 * ���ܣ�����
 * 
 * @author wm
 * @date 2009��1��9��14:27:38
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
	return new String[] { "����", "��������", "���ݺ�", "�Ƶ�����", "����", "δ��������",
		"��ⵥ�ӱ�PK" };
    }

    @Override
    public String getPkFieldCode() {
	return "pk_rkd_b";
    }

    @Override
    public String getRefTitle() {
	return "���β���";
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
     * �ĵ�����ȡֵ add by wb
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
     * ͨ������������װsql add by wb
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
