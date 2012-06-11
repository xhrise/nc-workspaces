package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.SimpleLayout;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.iufo.resource.StringResource;

public class AnaCrossAreaSetDlg extends UfoDialog{

	private static final long serialVersionUID = 1L;
	
    private AnaReportModel anaModel=null;
	private ExAreaCell selectExCell=null;//交叉区域所在的扩展区域
	private AnaCrossTableSet crossset = null;

	
	private UfoReport parent;
	private JPanel jAreaPanel=null;
	private JPanel topPanel=null;
	private JPanel bottomPanel=null;
	private ImageIcon upIcon=null;
	private ImageIcon downIcon=null;
	private JCheckBox jShowRowHeader = null;
	private JCheckBox jShowColHeader = null;
	private JPanel jCmdPanel = null;
	private JButton btnOK;
	private JButton btnCancel;
	private JButton btnPointFoldUp = null;
	private JTextField jCrossPointField;
	private JButton btnAreaFoldUp = null;
	private JTextField jCrossAreaField;
	private SelectListener m_selectListener = null;
	private boolean isEditArea=true;
	private EditFocusAdapter m_editFocusAdapter=null;
	
	/**
	 * @i18n miufo00336=交叉区域设置
	 */
	public AnaCrossAreaSetDlg(UfoReport container,AnaReportModel anaModel,ExAreaCell selectExCell,AnaCrossTableSet oldCrossSet) {
		super(container,StringResource.getStringResource("miufo00336"), false);
		this.parent=container;
		this.anaModel=anaModel;
		this.selectExCell=selectExCell;
		
		initUI(oldCrossSet);
		
		addListener();
	}
	
	
	private void initUI(AnaCrossTableSet oldCrossSet){
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getCrossAreaSetPanel(oldCrossSet), java.awt.BorderLayout.CENTER);
		getContentPane().add(getCmdPanel(), java.awt.BorderLayout.SOUTH);
		setResizable(false);
		pack();
	}
	
	 public void addListener() {
			if (m_selectListener == null) {
				m_selectListener = new SelectListener() {
					public void selectedChanged(SelectEvent e) {
						AreaPosition area = anaModel.getFormatModel().getSelectModel().getSelectedArea();
						if(isEditArea){
							getCrossAreaText().setText(area.toString());
						}else{
							getCrossPointText().setText(area.getStart().toString());
						}
						

					}
				};
			}
			anaModel.getFormatModel().getSelectModel().addSelectModelListener(
					m_selectListener);
		}
	 
	 private EditFocusAdapter getEditFocusListener(){
		 if(m_editFocusAdapter==null){
			 m_editFocusAdapter=new EditFocusAdapter();
		 }
		 return m_editFocusAdapter;
	 }
	 
	 public void removeListener(){
		 if (m_selectListener != null) { 
			 anaModel.getFormatModel().getSelectModel().removeSelectModelListener(m_selectListener);
		 }
	 }
	private JPanel getCmdPanel(){
		   if(jCmdPanel==null){
			   jCmdPanel=new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			   btnOK=createOkButton();
			   jCmdPanel.add(btnOK);
			   btnCancel=createCancleButton();
			   jCmdPanel.add(btnCancel);
		   }
		   return jCmdPanel;
	   }
	
	@Override
	protected JButton createCancleButton() {
		JButton cancleButton = new nc.ui.pub.beans.UIButton();
		cancleButton.setText(MultiLang.getString("cancel"));
		cancleButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setResult(ID_CANCEL);
				removeListener();
				close();
			}
		});
		return cancleButton;
	}

	@Override
	protected JButton createOkButton() {
		JButton okButton = new nc.ui.pub.beans.UIButton();
		okButton.setText(MultiLang.getString("ok"));
		okButton.addActionListener(new java.awt.event.ActionListener() {
			/**
			 * @i18n miufo00337=交叉点设置出错
			 */
			public void actionPerformed(java.awt.event.ActionEvent e) {
				AreaPosition crossArea=getSettingCrossArea();
				CellPosition crossPoint=getSettingCrossPoint();
				if(crossArea==null||crossPoint==null){
					return;
				}
				
				if (!crossArea.contain(crossPoint)) {
					UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo00337"), parent);
				} else {
						AreaDataModel areaModel=(AreaDataModel) selectExCell.getModel();
						if (areaModel != null && areaModel.getDSTool() != null) {
						if (!areaModel.getDSTool().isSupport(
								DescriptorType.AggrDescriptor)) {// 对于不支持汇总的数据集，此功能不可用
							UfoPublic.sendWarningMessage(StringResource
									.getStringResource("miufo00198"), parent);
							return;
						}
					}
						crossset=collectCrossTableSet(crossArea,crossPoint,anaModel.getFormatModel(),areaModel);
						if(areaModel==null){
							areaModel =new AreaDataModel(selectExCell.getExAreaPK(), anaModel);
						}
						   
						    areaModel.setCrossInfo(crossset);
						    selectExCell.setModel(areaModel);
						    selectExCell.setExAreaType(ExAreaCell.EX_TYPE_SAMPLE);
							if(!anaModel.getFormatModel().isDirty()){
								anaModel.getFormatModel().setDirty(true);
							}
							setResult(ID_OK);
							removeListener();
							close();
							parent.getTable().getCells().repaint(selectExCell.getArea(), true);
						
					}
			}
				
		});
		return okButton;
	}
	/**
	 * @i18n miufo00338=交叉区域:
	 * @i18n miufo00339=交叉点:
	 */
	private JPanel getCrossAreaSetPanel(AnaCrossTableSet oldCrossSet){
		if(jAreaPanel==null){
			jAreaPanel=new UIPanel();
			jAreaPanel.setLayout(new com.ufida.zior.comp.layout.SimpleGridLayout(2,1,6,2));
			
	        jAreaPanel.add(getTopPanel());
	        jAreaPanel.add(getBottomPanel());
	        
	        if(oldCrossSet!=null){
	        	getCrossAreaText().setText(oldCrossSet.getCrossArea().toString());
	        	getCrossPointText().setText(oldCrossSet.getCrossPoint().toString());
	        	getRowHeaderBox().setSelected(oldCrossSet.isShowRowHeader());
				getColHeaderBox().setSelected(oldCrossSet.isShowColHeader());
	        }else{
	        	AreaPosition area = anaModel.getFormatModel().getSelectModel().getSelectedArea();
				if(area!=null){
					getCrossAreaText().setText(area.toString());
				}
				getCrossPointText().requestFocus();
				isEditArea=false;
	        	getRowHeaderBox().setSelected(true);
				getColHeaderBox().setSelected(true);
	        }
	        
		}
		return jAreaPanel;
	}
	
	private JPanel getTopPanel(){
		if(topPanel==null){
			topPanel=new UIPanel(new com.ufida.zior.comp.layout.SimpleGridLayout(2,3,2,6));
	        JLabel areaName=new UILabel(StringResource.getStringResource("miufo00338"));
	        topPanel.add(areaName);
	        topPanel.add(getCrossAreaText());
	        topPanel.add(getBtnFoldArea());
	        getBtnFoldArea().addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					isEditArea = true;
					if (getBtnFoldArea().getIcon() == getUPIcon()) {
						getBtnFoldArea().setIcon(getDownIcon());
						jAreaPanel.remove(getBottomPanel());
						pack();
					} else if (getBtnFoldArea().getIcon() == getDownIcon()) {
						getBtnFoldArea().setIcon(getUPIcon());
						if (jAreaPanel.getComponentCount() < 2) {
							jAreaPanel.add(getBottomPanel());
							pack();
						}
					}
				}
	        	
	        });
	        JLabel areaPointName=new UILabel(StringResource.getStringResource("miufo00339"));
	        topPanel.add(areaPointName);
	        topPanel.add(getCrossPointText());
	        topPanel.add(getBtnFoldPoint());
	        getBtnFoldPoint().addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					isEditArea = false;
					if (getBtnFoldPoint().getIcon() == getUPIcon()) {
						getBtnFoldPoint().setIcon(getDownIcon());
						jAreaPanel.remove(getBottomPanel());
						pack();
					} else if (getBtnFoldPoint().getIcon() == getDownIcon()) {
						getBtnFoldPoint().setIcon(getUPIcon());
						if (jAreaPanel.getComponentCount() < 2) {
							jAreaPanel.add(getBottomPanel());
							pack();
						}
					}
				}
	        	
	        });
		}
		return topPanel;
	}
	private JPanel getBottomPanel(){
		if(bottomPanel==null){
			    bottomPanel=new UIPanel(new com.ufida.zior.comp.layout.SimpleGridLayout(2,1,6,2));
		        bottomPanel.add(getRowHeaderBox());
		        bottomPanel.add(getColHeaderBox());
		}
		return bottomPanel;
	}
	private JTextField getCrossPointText(){
		if(jCrossPointField==null){
			jCrossPointField=new UITextField();
			jCrossPointField.setPreferredSize(new Dimension(150,20));
			jCrossPointField.addFocusListener(getEditFocusListener());
		}
		return jCrossPointField;
	}
	private JButton getBtnFoldPoint() {
		if (btnPointFoldUp == null) {
				btnPointFoldUp = new JButton();
				btnPointFoldUp.setSize(20, 20);
				btnPointFoldUp.setName("JBAreaFoldArea");
				btnPointFoldUp.setIcon(getUPIcon());
		}
		return btnPointFoldUp;
	}
	private ImageIcon getUPIcon(){
		if(upIcon==null){
			upIcon=ResConst.getImageIcon("reportcore/up.gif");
		}
		return upIcon;
	}
	private ImageIcon getDownIcon(){
		if(downIcon==null){
			downIcon=ResConst.getImageIcon("reportcore/down.gif");
		}
		return downIcon;
	}
	private JTextField getCrossAreaText(){
		if(jCrossAreaField==null){
			jCrossAreaField=new UITextField();
			jCrossAreaField.setPreferredSize(new Dimension(150,20));
			jCrossAreaField.addFocusListener(getEditFocusListener());
		}
		return jCrossAreaField;
	}
	private JButton getBtnFoldArea() {
		if (btnAreaFoldUp == null) {
				btnAreaFoldUp = new JButton();
				btnAreaFoldUp.setSize(20, 20);
				btnAreaFoldUp.setName("JBAreaFoldArea");
				btnAreaFoldUp.setIcon(getUPIcon());
		}
		return btnAreaFoldUp;
	}
	/**
	 * @i18n miufo00340=显示行标题
	 */
	private JCheckBox getRowHeaderBox(){
		if (jShowRowHeader == null) {
			jShowRowHeader = new JCheckBox(StringResource.getStringResource("miufo00340"));
		}
		return jShowRowHeader;
	}
	
	/**
	 * @i18n miufo00341=显示列标题
	 */
	private JCheckBox getColHeaderBox(){
		if (jShowColHeader == null) {
			jShowColHeader = new JCheckBox(StringResource.getStringResource("miufo00341"));
		}
		return jShowColHeader;
	}

	/**
	 * 从CellsModel收集信息，构建交叉区域的交叉设置信息
	 * @param model
	 * @param areaModel
	 * @return
	 */
	private AnaCrossTableSet collectCrossTableSet(AreaPosition crossArea,CellPosition crossPoint,CellsModel model,AreaDataModel areaModel) {
		
		AnaCrossTableSet cross = new AnaCrossTableSet(crossArea,crossPoint,model,areaModel==null?null:areaModel.getDSTool());
		cross.setShowRowHeader(this.getRowHeaderBox().isSelected());
		cross.setShowColHeader(this.getColHeaderBox().isSelected());
		if(areaModel!=null){
			cross.getCrossSet();
		}
		return cross;
	}

	private AreaPosition getSettingCrossArea() {
		//判断是否正确
		String strPos =getCrossAreaText().getText();

		if (strPos == null || strPos.equals("")) {
			UfoPublic.sendErrorMessage(StringResource.getStringResource("miufo1000787"), this, null); //"区域不能输入为空！"
			getCrossAreaText().requestFocus();
			return null;
		}
		AreaPosition area = null;
		try {
			area = AreaPosition.getInstance(strPos);
		} catch (Throwable ex) {
			UfoPublic.sendErrorMessage(StringResource.getStringResource("miufo1001147"), this, null); //"区域名称不合法！"
			getCrossAreaText().requestFocus();
			return null;
		}
		area=this.selectExCell.getArea().interArea(area);
		if(area==null){
			UfoPublic.sendErrorMessage(StringResource.getStringResource("miufo1001147"), this, null); //"区域名称不合法！"
			getCrossAreaText().requestFocus();
			return null;
		}
		
		
		return area;
	}

	private CellPosition getSettingCrossPoint() {
		//判断是否正确
		String strPos =getCrossPointText().getText();

		if (strPos == null || strPos.equals("")) {
			UfoPublic.sendErrorMessage(StringResource.getStringResource("miufo1000787"), this, null); //"区域不能输入为空！"
			getCrossPointText().requestFocus();
			return null;
		}
		AreaPosition area = null;
		try {
			area = AreaPosition.getInstance(strPos);
		} catch (Throwable ex) {
			UfoPublic.sendErrorMessage(StringResource.getStringResource("miufo1001147"), this, null); //"区域名称不合法！"
			getCrossPointText().requestFocus();
			return null;
		}
		
		return area.getStart();
	}

	 private class EditFocusAdapter extends FocusAdapter {
		/**
		 * Invoked when a component loses the keyboard focus.
		 */
		public void focusLost(FocusEvent e) {
            if(e.getSource()==getCrossAreaText()){
            	isEditArea=true;
            }else if(e.getSource()==getCrossPointText()){
            	isEditArea=false;
            }
            
		}

		@Override
		public void focusGained(FocusEvent e) {

		}

	}
	
	
}
 