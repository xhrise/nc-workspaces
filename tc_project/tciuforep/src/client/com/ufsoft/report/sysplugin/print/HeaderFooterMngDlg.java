package com.ufsoft.report.sysplugin.print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

public class HeaderFooterMngDlg extends UfoDialog {
	private static final long serialVersionUID = -2323551061355544598L;
	private JPanel jContentPane = null;
	private JPanel headerPreviewPanel = null;
	private JPanel footerPreviewPanel = null;
	private JButton printButton = null;
	private JButton jButton = null;
	private JButton headerDefButton = null;
	private JButton footerDefButton = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JLabel headerLabel = null;
	private JLabel footerLabel = null;
	private JTextField headerDistanceTextField = null;
	private JTextField footerDistanceTextField = null;
	private HeaderFooterModel _model;
	private UfoReport _report;
	/**
	 * This is the default constructor
	 * @param model 
	 * @param container 
	 */
	public HeaderFooterMngDlg(UfoReport container, HeaderFooterModel model) {
		super(container);
		_report = container;
		_model = model == null?null:(HeaderFooterModel)model.clone();
		double headerDistance =_model == null ? 0 :_model.getHeaderDistance();
		double footerDistance =_model == null ? 0 : _model.getFooterDistance();
		getHeaderDistanceTextField().setText(""+headerDistance);
		getFooterDistanceTextField().setText(""+footerDistance);
		updateDisplay();
		initialize();
	}

	private void updateDisplay() {
		getHeaderPreviewPanel().removeAll();
		getFooterPreviewPanel().removeAll();
		if(_model != null){
			Dimension preSize = getHeaderPreviewPanel().getSize();
			for(int position=HeaderFooterModel.HEADERE_LEFT;position<=HeaderFooterModel.FOOTER_RIGHT;position++){
				HeaderFooterSegmentModel segmentModel = _model.getHeaderFooterSegmentModel(position);
				if(segmentModel != null && segmentModel.getValue() != null){
					HeaderFooterSegmentComp comp = new HeaderFooterSegmentComp(segmentModel,1,1);
					comp.setBounds(new Rectangle(0,0, preSize.width,preSize.height));
					if(segmentModel.isHeaderNotFooter()){
						getHeaderPreviewPanel().add(comp);						
					}else{
						getFooterPreviewPanel().add(comp);
					}
				}
			}
		}	
		this.repaint();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(472, 339);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @i18n report00025=页脚:
	 * @i18n report00026=页眉:
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			footerLabel = new nc.ui.pub.beans.UILabel();
			footerLabel.setBounds(355, 175, 44, 26);
			footerLabel.setText(MultiLang.getString("report00025"));
			headerLabel = new nc.ui.pub.beans.UILabel();
			headerLabel.setBounds(355, 133, 44, 26);
			headerLabel.setText(MultiLang.getString("report00026"));
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getHeaderPreviewPanel(), null);
			jContentPane.add(getFooterPreviewPanel(), null);
			jContentPane.add(getPrintButton(), null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getHeaderDefButton(), null);
			jContentPane.add(getFooterDefButton(), null);
			jContentPane.add(getOkButton(), null);
			jContentPane.add(getCancelButton(), null);
			jContentPane.add(headerLabel, null);
			jContentPane.add(footerLabel, null);
			jContentPane.add(getHeaderDistanceTextField(), null);
			jContentPane.add(getFooterDistanceTextField(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes headerPreviewPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getHeaderPreviewPanel() {
		if (headerPreviewPanel == null) {
			headerPreviewPanel = new UIPanel();
			headerPreviewPanel.setBounds(0, 0, 349, 95);
			headerPreviewPanel.setBackground(Color.WHITE);
			headerPreviewPanel.setLayout(null);
			headerPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		return headerPreviewPanel;
	}

	/**
	 * This method initializes footerPreviewPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getFooterPreviewPanel() {
		if (footerPreviewPanel == null) {
			footerPreviewPanel = new UIPanel();
			footerPreviewPanel.setBounds(1, 160, 349, 95);
			footerPreviewPanel.setBackground(Color.WHITE);
			footerPreviewPanel.setLayout(null);
			footerPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		return footerPreviewPanel;
	}

	/**
	 * This method initializes printButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n print=打印
	 */    
	private JButton getPrintButton() {
		if (printButton == null) {
			printButton = new nc.ui.pub.beans.UIButton();
			printButton.setBounds(355, 30, 75, 22);
			printButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					_report.getTable().print();
				}
			});
			printButton.setText(MultiLang.getString("print"));
		}
		return printButton;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n PrintPreview=打印预览
	 */    
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new nc.ui.pub.beans.UIButton();
			jButton.setBounds(356, 78, 75, 22);
			jButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					_report.getTable().printPreview(HeaderFooterMngDlg.this);
				}
			});
			jButton.setText(MultiLang.getString("PrintPreview"));
		}
		return jButton;
	}

	/**
	 * This method initializes headerDefButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00027=自定义页眉
	 */    
	private JButton getHeaderDefButton() {
		if (headerDefButton == null) {
			headerDefButton = new nc.ui.pub.beans.UIButton();
			headerDefButton.setBounds(16, 113, 120, 22);
			headerDefButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					defHeaderOrFooter(true);
				}
			});
			headerDefButton.setText(MultiLang.getString("report00027"));
		}
		return headerDefButton;
	}
	
	/**
	 * This method initializes footerDefButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00028=自定义页脚
	 */    
	private JButton getFooterDefButton() {
		if (footerDefButton == null) {
			footerDefButton = new nc.ui.pub.beans.UIButton();
			footerDefButton.setBounds(193, 113, 120, 22);
			footerDefButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					defHeaderOrFooter(false);
				}
			});
			footerDefButton.setText(MultiLang.getString("report00028"));
		}
		return footerDefButton;
	}
	/**
	 * @i18n report00027=自定义页眉
	 * @i18n report00028=自定义页脚
	 */
	private void defHeaderOrFooter(boolean isHeader){
		int startPosition = isHeader ? 0 : 3;
		HeaderFooterDefDlg defDlg ;
		if(getModel() == null){
			defDlg = new HeaderFooterDefDlg(this,null,null,null);
		}else{
			defDlg = new HeaderFooterDefDlg(
					this,
					getModel().getHeaderFooterSegmentModel(startPosition),
					getModel().getHeaderFooterSegmentModel(startPosition+1),
					getModel().getHeaderFooterSegmentModel(startPosition+2)
					);
		}

		defDlg.setTitle(isHeader?MultiLang.getString("report00027"):MultiLang.getString("report00028"));
		defDlg.setVisible(true);
		if(defDlg.getResult()==UfoDialog.ID_OK){
			HeaderFooterSegmentModel[] segmentModels = defDlg.getSegmentModels();
			if(segmentModels != null && segmentModels.length == 3 && 
					(segmentModels[0]!=null || segmentModels[1]!=null || segmentModels[2]!=null)){

				if(_model == null){
					_model = new HeaderFooterModel();
				}
				for(int i=0;i<3;i++){
					_model.setHeaderFooterSegmentModel(i+startPosition,segmentModels[i]);
				}
			}
			updateDisplay();
		}
	}
	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = createOkButton();
			okButton.setBounds(227, 274, 75, 22);
		}
		return okButton;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = createCancleButton();
			cancelButton.setBounds(342, 274, 75, 22);
		}
		return cancelButton;
	}

	/**
	 * This method initializes headerDistanceTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getHeaderDistanceTextField() {
		if (headerDistanceTextField == null) {
			headerDistanceTextField = new UITextField();
			headerDistanceTextField.setBounds(402, 133, 53, 26);
		}
		return headerDistanceTextField;
	}

	/**
	 * This method initializes footerDistanceTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getFooterDistanceTextField() {
		if (footerDistanceTextField == null) {
			footerDistanceTextField = new UITextField();
			footerDistanceTextField.setBounds(402, 175, 53, 26);
		}
		return footerDistanceTextField;
	}

	public HeaderFooterModel getModel() {
		if(_model != null){
			if(getHeaderDistanceTextField().getText().trim().length()>0){
				_model.setHeaderDistance(Double.parseDouble(getHeaderDistanceTextField().getText().trim()));
			}
			if(getFooterDistanceTextField().getText().trim().length()>0){
				_model.setFooterDistance(Double.parseDouble(getFooterDistanceTextField().getText().trim()));
			}	
		}
		return _model;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
  