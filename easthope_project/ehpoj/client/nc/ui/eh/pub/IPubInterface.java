package nc.ui.eh.pub;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public interface IPubInterface{
   
    // �ۿ۱���
    public static UFDouble DISCOUNTRATE= new PubTools().getDiscountrate();
    
    // ��Ʒ�Ƿ�����������
    public static UFBoolean CANCPFCK = new UFBoolean(false);
    
    // ԭ���Ƿ�����������
    public static UFBoolean CANYLFCK = new UFBoolean(false);
    
    // �̵�������
    public static UFDouble PDRATE= new PubTools().getPdrate();
    
}
