package nc.vo.hr.xmldatainout;

//import java.io.FileOutputStream;
//import java.io.PrintWriter;
import nc.bs.logging.Logger;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import java.io.*;
/**
 * �˴���������˵����
 * �������ڣ�(2001-9-28 14:49:18)
 * @author���� ɭ
 */
public class XMLDocReader {
	private DOMParser xmlParser = null;
/**
 * XMLDocWriter ������ע�⡣
 */
public XMLDocReader() {
	super();
}
/**
 * �õ�ָ�����Ƶ�xml�ĵ���
 * �������ڣ�(2001-11-14 15:36:57)
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
		//throw new TranslatingException("ȱ��"+bodyFile+"У���ļ�");
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
 * �˴����뷽��������
 * �������ڣ�(2003-01-14 14:22:08)
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
 * �˴����뷽��������
 * �������ڣ�(2003-01-14 14:22:08)
 * @param newXmlParser com.ibm.xml.parsers.DOMParser
 */
public void setXmlParser(DOMParser newXmlParser) {
	xmlParser = newXmlParser;
}
}
