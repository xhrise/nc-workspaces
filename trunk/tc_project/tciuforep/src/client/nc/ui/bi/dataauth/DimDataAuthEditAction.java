/**
 * DimDataAuthEditAction.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-04-18
 */
package nc.ui.bi.dataauth;

import java.util.ArrayList;

import nc.us.bi.dataauth.DataAuthSrv;
import nc.util.iufo.pub.IDMaker;
import nc.vo.bi.dataauth.DataAuthVO;
import nc.vo.bi.dataauth.IDataAuthConst;
import nc.vo.bi.dataauth.IDataAuthVO;
import nc.vo.pub.BusinessException;

import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;

/**
 * 类作用描述文字
 * zyjun
 * 2006-04-18
 */
public class DimDataAuthEditAction extends DialogAction {
    
    /**
     * <MethodDescription>新建
     * zyjun
     * 2006-04-18
     */
    public ActionForward create(ActionForm actionForm){
    	//设置form内容
    	DataAuthEditForm form = (DataAuthEditForm) actionForm;
    	setCreateForm(form);
    	
        return new ActionForward(DimDataAuthEditDlg.class.getName());
    }
    /**
     * <MethodDescription>修改
     * zyjun
     * 2006-04-18
     */
    public ActionForward update(ActionForm actionForm){
    	try{
    		//得到需要修改的授权VO
	    	String		strDataAuthPK = getTableSelectedID();
	    	IDataAuthVO	dataAuthVO = getDataAuthVO(strDataAuthPK);
	    	if( dataAuthVO != null ){
	    		//设置form
		    	DataAuthEditForm form = (DataAuthEditForm) actionForm;
		    	setUpdateForm(form, dataAuthVO);
		        return new ActionForward(DimDataAuthEditDlg.class.getName());
	    	}else{
	    		return new ErrorForward(StringResource.getStringResource("mbiauth0006"));//授权已经被删除,无法修改"));
	    	}
    	}catch(BusinessException e){
    		return new ErrorForward(e.getMessage());
    	}
    }
    /**
     * <MethodDescription>保存
     * zyjun
     * 2006-04-18
     */
    public ActionForward save(ActionForm actionForm){
    	try{
	        DataAuthEditForm 	form = (DataAuthEditForm) actionForm;
	        if( form.getAuthPK() == null || form.getAuthPK().length()==0){
	        	doCreate(form);
	        }else{
	        	//修改
	        	doUpdate(form);
	        }
	        return  new CloseForward(CloseForward.CLOSE_REFRESH_MAIN);
    	}catch(BusinessException e){
    		return  new ErrorForward(e.getMessage());
    	}
    }
    
    protected  void doCreate(DataAuthEditForm form) throws BusinessException {
    	//新建,需要分清楚哪些是新建,哪些是修改
    	String[]			strUserPKs = DataAuthToolKit.getIDs(form.getAutheePKs());
    	DataAuthVO[][]		dataAuthVOs = getCreateAndUpdateVOs(strUserPKs, form);
    	if( dataAuthVOs[0] != null ){
    		DataAuthSrv.getInstance().createDataAuthes(dataAuthVOs[0]);
    	}
    	if( dataAuthVOs[1] != null ){
    		DataAuthSrv.getInstance().updateDataAuthes(dataAuthVOs[1]);
    	}
    	
    }
    protected  void doUpdate(DataAuthEditForm form) throws BusinessException {
    	//修改
    	DataAuthVO		dataAuthVO = new DataAuthVO();
    	setFormToDataAuthVO(form, dataAuthVO);
    	DataAuthSrv.getInstance().updateDataAuth(dataAuthVO);
    }

       
   /**
    * Form值校验,userRefList内容不能为空,
    * include 不能为空
    * @param actionForm
    * @param errors 值校验失败的提示信息集合
    */
    @SuppressWarnings("unchecked")
	public void validate(ArrayList errors, ActionForm actionForm){
    	DataAuthEditForm form = (DataAuthEditForm) actionForm;
    	String	strUserPKs = form.getAutheePKs();
    	if( strUserPKs == null || strUserPKs.length() == 0){
    		errors.add(StringResource.getStringResource("mbiauth0008"));//请选择用户"));
    	}
    	if( !form.isIncludeAncestor() && !form.isIncludeChild() && !form.isIncludeOffSpring()
    		 && !form.isIncludeParent() && !form.isIncludeSelf() && !form.isIncludeSlibe() ){
    		errors.add(StringResource.getStringResource("mbiauth0009"));//请至少选择一项包含内容"));
    	}
    }        
       
   /**
    * 关联Form
    *
    */   
    public String getFormName(){
        return nc.ui.bi.dataauth.DataAuthEditForm.class.getName();
    }

	/**
	 * 设置新建时的form内容
	 * @param form
	 */
	protected void setCreateForm(DataAuthEditForm form){
    	form.setUpdate(false);
    	form.setDimPK(getRequestParameter(IDataAuthConst.DIMPK));
    	form.setDimMemberPK(getTreeSelectedID());
    	form.setType(IDataAuthConst.READ);
    	DataAuthToolKit.setIncludeRule(form, null);
    	form.setLangCode(getLanguageCode());		
	}
	/**
	 * 设置修改时的form内容
	 * @param form
	 * @param dataAuthVO
	 */
	protected void setUpdateForm(DataAuthEditForm form, IDataAuthVO  dataAuthVO){
    	form.setUpdate(true);
    	form.setAuthPK(dataAuthVO.getID());
    	form.setDimMemberPK(dataAuthVO.getDimMemberPK());
    	form.setDimPK(dataAuthVO.getDimPK());
    	if( dataAuthVO.getType() != null ){
    		form.setType(dataAuthVO.getType().intValue());
    	}
    	DataAuthToolKit.setIncludeRule(form, dataAuthVO.getRule());
    	
    	String	strUserPK = dataAuthVO.getAuthee();
    	String	strUserCode = DataAuthToolKit.getUserCode(strUserPK);
    	form.setAutheeListItems(new String[][]{{strUserPK, strUserCode}});
    	form.setLangCode(getLanguageCode());
		
	}
	/**
	 * 根据form 设置VO
	 * @param form
	 * @return
	 */
	protected void   setFormToDataAuthVO(DataAuthEditForm form, IDataAuthVO dataAuthVO){
		dataAuthVO.setID(form.getAuthPK());
		dataAuthVO.setDimPK(form.getDimPK());
		dataAuthVO.setDimMemberPK(form.getDimMemberPK());
		dataAuthVO.setAuthor(getCurUserInfo().getID());
		dataAuthVO.setAuthee(form.getAutheePKs());
		dataAuthVO.setType(new Integer(form.getType()) );
		dataAuthVO.setAuthorizable(Boolean.FALSE);
		
		//设置规则
		int			nRule = DataAuthToolKit.getIncludeRule(form);
		dataAuthVO.setRule(new Integer(nRule));
	}
	/**
	 * 根据PK得到VO
	 * @param strPK
	 * @return
	 * @throws BusinessException
	 */
	protected  IDataAuthVO  getDataAuthVO(String strPK) throws BusinessException{
		return DataAuthSrv.getInstance().getDimAuthByPK(strPK);
	}
	/**
	 * 针对选择的用户集合,查询数据库,将需要新建和修改的VO分开
	 * @param strUserPKs
	 * @param form
	 * @return
	 * @throws BusinessException
	 */
	private DataAuthVO[][]  getCreateAndUpdateVOs(String[] strUserPKs, DataAuthEditForm form) throws BusinessException{
		String		strDimPK = form.getDimPK();
		String		strMemberPK = form.getDimMemberPK();
    	ArrayList<DataAuthVO>	aryCreates = new ArrayList<DataAuthVO>();
    	ArrayList<DataAuthVO>	aryUpdates = new ArrayList<DataAuthVO>();
    	for( int i=0; i<strUserPKs.length; i++ ){
    		DataAuthVO	authVO = new DataAuthVO();
    		setFormToDataAuthVO(form, authVO);
    		authVO.setPk_user(strUserPKs[i]);
    		DataAuthVO	authOldVO = DataAuthSrv.getInstance().getDimAuthesByMemberUser(strDimPK, strMemberPK, strUserPKs[i]);
    		if( authOldVO != null ){
    			authVO.setPk_dim_dataauth(authOldVO.getPk_dim_dataauth());
    			aryUpdates.add(authVO);
    		}else{
    			authVO.setPk_dim_dataauth(IDMaker.makeID(20));
    			aryCreates.add(authVO);
    		}
    	}
    	DataAuthVO[][]	results = new DataAuthVO[2][];
    	if( aryCreates.size() >0){
    		results[0] = new DataAuthVO[aryCreates.size()];
    		aryCreates.toArray(results[0]);
    	}
       	if( aryUpdates.size() >0){
    		results[1] = new DataAuthVO[aryUpdates.size()];
    		aryUpdates.toArray(results[1]);
    	} 
       	return results;
	}

}

/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <ActionVO name="DimDataAuthEditAction" package="nc.ui.bi.dataauth" 关联Form="nc.ui.bi.dataauth.DimDataAuthEditForm">
      <MethodsVO create="" save="" update="">
      </MethodsVO>
    </ActionVO>
@WebDeveloper*/