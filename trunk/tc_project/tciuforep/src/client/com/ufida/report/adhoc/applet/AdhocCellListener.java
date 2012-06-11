/*
 * Created on 2005-6-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.adhoc.applet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;

import com.ufida.report.adhoc.model.AdhocCrossProperty;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsEvent;
import com.ufsoft.table.CellsModelListener;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.format.ConditionFormat;
import com.ufsoft.table.format.Format;

/**
 * Adhoc报表的读写权限管理
 * 
 * @author caijie
 */
public class AdhocCellListener implements CellsModelListener, Serializable {

	static final long serialVersionUID = -7116280144592434721L;

	/***/
	private AdhocPlugin adhocPlugin;

	// private AdhocModel m_detailArea = null;
	/**
	 * @param report
	 */
	public AdhocCellListener(AdhocPlugin adhocPlugin) {
		super();
		this.adhocPlugin = adhocPlugin;
		// m_detailArea =
		// this.adhocPlugin.getModel().getAreaByType(AdhocArea.DETAIL_AREA_TYPE)[0];
	}

	public void cellsChanged(CellsEvent event) {
		if (!adhocPlugin.getCellsModel().isEnableEvent())
			return;

		AdhocCrossProperty propCache = adhocPlugin.getModel().getDataCenter().getCrossTableProperty();
		if (event.getMessage() == CellsEvent.VALUE_CHANGED || event.getMessage() == CellsEvent.FORMAT_CHANGED) {// 值改变
			// ||
			// 单元格属性改变

			if (adhocPlugin.isFormat() && !adhocPlugin.getModel().isFormatState()
					&& adhocPlugin.getModel().getDataCenter().isCross()) {// 交叉表样式

				if (adhocPlugin.getModel().isSelfCrossEvent())
					return;
				if (propCache == null)
					return;
				// 标记一下，避免引起事件的死循环
				adhocPlugin.getModel().setIsSelfCrossEvent(true);
				try{
				CellPosition pos = event.getArea().getStart();
				Cell selCell = adhocPlugin.getCellsModel().getCell(pos);
				// 修改界面
				CellPosition[] relaPos = propCache.getRelationPoses(pos);

				if (event.getMessage() == CellsEvent.VALUE_CHANGED){ // 记录用户设置的标题内容
					if(selCell!=null)
					propCache.setUserText(pos, selCell.getExtFmt(AdhocCrossProperty.KEY_CROSSHEADER_REALVALUE), selCell
							.getValue());
					if (relaPos != null) {
						for (int i = 0; i < relaPos.length; i++) {
							adhocPlugin.getCellsModel().setCellValue(relaPos[i], selCell.getValue());
						}
					}
				}
				else if ((event.getMessage() == CellsEvent.FORMAT_CHANGED)) {// 用户设置的单元格式（包括条件格式）
					// 记录用户设置的单元格式
					Format cellFormat = selCell.getFormat();
					ArrayList<ConditionFormat> condFormat = (ArrayList<ConditionFormat>)selCell.getExtFmt("ConditionFormat");
					if(condFormat != null){
						cellFormat.setCondition(true);
					}
					Object realValue = selCell.getExtFmt(AdhocCrossProperty.KEY_CROSSHEADER_REALVALUE);
					propCache.setUserFormat(pos, cellFormat, condFormat, (String)realValue);
					if (relaPos != null) {
						for (int i = 0; i < relaPos.length; i++) {
							adhocPlugin.getCellsModel().setCellFormat(relaPos[i].getRow(), relaPos[i].getColumn(),
									cellFormat);
							if(condFormat != null)
								adhocPlugin.getCellsModel().getCell(relaPos[i]).setExtFmt("ConditionFormat", condFormat);
							else
								adhocPlugin.getCellsModel().getCell(relaPos[i]).removeExtFmt("ConditionFormat");
						}
					}
				}}finally{
				adhocPlugin.getModel().setIsSelfCrossEvent(false);
				}

			}
		}
	}

	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		// TODO Auto-generated method stub
		return null;
	}

}
