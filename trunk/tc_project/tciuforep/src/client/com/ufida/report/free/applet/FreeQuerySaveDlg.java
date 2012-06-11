package com.ufida.report.free.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import nc.bs.iufo.datasetmanager.DataSetDirDMO;
import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.us.bi.report.manager.BIReportSrv;
import nc.util.iufo.resmng.IResMngConsants;
import nc.vo.bi.report.manager.ReportDirVO;
import nc.vo.bi.report.manager.ReportVO;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;

public class FreeQuerySaveDlg extends UIDialog {

	private static final long serialVersionUID = -1180473186251360367L;

	private UIPanel jContentPane = null;

	private UIPanel jPanel = null;

	private UITextField tf_repCode = null;

	private UITextField tf_repName = null;

	private UITextArea tf_repNote = null;

	private UITextField tf_repDir = null;

	private UIButton m_dirRefbtn = null;

	private ReportDirDlg m_refDlg = null;

	private UIButton OKBtn = null;

	private UIButton cancelBtn = null;

	private String m_strUserPK = null;

	private String m_strUnitPK = null;
	private boolean m_bShowDS = false;
	private JTabbedPane m_tabPane = null;
	private UIPanel m_dsPanel = null;
	private UITextField tf_dsCode = null;

	private UITextField tf_dsName = null;

	/**
	 * This is the default constructor
	 */
	public FreeQuerySaveDlg(Container parent, String userPK, String unitPK) {
		super(parent);
		m_strUserPK = userPK;
		m_strUnitPK = unitPK;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n mbirep00009=筛选行数
	 */
	private void initialize() {
		this.setSize(300, 290);
		this.setTitle(StringResource.getStringResource("miufopublic108"));// 保存
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
			jContentPane.setLayout(null);
			jContentPane.add(getJPanel());
			jContentPane.add(getOKBtn(200));
			jContentPane.add(getCancelBtn(200));
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 * @i18n mbirep00009=筛选行数
	 */
	private UIPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(6, 5, 280, 150);

			jPanel.add(createLabelByResID("miufo1001012", 0));// miufo1001012=编码
			jPanel.add(getTextCode());
			jPanel.add(createLabelByResID("miufo1001051", 1));// miufo1001051=名称
			jPanel.add(getTextName());
			jPanel.add(createLabelByResID("miufo1001052", 2));// miufo1001052=说明
			jPanel.add(getTextNote());
			jPanel.add(createLabelByResID("miufopublic476", 3));// miufopublic476=位置
			jPanel.add(getTextReportDir());
			jPanel.add(getRefBtn());
		}
		return jPanel;
	}

	private UIPanel getDSPanel() {
		if (m_dsPanel == null) {
			m_dsPanel = new UIPanel();
			m_dsPanel.setLayout(null);
			m_dsPanel.setBounds(6, 5, 280, 150);

			m_dsPanel.add(createLabelByResID("miufo1001012", 0));// miufo1001012=编码
			m_dsPanel.add(getTextDSCode());
			m_dsPanel.add(createLabelByResID("miufo1001051", 1));// miufo1001051=名称
			m_dsPanel.add(getTextDSName());
		}
		return m_dsPanel;
	}

	private JLabel createLabelByResID(String resID, int index) {
		JLabel lbl = new UILabel();
		lbl.setText(StringResource.getStringResource(resID));
		lbl.setBounds(30, 20 + index * 30, 60, 22);
		return lbl;
	}

	private UITextField getTextCode() {
		if (tf_repCode == null) {
			tf_repCode = new UITextField();
			tf_repCode.setBounds(80, 20, 160, 22);
			tf_repCode.setAllowNumeric(true);
			tf_repCode.setAllowAlphabetic(true);
			tf_repCode.setAllowOtherCharacter(false);
		}
		return tf_repCode;
	}

	private UITextField getTextName() {
		if (tf_repName == null) {
			tf_repName = new UITextField();
			tf_repName.setBounds(80, 50, 160, 22);
		}
		return tf_repName;
	}

	private UITextField getTextDSCode() {
		if (tf_dsCode == null) {
			tf_dsCode = new UITextField();
			tf_dsCode.setBounds(80, 20, 160, 22);
			tf_dsCode.setAllowNumeric(true);
			tf_dsCode.setAllowAlphabetic(true);
			tf_dsCode.setAllowOtherCharacter(false);
		}
		return tf_dsCode;
	}

	private UITextField getTextDSName() {
		if (tf_dsName == null) {
			tf_dsName = new UITextField();
			tf_dsName.setBounds(80, 50, 160, 22);
		}
		return tf_dsName;
	}

	private UITextArea getTextNote() {
		if (tf_repNote == null) {
			tf_repNote = new UITextArea();
			tf_repNote.setBounds(80, 80, 160, 22);
		}
		return tf_repNote;
	}

	private UITextField getTextReportDir() {
		if (tf_repDir == null) {
			tf_repDir = new UITextField();
			tf_repDir.setBounds(80, 110, 160, 22);
			tf_repDir.setEditable(false);
		}
		return tf_repDir;
	}

	private UIButton getRefBtn() {
		if (m_dirRefbtn == null) {
			m_dirRefbtn = new UIButton();
			m_dirRefbtn.setBounds(240, 110, 22, 21);
			m_dirRefbtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getRefDlg().showModal() == UIDialog.ID_OK) {
						ReportDirVO dirVO = getRefDlg().getReportDirVO();
						getTextReportDir().setText(dirVO.getDirName());
					}
				};
			});
		}
		return m_dirRefbtn;
	}

	private ReportDirDlg getRefDlg() {
		if (m_refDlg == null) {
			m_refDlg = new ReportDirDlg(this, m_strUserPK, m_strUnitPK);
		}
		return m_refDlg;
	}

	/**
	 * This method initializes OKBtn
	 * 
	 * @return javax.swing.JButton
	 * @i18n miufo1003314=确定
	 */
	private UIButton getOKBtn(int y) {
		if (OKBtn == null) {
			OKBtn = new UIButton();
			OKBtn.setBounds(45, y, 75, 22);
			OKBtn.setText(StringResource.getStringResource("miufo1003314"));
			OKBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String msg = checkInput();
					if (msg != null) {
						UfoPublic.showErrorDialog(getJContentPane(), msg, null);
						return;
					}
					setResult(UfoDialog.ID_OK);
					close();
				}
			});
		}
		return OKBtn;
	}

	private String checkInput() {
		String msg = checkReportInput();
		if (msg != null)
			return msg;
		if (m_bShowDS) {
			msg = checkDSInput();
		}
		return msg;
	}

	/**
	 * @i18n ubirep0001=报表编码
	 * @i18n uiufofur50381=报表名称
	 * @i18n miufo1001022=、
	 * @i18n miufo00283=为必录项
	 * @i18n miufo00284=请选择报表目录
	 * @i18n miufo00285=根目录不能创建报表
	 * @i18n miufo00286=目录选择有误
	 */
	private String checkReportInput() {
		String msg = null;
		String code = getTextCode().getText();
		if (code != null)
			code = code.trim();
		if (code == null || code.length() == 0)
			msg = StringResource.getStringResource("ubirep0001");
		String name = getTextName().getText();
		if (name != null)
			name = name.trim();
		if (name == null || name.length() == 0) {
			if (msg == null)
				msg = StringResource.getStringResource("uiufofur50381");
			else
				msg += StringResource.getStringResource("miufo1001022")
						+ StringResource.getStringResource("uiufofur50381");
		}
		if (msg != null) {
			msg += StringResource.getStringResource("miufo00283");
			return msg;
		}
		String dirName = getTextReportDir().getText();
		if (dirName == null || dirName.length() == 0) {
			return StringResource.getStringResource("miufo00284");
		}

		ReportDirVO dirVO = getRefDlg().getReportDirVO();
		if (dirVO == null || dirVO.getDirID().equals(IResMngConsants.VIRTUAL_ROOT_ID))
			return StringResource.getStringResource("miufo00285");
		ReportVO newVO = new ReportVO();
		newVO.setReportcode(code);
		newVO.setReportname(name);
		String folderID = getRefDlg().getReportDirVO().getDirID();
		if (folderID == null)
			return StringResource.getStringResource("miufo00286");

		newVO.setPk_folderID(folderID);
		try {
			BIReportSrv.getInstance().checkReportFileVO(newVO);
		} catch (UISrvException e) {
			return e.getMessage();
		}
		return null;

	}

	/**
	 * @i18n miufo00287=数据集编码
	 * @i18n miufo1001022=、
	 * @i18n uiufofunc205=数据集名称
	 * @i18n miufo00283=为必录项
	 * @i18n miufo00289=数据集编码重复
	 * @i18n miufo00290=数据集名称重复
	 */
	private String checkDSInput() {
		String msg = null;
		String code = getTextDSCode().getText();
		if (code != null)
			code = code.trim();
		if (code == null || code.length() == 0)
			msg = StringResource.getStringResource("miufo00287");
		String name = getTextDSName().getText();
		if (name != null)
			name = name.trim();
		if (name == null || name.length() == 0) {
			if (msg != null)
				msg += StringResource.getStringResource("miufo1001022");
			else
				msg = "";
			msg += StringResource.getStringResource("uiufofunc205");
		}
		if (msg != null) {
			msg += StringResource.getStringResource("miufo00283");
			return msg;
		}
		try {
			if (DataSetDefBO_Client.loadDataSetDefVoByCode(code) != null) {
				return StringResource.getStringResource("miufo00289");
			}
			if (DataSetDefBO_Client.loadDataSetDefVoByName(DataSetDirDMO.PrivateDataSetPK, name) != null) {
				return StringResource.getStringResource("miufo00290");
			}

		} catch (UFOSrvException e) {
			return e.getMessage();
		}
		return null;

	}

	/**
	 * This method initializes cancelBtn
	 * 
	 * @return javax.swing.JButton
	 * @i18n miufo1003315=取消
	 */
	private UIButton getCancelBtn(int y) {
		if (cancelBtn == null) {
			cancelBtn = new UIButton();
			cancelBtn.setBounds(180, y, 75, 22);
			cancelBtn.setText(StringResource.getStringResource("miufo1003315"));
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(UfoDialog.ID_CANCEL);
					close();
				}
			});
		}
		return cancelBtn;
	}

	public static void main(String[] args) {
		FreeQuerySaveDlg dlg = new FreeQuerySaveDlg(null, null, null);
		dlg.setLocationRelativeTo(null);
		if ((dlg.showModal() == UfoDialog.ID_OK)) {
		}
	}

	public String[] getReportInfo() {
		String[] info = new String[4];
		info[0] = getTextCode().getText().trim();
		info[1] = getTextName().getText().trim();
		info[2] = getTextNote().getText().trim();
		info[3] = getRefDlg().getReportDirVO().getDirID();
		return info;
	}

	/**
	 * @i18n miuforepcalc0008=报表
	 * @i18n miufo00241=数据集
	 */
	public void setShowDataSet(boolean showDSInfo, String dsCode, String dsName) {
		m_bShowDS = showDSInfo;
		if (showDSInfo) {
			getTabbedPane().addTab(StringResource.getStringResource("miuforepcalc0008"), getJPanel());
			getTabbedPane().addTab(StringResource.getStringResource("miufo00241"), getDSPanel());

			this.getJContentPane().remove(getJPanel());
			this.getJContentPane().add(getTabbedPane());
		} else {

		}

	}

	private JTabbedPane getTabbedPane() {
		if (m_tabPane == null) {
			m_tabPane = new UITabbedPane();
			m_tabPane.setBounds(6, 5, 280, 180);
		}
		return m_tabPane;
	}

	public String[] getDataSetInfo() {
		String[] info = new String[2];
		info[0] = getTextDSCode().getText().trim();
		info[1] = getTextDSName().getText().trim();
		return info;
	}

}
