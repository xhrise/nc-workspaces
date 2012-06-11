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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.IRankFuncSet;
import com.ufida.report.anareport.model.RankFuncInfo;
import com.ufida.report.crosstable.CrossMeasureRankFuncSet;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * 设置TopN分析的界面
 * 
 * @author ll
 * 
 */
public class RankFunctionDlg extends UfoDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JCheckBox jEnabledRank = null;// 是否启用
	private JPanel jFieldPanel = null;// 属性面板
	private JCheckBox jDesceding = null;// 从大到小
	private JComboBox jRange = null;// 排名范围
	private JCheckBox jSameRank = null;// 允许并列排名

	private IRankFuncSet m_rank = null;

	private JButton OKBtn = null;
	private JButton cancelBtn = null;

	private String[] m_fldNames = null;
	private Field[] m_flds = null;//排名范围字段
	private boolean isCrossMeasure=false;

	/**
	 * This is the default constructor
	 */
	public RankFunctionDlg(Container owner,Field[] rangFlds,boolean iscross) {
		super(owner);
		isCrossMeasure=iscross;
		if(iscross){
			m_fldNames = new String[rangFlds.length];
			m_flds=new Field[rangFlds.length];
			for (int i = 0; i < rangFlds.length; i++) {
				m_fldNames[i] = rangFlds[i].getCaption();
				m_flds[i]=rangFlds[i];
			}
		}else{
			m_fldNames = new String[rangFlds.length + 1];
			m_flds=new Field[rangFlds.length+1];
			m_fldNames[0] = "";
			m_flds[0]=null;
			for (int i = 0; i < rangFlds.length; i++) {
				m_fldNames[1 + i] = rangFlds[i].getCaption();
				m_flds[1+i]=rangFlds[i];
			}
		}
		
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource(RankFunctionExt.RESID_RANK_DESIGN));
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
		panel.add(getEnabledRankBox(), BorderLayout.NORTH);

		panel.add(getFieldPanel(), BorderLayout.CENTER);
		return panel;
	}

	private JPanel getFieldPanel() {
		if (jFieldPanel == null) {
			jFieldPanel = new RankSetPanel();
		}
		return jFieldPanel;
	}
	/**
	 * @i18n miufopublic289=启用
	 */
	private JCheckBox getEnabledRankBox() {
		if (jEnabledRank == null) {
			jEnabledRank = new UICheckBox(StringResource.getStringResource("miufopublic289")+StringResource.getStringResource(RankFunctionExt.RESID_RANK_DESIGN));
			jEnabledRank.addActionListener(this);
		}
		return jEnabledRank;
	}

	private JCheckBox getDesceding() {
		if (jDesceding == null) {
			jDesceding = new UICheckBox(StringResource.getStringResource(RankFunctionExt.RESID_RANK_DESCEDING));
			jDesceding.addActionListener(this);
		}
		return jDesceding;
	}
	private JCheckBox getSameRank() {
		if (jSameRank == null) {
			jSameRank = new UICheckBox(StringResource.getStringResource(RankFunctionExt.RESID_RANK_SAME));
			jSameRank.addActionListener(this);
		}
		return jSameRank;
	}
	private JComboBox getRangeBox() {
		if (jRange == null) {
			jRange = new UIComboBox(m_fldNames);
			jRange.addActionListener(this);
		}
		return jRange;
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
					String msg = checkRank();
					if (msg != null) {
						MessageDialog.showHintDlg(RankFunctionDlg.this, null, msg);
						return;
					}
					getRankFromUI();
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

	private int getFieldNameIndex(String fld) {
		if (fld == null || fld.length() == 0)
			return 0;
		for (int j = 0; j < m_flds.length; j++) {
			if (m_flds[j]!=null&&fld.equals(m_flds[j].getFldname()))
				return j;
		}
		return 0;
	}

	private void setEnabledRank(boolean isEnable) {
		getEnabledRankBox().setSelected(isEnable);
		getFieldPanel().setEnabled(isEnable);
	}

	/**
	 * 
	 * @param rank rank不要为Null,否则new一个设置
	 */
	public void setRankInfo(IRankFuncSet rank) {
		m_rank = rank;
		if (rank == null) {
			setEnabledRank(false);
			return;
		}
		setEnabledRank(rank.isEnabled());
		getDesceding().setSelected(!rank.isASC());
		getSameRank().setSelected(rank.isSameRank());
		int index = getFieldNameIndex(rank.getRange());
		getRangeBox().setSelectedIndex(index);
	}
	
	public IRankFuncSet getRankInfo() {
		return m_rank;
	}

	private void getRankFromUI() {
		if (m_rank == null){
			if(isCrossMeasure){
				m_rank=new CrossMeasureRankFuncSet();
			}else{
				m_rank=new RankFuncInfo();
			}
		}

		m_rank.setEnabled(getEnabledRankBox().isSelected());
		if (!getEnabledRankBox().isSelected())
			return;
		m_rank.setASC(!getDesceding().isSelected());
		m_rank.setSameRank(getSameRank().isSelected());
		int index = getRangeBox().getSelectedIndex();
		String fldRange = m_flds[index] ==null ? null : m_flds[index].getFldname();
		m_rank.setRange(fldRange);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getEnabledRankBox()) {// 根据是否启用设置属性面板的可用性
			if (getEnabledRankBox().isSelected())
				getFieldPanel().setEnabled(true);
			else
				getFieldPanel().setEnabled(false);
		}

	}

	private String checkRank() {
		if (!getEnabledRankBox().isSelected())
			return null;
		// TODO 排名范围只能选择有效的分组字段，或许不用检查，无效的就当作默认当前范围好了
		return null;
	}

	private class RankSetPanel extends UIPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
        
		public RankSetPanel(){
			GridBagLayout gridbaglayout = new GridBagLayout();
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.weighty=1.0D;
			gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
			gridbagconstraints.insets=new Insets(0,2,0,2);
			setLayout(gridbaglayout);
			JPanel orderPanel=new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			JLabel lbl1 = new JLabel(StringResource.getStringResource(RankFunctionExt.RESID_RANK_ORDER));
			orderPanel.add(lbl1);
			orderPanel.add(getDesceding());
			gridbagconstraints.gridwidth = GridBagConstraints.RELATIVE;
			gridbaglayout.setConstraints(orderPanel, gridbagconstraints);
			add(orderPanel);
			
			gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridbaglayout.setConstraints(getSameRank(), gridbagconstraints);
			add(getSameRank());
			JPanel rankPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel lbl2 = new JLabel(StringResource.getStringResource(RankFunctionExt.RESID_RANK_RANGE));
			rankPanel.add(lbl2);
			rankPanel.add(getRangeBox());
			gridbaglayout.setConstraints(rankPanel, gridbagconstraints);
			add(rankPanel);
		}

		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			getDesceding().setEnabled(enabled);
			getSameRank().setEnabled(enabled);
			getRangeBox().setEnabled(enabled);
		}
		
	}
}
 