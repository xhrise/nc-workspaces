package nc.vo.hi.hi_301.validator;

/**
 * 此处插入类型描述。
 * 创建日期：(2004-5-14 16:59:29)
 * @author：Administrator
 */
import nc.bs.framework.common.NCLocator;
import nc.itf.hi.HIDelegator;
import nc.itf.uap.bd.psn.IPsncl;
import nc.ui.pub.ClientEnvironment;
import nc.vo.hi.hi_301.*;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.*;

import java.util.*;
public class PsndocValidator extends AbstractValidator {
	private static final Hashtable psnclMap=new Hashtable();
	static{
		init();
	}
/**
 * PsndocValidator 构造子注解。
 */
public PsndocValidator() {
	super();
	setRules(new String[][] {
//	    {"psncode", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000025")/*@res "人员编码"*/, "notnull" },
//	    {"psnname", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000064")/*@res "人员姓名"*/, "notnull" },
	    {"pk_psncl", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000042")/*@res "人员类别"*/, "notnull"}
	});
}
private static void init() {
	try {
		IPsncl ipsncl = (IPsncl)NCLocator.getInstance().lookup(IPsncl.class.getName());
		nc.vo.bd.b05.PsnclVO[] psncls = ipsncl.queryAllPsnclVOs(ClientEnvironment.getInstance().getCorporation().getPk_corp());
		for (int i = 0; i < psncls.length; i++)
			psnclMap.put(psncls[i].getPk_psncl(), psncls[i]);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
/**
 * validate 方法注解。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records) throws Exception {
	nc.vo.pub.CircularlyAccessibleValueObject psndocVO = records[0];
	int jobtype = (((Integer)((GeneralVO)psndocVO).getAttributeValue("jobtypeflag")).intValue());//wangkf fixed jobtype ->jobtypeflag
	if(jobtype!=0){
	    return;
	}
	//检查编码是否存在
	String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
	String pk_psndoc = (String) psndocVO.getAttributeValue("pk_psndoc");
	//检查职员编码
	UFBoolean clerkflag=(UFBoolean)psndocVO.getAttributeValue("clerkflag");
	if(clerkflag!=null&&clerkflag.booleanValue()){//起用职员编码
	    String clerkcode=(String)psndocVO.getAttributeValue("clerkcode");
	    FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000028")/*@res "职员编号"*/,"notnull",clerkcode);
	    //职员编码非空
	    boolean exists=HIDelegator.getIHRhiQBS().recordExists("bd_psndoc","pk_psndoc",pk_psndoc,"clerkcode",clerkcode,"pk_corp='"+pk_corp+"'");
	    //并且公司内唯一
	    if(exists)
	    	throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000165")/*@res "职员编号："*/+clerkcode+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000123")/*@res "已经存在！"*/);
	}
	//部门和岗位对应关系是否正确
	String deptpk = (String) ((GeneralVO)psndocVO).getFieldValue("pk_deptdoc");
	String jobpk = (String) ((GeneralVO)psndocVO).getFieldValue("pk_om_job");
	if (deptpk!= null&&jobpk!=null) {
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("om_job", "", null, "pk_om_job", jobpk, "pk_deptdoc='"+deptpk+"'");
		if (!exists)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000147")/*@res "选择的岗位和部门不匹配，请重新选择！"*/);
	}
	
	//V35 add
	String timecardid = (String) psndocVO.getAttributeValue("timecardid");
	if (timecardid != null && timecardid.trim().length() > 0) {
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("bd_psndoc", "bd_psndoc.pk_psndoc", pk_psndoc, "timecardid", timecardid, "bd_psndoc.pk_corp = '"+pk_corp+"'");
		if (exists)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000124")/*@res "考勤卡号："*/ + timecardid + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000123")/*@res "已经存在！"*/);
	}
}

	/**
	 * validate 方法注解。
	 */
	public void validateOld(nc.vo.pub.CircularlyAccessibleValueObject record)
			throws Exception {
		super.validate(record);
		int jobtype = (((Integer) ((GeneralVO) record)
				.getAttributeValue("jobtypeflag")).intValue());
		if (jobtype != 0) {
			return;
		}
		// 根据人员类别设置人员归属范围
		String pk_psncl = (String) record.getAttributeValue("pk_psncl");
		nc.vo.bd.b05.PsnclVO psncl = (nc.vo.bd.b05.PsnclVO) psnclMap
				.get(pk_psncl);
		if (psncl == null) {
			IPsncl ipsncl = (IPsncl) NCLocator.getInstance().lookup(
					IPsncl.class.getName());
			psncl = ipsncl.findPsnclVOByPk(pk_psncl);
			if (psncl != null) {
				psnclMap.put(pk_psncl, psncl);
			} else {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("600704", "upt600704-000042")/*
																	 * @res
																	 * "人员类别"
																	 */
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("600704", "UPP600704-000003")/*
																			 * @res
																			 * "该记录已经被删除,请刷新后再试！"
																			 */);
			}

		}
		int psnclscope = 0;
		if (psncl != null) {
			psnclscope = psncl.getPsnclscope();
			record.setAttributeValue("psnclscope", psncl.getPsnclscope());
		}
		boolean isonduty = true;
		if (psnclscope == 0 || psnclscope == 5) {
			isonduty = true;
		} else {
			isonduty = false;
		}
		String pk_deptdoc = (String) record.getAttributeValue("pk_deptdoc");
		try {
			// 部门，只有在维护时检查
			if (pk_deptdoc != null) {
				boolean bExist = HIDelegator.getIHRhiQBS().recordExists(
						"bd_deptdoc", null, null, "pk_deptdoc", pk_deptdoc,
						null);
				if (!bExist) {
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("600704",
									"upt600704-000139")/* @res "部门" */
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("600704", "UPP600704-000003")/*
																				 * @res
																				 * "该记录已经被删除,请刷新后再试！"
																				 */);
				}
			}
			Boolean maintain = (Boolean) record.getAttributeValue("maintain");
			if (maintain != null && maintain.booleanValue())
				FieldValidator.validate(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"600704", "upt600704-000139")/* @res "部门" */,
						"notnull", pk_deptdoc);
		} catch (Exception e) {
			rethrow(e, "pk_deptdoc");
		}
		String pk_om_job = (String) record.getAttributeValue("pk_om_job");
		try {
			if (pk_deptdoc != null && pk_deptdoc.length() == 20) {
				boolean bExist = HIDelegator.getIHRhiQBS().recordExists(
						"bd_deptdoc", null, null, "pk_deptdoc", pk_deptdoc,
						"hrcanceled='Y'");
				if (bExist && isonduty) {
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("600704",
									"UPP600704-000166")/*
														 * @res
														 * "选择的部门已经撤销，不能保存！"
														 */);
				}
			}
			// wangkf add
			if (pk_om_job != null && pk_om_job.length() == 20) {
				boolean bExist = HIDelegator.getIHRhiQBS().recordExists(
						"om_job", null, null, "pk_om_job", pk_om_job,
						"isabort='Y'");
				if (bExist && isonduty) {
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("600704",
									"UPP600704-000178")/*
														 * @res
														 * "选择的岗位已经撤销，不能保存！"
														 */);
				}
				// 判断是否删除
				bExist = HIDelegator.getIHRhiQBS().recordExists("om_job", null,
						null, "pk_om_job", pk_om_job, "0=0");
				if (!bExist) {
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("600704",
									"UPP600704-100003")/*@res "岗位"*/
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("600704", "UPP600704-000003")/*@res "该记录已经被删除,请刷新后再试！"*/);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * validate 方法注解。
	 */
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
			throws Exception {
		super.validate(record);
		int jobtype = (((Integer) ((GeneralVO) record)
				.getAttributeValue("jobtypeflag")).intValue());
		if (jobtype != 0) {
			return;
		}
		// 检查职员编码
		UFBoolean clerkflag = (UFBoolean) record.getAttributeValue("clerkflag");
		if (clerkflag != null && clerkflag.booleanValue()) {// 起用职员编码
			String clerkcode = (String) record.getAttributeValue("clerkcode");
			FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("600704", "upt600704-000028")/* @res "职员编号" */,
					"notnull", clerkcode);
		}
		Boolean maintain = (Boolean) record.getAttributeValue("maintain");
		if (maintain != null && maintain.booleanValue()) {
			String pk_deptdoc = (String) record.getAttributeValue("pk_deptdoc");
			FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("600704", "upt600704-000139")/* @res "部门" */,
					"notnull", pk_deptdoc);
		}
		
		//设置psnclscope不应该放在校验类里。已被移到PsnInfCollectUI的getInputData()中。dusx 修改2009.5.7
		GeneralVO psnVO = HIDelegator.getPsnInf().validatePsndocInf(
				(GeneralVO) record,
				ClientEnvironment.getInstance().getCorporation().getPk_corp());
//		// 
//		record.setAttributeValue("psnclscope", psnVO
//				.getAttributeValue("psnclscope"));

	}
/**
 * validate 方法注解。
 */
public void validate(nc.vo.pub.ExtendedAggregatedValueObject person) throws Exception {
	//joinsysdate
	//indutydate
	UFDate joinsysdate = (UFDate)((PersonEAVO)person).getAccpsndocVO().getAttributeValue("joinsysdate");
	UFDate indutydate = (UFDate)((PersonEAVO)person).getPsndocVO().getAttributeValue("indutydate");
	if(joinsysdate!=null&&indutydate!=null){
		if(joinsysdate.toDate().compareTo(indutydate.toDate())>0){
//			throw new Exception("到职日期不能早于进入集团日期！");
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("600700", "UPP600700-000434")/* @res "到职日期不能早于进入集团日期！" */);
		}
	}
	
}

}
