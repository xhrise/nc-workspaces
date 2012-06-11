/*
 * �������� 2006-4-10
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.component.ProcessController;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

public class SaveRepDataCmd extends AbsIufoBizCmd{
	private static boolean isSucceedSave = true;
	
    public SaveRepDataCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    /**
     * @i18n i18n miufopublic391=����ɹ�
     * @i18n i18n i18n miufopublic398=����ʧ��
     * @i18n i18n save=����
     */
    protected void executeIUFOBizCmd(final UfoReport ufoReport, Object[] params) {
    	ProcessController controller = new ProcessController(true);
		controller.setRunnable(new Runnable() {
			public void run() {
				try{
					save(ufoReport);
					//add by liuyy. ����δ֪�쳣��
				} catch(Throwable e){
					UfoPublic.sendMessage(e, ufoReport);
				}
			}
		});
		controller.setString(MultiLang.getString("save"));
		ufoReport.getStatusBar().setProcessDisplay(controller);
    }
	/**
	 * @i18n uiuforep00136=����ɹ������δͨ��
	 * @i18n uiuforep00137=����ɹ������ͨ��
	 * @i18n uiuforep00115=��˽��
	 */
	public static boolean save(UfoReport ufoReport) {
		IInputBizOper inputBizOper = new InputBizOper(ufoReport);
		isSucceedSave = true;
		String strReturn = (String)inputBizOper.performBizTask(ITableInputMenuType.MENU_TYPE_SAVE);
		int chIndex = strReturn.indexOf("@");
		String strOperReturn  = (chIndex > 0)?strReturn.substring(0, chIndex):strReturn;
		String strCheckReturn = (chIndex > 0)?strReturn.substring(chIndex+1):strReturn;
		if (strOperReturn != null && strOperReturn instanceof String && ((String) strOperReturn).equals("true")) {
			String messageStr = StringResource.getStringResource("miufopublic391");// i18n
			if(chIndex>0&&strCheckReturn != null){
				if(strCheckReturn.equals("false")){
				  messageStr = MultiLang.getString("uiuforep00136");
				  }else if(strCheckReturn.equals("true")){
					  messageStr = MultiLang.getString("uiuforep00137");  
				  }
				
				//@edit by liuyy at 2008-12-18 ����07:15:46 
//				ufoReport.getStatusBar().setHintMessage(messageStr);
				
				String strMenuName=MultiLang.getString("uiuforep00115");
				String[] strParentNames=new String[]{MultiLang.getString("window"),MultiLang.getString("panelManager")};
				FormulaTraceBizUtil.setMenuSelected(strMenuName, strParentNames, ufoReport);
			} 
			
			// @edit by wangyga at 2009-1-21,����03:06:44
			ufoReport.getStatusBar().setHintMessage(messageStr);
			
			// miufopublic391=����ɹ�
			//�ر�ҳ��ʱ��ʾ��ʾ�Ի���
		//	UfoPublic.sendMessage(messageStr, ufoReport);	
			
			
		} else if (strOperReturn != null && strOperReturn instanceof String && ((String) strOperReturn).equals("false")) {
			String errorStr = StringResource.getStringResource("miufopublic398");//
			UfoPublic.sendErrorMessage(errorStr, ufoReport, null);
			isSucceedSave = false;
		}
		
		return isSucceedSave;
	}

	public boolean isSucceedSave() {
		return isSucceedSave;
	}

}
 