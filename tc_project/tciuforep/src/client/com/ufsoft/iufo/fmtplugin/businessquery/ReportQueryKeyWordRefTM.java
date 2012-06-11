package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.resource.StringResource;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-9-15 15:45:01)
 * @author：刘良萍
 */
import nc.vo.iufo.keydef.KeyVO;
public class ReportQueryKeyWordRefTM
    extends nc.ui.pub.beans.table.VOTableModel {
    public static int COUNT_OF_COLUMN = 2;
    private String[] COLUMN_NAMES = { 
    		StringResource.getStringResource("miufopublic440"),   //"关键字名称"
    		StringResource.getStringResource("miufo1001388")  //"是否已被映射"
    		};
/**
 * KeyWordRefTableModel 构造子注解。
 * @param vos nc.vo.pub.ValueObject[]
 */
public ReportQueryKeyWordRefTM(nc.vo.pub.ValueObject[] vos) {
    super((KeyVO[]) vos);
}
/**
 * KeyWordRefTableModel 构造子注解。
 * @param c java.lang.Class
 */
public ReportQueryKeyWordRefTM(Class c) {
	super(c);
}
/**
 * KeyWordRefTableModel 构造子注解。
 * @param vo nc.vo.pub.ValueObject
 */
public ReportQueryKeyWordRefTM(nc.vo.pub.ValueObject vo) {
	super(vo);
}
/**
 * getColumnCount 方法注解。
 */
public int getColumnCount() {
    return COLUMN_NAMES.length;
}
/**
 * 返回列的名称
 */
public String getColumnName(int col) {
    return COLUMN_NAMES[col];
}
/**
 * getValueAt 方法注解。
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
//更新指定位置的指标，和col无关，col可以为任意值
public void setValueAt(Object obj, int row, int col) {
    if (col == 2) {
        //是否已映射列

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


