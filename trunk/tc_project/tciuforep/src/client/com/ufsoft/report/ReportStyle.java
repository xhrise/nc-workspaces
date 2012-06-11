/*
 * �������� 2004-11-2
 */
package com.ufsoft.report;

import java.awt.Color;

import com.ufsoft.table.TableStyle;

/**
 * ��¼ҳ����ʾ������Ϣ.
 * ��Ϊ���ࣺ��ֵ̬��ȫ�ֹ��ã��Ǿ�ֵ̬��ǰ������á�
 * @author wupeng
 * @version 3.1
 */
public class ReportStyle implements Cloneable {
	/**
	 * �Ƿ���ʾ�б�ǩ
	 */
	private boolean m_bShowRowHeader = true;
	/**
	 * �Ƿ���ʾ�б�ǩ
	 */
	private boolean m_bShowColHeader = true;
	/**
	 * �����ߵ���ɫ
	 */
	private Color m_cGrid = TableStyle.GRID_COLOR;
	/**
	 * ҳ����ʾ�İٷֱ�
	 */
	private int m_nPercent = 100;
	/**
	 * �Ƿ���ʾ0.
	 */
	private static boolean m_bShowZero = true;
    /**�����������Ƿ���ʾIDֵ*/
    private static boolean m_bShowRefID = false;

	/**
	 * @return �����Ƿ���ʾ�б�ǩ��
	 */
	public boolean isShowColHeader() {
		return m_bShowColHeader;
	}
	/**
	 * @param showColHeader Ҫ���õ��Ƿ���ʾ�б�ǩ��
	 */
	public void setShowColHeader(boolean showColHeader) {
		m_bShowColHeader = showColHeader;
	}
	/**
	 * @return ���� �Ƿ���ʾ�б�ǩ��
	 */
	public boolean isShowRowHeader() {
		return m_bShowRowHeader;
	}
	/**
	 * @param showRowHeader Ҫ���õ� �Ƿ���ʾ�б�ǩ��
	 */
	public void setShowRowHeader(boolean showRowHeader) {
		m_bShowRowHeader = showRowHeader;
	}
	/**
	 * @return ���� �Ƿ���ʾ0��
	 */
	public static boolean isShowZero() {
		return m_bShowZero;
	}
	/**
	 * @param showZero Ҫ���õ� �Ƿ���ʾ0��
	 */
	public static void setShowZero(boolean showZero) {
		m_bShowZero = showZero;
	}
	/**
	 * @return ���� �����ߵ���ɫ��
	 */
	public Color getGrid() {
		return m_cGrid;
	}
	/**
	 * @param grid Ҫ���õ� �����ߵ���ɫ��
	 */
	public void setGrid(Color grid) {
		m_cGrid = grid;
	}
	/**
	 * @return ���� ҳ����ʾ�İٷֱȡ�
	 */
	public int getPercent() {
		return m_nPercent;
	}
	/**
	 * @param percent Ҫ���õ� ҳ����ʾ�İٷֱȡ�
	 */
	public void setPercent(int percent) {
		m_nPercent = percent;
	}
    /**
     * @return ���� m_bShowRefID��
     */
    public static boolean isShowRefID() {
        return m_bShowRefID;
    }
    /**
     * @param showRefID Ҫ���õ� m_bShowRefID��
     */
    public static void setShowRefID(boolean showRefID) {
        m_bShowRefID = showRefID;
    }
}
