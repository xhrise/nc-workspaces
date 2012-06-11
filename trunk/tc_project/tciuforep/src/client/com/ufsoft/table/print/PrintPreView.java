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
 * Title: 该组件显示需要预览的内容。
 * </p>
 * <p>
 * Description:预览和打印的时候都是调用该组件
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
	/** 需要显示预览的部分对应组件上的尺寸。 */
	private Rectangle m_ViewRect;
	/** 需要打印的表格控件 */
	private TablePane m_table;
	/** 显示打印预览的比例。就是打印预览页面的缩放比例。该比例与组件的打印比例相乘就是预览页面输出的比例。 */
	private double m_dPreviewScale;
	/** 页头页尾数据模型*/
	private HeaderFooterModel _headerFooterModel;
	/** 打印或预览的页号*/
	private int _pageIndex;
	/** 打印或预览的总页数*/
	private int _totalPageCount;
	/** 页头页尾片段组件*/
	private HeaderFooterSegmentComp[] _headerFooterSegmentComp = new HeaderFooterSegmentComp[6];

	/**
	 * 构造函数
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
     * 移动组件并调整其大小，主要是根据layoutManager计算的PrintPreview组件边框设置页头页尾边框
     * modify by guogang 2007-12-6 页眉、页脚的高度确省为纸张边距高度
     */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		//add by guogang 2007-7-19
		//根据实际的PrintPreview组件的大小重新修正该组件的预览比例
		PageFormat tpf = m_table.getPrintSet().getPageformat();
		double  py = tpf.getHeight(),sy = getHeight()-1;
		m_dPreviewScale=sy/py;
		//add end
		if (_headerFooterModel != null) {
			//add by guogang 2007-7-19
			//设置子组件页眉页尾的边框，由于打印的时候是先打印页面再打印子组件页眉、页脚，此时已经设置过打印缩放比例，为保证在打印的时候（会*m_dPreviewScale）得到和PrintPreview一样的边框
			Rectangle rect=new Rectangle((int)(x/m_dPreviewScale),(int)(y/m_dPreviewScale),(int)(width/m_dPreviewScale),(int)(height/m_dPreviewScale));
			for (int position = HeaderFooterModel.HEADERE_LEFT; position <= HeaderFooterModel.FOOTER_RIGHT; position++) {
				HeaderFooterSegmentComp comp = _headerFooterSegmentComp[position];
				Dimension oldSize=null;
				Dimension preSize=null;
				if (comp != null) {
					//预设页眉、页脚的边框和其父一样
					comp.setBounds(rect);
					//修正TextPane的默认高度为页边距的高度
					oldSize=comp.getTextPane().getPreferredSize();
					comp.getTextPane().setPreferredSize(new Dimension((int)oldSize.getWidth(),position < 3 ? (int)(_headerFooterModel.getHeaderDistance()):(int)(_headerFooterModel.getFooterDistance())));
					preSize=comp.getTextPane().getPreferredSize();
					//调整图片的高度，使其在TextPane中缩小显示,主要是打印的时候使其高度为纸张边距的高度
					if(comp.getModel().getImage()!=null&&preSize.getHeight()-comp.getOffset()*2>0&&comp.getModel().getImage().getIconHeight()>(preSize.getHeight()-comp.getOffset()*2)){
						comp.getModel().getImage().setImage(comp.getModel().getImage().getImage().getScaledInstance((int)preSize.getWidth(), (int)(preSize.getHeight()-comp.getOffset()*2), Image.SCALE_SMOOTH));
					}
				}
			}
		}
	}
   /**
    * 初始化页头页尾各组件
    */
	private void initHeaderFooter() {
		// 添加6个页眉页脚显示组件到当前组件中。
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
	 * 得到组件的最大尺寸
	 */
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * 得到当前组件的最小的尺寸.
	 * 
	 * @return Dimension
	 */
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * 得到当前组件的缺省尺寸.首先计算出来页面的尺寸，然后利用页面的尺寸来设置
	 * 
	 * @return Dimension
	 */
	public Dimension getPreferredSize() {
		PageFormat tpf = m_table.getPrintSet().getPageformat();
		int width = (int) (tpf.getWidth() * m_dPreviewScale);
		int height = (int) (tpf.getHeight() * m_dPreviewScale);
		return new Dimension(width, height);
	}
	
    /*老的方法，以废弃 modify by guogang 2007-7-19
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
	 * 绘制当前组件， 打印或预览的时候共同调用。 
	 * 组件的绘制首先获得当前组件被分配的区域(由布局管理器分配.)
	 * 得到输出的纸张的大小,将纸张放置在组件区域的中央然后用白色填充纸张的区域. 计算出打印设置的有效区域对应的位置，这个空间用来绘制组件.
	 * modify by guogang 2007-7-20 增加打印到单页时的处理
	 * modify by guogang 2007-10-16 增加打印居中的处理
	 * modify by guogang 2007-12-6 页眉、页脚调整为打印在纸张的边距区域
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
		
		/*--进行各打印各区域的计算--*/
		// zzl添加**************************************
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
		// 打印左上角行头和列头重复区域
		crossHeadX = colModel.getPosition(nColHeadRang[0]);
		crossHeadY = rowModel.getPosition(nRowHeadRang[0]);
		crossHeadWidth = colModel.getPosition(nColHeadRang[1])
				- colModel.getPosition(nColHeadRang[0]);
		crossHeadHeight = rowModel.getPosition(nRowHeadRang[1])
				- rowModel.getPosition(nRowHeadRang[0]);
		
		//add by guogang 2007-6-27
		//没有考虑固定表头不是从(0,0)开始的情况,修正打印首页的情况
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
		/*--各打印各区域的计算完成--*/
		/*--调整x方向打印比例--分页的时候m_ViewRect.width是由pf.getImageableWidth() / printScale-crossHeadWidth获得
		 * 故与m_ViewRect.width比较必须要/printScale,但是缩放到单页的时候crossHeadWidth=0,并没有进行上面的处理，没必要/printScale*/
		double printScale = m_table.getPrintSet().getPrintScale();
		double xprintScale=printScale;
		double yprintScale=printScale;
		if(ps.isPageToOne()){
            xprintScale=tpf.getImageableWidth()/(crossHeadWidth+m_ViewRect.width);
		if((crossHeadHeight+m_ViewRect.height)>tpf.getImageableHeight()){
			yprintScale=tpf.getImageableHeight()/(crossHeadHeight+m_ViewRect.height);
		}
		}
		/*--调整打印比例完成--*/
		/*--打印前原点调整--*/
		Graphics2D g2 = (Graphics2D) g;
		
		double px = tpf.getWidth(),
        sx = getWidth() - 1,
        py=tpf.getHeight(),
        xoff = 0.5 * (sx - m_dPreviewScale * px),
        yoff = 0;
		//调整图形上下文
		g2.translate( (float) xoff, (float) yoff);
		//设置图形上下文为预览比例
        g2.scale( (float) m_dPreviewScale, (float) m_dPreviewScale);
     // 计算出打印纸张对应的有效区域
        Rectangle2D page = new Rectangle2D.Double(0, 0, px, py);
     // 将页面的绘制部分设置为白色
        g2.setPaint(Color.white);
        g2.fill(page);
     // 偏移纸张的边距，获得打印原点
        g2.translate(tpf.getImageableX(), tpf.getImageableY());
        //如果有页眉，进行原点调整
//        if (_headerFooterModel != null&&_headerFooterModel.getHeaderDistance()>0&&!ps.isPageToOne()) {
//        	if((_headerFooterModel.getHeaderDistance()+crossHeadHeight+m_ViewRect.height)*yprintScale>tpf.getImageableHeight()){
//        		return;
//        	}
//        	else{
//        		g2.translate(0,(int) (_headerFooterModel.getHeaderDistance()));
//        	}
//        }
        //打印区域没有占满整页，打印居中设置需要调整的偏移量,由于m_ViewRect.width是tpf.getImageableWidth()/printScale后经过计算得到的,故要还原
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
        /*--打印前原点调整完成,此前原点的调整不用*打印比例--*/
        /*--打印行头和列头重复区域--*/
		if (crossHeadWidth != 0 && crossHeadHeight != 0) {
			Rectangle rowColHeadRect = new Rectangle(crossHeadX, crossHeadY,
					crossHeadWidth, crossHeadHeight);
			m_table.getCells().getUI().print(g2.create(), rowColHeadRect,
					xprintScale,yprintScale);
		}
		/*--打印行头和列头重复区域完成--*/
		
		/*--调整打印原点并打印其他的行头部分--*/
		int dx = (int) (crossHeadWidth * xprintScale);
		g2.translate(dx, 0);
		
		if (crossHeadHeight != 0) {
			Rectangle rowHeadRect = new Rectangle(m_ViewRect.x, crossHeadY,
					m_ViewRect.width, crossHeadHeight);
			m_table.getCells().getUI().print(g2.create(), rowHeadRect,
					xprintScale,yprintScale);
		}
		/*--打印其他的行头部分完成--*/
		/*--调整打印原点并打印其他的列头部分--*/
		int dy = (int) (crossHeadHeight * yprintScale);
		g2.translate(-dx, dy);
		if (crossHeadWidth != 0) {
			Rectangle colHeadRect = new Rectangle(crossHeadX, m_ViewRect.y,
					crossHeadWidth, m_ViewRect.height);
			m_table.getCells().getUI().print(g2.create(), colHeadRect,
					 xprintScale,yprintScale);
		}
		/*--打印其他的列头部分完成--*/
		/*--调整打印原点并打印本页内容--*/
		g2.translate(dx, 0);

		m_table.getCells().getUI().print(g.create(), m_ViewRect,
				xprintScale,yprintScale);
		/*--打印本页内容完成--*/
		/*--图形上下文的原点恢复到边界原点,以使打印页眉页尾的时候获得正确的原点--*/
		g2.translate(-dx, -dy);
		g2.translate(-(int)cx, -(int)cy);
		g2.translate(-(int)xoff, -(int)yoff);
		g2.translate(-tpf.getImageableX(), -tpf.getImageableY());
	}
	/*
	老的方法，已丢弃
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (m_ViewRect == null) {
			return;
		}
		PageFormat tpf = m_table.getPrintSet().getPageformat();
		Rectangle pageRect = getPageRect();
		// 将页面的绘制部分设置为白色

		//存在打印后纸张空白区域右边比左边宽的问题，临时使用偏移量 liuyy. 2007-04-19
		final int offset = 0;
		g.setColor(Color.white);
		g.fillRect(pageRect.x - offset, pageRect.y, pageRect.width, pageRect.height);

		// 计算出打印设置的有效区域对应的位置，
		pageRect.x += (int) (tpf.getImageableX() * m_dPreviewScale);// 偏移纸张的边距
		pageRect.y += (int) (tpf.getImageableY() * m_dPreviewScale);
		g.translate(pageRect.x, pageRect.y);
		//
		float printScale = m_table.getPrintSet().getPrintScale();
		// zzl添加**************************************
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
		// 打印左上角行头和列头重复区域
		int crossHeadX = colModel.getPosition(nColHeadRang[0]);
		int crossHeadY = rowModel.getPosition(nRowHeadRang[0]);
		int crossHeadWidth = colModel.getPosition(nColHeadRang[1])
				- colModel.getPosition(nColHeadRang[0]);
		int crossHeadHeight = rowModel.getPosition(nRowHeadRang[1])
				- rowModel.getPosition(nRowHeadRang[0]);
		
		//add by guogang 2007-6-27
		//没有考虑固定表头不是从(0,0)开始的情况,修正打印首页的情况
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
		// 打印行头
		int dx = (int) (crossHeadWidth * m_dPreviewScale * printScale);
		g.translate(dx, 0);
		if (crossHeadHeight != 0) {
			Rectangle rowHeadRect = new Rectangle(m_ViewRect.x, crossHeadY,
					m_ViewRect.width, crossHeadHeight);
			m_table.getCells().getUI().print(g.create(), rowHeadRect,
					m_dPreviewScale * printScale);
		}
		// 打印列头
		int dy = (int) (crossHeadHeight * m_dPreviewScale * printScale);
		g.translate(-dx, dy);
		if (crossHeadWidth != 0) {
			Rectangle colHeadRect = new Rectangle(crossHeadX, m_ViewRect.y,
					crossHeadWidth, m_ViewRect.height);
			m_table.getCells().getUI().print(g.create(), colHeadRect,
					m_dPreviewScale * printScale);
		}
		// 然后再打印本页内容
		g.translate(dx, 0);
		// zzl添加结束***********************************
		
		m_table.getCells().getUI().print(g.create(), m_ViewRect,
				m_dPreviewScale * printScale);
		g.translate(-dx, -dy);
		g.translate(-pageRect.x + 0, -pageRect.y);
	}
	*/
	
	/**
	 * 设置打印比例。 创建日期：(2004-5-27 11:02:50)
	 * 
	 * @param newScale
	 *            double
	 */
	public void setScale(double newScale) {
		m_dPreviewScale = newScale;
	}

	/**
	 * 重新设置组件的参数。
	 * 
	 * @param previewScale
	 *            打印比例
	 * @param viewRect
	 *            组件绘制的区域
	 */
	public void reset(double previewScale, Rectangle viewRect) {
		m_dPreviewScale = previewScale;
		m_ViewRect = viewRect;
		revalidate();
		repaint();
	}
}