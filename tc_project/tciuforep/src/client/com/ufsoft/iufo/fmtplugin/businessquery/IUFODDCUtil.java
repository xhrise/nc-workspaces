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
	private static final String STR_REPDATA_MAIN_TABLE = "miufoiufoddc001";// miufoiufoddc001,报表数据主表

	private static final String STR_REPDATA_FIX_TABLE = "miufoiufoddc002";// miufoiufoddc002,报表固定数据表

	private static final String STR_REPDATA_DYN_TABLE = "miufoiufoddc003";// miufoiufoddc003,报表动态数据表

	private static final String STR_REPDATA_DYN_MAIN_TABLE = "miufoiufoddc004";// miufoiufoddc004,报表动态数据主表

	private static final String STR_KEYWORD = "miufoiufoddc005";// miufoiufoddc005,关键字

	// 指标数据主表的物理表名
	public static final String TABLE_NAME_MEASURE_PUBDATA = IDatabaseNames.IUFO_MEASURE_PUBDATA;

	public static final String TABLE_ALONEID_FIELD_NAME = "alone_id";

	public static final String TABLE_FILED_NAME_KEYWORD = "keyword";

	public static final String TABLE_FILED_NAME_DYNKEYWORD = "keyvalue";

	private static final String STR_MAINTABLE_MAINPRIM = "miufoiufoddc006";// miufoiufoddc006,主表主键
	private static final String STR_MAINPRIM = "miufoiufoddc007";// miufoiufoddc007,主键
	private static final String STR_TIME = "miufoiufoddc008";// miufoiufoddc008,时间
	private static final String STR_UNIT = "miufoiufoddc009";// miufoiufoddc009,单位
	
	private static final String STR_VER = "miufoiufoddc010";// miufoiufoddc010,版本
	private static final String STR_TASK = "miufoiufoddc011";// miufoiufoddc011,所属任务
	private static final String STR_UNITINFO = "miufoiufoddc012";// miufoiufoddc012,单位信息
	private static final String STR_UNITNAME = "miufoiufoddc013";// miufoiufoddc013,单位名称
	private static final String STR_UNITCODE = "miufoiufoddc014";// miufoiufoddc014,单位编码
	private static final String STR_PUBLIC_REPORT  = "miufoiufoddc015";// miufoiufoddc015,公有报表
	private static final String STR_TASK_INFO = "miufoiufoddc016";// miufoiufoddc016,任务信息
	private static final String STR_TASK_PK = "miufoiufoddc017";// miufoiufoddc017,任务主键
	private static final String STR_TASK_NAME = "miufoiufoddc018";// miufoiufoddc018,任务名称
	private static final String STR_UNITPK = "miufoiufoddc019";// miufoiufoddc019,单位主键
																
	public static final int TABLE_TYPE_NULL = -1;

	public static final int TABLE_TYPE_MAIN = 0;

	public static final int TABLE_TYPE_FIX = 1;

	public static final int TABLE_TYPE_DYN = 2;

	// Suppresses default constructor, ensuring non-instantiability.
	private IUFODDCUtil() {
	}

	/**
	 * 根据某动态区域的所有关键字（除去公有报表的关键字部分）， 计算该动态区域的公有关键字和私有关键字矢量。 创建日期：(2003-10-27
	 * 14:04:10)
	 * 
	 * @author：刘良萍
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
	 * 创建某报表的所有指标：按照指标所在物理表组织数据。
	 * 
	 * 创建日期：(2003-9-18 19:13:14)
	 * 
	 * @author：刘良萍
	 * @param vecObjectNodes
	 *            java.util.Vector - 所创建指标节点的容器
	 * @param repVO
	 *            ReportVO - 报表vo
	 * @param strParentGUID
	 *            java.lang.String - 父节点GUID
	 * @param strReportNodeID
	 *            父节点ID
	 */
	private static void createAllMeasNodes(Vector vecObjectNodes,
			ReportVO repVO, String strParentGUID,String strReportNodeID) {

		String strRepPK = repVO.getReportPK();
		String strMainKeyCombPK = repVO.getKeyCombPK();
		if (strMainKeyCombPK == null) {
			// 屏蔽错误的报表
			return;
		}

		// #数据物理表名与显示表名的别称(dbTableName,DataDictNode)
		Hashtable hashDbTables = new Hashtable();
		// #将创建的该报表指标表节点的存储Vector
		Vector vecRepDataDictNodes = new Vector();

		// #创建报表主表节点
		// 得到主表关键字集合
		KeyGroupCache keyGroupCache = CacheProxy.getSingleton()
				.getKeyGroupCache();
		KeyGroupVO mainKeyGroupVO = keyGroupCache.getByPK(strMainKeyCombPK);
		KeyVO[] mainKeyVOs = mainKeyGroupVO != null ? mainKeyGroupVO.getKeys()
				: null;
		Hashtable hashMainKey = getHashKeyWordPKs(mainKeyVOs);
		// 创建报表主表节点及其主键、主表关键字字段
		String strRepCode = repVO.getCode();
		DatadictNode mainTableNode = createMainTableNode(strParentGUID,strReportNodeID,repVO
				.getReportPK()
				+ "pub", strRepCode, mainKeyVOs);
		// 并维护hash索引 和存储Vector
		hashDbTables.put(getStrRepDataMainTable(), mainTableNode);
		vecRepDataDictNodes.add(mainTableNode);

		// #创建报表固定/动态数据子表
		// 得到报表的所有指标
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
		// 得到报表的格式模型
		RepFormatModelCache formatCache = CacheProxy.getSingleton()
				.getRepFormatCache();
		CellsModel tableFormatModel = formatCache
				.getUfoTableFormatModel(repVO.getReportPK());
		for (int i = 0; i < iLen; i++) {
			if (measVOs[i] == null || measVOs[i].getDbtable() == null
					|| measVOs[i].getDbcolumn() == null) {
				// 屏蔽错误的报表格式
				return;
			}
			String strDbTableName = measVOs[i].getDbtable();
			// 得到并维护固定表/动态区域的表对象
			if (hashDbTables.containsKey(strDbTableName)) {
				// 得到表节点
				tableNode = (DatadictNode) hashDbTables.get(strDbTableName);
			} else {
				// 准备strDisplayName和表类型nTableType
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
				// 创建表节点和"主键"节点,并维护hash索引
				String strDynAreaPK = "";
				String strIdentityPKSource = "";
				// 固定数据表
				if (nTableType == TABLE_TYPE_FIX) {
					strIdentityPKSource = repVO.getReportPK()
							+ strDbTableName.substring(18);
				}
				// 动态数据表
				else if (nTableType == TABLE_TYPE_DYN) { // 得到所属动态区域的PK
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

				// 如果是动态区域的表节点，则创建其关键字组合字段
				// 需要区别公有、私有：
				// 1)如果有公有关键字，需要建立该动态区域的公有关键字子表,增加相应的公有关键字字段
				// 2)私有关键字则直接放在动态区域表节点中作为表字段
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
						// 创建动态区域的主表节点和公有关键字字段(同一动态区域可能有多个物理表)
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
							// 创建关键字对应的表字段
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

			// 创建指标节点,将并添加到表节点里
			FieldDef measField = createMeasField(measVOs[i]);
			((TableDef) tableNode.getObject()).getFieldDefs().addFieldDef(
					measField);
		}

		// 将所创建指标表节点的加到Vector容器
		int iSize = vecRepDataDictNodes.size();
		for (int i = 0; i < iSize; i++) {
			DatadictNode node = (DatadictNode) vecRepDataDictNodes.get(i);
			vecObjectNodes.add(node);
		}

		// int j = 0;

	}

	/**
	 * 创建该目录dir_id下的所有报表节点。
	 * 
	 * 创建日期：(2003-9-18 16:21:06)
	 * 
	 * @author：刘良萍
	 * @param vecObjectNodes
	 *            java.util.Vector
	 * @param strDirId
	 *            java.lang.String
	 * @param strParentGUID
	 *            java.lang.String - 当前父节点GUID
	 */
	private static void createAllReportNodes(Vector vecObjectNodes,
			String strDirId, String strParentGUID, boolean bLoadAll) {

		// 得到该目录dir_id下的所有报表
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

				// 添加报表下的物理数据表
				if (bLoadAll)
					createAllMeasNodes(vecObjectNodes, reportVOSubs[i], subRepNode.getGUID(),subRepNode.getID());
			}
		}
	}

	/**
	 * 创建给定动态关键字的对应字段到指定的表节点里。
	 * 
	 * 创建日期：(2003-9-20 14:57:54)
	 * 
	 * @author：刘良萍
	 * @param tableNode
	 *            nc.vo.pub.ddc.datadict.DatadictNode - 动态数据表节点
	 * @param vecDynKeyVOs
	 *            java.util.Vector - 按照定义顺序放置的动态关键字矢量
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
	 * 创建报表主表节点及其主键、主表关键字字段。
	 * 
	 * 创建日期：(2003-9-20 13:56:11)
	 * 
	 * @author：刘良萍
	 * @return nc.vo.pub.ddc.datadict.DatadictNode
	 * @param strParentGUID
	 *            java.lang.String - 父表节点guid
	 * @param strRepPK
	 *            String
	 * @param strRepCode
	 *            java.lang.String - 报表编码
	 * @param mainKeyVOs
	 *            nc.vo.iufo.keydef.KeyVO[] - 报表的固定关键字集合
	 */
	private static DatadictNode createMainTableNode(String strParentGUID,String strReportNodeID,
			String strRepPK, String strRepCode, KeyVO[] mainKeyVOs) {
		// 创建表节点和“主表主键”字段
		DatadictNode mainTableNode = createTableNode(strParentGUID,strReportNodeID,
				getIdentityPK(strRepPK), TABLE_NAME_MEASURE_PUBDATA, strRepCode
						+ "-" + getStrRepDataMainTable(), TABLE_TYPE_MAIN);

		// 创建固定区域的关键字（只会有公有的）字段
		int iMainKeyLen = mainKeyVOs != null ? mainKeyVOs.length : 0;
		if (mainTableNode != null && iMainKeyLen > 0) {
			for (int i = 0; i < iMainKeyLen; i++) {
				String strDisplayName = getFieldDisPlayName(mainKeyVOs[i]);
				// 创建关键字对应的表字段
				FieldDef fieldDef = createTableField(TABLE_FILED_NAME_KEYWORD
						+ (i + 1), strDisplayName, MeasureVO.TYPE_CHAR);
				((TableDef) mainTableNode.getObject()).getFieldDefs()
						.addFieldDef(fieldDef);
			}
		}
		// 创建字段“版本(ver)”
		FieldDef verFieldDef = createTableField("ver", getStrVer(),
				MeasureVO.TYPE_NUMBER);// Variant.BIGDECIMAL);
		((TableDef) mainTableNode.getObject()).getFieldDefs().addFieldDef(
				verFieldDef);
		// 合并结果是按任务保存才增加任务信息表
		if(isHBvsTask()){
			// 创建字段“所属任务(formula_id)”
			FieldDef formulaIdFieldDef = createTableField("formula_id", getStrTask(),
					MeasureVO.TYPE_CHAR);;
			((TableDef) mainTableNode.getObject()).getFieldDefs().addFieldDef(
					formulaIdFieldDef);			
		}
		return mainTableNode;
	}

	/**
	 * 创建指标FieldDef。 创建日期：(2003-9-18 21:05:40)
	 * 
	 * @author：刘良萍
	 * @return nc.vo.pub.ddc.datadict.FieldDef
	 * @param measVO
	 *            nc.vo.iufo.measure.MeasureVO
	 */
	private static FieldDef createMeasField(MeasureVO measVO) {
		// System.out.println("[IUFODDCUtil指标字段类型]:Dbcolumn=" +
		// measVO.getDbcolumn()+",sql Type=
		// "+AreaDataSetUtil.getSqlTypeFromMeas(measVO.getType()));
		return createTableField(measVO.getDbcolumn(), measVO.getName(), measVO
				.getType());

	}

	/**
	 * 根据nTableType，创建“主表主键”或（指标表）“主键”。 如果不是指定类型，则返回null
	 * 
	 * 创建日期：(2003-9-20 10:25:15)
	 * 
	 * @author：刘良萍
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
	 * 创建报表目录节点。
	 * 
	 * 创建日期：(2003-9-18 16:07:37)
	 * 
	 * @author：刘良萍
	 * @param vecObjectNode
	 *            java.util.Vector -生成的节点装入的容器
	 * @param rDirVO
	 *            nc.vo.iuforeport.rep.ReportDirVO - 当前父报表目录
	 * @param strParentGUID
	 *            java.lang.String - 当前父节点GUID
	 * @param haveLeaf
	 *            boolean - 是否有子节点
	 */
	private static void createReportDirNodes(Vector vecObjectNode,
			ReportDirVO rDirVO, String strParentGUID, boolean haveLeaf, boolean bLoadAll) {
		// 1，子目录
		Vector vecSubDirVOs = rDirVO.getSubDirs();
		if (vecSubDirVOs != null) {

			// 报表目录树
			for (int i = 0; i < vecSubDirVOs.size(); i++) {
				ReportDirVO subDirVO = (ReportDirVO) vecSubDirVOs.get(i);
				// 递归添加子目录
				if (haveLeaf || subDirVO.getSubDirs() != null) {
					if (!(subDirVO.getDirId()
							.equals((ReportDirVO.REPORTDIR_PRIVATE_DIR_ID)))) {
						FolderNode subNode = new FolderNode();
						subNode.setID(subDirVO.getDirId());
						String strDisplayName = subDirVO.getDirName();
						// 如果是单位根目录，给出单位信息，2005－03－03，liulp,from wss
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
// 如果单位不存在，则没有单位根目录
								// added by liulp,2005-06-03,升级库可能出现该情形
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

		// 2，该报表目录下的所有报表节点
		String strDirId = rDirVO.getDirId();
		createAllReportNodes(vecObjectNode, strDirId, strParentGUID, bLoadAll);

	}

	/**
	 * 创建表节点的一个字段。 创建日期：(2003-9-20 13:10:45)
	 * 
	 * @author：刘良萍
	 * @return nc.vo.pub.ddc.datadict.FieldDef
	 * @param strDBColumn
	 *            java.lang.String
	 * @param strDisplayName
	 *            java.lang.String
	 * @param nColType
	 *            int 对应指标VO的数据类型
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
	 * 创建含有主键字段的一个表节点。
	 * 
	 * 创建日期：(2003-9-20 10:48:03)
	 * 
	 * @author：刘良萍
	 * @return nc.vo.pub.ddc.datadict.DatadictNode
	 * @param strParentGUID
	 *            java.lang.String
	 * @param strNodeID
	 *            节点ID
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
		// tableNode.setID("T_"+strGUID); //唯一ID
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

		// 创建表的"(主表)主键"节点
		FieldDef primField = createPrimField(nTableType);
		if (primField != null) {
			tableDef.getFieldDefs().addFieldDef(primField);
		}

		return tableNode;
	}

	/**
	 * 得到关键字字段的显示名称。 创建日期：(2003-9-20 15:14:03)
	 * 
	 * @author：刘良萍
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
	 * 得到过滤了指定hashToFilt中关键字的关键字Vector,要求顺序不被改变。
	 * 
	 * 创建日期：(2003-9-20 14:32:50)
	 * 
	 * @author：刘良萍
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
	 * 得到指定关键字集合的KeyWordPK的Hash。
	 * 
	 * 创建日期：(2003-9-20 14:30:04)
	 * 
	 * @author：刘良萍
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
	 * 得到对应指定字符串的短长度唯一PK。
	 * 
	 * 创建日期：(2003-10-29 10:36:23)
	 * 
	 * @author：刘良萍
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
	 * 得到DDC数需要的IUFO指标ObjectNodes。
	 * 
	 * 创建日期：(2003-9-18 14:23:07)
	 * 
	 * @author：刘良萍
	 * @return nc.vo.pub.core.ObjectNode[]
	 */
	public static nc.vo.pub.core.ObjectNode[] getMeasObjectNodes(Boolean bLoadAll) {
		// 设置语种值
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
		// 获得公有报表的节点数组:
		// 1，目录节点：FolderNode；FolderObject
		// ID，displayName,GUID,parentGUID；FolderObject
		// 2，表节点：DatadictNode；TableDef
		// 3，表里的字段对象：FieldDefList.addField(FieldDef)
		ReportDirVO pubRepDirVO = repDirCache.getRootReportDir(false);
		if (pubRepDirVO != null) {
			Vector vecObjectNode = new Vector();
			// 增加单位信息表在数据字典根节点下,added by liulp 2004-05-08(from zzj)
			addUnitInfoNode(vecObjectNode);
			// 增加任务信息表在数据字典根节点下,added by liulp 2005-03-03(from wss)
			addTaskInfoNode(vecObjectNode);
			// 增加IUFO公有报表目录节点及其所有报表
			ObjectNode pubRepRootNode = createRepRootNode(vecObjectNode,
					pubRepDirVO.getDirId());
			vecObjectNode.add(pubRepRootNode);
			// 创建所有的公有报表节点
			createReportDirNodes(vecObjectNode, pubRepDirVO, pubRepRootNode
					.getGUID(), true, bLoadAll.booleanValue());
			// 装换为ObjectNode树组
			int iLen = vecObjectNode.size();
			ObjectNode[] nodes = new ObjectNode[iLen];
			vecObjectNode.copyInto(nodes);

			return nodes;
		}
		return null;
	}

	/**
	 * 创建任务信息表节点到“数据字典”根目录下： IUFO任务信息表，含字段：单位名称，单位编码
	 * 
	 * @param vecObjectNode
	 */
	private static void addUnitInfoNode(Vector vecObjectNode) {
		String strIdentidyKey = "iufounitinfo";
		String strTableName = IDatabaseNames.IUFO_UNIT_INFO_TABLE;
		String strTableDisName = getStrUnitInfo();
		// 创建并增加“单位信息”表节点
		DatadictNode unitInfoNode = createTableNode(DataDictForNode.ROOT_GUID,"-1",
				strIdentidyKey, strTableName, strTableDisName, TABLE_TYPE_NULL);
		vecObjectNode.add(unitInfoNode);
//		// 创建并增加,"单位主键",“单位名称”、“单位编码”字段节点
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
		//IUFO5.1,除了单位主键，“单位名称”、“单位编码”字段节点，还需要增加单位的其他信息，包括上级单位主键，单位结构信息
		//单位主键
		String strFieldUnitPK = "unit_id";
		String strDisUnitPK = getStrUnitPK();
		FieldDef unitPKField = createTableField(strFieldUnitPK,
				strDisUnitPK, MeasureVO.TYPE_CHAR);
		tableDef.getFieldDefs().addFieldDef(unitPKField);
		//单位名称，单位编码，单位级次编码等其他单位结构
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
	 * 创建“公有报表”目录节点
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
	 * 创建单位信息表节点到“数据字典”根目录下： IUFO单位信息表，含字段：单位名称，单位编码
	 * 
	 * @param vecObjectNode
	 *            Vector
	 */
	private static void addTaskInfoNode(Vector vecObjectNode) {
		// 合并结果是按任务保存才增加任务信息表
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
		// 创建并增加“单位信息”表节点
		DatadictNode unitInfoNode = createTableNode(DataDictForNode.ROOT_GUID,"-1",
				strIdentidyKey, strTableName, strTableDisName, TABLE_TYPE_NULL);
		vecObjectNode.add(unitInfoNode);
		// 创建并增加“任务名称”、“任务主键”字段节点
		FieldDef taskNameField = createTableField(strFieldTaskName,
				strDisTaskName, MeasureVO.TYPE_CHAR);
		FieldDef unitTaskIdField = createTableField(strFieldTaskId,
				strDisTaskId, MeasureVO.TYPE_CHAR);
		TableDef tableDef = (TableDef) unitInfoNode.getObject();
		tableDef.getFieldDefs().addFieldDef(taskNameField);
		tableDef.getFieldDefs().addFieldDef(unitTaskIdField);
	}

	/**
	 * 合并结果是否按任务保存
	 * 
	 * @return true,是按任务保存;false,是按
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
	 * 创建该目录dir_id下的所有报表节点。
	 * 
	 * 创建日期：(2003-9-18 16:21:06)
	 * 
	 * @author：刘良萍
	 * @param vecObjectNodes
	 *            java.util.Vector
	 * @param strDirId
	 *            java.lang.String
	 * @param strParentGUID
	 *            java.lang.String - 当前父节点GUID
	 */
	public static ObjectNode[] createMeasNodes(String strDirId,
			String strReportId, String strReportGuid,String strReportNodeID) {

		// 得到该目录dir_id下的所有报表
		nc.pub.iufo.cache.ReportCache repCache = CacheProxy
				.getSingleton().getReportCache();

		ReportVO[] reportVOSubs = repCache.getReportsByDirPK(strDirId);

		Vector vecObjectNodes = new Vector();
		int iLen = (reportVOSubs == null) ? 0 : reportVOSubs.length;
		for (int i = 0; i < iLen; i++) {
			// 添加对应报表下的物理数据表
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
	 * @return 返回 STR_REPDATA_MAIN_TABLE。
	 */
	private static String getStrRepDataMainTable() {		;
		return getStrResoucre(STR_REPDATA_MAIN_TABLE);
	}

	/**
	 * 获得指定资源编码的字符串值
	 * 
	 * @param strResourceCode
	 * @return
	 */
	private static String getStrResoucre(String strResourceCode) {
		return StringResource.getStringResource(strResourceCode);
	}

	/**
	 * @return 返回 sTR_REPDATA_FIX_TABLE。
	 */
	private static String getStrRepDataFixTable() {
		return getStrResoucre(STR_REPDATA_FIX_TABLE);
	}

	/**
	 * @return 返回 sTR_REPDATA_DYN_TABLE。
	 */
	private static String getStrRepDataDynTable() {
		return getStrResoucre(STR_REPDATA_DYN_TABLE);
	}

	/**
	 * @return 返回 sTR_REPDATA_DYN_MAIN_TABLE。
	 */
	private static String getStrRepDataDynMainTable() {
		return getStrResoucre(STR_REPDATA_DYN_MAIN_TABLE);
	}

	/**
	 * @return 返回 sTR_KEYWORD。
	 */
	private static String getStrKeyword() {
		return getStrResoucre(STR_KEYWORD);
	}

	/**
	 * @return 返回 sTR_MAINTABLE_MAINPRIM。
	 */
	private static String getStrMainTableMainPrim() {
		return getStrResoucre(STR_MAINTABLE_MAINPRIM);
	}

	/**
	 * @return 返回 sTR_MAINPRIM。
	 */
	private static String getStrMainPrim() {
		return getStrResoucre(STR_MAINPRIM);
	}

	/**
	 * @return 返回 sTR_TIME。
	 */
	private static String getStrTime() {
		return getStrResoucre(STR_TIME);
	}

	/**
	 * @return 返回 sTR_UNIT。
	 */
	private static String getStrUnit() {
		return getStrResoucre(STR_UNIT);
	}

	/**
	 * @return 返回 sTR_VER。
	 */
	private static String getStrVer() {
		return getStrResoucre(STR_VER);
	}

	/**
	 * @return 返回 sTR_TASK。
	 */
	private static String getStrTask() {
		return getStrResoucre(STR_TASK);
	}

	/**
	 * @return 返回 sTR_UNITINFO。
	 */
	private static String getStrUnitInfo() {
		return getStrResoucre(STR_UNITINFO);
	}

	/**
	 * @return 返回 sTR_UNITNAME。
	 */
	private static String getStrUnitName() {
		return getStrResoucre(STR_UNITNAME);
	}

	/**
	 * @return 返回 sTR_UNITCODE。
	 */
	private static String getStrUnitCode() {
		return getStrResoucre(STR_UNITCODE);
	}
	/**
	 * @return 返回 sTR_UNITPK。
	 */
	private static String getStrUnitPK() {
		return getStrResoucre(STR_UNITPK);
	}

	/**
	 * @return 返回 sTR_PUBLIC_REPORT。
	 */
	private static String getStrPublicReport() {
		return getStrResoucre(STR_PUBLIC_REPORT);
	}

	/**
	 * @return 返回 sTR_TASK_INFO。
	 */
	private static String getStrTaskInfo() {
		return getStrResoucre(STR_TASK_INFO);
	}

	/**
	 * @return 返回 sTR_TASK_PK。
	 */
	private static String getStrTaskPK() {
		return getStrResoucre(STR_TASK_PK);
	}

	/**
	 * @return 返回 sTR_TASK_NAME。
	 */
	private static String getStrTaskName() {
		return getStrResoucre(STR_TASK_NAME);
	}

	/**
	 * 创建该目录dir_id下的所有报表节点。
	 */
	public static ObjectNode[] createMeasNodes(String strDirId,
			String strReportId, String strReportGuid) {
		return createMeasNodes(strDirId, strReportId, strReportGuid, strReportId);
	}

	/**
	  * 增补数据字典
	  */
	public static void appendDatadict(DataDictForNode treeIUFO, QueryBaseDef qbd) {
		if (treeIUFO.isIUFO()) {
		   FromTableVO[] fts = qbd.getFromTables();
		   int iLen = (fts == null) ? 0 : fts.length;
		   for (int i = 0; i < iLen; i++) {
			   String tableId = fts[i].getTablealias();
			   // 临时表不管
			   if (QueryUtil.isTempTable(tableId)) {
				   continue;
			   }
			   // 从数据字典树查找
			   BizObject td = treeIUFO.findObject(tableId);
			   if (td == null) {
				   // 该表未找到，属于分级加载遗漏品，需要找到其所属报表目录ID
				   String strReportNodeId = fts[i].getNote();
				   // 触发该目录节点的分级加载事件
				   if (strReportNodeId != null) {
					   treeIUFO.getChidren(strReportNodeId, new Vector(), false);
				   }
			   }
		   }
		}
	}
}
