package nc.vo.hi.hi_301.validator;

/**
 * �޹��ɱ����
 * �������ڣ�(2004-5-17 14:56:15)
 * @author��Administrator
 */
import nc.vo.pub.lang.*;

public class OrderlessSubsetValidator extends AbstractValidator {
	/**
	 * �˴����뷽�������� �������ڣ�(2004-5-18 9:12:48)
	 */
	public OrderlessSubsetValidator() {
		super();
	}

	/**
	 * validate ����ע�⡣
	 */
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records)
			throws Exception {

	}

	/**
	 * validate ����ע�⡣
	 */
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
			throws Exception {
		super.validate(record);
		UFDate begindate = null;

		Object obj = record.getAttributeValue("begindate");
		if (obj != null)
			begindate = new UFDate(obj.toString());
		try {
			// ����������
			UFDate enddate = null;
			Object obj2 = record.getAttributeValue("enddate");
			if (obj2 != null) {
				enddate = new UFDate(obj2.toString());
			}
			// ��ʼ���ڱ���С�ڽ�������
			if (begindate != null && enddate != null)
				if (begindate.after(enddate))
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("600704",
									"UPP600704-000157")/* @res "��ʼ���ڲ������ڽ������ڣ�" */);
		} catch (Exception ex) {
			rethrow(ex, "enddate");
		}
	}

	/**
	 * validate ����ע�⡣
	 */
	public void validate(nc.vo.pub.ExtendedAggregatedValueObject person)
			throws Exception {
	}
}
