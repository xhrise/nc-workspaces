/**
 * ResEditDirAction.java  1.0 
 * 
 * WebDeveloper�Զ�����.
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
 * �������������� liulp 2005-11-30
 */
public abstract class ResEditObjAction extends DialogAction {
	protected static String METHOD_SAVE = "actSave";

    /**
     * FormֵУ��
     * @param actionForm
     * @return ֵУ��ʧ�ܵ���ʾ��Ϣ����
     */
     public String[] validate(ActionForm actionForm){
         return null;      
     }      
     /**
      * ��������Form�����ơ�
      * 
      * NOTICE:�п�����Զ���Form��������Ҫ��д�ķ���
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
    	//��ѡ�ڵ�ID�Ƿ�Ϊ�ɱ༭��ID
		isEditableSelID(strSelectedID);
		//���Ȩ��
		//checkRight(strSelectedID);
		//END...
		
        IResTreeObject selResTreeObj = getSelectedObj(strSelectedID,resTreeObjForm);
        //#��ע�����ĸ���ģ��ͽڵ����ͣ�װ����ʾ�ı�ǩ����    
  
        initFormValue(actionForm, selResTreeObj);
        //end        
        actionForward = new ActionForward(getExecuteUI());
       
        return actionForward;
    }

	/**
     * ���Ȩ��
	 * @param strSelectedID
	 */
	protected void checkRight(String strSelectedID) {
		AuthUIBizHelper.checkRight(strSelectedID,this);
	}

	/**
     * �õ��½����޸Ķ���Ľ�������:
     *  ��������д��ģ����Ҫ��д�ķ���
	 * @return
	 */
	protected String getExecuteUI() {
		return ResEditObjUI.class.getName();
	}
    /**
     * ��ò�������Ľ������
     * ��������д��ģ��,���Ը�����Ҫ�����Ƿ���д�ķ���
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
	 * ����ɸ�������ҵ����д����������������Form����IResObjForm��ģ��
     * @param form
     * @param selResTreeObj
     */
    protected void initFormValue(ActionForm actionForm, IResTreeObject selResTreeObj) {
        IResTreeObjForm resTreeObjForm = changetoResTreeObjForm(actionForm);
        //����tableSelectedID��treeSelectedID��validate�������ã���ֹ�ұ�ѡ��ѡ����౻web����ص����������������
        resTreeObjForm.setBizTableSelectedID(getTableSelectedID());
        resTreeObjForm.setBizTreeSelectedID(getTreeSelectedID());
        //#��ע�����ĸ���ģ��ͽڵ����ͣ�װ����ʾ�ı�ǩ����
        resTreeObjForm.setLblName(getLabelObjName());
        resTreeObjForm.setLblNote(getLabelObjNote());

        //��ʾtextField��ֵ
        if (selResTreeObj != null) {
            resTreeObjForm.setID(selResTreeObj.getID());
            if(isModify(actionForm)){
            	resTreeObjForm.setName(selResTreeObj.getLabel());
            	resTreeObjForm.setNote(selResTreeObj.getNote());
            }
        }else{
            resTreeObjForm.setID(IResMngConsants.VIRTUAL_ROOT_ID);
        }
        //����DirUIת���ģ��༭Ŀ¼Action�����ͷ�����
        resTreeObjForm.setSubmitActionName(getSaveActionName());
        resTreeObjForm.setSubmitActionMethod(getSaveActionMethod());
        //����ģ�������ʾ����
        resTreeObjForm.setModuleDisPlayName(getExeceUITitle(actionForm));
    }
    /**
     * ��ñ����ύ��ActionName
	 * @return
	 */
	protected abstract String getSaveActionName();
    /**
     * ��ñ����ύAction�ķ�����
	 * @return
	 */
	protected  String getSaveActionMethod(){
		return METHOD_SAVE;
	}

	/**
     * ��ñ�ǩ���������ơ�
     * @return
     */
    protected abstract String getLabelObjName();
    /**
     * ��ñ�ǩ������˵����
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
     * ���objVO:�Ƿ���Խ��б༭����
     * 
     * @param objVO
     */
    protected abstract boolean isEditable(IResTreeObject resTreeObj);

    /**
     * �õ�ѡ�����Ŀ¼����
     * 
     * @param resTreeObjForm
     * @return
     * @i18n miuforesmng0001=ѡ���{0}�ѱ�ɾ��
     * @i18n miufores00004=��{0}�������޸ģ�
     */
    protected IResTreeObject getSelectedObj(String strSelectedID,IResTreeObjForm resTreeObjForm) {
//    	String strSelectedID = getSelectedObjID(resTreeObjForm);

//		isEditableSelID(strSelectedID);
        //���ѡ�����ڵ�Ĳ���   
		IResTreeObject selResTreeObj = doGetSelectedObj((ActionForm)resTreeObjForm,strSelectedID);
        if (selResTreeObj == null && !ResMngToolKit.isVitualRootDir(strSelectedID)) {
            //            throw new CommonException("miufo1002451"); //"ѡ���Ŀ¼�ѱ�ɾ����"
            throw new WebException(StringResource.getStringResource("miuforesmng0001"), new String[] { resTreeObjForm.getName() });
        }
        if (isModify((ActionForm)resTreeObjForm)) {
            if (!isEditable(selResTreeObj)) {
                //                throw new CommonException("miufo1002455"); //"��Ŀ¼�������޸�!"
                throw new WebException(StringResource.getStringResource("miufores00004"), new String[] { resTreeObjForm.getName() });
            }
        }
        return selResTreeObj;
    }
    
    /**
     * ���ѡ��Ľڵ�ID
	 * @param resTreeObjForm
	 * @return
	 */
	protected String getSelectedObjID(IResTreeObjForm resTreeObjForm) {
        String strSelectedID = null;
        if (resTreeObjForm.getID() != null) {
            //�½����޸��ύ
            strSelectedID = resTreeObjForm.getID();
        } else {
    		//�½����޸Ĵ򿪽���,�õ���ǰ��������ѡ��Ľڵ�ID
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
     * ���ѡ�����ڵ�Ĳ���
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
     * ѡ��Ľڵ��Ƿ�ɱ༭
	 * @param strSelectedID
	 */
	protected  void isEditableSelID(String strSelectedID) throws WebException{
        //�����Ŀ¼������������
        boolean bVitualRoot = ResMngToolKit.isVitualRootDir(strSelectedID);
        boolean bShareRoot = ResMngToolKit.isSharedRootTreeObj(strSelectedID);//ResourceMngUtil.isShareRootObj(treeModel,strSelItem);
        if((bShareRoot && !ResWebEnvKit.isModify(this))){
            throw new WebException("miuforesmng0013");  //"������Ŀ¼��������д���������"
        }
        //���Ŀ¼�Ĵ�����������
        if(bVitualRoot && !ResWebEnvKit.isModify(this)){
            if(!isDir() || !UserRoleUtil.isRoleAdministrator(getCurUserInfo()) ){
                throw new WebException("miuforesmng0104");//���Ŀ¼ֻ�ܴ���Ŀ¼���ұ�����ϵͳ����Ա            
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
        //�����ַ�������ȷ��validate���ύ����Ҫ�������ر����
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
    	//���Ȩ��
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
     * �õ����淽����ActionForward
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
     * ִ�д����ڵ����
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
     * ִ�и��½ڵ����
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
     * �Ƿ�Ϊ���޸ġ�����
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
        //#�õ�IResTreeObj����
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
     * ��VOת��ΪIResTreeObject
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
 *               ����Form="nc.ui.resourcemng.treetable.webdemo.ResEidtDirForm">
 *               <MethodsVO
 *               execute="nc.ui.resourcemng.treetable.webdemo.ResEditDirUI"
 *               save="CloseForward(CloseForward.CLOSE_REFRESH_PARENT)">
 *               </MethodsVO> </ActionVO>
 * @WebDeveloper
 */