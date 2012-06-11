package com.ufida.report.spreedsheet.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.integration.dimension.DimMemberVO;

import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.rep.applet.PageDimItem;
import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufida.report.spreedsheet.model.SpreadQueryCache;
import com.ufida.report.spreedsheet.model.SpreadSheetModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.sysplugin.cellattr.CellPropertyDialog;
import com.ufsoft.table.format.Format;

public class SpreadCellPropertyDlg extends CellPropertyDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel m_dimPanel = null;

	private SpreadCellPropertyVO m_oldCell = null;

	private ArrayList al_fields = new ArrayList();

	public SpreadCellPropertyDlg(Container owner, Format format, boolean bTypeLocked, SpreadCellPropertyVO cellVO,
			SpreadQueryCache queryCache, SpreadSheetModel spreadModel) {
		super(owner, format, bTypeLocked);
		initPanels();
		// if (cellVO != null)
		m_oldCell = cellVO;
		setCellDimProperty(cellVO, queryCache, spreadModel.getPageDimFields());
	}

	private void initPanels() {

		getTabbedPane().remove(0);
		addPropertyPanel();
	}

	private void addPropertyPanel() {
		getTabbedPane().add(StringResource.getStringResource("ubispreadsheet0011"), getDimPropertyPanel());
	}

	private JPanel getDimPropertyPanel() {
		if (m_dimPanel == null) {
			m_dimPanel = new UIPanel();
		}
		return m_dimPanel;
	}

	public void setCellDimProperty(SpreadCellPropertyVO cellVO, SpreadQueryCache queryCache, PageDimField[] pageDims) {
		getDimPropertyPanel().removeAll();
		al_fields = createDimRef(getDimPropertyPanel(), cellVO, queryCache, pageDims, true);
		if (getDimPropertyPanel().getComponentCount() == 0) {
			if (getDimPropertyPanel().getParent() != null)
				getDimPropertyPanel().getParent().remove(getDimPropertyPanel());
		} else {
			if (getDimPropertyPanel().getParent() == null) {
				addPropertyPanel();
			}
		}
	}

	public Object getPropertyExtended() {
		if ((getDimPropertyPanel().getComponentCount() == 0) || (getDimPropertyPanel().getParent() == null))
			return null;

		if (m_oldCell != null) {
			if (m_oldCell.getMembers().length != al_fields.size())
				return m_oldCell;// 不应该出现此情况
			IMember[] mems = m_oldCell.getMembers();
			for (int i = 0; i < mems.length; i++) {
				PageDimField field = (PageDimField) al_fields.get(i);
				IMember selMem = field.getSelectedValue();
				mems[i] = selMem;
			}
			m_oldCell.setMembers(mems);
			return m_oldCell;
		}
		return null;
	}

	static ArrayList createDimRef(JPanel panel, SpreadCellPropertyVO cellVO, SpreadQueryCache queryCache,
			PageDimField[] pageDims, boolean isShowPageDims) {
		if (cellVO == null)
			return null;
		String queryID = cellVO.getQueryID();
		if (queryID == null)
			return null;
		panel.removeAll();

		String[] allDims = queryCache.getDimPKs(queryID);
		IMember[] mems = cellVO.getMembers();
		// PageDimNavigationPanel dimPanel = new PageDimNavigationPanel();
		JPanel dimPanel = new UIPanel();
		dimPanel.setSize(300, 400);
		dimPanel.setLayout(new FlowLayout());
		ArrayList<PageDimField> al_allFields = new ArrayList<PageDimField>();

		for (int i = 0; i < allDims.length; i++) {
			boolean isMeasure = SpreadQueryCache.isMeasure(allDims[i]);
			DimMemberVO rootMem = null;
			if (!isMeasure) {
				rootMem = (DimMemberVO) queryCache.getRootMember(allDims[i]);
			}
			IMember dimMem = null;
			for (int j = 0; j < mems.length; j++) {
				if (mems[j].getDimID().equals(allDims[i])) {
					dimMem = mems[j];
					break;
				}
			}
			if (dimMem == null && !isMeasure)// 对于未选择成员的，按照顶级成员来计
				dimMem = rootMem;
			PageDimField field = queryCache.getPageDimField(allDims[i], dimMem);
			// if (dimMem != null)
			// field.setSelectedValue(dimMem);

			boolean isPageDim = false;
			if (pageDims != null) {
				for (int j = 0; j < pageDims.length; j++) {
					if (pageDims[j].getName().equals(field.getName())) {
						isPageDim = true;
						break;
					}
				}
			}
			al_allFields.add(field);
			if (isShowPageDims || !(isMeasure || isPageDim)) {//对于不显示页维度的情况（插入指标时），不显示页维度和指标信息
				dimPanel.add(createFieldPanel(field, isMeasure, isPageDim));
			}
		}
		panel.setLayout(null);
		if(dimPanel.getComponentCount()>0)
			panel.add(dimPanel);

		return al_allFields;
	}

	private static JPanel createFieldPanel(PageDimField field, boolean isMeasure, boolean isPageDim) {
		JPanel fPanel = new UIPanel();
		fPanel.setSize(290, 30);
		fPanel.setLayout(new BorderLayout());
		PageDimItem item = new PageDimItem(field);

		// 直接设置组件的尺寸
		JLabel label = item.getLabel();
		label.setPreferredSize(new Dimension(100, PageDimItem.ITEM_HEIGHT));
		// label.setSize(new Dimension(100,PageDimItem.ITEM_HEIGHT));
		JTextField txtField = item.getTextFieldt();
		txtField.setPreferredSize(new Dimension(120, PageDimItem.ITEM_HEIGHT));
		fPanel.add(item, BorderLayout.CENTER);

		if (isMeasure || isPageDim) {
			txtField.setEditable(false);
		}
		return fPanel;
	}
}
