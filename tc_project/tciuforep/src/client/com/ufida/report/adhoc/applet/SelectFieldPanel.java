/*
 * Created on 2005-6-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.adhoc.applet;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.report.adhoc.model.AdhocCrossTableSet;
import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.adhoc.model.AdhocPageDimField;
import com.ufsoft.iufo.fmtplugin.freequery.UniqueList;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author caijie
 * 
 */
public class SelectFieldPanel extends UIPanel implements ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UniqueList pageFieldList = null;

	private JLabel jLabelPageFld = null;

	private JScrollPane pageFldScrollPane = null;

	private JPanel pageDimListOprjPanel = null;

	private JPanel detailFldPanel = null;

	private SetCrossPanel crossFldPanel = null;

	private SetFldListPanel fldListPanel = null;

	private JRadioButton rbtn_List = null;

	private JRadioButton rbtn_Cross = null;
	
	private AdhocModel m_adhocModel = null;

	/**
	 * This is the default constructor
	 */
	public SelectFieldPanel() {
		super();
		initialize();
	}
    
	public SelectFieldPanel(AdhocModel model) {
		this();
		m_adhocModel=model;
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */

	private void initialize() {
		setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(getRadioBtnPanel(), BorderLayout.NORTH);
		
		jLabelPageFld = new UILabel();
		jLabelPageFld.setText(StringResource.getStringResource("mbiadhoc00039"));
		JPanel pageListPanel = new JPanel();
		pageListPanel.setLayout(new BorderLayout());
		pageListPanel.add(jLabelPageFld, BorderLayout.NORTH);
		pageListPanel.add(getPageFldScrollPane1(), BorderLayout.CENTER);
		
		topPanel.add(pageListPanel, BorderLayout.CENTER);
//		topPanel.add(getPageDimListOprjPanel(), BorderLayout.EAST);

		this.add(topPanel, BorderLayout.NORTH);
		this.add(getDetailFldPanel(), BorderLayout.CENTER);
	}
	
	private AdhocModel getAdhocModel() {
		return m_adhocModel;
	}
	
	public void setAdhocModel(AdhocModel model) {
		m_adhocModel = model;
	}

	/**
	 * This method initializes jList1
	 * 
	 * @return javax.swing.JList
	 */
	public UniqueList getPageFieldList() {
		if (pageFieldList == null) {
			pageFieldList = new UniqueList(){
				
				@Override
				public int getObjIndex(Object obj) {
					DefaultListModel model = (DefaultListModel)getModel();
					for (int i = 0; i < model.size(); i++) {
						AdhocPageDimField pageFld = (AdhocPageDimField)model.get(i);
						if(pageFld.getField().equals(obj))
							return i;
						
					}
					return -1;
				}
			};
			pageFieldList.setModel(new DefaultListModel());
		}
		return pageFieldList;
	}

	/**
	 * This method initializes jList2
	 * 
	 * @return javax.swing.JList
	 */
	public UniqueList getReportFieldList() {
		return getfldListPanel().getReportFieldList();
	}
	public UniqueList getCrossFieldList(int pos) {
		switch (pos) {
		case SetCrossPanel.MENU_TYPE_COL:
			return getCrossPanel().getJListColDims();
		case SetCrossPanel.MENU_TYPE_ROW:
			return getCrossPanel().getJListRowDims();
		case SetCrossPanel.MENU_TYPE_MEASURE:
			return getCrossPanel().getJListMeasures();
		}
		return null;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getPageFldScrollPane1() {
		if (pageFldScrollPane == null) {
			pageFldScrollPane = new UIScrollPane();
			pageFldScrollPane.setViewportView(getPageFieldList());
		}
		return pageFldScrollPane;
	}

	/**
	 * This method initializes pageDimListOprjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPageDimListOprjPanel() {
		if (pageDimListOprjPanel == null) {
			pageDimListOprjPanel = new ListOperPanel(getPageFieldList());
		}
		return pageDimListOprjPanel;
	}

	private JPanel getDetailFldPanel() {
		if (detailFldPanel == null) {
			detailFldPanel = new UIPanel();
			detailFldPanel.setLayout(new BorderLayout());
			detailFldPanel.add(getfldListPanel());
		}
		return detailFldPanel;
	}



	private SetCrossPanel getCrossPanel() {
		if (crossFldPanel == null) {
			crossFldPanel = new SetCrossPanel();

		}
		return crossFldPanel;
	}

	private SetFldListPanel getfldListPanel() {
		if (fldListPanel == null) {
			fldListPanel = new SetFldListPanel();

		}
		return fldListPanel;
	}

	private UIPanel getRadioBtnPanel() {
		UIPanel panel = new UIPanel();
		panel.add(getRadioBtnList());
		panel.add(getRadioBtnCross());
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(getRadioBtnList());
		btnGroup.add(getRadioBtnCross());

		return panel;
	}

	private JRadioButton getRadioBtnList() {
		if (rbtn_List == null) {
			rbtn_List = new JRadioButton();
			rbtn_List.setText(StringResource.getStringResource("uifreequery0007"));// 列表样式
			rbtn_List.addChangeListener(this);
		}
		return rbtn_List;
	}

	private JRadioButton getRadioBtnCross() {
		if (rbtn_Cross == null) {
			rbtn_Cross = new JRadioButton();
			rbtn_Cross.setText(StringResource.getStringResource("uifreequery0008"));// 交叉样式
			rbtn_Cross.addChangeListener(this);
		}
		return rbtn_Cross;
	}
	private void changeDetailPanel(boolean isCross) {
//		if (getDetailFldPanel().getComponentCount() > 0)
//			getDetailFldPanel().removeAll();
		if (isCross){
			if(getDetailFldPanel().getComponent(0) == getCrossPanel())
				return;
			getDetailFldPanel().remove(getfldListPanel());
			getDetailFldPanel().add(getCrossPanel(), BorderLayout.CENTER);
		}
		else{
			if(getDetailFldPanel().getComponent(0) == getfldListPanel())
				return;
			getDetailFldPanel().remove(getCrossPanel());
			getDetailFldPanel().add(getfldListPanel(), BorderLayout.CENTER);
		}
		this.validate();
		this.repaint();
	}
	public boolean isCrossTable(){
		return getRadioBtnCross().isSelected();
	}
	public AdhocCrossTableSet getCrossTableInfo(){
		return getCrossPanel().getCrossTableInfo();
	}
	public void setCrossTableInfo(AdhocModel adhocModel,boolean isCross, AdhocCrossTableSet crossInfo){
		getRadioBtnList().setSelected(!isCross);
		getRadioBtnCross().setSelected(isCross);
		
		getCrossPanel().setAdhocModel(adhocModel);
		getCrossPanel().setCrossTableInfo(crossInfo);
	}

	public void stateChanged(ChangeEvent e) {
		if(e.getSource()== getRadioBtnList() || e.getSource() == getRadioBtnCross()){
			changeDetailPanel(isCrossTable());//TODO
		}
	}

} 
