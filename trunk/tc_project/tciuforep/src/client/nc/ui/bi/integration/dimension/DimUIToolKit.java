/*
 * �������� 2006-8-25
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
	 * �õ�����������ά��
	 * ���ѡ�е�ά��������ά�ȣ��򷵻�����ά��PK
	 * @param action
	 * @return
	 */
	public  static final String  getDimIDToOperate(IUFOAction action){
		String		strSelectDimID = ResMngToolKit.getVOIDByTreeObjectID(
										action.getTableSelectedID());
		//�ж�һ���Ƿ�Ϊ����ά��
		DimensionVO		dimVO = DimensionSrv.getByID(strSelectDimID);
		if( dimVO != null ){
			if( dimVO.getReferDim() != null ){
				DimensionVO		refDimVO = DimensionSrv.getByID(dimVO.getReferDim());
				if( refDimVO != null ){
					strSelectDimID = refDimVO.getDimID();
				}else{
					throw new WebException(StringResource.getStringResource("mbidim0003",
							 new String[]{dimVO.getDimname()}));//"{0}���õ�ά�Ȳ�����",
				}
			}
		}else{
			throw new WebException("mbidim0004");//ѡ�е�ά�Ȳ�����
		}
		return strSelectDimID;
	}
	/**
	 * ���ѡ�е�ά���Ƿ�������ά��
	 * @param action
	 * @return
	 */
	public static final ActionForward checkDimRefer(IUFOAction action){
		String		strMsg = null;
		
		String		strSelectDimID = ResMngToolKit.getVOIDByTreeObjectID(
				action.getTableSelectedID());
		//�ж�һ���Ƿ�Ϊ����ά��
		DimensionVO		dimVO = DimensionSrv.getByID(strSelectDimID);
		if( dimVO != null ){
			if( dimVO.getReferDim() != null ){
				strMsg = StringResource.getStringResource("mbiauth0016");//��ά��������ά�ȣ�����ԭʼά�ȴ�������Ӧ����
			}
		}else{
			strMsg = StringResource.getStringResource("mbidim0004");//ѡ�е�ά�Ȳ�����
		}
		if( strMsg != null ){
			return new ErrorForward(strMsg);
		}
		return null;
	}

	/**
	 * �жϵ�ǰ�û��Ƿ��ѡ�е�ά�����޸�Ȩ��,��Ҫ���ڳ�Ա�½����޸�ɾ����Ȩ���ж�
	 * @param action
	 * @return
	 */
	public static final boolean hasModifyRight(String strDimPKWithAuthFlag, UserInfoVO	userVO){
		try{
			if( strDimPKWithAuthFlag == null || userVO == null ){
				return false;
			}
			//����û��ǳ����û����򷵻�true
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
	 * ���ݳ�Ա��ID�ϻ�ȡ��ԱID
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
	 * ���ݳ�Ա��ID�ϻ�ȡά��ID
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
	 * ���ɳ�Ա��ID
	 * @param memberID
	 * @param dimID
	 * @return
	 */
	public static String getMemberTreeID(String memberID, String dimID){
		return memberID + "^" + dimID;
	}
}
