/**
 * DimDataAuthMngAction.java  5.0 
 * 
 * WebDeveloper�Զ�����.
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
 * ά������Ȩ�޹���Action
 * zyjun
 * 2006-04-17
 */
public class DimDataAuthMngAction extends MultiFrameAction {

    /**
     * ֻ�й���Ա��ά�ȵĴ����߲ſ��Խ�������Ȩ�޵Ĺ���
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
	    		return new ErrorForward(StringResource.getStringResource("mbiauth0007"));//ֻ�й���Ա��ά�ȵĴ����߲��ܽ�������Ȩ�޹���"));
	    	}
    	}
    	return fwd;
    }

    /**
     * ��ģ�ͼ��أ���ǰά�ȵ����г�Ա
     * zyjun
     * 2006-04-17
     */	    
    public IWebTreeModel getTreeModel(){
    	String		strDimPK = DataAuthToolKit.getSelectedDimPK(this);
    	return DataAuthToolKit.getDimMemberTreeModel(strDimPK);
    }
    
    /**
     * ���ر��ģ�ͣ�
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
     * ��ȡAction��Ӧ��form���� 
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