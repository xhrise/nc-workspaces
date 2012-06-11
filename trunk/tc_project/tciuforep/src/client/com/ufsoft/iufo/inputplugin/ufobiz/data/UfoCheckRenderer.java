package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.view.KeyCondPanel;

import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

public class UfoCheckRenderer implements SheetCellRenderer {
	private Map<Color,Component> m_hashComp=new Hashtable<Color,Component>();
	
	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		//edit by wangyga 此版暂时这样处理
     	if(cellsPane.getOperationState() == ReportContextKey.OPERATION_FORMAT){
			return null;
		}
     	
		RepDataEditor editor=getRepDataEditor(cellsPane);
		if (editor==null)
			return null;
		
		RepDataControler controler=RepDataControler.getInstance(KeyCondPanel.getMainboard(cellsPane));
		if(controler == null){
			return null;
		}
		Color color=controler.getTaskCheckColor(editor);
		List<CellPosition> listCheckCell=controler.getTaskCheckCells(editor);
		if (color==null){
			color=editor.getRepCheckColor();
			listCheckCell=editor.getRepCheckCell();
		}
		
		if (color != null && listCheckCell != null 
				&& listCheckCell.contains(CellPosition.getInstance(row, column))) {
			Component com=m_hashComp.get(color);
			if (com==null){
				com=new JExComponent(color);
				m_hashComp.put(color, com);
			}
			return com;
		}
		return null;
	}

	public UfoCheckRenderer() {
		super();
	}
	
	private RepDataEditor getRepDataEditor(CellsPane cellsPane){
		Component comp=cellsPane;
		while (comp!=null){
			if (comp instanceof RepDataEditor)
				return (RepDataEditor)comp;
			comp=comp.getParent();
		}
		return null;
	}
	
	class JExComponent extends JComponent{
		private static final long serialVersionUID = -3091976093904212340L;
		
		private Color m_color=null;
		JExComponent(Color color){
			super();
			m_color=color;
		}
		
		protected void paintComponent(Graphics g) {
			Rectangle rect = this.getBounds();
			int width = rect.width;
			int height = rect.height;
			Color preColor = g.getColor();
			g.setColor(m_color);
			g.fillRect(0, 0, width, height);
			g.setColor(preColor);
		}
	}
}
