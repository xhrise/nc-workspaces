/*
 * Created on 2005-6-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.multidimension.model.MultiReportFormat;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.cellattr.CellPropertyDialog;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * @author ll
 * 
 */
public class ReportFormatDialog extends UIDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @i18n miufo1000849=居左
	 * @i18n miufo1000848=居中
	 * @i18n miufo1000850=居右
	 */
	private String[] hor_aligns = new String[] { StringResource.getStringResource("miufo1000849"), StringResource.getStringResource("miufo1000848"), StringResource.getStringResource("miufo1000850") };

	/**
	 * @i18n miufo1000885=居上
	 * @i18n miufo1000848=居中
	 * @i18n miufo1000884=居下
	 */
	private String[] ver_aligns = new String[] { StringResource.getStringResource("miufo1000885"), StringResource.getStringResource("miufo1000848"), StringResource.getStringResource("miufo1000884") };

	private JPanel jPanel = null;

	private JButton jBtnOK = null;

	private JButton jBtnCancel = null;

	private JButton jBtnFont = null;

	private JComboBox jComRowHor = null;

	private JComboBox jComRowVer = null;

	private JTextField jTfRowIndent = null;

	private JComboBox jComColHor = null;

	private JComboBox jComColVer = null;

	private JComboBox jComDataString = null;

	private JComboBox jComDataNumber = null;

	private JButton jBtnElseFormat = null;

	private JPanel jPanelTitle = null;

	private JPanel jPanelRow = null;

	private JPanel jPanelCol = null;

	private JPanel jPanelData = null;

	private MultiReportFormat m_formatModel = null;

	public ReportFormatDialog(Container cont) {
		super(cont);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(526, 413);
		this.setTitle(StringResource.getStringResource("ubimultidim0053"));

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
			jPanel.add(getJBtnOK(), null);
			jPanel.add(getJBtnCancel(), null);
			jPanel.add(getJPanelTitle(), null);
			jPanel.add(getJPanelData(), null);

		}
		return jPanel;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnOK() {
		if (jBtnOK == null) {
			jBtnOK = new nc.ui.pub.beans.UIButton();
			jBtnOK.setBounds(115, 346, 75, 22);
			jBtnOK.setText(StringResource.getStringResource("miufo1000094"));
			jBtnOK.addActionListener(this);
		}
		return jBtnOK;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnCancel() {
		if (jBtnCancel == null) {
			jBtnCancel = new nc.ui.pub.beans.UIButton();
			jBtnCancel.setBounds(298, 346, 75, 22);
			jBtnCancel.setText(StringResource.getStringResource("miufo1000274"));
			jBtnCancel.addActionListener(this);
		}
		return jBtnCancel;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnFont() {
		if (jBtnFont == null) {
			jBtnFont = new nc.ui.pub.beans.UIButton();
			jBtnFont.setText(StringResource.getStringResource("miufo1000846"));
			jBtnFont.setBounds(new java.awt.Rectangle(94, 15, 75, 22));
			jBtnFont.addActionListener(this);
		}
		return jBtnFont;
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComRowHor() {
		if (jComRowHor == null) {
			jComRowHor = new UIComboBox();
			jComRowHor.setBounds(new java.awt.Rectangle(116, 28, 114, 23));
			for (int i = 0; i < hor_aligns.length; i++) {
				jComRowHor.addItem(hor_aligns[i]);
			}
		}
		return jComRowHor;
	}

	/**
	 * This method initializes jComboBox1
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComRowVer() {
		if (jComRowVer == null) {
			jComRowVer = new UIComboBox();
			jComRowVer.setBounds(new java.awt.Rectangle(116, 58, 114, 23));
			for (int i = 0; i < ver_aligns.length; i++) {
				jComRowVer.addItem(ver_aligns[i]);
			}
		}
		return jComRowVer;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTfRowIndent() {
		if (jTfRowIndent == null) {
			jTfRowIndent = new UITextField();
			jTfRowIndent.setBounds(new java.awt.Rectangle(116, 88, 114, 23));
		}
		return jTfRowIndent;
	}

	/**
	 * This method initializes jComboBox2
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComColHor() {
		if (jComColHor == null) {
			jComColHor = new UIComboBox();
			jComColHor.setBounds(new java.awt.Rectangle(88, 28, 99, 23));
			for (int i = 0; i < hor_aligns.length; i++) {
				jComColHor.addItem(hor_aligns[i]);
			}
		}
		return jComColHor;
	}

	/**
	 * This method initializes jComboBox3
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComColVer() {
		if (jComColVer == null) {
			jComColVer = new UIComboBox();
			jComColVer.setBounds(new java.awt.Rectangle(88, 58, 99, 23));
			for (int i = 0; i < ver_aligns.length; i++) {
				jComColVer.addItem(ver_aligns[i]);
			}
		}
		return jComColVer;
	}

	/**
	 * This method initializes jComboBox4
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComDataString() {
		if (jComDataString == null) {
			jComDataString = new UIComboBox();
			jComDataString.setBounds(new java.awt.Rectangle(113, 28, 121, 26));
			for (int i = 0; i < hor_aligns.length; i++) {
				jComDataString.addItem(hor_aligns[i]);
			}
		}
		return jComDataString;
	}

	/**
	 * This method initializes jComboBox5
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComDataNumber() {
		if (jComDataNumber == null) {
			jComDataNumber = new UIComboBox();
			jComDataNumber.setBounds(new java.awt.Rectangle(113, 58, 121, 26));
			for (int i = 0; i < hor_aligns.length; i++) {
				jComDataNumber.addItem(hor_aligns[i]);
			}
		}
		return jComDataNumber;
	}

	/**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnElseFormat() {
		if (jBtnElseFormat == null) {
			jBtnElseFormat = new nc.ui.pub.beans.UIButton(StringResource.getStringResource("ubimultidim0061"));
			jBtnElseFormat.setBounds(new java.awt.Rectangle(301, 43, 75, 22));
			jBtnElseFormat.addActionListener(this);
		}
		return jBtnElseFormat;
	}

	/**
	 * This method initializes jPanelTitle
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelTitle() {
		if (jPanelTitle == null) {
			jPanelTitle = new UIPanel();
			jPanelTitle.setLayout(null);
			jPanelTitle.setBounds(new java.awt.Rectangle(19, 8, 484, 196));
			jPanelTitle.setBorder(javax.swing.BorderFactory.createTitledBorder(StringResource.getStringResource("ubimultidim0054")));
			jPanelTitle.add(getJPanelRow(), null);
			jPanelTitle.add(getJBtnFont(), null);
			jPanelTitle.add(getJPanelCol(), null);
		}
		return jPanelTitle;
	}

	/**
	 * This method initializes jPanelRow
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelRow() {
		if (jPanelRow == null) {
			JLabel jLblRowIndent = new nc.ui.pub.beans.UILabel();
			jLblRowIndent.setBounds(new java.awt.Rectangle(15, 90, 77, 18));
			jLblRowIndent.setText(StringResource.getStringResource("ubimultidim0058"));
			JLabel jLblRowVer = new nc.ui.pub.beans.UILabel();
			jLblRowVer.setBounds(new java.awt.Rectangle(15, 60, 77, 18));
			jLblRowVer.setText(StringResource.getStringResource("miufo1003094"));
			JLabel jLblRowHor = new nc.ui.pub.beans.UILabel();
			jLblRowHor.setBounds(new java.awt.Rectangle(15, 30, 77, 18));
			jLblRowHor.setText(StringResource.getStringResource("miufo1003093"));
			jPanelRow = new UIPanel();
			jPanelRow.setLayout(null);
			jPanelRow.setBounds(new java.awt.Rectangle(8, 50, 244, 135));
			jPanelRow.setBorder(javax.swing.BorderFactory.createTitledBorder(StringResource.getStringResource("ubimultidim0056")));
			jPanelRow.add(getJComRowHor(), null);
			jPanelRow.add(getJComRowVer(), null);
			jPanelRow.add(getJTfRowIndent(), null);
			jPanelRow.add(jLblRowHor, null);
			jPanelRow.add(jLblRowVer, null);
			jPanelRow.add(jLblRowIndent, null);
		}
		return jPanelRow;
	}

	/**
	 * This method initializes jPanelCol
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelCol() {
		if (jPanelCol == null) {
			JLabel jLblColVer = new nc.ui.pub.beans.UILabel();
			jLblColVer.setBounds(new java.awt.Rectangle(6, 60, 71, 18));
			jLblColVer.setText(StringResource.getStringResource("miufo1003094"));
			JLabel jLblColHor = new nc.ui.pub.beans.UILabel();
			jLblColHor.setBounds(new java.awt.Rectangle(6, 30, 71, 18));
			jLblColHor.setText(StringResource.getStringResource("miufo1003093"));
			jPanelCol = new UIPanel();
			jPanelCol.setLayout(null);
			jPanelCol.setBounds(new java.awt.Rectangle(263, 50, 216, 132));
			jPanelCol.setBorder(javax.swing.BorderFactory.createTitledBorder(StringResource.getStringResource("ubimultidim0057")));

			jPanelCol.add(getJComColHor(), null);
			jPanelCol.add(getJComColVer(), null);
			jPanelCol.add(jLblColHor, null);
			jPanelCol.add(jLblColVer, null);
		}
		return jPanelCol;
	}

	/**
	 * This method initializes jPanelData
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelData() {
		if (jPanelData == null) {
			JLabel jLblDataNumber = new nc.ui.pub.beans.UILabel();
			jLblDataNumber.setBounds(new java.awt.Rectangle(15, 60, 78, 18));
			jLblDataNumber.setText(StringResource.getStringResource("ubimultidim0060"));
			JLabel jLblDataString = new nc.ui.pub.beans.UILabel();
			jLblDataString.setBounds(new java.awt.Rectangle(15, 30, 78, 18));
			jLblDataString.setText(StringResource.getStringResource("ubimultidim0059"));
			jPanelData = new UIPanel();
			jPanelData.setLayout(null);
			jPanelData.setBounds(new java.awt.Rectangle(24, 214, 477, 110));
			jPanelData.setBorder(javax.swing.BorderFactory
					.createTitledBorder(StringResource.getStringResource("miufo1001692")));

			jPanelData.add(getJComDataNumber(), null);
			jPanelData.add(getJComDataString(), null);
			jPanelData.add(getJBtnElseFormat(), null);
			jPanelData.add(jLblDataString, null);
			jPanelData.add(jLblDataNumber, null);
		}
		return jPanelData;
	}

	public MultiReportFormat getFormatModel() {
		return m_formatModel;
	}

	public void setFormatModel(MultiReportFormat model) {
		if (model != null) {
			m_formatModel = (MultiReportFormat) model.clone();
		} else {
			m_formatModel = new MultiReportFormat();
		}
		initPanelDatas(m_formatModel);
	}

	/*
	 * 将原来设置的格式信息显示到对话框中
	 */
	private void initPanelDatas(MultiReportFormat formatModel) {
		getJComColHor().setSelectedIndex(getIndex2Disp(true, formatModel.getHor_colTitle()));
		getJComColVer().setSelectedIndex(getIndex2Disp(false, formatModel.getVer_colTitle()));
		getJComDataNumber().setSelectedIndex(getIndex2Disp(true, formatModel.getHor_dataNumber()));
		getJComDataString().setSelectedIndex(getIndex2Disp(true, formatModel.getHor_dataString()));
		getJComRowHor().setSelectedIndex(getIndex2Disp(true, formatModel.getTitleFormat().getHalign()));
		getJComRowVer().setSelectedIndex(getIndex2Disp(false, formatModel.getTitleFormat().getValign()));

		getJTfRowIndent().setText(formatModel.getRow_indent() + "");

		// TODO else info
	}

	/*
	 * 从界面上得到用户设置的格式信息
	 */
	private void getDataFromPanel(MultiReportFormat formatModel) {
		formatModel.setHor_colTitle(getAlignValue(true, getJComColHor().getSelectedIndex()));
		formatModel.setVer_colTitle(getAlignValue(false, getJComColVer().getSelectedIndex()));

		formatModel.setHor_dataNumber(getAlignValue(true, getJComDataNumber().getSelectedIndex()));
		formatModel.setHor_dataString(getAlignValue(true, getJComDataString().getSelectedIndex()));
		formatModel.getTitleFormat().setHalign(getAlignValue(true, getJComRowHor().getSelectedIndex()));
		formatModel.getTitleFormat().setValign(getAlignValue(false, getJComRowVer().getSelectedIndex()));

		try{
			Double dd = Double.valueOf(getJTfRowIndent().getText());
			formatModel.setRow_indent(dd);
		}catch(NumberFormatException ex){
			AppDebug.debug(ex);
		}
		// TODO else info
	}

	private int getIndex2Disp(boolean isHor, int align_value) {
		if (isHor) {
			switch (align_value) {
			case TableConstant.HOR_LEFT:
				return 0;
			case TableConstant.HOR_RIGHT:
				return 2;
			default:
				return 1;// TableConstant.HOR_CENTER
			}
		} else {
			switch (align_value) {
			case TableConstant.VER_UP:
				return 0;
			case TableConstant.VER_DOWN:
				return 2;
			default:
				return 1;// TableConstant.VER_CENTER
			}
		}
	}

	private int getAlignValue(boolean isHor, int index) {
		if (isHor) {
			switch (index) {
			case 0:
				return TableConstant.HOR_LEFT;
			case 2:
				return TableConstant.HOR_RIGHT;
			default:
				return TableConstant.HOR_CENTER;
			}
		} else {
			switch (index) {
			case 0:
				return TableConstant.VER_UP;
			case 2:
				return TableConstant.VER_DOWN;
			default:
				return TableConstant.VER_CENTER;
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJBtnFont()) {

//		} else if (e.getSource() == getJBtnBackGround()) {
			Format newFormat = getUserSetFormat(this, m_formatModel.getTitleFormat(), true);
			if (newFormat != null) {
				m_formatModel.setTitleFormat(newFormat);
			}

		} else if (e.getSource() == getJBtnElseFormat()) {

			Format newFormat = getUserSetFormat(this, m_formatModel.getDataFormat(), false);
			if (newFormat != null) {
				newFormat.setCellType(TableConstant.CELLTYPE_NUMBER);
				m_formatModel.setDataFormat(newFormat);
			}
		} else if (e.getSource() == getJBtnOK()) {
			getDataFromPanel(m_formatModel);
			this.closeOK();

		} else if (e.getSource() == getJBtnCancel()) {
			this.closeCancel();
		}

	}

	public static Format getUserSetFormat(Container cont, Format oldFormat, boolean isOnlyFont) {
		Format dataFormat = (Format) oldFormat.clone();
		CellPropertyDialog dlg = new CellPropertyDialog(cont, dataFormat, false);
		if(isOnlyFont){//只显示字体图画页签
			dlg.getTabbedPane().remove(3);
			dlg.getTabbedPane().remove(2);
			dlg.getTabbedPane().remove(0);
		}
		dlg.setVisible(true);
		if (dlg.getResult() == UfoDialog.ID_OK) {
			Hashtable propertyCache = dlg.getPropertyCache();
			Enumeration enums = propertyCache.keys();
			while (enums.hasMoreElements()) {
				Integer tmpType = (Integer) enums.nextElement();
				int nType = tmpType.intValue();
				int nValue = ((Integer) propertyCache.get(tmpType)).intValue();

				PropertyType.setPropertyByType(dataFormat, nType, nValue);
			}
			return dataFormat;
		}
		return null;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
