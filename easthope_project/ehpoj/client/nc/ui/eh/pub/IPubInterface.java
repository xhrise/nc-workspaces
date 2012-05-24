package nc.ui.eh.pub;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public interface IPubInterface{
   
    // 折扣比率
    public static UFDouble DISCOUNTRATE= new PubTools().getDiscountrate();
    
    // 成品是否允许负库存出库
    public static UFBoolean CANCPFCK = new UFBoolean(false);
    
    // 原料是否允许负库存出库
    public static UFBoolean CANYLFCK = new UFBoolean(false);
    
    // 盘点差异比例
    public static UFDouble PDRATE= new PubTools().getPdrate();
    
}
