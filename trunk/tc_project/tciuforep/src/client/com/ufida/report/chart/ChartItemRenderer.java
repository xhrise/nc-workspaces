/**
 * 
 */
package com.ufida.report.chart;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.ufsoft.iufo.fmtplugin.chart.ChartConstants;
import com.ufsoft.iufo.fmtplugin.chart.ChartDesc;

/**
 * @author wangyga
 * @created at 2009-5-27,ÉÏÎç11:02:40
 *
 */
public class ChartItemRenderer extends JLabel implements ListCellRenderer{
	private static final long serialVersionUID = 2183938572001825814L;
	
	private javax.swing.border.Border emptyBorder = BorderFactory
			.createEmptyBorder(4, 4, 4, 4);

	public ChartItemRenderer() {
		super();
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		ChartDesc cd = (ChartDesc) value;
		this.setText(cd.getName());
		this.setIcon(ChartConstants.getImageIcon(cd.getImgFileName()));
		if (isSelected) {
			this.setForeground(list.getSelectionForeground());
			this.setBackground(list.getSelectionBackground());
		} else {
			this.setForeground(list.getForeground());
			this.setBackground(list.getBackground());
		}

		// if (cellHasFocus)
		// this.setBorder(lineBorder);
		// else
		this.setBorder(emptyBorder);

		return this;
	}
}
