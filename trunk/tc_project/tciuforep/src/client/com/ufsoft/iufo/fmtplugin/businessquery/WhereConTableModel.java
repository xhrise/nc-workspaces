package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.resource.StringResource;

import nc.vo.iuforeport.businessquery.WhereCondVO;
/**
 * 报表查询的where条件设置表模型。
 *
 * 创建日期：(2003-9-16 13:36:07)
 * @author：刘良萍
 */
public class WhereConTableModel
    extends com.ufsoft.iufo.fmtplugin.dataprocess.basedef.MoveTableModel {
    private String[] m_strsWhereConHeads =
        new String[] { 
    		StringResource.getStringResource("miufo1001437"),  //"关系符"
			StringResource.getStringResource("miufo1001438"),  //"字段"
			StringResource.getStringResource("miufo1001439"),   //"比较符"
			StringResource.getStringResource("miufo1001440")   //"右操作数"
			};

    public static final int COLUMN_RELATION_SYNBOL = 0; //关系符
    public static final int COLUMN_FIELD = 1; //字段
    public static final int COLUMN_COMPARE_SYNBOL = 2; //比较符
    public static final int COLUMN_RIGHT_OPE = 3; //右操作数2

/**
 * WhereConTableModel 构造子注解。
 */
public WhereConTableModel() {
	super();
}
/**
 * WhereConTableModel 构造子注解。
 * @param fullItem java.util.Vector
 */
public WhereConTableModel(java.util.Vector fullItem) {
    super(fullItem);

    setHead(this.m_strsWhereConHeads);

}
/**
 * getValueAt 方法注解。******子类需要重载此访法********
 *
 * 创建日期：(2003-9-16 13:40:35)
 * @author：刘良萍
 * @return java.lang.Object
 * @param r int
 * @param c int
 */
public Object getValueAt(int r, int c) {
    Object returnObj = null;

    Object item = getAll().get(r);
    if (item == null) {
        return null;
    }

    WhereCondVO vo = (WhereCondVO) item;
    switch (c) {
        case COLUMN_RELATION_SYNBOL :
            returnObj = vo.getRelationflag();
            break;
        case COLUMN_FIELD :
            returnObj = vo.getLeftfld();
            break;
        case COLUMN_COMPARE_SYNBOL :
            returnObj = vo.getOperator();
            break;
        case COLUMN_RIGHT_OPE :
            returnObj = vo.getRightfld();
            break;
    }

    return returnObj;
}
//单元是否可编辑	******子类需要重载此访法********
public boolean isCellEditable(int row, int col) {
    if (row == 0 && col == 0)
        return false;
    else {
        return true;
    }
}
//设置单元******子类需要重载此访法********
public void setValueAt(Object obj, int r, int c) {
    WhereCondVO item = (WhereCondVO) getAll().get(r);
    if (item == null) {
        return;
    }

    String strValue = (String) obj;
    switch (c) {
        case COLUMN_RELATION_SYNBOL :
            item.setRelationflag(strValue);
            break;
        case COLUMN_FIELD :
            item.setLeftfld(strValue);
            break;
        case COLUMN_COMPARE_SYNBOL :
            item.setOperator(strValue);
            break;
        case COLUMN_RIGHT_OPE :
            item.setRightfld(strValue);
            break;
    }
    return;
}
}


