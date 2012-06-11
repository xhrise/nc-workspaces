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
     * 用来记录是否是私有报表，如果是，则能参照全部的报表，包括私有目录，如果是公有报表，则不允许参照私有目录
     */
    private ReportVO curRepVo;
    private boolean isContainsCurrentReport = true;
    /**
     * 3.1增加
     */
    private UserInfoVO  m_userVO;
    private boolean     m_bRepManager;

    /**
     containsCurRep 表示是否包含本报标的指标
     */

    public MeasureTreeModel(ReportVO repvo, KeyGroupVO groupvo,
                            boolean containsCurRep, String strUserPK, boolean bRepMgr) {

        setIsContainsCurrentReport(containsCurRep);
        this.m_oCurrentKeyGroupVO = groupvo;
        this.curRepVo = repvo;
        this.m_bRepManager = bRepMgr;
        //生成树
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
     * 初始化树
     * 创建日期：(2003-8-26 10:38:02)
     * @i18n miufohbbb00095=指标参照装载目录数出错！目录：
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

            //如果有报表或报表目录
            ReportDirVO dirvo;
            ReportVO repvo;
            for (int i = 0; i < n + m; i++) {
                //新建一个节点

                String name, pk;
                //如果有目录,则先处理目录
                if (i < n) {
                	
                    dirvo = (ReportDirVO) subDir.get(i);
                    
                    node = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTDIR);
                    name = dirvo.getDirName();
                    pk = dirvo.getDirId();
                    node.setName(name);
                    node.setPk(pk);
                    initTree(dirvo, node);
                    //将新建的节点加倒父节点中
                    pNode.addSubNode(node);
                }
                else {
                    node = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORT);
                    repvo = reps[i - n];
                    //报表和模板的指标不能互相参照
                    if (curRepVo.isModel() != repvo.isModel()) {
                        continue;
                    }
                    //模板参照只能参照和当前报表同一目录下的模板，并且模板的关键字组合要相同
                    if (curRepVo.isModel()) {
                        //比较模板目录，不同则不加到树中
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
                    //关键字组合判断
                    if ( (m_oCurrentKeyGroupVO == null &&
                          isContainsCurrentReport) ||
                        m_oCurrentKeyGroupVO != null &&
                        keyGroup.canContainsKeyGroup(m_oCurrentKeyGroupVO)) {
                        //隐藏报表的指标不允许参照
                        if (repvo.isHiddenReport()) {
                            continue;
                        }
                        //不加载本报表指标
                        if ( (!isContainsCurrentReport) &&
                            repvo.getReportPK().equals(curRepVo.getReportPK())) {
                            continue;
                        }
                        node.setName(repvo.getName());
                        node.setPk(repvo.getReportPK());
                        node.setReportCode(repvo.getCode());
                        //将新建的节点加倒父节点中
                        pNode.addSubNode(node);
                    }
                }

            }
        }
        catch (Exception e) {
            AppDebug.debug(StringResource.getStringResource("miufohbbb00095") + pDir.getDirName());//@devTools System.out.println("指标参照装载目录数出错！目录：" + pDir.getDirName());
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
        }
    }

    /**
     * 此处插入方法描述。
     * 创建日期：(2003-9-20 13:34:12)
     * @param isContains boolean
     */
    public void setIsContainsCurrentReport(boolean isContains) {
        isContainsCurrentReport = isContains;
    }

    /**
     * 根据指定用户的有权限的报表目录初始化树结构
     * @param strUserPK String 用户编码
     */

    private void initRepTree()
    {
        //构造报表目录虚根节点,名称为报表目录
    	rootNode = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTROOTDIR);
    	//"报表目录"
        rootNode.setName(StringResource.getStringResource("miufopublic364"));
        
        if( isRepManager()){
        	//得到本单位的根目录，并创建
        	addMyUnitNode();
        }
        
        //增加指定用户有权限的所有目录节点
        addSharedNode();
    }

    /**
     * 根据报表模板信息初始化树结构
     * @param strUserPK String 用户编码
     */
    private void initModelTree() {
        //创建根目录
        ReportDirVO rootvo = CacheProxy.getSingleton().
            getReportDirCache().getRootReportDir(true);
        rootNode = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTROOTDIR);
        //名称为报表模板目录
        rootNode.setName(StringResource.getStringResource(
            "uiufofn0012"));
        rootNode.setPk(rootvo.getDirId());

        //递归加载下级目录
        initTree(rootvo, rootNode);
}


/**
 * 得到指定用户被授权的目录,本单位目录在最前面
 * @return share列表
 * @i18n miufohbbb00096=指标参照装载目录数出错！无法得到用户有权限的目录
 */

private Vector getSharedDirsByUser()
{
    try {
        Vector       vecDir = new Vector();
        String       strUserPK = m_userVO.getID();
        ShareInfoVO[]  shareVOs = null;
        //TODO 装载目录的单位PK,暂为null则表示使用用户所在单位的权限，应该是当前装载哪个单位目录，就用哪个单位pk值（比如web界面有超级管理员可切换单位功能）
        shareVOs = AuthorizMngBO_Client.getAuthorizedShares(
        		IIUFOResMngConsants.MODULE_REPORT_DIR,m_userVO.getUnitId(),
				strUserPK);
        
        if( shareVOs != null ){
        	if( isRepManager() ){//如果用户是报表资源管理者
	            for( int i=0; i<shareVOs.length; i++ ){
	                if( shareVOs[i] != null && !shareVOs[i].getShareObjOwnerPK().equals(m_userVO.getUnitId()) ){
	                    vecDir.add(shareVOs[i]);
	                }            	
	            }
	        }else { //普通用户
	            //将本单位的目录放在最前面
	            for( int i=0; i<shareVOs.length; i++ ){
	                if( shareVOs[i] != null && shareVOs[i].getShareObjOwnerPK().equals(m_userVO.getUnitId()) ){
	                    vecDir.add(shareVOs[i]);
	                    shareVOs[i] = null;
	                }
	            }
	            //将非本单位的目录增加到vecDir后
	            for( int i=0; i<shareVOs.length; i++ ){
	                if( shareVOs[i] != null ){
	                    vecDir.add(shareVOs[i]);
	                }
	            }
	        }
        }
        return vecDir;
    }catch (Exception e) {
        AppDebug.debug(StringResource.getStringResource("miufohbbb00096"));//@devTools System.out.println("指标参照装载目录数出错！无法得到用户有权限的目录");
AppDebug.debug(e);//@devTools         AppDebug.debug(e);
    }

    return null;
}
/**
 * 创建指定单位的单位节点，如果单位与用户的单位一致，节点的名称为“本单位目录”，否则为单位名称＋单位编码
 * @param strUnit String 单位编码
 * @return MeasRefTreeNode
 */
private MeasRefTreeNode createUnitNode(String strUnit)
{
    MeasRefTreeNode unitNode = null;
    if( strUnit.equals(m_userVO.getUnitId()) ){
        unitNode = new MeasRefTreeNode(MeasureRefDlg.ICON_UNIT);
        //"本单位目录"
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
 * 创建指定目录的目录节点，
 * @param repDirVO ReportDirVO　报表目录VO
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
 * 得到指定单位的报表目录
 * @param strUnitID String
 * @return nc.vo.iufo.resourcemng.OwnerDirRefVO
 * @i18n miufohbbb00097=指标参照装载目录数出错！无法得到单位
 * @i18n miufohbbb00098=对应的目录
 */
 private OwnerDirRefVO getUnitRefDir(String strUnitID)
 {
     try{
         return OwnerDirRefBO_Client.getOwnerDirRef(IIUFOResMngConsants.MODULE_REPORT_DIR,
             strUnitID);
     }catch(Exception e){
         AppDebug.debug(StringResource.getStringResource("miufohbbb00097")+strUnitID+StringResource.getStringResource("miufohbbb00098"));//@devTools System.out.println("指标参照装载目录数出错！无法得到单位"+strUnitID+"对应的目录");
AppDebug.debug(e);//@devTools          AppDebug.debug(e);
     }
     return null;
 }
 
private boolean isRepManager()
{
	return m_bRepManager;
}
/**
 * 增加本单位目录节点
 *
 */
private void addMyUnitNode(){
	//得到本单位的根目录，并创建
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
 * 增加用户有权限的目录节点
 */
private void addSharedNode()
{
	//得到用户的有权限的目录信息
    Vector vecDir = getSharedDirsByUser();
    int   nDirs = 0;
    
    if( vecDir != null && (nDirs=vecDir.size()) >0 ){
        String  strUnit = null;
        MeasRefTreeNode  unitTreeNode = null;
        
        for( int i=0; i<nDirs; i++ ){
            ShareInfoVO  shareDirVO = (ShareInfoVO)vecDir.get(i);
            if( shareDirVO != null){
                if( strUnit == null || strUnit.equals(shareDirVO.getShareObjOwnerPK())== false ){
                    //新增加一个单位虚节点，并增加到根节点下
                    strUnit = shareDirVO.getShareObjOwnerPK();
                    unitTreeNode = createUnitNode(strUnit);
                    if( unitTreeNode != null ){
                        rootNode.addSubNode(unitTreeNode);
                    }
                }

                //新建报表目录节点
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


  