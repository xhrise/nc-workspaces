/**
 * 
 */
package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufida.report.anareport.model.AnaReportCondition;
import com.ufida.report.crosstable.CrossMeasureOrderSet;
import com.ufida.report.crosstable.CrossTableSet;
import com.ufida.report.crosstable.ICrossAnalyseSet;
import com.ufida.report.rep.applet.PageDimItem;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * @author guogang
 * @created at Feb 28, 2009,3:33:34 PM
 *
 */
public class CrossMeasureOrderSetDlg extends UfoDialog implements
		ActionListener {
	private static final long serialVersionUID = 1L;

	private MeasureOrderPanel rowSetPanel;//按行设置
	private MeasureOrderPanel colSetPanel;//按列设置
    private AnaDataSetTool dsTool=null;
	private JButton btnOK;
	private JButton btnCancel;

	/**
	 * @i18n iufobi00019=指标排序设置
	 * @i18n iufobi00020=按行排序
	 * @i18n iufobi00021=按列排序
	 */
	public CrossMeasureOrderSetDlg(Container parent,CrossMeasureOrderSet rowSet,Field[] rowField,CrossMeasureOrderSet colSet,Field[] colField,AnaDataSetTool dsTool) {
		super(parent);
		this.setTitle(StringResource.getStringResource("iufobi00019"));
		this.dsTool=dsTool;
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		JTabbedPane tpTabs = new JTabbedPane();
		tpTabs.setBorder(new EmptyBorder(5, 5, 5, 5));
		if(rowSet!=null){
			rowSetPanel= new MeasureOrderPanel(rowSet,rowField,dsTool);
			tpTabs.add(StringResource.getStringResource("iufobi00020"), rowSetPanel);
		}
		if(colSet!=null){
			colSetPanel=new MeasureOrderPanel(colSet,colField,dsTool);
			tpTabs.add(StringResource.getStringResource("iufobi00021"), colSetPanel);
		}
		if(colSet!=null&&colSet.getOrderType()!=CrossTableSet.ORDER_TYPE_NONE){
			tpTabs.setSelectedIndex(1);
		}
		if(rowSet!=null&&rowSet.getOrderType()!=CrossTableSet.ORDER_TYPE_NONE){
			tpTabs.setSelectedIndex(0);
		}
		container.add(tpTabs, "Center");
		container.add(getCmdPanel(), java.awt.BorderLayout.SOUTH);
		
		setResizable(false);
		pack();
	}

	private JPanel getCmdPanel() {
		JPanel jCmdPanel = new UIPanel(new FlowLayout(FlowLayout.TRAILING));
		btnOK = createOkButton();
		btnOK.addActionListener(this);
		jCmdPanel.add(btnOK);
		btnCancel = createCancleButton();
		btnCancel.addActionListener(this);
		jCmdPanel.add(btnCancel);

		return jCmdPanel;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOK) {
			if (rowSetPanel != null) {
				rowSetPanel.updateInfo();
			}
			if (colSetPanel != null) {
				colSetPanel.updateInfo();
			}
		}

	}
	public CrossMeasureOrderSet getOrderSetInfo(boolean isRow){
		if(isRow&&rowSetPanel!=null){
			return rowSetPanel.getOrderSet();
		}
		if(!isRow&&colSetPanel!=null){
			return colSetPanel.getOrderSet();
		}
		
		return null;
	}
		
	private class MeasureOrderPanel extends UIPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private OrderPanel orderPanel;
		private FixHeaderSetPanel headerSetPanel;
        private CrossMeasureOrderSet orderSet;
        private Field[] fields;
        private AnaDataSetTool dsTool;
        /**
         * 
         * @create by guogang at Feb 28, 2009,7:16:32 PM
         *
         * @param orderSet
         * @param fields 纬度字段，如果指标在改方向上则包括指标
         */
		public MeasureOrderPanel(CrossMeasureOrderSet orderSet,Field[] fields,AnaDataSetTool dsTool) {
			super();
            this.orderSet=orderSet;
            this.fields=fields;
            this.dsTool=dsTool;
            initUI();
		}

		private void initUI(){
			GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.weighty=1.0D;
			gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
			gridbagconstraints.insets=new Insets(0,2,0,2);
			setLayout(gridbaglayout);
			gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
			orderPanel=new OrderPanel(orderSet);
			gridbaglayout.setConstraints(orderPanel, gridbagconstraints);
	        add(orderPanel);
	        headerSetPanel=new FixHeaderSetPanel(orderSet,fields,dsTool);
	        gridbaglayout.setConstraints(headerSetPanel, gridbagconstraints);
	        add(headerSetPanel);
		}
		
	   public void updateInfo(){
		    orderPanel.updateInfo();
			headerSetPanel.updateInfo();
	   }
		
		public CrossMeasureOrderSet getOrderSet(){
			return orderSet;
		}
	}

	private class OrderPanel extends UIPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private CrossMeasureOrderSet orderSet;
		
		private JRadioButton rbNoOrder;
		private JRadioButton rbAsceding;
		private JRadioButton rbDesceding;
		public OrderPanel(CrossMeasureOrderSet orderSet) {
			super();
			this.orderSet = orderSet;
			initUI();
		}

		/**
		 * @i18n iufobi00022=排序设置
		 */
		private void initUI() {
			   this.setLayout(new FlowLayout(FlowLayout.LEADING));
			   setBorder(new TitledBorder(new EtchedBorder(), StringResource.getStringResource("iufobi00022"), TitledBorder.LEFT, TitledBorder.TOP,
						new java.awt.Font("dialog", 0, 14)));
			   ButtonGroup sortgroup= new ButtonGroup();
			   rbNoOrder=new UIRadioButton(StringResource.getStringResource("miufo1001304"));
			   sortgroup.add(rbNoOrder);
			   add(rbNoOrder);
			   rbAsceding=new UIRadioButton(StringResource.getStringResource("miufo1001305"));
			   sortgroup.add(rbAsceding);
			   add(rbAsceding);
			   rbDesceding=new UIRadioButton(StringResource.getStringResource("miufo1001306"));
			   sortgroup.add(rbDesceding);
			   add(rbDesceding);
			   int orderType=orderSet.getOrderType();
			   switch(orderType){
			     case CrossTableSet.ORDER_TYPE_NONE:
			    	  rbNoOrder.setSelected(true);
			    	  break;
			     case CrossTableSet.ORDER_TYPE_ASCEDING:
			    	  rbAsceding.setSelected(true);
			    	  break;
			     case CrossTableSet.ORDER_TYPE_DESCENDING:
			    	  rbDesceding.setSelected(true);
			    	  break;
			    default :
			    	 rbNoOrder.setSelected(true);
			   }
		}

		public void updateInfo() {
			if(rbAsceding.isSelected()){
				orderSet.setOrderType(CrossTableSet.ORDER_TYPE_ASCEDING);
			}else if(rbDesceding.isSelected()){
				orderSet.setOrderType(CrossTableSet.ORDER_TYPE_DESCENDING);
			}else{
				orderSet.setOrderType(CrossTableSet.ORDER_TYPE_NONE);
			}
			
		}
	}


	public static class FixHeaderSetPanel extends UIPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ICrossAnalyseSet analyse;
		private Field[] fields;
		private AnaDataSetTool dsTool=null;
		private Hashtable<String,PageDimItem> values=new Hashtable<String,PageDimItem>();
		
		public FixHeaderSetPanel(ICrossAnalyseSet analyse,Field[] fields,AnaDataSetTool dsTool) {
			super();
			this.analyse=analyse;
			this.fields=fields;
			this.dsTool=dsTool;
			initUI();
		}

		/**
		 * @i18n miufo00298=成员设置
		 */
		private void initUI() {
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			setBorder(new TitledBorder(new EtchedBorder(), StringResource.getStringResource("miufo00298"), TitledBorder.LEFT, TitledBorder.TOP, new java.awt.Font(
							"dialog", 0, 14)));
			for (int i = 0; i < fields.length; i++) {
				if (analyse.getLatiEditor(fields[i].getFldname())) {
					PageDimItem item = new PageDimItem(new AnaReportCondition(
							dsTool, fields[i]));
					//在模态对话框中调用非模态对话框无法选中的问题
					item.getTextFieldt().getDialog().setModal(true);
					if (analyse.getLatiValue(fields[i].getFldname()) != null) {
						item.getTextFieldt().setText(
								analyse.getLatiValue(fields[i].getFldname())
										.toString());
					}
					values.put(fields[i].getFldname(), item);
					JPanel box2Panel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
					box2Panel.add(item);
					add(box2Panel);
				}
			}
		}

		public void updateInfo() {
			PageDimItem editField = null;
			for (int i = 0; i < fields.length; i++) {

				editField = values.get(fields[i].getFldname());
				if (editField != null
						&& !editField.getTextFieldt().getText().equals("")) {
					analyse.setLatiValue(fields[i].getFldname(), editField
							.getTextFieldt().getText());
				} 
			}

		}
		
	}
}
 