package nc.bs.iufo.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.iufo.cache.IUFOBSCacheManager;
import nc.bs.iufo.toolkit.DatabaseNames;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.vo.iufo.data.MeasureDataVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.iufo.pub.GlobalValue;
import nc.vo.iufo.pub.IDatabaseType;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.total.TotalCellKeyValue;


public class MeasureDataDMO extends DataManageObjectIufo {

	public MeasureDataDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super();
	}

	public MeasureDataDMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super(dbName);
	}
	
	/**
	 * 生成含有私有关键字的动态区的汇总数据
	 * @param strTable,指标数据表
	 * @param strCond，汇总条件的SQL语句
	 * @param vMeasure，参与汇总，对应该指标数据表的指标
	 * @param pubData,主表的MeasurePubDataVO
	 * @param strMeasKeyGroupPK，指标对应的关键字组合PK
	 * @throws SQLException
	 */
	public void createTotalDynDatas(String strTable, String strCond, List<MeasureVO> vMeasure, MeasurePubDataVO pubData,
			String strMeasKeyGroupPK) throws SQLException {
		Connection con=null; 
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			
			//判断动态区是否仅有行号关键字
			boolean bOnlyLineKey = false;
			KeyGroupCache kgCache = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
			KeyVO[] keys = kgCache.getByPK(strMeasKeyGroupPK).getKeys();
			KeyVO[] mainKeys = pubData.getKeyGroup().getKeys();
			if (keys != null && mainKeys != null && keys.length == mainKeys.length + 1) {
				for (int i = 0; i < keys.length; i++) {
					if (keys[i].isPrivate() && keys[i].getName().equals("行号")) {
						bOnlyLineKey = true;
						break;
					}
				}
			}			
			
			// 因为line_no不能自动生成，所以不能直接用insert into select语句实现，需要将结果先取出来，
			// 再用insert语句插入
			// 拼insert语句
			StringBuffer insertBufSQL = new StringBuffer();
			insertBufSQL.append("insert into ");
			insertBufSQL.append(strTable);
			insertBufSQL.append("(line_no,alone_id,pk_key_comb,");
			
			// 拼select语句
			StringBuffer selBufSQL = new StringBuffer();
			selBufSQL.append("select ");
			selBufSQL.append("'" + pubData.getAloneID() + "',");
			selBufSQL.append("pk_key_comb,");
			
			String strParam = "?,?,?,";
			int iCount = 2;
			int iMeasStartPos=3;
			
			// 拼私有关键字列
			for (int i = 0; i < 10; i++,iCount++) {
				insertBufSQL.append("keyvalue" + (i + 1) + ",");
				strParam += "?,";
				
				if (bOnlyLineKey == false){
					selBufSQL.append("keyvalue" + (i + 1) + ",");
					iMeasStartPos++;
				}
			}

			// 拼指标列
			for (int i = 0; i < vMeasure.size(); i++,iCount++) {
				MeasureVO measure = (MeasureVO) vMeasure.get(i);
				insertBufSQL.append(measure.getDbcolumn());
				strParam += "?";
				
				if (bOnlyLineKey == false){
					if (measure.getType()==MeasureVO.TYPE_NUMBER)
						selBufSQL.append("sum(" + measure.getDbcolumn() + ")");
					else
						selBufSQL.append("max(" + measure.getDbcolumn() + ")");
				}
				else
					selBufSQL.append(measure.getDbcolumn());
			
				if (i < vMeasure.size() - 1) {
					insertBufSQL.append(",");
					strParam += ",";
					selBufSQL.append(",");		
				}
			}

			// 如果仅有行号关键字，私有关键字列都不用考虑，列数减10
			if (bOnlyLineKey)
				iCount -= 10;

			insertBufSQL.append(") values(");
			insertBufSQL.append(strParam);
			insertBufSQL.append(")");

			selBufSQL.append(" from ");
			selBufSQL.append(strTable + " t1,");
			selBufSQL.append(strCond+ " t2 ");

			selBufSQL.append(" where t1.alone_id=t2.alone_id  and ");
			selBufSQL.append(genWhereSQL(pubData, "t2", false));

			// 按私有关键字进行分组
			if (bOnlyLineKey == false) {
				selBufSQL.append(" group by pk_key_comb ");
				for (int i = 0; i < 10; i++)
					selBufSQL.append(",keyvalue" + (i + 1));
			}

			// 记录下查找出来的结果
			stmt1 = con.prepareStatement(selBufSQL.toString());
			rs = stmt1.executeQuery();
			Vector<Vector<Object>> vValue = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> vOneValue = new Vector<Object>();
				for (int i = 1; i <= iCount; i++) {
					if (i == 1)
						vOneValue.add(pubData.getAloneID());//alone_id
					else if (i <= 2)
						vOneValue.add(rs.getString(i));//pk_key_comb
					else {
						//关键字
						if (bOnlyLineKey == false && i <= 12)
							vOneValue.add(rs.getString(i));
						//数值型指标或字符型指标的值
						else{
							MeasureVO measure=vMeasure.get(i-iMeasStartPos);
							if (measure.getType()==MeasureVO.TYPE_NUMBER)
								vOneValue.add(new Double(rs.getDouble(i)));
							else
								vOneValue.add(rs.getString(i));
						}
					}
				}
				vValue.add(vOneValue);
			}
			rs.close();
			rs = null;
			stmt1.close();
			stmt1 = null;

			// 插入语句时，即使只有行号关键字，也需要考虑私有关键字
			if (bOnlyLineKey)
				iCount += 10;

			// 执行插入操作
			stmt2 = con.prepareStatement(insertBufSQL.toString());
			int iBatchCount = 0;
			for (int i = 0; i < vValue.size(); i++) {
				// 设置行号
				stmt2.setInt(1, i + 1);

				Vector vOneValue = (Vector) vValue.elementAt(i);
				for (int j = 1; j <= iCount; j++) {
					if (j <= 2)// 设置alone_id
						stmt2.setString(j + 1, (String) vOneValue.elementAt(j - 1));
					else {
						// 如果只有行号关键字，私有关键字的值不用设
						int iIndex = j - 1;
						if (bOnlyLineKey)
							iIndex = j - 1 - 10;

						if (j <= 12) {
							// 如果只有行号关键字，除了keyword1外私有关键字的值不用设，keyword1的值和line_no相同
							if (bOnlyLineKey) {
								if (j == 3)
									stmt2.setString(j + 1, "" + (i + 1));
								else
									stmt2.setString(j + 1, null);
							} else
								// 设置私有关键字的值
								stmt2.setString(j + 1, (String) vOneValue.elementAt(iIndex));
						} else{
							//数值型指标或字符型指标的值
							MeasureVO measure=vMeasure.get(j-iMeasStartPos);
							if (measure.getType()==MeasureVO.TYPE_NUMBER)
								stmt2.setDouble(j + 1, ((Double) vOneValue.elementAt(iIndex)).doubleValue());
							else
								stmt2.setString(j + 1, (String) vOneValue.elementAt(iIndex));
						}
					}
				}
				stmt2.addBatch();

				// 一次批处理100条记录
				if (iBatchCount++ == 100 || i == vValue.size() - 1) {
					stmt2.executeBatch();
					iBatchCount = 0;
					stmt2.clearBatch();
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt1 != null)
				stmt1.close();
			if (stmt2 != null)
				stmt2.close();
			if (con!=null)
				con.close();
		}
	}	
	
	/**
	 * 对于不属于参加汇总的报表所有的指标，进行汇总下级
	 * @param strTable，指标数据表
	 * @param strCond，汇总筛选条件SQL语句
	 * @param vMeasure，参加汇总的指标
	 * @param mainPubData，主表的MeasurePubDataVO
	 * @param vSubPubData,需要生成的子表的MeasurePubDataVO数组
	 * @throws SQLException
	 */
	public void createTotalNoDynDatasWithHZSub(String strTable, String strCond, List<MeasureVO> vMeasure,
			MeasurePubDataVO mainPubData, List<MeasurePubDataVO> vSubPubData) throws SQLException {
		// 将MeasurePubDataVO分组成需要用insert和用update语句生成的
		Vector<Vector<MeasurePubDataVO>> vRetPubData = splitPubDataByExistRecord(strTable, vSubPubData);

		// 调用update语句生成
		if (vRetPubData.get(0) != null && vRetPubData.get(0).size() > 0)
			createTotalNoDynDatasByUpdate(strTable, strCond, vMeasure, mainPubData, vRetPubData.get(0));

		// 调用insert语句生成
		if (vRetPubData.get(1) != null && vRetPubData.get(1).size() > 0)
			createTotalNoDynDatasByInsert(strTable, strCond, vMeasure, mainPubData, vRetPubData.get(1));
	}
	
	/**
	 * 数据库中已经存在该行记录，需要用update语句来更新值
	 * @param strTable，指标数据表
	 * @param strCond，汇总筛选条件SQL语句
	 * @param vMeasure，参加汇总的指标
	 * @param mainPubData，主表的MeasurePubDataVO
	 * @param vSubPubData，需要生成的子表的MeasurePubDataVO数组
	 * @throws SQLException
	 */
	public void createTotalNoDynDatasByUpdate(String strTable, String strCond, List<MeasureVO> vMeasure,
			MeasurePubDataVO mainPubData, List<MeasurePubDataVO> vSubPubData) throws SQLException {
		Connection con =null;
		Statement selStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet set = null;
		try {
			con=getConnection();
			
			boolean bNumberMeas=vMeasure.get(0).getType()==MeasureVO.TYPE_NUMBER;
			
			// 先利用select语句查找出记录，再用update语句来更新
			MeasurePubDataVO subPubData = (MeasurePubDataVO) vSubPubData.get(0);
			boolean[] bTimeType = getKeywordTimeType(mainPubData, subPubData);
			boolean bDayTime = bTimeType[2];

			// 生成update语句SQL语句，sum列的SQL语句
			StringBuffer selSumMeasSQL = new StringBuffer();
			StringBuffer updateBufSQL = new StringBuffer();
			updateBufSQL.append("update ");
			updateBufSQL.append(strTable);
			updateBufSQL.append(" set ");

			for (int i = 0; i < vMeasure.size(); i++) {
				MeasureVO measure =vMeasure.get(i);

				updateBufSQL.append(measure.getDbcolumn() + "=?");
				
				if (bNumberMeas)
					selSumMeasSQL.append("sum(" + measure.getDbcolumn() + ")");
				else
					selSumMeasSQL.append(measure.getDbcolumn());
				
				if (i < vMeasure.size() - 1) {
					updateBufSQL.append(",");
					selSumMeasSQL.append(",");
				}
			}
			updateBufSQL.append(" where alone_id=? and line_no=? ");

			// 记录查找出的结果
			Vector<Vector<Object>> vData = new Vector<Vector<Object>>();
			Vector<List<MeasurePubDataVO>> vSplitSubPubData = splitObject(vSubPubData, (bDayTime == true ? 1 : 100));
			for (int i = 0; i < vSplitSubPubData.size(); i++) {
				vSubPubData =vSplitSubPubData.get(i);

				subPubData = (MeasurePubDataVO) vSubPubData.get(0);
				String strAloneID = subPubData.getAloneID();

				// 拼select语句
				StringBuffer selBufSQL = new StringBuffer();
				selBufSQL.append("select ");

				if (subPubData.getKType().equals(mainPubData.getKType()) || bDayTime == true)
					selBufSQL.append("'" + strAloneID + "',");
				else
					selBufSQL.append("t5.alone_id,");
				
				selBufSQL.append("0,");

				selBufSQL.append(selSumMeasSQL.toString());
				selBufSQL.append(getTotalJoinCond(strTable, strCond, mainPubData, subPubData, vSubPubData,
						bTimeType[0], bTimeType[1], bTimeType[2],false,bNumberMeas,true));

				selStmt = con.createStatement();
				set = selStmt.executeQuery(selBufSQL.toString());
				Hashtable<String,Boolean> hashUsedData=new Hashtable<String,Boolean>();
				while (set != null && set.next()) {
					// 记录查找出的一行的结果
					Vector<Object> vOneData = new Vector<Object>();

					vOneData.add(set.getString(1));
					vOneData.add(new Integer(set.getInt(2)));
					for (int j = 0; j < vMeasure.size(); j++){
						if (bNumberMeas)
							vOneData.add(new Double(set.getDouble(j + 3)));
						else
							vOneData.add(set.getString(j+3));
					}

					if (bNumberMeas)
						vData.add(vOneData);
					else{
						if (hashUsedData.get(vOneData.get(0)+"\r\n"+vOneData.get(1))==null){
							vData.add(vOneData);
							hashUsedData.put((String)vOneData.get(0)+"\r\n"+vOneData.get(1),Boolean.TRUE);
						}
					}
				}
				set.close();
				set = null;
				selStmt.close();
				selStmt = null;
			}

			// 执行update操作，用批处理实现
			int iBatchCount = 0;
			updateStmt = con.prepareStatement(updateBufSQL.toString());
			for (int i = 0; i < vData.size(); i++) {
				Vector vOneData = (Vector) vData.get(i);
				for (int j = 2; j < vOneData.size(); j++) {
					if (bNumberMeas)
						updateStmt.setDouble(j-1, ((Double) vOneData.get(j)).doubleValue());
					else
						updateStmt.setString(j-1,(String)vOneData.get(j));
				}

				updateStmt.setString(vMeasure.size() + 1, (String) vOneData.get(0));
				updateStmt.setInt(vMeasure.size()+2,((Integer)vOneData.get(1)).intValue());
				updateStmt.addBatch();

				if (iBatchCount++ == 100 || i == vData.size() - 1) {
					updateStmt.executeBatch();
					iBatchCount = 0;
					updateStmt.clearBatch();
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (set!=null)
				set.close();
			if (selStmt != null)
				selStmt.close();
			if (updateStmt != null)
				updateStmt.close();
			if (con!=null)
				con.close();
		}
	}
	
	/**
	 * 使用插入记录的方法，生成公有关键字的动态区的汇总记录
	 * @param strTable
	 * @param strCond
	 * @param vMeasure
	 * @param mainPubData
	 * @param vSubPubData
	 * @throws SQLException
	 */
	public void createTotalNoDynDatasByInsert(String strTable, String strCond, List<MeasureVO> vMeasure,
			MeasurePubDataVO mainPubData, List<MeasurePubDataVO> vSubPubData) throws SQLException {
		//插入新记录
		innerCreateTotalNoDynDatasByInsert(strTable, strCond, vMeasure, mainPubData, vSubPubData);
		
		//如果是字符型指标，汇总值并未生成，用update方法生成内容
		boolean bNumberMeas=vMeasure.get(0).getType()==MeasureVO.TYPE_NUMBER;
		if (!bNumberMeas){
			createTotalNoDynDatasByUpdate(strTable, strCond, vMeasure, mainPubData, vSubPubData);
		}
	}

	/**
	 * 数据库中不存在该行记录，用insert语句来插入新记录
	 * @param strTable，指标数据表
	 * @param strCond，汇总筛选条件SQL语句
	 * @param vMeasure，参加汇总的指标
	 * @param mainPubData，主表的MeasurePubDataVO
	 * @param vSubPubData，需要生成的子表的MeasurePubDataVO数组
	 * @throws SQLException
	 */
	private void innerCreateTotalNoDynDatasByInsert(String strTable, String strCond, List<MeasureVO> vMeasure,
			MeasurePubDataVO mainPubData, List<MeasurePubDataVO> vSubPubData) throws SQLException {
		Connection con =null;
		Statement stmt = null;
		try {
			con=getConnection();
			stmt = con.createStatement();
			
			//字符型指标插入空值
			boolean bNumberMeas=vMeasure.get(0).getType()==MeasureVO.TYPE_NUMBER;

			MeasurePubDataVO subPubData = (MeasurePubDataVO) vSubPubData.get(0);
			boolean[] bTimeType = getKeywordTimeType(mainPubData, subPubData);
			boolean bDayTime = bTimeType[2];

			Vector<List<MeasurePubDataVO>> vSplitSubPubData = splitObject(vSubPubData, (bDayTime == true ? 1 : 100));
			int iBatchCount = 0;
			for (int i = 0; i < vSplitSubPubData.size(); i++) {
				vSubPubData =vSplitSubPubData.get(i);

				subPubData = (MeasurePubDataVO) vSubPubData.get(0);
				String strAloneID = subPubData.getAloneID();

				// 拼接insert into select语句
				StringBuffer bufSQL = new StringBuffer();
				bufSQL.append("insert into ");
				bufSQL.append(strTable);
				bufSQL.append("(line_no,alone_id,pk_key_comb,");

				for (int j = 0; j < vMeasure.size(); j++) {
					MeasureVO measure = (MeasureVO) vMeasure.get(j);
					bufSQL.append(measure.getDbcolumn());

					if (j < vMeasure.size() - 1)
						bufSQL.append(",");
				}

				bufSQL.append(") ");

				StringBuffer selBufSQL = new StringBuffer();
				
				StringBuffer colBufSQL=new StringBuffer();
				colBufSQL.append("0,");

				if (subPubData.getKType().equals(mainPubData.getKType()) || bDayTime == true) {
					colBufSQL.append("'" + strAloneID + "',");
					colBufSQL.append("'" + subPubData.getKType() + "',");
				} else {
					colBufSQL.append("t5.alone_id,t5.ktype,");
				}

				for (int j = 0; j < vMeasure.size(); j++) {
					MeasureVO measure = (MeasureVO) vMeasure.get(j);
					
					if (bNumberMeas)
						colBufSQL.append("sum(" + measure.getDbcolumn() + ")");
					else
						colBufSQL.append("''");

					if (j < vMeasure.size() - 1)
						colBufSQL.append(",");
				}
				
				if (!bNumberMeas && (subPubData.getKType().equals(mainPubData.getKType()) || bDayTime == true)) {
					selBufSQL.append(" values(");
					selBufSQL.append(colBufSQL);
					selBufSQL.append(")");
				}
				else{
					selBufSQL.append("select ");
					selBufSQL.append(colBufSQL);
					selBufSQL.append(getTotalJoinCond(strTable, strCond, mainPubData, subPubData, vSubPubData,
							bTimeType[0], bTimeType[1], bTimeType[2],false,true,true));
				}

				bufSQL.append(selBufSQL);
				stmt.addBatch(bufSQL.toString());

				if (iBatchCount++ == 100 || i == vSplitSubPubData.size() - 1) {
					stmt.executeBatch();
					iBatchCount = 0;
					stmt.clearBatch();
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			if (con!=null)
				con.close();
		}
	}
	
	/**
	 * 删除一组aloneid对应的一组指标的数据
	 * @param sAloneIDs
	 * @param measure
	 * @throws java.sql.SQLException
	 */
	public void deleteRepData(String[] sAloneIDs, MeasureVO[] measure) throws java.sql.SQLException {
		Vector vTableList = null;
		vTableList = getTableFromMeasure(measure);
		if (vTableList == null)
			return;

		Connection con =null;
		try {
			con=getConnection();
			for (int i = 0; i < vTableList.size(); i++) {
				String sTableName = (String) vTableList.elementAt(i);
				removeMeasureData(con,sTableName, sAloneIDs);
			}
		} finally {
			if (con != null)
				con.close();
		}
	}	
	
	/**
	 * 删除一张报表数据。 对于报表自己的数据表，直接删除所有aloneId对应的数据表记录； 对于引用自其它报表的指标所在数据表，将其指标值清空。
	 * @param repPK
	 * @param sAloneIDs
	 * @param measure
	 * @throws java.sql.SQLException
	 */
	public void deleteRepData(String repPK, String[] sAloneIDs, MeasureVO[] measure) throws java.sql.SQLException {
		if (repPK == null || sAloneIDs == null || sAloneIDs.length == 0 || measure == null || measure.length == 0) {
			return;
		}
		Object[] vTableList = splitTablesByRef(repPK, measure);

		Connection con =null; 
		try {
			con=getConnection();
			
			Vector vec1 = (Vector) vTableList[0];
			Vector vec2 = (Vector) vTableList[1];
			Hashtable ht = (Hashtable) vTableList[2];
			for (int i = 0; i < vec1.size(); i++) {
				String sTableName = (String) vec1.elementAt(i);
				removeMeasureData(con,sTableName, sAloneIDs);
			}
			for (int i = 0; i < vec2.size(); i++) {
				String sTableName = (String) vec2.elementAt(i);
				Vector vm = (Vector) ht.get(sTableName);
				if (vm.size() == 0) {
					continue;
				}
				MeasureVO[] ms = new MeasureVO[vm.size()];
				vm.copyInto(ms);
				emptyRepData(con,sAloneIDs, ms);
			}
		} finally {
			if (con != null)
				con.close();
		}
	}	
	
	/**
	 * 合并报表汇总
	 * @param strDBTable
	 * @param measures
	 * @param strSourAloneIDs
	 * @param strDestAloneID
	 * @param strKeyGroupId
	 * @throws java.sql.SQLException
	 */
	public void doTotalData(String strDBTable, MeasureVO[] measures, String[] strSourAloneIDs, String strDestAloneID,
			String strKeyGroupId) throws java.sql.SQLException {
		Connection con=null;
		Statement stmt=null;
		try{
			con = getConnection();
	
			String strSQL = "delete from " + strDBTable + " where alone_id='" + strDestAloneID + "'";
			stmt = con.createStatement();
			stmt.executeUpdate(strSQL);
			stmt.close();
			stmt=null;
	
			strSQL = "insert into " + strDBTable + "(alone_id,pk_key_comb,";
	
			for (int i = 0; i < measures.length; i++) {
				strSQL += measures[i].getDbcolumn() + ",";
			}
			strSQL += "line_no) ";
	
			strSQL += " select '" + strDestAloneID + "','" + strKeyGroupId + "',";
			for (int i = 0; i < measures.length; i++) {
				if (measures[i].getType() == MeasureVO.TYPE_CHAR || measures[i].getType() == MeasureVO.TYPE_CODE)
					strSQL += "'',";
				else
					strSQL += "sum(" + measures[i].getDbcolumn() + "),";
			}
	
			strSQL += "0 from " + strDBTable + " where alone_id in (";
	
			for (int i = 0; i < strSourAloneIDs.length; i++) {
				if (i == strSourAloneIDs.length - 1)
					strSQL += "'" + strSourAloneIDs[i] + "')";
				else
					strSQL += "'" + strSourAloneIDs[i] + "',";
			}

			stmt = con.createStatement();
			stmt.executeUpdate(strSQL);
		}
		finally{
			if (stmt!=null)
				stmt.close();
			if (con!=null)
				con.close();
		}
	}
	
	/**
	 * 编辑一张报表数据
	 * @param sAloneID
	 * @param measureData
	 * @throws java.sql.SQLException
	 */
	public void editRepData(String sAloneID, MeasureDataVO[] measureData) throws java.sql.SQLException {
		Connection con =null;

		try {
			con=getConnection();

			// 指标数据表中包含的数据库表的集合
			Vector<String> vTableList = getTable(measureData);

			for (int i = 0; i < vTableList.size(); i++) {
				String sTableName = (String) vTableList.elementAt(i);
				// 在某个报表中包含measuredatavo中的指标数据的集合。
				Vector<MeasureDataVO> vMeasureInTable = getMeasureInTable(sTableName, measureData);
				// 得到指标数据表中的最大行号
				int iRowNum = getRowNum(vMeasureInTable);

				// 是固定表
				if (iRowNum == 0) {
					// Vector vLineData = getLineDataInTable(0,
					// vMeasureInTable);
					// 如果没有第零行记录,这个方法是为了导入数据时防止没有
					// 第零行记录而先初始化的，因为在导入数据时调用的getaloneId（）
					// 方法和录入时调用的得到aloneId的方法不同。
					if (!isMeaDataExist(con,sTableName, sAloneID, 0)) {
						addMeasureData(con, sTableName, vMeasureInTable);
					}
					updateMeasureData(con, sTableName, sAloneID, 0, vMeasureInTable);
				} else {
					// 否则就认为他是可变表
					removeMeasureData(con,sTableName, new String[] { sAloneID });
					// 说明在可变表中增加行数
					addMeasureData(con, sTableName, vMeasureInTable);
				}
			}
		} finally {
			if (con != null)
				con.close();
		}
	}	
	
	/**
	 * 清空指定指标、指定aloneId的值。 注意：这些值被置为指标在清空时的默认值
	 * @param aloneIds
	 * @param measures
	 * @param commitFlags
	 * @throws java.sql.SQLException
	 */
	public void emptyRepData(String[] aloneIds, MeasureVO[] measures, int[] commitFlags) throws java.sql.SQLException {
		Connection con=null;
		try{
			con=getConnection();

			// 参数无效时，直接返回
			if (aloneIds == null || aloneIds.length == 0 || commitFlags == null || commitFlags.length == 0
					|| measures == null || measures.length == 0) {
				return;
			}
	
			Vector<String> vec = new Vector<String>();
			for (int i = 0; i < aloneIds.length; i++) {
				if (commitFlags[i] == nc.vo.iufo.query.returnquery.ReportCommitVO.NOTCOMMITED) {
					vec.add(aloneIds[i]);
				}
			}
	
			emptyRepData(con,vec.toArray(new String[0]), measures);
		}
		finally{
			if (con!=null)
				con.close();
		}
	}	
	
	/**
	 * 加载某一指标对应多个aloneid的值
	 * @param measure
	 * @param vPubData
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Vector<MeasureDataVO> getMeasureData(MeasureVO measure, MeasurePubDataVO[] vPubData) throws java.sql.SQLException {
		if (vPubData == null || vPubData.length == 0) {
			return new Vector<MeasureDataVO>();
		}
		
		Vector<MeasureDataVO> vRetData = new Vector<MeasureDataVO>();

		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (int i = 0; i < vPubData.length; i++) {
			sb.append("'");
			sb.append(((MeasurePubDataVO) vPubData[i]).getAloneID());
			sb.append("'");
			if (i < vPubData.length - 1) {
				sb.append(",");
			}
		}
		sb.append(")");
		MeasureVO[] measures = new MeasureVO[] { measure };
		String sql = makeSelectSQL(measure.getDbtable(), measures);
		sql += " where alone_id in " + sb.toString();

		Connection con =null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con=getConnection();
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				MeasureDataVO[] datas = extractMeasureData(rs, measures);
				for (int i = 0; i < datas.length; i++) {
					vRetData.add(datas[i]);
				}

			}
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}

		return vRetData;
	}
	
	/**
	 * 获得指定指标的所有数据
	 * @param sAloneIDs
	 * @param measures
	 * @return
	 * @throws Exception
	 */
	public MeasureDataVO[] getRepData(MeasureVO[] measures) throws Exception {
		Connection con = null;
		Vector<MeasureDataVO> vRe = new Vector<MeasureDataVO>();
		Vector vTableList = getTableFromMeasure(measures);
		try {
			con = getConnection();
			for (int i = 0; i < vTableList.size(); i++) {
				String sTableName = (String) vTableList.elementAt(i);
				Vector<MeasureVO> vMeasureInTable = getMeasureInTable(sTableName, measures);
				Vector<MeasureDataVO> vMeasureVOInTable = getMeasureDataVo(con, sTableName, vMeasureInTable);
				vRe.addAll(vMeasureVOInTable);
			}
		} finally {
			if (con != null)
				con.close();
		}
		
		return vRe.toArray(new MeasureDataVO[0]);
	}

	
	/**
	 * 获得对应一组aloneid的一组指标的数据
	 * @param sAloneIDs
	 * @param measures
	 * @return
	 * @throws Exception
	 */
	public MeasureDataVO[] getRepData(String[] sAloneIDs, MeasureVO[] measures) throws Exception {
		Connection con = null;
		Vector<String> vTableList = getTableFromMeasure(measures);
		try {
			Vector<MeasureDataVO> vRetData = new Vector<MeasureDataVO>();
			con=getConnection();
			for (int i = 0; i < vTableList.size(); i++) {
				String sTableName = vTableList.elementAt(i);
				Vector<MeasureVO> vMeasureInTable = getMeasureInTable(sTableName, measures);
				Vector<MeasureDataVO> vMeasDataInTable = getMeasureDataVoByMeasure(con, sTableName, sAloneIDs, vMeasureInTable);
				vRetData.addAll(vMeasDataInTable);
			}
			return vRetData.toArray(new MeasureDataVO[0]);
		} finally {
			if (con != null)
				con.close();
		}
	}
	
	/**
	 * 在相应的数据库表中得到相应行的数据
	 * @param con
	 * @param sTableName
	 * @param sAloneIDs
	 * @param vMeasureData
	 * @return
	 * @throws Exception
	 */
	private Vector<MeasureDataVO> getMeasureDataVo(Connection con, String sTableName, Vector<MeasureVO> vMeasure)
			throws Exception {
		MeasureVO[] measures=vMeasure.toArray(new MeasureVO[0]);
		String sql = makeSelectSQL(sTableName, vMeasure.toArray(measures));
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector<MeasureDataVO> vRe = new Vector<MeasureDataVO>();

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(sql);
			stmt = con.prepareStatement(sb.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				MeasureDataVO[] datas = extractMeasureData(rs, measures);
				for (int i = 0; i < datas.length; i++) {
					vRe.addElement(datas[i]);
				}
			}
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			rs=null;
			stmt=null;
		}
		
		return vRe;
	}
	

	/**
	 * 得到指标涉及到的指标数据表
	 * @param measures
	 * @return
	 */
	public Vector<String> getTableFromMeasure(MeasureVO[] measures) {
		if (measures == null || measures.length == 0)
			return null;

		Vector<String> vRetTable = new Vector<String>();

		for (int i = 0; i < measures.length; i++) {
			MeasureVO meavo = measures[i];
			if (meavo != null) {
				String sTableName = meavo.getDbtable();
				if (!vRetTable.contains(sTableName)) {
					vRetTable.addElement(sTableName);
				}
			}
		}
		return vRetTable;
	}
	
	/**
	 * 在相应的数据库表中增加一条指标为空的记录
	 * @param sTableName
	 * @param sAloneID
	 * @param keyCombPK
	 * @throws Exception
	 */
	public void initMeasureData(Vector<String> sTableName, String sAloneID, String keyCombPK) throws Exception {
		if (sTableName == null || sTableName.size() == 0) 
			return;
		
		String[] tables = sTableName.toArray(new String[0]);
		initMeasureData(tables, sAloneID, keyCombPK);
	}	
	
	/**
	 * 汇总时，对于一张指标数据表，得到需要汇总的符合条件的记录总数
	 * @param mainPubData，主表MeasurePubDataVO
	 * @param subPubData,子表MeasurePubDataVO
	 * @param strCond，汇总条件筛选表达式
	 * @param strTable，指标数据表
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int loadTotalDataCount(MeasurePubDataVO mainPubData, MeasurePubDataVO subPubData, String strCond,
			String strTable) throws java.sql.SQLException {
		Connection con =null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		int iCount = 0;
		try {
			con=getConnection();
			
			boolean[] bTimeType = getKeywordTimeType(mainPubData, subPubData);
			StringBuffer bufSQL = new StringBuffer();
			bufSQL.append("select count(t1.alone_id) ");
			bufSQL.append(getTotalJoinCond(strTable, strCond, mainPubData, subPubData, null, bTimeType[0],
					bTimeType[1], bTimeType[2],false,true,true));
			stmt = con.prepareStatement(bufSQL.toString());
			set = stmt.executeQuery();
			if (set.next())
				iCount = set.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (set != null)
				set.close();
			if (stmt != null)
				stmt.close();
			if (con!=null)
				con.close();
		}
		return iCount;
	}

	
	/**
	 * 一次保存一批指标数据。 注意指标数据VO中需要设置好aloneId,私有关键字的值，行号。
	 * @param datas
	 * @throws java.sql.SQLException
	 */
	public void saveMeasureDatas(MeasureDataVO[] datas) throws java.sql.SQLException {
		if (datas == null || datas.length == 0) {
			return;
		}
		// 根据指标所属的数据表分组
		MeasureVO[] measures = new MeasureVO[datas.length];
		for (int i = 0; i < measures.length; i++) {
			measures[i] = datas[i].getMeasureVO();
		}
		
		Vector<String> tables = getTableFromMeasure(measures);

		Vector<Object[]> id_datas = new Vector<Object[]>();
		for (int i = 0; i < tables.size(); i++) {
			String tableName = (String) tables.elementAt(i);
			// 将一个数据表中的指标数据按aloneId分组
			Hashtable<String,Vector<MeasureDataVO>> ht = groupMeasureDatasByAloneId_LineNo(datas, tableName);
			Enumeration keys = ht.keys();
			// 将分好组的数据再次放到总的分组记录中
			while (keys.hasMoreElements()) {
				String aloneId = (String) keys.nextElement();
				Vector<MeasureDataVO> vec = ht.get(aloneId);
				Object[] id_data = new Object[3];
				id_data[0] = tableName;
				id_data[1] = aloneId;
				id_data[2] = vec;
				id_datas.add(id_data);
			}
		}
		
		Connection con =null;
		Statement stat = null;
		try {
			con=getConnection();
			
			// 开始分组拼装SQL语句
			Vector<String> sqls = new Vector<String>();
			for (int i = 0; i < id_datas.size(); i++) {
				Object[] iddata = (Object[]) id_datas.elementAt(i);
				String tableName = (String) iddata[0];
				String aloneId = (String) iddata[1];
				// 拆分行号与aloneId值
				int lineNo = Integer.parseInt(aloneId.substring(aloneId.indexOf("_") + 1));
				aloneId = aloneId.substring(0, aloneId.indexOf("_"));
				Vector mdatas = (Vector) iddata[2];
				MeasureDataVO[] meadatas = null;
				if (mdatas.size() > 0) {
					meadatas = new MeasureDataVO[mdatas.size()];
					mdatas.copyInto(meadatas);
					// 根据aloneId和lineNO在一张表中是否存在，生成不同的SQL语句
					if (isMeaDataExist(con,tableName, aloneId, lineNo)) {
						sqls.add(makeUpdateSQL(tableName, aloneId, lineNo, meadatas));
					} else {
						sqls.add(makeInsertSQL(tableName, aloneId, lineNo, meadatas, null));
					}
				}
			}			
			
			stat = con.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
				stat.addBatch((String) sqls.elementAt(i));
			}
			stat.executeBatch();
		} finally {
			if (stat != null)
				stat.close();
			if (con != null)
				con.close();
		}
	}
	
	/**
	 * 汇总溯源时，对公有关键字动态区溯源
	 * @param measures，要溯源的指标
	 * @param strCondSQL，汇总条件的SQL语句
	 * @param keys，要查找的关键字值的关键字数组
	 * @param hashKeyPos，要查找的关键字与关键字组合中的关键字的位置的对应关系
	 * @param mainPubData，主表MeasurePubDataVO
	 * @param subPubData,子表MeasurePubDataVO
	 * @return
	 * @throws SQLException
	 */
	public TotalCellKeyValue[] loadTotalSourValsByPub(MeasureVO[] measures, String strCondSQL, KeyVO[] keys,
			Hashtable hashKeyPos, MeasurePubDataVO mainPubData, MeasurePubDataVO subPubData) throws SQLException {
		Connection con =null;
		PreparedStatement stmt = null;
		ResultSet set=null;
		try {
			con=getConnection();
			
			Vector<String> vMeasID = getMeasIDFromMeasure(measures);
			boolean[] bTimeType = getKeywordTimeType(mainPubData, subPubData);

			KeyVO[] measKeys = IUFOBSCacheManager.getSingleton().getKeyGroupCache().getByPK(measures[0].getKeyCombPK()).getKeys();
			Vector<KeyVO> vMeasKey = new Vector<KeyVO>(Arrays.asList(measKeys));

			String strTable = measures[0].getDbtable();
			// 拼接select语句
			StringBuffer selBufSQL = new StringBuffer();

			selBufSQL.append("select t1.alone_id,t3.alone_id,t2.code,");
			for (int i = 0; i < keys.length; i++) {
				if (vMeasKey.contains(keys[i])) {
					int iPos = ((Integer) hashKeyPos.get(new Integer(i))).intValue() + 1;
					selBufSQL.append("t2.keyword" + iPos + ",");
				} else
					selBufSQL.append("t3.inputdate,");
			}

			for (int i = 0; i < measures.length; i++) {
				MeasureVO measure = (MeasureVO) measures[i];
				selBufSQL.append("t1." + measure.getDbcolumn());

				if (i < measures.length - 1)
					selBufSQL.append(",");
			}
			selBufSQL.append(getTotalJoinCond(strTable, strCondSQL, mainPubData, subPubData, null, bTimeType[0],
					bTimeType[1], bTimeType[2],true,true,false));

			stmt = con.prepareStatement(selBufSQL.toString());
			set = stmt.executeQuery();

			Vector<TotalCellKeyValue> vRetVal = new Vector<TotalCellKeyValue>();
			while (set.next()) {
				TotalCellKeyValue oneValue = new TotalCellKeyValue();
				oneValue.setDynAloneID(set.getString(1));
				oneValue.setMainAloneID(set.getString(2));
				oneValue.setUnitPK(set.getString(3));

				String[] strKeyValues = new String[keys.length];
				String[] strMeasValues = new String[measures.length];
				for (int i = 0; i < keys.length + measures.length; i++) {
					if (i < keys.length)
						strKeyValues[i] = set.getString(i + 4);
					else if (measures[i - keys.length].getType() == MeasureVO.TYPE_NUMBER)
						strMeasValues[i - keys.length] = "" + set.getDouble(i + 4);
					else
						strMeasValues[i - keys.length] = set.getString(i + 4);
				}

				oneValue.setKeyVals(strKeyValues);
				oneValue.setMeasID(new Vector<String>(vMeasID));
				oneValue.setVals(new Vector<String>(Arrays.asList(strMeasValues)));

				vRetVal.add(oneValue);
			}
			return (TotalCellKeyValue[]) vRetVal.toArray(new TotalCellKeyValue[0]);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (set!=null)
				set.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * 汇总溯源时，对私有关键字动态区溯源
	 * @param measures，要溯源的指标
	 * @param strCondSQL，汇总条件对应的SQL语句
	 * @param pubData，主表MeasurePubDataVO
	 * @param mainKeys，主表关键字
	 * @param sourKeys，子表关键字
	 * @param destKeys，需要在界面上显示的关键字
	 * @param hashKeyPos，要显示的关键字与iufo_measure_pubdata表、iufo_measuredata表的关键字的对应关系
	 * @param strPrivKeyVals，要溯源的区间对应的动态区的关键字的值
	 * @return
	 * @throws SQLException
	 */
	public TotalCellKeyValue[] loadTotalSourValsByPriv(MeasureVO[] measures, String strCondSQL,
			MeasurePubDataVO pubData, KeyVO[] mainKeys, KeyVO[] sourKeys, KeyVO[] destKeys, Hashtable hashKeyPos,
			String[] strPrivKeyVals) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set=null;
		try {
			con=getConnection();
			
			Vector<String> vMeasID = getMeasIDFromMeasure(measures);

			String strDBTable = measures[0].getDbtable();
			Vector<KeyVO> vPubKey = new Vector<KeyVO>(Arrays.asList(mainKeys));
			Vector<KeyVO> vPrivKey = new Vector<KeyVO>(Arrays.asList(sourKeys));
			
			//将子表关键字中的主表关键字删除掉
			for (int i = 0; i < vPubKey.size(); i++) {
				int iIndex = vPrivKey.indexOf(vPubKey.get(i));
				if (iIndex >= 0)
					vPrivKey.remove(iIndex);
			}

			StringBuffer selBufSQL = new StringBuffer();
			selBufSQL.append("select t2.alone_id,t2.code,");

			//添加关键字字段
			for (int i = 0; i < destKeys.length; i++) {
				int iPos = ((Integer) hashKeyPos.get(new Integer(i))).intValue();
				//私有关键字与公有关键字从不同的表取
				if (destKeys[i].isPrivate()) {
					iPos = vPrivKey.indexOf(sourKeys[iPos]);
					selBufSQL.append("t1.keyvalue" + (iPos + 1) + ",");
				} else {
					iPos = vPubKey.indexOf(sourKeys[iPos]);
					selBufSQL.append("t2.keyword" + (iPos + 1) + ",");
				}
			}
			
			//添加指标字段
			for (int i = 0; i < measures.length; i++) {
				MeasureVO measure = measures[i];
				selBufSQL.append("t1." + measure.getDbcolumn());

				if (i < measures.length - 1)
					selBufSQL.append(",");
			}
			selBufSQL.append(" from ");
			selBufSQL.append(strDBTable + " t1,");
			selBufSQL.append(nc.bs.iufo.toolkit.DatabaseNames.IUFO_MEASURE_PUBDATA + " t2, ");
			selBufSQL.append("(" + strCondSQL + ") t3 ");
			selBufSQL.append(" where t1.alone_id=t2.alone_id and t1.alone_id=t3.alone_id and ");
			selBufSQL.append(genWhereSQL(pubData, "t2", false));

			//加上私有关键字条件
			for (int i = 0; i < strPrivKeyVals.length; i++)
				selBufSQL.append(" and t1.keyvalue" + (i + 1) + "=?");

			stmt = con.prepareStatement(selBufSQL.toString());

			for (int i = 0; i < strPrivKeyVals.length; i++)
				stmt.setString(i + 1, strPrivKeyVals[i]);

			Vector<TotalCellKeyValue> vRetVal = new Vector<TotalCellKeyValue>();
			set = stmt.executeQuery();
			while (set.next()) {
				TotalCellKeyValue oneValue = new TotalCellKeyValue();
				String[] strKeyVals = new String[destKeys.length];
				Vector<String> vMeasVal = new Vector<String>();
				oneValue.setMainAloneID(set.getString(1));
				oneValue.setDynAloneID(oneValue.getMainAloneID());
				oneValue.setUnitPK(set.getString(2));
				for (int i = 0; i < destKeys.length + measures.length; i++) {
					if (i < destKeys.length)
						strKeyVals[i] = set.getString(i + 3);
					else {
						if (measures[i - destKeys.length].getType() == MeasureVO.TYPE_NUMBER)
							vMeasVal.add("" + set.getDouble(i + 3));
						else
							vMeasVal.add(set.getString(i + 3));
					}
				}
				oneValue.setMeasID(new Vector<String>(vMeasID));
				oneValue.setKeyVals(strKeyVals);
				oneValue.setVals(vMeasVal);

				vRetVal.add(oneValue);
			}
			return (TotalCellKeyValue[]) vRetVal.toArray(new TotalCellKeyValue[0]);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (set!=null)
				set.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}
	}	
	
	/**
	 * 在数据表中增加指标数据
	 * @param con
	 * @param sTableName
	 * @param vMeasureData
	 * @throws java.sql.SQLException
	 */
	private void addMeasureData(Connection con, String sTableName, Vector<MeasureDataVO> vMeasureData) throws java.sql.SQLException {
		if (vMeasureData == null || vMeasureData.size() <= 0) 
			return;
		
		MeasureDataVO[] datas = (MeasureDataVO[]) vMeasureData.toArray(new MeasureDataVO[0]);
		String sAloneID = datas[0].getAloneID();
		String[] sqls = makeInsertSQL(sTableName, sAloneID, datas);

		Statement stmt =null; 
		try {
			stmt=con.createStatement();
			for (int i = 0; i < sqls.length; i++) {
				stmt.addBatch(sqls[i]);
				if ((i > 0 && i % 100 == 0) || i == sqls.length - 1) {
					stmt.executeBatch();
					stmt.clearBatch();
				}
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	/**
	 * 在相应的数据库表中增加一条指标为空的记录
	 * @param con
	 * @param sTableName
	 * @param sAloneID
	 * @param iLineNum
	 * @param keyCombPK
	 * @throws java.sql.SQLException
	 */
	private void createMeasureData(Connection con, String sTableName, String sAloneID, int iLineNum, String keyCombPK)
			throws java.sql.SQLException {
		String sql = makeInsertSQL(sTableName, sAloneID, iLineNum, null, keyCombPK);
		if (sql == null)
			return;
		
		PreparedStatement stmt =null;
		try {
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	/**
	 * 汇总时，生成几张表间关联条件的公共方法
	 * @param strMainTable，主表对应的iufo_measure_pubdata表
	 * @param strSubTable，动态区对应的iufo_measure_pubdata表
	 * @param mainPubData，主表对应的MeasurePubDataVO
	 * @param subPubData，动态区对应的MeasurePubDataVO
	 * @param vSubPubData,
	 * @param bSubTime,是否主、子表都有时间
	 * @param bWeekTime，子表是否有周时间
	 * @param bDayTime，子表是否有日
	 * @param strAppendTable
	 * @return
	 */
	private String getTotalTimeJoinCond(String strMainTable, String strSubTable, MeasurePubDataVO mainPubData,
			MeasurePubDataVO subPubData, List<MeasurePubDataVO> vSubPubData, boolean bSubTime, boolean bWeekTime, boolean bDayTime,
			String strAppendTable) {
		StringBuffer selBufSQL = new StringBuffer();

		// 主子表不同关键字，其在各自关键字组合中的位置可能不相同，建立两个间的对应关系
		Hashtable<Integer,Integer> hashPos = new Hashtable<Integer,Integer>();
		KeyVO[] mainKeys = mainPubData.getKeyGroup().getKeys();
		KeyVO[] subKeys = subPubData.getKeyGroup().getKeys();
		boolean bMainKeyNoTime = mainPubData.getKeyGroup().getTimeProp().equalsIgnoreCase(UFODate.NONE_PERIOD);
		for (int i = 0; i < subKeys.length; i++) {
			for (int j = 0; j < mainKeys.length; j++) {
				if (mainKeys[j].equals(subKeys[i])) {
					hashPos.put(new Integer(i), new Integer(j));
					break;
				}
			}
		}

		if (bDayTime == true || vSubPubData == null)
			selBufSQL.append(genWhereSQL(subPubData, strSubTable, bSubTime));
		else {
			for (int i = 0; i < subKeys.length; i++) {
				if (i==0 && subPubData != null && subPubData.getKType() != null)
					selBufSQL.append(" and " + strSubTable + ".ktype='" + subPubData.getKType() + "' ");
				if ((bMainKeyNoTime || subKeys[i].getTimeKeyIndex() < 0) && hashPos.get(new Integer(i)) == null)
					selBufSQL.append(" and " + strSubTable + ".keyword" + (i + 1) + "=" + strAppendTable + ".keyword"
							+ (i + 1));
			}
		}

		// 在t2表和t3表建立连接条件，
		// 根据关键字对应关系，建立keyword的连接条件
		Enumeration enumPos = hashPos.keys();
		while (enumPos.hasMoreElements()) {
			Integer iSubKeyPos = (Integer) enumPos.nextElement();
			Integer iMainKeyPos = (Integer) hashPos.get(iSubKeyPos);

			if (subKeys.length > iSubKeyPos.intValue()
					&& subKeys[iSubKeyPos.intValue()].getKeywordPK().equals(KeyVO.CORP_PK))
				selBufSQL.append(" and " + strSubTable + ".code=" + strMainTable + ".code");
			selBufSQL.append(" and " + strSubTable + ".keyword" + (iSubKeyPos.intValue() + 1) + "=" + strMainTable
					+ ".keyword" + (iMainKeyPos.intValue() + 1));
		}

		if (bSubTime == false)
			return selBufSQL.toString();

		// 建立t2表和t3表TimeCode的连接条件,子表时间类型为周时，只需要取TimeCode的周两位
		int iIndex = 2 * (mainPubData.getKeyGroup().getTimeKey().getTimeKeyIndex() + 1) + 2;
		int iSubLen, iSubIndex;
		String strSubTimeCode = null;

		if (bDayTime == false) {
			if (bWeekTime == false) {
				iSubIndex = 2 * (subPubData.getKeyGroup().getTimeKey().getTimeKeyIndex() + 1) + 2;
				strSubTimeCode = subPubData.getTimeCode().substring(iIndex, iSubIndex);
				iSubLen = iSubIndex - iIndex;
				iSubIndex = iIndex;
			} else {
				iSubIndex = 2 * subPubData.getKeyGroup().getTimeKey().getTimeKeyIndex() + 2;
				iSubLen = 2;
				strSubTimeCode = subPubData.getTimeCode().substring(iSubIndex, iSubIndex + 2);
			}

			String strFunc = "substr";
			if (GlobalValue.DATABASE_TYPE.equals(IDatabaseType.DATABASE_SQLSERVER))
				strFunc = "substring";

			selBufSQL.append(" and ");
			selBufSQL.append(getSubFuncSQL(strFunc, strSubTable, "time_code", 1, iIndex));
			selBufSQL.append("=");
			selBufSQL.append(getSubFuncSQL(strFunc, strMainTable, "time_code", 1, iIndex));

			selBufSQL.append(" and ");
			selBufSQL.append(getSubFuncSQL(strFunc, strSubTable, "time_code", iSubIndex + 1, iSubLen));

			if (vSubPubData == null) {
				selBufSQL.append("='");
				selBufSQL.append(strSubTimeCode);
				selBufSQL.append("'");
			} else {
				selBufSQL.append("=" + getSubFuncSQL(strFunc, strAppendTable, "time_code", iSubIndex + 1, iSubLen));
			}
		} else {
			iSubIndex = 2 * (subPubData.getKeyGroup().getTimeKey().getTimeKeyIndex() + 1);
			strSubTimeCode = subPubData.getTimeCode().substring(iIndex, iSubIndex);
			iSubLen = iSubIndex - iIndex - 2;
			int iSubIndex1 = iSubIndex;
			int iSubLen1 = 2;
			iSubIndex = iIndex;

			String strSubFunc = "substr";
			if (GlobalValue.DATABASE_TYPE.equals(IDatabaseType.DATABASE_SQLSERVER)) {
				strSubFunc = "substring";
			}

			selBufSQL.append(" and ");
			selBufSQL.append(getSubFuncSQL(strSubFunc, strSubTable, "time_code", 1, iIndex));
			selBufSQL.append("=");
			selBufSQL.append(getSubFuncSQL(strSubFunc, strMainTable, "time_code", 1, iIndex));

			selBufSQL.append(" and '");
			selBufSQL.append(subPubData.getTimeCode().substring(iSubIndex1, iSubLen1 + iSubIndex1));
			selBufSQL.append("'=");

			String strMonthCode = subPubData.getTimeCode().substring(8, 10);
			if (strMonthCode.equals("04") || strMonthCode.equals("06") || strMonthCode.equals("09")
					|| strMonthCode.equals("11")) {
				selBufSQL.append(" (case when "
						+ getSubFuncSQL(strSubFunc, strSubTable, "time_code", iSubIndex1 + 1, iSubLen1) + ">'30' ");
				selBufSQL.append(" then '30' else "
						+ getSubFuncSQL(strSubFunc, strSubTable, "time_code", iSubIndex1 + 1, iSubLen1) + " end) ");
			} else if (strMonthCode.equals("02")) {
				int iYear = Integer.parseInt(subPubData.getTimeCode().substring(0, 4));
				if (((double) iYear / 4) == iYear / 4) {
					selBufSQL.append(" (case when "
							+ getSubFuncSQL(strSubFunc, strSubTable, "time_code", iSubIndex1 + 1, iSubLen1) + ">'29' ");
					selBufSQL.append(" then '29' else "
							+ getSubFuncSQL(strSubFunc, strSubTable, "time_code", iSubIndex1 + 1, iSubLen1) + " end) ");
				} else {
					selBufSQL.append(" (case when "
							+ getSubFuncSQL(strSubFunc, strSubTable, "time_code", iSubIndex1 + 1, iSubLen1) + ">'28' ");
					selBufSQL.append(" then '28' else "
							+ getSubFuncSQL(strSubFunc, strSubTable, "time_code", iSubIndex1 + 1, iSubLen1) + " end) ");
				}
			} else
				selBufSQL.append(getSubFuncSQL(strSubFunc, strSubTable, "time_code", iSubIndex1 + 1, iSubLen1));
		}

		return selBufSQL.toString();
	}

	/**
	 * 截断处理的SQL语句的公共函数
	 * @param strFunc
	 * @param strTable
	 * @param strColumn
	 * @param iStart
	 * @param iLen
	 * @return
	 */
	private String getSubFuncSQL(String strFunc, String strTable, String strColumn, int iStart, int iLen) {
		return " " + strFunc + "(" + strTable + "." + strColumn + "," + iStart + "," + iLen + ") ";
	}

	/**
	 * 汇总时，生成几张表间关联条件的公共方法
	 * @param strTable
	 * @param strCond
	 * @param mainPubData
	 * @param subPubData
	 * @param vSubPubData
	 * @param bSubTime
	 * @param bWeekTime
	 * @param bDayTime
	 * @param bNeedPubData2
	 * @param bNumberMeas
	 * @param bUseTempTable
	 * @return
	 * @throws SQLException
	 */	
	private String getTotalJoinCond(String strTable,String strCond,MeasurePubDataVO mainPubData,MeasurePubDataVO subPubData,List<MeasurePubDataVO> vSubPubData,boolean bSubTime,boolean bWeekTime,boolean bDayTime,boolean bNeedPubData2,boolean bNumberMeas,boolean bUseTempTable) throws SQLException{
	    StringBuffer selBufSQL=new StringBuffer();
	    selBufSQL.append(" from ");

	    //如果主子表同时有时间关键字，则需要从iufo_measure_pubdata表查找两次，第二个表负责和查询条件
	    //表根据Alone_id做关联，查出主KType的表，第一个表是Select语句的主表，其KType为子表KType,两张
	    //表之间根据Keyword和TimeCode条件关联

	    //如果主子表不是同时有时间关键字，则iufo_measure_pubdata表直接和查询条件表通过Alone_id关联即可

	    selBufSQL.append(strTable+" t1,");
	    if (mainPubData.getKType().equals(subPubData.getKType())==false){
	        selBufSQL.append(nc.bs.iufo.toolkit.DatabaseNames.IUFO_MEASURE_PUBDATA+" t2,");
	        
	        if (bUseTempTable)
	        	selBufSQL.append(strCond + " t3");
	        else{
	        	selBufSQL.append(DatabaseNames.IUFO_MEASURE_PUBDATA+" t3,");
	        	selBufSQL.append(strCond+" t4");
	        }
	        
	        if (bDayTime==false && vSubPubData!=null)
	        	selBufSQL.append(","+nc.bs.iufo.toolkit.DatabaseNames.IUFO_MEASURE_PUBDATA + " t5 ");
	        
	        selBufSQL.append(" where t1.alone_id=t2.alone_id ");
	        if (!bUseTempTable)
	        	selBufSQL.append(" and t3.alone_id=t4.alone_id ");
	        selBufSQL.append(" and t3.ktype='"+mainPubData.getKType()+"'");
	        selBufSQL.append(" and t2.ver=t3.ver and ");
	        
	        if (bDayTime==false && vSubPubData!=null){
	        	selBufSQL.append(" t5.alone_id in(");
	        	for (int i=0;i<vSubPubData.size();i++){
	        		selBufSQL.append("'"+((MeasurePubDataVO)vSubPubData.get(i)).getAloneID()+"'");
	        		if (i<vSubPubData.size()-1)
	        			selBufSQL.append(",");
	        		else
	        			selBufSQL.append(") ");
	        	}
	        }
	        
	        selBufSQL.append(getTotalTimeJoinCond("t3","t2",mainPubData,subPubData,vSubPubData,bSubTime,bWeekTime,bDayTime,"t5"));
	        
	        if (bDayTime==false && vSubPubData!=null && bNumberMeas){
	        	selBufSQL.append(" group by t5.alone_id,t5.ktype ");
	        }
	    }
	    else{
	    	if (bNeedPubData2){
	    		selBufSQL.append(strCond+" t3, "+nc.bs.iufo.toolkit.DatabaseNames.IUFO_MEASURE_PUBDATA+" t2 ");
	    		selBufSQL.append(" where t1.alone_id=t2.alone_id and t3.alone_id=t2.alone_id ");
	    	}else{
	    		selBufSQL.append(""+strCond+" t3 ");
	    		selBufSQL.append(" where t1.alone_id=t3.alone_id ");    		
	    	}
	    }

	    return selBufSQL.toString();
	}

	/**
	 * 将一个大的数组拆分成几个小的数组
	 * 
	 * @param vObj，原数组
	 * @param iSize，一个数组的最大大小
	 * @return，数组的数组
	 */
	private Vector<List<MeasurePubDataVO>> splitObject(List<MeasurePubDataVO> vObj, int iSize) {
		Vector<List<MeasurePubDataVO>> vRet = new Vector<List<MeasurePubDataVO>>();
		if (vObj == null || vObj.size() <= 0)
			return vRet;

		int iCount = 0;
		Vector<MeasurePubDataVO> vArray = new Vector<MeasurePubDataVO>();
		for (int i = 0; i < vObj.size(); i++) {
			vArray.add(vObj.get(i));
			if (++iCount == iSize || i == vObj.size() - 1) {
				vRet.add(vArray);
				vArray = new Vector<MeasurePubDataVO>();
				iCount = 0;
			}
		}
		return vRet;
	}

	/**
	 * 将MeasurePubDataVO数组按是否在指标数据表中有记录，分成两组
	 * @param strTable，指标数据表
	 * @param vPubData，MeasurePubDataVO数组
	 * @return，返回两个数组，一个是在指标数据表中有记录的MeasurePubDataVO数组，一个是没有记录的MeasurePubDataVO数组
	 * @throws SQLException
	 */
	private Vector<Vector<MeasurePubDataVO>> splitPubDataByExistRecord(String strTable, List<MeasurePubDataVO> vPubData) throws SQLException {
		Connection con=null;
		Statement stmt = null;
		ResultSet set = null;
		try {
			con = getConnection();
			
			Vector<String> vExistAloneID = new Vector<String>();

			// 将MeasurePubDataVO数组变成多个小数组，防止in语句记录超出
			Vector<List<MeasurePubDataVO>> vSplitPubData = splitObject(vPubData, 100);

			// 查找出所有有记录的aloneid
			for (int i = 0; i < vSplitPubData.size(); i++) {
				List<MeasurePubDataVO> vOnePubData =vSplitPubData.get(i);
				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append("select alone_id from " + strTable + " where alone_id in(");

				for (int j = 0; j < vOnePubData.size(); j++) {
					sqlBuf.append("'" + ((MeasurePubDataVO) vOnePubData.get(j)).getAloneID() + "'");
					if (j < vOnePubData.size() - 1)
						sqlBuf.append(",");
				}
				sqlBuf.append(")");

				stmt = con.createStatement();
				set = stmt.executeQuery(sqlBuf.toString());
				while (set != null && set.next()) {
					vExistAloneID.add(set.getString(1));
				}
				set.close();
				set = null;
				stmt.close();
				stmt = null;
			}

			// 将MeasurePubDataVO进行分组
			Vector<MeasurePubDataVO> vExistPubData = new Vector<MeasurePubDataVO>();
			Vector<MeasurePubDataVO> vNoExistPubData = new Vector<MeasurePubDataVO>();
			for (int i = 0; i < vPubData.size(); i++) {
				MeasurePubDataVO pubData = (MeasurePubDataVO) vPubData.get(i);
				if (vExistAloneID.contains(pubData.getAloneID())) {
					vExistPubData.add(pubData);
				} else
					vNoExistPubData.add(pubData);
			}
			
			Vector<Vector<MeasurePubDataVO>> vRetPubData=new Vector<Vector<MeasurePubDataVO>>();
			vRetPubData.add(vExistPubData);
			vRetPubData.add(vNoExistPubData);
			return vRetPubData;
		} finally {
			if (set != null)
				set.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * 清空指定指标、指定aloneId的值。 注意：这些值被置为指标在清空时的默认值
	 * @param con
	 * @param aloneIds
	 * @param measures
	 * @throws java.sql.SQLException
	 */
	private void emptyRepData(Connection con,String[] aloneIds, MeasureVO[] measures) throws java.sql.SQLException {
		// 参数无效时，直接返回
		if (aloneIds == null || aloneIds.length == 0 || measures == null || measures.length == 0) 
			return;

		String[] updateSqls = geneEmptyRepSqls(aloneIds, measures);
		if (updateSqls == null || updateSqls.length == 0) 
			return;
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			for (int i = 0; i < updateSqls.length; i++) {
				if (updateSqls[i] != null)
					stmt.addBatch(updateSqls[i]);
				if ((i > 0 && i % 100 == 0) || i == updateSqls.length - 1) {
					stmt.executeBatch();
					stmt.clearBatch();
				}
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	private MeasureDataVO[] extractMeasureData(ResultSet rs, MeasureVO[] measures) throws java.sql.SQLException {
		String aloneId = rs.getString("alone_id");
		String kgPK = rs.getString("pk_key_comb");
		int lineNo = rs.getInt("line_no");
		String[] keyValues = new String[10];
		boolean hasValue = false;
		for (int i = 0; i < keyValues.length; i++) {
			keyValues[i] = rs.getString(i + 4);
			if (keyValues[i] != null && keyValues[i].trim().length() > 0
					&& keyValues[i].trim().equalsIgnoreCase("null") == false) {
				hasValue = true;
			} else
				keyValues[i] = null;
		}

		MeasureDataVO[] result = new MeasureDataVO[measures.length];
		for (int i = 0; i < measures.length; i++) {
			MeasureDataVO data = new MeasureDataVO();
			data.setAloneID(aloneId);
			data.setPrvtKeyGroupPK(kgPK);
			data.setRowNo(lineNo);
			data.setMeasureVO(measures[i]);
			if (hasValue) {
				data.setPrvtKeyValues(keyValues);
			}
			
			if (measures[i].getType() == MeasureVO.TYPE_NUMBER)
				data.setDataValue("" + rs.getDouble(measures[i].getDbcolumn()));
			else
				data.setDataValue(rs.getString(measures[i].getDbcolumn()));
			result[i] = data;
		}
		return result;
	}

	/**
	 * 产生清空指标数据需要的SQL语句
	 * @param aloneIds
	 * @param measures
	 * @return
	 */
	private String[] geneEmptyRepSqls(String[] aloneIds, MeasureVO[] measures) {
		// 取得指标所在数据库表
		Vector tables = getTableFromMeasure(measures);
		if (tables == null || tables.size() == 0) {
			return null;
		}
		// 根据AloneID得到in 语句的列表
		StringBuffer[] allAloneIds = getINClauseByAloneIDs(aloneIds, measures.length);
		// 产生所有更新表中数据的SQL语句
		int nGroup = allAloneIds.length;
		int nTable = tables.size();
		String[] updateSqls = new String[nTable * nGroup];
		// StringBuffer sb = new StringBuffer(); note by ljhua 2002-9-4 14:08
		for (int i = 0; i < nTable; i++) {
			StringBuffer sb = new StringBuffer(); // add by ljhua
			sb.append("update ");
			sb.append((String) tables.elementAt(i));
			sb.append(" set ");
			sb.append(geneUpdateSet((String) tables.elementAt(i), measures));
			sb.append(" where alone_id in (");
			for (int j = 0; j < nGroup; j++) {
				StringBuffer sbSql = new StringBuffer();
				sbSql.append(sb);
				sbSql.append(allAloneIds[j].toString());
				sbSql.append(")");
				updateSqls[i * nGroup + j] = sbSql.toString();
			}
		}
		return updateSqls;
	}

	/**
	 * 为一个数据库表产生清零使用的字段集
	 * @param tableName
	 * @param measures
	 * @return
	 */
	private String geneUpdateSet(String tableName, MeasureVO[] measures) {
		if (tableName == null || measures == null || measures.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < measures.length; i++) {
			if (tableName.equalsIgnoreCase(measures[i].getDbtable())) {
				sb.append(measures[i].getDbcolumn());
				sb.append("=");
				sb.append(getMeasureZeroData(measures[i]));
				sb.append(",");
			}
		}
		// 在上面的for循环中，可能会在字符串尾部多一个逗号，去掉之
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 汇总时，两张iufo_measure_pubdata表根据pubdata中的关键字值建立关联关系
	 * @param pubData
	 * @param strNewTableName
	 * @param bOmitTime
	 * @return
	 */
	private String genWhereSQL(MeasurePubDataVO pubData, String strNewTableName, boolean bOmitTime) {
		String strTableName = new String(strNewTableName);
		if (strTableName.length() > 0)
			strTableName += ".";
		StringBuffer bufSQL = new StringBuffer();
		if (pubData.getKType() != null)
			bufSQL.append(strTableName + "ktype='" + pubData.getKType() + "' ");

		KeyVO[] keys = pubData.getKeyGroup().getKeys();

		for (int i = 0; i < 10; i++) {
			String strKeyVal = pubData.getKeywordByIndex(i + 1);
			if (strKeyVal == null || strKeyVal.trim().length() <= 0 || strKeyVal.trim().equalsIgnoreCase("null"))
				continue;

			if (bOmitTime && keys.length > i && keys[i] != null && keys[i].getTimeKeyIndex() >= 0)
				continue;

			if (bufSQL.length() > 0)
				bufSQL.append(" and ");

			bufSQL.append(strTableName + "keyword" + (i + 1) + "='" + strKeyVal + "'");
		}

		return bufSQL.toString();
	}
	
	/**
	 * 根据指标、数据表、aloneid加载指标数据
	 * @param con
	 * @param sTableName
	 * @param sAloneIDs
	 * @param vMeasure
	 * @return
	 * @throws SQLException
	 */
	private Vector<MeasureDataVO> getMeasureDataVoByMeasure(Connection con, String sTableName, String[] sAloneIDs, Vector<MeasureVO> vMeasure) throws SQLException{
		MeasureVO[] measures = vMeasure.toArray(new MeasureVO[0]);
		String sql = makeSelectSQL(sTableName, measures);
		sql += " where alone_id in ";
		StringBuffer[] sbInClauses = getINClauseByAloneIDs(sAloneIDs, measures.length);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector<MeasureDataVO> vRe = new Vector<MeasureDataVO>();

		for (int j = 0; j < sbInClauses.length; j++) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(sql);
				sb.append('(');
				sb.append(sbInClauses[j]);
				sb.append(')');
				stmt = con.prepareStatement(sb.toString());
				rs = stmt.executeQuery();

				while (rs.next()) {
					MeasureDataVO[] datas = extractMeasureData(rs, measures);
					vRe.addAll(Arrays.asList(datas));
				}

			} finally {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				rs=null;
				stmt=null;
			}
		}
		return vRe;
	}

	/**
	 * 找出在某一指标数据表中的指标
	 * @param sTableName
	 * @param measureData
	 * @return
	 */
	private Vector<MeasureDataVO> getMeasureInTable(String sTableName, MeasureDataVO[] measureData) {
		Vector<MeasureDataVO> vRe = new Vector<MeasureDataVO>();

		for (int i = 0; i < measureData.length; i++) {
			if (measureData[i] == null || measureData[i].getMeasureVO() == null) {
				continue;
			}
			String sTemp = measureData[i].getMeasureVO().getDbtable();
			if (sTemp == null) {
				continue;
			}
			if (sTemp.equals(sTableName)) {
				vRe.addElement(measureData[i]);
			}
		}

		return vRe;
	}

	/**
	 * 找出在某一指标数据表中的指标
	 * @param sTableName
	 * @param measures
	 * @return
	 */
	private Vector<MeasureVO> getMeasureInTable(String sTableName, MeasureVO[] measures) {
		Vector<MeasureVO> vRe = new Vector<MeasureVO>();

		for (int i = 0; i < measures.length; i++) {
			if (measures[i] == null) {
				continue;
			}
			String sTemp = measures[i].getDbtable();
			if (sTemp == null) {
				continue;
			}
			if (sTemp.equals(sTableName)) {
				vRe.addElement(measures[i]);
			}
		}

		return vRe;
	}

	/**
	 * 根据指标返回需要将指标清空时的默认取值
	 * @param measure
	 * @return
	 */
	private String getMeasureZeroData(MeasureVO measure) {
		if (measure == null) {
			return "''";
		}
		switch (measure.getType()) {
		// 对字符类指标，返回长度为0的字符串
		case MeasureVO.TYPE_CHAR:
		case MeasureVO.TYPE_CODE:
		case MeasureVO.TYPE_NORMAL:
			return "''";
		// 对数值类指标，返回0
		case MeasureVO.TYPE_NUMBER:
			return "0";
		default:
			return "''";
		}
	}

	/**
	 * 找出一组指标数据中最大的行号
	 * @param vData
	 * @return
	 */
	private int getRowNum(Vector<MeasureDataVO> vData) {
		int iRe = 0;

		for (int i = 0; i < vData.size(); i++) {
			MeasureDataVO measureDataVO = vData.elementAt(i);
			if (measureDataVO != null) {
				if (iRe < measureDataVO.getRowNo()) {
					iRe = measureDataVO.getRowNo();
				}
			}
		}
		return iRe;
	}

	/**
	 * 得到指标对应的指标数据表数组
	 * @param measureData
	 * @return
	 */
	private Vector<String> getTable(MeasureDataVO[] measureData) {
		Vector<String> vRe = new Vector<String>();

		for (int i = 0; i < measureData.length; i++) {
			MeasureVO meavo = measureData[i].getMeasureVO();
			if (meavo != null) {
				String sTableName = meavo.getDbtable();
				if (!vRe.contains(sTableName)) {
					vRe.addElement(sTableName);
				}
			}
		}
		return vRe;
	}

	/**
	 * 在一张数据表中根据aloneID和行号分组指标数据
	 * @param datas
	 * @param tableName
	 * @return
	 */
	private Hashtable<String,Vector<MeasureDataVO>> groupMeasureDatasByAloneId_LineNo(MeasureDataVO[] datas, String tableName) {
		// 先根据指标数据的行号拆分指标数据
		Hashtable<String,Vector<MeasureDataVO>> htNos = new Hashtable<String,Vector<MeasureDataVO>>();
		for (int i = 0; i < datas.length; i++) {
			// 以aloneId_lineNo的形式做为hashtable的键值
			String key = datas[i].getAloneID() + "_" + datas[i].getRowNo();
			if (tableName.equals(datas[i].getMeasureVO().getDbtable())) {
				Vector<MeasureDataVO> vec =htNos.get(key);
				if (vec == null) {
					vec = new Vector<MeasureDataVO>();
					htNos.put(key, vec);
				}
				vec.add(datas[i]);
			}
		}
		return htNos;
	}

	/**
	 * 将指标数据按行号分组
	 * @param datas
	 * @return
	 */
	private Hashtable groupMeasureDatasByLine(MeasureDataVO[] datas) {
		// 先根据指标数据的行号拆分指标数据
		Hashtable<Integer,Vector<MeasureDataVO>> htNos = new Hashtable<Integer,Vector<MeasureDataVO>>();
		for (int i = 0; i < datas.length; i++) {
			Vector<MeasureDataVO> vec = htNos.get(new Integer(datas[i].getRowNo()));
			if (vec == null) {
				vec = new Vector<MeasureDataVO>();
				htNos.put(new Integer(datas[i].getRowNo()), vec);
			}
			vec.add(datas[i]);
		}
		return htNos;
	}

	/**
	 * 在相应的数据库表中增加一条指标为空的记录
	 * @param sTableName
	 * @param sAloneID
	 * @param keyCombPK
	 * @throws Exception
	 */
	private void initMeasureData(String[] sTableName, String sAloneID, String keyCombPK) throws Exception {
		if (sTableName == null || sAloneID == null)
			return;
		
		Connection con =null;
		try {
			con= getConnection();
			for (int i = 0; i < sTableName.length; i++) {
				String sTable = sTableName[i];
				if (sTable == null || sTable.length() == 0) {
					continue;
				}
				createMeasureData(con, sTable, sAloneID, 0, keyCombPK);
			}
		} finally {
			if (con != null)
				con.close();
		}
	}

	/**
	 * 判断指定记录是否存在
	 * @param con
	 * @param sTableName
	 * @param sAloneID
	 * @param iLineNum
	 * @return
	 * @throws java.sql.SQLException
	 */
	private boolean isMeaDataExist(Connection con,String sTableName, String sAloneID, int iLineNum) throws java.sql.SQLException {
		String sql = "select alone_id from " + sTableName + " where alone_id='" + sAloneID + "' and line_no="+ iLineNum;

		PreparedStatement prepStmt =null;
		ResultSet rs=null;
		try{
			prepStmt=con.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			boolean result = rs.next();
			return result;
		}
		finally{
			if (rs!=null)
				rs.close();
			if (prepStmt!=null)
				prepStmt.close();
		}
	}

	private String[] makeInsertSQL(String tableName, String aloneId, MeasureDataVO[] datas) {
		if (tableName == null || aloneId == null || datas == null || datas.length == 0) {
			return null;
		}
		return makeSQL(tableName, aloneId, datas, 0);

	}

	/**
	 * 返回SQL语句。此语句用来插入一行指标数据。如果指标数据为空，则插入一条只有aloneid和行号的空数据
	 * @param tableName
	 * @param aloneId
	 * @param lineNo
	 * @param datas
	 * @param keyCombPK
	 * @return
	 */
	private String makeInsertSQL(String tableName, String aloneId, int lineNo, MeasureDataVO[] datas, String keyCombPK) {
		if (tableName == null || aloneId == null) {
			return null;
		}
		if (keyCombPK == null && (datas == null || datas.length == 0))
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(tableName);
		sb.append(" (");
		sb.append("alone_id, ");
		sb.append("line_no ");
		sb.append(", ");
		sb.append("pk_key_comb ");
		if (datas != null && datas.length > 0) {
			sb.append(", ");
			for (int i = 1; i <= 10; i++) {
				sb.append("keyvalue" + i + ", ");
			}
			for (int i = 0; i < datas.length; i++) {
				sb.append(datas[i].getMeasureVO().getDbcolumn());
				if (i < datas.length - 1) {
					sb.append(", ");
				}
			}
		}
		sb.append(")");
		sb.append("values( ");
		sb.append("'" + aloneId + "', ");
		sb.append(lineNo);
		sb.append(", ");
		if (datas == null || datas.length == 0) {
			sb.append("'" + keyCombPK + "' ");
		} else {
			if (datas[0].getPrvtKeyGroupPK() != null) {
				sb.append("'" + datas[0].getPrvtKeyGroupPK() + "', ");
			} else {
				sb.append("'" + keyCombPK + "', ");
			}
			for (int i = 1; i <= 10; i++) {
				sb.append("'" + (datas[0].getKeyValueByIndex(i) == null ? "" : datas[0].getKeyValueByIndex(i)) + "', ");
			}
			for (int i = 0; i < datas.length; i++) {
				sb.append(datas[i].getDataValueForSql());
				if (i < datas.length - 1) {
					sb.append(", ");
				}
			}
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 查找指标数据的SQL语句
	 * @param tableName
	 * @param measures
	 * @return
	 */
	private String makeSelectSQL(String tableName, MeasureVO[] measures) {
		if (tableName == null || measures == null || measures.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select alone_id, pk_key_comb, line_no ");
		for (int i = 1; i <= 10; i++) {
			sb.append(", ");
			sb.append("keyvalue" + i);
		}
		for (int i = 0; i < measures.length; i++) {
			sb.append(", ");
			sb.append(measures[i].getDbcolumn());
		}
		sb.append(" from ");
		sb.append(tableName);

		return sb.toString();
	}

	/**
	 * 生成插入或更新指标数据的SQL语句
	 * @param tableName
	 * @param aloneId
	 * @param datas
	 * @param flag
	 * @return
	 */
	private String[] makeSQL(String tableName, String aloneId, MeasureDataVO[] datas, int flag) {
		if (tableName == null || aloneId == null || datas == null || datas.length == 0) {
			return null;
		}
		// 先根据指标数据的行号拆分指标数据
		Hashtable htNos = groupMeasureDatasByLine(datas);
		Enumeration lineNos = htNos.keys();
		String[] result = new String[htNos.size()];
		int i = 0;
		while (lineNos.hasMoreElements()) {
			Integer lineNo = (Integer) lineNos.nextElement();
			Vector vecDatas = (Vector) htNos.get(lineNo);
			MeasureDataVO[] mdatas = new MeasureDataVO[vecDatas.size()];
			vecDatas.copyInto(mdatas);
			String sql = flag == 0 ? makeInsertSQL(tableName, aloneId, lineNo.intValue(), mdatas, null)
					: makeUpdateSQL(tableName, aloneId, lineNo.intValue(), mdatas);
			result[i] = sql;
			i++;
		}
		return result;

	}

	/**
	 * 返回SQL语句。此语句用来插入一行指标数据。如果指标数据为空，则将除行号及aloneid外的数据清为空或0
	 * @param tableName
	 * @param aloneId
	 * @param lineNo
	 * @param datas
	 * @return
	 */
	private String makeUpdateSQL(String tableName, String aloneId, int lineNo, MeasureDataVO[] datas) {
		if (tableName == null || aloneId == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(tableName);
		sb.append(" set ");
		for (int i = 1; i <= 10; i++) {
			sb.append("keyvalue" + i + "= ");
			if (datas != null && datas.length > 0) {
				sb.append(datas[0].getKeyValueByIndex(i) == null ? "''" : "'" + datas[0].getKeyValueByIndex(i) + "'");
			} else {
				sb.append("''");
			}
			if (i <= 9) {
				sb.append(", ");
			}
		}
		if (datas != null && datas.length > 0) {
			sb.append(", ");
			for (int i = 0; i < datas.length; i++) {
				sb.append(datas[i].getMeasureVO().getDbcolumn() + "=");
				sb.append(datas[i].getDataValueForSql());
				if (i < datas.length - 1) {
					sb.append(", ");
				}
			}
		}
		sb.append(" where ");
		sb.append("alone_id=");
		sb.append("'" + aloneId + "' and ");
		sb.append("line_no=");
		sb.append(lineNo);

		return sb.toString();
	}

	/**
	 * 在相应的数据库表中删除一条记录
	 * @param con
	 * @param sTableName
	 * @param sAloneIDs
	 * @throws java.sql.SQLException
	 */
	private void removeMeasureData(Connection con,String sTableName, String[] sAloneIDs) throws java.sql.SQLException {
		if (sAloneIDs == null || sAloneIDs.length <= 0)
			return;
		PreparedStatement stmt = null;
		try {
			Vector<String> vecDeleteSQLs = new Vector<String>(10);

			StringBuffer sb = new StringBuffer();
			sb.append("(");
			int nCounter = 0;
			for (int i = 0; i < sAloneIDs.length; i++) {
				sb.append("'" + sAloneIDs[i] + "'");
				nCounter++;

				if (!(nCounter == 500 || i == sAloneIDs.length - 1)) {
					sb.append(",");
				}
				if (nCounter == 500 || i == sAloneIDs.length - 1) {
					nCounter = 0;
					sb.append(")");
					String strSQL = "delete from " + sTableName + " where alone_id in " + sb.toString();
					vecDeleteSQLs.add(strSQL);
					sb = new StringBuffer();
					sb.append("(");
				}
			}
			for (int i = 0; i < vecDeleteSQLs.size(); i++) {
				String strSQL = (String) vecDeleteSQLs.get(i);
				stmt = con.prepareStatement(strSQL);
				stmt.executeUpdate();
				stmt.close();
				stmt=null;
			}
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ignore) {
				}
			}
		}
	}

	/**
	 * 根据报表引用指标的情况，把报表数据表分为两组，以一个数组的形式返回。第一 组是报表自己指标所在的数据表集合，第二组是报表引用指标所在的数据表集合。
	 * 数组的第三项是一个哈希表，其中保存每个引用的报表对应的指标集合
	 * @param repPK
	 * @param measures
	 * @return
	 */
	private Object[] splitTablesByRef(String repPK, MeasureVO[] measures) {
		Vector<String> vRe = new Vector<String>();
		Vector<String> vec = new Vector<String>();
		Hashtable<String,Vector<MeasureVO>> ht = new Hashtable<String,Vector<MeasureVO>>();
		for (int i = 0; i < measures.length; i++) {
			MeasureVO meavo = measures[i];
			if (meavo != null) {
				String sTableName = meavo.getDbtable();
				if (repPK.equals(meavo.getReportPK()) && !vRe.contains(sTableName)) {
					vRe.addElement(sTableName);
				} else if (!repPK.equals(meavo.getReportPK())) {
					if (!vec.contains(sTableName)) {
						vec.add(sTableName);
					}
					Vector<MeasureVO> vm =ht.get(sTableName);
					if (vm == null) {
						vm = new Vector<MeasureVO>();
						ht.put(sTableName, vm);
					}
					
					if (!vm.contains(meavo)) {
						vm.add(meavo);
					}
				}
			}
		}
		Object[] result = new Object[3];
		result[0] = vRe;
		result[1] = vec;
		result[2] = ht;
		return result;
	}

	/**
	 * 在相应的数据库表中修改一条记录
	 * @param con
	 * @param sTableName
	 * @param sAloneID
	 * @param iLineNo
	 * @param vMeasureData
	 * @throws java.sql.SQLException
	 */
	private void updateMeasureData(Connection con, String sTableName, String sAloneID, int iLineNo, Vector vMeasureData)
			throws java.sql.SQLException {
		if (vMeasureData == null) {
			return;
		}

		MeasureDataVO[] datas = new MeasureDataVO[vMeasureData.size()];
		vMeasureData.copyInto(datas);
		String sql = makeUpdateSQL(sTableName, sAloneID, iLineNo, datas);

		PreparedStatement stmt =null; 
		try {
			stmt=con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
	
	/**
	 * getINClauseByAloneIDs
	 * 
	 * @return StringBuffer[]
	 */
	private StringBuffer[] getINClauseByAloneIDs(String[] aloneIds, int nMeasures) {
		// 得到生成SQL语句的数组的长度,
		int nMaxNumber = 500;
		int n = aloneIds.length / nMaxNumber;
		if (aloneIds.length % nMaxNumber > 0) {
			n++;
		}
		// 将所有的AloneId连接成一个条件的一部分，如 id1,id2,id3,...
		StringBuffer[] sbAloneIds = new StringBuffer[n];
		for (int i = 0; i < n; i++) {
			sbAloneIds[i] = new StringBuffer();
		}
		for (int i = 0, j = 0; i < aloneIds.length; i++) {
			sbAloneIds[j].append("'");
			sbAloneIds[j].append(aloneIds[i]);
			sbAloneIds[j].append("'");
			if ((i + 1) % nMaxNumber == 0 || i == aloneIds.length - 1) {
				j++;
			} else {
				sbAloneIds[j].append(",");
			}
		}
		return sbAloneIds;
	}

	private Vector<String> getMeasIDFromMeasure(MeasureVO[] measures) {
		Vector<String> vMeasID = new Vector<String>();
		for (int i = 0; i < measures.length; i++)
			vMeasID.add(measures[i].getCode());
		return vMeasID;
	}

	/**
	 * 得到主子表间的时间类型关系
	 * @param mainPubData
	 * @param subPubData
	 * @return
	 */
	private boolean[] getKeywordTimeType(MeasurePubDataVO mainPubData, MeasurePubDataVO subPubData) {
		boolean bSubTime = false;
		boolean bWeekTime = false;
		boolean bDayTime = false;

		String strMainTime = mainPubData.getKeyGroup().getTimeProp();
		String strSubTime = subPubData.getKeyGroup().getTimeProp();
		if (strMainTime.equals(nc.vo.iufo.pub.date.UFODate.NONE_PERIOD) == false
				&& strSubTime.equals(nc.vo.iufo.pub.date.UFODate.NONE_PERIOD) == false
				&& strMainTime.equals(strSubTime) == false) {
			bSubTime = true;
			bWeekTime = subPubData.getKeyGroup().getTimeProp().equals(nc.vo.iufo.pub.date.UFODate.WEEK_PERIOD);
			bDayTime = subPubData.getKeyGroup().getTimeProp().equals(nc.vo.iufo.pub.date.UFODate.DAY_PERIOD);
		}

		return new boolean[] { bSubTime, bWeekTime, bDayTime };
	}

}
