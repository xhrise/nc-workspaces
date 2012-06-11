/**
 * ResEditDirAction.java  1.0 
 * 
 * WebDeveloper自动生成.
 * 
 */
package nc.ui.iufo.resmng.uitemplate;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.iufo.resmng.common.ResUIBizHelper;
import nc.ui.iufo.resmng.common.ResWebEnvKit;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.iufo.resmng.uitemplate.describer.ModuleProductHome;
import nc.ui.iufo.resmng.uitemplate.describer.ResMenuInfo;
import nc.ui.iufo.resmng.uitemplate.describer.ResMngHome;
import nc.ui.iufo.resmng.uitemplate.describer.TableHeaderInfo;
import nc.util.iufo.resmng.IResMngConsants;
import nc.util.iufo.resmng.ResMngBizHelper;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResOperException;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.userrole.UserRoleUtil;
import com.ufsoft.iufo.web.DialogAction;

/**
 * 类作用描述文字 liulp 2005-11-30
 */
public abstract class ResEditObjAction extends DialogAction {
	protected static String METHOD_SAVE = "actSave";

    /**
     * Form值校验
     * @param actionForm
     * @return 值校验失败的提示信息集合
     */
     public String[] validate(ActionForm actionForm){
         return null;      
     }      
     /**
      * 传递数据Form的名称。
      * 
      * NOTICE:有框架外自定义Form的子类需要重写改方法
      */
     public String getFormName() {
        return ResTreeObjForm.class.getName();
     }
    /**
     * <MethodDescription>liulp 2005-11-30
     */
    public ActionForward execute(ActionForm actionForm) throws WebException {
        ActionForward actionForward = null;
        //USERCODE;
        IResTreeObjForm resTreeObjForm = changetoResTreeObjForm(actionForm);
    	String strSelectedID = getSelectedObjID(resTreeObjForm);
    	//已选节点ID是否为可编辑的ID
		isEditableSelID(strSelectedID);
		//检查权限
		//checkRight(strSelectedID);
		//END...
		
        IResTreeObject selResTreeObj = getSelectedObj(strSelectedID,resTreeObjForm);
        //#在注册中心根据模块和节点类型，装载显示的标签名称    
  
        initFormValue(actionForm, selResTreeObj);
        //end        
        actionForward = new ActionForward(getExecuteUI());
       
        return actionForward;
    }

	/**
     * 检查权限
	 * @param strSelectedID
	 */
	protected void checkRight(String strSelectedID) {
		AuthUIBizHelper.checkRight(strSelectedID,this);
	}

	/**
     * 得到新建或修改对象的界面类名:
     *  界面类重写的模块需要重写改方法
	 * @return
	 */
	protected String getExecuteUI() {
		return ResEditObjUI.class.getName();
	}
    /**
     * 获得操作界面的界面标题
     * 界面类重写的模块,可以根据需要决定是否重写改方法
     * @param actionForm
	 * @return
	 */
	protected  String getExeceUITitle(ActionForm actionForm){
		String strModuleDisName = ResMngHome.getInstance().getModuleDisNameValue(getModuleID());
        StringBuffer sbTitle = new StringBuffer(strModuleDisName);
        sbTitle.append(":");
        String strMenuOperResID = null;
        if(!isModify(actionForm)){
        	strMenuOperResID = ResMenuInfo.RESOURCE_ID_OF_MENU_ITEM_CREATE;
        }else{
        	strMenuOperResID = ResMenuInfo.RESOURCE_ID_OF_MENU_ITEM_UPDATE;
        }
        sbTitle.append(TableHeaderInfo.getValue(strMenuOperResID));
        sbTitle.append("->");
        String strObjName = null;
        if(isDir()){
        	strObjName = ResMngHome.getInstance().getDirNameValue(getModuleID());
        }else{
        	strObjName = ResMngHome.getInstance().getFileNameValue(getModuleID());
        }
        sbTitle.append(strObjName);
        
		return sbTitle.toString();
	}
	/**
	 * 子类可根据自身业务重写本方法：如适用于Form不是IResObjForm的模块
     * @param form
     * @param selResTreeObj
     */
    protected void initFormValue(ActionForm actionForm, IResTreeObject selResTreeObj) {
        IResTreeObjForm resTreeObjForm = changetoResTreeObjForm(actionForm);
        //设置tableSelectedID和treeSelectedID给validate方法调用（防止右表选项选择过多被web组件截掉这两参数的情况）
        resTreeObjForm.setBizTableSelectedID(getTableSelectedID());
        resTreeObjForm.setBizTreeSelectedID(getTreeSelectedID());
        //#在注册中心根据模块和节点类型，装载显示的标签名称
        resTreeObjForm.setLblName(getLabelObjName());
        resTreeObjForm.setLblNote(getLabelObjNote());

        //显示textField的值
        if (selResTreeObj != null) {
            resTreeObjForm.setID(selResTreeObj.getID());
            if(isModify(actionForm)){
            	resTreeObjForm.setName(selResTreeObj.getLabel());
            	resTreeObjForm.setNote(selResTreeObj.getNote());
            }
        }else{
            resTreeObjForm.setID(IResMngConsants.VIRTUAL_ROOT_ID);
        }
        //设置DirUI转向的模块编辑目录Action类名和方法名
        resTreeObjForm.setSubmitActionName(getSaveActionName());
        resTreeObjForm.setSubmitActionMethod(getSaveActionMethod());
        //设置模块标题显示名称
        resTreeObjForm.setModuleDisPlayName(getExeceUITitle(actionForm));
    }
    /**
     * 获得保存提交的ActionName
	 * @return
	 */
	protected abstract String getSaveActionName();
    /**
     * 获得保存提交Action的方法名
	 * @return
	 */
	protected  String getSaveActionMethod(){
		return METHOD_SAVE;
	}

	/**
     * 获得标签“对象名称”
     * @return
     */
    protected abstract String getLabelObjName();
    /**
     * 获得标签“对象说明”
     * @return
     */
    protected abstract String getLabelObjNote();
    
    /**
     * @param actionForm
     * @return
     */
    protected IResTreeObjForm changetoResTreeObjForm(ActionForm actionForm) {
        IResTreeObjForm resTreeObjForm = (ResTreeObjForm) actionForm;
        return resTreeObjForm;
    }

    /**
     * 检查objVO:是否可以进行编辑操作
     * 
     * @param objVO
     */
    protected abstract boolean isEditable(IResTreeObject resTreeObj);

    /**
     * 得到选择的树目录对象
     * 
     * @param resTreeObjForm
     * @return
     * @i18n miuforesmng0001=选择的{0}已被删除
     * @i18n miufores00004=此{0}不可以修改！
     */
    protected IResTreeObject getSelectedObj(String strSelectedID,IResTreeObjForm resTreeObjForm) {
//    	String strSelectedID = getSelectedObjID(resTreeObjForm);

//		isEditableSelID(strSelectedID);
        //获得选择树节点的操作   
		IResTreeObject selResTreeObj = doGetSelectedObj((ActionForm)resTreeObjForm,strSelectedID);
        if (selResTreeObj == null && !ResMngToolKit.isVitualRootDir(strSelectedID)) {
            //            throw new CommonException("miufo1002451"); //"选择的目录已被删除！"
            throw new WebException(StringResource.getStringResource("miuforesmng0001"), new String[] { resTreeObjForm.getName() });
        }
        if (isModify((ActionForm)resTreeObjForm)) {
            if (!isEditable(selResTreeObj)) {
                //                throw new CommonException("miufo1002455"); //"此目录不可以修改!"
                throw new WebException(StringResource.getStringResource("miufores00004"), new String[] { resTreeObjForm.getName() });
            }
        }
        return selResTreeObj;
    }
    
    /**
     * 获得选择的节点ID
	 * @param resTreeObjForm
	 * @return
	 */
	protected String getSelectedObjID(IResTreeObjForm resTreeObjForm) {
        String strSelectedID = null;
        if (resTreeObjForm.getID() != null) {
            //新建或修改提交
            strSelectedID = resTreeObjForm.getID();
        } else {
    		//新建或修改打开界面,得到当前界面树表选择的节点ID
        	if(isDir()){
        		strSelectedID = getTreeSelectedID();
        	}else{
                if(isModify((ActionForm)resTreeObjForm)){
                	strSelectedID = getTableSelectedID();
                }else{
                    strSelectedID = getTreeSelectedID();
                }
        	}
        }
		return strSelectedID;
	}

	/**
     * 获得选择树节点的操作
     * @param actionForm
	 * @param strSelectedID
	 * @return
	 */
	protected IResTreeObject doGetSelectedObj(ActionForm actionForm,String strSelectedID) {
		ITreeTableOperator treeTableOperator = TreeTableOperFactory.getTreeTableOper(this); 
        IResTreeObject selResTreeObj = null;
        try {
        	if(isDir() || !isModify(actionForm)){
        		selResTreeObj = treeTableOperator.getDir(strSelectedID);
        	}else{
        		selResTreeObj = treeTableOperator.getFile(strSelectedID);
        	}
        } catch (ResOperException e) {
AppDebug.debug(e);//@devTools             e.printStackTrace(System.out);
        }
		return selResTreeObj;
	}

	/**
     * 选择的节点是否可编辑
	 * @param strSelectedID
	 */
	protected  void isEditableSelID(String strSelectedID) throws WebException{
        //共享根目录不允许创建操作
        boolean bVitualRoot = ResMngToolKit.isVitualRootDir(strSelectedID);
        boolean bShareRoot = ResMngToolKit.isSharedRootTreeObj(strSelectedID);//ResourceMngUtil.isShareRootObj(treeModel,strSelItem);
        if((bShareRoot && !ResWebEnvKit.isModify(this))){
            throw new WebException("miuforesmng0013");  //"共享虚目录不允许进行创建操作！"
        }
        //虚根目录的创建操作控制
        if(bVitualRoot && !ResWebEnvKit.isModify(this)){
            if(!isDir() || !UserRoleUtil.isRoleAdministrator(getCurUserInfo()) ){
                throw new WebException("miuforesmng0104");//虚根目录只能创建目录，且必须是系统管理员            
            }
        }
    }
	
	protected boolean isDir(){
    	return ResWebEnvKit.isDir(this);
    }
    /**
     * <MethodDescription>liulp 2005-11-30
     */
    public ActionForward actSave(ActionForm actionForm) throws WebException {  
        ActionForward actionForward = null;
        //特殊字符不能正确在validate里提交，需要在这里特别调用
        String[] strErrors = validate(actionForm);
        if(strErrors != null && strErrors.length >0){
//            StringBuffer sbError = new StringBuffer();
//            for(int i =0;i<strErrors.length;i++){
//                sbError.append(strErrors[i]);
//            }
//            throw new WebException("sdfsdaf");
        	
        	return new ErrorForward(strErrors);
        }
        IResTreeObjForm resTreeObjForm = changetoResTreeObjForm(actionForm);
        //USERCODE
    	String strSelectedID = getSelectedObjID(resTreeObjForm);
    	//检查权限
		//checkRight(strSelectedID);
		//END
        IResTreeObject selResTreeObj = (IResTreeObject) getSelectedObj(strSelectedID,resTreeObjForm);

        try {
            if (isModify(actionForm)) {
                selResTreeObj = getPostVO(actionForm, selResTreeObj, true);
                doUpdateObj(selResTreeObj);
            } else {
                selResTreeObj = getPostVO(actionForm, selResTreeObj,false);
                selResTreeObj = doCreateObj(selResTreeObj);
                if(selResTreeObj != null){
                    String strNewTreeObjID = selResTreeObj.getID();
                    if(strNewTreeObjID != null && strNewTreeObjID.length() > 0){
                        resTreeObjForm.setID(strNewTreeObjID);
                        ResUIBizHelper.addSelTableID2Session(this,strNewTreeObjID);
                    }
                }
            }
        } catch (ResOperException e1) {
        	throw new WebException(e1.getMessage());
        }

        actionForward = getSaveActForward(actionForm);

        return actionForward;
    }
    /**
     * 得到保存方法的ActionForward
     * @param actionForm
	 * @return
	 */
	protected ActionForward getSaveActForward(ActionForm actionForm) {
		ActionForward actionForward = null;
		
		 String strSelectedTableObjID = null;
		 if(!isModify(actionForm)){
			 strSelectedTableObjID = ResUIBizHelper.getSelTableIDFromSession(this);
		 }
		
        boolean bRefreshParent = isDir() && ModuleProductHome.getInstance().isAppDirModule(getModuleID());
         
        actionForward = getCloseForward(strSelectedTableObjID, bRefreshParent);
        
        return actionForward;
	}

    /**
     * @param strSelectedTableObjID
     * @param bRefreshParent
     * @return
     */
    public static ActionForward getCloseForward(String strSelectedTableObjID, boolean bRefreshParent) {
        ActionForward actionForward;
        String spt = "";
        if(strSelectedTableObjID != null){
            spt = "setTableSelectedID('" + strSelectedTableObjID + "', opener.document);";
        }
        if(bRefreshParent){
        //	actionForward = new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
        	spt += CloseForward.CLOSE_REFRESH_PARENT;
        }else{
        	//actionForward = new CloseForward(CloseForward.CLOSE_REFRESH_MAIN);
        	spt += CloseForward.CLOSE_REFRESH_MAIN;
        }
        actionForward =  new CloseForward(spt);
        return actionForward;
    }

	/**
     * 执行创建节点操作
	 * @param selResTreeObj
     * @throws ResOperException
	 */
	protected IResTreeObject doCreateObj(IResTreeObject selResTreeObj) throws ResOperException {
		if(selResTreeObj == null){
			return null;
		}

		ITreeTableOperator treeTableOperator = TreeTableOperFactory.getTreeTableOper(this); 
        if(isDir()){
        	treeTableOperator.createDir(selResTreeObj);
        	return null;
        }else{
        	return treeTableOperator.createFile(selResTreeObj);                	
        }
		
	}
    /**
     * 执行更新节点操作
	 * @param selResTreeObj
     * @throws ResOperException
	 */
	protected void doUpdateObj(IResTreeObject selResTreeObj) throws ResOperException {
		if(selResTreeObj == null){
			return;
		}
		ITreeTableOperator treeTableOperator = TreeTableOperFactory.getTreeTableOper(this); 
        if(isDir()){
        	treeTableOperator.updateDir(selResTreeObj);
        }else{
        	treeTableOperator.updateFile(selResTreeObj);
        }
		
	}

	/**
     * 是否为“修改”操作
	 * @return
	 */
	protected boolean isModify(ActionForm actionForm) {
		return ResWebEnvKit.isModify(this);
	}

	protected String getModuleID(){
    	return ResWebEnvKit.getModuleID(this);
    }
    private IResTreeObject getPostVO(ActionForm actionForm,
            IResTreeObject selResTreeObj, boolean bModify) throws ResOperException {
        IResTreeObjForm resTreeObjForm = changetoResTreeObjForm(actionForm);
        ResWebParam resWebParam = getModuleInfoFromSession();
        ValueObject operateVO = null;
        if (bModify) {
            operateVO = doGetUpdateVO(resTreeObjForm,selResTreeObj.getSrcVO());
        } else {
            String strParentVOPK  = null;
            if(selResTreeObj != null)
            	strParentVOPK = ResMngToolKit.getVOIDByTreeObjectID(selResTreeObj.getID());
            else{
            	strParentVOPK = IResMngConsants.VIRTUAL_ROOT_ID;
            }
            operateVO = doGetNewVO(strParentVOPK,resTreeObjForm,resWebParam);
        }
        //#得到IResTreeObj对象
//        ITreeObject treeObj = ResourceMngUtil.getTreeObject((DirVO)dirVO,resWebParam.getModuleID(),ITreeObject.OBJECT_TYPE_FILE,null);
        String strResTreeObjID = selResTreeObj!=null?selResTreeObj.getID():null;
        return doChangetoResTreeObj(operateVO,resWebParam.getModuleID(),strResTreeObjID);
    }
	/**
	 * @return
	 * @throws UISrvException
	 */
	private ResWebParam getModuleInfoFromSession(){
		return ResUIBizHelper.getModuleInfoFromSession(this);
	}


	/**
     * 将VO转换为IResTreeObject
     * 
     * @param operateVO
     * @param strModuleID
     * @param strResTreeObjID
     * @return
     * @throws ResOperException
     */
    protected IResTreeObject doChangetoResTreeObj(ValueObject operateVO,String strModuleID,String strResTreeObjID) throws ResOperException{
        if(operateVO == null || strModuleID == null){
        	return null;
        }
        
    	String strShareFlag = ResMngToolKit.getShareFlagOfTreeObjID(strResTreeObjID);
        IResTreeObject resTreeObj = null;
        if(isDir()){
        	resTreeObj = ResMngBizHelper.getDirTreeObj(operateVO,strModuleID,strShareFlag);
        }else{
        	resTreeObj = ResMngBizHelper.getFileTreeObj(operateVO,strModuleID,strShareFlag);
        }
        
        return resTreeObj;

    }
    
    /**
     * 
     * @param resTreeObjForm
     * @param srcVO
     * @return
     */
    protected abstract ValueObject doGetUpdateVO(IResTreeObjForm resTreeObjForm, ValueObject srcVO);
    /**
     * 
     * @param strParentVOPK
     * @param resTreeObjForm
     * @param resWebParam
     * @return
     */
    protected abstract ValueObject doGetNewVO(String strParentVOPK,
    		IResTreeObjForm resTreeObjForm,ResWebParam resWebParam);
    
}

/**
 * @WebDeveloper <?xml version="1.0" encoding='gb2312'?> <ActionVO
 *               name="ResEditDirAction"
 *               package="nc.ui.resourcemng.treetable.webdemo"
 *               关联Form="nc.ui.resourcemng.treetable.webdemo.ResEidtDirForm">
 *               <MethodsVO
 *               execute="nc.ui.resourcemng.treetable.webdemo.ResEditDirUI"
 *               save="CloseForward(CloseForward.CLOSE_REFRESH_PARENT)">
 *               </MethodsVO> </ActionVO>
 * @WebDeveloper
 */