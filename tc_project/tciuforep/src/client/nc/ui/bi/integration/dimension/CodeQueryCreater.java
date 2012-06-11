package nc.ui.bi.integration.dimension;

import java.sql.Types;
import java.util.ArrayList;

import nc.bs.bi.report.manager.DataManageObjectUFBI;
import nc.pub.iufo.cache.base.CodeCache;
import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.resmng.ResMngDirBO_Client;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.uitemplate.TreeTableOperFactory;
import nc.ui.iufo.resmng.uitemplate.TreeTableOperator;
import nc.ui.iufo.unit.Exprop4UnitPropBizProxy;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.pub.IDMaker;
import nc.util.iufo.resmng.IResMngConsants;
import nc.util.iufo.resmng.ResMngBizHelper;
import nc.vo.bi.query.manager.AdvancedQueryModelDef;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryUtil;
import nc.vo.bi.query.manager.ForgeQueryModelVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.resmng.ResMngDirVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.unit.UnitPropVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.iuforeport.businessquery.WhereCondVO;
import nc.vo.pub.querymodel.QueryBaseVO;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufsoft.iufo.resource.StringResource;

/**
 * ����Ͷ���֯���ɲ�ѯ
 * 
 * @author ll
 * 
 */
public class CodeQueryCreater {
	private static final String SYS_QUERY_FOLDER_ID = "SystemQueryFolder001";

	private String codeFldName = StringResource.getStringResource("miufo1001012");// miufo1001012=����
	private String nameFldName = StringResource.getStringResource("miufo1001051");// miufo1001051=����

	/*
	 * Ϊ����Ͷ���֯�Զ����ɲ�ѯ
	 */
	public void createAllNeedQuerys() throws Exception {

		// ������ϵͳԤ�á��Ĳ�ѯĿ¼
		ResMngDirVO sysFolder = ResMngDirBO_Client.loadDirByPK(SYS_QUERY_FOLDER_ID, IBIResMngConstants.MODULE_QUERY);
		if (sysFolder == null){
			String sysName = StringResource.getStringResource("miufo1003058"); // miufo1003058=ϵͳԤ��
			createSysQueryFolder(SYS_QUERY_FOLDER_ID, sysName, UserInfoVO.SYSINIT_USERID);
		}
			

		String strWhere = "pk_folderID='" + SYS_QUERY_FOLDER_ID + "'";
		QueryModelVO[] querys = BIQueryUtil.getQueryModels(strWhere, DataManageObjectUFBI.UFBI_DATASOURCE);

		// ��ȡϵͳ�����д���
		CodeCache codeCache = IUFOUICacheManager.getSingleton().getCodeCache();
		CodeVO[] codes = codeCache.getAllCode();

		// ���ݴ������ɲ�ѯ
		ArrayList<QueryModelVO> al_query = new ArrayList<QueryModelVO>();
		ArrayList<BIQueryModelDef> al_queryDef = new ArrayList<BIQueryModelDef>();
		if (codes != null && codes.length > 0) {
			for (int i = 0; i < codes.length; i++) {
				if(codes[i].getId().equals(CodeVO.UNIT_CODE_ID))//��λ�ں��浥������
					continue;
				if (!isExistQuery(codes[i].getName(), querys)) {
					QueryModelVO query = createCodeQuery(codes[i]);
					if (query != null) {
						al_query.add(query);
						al_queryDef.add((BIQueryModelDef) query.getDefinition());
					}
				}
			}
		}
		// ��ȡϵͳ�����ж���֯
		UnitPropVO[] units=Exprop4UnitPropBizProxy.loadOrgUnitProps(true);

		// ���ݶ���֯���ɲ�ѯ
		if (units != null && units.length > 0) {
			for (int i = 0; i < units.length; i++) {
				if (!isExistQuery(units[i].getName(), querys)) {
					QueryModelVO query = createUnitQuery(units[i]);
					if (query != null) {
						al_query.add(query);
						al_queryDef.add((BIQueryModelDef) query.getDefinition());
					}
				}
			}
		}

		// ִ�в�ѯ���������
		if (al_query.size() > 0) {
			QueryModelVO[] queryVOs = al_query.toArray(new QueryModelVO[0]);
			BIQueryModelDef[] queryDefs = al_queryDef.toArray(new BIQueryModelDef[0]);
			QueryModelBO_Client.insertArray(queryVOs, queryDefs, DataManageObjectUFBI.UFBI_DATASOURCE);
		}

	}

	public static void createSysQueryFolder(String dirID, String dirName, String userPK) throws Exception {
		// Ŀ¼VO
		ResMngDirVO newDirVO = new ResMngDirVO();
		newDirVO.setDirPK(dirID);
		newDirVO.setName(dirName);
		newDirVO.setNote(dirName);
		newDirVO.setOwnerPK(userPK);
		newDirVO.setParentDirPK(IResMngConsants.VIRTUAL_ROOT_ID);

		// ת��Ϊ��Դ����
		IResTreeObject treeObj = ResMngBizHelper.getDirTreeObj(newDirVO, IBIResMngConstants.MODULE_QUERY, null);

		// ������Դ�����ܽ��д���
		ResWebParam webParam = new ResWebParam(IBIResMngConstants.MODULE_QUERY, userPK, userPK,UnitPropVO.BASEORGPK);
		TreeTableOperator treeOperator = TreeTableOperFactory.getTreeTableOper(webParam);
		treeOperator.createDir(treeObj);
	}

	private static boolean isExistQuery(String queryName, QueryModelVO[] querys) {
		if (querys != null) {
			for (int i = 0; i < querys.length; i++) {
				if (querys[i].getQueryname().equals(queryName))
					return true;
			}
		}
		return false;
	}

	private QueryModelVO createCodeQuery(CodeVO code) throws Exception {
		QueryModelVO queryVO = getQueryModelVO(code.getName());

		String tableName = "iufo_codedata";
		String[] fldNames = new String[]{"content", "data_id"};// // �����Ա�����ơ����α���
		String[] fldDiapNames = new String[]{nameFldName, codeFldName};
		String strWhereCond = "code_id = '" + code.getId() + "'";
		
		queryVO.setDefinition(getQueryModelDef(queryVO, tableName, fldNames, fldDiapNames, strWhereCond));

		return queryVO;

	}
	/**
	 * @i18n miufoiufoddc019=��λ����
	 * @i18n miufo00316=���α���
	 */
	private QueryModelVO createUnitQuery(UnitPropVO unitorg) throws Exception {
		QueryModelVO queryVO = getQueryModelVO(unitorg.getName());
		String tableName = "iufo_unit_info";
		String[] fldNames = new String[]{"unit_name", "unit_code","unit_id",unitorg.getDBColumnName()};// // �����Ա�����ơ����α���
		String[] fldDiapNames = new String[]{nameFldName, codeFldName, StringResource.getStringResource("miufoiufoddc019"), StringResource.getStringResource("miufo00316")};
		
		String strWhereCond = unitorg.getDBColumnName() + " is not null";
		
	
		queryVO.setDefinition(getQueryModelDef(queryVO, tableName, fldNames, fldDiapNames, strWhereCond));

		return queryVO;

	}

	/*
	 * ������ѯ����
	 */
	private static QueryModelVO getQueryModelVO(String queryName) {
		QueryModelVO queryModelVO = new QueryModelVO();
		queryModelVO.setID(IDMaker.makeID(20));
		queryModelVO.setPk_folderID(SYS_QUERY_FOLDER_ID);
		queryModelVO.setName(queryName);
		queryModelVO.setQuerycode(queryName);
		queryModelVO.setOwneruser(UserInfoVO.SYSINIT_USERID);

		queryModelVO.setPk_datasource(DataManageObjectUFBI.UFBI_DATASOURCE);
		queryModelVO.setType(BIQueryConst.TYPE_BUSIMODEL);

		return queryModelVO;
	}

	/*
	 * ����Ԫ���ݣ�������Ϊѡ���ֶΣ�
	 */
	private static MetaDataVO getFieldMetaDataVO(String tableName, String strFldName, String strDispName, int colType)
			throws Exception {
		MetaDataVO mDataVO = new MetaDataVO(strFldName, strDispName, colType);
		mDataVO.setExpression(tableName + "." + strFldName);
		mDataVO.setDimflag(false);
		mDataVO.setPrecision(50);
		return mDataVO;
	}
	/*
	 * ������ѯ����ĸ�ʽģ�ͣ�Ŀǰ���ǵ������ֶ�ȫ��Ϊ�ַ�����
	 */
	private BIQueryModelDef getQueryModelDef(QueryModelVO queryVO, String tableName, String[] fldNames, String[] fldDispNames, String strWhereCond) throws Exception {
		
		// ��������
		String strQueryPK = queryVO.getID();
		BIQueryModelDef def = new BIQueryModelDef();

		// Ԫ����ģ�ͺ��ﻯģ������
		AdvancedQueryModelDef advancedModelDef = new AdvancedQueryModelDef();
		ForgeQueryModelVO forgeVO = new ForgeQueryModelVO();
		forgeVO.setPrimaryKey(strQueryPK);
		forgeVO.setType(queryVO.getType());
		forgeVO.setPk_folderID(queryVO.getPk_folderID());
		advancedModelDef.setForgeQueryModel(forgeVO);
		
		// ����Ԫ����ģ��
		MetaDataVO[] mDataVOs = new MetaDataVO[fldNames.length];
		SelectFldVO[] selectFlds = new SelectFldVO[fldNames.length];
		for (int i = 0; i < mDataVOs.length; i++) {
			mDataVOs[i] = getFieldMetaDataVO(tableName, fldNames[i], fldDispNames[i], Types.VARCHAR);
			selectFlds[i] = mDataVOs[i];
		}
		advancedModelDef.setMetadatas(mDataVOs);
		def.setAdvModel(advancedModelDef);

		//��ѯ����ģ������
		QueryBaseVO queryBaseVO = new QueryBaseVO();
		FromTableVO fromTable = new FromTableVO();
		fromTable.setTablecode(tableName);
		fromTable.setTabledisname(StringResource.getStringResource("miufo1002568"));// miufo1002568=��������
		fromTable.setTablealias(tableName);
		queryBaseVO.setFromTables(new FromTableVO[] { fromTable });
		queryBaseVO.setDsName(DataManageObjectUFBI.UFBI_DATASOURCE);

		queryBaseVO.setSelectFlds(selectFlds);

		queryBaseVO.setWhereConds(new WhereCondVO[] { new WhereCondVO(strWhereCond) });
		QueryModelDef baseModelDef = new QueryModelDef();
		baseModelDef.setQueryBaseVO(queryBaseVO);

		def.setBaseModel(baseModelDef);
		return def;
	}

}
 