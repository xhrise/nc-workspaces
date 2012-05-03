package nc.vo.hr.xmldatainout;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import nc.bs.logging.Logger;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import java.io.*;
/**
 * �˴���������˵����
 * �������ڣ�(2001-9-28 14:49:18)
 * @author���� ɭ
 */
public class XMLDocWriter {
	private java.io.PrintWriter out;
	private java.io.File outPutFile = null;
	private java.io.FileOutputStream outputStream;
	private DOMParser xmlParser = null;
	private int space_num_per_level=2;
/**
 * XMLDocWriter ������ע�⡣
 */
public XMLDocWriter() {
	super();
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @exception �쳣����
 * @see            ��Ҫ�μ�����������
 * �������ڣ�(2001-9-28 14:50:16)
 * @author���� ɭ
 *  
 * 
 * @param outFile java.lang.String
 */
public XMLDocWriter(String outFile) 
{
	try 
	{
		if(outFile!=null && outFile.trim().length()>0)
		{
			setOutPutFile(new java.io.File(outFile));
			setOutputStream(new FileOutputStream(getOutPutFile()));
			//out = new PrintWriter(new FileOutputStream(outFile));
			setPrintWriter(new PrintWriter(getOutputStream()));
		}
	} 
	catch (Exception e) 
	{
		e.printStackTrace(System.err);
	}
}
/**
 * �رա�
 * �������ڣ�(2003-01-14 11:49:40)
 */
public void close() 
{
	try
	{
		if(getPrintWriter()!=null)
		{
			getPrintWriter().close();
		}
		if(getOutputStream()!=null)
		{
			getOutputStream().close();
		}
	}
	catch(Exception e)
	{
	}
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @exception �쳣����
 * @see            ��Ҫ�μ�����������
 * �������ڣ�(2001-9-28 16:48:15)
 * @author���� ɭ
 *  
 * 
 * @return org.w3c.dom.Attr[]
 * @param attrs org.w3c.dom.NamedNodeMap
 */
private Attr[] getAttrArray(NamedNodeMap attrs) {
	int len=(attrs!=null)?attrs.getLength():0;
	Attr array[]=new Attr[len];
	for(int i=0;i<len; i++){
		array[i]=(Attr)attrs.item(i);
		}
	return array;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-14 10:24:48)
 * @return java.io.File
 */
public java.io.File getOutPutFile() {
	return outPutFile;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-14 10:25:18)
 * @return java.io.FileOutputStream
 */
public java.io.FileOutputStream getOutputStream() {
	return outputStream;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-14 9:36:56)
 * @return java.io.PrintWriter
 */
public PrintWriter getPrintWriter() {
	return out;
}
/**
 * ����¿սڵ㣬���������µ�Document�ĵ�
 * �������ڣ�(2001-11-15 14:26:33)
 * @return org.w3c.dom.Node
 * @param spaces int
 */
private String getSpaceNode(int nodeLevel) {
	String str = "\r\n";
	
	for (int i = 0; i <nodeLevel*space_num_per_level; i++) 
	{
		str += " ";
	}
	
	return str;
}
/**
 * �õ�ָ�����Ƶ�xml�ĵ���
 * �������ڣ�(2001-11-14 15:36:57)
 * @return java.lang.String
 */
public Document getXmlDocument(String fileName) /*throws TranslatingException*/
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
		//throw new TranslatingException("ȱ��"+bodyFile+"У���ļ�");
		//return null;
	}
	catch (Exception ex)
	{
		ex.printStackTrace();
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
 * �������ڣ�(2003-01-14 10:24:48)
 * @param newOutPutFile java.io.File
 */
public void setOutPutFile(java.io.File newOutPutFile) {
	outPutFile = newOutPutFile;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-14 10:25:18)
 * @param newOutputStream java.io.FileOutputStream
 */
public void setOutputStream(java.io.FileOutputStream newOutputStream) {
	outputStream = newOutputStream;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-14 9:36:56)
 * @return java.io.PrintWriter
 */
public void setPrintWriter(PrintWriter newPrintWriter) {
	out=newPrintWriter;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-14 14:22:08)
 * @param newXmlParser com.ibm.xml.parsers.DOMParser
 */
public void setXmlParser(DOMParser newXmlParser) {
	xmlParser = newXmlParser;
}
///**
// * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
// * @exception �쳣����
// * @see            ��Ҫ�μ�����������
// * �������ڣ�(2001-9-29 14:05:33)
// * @author���� ɭ
// *  
// * 
// * @return java.lang.String
// * @param s java.lang.String
// */
//private String strToXML(String s) 
//{
//	StringBuffer str = new StringBuffer();
//
//	int len = (s == null) ? s.length() : 0;
//	for (int i = 0; i < len; i++) 
//	{
//		char ch = s.charAt(i);
//		switch (ch) {
//			case '<' :
//				{
//					str.append("&lt");
//					break;
//				}
//			case '>' :
//				{
//					str.append("&gt");
//					break;
//				}
//			case '&' :
//				{
//					str.append("&amp");
//					break;
//				}
//			case '"' :
//				{
//					str.append("&quot");
//					break;
//				}
//			case '\'' :
//				{
//					str.append("&apos");
//					break;
//				}
//			default :
//				{
//					str.append(ch);
//				}
//		}
//	}
//	return str.toString();
//}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @exception �쳣����
 * @see            ��Ҫ�μ�����������
 * �������ڣ�(2001-9-28 14:57:56)
 * @author���� ɭ
 *  
 * 
 * @param node org.w3c.dom.Node
 */
public String writeNode(Node node,int nodeLevel) 
{
	String line="";
	if (node == null) 
	{
		System.err.println("Nothing to do.node is null");
		return null;
	}
	int type = node.getNodeType();

	switch (type) 
	{
		case Node.DOCUMENT_NODE :
			{
				line+="<?xml version=\"1.0\" encoding=\"GB2312\" ?>\r\n";
				line+=writeNode(((Document) node).getDocumentElement(),nodeLevel);
				break;
			}
		case Node.ELEMENT_NODE :
			{
				line+=getSpaceNode(nodeLevel);
				line+='<';
				line+=node.getNodeName();

				Attr attrs[] = getAttrArray(node.getAttributes());

				for (int i = 0; i < attrs.length; i++) {
					Attr attr = attrs[i];
					line+=' ';//��ӡһ���ո�
					line+=attr.getNodeName();
					line+="=\"";
					line+=attr.getNodeValue();
					line+="\"";
				}
				line+=">";

				NodeList children = node.getChildNodes();
				if (children != null) 
				{
					int len = children.getLength();
					for (int i = 0; i < len; i++) 
					{
						line+=writeNode(children.item(i),nodeLevel+1);
					}
				}
				break;
			}
		case Node.ENTITY_REFERENCE_NODE :
			{
				
				line+='&';
				
				line+=node.getNodeName();
				
				line+=';';
				break;
			}
		case Node.PROCESSING_INSTRUCTION_NODE :
			{
				line+="\r\n";
				line+="<?";
				
				line+=node.getNodeName();
				String data = node.getNodeValue();
				if (data != null && data.length() > 0) 
				{
					
					line+=' ';
					
					line+=data;
				}
				
				line+="?>";
				break;
			}
		case Node.CDATA_SECTION_NODE :
		case Node.TEXT_NODE :
			{
				
				line+=node.getNodeValue();
				break;
			}

	}
	if (type == Node.ELEMENT_NODE) {
		
		line+="</";
		
		line+=node.getNodeName();
		
		line+=">";
		if(node.getNextSibling()==null)
		{
			line+=getSpaceNode(nodeLevel-1);
		}
	}
	
	return line;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * @exception �쳣����
 * @see            ��Ҫ�μ�����������
 * �������ڣ�(2001-9-28 14:55:29)
 * @author���� ɭ
 *  
 * 
 * @param param org.w3c.dom.Document
 * @param document org.w3c.dom.Document
 */
public void writeXmlFile(Document document) throws Exception
{
	String docContent=null;
	try 
	{
		int maxLength=5000;
		docContent=writeNode(document,0);
		if(docContent!=null)
		{
			while(docContent.length()>maxLength)
			{
				String line=docContent.substring(0,maxLength);
				out.print(line);
				out.flush();
				docContent=docContent.substring(maxLength);
			}
			out.print(docContent);
			out.flush();
		}
	} 
	catch (Exception e) 
	{
		e.printStackTrace(System.err);
	}
	finally 
	{
		close();
	}
}
}
