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
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebRadioGroup;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.BR;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 类作用描述文字 syang 2006-01-17
 */
public class ImportDataDlg extends WebDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 按钮
	/**
	 * @i18n miufo1002870=数据导入
	 */
	public final static String TITLE = StringResource.getStringResource("miufo1002870");

	/**
	 * @i18n miufo1001644=追加
	 */
	private final static String RB_TXT_APPEND = StringResource.getStringResource("miufo1001644");

	/**
	 * @i18n mbidim00066=替换
	 */
	private final static String RB_TXT_REPLACE = StringResource.getStringResource("mbidim00066");

	private final static String RADIOBTN_ID = "importType";

	private WebPanel dlgPane = null;

	private WebPanel fieldPane = null;

	private WebRadioGroup radio_importtype = null;
	private WebRadioGroup radio_conFlictdealType = null;
	

	private BR br1 = null;

	private WebPanel btnPane = null;

	private WebButton btnSubmit = null;

	private WebCloseButton btnClose = null;

	private WebHiddenField m_hidden = null;

	/**
	 * 设置页面表单数据和处理组件多语言 每次页面刷新时调用 syang 2006-01-17
	 * @i18n mbidim00067=冲突时停止导入
	 * @i18n mbidim00068=冲突时只取第一个成员
	 */
	protected void setData() throws WebException {

		setTitle(TITLE);
		btnSubmit.setValue(StringResource.getStringResource("miufopublic246")); // "确定"
		btnClose.setValue(StringResource.getStringResource("miufopublic247")); // "取消"

		radio_conFlictdealType.setItems(new String[][] { 
				new String[] { String.valueOf(ImportDataAction.CONPFLICT_DEAL_TYPE_STOP), StringResource.getStringResource("mbidim00067") },
				new String[] { String.valueOf(ImportDataAction.CONPFLICT_DEAL_TYPE_FIRST_RECORD), StringResource.getStringResource("mbidim00068") } });
		radio_conFlictdealType.setValue(String.valueOf(ImportDataAction.CONPFLICT_DEAL_TYPE_STOP));
		
		radio_importtype.setItems(new String[][] { new String[] { ImportDataForm.RB_VALUE_APPEND, RB_TXT_APPEND },
				new String[] { ImportDataForm.RB_VALUE_REPLACE, RB_TXT_REPLACE } });
		radio_importtype.setValue(ImportDataForm.RB_VALUE_APPEND);
		
		// 关联ActionForm数据
		ImportDataForm form = (ImportDataForm) getActionForm(ImportDataForm.class.getName());
		if (form == null) {
			return;
		}

		m_hidden.setValue(form.getDimID());
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
		this.addHiddenField(m_hidden);

	}

	/**
	 * @i18n mbidim00069=导入方式
	 * @i18n mbidim00070=编码或名称重复
	 */
	protected WebPanel getFieldPane() {
		if (fieldPane == null) {
			fieldPane = new WebPanel();
			fieldPane.setLayout(new WebGridLayout(4, 2));
			
			fieldPane.add(new WebLabel(StringResource.getStringResource("mbidim00069")), new Area(1, 1, 1, 1));
			//fieldPane.add(new WebLabel(""), new Area(2, 1, 1, 1));
			radio_importtype = new WebRadioGroup(RADIOBTN_ID);
			radio_importtype.setID(RADIOBTN_ID);
			fieldPane.add(radio_importtype, new Area(1, 2, 1, 1));
			
			fieldPane.add(new WebLabel(StringResource.getStringResource("mbidim00070")), new Area(3, 1, 1, 1));
			//fieldPane.add(new WebLabel(""), new Area(4, 1, 1, 1));
			radio_conFlictdealType = new WebRadioGroup("conflictDealType");
			radio_conFlictdealType.setID("conflictDealType");
			fieldPane.add(radio_conFlictdealType, new Area(3, 2, 1, 1));

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

			btnSubmit = new WebButton();
			ActionForward fwd = new ActionForward(ImportDataAction.class.getName(),
					ImportDataAction.METHOD_IMPORT_DATE_SUBMIT);
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
			dlgPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.VERTICAL));
			dlgPane.add(getFieldPane());
			dlgPane.add(getBr1());
			dlgPane.add(getBtnPane());

		}
		return dlgPane;
	}

}
 