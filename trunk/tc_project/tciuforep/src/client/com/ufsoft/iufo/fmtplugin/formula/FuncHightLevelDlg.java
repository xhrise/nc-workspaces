package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.script.util.FormulaCellExtPropertyUtil;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;

/**
 * 公式高级向导
 * @author wangyga
 *
 */
public class FuncHightLevelDlg extends UfoDialog implements ActionListener {

	private static final long serialVersionUID = 7974057817924309963L;

	private CellsPane cellsPane;

	private JPanel formulaEditPanel;

	private JPanel contentPanel;

	private JPanel btnPanel;

	/** 公式单元是否允许编辑的控制变量 */
	private int iFormulaEditType = 0;

	/** 不控制 */
	private JRadioButton radioNoControl;

	/** 可以编辑 */
	private JRadioButton radioEditYes;

	/** 不能编辑 */
	private JRadioButton radioEditNo;

	private JButton btnOK;

	private JButton btnCancel;

	public FuncHightLevelDlg(CellsPane cellsPane) {
		super(cellsPane);
		this.cellsPane = cellsPane;
		initialize();
	}

	/**
	 * @i18n miufo00631=公式高级向导
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("miufo00631"));
		this.setSize(new Dimension(350, 120));
		this.setContentPane(getContentPanel());
		this
				.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		initFormulaEditType();
	}

	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getFormulaEditPanel(), BorderLayout.CENTER);
			contentPanel.add(getBtnPanel(), BorderLayout.SOUTH);

			ButtonGroup formulaEditGroup = new ButtonGroup();
			formulaEditGroup.add(getRadioNoControl());
			formulaEditGroup.add(getRadioEditYes());
			formulaEditGroup.add(getRadioEditNo());
		}
		return contentPanel;
	}

	private JPanel getFormulaEditPanel() {
		if (formulaEditPanel == null) {
			formulaEditPanel = new UIPanel();
			formulaEditPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			formulaEditPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									StringResource
											.getStringResource("miufo1004047"),
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, null));
			formulaEditPanel.add(getRadioNoControl());
			formulaEditPanel.add(getRadioEditYes());
			formulaEditPanel.add(getRadioEditNo());

		}
		return formulaEditPanel;
	}

	/**
	 * 初始化公式单元是否允许编辑的radioButton
	 */
	private void initFormulaEditType() {	
		CellsModel cellsModel = cellsPane.getDataModel();
		IArea area = cellsModel.getSelectModel().getSelectedArea();
		ArrayList<CellPosition> cellPosList = cellsModel
				.getSeperateCellPos(area);
		int iFormulaEditType = FormulaCellExtPropertyUtil.FORMULA_EDIT_NO_CONTROL;
		if (cellPosList != null && cellPosList.size() > 0) {
			Cell cell = cellsModel.getCell(cellPosList.get(0));
			iFormulaEditType = FormulaCellExtPropertyUtil.getFormulaCellEditType(cell);
		}
		if (iFormulaEditType == FormulaCellExtPropertyUtil.FORMULA_EDIT_YES) {
			radioEditYes.setSelected(true);
		} else if (iFormulaEditType == FormulaCellExtPropertyUtil.FORMULA_EDIT_NO) {
			radioEditNo.setSelected(true);
		} else {
			radioNoControl.setSelected(true);
		}
	}

	/**
	 * this mentod initializes btnPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new JPanel();
			btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			btnPanel.add(getBtnOK());
			btnPanel.add(getBtnCancel());
		}
		return btnPanel;
	}

	/**
	 * 单元公式是否允许编辑
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioEditYes() {
		if (radioEditYes == null) {
			radioEditYes = new UIRadioButton();
			radioEditYes.setText(StringResource
					.getStringResource("miufo1002256"));// 是
		}
		return radioEditYes;
	}

	/**
	 * 单元公式是否允许编辑
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioEditNo() {
		if (radioEditNo == null) {
			radioEditNo = new UIRadioButton();
			radioEditNo.setText(StringResource
					.getStringResource("miufo1002257"));// 否
		}
		return radioEditNo;
	}

	/**
	 * 单元公式是否允许编辑
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioNoControl() {
		if (radioNoControl == null) {
			radioNoControl = new UIRadioButton();
			radioNoControl.setText(StringResource
					.getStringResource("miufotasknew00018"));// 不控制

		}
		return radioNoControl;
	}

	/**
	 * This method initializes btnOK
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			// btnOK.setBounds(220, 465, 75, 22);
			btnOK.setBounds(220, 530, 75, 22);
			btnOK.setText(StringResource.getStringResource("miufo1000790")); // "确
																				// 定"
			btnOK.addActionListener(this);
			btnOK.registerKeyboardAction(this, KeyStroke.getKeyStroke(
					KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
		}
		return btnOK;
	}

	/**
	 * This method initializes btnCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setBounds(350, 530, 75, 22);
			btnCancel.setText(StringResource.getStringResource("miufo1000274")); // "取
																					// 消"
			btnCancel.addActionListener(this);
			btnCancel.registerKeyboardAction(this, KeyStroke.getKeyStroke(
					KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
			//	   
		}
		return btnCancel;
	}

	/**
	 * add by wangyga 根据radio的选择，获得控制的变量值
	 * 
	 * @see com.ufsoft.table.format.TableConstant
	 */
	private void dealFormulaEdit() {
		if (getRadioNoControl().isSelected()) {
			setFormulaEditType(FormulaCellExtPropertyUtil.FORMULA_EDIT_NO_CONTROL);
		} else if (getRadioEditYes().isSelected()) {
			setFormulaEditType(FormulaCellExtPropertyUtil.FORMULA_EDIT_YES);
		} else if (getRadioEditNo().isSelected()) {
			setFormulaEditType(FormulaCellExtPropertyUtil.FORMULA_EDIT_NO);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			dealFormulaEdit();
			setResult(ID_OK);
			close();
		} else if (e.getSource() == getBtnCancel()) {
			setResult(ID_CANCEL);
			close();
		}

	}

	public int getFormulaEditType() {
		return iFormulaEditType;
	}

	public void setFormulaEditType(int iFormulaEditType) {
		this.iFormulaEditType = iFormulaEditType;
	}

}
