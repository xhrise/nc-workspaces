package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import nc.ui.pub.beans.UILabel;

import com.ufida.report.anareport.model.AnaRepField;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

public class AnaFieldFixFieldRender implements SheetCellRenderer {
	private JLabel fixFieldComp = null;
	public AnaFieldFixFieldRender() {
		
	}

	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		if (cell == null
				|| !(cellsPane.getOperationState() == ReportContextKey.OPERATION_FORMAT))
			return null;
		AnaRepField field = null;
		if (cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) instanceof AnaRepField) {
			field = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		}
		if (field == null)
			return null;
		if(field.getDimInfo()!=null&&field.getDimInfo().isFix()){
			return getPaintCompnent();
		}
			
		return null;
	}

	private JLabel getPaintCompnent(){
		if(fixFieldComp==null){
			fixFieldComp=new UILabel();
			fixFieldComp.setIcon(getBackImage("reportcore/fixField_back.gif",0));
			fixFieldComp.setOpaque(false);
			fixFieldComp.setVerticalAlignment(JLabel.TOP);
		}
		return fixFieldComp;
	}
	
	public static ImageIcon getBackImage(String fileName,int horizontalIndex){
		String imageFile=ResConst.UfoGifPath+fileName;
		BackImageIcon backImage=new BackImageIcon(ResConst.class.getResource(imageFile));
		backImage.setNumIndex(horizontalIndex);
		return backImage;
	}
	
	public static class BackImageIcon extends ImageIcon{
        private int numIndex=0;
        
        public BackImageIcon(URL location){
        	super(location);
        }
		@Override
		public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
			x+=numIndex*10;
			super.paintIcon(c, g, x, y);
		}
		public void setNumIndex(int numIndex) {
			this.numIndex = numIndex;
		}
		
	}
}
