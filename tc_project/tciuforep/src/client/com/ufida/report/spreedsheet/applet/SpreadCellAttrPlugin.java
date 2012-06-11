package com.ufida.report.spreedsheet.applet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.cellattr.CellAttrPlugin;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

public class SpreadCellAttrPlugin extends CellAttrPlugin {// implements
	// SelectListener {
	private final class MeasureDefRenderer implements SheetCellRenderer {
		JComponent com = new JComponent() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				Rectangle rect = this.getBounds();
				int width = rect.width;
				int height = rect.height;
				Color preColor = g.getColor();
				g.setColor(Color.red);
				g.fillRect(width - width / 12, height - height / 5, width / 12, height / 5);
				g.setColor(preColor);
			}
		};

		public Component getCellRendererComponent(CellsPane cellsPane,  Object obj, boolean isSelected,
				boolean hasFocus, int row, int column, Cell cell) {
			if(cell == null){
				return null;
			}
			try {
				if (getReport().getOperationState() == UfoReport.OPERATION_FORMAT) {
					if (cell.getExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP) != null) {
						return com;
					}
				}
			} catch (Exception e) {
			}
			return null;
		}
	}

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				ICommandExt extSetCellAttr = new SpreadCellAttrExt(getReport());
				return new IExtension[] { extSetCellAttr };
			}

		};
	}

	public void startup() {
		super.startup();
		getReport().getTable().getCells().registExtSheetRenderer(new MeasureDefRenderer());
	}

}
