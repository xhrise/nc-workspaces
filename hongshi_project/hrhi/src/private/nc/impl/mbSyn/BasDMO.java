package nc.impl.mbSyn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import nc.bs.pub.DataManageObject;
import nc.jdbc.framework.ConnectionFactory;

import com.seeyon.client.PersonServiceStub.PersonInfoParam_All;

public class BasDMO extends DataManageObject{

	public BasDMO() throws NamingException {
		super();
	}
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	private Connection getOclConn() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String sourceURL = "jdbc:oracle:thin:@127.0.0.1:1521/orcl";
			String user = "hssn";
			String password = "hssn";
			Connection cbd = DriverManager.getConnection(sourceURL, user,
					password);
			return cbd;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public List<PersonInfoParam_All> getPerson(String sql , String datasource) throws Exception{
		
		boolean check = true;
		List<PersonInfoParam_All> persons = new ArrayList<PersonInfoParam_All>();
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
//				conn = getOclConn();
				check = false;
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				PersonInfoParam_All person = new PersonInfoParam_All();
				person.setAccountId(ServiceUtil.getAccountId(rs.getString("unitname")));
				person.setLoginName(rs.getString("def1"));
				person.setSex(rs.getString("sex"));
				person.setBirthday(rs.getString("birthdate"));
				person.setEmail(rs.getString("email"));
				person.setMobilePhone(rs.getString("mobile"));
				person.setOtypeName(rs.getString("jobname"));
				person.setDepartmentName(new String[]{rs.getString("deptname") , rs.getString("deptname")});
				person.setOcupationName(rs.getString("jobname"));
				person.setSecondOcupationName(new String[]{});
				person.setDeptcode(rs.getString("deptcode"));
				person.setTrueName(rs.getString("psnname"));
				person.setStaffNumber(rs.getString("psncode"));
				
				persons.add(person);
			}
			
			return persons;
		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	public List<String> getSecondOcupationName(String sql , String datasource) throws Exception{
		
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			List<String> strList = new ArrayList<String>();
			
			while (rs.next()) {
				strList.add(rs.getString("deptname") + "_" + rs.getString("jobname"));
			}
			
			return strList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
//	public void addOrgAccount(String datasource) {
//		this.delOrgAccount(datasource);
//		
//		String sql = "insert into V3X_ORG_ACCOUNT select * from hsoa.V3X_ORG_ACCOUNT";
//
//		try {
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//			} else {
//				conn = ConnectionFactory.getConnection(datasource);
//			}
//
//			stmt = conn.prepareStatement(sql);
//			stmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeAll();
//		}
//	}
//	
//	public void addOrgDept(String datasource) {
//		this.delOrgDept(datasource);
//		
//		String sql = "insert into v3x_org_department select * from hsoa.v3x_org_department";
//
//		try {
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//			} else {
//				conn = ConnectionFactory.getConnection(datasource);
//			}
//
//			stmt = conn.prepareStatement(sql);
//			stmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeAll();
//		}
//	}
//	
//	public void addOrgPost(String datasource) {
//		this.delOrgPost(datasource);
//		
//		String sql = "insert into v3x_org_post select * from hsoa.v3x_org_post";
//
//		try {
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//			} else {
//				conn = ConnectionFactory.getConnection(datasource);
//			}
//
//			stmt = conn.prepareStatement(sql);
//			stmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeAll();
//		}
//	}
//	
//	public void delOrgAccount(String datasource) {
//		String sql = "delete from V3X_ORG_ACCOUNT";
//		try {
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//			} else {
//				conn = ConnectionFactory.getConnection(datasource);
//			}
//
//			stmt = conn.prepareStatement(sql);
//			stmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeAll();
//		}
//	}
//	
//	public void delOrgDept(String datasource) {
//		String sql = "delete from V3X_ORG_MEMBER";
//		try {
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//			} else {
//				conn = ConnectionFactory.getConnection(datasource);
//			}
//
//			stmt = conn.prepareStatement(sql);
//			stmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeAll();
//		}
//	}
//	
//	public void delOrgPost(String datasource) {
//		String sql = "delete from v3x_org_post";
//		try {
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//			} else {
//				conn = ConnectionFactory.getConnection(datasource);
//			}
//
//			stmt = conn.prepareStatement(sql);
//			stmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeAll();
//		}
//	}
//	
//	public Map<String , Object> getV3xMemberById(String sql , String datasource) throws Exception{
//		this.addOrgAccount(datasource);
//		this.addOrgDept(datasource);
//		this.addOrgPost(datasource);
//		
//		Map<String , Object> memberMap = new HashMap<String , Object>();
//		try {
//			if ("".equals(datasource)) {
//				conn = ConnectionFactory.getConnection();
//			} else {
//				conn = ConnectionFactory.getConnection("hsoa");
//
//			}
//
//			stmt = conn.prepareStatement(sql);
//			rs = stmt.executeQuery();
//			
//			if (rs.next()) {
//				memberMap.put("id", rs.getLong(1));
//				memberMap.put("name", rs.getString(2));
//				memberMap.put("code", rs.getString(3));
//				memberMap.put("org_department_id", rs.getLong(4));
//				memberMap.put("org_level_id", rs.getLong(5));
//				memberMap.put("org_account_id", rs.getLong(6));
//				memberMap.put("org_post_id", rs.getLong(7));
//			}
//			
//			return memberMap;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeAll();
//		}
//		
//		return null;
//	}
	
	public String[] getPersonDefVal(String sql , String datasource) {
		String[] strs = new String[3];
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				strs[0] = rs.getString(1);
				strs[1] = rs.getString(2);
				strs[2] = rs.getString(3);
			}
			
			return strs;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	public void modifyMemberById_x(String sql , String datasource) {

		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
			}

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}

	}
	
	public Long getLongId(String sql , String datasource) {

		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				return rs.getLong(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	public String getPersonName(String sql , String datasource) {

		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				return rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
public List<Long> getDeletePerson(String sql , String datasource) throws Exception{
		
		boolean check = true;
		List<Long> persons = new ArrayList<Long>();
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
//				conn = getOclConn();
				check = false;
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				persons.add(rs.getLong("def3"));
			}
			
			return persons;
		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	public String getDeptName(String sql , String datasource) throws Exception{
		
		boolean check = true;
		//List<PersonInfoParam_All> persons = new ArrayList<PersonInfoParam_All>();
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
//				conn = getOclConn();
				check = false;
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return rs.getString("deptname");
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	public boolean updatePerson(String sql , String datasource) throws Exception{
		boolean check = true;
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
//				conn = getOclConn();
				check = false;
			}

			stmt = conn.prepareStatement(sql);
			stmt.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();
		}
		
		return false;
	}
	
	public boolean insertPerson(String sql , String datasource) throws Exception{
		boolean check = true;
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
//				conn = getOclConn();
				check = false;
			}

			stmt = conn.prepareStatement(sql);
			return stmt.execute();

		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();
		}
		
		return false;
	}
	
	public List<String[]> getDepts(String sql , String datasource) throws Exception{
		
		boolean check = true;
		List<String[]> deptNames = new ArrayList<String[]>();
		
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
//				conn = getOclConn();
				check = false;
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String[] deptName = new String[4];
				deptName[1] = rs.getString("deptname");
				deptName[2] = rs.getString("deptcode");
				deptName[3] = rs.getString("pk_corp");
				deptNames.add(deptName);
			}
			
			return deptNames;
		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	public List<String[]> getCorps(String sql , String datasource) throws Exception{
		
		boolean check = true;
		List<String[]> corps = new ArrayList<String[]>();
		
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
//				conn = getOclConn();
				check = false;
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String[] strs = new String[2];
				strs[0] = rs.getString("unitname");
				strs[1] = rs.getString("pk_corp");
				corps.add(strs);
			}
			
			return corps;
		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();
		}
		
		return null;
	}
	
	public List<String[]> getOcups(String sql , String datasource) throws Exception{
		
		boolean check = true;
		List<String[]> ocupNames = new ArrayList<String[]>();
		
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				conn = ConnectionFactory.getConnection(datasource);
//				conn = getOclConn();
				check = false;
			}

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String[] ocupName = new String[4];
				ocupName[1] = rs.getString("jobname");
				ocupName[2] = rs.getString("jobcode");
				ocupName[3] = rs.getString("pk_corp");
				ocupNames.add(ocupName);
			}
			
			return ocupNames;
		} catch (Exception e) {
			e.printStackTrace();
//			if(check)
//				throw new Exception("数据源配置出错，请重新配置数据源文件！");
//			else
//				throw new Exception("数据库查询出错。");
		} finally {
			this.closeAll();
		}
		
		return null;
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
