package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.vo.pub.lang.UFDate;

/**
 * 劳动合同。
 * 创建日期：(2004-5-18 9:17:14)
 * @author：Administrator
 */
public class CtrtValidator extends OrderlessSubsetValidator {
/**
 * CtrtValidator 构造子注解。
 */
	public CtrtValidator() {
		super();
		setRules(new String[][] {
				{
						"vcontractnum",
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"600704", "UPP600704-100178")/* @res "合同编号" */,
						"notnull" },
				{
						"begindate",
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"600704", "UPP600704-100179")/* @res "合同起始日期" */,
						"notnull" },
				{
						"pk_termtype",
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"600704", "UPP600704-100180")/*@res "合同期限类型"*/,
						"notnull" } });
	}
/**
 *合同编号唯一
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
	try {
		//劳动合同子集，当 固定期限类型 是固定期限时，增加结束日期的非空检验。
		String pk_termtype = (String)record.getAttributeValue("$pk_termtype");
		if("0001AA1000000000120W".equals(pk_termtype)){//是固定期限
			setRules(new String[][]{
					{"vcontractnum",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100178")/*@res "合同编号"*/,"notnull"},
					{"begindate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100179")/*@res "合同起始日期"*/,"notnull"},
					{"pk_termtype",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100180")/*@res "合同期限类型"*/,"notnull"},
					{"enddate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100181")/*@res "合同结束日期"*/,"notnull"}
			});
		}else{
			setRules(new String[][]{
					{"vcontractnum",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100178")/*@res "合同编号"*/,"notnull"},
					{"begindate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100179")/*@res "合同起始日期"*/,"notnull"},
					{"pk_termtype",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100180")/*@res "合同期限类型"*/,"notnull"}
			});
		}
		super.validate(record);
		//
		//开始日期非空
		UFDate probegindate = (UFDate) record.getAttributeValue("probegindate");
		UFDate probenddate = (UFDate) record.getAttributeValue("probenddate");
		//开始日期必须小于结束日期
		if (probegindate != null && probenddate != null)
			if (probegindate.after(probenddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPT600704-000324")/*@res "试用开始日期不能晚于试用结束日期！"*/);

		//检查编码是否唯一
		String pk_psnbasdoc = (String) record.getAttributeValue("pk_psnbasdoc");
		String vcontractnum = (String) record.getAttributeValue("vcontractnum");
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("hi_psndoc_ctrt", "pk_psnbasdoc", pk_psnbasdoc, "vcontractnum", vcontractnum, null);
		if (exists){
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000133")/*@res "合同编码："*/ + vcontractnum + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000123")/*@res "已经存在！"*/);
		}
	} catch (Exception e) {
		rethrow(e, "vcontractnum");
	}
}
}
