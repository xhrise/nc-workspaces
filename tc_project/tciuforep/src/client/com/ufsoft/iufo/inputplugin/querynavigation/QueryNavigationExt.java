package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.report.util.MultiLang;

public class QueryNavigationExt extends AbsActionExt  {

	
	private QueryNaviMenu menu = null;
	
	
	protected QueryNavigationExt(UfoReport report) {
		super(); 
	}

	public QueryNavigationExt(QueryNaviMenu menu) {
		super(); 
		this.menu = menu;
		 
	}

	@Override
	public UfoCommand getCommand() {
		return new QueryNavigationCmd();
	}

	@Override
	public Object[] getParams(UfoReport container) { 
		 
		return new Object[]{menu};
		
	}


	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		
		uiDes.setPaths(new String[]{MultiLang.getString("window")});//综合查询窗口
		
//		uiDes.setDirectory(false);
		uiDes.setGroup("queryNavigation");
//		uiDes.setCheckBox(false);
//		uiDes.setCheckBoxMenuItem(false);
//		uiDes.set
		
		if(QueryNavigation.getSingleton().isCurWindow(menu)){
//			uiDes.setSelected(true);
			uiDes.setImageFile("reportcore/next.gif");
		}
		String label = menu.toString();
		uiDes.setName(label);
		uiDes.setTooltip(label);
		
		return new ActionUIDes[] { uiDes };
	}
//	
//	public ActionUIDes[] getUIDesArr() {
//        ActionUIDes uiDes = new ActionUIDes();
//        uiDes.setName(MultiLang.getString("窗口"));
//        uiDes.setPaths(new String[]{MultiLang.getString("edit"),});
//         
//        ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
//        uiDes2.setPaths(new String[]{});
//        uiDes2.setPopup(true);
//        return new ActionUIDes[]{uiDes,uiDes2}; 
//	}

}
