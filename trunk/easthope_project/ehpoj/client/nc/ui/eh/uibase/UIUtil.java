package nc.ui.eh.uibase;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

import nc.vo.pub.lang.UFDouble;

public class UIUtil {
    /** �б�״̬�ı�ͷ*/
    public static final int LIST_HEAD=0;
    /** �б�״̬�ı���*/
    public static final int LIST_BODY=1;
    /** ��Ƭ״̬�ı���*/
    public static final int CARD_BODY=2;

    /**
     * �ӹ�������л�ȡfield��Ӧ�е�����ֵ
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
     * �޸�ʱͨ������ֵ��ȡ���ز�ֵ
     * @param inum��Ҫ��д����
     * @param obj�����޸ĵ�ClientEventHandler��
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
     * ����: ����ǲ���,���ز�����,���򷵻�null
     * @param item
     * @return
     * @return:UIRefPane
     * @author honghai 2007-10-23 ����09:31:56
     */
    public static UIRefPane getRefPane(BillItem item) {
        java.awt.Component c = item.getComponent();
        if (c instanceof UIRefPane)
            return (UIRefPane) c;
        return null;
    }
    
}
