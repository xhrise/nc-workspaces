package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.fmtplugin.formula.FormulaRenderer;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.TablePane;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.FontFactory;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
/**
 * 公式定义时的渲染器，重构于V55以前的<code>FormulaRender</code>
 * @author zhaopq
 * @created at 2009-4-9,上午10:08:11
 * @sincev5.6
 */
public class FormulaDefRenderer extends FormulaRenderer { 
	
	/** 是普通公式还是汇总公式*/
	private boolean isInstantFml;
	
	/** 格式态下公式扩展属性是否显示 */
	private static boolean m_bFmlRendererVisible = true;
	
	private JLabel lblFmlComp = new nc.ui.pub.beans.UILabel();// 普通公式显示组件.

	// 区域公式显示组件.
	private static final JLabel lblAFmlComp = new nc.ui.pub.beans.UILabel() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = getBounds(); 
			int width = rect.width;
			int height = rect.height;
			int fontHeight = g.getFontMetrics().getAscent();

			Color preColor = g.getColor();
			g.setColor(Color.BLUE);
			g.drawString("f", width - width + 1,
					(height / 2 + (fontHeight / 2)));
			g.setColor(new Color(0, 128, 0));
			g.drawString("a", width - width + 5,
					(height / 2 + (fontHeight / 2)));
			g.setColor(preColor);
		}
	};

	// 个性化公式显示组件.
	private static final JLabel lblPFmlComp = new nc.ui.pub.beans.UILabel() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = getBounds();
			int width = rect.width;
			int height = rect.height;
			int fontHeight = g.getFontMetrics().getAscent();
			Color preColor = g.getColor();
			g.setColor(Color.BLUE);

			g.drawString("f", width - width + 1,
					(height / 2 + (fontHeight / 2)));
			g.setColor(new Color(0, 128, 0));
			g.drawString("m", width - width + 5,
					(height / 2 + (fontHeight / 2)));
			g.setColor(preColor);
		}
	};

	// 汇总公式显示组件.
	private static final JLabel lblSFmlComp = new nc.ui.pub.beans.UILabel() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = getBounds();
			int width = rect.width;
			int height = rect.height;
			int fontHeight = g.getFontMetrics().getAscent();
			Color preColor = g.getColor();
			g.setColor(Color.BLUE);
			g.drawString("f", width - width + 17,
					(height / 2 + (fontHeight / 2)));
			g.setColor(Color.RED);
			g.drawString("s", width - width + 20,
					(height / 2 + (fontHeight / 2)));
			g.setColor(preColor);
		}
	};

	// 错误公式显示组件.
	private static final JLabel lblErrFmlComp = new nc.ui.pub.beans.UILabel() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = getBounds();
			int width = rect.width;
			int height = rect.height;
			Color preColor = g.getColor();
			g.setColor(Color.red);
			g.drawString(StringResource.getStringResource("miufo1000713"),
					width - 50, (height - height / 2 + 5));
			g.setColor(preColor);
		}
	};

	public FormulaDefRenderer(boolean isInstantFml) {
		super();
		this.isInstantFml = isInstantFml;
	}

	/**
	 * @i18n miufo1000713=错误公式
	 * @i18n miufo1001801=我的公式
	 * @i18n miufo1001802=区域公式
	 * @i18n miufo1000909=单元公式
	 * 
	 * CellsPane统一控制，数据态将不会调用此Renderer 
	 * 
	 */
	public Component getCellRendererComponent(CellsPane cellsPane,   Object obj,
    		boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
    	if(!m_bFmlRendererVisible) return null;
    	
    	//edit by wangyga 此版暂时这样处理
     	if(cellsPane.getOperationState() != ReportContextKey.OPERATION_FORMAT){
			return null;
		}
     	
    	CellPosition cellPos = CellPosition.getInstance(row,column);
    	// @edit by wangyga at 2009-3-3,上午09:49:03
    	FormulaModel formulaModel = FormulaModel.getInstance(cellsPane.getDataModel());
    	Object[] objs = formulaModel.getRelatedFmlVO(cellPos, isInstantFml);
        Object pos = objs[0];
        FormulaVO fmlVO = (FormulaVO) objs[1];
        if(fmlVO == null) return null;
        boolean isAreaFml = !((IArea)pos).isCell();
        
        Component rendererComp = lblFmlComp;
    	if(isInstantFml){// 单元公式
			// 设置即时公式显示内容
    	    if(fmlVO.isErrorFml()){
    	    	rendererComp = lblErrFmlComp;
    	    }else{
    	    	FormulaVO fmlPerson = formulaModel.getPersonalDirectFml((IArea)pos);
    	    	if(fmlPerson != null && fmlPerson.getContextId() != null)
    	    		rendererComp = lblPFmlComp;
    	    	else if(fmlPerson == null){
    	    		if(isAreaFml){
    	    			rendererComp = lblAFmlComp;
    	    	    } else{
    	    	    	rendererComp = lblCFmlComp;
    	    	    }
    	    	} else{
    	    		lblFmlComp.setText(null);
    	    	}
    	    		
    	    }
    	    // 设置即时公式显示格式
    	    setDisFormatToInstantFmlComp(cellsPane, row, column, isSelected);
    	    return rendererComp;
    	}else{// 汇总公式.
    	    return lblSFmlComp;    		
    	}
    }

	/**
	 * 设置即时公式显示格式
	 * 
	 * @param cellsPane
	 * @param row
	 * @param column
	 * @param isSelected
	 */
	private void setDisFormatToInstantFmlComp(CellsPane cellsPane, int row,
			int column, boolean isSelected) {
		// 为这个组件设置字体
		Font font = null;
		String fontName = DefaultFormatValue.FONTNAME;
		int fontSize = DefaultFormatValue.FONT_SIZE;
		int fontStyle = DefaultFormatValue.FONT_STYLE;

		// 为这个组件设置字体，边框
		Format format = cellsPane.getDataModel().getRealFormat(
				CellPosition.getInstance(row, column));
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
		}

		// 需要扩大Font的尺寸.
		fontSize = (int) (fontSize * TablePane.getViewScale());
		font = FontFactory.createFont(fontName, fontStyle, fontSize);
		lblFmlComp.setFont(font);

		// 设置前景色
		Color foreGround = format == null
				|| format.getForegroundColor() == null ? cellsPane
				.getForeground() : format.getForegroundColor();
		if (isSelected) { // 设置选择的效果
			int foreColor = foreGround.getRGB();
			int sColor = TableStyle.SELECTION_BACKGROUND.getRGB();
			foreColor = foreColor != sColor ? foreColor ^ ~sColor : foreColor;
			foreGround = new Color(foreColor);
		}
		lblFmlComp.setForeground(foreGround);

		// 设置对齐:浮点型、整型和日期类型缺省居右显示
		if (format == null || format.getHalign() == TableConstant.UNDEFINED) {
			lblFmlComp.setHorizontalAlignment(DefaultFormatValue.HALIGN);
			if (format != null
					&& format.getHalign() == TableConstant.UNDEFINED
					&& (format.getCellType() == TableConstant.CELLTYPE_NUMBER || format
							.getCellType() == TableConstant.CELLTYPE_DATE)) {
				lblFmlComp.setHorizontalAlignment(TableConstant.HOR_RIGHT);
			}
		} else {
			lblFmlComp.setHorizontalAlignment(format.getHalign());
		}

		lblFmlComp
				.setVerticalAlignment(format == null
						|| format.getValign() == TableConstant.UNDEFINED ? DefaultFormatValue.VALIGN
						: format.getValign());
	}

	public static boolean isFmlRendererVisible() {
		return m_bFmlRendererVisible;
	}

	public static void setFmlRendererVisible(boolean fmlRendererVisible) {
		m_bFmlRendererVisible = fmlRendererVisible;
	}
}