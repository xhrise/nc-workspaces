package nc.ui.bi.web.reference;

import nc.ui.bi.integration.dimension.MemberMngAction;
import nc.ui.iufo.web.reference.base.BusinessRefForm;
import nc.ui.iufo.web.reference.base.TreeTableRef;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.table.WebTableModel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * Î¬¶È×Ö¶Î²ÎÕÕ
 * @author caijie
 *
 */
public class DimFieldRefAction  extends MemberMngAction {
	/**
	 * @i18n mbiweb00001=Î¬¶È×Ö¶Î²ÎÕÕ
	 * 
	 */
    public ActionForward execute(ActionForm actionForm) throws WebException {
        ActionForward actionForward = null;     
       
        BusinessRefForm form = (BusinessRefForm)actionForm;
        form.setRefTitle(StringResource.getStringResource("mbiweb00001"));  //"Î¬¶È×Ö¶Î²ÎÕÕ"       
        actionForward = new ActionForward(TreeTableRef.class.getName());
        return actionForward;
    	
    }
	protected String getTreeDimensionID(){
		return getRequestParameter(BIRefAction.DIM_ID); 
	}
	
	/**
	 * 
	 */
	public IWebTableModel getTableModel() {    	 	
		IWebTableModel tableModel = super.getTableModel();  
		tableModel.setSelectMode(IWebTableModel.SINGLE_SELECTION);
		((WebTableModel)tableModel).setRefDisplayColumn(2);
    	return tableModel;    
	}

	
	public String getFormName() {
		return BusinessRefForm.class.getName();
	}
	


}
 