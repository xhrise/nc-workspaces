package com.ufida.report.adhoc.applet;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufida.report.adhoc.model.AdhocArea;
import com.ufida.report.adhoc.model.AdhocFunctionField;
import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.IArea;

/** 计算项
 *	继承计算列的扩展，以解决快捷键相同（都是=）的问题 
 */
public class FunctionFieldExt extends CalcColumnExt {

	public FunctionFieldExt(AdhocPlugin plugin) {
		super(plugin);
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(StringResource.getStringResource("mbiadhoc00011"));
		uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0));
		uiDes1.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00005") });
		ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
		uiDes2.setPaths(new String[] {});
		uiDes2.setPopup(true);
		return new ActionUIDes[] { uiDes1, uiDes2 };
	}

	public Object[] getParams(UfoReport container) {
		AdhocFunctionField fld = null;
		String formula = null;
		CellPosition pos = getEditPos(false);
		if(pos == null){
			return super.doCalcColumnDef();//不满足计算项的设置条件时，尝试进行计算列设置
		}
		IArea adArea = m_adhocPlugin.getModel().getFormatArea(pos, false, false);
		if(adArea == null)
			return null;

		CellPosition formatPos = adArea.getStart();
		Object obj = m_adhocPlugin.getCellsModel().getBsFormat(formatPos, AdhocPublic.EXT_FMT_FUNCTION_FIELD);
		if ((obj != null) && (obj instanceof AdhocFunctionField)) {
			fld = (AdhocFunctionField) obj;
			formula = fld.getFormula();
		}

		AdhocFormulaDlg dlg = new AdhocFormulaDlg(m_adhocPlugin.getReport(), m_adhocPlugin.getModel(),
				formula);
		dlg.setLocationRelativeTo(m_adhocPlugin.getReport());
		dlg.show();
		if (dlg.getResult() == UfoDialog.ID_OK) {
			boolean bNeedQuery = false;
			String newFormula = dlg.getDbFormula();
			if (newFormula == null) {
				fld = null;
			} else {
				if (fld == null) {
					fld = new AdhocFunctionField();
				}
				fld.setFormula(newFormula);
				bNeedQuery = true;
			}
			m_adhocPlugin.getModel().setFunctionField(formatPos, fld);
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