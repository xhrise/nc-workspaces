/*
 * 创建日期 2006-3-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.us.bi.report.manager;

import nc.pub.iufo.exception.UFOSrvException;

import nc.ui.bi.report.manager.ReportBO_Client;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.resmng.AbsFiled4DirSysImpl;
import nc.util.iufo.resmng.IDirectoried;
import nc.util.iufo.resmng.ResDirectoriedImpl;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.base.util.IDMaker;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.report.manager.BIReportUtil;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResTreeObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 * 
 * BI 报表管理服务类
 */
public class BIReportSrv extends AbsFiled4DirSysImpl implements IDirectoried {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static String DATASOURCENAME = DimRescource.UFBI_DATASOURCE;// "iufo";

	private static BIReportSrv s_objSrv = new BIReportSrv();

	private BIReportSrv() {
		super(IBIResMngConstants.MODULE_REPORT);
	}

	protected BIReportSrv(String modelID) {
		super(modelID);
	}

	public static BIReportSrv getInstance() {
		return s_objSrv;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.AbsFiled4DirSysImpl#doGetAllFileVOs()
	 */
	protected ValueObject[] doGetAllFileVOs() throws UISrvException {
		return (ReportVO[]) BIReportUtil.getReportVOs(null, DATASOURCENAME);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.AbsFiled4DirImpl#doSetParentDirVOPK(nc.vo.pub.ValueObject,
	 *      java.lang.String)
	 */
	protected void doSetParentDirVOPK(ValueObject fileVO, String strParentDirVOPK) throws UISrvException {
		if (fileVO != null) {
			ReportVO reportVO = (ReportVO) fileVO;
			reportVO.setPk_folderID(strParentDirVOPK);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#changeToFileTreeObj(nc.vo.pub.ValueObject,
	 *      java.lang.String)
	 */
	protected IResTreeObject changeToFileTreeObj(ValueObject objVO, String strShareFlag) throws UISrvException {
		if (objVO == null) {
			return null;
		}

		ReportVO reportVO = (ReportVO) objVO;

		IResTreeObject resTreeObject = new ResTreeObject();
		resTreeObject = new ResTreeObject();
		int nObjectType = IResTreeObject.OBJECT_TYPE_FILE;
		resTreeObject.setID(ResMngToolKit.getTreeObjIDAued(reportVO.getID(), strShareFlag, nObjectType));
		resTreeObject.setLabel(reportVO.getReportname());
		resTreeObject.setParentID(ResMngToolKit.getTreeObjIDAued(reportVO.getPk_folderID(), strShareFlag,
				IResTreeObject.OBJECT_TYPE_DIR));
		resTreeObject.setType(nObjectType);
		resTreeObject.setSrcVO(objVO);

		return resTreeObject;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileDisAttr(java.lang.String,
	 *      nc.vo.pub.ValueObject)
	 */
	protected Object[] doGetFileDisAttr(String strFileTreeObjID, ValueObject fileVO) throws UISrvException {

		if (fileVO != null) {
			ReportVO reportVO = (ReportVO) fileVO;
			String[] strValues = new String[6];
			int nIndex = 0;
			strValues[nIndex++] = strFileTreeObjID;
			strValues[nIndex++] = reportVO.getReportcode();
			strValues[nIndex++] = reportVO.getReportname();
			String strType_ID = ReportResource.TYPENAME_IDS[reportVO.getType().intValue()];
			strValues[nIndex++] = StringResource.getStringResource(strType_ID);
			strValues[nIndex] = reportVO.getNote();

			return strValues;
		}
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileVO(java.lang.String)
	 */
	protected ValueObject doGetFileVO(String strSrcVOPK) throws UISrvException {
		if (strSrcVOPK != null) {
			String strWhere = "pk_reportmodel='" + strSrcVOPK + "'";
			ReportVO[] reportVOs = BIReportUtil.getReportVOs(strWhere, DATASOURCENAME);
			if (reportVOs != null && reportVOs.length == 1 && reportVOs[0] != null) {
				return reportVOs[0];
			}
		}
		return null;
	}

	/*
	 * 新建
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCreateFileVO(nc.vo.pub.ValueObject)
	 */
	protected ValueObject doCreateFileVO(ValueObject toCreateVO, String strOrgPK) throws UISrvException {
		if (toCreateVO != null) {
			try {
				String strID = ReportBO_Client.insert((SuperVO) toCreateVO, DATASOURCENAME);
				// 设置主键
				toCreateVO.setPrimaryKey(strID);
				return toCreateVO;
			} catch (UFOSrvException e) {
				throw new UISrvException(e.getCause().getMessage());
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCheckFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doCheckFileVO(ValueObject toCreateVO) throws UISrvException {
		// 检查名称和编码不能重复
		if (toCreateVO != null) {
			try {
				ReportVO newVO = (ReportVO) toCreateVO;
				checkReportFileVO(newVO);
			} catch (UISrvException e) {
				throw e;
			}
		}
	}

	public void checkReportFileVO(ReportVO newVO) throws UISrvException {
		try {
			ReportVO oldVO = getByCode(newVO.getPk_folderID(), newVO.getReportcode());
			if (oldVO != null && oldVO.getPrimaryKey().equals(newVO.getPrimaryKey()) == false) {
				throw new UISrvException(StringResource.getStringResource("mbirep0001"));// "报表编码重复");
			}
			oldVO = getByName(newVO.getPk_folderID(), newVO.getReportname());
			if (oldVO != null && oldVO.getPrimaryKey().equals(newVO.getPrimaryKey()) == false) {
				throw new UISrvException(StringResource.getStringResource("mbirep0002"));// "报表名称重复");
			}
		} catch (UISrvException e) {
			throw e;
		} catch (Exception e) {
			throw new UISrvException(e.getMessage());
		}

	}

	/*
	 * 修改
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doUpdateFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doUpdateFileVO(ValueObject fileVO, String strOrgPK) throws UISrvException {
		if (fileVO != null) {
			try {
				// 执行更新
				ReportBO_Client.update((SuperVO) fileVO, DATASOURCENAME);
			} catch (UFOSrvException e) {
				throw new UISrvException(e.getCause().getMessage());
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}

	}

	/*
	 * 删除
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doRemoveFileVOs(java.lang.String[])
	 */
	protected void doRemoveFileVOs(String[] strSrcVOPKs) throws UISrvException {
		if (strSrcVOPKs != null) {
			try {
				ReportBO_Client.deleteArrayByPKs(strSrcVOPKs, DATASOURCENAME);
			} catch (UFOSrvException e) {
				throw new UISrvException(e.getCause().getMessage());
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCopyFileVO(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	protected ValueObject doCopyFileVO(String strSrcVOPK, String strDstParentDirPK, String strOperUserPK,
			String strOrgPK) throws UISrvException {
		try {
			// 复制后新建
			ReportVO srcVO = getByID(strSrcVOPK);
			if (srcVO == null)
				return null;
			ReportVO cloneVO = (ReportVO) srcVO.clone();
			cloneVO.setID(IDMaker.makeID(20));
			cloneVO.setPk_folderID(strDstParentDirPK);
			int index = 0;
			do {
				index++;
				cloneVO.setReportname(srcVO.getReportname() + "-" + index);
			} while (getByName(strDstParentDirPK, cloneVO.getReportname()) != null);

			index = 0;
			do {
				index++;
				cloneVO.setReportcode(srcVO.getReportcode() + "-" + index);
			} while (getByCode(strDstParentDirPK, cloneVO.getReportcode()) != null);

			cloneVO.setPk_folderID(strDstParentDirPK);
			cloneVO.setOwnerid(strOperUserPK);
			cloneVO.setPrimaryKey(null);
			String pk = createReport(cloneVO);
			cloneVO.setPrimaryKey(pk);

			return cloneVO;

		} catch (Exception ex) {
			throw new UISrvException(ex.getMessage());
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getDirByID(java.lang.String)
	 */
	public IResTreeObject getDirByID(String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getDirByID(strDirID);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#createDir(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	public IResTreeObject createDir(IResTreeObject dirResTreeObj) throws UISrvException {
		return getDefaultDirectoriedImpl().createDir(dirResTreeObj);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#updateDir(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	public void updateDir(IResTreeObject dirResTreeObj) throws UISrvException {
		getDefaultDirectoriedImpl().updateDir(dirResTreeObj);

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#removeDir(java.lang.String)
	 */
	public void removeDir(String strDirID) throws UISrvException {
		getDefaultDirectoriedImpl().removeDir(strDirID);

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#isDescendant(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean isDescendant(String strAncestorID, String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().isDescendant(strAncestorID, strDirID);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getAllSubDirs(java.lang.String)
	 */
	public IResTreeObject[] getAllSubDirs(String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getAllSubDirs(strDirID);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#copyDir(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public IResTreeObject copyDir(String strSrcDirID, String strDestDirID, String strOperUserPK) throws UISrvException {
		return getDefaultDirectoriedImpl().copyDir(strSrcDirID, strDestDirID, strOperUserPK);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getFilesByDirID(java.lang.String)
	 */
	public IResTreeObject[] getFilesByDirID(String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getFilesByDirID(strDirID);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#copyFiles(java.lang.String[],
	 *      java.lang.String, java.lang.String)
	 */
	public void copyFiles(String[] strSrcFileIDs, String strDstDirID, String strOperUserID, String strOrgPK)
			throws UISrvException {
		getDefaultDirectoriedImpl().copyFiles(strSrcFileIDs, strDstDirID, strOperUserID, strOrgPK);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getDirsByParentID(java.lang.String)
	 */
	public IResTreeObject[] getDirsByParentID(String strParentDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getDirsByParentID(strParentDirID);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#moveDir(java.lang.String,
	 *      java.lang.String)
	 */
	public void moveDir(String strSrcDirID, String strDstDirID) throws UISrvException {
		getDefaultDirectoriedImpl().moveDir(strSrcDirID, strDstDirID);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#moveFile(java.lang.String,
	 *      java.lang.String)
	 */
	public void moveFile(String strSrcFileID, String strDstDirID) throws UISrvException {
		getDefaultDirectoriedImpl().moveFile(strSrcFileID, strDstDirID);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IDirectoried#getFullPathName(java.lang.String,
	 *      java.lang.String)
	 */
	public String getFullPathName(String strResTreeObjID, String strResOwnerPK) throws UISrvException {
		return getDefaultDirectoriedImpl().getFullPathName(strResTreeObjID, strResOwnerPK);

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IFiled4Dir#getSubFileVOs(java.lang.String)
	 */
	public ValueObject[] getSubFileVOs(String strDirVOPK) throws UISrvException {
		if (strDirVOPK != null) {
			String strWhere = "pk_folderID='" + strDirVOPK + "'";
			return BIReportUtil.getReportVOs(strWhere, DATASOURCENAME);
		}
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IFiled4Dir#moveFileVO(java.lang.String,
	 *      java.lang.String)
	 */
	public void moveFileVO(String strSrcFileVOPK, String strDstDirVOPK) throws UISrvException {
		// 得到reportVO,再设置
		ReportVO reportVO = (ReportVO) doGetFileVO(strSrcFileVOPK);
		if (reportVO != null) {
			reportVO.setPk_folderID(strDstDirVOPK);
			try {
				ReportBO_Client.update((SuperVO) reportVO, DATASOURCENAME);
			} catch (Exception e) {
				throw new UISrvException(e.getMessage());
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.util.iufo.resmng.IFiled4Dir#getRootDirDisName(java.lang.String,
	 *      boolean)
	 */
	public String getRootDirDisName(String strOwerPK, boolean bSelfRoot) throws UISrvException {
		return StringResource.getStringResource("ubirep0010");// "报表管理";//
		// TODO 多语言
	}

	public ReportVO getByID(String strRepPK) throws Exception {
		String strWhere = "pk_reportmodel='" + strRepPK + "'";
		SuperVO[] superVOs = ReportBO_Client.query(strWhere, DATASOURCENAME);
		if (superVOs != null && superVOs.length > 0) {
			return (ReportVO) superVOs[0];
		}
		return null;
	}

	public ReportVO getByCode(String strDirPK, String strCode) throws Exception {
		if (strCode != null) {
			String strWhere = "reportcode='" + strCode + "' and pk_folderID='" + strDirPK + "'";
			SuperVO[] superVOs = ReportBO_Client.query(strWhere, DATASOURCENAME);
			if (superVOs != null && superVOs.length > 0) {
				return (ReportVO) superVOs[0];
			}
		}
		return null;
	}

	public ReportVO getByName(String strDirPK, String strName) throws Exception {
		if (strDirPK != null && strName != null) {
			String strWhere = "reportname='" + strName + "' and pk_folderID='" + strDirPK + "'";
			SuperVO[] superVOs = ReportBO_Client.query(strWhere, DATASOURCENAME);
			if (superVOs != null && superVOs.length > 0) {
				return (ReportVO) superVOs[0];
			}
		}
		return null;
	}

	public String createReport(ReportVO repVO) throws Exception {
		if (repVO != null) {
			return ReportBO_Client.insert((SuperVO) repVO, DATASOURCENAME);
		}
		return null;
	}

	public void updateReport(ReportVO repVO) throws Exception {
		if (repVO != null) {
			ReportBO_Client.update((SuperVO) repVO, DATASOURCENAME);
		}
	}

	public void removeReports(String[] strRepPKs) throws Exception {
		if (strRepPKs != null) {
			ReportBO_Client.deleteArrayByPKs(strRepPKs, DATASOURCENAME);
		}
	}

	protected IDirectoried getDefaultDirectoriedImpl() {
		return ResDirectoriedImpl.getIDirectoried(getModuleID());
	}

}
