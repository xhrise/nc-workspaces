/*
 * Created on 2005-7-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.adhoc.applet;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectQueryModelPanel extends UIPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5002484859417502463L;

	private JList allQueryList = null;
	private JList allFieldList = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JTextField dataCountField = null;
	private JLabel jLabel2 = null;
	
	private transient EventListenerList listenerList =  new EventListenerList();
	/**
	 * This is the default constructor
	 */
	public SelectQueryModelPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		this.setSize(600,400);
		this.setLayout(null);
		this.setName(StringResource.getStringResource("mbiAdhoc00001"));
		jLabel2 = new nc.ui.pub.beans.UILabel();
		jLabel1 = new nc.ui.pub.beans.UILabel();
		jLabel = new nc.ui.pub.beans.UILabel();		
		jLabel.setBounds(12, 7, 106, 20);
		jLabel.setText(StringResource.getStringResource("mbiadhoc00002"));
		jLabel1.setBounds(220, 7, 106, 20);
		jLabel1.setText(StringResource.getStringResource("mbiadhoc00002"));
//		jLabel2.setBounds(277, 11, 64, 17);
//		jLabel2.setText("数据总数:");
		this.add(jLabel, null);
		this.add(jLabel1, null);
		this.add(getJScrollPane(), null);
		this.add(getJScrollPane1(), null);
//		this.add(getOKBtn(), null);
//		this.add(getCancelBtn(), null);
		this.add(getDataCountField(), null);
		this.add(jLabel2, null);
	}
	
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getAllQueryList() {
		if (allQueryList == null) {
			allQueryList = new UIList(new DefaultListModel());
			allQueryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			QueryModelVO[] qms = QueryModelSrv.getQueryModel("1=1");
			if(qms != null) {
			    DefaultListModel model =  (DefaultListModel) allQueryList.getModel();
			    for(int i = 0; i < qms.length; i++) {
			        model.add(i, qms[i]);
			    }			    
			}
			
			allQueryList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    DefaultListModel model =  (DefaultListModel) getAllFieldList().getModel();
                    model.removeAllElements();
                    
                    if(allQueryList.getSelectedValue() != null) {
                        QueryModelVO vo = (QueryModelVO) allQueryList.getSelectedValue();
                        notifyListeners(new PropertyChangeEvent(this,AdhocPublic.QueryModelVOChanged, null, vo));
                        
                        MetaDataVO[] flds = QueryModelSrv.getSelectFlds(vo.getID());
            	        if(flds != null) {
            	            for (int i = 0; i < flds.length; i++) {
            	                model.add(i, flds[i]); 
            	            }
            	        }  
            	        
            	        //显示每个查询对象的数据总数
//            	        try {                            
//                            BIMultiDataSet multiDataSet = MultiSrv.getMaterQueryResult(vo.getID(), null);
//                            if(multiDataSet == null) {
//                                getDataCountField().setText("0");
//                            }else {
//                                getDataCountField().setText(String.valueOf(multiDataSet.getDataSet().getRowCount()));
//                            }
//                        } catch (Exception e1) {                        
//                        }
            	    }else {
            	        throw new RuntimeException();
            	    }
                    
                }
			    });
		
			if(qms != null) {			    
			    allQueryList.setSelectedIndex(0);
			}
		}
		return allQueryList;
	}
	/**
	 * This method initializes jList1	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getAllFieldList() {
		if (allFieldList == null) {
			allFieldList = new UIList(new DefaultListModel());
		}
		return allFieldList;
	}
	public QueryModelVO getQueryModel() {
	    if(this.getAllQueryList().getSelectedValue() != null) {
	        return (QueryModelVO) this.getAllQueryList().getSelectedValue();
	    }
	    return null;
	}
	public Object[] getSelQueryModels() {
	    if(this.getAllQueryList().getSelectedValue() != null) {
	        return  this.getAllQueryList().getSelectedValues();
	    }
	    return null;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(13, 38, 190, 270);
			jScrollPane.setViewportView(getAllQueryList());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setBounds(222, 38, 190, 270);
			jScrollPane1.setViewportView(getAllFieldList());
		}
		return jScrollPane1;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getDataCountField() {
		if (dataCountField == null) {
			dataCountField = new UITextField();
//			dataCountField.setBounds(345, 9, 58, 18);
		}
		return dataCountField;
	}
	
	/**
     * Registers an object for notification of changes to the dataset.
     *
     * @param listener  the object to register.
     */
    public void addChangeListener(PropertyChangeListener listener) {
        this.listenerList.add(PropertyChangeListener.class, listener);
    }

    /**
     * Deregisters an object for notification of changes to the dataset.
     *
     * @param listener  the object to deregister.
     */
    public void removeChangeListener(PropertyChangeListener listener) {
        this.listenerList.remove(PropertyChangeListener.class, listener);
    }
    

    /**
     * Notifies all registered listeners that the dataset has changed.
     *
     * @param event  contains information about the event that triggered the notification.
     */
    private  void notifyListeners(final PropertyChangeEvent event) {
        final Object[] listeners = this.listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PropertyChangeListener.class) {
                ((PropertyChangeListener) listeners[i + 1]).propertyChange(event);
            }
        }
    }
    public void setSelectedQueryModel(QueryModelVO queryVO){
    	if(queryVO == null)
    		return;
    	
	    DefaultListModel model =  (DefaultListModel) allQueryList.getModel();
	    for(int i = 0; i < model.size(); i++) {
	    	QueryModelVO listQuery =(QueryModelVO)model.getElementAt(i); 
	    	if(listQuery.getPrimaryKey().equals(queryVO.getPrimaryKey())){
	    		allQueryList.setSelectedIndex(i);
	    		break;
	    	}
	    }			    

    }
}
