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

import nc.ui.bd.ref.AbstractRefGridTreeModel;

/**
 * ���ܣ��ջ�֪ͨ������
 * @author zqy
 * @date  2008-6-27 13:36:23
 */

public class ReceiptRefModel  extends AbstractRefGridTreeModel{
     
    private nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();

    
    @SuppressWarnings("unused")
    private String cuserid = ce.getUser().getPrimaryKey();
    
    private String codeRule = null;
/**
 * ElemRefModel2 ������ע�⡣
 */
public ReceiptRefModel() {
    super();
}
 
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-28 13:22:03)
 * @param corpID java.lang.String
 * @param jobTypeID java.lang.String
 * @param codeRule java.lang.String
 */
public ReceiptRefModel(String sGroupID,String corpID,String codeRule) {
    super();
    this.codeRule = codeRule;
}
/**
 * ָʾ���¼���ϵ�������ֶΡ�
 * ס�ӱ������ֶ�
 * �������ڣ�(2001-8-11 14:43:58)
 * @return java.lang.String
 */
@Override
public String getChildField() {
    return "pk_receipt";
}
/**
 * ������ֶ����顣
 * �������ʾ���ֶ�
 * �������ڣ�(2001-8-14 18:58:43)
 * @return java.lang.String[]
 */
//add by houcq 2010-11-22 ����receiptdate�ֶ�
@Override
public java.lang.String[] getClassFieldCode() {
    return new String[] {"receiptdate","billno", "tranno","custname","pk_receipt"};
}

/**
 * ������к͵������ӵ��ֶ�---һ���Ƿ���������
 * �����ֶ�
 * �������ڣ�(2001-8-15 16:39:47)
 */
@Override
public String getClassJoinField() {
    return "pk_receipt";
}
/**
 * ���������
 * �������ڣ�(2001-8-14 18:56:49)
 * @return java.lang.String
 */
@Override
public String getClassTableName() {
    //return "eh_stock_receipt a, bd_cumandoc c,bd_cubasdoc b";
    return " EH_VIEW_STOCK_RECEIPT_TWO ";
}

/**
 * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
 */
public int getClassDefaultFieldCount() {
	return 4;
}
/**
 * �����Where�Ӿ䡣
 * �������ڣ�(2001-8-14 18:57:46)
 * @return java.lang.String
 */
@Override
public String getClassWherePart() {
    //StringBuffer sql=new StringBuffer("a.pk_cubasdoc = c.pk_cumandoc and c.pk_cubasdoc = b.pk_cubasdoc and isnull(a.lock_flag,'N')='N' and (yjsb_flag=0 or yjsb_flag=1 ) and a.pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(a.dr,0)=0  ");  //����̵Ĺ���
	StringBuffer sql=new StringBuffer("pk_corp = '"+ce.getCorporation().getPk_corp()+"'  ");  //����̵Ĺ���
    return sql.toString();

}
/**
 * ָʾ�������2212��ʽ�������Ϊ�ձ�ʾ�������¼���ϵ������

 * @return java.lang.String
 */
@Override
public String getCodingRule() {
    return codeRule;
}
/**
 * �������кͷ������ӵ��ֶ�
 * �������ڣ�(2001-8-15 16:38:45)
 * @return java.lang.String
 */
@Override
public String getDocJoinField() {
    return "pk_receipt";
}
/**
 * ָʾĩ����־�ֶΡ�
 * �������ڣ�(2001-8-11 14:43:58)
 * @return java.lang.String
 */
@Override
public String getEndField() {
    return null;
}
/**
 * ָʾ���¼���ϵ�������ֶΡ�
 * �������ڣ�(2001-8-11 14:43:58)
 * @return java.lang.String
 */
@Override
public String getFatherField() {
    return null;
}
/**
 * ��ʾ�ֶ��б�
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
//add by houcq modify ���ӳ��ţ���Ƥ�ţ���ע�ֶ�2010-11-22
@Override
public java.lang.String[] getFieldCode() {
    //return new String[]{"c.billno","a.invcode","a.invname","a.invspec","a.invtype","a.def1","b.amount","b.inamount","b.packagweight","b.pk_receipt","b.pk_receipt_b"};
	return new String[]{"invcode","invname","invspec","invtype","def1","amount","inamount","packagweight","pk_receipt","pk_receipt_b","carnumber","tranno","memo"};
}
/**
 * ��ʾ�ֶ�������
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
//add by houcq modify ���ӳ��ţ���Ƥ�ţ���ע�ֶ�2010-11-22
@Override
public java.lang.String[] getFieldName() {
    return new String[]{"���ϱ���","��������","���","�ͺ�","��ɫ","��ͬ����","�����ջ���","��װ����(ǧ��/��)","������","����","����","��Ƥ��","��ע"};
}
/**
 * �����ֶ���
 * @return java.lang.String
 */
@Override
public String getPkFieldCode() {
    return "pk_receipt_b";
}
/**
 * ���ձ���
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getRefTitle() {
    return "�ջ�����-�ջ�֪ͨ��-��Ƥ�� -��Ӧ��";
}
/**
 * �������ݿ�������ͼ��
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getTableName() {
    //String table = " eh_stock_receipt_b b,bd_invmandoc d,bd_invbasdoc a,eh_stock_receipt c ";
	String table = " EH_VIEW_STOCK_RECEIPT ";
    return table;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-6-22 18:09:28)
 * @return java.lang.String
 */
@Override
public String getWherePart() {
    StringBuffer sqlWhere=new StringBuffer()
    //.append(" b.pk_invbasdoc = d.pk_invmandoc and d.pk_invbasdoc = a.pk_invbasdoc and b.pk_receipt=c.pk_receipt and issb='Y' and isnull(b.dr,0)=0 and isnull(c.dr,0)=0");
    .append(" pk_corp = '"+ce.getCorporation().getPk_corp()+"' ");
    return sqlWhere.toString();

}

public String getOrderPart() {
	// TODO Auto-generated method stub
	return " billno desc";
}


}

