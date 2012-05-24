/*
 * 创建日期 2007-10-31
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.button;

/**
 * @author 洪海
 * 
 * 功能说明：定义自定义按钮
 */
public interface IEHButton {
    /** 按UAP要求,业务组定义按钮ID号须从100开始* */

    //模糊查看
    public static final int GENRENDETAIL= 101;
    
    //产品选择
    public static final int ChooseInv = 102;
    //合同附件
    public static final int STOCKCOPE = 103;
    //合同变更
    public static final int STOCKCHANGE = 104;
    //关闭
    public static final int SBCLOSE = 105;
    //版本变更（成品质量标准单录入）
    public static final int EditionChange = 106;
    //自动取数
    public static final int AUTOGETDATA = 107;
    
    //文档管理
    public static final int DOCMANAGE = 108;
    
    //单据锁定
    public static final int LOCKBILL = 109;
    
    //第一次读数(司磅单)
    public static final int FIRSTREADDATE = 110;
    //第一次打印
    public static final int FIRSTPRINT = 111;
    //第二次读磅
    public static final int SECONDREADDATE = 112;
    //第二次打印
    public static final int SECONDPRINT = 113;
    public static final int DJXMMANGER = 114;
    
    //打卡审批
    public static final int CARDAPPROVE = 115;
    
    //派工单中 下达
    public static final int SENDFA = 116;
    //派工单中 冻结
    public static final int CONGEAL= 117;
    //派工单中 解冻
    public static final int THAW = 118;
    
    //临时折扣
    public static final int TEMPLETDISCOUNT = 119;
    
    //确认
    public static final int CONFIRMBUG = 120;
    
    //计算 (原料库存月报表)
    public static final int CALCKCYBB = 121;
    
    //期间关闭
    public static final int PeriodClose = 122;
    //取消结帐
    public static final int PERIODCANCEL = 123;
    
    //付款单确认按钮
    public static final int SUREMONEY = 124;
    
    public static final int Prev = 125;
    
    public static final int Next = 126;
    
    public static final int CREATEVOUCHER=127;
   
    //凭证科目导入按钮
    public static final int INSERTCDOE=128; 
    
    //上一版本
    public static final int prevedition =129;
    //下一版本
    public static final int nextedition = 130;
    
    //业务操作
    public static final int BusinesBtn = 131;
    
    //销售计划Excel导入
    public static final int ExcelImport = 132;
    
    //销售计划表体不检测
    public static final int UnCheck= 133;
     
    //特采
    public static final int SpecialCG= 134;
    
    //更新到客商档案中资料
    public static final int ToCusbasdoc= 135;
    
    //确定生产此配方
    public static final int ConfirmSC = 136;
    
    //生成下月数据
    public static final int GenNextData = 137;
    
    //本月折扣期初建帐
    public static final int FirDayOfMonCut = 138;
    
    
    

}
