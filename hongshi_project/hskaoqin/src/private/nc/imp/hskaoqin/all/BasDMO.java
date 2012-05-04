package nc.imp.hskaoqin.all;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.pub.DataManageObject;
import nc.itf.hskaoqin.all.IBasDMO;
import nc.jdbc.framework.ConnectionFactory;
import nc.vo.hskaoqin.all.ImpDataVO;

public class BasDMO extends DataManageObject implements IBasDMO {
	public BasDMO() throws NamingException {
		super();
	}

	private Connection conn = null;

	private PreparedStatement stmt = null;

	private ResultSet rs = null;
	
	private  String driverName = "";
	private  String sourceURL = "";
	private  String user = "";
	private  String password = ""; 
	
	private void initConnection(String datasource) throws Exception {
		
		if(driverName == null || "".equals(driverName)) {
			File file = new File("Ufida/datasource/"+datasource+".properties");
			
			try {
				if(!file.exists()) {
					file.createNewFile();
					throw new Exception ("考勤数据源未配置，请配置数据源后操作！文件实际路径 ：" + file.getAbsolutePath());
				}
				
				InputStream is = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(is);
				
				driverName = prop.getProperty("driverName");
				sourceURL = prop.getProperty("sourceURL");
				user = prop.getProperty("user");
				password = prop.getProperty("password");
				
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
		}
	}
	
	private Connection getSqlConn(String datasource) throws Exception {
//		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // 加载JDBC驱动
//		String sourceURL = "jdbc:sqlserver://localhost:1433; DatabaseName=attendance"; // 连接服务器和数据库sample
//		String user = "sa"; // 默认用户名
//		String password = "sa"; // 密码

		initConnection(datasource);
		
		try {
			Class.forName(driverName);
			Connection cbd = DriverManager.getConnection(sourceURL, user, password);
			
			return cbd;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	/**
	 * 获取考勤数据
	 */
	public Vector<ImpDataVO> getGetDataVOs(String startTime, String endTime,String datasource) throws Exception {
		Vector<ImpDataVO> getdataVector = new Vector<ImpDataVO>();
		try {
			String sql="select brcardno,brdate,brtime,brmachineno from brushrecord where brdate>='"+startTime+"' and brdate<='"+endTime+"'";
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//			} else {
//				conn = ConnectionFactory.getConnection(datasource);
//			}
			
			if(conn == null)
				conn = this.getSqlConn(datasource);
			
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ImpDataVO getDataVO = new ImpDataVO();
				getDataVO.setTimecardid(rs.getString("brcardno"));
				getDataVO.setCalendartime(("20"+rs.getString("brdate")+" "+rs.getString("brtime")));
				if (rs.getString("brmachineno").equals("3")) {
					getDataVO.setDatastatus("0");
				}else {
					int timea=Integer.parseInt(rs.getString("brtime").substring(0, 2));
					if (timea>6&&timea<12) {
						getDataVO.setDatastatus("0");
					}else {
						getDataVO.setDatastatus("1");
					}
				}
				getdataVector.add(getDataVO);
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		} finally {
			closeAll();
		}
		return getdataVector;
	}

	public String getStr(String sql, String datasource) throws Exception {
		String retstr = "";
		try {
			try {
				Integer.parseInt(datasource);
				conn = getSqlConn(datasource);
			} catch(Exception e) {
				if ("".equals(datasource)) {
					conn = ConnectionFactory.getConnection();
				} else {
					conn = ConnectionFactory.getConnection(datasource);
				}
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				retstr = rs.getString(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAll();
		}
		return retstr;
	}

	public ArrayList<String> getStrs(String sql, String datasource)
			throws Exception {
		ArrayList<String> retArrayList = new ArrayList<String>();
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				retArrayList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAll();
		}
		return retArrayList;
	}

	public boolean executeSql(String sql, String datasource)
			throws SQLException {
		boolean retB = false;
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			if (stmt.executeUpdate()>0) {
				retB=true;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			closeAll();
		}
		return retB;
	}

	public void closeAll() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
		}
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
		}
		try {
			if (conn != null) {
				conn.close();
				conn = null;
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
