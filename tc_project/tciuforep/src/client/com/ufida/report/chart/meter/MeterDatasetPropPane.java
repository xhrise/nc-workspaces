/*
 * Created on 2005-5-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.meter;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufida.report.chart.panel.DataPropertyPane;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MeterDatasetPropPane extends DataPropertyPane{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @param pageName
     * @param dataProperty
     */
    public MeterDatasetPropPane(MeterDatasetProperty dataProperty) {
//        super(null);
        super(null);
        this.add(getTestPanel().getName(), getTestPanel());
    }

    private JPanel getTestPanel() {
        JPanel result = new UIPanel();
        result.setName(StringResource.getStringResource("ubichart00161"));//仪表盘数据
        result.setPreferredSize(new  Dimension(400,300));
        result.setLayout(null);   
        JTextField dataArea = new UITextField();                
        dataArea.setBounds(new Rectangle(105, 140, 300, 35));

        JLabel jLabel1 = new nc.ui.pub.beans.UILabel();     
        jLabel1.setText(StringResource.getStringResource("ubichart00162"));//数据区域
        jLabel1.setBounds(new Rectangle(30, 140, 70, 35));
        result.add(dataArea, null);
        result.add(jLabel1, null);
        return result;
    }
}
