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
 * Title:单元的渲染器和编辑器的管理类
 * </p>
 * <p>
 * Description: 负责管理单元的渲染器和编辑器，如果用户希望自己定义在表格容纳的组件，
 * 控制该组件的显示和编辑，那么首先需要实现SheetCellRenderer和SheetCellEditor来定义
 * 自己的渲染器和编辑器，然后调用该类的注册方法注册。可以参考DefaultSheetCellRenderer
 * </p>
 */
public class CellRenderAndEditor implements Serializable {
	static final long serialVersionUID = -4011877819084280597L;
	/** 记录渲染器的映射关系 */
	private Hashtable<Object, SheetCellRenderer> m_hashRenders = new Hashtable<Object, SheetCellRenderer>();

	/** 记录编辑器的映射关系 */
	private Hashtable<Object, SheetCellEditor> m_hashEditors = new Hashtable<Object, SheetCellEditor>();

	// 2006.1.12添加注册扩展绘制器和编辑器的方法，对这些编辑器和绘制器，每次绘制都去检查。
	private List<SheetCellRenderer> extSheetCellRenderer = new Vector<SheetCellRenderer>();
	
	private List<SheetCellEditor> extSheetCellEditor = new Vector<SheetCellEditor>();
	
	/**
	 * 缺省的渲染器
	 */
	private final static  SheetCellRenderer DEFAULT_RENDERER = new DefaultSheetCellRenderer();
	/**
	 * 不可读单元的渲染器
	 */
	private final static SheetCellRenderer UNREADERABLE_RENDERER = new UnreadableSheetCellRenderer();;

	private final static  SheetCellRenderer BACKGROUND_RENDERER = new BackgroundRenderer();

	/***
	 * 全局编辑器和绘制器
	 * 绘制时将此全局设置和CellsPane里面的个性化设置整合使用
	 */
	private static CellRenderAndEditor m_renderAndEditor = null;
	

	/**
	 * 构造器。
	 */
	private CellRenderAndEditor() {
		super();
		
	}
	
	/**
	 * 全局绘制器与编辑器
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
	 * 缺省的编辑器
	 */
	public SheetCellEditor getDefaultEditor(){
		return new GenericEditor(new JTextField()); 
	}
	/**
	 * 得到渲染器
	 * 
	 * @param c
	 *            对于扩展数据 c - 扩展数据类型字符串。 对于value值，c - value实际对象。
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
	 * 得到不可读单元的构造器。
	 * 
	 * @return SheetCellRenderer
	 */
	public static SheetCellRenderer getUnreadableRenderer() {
		return UNREADERABLE_RENDERER;
	}

	/**
	 * 得到编辑器。需要保证每一种数据类型和扩展类型都有对应的编辑器。
	 * 
	 * @param c
	 *            单元实例
	 * @return SheetCellEditor
	 * 
	 *         changed by ll,
	 *         2008-06-19,原来的代码逻辑有问题，有扩展属性时，永远拿不到format和callValue对应的编辑器
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public SheetCellEditor getEditor(CellsModel cellsModel,
			CellPosition cellPos){
		
		Cell c = cellsModel.getCell(cellPos);
		
		SheetCellEditor editor = null;

		if (c != null) {
			if (c.getExtFmtSize() > 0) {// 如果存在扩展格式。
				String[] extNames = c.getExtNames();
				for (int i = 0; i < extNames.length; i++) {
					SheetCellEditor aEditor = (SheetCellEditor) m_hashEditors
							.get(extNames[i]);// 扩展数据按类型描述的字符串，得到编辑器	
					try {//由于静态的维护了编辑器组件，会导致一些问题，此出再重新实例
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
			if (c.getFormat() != null) {// 根据单元格的数据类型
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
							try {//由于静态的维护了编辑器组件，会导致一些问题，此出再重新实例
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
			if (c.getValue() != null) {// 否则，取基本数据的对应的编辑器。
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
		if (editor == null) {// 如果为空，说明有数据类型没有对应的编辑器。需提出警告否？
			editor = getDefaultEditor();
		}
		
		//liuyy+ ,2009-04-27 不用此步。CellsPane中getCellEditor（）会对所有扩展编辑器进行过滤。
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
	 * V56这样处理
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

	/** 记录每种格式对应的数据类型 */
	private Class[] m_FormatClasses = null;

	/**
	 * 选择编辑器的规则是： 首先检查扩展格式，得到优先级别最高的扩展格式选择。 然后检查单元的值，按照已经设置的值选择
	 * 如果值为空，但是单元格式设置了容纳数据的类型，按照数据的类型选择
	 * 
	 * @param dataType
	 * @param className
	 */
	public void setFormatEditor(int dataType, Class className) {
		if (dataType < 0) {
			throw new IllegalArgumentException();
		}
		// 保证数组的长度
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
	 * 注册渲染器
	 * 
	 * @param c
	 *            Class - 单元类型
	 * @param render
	 *            - 渲染器
	 */
	public static void registRender(Object c, SheetCellRenderer render) {
		if (c != null) {
			getInstance().m_hashRenders.put(c, render);
		}
	}

	/**
	 * 注册编辑器
	 * 
	 * @param c
	 *            Class - 单元类型
	 * @param editor
	 *            - 编辑器
	 */
	public static void registEditor(Object c, SheetCellEditor editor) {
		if (c != null) {
			getInstance().m_hashEditors.put(c, editor);
		}
	}

	private void init() {
		// 注册控件绘制器和控件渲染器。
		registRender(String.class, DEFAULT_RENDERER);
//		registEditor(String.class, DEFAULT_EDITOR);

		// registRender(Boolean.class, new BooleanRenderer());
		registRender(Boolean.class, DEFAULT_RENDERER);
		registEditor(Boolean.class, getCheckBoxEditor());

		registRender(ImageIcon.class, new IconRenderer());
		registEditor(ImageIcon.class, new EmptyEditor());// todo 暂时没有图标编辑器。

		registRender(Double.class, new DoubleRender());
		registEditor(Double.class, new DoubleEditor());
        //@edit by guogang 2009-1-16
		registRender(Integer.class, new IntegerRender());
		registEditor(Integer.class, new IntegerEditor());
		

		registRender(Date.class, new StringRender());
		// registEditor(Date.class, new DateEditor());//modify by wangyga
		// 2008-7-1 日期类型当作普通字符处理
//		registEditor(Date.class, DEFAULT_EDITOR);

		registRender(SelectedArea.class, new SelectedAreaRender());

		setFormatEditor(TableConstant.CELLTYPE_NUMBER, Double.class);
		setFormatEditor(TableConstant.CELLTYPE_STRING, String.class);
		setFormatEditor(TableConstant.CELLTYPE_DATE, Date.class);
	}

	// **********************以下代码为某些编辑器和渲染器的实现*****************************

	/**
	 * 不可读单元的渲染起
	 * 
	 * @author wupeng
	 * @version 3.1
	 */
	private static class UnreadableSheetCellRenderer extends DefaultSheetCellRenderer {

		private static final long serialVersionUID = -4041672735121934224L;

		/**
		 * 重载父类方法。对于不可读的单元显示红色的*号。
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
	 * 图形文件的渲染器
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
		 * 实现父类接口
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
			// 为这个组件设置字体，边框，前景色
			Object value = cell == null ? null : cell.getValue();

			JLabel lbl = getRenderComp();
			// lbl.setSize(width, height);
			ImageIcon icon = (value instanceof ImageIcon) ? (ImageIcon) value
					: null;
			// 04-11 图片设置宽高后绘制有严重效率问题.关闭此功能
			// 05-28,根据cellsModel中的开关来调用这个功能
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

			// 前景颜色和背景颜色
			Color backGround = format == null
					|| format.getBackgroundColor() == null ? table
					.getBackground() : format.getBackgroundColor();
			Color foreGround = format == null
					|| format.getForegroundColor() == null ? table
					.getForeground() : format.getForegroundColor();
			if (isSelected) { // 设置选择的效果
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
			
			// @edit by wangyga at 2009-5-22,上午10:36:26 条件格式的背景色处理放到绘制背景时处理，不要放在值的绘制器里面，会把前面的绘制效果覆盖
			
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
	 * 得到CheckBox类型的编辑器.
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