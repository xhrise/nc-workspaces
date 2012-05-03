package nc.vo.hi.hi_401;

import java.util.*;

/**
 * 此处插入类型说明。
 * 创建日期：(2002-3-31 15:40:32)
 * @author：田海波
 */
public class ChgConvert {
/**
 * ChgConvert 构造子注解。
 */
public ChgConvert() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-3-31 15:40:54)
 */
public static PsnDataVO convertVO(SetchgVO chgvo, SetdictVO dict)
	throws Exception {

	PsnDataVO psnvo = null;
	try {
		SetchgHeaderVO header = (SetchgHeaderVO) chgvo.getParentVO();
		SetchgItemVO[] items = (SetchgItemVO[]) chgvo.getChildrenVO();
		java.util.Vector fieldNamesVec = new java.util.Vector();
		Vector fieldValuesVec = new Vector();

		psnvo = new PsnDataVO(dict);
		psnvo.setPkname("pk_psndoc");
		psnvo.setPksubname("pk_psndoc_sub");

		//for (int i = 0; i < items.length; i++) {
		//if (items[i].getFldcode().equalsIgnoreCase("execstatus")
		//|| items[i].getFldcode().equalsIgnoreCase("psnname")
		//|| items[i].getFldcode().equalsIgnoreCase("pk_psndoc")
		//|| items[i].getFldcode().equalsIgnoreCase("pk_psndoc_sub"))
		//continue;
		//else
		//fieldNamesVec.addElement(items[i].getFldcode());
		//}
		//if (fieldNamesVec.size() > 0) {
		//String[] fieldnames = new String[fieldNamesVec.size()];
		//fieldNamesVec.copyInto(fieldnames);
		//psnvo.setFieldNames(fieldnames);
		//psnvo.setFieldValues(new Object[fieldnames.length]);
		//}

		
		if (items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				psnvo.setAttributeValueByAlt(items[i].getFldcode(), items[i].getFldname());
			} ////
		}
		if (header != null) {
			psnvo.setPk_main(header.getPk_psndoc());
			psnvo.setAttributeValue("lastflag", "Y");
			psnvo.setAttributeValue("recordnum", new Integer(0));
			psnvo.setExecStatus(header.getExecstatus());
			psnvo.setTablename(header.getTablename());

		}

	} catch (Exception e) {
		e.printStackTrace();
		throw e;

	}
	return psnvo;

}
}
