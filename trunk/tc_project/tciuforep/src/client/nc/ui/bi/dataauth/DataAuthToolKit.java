/*
 * �������� 2006-4-19
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * ����Ȩ�޹�����
 */
public class DataAuthToolKit {
	/**
	 * �����û�ID�õ��û����룬����û������ڣ����أ���ɾ��
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
	 * ������ʾ��Ȩ�����ͣ�������д
	 * @param nType
	 * @return
	 */
	public static String getTypeString(Integer  nType){
		if( nType != null ){
			int		nTypeValue = nType.intValue();
			if( nTypeValue == IDataAuthConst.READ){
				return StringResource.getStringResource("ubiauth0009");//��
			}else{
				return StringResource.getStringResource("ubiauth0010");//д
			}
		}
		return null;
	}
	/**
	 * ������ʾ�İ�������
	 * @param nRule
	 * @return
	 */
	public static  String getRuleString(Integer  nRule){
		if( nRule != null ){
			int		nRuleValue = nRule.intValue();
			StringBuffer	sbBuf = new StringBuffer();
			boolean		bIncluded = false;
			if( (nRuleValue & IDataAuthConst.INCLUDE_ONLYSELF)!=0 ){
				sbBuf.append(StringResource.getStringResource("ubiauth0002"));//����
				bIncluded = true;
			}
			if( (nRuleValue & IDataAuthConst.INCLUDE_SLIBE)!=0 ){
				sbBuf.append(StringResource.getStringResource("ubiauth0003"));//ƽ��
				bIncluded = true;
			}
			if( (nRuleValue &IDataAuthConst.INCLUDE_ALLPARENT) !=0){
				sbBuf.append(StringResource.getStringResource("ubiauth0005"));//�����ϼ�
				bIncluded = true;
			}else if( (nRuleValue &IDataAuthConst.INCLUDE_PARENT) !=0){
				sbBuf.append(StringResource.getStringResource("ubiauth0004"));//ֱ���ϼ�
				bIncluded = true;
			}
			if( (nRuleValue &IDataAuthConst.INCLUDE_OFFSPRING) !=0 ){
				if( bIncluded ){
					sbBuf.append(StringResource.getStringResource("ubiauth0029"));//"��"
				}
				sbBuf.append(StringResource.getStringResource("ubiauth0007"));//�����¼�
			}else if( (nRuleValue &IDataAuthConst.INCLUDE_CHILDREN)!=0){
				if( bIncluded ){
					sbBuf.append(StringResource.getStringResource("ubiauth0029"));
				}
				sbBuf.append(StringResource.getStringResource("ubiauth0006"));//ֱ���¼�
			}
			return sbBuf.toString();
		}
		return null;
	}
	/**
	 * ֻ�й����û�������ά�ȵĴ�������Ȩ��
	 * @return
	 */
	public static  boolean isAuthorizedDimUser(IUFOAction	action){
		UserInfoVO		userVO = action.getCurUserInfo();
		//����û��ǳ����û����򷵻�true
		if( UserInfoVO.SYSINIT_USERID.equals(userVO.getID()) ){
			return true;
		}
		
		//�Ƿ���ά�ȵĴ�����
		String			strDimPK = getSelectedDimPK(action);
		DimensionVO     dimVO = getDimVO(strDimPK);
		if( userVO.getID().equals(dimVO.getOwnerid()) ){
			return true;
		}
		try{
			//�Ƿ��й���Ȩ��
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
	 * ���ص�ǰѡ�е�ά��pK
	 * @param action
	 * @return
	 */
	public static  String  getSelectedDimPK(IUFOAction	 action){
		return ResMngToolKit.getVOIDByTreeObjectID(action.getTableSelectedID());	
	}
	

	/**
	 * ����ָ���У˵�DimensionVO,��������ڣ��׳�DimensionException
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
	 * ���ݽ�ɫPK���ؽ�ɫ����
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
					return StringResource.getStringResource("ubiauth0030");//"��ɾ";
				}
			}
		}catch(Exception e){
			AppDebug.debug(e);
		}
		return null;
	}
	/**
	 * ����,�������Ķ��ID���Ϊ����
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
	 * ����Form�е�nRule
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
	 * ��Form�еõ�IncludeRule
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
	 * ά�ȳ�Ա��ģ��
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
	 * Ȩ�ޱ��ģ��
	 * @param authVOs
	 * @return
	 */
	public static WebTableModel    getDataAuthTableModel(IDataAuthVO[] authVOs){
    	//���ģ���б�
    	String[]			strColumnNames ={
    			"miufopublic126",//�û�",
    			"miufopublic125",//��λ",
				"ubiauth0017",//"��������",
				"ubiauth0008",//"Ȩ������",
				"ubiauth0018",//"��Ȩ��"
    	};	    	
    	WebTableColumn[]	columns = new WebTableColumn[strColumnNames.length];
    	for(int i=0; i<columns.length; i++){
    		columns[i] = new WebTableColumn();
    		columns[i].setColumnLabel(StringResource.getStringResource(strColumnNames[i]));
    	}
    	
    	//��Ȩ��Ϣ
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
