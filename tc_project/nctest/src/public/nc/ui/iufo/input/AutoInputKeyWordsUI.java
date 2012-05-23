package nc.ui.iufo.input;

import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ¼��ؼ��ֽ���UI
 * @author weixl,Created on 2006-2-24
 */
public class AutoInputKeyWordsUI extends AbstractInputKeywordsUI {
	private static final long serialVersionUID = 4973348966600448497L;

	public String getUITitle() {
		return StringResource.getStringResource("miufo1002869");  //"¼�룺ѡ��ؼ���"
	}

	public ActionForward getCancelButtonForward(CSomeParam param) {
		return new CloseForward(CloseForward.CLOSE);
	}

	public ActionForward getSubmitButtonForward(CSomeParam param) {
		return new ActionForward(AutoInputKeywordsAction.class.getName(),null);
	}

	protected void clear() {
		super.clear();
	}	
}
