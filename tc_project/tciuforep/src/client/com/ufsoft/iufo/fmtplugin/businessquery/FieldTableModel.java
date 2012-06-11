package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.MoveTableModel;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.OrderByFld;
import com.ufsoft.iufo.resource.StringResource;

/**
 @update
   2003-10-28 19:35 liulp
   字段映射对象FieldMap添加Name属性，MapName的意义改为(映射类型+指标/关键字的PK)
 @end
 * 选择查询项目的TableModel，可实现已选和被选
 * 创建日期：(2003-9-3 14:44:09)
 * @author：王海涛
 */

import nc.vo.iuforeport.businessquery.SelectFldVO;
public class FieldTableModel extends MoveTableModel {
    private boolean m_bWaitForSel = true;
    private String[] m_strsWaitFor = new String[] { 
    		StringResource.getStringResource("miufopublic115"),  //"名称"
			StringResource.getStringResource("miufo1001375")   //"描述"
			};
    private String[] m_strsHaveSel = new String[] { 
    		StringResource.getStringResource("miufopublic115"),  //"名称"
			StringResource.getStringResource("miufo1001277"),   //"指标/关键字"
			StringResource.getStringResource("miufo1001315")   //"排序方式"
			};

    public static final int COLUMN_NAME = 0; //名称
    public static final int COLUMN_DISC = 1; //描述
    public static final int COLUMN_MEASURE_OR_KEYWORD = 1; //指标/关键字
    public static final int COLUMN_SORT = 2; //排序方式

/**
 * FieldTableModel 构造子注解。
 */
public FieldTableModel() {
	super();
}
/**
 * FieldTableModel 构造子注解。
 * 
 * 创建日期：(2003-9-10 11:36:33)
 * @author：刘良萍
 * @param fullItem java.util.Vector - 表模型的初始数据
 * @param bWaitForSel boolean - 是待选列表，还是已选的报表查询字段列表
 */
public FieldTableModel(java.util.Vector fullItem, boolean bWaitForSel) {
	super(fullItem);

	m_bWaitForSel = bWaitForSel;

	if (bWaitForSel) {
		setHead(this.m_strsWaitFor);
	} else {
		setHead(this.m_strsHaveSel);
	}
}
/**
 * getValueAt 方法注解。******子类需要重载此访法********
 */
public Object getValueAt(int r, int c) {
    Object returnObj = null;

    Object item = getAll().get(r);
    if (item == null) {
        return null;
    }
    //待选查询字段表模型
    if (m_bWaitForSel) {
        SelectFldVO vo = (SelectFldVO) item;
        switch (c) {
            case COLUMN_NAME :
                returnObj = vo.getFldname();
                break;
            case COLUMN_DISC :
                returnObj = vo.getNote();
                break;
        }
    } else {
        //已选报表查询字段表模型
        ReportSelectFldVO vo = (ReportSelectFldVO) item;
        switch (c) {
            case COLUMN_NAME :
                returnObj = vo.getSelectFldVO().getFldname();
                break;
            case COLUMN_MEASURE_OR_KEYWORD :
                returnObj = vo.getName();
                break;
            case COLUMN_SORT :
                //根据项目中的排序方式设置显示
                String strSort = vo.getSort();
            	String strSortTypeUIStr = getSortTypeUIStr(strSort);
            	if(strSortTypeUIStr!=null && strSortTypeUIStr.length()>0){
            		return strSortTypeUIStr;
            	}
//                if (OrderByFld.ORDER_NULL_STR.equals(strSort))
//                    returnObj = getSortType()[0];
//                else
//                    if (OrderByFld.ORDER_AESC_STR.equals(strSort))
//                        returnObj = getSortType()[1];
//                    else
//                        if (OrderByFld.ORDER_DESC_STR.equals(strSort))
//                            returnObj = getSortType()[2];
                break;
        }
    }

    return returnObj;
}
//单元是否可编辑	******子类需要重载此访法********
public boolean isCellEditable(int r, int c) {
    if (!m_bWaitForSel)
        if (c == 2)
            //排序列
            return true;
    return false;
}
//设置单元******子类需要重载此访法********
public void setValueAt(Object obj, int r, int c) {
    if (!m_bWaitForSel) {
        ReportSelectFldVO item = (ReportSelectFldVO) getAll().get(r);
        if (item == null) {
            return;
        }
        if (c == COLUMN_SORT) {
            //设置排序方式
			if(getSortTypeUIStr(OrderByFld.ORDER_NULL_STR).equals(obj))
				{
				//无序
				item.setSort(OrderByFld.ORDER_NULL_STR);
			}else if( getSortTypeUIStr(OrderByFld.ORDER_AESC_STR).equals(obj))
			{
				//升序
				item.setSort(OrderByFld.ORDER_AESC_STR);
			}else if( getSortTypeUIStr(OrderByFld.ORDER_DESC_STR).equals(obj))
			{
				//降序
				item.setSort(OrderByFld.ORDER_DESC_STR);
			}
//            if (getSortType()[0].equals(obj)) {
//                //无序
//                item.setSort(OrderByFld.ORDER_NULL_STR);
//            } else
//                if (getSortType()[1].equals(obj)) {
//                    //升序
//                    item.setSort(OrderByFld.ORDER_AESC_STR);
//                } else
//                    if (getSortType()[2].equals(obj)) {
//                        //降序
//                        item.setSort(OrderByFld.ORDER_DESC_STR);
//                    }
        } else
            if (c == COLUMN_MEASURE_OR_KEYWORD) {
                item.setName((String) obj);
            }
    }
}
}


