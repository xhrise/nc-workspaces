package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;

/**
 * 一个简单设置统计字段信息的界面
 * 
 * @author ll
 * 
 */
public class CountFieldDefDlg extends UfoDialog {
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JTextField jNameField = null;
	private JComboBox jFldList = null;
	private JComboBox jRangeFldList = null;
	private JComboBox jCountTypeList = null;
	
//	private JRadioButton aggrBefore;
//	private JRadioButton aggrAfter;
	
	private JButton OKBtn = null;
	private JButton cancelBtn = null;

	private String[] m_fldShowNames = null;//所有统计依据字段
	private ArrayList<String> m_fldIDs = null;

	private Field[] m_countFlds = null;// 所有待选择字段
	private String[] m_countFldShowNames = null;
	private ArrayList<String> m_countFldIDs = null;
	private boolean m_hasRange =  false;
    //是否显示统计范围
	private boolean isShowCountList = false;
	
	/**
	 * This is the default constructor
	 */
	public CountFieldDefDlg(Container owner, Field[] countFlds, Field[] allFlds, boolean mustHasRange,boolean isShowCountList) {
		super(owner);
		m_hasRange = mustHasRange;
		this.isShowCountList = isShowCountList;
		m_fldIDs = new ArrayList<String>();
		if (allFlds != null) {

			m_fldShowNames = new String[allFlds.length];
			for (int i = 0; i < allFlds.length; i++) {
				m_fldShowNames[i] = allFlds[i].getCaption();
				m_fldIDs.add(allFlds[i].getFldname());
			}
		}

		
		m_countFlds = countFlds;
		m_countFldIDs = new ArrayList<String>();
		if (countFlds != null) {
			m_countFldShowNames = new String[countFlds.length];
			for (int i = 0; i < countFlds.length; i++) {
				if(countFlds[i] instanceof FieldCountDef){
					m_countFldShowNames[i]=((FieldCountDef)countFlds[i]).getMainFieldCaption();
					m_countFldIDs.add(((FieldCountDef)countFlds[i]).getMainFldName());
				}else{
					m_countFldShowNames[i] = countFlds[i].getCaption();
					m_countFldIDs.add(countFlds[i].getFldname());
				}	
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
		this.setTitle(StringResource.getStringResource(AnaFieldTypeExt.RESID_SETFIELD_CALC));
		this.setSize(400, 200);
		this.setContentPane(getJContentPane());
	}
	
	/**
	 * @i18n miufo1000751=值
	 */
	private void setNameFieldText(){
		Object selectFld = getFldList().getSelectedItem();
		Object selectCountType = getCountTypeList().getSelectedItem();
		getNameField().setText(selectFld+""+selectCountType+StringResource.getStringResource("miufo1000751"));
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

	/**
	 * @i18n miufo00450=统计名称
	 * @i18n uifreequery0011=统计字段
	 * @i18n miufo00451=统计类型
	 * @i18n miufo00452=小计范围
	 * @i18n miufo00453=统计范围
	 */
	private JPanel getMainPanel() {
		JPanel panel = new UIPanel();
		panel.setSize(300, 150);
		panel.setLayout(null);
		// panel.setLayout(new GridLayout(5, 2));

		JLabel lbl0 = new JLabel(StringResource.getStringResource("miufo00450"));
		lbl0.setBounds(20, 10, 80, 20);
		panel.add(lbl0);
		panel.add(getNameField());

		JLabel lbl1 = new JLabel(StringResource.getStringResource("uifreequery0011"));
		lbl1.setBounds(20, 40, 80, 20);
		panel.add(lbl1);
		panel.add(getFldList());

		JLabel lbl2 = new JLabel(StringResource.getStringResource("miufo00451"));
		lbl2.setBounds(20, 70, 80, 20);
		panel.add(lbl2);
		panel.add(getCountTypeList());

		JLabel lbl3 = new JLabel(); 
		if(m_hasRange)
			lbl3.setText(StringResource.getStringResource("miufo00452"));
		else
			lbl3.setText(StringResource.getStringResource("miufo00453"));		
		lbl3.setBounds(20, 100, 80, 20);
        
		if(isShowCountList){
			panel.add(lbl3);
			panel.add(getRangeFldList());
		}
//		if(m_hasRange){//小计位置
//			panel.add(getAggrPositionPanel());
//		}
		return panel;
	}

//	private JPanel getAggrPositionPanel(){
//		   JPanel aggrpanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
//		   aggrpanel.setBounds(20, 130, 240, 30);
//		   JLabel lbl4 = new JLabel("小计头位置:"); 
//		   aggrpanel.add(lbl4);
//		   aggrpanel.add(getAggrBefore());
//		   aggrpanel.add(getAggrAfter());
//		   return aggrpanel;
//	}
//	
//	private JRadioButton getAggrBefore(){
//		if(aggrBefore==null){
//			aggrBefore=new JRadioButton("前"); 
//		}
//		return aggrBefore;
//	}
//	private JRadioButton getAggrAfter(){
//		if(aggrAfter==null){
//			 aggrAfter=new JRadioButton("后");
//		}
//		return aggrAfter;
//	}
	
	private JTextField getNameField() {
		if (jNameField == null) {
			jNameField = new JTextField();
			jNameField.setBounds(120, 10, 160, 20);
			jNameField.setEditable(false);
		}
		return jNameField;
	}

	private JComboBox getFldList() {
		if (jFldList == null) {
			jFldList = new UIComboBox(m_countFldShowNames);
			jFldList.setBounds(120, 40, 160, 20);
			jFldList.addActionListener(new ActionListener(){//add by wangyga 2008-09-17
				public void actionPerformed(ActionEvent e) {
					setNameFieldText();
				}
				
			});
		}

		return jFldList;
	}

	private JComboBox getCountTypeList() {
		if (jCountTypeList == null) {
			jCountTypeList = new UIComboBox((new FieldCountDef()).getCountTypeNames());
			jCountTypeList.setBounds(120, 70, 160, 20);
			jCountTypeList.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					setNameFieldText();
				}
				
			});
		}

		return jCountTypeList;
	}

	private JComboBox getRangeFldList() {
		if (jRangeFldList == null) {
			String[] rangNames = null;
			if(m_hasRange){//必须选择分组依据
				rangNames = m_fldShowNames;
			}else{
				rangNames = new String[m_fldShowNames.length + 1];
				System.arraycopy(m_fldShowNames, 0, rangNames, 0, m_fldShowNames.length);
				rangNames[m_fldShowNames.length] = "";
				
			}
			jRangeFldList = new UIComboBox(rangNames);
			jRangeFldList.setBounds(120, 100, 160, 20);
		}
		return jRangeFldList;
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
					String msg = doCheckField();
					if (msg != null)
						UfoPublic.sendWarningMessage(msg, CountFieldDefDlg.this);						
					else {
						setResult(UfoDialog.ID_OK);
						close();
					}
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

	public void setClacFieldInfo(FieldCountDef def) {
		if (def != null) {
			int countIndex = m_countFldIDs.indexOf(def.getMainFldName());
			getFldList().setSelectedIndex(countIndex < 0 ? 0 : countIndex);
			getCountTypeList().setSelectedIndex(def.getCountType());
			if (def.getRangeFld() != null) {
				int	index = m_fldIDs.indexOf(def.getRangeFld());
				getRangeFldList().setSelectedIndex(index);
			} else {
				getRangeFldList().setSelectedIndex(getRangeFldList().getModel().getSize() - 1);
			}
			setNameFieldText();
//			this.getAggrBefore().setSelected(def.isBefore());
//			this.getAggrAfter().setSelected(def.isAfter());
		}
	}

	/*
	 * 校验选择的统计类型和字段数据类型的匹配关系
	 */
	/**
	 * @i18n miufo00279=只能针对数值类型的字段计算 
	 */
	private String doCheckField() {
		int type = getCountTypeList().getSelectedIndex();
		int fldIndex = getFldList().getSelectedIndex();		
		Field fld = m_countFlds[fldIndex];
		int iFldType = fld.getDataType();
		if (!(type == IFldCountType.TYPE_COUNT || type == IFldCountType.TYPE_MAX || type == IFldCountType.TYPE_MIN)) {// 除了计数,最大，最小之外，其他统计类型都要求字段是数值类型
			if (!DataTypeConstant.isNumberType(iFldType)) {
				return StringResource.getStringResource("miufo00279") + getCountTypeList().getSelectedItem();
			}
		}
			
		return null;
	}

	public FieldCountDef getCalcInfo() {
		String name = getNameField().getText();
		Field countFld = m_countFlds[getFldList().getSelectedIndex()];
		int rangeIndex = getRangeFldList().getSelectedIndex();
        
		FieldCountDef def = new FieldCountDef(countFld, getCountTypeList().getSelectedIndex(), rangeIndex >= m_fldIDs.size() ? null : m_fldIDs.get(rangeIndex),rangeIndex >= m_fldShowNames.length ? null : m_fldShowNames[rangeIndex], name);
//		def.setBefore(getAggrBefore().isSelected());
//		def.setAfter(getAggrAfter().isSelected());
		return def;
	}

}
 