/*
 * 创建日期 2006-3-23
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.us.bi.integration.dimension;



import java.util.ArrayList;
import java.util.HashMap;

import nc.itf.bi.exproperty.IBIExPropConstants;
import nc.pub.bi.cache.DimCache;
import nc.pub.bi.cache.IBICacheNames;
import nc.pub.iufo.cache.base.BDCacheManager;
import nc.pub.iufo.cache.base.UICacheManager;
import nc.pub.iufo.cache.base.UserCache;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.resmng.AbsFiled4DirSysImpl;
import nc.util.iufo.resmng.IDirectoried;
import nc.util.iufo.resmng.ResDirectoriedImpl;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.base.util.IDMaker;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResTreeObject;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * UI维度服务类,利用UI缓存
 */
public class DimensionSrv extends AbsFiled4DirSysImpl implements IDirectoried {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2971918601061949923L;
	private static DimensionSrv		s_dimSrv = new DimensionSrv();
	private DimensionSrv(){
		super(IBIResMngConstants.MODULE_DIMENSION);
	}
	public static final DimensionSrv getInstance(){
		return s_dimSrv;
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiled4DirSysImpl#doGetAllFileVOs()
	 */
	protected ValueObject[] doGetAllFileVOs() throws UISrvException {
		try{
			DimCache	dimCache = getDimCache();
			return dimCache.getAllDims();

		}catch(Exception e){
			throw new UISrvException(e.getMessage());
		}
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiled4DirImpl#doSetParentDirVOPK(nc.vo.pub.ValueObject, java.lang.String)
	 */
	protected void doSetParentDirVOPK(ValueObject fileVO,
			String strParentDirVOPK) throws UISrvException {
		if( fileVO != null ){
			DimensionVO   dimVO = (DimensionVO)fileVO;
			dimVO.setPk_folderid(strParentDirVOPK);
		}
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#changeToFileTreeObj(nc.vo.pub.ValueObject, java.lang.String)
	 */
	protected IResTreeObject changeToFileTreeObj(ValueObject objVO,
			String strShareFlag) throws UISrvException {
		if( objVO == null ){
			return null;
		}
		
		DimensionVO	dimVO = (DimensionVO)objVO;
			
		IResTreeObject resTreeObject = new ResTreeObject();
        resTreeObject = new ResTreeObject();
        int nObjectType = IResTreeObject.OBJECT_TYPE_FILE;
       	resTreeObject.setID(ResMngToolKit.getTreeObjIDAued(dimVO.getDimID(), strShareFlag, nObjectType));
        resTreeObject.setLabel(dimVO.getDimname());
        resTreeObject.setParentID(ResMngToolKit.getTreeObjIDAued(dimVO.getPk_folderid(), strShareFlag,IResTreeObject.OBJECT_TYPE_DIR));
        resTreeObject.setType(nObjectType);
        resTreeObject.setSrcVO(objVO);
	        
        return resTreeObject;
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileDisAttr(java.lang.String, nc.vo.pub.ValueObject)
	 */
	protected Object[] doGetFileDisAttr(String strFileTreeObjID,
			ValueObject fileVO) throws UISrvException {
		// 需要根据用户的当前设置来重新定义
		if( fileVO != null ){
			DimensionVO		dimVO = (DimensionVO)fileVO;
			String[]		strValues = new String[7];
			int				nIndex = 0;
			strValues[nIndex++] = strFileTreeObjID;
			strValues[nIndex++] = dimVO.getDimcode();
			strValues[nIndex++] = dimVO.getDimname();
			//没有时间类型
			if(  dimVO.getDimensionType().intValue() ==DimensionVO.TIME_DIMENSION_TYPE ){
				strValues[nIndex++] = StringResource.getStringResource("ubidim0005");//"时间维度");
			}else{
				strValues[nIndex++] = StringResource.getStringResource("ubidim0006");//标准维度");
			}
			//引用维度
			strValues[nIndex] = "";
			String		strRefDimName = "";
			try{
				if( dimVO.getReferDim() != null && dimVO.getReferDim().length() >0){
					DimensionVO refDimVO = getDimByID(dimVO.getReferDim());
					if( refDimVO != null ){
						strRefDimName = refDimVO.getDimname();
					}
				}
			}catch(Exception e){
				AppDebug.debug(e);
			}
			strValues[nIndex++] = strRefDimName;
			
			String  strOwnerUser = "";
			try {
				UserCache userCache = BDCacheManager.getUserCache(true);
				UserInfoVO userVO = userCache.getUserById(dimVO.getOwnerid());
				if(userVO != null  ){
					strOwnerUser = userVO.getStrName();
				}
			} catch (Exception e) {				
				AppDebug.debug(e);		
			}
			strValues[nIndex ++ ] = strOwnerUser;

			strValues[nIndex] = dimVO.getNote();
			
			return strValues;
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileVO(java.lang.String)
	 */
	protected ValueObject doGetFileVO(String strSrcVOPK) throws UISrvException {
		//根据ID得到对象
		if( strSrcVOPK != null ){
			try{
				DimensionVO	dimVO = getDimByID(strSrcVOPK);
				return dimVO;
			}catch(Exception e){
				throw new UISrvException(e.getMessage());
			}
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCreateFileVO(nc.vo.pub.ValueObject)
	 */
	protected ValueObject doCreateFileVO(ValueObject toCreateVO,String strOrgPK)
			throws UISrvException {
		// 创建维度
		if( toCreateVO != null ){
			try{
				DimCache	dimCache = getDimCache();
				return dimCache.createDim((DimensionVO)toCreateVO);
			}catch(Exception e){
				throw new UISrvException(e.getMessage());
			}
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCheckFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doCheckFileVO(ValueObject toCreateVO) throws UISrvException {
		//需要检查维度的名称和编码是否重复
		if( toCreateVO != null ){
			try{
				DimensionVO		dimVO = (DimensionVO)toCreateVO;
				DimCache		dimCache = getDimCache();
				DimensionVO 	existDimVO = dimCache.getDimVOByCode(dimVO.getDimcode());
				if( existDimVO != null && 
					existDimVO.getDimID().equals(dimVO.getDimID()) == false ){
					throw new UISrvException("mbidim0001");//维度编码已经存在");
				}
				existDimVO = dimCache.getDimVOByName(dimVO.getPk_folderid(), dimVO.getDimname());
				if( existDimVO != null && 
						existDimVO.getDimID().equals(dimVO.getDimID()) == false ){
					throw new UISrvException("mbidim0002");//同目录下的维度名称已经存在");
				}
			}catch(UISrvException e){
				throw e;
			}catch(Exception e){
				throw new UISrvException(e.getMessage());
			}
		}

	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doUpdateFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doUpdateFileVO(ValueObject fileVO,String strOrgPK) throws UISrvException {
		// 更新维度
		if( fileVO != null ){
			try{
				getDimCache().updateDim((DimensionVO)fileVO);
			}catch(Exception e){
				throw new UISrvException(e.getMessage());
			}
		}

	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doRemoveFileVOs(java.lang.String[])
	 */
	protected void doRemoveFileVOs(String[] strSrcVOPKs) throws UISrvException {
		
		// 删除维度
		if( strSrcVOPKs != null ){
			for (int i = 0; i < strSrcVOPKs.length; i++) {
				if(strSrcVOPKs[i].equals(DimRescource.TRADE_TYPE_DIMPK))
					throw new UISrvException(StringResource.getStringResource("msrdef0049"));
			}
			try{				
				getDimCache().removeDims(strSrcVOPKs);
			}catch(Exception e){
				throw new UISrvException(e.getMessage());
			}
		}

	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCopyFileVO(java.lang.String, java.lang.String, java.lang.String)
	 */
	protected ValueObject doCopyFileVO(String strSrcVOPK,
			String strDstParentDirPK, String strOperUserPK,String strOrgPK)
			throws UISrvException {
		//复制后新建
		try {
			//复制维度
			DimensionVO srcVO = getDimCache().getDimByID(strSrcVOPK);
			DimensionVO cloneVO = (DimensionVO) srcVO.clone();			
			cloneVO.setDimID(IDMaker.makeID(20));
			cloneVO.setPk_folderid(strDstParentDirPK);
			int index = 0;
			do{
				index++;
				cloneVO.setDimname(srcVO.getDimname() + "-"+ index);
			}while(getDimByName(strDstParentDirPK, cloneVO.getDimname()) != null);
			
			index = 0;
			do{
				index++;
				cloneVO.setDimcode(srcVO.getDimcode() + "-"+ index);
			}while(getDimByCode(cloneVO.getDimcode()) != null);			
			
			DimensionVO newDimVO = getDimCache().createDim(cloneVO);		
			
			
			//复制维度结构
			ExPropertyVO[] oldDimExps = getAllExPropertyVOs(srcVO.getDimID());
			ExPropertyVO[] newDimExps = getAllExPropertyVOs(newDimVO.getDimID());
			ArrayList<ExPropertyVO> addExpList = new ArrayList<ExPropertyVO>();
			HashMap<String, ExPropertyVO[]> existExps = new HashMap<String, ExPropertyVO[]>();
			for(int i = 0; i < newDimExps.length; i++){
				existExps.put(newDimExps[i].getDBColumnName(), newDimExps);
			}
			for(int i = 0; i < oldDimExps.length; i++){
				if(existExps.containsKey(oldDimExps[i].getDBColumnName())) continue;
				addExpList.add(oldDimExps[i]);
			}
			if(addExpList.isEmpty() == false){
				ExPropertyVO[] addVOs = (ExPropertyVO[])addExpList.toArray(new ExPropertyVO[0]);				
				IExPropOperator exPropOperator = ExPropOperator.getExPropOper(IBIExPropConstants.EXPROP_MODULE_DIMENSION);
				for(int i = 0; i < addVOs.length; i++){
					exPropOperator.createExProperty(newDimVO.getDimID(), addVOs[i]);
				}
			}
			
			
			
			//复制维度成员
			DimMemberSrv orisrv = new DimMemberSrv(srcVO);
			DimMemberSrv newsrv = new DimMemberSrv(newDimVO);
			DimMemberVO[] oriDimMems = orisrv.getAllSubMembers(orisrv.getRoot().getMemberID());
			if((oriDimMems != null) && (oriDimMems.length > 0)){				
				DimMemberVO[] newDimMems = new DimMemberVO[oriDimMems.length];
				HashMap<String, String> ori2NewIDMap = new HashMap<String, String>();
				ori2NewIDMap.put(orisrv.getRoot().getMemberID(), newsrv.getRoot().getMemberID());
				String newID = null;
				for(int i = 0; i < newDimMems.length; i++){
					
					newDimMems[i] = (DimMemberVO) oriDimMems[i].clone();
					newID = IDMaker.makeID(20);
					newDimMems[i].setMemberID(newID);
					ori2NewIDMap.put(oriDimMems[i].getMemberID(), newDimMems[i].getMemberID());					
					newDimMems[i].setDimid(newDimVO.getDimID());
					newDimMems[i].setTableName(newDimVO.getDim_tablename());
				}
				//替换成员数ID
				for(int i = 0; i < newDimMems.length; i++){
					String[] levels = newDimMems[i].getLevels();
					if(levels == null || levels.length == 0) continue;
					for(int j = 0; j < levels.length; j++){
						levels[j] = (String) ori2NewIDMap.get(levels[j]);
					}				
				}
				newsrv.create(newDimMems);
			}
			
		} catch (Exception e) {			
			AppDebug.debug(e);
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#getDirByID(java.lang.String)
	 */
	public IResTreeObject getDirByID(String strDirID) throws UISrvException {
		return getDefaultDirectoriedImpl().getDirByID(strDirID);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#createDir(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	public IResTreeObject createDir(IResTreeObject dirResTreeObj)
			throws UISrvException {
		return getDefaultDirectoriedImpl().createDir(dirResTreeObj);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#updateDir(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	public void updateDir(IResTreeObject dirResTreeObj) throws UISrvException {
		getDefaultDirectoriedImpl().updateDir(dirResTreeObj);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#removeDir(java.lang.String)
	 */
	public void removeDir(String strDirID) throws UISrvException {
		getDefaultDirectoriedImpl().removeDir(strDirID);

	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#isDescendant(java.lang.String, java.lang.String)
	 */
	public boolean isDescendant(String strAncestorID, String strDirID)
			throws UISrvException {
		return getDefaultDirectoriedImpl().isDescendant(strAncestorID, strDirID);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#getAllSubDirs(java.lang.String)
	 */
	public IResTreeObject[] getAllSubDirs(String strDirID)
			throws UISrvException {
		return getDefaultDirectoriedImpl().getAllSubDirs(strDirID);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#copyDir(java.lang.String, java.lang.String, java.lang.String)
	 */
	public IResTreeObject copyDir(String strSrcDirID, String strDestDirID,
			String strOperUserPK) throws UISrvException {
		return getDefaultDirectoriedImpl().copyDir(strSrcDirID, strDestDirID, strOperUserPK);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#getFilesByDirID(java.lang.String)
	 */
	public IResTreeObject[] getFilesByDirID(String strDirID)
			throws UISrvException {
		return getDefaultDirectoriedImpl().getFilesByDirID(strDirID);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#copyFiles(java.lang.String[], java.lang.String, java.lang.String)
	 */
	public void copyFiles(String[] strSrcFileIDs, String strDstDirID,
			String strOperUserID,String strOrgPK) throws UISrvException {
		// TODO 自动生成方法存根
		getDefaultDirectoriedImpl().copyFiles(strSrcFileIDs, strDstDirID, strOperUserID,strOrgPK);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#getDirsByParentID(java.lang.String)
	 */
	public IResTreeObject[] getDirsByParentID(String strParentDirID)
			throws UISrvException {
		return getDefaultDirectoriedImpl().getDirsByParentID(strParentDirID);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#moveDir(java.lang.String, java.lang.String)
	 */
	public void moveDir(String strSrcDirID, String strDstDirID)
			throws UISrvException {
		getDefaultDirectoriedImpl().moveDir(strSrcDirID, strDstDirID);

	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#moveFile(java.lang.String, java.lang.String)
	 */
	public void moveFile(String strSrcFileID, String strDstDirID)
			throws UISrvException {   
//        IResTreeObject resTreeObj = getDirByID(strDstDirID);
//        if(resTreeObj.getParentID() == null || resTreeObj.getParentID().trim().length() ==0){
//            throw new UISrvException("不能移动到根目录");
//        }
//		
		getDefaultDirectoriedImpl().moveFile(strSrcFileID, strDstDirID);

	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IDirectoried#getFullPathName(java.lang.String, java.lang.String)
	 */
	public String getFullPathName(String strResTreeObjID, String strResOwnerPK)
			throws UISrvException {
		return getDefaultDirectoriedImpl().getFullPathName(strResTreeObjID, strResOwnerPK);
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IFiled4Dir#getSubFileVOs(java.lang.String)
	 */
	public ValueObject[] getSubFileVOs(String strDirVOPK) throws UISrvException {
		try{
			return getDimCache().getDimsByDirPK(strDirVOPK);
		}catch(Exception e){
			throw new UISrvException(e.getMessage());
		}
	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IFiled4Dir#moveFileVO(java.lang.String, java.lang.String)
	 */
	public void moveFileVO(String strSrcFileVOPK, String strDstDirVOPK)
			throws UISrvException {
		if( strSrcFileVOPK != null ){
			DimensionVO		dimVO = null;
			try{
				//得到DimensionVO
				dimVO = getDimByID(strSrcFileVOPK);				
			}catch(Exception e){				
				throw new UISrvException(e.getMessage());
			}
			if( dimVO != null ){
				String oriFolder = dimVO.getPk_folderid();
				dimVO.setPk_folderid(strDstDirVOPK);
				//更新
				try {
					getDimCache().updateDim(dimVO);
				} catch (Exception e) {
					dimVO.setPk_folderid(oriFolder);
					throw new UISrvException(e.getMessage());
				}
			}
		}

	}

	/* （非 Javadoc）
	 * @see nc.util.iufo.resmng.IFiled4Dir#getRootDirDisName(java.lang.String, boolean)
	 */
	public String getRootDirDisName(String strOwerPK, boolean bSelfRoot)
			throws UISrvException {
		return StringResource.getStringResource("ubidim0004");//维度管理");
	}
	
	public DimensionVO[] getReferencableDims() throws Exception{
		return getDimCache().getReferencableDims();
	}
	private DimCache	getDimCache() throws Exception{
		return (DimCache)UICacheManager.getSingleton().getCache(IBICacheNames.DIMENSION_CACHE);
	}
	
	private IDirectoried  getDefaultDirectoriedImpl(){
		return ResDirectoriedImpl.getIDirectoried(IBIResMngConstants.MODULE_DIMENSION);
	}

	public DimensionVO[]	getDimByIDs(String[] strDimIDs) throws Exception{
		return getDimCache().getDimByIDs(strDimIDs);
	}
	public DimensionVO		getDimByID(String strDimID) throws Exception{
		return getDimCache().getDimByID(strDimID);
	}
	
	public DimensionVO		getDimByCode(String strDimCode) throws Exception{
		return getDimCache().getDimVOByCode(strDimCode);
	}
	public DimensionVO		getDimByName(String strDirPK, String strDimName) throws Exception{
		return getDimCache().getDimVOByName(strDirPK, strDimName);
	}
	
	/**
	 * 主维度的所有属性维度ID
	 * @param dimID
	 * @return
	 * @throws UISrvException
	 */
	public String[] getPropDimIDs(String dimID) throws UISrvException{
		try{
			DimensionVO		dimVO = getDimCache().getDimByID(dimID);
			if( dimVO != null && dimVO.getReferDim() != null ){
				dimID = dimVO.getReferDim();
			}
			IExPropOperator exPropOper =ExPropOperator.getExPropOper(IBIExPropConstants.EXPROP_MODULE_DIMENSION);
        	ExPropertyVO[] allExp =  exPropOper.loadAllExProp(dimID);
        	if(allExp == null || allExp.length == 0) return null;
        	ArrayList<String> list = new ArrayList<String>();
        	for(int i = 0; i < allExp.length; i++){
        		if((allExp[i].getType() == ExPropertyVO.TYPE_REF) && (allExp[i].getRefTypePK() != null)){
        			list.add(allExp[i].getRefTypePK());
        		}
        	}
        	return (String[]) list.toArray(new String[0]);	
        } catch (Exception e) {
        	throw new UISrvException(e.getMessage());
            
        }        		
	}
	
	/**
	 * 根据主维度的属性维度对应的数据库中表数据列名称
	 * @param dimID
	 * @param propDimID
	 * @return
	 * @throws UISrvException
	 */
	public String getPropDimDBColumn(String dimID, String propDimID) throws UISrvException{
        try {
			DimensionVO		dimVO = getDimCache().getDimByID(dimID);
			if( dimVO != null && dimVO.getReferDim() != null ){
				dimID = dimVO.getReferDim();
			}
			IExPropOperator exPropOper =ExPropOperator.getExPropOper(IBIExPropConstants.EXPROP_MODULE_DIMENSION);
        	ExPropertyVO[] allExp =  exPropOper.loadAllExProp(dimID);
        	if(allExp == null || allExp.length == 0) return null;
        	
        	for(int i = 0; i < allExp.length; i++){
        		if(allExp[i].getType() == ExPropertyVO.TYPE_REF){
        			if((allExp[i].getRefTypePK() != null) && (allExp[i].getRefTypePK().equals(propDimID))){
        				return allExp[i].getDBColumnName();
        			}
        		}
        	}        	
        } catch (Exception e) {
        	throw new UISrvException(e.getMessage());            
        }       
        return null;
	}
	public ExPropertyVO[] getAllExPropertyVOs(String strDimID) throws Exception{
		if( strDimID != null ){
			DimensionVO		dimVO = getDimCache().getDimByID(strDimID);
			if( dimVO != null && dimVO.getReferDim() != null ){//对于引用维度，获取原始维度信息
				dimVO = getDimCache().getDimByID(dimVO.getReferDim());
			}
			if( dimVO != null ){//加载维度的所有自定义属性（包括系统预制的自定义属性）
				IExPropOperator exPropOper = ExPropOperator.getExPropOper(IBIExPropConstants.EXPROP_MODULE_DIMENSION);
				try {
					return exPropOper.loadAllExProp(dimVO.getDimID());
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
		}
		return null;
	}
	public DimensionVO[]	getAllDimVOs() throws Exception{
		return getDimCache().getAllDims();
	}
}
