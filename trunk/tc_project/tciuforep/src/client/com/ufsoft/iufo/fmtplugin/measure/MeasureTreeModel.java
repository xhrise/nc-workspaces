package com.ufsoft.iufo.fmtplugin.measure;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.ReportCache;
import nc.ui.iufo.authorization.AuthorizMngBO_Client;
import nc.ui.iufo.resmng.OwnerDirRefBO_Client;
import nc.util.iufo.iufo.resmng.IIUFOResMngConsants;
import nc.util.iufo.pub.UFOString;
import nc.vo.iufo.authorization.ShareInfoVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.resmng.OwnerDirRefVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportDirVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.resource.StringResource;

public class MeasureTreeModel {
    MeasRefTreeNode rootNode;
    private KeyGroupVO m_oCurrentKeyGroupVO = null;
    MeasureCache measureCache = CacheProxy.getSingleton().getMeasureCache();
    KeyGroupCache keyGroupCache = CacheProxy.getSingleton().
        getKeyGroupCache();
    ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
    Comparator comparator = null;
    /**
     * ������¼�Ƿ���˽�б�������ǣ����ܲ���ȫ���ı�������˽��Ŀ¼������ǹ��б������������˽��Ŀ¼
     */
    private ReportVO curRepVo;
    private boolean isContainsCurrentReport = true;
    /**
     * 3.1����
     */
    private UserInfoVO  m_userVO;
    private boolean     m_bRepManager;

    /**
     containsCurRep ��ʾ�Ƿ�����������ָ��
     */

    public MeasureTreeModel(ReportVO repvo, KeyGroupVO groupvo,
                            boolean containsCurRep, String strUserPK, boolean bRepMgr) {

        setIsContainsCurrentReport(containsCurRep);
        this.m_oCurrentKeyGroupVO = groupvo;
        this.curRepVo = repvo;
        this.m_bRepManager = bRepMgr;
        //������
        if (this.curRepVo != null) {
            if (this.curRepVo.isModel()) {
                initModelTree();
            }else {
                this.m_userVO = CacheProxy.getSingleton().getUserCache().getUserById(strUserPK);
                initRepTree();
            }
        }
    }

    public MeasRefTreeNode getRootNode() {
        return rootNode;
    }

    private Comparator initComparator() {
        if (comparator == null) {
            comparator = new Comparator() {
                public int compare(Object a, Object b) {
                    String aObj, bObj;
                    if (a != null && b != null) {
                        if (a instanceof ReportDirVO) {
                            if (b instanceof ReportVO) {
                                return -1;
                            }
                            aObj = ( (ReportDirVO) a).getDirName();
                            bObj = ( (ReportDirVO) b).getDirName();
                            if(aObj == null || bObj == null) return 0;
                            return UFOString.compareHZString(aObj, bObj);
                        }
                        else {
                            if (b instanceof ReportDirVO) {
                                return 1;
                            }
                            aObj = ( (ReportVO) a).getCode();
                            bObj = ( (ReportVO) b).getCode();
                            if(aObj == null || bObj == null) return 0;
                            return UFOString.compareHZString(aObj, bObj);
                        }
                    }
                    return -1;
                }

            };
        }
        return comparator;
    }

    /**
     * ��ʼ����
     * �������ڣ�(2003-8-26 10:38:02)
     * @i18n miufohbbb00095=ָ�����װ��Ŀ¼������Ŀ¼��
     */
    private void initTree(ReportDirVO pDir, MeasRefTreeNode pNode) {
        try {            
            Vector subDir = pDir.getSubDirs();
            if (subDir != null) {
                Collections.sort(subDir, initComparator());
            }
            ReportVO[] reps = reportCache.getReportsByDirPK(pDir.getDirId());
            if (reps != null && reps.length > 0) {
                Arrays.sort(reps, initComparator());
            }
            int n = subDir == null ? 0 : subDir.size();
            int m = reps == null ? 0 : reps.length;

            MeasRefTreeNode node;

            //����б���򱨱�Ŀ¼
            ReportDirVO dirvo;
            ReportVO repvo;
            for (int i = 0; i < n + m; i++) {
                //�½�һ���ڵ�

                String name, pk;
                //�����Ŀ¼,���ȴ���Ŀ¼
                if (i < n) {
                	
                    dirvo = (ReportDirVO) subDir.get(i);
                    
                    node = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTDIR);
                    name = dirvo.getDirName();
                    pk = dirvo.getDirId();
                    node.setName(name);
                    node.setPk(pk);
                    initTree(dirvo, node);
                    //���½��Ľڵ�ӵ����ڵ���
                    pNode.addSubNode(node);
                }
                else {
                    node = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORT);
                    repvo = reps[i - n];
                    //�����ģ���ָ�겻�ܻ������
                    if (curRepVo.isModel() != repvo.isModel()) {
                        continue;
                    }
                    //ģ�����ֻ�ܲ��պ͵�ǰ����ͬһĿ¼�µ�ģ�壬����ģ��Ĺؼ������Ҫ��ͬ
                    if (curRepVo.isModel()) {
                        //�Ƚ�ģ��Ŀ¼����ͬ�򲻼ӵ�����
                        if (!curRepVo.getRepDir().equalsIgnoreCase(repvo.
                            getRepDir())) {
                            continue;
                        }
                    }
                    KeyGroupVO keyGroup = keyGroupCache.getByPK(repvo.
                        getKeyCombPK());
                    if (keyGroup == null) {
                        continue;
                    }
                    //�ؼ�������ж�
                    if ( (m_oCurrentKeyGroupVO == null &&
                          isContainsCurrentReport) ||
                        m_oCurrentKeyGroupVO != null &&
                        keyGroup.canContainsKeyGroup(m_oCurrentKeyGroupVO)) {
                        //���ر����ָ�겻�������
                        if (repvo.isHiddenReport()) {
                            continue;
                        }
                        //�����ر�����ָ��
                        if ( (!isContainsCurrentReport) &&
                            repvo.getReportPK().equals(curRepVo.getReportPK())) {
                            continue;
                        }
                        node.setName(repvo.getName());
                        node.setPk(repvo.getReportPK());
                        node.setReportCode(repvo.getCode());
                        //���½��Ľڵ�ӵ����ڵ���
                        pNode.addSubNode(node);
                    }
                }

            }
        }
        catch (Exception e) {
            AppDebug.debug(StringResource.getStringResource("miufohbbb00095") + pDir.getDirName());//@devTools System.out.println("ָ�����װ��Ŀ¼������Ŀ¼��" + pDir.getDirName());
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
        }
    }

    /**
     * �˴����뷽��������
     * �������ڣ�(2003-9-20 13:34:12)
     * @param isContains boolean
     */
    public void setIsContainsCurrentReport(boolean isContains) {
        isContainsCurrentReport = isContains;
    }

    /**
     * ����ָ���û�����Ȩ�޵ı���Ŀ¼��ʼ�����ṹ
     * @param strUserPK String �û�����
     */

    private void initRepTree()
    {
        //���챨��Ŀ¼����ڵ�,����Ϊ����Ŀ¼
    	rootNode = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTROOTDIR);
    	//"����Ŀ¼"
        rootNode.setName(StringResource.getStringResource("miufopublic364"));
        
        if( isRepManager()){
        	//�õ�����λ�ĸ�Ŀ¼��������
        	addMyUnitNode();
        }
        
        //����ָ���û���Ȩ�޵�����Ŀ¼�ڵ�
        addSharedNode();
    }

    /**
     * ���ݱ���ģ����Ϣ��ʼ�����ṹ
     * @param strUserPK String �û�����
     */
    private void initModelTree() {
        //������Ŀ¼
        ReportDirVO rootvo = CacheProxy.getSingleton().
            getReportDirCache().getRootReportDir(true);
        rootNode = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTROOTDIR);
        //����Ϊ����ģ��Ŀ¼
        rootNode.setName(StringResource.getStringResource(
            "uiufofn0012"));
        rootNode.setPk(rootvo.getDirId());

        //�ݹ�����¼�Ŀ¼
        initTree(rootvo, rootNode);
}


/**
 * �õ�ָ���û�����Ȩ��Ŀ¼,����λĿ¼����ǰ��
 * @return share�б�
 * @i18n miufohbbb00096=ָ�����װ��Ŀ¼�������޷��õ��û���Ȩ�޵�Ŀ¼
 */

private Vector getSharedDirsByUser()
{
    try {
        Vector       vecDir = new Vector();
        String       strUserPK = m_userVO.getID();
        ShareInfoVO[]  shareVOs = null;
        //TODO װ��Ŀ¼�ĵ�λPK,��Ϊnull���ʾʹ���û����ڵ�λ��Ȩ�ޣ�Ӧ���ǵ�ǰװ���ĸ���λĿ¼�������ĸ���λpkֵ������web�����г�������Ա���л���λ���ܣ�
        shareVOs = AuthorizMngBO_Client.getAuthorizedShares(
        		IIUFOResMngConsants.MODULE_REPORT_DIR,m_userVO.getUnitId(),
				strUserPK);
        
        if( shareVOs != null ){
        	if( isRepManager() ){//����û��Ǳ�����Դ������
	            for( int i=0; i<shareVOs.length; i++ ){
	                if( shareVOs[i] != null && !shareVOs[i].getShareObjOwnerPK().equals(m_userVO.getUnitId()) ){
	                    vecDir.add(shareVOs[i]);
	                }            	
	            }
	        }else { //��ͨ�û�
	            //������λ��Ŀ¼������ǰ��
	            for( int i=0; i<shareVOs.length; i++ ){
	                if( shareVOs[i] != null && shareVOs[i].getShareObjOwnerPK().equals(m_userVO.getUnitId()) ){
	                    vecDir.add(shareVOs[i]);
	                    shareVOs[i] = null;
	                }
	            }
	            //���Ǳ���λ��Ŀ¼���ӵ�vecDir��
	            for( int i=0; i<shareVOs.length; i++ ){
	                if( shareVOs[i] != null ){
	                    vecDir.add(shareVOs[i]);
	                }
	            }
	        }
        }
        return vecDir;
    }catch (Exception e) {
        AppDebug.debug(StringResource.getStringResource("miufohbbb00096"));//@devTools System.out.println("ָ�����װ��Ŀ¼�������޷��õ��û���Ȩ�޵�Ŀ¼");
AppDebug.debug(e);//@devTools         AppDebug.debug(e);
    }

    return null;
}
/**
 * ����ָ����λ�ĵ�λ�ڵ㣬�����λ���û��ĵ�λһ�£��ڵ������Ϊ������λĿ¼��������Ϊ��λ���ƣ���λ����
 * @param strUnit String ��λ����
 * @return MeasRefTreeNode
 */
private MeasRefTreeNode createUnitNode(String strUnit)
{
    MeasRefTreeNode unitNode = null;
    if( strUnit.equals(m_userVO.getUnitId()) ){
        unitNode = new MeasRefTreeNode(MeasureRefDlg.ICON_UNIT);
        //"����λĿ¼"
        unitNode.setName(StringResource.getStringResource("uiuforep000102"));
    }else{
        UnitInfoVO unitVO = CacheProxy.getSingleton().getUnitCache().
            getUnitInfoByPK(strUnit);
        if (unitVO != null) {
            unitNode = new MeasRefTreeNode(MeasureRefDlg.ICON_UNIT);
            unitNode.setName(unitVO.getUnitName() + "(" +
                             unitVO.getCode() + ")");
        }
    }
    return unitNode;
}
/**
 * ����ָ��Ŀ¼��Ŀ¼�ڵ㣬
 * @param repDirVO ReportDirVO������Ŀ¼VO
 * @return MeasRefTreeNode
 */
private MeasRefTreeNode createShareDirNode(ShareInfoVO shareVO)
{
    MeasRefTreeNode repDirNode =new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTDIR);
    repDirNode.setName(shareVO.getShareName());
    repDirNode.setPk(shareVO.getShareObjPK());
    return repDirNode;
}

 
 /**
 * �õ�ָ����λ�ı���Ŀ¼
 * @param strUnitID String
 * @return nc.vo.iufo.resourcemng.OwnerDirRefVO
 * @i18n miufohbbb00097=ָ�����װ��Ŀ¼�������޷��õ���λ
 * @i18n miufohbbb00098=��Ӧ��Ŀ¼
 */
 private OwnerDirRefVO getUnitRefDir(String strUnitID)
 {
     try{
         return OwnerDirRefBO_Client.getOwnerDirRef(IIUFOResMngConsants.MODULE_REPORT_DIR,
             strUnitID);
     }catch(Exception e){
         AppDebug.debug(StringResource.getStringResource("miufohbbb00097")+strUnitID+StringResource.getStringResource("miufohbbb00098"));//@devTools System.out.println("ָ�����װ��Ŀ¼�������޷��õ���λ"+strUnitID+"��Ӧ��Ŀ¼");
AppDebug.debug(e);//@devTools          AppDebug.debug(e);
     }
     return null;
 }
 
private boolean isRepManager()
{
	return m_bRepManager;
}
/**
 * ���ӱ���λĿ¼�ڵ�
 *
 */
private void addMyUnitNode(){
	//�õ�����λ�ĸ�Ŀ¼��������
	OwnerDirRefVO  unitOwnVO = getUnitRefDir(m_userVO.getUnitId());
	if( unitOwnVO != null){
		MeasRefTreeNode unitTreeNode = createUnitNode(m_userVO.getUnitId());
        if( unitTreeNode != null ){
            rootNode.addSubNode(unitTreeNode);
            ReportDirVO	unitDirVO = CacheProxy.getSingleton().getReportDirCache().getReportDir(unitOwnVO.getDirPK());
            if( unitDirVO != null){
            	initTree(unitDirVO, unitTreeNode);
            }
        }       		
	}	
}
/**
 * �����û���Ȩ�޵�Ŀ¼�ڵ�
 */
private void addSharedNode()
{
	//�õ��û�����Ȩ�޵�Ŀ¼��Ϣ
    Vector vecDir = getSharedDirsByUser();
    int   nDirs = 0;
    
    if( vecDir != null && (nDirs=vecDir.size()) >0 ){
        String  strUnit = null;
        MeasRefTreeNode  unitTreeNode = null;
        
        for( int i=0; i<nDirs; i++ ){
            ShareInfoVO  shareDirVO = (ShareInfoVO)vecDir.get(i);
            if( shareDirVO != null){
                if( strUnit == null || strUnit.equals(shareDirVO.getShareObjOwnerPK())== false ){
                    //������һ����λ��ڵ㣬�����ӵ����ڵ���
                    strUnit = shareDirVO.getShareObjOwnerPK();
                    unitTreeNode = createUnitNode(strUnit);
                    if( unitTreeNode != null ){
                        rootNode.addSubNode(unitTreeNode);
                    }
                }

                //�½�����Ŀ¼�ڵ�
                ReportDirVO  repDirVO = (ReportDirVO)CacheProxy.getSingleton().getReportDirCache().get(shareDirVO.getShareObjPK());
                if( repDirVO != null ){
	                MeasRefTreeNode  repDirNode = createShareDirNode(shareDirVO);
	                if( unitTreeNode != null ){
	                    unitTreeNode.addSubNode(repDirNode);
	                }else{
	                    rootNode.addSubNode(repDirNode);
	                }
	                initTree(repDirVO, repDirNode);
                }
            }
        }
    }

}
}


  