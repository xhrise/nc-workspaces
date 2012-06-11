package com.ufsoft.iufo.inputplugin.autocalc;


import java.util.ArrayList;
import java.util.Iterator;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.table.AreaCell;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.IAreaAtt;


/**
 * ����: �ṩһ�鶯̬�������Ĺ��߷�����
 * ��������:(2005-9-19 22:08:34)
 * @author chxiaowei
 */
public class DynAreaOperUtil {
	  /**
     * ���ܣ����ݸ�ʽ���ʱ�ĸ�ʽλ��,�õ�����̬��̬����չ���ʵ��λ��.
     * @param fmtArea
     * @param cellsModel
     * @return
     */
    public static IArea getRealArea(IArea fmtArea,CellsModel cellsModel){
    	IArea areaReturn=fmtArea;
    	ArrayList list = cellsModel.getAreaDatas();
    	for (int i = 0; i < list.size(); i++) {
    		AreaCell areaCell = (AreaCell) list.get(i);
    		if(areaCell instanceof DynAreaCell){
    			DynAreaCell dynCell = (DynAreaCell)areaCell;
    			AreaPosition dynOriArea = dynCell.getDynAreaVO().getOriArea();
    			int direction = dynCell.getDynAreaVO().getDirection();
    			if(direction == DynAreaVO.DIRECTION_ROW){
    				if(fmtArea.getStart().getRow() > dynOriArea.getEnd().getRow()){
    					int dRow = dynCell.getArea().getHeigth() - dynOriArea.getHeigth();
    					areaReturn = areaReturn.getMoveArea(dRow,0);
    				}
    			}else{
    				if(fmtArea.getStart().getColumn() > dynOriArea.getStart().getColumn()){
    					int dCol = dynCell.getArea().getWidth() - dynOriArea.getWidth();
    					areaReturn = areaReturn.getMoveArea(0,dCol);
    				}
    			}
    		}
    	}
    	return areaReturn;
    }
    
    /**
	 * ���ܣ�����ʵ��λ�õõ����ʽ���ʱ��λ�á�
	 * @param realArea
	 * @param cellsModel
	 * @return
	 */
    public static IArea getFormatArea(IArea realArea,CellsModel cellsModel){
    	DynAreaCell[] dynCells = getDynAreaCells(cellsModel);
    	if(dynCells.length == 0){
    		return realArea;
    	}
    	if(dynCells[0].getDirection() == DynAreaVO.DIRECTION_ROW){
    		int dRow = 0;
    		for(int i=0;i<dynCells.length;i++){
    			if(dynCells[i].getArea().contain(realArea.getStart())){
    				int relativeRow = realArea.getStart().getRow()-dynCells[i].getArea().getStart().getRow();
    				int offSet = relativeRow / dynCells[i].getDynAreaVO().getOriArea().getHeigth();
    				dRow += (offSet * dynCells[i].getDynAreaVO().getOriArea().getHeigth());
    			}else if(realArea.getStart().getRow() > dynCells[i].getArea().getEnd().getRow()){
    				dRow += (dynCells[i].getArea().getHeigth() - dynCells[i].getDynAreaVO().getOriArea().getHeigth());
    			}				
    		}
    		return realArea.getMoveArea(-dRow,0);
    	} else{
    		int dCol = 0;
    		for(int i=0;i<dynCells.length;i++){
    			if(dynCells[i].getArea().contain(realArea.getStart())){
    				int relativeCol = realArea.getStart().getColumn()-dynCells[i].getArea().getStart().getColumn();
    				int offSet = relativeCol / dynCells[i].getDynAreaVO().getOriArea().getWidth();
    				dCol += (offSet * dynCells[i].getDynAreaVO().getOriArea().getWidth());
    			}else if(realArea.getStart().getColumn() > dynCells[i].getArea().getEnd().getColumn()){
    				dCol += (dynCells[i].getArea().getWidth() - dynCells[i].getDynAreaVO().getOriArea().getWidth());
    			}				
    		}
    		return realArea.getMoveArea(0,-dCol);
    	}
    }
    
    /**
     * ���ܣ��õ���ǰ���ģ���еĶ�̬�������ݡ�
     * @return DynAreaCell[]
     */
    private static DynAreaCell[] getDynAreaCells(CellsModel cellsModel){
        ArrayList dynCells = new ArrayList();
        ArrayList allAreaCell = cellsModel.getAreaDatas();
        for (Iterator iter = allAreaCell.iterator();iter.hasNext();) {
            IAreaAtt each = (IAreaAtt) iter.next();
            if(each instanceof DynAreaCell){
                dynCells.add(each);
            }
        }
        
        return (DynAreaCell[]) dynCells.toArray(new DynAreaCell[dynCells.size()]);
    }
    
    /**
     * ���ݵ�Ԫ��Ϣ��ö�̬������Ϣ�� (��������̬ģ�ͣ�Ҫ��������Ϊ����״̬����)  
     * 
     * @param cellsModel
     * @param cell
     * @return DynAreaCell
     */
    public static DynAreaCell getDynAreaCellByPos(CellsModel cellsModel, CellPosition cell) {
    	if(cell == null) return null;
    	DynAreaCell[] dynCells = getDynAreaCells(cellsModel);
    	for(int i=0;i<dynCells.length;i++){
    		if(dynCells[i].getArea().contain(cell)){
    			return dynCells[i];
    		}
    	}
    	return null;
    }
    
    /**
	 * ���ָ��λ���Ƿ��ڶ�̬��
	 * @param cellsModel
	 * @param realPos ��Ԫ��ʵ��λ��
	 * @return
	 */
	public static boolean isInDynArea(CellsModel cellsModel, CellPosition realPos){
		return DynAreaCell.isInDynArea(realPos, cellsModel);
	}
	
}
