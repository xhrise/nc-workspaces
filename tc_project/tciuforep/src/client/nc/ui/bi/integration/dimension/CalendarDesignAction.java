/**
 * Action1.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimensionException;
import nc.vo.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.action.MessageForward;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;

/**
 * 类作用描述文字 ll 2006-01-17
 */
public class CalendarDesignAction extends DialogAction {
	
	/**
	 * 空日期分割符：表示不用分割符号分割日期
	 */
	public static String CALENDAR_NULL_SPLITER = "NULL";
	public static String[] CALENDAR_GEN_RULES = {"yyyy-MM-dd","yy-MM-dd"};
	public static String[] CALENDAR_GEN_SPLITER = {"-","/",".",CALENDAR_NULL_SPLITER};
	
	static final String KEY_DIM_ID = "key_of_dimension";

	static final String KEY_IS_MOVEMEMBER = "key_is_move_member";

	static final String IS_MOVEMEMBER = "is_move_member";

	private static final String SESSION_KEY_CURR_DIMVO = "current_dimension_vo";

	private static final String SESSION_KEY_CALENDARMEMBER_VOS = "current_CalendarMembers";

	public static final String METHOD_CALENDAR_DESIGN = "execute";

	public static final String METHOD_CALENDAR_NEXT = "showMemebers";

	public static final String METHOD_CALENDAR_SUBMIT = "submit";
	
	public static final String METHOD_APPEND_CALENDAR = "appendCalendar";

	public static final String METHOD_SUBMIT_APPEND = "submitAppend";

	/**
	 * <MethodDescription> ll 2006-01-17
	 */
	public ActionForward execute(ActionForm actionForm) throws WebException {
		String dimID = getRequestParameter(KEY_DIM_ID);

		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
		addSessionObject(SESSION_KEY_CURR_DIMVO, dimVO);
		CalendarDesignForm form = (CalendarDesignForm)actionForm;
		form.setCalendarGenRuleItems(getCalendarGenRules());
		ActionForward actionForward = new ActionForward(CalendarDesignDlg.class.getName());
		return actionForward;
	}
	
	private String[][] getCalendarGenRules(){
		String[][] items = new String[CALENDAR_GEN_RULES.length][2];
		for(int i = 0; i < items.length; i++){
			items[i][0] = CALENDAR_GEN_RULES[i];
			items[i][1] = CALENDAR_GEN_RULES[i];
		}
		return items;
	}
	public ActionForward showMemebers(ActionForm actionForm) throws WebException {
		boolean bOk = onOk((CalendarDesignForm) actionForm);
		try {
			if (bOk) {
				DimMemberVO[] vos = (DimMemberVO[]) getSessionObject(SESSION_KEY_CALENDARMEMBER_VOS);
				CalendarDesignForm form = (CalendarDesignForm) actionForm;
				form.setMemberVOs(vos);
				form.setIsMoveMember("showMembers");

				ActionForward fwd = new ActionForward(CalendarTreeDlg.class.getName());
				return fwd;
			}
		} catch (Exception ex) {
			AppDebug.debug(ex);
			throw new DimensionException();
		}
		ActionForward actionForward = new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
		return actionForward;
	}

	/**
	 * <MethodDescription> ll 2006-01-17
	 */
	public ActionForward submit(ActionForm actionForm) throws WebException {
		CalendarDesignForm form = (CalendarDesignForm) actionForm;
		String isMoveMember = form.getIsMoveMember();
		if (isMoveMember != null && isMoveMember.equals(CalendarDesignAction.IS_MOVEMEMBER)) {
			return moveMember_submit(form);
		}

		try {
			DimMemberSrv memberSrv = new DimMemberSrv((DimensionVO) getSessionObject(SESSION_KEY_CURR_DIMVO));

			DimMemberVO[] vos = (DimMemberVO[]) getSessionObject(SESSION_KEY_CALENDARMEMBER_VOS);
			vos = (DimMemberVO[]) memberSrv.create(vos);

		} catch (Exception ex) {
			AppDebug.debug(ex);
			throw new DimensionException();
		}
		removeSessionObject(SESSION_KEY_CALENDARMEMBER_VOS);
		removeSessionObject(SESSION_KEY_CURR_DIMVO);

		return new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
	}
	public ActionForward appendCalendar(ActionForm actionForm) throws WebException {		
		String dimID = getRequestParameter(KEY_DIM_ID);

		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
		addSessionObject(SESSION_KEY_CURR_DIMVO, dimVO);
		CalendarDesignForm form = (CalendarDesignForm)actionForm;
		form.setCalendarGenRuleItems(getCalendarGenRules());
		ActionForward actionForward = new ActionForward(CalendarAppendDlg.class.getName());
		return actionForward;
	}
	/**
	 * 时间维度追加期间的提交
	 * @param actionForm
	 * @return
	 * @throws WebException
	 */
	public ActionForward submitAppend(ActionForm actionForm) throws WebException {//TODO
		CalendarDesignForm form = (CalendarDesignForm) actionForm;
		String[] sRules = form.getRules().split(WebGlobalValue.FLAG_SPLIT);
		String sStart = form.getStartDate();
		String sEnd = form.getEndDate();	
		boolean isReplace = form.getIsReplace();
		CalendarCreator creator = new CalendarCreator(sStart, sEnd, sRules,
				form.getCalendarGenRule(),
				form.getCalendarGenSpliter(),
				(DimensionVO) getSessionObject(SESSION_KEY_CURR_DIMVO));
		try {
			DimMemberSrv memberSrv = new DimMemberSrv((DimensionVO) getSessionObject(SESSION_KEY_CURR_DIMVO));
			
			DimMemberVO[] vos = creator.createCalendar();
			vos = (DimMemberVO[]) memberSrv.createAppend(vos, isReplace);

		} catch (Exception ex) {
			AppDebug.debug(ex);
			throw new DimensionException();
		}
		removeSessionObject(SESSION_KEY_CURR_DIMVO);

		return new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
	}

	private boolean onOk(CalendarDesignForm form) {

		String[] sRules = form.getRules().split(WebGlobalValue.FLAG_SPLIT);
		String sStart = form.getStartDate();
		String sEnd = form.getEndDate();		
		CalendarCreator creator = new CalendarCreator(sStart, sEnd, sRules,
				form.getCalendarGenRule(),
				form.getCalendarGenSpliter(),
				(DimensionVO) getSessionObject(SESSION_KEY_CURR_DIMVO));
		try {
			DimMemberVO[] vos = creator.createCalendar();
			// 设置传入下一个界面的信息
			addSessionObject(SESSION_KEY_CALENDARMEMBER_VOS, vos);
		} catch (Exception e) {
			AppDebug.debug(e);
			if (e instanceof DimensionException)
				throw new DimensionException((DimensionException) e);
			else
				throw new DimensionException();
		}

		return true;
	}

	/**
	 * Form值校验
	 * 
	 * @param actionForm
	 * @return 值校验失败的提示信息集合
	 * @i18n mbidim00001=请先从层次列表中选择层次添加到产生规则列表
	 * @i18n mbidim00002=起始日期错误
	 * @i18n mbidim00003=终止日期错误
	 * @i18n mbidim00004=起始日期范围应该小于终止日期
	 */
	@SuppressWarnings("unchecked")
	public String[] validate(ActionForm actionForm) {
		CalendarDesignForm form = (CalendarDesignForm) actionForm;

		if (form.getIsMoveMember() != null) {
			if (form.getIsMoveMember().equals(IS_MOVEMEMBER)) {
				/** 不能移动到自身及下级 */
				return null;
			}
			//成员浏览，不需要校验
			return null;
		}
		
		
		
		try {
			String[] sRules = getListValues("rules");
			if (sRules == null || sRules.length == 0)
				return new String[] { StringResource.getStringResource("mbidim00001") };	

			ArrayList list = new ArrayList();
			for(int i = 0; i < sRules.length; i++)list.add(sRules[i]);
			
				
			GregorianCalendar startDate = null;			
			try {
				String year = form.getStartDate().substring(0, 4);
				String month = form.getStartDate().substring(5, 7);
				String day = form.getStartDate().substring(8, 10);
				startDate = new GregorianCalendar(Integer.parseInt(year),
						Integer.parseInt(month),
						Integer.parseInt(day));
			} catch (Exception e1) {	
				AppDebug.debug(e1);
				return new String[]{StringResource.getStringResource("mbidim00002")};
			}
			GregorianCalendar endDate = null;			
			try {
				String year = form.getEndDate().substring(0, 4);
				String month = form.getEndDate().substring(5, 7);
				String day = form.getEndDate().substring(8, 10);
				endDate = new GregorianCalendar(Integer.parseInt(year),
						Integer.parseInt(month),
						Integer.parseInt(day));
			} catch (Exception e1) {	
				AppDebug.debug(e1);
				return new String[]{StringResource.getStringResource("mbidim00003")};
			}				
			if(startDate.after(endDate)) return new String[] { StringResource.getStringResource("mbidim00004") };			
					
			String checkRule = CalendarCreator.validateCalendarRule(list);
			if(checkRule != null){
				return new String[]{checkRule};
			}
			return null;
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new DimensionException(DimensionException.ERR_INPUT_ERROR);
		}

	}

	public ActionForward moveMember(ActionForm actionForm) throws WebException {
		try{
			String dimID = getRequestParameter(KEY_DIM_ID);
			DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
			DimMemberSrv srv = new DimMemberSrv(dimVO);
			DimMemberVO[] memVOs = srv.getAll();
	
			CalendarDesignForm form = (CalendarDesignForm) actionForm;
			form.setIsMoveMember(IS_MOVEMEMBER);
			form.setMemberVOs(memVOs);
	
			ActionForward actionForward = new ActionForward(CalendarTreeDlg.class.getName());
			return actionForward;
		}catch(Exception e){
			return new ErrorForward(e.getMessage());
		}
	}

	/**
	 * <MethodDescription> ll 2006-01-17
	 * @i18n mbidim00005=成员移动功能尚未实现
	 */
	public ActionForward moveMember_submit(ActionForm actionForm) throws WebException {
		try {

			/** TODO */
		} catch (Exception ex) {
			AppDebug.debug(ex);
			throw new DimensionException();
		}

		return new MessageForward(StringResource.getStringResource("mbidim00005"));
		// return new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
	}

	/**
	 * 关联Form
	 * 
	 */
	public String getFormName() {
		return CalendarDesignForm.class.getName();
	}
}
 