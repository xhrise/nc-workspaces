
package nc.vo.eh.iso.z0502005;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;

public class StockCheckBillVO extends HYBillVO implements IExAggVO {

private HashMap<String, CircularlyAccessibleValueObject[]> hmchidvos=new HashMap<String, CircularlyAccessibleValueObject[]>();

    
    public StockCheckBillVO() {
        super();
    }

    public CircularlyAccessibleValueObject[] getAllChildrenVO() {
        ArrayList<CircularlyAccessibleValueObject> al = new ArrayList<CircularlyAccessibleValueObject>();
        for (int i = 0; i < getTableCodes().length; i++) {
            CircularlyAccessibleValueObject[] cvos = getTableVO(getTableCodes()[i]);
            if (cvos != null && cvos.length > 0)
                al.addAll(Arrays.asList(cvos));
        }
        return (nc.vo.pub.SuperVO[]) al.toArray(new nc.vo.pub.SuperVO[0]);
    }

    public SuperVO[] getChildVOsByParentId(String tableCode, String parentid) {
        return null;
    }

    public String getDefaultTableCode() {
        return getTableCodes()[0];
    }

    public HashMap getHmEditingVOs() throws Exception {
        return null;
    }

    public String getParentId(SuperVO item) {
        return null;
    }

    public String[] getTableCodes() {
        return new String[] {"eh_stock_checkreport","eh_stock_checkreport_c"};
    }

    public String[] getTableNames() {
        return new String[] {"检测报告子表", "检测物料"};
    }

    public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
        Object o = hmchidvos.get(tableCode);
        if (o != null)
            return (nc.vo.pub.CircularlyAccessibleValueObject[]) o;
        return null;
    }

    public void setParentId(SuperVO item, String id) {
    }

    public void setTableVO(String tableCode,
            CircularlyAccessibleValueObject[] values) {
        if (values == null || values.length == 0)
            hmchidvos.remove(tableCode);
        else
            hmchidvos.put(tableCode, values);
    }
    
}

