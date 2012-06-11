package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITextField;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.ITopNSet;
import com.ufida.report.anareport.edit.CrossMeasureOrderSetDlg.FixHeaderSetPanel;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufida.report.crosstable.CrossMeasureTopNSet;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

public class CrossTopNDefDlg extends UfoDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tpTabs;
	private MeasureTopNPanel rowSetPanel;//按行设置
	private MeasureTopNPanel colSetPanel;//按列设置
	private AnaDataSetTool dsTool=null;
	private FieldCountDef[] measures=null;
	private JButton btnOK;
	private JButton btnCancel;

	/**
	 * @i18n iufobi00091=指标TopN设置
	 * @i18n iufobi00092=按行TopN分析
	 * @i18n iufobi00093=按列TopN分析
	 */
	public CrossTopNDefDlg(Container parent,CrossMeasureTopNSet rowSet,Field[] rowField,CrossMeasureTopNSet colSet,Field[] colField,AnaDataSetTool dsTool,FieldCountDef[] measures) {
		super(parent);
		this.setTitle(StringResource.getStringResource("iufobi00091"));
		this.dsTool=dsTool;
		this.measures=measures;
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		tpTabs= new JTabbedPane();
		tpTabs.setBorder(new EmptyBorder(5, 5, 5, 5));
		if(rowSet!=null){
			rowSetPanel= new MeasureTopNPanel(rowSet,rowField);
			tpTabs.add(StringResource.getStringResource("iufobi00092"), rowSetPanel);
		}
		if(colSet!=null){
			colSetPanel=new MeasureTopNPanel(colSet,colField);
			tpTabs.add(StringResource.getStringResource("iufobi00093"), colSetPanel);
		}
		if(colSet!=null&&colSet.isEnabled()){
			tpTabs.setSelectedIndex(1);
		}
		if(rowSet!=null&&rowSet.isEnabled()){
			tpTabs.setSelectedIndex(0);
		}
		
		container.add(tpTabs, "Center");
		container.add(getCmdPanel(), java.awt.BorderLayout.SOUTH);
		
		setResizable(false);
		pack();
	}
    private FieldCountDef[] getOtherFlds(){
    	return this.measures;
    }
    private AnaDataSetTool getDataSetTool(){
    	return this.dsTool;
    }
	private JPanel getCmdPanel() {
		JPanel jCmdPanel = new UIPanel(new FlowLayout(FlowLayout.TRAILING));
		btnOK = new UIButton(MultiLang.getString("ok"));
		btnOK.addActionListener(this);
		jCmdPanel.add(btnOK);
		btnCancel = new UIButton(MultiLang.getString("cancel"));
		btnCancel.addActionListener(this);
		jCmdPanel.add(btnCancel);

		return jCmdPanel;
	}
	public CrossMeasureTopNSet getTopNSetInfo(boolean isRow){
		if(isRow&&rowSetPanel!=null){
			return rowSetPanel.getTopNSet();
		}
		if(!isRow&&colSetPanel!=null){
			return colSetPanel.getTopNSet();
		}
		
		return null;
	}
	public void actionPerformed(ActionEvent actionevent) {
		if (actionevent.getSource() == btnOK) {
			if (rowSetPanel != null) {
				rowSetPanel.topNPanel.getElsePanel().stopTableEdit();
				String msg = rowSetPanel.checkTopNSet();
				if (msg != null) {
					tpTabs.setSelectedComponent(rowSetPanel);
					MessageDialog.showHintDlg(this, null, msg);
					return;
				}
				rowSetPanel.updateInfo();
			}
			if (colSetPanel != null) {
				colSetPanel.topNPanel.getElsePanel().stopTableEdit();
				String msg = colSetPanel.checkTopNSet();
				if (msg != null) {
					tpTabs.setSelectedComponent(colSetPanel);
					MessageDialog.showHintDlg(this, null, msg);
					return;
				}
				colSetPanel.updateInfo();
			}
			setResult(ID_OK);
			close();
		}
		
		if(actionevent.getSource()==btnCancel){
			setResult(ID_CANCEL);
			close();
		}
	}

	private class MeasureTopNPanel extends UIPanel implements ItemListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private EnablePanel enablePanel;
		private TopNPanel topNPanel;
		private FixHeaderSetPanel headerSetPanel;
        private CrossMeasureTopNSet topNSet;
        private Field[] fields;
        /**
         * 
         * @create by guogang at Feb 28, 2009,7:16:32 PM
         *
         * @param orderSet
         * @param fields 纬度字段，如果指标在改方向上则包括指标
         */
		public MeasureTopNPanel(CrossMeasureTopNSet topNSet,Field[] fields) {
			super();
            this.topNSet=topNSet;
            this.fields=fields;
            initUI();
		}

		private void initUI(){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			enablePanel=new EnablePanel(topNSet);
			add(enablePanel);
			enablePanel.addItemListener(this);
			topNPanel=new TopNPanel(topNSet);
	        add(topNPanel);
	        topNPanel.addItemListener(this);
	        enablePanel.initInfo();
	        topNPanel.initInfo();
	        headerSetPanel=new FixHeaderSetPanel(topNSet,fields,getDataSetTool());
	        add(headerSetPanel);
		}
	  
	   public void updateInfo(){
		    enablePanel.updateInfo();
		    topNPanel.updateInfo();
			headerSetPanel.updateInfo();
	   }
	   
	   public String checkTopNSet() {
		   if (!enablePanel.getYesRb().isSelected())
				return null;
			String strN = topNPanel.getTextN().getText();
			if (strN == null || strN.trim().length() == 0)
				return StringResource.getStringResource("miufo00254");
			Integer n = Integer.valueOf(strN);
			if(n<=0)
				return StringResource.getStringResource("miufopublic389");//miufopublic389=请输入大于0的整数！

			return null;
		}
	   
		public CrossMeasureTopNSet getTopNSet(){
				return topNSet;
		}

		public void itemStateChanged(ItemEvent e) {
			topNPanel.getElsePanel().stopTableEdit();
			if (e.getSource() == enablePanel.getYesRb()
					|| e.getSource() == enablePanel.getNoRb()) {
				if (enablePanel.getYesRb().isSelected()) {
					topNPanel.getTextN().setEditable(true);
					topNPanel.getAscRb().setEnabled(true);
					topNPanel.getDesRb().setEnabled(true);
					topNPanel.getExtendYesRb().setEnabled(true);
					topNPanel.getExtendNoRb().setEnabled(true);
					topNPanel.getShowElse().setEnabled(true);
					topNPanel.getElsePanel().setEnabled(topNPanel.getShowElse().isSelected());
				} else {
					topNPanel.getTextN().setEditable(false);
					topNPanel.getAscRb().setEnabled(false);
					topNPanel.getDesRb().setEnabled(false);
					topNPanel.getExtendYesRb().setEnabled(false);
					topNPanel.getExtendNoRb().setEnabled(false);
					topNPanel.getShowElse().setEnabled(false);
					topNPanel.getElsePanel().setEnabled(false);
				}
			}
			if (e.getSource() == topNPanel.getShowElse()) {
				topNPanel.getElsePanel().setEnabled(topNPanel.getShowElse().isEnabled()&&topNPanel.getShowElse().isSelected());
			}
		}
	}
	
	public class TopNPanel extends UIPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private CrossMeasureTopNSet topNSet;

		private UITextField jTextN = null;// N
		private JRadioButton jrbAsc = null;
		private JRadioButton jrbDes = null;
		private JRadioButton jrbExtendYes = null;
		private JRadioButton jrbExtendNo = null;
		private JCheckBox jShowElse = null;// 显示其他
		private TopNElseDesPanel jElsePanel = null;// 其他的设置面板

		public TopNPanel(CrossMeasureTopNSet topNSet) {
			super();
			this.topNSet = topNSet;
			initUI();
		}

		/**
		 * @i18n iufobi00094=TopN分析设置
		 * @i18n iufobi00095=数据排序
		 * @i18n miufo00424=补足行数
		 */
		private void initUI() {
			this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("iufobi00094"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			JPanel texNPanel = new UIPanel(new FlowLayout(FlowLayout.LEADING));
			texNPanel.setBorder(BorderFactory.createTitledBorder(null, "",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			JLabel lblN = new JLabel(StringResource
					.getStringResource("miufo00253"));
			texNPanel.add(lblN);
			texNPanel.add(getTextN());
			add(texNPanel);
			JPanel orderPanel = new UIPanel(new FlowLayout(FlowLayout.LEADING));
			orderPanel.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("iufobi00095"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			ButtonGroup ordergroup = new ButtonGroup();
			ordergroup.add(getAscRb());
			orderPanel.add(getAscRb());
			ordergroup.add(getDesRb());
			orderPanel.add(getDesRb());
			add(orderPanel);
			JPanel extendNPanel = new UIPanel(new FlowLayout(
					FlowLayout.LEADING));
			extendNPanel.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("miufo00424"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			ButtonGroup extendgroup = new ButtonGroup();
			extendgroup.add(getExtendYesRb());
			extendNPanel.add(getExtendYesRb());
			extendgroup.add(getExtendNoRb());
			extendNPanel.add(getExtendNoRb());
			add(extendNPanel);
			JPanel box2Panel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			box2Panel.add(getShowElse());
			add(box2Panel);
			add(getElsePanel());

		}
        
		public void initInfo(){
			if(topNSet.getN()>0){
				getTextN().setText(String.valueOf(topNSet.getN()));
			}
			if(topNSet.isASC()){
				getAscRb().setSelected(true);
			}else{
				getDesRb().setSelected(true);
			}
			if(topNSet.isExtendN()){
				getExtendYesRb().setSelected(true);
			}else{
				getExtendNoRb().setSelected(true);
			}
			if(topNSet.isShowElse()){
				getShowElse().setSelected(true);
			}else{
				getShowElse().setSelected(false);
			}
		}
		
		public UITextField getTextN() {
			if (jTextN == null) {
				jTextN = new UITextField();
				jTextN.setTextType("TextInt");
//				jTextN.setMinValue(1);
			}
			return jTextN;
		}

		/**
		 * @i18n miufo1001305=升序
		 */
		public JRadioButton getAscRb() {
			if (jrbAsc == null) {
				jrbAsc = new UIRadioButton(StringResource.getStringResource("miufo1001305"));
			}
			return jrbAsc;
		}

		/**
		 * @i18n miufo1001306=降序
		 */
		public JRadioButton getDesRb() {
			if (jrbDes == null) {
				jrbDes = new UIRadioButton(StringResource.getStringResource("miufo1001306"));
			}
			return jrbDes;
		}

		/**
		 * @i18n miufo1002256=是
		 */
		public JRadioButton getExtendYesRb() {
			if (jrbExtendYes == null) {
				jrbExtendYes = new UIRadioButton(StringResource.getStringResource("miufo1002256"));
			}
			return jrbExtendYes;
		}

		/**
		 * @i18n miufo1002257=否
		 */
		public JRadioButton getExtendNoRb() {
			if (jrbExtendNo == null) {
				jrbExtendNo = new UIRadioButton(StringResource.getStringResource("miufo1002257"));
			}
			return jrbExtendNo;
		}
		private JCheckBox getShowElse() {
			if (jShowElse == null) {
				jShowElse = new JCheckBox(StringResource.getStringResource(TopNDesignExt.RESID_TOPN_SHOWELSE));
			}
			return jShowElse;
		}
		public void addItemListener(ItemListener listener) {
			getAscRb().addItemListener(listener);
			getDesRb().addItemListener(listener);
			getExtendYesRb().addItemListener(listener);
			getExtendNoRb().addItemListener(listener);
			getShowElse().addItemListener(listener);
		}
		
		public void updateInfo() {
			topNSet.setASC(getAscRb().isSelected());
			if (!getTextN().getText().equals(""))
				topNSet.setN(Integer.parseInt(getTextN().getText()));
			topNSet.setExtendN(getExtendYesRb().isSelected());
			topNSet.setShowElse(getShowElse().isSelected());
			topNSet.setElseFields(getElsePanel().getElseFieldList());
		}
		
		/**
		 * @i18n iufobi00096=其他设置
		 */
		private TopNElseDesPanel getElsePanel() {
			if (jElsePanel == null) {
				if(this.topNSet==null){
					jElsePanel = new TopNElseDesPanel(getOtherFlds(),null);
				}else{
					jElsePanel = new TopNElseDesPanel(getOtherFlds(),this.topNSet.getElseFields());
				}
				jElsePanel.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("iufobi00096"), TitledBorder.DEFAULT_JUSTIFICATION,
						TitledBorder.DEFAULT_POSITION, new Font("Dialog",
								Font.BOLD, 12), Color.blue));
			}
			return jElsePanel;
		}
}
	
	public class EnablePanel extends UIPanel{
		private static final long serialVersionUID = 1L;
		private JRadioButton jrbYes = null;
		private JRadioButton jrbNo = null;
		
		private ITopNSet topNSet;
		/**
		 * @i18n uiufo20037=是否启用
		 */
		public EnablePanel(ITopNSet topNSet) {
			super();
			this.topNSet=topNSet;
			setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("uiufo20037"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			setLayout(new FlowLayout(FlowLayout.LEADING));
			ButtonGroup setgroup = new ButtonGroup();
			setgroup.add(getYesRb());
			add(getYesRb());
			setgroup.add(getNoRb());
			add(getNoRb());
		}
		
	    /**
		 * @i18n miufo1002256=是
		 */
	    public JRadioButton getYesRb(){
	    	if(jrbYes==null){
	    		jrbYes=new UIRadioButton(StringResource.getStringResource("miufo1002256"));
	    	}
	    	return jrbYes;
	    }
	    /**
		 * @i18n miufo1002257=否
		 */
	    public JRadioButton getNoRb(){
	    	if(jrbNo==null){
	    		jrbNo=new UIRadioButton(StringResource.getStringResource("miufo1002257"));
	    	}
	    	return jrbNo;
	    }
	    
	    public void addItemListener(ItemListener listener){
	    	getYesRb().addItemListener(listener);
	    	getNoRb().addItemListener(listener);
	    }
	    public void initInfo() {
			if (topNSet.isEnabled()) {
				getYesRb().setSelected(true);
			} else {
				getNoRb().setSelected(true);
			}
		}
	    
	    public void updateInfo(){
	    	topNSet.setEnabled(getYesRb().isSelected());
	    }
	}
}
 