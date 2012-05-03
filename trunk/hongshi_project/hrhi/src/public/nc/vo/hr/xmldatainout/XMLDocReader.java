package nc.vo.hr.xmldatainout;

//import java.io.FileOutputStream;
//import java.io.PrintWriter;
import nc.bs.logging.Logger;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import java.io.*;
/**
 * 此处插入类型说明。
 * 创建日期：(2001-9-28 14:49:18)
 * @author：张 森
 */
public class XMLDocReader {
	private DOMParser xmlParser = null;
/**
 * XMLDocWriter 构造子注解。
 */
public XMLDocReader() {
	super();
}
/**
 * 得到指定名称的xml文档。
 * 创建日期：(2001-11-14 15:36:57)
 * @return java.lang.String
 */
public Document getXmlDocument(String fileName) throws Exception
{
	Document doc = null;
	try
	{
		getXmlParser().parse(fileName);
		doc = getXmlParser().getDocument();
	}
	catch (FileNotFoundException fex)
	{
		Logger.error(fex.toString());
		throw fex;
		//throw new TranslatingException("缺少"+bodyFile+"校验文件");
		//return null;
	}
	catch (Exception ex)
	{
		ex.printStackTrace();
		throw ex;
	}
	return doc;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-01-14 14:22:08)
 * @return com.ibm.xml.parsers.DOMParser
 */
public DOMParser getXmlParser() 
{
	if(xmlParser==null)
	{
		xmlParser = new DOMParser();
	}
	return xmlParser;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-01-14 14:22:08)
 * @param newXmlParser com.ibm.xml.parsers.DOMParser
 */
public void setXmlParser(DOMParser newXmlParser) {
	xmlParser = newXmlParser;
}
}
