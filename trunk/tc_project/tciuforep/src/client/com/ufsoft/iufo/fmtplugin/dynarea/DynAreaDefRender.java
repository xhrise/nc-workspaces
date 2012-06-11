package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JLabel;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

public class DynAreaDefRender implements SheetCellRenderer{

	 //行动态区显示组件.
    private JLabel lblDynAreaRowDirectComp = 
    	new nc.ui.pub.beans.UILabel() {
    	private static final long serialVersionUID = 1L;
    	protected  void paintComponent(Graphics g){
    		Rectangle rect = getBounds();
    		int width = rect.width;
    		int height = rect.height;
    		Color preColor = g.getColor();
    		g.setColor(Color.BLUE);
    		
    		Graphics2D g2d = (Graphics2D)g;
        	g2d.fillPolygon(
        			new int[]{(int)(width/2-3),(int)(width/2),(int)(width/2+3)},
        			new int[]{(int)height-4,(int)height,(int)height-4},
        			3);
    		g.setColor(preColor);
    	}
    };
    
    //列动态区显示组件.
    private JLabel lblDynAreaColDirectComp = 
    	new nc.ui.pub.beans.UILabel() {
    	private static final long serialVersionUID = 1L;
    	protected  void paintComponent(Graphics g){
    		Rectangle rect = getBounds();
    		int width = rect.width;
    		int height = rect.height;
    		Color preColor = g.getColor();
    		g.setColor(Color.BLUE);
    		
    		Graphics2D g2d = (Graphics2D)g;
        	g2d.fillPolygon(
        			new int[]{(int)width-4,(int)width,(int)width-4},
        			new int[]{(int)(height/2-3),(int)(height/2),(int)(height/2+3)},
        			3);
    		g.setColor(preColor);
    	}
    };
 
	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		//edit by wangyga 此版暂时这样处理
     	if(cellsPane.getOperationState() != ReportContextKey.OPERATION_FORMAT){
			return null;
		}
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsPane.getDataModel());
		CellPosition cellPos = CellPosition.getInstance(row,column);
		DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByFmtPos(cellPos);
		if(dynAreaCell == null){
			return null;
		}
		
		// @edit by wangyga at 2008-12-26,下午01:24:07 设置时,添加默认颜色，但愿属性还可以修改

//		IufoFormat f = (IufoFormat)cellsPane.getDataModel().getCellFormatIfNullNew(cellPos);
//		f.setLineType(Format.TOPLINE, TableConstant.L_SOLID1);
//		f.setLineType(Format.BOTTOMLINE, TableConstant.L_SOLID1);
//		f.setLineType(Format.LEFTLINE, TableConstant.L_SOLID1);
//		f.setLineType(Format.RIGHTLINE, TableConstant.L_SOLID1);
//		f.setLineColor(Format.TOPLINE, Color.BLUE);
//		f.setLineColor(Format.BOTTOMLINE, Color.BLUE);
//		f.setLineColor(Format.LEFTLINE, Color.BLUE);
//		f.setLineColor(Format.RIGHTLINE, Color.BLUE);
//		CellPosition[] extCellPos = dynAreaCell.getArea().split();
//		for(CellPosition c :extCellPos){
//			f = (IufoFormat)cellsPane.getDataModel().getCellFormatIfNullNew(c);
//			f.setLineType(Format.TOPLINE, TableConstant.L_SOLID1);
//			f.setLineType(Format.BOTTOMLINE, TableConstant.L_SOLID1);
//			f.setLineType(Format.LEFTLINE, TableConstant.L_SOLID1);
//			f.setLineType(Format.RIGHTLINE, TableConstant.L_SOLID1);
//			f.setLineColor(Format.TOPLINE, Color.BLUE);
//			f.setLineColor(Format.BOTTOMLINE, Color.BLUE);
//			f.setLineColor(Format.LEFTLINE, Color.BLUE);
//			f.setLineColor(Format.RIGHTLINE, Color.BLUE);
//		}
		
		if(dynAreaModel.getDynAreaDirection() == DynAreaVO.DIRECTION_COL){
		    return lblDynAreaColDirectComp;
		} else if(dynAreaModel.getDynAreaDirection() == DynAreaVO.DIRECTION_ROW){
		    return lblDynAreaRowDirectComp;
		} else{
		    return null;
		}
	}
}
