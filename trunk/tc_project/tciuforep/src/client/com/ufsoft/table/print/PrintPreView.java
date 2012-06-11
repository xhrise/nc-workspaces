package com.ufsoft.table.print;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.*;

import com.ufsoft.report.sysplugin.print.HeaderFooterModel;
import com.ufsoft.report.sysplugin.print.HeaderFooterSegmentComp;
import com.ufsoft.report.sysplugin.print.HeaderFooterSegmentModel;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.*;

/**
 * <p>
 * Title: �������ʾ��ҪԤ�������ݡ�
 * </p>
 * <p>
 * Description:Ԥ���ʹ�ӡ��ʱ���ǵ��ø����
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0
 */

public class PrintPreView extends JComponent {
	/** ��Ҫ��ʾԤ���Ĳ��ֶ�Ӧ����ϵĳߴ硣 */
	private Rectangle m_ViewRect;
	/** ��Ҫ��ӡ�ı��ؼ� */
	private TablePane m_table;
	/** ��ʾ��ӡԤ���ı��������Ǵ�ӡԤ��ҳ������ű������ñ���������Ĵ�ӡ������˾���Ԥ��ҳ������ı����� */
	private double m_dPreviewScale;
	/** ҳͷҳβ����ģ��*/
	private HeaderFooterModel _headerFooterModel;
	/** ��ӡ��Ԥ����ҳ��*/
	private int _pageIndex;
	/** ��ӡ��Ԥ������ҳ��*/
	private int _totalPageCount;
	/** ҳͷҳβƬ�����*/
	private HeaderFooterSegmentComp[] _headerFooterSegmentComp = new HeaderFooterSegmentComp[6];

	/**
	 * ���캯��
	 * 
	 * @param view
	 *            Rectangle
	 * @param table
	 *            TablePane
	 * @param scale
	 *            double
	 */
	public PrintPreView(Rectangle view, TablePane table, double scale,
			HeaderFooterModel headerFooterModel, int pageIndex,
			int totalPageCount) {
		m_ViewRect = view;
		m_table = table;
		m_dPreviewScale = scale;
		_headerFooterModel = headerFooterModel;
		_pageIndex = pageIndex;
		_totalPageCount = totalPageCount;
		initHeaderFooter();
	}
    /**
     * �ƶ�������������С����Ҫ�Ǹ���layoutManager�����PrintPreview����߿�����ҳͷҳβ�߿�
     * modify by guogang 2007-12-6 ҳü��ҳ�ŵĸ߶�ȷʡΪֽ�ű߾�߶�
     */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		//add by guogang 2007-7-19
		//����ʵ�ʵ�PrintPreview����Ĵ�С���������������Ԥ������
		PageFormat tpf = m_table.getPrintSet().getPageformat();
		double  py = tpf.getHeight(),sy = getHeight()-1;
		m_dPreviewScale=sy/py;
		//add end
		if (_headerFooterModel != null) {
			//add by guogang 2007-7-19
			//���������ҳüҳβ�ı߿����ڴ�ӡ��ʱ�����ȴ�ӡҳ���ٴ�ӡ�����ҳü��ҳ�ţ���ʱ�Ѿ����ù���ӡ���ű�����Ϊ��֤�ڴ�ӡ��ʱ�򣨻�*m_dPreviewScale���õ���PrintPreviewһ���ı߿�
			Rectangle rect=new Rectangle((int)(x/m_dPreviewScale),(int)(y/m_dPreviewScale),(int)(width/m_dPreviewScale),(int)(height/m_dPreviewScale));
			for (int position = HeaderFooterModel.HEADERE_LEFT; position <= HeaderFooterModel.FOOTER_RIGHT; position++) {
				HeaderFooterSegmentComp comp = _headerFooterSegmentComp[position];
				Dimension oldSize=null;
				Dimension preSize=null;
				if (comp != null) {
					//Ԥ��ҳü��ҳ�ŵı߿���丸һ��
					comp.setBounds(rect);
					//����TextPane��Ĭ�ϸ߶�Ϊҳ�߾�ĸ߶�
					oldSize=comp.getTextPane().getPreferredSize();
					comp.getTextPane().setPreferredSize(new Dimension((int)oldSize.getWidth(),position < 3 ? (int)(_headerFooterModel.getHeaderDistance()):(int)(_headerFooterModel.getFooterDistance())));
					preSize=comp.getTextPane().getPreferredSize();
					//����ͼƬ�ĸ߶ȣ�ʹ����TextPane����С��ʾ,��Ҫ�Ǵ�ӡ��ʱ��ʹ��߶�Ϊֽ�ű߾�ĸ߶�
					if(comp.getModel().getImage()!=null&&preSize.getHeight()-comp.getOffset()*2>0&&comp.getModel().getImage().getIconHeight()>(preSize.getHeight()-comp.getOffset()*2)){
						comp.getModel().getImage().setImage(comp.getModel().getImage().getImage().getScaledInstance((int)preSize.getWidth(), (int)(preSize.getHeight()-comp.getOffset()*2), Image.SCALE_SMOOTH));
					}
				}
			}
		}
	}
   /**
    * ��ʼ��ҳͷҳβ�����
    */
	private void initHeaderFooter() {
		// ���6��ҳüҳ����ʾ�������ǰ����С�
		if (_headerFooterModel != null) {
			for (int position = HeaderFooterModel.HEADERE_LEFT; position <= HeaderFooterModel.FOOTER_RIGHT; position++) {
				HeaderFooterSegmentModel segmentModel = _headerFooterModel
						.getHeaderFooterSegmentModel(position);
				if (segmentModel != null && segmentModel.getValue() != null) {
					HeaderFooterSegmentComp comp = new HeaderFooterSegmentComp(
							segmentModel, _pageIndex, _totalPageCount);
					add(comp);
					_headerFooterSegmentComp[position] = comp;
				}
			}
		}
	}

	/**
	 * �õ���������ߴ�
	 */
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * �õ���ǰ�������С�ĳߴ�.
	 * 
	 * @return Dimension
	 */
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * �õ���ǰ�����ȱʡ�ߴ�.���ȼ������ҳ��ĳߴ磬Ȼ������ҳ��ĳߴ�������
	 * 
	 * @return Dimension
	 */
	public Dimension getPreferredSize() {
		PageFormat tpf = m_table.getPrintSet().getPageformat();
		int width = (int) (tpf.getWidth() * m_dPreviewScale);
		int height = (int) (tpf.getHeight() * m_dPreviewScale);
		return new Dimension(width, height);
	}
	
    /*�ϵķ������Է��� modify by guogang 2007-7-19
	private Rectangle getPageRect() {
		Dimension preSize = getPreferredSize();
		Rectangle rect = getBounds();
		int height = preSize.height;
		int width = preSize.width;
		int x = rect.x + (rect.width - width) / 2;
		int y = rect.y + (rect.height - height) / 2;
		return new Rectangle(x, y, width, height);
	}
    */
	/**
	 * ���Ƶ�ǰ����� ��ӡ��Ԥ����ʱ��ͬ���á� 
	 * ����Ļ������Ȼ�õ�ǰ��������������(�ɲ��ֹ���������.)
	 * �õ������ֽ�ŵĴ�С,��ֽ�ŷ�����������������Ȼ���ð�ɫ���ֽ�ŵ�����. �������ӡ���õ���Ч�����Ӧ��λ�ã�����ռ������������.
	 * modify by guogang 2007-7-20 ���Ӵ�ӡ����ҳʱ�Ĵ���
	 * modify by guogang 2007-10-16 ���Ӵ�ӡ���еĴ���
	 * modify by guogang 2007-12-6 ҳü��ҳ�ŵ���Ϊ��ӡ��ֽ�ŵı߾�����
	 * @param g
	 *            Graphics
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (m_ViewRect == null) {
			return;
		}
		
		PageFormat tpf = m_table.getPrintSet().getPageformat();
		PrintSet ps = m_table.getPrintSet();
		
		/*--���и���ӡ������ļ���--*/
		// zzl���**************************************
		com.ufsoft.table.header.HeaderModel rowModel = m_table.getCells()
				.getDataModel().getRowHeaderModel();
		com.ufsoft.table.header.HeaderModel colModel = m_table.getCells()
				.getDataModel().getColumnHeaderModel();
		int crossHeadX=0;
		int crossHeadY=0;
		int crossHeadWidth=0;
		int crossHeadHeight=0;
		
		if(!ps.isPageToOne()){
		int[] nRowHeadRang = new int[] { -1, 0 };
		int[] nColHeadRang = new int[] { -1, 0 };
		if (ps.getRowHeadRang() != null) {
			System.arraycopy(ps.getRowHeadRang(), 0, nRowHeadRang, 0, ps
					.getRowHeadRang().length);
		}
		if (ps.getColHeadRang() != null) {
			System.arraycopy(ps.getColHeadRang(), 0, nColHeadRang, 0, ps
					.getColHeadRang().length);
		}
		if (nRowHeadRang[0] == -1) {
			nRowHeadRang[0] = 0;
		}
		if (nColHeadRang[0] == -1) {
			nColHeadRang[0] = 0;
		}
		// ��ӡ���Ͻ���ͷ����ͷ�ظ�����
		crossHeadX = colModel.getPosition(nColHeadRang[0]);
		crossHeadY = rowModel.getPosition(nRowHeadRang[0]);
		crossHeadWidth = colModel.getPosition(nColHeadRang[1])
				- colModel.getPosition(nColHeadRang[0]);
		crossHeadHeight = rowModel.getPosition(nRowHeadRang[1])
				- rowModel.getPosition(nRowHeadRang[0]);
		
		//add by guogang 2007-6-27
		//û�п��ǹ̶���ͷ���Ǵ�(0,0)��ʼ�����,������ӡ��ҳ�����
		if(nColHeadRang[0]!=0&&_pageIndex==1){
			crossHeadX=0;
			crossHeadWidth=colModel.getPosition(nColHeadRang[1]);
		}
		if(nRowHeadRang[0]!=0&&_pageIndex==1){
			crossHeadY=0;
			crossHeadHeight=colModel.getPosition(nRowHeadRang[1]);
		}
		//add end
		
		}
		/*--����ӡ������ļ������--*/
		/*--����x�����ӡ����--��ҳ��ʱ��m_ViewRect.width����pf.getImageableWidth() / printScale-crossHeadWidth���
		 * ����m_ViewRect.width�Ƚϱ���Ҫ/printScale,�������ŵ���ҳ��ʱ��crossHeadWidth=0,��û�н�������Ĵ���û��Ҫ/printScale*/
		double printScale = m_table.getPrintSet().getPrintScale();
		double xprintScale=printScale;
		double yprintScale=printScale;
		if(ps.isPageToOne()){
            xprintScale=tpf.getImageableWidth()/(crossHeadWidth+m_ViewRect.width);
		if((crossHeadHeight+m_ViewRect.height)>tpf.getImageableHeight()){
			yprintScale=tpf.getImageableHeight()/(crossHeadHeight+m_ViewRect.height);
		}
		}
		/*--������ӡ�������--*/
		/*--��ӡǰԭ�����--*/
		Graphics2D g2 = (Graphics2D) g;
		
		double px = tpf.getWidth(),
        sx = getWidth() - 1,
        py=tpf.getHeight(),
        xoff = 0.5 * (sx - m_dPreviewScale * px),
        yoff = 0;
		//����ͼ��������
		g2.translate( (float) xoff, (float) yoff);
		//����ͼ��������ΪԤ������
        g2.scale( (float) m_dPreviewScale, (float) m_dPreviewScale);
     // �������ӡֽ�Ŷ�Ӧ����Ч����
        Rectangle2D page = new Rectangle2D.Double(0, 0, px, py);
     // ��ҳ��Ļ��Ʋ�������Ϊ��ɫ
        g2.setPaint(Color.white);
        g2.fill(page);
     // ƫ��ֽ�ŵı߾࣬��ô�ӡԭ��
        g2.translate(tpf.getImageableX(), tpf.getImageableY());
        //�����ҳü������ԭ�����
//        if (_headerFooterModel != null&&_headerFooterModel.getHeaderDistance()>0&&!ps.isPageToOne()) {
//        	if((_headerFooterModel.getHeaderDistance()+crossHeadHeight+m_ViewRect.height)*yprintScale>tpf.getImageableHeight()){
//        		return;
//        	}
//        	else{
//        		g2.translate(0,(int) (_headerFooterModel.getHeaderDistance()));
//        	}
//        }
        //��ӡ����û��ռ����ҳ����ӡ����������Ҫ������ƫ����,����m_ViewRect.width��tpf.getImageableWidth()/printScale�󾭹�����õ���,��Ҫ��ԭ
        double cx=0,cy=0;
        if(ps.isHCenter()){
        	if(tpf.getImageableWidth()>(crossHeadWidth+m_ViewRect.width)*xprintScale){
        		cx=0.5*(tpf.getImageableWidth()-(crossHeadWidth+m_ViewRect.width)*xprintScale);
        	}        	
        }
        if(ps.isVCenter()){
        	if((crossHeadHeight+m_ViewRect.height)*yprintScale<tpf.getImageableHeight()){
        		cy=0.5*(tpf.getImageableHeight()-(crossHeadHeight+m_ViewRect.height)*yprintScale);
        	}
        }
        g2.translate((int)cx, (int)cy);
        /*--��ӡǰԭ��������,��ǰԭ��ĵ�������*��ӡ����--*/
        /*--��ӡ��ͷ����ͷ�ظ�����--*/
		if (crossHeadWidth != 0 && crossHeadHeight != 0) {
			Rectangle rowColHeadRect = new Rectangle(crossHeadX, crossHeadY,
					crossHeadWidth, crossHeadHeight);
			m_table.getCells().getUI().print(g2.create(), rowColHeadRect,
					xprintScale,yprintScale);
		}
		/*--��ӡ��ͷ����ͷ�ظ��������--*/
		
		/*--������ӡԭ�㲢��ӡ��������ͷ����--*/
		int dx = (int) (crossHeadWidth * xprintScale);
		g2.translate(dx, 0);
		
		if (crossHeadHeight != 0) {
			Rectangle rowHeadRect = new Rectangle(m_ViewRect.x, crossHeadY,
					m_ViewRect.width, crossHeadHeight);
			m_table.getCells().getUI().print(g2.create(), rowHeadRect,
					xprintScale,yprintScale);
		}
		/*--��ӡ��������ͷ�������--*/
		/*--������ӡԭ�㲢��ӡ��������ͷ����--*/
		int dy = (int) (crossHeadHeight * yprintScale);
		g2.translate(-dx, dy);
		if (crossHeadWidth != 0) {
			Rectangle colHeadRect = new Rectangle(crossHeadX, m_ViewRect.y,
					crossHeadWidth, m_ViewRect.height);
			m_table.getCells().getUI().print(g2.create(), colHeadRect,
					 xprintScale,yprintScale);
		}
		/*--��ӡ��������ͷ�������--*/
		/*--������ӡԭ�㲢��ӡ��ҳ����--*/
		g2.translate(dx, 0);

		m_table.getCells().getUI().print(g.create(), m_ViewRect,
				xprintScale,yprintScale);
		/*--��ӡ��ҳ�������--*/
		/*--ͼ�������ĵ�ԭ��ָ����߽�ԭ��,��ʹ��ӡҳüҳβ��ʱ������ȷ��ԭ��--*/
		g2.translate(-dx, -dy);
		g2.translate(-(int)cx, -(int)cy);
		g2.translate(-(int)xoff, -(int)yoff);
		g2.translate(-tpf.getImageableX(), -tpf.getImageableY());
	}
	/*
	�ϵķ������Ѷ���
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (m_ViewRect == null) {
			return;
		}
		PageFormat tpf = m_table.getPrintSet().getPageformat();
		Rectangle pageRect = getPageRect();
		// ��ҳ��Ļ��Ʋ�������Ϊ��ɫ

		//���ڴ�ӡ��ֽ�ſհ������ұ߱���߿�����⣬��ʱʹ��ƫ���� liuyy. 2007-04-19
		final int offset = 0;
		g.setColor(Color.white);
		g.fillRect(pageRect.x - offset, pageRect.y, pageRect.width, pageRect.height);

		// �������ӡ���õ���Ч�����Ӧ��λ�ã�
		pageRect.x += (int) (tpf.getImageableX() * m_dPreviewScale);// ƫ��ֽ�ŵı߾�
		pageRect.y += (int) (tpf.getImageableY() * m_dPreviewScale);
		g.translate(pageRect.x, pageRect.y);
		//
		float printScale = m_table.getPrintSet().getPrintScale();
		// zzl���**************************************
		com.ufsoft.table.header.HeaderModel rowModel = m_table.getCells()
				.getDataModel().getRowHeaderModel();
		com.ufsoft.table.header.HeaderModel colModel = m_table.getCells()
				.getDataModel().getColumnHeaderModel();
		PrintSet ps = m_table.getPrintSet();
		int[] nRowHeadRang = new int[] { -1, 0 };
		int[] nColHeadRang = new int[] { -1, 0 };
		if (ps.getRowHeadRang() != null) {
			System.arraycopy(ps.getRowHeadRang(), 0, nRowHeadRang, 0, ps
					.getRowHeadRang().length);
		}
		if (ps.getColHeadRang() != null) {
			System.arraycopy(ps.getColHeadRang(), 0, nColHeadRang, 0, ps
					.getColHeadRang().length);
		}
		if (nRowHeadRang[0] == -1) {
			nRowHeadRang[0] = 0;
		}
		if (nColHeadRang[0] == -1) {
			nColHeadRang[0] = 0;
		}
		// ��ӡ���Ͻ���ͷ����ͷ�ظ�����
		int crossHeadX = colModel.getPosition(nColHeadRang[0]);
		int crossHeadY = rowModel.getPosition(nRowHeadRang[0]);
		int crossHeadWidth = colModel.getPosition(nColHeadRang[1])
				- colModel.getPosition(nColHeadRang[0]);
		int crossHeadHeight = rowModel.getPosition(nRowHeadRang[1])
				- rowModel.getPosition(nRowHeadRang[0]);
		
		//add by guogang 2007-6-27
		//û�п��ǹ̶���ͷ���Ǵ�(0,0)��ʼ�����,������ӡ��ҳ�����
		if(nColHeadRang[0]!=0&&_pageIndex==1){
			crossHeadX=0;
			crossHeadWidth=colModel.getPosition(nColHeadRang[1]);
		}
		if(nRowHeadRang[0]!=0&&_pageIndex==1){
			crossHeadY=0;
			crossHeadHeight=colModel.getPosition(nRowHeadRang[1]);
		}
		//add end
		if (crossHeadWidth != 0 && crossHeadHeight != 0) {
			Rectangle rowColHeadRect = new Rectangle(crossHeadX, crossHeadY,
					crossHeadWidth, crossHeadHeight);
			m_table.getCells().getUI().print(g.create(), rowColHeadRect,
					m_dPreviewScale * printScale);
		}
		// ��ӡ��ͷ
		int dx = (int) (crossHeadWidth * m_dPreviewScale * printScale);
		g.translate(dx, 0);
		if (crossHeadHeight != 0) {
			Rectangle rowHeadRect = new Rectangle(m_ViewRect.x, crossHeadY,
					m_ViewRect.width, crossHeadHeight);
			m_table.getCells().getUI().print(g.create(), rowHeadRect,
					m_dPreviewScale * printScale);
		}
		// ��ӡ��ͷ
		int dy = (int) (crossHeadHeight * m_dPreviewScale * printScale);
		g.translate(-dx, dy);
		if (crossHeadWidth != 0) {
			Rectangle colHeadRect = new Rectangle(crossHeadX, m_ViewRect.y,
					crossHeadWidth, m_ViewRect.height);
			m_table.getCells().getUI().print(g.create(), colHeadRect,
					m_dPreviewScale * printScale);
		}
		// Ȼ���ٴ�ӡ��ҳ����
		g.translate(dx, 0);
		// zzl��ӽ���***********************************
		
		m_table.getCells().getUI().print(g.create(), m_ViewRect,
				m_dPreviewScale * printScale);
		g.translate(-dx, -dy);
		g.translate(-pageRect.x + 0, -pageRect.y);
	}
	*/
	
	/**
	 * ���ô�ӡ������ �������ڣ�(2004-5-27 11:02:50)
	 * 
	 * @param newScale
	 *            double
	 */
	public void setScale(double newScale) {
		m_dPreviewScale = newScale;
	}

	/**
	 * ������������Ĳ�����
	 * 
	 * @param previewScale
	 *            ��ӡ����
	 * @param viewRect
	 *            ������Ƶ�����
	 */
	public void reset(double previewScale, Rectangle viewRect) {
		m_dPreviewScale = previewScale;
		m_ViewRect = viewRect;
		revalidate();
		repaint();
	}
}