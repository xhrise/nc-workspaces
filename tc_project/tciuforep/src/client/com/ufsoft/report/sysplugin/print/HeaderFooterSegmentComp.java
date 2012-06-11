package com.ufsoft.report.sysplugin.print;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import nc.ui.pub.beans.UIFrame;
import com.ufsoft.report.util.MultiLang;

/**
 * ÿ��Ƭ�ε������ʾ��
 * 
 * @author zzl
 */
public class HeaderFooterSegmentComp extends nc.ui.pub.beans.UIPanel {
	private static final long serialVersionUID = -3598272458263950645L;
	private HeaderFooterSegmentModel _model;
	private JTextPane _textPane = new JTextPane();

	/**
	 * @i18n report00063=&[ͼƬ]
	 */
	public static final String IMAGE_TEXT = MultiLang.getString("report00063");
	/**
	 * @i18n report00064=&[ҳ��]
	 */
	public static final String PAGENUMBER_TEXT = MultiLang.getString("report00064");
	/**
	 * @i18n report00065=&[��ҳ��]
	 */
	public static final String TOTALPAGENUMBER_TEXT = MultiLang.getString("report00065");
	/**
	 * @i18n report00066=&[����]
	 */
	public static final String DATE_TEXT = MultiLang.getString("report00066");
	/**
	 * @i18n report00067=&[ʱ��]
	 */
	public static final String TIME_TEXT = MultiLang.getString("report00067");

	private int _pageNumber = -1;
	private int _totalPageNumber = -1;
	private final int offset = 8; //����ҳüҳ�Ŵ�ӡ��ȫ���⣬ ΢��һ�¾��� liuyy. 2007-04-19

	public HeaderFooterSegmentComp(HeaderFooterSegmentModel model,
			int pageIndex, int pageTotalCount) {
		super();
		_model = model;
		_pageNumber = pageIndex;
		_totalPageNumber = pageTotalCount;
		init();
		setOpaque(false);
	}

	private void init() {
		Object[] comps = parseValue();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof String) {
				SimpleAttributeSet attrSet = new SimpleAttributeSet();
				Font font = getModel().getFont();
				if (font != null) {
					StyleConstants.setFontFamily(attrSet, font.getFontName());
					StyleConstants.setFontSize(attrSet, font.getSize());
					StyleConstants.setBold(attrSet, font.isBold());
					StyleConstants.setItalic(attrSet, font.isItalic());
				}
				try {
					Document doc = getTextPane().getDocument();
					getTextPane().getDocument().insertString(doc.getLength(),
							(String) comps[i], attrSet);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else if (comps[i] instanceof Icon) {
				getTextPane().insertIcon((Icon) comps[i]);
			} else {
				throw new IllegalArgumentException();
			}

		}
		setLayout(null);
		getTextPane().setOpaque(false);
		getTextPane().setEditable(false);
		add(getTextPane());
	}
    /**
     * ҳü��ҳ�ŵĻ��ƣ�����Ԥ�����á���ӡ���á���ӡԤ������(ÿ�λ�������bounds,��ӡ��ʱ����丸PrintPreview��ͬ)
     * ����֮ǰҪ����TextPane����ı߿�ʹ������ȷ��ӡ
     * 
     */
	protected void paintComponent(Graphics g) {
		Point startPoint = getStartPoint();
		Dimension preSize=getTextPane().getPreferredSize();
		getTextPane().setBounds(startPoint.x, startPoint.y, (int)preSize.getWidth(), (int)preSize.getHeight());
		super.paintComponent(g);
	}
	
    public int getOffset() {
		return offset;
	}

	/**
     * ��boundsӰ���ӡЧ��,����ֵ����HeaderFooterSegmentComp.getBounds()��JTextPane.getPreferredSize()��������
     * @return
     */
	public JTextPane getTextPane() {
		return _textPane;
	}

	public static void main(String[] args) {
		ImageIcon image = new ImageIcon("c:/31.jpg");
		HeaderFooterSegmentModel model = new HeaderFooterSegmentModel();
		model.setPosition(HeaderFooterModel.HEADERE_MIDDLE);
		model.setImage(image);
		model.setValue("leftleft" + IMAGE_TEXT + "rightright");
		HeaderFooterSegmentComp comp = new HeaderFooterSegmentComp(model, 1, 1);
		JFrame f = new UIFrame();
		f.setBounds(5, 5, 100, 100);
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(comp, BorderLayout.CENTER);
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}

	/**
	 * ����model�е�value�����ɶ�������
	 * 
	 * @return JComponent ����ImageIcon
	 */
	private Object[] parseValue() {
		// �������ͼƬ����ı�ǡ�
		String value = getModel().getValue();
		value = value.replaceAll(convertStr(DATE_TEXT), new SimpleDateFormat(
				"yyyy-MM-dd").format(new Date()));
		value = value.replaceAll(convertStr(TIME_TEXT), new SimpleDateFormat(
				"HH:mm:ss").format(new Date()));
		value = value.replaceAll(convertStr(PAGENUMBER_TEXT), "" + _pageNumber);
		value = value.replaceAll(convertStr(TOTALPAGENUMBER_TEXT), ""
				+ _totalPageNumber);
		// ����ͼƬ��ǡ�ֻ�����һ�����ֵ�λ�á�
		int imageIndex = value.indexOf(IMAGE_TEXT);
		if (imageIndex >= 0 && getModel().getImage() != null) {
			String value1 = value.substring(0, imageIndex);
			ImageIcon comp2 = getModel().getImage();
			String value3 = value.substring(imageIndex + IMAGE_TEXT.length());
			return new Object[] { value1, comp2, value3 };
		} else {
			return new Object[] { value };
		}
	}

	private static String convertStr(String textToken) {
		return textToken.substring(0, 1) + "\\" + textToken.substring(1);
	}

	public HeaderFooterSegmentModel getModel() {
		return _model;
	}

	// public void setPageNumber(int pageNumber){
	// _pageNumber = pageNumber;
	// }
	// public void setTotalPageNumber(int totalPageNumber){
	// _totalPageNumber = totalPageNumber;
	// }
	
	/**
	 * ����Ԥ����ʱ��getTextPane().getPreferredSize()�������ݵ�ʵ�ʴ�С������ӡ��ʱ���������߶�Ϊֽ�ű߾�ĸ߶�
	 */
	private Point getStartPoint() {
		Rectangle bounds = getBounds();
		Dimension textPanePreferredSize = getTextPane().getPreferredSize();

		int dx = -1, dy = -1;
		if (getModel().isLeft()) {
			dx = offset;
		} else if (getModel().isRight()) {
			dx = bounds.width - textPanePreferredSize.width - offset;
		} else if (getModel().isMiddle()) {
			dx = (bounds.width - textPanePreferredSize.width) / 2;
		}
		if (dx < 0) {
			dx = 0;
		}
		if (getModel().isHeaderNotFooter()) {
			dy = offset;
		} else {
			dy = bounds.height - textPanePreferredSize.height-offset;
		}
		return new Point(dx, dy);
	}
}
 