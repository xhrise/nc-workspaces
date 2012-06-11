/**
 * Action1.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;
import nc.ui.iufo.web.reference.base.BusinessRefForm;
import nc.ui.iufo.web.reference.base.TreeRefDlg;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufsoft.iufo.web.DialogAction;

/**
 * �������������� ll 2006-01-17
 */
public class MemberTreeRefAction extends DialogAction {

	static final String KEY_DIM_ID = "key_of_dimension";
	public static final String METHOD_GETTREEREF = "getMemberTreeRef";

    public ActionForward getMemberTreeRef(ActionForm actionForm) throws WebException {
        ActionForward actionForward = null;

		String dimID = getRequestParameter(KEY_DIM_ID);
    	WebTreeModel memberTreeModel = MemberMngAction.getMemberTreeModel(dimID, true);
		BusinessRefForm form = (BusinessRefForm)actionForm;
		form.setTreeModel(memberTreeModel);
        actionForward = new ActionForward(TreeRefDlg.class.getName());
        return actionForward;        
    }
	/**
	 * ����Form
	 * 
	 */
	public String getFormName() {
		return BusinessRefForm.class.getName();
	}
}
 