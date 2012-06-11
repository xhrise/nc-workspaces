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
 * topN»æÖÆÆ÷
 * @author wangyga
 *
 */
public class TopNRenderer implements SheetCellRenderer{
//	private JLabel topNComp = 
//		new nc.ui.pub.beans.UILabel(){
//		private static final long serialVersionUID = 1L;
//    	protected  void paintComponent(Graphics g){
//    		Rectangle rect = getBounds();
//    		int width = rect.width;
//    		int height = rect.height;
//    		super.setOpaque(false);
//    		Color preColor = g.getColor();
//    		g.getFont().deriveFont(6);
//    		g.setColor(Color.BLUE);
//    		g.drawString("top", width-24, (height-height+8));
//    		g.setColor(Color.GREEN);
//    		g.drawString("N", width-8, (height-height+8));
//    		g.setColor(preColor);
//    	}
//	};
	private JLabel topNComp=null;
	
	public TopNRenderer() {

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
		
		if(field.getTopNInfo()!=null&&field.getTopNInfo().isEnabled())
			return getPaintCompnent();
		return null;
	}
    
	private JLabel getPaintCompnent(){
		if(topNComp==null){
			topNComp=new UILabel();
			topNComp.setIcon(AnaFieldFixFieldRender.getBackImage("reportcore/topN_back.gif",2));
			topNComp.setOpaque(false);
			topNComp.setVerticalAlignment(JLabel.TOP);
		}
		return topNComp;
	}
}
