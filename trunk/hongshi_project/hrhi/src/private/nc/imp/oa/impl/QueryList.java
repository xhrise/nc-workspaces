package nc.imp.oa.impl;

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

import nc.bs.framework.common.NCLocator;
import nc.itf.oa.IBasDMO;
import nc.itf.oa.IoaqueryList;
import nc.ui.hi.hi_301.PsnInfCollectUI;
import nc.vo.jcom.xml.XMLUtil;

import org.xml.sax.SAXException;

public class QueryList implements IoaqueryList{
	private String ORACLEDATASOURCE = "nc56true";
	private IBasDMO dmo  = (IBasDMO) NCLocator.getInstance().lookup(IBasDMO.class.getName());
	
	public boolean checkOANameRep(String OAName) throws Exception {
		int count = dmo.checkOANameRep(OAName, ORACLEDATASOURCE);
		if(count == 0)
			return true;
		return false;
	}
	
	public String getDatasourse() {
		String ncFileName = "Ufida/datasource";
		String xmlfile = "Ufida/datasource/datasource.xml";
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
			org.w3c.dom.Element element_ip = document.createElement("source");
			element_ip.setTextContent("hssn");
			document.appendChild(element_ip);
			this.toSave(document, xmlList.getPath());
		}

		try {
			document = XMLUtil.getDocumentBuilder().parse(xmlList);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ip = document.getElementsByTagName("source");

		return ip.item(0).getTextContent();
	}
	
	public void toSave(org.w3c.dom.Document document, String filename) {
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
