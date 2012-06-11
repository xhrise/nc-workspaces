package com.ufsoft.table.re;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.Serializable;
import java.util.Date;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;

import nc.ui.pub.beans.UICheckBox;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SelectedArea;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * <p>
 * Title:��Ԫ����Ⱦ���ͱ༭���Ĺ�����
 * </p>
 * <p>
 * Description: �������Ԫ����Ⱦ���ͱ༭��������û�ϣ���Լ������ڱ�����ɵ������
 * ���Ƹ��������ʾ�ͱ༭����ô������Ҫʵ��SheetCellRenderer��SheetCellEditor������
 * �Լ�����Ⱦ���ͱ༭����Ȼ����ø����ע�᷽��ע�ᡣ���Բο�DefaultSheetCellRenderer
 * </p>
 */
public class CellRenderAndEditor implements Serializable {
	static final long serialVersionUID = -4011877819084280597L;
	/** ��¼��Ⱦ����ӳ���ϵ */
	private Hashtable<Object, SheetCellRenderer> m_hashRenders = new Hashtable<Object, SheetCellRenderer>();

	/** ��¼�༭����ӳ���ϵ */
	private Hashtable<Object, SheetCellEditor> m_hashEditors = new Hashtable<Object, SheetCellEditor>();

	// 2006.1.12���ע����չ�������ͱ༭���ķ���������Щ�༭���ͻ�������ÿ�λ��ƶ�ȥ��顣
	private List<SheetCellRenderer> extSheetCellRenderer = new Vector<SheetCellRenderer>();
	
	private List<SheetCellEditor> extSheetCellEditor = new Vector<SheetCellEditor>();
	
	/**
	 * ȱʡ����Ⱦ��
	 */
	private final static  SheetCellRenderer DEFAULT_RENDERER = new DefaultSheetCellRenderer();
	/**
	 * ���ɶ���Ԫ����Ⱦ��
	 */
	private final static SheetCellRenderer UNREADERABLE_RENDERER = new UnreadableSheetCellRenderer();;

	private final static  SheetCellRenderer BACKGROUND_RENDERER = new BackgroundRenderer();

	/***
	 * ȫ�ֱ༭���ͻ�����
	 * ����ʱ����ȫ�����ú�CellsPane����ĸ��Ի���������ʹ��
	 */
	private static CellRenderAndEditor m_renderAndEditor = null;
	

	/**
	 * ��������
	 */
	private CellRenderAndEditor() {
		super();
		
	}
	
	/**
	 * ȫ�ֻ�������༭��
	 * @return
	 */
	public static CellRenderAndEditor getInstance(){
		if(m_renderAndEditor == null){
			m_renderAndEditor = new CellRenderAndEditor();
			m_renderAndEditor.init();
		}
		return m_renderAndEditor;
	}


	/**
	 * ȱʡ�ı༭��
	 */
	public SheetCellEditor getDefaultEditor(){
		return new GenericEditor(new JTextField()); 
	}
	/**
	 * �õ���Ⱦ��
	 * 
	 * @param c
	 *            ������չ���� c - ��չ���������ַ����� ����valueֵ��c - valueʵ�ʶ���
	 * @return TableCellRenderer
	 */
	public SheetCellRenderer getRender(Object c) {
		SheetCellRenderer render = null;
		if (c != null) {
			render = (SheetCellRenderer) m_hashRenders.get(c);
		} else {
			render = DEFAULT_RENDERER;
		}
		if (render == null) {
			render = new SheetCellRenderer() {
				public Component getCellRendererComponent(CellsPane cellsPane,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column, Cell cell) {
					return null;
				}
			};
		}
		return render;
	}

	public static SheetCellRenderer getBackgroundRenderer() {
		return BACKGROUND_RENDERER;
	}

	/**
	 * �õ����ɶ���Ԫ�Ĺ�������
	 * 
	 * @return SheetCellRenderer
	 */
	public static SheetCellRenderer getUnreadableRenderer() {
		return UNREADERABLE_RENDERER;
	}

	/**
	 * �õ��༭������Ҫ��֤ÿһ���������ͺ���չ���Ͷ��ж�Ӧ�ı༭����
	 * 
	 * @param c
	 *            ��Ԫʵ��
	 * @return SheetCellEditor
	 * 
	 *         changed by ll,
	 *         2008-06-19,ԭ���Ĵ����߼������⣬����չ����ʱ����Զ�ò���format��callValue��Ӧ�ı༭��
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public SheetCellEditor getEditor(CellsModel cellsModel,
			CellPosition cellPos){
		
		Cell c = cellsModel.getCell(cellPos);
		
		SheetCellEditor editor = null;

		if (c != null) {
			if (c.getExtFmtSize() > 0) {// ���������չ��ʽ��
				String[] extNames = c.getExtNames();
				for (int i = 0; i < extNames.length; i++) {
					SheetCellEditor aEditor = (SheetCellEditor) m_hashEditors
							.get(extNames[i]);// ��չ���ݰ������������ַ������õ��༭��	
					try {//���ھ�̬��ά���˱༭��������ᵼ��һЩ���⣬�˳�������ʵ��
						if(aEditor != null){
							Class<?> clz = Class.forName(aEditor.getClass().getName());
							aEditor = (SheetCellEditor) clz.newInstance();
						}
					} catch (Throwable e) {
						AppDebug.debug("Error!! SheetCellEditor create failture:" + aEditor.getClass().getName());
					}
					if (aEditor != null
							&& (editor == null || editor.getEditorPRI() < aEditor
									.getEditorPRI())) {
						editor = aEditor;
					}
				}
			}
			if (c.getFormat() != null) {// ���ݵ�Ԫ�����������
				Format fmt = c.getFormat();
				if (fmt != null) {
					int cellType = fmt.getCellType();
//					SheetCellEditor aEditor = getEditorByDataType(cellType);
					if (m_FormatClasses != null
							&& cellType < m_FormatClasses.length
							&& cellType >= 0) {
						if (m_FormatClasses[cellType] != null) {
							SheetCellEditor aEditor = (SheetCellEditor) m_hashEditors
									.get(m_FormatClasses[cellType]);
							try {//���ھ�̬��ά���˱༭��������ᵼ��һЩ���⣬�˳�������ʵ��
								if(aEditor != null){
									Class<?> clz = Class.forName(aEditor.getClass().getName());
									aEditor = (SheetCellEditor) clz.newInstance();
								}
							} catch (Throwable e) {
								AppDebug.debug("Error!! SheetCellEditor create failture:" + aEditor.getClass().getName());
							}
							if (aEditor != null
									&& (editor == null || editor.getEditorPRI() < aEditor
											.getEditorPRI())) {
								editor = aEditor;
							}
						}
					}
				}
			}
			if (c.getValue() != null) {// ����ȡ�������ݵĶ�Ӧ�ı༭����
				SheetCellEditor aEditor = (SheetCellEditor) m_hashEditors.get(c
						.getValue().getClass());
				// liuyy. 2007-03-22
				if (aEditor != null && aEditor instanceof EmptyEditor) {
					return null;
				}
				if (aEditor != null
						&& (editor == null || editor.getEditorPRI() < aEditor
								.getEditorPRI())) {
					editor = aEditor;
				}
			}
		}
		if (editor == null) {// ���Ϊ�գ�˵������������û�ж�Ӧ�ı༭��������������
			editor = getDefaultEditor();
		}
		
		//liuyy+ ,2009-04-27 ���ô˲���CellsPane��getCellEditor�������������չ�༭�����й��ˡ�
//		if(oper == ReportContextKey.OPERATION_INPUT){
//			return editor;
//		}
//		SheetCellEditor[] arrExtSheetEditors = getExtSheetEditor();
//		for (int i = 0; i < arrExtSheetEditors.length; i++) {
//			SheetCellEditor aEditor = arrExtSheetEditors[i];
//			if (aEditor != null && editor.getEditorPRI() < aEditor.getEditorPRI()
//					&& aEditor.isEnabled(cellsModel, cellPos)
//					) {
//				editor = aEditor;
//			}
//		}

		return editor;
	}

	/**
	 * V56��������
	 * @param dataType
	 * @return
	 */
	private SheetCellEditor getEditorByDataType(int dataType){
		switch (dataType) {
		case TableConstant.CELLTYPE_NUMBER:
			return new DoubleEditor();
		default:
			return getDefaultEditor();
		}
	}

	/** ��¼ÿ�ָ�ʽ��Ӧ���������� */
	private Class[] m_FormatClasses = null;

	/**
	 * ѡ��༭���Ĺ����ǣ� ���ȼ����չ��ʽ���õ����ȼ�����ߵ���չ��ʽѡ�� Ȼ���鵥Ԫ��ֵ�������Ѿ����õ�ֵѡ��
	 * ���ֵΪ�գ����ǵ�Ԫ��ʽ�������������ݵ����ͣ��������ݵ�����ѡ��
	 * 
	 * @param dataType
	 * @param className
	 */
	public void setFormatEditor(int dataType, Class className) {
		if (dataType < 0) {
			throw new IllegalArgumentException();
		}
		// ��֤����ĳ���
		if (m_FormatClasses == null) {
			m_FormatClasses = new Class[dataType + 1];
		} else if (dataType >= m_FormatClasses.length) {
			Class[] newArray = new Class[dataType + 1];
			System.arraycopy(m_FormatClasses, 0, newArray, 0,
					m_FormatClasses.length);
			m_FormatClasses = newArray;
		}
		m_FormatClasses[dataType] = className;
	}

	/**
	 * ע����Ⱦ��
	 * 
	 * @param c
	 *            Class - ��Ԫ����
	 * @param render
	 *            - ��Ⱦ��
	 */
	public static void registRender(Object c, SheetCellRenderer render) {
		if (c != null) {
			getInstance().m_hashRenders.put(c, render);
		}
	}

	/**
	 * ע��༭��
	 * 
	 * @param c
	 *            Class - ��Ԫ����
	 * @param editor
	 *            - �༭��
	 */
	public static void registEditor(Object c, SheetCellEditor editor) {
		if (c != null) {
			getInstance().m_hashEditors.put(c, editor);
		}
	}

	private void init() {
		// ע��ؼ��������Ϳؼ���Ⱦ����
		registRender(String.class, DEFAULT_RENDERER);
//		registEditor(String.class, DEFAULT_EDITOR);

		// registRender(Boolean.class, new BooleanRenderer());
		registRender(Boolean.class, DEFAULT_RENDERER);
		registEditor(Boolean.class, getCheckBoxEditor());

		registRender(ImageIcon.class, new IconRenderer());
		registEditor(ImageIcon.class, new EmptyEditor());// todo ��ʱû��ͼ��༭����

		registRender(Double.class, new DoubleRender());
		registEditor(Double.class, new DoubleEditor());
        //@edit by guogang 2009-1-16
		registRender(Integer.class, new IntegerRender());
		registEditor(Integer.class, new IntegerEditor());
		

		registRender(Date.class, new StringRender());
		// registEditor(Date.class, new DateEditor());//modify by wangyga
		// 2008-7-1 �������͵�����ͨ�ַ�����
//		registEditor(Date.class, DEFAULT_EDITOR);

		registRender(SelectedArea.class, new SelectedAreaRender());

		setFormatEditor(TableConstant.CELLTYPE_NUMBER, Double.class);
		setFormatEditor(TableConstant.CELLTYPE_STRING, String.class);
		setFormatEditor(TableConstant.CELLTYPE_DATE, Date.class);
	}

	// **********************���´���ΪĳЩ�༭������Ⱦ����ʵ��*****************************

	/**
	 * ���ɶ���Ԫ����Ⱦ��
	 * 
	 * @author wupeng
	 * @version 3.1
	 */
	private static class UnreadableSheetCellRenderer extends DefaultSheetCellRenderer {

		private static final long serialVersionUID = -4041672735121934224L;

		/**
		 * ���ظ��෽�������ڲ��ɶ��ĵ�Ԫ��ʾ��ɫ��*�š�
		 * 
		 * @param table
		 * @param cell
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param column
		 * @return Component
		 */
		public Component getCellRendererComponent(CellsPane table, Object obj,
				boolean isSelected, boolean hasFocus, int row, int column,
				Cell cell) {
			super.getCellRendererComponent(table, obj, isSelected, hasFocus,
					row, column, cell);
			getRenderComp().setForeground(Color.red);
			getRenderComp().setHorizontalAlignment(SwingConstants.CENTER);
			getRenderComp().setVerticalAlignment(SwingConstants.CENTER);
			getRenderComp().setText("***");
			return getRenderComp();

		}

	}

	/**
	 * ͼ���ļ�����Ⱦ��
	 * 
	 * @author wupeng
	 * @version 3.1
	 */
	private static class IconRenderer extends DefaultSheetCellRenderer {

		private static final long serialVersionUID = 5830221233269467923L;

		public IconRenderer() {
			super();
			getRenderComp().setHorizontalAlignment(JLabel.CENTER);
		}

		/**
		 * ʵ�ָ���ӿ�
		 * 
		 * @see com.ufsoft.table.re.SheetCellRenderer#getCellRendererComponent(com.ufsoft.table.CellsPane,
		 *      java.lang.Object, boolean, boolean, int, int)
		 * 
		 */
		public Component getCellRendererComponent(CellsPane table, Object obj,
				boolean isSelected, boolean hasFocus, int row, int column,
				Cell cell) {

			CellsModel cellsModel = table.getDataModel();
			CellPosition pos = CellPosition.getInstance(row, column);
			Format format = cell == null ? null : cellsModel.getRealFormat(pos);
			Color backGround = format == null
					|| format.getBackgroundColor() == null ? table
					.getBackground() : format.getBackgroundColor();
			// Ϊ�������������壬�߿�ǰ��ɫ
			Object value = cell == null ? null : cell.getValue();

			JLabel lbl = getRenderComp();
			// lbl.setSize(width, height);
			ImageIcon icon = (value instanceof ImageIcon) ? (ImageIcon) value
					: null;
			// 04-11 ͼƬ���ÿ�ߺ����������Ч������.�رմ˹���
			// 05-28,����cellsModel�еĿ����������������
			if (cellsModel.isExpandImage()) {
				if (icon != null) {
					Image img = icon.getImage();
					if (img != null) {
						// liuyy. 2007-2-1
						IArea area = table.getDataModel().getArea(pos);
						int row1 = area.getStart().getRow();
						int row2 = area.getEnd().getRow();
						int col1 = area.getStart().getColumn();
						int col2 = area.getEnd().getColumn();

						// Rectangle cellRect = table.getCellRect(area, false);
						// cellRect.getWidth();
						int width = 0;
						int height = 0;
						for (int i = row1; i <= row2; i++) {
							height += cellsModel.getRowHeaderModel().getSize(i);
						}
						for (int i = col1; i <= col2; i++) {
							width += cellsModel.getColumnHeaderModel().getSize(
									i);
						}
						img = img.getScaledInstance(width, height,
								Image.SCALE_SMOOTH);
						icon.setImage(img);
					}
				}
			}
			lbl.setIcon(icon);
			lbl.setBackground(backGround);
			return lbl;
		}
	}

	private static class BooleanRenderer extends nc.ui.pub.beans.UICheckBox implements
			SheetCellRenderer {
		private static final long serialVersionUID = -8279253060740646200L;

		public BooleanRenderer() {
			super();
			setHorizontalAlignment(JLabel.CENTER);
		}

		public Component getCellRendererComponent(CellsPane table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column, Cell cell) {

			if (isSelected) {
				super.setBackground(TableStyle.SELECTION_BACKGROUND);
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			// setSelected( (value != null && ( (Boolean)
			// value).booleanValue()));
			return this;
		}
	}

	private static class BackgroundRenderer extends JLabel implements SheetCellRenderer {

		private static final long serialVersionUID = 868652617965440951L;

		public BackgroundRenderer() {
			super();
		}

		public Component getCellRendererComponent(CellsPane table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column, Cell cell) {

			Format format = table.getDataModel().getRealFormat(
					CellPosition.getInstance(row, column));

			// ǰ����ɫ�ͱ�����ɫ
			Color backGround = format == null
					|| format.getBackgroundColor() == null ? table
					.getBackground() : format.getBackgroundColor();
			Color foreGround = format == null
					|| format.getForegroundColor() == null ? table
					.getForeground() : format.getForegroundColor();
			if (isSelected) { // ����ѡ���Ч��
				int foreColor = foreGround.getRGB();
				int backColor = backGround.getRGB();
				int sColor = TableStyle.SELECTION_BACKGROUND.getRGB();
				foreColor = foreColor != sColor ? foreColor ^ ~sColor
						: foreColor;
				if (!hasFocus) {
					backColor = backColor != sColor ? backColor ^ ~sColor
							: backColor;
				}
				backGround = new Color(backColor);
				foreGround = new Color(foreColor);

			}

			this.setBackground(backGround);
			this.setForeground(foreGround);

			this.setOpaque(true);
			
			// @edit by wangyga at 2009-5-22,����10:36:26 ������ʽ�ı���ɫ����ŵ����Ʊ���ʱ������Ҫ����ֵ�Ļ��������棬���ǰ��Ļ���Ч������
			
			if (format != null && format.isCondition() && value != null) {
				Format tmp = null;
				if (((IufoFormat) format).getCellType() == TableConstant.CELLTYPE_NUMBER) {
					if (value.toString() != null && value.toString().length() > 0){
						try{
							Double dValue = Double.valueOf(value.toString());
							tmp = DefaultSheetCellRenderer.checkCondition(cell, dValue);
						}catch(NumberFormatException ex){
							AppDebug.debug(ex);
						}
					}
					if (tmp != null) {
						backGround = tmp == null ? null : tmp.getBackgroundColor();
						if(backGround != null){
							if (isSelected) {
								int backColor = backGround.getRGB();
								int sColor = TableStyle.SELECTION_BACKGROUND.getRGB();
								backColor = backColor != sColor ? backColor ^ ~sColor : backColor;
								backGround = new Color(backColor);
							}
							setBackground(backGround);
						}						
					}
				}
			}

			return this;
		}
	}

	/**
	 * �õ�CheckBox���͵ı༭��.
	 * 
	 * @return SheetCellEditor
	 */
	private SheetCellEditor getCheckBoxEditor() {
		JCheckBox box = new UICheckBox();
		box.setHorizontalAlignment(JCheckBox.CENTER);
		return new DefaultSheetCellEditor(box);
	}

	private static class EmptyEditor implements SheetCellEditor {

		public Component getTableCellEditorComponent(CellsPane table,
				Object value, boolean isSelected, int row, int column) {
			return null;
		}

		public int getEditorPRI() {
			return 0;
		}

		public void cancelCellEditing() {

		}

		public boolean stopCellEditing() {
			return false;
		}

		public Object getCellEditorValue() {
			return null;
		}

		public boolean isCellEditable(EventObject anEvent) {
			return false;
		}

		public boolean shouldSelectCell(EventObject anEvent) {
			return false;
		}

		public void addCellEditorListener(CellEditorListener l) {

		}

		public void removeCellEditorListener(CellEditorListener l) {

		}

		public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
			return true;
		}

	}

	public void registExtSheetRenderer(SheetCellRenderer renderer) {
		if (renderer == null) {
			return;
		}
		if(!extSheetCellRenderer.contains(renderer)){
			extSheetCellRenderer.add(renderer);
		} 
	}

	public SheetCellRenderer[] getExtSheetRender() {
		return extSheetCellRenderer.toArray(new SheetCellRenderer[0]);
	}

	public void registExtSheetEditor(SheetCellEditor editor) {
		if (editor == null) {
			return;
		}
		if(!extSheetCellEditor.contains(editor)){
			extSheetCellEditor.add(editor);
		}
	}

	public SheetCellEditor[] getExtSheetEditor() {
		return extSheetCellEditor.toArray(new SheetCellEditor[0]);
	}
}