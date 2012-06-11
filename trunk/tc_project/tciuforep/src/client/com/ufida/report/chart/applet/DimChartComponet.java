package com.ufida.report.chart.applet;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.model.DimChartModel;
import com.ufsoft.table.print.PrintSet;

public class DimChartComponet extends UIScrollPane implements Pageable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel chartPane = null;
    private DimChartPanel[] allDimChartPanels = null;//所有的图形面板，每个面板上都有一个图形
    
    private DimChartModel m_chartModel = null;
    
	private PrinterJob printerJob;
    
    public DimChartComponet(DimChartModel chartModel,JFreeChart[] charts){
    	super();
    	this.m_chartModel=chartModel;
    	createDimChartPanel(charts);
    }
    private  void createDimChartPanel(JFreeChart[] charts) {    
    	chartPane = new UIPanel();    	
//    	scrollPane = new UIScrollPane();
    	setViewportView(chartPane);
    	updateAllCharts(charts);
    }
    
    public void updateAllCharts(JFreeChart[] charts){
    	if(charts != null){
    		if(allDimChartPanels != null){
    			for(int i = 0; i < allDimChartPanels.length; i++){
    				chartPane.remove(allDimChartPanels[i]);
    			}
    		}    		
    		
    		allDimChartPanels = new DimChartPanel[charts.length];
    		for(int i = 0; i < charts.length; i++){
    			allDimChartPanels[i] = new DimChartPanel(m_chartModel, charts[i]);
    			chartPane.add(allDimChartPanels[i]);
    		}
    	}     

    	validate();
    	invalidate();
    	repaint();
    	setViewportView(chartPane);
    }
    
    public DimChartPanel getDimChartPanel() {
        return getDimChartPanel(0);
    }
    public DimChartPanel getDimChartPanel(int iIndex) {
        return allDimChartPanels[iIndex];
    }
	public int getNumberOfPages() {
		return allDimChartPanels==null?0:allDimChartPanels.length;
	}
	public PageFormat getPageFormat(int pageIndex)
			throws IndexOutOfBoundsException {
		PageFormat pf = (PageFormat) getPrintSet().getPageformat().clone();
		Paper paper = pf.getPaper();
		paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
		pf.setPaper(paper);
		return pf;
	}
	public Printable getPrintable(int pageIndex)
			throws IndexOutOfBoundsException {
		
		Printable print = new Printable() {
			public int print(Graphics g, PageFormat pf, int pi)
					throws PrinterException {
				if (allDimChartPanels==null || allDimChartPanels.length==0) {
					return Printable.NO_SUCH_PAGE;
				}

				DimChartPrintPreView view = new DimChartPrintPreView( allDimChartPanels[pi],getPrintSet(),
						1,pi+1,allDimChartPanels.length);
				view.setDoubleBuffered(false);
				view.setBounds(0, 0, view.getPreferredSize().width, view
						.getPreferredSize().height);
				view.printAll(g);
				return Printable.PAGE_EXISTS;
			}
		};
		return print;
	}
	
	public PrintSet getPrintSet(){
		return m_chartModel.getPrintSet();
	}
	public PrinterJob getPrinterJob() {
        if(printerJob == null){
            printerJob = PrinterJob.getPrinterJob();
        }
		printerJob.setPageable(this);
		return printerJob;
	}
	
	public void pageFromat() {
        
        Thread t = new Thread(new Runnable(){
	        public void run(){
	            PrintSet ps = getPrintSet();
	            PageFormat oldPf = ps.getPageformat();
	            PageFormat newPf = getPrinterJob().pageDialog(oldPf);
	            ps.setPageFormat(newPf);
	    	    repaint();
	        }
	    });
	    
	    
	    t.start();
	    try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            // TODO 自动生成 catch 块
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
        }

	}
	
	public void printPreview(Container parent) {
		if(parent == null){
			parent = this;
		}
//		double oldScale = TablePane.getViewScale();
//		setViewScale(TablePane.PRINT_SCALE);
		ChartPrintPreviewDialog printPreView = new ChartPrintPreviewDialog(parent, true, this);	
		printPreView.setVisible(true);
//		setViewScale(oldScale);
	}
//	private void setViewScale(double scale) {
//		if (TableStyle.getViewScale() != scale) {
//            TableStyle.setViewScale(scale);
//			revalidate();
//			repaint();
//		}
//	}
	
//	public Rectangle[] getPrintPageRect() {
//		//TODO ?
//		if(allDimChartPanels==null || allDimChartPanels.length==0)
//			return new Rectangle[0];
//		
//		int iLen=allDimChartPanels.length;
//		Rectangle[] rects=new Rectangle[iLen];
//		for(int i=0;i<iLen;i++){
//			rects[i]=getRect();
//		}
//		return rects;
//	}
	
	 public boolean print(boolean showPrintDlg){
		 /**
		  * 磅是打印度量单位，像素是屏幕度量单位；磅的实际大小是固定的（1/72 英寸）
		  * 但是像素的大小取决于特定屏幕的分辨率
		  */
	        if(allDimChartPanels==null || allDimChartPanels.length == 0){
	            return false;
	        }
	        //TODO 处理ViewScale
	        boolean result = false;
//	        double oldScale = getViewScale();
	        try {            
//	            setViewScale(PRINT_SCALE);
	            PrinterJob pj = getPrinterJob();
	            if(showPrintDlg==true && !pj.printDialog()){
	                return false;
	            }
	            pj.print();
	            result= true;            
	        } catch (PrinterException ex) {
	            JOptionPane.showMessageDialog(this, ex);
	        }finally{
//	            setViewScale(oldScale);
	        }
	        return result;
	    }
}
