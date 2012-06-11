/*
 * Created on 2005-7-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.applet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.report.chart.model.DimDataProvider;
import com.ufida.report.multidimension.applet.SelDimSetPanel;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectDimPanel extends nc.ui.pub.beans.UIPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JScrollPane jScrollPane = null;
	private JList allQueryList = null;
	
	private SelDimSetPanel selDimSetPanel = null;
	
	private DimDataProvider dataProperty = null;
	/**
	 * This is the default constructor
	 */
	public SelectDimPanel(DimDataProvider dataProp, SelDimSetPanel panel) {
		super();
		this.selDimSetPanel = panel;
		this.dataProperty = dataProp;
		if(panel == null || dataProperty == null) {
		    throw new IllegalArgumentException();
		}
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
        this.setLayout(null);
        this.setBounds(0, 0, 600, 400);
        this.setName(StringResource.getStringResource("ubichart00012"));
        this.add(getJPanel(), null);
        this.add(getJScrollPane(), null);
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
			jPanel.setBounds(105, 0, 495, 400);
			this.selDimSetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("ubichart00012"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanel.add(this.selDimSetPanel);
		}
		return jPanel;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane(getAllQueryList());
			jScrollPane.setBounds(0, 0, 105, 400);
//			jScrollPane.setViewportView(getAllQueryList());
			jScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("ubichart00013"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getAllQueryList() {
		if (allQueryList == null) {		
			allQueryList = new UIList(new DefaultListModel());
			allQueryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			QueryModelVO[] qms = QueryModelSrv.getQueryModel("1=1");
			if(qms != null) {
			    DefaultListModel model =  (DefaultListModel) allQueryList.getModel();
			    for(int i = 0; i < qms.length; i++) {
			        model.add(i, qms[i]);
			    }			    
			}	
			if(qms != null) {			    
			    allQueryList.setSelectedIndex(0);
			    whenQueryChanged();
			}
			
			allQueryList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    whenQueryChanged();
                }
			    });
		}
		return allQueryList;
	}
	
	private void whenQueryChanged() {
	    if(allQueryList.getSelectedValue() != null) {
	        QueryModelVO vo = (QueryModelVO) allQueryList.getSelectedValue();	        
	        SelDimModel model = new SelDimModel(this.dataProperty.getMultiDimemsionModel());//MultiDimensionUtil.createSelDimModel(vo.getID());
	        model.setQueryModel(vo);
	        selDimSetPanel.setSelModel(model, this.dataProperty.getUserID());                       
	        this.dataProperty.setSelDimModel(model);
	    }	    
	}
    /**
     * @return Returns the selDimSetPanel.
     */
    public SelDimSetPanel getSelDimSetPanel() {
        return selDimSetPanel;
    }
    }  //  @jve:decl-index=0:visual-constraint="10,10"
