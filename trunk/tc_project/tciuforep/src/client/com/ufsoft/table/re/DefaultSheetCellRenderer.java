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
 * Title: ȱʡ�������Ⱦ��
 * </p>
 * <p>
 * Description: ���ģ��DefaultTableCellRender��ֻ�ǽ����е�JTable�޸�ΪCellsPane
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
	 * <code>noFocusBorder</code> ѡ���ǵ�Ԫ�ı߽�
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
	 * add by guogang 2007-7-2 ��������ʽ�Ĵ��� modify by guogang 2007-10-17 ���ӶԶ������Ĵ���
	 * 
	 * @return ����������еĵ�һ������
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
	 * ʵ�ָ���ӿ�
	 * 
	 * @see com.ufsoft.table.re.SheetCellRenderer#getCellRendererComponent(com.ufsoft.table.CellsPane,
	 *      java.lang.Object, boolean, boolean, int, int) modify by guogang
	 *      �����к���С����ͬʱѡ��ʱ����render��JWrapLabel���ֵĴ���
	 *      �ȴ�����С���壬������崦����Ѿ�����С���嵫�ǻ����޷���ʾ��ȫ���ͽ��л��д��� modify by guogang
	 *      ��֤ÿ�ν���÷�����ʱ��renderΪĬ�ϵ�m_unWrapRender,��ҪΪ�˽������ж����ԪҪ��С�����ʱ���ֹ��һ����С���������ͬʱ���������в���ʹrender��Ϊm_wrapRender
	 *      ��getCellShrinkFontSize()������ֻ����render==m_unWrapRender���¼����ֺŵ�
	 */
	public Component getCellRendererComponent(CellsPane table, Object obj, boolean isSelected, boolean hasFocus,
			int row, int column, Cell cell) {
		if (table == null) {
			return null;
		}
		render = m_unWrapRender;
		 
		// Ϊ�������������壬�߿�ǰ��ɫ
		Object value = cell == null ? null : cell.getValue();
		Format format = table.getDataModel().getRealFormat(CellPosition.getInstance(row, column));
		// add by guogang 2007-7-2 ����������ʽ�Ĵ���
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

		// Ϊ��������������
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
//			// add by guogang 2007-6-7 ���֧����С������䣬����������ʵ��ֺ�
//			if (format.isShrink() && obj != null && (obj instanceof String)) {
//				int newFontSize = getCellShrinkFontSize(table, (String) obj, row, column, fontName, fontStyle, fontSize);
//				fontSize = newFontSize;
//				// format.setFontSize(newFontSize);
//			}
			// add end
		}
		// �ȵ��������ٴ�����
		if (format != null && format.isFold() && !(obj instanceof Double)) {
			render = m_wrapRender;
		} else {
			render = m_unWrapRender;
		}
		// ��Ҫ����Font�ĳߴ�.
		fontSize = (int) (fontSize * TablePane.getViewScale());
		font = FontFactory.createFont(fontName, fontStyle, fontSize);
		render.setFont(font);
		// liuyy.
		// // ǰ����ɫ�ͱ�����ɫ

		Color foreGround = format == null || format.getForegroundColor() == null ? table.getForeground() : format
				.getForegroundColor();
		if (isSelected) { // ����ѡ���Ч��
			int foreColor = foreGround.getRGB();
			int sColor = TableStyle.SELECTION_BACKGROUND.getRGB();
			foreColor = foreColor != sColor ? foreColor ^ ~sColor : foreColor;
			foreGround = new Color(foreColor);
		}
		render.setForeground(foreGround);
		// ����������ʽ�ı���ɫ // @edit by wangyga at 2009-5-22,����11:02:03
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

		// ���ö���:�����͡����ͺ���������ȱʡ������ʾ
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
		// ��Ҫ������ֵ�ŵ������ΪIUFORender���е�decorateValue�����л�ı�ǰ��ɫ��
		// ͬʱ���render��������Ϊ��ʹ�õ���WrapRenderʱ��decorateValue��ʹ��this����ǰ��ɫ��Ч��
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
	 * ������ʾֵ
	 * 
	 * @param render
	 * @param value
	 *            ����
	 * @param format
	 *            ��ʽ
	 * @return ���ι������ݡ�
	 */
	protected String decorateValue(Component render, Object value, Format format) {
		return value == null ? "" : value.toString();
	}

	/**
	 * add by guogang 2007-6-7 ���ӶԵ�Ԫ����С��������֧��
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