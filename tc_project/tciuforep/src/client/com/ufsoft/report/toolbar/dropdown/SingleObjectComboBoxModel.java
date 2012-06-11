package com.ufsoft.report.toolbar.dropdown;

import javax.swing.*;

/**
 *
 * <p>Description: 只有一个值的ComboBoxModel</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: UFIDA</p>
 *
 * @author <a href="mailto:guogang@ufida.com.cn">guogang</a>
 * @version 1.0
 */
public class SingleObjectComboBoxModel extends AbstractListModel implements ComboBoxModel{
    private int propertyValue;
    private Object swatch;
    
    /**
     * @see ListModel.getElementAt()
     */
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return swatch;
	}
	/**
	 * @see ListModel.getSize()
	 */
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}
	/**
	 *@see ComboBoxModel.getSelectedItem()
	 */
	public Object getSelectedItem() {
		// TODO Auto-generated method stub
		return swatch;
	}
	/**
	 * @see ComboBoxModel.setSelectedItem() 主要要重载的方法
	 * 
	 */
	public void setSelectedItem(Object anItem) {
		// TODO Auto-generated method stub
		swatch=anItem;
	}
    
}
