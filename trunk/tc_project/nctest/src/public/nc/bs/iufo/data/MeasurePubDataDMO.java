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
	 * ��iufo_measure_pubdata���в���һ����¼
	 * @param measurePubData
	 * @return
	 * @throws SQLException
	 */
	public String createMeasurePubData(MeasurePubDataVO measurePubData)
			throws SQLException {
		return createMeasurePubDatas(new MeasurePubDataVO[]{measurePubData})[0].getAloneID();
	}

	/**
	 * ��iufo_measure_pubdata���в���һ���¼
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
				//liuyy. ����жϣ������е�λ�ؼ��ֵ� iufo_measure_pubdata��code�ֶ�Ϊ�յ����⡣
				if(measurePubData.isContainUnitKey() && Toolkit.isNullStr(measurePubData.getUnitPK())){
					throw new RuntimeException("MeasurePubData��λ�ؼ���ֵΪ�գ�");
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
	 * ���ݽ��������MeasurePubDataVO
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
	 * ����aloneid����iufo_measure_pubdata���¼
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
	 * ����һ��aloneid��Ӧ��iufo_measure_pubdata���еļ�¼
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
	 * ���ݲ�ѯ������MeasurePubDataVO�����ҳ�������Ӧ������Ķ�̬���е�MeasurePubDataVO
	 * @param pubData,��ѯ������MeasurePubDataVO
	 * @param strUnitCode,��ǰ��λ
	 * @param keyCombPKs����Ҫ���ҵĶ�̬���Ĺؼ������PK
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
	 * ���������MeasurePubDataVO�����ҳ�������Ӧ������Ķ�̬���е�MeasurePubDataVO
	 * @param pubData������MeasurePubDataVO
	 * @param strUnitCode,��ǰ��λ
	 * @param includeSubUnit���Ƿ�����¼�
	 * @param keyCombPKs���ؼ������PK
	 * @param strOrgPK,��ǰ��֯PK
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
	 * �������õ�MeasurePubDataVO���õ�aloneid��ֵ
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
	 * ����ĳһ���ܽ����Ӧ������MeasurePubDataVO,������λ�汾��
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
	 * ƴ�����¼���λ��¼��SQL���
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
	 * ���һ��PubData���Ƿ��е�λ�ؼ���
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
	 * ���ݲ�ѯ�������ɲ�ѯMeasurePubData��¼��SQL���ķ���
	 * @param pubData����ѯ����VO
	 * @param strUnitCode,��ǰ��λPK
	 * @param includeSubUnit���Ƿ�����¼���λ
	 * @param ver���������ݰ汾
	 * @param keyCombPKs��Ҫ���ҵĹؼ������PK
	 * @param strOrgPK����ǰ��֯
	 * @return
	 * @throws java.sql.SQLException
	 */
	private String getFindSQLByCondition(MeasurePubDataVO pubData, String strUnitPK,
			boolean includeSubUnit, int ver, String[] keyCombPKs,
			String strOrgPK,boolean bIgnoreFormulaID){
		String sql = getSelectSQL();
		boolean sqlWithSub = false;
		
		//�����λ��Ϊ�գ��Ұ����¼�����ƴ���ҷ�Χ�����¼���SQL���
		if (includeSubUnit && strUnitPK != null && strUnitPK.trim().length()>0) {
			sql = getSelectSQLWithSubs(strUnitPK, GlobalValue.DATABASE_TYPE,strOrgPK);
			sqlWithSub = true;
		}

		//����where����
		String strCond = "";
		
		//����е�λ�ؼ��֣��ҵ�ǰ��λ��Ϊ�գ��Ҳ������¼���λ�������õ�λ����
		boolean hasUnitKey = hasUnitKey(pubData);
		if (!includeSubUnit && hasUnitKey && strUnitPK != null&& strUnitPK.length() > 0
				&& (pubData != null && pubData.getUnitPK() == null))
			strCond += "code='" + strUnitPK + "' ";

		//�������ݰ汾����
		if (ver >= 0) {
			if (strCond.length() > 0) {
				strCond += " and ";
			}
			// �ǻ��ܱ�������������
			if (pubData == null || pubData.getVer() < 0)
				strCond += " not(ver between 300 and 999) and not(ver >3999)";
			else if (pubData.getVer() == 0)
				strCond += "ver<300 ";
			else
				strCond += "ver=" + (pubData.getVer());
		}
		
		if (pubData != null) {
			//���ùؼ����������
			if (pubData.getKType() != null && pubData.getKType().length() > 0) {
				if (strCond.length() > 0)
					strCond += " and ";
				strCond += " ktype='" + pubData.getKType() + "'";
			}

			//����formulaid����
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

			//���������ؼ���ֵ����
			String keyCond = processKeywordCondition(pubData, keyCombPKs);
			if (strCond.length() > 0 && keyCond.length() > 0) {
				strCond += " and ";
				strCond += keyCond;
			}
		}

		//�����Ӧ����ؼ�����ϣ����ùؼ����������
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
	 * ���ݲ�ѯ������ѯMeasurePubData��¼�Ĺ�������
	 * @param con��
	 * @param pubData����ѯ����VO
	 * @param strUnitCode,��ǰ��λPK
	 * @param includeSubUnit���Ƿ�����¼���λ
	 * @param ver���������ݰ汾
	 * @param keyCombPKs��Ҫ���ҵĹؼ������PK
	 * @param strOrgPK����ǰ��֯
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
	 * ����ʱ�����ݻ�������SQL��䡢ָ�����ݱ����ܽ������MeasurePubDataVO������ؼ�����ϡ��ӱ�ؼ������,�õ���Ҫ���ɻ��ܽ����MeasurePubDataVO����
	 * @param strCond����������SQL���
	 * @param pubData�����ܽ�������MeasurePubDataVO
	 * @param mainKeyGroup������ؼ������
	 * @param subKeyGroup���ӱ�ؼ������
	 * @param strDBTable��ָ�����ݱ�
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
			
			//�ж��Ƿ����ӱ���ʱ��ؼ��֣��ӱ�ؼ����Ƿ����ܡ���
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

			//���ӱ���ͬ�ؼ��֣����ڸ��Թؼ�������е�λ�ÿ��ܲ���ͬ������������Ķ�Ӧ��ϵ
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

			//���ҵ��ֶε����
			StringBuffer bufItem = new StringBuffer();
			
			//����Ҫ��ȡ�������õ���
			for (int i = 0; i < subKeys.length; i++) {
				// �ж��Ƿ��ӱ��еĹؼ��������д��ڣ�������ڣ�������MeasurePubDataVO�Ѿ����ڸùؼ���ֵ�����ô����ݿ��ж�ȡ
				int iMainPos = -1;
				if (hashPos.get(new Integer(i)) != null)
					iMainPos = ((Integer) hashPos.get(new Integer(i))).intValue();

				if (iMainPos >= 0&& pubData.getKeywordByIndex(iMainPos + 1) != null)
					continue;

				if (bSubTime == false || subKeys[i].getTimeKeyIndex() < 0)
					bufItem.append("t1.keyword" + (i + 1) + ",");
				else {
					// �������ӱ�ͬʱ��ʱ��ؼ��ֵ����,��Ҫ�����ӱ�ʱ��ؼ����Ƿ�Ϊ��,���TimeCode�Ľضϴ�����ͬ
					// ��Ϊͬһ�ܣ���ͬ�꣬��ǰ����TimeCode��ֵ���ܲ���ͬ

					// ȷ��TimeCodeҪ�ضϵĲ���
					int iIndex, iLen;
					if (!bDayTime) {
						if (bWeekTime) {
							// �������ͣ�ֻ��Ҫȡ�ܲ��ֵ�TimeCode����λ
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
			
			//ȥ�������ֶ��������һ������
			bufItem.delete(bufItem.length() - 1, bufItem.length());

			//���ҵ�SQL���
			StringBuffer bufSQL = new StringBuffer();
			bufSQL.append("select ");

			//�������һ��ָ�����ݱ����ҳ����ļ�¼�������ظ��ģ�ʹ��distinct
			if (strDBTable != null)
				bufSQL.append(" distinct ");

			bufSQL.append(bufItem);
			bufSQL.append(" from ");
			bufSQL.append(DatabaseNames.IUFO_MEASURE_PUBDATA + " t1,");

			//������ӱ�ͬʱ��ʱ��ؼ��֣�����Ҫ��iufo_measure_pubdata��������Σ��ڶ�������Ͳ�ѯ����
			//�����Alone_id�������������KType�ı���һ������Select����������KTypeΪ�ӱ�KType,����
			//��֮�����Keyword��TimeCode��������

			//������ӱ���ͬʱ��ʱ��ؼ��֣���iufo_measure_pubdata��ֱ�ӺͲ�ѯ������ͨ��Alone_id��������
			bufSQL.append(strCond + " t2");

			if (strDBTable != null) {
				bufSQL.append("," + strDBTable + " t4");
			}

			bufSQL.append(" where t1.ver=t2.ver and t2.ktype='"+ mainKeyGroup.getKeyGroupPK() + "'");
			bufSQL.append(" and t1.ktype='" + subKeyGroup.getKeyGroupPK()+ "' ");

			//�������ӱ������λ�ò���ͬ��Ҫ����λ�ü�Ķ�Ӧ��ϵ������������
			Enumeration<Integer> enumPos = hashPos.keys();
			while (enumPos.hasMoreElements()) {
				Integer iSubKeyPos = (Integer) enumPos.nextElement();
				Integer iMainKeyPos = (Integer) hashPos.get(iSubKeyPos);
				bufSQL.append(" and t1.keyword" + (iSubKeyPos.intValue() + 1)+ "=t2.keyword" + (iMainKeyPos.intValue() + 1));
			}

			if (bSubTime) {
				// ��������MeasurePubData��֮���TimeCode��������
				int iIndex = 2 * (mainKeyGroup.getTimeKey().getTimeKeyIndex() + 1) + 2;
				if (GlobalValue.DATABASE_TYPE.equals(IDatabaseType.DATABASE_SQLSERVER)) {
					bufSQL.append(" and substring(t1.time_code,1," + iIndex
							+ ")=substring(t2.time_code,1," + iIndex + ")");
				} else {
					bufSQL.append(" and substr(t1.time_code,1," + iIndex
							+ ")=substr(t2.time_code,1," + iIndex + ")");
				}
			}
			
			//�������ָ�����ݱ���Ҫ��ָ�����ݱ���ָ�깫�����ݱ����
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

				//���û��ָ�����ݱ���ʾֻ����Ҫ�����Ƿ���Ҫ���ɻ��ܽ����MeasurePubDataVO��¼��ֱ�ӷ���һ����¼����
				if (strDBTable == null)
					return new MeasurePubDataVO[] { onePubData };

				int iValidPos = 1;
				for (int i = 0; i < subKeys.length; i++) {
					int iMainPos = -1;
					if (hashPos.get(new Integer(i)) != null)
						iMainPos = ((Integer) hashPos.get(new Integer(i))).intValue();

					// ���������еĹؼ��֣�����MeasurPubdataVO�Ѿ��д˹ؼ���ֵ�ģ�ֱ�Ӵ���MeasurePubDataVO��ȡ
					if (iMainPos >= 0 && pubData.getKeywordByIndex(iMainPos + 1) != null) {
						onePubData.setKeywordByIndex(i + 1, pubData.getKeywordByIndex(iMainPos + 1));
					} else {
						String strKeyVal = set.getString(iValidPos++);
						if (strKeyVal != null
								&& (strKeyVal.trim().length() <= 0 || strKeyVal.trim().equalsIgnoreCase("null") == true))
							strKeyVal = null;

						// ���ӱ�ʱ��ؼ��֣�ֱ����ֵ
						if (bSubTime == false || subKeys[i].getTimeKeyIndex() < 0)
							onePubData.setKeywordByIndex(i + 1, strKeyVal);
						else {
							// ��Ҫ������ʱ�����ͣ������MeasurePubdataVO��TimeCode��ȡ��ֵ����ͬ
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
	 * ���ҹؼ�����϶�Ӧ��MeasurePubDataVO
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
	 * �˴����뷽�������� �������ڣ�(2003-8-19 16:14:52)
	 * 
	 * @return java.lang.String
	 */
	private String makeInsertSql() {
		return INSERT_SQL;
	}

	/**
	 * ����timecode�Ĳ�ѯ����
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
		// �õ�����������Ĺؼ��������ʱ�����Ե�������Сֵ(��>����>��>��>Ѯ>��>��)
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
			// ����ʱ������ȡTIMECODE���Ӵ�
			/*
			 * 0:�� --4 1:����--6 2:�� --8 3:�� --10 4:Ѯ --12 5:�� --14 6:�� --16
			 * ��Ϊ�ձ�ʱ,����Ҫʹ��time_code like "xxx%"����ʽ,���Է���null
			 */
			int pos = (min + 1) * 2 + 2;
			if (pos == tc.length()) {
				return null;
			}
			if (max == 5) {
				// �ӱ�ؼ��ֺ����ܱ�����Ҫ�����⴦��
				return makeWeekCondition(min, pubData.getInputDate());
			}
			tc = timeCode.substring(0, pos);
		}
		return tc;
	}

	/**
	 * ��Ӧ�ܹؼ��֣�ֱ������inputdate����
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
	 * ���ɹؼ���ֵ������
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
					// Ѯʱ�����⴦��
					String tCode = UFODate.getTimeCode(pubData.getInputDate());
					// ֻȡѮ��ʱ�����
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
						// Ѯʱ���ٴ���
					} else {
						if (sb.length() > 0)
							sb.append(" and ");
						// liuyy 2005-5-14 �����������޸�
						key = this.convertSQL(key);
						sb.append("keyword" + i + "='" + key + "' ");
					}
				} else {
					if (sb.length() > 0)
						sb.append(" and ");
					if (timeCode.indexOf("inputdate") < 0) {
						// ��������ܱ����򷵻ص����ݲ��Ậ�������ַ���
						sb.append("time_code like '" + timeCode + "%' ");
					} else {
						// �ܱ�ʱֱ�ӷ�������
						sb.append(timeCode);
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * �༶������������������ʱ������¼����������Ҽ�¼ʱ�����ɵĹؼ��ֲ��ֵĲ�ѯ����
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
				//��λ��ʱ��ؼ����Ѿ�����
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
	 * ����aloneid��ɾ��iufo_measure_pubdata���еļ�¼
	 * @param sAloneIDs
	 * @throws SQLException
	 */
	public void removeMeasurePubDatas(String[] sAloneIDs) throws SQLException {
		if (sAloneIDs == null || sAloneIDs.length == 0)
			return;
		
		Arrays.sort(sAloneIDs);

		Connection con = null;

		//��aloneid���飬�õ�����ɾ�����
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
		stmt.setString(1, pubData.getAloneID()); // Ψһ����
		stmt.setString(2, pubData.getUnitPK()); // ��λ����
		stmt.setString(3, pubData.getKType()); // ����
		stmt.setInt(4, pubData.getVer()); // �汾
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
		stmt.setString(15, pubData.getInputDate()); // ����
		stmt.setString(16, pubData.getFormulaID()); // ���ܱ���
		stmt.setString(17, pubData.getTimeCode()); // ʱ�����
	}

	/**
	 * �ж��Ƿ��ж�Ӧĳһ�汾�����ݣ�����ɾ����λ����ʱ���ж��Ƿ�����λ����δɾ��
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
	 * �༶������������������ʱ������ѡ�е����������ҳ���Ӧÿ�ű��������ݵ�MeasurePubDataVO����
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

			// �ǻ��ܱ�������������
			String strCond = "";
			if (qryData == null || qryData.getVer() < 0)
				strCond += " not(ver between 300 and 999) and not(ver >3999) ";
			else if (qryData.getVer() == 0)
				strCond += "ver<300 ";
			else if (qryData.getVer() >= 1 && qryData.getVer() <= 10) {// ��λ�汾����Ҫ����
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
	 * ����ʱ��Ϊ�˼ӿ�����ٶȣ�����������Ӧ�ò��ҳ���iufo_measure_pubdata���еļ�¼���ҳ�����
	 * ����һ��ͬiufo_measure_pubdata��ṹ��ȫ��ͬ����ʱ�������ҳ����ļ�¼������ʱ����
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
			
			//��ʱ�����������
			String strTable = "iufo_tmp" + IDMaker.makeID(8);

			//oracle���ݿ����ַ��͡���ֵ�Ͷ�Ӧ���������������ݿⲻ��ͬ
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
	 * ɾ����ʱ��
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