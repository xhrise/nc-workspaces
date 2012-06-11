package com.ufsoft.table.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.PageFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.print.PrintSettingDlg;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.TablePane;

/**
 * <p>Title:打印预览的对话框。 </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0
 */

public class PrintPreviewDialog extends UfoDialog {

	private static final long serialVersionUID = 1067045169041491077L;

	/**支持多页预览的显示风格。*/
	private final int[][] MUTI_PAGE = { { 1, 2 }, { 1, 3 }, { 2, 2 }, { 2, 3 } };

	//在打印设置下，需要打印内容的分页范围。
	private Rectangle[] viewRects = null;
	private final int SINGLE_PAGE = -1;

	/**记录多页显示风格所使用的具体是哪一种.-1表示当前只显示选择的页显示预览*/
	private int m_nMutiFormatIndex = SINGLE_PAGE;
	private TablePane table;
	/**当前预览的页面的索引，如果是多页，是首页索引*/
	private int m_nCurPageIndex=1;
	/**预览面板显示的比例。*/
	double m_nPrintScale = 1.0;
	boolean fitWindow = false;

	//×××××××××××××××构造显示界面需要的属性×××××××××
	//  ResourceBundle res = ResourceBundle.getBundle("com.ufsoft.table.print.Res");
	JPanel mainPane = new UIPanel();
	/**容纳按钮的面板*/
	JPanel btnPanel = new UIPanel();

	JButton m_btPageSet = new UIButton(MultiLang.getString("PagePrintSet"));
	JButton m_btPrint = new UIButton(MultiLang.getString("print"));
//	JButton m_btZoom = new UIButton(MultiLang.getString("Zoom"));
	JButton m_btLastPage = new UIButton(MultiLang.getString("LastPage"));
	JButton m_btNextPage = new UIButton(MultiLang.getString("NextPage"));
	JButton m_btClose = new UIButton(MultiLang.getString("close"));
	//  JButton m_btAreaSet = new UIButton(MultiLang.getString("PrintArea"));
	JButton m_btPrintSetting = new UIButton(MultiLang.getString("miufo00008"));
	JLabel m_lbPage = new JLabel();
	/**容纳需要预览的组件的面板*/
	JScrollPane scrollPane = new UIScrollPane();
	private int previewPageHeight;
	private int pageNumber=1;
	//modify by guogang 2007-7-17 缩放比例下拉框
	/**
	 * @i18n report00016=页面宽度
	 * @i18n report00017=文字宽度
	 * @i18n report00018=整页
	 * @i18n report00019=双页
	 */
	JComboBox boxResize = new UIComboBox(
	        new String[] {
	        "500%", "250%", "150%", "100%", "75%",
	        "50%", "25%", "10%",
	        MultiLang.getString("report00016"), MultiLang.getString("report00017"), MultiLang.getString("report00018"), MultiLang.getString("report00019")
	    });
	/**容纳所有需要显示的页面的面板，使用一个GridLayout的布局管理器。*/
	private JPanel printPane = new UIPanel(new FlowLayout());
	
//	private JPanel printPane = new UIPanel();
	/**
	 * 容纳预览表页的布局管理器
	 */
	private BoxLayout boxLayout=new BoxLayout(printPane, BoxLayout.PAGE_AXIS);	
//	private GridLayout gridLayout = new GridLayout();
	
	/**
	 * 表页预览组件
	 */
	JPanel pPreviews[];
//	private PrintPreView m_view;
	//modify end
	private UfoReport m_Rep = null;

	/**
	 * 构造打印预览对话框。不推荐使用
	 * @param container container 父窗口
	 * @param modal boolean 是否模式对话框
	 * @param table TablePane 预览的表格组件
	 */
	public PrintPreviewDialog(Container container, boolean modal,
			TablePane table) {
		super(container, MultiLang.getString("PrintPreview"));
		
//		EventTracer trace=new EventTracer();
//        trace.add(scrollPane.getViewport());
        
		m_Rep = (UfoReport) container;
        
		this.table = table;
		try {
			jbInit();
			//      pack();
		} catch (Exception ex) {
			AppDebug.debug(ex);
		}
		setLocation(0, 0);
		
	}
	/**
	 * 构造打印预览对话框。
	 * @param UfoReport container 父窗口
	 * @param modal boolean 是否模式对话框
	 * @param table TablePane 预览的表格组件
	 */
	public PrintPreviewDialog(UfoReport container, boolean modal,
			TablePane table) {
		super(container, MultiLang.getString("PrintPreview"));

		m_Rep = container;

		this.table = table;
		try {
			jbInit();
			//      pack();
		} catch (Exception ex) {
			AppDebug.debug(ex);
		}
		setLocation(0, 0);
	}
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == m_btNextPage) { //翻页
				goNextPage(true);
				
			} else if (e.getSource() == m_btLastPage) {//翻页
				goLastPage(true);
			} else if (e.getSource() == boxResize) {//调整预览的显示比例
				zoom();
			} else if (e.getSource() == m_btPrint) {//调用打印
				if (table.print()) {
					close();
				}
			}

			else if (e.getSource() == m_btPrintSetting) {
				
				PrintSettingDlg dlg = new PrintSettingDlg(m_Rep);
				dlg.setVisible(true);
				
				 
			}

			else if (e.getSource() == m_btClose) {
				close();
			}
		}
	}

	public void close() {
		this.dispose();
	}

	/**
	 * 构建在中间显示的打印预览组件
	 * 
	 * @param index
	 *            被打印的区域索引。
	 * @param scale
	 *            打印的比例。
	 * @return PrintPreView
	 */
	private PrintPreView createPreView(int index, double scale) {
		Rectangle view = viewRects[index];
		return new PrintPreView(view, table, scale, table.getPrintSet()
				.getHeaderFooterModel(), index + 1, viewRects.length);
	}

	private void freshPageLabel(int index) {
		if(index>0&&index<=pageNumber){
		setCurPageIndex(index);
		if (index == pageNumber) {
			m_btNextPage.setEnabled(false);
		} else {
			m_btNextPage.setEnabled(true);
		}
		if (index == 1) {
			m_btLastPage.setEnabled(false);
		} else {
			m_btLastPage.setEnabled(true);
		}		
		setPageLabel(index, pageNumber);
		}
//		validate();
	}

	/**
	 * 得到当前预览的页面的索引。
	 * 
	 * @return int
	 */
	public int getCurPageIndex() {
		return m_nCurPageIndex;
	}

	/**
	 * 得到当前选择的多页显示的方式
	 * 
	 * @return int
	 */
	public int getMutiFormatIndex() {
		return m_nMutiFormatIndex;
	}

	private void goLastPage(boolean isButton) {
		int cur = getCurPageIndex()-1;
		if (cur > 0) {
			if(isButton)
			scrollPane.getViewport().setViewPosition(new Point(scrollPane.getViewport().getViewPosition().x,scrollPane.getViewport().getViewPosition().y-previewPageHeight));
		}
	}

	private void goNextPage(boolean isButton) {
		int cur = getCurPageIndex()+1;
		if (cur <=pageNumber) {
			if(isButton)
			scrollPane.getViewport().setViewPosition(new Point(scrollPane.getViewport().getViewPosition().x,scrollPane.getViewport().getViewPosition().y+previewPageHeight));
			
		}

	}

	private void initButtonPanel() {
		btnPanel.setLayout(new FlowLayout());
		ButtonListener l = new ButtonListener();
		btnPanel.add(m_lbPage);
		//modify by guogang 2007-7-17
		/*
		JButton[] btns = {
				// m_btPageSet,m_btAreaSet,
				m_btNextPage, m_btLastPage, m_btZoom, m_btPrint,
				m_btPrintSetting, m_btClose };//zzl更改前两个按钮的顺序
		for (int i = 0; i < btns.length; i++) {
			btnPanel.add(btns[i]);
			btns[i].addActionListener(l);
		}
		*/
		btnPanel.add(m_btNextPage);
		m_btNextPage.addActionListener(l);
		btnPanel.add(m_btLastPage);
		m_btLastPage.addActionListener(l);
		btnPanel.add(boxResize);
		boxResize.addActionListener(l);
		btnPanel.add(m_btPrint);
		m_btPrint.addActionListener(l);
		btnPanel.add(m_btPrintSetting);
		m_btPrintSetting.addActionListener(l);
		btnPanel.add(m_btClose);
		m_btClose.addActionListener(l);
		//modify end
//		scrollPane.getVerticalScrollBar().addAdjustmentListener(
//			new AdjustmentListener(){
//
//				public void adjustmentValueChanged(AdjustmentEvent e) {
//					if(e.getAdjustmentType()==AdjustmentEvent.BLOCK_INCREMENT){
//						goNextPage(false);
//					}
//					if(e.getAdjustmentType()==AdjustmentEvent.BLOCK_DECREMENT){
//						goLastPage(false);
//					}
//				}
//				
//			}
//		);		
		scrollPane.getViewport().addChangeListener(
				new ChangeListener(){

					public void stateChanged(ChangeEvent e) {
					    if(e.getSource()==scrollPane.getViewport()){
					    	int pageY=((JViewport)e.getSource()).getViewPosition().y;
//					    	int num=pageY/previewPageHeight+((pageY%previewPageHeight==0)?0:1);
					    	int num=pageY/(previewPageHeight*5/6);
//					    	int num=pageY+scrollPane.getViewport().getHeight()/(previewPageHeight);
					    	freshPageLabel(num+1);
					    }
						
					}
					
				}
		);
	}
	
	/**
	 * add by guogang 2007-7-17
     * 根据当前所设置的选项取得预览面板组件的宽度
     * @return
     */
    private int getPreviewWidth() {
    	double fScale[] = {
	            5, 2.5, 1.5, 1, 0.75, 0.5, 0.25, 0.1};
    	int spW = scrollPane.getWidth() - scrollPane.getVerticalScrollBar().getWidth() - 20;
        int spH = scrollPane.getHeight() - scrollPane.getHorizontalScrollBar().getHeight() - 20;
        PageFormat tpf = table.getPrintSet().getPageformat();
        double w = tpf.getWidth();
        double h = tpf.getHeight();
        
        int index = boxResize.getSelectedIndex();
        if (index < fScale.length) //"500%","250%","150%","100%","75%","50%","25%","10%"
            {
        	return (int) (w * fScale[index]);
            }
        else if (index == 8) //"页面宽度"
            return (int) spW;
        else if (index == 9) { //"文字宽度"
            double d = tpf.getImageableWidth();
            return (int) (w * spW / d) - 20;
        }
        else if (index == 10) { //"整页"
            if (w > h) {
                return (int)spW;
            }
            else {
                return (int) (w * spH / h);
            }
        }
        else { //"双页"
            if (w > h) {
                return (int)spW / 2;
            }
            else {
                return (int) (w * spH / h / 2);
            }
        }
    }
    /**
     * add by guogang 2007-7-17
     * 根据分页情况初始化预览组件数组
     * @return
     */
	 private JPanel[] getPreviewPanel() {
	        JPanel result[] = new JPanel[viewRects.length];
	        PageFormat pageFormat = table.getPrintSet().getPageformat();
	        for (int i = 0; i < viewRects.length; i++) {
	        	PrintPreView view = createPreView(i,
						m_nPrintScale);
	        	view.setBackground(Color.white);
	        	view.setPreferredSize(new Dimension(
	                (int) pageFormat.getWidth(), (int) pageFormat.getHeight()));
	            JPanel p = new JPanel(new BorderLayout());
//	            p.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 3));
	            p.setBackground(Color.gray);
	            p.add(view);
	            result[i] = p;
	        }
	        return result;
	    }
	  /**
	   * add by guogang 2007-7-17
	   * 调整预览比例后，根据当前所设置的选项排列预览面板
	   */
	    public void layoutPreview() {
	    	if ( pPreviews == null || pPreviews.length == 0)return;
			
	    printPane.removeAll();
	    
         int col=0,row=1;

         int spW = scrollPane.getWidth() - scrollPane.getVerticalScrollBar().getWidth() - 20;
         int spH = scrollPane.getHeight() - scrollPane.getHorizontalScrollBar().getHeight() - 20;

	        PageFormat tpf = table.getPrintSet().getPageformat();
	        double pw = tpf.getWidth();
	        double ph = tpf.getHeight();
	        int w=getPreviewWidth();
	        int h=(int) (ph * w / pw);

	        //单页\多页\和宽度小于总宽度的一半的
	        if ( boxResize.getSelectedIndex() > 9 ||
	            w < (spW / 2)) {
	            if (boxResize.getSelectedIndex() == 10) col = 1;
	            else if (boxResize.getSelectedIndex() == 11) col = 2;
	            else col = spW / w;

	            boolean isSingleLine = false;
	            if (w > h) {
	                w = spW / col;
	                h = (int) (ph * w / pw);
	            }
	            else {
//	            	row = pPreviews.length / col +( (pPreviews.length % col == 0) ? 0 : 1);
//	                h = spH / row;
//	                w = (int) (pw * h / ph);
	                if (w * col >= spW) {
	                    isSingleLine = true;
	                    w = spW / col;
	                    h = (int) (ph * w / pw);
	                }
	            }
	            //按列数和预览面板的个数求出应该有多少行
	            if (col * row < pPreviews.length) {
	                row = pPreviews.length / col +
	                    ( (pPreviews.length % col == 0) ? 0 : 1);
	            }
	            int index = 0;
	            for (int i = 0; i < row; i++) {
	                JPanel p = new JPanel();
	                for (int j = 0; j < col; j++) {
	                    if (index == pPreviews.length)break; //如果已经排完了,则跳出
	                    pPreviews[index].setPreferredSize(new Dimension(w, h));
	                    p.add(pPreviews[index]);
	                    index++;
	                }
	                //如果是单行单行的则为面板加入上边界,以达到垂直居中的效果
	                if (isSingleLine || row == 1) {
	                    p.setSize(new Dimension(spW + 20, spH + 20));//注意设置setPreferredSize()和setSize()的不同,该处如果调用前者可能引起没有绘制出来
	                    p.setBorder(BorderFactory.createEmptyBorder(
	                        (spH + 20 - h) / 2, -50, 0, -50
	                        ));
	                }
	                if (p.getComponentCount() != 0) printPane.add(p);
	            }
	        }
	        /**
	         *一般的排列
	         */
	        else {
	            for (int i = 0; i < pPreviews.length; i++) {
	            	JPanel p = new JPanel();
	                p.add(pPreviews[i]);
	                pPreviews[i].setPreferredSize(new Dimension(w, h));
					printPane.add(p);
	            }
	            row=pPreviews.length;
	        }
	    printPane.setSize(printPane.getPreferredSize());
	    
	    previewPageHeight=h;
	    pageNumber=row;
	    freshPageLabel(1);
	    
		printPane.repaint();	

		
	    }
	    /**
	     * add by guogang 2007-7-17
	     * 新的初始化预览面版方法,打印设置后重新调用
	     */
	 public void freshPrintPanel() {
		 setViewRects(table.getPrintPageRect());
	     pPreviews=getPreviewPanel();
	     layoutPreview();
	    }
	 
    /*旧的方法，已废弃
	private void initPrintPanel() {
		//    double scale = 1.0;//table.getPrintSet().getPrintScale();
		int formatIndex = getMutiFormatIndex();
		printPane.removeAll();
		gridLayout.setHgap(5);
		gridLayout.setVgap(5);
		int start = getCurPageIndex();
		if (formatIndex == SINGLE_PAGE) {
			gridLayout.setRows(1);
			gridLayout.setColumns(1);
			//      if(m_view==null){
			m_view = createPreView(start, m_nPrintScale);
			//      }else {
			//      	m_view.reset(m_nPrintScale,viewRects[start]);
			//      }
			printPane.add(m_view);
			m_view.repaint();
		} else {
			int col = MUTI_PAGE[formatIndex][0], row = MUTI_PAGE[formatIndex][1];
			gridLayout.setRows(row);
			gridLayout.setColumns(col);
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					PrintPreView view = createPreView(start + i * row + j,
							m_nPrintScale);
					printPane.add(view);
				}
			}

		}
	}
    */
	private void jbInit() throws Exception {
		
		initButtonPanel();
		mainPane.setLayout(new PreviewLayout());
		mainPane.add(btnPanel, PreviewLayout.BUTTONPANEL);
		mainPane.add(scrollPane, PreviewLayout.SCROLL_PANEL);
		//modify by guogang 2007-7-17
//		printPane.setLayout(gridLayout);
		printPane.setLayout(boxLayout);
		//modify end
		scrollPane.setViewportView(printPane);
		getContentPane().add(mainPane);
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInset = Toolkit.getDefaultToolkit().getScreenInsets(
				getGraphicsConfiguration());
		setSize(screenDim.width - screenInset.left - screenInset.right,
				screenDim.height - screenInset.top - screenInset.bottom);
        //add by guogang 2007-7-17
		//设置默认显示为"100%"
		boxResize.setSelectedIndex(3);
		scrollPane.addComponentListener(new ComponentAdapter() {
	            public void componentResized(ComponentEvent e) {
	                layoutPreview();
	            }
	        });
		//add end
		freshPrintPanel();
	}

	/**
	 * 设置当前预览的页面的索引
	 * @param curPage int
	 */
	public void setCurPageIndex(int curPage) {
		if (curPage != m_nCurPageIndex) {
			this.m_nCurPageIndex = curPage;			
		}
	}

	/**
	 * 设置当前选择的多页显示的方式
	 * @param formatIndex int
	 */
	/**此方法已废弃
	public void setMutiFormatIndex(int formatIndex) {
		if (this.m_nMutiFormatIndex != formatIndex) {
			this.m_nMutiFormatIndex = formatIndex;
			freshPageLabel(getCurPageIndex());
		}
	}
    **/
	private void setPageLabel(int index, int len) {
		String text = MultiLang.getString("The") + " " + +(index) + "/"
				+ len + " " + MultiLang.getString("Page");
		m_lbPage.setText(text);
	}

	/**
	 * 重新设置页面的分割。通常是由于打印的设置发生改变而使需要打印的页数量和范围发生改变。
	 * @param rects Rectangle[]
	 */
	public void setViewRects(Rectangle[] rects) {
		if (rects == null || rects.length == 0) {
			this.viewRects = new Rectangle[] { null };
		} else {
			this.viewRects = rects;
		}

	}

	//liuyy
	public Rectangle[] getviewRects() {
		return this.viewRects;
	}

	/**
	 * 调整窗口的大小，意味着首先如果是多页显示，需要切换为单页显示；其次是对于显示的比例，需要根据
	 * 组件当前的大小和纸张的大小决定，保证纸张可以被完全显示在屏幕上。
	 */
	/*老的缩放方法，已丢弃
	private void zoom() {
		m_nMutiFormatIndex = SINGLE_PAGE;
		fitWindow = !fitWindow;
		double scale = 1.0;
		if (fitWindow) {
			Dimension dime = scrollPane.getViewport().getExtentSize();
			//保证视口可以显示页面。
			PageFormat tpf = table.getPrintSet().getPageformat();
			double width = tpf.getWidth();
			double height = tpf.getHeight();
			scale = Math
					.min(dime.getWidth() / width, dime.getHeight() / height);
		}
		m_nPrintScale = scale;
		freshPage(getCurPageIndex());
	}
	*/
	private void zoom() {
		layoutPreview();
	}
}
 