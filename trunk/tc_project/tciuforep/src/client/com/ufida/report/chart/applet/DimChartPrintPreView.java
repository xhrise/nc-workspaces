package com.ufida.report.chart.applet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;

import javax.swing.JComponent;

import com.ufsoft.report.sysplugin.print.HeaderFooterModel;
import com.ufsoft.report.sysplugin.print.HeaderFooterSegmentComp;
import com.ufsoft.report.sysplugin.print.HeaderFooterSegmentModel;
import com.ufsoft.table.print.PrintSet;

public class DimChartPrintPreView extends JComponent {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	private Rectangle m_ViewRect;
	private DimChartPanel m_chartPane=null;
	/** ��ʾ��ӡԤ���ı��������Ǵ�ӡԤ��ҳ������ű������ñ���������Ĵ�ӡ������˾���Ԥ��ҳ������ı����� */
	private double m_dPreviewScale;
	private HeaderFooterModel _headerFooterModel;
	private int _pageIndex;
	private int _totalPageCount;
	private HeaderFooterSegmentComp[] _headerFooterSegmentComp = new HeaderFooterSegmentComp[6];
	private PrintSet m_printSet=null;

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
	public DimChartPrintPreView( DimChartPanel chartPane,PrintSet printSet,double previewScale,int pageIndex,int totalPageCount) {
//		m_ViewRect=rect;
		m_chartPane=chartPane;
		m_dPreviewScale = previewScale;
		_headerFooterModel = printSet==null?null:printSet.getHeaderFooterModel();
		_pageIndex = pageIndex;
		_totalPageCount = totalPageCount;
		m_printSet=printSet;
		initHeaderFooter();
	}
    public void setBounds(int x, int y, int width, int height) {
    	super.setBounds(x,y,width,height);
		if(_headerFooterModel != null){
			Rectangle headerRect = getPageRect();
			headerRect.y += 	(int)(_headerFooterModel.getHeaderDistance()*m_dPreviewScale);
			headerRect.height -= headerRect.y;
			Rectangle footerRect = getPageRect();
			footerRect.height -= (int)(_headerFooterModel.getFooterDistance()*m_dPreviewScale);
			
			for(int position=HeaderFooterModel.HEADERE_LEFT;position<=HeaderFooterModel.FOOTER_RIGHT;position++){
				HeaderFooterSegmentComp comp = _headerFooterSegmentComp[position];
				if(comp != null){
					comp.setBounds(position < 3 ? headerRect : footerRect);
				}
			}
		}
    }
	private void initHeaderFooter() {
		//���6��ҳüҳ����ʾ�������ǰ����С�
		if(_headerFooterModel != null){
			for(int position=HeaderFooterModel.HEADERE_LEFT;position<=HeaderFooterModel.FOOTER_RIGHT;position++){
				HeaderFooterSegmentModel segmentModel = _headerFooterModel.getHeaderFooterSegmentModel(position);
				if(segmentModel != null && segmentModel.getValue() != null){
					HeaderFooterSegmentComp comp = new HeaderFooterSegmentComp(segmentModel,_pageIndex,_totalPageCount);
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
		PageFormat tpf = getPrintSet().getPageformat();
		int width = (int) (tpf.getWidth() * m_dPreviewScale);
		int height = (int) (tpf.getHeight() * m_dPreviewScale);
		return new Dimension(width, height);
	}
	private Rectangle getPageRect(){
		Dimension preSize = getPreferredSize();
		Rectangle rect = getBounds();
		int height = preSize.height;
		int width = preSize.width;
		int x = rect.x + (rect.width - width) / 2;
		int y = rect.y + (rect.height - height) / 2;
		return new Rectangle(x,y,width,height);
	}
	/**
	 * ���Ƶ�ǰ����� ����Ļ������Ȼ�õ�ǰ��������������(�ɲ��ֹ���������.)
	 * �õ������ֽ�ŵĴ�С,��ֽ�ŷ�����������������Ȼ���ð�ɫ���ֽ�ŵ�����. �������ӡ���õ���Ч�����Ӧ��λ�ã�����ռ������������.
	 * 
	 * @param g
	 *            Graphics
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

        PageFormat tpf = getPrintSet().getPageformat();
        Rectangle pageRect = getPageRect();
		//    ��ҳ��Ļ��Ʋ�������Ϊ��ɫ

		g.setColor(Color.white);
		g.fillRect(pageRect.x, pageRect.y, pageRect.width, pageRect.height);
		
		//    �������ӡ���õ���Ч�����Ӧ��λ�ã�
		double dOffX=tpf.getImageableX() * (double)0.5;

		
		pageRect.x += (int) (dOffX* m_dPreviewScale);//ƫ��ֽ�ŵı߾�
		pageRect.y += (int) (tpf.getImageableY() * m_dPreviewScale);
		g.translate(pageRect.x, pageRect.y);
		//
		double printScale =getPrintSet().getPrintScale();

		if(m_chartPane!=null)
			m_chartPane.print(g.create(), getRect(), m_dPreviewScale * printScale);
		

		g.translate(-pageRect.x, -pageRect.y);
	}
	/**
	 * ���ô�ӡ������ �������ڣ�(2004-5-27 11:02:50)
	 * 
	 * @param newScale
	 *            double
	 */
	public void setScale(double newScale) {
		m_dPreviewScale = newScale;
	}

	private PrintSet getPrintSet(){
		return m_printSet;
	}
	private Rectangle2D getRect(){

		double dWidthScale=1.2;//
		PageFormat pf=getPrintSet().getPageformat();
		double w =  (pf.getImageableWidth() )*dWidthScale;
		double h = 0;

		if(m_chartPane!=null){
			Dimension size=m_chartPane.getSize();
			double dScale=size.getWidth()/size.getHeight();
			h=w/dScale;
		}
		else
			 h=(pf.getImageableHeight());
		
		//TODO ȷ�ϴ�ӡͼ������
		return new Rectangle2D.Double(0, 0, w, h);
	}
	
}