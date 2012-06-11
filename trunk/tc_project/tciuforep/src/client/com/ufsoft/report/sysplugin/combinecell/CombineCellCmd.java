/*
 * Created on 2004-12-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufsoft.report.sysplugin.combinecell;

import java.util.ArrayList;
import java.util.Iterator;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.CombineCellDlg;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.IAreaAtt;
import com.ufsoft.table.TableDataModelException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.IVerify.VerifyType;

/**
 * 组合单元设置的操作
 * @author zzl
 * @version 5.0
 * Create on 2004-12-1
 */
public class CombineCellCmd extends UfoCommand {
	
	protected UfoReport m_rep;
	protected CellsModel m_cm;

	public void execute(Object[] params) {
		m_rep = (UfoReport)params[0];
		m_cm = m_rep.getCellsModel();
		AreaPosition selArea = m_cm.getSelectModel().getSelectedArea();

		CombineCellDlg dialog = new CombineCellDlg(selArea.toString(),m_rep.getFrame());
		dialog.setVisible(true);
		if (dialog.getResult() == CombineCellDlg.ID_COMALL){//整体组合
		   doCombineCell(selArea,new AreaPosition[]{selArea});
		}else if (dialog.getResult() == CombineCellDlg.ID_CANCEL){
		    return;
		}
		if (dialog.getResult() == CombineCellDlg.ID_COMCOL){//按列组合
		    AreaPosition[] colAreas = new AreaPosition[selArea.getWidth()];
		    int startRow = selArea.getStart().getRow();
		    int startCol = selArea.getStart().getColumn();
		    for(int i=0;i<selArea.getWidth();i++){
		        colAreas[i] = AreaPosition.getInstance(startRow,startCol+i,1,selArea.getHeigth());
		    }
		    doCombineCell(selArea,colAreas);
		}
		if (dialog.getResult() == CombineCellDlg.ID_COMROW){//按行组合
		    AreaPosition[] rowAreas = new AreaPosition[selArea.getHeigth()];
		    int startRow = selArea.getStart().getRow();
		    int startCol = selArea.getStart().getColumn();
		    for(int i=0;i<selArea.getHeigth();i++){
		        rowAreas[i] = AreaPosition.getInstance(startRow+i,startCol,selArea.getWidth(),1);
		    }
		    doCombineCell(selArea,rowAreas);
		}	
		if (dialog.getResult() == CombineCellDlg.ID_COMREMOVE){//删除组合单元	
		    delCombineCell(selArea, m_rep.getTable());
		}
 
	}
	
	/**
	 *判断区域是否可以进行组合单元的操作. 
	 *如果支持生成新文件方式备份的undo功能,这里就可以直接执行,遇到不符合条件的情况后,再回退即可.
	 *就不用全部检查通过后再执行了.
	 * @param area
	 * @return boolean
	 */
	protected boolean isCanCombineCell(AreaPosition area){
		if(area.isCell()){
			UfoPublic.sendErrorMessage(MultiLang.getString("miufo1001828"),m_rep,null);  //"单个单元不能设置或取消组合"
			return false;
		}
	    if(area.getWidth()*area.getHeigth() > 300){
			UfoPublic.sendErrorMessage(MultiLang.getString("miufo1000741"),m_rep,null);  //"组合单元包含最大单元个数为300"
			return false;
	    }	 
        //在左上单元之外的区域存在指标或者关键字，不允许组合。
		ArrayList list =  m_cm.getSeperateCellPos(area);
		for(int i=1;i<list.size();i++)	{
		    CellPosition pos = (CellPosition)list.get(i);
		    Cell cell = m_cm.getCell(pos);
		    if(cell != null && m_cm.isVerify(pos,VerifyType.UNSUPPORT_COMBINED,true)){
		        UfoPublic.sendErrorMessage(MultiLang.getString("miufo1001835"),m_rep,null);  //"在左上单元之外的区域存在指标或者关键字，不允许组合"
		        return false;
		    }
		}
		//插件注册插件是否允许区域合并操作
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.COMBINECELL, area, null);
		if (!m_rep.getTable().checkEvent(event)) {
			return false;
		}
		return true;
	}
	

	/**
	 * 删除特定区域里的所有组和单元.特定区域可以大于组合单元区域.
	 * @param area
	 */
	public static void delCombineCell(AreaPosition area, UFOTable ufoTable){
	    CellsModel cellsModel = ufoTable.getCellsModel();
		CombinedAreaModel crm = CombinedAreaModel.getInstance(cellsModel);
		CombinedCell[] ccs = crm.getCombineCells(area);
	     
        for (CombinedCell cc: ccs) {
        	
        	UserUIEvent event = new UserUIEvent(ufoTable, UserUIEvent.UNCOMBINECELL,
    				cc.getArea(), null);
        	
    		//插件是否允许操作.
    		if (!ufoTable.checkEvent(event)) {
    			continue;
    		}
    		crm.removeCombinedCell(cc);
    		
    		ufoTable.fireEvent(event);
        	 
        }
     
	}
	
	/**
	 * 合并单元.组合最后选择区域的单元.
	 * 
	 * @param area
	 * @throws TableDataModelException
	 */
	public void combineCell(AreaPosition area,UFOTable ufoTable) throws TableDataModelException {
		if (area == null || area.isCell()) {
			return;
		}
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.COMBINECELL,
				area, null);
		//插件是否允许操作.
		if (!ufoTable.checkEvent(event)) {
			return;
		}
		//插件操作放在模型操作前,对模型操作进行预处理.
		ufoTable.fireEvent(event);
		//模型操作.
		CellsModel cm = m_rep.getCellsModel();
		ArrayList areaDatas = cm.getAreaDatas();
		if (areaDatas != null) {
			Iterator iter = areaDatas.iterator();
			ArrayList listRemove = new ArrayList();
			//检查组合区域是否交叉，是否容纳旧的区域；
			while (iter.hasNext()) {
				IAreaAtt att = (IAreaAtt) iter.next();
				if (att instanceof CombinedCell) {
					//如果区域相交但是不相含，抛出异常。
					if (att.getArea().intersection(area)) {
						if (area.contain(att.getArea())) { //新区域包含旧区域，记录需要删除的旧区域。
							listRemove.add(att);
						} else {
							throw new TableDataModelException();
						}
					}
				}
			}
			areaDatas.removeAll(listRemove);
		}
		//得到当前合并单元首单元的数据。
		cm.combineCell(area);
	}

	/**
	 * add by 王宇光 2008-6-4 获得目标区域
	 * 
	 * @param AreaPosition
	 *            areaSrc：原区域,AreaPosition areaCombine：原区域中组合区域,CellPosition
	 *            target，目标区域锚点
	 * @return AreaPosition
	 */
	public static AreaPosition getDestAreaPos(AreaPosition areaSrc,
			AreaPosition areaCombine, CellPosition target) {
		if (areaSrc == null || areaCombine == null || target == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// 输入参数不允许为空
		}
		int iStartRowSrc = areaSrc.getStart().getRow();
		int iStartColumnSrc = areaSrc.getStart().getColumn();
		int iCombineStartRow = areaCombine.getStart().getRow();
		int iCombineStartColumn = areaCombine.getStart().getColumn();
		int iOffRow = iCombineStartRow - iStartRowSrc;
		int iOffColumn = iCombineStartColumn - iStartColumnSrc;
		int iAreaWidth = areaCombine.getWidth();
		int iAreaHeight = areaCombine.getHeigth();
		AreaPosition areaPos = AreaPosition.getInstance(target.getRow()
				+ iOffRow, target.getColumn() + iOffColumn, iAreaWidth,
				iAreaHeight);
		return areaPos;
	}
	
	/**执行合并的动作.
	 * @param selArea 整个选中区域.
	 * @param areas 需要合并的小区域.
	 */
	public boolean doCombineCell(AreaPosition selArea,AreaPosition[] areas){
	    //首先判断是否可以执行.
	    for(int i=0;i<areas.length;i++){
	        if(!isCanCombineCell(areas[i])){
	            return false;
	        }
	    }
	    //开始执行操作.
	    delCombineCell(selArea, m_rep.getTable());
	    try {
            for (int i = 0; i < areas.length; i++) {
                combineCell(areas[i],m_rep.getTable());
            }
//        } catch (TableDataModelException e) {
//            //已清除掉了内含的组合单元.不应抛出例外.
//            AppDebug.debug(e);
        } finally {
        	CombinedAreaModel.getInstance(m_rep.getCellsModel()).clearCache();
        	
        }
        return true;
	}
	/**
	 * modify by 王宇光 2008-4-21 提供给外部的静态方法,用于包含业务判断的单元格合并.
	 * @param area
	 * @param cm
	 */
	public static void combineCell(AreaPosition area,UfoReport m_rep){
	    CombineCellCmd cmd = new CombineCellCmd();
	    cmd.m_cm = m_rep.getCellsModel();
	    cmd.m_rep = m_rep;
	    cmd.doCombineCell(area,new AreaPosition[]{area});
	}
}