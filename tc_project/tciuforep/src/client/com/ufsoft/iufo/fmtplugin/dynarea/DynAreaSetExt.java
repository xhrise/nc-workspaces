package com.ufsoft.iufo.fmtplugin.dynarea;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

/**
 * 设置为动态区
 * @author chxw
 */
public class DynAreaSetExt extends AbsDynAreaExt{
	public DynAreaSetExt(DynAreaDefPlugIn dynAreaPlug){
		super(dynAreaPlug);
	}
	
	private boolean dynAreaSetImpl(){
		CellsModel model = getDynAreaPlugIn().getCellsModel();
		IArea selAnchorCell = model.getSelectModel().getSelectedArea();
		return addDynAreaImpl(null, selAnchorCell);
	}
	
	@Override
	public void excuteImpl(UfoReport report){
		if(dynAreaSetImpl()){
			dynAreaMngImpl(report);	
		}
	}
	
	@Override
	public String getDesName() {
		return StringResource.getStringResource("miufo1004036");
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getDesName());
        uiDes.setImageFile("reportcore/dyn_area.gif");
        uiDes.setPaths(new String[]{MultiLang.getString("format")});
        uiDes.setGroup(DynAreaDefPlugIn.MENU_GROUP);
        uiDes.setShowDialog(true);
        ActionUIDes uiDes2 = (ActionUIDes)uiDes.clone();
        uiDes2.setPopup(true);
        uiDes2.setPaths(new String[]{});
        return new ActionUIDes[]{uiDes, uiDes2};
	}

	
}
