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
	 * ���ɺ���˽�йؼ��ֵĶ�̬���Ļ�������
	 * @param strTable,ָ�����ݱ�
	 * @param strCond������������SQL���
	 * @param vMeasure��������ܣ���Ӧ��ָ�����ݱ��ָ��
	 * @param pubData,�����MeasurePubDataVO
	 * @param strMeasKeyGroupPK��ָ���Ӧ�Ĺؼ������PK
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
			
			//�ж϶�̬���Ƿ�����кŹؼ���
			boolean bOnlyLineKey = false;
			KeyGroupCache kgCache = IUFOBSCacheManager.getSingleton().getKeyGroupCache();
			KeyVO[] keys = kgCache.getByPK(strMeasKeyGroupPK).getKeys();
			KeyVO[] mainKeys = pubData.getKeyGroup().getKeys();
			if (keys != null && mainKeys != null && keys.length == mainKeys.length + 1) {
				for (int i = 0; i < keys.length; i++) {
					if (keys[i].isPrivate() && keys[i].getName().equals("�к�")) {
						bOnlyLineKey = true;
						break;
					}
				}
			}			
			
			// ��Ϊline_no�����Զ����ɣ����Բ���ֱ����insert into select���ʵ�֣���Ҫ�������ȡ������
			// ����insert������
			// ƴinsert���
			StringBuffer insertBufSQL = new StringBuffer();
			insertBufSQL.append("insert into ");
			insertBufSQL.append(strTable);
			insertBufSQL.append("(line_no,alone_id,pk_key_comb,");
			
			// ƴselect���
			StringBuffer selBufSQL = new StringBuffer();
			selBufSQL.append("select ");
			selBufSQL.append("'" + pubData.getAloneID() + "',");
			selBufSQL.append("pk_key_comb,");
			
			String strParam = "?,?,?,";
			int iCount = 2;
			int iMeasStartPos=3;
			
			// ƴ˽�йؼ�����
			for (int i = 0; i < 10; i++,iCount++) {
				insertBufSQL.append("keyvalue" + (i + 1) + ",");
				strParam += "?,";
				
				if (bOnlyLineKey == false){
					selBufSQL.append("keyvalue" + (i + 1) + ",");
					iMeasStartPos++;
				}
			}

			// ƴָ����
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

			// ��������кŹؼ��֣�˽�йؼ����ж����ÿ��ǣ�������10
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

			// ��˽�йؼ��ֽ��з���
			if (bOnlyLineKey == false) {
				selBufSQL.append(" group by pk_key_comb ");
				for (int i = 0; i < 10; i++)
					selBufSQL.append(",keyvalue" + (i + 1));
			}

			// ��¼�²��ҳ����Ľ��
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
						//�ؼ���
						if (bOnlyLineKey == false && i <= 12)
							vOneValue.add(rs.getString(i));
						//��ֵ��ָ����ַ���ָ���ֵ
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

			// �������ʱ����ʹֻ���кŹؼ��֣�Ҳ��Ҫ����˽�йؼ���
			if (bOnlyLineKey)
				iCount += 10;

			// ִ�в������
			stmt2 = con.prepareStatement(insertBufSQL.toString());
			int iBatchCount = 0;
			for (int i = 0; i < vValue.size(); i++) {
				// �����к�
				stmt2.setInt(1, i + 1);

				Vector vOneValue = (Vector) vValue.elementAt(i);
				for (int j = 1; j <= iCount; j++) {
					if (j <= 2)// ����alone_id
						stmt2.setString(j + 1, (String) vOneValue.elementAt(j - 1));
					else {
						// ���ֻ���кŹؼ��֣�˽�йؼ��ֵ�ֵ������
						int iIndex = j - 1;
						if (bOnlyLineKey)
							iIndex = j - 1 - 10;

						if (j <= 12) {
							// ���ֻ���кŹؼ��֣�����keyword1��˽�йؼ��ֵ�ֵ�����裬keyword1��ֵ��line_no��ͬ
							if (bOnlyLineKey) {
								if (j == 3)
									stmt2.setString(j + 1, "" + (i + 1));
								else
									stmt2.setString(j + 1, null);
							} else
								// ����˽�йؼ��ֵ�ֵ
								stmt2.setString(j + 1, (String) vOneValue.elementAt(iIndex));
						} else{
							//��ֵ��ָ����ַ���ָ���ֵ
							MeasureVO measure=vMeasure.get(j-iMeasStartPos);
							if (measure.getType()==MeasureVO.TYPE_NUMBER)
								stmt2.setDouble(j + 1, ((Double) vOneValue.elementAt(iIndex)).doubleValue());
							else
								stmt2.setString(j + 1, (String) vOneValue.elementAt(iIndex));
						}
					}
				}
				stmt2.addBatch();

				// һ��������100����¼
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
	 * ���ڲ����ڲμӻ��ܵı������е�ָ�꣬���л����¼�
	 * @param strTable��ָ�����ݱ�
	 * @param strCond������ɸѡ����SQL���
	 * @param vMeasure���μӻ��ܵ�ָ��
	 * @param mainPubData�������MeasurePubDataVO
	 * @param vSubPubData,��Ҫ���ɵ��ӱ��MeasurePubDataVO����
	 * @throws SQLException
	 */
	public void createTotalNoDynDatasWithHZSub(String strTable, String strCond, List<MeasureVO> vMeasure,
			MeasurePubDataVO mainPubData, List<MeasurePubDataVO> vSubPubData) throws SQLException {
		// ��MeasurePubDataVO�������Ҫ��insert����update������ɵ�
		Vector<Vector<MeasurePubDataVO>> vRetPubData = splitPubDataByExistRecord(strTable, vSubPubData);

		// ����update�������
		if (vRetPubData.get(0) != null && vRetPubData.get(0).size() > 0)
			createTotalNoDynDatasByUpdate(strTable, strCond, vMeasure, mainPubData, vRetPubData.get(0));

		// ����insert�������
		if (vRetPubData.get(1) != null && vRetPubData.get(1).size() > 0)
			createTotalNoDynDatasByInsert(strTable, strCond, vMeasure, mainPubData, vRetPubData.get(1));
	}
	
	/**
	 * ���ݿ����Ѿ����ڸ��м�¼����Ҫ��update���������ֵ
	 * @param strTable��ָ�����ݱ�
	 * @param strCond������ɸѡ����SQL���
	 * @param vMeasure���μӻ��ܵ�ָ��
	 * @param mainPubData�������MeasurePubDataVO
	 * @param vSubPubData����Ҫ���ɵ��ӱ��MeasurePubDataVO����
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
			
			// ������select�����ҳ���¼������update���������
			MeasurePubDataVO subPubData = (MeasurePubDataVO) vSubPubData.get(0);
			boolean[] bTimeType = getKeywordTimeType(mainPubData, subPubData);
			boolean bDayTime = bTimeType[2];

			// ����update���SQL��䣬sum�е�SQL���
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

			// ��¼���ҳ��Ľ��
			Vector<Vector<Object>> vData = new Vector<Vector<Object>>();
			Vector<List<MeasurePubDataVO>> vSplitSubPubData = splitObject(vSubPubData, (bDayTime == true ? 1 : 100));
			for (int i = 0; i < vSplitSubPubData.size(); i++) {
				vSubPubData =vSplitSubPubData.get(i);

				subPubData = (MeasurePubDataVO) vSubPubData.get(0);
				String strAloneID = subPubData.getAloneID();

				// ƴselect���
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
					// ��¼���ҳ���һ�еĽ��
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

			// ִ��update��������������ʵ��
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
	 * ʹ�ò����¼�ķ��������ɹ��йؼ��ֵĶ�̬���Ļ��ܼ�¼
	 * @param strTable
	 * @param strCond
	 * @param vMeasure
	 * @param mainPubData
	 * @param vSubPubData
	 * @throws SQLException
	 */
	public void createTotalNoDynDatasByInsert(String strTable, String strCond, List<MeasureVO> vMeasure,
			MeasurePubDataVO mainPubData, List<MeasurePubDataVO> vSubPubData) throws SQLException {
		//�����¼�¼
		innerCreateTotalNoDynDatasByInsert(strTable, strCond, vMeasure, mainPubData, vSubPubData);
		
		//������ַ���ָ�꣬����ֵ��δ���ɣ���update������������
		boolean bNumberMeas=vMeasure.get(0).getType()==MeasureVO.TYPE_NUMBER;
		if (!bNumberMeas){
			createTotalNoDynDatasByUpdate(strTable, strCond, vMeasure, mainPubData, vSubPubData);
		}
	}

	/**
	 * ���ݿ��в����ڸ��м�¼����insert����������¼�¼
	 * @param strTable��ָ�����ݱ�
	 * @param strCond������ɸѡ����SQL���
	 * @param vMeasure���μӻ��ܵ�ָ��
	 * @param mainPubData�������MeasurePubDataVO
	 * @param vSubPubData����Ҫ���ɵ��ӱ��MeasurePubDataVO����
	 * @throws SQLException
	 */
	private void innerCreateTotalNoDynDatasByInsert(String strTable, String strCond, List<MeasureVO> vMeasure,
			MeasurePubDataVO mainPubData, List<MeasurePubDataVO> vSubPubData) throws SQLException {
		Connection con =null;
		Statement stmt = null;
		try {
			con=getConnection();
			stmt = con.createStatement();
			
			//�ַ���ָ������ֵ
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

				// ƴ��insert into select���
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
	 * ɾ��һ��aloneid��Ӧ��һ��ָ�������
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
	 * ɾ��һ�ű������ݡ� ���ڱ����Լ������ݱ�ֱ��ɾ������aloneId��Ӧ�����ݱ��¼�� �������������������ָ���������ݱ�����ָ��ֵ��ա�
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
	 * �ϲ��������
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
	 * �༭һ�ű�������
	 * @param sAloneID
	 * @param measureData
	 * @throws java.sql.SQLException
	 */
	public void editRepData(String sAloneID, MeasureDataVO[] measureData) throws java.sql.SQLException {
		Connection con =null;

		try {
			con=getConnection();

			// ָ�����ݱ��а��������ݿ��ļ���
			Vector<String> vTableList = getTable(measureData);

			for (int i = 0; i < vTableList.size(); i++) {
				String sTableName = (String) vTableList.elementAt(i);
				// ��ĳ�������а���measuredatavo�е�ָ�����ݵļ��ϡ�
				Vector<MeasureDataVO> vMeasureInTable = getMeasureInTable(sTableName, measureData);
				// �õ�ָ�����ݱ��е�����к�
				int iRowNum = getRowNum(vMeasureInTable);

				// �ǹ̶���
				if (iRowNum == 0) {
					// Vector vLineData = getLineDataInTable(0,
					// vMeasureInTable);
					// ���û�е����м�¼,���������Ϊ�˵�������ʱ��ֹû��
					// �����м�¼���ȳ�ʼ���ģ���Ϊ�ڵ�������ʱ���õ�getaloneId����
					// ������¼��ʱ���õĵõ�aloneId�ķ�����ͬ��
					if (!isMeaDataExist(con,sTableName, sAloneID, 0)) {
						addMeasureData(con, sTableName, vMeasureInTable);
					}
					updateMeasureData(con, sTableName, sAloneID, 0, vMeasureInTable);
				} else {
					// �������Ϊ���ǿɱ��
					removeMeasureData(con,sTableName, new String[] { sAloneID });
					// ˵���ڿɱ������������
					addMeasureData(con, sTableName, vMeasureInTable);
				}
			}
		} finally {
			if (con != null)
				con.close();
		}
	}	
	
	/**
	 * ���ָ��ָ�ꡢָ��aloneId��ֵ�� ע�⣺��Щֵ����Ϊָ�������ʱ��Ĭ��ֵ
	 * @param aloneIds
	 * @param measures
	 * @param commitFlags
	 * @throws java.sql.SQLException
	 */
	public void emptyRepData(String[] aloneIds, MeasureVO[] measures, int[] commitFlags) throws java.sql.SQLException {
		Connection con=null;
		try{
			con=getConnection();

			// ������Чʱ��ֱ�ӷ���
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
	 * ����ĳһָ���Ӧ���aloneid��ֵ
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
	 * ���ָ��ָ�����������
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
	 * ��ö�Ӧһ��aloneid��һ��ָ�������
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
	 * ����Ӧ�����ݿ���еõ���Ӧ�е�����
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
	 * �õ�ָ���漰����ָ�����ݱ�
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
	 * ����Ӧ�����ݿ��������һ��ָ��Ϊ�յļ�¼
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
	 * ����ʱ������һ��ָ�����ݱ��õ���Ҫ���ܵķ��������ļ�¼����
	 * @param mainPubData������MeasurePubDataVO
	 * @param subPubData,�ӱ�MeasurePubDataVO
	 * @param strCond����������ɸѡ���ʽ
	 * @param strTable��ָ�����ݱ�
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
	 * һ�α���һ��ָ�����ݡ� ע��ָ������VO����Ҫ���ú�aloneId,˽�йؼ��ֵ�ֵ���кš�
	 * @param datas
	 * @throws java.sql.SQLException
	 */
	public void saveMeasureDatas(MeasureDataVO[] datas) throws java.sql.SQLException {
		if (datas == null || datas.length == 0) {
			return;
		}
		// ����ָ�����������ݱ����
		MeasureVO[] measures = new MeasureVO[datas.length];
		for (int i = 0; i < measures.length; i++) {
			measures[i] = datas[i].getMeasureVO();
		}
		
		Vector<String> tables = getTableFromMeasure(measures);

		Vector<Object[]> id_datas = new Vector<Object[]>();
		for (int i = 0; i < tables.size(); i++) {
			String tableName = (String) tables.elementAt(i);
			// ��һ�����ݱ��е�ָ�����ݰ�aloneId����
			Hashtable<String,Vector<MeasureDataVO>> ht = groupMeasureDatasByAloneId_LineNo(datas, tableName);
			Enumeration keys = ht.keys();
			// ���ֺ���������ٴηŵ��ܵķ����¼��
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
			
			// ��ʼ����ƴװSQL���
			Vector<String> sqls = new Vector<String>();
			for (int i = 0; i < id_datas.size(); i++) {
				Object[] iddata = (Object[]) id_datas.elementAt(i);
				String tableName = (String) iddata[0];
				String aloneId = (String) iddata[1];
				// ����к���aloneIdֵ
				int lineNo = Integer.parseInt(aloneId.substring(aloneId.indexOf("_") + 1));
				aloneId = aloneId.substring(0, aloneId.indexOf("_"));
				Vector mdatas = (Vector) iddata[2];
				MeasureDataVO[] meadatas = null;
				if (mdatas.size() > 0) {
					meadatas = new MeasureDataVO[mdatas.size()];
					mdatas.copyInto(meadatas);
					// ����aloneId��lineNO��һ�ű����Ƿ���ڣ����ɲ�ͬ��SQL���
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
	 * ������Դʱ���Թ��йؼ��ֶ�̬����Դ
	 * @param measures��Ҫ��Դ��ָ��
	 * @param strCondSQL������������SQL���
	 * @param keys��Ҫ���ҵĹؼ���ֵ�Ĺؼ�������
	 * @param hashKeyPos��Ҫ���ҵĹؼ�����ؼ�������еĹؼ��ֵ�λ�õĶ�Ӧ��ϵ
	 * @param mainPubData������MeasurePubDataVO
	 * @param subPubData,�ӱ�MeasurePubDataVO
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
			// ƴ��select���
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
	 * ������Դʱ����˽�йؼ��ֶ�̬����Դ
	 * @param measures��Ҫ��Դ��ָ��
	 * @param strCondSQL������������Ӧ��SQL���
	 * @param pubData������MeasurePubDataVO
	 * @param mainKeys������ؼ���
	 * @param sourKeys���ӱ�ؼ���
	 * @param destKeys����Ҫ�ڽ�������ʾ�Ĺؼ���
	 * @param hashKeyPos��Ҫ��ʾ�Ĺؼ�����iufo_measure_pubdata��iufo_measuredata��Ĺؼ��ֵĶ�Ӧ��ϵ
	 * @param strPrivKeyVals��Ҫ��Դ�������Ӧ�Ķ�̬���Ĺؼ��ֵ�ֵ
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
			
			//���ӱ�ؼ����е�����ؼ���ɾ����
			for (int i = 0; i < vPubKey.size(); i++) {
				int iIndex = vPrivKey.indexOf(vPubKey.get(i));
				if (iIndex >= 0)
					vPrivKey.remove(iIndex);
			}

			StringBuffer selBufSQL = new StringBuffer();
			selBufSQL.append("select t2.alone_id,t2.code,");

			//��ӹؼ����ֶ�
			for (int i = 0; i < destKeys.length; i++) {
				int iPos = ((Integer) hashKeyPos.get(new Integer(i))).intValue();
				//˽�йؼ����빫�йؼ��ִӲ�ͬ�ı�ȡ
				if (destKeys[i].isPrivate()) {
					iPos = vPrivKey.indexOf(sourKeys[iPos]);
					selBufSQL.append("t1.keyvalue" + (iPos + 1) + ",");
				} else {
					iPos = vPubKey.indexOf(sourKeys[iPos]);
					selBufSQL.append("t2.keyword" + (iPos + 1) + ",");
				}
			}
			
			//���ָ���ֶ�
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

			//����˽�йؼ�������
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
	 * �����ݱ�������ָ������
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
	 * ����Ӧ�����ݿ��������һ��ָ��Ϊ�յļ�¼
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
	 * ����ʱ�����ɼ��ű����������Ĺ�������
	 * @param strMainTable�������Ӧ��iufo_measure_pubdata��
	 * @param strSubTable����̬����Ӧ��iufo_measure_pubdata��
	 * @param mainPubData�������Ӧ��MeasurePubDataVO
	 * @param subPubData����̬����Ӧ��MeasurePubDataVO
	 * @param vSubPubData,
	 * @param bSubTime,�Ƿ������ӱ���ʱ��
	 * @param bWeekTime���ӱ��Ƿ�����ʱ��
	 * @param bDayTime���ӱ��Ƿ�����
	 * @param strAppendTable
	 * @return
	 */
	private String getTotalTimeJoinCond(String strMainTable, String strSubTable, MeasurePubDataVO mainPubData,
			MeasurePubDataVO subPubData, List<MeasurePubDataVO> vSubPubData, boolean bSubTime, boolean bWeekTime, boolean bDayTime,
			String strAppendTable) {
		StringBuffer selBufSQL = new StringBuffer();

		// ���ӱ�ͬ�ؼ��֣����ڸ��Թؼ�������е�λ�ÿ��ܲ���ͬ������������Ķ�Ӧ��ϵ
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

		// ��t2���t3��������������
		// ���ݹؼ��ֶ�Ӧ��ϵ������keyword����������
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

		// ����t2���t3��TimeCode����������,�ӱ�ʱ������Ϊ��ʱ��ֻ��ҪȡTimeCode������λ
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
	 * �ضϴ����SQL���Ĺ�������
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
	 * ����ʱ�����ɼ��ű����������Ĺ�������
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

	    //������ӱ�ͬʱ��ʱ��ؼ��֣�����Ҫ��iufo_measure_pubdata��������Σ��ڶ�������Ͳ�ѯ����
	    //�����Alone_id�������������KType�ı���һ������Select����������KTypeΪ�ӱ�KType,����
	    //��֮�����Keyword��TimeCode��������

	    //������ӱ���ͬʱ��ʱ��ؼ��֣���iufo_measure_pubdata��ֱ�ӺͲ�ѯ������ͨ��Alone_id��������

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
	 * ��һ����������ֳɼ���С������
	 * 
	 * @param vObj��ԭ����
	 * @param iSize��һ�����������С
	 * @return�����������
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
	 * ��MeasurePubDataVO���鰴�Ƿ���ָ�����ݱ����м�¼���ֳ�����
	 * @param strTable��ָ�����ݱ�
	 * @param vPubData��MeasurePubDataVO����
	 * @return�������������飬һ������ָ�����ݱ����м�¼��MeasurePubDataVO���飬һ����û�м�¼��MeasurePubDataVO����
	 * @throws SQLException
	 */
	private Vector<Vector<MeasurePubDataVO>> splitPubDataByExistRecord(String strTable, List<MeasurePubDataVO> vPubData) throws SQLException {
		Connection con=null;
		Statement stmt = null;
		ResultSet set = null;
		try {
			con = getConnection();
			
			Vector<String> vExistAloneID = new Vector<String>();

			// ��MeasurePubDataVO�����ɶ��С���飬��ֹin����¼����
			Vector<List<MeasurePubDataVO>> vSplitPubData = splitObject(vPubData, 100);

			// ���ҳ������м�¼��aloneid
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

			// ��MeasurePubDataVO���з���
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
	 * ���ָ��ָ�ꡢָ��aloneId��ֵ�� ע�⣺��Щֵ����Ϊָ�������ʱ��Ĭ��ֵ
	 * @param con
	 * @param aloneIds
	 * @param measures
	 * @throws java.sql.SQLException
	 */
	private void emptyRepData(Connection con,String[] aloneIds, MeasureVO[] measures) throws java.sql.SQLException {
		// ������Чʱ��ֱ�ӷ���
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
	 * �������ָ��������Ҫ��SQL���
	 * @param aloneIds
	 * @param measures
	 * @return
	 */
	private String[] geneEmptyRepSqls(String[] aloneIds, MeasureVO[] measures) {
		// ȡ��ָ���������ݿ��
		Vector tables = getTableFromMeasure(measures);
		if (tables == null || tables.size() == 0) {
			return null;
		}
		// ����AloneID�õ�in �����б�
		StringBuffer[] allAloneIds = getINClauseByAloneIDs(aloneIds, measures.length);
		// �������и��±������ݵ�SQL���
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
	 * Ϊһ�����ݿ���������ʹ�õ��ֶμ�
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
		// �������forѭ���У����ܻ����ַ���β����һ�����ţ�ȥ��֮
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * ����ʱ������iufo_measure_pubdata�����pubdata�еĹؼ���ֵ����������ϵ
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
	 * ����ָ�ꡢ���ݱ�aloneid����ָ������
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
	 * �ҳ���ĳһָ�����ݱ��е�ָ��
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
	 * �ҳ���ĳһָ�����ݱ��е�ָ��
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
	 * ����ָ�귵����Ҫ��ָ�����ʱ��Ĭ��ȡֵ
	 * @param measure
	 * @return
	 */
	private String getMeasureZeroData(MeasureVO measure) {
		if (measure == null) {
			return "''";
		}
		switch (measure.getType()) {
		// ���ַ���ָ�꣬���س���Ϊ0���ַ���
		case MeasureVO.TYPE_CHAR:
		case MeasureVO.TYPE_CODE:
		case MeasureVO.TYPE_NORMAL:
			return "''";
		// ����ֵ��ָ�꣬����0
		case MeasureVO.TYPE_NUMBER:
			return "0";
		default:
			return "''";
		}
	}

	/**
	 * �ҳ�һ��ָ�������������к�
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
	 * �õ�ָ���Ӧ��ָ�����ݱ�����
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
	 * ��һ�����ݱ��и���aloneID���кŷ���ָ������
	 * @param datas
	 * @param tableName
	 * @return
	 */
	private Hashtable<String,Vector<MeasureDataVO>> groupMeasureDatasByAloneId_LineNo(MeasureDataVO[] datas, String tableName) {
		// �ȸ���ָ�����ݵ��кŲ��ָ������
		Hashtable<String,Vector<MeasureDataVO>> htNos = new Hashtable<String,Vector<MeasureDataVO>>();
		for (int i = 0; i < datas.length; i++) {
			// ��aloneId_lineNo����ʽ��Ϊhashtable�ļ�ֵ
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
	 * ��ָ�����ݰ��кŷ���
	 * @param datas
	 * @return
	 */
	private Hashtable groupMeasureDatasByLine(MeasureDataVO[] datas) {
		// �ȸ���ָ�����ݵ��кŲ��ָ������
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
	 * ����Ӧ�����ݿ��������һ��ָ��Ϊ�յļ�¼
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
	 * �ж�ָ����¼�Ƿ����
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
	 * ����SQL��䡣�������������һ��ָ�����ݡ����ָ������Ϊ�գ������һ��ֻ��aloneid���кŵĿ�����
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
	 * ����ָ�����ݵ�SQL���
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
	 * ���ɲ�������ָ�����ݵ�SQL���
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
		// �ȸ���ָ�����ݵ��кŲ��ָ������
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
	 * ����SQL��䡣�������������һ��ָ�����ݡ����ָ������Ϊ�գ��򽫳��кż�aloneid���������Ϊ�ջ�0
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
	 * ����Ӧ�����ݿ����ɾ��һ����¼
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
	 * ���ݱ�������ָ���������ѱ������ݱ��Ϊ���飬��һ���������ʽ���ء���һ ���Ǳ����Լ�ָ�����ڵ����ݱ��ϣ��ڶ����Ǳ�������ָ�����ڵ����ݱ��ϡ�
	 * ����ĵ�������һ����ϣ�����б���ÿ�����õı����Ӧ��ָ�꼯��
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
	 * ����Ӧ�����ݿ�����޸�һ����¼
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
		// �õ�����SQL��������ĳ���,
		int nMaxNumber = 500;
		int n = aloneIds.length / nMaxNumber;
		if (aloneIds.length % nMaxNumber > 0) {
			n++;
		}
		// �����е�AloneId���ӳ�һ��������һ���֣��� id1,id2,id3,...
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
	 * �õ����ӱ���ʱ�����͹�ϵ
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
