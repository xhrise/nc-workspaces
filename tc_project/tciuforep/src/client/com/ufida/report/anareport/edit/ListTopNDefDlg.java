package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.model.TopNSetInfo;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * 设置TopN分析的界面
 * 
 * @author ll
 * 
 */
public class ListTopNDefDlg extends UfoDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JCheckBox jEnabledTopN = null;// 是否启用
	private TopNSetPanel jFieldPanel = null;// 属性面板
	private UITextField jTextN = null;// N
	private JCheckBox jDesceding = null;// 从大到小
	private JCheckBox jExtendToN = null;// 强行占行
	private JCheckBox jShowElse = null;// 显示其他
	private TopNElseDesPanel jElsePanel = null;// 其他的设置面板
	private Field[] all_flds = null;

	private TopNSetInfo m_topN = null;
    private Field field=null;//要设置TopN的字段
	private JButton OKBtn = null;
	private JButton cancelBtn = null;

	/**
	 * This is the default constructor
	 */
	public ListTopNDefDlg(Container owner,TopNSetInfo topNInfo,Field field,Field[] flds) {
		super(owner);
		this.field=field;
		all_flds = flds;
		m_topN=topNInfo;
		initialize();
		updateTopNUI();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource(TopNDesignExt.RESID_TOPN_DESIGN));
		this.setContentPane(getJContentPane());
		this.setResizable(false);
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
		panel.setLayout(new BorderLayout());
		JPanel boxPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
		boxPanel.add(getEnabledTopNBox());
		panel.add(boxPanel, BorderLayout.NORTH);
		panel.add(getTopNSetPanel(), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * @i18n miufo00253=数据分析行数
	 */
	private TopNSetPanel getTopNSetPanel() {
		if (jFieldPanel == null) {
			jFieldPanel = new TopNSetPanel();
		}
		return jFieldPanel;
	}

	private TopNElseDesPanel getElsePanel() {
		if (jElsePanel == null) {
			if(this.getTopNInfo()==null){
				jElsePanel = new TopNElseDesPanel(this.all_flds,null);
			}else{
				jElsePanel = new TopNElseDesPanel(this.all_flds,this.getTopNInfo().getElseFields());
			}
			
		}
		return jElsePanel;
	}



	/**
	 * @i18n miufopublic289=启用
	 */
	private JCheckBox getEnabledTopNBox() {
		if (jEnabledTopN == null) {
			jEnabledTopN = new JCheckBox(StringResource.getStringResource("miufopublic289")+StringResource.getStringResource(TopNDesignExt.RESID_TOPN_DESIGN));
			jEnabledTopN.addActionListener(this);
		}
		return jEnabledTopN;
	}

	private UITextField getTextN() {
		if (jTextN == null) {
			jTextN = new UITextField();
			jTextN.setTextType("TextInt");
//			jTextN.setMinValue(1);
		}
		return jTextN;
	}

	private JCheckBox getDesceding() {
		if (jDesceding == null) {
			jDesceding = new JCheckBox(StringResource.getStringResource(TopNDesignExt.RESID_TOPN_DESCEDING));
			jDesceding.addActionListener(this);
		}
		return jDesceding;
	}

	private JCheckBox getExtendToN() {
		if (jExtendToN == null) {
			jExtendToN = new JCheckBox(StringResource.getStringResource(TopNDesignExt.RESID_TOPN_EXTEND));
			jExtendToN.addActionListener(this);
		}
		return jExtendToN;
	}

	private JCheckBox getShowElse() {
		if (jShowElse == null) {
			jShowElse = new JCheckBox(StringResource.getStringResource(TopNDesignExt.RESID_TOPN_SHOWELSE));
			jShowElse.addActionListener(this);
		}
		return jShowElse;
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
			OKBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getElsePanel().stopTableEdit();
					String msg = checkTopNSet();
					if (msg != null) {
						MessageDialog.showHintDlg(ListTopNDefDlg.this, null, msg);
						return;
					}
					getTopNFromUI();
					setResult(UfoDialog.ID_OK);
					close();
				}
			});
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
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(UfoDialog.ID_CANCEL);
					close();
				}
			});
		}
		return cancelBtn;
	}

	private void updateTopNUI() {
		if (m_topN == null) {
			setEnabledTopN(false);
			getElsePanel().setEnabled(false);
			return;
		}
		getTextN().setText(String.valueOf(m_topN.getN()));
		getDesceding().setSelected(!m_topN.isASC());
		getExtendToN().setSelected(m_topN.isExtendN());
		getShowElse().setSelected(m_topN.isShowElse());
		setEnabledTopN(m_topN.isEnabled());
	}

	private void setEnabledTopN(boolean isEnable) {
		getEnabledTopNBox().setSelected(isEnable);
		getTopNSetPanel().setEnabled(isEnable);
	}

	public TopNSetInfo getTopNInfo() {
		return m_topN;
	}

	private void getTopNFromUI() {
		if (m_topN == null)
			m_topN = new TopNSetInfo();

		m_topN.setEnabled(getEnabledTopNBox().isSelected());
		if (!getEnabledTopNBox().isSelected())
			return;
		m_topN.setN(Integer.parseInt(getTextN().getText()));
		m_topN.setASC(!getDesceding().isSelected());
		m_topN.setExtendN(getExtendToN().isSelected());
		m_topN.setShowElse(getShowElse().isSelected());
		if (m_topN.isShowElse()) {
			m_topN.getElseFields().clear();
			m_topN.setElseFields(getElsePanel().getElseFieldList());
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getEnabledTopNBox()) {// 根据是否启用设置属性面板的可用性
			if (getEnabledTopNBox().isSelected())
				getTopNSetPanel().setEnabled(true);
			else
				getTopNSetPanel().setEnabled(false);
		}
		 else if (e.getSource() == getShowElse()) {
			if (getShowElse().isSelected())
				getElsePanel().setEnabled(true);
			else
				getElsePanel().setEnabled(false);
		}

	}

	/**
	 * @i18n miufo00254=请输入分析数据的行数
	 */
	private String checkTopNSet() {
		if (!getEnabledTopNBox().isSelected())
			return null;

		String strN = getTextN().getText();
		if (strN == null || strN.trim().length() == 0)
			return StringResource.getStringResource("miufo00254");
		Integer n = Integer.valueOf(strN);
		if(n<=0)
			return StringResource.getStringResource("miufopublic389");//miufopublic389=请输入大于0的整数！
		return null;
	}
	private UIButton createButton(String s, ActionListener actionlistener)
    {
        UIButton jbutton = new UIButton(s);
        jbutton.addActionListener(actionlistener);
        return jbutton;
    }
	
	private class TopNSetPanel extends UIPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TopNSetPanel(){
			super();
			GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.weighty=1.0D;
			gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
			gridbagconstraints.insets=new Insets(0,2,0,2);
			setLayout(gridbaglayout);
			JPanel topNPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel lblN = new JLabel(StringResource.getStringResource(StringResource.getStringResource("miufo00253")));
			topNPanel.add(lblN);
			topNPanel.add(getTextN());
			topNPanel.add(getDesceding());
			gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridbaglayout.setConstraints(topNPanel, gridbagconstraints);
			add(topNPanel);
			gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
			JPanel box1Panel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			box1Panel.add(getExtendToN());
			gridbaglayout.setConstraints(box1Panel, gridbagconstraints);
			add(box1Panel);
			JPanel box2Panel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			box2Panel.add(getShowElse());
			gridbaglayout.setConstraints(box2Panel, gridbagconstraints);
			add(box2Panel);
			gridbaglayout.setConstraints(getElsePanel(), gridbagconstraints);
			add(getElsePanel());
		}

		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			getTextN().setEnabled(enabled);
			getDesceding().setEnabled(enabled);
			getExtendToN().setEnabled(enabled);
			getShowElse().setEnabled(enabled);
			getElsePanel().setEnabled(enabled&&getShowElse().isSelected());
		}
		
	}
	
}
  