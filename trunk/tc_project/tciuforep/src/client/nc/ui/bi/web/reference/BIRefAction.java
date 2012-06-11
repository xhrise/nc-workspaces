package nc.ui.bi.web.reference;

import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.uitemplate.IWebTreeLoader;
import nc.ui.iufo.resmng.uitemplate.TreeTableOperFactory;
import nc.ui.iufo.web.reference.base.BDRefAction;
import nc.ui.iufo.web.reference.base.BusinessRefForm;
import nc.ui.iufo.web.reference.base.TreeRefDlg;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.resmng.ResMngBizHelper;
import nc.util.iufo.resmng.loader.LoaderParam;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimensionException;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.resmng.uitemplate.ResOperException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.comp.tree.WebTreeNode;
import com.ufsoft.iufo.resource.StringResource;

public class BIRefAction  extends BDRefAction {
	public final static String METHOD_DIM_LSIT_REF = "getDimListRef";
	//public final static String METHOD_DIM_FIELD_TREE_REF = "getDimFieldTreeRef";
	public final static String DIM_REF_RETURN_TYPE = "returnType"; // 代码参照返回类型
	public final static String DIM_ID = "DimID"; // 根据维度的ID来进行参照
	public final static String DIM_NAME = "DimName"; // 根据维度的名称来进行参照
	public final static String PARAM_AUTH_TYPE = "authType";//权限类型
	public final static String PARAM_MEMBER_SELTYPE ="memberSelType";
	public final static String SELECT_LEAF ="1";

	
	/**
	 * @i18n mbiweb00004=维度结构
	 */
	private static final String str_dim_struct = StringResource.getStringResource("mbiweb00004");	
	/**
	 * @i18n ubidim0008=维度
	 */
	private static final String str_dim = StringResource.getStringResource("ubidim0008");
	/**
	 *  获得当前用户所有权限可以看到的维度结构参照
	 */
	public ActionForward getDimListRef(ActionForm actionForm)throws WebException {
		ActionForward actionForward = null;
		try {
			DimensionVO[] dimVOs = DimensionSrv.getInstance().getAllDimVOs();
			WebTreeModel model = new WebTreeModel();
			WebTreeNode rootNode = new WebTreeNode("rootId", null,str_dim_struct); // 维度结构
			rootNode.setUrl("");
			model.setRoot(rootNode);
			WebTreeNode itemNode = null;
			
//			 默认为返回代码Id
			boolean bRetCodeId = true;
			String strRetType = getRequestParameter(DIM_REF_RETURN_TYPE);
			if (strRetType != null && DIM_NAME.equals(strRetType)) {
				bRetCodeId = false;
			}			
			if (dimVOs != null && dimVOs.length > 0) {
				int nSize = dimVOs.length;
				String strDimPropPrefix =str_dim + "->";
				for (int i = 0; i < nSize; i++) {
					String name = strDimPropPrefix + dimVOs[i].getDimname();
					itemNode = null;				
					if (bRetCodeId) {
						itemNode = new WebTreeNode(dimVOs[i].getDimID(), rootNode.getID(),
								name);
					} else {
						itemNode = new WebTreeNode(dimVOs[i].getDimname(), rootNode.getID(),
								name);
					}
					rootNode.add(itemNode);	
				}
			}
			model.setTreeType(WebTreeModel.TYPE_REFERENCE);
			BusinessRefForm form = (BusinessRefForm) actionForm;
			form.setTreeModel(model);
			actionForward = new ActionForward(TreeRefDlg.class.getName());
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		return actionForward;
	}
	
	/**
	 *  获得当前用户所有权限可以看到的维度字段参照
	 */
	public ActionForward getDimFieldTreeRef(ActionForm actionForm)throws WebException {
		ActionForward actionForward = null;
//		try {
//			DimensionVO[] dimVOs = DimensionSrv.getInstance().getReferencableDims();
//			WebTreeModel model = new WebTreeModel();
//			WebTreeNode rootNode = new WebTreeNode("rootId", null,str_dim_struct); // 维度结构
//			rootNode.setUrl("");
//			model.setRoot(rootNode);
//			WebTreeNode itemNode = null;
//			if (dimVOs != null && dimVOs.length > 0) {
//				int nSize = dimVOs.length;
//				String strDimPropPrefix =str_dim + "->";
//				for (int i = 0; i < nSize; i++) {
//					String data = strDimPropPrefix + dimVOs[i].getDimname();
//					itemNode = new WebTreeNode(data, rootNode.getID(), data);
//					rootNode.add(itemNode);
//				}
//
//			}
//			model.setTreeType(WebTreeModel.TYPE_REFERENCE);
//			BusinessRefForm form = (BusinessRefForm) actionForm;
//			form.setTreeModel(model);
//			actionForward = new ActionForward(TreeRefDlg.class.getName());
//		} catch (Exception e) {
//			AppDebug.debug(e);
//		}
		actionForward = new ActionForward(DimFieldRefAction.class.getName(), "");
		return actionForward;
	}
	/**
	 * 查询目录参照
	 * @param actionForm
	 * @return
	 */
	public ActionForward  getQueryDirRef(ActionForm actionForm){

		return getDirRef(IBIResMngConstants.MODULE_QUERY, actionForm);
	}
	/**
	 * BI报表目录参照
	 * @param actionForm
	 * @return
	 */
	public ActionForward  getReportDirRef(ActionForm actionForm){
		return getDirRef(IBIResMngConstants.MODULE_REPORT, actionForm);
	}
	
	private ActionForward getDirRef(String strModuleID, ActionForm actionForm){
		WebTreeModel	model = getTreeModel(strModuleID, getRequestedAuthType());
		model.setTreeType(WebTreeModel.TYPE_REFERENCE);
		
		BusinessRefForm form = (BusinessRefForm) actionForm;
		form.setTreeModel(model);
		return new ActionForward(TreeRefDlg.class.getName());
	}
	
	private WebTreeModel getTreeModel(String strModuleID, int nRightType){
    	WebTreeModel treeModel = new WebTreeModel();    	
		
		try {  	  
			String strOperator = getCurUserInfo().getID();
			String strResOwnerPK = ResMngBizHelper.getResOwnerPK(strModuleID, strOperator);
			ResWebParam webParam = new ResWebParam(strModuleID, strResOwnerPK, strOperator,getCurOrgPK()); 
	    	
			IWebTreeLoader treeLoader = TreeTableOperFactory.getTreeTableOper(webParam);
    		boolean bHaveResMngRight = AuthUIBizHelper.haveResMngRight(strModuleID,getCurUserInfo());

//            int nRightType = IAuthorizeTypes.AU_TYPE_VIEW;//
    		LoaderParam loaderParam = new LoaderParam(webParam.getResOwnerPK(), bHaveResMngRight, nRightType,false);

            boolean bIncludeFile = false;
            boolean bRef = true;
    	    treeModel = (WebTreeModel)treeLoader.loadTreeModel(bIncludeFile,bRef, loaderParam, null);
        } catch (ResOperException e) {
            AppDebug.debug(e);
            throw new WebException(e.getMessage());
        }
    	
    	//set treeModel's nodes
    	return treeModel;
	}
	/**
	 * 维度成员参照
	 * @param actionForm
	 * @return
	 * @throws WebException
	 */
	public ActionForward getDimMemberRef(ActionForm actionForm)throws WebException{
		//得到维度ID
		String		strDimID = getRequestParameter(DIM_ID);
		if( strDimID != null ){
			DimensionVO dimVO = null;
		
			try{
				dimVO = DimensionSrv.getInstance().getDimByID(strDimID);
			}catch(Exception e){
				throw new DimensionException(DimensionException.ERR_DATABASE);
			}
			if (dimVO == null) {
				throw new DimensionException(DimensionException.ERR_NO_DIMDEF);
			}

			DimMemberSrv srv = new DimMemberSrv(dimVO);
			//TODO 用户权限
			DimMemberVO[] memVOS = srv.getAll();
			WebTreeModel treeModel = new WebTreeModel(memVOS);
			treeModel.setTreeType(WebTreeModel.TYPE_REFERENCE);
			//得到选择类型
			String	strSelType = getRequestParameter(PARAM_MEMBER_SELTYPE);
			if( SELECT_LEAF.equals(strSelType)){
				WebTreeNode	root = (WebTreeNode)treeModel.getRoot();
				disableNode(root);	
			}
			
			BusinessRefForm form = (BusinessRefForm) actionForm;
			form.setTreeModel(treeModel);
			return new ActionForward(TreeRefDlg.class.getName());

		}
		return null;
	}
	private int getRequestedAuthType(){
		String	strAuthType = getRequestParameter(PARAM_AUTH_TYPE);
		if( strAuthType != null && strAuthType.length()>0 ){
			return Integer.parseInt(strAuthType);
		}else{
			return IAuthorizeTypes.AU_TYPE_VIEW;
		}
		
	}
	private void disableNode(WebTreeNode node){
		int		nChildren = node.getChildCount();
		if( nChildren >0){
			node.setUrl("");
			for( int i=0; i<nChildren; i++ ){
				WebTreeNode		child = (WebTreeNode)node.getChildAt(i);
				disableNode(child);
			}
		}
	}
}
 