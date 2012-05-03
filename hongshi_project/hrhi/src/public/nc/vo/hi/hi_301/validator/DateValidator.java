package nc.vo.hi.hi_301.validator;

/**
 * 此处插入类型描述。
 * 创建日期：(2004-5-14 19:58:24)
 * @author：Administrator
 */
public class DateValidator implements IFieldValidator {
	private static int[] month_days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-14 20:13:35)
 * @return int
 * @param year int
 * @param month int
 */
private int getMonthDayCount(int year, int month) {
	if (month == 2)
		if (leapYear(year))
			return 29;
		else
			return 28;
	return month_days[month - 1];
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-14 20:12:12)
 * @return boolean
 * @param year int
 */
private boolean leapYear(int year) {
	if(year%100==0&&year%400==0){
		return true;
	}else{
		return (year%4==0);
	}
}
/**
 * validate 方法注解。
 */
public void validate(Object o) throws Exception {
	if(o==null)
	return;
	String date=o.toString().trim();
	if(date.length()==0)
	return;
	if(date.length()!=10)
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000134")/*@res "格式应该为：YYYY-MM-DD"*/);
	String year=date.substring(0,4);
	int y=1977;
	try{
		y=Integer.parseInt(year);
	}catch(NumberFormatException nfe){
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000135")/*@res "格式应该为：YYYY-MM-DD，年份应该为四位数字"*/);
	}
	String sub=date.substring(4,5);
	if(!sub.equals("-"))
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000136")/*@res "格式应该为：YYYY-MM-DD，第五位应该是减号"*/);
	String month=date.substring(5,7);
	int m=4;
	try{
		m=Integer.parseInt(month);
		if(m<1||m>12)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000137")/*@res "月份是应该介于1-12之间"*/);
	}catch(NumberFormatException nfe){
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000138")/*@res "格式应该为：YYYY-MM-DD，月份应该为两位数字"*/);
	}
	sub=date.substring(7,8);
	if(!sub.equals("-"))
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000139")/*@res "格式应该为：YYYY-MM-DD，第七位应该是减号"*/);
	String day=date.substring(8,10);
	int d=27;
	try{
		d=Integer.parseInt(day);
		int md=getMonthDayCount(y,m);
		if(d<1||d>md)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000093")/*@res "："*/+y+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000140")/*@res "年的"*/+m+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000141")/*@res "月应该介于1-"*/+md+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000142")/*@res "之间"*/);
	}catch(Exception e){
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000143")/*@res "格式应该为：YYYY-MM-DD，日期应该为两位数字"*/);
	}
}
}
