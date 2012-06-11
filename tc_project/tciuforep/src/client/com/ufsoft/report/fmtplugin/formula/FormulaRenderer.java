package com.ufsoft.report.fmtplugin.formula;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import com.ufsoft.report.ReportContextKey;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;
/**
 * 公式单元格渲染器
 */
public class FormulaRenderer implements SheetCellRenderer {

	private AreaFormulaPlugin plugin;
	
	public FormulaRenderer() {
		super();
	}
	
	public FormulaRenderer(AreaFormulaPlugin plugin) {
		super();
        this.plugin = plugin;
	}

	// 单元公式显示组件.
	protected static final JLabel lblCFmlComp = new nc.ui.pub.beans.UILabel() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = getBounds();
			int width = rect.width;
			int height = rect.height;

			Color preColor = g.getColor();
			int fontHeight = g.getFontMetrics().getAscent();
			g.setColor(Color.BLUE);
			g.drawString("f", width - width + 1,
					(height / 2 + (fontHeight / 2)));
			g.setColor(new Color(0, 128, 0));
			g.drawString("c", width - width + 5,
					(height / 2 + (fontHeight / 2)));
			g.setColor(preColor);
		}
	};

	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		if(!plugin.isFmlRendererVisible()){
			return null;
		}
		
		//edit by wangyga 此版暂时这样处理
		if(cellsPane.getOperationState() != ReportContextKey.OPERATION_FORMAT){
			return null;
		}
		
		if (cell == null) {
			return null;
		}
		CellPosition cellPos = CellPosition.getInstance(row, column);
		AreaFormulaModel areaFormulaModel = AreaFormulaModel
				.getInstance(cellsPane.getDataModel());
		Object[] objs = areaFormulaModel.getRelatedFmlVO(cellPos);
		FormulaVO fmlVO = (FormulaVO) objs[1];
		if (fmlVO == null)
			return null;
		return lblCFmlComp;
	}

}
