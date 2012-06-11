package nc.us.bi.report.manager;

import com.ufida.report.anareport.model.AnaReportModel;

import nc.ui.iufo.resmng.common.UISrvException;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.vo.bi.base.util.IDMaker;
import nc.vo.bi.report.manager.BIReportUtil;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.pub.ValueObject;
import nc.ui.iufo.cache.IUFOUICacheManager;

public class BIFreeQueryReportSrv extends BIReportSrv {
	private static BIFreeQueryReportSrv s_objSrv = new BIFreeQueryReportSrv();

	private BIFreeQueryReportSrv() {
		super(IBIResMngConstants.MODULE_FREEQUERY);
	}
	public static BIFreeQueryReportSrv getInstance() {
		return s_objSrv;
	}
	protected ValueObject[] doGetAllFileVOs() throws UISrvException {
		return (ReportVO[]) BIReportUtil.getReportDefines("(type = 5 or type = 6)", DATASOURCENAME);
	}
	public ValueObject[] getSubFileVOs(String strDirVOPK) throws UISrvException {
		if (strDirVOPK != null) {
			String strWhere = "pk_folderID='" + strDirVOPK + "' and (type = 5 or type = 6)";
			return BIReportUtil.getReportDefines(strWhere, DATASOURCENAME);
		}
		return null;
	}

	protected Object[] doGetFileDisAttr(String strFileTreeObjID, ValueObject fileVO) throws UISrvException {

		if (fileVO != null) {
			ReportVO reportVO = (ReportVO) fileVO;
			String[] strValues = new String[6];
			int nIndex = 0;
			strValues[nIndex++] = strFileTreeObjID;
			strValues[nIndex++] = reportVO.getReportcode();
			strValues[nIndex++] = reportVO.getReportname();
			UserInfoVO user=IUFOUICacheManager.getSingleton().getUserCache().getUserById(reportVO.getOwnerid());
			strValues[nIndex++] = user!=null ? user.getStrName():"";
			strValues[nIndex++] = reportVO.getTs().toString();
			strValues[nIndex] = reportVO.getNote();

			return strValues;
		}
		return null;
	}
	@Override
	protected ValueObject doCopyFileVO(String strSrcVOPK,
			String strDstParentDirPK, String strOperUserPK, String strOrgPK)
			throws UISrvException {

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

			cloneVO.setOwnerid(strOperUserPK);
			if(cloneVO.getDefinition() instanceof AnaReportModel){
				((AnaReportModel)cloneVO.getDefinition()).clonePrivateDs(cloneVO.getID());
			}
			String pk = createReport(cloneVO);
			cloneVO.setPrimaryKey(pk);

			return cloneVO;

		} catch (Exception ex) {
			throw new UISrvException(ex.getMessage());
		}
	
	}
	
	

}
