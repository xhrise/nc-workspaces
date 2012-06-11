/*
 * 创建日期 2006-8-25
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.integration.dimension;

import nc.ui.iufo.resmng.common.UISrvException;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.authorization.AuthChecker;
import nc.util.iufo.authorization.AuthParam;
import nc.util.iufo.authorization.IAuthChecker;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.user.UserInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.ErrorForward;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.userrole.UserRoleUtil;
import com.ufsoft.iufo.web.IUFOAction;

public class DimUIToolKit {
	/**
	 * 得到真正操作的维度
	 * 如果选中的维度是引用维度，则返回引用维度PK
	 * @param action
	 * @return
	 */
	public  static final String  getDimIDToOperate(IUFOAction action){
		String		strSelectDimID = ResMngToolKit.getVOIDByTreeObjectID(
										action.getTableSelectedID());
		//判断一下是否为引用维度
		DimensionVO		dimVO = DimensionSrv.getByID(strSelectDimID);
		if( dimVO != null ){
			if( dimVO.getReferDim() != null ){
				DimensionVO		refDimVO = DimensionSrv.getByID(dimVO.getReferDim());
				if( refDimVO != null ){
					strSelectDimID = refDimVO.getDimID();
				}else{
					throw new WebException(StringResource.getStringResource("mbidim0003",
							 new String[]{dimVO.getDimname()}));//"{0}引用的维度不存在",
				}
			}
		}else{
			throw new WebException("mbidim0004");//选中的维度不存在
		}
		return strSelectDimID;
	}
	/**
	 * 检查选中的维度是否是引用维度
	 * @param action
	 * @return
	 */
	public static final ActionForward checkDimRefer(IUFOAction action){
		String		strMsg = null;
		
		String		strSelectDimID = ResMngToolKit.getVOIDByTreeObjectID(
				action.getTableSelectedID());
		//判断一下是否为引用维度
		DimensionVO		dimVO = DimensionSrv.getByID(strSelectDimID);
		if( dimVO != null ){
			if( dimVO.getReferDim() != null ){
				strMsg = StringResource.getStringResource("mbiauth0016");//本维度是引用维度，请在原始维度处进行相应操作
			}
		}else{
			strMsg = StringResource.getStringResource("mbidim0004");//选中的维度不存在
		}
		if( strMsg != null ){
			return new ErrorForward(strMsg);
		}
		return null;
	}

	/**
	 * 判断当前用户是否对选中的维度又修改权限,主要用于成员新建和修改删除的权限判断
	 * @param action
	 * @return
	 */
	public static final boolean hasModifyRight(String strDimPKWithAuthFlag, UserInfoVO	userVO){
		try{
			if( strDimPKWithAuthFlag == null || userVO == null ){
				return false;
			}
			//如果用户是超级用户，则返回true
			if( UserRoleUtil.isRoleAdministrator(userVO)){
				return true;
			}
			String				strAuthID = ResMngToolKit.getAuthorizedVOPK(strDimPKWithAuthFlag);
			AuthParam			param = new AuthParam(userVO.getID(),false,false,null);
			
			IAuthChecker		resAuthChecker = AuthChecker.getInstance();
			return resAuthChecker.checkRight(
									IBIResMngConstants.MODULE_DIMENSION, 
									strAuthID, 
									IAuthorizeTypes.AU_TYPE_MODIFY, 
									param);
		}catch(UISrvException e){
			AppDebug.debug(e);
			return false;
		}
	}
	
	/**
	 * 根据成员树ID上获取成员ID
	 * @param treeSelectedID
	 * @return
	 */
	public static String  getMemberIDByTreeID(String treeSelectedID){	
		 if(treeSelectedID != null){
	            int nLen = treeSelectedID.length();
	            int nPos = treeSelectedID.indexOf("^");	            
	            if(nPos < nLen && nPos >= 0){
	            	return treeSelectedID.substring(0, nPos);	            	
	            } else{
	               throw new RuntimeException();
	            }
	        }		
		 return null;
	}
	/**
	 * 根据成员树ID上获取维度ID
	 * @param treeSelectedID
	 * @return
	 */
	public static String  getDimIDByTreeID(String treeSelectedID){	
		 if(treeSelectedID != null){
	            int nLen = treeSelectedID.length();
	            int nPos = treeSelectedID.indexOf("^");	            
	            if(nPos < nLen && nPos >= 0){	            	
	            	return treeSelectedID.substring(nPos + 1, nLen);
	            } 
	     }		
		 return null;
	}
	
	/**
	 * 生成成员数ID
	 * @param memberID
	 * @param dimID
	 * @return
	 */
	public static String getMemberTreeID(String memberID, String dimID){
		return memberID + "^" + dimID;
	}
}
