package com.ufsoft.table.exarea;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;


/**
 * ��������
 * @author liuyy
 *
 */
class ExAreaSetDlg extends UfoDialog implements ActionListener {
	 
	private JButton btnCancel = null;
	private JButton btnOk = null;
	private ExAreaBaseInfoPanel contentPane = null;
	private JDialog preDialog;
	private static final long serialVersionUID = -4681468577745857338L;
 
	ExAreaSetDlg(UfoReport parent,JDialog preDialog,ExAreaModel model, ExAreaCell cell) {
		super(parent);
		this.preDialog=preDialog;
		this.contentPane=new ExAreaBaseInfoPanel(model,cell,parent);
		contentPane.setParent(this);
		initialize();
		
	}
		
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) {
			this.setResult(UfoDialog.ID_CANCEL);
			
			this.contentPane.removeSelectedListener();
			if(this.preDialog!=null){
				this.preDialog.show();
			}
			return;
		}
		
		if (e.getSource() == btnOk) {
			 if(!contentPane.saveData()){
				 return;
			 }
			 setResult(UfoDialog.ID_OK);
			 if(this.preDialog!=null){
					this.preDialog.show();
				}
		}

		this.contentPane.removeSelectedListener();
		
	}
	
	public void setResult(int n) { 
		super.setResult(n);
		close();
	}

 
	protected String getHelpID() {
		return "";
	}

	public void closeDiolog() {

		this.contentPane.removeSelectedListener();
		
		setResult(ID_CANCEL);
		
	}
 
	private javax.swing.JButton getJBCancel() {
		if (btnCancel == null) {
			try {
				btnCancel = new nc.ui.pub.beans.UIButton();
				btnCancel.setBounds(277, 331, 75, 22);
				btnCancel.setName("JBCancel");
				//      ivjJBCancel.setFont(new java.awt.Font("dialog", 0, 14));
				btnCancel.setText(StringResource
						.getStringResource("miufo1000274")); //"ȡ  ��"
				btnCancel.setActionCommand("JBOK");
				// user code begin {1}
				btnCancel.addActionListener(this);
				btnCancel.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return btnCancel;
	}
  

	

	/**
	 * ���� JBOK ����ֵ��
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JButton getJBOK() {
		if (btnOk == null) {
			try {
				btnOk = new nc.ui.pub.beans.UIButton();
				btnOk.setBounds(130, 331, 75, 22);
				btnOk.setName("JBOK");
				//      ivjJBOK.setFont(new java.awt.Font("dialog", 0, 14));
				btnOk.setText(StringResource
						.getStringResource("miufo1000790")); //"ȷ  ��"
				btnOk.setActionCommand("JBOK");
				// user code begin {1}
				btnOk.addActionListener(this);
				btnOk.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return btnOk;
	}


	/**
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		// System.out.println("--------- δ��׽�����쳣 ---------");
		// exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 * @i18n miufo00079=����չ������
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("CellFuncFilterDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(MultiLang.getString("miufo00079"));  
			setSize(519, 418);
			setResizable(false);
			if (this.contentPane != null) {
				getContentPane().add(contentPane, BorderLayout.CENTER);
				JPanel jpanel = new JPanel(new FlowLayout(4));
				jpanel.add(getJBOK());
				jpanel.add(getJBCancel());
				getContentPane().add(jpanel, "South");
			}
			setModal(false);
			setLocationRelativeTo(this);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		

		
	}
	
	
	public void close() {
		this.dispose();
		
	}
	public ExAreaCell getExArea(){
		ExAreaCell cell=null;
		if(this.contentPane!=null){
			cell=contentPane.getExArea();
		}
		return cell;
	}
}
 