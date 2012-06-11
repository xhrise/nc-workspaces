/*
 * �������� 2006-3-16
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.us.bi.query.manager;

import java.io.Serializable;

import nc.bs.bi.report.manager.DataManageObjectUFBI;
import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.resmng.AbsFiled4DirSysImpl;
import nc.util.iufo.resmng.IDirectoried;
import nc.util.iufo.resmng.ResDirectoriedImpl;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryUtil;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResTreeObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 * 
 * ��ѯ����UI������
 */
public class QuerySrv extends AbsFiled4DirSysImpl implements IDirectoried, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String DATASOURCENAME = DataManageObjectIufo.IUFO_DATASOURCE;

	private static QuerySrv s_querySrv = new QuerySrv();

	public QuerySrv() {
		super(IBIResMngConstants.MODULE_QUERY);
	}

	public static QuerySrv getInstance() {
		return s_querySrv;
	}

	/*
	 * �������еĲ�ѯ
	 * 
	 * @see nc.util.iufo.resmng.AbsFiled4DirSysImpl#doGetAllFileVOs()
	 */
	protected ValueObject[] doGetAllFileVOs() throws UISrvException {
		String strWhere = "1=1";
		return BIQueryUtil.getQueryModels(strWhere, DATASOURCENAME);
	}

	/*
	 * ���ò�ѯ��Ŀ¼�ɣ�
	 * 
	 * @see nc.util.iufo.resmng.AbsFiled4DirImpl#doSetParentDirVOPK(nc.vo.pub.ValueObject,
	 *      java.lang.String)
	 */
	protected void doSetParentDirVOPK(ValueObject fileVO, String strParentDirVOPK) throws UISrvException {
		if (fileVO != null) {
			QueryModelVO queryVO = (QueryModelVO) fileVO;
			queryVO.setPk_folderID(strParentDirVOPK);
		}
	}

	/*
	 * VO ����ת��Ϊ������
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#changeToFileTreeObj(nc.vo.pub.ValueObject,
	 *      java.lang.String)
	 */
	protected IResTreeObject changeToFileTreeObj(ValueObject objVO, String strShareFlag) throws UISrvException {
		if (objVO == null) {
			return null;
		}

		QueryModelVO queryVO = (QueryModelVO) objVO;

		IResTreeObject resTreeObject = new ResTreeObject();
		resTreeObject = new ResTreeObject();
		int nObjectType = IResTreeObject.OBJECT_TYPE_FILE;
		resTreeObject.setID(ResMngToolKit.getTreeObjIDAued(queryVO.getID(), strShareFlag, nObjectType));
		resTreeObject.setLabel(queryVO.getQueryname());
		resTreeObject.setParentID(ResMngToolKit.getTreeObjIDAued(queryVO.getPk_folderID(), strShareFlag,
				IResTreeObject.OBJECT_TYPE_DIR));
		resTreeObject.setType(nObjectType);
		resTreeObject.setSrcVO(objVO);

		return resTreeObject;

	}

	/*
	 * ������ʾ�Ĳ�ѯ�ֶε�ֵ����ѯ���룬���ƣ�����, ����Դ���ƣ�˵��
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileDisAttr(java.lang.String,
	 *      nc.vo.pub.ValueObject)
	 */
	protected Object[] doGetFileDisAttr(String strFileTreeObjID, ValueObject fileVO) throws UISrvException {

		if (fileVO != null) {
			QueryModelVO queryVO = (QueryModelVO) fileVO;
			String[] strValues = new String[6];
			int nIndex = 0;
			strValues[nIndex++] = strFileTreeObjID;
			strValues[nIndex++] = queryVO.getQuerycode();
			strValues[nIndex++] = queryVO.getQueryname();
			String strType = BIQueryConst.TYPENAMES[Integer.parseInt(queryVO.getType()) - 1];
			strValues[nIndex++] = StringResource.getStringResource(strType);
			strValues[nIndex++] = queryVO.getDscode();
			strValues[nIndex] = queryVO.getNote();

			return strValues;
		}
		return null;
	}

	/*
	 * ����ָ��PK�Ĳ�ѯ����
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileVO(java.lang.String)
	 */
	protected ValueObject doGetFileVO(String strSrcVOPK) throws UISrvException {
		if (strSrcVOPK != null) {
			String strWhere = "pk_querymodel='" + strSrcVOPK + "'";
			QueryModelVO[] queryVOs = BIQueryUtil.getQueryModels(strWhere, DATASOURCENAME);
			if (queryVOs != null && queryVOs.length == 1 && queryVOs[0] != null) {
				return queryVOs[0];
			}
		}
		return null;
	}

	/*
	 * �½�
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCreateFileVO(nc.vo.pub.ValueObject)
	 */
	protected ValueObject doCreateFileVO(ValueObject toCreateVO,String strOrgPK) throws UISrvException {
		if (toCreateVO != null) {
			try {
				String strID = QueryModelBO_Client.insert((SuperVO) toCreateVO, null, DATASOURCENAME);
				// ��������
				toCreateVO.setPrimaryKey(strID);
				return toCreateVO;
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCheckFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doCheckFileVO(ValueObject toCreateVO) throws UISrvException {
		// ����ѯ�ı���������Ƿ��ظ�
		if (toCreateVO != null) {
			try {
				QueryModelVO newVO = (QueryModelVO) toCreateVO;
				QueryModelVO oldVO = getQueryModelVOByCode(newVO.getPk_folderID(), newVO.getQuerycode());
				if (oldVO != null && oldVO.getPrimaryKey().equals(newVO.getPrimaryKey()) == false) {
					throw new UISrvException(StringResource.getStringResource("mbiquery0001"));// "��ѯ�����ظ�
				}
				oldVO = getQueryModelVOByName(newVO.getPk_folderID(), newVO.getQueryname());
				if (oldVO != null && oldVO.getPrimaryKey().equals(newVO.getPrimaryKey()) == false) {
					throw new UISrvException(StringResource.getStringResource("mbiquery0002"));// "��ѯ�����ظ�
				}
			} catch (UISrvException e) {
				throw e;
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}

	}

	/*
	 * �޸�
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doUpdateFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doUpdateFileVO(ValueObject fileVO,String strOrgPK) throws UISrvException {
		if (fileVO != null) {
			try {
				// ִ�и���
				QueryModelBO_Client.update((SuperVO) fileVO, DATASOURCENAME);
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}

	}

	/*
	 * ɾ��
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doRemoveFileVOs(java.lang.String[])
	 */
	protected void doRemoveFileVOs(String[] strSrcVOPKs) throws UISrvException {
		if (strSrcVOPKs != null) {
			try {
				QueryModelBO_Client.deleteArrayByPKs(strSrcVOPKs, DATASOURCENAME);
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCopyFileVO(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	protected ValueObject doCopyFileVO(String strSrcVOPK, String strDstParentDirPK, String strOperUserPK,String strOrgPK)
			throws UISrvException {
		QueryModelVO query = null;
		try {
			query = getQueryModelVO(strSrcVOPK);

			if (query != null) {
				// �����µı��룬���Ʋ����ظ�
				query.setQuerycode(getQueryCodeNameForCopy(query, true));
				query.setQueryname(getQueryCodeNameForCopy(query, false));
				query.setPk_folderID(strDstParentDirPK);
				query.setOwneruser(strOperUserPK);
				query.setPrimaryKey(null);
				String pk = createQueryModelVO(query);
				// ���Ʋ�ѯģ��,�õ���ѯģ�ͣ�����
				QueryModelBO_Client.copyQmd(new String[] { strSrcVOPK }, new String[] { pk }, DATASOURCENAME);
				query.setPrimaryKey(pk);
			}
		} catch (Exception ex) {
			throw new UISrvException(ex.getMessage());
		}
		return query;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getDirByID(java.lang.String)
	 */
	public IResTreeObject getDirByID(String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getDirByID(strDirID);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#createDir(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	public IResTreeObject createDir(IResTreeObject dirResTreeObj) throws UISrvException {
		return getDefaultDirectoriedImpl().createDir(dirResTreeObj);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#updateDir(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	public void updateDir(IResTreeObject dirResTreeObj) throws UISrvException {
		getDefaultDirectoriedImpl().updateDir(dirResTreeObj);

	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#removeDir(java.lang.String)
	 */
	public void removeDir(String strDirID) throws UISrvException {
		getDefaultDirectoriedImpl().removeDir(strDirID);

	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#isDescendant(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean isDescendant(String strAncestorID, String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().isDescendant(strAncestorID, strDirID);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getAllSubDirs(java.lang.String)
	 */
	public IResTreeObject[] getAllSubDirs(String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getAllSubDirs(strDirID);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#copyDir(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public IResTreeObject copyDir(String strSrcDirID, String strDestDirID, String strOperUserPK) throws UISrvException {
		return getDefaultDirectoriedImpl().copyDir(strSrcDirID, strDestDirID, strOperUserPK);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getFilesByDirID(java.lang.String)
	 */
	public IResTreeObject[] getFilesByDirID(String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getFilesByDirID(strDirID);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#copyFiles(java.lang.String[],
	 *      java.lang.String, java.lang.String)
	 */
	public void copyFiles(String[] strSrcFileIDs, String strDstDirID, String strOperUserID,String strOrgPK) throws UISrvException {
		getDefaultDirectoriedImpl().copyFiles(strSrcFileIDs, strDstDirID, strOperUserID,strOrgPK);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getDirsByParentID(java.lang.String)
	 */
	public IResTreeObject[] getDirsByParentID(String strParentDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getDirsByParentID(strParentDirID);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#moveDir(java.lang.String,
	 *      java.lang.String)
	 */
	public void moveDir(String strSrcDirID, String strDstDirID) throws UISrvException {
		getDefaultDirectoriedImpl().moveDir(strSrcDirID, strDstDirID);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#moveFile(java.lang.String,
	 *      java.lang.String)
	 */
	public void moveFile(String strSrcFileID, String strDstDirID) throws UISrvException {
		getDefaultDirectoriedImpl().moveFile(strSrcFileID, strDstDirID);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getFullPathName(java.lang.String,
	 *      java.lang.String)
	 */
	public String getFullPathName(String strResTreeObjID, String strResOwnerPK) throws UISrvException {
		return getDefaultDirectoriedImpl().getFullPathName(strResTreeObjID, strResOwnerPK);

	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IFiled4Dir#getSubFileVOs(java.lang.String)
	 */
	public ValueObject[] getSubFileVOs(String strDirVOPK) throws UISrvException {
		if (strDirVOPK != null) {
			String strWhere = "pk_folderID='" + strDirVOPK + "'";
			return BIQueryUtil.getQueryModels(strWhere, DATASOURCENAME);
		}
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IFiled4Dir#moveFileVO(java.lang.String,
	 *      java.lang.String)
	 */
	public void moveFileVO(String strSrcFileVOPK, String strDstDirVOPK) throws UISrvException {
		// �õ�QueryVO,������
		QueryModelVO queryVO = (QueryModelVO) doGetFileVO(strSrcFileVOPK);
		if (queryVO != null) {
			queryVO.setPk_folderID(strDstDirVOPK);
			try {
				QueryModelBO_Client.update((SuperVO) queryVO, DATASOURCENAME);
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}

	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.util.iufo.resmng.IFiled4Dir#getRootDirDisName(java.lang.String,
	 *      boolean)
	 */
	public String getRootDirDisName(String strOwerPK, boolean bSelfRoot) throws UISrvException {
		return StringResource.getStringResource("ubiquery0113");// TODO ������
	}

	public QueryModelVO getQueryModelVO(String strQueryPK) throws UISrvException {
		return (QueryModelVO) doGetFileVO(strQueryPK);
	}

	public QueryModelVO getQueryModelVOByCode(String strDirPK, String strQueryCode) throws Exception {
		return getVOByNameCode(strDirPK, strQueryCode, true);
	}

	public QueryModelVO getQueryModelVOByName(String strDirPK, String strName) throws Exception {
		return getVOByNameCode(strDirPK, strName, false);
	}
	private QueryModelVO getVOByNameCode(String strDirPK, String strNameCode, boolean isCode) throws Exception{
		if (strDirPK != null && strNameCode != null) {
			String strWhere = null;
			if(isCode)
				strWhere = "querycode='" + strNameCode;
			else
				strWhere = "queryname='" + strNameCode;
			strWhere +=  "' and pk_folderID='" + strDirPK + "'";
			SuperVO[] superVOs = QueryModelBO_Client.query(strWhere, DATASOURCENAME);
			if (superVOs != null && superVOs.length > 0) {
				return (QueryModelVO) superVOs[0];
			}
		}
		return null;

	}

	public BIQueryModelDef getQueryModelDef(String strQueryPK) throws Exception {
		if (strQueryPK != null) {
			return QueryModelBO_Client.queryQmd(strQueryPK, DATASOURCENAME);
		}
		return null;
	}

	public String createQueryModelVO(QueryModelVO queryVO) throws Exception {
		return QueryModelBO_Client.insert(queryVO, (BIQueryModelDef) queryVO.getDefinition(), DATASOURCENAME);
	}

	public void updateQueryModelVO(QueryModelVO queryVO) throws Exception {
		QueryModelBO_Client.update(queryVO, DATASOURCENAME);
	}

	public void updateQmd(String strQueryPK, BIQueryModelDef modelDef) throws Exception {
		QueryModelBO_Client.updateQmd(strQueryPK, modelDef, DATASOURCENAME);
	}

	public void removeQueryModels(String[] strQueryPKs) throws Exception {
		QueryModelBO_Client.deleteArrayByPKs(strQueryPKs, DATASOURCENAME);
	}

	private IDirectoried getDefaultDirectoriedImpl() {
		return ResDirectoriedImpl.getIDirectoried(IBIResMngConstants.MODULE_QUERY);
	}

	public static void copyQmd(String[] strSrcFileIDs, String[] strDestFileIDs) throws Exception {
		String dsNameForDef = DataManageObjectUFBI.UFBI_DATASOURCE;
		QueryModelBO_Client.copyQmd(strSrcFileIDs, strDestFileIDs, dsNameForDef);
	}

	private String getQueryCodeNameForCopy(QueryModelVO queryVO, boolean isCode) throws Exception {
		int nMaxCodeChar = 30;
		int nMaxNameChar = 62;
		
		String strNewCodePrefix = null;
		String strNewCode = null;
		int nIndex = 0;
		int nMaxChar = isCode?nMaxCodeChar:nMaxNameChar;
		String strOld = isCode?queryVO.getQuerycode():queryVO.getQueryname();
		if(getVOByNameCode(queryVO.getPk_folderID(), strOld, isCode) == null)
			return strOld;

		if (strOld.length() <= nMaxChar-2) {
			strNewCodePrefix = strOld;
		}
		else {
			strNewCodePrefix = strOld.substring(0, nMaxChar - 2);
		}
		do{
			nIndex++;
			// �жϲ�ѯ�����Ƿ��ظ�
			strNewCode = strNewCodePrefix + "-" + nIndex ;
		}while(getVOByNameCode(queryVO.getPk_folderID(), strNewCode, isCode) != null);			

		return strNewCode;
	}
}
