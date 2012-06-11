package com.ufida.report.anareport.edit;

//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Rectangle;
import java.awt.Component;
import javax.swing.JLabel;

import nc.ui.pub.beans.UILabel;

import com.ufida.report.anareport.model.AnaRepField;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;
/**
 * ÅÅÃûº¯Êý»æÖÆÆ÷
 * @author guogang
 *
 */
public class RankFunctionRender implements SheetCellRenderer{

//	private JLabel rankFuncComp = 
//		new nc.ui.pub.beans.UILabel(){
//		private static final long serialVersionUID = 1L;
//    	protected  void paintComponent(Graphics g){
//    		Rectangle rect = getBounds();
//    		int width = rect.width;
//    		int height = rect.height;
//    		super.setOpaque(false);
//    		Color preColor = g.getColor();
//    		g.getFont().deriveFont(6);
//    		g.setColor(Color.RED);
//    		g.drawString("¡Ò", width-48, (height-height+8));
//    		g.setColor(Color.GREEN);
//    		g.drawString("F", width-40, (height-height+8));
//    		g.setColor(preColor);
//    	}
//	};
	private JLabel rankFuncComp =null;
	
	public RankFunctionRender() {

	}
		
	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		if(cell == null||!(cellsPane.getOperationState() == ReportContextKey.OPERATION_FORMAT))
			return null;
		AnaRepField field =null;
		if(cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) instanceof AnaRepField){
			field=(AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		}
		if(field == null)
			return null;
		
		if(field.getRankInfo()!=null&&field.getRankInfo().isEnabled())
			return getPaintCompnent();
		return null;
	}
    
	private JLabel getPaintCompnent(){
		if(rankFuncComp==null){
			rankFuncComp=new UILabel();
			rankFuncComp.setIcon(AnaFieldFixFieldRender.getBackImage("reportcore/rank_func_back.gif",3));
			rankFuncComp.setOpaque(false);
			rankFuncComp.setVerticalAlignment(JLabel.TOP);
		}
		return rankFuncComp;
	}
}
