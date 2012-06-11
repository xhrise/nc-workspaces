package com.ufida.report.chart;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.UIComboBox;

import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.AxisPropEvent;
import com.ufsoft.iufo.fmtplugin.chart.AxisPropListener;
import com.ufsoft.iufo.fmtplugin.chart.ChartAxis;
import com.ufsoft.iufo.resource.StringResource;

 
 
/**
 * ChartAxis对应属性设置面板
 * @author liuyy
 *
 */
public class AxisPropPanel extends JPanel implements AxisPropListener {
	
	private static final long serialVersionUID = 1L;

	private JComboBox cbox = null;
	private JTextField fldCaption = null;
	private ChartAxis m_axis = null;
	private Field[] fields = null;
	
	/**
	 * @i18n miufo00211=关联字段：
	 * @i18n miufo00212=显示名称：
	 */
	public AxisPropPanel(String title, ChartAxis axis, Field[] flds){
		super();
		if(axis == null){
			throw new IllegalArgumentException();
		}

		this.m_axis = axis;
		this.fields = flds;
		
		setLayout(null);
		setPreferredSize(new Dimension(505, 44));
		
		setBorder(BorderFactory.createTitledBorder(null, title,
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));
		
		final JLabel label_4_1 = new JLabel();
		label_4_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label_4_1.setText(StringResource.getStringResource("miufo00211"));
		label_4_1.setBounds(10, 26, 81, 18);
		add(label_4_1);
		
		cbox = new JComboBox();
		cbox.setBounds(90, 22, 132, 27);
		add(cbox);
		
		final JLabel label_5_1 = new JLabel();
		label_5_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label_5_1.setText(StringResource.getStringResource("miufo00212"));
		label_5_1.setBounds(251, 26, 81, 18);
		add(label_5_1);

		fldCaption = new JTextField();
		fldCaption.setBounds(338, 24, 138, 22);
		add(fldCaption);
		
		cbox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Object selected = cbox.getSelectedItem();
				if (selected instanceof Field) {
					Field selectedFld = (Field) selected;
//					if (axisName.indexOf("Data") >= 0
//							&& !DataTypeConstant.isNumberType(selectedFld
//									.getDataType())) {
//						throw new RuntimeException("数据轴关联字段的数据类型必须为数值类型。");
//					}
					m_axis.setFieldId((selectedFld).getFldname());
					m_axis.setDataType(selectedFld.getDataType());
					
					m_axis.setLabel(selectedFld.getCaption());
					fldCaption.setText(selectedFld.getCaption());
					
				} else {
					m_axis.setFieldId(null);
					fldCaption.setText("");
					m_axis.setLabel(null);
				}
			}
			
		});
		
		fldCaption.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
				m_axis.setLabel(fldCaption.getText());
			}
			public void keyTyped(KeyEvent e) {
				
			}});
		

		initData();
		
	}
	
	private void initData(){
		
		String axixFieldId = m_axis.getFieldId();
		String label = m_axis.getLabel();
		
		Field selected = null;
		DefaultComboBoxModel cbm = new DefaultComboBoxModel();
		cbm.addElement("");
		if(fields != null){
			for (Field f : fields) {
				cbm.addElement(f);
				if (f.getFldname().equals(axixFieldId)) {
					selected = f;
				}
			}
		}
		cbox.setModel(cbm);
		if (selected != null) {
			cbox.setSelectedItem(selected);
			fldCaption.setText(label);
			
		} else {
			cbox.setSelectedItem("");
			fldCaption.setText(null);
		}

	}

	public void actionPerformed(AxisPropEvent e) {
		this.fields = (Field[]) e.getNewValue();
		initData();
		
	}
	
	
}
 