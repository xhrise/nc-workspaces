package nc.ui.ehpta.pub.convert;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class ConvertFunc {

	private ConvertFunc() {
		
	}
	
	/**
	 * 功能 : 将字符串数组转换为字符串。
	 * 
	 * Author : river 
	 * 
	 * Create : 2012-08-20
	 * 
	 * @param strArray
	 * @return
	 * @throws Exception
	 */
	public static final String change(String[] strArray) throws Exception {
		
		String newStr = "";
		for(String str : strArray) {
			newStr += str + ",";
		}
		
		if(newStr.length() > 0)
			newStr = newStr.substring(0 , newStr.length() - 1);
		
		return newStr;
	}
	
	/**
	 * 功能 ： 将value的数据类型转换为对应Clz中的字段的类型。
	 * 
	 * Author : river 
	 * 
	 * Create : 2012-08-20
	 * 
	 * @param clz
	 * @param attr
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static final Object change(Class clz , String attr , Object value) throws Exception {

		if(clz.getField(attr).getType() == String.class)
			return value;
		
		else if(clz.getField(attr).getType() == UFBoolean.class)
			return new UFBoolean(value == null ? "N" : value.toString());
		
		else if(clz.getField(attr).getType() == UFDate.class)
			return value == null ? null : new UFDate(value.toString());
		
		else if(clz.getField(attr).getType() == UFDouble.class)
			return new UFDouble(value == null ? "0" : value.toString() , 2);
		
		return value;
	}
	
	public static final String getChinaNum(String number) {
		
		if(number == null || "".equals(number) || Double.valueOf(number) == 0)
			return "";
		
		String val = "";
		boolean check = false;
		for (int i = 0; i < number.length(); i++) {
			char splitChar = number.charAt(number.length() - i - 1);
			
			if(splitChar != '.') {
				Integer signnum = Integer.valueOf(String.valueOf(splitChar));
				
				if(signnum != 0)
					check = true;
				
				if(signnum != 0 || check)
					val += signnum;
			} else {
				val += splitChar;
				check = true;
			}
		}
		
		String[] numArray = val.split("\\.");
		String bigNum = "";
		
		String xiao = numArray[0];
		String zheng = numArray[1];
		
		for (int i = 0; i < xiao.length(); i++) {
			bigNum = getChinaSignNum(xiao.substring(i, i + 1)) + bigNum;
		}
		
		if(xiao.length() > 0 )
			bigNum = "点" + bigNum + "吨";
		else 
			bigNum = "吨整";
		
		String f = "";
		String x = "";
		for (int i = 1; i < zheng.length() + 1; i++) {
			x = zheng.substring(i - 1, i);
			int w = i % 8;
			if (i == 1) {
				if (x.equals("0") == false) {
					bigNum = getChinaSignNum(x) + bigNum;
				}
			} else {
				if (w == 1)
					f = "";
				if (w == 2)
					f = "拾";
				if (w == 3)
					f = "佰";
				if (w == 4)
					f = "仟";
				if (w == 5)
					f = "万";
				if (w == 6)
					f = "拾";
				if (w == 7)
					f = "佰";
				if (w == 0)
					f = "仟";
				if (w == 5) {
					if (zheng.length() - i > 3 && x.equals("0")
							&& (zheng.substring(i, i + 1)).equals("0")
							&& (zheng.substring(i + 1, i + 2)).equals("0")
							&& (zheng.substring(i + 2, i + 3)).equals("0")) {
					} else if (x.equals("0") == false) {
					} else
						bigNum = "万" + bigNum;
				}
				if (w == 1)
					bigNum = "亿" + bigNum;
				if (x.equals("0")
						&& (zheng.substring(i - 2, i - 1)).equals("0")
						|| x.equals("0") && w == 1 || x.equals("0") && w == 5) {
				} else {
					if (x.equals("0")) {
						bigNum = getChinaSignNum(x) + bigNum;
					} else {
						bigNum = getChinaSignNum(x) + f + bigNum;
					}
				}
			}
		}
		
		return bigNum;
	}
	
	public static final String getChinaSignNum(String number) {
		
		if (number.equals("0"))
			return "零";
		
		if (number.equals("1"))
			return "壹";
		
		if (number.equals("2"))
			return "贰";
		
		if (number.equals("3"))
			return "叁";
		
		if (number.equals("4"))
			return "肆";
		
		if (number.equals("5"))
			return "伍";
		
		if (number.equals("6"))
			return "陆";
		
		if (number.equals("7"))
			return "柒";
		
		if (number.equals("8"))
			return "捌";
		
		if (number.equals("9"))
			return "玖";
		
		return "";
		
	}
}
