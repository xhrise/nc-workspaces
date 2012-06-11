package com.ufsoft.iufo.fmtplugin.key;

import java.awt.Component;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**关键字设置*/
	public class KeywordSetExt extends AbsActionExt implements IUfoContextKey{//implements IMainMenuExt{

		private UfoReport _report;

        /**
         * @param report
         */
        KeywordSetExt(UfoReport report) {
            _report = report;
        }

        /* (non-Javadoc)
		 * @see com.ufsoft.report.menu.ICommandExt#getName()
		 */
		public String getName() {			
			return  StringResource.getStringResource("miufo1000265");
		}

		/* (non-Javadoc)
		 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
		 */
		public UfoCommand getCommand() {			
//			return new KeywordSetCmd(_report);
			return null;
		}
		

		/* (non-Javadoc)
		 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			AbsEditorAction editorAction = new KeyWordSetEditorAction(container);
			editorAction.execute(editorAction.getParams());
		    return null;
		}      

        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes = new ActionUIDes();
            uiDes.setName(getName());
            uiDes.setPaths(new String[]{MultiLang.getString("data")});
            uiDes.setImageFile("reportcore/key_word.gif");
            uiDes.setGroup("measureMngExt");
            uiDes.setShowDialog(true);
            return new ActionUIDes[]{uiDes};
        }

		public boolean isEnabled(Component focusComp) {
			// @edit by wangyga at 2009-6-4,下午03:24:58 没必要如此判断
//			if(!StateUtil.isCellsPane(_report,focusComp)){
//				return false;
//			}
			return true;
		}
		
	}