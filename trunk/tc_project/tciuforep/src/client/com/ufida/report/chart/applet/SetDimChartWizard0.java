/*
 * Created on 2005-4-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.applet;

import java.awt.Dimension;

import nc.ui.pub.beans.MessageDialog;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.common.ChartTypePanel;
import com.ufida.report.chart.model.DimChartPublic;
import com.ufida.report.chart.model.DimDataProvider;
import com.ufida.report.chart.property.ChartProperty;
import com.ufida.report.multidimension.applet.SelDimSetPanel;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.rep.applet.SelectQueryPanel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.Wizard;
import com.ufsoft.report.dialog.WizardPage;

/**
 * ��SetDimChartWizard��ʡȥ��ͼ�������õ�ҳ��
 * @author caijie
 */
public class SetDimChartWizard0 extends  Wizard{
    
    /**
     * ͼ�����Ϣҳ   
     */
    private class ChartTypePage extends WizardPage {             
        /**
		 * 
		 */
		private static final long serialVersionUID = -6204291947546272210L;
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
            }
        
        public boolean canFlipToNextPage() {
            boolean yes = super.canFlipToNextPage();            
    		if(yes) { //����Ƿ�ͼ���͸ı䣬������Ҫ�ı���������ҳ��
    		    whenChartTypeChanged();
    		}
    		return yes;
    	}
        
        public int getChartType() {
            return m_typePanel.getSelectedChartIndex();
        }      
        
        /**��ͼ���͸ı�����Ҫ�ı���������ҳ��**/
        private void whenChartTypeChanged() {
//            SetDimChartWizard0 wizard = (SetDimChartWizard0) this.getWizard();            
            m_chartProperty =  DimChartPublic.createDimChartProperty(null, getChartType(), userID, reportID);            
           // ((DimModelPage)getDimModelPage()).refresh(); 
        }        
    }    
    private class SelectQueryPage extends WizardPage{   
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SelectQueryPanel m_selectQueryPanel = null;
        /**
		 * @i18n uibichart00039=��άģ��
		 */
        public SelectQueryPage() {
            super(StringResource.getStringResource("uibichart00039"));
            m_selectQueryPanel = new SelectQueryPanel(userID);            
            this.add(m_selectQueryPanel);
           // this.setLayout(null);
         
            m_selectQueryPanel.setPreferredSize(m_pageSize);
            m_selectQueryPanel.setSize(m_pageSize);
            m_selectQueryPanel.setMaximumSize(m_pageSize);
            m_selectQueryPanel.setMinimumSize(m_pageSize);		
        }  
        
    	/**
    	 * ҳ���Ƿ��Ѿ����
    	 * 
    	 * @return boolean
    	 */
    	public boolean isPageFinish() {
            return true;
    	}   	
        
    	public SelectQueryPanel getSelectQueryPanel(){
    		return m_selectQueryPanel;
    	}
        
        public boolean canFlipToNextPage() {
            boolean yes = super.canFlipToNextPage();  
    		if(yes) { //�������Ըı��ı�ͼ������   
    		   getDimModelPage().refresh();
    		}
    		return yes;
    	}  
    }
    
    private class DimModelPage extends WizardPage{   
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SelDimSetPanel m_selDimSetPanel = null;
        /**
		 * @i18n uibichart00039=��άģ��
		 */
        public DimModelPage() {
            super(StringResource.getStringResource("uibichart00039"));
            this.setLayout(null); 
        }  
        
    	/**
    	 * ҳ���Ƿ��Ѿ����
    	 * 
    	 * @return boolean
    	 */
    	public boolean isPageFinish() {    	
        	String msg = m_selDimSetPanel.validateDim();
        	if(msg != null){
        		MessageDialog.showErrorDlg(this, null, msg);
        		return false;
        	}
            return true;
    	}
    	
        
        protected  void refresh(){
        	DimDataProvider dataProp = (DimDataProvider) getChartProperty().getDataProperty().getDataProvider();
            this.removeAll();  
            m_selDimSetPanel = new SelDimSetPanel();
            this.add(m_selDimSetPanel);

            QueryModelVO vo = (QueryModelVO) getSelectQueryPage().getSelectQueryPanel().getSelectedQueryModelVO();	        
	        SelDimModel model = new SelDimModel(dataProp.getMultiDimemsionModel());//MultiDimensionUtil.createSelDimModel(vo.getID());
	        model.setQueryModel(vo);
	        m_selDimSetPanel.setSelModel(model, dataProp.getUserID());                       
	        dataProp.setSelDimModel(model);	        
        }
        public boolean canFlipToNextPage() {
            boolean yes = super.canFlipToNextPage();  
    		if(yes) { //�������Ըı��ı�ͼ������      		    
    		    DimDataProvider dimDataPro = (DimDataProvider) getChartProperty().getPlotProperty().getDataProperty().getDataProvider();    		    
    		    dimDataPro.setSelDimModel(m_selDimSetPanel.getSelModel());  		    
    		   
    		}
    		return yes;
    	}  
    }
    /**
     * ��������   
     */
    private class DataPropPage extends WizardPage{  
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @i18n uibichart00040=��������
		 */
    	public DataPropPage() {
            super(StringResource.getStringResource("uibichart00040"));
            this.setLayout(null);
        }  
    	
        public boolean canFlipToNextPage() {
            boolean yes = super.canFlipToNextPage();             
    		if(yes) { //�������Ըı��ı�ͼ������  
    		    if(!canFinish()) {
    		        return false;
    		    }
    		  //  getChartProperty().getPlotProperty().setDataProperty(m_dataPane.getDataProperty());  		    
    		   
    		}
    		return yes;
    	}  
    }
       
    /**
     * ͼ���Զ���
     */
    private ChartProperty m_chartProperty =null;
    private ChartTypePage m_chartTypePage = null;//ͼ������ҳ��
    private SelectQueryPage m_selectQueryPage = null;
    private DimModelPage m_DimModelPage = null;//��άģ��ҳ��   
    private DataPropPage m_dataPropPage = null;//��������ҳ��
    
    private String userID = null;
    private String reportID = null;
    private Dimension m_pageSize = ChartPublic.DEFAULT_PROPERTYPANEL_SIZE;
    public SetDimChartWizard0(String userID, String reportID) {
        super();
        this.userID = userID;
        this.reportID = reportID;
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
		
		
		getSelectQueryPage().setPageFinish(true);
		getSelectQueryPage().setWizard(this);   
		getSelectQueryPage().setPreviousPage(getChartTypePage()); 		
		getSelectQueryPage().setPreferredSize(m_pageSize);
		getSelectQueryPage().setSize(m_pageSize);
		getSelectQueryPage().setMaximumSize(m_pageSize);
		getSelectQueryPage().setMinimumSize(m_pageSize);		
		
		getDimModelPage().setPageFinish(true);
		getDimModelPage().setWizard(this);   
		getDimModelPage().setPreviousPage(getSelectQueryPage()); 		
		getDimModelPage().setPreferredSize(m_pageSize);
		getDimModelPage().setSize(m_pageSize);
		getDimModelPage().setMaximumSize(m_pageSize);
		getDimModelPage().setMinimumSize(m_pageSize);
		
//        getDataPropPage().setPageFinish(true);
//        getDataPropPage().setWizard(this);
//        getDataPropPage().setPreviousPage(getDimModelPage()); 
//        getDataPropPage().setPreferredSize(m_pageSize);
//        getDataPropPage().setSize(m_pageSize);
//        getDataPropPage().setMaximumSize(m_pageSize);
//        getDataPropPage().setMinimumSize(m_pageSize);        
      
        this.addPage(getChartTypePage());
        this.addPage(getSelectQueryPage());        
        this.addPage(getDimModelPage());   
       // this.addPage(getDataPropPage());          
    }
    
    public boolean performFinish(){
    	DimModelPage page = (DimModelPage) getDimModelPage();
        String msg = page.m_selDimSetPanel.validateDim();
		if (msg != null) {
			MessageDialog.showErrorDlg(page, null, msg);
			return false;
		}
		return true;
    }
    protected WizardPage getChartTypePage() {
        if(m_chartTypePage == null)
            m_chartTypePage = new ChartTypePage(new ChartTypePanel(ChartPublic.CLUSTERED_BAR_CHART));
        return m_chartTypePage;
    }
    protected SelectQueryPage getSelectQueryPage(){
        if( m_selectQueryPage == null) {
        	m_selectQueryPage = new SelectQueryPage();
        }            
        return m_selectQueryPage;
    }
    protected DimModelPage getDimModelPage(){
        if(m_DimModelPage == null) {
        	m_DimModelPage = new DimModelPage();
        }            
        return m_DimModelPage;
    }
    protected WizardPage getDataPropPage(){
        if(m_dataPropPage == null) {
        	m_dataPropPage = new DataPropPage();
        }            
        return m_dataPropPage;
    }

    /**
     * ���¶�άģ��ҳ��
     */
    @SuppressWarnings("unused")
	private void updateDimModelPage() {
    	      
    }
   
    
    public ChartProperty getChartProperty() {          
        return m_chartProperty;
    }
}
