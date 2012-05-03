package nc.vo.hr.pub.carddef;

import org.w3c.dom.Element;

import com.ufsoft.report.sysplugin.xml.AbsXmlParser;
import com.ufsoft.report.sysplugin.xml.IXmlParser;

/**
 * XML转换方法类,HrCellDataVO
 * @author wangxing
 *
 */
public class XMLParser2HrCellDataVO extends AbsXmlParser {
	// 对象的对应属性
	private static final String CELLBELONG = "cellBelongTo";
	private static final String ITEM_TYPE = "itemType";
	private static final String PERSON_TYPE = "personType";
	private static final String ITEM_KEY = "itemKey";
	private static final String ITEM_NAME = "itemName";
	private static final String ITEM_TABLE = "itemTable";
	private static final String ITEM_DATATYPE = "itemDataType";
	private static final String DATEFORMAT_ID = "dateFormatID";
	private static final String DATEFORMAT_NAME = "dateFormatName";
	private static final String DATEFORMAT_PREFIX = "dateFormatPrefix";
	private static final String DATEFUNC_ID = "dateFuncID";
	private static final String DATEFUNC_NAME = "dateFuncName";
	private static final String SUB_LOCATION = "subLocation";
	private static final String SUB_LOCATIONORDER = "subSetLocationOrder";
	private static final String SUB_LOCATIONORDERCOUNT = "subSetLocationOrderCount";
	// 条件数组长度属性
	private static final String SUB_CONDITION_LENGTH = "ItemConditionArrayLength";
	private static final String SUB_CONDITION_ITEM = "ItemConditionVO";
	
	private static final String ITEM_REFTYPE = "refType";
	private static final String ITEM_CELLROW = "cellRow";
	private static final String ITEM_CELLCOL = "cellCol";
	
	// CELL VALUE不保存,也不复制
//	private static final String ITEM_CELLVALUE = "ItemConditionVO";
	/**
	 * 构造函数
	 */
	public XMLParser2HrCellDataVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 对象到XML方法
	 */
	protected void objectToXmlImpl(Element element, Object obj) {
		HrCellDataVO vo = (HrCellDataVO)obj;
		element.setAttribute(CELLBELONG, ""+vo.getCellBelongTo());
		element.setAttribute(ITEM_TYPE, ""+vo.getItemType());
		element.setAttribute(PERSON_TYPE, ""+vo.getPersonType());
		element.setAttribute(ITEM_KEY, vo.getItemKey());
		element.setAttribute(ITEM_NAME, vo.getItemName());
		element.setAttribute(ITEM_TABLE, vo.getItemTable());
		element.setAttribute(ITEM_DATATYPE, ""+vo.getItemDataType());
		element.setAttribute(DATEFORMAT_ID, ""+vo.getDateFormatID());
		element.setAttribute(DATEFORMAT_NAME, vo.getDateFormatName());
		element.setAttribute(DATEFORMAT_PREFIX, vo.getDateFormatPrefix());
		element.setAttribute(DATEFUNC_ID, ""+vo.getDateFuncID());
		element.setAttribute(DATEFUNC_NAME, vo.getDateFuncName());
		element.setAttribute(SUB_LOCATION, ""+vo.getSubLocation());
		element.setAttribute(SUB_LOCATIONORDER, ""+vo.getSubSetLocationOrder());
		element.setAttribute(SUB_LOCATIONORDERCOUNT, ""+vo.getSubSetLocationOrderCount());
		
		int len = vo.getSubSetLocationCondition()==null?0:vo.getSubSetLocationCondition().length;
		element.setAttribute(SUB_CONDITION_LENGTH, ""+len);
		if(len>0){
			Element tmpEle = null;
			IXmlParser xmlp = getManager().getXmlParser(ItemConditionVO.class);
			for(int i=0; i<len; i++){
				tmpEle = element.getOwnerDocument().createElement(SUB_CONDITION_ITEM+i);
				xmlp.objectToXml(tmpEle,vo.getSubSetLocationCondition()[i]);
				element.appendChild(tmpEle);
			}//end for
		}
		element.setAttribute(ITEM_REFTYPE, vo.getRefType());
		element.setAttribute(ITEM_CELLROW, ""+vo.getCellRow());
		element.setAttribute(ITEM_CELLCOL, ""+vo.getCellCol());
	}

	/**
	 * XML到对象的方法
	 */
	protected Object xmlToObjectImpl(Element element) {
		HrCellDataVO vo = new HrCellDataVO();
		vo.setCellBelongTo(Integer.parseInt(element.getAttribute(CELLBELONG)));
		vo.setItemType(Integer.parseInt(element.getAttribute(ITEM_TYPE)));
		vo.setPersonType(Integer.parseInt(element.getAttribute(PERSON_TYPE)));
		vo.setItemKey(element.getAttribute(ITEM_KEY));
		vo.setItemName(element.getAttribute(ITEM_NAME));
		vo.setItemTable(element.getAttribute(ITEM_TABLE));
		vo.setItemDataType(Integer.parseInt(element.getAttribute(ITEM_DATATYPE)));
		vo.setDateFormatID(Integer.parseInt(element.getAttribute(DATEFORMAT_ID)));
		vo.setDateFormatName(element.getAttribute(DATEFORMAT_NAME));
		vo.setDateFormatPrefix(element.getAttribute(DATEFORMAT_PREFIX));
		vo.setDateFuncID(Integer.parseInt(element.getAttribute(DATEFUNC_ID)));
		vo.setDateFuncName(element.getAttribute(DATEFUNC_NAME));
		vo.setSubLocation(Integer.parseInt(element.getAttribute(SUB_LOCATION)));
		vo.setSubSetLocationOrder(Integer.parseInt(element.getAttribute(SUB_LOCATIONORDER)));
		vo.setSubSetLocationOrderCount(Integer.parseInt(element.getAttribute(SUB_LOCATIONORDERCOUNT)));
		int len = Integer.parseInt(element.getAttribute(SUB_CONDITION_LENGTH));
		if(len>0){
			ItemConditionVO[] vos = new ItemConditionVO[len];
			IXmlParser xmlp = getManager().getXmlParser(ItemConditionVO.class);
			Element tmpEle = null;
			for(int i=0; i<len; i++){
				tmpEle = getChildByTagName(element,SUB_CONDITION_ITEM+i);
				vos[i] = (ItemConditionVO)xmlp.xmlToObject(tmpEle);
			}//end for
			vo.setSubSetLocationCondition(vos);
		}
		vo.setRefType(element.getAttribute(ITEM_REFTYPE));
		vo.setCellRow(Integer.parseInt(element.getAttribute(ITEM_CELLROW)));
		vo.setCellCol(Integer.parseInt(element.getAttribute(ITEM_CELLCOL)));
		
		return vo;
	}

	public String getSupportClassName() {
		// TODO Auto-generated method stub
		return HrCellDataVO.class.getName();
	}

}
