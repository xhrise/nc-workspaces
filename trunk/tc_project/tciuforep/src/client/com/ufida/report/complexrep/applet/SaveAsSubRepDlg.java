/*
 * 创建日期 2006-7-17
 */
package com.ufida.report.complexrep.applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.us.bi.report.manager.BIReportSrv;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.resmng.ResMngBizHelper;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.resmng.ResMngDirVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.rep.model.BIContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * @author ljhua
 */
class SaveAsSubRepDlg extends UfoDialog  implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JLabel lblName = null;
	private JTextField txtName = null;
	private JLabel lblCode = null;
	private JTextField txtCode = null;
	private JLabel lblDir = null;
	private JTextField txtDir = null;
	private JButton btnRefer = null;
	private JButton btnOK = null;
	private JButton jButton2 = null;
	
	private ReportVO m_repVO=null;
	private String m_repDirId=null;
	private ComplexRepPlugin m_plugIn=null; 
	/**
	 * This is the default constructor
	 */
	public SaveAsSubRepDlg(ComplexRepPlugin plugin) {
		super(plugin==null?null:plugin.getReport().getFrame());
		m_plugIn=plugin;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n miufo00328=另存为
	 */
	private void initialize() {
		setTitle(StringResource.getStringResource("miufo00328"));
		this.setSize(350, 200);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			lblDir = new nc.ui.pub.beans.UILabel();
			lblCode = new nc.ui.pub.beans.UILabel();
			lblName = new nc.ui.pub.beans.UILabel();
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			lblName.setBounds(12, 11, 77, 24);
			lblName.setText(StringResource.getStringResource(StringResConst.LABEL_REPNAME));
			lblCode.setBounds(12, 49, 77, 24);
			lblCode.setText(StringResource.getStringResource(StringResConst.LABEL_REPCODE));
			lblDir.setBounds(12, 86, 77, 24);
			lblDir.setText(StringResource.getStringResource(StringResConst.LABEL_REPDIR));
			jContentPane.add(lblName, null);
			jContentPane.add(getTxtName(), null);
			jContentPane.add(lblCode, null);
			jContentPane.add(getTxtCode(), null);
			jContentPane.add(lblDir, null);
			jContentPane.add(getTxtDir(), null);
			jContentPane.add(getBtnRefer(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getJButton2(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes txtName	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new UITextField();
			txtName.setBounds(103, 11, 145, 24);
		}
		return txtName;
	}
	/**
	 * This method initializes txtCode	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtCode() {
		if (txtCode == null) {
			txtCode = new UITextField();
			txtCode.setBounds(103, 49, 145, 24);
		}
		return txtCode;
	}
	/**
	 * This method initializes txtDir	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtDir() {
		if (txtDir == null) {
			txtDir = new UITextField();
			txtDir.setBounds(103, 86, 145, 24);
		}
		return txtDir;
	}
	/**
	 * This method initializes btnRefer	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnRefer() {
		if (btnRefer == null) {
			btnRefer = new nc.ui.pub.beans.UIButton();
			btnRefer.setBounds(250, 87, 39, 22);
			btnRefer.setText(StringResource.getStringResource(StringResConst.BTN_REFER));
			btnRefer.addActionListener(this);
		}
		return btnRefer;
	}
	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new nc.ui.pub.beans.UIButton();
			btnOK.setBounds(15, 139, 75, 22);
			btnOK.setText(StringResource.getStringResource("miufopublic246"));
			btnOK.addActionListener(this);
		}
		return btnOK;
	}
	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new nc.ui.pub.beans.UIButton();
			jButton2.setBounds(169, 136, 75, 22);
			jButton2.setText(StringResource
					.getStringResource("miufopublic247"));
			jButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(UfoDialog.ID_CANCEL);
					close();
				}
			});
		}
		return jButton2;
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			String strName=getTxtName().getText();
			
			
			if(strName==null || strName.trim().length()==0){
				JOptionPane.showConfirmDialog(this,StringResource.getStringResource(StringResConst.MSG_REPNAMENULL));
				return;
			}
			String strCode=getTxtCode().getText();
			if(strCode==null || strCode.trim().length()==0){
				JOptionPane.showConfirmDialog(this,StringResource.getStringResource(StringResConst.MSG_REPCODENULL));
				return;
			}
			if(m_repDirId==null){
				JOptionPane.showConfirmDialog(this,StringResource.getStringResource(StringResConst.MSG_REPDIRNULL));
				return;
			}
				
			
			//TOOD 校验名称、编码不重复
			
			m_repVO=new ReportVO();
			m_repVO.setReportcode(strCode);
			m_repVO.setReportname(strName);
			m_repVO.setPk_folderID(m_repDirId);
			
			setResult(UfoDialog.ID_OK);
			close();
		}
		else if(e.getSource() ==getBtnRefer()){
			//参照
			String userPK = ((BIContextVO) m_plugIn.getReport().getContextVo()).getCurUserID();
			SelRepDirDlg refDlg=new SelRepDirDlg(this,userPK);
			refDlg.show();
			if(refDlg.getResult()==ID_OK){
				IResTreeObject res=refDlg.getSelectedDir();
				if(res!=null && res.getSrcVO()!=null
						&& res.getSrcVO() instanceof ResMngDirVO){
					m_repDirId=((ResMngDirVO) res.getSrcVO()).getDirPK();
					String strFullName=((ResMngDirVO) res.getSrcVO()).getName();
					try{
						String strResOwnerPK=ResMngBizHelper.getResOwnerPK(IBIResMngConstants.MODULE_REPORT,userPK);
					 	strFullName=BIReportSrv.getInstance().getFullPathName(res.getID(),strResOwnerPK);
					}catch(Exception ex){
						AppDebug.debug(ex);
					}
		
					getTxtDir().setText(strFullName);
				}
			}
		}

	}
	
	public ReportVO getReportVO(){
		return m_repVO;
	}
	
      }  //  @jve:decl-index=0:visual-constraint="10,10"

 