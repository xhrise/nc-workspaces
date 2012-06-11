package com.ufida.report.anareport.applet;

import java.util.ArrayList;

import nc.ui.bi.query.manager.RptProvider;
import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.model.AnaCellCombine;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

public class AnaReportFieldAction {

	public static void removeFlds(AnaReportModel model, CellPosition[] cells, CellPosition aimPos) {
		if (cells == null || cells.length == 0)
			return;
		CellsModel cellsModel=model.getFormatModel();
		ArrayList<ExAreaCell> al_areas = new ArrayList<ExAreaCell>();
		CombinedCell aimCombineCell = cellsModel.getCombinedAreaModel().belongToCombinedCell(aimPos);
		for (CellPosition pos : cells) {
			CombinedCell combineCell = cellsModel.getCombinedAreaModel().belongToCombinedCell(pos);
			if(aimCombineCell != null && combineCell == aimCombineCell)
				continue;//modify by wangyga 2008-10-6 删除字段时,如果选择区域和目标区域处于同一个合并单元里，则不删除						
			Cell cell = cellsModel.getCell(pos);
			if (cell == null)
				continue;
			AnaRepField fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
			if (fld != null) {
				ExAreaCell exCell = ExAreaModel.getInstance(cellsModel).getExArea(
						AreaPosition.getInstance(pos, pos));
				if (exCell != null
						&& exCell.getModel() instanceof AreaDataModel) {
					al_areas.add(exCell);

					AreaDataModel dModel = (AreaDataModel) exCell.getModel();
					if (dModel.getCrossSet() != null&&dModel.getCrossSet().getCrossArea().contain(pos)) {
						dModel.getCrossSet().setDirty(true);
					}

				}
				cell.removeExtFmt(AnaRepField.EXKEY_FIELDINFO);
				cell.removeExtFmt(AnaCellCombine.KEY);
				cellsModel.setCellValue(pos, null);
				cellsModel.setCellProperty(pos, PropertyType.DataType,TableConstant.CELLTYPE_SAMPLE);
			}
		}
		model.validateAreaModel(al_areas);
	}


    /**
     * 向目标位置添加字段,统计对交叉区域的AnaRepField修改初始化的信息
     * @param model
     * @param aimPos
     * @param fld
     */
	public static void addFlds(CellsModel model, CellPosition aimPos,
			AnaRepField fld) {
		AreaDataModel areaData = getAreaDataModel(model, aimPos);
		if (areaData == null)
			return;
		AnaDataSetTool dsTool=areaData.getDSTool();
		if(dsTool==null)
			return ;

		if (areaData.getCrossSet() != null
				&& areaData.getCrossSet().getCrossArea().contain(aimPos)) {
			AnaCrossTableSet cross = areaData.getCrossSet();
			cross.setDirty(true);
			if (cross.getDsTool() == null) {
				cross.setDsTool(dsTool);
			}
			int crossPosType = cross.getCrossType(aimPos);
			fld.setFieldType(crossPosType);

			if (crossPosType > AnaRepField.Type_CROSS_LEFTTOP) {
				if (crossPosType == AnaRepField.Type_CROSS_MEASURE) {
					
					model.setCellProperty(aimPos, PropertyType.DataType,
								TableConstant.CELLTYPE_NUMBER);
					
					if (!(fld.getField() instanceof FieldCountDef)) {
						model.setCellValue(aimPos, fld.getFieldLabel());
						
						if (DataTypeConstant.isNumberType(fld.getField()
								.getDataType())&&fld.getField().getExtType()!=RptProvider.DIMENSION) {
							fld.setCountFieldDef(new FieldCountDef(fld
									.getField(), IFldCountType.TYPE_SUM, null,
									null, null));
						} else {
							fld.setCountFieldDef(new FieldCountDef(fld
									.getField(), IFldCountType.TYPE_COUNT,
									null, null, null));
						}
						
					} else {
						FieldCountDef countDef = (FieldCountDef) fld.getField();
						if (countDef.getRangeFld() != null) {
							fld.setFieldType(AnaRepField.Type_CROSS_SUBTOTAL);
						}
						AnaRepField showFld = new AnaRepField(fld.getField(),
								crossPosType, fld.getDSPK());
						model.setCellValue(aimPos, showFld.getFieldLabel());
					}

				} else {
					if (fld.getField() instanceof FieldCountDef) {
						FieldCountDef fcd = (FieldCountDef) fld.getField();
						fld = new AnaRepField(dsTool.getField(fcd
								.getMainFldName()), crossPosType, fld.getDSPK());
					}
					
					model.setCellProperty(aimPos, PropertyType.DataType,
								TableConstant.CELLTYPE_SAMPLE);
					
					model.setCellValue(aimPos, fld.getFieldLabel());
				}
				model.getCell(aimPos).addExtFmt(AnaRepField.EXKEY_FIELDINFO,
						fld);
			}

		} else {
			if(fld.getFieldType()>AnaRepField.Type_CROSS_LEFTTOP){//从交叉区域移到交叉区域外
				if(fld.getField() instanceof FieldCountDef){
					FieldCountDef fcd=(FieldCountDef)fld.getField();
					fld= new AnaRepField(dsTool.getField(fcd.getMainFldName()), AnaRepField.TYPE_DETAIL_FIELD, fld.getDSPK());
				}else{
				  fld.setFieldType(AnaRepField.TYPE_DETAIL_FIELD);
				}
			}
//			if (PropertyType.getPropertyByType(model
//					.getCellFormat(aimPos), PropertyType.DataType) == TableConstant.UNDEFINED) {
			if (DataTypeConstant.isNumberType(fld.getField()
					.getDataType())||fld.getField().getExtType()==RptProvider.MEASURE){
				model.setCellProperty(aimPos, PropertyType.DataType,
						TableConstant.CELLTYPE_NUMBER);
				Format ff = model.getCellFormat(aimPos);
				if(ff != null && ff instanceof IufoFormat){
					((IufoFormat)ff).setDecimalDigits(2);
				}
			}else{
				model.setCellProperty(aimPos, PropertyType.DataType,
						TableConstant.CELLTYPE_SAMPLE);
			}
//			}
			model.setCellValue(aimPos, fld.getFieldLabel());
			model.getCell(aimPos).addExtFmt(AnaRepField.EXKEY_FIELDINFO, fld);
		}
	}

	private static AreaDataModel getAreaDataModel(CellsModel model, CellPosition pos) {
		ExAreaCell exCell = ExAreaModel.getInstance(model).getExArea(pos.getRow(), pos.getColumn());
		if (exCell != null && exCell.getModel() instanceof AreaDataModel)
			return (AreaDataModel) exCell.getModel();

		return null;
	}


	private boolean isMeasOrSubTotal(AnaRepField anaFld, boolean isMeas) {
		if (anaFld != null && anaFld.getFieldType() > AnaRepField.Type_CROSS_LEFTTOP
				&& anaFld.getField() instanceof FieldCountDef) {
			if (isMeas && ((FieldCountDef) anaFld.getField()).getRangeFld() == null)
				return true;
			if (!isMeas && ((FieldCountDef) anaFld.getField()).getRangeFld() != null)
				return true;

		}

		return false;
	}
	

    /**
     * 选中区域中是否有字段
     * @param model
     * @param selPos
     * @return
     */
	public static boolean isSelAnaField(CellsModel model,CellPosition[] selPos) {
		if(selPos == null || selPos.length == 0)
			return false;
		for(CellPosition pos : selPos){
				if (model.getCell(pos) != null && model.getCell(pos).getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null)
					return true;
		}		
		return false;
	}
	/**
	 * 
	 * @param cross
	 * @param area
	 * @return
	 */
	public static boolean isInMeasureArea(AnaCrossTableSet cross,CellPosition cellPos){
		if(cross != null && cross.getCrossType(cellPos) == AnaRepField.Type_CROSS_MEASURE)
			return true;
		return false;
	}
}
