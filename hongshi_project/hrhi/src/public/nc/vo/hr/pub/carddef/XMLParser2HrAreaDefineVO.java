package nc.vo.hr.pub.carddef;

import nc.vo.hr.tools.pub.StringUtils;

import org.w3c.dom.Element;

import com.ufsoft.report.sysplugin.xml.AbsXmlParser;
import com.ufsoft.report.sysplugin.xml.IXmlParser;

/**
 * XML转换方法类,HrCellDataVO
 * @author wangxing
 *
 */
public class XMLParser2HrAreaDefineVO extends AbsXmlParser {
	private static final String START_ROW = "startCellRow";
	private static final String START_COL = "startCellCol";
	private static final String END_ROW = "endCellRow";
	private static final String END_COL = "endCellCol";
	private static final String WIDTH = "areaWidth";
	private static final String HEIGHT = "areaHeight";
	// 条件数组长度属性
	private static final String SUB_CONDITION_LENGTH = "ItemConditionArrayLength";
	private static final String SUB_CONDITION_ITEM = "ItemConditionVO";
	
	private static final String SUB_LOCATION = "subLocation";
	private static final String SUB_LOCATIONORDER = "subSetLocationOrder";
	private static final String SUB_LOCATIONORDERCOUNT = "subSetLocationOrderCount";
	private static final String PSN_SET_CODE = "psnSetCode";
	
	// 字段编码数组
	private static final String ARRAY_LEN_FLDCODE = "FldCodeArrayLength";
	private static final String ARRAY_ITEM_FLDCODE = "FldCode";
	
	// 字段数据类型数组
	private static final String ARRAY_FLDDT = "FldDTArray";
	
	// 字段参照类型数组
	private static final String ARRAY_LEN_FLDREFTYPE = "FldRefTypeArrayLength";
	private static final String ARRAY_ITEM_FLDREFTYPE = "FldRefType";
	
	// 字段在Area中相对的列索引,第一列索引为0，数组
	private static final String ARRAY_FLDCOLIDX = "FldColIdxArray";
	
	private static final String INT_ARRAY_SEPERATOR = "::";
	/**
	 * 构造函数
	 *
	 */
	public XMLParser2HrAreaDefineVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 对象到XML方法
	 */
	protected void objectToXmlImpl(Element element, Object obj) {
		// TODO Auto-generated method stub
		HrAreaDefineVO vo = (HrAreaDefineVO)obj;
		element.setAttribute(START_ROW, ""+vo.getStartCellRow());
		element.setAttribute(START_COL, ""+vo.getStartCellCol());
		element.setAttribute(END_ROW, ""+vo.getEndCellRow());
		element.setAttribute(END_COL, ""+vo.getEndCellCol());
		element.setAttribute(WIDTH, ""+vo.getAreaWidth());
		element.setAttribute(HEIGHT, ""+vo.getAreaHeight());
		
		// 条件数组
		int len = vo.getSubSetLocationConditionVOs()==null?0:vo.getSubSetLocationConditionVOs().length;
		element.setAttribute(SUB_CONDITION_LENGTH, ""+len);
		if(len>0){
			Element tmpEle = null;
			IXmlParser xmlp = getManager().getXmlParser(ItemConditionVO.class);
			for(int i=0; i<len; i++){
				tmpEle = element.getOwnerDocument().createElement(SUB_CONDITION_ITEM+i);
				xmlp.objectToXml(tmpEle,vo.getSubSetLocationConditionVOs()[i]);
				element.appendChild(tmpEle);
			}//end for
		}//end if
		
		element.setAttribute(SUB_LOCATION, ""+vo.getSubLocation());
		element.setAttribute(SUB_LOCATIONORDER, ""+vo.getSubSetLocationOrder());
		element.setAttribute(SUB_LOCATIONORDERCOUNT, ""+vo.getSubSetLocationOrderCount());
		element.setAttribute(PSN_SET_CODE, vo.getPsnSetCode());
		
		// 字段编码数组
		len = vo.getPsnFldCodeArray()==null?0:vo.getPsnFldCodeArray().length;
		element.setAttribute(ARRAY_LEN_FLDCODE, ""+len);
		if(len>0){
			Element tmpEle = null;
			IXmlParser xmlp = getManager().getXmlParser(String.class);
			for(int i=0; i<len; i++){
				tmpEle = element.getOwnerDocument().createElement(ARRAY_ITEM_FLDCODE+i);
				xmlp.objectToXml(tmpEle,vo.getPsnFldCodeArray()[i]);
				element.appendChild(tmpEle);
			}//end for
		}//end if
		
		// 字段数据类型数组
		len = vo.getPsnFldDataTypeArray()==null?0:vo.getPsnFldDataTypeArray().length;
		if(len>0){
			StringBuffer tmpStr = new StringBuffer();
			for(int i=0; i<len; i++){
				tmpStr.append(vo.getPsnFldDataTypeArray()[i]);
				if(i<(len-1)){
					tmpStr.append(INT_ARRAY_SEPERATOR);
				}
			}//end for
			element.setAttribute(ARRAY_FLDDT, tmpStr.toString());
		}else{
			element.setAttribute(ARRAY_FLDDT, null);
		}//end if
		
		// 字段参照类型数组
		len = vo.getPsnFldRefTypeArray()==null?0:vo.getPsnFldRefTypeArray().length;
		element.setAttribute(ARRAY_LEN_FLDREFTYPE, ""+len);
		if(len>0){
			Element tmpEle = null;
			IXmlParser xmlp = getManager().getXmlParser(String.class);
			for(int i=0; i<len; i++){
				tmpEle = element.getOwnerDocument().createElement(ARRAY_ITEM_FLDREFTYPE+i);
				xmlp.objectToXml(tmpEle,vo.getPsnFldRefTypeArray()[i]);
				element.appendChild(tmpEle);
			}//end for
		}//end if
		
		// 字段在Area中相对的列索引,第一列索引为0 数组
		len = vo.getPsnFldColIdxArray()==null?0:vo.getPsnFldColIdxArray().length;
		if(len>0){
			StringBuffer tmpStr = new StringBuffer();
			for(int i=0; i<len; i++){
				tmpStr.append(vo.getPsnFldColIdxArray()[i]);
				if(i<(len-1)){
					tmpStr.append(INT_ARRAY_SEPERATOR);
				}
			}//end for
			element.setAttribute(ARRAY_FLDCOLIDX, tmpStr.toString());
		}else{
			element.setAttribute(ARRAY_FLDCOLIDX, null);
		}//end if
	}

	/**
	 * XML到对象的方法
	 */
	protected Object xmlToObjectImpl(Element element) {
		HrAreaDefineVO vo = new HrAreaDefineVO();
		vo.setStartCell(Integer.parseInt(element.getAttribute(START_ROW)), 
				Integer.parseInt(element.getAttribute(START_COL)));
		vo.setEndCell(Integer.parseInt(element.getAttribute(END_ROW)), 
				Integer.parseInt(element.getAttribute(END_COL)));
		vo.setAreaWidth(Integer.parseInt(element.getAttribute(WIDTH)));
		vo.setAreaHeight(Integer.parseInt(element.getAttribute(HEIGHT)));
		
		int len = Integer.parseInt(element.getAttribute(SUB_CONDITION_LENGTH));
		if(len>0){
			ItemConditionVO[] vos = new ItemConditionVO[len];
			IXmlParser xmlp = getManager().getXmlParser(ItemConditionVO.class);
			Element tmpEle = null;
			for(int i=0; i<len; i++){
				tmpEle = getChildByTagName(element,SUB_CONDITION_ITEM+i);
				vos[i] = (ItemConditionVO)xmlp.xmlToObject(tmpEle);
			}//end for
			vo.setSubSetLocationConditionVOs(vos);
		}
		
		vo.setSubLocation(Integer.parseInt(element.getAttribute(SUB_LOCATION)));
		vo.setSubSetLocationOrder(Integer.parseInt(element.getAttribute(SUB_LOCATIONORDER)));
		vo.setSubSetLocationOrderCount(Integer.parseInt(element.getAttribute(SUB_LOCATIONORDERCOUNT)));
		vo.setPsnSetCode(element.getAttribute(PSN_SET_CODE));
		
		// 数组
		len = Integer.parseInt(element.getAttribute(ARRAY_LEN_FLDCODE));
		if(len>0){
			String[] ary = new String[len];
			IXmlParser xmlp = getManager().getXmlParser(String.class);
			Element tmpEle = null;
			for(int i=0; i<len; i++){
				tmpEle = getChildByTagName(element,ARRAY_ITEM_FLDCODE+i);
				ary[i] = (String)xmlp.xmlToObject(tmpEle);
			}//end for
			vo.setPsnFldCodeArray(ary);
		}
		
		// 数组
		String s = element.getAttribute(ARRAY_FLDDT);
		if(StringUtils.hasText(s)){
			String[] ary = s.split(INT_ARRAY_SEPERATOR);
			int[] aryInt = new int[ary.length];
			for(int i=0 ;i<ary.length; i++){
				aryInt[i] = Integer.parseInt(ary[i]);
			}
			vo.setPsnFldDataTypeArray(aryInt);
		}
		
		// 数组
		len = Integer.parseInt(element.getAttribute(ARRAY_LEN_FLDREFTYPE));
		if(len>0){
			String[] ary = new String[len];
			IXmlParser xmlp = getManager().getXmlParser(String.class);
			Element tmpEle = null;
			for(int i=0; i<len; i++){
				tmpEle = getChildByTagName(element,ARRAY_ITEM_FLDREFTYPE+i);
				ary[i] = (String)xmlp.xmlToObject(tmpEle);
			}//end for
			vo.setPsnFldRefTypeArray(ary);
		}
		
		// 数组
		s = element.getAttribute(ARRAY_FLDCOLIDX);
		if(StringUtils.hasText(s)){
			String[] ary = s.split(INT_ARRAY_SEPERATOR);
			int[] aryInt = new int[ary.length];
			for(int i=0 ;i<ary.length; i++){
				aryInt[i] = Integer.parseInt(ary[i]);
			}
			vo.setPsnFldColIdxArray(aryInt);
		}
		
		return vo;
	}

	public String getSupportClassName() {
		// TODO Auto-generated method stub
		return HrAreaDefineVO.class.getName();
	}

}
