/**
 * DataPolicyEditAction.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-04-19
 */
package nc.ui.bi.dataauth;


import java.util.ArrayList;
import java.util.List;

import nc.itf.bi.exproperty.IBIExPropConstants;
import nc.itf.iufo.exproperty.IExPropConstants;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.us.bi.dataauth.DataPolicySrv;
import nc.util.iufo.pub.IDMaker;
import nc.vo.bi.dataauth.DataPolicyVO;
import nc.vo.bi.dataauth.IDataAuthConst;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.pub.BusinessException;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.role.ui.RoleMngBO_Client;
import com.ufsoft.iufo.role.vo.RoleVO;
import com.ufsoft.iufo.web.DialogAction;

/**
 * 类作用描述文字
 * zyjun
 * 2006-04-19
 */
public class DataPolicyEditAction extends DialogAction {
    
    /**
     * <MethodDescription>
     * zyjun
     * 2006-04-19
     */
    public ActionForward create(ActionForm actionForm){
        DataAuthEditForm form = (DataAuthEditForm) actionForm;
        setCreateForm(form);
        if( form.getUserFieldItems() != null ){
        	return  new ActionForward(DataPolicyEditDlg.class.getName());
        }else{
        	return new ErrorForward(StringResource.getStringResource("mbiauth0003"));//没有定义参照类型的自定义字段，无法新建或修改数据安全策略"));
        }
    }
    /**
     * <MethodDescription>
     * zyjun
     * 2006-04-19
     */
    public ActionForward update(ActionForm actionForm){
    	try{
	    	//得到需要修改的内容VO
	    	String			strID  = getTableSelectedID();
	    	DataPolicyVO	dataPolicyVO = DataPolicySrv.getInstance().getDataPolicyByPK(strID);
	    	if( dataPolicyVO != null ){
		        DataAuthEditForm form = (DataAuthEditForm) actionForm;
		        setUpdateForm(form, dataPolicyVO);
		        if( form.getUserFieldItems() != null ){
		        	return  new ActionForward(DataPolicyEditDlg.class.getName());
		        }else{
		        	return new ErrorForward(StringResource.getStringResource("mbiauth0003"));
		        }
	    	}else{
	    		return new ErrorForward(StringResource.getStringResource("mbiauth0004"));//"已经删除，无法修改"));
	    	}
    	}catch(BusinessException e){
    		return new ErrorForward(e.getMessage());
    	}
    }
    /**
     * <MethodDescription>保存
     * zyjun
     * 2006-04-19
     */
    public ActionForward save(ActionForm actionForm){
    	try{
	        DataAuthEditForm form = (DataAuthEditForm) actionForm;
	        if( form.getAuthPK() == null || form.getAuthPK().length()==0){
	        	//新建,可能有多个,需要判断是新建还是修改
	        	String[]			strRolePKs = DataAuthToolKit.getIDs(form.getAutheePKs());
	        	DataPolicyVO[][]	dataPolicyVOs = getCreateAndUpdateVOs(strRolePKs, form);
	        	if( dataPolicyVOs[0] != null ){
	        		DataPolicySrv.getInstance().createDataPolicys(dataPolicyVOs[0]);
	        	}
	        	if( dataPolicyVOs[1] != null ){
	        		DataPolicySrv.getInstance().updateDataPolicys(dataPolicyVOs[1]);
	        	}      	
	        }else{
	        	//修改
	        	DataPolicyVO	policyVO = getDataPolicyVOFromForm(form);
	        	DataPolicySrv.getInstance().updateDataPolicy(policyVO);
	        }
	        return  new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
    	}catch(BusinessException e){
    		return new ErrorForward(e.getMessage());
    	}
    }

       
   /**
    * Form值校验
    * UserField不能为空，角色不能为空
    * @param actionForm
    * @param errors 值校验失败的提示信息集合
    */
    @SuppressWarnings("unchecked")
	public void validate(ArrayList errors, ActionForm actionForm){
    	DataAuthEditForm form = (DataAuthEditForm) actionForm;
        if( form.getUserField() == null || form.getUserField().length()==0 ){
        	errors.add(StringResource.getStringResource("mbiauth0005"));//请选择用户字段"));
        }
        if( form.getAutheePKs() == null || form.getAutheePKs().length() == 0 ){
        	errors.add(StringResource.getStringResource("mbiauth0014"));//请选择角色"));
        }
    }        
       
   /**
    * 关联Form
    *
    */   
    public String getFormName(){
        return DataAuthEditForm.class.getName();
    }
    /**
     * 设置新建的form
     * @param form
     */
    private void setCreateForm(DataAuthEditForm form){
    	String	strDimPK = getRequestParameter(IDataAuthConst.DIMPK);
    	form.setUpdate(false);
    		
    	form.setDimPK( strDimPK );
    	form.setType(IDataAuthConst.READ);
    	DataAuthToolKit.setIncludeRule(form, null);
    	
    	//设置纬度属性
    	form.setDimFieldItems(getDimFieldItems(strDimPK));
    	
    	//设置用户属性
    	String[][]	strUserFieldItems = getUserFieldItems();
   		form.setUserFieldItems(strUserFieldItems);
 
    	form.setLangCode(getLanguageCode());	
    }
    /**
     * 设置修改的form
     * @param form
     */
    private void setUpdateForm(DataAuthEditForm form, DataPolicyVO dataPolicyVO){
    	String	strDimPK = getRequestParameter(IDataAuthConst.DIMPK);
    	form.setUpdate(true);
    	form.setDimPK(strDimPK);
    	form.setAuthPK(getTableSelectedID());
    	
    	//得到角色的名称
    	form.setAutheePKs(dataPolicyVO.getPk_role());
    	try{
    		RoleVO			roleVO = RoleMngBO_Client.loadRole(dataPolicyVO.getPk_role());
        	String[][]		strAutheeItems = {{roleVO.getRoleID(), roleVO.getRoleName()}};
        	form.setAutheeListItems(strAutheeItems);    		
    	}catch(Exception e){
    		throw new WebException("mbiauth0015");
    	}
    	
    	if( dataPolicyVO.getType() != null ){
    		form.setType(dataPolicyVO.getType().intValue());
    	}else{
    		form.setType(IDataAuthConst.READ);
    	}
    	DataAuthToolKit.setIncludeRule(form, dataPolicyVO.getRule());
    	//设置纬度属性
    	form.setDimFieldItems(getDimFieldItems(strDimPK));
    	form.setDimField(dataPolicyVO.getDimfield());
    	
    	//设置用户属性
    	form.setUserFieldItems(getUserFieldItems()); 
    	form.setUserField(dataPolicyVO.getUserfield());
    	
    	form.setLangCode(getLanguageCode());	
    }
    
    private String[][]  getDimFieldItems(String strDimPK){
   		ExPropertyVO[]	propVOs = getRefTypedExProps(
   									IBIExPropConstants.EXPROP_MODULE_DIMENSION, strDimPK);
   		int				n = 1+(propVOs != null ?propVOs.length:0);
  		String[][]	items = new String[n][];
  		//纬度PK字段
  		items[0] = new String[]{"pk_member","  "};
   		if( propVOs != null ){
   			for( int i=0; i<propVOs.length; i++){
   				items[i+1] = new String[]{propVOs[i].getDBColumnName(), propVOs[i].getName()};
   			}
   		}
    	return items;
    }
    /**
     * 生成WebChoice需要的列表内容
     * @return
     */
    private String[][]  getUserFieldItems() {
    	//得到用户自定义字段信息
   		String[][]	items = null;
   		ExPropertyVO[]	propVOs = getRefTypedExProps(IExPropConstants.EXPROP_MODULE_USER, null);
   		if( propVOs != null ){
   			items = new String[propVOs.length][];
   			for( int i=0; i<propVOs.length; i++){
   				items[i] = new String[]{propVOs[i].getDBColumnName(), propVOs[i].getName()};
   			}
   		}
    	return items;
   
    }

    /**
     * 得到自定义、类型为参照的属性字段
     * @param strModuleID
     * @return
     */
    private ExPropertyVO[]	getRefTypedExProps(String strModuleID, String strPropOwnerPK){
    	IExPropOperator  operator = ExPropOperator.getExPropOper(strModuleID);
    	try{
    		ExPropertyVO[]	propVOs = operator.loadAllExProp(strPropOwnerPK);
    		if( propVOs != null ){
    			List<ExPropertyVO>	aryTypeRef = new ArrayList<ExPropertyVO>();
    			for( int i=0; i<propVOs.length; i++ ){
    				if( propVOs[i].getType() == ExPropertyVO.TYPE_REF ){
    					aryTypeRef.add(propVOs[i]);
    				}
    			}
    			if( aryTypeRef.size() >0 ){
    				propVOs = new ExPropertyVO[aryTypeRef.size()];
    				aryTypeRef.toArray(propVOs);
    				return propVOs;
    			}
    		}
    	}catch(Exception e){
    		throw new WebException(e.getMessage());
    	}	
    	return null;
    }
    
    private DataPolicyVO  getDataPolicyVOFromForm(DataAuthEditForm form){
    	DataPolicyVO	policyVO = new DataPolicyVO();
    	policyVO.setPk_datapolicy(form.getAuthPK());
    	policyVO.setPk_dim(form.getDimPK());
    	policyVO.setPk_role(form.getAutheePKs());
    	policyVO.setDimfield(form.getDimField());
    	policyVO.setUserfield(form.getUserField());
    	policyVO.setType(new Integer(form.getType()));
    	policyVO.setRule(new Integer(DataAuthToolKit.getIncludeRule(form)));
    	
    	policyVO.setPk_author(getCurUserInfo().getID());
    	return policyVO;
    }
	/**
	 * 针对选择的用户集合,查询数据库,将需要新建和修改的VO分开
	 * @param strUserPKs
	 * @param form
	 * @return
	 * @throws BusinessException
	 */
	private DataPolicyVO[][]  getCreateAndUpdateVOs(String[] strRolePKs, DataAuthEditForm form) throws BusinessException{
		String		strDimPK = form.getDimPK();

    	List<DataPolicyVO>	aryCreates = new ArrayList<DataPolicyVO>();
    	List<DataPolicyVO>	aryUpdates = new ArrayList<DataPolicyVO>();
    	for( int i=0; i<strRolePKs.length; i++ ){
    		DataPolicyVO	policyVO = getDataPolicyVOFromForm(form);
    		policyVO.setPk_role(strRolePKs[i]);
    		DataPolicyVO	policyOldVO = DataPolicySrv.getInstance().getByRoleAndField(strDimPK, strRolePKs[i],policyVO.getDimfield());
    		if( policyOldVO != null ){
    			policyVO.setPk_datapolicy(policyOldVO.getPk_datapolicy());
    			aryUpdates.add(policyVO);
    		}else{
    			policyVO.setPk_datapolicy(IDMaker.makeID(20));
    			aryCreates.add(policyVO);
    		}
    	}
    	DataPolicyVO[][]	results = new DataPolicyVO[2][];
    	if( aryCreates.size() >0){
    		results[0] = new DataPolicyVO[aryCreates.size()];
    		aryCreates.toArray(results[0]);
    	}
       	if( aryUpdates.size() >0){
    		results[1] = new DataPolicyVO[aryUpdates.size()];
    		aryUpdates.toArray(results[1]);
    	} 
       	return results;
	}
	
    
}

/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <ActionVO name="DataPolicyEditAction" package="nc.ui.bi.dataauth" 关联Form="nc.ui.bi.dataauth.DataPolicyEditForm">
      <MethodsVO create="" save="" update="">
      </MethodsVO>
    </ActionVO>
@WebDeveloper*/