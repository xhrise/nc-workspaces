package nc.ui.iufo.input;

import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.vo.iufo.data.MeasurePubDataVO;

/**
 * 录入关键字界面
 * @author weixl,Created on 2006-2-24
 */
public class AutoInputKeywordsAction extends AutoAbstractInputKeywordsAction {

	protected UfoTableUtil getTableUtil() {
		return new UfoTableUtil(this);
	}

	protected String getHintMess() {
		return "";
	}

	protected boolean isCheckTimeInTask() {
		return true;
	}
	
	protected boolean isInOpenWindow() {
		return false;
	}

	protected void doCheckAloneID(CSomeParam somePar, MeasurePubDataVO pubData) throws Exception {
        String strTaskPK = somePar.getTaskId();
        String strAloneID = doGetAloneID(pubData, strTaskPK);
        if (strAloneID!=null)
            somePar.setAloneId(strAloneID);
	}

    /**
     * 得到AloneID
     * @param pubData
     * @param strTaskPK
     * @return
     * @throws Exception
     */
    public static String doGetAloneID(MeasurePubDataVO pubData, String strTaskPK) throws Exception {
        String str = MeasurePubDataBO_Client.getAloneID(pubData);
    	return str;
    }

	protected String getInputKeywordsUIClass() {
		return AutoInputKeyWordsUI.class.getName();
	}

	protected String getInputActionClass() {
		return InputNewAction.class.getName();
	}
}
