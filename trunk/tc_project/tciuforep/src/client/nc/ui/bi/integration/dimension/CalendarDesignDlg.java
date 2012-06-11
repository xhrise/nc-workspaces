/**
 * Dialog1.java  5.0 
 * WebDeveloper自动生成.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import nc.vo.bi.integration.dimension.DimRescource;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebChoice;
import com.ufida.web.comp.WebDateRef;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebList2List;
import com.ufida.web.comp.WebList2ListModel;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 类作用描述文字 syang 2006-01-17
 */
public class CalendarDesignDlg extends WebDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @i18n mbidim00060=时间维度设计
	 */
	public final static String TITLE = StringResource.getStringResource("mbidim00060");

	/**
	 * @i18n mbidim00061=层次列表
	 */
	public static String LIST_SRC_NOTE = StringResource.getStringResource("mbidim00061");

	/**
	 * @i18n mbidim00062=产生规则
	 */
	public static String LIST_DEST_NOTE = StringResource.getStringResource("mbidim00062");

	/**
	 * @i18n mbidim00063=时间范围
	 */
	private final static String LBL_RANGE = StringResource.getStringResource("mbidim00063");

	private WebPanel dlgPane = null;

	private WebPanel fieldPane = null;

	private WebList2List list2list_Rules = null;

	private WebLabel lbl_timeRange = null;

	private WebChoice calendarGenRuleChoice = null;
	private WebChoice calendarGenSpliterChoice = null;
	private WebDateRef ref_start = null;

	private WebDateRef ref_end = null;

	private WebPanel btnPane = null;

	private WebButton btnSubmit = null;

	private WebButton btnClose = null;

	/**
	 * 设置页面表单数据和处理组件多语言 每次页面刷新时调用 syang 2006-01-17
	 */
	protected void setData() throws WebException {
		setTitle(TITLE);

		list2list_Rules.getModel().setSrcTitle(LIST_SRC_NOTE);
		list2list_Rules.getModel().setDestTitle(LIST_DEST_NOTE);

		lbl_timeRange.setValue(LBL_RANGE);
		
		btnSubmit.setValue(StringResource.getStringResource("miufopublic246")); // "确定"
		btnClose.setValue(StringResource.getStringResource("miufopublic247")); // "取消"

		// 关联ActionForm数据
		CalendarDesignForm form = (CalendarDesignForm) getActionForm(CalendarDesignForm.class.getName());
		if (form == null) {
			return;
		}
		String[][] rule_items = new String[][] { new String[] { DimRescource.TIME_YEAR, DimRescource.TIME_YEAR },
				new String[] { DimRescource.TIME_HALFYEAR, DimRescource.TIME_HALFYEAR },
				new String[] { DimRescource.TIME_QUARTER, DimRescource.TIME_QUARTER },
				new String[] { DimRescource.TIME_MONTH, DimRescource.TIME_MONTH },
				new String[] { DimRescource.TIME_TENDAY, DimRescource.TIME_TENDAY },
				new String[] { DimRescource.TIME_WEEK, DimRescource.TIME_WEEK },
				new String[] { DimRescource.TIME_DAY, DimRescource.TIME_DAY } };

		getCalendarGenRuleChoice().setItems(form.getCalendarGenRuleItems());
		getCalendarGenSpliterChoice().setItems(getCalendarGenSpliterItems());		
		list2list_Rules.getModel().setSrcItems(rule_items);
	}

	/**
	 * 初始化页面组件 只在servlet实例化时调用一次. 此方法中不得涉及任何多语言处理 syang 2006-01-17
	 */
	protected void initUI() {
		setWindowWidth(0);
		setWindowHeight(0);

		setContentPane(getDlgPane());

	}

	private WebChoice getCalendarGenRuleChoice(){
		if(calendarGenRuleChoice == null){
			calendarGenRuleChoice = new WebChoice();	
			calendarGenRuleChoice.setID("calendarGenRule");
			calendarGenRuleChoice.setName("calendarGenRule");
		}
		return calendarGenRuleChoice;
	}
	private WebChoice getCalendarGenSpliterChoice(){
		if(calendarGenSpliterChoice == null){
			calendarGenSpliterChoice = new WebChoice();	
			calendarGenSpliterChoice.setID("calendarGenSpliter");
			calendarGenSpliterChoice.setName("calendarGenSpliter");
			
		}
		return calendarGenSpliterChoice;
	}
	
	private String[][] getCalendarGenSpliterItems(){
		String[][] items = new String[CalendarDesignAction.CALENDAR_GEN_SPLITER.length][2];
		for(int i = 0; i < items.length; i++){
			items[i][0] = CalendarDesignAction.CALENDAR_GEN_SPLITER[i];
			items[i][1] = CalendarDesignAction.CALENDAR_GEN_SPLITER[i];
		}
		return items;
	}
	/**
	 * @i18n mbidim00064=日期格式
	 * @i18n mbidim00065=分割符
	 */
	protected WebPanel getFieldPane() {
		if (fieldPane == null) {
			fieldPane = new WebPanel();
			fieldPane.setLayout(new WebGridLayout(3, 4));

			list2list_Rules = new WebList2List();
			list2list_Rules.setID("rules");
			list2list_Rules.setName("rules");
			list2list_Rules.setModel(new WebList2ListModel());
			fieldPane.add(list2list_Rules, new Area(1, 1, 4, 1));

			
			fieldPane.add(new WebLabel(StringResource.getStringResource("mbidim00064")), new Area(2, 1, 1, 1));
			fieldPane.add(getCalendarGenRuleChoice(), new Area(2, 2, 1, 1));
			
			fieldPane.add(new WebLabel(StringResource.getStringResource("mbidim00065")), new Area(2, 3, 1, 1));
			fieldPane.add(getCalendarGenSpliterChoice(), new Area(2,4, 1, 1));
			
			lbl_timeRange = new WebLabel();
			fieldPane.add(lbl_timeRange, new Area(3, 1, 1, 1));

			ref_start = new WebDateRef();
			ref_start.setID("startDate");
			ref_start.setName("startDate");			
			fieldPane.add(ref_start, new Area(3, 2, 1, 1));

			fieldPane.add(new WebLabel("-"), new Area(3, 3, 1, 1));

			ref_end = new WebDateRef();
			ref_end.setID("endDate");
			ref_end.setName("endDate");
			fieldPane.add(ref_end, new Area(3, 4, 1, 1));

		}
		return fieldPane;
	}

	protected WebPanel getBtnPane() {
		if (btnPane == null) {
			btnPane = new WebPanel();
			btnPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
			btnSubmit = new WebButton();
			ActionForward fwd = new ActionForward(CalendarDesignAction.class.getName(), CalendarDesignAction.METHOD_CALENDAR_NEXT);
			btnSubmit.setActionForward(fwd);

			btnPane.add(btnSubmit);

			btnClose = new WebButton();
			btnClose.setActionForward(new CloseForward(CloseForward.CLOSE_REFRESH_MAIN));
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
 