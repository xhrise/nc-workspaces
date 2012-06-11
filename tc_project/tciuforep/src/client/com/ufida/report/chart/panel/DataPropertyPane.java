/*
 * Created on 2005-4-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import junit.framework.Assert;

import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.property.IDataProperty;

/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataPropertyPane extends nc.ui.pub.beans.UITabbedPane{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IDataProperty m_dataProperty = null;
    
    public DataPropertyPane(IDataProperty dataProperty) {        
        Assert.assertNotNull(dataProperty);
        this.setPreferredSize(ChartPublic.DEFAULT_PROPERTYPANEL_SIZE);
        m_dataProperty = dataProperty;
    }
        
    /**
     * @return Returns the dataProperty.
     */
    public IDataProperty getDataProperty() {
        return m_dataProperty;
    }
}
