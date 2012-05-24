/**
 * @(#)ReceiptRefModel.java	V3.1 2008-6-27
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.refpub;

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
    
    @SuppressWarnings("unused")
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
 * �������ڣ�(2001-8-11 14:43:58)
 * @return java.lang.String
 */
@Override
public String getChildField() {
    return "pk_receipt";
}
/**
 * ������ֶ����顣
 * �������ڣ�(2001-8-14 18:58:43)
 * @return java.lang.String[]
 */
@Override
public java.lang.String[] getClassFieldCode() {
    return new String[] {"billno", "pk_cubasdoc","retail_flag","pk_receipt"};
}
/**
 * ������к͵������ӵ��ֶ�---һ���Ƿ���������
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
    return "eh_stock_receipt";
}
/**
 * �����Where�Ӿ䡣
 * �������ڣ�(2001-8-14 18:57:46)
 * @return java.lang.String
 */
@Override
public String getClassWherePart() {
    StringBuffer sql=new StringBuffer("").append(" isnull(dr,0)=0  ");
    
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
    return "pk_receipt";
}
/**
 * ��ʾ�ֶ��б�
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public java.lang.String[] getFieldCode() {
    return new String[]{"vinvcode","vinvname","gg","invtype","colour","amount","inamount","packagweight","pk_receipt","pk_receipt_b"};
}
/**
 * ��ʾ�ֶ�������
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public java.lang.String[] getFieldName() {
    return new String[]{"���ϱ���","��������","���","�ͺ�","��ɫ","��ͬ����","�����ջ���","��װ����(ǧ��/��)","������","����"};
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
    return "�ջ�֪ͨ��";
}
/**
 * �������ݿ�������ͼ��
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getTableName() {
    String table = "eh_stock_receipt_b";
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
    
    .append("pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ");
    
    return sqlWhere.toString();

}

}

