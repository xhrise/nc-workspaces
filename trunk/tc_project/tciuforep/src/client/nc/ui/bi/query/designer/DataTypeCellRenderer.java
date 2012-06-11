package nc.ui.bi.query.designer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;

/**
 * 用于显示字段是否为数值类型的列表项修饰 创建日期：(2001-4-14 13:38:11)
 * 
 * @author：朱俊彬
 */
public class DataTypeCellRenderer implements ListCellRenderer {
	JLabel label = new nc.ui.pub.beans.UILabel();

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		label.setText(value.toString());
		label.setOpaque(true);

		Color color = Color.black;
		if (value instanceof SelectFldVO) {
			SelectFldVO sf = (SelectFldVO) value;
			if (sf.getColtype() != null
					&& BIModelUtil.isNumberType(sf.getColtype().intValue()))
				color = Color.blue;
		}

		label.setBackground(isSelected ? list.getSelectionBackground()
				: Color.white);
		label.setForeground(color);
		return label;
	}
}
