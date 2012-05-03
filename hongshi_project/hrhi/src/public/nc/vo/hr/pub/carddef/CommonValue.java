package nc.vo.hr.pub.carddef;

import java.util.HashMap;

import javax.swing.ImageIcon;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * 卡片报表使用到的相关常量定义类
 * @author wangxing
 *
 */
public class CommonValue extends nc.vo.hi.pub.CommonValue {
    	// 节点编码
    	public static final String MOUDLE_CODE_CARDDEF = "60070104";
    	public static final String MOUDLE_CODE_CARDAUTH = "60070107";
    	public static final String MOUDLE_CODE_CARDRPT = "600715";
    	
    	// 多语言公共文件夹名
    	public static final String MLANG_RES_COMMON = "common";

	//定义变量或区域格式索引字符串，此索引存储于IUFO CELL的单元扩展格式中
	public static final String CELL_VAR_FORMAT = "hr_card_variable_format";
	
	// 需要特出处理的人员信息主集的编码
	public static final String PSN_SPEC_MAIN_SET_CODE = "bd_psndoc";
	
	// 用来处理主集字段到任职子集字段替换的影射
	public static HashMap<String, String> PSN_SET_FIELDS_MAP01 = new HashMap<String, String>();
	
	// 用来处理任职子集字段到主集字段替换的影射
	public static HashMap<String, String> PSN_SET_FIELDS_MAP02 = new HashMap<String, String>();
	
	// 任职表和人员信息主集中的重复字段
	public static final String[] FIELDS_PSN_SUB = {
		"pk_psncl",
		"pk_deptdoc",
		"pk_postdoc",
		"pk_om_duty",
		"pk_jobserial",
		"pk_jobrank",
		"pk_detytype",
		"pk_dutyrank"
	};
	
	// 人员信息主集和任职表中重复的字段
	public static final String[] FIELDS_PSN_MAIN = {
		"pk_psncl",
		"pk_deptdoc",
		"pk_om_job",
		"dutyname",
		"jobseries",
		"jobrank",
		"series",
		"pk_dutyrank"
	};
	
	// 初始化影射
	static{
		for(int i=0; i<FIELDS_PSN_SUB.length; i++){
			PSN_SET_FIELDS_MAP01.put(FIELDS_PSN_MAIN[i], FIELDS_PSN_SUB[i]);
			PSN_SET_FIELDS_MAP02.put(FIELDS_PSN_SUB[i], FIELDS_PSN_MAIN[i]);
		}//end for
	}
	
	public static final String REFTYPE_NULL = "::REFTYPE_NULL::";
	// 比较操作符
	// 等于
	public static final int RELAOPER_EQUALS = 0;
	// 大于
	public static final int RELAOPER_BIGGER = 1;
	// 小于
	public static final int RELAOPER_SMALLER = 2;
	// 包含
	public static final int RELAOPER_LIKE = 3;
	
	// 人员信息业务子集编码数组
	public static final String[] PSN_BUSINESS_SET_CODES = {
		"hi_psndoc_deptchg", /* 任职情况 */
		"hi_psndoc_ctrt",/* 劳动合同 */
		"hi_psndoc_part",/* 兼职情况 */
		"hi_psndoc_training",/* 培训记录 */
		"hi_psndoc_ass",/* 考核记录 */
		"hi_psndoc_retire",/* 离退待遇 */
		"hi_psndoc_orgpsn",/* 虚拟组织 */
		"hi_psndoc_psnchg",/* 员工流动 */
		"hi_psndoc_dimission"/* 离职情况 */
	};
	
	
	// 人员类型
	// 全职
	public static final int PSNTYPE_NORMAL = 1;
	// 兼职
	public static final int PSNTYPE_PARTIME = 2;
	
	/**数据类型对应类**/
	public final static Class DATATYPE_CLASS_STRING = String.class; //字符 
	public final static Class DATATYPE_CLASS_INTEGER = Integer.class; //整数 
	public final static Class DATATYPE_CLASS_DECIMAL = UFDouble.class; //小数 
	public final static Class DATATYPE_CLASS_DATE = UFDate.class; //日期 
	public final static Class DATATYPE_CLASS_BOOLEAN = UFBoolean.class; //逻辑 
	public final static Class DATATYPE_CLASS_UFREF = String.class; //参照
	public final static Class DATATYPE_CLASS_PICTURE = ImageIcon.class; //图片 
	public final static Class DATATYPE_CLASS_TIME = UFDateTime.class; //时间 
	public final static Class DATATYPE_CLASS_MEMO = String.class; //备注
	public final static Class DATATYPE_CLASS_FORMULADATE = String.class; //日期公式型
	
	public static final String[] CHINESE_NUMS_LOWWER = {
		"零",
		"一",
		"二",
		"三",
		"四",
		"五",
		"六",
		"七",
		"八",
		"九",
		"十",
		"十一",
		"十二",
		"十三",
		"十四",
		"十五",
		"十六",
		"十七",
		"十八",
		"十九",
		"二十",
		"二十一",
		"二十二",
		"二十三",
		"二十四",
		"二十五",
		"二十六",
		"二十七",
		"二十八",
		"二十九",
		"三十",
		"三十一"
	};
	
	public static final String[] CHINESE_NUMS_UPPER = {
		"零",
		"壹",
		"贰",
		"叁",
		"肆",
		"伍",
		"陆",
		"柒",
		"捌",
		"玖",
		"拾",
		"拾壹",
		"拾贰",
		"拾叁",
		"拾肆",
		"拾伍",
		"拾陆",
		"拾柒",
		"拾捌",
		"拾玖",
		"贰拾",
		"贰拾壹",
		"贰拾贰",
		"贰拾叁",
		"贰拾肆",
		"贰拾伍",
		"贰拾陆",
		"贰拾柒",
		"贰拾捌",
		"贰拾玖",
		"叁拾",
		"叁拾壹"
	};
	/**
	 * 日期格式的名称
	 */
	public static final String[] DATEFORMAT_NAMES = {
		"1977-08-28",
		"1977.8.28",
		"77.8.28",
		"1977.8",
		"1977.08",
		"77.8",
		"77.08",
		"一九七七年八月二十八日",
		"一九七七年八月",
		"1977年8月28日",
		"1977年8月",
		"1977年08月28日",
		"1977年08月",
		"77年8月28日",
		"77年8月",
		"年限",
		"年份",
		"月份",
		"日份"
	};
	

	// 单元格信息项的前缀
	public static final String CELL_INFOITEM_PREFIX = "$";
	
	// 报表节点的类型，分为报表和文件夹两种类型
	// 文件夹
	public static final int RPT_NODETYPE_FOLDER = 1;
	// 报表
	public static final int RPT_NODETYPE_REPORT = 2;
	
	// 报表类型，目前只有卡片报表一种类型，其他类型可以以后扩展
	// 卡片报表
	public static final int RPT_TYPE_CARD = 1;
	// 花名册报表, add by walkfire 2007-08-02 V5.02
	public static final int RPT_TYPE_LIST = 2;
	
	// 类型数组
	public static int[] RPT_TYPE_ARRAY = new int[]{
		RPT_TYPE_CARD,
		RPT_TYPE_LIST
	}; 
	
	// 报表授权对象类型
	public static final int RPT_AUTH_REF_TYPE_USER = 1;
	public static final int RPT_AUTH_REF_TYPE_ROLE = 2;
	
	// 权限类型
	public static final int RPT_AUTH_NONE = 0;		// 无权限
	public static final int RPT_AUTH_VIEW = 1;		// 浏览权限
//	public static final int RPT_AUTH_FULL = 5;		// 所有权限

}
