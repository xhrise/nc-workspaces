package nc.itf.hskaoqin.all;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import nc.vo.hskaoqin.all.ImpDataVO;


public interface IBasDMO {
	public Vector<ImpDataVO> getGetDataVOs(String startTime,String endTime,String datasource) throws Exception;
	public String getStr(String sql, String datasource) throws Exception;
	public ArrayList<String> getStrs(String sql, String datasource) throws Exception;
	public boolean executeSql(String sql, String datasource) throws Exception;
	public void closeAll();
	public void closeAll(Connection conn, PreparedStatement stmt, ResultSet rs);
	
}
