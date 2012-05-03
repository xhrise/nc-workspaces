package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.pub.lang.*;
/**
 * 参加虚拟组织。
 * 创建日期：(2004-5-18 8:58:49)
 * @author：Administrator
 */
public class OrgpsnValidator extends CurrentSubsetValidator {
/**
 * OrgpsnValidator 构造子注解。
 */
public OrgpsnValidator() {
	super();
	setRules(new String[][]{
			{"pk_om_dumorg",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000158")/*@res "虚拟组织"*/,"notnull"},
			{"pk_orgrole",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000159")/*@res "虚拟组织角色"*/,"notnull"}
		});
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-8-2 16:31:20)
 * @param record nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception 异常说明。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
	throws java.lang.Exception {
	 super.validate(record);
	 UFDate begindate=null;
	 begindate = (UFDate) record.getAttributeValue("djoindate");
	 UFBoolean flag = (UFBoolean)record.getAttributeValue("bquit");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dquitdate");
		if (begindate != null) {			
				if (enddate != null && begindate.after(enddate))
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("600704",
									"UPP600704-000160")/* @res "退出时间不能早于加入时间！" */);
				GeneralVO vo = HIDelegator.getPsnInf().queryDetailForDumorg(
						(String) record.getAttributeValue("$pk_om_dumorg"));
				if (vo != null && vo.getAttributeValue("builddate") != null) {
					UFDate builddate = new UFDate((String) vo
							.getAttributeValue("builddate"));
					if (builddate != null && builddate.after(begindate))
						throw new Exception(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("600704",
										"UPT600704-000326")/* @res "参加日期不能早于虚拟组织的创建日期！" */);
				}
			}
			if (enddate != null && flag == null || enddate != null
					&& !flag.booleanValue()) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("600704", "UPP600704-000161")/*
																	 * @res
																	 * "未退出虚拟组织的人员，不能有退出日期"
																	 */);
			}
			if (flag != null && flag.booleanValue() && enddate == null) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("600704", "UPP600704-000162")/*@res "已经退出虚拟组织，请输入退出日期"*/);
			}
	} catch (Exception ex) {
		rethrow(ex, "dquitdate");
	}
}
}
