package nc.vo.yto.business;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class PsnbasdocVO extends SuperVO{

//	private String addr = null;
//	private UFDate birthdate = null;                
//	private String id=null;               
//	private String pk_corp=null;          
//	private String psnname=null;            
//	private String sex=null;         
//	private String ssnum=null;                
//	private String indocflag=null;      
//	private String nationality=null;           
//	private String nativeplace=null;    
//	private String basgroupdef9=null; 
//	private int approveflag = 0;
//	
//	@Override
//	public String getPKFieldName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public String getParentPKFieldName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public String getTableName() {
//		
//		return "bd_psnbasdoc";
//	}
	
	  public static final String ADDR = "addr";
	  public static final String APPROVEFLAG = "approveflag";
	  public static final String BIRTHDATE = "birthdate";
	  public static final String BLOODTYPE = "bloodtype";
	  public static final String BP = "bp";
	  public static final String CHARACTERRPR = "characterrpr";
	  public static final String CITY = "city";
	  public static final String COMPUTERLEVEL = "computerlevel";
	  public static final String COUNTRY = "country";
	  public static final String DATAOPERATOR = "dataoperator";
	  public static final String DR = "dr";
	  public static final String EMAIL = "email";
	  public static final String EMPLOYFORM = "employform";
	  public static final String FROEIGNLANG = "froeignlang";
	  public static final String FROLANLEVEL = "frolanlevel";
	  public static final String FUNCTIONTYPE = "functiontype";
	  public static final String HEALTH = "health";
	  public static final String HOMEPHONE = "homephone";
	  public static final String ID = "id";
	  public static final String INDOCFLAG = "indocflag";
	  public static final String INTEREST = "interest";
	  public static final String JOINPOLITYDATE = "joinpolitydate";
	  public static final String JOINSYSDATE = "joinsysdate";
	  public static final String JOINWORKDATE = "joinworkdate";
	  public static final String MARITAL = "marital";
	  public static final String MARRIAGEDATE = "marriagedate";
	  public static final String MOBILE = "mobile";
	  public static final String NATIONALITY = "nationality";
	  public static final String NATIVEPLACE = "nativeplace";
	  public static final String OFFICEPHONE = "officephone";
	  public static final String PENELAUTH = "penelauth";
	  public static final String PERMANRESIDE = "permanreside";
	  public static final String PHOTO = "photo";
	  public static final String PK_CORP = "pk_corp";
	  public static final String PK_PSNBASDOC = "pk_psnbasdoc";
	  public static final String POLITY = "polity";
	  public static final String POSTALCODE = "postalcode";
	  public static final String PROVINCE = "province";
	  public static final String PSNNAME = "psnname";
	  public static final String REG_ACCEPT_RESEARCH_FLAG = "reg_accept_research_flag";
	  public static final String REG_BACKGROUND_FINDINGS = "reg_background_findings";
	  public static final String REG_CONTRACT_END_DATE = "reg_contract_end_date";
	  public static final String REG_CONTRACT_UNCHAIN_FLAG = "reg_contract_unchain_flag";
	  public static final String REG_DATA_SOURCE = "reg_data_source";
	  public static final String REG_DATE = "reg_date";
	  public static final String REG_DUE_DATE = "reg_due_date";
	  public static final String REG_EXAMINATION_RESULTS = "reg_examination_results";
	  public static final String REG_EXPECT_WAGE = "reg_expect_wage";
	  public static final String REG_FILE_PLACE = "reg_file_place";
	  public static final String REG_FILE_TRANSFER_FLAG = "reg_file_transfer_flag";
	  public static final String REG_OLD_PAY_BEFORE_TAX = "reg_old_pay_before_tax";
	  public static final String REG_PERSONAL_FORTES = "reg_personal_fortes";
	  public static final String REG_PK_ACTIVITY = "reg_pk_activity";
	  public static final String REG_PK_CHANNEL = "reg_pk_channel";
	  public static final String REG_RECOMMENDATION_VIEWS = "reg_recommendation_views";
	  public static final String REG_RECOMMENDER = "reg_recommender";
	  public static final String REG_RECOMMENDER_CONTACT_FORM = "reg_recommender_contact_form";
	  public static final String REG_SEAL_FLAG = "reg_seal_flag";
	  public static final String REG_SELF_EVALUATION = "reg_self_evaluation";
	  public static final String REG_SHARE_FLAG = "reg_share_flag";
	  public static final String REG_STATUS = "reg_status";
	  public static final String REG_STATUS_DATE = "reg_status_date";
	  public static final String REG_TALENT_BANK = "reg_talent_bank_flag";
	  public static final String REG_TALENT_BANK_DATE = "reg_talent_bank_date";
	  public static final String REG_TALENT_FLOW = "reg_talent_flow";
	  public static final String REG_TALENT_NOTE = "reg_talent_note";
	  public static final String REG_TALENT_TYPE = "reg_talent_type";
	  public static final String REG_TYPE = "reg_type";
	  public static final String REG_WORK_ADDR = "reg_work_addr";
	  public static final String REG_WORK_TYPE = "reg_work_type";
	  public static final String REG_WORKING_UNIT = "reg_working_unit";
	  public static final String REMARK = "remark";
	  public static final String SEX = "sex";
	  public static final String SKILLLEVEL = "skilllevel";
	  public static final String SSNUM = "ssnum";
	  public static final String TABLE_NAME = "rm_psnbasdoc";
	  public static final String TITLETECHPOST = "titletechpost";
	  public static final String TS = "ts";
	  public static final String USEDNAME = "usedname";
	  private String addr;
	  private Integer approveflag;
	  private String basgroupdef1;
	  private String basgroupdef10;
	  private String basgroupdef100;
	  private String basgroupdef101;
	  private String basgroupdef102;
	  private String basgroupdef103;
	  private String basgroupdef104;
	  private String basgroupdef105;
	  private String basgroupdef106;
	  private String basgroupdef107;
	  private String basgroupdef108;
	  private String basgroupdef109;
	  private String basgroupdef11;
	  private String basgroupdef110;
	  private String basgroupdef111;
	  private String basgroupdef112;
	  private String basgroupdef113;
	  private String basgroupdef114;
	  private String basgroupdef115;
	  private String basgroupdef116;
	  private String basgroupdef117;
	  private String basgroupdef118;
	  private String basgroupdef119;
	  private String basgroupdef12;
	  private String basgroupdef120;
	  private String basgroupdef121;
	  private String basgroupdef122;
	  private String basgroupdef123;
	  private String basgroupdef124;
	  private String basgroupdef125;
	  private String basgroupdef126;
	  private String basgroupdef127;
	  private String basgroupdef128;
	  private String basgroupdef129;
	  private String basgroupdef13;
	  private String basgroupdef130;
	  private String basgroupdef14;
	  private String basgroupdef15;
	  private String basgroupdef16;
	  private String basgroupdef17;
	  private String basgroupdef18;
	  private String basgroupdef19;
	  private String basgroupdef2;
	  private String basgroupdef20;
	  private String basgroupdef21;
	  private String basgroupdef22;
	  private String basgroupdef23;
	  private String basgroupdef24;
	  private String basgroupdef25;
	  private String basgroupdef26;
	  private String basgroupdef27;
	  private String basgroupdef28;
	  private String basgroupdef29;
	  private String basgroupdef3;
	  private String basgroupdef30;
	  private String basgroupdef31;
	  private String basgroupdef32;
	  private String basgroupdef33;
	  private String basgroupdef34;
	  private String basgroupdef35;
	  private String basgroupdef36;
	  private String basgroupdef37;
	  private String basgroupdef38;
	  private String basgroupdef39;
	  private String basgroupdef4;
	  private String basgroupdef40;
	  private String basgroupdef41;
	  private String basgroupdef42;
	  private String basgroupdef43;
	  private String basgroupdef44;
	  private String basgroupdef45;
	  private String basgroupdef46;
	  private String basgroupdef47;
	  private String basgroupdef48;
	  private String basgroupdef49;
	  private String basgroupdef5;
	  private String basgroupdef50;
	  private String basgroupdef51;
	  private String basgroupdef52;
	  private String basgroupdef53;
	  private String basgroupdef54;
	  private String basgroupdef55;
	  private String basgroupdef56;
	  private String basgroupdef57;
	  private String basgroupdef58;
	  private String basgroupdef59;
	  private String basgroupdef6;
	  private String basgroupdef60;
	  private String basgroupdef61;
	  private String basgroupdef62;
	  private String basgroupdef63;
	  private String basgroupdef64;
	  private String basgroupdef65;
	  private String basgroupdef66;
	  private String basgroupdef67;
	  private String basgroupdef68;
	  private String basgroupdef69;
	  private String basgroupdef7;
	  private String basgroupdef70;
	  private String basgroupdef71;
	  private String basgroupdef72;
	  private String basgroupdef73;
	  private String basgroupdef74;
	  private String basgroupdef75;
	  private String basgroupdef76;
	  private String basgroupdef77;
	  private String basgroupdef78;
	  private String basgroupdef79;
	  private String basgroupdef8;
	  private String basgroupdef80;
	  private String basgroupdef81;
	  private String basgroupdef82;
	  private String basgroupdef83;
	  private String basgroupdef84;
	  private String basgroupdef85;
	  private String basgroupdef86;
	  private String basgroupdef87;
	  private String basgroupdef88;
	  private String basgroupdef89;
	  private String basgroupdef9;
	  private String basgroupdef90;
	  private String basgroupdef91;
	  private String basgroupdef92;
	  private String basgroupdef93;
	  private String basgroupdef94;
	  private String basgroupdef95;
	  private String basgroupdef96;
	  private String basgroupdef97;
	  private String basgroupdef98;
	  private String basgroupdef99;
	  private UFDate birthdate;
	  private String bloodtype;
	  private String bp;
	  private String characterrpr;
	  private String city;
	  private String computerlevel;
	  private String country;
	  private String dataoperator;
	  private Integer dr;
	  private String email;
	  private String employform;
	  private String froeignlang;
	  private String frolanlevel;
	  private String functiontype;
	  private String health;
	  private String homephone;
	  private String id;
	  private UFBoolean indocflag;
	  private String interest;
	  private UFDate joinpolitydate;
	  private UFDate joinsysdate;
	  private UFDate joinworkdate;
	  private String marital;
	  private UFDate marriagedate;
	  private String mobile;
	  private String nationality;
	  private String nativeplace;
	  private String officephone;
	  private String penelauth;
	  private String permanreside;
	  private byte[] photo;
	  private String pk_corp;
	  private String pk_psnbasdoc;
	  private String polity;
	  private String postalcode;
	  private String province;
	  private String psnname;
	  private UFBoolean reg_accept_research_flag;
	  private Integer reg_background_findings;
	  private UFDate reg_contract_end_date;
	  private UFBoolean reg_contract_unchain_flag;
	  private Integer reg_data_source;
	  private UFDate reg_date;
	  private UFDate reg_due_date;
	  private Integer reg_examination_results;
	  private UFDouble reg_expect_wage;
	  private String reg_file_place;
	  private UFBoolean reg_file_transfer_flag;
	  private UFDouble reg_old_pay_before_tax;
	  private String reg_personal_fortes;
	  private String reg_pk_activity;
	  private String reg_pk_channel;
	  private String reg_recommendation_views;
	  private String reg_recommender;
	  private String reg_recommender_contact_form;
	  private UFBoolean reg_seal_flag;
	  private String reg_self_evaluation;
	  private UFBoolean reg_share_flag;
	  private Integer reg_status;
	  private UFDate reg_status_date;
	  private UFDate reg_talent_bank_date;
	  private UFBoolean reg_talent_bank_flag;
	  private String reg_talent_flow;
	  private String reg_talent_note;
	  private String reg_talent_type;
	  private Integer reg_type;
	  private String reg_work_addr;
	  private String reg_work_type;
	  private String reg_working_unit;
	  private String remark;
	  private String sex;
	  private String skilllevel;
	  private String ssnum;
	  private String titletechpost;
	  private UFDateTime ts;
	  private String usedname;
	  private String strDefFieldCodePrefix;

	  public PsnbasdocVO()
	  {
	    this.strDefFieldCodePrefix = "basgroupdef";
	  }

	  public PsnbasdocVO(String newPk_psnbasdoc)
	  {
	    this.pk_psnbasdoc = newPk_psnbasdoc;
	  }

	  public String getAddr()
	  {
	    return this.addr;
	  }

	  public Integer getApproveflag()
	  {
	    return this.approveflag;
	  }

	  public String getBasgroupdef1()
	  {
	    return this.basgroupdef1;
	  }

	  public String getBasgroupdef10()
	  {
	    return this.basgroupdef10;
	  }

	  public String getBasgroupdef11()
	  {
	    return this.basgroupdef11;
	  }

	  public String getBasgroupdef12()
	  {
	    return this.basgroupdef12;
	  }

	  public String getBasgroupdef13()
	  {
	    return this.basgroupdef13;
	  }

	  public String getBasgroupdef14()
	  {
	    return this.basgroupdef14;
	  }

	  public String getBasgroupdef15()
	  {
	    return this.basgroupdef15;
	  }

	  public String getBasgroupdef16()
	  {
	    return this.basgroupdef16;
	  }

	  public String getBasgroupdef17()
	  {
	    return this.basgroupdef17;
	  }

	  public String getBasgroupdef18()
	  {
	    return this.basgroupdef18;
	  }

	  public String getBasgroupdef19()
	  {
	    return this.basgroupdef19;
	  }

	  public String getBasgroupdef2()
	  {
	    return this.basgroupdef2;
	  }

	  public String getBasgroupdef20()
	  {
	    return this.basgroupdef20;
	  }

	  public String getBasgroupdef21()
	  {
	    return this.basgroupdef21;
	  }

	  public String getBasgroupdef22()
	  {
	    return this.basgroupdef22;
	  }

	  public String getBasgroupdef23()
	  {
	    return this.basgroupdef23;
	  }

	  public String getBasgroupdef24()
	  {
	    return this.basgroupdef24;
	  }

	  public String getBasgroupdef25()
	  {
	    return this.basgroupdef25;
	  }

	  public String getBasgroupdef26()
	  {
	    return this.basgroupdef26;
	  }

	  public String getBasgroupdef27()
	  {
	    return this.basgroupdef27;
	  }

	  public String getBasgroupdef28()
	  {
	    return this.basgroupdef28;
	  }

	  public String getBasgroupdef29()
	  {
	    return this.basgroupdef29;
	  }

	  public String getBasgroupdef3()
	  {
	    return this.basgroupdef3;
	  }

	  public String getBasgroupdef30()
	  {
	    return this.basgroupdef30;
	  }

	  public String getBasgroupdef31()
	  {
	    return this.basgroupdef31;
	  }

	  public String getBasgroupdef32()
	  {
	    return this.basgroupdef32;
	  }

	  public String getBasgroupdef33()
	  {
	    return this.basgroupdef33;
	  }

	  public String getBasgroupdef34()
	  {
	    return this.basgroupdef34;
	  }

	  public String getBasgroupdef35()
	  {
	    return this.basgroupdef35;
	  }

	  public String getBasgroupdef36()
	  {
	    return this.basgroupdef36;
	  }

	  public String getBasgroupdef37()
	  {
	    return this.basgroupdef37;
	  }

	  public String getBasgroupdef38()
	  {
	    return this.basgroupdef38;
	  }

	  public String getBasgroupdef39()
	  {
	    return this.basgroupdef39;
	  }

	  public String getBasgroupdef4()
	  {
	    return this.basgroupdef4;
	  }

	  public String getBasgroupdef40()
	  {
	    return this.basgroupdef40;
	  }

	  public String getBasgroupdef41()
	  {
	    return this.basgroupdef41;
	  }

	  public String getBasgroupdef42()
	  {
	    return this.basgroupdef42;
	  }

	  public String getBasgroupdef43()
	  {
	    return this.basgroupdef43;
	  }

	  public String getBasgroupdef44()
	  {
	    return this.basgroupdef44;
	  }

	  public String getBasgroupdef45()
	  {
	    return this.basgroupdef45;
	  }

	  public String getBasgroupdef46()
	  {
	    return this.basgroupdef46;
	  }

	  public String getBasgroupdef47()
	  {
	    return this.basgroupdef47;
	  }

	  public String getBasgroupdef48()
	  {
	    return this.basgroupdef48;
	  }

	  public String getBasgroupdef49()
	  {
	    return this.basgroupdef49;
	  }

	  public String getBasgroupdef5()
	  {
	    return this.basgroupdef5;
	  }

	  public String getBasgroupdef50()
	  {
	    return this.basgroupdef50;
	  }

	  public String getBasgroupdef6()
	  {
	    return this.basgroupdef6;
	  }

	  public String getBasgroupdef7()
	  {
	    return this.basgroupdef7;
	  }

	  public String getBasgroupdef8()
	  {
	    return this.basgroupdef8;
	  }

	  public String getBasgroupdef9()
	  {
	    return this.basgroupdef9;
	  }

	  public UFDate getBirthdate()
	  {
	    return this.birthdate;
	  }

	  public String getBloodtype()
	  {
	    return this.bloodtype;
	  }

	  public String getBp()
	  {
	    return this.bp;
	  }

	  public String getCharacterrpr()
	  {
	    return this.characterrpr;
	  }

	  public String getCity()
	  {
	    return this.city;
	  }

	  public String getComputerlevel()
	  {
	    return this.computerlevel;
	  }

	  public String getCountry()
	  {
	    return this.country;
	  }

	  public String getDataoperator()
	  {
	    return this.dataoperator;
	  }

	  public Integer getDr()
	  {
	    return this.dr;
	  }

	  public String getEmail()
	  {
	    return this.email;
	  }

	  public String getEmployform()
	  {
	    return this.employform;
	  }

	  public String getEntityName()
	  {
	    return "rm_psnbasdoc";
	  }

	  public String getFroeignlang()
	  {
	    return this.froeignlang;
	  }

	  public String getFrolanlevel()
	  {
	    return this.frolanlevel;
	  }

	  public String getFunctiontype()
	  {
	    return this.functiontype;
	  }

	  public String getHealth()
	  {
	    return this.health;
	  }

	  public String getHomephone()
	  {
	    return this.homephone;
	  }

	  public String getId()
	  {
	    return this.id;
	  }

	  public UFBoolean getIndocflag()
	  {
	    return this.indocflag;
	  }

	  public String getInterest()
	  {
	    return this.interest;
	  }

	  public UFDate getJoinpolitydate()
	  {
	    return this.joinpolitydate;
	  }

	  public UFDate getJoinsysdate()
	  {
	    return this.joinsysdate;
	  }

	  public UFDate getJoinworkdate()
	  {
	    return this.joinworkdate;
	  }

	  public String getMarital()
	  {
	    return this.marital;
	  }

	  public UFDate getMarriagedate()
	  {
	    return this.marriagedate;
	  }

	  public String getMobile()
	  {
	    return this.mobile;
	  }

	  public String getNationality()
	  {
	    return this.nationality;
	  }

	  public String getNativeplace()
	  {
	    return this.nativeplace;
	  }

	  public String getOfficephone()
	  {
	    return this.officephone;
	  }

	  public String getParentPKFieldName()
	  {
	    return null;
	  }

	  public String getPenelauth()
	  {
	    return this.penelauth;
	  }

	  public String getPermanreside()
	  {
	    return this.permanreside;
	  }

	  public byte[] getPhoto()
	  {
	    return this.photo;
	  }

	  public String getPk_corp()
	  {
	    return this.pk_corp;
	  }

	  public String getPk_psnbasdoc()
	  {
	    return this.pk_psnbasdoc;
	  }

	  public String getPKFieldName()
	  {
	    return "pk_psnbasdoc";
	  }

	  public String getPolity()
	  {
	    return this.polity;
	  }

	  public String getPostalcode()
	  {
	    return this.postalcode;
	  }

	  public String getPrimaryKey()
	  {
	    return this.pk_psnbasdoc;
	  }

	  public String getProvince()
	  {
	    return this.province;
	  }

	  public String getPsnname()
	  {
	    return this.psnname;
	  }

	  public UFBoolean getReg_accept_research_flag()
	  {
	    return this.reg_accept_research_flag;
	  }

	  public Integer getReg_background_findings()
	  {
	    return this.reg_background_findings;
	  }

	  public UFDate getReg_contract_end_date()
	  {
	    return this.reg_contract_end_date;
	  }

	  public UFBoolean getReg_contract_unchain_flag()
	  {
	    return this.reg_contract_unchain_flag;
	  }

	  public Integer getReg_data_source()
	  {
	    return this.reg_data_source;
	  }

	  public UFDate getReg_date()
	  {
	    return this.reg_date;
	  }

	  public UFDate getReg_due_date()
	  {
	    return this.reg_due_date;
	  }

	  public Integer getReg_examination_results()
	  {
	    return this.reg_examination_results;
	  }

	  public UFDouble getReg_expect_wage()
	  {
	    return this.reg_expect_wage;
	  }

	  public String getReg_file_place()
	  {
	    return this.reg_file_place;
	  }

	  public UFBoolean getReg_file_transfer_flag()
	  {
	    return this.reg_file_transfer_flag;
	  }

	  public UFDouble getReg_old_pay_before_tax()
	  {
	    return this.reg_old_pay_before_tax;
	  }

	  public String getReg_personal_fortes()
	  {
	    return this.reg_personal_fortes;
	  }

	  public String getReg_pk_activity()
	  {
	    return this.reg_pk_activity;
	  }

	  public String getReg_pk_channel()
	  {
	    return this.reg_pk_channel;
	  }

	  public String getReg_recommendation_views()
	  {
	    return this.reg_recommendation_views;
	  }

	  public String getReg_recommender()
	  {
	    return this.reg_recommender;
	  }

	  public String getReg_recommender_contact_form()
	  {
	    return this.reg_recommender_contact_form;
	  }

	  public UFBoolean getReg_seal_flag()
	  {
	    return this.reg_seal_flag;
	  }

	  public String getReg_self_evaluation()
	  {
	    return this.reg_self_evaluation;
	  }

	  public UFBoolean getReg_share_flag()
	  {
	    return this.reg_share_flag;
	  }

	  public Integer getReg_status()
	  {
	    return this.reg_status;
	  }

	  public UFDate getReg_status_date()
	  {
	    return this.reg_status_date;
	  }

	  public UFDate getReg_talent_bank_date()
	  {
	    return this.reg_talent_bank_date;
	  }

	  public UFBoolean getReg_talent_bank_flag()
	  {
	    return this.reg_talent_bank_flag;
	  }

	  public String getReg_talent_flow()
	  {
	    return this.reg_talent_flow;
	  }

	  public String getReg_talent_note()
	  {
	    return this.reg_talent_note;
	  }

	  public String getReg_talent_type()
	  {
	    return this.reg_talent_type;
	  }

	  public Integer getReg_type()
	  {
	    return this.reg_type;
	  }

	  public String getReg_work_addr()
	  {
	    return this.reg_work_addr;
	  }

	  public String getReg_work_type()
	  {
	    return this.reg_work_type;
	  }

	  public String getReg_working_unit()
	  {
	    return this.reg_working_unit;
	  }

	  public String getRemark()
	  {
	    return this.remark;
	  }

	  public String getSex()
	  {
	    return this.sex;
	  }

	  public String getSkilllevel()
	  {
	    return this.skilllevel;
	  }

	  public String getSsnum()
	  {
	    return this.ssnum;
	  }

	  public String getTableName()
	  {
	    return "bd_psnbasdoc";
	  }

	  public String getTitletechpost()
	  {
	    return this.titletechpost;
	  }

	  public UFDateTime getTs()
	  {
	    return this.ts;
	  }

	  public String getUsedname()
	  {
	    return this.usedname;
	  }

	  public void setAddr(String newAddr)
	  {
	    this.addr = newAddr;
	  }

	  public void setApproveflag(Integer newApproveflag)
	  {
	    this.approveflag = newApproveflag;
	  }

	  public void setBasgroupdef1(String newBasgroupdef1)
	  {
	    this.basgroupdef1 = newBasgroupdef1;
	  }

	  public void setBasgroupdef10(String newBasgroupdef10)
	  {
	    this.basgroupdef10 = newBasgroupdef10;
	  }

	  public void setBasgroupdef11(String newBasgroupdef11)
	  {
	    this.basgroupdef11 = newBasgroupdef11;
	  }

	  public void setBasgroupdef12(String newBasgroupdef12)
	  {
	    this.basgroupdef12 = newBasgroupdef12;
	  }

	  public void setBasgroupdef13(String newBasgroupdef13)
	  {
	    this.basgroupdef13 = newBasgroupdef13;
	  }

	  public void setBasgroupdef14(String newBasgroupdef14)
	  {
	    this.basgroupdef14 = newBasgroupdef14;
	  }

	  public void setBasgroupdef15(String newBasgroupdef15)
	  {
	    this.basgroupdef15 = newBasgroupdef15;
	  }

	  public void setBasgroupdef16(String newBasgroupdef16)
	  {
	    this.basgroupdef16 = newBasgroupdef16;
	  }

	  public void setBasgroupdef17(String newBasgroupdef17)
	  {
	    this.basgroupdef17 = newBasgroupdef17;
	  }

	  public void setBasgroupdef18(String newBasgroupdef18)
	  {
	    this.basgroupdef18 = newBasgroupdef18;
	  }

	  public void setBasgroupdef19(String newBasgroupdef19)
	  {
	    this.basgroupdef19 = newBasgroupdef19;
	  }

	  public void setBasgroupdef2(String newBasgroupdef2)
	  {
	    this.basgroupdef2 = newBasgroupdef2;
	  }

	  public void setBasgroupdef20(String newBasgroupdef20)
	  {
	    this.basgroupdef20 = newBasgroupdef20;
	  }

	  public void setBasgroupdef21(String basgroupdef21)
	  {
	    this.basgroupdef21 = basgroupdef21;
	  }

	  public void setBasgroupdef22(String basgroupdef22)
	  {
	    this.basgroupdef22 = basgroupdef22;
	  }

	  public void setBasgroupdef23(String basgroupdef23)
	  {
	    this.basgroupdef23 = basgroupdef23;
	  }

	  public void setBasgroupdef24(String basgroupdef24)
	  {
	    this.basgroupdef24 = basgroupdef24;
	  }

	  public void setBasgroupdef25(String basgroupdef25)
	  {
	    this.basgroupdef25 = basgroupdef25;
	  }

	  public void setBasgroupdef26(String basgroupdef26)
	  {
	    this.basgroupdef26 = basgroupdef26;
	  }

	  public void setBasgroupdef27(String basgroupdef27)
	  {
	    this.basgroupdef27 = basgroupdef27;
	  }

	  public void setBasgroupdef28(String basgroupdef28)
	  {
	    this.basgroupdef28 = basgroupdef28;
	  }

	  public void setBasgroupdef29(String basgroupdef29)
	  {
	    this.basgroupdef29 = basgroupdef29;
	  }

	  public void setBasgroupdef3(String newBasgroupdef3)
	  {
	    this.basgroupdef3 = newBasgroupdef3;
	  }

	  public void setBasgroupdef30(String basgroupdef30)
	  {
	    this.basgroupdef30 = basgroupdef30;
	  }

	  public void setBasgroupdef31(String basgroupdef31)
	  {
	    this.basgroupdef31 = basgroupdef31;
	  }

	  public void setBasgroupdef32(String basgroupdef32)
	  {
	    this.basgroupdef32 = basgroupdef32;
	  }

	  public void setBasgroupdef33(String basgroupdef33)
	  {
	    this.basgroupdef33 = basgroupdef33;
	  }

	  public void setBasgroupdef34(String basgroupdef34)
	  {
	    this.basgroupdef34 = basgroupdef34;
	  }

	  public void setBasgroupdef35(String basgroupdef35)
	  {
	    this.basgroupdef35 = basgroupdef35;
	  }

	  public void setBasgroupdef36(String basgroupdef36)
	  {
	    this.basgroupdef36 = basgroupdef36;
	  }

	  public void setBasgroupdef37(String basgroupdef37)
	  {
	    this.basgroupdef37 = basgroupdef37;
	  }

	  public void setBasgroupdef38(String basgroupdef38)
	  {
	    this.basgroupdef38 = basgroupdef38;
	  }

	  public void setBasgroupdef39(String basgroupdef39)
	  {
	    this.basgroupdef39 = basgroupdef39;
	  }

	  public void setBasgroupdef4(String newBasgroupdef4)
	  {
	    this.basgroupdef4 = newBasgroupdef4;
	  }

	  public void setBasgroupdef40(String basgroupdef40)
	  {
	    this.basgroupdef40 = basgroupdef40;
	  }

	  public void setBasgroupdef41(String basgroupdef41)
	  {
	    this.basgroupdef41 = basgroupdef41;
	  }

	  public void setBasgroupdef42(String basgroupdef42)
	  {
	    this.basgroupdef42 = basgroupdef42;
	  }

	  public void setBasgroupdef43(String basgroupdef43)
	  {
	    this.basgroupdef43 = basgroupdef43;
	  }

	  public void setBasgroupdef44(String basgroupdef44)
	  {
	    this.basgroupdef44 = basgroupdef44;
	  }

	  public void setBasgroupdef45(String basgroupdef45)
	  {
	    this.basgroupdef45 = basgroupdef45;
	  }

	  public void setBasgroupdef46(String basgroupdef46)
	  {
	    this.basgroupdef46 = basgroupdef46;
	  }

	  public void setBasgroupdef47(String basgroupdef47)
	  {
	    this.basgroupdef47 = basgroupdef47;
	  }

	  public void setBasgroupdef48(String basgroupdef48)
	  {
	    this.basgroupdef48 = basgroupdef48;
	  }

	  public void setBasgroupdef49(String basgroupdef49)
	  {
	    this.basgroupdef49 = basgroupdef49;
	  }

	  public void setBasgroupdef5(String newBasgroupdef5)
	  {
	    this.basgroupdef5 = newBasgroupdef5;
	  }

	  public void setBasgroupdef50(String basgroupdef50)
	  {
	    this.basgroupdef50 = basgroupdef50;
	  }

	  public void setBasgroupdef6(String newBasgroupdef6)
	  {
	    this.basgroupdef6 = newBasgroupdef6;
	  }

	  public void setBasgroupdef7(String newBasgroupdef7)
	  {
	    this.basgroupdef7 = newBasgroupdef7;
	  }

	  public void setBasgroupdef8(String newBasgroupdef8)
	  {
	    this.basgroupdef8 = newBasgroupdef8;
	  }

	  public void setBasgroupdef9(String newBasgroupdef9)
	  {
	    this.basgroupdef9 = newBasgroupdef9;
	  }

	  public void setBirthdate(UFDate newBirthdate)
	  {
	    this.birthdate = newBirthdate;
	  }

	  public void setBloodtype(String newBloodtype)
	  {
	    this.bloodtype = newBloodtype;
	  }

	  public void setBp(String newBp)
	  {
	    this.bp = newBp;
	  }

	  public void setCharacterrpr(String newCharacterrpr)
	  {
	    this.characterrpr = newCharacterrpr;
	  }

	  public void setCity(String newCity)
	  {
	    this.city = newCity;
	  }

	  public void setComputerlevel(String newComputerlevel)
	  {
	    this.computerlevel = newComputerlevel;
	  }

	  public void setCountry(String newCountry)
	  {
	    this.country = newCountry;
	  }

	  public void setDataoperator(String newDataoperator)
	  {
	    this.dataoperator = newDataoperator;
	  }

	  public void setDr(Integer newDr)
	  {
	    this.dr = newDr;
	  }

	  public void setEmail(String newEmail)
	  {
	    this.email = newEmail;
	  }

	  public void setEmployform(String newEmployform)
	  {
	    this.employform = newEmployform;
	  }

	  public void setFroeignlang(String newFroeignlang)
	  {
	    this.froeignlang = newFroeignlang;
	  }

	  public void setFrolanlevel(String newFrolanlevel)
	  {
	    this.frolanlevel = newFrolanlevel;
	  }

	  public void setFunctiontype(String newFunctiontype)
	  {
	    this.functiontype = newFunctiontype;
	  }

	  public void setHealth(String newHealth)
	  {
	    this.health = newHealth;
	  }

	  public void setHomephone(String newHomephone)
	  {
	    this.homephone = newHomephone;
	  }

	  public void setId(String newId)
	  {
	    this.id = newId;
	  }

	  public void setIndocflag(UFBoolean newIndocflag)
	  {
	    this.indocflag = newIndocflag;
	  }

	  public void setInterest(String newInterest)
	  {
	    this.interest = newInterest;
	  }

	  public void setJoinpolitydate(UFDate newJoinpolitydate)
	  {
	    this.joinpolitydate = newJoinpolitydate;
	  }

	  public void setJoinsysdate(UFDate newJoinsysdate)
	  {
	    this.joinsysdate = newJoinsysdate;
	  }

	  public void setJoinworkdate(UFDate newJoinworkdate)
	  {
	    this.joinworkdate = newJoinworkdate;
	  }

	  public void setMarital(String newMarital)
	  {
	    this.marital = newMarital;
	  }

	  public void setMarriagedate(UFDate newMarriagedate)
	  {
	    this.marriagedate = newMarriagedate;
	  }

	  public void setMobile(String newMobile)
	  {
	    this.mobile = newMobile;
	  }

	  public void setNationality(String newNationality)
	  {
	    this.nationality = newNationality;
	  }

	  public void setNativeplace(String newNativeplace)
	  {
	    this.nativeplace = newNativeplace;
	  }

	  public void setOfficephone(String newOfficephone)
	  {
	    this.officephone = newOfficephone;
	  }

	  public void setPenelauth(String newPenelauth)
	  {
	    this.penelauth = newPenelauth;
	  }

	  public void setPermanreside(String newPermanreside)
	  {
	    this.permanreside = newPermanreside;
	  }

	  public void setPhoto(byte[] newPhoto)
	  {
	    this.photo = newPhoto;
	  }

	  public void setPk_corp(String newPk_corp)
	  {
	    this.pk_corp = newPk_corp;
	  }

	  public void setPk_psnbasdoc(String newPk_psnbasdoc)
	  {
	    this.pk_psnbasdoc = newPk_psnbasdoc;
	  }

	  public void setPolity(String newPolity)
	  {
	    this.polity = newPolity;
	  }

	  public void setPostalcode(String newPostalcode)
	  {
	    this.postalcode = newPostalcode;
	  }

	  public void setPrimaryKey(String newPk_psnbasdoc)
	  {
	    this.pk_psnbasdoc = newPk_psnbasdoc;
	  }

	  public void setProvince(String newProvince)
	  {
	    this.province = newProvince;
	  }

	  public void setPsnname(String newPsnname)
	  {
	    this.psnname = newPsnname;
	  }

	  public void setReg_accept_research_flag(UFBoolean newReg_accept_research_flag)
	  {
	    this.reg_accept_research_flag = newReg_accept_research_flag;
	  }

	  public void setReg_background_findings(Integer newReg_background_findings)
	  {
	    this.reg_background_findings = newReg_background_findings;
	  }

	  public void setReg_contract_end_date(UFDate newReg_contract_end_date)
	  {
	    this.reg_contract_end_date = newReg_contract_end_date;
	  }

	  public void setReg_contract_unchain_flag(UFBoolean newReg_contract_unchain_flag)
	  {
	    this.reg_contract_unchain_flag = newReg_contract_unchain_flag;
	  }

	  public void setReg_data_source(Integer newReg_data_source)
	  {
	    this.reg_data_source = newReg_data_source;
	  }

	  public void setReg_date(UFDate newReg_date)
	  {
	    this.reg_date = newReg_date;
	  }

	  public void setReg_due_date(UFDate newReg_due_date)
	  {
	    this.reg_due_date = newReg_due_date;
	  }

	  public void setReg_examination_results(Integer newReg_examination_results)
	  {
	    this.reg_examination_results = newReg_examination_results;
	  }

	  public void setReg_expect_wage(UFDouble newReg_expect_wage)
	  {
	    this.reg_expect_wage = newReg_expect_wage;
	  }

	  public void setReg_file_place(String newReg_file_place)
	  {
	    this.reg_file_place = newReg_file_place;
	  }

	  public void setReg_file_transfer_flag(UFBoolean newReg_file_transfer_flag)
	  {
	    this.reg_file_transfer_flag = newReg_file_transfer_flag;
	  }

	  public void setReg_old_pay_before_tax(UFDouble newReg_old_pay_before_tax)
	  {
	    this.reg_old_pay_before_tax = newReg_old_pay_before_tax;
	  }

	  public void setReg_personal_fortes(String newReg_personal_fortes)
	  {
	    this.reg_personal_fortes = newReg_personal_fortes;
	  }

	  public void setReg_pk_activity(String newReg_pk_activity)
	  {
	    this.reg_pk_activity = newReg_pk_activity;
	  }

	  public void setReg_pk_channel(String newReg_pk_channel)
	  {
	    this.reg_pk_channel = newReg_pk_channel;
	  }

	  public void setReg_recommendation_views(String newReg_recommendation_views)
	  {
	    this.reg_recommendation_views = newReg_recommendation_views;
	  }

	  public void setReg_recommender(String newReg_recommender)
	  {
	    this.reg_recommender = newReg_recommender;
	  }

	  public void setReg_recommender_contact_form(String newReg_recommender_contact_form)
	  {
	    this.reg_recommender_contact_form = newReg_recommender_contact_form;
	  }

	  public void setReg_seal_flag(UFBoolean reg_seal_flag)
	  {
	    this.reg_seal_flag = reg_seal_flag;
	  }

	  public void setReg_self_evaluation(String newReg_self_evaluation)
	  {
	    this.reg_self_evaluation = newReg_self_evaluation;
	  }

	  public void setReg_share_flag(UFBoolean newReg_share_flag)
	  {
	    this.reg_share_flag = newReg_share_flag;
	  }

	  public void setReg_status(Integer newReg_status)
	  {
	    this.reg_status = newReg_status;
	  }

	  public void setReg_status_date(UFDate newReg_status_date)
	  {
	    this.reg_status_date = newReg_status_date;
	  }

	  public void setReg_talent_bank_date(UFDate reg_talent_bank_date)
	  {
	    this.reg_talent_bank_date = reg_talent_bank_date;
	  }

	  public void setReg_talent_bank_flag(UFBoolean newReg_talent_bank)
	  {
	    this.reg_talent_bank_flag = newReg_talent_bank;
	  }

	  public void setReg_talent_flow(String reg_talent_flow)
	  {
	    this.reg_talent_flow = reg_talent_flow;
	  }

	  public void setReg_talent_note(String reg_talent_note)
	  {
	    this.reg_talent_note = reg_talent_note;
	  }

	  public void setReg_talent_type(String reg_talent_type)
	  {
	    this.reg_talent_type = reg_talent_type;
	  }

	  public void setReg_type(Integer newReg_type)
	  {
	    this.reg_type = newReg_type;
	  }

	  public void setReg_work_addr(String newReg_work_addr)
	  {
	    this.reg_work_addr = newReg_work_addr;
	  }

	  public void setReg_work_type(String newReg_work_type)
	  {
	    this.reg_work_type = newReg_work_type;
	  }

	  public void setReg_working_unit(String newReg_working_unit)
	  {
	    this.reg_working_unit = newReg_working_unit;
	  }

	  public void setRemark(String newRemark)
	  {
	    this.remark = newRemark;
	  }

	  public void setSex(String newSex)
	  {
	    this.sex = newSex;
	  }

	  public void setSkilllevel(String newSkilllevel)
	  {
	    this.skilllevel = newSkilllevel;
	  }

	  public void setSsnum(String newSsnum)
	  {
	    this.ssnum = newSsnum;
	  }

	  public void setTitletechpost(String newTitletechpost)
	  {
	    this.titletechpost = newTitletechpost;
	  }

	  public void setTs(UFDateTime newTs)
	  {
	    this.ts = newTs;
	  }

	  public void setUsedname(String newUsedname)
	  {
	    this.usedname = newUsedname;
	  }

	public String getBasgroupdef100() {
		return basgroupdef100;
	}

	public void setBasgroupdef100(String basgroupdef100) {
		this.basgroupdef100 = basgroupdef100;
	}

	public String getBasgroupdef101() {
		return basgroupdef101;
	}

	public void setBasgroupdef101(String basgroupdef101) {
		this.basgroupdef101 = basgroupdef101;
	}

	public String getBasgroupdef102() {
		return basgroupdef102;
	}

	public void setBasgroupdef102(String basgroupdef102) {
		this.basgroupdef102 = basgroupdef102;
	}

	public String getBasgroupdef103() {
		return basgroupdef103;
	}

	public void setBasgroupdef103(String basgroupdef103) {
		this.basgroupdef103 = basgroupdef103;
	}

	public String getBasgroupdef104() {
		return basgroupdef104;
	}

	public void setBasgroupdef104(String basgroupdef104) {
		this.basgroupdef104 = basgroupdef104;
	}

	public String getBasgroupdef105() {
		return basgroupdef105;
	}

	public void setBasgroupdef105(String basgroupdef105) {
		this.basgroupdef105 = basgroupdef105;
	}

	public String getBasgroupdef106() {
		return basgroupdef106;
	}

	public void setBasgroupdef106(String basgroupdef106) {
		this.basgroupdef106 = basgroupdef106;
	}

	public String getBasgroupdef107() {
		return basgroupdef107;
	}

	public void setBasgroupdef107(String basgroupdef107) {
		this.basgroupdef107 = basgroupdef107;
	}

	public String getBasgroupdef108() {
		return basgroupdef108;
	}

	public void setBasgroupdef108(String basgroupdef108) {
		this.basgroupdef108 = basgroupdef108;
	}

	public String getBasgroupdef109() {
		return basgroupdef109;
	}

	public void setBasgroupdef109(String basgroupdef109) {
		this.basgroupdef109 = basgroupdef109;
	}

	public String getBasgroupdef110() {
		return basgroupdef110;
	}

	public void setBasgroupdef110(String basgroupdef110) {
		this.basgroupdef110 = basgroupdef110;
	}

	public String getBasgroupdef111() {
		return basgroupdef111;
	}

	public void setBasgroupdef111(String basgroupdef111) {
		this.basgroupdef111 = basgroupdef111;
	}

	public String getBasgroupdef112() {
		return basgroupdef112;
	}

	public void setBasgroupdef112(String basgroupdef112) {
		this.basgroupdef112 = basgroupdef112;
	}

	public String getBasgroupdef113() {
		return basgroupdef113;
	}

	public void setBasgroupdef113(String basgroupdef113) {
		this.basgroupdef113 = basgroupdef113;
	}

	public String getBasgroupdef114() {
		return basgroupdef114;
	}

	public void setBasgroupdef114(String basgroupdef114) {
		this.basgroupdef114 = basgroupdef114;
	}

	public String getBasgroupdef115() {
		return basgroupdef115;
	}

	public void setBasgroupdef115(String basgroupdef115) {
		this.basgroupdef115 = basgroupdef115;
	}

	public String getBasgroupdef116() {
		return basgroupdef116;
	}

	public void setBasgroupdef116(String basgroupdef116) {
		this.basgroupdef116 = basgroupdef116;
	}

	public String getBasgroupdef117() {
		return basgroupdef117;
	}

	public void setBasgroupdef117(String basgroupdef117) {
		this.basgroupdef117 = basgroupdef117;
	}

	public String getBasgroupdef118() {
		return basgroupdef118;
	}

	public void setBasgroupdef118(String basgroupdef118) {
		this.basgroupdef118 = basgroupdef118;
	}

	public String getBasgroupdef119() {
		return basgroupdef119;
	}

	public void setBasgroupdef119(String basgroupdef119) {
		this.basgroupdef119 = basgroupdef119;
	}

	public String getBasgroupdef120() {
		return basgroupdef120;
	}

	public void setBasgroupdef120(String basgroupdef120) {
		this.basgroupdef120 = basgroupdef120;
	}

	public String getBasgroupdef121() {
		return basgroupdef121;
	}

	public void setBasgroupdef121(String basgroupdef121) {
		this.basgroupdef121 = basgroupdef121;
	}

	public String getBasgroupdef122() {
		return basgroupdef122;
	}

	public void setBasgroupdef122(String basgroupdef122) {
		this.basgroupdef122 = basgroupdef122;
	}

	public String getBasgroupdef123() {
		return basgroupdef123;
	}

	public void setBasgroupdef123(String basgroupdef123) {
		this.basgroupdef123 = basgroupdef123;
	}

	public String getBasgroupdef124() {
		return basgroupdef124;
	}

	public void setBasgroupdef124(String basgroupdef124) {
		this.basgroupdef124 = basgroupdef124;
	}

	public String getBasgroupdef125() {
		return basgroupdef125;
	}

	public void setBasgroupdef125(String basgroupdef125) {
		this.basgroupdef125 = basgroupdef125;
	}

	public String getBasgroupdef126() {
		return basgroupdef126;
	}

	public void setBasgroupdef126(String basgroupdef126) {
		this.basgroupdef126 = basgroupdef126;
	}

	public String getBasgroupdef127() {
		return basgroupdef127;
	}

	public void setBasgroupdef127(String basgroupdef127) {
		this.basgroupdef127 = basgroupdef127;
	}

	public String getBasgroupdef128() {
		return basgroupdef128;
	}

	public void setBasgroupdef128(String basgroupdef128) {
		this.basgroupdef128 = basgroupdef128;
	}

	public String getBasgroupdef129() {
		return basgroupdef129;
	}

	public void setBasgroupdef129(String basgroupdef129) {
		this.basgroupdef129 = basgroupdef129;
	}

	public String getBasgroupdef130() {
		return basgroupdef130;
	}

	public void setBasgroupdef130(String basgroupdef130) {
		this.basgroupdef130 = basgroupdef130;
	}

	public String getBasgroupdef51() {
		return basgroupdef51;
	}

	public void setBasgroupdef51(String basgroupdef51) {
		this.basgroupdef51 = basgroupdef51;
	}

	public String getBasgroupdef52() {
		return basgroupdef52;
	}

	public void setBasgroupdef52(String basgroupdef52) {
		this.basgroupdef52 = basgroupdef52;
	}

	public String getBasgroupdef53() {
		return basgroupdef53;
	}

	public void setBasgroupdef53(String basgroupdef53) {
		this.basgroupdef53 = basgroupdef53;
	}

	public String getBasgroupdef54() {
		return basgroupdef54;
	}

	public void setBasgroupdef54(String basgroupdef54) {
		this.basgroupdef54 = basgroupdef54;
	}

	public String getBasgroupdef55() {
		return basgroupdef55;
	}

	public void setBasgroupdef55(String basgroupdef55) {
		this.basgroupdef55 = basgroupdef55;
	}

	public String getBasgroupdef56() {
		return basgroupdef56;
	}

	public void setBasgroupdef56(String basgroupdef56) {
		this.basgroupdef56 = basgroupdef56;
	}

	public String getBasgroupdef57() {
		return basgroupdef57;
	}

	public void setBasgroupdef57(String basgroupdef57) {
		this.basgroupdef57 = basgroupdef57;
	}

	public String getBasgroupdef58() {
		return basgroupdef58;
	}

	public void setBasgroupdef58(String basgroupdef58) {
		this.basgroupdef58 = basgroupdef58;
	}

	public String getBasgroupdef59() {
		return basgroupdef59;
	}

	public void setBasgroupdef59(String basgroupdef59) {
		this.basgroupdef59 = basgroupdef59;
	}

	public String getBasgroupdef60() {
		return basgroupdef60;
	}

	public void setBasgroupdef60(String basgroupdef60) {
		this.basgroupdef60 = basgroupdef60;
	}

	public String getBasgroupdef61() {
		return basgroupdef61;
	}

	public void setBasgroupdef61(String basgroupdef61) {
		this.basgroupdef61 = basgroupdef61;
	}

	public String getBasgroupdef62() {
		return basgroupdef62;
	}

	public void setBasgroupdef62(String basgroupdef62) {
		this.basgroupdef62 = basgroupdef62;
	}

	public String getBasgroupdef63() {
		return basgroupdef63;
	}

	public void setBasgroupdef63(String basgroupdef63) {
		this.basgroupdef63 = basgroupdef63;
	}

	public String getBasgroupdef64() {
		return basgroupdef64;
	}

	public void setBasgroupdef64(String basgroupdef64) {
		this.basgroupdef64 = basgroupdef64;
	}

	public String getBasgroupdef65() {
		return basgroupdef65;
	}

	public void setBasgroupdef65(String basgroupdef65) {
		this.basgroupdef65 = basgroupdef65;
	}

	public String getBasgroupdef66() {
		return basgroupdef66;
	}

	public void setBasgroupdef66(String basgroupdef66) {
		this.basgroupdef66 = basgroupdef66;
	}

	public String getBasgroupdef67() {
		return basgroupdef67;
	}

	public void setBasgroupdef67(String basgroupdef67) {
		this.basgroupdef67 = basgroupdef67;
	}

	public String getBasgroupdef68() {
		return basgroupdef68;
	}

	public void setBasgroupdef68(String basgroupdef68) {
		this.basgroupdef68 = basgroupdef68;
	}

	public String getBasgroupdef69() {
		return basgroupdef69;
	}

	public void setBasgroupdef69(String basgroupdef69) {
		this.basgroupdef69 = basgroupdef69;
	}

	public String getBasgroupdef70() {
		return basgroupdef70;
	}

	public void setBasgroupdef70(String basgroupdef70) {
		this.basgroupdef70 = basgroupdef70;
	}

	public String getBasgroupdef71() {
		return basgroupdef71;
	}

	public void setBasgroupdef71(String basgroupdef71) {
		this.basgroupdef71 = basgroupdef71;
	}

	public String getBasgroupdef72() {
		return basgroupdef72;
	}

	public void setBasgroupdef72(String basgroupdef72) {
		this.basgroupdef72 = basgroupdef72;
	}

	public String getBasgroupdef73() {
		return basgroupdef73;
	}

	public void setBasgroupdef73(String basgroupdef73) {
		this.basgroupdef73 = basgroupdef73;
	}

	public String getBasgroupdef74() {
		return basgroupdef74;
	}

	public void setBasgroupdef74(String basgroupdef74) {
		this.basgroupdef74 = basgroupdef74;
	}

	public String getBasgroupdef75() {
		return basgroupdef75;
	}

	public void setBasgroupdef75(String basgroupdef75) {
		this.basgroupdef75 = basgroupdef75;
	}

	public String getBasgroupdef76() {
		return basgroupdef76;
	}

	public void setBasgroupdef76(String basgroupdef76) {
		this.basgroupdef76 = basgroupdef76;
	}

	public String getBasgroupdef77() {
		return basgroupdef77;
	}

	public void setBasgroupdef77(String basgroupdef77) {
		this.basgroupdef77 = basgroupdef77;
	}

	public String getBasgroupdef78() {
		return basgroupdef78;
	}

	public void setBasgroupdef78(String basgroupdef78) {
		this.basgroupdef78 = basgroupdef78;
	}

	public String getBasgroupdef79() {
		return basgroupdef79;
	}

	public void setBasgroupdef79(String basgroupdef79) {
		this.basgroupdef79 = basgroupdef79;
	}

	public String getBasgroupdef80() {
		return basgroupdef80;
	}

	public void setBasgroupdef80(String basgroupdef80) {
		this.basgroupdef80 = basgroupdef80;
	}

	public String getBasgroupdef81() {
		return basgroupdef81;
	}

	public void setBasgroupdef81(String basgroupdef81) {
		this.basgroupdef81 = basgroupdef81;
	}

	public String getBasgroupdef82() {
		return basgroupdef82;
	}

	public void setBasgroupdef82(String basgroupdef82) {
		this.basgroupdef82 = basgroupdef82;
	}

	public String getBasgroupdef83() {
		return basgroupdef83;
	}

	public void setBasgroupdef83(String basgroupdef83) {
		this.basgroupdef83 = basgroupdef83;
	}

	public String getBasgroupdef84() {
		return basgroupdef84;
	}

	public void setBasgroupdef84(String basgroupdef84) {
		this.basgroupdef84 = basgroupdef84;
	}

	public String getBasgroupdef85() {
		return basgroupdef85;
	}

	public void setBasgroupdef85(String basgroupdef85) {
		this.basgroupdef85 = basgroupdef85;
	}

	public String getBasgroupdef86() {
		return basgroupdef86;
	}

	public void setBasgroupdef86(String basgroupdef86) {
		this.basgroupdef86 = basgroupdef86;
	}

	public String getBasgroupdef87() {
		return basgroupdef87;
	}

	public void setBasgroupdef87(String basgroupdef87) {
		this.basgroupdef87 = basgroupdef87;
	}

	public String getBasgroupdef88() {
		return basgroupdef88;
	}

	public void setBasgroupdef88(String basgroupdef88) {
		this.basgroupdef88 = basgroupdef88;
	}

	public String getBasgroupdef89() {
		return basgroupdef89;
	}

	public void setBasgroupdef89(String basgroupdef89) {
		this.basgroupdef89 = basgroupdef89;
	}

	public String getBasgroupdef90() {
		return basgroupdef90;
	}

	public void setBasgroupdef90(String basgroupdef90) {
		this.basgroupdef90 = basgroupdef90;
	}

	public String getBasgroupdef91() {
		return basgroupdef91;
	}

	public void setBasgroupdef91(String basgroupdef91) {
		this.basgroupdef91 = basgroupdef91;
	}

	public String getBasgroupdef92() {
		return basgroupdef92;
	}

	public void setBasgroupdef92(String basgroupdef92) {
		this.basgroupdef92 = basgroupdef92;
	}

	public String getBasgroupdef93() {
		return basgroupdef93;
	}

	public void setBasgroupdef93(String basgroupdef93) {
		this.basgroupdef93 = basgroupdef93;
	}

	public String getBasgroupdef94() {
		return basgroupdef94;
	}

	public void setBasgroupdef94(String basgroupdef94) {
		this.basgroupdef94 = basgroupdef94;
	}

	public String getBasgroupdef95() {
		return basgroupdef95;
	}

	public void setBasgroupdef95(String basgroupdef95) {
		this.basgroupdef95 = basgroupdef95;
	}

	public String getBasgroupdef96() {
		return basgroupdef96;
	}

	public void setBasgroupdef96(String basgroupdef96) {
		this.basgroupdef96 = basgroupdef96;
	}

	public String getBasgroupdef97() {
		return basgroupdef97;
	}

	public void setBasgroupdef97(String basgroupdef97) {
		this.basgroupdef97 = basgroupdef97;
	}

	public String getBasgroupdef98() {
		return basgroupdef98;
	}

	public void setBasgroupdef98(String basgroupdef98) {
		this.basgroupdef98 = basgroupdef98;
	}

	public String getBasgroupdef99() {
		return basgroupdef99;
	}

	public void setBasgroupdef99(String basgroupdef99) {
		this.basgroupdef99 = basgroupdef99;
	}
	
}
