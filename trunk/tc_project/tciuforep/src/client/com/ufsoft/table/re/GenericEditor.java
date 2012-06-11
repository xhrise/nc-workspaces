/*
 * �������� 2004-11-9
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
 * ȱʡ�ı༭��.�ṩjtextfield�ı༭��ʽ.
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
	 * �μ�����
	 * 
	 * @param table
	 * @param value Object �������������String����Object����,��������StringΪ�����Ĺ��캯��;
	 *            �õ�Ԫ��ʾ�ı༭����,Ϊ�ö���toString�Ľ��
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return Component
	 *  
	 */
	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		//���ԭ��������Ϊ�ջ�����Class���͵�����,��ô����һ��String���͵Ĺ�����;������һ������ΪString�ĸ����͵Ĺ�����.
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
	 * ��ֹ�༭. ���constructor�ǿ�,��ô��������е�StringΪ����������췵��ֵ.
	 * 
	 * @return boolean
	 *  
	 */
	public boolean stopCellEditing() {
		//zzl�����������.***********************
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
		//zzl�����������.
		finally {
			constructor = null;
		}
		return super.stopCellEditing();
	}
	/**
	 * ���ر༭���
	 * 
	 * @return Object
	 */
	public Object getCellEditorValue() {
		return value;
	}
}