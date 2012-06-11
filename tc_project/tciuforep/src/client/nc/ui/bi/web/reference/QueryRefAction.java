/*
 * 创建日期 2006-7-11
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.web.reference;

import java.util.ArrayList;

import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.uitemplate.IWebTreeLoader;
import nc.ui.iufo.resmng.uitemplate.TreeTableOperFactory;
import nc.ui.iufo.resmng.uitemplate.describer.TableHeaderInfo;
import nc.ui.iufo.web.reference.base.BusinessRefForm;
import nc.ui.iufo.web.reference.base.TreeTableRef;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.resmng.ResMngBizHelper;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.util.iufo.resmng.loader.ITableLoader;
import nc.util.iufo.resmng.loader.LoaderParam;
import nc.util.iufo.resmng.loader.TableLoaderParam;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResOperException;

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
 * @author zyjun
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class QueryRefAction extends MultiFrameAction {
    /**
	 * @i18n mbiweb00003=查询参照
	 */
    public ActionForward execute(ActionForm actionForm) throws WebException {
        ActionForward actionForward = null;     
       
        BusinessRefForm form = (BusinessRefForm)actionForm;
        form.setRefTitle(StringResource.getStringResource(StringResource.getStringResource("mbiweb00003")));  //"查询参照"       
        actionForward = new ActionForward(TreeTableRef.class.getName());
        return actionForward;
    	
    }
    
    /**
     * 
     */
	public Object[] getTableColumns() {
   		String[] strHearderNames =  new String[] {
                StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_QUERYCODE),
                StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_QUERYNAME),
                StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_QUERYTYPE),
                StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_DSNAME),
                StringResource.getStringResource(TableHeaderInfo.RESOURCE_ID_OF_QUERYNOTE)};
   		return strHearderNames;
	}

	/**
	 * 
	 */
	public IWebTableModel getTableModel() {    	
    	WebTableModel tableModel = new WebTableModel();
    	String strTreeObjId = getTreeSelectedID();
    	try{
        	tableModel.setRefDisplayColumn(2);
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
    					ArrayList<String[]> vRowData=new ArrayList<String[]>();
    		            for(int i=0; i<fileResTreeObjs.length; i++){
    		            	QueryModelVO 	queryVO = (QueryModelVO)fileResTreeObjs[i].getSrcVO();        	
    		            	String[] 		oneRowData=new String[6];
 
    		            	oneRowData[0] = queryVO.getID();
    		            	oneRowData[1] = queryVO.getQuerycode();
    		            	oneRowData[2] = queryVO.getQueryname();
    		    			String			strType = BIQueryConst.TYPENAMES[Integer.parseInt(queryVO.getType())-1];
    		    			oneRowData[3] = StringResource.getStringResource(strType);
    		            	oneRowData[4] = queryVO.getDscode();
    		            	oneRowData[5] = queryVO.getNote();
    		            	
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

	/**
	 * 
	 */
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
		return IBIResMngConstants.MODULE_QUERY;
	}

}
 