package com.ufsoft.table.re;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;

/**
 * <p>Title: 缺省的组件编辑器.这个编辑器主要是实现JTextfield,JComboBox,JCheckBox的录入方式,
 * 与JTable中的编辑器控制方式一致. </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class DefaultSheetCellEditor implements SheetCellEditor {

	/**
	 * <code>listenerList</code> 监听器列表
	 */
	protected EventListenerList listenerList = new EventListenerList();

	/**
	 * <code>changeEvent</code> 编辑事件
	 */
	protected ChangeEvent changeEvent = null;
	/**
	 * 编辑组件
	 */
	protected JComponent editorComponent;
	/**
	 * <code>delegate</code> 编辑功能的代理
	 */
	protected EditorDelegate delegate;
	/**
	 * 鼠标点击激活编辑的次数
	 */
	protected int clickCountToStart = 2;

	/**
	 * 代理类.子类中实现对于不同类型编辑器的处理
	 * @author wupeng
	 * @version 3.1
	 */
	protected class EditorDelegate
			implements
				ActionListener,
				ItemListener,
				Serializable {
	     
		private static final long serialVersionUID = 4371149241887912137L;
		/**
		 * <code>value</code> 编辑内容
		 */
		protected Object value;

		/**
		 * 得到单元数据
		 * @return Object
		 */
		public Object getCellEditorValue() {
			return value;
		}

		/** 
		 * 设置单元数据
		 * @param x Object
		 */
		public void setValue(Object x) {
			value = x;
		}

		/** 
		 * 编辑已经开始
		 * @param anEvent EventObject
		 * @return boolean
		 */
		public boolean startCellEditing(EventObject anEvent) {
			return true;
		}
		/** 
		 * 返回true,表明编辑时需要选择单元.
		 * @param anEvent EventObject
		 * @return boolean
		 */
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}
		/**
		 *事件是否可以激活编辑
		 * @param anEvent
		 * @return boolean
		 */
		public boolean isCellEditable(EventObject anEvent) {
			if (anEvent instanceof MouseEvent) {
				return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
			}
			return true;
		}

		/** 
		 * 编辑已经结束
		 * @return boolean
		 */
		public boolean stopCellEditing() {
			fireEditingStopped();
			return true;
		}

		/** 
		 * 中止编辑
		 *
		 */
		public void cancelCellEditing() {
		}

		/**
		 * 对回车的事件响应，先结束编辑，再设置新焦点.
		 * @param e ActionEvent
		 * modify by guogang 2007-11-13
		 */
		public void actionPerformed(ActionEvent e) {
			//编辑事件结束的时候会移除编辑组件
			CellsPane cp=null;
			if(editorComponent.getParent() instanceof CellsPane){
				cp=(CellsPane)editorComponent.getParent();
			}
			DefaultSheetCellEditor.this.stopCellEditing();
			
            if(cp!=null){
                cp.getSelectionModel().setAnchorCell((CellPosition) cp.getSelectionModel().getAnchorCell().getMoveArea(1,0));
            }
		}

		/**
		 * 事件响应后,结束编辑.
		 * @param e ItemEvent
		 */
		public void itemStateChanged(ItemEvent e) {
			DefaultSheetCellEditor.this.stopCellEditing();
		}
	}

	//**********************************************
	//           监听器处理
	//************************************************

	/**
	 * 注册监听器
	 * @param l CellEditorListener
	 */
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}
	/**
	 * 删除监听器.
	 * @param l CellEditorListener
	 */
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);
	}
	/**
	 * 取消编辑过程
	 */
	public void cancelCellEditing() {
		fireEditingCanceled();
	}
	/**
	 * 编辑过程被取消
	 * @see EventListenerList
	 */
	protected void fireEditingCanceled() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((CellEditorListener) listeners[i + 1])
						.editingCanceled(changeEvent);
			}
		}
	}
	/**
	 * 编辑过程终止
	 * @see EventListenerList
	 */
	protected void fireEditingStopped() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((CellEditorListener) listeners[i + 1])
						.editingStopped(changeEvent);
			}
		}
	}
	/**
	 * 获取编辑器的值
	 * @return Object
	 */
	public Object getCellEditorValue() {
		return delegate.getCellEditorValue();
	}
    /**
     * 设置编辑器的值
     * @param value void
     */
    public void setValue(Object value) {
        delegate.setValue(value);        
    }
	/**
	 * 得到编辑组件的参照.
	 * @return the editor Component
	 */
	public Component getComponent() {
		return editorComponent;
	}

	/**
	 *事件是否可以激活编辑
	 * @param anEvent
	 * @return boolean
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return delegate.isCellEditable(anEvent);
	}

	/** 
	 * 返回true,表明编辑时需要选择单元.
	 * @param anEvent EventObject
	 * @return boolean
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return delegate.shouldSelectCell(anEvent);
	}
	/**
	 * 终止编辑
	 * @return boolean
	 */
	public boolean stopCellEditing() {
		return delegate.stopCellEditing();

	}

	/**
	 * 构建使用checkBox的编辑器 
	 * @param checkBox JCheckBox
	 */
	public DefaultSheetCellEditor(final JCheckBox checkBox) {
		editorComponent = checkBox;
		delegate = new EditorDelegate() {
			public void setValue(Object value) {
				boolean selected = false;
				if (value instanceof Boolean) {
					selected = ((Boolean) value).booleanValue();
				}
				checkBox.setSelected(selected);
			}
			public Object getCellEditorValue() {
				return checkBox.isSelected()?Boolean.TRUE:Boolean.FALSE;
			}
		};
		
		checkBox.addActionListener(delegate);
	}

	/**
	 * 构建使用comboBox的编辑器.
	 * @param comboBox
	 *
	 */
	public DefaultSheetCellEditor(final JComboBox comboBox ) {
		editorComponent = comboBox;
		delegate = new EditorDelegate() {
			private static final long serialVersionUID = 1L;
			public void setValue(Object value) {
				comboBox.setSelectedItem(value);
			}
			public Object getCellEditorValue() {
				return comboBox.getSelectedItem();
			}

			public boolean shouldSelectCell(EventObject anEvent) {
				if (anEvent instanceof MouseEvent) {
					MouseEvent e = (MouseEvent) anEvent;
					return e.getID() != MouseEvent.MOUSE_DRAGGED;
				}
				return true;
			}
		};
		
		comboBox.addActionListener(delegate);
	}
	/**
	 * 构建使用TextField的构建器
	 * @param textField
	 *
	 */
	public DefaultSheetCellEditor(final JTextField textField) {
		editorComponent = textField;
		
		delegate = new EditorDelegate() {
			private static final long serialVersionUID = 1L;

			public void setValue(Object newValue) {
				textField.setText((newValue != null) ? newValue.toString() : "");
			}

			public Object getCellEditorValue() {
				return textField.getText();
			}
		};
		textField.addActionListener(delegate);
	}
	/**
	 *子类继承时，必须实例化editorComponent和delegate。
	 */
	protected DefaultSheetCellEditor() {
	    
	}
    public DefaultSheetCellEditor(final JLabel label) {
        editorComponent = label;
        delegate = new EditorDelegate(){
			private static final long serialVersionUID = 1L;

			public void setValue(Object newValue) {
                label.setText((newValue != null) ? newValue.toString() : "");
            }

            public Object getCellEditorValue() {
                return label.getText();
            }
        };
    }

	/**
	 * 得到编辑器
	 * @param table 表格组件
	 * @param value 单元的原值
	 * @param isSelected 单元是否被选中
	 * @param row 所处行
	 * @param column 所处列
	 * @return Component 编辑组件
	 * 
	 */
	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		setValue(value);
		return editorComponent;
	}
    /**
	 *  @see com.ufsoft.table.re.SheetCellEditor#getEditorPRI()
	 */
	public int getEditorPRI() {
		return 0;
	}
	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
		return true;
	}
}