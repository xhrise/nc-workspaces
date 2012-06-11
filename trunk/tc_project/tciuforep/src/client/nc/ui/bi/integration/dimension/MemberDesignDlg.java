/**
 * Dialog1.java  5.0 
 * WebDeveloper�Զ�����.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import nc.ui.iufo.exproperty.ExPropDataEditHelper;
import nc.ui.iufo.exproperty.IExPropDataEditForm;
import nc.vo.bi.integration.dimension.DimRescource;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebChoice;
import com.ufida.web.comp.WebHiddenField;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * �������������� syang 2006-01-17
 */
public class MemberDesignDlg extends WebDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @i18n mbidim00007=��Ա��Ϣά��
	 */
	public final static String TITLE = StringResource.getStringResource("mbidim00007");

	// ��Ա��Ϣ����ǩ
	/**
	 * @i18n miufo1003134=����
	 */
	private final static String LBL_MEMNAME = StringResource.getStringResource("miufo1003134");

	/**
	 * @i18n miufo1001012=����
	 */
	private final static String LBL_MEMCODE = StringResource.getStringResource("miufo1001012");

	/**
	 * @i18n mbidim00008=�������
	 */
	private final static String LBL_RULE = StringResource.getStringResource("mbidim00008");

	// private final static String LBL_OTHER = "����";
	//
	// private final static String LBL_ORG = "��֯";

	private WebPanel dlgPane = null;

	private WebPanel fieldPane = null;

	private WebLabel[] fixLables = null;// �̶����Եı�ǩ

	private WebTextField m_tfCode = null;

	private WebTextField m_tfName = null;

	private WebChoice m_cmbRule = null; // ��������ı���

	private WebPanel btnPane = null;

	private WebButton btnSubmit = null;
	private WebButton btnNext = null;
	private WebButton btnClose = null;

	/**
	 * ����ҳ������ݺʹ������������ ÿ��ҳ��ˢ��ʱ���� syang 2006-01-17
	 * @i18n miufopublic248=��һ��
	 */
	protected void setData() throws WebException {
		setTitle(TITLE);
		fixLables[0].setValue(LBL_MEMCODE);
		fixLables[1].setValue(LBL_MEMNAME);
		fixLables[2].setValue(LBL_RULE);

		String[][] rule_items = new String[][] {
				new String[] { String.valueOf(DimRescource.INT_CACLRULE_NULL), StringResource.getStringResource(DimRescource.CACLERULE_NULL_ID) },
				new String[] { String.valueOf(DimRescource.INT_CACLRULE_ADD), DimRescource.CACLERULE_ADD },
				new String[] { String.valueOf(DimRescource.INT_CACLRULE_SUB), DimRescource.CACLERULE_SUB } };

		m_cmbRule.setItems(rule_items);
		getSubmitBtn().setValue(StringResource.getStringResource("miufopublic246")); // "ȷ��"
		getNextBtn().setValue(StringResource.getStringResource("miufopublic248"));
		getCloseBtn().setValue(StringResource.getStringResource("miufopublic247")); // "ȡ��"
		
//		 ����ActionForm����
		MemberDesignForm form = getMemberDesignForm();
		if (form == null) {
			return;
		}
		
		ActionForward fwd = new ActionForward(MemberDesignAction.class.getName(), MemberDesignAction.METHOD_NEXT);
		getNextBtn().setActionForward(fwd);
//		getNextBtn().setOnClick("btn_action('" + fwd.toString() + "', true);setTreeSelectedID('" + form.getSelectedTreeID() + "',opener.window.document);");
		//.setActionForward(fwd);		
		
		
		fwd = new ActionForward(MemberDesignAction.class.getName(), MemberDesignAction.METHOD_SUBMIT);
		getSubmitBtn().setActionForward(fwd);
		//getSubmitBtn().setOnClick("btn_action('" + fwd.toString() + "', true);setTreeSelectedID('" + form.getSelectedTreeID() + "',opener.window.document);");
		
		getCloseBtn().setActionForward(new CloseForward(CloseForward.CLOSE_REFRESH_PARENT_All));
		//getCloseBtn().setOnClick("close_refresh_parent_all(); setTreeSelectedID('" + form.getSelectedTreeID() + "',opener.window.document);");

		
		if(MemberDesignAction.EDITTYPE_MODIFY.equalsIgnoreCase(form.getUIType())){
			getNextBtn().setDisabled(true);
		}else{
			getNextBtn().setDisabled(false);
		}
		m_tfCode.setValue(form.getMemcode());
		m_tfName.setValue(form.getMemname());
		m_cmbRule.setValue(form.getCalattr());

		/** TODO �Զ������Եĸ�ֵ */
		addSelfPropInput(this.getFieldPane(), 4);		
		this.addHiddenField(new WebHiddenField("UIType", form.getUIType()));
		this.addHiddenField(new WebHiddenField("selectedTreeID", form.getSelectedTreeID()));
//		setWindowWidth(300);
//		setWindowHeight(300);
//		this.enableAutoResize();
	}
	/**
	 * �����Ӷ�������¼��Ŀؼ�
	 * @param oFieldPane
	 * @param nRowLine
	 */
	protected void addSelfPropInput(WebPanel oFieldPane,int nRowLine) {
	    ExPropDataEditHelper.addExPropsToPanel(oFieldPane,nRowLine,getExPropDataEditForm());
	}
	
	/**
     * ������������Form����
     * @return
     */
    private MemberDesignForm getMemberDesignForm() {
    	MemberDesignForm memberDesignForm = (MemberDesignForm) getActionForm(MemberDesignForm.class.getName());
        return memberDesignForm;
    }
    private IExPropDataEditForm getExPropDataEditForm(){
        IExPropDataEditForm exPropDataEditForm = (IExPropDataEditForm)getMemberDesignForm();
        return exPropDataEditForm;
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
			fieldPane.setLayout(new WebGridLayout(14, 2));

			fixLables = new WebLabel[3];
			for (int i = 0; i < fixLables.length; i++) {
				fixLables[i] = new WebLabel();
				fieldPane.add(fixLables[i], new Area((i + 1), 1, 1, 1));
			}

			m_tfCode = new WebTextField();
			m_tfCode.setVld_NoNull(true);
			m_tfCode.setID("memcode");
			m_tfCode.setName("memcode");
			m_tfCode.setMaxlength(32);
			fieldPane.add(m_tfCode, new Area(1, 2, 1, 1));

			m_tfName = new WebTextField();
			m_tfName.setVld_NoNull(true);
			m_tfName.setID("memname");
			m_tfName.setName("memname");
			m_tfName.setMaxlength(64);
			fieldPane.add(m_tfName, new Area(2, 2, 1, 1));

			m_cmbRule = new WebChoice();
			m_cmbRule.setID("calattr");
			m_cmbRule.setName("calattr");
			fieldPane.add(m_cmbRule, new Area(3, 2, 1, 1));

		}
		return fieldPane;
	}

	protected WebButton getNextBtn(){
		if(btnNext == null){
			btnNext = new WebButton();		
		}
		return btnNext;
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
			btnPane.add(getNextBtn());	
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
 