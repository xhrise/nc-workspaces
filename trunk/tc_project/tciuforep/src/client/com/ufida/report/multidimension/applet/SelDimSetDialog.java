/*
 * Created on 2005-6-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.bi.base.BIException;
import com.ufida.report.adhoc.applet.SelectQueryModelDlg;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * @author ll 数据导航设置对话框
 */
public class SelDimSetDialog extends UIDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel ivjUIDialogContentPane = null;

	//维度／成员选择模型
	private SelDimModel m_selModel = null;

	private String m_userID = null;

	//界面控件
	private SelDimSetPanel m_selDimPanel = null;

	private UILabel m_queryLabel = null;

	private UIButton m_btn_LastStep = null;

	private UIButton m_btn_OK = null;

	private UIButton m_btn_Cancel = null;

	public SelDimSetDialog(Container cont) {
		super(cont);
		setContentPane(getUIDialogContentPane());
	}

	public SelDimSetDialog(Container cont, boolean isShowStep) {
		super(cont);
		setContentPane(getUIDialogContentPane());
		setShowStep(isShowStep);
	}

	public SelDimModel getSelModel() {
		return m_selModel;
	}

	public void setSelModel(SelDimModel selModel, String userID) {
		try{
			m_selModel = (SelDimModel) selModel.clone();
		}catch(BIException ex){
			m_selModel = new SelDimModel(selModel.getMultiDimensionModel());
		}
		m_userID = userID;

		if (m_selModel != null) {//界面加载数据
			updateQueryInfo();
		}
	}

	private void updateQueryInfo() {
		String queryName = null;
		if (m_selModel.getQueryModel() != null)
			queryName = m_selModel.getQueryModel().getName();
		getQueryLabel().setText(queryName);

		//显示维度选择
		getSelDimPanel().setSelModel(m_selModel, m_userID);

	}

	public String getTitle() {
		return StringResource.getStringResource("ubimultidim0001");
	}

	/**
	 * 返回 UIDialogContentPane 特性值.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告:此方法将重新生成. */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new UIPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());

			ivjUIDialogContentPane.add(initPanel(),
					java.awt.BorderLayout.CENTER);

			setSize(500, 500);
		}
		return ivjUIDialogContentPane;
	}

	private UIPanel initPanel() {
		UIPanel mainPanel = new UIPanel();
		mainPanel.setLayout(new BorderLayout());

		//查询显示区域
		UIPanel queryPanel = new UIPanel();
		queryPanel.setLayout(null);

		UILabel label1 = new UILabel(StringResource.getStringResource("ubimultidim0002"));
		label1.setBounds(30, 10, 70, 20);
		queryPanel.add(label1);

		getQueryLabel().setBounds(120, 10, 200, 20);
		queryPanel.add(getQueryLabel());
		queryPanel.setPreferredSize(new Dimension(100, 30));

		mainPanel.add(queryPanel, BorderLayout.NORTH);
		//维度选择区域
		mainPanel.add(getSelDimPanel(), BorderLayout.CENTER);

		//按钮区域
		UIPanel buttonPanel = new UIPanel();
		buttonPanel.add(getBtnLastStep());
		buttonPanel.add(new UILabel());
		buttonPanel.add(getBtnOK());
		buttonPanel.add(new UILabel());
		buttonPanel.add(getBtnCancel());

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		return mainPanel;
	}

	private UILabel getQueryLabel() {
		if (m_queryLabel == null) {
			m_queryLabel = new UILabel();
		}
		return m_queryLabel;
	}

	/**
	 * 维度设置面板
	 * 
	 * @return
	 */
	private SelDimSetPanel getSelDimPanel() {
		if (m_selDimPanel == null) {
			m_selDimPanel = new SelDimSetPanel();
		}
		return m_selDimPanel;
	}

	public UIButton getBtnLastStep() {
		if (m_btn_LastStep == null) {
			m_btn_LastStep = new UIButton(StringResource.getStringResource("miufopublic260"));
			m_btn_LastStep.addActionListener(this);
		}
		return m_btn_LastStep;
	}

	private UIButton getBtnOK() {
		if (m_btn_OK == null) {
			m_btn_OK = new UIButton(StringResource.getStringResource("miufo1000094"));
			m_btn_OK.addActionListener(this);
		}
		return m_btn_OK;
	}

	private UIButton getBtnCancel() {
		if (m_btn_Cancel == null) {
			m_btn_Cancel = new UIButton(StringResource.getStringResource("miufo1000274"));
			m_btn_Cancel.addActionListener(this);
		}
		return m_btn_Cancel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			//从界面上取数
			m_selModel = getSelDimPanel().getSelModel();
			String msg = getSelDimPanel().validateDim();
			if (msg != null) {
				MessageDialog.showErrorDlg(this, null, msg);
				return;
			}
			closeOK();

		}
		if (e.getSource() == getBtnCancel()) {
			closeCancel();
		}
		if (e.getSource() == getBtnLastStep()) {
			SelectQuery();
		}
	}

	public int showModal() {
		if (m_selModel.getQueryModel() == null) {
			if (!SelectQuery())
				return UIDialog.ID_CANCEL;
		}
		return super.showModal();
	}

	private boolean SelectQuery() {
		//选择查询，构建初始化模型
		SelectQueryModelDlg dlg = new SelectQueryModelDlg(this, m_userID);
		QueryModelVO queryModelVO = null;
		if (m_selModel.getQueryModel() != null) {
			queryModelVO = m_selModel.getQueryModel();
//TODO			dlg.setSelectedQueryModel(queryModelVO);
		}

		dlg.show();
		if ((dlg.getResult() != UfoDialog.ID_OK)
				|| (dlg.getQueryModel() == null))
			return false;

		queryModelVO = dlg.getQueryModel();

		if (QueryModelSrv.getSelectFlds(queryModelVO.getPrimaryKey()) != null) {
			m_selModel.setQueryModel(queryModelVO);
			m_selModel.clearDimInfo();
			try{
			updateQueryInfo();
			}catch(BIException ex){
				MessageDialog.showErrorDlg(this, null, ex.getMessage());
				return false;
			}
		}
		return true;
	}

	public void setShowStep(boolean isShowStep) {
		getBtnLastStep().setVisible(isShowStep);
	}
}