package com.ufsoft.iufo.fmtplugin.key;

import java.awt.Component;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaDefPlugIn;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class DynAreaKeyMngExt extends AbsActionExt {    

    private UfoReport report;
    private AbsEditorAction newAction=null;//�µļ���action
    
    public DynAreaKeyMngExt(UfoReport report) {
        super();
        this.report = report;
        this.newAction=new DynAreaKeyMngEditorAction(report);
    }
    /**
	 * @i18n uiiufofmt00053=��̬���ؼ�������
	 */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(StringResource.getStringResource("uiiufofmt00053"));
        uiDes.setImageFile("reportcore/key_word.gif");
        uiDes.setPopup(true);
        uiDes.setShowDialog(true);
        uiDes.setGroup(DynAreaDefPlugIn.MENU_GROUP);
        return new ActionUIDes[]{uiDes};
    }
    public boolean isEnabled(Component focusComp) {
        CellsModel cellsModel = report.getCellsModel();
        if(cellsModel == null){
        	return true;
        }
        CellPosition anchorPos = cellsModel.getSelectModel().getAnchorCell();
        DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
        if(dynAreaModel.isInDynArea(anchorPos)){
            return true;
        }else{
            return false;
        }
    }
    public UfoCommand getCommand() {
        return new UfoCommand(){
			@Override
			public void execute(Object[] params) {
				newAction.execute(params);
			}};
    }

    /**
	 * @i18n uiiufofmt00054=��Ԫ���Ѿ���ָ��,��������Ϊ�ؼ���
	 * @i18n uiiufofmt00055=��Ԫ���Ѿ��б���,��������Ϊ�ؼ���
	 */
    public Object[] getParams(UfoReport container) {
    	return newAction.getParams();
    }
}
 