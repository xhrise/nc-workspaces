package nc.imp.tc.imp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import nc.bs.pub.DataManageObject;
import nc.jdbc.framework.ConnectionFactory;

public class BasDMO extends DataManageObject {
	public BasDMO() throws NamingException {
		super();
	}

	private Connection conn = null;

	private PreparedStatement stmt = null;

	private ResultSet rs = null;
	//测试获得连接方法
	private Connection getfdhConn()
	{
		   try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		    String sourceURL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		    String user = "iufoxx";
		    String password = "iufoxx";
		    Connection cbd = DriverManager.getConnection(
		    	      sourceURL, user, password);
		    return cbd;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	// 获取iufo_function的信息
	public int getLikeFuncCount(String datasource)
	{
		String sql="select count(*) counts from iufo_function where func_name = '报表数据->录入->子公司上报报表录入'";
		int num=0;
		int ckno = 0;
		while(ckno == 0){
			try {
				if ("".equals(datasource)) {
					conn = ConnectionFactory.getConnection();
				} else {
	//				conn=getfdhConn();
					conn = ConnectionFactory.getConnection(datasource);
				}
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					num=rs.getInt("counts");
				}
				ckno = 1;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeAll();
			}
		}
		return num;
	}
	
	public int insertFunc(String datasource)throws SQLException
	{
		String sql="insert into iufo_function(ICON , TYPE , URL , PARENT_ID , Func_Name , Func_Order , Is_Display , Func_Res_Id , Type_Ex , TS , DR) " + 
			"VALUES('',8,'nc.ui.iufo.input.AutoInputKeywordsAction' , 'A10A20A20' , '报表数据->录入->子公司上报报表录入' , 'A10A20A20A30' , "+
			"'Y' , 'uiufofurl530123' , 0 , '2011-03-05 16:05:01' , 0)";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
//				conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		} finally {
			closeAll();
		}
		
	}
	
	public String getReportPk(String reportcode , String datasource) throws SQLException{
		String sql = "select id from iufo_report where reportcode = '" + reportcode + "'";
		String pk = null;
		int num = 0;
		while(num == 0)
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				//conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			
			if (rs.next()) {
				pk=rs.getString("id");
			}
			num = 1;
		} catch (SQLException e) {
			num = 0;
		} finally {
			closeAll();
		}
		return pk;
	}
	
////	更新报表表样主键
//	public int alterReportCommit(String datasource)
//	{
//		int rest=0;
//		String sql="alter table iufo_report_commit add(UNIT_ID VARCHAR2(64))";
//		try {
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//				conn.setAutoCommit(true);
//			} else {
////				conn=getfdhConn();
//				conn = ConnectionFactory.getConnection(datasource);
//				conn.setAutoCommit(true);
//			}
//			stmt = conn.prepareStatement(sql);
//			 rest= stmt.executeUpdate();
//
//		} catch (SQLException e) {
//			return 0;
//		} finally {
//			closeAll();
//		}
//		return rest;
//	}
	
	public int insertReportCommit(String id , String aloneId , String datasource)throws SQLException
	{
		String sql="insert into iufo_report_commit(ver , commit_flag , request_cancel , rep_id , alone_id , id , commit_time , ts , dr) " + 
			"VALUES(0 , 0 , 0 , '"+ id +"' , '"+ aloneId +"' , '"+ aloneId +"' , to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') , to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') , 0)";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
//				conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		} finally {
			closeAll();
		}
		
	}
	//
	public String getUnitID(String unit_code , String datasource) throws SQLException{
		String sql = "select distinct unit_id from iufo_unit_info where unit_code = '"+unit_code+"'";
		String pk = null;
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
//				conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			
			if (rs.next()) {
				pk=rs.getString("unit_id");
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		} finally {
			closeAll();
		}
		return pk;
	}
	
	public int updateReportCommit(String datasource)throws SQLException
	{
		String sql="update iufo_report_commit set commit_flag = 1";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
//				conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		} finally {
			closeAll();
		}
		
	}
	
	public int updateCancleCommit(String unit_name , String reportcodes , String datasource)throws SQLException
	{
		String sql="update iufo_report_commit set request_cancel = 1 where commit_flag <> 0 and  ( (unit_id in (select distinct unit_id from iufo_unit_info where unit_name = "+unit_name+") and rep_id in (select distinct id from iufo_report where reportcode in ("+reportcodes+"))) )";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				//conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
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

	public int insertReleaseinfo(String content, String title, String bbsid,
			String datasource) throws Exception {
		String sql = "insert into iufo_releaseinfo (createtime , content , title , creator , bbs_id) values (to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') , '"
				+ content
				+ "' , '"
				+ title
				+ "' , '000000000000' , '"
				+ bbsid
				+ "')";
		try {

			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}

			stmt = conn.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeAll();
		}
	}

	public int insertReleasetarget(String bbsid, String user_id,
			String datasource) throws Exception {
		String sql = "insert into IUFO_RELEASETARGET (bbs_id , user_id , status) values ('"
				+ bbsid + "' , " + user_id + " , 0)";
		try {

			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}

			stmt = conn.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeAll();
		}
	}

	public java.util.List<String> getUserId(String datasource) throws Exception {

		String sql = "select distinct user_id from iufo_user_role";
		java.util.List<String> userList = new java.util.ArrayList<String>();
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
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

	public void closeAll(Connection conn, PreparedStatement stmt, ResultSet rs) {
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
}
