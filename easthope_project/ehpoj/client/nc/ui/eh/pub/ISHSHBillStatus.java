package nc.ui.eh.pub;

import nc.vo.trade.pub.IBillStatus;

public interface ISHSHBillStatus extends IBillStatus{
    //  单据状态名称
    public static String[]  strStateRemark = new String[] { "审批不通过", "审批通过", "审批进行中", "提交态", "作废态",
            "冲销态", "终止(结算)态", "冻结态", "自由态","已关闭","执行中","已完成","已确认"  ,"检验完毕","处理完毕","发运中","待入库","已锁定","未锁定"};
    //  已关闭
    public static int CLOSE=9;
    //  执行中
    public static int EXECUTING=10;
    //  已完成
    public static int COMPLETE=11;
    //  已确认
    public static int CONFIRM=12;
    //  检验完毕
    public static int CHECKED=13;
    //  处理完毕
    public static int HANDLED=14;
    //  发运中
    public static int DELIVERING=15;
    //  待入库
    public static int ENTERING=16;
    //  锁定
    public static int LOCKED=17;
    //  未锁定
    public static int UNLOCKED=18;
}
