package com.ufida.report.chart.applet;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.print.PreviewLayout;
import com.ufsoft.table.print.PrintSet;


public class ChartPrintPreviewDialog     extends UfoDialog {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**支持多页预览的显示风格。*/
	  private final int[][] MUTI_PAGE = {{1, 2},{1, 3}, {2, 2}, { 2, 3}};

	  //在打印设置下，需要打印内容的分页范围。
//	  private Rectangle[] viewRects = null;
	  /**
	   * 打印页数
	   */
	  private int m_iPageNum=0;
	  private final int SINGLE_PAGE = -1;

	  /**记录多页显示风格所使用的具体是哪一种.-1表示当前只显示选择的页显示预览*/
	  private int m_nMutiFormatIndex = SINGLE_PAGE;
	  
	  private DimChartComponet m_chartComp;
	  /**当前预览的页面的索引，如果是多页，是首页索引*/
	  private int m_nCurPageIndex;
	  /**预览面板显示的比例。*/
	  double m_nPrintViewScale = 1.0;
	  boolean fitWindow = false;

	  //×××××××××××××××构造显示界面需要的属性×××××××××
//	  ResourceBundle res = ResourceBundle.getBundle("com.ufsoft.table.print.Res");
	  JPanel mainPane = new UIPanel();
	  /**容纳按钮的面板*/
	  JPanel btnPanel = new UIPanel();

	  JButton m_btPageSet = new nc.ui.pub.beans.UIButton(MultiLang.getString("PagePrintSet"));
	  JButton m_btPrint = new nc.ui.pub.beans.UIButton(MultiLang.getString("print"));
	  JButton m_btZoom = new nc.ui.pub.beans.UIButton(MultiLang.getString("Zoom"));
	  JButton m_btLastPage = new nc.ui.pub.beans.UIButton(MultiLang.getString("LastPage"));
	  JButton m_btNextPage = new nc.ui.pub.beans.UIButton(MultiLang.getString("NextPage"));
	  JButton m_btClose = new nc.ui.pub.beans.UIButton(MultiLang.getString("close"));
//	  JButton m_btAreaSet = new nc.ui.pub.beans.UIButton(MultiLang.getString("PrintArea"));
	  JLabel m_lbPage = new nc.ui.pub.beans.UILabel();
	  /**容纳需要预览的组件的面板*/
	  JScrollPane scrollPane = new UIScrollPane();
	  /**容纳所有需要显示的页面的面板，使用一个GridLayout的布局管理器。*/
	  private JPanel printPane = new UIPanel();
	  /**
	   * 容纳预览表页的布局管理器
	   */
	  private GridLayout gridLayout = new GridLayout();

	  private DimChartPrintPreView m_view ;
	  

	  private class ButtonListener
	      implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	      if (e.getSource() == m_btNextPage) { //翻页
	        goNextPage();
	      }
	      else if (e.getSource() == m_btLastPage) {//翻页
	        goLastPage();
	      }
	      else if (e.getSource() == m_btZoom) {//调整预览的显示比例
	        zoom();
	      }
	      else if (e.getSource() == m_btPrint) {//调用打印
	        if(m_chartComp.print(true)){
	          close();
	        }
	      }
	      else if (e.getSource() == m_btPageSet) {//页面设置的调用
	        Thread t = new Thread(new Runnable(){
		        public void run(){
		            PrintSet ps = m_chartComp.getPrintSet();
		            PageFormat oldPf = ps.getPageformat();
		            PageFormat newPf = m_chartComp.getPrinterJob().pageDialog(oldPf);
		    		//这里需要检查返回的页面设置是否合法，如果不合法，需要修正。
//		    		PageFormat validPf = getPrinterJob().validatePage(pf);
		            ps.setPageFormat(newPf);
//		            Rectangle[] rects=m_chartComp.getPrintPageRect();
		            if(m_iPageNum > 0){
		            	 freshPage(getCurPageIndex());
		            }
		    	    
		    	    repaint();
		        }
		    });
		    
		    
		    t.start();
		    try {
	            Thread.sleep(1);
	        } catch (InterruptedException e1) {
	            // TODO 自动生成 catch 块
	AppDebug.debug(e1);
	        }

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
	   * 构造打印预览对话框。
	   * @param container container 父窗口
	   * @param modal boolean 是否模式对话框
	   * @param table TablePane 预览的表格组件
	   */
	  public ChartPrintPreviewDialog(Container container,  boolean modal,
			  DimChartComponet chartComp) {
	    super(container, MultiLang.getString("PrintPreview"));
	    this.m_chartComp = chartComp;
	    m_iPageNum=chartComp.getNumberOfPages();
	    try {
	      jbInit();
//	      pack();
	    }
	    catch (Exception ex) {
	AppDebug.debug(ex);//@devTools       AppDebug.debug(ex);
	    }
	    setLocation(0,0);
	  }

	  /**
	   * 构建在中间显示的打印预览组件
	   * @param index 被打印的区域索引。
	   * @param scale 打印的比例。
	   * @return PrintPreView
	   */
	  private DimChartPrintPreView createPreView(int index) {
//	    Rectangle view = viewRects[index];
	    return new DimChartPrintPreView( m_chartComp.getDimChartPanel(index), m_chartComp.getPrintSet(),
	    		m_nPrintViewScale,index+1,m_iPageNum);
	  }
	  private void freshPage(int index){
	      //zzl添加**********************
	      if (index == m_iPageNum - 1) {
	        m_btNextPage.setEnabled(false);
	      }else{
	        m_btNextPage.setEnabled(true);
	      }
	      if(getCurPageIndex() == 0){
	        m_btLastPage.setEnabled(false);
	      }else{
	        m_btLastPage.setEnabled(true);
	      }
	      //zzl添加结束********************
	      initPrintPanel();
	      setPageLabel(index, m_iPageNum);
	      validate();
	  }
	  /**
	   * 得到当前预览的页面的索引。
	   * @return int
	   */
	  public int getCurPageIndex() {
	    if(m_nCurPageIndex >= m_iPageNum){
	      m_nCurPageIndex = m_iPageNum-1;
	    }
	    return m_nCurPageIndex;
	  }
	  /**得到当前选择的多页显示的方式
	 * @return int
	 * */
	  public int getMutiFormatIndex() {
	    return m_nMutiFormatIndex;
	  }
	  private void goLastPage() {
	    int cur = getCurPageIndex();
	    if (cur > 0) {
	      setCurPageIndex(cur - 1);
	      if (cur - 1 == 0) {
	        m_btLastPage.setEnabled(false);
	      }
	      if (!m_btNextPage.isEnabled()) {
	        m_btNextPage.setEnabled(true);
	      }
	    }
	  }
	  private void goNextPage() {
	    int cur = getCurPageIndex();
	    if (cur < m_iPageNum- 1) {
	      setCurPageIndex(cur + 1);
	      if (cur + 1 == m_iPageNum - 1) {
	        m_btNextPage.setEnabled(false);
	      }
	      if (!m_btLastPage.isEnabled()) {
	        m_btLastPage.setEnabled(true);
	      }
	    }

	  }
	  private void initButtonPanel() {
	    btnPanel.setLayout(new FlowLayout());
	    ButtonListener l = new ButtonListener();
	    btnPanel.add(m_lbPage);
	    JButton[] btns = {
	    		m_btNextPage, m_btLastPage, m_btZoom, m_btPrint, m_btPageSet, m_btClose};//zzl更改前两个按钮的顺序
	    for (int i = 0; i < btns.length; i++) {
	      btnPanel.add(btns[i]);
	      btns[i].addActionListener(l);
	    }
	    if (getCurPageIndex() == 0) {
	      m_btLastPage.setEnabled(false);
	    }
	    if (getCurPageIndex() == m_iPageNum - 1) {
	      m_btNextPage.setEnabled(false);
	    }
	    setPageLabel(getCurPageIndex(), m_iPageNum);
	  }
	  private void initPrintPanel() {
//	    double scale = 1.0;//table.getPrintSet().getPrintScale();
	    int formatIndex = getMutiFormatIndex();
	    printPane.removeAll();
	    gridLayout.setHgap(5);
	    gridLayout.setVgap(5);
	    int start = getCurPageIndex();
	    if (formatIndex == SINGLE_PAGE) {
	      gridLayout.setRows(1);
	      gridLayout.setColumns(1);

	      m_view = createPreView(start);

	      printPane.add(m_view);
	      m_view.repaint();
	    }
	    else {
	      int col = MUTI_PAGE[formatIndex][0], row = MUTI_PAGE[formatIndex][1];
	      gridLayout.setRows(row);
	      gridLayout.setColumns(col);
	      for (int i = 0; i < row; i++) {
	        for (int j = 0; j < col; j++) {
	        	DimChartPrintPreView view = createPreView(start + i * row + j);
	          printPane.add(view);
	        }
	      }

	    }
	  }
	  private void jbInit() throws Exception {
	    initButtonPanel();
	    freshPage(getCurPageIndex());
	    mainPane.setLayout(new PreviewLayout());
	    mainPane.add(btnPanel, PreviewLayout.BUTTONPANEL);
	    mainPane.add(scrollPane, PreviewLayout.SCROLL_PANEL);
	    printPane.setLayout(gridLayout);
	    scrollPane.setViewportView(printPane);
	    getContentPane().add(mainPane);
	    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	    Insets screenInset = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
	    setSize(screenDim.width - screenInset.left - screenInset.right, 
	    		screenDim.height - screenInset.top - screenInset.bottom);
	    
	  }
	  /**
	   * 设置当前预览的页面的索引
	   * @param curPage int
	   */
	  public void setCurPageIndex(int curPage) {
	    if (curPage != m_nCurPageIndex) {
	      this.m_nCurPageIndex = curPage;
	      freshPage(getCurPageIndex());
	    }
	  }
	  /**
	   * 设置当前选择的多页显示的方式
	   * @param formatIndex int
	   */
	  public void setMutiFormatIndex(int formatIndex) {
	    if (this.m_nMutiFormatIndex != formatIndex) {
	      this.m_nMutiFormatIndex = formatIndex;
	      freshPage(getCurPageIndex());
	    }
	  }
	  private void setPageLabel(int index, int len) {
	    String text = MultiLang.getString("The") + " " + + (index + 1) + "/" + len + " " + MultiLang.getString("Page");
	    m_lbPage.setText(text);
	  }
//	  /**
//	   * 重新设置页面的分割。通常是由于打印的设置发生改变而使需要打印的页数量和范围发生改变。
//	   * @param rects Rectangle[]
//	   */
//	  private void setViewRects(Rectangle[] rects) {
//	    if (rects == null || rects.length == 0) {
//	        this.viewRects = new Rectangle[]{null};
//	    }else{
//	        this.viewRects = rects;
//	    }
//	    freshPage(getCurPageIndex());
//	  }
	  /**
	   * 调整窗口的大小，意味着首先如果是多页显示，需要切换为单页显示；其次是对于显示的比例，需要根据
	   * 组件当前的大小和纸张的大小决定，保证纸张可以被完全显示在屏幕上。
	   */
	  private void zoom() {
	    m_nMutiFormatIndex = SINGLE_PAGE;
	    fitWindow = !fitWindow;
	    double scale = 1.0;
	    if (fitWindow) {
	      Dimension dime = scrollPane.getViewport().getExtentSize();
	      //保证视口可以显示页面。
	      PageFormat tpf = m_chartComp.getPrintSet().getPageformat();
	      double width = tpf.getWidth();
	      double height = tpf.getHeight();
	      scale = Math.min(dime.getWidth() / width, dime.getHeight() / height);
	    }
	    m_nPrintViewScale = scale;
	    freshPage(getCurPageIndex());
	  }
	}