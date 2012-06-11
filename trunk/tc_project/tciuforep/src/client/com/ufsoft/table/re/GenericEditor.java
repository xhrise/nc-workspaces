/*
 * 创建日期 2004-11-9
 */
package com.ufsoft.table.re;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Constructor;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.ufsoft.table.CellsPane;

/**
 * 缺省的编辑器.提供jtextfield的编辑方式.
 */
public class GenericEditor extends DefaultSheetCellEditor {

	private Class[] argTypes = new Class[]{String.class};
	private Constructor constructor;
	private Object value;

	/**
	 * @param tf
	 *  
	 */
	public GenericEditor(JTextField tf) {
		super(tf);
	}
	/**
	 * 参见父类
	 * 
	 * @param table
	 * @param value Object 该数据如果不是String或者Object类型,必须有以String为参数的构造函数;
	 *            该单元显示的编辑内容,为该对象toString的结果
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return Component
	 *  
	 */
	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		//如果原来的数据为空或者是Class类型的数据,那么构建一个String类型的构造器;否则构造一个参数为String的该类型的构造器.
		this.value = null;
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		try {
			Class type = null;
			if (value == null) {
				type = String.class;
			} else {
				type = value.getClass();
				if (type == Object.class) {
					type = String.class;
				}
			}
			constructor = type.getConstructor(argTypes);
		} catch (Exception e) {
			return null;
		}
		return super.getTableCellEditorComponent(table, value, isSelected, row,
				column);
	}
	/**
	 * 终止编辑. 如果constructor非空,那么以输入框中的String为构造参数构造返回值.
	 * 
	 * @return boolean
	 *  
	 */
	public boolean stopCellEditing() {
		//zzl添加下面三行.***********************
		if (constructor == null) {
			return true;
		}
		String s = (String) super.getCellEditorValue();
		if ("".equals(s)) {
			if (constructor.getDeclaringClass() == String.class || constructor.getDeclaringClass() == Double.class) {
				value = null;
				return super.stopCellEditing();
			}			
		}

		try {
			value = constructor.newInstance(new Object[]{s});
		} catch (Exception e) {
			((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
			return false;
		}
		//zzl添加下面三行.
		finally {
			constructor = null;
		}
		return super.stopCellEditing();
	}
	/**
	 * 返回编辑结果
	 * 
	 * @return Object
	 */
	public Object getCellEditorValue() {
		return value;
	}
}