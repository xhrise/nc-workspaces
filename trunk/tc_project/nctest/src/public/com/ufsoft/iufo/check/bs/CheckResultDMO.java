package com.ufsoft.iufo.check.bs;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.iufo.toolkit.DatabaseNames;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.crossdb.CrossDBResultSet;
import nc.jdbc.framework.exception.DbException;
import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.pub.GlobalValue;
import nc.vo.iufo.pub.IDatabaseNames;
import nc.vo.iufo.pub.IDatabaseType;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.check.vo.CheckNoteVO;
import com.ufsoft.iufo.check.vo.CheckResultVO;
/**
 * 审核结果DMO.
* @update
* 20031105
* 创建审核结果时，需要判断审核时间是否为空
* @end
* @update
* 20031016
* 修改错误：审核结果查询时，需要对aloneId进行过滤
* @end
*/
public class CheckResultDMO extends nc.vo.iufo.pub.DataManageObjectIufo{

   /**
	* @roseuid 3BE5E82E0374
	*/
   public CheckResultDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException{
   }
   
	/**
	 * 此处插入方法描述。
	 * 创建日期：(2002-6-3 11:57:29)
	 * @param dbName java.lang.String
	 */
	public CheckResultDMO(String dbName)
	    throws javax.naming.NamingException, nc.bs.pub.SystemException {
	    super(dbName);
	}
	/**
	 * 在数据库表审核结果表中增加新记录
	 * @param result
	 * @throws SQLException
	 */
	public void creatCheckResult(CheckResultVO resultVO) throws SQLException {
		if (resultVO==null)
			return;
		
		creatCheckResults(new CheckResultVO[]{resultVO});
	}
	
	/**
	 * 在数据库表审核结果表中增加新记录(数组的接口)
	 * @param results
	 * @throws SQLException
	 */
	public void creatCheckResults(CheckResultVO[] results) throws SQLException {
	    Connection con =null;
	    PreparedStatement prepStmt=null;
	    try{
	    	con=getConnection();
	
	    	//先删除掉原来的审核结果
	    	String[][] strDeleteSQLs=new String[2][2];
		    strDeleteSQLs[0][0]="delete from "+DatabaseNames.IUFO_CHECK_RESULT_NOTE +" where pk_checkresult in(" 
		    				   +"select pk_checkresult from "+DatabaseNames.IUFO_CHECK_RESULT+" where aloneid = ? and taskid = ? and repid is null)";
		    strDeleteSQLs[0][1]="delete from "+DatabaseNames.IUFO_CHECK_RESULT+" where aloneid = ? and taskid = ? and repid is null";
		    strDeleteSQLs[1][0]="delete from "+DatabaseNames.IUFO_CHECK_RESULT_NOTE +" where pk_checkresult in(" 
				   			   +"select pk_checkresult from "+DatabaseNames.IUFO_CHECK_RESULT+" where aloneid = ? and repid = ?)";
		    strDeleteSQLs[1][1]="delete from "+DatabaseNames.IUFO_CHECK_RESULT+" where aloneid= ? and repid = ? ";
		    
//		    SQLParameter param =null;
//		    JdbcSession session = getJdbcsession();
		    
		    for (int i=0;i<2;i++){
			    for (int j=0;j<results.length;j++){
			    	CheckResultVO resultVO=results[j];
			    	
			    	String strSQL=null;
			    	if (resultVO.getRepId()==null || resultVO.getRepId().trim().length()<=0){
			    		strSQL=strDeleteSQLs[0][i];
			    		
			    		if (resultVO.getTaskId()==null || resultVO.getTaskId().trim().length()<=0){
			    			try{
			    				throw new NullPointerException();
			    			}catch(Exception e){
			    				AppDebug.error(e);
			    			}
			    		}
			    	}
			    	else
			    		strSQL=strDeleteSQLs[1][i];
			    	
			    	
			    	prepStmt=con.prepareStatement(strSQL);
			    	prepStmt.setString(1, resultVO.getAloneId());
					if (resultVO.getRepId() == null || resultVO.getRepId().trim().length()<=0)
						prepStmt.setString(2,resultVO.getTaskId());
					else
						prepStmt.setString(2,resultVO.getRepId());
					
					prepStmt.executeUpdate();
					prepStmt.close();
					prepStmt=null;
			    	
//			    	param = new SQLParameter();			    	
//					param.addParam(resultVO.getAloneId());
//					
//					if (resultVO.getRepId() == null || resultVO.getRepId().trim().length()<=0)
//						param.addParam(resultVO.getTaskId());
//					else
//						param.addParam(resultVO.getRepId());
//			    	
//			    	session.addBatch(strSQL, param);
			    }
//			    session.executeBatch();
		    }
		   
		    //再在审核结果主表中插入记录
		    String insertStatement =
		        "insert into "
		            + DatabaseNames.IUFO_CHECK_RESULT
		            + "(pk_checkresult,taskid,aloneid,repid,checktime,checkstate)"
		            + "values(?,?,?,?,?,?)";
	
		    for (int i=0;i<results.length;i++){
		    	CheckResultVO resultVO=results[i];
			    int iCount=0;
			    String strResultPK=null;
			    while (true){
			    	strResultPK=IDMaker.makeID(20);
			    	resultVO.setResultPK(strResultPK);
			    	try{
					    prepStmt = con.prepareStatement(insertStatement);
				        prepStmt.setString(1,strResultPK);
				        prepStmt.setString(2, resultVO.getTaskId());
				        prepStmt.setString(3, resultVO.getAloneId());
				        prepStmt.setString(4, resultVO.getRepId());
				        prepStmt.setString(5, resultVO.getCheckTime());
				        prepStmt.setInt(6, resultVO.getCheckState());
				        prepStmt.executeUpdate();
				        prepStmt.close();
				        prepStmt=null;
				        break;
			    	}catch(SQLException e){
			    		AppDebug.debug(e);
			    		if (iCount==3)
			    			throw e;
			    		iCount++;
			    	}
			    }
	    	}
		    
		    //再插入审核明细记录
		    for (int i=0;i<results.length;i++){
		    	CheckResultVO resultVO=results[i];
			    if (resultVO.getCheckNote()!=null && resultVO.getCheckNote().size()>0){
			    	insertStatement =
				        "insert into "
				            + DatabaseNames.IUFO_CHECK_RESULT_NOTE
				            + "(pk_checkresult,formula_id,checkstate,note,position)"
				            + "values(?,?,?,?,?)";
			    	
			    	for (int j=0;j<resultVO.getCheckNote().size();j++){
			    		CheckNoteVO checkNote=resultVO.getCheckNote().get(j);
			    		
				    	prepStmt=con.prepareStatement(insertStatement);
				    	prepStmt.setString(1, resultVO.getResultPK());
				    	prepStmt.setString(2,checkNote.getFormulaID());
				    	prepStmt.setInt(3,checkNote.getCheckState());
						prepStmt.setBytes(4,convertObjectToBytes(checkNote.getNote()));
						prepStmt.setInt(5,j);
						
						prepStmt.executeUpdate();
						prepStmt.close();
						prepStmt=null;
				        
//				    	param = new SQLParameter();			    	
//						param.addParam(resultVO.getResultPK());
//						param.addParam(checkNote.getFormulaID());
//						param.addParam(checkNote.getCheckState());
//						param.addBlobParam(convertObjectToBytes(checkNote.getNote()));
//						param.addParam(j);
//				    	
//				    	session.addBatch(insertStatement, param);
			    	}
			    }
		    }
		//    session.executeBatch();
	    }
//	    catch(DbException e){
//			AppDebug.debug(e);//@devTools e.printStackTrace();
//			throw new SQLException("creatCheckResults  is error");
//		}
	    finally{
	    	release();
	    	if (prepStmt!=null)
	    		prepStmt.close();
	    	if (con!=null)
	    		con.close();
	    }
	}
	
	/**
	 * 在指标数据保存后，添加数据已录入信息
	 * @param aloneId
	 * @param repIds
	 * @throws SQLException
	 */
	public void creatInputResult(String aloneId, String[] repIds) throws SQLException {
		if (aloneId==null || repIds==null || repIds.length<=0)
			return;
		
		Connection con=null;
		PreparedStatement prepStmt=null;
		ResultSet rs=null;
		try{
		    con = getConnection();
		
		    String selectStatement = "select repid from " + DatabaseNames.IUFO_CHECK_RESULT + " where aloneid = ? and repid  in(";
		    for (int i=0;i<repIds.length;i++){
		    	selectStatement+="'"+repIds[i]+"'";
		    	if (i==repIds.length-1)
		    		selectStatement+=")";
		    	else
		    		selectStatement+=",";
		    }
		
	        prepStmt = con.prepareStatement(selectStatement);
	        prepStmt.setString(1, aloneId);
	
	        rs = prepStmt.executeQuery();
	        List<String> vRepID=new ArrayList<String>();
	        while (rs.next()){
	        	vRepID.add(rs.getString(1));
	        }
	        rs.close();
	        rs=null;
	        prepStmt.close();
	        prepStmt=null;
	        con.close();
	        con=null;
	        
	        List<CheckResultVO> vResult=new ArrayList<CheckResultVO>();
	        for (int i=0;i<repIds.length;i++){
	        	if (vRepID.contains(repIds[i]))
	        		continue;
	        	
	        	CheckResultVO result=new CheckResultVO();
	        	result.setAloneId(aloneId);
	        	result.setCheckState(CheckResultVO.NOCHECK);
	        	result.setRepId(repIds[i]);
	        	vResult.add(result);
	        }
	        
	        if (vResult.size()>0)
	        	creatCheckResults(vResult.toArray(new CheckResultVO[0]));
		}
		finally{
			if (rs!=null)
				rs.close();
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();
		}
	}
	
	/**
	 * 判断aloneId是否已经录入过报表数据
	 * @param aloneId
	 * @param reportIds
	 * @return
	 * @throws SQLException
	 */
	public Boolean isAloneIDInput(String aloneId,String[] reportIds) throws SQLException {
		Connection con=null;
		PreparedStatement prepStmt=null;
		ResultSet rs=null;
		try{
		    con = getConnection();
			
			Vector<String> vRepCond=splitInClause(reportIds);
			if (vRepCond.size()<=0)
				vRepCond.add("");
			for (int i=0;i<vRepCond.size();i++){
				String selectStatement = "select 1 " + " from  " + DatabaseNames.IUFO_CHECK_RESULT + " where aloneid = ? and repid is not null";
				
				String strRepCond=(String)vRepCond.get(i);
				if (strRepCond!=null && strRepCond.trim().length()>0)
					selectStatement+=" and repid in("+vRepCond.get(i)+")";
				
			    prepStmt = con.prepareStatement(selectStatement);
			    prepStmt.setString(1, aloneId);
			    rs = prepStmt.executeQuery();
			    boolean bExist=rs.next();
			    rs.close();
			    rs=null;
			    prepStmt.close();
			    prepStmt=null;
			    
			    if (bExist)
			    	return bExist;
			}
			return false;
		}
		finally{
			if (rs!=null)
				rs.close();
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();
		}
	}

	/**
	 * 根据aloneId，加载该aloneId下审核通过的报表Id
	 * @param aloneId
	 * @return
	 * @throws SQLException
	 */
	public String[] loadPassedRepIdsByAloneId(String aloneId) throws SQLException {
		Connection con=null;
		PreparedStatement prepStmt=null;
		ResultSet rs=null;
	    try {
			con = getConnection();
			String selectStatement = "select distinct repid " + " from  " + DatabaseNames.IUFO_CHECK_RESULT + " where aloneid = ? and checkState = 2 and repid is not null";
	
			prepStmt = con.prepareStatement(selectStatement);
			Vector<String> vecRepIds = new Vector<String>();
			prepStmt.setString(1, aloneId);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
			    vecRepIds.add(rs.getString(1));
			}
			return (String[])vecRepIds.toArray(new String[0]);
		} 
	    finally{
			if (rs!=null)
				rs.close();
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();    	
	    }
	}
	
	/**
	 * 根据aloneId、审核状态加载表内审核结果
	 * @param checkState
	 * @param aloneIds
	 * @param repIds
	 * @return
	 * @throws SQLException
	 */
	public CheckResultVO[] loadRepCheckResults(int checkState, String[] aloneIds, String[] repIds,String strFormulaID) throws SQLException {
		Connection con=null;
		PreparedStatement prepStmt=null;
		ResultSet rs=null;
	    try {
			con = getConnection();
			
			//将aloneid和repid分成多组条件，避免in条件中值过多的问题
			Vector<String> vAloneIDCond=splitInClause(aloneIds);
			Vector<String> vRepIDCond=splitInClause(repIds);
			Hashtable<String,CheckResultVO> hashCheckResult=new Hashtable<String,CheckResultVO>();
			
			for (int i=0;i<vAloneIDCond.size();i++){
				String strAloneCond=(String)vAloneIDCond.get(i);
				for (int j=0;j<vRepIDCond.size();j++){
					String strRepCond=(String)vRepIDCond.get(j);
					
					//因为存在主表有记录而子表没有记录的情况，所以要用外连接，Oracle与其他数据库不相同
					String selectStatement=null;
					if (GlobalValue.DATABASE_TYPE.equalsIgnoreCase(IDatabaseType.DATABASE_ORACLE))
						selectStatement = "select t1.pk_checkresult,t1.aloneid,t1.taskid,t1.repid,t1.checkTime,t1.checkState,t2.formula_id,t2.checkstate,t2.note " 
										+ " from  " + DatabaseNames.IUFO_CHECK_RESULT+" t1,"+DatabaseNames.IUFO_CHECK_RESULT_NOTE+" t2 " 
										+ " where t1.pk_checkresult=t2.pk_checkresult(+) and t1.aloneid in("+strAloneCond+") and t1.repid in("+strRepCond+") ";
					else
						selectStatement = "select t1.pk_checkresult,t1.aloneid,t1.taskid,t1.repid,t1.checkTime,t1.checkState,t2.formula_id,t2.checkstate,t2.note " 
										+ " from  " + DatabaseNames.IUFO_CHECK_RESULT+" t1 left outer join "+DatabaseNames.IUFO_CHECK_RESULT_NOTE+" t2 " 
										+ " on t1.pk_checkresult=t2.pk_checkresult where t1.aloneid in("+strAloneCond+") and t1.repid in("+strRepCond+")";
					
					if (checkState!=CheckResultVO.CHECK_ALLSTATE){
						selectStatement+=" and t1.checkstate="+checkState;
						selectStatement+=" and t2.checkstate="+checkState;
					}
					if (strFormulaID!=null && strFormulaID.trim().length()>0)
						selectStatement+=" and t2.formula_id=?";
					
					selectStatement+=" order by t2.position";

			    	prepStmt = con.prepareStatement(selectStatement);
			    	
					if (strFormulaID!=null && strFormulaID.trim().length()>0)
						prepStmt.setString(1,strFormulaID);
			    	
			        rs = prepStmt.executeQuery();
			        loadCheckResltFromResultSet(rs, hashCheckResult);
			        rs.close();
			        rs=null;
			        prepStmt.close();
			        prepStmt=null;
			    }
			}
			return (CheckResultVO[])hashCheckResult.values().toArray(new CheckResultVO[0]);
		}
	    finally{
			if (rs!=null)
				rs.close();
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();     	
	    }
	}
	
	/**
	 * 根据aloneId，加载该aloneId下已经保存过的报表Id
	 * @param aloneId
	 * @return
	 * @throws SQLException
	 */
	public String[] loadRepIdsByAloneId(String aloneId) throws SQLException {
		Connection con=null;
		PreparedStatement prepStmt=null;
		ResultSet rs=null;
	    try {
			con = getConnection();
			String selectStatement = "select distinct repid " + " from  " + DatabaseNames.IUFO_CHECK_RESULT + " where aloneid = ? and repid is not null";
	
			prepStmt = con.prepareStatement(selectStatement);
			Vector<String> vecRepIds = new Vector<String>();
			prepStmt.setString(1, aloneId);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
			    vecRepIds.add(rs.getString(1));
			}
			String[] repIds = new String[vecRepIds.size()];
			for (int i=0;i<vecRepIds.size();i++){
			    repIds[i] = (String)vecRepIds.get(i);
			}
			return repIds;
		} 
	    finally{
			if (rs!=null)
				rs.close();
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();     	
	    }
	}

	/**
	 * 根据任务id和aloneId、审核状态加载表间审核结果
	 * @param taskId
	 * @param checkState
	 * @param aloneIds
	 * @return
	 * @throws SQLException
	 */
	public CheckResultVO[] loadTaskCheckResults(String taskId,int checkState,String[] aloneIds,String strFormulaID) throws SQLException {
		if (aloneIds==null || aloneIds.length<=0 || taskId==null)
			return null;
		
		Connection con=null;
		PreparedStatement prepStmt=null;
		ResultSet rs=null;
	    try {
			con = getConnection();
			
			//将aloneid分组
			Vector<String> vCondClause=splitInClause(aloneIds);
			
			Hashtable<String,CheckResultVO> hashCheckResult=new Hashtable<String,CheckResultVO>();
			for (int i=0;i<vCondClause.size();i++){
				String selectStatement=null;
				//外连接，处理oracle与其他数据库的不同
				if (GlobalValue.DATABASE_TYPE.equalsIgnoreCase(IDatabaseType.DATABASE_ORACLE))
					selectStatement = "select t1.pk_checkresult,t1.aloneid,t1.taskid,t1.repid,t1.checkTime,t1.checkState,t2.formula_id,t2.checkstate,t2.note " 
									+ " from  " + DatabaseNames.IUFO_CHECK_RESULT+" t1,"+DatabaseNames.IUFO_CHECK_RESULT_NOTE+" t2 " 
									+ " where t1.pk_checkresult=t2.pk_checkresult(+) and t1.aloneid in("+vCondClause.get(i)+") ";
				else
					selectStatement = "select t1.pk_checkresult,t1.aloneid,t1.taskid,t1.repid,t1.checkTime,t1.checkState,t2.formula_id,t2.checkstate,t2.note " 
									+ " from  " + DatabaseNames.IUFO_CHECK_RESULT+" t1 left outer join "+DatabaseNames.IUFO_CHECK_RESULT_NOTE+" t2 " 
									+ " on t1.pk_checkresult=t2.pk_checkresult where t1.aloneid in("+vCondClause.get(i)+") ";
				
				selectStatement+=" and taskid=? ";
				if (checkState!=CheckResultVO.CHECK_ALLSTATE){
					selectStatement+=" and t1.checkstate="+checkState;
					selectStatement+=" and t2.checkstate="+checkState;
				}
				
				if (strFormulaID!=null && strFormulaID.trim().length()>0)
					selectStatement+=" and t2.formula_id=?";
				
				selectStatement+=" order by t2.position";
				
		    	prepStmt = con.prepareStatement(selectStatement);
		    	prepStmt.setString(1, taskId);
		    	
				if (strFormulaID!=null && strFormulaID.trim().length()>0)
					prepStmt.setString(2,strFormulaID);
				
		        rs = prepStmt.executeQuery();
		        loadCheckResltFromResultSet(rs, hashCheckResult);
		        rs.close();
		        rs=null;
		        prepStmt.close();
		        prepStmt=null;
			}
			
			//对返回的记录进行整理，没有找到审核记录的aloneid，其审核结果为未审核
			CheckResultVO[] findResults=hashCheckResult.values().toArray(new CheckResultVO[0]);
			CheckResultVO[] retResults=new CheckResultVO[aloneIds.length];
			for (int i=0;i<aloneIds.length;i++){
				for (int j=0;j<findResults.length;j++){
					if (findResults[j].getAloneId().equals(aloneIds[i])){
						retResults[i]=findResults[j];
						break;
					}
				}
				if (retResults[i]==null && (checkState==CheckResultVO.CHECK_ALLSTATE || checkState == CheckResultVO.NOCHECK)){
					retResults[i]=new CheckResultVO();
					retResults[i].setAloneId(aloneIds[i]);
					retResults[i].setTaskId(taskId);
					retResults[i].setCheckState(CheckResultVO.NOCHECK);
				}
			}
			return retResults;
		} 
	    finally{
			if (rs!=null)
				rs.close();
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();     	
	    }
	}
	
	/**
	 * 从结果集中加载审核结果内容，一个审核结果对应结果集的多条记录
	 * @param rs
	 * @param hashCheckResult
	 * @throws SQLException
	 */
	private void loadCheckResltFromResultSet(ResultSet rs,Hashtable<String,CheckResultVO> hashCheckResult) throws SQLException{
        while (rs.next()) {
        	String strResultPK=rs.getString(1);
        	
        	//得到主表记录
        	CheckResultVO result=hashCheckResult.get(strResultPK);
        	if (result==null){
        		result=new CheckResultVO();
        		hashCheckResult.put(strResultPK, result);
        		
        		result.setResultPK(strResultPK);
        		result.setAloneId(rs.getString(2));
        		result.setTaskId(rs.getString(3));
        		result.setRepId(rs.getString(4));
        		result.setCheckTime(rs.getString(5));
        		result.setCheckState(rs.getInt(6));
        	}
        	
        	String strFormulaID=rs.getString(7);
        	if (strFormulaID==null || strFormulaID.trim().length()<=0)
        		continue;
        	
        	//得到审核明细记录
        	CheckNoteVO checkNote=new CheckNoteVO();
        	checkNote.setFormulaID(strFormulaID);
        	checkNote.setCheckState(rs.getInt(8));
        	checkNote.setNote(getResultNote(rs,9));
        	
        	List<CheckNoteVO> vCheckNote=result.getCheckNote();
        	if (vCheckNote==null){
        		vCheckNote=new ArrayList<CheckNoteVO>();
        		result.setNote(vCheckNote);
        	}
        	vCheckNote.add(checkNote);
		}
	}
	
	/**
	 * 将指定AloneId的审核结果删除
	 * @param sAloneIDs
	 * @throws SQLException
	 */
	public void removeByAloneIds(String[] sAloneIDs) throws SQLException {
		if(sAloneIDs == null || sAloneIDs.length == 0)
			return;
		Connection con=null;
	    PreparedStatement stmt = null;
		try{
		    con = getConnection();
	
		    Vector vAloneCond=splitInClause(sAloneIDs);
	    	for (int i=0;i<vAloneCond.size();i++){
	    		String strSQL="delete from "+DatabaseNames.IUFO_CHECK_RESULT_NOTE+" where pk_checkresult in(select pk_checkresult from "+DatabaseNames.IUFO_CHECK_RESULT+ " where aloneid in("+vAloneCond.get(i)+"))";
	    		stmt = con.prepareStatement(strSQL);
	    		stmt.executeUpdate();
	    		stmt.close();
	    		stmt=null;
	    		
	    		strSQL = "delete from " + DatabaseNames.IUFO_CHECK_RESULT + " where aloneid in("+vAloneCond.get(i)+")";
	    		stmt = con.prepareStatement(strSQL);
	    		stmt.executeUpdate();
	    		stmt.close();
	    		stmt=null;
	    	}
	    } finally {
	        if (stmt != null)
	            stmt.close();
	        if (con != null)
	            con.close();
	    }
	}
	/**
	 * 将指定报表id和AloneId的审核结果删除
	 * @param repId
	 * @param aloneId
	 * @throws SQLException
	 */
	public void removeByRepIdAloneId(String repId, String aloneId) throws SQLException {
		removeByRepIdAloneIds(repId,new String[]{aloneId});
	}
	/**
	 * 将指定报表id和AloneId的审核结果删除
	 * @param repId
	 * @param aloneIds
	 * @throws SQLException
	 */
	public void removeByRepIdAloneIds(String repId,String[] aloneIds) throws SQLException {
		Connection con=null;
		PreparedStatement prepStmt=null;
		try{
		    con = getConnection();
		    
		    Vector vAloneCond=splitInClause(aloneIds);
		    for (int i = 0; i < vAloneCond.size(); i++) {
		    	String strSQL="delete from "+DatabaseNames.IUFO_CHECK_RESULT_NOTE+" where pk_checkresult in(select pk_checkresult from " + DatabaseNames.IUFO_CHECK_RESULT + " where  repid = ? and aloneid in("+vAloneCond.get(i)+"))";
		    	prepStmt = con.prepareStatement(strSQL);
		    	prepStmt.setString(1,repId);
		        prepStmt.executeUpdate();
		        prepStmt.close();
		        prepStmt=null;
		        
		    	strSQL="delete from " + DatabaseNames.IUFO_CHECK_RESULT + " where  repid = ? and aloneid in("+vAloneCond.get(i)+")";
		    	prepStmt = con.prepareStatement(strSQL);
		    	prepStmt.setString(1,repId);
		        prepStmt.executeUpdate();
		        prepStmt.close();
		        prepStmt=null;
		    }
		}
		finally{
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();
		}
	}
	
	public void removeTaskCheckResultsByRepId(String strRepID,String strAloneID) throws SQLException{
		Connection con=null;
		PreparedStatement prepStmt=null;
		try{
			con = getConnection();

	    	String strSQL="delete from "+DatabaseNames.IUFO_CHECK_RESULT_NOTE+" where pk_checkresult in(select pk_checkresult from " + DatabaseNames.IUFO_CHECK_RESULT + " where aloneid=? and repid is null and  taskid in(select id from "+IDatabaseNames.IUFO_TASKSET_TABLE+" where rep_id=?))";
	    	prepStmt = con.prepareStatement(strSQL);
	    	prepStmt.setString(1, strAloneID);
	    	prepStmt.setString(2, strRepID);
	        prepStmt.executeUpdate();
	        prepStmt.close();
	        prepStmt=null;
	        
	    	strSQL ="delete from " + DatabaseNames.IUFO_CHECK_RESULT + " where aloneid=? and repid is null and  taskid in(select id from "+IDatabaseNames.IUFO_TASKSET_TABLE+" where rep_id=?)";
	    	prepStmt = con.prepareStatement(strSQL);
	    	prepStmt.setString(1, strAloneID);
	    	prepStmt.setString(2, strRepID);	    	
	        prepStmt.executeUpdate();
	        prepStmt.close();
	        prepStmt=null;
		}
		finally{
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();
		}
	}

	/**
	 * 将指定任务的审核结果删除
	 * @param taskIds
	 * @throws SQLException
	 */
	public void removeByTaskIds(String[] taskIds) throws SQLException {
		Connection con=null;
		PreparedStatement prepStmt=null;
		try{
			con = getConnection();

			Vector vTaskCond=splitInClause(taskIds);
		    for (int i = 0; i < vTaskCond.size(); i++) {
		    	String strSQL="delete from "+DatabaseNames.IUFO_CHECK_RESULT_NOTE+" where pk_checkresult in(select pk_checkresult from " + DatabaseNames.IUFO_CHECK_RESULT + " where repid is null and  taskid in("+vTaskCond.get(i)+"))";
		    	prepStmt = con.prepareStatement(strSQL);
		        prepStmt.executeUpdate();
		        prepStmt.close();
		        prepStmt=null;
		        
		    	strSQL ="delete from " + DatabaseNames.IUFO_CHECK_RESULT + " where repid is null and  taskid in("+vTaskCond.get(i)+")";
		    	prepStmt = con.prepareStatement(strSQL);
		        prepStmt.executeUpdate();
		        prepStmt.close();
		        prepStmt=null;
		    }
		}
		finally{
			if (prepStmt!=null)
				prepStmt.close();
			if (con!=null)
				con.close();
		}
	}
	
	/**
	 * 得到审核结果提示
	 * @param rs
	 * @param iCol
	 * @return
	 */
	private String getResultNote(ResultSet rs,int iCol){
        byte[] objBytes = null;
        try{
            objBytes = ( (CrossDBResultSet)rs).getBlobBytes(iCol);
        } 
        catch(NegativeArraySizeException e){
        } 
        catch(Exception e){
        }
        String strMsg=null;
        if(objBytes != null && objBytes.length > 0){
        	strMsg = (String)convertBytesToObject(objBytes);
        }		
        return strMsg;
	}
	
	private Vector<String> splitInClause(String[] strVals){
		Vector<String> vRet=new Vector<String>();
		if (strVals==null || strVals.length<=0)
			return vRet;
		
		int iLimit=200;
		int iPos=0;
		String strCond="";
		for (int i=0;i<strVals.length;i++){
			strCond+="'"+strVals[i]+"'";
			if(iPos==iLimit || i==strVals.length-1){
				iPos=0;
				vRet.add(strCond);
				strCond="";
			}
			else{
				strCond+=",";
				iPos++;
			}
		}
		
		return vRet;
	}
}
