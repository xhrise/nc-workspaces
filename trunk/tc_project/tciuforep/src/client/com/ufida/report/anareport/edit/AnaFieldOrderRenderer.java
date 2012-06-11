package com.ufida.report.anareport.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufida.report.anareport.model.AnaRepField;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * �����ֶ���Ⱦ�������Ƶ�����ʵ�������Σ�����ָ�������εױߵĳ��Ⱥ͵׽ǵĽǶ�
 * @author wangyga
 *
 */
public class AnaFieldOrderRenderer implements SheetCellRenderer {

	/**��������������X��������*/
	private int[] TriangleXValues = new int[3];
	/**��������������Y��������*/
	private int[] TriangleYValues = new int[3];
	/**���������ε׽ǽǶ�*/
	private final int ALPHA = 60;
	/**���������εױ߳���*/
	private final int BASE_LINE = 10;
	/**�����ξ൥Ԫ�ұ��ߵ�ƫ����*/
	private final int XOFFSET = 2;
	/**�����ɫ*/
	private Color fillColor = Color.BLUE;
	/**ƫ����*/
	private int offset = (int) (BASE_LINE / 2 * Math.tan(ALPHA * Math.PI / 180));

	public AnaFieldOrderRenderer() {

	}

	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {

		if(cell == null||!(cellsPane.getOperationState() == ReportContextKey.OPERATION_FORMAT)){
			return null;
		}
		AnaRepField fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		if(fld == null){
			return null;
		}
		return new OrderComponent(fld.getOrderType());
	}

	private class OrderComponent extends JComponent {
		private static final long serialVersionUID = 7978685698106740558L;

		/**��������*/
		private int iOrderType = AnaRepField.ORDERTYPE_NONE;

		public OrderComponent(int iOrderType) {
			setOrderType(iOrderType);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Rectangle rect = this.getBounds();
			drawTriangle(getOrderType(), g, rect);
		}

		/**
		 * �����������ͻ���
		 * @param iOrderType:��������
		 * @param g
		 * @param rect
		 */
		private void drawTriangle(int iOrderType, Graphics g, Rectangle rect) {

			if (g == null || rect == null)
				return;
			int iWidth = rect.width;
			int iHeight = rect.height;
			int iMidpointX = iWidth - XOFFSET - BASE_LINE;

			if (getOrderType() == AnaRepField.ORDERTYPE_NONE) {
				return;
			} else if (getOrderType() == AnaRepField.ORDERTYPE_ASCENDING) {
				int iMidpointY = iHeight / 2 + offset / 2;

				TriangleXValues[0] = iMidpointX - BASE_LINE / 2;
				TriangleYValues[0] = iMidpointY;
				TriangleXValues[1] = iMidpointX + BASE_LINE / 2;
				TriangleYValues[1] = iMidpointY;
				TriangleXValues[2] = iMidpointX;
				TriangleYValues[2] = iMidpointY - offset;

			} else if (getOrderType() == AnaRepField.ORDERTYPE_DESCENDING) {
				int iMidpointY = iHeight / 2 - offset / 2;

				TriangleXValues[0] = iMidpointX - BASE_LINE / 2;
				TriangleYValues[0] = iMidpointY;
				TriangleXValues[1] = iMidpointX + BASE_LINE / 2;
				TriangleYValues[1] = iMidpointY;
				TriangleXValues[2] = iMidpointX;
				TriangleYValues[2] = iMidpointY + offset;
			}
			Color preColor = g.getColor();
			g.setColor(fillColor);
			g.fillPolygon(TriangleXValues, TriangleYValues, 3);
			g.setColor(preColor);
		}

		/**
		 * 
		 * @return
		 */
		private int getOrderType() {
			return this.iOrderType;
		}

		/**
		 * 
		 * @param orderType
		 */
		private void setOrderType(int orderType) {
			this.iOrderType = orderType;
		}

	}

}
