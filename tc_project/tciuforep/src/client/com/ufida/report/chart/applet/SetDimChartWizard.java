/*
 * Created on 2005-4-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.applet;

import java.awt.Dimension;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.common.ChartPropertyPaneFactory;
import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.common.ChartTypePanel;
import com.ufida.report.chart.panel.ChartPropertyPane;
import com.ufida.report.chart.panel.DataPropertyPane;
import com.ufida.report.chart.property.ChartProperty;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.Wizard;
import com.ufsoft.report.dialog.WizardPage;

/**
 * @author caijie
 */
public class SetDimChartWizard extends  Wizard{
    
    /**
     * ͼ�����Ϣҳ   
     */
    private class ChartTypePage extends WizardPage {         
/**
		 * 
		 */
		private static final long serialVersionUID = -5730342664380899411L;
////        private JList m_categoryList = null;//ͼ���б�
////        private JList m_typeList = null;//ͼ���б�        
//        private int[] m_typeChartArr = null;//ͼ���б��Ӧ����
        private int m_resultChartIndex = ChartPublic.CLUSTERED_BAR_CHART;//����Ҫ���õ�ͼ��
//        private int[] m_chartCategoryIndex = ChartPublic.getAllBasicType();
        
        ChartTypePanel m_typePanel = null;
        public ChartTypePage(ChartTypePanel panel) {
            super(panel.getName());
            m_typePanel = panel;  
            
            init();
        } 
        private void init() {   
            this.setLayout(null);
            this.add(m_typePanel); 
            this.setName(m_typePanel.getName());
         //   initCategoryAndTypeList(); 
           
            
        }
        
//        private void initCategoryAndTypeList() {
//            //׼��ͼ��       
//            String[] items = new String[m_chartCategoryIndex.length];
//            for(int i = 0; i < m_chartCategoryIndex.length; i++){
//                items[i] = ChartPublic.getChartName(m_chartCategoryIndex[i]);
//            }         
//            m_resultChartIndex = ChartPublic.UNDIFINED;
//            //����ͼ��
//            m_typePanel.getSubChartTypeList().setModel(new DefaultListModel());
//            m_typePanel.getSubChartTypeList().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            m_typePanel.getSubChartTypeList().addListSelectionListener(new ListSelectionListener(){
//                public void valueChanged(ListSelectionEvent e) {               
//                    int selected = m_typePanel.getSubChartTypeList().getSelectedIndex();
//                    if(selected == -1) {
//                        m_resultChartIndex = ChartPublic.UNDIFINED;
//                        m_typePanel.getTypeDescArea().setText(null);
//                        return;  
//                    }
//                    m_resultChartIndex = m_typeChartArr[selected];
//                    m_typePanel.getTypeDescArea().setText(ChartPublic.getChartHint(m_resultChartIndex));
//                }});
//            
//            //����ͼ���б�       
//            DefaultListModel model = new DefaultListModel();
//            for(int i = 0; i < items.length; i++)
//                model.addElement(items[i]);       
//            m_typePanel.getChartTypeList().setModel(model);
//            m_typePanel.getChartTypeList().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);       
//            
//            //��ͼ��ı�ʱ�Զ�����ѡȡͼ�������ͼ��������ͼ�����ʾ��Ϣ
//            m_typePanel.getChartTypeList().addListSelectionListener(new ListSelectionListener(){
//             public void valueChanged(ListSelectionEvent e) {
//                 int selected = m_typePanel.getChartTypeList().getSelectedIndex();            
//                 if(selected == -1 
//                         || ChartPublic.getSubTypes(m_chartCategoryIndex[selected]) == null 
//                         || ChartPublic.getSubTypes(m_chartCategoryIndex[selected]).isEmpty()) {
//                     DefaultListModel m = (DefaultListModel)m_typePanel.getSubChartTypeList().getModel();
//                     m.removeAllElements();
//                     m_typeChartArr = null;
//                     m_typePanel.getTypeDescArea().setText(null);
//                     m_resultChartIndex = ChartPublic.UNDIFINED;
//                     return;
//                 }
//                 
//                 ArrayList list = ChartPublic.getSubTypes(m_chartCategoryIndex[selected]);            
//                 m_typeChartArr = new int[list.size()];
//                 for(int i = 0; i < list.size();i++)
//                     m_typeChartArr[i] =((Integer)list.get(i)).intValue();                    
//                 DefaultListModel m = (DefaultListModel)m_typePanel.getSubChartTypeList().getModel();
//                 m.removeAllElements();
//                 for(int i = 0; i < m_typeChartArr.length; i++)                
//                     m.addElement(ChartPublic.getChartName(m_typeChartArr[i]));   
//                 m_typePanel.getSubChartTypeList().setSelectedIndex(0);
//                 m_resultChartIndex = m_typeChartArr[0];
//                 m_typePanel.getTypeDescArea().setText(ChartPublic.getChartHint(m_resultChartIndex)); 
//             }});
//            
//            //��ʼ��ֵ
//            int initialCatIndex = 0;
//            m_typePanel.getChartTypeList().setSelectedIndex(initialCatIndex);       
//            ArrayList subTypes = ChartPublic.getSubTypes(m_chartCategoryIndex[initialCatIndex]);
//            if((subTypes != null) && (!subTypes.isEmpty())){
//                m_typeChartArr = new int[subTypes.size()];
//                for(int i = 0; i < subTypes.size();i++)
//                    m_typeChartArr[i] =((Integer)subTypes.get(i)).intValue();           
//                DefaultListModel md = (DefaultListModel)m_typePanel.getSubChartTypeList().getModel();
//                md.removeAllElements();
//                for(int i = 0; i < m_typeChartArr.length; i++)                
//                    md.addElement(ChartPublic.getChartName(m_typeChartArr[i]));
//                m_typePanel.getSubChartTypeList().setSelectedIndex(0);
//                m_resultChartIndex = m_typeChartArr[0];
//                m_typePanel.getTypeDescArea().setText(ChartPublic.getChartHint(m_resultChartIndex));          
//               
//            }        
//        }
        
        public boolean canFlipToNextPage() {
            boolean yes = super.canFlipToNextPage();            
    		if(yes) { //����Ƿ�ͼ���͸ı䣬������Ҫ�ı���������ҳ��
    		    whenChartTypeChanged();
    		}
    		return yes;
    	}
        
        public int getChartType() {
            return m_resultChartIndex;
        }      
        
        /**��ͼ���͸ı�����Ҫ�ı���������ҳ��**/
        private void whenChartTypeChanged() {
            m_chartProperty = ChartPublic.createChartProperty(getChartType());
//            DimDataProperty dataProp = new DimDataProperty(new MultiDimemsionModel(""), m_chartProperty.getDataProperty().getDataType(), UfoReport.OPERATION_FORMAT, userID);            
//            m_chartProperty.getPlotProperty().setDataProperty(dataProp);
//            m_dimChartmodel.setDatsetType(prop.getDataProperty().getDataType());
//            prop.getPlotProperty().setDataProperty(m_dimChartmodel);
//            m_dimChartmodel.setChartProperty(prop);            
            updateDataPropertyPage();          
            
        }
        
    }    
   
    /**
     * ��������   
     */
    private class DataPropPage extends WizardPage{   
        /**
		 * 
		 */
		private static final long serialVersionUID = -3239876192399973300L;
		private DataPropertyPane m_dataPane =null;
        /**
		 * @i18n uibichart00008=����Դ����
		 */
        public DataPropPage() {
            super(StringResource.getStringResource("uibichart00008"));
                 
        }  

        public DataPropertyPane getDataPropertyPane() {           
           return m_dataPane;
        }
        
        public boolean canFlipToNextPage() {
            boolean yes = super.canFlipToNextPage();            
    		if(yes) { //�������Ըı��ı�ͼ������    
    		    getChartProperty().getPlotProperty().setDataProperty(m_dataPane.getDataProperty());    		    
    		    updateChartPropertyPage();
    		}
    		return yes;
    	}      
                
        public void setDataPropertyPane(DataPropertyPane dataPane) {           
            this.removeAll();
            if(dataPane != null) {
                this.add(dataPane);
                this.setName(dataPane.getName());
            }            
            m_dataPane = dataPane;
        }
    }
    
   /**
    * ͼ����ҳ��
    */   
    protected class ChartPropertyPage extends WizardPage{        
        /**
	 * 
	 */
	private static final long serialVersionUID = -7792875949410869293L;
		private ChartPropertyPane m_chartPane =null;
        /**
		 * @i18n ubichart00024=ͼ����
		 */
        public ChartPropertyPage() {
           super(StringResource.getStringResource("ubichart00024"));                 
        }  
        
        public ChartPropertyPane getChartPropertyPane() {           
          return m_chartPane;
        } 
        /**
         *����ͼ�������       
         */
        public void setChartPropertyPane(ChartPropertyPane chartPane) {           
            this.removeAll();
            if(chartPane != null) {
                this.add(chartPane);
                this.setName(chartPane.getName());
            }            
            m_chartPane = chartPane;
        } 
    }
       
    /**
     * ͼ���Զ���
     */
    private ChartProperty m_chartProperty =null;
    private ChartTypePage m_chartTypePage = null;//ͼ������ҳ��
    private DataPropPage m_dataAreaPage = null;//��������ҳ��
    private ChartPropertyPage m_chartPropPage = null;//ͼ����ҳ��
    private Dimension m_pageSize = ChartPublic.DEFAULT_PROPERTYPANEL_SIZE;
    public SetDimChartWizard(String userID, String reportID) {
        super();
        init();       
    }
    private void init(){   
//      ����ҳ��Ϊδ���
        getChartTypePage().setPageFinish(true);
        getChartTypePage().setWizard(this);        
        getChartTypePage().setPreferredSize(m_pageSize);
		getChartTypePage().setSize(m_pageSize);
		getChartTypePage().setMaximumSize(m_pageSize);
		getChartTypePage().setMinimumSize(m_pageSize);
		
        getDataPropPage().setPageFinish(true);
        getDataPropPage().setWizard(this);
        getDataPropPage().setPreviousPage(getChartTypePage()); 
        getDataPropPage().setPreferredSize(m_pageSize);
        getDataPropPage().setSize(m_pageSize);
        getDataPropPage().setMaximumSize(m_pageSize);
        getDataPropPage().setMinimumSize(m_pageSize);
        
        getChartPropertyPage().setPageFinish(true);
        getChartPropertyPage().setWizard(this);
        getChartPropertyPage().setPreviousPage(getDataPropPage()); 
        
        this.addPage(getChartTypePage());
        this.addPage(getDataPropPage());  
        this.addPage(getChartPropertyPage()); 
    }
     
    protected WizardPage getChartTypePage() {
        if(m_chartTypePage == null)
            m_chartTypePage = new ChartTypePage(new ChartTypePanel(ChartPublic.BAR_CHART));
        return m_chartTypePage;
    }
    protected WizardPage getDataPropPage(){
        if(m_dataAreaPage == null) {
            m_dataAreaPage = new DataPropPage();
        }
            
        return m_dataAreaPage;
    }
    protected WizardPage getChartPropertyPage(){
        if(m_chartPropPage == null) {           
            m_chartPropPage = new ChartPropertyPage();
        }            
        return m_chartPropPage;
    }
    
    /**
     * ��������Դҳ��
     */
    private void updateDataPropertyPage() {
//        DataPropPage page = (DataPropPage) this.getDataPropPage();        
//        if(page.getDataPropertyPane() != null) {//ɾ��������
//            page.removeAll();
//        }
//        
//        DataPropertyPane pane = SetDimChartDlg.createDataPropertyPane((DimDataProperty) getChartProperty().getDataProperty());
//        if(pane != null) {
//            page.setDataPropertyPane(pane);
//        }else {
//            pane.removeAll();
//        }              
    }
    /**
     * ����ͼ��ʽҳ��
     */
    private void updateChartPropertyPage() {
        ChartPropertyPage page = (ChartPropertyPage) this.getChartPropertyPage();        
        if(page.getChartPropertyPane() != null) {//ɾ��������
            page.removeAll();
        }
        if(getChartProperty() != null){
        	  JFreeChart[] charts = getChartProperty().getChart();
              if(charts != null){
            	  ChartPropertyPane pane = ChartPropertyPaneFactory.createChartPropertyPane(getChartProperty(),charts[0]);
                  page.setChartPropertyPane(pane);
              }
              
        }
      
    }
    
    public ChartProperty getChartProperty() {          
        return m_chartProperty;
    }
}
