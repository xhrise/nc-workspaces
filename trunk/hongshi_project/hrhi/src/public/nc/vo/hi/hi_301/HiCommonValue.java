package nc.vo.hi.hi_301;

/**
 * 此处插入类型说明。
 * 创建日期：(2002-5-15 11:22:17)
 * @author：代群义
 */
public class HiCommonValue extends nc.vo.pub.ValueObject {

	public final static String ORG_GRAD_PK = "HI000000000000000019"; //组织级次主键
	public final static String JOB_REQUIRE_TYPE_PK = "HI000000000000000018"; //任职资格主键
	public final static String JOB_GRADE_PK = "HI000000000000000022"; //岗位等级主键
	public final static String JOB_SERIES_PK = "HI000000000000000021"; //岗位序列主键
	public final static String DUTY_SERIES_PK =  "HI000000000000000020"; //职务类别主键
	public final static String COUNTRY_PK = "HI000000000000000002"; //国家/地区
	public final static String NATION_PK = "HI000000000000000003"; //民族
	public final static String SPECILITY_PK = "HI000000000000000005"; //专业
	public final static String KNOWLEDGE_PK = "HI000000000000000007"; //学历
	public final static String DEGREE_PK =  "HI000000000000000008"; //学位
	public final static String HEALTH_PK = "HI000000000000000010"; //健康状况
	public final static String MARRIAGE_PK =  "HI000000000000000011"; //婚姻状况
//以下是做交通部人才库项目时做的扩展
	public final static String PSNBASETYPE_PK = "0001AA10000000001NRQ";//"HI000000000000000053"; //类别
	public final static String POLITY_PK =  "0001AA10000000001NS2";//"HI000000000000000055"; //政治面貌
	public final static String TECHNICAL_TITLE_PK = "0001AA10000000001NSV";//"HI000000000000000056"; //职称
	public final static String DUTY_PK =  "0001AA10000000001NTB";//"HI000000000000000057"; //职务
	public final static String WORKCORP_PK =  "0001AA10000000001OJF";//"HI000000000000000058"; //工作单位
//交通部项目专用常量
	public final static String TABLENAME_WORKEXP = "hi_psndoc_work";
	public final static String TABLENAME_ARTICLE = "hi_psndoc_grpdef1";
	public final static String TABLENAME_TASK = "hi_psndoc_grpdef10";
	public final static String FLDNAME_DATE = "groupdef5";
	public final static String FLDNAME_DUTY = "hi_psndoc_deptchg.groupdef4";
	public final static String FLDNAME_PSNBASETYPE = "hi_psndoc_deptchg.groupdef2";
	public final static String FLDNAME_TECHTITLE = "groupdef1";
	public final static String FLDNAME_POLICY = "groupdef2";
	public final static String FLDNAME_ARTICLE = "groupdef4";
	public final static String FLDNAME_CHECKSUG = "hi_psndoc_deptchg.groupdef6";
	public final static String FLDNAME_UNITSUG = "hi_psndoc_deptchg.groupdef5";
	public final static String FLDNAME_PSNCODE = "hi_psndoc_deptchg.groupdef1";
	public final static String FLDNAME_BENIF = "groupdef3";
	public final static String FLDNAME_WORKUNIT = "hi_psndoc_deptchg.groupdef3";
	public final static String FLDNAME_EDUCATION = "hi_psndoc_edu.education";
	public final static String FLDNAME_DEGREE = "hi_psndoc_edu.degree";
	public final static String FLDNAME_MAJOR = "hi_psndoc_edu.major";
	public final static String FLDNAME_SCHOOL = "hi_psndoc_edu.school";

	public final static String[] tables = {"hi_psndoc_deptchg","hi_psndoc_edu"};
	//界面操作状态
	public final static int BROWSE = 1;
	public final static int INSERT = 2;
	public final static int UPDATE = 3;
	public final static int DELETE = 4;
	//4000AA10000000000000、4000AA10000000000001、40000000000000000010
/**
 * OmCommonValue 构造子注解。
 */
public HiCommonValue() {
	super();
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return null;
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
