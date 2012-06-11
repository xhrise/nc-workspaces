package com.ufsoft.report.sysplugin.print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.FloatDocument;
import com.ufsoft.report.util.IntegerDocument;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.print.PrintSet;

/**
 * printer settings and print customized information
 * @author liuyy
 *
 */
public class PrintSettingDlg extends UfoDialog {

	private static final long serialVersionUID = -4528546499006409178L;

	private UfoReport m_Rep = null;

	private JPanel pnlMain = null;
	private JTabbedPane tabpnlMain = null;
	private JPanel pnlBtn = null;
	private JButton btnOK = null;
	private JButton btnCancel = null;
	private JPanel pnlPage = null;
	private JPanel pnlHeadFoot = null;
	private JPanel pnlPrintArea = null;

	private JPanel pnlBtnView = null;

	private JButton btnPrint = null;

	private JButton btnPrintView = null;

	private JButton btnPageSetting = null;
 
//页眉页脚
	private JPanel headerPreviewPanel = null;
	private JPanel footerPreviewPanel = null; 
	private JButton btnHeaderDef = null;
	private JButton btnFooterDef = null;  
	private JTextField fldHeaderDistance = null;
	private JTextField fldFooterDistance = null;
	private HeaderFooterModel _headFooterModel;  //  @jve:decl-index=0:
	private PrintSet printset;
	
	
	//打印设置
	private JTextField fldPrintScale = null;
	private JRadioButton rbRowToCol = null;
	private JRadioButton rbColToRow = null;
	//add by guogang 2007-6-20
	private JCheckBox rbPageToOne=null;
	//add end
	//add by guogang 2007-10-16
	private JCheckBox rbHCenter=null;
	private JCheckBox rbVCenter=null;
	//add end
	private JLabel lblScale2 = null;
	private JPanel pnlPrintOrder = null;
	private ImageIcon iconCol2Row = ResConst.getImageIcon("reportcore/printorder1.gif");//先列后行
	private ImageIcon iconRow2Col = ResConst.getImageIcon("reportcore/printorder2.gif");//先行后列
	private JLabel lblOrderImg = null;

	private JPanel pnlArea1 = null;

	private JPanel pnlAreaTitle = null;

	private JLabel lblRowArea = null;

	private JLabel lblColArea = null;

	private JLabel lblFrom1 = null;

	private JLabel lblTo1 = null;

	private JTextField tfAreaRow1 = null;

	private JTextField tfAreaRow2 = null;

	private JLabel lblFrom2 = null;

	private JTextField tfAreaCol1 = null;

	private JLabel lblTo2 = null;

	private JTextField tfAreaCol2 = null;

	private JLabel lblRowTitle = null;

	private JLabel lblFrom3 = null;

	private JTextField tfRowHeader1 = null;

	private JLabel lblTo3 = null;

	private JTextField tfRowHeader2 = null;

	private JLabel lblColTitle = null;

	private JLabel lblFrom4 = null;

	private JTextField tfColHeader1 = null;

	private JLabel lblTo4 = null;

	private JTextField tfColHeader2 = null;

	public PrintSettingDlg(UfoReport rep) {
		super(rep);
		m_Rep = rep;
				
		initialize();
	}
	
	 
	/**
	 * This method initializes this
	 * @i18n miufo00008=打印设置
	 * 
	 */
	private void initialize() {		
		this.setSize(new Dimension(678, 445));
		this.setResizable(false);
		this.setTitle(MultiLang.getString("miufo00008"));
		
		printset = (PrintSet) m_Rep.getCellsModel().getPrintSet().clone();  
		HeaderFooterModel hfModel = printset.getHeaderFooterModel();
		if(hfModel != null){
			_headFooterModel = (HeaderFooterModel) hfModel.clone();			
		} else {
			_headFooterModel = new HeaderFooterModel();
		}
		
		this.setContentPane(getPnlMain());

		restrict();
		initData();
		
	}

	private void initData(){
		//打印比例
		String value = Float.toString(printset.getViewScale());
		 
		fldPrintScale.setText(value);
		//设置页首页尾
		//    if(printset.isHeadEnable()){
		//      cbHeader.setEnabled(true);
		int[] rowHeadRang = printset.getRowHeadRang();
		if (rowHeadRang != null) {
			tfRowHeader1.setText(getIntString(rowHeadRang[0] + 1));
			tfRowHeader2.setText(getIntString(rowHeadRang[1]));
		}
		int[] colHeadRang = printset.getColHeadRang();
		if (colHeadRang != null) {
			tfColHeader1.setText(colValue2ColLabel(colHeadRang[0] + 1));
			tfColHeader2.setText(colValue2ColLabel(colHeadRang[1]));
		}
		//设置打印区域.
		int[] printArea = printset.getPrintArea();
		if (printArea != null) {
			tfAreaRow1.setText(getIntString(printArea[0] + 1));
			tfAreaRow2.setText(getIntString(printArea[2]));
			tfAreaCol1.setText(colValue2ColLabel(printArea[1] + 1));
			tfAreaCol2.setText(colValue2ColLabel(printArea[3]));
		}
		//行列优先
		if (printset.isColPriorityPrinted()) {
			rbColToRow.setSelected(true);
		} else {
			rbRowToCol.setSelected(true);
		}
		//add by guogang 2007-7-20
		if(printset.isPageToOne()){
			rbPageToOne.setSelected(true);
			fldPrintScale.setEnabled(false);
		}
		if(printset.isHCenter()){
			rbHCenter.setSelected(true);
		}
		if(printset.isVCenter()){
			rbVCenter.setSelected(true);
		}
	}
	/**
	 * This method initializes pnlMain	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlMain() {
		if (pnlMain == null) {

			pnlMain = new JPanel();
			pnlMain.setLayout(new GridBagLayout());
  
			GridBagConstraints cTabPane = new GridBagConstraints();
			cTabPane.fill = GridBagConstraints.BOTH;
			cTabPane.gridx = 1;
			cTabPane.gridy = 0;
			cTabPane.ipadx = 428;
			cTabPane.ipady = 222;
			cTabPane.weightx = 1.0;
			cTabPane.weighty = 1.0; 
			pnlMain.add(getTabpnlMain(), cTabPane);
			
			GridBagConstraints c1 = new GridBagConstraints();
			c1.fill = GridBagConstraints.REMAINDER;
			c1.gridx = 0;
			c1.gridy = 0;
			c1.ipadx = 100;
			c1.ipady = 300;
			c1.weightx = 1.0;
			c1.weighty = 1.0; 
			pnlMain.add(getPnlBtnView(), c1);		
			
			GridBagConstraints cMainBtn = new GridBagConstraints(); 
			cMainBtn.gridx = 0;
			cMainBtn.gridy = 1;
			cMainBtn.ipadx = 700;
			cMainBtn.ipady = 60; 
			cMainBtn.gridwidth = 2;
//			cMainBtn.weightx = 1.0;
//			cMainBtn.weighty = 1.0;
			pnlMain.add(getPnlBtn(), cMainBtn);
		}
		return pnlMain;
	}

	/**
	 * This method initializes tabpnlMain	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 * @i18n miufo00008=打印设置
	 * @i18n miufo00009=页眉/页脚
	 * @i18n PrintArea=打印区域
	 */
	private JTabbedPane getTabpnlMain() {
		if (tabpnlMain == null) {
			tabpnlMain = new UITabbedPane();
			tabpnlMain.setPreferredSize(new Dimension(50, 70));
			tabpnlMain.addTab(MultiLang.getString("miufo00008"), null, getPnlPage(), null);
			tabpnlMain.addTab(MultiLang.getString("miufo00009"), null, getPnlHeadFoot(), null);
			tabpnlMain.addTab(MultiLang.getString("PrintArea"), null, getPnlPrintArea(), null);
		}
		return tabpnlMain;
	}

	/**
	 * This method initializes pnlBtn	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlBtn() {
		if (pnlBtn == null) {
			pnlBtn = new JPanel();
			pnlBtn.setLayout(null);
			pnlBtn.add(getBtnOK(), null);
			pnlBtn.add(getBtnCancel(), null);
		}
		return pnlBtn;
	}

 
	/**
	 * @i18n uiuforep0000704=确定
	 */
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setName("");
			btnOK.setText(MultiLang.getString("uiuforep0000704"));//MultiLang.getString("ok")
//			btnOK = createOkButton();
			btnOK.setBounds(new Rectangle(421, 16, 100, 29));
//			btnOK.setMnemonic(KeyEvent.VK_UNDEFINED);
			btnOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean rlt = performOK();
					if(!rlt){
						return;
					}
					setResult(ID_OK);					
					close();
					m_Rep.getTable().printPreview(m_Rep);
				}
			});
			
		}
		return btnOK;
	}
	
	private boolean performOK(){

		try { 
			fillPrintSet();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(PrintSettingDlg.this, ex
					.getMessage());
			return false;
		}

     	printset.setHeaderFooterModel(_headFooterModel);
     	m_Rep.getCellsModel().setPrintSet(printset); 
     	return true;

	}
 
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText(MultiLang.getString("cancel"));//MultiLang.getString("cancel")  "取消"
			btnCancel.setBounds(new Rectangle(547, 16, 91, 30));
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setResult(ID_CANCEL);
					close();
				}
			});
		}
		return btnCancel;
	}
 
	/**
	 * @i18n miufo00010=倍正常尺寸(0.3~3.0)
	 * @i18n miufo00011=缩放比例：
	 * @i18n miufo00012=缩放比例
	 * modify by guogang 2007-10-16 增加打印居中功能
	 */
	private JPanel getPnlPage() {
		if (pnlPage == null) { 
			
//			lbl1.setBounds(new Rectangle(59, 183, 72, 18));
			lblScale2 = new JLabel();
			lblScale2.setBounds(new Rectangle(197, 30, 187, 18));
			lblScale2.setText(MultiLang.getString("miufo00010"));
			pnlPage = new JPanel();
			pnlPage.setLayout(null); 
			
			//打印比例的构建 
			fldPrintScale = new JTextField(); 
			fldPrintScale.setBounds(new Rectangle(107, 26, 89, 22)); 
			fldPrintScale.setHorizontalAlignment(JTextField.RIGHT);
			JLabel lblScale = new JLabel(MultiLang.getString("PrintScale"));
			 
			lblScale.setBounds(new Rectangle(11, 30, 87, 18));
			lblScale.setHorizontalAlignment(SwingConstants.RIGHT);
			lblScale.setText(MultiLang.getString("miufo00011"));
			//add by guogang 2007-6-20
			rbPageToOne = new UICheckBox(MultiLang
					.getString("pageToOne"));
			rbPageToOne.setBounds(new Rectangle(39, 48, 100, 26));
			rbPageToOne.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						fldPrintScale.setEnabled(false);
					}
					if(e.getStateChange()==ItemEvent.DESELECTED){
						fldPrintScale.setEnabled(true);
					}
				}
				
			});
			//add end
			JPanel pnPrintScale = new JPanel();
			pnPrintScale.setLayout(null);
			pnPrintScale.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("miufo00012"), TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 12), Color.blue));
			pnPrintScale.setBounds(new Rectangle(20, 20, 447, 82));
 		 	pnPrintScale.add(lblScale, null);
			pnPrintScale.add(fldPrintScale, null);
			pnPrintScale.add(lblScale2, null);
			pnPrintScale.add(rbPageToOne,null);//add by guogang
			pnlPage.add(pnPrintScale);
			//打印顺序的面板构建
			pnlPage.add(getPnlPrintOrder(), null);
			//打印居中面板构建
			JPanel pnPrintCenterModel=new JPanel(new FlowLayout(FlowLayout.LEADING));
			pnPrintCenterModel.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("PrintCenterModel"), TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 12), Color.blue));
			pnPrintCenterModel.setBounds(new Rectangle(21, 250, 447, 70));
			rbHCenter = new UICheckBox(MultiLang
					.getString("Horizontal"));
			pnPrintCenterModel.add(rbHCenter);
			rbVCenter = new UICheckBox(MultiLang
					.getString("Vertical"));
			pnPrintCenterModel.add(rbVCenter);
			pnlPage.add(pnPrintCenterModel, null);
		}
		return pnlPage;
	}
	
	
	
 
	/**
	 * @i18n miufo00013=页脚高度：
	 * @i18n miufo00014=页眉高度：
	 * modify by guogang 2007-12-5 取消页眉、页脚高度的设置功能，默认为纸张边距的一半
	 */
	public JPanel getPnlHeadFoot() {
		if (pnlHeadFoot == null) {
			pnlHeadFoot = new JPanel();
		
//			JLabel footerLabel = new JLabel();
//			footerLabel.setBounds(375, 185, 70, 26);
//			footerLabel.setText(MultiLang.getString("miufo00013"));//MultiLang.getString("report00025"));
//			JLabel headerLabel = new JLabel();
//			headerLabel.setBounds(375, 23, 71, 26);
//			headerLabel.setText(MultiLang.getString("miufo00014"));//MultiLang.getString("report00026"));
			 
			pnlHeadFoot.setLayout(null);
			pnlHeadFoot.add(getHeaderPreviewPanel(), null);
			pnlHeadFoot.add(getFooterPreviewPanel(), null); 
			pnlHeadFoot.add(getHeaderDefButton(), null);
			pnlHeadFoot.add(getFooterDefButton(), null); 
//			pnlHeadFoot.add(headerLabel, null);
//			pnlHeadFoot.add(footerLabel, null);
//			pnlHeadFoot.add(getHeaderDistanceTextField(), null);
//			pnlHeadFoot.add(getFooterDistanceTextField(), null);
			  
//			getHeaderDistanceTextField().setText(
//					"" + _headFooterModel.getHeaderDistance());
//			getFooterDistanceTextField().setText(
//					"" + _headFooterModel.getFooterDistance());
			
			updateDisplay();
			
		}
		return pnlHeadFoot;
	}

	private JTextField getHeaderDistanceTextField() {
		if (fldHeaderDistance == null) {
			fldHeaderDistance = new UITextField();
			fldHeaderDistance.setBounds(451, 23, 55, 20);
			fldHeaderDistance.setHorizontalAlignment(SwingConstants.RIGHT);
			fldHeaderDistance.setDocument(new FloatDocument(0, Float.MAX_VALUE));
	  
		}
		return fldHeaderDistance;
	}
 
	private JTextField getFooterDistanceTextField() {
		if (fldFooterDistance == null) {
			fldFooterDistance = new UITextField();
			fldFooterDistance.setBounds(452, 185, 55, 20);
			fldFooterDistance.setHorizontalAlignment(SwingConstants.RIGHT);
			fldFooterDistance.setDocument(new FloatDocument(0, Float.MAX_VALUE));		
		}
		return fldFooterDistance;
	}
 
	
	/**
	 * @i18n report00027=自定义页眉
	 * @i18n report00028=自定义页脚
	 */
	private void defHeaderOrFooter(boolean isHeader){
		int startPosition = isHeader ? 0 : 3;
		HeaderFooterDefDlg defDlg ;
//		if(getHeadFootModel() == null){
//			defDlg = new HeaderFooterDefDlg(this,null,null,null);
//		}else{
			defDlg = new HeaderFooterDefDlg(
					this,
					_headFooterModel.getHeaderFooterSegmentModel(startPosition),
					_headFooterModel.getHeaderFooterSegmentModel(startPosition+1),
					_headFooterModel.getHeaderFooterSegmentModel(startPosition+2)
					);
//		}

		defDlg.setTitle(isHeader?MultiLang.getString("report00027"):MultiLang.getString("report00028"));
		defDlg.setVisible(true);
		if(defDlg.getResult()==UfoDialog.ID_OK){
			HeaderFooterSegmentModel[] segmentModels = defDlg.getSegmentModels();
			if(segmentModels != null && segmentModels.length == 3 && 
					(segmentModels[0]!=null || segmentModels[1]!=null || segmentModels[2]!=null)){

				if(_headFooterModel == null){
					_headFooterModel = new HeaderFooterModel();
				}
				for(int i=0;i<3;i++){
					if((segmentModels[i].getValue()==null||"".equals(segmentModels[i].getValue()))&&segmentModels[i].getImage()!=null){
						segmentModels[i].setImage(null);
					}
					_headFooterModel.setHeaderFooterSegmentModel(i+startPosition,segmentModels[i]);
				}
			}
			updateDisplay();
		}
	}
//	
//	private HeaderFooterModel getHeadFootModel() {
//		if(_headFooterModel != null){
//			if(getHeaderDistanceTextField().getText().trim().length()>0){
//				_headFooterModel.setHeaderDistance(Double.parseDouble(getHeaderDistanceTextField().getText().trim()));
//			}
//			if(getFooterDistanceTextField().getText().trim().length()>0){
//				_headFooterModel.setFooterDistance(Double.parseDouble(getFooterDistanceTextField().getText().trim()));
//			}	
//		}
//		return _headFooterModel;
//	}
	
	private void updateDisplay() {
		getHeaderPreviewPanel().removeAll();
		getFooterPreviewPanel().removeAll();
		if(_headFooterModel != null){
			Dimension preSize = getHeaderPreviewPanel().getSize();
			for(int position=HeaderFooterModel.HEADERE_LEFT;position<=HeaderFooterModel.FOOTER_RIGHT;position++){
				HeaderFooterSegmentModel segmentModel = _headFooterModel.getHeaderFooterSegmentModel(position);
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
	 * modify by guogang 2007-7-20 打印缩放到单页的修改
	 * 得到页面的设置,需要检查所有的输入值是否合法.在方法restrict()中对于某些组件的录入已经作了限制,所以 忽略这些组件值的检查.
	 * modify by guogang 2007-12-6 页眉、页脚的高度确省为纸张边距高度
	 */
	private void fillPrintSet() throws InputException {
		
//		String value = getHeaderDistanceTextField().getText().trim();
//		if(value.length() <= 0){
//			value = "0";
//		}
//		_headFooterModel.setHeaderDistance(Double.parseDouble(value));
		_headFooterModel.setHeaderDistance(printset.getPageformat().getImageableY());
//		value = getFooterDistanceTextField().getText().trim();
//		if(value.length() <= 0){
//			value = "0";
//		}
//     	_headFooterModel.setFooterDistance(Double.parseDouble(value));
     	_headFooterModel.setFooterDistance((printset.getPageformat().getHeight()-printset.getPageformat().getImageableHeight()-printset.getPageformat().getImageableY()));
		String strScale = fldPrintScale.getText().trim();
		if("".equals(strScale) || "0".equals(strScale)){
			strScale = "1.0";
		}
		float scale = Float.parseFloat(strScale);
//		if (scale < MIN_SCALE || scale > MAX_SCALE) {
//			throw new InputException(MultiLang.getString("m1"));//("打印比例设置错误(0.3-3.0)");
//		}
		printset.setViewScale(scale);
		//add by guogang
		printset.setPageToOne(rbPageToOne.isSelected());
		printset.setHCenter(rbHCenter.isSelected());
		printset.setVCenter(rbVCenter.isSelected());
		printset.setColPriorityPrinted(rbColToRow.isSelected());
	 
		try {
			int[] rang = null;
			// if(cbHeader.isEnabled()) {
			int start1 = getIntValue(tfRowHeader1.getText());
			int end1 = getIntValue(tfRowHeader2.getText());
			if (start1 > end1) {
				throw new InputException(MultiLang.getString("m2"));//("行头部信息设置错误,结束位置小于起始位置.");
			}
			if (start1 != 0 || end1 != 0) {
				rang = new int[]{start1 - 1, end1};
			}
			//}
			printset.setRowHeadRang(rang);
			rang = null;
			//if(cbTail.isEnabled()) {
			//将客户计数习惯转为程序计数习惯：从1开始－》从0开始；包括最大值－》不包括最大值。
			int start2 = colLabel2colValue(tfColHeader1.getText());
			int end2 = colLabel2colValue(tfColHeader2.getText());
			if (start2 > end2) {
				throw new InputException(MultiLang.getString("m3"));//("列头部信息设置错误,结束位置小于起始位置.");
			}
			if (start2 != 0 || end2 != 0) {
				rang = new int[]{start2 - 1, end2};
			}
			//}
			printset.setColHeadRang(rang);
			int startRow = getIntValue(tfAreaRow1.getText());
			int startCol = colLabel2colValue(tfAreaCol1.getText());
			int endRow = getIntValue(tfAreaRow2.getText());
			int endCol = colLabel2colValue(tfAreaCol2.getText());
			if (startRow > endRow) {
				throw new InputException(MultiLang.getString("m4"));//("打印区域设置错误,行结束位置小于起始位置.");
			}
			if (startCol > endCol) {
				throw new InputException(MultiLang.getString("m5"));//("打印区域设置错误,列结束位置小于起始位置.");
			}
			int[] area = null;
			if (startRow != 0 || startCol != 0 || endRow != 0 || endCol != 0) {
				//设置为0或空，起始位置默认值为-1，结束位置默认值为0。目的是与正常设置情况下算法一致。
				area = new int[]{startRow - 1, startCol - 1, endRow, endCol};
			}
			printset.setPrintArea(area);
			
			
		} catch (Exception e) {
			throw new InputException(MultiLang.getString("m6"));//("打印区域设置错误");
		}
		
	}
	
	
	/**
	 * 给组件的录入允许范围添加限制.
	 */
	private void restrict() {
		ButtonGroup g1 = new ButtonGroup();
		//设置按钮组
		g1.add(rbColToRow);
		g1.add(rbRowToCol);
		//设置输入比例的检查.
		fldPrintScale.setDocument(new FloatDocument(PrintSet.MIN_SCALE, PrintSet.MAX_SCALE));
		//RadioButton 行
		ItemListener itemL = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == rbColToRow) {
					if (rbColToRow.isSelected()) {
						lblOrderImg.setIcon(iconCol2Row);
					}
				} else if (e.getSource() == rbRowToCol) {
					if (rbRowToCol.isSelected()) {
						lblOrderImg.setIcon(iconRow2Col);
					}
				}
				
			}
		};
		rbColToRow.addItemListener(itemL);
		rbRowToCol.addItemListener(itemL);
		JTextField[] tfs = new JTextField[]{tfAreaRow1,
				tfAreaRow2, tfRowHeader1, tfRowHeader2
				};
		for (int i = 0; i < tfs.length; i++) {
			tfs[i].setDocument(new IntegerDocument());
			tfs[i].setHorizontalAlignment(JTextField.CENTER);
		} 
		
		tfs = new JTextField[]{tfAreaCol1, tfAreaCol2, tfColHeader1,
				tfColHeader2};
		for (int i = 0; i < tfs.length; i++) {
			tfs[i].setDocument(new ColDocument());
			tfs[i].setHorizontalAlignment(JTextField.CENTER);
		} 
	 
	}
	
	//校验输入列值的合法性
	private boolean validColValue(String v){
		if(v == null || v.length() < 1){
			return true;
		}
		if(v.length() > 2){
			return false;
		}
		
		char[] arr  = v.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			if(c < 'A' || c > 'Z'){
				return false;
			}
		}
		
		return true;
	}
	
	private int colLabel2colValue(String lbl){
		if(lbl == null || lbl.trim().length() < 1){
			return 0;
		}
		if(!validColValue(lbl)){
			return 0;
		}
		
		char[] arr = lbl.toCharArray();
		if(arr.length > 2){
			return 0;
		}
		int val = 0;
		if(arr.length == 2){
			char c = arr[0];
			val = ((c - 'A') + 1) * 26;
			val += (arr[1] - 'A') + 1;
		} else {
			val += (arr[0] - 'A') + 1;
		}
//		for (int i = 0; i < arr.length; i++){
//			val += ((arr.length - 1 - i) * 26) + (arr[i] - 'A') + 1;
//		}
		return val;
	}
	
	private String colValue2ColLabel(int val){
		if(val == 0){
			return "";
		}
		String lbl = "";
		int a, b = 0;
		a = val;
		while(true){
			double a1 = a / 26;
			b = a % 26;
			lbl = String.valueOf((char)(b + 'A' - 1)) + lbl;
			if(a1 < 1){
				break;
			}
			a = (int) (a1);
		}
		return lbl;
		
	}
	 
	
	private class ColDocument extends PlainDocument {
		private static final long serialVersionUID = 3202393796638006597L;
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			
			str = str.toUpperCase();
			if(!validColValue(str)){
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			super.insertString(offs, str, a);
		}
	}
	
	private class InputException extends Exception {
		private static final long serialVersionUID = -7345959918983527268L;
		public InputException(String msg) {
			super(msg);
		}
	}
	
	private String getIntString(int value) {
		if (value <= 0) {
			return "";
		}
		return Integer.toString(value);
	}
	private int getIntValue(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		return Integer.parseInt(s);
	}
	
	private JPanel getHeaderPreviewPanel() {
		if (headerPreviewPanel == null) {
			headerPreviewPanel = new UIPanel();
			headerPreviewPanel.setBounds(20, 20, 349, 95);
			headerPreviewPanel.setBackground(Color.WHITE);
			headerPreviewPanel.setLayout(null);
			headerPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		return headerPreviewPanel;
	}
   
	private JPanel getFooterPreviewPanel() {
		if (footerPreviewPanel == null) {
			footerPreviewPanel = new UIPanel();
			footerPreviewPanel.setBounds(20, 180, 349, 95);
			footerPreviewPanel.setBackground(Color.WHITE);
			footerPreviewPanel.setLayout(null);
			footerPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		return footerPreviewPanel;
	}
	/**
	 * This method initializes headerDefButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00027=自定义页眉
	 */    
	private JButton getHeaderDefButton() {
		if (btnHeaderDef == null) {
			btnHeaderDef = new JButton();
			btnHeaderDef.setBounds(56, 133, 120, 22);
			btnHeaderDef.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					defHeaderOrFooter(true);
				}
			});
			btnHeaderDef.setText(MultiLang.getString("report00027"));
		}
		return btnHeaderDef;
	}
	
	/**
	 * This method initializes footerDefButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00028=自定义页脚
	 */    
	private JButton getFooterDefButton() {
		if (btnFooterDef == null) {
			btnFooterDef = new JButton();
			btnFooterDef.setBounds(214, 132, 120, 22);
			btnFooterDef.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					defHeaderOrFooter(false);
				}
			});
			btnFooterDef.setText(MultiLang.getString("report00028"));
		}
		return btnFooterDef;
	}
	/**
	 * This method initializes pnlPrintArea	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlPrintArea() {
		if (pnlPrintArea == null) {
			pnlPrintArea = new JPanel();
			pnlPrintArea.setLayout(null);
			pnlPrintArea.add(getPnlArea1(), null);
			pnlPrintArea.add(getPnlAreaTitle(), null);
			 
		}
		return pnlPrintArea;
	}

	/**
	 * This method initializes pnlBtnView	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlBtnView() {
		if (pnlBtnView == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			pnlBtnView = new JPanel();
			pnlBtnView.setLayout(null);
			pnlBtnView.add(getBtnPrint(), null);
			pnlBtnView.add(getBtnPrintView(), null);
			pnlBtnView.add(getBtnPageSetting(), null);
		}
		return pnlBtnView;
	}

	/**
	 * This method initializes btnPrint	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n print=打印
	 */
	private JButton getBtnPrint() {
		if (btnPrint == null) {
			btnPrint = new JButton();
			btnPrint.setText(MultiLang.getString("print"));
			btnPrint.setBounds(new Rectangle(6, 119, 92, 28));
			btnPrint.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean rlt = performOK();
					if(!rlt){
						return;
					}		 				
					close();			
					m_Rep.getTable().print(); 	 
				}
			});	
		}
		return btnPrint;
	}

	/**
	 * This method initializes btnPrintView	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n PrintPreview=打印预览
	 */
	private JButton getBtnPrintView() {
		if (btnPrintView == null) {
			btnPrintView = new JButton();
			btnPrintView.setText(MultiLang.getString("PrintPreview"));
			btnPrintView.setBounds(new Rectangle(6, 77, 92, 28));
			btnPrintView.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean rlt = performOK();
					if(!rlt){
						return;
					}				
					close();					
					m_Rep.getTable().printPreview(m_Rep);					 
		          //  PrintSettingDlg.this.setVisible(true);
				}
			});			
			
			
		}
		return btnPrintView;
	}

	/**
	 * 页面设置
	 * @i18n PagePrintSet=页面设置
	 */
	private JButton getBtnPageSetting() {
		if (btnPageSetting == null) {
			btnPageSetting = new JButton();
			btnPageSetting.setText(MultiLang.getString("PagePrintSet"));
			btnPageSetting.setBounds(new Rectangle(6, 34, 93, 28));
			btnPageSetting.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean rlt = performOK();
					if(!rlt){
						return;
					}		
					PrintSettingDlg.this.setVisible(false);
//					m_Rep.getTable().pageFromat(); 					
//					initialize();
					PrintSet ps = m_Rep.getTable().getPrintSet();
		            PageFormat oldPf = ps.getPageformat();
		            PageFormat newPf = m_Rep.getTable().getPrinterJob().pageDialog(oldPf);
		    		//这里需要检查返回的页面设置是否合法，如果不合法，需要修正。
//		    		PageFormat validPf = getPrinterJob().validatePage(pf);
		            ps.setPageFormat(newPf);
		            if(m_Rep.getTable().getPrintPageRect().length == 0){
		            	ps.setPageFormat(oldPf);
		            }
		            PrintSettingDlg.this.printset = ps;
		            PrintSettingDlg.this.setVisible(true);
		            
				}
			});
		}
		return btnPageSetting;
	}

	/**
	 * 打印顺序的面板构建
	 * @return
	 * @i18n PrintOrder=打印顺序
	 */
	private JPanel getPnlPrintOrder() {
		if (pnlPrintOrder == null) {
			lblOrderImg = new JLabel(this.iconCol2Row);
			lblOrderImg.setBounds(new Rectangle(133, 18, 125, 85));
			lblOrderImg.setText("");
			pnlPrintOrder = new JPanel();
			pnlPrintOrder.setLayout(null);
			pnlPrintOrder.setBounds(new Rectangle(21, 120, 446, 125));
			pnlPrintOrder.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("PrintOrder"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), Color.blue));
			 
			 rbRowToCol = new JRadioButton(MultiLang
					.getString("RowThenColumn"));
			 rbRowToCol.setBounds(new Rectangle(39, 68, 77, 26));
			 rbColToRow = new JRadioButton(MultiLang
					.getString("ColumnThenRow"));
			 rbColToRow.setBounds(new Rectangle(39, 36, 77, 26));
			 pnlPrintOrder.add(rbColToRow, null);
			 pnlPrintOrder.add(rbRowToCol, null);
			 pnlPrintOrder.add(lblOrderImg, null);
			 
		}
		return pnlPrintOrder;
	}

	/**
	 * This method initializes pnlArea1	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n To=到
	 * @i18n From=从
	 * @i18n miufo00015=列(A,B,C...):
	 * @i18n miufo00016=行(1,2,3...):
	 * @i18n PrintArea=打印区域
	 */
	private JPanel getPnlArea1() {
		if (pnlArea1 == null) {
			lblTo2 = new JLabel();
			lblTo2.setBounds(new Rectangle(202, 65, 37, 18));
			lblTo2.setText(MultiLang.getString("To"));
			lblFrom2 = new JLabel();
			lblFrom2.setBounds(new Rectangle(94, 65, 32, 18));
			lblFrom2.setText(MultiLang.getString("From"));
			lblFrom2.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTo1 = new JLabel();
			lblTo1.setBounds(new Rectangle(201, 29, 38, 18));
			lblTo1.setText(MultiLang.getString("To"));
			lblFrom1 = new JLabel();
			lblFrom1.setBounds(new Rectangle(95, 29, 32, 18));
			lblFrom1.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFrom1.setText(MultiLang.getString("From"));
			lblColArea = new JLabel();
			lblColArea.setBounds(new Rectangle(10, 65, 85, 18));
			lblColArea.setHorizontalAlignment(SwingConstants.RIGHT);
			lblColArea.setText(MultiLang.getString("miufo00015"));
			lblRowArea = new JLabel();
			lblRowArea.setText(MultiLang.getString("miufo00016"));
			lblRowArea.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRowArea.setBounds(new Rectangle(10, 29, 85, 18));
			pnlArea1 = new JPanel();
			pnlArea1.setLayout(null);
			pnlArea1.setBounds(new Rectangle(20, 20, 447, 104));
			pnlArea1.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("PrintArea"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), Color.blue));
			pnlArea1.add(lblRowArea, null);
			pnlArea1.add(lblColArea, null);
			pnlArea1.add(lblFrom1, null);
			pnlArea1.add(lblTo1, null);
			pnlArea1.add(getTfAreaRow1(), null);
			pnlArea1.add(getTfAreaRow2(), null);
			pnlArea1.add(lblFrom2, null);
			pnlArea1.add(getTfAreaCol1(), null);
			pnlArea1.add(lblTo2, null);
			pnlArea1.add(getTfAreaCol2(), null);
		}
		return pnlArea1;
	}

	/**
	 * This method initializes pnlAreaTitle	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n To=到
	 * @i18n From=从
	 * @i18n miufo00017=左端标题列(A,B,C...):
	 * @i18n miufo00018=顶端标题行(1,2,3...):
	 * @i18n miufo00019=打印标题
	 */
	private JPanel getPnlAreaTitle() {
		if (pnlAreaTitle == null) {
			lblTo4 = new JLabel();
			lblTo4.setBounds(new Rectangle(207, 67, 32, 18));
			lblTo4.setText(MultiLang.getString("To"));
			lblFrom4 = new JLabel();
			lblFrom4.setBounds(new Rectangle(106, 67, 32, 18));
			lblFrom4.setText(MultiLang.getString("From"));
			lblFrom4.setHorizontalAlignment(SwingConstants.RIGHT);
			lblColTitle = new JLabel();
			lblColTitle.setBounds(new Rectangle(5, 67, 115, 18));
			lblColTitle.setText(MultiLang.getString("miufo00017"));
			lblColTitle.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTo3 = new JLabel();
			lblTo3.setBounds(new Rectangle(206, 31, 32, 18));
			lblTo3.setText(MultiLang.getString("To"));
			lblFrom3 = new JLabel();
			lblFrom3.setBounds(new Rectangle(106, 31, 31, 18));
			lblFrom3.setText(MultiLang.getString("From"));
			lblFrom3.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRowTitle = new JLabel();
			lblRowTitle.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRowTitle.setBounds(new Rectangle(5, 31, 115, 18));
			lblRowTitle.setText(MultiLang.getString("miufo00018"));
			pnlAreaTitle = new JPanel();
			pnlAreaTitle.setLayout(null);
			pnlAreaTitle.setBounds(new Rectangle(20, 159, 447, 119));
			pnlAreaTitle.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("miufo00019"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), Color.blue));
			pnlAreaTitle.add(lblRowTitle, null);
			pnlAreaTitle.add(lblFrom3, null);
			pnlAreaTitle.add(getTfRowHeader1(), null);
			pnlAreaTitle.add(lblTo3, null);
			pnlAreaTitle.add(getTfRowHeader2(), null);
			pnlAreaTitle.add(lblColTitle, null);
			pnlAreaTitle.add(lblFrom4, null);
			pnlAreaTitle.add(getTfColHeader1(), null);
			pnlAreaTitle.add(lblTo4, null);
			pnlAreaTitle.add(getTfColHeader2(), null);
		}
		return pnlAreaTitle;
	}

	/**
	 * This method initializes tfAreaRow1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfAreaRow1() {
		if (tfAreaRow1 == null) {
			tfAreaRow1 = new JTextField();
			tfAreaRow1.setBounds(new Rectangle(138, 26, 55, 22));
		}
		return tfAreaRow1;
	}

	/**
	 * This method initializes tfAreaRow2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfAreaRow2() {
		if (tfAreaRow2 == null) {
			tfAreaRow2 = new JTextField();
			tfAreaRow2.setBounds(new Rectangle(238, 26, 55, 22));
		}
		return tfAreaRow2;
	}

	/**
	 * This method initializes tfAreaCol1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfAreaCol1() {
		if (tfAreaCol1 == null) {
			tfAreaCol1 = new JTextField();
			tfAreaCol1.setBounds(new Rectangle(137, 62, 55, 22));
		}
		return tfAreaCol1;
	}

	/**
	 * This method initializes tfAreaCol2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfAreaCol2() {
		if (tfAreaCol2 == null) {
			tfAreaCol2 = new JTextField();
			tfAreaCol2.setBounds(new Rectangle(239, 62, 55, 22));
		}
		return tfAreaCol2;
	}

	/**
	 * This method initializes tfRowHeader1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfRowHeader1() {
		if (tfRowHeader1 == null) {
			tfRowHeader1 = new JTextField();
			tfRowHeader1.setBounds(new Rectangle(145, 28, 55, 22));
		}
		return tfRowHeader1;
	}

	/**
	 * This method initializes tfRowHeader2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfRowHeader2() {
		if (tfRowHeader2 == null) {
			tfRowHeader2 = new JTextField();
			tfRowHeader2.setBounds(new Rectangle(240, 28, 55, 22));
		}
		return tfRowHeader2;
	}

	/**
	 * This method initializes tfColHeader1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfColHeader1() {
		if (tfColHeader1 == null) {
			tfColHeader1 = new JTextField();
			tfColHeader1.setBounds(new Rectangle(147, 64, 55, 22));
		}
		return tfColHeader1;
	}

	/**
	 * This method initializes tfColHeader2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfColHeader2() {
		if (tfColHeader2 == null) {
			tfColHeader2 = new JTextField();
			tfColHeader2.setBounds(new Rectangle(241, 64, 55, 22));
		}
		return tfColHeader2;
	}
	

}  //  @jve:decl-index=0:visual-constraint="32,9"
 