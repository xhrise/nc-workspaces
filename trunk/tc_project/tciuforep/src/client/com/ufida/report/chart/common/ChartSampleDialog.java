package com.ufida.report.chart.common;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIPanel;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.property.ChartProperty;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

public class ChartSampleDialog extends UfoDialog implements WindowListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9016040072917028882L;
	private JPanel jContentPane = null;
	private JPanel typePanel = null;
	private JPanel propPanel = null;
	private JLabel hintLabel = null;  //  @jve:decl-index=0:visual-constraint="527,379"

	/**
	 * This is the default constructor
	 */
	public ChartSampleDialog() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1017, 417);
		this.setTitle(StringResource.getStringResource("ubichart00156"));
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getTypePanel(), null);
			jContentPane.add(getPropPanel(), null);
			jContentPane.add(getHintLabel(), null);
		}
		return jContentPane;
	}
	private JLabel getHintLabel(){
		if(hintLabel == null){
			hintLabel = new nc.ui.pub.beans.UILabel();
			hintLabel.setText("hint Message");
			hintLabel.setLocation(new java.awt.Point(486,345));
			hintLabel.setSize(new java.awt.Dimension(366,24));
		}
		return hintLabel;
	}
	/**
	 * This method initializes typePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTypePanel() {
		if (typePanel == null) {
			typePanel = new ChartTypePanel(ChartPublic.CLUSTERED_BAR_CHART);
//			typePanel = new UIPanel();
			
			showPreview();
			
			((ChartTypePanel)typePanel).getSubChartTypeList().addListSelectionListener(new ListSelectionListener(){
				public void valueChanged(ListSelectionEvent e) {	
//					int	chartType = ((ChartTypePanel)getTypePanel()).getSelectedChartIndex();
					showPreview();
				}				
			});	
			((ChartTypePanel)typePanel).getSubChartTypeList().addPropertyChangeListener("ChartChanged",new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent evt) {
					showPreview();					
				}});
			typePanel.setBounds(new java.awt.Rectangle(-3,0,463,387));
		}
		return typePanel;
	}

	private void showPreview(){
		getPropPanel().removeAll();
		getHintLabel().setText("");
		
		int	chartType = ((ChartTypePanel)getTypePanel()).getSelectedChartIndex();
		if(chartType == ChartPublic.UNDIFINED) {
			getPropPanel().removeAll();
			getHintLabel().setText(StringResource.getStringResource("mbichart00001"));
			return ;
		}
		ChartProperty prop = ChartPublic.createChartProperty(chartType);
		if(prop == null) {
			getPropPanel().removeAll();
			getHintLabel().setText(StringResource.getStringResource("mbichart00002"));
			return ;
		}
		JFreeChart chart = ChartPublic.createSampleChart(chartType);
		if(chart == null) {
			getPropPanel().removeAll();
			getHintLabel().setText(StringResource.getStringResource("mbichart00003"));
			return ;
		}
		getPropPanel().add(ChartPropertyPaneFactory.createChartPropertyPane(prop, chart));
		getPropPanel().invalidate();
		getPropPanel().validate();
		getPropPanel().repaint();
	}
	/**
	 * This method initializes propPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPropPanel() {
		if (propPanel == null) {
			propPanel = new UIPanel();
			propPanel.setBounds(new java.awt.Rectangle(467,0,544,320));
		}
		return propPanel;
	}
	public static void main(String[] args) {        
		ChartSampleDialog dlg = new ChartSampleDialog();;
//        dlg.setPageSize(400, 300);         
        dlg.show();
       
    }

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) {		
		System.exit(0);
		
	}

	public void windowClosing(WindowEvent e) {		
		System.exit(0);
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}  
}  //  @jve:decl-index=0:visual-constraint="53,10"
