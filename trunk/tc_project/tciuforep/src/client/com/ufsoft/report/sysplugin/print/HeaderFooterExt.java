package com.ufsoft.report.sysplugin.print;

import java.awt.Container;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.print.PrintSet;

public class HeaderFooterExt extends AbsActionExt {

	/**
	 * @i18n report00075=Ò³Ã¼Ò³½Å
	 */
	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setGroup(MultiLang.getString("printToolBar"));
        uiDes1.setName(MultiLang.getString("report00075"));
        uiDes1.setPaths(new String[]{MultiLang.getString("file")});
        return new ActionUIDes[]{uiDes1};
	}

	public UfoCommand getCommand() {
		return null;
	}

	public Object[] getParams(UfoReport container) {
		PrintSet printSet = container.getCellsModel().getPrintSet();
		HeaderFooterModel model = printSet.getHeaderFooterModel();
		HeaderFooterMngDlg dlg = new HeaderFooterMngDlg(container,model);
		dlg.setVisible(true);
		if(dlg.getResult()==UfoDialog.ID_OK){
			HeaderFooterModel newModel = dlg.getModel();
			printSet.setHeaderFooterModel(newModel);
		}
		return null;
	}

}
  