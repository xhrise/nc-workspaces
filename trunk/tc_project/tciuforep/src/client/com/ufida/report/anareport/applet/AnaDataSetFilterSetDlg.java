package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.datasetmanager.DataSetDefVO;

import com.ufida.dataset.descriptor.FilterDescriptor;
import com.ufida.report.rep.applet.exarea.ExAreaFilterSetPanel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

public class AnaDataSetFilterSetDlg extends UfoDialog implements ActionListener{
	private JPanel jDataSetInfoPanel = null;
	private JTextField jTxDataSetValue = null;//数据集
	private ExAreaFilterSetPanel filterPanel;
	private JPanel jCmdPanel = null;
	private JButton btnOK;
	private JButton btnCancel;
	
	private AnaReportPlugin m_plugin=null;
	private DataSetDefVO selDataDef=null;
	
	public AnaDataSetFilterSetDlg(Container parent, AnaReportPlugin plugin,DataSetDefVO selDefVo){
		super(parent, StringResource.getStringResource("miufo00429"), true);
		this.m_plugin=plugin;
		this.selDataDef=selDefVo;
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getTopPanel(), BorderLayout.NORTH);
		getContentPane().add(getFilterSetPanel().getContentPanel(), BorderLayout.CENTER);
		getContentPane().add(getCmdPanel(), BorderLayout.SOUTH);
		pack();
		initData();
	}
	
	private void initData(){
		if (selDataDef != null) {
			getTxDataSetValue().setText(selDataDef.toString());
			FilterDescriptor filter = m_plugin.getModel().getDataSource()
					.getReportFilter(selDataDef.getPk_datasetdef());
			if (filter == null) {
				filter = new FilterDescriptor();
			} else {
				filter = (FilterDescriptor) filter.clone();
			}
			getFilterSetPanel().initData(selDataDef.getDataSetDef(), filter);
		}
	}
	private JPanel getTopPanel() {
		if (jDataSetInfoPanel == null) {
			jDataSetInfoPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlDataset=new UILabel(StringResource.getStringResource("miufo00241"));
			jlDataset.setPreferredSize(new Dimension(100,20));
			jDataSetInfoPanel.add(jlDataset);
			getTxDataSetValue().setPreferredSize(new Dimension(100,20));
			jDataSetInfoPanel.add(getTxDataSetValue());
		}
		return jDataSetInfoPanel;
	}
	
	private JTextField getTxDataSetValue(){
		if(jTxDataSetValue==null){
			jTxDataSetValue=new UITextField();
			jTxDataSetValue.setName("TxFindValue");
			jTxDataSetValue.setEditable(false);
		}
		return jTxDataSetValue;
	}
	private JPanel getCmdPanel() {
		if (jCmdPanel == null) {
			jCmdPanel = new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			jCmdPanel.add(getOKButton());
			jCmdPanel.add(getCancleButton());
		}
		return jCmdPanel;
	}
	private ExAreaFilterSetPanel getFilterSetPanel() {
		if (filterPanel == null) {
			filterPanel = new ExAreaFilterSetPanel();
		}
		return filterPanel;
	}
	private JButton getOKButton() {
		if (btnOK == null) {
			btnOK = new UIButton(MultiLang.getString("ok"));
			btnOK.addActionListener(this);
		}
		return btnOK;
	}

	private JButton getCancleButton() {
		if (btnCancel == null) {
			btnCancel = new UIButton(MultiLang.getString("cancel"));
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	/**
	 * @i18n iufobi00023=设置不合法请重新设置
	 * @i18n miufo1000155=错误
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getOKButton()) {//保存所有的并关闭			
			if(updataInfo()){
				FilterDescriptor filter = getFilterSetPanel().getFilterDescPanel().getDescriptor() ;
				if (filter != null&&this.selDataDef!=null) {
					m_plugin.getModel().getDataSource().setReportFilter(this.selDataDef.getPk_datasetdef(), filter);
					m_plugin.getModel().setDirty(true);
				}
				setResult(ID_OK);
				close();
				if(!m_plugin.getModel().isFormatState()){
					m_plugin.setDirty(true);
					m_plugin.refreshDataModel(true);
				}
			}else{
				UfoPublic.showErrorDialog(this, StringResource.getStringResource("iufobi00023"), StringResource.getStringResource("miufo1000155"));
				return ;
			}
			getFilterSetPanel().getFilterDescPanel().setDirty(false);
		}
		if (e.getSource() == getCancleButton()) {
			setResult(ID_CANCEL);
			close();
			getFilterSetPanel().getFilterDescPanel().setDirty(false);
		}
	}
	
	private boolean updataInfo() {
		if (getFilterSetPanel().getFilterDescPanel().getDescriptor() == null
				|| getFilterSetPanel().getFilterDescPanel().getDataSet() == null) {
			getFilterSetPanel().getFilterDescPanel().setDirty(false);
			return true;
		}
		getFilterSetPanel().getFilterDescPanel().stopTableEdit();
		if (getFilterSetPanel().getFilterDescPanel().isDirty()) {
			if (!getFilterSetPanel().updateInfo()) {
				return false;
			}
		}
		return true;
	}

}
 