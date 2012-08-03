package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class PeriodRefPane extends AbstractRefModel {

	public PeriodRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("会计期间");
		setRefTitle("会计期间");
		setFieldCode(new String[] {
			"month",
			"period",
			"begindate"
		});
		
		setFieldName(new String[] {
			"会计月份",
			"会计期间",
			"开始日期"
		});
		
		setPkFieldCode("period");
		
		setHiddenFieldCode(new String[] {
			"pk_accperiodmonth"
		});

		setRefCodeField("month");
		
		setRefNameField("period");
		
		setTableName(" (select fmonth.month , fyear.periodyear || '-' || fmonth.month period , fmonth.begindate , fmonth.pk_accperiodmonth  from bd_accperiodmonth fmonth left join bd_accperiod fyear on fyear.pk_accperiod = fmonth.pk_accperiod where fmonth.begindate is not null and fmonth.enddate is not null) ");
		
		setWherePart(" 1 = 1 ");
		
		setOrderPart(" begindate asc ");
		
	}
}
