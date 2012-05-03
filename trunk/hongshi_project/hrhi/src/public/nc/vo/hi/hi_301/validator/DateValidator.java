package nc.vo.hi.hi_301.validator;

/**
 * �˴���������������
 * �������ڣ�(2004-5-14 19:58:24)
 * @author��Administrator
 */
public class DateValidator implements IFieldValidator {
	private static int[] month_days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-14 20:13:35)
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
 * �˴����뷽��������
 * �������ڣ�(2004-5-14 20:12:12)
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
 * validate ����ע�⡣
 */
public void validate(Object o) throws Exception {
	if(o==null)
	return;
	String date=o.toString().trim();
	if(date.length()==0)
	return;
	if(date.length()!=10)
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000134")/*@res "��ʽӦ��Ϊ��YYYY-MM-DD"*/);
	String year=date.substring(0,4);
	int y=1977;
	try{
		y=Integer.parseInt(year);
	}catch(NumberFormatException nfe){
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000135")/*@res "��ʽӦ��Ϊ��YYYY-MM-DD�����Ӧ��Ϊ��λ����"*/);
	}
	String sub=date.substring(4,5);
	if(!sub.equals("-"))
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000136")/*@res "��ʽӦ��Ϊ��YYYY-MM-DD������λӦ���Ǽ���"*/);
	String month=date.substring(5,7);
	int m=4;
	try{
		m=Integer.parseInt(month);
		if(m<1||m>12)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000137")/*@res "�·���Ӧ�ý���1-12֮��"*/);
	}catch(NumberFormatException nfe){
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000138")/*@res "��ʽӦ��Ϊ��YYYY-MM-DD���·�Ӧ��Ϊ��λ����"*/);
	}
	sub=date.substring(7,8);
	if(!sub.equals("-"))
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000139")/*@res "��ʽӦ��Ϊ��YYYY-MM-DD������λӦ���Ǽ���"*/);
	String day=date.substring(8,10);
	int d=27;
	try{
		d=Integer.parseInt(day);
		int md=getMonthDayCount(y,m);
		if(d<1||d>md)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000093")/*@res "��"*/+y+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000140")/*@res "���"*/+m+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000141")/*@res "��Ӧ�ý���1-"*/+md+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000142")/*@res "֮��"*/);
	}catch(Exception e){
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000143")/*@res "��ʽӦ��Ϊ��YYYY-MM-DD������Ӧ��Ϊ��λ����"*/);
	}
}
}
