package nc.ui.eh.uibase;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

import nc.vo.pub.lang.UFDouble;

public class UIUtil {
    /** 列表状态的表头*/
    public static final int LIST_HEAD=0;
    /** 列表状态的表体*/
    public static final int LIST_BODY=1;
    /** 卡片状态的表体*/
    public static final int CARD_BODY=2;

    /**
     * 从管理界面中获取field对应列的所有值
     * @param ui
     * @param field
     * @param pos
     * @return
     */
    public static Object[] retriveValueFromUI(BillManageUI ui,String field,int pos){
        BillModel model=null;
        switch (pos) {
        case LIST_HEAD:
            model=ui.getBillListPanel().getHeadBillModel();
            break;
        case LIST_BODY:
            model=ui.getBillListPanel().getBodyBillModel();
            break;
        case CARD_BODY:
            model=ui.getBillCardPanel().getBillModel();
            break;
            default:
                return null;
        }
        int rowCount=model.getRowCount();
        Object []values=new Object[rowCount];
        for (int i = 0; i < rowCount; i++) {
            Object value=model.getValueAt(i, field);
            values[i]=value;
        }
        return values;
    }
    
    public static void execBodyFormulas(BillModel bodymodel){
        int count=bodymodel.getRowCount();
        for (int i = 0; i < count; i++) {
            bodymodel.execEditFormulas(i);
        }
    }
    /**
     * 修改时通过返回值获取返回差值
     * @param inum　要回写的数
     * @param obj　　修改的ClientEventHandler类
     * @author honghai
     * @return
     */
    public static void getOrinumOFAdd(String inum,CircularlyAccessibleValueObject[] objVO){
        CircularlyAccessibleValueObject[] vos = objVO;
        UFDouble orinum = null;
        for (int i = 0; i < vos.length; i++) {
            orinum = (UFDouble) vos[i].getAttributeValue(inum);
            vos[i].setAttributeValue("orinum", orinum);
        }
        
    }
    /**
     * 功能: 如果是参照,返回参照类,否则返回null
     * @param item
     * @return
     * @return:UIRefPane
     * @author honghai 2007-10-23 上午09:31:56
     */
    public static UIRefPane getRefPane(BillItem item) {
        java.awt.Component c = item.getComponent();
        if (c instanceof UIRefPane)
            return (UIRefPane) c;
        return null;
    }
    
}
