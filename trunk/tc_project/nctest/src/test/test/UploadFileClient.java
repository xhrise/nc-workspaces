package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.vo.jcom.xml.XMLUtil;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory;
import org.xml.sax.SAXException;

/**
 * <b>function:</b>上传文件WebService客户端
 * 
 * @author hoojo
 * @createDate Dec 18, 2010 1:38:14 PM
 * @file UploadFileClient.java
 * @package com.hoo.client
 * @project AxisWebService
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class UploadFileClient {
	public static void main(String[] args) throws ServiceException,
			RemoteException {
		new UploadFileClient().upload(null, null);
	}
	
	public String upload(DataHandler handlers, String fileNames) {
		String fileName = "030507_excel.xls";
		DataHandler handler = new DataHandler(new FileDataSource("E:/NCSOFT/NC56/NCHOME/Ufida_IUFO/shangbao/"+fileName));
		if (fileName != null && !"".equals(fileName)) {
			File file = new File(fileName);

			String ncFileName = "C:/Ufida_IUFO/shangbaojieshou/";
			File myFilePath = new File(ncFileName);
			if (!(myFilePath.exists())) {
				myFilePath.mkdirs();
			}

			if (handler != null) {
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					is = handler.getInputStream();
					fos = new FileOutputStream(ncFileName + file);
					byte[] buff = new byte[1024 * 8];
					int len = 0;
					while ((len = is.read(buff)) > 0) {
						fos.write(buff, 0, len);
					}
					
					createXMLList("" + file, ncFileName + file);
				} catch (FileNotFoundException e) {
					return "fileNotFound - " + fileName;
				} catch (Exception e) {
					e.printStackTrace();
					return "upload File failure";
				} finally {
					try {
						if (fos != null) {
							fos.flush();
							fos.close();
						}
						if (is != null) {
							is.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return "file absolute path:" + ncFileName + file;
			} else {
				return "handler is null";
			}
		} else {
			return "fileName is null";
		}
	}
	
	/**
	 * 创建XMLLIST.XML 保存上报接收到文件的列表
	 * add by yh for 2011年3月14日16:51:07
	 * @param fileName
	 * @param newFile
	 * @return
	 */
	private String createXMLList(String fileName, String newFile) {
		File xmlList = new File("C:/Ufida_IUFO/shangbaojieshou/xmlList.xml");
		// 如果xmlList.xml不存在则创建
		Boolean bl = xmlList.exists();
		if (!bl) {
			try {
				org.w3c.dom.Document document = XMLUtil.newDocument();
				org.w3c.dom.Element element_main = document
						.createElement("main");
				org.w3c.dom.Element element_all = document
						.createElement("list");
				org.w3c.dom.Element efileName = document
						.createElement("fileName");
				efileName.appendChild(document.createTextNode(fileName));
				element_all.appendChild(efileName);
				org.w3c.dom.Element efile = document.createElement("file");
				// by fdh newfile.getPath()
				efile.appendChild(document.createTextNode(newFile));
				element_all.appendChild(efile);
				org.w3c.dom.Element etype = document.createElement("type");
				etype.appendChild(document.createTextNode("0"));
				element_all.appendChild(etype);
				org.w3c.dom.Element eID = document.createElement("ID");
				eID.appendChild(document.createTextNode("0"));
				element_all.appendChild(eID);
				element_main.appendChild(element_all);
				document.appendChild(element_main);
				toSave(document, xmlList.getPath());
			} catch (Exception e) {
				return "构建XmlList.xml错误";
			}
		} else { // 如果存在则读取并写入
			org.w3c.dom.Document document = null;
			try {
				document = XMLUtil.getDocumentBuilder().parse(xmlList);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			org.w3c.dom.NodeList lists = document.getElementsByTagName("list");

			for (int s = 0; s < lists.getLength(); s++) {
				String xmlfileName = document.getElementsByTagName("fileName")
						.item(s).getFirstChild().getNodeValue();
				String xmltype = document.getElementsByTagName("type").item(s)
						.getFirstChild().getNodeValue();
				String xmlID = document.getElementsByTagName("ID").item(s)
						.getFirstChild().getNodeValue();
				if (xmlfileName.equals(fileName) || xmlfileName == fileName) {
					// 如果已经传过去，还没导入，又下发了一次的情况
					if (xmltype.equals("0") || xmltype == "0") {
						document.getElementsByTagName("file").item(s)
								.getFirstChild().setNodeValue(newFile);
						toSave(document, xmlList.getPath());
						return "出现已经传过去，还没导入，又下发了一次的情况";
					}
				}

				if (xmltype.equals("1")) {
					document.getElementsByTagName("ID").item(s).getFirstChild()
							.setNodeValue(
									document.getElementsByTagName("fileName")
											.item(s).getFirstChild()
											.getNodeValue());

					document.getElementsByTagName("fileName").item(s)
							.getFirstChild().setNodeValue(fileName);

					document.getElementsByTagName("type").item(s)
							.getFirstChild().setNodeValue("0");
				} 
			}
			org.w3c.dom.Element element_main = document
			.getDocumentElement();
			org.w3c.dom.Element element_all = document
					.createElement("list");
		
			org.w3c.dom.Element efileName = document
					.createElement("fileName");
			efileName.appendChild(document.createTextNode(fileName));
			element_all.appendChild(efileName);
		
			org.w3c.dom.Element efile = document.createElement("file");
			// by fdh newfile.getPath()
			efile.appendChild(document.createTextNode(newFile));
			element_all.appendChild(efile);
			org.w3c.dom.Element etype = document.createElement("type");
			etype.appendChild(document.createTextNode("0"));
			element_all.appendChild(etype);
			org.w3c.dom.Element eID = document.createElement("ID");
			eID.appendChild(document.createTextNode("0"));
			element_all.appendChild(eID);
			element_main.appendChild(element_all);
			toSave(document, xmlList.getPath());
		}
		return null;
	}

	/**
	 * 保存XMLLIST.XML的修改与其他操作
	 * add by yh for 2011年3月14日16:56:14
	 * @param document
	 * @param filename
	 */
	public void toSave(org.w3c.dom.Document document, String filename) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerException mye) {
			mye.printStackTrace();
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}
}
