package com.ufsoft.iufo.view;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.dataset.IContext;
import com.ufida.zior.docking.core.DockingConstants;
import com.ufida.zior.docking.core.state.DockingState;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.event.MainboardListener;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;

/**
 * 初始化格式态的一些数据的监听器：比如打开默认的报表
 * 
 * @author wangyga
 * 
 */
public class ReportDesignMainboardListener implements MainboardListener,
		IUfoContextKey {

	public void shutdown(Mainboard mainboard) {
		// TODO Auto-generated method stub
	}

	public void startup(Mainboard mainboard) {
		if (mainboard == null) {
			return;
		}
		IContext context = mainboard.getContext();
		String strRepPk = context.getAttribute(REPORT_PK) + "";
		if (strRepPk.trim().length() == 0) {
			return;
		}
		String strPerspectiveId = context.getAttribute(PERSPECTIVE_ID) + "";
		if (strPerspectiveId.trim().length() == 0) {
			return;
		}

		DockingState info = null;
		if (strPerspectiveId.equalsIgnoreCase(PERS_FORMAT_DESIGNER)) {
			String id = "format_" + strRepPk;
			info = new DockingState(id);
			info.setRegion(DockingConstants.EAST_REGION);
			info.setSplitRatio((float) 0.8);
			info.setClzName(ReportFormatDesigner.class.getName());
		} else if (strPerspectiveId.equalsIgnoreCase(PERS_DATA_INPUT)) {
			String id = "iufo.input.data.view";
			info = new DockingState(id);
			info.setRegion(DockingConstants.EAST_REGION);
			info.setSplitRatio((float) 0.8);
			info.setClzName(RepDataEditor.class.getName());
			info.setRelativeParentId("iufo.input.dir_unit.view");
		} else {
			throw new IllegalArgumentException("strPerspectiveId is wrong");
		}

		mainboard.getPerspectiveManager().getCurrentPerspective().getSequence()
				.add(info);
	}

}
