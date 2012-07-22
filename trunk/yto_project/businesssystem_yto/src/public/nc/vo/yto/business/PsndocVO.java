package nc.vo.yto.business;

import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class PsndocVO extends SuperVO {
	
	private String pk_corp;

	private String def5;

	private String groupdef19;

	private String pk_clerkclass;

	private Integer dr;

	private String groupdef18;

	private String groupdef4;

	private String pk_psntype;

	private String def14;

	private String def7;

	private UFDateTime ts;

	private String def6;

	private String dutyname;

	private String groupdef16;

	private String groupdef13;

	private String def15;

	private String sealdate;

	private String def18;

	private String groupdef9;

	private String pk_deptdoc;

	private String insource;

	private String def1;

	private UFDate regulardata;

	private String groupdef14;

	private UFDate indutydate;

	private String psnname;

	private UFBoolean regular;

	private String pk_om_job;

	private UFBoolean indocflag;

	private String def19;

	private String def20;

	private String groupdef8;

	private String clerkcode;

	private UFBoolean iscalovertime;

	private String directleader;

	private String outmethod;

	private String groupdef10;

	private String pk_psndoc;

	private String groupdef11;

	private String innercode;

	private String pk_psnbasdoc;

	private String groupdef7;

	private String def17;

	private String def11;

	private Integer psnclscope;

	private String psncode;

	private String def16;

	private String maxinnercode;

	private Integer showorder;

	private UFBoolean clerkflag;

	private String groupdef3;

	private UFBoolean isreferenced;

	private String series;

	private UFDate outdutydate;

	private UFBoolean isreturn;

	private String def10;

	private String pk_psncl;

	private String groupdef15;

	private String groupdef5;

	private String amcode;

	private String jobrank;

	private String groupdef2;

	private String def13;

	private String def8;

	private String jobseries;

	private String groupdef1;

	private UFBoolean poststat;

	private String recruitresource;

	private String groupdef20;

	private UFDate wastopdate;

	private String groupdef17;

	private String def2;

	private String pk_dutyrank;

	private String def12;

	private String groupdef12;

	private String timecardid;

	private String def4;

	private Integer tbm_prop;

	private String groupdef6;

	private String def3;

	private String def9;

	private UFDate onpostdate;

	private String pk_belongcorp;

	private String groupdef21;

	private String groupdef22;

	private String groupdef23;

	private String groupdef24;

	private String groupdef25;

	private String groupdef26;

	private String groupdef27;

	private String groupdef28;

	private String groupdef29;

	private String groupdef30;

	private String groupdef31;

	private String groupdef32;

	private String groupdef33;

	private String groupdef34;

	private String groupdef35;

	private String groupdef36;

	private String groupdef37;

	private String groupdef38;

	private String groupdef39;

	private String groupdef40;

	private String groupdef41;

	private String groupdef42;

	private String groupdef43;

	private String groupdef44;

	private String groupdef45;

	private String groupdef46;

	private String groupdef47;

	private String groupdef48;

	private String groupdef49;

	private String groupdef50;

	private String groupdef51;

	private String groupdef52;

	private String groupdef53;

	private String groupdef54;

	private String groupdef55;

	private String groupdef56;

	private String groupdef57;

	private String groupdef58;

	private String groupdef59;

	private String groupdef60;

	private String groupdef61;

	private String groupdef62;

	private String groupdef63;

	private String groupdef64;

	private String groupdef65;

	private String groupdef66;

	private String groupdef67;

	private String groupdef68;

	private String groupdef69;

	private String groupdef70;

	private String groupdef71;

	private String groupdef72;

	private String groupdef73;

	private String groupdef74;

	private String groupdef75;

	private String groupdef76;

	private String groupdef77;

	private String groupdef78;

	private String groupdef79;

	private String groupdef80;

	public static final String PK_BELONGCORP = "pk_belongcorp";

	public static final String PK_CORP = "pk_corp";

	public static final String DEF5 = "def5";

	public static final String GROUPDEF19 = "groupdef19";

	public static final String PK_CLERKCLASS = "pk_clerkclass";

	public static final String DR = "dr";

	public static final String GROUPDEF18 = "groupdef18";

	public static final String GROUPDEF4 = "groupdef4";

	public static final String PK_PSNTYPE = "pk_psntype";

	public static final String DEF14 = "def14";

	public static final String DEF7 = "def7";

	public static final String TS = "ts";

	public static final String DEF6 = "def6";

	public static final String DUTYNAME = "dutyname";

	public static final String GROUPDEF16 = "groupdef16";

	public static final String GROUPDEF13 = "groupdef13";

	public static final String DEF15 = "def15";

	public static final String SEALDATE = "sealdate";

	public static final String DEF18 = "def18";

	public static final String GROUPDEF9 = "groupdef9";

	public static final String PK_DEPTDOC = "pk_deptdoc";

	public static final String INSOURCE = "insource";

	public static final String DEF1 = "def1";

	public static final String REGULARDATA = "regulardata";

	public static final String GROUPDEF14 = "groupdef14";

	public static final String INDUTYDATE = "indutydate";

	public static final String PSNNAME = "psnname";

	public static final String REGULAR = "regular";

	public static final String PK_OM_JOB = "pk_om_job";

	public static final String INDOCFLAG = "indocflag";

	public static final String DEF19 = "def19";

	public static final String DEF20 = "def20";

	public static final String GROUPDEF8 = "groupdef8";

	public static final String CLERKCODE = "clerkcode";

	public static final String ISCALOVERTIME = "iscalovertime";

	public static final String DIRECTLEADER = "directleader";

	public static final String OUTMETHOD = "outmethod";

	public static final String GROUPDEF10 = "groupdef10";

	public static final String PK_PSNDOC = "pk_psndoc";

	public static final String GROUPDEF11 = "groupdef11";

	public static final String INNERCODE = "innercode";

	public static final String PK_PSNBASDOC = "pk_psnbasdoc";

	public static final String GROUPDEF7 = "groupdef7";

	public static final String DEF17 = "def17";

	public static final String DEF11 = "def11";

	public static final String PSNCLSCOPE = "psnclscope";

	public static final String PSNCODE = "psncode";

	public static final String DEF16 = "def16";

	public static final String MAXINNERCODE = "maxinnercode";

	public static final String SHOWORDER = "showorder";

	public static final String CLERKFLAG = "clerkflag";

	public static final String GROUPDEF3 = "groupdef3";

	public static final String ISREFERENCED = "isreferenced";

	public static final String SERIES = "series";

	public static final String OUTDUTYDATE = "outdutydate";

	public static final String ISRETURN = "isreturn";

	public static final String DEF10 = "def10";

	public static final String PK_PSNCL = "pk_psncl";

	public static final String GROUPDEF15 = "groupdef15";

	public static final String GROUPDEF5 = "groupdef5";

	public static final String AMCODE = "amcode";

	public static final String JOBRANK = "jobrank";

	public static final String GROUPDEF2 = "groupdef2";

	public static final String DEF13 = "def13";

	public static final String DEF8 = "def8";

	public static final String JOBSERIES = "jobseries";

	public static final String GROUPDEF1 = "groupdef1";

	public static final String POSTSTAT = "poststat";

	public static final String RECRUITRESOURCE = "recruitresource";

	public static final String GROUPDEF20 = "groupdef20";

	public static final String WASTOPDATE = "wastopdate";

	public static final String GROUPDEF17 = "groupdef17";

	public static final String DEF2 = "def2";

	public static final String PK_DUTYRANK = "pk_dutyrank";

	public static final String DEF12 = "def12";

	public static final String GROUPDEF12 = "groupdef12";

	public static final String TIMECARDID = "timecardid";

	public static final String DEF4 = "def4";

	public static final String TBM_PROP = "tbm_prop";

	public static final String GROUPDEF6 = "groupdef6";

	public static final String DEF3 = "def3";

	public static final String DEF9 = "def9";

	public static final String ONPOSTDATE = "onpostdate";

	public String getPk_corp() {
		return this.pk_corp;
	}

	public void setPk_corp(String newPk_corp) {
		this.pk_corp = newPk_corp;
	}

	public String getDef5() {
		return this.def5;
	}

	public void setDef5(String newDef5) {
		this.def5 = newDef5;
	}

	public String getGroupdef19() {
		return this.groupdef19;
	}

	public void setGroupdef19(String newGroupdef19) {
		this.groupdef19 = newGroupdef19;
	}

	public String getPk_clerkclass() {
		return this.pk_clerkclass;
	}

	public void setPk_clerkclass(String newPk_clerkclass) {
		this.pk_clerkclass = newPk_clerkclass;
	}

	public Integer getDr() {
		return this.dr;
	}

	public void setDr(Integer newDr) {
		this.dr = newDr;
	}

	public String getGroupdef18() {
		return this.groupdef18;
	}

	public void setGroupdef18(String newGroupdef18) {
		this.groupdef18 = newGroupdef18;
	}

	public String getGroupdef4() {
		return this.groupdef4;
	}

	public void setGroupdef4(String newGroupdef4) {
		this.groupdef4 = newGroupdef4;
	}

	public String getPk_psntype() {
		return this.pk_psntype;
	}

	public void setPk_psntype(String newPk_psntype) {
		this.pk_psntype = newPk_psntype;
	}

	public String getDef14() {
		return this.def14;
	}

	public void setDef14(String newDef14) {
		this.def14 = newDef14;
	}

	public String getDef7() {
		return this.def7;
	}

	public void setDef7(String newDef7) {
		this.def7 = newDef7;
	}

	public UFDateTime getTs() {
		return this.ts;
	}

	public void setTs(UFDateTime newTs) {
		this.ts = newTs;
	}

	public String getDef6() {
		return this.def6;
	}

	public void setDef6(String newDef6) {
		this.def6 = newDef6;
	}

	public String getDutyname() {
		return this.dutyname;
	}

	public void setDutyname(String newDutyname) {
		this.dutyname = newDutyname;
	}

	public String getGroupdef16() {
		return this.groupdef16;
	}

	public void setGroupdef16(String newGroupdef16) {
		this.groupdef16 = newGroupdef16;
	}

	public String getGroupdef13() {
		return this.groupdef13;
	}

	public void setGroupdef13(String newGroupdef13) {
		this.groupdef13 = newGroupdef13;
	}

	public String getDef15() {
		return this.def15;
	}

	public void setDef15(String newDef15) {
		this.def15 = newDef15;
	}

	public String getSealdate() {
		return this.sealdate;
	}

	public void setSealdate(String newSealdate) {
		this.sealdate = newSealdate;
	}

	public String getDef18() {
		return this.def18;
	}

	public void setDef18(String newDef18) {
		this.def18 = newDef18;
	}

	public String getGroupdef9() {
		return this.groupdef9;
	}

	public void setGroupdef9(String newGroupdef9) {
		this.groupdef9 = newGroupdef9;
	}

	public String getPk_deptdoc() {
		return this.pk_deptdoc;
	}

	public void setPk_deptdoc(String newPk_deptdoc) {
		this.pk_deptdoc = newPk_deptdoc;
	}

	public String getInsource() {
		return this.insource;
	}

	public void setInsource(String newInsource) {
		this.insource = newInsource;
	}

	public String getDef1() {
		return this.def1;
	}

	public void setDef1(String newDef1) {
		this.def1 = newDef1;
	}

	public UFDate getRegulardata() {
		return this.regulardata;
	}

	public void setRegulardata(UFDate newRegulardata) {
		this.regulardata = newRegulardata;
	}

	public String getGroupdef14() {
		return this.groupdef14;
	}

	public void setGroupdef14(String newGroupdef14) {
		this.groupdef14 = newGroupdef14;
	}

	public UFDate getIndutydate() {
		return this.indutydate;
	}

	public void setIndutydate(UFDate newIndutydate) {
		this.indutydate = newIndutydate;
	}

	public String getPsnname() {
		return this.psnname;
	}

	public void setPsnname(String newPsnname) {
		this.psnname = newPsnname;
	}

	public UFBoolean getRegular() {
		return this.regular;
	}

	public void setRegular(UFBoolean newRegular) {
		this.regular = newRegular;
	}

	public String getPk_om_job() {
		return this.pk_om_job;
	}

	public void setPk_om_job(String newPk_om_job) {
		this.pk_om_job = newPk_om_job;
	}

	public UFBoolean getIndocflag() {
		return this.indocflag;
	}

	public void setIndocflag(UFBoolean newIndocflag) {
		this.indocflag = newIndocflag;
	}

	public String getDef19() {
		return this.def19;
	}

	public void setDef19(String newDef19) {
		this.def19 = newDef19;
	}

	public String getDef20() {
		return this.def20;
	}

	public void setDef20(String newDef20) {
		this.def20 = newDef20;
	}

	public String getGroupdef8() {
		return this.groupdef8;
	}

	public void setGroupdef8(String newGroupdef8) {
		this.groupdef8 = newGroupdef8;
	}

	public String getClerkcode() {
		return this.clerkcode;
	}

	public void setClerkcode(String newClerkcode) {
		this.clerkcode = newClerkcode;
	}

	public UFBoolean getIscalovertime() {
		return this.iscalovertime;
	}

	public void setIscalovertime(UFBoolean newIscalovertime) {
		this.iscalovertime = newIscalovertime;
	}

	public String getDirectleader() {
		return this.directleader;
	}

	public void setDirectleader(String newDirectleader) {
		this.directleader = newDirectleader;
	}

	public String getOutmethod() {
		return this.outmethod;
	}

	public void setOutmethod(String newOutmethod) {
		this.outmethod = newOutmethod;
	}

	public String getGroupdef10() {
		return this.groupdef10;
	}

	public void setGroupdef10(String newGroupdef10) {
		this.groupdef10 = newGroupdef10;
	}

	public String getPk_psndoc() {
		return this.pk_psndoc;
	}

	public void setPk_psndoc(String newPk_psndoc) {
		this.pk_psndoc = newPk_psndoc;
	}

	public String getGroupdef11() {
		return this.groupdef11;
	}

	public void setGroupdef11(String newGroupdef11) {
		this.groupdef11 = newGroupdef11;
	}

	public String getInnercode() {
		return this.innercode;
	}

	public void setInnercode(String newInnercode) {
		this.innercode = newInnercode;
	}

	public String getPk_psnbasdoc() {
		return this.pk_psnbasdoc;
	}

	public void setPk_psnbasdoc(String newPk_psnbasdoc) {
		this.pk_psnbasdoc = newPk_psnbasdoc;
	}

	public String getGroupdef7() {
		return this.groupdef7;
	}

	public void setGroupdef7(String newGroupdef7) {
		this.groupdef7 = newGroupdef7;
	}

	public String getDef17() {
		return this.def17;
	}

	public void setDef17(String newDef17) {
		this.def17 = newDef17;
	}

	public String getDef11() {
		return this.def11;
	}

	public void setDef11(String newDef11) {
		this.def11 = newDef11;
	}

	public Integer getPsnclscope() {
		return this.psnclscope;
	}

	public void setPsnclscope(Integer newPsnclscope) {
		this.psnclscope = newPsnclscope;
	}

	public String getPsncode() {
		return this.psncode;
	}

	public void setPsncode(String newPsncode) {
		this.psncode = newPsncode;
	}

	public String getDef16() {
		return this.def16;
	}

	public void setDef16(String newDef16) {
		this.def16 = newDef16;
	}

	public String getMaxinnercode() {
		return this.maxinnercode;
	}

	public void setMaxinnercode(String newMaxinnercode) {
		this.maxinnercode = newMaxinnercode;
	}

	public Integer getShoworder() {
		return this.showorder;
	}

	public void setShoworder(Integer newShoworder) {
		this.showorder = newShoworder;
	}

	public UFBoolean getClerkflag() {
		return this.clerkflag;
	}

	public void setClerkflag(UFBoolean newClerkflag) {
		this.clerkflag = newClerkflag;
	}

	public String getGroupdef3() {
		return this.groupdef3;
	}

	public void setGroupdef3(String newGroupdef3) {
		this.groupdef3 = newGroupdef3;
	}

	public UFBoolean getIsreferenced() {
		return this.isreferenced;
	}

	public void setIsreferenced(UFBoolean newIsreferenced) {
		this.isreferenced = newIsreferenced;
	}

	public String getSeries() {
		return this.series;
	}

	public void setSeries(String newSeries) {
		this.series = newSeries;
	}

	public UFDate getOutdutydate() {
		return this.outdutydate;
	}

	public void setOutdutydate(UFDate newOutdutydate) {
		this.outdutydate = newOutdutydate;
	}

	public UFBoolean getIsreturn() {
		return this.isreturn;
	}

	public void setIsreturn(UFBoolean newIsreturn) {
		this.isreturn = newIsreturn;
	}

	public String getDef10() {
		return this.def10;
	}

	public void setDef10(String newDef10) {
		this.def10 = newDef10;
	}

	public String getPk_psncl() {
		return this.pk_psncl;
	}

	public void setPk_psncl(String newPk_psncl) {
		this.pk_psncl = newPk_psncl;
	}

	public String getGroupdef15() {
		return this.groupdef15;
	}

	public void setGroupdef15(String newGroupdef15) {
		this.groupdef15 = newGroupdef15;
	}

	public String getGroupdef5() {
		return this.groupdef5;
	}

	public void setGroupdef5(String newGroupdef5) {
		this.groupdef5 = newGroupdef5;
	}

	public String getAmcode() {
		return this.amcode;
	}

	public void setAmcode(String newAmcode) {
		this.amcode = newAmcode;
	}

	public String getJobrank() {
		return this.jobrank;
	}

	public void setJobrank(String newJobrank) {
		this.jobrank = newJobrank;
	}

	public String getGroupdef2() {
		return this.groupdef2;
	}

	public void setGroupdef2(String newGroupdef2) {
		this.groupdef2 = newGroupdef2;
	}

	public String getDef13() {
		return this.def13;
	}

	public void setDef13(String newDef13) {
		this.def13 = newDef13;
	}

	public String getDef8() {
		return this.def8;
	}

	public void setDef8(String newDef8) {
		this.def8 = newDef8;
	}

	public String getJobseries() {
		return this.jobseries;
	}

	public void setJobseries(String newJobseries) {
		this.jobseries = newJobseries;
	}

	public String getGroupdef1() {
		return this.groupdef1;
	}

	public void setGroupdef1(String newGroupdef1) {
		this.groupdef1 = newGroupdef1;
	}

	public UFBoolean getPoststat() {
		return this.poststat;
	}

	public void setPoststat(UFBoolean newPoststat) {
		this.poststat = newPoststat;
	}

	public String getRecruitresource() {
		return this.recruitresource;
	}

	public void setRecruitresource(String newRecruitresource) {
		this.recruitresource = newRecruitresource;
	}

	public String getGroupdef20() {
		return this.groupdef20;
	}

	public void setGroupdef20(String newGroupdef20) {
		this.groupdef20 = newGroupdef20;
	}

	public UFDate getWastopdate() {
		return this.wastopdate;
	}

	public void setWastopdate(UFDate newWastopdate) {
		this.wastopdate = newWastopdate;
	}

	public String getGroupdef17() {
		return this.groupdef17;
	}

	public void setGroupdef17(String newGroupdef17) {
		this.groupdef17 = newGroupdef17;
	}

	public String getDef2() {
		return this.def2;
	}

	public void setDef2(String newDef2) {
		this.def2 = newDef2;
	}

	public String getPk_dutyrank() {
		return this.pk_dutyrank;
	}

	public void setPk_dutyrank(String newPk_dutyrank) {
		this.pk_dutyrank = newPk_dutyrank;
	}

	public String getDef12() {
		return this.def12;
	}

	public void setDef12(String newDef12) {
		this.def12 = newDef12;
	}

	public String getGroupdef12() {
		return this.groupdef12;
	}

	public void setGroupdef12(String newGroupdef12) {
		this.groupdef12 = newGroupdef12;
	}

	public String getTimecardid() {
		return this.timecardid;
	}

	public void setTimecardid(String newTimecardid) {
		this.timecardid = newTimecardid;
	}

	public String getDef4() {
		return this.def4;
	}

	public void setDef4(String newDef4) {
		this.def4 = newDef4;
	}

	public Integer getTbm_prop() {
		return this.tbm_prop;
	}

	public void setTbm_prop(Integer newTbm_prop) {
		this.tbm_prop = newTbm_prop;
	}

	public String getGroupdef6() {
		return this.groupdef6;
	}

	public void setGroupdef6(String newGroupdef6) {
		this.groupdef6 = newGroupdef6;
	}

	public String getDef3() {
		return this.def3;
	}

	public void setDef3(String newDef3) {
		this.def3 = newDef3;
	}

	public String getDef9() {
		return this.def9;
	}

	public void setDef9(String newDef9) {
		this.def9 = newDef9;
	}

	public String getPk_belongcorp() {
		return this.pk_belongcorp;
	}

	public void setPk_belongcorp(String pk_belongcorp) {
		this.pk_belongcorp = pk_belongcorp;
	}

	// public void validate()
	// throws ValidationException
	// {
	// ArrayList errFields = new ArrayList();
	//
	// if (this.pk_psndoc == null) {
	// errFields.add(new String("pk_psndoc"));
	// }
	// if (this.pk_psnbasdoc == null) {
	// errFields.add(new String("pk_psnbasdoc"));
	// }
	// if (this.pk_psncl == null) {
	// errFields.add(new String("pk_psncl"));
	// }
	//
	// StringBuffer message = new StringBuffer();
	// message.append("下列字段不能为空:");
	// if (errFields.size() > 0) {
	// String[] temp = (String[])(String[])errFields.toArray(new String[0]);
	// message.append(temp[0]);
	// for (int i = 1; i < temp.length; ++i) {
	// message.append(",");
	// message.append(temp[i]);
	// }
	// throw new NullFieldException(message.toString());
	// }
	// }

	public String getParentPKFieldName() {
		return null;
	}

	public String getPKFieldName() {
		return "pk_psndoc";
	}

	public String getTableName() {
		return "bd_psndoc";
	}

	public PsndocVO() {
	}

	public PsndocVO(String newPk_psndoc) {
		this.pk_psndoc = newPk_psndoc;
	}

	public String getPrimaryKey() {
		return this.pk_psndoc;
	}

	public void setPrimaryKey(String newPk_psndoc) {
		this.pk_psndoc = newPk_psndoc;
	}

	public String getEntityName() {
		return "bd_psndoc";
	}

	public UFDate getOnpostdate() {
		return this.onpostdate;
	}

	public void setOnpostdate(UFDate onpostdate) {
		this.onpostdate = onpostdate;
	}

	public String getGroupdef21() {
		return groupdef21;
	}

	public void setGroupdef21(String groupdef21) {
		this.groupdef21 = groupdef21;
	}

	public String getGroupdef22() {
		return groupdef22;
	}

	public void setGroupdef22(String groupdef22) {
		this.groupdef22 = groupdef22;
	}

	public String getGroupdef23() {
		return groupdef23;
	}

	public void setGroupdef23(String groupdef23) {
		this.groupdef23 = groupdef23;
	}

	public String getGroupdef24() {
		return groupdef24;
	}

	public void setGroupdef24(String groupdef24) {
		this.groupdef24 = groupdef24;
	}

	public String getGroupdef25() {
		return groupdef25;
	}

	public void setGroupdef25(String groupdef25) {
		this.groupdef25 = groupdef25;
	}

	public String getGroupdef26() {
		return groupdef26;
	}

	public void setGroupdef26(String groupdef26) {
		this.groupdef26 = groupdef26;
	}

	public String getGroupdef27() {
		return groupdef27;
	}

	public void setGroupdef27(String groupdef27) {
		this.groupdef27 = groupdef27;
	}

	public String getGroupdef28() {
		return groupdef28;
	}

	public void setGroupdef28(String groupdef28) {
		this.groupdef28 = groupdef28;
	}

	public String getGroupdef29() {
		return groupdef29;
	}

	public void setGroupdef29(String groupdef29) {
		this.groupdef29 = groupdef29;
	}

	public String getGroupdef30() {
		return groupdef30;
	}

	public void setGroupdef30(String groupdef30) {
		this.groupdef30 = groupdef30;
	}

	public String getGroupdef31() {
		return groupdef31;
	}

	public void setGroupdef31(String groupdef31) {
		this.groupdef31 = groupdef31;
	}

	public String getGroupdef32() {
		return groupdef32;
	}

	public void setGroupdef32(String groupdef32) {
		this.groupdef32 = groupdef32;
	}

	public String getGroupdef33() {
		return groupdef33;
	}

	public void setGroupdef33(String groupdef33) {
		this.groupdef33 = groupdef33;
	}

	public String getGroupdef34() {
		return groupdef34;
	}

	public void setGroupdef34(String groupdef34) {
		this.groupdef34 = groupdef34;
	}

	public String getGroupdef35() {
		return groupdef35;
	}

	public void setGroupdef35(String groupdef35) {
		this.groupdef35 = groupdef35;
	}

	public String getGroupdef36() {
		return groupdef36;
	}

	public void setGroupdef36(String groupdef36) {
		this.groupdef36 = groupdef36;
	}

	public String getGroupdef37() {
		return groupdef37;
	}

	public void setGroupdef37(String groupdef37) {
		this.groupdef37 = groupdef37;
	}

	public String getGroupdef38() {
		return groupdef38;
	}

	public void setGroupdef38(String groupdef38) {
		this.groupdef38 = groupdef38;
	}

	public String getGroupdef39() {
		return groupdef39;
	}

	public void setGroupdef39(String groupdef39) {
		this.groupdef39 = groupdef39;
	}

	public String getGroupdef40() {
		return groupdef40;
	}

	public void setGroupdef40(String groupdef40) {
		this.groupdef40 = groupdef40;
	}

	public String getGroupdef41() {
		return groupdef41;
	}

	public void setGroupdef41(String groupdef41) {
		this.groupdef41 = groupdef41;
	}

	public String getGroupdef42() {
		return groupdef42;
	}

	public void setGroupdef42(String groupdef42) {
		this.groupdef42 = groupdef42;
	}

	public String getGroupdef43() {
		return groupdef43;
	}

	public void setGroupdef43(String groupdef43) {
		this.groupdef43 = groupdef43;
	}

	public String getGroupdef44() {
		return groupdef44;
	}

	public void setGroupdef44(String groupdef44) {
		this.groupdef44 = groupdef44;
	}

	public String getGroupdef45() {
		return groupdef45;
	}

	public void setGroupdef45(String groupdef45) {
		this.groupdef45 = groupdef45;
	}

	public String getGroupdef46() {
		return groupdef46;
	}

	public void setGroupdef46(String groupdef46) {
		this.groupdef46 = groupdef46;
	}

	public String getGroupdef47() {
		return groupdef47;
	}

	public void setGroupdef47(String groupdef47) {
		this.groupdef47 = groupdef47;
	}

	public String getGroupdef48() {
		return groupdef48;
	}

	public void setGroupdef48(String groupdef48) {
		this.groupdef48 = groupdef48;
	}

	public String getGroupdef49() {
		return groupdef49;
	}

	public void setGroupdef49(String groupdef49) {
		this.groupdef49 = groupdef49;
	}

	public String getGroupdef50() {
		return groupdef50;
	}

	public void setGroupdef50(String groupdef50) {
		this.groupdef50 = groupdef50;
	}

	public String getGroupdef51() {
		return groupdef51;
	}

	public void setGroupdef51(String groupdef51) {
		this.groupdef51 = groupdef51;
	}

	public String getGroupdef52() {
		return groupdef52;
	}

	public void setGroupdef52(String groupdef52) {
		this.groupdef52 = groupdef52;
	}

	public String getGroupdef53() {
		return groupdef53;
	}

	public void setGroupdef53(String groupdef53) {
		this.groupdef53 = groupdef53;
	}

	public String getGroupdef54() {
		return groupdef54;
	}

	public void setGroupdef54(String groupdef54) {
		this.groupdef54 = groupdef54;
	}

	public String getGroupdef55() {
		return groupdef55;
	}

	public void setGroupdef55(String groupdef55) {
		this.groupdef55 = groupdef55;
	}

	public String getGroupdef56() {
		return groupdef56;
	}

	public void setGroupdef56(String groupdef56) {
		this.groupdef56 = groupdef56;
	}

	public String getGroupdef57() {
		return groupdef57;
	}

	public void setGroupdef57(String groupdef57) {
		this.groupdef57 = groupdef57;
	}

	public String getGroupdef58() {
		return groupdef58;
	}

	public void setGroupdef58(String groupdef58) {
		this.groupdef58 = groupdef58;
	}

	public String getGroupdef59() {
		return groupdef59;
	}

	public void setGroupdef59(String groupdef59) {
		this.groupdef59 = groupdef59;
	}

	public String getGroupdef60() {
		return groupdef60;
	}

	public void setGroupdef60(String groupdef60) {
		this.groupdef60 = groupdef60;
	}

	public String getGroupdef61() {
		return groupdef61;
	}

	public void setGroupdef61(String groupdef61) {
		this.groupdef61 = groupdef61;
	}

	public String getGroupdef62() {
		return groupdef62;
	}

	public void setGroupdef62(String groupdef62) {
		this.groupdef62 = groupdef62;
	}

	public String getGroupdef63() {
		return groupdef63;
	}

	public void setGroupdef63(String groupdef63) {
		this.groupdef63 = groupdef63;
	}

	public String getGroupdef64() {
		return groupdef64;
	}

	public void setGroupdef64(String groupdef64) {
		this.groupdef64 = groupdef64;
	}

	public String getGroupdef65() {
		return groupdef65;
	}

	public void setGroupdef65(String groupdef65) {
		this.groupdef65 = groupdef65;
	}

	public String getGroupdef66() {
		return groupdef66;
	}

	public void setGroupdef66(String groupdef66) {
		this.groupdef66 = groupdef66;
	}

	public String getGroupdef67() {
		return groupdef67;
	}

	public void setGroupdef67(String groupdef67) {
		this.groupdef67 = groupdef67;
	}

	public String getGroupdef68() {
		return groupdef68;
	}

	public void setGroupdef68(String groupdef68) {
		this.groupdef68 = groupdef68;
	}

	public String getGroupdef69() {
		return groupdef69;
	}

	public void setGroupdef69(String groupdef69) {
		this.groupdef69 = groupdef69;
	}

	public String getGroupdef70() {
		return groupdef70;
	}

	public void setGroupdef70(String groupdef70) {
		this.groupdef70 = groupdef70;
	}

	public String getGroupdef71() {
		return groupdef71;
	}

	public void setGroupdef71(String groupdef71) {
		this.groupdef71 = groupdef71;
	}

	public String getGroupdef72() {
		return groupdef72;
	}

	public void setGroupdef72(String groupdef72) {
		this.groupdef72 = groupdef72;
	}

	public String getGroupdef73() {
		return groupdef73;
	}

	public void setGroupdef73(String groupdef73) {
		this.groupdef73 = groupdef73;
	}

	public String getGroupdef74() {
		return groupdef74;
	}

	public void setGroupdef74(String groupdef74) {
		this.groupdef74 = groupdef74;
	}

	public String getGroupdef75() {
		return groupdef75;
	}

	public void setGroupdef75(String groupdef75) {
		this.groupdef75 = groupdef75;
	}

	public String getGroupdef76() {
		return groupdef76;
	}

	public void setGroupdef76(String groupdef76) {
		this.groupdef76 = groupdef76;
	}

	public String getGroupdef77() {
		return groupdef77;
	}

	public void setGroupdef77(String groupdef77) {
		this.groupdef77 = groupdef77;
	}

	public String getGroupdef78() {
		return groupdef78;
	}

	public void setGroupdef78(String groupdef78) {
		this.groupdef78 = groupdef78;
	}

	public String getGroupdef79() {
		return groupdef79;
	}

	public void setGroupdef79(String groupdef79) {
		this.groupdef79 = groupdef79;
	}

	public String getGroupdef80() {
		return groupdef80;
	}

	public void setGroupdef80(String groupdef80) {
		this.groupdef80 = groupdef80;
	}
}