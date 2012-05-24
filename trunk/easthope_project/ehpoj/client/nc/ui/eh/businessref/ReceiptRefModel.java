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
 * 功能：收货通知单参照
 * @author zqy
 * @date  2008-6-27 13:36:23
 */

public class ReceiptRefModel  extends AbstractRefGridTreeModel{
     
    private nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();

    
    @SuppressWarnings("unused")
    private String cuserid = ce.getUser().getPrimaryKey();
    
    private String codeRule = null;
/**
 * ElemRefModel2 构造子注解。
 */
public ReceiptRefModel() {
    super();
}
 
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-28 13:22:03)
 * @param corpID java.lang.String
 * @param jobTypeID java.lang.String
 * @param codeRule java.lang.String
 */
public ReceiptRefModel(String sGroupID,String corpID,String codeRule) {
    super();
    this.codeRule = codeRule;
}
/**
 * 指示上下级关系－－子字段。
 * 住子表连接字段
 * 创建日期：(2001-8-11 14:43:58)
 * @return java.lang.String
 */
@Override
public String getChildField() {
    return "pk_receipt";
}
/**
 * 分类表字段数组。
 * 左边树显示的字段
 * 创建日期：(2001-8-14 18:58:43)
 * @return java.lang.String[]
 */
//add by houcq 2010-11-22 增加receiptdate字段
@Override
public java.lang.String[] getClassFieldCode() {
    return new String[] {"receiptdate","billno", "tranno","custname","pk_receipt"};
}

/**
 * 分类表中和档案连接的字段---一般是分类主键。
 * 连接字段
 * 创建日期：(2001-8-15 16:39:47)
 */
@Override
public String getClassJoinField() {
    return "pk_receipt";
}
/**
 * 分类表名。
 * 创建日期：(2001-8-14 18:56:49)
 * @return java.lang.String
 */
@Override
public String getClassTableName() {
    //return "eh_stock_receipt a, bd_cumandoc c,bd_cubasdoc b";
    return " EH_VIEW_STOCK_RECEIPT_TWO ";
}

/**
 * 默认显示字段中的显示字段数----表示显示前几个字段
 */
public int getClassDefaultFieldCount() {
	return 4;
}
/**
 * 分类表Where子句。
 * 创建日期：(2001-8-14 18:57:46)
 * @return java.lang.String
 */
@Override
public String getClassWherePart() {
    //StringBuffer sql=new StringBuffer("a.pk_cubasdoc = c.pk_cumandoc and c.pk_cubasdoc = b.pk_cubasdoc and isnull(a.lock_flag,'N')='N' and (yjsb_flag=0 or yjsb_flag=1 ) and a.pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(a.dr,0)=0  ");  //与客商的关联
	StringBuffer sql=new StringBuffer("pk_corp = '"+ce.getCorporation().getPk_corp()+"'  ");  //与客商的关联
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
    return "pk_receipt";
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
    return null;
}
/**
 * 显示字段列表
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
//add by houcq modify 增加车号，车皮号，备注字段2010-11-22
@Override
public java.lang.String[] getFieldCode() {
    //return new String[]{"c.billno","a.invcode","a.invname","a.invspec","a.invtype","a.def1","b.amount","b.inamount","b.packagweight","b.pk_receipt","b.pk_receipt_b"};
	return new String[]{"invcode","invname","invspec","invtype","def1","amount","inamount","packagweight","pk_receipt","pk_receipt_b","carnumber","tranno","memo"};
}
/**
 * 显示字段中文名
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
//add by houcq modify 增加车号，车皮号，备注字段2010-11-22
@Override
public java.lang.String[] getFieldName() {
    return new String[]{"物料编码","物料名称","规格","型号","颜色","合同数量","本次收货量","包装物重(千克/件)","树主键","主键","车号","车皮号","备注"};
}
/**
 * 主键字段名
 * @return java.lang.String
 */
@Override
public String getPkFieldCode() {
    return "pk_receipt_b";
}
/**
 * 参照标题
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getRefTitle() {
    return "收货日期-收货通知单-车皮号 -供应商";
}
/**
 * 参照数据库表或者视图名
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
@Override
public String getTableName() {
    //String table = " eh_stock_receipt_b b,bd_invmandoc d,bd_invbasdoc a,eh_stock_receipt c ";
	String table = " EH_VIEW_STOCK_RECEIPT ";
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
    //.append(" b.pk_invbasdoc = d.pk_invmandoc and d.pk_invbasdoc = a.pk_invbasdoc and b.pk_receipt=c.pk_receipt and issb='Y' and isnull(b.dr,0)=0 and isnull(c.dr,0)=0");
    .append(" pk_corp = '"+ce.getCorporation().getPk_corp()+"' ");
    return sqlWhere.toString();

}

public String getOrderPart() {
	// TODO Auto-generated method stub
	return " billno desc";
}


}

