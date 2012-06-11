/*
 * DimChartPlugin.java
 * 创建日期 2005-2-17
 * Created by CaiJie
 */
package com.ufida.report.chart.applet;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;

import org.jfree.chart.JFreeChart;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.model.DimChartException;
import com.ufida.report.chart.model.DimChartModel;
import com.ufida.report.chart.property.ChartProperty;
import com.ufida.report.multidimension.applet.SelDimSetDialog;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.rep.applet.BIPlugIn;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * @author caijie 
 * @since 3.1
 */
public class DimChartPlugin extends BIPlugIn implements PropertyChangeListener{
    private DimChartModel m_chartModel = null;
    
    private String pk_report = null;
    
    private DimChartComponet m_chartComp=null;
    

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
        

		try {
			//初始化模型
			BIContextVO context = (BIContextVO) getReport().getContextVo();
			pk_report = context.getReportPK();

			m_chartModel = (DimChartModel) context.getBaseReportModel();			


			if(m_chartModel == null) {
				m_chartModel = new DimChartModel("");
				CreateDimChartWirzardDlg dlg = new CreateDimChartWirzardDlg(this, new SetDimChartWizard0(((BIContextVO)getReport().getContextVo()).getCurUserID(), ((BIContextVO)getReport().getContextVo()).getReportPK()));  

		          dlg.show();
		          if(dlg.getSelectOption() == BaseDialog.OK_OPTION) {
		              ChartProperty chart = dlg.getChartProperty();
		              if(chart == null)exitPlugin(); 
		              try {
		            	  m_chartModel.addChartProperty(chart);
		            } catch (DimChartException e) {
		            	AppDebug.debug(e);
		            	exitPlugin();
		            }
		          } else{
		        	  exitPlugin();
		          }
	        }
			m_chartModel.addChangeListener(this);    			
	        this.setOperationState(getReport().getOperationState());	 
	        SelDimModel selDim = getModel().getMultiDimemsionModel().getSelDimModel();
			getModel().getMultiDimemsionModel().setSelDimModel(selDim);
		} catch (Exception e1) {
			AppDebug.debug(e1);
			return;
		}

	
    }

    private void exitPlugin(){
    	 AppDebug.debug("DimChartPlugin error when startup");//@devTools System.out.println("AdhocPlugin error when startup");
         SwingUtilities.getWindowAncestor(getReport()).dispose();
    }
    public void setOperationState(int operationState) {
		this.getModel().setOperationState(operationState,null, ((BIContextVO)getReport().getContextVo()).getCurUserID());
	}
    
    public int getOperationState() {
     return this.getModel().getOperationState();   
    }
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
        
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#store()
     */
    /**
	 * @i18n uibichart00038=fail to save report！
	 */
    public void store() {
        boolean local = false;//序列化文件是否在本地       
        
        if(local) {
            String filePath = "C:\\BIReport.rep";
            try {
    			
    			ObjectOutputStream out = new ObjectOutputStream(
    					new FileOutputStream(filePath));
    			out.writeObject(m_chartModel);
    			out.close();
    		} catch (Exception e) {
    			AppDebug.debug(e);
    			JOptionPane.showMessageDialog(getReport(), "Error ",
    					StringResource.getStringResource("uibichart00038"), JOptionPane.ERROR_MESSAGE);
    		}
    		setDirty(false);
        }else {
            ReportSrv srv = new ReportSrv();
    		ReportVO vo = null;
    		ValueObject[] vo2 = null;
    		try {
    			if (pk_report != null) {
    				vo = ((ReportVO) (srv.getByIDs(new String[] { pk_report }))[0]);
    				vo.setDefinition(getModel());
    				srv.update(new ReportVO[] { vo });
    			} else {
    				vo = new ReportVO();
    				vo.setDefinition(getModel());
    				vo.setType(new Integer(ReportResource.INT_REPORT_CHART));
    				vo2 = srv.create(new ReportVO[] { vo });
    			}
    			if (vo2 != null) {
    				pk_report = vo2[0].getPrimaryKey();
    			}
//    			AppDebug.debug();//@devTools System.out.println();
    			AppDebug.debug("saved char pk = " + pk_report);//@devTools System.out.println("saved char pk = " + pk_report);
//    			AppDebug.debug();//@devTools System.out.println();
    		} catch (BusinessException e) {
    			// TODO Auto-generated catch block
    			AppDebug.debug(e);
    		}
        }
    }

    /**
	 * 打开存储的报表	
	 */
	public void OpenChartReport() {	   
	    boolean local = false;//序列化文件是否在本地
        try{            
            if(local) {// 从本地打开
                String filePath = "C:\\ChartReport.rep";	        
    			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
    			m_chartModel  = (DimChartModel)in.readObject();	
    			in.close();
            }else {//从服务器打开
                ReportSrv reportSrv = new ReportSrv();
    	        pk_report = "0001AA100000000060TK";
    	        
    	        ValueObject[] vos = reportSrv.getByIDs(new String[] {pk_report});
    	        if(vos == null || vos.length == 0) return ;
    	        
    	        if(vos[0] == null || !(vos[0] instanceof ReportVO)) return ;
    	        
    	        ReportVO reportVO = (ReportVO) vos[0];
    	        
    	        BaseReportModel model = (BaseReportModel)reportVO.getDefinition();
    	        if((model != null )&& (model instanceof DimChartModel)) {
    	            m_chartModel =  (DimChartModel) reportVO.getDefinition();
    	        }else {
    	            throw new RuntimeException();
    	        }    	        
            }
            if(m_chartModel.getOperationState() != getReport().getOperationState()) {
                setOperationState(getReport().getOperationState());
            }
            m_chartModel.addChangeListener(this);
            createDimChartComp(m_chartModel.getCharts());
		}catch(Exception e){
			AppDebug.debug(e);
			JOptionPane.showMessageDialog(getReport(), StringResource.getStringResource("ubichart00001"),
					StringResource.getStringResource("ubichart00001"), JOptionPane.ERROR_MESSAGE);//打开报表时出错，打开失败
		}  
	}
    
    public DimChartModel getModel() {
        return m_chartModel;
    }
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new DimChartDescriptor(this);
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
     */
    public boolean isDirty() {
        return false;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        return new String[]{ChartPublic.EXT_FMT_CHART};
    }
   

    

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        return new ChartCellRenderer();
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {        
        return null;
    }

    public class ChartCellRenderer implements SheetCellRenderer{
        public Component getCellRendererComponent(CellsPane cellsPane, Object value,
				boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {				
			if(value == null) {
			    return null;
			}else{
			    return (Component) value;
			}
		}
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt != null) {
            if(evt.getPropertyName().equals(DimChartModel.ChartChanged)) {    
                if(this.m_chartComp == null) {
                	createDimChartComp(getModel().getCharts());
                }else {
                	updateAllCharts(getModel().getCharts());
                }
                
            }
        }
        
    }
    
    private void updateAllCharts(JFreeChart[] charts){
 
    	if(m_chartComp!=null)
    		m_chartComp.updateAllCharts(charts);
    	getReport().resetGlobalPopMenuSupport();
    }
    private  void createDimChartComp(JFreeChart[] charts) {  
    	m_chartComp=new DimChartComponet(m_chartModel,charts);

        getReport().getReportNavPanel().setMidComp(m_chartComp);
        getReport().resetGlobalPopMenuSupport();
    }
    
    public DimChartPanel getDimChartPanel() {
        return m_chartComp==null?null:m_chartComp.getDimChartPanel();
    }
    public DimChartComponet getChartComp(){
    	return m_chartComp;
    }
}
