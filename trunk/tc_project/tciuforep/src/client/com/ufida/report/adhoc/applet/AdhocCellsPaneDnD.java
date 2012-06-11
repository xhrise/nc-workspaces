/*
 * Created on 2005-6-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.adhoc.applet;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.JOptionPane;

import nc.vo.bi.query.manager.MetaDataVO;

import com.ufida.bi.base.BIException;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.rep.model.AreaPositionSelection;
import com.ufida.report.rep.model.DefaultReportField;
import com.ufida.report.rep.model.MetaDataVOSelection;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;

/**
 * Adhoc报表设计区域鼠标拖拽控制类
 * @author caijie
 */
public class AdhocCellsPaneDnD extends DefaultCellsPaneDnD{
    AdhocPlugin adhocPlugin = null;
    /**
     * @param cellsPane
     */
    public AdhocCellsPaneDnD(CellsPane cellsPane, AdhocPlugin adhocPlugin) {
        super(cellsPane);       
        this.adhocPlugin = adhocPlugin;
    }
    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
     */
    public void drop(DropTargetDropEvent dtde) {
        try {            
            Transferable tr = dtde.getTransferable();
            int dropRow = getCellsPane().rowAtPoint(dtde.getLocation());
            int dropCol = getCellsPane().columnAtPoint(dtde.getLocation());
            
            
//            int areaType = getAdhocModel().getAreaTypeByRow(dropRow);
//            if(areaType != AdhocArea.UNDIFINED_AREA_TYPE){
//                if((getAdhocModel().getAreaByType(areaType).getStartRow() == dropRow) 
//                        && (getAdhocModel().getAreaByType(areaType).isTopBarVisisble())) {                       
//                    return;//标题栏上释放无效
//                }
//            }
            
            //移动区域
            try {
				if (tr.isDataFlavorSupported(AreaPositionSelection.AreaPositionFlavor)) {               
				    dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);                 
				    AreaPosition oldArea = (AreaPosition) tr.getTransferData(AreaPositionSelection.AreaPositionFlavor); 
				    this.getAdhocModel().moveCells(oldArea, CellPosition.getInstance(dropRow, dropCol));
				    
				    // 回马一枪:利用选择模型的重新绘制机制清除旧单元格的值
				    getCellsPane().changeSelectionByUser(oldArea.getStart().getRow(), oldArea.getStart().getColumn(), false, false, false);
				}
			} catch (Exception e) {
				adhocPlugin.showErrorMessage(e.getMessage());    
				AreaPosition oldArea = (AreaPosition) tr.getTransferData(AreaPositionSelection.AreaPositionFlavor);
				getCellsPane().changeSelectionByUser(oldArea.getStart().getRow(), oldArea.getStart().getColumn(), false, false, false);
				return;
			}
            
//          拖动源为查询对象数
			CellPosition fmtPos = getAdhocModel().getFormatArea(CellPosition.getInstance(dropRow, dropCol), false, false).getStart();
            if (tr.isDataFlavorSupported(MetaDataVOSelection.ReportFieldFlavor)) {
                dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY); 
                DefaultReportField selectFldVO = (DefaultReportField) tr.getTransferData(MetaDataVOSelection.ReportFieldFlavor);
                this.getAdhocModel().addQueryField(selectFldVO, fmtPos);                
            }else if (tr.isDataFlavorSupported(MetaDataVOSelection.MetaDataVOFlavor)) {
                dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY); 
                MetaDataVO selectFldVO = (MetaDataVO) tr.getTransferData(MetaDataVOSelection.MetaDataVOFlavor);
                this.getAdhocModel().addQueryField(new DefaultReportField(selectFldVO), fmtPos);                
            }
            
            //
            if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY); 
                String  str = (String) tr.getTransferData(DataFlavor.stringFlavor);
                this.getAdhocModel().getCellsModel().setCellValue(dropRow, dropCol, str);  
            }
            getCellsPane().changeSelectionByUser(dropRow, dropCol, false, false, false);
            dtde.dropComplete(true);
        }  catch (BIException e1) {		
        	AppDebug.debug(e1);
        	adhocPlugin.showErrorMessage(e1.getMessage());
		}catch (Exception e) { 
			AppDebug.debug(e);
        	adhocPlugin.showErrorMessage(e.getMessage());           
        }        
        
    }   
    private AdhocModel getAdhocModel() {
        return this.adhocPlugin.getModel();
    }
}
