package nc.ui.eh.pub;


/*
 * 功能说明:对常用的一些下拉菜单进行定义
 */
public interface ICombobox {
	
	
	/*物料档案*/
	public static final String[] STR_BIGTYPE = new String[]{"原材料","半成品","成品"};
    
    /*客商类别*/
    public static final String[] STR_custprop = new String[]{"客户","供应商","客商"};
    
    /*政策类型*/
    public static final String[] STR_polictype = new String[]{"月度","年度","季度","时段"};
    
    /*折扣类型*/
    public static final String[] STR_discounttype = new String[]{"折扣额","折扣率"};
    
    /*发票类型*/
    public static final String[] STR_invice = new String[]{"普通发票","普通增值税发票","专用增值税发票"};
    
    /*处理方式*/
    public static final String[] STR_dealtype = new String[]{"回机","继续销售"};
    
    /*收获方式(原料质量标准单)*/
    public static final String[] STR_treatype = new String[]{"扣重收货","扣价收货"};
    
    /*司磅单的取数类型*/
    public static final String[] STR_SBGETDATETYPE = new String[]{"手动","自动"};
    
    /*司磅单类型*/
    //public static final String[] STR_SBBILLTYPE = new String[]{"自制","原料司磅","成品司磅","其他司磅"};
    public static final String[] STR_SBBILLTYPE = new String[]{"自制","原料司磅","成品司磅"};//modify by houcq 2011-06-22
    /*司磅单状态*/
    public static final String[] STR_SBSTATUS = new String[]{"挂起","结束"};
    
    /*合同状态*/
    public static final String[] STR_CONTSTYPE = new String[]{"确定","未确定"};
    
    /*抽样单类型（抽样单）*/
    public static final String[] STR_TYPEFLAG = new String[]{"自动","手动"};
    
    /*检测结果（检测报告）*/
    public static final String[] STR_RESULE = new String[]{"合格","不合格","扣重收货","扣价收货","扣重扣价收货"};
    
    /*二次折扣中计算方式*/
    public static final String[] STR_JSMETHOD = new String[]{"按量计算","按额计算"};
    
    /*二次折扣中结算方式*/
    public static final String[] STR_JSTYPE = new String[]{"固定全额结算","固定累进结算","浮动全额结算","浮动累进结算"};
    
    /*二次折扣中产品计算方式*/
    public static final String[] STR_INVJSTYPE = new String[]{"单产品单独计算","多产品汇总计算"};
    
    /*成本对象中对象类型*/
    public static final String[] STR_OBJTYPE = new String[]{"成品","部门","车间"};
    
    /*发票类别*/
    public static final String[] INVOICETYPE = new String[]{"普通发票","普通增值税发票","专用增值税发票"};
    
    //BUG所属类型
    public static final String[] STR_BUGTYPE = new String[]{"严重错误","一般错误","显示错误","优化功能","新增功能"};
    
    //收款单
    public static final String[] STR_pk_sfkfs = new String[]{"客户","员工","其他"};
      
    //U8凭证模板 借贷方向
    public static final String[] STR_DIRECTION = new String[]{"借","贷"};
    
    //材料调拨单 调拨类型
    public static final String[] STR_DBTYPE = new String[]{"原料","成品"};
    
    /*收获通知单司磅类型定义*/
    public static final String[] STR_YJSB = new String[]{"全部司磅","部分司磅","无需司磅"};
    
    /*收货通知单检测值的定义*/
    public static final String[] STR_ALLCHECK = new String[]{"全部检测","部分免检","全部免检"};
    
    /*检测报告表体的检测结果类型的定义*/
    public static final String[] STR_PASS_FLAG = new String[]{"合格","不合格","扣重收货","扣价收货"};
    
    /*仓库维护中的仓库分类*/
    public static final String[] STR_IS_FLAG = new String[]{"原料库","成品库"};
    /*成本费用分摊中的类型*/
    public static final String[] STR_INVTYPE = new String[]{"粉碎","制粒","其他","",};//modify by houcq 2011-03-22    
    /*上下线指标*/
    public static final String[] CW_SXZB= new String[]{"上线指标","下线指标"};
    /*是否扣重*/
    public static final String[] CW_KZKJ= new String[]{"扣重","扣价",""};
    /*是否是最高相*/
    public static final String[] CW_HIGH= new String[]{"最高","最低",""};
    /*分组*/
    public static final String[] CW_GROUP= new String[]{"第一组","第二组","第三组","第四组","第五组","第六组","第七组","第八组","第九组",""};
    /*凭证中成本核算类型*/
    public static final String[] CW_CBHS= new String[]{"电费","燃料费","工资","制造费"};
    
    /*BOM中大料，小料**/
    public static final String[] BOM_INVPTYPE = new String[]{"大料","小料",""};
    public static final String[] CW_CLCKPZ = new String[]{"包装","非包装"};
    /*询价方式**/
    public static final String[] CG_XJFS = new String[]{"采购点","供应商"};
    
    /*销售旬计划中旬标记*/
    public static final String[] Period_flag = new String[]{"上旬","中旬","下旬"};
    
    /*特殊采购计划*/
    public static final String[] SpecCG_flag = new String[]{"在标准内","不在标准内","药品/添加剂"};
    
    /*采购决策中物料类别*/
    public static final String[] CG_DECISION = new String[]{"原料","包装标签"};
    
    /*采购决策中后十天行情判断*/
    public static final String[] CG_HQ = new String[]{" ","上涨10元","上涨20元","上涨更多","下跌10元","下跌20元","下跌更多","涨跌10元以内"};
    
    /*供应商性质*/
    public static final String[] Gys_xz = new String[]{" ","生产厂商","厂家代理","中间商","个体"};
    
}

