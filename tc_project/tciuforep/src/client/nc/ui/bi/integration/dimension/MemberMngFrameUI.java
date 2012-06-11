/**
 * LogFrameUI.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-11
 */
package nc.ui.bi.integration.dimension;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.menu.WebMenuItem;
import com.ufida.web.comp.menu.WebMenubar;
import com.ufida.web.comp.menu.WebToolBar;
import com.ufida.web.window.WebMultiFrame;
import com.ufsoft.iufo.resource.StringResource;

/**
 * �������������� ll 2006-01-11
 */
public class MemberMngFrameUI extends WebMultiFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ����
	/**
	 * @i18n ubdim0016=��Աά��
	 */
	public static final String TITLE = StringResource.getStringResource("ubdim0016");

	// �˵���
	/**
	 * @i18n miufopublic242=�½�
	 */
	public static final String MENU_CREATE = StringResource.getStringResource("miufopublic242");

	/**
	 * @i18n miufo1001396=�޸�
	 */
	public static final String MENU_MODIFY = StringResource.getStringResource("miufo1001396");

	/**
	 * @i18n ubiquery0110=ɾ��
	 */
	public static final String MENU_REMOVE = StringResource.getStringResource("ubiquery0110");

	/**
	 * @i18n miufo1003341=�ƶ�
	 */
	public static final String MENU_MOVE = StringResource.getStringResource("miufo1003341");

	/**
	 * @i18n miufo1000223=��Ȩ
	 */
	public static final String MENU_AUTH = StringResource.getStringResource("miufo1000223");
	/**
	 * @i18n miufo1001644=׷��
	 */
	public static final String MENU_APPEND = StringResource.getStringResource("miufo1001644");

	private WebMenubar menubar2 = null;

	private WebMenuItem menu_create = null;

	private WebMenuItem menu_modify = null;

	private WebMenuItem menu_remove = null;

	private WebMenuItem menu_move = null;

	private WebMenuItem menu_append = null;

	//private WebMenuItem menu_auth = null;

	private WebToolBar toolBar1 = null;

	/**
	 * ҳ�����
	 */
	public String getTitle() {
		return TITLE;

	}

	/**
	 * ���ý�����Ϣ����������� ÿ��ҳ��ˢ��ʱ����
	 */
	protected void setData() throws WebException {

		menu_create.setMenuLabel(MENU_CREATE);
		menu_modify.setMenuLabel(MENU_MODIFY);
		menu_remove.setMenuLabel(MENU_REMOVE);		
		menu_move.setMenuLabel(MENU_MOVE);
		menu_append.setMenuLabel(MENU_APPEND);
	

		super.setData();
		
		MemberMngForm form = (MemberMngForm)getActionForm(MemberMngForm.class.getName());
		if( form != null ){
			menu_create.setEnable(form.canBeModified());
			menu_modify.setEnable(form.canBeModified());
			menu_remove.setEnable(form.canBeModified());
			menu_move.setEnable(form.canBeModified());
			menu_append.setEnable(form.canBeModified() && form.isCalendar());
			
			if( form.canBeModified() ){
				String dimID = form.getDimID();
				menu_create.getActionForward().addParameter(MemberDesignAction.KEY_DIM_ID, dimID);
				menu_modify.getActionForward().addParameter(MemberDesignAction.KEY_DIM_ID, dimID);
				menu_remove.getActionForward().addParameter(MemberDesignAction.KEY_DIM_ID, dimID);
				menu_move.getActionForward().addParameter(MemberDesignAction.KEY_DIM_ID, dimID);
				menu_append.getActionForward().addParameter(MemberDesignAction.KEY_DIM_ID, dimID);
			}
		}
		
	}

	/**
	 * ��ʼ���˵� ֻ��servletʵ����ʱ����һ��. �˷����в����漰�����Դ���
	 */
	protected void initMenuBar() {
	//	this.disableTable();
		this.setMenubar(getMenubar2());
		
	}

	/**
	 * ��ʼ�������� ֻ��servletʵ����ʱ����һ��. �˷����в����漰�����Դ���
	 */
	protected void initToolBar() {
		this.setToolBar(getToolBar1());
	}

	protected WebMenubar getMenubar2() {
		if (menubar2 == null) {
			menubar2 = new WebMenubar();
			menu_create = new WebMenuItem();
			menu_create.setSubmitType(WebMenuItem.TREE_SUBMIT);
			ActionForward fwd = new ActionForward(MemberDesignAction.class.getName(), MemberDesignAction.METHOD_EXCUTE);
			menu_create.setActionForward(fwd);
			menubar2.add(menu_create);

			menu_modify = new WebMenuItem();
			menu_modify.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			fwd = new ActionForward(MemberDesignAction.class.getName(), MemberDesignAction.METHOD_UPDATE);
			fwd.addParameter(MemberDesignAction.KEY_EDIT_TYPE, MemberDesignAction.EDITTYPE_MODIFY);
			menu_modify.setActionForward(fwd);
			menubar2.add(menu_modify);

			menu_remove = new WebMenuItem();
			menu_remove.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			menu_remove.setSubmitDel(true);
			fwd = new ActionForward(MemberMngAction.class.getName(), MemberMngAction.METHOD_REMOVE);
			menu_remove.setActionForward(fwd);
			menubar2.add(menu_remove);

			menu_move = new WebMenuItem();
			menu_move.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			fwd = new ActionForward(MemberMoveAction.class.getName(), MemberMoveAction.METHOD_MOVE_MEMBER);
			menu_move.setActionForward(fwd);
			menubar2.add(menu_move);

			menu_append = new WebMenuItem();
			fwd = new ActionForward(CalendarDesignAction.class.getName(), CalendarDesignAction.METHOD_APPEND_CALENDAR);
			menu_append.setActionForward(fwd);
			menubar2.add(menu_append);

//			menu_auth = new WebMenuItem();
//			fwd = null;// TODO
//			menu_auth.setActionForward(fwd);
//			menubar2.add(menu_auth);

		}
		return menubar2;
	}

	protected WebToolBar getToolBar1() {
		if (toolBar1 == null) {
			toolBar1 = new WebToolBar();

		}
		return toolBar1;
	}

}
 