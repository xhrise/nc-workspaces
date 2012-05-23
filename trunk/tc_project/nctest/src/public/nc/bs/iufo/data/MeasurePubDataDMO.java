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
import nc.pub.iufo.cache.base.UnitCache;
import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.data.MeasurePubDataQryVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.iufo.pub.GlobalValue;
import nc.vo.iufo.pub.IDatabaseNames;
import nc.vo.iufo.pub.IDatabaseType;
import nc.vo.iufo.pub.date.UFODate;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.Toolkit;

public class MeasurePubDataDMO extends DataManageObjectIufo {
	private static String SELECT_SQL= "select alone_id, code, ktype, inputdate, formula_id, time_code, ver," 
			+" keyword1,keyword2,keyword3,keyword4,keyword5,keyword6,keyword7,keyword8,keyword9,keyword10 from "
			+DatabaseNames.IUFO_MEASURE_PUBDATA+" pub ";
	
	private static String INSERT_SQL="insert into "+DatabaseNames.IUFO_MEASURE_PUBDATA+"(alone_id,code,ktype,ver,"
			+"keyword1,keyword2,keyword3,keyword4,keyword5,keyword6,keyword7,keyword8,keyword9,keyword10,inputdate,formula_id,time_code)"
			+" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public MeasurePubDataDMO() throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
	}

	public MeasurePubDataDMO(String dbName)
			throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super(dbName);
	}

	/**
	 * 向iufo_measure_pubdata表中插入一条记录
	 * @param measurePubData
	 * @return
	 * @throws SQLException
	 */
	public String createMeasurePubData(MeasurePubDataVO measurePubData)
			throws SQLException {
		return createMeasurePubDatas(new MeasurePubDataVO[]{measurePubData})[0].getAloneID();
	}

	/**
	 * 往iufo_measure_pubdata表中插入一组记录
	 * @param measurePubDatas
	 * @return
	 * @throws SQLException
	 */
	public MeasurePubDataVO[] createMeasurePubDatas(
			MeasurePubDataVO[] measurePubDatas) throws SQLException {
		if (measurePubDatas == null)
			return null;

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			String sql = makeInsertSql();
			con = getConnection();
			stmt = con.prepareStatement(sql);
			int iBatchCount = 0;
			for (int i = 0; i < measurePubDatas.length; i++) {
				MeasurePubDataVO measurePubData = measurePubDatas[i];
				//liuyy. 添加判断，跟踪有单位关键字的 iufo_measure_pubdata表code字段为空的问题。
				if(measurePubData.isContainUnitKey() && Toolkit.isNullStr(measurePubData.getUnitPK())){
					throw new RuntimeException("MeasurePubData单位关键字值为空！");
				}
				setStmtParam(stmt, measurePubData);
				stmt.addBatch();

				if (i == measurePubDatas.length - 1 || iBatchCount++ == 100) {
					stmt.executeBatch();
					iBatchCount = 0;
					stmt.clearBatch();
				}
			}
			return measurePubDatas;
		} catch (SQLException e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 根据结果集生成MeasurePubDataVO
	 * @param data
	 * @param rs
	 * @throws SQLException
	 */
	private void extractData(MeasurePubDataVO data, ResultSet rs)
			throws SQLException {
		data.setAloneID(rs.getString(1));
		data.setUnitPK(rs.getString(2));
		data.setKType(rs.getString(3));
		data.setKeyGroup(nc.bs.iufo.cache.IUFOBSCacheManager.getSingleton()
				.getKeyGroupCache().getByPK(data.getKType()));
		data.setInputDate(rs.getString(4));
		data.setFormulaID(rs.getString(5));
		data.setTimeCode(rs.getString(6));
		data.setVer(rs.getInt(7));
		data.setKeyword1(rs.getString(8));
		data.setKeyword2(rs.getString(9));
		data.setKeyword3(rs.getString(10));
		data.setKeyword4(rs.getString(11));
		data.setKeyword5(rs.getString(12));
		data.setKeyword6(rs.getString(13));
		data.setKeyword7(rs.getString(14));
		data.setKeyword8(rs.getString(15));
		data.setKeyword9(rs.getString(16));
		data.setKeyword10(rs.getString(17));
	}

	/**
	 * 根据aloneid查找iufo_measure_pubdata表记录
	 * @param sAlone_ID
	 * @return
	 * @throws SQLException
	 */
	public MeasurePubDataVO findByAloneID(String strAloneID) throws SQLException {
		MeasurePubDataVO[] pubDatas=findByAloneIDs(new String[]{strAloneID});
		if (pubDatas!=null && pubDatas.length>0)
			return pubDatas[0];
		return null;
	}

	/**
	 * 查找一组aloneid对应的iufo_measure_pubdata表中的记录
	 * @param sAlone_IDs
	 * @return
	 * @throws SQLException
	 */
	public MeasurePubDataVO[] findByAloneIDs(String[] sAlone_IDs)
			throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			String sql = getSelectSQL() + " where alone_id = ?";
			Vector<MeasurePubDataVO> vecPubDatas = new Vector<MeasurePubDataVO>();
			con = getConnection();
			stmt = con.prepareStatement(sql);
			for (int i = 0; i < sAlone_IDs.length; i++) {
				stmt.setString(1, sAlone_IDs[i]);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					MeasurePubDataVO measurePubData = new MeasurePubDataVO();
					extractData(measurePubData, rs);
					vecPubDatas.add(measurePubData);
				} else {
					vecPubDatas.add(null);
				}
				if (rs != null)
					rs.close();
			}
			return vecPubDatas.toArray(new MeasurePubDataVO[0]);
		} finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 根据查询条件的MeasurePubDataVO，查找出主表及对应该主表的动态区中的MeasurePubDataVO
	 * @param pubData,查询条件的MeasurePubDataVO
	 * @param strUnitCode,当前单位
	 * @param keyCombPKs，需要查找的动态区的关键字组合PK
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Vector<MeasurePubDataVO> findByCondition(MeasurePubDataVO pubData,
			String strUnitCode, int iVer,String[] keyCombPKs,boolean bIgnoreFormuaID)
			throws java.sql.SQLException {
		Connection con = null;
		try {
			con = getConnection();
			Vector<MeasurePubDataVO> result = innerFindByCondition(con, pubData, strUnitCode, false, iVer,
					keyCombPKs, null,bIgnoreFormuaID);
			return result;
		} finally {
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 根据主表的MeasurePubDataVO，查找出主表及对应该主表的动态区中的MeasurePubDataVO
	 * @param pubData，主表MeasurePubDataVO
	 * @param strUnitCode,当前单位
	 * @param includeSubUnit，是否包含下级
	 * @param keyCombPKs，关键字组合PK
	 * @param strOrgPK,当前组织PK
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Vector<MeasurePubDataVO> findByCondition(MeasurePubDataVO pubData,
			String strUnitCode, boolean includeSubUnit, String[] keyCombPKs,
			String strOrgPK) throws java.sql.SQLException {
		Connection con = null;
		try {
			con = getConnection();
			Vector<MeasurePubDataVO> result = innerFindByCondition(con, pubData, strUnitCode,
					includeSubUnit, 0, keyCombPKs, strOrgPK,false);
			return result;
		} finally {
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 根据设置的MeasurePubDataVO，得到aloneid的值
	 * @param vPubData
	 * @return
	 * @throws java.sql.SQLException
	 */
	public MeasurePubDataVO[] findByKeywordArray(MeasurePubDataVO[] vPubData)
			throws java.sql.SQLException {
		Connection con = null;
		try {
			Vector<MeasurePubDataVO> vRetPubData = new Vector<MeasurePubDataVO>();
			con = getConnection();
			for (int i = 0; i < vPubData.length; i++) {
				Vector<MeasurePubDataVO> vec = innerFindByCondition(con,
						(MeasurePubDataVO) vPubData[i], null, false, 0, null,
						null,false);
				if (vec.size() > 0)
					vRetPubData.add(vec.get(0));
				else
					vRetPubData.add(null);
			}
			return vRetPubData.toArray(new MeasurePubDataVO[0]);
		} finally {
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public MeasurePubDataVO findByKeywords(MeasurePubDataVO measurePubData)
			throws SQLException {
		MeasurePubDataVO[] vPubData = { measurePubData };
		return (MeasurePubDataVO) findByKeywordArray(vPubData)[0];
	}

	/**
	 * 查找某一汇总结果对应的所有MeasurePubDataVO,包括舍位版本的
	 * @param pubData
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Vector<MeasurePubDataVO> findByTotalCondition(
			MeasurePubDataVO pubData) throws java.sql.SQLException {
		Connection con = null;
		try {
			con = getConnection();
			Vector<MeasurePubDataVO> result = innerFindByCondition(con, pubData, pubData.getUnitPK(),
					false, pubData.getVer(), null, null,false);
			return result;
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
				}
		}
	}

	private String getSelectSQL() {
		return SELECT_SQL;
	}

	/**
	 * 拼查找下级单位记录的SQL语句
	 * @param unitID
	 * @param dbType
	 * @param strOrgPK
	 * @return
	 */
	private String getSelectSQLWithSubs(String unitID, String dbType,String strOrgPK) {
		UnitCache uc = IUFOBSCacheManager.getSingleton().getUnitCache();
		String strOrgCode = uc.getUnitInfoByPK(unitID).getPropValue(strOrgPK);
		StringBuffer sb = new StringBuffer();
		sb.append("select pub.alone_id, pub.code, pub.ktype, pub.inputdate, pub.formula_id, pub.time_code, pub.ver, ");
		sb.append("pub.keyword1,pub.keyword2,pub.keyword3,pub.keyword4,pub.keyword5,pub.keyword6,pub.keyword7,pub.keyword8,pub.keyword9,pub.keyword10 ");
		sb.append("from ");
		sb.append(DatabaseNames.IUFO_MEASURE_PUBDATA);
		sb.append(" pub, ");
		sb.append(DatabaseNames.IUFO_UNIT_INFO_TABLE);
		sb.append(" unit ");
		sb.append("where pub.code = unit.unit_id");
		sb.append(" and unit." + strOrgPK + " like '");
		sb.append(strOrgCode);
		sb.append("'");
		if (IDatabaseType.DATABASE_ORACLE.equalsIgnoreCase(dbType)
				|| IDatabaseType.DATABASE_DB2.equalsIgnoreCase(dbType)) {
			sb.append("||");
		} else {
			sb.append("+");
		}
		sb.append("'%' ");

		return sb.toString();
	}

	/**
	 * 检查一个PubData中是否有单位关键字
	 * @param pubData
	 * @return
	 */
	private boolean hasUnitKey(MeasurePubDataVO pubData) {
		if (pubData == null) {
			return false;
		}
		String keyCombPK = pubData.getKType();
		KeyGroupVO kg = null;
		if (keyCombPK != null) {
			KeyGroupCache kgc = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
			kg = kgc.getByPK(keyCombPK);
		} else {
			kg = pubData.getKeyGroup();
		}
		if (kg == null) {
			return false;
		}
		KeyVO[] keys = kg.getKeys();
		if (keys == null) {
			return false;
		}
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null
					&& KeyVO.CODE_TYPE_CORP.equals(keys[i].getCode())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据查询条件生成查询MeasurePubData记录的SQL语句的方法
	 * @param pubData，查询条件VO
	 * @param strUnitCode,当前单位PK
	 * @param includeSubUnit，是否包含下级单位
	 * @param ver，报表数据版本
	 * @param keyCombPKs，要查找的关键字组合PK
	 * @param strOrgPK，当前组织
	 * @return
	 * @throws java.sql.SQLException
	 */
	private String getFindSQLByCondition(MeasurePubDataVO pubData, String strUnitPK,
			boolean includeSubUnit, int ver, String[] keyCombPKs,
			String strOrgPK,boolean bIgnoreFormulaID){
		String sql = getSelectSQL();
		boolean sqlWithSub = false;
		
		//如果单位不为空，且包含下级，则拼查找范围包含下级的SQL语句
		if (includeSubUnit && strUnitPK != null && strUnitPK.trim().length()>0) {
			sql = getSelectSQLWithSubs(strUnitPK, GlobalValue.DATABASE_TYPE,strOrgPK);
			sqlWithSub = true;
		}

		//生成where条件
		String strCond = "";
		
		//如果有单位关键字，且当前单位不为空，且不包含下级单位，则设置单位条件
		boolean hasUnitKey = hasUnitKey(pubData);
		if (!includeSubUnit && hasUnitKey && strUnitPK != null&& strUnitPK.length() > 0
				&& (pubData != null && pubData.getUnitPK() == null))
			strCond += "code='" + strUnitPK + "' ";

		//设置数据版本条件
		if (ver >= 0) {
			if (strCond.length() > 0) {
				strCond += " and ";
			}
			// 非汇总表所有类型数据
			if (pubData == null || pubData.getVer() < 0)
				strCond += " not(ver between 300 and 999) and not(ver >3999)";
			else if (pubData.getVer() == 0)
				strCond += "ver<300 ";
			else
				strCond += "ver=" + (pubData.getVer());
		}
		
		if (pubData != null) {
			//设置关键字组合条件
			if (pubData.getKType() != null && pubData.getKType().length() > 0) {
				if (strCond.length() > 0)
					strCond += " and ";
				strCond += " ktype='" + pubData.getKType() + "'";
			}

			//设置formulaid条件
			if (bIgnoreFormulaID==false){
				if (strCond.length() > 0)
					strCond += " and ";
			
				if (pubData.getFormulaID() != null
						&& pubData.getFormulaID().trim().length() > 0
						&& pubData.getFormulaID().trim().equalsIgnoreCase("null") == false)
					strCond += " formula_id='" + pubData.getFormulaID().trim()+ "' ";
				else
					strCond += " formula_id is null";
			}

			//设置其他关键字值条件
			String keyCond = processKeywordCondition(pubData, keyCombPKs);
			if (strCond.length() > 0 && keyCond.length() > 0) {
				strCond += " and ";
				strCond += keyCond;
			}
		}

		//如果对应多个关键字组合，设置关键字组合条件
		if (pubData.getKType() == null) {
			StringBuffer inCond = new StringBuffer();
			if (keyCombPKs != null && keyCombPKs.length > 0) {
				for (int i = 0; i < keyCombPKs.length; i++) {
					inCond.append("'");
					inCond.append(keyCombPKs[i]);
					inCond.append("',");
				}
				inCond.deleteCharAt(inCond.length() - 1);
			}

			if (strCond.length() > 0 && inCond.length() > 0) {
				strCond += " and ";
				strCond += "ktype in (" + inCond.toString() + ")";
			}
		}

		if (strCond.length() > 0) {
			if (!sqlWithSub) {
				sql += " where " + strCond;
			} else {
				sql += " and " + strCond;
			}
		}

		sql += " order by inputdate";
		return sql;
	}

	/**
	 * 根据查询条件查询MeasurePubData记录的公共方法
	 * @param con，
	 * @param pubData，查询条件VO
	 * @param strUnitCode,当前单位PK
	 * @param includeSubUnit，是否包含下级单位
	 * @param ver，报表数据版本
	 * @param keyCombPKs，要查找的关键字组合PK
	 * @param strOrgPK，当前组织
	 * @return
	 * @throws java.sql.SQLException
	 */
	private Vector<MeasurePubDataVO> innerFindByCondition(Connection con,
			MeasurePubDataVO pubData, String strUnitPK,
			boolean includeSubUnit, int ver, String[] keyCombPKs,
			String strOrgPK,boolean bIngoreFormuaID) throws java.sql.SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Vector<MeasurePubDataVO> vRetPubData = new Vector<MeasurePubDataVO>();
			stmt = con.createStatement();
			rs = stmt.executeQuery(getFindSQLByCondition(pubData, strUnitPK, includeSubUnit, ver, keyCombPKs, strOrgPK,bIngoreFormuaID));
			while (rs.next()) {
				MeasurePubDataVO measurePubDataVO = new MeasurePubDataVO();
				extractData(measurePubDataVO, rs);
				vRetPubData.add(measurePubDataVO);
			}
			return vRetPubData;
		} finally {
			if (rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 汇总时，根据汇总条件SQL语句、指标数据表、汇总结果主表MeasurePubDataVO、主表关键字组合、子表关键字组合,得到需要生成汇总结果的MeasurePubDataVO数组
	 * @param strCond，汇总条件SQL语句
	 * @param pubData，汇总结果主表的MeasurePubDataVO
	 * @param mainKeyGroup，主表关键字组合
	 * @param subKeyGroup，子表关键字组合
	 * @param strDBTable，指标数据表
	 * @return
	 * @throws java.sql.SQLException
	 */
	public MeasurePubDataVO[] loadAllTotalPubDatas(String strCond,
			MeasurePubDataVO pubData, KeyGroupVO mainKeyGroup,
			KeyGroupVO subKeyGroup, String strDBTable)
			throws java.sql.SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		try {
			Vector<MeasurePubDataVO> vRetData = new Vector<MeasurePubDataVO>();
			con = getConnection();
			
			//判断是否主子表都有时间关键字，子表关键字是否有周、日
			boolean bSubTime = false;
			boolean bWeekTime = false;
			boolean bDayTime = false;
			String strMainTime = mainKeyGroup.getTimeProp();
			String strSubTime = subKeyGroup.getTimeProp();
			if (strMainTime.equals(nc.vo.iufo.pub.date.UFODate.NONE_PERIOD) == false
					&& strSubTime.equals(nc.vo.iufo.pub.date.UFODate.NONE_PERIOD) == false
					&& strMainTime.equals(strSubTime) == false) {
				bSubTime = true;
				bWeekTime = subKeyGroup.getTimeProp().equals(
						UFODate.WEEK_PERIOD);
				bDayTime = subKeyGroup.getTimeProp().equals(UFODate.DAY_PERIOD);
			}

			//主子表相同关键字，其在各自关键字组合中的位置可能不相同，建立两个间的对应关系
			Hashtable<Integer, Integer> hashPos = new Hashtable<Integer, Integer>();
			KeyVO[] mainKeys = mainKeyGroup.getKeys();
			KeyVO[] subKeys = subKeyGroup.getKeys();
			for (int i = 0; i < subKeys.length; i++) {
				for (int j = 0; j < mainKeys.length; j++) {
					if (mainKeys[j].equals(subKeys[i])) {
						hashPos.put(new Integer(i), new Integer(j));
						break;
					}
				}
			}

			//查找的字段的语句
			StringBuffer bufItem = new StringBuffer();
			
			//生成要读取及分组用的列
			for (int i = 0; i < subKeys.length; i++) {
				// 判断是否子表中的关键字主表中存在，如果存在，且主表MeasurePubDataVO已经存在该关键字值，则不用从数据库中读取
				int iMainPos = -1;
				if (hashPos.get(new Integer(i)) != null)
					iMainPos = ((Integer) hashPos.get(new Integer(i))).intValue();

				if (iMainPos >= 0&& pubData.getKeywordByIndex(iMainPos + 1) != null)
					continue;

				if (bSubTime == false || subKeys[i].getTimeKeyIndex() < 0)
					bufItem.append("t1.keyword" + (i + 1) + ",");
				else {
					// 对于主子表同时有时间关键字的情况,需要区分子表时间关键字是否为周,其对TimeCode的截断处理不相同
					// 因为同一周，不同年，其前部分TimeCode的值可能不相同

					// 确定TimeCode要截断的部分
					int iIndex, iLen;
					if (!bDayTime) {
						if (bWeekTime) {
							// 对周类型，只需要取周部分的TimeCode的两位
							iIndex = 2 * subKeyGroup.getTimeKey().getTimeKeyIndex() + 2;
							iLen = 2;
						} else {
							iIndex = 2 * (mainKeyGroup.getTimeKey().getTimeKeyIndex() + 1) + 2;
							iLen = 2 * (subKeyGroup.getTimeKey().getTimeKeyIndex() - mainKeyGroup.getTimeKey().getTimeKeyIndex());
						}
						if (GlobalValue.DATABASE_TYPE.equals(IDatabaseType.DATABASE_SQLSERVER)) {
							bufItem.append("substring(t1.time_code,"+ (iIndex + 1) + "," + iLen + "),");
						} else {
							bufItem.append("substr(t1.time_code,"+ (iIndex + 1) + "," + iLen + "),");
						}
					} else {
						iIndex = 2 * (mainKeyGroup.getTimeKey().getTimeKeyIndex() + 1) + 2;
						iLen = 2 * (subKeyGroup.getTimeKey().getTimeKeyIndex() - mainKeyGroup.getTimeKey().getTimeKeyIndex()) - 4;

						int iIndex1 = 2 * subKeyGroup.getTimeKey()
								.getTimeKeyIndex() + 2;
						if (GlobalValue.DATABASE_TYPE.equals(IDatabaseType.DATABASE_SQLSERVER)) {
							bufItem.append("substring(t1.time_code,"
									+ (iIndex + 1) + "," + iLen
									+ ")+substring(t1.time_code,"
									+ (iIndex1 + 1) + ",2),");
						} else
							bufItem.append("substr(t1.time_code,"
									+ (iIndex + 1) + "," + iLen
									+ ")||substr(t1.time_code," + (iIndex1 + 1)
									+ ",2),");
					}
				}
			}
			
			//去掉查找字段语句的最后一个逗号
			bufItem.delete(bufItem.length() - 1, bufItem.length());

			//查找的SQL语句
			StringBuffer bufSQL = new StringBuffer();
			bufSQL.append("select ");

			//如果关联一张指标数据表，查找出来的记录可能有重复的，使用distinct
			if (strDBTable != null)
				bufSQL.append(" distinct ");

			bufSQL.append(bufItem);
			bufSQL.append(" from ");
			bufSQL.append(DatabaseNames.IUFO_MEASURE_PUBDATA + " t1,");

			//如果主子表同时有时间关键字，则需要从iufo_measure_pubdata表查找两次，第二个表负责和查询条件
			//表根据Alone_id做关联，查出主KType的表，第一个表是Select语句的主表，其KType为子表KType,两张
			//表之间根据Keyword和TimeCode条件关联

			//如果主子表不是同时有时间关键字，则iufo_measure_pubdata表直接和查询条件表通过Alone_id关联即可
			bufSQL.append(strCond + " t2");

			if (strDBTable != null) {
				bufSQL.append("," + strDBTable + " t4");
			}

			bufSQL.append(" where t1.ver=t2.ver and t2.ktype='"+ mainKeyGroup.getKeyGroupPK() + "'");
			bufSQL.append(" and t1.ktype='" + subKeyGroup.getKeyGroupPK()+ "' ");

			//由于主子表关联字位置不相同，要根据位置间的对应关系建立关联条件
			Enumeration<Integer> enumPos = hashPos.keys();
			while (enumPos.hasMoreElements()) {
				Integer iSubKeyPos = (Integer) enumPos.nextElement();
				Integer iMainKeyPos = (Integer) hashPos.get(iSubKeyPos);
				bufSQL.append(" and t1.keyword" + (iSubKeyPos.intValue() + 1)+ "=t2.keyword" + (iMainKeyPos.intValue() + 1));
			}

			if (bSubTime) {
				// 建立两张MeasurePubData表之间的TimeCode关联条件
				int iIndex = 2 * (mainKeyGroup.getTimeKey().getTimeKeyIndex() + 1) + 2;
				if (GlobalValue.DATABASE_TYPE.equals(IDatabaseType.DATABASE_SQLSERVER)) {
					bufSQL.append(" and substring(t1.time_code,1," + iIndex
							+ ")=substring(t2.time_code,1," + iIndex + ")");
				} else {
					bufSQL.append(" and substr(t1.time_code,1," + iIndex
							+ ")=substr(t2.time_code,1," + iIndex + ")");
				}
			}
			
			//如果还有指标数据表，还要让指标数据表与指标公共数据表关联
			if (strDBTable != null)
				bufSQL.append(" and t4.alone_id=t1.alone_id ");

			bufSQL.append(" group by ");
			bufSQL.append(bufItem);

			stmt = con.prepareStatement(bufSQL.toString());
			set = stmt.executeQuery();
			while (set.next()) {
				MeasurePubDataVO onePubData = new MeasurePubDataVO();
				onePubData.setKeyGroup(subKeyGroup);
				onePubData.setKType(subKeyGroup.getKeyGroupPK());

				//如果没有指标数据表，表示只是需要查找是否需要生成汇总结果的MeasurePubDataVO记录，直接返回一条记录即可
				if (strDBTable == null)
					return new MeasurePubDataVO[] { onePubData };

				int iValidPos = 1;
				for (int i = 0; i < subKeys.length; i++) {
					int iMainPos = -1;
					if (hashPos.get(new Integer(i)) != null)
						iMainPos = ((Integer) hashPos.get(new Integer(i))).intValue();

					// 主表中已有的关键字，且主MeasurPubdataVO已经有此关键字值的，直接从主MeasurePubDataVO中取
					if (iMainPos >= 0 && pubData.getKeywordByIndex(iMainPos + 1) != null) {
						onePubData.setKeywordByIndex(i + 1, pubData.getKeywordByIndex(iMainPos + 1));
					} else {
						String strKeyVal = set.getString(iValidPos++);
						if (strKeyVal != null
								&& (strKeyVal.trim().length() <= 0 || strKeyVal.trim().equalsIgnoreCase("null") == true))
							strKeyVal = null;

						// 非子表时间关键字，直接设值
						if (bSubTime == false || subKeys[i].getTimeKeyIndex() < 0)
							onePubData.setKeywordByIndex(i + 1, strKeyVal);
						else {
							// 需要区分周时间类型，其从主MeasurePubdataVO的TimeCode截取的值不相同
							int iIndex;
							if (bDayTime == false) {
								if (bWeekTime == false)
									iIndex = 2 * (mainKeyGroup.getTimeKey().getTimeKeyIndex() + 1) + 2;
								else
									iIndex = 2 * (subKeyGroup.getTimeKey().getTimeKeyIndex() + 1);
								onePubData.setKeywordByIndex(i + 1, UFODate.getDateByTimeCode(pubData.getTimeCode().substring(0,iIndex)+ strKeyVal));
							} else {
								iIndex = 2 * (mainKeyGroup.getTimeKey().getTimeKeyIndex() + 1) + 2;
								String strDate1 = UFODate.getDateByTimeCode(pubData.getTimeCode().substring(0,iIndex)
												+ strKeyVal.substring(0,strKeyVal.length() - 2));
								strKeyVal = strKeyVal.substring(strKeyVal.length() - 2, strKeyVal.length());
								String strDate = "";
								int iCount = 0;
								while ((iCount++) <= 5) {
									strDate = strDate1.toString().substring(0,8)+ strKeyVal;
									try {
										UFODate date = new UFODate(strDate);
										if (date.toString().length() > 0) {
											strDate = date.toString();
											break;
										}
									} catch (Exception e) {
										AppDebug.debug(e);//@devTools e.printStackTrace();
									}
									int iDay = Integer.parseInt(strKeyVal);
									strKeyVal = "" + (iDay - 1);
								}
								onePubData.setKeywordByIndex(i + 1, strDate);
							}
						}
					}
				}
				vRetData.add(onePubData);
			}
			return (MeasurePubDataVO[]) vRetData.toArray(new MeasurePubDataVO[0]);
		} finally {
			if (set != null)
				try {
					set.close();
				} catch (SQLException e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 查找关键字组合对应的MeasurePubDataVO
	 * @param keyCombPKs
	 * @return
	 * @throws java.lang.Exception
	 */
	public Vector<MeasurePubDataVO> loadPubDataByKeyCombPKs(String[] keyCombPKs)
			throws java.lang.Exception {
		if (keyCombPKs == null || keyCombPKs.length == 0)
			return null;
		
		Connection con = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			Vector<MeasurePubDataVO> vec = new Vector<MeasurePubDataVO>();
			String sql = getSelectSQL();
			
			con = getConnection();
			for (int i = 0; i < keyCombPKs.length; i++) {
				StringBuffer inCond = new StringBuffer();
				inCond.append("'");
				inCond.append(keyCombPKs[i]);
				inCond.append("',");
				if ((i > 0 && i % 100 == 0) || i == keyCombPKs.length - 1) {
					inCond.deleteCharAt(inCond.length() - 1);
					stat = con.createStatement();
					rs = stat.executeQuery(sql + " where ktype in ("
							+ inCond.toString() + ")");
					while (rs.next()) {
						MeasurePubDataVO pubData = new MeasurePubDataVO();
						extractData(pubData, rs);
						vec.add(pubData);
					}
					rs.close();
					stat.close();
				}
			}
			return vec;
		} finally {
			if (rs != null){
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (stat != null){
				try {
					stat.close();
				} catch (Exception e) {
				}
			}
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}


	/**
	 * 此处插入方法描述。 创建日期：(2003-8-19 16:14:52)
	 * 
	 * @return java.lang.String
	 */
	private String makeInsertSql() {
		return INSERT_SQL;
	}

	/**
	 * 生成timecode的查询条件
	 * @param keyCombPKs
	 * @param kc
	 * @param pubData
	 * @return
	 */
	private String makeTimeCodeCondition(String[] keyCombPKs, KeyGroupCache kc,
			MeasurePubDataVO pubData) {
		if (keyCombPKs == null || keyCombPKs.length == 0 || pubData == null) {
			return null;
		}
		String timeCode = pubData.getTimeCode();
		if (timeCode == null) {
			return null;
		}
		// 得到本报表包含的关键字组合中时间属性的最大和最小值(年>半年>季>月>旬>周>日)
		int max = -1;
		int min = 100;
		if (keyCombPKs != null) {
			for (int i = 0; i < keyCombPKs.length; i++) {
				KeyGroupVO kg1 = kc.getByPK(keyCombPKs[i]);
				KeyVO timeKey = kg1.getTimeKey();
				if (timeKey == null)
					continue;

				int timeprop = timeKey.getTimeKeyIndex();
				if (timeprop > max) {
					max = timeprop;
				}
				if (timeprop < min) {
					min = timeprop;
				}
			}
		}
		if (min == max) {
			return null;
		}
		String tc = timeCode;
		if (tc != null) {
			// 根据时间属性取TIMECODE的子串
			/*
			 * 0:年 --4 1:半年--6 2:季 --8 3:月 --10 4:旬 --12 5:周 --14 6:日 --16
			 * 当为日报时,不需要使用time_code like "xxx%"的形式,所以返回null
			 */
			int pos = (min + 1) * 2 + 2;
			if (pos == tc.length()) {
				return null;
			}
			if (max == 5) {
				// 子表关键字含有周报，需要做特殊处理
				return makeWeekCondition(min, pubData.getInputDate());
			}
			tc = timeCode.substring(0, pos);
		}
		return tc;
	}

	/**
	 * 对应周关键字，直接生成inputdate条件
	 * @param timeProp
	 * @param inputDate
	 * @return
	 */
	private String makeWeekCondition(int timeProp, String inputDate) {
		String prop = null;
		if (inputDate == null) {
			return null;
		}
		switch (timeProp) {
		case 0:
			prop = UFODate.YEAR_PERIOD;
			break;
		case 1:
			prop = UFODate.HALFYEAR_PERIOD;
			break;
		case 2:
			prop = UFODate.SEASON_PERIOD;
			break;
		case 3:
			prop = UFODate.MONTH_PERIOD;
			break;
		case 4:
			prop = UFODate.TENDAYS_PERIOD;
			break;
		default:
			break;
		}
		if (prop == null) {
			return null;
		}
		UFODate startDate = new UFODate(inputDate);
		startDate = startDate.getStartDay(UFODate.WEEK_PERIOD);
		startDate = startDate.getStartDay(prop);
		startDate = startDate.getEndDay(UFODate.WEEK_PERIOD);
		StringBuffer sb = new StringBuffer();
		sb.append(" inputdate >= '");
		sb.append(startDate.toString());
		sb.append("' and inputdate <= '");
		sb.append(inputDate);
		sb.append("' ");
		return sb.toString();
	}

	/**
	 * 生成关键字值的条件
	 * @param pubData
	 * @param keyCombPKs
	 * @return
	 */
	private String processKeywordCondition(MeasurePubDataVO pubData,String[] keyCombPKs) {
		KeyGroupCache kc = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
		if (pubData.getKeyGroup() == null) {
			pubData.setKeyGroup(kc.getByPK(pubData.getKType()));
		}
		KeyGroupVO kg = pubData.getKeyGroup();

		String timeCode = null;
		if (kg.getTimeKey() != null) {
			timeCode = makeTimeCodeCondition(keyCombPKs, kc, pubData);
		}

		KeyVO[] keys = kg.getKeys();
		StringBuffer sb = new StringBuffer();
		for (int i = 1; keys != null && i <= keys.length; i++) {
			if (keys.length >= i
					&& keys[i - 1].getType() == KeyVO.TYPE_TIME
					&& timeCode == null
					&& !nc.vo.iufo.pub.date.UFODate.NONE_DATE.equals(pubData.getInputDate())) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				if (keys[i - 1].getTimeKeyIndex() == 4) {
					// 旬时做特殊处理
					String tCode = UFODate.getTimeCode(pubData.getInputDate());
					// 只取旬的时间编码
					tCode = tCode.substring(0, 12);
					sb.append("time_code like '" + tCode + "%'");
				} else {
					sb.append("inputdate = '" + pubData.getInputDate() + "'");
				}
			}
			String key = pubData.getKeywordByIndex(i);
			if (keys.length >= i && key != null && key.length() > 0) {
				if (keys[i - 1].getType() != KeyVO.TYPE_TIME
						|| timeCode == null) {
					if (keys[i - 1].getTimeKeyIndex() == 4) {
						// 旬时不再处理
					} else {
						if (sb.length() > 0)
							sb.append(" and ");
						// liuyy 2005-5-14 单引号问题修改
						key = this.convertSQL(key);
						sb.append("keyword" + i + "='" + key + "' ");
					}
				} else {
					if (sb.length() > 0)
						sb.append(" and ");
					if (timeCode.indexOf("inputdate") < 0) {
						// 如果不是周报，则返回的内容不会含有上述字符串
						sb.append("time_code like '" + timeCode + "%' ");
					} else {
						// 周报时直接返回条件
						sb.append(timeCode);
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 多级服务器导出报表数据时，根据录入的条件查找记录时，生成的关键字部分的查询条件
	 * @param qryData
	 * @return
	 */
	private String processKeywordCondition1(MeasurePubDataQryVO qryData) {
		KeyGroupCache kc = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
		if (qryData.getKeyGroup() == null) {
			qryData.setKeyGroup(kc.getByPK(qryData.getKType()));
		}
		KeyGroupVO kg = qryData.getKeyGroup();

		KeyVO[] keys = kg.getKeys();
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= 10; i++) {
			if (keys.length >= i && qryData.getKeywordByIndex(i) != null
					&& qryData.getKeywordByIndex(i).length() > 0) {
				//单位、时间关键字已经处理
				if (qryData.isIncSubUnit()
						&& KeyVO.CORP_PK.equals(keys[i - 1].getKeywordPK())) {
					continue;
				}
				if (keys[i - 1].getType() != KeyVO.TYPE_TIME) {
					if (sb.length() > 0)
						sb.append(" and ");
					sb.append("keyword" + i + "='"
							+ qryData.getKeywordByIndex(i) + "' ");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 根据aloneid，删除iufo_measure_pubdata表中的记录
	 * @param sAloneIDs
	 * @throws SQLException
	 */
	public void removeMeasurePubDatas(String[] sAloneIDs) throws SQLException {
		if (sAloneIDs == null || sAloneIDs.length == 0)
			return;
		
		Arrays.sort(sAloneIDs);

		Connection con = null;

		//将aloneid分组，得到多条删除语句
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
				String strSQL = "delete from "+ DatabaseNames.IUFO_MEASURE_PUBDATA+ " where alone_id in " + sb.toString();
				vecDeleteSQLs.add(strSQL);
				sb = new StringBuffer();
				sb.append("(");
			}
		}

		PreparedStatement stmt = null;
		try {
			con = getConnection();
			for (int i = 0; i < vecDeleteSQLs.size(); i++) {
				stmt = con.prepareStatement((String) vecDeleteSQLs.get(i));
				stmt.executeUpdate();
			}

		} finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void setStmtParam(PreparedStatement stmt, MeasurePubDataVO pubData)
			throws java.sql.SQLException {
		if (pubData.getAloneID() == null) {
			pubData.setAloneID(IDMaker.makeID(GlobalValue.ID_LEN));
		}
		stmt.setString(1, pubData.getAloneID()); // 唯一编码
		stmt.setString(2, pubData.getUnitPK()); // 单位编码
		stmt.setString(3, pubData.getKType()); // 类型
		stmt.setInt(4, pubData.getVer()); // 版本
		stmt.setString(5, pubData.getKeyword1());
		stmt.setString(6, pubData.getKeyword2());
		stmt.setString(7, pubData.getKeyword3());
		stmt.setString(8, pubData.getKeyword4());
		stmt.setString(9, pubData.getKeyword5());
		stmt.setString(10, pubData.getKeyword6());
		stmt.setString(11, pubData.getKeyword7());
		stmt.setString(12, pubData.getKeyword8());
		stmt.setString(13, pubData.getKeyword9());
		stmt.setString(14, pubData.getKeyword10());
		stmt.setString(15, pubData.getInputDate()); // 日期
		stmt.setString(16, pubData.getFormulaID()); // 汇总编码
		stmt.setString(17, pubData.getTimeCode()); // 时间编码
	}

	/**
	 * 判断是否有对应某一版本的数据，用于删除舍位条件时，判断是否有舍位数据未删除
	 * @param iVer
	 * @return
	 * @throws SQLException
	 */
	public boolean isHaveDataByVersion(int iVer) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet set = null;
		try {
			boolean bHasData = false;
			con = getConnection();
			String strSQL = "select t1.ver,count(t1.alone_id) from "+ DatabaseNames.IUFO_MEASURE_PUBDATA + " t1,";
			strSQL += DatabaseNames.IUFO_CHECK_RESULT + " t2 ";
			strSQL += "where ";
			strSQL += " t1.ver>=1000";
			strSQL += " and t1.alone_id=t2.aloneid group by t1.ver";
			
			stmt = con.createStatement();
			set = stmt.executeQuery(strSQL);
			while (set.next()){
				int iDataVer=set.getInt(1);
				int iCount=set.getInt(2);
				if (iCount>0 && (iDataVer-999-iVer)%10==0){
					bHasData=true;
					break;
				}
			}
			
			return bHasData;
		} finally {
			if (set != null){
				try {
					set.close();
				} catch (SQLException e) {
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 多级服务器导出报表数据时，根据选中的条件，查找出对应每张表，其有数据的MeasurePubDataVO数组
	 * @param qryData
	 * @param strRepIDs
	 * @return
	 * @throws SQLException
	 */
	public Hashtable loadInputRepAloneIDs(MeasurePubDataQryVO qryData,
			String[] strRepIDs) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet set = null;
		Hashtable<String,List<String>> hashRep = new Hashtable<String,List<String>>();
		try {
			con = getConnection();

			boolean shouldAnd = false;
			String strUnitCode = qryData.getUnitPK();
			String strSQL = "";
			if (strUnitCode != null && strUnitCode.length() > 0) {
				if (qryData.isIncSubUnit() == false) {
					strSQL = getSelectSQL() + "where code='" + strUnitCode
							+ "' ";
				} else {
					strSQL = getSelectSQLWithSubs(strUnitCode,
							GlobalValue.DATABASE_TYPE, qryData.getOrgPK());
				}
				shouldAnd = true;
			} else {
				strSQL = getSelectSQL();
			}

			// 非汇总表所有类型数据
			String strCond = "";
			if (qryData == null || qryData.getVer() < 0)
				strCond += " not(ver between 300 and 999) and not(ver >3999) ";
			else if (qryData.getVer() == 0)
				strCond += "ver<300 ";
			else if (qryData.getVer() >= 1 && qryData.getVer() <= 10) {// 舍位版本，需要调整
				strCond += "ver=" + (999 + qryData.getVer()) + " ";
			} else {
				strCond += "ver=" + qryData.getVer() + " ";
			}

			if (qryData != null) {
				if (strCond.length() > 0)
					strCond += " and ";
				strCond += " ktype='" + qryData.getKType() + "' ";
				if (qryData.getInputDate() != null
						&& qryData.getInputDate().length() > 0) {
					if (strCond.length() > 0)
						strCond += " and ";
					strCond += "inputdate>='" + qryData.getInputDate() + "'";
				}

				if (qryData.getEndDate() != null
						&& qryData.getEndDate().length() > 0) {
					if (strCond.length() > 0)
						strCond += " and ";
					strCond += "inputdate<='" + qryData.getEndDate() + "'";
				}

				String timeCondition = processKeywordCondition1(qryData);
				if (timeCondition != null && timeCondition.length() > 0) {
					if (strCond.length() > 0)
						strCond += "and " + timeCondition;
				}
				if (strCond.length() > 0)
					strCond += " and ";
				if (qryData.getFormulaID() != null
						&& qryData.getFormulaID().trim().length() > 0)
					strCond += " formula_id='" + qryData.getFormulaID() + "'";
				else
					strCond += " formula_id is null";
			}

			if (strCond.length() > 0) {
				if (!shouldAnd)
					strSQL += " where " + strCond;
				else
					strSQL += " and " + strCond;
			}

			strCond = strSQL;

			strSQL = "select t1.repid,t1.aloneid from "
					+ DatabaseNames.IUFO_CHECK_RESULT + " t1,";
			strSQL += "(" + strCond + ") t2 where t1.aloneid=t2.alone_id and t1.repid in(";

			for (int i = 0; i < strRepIDs.length; i++) {
				strSQL += "'" + strRepIDs[i] + "'";
				if (i < strRepIDs.length - 1)
					strSQL += ",";
			}
			strSQL += ") ";
			stmt = con.createStatement();
			set = stmt.executeQuery(strSQL);

			while (set != null && set.next()) {
				String strRepID = set.getString(1);
				String strAloneID = set.getString(2);

				List<String> vAloneID = (List<String>) hashRep.get(strRepID);
				if (vAloneID == null) {
					vAloneID = new Vector<String>();
					hashRep.put(strRepID, vAloneID);
				}
				vAloneID.add(strAloneID);
			}
			return hashRep;
		} finally {
			if (set != null){
				try {
					set.close();
				} catch (SQLException e) {
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 汇总时，为了加快汇总速度，将汇总条件应该查找出的iufo_measure_pubdata表中的记录查找出来，
	 * 生成一张同iufo_measure_pubdata表结构完全相同的临时表，将查找出来的记录插入临时表中
	 * @param strCond
	 * @return
	 * @throws SQLException
	 */
	public String createTempPubDataTableFromCond(String strCond)
			throws SQLException {
		Connection con = null;
		Statement stmt = null;
		try {
			con = getConnection();
			
			//临时表名随机生成
			String strTable = "iufo_tmp" + IDMaker.makeID(8);

			//oracle数据库其字符型、数值型对应的类型与其他数据库不相同
			String strChar = null;
			String strNumber = null;
			if (GlobalValue.DATABASE_TYPE.equals(IDatabaseType.DATABASE_ORACLE)) {
				strChar = "varchar2";
				strNumber = "number";
			} else {
				strChar = "varchar";
				strNumber = "float";
			}

			StringBuffer bufSQL = new StringBuffer();
			bufSQL.append("create table ").append(strTable).append("(");
			bufSQL.append("alone_id ").append(strChar).append("(20),");
			bufSQL.append("code ").append(strChar).append("(64),");
			bufSQL.append("ktype ").append(strChar).append("(20),");
			bufSQL.append("inputdate ").append(strChar).append("(20),");
			bufSQL.append("formula_id ").append(strChar).append("(20),");
			bufSQL.append("time_code ").append(strChar).append("(20),");
			bufSQL.append("ver ").append(strNumber).append(",");
			bufSQL.append("keyword1 ").append(strChar).append("(64),");
			bufSQL.append("keyword2 ").append(strChar).append("(64),");
			bufSQL.append("keyword3 ").append(strChar).append("(64),");
			bufSQL.append("keyword4 ").append(strChar).append("(64),");
			bufSQL.append("keyword5 ").append(strChar).append("(64),");
			bufSQL.append("keyword6 ").append(strChar).append("(64),");
			bufSQL.append("keyword7 ").append(strChar).append("(64),");
			bufSQL.append("keyword8 ").append(strChar).append("(64),");
			bufSQL.append("keyword9 ").append(strChar).append("(64),");
			bufSQL.append("keyword10 ").append(strChar).append("(64))");
			stmt = con.createStatement();
			stmt.executeUpdate(bufSQL.toString());
			stmt.close();
			stmt = null;

			StringBuffer bufCol = new StringBuffer();
			bufCol.append("alone_id,code,ktype,inputdate,formula_id,time_code,ver,keyword1,keyword2,keyword3,keyword4,keyword5,");
			bufCol.append("keyword6,keyword7,keyword8,keyword9,keyword10");

			bufSQL = new StringBuffer();
			bufSQL.append("insert into ").append(strTable).append("(").append(
					bufCol.toString()).append(") ");
			bufSQL.append("select ").append(bufCol.toString()).append(" from ")
					.append(IDatabaseNames.IUFO_MEASURE_PUBDATA);
			bufSQL.append(" where alone_id in (").append(strCond).append(")");
			stmt = con.createStatement();
			stmt.executeUpdate(bufSQL.toString());

			return strTable;
		} finally {
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * 删除临时表
	 * @param strDBTable
	 * @throws SQLException
	 */
	public void dropTempPubDataTable(String strDBTable) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		try {
			con = getConnection();
			String strSQL = "drop table " + strDBTable;
			stmt = con.createStatement();
			stmt.executeUpdate(strSQL);
		} finally {
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}
	}
}