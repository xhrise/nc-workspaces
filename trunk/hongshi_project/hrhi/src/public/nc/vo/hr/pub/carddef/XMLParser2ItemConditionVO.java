package nc.vo.hr.pub.carddef;

import org.w3c.dom.Element;

import com.ufsoft.report.sysplugin.xml.AbsXmlParser;

/**
 * XML转换方法类,HrAreaDefineVO
 * @author wangxing
 *
 */
public class XMLParser2ItemConditionVO extends AbsXmlParser {

	// 对象的对应字段
	private static final String ITEM_CODE = "conditionItemCode";
	private static final String ITEM_NAME = "conditionItemName";
	private static final String ITEM_OPER = "relaOperation";
	private static final String ITEM_VALUE = "comparValue";
	private static final String ITEM_DATATYPE = "itemDataType";
	
	/**
	 * 构造函数
	 *
	 */
	public XMLParser2ItemConditionVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 对象到XML方法
	 */
	protected void objectToXmlImpl(Element element, Object obj) {
		ItemConditionVO vo = (ItemConditionVO)obj; 
		element.setAttribute(ITEM_CODE, vo.getConditionItemCode());
		element.setAttribute(ITEM_NAME, vo.getConditionItemName());
		element.setAttribute(ITEM_OPER, ""+vo.getRelaOperation());
		element.setAttribute(ITEM_VALUE, vo.getComparValue());
		element.setAttribute(ITEM_DATATYPE, ""+vo.getItemDataType());
	}

	/**
	 * XML到对象的方法
	 */
	protected Object xmlToObjectImpl(Element element) {
		ItemConditionVO vo = new ItemConditionVO();
		vo.setConditionItemCode(element.getAttribute(ITEM_CODE));
		vo.setConditionItemName(element.getAttribute(ITEM_NAME));
		vo.setRelaOperation(Integer.parseInt(element.getAttribute(ITEM_OPER)));
		vo.setComparValue(element.getAttribute(ITEM_VALUE));
		vo.setItemDataType(Integer.parseInt(element.getAttribute(ITEM_DATATYPE)));
		return vo;
	}

	public String getSupportClassName() {
		// TODO Auto-generated method stub
		return ItemConditionVO.class.getName();
	}

}
