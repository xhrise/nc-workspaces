package nc.vo.hi.hi_301;

/**
 * 人员信息的ExctendedAggregatedValueObject实现。
 * 创建日期：(2004-5-13 9:47:53)
 * @author：Administrator
 */
import java.util.*;
import nc.vo.pub.*;
import nc.vo.hi.hi_301.validator.*;
import nc.vo.hi.pub.CommonValue;
public class PersonEAVO extends nc.vo.pub.ExtendedAggregatedValueObject implements Cloneable{

	//人员基本信息集的字段
	public static final String[] psndocFields =
		{
			"amcode",
			"clerkcode",
			"clerkflag",
			"indocflag",
			"pk_corp",
			"pk_deptdoc",
			"pk_om_job",
			"pk_psncl",
			"pk_psndoc",
			"psnclscope",
			"psncode",
//			"psnname",//v50 del
			"sealdate",
			"indutydate",//V35 add 
			"jobrank",//V35 add
			"jobseries",//V35 add
			"regulardata",//V35 add
			"series",//V35 add
			"recruitresource",//V35 add by zhyan 2006-03-02
			"tbm_prop",//V35 add
			"timecardid",//V35 add
			"regular",//V35
			"showorder",//V35
			"insource",//V35
			"outmethod",//V35
			"iscalovertime",//V35
			"wastopdate",//V35
			"poststat",//V35
			"pk_dutyrank",//V35
			"outdutydate",//V35
			"dutyname",//V35			
			"clerkcode",//V35
			"clerkflag",//V35
			"isreferenced",
			"directleader",	
			"pk_clerkclass",
			"pk_psntype",
			"onpostdate",
			"groupdef1",
			"groupdef2",
			"groupdef3",
			"groupdef4",
			"groupdef5",
			"groupdef6",
			"groupdef7",
			"groupdef8",
			"groupdef9",
			"groupdef10",
			"groupdef11",
			"groupdef12",
			"groupdef13",
			"groupdef14",
			"groupdef15",
			"groupdef16",
			"groupdef17",
			"groupdef18",
			"groupdef19",
			"groupdef20",
			"def1",//add by zhyan 2006-04-26 for upgrade
			"def2",
			"def3",
			"def4",
			"def5",
			"def6",
			"def7",
			"def8",
			"def9",
			"def10",
			"def11",
			"def12",
			"def13",
			"def14",
			"def15",
			"def16",
			"def17",
			"def18",
			"def19",
			"def20"
			};

	//人员辅助信息集的字段
	public static final String[] accpsndocFields =
		{
			"pk_psnbasdoc",//V35 pk_psndoc -->pk_psnbasdoc
			"belong_pk_corp",
//			"psncode",//V35 add
//			"psnname",//V35 add
			"penelauth",
			"id",
			"psnname",//v50 add
			"ssnum",
			"sex",
			"birthdate",
			"nationality",
			"bloodtype",
			"marital",
			"health",
			"photo",
			"joinworkdate",
			"joinsysdate",//V35 add
			"postalcode",
			"addr",
			"email",
			"officephone",
			"mobile",
			"bp",//以上字段是V35中 bd_psnbasdoc文档中明确说明的			
			"approveflag",//以下字段 暂作保留  bd_psnbasdoc 没有说明
			"characterrpr",
			"city",
			"computerlevel",
			"country",
			"dataoperator",		
			"employform",
			"froeignlang",
			"frolanlevel",
			"functiontype",
			"homephone",
			"interest",
			"joinpolitydate",
			"marriagedate",			
			"nativeplace",			
			"permanreside",
			"polity",			
			"province",
//			"recruitresource",//del by zhyan 2006-03-02 属于管理档案，不属于基本档案
			"skilllevel",
			"titletechpost",
			"usedname",
			"fileaddress",//v50 add
//			"clerkcode",//V35 移到bd_psndoc
//			"clerkflag",//V35 移到bd_psndoc
//			"dutyname",//V35 移到bd_psndoc
//			"outdutydate",//V35 移到bd_psndoc
			//"indocflag",//V35
			//"indutydate",//V35 移到bd_psndoc				
//			"jobrank", //V35移到bd_psndoc	
//			"jobseries", //V35 移到bd_psndoc				
//			"regulardata",//V35 移到bd_psndoc	
//			"series",//V35 移到bd_psndoc				
//			"tbm_prop",//V35 移到bd_psndoc	
//			"timecardid",//V35 移到bd_psndoc				
//			"showorder",//V35 移到bd_psndoc	
//			"regular",//V35 移到bd_psndoc			
//			"insource",//V35  移到bd_psndoc	
//			"outmethod",//V35 移到bd_psndoc	
//			"iscalovertime",//V35 移到bd_psndoc	
//			"outdutytype",//V35 去掉
//			"outdutyreason",//V35 去掉
//			"outdutynote",//V35 去掉
//			"outdutyto",//V35 去掉
//			"wastopdate",//V35
//			"poststat",//V35
//			"pk_dutyrank"//V35
			"basgroupdef1",
			"basgroupdef2",
			"basgroupdef3",
			"basgroupdef4",
			"basgroupdef5",
			"basgroupdef6",
			"basgroupdef7",
			"basgroupdef8",
			"basgroupdef9",
			"basgroupdef10",
			"basgroupdef11",
			"basgroupdef12",
			"basgroupdef13",
			"basgroupdef14",
			"basgroupdef15",
			"basgroupdef16",
			"basgroupdef17",
			"basgroupdef18",
			"basgroupdef19",
			"basgroupdef20" 
			};
	//人员其他情况信息集
	//public static final String[] mainaddpsndocFields =
		//{
			//"indate",
			//"insource",
			//"outdate",
			//"outmethod",
			//"penelauth",
			//"memo",
			//"period",
			//"corpdef1",
			//"corpdef2",
			//"corpdef3",
			//"corpdef4",
			//"corpdef5",
			//"corpdef6",
			//"corpdef7",
			//"corpdef8",
			//"corpdef9",
			//"corpdef10",
			//"groupdef1",
			//"groupdef2",
			//"groupdef3",
			//"groupdef4",
			//"groupdef5",
			//"groupdef6",
			//"groupdef7",
			//"groupdef8",
			//"groupdef9",
			//"groupdef10" };

	//主表字段
	public static String[] mainFields;

	//高速映射
	private static final Hashtable pfields = new Hashtable();
	private static final Hashtable afields = new Hashtable();
	//private static final Hashtable mfields = new Hashtable();

	//静态构造函数，初始化映射表
	static {
		init();
	}

	//主表信息
	private GeneralVO psndocVO;
	private GeneralVO accpsndocVO;
	//private GeneralVO mainaddpsndocVO;

	//子表信息
	private Hashtable subtables = new Hashtable();

	//人员主键
	private java.lang.String pk_psndoc;
	private java.lang.String pk_psnbasdoc;//V35 add
	private int jobtype = 0;
/**
 * PersonEAVO 构造子注解。
 */
public PersonEAVO() {
	super();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 10:46:19)
 * @param subtable nc.vo.hi.hi_301.SubTable
 */
public void addSubtable(SubTable subtable) {
	subtables.put(subtable.getTableCode(), subtable);
}
	public Object clone(){
	    PersonEAVO copy;
	    try{
		    copy=(PersonEAVO)super.clone();
	    }catch(Exception e){
		    copy=new PersonEAVO();
	    }
	    copy.psndocVO= (psndocVO==null?null:(GeneralVO)psndocVO.clone());
	    copy.accpsndocVO=(accpsndocVO==null?null:(GeneralVO)accpsndocVO.clone());
	    //copy.mainaddpsndocVO=(GeneralVO)mainaddpsndocVO.clone();
	    copy.subtables=new Hashtable();
	    Enumeration enums=subtables.keys();
	    while(enums.hasMoreElements()){
		    String key=(String)enums.nextElement();
		    SubTable subtable=(SubTable)subtables.get(key);
		    copy.subtables.put(key,subtable.clone());
	    }
	    return copy;
	}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 9:55:24)
 * @return nc.vo.hi.hi_301.GeneralVO
 */
public GeneralVO getAccpsndocVO() {
	return accpsndocVO;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-12-16 16:02:33)
 * @return int
 */
public int getJobType() {
	return jobtype;
}
/**
 * 返回母表VO。
 * 创建日期：(01-3-20 17:32:28)
 * @return nc.vo.pub.ValueObject
 */
public nc.vo.pub.CircularlyAccessibleValueObject getParentVO() {
	return new nc.vo.pub.CircularlyAccessibleValueObject() {
		/**
		 * 此处插入方法说明。
		 * 创建日期：(01-3-20 17:26:03)
		 * @return java.lang.String[]
		 */
		public java.lang.String[] getAttributeNames() {
			return mainFields;
		}
		/**
		 * 此处插入方法说明。
		 * 创建日期：(01-3-20 17:24:29)
		 * @param key java.lang.String
		 */
		public Object getAttributeValue(String name) {
			if ((pfields.get(name) != null || name.startsWith("corpdef")
						|| name.startsWith("groupdef") || name
						.startsWith(CommonValue.UFFORMULA_DATA))
						&& psndocVO != null)
				return psndocVO.getAttributeValue(name);
			else
				if (accpsndocVO != null ){
					return accpsndocVO.getAttributeValue(name);
				}
			//else
				//if (mfields.get(name) != null && mainaddpsndocVO != null)
					//return mainaddpsndocVO.getAttributeValue(name);

			return null;
		}
		/**
		 * 返回数值对象的显示名称。
		 *
		 * 创建日期：(2001-2-15 14:18:08)
		 * @return java.lang.String 返回数值对象的显示名称。
		 */
		public String getEntityName() {
			return "main";
		}
		/**
		 * 此处插入方法说明。
		 * 创建日期：(01-3-20 17:24:29)
		 * @param key java.lang.String
		 */
		public void setAttributeValue(String name, Object value) {
			if (pfields.get(name) != null || name.startsWith("corpdef")
						|| name.startsWith("groupdef")
						|| name.startsWith(CommonValue.UFFORMULA_DATA)) {
					if (psndocVO == null) {
						psndocVO = new GeneralVO();
					}
				psndocVO.setAttributeValue(name, value);
			} else
				if (name.startsWith("basgroupdef")|| afields.get(name) != null) {
					if (accpsndocVO == null) {
						accpsndocVO = new GeneralVO();
					}
					accpsndocVO.setAttributeValue(name, value);
				} /*else
					if (mfields.get(name) != null) {
						if (mainaddpsndocVO == null) {
							mainaddpsndocVO = new GeneralVO();
						}
						mainaddpsndocVO.setAttributeValue(name, value);
					}*/
		}
		/**
		 * 验证对象各属性之间的数据逻辑正确性。
		 *
		 * 创建日期：(2001-2-15 11:47:35)
		 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
		 *     ValidationException，对错误进行解释。
		 */
		public void validate() throws nc.vo.pub.ValidationException {
		}
	};
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 17:08:26)
 * @return java.lang.String
 */
public java.lang.String getPk_psndoc() {
	return pk_psndoc;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 9:54:49)
 * @return nc.vo.hi.hi_301.GeneralVO
 */
public GeneralVO getPsndocVO() {
	return psndocVO;
}
/**
 * 获取tableCode得子表。
 * 创建日期：(2004-5-21 11:58:47)
 * @return nc.vo.hi.hi_301.SubTable
 * @param tableCode java.lang.String
 */
public SubTable getSubTable(String tableCode) {
	SubTable subtable = (SubTable) subtables.get(tableCode);
	if (subtable == null){
	    subtable=new SubTable();
	    subtable.setTableCode(tableCode);
	    subtables.put(tableCode, subtable);
	}
	return subtable;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 10:45:29)
 * @return java.util.Hashtable
 */
public java.util.Hashtable getSubtables() {
	return subtables;
}
/**
 * 返回各个子表的编码。
 * 创建日期：(01-3-20 17:36:56)
 * @return String[]
 */
public java.lang.String[] getTableCodes() {
	return new String[]{"bd_psndoc","bd_psnbasdoc"/*,"hi_psndoc_mainadd"*/};
}
/**
 * 返回各个子表的中文名称。
 * 创建日期：(01-3-20 17:36:56)
 * @return String[]
 */
public java.lang.String[] getTableNames() {
	return new String[]{nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000088")/*@res "人员基本情况"*/,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000177")/*@res "人员辅助情况"*//*,"人员其他情况"*/};
}
/**
 * 返回某个子表的VO数组。
 * 创建日期：(01-3-20 17:36:56)
 * @return nc.vo.pub.ValueObject[]
 * @param tableCode String 子表的编码
 */
public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
	SubTable subtable = (SubTable) subtables.get(tableCode);
	if (subtable == null)
		return new nc.vo.pub.CircularlyAccessibleValueObject[0];
	Vector v=subtable.getRecords();
	return (CircularlyAccessibleValueObject[])v.toArray(new CircularlyAccessibleValueObject[0]);
}
private static void init() {
	// 初始化哈希表
	for (int i = 0; i < psndocFields.length; i++)
		pfields.put(psndocFields[i], psndocFields[i]);
	for (int i = 0; i < accpsndocFields.length; i++)
		afields.put(accpsndocFields[i], accpsndocFields[i]);
	//for (int i = 0; i < mainaddpsndocFields.length; i++)
		//mfields.put(mainaddpsndocFields[i], mainaddpsndocFields[i]);
	//初始化表头子段
	mainFields = new String[psndocFields.length + accpsndocFields.length/*+mainaddpsndocFields.length*/];
	System.arraycopy(psndocFields, 0, mainFields, 0, psndocFields.length);
	System.arraycopy(
		accpsndocFields,
		0,
		mainFields,
		psndocFields.length,
		accpsndocFields.length);
	//System.arraycopy(
		//mainaddpsndocFields,
		//0,
		//mainFields,
		//psndocFields.length+accpsndocFields.length,
		//mainaddpsndocFields.length);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 10:48:40)
 * @param tableCode java.lang.String
 */
public void removeSubtable(String tableCode) {
	subtables.remove(tableCode);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 10:49:17)
 * @param subtable nc.vo.hi.hi_301.SubTable
 */
public void removeSubtable(SubTable subtable) {
	subtables.remove(subtable.getTableCode());
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 9:55:24)
 * @param newAccpsndocVO nc.vo.hi.hi_301.GeneralVO
 */
public void setAccpsndocVO(GeneralVO newAccpsndocVO) {
	accpsndocVO = newAccpsndocVO;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-12-16 16:02:17)
 * @param jobtype int
 */
public void setJobtype(int jobtype) {
	this.jobtype = jobtype;
}

	/**
	 * 母表VO的setter方法。 创建日期：(01-3-20 17:32:28)
	 * 
	 * @param parent
	 *            nc.vo.pub.ValueObject 母表VO
	 */
	public void setParentVO(nc.vo.pub.CircularlyAccessibleValueObject parent) {
		if (psndocVO == null) {
			psndocVO = new GeneralVO();
		}
		if (accpsndocVO == null) {
			accpsndocVO = new GeneralVO();
		}
		for (int i = 0; i < psndocFields.length; i++)
			psndocVO.setAttributeValue(psndocFields[i], parent
					.getAttributeValue(psndocFields[i]));
		for (int i = 0; i < accpsndocFields.length; i++)
			accpsndocVO.setAttributeValue(accpsndocFields[i], parent
					.getAttributeValue(accpsndocFields[i]));
	}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 17:08:26)
 * @param newPk_psndoc java.lang.String
 */
public void setPk_psndoc(java.lang.String newPk_psndoc) {
	pk_psndoc = newPk_psndoc;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 9:54:49)
 * @param newPsndocVO nc.vo.hi.hi_301.GeneralVO
 */
public void setPsndocVO(GeneralVO newPsndocVO) {
	psndocVO = newPsndocVO;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-13 10:45:29)
 * @param newSubtables java.util.Hashtable
 */
public void setSubtables(java.util.Hashtable newSubtables) {
	subtables = newSubtables;
}
/**
 * 某个子表VO数组的setter方法。
 * 创建日期：(01-3-20 17:36:56)
 * @param tableCode String 子表编码
 * @param values nc.vo.pub.ValueObject[] 子表VO数组
 */
public void setTableVO(String tableCode, CircularlyAccessibleValueObject[] values) {
	SubTable subtable = (SubTable) subtables.get(tableCode);
	if (subtable == null) {
		subtable = new SubTable();
		subtable.setTableCode(tableCode);
		subtables.put(tableCode, subtable);
	}
	Vector v = new Vector();
	for (int i = 0; i < values.length; i++)
		v.addElement(values[i]);
	subtable.setRecords(v);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-14 16:06:40)
 * @exception java.lang.Exception 异常说明。
 */
public void validate(String tableCode) throws Exception {
	//获取当前表的检验器
	IValidator validator = AbstractValidator.getValidator(tableCode);

	if (validator != null) {
		//检验规则是先校验纪录，再校验纪录间，最后校验跨信息集
		if (tableCode.equals("bd_psndoc")) {
			try {
				psndocVO.setAttributeValue("psnname",accpsndocVO.getFieldValue("psnname"));
				validator.validate(psndocVO);
				// validator.validate(new CircularlyAccessibleValueObject[] { psndocVO });
				validator.validate(this);
			} catch (Exception e) {
				ValidException ve = null;
				if(e instanceof ValidException){
					ve = (ValidException)e;
				}else{
					ve = new ValidException(e.getMessage());
				}
				ve.setTableCode(tableCode);
				ve.setLineNo(-1);
				if (e instanceof FieldValidationException)
					ve.setFieldCode(((FieldValidationException) e).getFieldCode());
				throw ve;
			}
		} else
			if (tableCode.equals("bd_psnbasdoc")) {
				try {
					accpsndocVO.setAttributeValue("pk_psnbasdoc",pk_psnbasdoc);	                
					validator.validate(accpsndocVO);
					validator.validate(new CircularlyAccessibleValueObject[] { accpsndocVO });
					validator.validate(this);
//					accpsndocVO.removeAttributeName("psnname");						
				} catch (Exception e) {
					ValidException ve = null;
					if(e instanceof ValidException){
						ve = (ValidException)e;
					}else{
						ve = new ValidException(e.getMessage());
					}
					ve.setTableCode(tableCode);
					ve.setLineNo(-1);
					if (e instanceof FieldValidationException){
						ve.setFieldCode(((FieldValidationException) e).getFieldCode());
					}
					throw ve;
				}
			} else {
				CircularlyAccessibleValueObject[] records = getTableVO(tableCode);
				if (records != null) {
	                AbstractValidator.checkNullRecord(records);
					for (int i = 0; i < records.length; i++)
						try {
							validator.validate(records[i]);
						} catch (Exception e) {
							ValidException ve = null;
							if(e instanceof ValidException){
								ve = (ValidException)e;
							}else{
								ve = new ValidException(e.getMessage());
							}
							ve.setTableCode(tableCode);
							ve.setLineNo(i);
							if (e instanceof FieldValidationException)
								ve.setFieldCode(((FieldValidationException) e).getFieldCode());
							throw ve;
						}
					try {
						validator.validate(records);
						validator.validate(this);
					} catch (Exception e) {
						ValidException ve = null;
						if(e instanceof ValidException){
							ve = (ValidException)e;
						}else{
							ve = new ValidException(e.getMessage());
						}
						ve.setTableCode(tableCode);
						ve.setLineNo(-1);
						if (e instanceof FieldValidationException)
							ve.setFieldCode(((FieldValidationException) e).getFieldCode());
						throw ve;
					}
				}
			}
	}
}
/**
 * @return 返回 pk_psnbasdoc。
 */
public java.lang.String getPk_psnbasdoc() {
	return pk_psnbasdoc;
}
/**
 * @param pk_psnbasdoc 要设置的 pk_psnbasdoc。
 */
public void setPk_psnbasdoc(java.lang.String pk_psnbasdoc) {
	this.pk_psnbasdoc = pk_psnbasdoc;
}
}
