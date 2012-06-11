package nc.ui.bi.web.reference;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.pub.iufo.cache.base.BDCacheManager;
import nc.pub.iufo.cache.base.UserCache;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.uitemplate.IWebTreeLoader;
import nc.ui.iufo.resmng.uitemplate.TreeTableOperFactory;
import nc.ui.iufo.resmng.uitemplate.describer.TableHeaderInfo;
import nc.ui.iufo.web.reference.base.BusinessRefForm;
import nc.ui.iufo.web.reference.base.TreeTableRef;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.resmng.ResMngBizHelper;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.util.iufo.resmng.loader.ITableLoader;
import nc.util.iufo.resmng.loader.LoaderParam;
import nc.util.iufo.resmng.loader.TableLoaderParam;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.integration.dimension.IDimension;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResOperException;
import nc.vo.iufo.user.UserInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.table.WebTableModel;
import com.ufida.web.comp.tree.IWebTreeModel;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.MultiFrameAction;
/**
 * 维度创建时引用维度参照
 * @author caijie 
 */
public class DimensionRefRefAction extends MultiFrameAction{


	/**
	 * 可以参考的维度的类型
	 */
	public static final String PARAM_REFER_DIM_TYPE = "paramReferDimType";
	
    /**
	 * @i18n mbiweb00002=维度引用参照
	 */
    public ActionForward execute(ActionForm actionForm) throws WebException {
        ActionForward actionForward = null;     
       
        BusinessRefForm form = (BusinessRefForm)actionForm;
        form.setRefTitle(StringResource.getStringResource(StringResource.getStringResource("mbiweb00002")));  //"维度引用参照"       
        actionForward = new ActionForward(TreeTableRef.class.getName());
        return actionForward;    	
    }
    
    /**
     * 
     */
	public Object[] getTableColumns() {
   		String[] strHearderNames =  new String[] {
   				StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_DIMCODE),
   				StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_DIMNAME),
   				StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_DIMTYPE),
   				StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_REFDIM),
   				StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_DIMOWNER),
   				StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_DIMNOTE)};
   		return strHearderNames;
	}
	@SuppressWarnings("unchecked")
	public IWebTableModel getTableModel() {    	
    	WebTableModel tableModel = new WebTableModel();
    	String strTreeObjId = getTreeSelectedID();
    	Hashtable referableDimsMap = new Hashtable();
    	try {
    		Integer  refDimType = IDimension.STANDARD_DIMENSION_TYPE;
    		try {
				if(getRequestParameter(PARAM_REFER_DIM_TYPE) != null){
					refDimType = Integer.valueOf(getRequestParameter(PARAM_REFER_DIM_TYPE));    			
				}
			} catch(Exception e) {				
				AppDebug.debug(e);
			}
			DimensionVO[] allReferableDims = DimensionSrv.getInstance().getReferencableDims();
			if(allReferableDims != null){
				for(int i = 0; i < allReferableDims.length; i++){
					if(refDimType.equals(allReferableDims[i].getDimensionType())){
						referableDimsMap.put(allReferableDims[i].getDimID(), allReferableDims[i]);
					}
				}
			}			
		} catch (Exception e1) {			
			AppDebug.debug(e1);
		}
    	
    	try{
        	tableModel.setRefDisplayColumn(1);
        	//set tableModel's data
        	if(strTreeObjId!=null){
        		String strQueryDirPK = ResMngToolKit.getVOIDByTreeObjectID(strTreeObjId);
        		int nTreeObjType = ResMngToolKit.getTreeObjType(strTreeObjId);
        		if(nTreeObjType == IResTreeObject.OBJECT_TYPE_DIR){
        			String strModeuleId = getModuleID();
        			String strOperator = getCurUserInfo().getID();
        			String strResOwnerPK = ResMngBizHelper.getResOwnerPK(strModeuleId, strOperator); 		
        			ResWebParam webParam = new ResWebParam(strModeuleId, strResOwnerPK, strOperator,getCurOrgPK()); 
        			
    				ITableLoader tableLoader = ResMngBizHelper.getTableLoader(strModeuleId);
    		        TableLoaderParam tableLoaderParam = new TableLoaderParam(webParam.getResOwnerPK(),webParam.getOrgPK(),null,strQueryDirPK);
    				IResTreeObject[] fileResTreeObjs = tableLoader.loadResTreeObjs(tableLoaderParam);//strQueryDirPK, webParam.getResOwnerPK(),null);
 
    				if(fileResTreeObjs!=null && fileResTreeObjs.length>0){
    					ArrayList vRowData=new ArrayList();
    		            for(int i=0; i<fileResTreeObjs.length; i++){
    		            	DimensionVO 	dimVO = (DimensionVO)fileResTreeObjs[i].getSrcVO();      
    		            	if(referableDimsMap.containsKey(dimVO.getDimID()) == false) continue;
    		            	String[] 		oneRowData=new String[7];    		            	
    		    			int				nIndex = 0;
    		    			oneRowData[nIndex++] = dimVO.getDimID();
    		    			oneRowData[nIndex++] = dimVO.getDimcode();
    		    			oneRowData[nIndex++] = dimVO.getDimname();    		    			
    		    			if(  dimVO.getDimensionType().intValue() ==DimensionVO.TIME_DIMENSION_TYPE ){
    		    				oneRowData[nIndex++] = StringResource.getStringResource("ubidim0005");//"时间维度");
    		    			}else{
    		    				oneRowData[nIndex++] = StringResource.getStringResource("ubidim0006");//标准维度");
    		    			}
    		    			//引用维度
    		    			oneRowData[nIndex] = "";
    		    			String		strRefDimName = "";
    		    			try{
    		    				if( dimVO.getReferDim() != null && dimVO.getReferDim().length() >0){
    		    					DimensionVO refDimVO = DimensionSrv.getInstance().getDimByID(dimVO.getReferDim());
    		    					if( refDimVO != null ){
    		    						strRefDimName = refDimVO.getDimname();
    		    					}
    		    				}
    		    			}catch(Exception e){
    		    				AppDebug.debug(e);
    		    			}
    		    			oneRowData[nIndex++] = strRefDimName;
    		    			
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
    		    			oneRowData[nIndex ++ ] = strOwnerUser;

    		    			oneRowData[nIndex] = dimVO.getNote();   
    		            	
    		            	vRowData.add(oneRowData);
    		            }
    		            tableModel.setDatas((String[][])vRowData.toArray(new String[vRowData.size()][3]));    	            
    				}
    	        }    	            
    		}
    	}catch(Exception e){
    		AppDebug.debug(e);
    	}
    	return tableModel;
    			
	}
		public IWebTreeModel getTreeModel() {
    	IWebTreeModel treeModel = new WebTreeModel();    	
		try {  	  
			String strModeuleId = getModuleID();
			String strOperator = getCurUserInfo().getID();
			String strResOwnerPK = ResMngBizHelper.getResOwnerPK(strModeuleId, strOperator);
			ResWebParam webParam = new ResWebParam(strModeuleId, strResOwnerPK, strOperator,getCurOrgPK()); 
	    	
			IWebTreeLoader treeLoader = TreeTableOperFactory.getTreeTableOper(webParam);
    		boolean bHaveResMngRight = AuthUIBizHelper.haveResMngRight(strModeuleId,getCurUserInfo());

            int nRightType = IAuthorizeTypes.AU_USER_TYPE_VIEW;//
    		LoaderParam loaderParam = new LoaderParam(webParam.getResOwnerPK(), bHaveResMngRight, nRightType,false);

            boolean bIncludeFile = false;
            boolean bRef = false;
    	    treeModel = (WebTreeModel)treeLoader.loadTreeModel(bIncludeFile,bRef, loaderParam, null);
        } catch (ResOperException e) {
            AppDebug.debug(e);
            throw new WebException(e.getMessage());
        }
    	
    	//set treeModel's nodes
    	return treeModel;
	}
    
	public String getFormName() {
		return BusinessRefForm.class.getName();
	}
	private String getModuleID(){
		return IBIResMngConstants.MODULE_DIMENSION;
	}




}
 