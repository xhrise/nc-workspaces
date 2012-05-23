package nc.bs.iufo.measure;
import com.ufida.iufo.pub.tools.AppDebug;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.iufo.toolkit.DatabaseNames;
import nc.bs.pub.SystemException;
import nc.jdbc.framework.crossdb.CrossDBPreparedStatement;
import nc.jdbc.framework.crossdb.CrossDBResultSet;
import nc.pub.iufo.cache.base.CacheObjPackage;
import nc.pub.iufo.cache.base.RefreshedObjDesc;
import nc.vo.iufo.measure.MeasurePackVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.pub.DataManageObjectIufo;

/**
 * @update by ʷ�� at (2004-8-19 18:44:52) �޸ļ���ָ��ķ������ڷ�������ǰ�رմ򿪵����ݿ�����
 * @end
 * @update by ʷ�� at (2004-8-6 17:21:52) ��ɾ��ָ��ʱ����������ķ�ʽ
 * @end
 * @update by ʷ�� at (2004-8-4 16:34:52)
 *         �޸ı���ģ��ɾ��ʱ���ָ�����ݵķ�����������ģ���е�ָ��û�ж�Ӧ��ָ�����ݱ������Ҫ�ж��Ƿ�Ϊ�գ�
 * @end
 * @update by ʷ�� at (2004-7-30 16:01:52) �޸�ɾ��ָ��ʱ����ԭ��ָ�����ݵķ�����ʹ��������ķ�ʽ����ԭ����ѭ������ķ�ʽ
 * @end
 * @update 2004-4-28 8��46 ָ����Ϣ�������ӹؼ������pk�ֶ�
 * @end
 * @update 2003-11-10 20��53 �޸�ָ�����ݿ��Ĵ�������DB2���ݿ��£�����ֶβ�������500�У�float�͵��ֶ�Ϊ9���ֽ�
 * @end
 * @update 2003-11-10 yym �޸���������ָ�����ݿ���SQL����﷨����
 * @end
 * @update 2003-11-07 yym 1)���ӷ��������������ط�ʽ��ֻ���ض����ID�������뻺���ˢ�¼�¼��
 *         2)������ָ�����ݿ����ֶθ�Ϊ��������
 * @end
 * @update ���500�����ϵĲ�ѯʱ����count>=0��Ϊcount>0
 * @end
 * @update 2003-10-17 14:17������ �޸�ȥ��ֻ�����Ժ�������
 * @end
 * @update 2003-10-16 16:52������ ȥ��ֻ�������ֶΣ��޸��漰������ֻ�����Եķ���
 * @end
 * @udpate 2003-01-24 ������ Ϊ��ָֹ�������������޷������ݿ������޸Ĳ����������޸�, private void
 *         updateMeasure(MeasureVO[] vos, Connection con)
 * @end
 * @udpate 2003-01-22 ������ �޸�recycleTable(MeasureVO [],
 *         Connection)�У�ps.close()ʹ�ò�ǡ�����ɾ��ָ������ʱ�� ��SQLException.
 * @end
 * @udpate 2003-01-15 10:02 ����Ƽ 1���޸Ķ������read_only�ֶε�֧��
 *         �޸��ˣ�m_sSelectFrom,createMeasure(Object [], Connection)
 *         getInsertSql(MeasureVO),generateSysVO(ResultSet)
 *         updateMeasure(MeasureVO [], Connection) 2��ע�͵��˺�̨��װ��ָ�����ƵĴ�ӡ
 * @end ָ���������ݹ������
 * 
 * @version 2.0
 */

public class MeasureDMO extends DataManageObjectIufo {

    //�ֶ�����
    private static final String MEASURE_PACK_PK = "measurePackPK";

    private static final String REPORT_PK = "reportPK";

    private static final String KEY_GROUP_PK = "keyGroupPK";

    private static final String MEASURES = "measures";

    /**
     * ȱʡ�Ľṹ����
     */
    public MeasureDMO() throws NamingException, SystemException {

    }

    /**
     * �˴����뷽�������� �������ڣ�(2002-06-03 11:35:40)
     * 
     * @param param
     *            java.lang.String
     */
    public MeasureDMO(String param) throws NamingException, SystemException {
        super(param);
    }

    //    /**
    //     * ������������LEVELʱ���������ݲ���������
    //     */
    //    private final int LEVEL = 5;
    //
    //    /**
    //     * ѡ�����ı�׼����
    //     */
    //    private final String m_sSelectFrom = "select code,name,input_code,
    // attribute,type,len,formula,note,dbtable,dbcolumn,code_ref_id,exttype,marks,rep_id,pk_key_comb
    // from "
    //            + DatabaseNames.IUFO_MEASURE_TABLE;
    //
    //    private static MeasurePropVO[] MEASURE_PROPS = null;
    //
    //    /**
    //     * ���ָ�����չ���ԡ�****lyf020718 �÷��������ݿ�����ȡ��չ���Ե�ֵ��������Щ����ֵ��������ʵ�� �������ڣ�(2002-04-23
    //     * 10:03:40)
    //     *
    //     * @param vos
    //     * Object[] ָ������
    //     * @param connect
    //     * java.sql.Connection ����
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    private void addMeasureProp(Object[] vos, Connection connect)
    //            throws SQLException {
    //
    //        int[] nTypes = nc.ui.iufo.measure.IMeasSysInfo
    //                .getMEASURE_EXTNED_TYPES();
    //        if (nTypes == null || vos == null) {
    //            return;
    //        }
    //        //��ָ�갴����չ���ͷ��࣮
    //        ArrayList[] arys = divideInArrayByType(vos, nTypes);
    //        try {
    //            //��¼�����;��е�����
    //            ArrayList aryName = new ArrayList();
    //            ArrayList aryColName = new ArrayList();
    //            ArrayList aryProp = new ArrayList();
    //
    //            for (int i = 0; i < nTypes.length; i++) {
    //                getPropArraysByType(aryName, aryColName, aryProp, nTypes[i],
    //                        connect);
    //                addPropByType(arys[i], aryName, aryColName, aryProp, connect);
    //                aryName.clear();
    //                aryColName.clear();
    //                aryProp.clear();
    //            }
    //        } catch (SQLException se) {
    //            throw se;
    //        } finally {
    //        }
    //
    //    }
    //
    //    /**
    //     * ��Ӹ�����Ϣ��VO�С� �������ڣ�(2002-04-22 09:00:37)
    //     *
    //     * @param aryVo
    //     * java.util.ArrayList ������ָ��
    //     * @param aryname
    //     * java.util.ArrayList ���Ե�����
    //     * @param aryColname
    //     * java.util.ArrayList ���Զ�Ӧ����
    //     * @param aryProp
    //     * java.util.ArrayList ���Զ�Ӧ�е�����(int:char)
    //     * @param connect
    //     * java.sql.Connection
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    private void addPropByType(ArrayList aryVo, ArrayList aryname,
    //            ArrayList aryColname, ArrayList aryProp, Connection connect)
    //            throws SQLException {
    //
    //        if (aryVo.size() == 0) {
    //            return;
    //        }
    //        int nLen = aryname.size();
    //        StringBuffer strSelect = new StringBuffer();
    //        strSelect.append("select code,");
    //        for (int i = 0; i < nLen; i++) { //ƴ�����Բ������
    //            strSelect.append((String) aryColname.get(i));
    //            if (i != nLen - 1) {
    //                strSelect.append(",");
    //            }
    //        }
    //        strSelect.append(" from ");
    //        strSelect.append(DatabaseNames.IUFO_MEASURE_TABLE);
    //        strSelect.append(" where code in (");
    //        PreparedStatement prepStmt = null;
    //        ResultSet rs = null;
    //        try {
    //
    //            // int nVoLen = aryVo.size(); //
    //            Vector vMeasure = new Vector();
    //
    //            Vector vStr = toArrayString(aryVo, 40, vMeasure);
    //            for (int index = 0; index < vStr.size(); index++) {
    //                String strCond = (String) vStr.elementAt(index);
    //                // MeasureVO vo = (MeasureVO) aryVo.get(index);
    //
    //                Vector vGroupMeasure = (Vector) vMeasure.elementAt(index);
    //
    //                StringBuffer strOneSelect = new StringBuffer();
    //                strOneSelect.append(strSelect);
    //                strOneSelect.append(strCond);
    //                strOneSelect.append(")");
    //                prepStmt = connect.prepareStatement(strOneSelect.toString());
    //
    //                rs = prepStmt.executeQuery();
    //                while (rs.next()) {
    //                    String strCode = rs.getString(1);
    //                    Hashtable props = new Hashtable(12);
    //                    for (int i = 0; i < nLen; i++) {
    //                        //�õ����ݿ��Ӧ����
    //                        int nType = ((Integer) aryProp.get(i)).intValue();
    //                        //��������
    //                        String strPropName = (String) aryname.get(i);
    //                        if (nType == MeasureVO.PROP_CHAR) { //��Ӧ�����ַ�
    //                            props.put(strPropName, rs.getString(i + 2));
    //                        } else { //��Ӧ������ֵ
    //                            props.put(strPropName,
    //                                    new Integer(rs.getInt(i + 2)));
    //                        }
    //                    }
    //                    MeasureVO vo = null;
    //                    for (int i = 0; i < vGroupMeasure.size(); i++) {
    //                        if (((MeasureVO) vGroupMeasure.elementAt(i)).getCode()
    //                                .equals(strCode)) {
    //                            vo = (MeasureVO) vGroupMeasure.elementAt(i);
    //                            break;
    //                        }
    //                    }
    //                    if (vo != null)
    //                        vo.setProps(props);
    //                }
    //                if (rs != null) {
    //                    rs.close();
    //                }
    //                prepStmt.close();
    //            }
    //        } catch (SQLException se) {
    //            try {
    //                if (rs != null) {
    //                    rs.close();
    //                }
    //            } catch (Exception e) {
    //            }
    //            throw se;
    //        } finally {
    //
    //            if (prepStmt != null) {
    //                prepStmt.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * ������ݿ��¼�� �������ڣ�(2001-12-07 18:39:29)
    //     *
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    public void clearDataBase() throws java.sql.SQLException {
    //        PreparedStatement prepStmt = null;
    //        try {
    //            Connection con = getConnection();
    //            String deleteAllStm = "delete from "
    //                    + DatabaseNames.IUFO_MEASURE_TABLE;
    //            prepStmt = con.prepareStatement(deleteAllStm);
    //            prepStmt.executeUpdate();
    //        } catch (SQLException sqle) {
    //            throw sqle;
    //        } finally {
    //            if (prepStmt != null) {
    //                prepStmt.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * �������ϵͳȱʡָ��,����Ҫ�����չ�����ԡ� �������ڣ�(2002-04-21 14:52:27)
    //     *
    //     * @param types
    //     * int[] ��չ����
    //     * @param vos
    //     * nc.vo.iufo.measure.MeasureVO[] ָ������
    //     * @param connect
    //     * Connection ����
    //     * @exception SQLException
    //     * �쳣˵����
    //     */
    //    private void createExpandMessage(int[] types, Object[] vos,
    //            Connection connect) throws java.sql.SQLException {
    //        if (types == null || vos == null) {
    //            return;
    //        }
    //        //�������ָ��Vo����.
    //        ArrayList[] arys = divideInArrayByType(vos, types);
    //
    //        try {
    //            //��¼�����;��е�����
    //            ArrayList aryName = new ArrayList();
    //            ArrayList aryColName = new ArrayList();
    //            ArrayList aryProp = new ArrayList();
    //
    //            for (int i = 0; i < types.length; i++) {
    //                getPropArraysByType(aryName, aryColName, aryProp, types[i],
    //                        connect);
    //                updataPropByType(arys[i], aryName, aryColName, aryProp, connect);
    //                aryName.clear();
    //                aryColName.clear();
    //                aryProp.clear();
    //            }
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //        }
    //    }
    //
    //    /**
    //     * ����ָ��
    //     *
    //     * @param vos
    //     * Object[]
    //     * @throws java.sql.SQLException
    //     */
    //    private void createMeasure(Object[] vos, Connection con)
    //            throws SQLException {
    //        /**
    //         * insert into DatabaseNames.IUFO_MEASURE_TABLE (�ֶ�����) values(����)
    //         */
    //        if (vos == null || vos.length == 0) {
    //            return;
    //        }
    //        MeasureVO singleVO = null;
    //        MeasureVO batchVO = null;
    //        PreparedStatement prepStmt = null;
    //        Statement stmt = null;
    //        if (con == null) {
    //            con = getConnection();
    //        }
    //        try {
    //            if (vos.length > LEVEL) { //�������ݽ�����������
    //                stmt = con.createStatement();
    //
    //                //ÿ���ύ������¼����
    //                int nMaxRow = 100; //2004-07-14 liuyy
    //
    //                //stmt.getMaxRows() - 1;//�÷���Sqlserver��֧��.
    //                //�������
    //                //int nMaxCol = stmt.getMaxFieldSize();
    //                for (int i = 0; i < vos.length; i++) {
    //                    batchVO = (MeasureVO) vos[i];
    //                    if (batchVO == null) {
    //                        continue;
    //                    }
    //                    stmt.addBatch(getInsertSql(batchVO));
    //                    if (i > 0 && i % nMaxRow == 0) { //����ÿ���ύ���������
    //                        stmt.executeBatch();
    //                        stmt.close();
    //                        stmt = con.createStatement();
    //                    }
    //                }
    //                stmt.executeBatch();
    //                stmt.close();
    //            } else { //Ԥ����Ĵ���
    //                String insertStatement = "insert into "
    //                        + DatabaseNames.IUFO_MEASURE_TABLE
    //                        + " (
    // code,name,input_code,attribute,type,len,formula,note,dbtable,dbcolumn,code_ref_id,exttype,marks,rep_id,pk_key_comb)
    // values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    //                prepStmt = con.prepareStatement(insertStatement);
    //                singleVO = null;
    //                for (int i = 0; i < vos.length; i++) {
    //                    singleVO = (MeasureVO) vos[i];
    //                    if (singleVO == null) {
    //                        continue;
    //                    }
    //                    prepStmt.setString(1, singleVO.getCode()); //code
    //                    String strName = singleVO.getName();
    //                    prepStmt.setString(2, strName); //name
    //                    prepStmt.setString(3, singleVO.getInputCode()); //input_code
    //                    prepStmt.setInt(4, singleVO.getAttribute()); //attribe
    //                    prepStmt.setInt(5, singleVO.getType()); //type
    //                    prepStmt.setInt(6, singleVO.getLen()); //len
    //                    prepStmt.setString(7, singleVO.getFormula()); //formula
    //                    prepStmt.setString(8, singleVO.getNote()); //note
    //                    prepStmt.setString(9, singleVO.getDbtable()); //dbtable
    //                    prepStmt.setString(10, singleVO.getDbcolumn()); //dbcolumn
    //                    prepStmt.setString(11, singleVO.getRefPK()); //code_ref_id
    //                    prepStmt.setInt(12, singleVO.getExttype());
    //                    prepStmt.setInt(13, singleVO.getMarks());
    //                    prepStmt.setString(14, singleVO.getReportPK()); //rep_id
    //                    prepStmt.setString(15, singleVO.getKeyCombPK());//pk_key_comb
    //                    prepStmt.executeUpdate();
    //                }
    //                prepStmt.close();
    //            }
    //            //�ж�ϵͳ�Ƿ������������Ϣ
    //            int[] nTypes = nc.ui.iufo.measure.IMeasSysInfo
    //                    .getMEASURE_EXTNED_TYPES();
    //            if (nTypes != null) {
    //                createExpandMessage(nTypes, vos, con);
    //            }
    //        } catch (SQLException e) {
    //            System.out.println("MeasureDMO Exception :_____" + e.getMessage());
    //            throw e;
    //        } finally {
    //            if (prepStmt != null) {
    //                prepStmt.close();
    //            }
    //            if (stmt != null) {
    //                stmt.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * ����ϵͳ���ڵ���չ���ͣ������е�ָ����顣 �������ڣ�(2002-04-23 10:13:00)
    //     *
    //     * @return java.util.ArrayList
    //     * @param vos
    //     * java.lang.Object[]
    //     * @param exttypes
    //     * int[]
    //     */
    //    private ArrayList[] divideInArrayByType(Object[] vos, int[] exttypes) {
    //        ArrayList[] arys = new ArrayList[exttypes.length];
    //        for (int i = 0; i < arys.length; i++) { //��ʼ��
    //            arys[i] = new ArrayList();
    //        }
    //        for (int i = 0; i < vos.length; i++) {
    //            MeasureVO vo = (MeasureVO) vos[i];
    //            for (int j = 0; j < exttypes.length; j++) {
    //                if (vo.getExttype() == exttypes[j]) {
    //                    arys[j].add(vo);
    //                    break;
    //                }
    //            }
    //        }
    //        return arys;
    //
    //    }
    //
    //
    //
    //    /**
    //     * �õ����е�ָ��Id.
    //     *
    //     * @return jVector
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    public final Vector getAllMeasureIDs() throws java.sql.SQLException {
    //        String sSltIds = "select code from " + DatabaseNames.IUFO_MEASURE_TABLE;
    //        Connection con = getConnection();
    //        PreparedStatement pstmt = null;
    //        ResultSet rs = null;
    //        Vector vec = new Vector(200);
    //        try {
    //            pstmt = con.prepareStatement(sSltIds);
    //            rs = pstmt.executeQuery();
    //            while (rs.next()) {
    //                vec.add(rs.getString(1));
    //            }
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //            if (rs != null) {
    //                rs.close();
    //            }
    //            if (pstmt != null) {
    //                pstmt.close();
    //            }
    //        }
    //        return vec;
    //    }
    //
    //    /**
    //     * �˴����뷽��˵���� �������ڣ�(2001-12-11 16:17:37)
    //     *
    //     * @return java.lang.String[]
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    public String getCreateTableSql(String strTableName)
    //            throws java.sql.SQLException {
    //        if (strTableName == null || strTableName.equals("")
    //                || strTableName.equalsIgnoreCase("null"))
    //            return null;
    //        String createSql = "create table " + strTableName + "( ";
    //        // boolean bHave = false;
    //
    //        String loadStmt = "select dbcolumn,type,len from "
    //                + DatabaseNames.IUFO_MEASURE_TABLE + " where dbtable='"
    //                + strTableName + "'";
    //        Connection con = getConnection();
    //        PreparedStatement pstmt = null;
    //        ResultSet rs = null;
    //        try {
    //            if (GlobalValue.DATABASE_TYPE
    //                    .equalsIgnoreCase(GlobalValue.DATABASE_ORACLE)) {
    //                createSql += " alone_id VARCHAR2(20) not null,";
    //                createSql += " line_no number(4),";
    //                createSql += " input_time char(20),";
    //
    //                pstmt = con.prepareStatement(loadStmt);
    //                rs = pstmt.executeQuery();
    //                while (rs.next()) {
    //                    String column_name = rs.getString(1);
    //                    int type = rs.getInt(2);
    //                    int len = rs.getInt(3);
    //                    if (type == MeasureVO.TYPE_NUMBER)
    //                        createSql += (column_name + " number default 0,");
    //
    //                    else {
    //                        int ncharLen;
    //
    //                        if (type == MeasureVO.TYPE_CHAR) {
    //                            int mLen = (len * 2) / 64;
    //                            if (mLen >= 1)
    //                                ncharLen = 64 * (mLen + 1);
    //                            else
    //                                ncharLen = 64;
    //                        } else
    //                            ncharLen = 128;
    //                        createSql += (column_name + " varchar2(" + ncharLen + ") default '',");
    //                    }
    //
    //                }
    //
    //                String tableId = strTableName
    //                        .substring(strTableName.length() - 5);
    //                createSql += " constraint PK_IUFO_MEASURE_DATA" + tableId
    //                        + " primary key (alone_id,line_no))";
    //                /*
    //                 * + "constraint FK_IUFO_MD" + tableId + "_REF_MPD foreign key
    //                 * (alone_id) " + " references iufo_measure_pubdata (alone_id)
    //                 * )";
    //                 */
    //
    //            } else if (GlobalValue.DATABASE_TYPE
    //                    .equalsIgnoreCase(GlobalValue.DATABASE_SQLSERVER)) {
    //                createSql += " alone_id VARCHAR(20) not null,";
    //                createSql += " line_no numeric(4),";
    //                createSql += " input_time char(20),";
    //
    //                pstmt = con.prepareStatement(loadStmt);
    //                rs = pstmt.executeQuery();
    //                while (rs.next()) {
    //                    String column_name = rs.getString(1);
    //                    int type = rs.getInt(2);
    //                    int len = rs.getInt(3);
    //                    if (type == MeasureVO.TYPE_NUMBER)
    //                        createSql += (column_name + " float default 0,");
    //
    //                    else {
    //                        int ncharLen;
    //
    //                        if (type == MeasureVO.TYPE_CHAR) {
    //                            int mLen = (len * 2) / 64;
    //                            if (mLen >= 1)
    //                                ncharLen = 64 * (mLen + 1);
    //                            else
    //                                ncharLen = 64;
    //                        } else
    //                            ncharLen = 128;
    //                        createSql += (column_name + " varchar(" + ncharLen + ") default '',");
    //                    }
    //
    //                }
    //
    //                String tableId = strTableName
    //                        .substring(strTableName.length() - 5);
    //                createSql += " constraint PK_IUFO_MEASURE_DATA" + tableId
    //                        + " primary key (alone_id,line_no))";
    //                /*
    //                 * + "constraint FK_IUFO_MD" + tableId + "_REF_MPD foreign key
    //                 * (alone_id) " + " references iufo_measure_pubdata (alone_id)
    //                 * )";
    //                 */
    //
    //            }
    //
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //            if (rs != null) {
    //                rs.close();
    //            }
    //            if (pstmt != null) {
    //                pstmt.close();
    //            }
    //        }
    //        return createSql;
    //
    //    }
    //
    //    /**
    //     * �õ�����������Sql��䡣 �������ڣ�(2002-05-08 11:05:40)
    //     *
    //     * @return java.lang.String
    //     * @param vo
    //     * nc.vo.iufo.measure.MeasureVO
    //     */
    //    private String getInsertSql(MeasureVO batchVO) {
    //        StringBuffer batchString = new StringBuffer(300);
    //        batchString.append("insert into ");
    //        batchString.append(DatabaseNames.IUFO_MEASURE_TABLE);
    //        batchString
    //                .append(" ( code, name, input_code, attribute,
    // type,len,formula,note,dbtable,dbcolumn,code_ref_id,exttype,marks,rep_id,pk_key_comb)
    // values (");
    //        batchString.append(getString(batchVO.getCode().trim()));
    //        batchString.append(",");
    //        String strName = batchVO.getName();
    //        batchString.append(getString(strName));
    //        batchString.append(",");
    //        batchString.append(getString(batchVO.getInputCode()));
    //        batchString.append(",");
    //        batchString.append(batchVO.getAttribute());
    //        batchString.append(",");
    //        batchString.append(batchVO.getType());
    //        batchString.append(",");
    //        batchString.append(batchVO.getLen());
    //        batchString.append(",");
    //        batchString.append(getString(batchVO.getFormula()));
    //        batchString.append(",");
    //        String strNote = batchVO.getNote();
    //        if (strNote != null) {
    //            strNote = convertSQL(strNote);
    //        }
    //        batchString.append(getString(strNote));
    //        batchString.append(",");
    //        batchString.append(getString(batchVO.getDbtable()));
    //        batchString.append(",");
    //        batchString.append(getString(batchVO.getDbcolumn()));
    //        batchString.append(",");
    //        batchString.append(getString(batchVO.getRefPK()));
    //        batchString.append(",");
    //        batchString.append(batchVO.getExttype());
    //        batchString.append(",");
    //        batchString.append(batchVO.getMarks());
    //        batchString.append(",");
    //        batchString.append((getString(batchVO.getReportPK())));
    //        batchString.append(",");
    //        batchString.append(getString(batchVO.getKeyCombPK()));
    //        batchString.append(")");
    //        return batchString.toString();
    //    }
    //
    //    /**
    //     * �������Ƶõ�ָ��ı��롣 �������ڣ�(2002-04-28 16:35:03)
    //     *
    //     * @return MeasureVO
    //     * @param name
    //     * java.lang.String
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    // public MeasureVO getMeasureByName(String name) throws
    //    // java.sql.SQLException {
    //    // if (name == null) {
    //    // return null;
    //    // }
    //    // String sSltIds = m_sSelectFrom + " where name = ?";
    //    // Connection con = getConnection();
    //    // PreparedStatement pstmt = null;
    //    // ResultSet rs = null;
    //    // try {
    //    // pstmt = con.prepareStatement(sSltIds);
    //    // pstmt.setString(1, name);
    //    // rs = pstmt.executeQuery();
    //    // if (rs.next()) {
    //    // return generateSysVO(rs);
    //    // }
    //    // }
    //    // catch (SQLException e) {
    //    // throw e;
    //    // }
    //    // finally {
    //    // if (rs != null) {
    //    // rs.close();
    //    // }
    //    // if (pstmt != null) {
    //    // pstmt.close();
    //    // }
    //    // }
    //    // return null;
    //    // }
    //    /**
    //     * ���ָ��������Ϣ�� �������ڣ�(2003-9-27 9:02:22)
    //     *
    //     * @return nc.vo.iufo.measure.MeasurePropVO[]
    //     */
    //    private static MeasurePropVO[] getMeasureProps(Connection con)
    //            throws SQLException {
    //        //if (MEASURE_PROPS != null) {
    //        //return MEASURE_PROPS;
    //        //}
    //        PreparedStatement ps = null;
    //        ResultSet rs = null;
    //        try {
    //            ps = con.prepareStatement(getSelPropString());
    //            rs = ps.executeQuery();
    //            Vector vecMeasureProps = new Vector();
    //            while (rs.next()) {
    //                MeasurePropVO prop = new MeasurePropVO();
    //                prop.setName(rs.getString(1));
    //                prop.setColName(rs.getString(2));
    //                prop.setProp(rs.getInt(3));
    //                prop.setType(rs.getInt(4));
    //                vecMeasureProps.add(prop);
    //            }
    //
    //            if (vecMeasureProps != null) {
    //                MEASURE_PROPS = new MeasurePropVO[vecMeasureProps.size()];
    //                vecMeasureProps.copyInto(MEASURE_PROPS);
    //            }
    //            return MEASURE_PROPS;
    //        } finally {
    //            if (rs != null) {
    //                rs.close();
    //            }
    //            if (ps != null) {
    //                ps.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * Insert the method's description here. Creation date: (2002-9-27
    // 10:28:18)
    //     *
    //     * @return java.util.Hashtable
    //     * @param strName
    //     * java.lang.String
    //     * @param strCode
    //     * java.lang.String
    //     */
    //    public Object[] getMeasuresByNameAndCode(String strName, String strCode)
    //            throws SQLException {
    //        Hashtable returnTable = new Hashtable();
    //        //ƴ��ѯ�����ַ���
    //        StringBuffer queryStr = new StringBuffer();
    //        queryStr.append(" where");
    //        if (null != strName) {
    //            queryStr.append(" name like '%");
    //            queryStr.append(strName);
    //            queryStr.append("%'");
    //        }
    //        if (null != strCode && null != strName) {
    //            queryStr.append(" and code like '");
    //            queryStr.append(strCode);
    //            queryStr.append("%'");
    //        } else if (null != strCode && null == strName) {
    //            queryStr.append(" code like '");
    //            queryStr.append(strCode);
    //            queryStr.append("%'");
    //        }
    //
    //        String currentStr = "select code from "
    //                + DatabaseNames.IUFO_MEASURE_TABLE + queryStr.toString();
    //
    //        Connection con = getConnection();
    //        PreparedStatement pstmt = null;
    //        ResultSet rs = null;
    //
    //        pstmt = con.prepareStatement(currentStr);
    //        rs = pstmt.executeQuery();
    //        while (rs.next()) {
    //            String currentCode = rs.getString("code");
    //            returnTable.put(currentCode, currentCode);
    //        }
    //
    //        //����
    //        if (returnTable.size() > 0)
    //            return returnTable.values().toArray();
    //        else
    //            return null;
    //    }
    //
    //    /**
    //     * �������ͼ���MeasureProp����Ϣ�� �������ڣ�(2003-9-27 9:24:01)
    //     *
    //     * @param aryName
    //     * java.util.ArrayList
    //     * @param aryColName
    //     * java.util.ArrayList
    //     * @param aryProp
    //     * java.util.ArrayList
    //     * @param nType
    //     * int
    //     */
    //    private static void getPropArraysByType(ArrayList aryName,
    //            ArrayList aryColName, ArrayList aryProp, int nType, Connection con)
    //            throws SQLException {
    //        MeasurePropVO[] props = getMeasureProps(con);
    //        if (props != null) {
    //            for (int i = 0; i < props.length; i++) {
    //                if (props[i].getType() == nType) {
    //                    aryName.add(props[i].getName());
    //                    aryColName.add(props[i].getColName());
    //                    aryProp.add(new Integer(props[i].getProp()));
    //                }
    //            }
    //        }
    //    }
    //
    //    /**
    //     * �õ���������ѡ���Sql��䡣 �������ڣ�(2002-04-23 11:05:54)
    //     *
    //     * @return java.lang.String
    //     * @param type
    //     * int
    //     */
    //    private String getSelByTypeString(int type) {
    //        StringBuffer strBuf = new StringBuffer();
    //        strBuf.append(m_sSelectFrom);
    //        strBuf.append(" where type ");
    //        if (type == MeasureVO.TYPE_NORMAL) { //��ָͨ��
    //            strBuf.append(" in (");
    //            strBuf.append(MeasureVO.TYPE_CHAR);
    //            strBuf.append(",");
    //            strBuf.append(MeasureVO.TYPE_NUMBER);
    //            strBuf.append(",");
    //            strBuf.append(MeasureVO.TYPE_CODE);
    //            strBuf.append(")");
    //        } else { //��������ָ��
    //            strBuf.append(" = ");
    //            strBuf.append(type);
    //        }
    //        return strBuf.toString();
    //    }
    //
    //    /**
    //     * �˴����뷽�������� �������ڣ�(2002-05-08 11:23:14)
    //     *
    //     * @return java.lang.String
    //     * @param type
    //     * int
    //     */
    //    private String getSelExtSql(int type) {
    //        StringBuffer strBuf = new StringBuffer();
    //        strBuf.append(m_sSelectFrom);
    //        strBuf.append(" where type = ");
    //        strBuf.append(type);
    //        return strBuf.toString();
    //    }
    //
    //    /**
    //     * �õ�ȡ��ָ����չ���Ե�Sql��䡣 �������ڣ�(2002-04-23 10:25:26)
    //     *
    //     * @return java.lang.String
    //     */
    //    private static String getSelPropString() {
    //        StringBuffer strSel = new StringBuffer();
    //        strSel.append("select name ,column_name ,prop ,type from ");
    //        strSel.append(DatabaseNames.IUFO_MEASURE_PROP_TABLE);
    //        return strSel.toString();
    //    }
    //
    //    /**
    //     * �ս�Sql���ʱ,�����ַ�ֵ�ı任 ����ʱ�䣺(2002-03-19 18:18:21)
    //     *
    //     * @return java.lang.String
    //     * @param aStr
    //     * java.lang.String
    //     */
    //    private String getString(String aStr) {
    //        if (aStr == null) {
    //            return "null";
    //        } else {
    //            return "'" + aStr + "'";
    //        }
    //    }
    //    /**
    //     * ��������ָ�꣬��Cache�й������а������ⶨ���ڵ㡣
    //     *
    //     * @return nc.vo.iufo.measure.MeasureVO[]
    //     * @throws java.sql.SQLException
    //     */
    //    public MeasureVO[] loadMeasure() throws SQLException {
    //        /**
    //         * ���ձ����������أ���ָ֤�������ص���ȷ
    //         */
    //        String findNameStmt = m_sSelectFrom + " order by code asc";
    //        Connection con = getConnection();
    //
    //        PreparedStatement pstmt = null;
    //        ResultSet rs = null;
    //        Vector vecMeasureVOs = new Vector();
    //        try {
    //            pstmt = con.prepareStatement(findNameStmt);
    //            rs = pstmt.executeQuery();
    //            // String parentId = null;
    //            // MeasureVO mVO = null;
    //            while (rs.next()) {
    //                vecMeasureVOs.add(MeasureTableDMO.generateSysVO(rs));
    //            }
    //            MeasureVO[] measureVOs = new MeasureVO[vecMeasureVOs.size()];
    //            vecMeasureVOs.copyInto(measureVOs);
    //            addMeasureProp(measureVOs, con);
    //            return measureVOs;
    //
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //            if (rs != null) {
    //                rs.close();
    //            }
    //            if (pstmt != null) {
    //                pstmt.close();
    //            }
    //            if (con != null) {
    //                con.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * ����ָ����������Ӧ��ָ�ꡣ
    //     *
    //     * @notic ���ص�ָ�겻������ָ�����Ϣ�� �������ڣ�(2001-11-03 10:51:55)
    //     * @return java.lang.Object[]
    //     * @param codes
    //     * java.lang.String[]
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    public MeasureVO[] loadMeasuresByCodes(String[] codes) throws
    // SQLException {
    //        // ���500�����ϵĲ�ѯ����ֹin������������
    //        MeasureVO[] ms = new MeasureVO[0];
    //        if (codes.length <= 500)
    //            return loadMeasuresByCodes(codes, null);
    //        else {
    //            int count = 500;
    //            int pos = 0;
    //            while (count > 0) {
    //                String[] tcodes = new String[count];
    //                System.arraycopy(codes, pos, tcodes, 0, count);
    //                MeasureVO[] tms = loadMeasuresByCodes(tcodes, null);
    //                MeasureVO[] tms1 = new MeasureVO[count + ms.length];
    //                System.arraycopy(ms, 0, tms1, 0, ms.length);
    //                System.arraycopy(tms, 0, tms1, ms.length, tms.length);
    //                ms = tms1;
    //
    //                pos += 500;
    //                if (codes.length - pos >= 500)
    //                    count = 500;
    //                else
    //                    count = codes.length - pos;
    //            }
    //        }
    //        return ms;
    //    }
    //
    //    /**
    //     * ����ָ����������Ӧ��ָ�ꡣ
    //     *
    //     * @notic ���ص�ָ�겻������ָ�����Ϣ�� �������ڣ�(2001-11-03 10:51:55)
    //     * @return java.lang.Object[]
    //     * @param codes
    //     * java.lang.String[]
    //     * @param con
    //     * Connection
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    private MeasureVO[] loadMeasuresByCodes(String[] codes, Connection con)
    //            throws SQLException {
    //
    //        if (con == null) {
    //            con = getConnection();
    //        }
    //        String findMeasuresStmt = m_sSelectFrom + " where code in ('";
    //
    //        if (codes == null || codes.length == 0) {
    //            return null;
    //        }
    //        for (int i = 0; i < codes.length; i++) {
    //            findMeasuresStmt += (codes[i] + "','");
    //        }
    //        findMeasuresStmt += "')";
    //        Vector vector = new Vector();
    //        PreparedStatement pstmt = null;
    //        ResultSet rs = null;
    //        try {
    //            pstmt = con.prepareStatement(findMeasuresStmt);
    //            rs = pstmt.executeQuery();
    //            // MeasureVO mVO = null;
    //            while (rs.next()) {
    //                vector.add(MeasureTableDMO.generateSysVO(rs));
    //            }
    //            /// Array.
    //            Object[] objs = vector.toArray();
    //            //*********iufo2.2���
    //            addMeasureProp(objs, con);
    //            //********
    //            MeasureVO[] vos = null;
    //            if (objs.length > 0) {
    //                vos = new MeasureVO[objs.length];
    //                for (int i = 0; i < objs.length; i++) {
    //                    vos[i] = (MeasureVO) objs[i];
    //                }
    //            }
    //            return vos;
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //            if (rs != null) {
    //                rs.close();
    //            }
    //            if (pstmt != null) {
    //                pstmt.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * �������ͷ�����Ӧ��ָ�ꡣ�� �������ڣ�(2001-11-19 09:55:58)
    //     *
    //     * @return nc.vo.iufo.measure.MeasureVO[]
    //     * @param nType
    //     * int��MeasureVO.TYPE_NORMAL-������ָͨ�ꣻMeasureVO.TYPE_MUTATIVE-���и���ָ�ꡣ
    //     * @param bExtends
    //     * boolean �Ƿ�Ϊ��չ����.
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    public MeasureVO[] loadMeasuresByType(int nType, boolean bExtends)
    //            throws SQLException {
    //        String findMeasuresStmt = bExtends ? getSelExtSql(nType)
    //                : getSelByTypeString(nType);
    //        Vector vector = new Vector();
    //        Connection con = getConnection();
    //        PreparedStatement pstmt = null;
    //        ResultSet rs = null;
    //        try {
    //            pstmt = con.prepareStatement(findMeasuresStmt);
    //            rs = pstmt.executeQuery();
    //            // MeasureVO mVO = null;
    //            while (rs.next()) {
    //                vector.add(MeasureTableDMO.generateSysVO(rs));
    //            }
    //            //// Array.
    //            Object[] objs = vector.toArray();
    //            //iufo2.2���
    //            addMeasureProp(objs, con);
    //
    //            MeasureVO[] vos = null;
    //            if (objs.length > 0) {
    //                vos = new MeasureVO[objs.length];
    //                for (int i = 0; i < objs.length; i++) {
    //                    vos[i] = (MeasureVO) objs[i];
    //                }
    //            }
    //            return vos;
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //            if (rs != null) {
    //                rs.close();
    //            }
    //            if (pstmt != null) {
    //                pstmt.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * ɾ��ָ�ꡣ
    //     *
    //     * @param vos
    //     * MeasureVO[] ��Ҫɾ����VO����.
    //     * @throws java.sql.SQLException
    //     * @throws NamingException
    //     * @throws SystemException
    //     */
    //    public void removeMeasure(MeasureVO[] vos) throws SQLException,
    //            SystemException, NamingException {
    //        if (vos == null || vos.length == 0) {
    //            return;
    //        }
    //        Connection con = getConnection();
    //        String deleteStm = "delete from " + DatabaseNames.IUFO_MEASURE_TABLE
    //                + " where code in(";
    //
    //        Statement stmt = null;
    //        try {
    //
    //            stmt = con.createStatement();
    //
    //            Vector vecSQL = new Vector();
    //            StringBuffer sb = new StringBuffer(deleteStm);
    //            for (int i = 0; i < vos.length; i++) {
    //                sb.append("'");
    //                sb.append(vos[i].getCode());
    //                sb.append("'");
    //                sb.append(",");
    //                if ((i > 0 && i % 100 == 0) || i == vos.length - 1) {
    //                    String strDel = sb.substring(0, sb.length() - 1);
    //                    strDel = strDel + ")";
    //                    vecSQL.add(strDel);
    //                    sb = new StringBuffer(deleteStm);
    //                }
    //            }
    //            if (vecSQL.size() > 0) {
    //                for (int i = 0; i < vecSQL.size(); i++) {
    //                    stmt.addBatch((String) vecSQL.elementAt(i));
    //                    if ((i > 0 && i % 100 == 0) || i == vecSQL.size() - 1) {
    //                        stmt.executeBatch();
    //                        stmt.clearBatch();
    //                    }
    //                }
    //            }
    //            stmt.close();
    //            removeData(vos, con);
    //
    //            MeasureTableDMO mtDMO = new MeasureTableDMO(
    //                    DataManageObjectIufo.IUFO_DATASOURCE);
    //            mtDMO.recycleTable(vos, con);
    //
    //        } catch (SQLException e) {
    //            throw e;
    //        }
    //
    //        finally {
    //            if (stmt != null) {
    //                stmt.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * �˴����뷽�������� �������ڣ�(2002-06-17 10:32:48)
    //     *
    //     * @param mvo
    //     * nc.vo.iufo.measure.MeasureVO
    //     * @param sOld
    //     * java.lang.String
    //     * @param sNew
    //     * java.lang.String
    //     */
    //    private void replaceNameByRptC(MeasureVO mvo, String sOld, String sNew) {
    //        if (mvo == null) {
    //            return;
    //        }
    //        String strOldName = mvo.getName();
    //        //���Ʊ�Ȼ�Ǳ�������������Ϣ
    //        mvo.setName(sNew + strOldName.substring(sOld.length()));
    //    }
    //
    //    /**
    //     * Insert the method's description here. Creation date: (2003-4-14
    // 15:10:59)
    //     *
    //     * @author: κ����
    //     * @return java.util.Vector
    //     * @param strUnitCodes
    //     * java.lang.String[]
    //     * @param iLen
    //     * int
    //     */
    //    private Vector toArrayString(ArrayList measureList, int iLen,
    //            Vector vRetMeasure) {
    //        Vector vStr = new Vector();
    //        int iNum = 0;
    //        String strOne = "";
    //        Vector vOne = new Vector();
    //        for (int i = 0; i < measureList.size(); i++) {
    //            MeasureVO measure = (MeasureVO) measureList.get(i);
    //
    //            if (iNum != 0)
    //                strOne += ",";
    //            strOne += "'" + measure.getCode() + "'";
    //
    //            vOne.add(measure);
    //
    //            iNum++;
    //            if (iNum == iLen
    //                    || (i == measureList.size() - 1 && strOne.length() > 0)) {
    //                vStr.add(strOne);
    //                vRetMeasure.add(vOne);
    //                strOne = "";
    //                iNum = 0;
    //                vOne = new Vector();
    //            }
    //        }
    //        return vStr;
    //    }
    //
    //    /**
    //     * ����ָ�����չ���ԡ� �������ڣ�(2002-04-23 10:03:40)
    //     *
    //     * @param vos
    //     * Object[]
    //     * @param connect
    //     * java.sql.Connection
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    private void updataMeasureProp(Object[] vos, Connection connect)
    //            throws SQLException {
    //
    //        int[] nTypes = nc.ui.iufo.measure.IMeasSysInfo
    //                .getMEASURE_EXTNED_TYPES();
    //        if (nTypes == null || vos == null) {
    //            return;
    //        }
    //        //��ָ�갴����չ���ͷ��࣮
    //        ArrayList[] arys = divideInArrayByType(vos, nTypes);
    //        //��¼�����;��е�����
    //        ArrayList aryName = new ArrayList();
    //        ArrayList aryColName = new ArrayList();
    //        ArrayList aryProp = new ArrayList();
    //        try {
    //            for (int i = 0; i < nTypes.length; i++) {
    //                getPropArraysByType(aryName, aryColName, aryProp, nTypes[i],
    //                        connect);
    //
    //                updataPropByType(arys[i], aryName, aryColName, aryProp, connect);
    //                aryName.clear();
    //                aryColName.clear();
    //                aryProp.clear();
    //            }
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //        }
    //    }
    //
    //    /**
    //     * ����VO�еĸ�����Ϣ�� �������ڣ�(2002-04-22 09:00:37)
    //     *
    //     * @param aryVo
    //     * java.util.ArrayList ������ָ��
    //     * @param aryname
    //     * java.util.ArrayList ���Ե�����
    //     * @param aryColname
    //     * java.util.ArrayList ���Զ�Ӧ����
    //     * @param aryProp
    //     * java.util.ArrayList ���Զ�Ӧ�е�����(int:char)
    //     * @param connect
    //     * java.sql.Connection
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    private void updataPropByType(ArrayList aryVo, ArrayList aryname,
    //            ArrayList aryColname, ArrayList aryProp, Connection connect)
    //            throws SQLException {
    //
    //        if (aryVo.size() == 0) {
    //            return;
    //        }
    //        int nLen = aryname.size();
    //        StringBuffer strUpdata = new StringBuffer();
    //        strUpdata.append("update ");
    //        strUpdata.append(DatabaseNames.IUFO_MEASURE_TABLE);
    //        strUpdata.append(" set ");
    //
    //        for (int i = 0; i < nLen; i++) { //ƴ�����Բ������
    //            strUpdata.append((String) aryColname.get(i));
    //            strUpdata.append(" = ? ");
    //            if (i != nLen - 1) {
    //                strUpdata.append(",");
    //            }
    //        }
    //        strUpdata.append(" where code = ?");
    //        PreparedStatement prepStmt = null;
    //        int nVoLen = aryVo.size(); //
    //        try {
    //            prepStmt = connect.prepareStatement(strUpdata.toString());
    //            for (int index = 0; index < nVoLen; index++) {
    //                MeasureVO vo = (MeasureVO) aryVo.get(index);
    //                Hashtable props = vo.getProps();
    //                if (props == null) {
    //                    continue;
    //                }
    //                for (int i = 0; i < nLen; i++) {
    //                    //�õ����ݿ��Ӧ����
    //                    int nType = ((Integer) aryProp.get(i)).intValue();
    //                    //��������
    //                    String strPropName = (String) aryname.get(i);
    //                    Object obj = props.get(strPropName);
    //                    if (nType == MeasureVO.PROP_CHAR) { //��Ӧ�����ַ�
    //                        prepStmt.setString(i + 1, (String) obj);
    //                    } else { //��Ӧ������ֵ
    //                        int nValue = obj == null ? 0 : Integer.parseInt(obj
    //                                .toString());
    //                        prepStmt.setInt(i + 1, nValue);
    //                    }
    //                }
    //                prepStmt.setString(nLen + 1, vo.getCode());
    //                prepStmt.executeUpdate();
    //            }
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //            if (prepStmt != null) {
    //                prepStmt.close();
    //            }
    //        }
    //
    //    }
    //
    //    /**
    //     * ���ڱ���ı�����Ķ�����ָ������ơ� �������ڣ�(2002-06-17 09:58:34)
    //     *
    //     * @param ids
    //     * java.lang.String[] ָ�����
    //     * @param oldCode
    //     * java.lang.String �ɵı������
    //     * @param newCode
    //     * java.lang.String �µı������
    //     * @param Vector
    //     * ���������Ƶ�ָ����
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    public Vector updateMeasByPrtC(String[] ids, String oldCode, String
    // newCode)
    //            throws java.sql.SQLException {
    //        Connection con = getConnection();
    //        //�õ����е�ָ��
    //        if (ids == null || oldCode == null || newCode == null) {
    //            return null;
    //        }
    //        MeasureVO[] vos = loadMeasuresByCodes(ids, con);
    //        if (vos == null) {
    //            return null;
    //        }
    //        Vector array = new Vector();
    //        MeasureVO mvo = null;
    //        for (int i = 0; i < vos.length; i++) {
    //            mvo = vos[i];
    //            if (vos[i].isHidden()) {
    //                //��������ָ�갴�չ����������
    //                replaceNameByRptC(mvo, oldCode, newCode);
    //                array.add(mvo);
    //            }
    //        }
    //        int nSize = array.size();
    //        //����ָ������.
    //        String updateStatement = "update " + DatabaseNames.IUFO_MEASURE_TABLE
    //                + " set name=? where code = ? ";
    //        PreparedStatement prepStmt = null;
    //
    //        try {
    //            prepStmt = con.prepareStatement(updateStatement);
    //            for (int i = 0; i < nSize; i++) {
    //                mvo = (MeasureVO) array.get(i);
    //                prepStmt.setString(1, mvo.getName());
    //                prepStmt.setString(2, mvo.getCode());
    //                prepStmt.executeUpdate();
    //            }
    //        } catch (SQLException se) {
    //            se.printStackTrace();
    //            throw se;
    //        } finally {
    //            if (prepStmt != null) {
    //                prepStmt.close();
    //            }
    //        }
    //        return array;
    //
    //    }
    //
    //    /**
    //     * �޸�ָ�����������
    //     *
    //     * @param vos
    //     * ValueObject[]
    //     * @throws java.sql.SQLException
    //     */
    //    public void updateMeasure(MeasureVO[] vos) throws SQLException {
    //        /**
    //         * update DatabaseNames.IUFO_MEASURE_TABLE set �ֶ����� = ? where id = ?
    //         */
    //        updateMeasure(vos, null);
    //    }
    //
    //    /**
    //     * �޸�ָ�����������
    //     *
    //     * @param vos
    //     * MeasureVO[]
    //     * @param con
    //     * Connection
    //     * @throws java.sql.SQLException
    //     */
    //    private void updateMeasure(MeasureVO[] vos, Connection con)
    //            throws SQLException {
    //        /**
    //         * update DatabaseNames.IUFO_MEASURE_TABLE set �ֶ����� = ? where id = ?
    //         */
    //        if (vos.length == 0) {
    //            return;
    //        }
    //        if (con == null) {
    //            con = getConnection();
    //        }
    //        MeasureVO singleVO = null;
    //        // MeasureVO batchVO = null;
    //        PreparedStatement prepStmt = null;
    //        try {
    //            String updateStatement = null;
    //            // String updatePreStatement = null;
    //            /*
    //             * ��������һ��ָ�꣨����ΪA,B,C���ڱ������о�������ɾ�����е�Ԫ�Ĳ�����
    //             * ʹָ֮�����Ƹ�Ϊ��B,C,D���󣬱��浽���ݿ���ʱ������ָ��������Ψһ������
    //             * ��ô�ڸ���ԭ��κA��ָ��ʱ�ͻ���Ϊ�µ�ָ������B��ɵ�ָ����B��ͻ���޷����棬��ʵ������������ǺϷ���
    //             *
    //             * Ϊ��ָֹ�������������޷������ݿ������޸Ĳ����� ������Ԥ����������ص�ָ���ָ�������ȸ�Ϊĳһ�������ƣ�
    //             * Ȼ���ڽ���������ָ����£������Ϳ��Ա�ָ֤�����Ʋ�����ֳ�ͻ�� ��������ǰ�������ǣ��ڸ���ָ��֮ǰ��ָ���Ѿ�������飬
    //             */
    //            //��ֻ��һ��ָ����Ҫ�޸ĵ�ʱ�򣬲����ϳ���������ִ��ִ����������������˲��迼��
    //            //if(vos.length > 1)
    //            //{
    //            //updatePreStatement = "update "
    //            //+ DatabaseNames.IUFO_MEASURE_TABLE
    //            //+ " set name=? where code = ?";
    //            //prepStmt = con.prepareStatement(updatePreStatement);
    //            //singleVO = null;
    //            //for (int i = 0; i < vos.length; i++) {
    //            //singleVO = vos[i];
    //            //prepStmt.setString(1, singleVO.getName());
    //            //prepStmt.setString(2, singleVO.getCode());
    //            //prepStmt.executeUpdate();
    //            //}
    //            //prepStmt.close();
    //            //}
    //            updateStatement = "update "
    //                    + DatabaseNames.IUFO_MEASURE_TABLE
    //                    + " set input_code = ?,attribute = ?,len = ? ,formula = ?,note = ? ,
    // dbtable = ?,dbcolumn = ?,code_ref_id = ?,type = ?,exttype = ?,marks = ? ,
    // name=? ,rep_id = ?,pk_key_comb=? where code = ? ";
    //            prepStmt = con.prepareStatement(updateStatement);
    //            singleVO = null;
    //            for (int i = 0; i < vos.length; i++) {
    //                singleVO = vos[i];
    //                prepStmt.setString(1, singleVO.getInputCode()); //input_code
    //                prepStmt.setInt(2, singleVO.getAttribute()); //attribe
    //                prepStmt.setInt(3, singleVO.getLen()); //len
    //                prepStmt.setString(4, singleVO.getFormula()); //formula
    //                prepStmt.setString(5, singleVO.getNote());
    //                prepStmt.setString(6, singleVO.getDbtable());
    //                prepStmt.setString(7, singleVO.getDbcolumn()); //note
    //                prepStmt.setString(8, singleVO.getRefPK());
    //                prepStmt.setInt(9, singleVO.getType());
    //                prepStmt.setInt(10, singleVO.getExttype());
    //                prepStmt.setInt(11, singleVO.getMarks());
    //                String strName = singleVO.getName();
    //                prepStmt.setString(12, strName);
    //                prepStmt.setString(13, singleVO.getReportPK());
    //                prepStmt.setString(14, singleVO.getKeyCombPK());//pk_key_comb
    //                prepStmt.setString(15, singleVO.getCode());
    //                prepStmt.executeUpdate();
    //            }
    //            updataMeasureProp(vos, con);
    //        } catch (SQLException e) {
    //            e.printStackTrace();
    //            throw e;
    //        } finally {
    //            if (prepStmt != null) {
    //                prepStmt.close();
    //            }
    //        }
    //
    //    }
    //
    //    /**
    //     * �޸�ָ�꼰���¼�ָ��ĵ�λ.
    //     *
    //     * @param vos
    //     * ValueObject
    //     * @throws java.sql.SQLException
    //     */
    //    public void updateTotalMeasAttr(MeasureVO mvo) throws SQLException {
    //        boolean bOracal = true;
    //        if (GlobalValue.DATABASE_TYPE
    //                .equalsIgnoreCase(GlobalValue.DATABASE_ORACLE)) {
    //        } else if (GlobalValue.DATABASE_TYPE
    //                .equalsIgnoreCase(GlobalValue.DATABASE_SQLSERVER)) {
    //            bOracal = false;
    //        } else {
    //            throw new SQLException(
    //                    "The database doesn't support the curren operation!");
    //        }
    //        Connection con = getConnection();
    //        String updateStatement = "update " + DatabaseNames.IUFO_MEASURE_TABLE
    //                + " set attribute = ? " + " where code like ?";
    //        PreparedStatement prepStmt = null;
    //        try {
    //            prepStmt = con.prepareStatement(updateStatement);
    //            prepStmt.setInt(1, mvo.getAttribute());
    //            prepStmt.setString(2, bOracal ? (mvo.getCode().trim() + "%") : ("["
    //                    + mvo.getCode().trim() + "]%"));
    //            // int num = prepStmt.executeUpdate();
    //        } catch (SQLException e) {
    //            throw e;
    //        } finally {
    //            if (prepStmt != null) {
    //                prepStmt.close();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * ָ���������ݱ��пռ� Creation date: (2002-06-20 11:02:55)
    //     *
    //     * @return boolean
    //     * @param vecInfo
    //     * Vector
    //     * @param vo
    //     * nc.vo.iufo.measure.MeasureVO
    //     * @param con
    //     * Connection
    //     */
    //    // private boolean useRecycledCol(Vector vecInfo, MeasureVO vo,
    // Connection
    //    // con)
    //    // throws SQLException {
    //    // boolean bNumber = MeasureTableDMO.isNumber(vo.getType());
    //    // int nSize = vecInfo.size();
    //    // DBStatusVO status = null;
    //    // if (bNumber) { //��������
    //    // for (int i = 0; i < nSize; i++) {
    //    // status = (DBStatusVO) vecInfo.get(i);
    //    // if (status.getType() == DBStatusVO.INT) { //����Ӧ��
    //    // vo.setDbtable(status.getTablename());
    //    // vo.setDbcolumn(status.getColname());
    //    // removeDbStatus(status, con);
    //    // vecInfo.remove(status);
    //    // return true;
    //    // }
    //    // }
    //    // }
    //    // else { //�ַ�����
    //    // for (int i = 0; i < nSize; i++) {
    //    // status = (DBStatusVO) vecInfo.get(i);
    //    // if (status.getType() == DBStatusVO.CHAR
    //    // && status.getLen() == getSpace(vo)) { //Ҫ�󳤶���ͬ
    //    // vo.setDbtable(status.getTablename());
    //    // vo.setDbcolumn(status.getColname());
    //    // removeDbStatus(status, con);
    //    // vecInfo.remove(status);
    //    // return true;
    //    // }
    //    // }
    //    // }
    //    // return false;
    //    // }
    //    public CacheObjPackage[] loadMeasureObjPackages() throws SQLException {
    //        // String selectSql = "select code,rep_id from " +
    //        // DatabaseNames.IUFO_MEASURE_TABLE;
    //        // Connection con = null;
    //        // Statement stat = null;
    //        // ResultSet rs = null;
    //        // try{
    //        // con = getConnection();
    //        // stat = con.createStatement();
    //        // rs = stat.executeQuery(selectSql);
    //        // Hashtable table = new Hashtable();
    //        // while(rs.next()){
    //        // String code = rs.getString(1);
    //        // String repId = rs.getString(2);
    //        // if(code != null && repId != null){
    //        // CacheObjPackage pack = (CacheObjPackage)table.get(repId);
    //        // if(pack == null){
    //        // pack = new CacheObjPackage(repId);
    //        // table.put(repId, pack);
    //        // }
    //        // pack.addCacheObjID(code);
    //        // }
    //        // }
    //        // if(table.size() > 0){
    //        // CacheObjPackage[] result = new CacheObjPackage[table.size()];
    //        // Enumeration keys = table.keys();
    //        // int k=0;
    //        // while(keys.hasMoreElements()){
    //        // result[k] = (CacheObjPackage)table.get(keys.nextElement());
    //        // k++;
    //        // }
    //        // return result;
    //        // }
    //        // }finally{
    //        // if(rs != null)
    //        // rs.close();
    //        // if(stat != null)
    //        // stat.close();
    //        // if(con != null)
    //        // con.close();
    //        // }
    //        //�����ڱ����߶˴�ʱ��һ�μ�������ID���ǱȽ��������Ը�Ϊһ��ʼʲôҲ�����أ���Ҫʱֱ�Ӵ����ݿ��ѯ
    //        return null;
    //    }
    //
    //    public CacheObjPackage loadMeasureObjPackage(String repId)
    //            throws SQLException {
    //        if (repId == null) {
    //            return null;
    //        }
    //        String selectSql = "select code,rep_id from "
    //                + DatabaseNames.IUFO_MEASURE_TABLE + " where rep_id = '"
    //                + repId + "'";
    //        Connection con = null;
    //        Statement stat = null;
    //        ResultSet rs = null;
    //        try {
    //            con = getConnection();
    //            stat = con.createStatement();
    //            rs = stat.executeQuery(selectSql);
    //            CacheObjPackage result = new CacheObjPackage(repId);
    //            while (rs.next()) {
    //                String code = rs.getString(1);
    //                if (code != null) {
    //                    result.addCacheObjID(code);
    //                }
    //            }
    //            return result;
    //        } finally {
    //            if (rs != null)
    //                rs.close();
    //            if (stat != null)
    //                stat.close();
    //            if (con != null)
    //                con.close();
    //        }
    //    }
    //
    //    /**
    //     * �������ڣ�(2002-3-13 16:57:34)
    //     * @author��л�� @version������޸�����
    //     * ����ָ�귵����Ҫ��ָ�����ʱ��Ĭ��ȡֵ ע�⣺ ���������Ϊ������ݿ���ָ������ֵר�õģ��ڷ���ֵʱ������SQL���
    //     * ��д���������ַ�������ֵΪ�������������Ŀմ����� "''"
    //     *
    //     * @concurrency��ͬ��˵�� @see����Ҫ�μ����� @return java.lang.String
    //     * @param measure
    //     * nc.vo.iufo.measure.MeasureVO
    //     */
    //    private String getMeasureZeroData(MeasureVO measure) {
    //        if (measure == null) {
    //            return "''";
    //        }
    //        switch (measure.getType()) {
    //        //���ַ���ָ�꣬���س���Ϊ0���ַ���
    //        case MeasureVO.TYPE_CHAR:
    //        //case MeasureVO.TYPE_CHAR_MUTATIVE:
    //        case MeasureVO.TYPE_CODE:
    //        //case MeasureVO.TYPE_CODE_MUTATIVE:
    //        //case MeasureVO.TYPE_MUTATIVE:
    //        case MeasureVO.TYPE_NORMAL:
    //            return "''";
    //        //����ֵ��ָ�꣬����0
    //        case MeasureVO.TYPE_NUMBER:
    //            //case MeasureVO.TYPE_NUMBER_MUTATIVE:
    //            //case MeasureVO.TYPE_TOTAL:
    //            return "0";
    //        default:
    //            return "''";
    //        }
    //    }
    //
    //    /**
    //     * ��ָ�갴��ָ�����ݱ�ı������� �������ڣ�(2001-11-13 9:43:21)
    //     *
    //     * @return java.util.Vector
    //     * @param measureData
    //     * nc.vo.iufo.input.MeasureDataVO[]
    //     */
    //    private Hashtable getTableFromMeasure(MeasureVO[] measures) {
    //        if (measures == null || measures.length == 0)
    //            return null;
    //        Hashtable hashMeasure = new Hashtable();
    //        for (int i = 0; i < measures.length; i++) {
    //            MeasureVO meavo = measures[i];
    //            if (meavo != null) {
    //                String sTableName = meavo.getDbtable();
    //                if (sTableName != null && !"".equals(sTableName)) {
    //                    if (hashMeasure.containsKey(sTableName)) {
    //                        Vector vecMeasure = (Vector) hashMeasure
    //                                .get(sTableName);
    //                        vecMeasure.addElement(meavo);
    //                    } else {
    //                        Vector vecMeasure = new Vector();
    //                        vecMeasure.addElement(meavo);
    //                        hashMeasure.put(sTableName, vecMeasure);
    //                    }
    //                }
    //            }
    //        }
    //        return hashMeasure;
    //    }
    //
    //    /**
    //     * �������ڣ�(2002-3-14 9:23:45)
    //     *
    //     * @author��л�� @version������޸����� �������ָ��������Ҫ��SQL���
    //     * @concurrency��ͬ��˵�� @see����Ҫ�μ����� @param
    //     * aloneIds java.lang.String[]
    //     * @param measures
    //     * nc.vo.iufo.measure.MeasureVO[]
    //     */
    //    private String[] geneEmptyMeaDataSqls(MeasureVO[] measures) {
    //
    //        //ȡ��ָ���������ݿ��
    //        Hashtable tables = getTableFromMeasure(measures);
    //        if (tables == null || tables.size() == 0) {
    //            return null;
    //        }
    //        int nTable = tables.size();
    //        String[] updateSqls = new String[nTable];
    //        Enumeration enume = tables.keys();
    //        int nLoop = 0;
    //        while (enume.hasMoreElements()) {
    //            String strTableName = (String) enume.nextElement();
    //            Vector vecMeas = (Vector) tables.get(strTableName);
    //            if (vecMeas == null || vecMeas.size() == 0) {
    //                continue;
    //            }
    //            StringBuffer sb = new StringBuffer();
    //            sb.append("update ");
    //            sb.append(strTableName);
    //            sb.append(" set ");
    //            for (int i = 0; i < vecMeas.size(); i++) {
    //                MeasureVO measure = (MeasureVO) vecMeas.elementAt(i);
    //                String strColumnName = measure.getDbcolumn();
    //                sb.append(strColumnName);
    //                sb.append(" = ");
    //                sb.append(getMeasureZeroData(measure));
    //                if (i < vecMeas.size() - 1) {
    //                    sb.append(", ");
    //                }
    //            }
    //            updateSqls[nLoop] = sb.toString();
    //            nLoop++;
    //        }
    //        return updateSqls;
    //    }
    //
    //    
    //    
    //    
    //    /**
    //     * �ж�һ��ָ���Ƿ��Ѿ���¼���ָ�����ݡ�
    //     *
    //     * �������ڣ�(2002-12-12 11:39:42)
    //     *
    //     * @author������Ƽ @return boolean[]
    //     * @param vos
    //     * nc.vo.iufo.measure.MeasureVO[]
    //     * @exception java.sql.SQLException
    //     * �쳣˵����
    //     */
    //    public boolean[] existMeasureData(MeasureVO[] vos)
    //            throws java.sql.SQLException {
    //        if (vos == null || vos.length <= 0)
    //            return null;
    //
    //        boolean[] bReturns = new boolean[vos.length];
    //
    //        Connection con = getConnection();
    //
    //        String selectStm = null;
    //        PreparedStatement stmt = null;
    //        ResultSet rs = null;
    //        for (int i = 0; i < vos.length; i++) {
    //            if (vos[i].getDbtable() == null || vos[i].getDbcolumn() == null)
    //                continue;
    //
    //            selectStm = "select 1 from " + vos[i].getDbtable() + " where "
    //                    + vos[i].getDbcolumn();
    //            boolean bNumber = MeasureTableDMO.isNumber(vos[i].getType());
    //            if (bNumber) {
    //                selectStm += " <> 0 ";
    //            } else {
    //                selectStm += " is null ";
    //            }
    //            stmt = con.prepareStatement(selectStm);
    //            rs = stmt.executeQuery();
    //            if (rs.next()) {
    //                bReturns[i] = true;
    //            }
    //
    //            rs.close();
    //            rs = null;
    //            stmt.close();
    //            stmt = null;
    //            selectStm = null;
    //        }
    //        con.close();
    //
    //        return bReturns;
    //    }
    //    

    /**
     * ���������ط�ʽ��ֻ���ض����ID�������뻺���ˢ�¼�¼�� �������ڣ�(2003-11-7 11:48:09)
     * 
     * @return nc.pub.iufo.cache.RefreshedObjDesc[]
     */
    public nc.pub.iufo.cache.base.RefreshedObjDesc[] lightweightLoad()
            throws SQLException {

        String strSelectSql = "select " + MEASURE_PACK_PK + " from "
                + DatabaseNames.IUFO_MEASURE_TABLE;

        Vector vecRefreshObjs = new Vector();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(strSelectSql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RefreshedObjDesc refreshObj = new RefreshedObjDesc(
                        MeasurePackVO.class.getName(), rs.getString(1),
                        RefreshedObjDesc.OP_ADD);
                vecRefreshObjs.add(refreshObj);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {

            }

        }
        if (vecRefreshObjs.size() > 0) {
            RefreshedObjDesc[] refreshObjs = new RefreshedObjDesc[vecRefreshObjs
                    .size()];
            vecRefreshObjs.toArray(refreshObjs);
            return refreshObjs;
        }
        return null;
    }

    /**
     * --------------***--------------
     * 
     * Ϊ���Ʊ������Ч�� ָ���Ϊָ����ķ�ʽ��ȡ liuyy. 2005-01-05
     *  
     */

    /**
     * ����ָ����, �����½�ָ����PK 2005-1-5 by liuyy.
     * 
     * @param mpvo
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public MeasurePackVO create(MeasurePackVO mpvo) throws SQLException,
            IOException {

        if (mpvo == null)
            return null;

        String strSQL = "insert into " + DatabaseNames.IUFO_MEASURE_TABLE
                + " (" + MEASURE_PACK_PK + "," + REPORT_PK + "," + KEY_GROUP_PK
                + "," + MEASURES + " ) values (?,?,?,?) ";

        Connection con = null;
        CrossDBPreparedStatement prepStmt = null;

        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);

            try {
                prepStmt.setString(1, mpvo.getPackgeID());
                prepStmt.setString(2, mpvo.getRepPK());
                prepStmt.setString(3, mpvo.getKeyGroupPK());
                prepStmt.setBytes(4, measures2Bytes(mpvo.getMeasures()));

                prepStmt.executeUpdate();

            } catch (SQLException e) {
                AppDebug.debug("ReportPK:" + mpvo.getRepPK()//@devTools System.out.println("ReportPK:" + mpvo.getRepPK() + "; "
                        + "KeyGroupPK:" + mpvo.getKeyGroupPK() + "; "
                        + "MeasPackPK:" + mpvo.getPackgeID() + "; ");
                throw e;
            }

        } finally {
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }
        return mpvo;

    }

    /**
     * �������ָ����, �����½�ָ����PK 2005-1-5 by liuyy.
     * 
     * @param mpvo
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public void createPackages(MeasurePackVO[] mpvos) throws SQLException,
            IOException {

        if (mpvos == null || mpvos.length == 0) {
            return;
        }

        int nLen = mpvos.length;
        String strSQL = "insert into " + DatabaseNames.IUFO_MEASURE_TABLE
                + " (" + MEASURE_PACK_PK + "," + REPORT_PK + "," + KEY_GROUP_PK
                + "," + MEASURES + " ) values (?,?,?,?) ";

        Connection con = null;
        MeasurePackVO mpvo = null;
        CrossDBPreparedStatement prepStmt = null;

        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);

            for (int i = 0; i < nLen; i++) {
                mpvo = mpvos[i];
                if (mpvo == null)
                    continue;
                //sql server�к���ÿ�ζ����ؽ�prepStmt
//                prepStmt = (CrossDBPreparedStatement) con
//                	.prepareStatement(strSQL);
                
                prepStmt.setString(1, mpvo.getPackgeID());
                prepStmt.setString(2, mpvo.getRepPK());
                prepStmt.setString(3, mpvo.getKeyGroupPK());
                prepStmt.setBytes(4, measures2Bytes(mpvo.getMeasures()));

                prepStmt.executeUpdate();
            }
        } catch (SQLException e) {
            AppDebug.debug("ReportPK:" + mpvo.getRepPK()//@devTools System.out.println("ReportPK:" + mpvo.getRepPK() + "; "
                    + "KeyGroupPK:" + mpvo.getKeyGroupPK() + "; "
                    + "MeasPackPK:" + mpvo.getPackgeID() + "; ");
            throw e;

        } finally {
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }
    }

    /**
     * �޸�ָ���� 2005-1-5 by liuyy.
     * 
     * @param mpvo
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public boolean update(MeasurePackVO mpvo) throws SQLException, IOException {

        if (mpvo == null)
            return false;

        String strSQL = "update " + DatabaseNames.IUFO_MEASURE_TABLE + " set "
                + REPORT_PK + " = ?," + KEY_GROUP_PK + " = ?," + MEASURES
                + " = ? " + " where " + MEASURE_PACK_PK + " = ? ";

        Connection con = null;
        CrossDBPreparedStatement prepStmt = null;
        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);

            prepStmt.setString(1, mpvo.getRepPK());
            prepStmt.setString(2, mpvo.getKeyGroupPK());
            prepStmt.setBytes(3, measures2Bytes(mpvo.getMeasures()));
            prepStmt.setString(4, mpvo.getPackgeID());
            prepStmt.executeUpdate();

        } catch (SQLException e) {
            AppDebug.debug("ReportPK:" + mpvo.getRepPK()//@devTools System.out.println("ReportPK:" + mpvo.getRepPK() + "; "
                    + "KeyGroupPK:" + mpvo.getKeyGroupPK() + "; "
                    + "MeasPackPK:" + mpvo.getPackgeID() + "; ");
            throw e;

        } finally {
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }
        return true;
    }

    /**
     * �޸Ķ��ָ���� 2005-1-5 by liuyy.
     * 
     * @param mpvos
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public void updatePackages(MeasurePackVO[] arrmpvos) throws SQLException,
            IOException {

        if (arrmpvos == null)
            return;

        String strSQL = " update " + DatabaseNames.IUFO_MEASURE_TABLE + " set "
                + REPORT_PK + " = ?," + KEY_GROUP_PK + " = ?," + MEASURES
                + " = ? " + " where " + MEASURE_PACK_PK + " = ? ";

        Connection con = null;
        MeasurePackVO mpvo = null;
        CrossDBPreparedStatement prepStmt = null;

        int nLen = arrmpvos.length;

        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con.prepareStatement(strSQL);
            for (int i = 0; i < nLen; i++) {                

                mpvo = arrmpvos[i];
                prepStmt.setString(1, mpvo.getRepPK());
                prepStmt.setString(2, mpvo.getKeyGroupPK());
                prepStmt.setBytes(3, measures2Bytes(mpvo.getMeasures()));
                prepStmt.setString(4, mpvo.getPackgeID());
                prepStmt.executeUpdate();
                
            }

        } catch (SQLException e) {
            AppDebug.debug("ReportPK:" + mpvo.getRepPK()//@devTools System.out.println("ReportPK:" + mpvo.getRepPK() + "; "
                    + "KeyGroupPK:" + mpvo.getKeyGroupPK() + "; "
                    + "MeasPackPK:" + mpvo.getPackgeID() + "; ");
            throw e;

        } finally {
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }

    }

    /**
     * 2005-1-5 by liuyy.
     * 
     * @param hashMeasures
     * @return
     */
    private byte[] measures2Bytes(Hashtable hashMeasures) {
        if (hashMeasures == null) {
            return null;
        }

        //Ϊ�������Ч��, Ԥ��ָ��VOתΪBytes�ĳ���. liuyy. 2005-3-21 
        final int BYTES_LENGTH_FOR_MEAS = 650;

        byte[] bytes = null;
        try {
            //
            //            long nTime = 0;
            //            if(GlobalValue.DEBUG){
            //                nTime = System.currentTimeMillis();
            //            }

            int nLen = hashMeasures.size();

            //            MeasureVO[] arrmvos = new MeasureVO[nLen];
            //            hashMeasures.values().toArray(arrmvos);

            nLen = BYTES_LENGTH_FOR_MEAS * nLen;
            //�����ֽ����������
            ByteArrayOutputStream o = new ByteArrayOutputStream(nLen);
            //����gzipѹ�������
            //GZIPOutputStream gzout = new GZIPOutputStream(o);
            //�����������л������
            ObjectOutputStream out = new ObjectOutputStream(o);

            out.writeObject(hashMeasures);

            out.flush();
            out.close();
            //gzout.close();
            //����ѹ���ֽ���
            bytes = o.toByteArray();
            o.close();

            //            AppDebug.debug("Measure2Bytes: " + (System.currentTimeMillis() - nTime));

        } catch (IOException e) {
            AppDebug.debug(e);//@devTools System.out.println(e);
        }
        return bytes;
    }

    /**
     * ɾ��ָ���� 2005-1-5 by liuyy.
     * 
     * @param mpvo
     * @return
     * @throws SQLException
     */
    public boolean remove(String strMeasurePackPK) throws SQLException {

        if (strMeasurePackPK == null)
            return false;

        String strSQL = "delete from " + DatabaseNames.IUFO_MEASURE_TABLE
                + " where " + MEASURE_PACK_PK + " = ? ";

        Connection con = null;
        CrossDBPreparedStatement prepStmt = null;
        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);
            prepStmt.setString(1, strMeasurePackPK);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw e;

        } finally {
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }

        //ɾ��ָ�����ݱ�

        return true;

    }

    /**
     * ɾ��ָ���� 2005-1-5 by liuyy.
     * 
     * @param mpvo
     * @return
     * @throws SQLException
     */
    public void removePackages(String[] arrMeasPackPKs) throws SQLException {

        if (arrMeasPackPKs == null)
            return;

        String strSQL = "delete from " + DatabaseNames.IUFO_MEASURE_TABLE
                + " where " + MEASURE_PACK_PK + " = ? ";

        Connection con = null;
        CrossDBPreparedStatement prepStmt = null;

        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con.prepareStatement(strSQL);
            int nLen = arrMeasPackPKs.length;
            for (int i = 0; i < nLen; i++) {
                prepStmt.setString(1, arrMeasPackPKs[i]);
                prepStmt.executeUpdate();           
            }
        } catch (SQLException e) {
            throw e;

        } finally {
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }
    }

    /**
     * ɾ��ָ�������ָ���� 2005-1-5 by liuyy.
     * 
     * @param mpvo
     * @return
     * @throws SQLException
     */
    public boolean removeByRepPK(String strRepPK) throws SQLException {

        if (strRepPK == null)
            return false;

        String strSQL = "delete from " + DatabaseNames.IUFO_MEASURE_TABLE
                + " where " + REPORT_PK + " = ? ";

        Connection con = null;
        CrossDBPreparedStatement prepStmt = null;
        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);
            prepStmt.setString(1, strRepPK);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw e;

        } finally {
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }
        return true;
    }
    

    /**
     * ɾ��ָ�������ָ���� 2005-1-5 by liuyy.
     * 
     * @param mpvo
     * @return
     * @throws SQLException
     */
    public boolean removeByRepPKs(String arrRepPK[]) throws SQLException {

        if (arrRepPK == null || arrRepPK.length < 1)
            return false;

        String strSQL = "delete from " + DatabaseNames.IUFO_MEASURE_TABLE
                + " where " + REPORT_PK + " in ( ";

        for(int i = 0; i < arrRepPK.length; i++){
            strSQL += "'" + arrRepPK[i] + "', ";
        }
        
        strSQL += "'')";
        
        Connection con = null;
        CrossDBPreparedStatement prepStmt = null;
        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);
        
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw e;

        } finally {
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }
        return true;
    }

    /**
     * �õ�ָ��PK��ָ����
     * 
     * 2005-1-5 by liuyy.
     * 
     * @param strMeasPackPK
     * @return
     * @throws SQLException
     */
    public MeasurePackVO getByPackagePK(String strMeasPackPK)
            throws SQLException {

        if (strMeasPackPK == null)
            return null;

        String strSQL = "select " + MEASURE_PACK_PK + "," + REPORT_PK + ","
                + KEY_GROUP_PK + "," + MEASURES + " from "
                + DatabaseNames.IUFO_MEASURE_TABLE + " where "
                + MEASURE_PACK_PK + " = ? ";

        Connection con = null;
        ResultSet rs = null;
        CrossDBPreparedStatement prepStmt = null;
        MeasurePackVO mpvo = null;

        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);

            prepStmt.setString(1, strMeasPackPK);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                mpvo = genMeasurePackVO(rs);
            }

        } catch (SQLException e) {
            throw e;

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (prepStmt != null)
                    prepStmt.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {

            }
        }
        return mpvo;

    }

    /**
     * ���ݲ�ѯ�������һ����¼����ָ�������
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    private MeasurePackVO genMeasurePackVO(ResultSet rs) throws SQLException {
        MeasurePackVO mpvo = new MeasurePackVO();
        mpvo.setPackgeID(rs.getString(1));
        mpvo.setRepPK(rs.getString(2));
        mpvo.setKeyGroupPK(rs.getString(3));

        //��ȡ�����ʽ��Ϣ������������
        byte[] objBytes = null;
        try {
            objBytes = ((CrossDBResultSet) rs).getBlobBytes(4);
        } catch (NegativeArraySizeException e) {
        } catch (Exception e) {
        }
        Hashtable measures = null;
        if (objBytes != null && objBytes.length > 0) {
            measures = (Hashtable) convertBytesToObject(objBytes, false);
        }
        if (measures == null) {
            measures = new Hashtable();
        }
        mpvo.setMeasures(measures);
        return mpvo;
    }

    /**
     * �õ��õ�ָ�������ָ����
     * 
     * 2005-1-5 by liuyy.
     * 
     * @param strRepPK
     * @return
     * @throws SQLException
     */
    public MeasurePackVO[] getByRepPK(String strRepPK) throws SQLException {

        if (strRepPK == null)
            return null;

        String strSQL = "select " + MEASURE_PACK_PK + "," + REPORT_PK + ","
                + KEY_GROUP_PK + "," + MEASURES + " from "
                + DatabaseNames.IUFO_MEASURE_TABLE + " where " + REPORT_PK
                + " = ? ";

        Connection con = null;
        ResultSet rs = null;
        CrossDBPreparedStatement prepStmt = null;
        MeasurePackVO mpvo = null;
        Vector vec = new Vector();
        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);

            prepStmt.setString(1, strRepPK);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                mpvo = genMeasurePackVO(rs);
                vec.addElement(mpvo);
            }

        } catch (SQLException e) {
            throw e;

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (prepStmt != null)
                    prepStmt.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {

            }

        }
        int nLen = vec.size();
        MeasurePackVO[] arrmpvos = new MeasurePackVO[nLen];
        vec.copyInto(arrmpvos);
        return arrmpvos;

    }

    /**
     * liuyy. 2005-3-16
     * 
     * ����ָ����������ָ����Ϣ�� �������ڣ�(2003-8-15 14:16:36)
     *
     * @return nc.vo.iufo.measure.MeasureVO[]
     * @param codes
     * java.lang.String[]
     * @throws SQLException
     */
    public MeasureVO[] loadMeasuresByCodes(String[] arrCodes)
            throws SQLException {
        if (arrCodes == null) {
            return null;
        }

        String strMeasPK = null;
        String strMeasPackPK = null;
        Vector vecMeasPackPKs = new Vector();
        int nLen = arrCodes.length;

        for (int i = 0; i < nLen; i++) {
            strMeasPK = arrCodes[i];
            if (strMeasPK == null
                    || strMeasPK.length() < MeasurePackVO.MEASUREPACK_PK_LENGTH) {
                continue;
            }
            strMeasPackPK = strMeasPK.substring(0,
                    MeasurePackVO.MEASUREPACK_PK_LENGTH);
            if (!vecMeasPackPKs.contains(strMeasPackPK)) {
                vecMeasPackPKs.add(strMeasPackPK);
            }
        }

        nLen = vecMeasPackPKs.size();
        String[] arrMeasPackPKs = new String[nLen];
        vecMeasPackPKs.copyInto(arrMeasPackPKs);
        MeasurePackVO[] arrmpvos = this.getByPackagePKs(arrMeasPackPKs);
        MeasurePackVO mpvo = null;

        int nMeasureNum = arrCodes.length;
        MeasureVO[] arrmvos = new MeasureVO[nMeasureNum];

        if (arrmpvos != null) {
            nLen = arrmpvos.length;
        } else {
            nLen = -1;
        }
        for (int i = 0; i < nLen; i++) {
            mpvo = arrmpvos[i];
            if (mpvo == null) {
                continue;
            }
            Hashtable hashMeas = mpvo.getMeasures();
            if (hashMeas == null) {
                continue;
            }
            for (int j = 0; j < nMeasureNum; j++) {
                strMeasPK = arrCodes[j];
                if(strMeasPK == null){
                    continue;
                }
                if (hashMeas.containsKey(strMeasPK)) {
                    arrmvos[j] = (MeasureVO) hashMeas.get(strMeasPK);
                }
            }
        }

        return arrmvos;

    }

    /**
     * �õ��õ������ָ����
     * 
     *2005-1-5 by liuyy. 
     *
     * @param strRepPK
     * @return
     * @throws SQLException
     */
    private MeasurePackVO[] getByPackagePKs(String[] arrMeasPackPKs,
            Connection con) throws SQLException {

        if (arrMeasPackPKs == null)
            return null;

        String strSQL = "select " + MEASURE_PACK_PK + "," + REPORT_PK + ","
                + KEY_GROUP_PK + "," + MEASURES + " from "
                + DatabaseNames.IUFO_MEASURE_TABLE + " where "
                + MEASURE_PACK_PK + " in (";

        int nLen = arrMeasPackPKs.length;
        if (nLen < 1) {
            return null;
        }

        StringBuffer sbIds = new StringBuffer();
        for (int j = 0; j < nLen; j++) {
            sbIds.append("'");
            sbIds.append(arrMeasPackPKs[j]);
            sbIds.append("'");
            if (j != nLen - 1) {
                sbIds.append(",");
            }
        }
        sbIds.append(")");

        strSQL += sbIds.toString();

        if (con == null) {
            con = getConnection();
        }
        ResultSet rs = null;
        CrossDBPreparedStatement prepStmt = null;
        MeasurePackVO mpvo = null;
        Vector vec = new Vector();
        try {
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);

            nLen = arrMeasPackPKs.length;

            rs = prepStmt.executeQuery();
            while (rs.next()) {
                mpvo = genMeasurePackVO(rs);
                vec.addElement(mpvo);
            }

        } catch (SQLException e) {
            throw e;

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (prepStmt != null)
                    prepStmt.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {

            }

        }
        nLen = vec.size();
        MeasurePackVO[] arrmpvos = new MeasurePackVO[nLen];
        vec.copyInto(arrmpvos);
        return arrmpvos;

    }

    /**
     * �õ��õ������ָ����
     * 
     *2005-1-5 by liuyy. 
     *
     * @param strRepPK
     * @return
     * @throws SQLException
     */
    public MeasurePackVO[] getByPackagePKs(String[] arrMeasPackPKs)
            throws SQLException {

        if (arrMeasPackPKs == null)
            return null;
        // ���500�����ϵĲ�ѯ����ֹin������������
        MeasurePackVO[] arrReturnMPVOs = new MeasurePackVO[0];
        if (arrMeasPackPKs.length <= 500)
            return getByPackagePKs(arrMeasPackPKs, null);
        else {
            int count = 500;
            int pos = 0;
            while (count > 0) {
                String[] tcodes = new String[count];
                System.arraycopy(arrMeasPackPKs, pos, tcodes, 0, count);
                MeasurePackVO[] tms = getByPackagePKs(tcodes, null);
                if(tms != null){
	                MeasurePackVO[] tms1 = new MeasurePackVO[count
	                        + arrReturnMPVOs.length];
	                System.arraycopy(arrReturnMPVOs, 0, tms1, 0,
	                        arrReturnMPVOs.length);
	                System.arraycopy(tms, 0, tms1, arrReturnMPVOs.length,
	                        tms.length);
	                arrReturnMPVOs = tms1;
                }
                pos += 500;
                if (arrMeasPackPKs.length - pos >= 500)
                    count = 500;
                else
                    count = arrMeasPackPKs.length - pos;
            }
        }
        return arrReturnMPVOs;

    }

    /**
     * �õ����е�ָ����
     * 
     *2005-1-5 by liuyy. 
     *
     * @param strRepPK
     * @return
     * @throws SQLException
     */
    public MeasurePackVO[] getAll() throws SQLException {

        String strSQL = "select " + MEASURE_PACK_PK + "," + REPORT_PK + ","
                + KEY_GROUP_PK + "," + MEASURES + " from "
                + DatabaseNames.IUFO_MEASURE_TABLE;

        Connection con = null;
        ResultSet rs = null;
        CrossDBPreparedStatement prepStmt = null;
        MeasurePackVO mpvo = null;
        Vector vec = new Vector();
        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                mpvo = genMeasurePackVO(rs);
                vec.addElement(mpvo);
            }

        } catch (SQLException e) {
            throw e;

        } finally {
            try{
            if (rs != null)
                rs.close();
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
            } catch (Exception e){
                
            }
        }
        int nLen = vec.size();
        MeasurePackVO[] arrmpvos = new MeasurePackVO[nLen];
        vec.copyInto(arrmpvos);
        return arrmpvos;

    }

    /**
     * ���ݱ���PK����ָ����VO
     * liuyy. 2005-3-29 
     *
     * @param repId
     * @return
     * @throws SQLException
     */
    public CacheObjPackage loadMeasurePackObjPackage(String repId)
            throws SQLException {
        if (repId == null) {
            return null;
        }

        String strSQL = "select " + MEASURE_PACK_PK + "," + REPORT_PK + ","
                + KEY_GROUP_PK + "," + MEASURES + " from "
                + DatabaseNames.IUFO_MEASURE_TABLE + " where " + REPORT_PK
                + " = ? ";

        Connection con = null;
        ResultSet rs = null;
        CrossDBPreparedStatement prepStmt = null;
        MeasurePackVO mpvo = null;
        CacheObjPackage result = new CacheObjPackage(repId);

        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);
            prepStmt.setString(1, repId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                mpvo = genMeasurePackVO(rs);
                result.addCacheObj(mpvo);
            }

        } catch (SQLException e) {
            throw e;

        } finally {
            try{
	            if (rs != null)
	                rs.close();
	            if (prepStmt != null)
	                prepStmt.close();
	            if (con != null)
	                con.close();
            }catch(Exception e){
                
            }
        }
        return result;

    }

    /**
     * ���ݱ���PK����ָ����PK
     * liuyy. 2005-3-29 
     *
     * @param repId
     * @return
     * @throws SQLException
     */
    public String[] loadMeasPackPKByRepId(String repId) throws SQLException {
        if (repId == null) {
            return null;
        }

        String strSQL = "select " + MEASURE_PACK_PK + " from "
                + DatabaseNames.IUFO_MEASURE_TABLE + " where " + REPORT_PK
                + " = ? ";

        Connection con = null;
        ResultSet rs = null;
        CrossDBPreparedStatement prepStmt = null;       
        Vector vecPKs = new Vector();

        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);
            prepStmt.setString(1, repId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                String strMeasPackPK = rs.getString(1);
                if(strMeasPackPK != null){
                    vecPKs.addElement(strMeasPackPK);
                }
            }

        } catch (SQLException e) {
            throw e;

        } finally {
            try{
	            if (rs != null)
	                rs.close();
	            if (prepStmt != null)
	                prepStmt.close();
	            if (con != null)
	                con.close();
            }catch(Exception e){
                
            }
        }
        String[] arrPKs = new String[vecPKs.size()];
        vecPKs.copyInto(arrPKs);
        return arrPKs;

    }
    
    public String[] loadMeasurePackPKByKeyCombPK(String strKeyCombPK) throws SQLException{
        String strSQL = "select " + MEASURE_PACK_PK + " from "
		        + DatabaseNames.IUFO_MEASURE_TABLE + " where KEYGROUPPK" 
		        + " = ? ";
		
		Connection con = null;
		ResultSet rs = null;
		CrossDBPreparedStatement prepStmt = null;       
		Vector<String> vecPKs = new Vector<String>();
		
		try {
		    con = getConnection();
		    prepStmt = (CrossDBPreparedStatement) con
		            .prepareStatement(strSQL);
		    prepStmt.setString(1, strKeyCombPK);
		    rs = prepStmt.executeQuery();
		    while (rs.next()) {
		        String strMeasPackPK = rs.getString(1);
		        if(strMeasPackPK != null){
		            vecPKs.addElement(strMeasPackPK);
		        }
		    }
		
		} catch (SQLException e) {
		    throw e;
		} finally {
		    try{
		        if (rs != null)
		            rs.close();
		        if (prepStmt != null)
		            prepStmt.close();
		        if (con != null)
		            con.close();
		    }catch(Exception e){
		        
		    }
		}
		String[] arrPKs = new String[vecPKs.size()];
		vecPKs.copyInto(arrPKs);
		return arrPKs;
    }
    

    public CacheObjPackage[] loadMeasurePackObjPackages() throws SQLException {
        //�����ڱ����߶˴�ʱ��һ�μ�������ID���ǱȽ��������Ը�Ϊһ��ʼʲôҲ�����أ���Ҫʱֱ�Ӵ����ݿ��ѯ

        String strSQL = "select " + MEASURE_PACK_PK + "," + REPORT_PK + ","
                + KEY_GROUP_PK + "," + MEASURES + " from "
                + DatabaseNames.IUFO_MEASURE_TABLE;

        Connection con = null;
        ResultSet rs = null;
        CrossDBPreparedStatement prepStmt = null;
        MeasurePackVO mpvo = null;
        String strRepPK = null;
        Hashtable hashCacheObjs = new Hashtable();
        CacheObjPackage cacheObj = null;
        try {
            con = getConnection();
            prepStmt = (CrossDBPreparedStatement) con
                    .prepareStatement(strSQL);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                mpvo = genMeasurePackVO(rs);
                strRepPK = mpvo.getRepPK();
                cacheObj = (CacheObjPackage) hashCacheObjs.get(strRepPK);
                if (cacheObj == null) {
                    cacheObj = new CacheObjPackage(strRepPK);
                }
                cacheObj.addCacheObj(mpvo);
            }

        } catch (SQLException e) {
            throw e;

        } finally {  
          try{
            if (rs != null)
                rs.close();
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }catch(Exception e){
            
        }
        }

        CacheObjPackage[] arr = new CacheObjPackage[hashCacheObjs.size()];
        hashCacheObjs.values().toArray(arr);
        return arr;

    }

    //    	String selectSql = "select code,rep_id from " + DatabaseNames.IUFO_MEASURE_TABLE;
    //    	Connection con = null;
    //    	Statement stat = null;
    //    	ResultSet rs = null;
    //    	try{
    //    		con = getConnection();
    //    		stat = con.createStatement();
    //    		rs = stat.executeQuery(selectSql);
    //    		Hashtable table = new Hashtable();
    //    		while(rs.next()){
    //    			String code = rs.getString(1);
    //    			String repId = rs.getString(2);
    //    			if(code != null && repId != null){
    //	    			CacheObjPackage pack = (CacheObjPackage)table.get(repId);
    //	    			if(pack == null){
    //	    				pack = new CacheObjPackage(repId);
    //	    				table.put(repId, pack);
    //	    			}
    //	    			pack.addCacheObjID(code);
    //    			}
    //    		}
    //    		if(table.size() > 0){
    //    			CacheObjPackage[] result = new CacheObjPackage[table.size()];
    //    			Enumeration keys = table.keys();
    //    			int k=0;
    //    			while(keys.hasMoreElements()){
    //    				result[k] = (CacheObjPackage)table.get(keys.nextElement());
    //    				k++;
    //    			}
    //    			return result;
    //    		}
    //    	}finally{
    //    		if(rs != null)
    //    			rs.close();
    //    		if(stat != null)
    //    			stat.close();
    //    		if(con != null)
    //    			con.close();
    //    	}
    //
    //    }

}