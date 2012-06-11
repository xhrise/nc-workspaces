package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.re.RefTextField;

/**
 * 报表穿透的设置界面
 * 
 * @author ll
 * 
 */
public class ReportDrillDlg extends UfoDialog {
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JLabel jLabel = null;
	private RefTextField jRefText = null;
	private JButton OKBtn = null;
	private JButton cancelBtn = null;
	private ReportVO m_aimReportVO = null;
	private String m_user = null;
	private String m_unit = null;
	private AnaReportRefPanel m_refDlg = null;

	/**
	 * This is the default constructor
	 */
	public ReportDrillDlg(Container owner, String userPk, String unitPK, String aimReport) {
		super(owner);
		m_user = userPk;
		m_unit = unitPK;
		initialize();
		ReportSrv srv = new ReportSrv();
		ValueObject[] vos = srv.getByIDs(new String[] { aimReport });
		if (vos != null && vos.length > 0) {
			m_aimReportVO = (ReportVO) vos[0];
			getRefPane().setText(m_aimReportVO.getReportcode());
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource(ReportDrillExt.RESID_SET_DRILL_REPORT));
//		this.setSize(400, 200);
		this.setContentPane(getJContentPane());
        pack();
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
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
			JPanel panel = new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			panel.add(getOKBtn());
			panel.add(getCancelBtn());
			jContentPane.add(panel, BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JPanel getMainPanel() {
		JPanel panel = new UIPanel();
		panel.setSize(400, 100);
		panel.setLayout(new BorderLayout());
		jLabel = new JLabel(StringResource.getStringResource(ReportDrillExt.RESID_CHOOSE_DRILL_REPORT));
		// panel.setLayout(new GridLayout(5, 2));
		panel.add(jLabel, BorderLayout.NORTH);
		JPanel pp1 = new UIPanel();
		pp1.add(getRefPane());
		panel.add(pp1, BorderLayout.CENTER);
		return panel;
	}

	private RefTextField getRefPane() {
		if (jRefText == null) {
			jRefText = new RefTextField() {
				protected void addCloseListener() {
				}

				protected void showDialog() {
					getRefDlg().setDefaultValue(m_aimReportVO);
					super.showDialog();
				}
			};
			jRefText.getDialog().setModal(true);
			jRefText.setPreferredSize(new Dimension(260, 22));
			jRefText.setRefComp(getRefDlg(), m_aimReportVO);
		}
		return jRefText;
	}

	private AnaReportRefPanel getRefDlg() {
		if (m_refDlg == null) {
			m_refDlg = new AnaReportRefPanel(this, m_user, m_unit) {
				public void doClose() {
					getRefPane().closeRef();
				}
			};
			m_refDlg.setPreferredSize(new Dimension(300, 500));
		}
		return m_refDlg;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();
			OKBtn.setText(StringResource.getStringResource("mbiadhoc00021"));
			OKBtn.addActionListener(new BtnListener());
		}
		return OKBtn;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new nc.ui.pub.beans.UIButton();
			cancelBtn.setText(StringResource.getStringResource("mbiadhoc00022"));
			cancelBtn.addActionListener(new BtnListener());
		}
		return cancelBtn;
	}

	private class BtnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == OKBtn) {
				ReportVO newVO = (ReportVO) getRefDlg().getSelectValue();
				if (newVO != null)
					m_aimReportVO = newVO;
				String text = getRefPane().getText();
				if(text == null || text.trim().length()==0)
					m_aimReportVO = null;
				setResult(UfoDialog.ID_OK);
				close();
			} else if (e.getSource() == cancelBtn) {
				setResult(UfoDialog.ID_CANCEL);
				close();
			}
		}
	}

	public ReportVO getAimReportVO() {
		return m_aimReportVO;
	}
}
