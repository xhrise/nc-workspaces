package nc.imp.tc.imp;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import oracle.sql.BLOB;

import sun.misc.BASE64Encoder;

import nc.bs.pub.DataManageObject;
import nc.jdbc.framework.ConnectionFactory;
import nc.vo.tc.imp.KeywordVO;

public class BasDMO extends DataManageObject {
	public BasDMO() throws NamingException {
		super();
	}

	private Connection conn = null;

	private PreparedStatement stmt = null;

	private ResultSet rs = null;

	// 测试获得连接方法
	private Connection getfdhConn() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String sourceURL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
			String user = "iufo56";
			String password = "iufo56";
			Connection cbd = DriverManager.getConnection(sourceURL, user,
					password);
			return cbd;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	// 更新报表任务
	public int updateReporttaskset(String oldId, String newId, String datasource) {
		int rest = 0;
		String sql = "update iufo_taskset set REP_ID='" + newId
				+ "' where REP_ID='" + oldId + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
				conn.setAutoCommit(true);
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
				conn.setAutoCommit(true);
			}
			stmt = conn.prepareStatement(sql);
			rest = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return rest;
	}

	// 更新报表表样主键
	public int updateReportPKset(String name, String newId, String datasource) {
		int rest = 0;
		String sql = "update iufo_report set id='" + newId + "' where name='"
				+ name + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
				conn.setAutoCommit(true);
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
				conn.setAutoCommit(true);
			}
			stmt = conn.prepareStatement(sql);
			rest = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return rest;
	}

	// 更新报表表样主键
	public int updateMeasurePKset(String oldID, String newId, String datasource) {
		int rest = 0;
		String sql = "update IUFO_TEMPMEASURE set REPORTPK='" + newId
				+ "' where REPORTPK='" + oldID + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
				conn.setAutoCommit(true);
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
				conn.setAutoCommit(true);
			}
			stmt = conn.prepareStatement(sql);
			rest = stmt.executeUpdate();

		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return rest;
	}

	// 更新报表表样主键
	public int updateCOPKset(String oldID, String newId, String datasource) {
		int rest = 0;
		String sql = "update IUFO_CO211558983 set OBJ_ID='" + newId
				+ "' where OBJ_ID='" + oldID + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
				conn.setAutoCommit(true);
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
				conn.setAutoCommit(true);
			}
			stmt = conn.prepareStatement(sql);
			rest = stmt.executeUpdate();

		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return rest;
	}

	// 更新报表表样主键
	public int alterTs(String datasource) {
		int rest = 0;
		String sql = "alter table IUFO_CO211558983 add(TS CHAR(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'))";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
				conn.setAutoCommit(true);
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
				conn.setAutoCommit(true);
			}
			stmt = conn.prepareStatement(sql);
			rest = stmt.executeUpdate();

		} catch (SQLException e) {
			return 0;
		} finally {
			closeAll();
		}
		return rest;
	}

	// 更新报表编码和名字
	public int updateReportnamecode(String reportcode, String name, String id,
			String datasource) throws SQLException {
		int rest = 0;
		// by fdh 1.26更新 把原来的表样迁移到 集团备份 文件夹下
		String sql = "update iufo_report set DIRECTORYID=(select DIR_ID from iufo_reportdir where DIR_NAME='集团备份'),reportcode='"
				+ reportcode + "',name='" + name + "' where id='" + id + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
				conn.setAutoCommit(true);
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
				conn.setAutoCommit(true);
			}
			stmt = conn.prepareStatement(sql);
			rest = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return rest;
	}

	// 取得报表编码和名字
	public int getReportnamecodeCount(String id, String datasource) {
		String[] rest = new String[3];
		String sql = "select count(id) as counts from iufo_report where id='"
				+ id + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("counts");
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return 0;

	}

	// 取得报表编码和名字
	public int getReportcodeCount(String pk, String datasource) {
		String[] rest = new String[3];
		String sql = "select count(id) as counts from iufo_report where reportcode like '%"
				+ pk + "%'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("counts");
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return 0;

	}

	// 取得报表编码和名字
	public String[] getReportnamecode(String name, String datasource) {
		String[] rest = new String[3];
		String sql = "select reportcode,name,id from iufo_report where reportcode='"
				+ name + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				rest[0] = rs.getString("reportcode");
				rest[1] = rs.getString("name");
				rest[2] = rs.getString("id");
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return rest;

	}

	// DR NUMBER(10) Y 0
	// KEYGROUPPK VARCHAR2(20) N '00000000000000000000'
	// MEASUREPACKPK VARCHAR2(12) N
	// MEASURES BLOB Y
	// REPORTPK VARCHAR2(20) N
	// TS CHAR(19) Y to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')

	// 取得报表关联表编码和名字
	public Object[] getMeasurenamecode(String REPORTPK, String datasource) {
		Object[] rest = new Object[4];
		String sql = "select REPORTPK,MEASURES,MEASUREPACKPK,KEYGROUPPK from iufo_measure where REPORTPK='"
				+ REPORTPK + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				rest[0] = rs.getString("REPORTPK");
				rest[1] = rs.getObject("MEASURES");
				rest[2] = rs.getString("MEASUREPACKPK");
				rest[3] = rs.getString("KEYGROUPPK");
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return rest;

	}

	// 创建中间表
	public int createTempMeasure(String datasource) {
		String createTable = "create table IUFO_TEMPMEASURE(KEYGROUPPK VARCHAR2(20) default '00000000000000000000' not null,"
				+ "MEASUREPACKPK VARCHAR2(12) not null,MEASURES BLOB,REPORTPK VARCHAR2(20) not null,TS CHAR(19) default "
				+ "to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),DR NUMBER(10) default 0)";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(createTable);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			return 0;
		} finally {
			closeAll();
		}
	}

	// 创建中间表
	public int insertTempMeasure(String datasource) {
		String sql = "insert into IUFO_TEMPMEASURE(KEYGROUPPK,MEASUREPACKPK,MEASURES,REPORTPK,TS,DR) values('@@@@@@@@@@0000000000','@@@@@@000000',empty_blob(),'@@@@@@@@@@1111111111','2011-01-01 00:00:00' , 0)";
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
			return 0;
		} finally {
			closeAll();
		}
	}

	// 查看是否已经有中间表
	public int gettempMeasure(String datasource) {
		String[] rest = new String[3];
		String sql = "select count(REPORTPK) as count from IUFO_TEMPMEASURE";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt("count");
			}
			return count;
		} catch (SQLException e) {
			return 0;
		} finally {
			closeAll();
		}
	}

	// 取得备份报表编码和名字
	public String[] getOldReportnamecode(String id, String datasource) {
		String[] rest = new String[3];
		String sql = "select reportcode,name,id from iufo_report where ID='"
				+ id + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				rest[0] = rs.getString("reportcode");
				rest[1] = rs.getString("name");
				rest[2] = rs.getString("id");
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return rest;

	}

	// 取得报表关联表编码和名字
	public int delMeasurenamecode(String REPORTPK, String OLDPK,
			String datasource) {

		String sql = "delete from iufo_measure where REPORTPK in ('" + REPORTPK
				+ "' , '" + OLDPK + "')";
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
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return 0;

	}

	// 迁移measure表数据
	public int insertMeasurenamecode(String reportpk, String datasource) {
		// String sql="delete from iufo_measure where REPORTPK in
		// ('"+REPORTPK+"' , '"+OLDPK+"')";
		String sql = "insert into iufo_tempmeasure(REPORTPK,MEASURES,MEASUREPACKPK,KEYGROUPPK,DR,TS) "
				+ " select REPORTPK,MEASURES,MEASUREPACKPK,KEYGROUPPK,DR,TS from iufo_measure where reportpk = '"
				+ reportpk + "'";
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
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return 0;

	}

	// 还原measure表数据
	public int revertMeasure(String reportpk, String datasource) {
		// String sql="delete from iufo_measure where REPORTPK in
		// ('"+REPORTPK+"' , '"+OLDPK+"')";
		String sql = "insert into iufo_measure(REPORTPK,MEASURES,MEASUREPACKPK,KEYGROUPPK,DR,TS) "
				+ " select REPORTPK,MEASURES,MEASUREPACKPK,KEYGROUPPK,DR,TS from iufo_tempmeasure where reportpk = '"
				+ reportpk + "'";
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
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return 0;

	}

	// 删除中间表
	public int delTempMeasure(String datasource) {
		String sql = "delete from iufo_tempmeasure where REPORTPK not in ('@@@@@@@@@@1111111111')";
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
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return 0;

	}

	// 取得目录的ID
	public String getDir_Id(String name, String datasource) throws SQLException {
		String sql = "select DIR_ID from iufo_reportdir where DIR_NAME='"
				+ name + "'";
		String DIR_ID = "";
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
				DIR_ID = rs.getString("DIR_ID");
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		} finally {
			closeAll();
		}
		return DIR_ID;
	}

	public void getBlob(String id, String datasource) throws SQLException,
			IOException {
		String sql = "select repinfo from iufo_report where id = '" + id
				+ "' for update";
		String DIR_ID = "";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				BLOB blob = (BLOB) rs.getObject("repinfo");
				// java.sql.Blob blob= (java.sql.Blob)obj;

				byte[] base64;
				String newStr = "";

				base64 = org.apache.commons.io.IOUtils.toByteArray(blob
						.getBinaryStream());
				// newStr=new BASE64Encoder().encodeBuffer(base64);
				newStr = new String(base64, "UTF-8");
				System.out.print("\n\n\n" + newStr);

				InputStream ins = blob.getBinaryStream();

				// 输出到文件
				String paths = "Ufida_IUFO/blobFile/";
				File path = new File(paths);
				if (!path.exists()) {
					path.mkdirs();
				}
				File file = new File("Ufida_IUFO/blobFile/" + id + ".txt");
				OutputStream fout = new FileOutputStream(file);
				// 下面将BLOB数据写入文件
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = ins.read(b)) != -1) {
					fout.write(b, 0, len);
				}

			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			throw e;
		} finally {
			closeAll();
		}
	}

	public String getReportcode(String repid, String datasource)
			throws Exception {

		String sql = "select distinct reportcode from iufo_report where id = '"
				+ repid + "'";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("reportcode");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}

		return null;
	}

	public String[] getUnitId(String unit_id, String datasource)
			throws Exception {

		String sql = "select unit_name , unit_code from iufo_unit_info where unit_id = '"
				+ unit_id + "'";
		String[] retStr = new String[2];
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				retStr[0] = rs.getString("unit_name");
				retStr[1] = rs.getString("unit_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}

		return retStr;
	}

	public List<KeywordVO> getKeywords(String datasource) {
		String sql = "select * from iufo_keyword where isprivate = 'N' and note is null";
		List<KeywordVO> keywords = new ArrayList<KeywordVO>();
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

	public int insertKeyword(KeywordVO keyword, String datasource) {
		String sql = "insert into iufo_keyword(code , isbuiltin , isprivate , isseal , key_count , len , name , note , pk_keyword , ref_pk , rep_id , time_div , type , ver)"
				+ " values('' , '"
				+ keyword.getIsbuiltin()
				+ "' , 'N' , '"
				+ keyword.getIsseal()
				+ "' , "
				+ keyword.getKey_count()
				+ " , "
				+ keyword.getLen()
				+ " , '"
				+ keyword.getName()
				+ "' , '' , '"
				+ keyword.getPk_keyword()
				+ "'"
				+ " , '' , '' , empty_blob() , "
				+ keyword.getType()
				+ " , '"
				+ keyword.getVer() + "')";
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
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return 0;

	}

	public String getTaskPk(String name , String datasource) throws SQLException {
		try {
			String sql = "select ID from iufo_task where name = '"+name+"'";

			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			if (rs.next())
				return rs.getString("ID");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeAll();
		}

		return null;
	}

	public String getRepId(String repCode, String repName, String datasource)
			throws SQLException {
		try {
			String sql = "select ID from iufo_report where name = '" + repCode
					+ "' and reportcode = '" + repName + "'";
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			if (rs.next())
				return rs.getString("ID");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeAll();
		}

		return null;
	}

	public int getPosition(String name , String datasource) {
		try {
			String taskId = this.getTaskPk(name , datasource);
			String sql = "select max(position) as position from iufo_taskset where ID = '"
					+ taskId + "'";
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			if (rs.next())
				return rs.getInt("position") + 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeAll();
		}

		return 0;
	}

	public int insertTaskSet(String name , String ID, String RepID, String datasource)
			throws SQLException {
		String sql = "insert into iufo_taskset(ID , POSITION , REP_ID , SEPGROUP)"
				+ " values('"
				+ ID
				+ "' , '"
				+ this.getPosition(name , datasource)
				+ "' , '" + RepID + "' , 0)";
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);

			if (stmt.execute())
				return 1;
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			closeAll();
		}
		return 0;

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

	public String getUnit_code(String unitid, String datasource) {
		String sql = "select unit_code from iufo_unit_info where unit_id = '"
				+ unitid + "'";

		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("unit_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}

		return null;
	}

	public List<String> getTimeList(String repid, String datasource) {
		String sql = "select distinct inputdate from iufo_measure_pubdata where alone_id in(  select Aloneid from iufo_checkresult where repid = '"
				+ repid + "')";
		List<String> timeList = new ArrayList<String>();
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
				timeList.add(rs.getString("inputdate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}

		return timeList;
	}
	
	public boolean delRepCommit(String whereSql , String datasource){
		String sql = "delete from iufo_report_commit where " + whereSql.substring(0 , whereSql.length() - 3);
		try {
			if ("".equals(datasource)) {
				conn = ConnectionFactory.getConnection();
			} else {
				// conn=getfdhConn();
				conn = ConnectionFactory.getConnection(datasource);
			}
			stmt = conn.prepareStatement(sql);
			return stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeAll();
		}
		
		return false;
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
