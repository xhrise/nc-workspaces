package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class removeNode {

	public static void main(String[] args) throws SAXException, IOException{
		org.w3c.dom.Document newDoc = XMLUtil.getDocumentBuilder().parse("E:/NCSOFT/NC502/NCHOME/Ufida_IUFO/shangbaojieshou/xmlList.xml");
		org.w3c.dom.NodeList typeList = newDoc.getElementsByTagName("type");
		org.w3c.dom.NodeList fileList = newDoc.getElementsByTagName("file");
		org.w3c.dom.NodeList AllList = newDoc.getElementsByTagName("list");
		System.out.print(fileList.getLength());
		for(int k = 0 ; k < fileList.getLength() ; k++){
			String fileAllPath = fileList.item(k).getTextContent();
			String date = fileAllPath.substring(fileAllPath.lastIndexOf("\\") - 10 , fileAllPath.lastIndexOf("\\"));
			if(date.equals("2011-04-30") || date == "2011-04-30"){
				Node node = AllList.item(k);
				newDoc.getFirstChild().removeChild(node);
				new removeNode().toSave(newDoc, "E:/NCSOFT/NC502/NCHOME/Ufida_IUFO/shangbaojieshou/xmlList.xml");
				k--;
			}
		}
	}
	
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
