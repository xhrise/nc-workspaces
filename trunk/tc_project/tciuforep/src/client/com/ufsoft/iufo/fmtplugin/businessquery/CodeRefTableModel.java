package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.resource.StringResource;

/**
 * 编码参照表格模型。
 * 
 * 创建日期：(2003-9-17 20:22:22)
 * @author：刘良萍
 */
public class CodeRefTableModel extends nc.ui.pub.beans.table.VOTableModel {
	public static int COUNT_OF_COLUMNS = 2;
    private String[] m_aryObjColunmNames = { 
    		StringResource.getStringResource("miufopublic438"),  //"编码名称"
    		StringResource.getStringResource("miufo1001374")   //"编码规则"
			};
/**
 * CodeRefTableModel 构造子注解。
 * @param vos nc.vo.pub.ValueObject[]
 */
public CodeRefTableModel(nc.vo.pub.ValueObject[] vos) {
    super((CodeRefVO[]) vos);

}
/**
 * CodeRefTableModel 构造子注解。
 * @param c java.lang.Class
 */
public CodeRefTableModel(Class c) {
	super(c);
}
/**
 * CodeRefTableModel 构造子注解。
 * @param vo nc.vo.pub.ValueObject
 */
public CodeRefTableModel(nc.vo.pub.ValueObject vo) {
	super(vo);
}
/**
 * getColumnCount 方法注解。
 */
public int getColumnCount() {
    return m_aryObjColunmNames.length;
}
/**
 * 返回列的名称
 */
public String getColumnName(int col) {
    return m_aryObjColunmNames[col];
}
/**
 * getValueAt 方法注解。
 */
public Object getValueAt(int row, int col) {

    CodeRefVO vo = (CodeRefVO) getVO(row);
    switch (col) {
        case 0 :
            return vo.getCodeVO().getName();
        case 1 :
            return vo.getCodeRuleStr();
    }

    return null;
}
}


