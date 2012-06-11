/**
 * Dialog1.java  5.0 
 * WebDeveloper自动生成.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebCloseButton;
import com.ufida.web.comp.WebHiddenField;
import com.ufida.web.comp.table.WebTable;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.BR;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 类作用描述文字 syang 2006-01-17
 */
public class ImportDesign2Dlg extends WebDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @i18n mbidim00006=字段对照
	 */
	public final static String TITLE = StringResource.getStringResource("mbidim00006");

	private WebPanel dlgPane = null;

	private WebPanel fieldPane = null;

	private WebTable m_table = null;

	private BR br1 = null;

	private WebPanel btnPane = null;

	private WebButton btnSubmit = null;

	private WebButton btnPrePage = null;

	private WebCloseButton btnClose = null;

	private WebHiddenField m_hidden = null;

	private WebHiddenField m_hidden_query = null;

	/**
	 * 设置页面表单数据和处理组件多语言 每次页面刷新时调用 syang 2006-01-17
	 */
	protected void setData() throws WebException {

		setTitle(TITLE);

		btnSubmit.setValue(StringResource.getStringResource("miufopublic246")); // "确定"
		btnClose.setValue(StringResource.getStringResource("miufopublic247")); // "取消"
		btnPrePage.setValue(StringResource.getStringResource("miufopublic260")); // "上一步"
		
		// 关联ActionForm数据
		ImportDataForm form = (ImportDataForm) getActionForm(ImportDataForm.class.getName());
		if (form == null) {
			return;
		}
		btnPrePage.getActionForward().addParameter("btnPrePageDimID", form.getDimID());
		btnPrePage.setProcessValidate(false);
		m_table.setModel(form.getTableModel());
		m_hidden.setValue(form.getDimID());
		m_hidden_query.setValue(form.getQueryID());
		
		this.addHiddenField(new WebHiddenField("spliterRuleType", form.getSpliterRuleType()));
		this.addHiddenField(new WebHiddenField("currentUIFlag", ImportDataAction.METHOD_IMPORT_DESIGN_NEXT));
	}

	/**
	 * 初始化页面组件 只在servlet实例化时调用一次. 此方法中不得涉及任何多语言处理 syang 2006-01-17
	 */
	protected void initUI() {
		setWindowWidth(0);
		setWindowHeight(0);

		setContentPane(getDlgPane());
		m_hidden = new WebHiddenField("dimID");
		m_hidden.setID("dimID");
		m_hidden_query = new WebHiddenField("queryID");
		m_hidden_query.setID("queryID");
		this.addHiddenField(m_hidden);
		this.addHiddenField(m_hidden_query);

	}

	protected WebPanel getFieldPane() {
		if (fieldPane == null) {
			fieldPane = new WebPanel();
			fieldPane.setLayout(new WebGridLayout(2, 2));
			Area area = null;
			area = new Area(1, 1, 1, 1);
			m_table = new WebTable();
			m_table.setID("tableModel");
			m_table.setName("tableModel");
			m_table.setWidth(400);
			m_table.setHeight(300);
			fieldPane.add(m_table, area);
		}
		return fieldPane;
	}

	protected BR getBr1() {
		if (br1 == null) {
			br1 = new BR();

		}
		return br1;
	}

	protected WebPanel getBtnPane() {
		if (btnPane == null) {
			btnPane = new WebPanel();
			btnPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
			btnPrePage = new WebButton();
			btnPrePage.setActionForward(new ActionForward(ImportDataAction.class.getName(),
					ImportDataAction.METHOD_IMPORT_DESIGN_BACK));
			
			btnPane.add(btnPrePage);

			btnSubmit = new WebButton();
			ActionForward fwd = new ActionForward(ImportDataAction.class.getName(),
					ImportDataAction.METHOD_IMPORT_DESIGN_SUBMIT);
			btnSubmit.setActionForward(fwd);
			btnPane.add(btnSubmit);

			btnClose = new WebCloseButton();
			btnPane.add(btnClose);

		}
		return btnPane;
	}

	protected WebPanel getDlgPane() {
		if (dlgPane == null) {
			dlgPane = new WebPanel();
			dlgPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
			dlgPane.add(getFieldPane());
			dlgPane.add(getBr1());
			dlgPane.add(getBtnPane());

		}
		return dlgPane;
	}

}
 