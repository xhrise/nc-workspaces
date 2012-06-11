/*
 * 创建日期 2006-1-13
 *
 */
package nc.ui.iufo.analysisrep;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.BDCacheManager;
import nc.pub.iufo.cache.base.ICacheObject;
import nc.pub.iufo.cache.base.UserCache;
import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.iuforeport.rep.ReportBO_Client;
import nc.util.iufo.iufo.resmng.IIUFOResMngConsants;
import nc.util.iufo.iufo.resmng.IUFOFiled4DirImpl;
import nc.util.iufo.pub.IDMaker;
import nc.util.iufo.report.ReportUtil;
import nc.util.iufo.resmng.IFiled4Dir;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.WebImage;
import com.ufida.web.util.WebGlobalValue;

/**
 * 分析表和报表目录：关于文件操作接口IFile4Dir的实现类
 * @author liulp
 *
 */
public class RepFiledImpl extends IUFOFiled4DirImpl  implements IFiled4Dir{
	private static final long serialVersionUID = 3504697704401120487L;
	protected static int s_nCodeLen = 30;
	
	public RepFiledImpl(String strModuleID){
		super(strModuleID);
	}
	/**
	 * 执行操作：复制文件VO
	 * 
	 * from liuyy. 2006-04-30
     * 此分支只有分析表复制为分析表使用,liulp 200-05-29
	 * @param strSrcVOPK
	 * @param strDstParentDirPK
	 * @param strOperUserPK
	 * @return
	 */
	protected ValueObject doCopyFileVO(String strSrcVOPK,
			String strDstParentDirPK, String strOperUserPK,String strOrgPK) throws UISrvException{
		//复制报表
		ReportVO copyedRepVO = null;
        ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
        UserCache userCache = BDCacheManager.getUserCache(true);
        if(IIUFOResMngConsants.MODULE_ANALYSIS_REPORT.equals(getModuleID())){
            //此分支只有分析表复制为分析表使用, 与zhuyf确认,流程和报表复制为分析表流程一样.
            //因而此处引用Rep2AnaCopyUI的调用方式.
            //liuyy. 2005-11-30
            String strCurUnitId = userCache.getUserById(strOperUserPK).getUnitId(); 
    
            //源报表IDs
            String[] strSrcReportIds = null;
            //源报表Ids
            strSrcReportIds = new String[]{strSrcVOPK};
          
            //目的单位Id
            
            String strDestUnitId = ReportUtil.getUnitIdByRepDirId(strDstParentDirPK, IIUFOResMngConsants.MODULE_ANALYSIS_REPORT);
            //准备数据
            Hashtable<String,Object> hash = new Hashtable<String,Object>();
            hash.put("srcUnitId", "");
            hash.put("strCurUnitId", strCurUnitId);
            hash.put("destUnitId", strDestUnitId);
            hash.put("destDirId", strDstParentDirPK);
            hash.put("srcReportIds", strSrcReportIds);
            hash.put("userId", strOperUserPK);
            hash.put("transType", new Integer(
                            ReportUtil.REP_COPY_FOR_ANALYSIS));
            hash.put("strModuleName", IIUFOResMngConsants.MODULE_ANALYSIS_REPORT);
    
    
            try {
                ReportBO_Client.transReports(hash);
            } catch (Exception e) {
AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
            }
            
    //      try {
    //          copyedRepVO = ReportBO_Client.copyReport(strSrcVOPK,
    //                  strDstParentDirPK, strOperUserPK);
    //      } catch (Exception e) {
    //          e.printStackTrace(System.out);
    //          throw new ResMngException(e.getMessage());
    //      }
            //处理UI层报表、报表格式缓存刷新
            repCache.refresh();
            IUFOUICacheManager.getSingleton().getRepFormatCache().refresh();
        }else if(IIUFOResMngConsants.MODULE_REPORT_DIR.equals(getModuleID())){
            String strCurUnitId = userCache.getUserById(strOperUserPK).getUnitId(); 
            //目的单位Id
            String strDestUnitId = ReportUtil.getUnitIdByRepDirId(strDstParentDirPK, IIUFOResMngConsants.MODULE_REPORT_DIR);
            //源单位Id
            ReportVO srcRepVO = (ReportVO)repCache.get(strSrcVOPK);
            String strSrcUnitId =  ReportUtil.getUnitIdByRepDirId(srcRepVO.getRepDir(), IIUFOResMngConsants.MODULE_REPORT_DIR);;
            //目标目录Id
            String strDestDirId = ResMngToolKit.getVOIDByTreeObjectID(strDstParentDirPK);

            //源报表Ids
            String[] strSrcReportIds = new String[]{strSrcVOPK};
            boolean isCreateMeasure = true;//复制目录时
            //目标单位所有子单位集合, 不包含目标单位本身
            String[]    arrSubUnitIds = ReportUtil.getSubUnitIds(strDestUnitId,strOrgPK);
            
            //准备数据
            Hashtable<String,Object> hash = new Hashtable<String,Object>();
            hash.put("srcUnitId", strSrcUnitId);
            hash.put("strCurUnitId", strCurUnitId);     
            hash.put("destUnitId", strDestUnitId);
            hash.put("destDirId", strDestDirId);
            hash.put("srcReportIds", strSrcReportIds);
            hash.put("userId", strOperUserPK);
            hash.put("isCreateMeasure", new Boolean(isCreateMeasure));
            hash.put("subUnitIds", arrSubUnitIds);
            if (isCreateMeasure) {
                hash.put("transType", new Integer(ReportUtil.REP_COPY_BY_CREATE));
            } else {
                hash.put("transType", new Integer(ReportUtil.REP_COPY_BY_REF));
            }

            if (!ReportUtil.isRunning()) {
                try {
                    ReportUtil.setRunning(true);
                    ReportBO_Client.transReports(hash);
                } catch (Exception e) {
AppDebug.debug(e);//@devTools                     AppDebug.debug(e);
                    throw new UISrvException("uiuforesmng0065");//报表复制失败！
                } finally {
                    //处理UI层缓存刷新
                    nc.ui.iufo.cache.IUFOUICacheManager.getSingleton().getReportCache()
                            .refresh();
                    nc.ui.iufo.cache.IUFOUICacheManager.getSingleton()
                            .getRepFormatCache().refresh();
                    nc.ui.iufo.cache.IUFOUICacheManager.getSingleton()
                            .getMeasureCache().refresh();
                    nc.ui.iufo.cache.IUFOUICacheManager.getSingleton()
                            .getKeywordCache().refresh();
                    nc.ui.iufo.cache.IUFOUICacheManager.getSingleton()
                            .getKeyGroupCache().refresh();
                    
                    ReportUtil.setRunning(false);               
                }
            } else {
                throw new UISrvException("uiufo70022");//报表正在复制
            }
        }
		return copyedRepVO;
	}
	
	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.dir.IFiled#moveFileVO(java.lang.String, java.lang.String)
	 */
	public void moveFileVO(String strSrcFileVOPK, String strDstDirVOPK) throws UISrvException {
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		ReportVO[] repVOs = repCache.getByPks(new String[] { strSrcFileVOPK });
		if (repVOs == null || repVOs.length <= 0 || repVOs[0] == null) {
			return;
		}

		//更新报表的目录ID信息
		ReportVO movedRepVO = (ReportVO) repVOs[0].clone();
		movedRepVO.setRepDir(strDstDirVOPK);
		try {
			repCache.updateRepBaseInfo(movedRepVO);
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new UISrvException(e.getMessage());
		}			
	}
	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.dir.IFiled#getSubFileVOs(java.lang.String)
	 */
	public ValueObject[] getSubFileVOs(String strDirVOPK) throws UISrvException {
		//获得指定目录VOPK的所有直接子文件VO数组
		if (strDirVOPK == null) {
			return null;
		}

		ReportVO[] repVOs = null;
		ReportCache reportCache = IUFOUICacheManager.getSingleton()
				.getReportCache();
		repVOs = reportCache.getReportsByDirPK(strDirVOPK);
		if(repVOs != null){
			// 报表，报表模板：缺省按照报表编码排序；分析表：缺省按照报表名称排序
			if (!IIUFOResMngConsants.MODULE_ANALYSIS_REPORT
					.equals(getModuleID())) {
				Arrays.sort(repVOs, new Comparator<ReportVO>() {
					public int compare(ReportVO o1, ReportVO o2) {
						if (o1 == null)
							return -1;
						if (o2 == null)
							return 1;
						return o1.getCode().compareTo(o2.getCode());
					}
				});
			} else {
				Arrays.sort(repVOs, new Comparator<ReportVO>() {
					public int compare(ReportVO o1, ReportVO o2) {
						if (o1 == null)
							return -1;
						if (o2 == null)
							return 1;
						return o1.getName().compareTo(o2.getName());
					}
				});
			}
		}
		return repVOs;
	}
	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileDisAttr(java.lang.String, nc.vo.pub.ValueObject)
	 */
	protected Object[] doGetFileDisAttr(String strFileTreeObjID, ValueObject fileVO) {
		if(strFileTreeObjID == null || fileVO == null){
			return null;
		}
		
		ReportVO reportVO = (ReportVO) fileVO;
		Object[] strValues = null;
		int nIndex = 0;
		String strModuleName = getModuleID();
		if (strModuleName.equals(IIUFOResMngConsants.MODULE_ANALYSIS_REPORT)) {

			strValues = new Object[6];
			nIndex = 0;
			//select
			strValues[nIndex] = strFileTreeObjID;

			//分析表名称
			nIndex++;
			strValues[nIndex] = reportVO.getName();
			//用户名称,如果用户已删除，则显示空
			String strUserName = "";
			UserCache userCache = IUFOUICacheManager.getSingleton().getUserCache();
			UserInfoVO userVO = (nc.vo.iufo.user.UserInfoVO) userCache
					.get(reportVO.getUserPK());
			if (userVO != null) {
				strUserName = userVO.getStrName();
			}
			nIndex++;
			strValues[nIndex] = strUserName;
			//创建时间
			nIndex++;
			strValues[nIndex] = reportVO.getTime();
			//修改时间
			nIndex++;
			strValues[nIndex] = reportVO.getModifiedTime();
			//分析表说明
			nIndex++;
			strValues[nIndex] = reportVO.getNote();

		} else if (strModuleName.equals(IIUFOResMngConsants.MODULE_REPORT_DIR)
				|| strModuleName.equals(IIUFOResMngConsants.MODULE_REPORT_MODEL) ) {
			boolean bRepDir=strModuleName.equals(IIUFOResMngConsants.MODULE_REPORT_DIR);
			strValues = new Object[7+(bRepDir?1:0)];
			nIndex = 0;
			//select
			strValues[nIndex] = strFileTreeObjID;

			//报表编码
			nIndex++;
			strValues[nIndex] = reportVO.getCode();
			//报表名称
			nIndex++;
			strValues[nIndex] = reportVO.getName();
			//用户名称,如果用户已删除，则显示空
			String strUserName = "";
			UserCache userCache = IUFOUICacheManager.getSingleton().getUserCache();
			UserInfoVO userVO = (nc.vo.iufo.user.UserInfoVO) userCache
					.get(reportVO.getUserPK());
			if (userVO != null) {
				strUserName = userVO.getStrName();
			}
			nIndex++;
			strValues[nIndex] = strUserName;
			//创建时间
			nIndex++;
			strValues[nIndex] = reportVO.getTime();
			//修改时间
			nIndex++;
			strValues[nIndex] = reportVO.getModifiedTime();
			//报表说明
			nIndex++;
			strValues[nIndex] = reportVO.getNote();
			
			if (bRepDir){
				nIndex++;
				ActionForward fwd=new ActionForward(ViewTaskOfRepAction.class.getName(),"");
				fwd.addParameter(WebGlobalValue.TABLE_SELECTED_ID,reportVO.getReportPK());
				WebImage image = new WebImage(WebGlobalValue.IMAGE_PATH+ "/iedit.gif");
				image.setOnMouseOver("this.style.cursor=\"hand\"");
				image.setOnMouseOut("this.style.cursor=\"auto\"");
				image.setOnClick("openWindow(\"" + fwd.genURI()+ "\");");
				strValues[nIndex]= image;
			}
		}

		return strValues;
	}
	
	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileVO(java.lang.String)
	 */
	protected ValueObject doGetFileVO(String strSrcVOPK) throws UISrvException {
		ReportCache reportCache = IUFOUICacheManager.getSingleton()
		.getReportCache();
		ReportVO[] repVOs = reportCache.getByPks(new String[] { strSrcVOPK });
		if (repVOs != null && repVOs.length >= 1 && repVOs[0] != null) {
			return repVOs[0];
		}
		return null;
	}
	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCreateFileVO(nc.vo.pub.ValueObject)
	 */
	protected ValueObject doCreateFileVO(ValueObject toCreateVO,String strOrgPK) throws UISrvException {
//		//创建报表时需要检查报表空间数量 liuyy 2004-11-08
//		if (getModuleName().equals(IResMngConsants.MODULE_REPORT_DIR)) {
//
//		}
		if(toCreateVO == null){
			return null;
		}
		
		ReportCache reportCache = IUFOUICacheManager.getSingleton()
				.getReportCache();		
		try {
			return (ReportVO) reportCache.add((ICacheObject) toCreateVO);
//        } catch (SQLException ex) {
//			ex.printStackTrace(System.out);
//            throw new UISrvException("miufoRepMng002");
 		}catch(UFOSrvException e){
AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
            throw new UISrvException(e.getCause().getMessage());
            
        }
        catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			throw new UISrvException(e.getMessage());
		}
	}
	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCheckFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doCheckFileVO(ValueObject toCreateVO) throws UISrvException {
        if (toCreateVO == null) {
        return;
    }
        ReportVO reportVO = (ReportVO) toCreateVO;
        //是报表(或报表模板)模块还是分析表模块
        boolean bAnalysisModule = isAnalysisModule(getModuleID());
        if(!bAnalysisModule){
            chekRepBizRule(getModuleID(),reportVO.getReportPK(),reportVO.getName(),reportVO.getCode(),reportVO.getRepDir());
        }else{
            String strRepCode = chekAnaRepBizRule(getModuleID(),reportVO.getReportPK(),reportVO.getName(),reportVO.getRepDir());
            reportVO.setCode(strRepCode);
        }        
	}
    /**
     * @param strModuleID
     * @param toCreateVO
     * @throws UISrvException
     */
    public static String chekAnaRepBizRule(String strModuleID,
            String strReportPK,
            String strRepName,
            String strRepDirPK) throws UISrvException {
        if (strModuleID == null) {
            return null;
        }
        ReportCache reportCache = IUFOUICacheManager.getSingleton()
                .getReportCache();

        boolean bFind = false;
        boolean bSameDirName = false;
        boolean bSameDirCode = false;
        ReportVO[] oldRepVOs = reportCache.getByPks(new String[] {strReportPK });
        String strRepCode = null;
        //新建
        if (oldRepVOs == null || oldRepVOs.length <= 0 || oldRepVOs[0] == null) {
        }
        //修改
        else {
            if (oldRepVOs[0].getName().equals(strRepName)) {
                bSameDirName = true;
            }
            bSameDirCode = true;
            //修改的分析表VO里没有传递code属性值，需要将旧VO的Code值赋值
            strRepCode = oldRepVOs[0].getCode();
        }

        //1,报表名称在同一目录里不能重复
        if (!bSameDirName) {
            bFind = reportCache.isExistRepNameByDir(strRepName,
                    strRepDirPK);
            if (bFind) {
                //同一目录下，报表名称重复！
                throw new UISrvException("miufo131");
            }
        }
        //2.报表编码全局唯一
        if (!bSameDirCode) {            
                //生成分析表的报表编码
                String strTryNewRepCode = null;
                int nTryCount = 3;
                for (int i = 0; i < nTryCount; i++) {
                    strTryNewRepCode = IDMaker.makeID(s_nCodeLen);
                    bFind = reportCache.isExistRepByRepCode(strTryNewRepCode, false);
                    if (!bFind) {
                        //保证唯一，可跳出循环！
                        strRepCode = strTryNewRepCode;
                        break;
                    }
                }
                if (bFind) {
                    //分析报表编码生成3次均失败！
                    throw new UISrvException("miufo1003931"); //"系统创建分析表（编码）错误，请重试！"
                }
        }
        return  strRepCode;        
    }
    /**
     * @param strModuleID
     * @param toCreateVO
     * @throws UISrvException
     */
    public static void chekRepBizRule(String strModuleID,String strReportPK,String strRepName,String strRepCode,String strRepDirPK) throws UISrvException {
        if (strModuleID == null) {
            return;
        }
        ReportCache reportCache = IUFOUICacheManager.getSingleton()
				.getReportCache();


		boolean bFind = false;
		boolean bSameDirName = false;
		boolean bSameDirCode = false;
		ReportVO[] oldRepVOs = reportCache.getByPks(new String[] {strReportPK });

		//新建
		if (oldRepVOs == null || oldRepVOs.length <= 0 || oldRepVOs[0] == null) {
		}
		//修改
		else {
			if (oldRepVOs[0].getName().equals(strRepName)) {
				bSameDirName = true;
			}
			if (oldRepVOs[0].getCode().equals(strRepCode)) {
				bSameDirCode = true;
			}
		}
        //1,报表名称在同一目录里不能重复
		if (!bSameDirName) {
			bFind = reportCache.isExistRepNameByDir(strRepName,
                    strRepDirPK);
			if (bFind) {
				//同一目录下，报表名称重复！
				throw new UISrvException("miufo131");
			}
		}
		//2.报表编码全局唯一
		if (!bSameDirCode) {
			//liuyy 2004-11-04
			bFind = reportCache.isExistRepByRepCode(strRepCode,
                    IIUFOResMngConsants.MODULE_REPORT_MODEL.equals(strModuleID));
			if (bFind) {
				throw new UISrvException("uiuforesmng0054"); //"报表编码已存在！请重新输入！"
			}
		}
    }
    /**
     * @param strModuleID
     * @return
     */
    public static boolean isAnalysisModule(String strModuleID) {
        boolean bAnalysisModule = false;
		if( IIUFOResMngConsants.MODULE_ANALYSIS_REPORT.equals(strModuleID)) {
			bAnalysisModule = true;
		}
        return bAnalysisModule;
    }
	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doUpdateFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doUpdateFileVO(ValueObject fileVO,String strOrgPK) throws UISrvException {
		ReportVO repVO = (ReportVO)fileVO;
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		try {
			//更新操作
			repCache.updateRepBaseInfo(repVO);
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			throw new UISrvException(e.getMessage());
		}
		
	}
	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doRemoveFileVOs(java.lang.String[])
	 */
	protected void doRemoveFileVOs(String[] strSrcVOPKs) throws UISrvException {
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		
		//判断是否是系统预制的报表
		ReportVO[]	repVOs = repCache.getByPks(strSrcVOPKs);
		if( repVOs != null ){
			for( int i=0; i<repVOs.length; i++ ){
				if( repVOs[i]!= null && repVOs[i].isBuiltIn() ){
					throw new UISrvException("uiufo70039");//"系统预制的模板不能删除"
				}
			}
		}
		
		//删除报表或报表目录时判断是否被任务引用.
		if(RepDirectoriedImpl.checkTaskRef(strSrcVOPKs)){
			throw new UISrvException("uiufo70018");//报表被任务引用，删除失败！
		}
		
		try {			
			//删除一组报表
			repCache.removeReportByIds(strSrcVOPKs);
//			ReportBO_Client.removeReport(strSrcVOPKs);			
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			throw new UISrvException(e.getMessage());
		}		
	}
	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doSetParentDirVOPK(nc.vo.pub.ValueObject, java.lang.String)
	 */
	protected void doSetParentDirVOPK(ValueObject fileVO, String strParentDirVOPK) throws UISrvException {
		if(fileVO == null){
			return;
		}
		
		ReportVO repVO = (ReportVO)fileVO;
		repVO.setRepDir(strParentDirVOPK);
	}
	


}
