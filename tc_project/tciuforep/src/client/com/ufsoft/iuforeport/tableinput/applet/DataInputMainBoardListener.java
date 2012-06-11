package com.ufsoft.iuforeport.tableinput.applet;

import com.ufida.dataset.IContext;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.event.MainboardListener;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
/**
 * V56打开指定模块的透视图
 * @author wangyga
 *
 */
public class DataInputMainBoardListener implements MainboardListener,IUfoContextKey{

	public void shutdown(Mainboard mainboard) {		
	}

	public void startup(Mainboard mainboard) {
		if(mainboard == null){
			return;
		}
		IContext context = mainboard.getContext();
		String strPerspectiveId = context.getAttribute(PERSPECTIVE_ID)+"";
		if(strPerspectiveId == null || strPerspectiveId.trim().length() == 0){
			return;
		}
		mainboard.getPerspectiveManager().setCurrentPerspective(strPerspectiveId);		
		mainboard.getPerspectiveManager().setDefaultPersistenceKey(strPerspectiveId);
	}

}
