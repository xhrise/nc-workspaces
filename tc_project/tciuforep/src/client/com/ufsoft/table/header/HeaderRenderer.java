package com.ufsoft.table.header;

import java.awt.Color;


/**
 * 行头列头渲染器
 * @author chxw
 * @date 2008-03-17
 */
public class HeaderRenderer extends com.ufsoft.table.beans.UFOLabel {
	static final long serialVersionUID = 1650320375023852243L;

	private boolean isSelected = false;
	
	public HeaderRenderer() {
		super();
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
//		if(isSelected){
//			this.setForeground(Color.WHITE);
//		} else {
//			this.setForeground(Color.BLACK);
//		}
	}
	
}
