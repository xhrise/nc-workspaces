package nc.imp.oa.bas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import nc.bs.pub.DataManageObject;
import nc.itf.oa.IBasDMO;
import nc.jdbc.framework.ConnectionFactory;

public class BasDMO extends DataManageObject implements IBasDMO{

	public BasDMO() throws NamingException {
		super();
		// TODO Auto-generated constructor stub
	}

	private Connection conn = null;

	private PreparedStatement stmt = null;

	private ResultSet rs = null;

	private String sql = "";


	public int checkOANameRep(String OAName, String datasource) throws Exception {
//		sql = "select count(def1) as count from bd_psndoc where def1 = '"
//				+ OAName + "'";
		
		sql = "select count(*) as count from bd_psndoc where def1 = '" + OAName + "'";
		
		boolean check = true;
		
		try {
			while(check){
				try{
				if ("".equals(datasource)) {
					conn = ConnectionFactory.getConnection();
				} else {
					conn = ConnectionFactory.getConnection(datasource);
					check = false;
				}
				}catch(Exception ex){
					check = true;
				}
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();

		}

		return 0;
	}
	public String getStr(String sql, String datasource) throws SQLException {
		String retstr = "";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				retstr = String.valueOf(rs.getDouble(1));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAll();
		}
		return retstr;
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
