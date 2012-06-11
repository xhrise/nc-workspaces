/*
 * Created on 2005-5-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import javax.swing.JPanel;

import org.jfree.chart.plot.CategoryPlot;

import com.ufida.report.chart.property.CategoryPlotProperty;

/**
 * 分类图属性面板
 * @author caijie
 */
public class CategoryPlotPropPane{
    CategoryPlotProperty m_property = null;
    CategoryPlot m_sample = null;  
    JPanel domainAxisPanel = null;
    JPanel rangeAxisPanel = null;
    JPanel[] propertyPanels = null;
    
    public CategoryPlotPropPane(CategoryPlotProperty plotProp, CategoryPlot plot) {
        m_property = plotProp;        
        m_sample = plot;
        if(m_property == null ||  m_sample == null)
            throw new IllegalArgumentException();       
    }
       
    
    /**
     * 分类轴面板
     * @return
     */
    protected JPanel getDomainAxisPanel() {
        if(domainAxisPanel == null) {
            domainAxisPanel = new CategoryAxisPropPanel(m_property.getDomainAxisProperty(),m_property.getDomainGridlineProperty(), m_sample);
        }
        return domainAxisPanel;
    }

    /**
     * 数据轴面板
     * @return
     */
    protected JPanel getRangeAxisPanel() {
        if(rangeAxisPanel == null) {
            rangeAxisPanel = new NumberAxisPropPanel(m_property.getRangeAxisProperty(), m_property.getRangeGridlineProperty(), m_sample);
        }
        return rangeAxisPanel;        
    }
      
    public JPanel[] getPropertyPanels() {
        
        if(propertyPanels == null) {
            propertyPanels =  new JPanel[2];
            propertyPanels[0] = getDomainAxisPanel();
            propertyPanels[1] = getRangeAxisPanel();
        }
        return propertyPanels;   
       
    }
}
