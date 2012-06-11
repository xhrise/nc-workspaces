/*
 * �������� 2006-1-13
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
 * ������ͱ���Ŀ¼�������ļ������ӿ�IFile4Dir��ʵ����
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
	 * ִ�в����������ļ�VO
	 * 
	 * from liuyy. 2006-04-30
     * �˷�ֻ֧�з�������Ϊ������ʹ��,liulp 200-05-29
	 * @param strSrcVOPK
	 * @param strDstParentDirPK
	 * @param strOperUserPK
	 * @return
	 */
	protected ValueObject doCopyFileVO(String strSrcVOPK,
			String strDstParentDirPK, String strOperUserPK,String strOrgPK) throws UISrvException{
		//���Ʊ���
		ReportVO copyedRepVO = null;
        ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
        UserCache userCache = BDCacheManager.getUserCache(true);
        if(IIUFOResMngConsants.MODULE_ANALYSIS_REPORT.equals(getModuleID())){
            //�˷�ֻ֧�з�������Ϊ������ʹ��, ��zhuyfȷ��,���̺ͱ�����Ϊ����������һ��.
            //����˴�����Rep2AnaCopyUI�ĵ��÷�ʽ.
            //liuyy. 2005-11-30
            String strCurUnitId = userCache.getUserById(strOperUserPK).getUnitId(); 
    
            //Դ����IDs
            String[] strSrcReportIds = null;
            //Դ����Ids
            strSrcReportIds = new String[]{strSrcVOPK};
          
            //Ŀ�ĵ�λId
            
            String strDestUnitId = ReportUtil.getUnitIdByRepDirId(strDstParentDirPK, IIUFOResMngConsants.MODULE_ANALYSIS_REPORT);
            //׼������
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
            //����UI�㱨�������ʽ����ˢ��
            repCache.refresh();
            IUFOUICacheManager.getSingleton().getRepFormatCache().refresh();
        }else if(IIUFOResMngConsants.MODULE_REPORT_DIR.equals(getModuleID())){
            String strCurUnitId = userCache.getUserById(strOperUserPK).getUnitId(); 
            //Ŀ�ĵ�λId
            String strDestUnitId = ReportUtil.getUnitIdByRepDirId(strDstParentDirPK, IIUFOResMngConsants.MODULE_REPORT_DIR);
            //Դ��λId
            ReportVO srcRepVO = (ReportVO)repCache.get(strSrcVOPK);
            String strSrcUnitId =  ReportUtil.getUnitIdByRepDirId(srcRepVO.getRepDir(), IIUFOResMngConsants.MODULE_REPORT_DIR);;
            //Ŀ��Ŀ¼Id
            String strDestDirId = ResMngToolKit.getVOIDByTreeObjectID(strDstParentDirPK);

            //Դ����Ids
            String[] strSrcReportIds = new String[]{strSrcVOPK};
            boolean isCreateMeasure = true;//����Ŀ¼ʱ
            //Ŀ�굥λ�����ӵ�λ����, ������Ŀ�굥λ����
            String[]    arrSubUnitIds = ReportUtil.getSubUnitIds(strDestUnitId,strOrgPK);
            
            //׼������
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
                    throw new UISrvException("uiuforesmng0065");//������ʧ�ܣ�
                } finally {
                    //����UI�㻺��ˢ��
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
                throw new UISrvException("uiufo70022");//�������ڸ���
            }
        }
		return copyedRepVO;
	}
	
	/* ���� Javadoc��
	 * @see nc.ui.iufo.resmng.dir.IFiled#moveFileVO(java.lang.String, java.lang.String)
	 */
	public void moveFileVO(String strSrcFileVOPK, String strDstDirVOPK) throws UISrvException {
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		ReportVO[] repVOs = repCache.getByPks(new String[] { strSrcFileVOPK });
		if (repVOs == null || repVOs.length <= 0 || repVOs[0] == null) {
			return;
		}

		//���±����Ŀ¼ID��Ϣ
		ReportVO movedRepVO = (ReportVO) repVOs[0].clone();
		movedRepVO.setRepDir(strDstDirVOPK);
		try {
			repCache.updateRepBaseInfo(movedRepVO);
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new UISrvException(e.getMessage());
		}			
	}
	/* ���� Javadoc��
	 * @see nc.ui.iufo.resmng.dir.IFiled#getSubFileVOs(java.lang.String)
	 */
	public ValueObject[] getSubFileVOs(String strDirVOPK) throws UISrvException {
		//���ָ��Ŀ¼VOPK������ֱ�����ļ�VO����
		if (strDirVOPK == null) {
			return null;
		}

		ReportVO[] repVOs = null;
		ReportCache reportCache = IUFOUICacheManager.getSingleton()
				.getReportCache();
		repVOs = reportCache.getReportsByDirPK(strDirVOPK);
		if(repVOs != null){
			// ��������ģ�壺ȱʡ���ձ���������򣻷�����ȱʡ���ձ�����������
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
	/* ���� Javadoc��
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

			//����������
			nIndex++;
			strValues[nIndex] = reportVO.getName();
			//�û�����,����û���ɾ��������ʾ��
			String strUserName = "";
			UserCache userCache = IUFOUICacheManager.getSingleton().getUserCache();
			UserInfoVO userVO = (nc.vo.iufo.user.UserInfoVO) userCache
					.get(reportVO.getUserPK());
			if (userVO != null) {
				strUserName = userVO.getStrName();
			}
			nIndex++;
			strValues[nIndex] = strUserName;
			//����ʱ��
			nIndex++;
			strValues[nIndex] = reportVO.getTime();
			//�޸�ʱ��
			nIndex++;
			strValues[nIndex] = reportVO.getModifiedTime();
			//������˵��
			nIndex++;
			strValues[nIndex] = reportVO.getNote();

		} else if (strModuleName.equals(IIUFOResMngConsants.MODULE_REPORT_DIR)
				|| strModuleName.equals(IIUFOResMngConsants.MODULE_REPORT_MODEL) ) {
			boolean bRepDir=strModuleName.equals(IIUFOResMngConsants.MODULE_REPORT_DIR);
			strValues = new Object[7+(bRepDir?1:0)];
			nIndex = 0;
			//select
			strValues[nIndex] = strFileTreeObjID;

			//�������
			nIndex++;
			strValues[nIndex] = reportVO.getCode();
			//��������
			nIndex++;
			strValues[nIndex] = reportVO.getName();
			//�û�����,����û���ɾ��������ʾ��
			String strUserName = "";
			UserCache userCache = IUFOUICacheManager.getSingleton().getUserCache();
			UserInfoVO userVO = (nc.vo.iufo.user.UserInfoVO) userCache
					.get(reportVO.getUserPK());
			if (userVO != null) {
				strUserName = userVO.getStrName();
			}
			nIndex++;
			strValues[nIndex] = strUserName;
			//����ʱ��
			nIndex++;
			strValues[nIndex] = reportVO.getTime();
			//�޸�ʱ��
			nIndex++;
			strValues[nIndex] = reportVO.getModifiedTime();
			//����˵��
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
	
	/* ���� Javadoc��
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
	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCreateFileVO(nc.vo.pub.ValueObject)
	 */
	protected ValueObject doCreateFileVO(ValueObject toCreateVO,String strOrgPK) throws UISrvException {
//		//��������ʱ��Ҫ��鱨��ռ����� liuyy 2004-11-08
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
	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCheckFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doCheckFileVO(ValueObject toCreateVO) throws UISrvException {
        if (toCreateVO == null) {
        return;
    }
        ReportVO reportVO = (ReportVO) toCreateVO;
        //�Ǳ���(�򱨱�ģ��)ģ�黹�Ƿ�����ģ��
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
        //�½�
        if (oldRepVOs == null || oldRepVOs.length <= 0 || oldRepVOs[0] == null) {
        }
        //�޸�
        else {
            if (oldRepVOs[0].getName().equals(strRepName)) {
                bSameDirName = true;
            }
            bSameDirCode = true;
            //�޸ĵķ�����VO��û�д���code����ֵ����Ҫ����VO��Codeֵ��ֵ
            strRepCode = oldRepVOs[0].getCode();
        }

        //1,����������ͬһĿ¼�ﲻ���ظ�
        if (!bSameDirName) {
            bFind = reportCache.isExistRepNameByDir(strRepName,
                    strRepDirPK);
            if (bFind) {
                //ͬһĿ¼�£����������ظ���
                throw new UISrvException("miufo131");
            }
        }
        //2.�������ȫ��Ψһ
        if (!bSameDirCode) {            
                //���ɷ�����ı������
                String strTryNewRepCode = null;
                int nTryCount = 3;
                for (int i = 0; i < nTryCount; i++) {
                    strTryNewRepCode = IDMaker.makeID(s_nCodeLen);
                    bFind = reportCache.isExistRepByRepCode(strTryNewRepCode, false);
                    if (!bFind) {
                        //��֤Ψһ��������ѭ����
                        strRepCode = strTryNewRepCode;
                        break;
                    }
                }
                if (bFind) {
                    //���������������3�ξ�ʧ�ܣ�
                    throw new UISrvException("miufo1003931"); //"ϵͳ�������������룩���������ԣ�"
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

		//�½�
		if (oldRepVOs == null || oldRepVOs.length <= 0 || oldRepVOs[0] == null) {
		}
		//�޸�
		else {
			if (oldRepVOs[0].getName().equals(strRepName)) {
				bSameDirName = true;
			}
			if (oldRepVOs[0].getCode().equals(strRepCode)) {
				bSameDirCode = true;
			}
		}
        //1,����������ͬһĿ¼�ﲻ���ظ�
		if (!bSameDirName) {
			bFind = reportCache.isExistRepNameByDir(strRepName,
                    strRepDirPK);
			if (bFind) {
				//ͬһĿ¼�£����������ظ���
				throw new UISrvException("miufo131");
			}
		}
		//2.�������ȫ��Ψһ
		if (!bSameDirCode) {
			//liuyy 2004-11-04
			bFind = reportCache.isExistRepByRepCode(strRepCode,
                    IIUFOResMngConsants.MODULE_REPORT_MODEL.equals(strModuleID));
			if (bFind) {
				throw new UISrvException("uiuforesmng0054"); //"��������Ѵ��ڣ����������룡"
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
	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doUpdateFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doUpdateFileVO(ValueObject fileVO,String strOrgPK) throws UISrvException {
		ReportVO repVO = (ReportVO)fileVO;
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		try {
			//���²���
			repCache.updateRepBaseInfo(repVO);
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			throw new UISrvException(e.getMessage());
		}
		
	}
	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doRemoveFileVOs(java.lang.String[])
	 */
	protected void doRemoveFileVOs(String[] strSrcVOPKs) throws UISrvException {
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		
		//�ж��Ƿ���ϵͳԤ�Ƶı���
		ReportVO[]	repVOs = repCache.getByPks(strSrcVOPKs);
		if( repVOs != null ){
			for( int i=0; i<repVOs.length; i++ ){
				if( repVOs[i]!= null && repVOs[i].isBuiltIn() ){
					throw new UISrvException("uiufo70039");//"ϵͳԤ�Ƶ�ģ�岻��ɾ��"
				}
			}
		}
		
		//ɾ������򱨱�Ŀ¼ʱ�ж��Ƿ���������.
		if(RepDirectoriedImpl.checkTaskRef(strSrcVOPKs)){
			throw new UISrvException("uiufo70018");//�����������ã�ɾ��ʧ�ܣ�
		}
		
		try {			
			//ɾ��һ�鱨��
			repCache.removeReportByIds(strSrcVOPKs);
//			ReportBO_Client.removeReport(strSrcVOPKs);			
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			throw new UISrvException(e.getMessage());
		}		
	}
	/* ���� Javadoc��
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
