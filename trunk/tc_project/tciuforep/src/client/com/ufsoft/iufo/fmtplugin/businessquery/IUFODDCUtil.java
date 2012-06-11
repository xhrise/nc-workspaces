package com.ufsoft.iufo.fmtplugin.businessquery;
import java.util.Hashtable;
import java.util.Vector;

import nc.itf.iufo.exproperty.IExPropConstants;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.RepFormatModelCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.ReportDirCache;
import nc.ui.iufo.exproperty.ExPropException;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.ui.iufo.resmng.OwnerDirRefBO_Client;
import nc.ui.ml.NCLangRes;
import nc.util.iufo.iufo.resmng.IIUFOResMngConsants;
import nc.vo.com.utils.UUID;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.pub.IDatabaseNames;
import nc.vo.iufo.resmng.OwnerDirRefVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.iuforeport.rep.ReportDirVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.ml.Language;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.core.FolderNode;
import nc.vo.pub.core.FolderObject;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.ddc.datadict.DatadictNode;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.TableDef;
import nc.vo.pub.querymodel.DataDictForNode;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dataprocess.extend.AreaDataSetUtil;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.table.CellsModel;

public class IUFODDCUtil {
	private static final String STR_REPDATA_MAIN_TABLE = "miufoiufoddc001";// miufoiufoddc001,������������

	private static final String STR_REPDATA_FIX_TABLE = "miufoiufoddc002";// miufoiufoddc002,����̶����ݱ�

	private static final String STR_REPDATA_DYN_TABLE = "miufoiufoddc003";// miufoiufoddc003,����̬���ݱ�

	private static final String STR_REPDATA_DYN_MAIN_TABLE = "miufoiufoddc004";// miufoiufoddc004,����̬��������

	private static final String STR_KEYWORD = "miufoiufoddc005";// miufoiufoddc005,�ؼ���

	// ָ������������������
	public static final String TABLE_NAME_MEASURE_PUBDATA = IDatabaseNames.IUFO_MEASURE_PUBDATA;

	public static final String TABLE_ALONEID_FIELD_NAME = "alone_id";

	public static final String TABLE_FILED_NAME_KEYWORD = "keyword";

	public static final String TABLE_FILED_NAME_DYNKEYWORD = "keyvalue";

	private static final String STR_MAINTABLE_MAINPRIM = "miufoiufoddc006";// miufoiufoddc006,��������
	private static final String STR_MAINPRIM = "miufoiufoddc007";// miufoiufoddc007,����
	private static final String STR_TIME = "miufoiufoddc008";// miufoiufoddc008,ʱ��
	private static final String STR_UNIT = "miufoiufoddc009";// miufoiufoddc009,��λ
	
	private static final String STR_VER = "miufoiufoddc010";// miufoiufoddc010,�汾
	private static final String STR_TASK = "miufoiufoddc011";// miufoiufoddc011,��������
	private static final String STR_UNITINFO = "miufoiufoddc012";// miufoiufoddc012,��λ��Ϣ
	private static final String STR_UNITNAME = "miufoiufoddc013";// miufoiufoddc013,��λ����
	private static final String STR_UNITCODE = "miufoiufoddc014";// miufoiufoddc014,��λ����
	private static final String STR_PUBLIC_REPORT  = "miufoiufoddc015";// miufoiufoddc015,���б���
	private static final String STR_TASK_INFO = "miufoiufoddc016";// miufoiufoddc016,������Ϣ
	private static final String STR_TASK_PK = "miufoiufoddc017";// miufoiufoddc017,��������
	private static final String STR_TASK_NAME = "miufoiufoddc018";// miufoiufoddc018,��������
	private static final String STR_UNITPK = "miufoiufoddc019";// miufoiufoddc019,��λ����
																
	public static final int TABLE_TYPE_NULL = -1;

	public static final int TABLE_TYPE_MAIN = 0;

	public static final int TABLE_TYPE_FIX = 1;

	public static final int TABLE_TYPE_DYN = 2;

	// Suppresses default constructor, ensuring non-instantiability.
	private IUFODDCUtil() {
	}

	/**
	 * ����ĳ��̬��������йؼ��֣���ȥ���б���Ĺؼ��ֲ��֣��� ����ö�̬����Ĺ��йؼ��ֺ�˽�йؼ���ʸ���� �������ڣ�(2003-10-27
	 * 14:04:10)
	 * 
	 * @author������Ƽ
	 * @param vecDynAllKeyVOs
	 *            java.util.Vector
	 * @param vecDynPubKeyVOs
	 *            java.util.Vector
	 * @param vecDynPriKeyVOs
	 *            java.util.Vector
	 */
	private static void calDynPubAndPriKeyVOs(Vector vecDynAllKeyVOs,
			Vector vecDynPubKeyVOs, Vector vecDynPriKeyVOs) {
		int iSize = vecDynAllKeyVOs.size();
		for (int i = 0; i < iSize; i++) {
			KeyVO keyVO = (KeyVO) vecDynAllKeyVOs.get(i);
			if (keyVO.isPrivate()) {
				vecDynPriKeyVOs.add(keyVO);
			} else {
				vecDynPubKeyVOs.add(keyVO);
			}
		}

	}

	/**
	 * ����ĳ���������ָ�꣺����ָ�������������֯���ݡ�
	 * 
	 * �������ڣ�(2003-9-18 19:13:14)
	 * 
	 * @author������Ƽ
	 * @param vecObjectNodes
	 *            java.util.Vector - ������ָ��ڵ������
	 * @param repVO
	 *            ReportVO - ����vo
	 * @param strParentGUID
	 *            java.lang.String - ���ڵ�GUID
	 * @param strReportNodeID
	 *            ���ڵ�ID
	 */
	private static void createAllMeasNodes(Vector vecObjectNodes,
			ReportVO repVO, String strParentGUID,String strReportNodeID) {

		String strRepPK = repVO.getReportPK();
		String strMainKeyCombPK = repVO.getKeyCombPK();
		if (strMainKeyCombPK == null) {
			// ���δ���ı���
			return;
		}

		// #���������������ʾ�����ı��(dbTableName,DataDictNode)
		Hashtable hashDbTables = new Hashtable();
		// #�������ĸñ���ָ���ڵ�Ĵ洢Vector
		Vector vecRepDataDictNodes = new Vector();

		// #������������ڵ�
		// �õ�����ؼ��ּ���
		KeyGroupCache keyGroupCache = CacheProxy.getSingleton()
				.getKeyGroupCache();
		KeyGroupVO mainKeyGroupVO = keyGroupCache.getByPK(strMainKeyCombPK);
		KeyVO[] mainKeyVOs = mainKeyGroupVO != null ? mainKeyGroupVO.getKeys()
				: null;
		Hashtable hashMainKey = getHashKeyWordPKs(mainKeyVOs);
		// ������������ڵ㼰������������ؼ����ֶ�
		String strRepCode = repVO.getCode();
		DatadictNode mainTableNode = createMainTableNode(strParentGUID,strReportNodeID,repVO
				.getReportPK()
				+ "pub", strRepCode, mainKeyVOs);
		// ��ά��hash���� �ʹ洢Vector
		hashDbTables.put(getStrRepDataMainTable(), mainTableNode);
		vecRepDataDictNodes.add(mainTableNode);

		// #��������̶�/��̬�����ӱ�
		// �õ����������ָ��
		ReportCache repCache = CacheProxy.getSingleton().getReportCache();
		MeasureCache measCache = CacheProxy.getSingleton()
				.getMeasureCache();
		String[] strRepMeasPKs = repCache.getMeasurePKs(strRepPK);
		MeasureVO[] measVOs = measCache.getMeasures(strRepMeasPKs);
		int iLen = measVOs != null ? measVOs.length : 0;
		int iFixCount = 1;
		int iDynCount = 1;
		String strRepDbTableName = null;
		DatadictNode tableNode = null;
		// �õ�����ĸ�ʽģ��
		RepFormatModelCache formatCache = CacheProxy.getSingleton()
				.getRepFormatCache();
		CellsModel tableFormatModel = formatCache
				.getUfoTableFormatModel(repVO.getReportPK());
		for (int i = 0; i < iLen; i++) {
			if (measVOs[i] == null || measVOs[i].getDbtable() == null
					|| measVOs[i].getDbcolumn() == null) {
				// ���δ���ı����ʽ
				return;
			}
			String strDbTableName = measVOs[i].getDbtable();
			// �õ���ά���̶���/��̬����ı����
			if (hashDbTables.containsKey(strDbTableName)) {
				// �õ���ڵ�
				tableNode = (DatadictNode) hashDbTables.get(strDbTableName);
			} else {
				// ׼��strDisplayName�ͱ�����nTableType
				String strCurKeyCombPK = measCache.getKeyCombPk(measVOs[i]
						.getCode());
				// repCache.getKeyCombByRepMea(strRepPK, measVOs[i].getCode());
				int nTableType = TABLE_TYPE_FIX;
				if (strMainKeyCombPK.equals(strCurKeyCombPK)) {
					strRepDbTableName = iFixCount + "_" + strRepCode + "-"
							+ getStrRepDataFixTable();
					iFixCount++;
				} else {
					strRepDbTableName = iDynCount + "_" + strRepCode + "-"
							+ getStrRepDataDynTable();
					nTableType = TABLE_TYPE_DYN;
					iDynCount++;
				}
				// ������ڵ��"����"�ڵ�,��ά��hash����
				String strDynAreaPK = "";
				String strIdentityPKSource = "";
				// �̶����ݱ�
				if (nTableType == TABLE_TYPE_FIX) {
					strIdentityPKSource = repVO.getReportPK()
							+ strDbTableName.substring(18);
				}
				// ��̬���ݱ�
				else if (nTableType == TABLE_TYPE_DYN) { // �õ�������̬�����PK
					KeywordModel keywordModel = KeywordModel.getInstance(tableFormatModel);
					strDynAreaPK = keywordModel.getDynAreaPK(strCurKeyCombPK);
					strIdentityPKSource = strDynAreaPK
							+ strDbTableName.substring(18);
				}

				tableNode = createTableNode(strParentGUID,strReportNodeID,
						getIdentityPK(strIdentityPKSource), strDbTableName,
						strRepDbTableName, nTableType);
				hashDbTables.put(strDbTableName, tableNode);
				vecRepDataDictNodes.add(tableNode);

				// ����Ƕ�̬����ı�ڵ㣬�򴴽���ؼ�������ֶ�
				// ��Ҫ�����С�˽�У�
				// 1)����й��йؼ��֣���Ҫ�����ö�̬����Ĺ��йؼ����ӱ�,������Ӧ�Ĺ��йؼ����ֶ�
				// 2)˽�йؼ�����ֱ�ӷ��ڶ�̬�����ڵ�����Ϊ���ֶ�
				if (nTableType == TABLE_TYPE_DYN) {
					KeyGroupVO dynKeyGroupVO = keyGroupCache
							.getByPK(strCurKeyCombPK);
					KeyVO[] dynKeyVOs = dynKeyGroupVO != null ? dynKeyGroupVO
							.getKeys() : null;
					Vector vecDynAllKeyVOs = getFilterVecKeyWordPKs(
							hashMainKey, dynKeyVOs);
					Vector vecDynPubKeyVOs = new Vector();
					Vector vecDynPriKeyVOs = new Vector();
					calDynPubAndPriKeyVOs(vecDynAllKeyVOs, vecDynPubKeyVOs,
							vecDynPriKeyVOs);
					int iDynPubKeySize = vecDynPubKeyVOs.size();
					if (iDynPubKeySize > 0) {
						// ������̬���������ڵ�͹��йؼ����ֶ�(ͬһ��̬��������ж�������)
						// iufo_measure_data_*,18
						DatadictNode dynMainTableNode = createTableNode(
								strParentGUID,strReportNodeID,
								getIdentityPK(strDynAreaPK + "pub"
										+ strDbTableName.substring(18)),
								TABLE_NAME_MEASURE_PUBDATA, new Integer(
										iDynCount - 1).intValue()
										+ "_"
										+ strRepCode
										+ "-"
										+ getStrRepDataDynMainTable(),
								TABLE_TYPE_MAIN);
						vecRepDataDictNodes.add(dynMainTableNode);
						int iMainKeyVOLen = mainKeyVOs != null ? mainKeyVOs.length
								: 0;
						for (int k = 0; k < iDynPubKeySize; k++) {
							String strDisplayName = getFieldDisPlayName((KeyVO) vecDynPubKeyVOs
									.get(k));
							// �����ؼ��ֶ�Ӧ�ı��ֶ�
							FieldDef fieldDef = createTableField(
									TABLE_FILED_NAME_KEYWORD
											+ (k + 1 + iMainKeyVOLen),
									strDisplayName, MeasureVO.TYPE_CHAR);
							((TableDef) dynMainTableNode.getObject())
									.getFieldDefs().addFieldDef(fieldDef);
						}
					}
					if (vecDynPriKeyVOs.size() > 0) {
						createDynKeyFields(tableNode, vecDynPriKeyVOs);
					}

				}
			}

			// ����ָ��ڵ�,������ӵ���ڵ���
			FieldDef measField = createMeasField(measVOs[i]);
			((TableDef) tableNode.getObject()).getFieldDefs().addFieldDef(
					measField);
		}

		// ��������ָ���ڵ�ļӵ�Vector����
		int iSize = vecRepDataDictNodes.size();
		for (int i = 0; i < iSize; i++) {
			DatadictNode node = (DatadictNode) vecRepDataDictNodes.get(i);
			vecObjectNodes.add(node);
		}

		// int j = 0;

	}

	/**
	 * ������Ŀ¼dir_id�µ����б���ڵ㡣
	 * 
	 * �������ڣ�(2003-9-18 16:21:06)
	 * 
	 * @author������Ƽ
	 * @param vecObjectNodes
	 *            java.util.Vector
	 * @param strDirId
	 *            java.lang.String
	 * @param strParentGUID
	 *            java.lang.String - ��ǰ���ڵ�GUID
	 */
	private static void createAllReportNodes(Vector vecObjectNodes,
			String strDirId, String strParentGUID, boolean bLoadAll) {

		// �õ���Ŀ¼dir_id�µ����б���
		nc.pub.iufo.cache.ReportCache repCache = CacheProxy.getSingleton().getReportCache();

		ReportVO[] reportVOSubs = repCache.getReportsByDirPK(strDirId);
		// ReportVO[] reportVOSubs = ReportTreeUI.getReportsById(strDirId);

		if (reportVOSubs != null) {
			for (int i = 0; i < reportVOSubs.length; i++) {
				FolderNode subRepNode = new FolderNode();
				subRepNode.setID(reportVOSubs[i].getReportPK());
				subRepNode.setDisplayName("_" + reportVOSubs[i].getName());
				subRepNode.setGUID(new UUID().toString());
// subRepNode.setGUID(reportVOSubs[i].getReportPK());

				FolderObject folderObject = new FolderObject();
				folderObject.setNode(subRepNode);

				subRepNode.setObject(folderObject);
				subRepNode.setParentGUID(strParentGUID);
				subRepNode.setKind(FolderNode.Kind);

				vecObjectNodes.add(subRepNode);

				// ��ӱ����µ��������ݱ�
				if (bLoadAll)
					createAllMeasNodes(vecObjectNodes, reportVOSubs[i], subRepNode.getGUID(),subRepNode.getID());
			}
		}
	}

	/**
	 * ����������̬�ؼ��ֵĶ�Ӧ�ֶε�ָ���ı�ڵ��
	 * 
	 * �������ڣ�(2003-9-20 14:57:54)
	 * 
	 * @author������Ƽ
	 * @param tableNode
	 *            nc.vo.pub.ddc.datadict.DatadictNode - ��̬���ݱ�ڵ�
	 * @param vecDynKeyVOs
	 *            java.util.Vector - ���ն���˳����õĶ�̬�ؼ���ʸ��
	 */
	private static void createDynKeyFields(DatadictNode tableNode,
			Vector vecDynKeyVOs) {

		int iDynKeyLen = vecDynKeyVOs.size();
		for (int k = 0; k < iDynKeyLen; k++) {
			KeyVO dynKeyVO = (KeyVO) vecDynKeyVOs.get(k);
			String strDisplayName = getFieldDisPlayName(dynKeyVO);
			FieldDef dynKeyField = createTableField(TABLE_FILED_NAME_DYNKEYWORD
					+ (k + 1), getStrKeyword() + "_" + strDisplayName,
					MeasureVO.TYPE_CHAR);
			((TableDef) tableNode.getObject()).getFieldDefs().addFieldDef(
					dynKeyField);
		}
	}

	/**
	 * ������������ڵ㼰������������ؼ����ֶΡ�
	 * 
	 * �������ڣ�(2003-9-20 13:56:11)
	 * 
	 * @author������Ƽ
	 * @return nc.vo.pub.ddc.datadict.DatadictNode
	 * @param strParentGUID
	 *            java.lang.String - ����ڵ�guid
	 * @param strRepPK
	 *            String
	 * @param strRepCode
	 *            java.lang.String - �������
	 * @param mainKeyVOs
	 *            nc.vo.iufo.keydef.KeyVO[] - ����Ĺ̶��ؼ��ּ���
	 */
	private static DatadictNode createMainTableNode(String strParentGUID,String strReportNodeID,
			String strRepPK, String strRepCode, KeyVO[] mainKeyVOs) {
		// ������ڵ�͡������������ֶ�
		DatadictNode mainTableNode = createTableNode(strParentGUID,strReportNodeID,
				getIdentityPK(strRepPK), TABLE_NAME_MEASURE_PUBDATA, strRepCode
						+ "-" + getStrRepDataMainTable(), TABLE_TYPE_MAIN);

		// �����̶�����Ĺؼ��֣�ֻ���й��еģ��ֶ�
		int iMainKeyLen = mainKeyVOs != null ? mainKeyVOs.length : 0;
		if (mainTableNode != null && iMainKeyLen > 0) {
			for (int i = 0; i < iMainKeyLen; i++) {
				String strDisplayName = getFieldDisPlayName(mainKeyVOs[i]);
				// �����ؼ��ֶ�Ӧ�ı��ֶ�
				FieldDef fieldDef = createTableField(TABLE_FILED_NAME_KEYWORD
						+ (i + 1), strDisplayName, MeasureVO.TYPE_CHAR);
				((TableDef) mainTableNode.getObject()).getFieldDefs()
						.addFieldDef(fieldDef);
			}
		}
		// �����ֶΡ��汾(ver)��
		FieldDef verFieldDef = createTableField("ver", getStrVer(),
				MeasureVO.TYPE_NUMBER);// Variant.BIGDECIMAL);
		((TableDef) mainTableNode.getObject()).getFieldDefs().addFieldDef(
				verFieldDef);
		// �ϲ�����ǰ����񱣴������������Ϣ��
		if(isHBvsTask()){
			// �����ֶΡ���������(formula_id)��
			FieldDef formulaIdFieldDef = createTableField("formula_id", getStrTask(),
					MeasureVO.TYPE_CHAR);;
			((TableDef) mainTableNode.getObject()).getFieldDefs().addFieldDef(
					formulaIdFieldDef);			
		}
		return mainTableNode;
	}

	/**
	 * ����ָ��FieldDef�� �������ڣ�(2003-9-18 21:05:40)
	 * 
	 * @author������Ƽ
	 * @return nc.vo.pub.ddc.datadict.FieldDef
	 * @param measVO
	 *            nc.vo.iufo.measure.MeasureVO
	 */
	private static FieldDef createMeasField(MeasureVO measVO) {
		// System.out.println("[IUFODDCUtilָ���ֶ�����]:Dbcolumn=" +
		// measVO.getDbcolumn()+",sql Type=
		// "+AreaDataSetUtil.getSqlTypeFromMeas(measVO.getType()));
		return createTableField(measVO.getDbcolumn(), measVO.getName(), measVO
				.getType());

	}

	/**
	 * ����nTableType��������������������ָ������������� �������ָ�����ͣ��򷵻�null
	 * 
	 * �������ڣ�(2003-9-20 10:25:15)
	 * 
	 * @author������Ƽ
	 * @return nc.vo.pub.ddc.datadict.FieldDef
	 * @param nTableType
	 *            int
	 */
	private static FieldDef createPrimField(int nTableType) {
		String strPrimNodeName = null;
		if (nTableType == TABLE_TYPE_MAIN) {
			strPrimNodeName = getStrMainTableMainPrim();
		} else if (nTableType == TABLE_TYPE_FIX || nTableType == TABLE_TYPE_DYN) {
			strPrimNodeName = getStrMainPrim();
		} else {
			return null;
		}

		return createTableField(TABLE_ALONEID_FIELD_NAME, strPrimNodeName,
				MeasureVO.TYPE_CHAR);

	}

	/**
	 * ��������Ŀ¼�ڵ㡣
	 * 
	 * �������ڣ�(2003-9-18 16:07:37)
	 * 
	 * @author������Ƽ
	 * @param vecObjectNode
	 *            java.util.Vector -���ɵĽڵ�װ�������
	 * @param rDirVO
	 *            nc.vo.iuforeport.rep.ReportDirVO - ��ǰ������Ŀ¼
	 * @param strParentGUID
	 *            java.lang.String - ��ǰ���ڵ�GUID
	 * @param haveLeaf
	 *            boolean - �Ƿ����ӽڵ�
	 */
	private static void createReportDirNodes(Vector vecObjectNode,
			ReportDirVO rDirVO, String strParentGUID, boolean haveLeaf, boolean bLoadAll) {
		// 1����Ŀ¼
		Vector vecSubDirVOs = rDirVO.getSubDirs();
		if (vecSubDirVOs != null) {

			// ����Ŀ¼��
			for (int i = 0; i < vecSubDirVOs.size(); i++) {
				ReportDirVO subDirVO = (ReportDirVO) vecSubDirVOs.get(i);
				// �ݹ������Ŀ¼
				if (haveLeaf || subDirVO.getSubDirs() != null) {
					if (!(subDirVO.getDirId()
							.equals((ReportDirVO.REPORTDIR_PRIVATE_DIR_ID)))) {
						FolderNode subNode = new FolderNode();
						subNode.setID(subDirVO.getDirId());
						String strDisplayName = subDirVO.getDirName();
						// ����ǵ�λ��Ŀ¼��������λ��Ϣ��2005��03��03��liulp,from wss
						if(subDirVO.getParentId().equals(ReportDirVO.REPORTDIR_ROOT_DIR_ID)){
							String strModuleName = IIUFOResMngConsants.MODULE_REPORT_DIR;
							OwnerDirRefVO ownerDirRefVO = null;
							try {
								ownerDirRefVO = OwnerDirRefBO_Client.getRefByDirPK(strModuleName,subDirVO.getDirId());
							} catch (Exception e) {
								AppDebug.debug(e);// @devTools
													// AppDebug.debug(e);
							}
							if(ownerDirRefVO!=null){
								UnitInfoVO unitInfoVO = CacheProxy.getSingleton().getUnitCache().getUnitInfoByPK(ownerDirRefVO.getOwnerPK());
// �����λ�����ڣ���û�е�λ��Ŀ¼
								// added by liulp,2005-06-03,��������ܳ��ָ�����
								if(unitInfoVO == null){
									continue;
								}
								strDisplayName = unitInfoVO.getUnitName()+"("+unitInfoVO.getCode()+")"+ strDisplayName;
							}
						}
						subNode.setDisplayName(strDisplayName);
						
						subNode.setGUID(new UUID().toString());
						FolderObject folderObject = new FolderObject();
						folderObject.setNode(subNode);
						subNode.setObject(folderObject);
						subNode.setParentGUID(strParentGUID);
						subNode.setKind(FolderNode.Kind);

						vecObjectNode.add(subNode);

						createReportDirNodes(vecObjectNode, subDirVO, subNode
								.getGUID(), haveLeaf, bLoadAll);
					}
				}

			}

		}

		// 2���ñ���Ŀ¼�µ����б���ڵ�
		String strDirId = rDirVO.getDirId();
		createAllReportNodes(vecObjectNode, strDirId, strParentGUID, bLoadAll);

	}

	/**
	 * ������ڵ��һ���ֶΡ� �������ڣ�(2003-9-20 13:10:45)
	 * 
	 * @author������Ƽ
	 * @return nc.vo.pub.ddc.datadict.FieldDef
	 * @param strDBColumn
	 *            java.lang.String
	 * @param strDisplayName
	 *            java.lang.String
	 * @param nColType
	 *            int ��Ӧָ��VO����������
	 */
	private static FieldDef createTableField(String strDBColumn,
			String strDisplayName, int nColType) {

		FieldDef fieldDef = new FieldDef();

		fieldDef.setID(strDBColumn);
		fieldDef.setDisplayName(strDisplayName);
		fieldDef.setGUID(new UUID().toString());
		fieldDef.setDataType(AreaDataSetUtil.getSqlTypeFromMeas(nColType));

		return fieldDef;
	}

	/**
	 * �������������ֶε�һ����ڵ㡣
	 * 
	 * �������ڣ�(2003-9-20 10:48:03)
	 * 
	 * @author������Ƽ
	 * @return nc.vo.pub.ddc.datadict.DatadictNode
	 * @param strParentGUID
	 *            java.lang.String
	 * @param strNodeID
	 *            �ڵ�ID
	 * @param strIdentityPK
	 *            java.lang.String
	 * @param strDbName
	 *            java.lang.String
	 * @param strDisplayName
	 *            java.lang.String
	 * @param nTableType
	 *            int
	 */
	private static DatadictNode createTableNode(String strParentGUID,String strNodeID,
			String strIdentityPK, String strDbName, String strDisplayName,
			int nTableType) {

		DatadictNode tableNode = new DatadictNode();
		String strGUID = new UUID().toString();
		// tableNode.setID("T_"+strGUID); //ΨһID
		// tableNode.setID("T_" + strRepPK + strDbName);
		tableNode.setID("T" + strIdentityPK);
		tableNode.setDisplayName(strDisplayName);
		tableNode.setGUID(strGUID);
		// tableNode.setRealName(strDbName); //dbName

		nc.vo.iuforeport.businessquery.TableDefWithAlias tableDef = new nc.vo.iuforeport.businessquery.TableDefWithAlias();
		tableDef.setNode(tableNode);
		tableDef.setRealName(strDbName);
		tableDef.setNote(strNodeID);

		tableNode.setObject(tableDef);
		tableNode.setParentGUID(strParentGUID);
		tableNode.setKind(DatadictNode.TableKind);

		// �������"(����)����"�ڵ�
		FieldDef primField = createPrimField(nTableType);
		if (primField != null) {
			tableDef.getFieldDefs().addFieldDef(primField);
		}

		return tableNode;
	}

	/**
	 * �õ��ؼ����ֶε���ʾ���ơ� �������ڣ�(2003-9-20 15:14:03)
	 * 
	 * @author������Ƽ
	 * @return java.lang.String
	 * @param keyVO
	 *            nc.vo.iufo.keydef.KeyVO
	 */
	private static String getFieldDisPlayName(KeyVO keyVO) {

		String strDisplayName = keyVO.getName();
		if (keyVO.getType() == KeyVO.TYPE_TIME) {
			strDisplayName = getStrTime() + "_" + keyVO.getName();
		} else if (keyVO.getKeywordPK().equals(KeyVO.CORP_PK)) {
			strDisplayName = getStrUnit();
		}

		return strDisplayName;
	}

	/**
	 * �õ�������ָ��hashToFilt�йؼ��ֵĹؼ���Vector,Ҫ��˳�򲻱��ı䡣
	 * 
	 * �������ڣ�(2003-9-20 14:32:50)
	 * 
	 * @author������Ƽ
	 * @return java.util.Vector
	 * @param hashToFilt
	 *            java.util.Hashtable
	 * @param keyVOs
	 *            nc.vo.iufo.keydef.KeyVO[]
	 */
	private static Vector getFilterVecKeyWordPKs(Hashtable hashToFilt,
			KeyVO[] keyVOs) {
		Vector vecDynKeyVOs = new Vector();

		int iLen = keyVOs != null ? keyVOs.length : 0;
		for (int i = 0; i < iLen; i++) {
			if (!hashToFilt.containsKey(keyVOs[i].getKeywordPK())) {
				vecDynKeyVOs.add(keyVOs[i]);
			}
		}

		return vecDynKeyVOs;
	}

	/**
	 * �õ�ָ���ؼ��ּ��ϵ�KeyWordPK��Hash��
	 * 
	 * �������ڣ�(2003-9-20 14:30:04)
	 * 
	 * @author������Ƽ
	 * @return java.util.Hashtable
	 * @param keyVOs
	 *            nc.vo.iufo.keydef.KeyVO[]
	 */
	private static Hashtable getHashKeyWordPKs(KeyVO[] keyVOs) {
		Hashtable hashKeyWordPKs = new Hashtable();
		int iMainKeyLen = keyVOs != null ? keyVOs.length : 0;
		for (int i = 0; i < iMainKeyLen; i++) {
			String strKeywordPK = keyVOs[i].getKeywordPK();
			hashKeyWordPKs.put(strKeywordPK, strKeywordPK);
		}

		return hashKeyWordPKs;
	}

	/**
	 * �õ���Ӧָ���ַ����Ķ̳���ΨһPK��
	 * 
	 * �������ڣ�(2003-10-29 10:36:23)
	 * 
	 * @author������Ƽ
	 * @return java.lang.String
	 * @param strSource
	 *            java.lang.String
	 */
	private static String getIdentityPK(String strSource) {
		String strReturn = new Integer(strSource.hashCode()).toString();
		if (strReturn.indexOf("-") >= 0) {
			strReturn = strReturn.replace('-', 'N');
		}
		return strReturn;
	}

	/**
	 * �õ�DDC����Ҫ��IUFOָ��ObjectNodes��
	 * 
	 * �������ڣ�(2003-9-18 14:23:07)
	 * 
	 * @author������Ƽ
	 * @return nc.vo.pub.core.ObjectNode[]
	 */
	public static nc.vo.pub.core.ObjectNode[] getMeasObjectNodes(Boolean bLoadAll) {
		// ��������ֵ
		Language curLang  = NCLangRes.getInstance().getCurrLanguage();
		if(curLang == null){
			String strLangCode = MultiLangUtil.getDefaultLangCode();
			curLang = NCLangRes.getInstance().getLanguage(strLangCode);
			NCLangRes.getInstance().setCurrLanguage(curLang);				
		}
		String strLangCode = curLang.getCode();
		AppDebug.debug("[debug IUFODDCUtil.java] curLangCode = " + strLangCode);// @devTools
																				// System.out.println("[debug
																				// IUFODDCUtil.java]
																				// curLangCode
																				// = "
																				// +
																				// strLangCode);
		MultiLangUtil.saveLanguage(strLangCode,true);
		
		ReportDirCache repDirCache = CacheProxy.getSingleton()
				.getReportDirCache();

// System.out.println("enter IUFO code: bLoadAll=" + bLoadAll);
		// ��ù��б���Ľڵ�����:
		// 1��Ŀ¼�ڵ㣺FolderNode��FolderObject
		// ID��displayName,GUID,parentGUID��FolderObject
		// 2����ڵ㣺DatadictNode��TableDef
		// 3��������ֶζ���FieldDefList.addField(FieldDef)
		ReportDirVO pubRepDirVO = repDirCache.getRootReportDir(false);
		if (pubRepDirVO != null) {
			Vector vecObjectNode = new Vector();
			// ���ӵ�λ��Ϣ���������ֵ���ڵ���,added by liulp 2004-05-08(from zzj)
			addUnitInfoNode(vecObjectNode);
			// ����������Ϣ���������ֵ���ڵ���,added by liulp 2005-03-03(from wss)
			addTaskInfoNode(vecObjectNode);
			// ����IUFO���б���Ŀ¼�ڵ㼰�����б���
			ObjectNode pubRepRootNode = createRepRootNode(vecObjectNode,
					pubRepDirVO.getDirId());
			vecObjectNode.add(pubRepRootNode);
			// �������еĹ��б���ڵ�
			createReportDirNodes(vecObjectNode, pubRepDirVO, pubRepRootNode
					.getGUID(), true, bLoadAll.booleanValue());
			// װ��ΪObjectNode����
			int iLen = vecObjectNode.size();
			ObjectNode[] nodes = new ObjectNode[iLen];
			vecObjectNode.copyInto(nodes);

			return nodes;
		}
		return null;
	}

	/**
	 * ����������Ϣ��ڵ㵽�������ֵ䡱��Ŀ¼�£� IUFO������Ϣ�����ֶΣ���λ���ƣ���λ����
	 * 
	 * @param vecObjectNode
	 */
	private static void addUnitInfoNode(Vector vecObjectNode) {
		String strIdentidyKey = "iufounitinfo";
		String strTableName = IDatabaseNames.IUFO_UNIT_INFO_TABLE;
		String strTableDisName = getStrUnitInfo();
		// ���������ӡ���λ��Ϣ����ڵ�
		DatadictNode unitInfoNode = createTableNode(DataDictForNode.ROOT_GUID,"-1",
				strIdentidyKey, strTableName, strTableDisName, TABLE_TYPE_NULL);
		vecObjectNode.add(unitInfoNode);
//		// ����������,"��λ����",����λ���ơ�������λ���롱�ֶνڵ�
//		String strFieldUnitPK = "unit_id";
//		String strDisUnitPK = getStrUnitPK();
//		String strFieldUnitName = "unit_name";
//		String strDisUnitName = getStrUnitName();;
//		String strFieldUnitCode = "unit_code";
//		String strDisUnitCode = getStrUnitCode();
//		FieldDef unitPKField = createTableField(strFieldUnitPK,
//				strDisUnitPK, MeasureVO.TYPE_CHAR);
//		FieldDef unitNameField = createTableField(strFieldUnitName,
//				strDisUnitName, MeasureVO.TYPE_CHAR);
//		FieldDef unitCodeField = createTableField(strFieldUnitCode,
//				strDisUnitCode, MeasureVO.TYPE_CHAR);
//		TableDef tableDef = (TableDef) unitInfoNode.getObject();
//		tableDef.getFieldDefs().addFieldDef(unitPKField);
//		tableDef.getFieldDefs().addFieldDef(unitNameField);
//		tableDef.getFieldDefs().addFieldDef(unitCodeField);

		TableDef tableDef = (TableDef) unitInfoNode.getObject();
		//IUFO5.1,���˵�λ����������λ���ơ�������λ���롱�ֶνڵ㣬����Ҫ���ӵ�λ��������Ϣ�������ϼ���λ��������λ�ṹ��Ϣ
		//��λ����
		String strFieldUnitPK = "unit_id";
		String strDisUnitPK = getStrUnitPK();
		FieldDef unitPKField = createTableField(strFieldUnitPK,
				strDisUnitPK, MeasureVO.TYPE_CHAR);
		tableDef.getFieldDefs().addFieldDef(unitPKField);
		//��λ���ƣ���λ���룬��λ���α����������λ�ṹ
		IExPropOperator unitExpropOperator = ExPropOperator.getExPropOper(IExPropConstants.EXPROP_MODULE_UNIT);
		try {
			ExPropertyVO[]  exPropVOs = unitExpropOperator.loadAllExProp("");
			if(exPropVOs!= null){
				int iUnitPropCount = exPropVOs.length;
				FieldDef curField = null;
				String strFieldDBColumn = null;
				String strDisFieldName = null;
				for(int i =0;i < iUnitPropCount;i++){
					strFieldDBColumn = exPropVOs[i].getDBColumnName();
					strDisFieldName = exPropVOs[i].getName();
					curField = createTableField(strFieldDBColumn,
							strDisFieldName, MeasureVO.TYPE_CHAR);
					tableDef.getFieldDefs().addFieldDef(curField);
				}
			}
		} catch (ExPropException e) {
			AppDebug.debug(e);
		}
		
	}

	/**
	 * ���������б���Ŀ¼�ڵ�
	 * 
	 * @param vecObjectNode
	 *            Vector
	 * @param strID
	 *            String
	 * @return ObjectNode
	 */
	private static ObjectNode createRepRootNode(Vector vecObjectNode,
			String strID) {
		String strRootName = getStrPublicReport();

		ObjectNode pubRepRootNode = new FolderNode();
		pubRepRootNode.setID(strID);
		pubRepRootNode.setDisplayName(strRootName);
		pubRepRootNode.setGUID(new UUID().toString());

		FolderObject folderObject = new FolderObject();
		folderObject.setNode(pubRepRootNode);
		pubRepRootNode.setKind(FolderNode.Kind);

		pubRepRootNode.setObject(folderObject);
		pubRepRootNode.setParentGUID(DataDictForNode.ROOT_GUID);

		return pubRepRootNode;
	}

	/**
	 * ������λ��Ϣ��ڵ㵽�������ֵ䡱��Ŀ¼�£� IUFO��λ��Ϣ�����ֶΣ���λ���ƣ���λ����
	 * 
	 * @param vecObjectNode
	 *            Vector
	 */
	private static void addTaskInfoNode(Vector vecObjectNode) {
		// �ϲ�����ǰ����񱣴������������Ϣ��
		if(!isHBvsTask()){
			return;
		}
			
		String strIdentidyKey = "iufotaskinfo";
		String strTableName = IDatabaseNames.IUFO_TASK_TABLE;
		String strTableDisName = getStrTaskInfo();
		String strFieldTaskId = "id";
		String strDisTaskId = getStrTaskPK();
		String strFieldTaskName = "name";
		String strDisTaskName = getStrTaskName();
		// ���������ӡ���λ��Ϣ����ڵ�
		DatadictNode unitInfoNode = createTableNode(DataDictForNode.ROOT_GUID,"-1",
				strIdentidyKey, strTableName, strTableDisName, TABLE_TYPE_NULL);
		vecObjectNode.add(unitInfoNode);
		// ���������ӡ��������ơ����������������ֶνڵ�
		FieldDef taskNameField = createTableField(strFieldTaskName,
				strDisTaskName, MeasureVO.TYPE_CHAR);
		FieldDef unitTaskIdField = createTableField(strFieldTaskId,
				strDisTaskId, MeasureVO.TYPE_CHAR);
		TableDef tableDef = (TableDef) unitInfoNode.getObject();
		tableDef.getFieldDefs().addFieldDef(taskNameField);
		tableDef.getFieldDefs().addFieldDef(unitTaskIdField);
	}

	/**
	 * �ϲ�����Ƿ����񱣴�
	 * 
	 * @return true,�ǰ����񱣴�;false,�ǰ�
	 */
	private static boolean isHBvsTask() {
		SysPropVO prop = (SysPropVO)CacheProxy.getSingleton().getSysPropCache().get(ISysProp.HB_REPDATA_RELATING_TASK);
		if(prop!=null){
			if(prop.getValue().equalsIgnoreCase("true")){
				return true;
			}
		}
		return false;
	}

	/**
	 * ������Ŀ¼dir_id�µ����б���ڵ㡣
	 * 
	 * �������ڣ�(2003-9-18 16:21:06)
	 * 
	 * @author������Ƽ
	 * @param vecObjectNodes
	 *            java.util.Vector
	 * @param strDirId
	 *            java.lang.String
	 * @param strParentGUID
	 *            java.lang.String - ��ǰ���ڵ�GUID
	 */
	public static ObjectNode[] createMeasNodes(String strDirId,
			String strReportId, String strReportGuid,String strReportNodeID) {

		// �õ���Ŀ¼dir_id�µ����б���
		nc.pub.iufo.cache.ReportCache repCache = CacheProxy
				.getSingleton().getReportCache();

		ReportVO[] reportVOSubs = repCache.getReportsByDirPK(strDirId);

		Vector vecObjectNodes = new Vector();
		int iLen = (reportVOSubs == null) ? 0 : reportVOSubs.length;
		for (int i = 0; i < iLen; i++) {
			// ��Ӷ�Ӧ�����µ��������ݱ�
			if (reportVOSubs[i].getReportPK().equals(strReportId)) {
				createAllMeasNodes(vecObjectNodes, reportVOSubs[i], strReportGuid,strReportNodeID);
				break;
			}
		}

		ObjectNode[] objNodes = new ObjectNode[vecObjectNodes.size()];
		vecObjectNodes.copyInto(objNodes);
		return objNodes;
	}

	/**
	 * @return ���� STR_REPDATA_MAIN_TABLE��
	 */
	private static String getStrRepDataMainTable() {		;
		return getStrResoucre(STR_REPDATA_MAIN_TABLE);
	}

	/**
	 * ���ָ����Դ������ַ���ֵ
	 * 
	 * @param strResourceCode
	 * @return
	 */
	private static String getStrResoucre(String strResourceCode) {
		return StringResource.getStringResource(strResourceCode);
	}

	/**
	 * @return ���� sTR_REPDATA_FIX_TABLE��
	 */
	private static String getStrRepDataFixTable() {
		return getStrResoucre(STR_REPDATA_FIX_TABLE);
	}

	/**
	 * @return ���� sTR_REPDATA_DYN_TABLE��
	 */
	private static String getStrRepDataDynTable() {
		return getStrResoucre(STR_REPDATA_DYN_TABLE);
	}

	/**
	 * @return ���� sTR_REPDATA_DYN_MAIN_TABLE��
	 */
	private static String getStrRepDataDynMainTable() {
		return getStrResoucre(STR_REPDATA_DYN_MAIN_TABLE);
	}

	/**
	 * @return ���� sTR_KEYWORD��
	 */
	private static String getStrKeyword() {
		return getStrResoucre(STR_KEYWORD);
	}

	/**
	 * @return ���� sTR_MAINTABLE_MAINPRIM��
	 */
	private static String getStrMainTableMainPrim() {
		return getStrResoucre(STR_MAINTABLE_MAINPRIM);
	}

	/**
	 * @return ���� sTR_MAINPRIM��
	 */
	private static String getStrMainPrim() {
		return getStrResoucre(STR_MAINPRIM);
	}

	/**
	 * @return ���� sTR_TIME��
	 */
	private static String getStrTime() {
		return getStrResoucre(STR_TIME);
	}

	/**
	 * @return ���� sTR_UNIT��
	 */
	private static String getStrUnit() {
		return getStrResoucre(STR_UNIT);
	}

	/**
	 * @return ���� sTR_VER��
	 */
	private static String getStrVer() {
		return getStrResoucre(STR_VER);
	}

	/**
	 * @return ���� sTR_TASK��
	 */
	private static String getStrTask() {
		return getStrResoucre(STR_TASK);
	}

	/**
	 * @return ���� sTR_UNITINFO��
	 */
	private static String getStrUnitInfo() {
		return getStrResoucre(STR_UNITINFO);
	}

	/**
	 * @return ���� sTR_UNITNAME��
	 */
	private static String getStrUnitName() {
		return getStrResoucre(STR_UNITNAME);
	}

	/**
	 * @return ���� sTR_UNITCODE��
	 */
	private static String getStrUnitCode() {
		return getStrResoucre(STR_UNITCODE);
	}
	/**
	 * @return ���� sTR_UNITPK��
	 */
	private static String getStrUnitPK() {
		return getStrResoucre(STR_UNITPK);
	}

	/**
	 * @return ���� sTR_PUBLIC_REPORT��
	 */
	private static String getStrPublicReport() {
		return getStrResoucre(STR_PUBLIC_REPORT);
	}

	/**
	 * @return ���� sTR_TASK_INFO��
	 */
	private static String getStrTaskInfo() {
		return getStrResoucre(STR_TASK_INFO);
	}

	/**
	 * @return ���� sTR_TASK_PK��
	 */
	private static String getStrTaskPK() {
		return getStrResoucre(STR_TASK_PK);
	}

	/**
	 * @return ���� sTR_TASK_NAME��
	 */
	private static String getStrTaskName() {
		return getStrResoucre(STR_TASK_NAME);
	}

	/**
	 * ������Ŀ¼dir_id�µ����б���ڵ㡣
	 */
	public static ObjectNode[] createMeasNodes(String strDirId,
			String strReportId, String strReportGuid) {
		return createMeasNodes(strDirId, strReportId, strReportGuid, strReportId);
	}

	/**
	  * ���������ֵ�
	  */
	public static void appendDatadict(DataDictForNode treeIUFO, QueryBaseDef qbd) {
		if (treeIUFO.isIUFO()) {
		   FromTableVO[] fts = qbd.getFromTables();
		   int iLen = (fts == null) ? 0 : fts.length;
		   for (int i = 0; i < iLen; i++) {
			   String tableId = fts[i].getTablealias();
			   // ��ʱ����
			   if (QueryUtil.isTempTable(tableId)) {
				   continue;
			   }
			   // �������ֵ�������
			   BizObject td = treeIUFO.findObject(tableId);
			   if (td == null) {
				   // �ñ�δ�ҵ������ڷּ�������©Ʒ����Ҫ�ҵ�����������Ŀ¼ID
				   String strReportNodeId = fts[i].getNote();
				   // ������Ŀ¼�ڵ�ķּ������¼�
				   if (strReportNodeId != null) {
					   treeIUFO.getChidren(strReportNodeId, new Vector(), false);
				   }
			   }
		   }
		}
	}
}
