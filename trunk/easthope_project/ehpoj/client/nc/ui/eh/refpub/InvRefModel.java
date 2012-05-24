/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefGridTreeModel;

/**
 * ���ܣ����ϲ���
 * @author newyear
 * @date  2008-4-12
 */
public class InvRefModel extends AbstractRefGridTreeModel {
    private nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();

   
	@SuppressWarnings("unused")
    private String cuserid = ce.getUser().getPrimaryKey();
	
	@SuppressWarnings("unused")
    private String codeRule = null;
/**
 * ElemRefModel2 ������ע�⡣
 */
public InvRefModel() {
	super();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-28 13:22:03)
 * @param corpID java.lang.String
 * @param jobTypeID java.lang.String
 * @param codeRule java.lang.String
 */
public InvRefModel(String sGroupID,String corpID,String codeRule) {
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
	return "pk_invcl";
}
/**
 * ������ֶ����顣
 * �������ڣ�(2001-8-14 18:58:43)
 * @return java.lang.String[]
 */
@Override
public java.lang.String[] getClassFieldCode() {
	return new String[] {"invclasscode", "invclassname","pk_invcl","pk_father"};
}
/**
 * ������к͵������ӵ��ֶ�---һ���Ƿ���������
 * �������ڣ�(2001-8-15 16:39:47)
 */
@Override
public String getClassJoinField() {
	return "pk_invcl";
}
/**
 * ���������
 * �������ڣ�(2001-8-14 18:56:49)
 * @return java.lang.String
 */
@Override
public String getClassTableName() {
	return "bd_invcl";
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
	return "pk_invcl";
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
	return "pk_father";
}
/**
 * ��ʾ�ֶ��б�
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public java.lang.String[] getFieldCode() {
	return new String[]{"invcode","invname","invmnecode","invspec","invtype","colour","def_2","pk_invcl","pk_invbasdoc"};
}
/**
 * ��ʾ�ֶ�������
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public java.lang.String[] getFieldName() {
	return new String[]{"��Ʒ����","��Ʒ����","������","���","�ͺ�","��ɫ","Ʒ��","������","����"};
}
/**
 * �����ֶ���
 * @return java.lang.String
 */
@Override
public String getPkFieldCode() {
	return "pk_invbasdoc";
}
/**
 * ���ձ���
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getRefTitle() {
	return "���ϵ���";
}
/**
 * �������ݿ�������ͼ��
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getTableName() {
	String table = "eh_invbasdoc ";
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
	
    .append(" pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(lock_flag,'N')='N' and isnull(dr,0)=0 ");
	
	return sqlWhere.toString();

}

/**
 * �˴����뷽��˵���� �������ڣ�(2003-2-19 9:24:17)
 * 
 * @return java.lang.String
 */
@Override
public java.lang.String[] getMnecodes() {
    return new String[]{"invcode","invname","invmnecode"};
}

}
