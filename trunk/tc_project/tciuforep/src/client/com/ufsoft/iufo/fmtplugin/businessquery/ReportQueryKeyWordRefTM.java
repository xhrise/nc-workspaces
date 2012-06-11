package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.resource.StringResource;

/**
 * �˴���������������
 * �������ڣ�(2003-9-15 15:45:01)
 * @author������Ƽ
 */
import nc.vo.iufo.keydef.KeyVO;
public class ReportQueryKeyWordRefTM
    extends nc.ui.pub.beans.table.VOTableModel {
    public static int COUNT_OF_COLUMN = 2;
    private String[] COLUMN_NAMES = { 
    		StringResource.getStringResource("miufopublic440"),   //"�ؼ�������"
    		StringResource.getStringResource("miufo1001388")  //"�Ƿ��ѱ�ӳ��"
    		};
/**
 * KeyWordRefTableModel ������ע�⡣
 * @param vos nc.vo.pub.ValueObject[]
 */
public ReportQueryKeyWordRefTM(nc.vo.pub.ValueObject[] vos) {
    super((KeyVO[]) vos);
}
/**
 * KeyWordRefTableModel ������ע�⡣
 * @param c java.lang.Class
 */
public ReportQueryKeyWordRefTM(Class c) {
	super(c);
}
/**
 * KeyWordRefTableModel ������ע�⡣
 * @param vo nc.vo.pub.ValueObject
 */
public ReportQueryKeyWordRefTM(nc.vo.pub.ValueObject vo) {
	super(vo);
}
/**
 * getColumnCount ����ע�⡣
 */
public int getColumnCount() {
    return COLUMN_NAMES.length;
}
/**
 * �����е�����
 */
public String getColumnName(int col) {
    return COLUMN_NAMES[col];
}
/**
 * getValueAt ����ע�⡣
 */
public Object getValueAt(int row, int col) {
    ReportQueryRefKeyWordVO vo = (ReportQueryRefKeyWordVO) getVO(row);
    switch (col) {
        case 0 :
            return vo.getName();
        case 1 :
            return ReportQueryMeasRefTM.getStrsOfMapType()[vo.getMapStatus()];
    }
    return null;
}
//����ָ��λ�õ�ָ�꣬��col�޹أ�col����Ϊ����ֵ
public void setValueAt(Object obj, int row, int col) {
    if (col == 2) {
        //�Ƿ���ӳ����

        ReportQueryRefKeyWordVO rqRefKeyWordVO =
            (ReportQueryRefKeyWordVO) getVOs().get(row);
        if (rqRefKeyWordVO != null) {
            String strValue = (String) obj;
            int nMapStatus = 0;
            if (ReportQueryMeasRefTM.getStrsOfMapType()[1].equals(strValue)) {
                nMapStatus = 1;
            } else
                if (ReportQueryMeasRefTM.getStrsOfMapType()[2].equals(strValue)) {
                    nMapStatus = 2;
                }
            rqRefKeyWordVO.setMapStatus(nMapStatus);
        }
    }
    return;
}
}


