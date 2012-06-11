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
import com.ufida.web.comp.tree.WebTree;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 类作用描述文字 syang 2006-01-17
 */
public class CalendarTreeDlg extends WebDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4047553059250972094L;

	/**
	 * @i18n mbidim00058=日历成员
	 */
	public final static String TITLE_CALENDAR = StringResource.getStringResource("mbidim00058");

	/**
	 * @i18n mbidim00059=成员移动
	 */
	public final static String TITLE_MOVE = StringResource.getStringResource("mbidim00059");

	private WebPanel dlgPane = null;

	private WebPanel fieldPane = null;

	private WebTree m_tree = null;

	private WebPanel btnPane = null;

	private WebButton btnSubmit = null;

	private WebCloseButton btnClose = null;
	private WebHiddenField m_hidden = null;

	/**
	 * 设置页面表单数据和处理组件多语言 每次页面刷新时调用 syang 2006-01-17
	 */
	protected void setData() throws WebException {
		btnSubmit.setValue(StringResource.getStringResource("miufopublic246")); // "确定"
		btnClose.setValue(StringResource.getStringResource("miufopublic247")); // "取消"

		// 关联ActionForm数据
		CalendarDesignForm form = (CalendarDesignForm) getActionForm(CalendarDesignForm.class.getName());
		if (form == null) {
			return;
		}
		m_tree.setModel(new WebTreeModel(form.getMemberVOs()));

		String isMoveMember = form.getIsMoveMember();
		if (isMoveMember != null && isMoveMember.equals(CalendarDesignAction.IS_MOVEMEMBER)) {
			setTitle(TITLE_MOVE);
		} else {
			setTitle(TITLE_CALENDAR);
		}
		m_hidden.setValue(isMoveMember);
		this.setWindowWidth(800);
		this.setWindowHeight(600);
	}

	/**
	 * 初始化页面组件 只在servlet实例化时调用一次. 此方法中不得涉及任何多语言处理 syang 2006-01-17
	 */
	protected void initUI() {
		setWindowWidth(800);
		setWindowHeight(600);

		setContentPane(getDlgPane());

		m_hidden = new WebHiddenField("isMoveMember");
		m_hidden.setID("isMoveMember");
		this.addHiddenField(m_hidden);
	}

	protected WebPanel getFieldPane() {
		if (fieldPane == null) {
			fieldPane = new WebPanel();
			fieldPane.setLayout(new WebGridLayout(2, 4));

			m_tree = new WebTree();
			fieldPane.add(m_tree, new Area(1, 1, 4, 1));
			m_tree.setWidth(800);
			m_tree.setHeight(600);
			fieldPane.setWidth(800);
			fieldPane.setHeight(600);
		}
		return fieldPane;
	}

	protected WebPanel getBtnPane() {
		if (btnPane == null) {
			btnPane = new WebPanel();
			btnPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
			btnSubmit = new WebButton();
			ActionForward fwd = new ActionForward(CalendarDesignAction.class.getName(),
					CalendarDesignAction.METHOD_CALENDAR_SUBMIT);
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
			dlgPane.add(getBtnPane());

		}
		return dlgPane;
	}
}
 