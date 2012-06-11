/*
 * SetDimChartDlg.java
 * �������� 2005-2-21
 * Created by CaiJie
 */
package com.ufida.report.chart.applet;

import javax.swing.JPanel;

import com.ufida.report.chart.model.DimDataProvider;
import com.ufida.report.chart.property.ChartProperty;
import com.ufida.report.multidimension.applet.SelDimSetPanel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.WizardDialog;

/**
 * ͼ�����öԻ���
 * ��ȡͼ��ĸ��ֲ���������
 * @author caijie 
 * @since 3.1
 */
public class CreateDimChartWirzardDlg extends WizardDialog{  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * �����������Զ������������������
     * @param dataProperty
     * @return
     */
    public static JPanel createDataPropertyPane(DimDataProvider dataProperty) {
   //     DataPropertyPane result = new DataPropertyPane(dataProperty);
        if(dataProperty == null) return null;     
        SelectDimPanel panel = new SelectDimPanel(dataProperty, new SelDimSetPanel());
       
       // result.add(panel);
        	
        return panel;
    }
    private SetDimChartWizard0 m_wizard = null;
    
    /**
     * ����
     */
    public CreateDimChartWirzardDlg(DimChartPlugin chartPlugin, SetDimChartWizard0 wizard) { 
        super(chartPlugin.getReport(),StringResource.getStringResource("ubichart00014"), wizard);        
        m_wizard = wizard;     
    }
       
  
    /**
     * ����ͼ���Զ���
     * @return 
     */
    public ChartProperty getChartProperty() {          
        return m_wizard.getChartProperty();
    }
   
   public static void main(String[] args) {
       String userID = null;//((BIContextVO)(m_chartPlugin.getReport().getContextVo())).getUserPK();
       String reportID = null;//((BIContextVO)(m_chartPlugin.getReport().getContextVo())).getReportPK();
       CreateDimChartWirzardDlg dlg = new CreateDimChartWirzardDlg(null, new SetDimChartWizard0(userID, reportID));
//       dlg.setPageSize(400, 300);         
       dlg.show();
      
   }      
   
}
