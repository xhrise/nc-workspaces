package nc.ui.eh.tools;

public class TypeUntil {

	
	/**
	 * 说明：将模板里的数据类型转化为单据项目里的类型
	 * @param templtetype
	 * @return
	 *  在模板里数据类型      字符 0 ，参照 5，下拉　6 ,大文本 9，整型 1 ，小数 2 日期 3 ，逻辑 4
	 *  在单据项目里的数据类型 字符 0 ，浮点型 1， 日期 2 ，整型 3 ，ts 4， 布尔型 5
	 */
	public static int changeDatatype(int templtetype){
		int itemtype = 0;
		switch (templtetype) {
		case 1:
			itemtype = 3;
			break;
		case 2:
			itemtype = 1;
			break;
		case 3:
			itemtype = 2;
			break;
		case 4:
			itemtype = 5;
			break;
		}
		return itemtype;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(changeDatatype(4));
	}

	
}
