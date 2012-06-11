/*
 * Created on 2005-7-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIPanel;

import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.common.ChartTypePanel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectChartTypeDlg extends UfoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JPanel jContentPane = null;
	private ChartTypePanel chartTypePanel= null;
	private int chartType = ChartPublic.UNDIFINED; 
	private JPanel jPanel = null;
	private JButton OKBtn = null;
	private JButton cancleBtn = null;
	/**
	 * This is the default constructor
	 */
	public SelectChartTypeDlg(Container parent, int type) {
		super(parent);
		this.chartType = type;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("ubichart00002"));
		this.setSize(625, 482);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);			
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getOKBtn(), null);
			jContentPane.add(getCancleBtn(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(8, 7, 600, 400);
			jPanel.add(getChartTypePanel());
		}
		return jPanel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();
			OKBtn.setBounds(144, 412, 75, 22);
			OKBtn.setText(StringResource.getStringResource("ubichart00003"));
			OKBtn.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {			       
		            setResult(UfoDialog.ID_OK);
		            close();			        
			    }
			    });
		
		}
		return OKBtn;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancleBtn() {
		if (cancleBtn == null) {
			cancleBtn = new nc.ui.pub.beans.UIButton();
			cancleBtn.setBounds(380, 412, 75, 22);
			cancleBtn.setText(StringResource.getStringResource("ubichart00004"));
			cancleBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setResult(UfoDialog.ID_CANCEL);
                    close();
                }
            });
		
		}
		return cancleBtn;
	}
    /**
     * @return Returns the chartTypePanel.
     */
    private ChartTypePanel getChartTypePanel() {
        if(chartTypePanel == null) {
            chartTypePanel = new ChartTypePanel(this.chartType);
            if(this.chartType != ChartPublic.UNDIFINED) {
                
            }
        }
        return chartTypePanel;
    }
 
    public int getSelectedType() {
        return getChartTypePanel().getSelectedChartIndex();
    }
    public static void main(String[] args) {        
        SelectChartTypeDlg dlg = new SelectChartTypeDlg(null, ChartPublic.PIE_CHART);;
//        dlg.setPageSize(400, 300);         
        dlg.show();
       
    }     
   }  //  @jve:decl-index=0:visual-constraint="10,10"
