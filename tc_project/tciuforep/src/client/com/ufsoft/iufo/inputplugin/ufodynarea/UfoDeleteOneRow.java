package com.ufsoft.iufo.inputplugin.ufodynarea;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.header.HeaderModel;

/**
 * 
 * @author Administrator 2005-7-1
 */
public class UfoDeleteOneRow extends AbsUfoDynAreaDelRowExt {

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaDelRowExt#getDelCount()
     */
    protected int getDelCount() {
    	CellsModel cellsModel=getReportDesigner().getCellsModel();
    	
        CellPosition anchorCell = cellsModel.getSelectModel().getAnchorCell();
        if(anchorCell == null)
        	return 0;
        
        AreaPosition selArea=cellsModel.getSelectModel().getSelectedArea();
        DynAreaCell dynAreaCell =getAnchorDynAreaCell(getReportDesigner());
        if (dynAreaCell==null)
        	return 0;
        
        boolean isRow = dynAreaCell.getDirection() == DynAreaVO.DIRECTION_ROW;
        int delPos = isRow ? anchorCell.getRow():anchorCell.getColumn();
        
        AreaPosition area = dynAreaCell.getArea();
        //修正位置为分组的起始位置
        int groupLen = dynAreaCell.getHeaderCount();
        int startPos = isRow ? area.getStart().getRow() : area.getStart().getColumn();
        delPos = startPos + (delPos-startPos)/groupLen * groupLen;
        
        int iDelLen=0;
        if (isRow){
        	//锚点是选中第一行
        	if (selArea.getEnd().getRow()>=delPos){
        		iDelLen=1+(selArea.getEnd().getRow()-delPos)/groupLen;
        	}else{
        		iDelLen=1+(delPos-selArea.getStart().getRow())/groupLen;
        		iDelLen=-iDelLen;
        	}
        }else{
        	//锚点是选中第一列
        	if (selArea.getEnd().getColumn()>=delPos){
        		iDelLen=1+(selArea.getEnd().getColumn()-delPos)/groupLen;
        	}else{
        		iDelLen=1+(delPos-selArea.getStart().getColumn())/groupLen;
        		iDelLen=-iDelLen;
        	}
        }
        
        return iDelLen;
    }

    /*
     * @see com.ufsoft.iufo.inputplugin.dynarea.AbsDynAreaActionExt#getMenuName()
     */
    protected String getMenuName() {
        return StringResource.getStringResource("uiufotask00079");
    }

	protected String getIconName() {
		return "images/reportcore/removerow.gif";
	}
	
	protected KeyStroke getActionKeyStroke() {
		return KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK);
	}

	protected int getMemonic() {
		return 'X';
	}
}
