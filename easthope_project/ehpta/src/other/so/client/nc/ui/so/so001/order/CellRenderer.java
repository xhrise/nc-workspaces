package nc.ui.so.so001.order;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRenderer  extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		
		super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		if (s==null) {
			if (value instanceof Integer)//整数
				setHorizontalAlignment(SwingConstants.RIGHT);
			else if (value instanceof Double)//小数
				setHorizontalAlignment(SwingConstants.RIGHT);
			else if (value instanceof Float)//小数
				setHorizontalAlignment(SwingConstants.RIGHT);
			else
				setHorizontalAlignment(SwingConstants.LEFT);
		}else {
			if (s.equals(SwingConstants.RIGHT)) {
				if (value instanceof Integer)//整数
					setHorizontalAlignment(SwingConstants.RIGHT);
				else if (value instanceof Double)//小数
					setHorizontalAlignment(SwingConstants.RIGHT);
				else if (value instanceof Float)//小数
					setHorizontalAlignment(SwingConstants.RIGHT);
				else
					setHorizontalAlignment(SwingConstants.RIGHT);
			}else if (s.equals(SwingConstants.LEFT)) {
				if (value instanceof Integer)//整数
					setHorizontalAlignment(SwingConstants.LEFT);
				else if (value instanceof Double)//小数
					setHorizontalAlignment(SwingConstants.LEFT);
				else if (value instanceof Float)//小数
					setHorizontalAlignment(SwingConstants.LEFT);
				else
					setHorizontalAlignment(SwingConstants.LEFT);
				
			}
		}

		return this;
	}
	public CellRenderer(){
			setHorizontalAlignment(SwingConstants.RIGHT);
		
	}
	public CellRenderer(Integer s){
		this.s=s;
		setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	
	Integer s =null;

}
