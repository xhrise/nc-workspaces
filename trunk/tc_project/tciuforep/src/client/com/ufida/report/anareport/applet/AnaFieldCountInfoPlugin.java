package com.ufida.report.anareport.applet;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.postil.PostilInternalFrame;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
/**
 * 分析报表：显示统计字段详细信息插件
 * @author wangyga 2008-9-23
 *
 */
public class AnaFieldCountInfoPlugin extends AbstractPlugIn{

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){

			@Override
			protected IExtension[] createExtensions() {
				AnaFieldCountInfoExt fieldInfoExt = new AnaFieldCountInfoExt(getReport());
				return new IExtension[]{fieldInfoExt};
			}
			
		};
	}

	private class AnaFieldCountInfoExt extends AbsActionExt{		
		private CellsModel m_cellsModel;
	    private UfoReport m_report;
	    
	    public AnaFieldCountInfoExt(UfoReport report){
	    	super();
			m_report = report;
			m_cellsModel = m_report.getCellsModel();	       			
			initFieldInfoMouseMotionAdapter(report);
	    }
	    
		@Override
		public UfoCommand getCommand() {
           return null;
		}

		@Override
		public Object[] getParams(UfoReport container) {
			 return null;	     
		}

		@Override
		public ActionUIDes[] getUIDesArr() {

			return null;
		}
				   
	    private class FieldInfoMouseMotionAdapter extends MouseMotionAdapter{
	        private CellsPane m_cellsPane;
	        final private PostilInternalFrame viewFrame=new PostilInternalFrame(false);
	        
	        FieldInfoMouseMotionAdapter(CellsPane cellspane){
	    		this.m_cellsPane=cellspane;
	    	}
	        
			@Override
			 public void mouseMoved(MouseEvent e) {
	            Point p = e.getPoint();
	            int row = m_cellsPane.rowAtPoint(p);
	            int col = m_cellsPane.columnAtPoint(p);
	            CellPosition cellPos=CellPosition.getInstance(row,col);
	            AnaReportModel anaModel = getAanRepModel();
	            if(!anaModel.isFormatState()){
	            	AreaPosition [] areas = new AreaPosition[]{AreaPosition.getInstance(cellPos, cellPos)};
	            	CellPosition[] cellPoses = anaModel.getFormatPoses(anaModel.getDataModel(), areas);
	            	if(cellPoses == null || cellPoses.length ==0)
	            		return;	 
	            	cellPos = cellPoses[0];
	            	
	            }
	            AnaRepField fld = (AnaRepField) anaModel.getFormatModel().getBsFormat(cellPos, AnaRepField.EXKEY_FIELDINFO);
	            if(fld != null && isInCtrlArea(row,col,p)){
	            	FieldCountDef countDef = fld.getFieldCountDef();
	            	if(countDef != null){
	            		if(!anaModel.isFormatState())
	            			viewFrame.setLocation(m_cellsPane,CellPosition.getInstance(row,col));
	            		else
	            			viewFrame.setLocation(m_cellsPane,cellPos);	            		
		            	viewFrame.setContent(countDef.getToolTipText());
		            	viewFrame.setSize(new Dimension(100,40));
		            	viewFrame.setVisible(true);
		            	m_cellsPane.add(viewFrame);
		            	SwingUtilities.invokeLater(new Runnable(){
							public void run() {
								((JViewport)viewFrame.getTextArea().getParent()).setViewPosition(new Point(0,0));
							}
		            	});
	            	}else{
	            		viewFrame.setVisible(false);
		            	m_cellsPane.remove(viewFrame);
	            	}
	            }else{
	            	viewFrame.setVisible(false);
	            	m_cellsPane.remove(viewFrame);
	            }
	        }
			 private boolean isInCtrlArea(int row,int col,Point p) {
	             Rectangle rect = m_cellsPane.getCellRect(CellPosition.getInstance(row,col),true);
	             int x = p.x-rect.x; 
	             int y = p.y-rect.y;
	             if(x < rect.width*1/2 || y > rect.height){
	                 return false;
	             }
	             return true;
	         }
	    }
	    

	    private boolean isFieldInfoMouseMotionAdapter(CellsPane pane) {
			boolean isHas = false;
			MouseMotionListener[] listeners = pane.getMouseMotionListeners();
			if (listeners != null) {
				for (int i = 0; i < listeners.length; i++) {
					if (listeners[i] instanceof FieldInfoMouseMotionAdapter) {
						isHas = true;
						break;
					}
				}
			}
			return isHas;
		}
	    
	    
	    private void initFieldInfoMouseMotionAdapter(UfoReport report) {
			CellsPane pane = null;
			JViewport view = null;

			if (report.getTable().getMainView() != null) {
				view = report.getTable().getMainView();
				pane = (CellsPane) view.getView();
				if (!isFieldInfoMouseMotionAdapter(pane)) {
					pane.addMouseMotionListener(new FieldInfoMouseMotionAdapter(pane));
				}
			}
			if (report.getTable().getRightView() != null) {
				view = report.getTable().getRightView();
				pane = (CellsPane) view.getView();
				if (!isFieldInfoMouseMotionAdapter(pane)) {
					pane.addMouseMotionListener(new FieldInfoMouseMotionAdapter(pane));
				}
			}
			if (report.getTable().getDownView() != null) {
				view = report.getTable().getDownView();
				pane = (CellsPane) view.getView();
				if (!isFieldInfoMouseMotionAdapter(pane)) {
					pane.addMouseMotionListener(new FieldInfoMouseMotionAdapter(pane));
				}
			}
			if (report.getTable().getRightDownView() != null) {
				view = report.getTable().getRightDownView();
				pane = (CellsPane) view.getView();
				if (!isFieldInfoMouseMotionAdapter(pane)) {
					pane.addMouseMotionListener(new FieldInfoMouseMotionAdapter(pane));
				}
			}
		}
		
	}
	
	
	private AnaReportModel getAanRepModel(){
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}

  
}
