/*
 * 创建日期 2004-11-2
 */
package com.ufsoft.report;

import java.awt.Color;

import com.ufsoft.table.TableStyle;

/**
 * 记录页面显示风格的信息.
 * 分为两类：静态值供全局共用；非静态值当前报表独用。
 * @author wupeng
 * @version 3.1
 */
public class ReportStyle implements Cloneable {
	/**
	 * 是否显示行标签
	 */
	private boolean m_bShowRowHeader = true;
	/**
	 * 是否显示列标签
	 */
	private boolean m_bShowColHeader = true;
	/**
	 * 网格线的颜色
	 */
	private Color m_cGrid = TableStyle.GRID_COLOR;
	/**
	 * 页面显示的百分比
	 */
	private int m_nPercent = 100;
	/**
	 * 是否显示0.
	 */
	private static boolean m_bShowZero = true;
    /**参照型数据是否显示ID值*/
    private static boolean m_bShowRefID = false;

	/**
	 * @return 返回是否显示行标签。
	 */
	public boolean isShowColHeader() {
		return m_bShowColHeader;
	}
	/**
	 * @param showColHeader 要设置的是否显示行标签。
	 */
	public void setShowColHeader(boolean showColHeader) {
		m_bShowColHeader = showColHeader;
	}
	/**
	 * @return 返回 是否显示列标签。
	 */
	public boolean isShowRowHeader() {
		return m_bShowRowHeader;
	}
	/**
	 * @param showRowHeader 要设置的 是否显示列标签。
	 */
	public void setShowRowHeader(boolean showRowHeader) {
		m_bShowRowHeader = showRowHeader;
	}
	/**
	 * @return 返回 是否显示0。
	 */
	public static boolean isShowZero() {
		return m_bShowZero;
	}
	/**
	 * @param showZero 要设置的 是否显示0。
	 */
	public static void setShowZero(boolean showZero) {
		m_bShowZero = showZero;
	}
	/**
	 * @return 返回 网格线的颜色。
	 */
	public Color getGrid() {
		return m_cGrid;
	}
	/**
	 * @param grid 要设置的 网格线的颜色。
	 */
	public void setGrid(Color grid) {
		m_cGrid = grid;
	}
	/**
	 * @return 返回 页面显示的百分比。
	 */
	public int getPercent() {
		return m_nPercent;
	}
	/**
	 * @param percent 要设置的 页面显示的百分比。
	 */
	public void setPercent(int percent) {
		m_nPercent = percent;
	}
    /**
     * @return 返回 m_bShowRefID。
     */
    public static boolean isShowRefID() {
        return m_bShowRefID;
    }
    /**
     * @param showRefID 要设置的 m_bShowRefID。
     */
    public static void setShowRefID(boolean showRefID) {
        m_bShowRefID = showRefID;
    }
}
