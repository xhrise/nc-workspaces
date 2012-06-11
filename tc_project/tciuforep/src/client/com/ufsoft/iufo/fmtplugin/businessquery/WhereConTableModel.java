package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.resource.StringResource;

import nc.vo.iuforeport.businessquery.WhereCondVO;
/**
 * �����ѯ��where�������ñ�ģ�͡�
 *
 * �������ڣ�(2003-9-16 13:36:07)
 * @author������Ƽ
 */
public class WhereConTableModel
    extends com.ufsoft.iufo.fmtplugin.dataprocess.basedef.MoveTableModel {
    private String[] m_strsWhereConHeads =
        new String[] { 
    		StringResource.getStringResource("miufo1001437"),  //"��ϵ��"
			StringResource.getStringResource("miufo1001438"),  //"�ֶ�"
			StringResource.getStringResource("miufo1001439"),   //"�ȽϷ�"
			StringResource.getStringResource("miufo1001440")   //"�Ҳ�����"
			};

    public static final int COLUMN_RELATION_SYNBOL = 0; //��ϵ��
    public static final int COLUMN_FIELD = 1; //�ֶ�
    public static final int COLUMN_COMPARE_SYNBOL = 2; //�ȽϷ�
    public static final int COLUMN_RIGHT_OPE = 3; //�Ҳ�����2

/**
 * WhereConTableModel ������ע�⡣
 */
public WhereConTableModel() {
	super();
}
/**
 * WhereConTableModel ������ע�⡣
 * @param fullItem java.util.Vector
 */
public WhereConTableModel(java.util.Vector fullItem) {
    super(fullItem);

    setHead(this.m_strsWhereConHeads);

}
/**
 * getValueAt ����ע�⡣******������Ҫ���ش˷÷�********
 *
 * �������ڣ�(2003-9-16 13:40:35)
 * @author������Ƽ
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
//��Ԫ�Ƿ�ɱ༭	******������Ҫ���ش˷÷�********
public boolean isCellEditable(int row, int col) {
    if (row == 0 && col == 0)
        return false;
    else {
        return true;
    }
}
//���õ�Ԫ******������Ҫ���ش˷÷�********
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


