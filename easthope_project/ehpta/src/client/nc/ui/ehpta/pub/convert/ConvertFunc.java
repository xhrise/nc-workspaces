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
}
