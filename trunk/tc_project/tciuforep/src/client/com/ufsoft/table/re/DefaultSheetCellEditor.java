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
 * <p>Title: ȱʡ������༭��.����༭����Ҫ��ʵ��JTextfield,JComboBox,JCheckBox��¼�뷽ʽ,
 * ��JTable�еı༭�����Ʒ�ʽһ��. </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class DefaultSheetCellEditor implements SheetCellEditor {

	/**
	 * <code>listenerList</code> �������б�
	 */
	protected EventListenerList listenerList = new EventListenerList();

	/**
	 * <code>changeEvent</code> �༭�¼�
	 */
	protected ChangeEvent changeEvent = null;
	/**
	 * �༭���
	 */
	protected JComponent editorComponent;
	/**
	 * <code>delegate</code> �༭���ܵĴ���
	 */
	protected EditorDelegate delegate;
	/**
	 * ���������༭�Ĵ���
	 */
	protected int clickCountToStart = 2;

	/**
	 * ������.������ʵ�ֶ��ڲ�ͬ���ͱ༭���Ĵ���
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
		 * <code>value</code> �༭����
		 */
		protected Object value;

		/**
		 * �õ���Ԫ����
		 * @return Object
		 */
		public Object getCellEditorValue() {
			return value;
		}

		/** 
		 * ���õ�Ԫ����
		 * @param x Object
		 */
		public void setValue(Object x) {
			value = x;
		}

		/** 
		 * �༭�Ѿ���ʼ
		 * @param anEvent EventObject
		 * @return boolean
		 */
		public boolean startCellEditing(EventObject anEvent) {
			return true;
		}
		/** 
		 * ����true,�����༭ʱ��Ҫѡ��Ԫ.
		 * @param anEvent EventObject
		 * @return boolean
		 */
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}
		/**
		 *�¼��Ƿ���Լ���༭
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
		 * �༭�Ѿ�����
		 * @return boolean
		 */
		public boolean stopCellEditing() {
			fireEditingStopped();
			return true;
		}

		/** 
		 * ��ֹ�༭
		 *
		 */
		public void cancelCellEditing() {
		}

		/**
		 * �Իس����¼���Ӧ���Ƚ����༭���������½���.
		 * @param e ActionEvent
		 * modify by guogang 2007-11-13
		 */
		public void actionPerformed(ActionEvent e) {
			//�༭�¼�������ʱ����Ƴ��༭���
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
		 * �¼���Ӧ��,�����༭.
		 * @param e ItemEvent
		 */
		public void itemStateChanged(ItemEvent e) {
			DefaultSheetCellEditor.this.stopCellEditing();
		}
	}

	//**********************************************
	//           ����������
	//************************************************

	/**
	 * ע�������
	 * @param l CellEditorListener
	 */
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}
	/**
	 * ɾ��������.
	 * @param l CellEditorListener
	 */
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);
	}
	/**
	 * ȡ���༭����
	 */
	public void cancelCellEditing() {
		fireEditingCanceled();
	}
	/**
	 * �༭���̱�ȡ��
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
	 * �༭������ֹ
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
	 * ��ȡ�༭����ֵ
	 * @return Object
	 */
	public Object getCellEditorValue() {
		return delegate.getCellEditorValue();
	}
    /**
     * ���ñ༭����ֵ
     * @param value void
     */
    public void setValue(Object value) {
        delegate.setValue(value);        
    }
	/**
	 * �õ��༭����Ĳ���.
	 * @return the editor Component
	 */
	public Component getComponent() {
		return editorComponent;
	}

	/**
	 *�¼��Ƿ���Լ���༭
	 * @param anEvent
	 * @return boolean
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return delegate.isCellEditable(anEvent);
	}

	/** 
	 * ����true,�����༭ʱ��Ҫѡ��Ԫ.
	 * @param anEvent EventObject
	 * @return boolean
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return delegate.shouldSelectCell(anEvent);
	}
	/**
	 * ��ֹ�༭
	 * @return boolean
	 */
	public boolean stopCellEditing() {
		return delegate.stopCellEditing();

	}

	/**
	 * ����ʹ��checkBox�ı༭�� 
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
	 * ����ʹ��comboBox�ı༭��.
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
	 * ����ʹ��TextField�Ĺ�����
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
	 *����̳�ʱ������ʵ����editorComponent��delegate��
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
	 * �õ��༭��
	 * @param table ������
	 * @param value ��Ԫ��ԭֵ
	 * @param isSelected ��Ԫ�Ƿ�ѡ��
	 * @param row ������
	 * @param column ������
	 * @return Component �༭���
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