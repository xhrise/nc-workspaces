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

	/**֧�ֶ�ҳԤ������ʾ���*/
	  private final int[][] MUTI_PAGE = {{1, 2},{1, 3}, {2, 2}, { 2, 3}};

	  //�ڴ�ӡ�����£���Ҫ��ӡ���ݵķ�ҳ��Χ��
//	  private Rectangle[] viewRects = null;
	  /**
	   * ��ӡҳ��
	   */
	  private int m_iPageNum=0;
	  private final int SINGLE_PAGE = -1;

	  /**��¼��ҳ��ʾ�����ʹ�õľ�������һ��.-1��ʾ��ǰֻ��ʾѡ���ҳ��ʾԤ��*/
	  private int m_nMutiFormatIndex = SINGLE_PAGE;
	  
	  private DimChartComponet m_chartComp;
	  /**��ǰԤ����ҳ�������������Ƕ�ҳ������ҳ����*/
	  private int m_nCurPageIndex;
	  /**Ԥ�������ʾ�ı�����*/
	  double m_nPrintViewScale = 1.0;
	  boolean fitWindow = false;

	  //������������������������������������ʾ������Ҫ�����ԡ�����������������
//	  ResourceBundle res = ResourceBundle.getBundle("com.ufsoft.table.print.Res");
	  JPanel mainPane = new UIPanel();
	  /**���ɰ�ť�����*/
	  JPanel btnPanel = new UIPanel();

	  JButton m_btPageSet = new nc.ui.pub.beans.UIButton(MultiLang.getString("PagePrintSet"));
	  JButton m_btPrint = new nc.ui.pub.beans.UIButton(MultiLang.getString("print"));
	  JButton m_btZoom = new nc.ui.pub.beans.UIButton(MultiLang.getString("Zoom"));
	  JButton m_btLastPage = new nc.ui.pub.beans.UIButton(MultiLang.getString("LastPage"));
	  JButton m_btNextPage = new nc.ui.pub.beans.UIButton(MultiLang.getString("NextPage"));
	  JButton m_btClose = new nc.ui.pub.beans.UIButton(MultiLang.getString("close"));
//	  JButton m_btAreaSet = new nc.ui.pub.beans.UIButton(MultiLang.getString("PrintArea"));
	  JLabel m_lbPage = new nc.ui.pub.beans.UILabel();
	  /**������ҪԤ������������*/
	  JScrollPane scrollPane = new UIScrollPane();
	  /**����������Ҫ��ʾ��ҳ�����壬ʹ��һ��GridLayout�Ĳ��ֹ�������*/
	  private JPanel printPane = new UIPanel();
	  /**
	   * ����Ԥ����ҳ�Ĳ��ֹ�����
	   */
	  private GridLayout gridLayout = new GridLayout();

	  private DimChartPrintPreView m_view ;
	  

	  private class ButtonListener
	      implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	      if (e.getSource() == m_btNextPage) { //��ҳ
	        goNextPage();
	      }
	      else if (e.getSource() == m_btLastPage) {//��ҳ
	        goLastPage();
	      }
	      else if (e.getSource() == m_btZoom) {//����Ԥ������ʾ����
	        zoom();
	      }
	      else if (e.getSource() == m_btPrint) {//���ô�ӡ
	        if(m_chartComp.print(true)){
	          close();
	        }
	      }
	      else if (e.getSource() == m_btPageSet) {//ҳ�����õĵ���
	        Thread t = new Thread(new Runnable(){
		        public void run(){
		            PrintSet ps = m_chartComp.getPrintSet();
		            PageFormat oldPf = ps.getPageformat();
		            PageFormat newPf = m_chartComp.getPrinterJob().pageDialog(oldPf);
		    		//������Ҫ��鷵�ص�ҳ�������Ƿ�Ϸ���������Ϸ�����Ҫ������
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
	            // TODO �Զ����� catch ��
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
	   * �����ӡԤ���Ի���
	   * @param container container ������
	   * @param modal boolean �Ƿ�ģʽ�Ի���
	   * @param table TablePane Ԥ���ı�����
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
	   * �������м���ʾ�Ĵ�ӡԤ�����
	   * @param index ����ӡ������������
	   * @param scale ��ӡ�ı�����
	   * @return PrintPreView
	   */
	  private DimChartPrintPreView createPreView(int index) {
//	    Rectangle view = viewRects[index];
	    return new DimChartPrintPreView( m_chartComp.getDimChartPanel(index), m_chartComp.getPrintSet(),
	    		m_nPrintViewScale,index+1,m_iPageNum);
	  }
	  private void freshPage(int index){
	      //zzl���**********************
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
	      //zzl��ӽ���********************
	      initPrintPanel();
	      setPageLabel(index, m_iPageNum);
	      validate();
	  }
	  /**
	   * �õ���ǰԤ����ҳ���������
	   * @return int
	   */
	  public int getCurPageIndex() {
	    if(m_nCurPageIndex >= m_iPageNum){
	      m_nCurPageIndex = m_iPageNum-1;
	    }
	    return m_nCurPageIndex;
	  }
	  /**�õ���ǰѡ��Ķ�ҳ��ʾ�ķ�ʽ
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
	    		m_btNextPage, m_btLastPage, m_btZoom, m_btPrint, m_btPageSet, m_btClose};//zzl����ǰ������ť��˳��
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
	   * ���õ�ǰԤ����ҳ�������
	   * @param curPage int
	   */
	  public void setCurPageIndex(int curPage) {
	    if (curPage != m_nCurPageIndex) {
	      this.m_nCurPageIndex = curPage;
	      freshPage(getCurPageIndex());
	    }
	  }
	  /**
	   * ���õ�ǰѡ��Ķ�ҳ��ʾ�ķ�ʽ
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
//	   * ��������ҳ��ķָͨ�������ڴ�ӡ�����÷����ı��ʹ��Ҫ��ӡ��ҳ�����ͷ�Χ�����ı䡣
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
	   * �������ڵĴ�С����ζ����������Ƕ�ҳ��ʾ����Ҫ�л�Ϊ��ҳ��ʾ������Ƕ�����ʾ�ı�������Ҫ����
	   * �����ǰ�Ĵ�С��ֽ�ŵĴ�С��������ֽ֤�ſ��Ա���ȫ��ʾ����Ļ�ϡ�
	   */
	  private void zoom() {
	    m_nMutiFormatIndex = SINGLE_PAGE;
	    fitWindow = !fitWindow;
	    double scale = 1.0;
	    if (fitWindow) {
	      Dimension dime = scrollPane.getViewport().getExtentSize();
	      //��֤�ӿڿ�����ʾҳ�档
	      PageFormat tpf = m_chartComp.getPrintSet().getPageformat();
	      double width = tpf.getWidth();
	      double height = tpf.getHeight();
	      scale = Math.min(dime.getWidth() / width, dime.getHeight() / height);
	    }
	    m_nPrintViewScale = scale;
	    freshPage(getCurPageIndex());
	  }
	}