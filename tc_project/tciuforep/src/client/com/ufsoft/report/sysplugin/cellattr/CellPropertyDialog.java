package com.ufsoft.report.sysplugin.cellattr;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.basic.BasicArrowButton;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.dialog.ColorButton;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.IUFOLogger;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.LineFactory;
import com.ufsoft.table.format.TableConstant;

/**
 * ��Ԫ�������öԻ��� ���ڹ��캯������ĸ�ʽ��Ϣ�ڶԻ����лᱻ�޸ģ��û�Ӧ�ÿ�¡��ʽ��Ϣ����Ϊ�������롣�û��ڻ�ȡ��ʽ��Ϣǰ����isModified������
 * ����true��ʾ��ʽ��Ϣȷʵ���û��޸ġ� �������ڣ�(2004-5-8 10:37:13)
 * 
 * @author��Administrator
 */
public class CellPropertyDialog extends UfoDialog
		implements
			java.awt.event.ActionListener,
			java.awt.event.MouseListener {
  
	private static final long serialVersionUID = -9170930342589119315L;

	/** ����Ԥ����Ԫ�߿�����Ч������� */
	private class BorderPanel extends UIPanel {

		private static final long serialVersionUID = -1801854031361155398L;
		//�߿����κ���ɫ���ϡ��¡����ҡ��ᡢ��,�Խ���
		private int[][] border = {
				{TableConstant.UNDEFINED, TableConstant.UNDEFINED},
				{TableConstant.UNDEFINED, TableConstant.UNDEFINED},
				{TableConstant.UNDEFINED, TableConstant.UNDEFINED},
				{TableConstant.UNDEFINED, TableConstant.UNDEFINED},
				{TableConstant.UNDEFINED, TableConstant.UNDEFINED},
				{TableConstant.UNDEFINED, TableConstant.UNDEFINED},
				{TableConstant.UNDEFINED, TableConstant.UNDEFINED},
				{TableConstant.UNDEFINED, TableConstant.UNDEFINED}
				
		};

		/**
		 * ���캯��
		 */
		public BorderPanel() {
			super();
		}
		/**
		 * �õ�ĳ���߿������
		 * 
		 * @param nIndex
		 *            ����λ��
		 * @return ���ԣ����κ���ɫ
		 */
		public int[] getBorderValue(int nIndex) {
			return border[nIndex];
		}
		
		/**
		 * ����Ԥ����Ч��
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D ng = (Graphics2D) g;
			int width = getWidth();
			int height = getHeight();
			//���ս�
			setLineType(ng, 1);
			ng.setColor(Color.lightGray);
			//����
			ng.drawLine(width / 8 - 10, height / 8, width / 8, height / 8);
			ng.drawLine(width / 8, height / 8 - 10, width / 8, height / 8);
			//����
			ng.drawLine(width / 8 * 7, height / 8, width / 8 * 7 + 10,
					height / 8);
			ng.drawLine(width / 8 * 7, height / 8 - 10, width / 8 * 7,
					height / 8);
			//����
			ng.drawLine(width / 8 - 10, height / 8 * 7, width / 8,
					height / 8 * 7);
			ng.drawLine(width / 8, height / 8 * 7, width / 8,
					height / 8 * 7 + 10);
			//����
			ng.drawLine(width / 8 * 7, height / 8 * 7, width / 8 * 7 + 10,
					height / 8 * 7);
			ng.drawLine(width / 8 * 7, height / 8 * 7, width / 8 * 7,
					height / 8 * 7 + 10);
			
			if(!m_bSingleSelected){
				//����
				ng.drawLine(width / 8 - 10, height / 2, width / 8, height / 2);
				ng.drawLine(width / 8, height / 2 - 10, width / 8, height / 2 + 10);
				//����
				ng.drawLine(width / 8 * 7, height / 2, width / 8 * 7 + 10,
						height / 2);
				ng.drawLine(width / 8 * 7, height / 2 - 10, width / 8 * 7,
						height / 2 + 10);
				//����
				ng.drawLine(width / 2 - 10, height / 8, width / 2 + 10, height / 8);
				ng.drawLine(width / 2, height / 8 - 10, width / 2, height / 8);
				//����
				ng.drawLine(width / 2 - 10, height / 8 * 7, width / 2 + 10,
						height / 8 * 7);
				ng.drawLine(width / 2, height / 8 * 7, width / 2,
						height / 8 * 7 + 10);
			
			}	
			
			//�ϱ߿�
			if (border[0][0] != TableConstant.UNDEFINED) {
				setLineType(ng, border[0][0]);
				Color color = border[0][1] == TableConstant.UNDEFINED ? Color.BLACK : new Color(border[0][1]);
				ng.setColor(color);
				ng.drawLine(width / 8, height / 8, width / 8 * 7, height / 8);
			}

			//�±߿�
			if (border[1][0] != TableConstant.UNDEFINED) {
				setLineType(ng, border[1][0]);
				Color color = border[1][1] == TableConstant.UNDEFINED ? Color.BLACK : new Color(border[1][1]);
				ng.setColor(color);
				ng.drawLine(width / 8, height / 8 * 7, width / 8 * 7,
						height / 8 * 7);
			}

			//��߿�
			if (border[2][0] != TableConstant.UNDEFINED) {
				setLineType(ng, border[2][0]);
				Color color = border[2][1] == TableConstant.UNDEFINED ? Color.BLACK : new Color(border[2][1]);
				ng.setColor(color);
				ng.drawLine(width / 8, height / 8, width / 8, height / 8 * 7);
			}

			//�ұ߿�
			if (border[3][0] != TableConstant.UNDEFINED) {
				setLineType(ng, border[3][0]);
				Color color = border[3][1] == TableConstant.UNDEFINED ? Color.BLACK : new Color(border[3][1]);
				ng.setColor(color);
				ng.drawLine(width / 8 * 7, height / 8, width / 8 * 7,
						height / 8 * 7);
			}
			
			if(!m_bSingleSelected){
				//��߿�
				if (border[4][0] != TableConstant.UNDEFINED) {
					setLineType(ng, border[4][0]);
					Color color = border[4][1] == TableConstant.UNDEFINED ? Color.BLACK : new Color(border[4][1]);
					ng.setColor(color);
					ng.drawLine(width / 8, height / 2, width / 8 * 7, height / 2);
				}
				//���߿�
				if (border[5][0] != TableConstant.UNDEFINED) {
					setLineType(ng, border[5][0]);
					Color color = border[5][1] == TableConstant.UNDEFINED ? Color.BLACK : new Color(border[5][1]);
					ng.setColor(color);
					ng.drawLine(width / 2, height / 8, width / 2, height / 8 * 7);
				}
			}
			
			//�Խ���
			if (border[6][0] != TableConstant.UNDEFINED) {
				setLineType(ng, border[6][0]);
				Color color = border[6][1] == TableConstant.UNDEFINED ? Color.BLACK : new Color(border[6][1]);
				ng.setColor(color);
				ng.drawLine(width / 8, height / 8, width / 8 * 7, height / 8 * 7);
			}
			if (border[7][0] != TableConstant.UNDEFINED) {
				setLineType(ng, border[7][0]);
				Color color = border[7][1] == TableConstant.UNDEFINED ? Color.BLACK : new Color(border[7][1]);
				ng.setColor(color);
				ng.drawLine(width / 8 * 7, height / 8, width / 8, height / 8 * 7);
			}			
			
		}
		/**
		 * @param nIndex
		 * @param values
		 */
		public void setBorderValue(int nIndex, int[] values) {
			border[nIndex] = values;
		}
		/**
		 * @param ng
		 * @param nIndex
		 */
		public void setLineType(Graphics2D ng, int nIndex) {
			Stroke bs;
			switch (nIndex) {
				case TableConstant.L_SOLID1 :
				case TableConstant.L_DASH :
				case TableConstant.L_DOT :
				case TableConstant.L_DASHDOT :
				case TableConstant.L_SOLID2 :
				case TableConstant.L_SOLID3 :
				case TableConstant.L_SOLID4 :
					bs = LineFactory.createLineStroke(nIndex);
					ng.setStroke(bs);
					break;
				default :
					break;
			}
		}

	}

	/** ������������ѡ����б������ */
	class LineRender extends nc.ui.pub.beans.UILabel implements ListCellRenderer {
	 
		private static final long serialVersionUID = -8749954928568637770L;

		private LineIcon icon = new LineIcon();

		private Border redBorder = BorderFactory.createLineBorder(Color.black,
				2), emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			setText(" ");
			Object array = value;
			icon.setLineType(Integer.parseInt((String) array));

			setIcon(icon);

			if (isSelected) {
				setBorder(redBorder);
			} else {
				setBorder(emptyBorder);
			}
			return this;

		}
	}

	/** ��������ѡ���б��������һ�У���LineRender����� */
	class LineIcon implements Icon {
		private int linetype;
		private int w, h;
		/**
		 * ���캯��
		 */
		public LineIcon() {
			this(0, 120, 15);
		}
		private LineIcon(int linetype, int w, int h) {
			super();
			this.linetype = linetype;
			this.w = w;
			this.h = h;
		}
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D ng = (Graphics2D) g;
			ng.setColor(Color.black);

			Line2D line;
			Stroke bs;

			switch (linetype) {
				case TableConstant.L_NULL :
					ng.drawString(MultiLang.getString("uiuforep0000733"), x + w / 3,
							y + 10);//��
					break;
				case TableConstant.L_SOLID1 :
				case TableConstant.L_DASH :
				case TableConstant.L_DOT :
				case TableConstant.L_DASHDOT :
				case TableConstant.L_SOLID2 :
				case TableConstant.L_SOLID3 :
				case TableConstant.L_SOLID4 :
					bs = LineFactory.createLineStroke(linetype);
					ng.setStroke(bs);
					line = new Line2D.Float(x + 4, (y + h) / 2, w - 2,
							(y + h) / 2);
					ng.draw(line);
					break;
				default :
					break;
			}
		}

		/**
		 * @param linetype
		 */
		public void setLineType(int linetype) {
			this.linetype = linetype;
		}

		public int getIconWidth() {
			return w;
		}

		public int getIconHeight() {
			return h;
		}
	}

	/** ������ʾSample��ǩ */
	class LineLabel extends nc.ui.pub.beans.UILabel {
	 
		private static final long serialVersionUID = -490697581309498877L;

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			FontMetrics fm = g.getFontMetrics();
			Dimension size = getSize();
			Point point = new Point();
			int sw = fm.stringWidth(getText()), ascent = fm.getAscent(), descent = fm
					.getDescent(), leading = fm.getLeading();

			point.x = size.width / 2 - sw / 2;
			point.y = size.height / 2 + (ascent + descent + leading) / 2;

			int nIndex = getFontTypeComb().getSelectedIndex();
			if (nIndex == 3 || nIndex == 5 || nIndex == 6 || nIndex == 7) {
				Graphics2D g2 = (Graphics2D) g;
				int nFontSize = getFont().getSize();
				float f = nFontSize / 10;
				BasicStroke sLine = new BasicStroke(f);
				g2.setStroke(sLine);
				g2.setColor(getForeground());
				g2.drawLine(point.x, point.y, point.x + sw, point.y);
			}

		}
	}

	/** �������������б������ */
	class MinusRender extends nc.ui.pub.beans.UILabel implements ListCellRenderer {
 
		private static final long serialVersionUID = -810175336955178158L;

		/**
	     * @param list
	     * @param value
	     * @param index
	     * @param isSelected
	     * @param cellHasFocus
	     * @return  Component
	     */
		public java.awt.Component getListCellRendererComponent(
				javax.swing.JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
		    if (index == -1) {
				setOpaque(false);
			} else {
				setOpaque(true);
			}
		    int selValue = ((Integer)value).intValue();
			//��ʾֵ���á�
			if(selValue == TableConstant.MIN_RED1){
			    setText("123.45");
			}else if(selValue == TableConstant.MIN_BLACK1 || selValue == TableConstant.MIN_RED2){
			    setText("-123.45");
			}else if(selValue == TableConstant.MIN_BLACK2 || selValue == TableConstant.MIN_RED3){
			    setText("(123.45)");
			}
			//��ɫ���á�
			if (selValue == TableConstant.MIN_BLACK1 || selValue == TableConstant.MIN_BLACK2) {
				setForeground(UIManager.getColor("ComboBox.foreground"));
			} else {
				setForeground(java.awt.Color.red);
			}
			//ѡ��Ч����
			if (isSelected) {
				setBackground(UIManager
						.getColor("ComboBox.selectionBackground"));
			} else {
				setBackground(UIManager.getColor("ComboBox.background"));
			}
			return this;

		}
	}

	/** ���ͼ�������� */
	class FillCellRenderer extends nc.ui.pub.beans.UILabel implements ListCellRenderer {
 
		private static final long serialVersionUID = 7152459686613982428L;

		private FillCellIcon icon = new FillCellIcon();

		private Border redBorder = BorderFactory.createLineBorder(Color.red, 2),
				emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Object array = value;
			icon.setFillType(Integer.parseInt((String) array));
			setIcon(icon);
			if (isSelected) {
				setBorder(redBorder);
			} else {
				setBorder(emptyBorder);
			}
			return this;

		}
	}

	/** ���ͼ���������е�ͼ�� */
	class FillCellIcon implements Icon {
		private int filltype;

		private int w, h;

		private boolean isCom;

		/**
		 * 
		 */
		public FillCellIcon() {
			this(true, 0, 125, 15);
		}
		private FillCellIcon(boolean isCom, int filltype, int w, int h) {
			this.isCom = isCom;
			this.filltype = filltype;
			this.w = w;
			this.h = h;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D ng = (Graphics2D) g;
			ng.setColor(Color.gray);
			ng.setColor(Color.black);

			Line2D line;
			BasicStroke bs;
			int hNum, wNum, yNum, offset;
			if (isCom) {
				hNum = 2;
				wNum = 10;
				yNum = 1;
				offset = 3;
			} else {
				hNum = 10;
				wNum = 30;
				yNum = 4;
				offset = 0;
				Font labelFont = null;
//				labelFont = new Font(m_strFontName, m_nFontStyle,
//						m_nFontSize + 1);
//				ng.setFont(labelFont);
				ng.drawString("", -40, -40);
				labelFont = new Font(m_strFontName, m_nFontStyle, m_nFontSize);
				ng.setFont(labelFont);
				String strText = MultiLang.getString("uiuforep0000734");//"����Sample";
				FontMetrics fm = g.getFontMetrics();
				int nSw = fm.stringWidth(strText);
				int nAscent = fm.getAscent();
				int nDescent = fm.getDescent();
				int nLeading = fm.getLeading();
				int nSh = nAscent + nDescent + nLeading;
				ng.setColor(m_nForColor);
				//���»���
				int xoff = (380 - nSw) / 2;
				int yoff = (h - nSh) / 2 + m_nFontSize - nDescent;
				ng.drawString(strText, xoff, yoff);

			}

			ng.setColor(Color.black);
			switch (filltype) {
				case TableConstant.FillEmpty :
					break;
				case TableConstant.HORIZONTAL :
					bs = new BasicStroke();
					ng.setStroke(bs);
					for (int i = 0; i <= hNum; i++) {
						line = new Line2D.Float(x + 1, y + h / hNum * i + 4, w,
								y + h / hNum * i + 4);
						ng.draw(line);
					}
					break;
				case TableConstant.VERTICAL :
					bs = new BasicStroke();
					ng.setStroke(bs);
					for (int i = 1; i <= wNum; i++) {
						line = new Line2D.Float(x + w / wNum * i, y + 1, x + w
								/ wNum * i, h);
						ng.draw(line);
					}
					break;
				case TableConstant.CROSS :
					bs = new BasicStroke();
					ng.setStroke(bs);
					for (int i = 0; i <= hNum; i++) {
						line = new Line2D.Float(x + 1, y + h / hNum * i + 4, w,
								y + h / hNum * i + 4);
						ng.draw(line);
					}
					for (int i = 1; i <= wNum; i++) {
						line = new Line2D.Float(x + w / wNum * i, y + 1, x + w
								/ wNum * i, h);
						ng.draw(line);
					}
					break;
				case TableConstant.BDIAGONAL :
					bs = new BasicStroke();
					ng.setStroke(bs);
					for (int m = 0; m < yNum; m++) {
						for (int i = 1; i <= wNum; i++) {
							line = new Line2D.Float(x + w / wNum * i + offset,
									y + h / yNum * m + 1, x + w / wNum
											* (i - 1), y + h / yNum * (m + 1)
											+ 1);
							ng.draw(line);
						}
					}
					break;
				case TableConstant.FDIAGONAL :
					bs = new BasicStroke();
					ng.setStroke(bs);
					for (int m = 0; m < yNum; m++) {
						for (int i = 1; i <= wNum; i++) {
							line = new Line2D.Float(x + w / wNum * (i - 1), y
									+ h / yNum * m + 1, x + w / wNum * i
									+ offset, y + h / yNum * (m + 1) + 1);
							ng.draw(line);
						}
					}
					break;
				case TableConstant.DIAGCROSS :
					bs = new BasicStroke();
					ng.setStroke(bs);
					for (int m = 0; m < yNum; m++) {
						for (int i = 1; i <= wNum; i++) {
							line = new Line2D.Float(x + w / wNum * (i - 1), y
									+ h / yNum * m + 1, x + w / wNum * i
									+ offset, y + h / yNum * (m + 1) + 1);
							ng.draw(line);
						}
						for (int i = 1; i <= wNum; i++) {
							line = new Line2D.Float(x + w / wNum * i + offset,
									y + h / yNum * m + 1, x + w / wNum
											* (i - 1), y + h / yNum * (m + 1)
											+ 1);
							ng.draw(line);
						}
					}
					break;
				default :
					break;
			}

		}

		/**
		 * @param filltype
		 */
		public void setFillType(int filltype) {
			this.filltype = filltype;
		}

		public int getIconWidth() {
			return w;
		}

		public int getIconHeight() {
			return h;
		}
	}

	private JPanel ivjUfoDialogContentPane = null;

	private JLabel ivjJLabel1 = null;

	private JLabel ivjJLabel2 = null;

	private JLabel ivjJLabel3 = null;

	private JLabel ivjJLabel4 = null;
	
	private JLabel ivjJLabel5 = null;

	private JButton ivjcancelButton = null;

	private UITabbedPane ivjCellTabbedPane = null;

	private JLabel ivjJLabelBackground = null;

	private JLabel ivjJLabel6 = null;

	private JLabel ivjJLabel7 = null;

	private JLabel ivjJLabel8 = null;

	private JLabel ivjJLabel9 = null;

	private JLabel ivjJLabel12 = null;

	private JLabel ivjJLabel13 = null;

	private JLabel ivjJLabel14 = null;

	private JLabel ivjJLabel15 = null;

	private JLabel ivjJLabel16 = null;

	private JLabel ivjJLabel17 = null;

	private JPanel ivjAligPanel = null;

	private ColorButton ivjBackColorButton = null;

	private JPanel ivjBorderPagePanel = null;

	private JCheckBox ivjChangeLineCheck = null;
	//add by guogang 2007-6-7
	private JCheckBox ivjShrinkFitCheck=null;

	private JComboBox ivjChineseComb = null;

	private JPanel ivjColorPanel = null;

	private JCheckBox ivjCommaCheck = null;

	private JComboBox ivjCurCombo = null;

	private JTextField ivjDecimalText = null;

	private JComboBox ivjFontNameComb = null;

	private JPanel ivjFontPagePanel = null;

	private JPanel ivjFontPanel = null;

	private JComboBox ivjFontSizeComb = null;

	private JComboBox ivjFontTypeComb = null;

	private ColorButton ivjForeColorButton = null;

	private JRadioButton ivjHAutoRadio = null;

	private JRadioButton ivjHCenterRadio = null;

	private JRadioButton ivjHLeftRadio = null;

	private JRadioButton ivjHRightRadio = null;

	private JButton ivjInnerLineButton = null;

	private ColorButton ivjLineColorButton = null;

	private JPanel ivjLineStylePanel = null;

	private JComboBox ivjMinusComb = null;

	private JButton ivjNoLineButton = null;

	private JButton ivjOuterLineButton = null;

	private JCheckBox ivjPercentCheck = null;

	private JLabel ivjPreviewLabel = null;

	private JPanel ivjPreviewPanel = null;

	private JPanel ivjSamplePanel = null;

	private JPanel ivjStylePanel = null;

	private JList ivjTypeList = null;

	private JPanel ivjTypePagePanel = null;

	private JPanel ivjTypePanel = null;

	private JRadioButton ivjVAutoRadio = null;

	private JRadioButton ivjVCenterRadio = null;

	private JRadioButton ivjVDownRadio = null;

	private JRadioButton ivjVTopRadio = null;

	private JLabel ivjStringLenLable = null;

	private JLabel ivjStringLenText = null;

	private JComboBox ivjDateTypeComb = null;

	

	//nType-nValue;�������õ�ֵ�������OK�����������档
	private Hashtable<Integer, Integer> m_propertyCache = new Hashtable<Integer, Integer>();

	private JButton ivjOkButton = null;

	private LineLabel sampleLabel = null;

	private JPanel ivjAligPagePanel = null;

	private JPanel ivjBorderPanel = null;

	private JList ivjJLineList = null;

	BasicArrowButton upAB = new BasicArrowButton(BasicArrowButton.NORTH);

	BasicArrowButton downAB = new BasicArrowButton(BasicArrowButton.SOUTH);

	BorderPanel m_borderPanel = null;

	private int m_nXLeft = 0;

	private int m_nXRight = 0;

	private int m_nYTop = 0;

	private int m_nYBottom = 0;

	private int m_nXMid = 0;

	private int m_nYMid = 0;

	

	private boolean m_LineisNull = false; //�߿������Ƿ�ѡ��

	private boolean m_isNeedResetBorder = false; //�Ƿ���Ҫ�Ա߿��������
	
	private boolean[] m_updatedBorder = new boolean[8];//���Format�г�����Ӧ�ı߿���£����ֵΪtrue

	private String m_strFontName;

	private int m_nFontStyle, m_nFontSize;

	private Color m_nForColor, m_nBackColor;

	private boolean m_bModified;
	
	private Format m_format = null;//�Ի���Ҫ���õ�ԭʼ��ʽ
    /** ��Ԫ�����Ƿ�����*/
    private boolean m_bTypeLocked;
	
    //ѡȡ��Ԫ�Ƿ�Ϊ������Ԫ������������ϵ�Ԫ��
    private boolean m_bSingleSelected = false;
    //ѡȡ��Ԫ�Ƿ���������ʽ add by guogang 2007-7-2
	private boolean m_bCondition;
    //add end
    //б��
	private JButton btnDiagonal = null;
	private JButton btnDiagonal2 = null;
	/**
	 * ���캯��
	 * @param owner �������ڵı�������
	 * @param format ����ǰ�ĸ�ʽ��Ϣ
	 * @param bTypeLocked ��Ԫ�����Ƿ�����
	 * @param bSingleSelected ѡȡ��Ԫ�Ƿ�Ϊ������Ԫ
	 */
	public CellPropertyDialog(Container owner, Format format, boolean bTypeLocked, boolean bSingleSelected) {
		super(owner);		
		m_format = format;	
		m_bTypeLocked = bTypeLocked;
		m_bSingleSelected = bSingleSelected;
		initialize();
		
//		EventTracer trace=new EventTracer();
//        trace.add(getAlignPage());
	}	
	/**
	 * @see CellPropertyDialog(Container owner, Format format, boolean bTypeLocked, boolean bSingleSelected)
	 * @param owner
	 * @param format
	 * @param bTypeLocked
	 */
	public CellPropertyDialog(Container owner, Format format, boolean bTypeLocked) {
		this(owner, format, bTypeLocked, true);
	}
	
	/**
	 * ����һ��������־��ʹ������ʽ�Ի�����õ�ʱ����Щ���ܲ����ṩ
	 * add by guogang 2007-7-2  
	 */
	public CellPropertyDialog(Container owner, Format format, boolean bTypeLocked, boolean bSingleSelected,boolean bCondition){
		super(owner);
		m_format = format;	
		m_bTypeLocked = bTypeLocked;
		m_bSingleSelected = bSingleSelected;
		m_bCondition=bCondition;
		initialize();
		
	}

	/**
	 * �ӿ�ActionListener����ʵ�֣�����ȷ����ȡ����ť������ѡ��ť��ˮƽ��ֱ���뷽ʽ��ť�Ķ����� �������ڣ�(2000-12-6
	 * 11:25:49)
	 * 
	 * @param event
	 *            java.awt.event.ActionEvent
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JButton) {
			JButton source = (JButton) event.getSource();
			if (source == ivjOkButton) { //ȷ����ť�Ĵ���
				setResult(UfoDialog.ID_OK);				
				if (!m_LineisNull && m_isNeedResetBorder) {
					int borderIndex = 0;
					//�ϱ߿�,�������ϱ�һ�е��ϱ߿�
					int[] values = m_borderPanel.getBorderValue(borderIndex++);
					if ((values[1] != 10264988) && m_updatedBorder[Format.TOPLINE]){						
						setPropertyLater(PropertyType.TLType, values[0]);
						setPropertyLater(PropertyType.TLColor, values[1]);
					}

					//�±߿��������±�һ�е��±߿�
					values = m_borderPanel.getBorderValue(borderIndex++);
					if ((values[1] != 10264988) && m_updatedBorder[Format.BOTTOMLINE]){						
						setPropertyLater(PropertyType.BLType, values[0]);
						setPropertyLater(PropertyType.BLColor, values[1]);
					}
					//��߿����������һ�е���߿�
					values = m_borderPanel.getBorderValue(borderIndex++);
					if ((values[1] != 10264988) && m_updatedBorder[Format.LEFTLINE]){						
						setPropertyLater(PropertyType.LLType, values[0]);
						setPropertyLater(PropertyType.LLColor, values[1]);
					}
					//�ұ߿��������ұ�һ�е��ұ߿�
					values = m_borderPanel.getBorderValue(borderIndex++);
					if ((values[1] != 10264988) && m_updatedBorder[Format.RIGHTLINE]){						
						setPropertyLater(PropertyType.RLType, values[0]);
						setPropertyLater(PropertyType.RLColor, values[1]);
					}
					//��߿򣬳�ȥ���һ�У�����ÿһ�е��±߿�
					values = m_borderPanel.getBorderValue(borderIndex++);
					if ((values[1] != 10264988) && m_updatedBorder[Format.HORLINE]){						
						setPropertyLater(PropertyType.HLType, values[0]);
						setPropertyLater(PropertyType.HLColor, values[1]);
					}
					//���߿򣬳�ȥ���һ�У�����ÿһ�е��ұ߿�
					values = m_borderPanel.getBorderValue(borderIndex++);
					if ((values[1] != 10264988) && m_updatedBorder[Format.VERLINE]){						
						setPropertyLater(PropertyType.VLType, values[0]);
						setPropertyLater(PropertyType.VLColor, values[1]);
					}
					//б�� 
					values = m_borderPanel.getBorderValue(borderIndex++);
					if ((values[1] != 10264988) && m_updatedBorder[Format.DIAGONAL_LINE]){						
						setPropertyLater(PropertyType.DLType, values[0]);
						setPropertyLater(PropertyType.DLColor, values[1]);
					}		
					
					//��б�� 
					values = m_borderPanel.getBorderValue(borderIndex++);
					if ((values[1] != 10264988) && m_updatedBorder[Format.DIAGONAL2_LINE]){						
						setPropertyLater(PropertyType.D2LType, values[0]);
						setPropertyLater(PropertyType.D2LColor, values[1]);
					}					
					
				}

				//whtao this.dispose();
				this.close();
				return;
			} else if (source == ivjcancelButton) { //ȡ����ť�Ĵ���
				setResult(UfoDialog.ID_CANCEL);
				this.close();
				return;
			} else if (source == ivjInnerLineButton) { //�ڱ߿���
				int[] values = getBorderSet();
				m_borderPanel.setBorderValue(4, values);
				m_borderPanel.setBorderValue(5, values);
				repaint();
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.HORLINE] = true;
				m_updatedBorder[Format.VERLINE] = true;
				return;
				
			} else if (source == btnDiagonal) { //�Խ���
				int[] values = getBorderSet();
//				if(getProperty(PropertyType.DLType) != TableConstant.UNDEFINED){
//					values = new int[]{TableConstant.UNDEFINED, TableConstant.UNDEFINED};
//				}
				m_borderPanel.setBorderValue(6, values); 
				repaint();
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.DIAGONAL_LINE] = true; 
				return;		
				
			} else if (source == btnDiagonal2) {  
				int[] values = getBorderSet();
//				if(getProperty(PropertyType.DLType) != TableConstant.UNDEFINED){
//					values = new int[]{TableConstant.UNDEFINED, TableConstant.UNDEFINED};
//				}
				m_borderPanel.setBorderValue(7, values); 
				repaint();
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.DIAGONAL2_LINE] = true; 
				return;					
				
			} else if (source == ivjOuterLineButton) { //�����
				int[] values = getBorderSet();
				m_borderPanel.setBorderValue(0, values);
				m_borderPanel.setBorderValue(1, values);
				m_borderPanel.setBorderValue(2, values);
				m_borderPanel.setBorderValue(3, values);				
				repaint();
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.TOPLINE] = true;
				m_updatedBorder[Format.BOTTOMLINE] = true;
				m_updatedBorder[Format.LEFTLINE] = true;
				m_updatedBorder[Format.RIGHTLINE] = true;
				return;
			}
			if (source == ivjNoLineButton) { //��������
				int[] values = {TableConstant.UNDEFINED,
						TableConstant.UNDEFINED};
				for(int i = 0; i < 8; i++){
					m_borderPanel.setBorderValue(i, values);
					m_updatedBorder[i] = true;
				}
				if(m_bSingleSelected){//modify by wangyga 2008-11-10����ǵ�����Ԫ,����Format.HORLINE,Format.VERLINE
					m_updatedBorder[Format.HORLINE] = false;
					m_updatedBorder[Format.VERLINE] = false;
				}
//				m_borderPanel.setBorderValue(0, values);
//				m_borderPanel.setBorderValue(1, values);
//				m_borderPanel.setBorderValue(2, values);
//				m_borderPanel.setBorderValue(3, values);
//				m_borderPanel.setBorderValue(4, values);
//				m_borderPanel.setBorderValue(5, values);
				repaint();
				m_isNeedResetBorder = true;
//				m_updatedBorder[Format.HORLINE] = true;
//				m_updatedBorder[Format.VERLINE] = true;
//				m_updatedBorder[Format.TOPLINE] = true;
//				m_updatedBorder[Format.BOTTOMLINE] = true;
//				m_updatedBorder[Format.LEFTLINE] = true;
//				m_updatedBorder[Format.RIGHTLINE] = true;
				return;
			}

		} else if (event.getSource() instanceof JRadioButton) {
			JRadioButton source = (JRadioButton) event.getSource();
			if (source == ivjHAutoRadio) { //���뷽ʽˮƽ�Զ�
				ivjJLabel12.setForeground(Color.BLACK);
				setPropertyLater(PropertyType.HorAlig, TableConstant.UNDEFINED);
			} else if (source == ivjHLeftRadio) { //���뷽ʽˮƽ��
				ivjJLabel12.setForeground(Color.BLACK);
				setPropertyLater(PropertyType.HorAlig, TableConstant.HOR_LEFT);
			} else if (source == ivjHCenterRadio) { //���뷽ʽˮƽ����
				ivjJLabel12.setForeground(Color.BLACK);
				setPropertyLater(PropertyType.HorAlig, TableConstant.HOR_CENTER);
			} else if (source == ivjHRightRadio) { //���뷽ʽˮƽ�Ҳ�
				ivjJLabel12.setForeground(Color.BLACK);
				setPropertyLater(PropertyType.HorAlig, TableConstant.HOR_RIGHT);
			} else if (source == ivjVAutoRadio) {
				ivjJLabel13.setForeground(Color.BLACK);
				setPropertyLater(PropertyType.VerAlig, TableConstant.UNDEFINED);
			} else if (source == ivjVTopRadio) {
				ivjJLabel13.setForeground(Color.BLACK);
				setPropertyLater(PropertyType.VerAlig, TableConstant.VER_UP);
			} else if (source == ivjVCenterRadio) {
				ivjJLabel13.setForeground(Color.BLACK);
				setPropertyLater(PropertyType.VerAlig, TableConstant.VER_CENTER);
			} else if (source == ivjVDownRadio) {
				ivjJLabel13.setForeground(Color.BLACK);
				setPropertyLater(PropertyType.VerAlig, TableConstant.VER_DOWN);
			}
			return;
		}
		repaint();
	}

	

	

	
	/**
	 * ���� aligPagePanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getAlignPage() {
		if (ivjAligPagePanel == null) {
			try {
				ivjAligPagePanel = new UIPanel();
				ivjAligPagePanel.setName("AligPagePanel");
//				ivjAligPagePanel.setFont(new java.awt.Font("dialog", 1, 14));
				ivjAligPagePanel.setLayout(null);
				getAlignPage().add(getAlignPageAlignPanel(),
						getAlignPageAlignPanel().getName());
				getAlignPage().add(getAlignPageChangeLineCheck(),
						getAlignPageChangeLineCheck().getName());
				// add by guogang 2007-6-7 
				getAlignPage().add(getAlignPageShrinkFitCheck(),
						getAlignPageShrinkFitCheck().getName());
				//add end
				getAlignPage().add(getAlignPageJLabel14(),
						getAlignPageJLabel14().getName());
				getAlignPage().add(getAlignPageJLabel15(),
						getAlignPageJLabel15().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjAligPagePanel;
	}

	/**
	 * ���� AligPanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getAlignPageAlignPanel() {
		if (ivjAligPanel == null) {
			try {
				ivjAligPanel = new UIPanel();
				ivjAligPanel.setName("AligPanel");
//				ivjAligPanel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjAligPanel.setLayout(null);
				ivjAligPanel.setBounds(3, 14, 404, 102);
				getAlignPageAlignPanel().add(getJLabel12(),
						getJLabel12().getName());
				getAlignPageAlignPanel().add(getHAutoRadio(),
						getHAutoRadio().getName());
				getAlignPageAlignPanel().add(getHLeftRadio(),
						getHLeftRadio().getName());
				getAlignPageAlignPanel().add(getHCenterRadio(),
						getHCenterRadio().getName());
				getAlignPageAlignPanel().add(getHRightRadio(),
						getHRightRadio().getName());
				getAlignPageAlignPanel().add(getJLabel13(),
						getJLabel13().getName());
				getAlignPageAlignPanel().add(getVAutoRadio(),
						getVAutoRadio().getName());
				getAlignPageAlignPanel().add(getVTopRadio(),
						getVTopRadio().getName());
				getAlignPageAlignPanel().add(getVCenterRadio(),
						getVCenterRadio().getName());
				getAlignPageAlignPanel().add(getVDownRadio(),
						getVDownRadio().getName());
				// user code begin {1}
				ivjAligPanel.setBorder(new TitledBorder(new EtchedBorder(),
				        MultiLang.getString("uiuforep0000737"),
						TitledBorder.LEFT, TitledBorder.TOP, //����
						new java.awt.Font("dialog", 0, 14)));

				ButtonGroup groupH = new ButtonGroup();
				groupH.add(getHAutoRadio());
				groupH.add(getHLeftRadio());
				groupH.add(getHCenterRadio());
				groupH.add(getHRightRadio());

				ButtonGroup groupV = new ButtonGroup();
				groupV.add(getVAutoRadio());
				groupV.add(getVTopRadio());
				groupV.add(getVCenterRadio());
				groupV.add(getVDownRadio());

				int n = getProperty(PropertyType.HorAlig);
				if (n == TableConstant.UNDEFINED) {
					n = TableConstant.UNDEFINED;
				} else if (n == TableConstant.DIFFERENT) {
					n = TableConstant.UNDEFINED;
					ivjJLabel12.setForeground(Color.gray);
				}

				switch (n) {
					case TableConstant.UNDEFINED :
						getHAutoRadio().setSelected(true);
						break;
					case TableConstant.HOR_LEFT :
						getHLeftRadio().setSelected(true);
						break;
					case TableConstant.HOR_CENTER :
						getHCenterRadio().setSelected(true);
						break;
					case TableConstant.HOR_RIGHT :
						getHRightRadio().setSelected(true);
				}

				n = getProperty(PropertyType.VerAlig);
				if (n == TableConstant.UNDEFINED) {
					n = TableConstant.UNDEFINED;
				} else if (n == TableConstant.DIFFERENT) {
					n = TableConstant.UNDEFINED;
					ivjJLabel13.setForeground(Color.gray);
				}

				switch (n) {
					case TableConstant.UNDEFINED :
						getVAutoRadio().setSelected(true);
						break;
					case TableConstant.VER_UP :
						getVTopRadio().setSelected(true);
						break;
					case TableConstant.VER_CENTER :
						getVCenterRadio().setSelected(true);
						break;
					case TableConstant.VER_DOWN :
						getVDownRadio().setSelected(true);
				}
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjAligPanel;
	}

	/**
	 * �������屳��ɫ������ɫ��ť caijie 2004-11-17
	 * 
	 * @return ColorButton
	 */
	private ColorButton getBackColorButton() {
		if (ivjBackColorButton == null) {
			try {
				int n = getProperty(PropertyType.BackColor);
				if (n == TableConstant.UNDEFINED) {
					n = DefaultFormatValue.BACK_COLOR.getRGB();
				} else if (n == TableConstant.DIFFERENT) {
					n = Color.gray.getRGB();
					ivjJLabelBackground.setForeground(Color.gray);
				}
				ivjBackColorButton = new ColorButton(new Color(n), this) {

					private static final long serialVersionUID = 6779194138869520632L;

					public void actionPerformed(ActionEvent e) {
						Color c = JColorChooser.showDialog(
								CellPropertyDialog.this, MultiLang.getString("uiuforep0000908"),
								this//���屳����ɫ
										.getUserColor());
						if (c != null) {
							ivjJLabelBackground.setForeground(Color.BLACK);
							setUserColor(c);
							setPropertyLater(PropertyType.BackColor, c.getRGB());
							showSample();
						}
					}
				};
				ivjBackColorButton.setName("BackColorButton");
				ivjBackColorButton.setBounds(65, 62, 130, 22);
			} catch (java.lang.Throwable ivjExc) {
				IUFOLogger.getLogger(this).fatal(
				        MultiLang.getString("uiuforep0000909"));//��ȡ���屳����ɫ��ťʱ����
				handleException(ivjExc);
			}

		}
		return ivjBackColorButton;
	}
	/**
	 * ���� BorderPagePanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getBorderPage() {
		if (ivjBorderPagePanel == null) {
			try {
				ivjBorderPagePanel = new UIPanel();
				ivjBorderPagePanel.setName("BorderPagePanel");
//				ivjBorderPagePanel.setFont(new java.awt.Font("dialog", 1, 14));
				ivjBorderPagePanel.setLayout(null);
				getBorderPage().add(getBorderPageBorderPanel(),
						getBorderPageBorderPanel().getName());
				getBorderPage().add(getBorderPageLineStylePanel(),
						getBorderPageLineStylePanel().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjBorderPagePanel;
	}

	/**
	 * ���� FramePanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getBorderPageBorderPanel() {
		if (ivjBorderPanel == null) {
			try {
				ivjBorderPanel = new UIPanel();
				ivjBorderPanel.setName("BorderPanel");
				ivjBorderPanel.setLayout(null);
				ivjBorderPanel.setBounds(1, 11, 245, 255);
				getBorderPageBorderPanel().add(getNoLineButton(),
						getNoLineButton().getName());
				getBorderPageBorderPanel().add(getOuterLineButton(),
						getOuterLineButton().getName());
				getBorderPageBorderPanel().add(getInnerLineButton(),
						getInnerLineButton().getName());
				// user code begin {1}
				ivjBorderPanel.setBorder(new TitledBorder(new EtchedBorder(),
				        MultiLang.getString("uiuforep0000738"),
						TitledBorder.LEFT, TitledBorder.TOP,//�߿�
						null));

				m_borderPanel = new BorderPanel();				
				m_borderPanel.setName("JPanel1");
				m_borderPanel.setLayout(null);
				m_borderPanel.setBackground(java.awt.Color.white);
				m_borderPanel.setBounds(16, 22, 214, 133);
				int width = m_borderPanel.getWidth();
				int height = m_borderPanel.getHeight();
				m_nXLeft = width / 8;
				m_nXRight = width / 8 * 7;
				m_nYTop = height / 8;
				m_nYBottom = height / 8 * 7;
				m_nXMid = width / 2;
				m_nYMid = height / 2;
				getBorderPageBorderPanel().add(m_borderPanel,
						m_borderPanel.getName());
				ivjBorderPanel.add(getBtnDiagonal(), null);
				
				ivjBorderPanel.add(getBtnDiagonal2(), null);
				
				m_borderPanel.addMouseListener(this);
				
				for(int i = 0; i < m_updatedBorder.length; i++)
				    m_updatedBorder[i] = false;
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjBorderPanel;
	}

	/**
	 * �õ���ʱ��Ԫ�߿��������Ϣ�� �������ڣ�(2004-5-13 10:37:52)
	 * 
	 * @return int[] {���Σ���ɫ}
	 */
	private int[] getBorderSet() {
		int[] values = new int[2];
		if (ivjJLineList.getSelectedIndex() != -1) {
			values[0] = ivjJLineList.getSelectedIndex();
		} else {
			values[0] = TableConstant.UNDEFINED;
		}
		//        Integer color = (Integer) ivjLineColorComb.getSelectedItem();
		values[1] = ivjLineColorButton.getUserColor().getRGB();
		return values;
	}

	/**
	 * ���� cancelButton ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getcancelButton() {
		if (ivjcancelButton == null) {
			try {
				ivjcancelButton = new nc.ui.pub.beans.UIButton();
				ivjcancelButton.setName("cancelButton");
//				ivjcancelButton.setFont(new java.awt.Font("dialog", 0, 14));
				ivjcancelButton.setText(MultiLang.getString("uiuforep0000739"));//"ȡ��");//
				ivjcancelButton.setBounds(339, 314, 75, 22);
				ivjcancelButton.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjcancelButton;
	}

	/**
	 * ���� nc.ui.pub.beans.UITabbedPane ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITabbedPane
	 */
	public nc.ui.pub.beans.UITabbedPane getTabbedPane() {
		if (ivjCellTabbedPane == null) {
			try {
				ivjCellTabbedPane = new UITabbedPane();
				ivjCellTabbedPane.setName("CellTabbedPane");
//				ivjCellTabbedPane.setFont(new java.awt.Font("dialog", 0, 14));
				ivjCellTabbedPane.setBackground(java.awt.SystemColor.control);
				ivjCellTabbedPane.setBounds(9, 7, 415, 298);
				ivjCellTabbedPane.setBorder(null);
				//modify by guogang 2007-7-2	
				int tabIndex=0;
				if(!m_bCondition){
				ivjCellTabbedPane.insertTab(MultiLang.getString("uiuforep0000740"), null,
							getDataTypePage(),//��������
							null, tabIndex++);
				}
			    ivjCellTabbedPane.insertTab(MultiLang.getString("uiuforep0000741"), null,
						getFontPage(), null,//����ͼ��
						tabIndex++);
			    
			    if(!m_bCondition){
				ivjCellTabbedPane.insertTab(MultiLang.getString("uiuforep0000742"), null,
						getAlignPage(),//  ����
						null, tabIndex++);
				

				ivjCellTabbedPane.insertTab(MultiLang.getString("uiuforep0000743"), null,
						getBorderPage(),//  �߿�
						null, tabIndex++);
			    }
				//modify end
//				if(m_bTypeLocked == true){
//					ivjCellTabbedPane.removeTabAt(0);
//				}
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCellTabbedPane;
	}

	/**
	 * ���� ChangeLineCheck ����ֵ��
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getAlignPageChangeLineCheck() {
		if (ivjChangeLineCheck == null) {
			try {
				ivjChangeLineCheck = new UICheckBox();
				ivjChangeLineCheck.setName("ChangeLineCheck");
				ivjChangeLineCheck.setText(MultiLang.getString("uiuforep0000744"));//�����ڵ�Ԫ��������ʾ
				ivjChangeLineCheck.setBounds(18, 125, 178, 27);
				int n = getProperty(PropertyType.ChangeLine);
				if (n == TableConstant.UNDEFINED || n == 0) {
					ivjChangeLineCheck.setSelected(false);
				} else if (n == 1) {
					ivjChangeLineCheck.setSelected(true);
					getAlignPageShrinkFitCheck().setEnabled(false);
				} else if (n == TableConstant.DIFFERENT) {
					ivjChangeLineCheck.setSelected(true);
					ivjChangeLineCheck.setForeground(Color.gray);
				}

				ivjChangeLineCheck.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							setPropertyLater(PropertyType.ChangeLine, 1);
							ivjShrinkFitCheck.setEnabled(false);
						} else if (e.getStateChange() == ItemEvent.DESELECTED) {
							setPropertyLater(PropertyType.ChangeLine, 0);
							ivjShrinkFitCheck.setEnabled(true);
						}
						ivjChangeLineCheck.setForeground(Color.black);

					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjChangeLineCheck;
	}

	/**
	 * ���� ChineseComb ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getChineseComb() {
		if (ivjChineseComb == null) {
			try {
				ivjChineseComb = new UIComboBox();
				ivjChineseComb.setName("ChineseComb");
//				ivjChineseComb.setFont(new java.awt.Font("dialog", 0, 14));
				ivjChineseComb.setBounds(90, 114, 178, 27);
				String[] str = DefaultSetting.chineseFormat;
				for (int i = 0; i < str.length; i++) {
					ivjChineseComb.addItem(str[i]);
				}
				int n = getProperty(PropertyType.ChineseFormat);
				if (n == TableConstant.UNDEFINED || n == 0) {
					n = 0;
				} else if (n == TableConstant.DIFFERENT) {
					n = -1;
					ivjJLabel3.setForeground(Color.gray);
				}

				ivjChineseComb.setSelectedIndex(n);

				ivjChineseComb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						if (ivjCommaCheck.getSelectedObjects() != null
								|| ivjPercentCheck.getSelectedObjects() != null) {
							Object[] options = {
							        MultiLang.getString("uiuforep0000745"),
							        MultiLang.getString("uiuforep0000746")};//{ "ȷ
							// ��",
							// "ȡ
							// ��"
							// };//
							int nReturn = JOptionPane
									.showOptionDialog(
											CellPropertyDialog.this,
											MultiLang.getString("uiuforep0000747"),
											"Warning",//ѡ�����Ĵ�Сд�󣬷ָ��š��ٷֺŵ�ѡ����Ч���Ƿ������
											JOptionPane.DEFAULT_OPTION,
											JOptionPane.WARNING_MESSAGE, null,
											options, options[0]);
							if (nReturn == JOptionPane.OK_OPTION) {
								ivjCommaCheck.setSelected(false);
								ivjPercentCheck.setSelected(false);
							} else {
								int n = getProperty(PropertyType.ChineseFormat);
								if (n == TableConstant.UNDEFINED || n == 0) {
									n = 0;
								} else if (n == TableConstant.DIFFERENT) {
									ivjChineseComb.setForeground(Color.gray);
								}

								ivjChineseComb.setSelectedIndex(n);
								return;
							}
						}
						int n = ivjChineseComb.getSelectedIndex();
						ivjJLabel3.setForeground(Color.BLACK);
						setPropertyLater(PropertyType.ChineseFormat, n);
						showPreview();
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjChineseComb;
	}
	
	/**
	 * ���� DateTypeCombo ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getDateTypeCombo(){
		if (ivjDateTypeComb == null) {
			try {
				ivjDateTypeComb = new UIComboBox();
				ivjDateTypeComb.setName("DateTypeComb");
				ivjDateTypeComb.setBounds(90, 21, 178, 27);
				String[] str = DefaultSetting.dateFormat;
				for (int i = 0; i < str.length; i++) {
					ivjDateTypeComb.addItem(str[i]);
				}
				
				int n = getProperty(PropertyType.DateType);
				if (n == TableConstant.UNDEFINED || n == 0) {
					n = 0;
				} else if (n == TableConstant.DIFFERENT) {
					n = -1;
					ivjJLabel5.setForeground(Color.gray);
				}

				ivjDateTypeComb.setSelectedIndex(n);
				ivjDateTypeComb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						int n = ivjDateTypeComb.getSelectedIndex();
						ivjJLabel5.setForeground(Color.BLACK);
						setPropertyLater(PropertyType.DateType, n);
						showPreview();
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		
		return ivjDateTypeComb;
	}
	
	/**
	 * ���� ColorPanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getFontPageColorPanel() {
		if (ivjColorPanel == null) {
			try {
				ivjColorPanel = new UIPanel();
				ivjColorPanel.setName("ColorPanel");
				ivjColorPanel.setLayout(null);
				ivjColorPanel.setBounds(201, 12, 206, 142);

				getFontPageColorPanel().add(getJLabel9(),
						getJLabel9().getName());
				getFontPageColorPanel().add(getForeColorButton(),
						getForeColorButton().getName());
				//                getFontPageColorPanel().add(getForeColorComb(),
				//                        getForeColorComb().getName());

				getFontPageColorPanel().add(getJLabelBackground(),
						getJLabelBackground().getName());
				getFontPageColorPanel().add(getBackColorButton(),
						getBackColorButton().getName());
				//                getFontPageColorPanel().add(getBackColorComb(),
				//                        getBackColorComb().getName());

//				getFontPageColorPanel().add(getJLabel11(),
//						getJLabel11().getName());
//				getFontPageColorPanel().add(getFillTypeComb(),
//						getFillTypeComb().getName());
				ivjColorPanel.setBorder(new TitledBorder(new EtchedBorder(),
				        MultiLang.getString("uiuforep0000748"),
						TitledBorder.LEFT, TitledBorder.TOP,//��ɫͼ��
						new java.awt.Font("dialog", 0, 14)));
			} catch (java.lang.Throwable ivjExc) {
				System.out.println("error in getColorPanel():"
						+ ivjExc.getMessage());
				handleException(ivjExc);
			}
		}
		return ivjColorPanel;
	}

	/**
	 * ���� CommaCheck ����ֵ��
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getCommaCheck() {
		if (ivjCommaCheck == null) {
			try {
				ivjCommaCheck = new UICheckBox();
				ivjCommaCheck.setName("CommaCheck");
//				ivjCommaCheck.setFont(new java.awt.Font("dialog", 0, 14));
				ivjCommaCheck.setText(MultiLang.getString("uiuforep0000749"));//"�ָ���");
				ivjCommaCheck.setBounds(13, 19, 67, 26);
				int n = getProperty(PropertyType.Comma);
				if (n == TableConstant.UNDEFINED || n == 0) {
					ivjCommaCheck.setSelected(false);
				} else if (n == 1) {
					ivjCommaCheck.setSelected(true);
				} else if (n == TableConstant.DIFFERENT) {
					ivjCommaCheck.setSelected(true);
					ivjCommaCheck.setForeground(Color.gray);
				}

				ivjCommaCheck.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (ivjChineseComb.getSelectedIndex() != 0
								&& e.getStateChange() == ItemEvent.SELECTED) { //��������Ĵ�Сд��ʽ���򶺺�ѡ����Ч
							JOptionPane
									.showMessageDialog(
											CellPropertyDialog.this,
											MultiLang.getString("uiuforep0000750"),//�����Ĵ�Сд���ָ���ѡ����Ч��
													MultiLang.getString("uiuforep0000751"),
											JOptionPane.INFORMATION_MESSAGE);//��ʾ
							ivjCommaCheck.setSelected(false);
							return;
						}
						if (e.getStateChange() == ItemEvent.SELECTED) {
							setPropertyLater(PropertyType.Comma, 1);
							showPreview();
						} else if (e.getStateChange() == ItemEvent.DESELECTED) {
							setPropertyLater(PropertyType.Comma, 0);
							showPreview();
						}
						ivjCommaCheck.setForeground(Color.black);

					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCommaCheck;
	}

	/**
	 * ���� CurCombo ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCurCombo() {
		if (ivjCurCombo == null) {
			try {
				ivjCurCombo = new UIComboBox();
				ivjCurCombo.setName("CurCombo");
//				ivjCurCombo.setFont(new java.awt.Font("dialog", 0, 14));
				ivjCurCombo.setBounds(90, 82, 178, 27);

				String[] str = DefaultSetting.currencySymbol;
				for (int i = 0; i < str.length; i++) {
					ivjCurCombo.addItem(str[i]);
				}

				int n = getProperty(PropertyType.CurSymbol);
				if (n == TableConstant.UNDEFINED || n == 0) {
					n = 0;
				} else if (n == TableConstant.DIFFERENT) {
					n = -1;
					ivjJLabel2.setForeground(Color.gray);
				}

				ivjCurCombo.setSelectedIndex(n);

				ivjCurCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						ivjJLabel2.setForeground(Color.black);
						int nIndex = ivjCurCombo.getSelectedIndex();
						setPropertyLater(PropertyType.CurSymbol, nIndex);
						showPreview();
					}
				});

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCurCombo;
	}

	/**
	 * ���� DecimalText ����ֵ��
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getDecimalText() {
		if (ivjDecimalText == null) {
			try {
				ivjDecimalText = new UITextField();
				ivjDecimalText.setName("DecimalText");
//				ivjDecimalText.setFont(new java.awt.Font("dialog", 0, 14));
				ivjDecimalText.setBounds(90, 49, 156, 28);
				int n = getProperty(PropertyType.DecimalDigits);
				if (n == TableConstant.UNDEFINED) {
					n = DefaultFormatValue.DECIMAL_DIGITS;
				} else if (n == TableConstant.DIFFERENT) {
					n = DefaultFormatValue.DECIMAL_DIGITS;
					ivjJLabel4.setForeground(Color.gray);
				}
				ivjDecimalText.setText(String.valueOf(n));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjDecimalText;
	}

	/**
	 * ���� FontNameComb ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getFontNameComb() {
		if (ivjFontNameComb == null) {
			try {
				ivjFontNameComb = new UIComboBox();
				ivjFontNameComb.setName("FontNameComb");
//				ivjFontNameComb.setFont(new java.awt.Font("dialog", 0, 14));
				ivjFontNameComb.setBounds(52, 27, 130, 27);
				//ϵͳ�����
				String[] fontNames = DefaultSetting.fontNames;

				if (fontNames != null) {
					int songti = 0;//������������к�
					for (int i = 0; i < fontNames.length; i++) {
						if (fontNames[i] != null && !fontNames[i].equals("")) {
							ivjFontNameComb.addItem(fontNames[i]);
						}

						try {
							if (fontNames[i].equals(DefaultFormatValue.FONTNAME))
								songti = i;
						} catch (Exception e) {
							songti = 0;
						}
					}
					int fontIndex = getProperty(PropertyType.FontIndex);
					if (fontIndex == TableConstant.UNDEFINED) {
						fontIndex = songti;
					} else if (fontIndex == TableConstant.DIFFERENT) {
						fontIndex = -1;
						ivjJLabel6.setForeground(Color.gray);
					}

					ivjFontNameComb.setSelectedIndex(fontIndex);
				}

				ivjFontNameComb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						ivjJLabel6.setForeground(Color.BLACK);
						int nIndex = ivjFontNameComb.getSelectedIndex();
						//setProperty(PropertyType.FontIndex, nIndex);
						setPropertyLater(PropertyType.FontIndex, nIndex);
						showSample();
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjFontNameComb;
	}

	/**
	 * ���� FontPagePanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getFontPage() {
		if (ivjFontPagePanel == null) {
			try {
				ivjFontPagePanel = new UIPanel();
				ivjFontPagePanel.setName("FontPagePanel");
//				ivjFontPagePanel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjFontPagePanel.setLayout(null);
				getFontPage().add(getFontPageFontPanel(),
						getFontPageFontPanel().getName());
				getFontPage().add(getFontPageColorPanel(),
						getFontPageColorPanel().getName());
				getFontPage().add(getFontPageSamplePanel(),
						getFontPageSamplePanel().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjFontPagePanel;
	}

	/**
	 * ���� FontPanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getFontPageFontPanel() {
		if (ivjFontPanel == null) {
			try {
				ivjFontPanel = new UIPanel();
				ivjFontPanel.setName("FontPanel");
				ivjFontPanel.setLayout(null);
				ivjFontPanel.setBounds(2, 12, 197, 142);
				getFontPageFontPanel()
						.add(getJLabel6(), getJLabel6().getName());
				getFontPageFontPanel().add(getFontNameComb(),
						getFontNameComb().getName());
				getFontPageFontPanel()
						.add(getJLabel7(), getJLabel7().getName());
				getFontPageFontPanel().add(getFontTypeComb(),
						getFontTypeComb().getName());
				getFontPageFontPanel()
						.add(getJLabel8(), getJLabel8().getName());
				getFontPageFontPanel().add(getFontSizeComb(),
						getFontSizeComb().getName());
				ivjFontPanel.setBorder(new TitledBorder(new EtchedBorder(),
				        MultiLang.getString("uiuforep0000754"),
						TitledBorder.LEFT, TitledBorder.TOP,//"����"
						new java.awt.Font("dialog", 0, 14)));
			} catch (java.lang.Throwable ivjExc) {
				System.out.println("error in getFontPanel():"
						+ ivjExc.getMessage());
				handleException(ivjExc);
			}
		}
		return ivjFontPanel;
	}

	/**
	 * ���� FontSizeComb ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getFontSizeComb() {
		if (ivjFontSizeComb == null) {
			try {
				ivjFontSizeComb = new UIComboBox();
				ivjFontSizeComb.setName("FontSizeComb");
//				ivjFontSizeComb.setFont(new java.awt.Font("dialog", 0, 14));
				ivjFontSizeComb.setBounds(52, 98, 130, 27);
				for (int i = 0; i < DefaultSetting.fontSizes.length; i++) {
					ivjFontSizeComb.addItem(DefaultSetting.fontSizes[i]);
				}
				int n = getProperty(PropertyType.FontSize);

				if (n == TableConstant.UNDEFINED) {
					n = DefaultFormatValue.FONT_SIZE;
				} else if (n == TableConstant.DIFFERENT) {
					n = -1;
					ivjJLabel8.setForeground(Color.gray);
				}
				for(int i = 0; i < DefaultSetting.fontSizes.length; i++){
					int each = Integer.parseInt(DefaultSetting.fontSizes[i]);
					if(n <= each){
						n = each;
						break;
					}
				}
				ivjFontSizeComb.setSelectedItem(String.valueOf(n));

				ivjFontSizeComb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						ivjJLabel8.setForeground(Color.BLACK);
						String strSize = (String) ivjFontSizeComb
								.getSelectedItem();
						int nSize = Integer.parseInt(strSize);
						setPropertyLater(PropertyType.FontSize, nSize);

						showSample();

					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjFontSizeComb;
	}

	/**
	 * ���� FontTypeComb ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getFontTypeComb() {
		if (ivjFontTypeComb == null) {
			try {
				ivjFontTypeComb = new UIComboBox();
				ivjFontTypeComb.setName("FontTypeComb");
//				ivjFontTypeComb.setFont(new java.awt.Font("dialog", 0, 14));
				ivjFontTypeComb.setBounds(52, 62, 130, 27);
				for (int i = 0; i < DefaultSetting.fontStyles.length; i++) {
					ivjFontTypeComb.addItem(DefaultSetting.fontStyles[i]);
				}
				int n = getProperty(PropertyType.FontStyle);

				if (n == TableConstant.UNDEFINED) {
					n = DefaultFormatValue.FONT_STYLE;
				} else if (n == TableConstant.DIFFERENT) {
					n = -1;
					ivjJLabel7.setForeground(Color.gray);
				}
				ivjFontTypeComb.setSelectedIndex(n);

				ivjFontTypeComb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						ivjJLabel7.setForeground(Color.BLACK);
						int nIndex = ivjFontTypeComb.getSelectedIndex();
						setPropertyLater(PropertyType.FontStyle, nIndex);

						showSample();

					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjFontTypeComb;
	}

	/**
	 * ��������ǰ��ɫ������ɫ��ť caijie 2004-11-17
	 * 
	 * @return ColorButton
	 */
	private ColorButton getForeColorButton() {
		if (ivjForeColorButton == null) {
			try {
				int n = getProperty(PropertyType.ForeColor);
				if (n == TableConstant.UNDEFINED) {
					n = DefaultFormatValue.FORE_COLOR.getRGB();
				} else if (n == TableConstant.DIFFERENT) {
					n = Color.gray.getRGB();
					ivjJLabel9.setForeground(Color.gray);
				}
				ivjForeColorButton = new ColorButton(new Color(n), this) {
					private static final long serialVersionUID = 2108696882366348284L;

					public void actionPerformed(ActionEvent e) {
						Color c = JColorChooser.showDialog(
								CellPropertyDialog.this, MultiLang.getString("uiuforep0000906"),
								this//������ɫ
										.getUserColor());
						if (c != null) {
							ivjJLabel9.setForeground(Color.BLACK);
							setUserColor(c);
							setPropertyLater(PropertyType.ForeColor, c.getRGB());
							showSample();
						}
					}
				};
				ivjForeColorButton.setName("ForeColorButton");
				//                ivjForeColorButton.setFont(new java.awt.Font("dialog", 0,
				// 14));
				ivjForeColorButton.setBounds(65, 27, 130, 22);

			} catch (java.lang.Throwable ivjExc) {
				IUFOLogger.getLogger(this).fatal(
				        MultiLang.getString("uiuforep0000907"));//��ȡ������ɫ��ťʱ����
				handleException(ivjExc);
			}

		}
		return ivjForeColorButton;
	}
	/**
	 * ���� HAutoRadio ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getHAutoRadio() {
		if (ivjHAutoRadio == null) {
			try {
				ivjHAutoRadio = new UIRadioButton();
				ivjHAutoRadio.setName("HAutoRadio");
				ivjHAutoRadio.setText(MultiLang.getString("uiuforep0000755"));//�Զ�
				ivjHAutoRadio.setBounds(118, 25, 53, 26);
				ivjHAutoRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjHAutoRadio;
	}

	/**
	 * ���� HCenterRadio ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getHCenterRadio() {
		if (ivjHCenterRadio == null) {
			try {
				ivjHCenterRadio = new UIRadioButton();
				ivjHCenterRadio.setName("HCenterRadio");
				ivjHCenterRadio.setText(MultiLang.getString("uiuforep0000756"));//����
				ivjHCenterRadio.setBounds(260, 25, 53, 26);
				ivjHCenterRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjHCenterRadio;
	}

	/**
	 * ���� HLeftRadio ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getHLeftRadio() {
		if (ivjHLeftRadio == null) {
			try {
				ivjHLeftRadio = new UIRadioButton();
				ivjHLeftRadio.setName("HLeftRadio");
				ivjHLeftRadio.setText(MultiLang.getString("uiuforep0000757"));//����
				ivjHLeftRadio.setBounds(189, 25, 53, 26);
				ivjHLeftRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjHLeftRadio;
	}

	/**
	 * ���� HRightRadio ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getHRightRadio() {
		if (ivjHRightRadio == null) {
			try {
				ivjHRightRadio = new UIRadioButton();
				ivjHRightRadio.setName("HRightRadio");
				ivjHRightRadio.setText(MultiLang.getString("uiuforep0000758"));//����
				ivjHRightRadio.setBounds(331, 25, 53, 26);
				ivjHRightRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjHRightRadio;
	}

	/**
	 * ���� InnerLineButton ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getInnerLineButton() {
		if (ivjInnerLineButton == null) {
			try {
				ivjInnerLineButton = new nc.ui.pub.beans.UIButton();
				ivjInnerLineButton.setName("InnerLineButton");
				ivjInnerLineButton.setText(MultiLang.getString("uiuforep0000759"));//�ڿ���
				ivjInnerLineButton.setBounds(164, 194, 74, 22);
				ivjInnerLineButton.addActionListener(this);
				 
				if(m_bSingleSelected){
					ivjInnerLineButton.setEnabled(false);
				}
				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjInnerLineButton;
	}

	/**
	 * ���� JLabel1 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (ivjJLabel1 == null) {
			try {
				ivjJLabel1 = new UILabel();
				ivjJLabel1.setName("JLabel1");
				ivjJLabel1.setText(MultiLang.getString("uiuforep0000760"));//������ʾ
				ivjJLabel1.setBackground(java.awt.SystemColor.control);
				ivjJLabel1.setBounds(13, 150, 56, 18);
				ivjJLabel1.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel1;
	}

	/**
	 * ���� JLabel10 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelBackground() {
		if (ivjJLabelBackground == null) {
			try {
				ivjJLabelBackground = new UILabel();
				ivjJLabelBackground.setName("JLabel10");
				ivjJLabelBackground.setText(MultiLang.getString("uiuforep0000761"));//����ɫ
				ivjJLabelBackground.setBounds(13, 67, 42, 18);
				ivjJLabelBackground.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabelBackground;
	}
	

	/**
	 * ���� JLabel12 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel12() {
		if (ivjJLabel12 == null) {
			try {
				ivjJLabel12 = new UILabel();
				ivjJLabel12.setName("JLabel12");
				ivjJLabel12.setText(MultiLang.getString("uiuforep0000763"));//ˮƽ����
				ivjJLabel12.setBounds(18, 29, 82, 18);
				ivjJLabel12.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel12;
	}

	/**
	 * ���� JLabel13 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel13() {
		if (ivjJLabel13 == null) {
			try {
				ivjJLabel13 = new UILabel();
				ivjJLabel13.setName("JLabel13");
				ivjJLabel13.setText(MultiLang.getString("uiuforep0000764"));//��ֱ����
				ivjJLabel13.setBounds(18, 65, 88, 18);
				ivjJLabel13.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel13;
	}

	/**
	 * ���� JLabel14 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getAlignPageJLabel14() {
		if (ivjJLabel14 == null) {
			try {
				ivjJLabel14 = new UILabel();
				ivjJLabel14.setName("JLabel14");
				ivjJLabel14.setText(MultiLang.getString("uiuforep0000765"));//���뷽ʽѡ���Զ�����ʾ�Զ����ݵ�Ԫ���������룬������
				ivjJLabel14.setForeground(java.awt.Color.black);
				ivjJLabel14
						.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
				ivjJLabel14
						.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
				ivjJLabel14.setBounds(16, 171, 383, 23);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel14;
	}

	/**
	 * ���� JLabel15 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getAlignPageJLabel15() {
		if (ivjJLabel15 == null) {
			try {
				ivjJLabel15 = new UILabel();
				ivjJLabel15.setName("JLabel15");
				ivjJLabel15.setText(MultiLang.getString("uiuforep0000766"));//���ַ����;����������;��ҡ�
				ivjJLabel15.setBounds(16, 203, 257, 16);
				ivjJLabel15.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel15;
	}

	/**
	 * ���� JLabel16 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel16() {
		if (ivjJLabel16 == null) {
			try {
				ivjJLabel16 = new UILabel();
				ivjJLabel16.setName("JLabel16");
				ivjJLabel16.setText(MultiLang.getString("uiuforep0000767"));//��ʽ
				ivjJLabel16.setBounds(17, 22, 52, 16);
				ivjJLabel16.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel16;
	}

	/**
	 * ���� JLabel17 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel17() {
		if (ivjJLabel17 == null) {
			try {
				ivjJLabel17 = new UILabel();
				ivjJLabel17.setName("JLabel17");
				ivjJLabel17.setText(MultiLang.getString("uiuforep0000768"));//��ɫ
				ivjJLabel17.setBounds(17, 207, 52, 16);
				ivjJLabel17.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel17;
	}

	/**
	 * ���� JLabel2 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (ivjJLabel2 == null) {
			try {
				ivjJLabel2 = new UILabel();
				ivjJLabel2.setName("JLabel2");
				ivjJLabel2.setText(MultiLang.getString("uiuforep0000769"));//���ҷ���
				ivjJLabel2.setBackground(java.awt.SystemColor.control);
				ivjJLabel2.setBounds(13, 86, 56, 18);
				ivjJLabel2.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel2;
	}

	/**
	 * ���� JLabel3 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (ivjJLabel3 == null) {
			try {
				ivjJLabel3 = new UILabel();
				ivjJLabel3.setName("JLabel3");
				ivjJLabel3.setText(MultiLang.getString("uiuforep0000770"));//���Ĵ�Сд
				ivjJLabel3.setBackground(java.awt.SystemColor.control);
				ivjJLabel3.setBounds(13, 118, 70, 18);
				ivjJLabel3.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel3;
	}

	/**
	 * ���� JLabel4 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (ivjJLabel4 == null) {
			try {
				ivjJLabel4 = new UILabel();
				ivjJLabel4.setName("JLabel4");
				ivjJLabel4.setText(MultiLang.getString("uiuforep0000771"));//С��λ��
				ivjJLabel4.setBackground(java.awt.SystemColor.control);
				ivjJLabel4.setBounds(13, 54, 56, 18);
				ivjJLabel4.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel4;
	}
	
	/**
	 * ���� JLabel5 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 * @i18n miufo00003=��ʾ��ʽ
	 */
	private javax.swing.JLabel getJLabel5() {
		if (ivjJLabel5 == null) {
			try {
				ivjJLabel5 = new UILabel();
				ivjJLabel5.setName("JLabel5");
				ivjJLabel5.setText(MultiLang.getString("miufo00003"));//��ʾ��ʽ
				ivjJLabel5.setBackground(java.awt.SystemColor.control);
				ivjJLabel5.setBounds(23, 21, 70, 22);
				ivjJLabel5.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel5;
	}

	/**
	 * ���� JLabel6 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if (ivjJLabel6 == null) {
			try {
				ivjJLabel6 = new UILabel();
				ivjJLabel6.setName("JLabel6");
				ivjJLabel6.setText(MultiLang.getString("uiuforep0000772"));//����
				ivjJLabel6.setBounds(15, 34, 28, 18);
				ivjJLabel6.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel6;
	}

	/**
	 * ���� JLabel7 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if (ivjJLabel7 == null) {
			try {
				ivjJLabel7 = new UILabel();
				ivjJLabel7.setName("JLabel7");
				ivjJLabel7.setText(MultiLang.getString("uiuforep0000773"));//����
				ivjJLabel7.setBounds(15, 67, 28, 18);
				ivjJLabel7.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel7;
	}

	/**
	 * ���� JLabel8 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if (ivjJLabel8 == null) {
			try {
				ivjJLabel8 = new UILabel();
				ivjJLabel8.setName("JLabel8");
				ivjJLabel8.setText(MultiLang.getString("uiuforep0000774"));//�ֺ�
				ivjJLabel8.setBounds(17, 102, 28, 18);
				ivjJLabel8.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel8;
	}

	/**
	 * ���� JLabel9 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel9() {
		if (ivjJLabel9 == null) {
			try {
				ivjJLabel9 = new UILabel();
				ivjJLabel9.setName("JLabel9");
				ivjJLabel9.setText(MultiLang.getString("uiuforep0000775"));//ǰ��ɫ
				ivjJLabel9.setBounds(13, 34, 42, 18);
				ivjJLabel9.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel9;
	}

	/**
	 * ���� JLineList ����ֵ��
	 * 
	 * @return javax.swing.JList
	 */
	private javax.swing.JList getJLineList() {
		if (ivjJLineList == null) {
			try {
				ivjJLineList = new UIList();
				ivjJLineList.setName("JLineList");
				ivjJLineList.setBounds(17, 39, 128, 165);
				ivjJLineList.setBorder(new BevelBorder(BevelBorder.LOWERED));
				String typeName[] = {
						new Integer(TableConstant.L_NULL).toString(),
						new Integer(TableConstant.L_SOLID1).toString(),
						new Integer(TableConstant.L_DASH).toString(),
						new Integer(TableConstant.L_DOT).toString(),
						new Integer(TableConstant.L_DASHDOT).toString(),
						new Integer(TableConstant.L_SOLID2).toString(),
						new Integer(TableConstant.L_SOLID3).toString(),
						new Integer(TableConstant.L_SOLID4).toString()};

				ivjJLineList.setListData(typeName);
				ivjJLineList.setCellRenderer(new LineRender());
				ivjJLineList.setSelectedIndex(1);

				ivjJLineList
						.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								if (e.getValueIsAdjusting() == false) {
									int nIndex = ivjJLineList
											.getSelectedIndex();
									if (nIndex == -1) {
										m_LineisNull = true;
									} else {
										m_LineisNull = false;
									}
								}
							}
						});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLineList;
	}
	/**
	 * ������ɫ������ɫ��ť caijie 2004-11-17
	 * 
	 * @return ColorButton
	 */
	private ColorButton getLineColorButton() {
		if (ivjLineColorButton == null) {
			try {
			    ivjLineColorButton = new
			    ColorButton(DefaultSetting.LINE_COLOR, this) {
					private static final long serialVersionUID = -8424089644512374377L;

					public void actionPerformed(ActionEvent e) {
						Color c = JColorChooser.showDialog(
								CellPropertyDialog.this, MultiLang.getString("uiuforep0000910"),
								this//����ɫ
										.getUserColor());
						if (c != null) {
							ivjJLabel17.setForeground(Color.BLACK);
							setUserColor(c);
						}
					}
				};
				ivjLineColorButton.setName("LineColorButton");
				ivjLineColorButton.setBounds(17, 225, 130, 22);
			} catch (java.lang.Throwable ivjExc) {
				IUFOLogger.getLogger(this).fatal(
				        MultiLang.getString("uiuforep0000911"));//��ȡ����ɫ��ťʱ����
				handleException(ivjExc);
			}

		}
		return ivjLineColorButton;
	}

	
	/**
	 * ���� LineStylePanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getBorderPageLineStylePanel() {
		if (ivjLineStylePanel == null) {
			try {
				ivjLineStylePanel = new UIPanel();
				ivjLineStylePanel.setName("LineStylePanel");
				ivjLineStylePanel.setLayout(null);
				ivjLineStylePanel.setBounds(247, 11, 160, 255);
				getBorderPageLineStylePanel().add(getJLabel16(),
						getJLabel16().getName());

				getBorderPageLineStylePanel().add(getJLabel17(),
						getJLabel17().getName());
				getBorderPageLineStylePanel().add(getLineColorButton(),
						getLineColorButton().getName());
				//                getBorderPageLineStylePanel().add(getLineColorComb(),
				//                        getLineColorComb().getName());

				getBorderPageLineStylePanel().add(getJLineList(),
						getJLineList().getName());
				ivjLineStylePanel.setBorder(new TitledBorder(
						new EtchedBorder(), MultiLang.getString("line_type"), TitledBorder.LEFT,
						TitledBorder.TOP, new java.awt.Font("dialog", 0, 14)));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjLineStylePanel;
	}

	/**
	 * ���� MinusComb ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getMinusComb() {
		if (ivjMinusComb == null) {
			try {
				ivjMinusComb = new UIComboBox();
				ivjMinusComb.setName("MinusComb");
//				ivjMinusComb.setFont(new java.awt.Font("dialog", 0, 14));
				ivjMinusComb.setBounds(90, 146, 178, 27);
				ivjMinusComb.setRenderer(new MinusRender());
				ivjMinusComb.addItem(new Integer(TableConstant.MIN_BLACK1));
				ivjMinusComb.addItem(new Integer(TableConstant.MIN_BLACK2));
				ivjMinusComb.addItem(new Integer(TableConstant.MIN_RED1));
				ivjMinusComb.addItem(new Integer(TableConstant.MIN_RED2));
				ivjMinusComb.addItem(new Integer(TableConstant.MIN_RED3));
				int n = getProperty(PropertyType.MinusFormat);
				if (n == TableConstant.UNDEFINED) {
					n = 0;
				} else if (n == TableConstant.DIFFERENT) {
					n = -1;
					ivjJLabel1.setForeground(Color.gray);
				}

				ivjMinusComb.setSelectedIndex(n);

				ivjMinusComb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						int nIndex = ivjMinusComb.getSelectedIndex();
						//����������jdk1.5��Bug��
						//�����㲻���ߣ���ǰ��ʾ��ѡ�е�Ԫ�����ɺ�ɫ��jdk1.4�Ϳ��ԡ�
						ivjMinusComb.transferFocus();
						setPropertyLater(PropertyType.MinusFormat,nIndex);
						showPreview();
					}
				});

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjMinusComb;
	}

	/**
	 * ���� NoLineButton ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getNoLineButton() {
		if (ivjNoLineButton == null) {
			try {
				ivjNoLineButton = new nc.ui.pub.beans.UIButton();
				ivjNoLineButton.setName("NoLineButton");
//				ivjNoLineButton.setFont(new java.awt.Font("dialog", 0, 12));
				ivjNoLineButton.setText(MultiLang.getString("uiuforep0000781"));//�޿���
				ivjNoLineButton.setBounds(12, 194, 75, 22);
				ivjNoLineButton.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjNoLineButton;
	}

	/**
	 * ���� okButton ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getOkButton() {
		if (ivjOkButton == null) {
			try {
				ivjOkButton = new nc.ui.pub.beans.UIButton();
				ivjOkButton.setName("OkButton");
//				ivjOkButton.setFont(new java.awt.Font("dialog", 0, 14));
				ivjOkButton.setText(MultiLang.getString("uiuforep0000782"));//ȷ��
				ivjOkButton.setBounds(239, 315, 75, 22);
				ivjOkButton.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOkButton;
	}

	/**
	 * ���� OuterLineButton ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 * @i18n miufo00004=�����
	 */
	private javax.swing.JButton getOuterLineButton() {
		if (ivjOuterLineButton == null) {
			try {
				ivjOuterLineButton = new nc.ui.pub.beans.UIButton();
				ivjOuterLineButton.setName("OuterLineButton");
//				ivjOuterLineButton.setFont(new java.awt.Font("dialog", 0, 12));
				ivjOuterLineButton.setText(MultiLang.getString("miufo00004"));//MultiLang.getString("uiuforep0000783"));//��߿�
				ivjOuterLineButton.setBounds(88, 194, 75, 22);
				ivjOuterLineButton.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOuterLineButton;
	}

	/**
	 * ���� PercentCheck ����ֵ��
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getPercentCheck() {
		if (ivjPercentCheck == null) {
			try {
				ivjPercentCheck = new UICheckBox();
				ivjPercentCheck.setName("PercentCheck");
//				ivjPercentCheck.setFont(new java.awt.Font("dialog", 0, 14));
				ivjPercentCheck.setText(MultiLang.getString("uiuforep0000784"));//�ٷֺ�
				ivjPercentCheck.setBounds(90, 19, 67, 26);
				int n = getProperty(PropertyType.Percent);
				if (n == TableConstant.UNDEFINED || n == 0) {
					ivjPercentCheck.setSelected(false);
				} else if (n == 1) {
					ivjPercentCheck.setSelected(true);
				} else if (n == TableConstant.DIFFERENT) {
					ivjPercentCheck.setSelected(true);
					ivjPercentCheck.setForeground(Color.gray);
				}

				ivjPercentCheck.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (ivjChineseComb.getSelectedIndex() != 0
								&& e.getStateChange() == ItemEvent.SELECTED) { //��������Ĵ�Сд��ʽ����ٷֺ�ѡ����Ч
							JOptionPane
									.showMessageDialog(
											CellPropertyDialog.this,
											MultiLang.getString("uiuforep0000785"), //�����Ĵ�Сдѡ�񣬰ٷֺ�ѡ����Ч��
											MultiLang.getString("uiuforep0000786"),//"��ʾ",
											JOptionPane.INFORMATION_MESSAGE);
							ivjPercentCheck.setSelected(false);
							return;
						}
						if (e.getStateChange() == ItemEvent.SELECTED) {
							setPropertyLater(PropertyType.Percent, 1);
							showPreview();
						} else if (e.getStateChange() == ItemEvent.DESELECTED) {
							setPropertyLater(PropertyType.Percent, 0);
							showPreview();
						}
						if (ivjCommaCheck.getSelectedObjects() != null) {
							ivjCommaCheck.setSelected(true);
						}
						ivjPreviewLabel.repaint();
						ivjPercentCheck.repaint();
						ivjPercentCheck.setForeground(Color.black);
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPercentCheck;
	}

	/**
	 * ���� PreviewLabel ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getPreviewLabel() {
		if (ivjPreviewLabel == null) {
			try {
				ivjPreviewLabel = new UILabel();
				ivjPreviewLabel.setName("PreviewLabel");
				ivjPreviewLabel.setText("12345.67");
				ivjPreviewLabel.setForeground(java.awt.Color.black);
//				ivjPreviewLabel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjPreviewLabel.setBounds(8, 12, 263, 42);
				ivjPreviewLabel
						.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPreviewLabel;
	}

	/**
	 * ���� PreviewPanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getDataTypePagePreviewPanel() {
		if (ivjPreviewPanel == null) {
			try {
				ivjPreviewPanel = new UIPanel();
				ivjPreviewPanel.setName("PreviewPanel");
//				ivjPreviewPanel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjPreviewPanel.setLayout(null);
				ivjPreviewPanel.setBounds(129, 197, 280, 66);
				getDataTypePagePreviewPanel().add(getPreviewLabel(),
						getPreviewLabel().getName());
				ivjPreviewPanel.setBorder(new TitledBorder(new EtchedBorder(),
				        MultiLang.getString("uiuforep0000787"),
						TitledBorder.LEFT, TitledBorder.TOP,//Ԥ��
						new java.awt.Font("dialog", 0, 14)));

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPreviewPanel;
	}

	

	/**
	 * ���� SamplePanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getFontPageSamplePanel() {
		if (ivjSamplePanel == null) {
			try {
				ivjSamplePanel = new UIPanel();
				ivjSamplePanel.setName("SamplePanel");
//				ivjSamplePanel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjSamplePanel.setLayout(null);
				ivjSamplePanel.setBounds(5, 159, 400, 103);
				sampleLabel = new LineLabel();

				sampleLabel.setName("SampleLabel");
				sampleLabel
						.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
//				sampleLabel.setFont(new java.awt.Font("dialog", 0, 14));
				sampleLabel.setBounds(15, 18, 370, 78);
				sampleLabel
						.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				sampleLabel.setOpaque(true);
				showSample();

				getFontPageSamplePanel().add(sampleLabel);
				ivjSamplePanel.setBorder(new TitledBorder(new EtchedBorder(),
				        MultiLang.getString("uiuforep0000788"),
						TitledBorder.LEFT, TitledBorder.TOP,//Ԥ��
						new java.awt.Font("dialog", 0, 14)));
			} catch (java.lang.Throwable ivjExc) {
				System.out.println("error in getSamplePanel():"
						+ ivjExc.getMessage());
				handleException(ivjExc);
			}
		}
		return ivjSamplePanel;
	}

	/**
	 * ���� StringLenLable ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getStringLenLable() {
		if (ivjStringLenLable == null) {
			try {
				ivjStringLenLable = new UILabel();
				ivjStringLenLable.setName("StringLenLable");
//				ivjStringLenLable.setFont(new java.awt.Font("dialog", 0, 14));
				ivjStringLenLable.setText(MultiLang.getString("uiuforep0000789"));//�ַ�����
				ivjStringLenLable.setBounds(23, 21, 70, 22);
				ivjStringLenLable.setVisible(false);
				ivjStringLenLable.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjStringLenLable;
	}

	/**
	 * ���� StringLenText ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getStringLenText() {
		if (ivjStringLenText == null) {
			try {
				ivjStringLenText = new UILabel();
				ivjStringLenText.setName("StringLenText");
//				ivjStringLenText.setFont(new java.awt.Font("dialog", 0, 14));
				ivjStringLenText.setText("64");
				ivjStringLenText.setBounds(103, 21, 70, 22);
				ivjStringLenText.setVisible(false);
				ivjStringLenText.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjStringLenText;
	}

	/**
	 * ����������������еĸ�ʽ����
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getDataTypePageFormatPanel() {
		if (ivjStylePanel == null) {
			try {
				ivjStylePanel = new UIPanel();
				ivjStylePanel.setName("StylePanel");
//				ivjStylePanel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjStylePanel.setLayout(null);
				ivjStylePanel.setBounds(129, 6, 279, 186);
//				if(m_bTypeLocked){
//				    ivjStylePanel.setLayout(new BorderLayout());
//				    ivjStylePanel.add(new nc.ui.pub.beans.UILabel( MultiLang.getString("uiuforep0000797")));//��ѡ��������ָ���ؼ��֣������޸ĵ�Ԫ���ͣ�
//				    return ivjStylePanel;
//				}
				ivjStylePanel.add(getCommaCheck(),
						getCommaCheck().getName());
				ivjStylePanel.add(getPercentCheck(),
						getPercentCheck().getName());
				ivjStylePanel.add(getJLabel4(),
						getJLabel4().getName());
				ivjStylePanel.add(getDecimalText(),
						getDecimalText().getName());
				ivjStylePanel.add(getJLabel2(),
						getJLabel2().getName());
				ivjStylePanel.add(getCurCombo(),
						getCurCombo().getName());
				ivjStylePanel.add(getJLabel3(),
						getJLabel3().getName());
				ivjStylePanel.add(getJLabel5(),
						getJLabel5().getName());
				ivjStylePanel.add(getChineseComb(),
						getChineseComb().getName());
				ivjStylePanel.add(getDateTypeCombo(),
						getDateTypeCombo().getName());
				ivjStylePanel.add(getJLabel1(),
						getJLabel1().getName());
				ivjStylePanel.add(getMinusComb(),
						getMinusComb().getName());
				ivjStylePanel.add(getStringLenLable(),
						getStringLenLable().getName());
				ivjStylePanel.add(getStringLenText(),
						getStringLenText().getName());
				ivjStylePanel.setBorder(new TitledBorder(new EtchedBorder(),
				        MultiLang.getString("uiuforep0000790"),
						TitledBorder.LEFT, TitledBorder.TOP,//��ʽ
						new java.awt.Font("dialog", 0, 14)));

				ivjDecimalText.setEditable(false);
				ivjDecimalText.setBorder(new EtchedBorder());

				Rectangle r = ivjDecimalText.getBounds();
				upAB.setBounds(r.x + r.width, r.y, 18, 14);
				downAB.setBounds(r.x + r.width, r.y + 14, 18, 14);
				getDataTypePageFormatPanel().add(upAB);
				getDataTypePageFormatPanel().add(downAB);
				upAB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ivjJLabel4.setForeground(Color.black);
						String text = ivjDecimalText.getText();
						int num = Integer.parseInt(text);
						if (num < 7 && num >= 0) { //С����Ϊ�������������7λ
							num++;
							text = String.valueOf(num);
							ivjDecimalText.setText(text);
							setPropertyLater(PropertyType.DecimalDigits, num);
							showPreview();
						}
					}
				});
				downAB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ivjJLabel4.setForeground(Color.black);
						String text = ivjDecimalText.getText();
						int num = Integer.parseInt(text);
						if (num <= 7 && num > 0) {
							num--;
							text = String.valueOf(num);
							ivjDecimalText.setText(text);
							setPropertyLater(PropertyType.DecimalDigits, num);
							showPreview();
						}
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjStylePanel;
	}

	

	/**
	 * ����ѡ��Ԫ���͵��б�
	 * 
	 * @return javax.swing.JList
	 */
	private javax.swing.JList getTypeList() {
		if (ivjTypeList == null) {
			try {
				ivjTypeList = new UIList();
				ivjTypeList.setName("TypeList");
				ivjTypeList.setBounds(10, 23, 103, 146);
				ivjTypeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				ivjTypeList.setBorder(new BevelBorder(BevelBorder.LOWERED));
				//��Ԫ֧�ֵ����ͣ��μ�TableConstant�б��֧�ֵĵ�Ԫ���͡�
				String typeName[] = {
				        MultiLang.getString("uiuforep0000791"),//��ֵ
				        MultiLang.getString("uiuforep0000792"),//�ַ�
				        MultiLang.getString("uiuforep0000793"),//����
				        MultiLang.getString("uiuforep0001110")};//����
				ivjTypeList.setListData(typeName);
		
				int n = getProperty(PropertyType.DataType);
				if (n == TableConstant.CELLTYPE_NUMBER
						|| n == TableConstant.CELLTYPE_DEFAULT
						|| n == TableConstant.UNDEFINED) {
					ivjTypeList.setSelectedIndex(0);
					ivjDateTypeComb.setVisible(false);
				} else if (n == TableConstant.CELLTYPE_STRING) {
					ivjTypeList.setSelectedIndex(1);
					ivjTypeList.setBackground(Color.white);
					ivjCommaCheck.setVisible(false);
					ivjPercentCheck.setVisible(false);
					ivjJLabel1.setVisible(false);
					ivjJLabel2.setVisible(false);
					ivjJLabel3.setVisible(false);
					ivjJLabel4.setVisible(false);
					ivjJLabel5.setVisible(false);
					ivjDecimalText.setVisible(false);
					ivjCurCombo.setVisible(false);
					ivjChineseComb.setVisible(false);
					ivjDateTypeComb.setVisible(false);
					ivjMinusComb.setVisible(false);
					upAB.setVisible(false);
					downAB.setVisible(false);
					ivjStringLenLable.setVisible(true);
					ivjStringLenText.setVisible(true);
				} else if (n == TableConstant.CELLTYPE_SAMPLE) {
					ivjTypeList.setSelectedIndex(2);
					ivjTypeList.setBackground(Color.white);
					ivjCommaCheck.setVisible(false);
					ivjPercentCheck.setVisible(false);
					ivjJLabel1.setVisible(false);
					ivjJLabel2.setVisible(false);
					ivjJLabel3.setVisible(false);
					ivjJLabel4.setVisible(false);
					ivjJLabel5.setVisible(false);
					ivjDecimalText.setVisible(false);
					ivjCurCombo.setVisible(false);
					ivjChineseComb.setVisible(false);
					ivjDateTypeComb.setVisible(false);
					ivjMinusComb.setVisible(false);
					ivjStringLenLable.setVisible(false);
					ivjStringLenText.setVisible(false);
					upAB.setVisible(false);
					downAB.setVisible(false);
				} else if(n == TableConstant.CELLTYPE_DATE){
					ivjTypeList.setSelectedIndex(3);;
					ivjTypeList.setBackground(Color.white);
					ivjCommaCheck.setVisible(false);
					ivjPercentCheck.setVisible(false);
					ivjJLabel1.setVisible(false);
					ivjJLabel2.setVisible(false);
					ivjJLabel3.setVisible(false);
					ivjJLabel4.setVisible(false);
					ivjJLabel5.setVisible(true);
					ivjDecimalText.setVisible(false);
					ivjCurCombo.setVisible(false);
					ivjChineseComb.setVisible(false);
					ivjDateTypeComb.setVisible(true);
					ivjMinusComb.setVisible(false);
					ivjStringLenLable.setVisible(false);
					ivjStringLenText.setVisible(false);
					upAB.setVisible(false);
					downAB.setVisible(false);
				} else if (n == TableConstant.DIFFERENT) {
					ivjTypeList.setBackground(Color.lightGray);
					ivjCommaCheck.setVisible(false);
					ivjPercentCheck.setVisible(false);
					ivjJLabel1.setVisible(false);
					ivjJLabel2.setVisible(false);
					ivjJLabel3.setVisible(false);
					ivjJLabel4.setVisible(false);
					ivjJLabel5.setVisible(false);
					ivjDecimalText.setVisible(false);
					ivjCurCombo.setVisible(false);
					ivjChineseComb.setVisible(false);
					ivjDateTypeComb.setVisible(false);
					ivjMinusComb.setVisible(false);
					ivjStringLenLable.setVisible(false);
					ivjStringLenText.setVisible(false);
					upAB.setVisible(false);
					downAB.setVisible(false);
				} 

				if(m_bTypeLocked){
					ivjTypeList.setEnabled(false);
					return ivjTypeList;
				}
				ivjTypeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting() == false) {
							int nIndex = ivjTypeList.getSelectedIndex();
							if (nIndex == 0) {
								ivjTypeList.setBackground(Color.white);
								ivjCommaCheck.setVisible(true);
								ivjPercentCheck.setVisible(true);
								ivjJLabel1.setVisible(true);
								ivjJLabel2.setVisible(true);
								ivjJLabel3.setVisible(true);
								ivjJLabel4.setVisible(true);
								ivjJLabel5.setVisible(false);
								ivjDecimalText.setVisible(true);
								ivjCurCombo.setVisible(true);
								ivjChineseComb.setVisible(true);
								ivjDateTypeComb.setVisible(false);
								ivjMinusComb.setVisible(true);
								upAB.setVisible(true);
								downAB.setVisible(true);
								ivjStringLenLable.setVisible(false);
								ivjStringLenText.setVisible(false);
								setPropertyLater(PropertyType.DataType,
										TableConstant.CELLTYPE_NUMBER);
								//�����Ű���Ϣ
							} else if (nIndex == 1) {
								ivjTypeList.setBackground(Color.white);
								ivjCommaCheck.setVisible(false);
								ivjPercentCheck.setVisible(false);
								ivjJLabel1.setVisible(false);
								ivjJLabel2.setVisible(false);
								ivjJLabel3.setVisible(false);
								ivjJLabel4.setVisible(false);
								ivjJLabel5.setVisible(false);
								ivjDecimalText.setVisible(false);
								ivjCurCombo.setVisible(false);
								ivjChineseComb.setVisible(false);
								ivjDateTypeComb.setVisible(false);
								ivjMinusComb.setVisible(false);
								upAB.setVisible(false);
								downAB.setVisible(false);
								ivjStringLenLable.setVisible(true);
								ivjStringLenText.setVisible(true);
								setPropertyLater(PropertyType.DataType,
										TableConstant.CELLTYPE_STRING);
							} else if (nIndex == 2) { //��������
								//��ʱû�пɱ�������,������ʱ����Ϊû�пɱ���.
								boolean m_isVara = false;
								if (!m_isVara) {
									ivjTypeList
											.setBackground(Color.white);
									ivjCommaCheck.setVisible(false);
									ivjPercentCheck.setVisible(false);
									ivjJLabel1.setVisible(false);
									ivjJLabel2.setVisible(false);
									ivjJLabel3.setVisible(false);
									ivjJLabel4.setVisible(false);
									ivjJLabel5.setVisible(false);
									ivjDecimalText.setVisible(false);
									ivjCurCombo.setVisible(false);
									ivjChineseComb.setVisible(false);
									ivjDateTypeComb.setVisible(false);
									ivjMinusComb.setVisible(false);
									ivjStringLenLable.setVisible(false);
									ivjStringLenText.setVisible(false);
									upAB.setVisible(false);
									downAB.setVisible(false);
									setPropertyLater(
											PropertyType.DataType,
											TableConstant.CELLTYPE_SAMPLE);
								} else {
									JOptionPane
											.showMessageDialog(
													CellPropertyDialog.this,
													MultiLang.getString("uiuforep0000798"),//�����а����ɱ������������óɱ������ͣ�
													MultiLang.getString("uiuforep0000799"),//��ʾ
													JOptionPane.INFORMATION_MESSAGE);
									ivjTypeList
											.setBackground(Color.white);
									ivjCommaCheck.setVisible(false);
									ivjPercentCheck.setVisible(false);
									ivjJLabel1.setVisible(false);
									ivjJLabel2.setVisible(false);
									ivjJLabel3.setVisible(false);
									ivjJLabel4.setVisible(false);
									ivjJLabel5.setVisible(false);
									ivjDecimalText.setVisible(false);
									ivjCurCombo.setVisible(false);
									ivjChineseComb.setVisible(false);
									ivjDateTypeComb.setVisible(false);
									ivjMinusComb.setVisible(false);
									ivjStringLenLable.setVisible(false);
									ivjStringLenText.setVisible(false);
									upAB.setVisible(false);
									downAB.setVisible(false);
								}
							} else if (nIndex == 3) {
								ivjTypeList.setBackground(Color.white);
								ivjCommaCheck.setVisible(false);
								ivjPercentCheck.setVisible(false);
								ivjJLabel1.setVisible(false);
								ivjJLabel2.setVisible(false);
								ivjJLabel3.setVisible(false);
								ivjJLabel4.setVisible(false);
								ivjJLabel5.setVisible(true);
								ivjDecimalText.setVisible(false);
								ivjCurCombo.setVisible(false);
								ivjChineseComb.setVisible(false);
								ivjDateTypeComb.setVisible(true);
								ivjMinusComb.setVisible(false);
								upAB.setVisible(false);
								downAB.setVisible(false);
								ivjStringLenLable.setVisible(false);
								ivjStringLenText.setVisible(false);
								setPropertyLater(PropertyType.DataType,
										TableConstant.CELLTYPE_DATE);
							} else if (nIndex == -1) {
								ivjTypeList.setBackground(Color.white);
								ivjCommaCheck.setVisible(false);
								ivjPercentCheck.setVisible(false);
								ivjJLabel1.setVisible(false);
								ivjJLabel2.setVisible(false);
								ivjJLabel3.setVisible(false);
								ivjJLabel4.setVisible(false);
								ivjJLabel5.setVisible(false);
								ivjDecimalText.setVisible(false);
								ivjCurCombo.setVisible(false);
								ivjChineseComb.setVisible(false);
								ivjDateTypeComb.setVisible(false);
								ivjMinusComb.setVisible(false);
								ivjStringLenLable.setVisible(false);
								ivjStringLenText.setVisible(false);
								upAB.setVisible(false);
								downAB.setVisible(false);
							}
						}
						repaint();
					}
				});				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjTypeList;
	}

	/**
	 * ������������ҳ�档
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getDataTypePage() {
		if (ivjTypePagePanel == null) {
			try {
				ivjTypePagePanel = new UIPanel();
				ivjTypePagePanel.setName("TypePagePanel");
//				ivjTypePagePanel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjTypePagePanel.setLayout(null);
				ivjTypePagePanel.add(getDataTypePageFormatPanel(),
						getDataTypePageFormatPanel().getName());
				ivjTypePagePanel.add(getDataTypePagePreviewPanel(),
						getDataTypePagePreviewPanel().getName());
				ivjTypePagePanel.add(getDataTypePageDataTypePanel(),
						getDataTypePageDataTypePanel().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjTypePagePanel;
	}

	/**
	 * ���� TypePanel ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getDataTypePageDataTypePanel() {
		if (ivjTypePanel == null) {
			try {
				ivjTypePanel = new UIPanel();
				ivjTypePanel.setName("TypePanel");
//				ivjTypePanel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjTypePanel.setLayout(null);
				ivjTypePanel.setBounds(3, 6, 123, 186);
				getDataTypePageDataTypePanel().add(getTypeList(),
						getTypeList().getName());
				ivjTypePanel.setBorder(new TitledBorder(new EtchedBorder(),
				        MultiLang.getString("uiuforep0000800"),
						TitledBorder.LEFT, TitledBorder.TOP,//��������
						new java.awt.Font("dialog", 0, 14)));
				showPreview();
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjTypePanel;
	}

	/**
	 * ���� UfoDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getUfoDialogContentPane() {
		if (ivjUfoDialogContentPane == null) {
			try {
				ivjUfoDialogContentPane = new UIPanel();
				ivjUfoDialogContentPane.setName("UfoDialogContentPane");
//				ivjUfoDialogContentPane.setFont(new java.awt.Font("dialog", 0,
//						14));
				ivjUfoDialogContentPane.setLayout(null);
				getUfoDialogContentPane().add(getTabbedPane(),
						getTabbedPane().getName());
				getUfoDialogContentPane().add(getOkButton(),
						getOkButton().getName());
				getUfoDialogContentPane().add(getcancelButton(),
						getcancelButton().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUfoDialogContentPane;
	}

	/**
	 * ���� VAutoRadio ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getVAutoRadio() {
		if (ivjVAutoRadio == null) {
			try {
				ivjVAutoRadio = new UIRadioButton();
				ivjVAutoRadio.setName("VAutoRadio");
//				ivjVAutoRadio.setFont(new java.awt.Font("dialog", 0, 14));
				ivjVAutoRadio.setText(MultiLang.getString("uiuforep0000801"));//�Զ�
				ivjVAutoRadio.setBounds(118, 61, 53, 26);
				ivjVAutoRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjVAutoRadio;
	}

	/**
	 * ���� VCenterRadio ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getVCenterRadio() {
		if (ivjVCenterRadio == null) {
			try {
				ivjVCenterRadio = new UIRadioButton();
				ivjVCenterRadio.setName("VCenterRadio");
//				ivjVCenterRadio.setFont(new java.awt.Font("dialog", 0, 14));
				ivjVCenterRadio.setText(MultiLang.getString("uiuforep0000802"));//����
				ivjVCenterRadio.setBounds(260, 61, 53, 26);
				ivjVCenterRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjVCenterRadio;
	}

	/**
	 * ���� VDownRadio ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getVDownRadio() {
		if (ivjVDownRadio == null) {
			try {
				ivjVDownRadio = new UIRadioButton();
				ivjVDownRadio.setName("VDownRadio");
//				ivjVDownRadio.setFont(new java.awt.Font("dialog", 0, 14));
				ivjVDownRadio.setText(MultiLang.getString("uiuforep0000803"));//����
				ivjVDownRadio.setBounds(331, 61, 53, 26);
				ivjVDownRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjVDownRadio;
	}

	/**
	 * ���� VTopRadio ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getVTopRadio() {
		if (ivjVTopRadio == null) {
			try {
				ivjVTopRadio = new UIRadioButton();
				ivjVTopRadio.setName("VTopRadio");
//				ivjVTopRadio.setFont(new java.awt.Font("dialog", 0, 14));
				ivjVTopRadio.setText(MultiLang.getString("uiuforep0000804"));//����
				ivjVTopRadio.setBounds(189, 61, 53, 26);
				ivjVTopRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjVTopRadio;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		System.out.println(MultiLang.getString("uiuforep0000805"));//---------
		// δ��׽�����쳣
		// ---------
		exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	private void initialize() {
		try {
			setResizable(false);
			setName("CellPropertyDialog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
//			setFont(new java.awt.Font("dialog", 1, 14));
			setSize(450, 400);
			setTitle(MultiLang.getString("uiuforep0000806"));//��Ԫ������
			setContentPane(getUfoDialogContentPane());
			 
			
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		addWindowListener(this);
		initBorderPageBorderPane();
		setLocationRelativeTo(this);
		setModified(false);
	}

	

	/**
	 * �˴����뷽�������� �������ڣ�(2004-5-8 13:38:38)
	 * 
	 * @return boolean
	 */
	public boolean isModified() {
		return m_bModified;
	}

	/**
	 * �ӿ�MouseListener��ʵ�֣�����ͨ��������߿������������Ķ���
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseClicked(java.awt.event.MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		int[] values = new int[2];
		if (ivjJLineList.getSelectedIndex() != -1) {
			values[0] = ivjJLineList.getSelectedIndex();
		} else {
			values[0] = TableConstant.UNDEFINED;
		}
		
		int[] notDefine = {TableConstant.UNDEFINED, TableConstant.UNDEFINED};
		//        Integer color = (Integer) ivjLineColorComb.getSelectedItem();
		//        values[1] = color.intValue();
		values[1] = ivjLineColorButton.getUserColor().getRGB();
		//�ϱ߿�
		if (x > m_nXLeft && x < m_nXRight && y > m_nYTop - 10
				&& y < m_nYTop + 10) {
			int[] curBorder = m_borderPanel.getBorderValue(0);
			if ((curBorder[0] == TableConstant.UNDEFINED)
					|| (curBorder[1] == TableConstant.UNDEFINED)) {
				m_borderPanel.setBorderValue(0, values);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.TOPLINE] = true;
			} else {
				m_borderPanel.setBorderValue(0, notDefine);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.TOPLINE] = true;
			}
		}

		//�±߿�
		if (x > m_nXLeft && x < m_nXRight && y > m_nYBottom - 10
				&& y < m_nYBottom + 10) {
			int[] curBorder = m_borderPanel.getBorderValue(1);
			if ((curBorder[0] == TableConstant.UNDEFINED)
					|| (curBorder[1] == TableConstant.UNDEFINED)) {
				m_borderPanel.setBorderValue(1, values);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.BOTTOMLINE] = true;
			} else {
				m_borderPanel.setBorderValue(1, notDefine);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.BOTTOMLINE] = true;
			}

		}

		//��߿�
		if (x > m_nXLeft - 10 && x < m_nXLeft + 10 && y > m_nYTop
				&& y < m_nYBottom) {
			int[] curBorder = m_borderPanel.getBorderValue(2);
			if ((curBorder[0] == TableConstant.UNDEFINED)
					|| (curBorder[1] == TableConstant.UNDEFINED)) {
				m_borderPanel.setBorderValue(2, values);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.LEFTLINE] = true;
			} else {
				m_borderPanel.setBorderValue(2, notDefine);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.LEFTLINE] = true;
			}
		}

		//�ұ߿�
		if (x > m_nXRight - 10 && x < m_nXRight + 10 && y > m_nYTop
				&& y < m_nYBottom) {
			int[] curBorder = m_borderPanel.getBorderValue(3);
			if ((curBorder[0] == TableConstant.UNDEFINED)
					|| (curBorder[1] == TableConstant.UNDEFINED)) {
				m_borderPanel.setBorderValue(3, values);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.RIGHTLINE] = true;
			} else {
				m_borderPanel.setBorderValue(3, notDefine);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.RIGHTLINE] = true;
			}
		}

		//��߿�
		if (x > m_nXLeft + 10 && x < m_nXRight - 10 && y > m_nYMid - 10
				&& y < m_nYMid + 10) {
			int[] curBorder = m_borderPanel.getBorderValue(4);
			if ((curBorder[0] == TableConstant.UNDEFINED)
					|| (curBorder[1] == TableConstant.UNDEFINED)) {
				m_borderPanel.setBorderValue(4, values);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.HORLINE] = true;
			} else {
				m_borderPanel.setBorderValue(4, notDefine);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.HORLINE] = true;
			}
		}
		
		//���߿�
		if (x > m_nXMid - 10 && x < m_nXMid + 10 && y > m_nYTop + 10
				&& y < m_nYBottom - 10) {
			int[] curBorder = m_borderPanel.getBorderValue(5);
			if ((curBorder[0] == TableConstant.UNDEFINED)
					|| (curBorder[1] == TableConstant.UNDEFINED)) {
				m_borderPanel.setBorderValue(5, values);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.VERLINE] = true;
			} else {
				m_borderPanel.setBorderValue(5, notDefine);
				m_isNeedResetBorder = true;
				m_updatedBorder[Format.VERLINE] = true;
			}
		}
		m_borderPanel.repaint();

	}

	/**
	 * �ӿ�MouseListener��ʵ��
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * �ӿ�MouseListener��ʵ��
	 * 
	 * @param e
	 *            MouseEvent
	 */

	public void mouseExited(MouseEvent e) {
	}

	/**
	 * �ӿ�MouseListener��ʵ��
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mousePressed(java.awt.event.MouseEvent e) {
	}

	/**
	 * �ӿ�MouseListener��ʵ��
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseReleased(java.awt.event.MouseEvent e) {
	}

	/**
	 * �Ի����ʼ��ʱ���߿����Եĳ�ʼ��ʾ���á� �������ڣ�(2001-3-22 19:30:21)
	 */
	private void initBorderPageBorderPane() {
		//7��Ԫ�أ��ֱ����������Һ���,�Խ��ߵ����ͺ���ɫ��TableConstant.UNDEFINEDΪû������ʾ��
		if(m_borderPanel!=null)
		for (int i = 0; i < 8; i++) {
			int[] values = getLineProperty(i);
			m_borderPanel.setBorderValue(i, values);
		}
	}

	/**
	 * �����Ƿ��޸ģ���ʱδʹ�ô˹���
	 * 
	 * @param newModified
	 *            boolean
	 */
	public void setModified(boolean newModified) {
		m_bModified = newModified;
	}

	/**
	 * ����cacheSetProperty��ֵ����֤�����OKǰtable�����Բ��䡣
	 * 
	 * @param nType
	 *            int
	 * @param nValue
	 *            int
	 */
	private void setPropertyLater(int nType, int nValue) {
		if (m_propertyCache.containsKey(new Integer(nType))) {
			m_propertyCache.remove(new Integer(nType));
		}
		m_propertyCache.put(new Integer(nType), new Integer(nValue));
	}

	/**
	 * ��ʾԤ����壬�����������ʱʱ���¡�
	 */
	public void showPreview() {
		String disTextStr = null;
		Object value = null;
		
		//������ʾ��ʽ
		IufoFormat format = new IufoFormat();  
	    format.setDateType(getDateTypeCombo().getSelectedIndex());
		Integer objDataType = m_propertyCache.get(new Integer(PropertyType.DataType));
		if (objDataType != null && objDataType.intValue() == TableConstant.CELLTYPE_DATE){
			Date srcDate = value==null || !(value instanceof Date)?null:(Date)value;
			disTextStr = format.getDateStr(srcDate);
		} else {
		    //����
		    format.setChineseFormat(getChineseComb().getSelectedIndex());	 
		    
		    //�ָ�����
		    if(getCommaCheck().isSelected()){
		        format.setHasComma(1);
		    }else{
		        format.setHasComma(0);
		    }
		    //С��λ��
		    format.setDecimalDigits(Integer.parseInt(getDecimalText().getText()));		    
		    //�ٷֺ�
		    if(getPercentCheck().isSelected()){
		        format.setHasPercent(1);
		    }else{
		        format.setHasPercent(0); 
		    }		
			//���ҷ���
		    format.setCurrencySymbol(getCurCombo().getSelectedIndex());	    
		    //������ʾ
		    int nIndex = getMinusComb().getSelectedIndex();	
		    format.setMinusFormat(nIndex);
		    double dValue;//ʾ����        
		    dValue = -1234567.1234567;   
			//����--��ɫ��ʾ
		    if (format.isMinusRed()) {
		    	getMinusComb().setForeground(Color.red);
		    	getPreviewLabel().setForeground(Color.red);
			} else {
				getMinusComb().setForeground(UIManager.getColor("ComboBox.foreground"));
				getPreviewLabel().setForeground(UIManager.getColor("Lable.foreground"));
			}
		    disTextStr = format.getString(dValue);
		}
		getPreviewLabel().setText(disTextStr);
	}
	/**
	 * ��������ֵ,��ʾSample���.
	 */
	public void showSample() {
		if (sampleLabel == null) {
			return;
		}
		m_strFontName = DefaultSetting.fontNames[ivjFontNameComb
				.getSelectedIndex()];
		m_nFontStyle = this.ivjFontTypeComb.getSelectedIndex();
		switch (m_nFontStyle) {
			case TableConstant.FS_BOLD :
				m_nFontStyle = Font.BOLD;
				break;
			case TableConstant.FS_SLOPE :
				m_nFontStyle = Font.ITALIC;
				break;
			case TableConstant.FS_SLOPE | TableConstant.FS_BOLD :
				m_nFontStyle = Font.BOLD | Font.ITALIC;
				break;
			default :
				m_nFontStyle = Font.PLAIN;
				break;
		}
		m_nFontSize = Integer.parseInt((String) this.ivjFontSizeComb
				.getSelectedItem());
		//        int nColor = ((Integer) (this.ivjForeColorComb.getSelectedItem()))
		//                .intValue();
		m_nForColor = this.ivjForeColorButton.getUserColor();
		//        nColor = ((Integer) (this.ivjBackColorComb.getSelectedItem()))
		//                .intValue();
		m_nBackColor = this.ivjBackColorButton.getUserColor();
		//�����ͼ��
		FillCellIcon icon = new FillCellIcon(false, TableConstant.FillEmpty , 400, 78);
//		Object fillType = ivjFillTypeComb.getSelectedItem();
//		int nFillType = Integer.parseInt((String) fillType);
//		icon.setFillType(nFillType);
		sampleLabel.setIcon(icon);
		sampleLabel.setBackground(m_nBackColor);
	}
	/**
	 * @return �������Ի��档
	 */
	public Hashtable<Integer, Integer> getPropertyCache() {
		return m_propertyCache;
	}
	public void setPropertyCache(Hashtable<Integer, Integer> oldProperty){
		if(oldProperty==null)
			return;
		m_propertyCache=oldProperty;
	}
	/**
	 * ������չ���������ݣ�������ʵ��
	 * @return
	 */
	public Object getPropertyExtended(){
		return null;
	}
	/**
	 * �õ���ǰ���ø�ʽ����ĸ�ʽ���ݡ��Ǳ߿����ԣ��߿����Ե���������
	 * 
	 * @return int �����������õ����ݡ�
	 * @param nType
	 *            int�����Ե�����
	 */
	private int getProperty(int nType) {	
		return PropertyType.getPropertyByType(m_format, nType);
	}
	
	private int[] getLineProperty(int direction) {
        int[] vValue = { TableConstant.UNDEFINED, TableConstant.UNDEFINED };
        
        switch (direction) {
        case Format.TOPLINE:
            vValue[0] = getProperty(PropertyType.TLType);
            vValue[1] = getProperty(PropertyType.TLColor);
            break;
        case Format.BOTTOMLINE:
            vValue[0] = getProperty(PropertyType.BLType);
            vValue[1] = getProperty(PropertyType.BLColor);
            break;
        case Format.LEFTLINE:
            vValue[0] = getProperty(PropertyType.LLType);
            vValue[1] = getProperty(PropertyType.LLColor);
            break;
        case Format.RIGHTLINE:
            vValue[0] = getProperty(PropertyType.RLType);
            vValue[1] = getProperty(PropertyType.RLColor);
            break;
        case Format.HORLINE:
            vValue[0] = getProperty(PropertyType.HLType);
            vValue[1] = getProperty(PropertyType.HLColor);
            break;
        case Format.VERLINE:
            vValue[0] = getProperty(PropertyType.VLType);
            vValue[1] = getProperty(PropertyType.VLColor);
            break;
        case Format.DIAGONAL_LINE:
            vValue[0] = getProperty(PropertyType.DLType);
            vValue[1] = getProperty(PropertyType.DLColor);
            break;
        case Format.DIAGONAL2_LINE:
            vValue[0] = getProperty(PropertyType.D2LType);
            vValue[1] = getProperty(PropertyType.D2LColor);
            break;           
            
        default:
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000776"));//��λ�����Ƿ�
        }
        
        return vValue;
    }

	/**
	 * б�߻��ư�ť 
	 * @i18n miufo00005=б��
	 */
	private JButton getBtnDiagonal() {
		if (btnDiagonal == null) {
			btnDiagonal = new nc.ui.pub.beans.UIButton();
			btnDiagonal.setBounds(new Rectangle(11, 224, 74, 19));
			btnDiagonal.addActionListener(this);
//			if(!m_bSingleSelected){
//				btnDiagonal.setEnabled(false);
//			}
			btnDiagonal.setText(MultiLang.getString("miufo00005"));
		}
		return btnDiagonal;
	}
	
	/**
	 * б�߻��ư�ť 
	 * @i18n miufo00006=��б��
	 */
	private JButton getBtnDiagonal2() {
		if (btnDiagonal2 == null) {
			btnDiagonal2 = new nc.ui.pub.beans.UIButton();
			btnDiagonal2.setBounds(new Rectangle(87, 224, 74, 19));
			btnDiagonal2.addActionListener(this);
//			if(!m_bSingleSelected){
//				btnDiagonal.setEnabled(false);
//			}
			btnDiagonal2.setText(MultiLang.getString("miufo00006"));
		}
		return btnDiagonal2;
	}
	/**
	 * add by guogang 2007-6-7 ������С��������JCheckBox
	 * ���� ShrinkFitCheck ����ֵ��
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getAlignPageShrinkFitCheck() {
		if (ivjShrinkFitCheck == null) {
			try {
				ivjShrinkFitCheck = new UICheckBox();
				ivjShrinkFitCheck.setName("ShrinkFitCheck");
				ivjShrinkFitCheck.setText(MultiLang.getString("uiuforep0001112"));//�����ڵ�Ԫ����С������ʾ
				ivjShrinkFitCheck.setBounds(18, 147, 178, 27);
				int n = getProperty(PropertyType.ShrinkFit);
				if (n == TableConstant.UNDEFINED || n == 0) {
					ivjShrinkFitCheck.setSelected(false);
				} else if (n == 1) {
					ivjShrinkFitCheck.setSelected(true);
					getAlignPageChangeLineCheck().setEnabled(false);
				} else if (n == TableConstant.DIFFERENT) {
					ivjShrinkFitCheck.setSelected(true);
					ivjShrinkFitCheck.setForeground(Color.gray);
				}

				ivjShrinkFitCheck.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							setPropertyLater(PropertyType.ShrinkFit, 1);
							ivjChangeLineCheck.setEnabled(false);
						} else if (e.getStateChange() == ItemEvent.DESELECTED) {
							setPropertyLater(PropertyType.ShrinkFit, 0);
							ivjChangeLineCheck.setEnabled(true);
						}
						ivjShrinkFitCheck.setForeground(Color.black);

					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjShrinkFitCheck;
	}
	
}