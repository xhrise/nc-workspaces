/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefGridTreeModel;

/**
 * 功能：物料参照
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
 * ElemRefModel2 构造子注解。
 */
public InvRefModel() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-28 13:22:03)
 * @param corpID java.lang.String
 * @param jobTypeID java.lang.String
 * @param codeRule java.lang.String
 */
public InvRefModel(String sGroupID,String corpID,String codeRule) {
	super();
	this.codeRule = codeRule;
}
/**
 * 指示上下级关系－－子字段。
 * 创建日期：(2001-8-11 14:43:58)
 * @return java.lang.String
 */
@Override
public String getChildField() {
	return "pk_invcl";
}
/**
 * 分类表字段数组。
 * 创建日期：(2001-8-14 18:58:43)
 * @return java.lang.String[]
 */
@Override
public java.lang.String[] getClassFieldCode() {
	return new String[] {"invclasscode", "invclassname","pk_invcl","pk_father"};
}
/**
 * 分类表中和档案连接的字段---一般是分类主键。
 * 创建日期：(2001-8-15 16:39:47)
 */
@Override
public String getClassJoinField() {
	return "pk_invcl";
}
/**
 * 分类表名。
 * 创建日期：(2001-8-14 18:56:49)
 * @return java.lang.String
 */
@Override
public String getClassTableName() {
	return "bd_invcl";
}
/**
 * 分类表Where子句。
 * 创建日期：(2001-8-14 18:57:46)
 * @return java.lang.String
 */
@Override
public String getClassWherePart() {
    StringBuffer sql=new StringBuffer("").append(" isnull(dr,0)=0  ");
    
    return sql.toString();

}
/**
 * 指示编码规则（2212形式）。如果为空表示采用上下级关系构造树

 * @return java.lang.String
 */
@Override
public String getCodingRule() {
	return codeRule;
}
/**
 * 档案表中和分类连接的字段
 * 创建日期：(2001-8-15 16:38:45)
 * @return java.lang.String
 */
@Override
public String getDocJoinField() {
	return "pk_invcl";
}
/**
 * 指示末级标志字段。
 * 创建日期：(2001-8-11 14:43:58)
 * @return java.lang.String
 */
@Override
public String getEndField() {
	return null;
}
/**
 * 指示上下级关系－－父字段。
 * 创建日期：(2001-8-11 14:43:58)
 * @return java.lang.String
 */
@Override
public String getFatherField() {
	return "pk_father";
}
/**
 * 显示字段列表
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public java.lang.String[] getFieldCode() {
	return new String[]{"invcode","invname","invmnecode","invspec","invtype","colour","def_2","pk_invcl","pk_invbasdoc"};
}
/**
 * 显示字段中文名
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public java.lang.String[] getFieldName() {
	return new String[]{"产品编码","产品名称","助记码","规格","型号","颜色","品牌","树主键","主键"};
}
/**
 * 主键字段名
 * @return java.lang.String
 */
@Override
public String getPkFieldCode() {
	return "pk_invbasdoc";
}
/**
 * 参照标题
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getRefTitle() {
	return "物料档案";
}
/**
 * 参照数据库表或者视图名
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getTableName() {
	String table = "eh_invbasdoc ";
	return table;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-6-22 18:09:28)
 * @return java.lang.String
 */
@Override
public String getWherePart() {
	StringBuffer sqlWhere=new StringBuffer()
	
    .append(" pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(lock_flag,'N')='N' and isnull(dr,0)=0 ");
	
	return sqlWhere.toString();

}

/**
 * 此处插入方法说明。 创建日期：(2003-2-19 9:24:17)
 * 
 * @return java.lang.String
 */
@Override
public java.lang.String[] getMnecodes() {
    return new String[]{"invcode","invname","invmnecode"};
}

}
