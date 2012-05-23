package nc.axis.web.service;





import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.io.Reader;

import java.io.Serializable;

import java.io.StringReader;



import javax.activation.DataHandler;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;

import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerException;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;



import org.dom4j.Document;

import org.dom4j.io.SAXReader;

import org.xml.sax.InputSource;

import java.security.MessageDigest;



/**

 * IUFO502上的WEBSERVICE服务器端类，需要放到NCHOME\hotwebs\axis下，改名为*.jws

 * add by yh for 2011年3月14日17:02:03

 * @author Administrator

 *

 */

public class UploadFileService  implements Serializable{

	public String upload(DataHandler handler, String fileName , String date) {

		if (fileName != null && !"".equals(fileName)) {

			//try {

			//	sun.misc.BASE64Decoder decode = new sun.misc.BASE64Decoder();

			//	fileName = new String(decode.decodeBuffer(fileName));

				
			//} catch (Exception e1) {

			//	e1.printStackTrace();

			//}

			//MessageDigest md5 = null;
			//try {
			//	md5 = MessageDigest.getInstance("MD5");
			//} catch (Exception e) {
			//	AppDebug.debug(e.getMessage());
			//}
			
			//fileName = new String(md5.digest(fileName.getBytes())) + ".xls";	
			
			System.out.println ("\n\n\nfileName is : " + fileName +"\n\n\n");

			try {
			if(fileName.split("/").length > 1) 
				fileName = fileName.split("/")[1];
				System.out.println ("\n\n\n Format fileName is : " + fileName +"\n\n\n");

			} catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
			
			fileName += ".xls";		

			File file = new File(fileName);



			String ncFileName = "Ufida_IUFO/shangbaojieshou/" + date + "/";

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



					System.out.println("接收到的文件位置：" + new File(ncFileName + fileName).getAbsolutePath());

					

					createXMLList("" + fileName, ncFileName + fileName);

				} catch (FileNotFoundException e) {

					return "fileNotFound - " + fileName;

				} catch (Exception e) {

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

						return "create file error";

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

		File xmlList = new File("Ufida_IUFO/shangbaojieshou/xmlList.xml");

		// 如果xmlList.xml不存在则创建

		Boolean bl = xmlList.exists();

		if (!bl) {

			try {

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				DocumentBuilder builder = factory.newDocumentBuilder();

				

				org.w3c.dom.Document document = builder.newDocument();

				

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

				SAXReader readers = new SAXReader(); 

				FileInputStream in = new FileInputStream(xmlList);  

				Reader read = new InputStreamReader(in); 

				Document doc = readers.read(read);

				

				StringReader   reader   =   new  StringReader(doc.asXML());  

		        	InputSource   source   =   new   InputSource(reader);  

				

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				DocumentBuilder builder = factory.newDocumentBuilder();

				

				document =builder.parse(source);

			} catch (Exception e) {

				// TODO Auto-generated catch block

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

			transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			PrintWriter pw = new PrintWriter(new FileOutputStream(filename));

			StreamResult result = new StreamResult(pw);

			transformer.transform(source, result);



			pw.close();

		} catch (TransformerException mye) {

			mye.printStackTrace();

		} catch (IOException exp) {

			exp.printStackTrace();

		}

	}

}


