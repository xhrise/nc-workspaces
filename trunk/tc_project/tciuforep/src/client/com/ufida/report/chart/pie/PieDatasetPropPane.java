package com.ufida.report.chart.pie;

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
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 * @author caijie
 */
public class PieDatasetPropPane extends DataPropertyPane {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * @param dataProperty
     */
    public PieDatasetPropPane(PieDatasetProperty dataProperty) {
        super(dataProperty);
        this.add(getTestPanel().getName(), getTestPanel());
    }
    
   
    /**
	 * @i18n uibichart00020=��ͼ��������
	 * @i18n ubichart00162=��������
	 */
    private JPanel getTestPanel() {
        JPanel result = new UIPanel();
        result.setName(StringResource.getStringResource("uibichart00020"));
        result.setPreferredSize(new  Dimension(400,300));
        result.setLayout(null);   
        JTextField dataArea = new UITextField();                
        dataArea.setBounds(new Rectangle(105, 140, 300, 35));

        JLabel jLabel1 = new nc.ui.pub.beans.UILabel();     
        jLabel1.setText(StringResource.getStringResource("ubichart00162"));
        jLabel1.setBounds(new Rectangle(30, 140, 70, 35));
        result.add(dataArea, null);
        result.add(jLabel1, null);
        return result;
    }
}
