/*
 * 创建日期 2006-10-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.ufida.report.multidimension.applet;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;

import com.ufida.report.multidimension.model.LimitRowsSetVO;
import com.ufida.report.rep.model.FilterRowDescriptor;
import com.ufida.report.rep.model.SortVO;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.resource.StringResource;

public class RowLimitDlg extends UIDialog implements ActionListener{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JScrollPane	jScrollPane = null;

	private JLabel jLabel1 = null;

	private JList listCandidate = null;

	private JTextField txtLimitNum = null;

	private JLabel lblTop = null;

	private JLabel lblOrder = null;

	private JRadioButton rdBtnAsc = null;

	private JRadioButton rdBtnDesc = null;

	private JButton btnOK = null;

	private JButton btnCancel = null;
	
	private LimitRowsSetVO  m_setVO = null;  //  @jve:decl-index=0:

	/**
	 * @param owner
	 */
	public RowLimitDlg(Container parent){//Frame owner) {
		super(parent);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(347, 354);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			
			jLabel1 = new UILabel();
			jLabel1.setBounds(new Rectangle(8, 9, 56, 18));
			jLabel1.setText(StringResource.getStringResource("miufo1000589"));//选择
			jContentPane.add(jLabel1, null);
			jContentPane.add(getJScrollPane(), null);	
			
			lblTop = new UILabel();
			lblTop.setBounds(new Rectangle(228, 40, 78, 20));
			lblTop.setText(StringResource.getStringResource("ubimultidim0066"));//显示行数
			jContentPane.add(getTxtLimitNum(), null);
			jContentPane.add(lblTop, null);
			
			lblOrder = new UILabel();
			lblOrder.setBounds(new Rectangle(228, 145, 38, 20));
			lblOrder.setText(StringResource.getStringResource("ubimultidim0067"));//次序
			jContentPane.add(lblOrder, null);
			
			jContentPane.add(getRdBtnAsc(), null);
			jContentPane.add(getRdBtnDesc(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getBtnCancel(), null);
		}
		return jContentPane;
	}
	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(8, 33, 200, 238);
			jScrollPane.setViewportView(getListCandidate());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes listCandidate	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getListCandidate() {
		if (listCandidate == null) {
			listCandidate = new UIList();
		}
		return listCandidate;
	}

	/**
	 * This method initializes txtLimitNum	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtLimitNum() {
		if (txtLimitNum == null) {
			txtLimitNum = new UITextField();
			txtLimitNum.setBounds(new Rectangle(228, 84, 107, 20));
			((UITextField)txtLimitNum).setTextType("TextInt");
			((UITextField)txtLimitNum).setMinValue(1);
			((UITextField)txtLimitNum).setHorizontalAlignment(UITextField.LEFT);
		
		}
		return txtLimitNum;
	}

	/**
	 * This method initializes rdBtnAsc	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRdBtnAsc() {
		if (rdBtnAsc == null) {
			rdBtnAsc = new UIRadioButton();
			rdBtnAsc.setBounds(new Rectangle(228, 182, 64, 20));
			rdBtnAsc.setText(StringResource.getStringResource("miufo1001305"));//升序
			rdBtnAsc.addActionListener(this);
		}
		return rdBtnAsc;
	}

	/**
	 * This method initializes rdBtnDesc	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRdBtnDesc() {
		if (rdBtnDesc == null) {
			rdBtnDesc = new UIRadioButton();
			rdBtnDesc.setBounds(new Rectangle(228, 214, 64, 20));
			rdBtnDesc.setText(StringResource.getStringResource("miufo1001306"));//降序
			rdBtnDesc.addActionListener(this);
		}
		return rdBtnDesc;
	}

	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK.setBounds(new Rectangle(148, 289, 62, 17));
			btnOK.setText(StringResource.getStringResource("miufo1000758"));//确定
			btnOK.addActionListener(this);
		}
		return btnOK;
	}

	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setBounds(new Rectangle(244, 289, 62, 17));
			btnCancel.setText(StringResource.getStringResource("miufo1000757"));//取消
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == getBtnOK()){
			//得到控件的值，组成　RowLimitSetVO
			if( m_setVO != null ){
				//判断录入的行数必须是>0的整数
				int			nNums = Integer.parseInt(getTxtLimitNum().getText());
				FilterRowDescriptor filter = new FilterRowDescriptor();
				filter.setField((IMetaData)getListCandidate().getSelectedValue());
				filter.setMaxLineNum(nNums);
				if( getRdBtnAsc().isSelected()){
					filter.setType(SortVO.SORT_ASCENDING);
				}else{
					filter.setType(SortVO.SORT_DESCENDING);
				}
				m_setVO.setFilterRowDesc(filter);
			}
			closeOK();
		}else if( e.getSource()== getBtnCancel()){
			closeCancel();
		}else if( e.getSource()== getRdBtnAsc() ){
			if( !getRdBtnAsc().isSelected()){
				getRdBtnAsc().setSelected(true);
			}
			getRdBtnDesc().setSelected(false);
		}else if( e.getSource()==getRdBtnDesc() ){
			if( !getRdBtnDesc().isSelected()){
				getRdBtnDesc().setSelected(true);
			}
			getRdBtnAsc().setSelected(false);
			
		}
	}
	public void setCandidates(IMetaData[] candidates){
		//m_candidates = candidates;
		getListCandidate().setListData(candidates);
	}
	public void setRowLimitSetVO(LimitRowsSetVO limitVO){
		//设置各个控件的值
		if( limitVO != null ){
			m_setVO = limitVO;
			FilterRowDescriptor filter = limitVO.getFilterRowDesc();
			if( filter != null ){
				if( filter.getField()!= null ){
					getListCandidate().setSelectedValue(filter.getField(), true);
				}else{
					getListCandidate().setSelectedIndex(0);
				}
				getTxtLimitNum().setText(Integer.toString(filter.getMaxLineNum()) );
				getRdBtnDesc().setSelected(filter.getType()== SortVO.SORT_DESCENDING);
				getRdBtnAsc().setSelected(!(filter.getType()== SortVO.SORT_DESCENDING));
			}else{
				getListCandidate().setSelectedIndex(0);
				getRdBtnAsc().setSelected(true);
				getRdBtnDesc().setSelected(false);
				getTxtLimitNum().setText("100");
			}
		}
	}
	public LimitRowsSetVO getRowLimitSetVO(){
		return m_setVO;
	}
	

}  //  @jve:decl-index=0:visual-constraint="63,3"
