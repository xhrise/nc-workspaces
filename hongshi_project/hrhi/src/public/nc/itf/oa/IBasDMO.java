package nc.itf.oa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IBasDMO {
	public int checkOANameRep(String OAName, String datasource) throws Exception;
	public void closeAll();
	public void closeAll(Connection conn, PreparedStatement stmt, ResultSet rs);
	public String getStr(String sql, String datasource) throws SQLException;
}
