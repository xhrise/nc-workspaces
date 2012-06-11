/*
 * 创建日期 2006-4-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.dataauth;

import java.util.StringTokenizer;

import nc.pub.iufo.cache.base.BDCacheConstants;
import nc.pub.iufo.cache.base.UICacheManager;
import nc.pub.iufo.cache.base.UnitCache;
import nc.pub.iufo.cache.base.UserCache;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.authorization.AuthChecker;
import nc.util.iufo.authorization.AuthParam;
import nc.util.iufo.authorization.IAuthChecker;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.dataauth.IDataAuthConst;
import nc.vo.bi.dataauth.IDataAuthVO;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimensionException;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.comp.table.WebTableColumn;
import com.ufida.web.comp.table.WebTableModel;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.role.ui.RoleMngBO_Client;
import com.ufsoft.iufo.role.vo.RoleVO;
import com.ufsoft.iufo.web.IUFOAction;

/**
 * @author zyjun
 *
 * 数据权限工具类
 */
public class DataAuthToolKit {
	/**
	 * 根据用户ID得到用户编码，如果用户不存在，返回＂已删＂
	 * @param strUserPK
	 * @return
	 */
	public static String  getUserCode(String strUserPK){
		UserInfoVO	userVO = getUserVO(strUserPK);
		if( userVO != null ){
			return userVO.getStrCode();
		}
		return StringResource.getStringResource("ubiauth0030");
	}
	/**
	 * 返回显示的权限类型，读或者写
	 * @param nType
	 * @return
	 */
	public static String getTypeString(Integer  nType){
		if( nType != null ){
			int		nTypeValue = nType.intValue();
			if( nTypeValue == IDataAuthConst.READ){
				return StringResource.getStringResource("ubiauth0009");//读
			}else{
				return StringResource.getStringResource("ubiauth0010");//写
			}
		}
		return null;
	}
	/**
	 * 返回显示的包含规则
	 * @param nRule
	 * @return
	 */
	public static  String getRuleString(Integer  nRule){
		if( nRule != null ){
			int		nRuleValue = nRule.intValue();
			StringBuffer	sbBuf = new StringBuffer();
			boolean		bIncluded = false;
			if( (nRuleValue & IDataAuthConst.INCLUDE_ONLYSELF)!=0 ){
				sbBuf.append(StringResource.getStringResource("ubiauth0002"));//自身
				bIncluded = true;
			}
			if( (nRuleValue & IDataAuthConst.INCLUDE_SLIBE)!=0 ){
				sbBuf.append(StringResource.getStringResource("ubiauth0003"));//平级
				bIncluded = true;
			}
			if( (nRuleValue &IDataAuthConst.INCLUDE_ALLPARENT) !=0){
				sbBuf.append(StringResource.getStringResource("ubiauth0005"));//所有上级
				bIncluded = true;
			}else if( (nRuleValue &IDataAuthConst.INCLUDE_PARENT) !=0){
				sbBuf.append(StringResource.getStringResource("ubiauth0004"));//直接上级
				bIncluded = true;
			}
			if( (nRuleValue &IDataAuthConst.INCLUDE_OFFSPRING) !=0 ){
				if( bIncluded ){
					sbBuf.append(StringResource.getStringResource("ubiauth0029"));//"、"
				}
				sbBuf.append(StringResource.getStringResource("ubiauth0007"));//所有下级
			}else if( (nRuleValue &IDataAuthConst.INCLUDE_CHILDREN)!=0){
				if( bIncluded ){
					sbBuf.append(StringResource.getStringResource("ubiauth0029"));
				}
				sbBuf.append(StringResource.getStringResource("ubiauth0006"));//直接下级
			}
			return sbBuf.toString();
		}
		return null;
	}
	/**
	 * 只有管理用户或者是维度的创建者有权限
	 * @return
	 */
	public static  boolean isAuthorizedDimUser(IUFOAction	action){
		UserInfoVO		userVO = action.getCurUserInfo();
		//如果用户是超级用户，则返回true
		if( UserInfoVO.SYSINIT_USERID.equals(userVO.getID()) ){
			return true;
		}
		
		//是否是维度的创建者
		String			strDimPK = getSelectedDimPK(action);
		DimensionVO     dimVO = getDimVO(strDimPK);
		if( userVO.getID().equals(dimVO.getOwnerid()) ){
			return true;
		}
		try{
			//是否有管理权限
			String				strAuthID = ResMngToolKit.getAuthorizedVOPK(action.getTableSelectedID());
			AuthParam			param = new AuthParam(userVO.getID(),false,false,null);
			
			IAuthChecker		resAuthChecker = AuthChecker.getInstance();
			return resAuthChecker.checkRight(
									IBIResMngConstants.MODULE_DIMENSION, 
									strAuthID, 
									IAuthorizeTypes.AU_TYPE_MANAGE, 
									param);
		}catch(UISrvException e){
			AppDebug.debug(e);
			return false;
		}
	}
	/**
	 * 返回当前选中的维度pK
	 * @param action
	 * @return
	 */
	public static  String  getSelectedDimPK(IUFOAction	 action){
		return ResMngToolKit.getVOIDByTreeObjectID(action.getTableSelectedID());	
	}
	

	/**
	 * 返回指定ＰＫ的DimensionVO,如果不存在，抛出DimensionException
	 * @param strDimPK
	 * @return
	 */
	public static DimensionVO	getDimVO(String strDimPK){
		DimensionVO		dimVO = null;
		try{
			dimVO = DimensionSrv.getInstance().getDimByID(strDimPK);
		}catch(Exception e){
			AppDebug.debug(e);
		}
		if (dimVO == null) {
			throw new DimensionException(DimensionException.ERR_NO_DIMDEF);
		}
		return dimVO;		
	}
	/**
	 * 根据角色PK返回角色名称
	 * @param strRolePK
	 * @return
	 */
	public static String getRoleName(String strRolePK){
		try{
			if( strRolePK != null ){
				RoleVO	roleVO = RoleMngBO_Client.loadRole(strRolePK);
				if( roleVO != null ){
					return roleVO.getRoleName();
				}else{
					return StringResource.getStringResource("ubiauth0030");//"已删";
				}
			}
		}catch(Exception e){
			AppDebug.debug(e);
		}
		return null;
	}
	/**
	 * 将用,连起来的多个ID拆分为数组
	 * @param strIDs
	 * @return
	 */
	public static String[]		getIDs(String strIDs){
    	StringTokenizer		tokenizer = new StringTokenizer(strIDs, WebGlobalValue.FLAG_SPLIT);
    	String[]			strPKs = new String[tokenizer.countTokens()];
    	int					nIndex = 0;
    	while(tokenizer.hasMoreTokens()){
    		strPKs[nIndex++] = tokenizer.nextToken();
    	}
    	return strPKs;
	}
	/**
	 * 设置Form中的nRule
	 * @param form
	 * @param nRule
	 */
	public static void  setIncludeRule(DataAuthEditForm form, Integer nRule){
    	if( nRule != null ){
	    	int		nRuleValue = nRule.intValue();
	    	form.setIncludeAncestor((nRuleValue & IDataAuthConst.INCLUDE_ALLPARENT)!=0);
	    	form.setIncludeChild((nRuleValue & IDataAuthConst.INCLUDE_CHILDREN)!=0);
	    	form.setIncludeOffSpring((nRuleValue & IDataAuthConst.INCLUDE_OFFSPRING)!=0);
	    	form.setIncludeParent((nRuleValue & IDataAuthConst.INCLUDE_PARENT)!=0);
	    	form.setIncludeSelf((nRuleValue & IDataAuthConst.INCLUDE_ONLYSELF)!=0);
	    	form.setIncludeSlibe((nRuleValue & IDataAuthConst.INCLUDE_SLIBE)!=0);
    	}else{
        	form.setIncludeSelf(true);
        	form.setIncludeAncestor(false);
        	form.setIncludeSlibe(false);
        	form.setIncludeParent(false);
        	form.setIncludeChild(false);
        	form.setIncludeOffSpring(false);
    	}
	}
	/**
	 * 从Form中得到IncludeRule
	 * @param form
	 * @return
	 */
	public static int  getIncludeRule(DataAuthEditForm form){
		int			nRule =0;
		if( form.isIncludeAncestor()){
			nRule |= IDataAuthConst.INCLUDE_ALLPARENT;
		}
		if( form.isIncludeChild()){
			nRule |= IDataAuthConst.INCLUDE_CHILDREN;
		}
		if( form.isIncludeOffSpring()){
			nRule |= IDataAuthConst.INCLUDE_OFFSPRING;
		}
		if( form.isIncludeParent()){
			nRule |= IDataAuthConst.INCLUDE_PARENT;
		}
		if( form.isIncludeSelf()){
			nRule |= IDataAuthConst.INCLUDE_ONLYSELF;
		}
		if( form.isIncludeSlibe()){
			nRule |= IDataAuthConst.INCLUDE_SLIBE;
		}
		return nRule;
	}
	/**
	 * 维度成员树模型
	 * @param strDimPK
	 * @return
	 */
	public static WebTreeModel		getDimMemberTreeModel(String strDimPK){
		DimensionVO dimVO = getDimVO(strDimPK);
		DimMemberSrv srv = new DimMemberSrv(dimVO);
	
		DimMemberVO[] memVOS = srv.getAll();
		return  new WebTreeModel(memVOS);
	}
	/**
	 * 权限表格模型
	 * @param authVOs
	 * @return
	 */
	public static WebTableModel    getDataAuthTableModel(IDataAuthVO[] authVOs){
    	//表格模型列表
    	String[]			strColumnNames ={
    			"miufopublic126",//用户",
    			"miufopublic125",//单位",
				"ubiauth0017",//"包含规则",
				"ubiauth0008",//"权限类型",
				"ubiauth0018",//"授权人"
    	};	    	
    	WebTableColumn[]	columns = new WebTableColumn[strColumnNames.length];
    	for(int i=0; i<columns.length; i++){
    		columns[i] = new WebTableColumn();
    		columns[i].setColumnLabel(StringResource.getStringResource(strColumnNames[i]));
    	}
    	
    	//授权信息
    	Object[][]			objs =null;
    	if( authVOs != null ){
    		objs = new Object[authVOs.length][];
	    	for(int i=0; i<objs.length; i++){
	    		String		strRightType = DataAuthToolKit.getTypeString(authVOs[i].getType());

	    		objs[i] = new String[]{authVOs[i].getID(),
	    					DataAuthToolKit.getUserCode(authVOs[i].getAuthee()),
	    					getUserUnitCode(authVOs[i].getAuthee()),
	    					DataAuthToolKit.getRuleString(authVOs[i].getRule()),
							strRightType,
							DataAuthToolKit.getUserCode(authVOs[i].getAuthor())
	    				};
	    	}
    	}
    	WebTableModel 		tableModel = new WebTableModel(objs,columns);   	
    	
    	return tableModel;

	}
	
	private static UserInfoVO	getUserVO(String strUserPK){
		UserCache 	userCache = (UserCache)UICacheManager.getSingleton().getCache(BDCacheConstants.UserCacheObjName);
		return userCache.getUserById(strUserPK);
	}
	private static String getUserUnitCode(String strUserPK){
		UserInfoVO	userVO = getUserVO(strUserPK);
		if( userVO != null ){
			UnitCache	unitCache = (UnitCache)UICacheManager.getSingleton().getCache(BDCacheConstants.UnitCacheObjName);
			UnitInfoVO	unitVO = unitCache.getUnitInfoByPK(userVO.getUnitId());
			if( unitVO != null ){
				return unitVO.getCode();
			}
		}
		return null;
	}
	
}
