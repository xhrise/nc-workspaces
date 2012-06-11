package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.MoveTableModel;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.OrderByFld;
import com.ufsoft.iufo.resource.StringResource;

/**
 @update
   2003-10-28 19:35 liulp
   �ֶ�ӳ�����FieldMap���Name���ԣ�MapName�������Ϊ(ӳ������+ָ��/�ؼ��ֵ�PK)
 @end
 * ѡ���ѯ��Ŀ��TableModel����ʵ����ѡ�ͱ�ѡ
 * �������ڣ�(2003-9-3 14:44:09)
 * @author��������
 */

import nc.vo.iuforeport.businessquery.SelectFldVO;
public class FieldTableModel extends MoveTableModel {
    private boolean m_bWaitForSel = true;
    private String[] m_strsWaitFor = new String[] { 
    		StringResource.getStringResource("miufopublic115"),  //"����"
			StringResource.getStringResource("miufo1001375")   //"����"
			};
    private String[] m_strsHaveSel = new String[] { 
    		StringResource.getStringResource("miufopublic115"),  //"����"
			StringResource.getStringResource("miufo1001277"),   //"ָ��/�ؼ���"
			StringResource.getStringResource("miufo1001315")   //"����ʽ"
			};

    public static final int COLUMN_NAME = 0; //����
    public static final int COLUMN_DISC = 1; //����
    public static final int COLUMN_MEASURE_OR_KEYWORD = 1; //ָ��/�ؼ���
    public static final int COLUMN_SORT = 2; //����ʽ

/**
 * FieldTableModel ������ע�⡣
 */
public FieldTableModel() {
	super();
}
/**
 * FieldTableModel ������ע�⡣
 * 
 * �������ڣ�(2003-9-10 11:36:33)
 * @author������Ƽ
 * @param fullItem java.util.Vector - ��ģ�͵ĳ�ʼ����
 * @param bWaitForSel boolean - �Ǵ�ѡ�б�������ѡ�ı����ѯ�ֶ��б�
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
 * getValueAt ����ע�⡣******������Ҫ���ش˷÷�********
 */
public Object getValueAt(int r, int c) {
    Object returnObj = null;

    Object item = getAll().get(r);
    if (item == null) {
        return null;
    }
    //��ѡ��ѯ�ֶα�ģ��
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
        //��ѡ�����ѯ�ֶα�ģ��
        ReportSelectFldVO vo = (ReportSelectFldVO) item;
        switch (c) {
            case COLUMN_NAME :
                returnObj = vo.getSelectFldVO().getFldname();
                break;
            case COLUMN_MEASURE_OR_KEYWORD :
                returnObj = vo.getName();
                break;
            case COLUMN_SORT :
                //������Ŀ�е�����ʽ������ʾ
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
//��Ԫ�Ƿ�ɱ༭	******������Ҫ���ش˷÷�********
public boolean isCellEditable(int r, int c) {
    if (!m_bWaitForSel)
        if (c == 2)
            //������
            return true;
    return false;
}
//���õ�Ԫ******������Ҫ���ش˷÷�********
public void setValueAt(Object obj, int r, int c) {
    if (!m_bWaitForSel) {
        ReportSelectFldVO item = (ReportSelectFldVO) getAll().get(r);
        if (item == null) {
            return;
        }
        if (c == COLUMN_SORT) {
            //��������ʽ
			if(getSortTypeUIStr(OrderByFld.ORDER_NULL_STR).equals(obj))
				{
				//����
				item.setSort(OrderByFld.ORDER_NULL_STR);
			}else if( getSortTypeUIStr(OrderByFld.ORDER_AESC_STR).equals(obj))
			{
				//����
				item.setSort(OrderByFld.ORDER_AESC_STR);
			}else if( getSortTypeUIStr(OrderByFld.ORDER_DESC_STR).equals(obj))
			{
				//����
				item.setSort(OrderByFld.ORDER_DESC_STR);
			}
//            if (getSortType()[0].equals(obj)) {
//                //����
//                item.setSort(OrderByFld.ORDER_NULL_STR);
//            } else
//                if (getSortType()[1].equals(obj)) {
//                    //����
//                    item.setSort(OrderByFld.ORDER_AESC_STR);
//                } else
//                    if (getSortType()[2].equals(obj)) {
//                        //����
//                        item.setSort(OrderByFld.ORDER_DESC_STR);
//                    }
        } else
            if (c == COLUMN_MEASURE_OR_KEYWORD) {
                item.setName((String) obj);
            }
    }
}
}


