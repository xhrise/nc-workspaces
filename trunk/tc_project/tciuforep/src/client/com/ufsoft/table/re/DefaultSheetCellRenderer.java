package com.ufsoft.table.re;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.TablePane;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.beans.UFOLabel;
import com.ufsoft.table.format.ConditionFormat;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.FontFactory;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * <p>
 * Title: 缺省的组件渲染器
 * </p>
 * <p>
 * Description: 组件模仿DefaultTableCellRender，只是将其中的JTable修改为CellsPane
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1
 */

public class DefaultSheetCellRenderer // extends nc.ui.pub.beans.UILabel
		implements SheetCellRenderer, Serializable {
	static final long serialVersionUID = 3270266275923094363L;
	/**
	 * <code>noFocusBorder</code> 选中是单元的边界
	 */
	protected static Border noFocusBorder = new EmptyBorder(3,3,3,3);
	private JWrapLabel m_wrapRender = new JWrapLabel();
	private UFOLabel m_unWrapRender = new com.ufsoft.table.beans.UFOLabel();
	private UFOLabel render = m_unWrapRender;

	public DefaultSheetCellRenderer() {
		super();

		m_wrapRender.setOpaque(false);
		m_wrapRender.setBorder(noFocusBorder);

		m_unWrapRender.setOpaque(false);
		m_unWrapRender.setBorder(noFocusBorder);

	}

	// /**
	// * Overridden for performance reasons.
	// * See the <a href="#override">Implementation Note</a>
	// * for more information.
	// * @param propertyName
	// * @param oldValue
	// * @param newValue
	// */
	// protected void firePropertyChange(String propertyName, Object oldValue,
	// Object newValue) {
	// // Strings get interned...
	// if (propertyName == "text") {
	// super.firePropertyChange(propertyName, oldValue, newValue);
	// }
	// }
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 * 
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
	}

	/**
	 * add by guogang 2007-7-2 对条件格式的处理 modify by guogang 2007-10-17 增加对多条件的处理
	 * 
	 * @return 满足多条件中的第一个条件
	 */
	@SuppressWarnings("unchecked")
	public static Format checkCondition(Cell cell, Double value) {
		ArrayList<ConditionFormat> allCondition = null;
		ConditionFormat condition = null;
		Format newFormat;
		if (cell != null && cell.getExtFmt("ConditionFormat") != null)
			allCondition = (ArrayList<ConditionFormat>) cell.getExtFmt("ConditionFormat");
		if (allCondition != null && !allCondition.isEmpty()) {
			for (int i = 0; i < allCondition.size(); i++) {
				condition = allCondition.get(i);
				newFormat = condition.checkCondition(value);
				if (newFormat != null) {
					return newFormat;
				}
			}

		}
		return null;
	}

	/**
	 * 实现父类接口
	 * 
	 * @see com.ufsoft.table.re.SheetCellRenderer#getCellRendererComponent(com.ufsoft.table.CellsPane,
	 *      java.lang.Object, boolean, boolean, int, int) modify by guogang
	 *      处理换行和缩小字体同时选择时由于render是JWrapLabel出现的错误
	 *      先处理缩小字体，如果字体处理后已经是最小字体但是还是无法显示完全，就进行换行处理 modify by guogang
	 *      保证每次进入该方法的时候render为默认的m_unWrapRender,主要为了解决如果有多个单元要缩小字体的时候防止第一次缩小字体后由于同时设置了折行操作使render变为m_wrapRender
	 *      而getCellShrinkFontSize()方法中只有在render==m_unWrapRender重新计算字号的
	 */
	public Component getCellRendererComponent(CellsPane table, Object obj, boolean isSelected, boolean hasFocus,
			int row, int column, Cell cell) {
		if (table == null) {
			return null;
		}
		render = m_unWrapRender;
		 
		// 为这个组件设置字体，边框，前景色
		Object value = cell == null ? null : cell.getValue();
		Format format = table.getDataModel().getRealFormat(CellPosition.getInstance(row, column));
		// add by guogang 2007-7-2 增加条件格式的处理
//		boolean isCondition = false;
		if (format != null && format.isCondition() && value != null) {
			Format tmp = null;
			if (((IufoFormat) format).getCellType() == TableConstant.CELLTYPE_NUMBER) {
				if (value.toString() != null && value.toString().length() > 0){
					try{
						Double dValue = Double.valueOf(value.toString());
						tmp = checkCondition(cell, dValue);
					}catch(NumberFormatException ex){
						AppDebug.debug(ex);
					}
				}
				if (tmp != null) {
					format = tmp;
//					isCondition = true;
				}
			}
		}
		// add end

		// 为这个组件设置字体
		Font font = null;
		String fontName = DefaultFormatValue.FONTNAME;
		int fontSize = DefaultFormatValue.FONT_SIZE;
		int fontStyle = DefaultFormatValue.FONT_STYLE;
		render.setShrink(false);
		if (format != null) {
			fontName = format.getFontname();
			if (fontName == null) {
				fontName = DefaultFormatValue.FONTNAME;
			}
			fontStyle = format.getFontstyle();
			if (fontStyle == TableConstant.UNDEFINED) {
				fontStyle = DefaultFormatValue.FONT_STYLE;
			}
			fontSize = format.getFontsize();
			if (fontSize == TableConstant.UNDEFINED) {
				fontSize = DefaultFormatValue.FONT_SIZE;
			}
			render.setShrink(format.isShrink());
//			// add by guogang 2007-6-7 如果支持缩小字体填充，则调整到合适的字号
//			if (format.isShrink() && obj != null && (obj instanceof String)) {
//				int newFontSize = getCellShrinkFontSize(table, (String) obj, row, column, fontName, fontStyle, fontSize);
//				fontSize = newFontSize;
//				// format.setFontSize(newFontSize);
//			}
			// add end
		}
		// 先调整字体再处理换行
		if (format != null && format.isFold() && !(obj instanceof Double)) {
			render = m_wrapRender;
		} else {
			render = m_unWrapRender;
		}
		// 需要扩大Font的尺寸.
		fontSize = (int) (fontSize * TablePane.getViewScale());
		font = FontFactory.createFont(fontName, fontStyle, fontSize);
		render.setFont(font);
		// liuyy.
		// // 前景颜色和背景颜色

		Color foreGround = format == null || format.getForegroundColor() == null ? table.getForeground() : format
				.getForegroundColor();
		if (isSelected) { // 设置选择的效果
			int foreColor = foreGround.getRGB();
			int sColor = TableStyle.SELECTION_BACKGROUND.getRGB();
			foreColor = foreColor != sColor ? foreColor ^ ~sColor : foreColor;
			foreGround = new Color(foreColor);
		}
		render.setForeground(foreGround);
		// 处理条件格式的背景色 // @edit by wangyga at 2009-5-22,上午11:02:03
//		Color backGround = format == null ? null : format.getBackgroundColor();
//		if (backGround != null && isCondition) {
//			if (isSelected) {
//				int backColor = backGround.getRGB();
//				int sColor = TableStyle.SELECTION_BACKGROUND.getRGB();
//				backColor = backColor != sColor ? backColor ^ ~sColor : backColor;
//				backGround = new Color(backColor);
//			}
//			render.setOpaque(true);
//			render.setBackground(backGround);
//		} else {
			render.setOpaque(false);
//		}

		// 设置对齐:浮点型、整型和日期类型缺省居右显示
		if (format == null || format.getHalign() == TableConstant.UNDEFINED) {
			render.setHorizontalAlignment(DefaultFormatValue.HALIGN);
			if (obj != null
					&& (obj instanceof Double || obj instanceof Integer || (format != null
							&& format.getHalign() == TableConstant.UNDEFINED && format
							.getCellType() == TableConstant.CELLTYPE_NUMBER))) {
				render.setHorizontalAlignment(TableConstant.HOR_RIGHT);
			} else if (obj != null && obj instanceof String && format != null
					&& format.getHalign() == TableConstant.UNDEFINED
					&& format.getCellType() == TableConstant.CELLTYPE_DATE) {
				render.setHorizontalAlignment(TableConstant.HOR_RIGHT);
			}
		} else {
			render.setHorizontalAlignment(format.getHalign());
		}
		render
				.setVerticalAlignment(format == null
						|| format.getValign() == TableConstant.UNDEFINED ? DefaultFormatValue.VALIGN
						: format.getValign());
		render.setToolTipText("fd");
		// 需要把设置值放到最后，因为IUFORender类中的decorateValue方法中会改变前景色。
		// 同时添加render参数，因为当使用的是WrapRender时，decorateValue中使用this设置前景色无效。
		String strContent = decorateValue(render, value, format);
		render.setText(strContent);
		return render;
	}

	//
	public String getToolTipText(MouseEvent e) {
		System.out.println("has been called");
		return "anywhere" + e.getPoint();
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public boolean isOpaque() {
		Color back = render.getBackground();
		Component p = render.getParent();
		if (p != null) {
			p = p.getParent();
		}
		boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground()) && p.isOpaque();
		return !colorMatch && render.isOpaque();
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void repaint(long tm, int x, int y, int width, int height) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void repaint(Rectangle r) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void revalidate() {
	}

	/**
	 * See the <a href="#override">Implementation Note</a>
	 */
	public void validate() {
	}

	public JLabel getRenderComp() {
		return render;
	}

	/**
	 * 修饰显示值
	 * 
	 * @param render
	 * @param value
	 *            内容
	 * @param format
	 *            格式
	 * @return 修饰过的内容。
	 */
	protected String decorateValue(Component render, Object value, Format format) {
		return value == null ? "" : value.toString();
	}

	/**
	 * add by guogang 2007-6-7 增加对单元格缩小字体填充的支持
	 * 
	 * @param value
	 * @param row
	 * @param column
	 * @return
	 * 
	 */
	public int getCellShrinkFontSize(CellsPane table, String value, int row, int column, String fontName,
			int fontStyle, int fontSize) {
		String[] fontSizes = DefaultSetting.fontSizes;
		Font font = FontFactory.createFont(fontName, fontStyle, fontSize);
		int cWidth = table.getDataModel().getColumnHeaderModel().getHeader(column).getSize();
		render.setText(value);
		render.setFont(font);

		int vWidth = cWidth;
		if (render == m_unWrapRender)
			vWidth = (int) render.getPreferredSize().getWidth();

		if (vWidth > cWidth) {
			fontSize = cWidth * fontSize / vWidth;
		}
		if (fontSize < Integer.parseInt(fontSizes[0]))
			fontSize = Integer.parseInt(fontSizes[0]);
		if (fontSize > Integer.parseInt(fontSizes[fontSizes.length - 1]))
			fontSize = Integer.parseInt(fontSizes[fontSizes.length - 1]);
		return fontSize;
	}
}