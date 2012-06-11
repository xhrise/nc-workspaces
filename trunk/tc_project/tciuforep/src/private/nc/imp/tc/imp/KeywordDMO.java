package nc.imp.tc.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.vo.jcom.xml.XMLUtil;
import nc.vo.tc.imp.KeywordVO;

public class KeywordDMO extends BasDMO {

	public KeywordDMO() throws NamingException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private Connection conn = null;

	private PreparedStatement stmt = null;

	private ResultSet rs = null;

	// 测试获得连接方法
	private Connection getfdhConn() {
		try {
			String[] source = this.getDataSource();
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String sourceURL = "jdbc:oracle:thin:@" + source[0];
			String user = source[1];
			String password = source[2];
			Connection cbd = DriverManager.getConnection(sourceURL, user,
					password);
			return cbd;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public List<KeywordVO> getKeywords(){
		String sql="select * from iufo_keyword where isprivate = 'N' and note is null";
		List<KeywordVO> keywords = new ArrayList<KeywordVO>();
		try {
			conn=getfdhConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				KeywordVO keyword = new KeywordVO();
				keyword.setIsbuiltin(rs.getString("isbuiltin"));
				keyword.setIsseal(rs.getString("isseal"));
				keyword.setKey_count(rs.getInt("key_count"));
				keyword.setLen(rs.getInt("len"));
				keyword.setName(rs.getString("name"));
				keyword.setPk_keyword(rs.getString("pk_keyword"));
				keyword.setType(rs.getInt("type"));
				keyword.setVer(rs.getString("ver"));
				keywords.add(keyword);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return keywords;
	}
	
	public String[] getDataSource() {
		String ncFileName = "Ufida_IUFO/service502ip";
		String xmlfile = "Ufida_IUFO/service502ip/datasource.xml";
		org.w3c.dom.Document document = null;
		java.io.File xmlList = new java.io.File(xmlfile);

		File myFilePath = new File(ncFileName);
		if (!(myFilePath.exists())) {
			myFilePath.mkdirs();
		}

		// 如果xmlList.xml不存在则创建
		Boolean bl = xmlList.exists();
		if (!bl) {

			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Element main = document.createElement("main");
			org.w3c.dom.Element datasource = document
					.createElement("datasource");
			org.w3c.dom.Element user = document.createElement("user");
			org.w3c.dom.Element pass = document.createElement("pass");

			datasource.setTextContent("127.0.0.1:1521:orcl");
			user.setTextContent("iufoxx");
			pass.setTextContent("iufoxx");

			main.appendChild(datasource);
			main.appendChild(user);
			main.appendChild(pass);
			document.appendChild(main);
			this.toSave(document, xmlList.getPath());
		}

		try {
			document = XMLUtil.getDocumentBuilder().parse(xmlList);
		} catch (Exception e) {
			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException ex) {
				ex.printStackTrace();
			}
			org.w3c.dom.Element main = document.createElement("main");
			org.w3c.dom.Element datasource = document
					.createElement("datasource");
			org.w3c.dom.Element user = document.createElement("user");
			org.w3c.dom.Element pass = document.createElement("pass");

			datasource.setTextContent("127.0.0.1:1521:orcl");
			user.setTextContent("iufoxx");
			pass.setTextContent("iufoxx");

			main.appendChild(datasource);
			main.appendChild(user);
			main.appendChild(pass);
			document.appendChild(main);
			this.toSave(document, xmlList.getPath());
		}

		String[] datasource = new String[3];
		datasource[0] = document.getElementsByTagName("datasource").item(0)
				.getTextContent();
		datasource[1] = document.getElementsByTagName("user").item(0)
				.getTextContent();
		datasource[2] = document.getElementsByTagName("pass").item(0)
				.getTextContent();

		return datasource;
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
