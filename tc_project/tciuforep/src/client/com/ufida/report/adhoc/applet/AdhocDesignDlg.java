/*
 * Created by caijie  on  2005-12-31
 *   
 */
package com.ufida.report.adhoc.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.vo.bi.query.manager.MetaDataVO;

import com.ufida.report.adhoc.model.AdhocCrossTableSet;
import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.adhoc.model.AdhocPageDimField;
import com.ufida.report.adhoc.model.IAdhocAnalyzer;
import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.adhoc.model.SortAnalyzer;
import com.ufida.report.rep.applet.BINavigationPanel;
import com.ufida.report.rep.model.DefaultReportField;
import com.ufida.report.rep.model.IBIField;
import com.ufida.report.rep.model.SortDescriptor;
import com.ufida.report.rep.model.SortVO;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.fmtplugin.freequery.UniqueList;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.dnd.DndHandler;

/**
 * Adhoc设计向导
 * 
 * @author caijie
 */
public class AdhocDesignDlg extends UfoDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JButton btnOK = null;

	private JButton jBtnCancel = null;

	private JSplitPane jSplitPane = null;

	private JScrollPane infoTreeSPanel = null;

	private JScrollPane jScrollPane1 = null;

	private nc.ui.pub.beans.UITabbedPane tabbedPane = null;

	private BINavigationPanel infoNavigatePanel = null;

	private SelectFieldPanel selectFieldPanel = null;

//	private SortPanel sortPanel = null;
//
//	private SetGroupPanel setGroupPanel = null;

	private SortGroupPanel sortGroupPanel = null;
	private AdhocModel m_adhocModel = null;

	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public AdhocDesignDlg(Container parent, String title, AdhocModel adhocModel, IBIField[] flds)
			throws HeadlessException {
		super(parent, title);
		m_adhocModel = adhocModel;
		initialize();
		initValues(adhocModel, flds);
	}

	private void initValues(AdhocModel adhocModel, IBIField[] flds) {
		// MetaDataVO[] flds =
		// QueryModelSrv.getSelectFlds(adhocModel.getQueryModel().getID());
		// IMetaData[] flds = queryDef.getBIDataSet(null).getMetadatas();
		getInfoNavigatePanel().addFieldNode(adhocModel.getDataCenter().getCurrQuery().getQueryModel(), flds);

		//各种字段选择
		setSelectedFields(IBIField.BI__PAGE_DIM_FIELD, adhocModel.getFields(IBIField.BI__PAGE_DIM_FIELD));
		setSelectedFields(IBIField.BI_REPORT_FIELD, adhocModel.getFields(IBIField.BI_REPORT_FIELD));
		setSelectedFields(IBIField.ADHOC_GROUP_FIELD, adhocModel.getFields(IBIField.ADHOC_GROUP_FIELD));

		getSelectFieldPanel().setCrossTableInfo(adhocModel,adhocModel.getDataCenter().isCross(), adhocModel.getDataCenter().getCrossTableInfo());
		
		// 排序对象
		SortVO sortVO = null;
		IAdhocAnalyzer[] anas = getAdhocModel().getDataCenter().getAnalyzerModel().getAnalyzerByType(
				IAdhocAnalyzer.TYPE_SORT);
		if (anas.length > 0) {
			sortVO = ((SortAnalyzer) anas[0]).getSortVO();
		}
		if (sortVO == null || sortVO.getSortDescriptorList() == null || sortVO.getSortDescriptorList().isEmpty()) {

		} else {
			sortVO = (SortVO) sortVO.clone();
			DefaultListModel model = (DefaultListModel) getSortGroupPanel().getSortList().getModel();
			ArrayList list = sortVO.getSortDescriptorList();
			for (int i = 0; i < list.size(); i++) {
				model.addElement(list.get(i));
			}
		}
	}

	public SortDescriptor[] getSortDescriptors() {
		DefaultListModel model = null;
		model = (DefaultListModel) getSortGroupPanel().getSortList().getModel();
		model.trimToSize();
		Object[] objs = model.toArray();
		if (objs == null)
			return null;
		;
		SortDescriptor[] fields = new SortDescriptor[objs.length];
		for (int i = 0; i < objs.length; i++) {
			fields[i] = (SortDescriptor) objs[i];
		}
		return fields;
	}

	private AdhocModel getAdhocModel() {
		return m_adhocModel;
	}

	private void setSelectedFields(int fieldType, IMetaData[] fields) {
		DefaultListModel model = null;
		if (fieldType == IBIField.BI_REPORT_FIELD) {
			model = (DefaultListModel) getSelectFieldPanel().getReportFieldList().getModel();
		}
		if (fieldType == IBIField.BI__PAGE_DIM_FIELD) {
			model = (DefaultListModel) getSelectFieldPanel().getPageFieldList().getModel();
		}
		if (fieldType == IBIField.ADHOC_GROUP_FIELD) {
			model = (DefaultListModel) getSortGroupPanel().getGroupFieldList().getModel();
		}
		for (int i = 0; i < fields.length; i++) {
			model.addElement(fields[i]);
		}
	}

	public IMetaData[] getSelectedFields(int fieldType) {
		DefaultListModel model = null;
		if (fieldType == IBIField.BI_REPORT_FIELD) {
			model = (DefaultListModel) getSelectFieldPanel().getReportFieldList().getModel();
		}
		if (fieldType == IBIField.BI__PAGE_DIM_FIELD) {
			model = (DefaultListModel) getSelectFieldPanel().getPageFieldList().getModel();
		}
		if (fieldType == IBIField.ADHOC_GROUP_FIELD) {
			model = (DefaultListModel) getSortGroupPanel().getGroupFieldList().getModel();
		}
		model.trimToSize();
		Object[] objs = model.toArray();
		if (objs == null)
			return new IMetaData[0];
		IMetaData[] fields = new IMetaData[objs.length];
		for (int i = 0; i < objs.length; i++) {
			fields[i] = (IMetaData) objs[i];
		}
		return fields;
	}
	public boolean isCrossTable(){
		return getSelectFieldPanel().isCrossTable();
	}
	public AdhocCrossTableSet getCrossTableSet(){
		/*
		//暂时由报表字段生成一下
		IMetaData[] repFlds = getSelectedFields(IBIField.BI_REPORT_FIELD);
		ArrayList<IMetaData> al_colDims = new ArrayList<IMetaData>();
		ArrayList<DefaultReportField> al_measures = new ArrayList<DefaultReportField>();
		for(IMetaData fld:repFlds){
			if(fld.isMeasure()){
				if(fld instanceof DefaultReportField){
					((DefaultReportField)fld).setCountType(al_measures.size()%5);
					al_measures.add((DefaultReportField)fld);
				}
			}else
				al_colDims.add(fld);
		}
		AdhocCrossTableSet crossInfo = new AdhocCrossTableSet();
		crossInfo.setRowFlds(new IMetaData[]{al_colDims.get(0)});
		al_colDims.remove(0);
		crossInfo.setColFlds(al_colDims.toArray(new IMetaData[0]));
		crossInfo.setCountFlds(al_measures.toArray(new DefaultReportField[0]));
		
		return crossInfo;
		*/
		return getSelectFieldPanel().getCrossTableInfo();
	}

	// 页纬度
	public void setSelectedPageDimFields(IBIField[] fields) {
		for (int i = 0; i < fields.length; i++) {
			DefaultListModel model = (DefaultListModel) getSelectFieldPanel().getPageFieldList().getModel();
			model.addElement(fields[i]);
		}
	}

	public IBIField[] getSelectedPageDimFields() {
		DefaultListModel model = (DefaultListModel) getSelectFieldPanel().getPageFieldList().getModel();
		IBIField[] fields = (IBIField[]) model.toArray();
		return fields;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(850, 600);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);

			Dimension size = new Dimension(75, 20);
			getBtnOK().setSize(size);
			getBtnOK().setPreferredSize(size);
			getBtnCancel().setSize(size);
			getBtnCancel().setPreferredSize(size);
			JPanel btnPane = new UIPanel();
			btnPane.add(getBtnOK());
			btnPane.add(getBtnCancel());
			jContentPane.add(btnPane, BorderLayout.SOUTH);

		}
		return jContentPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new nc.ui.pub.beans.UIButton();
			btnOK.setText(StringResource.getStringResource("mbiadhoc00021"));
			btnOK.addActionListener(new ActionListener() {
				/**
				 * @i18n miufo00322=请选择统计字段
				 */
				public void actionPerformed(ActionEvent e) {
					if(isCrossTable() && getCrossTableSet().getMeasureFlds() == null){
						JOptionPane.showMessageDialog(AdhocDesignDlg.this, StringResource.getStringResource("miufo00322"));
					}else{
					AdhocDesignDlg.this.setResult(UfoDialog.ID_OK);
					AdhocDesignDlg.this.close();
					}
				}
			});
		}
		return btnOK;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCancel() {
		if (jBtnCancel == null) {
			jBtnCancel = new nc.ui.pub.beans.UIButton();
			jBtnCancel.setText(StringResource.getStringResource("mbiadhoc00022"));
			jBtnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AdhocDesignDlg.this.setResult(UfoDialog.ID_CANCEL);
					AdhocDesignDlg.this.close();
				}
			});
		}
		return jBtnCancel;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new UISplitPane();
			jSplitPane.setBounds(11, 8, 469, 317);
//			jSplitPane.setLeftComponent(getInfoTreeSPanel());
			jSplitPane.setLeftComponent(getInfoNavigatePanel());
			jSplitPane.setRightComponent(getJScrollPane1());
			jSplitPane.setDividerLocation(180);
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getInfoTreeSPanel() {
		if (infoTreeSPanel == null) {
			infoTreeSPanel = new UIScrollPane();
			infoTreeSPanel.setViewportView(getInfoNavigatePanel());
		}
		return infoTreeSPanel;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setViewportView(getTabbedPane());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return nc.ui.pub.beans.UITabbedPane
	 */
	private nc.ui.pub.beans.UITabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new nc.ui.pub.beans.UITabbedPane();
			tabbedPane.add(StringResource.getStringResource("mbiadhoc00025"), getSelectFieldPanel());
			tabbedPane.add(StringResource.getStringResource("mbiadhoc00026")+StringResource.getStringResource("mbiadhoc00027"), getSortGroupPanel());

		}
		return tabbedPane;
	}

	private SortGroupPanel getSortGroupPanel() {
		if (sortGroupPanel == null) {
			sortGroupPanel = new SortGroupPanel();
//			AdhocDesignFieldDnD dnd = new AdhocDesignFieldDnD(sortPanel.getSortList());
			UniqueList list = sortGroupPanel.getSortList();
			DndHandler.enableDndDrag(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL), DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL));
			list = sortGroupPanel.getGroupFieldList();
			DndHandler.enableDndDrag(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL), DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL));
		}
		return sortGroupPanel;
	}

	private SelectFieldPanel getSelectFieldPanel() {
		if (selectFieldPanel == null) {
			selectFieldPanel = new SelectFieldPanel();
//			AdhocDesignFieldDnD dnd = new AdhocDesignFieldDnD(selectFieldPanel.getReportFieldList());
			UniqueList list = selectFieldPanel.getReportFieldList();
			DndHandler.enableDndDrag(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL), DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL));
			
			UniqueList crossList = selectFieldPanel.getCrossFieldList(SetCrossPanel.MENU_TYPE_COL);
			DndHandler.enableDndDrag(crossList, new SelCrossInfoAdapter(crossList, SelCrossInfoAdapter.TYPE_ONLY_CHARACTER), DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(crossList, new SelCrossInfoAdapter(crossList, SelCrossInfoAdapter.TYPE_ONLY_CHARACTER));

			crossList = selectFieldPanel.getCrossFieldList(SetCrossPanel.MENU_TYPE_ROW);
			DndHandler.enableDndDrag(crossList, new SelCrossInfoAdapter(crossList, SelCrossInfoAdapter.TYPE_ONLY_CHARACTER), DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(crossList, new SelCrossInfoAdapter(crossList, SelCrossInfoAdapter.TYPE_ONLY_CHARACTER));
			
			crossList = selectFieldPanel.getCrossFieldList(SetCrossPanel.MENU_TYPE_MEASURE);
			DndHandler.enableDndDrag(crossList, new SelCrossInfoAdapter(crossList, SelCrossInfoAdapter.TYPE_ONLY_NUMERIC), DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(crossList, new SelCrossInfoAdapter(crossList, SelCrossInfoAdapter.TYPE_ONLY_NUMERIC));
			
			DefaultListModel model = new DefaultListModel();
			selectFieldPanel.getPageFieldList().setModel(model);
			model.addListDataListener(new ListDataListener() {
				public void contentsChanged(ListDataEvent e) {
				}

				// 将对象自动转化为页纬度
				public void intervalAdded(ListDataEvent e) {
					DefaultListModel listModel = (DefaultListModel) selectFieldPanel.getPageFieldList().getModel();
					Object obj = listModel.getElementAt(e.getIndex0());
					if (obj instanceof PageDimField)
						return;
					if(obj instanceof DefaultReportField){
						String queryID = ((DefaultReportField)obj).getQueryID();
						PageDimField fld = new AdhocPageDimField(getAdhocModel().getDataCenter().getCurrQuery(), (IMetaData)obj);
						listModel.setElementAt(fld, e.getIndex0());
					}
					else if (obj instanceof MetaDataVO) {
						PageDimField fld = new AdhocPageDimField(getAdhocModel().getDataCenter().getCurrQuery().getQueryID(), (MetaDataVO) obj);
						listModel.setElementAt(fld, e.getIndex0());
					} else {
						JOptionPane.showMessageDialog(AdhocDesignDlg.this, StringResource
								.getStringResource("mbiadhoc00028"), StringResource.getStringResource("mbiadhoc00029"),
								JOptionPane.ERROR_MESSAGE);
					}
				}

				public void intervalRemoved(ListDataEvent e) {
				}
			});

//			dnd = new AdhocDesignFieldDnD(selectFieldPanel.getPageFieldList());
			list = selectFieldPanel.getPageFieldList();
			DndHandler.enableDndDrag(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL), DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL));
		
		}
		return selectFieldPanel;
	}

//	private SetGroupPanel getSetGroupPanel() {
//		if (setGroupPanel == null) {
//			setGroupPanel = new SetGroupPanel();
////			AdhocDesignFieldDnD dnd = new AdhocDesignFieldDnD(setGroupPanel.getGroupFieldList());
//			UniqueList list = setGroupPanel.getGroupFieldList();
//			DndHandler.enableDndDrag(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL), DnDConstants.ACTION_MOVE);
//			DndHandler.enableDndDrop(list, new SelCrossInfoAdapter(list, SelCrossInfoAdapter.TYPE_ALL));
//		}
//		return setGroupPanel;
//	}

	/**
	 * This method initializes infoNavigatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private BINavigationPanel getInfoNavigatePanel() {
		if (infoNavigatePanel == null) {
			infoNavigatePanel = new BINavigationPanel(){
				public void applyQueryModelDragSource() {
				}
			};
			JTree queryTree = infoNavigatePanel.getQueryModelTree();
			DndHandler.enableDndDrag(queryTree, new SelCrossInfoAdapter(queryTree), DnDConstants.ACTION_COPY);
			DndHandler.enableDndDrop(queryTree, new SelCrossInfoAdapter(queryTree));

		}
		return infoNavigatePanel;
	}

	// +
} // @jve:decl-index=0:visual-constraint="10,10"
 