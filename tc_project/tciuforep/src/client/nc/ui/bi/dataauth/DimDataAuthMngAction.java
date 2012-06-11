/**
 * DimDataAuthMngAction.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-04-17
 */
package nc.ui.bi.dataauth;


import nc.ui.bi.integration.dimension.DimUIToolKit;
import nc.us.bi.dataauth.DataAuthSrv;
import nc.vo.bi.dataauth.DataAuthVO;
import nc.vo.pub.BusinessException;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.tree.IWebTreeModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.MultiFrameAction;


/**
 * 维度数据权限管理Action
 * zyjun
 * 2006-04-17
 */
public class DimDataAuthMngAction extends MultiFrameAction {

    /**
     * 只有管理员和维度的创建者才可以进行数据权限的管理
     * <MethodDescription>
     * zyjun
     * 2006-04-17
     */
    public ActionForward execute(ActionForm actionForm) {
    	ActionForward	fwd = DimUIToolKit.checkDimRefer(this);
    	if( fwd == null ){
	    	if( DataAuthToolKit.isAuthorizedDimUser(this) ){
		    	DataAuthMngForm	form = (DataAuthMngForm)actionForm;
		    	form.setDimPK(DataAuthToolKit.getSelectedDimPK(this));
		        return  new ActionForward(DimDataAuthMngUI.class.getName());    
	    	}else{
	    		return new ErrorForward(StringResource.getStringResource("mbiauth0007"));//只有管理员和维度的创建者才能进行数据权限管理"));
	    	}
    	}
    	return fwd;
    }

    /**
     * 树模型加载，当前维度的所有成员
     * zyjun
     * 2006-04-17
     */	    
    public IWebTreeModel getTreeModel(){
    	String		strDimPK = DataAuthToolKit.getSelectedDimPK(this);
    	return DataAuthToolKit.getDimMemberTreeModel(strDimPK);
    }
    
    /**
     * 加载表格模型，
     * zyjun
     * 2006-04-17
     */	    
    public IWebTableModel 	getTableModel(){
    	try{
	    	String				strDimMemberPK = getTreeSelectedID();
	    	String				strDimPK = DataAuthToolKit.getSelectedDimPK(this);
	    	DataAuthVO[]		dataAuthVOs = DataAuthSrv.getInstance().getDimAuthesByMember(strDimPK, strDimMemberPK);
	    	
	    	return DataAuthToolKit.getDataAuthTableModel(dataAuthVOs);
	    	
    	}catch(BusinessException  e){
    		throw new WebException(e.getMessage());
    	}
    }
	
	public ActionForward  delete(ActionForm actionForm) {
		try{
			String[]	strIDs = getTableSelectedIDs();
			DataAuthSrv.getInstance().removeDataAuthes(strIDs);
			return new CloseForward(CloseForward.CLOSE_REFRESH_MAIN);
		}catch(BusinessException e){
			return new ErrorForward(e.getMessage());
		}
		
	}
			
    /**
     * 获取Action对应的form名称 
     * zyjun
     * 2006-04-17
     */
	public String getFormName() {
		return DataAuthMngForm.class.getName();
	}
	

	
  }
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <MultiActionVO name="DimDataAuthMngAction" package="nc.ui.bi.dataauth">
      <MethodsVO execute="">
      </MethodsVO>
    </MultiActionVO>
@WebDeveloper*/