package com.ufida.report.chart;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.dsmanager.DatasetTreeDlg;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.iufo.datasetmanager.DataSetDefVO;

import org.jfree.chart.plot.PlotOrientation;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.chart.CategoryChartModel;
import com.ufsoft.iufo.fmtplugin.chart.ChartConstants;
import com.ufsoft.iufo.fmtplugin.chart.ChartDesc;
import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.iufo.fmtplugin.chart.MultipleAxisChartModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.re.JWrapLabel;

/**
 * 图表定义界面。
 * 
 * @author liuyy
 * 
 */
public class ChartSettingDlg extends UfoDialog implements IUfoContextKey {
	private static final long serialVersionUID = 1L;

	private JList listType;
	private JTextField fldTitle;
	private JTextField fldDataSet;
	private JList listRootTypes;
	private JWrapLabel lblImgNote = new JWrapLabel();
	private JPanel pnlExtendsContainer = null;

	private ChartSettingPanel m_settingPnl = null;

	private JCheckBox cbLegend; 

	private final JRadioButton rbOriH = new JRadioButton();
	private final JRadioButton rbOriV = new JRadioButton();

	private ChartModel m_chartModel = null;
	// 记录反复切换报表类别产生的临时模型和设置的临时值。
	private Map<Class<? extends ChartModel>, ChartModel> m_settingModels = new HashMap<Class<? extends ChartModel>, ChartModel>();

	
	private UfoReport m_report = null;
	
	/**
	 * @i18n miufo00410=图表定义
	 * @i18n miufo1000602=图表类型
	 * @i18n miufo00411=图表类型：
	 * @i18n miufo00412=子图表类型：
	 * @i18n miufo00413=图表类型说明
	 * @i18n miufo00414=数据定义
	 */
	public ChartSettingDlg(ChartModel chartModel, Container owner) {

		super(owner);
		m_report = (UfoReport) owner;
		
		setSize(new Dimension(600, 530));
		setResizable(false);
		setLocationRelativeTo(owner);

		m_chartModel = chartModel;

		getContentPane().setLayout(null);
		setTitle(StringResource.getStringResource("miufo00410"));

		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0, 0, 564, 437);
		getContentPane().add(tabbedPane);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

			}
		});

		final JPanel pnlChartType = new JPanel();
		pnlChartType.setLayout(null);
		tabbedPane.addTab(StringResource.getStringResource("miufo1000602"), null, pnlChartType, null);

		final JLabel label = new JLabel();
		label.setText(StringResource.getStringResource("miufo00411"));
		label.setBounds(20, 10, 66, 18);
		pnlChartType.add(label);

		final JLabel label_1 = new JLabel();
		label_1.setText(StringResource.getStringResource("miufo00412"));
		label_1.setBounds(222, 10, 101, 18);
		pnlChartType.add(label_1);

		lblImgNote.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, false));
		lblImgNote.setToolTipText(StringResource.getStringResource("miufo00413"));
		lblImgNote.setRequestFocusEnabled(false);
		lblImgNote.setText("");
		lblImgNote.setBounds(222, 345, 292, 51);
		pnlChartType.add(lblImgNote);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(222, 34, 292, 305);
		pnlChartType.add(scrollPane);

		listType = new JList();
		scrollPane.setViewportView(listType);

		final JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 34, 178, 244);
		pnlChartType.add(scrollPane_1);

		listRootTypes = new JList();
		scrollPane_1.setViewportView(listRootTypes);

		pnlChartType.add(getPnlOptions());

		pnlChartType.add(getPnlOrientation());

		final JPanel pnlData = new JPanel();
		pnlData.setLayout(null);
		tabbedPane.addTab(StringResource.getStringResource("miufo00414"), null, pnlData, null);

		pnlData.add(getPnlDataSet());

		pnlExtendsContainer = new JPanel();
		pnlExtendsContainer.setBounds(32, 85, 505, 300);
		pnlData.add(pnlExtendsContainer);
		pnlExtendsContainer.setLayout(null);

		initButtons();

		initData();

	}

	public ChartModel getChartModel() {
		return m_chartModel;
	}

	/**
	 * @i18n miufo00415=图表方向
	 * @i18n miufo1003093=水平
	 * @i18n miufo1003094=垂直
	 */
	private JPanel getPnlOrientation() {
		final JPanel pnlOrientation = new JPanel();
		pnlOrientation.setLayout(null);
		pnlOrientation.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("miufo00415"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));
		pnlOrientation.setBounds(20, 284, 178, 56);

		rbOriH.setBounds(28, 20, 59, 26);
		pnlOrientation.add(rbOriH);
		rbOriH.setText(StringResource.getStringResource("miufo1003093"));

		rbOriV.setBounds(87, 20, 59, 26);
		pnlOrientation.add(rbOriV);
		rbOriV.setText(StringResource.getStringResource("miufo1003094"));

		// 设置按钮组
		final ButtonGroup groupExType = new ButtonGroup();
		groupExType.add(rbOriH);
		groupExType.add(rbOriV);

		return pnlOrientation;
	}

	/**
	 * @i18n miufo00416=图例选项
	 * @i18n miufo1001316=显示图例
	 */
	private JPanel getPnlOptions() {
		final JPanel pnlOption = new JPanel();
		pnlOption.setLayout(null);
		pnlOption.setBounds(20, 345, 178, 51);
		pnlOption.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("miufo00416"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));

		cbLegend = new JCheckBox();
		cbLegend.setBounds(39, 16, 90, 26);
		cbLegend.setText(StringResource.getStringResource("miufo1001316"));
		pnlOption.add(cbLegend);
 
		return pnlOption;
	}

	/**
	 * @i18n miufo00417=关联数据集
	 * @i18n miufo00418=数据集：
	 * @i18n miufo00419=图表标题：
	 */
	private JPanel getPnlDataSet() {
		final JPanel pnlDataSet = new JPanel();
		pnlDataSet.setLayout(null);
		pnlDataSet.setBounds(32, 15, 505, 64);
		pnlDataSet.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("miufo00417"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));

		JButton btnDataSet = new JButton();
		btnDataSet.setBounds(206, 30, 27, 22);
		pnlDataSet.add(btnDataSet);
		btnDataSet.setText("...");
		btnDataSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Context context = m_report.getContextVo();
				String strRepId = context.getAttribute(REPORT_PK) == null ? null : (String)context.getAttribute(REPORT_PK);
				//modified by csli:obj==-1||obj==null:从IUFO打开；obj==0:从NC的管理界面打开；obj==1:从NC设计想到中打开
				Integer obj=(Integer)context.getAttribute(ReportResource.OPEN_IN_MODAL_DIALOG);
				DatasetTreeDlg dlg = new DatasetTreeDlg(ChartSettingDlg.this,
						true,false, strRepId,(obj!=null&&obj>=0) ? new String[]{"R","G","H"} : null);
				dlg.setUsingType(true, true);
				dlg.showModal();
				dlg.destroy();
				if (dlg.getResult() == UIDialog.ID_OK) {
					DataSetDefVO[] defs = dlg.getSelDatasetDefs();
					if (defs != null && defs.length > 0) {
						String id = defs[0].getPk_datasetdef();
						m_chartModel.setDataSetDefPK(id);
						String name = defs[0].getName();
						fldTitle.setText(name);
						ChartSettingDlg.this.fldDataSet.setText(name);

						ChartSettingDlg.this.m_settingPnl.changeDataSet();
					}
				}
			}
		});

		fldDataSet = new JTextField();
		fldDataSet.setBounds(81, 30, 119, 22);
		fldDataSet.setEnabled(false);
		pnlDataSet.add(fldDataSet);

		final JLabel label_2 = new JLabel();
		label_2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label_2.setBounds(10, 32, 72, 18);
		pnlDataSet.add(label_2);
		label_2.setText(StringResource.getStringResource("miufo00418"));

		final JLabel label_3 = new JLabel();
		label_3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label_3.setBounds(256, 35, 91, 15);
		pnlDataSet.add(label_3);
		label_3.setText(StringResource.getStringResource("miufo00419"));

		fldTitle = new JTextField();
		fldTitle.setBounds(353, 33, 128, 19);
		pnlDataSet.add(fldTitle);

		return pnlDataSet;
	}

	/**
	 * @i18n miufo1000758=确定
	 * @i18n miufo1000757=取消
	 */
	private void initButtons() {
		final JButton btnOK = new JButton();
		btnOK.setText(StringResource.getStringResource("miufo1000758"));
		btnOK.setBounds(303, 452, 106, 28);
		getContentPane().add(btnOK);
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (Throwable t) {
					UfoPublic.showErrorDialog(ChartSettingDlg.this, t
							.getMessage(), "");
					return;
				}
				setResult(ID_OK);
				ChartSettingDlg.this.close();
			}
		});

		final JButton btnCancel = new JButton();
		btnCancel.setText(StringResource.getStringResource("miufo1000757"));
		btnCancel.setBounds(436, 452, 106, 28);
		getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setResult(ID_CANCEL);
				ChartSettingDlg.this.close();
			}
		});
	}

	/**
	 * 初始化各类型图表的细节数据定义面板。
	 */
	private void initDetailPanel() {
		ChartDesc cd = (ChartDesc) listRootTypes.getSelectedValue();
		int parentType = cd.getType();

		ChartModel newModel = null;
		Class<? extends ChartModel> newClz = cd.getModelClz();
		if (newClz == null) {
			if(m_chartModel instanceof MultipleAxisChartModel){
				newClz = MultipleAxisChartModel.class;
			} else{
				newClz = CategoryChartModel.class;
			}
		}

		ChartModel oldModel = m_chartModel;
		m_settingModels.put(oldModel.getClass(), oldModel);

		newModel = m_settingModels.get(newClz);

		if (newModel != null && newModel.equals(oldModel)
				&& m_settingPnl != null) {
			m_settingPnl.changeDataSet();
			return;
		}

		if (newModel == null) {
			try {
				newModel = newClz.newInstance();
			} catch (Throwable e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		com.ufida.iufo.pub.tools.Toolkit.copyProperties(oldModel.clone(), newModel);
		m_chartModel = newModel;

		ChartSettingPanel sp = null;
		switch (parentType) {
		
//		case ChartConstants.CHART_PIE:
//			sp = new PieSettingPanel(m_chartModel);
//			break;
		
		case ChartConstants.CHART_METER:
			sp = new MeterSettingPanel(m_chartModel);
			break;
		case ChartConstants.CHART_XYSERICES:
			sp = new XYSericesSettingPanel(m_chartModel);
			break;
			
		case ChartConstants.CHART_TIMESERICES:
			sp = new TimeSericesSettingPanel(m_chartModel);
			break;		

		case ChartConstants.CHART_BUBBLE:
			sp = new BubbleSettingPanel(m_chartModel);
			break;		

		case ChartConstants.CHART_CANDLESTICK:
			sp = new CandlestickSettingPanel(m_chartModel);
			break;		
			
		case ChartConstants.CHART_GANTT:
			sp = new GanttSettingPanel(m_chartModel);
			break;
		
		case ChartConstants.CHART_COMBINE:
			sp = new CombineSettingPanel(m_chartModel);
			break;
		default:
			sp = new MultiChartSettingPanel(m_chartModel);
			break;
		}
		pnlExtendsContainer.removeAll();
		pnlExtendsContainer.add(sp);
		sp.setBounds(0, 0, 505, 300);

		m_settingPnl = sp;

	}

	private void initData() {

		DefaultListModel lmRoot = new DefaultListModel();
		ChartDesc[] cds = ChartConstants.getRoots();
		for (ChartDesc cd : cds) {
			lmRoot.addElement(cd);
		}
		this.listRootTypes.setModel(lmRoot);
		listRootTypes.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		this.listRootTypes
				.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						ChartDesc cd = (ChartDesc) listRootTypes
								.getSelectedValue();
						ChartDesc[] subs = ChartConstants.getSubs(cd.getType());
						DefaultListModel md = (DefaultListModel) listType
								.getModel();
						md.removeAllElements();
						for (ChartDesc c : subs) {
							md.addElement(c);
						}
						ChartDesc selectedCD = ChartConstants
								.getChartDesc(m_chartModel.getType());
						listType.setSelectedValue(selectedCD, true);
						if (listType.getSelectedIndex() < 0) {
							listType.setSelectedIndex(0);
						}

						initDetailPanel();

					}

				});

		listType.setModel(new DefaultListModel());
		listType.setCellRenderer(new ChartItemRenderer());
		listType.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		this.listType.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ChartDesc cd = (ChartDesc) listType.getSelectedValue();
				if (cd == null) {
					return;
				}
				String note = cd.getNote();
				if (note == null || "".equals(note)) {
					note = cd.getName();
				}
				lblImgNote.setText(note);
				m_chartModel.setType(cd.getType());
			}
		});

		// 初始化类型
		int type = m_chartModel.getType();
		ChartDesc cd = ChartConstants.getChartDesc(type);
		if (cd == null) {
			cd = ChartConstants
					.getChartDesc(ChartConstants.CHART_BAR_CLUSTERED);
		}
		ChartDesc pcd = ChartConstants.getChartDesc(cd.getParentType());
		listRootTypes.setSelectedValue(pcd, true);

		fldTitle.setText(m_chartModel.getTitle());

		cbLegend.setSelected(m_chartModel.isLegendVisible());

		if (m_chartModel.getOrientation() == PlotOrientation.HORIZONTAL) {
			rbOriH.setSelected(true);
		} else {
			rbOriV.setSelected(true);
		}
		
		String dataSetDefId = m_chartModel.getDataSetDefPK();
		if (dataSetDefId != null) {
			DataSetDefVO ddvo;
			try {
				ddvo = DataSetDefBO_Client.loadDataSetDefVOByPk(dataSetDefId);
				if (ddvo != null) {
					fldDataSet.setText(ddvo.getName());
				}
			} catch (UFOSrvException e1) {
				AppDebug.debug(e1);
			}

		}

	}

	private void save() {
		
		m_settingPnl.save();
		
		m_chartModel = (ChartModel)m_settingPnl.getChartModel();
		m_chartModel.setTitle(fldTitle.getText());
		m_chartModel.setLegend(cbLegend.isSelected());
		if (rbOriH.isSelected()) {
			m_chartModel.setOrientation(PlotOrientation.HORIZONTAL);
		} else {
			m_chartModel.setOrientation(PlotOrientation.VERTICAL);
		}

		

	}

	private void processEnabled(Container comp, boolean enabled) {
		if (comp == null) {
			return;
		}
		comp.setEnabled(enabled);
		Component[] comps = comp.getComponents();
		if (comps == null)
			return;
		for (Component c : comps) {
			if (c instanceof Container) {
				processEnabled((Container) c, enabled);
			}
		}
	}

}
 