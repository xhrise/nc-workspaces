/**
 * Dialog1.java  5.0 
 * WebDeveloper�Զ�����.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.comp.WebTextRef;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * �������������� syang 2006-01-17
 */
public class MemberMoveDlg extends WebDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7548046469208879620L;

	/**
	 * @i18n mbidim00007=��Ա��Ϣά��
	 */
	public final static String TITLE = StringResource.getStringResource("mbidim00007");

	private WebPanel dlgPane = null;
	private WebPanel fieldPane = null;

    private WebLabel lblDstObjID = null;
    private WebTextRef refDstObjID = null;

	private WebPanel btnPane = null;
	private WebButton btnSubmit = null;
	private WebButton btnClose = null;

	/**
	 * ����ҳ������ݺʹ������������ ÿ��ҳ��ˢ��ʱ���� syang 2006-01-17
	 * @i18n miufopublic248=��һ��
	 */
	protected void setData() throws WebException {
		setTitle(TITLE);
        lblDstObjID.setValue(StringResource.getStringResource("miufo1003346")); //"Ŀ��λ��"
		getSubmitBtn().setValue(StringResource.getStringResource("miufopublic246")); // "ȷ��"
		getCloseBtn().setValue(StringResource.getStringResource("miufopublic247")); // "ȡ��"

        refDstObjID.setReadonly(true);
        ((WebTextField)refDstObjID.getRefFld()).setVld_NoNull(true);
        ((WebTextField)refDstObjID.getRefFld()).setVld_label_id("lblDstObjID");//StringResource.getStringResource("miufo1003346"));//Ŀ��λ��

//		 ����ActionForm����
		MemberMoveForm form = getMemberMoveForm();
		if (form == null) {
			return;
		}
        ActionForward dstRefFwd = new ActionForward(MemberTreeRefAction.class.getName(), MemberTreeRefAction.METHOD_GETTREEREF);
        dstRefFwd.addParameter(MemberMoveAction.KEY_DIM_ID, form.getSelectedTreeID());
        refDstObjID.setActionForward(dstRefFwd);

		
		ActionForward fwd = new ActionForward(MemberMoveAction.class.getName(), MemberMoveAction.METHOD_MOVE_MEMBER_SUBMIT);
		getSubmitBtn().setActionForward(fwd);
		
		getCloseBtn().setActionForward(new CloseForward(CloseForward.CLOSE_REFRESH_PARENT_All));
		
	}
	/**
     * ������������Form����
     * @return
     */
    private MemberMoveForm getMemberMoveForm() {
    	MemberMoveForm memberMoveForm = (MemberMoveForm) getActionForm(MemberMoveForm.class.getName());
        return memberMoveForm;
    }
	/**
	 * ��ʼ��ҳ����� ֻ��servletʵ����ʱ����һ��. �˷����в����漰�κζ����Դ��� syang 2006-01-17
	 */
	protected void initUI() {
		setWindowWidth(0);
		setWindowHeight(0);

		setContentPane(getDlgPane());
		
	}

	protected WebPanel getFieldPane() {
		if (fieldPane == null) {
			fieldPane = new WebPanel();
			fieldPane.setLayout(new WebGridLayout(2, 2));

			lblDstObjID = new WebLabel();
			fieldPane.add(lblDstObjID, new Area(1, 1, 1, 1));
			refDstObjID = new WebTextRef();
			refDstObjID.setName(MemberMoveForm.ID_memRef);
			refDstObjID.setID(MemberMoveForm.ID_memRef);
			fieldPane.add(refDstObjID, new Area(1, 2, 1, 1));

		}
		return fieldPane;
	}
	
	protected WebButton getSubmitBtn(){
		if(btnSubmit == null){
			btnSubmit = new WebButton();		
		}
		return btnSubmit;
	}
	
	protected WebButton getCloseBtn(){
		if(btnClose == null){
			btnClose = new WebButton();		
		}
		return btnClose;
	}
	
	protected WebPanel getBtnPane() {
		if (btnPane == null) {
			btnPane = new WebPanel();
			btnPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
				
			btnPane.add(getSubmitBtn());	
			btnPane.add(getCloseBtn());

		}
		return btnPane;
	}

	
	protected WebPanel getDlgPane() {
		if (dlgPane == null) {
			dlgPane = new WebPanel();
			dlgPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.VERTICAL));
			dlgPane.add(getFieldPane());
			dlgPane.add(getBtnPane());

		}
		return dlgPane;
	}
}
 