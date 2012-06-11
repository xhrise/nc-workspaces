package com.ufida.report.adhoc.applet;

import java.awt.Component;

import com.ufida.report.adhoc.calc.AdhocFormFieldSrv;
import com.ufida.report.adhoc.model.AdhocArea;
import com.ufida.report.adhoc.model.AdhocCalcColumn;
import com.ufida.report.adhoc.model.AdhocException;
import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufida.report.adhoc.model.AdhocQueryModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.IArea;

/**
 * 计算列
 */
public class CalcColumnExt extends AbsActionExt {
	AdhocPlugin m_adhocPlugin = null;

	public CalcColumnExt(AdhocPlugin plugin) {
		m_adhocPlugin = plugin;
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(StringResource.getStringResource("mbiadhoc00012"));
//		uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0));
		uiDes1.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00005") });
		ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
		uiDes2.setPaths(new String[] {});
		uiDes2.setPopup(true);
		return new ActionUIDes[] { uiDes1, uiDes2 };
	}

	public UfoCommand getCommand() {
		return null;
	}

	public boolean isEnabled(Component focusComp) {
		return m_adhocPlugin.isFormat();			
	}
	/*
	 * 获取编辑位置（包含了此位置可定义计算列的校验）
	 */
	protected CellPosition getEditPos(boolean canEditDetail){
		CellPosition[] selectedCells = m_adhocPlugin.getCellsModel().getSelectModel().getSelectedCells();
		if (selectedCells == null || selectedCells.length == 0 || selectedCells[0] == null)
			return null;
		CellPosition selPos = selectedCells[0];
		AdhocArea area = m_adhocPlugin.getModel().getAreaByRow(selPos.getRow());
		if(area != null && (area.getAreaType()==AdhocArea.DETAIL_AREA_TYPE)== canEditDetail)
			return selPos;
		else
			return null;
	}
	public Object[] getParams(UfoReport container) {
		return doCalcColumnDef();
	}
	protected Object[] doCalcColumnDef(){
		AdhocCalcColumn fld = null;
		String formula = null;
		String name = null;
		CellPosition pos = getEditPos(true);
		if(pos == null)
			return null;
		
		AdhocQueryModel query = m_adhocPlugin.getModel().getDataCenter().getCurrQuery();
		IArea formatArea = m_adhocPlugin.getModel().getFormatArea(pos, true, false);
		CellPosition fmtPos = formatArea.getStart();
		Object obj = m_adhocPlugin.getCellsModel().getBsFormat(fmtPos, AdhocPublic.EXT_FMT_CALCULATE_COLUMN);
		if ((obj != null) && (obj instanceof AdhocCalcColumn)) {
			fld = (AdhocCalcColumn) obj;
			AdhocFormFieldSrv srv = new AdhocFormFieldSrv(query);
			formula = srv.getUserDefFormula(fld.getFormula());
			name = fld.getName();
		}


		DefFormulaFieldDlg dlg = new DefFormulaFieldDlg(m_adhocPlugin.getReport(),query, name, formula);
		dlg.setLocationRelativeTo(m_adhocPlugin.getReport());
		dlg.show();
		if (dlg.getResult() == UfoDialog.ID_OK) {
			String newFormula = dlg.getDbFormula();
			String dbColumn = dlg.getDbColumn();
			boolean bNeedQuery = false;
			if (newFormula == null) {// 公式为空等同删除
				m_adhocPlugin.getModel().removeCalcColumn(fmtPos);
			} else if (fld != null) {
				fld.setFormula(newFormula, dbColumn);
				fld.setName(dlg.getName());
				fld.setColType(dlg.getFormulaType());
				m_adhocPlugin.getModel().upDateCalcColumn(fld);
				bNeedQuery = true;
			} else {
				fld = new AdhocCalcColumn();
				fld.setFormula(newFormula, dbColumn);
				fld.setName(dlg.getName());
				fld.setColType(dlg.getFormulaType());
				try {
					m_adhocPlugin.getModel().addCalcColumn(fld, fmtPos);
					bNeedQuery = true;
				} catch (AdhocException e) {
					m_adhocPlugin.showErrorMessage(e.getMessage());
					return null;
				}
			}
			boolean bOldFormat = m_adhocPlugin.getModel().isFormatState();
			if(!bOldFormat)
			{
				m_adhocPlugin.getModel().changeState(true, false);
				m_adhocPlugin.getModel().changeState(bOldFormat, bNeedQuery);
			}
		}
		return null;
	}
}