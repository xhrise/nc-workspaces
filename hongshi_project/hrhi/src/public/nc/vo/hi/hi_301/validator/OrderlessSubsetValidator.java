package nc.vo.hi.hi_301.validator;

/**
 * 无规律变更。
 * 创建日期：(2004-5-17 14:56:15)
 * @author：Administrator
 */
import nc.vo.pub.lang.*;

public class OrderlessSubsetValidator extends AbstractValidator {
	/**
	 * 此处插入方法描述。 创建日期：(2004-5-18 9:12:48)
	 */
	public OrderlessSubsetValidator() {
		super();
	}

	/**
	 * validate 方法注解。
	 */
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records)
			throws Exception {

	}

	/**
	 * validate 方法注解。
	 */
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
			throws Exception {
		super.validate(record);
		UFDate begindate = null;

		Object obj = record.getAttributeValue("begindate");
		if (obj != null)
			begindate = new UFDate(obj.toString());
		try {
			// 检查结束日期
			UFDate enddate = null;
			Object obj2 = record.getAttributeValue("enddate");
			if (obj2 != null) {
				enddate = new UFDate(obj2.toString());
			}
			// 开始日期必须小于结束日期
			if (begindate != null && enddate != null)
				if (begindate.after(enddate))
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("600704",
									"UPP600704-000157")/* @res "开始日期不能晚于结束日期！" */);
		} catch (Exception ex) {
			rethrow(ex, "enddate");
		}
	}

	/**
	 * validate 方法注解。
	 */
	public void validate(nc.vo.pub.ExtendedAggregatedValueObject person)
			throws Exception {
	}
}
