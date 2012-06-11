package com.ufida.report.multidimension.applet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;

import com.ufida.report.multidimension.model.AnalyzerSet;
import com.ufida.report.multidimension.model.DrillThroughSet;
import com.ufida.report.multidimension.model.MultiDimemsionModel;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ReportCore中运行的applet。
 * 
 * @author zzl 2005-5-12
 */
public class Test_dialog extends JApplet implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String m_userID = "00000000000000000000";
	
	private String m_queryID = "00010010000000000001";

	/**
	 * @i18n ubimultidim0033=分析管理器
	 */
	private UIButton btn_Analyzer = new UIButton(StringResource.getStringResource("ubimultidim0033"));

	/**
	 * @i18n mbimulti00025=维度设置
	 */
	private UIButton btn_seldim = new UIButton(StringResource.getStringResource("mbimulti00025"));

	/**
	 * @i18n mbimulti00026=报表穿透
	 */
	private UIButton btn_drillSet = new UIButton(StringResource.getStringResource("mbimulti00026"));

	private MultiDimemsionModel m_model = null;

	private AnalyzerDialog m_analyzerDlg = null;

	private SelDimSetDialog m_seldimDlg = null;

	private DrillThroughSetDialog m_drillDlg = null;

	public void init() {
		initPanel();
		initData();
	}

	private void initPanel() {

		btn_Analyzer.addActionListener(this);
		btn_seldim.addActionListener(this);
		btn_drillSet.addActionListener(this);
		UIPanel panel = new UIPanel();
		panel.add(btn_Analyzer);
		panel.add(btn_seldim);
		panel.add(btn_drillSet);

		setContentPane(panel);

	}

	public void initData() {
		m_model = new MultiDimemsionModel("");

		SelDimModel selModel = MultiDimensionUtil
				.createSelDimModel(m_queryID);
		m_model.setSelDimModel(selModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_Analyzer) {
			m_analyzerDlg = null;
			getAnalyzerDlg().showModal();
			AnalyzerSet model = getAnalyzerDlg().getAnalyzerSet();
			m_model.setAnalyzerSet(model);
			return;
		}
		if (e.getSource() == btn_seldim) {
			m_seldimDlg = null;
			getSeldimDlg().setSelModel(m_model.getSelDimModel(), null);
			if (getSeldimDlg().showModal() == UIDialog.ID_OK) {

				SelDimModel model = getSeldimDlg().getSelModel();

				m_model.setSelDimModel(model);
			}
			return;
		}
		if (e.getSource() == btn_drillSet) {
			m_drillDlg = null;
			getDrillDlg()
					.setDrillThroughSet(m_model.getDrillTroughSet());
			if (getDrillDlg().showModal() == UIDialog.ID_OK) {

				DrillThroughSet model = getDrillDlg()
						.getDrillThroughSet();

				m_model.setDrillTroughSet(model);
			}
			return;
		}

	}

	private AnalyzerDialog getAnalyzerDlg() {
		if (m_analyzerDlg == null) {
			m_analyzerDlg = new AnalyzerDialog(this, m_model);
		}
		return m_analyzerDlg;

	}

	private SelDimSetDialog getSeldimDlg() {
		if (m_seldimDlg == null) {
			m_seldimDlg = new SelDimSetDialog(this);
		}
		return m_seldimDlg;
	}

	private DrillThroughSetDialog getDrillDlg() {
		if (m_drillDlg == null) {
			m_drillDlg = new DrillThroughSetDialog(null, m_model.getSelDimModel(),
					m_userID);
		}
		return m_drillDlg;
	}

}

