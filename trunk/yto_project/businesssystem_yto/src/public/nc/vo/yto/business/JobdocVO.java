package nc.vo.yto.business;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class JobdocVO extends SuperVO {

	private UFDate abortdate;

	private String bespvised;

	private UFDate builddate;

	private String createcorp;

	private int dr;

	private String filepath;

	private String groupdef1;

	private String groupdef10;

	private String groupdef2;

	private String groupdef3;

	private String groupdef4;

	private String groupdef5;

	private String groupdef6;

	private String groupdef7;

	private String groupdef8;

	private String groupdef9;

	private String horisitu;

	private String incontact;

	private UFBoolean isabort;

	private UFBoolean isdeptrespon;

	private String jobcode;

	private String jobname;

	private String jobrank;

	private String jobseries;

	private String junior;

	private String outcontact;

	private String pk_corp;

	private String pk_deptdoc;

	private String pk_jobdoc;

	private String pk_om_duty;

	private String pk_om_job;

	private String suporior;

	private String tospvise;

	private UFDateTime ts;

	private String worksumm;

	public static final String ABORTDATE = "abortdate";

	public static final String BESPVISED = "bespvised";

	public static final String BUILDDATE = "builddate";

	public static final String CREATECORP = "createcorp";

	public static final String DR = "dr";

	public static final String FILEPATH = "filepath";

	public static final String GROUPDEF1 = "groupdef1";

	public static final String GROUPDEF10 = "groupdef10";

	public static final String GROUPDEF2 = "groupdef2";

	public static final String GROUPDEF3 = "groupdef3";

	public static final String GROUPDEF4 = "groupdef4";

	public static final String GROUPDEF5 = "groupdef5";

	public static final String GROUPDEF6 = "groupdef6";

	public static final String GROUPDEF7 = "groupdef7";

	public static final String GROUPDEF8 = "groupdef8";

	public static final String GROUPDEF9 = "groupdef9";

	public static final String HORISITU = "horisitu";

	public static final String INCONTACT = "incontact";

	public static final String ISABORT = "isabort";

	public static final String ISDEPTRESPON = "isdeptrespon";

	public static final String JOBCODE = "jobcode";

	public static final String JOBNAME = "jobname";

	public static final String JOBRANK = "jobrank";

	public static final String JOBSERIES = "jobseries";

	public static final String JUNIOR = "junior";

	public static final String OUTCONTACT = "outcontact";

	public static final String PK_CORP = "pk_corp";

	public static final String PK_DEPTDOC = "pk_deptdoc";

	public static final String PK_JOBDOC = "pk_jobdoc";

	public static final String PK_OM_DUTY = "pk_om_duty";

	public static final String PK_OM_JOB = "pk_om_job";

	public static final String SUPORIOR = "suporior";

	public static final String TOSPVISE = "tospvise";

	public static final String TS = "ts";

	public static final String WORKSUMM = "worksumm";

	public UFDate getAbortdate() {
		return abortdate;
	}

	public void setAbortdate(UFDate abortdate) {
		this.abortdate = abortdate;
	}

	public String getBespvised() {
		return bespvised;
	}

	public void setBespvised(String bespvised) {
		this.bespvised = bespvised;
	}

	public UFDate getBuilddate() {
		return builddate;
	}

	public void setBuilddate(UFDate builddate) {
		this.builddate = builddate;
	}

	public String getCreatecorp() {
		return createcorp;
	}

	public void setCreatecorp(String createcorp) {
		this.createcorp = createcorp;
	}

	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getGroupdef1() {
		return groupdef1;
	}

	public void setGroupdef1(String groupdef1) {
		this.groupdef1 = groupdef1;
	}

	public String getGroupdef10() {
		return groupdef10;
	}

	public void setGroupdef10(String groupdef10) {
		this.groupdef10 = groupdef10;
	}

	public String getGroupdef2() {
		return groupdef2;
	}

	public void setGroupdef2(String groupdef2) {
		this.groupdef2 = groupdef2;
	}

	public String getGroupdef3() {
		return groupdef3;
	}

	public void setGroupdef3(String groupdef3) {
		this.groupdef3 = groupdef3;
	}

	public String getGroupdef4() {
		return groupdef4;
	}

	public void setGroupdef4(String groupdef4) {
		this.groupdef4 = groupdef4;
	}

	public String getGroupdef5() {
		return groupdef5;
	}

	public void setGroupdef5(String groupdef5) {
		this.groupdef5 = groupdef5;
	}

	public String getGroupdef6() {
		return groupdef6;
	}

	public void setGroupdef6(String groupdef6) {
		this.groupdef6 = groupdef6;
	}

	public String getGroupdef7() {
		return groupdef7;
	}

	public void setGroupdef7(String groupdef7) {
		this.groupdef7 = groupdef7;
	}

	public String getGroupdef8() {
		return groupdef8;
	}

	public void setGroupdef8(String groupdef8) {
		this.groupdef8 = groupdef8;
	}

	public String getGroupdef9() {
		return groupdef9;
	}

	public void setGroupdef9(String groupdef9) {
		this.groupdef9 = groupdef9;
	}

	public String getHorisitu() {
		return horisitu;
	}

	public void setHorisitu(String horisitu) {
		this.horisitu = horisitu;
	}

	public String getIncontact() {
		return incontact;
	}

	public void setIncontact(String incontact) {
		this.incontact = incontact;
	}

	public UFBoolean getIsabort() {
		return isabort;
	}

	public void setIsabort(UFBoolean isabort) {
		this.isabort = isabort;
	}

	public UFBoolean getIsdeptrespon() {
		return isdeptrespon;
	}

	public void setIsdeptrespon(UFBoolean isdeptrespon) {
		this.isdeptrespon = isdeptrespon;
	}

	public String getJobcode() {
		return jobcode;
	}

	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getJobrank() {
		return jobrank;
	}

	public void setJobrank(String jobrank) {
		this.jobrank = jobrank;
	}

	public String getJobseries() {
		return jobseries;
	}

	public void setJobseries(String jobseries) {
		this.jobseries = jobseries;
	}

	public String getJunior() {
		return junior;
	}

	public void setJunior(String junior) {
		this.junior = junior;
	}

	public String getOutcontact() {
		return outcontact;
	}

	public void setOutcontact(String outcontact) {
		this.outcontact = outcontact;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getPk_jobdoc() {
		return pk_jobdoc;
	}

	public void setPk_jobdoc(String pk_jobdoc) {
		this.pk_jobdoc = pk_jobdoc;
	}

	public String getPk_om_duty() {
		return pk_om_duty;
	}

	public void setPk_om_duty(String pk_om_duty) {
		this.pk_om_duty = pk_om_duty;
	}

	public String getPk_om_job() {
		return pk_om_job;
	}

	public void setPk_om_job(String pk_om_job) {
		this.pk_om_job = pk_om_job;
	}

	public String getSuporior() {
		return suporior;
	}

	public void setSuporior(String suporior) {
		this.suporior = suporior;
	}

	public String getTospvise() {
		return tospvise;
	}

	public void setTospvise(String tospvise) {
		this.tospvise = tospvise;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getWorksumm() {
		return worksumm;
	}

	public void setWorksumm(String worksumm) {
		this.worksumm = worksumm;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_om_job";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "om_job";
	}

}
