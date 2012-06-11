/*
 * GotoDialog.java
 * 创建日期 2004-11-15
 * Created by CaiJie
 */
package com.ufsoft.iufo.fmtplugin.location;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalRadioButtonUI;
import javax.swing.text.View;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITextField;

import com.sun.java.swing.SwingUtilities2;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.IInputValidator;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.TableUtilities;
import com.ufsoft.table.format.TableConstant;

/**
 * 定位对话框
 * 
 * @author 王宇光 2008-5-19
 * 
 */
public class GotoDialog extends UfoDialog {
	private UfoReport m_Report;
	/**
	 * 定位输入项
	 */
	private JPanel bottomPanel = null;
	private JPanel contentPanel = null;
	private JButton conditionButton = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JList conditionList = null;
	private JTextField positionTextField = null;
	private JLabel positionLabel = null;
	private JPanel conditionPanel = null;
	private Container container;
	private Object selectObject = null;
	private DefaultListModel positionListModel = null;
	/**
	 * @param rep
	 */
	public GotoDialog(UfoReport rep) {
		super(rep);
		m_Report = rep;
		init();
	}
	
	protected void init() {
		setResizable(false);
		container = getContentPane();
		container.setLayout(new BorderLayout());
		container.removeAll();
		JPanel westPanel = new UIPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(getContentPanel(), BorderLayout.CENTER);
		westPanel.add(getBottomPanel(), BorderLayout.SOUTH);
		container.add(westPanel,BorderLayout.CENTER);	
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(MultiLang.getString("uiuforep0000727"));//单元格定位
		pack();

	}

	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			try {
				bottomPanel = new UIPanel();
				bottomPanel.setLayout(new FlowLayout());
				bottomPanel.add(getConditionBtn());
				bottomPanel.add(getOkbtn());
				bottomPanel.add(getCancelbtn());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return bottomPanel;
	}

	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new UIPanel();
			GridBagLayout bagLayout = new GridBagLayout();
			contentPanel.setBorder(BorderFactory.createTitledBorder(StringResource.getStringResource("miufo1001079")));//定位
			contentPanel.setLayout(bagLayout);
			GridBagConstraints constraint = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 1,
					GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
							0), 0, 0);
			
			bagLayout.setConstraints(getConditionList(),
					constraint);
			contentPanel.add(getConditionList());
			bagLayout.setConstraints(getPositionLabel(),
					constraint);
			contentPanel.add(getPositionLabel());
			bagLayout.setConstraints(getPositionText(),
					constraint);
			contentPanel.add(getPositionText());

		}
		return contentPanel;
	}

	private JLabel getPositionLabel() {
		if (positionLabel == null) {
			positionLabel = new UILabel(MultiLang.getString("uiuforep0000722"));//引用位置
		}
		return positionLabel;
	}

	private JTextField getPositionText() {
		if (positionTextField == null) {
			positionTextField = new UITextField();

		}
		return positionTextField;
	}

	private javax.swing.JList getConditionList() {
		if (conditionList == null) {
			try {
				conditionList = new UIList();
				conditionList.setName("JListDynArea");
				conditionList.setPreferredSize(new Dimension(80, 120));
				conditionList
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				conditionList.setBorder(BorderFactory
						.createLineBorder(Color.BLACK));
				if(positionListModel == null){
					positionListModel = new DefaultListModel();
				}				
				conditionList.setModel(positionListModel);
				conditionList.addListSelectionListener(new ListSelectionListener(){

					public void valueChanged(ListSelectionEvent e) {
						Object selectValue = conditionList.getSelectedValue();
						if(positionTextField != null && selectValue != null){
							positionTextField.setText(selectValue.toString());
						}
					}
					
				});
				conditionList.addMouseListener(new MouseAdapter(){

					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() >= 2){
							Object selectValue = conditionList.getSelectedValue();
							if(selectValue != null){
								finishDialog(selectValue.toString());
							}
						}
						
					}
					
				});
				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return conditionList;
	}

	private JButton getConditionBtn() {
		if (conditionButton == null) {
			try {
				conditionButton = new UIButton();
				conditionButton.setName("locationCondition");
				conditionButton.setText(StringResource.getStringResource("miufo1004061"));//定位条件
				conditionButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						LocationConditionPanel conditionPanel = (LocationConditionPanel) getConditionPanel();
						container.removeAll();
						container.setLayout(new BorderLayout());
						container.add(conditionPanel,BorderLayout.CENTER);
						pack();
					}

				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return conditionButton;
	}

	/**
	 * 取消按钮
	 * 
	 * @param
	 * @return JButton
	 */
	private JButton getCancelbtn() {
		if (cancelButton == null) {
			try {
				cancelButton = new UIButton();
				cancelButton.setName("btCancel");
				cancelButton.setText(MultiLang.getString("uiuforep0000739"));//取笑
				cancelButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						setResult(UfoDialog.ID_CANCEL);
						close();
						return;

					}

				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return cancelButton;
	}

	/**
	 * 确定按钮
	 * 
	 * @param
	 * @return JButton
	 */
	private JButton getOkbtn() {
		if (okButton == null) {
			try {
				okButton = new UIButton();
				okButton.setName("btOk");
				okButton.setText(MultiLang.getString("uiuforep0000782"));//确定
				okButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						String inputString = getText();
						finishDialog(inputString);						
											
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return okButton;
	}

	/**
	 * 响应ok按钮的OK消息，用检测每个输入项, 如果所有的输入都满足检测,则关闭对话框.
	 */
	private void finishDialog(String inputString) {
		IInputValidator validator = new GotoValidator();
		if (!validator.isValid(inputString)) {
			return;
		}
		if(!positionListModel.contains(inputString)){
			if(positionListModel.size()>5){
				positionListModel.remove(0);
			}
			positionListModel.addElement(inputString);
		}	
		setSelectObject(new PositionLocation(m_Report, inputString));
		
		setResult(UfoDialog.ID_OK);
		// 关闭对话框
		dispose();
	}

	protected void setSelectObject(Object obj) {
		this.selectObject = obj;
	}

	protected Object getSelectObject() {
		return this.selectObject;
	}

	private String getText() {
		if (positionTextField != null) {
			return positionTextField.getText();
		}
		return null;
	}

	private JPanel getConditionPanel() {
//		if (conditionPanel == null) {
			conditionPanel = new LocationConditionPanel();
//		}
		return conditionPanel;
	}

	private class LocationConditionPanel extends UIPanel implements
			ActionListener {

		private JRadioButton postilRadioBtn = null;
		private JRadioButton constantRadioBtn = null;
		private JRadioButton formulaRadioBtn = null;
		private JRadioButton noValueRadioBtn = null;
		private JRadioButton measureRadioBtn = null;
		private JRadioButton refCellRadioBtn = null;
		private JRadioButton lastCellRadioBtn = null;
		private JRadioButton conditionRadioBtn = null;
		private JRadioButton dataTypeRadioBtn = null;
		private JRadioButton areaFormulaRadioBtn = null;
		private JRadioButton measureFormulaRadioBtn = null;
		private JRadioButton totalFormulaRadioBtn = null;
		private JRadioButton allMeasureRadioBtn = null;
		private JRadioButton curRepMeasureRadioBtn = null;
		private JRadioButton otherRepMeasureRadioBtn = null;
		private JRadioButton numberTypeRadioBtn = null;
		private JRadioButton charTypeRadioBtn = null;
		private JRadioButton dateTypeRadioBtn = null;
		private JRadioButton reportTypeRadioBtn = null;
		private JButton okButton = null;
		private JButton cancelButton = null;
		private JPanel radioPanel = null;
		private JPanel bottomPanle = null;

		public LocationConditionPanel() {
			init();
		}

		private void init() {
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(250, 300));
			add(getRadioPanel(), BorderLayout.CENTER);
			add(getBottomPanel(), BorderLayout.SOUTH);			
		}

		private JPanel getBottomPanel() {
			if (bottomPanle == null) {
				bottomPanle = new UIPanel();
				bottomPanle.setLayout(new FlowLayout());
				bottomPanle.add(getOkbtn());
				bottomPanle.add(getCancelbtn());
			}
			return bottomPanle;
		}

		private JPanel getRadioPanel() {
			if (radioPanel == null) {
				radioPanel = new UIPanel();
				radioPanel.setLayout(null);

				radioPanel.add(getPostilRadioBtn());
				radioPanel.add(getConstantRadioBtn());
				radioPanel.add(getFormulaRadioBtn());
				radioPanel.add(getMeasureRadioBtn());
				radioPanel.add(getAreaFormulaRadioBtn());
				radioPanel.add(getMeasureFormulaRadioBtn());
				radioPanel.add(getTotalFormulaRadioBtn());
				radioPanel.add(getAllMeasureRadioBtn());
				radioPanel.add(getCurRepMeasureRadioBtn());
				radioPanel.add(getOtherRepMeasureRadioBtn());
				radioPanel.add(getNoValueRadioBtn());
				radioPanel.add(getRefCellRadioBtn());
				radioPanel.add(getLastCellRadioBtn());
				radioPanel.add(getConditionRadioBtn());
				radioPanel.add(getDataTypeRadioBtn());
				radioPanel.add(getNumberTypeRadioBtn());
				radioPanel.add(getCharTypeRadioBtn());
				radioPanel.add(getDateTypeRadioBtn());
				radioPanel.add(getReportTypeRadioBtn());
				radioPanel.setBorder(BorderFactory.createTitledBorder(StringResource.getStringResource("miufopublic117")));//选择

				ButtonGroup group = new ButtonGroup();
				group.add(getPostilRadioBtn());
				group.add(getConstantRadioBtn());
				group.add(getFormulaRadioBtn());
				group.add(getMeasureRadioBtn());
				group.add(getNoValueRadioBtn());
				group.add(getRefCellRadioBtn());
				group.add(getLastCellRadioBtn());
				group.add(getConditionRadioBtn());
				group.add(getDataTypeRadioBtn());

				ButtonGroup meauserGroup = new ButtonGroup();
				meauserGroup.add(getAllMeasureRadioBtn());
				meauserGroup.add(getCurRepMeasureRadioBtn());
				meauserGroup.add(getOtherRepMeasureRadioBtn());

				ButtonGroup formulaGroup = new ButtonGroup();
				formulaGroup.add(getAreaFormulaRadioBtn());
				formulaGroup.add(getMeasureFormulaRadioBtn());
				formulaGroup.add(getTotalFormulaRadioBtn());

				ButtonGroup dataTypeGroup = new ButtonGroup();
				dataTypeGroup.add(getNumberTypeRadioBtn());
				dataTypeGroup.add(getCharTypeRadioBtn());
				dataTypeGroup.add(getDateTypeRadioBtn());
				dataTypeGroup.add(getReportTypeRadioBtn());
			}
			return radioPanel;
		}

		/**
		 * 返回 postilRadioBtn 特性值。
		 * 
		 * @return javax.swing.JRadioButton
		 */
		private javax.swing.JRadioButton getPostilRadioBtn() {
			if (postilRadioBtn == null) {
				try {
					postilRadioBtn = new UIRadioButton();
					postilRadioBtn.setName("postilRadioBtn");
					postilRadioBtn.setSelected(true);
					postilRadioBtn.setText(StringResource.getStringResource("miufo1004051"));// 批注
					postilRadioBtn.setLocation(30, 20);
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return postilRadioBtn;
		}

		/**
		 * 返回 postilRadioBtn 特性值。
		 * 
		 * @return javax.swing.JRadioButton
		 */
		private javax.swing.JRadioButton getConstantRadioBtn() {
			if (constantRadioBtn == null) {
				try {
					constantRadioBtn = new UIRadioButton();
					constantRadioBtn.setName("constantRadioBtn");
					constantRadioBtn.setSize(new Dimension(80,22));
					constantRadioBtn.setText(StringResource.getStringResource("miufo1004062"));// 常量
					constantRadioBtn.setLocation(140, 20);
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return constantRadioBtn;
		}

		/**
		 * 返回 formulaRadioBtn 特性值。
		 * 
		 * @return javax.swing.JRadioButton
		 */
		private javax.swing.JRadioButton getFormulaRadioBtn() {
			if (formulaRadioBtn == null) {
				try {
					formulaRadioBtn = new UIRadioButton();
					formulaRadioBtn.setName("formulaRadioBtn");

					formulaRadioBtn.setText(StringResource.getStringResource("uibimultical014"));// 公式
					formulaRadioBtn.setLocation(30, 40);
					formulaRadioBtn.addChangeListener(new ChangeListener() {

						public void stateChanged(ChangeEvent e) {
							if (formulaRadioBtn.isSelected()) {
								areaFormulaRadioBtn.setEnabled(true);
								measureFormulaRadioBtn.setEnabled(true);
								totalFormulaRadioBtn.setEnabled(true);

								areaFormulaRadioBtn.setSelected(areaFormulaRadioBtn
										.isSelected());
								measureFormulaRadioBtn.setSelected(measureFormulaRadioBtn
										.isSelected());
								totalFormulaRadioBtn.setSelected(totalFormulaRadioBtn
										.isSelected());
							} else {
								areaFormulaRadioBtn.setEnabled(false);
								measureFormulaRadioBtn.setEnabled(false);
								totalFormulaRadioBtn.setEnabled(false);
							}

						}

					});
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return formulaRadioBtn;
		}

		private JRadioButton getMeasureRadioBtn() {
			if (measureRadioBtn == null) {
				measureRadioBtn = new UIRadioButton();
				measureRadioBtn.setText(StringResource.getStringResource("ubiquery0108"));// 指标
				measureRadioBtn.setLocation(140, 40);
				measureRadioBtn.setSize(new Dimension(80,22));
				measureRadioBtn.addChangeListener(new ChangeListener() {

					public void stateChanged(ChangeEvent e) {
						if (measureRadioBtn.isSelected()) {
							allMeasureRadioBtn.setEnabled(true);
							curRepMeasureRadioBtn.setEnabled(true);
							otherRepMeasureRadioBtn.setEnabled(true);

							allMeasureRadioBtn.setSelected(allMeasureRadioBtn
									.isSelected());
							curRepMeasureRadioBtn.setSelected(curRepMeasureRadioBtn
									.isSelected());
							otherRepMeasureRadioBtn.setSelected(otherRepMeasureRadioBtn
									.isSelected());
						} else {
							allMeasureRadioBtn.setEnabled(false);
							curRepMeasureRadioBtn.setEnabled(false);
							otherRepMeasureRadioBtn.setEnabled(false);
						}

					}

				});
			}

			return measureRadioBtn;
		}

		private JRadioButton getAreaFormulaRadioBtn() {
			if (areaFormulaRadioBtn == null) {
				areaFormulaRadioBtn = new UIRadioButton();
				areaFormulaRadioBtn.setText(StringResource.getStringResource("miufo1001802"));//区域公式
				areaFormulaRadioBtn.setLocation(40, 60);
				areaFormulaRadioBtn.setUI(new RadioButtonUI());
				areaFormulaRadioBtn.setSelected(true);
				areaFormulaRadioBtn.setEnabled(false);
			}
			return areaFormulaRadioBtn;
		}

		private JRadioButton getMeasureFormulaRadioBtn() {
			if (measureFormulaRadioBtn == null) {
				measureFormulaRadioBtn = new UIRadioButton();
				measureFormulaRadioBtn.setText(StringResource.getStringResource("miufo1002807"));//指标公式
				measureFormulaRadioBtn.setUI(new RadioButtonUI());
				measureFormulaRadioBtn.setSelected(true);
				measureFormulaRadioBtn.setLocation(40, 80);
				measureFormulaRadioBtn.setEnabled(false);
			}
			return measureFormulaRadioBtn;
		}

		private JRadioButton getTotalFormulaRadioBtn() {
			if (totalFormulaRadioBtn == null) {
				totalFormulaRadioBtn = new UIRadioButton();
				totalFormulaRadioBtn.setText(StringResource.getStringResource("miufo1000910"));//汇总公式
				totalFormulaRadioBtn.setUI(new RadioButtonUI());
				totalFormulaRadioBtn.setSelected(true);
				totalFormulaRadioBtn.setLocation(40, 100);
				totalFormulaRadioBtn.setOpaque(false);
				totalFormulaRadioBtn.setEnabled(false);
			}
			return totalFormulaRadioBtn;
		}

		private JRadioButton getAllMeasureRadioBtn() {
			if (allMeasureRadioBtn == null) {
				allMeasureRadioBtn = new UIRadioButton();
				allMeasureRadioBtn.setText(StringResource.getStringResource("miufo1004063"));//全部指标
				allMeasureRadioBtn.setSize(new Dimension(80,22));
				allMeasureRadioBtn.setUI(new RadioButtonUI());
				allMeasureRadioBtn.setSelected(true);
				allMeasureRadioBtn.setEnabled(false);
				allMeasureRadioBtn.setLocation(150, 60);
			}
			return allMeasureRadioBtn;
		}

		private JRadioButton getCurRepMeasureRadioBtn() {
			if (curRepMeasureRadioBtn == null) {
				curRepMeasureRadioBtn = new UIRadioButton();
				curRepMeasureRadioBtn.setText(StringResource.getStringResource("miufo1004064"));//本表指标
				curRepMeasureRadioBtn.setUI(new RadioButtonUI());
				curRepMeasureRadioBtn.setSize(new Dimension(80,22));
				curRepMeasureRadioBtn.setEnabled(false);
				curRepMeasureRadioBtn.setLocation(150, 80);
			}
			return curRepMeasureRadioBtn;
		}

		private JRadioButton getOtherRepMeasureRadioBtn() {
			if (otherRepMeasureRadioBtn == null) {
				otherRepMeasureRadioBtn = new UIRadioButton();
				otherRepMeasureRadioBtn.setText(StringResource.getStringResource("miufo1004065"));//它表指标
				otherRepMeasureRadioBtn.setUI(new RadioButtonUI());
				otherRepMeasureRadioBtn.setSize(new Dimension(80,22));
				otherRepMeasureRadioBtn.setEnabled(false);
				otherRepMeasureRadioBtn.setLocation(150, 100);
			}
			return otherRepMeasureRadioBtn;
		}

		private JRadioButton getNoValueRadioBtn() {
			if (noValueRadioBtn == null) {
				noValueRadioBtn = new UIRadioButton();
				noValueRadioBtn.setText(StringResource.getStringResource("miufo1004066"));//空值
				noValueRadioBtn.setLocation(30, 120);
			}
			return noValueRadioBtn;
		}

		private JRadioButton getRefCellRadioBtn() {
			if (refCellRadioBtn == null) {
				refCellRadioBtn = new UIRadioButton();
				refCellRadioBtn.setSize(new Dimension(90,22));
				refCellRadioBtn.setText(StringResource.getStringResource("miufo1004067"));//引用单元格
				refCellRadioBtn.setLocation(140, 120);
			}
			return refCellRadioBtn;
		}

		private JRadioButton getLastCellRadioBtn() {
			if (lastCellRadioBtn == null) {
				lastCellRadioBtn = new UIRadioButton();
				lastCellRadioBtn.setText(StringResource.getStringResource("miufo1004068"));//最后一个单元格
				lastCellRadioBtn.setSize(new Dimension(110,22));
				lastCellRadioBtn.setLocation(30, 140);
			}
			return lastCellRadioBtn;
		}

		private JRadioButton getConditionRadioBtn() {
			if (conditionRadioBtn == null) {
				conditionRadioBtn = new UIRadioButton();
				conditionRadioBtn.setText(StringResource.getStringResource("miufo1004069"));//条件格式
				conditionRadioBtn.setSize(new Dimension(80,22));
				conditionRadioBtn.setLocation(140, 140);
			}
			return conditionRadioBtn;
		}

		private JRadioButton getDataTypeRadioBtn() {
			if (dataTypeRadioBtn == null) {
				dataTypeRadioBtn = new UIRadioButton();
				dataTypeRadioBtn.setText(MultiLang.getString("uiuforep0000800"));//数据类型
				dataTypeRadioBtn.setLocation(30, 160);
				dataTypeRadioBtn.addChangeListener(new ChangeListener() {

					public void stateChanged(ChangeEvent e) {
						if (dataTypeRadioBtn.isSelected()) {
							numberTypeRadioBtn.setEnabled(true);
							charTypeRadioBtn.setEnabled(true);
							dateTypeRadioBtn.setEnabled(true);
							reportTypeRadioBtn.setEnabled(true);

							numberTypeRadioBtn.setSelected(numberTypeRadioBtn
									.isSelected());
							charTypeRadioBtn.setSelected(charTypeRadioBtn.isSelected());
							dateTypeRadioBtn.setSelected(dateTypeRadioBtn.isSelected());
							reportTypeRadioBtn.setSelected(reportTypeRadioBtn
									.isSelected());
						} else {
							numberTypeRadioBtn.setEnabled(false);
							charTypeRadioBtn.setEnabled(false);
							dateTypeRadioBtn.setEnabled(false);
							reportTypeRadioBtn.setEnabled(false);
						}

					}

				});
			}
			return dataTypeRadioBtn;
		}

		private JRadioButton getNumberTypeRadioBtn() {
			if (numberTypeRadioBtn == null) {
				numberTypeRadioBtn = new UIRadioButton();
				numberTypeRadioBtn.setText(MultiLang.getString("uiuforep0000791"));//数值
				numberTypeRadioBtn.setLocation(40, 180);
				numberTypeRadioBtn.setUI(new RadioButtonUI());
				numberTypeRadioBtn.setSelected(true);
				numberTypeRadioBtn.setEnabled(false);
			}
			return numberTypeRadioBtn;
		}

		private JRadioButton getReportTypeRadioBtn() {
			if (reportTypeRadioBtn == null) {
				reportTypeRadioBtn = new UIRadioButton();
				reportTypeRadioBtn.setText(MultiLang.getString("uiuforep0000793"));//"表样"
				reportTypeRadioBtn.setUI(new RadioButtonUI());
				reportTypeRadioBtn.setSelected(true);
				reportTypeRadioBtn.setLocation(40, 240);
				reportTypeRadioBtn.setEnabled(false);
			}
			return reportTypeRadioBtn;
		}

		private JRadioButton getCharTypeRadioBtn() {
			if (charTypeRadioBtn == null) {
				charTypeRadioBtn = new UIRadioButton();
				charTypeRadioBtn.setText(MultiLang.getString("uiuforep0000792"));//"字符"
				charTypeRadioBtn.setUI(new RadioButtonUI());
				charTypeRadioBtn.setSelected(true);
				charTypeRadioBtn.setLocation(40, 200);
				charTypeRadioBtn.setEnabled(false);
			}
			return charTypeRadioBtn;
		}

		private JRadioButton getDateTypeRadioBtn() {
			if (dateTypeRadioBtn == null) {
				dateTypeRadioBtn = new UIRadioButton();
				dateTypeRadioBtn.setText(MultiLang.getString("uiuforep0001110"));//"日期"
				dateTypeRadioBtn.setUI(new RadioButtonUI());
				dateTypeRadioBtn.setLocation(40, 220);
				dateTypeRadioBtn.setSelected(true);
				dateTypeRadioBtn.setEnabled(false);
			}
			return dateTypeRadioBtn;
		}

		/**
		 * 取消按钮
		 * 
		 * @param
		 * @return JButton
		 */
		private JButton getCancelbtn() {
			if (cancelButton == null) {
				try {
					cancelButton = new UIButton();
					cancelButton.setName("btCancel");
					cancelButton
							.setText(MultiLang.getString("uiuforep0000739"));//取消
					cancelButton.addActionListener(this);
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return cancelButton;
		}

		/**
		 * 确定按钮
		 * 
		 * @param
		 * @return JButton
		 */
		private JButton getOkbtn() {
			if (okButton == null) {
				try {
					okButton = new UIButton();
					okButton.setName("btOk");
					okButton.setText(MultiLang.getString("uiuforep0000782"));//确定
					okButton.addActionListener(this);
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return okButton;
		}

		public void actionPerformed(ActionEvent e) {
			JButton sourceBtn = (JButton) e.getSource();
			if (sourceBtn == okButton) {
				if (postilRadioBtn.isSelected()) {
					setSelectObject(new PostilLocation(m_Report));
				} else if (constantRadioBtn.isSelected()) {
					setSelectObject(new ConstantLocation(m_Report));
				} else if (formulaRadioBtn.isSelected()) {
                    if(areaFormulaRadioBtn.isSelected()){
                    	setSelectObject(new FormulaLocation(m_Report,FormulaLocation.AREA_FORMULA));
                    }else if(measureFormulaRadioBtn.isSelected()){
                    	setSelectObject(new FormulaLocation(m_Report,FormulaLocation.MEASURE_FORMULA));
                    }else{
                    	setSelectObject(new FormulaLocation(m_Report,FormulaLocation.TOTAL_FORMULA));
                    }
				} else if (noValueRadioBtn.isSelected()) {
					setSelectObject(new NullValueLocation(m_Report));
				} else if (measureRadioBtn.isSelected()) {
					if(allMeasureRadioBtn.isSelected()){
						setSelectObject(new MeasureLocation(m_Report,MeasureLocation.ALL_MEASURES));
					}else if(curRepMeasureRadioBtn.isSelected()){
						setSelectObject(new MeasureLocation(m_Report,MeasureLocation.CURRENT_REPORT_MEASURES));
					}else{
						setSelectObject(new MeasureLocation(m_Report,MeasureLocation.OTHER_REPORT_MEASURES));
					}                    
				} else if (refCellRadioBtn.isSelected()) {
                    setSelectObject(new PrecedentsLocation(m_Report));
				} else if (lastCellRadioBtn.isSelected()) {
					setSelectObject(new LastCellLocation(m_Report));
				} else if (conditionRadioBtn.isSelected()) {
					setSelectObject(new ConditionFormatLocation(m_Report));
				} else if (dataTypeRadioBtn.isSelected()) {
                    if(numberTypeRadioBtn.isSelected()){
                    	setSelectObject(new DataFormatLocation(m_Report,TableConstant.CELLTYPE_NUMBER));
                    }else if(charTypeRadioBtn.isSelected()){
                    	setSelectObject(new DataFormatLocation(m_Report,TableConstant.CELLTYPE_STRING));
                    }else if(dateTypeRadioBtn.isSelected()){
                    	setSelectObject(new DataFormatLocation(m_Report,TableConstant.CELLTYPE_DATE));
                    }else{
                    	setSelectObject(new DataFormatLocation(m_Report,TableConstant.CELLTYPE_SAMPLE));
                    }
				}
				setResult(UfoDialog.ID_OK);
				close();
				return;
			} else if (sourceBtn == cancelButton) {
				setResult(UfoDialog.ID_CANCEL);
				close();
				return;
			}
		}
	}

	/**
	 * 定位验证
	 * 
	 * @author
	 * @since
	 */
	private class GotoValidator implements IInputValidator {

		/*
		 * @see com.ufsoft.report.dialog.IInputValidator#isValid(java.lang.String)
		 */
		public boolean isValid(String input) {
			if ((input == null) || input.length() == 0)
				return false;
			AreaPosition area = TableUtilities.getAreaPosByString(input);
			if (area == null) {
				
				UfoPublic.sendErrorMessage(MultiLang
						.getString("uiuforep0000855"), m_Report, null);// "引用无效"
				return false;
			}

			CellsModel cellsModel = m_Report.getCellsModel();
			int[] validHeaders = cellsModel.getValidHeaders();
			if (validHeaders == null || validHeaders.length < 2) {
				throw new IllegalArgumentException();
			}
			int validRow = validHeaders[0];
			int validCol = validHeaders[1];
						
			// 目标区域是一个区域单元，检查起始单元格引用是否有效
			if ((area.getStart() == null)
					|| (area.getStart().getColumn() > validCol)
					|| (area.getStart().getRow() > validRow)) {
				UfoPublic.sendErrorMessage(MultiLang
						.getString("uiuforep0000855"), m_Report, null);// 引用无效
				return false;
			}

			// 检查结束单元格引用是否有效
			if ((area.getEnd() == null)
					|| (area.getEnd().getColumn() > validCol)
					|| (area.getEnd().getRow() > validRow)) {
				UfoPublic.sendErrorMessage(MultiLang
						.getString("uiuforep0000855"), m_Report, null);// 引用无效
				return false;
			}
			
			return true;
		}
	}

	private class RadioButtonUI extends MetalRadioButtonUI{

		public synchronized void paint(Graphics g, JComponent c) {
            super.paint(g, c);
	        AbstractButton b = (AbstractButton) c;
	        ButtonModel model = b.getModel();
	        
	        Dimension size = c.getSize();

	        Font f = c.getFont();
	        g.setFont(f);
	        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);

	        Rectangle viewRect = new Rectangle(size);
	        Rectangle iconRect = new Rectangle();
	        Rectangle textRect = new Rectangle();

	        Insets i = c.getInsets();
			viewRect.x += i.left;
			viewRect.y += i.top;
			viewRect.width -= (i.right + viewRect.x);
			viewRect.height -= (i.bottom + viewRect.y);

	        Icon altIcon = b.getIcon();
	       
	        String text = SwingUtilities.layoutCompoundLabel(
	            c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
	            b.getVerticalAlignment(), b.getHorizontalAlignment(),
	            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
	            viewRect, iconRect, textRect, b.getIconTextGap());
	        // Draw the Text
	        if(text != null) {
	            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
	            if (v != null) {
	                v.paint(g, textRect);
	            } else {
	               int mnemIndex = b.getDisplayedMnemonicIndex();
	               if(model.isEnabled()) {
	                   // *** paint the text normally
	                   g.setColor(b.getForeground());
	               } else {
	                   // *** paint the text disabled           	   
	                   g.setColor(new Color(192,191,181));
	                  
	               }
	               
	               SwingUtilities2.drawStringUnderlineCharAt(c,g,text,
	                       mnemIndex, textRect.x, textRect.y + fm.getAscent());
		   }
		   if(b.hasFocus() && b.isFocusPainted() &&
		      textRect.width > 0 && textRect.height > 0 ) {
		       paintFocus(g,textRect,size);
		   }
	        }
	    }

		protected void paintFocus(Graphics g, Rectangle t, Dimension d){
	        
	    } 

	}
	
	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug(MultiLang.getString("uiuforep0000805"));//@devTools System.out.println(MultiLang.getString("uiuforep0000805"));// ---------
		// 未捕捉到的异常
		// ---------
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

}