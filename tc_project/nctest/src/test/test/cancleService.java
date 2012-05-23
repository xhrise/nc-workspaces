package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.vo.jcom.xml.XMLUtil;

public class cancleService implements Serializable {
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

	 public static void main(String[] args) throws Exception {
	 new cancleService().CancleCommit("'太仓港协鑫发电有限公司'", "'Z1资产负债表'" ,
	 "'199'", "2011-04");
	 // System.out.println( new cancleService().GenPk());
	 }

	public String CancleCommit(String unit_name, String reportcodes,
			String unit_code , String date) throws Exception {
		try {
			// QueryList queryList = new QueryList();
			int num = 0;
			num = this.updateCancleCommit(unit_name, reportcodes, unit_code , date);
			if (num > 0) {
				String[] repcodes = reportcodes.substring(1 , reportcodes.length()-1).split("','");
				String repnewcodes = "";
				for(String repcode : repcodes){
					repnewcodes += repcode + " ";
				}
				String content = "子公司_(" + unit_code.substring(1 , unit_code.length() - 1) +")"+unit_name.substring(1 , unit_name.length() - 1) + "_请求取消上报 "+date+" 的报表_"+ repnewcodes+ "\n\t\t";
				
				String title = unit_name.substring(1 , unit_name.length() - 1) + "请求取消上报" ;
				String randomVal = this.GenPk();
				this.insertReleaseinfo(content, title, randomVal);

				java.util.List<String> userList = this.getUserId();
				for (String userId : userList) {
					this.insertReleasetarget(randomVal, userId);
				}

				return "取消上报已经成功提交，请等待处理！";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "请求失败！";
		}
		return "请求失败！";
	}
	
	public boolean checkUpload(String strCode , String date , String unit_code){
		if(this.getCommitCount(strCode, date, unit_code) > 0)
			return false;
		return true;
	}
	
	
	
	public int getCommitCount(String strCode , String date , String unit_code){
			String sql = "select count(ts) as count from iufo_report_commit where alone_id in (select alone_id from iufo_measure_pubdata where code in (select unit_id from iufo_unit_info where unit_code = '"+unit_code+"')" 
						+" and inputdata like '"+date+"%') and rep_id in (select id from iufo_report where reportcode = '"+strCode+"')";
		try {
			conn = getfdhConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return 0;
	}

	public int updateCancleCommit(String unit_name, String reportcodes,
			String unit_code, String date) throws SQLException {
		// String sql = "update iufo_report_commit set request_cancel = 1 where
		// commit_flag <> 0 and ( (unit_id in (select distinct unit_id from
		// iufo_unit_info where unit_name = "
		// + unit_name
		// + " or unit_code = "
		// + unit_code
		// + ") and rep_id in (select distinct id from iufo_report where
		// reportcode in ("
		// + reportcodes + "))) )";

		String sql = "update iufo_report_commit set request_cancel = 1 where commit_flag <> 0 and  alone_id in (select alone_id from iufo_measure_pubdata where code = (select unit_id from iufo_unit_info where unit_code in ( "
				+ unit_code
				+ ") or unit_name in ( "
				+ unit_name
				+ ")) and inputdate like '"
				+ date
				+ "%') and rep_id in (select id from iufo_report where reportcode in ( "
				+ reportcodes + "))";

		try {

			conn = getfdhConn();

			stmt = conn.prepareStatement(sql);
			return stmt.executeUpdate();

		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		} finally {
			closeAll();
		}

	}

	public String GenPk() throws Exception {
		java.util.Random random = new java.util.Random();
		String randoms = "qwertyuiopasdfghjklzxcvbnm1234567890";
		String randomVal = "";

		for (int i = 0; i < 20; i++) {
			int ran = random.nextInt(randoms.length());
			if (ran == 0)
				ran = 1;

			try {
				randomVal += randoms.substring(ran - 1, ran);
			} catch (Exception e) {
				randomVal += randoms.substring(ran - 1, ran);
			}
		}
		return randomVal;
	}

	public int insertReleaseinfo(String content, String title, String bbsid)
			throws Exception {
		String sql = "insert into iufo_releaseinfo (createtime , content , title , creator , bbs_id) values (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') , '"
				+ content
				+ "' , '"
				+ title
				+ "' , '000000000000' , '"
				+ bbsid
				+ "')";
		try {

			conn = getfdhConn();

			stmt = conn.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeAll();
		}
	}

	public int insertReleasetarget(String bbsid, String user_id)
			throws Exception {
		String sql = "insert into IUFO_RELEASETARGET (bbs_id , user_id , status) values ('"
				+ bbsid + "' , " + user_id + " , 0)";
		try {

			conn = getfdhConn();
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeAll();
		}
	}

	public java.util.List<String> getUserId() throws Exception {

		String sql = "select distinct user_id from iufo_user_role";
		java.util.List<String> userList = new java.util.ArrayList<String>();
		try {
			conn = getfdhConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				userList.add("'" + rs.getString("user_id") + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}

		return userList;
	}

	public void closeAll() {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
		}
	}

	public String[] getDataSource() {
		String ncFileName = "C:/Ufida_IUFO/service502ip";
		String xmlfile = "C:/Ufida_IUFO/service502ip/datasource.xml";
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
