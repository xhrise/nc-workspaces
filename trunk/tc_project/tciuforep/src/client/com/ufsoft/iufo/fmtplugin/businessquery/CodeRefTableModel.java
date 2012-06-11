package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.resource.StringResource;

/**
 * ������ձ��ģ�͡�
 * 
 * �������ڣ�(2003-9-17 20:22:22)
 * @author������Ƽ
 */
public class CodeRefTableModel extends nc.ui.pub.beans.table.VOTableModel {
	public static int COUNT_OF_COLUMNS = 2;
    private String[] m_aryObjColunmNames = { 
    		StringResource.getStringResource("miufopublic438"),  //"��������"
    		StringResource.getStringResource("miufo1001374")   //"�������"
			};
/**
 * CodeRefTableModel ������ע�⡣
 * @param vos nc.vo.pub.ValueObject[]
 */
public CodeRefTableModel(nc.vo.pub.ValueObject[] vos) {
    super((CodeRefVO[]) vos);

}
/**
 * CodeRefTableModel ������ע�⡣
 * @param c java.lang.Class
 */
public CodeRefTableModel(Class c) {
	super(c);
}
/**
 * CodeRefTableModel ������ע�⡣
 * @param vo nc.vo.pub.ValueObject
 */
public CodeRefTableModel(nc.vo.pub.ValueObject vo) {
	super(vo);
}
/**
 * getColumnCount ����ע�⡣
 */
public int getColumnCount() {
    return m_aryObjColunmNames.length;
}
/**
 * �����е�����
 */
public String getColumnName(int col) {
    return m_aryObjColunmNames[col];
}
/**
 * getValueAt ����ע�⡣
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


