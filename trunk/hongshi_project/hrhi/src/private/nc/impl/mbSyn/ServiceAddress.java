package nc.impl.mbSyn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.itf.mbSyn.IServiceAddress;
import nc.vo.jcom.xml.XMLUtil;

import org.xml.sax.SAXException;

public  class ServiceAddress implements IServiceAddress{
	public String getDatasourse() {
		String ncFileName = "Ufida/address";
		String xmlfile = "Ufida/address/ServiceAddress.xml";
		org.w3c.dom.NodeList ip = null;
		org.w3c.dom.Document document = null;

		File myFilePath = new File(ncFileName);
		if (!(myFilePath.exists())) {
			myFilePath.mkdirs();
		}
		java.io.File xmlList = new java.io.File(xmlfile);

		// 如果xmlList.xml不存在则创建
		Boolean bl = xmlList.exists();
		if (!bl) {

			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Element element_ip = document.createElement("address");
			element_ip.setTextContent("192.168.10.8:8080");
			document.appendChild(element_ip);
			toSave(document, xmlList.getPath());
		}

		try {
			document = XMLUtil.getDocumentBuilder().parse(xmlList);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ip = document.getElementsByTagName("address");

		return ip.item(0).getTextContent();
	}
	
	public static void toSave(org.w3c.dom.Document document, String filename) {
		PrintWriter pw = null;
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			pw = new PrintWriter(new FileOutputStream(filename));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerException mye) {
			mye.printStackTrace();
		} catch (IOException exp) {
			exp.printStackTrace();
		} finally {
			pw.close();
		}
	}
}
