/*
 * 创建日期 2006-5-15
 */
package com.ufida.report.multidimension.calc;
import com.ufida.iufo.pub.tools.AppDebug;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.vo.bi.base.util.IDMaker;

import com.ufida.report.multidimension.model.CalcMemberVO;
import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.script.exception.ParseException;
/**
 * @author ljhua
 */
public class MultiDimCalcDlg extends UfoDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String NAME_LABEL_MEMBER="uibimultical013";//成员
	
	private CalcMemberVO m_calcMem=null;
	private MultiDimCalcService m_calcSrv=null;
	private SelDimModel m_seldimModel = null;
	/**
	 * 对话框初始打开时的维度
	 */
	private String m_strDimPK=null;
	/**
	 * 行或列类型
	 */
	private int m_combineType=-1;
	
	/**
	 * 行或列的所有组合.
	 */
	private DimMemberCombinationVO[] m_fields=null;
	
	private javax.swing.JPanel jContentPane = null;

	private JLabel jLabel = null;
	private JTextField jTxtName = null;
	private JCheckBox jCheckBox = null;
	private JPanel jPanel = null;
	private JComboBox jCombMem = null;
	private JComboBox jcombPos = null;
	private JPanel jPanel1 = null;
	private JButton jbtnOK = null;
	private JButton jBtnCancel = null;
	

	private DefFormulaPane formPane = null;
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return JTextField	
	 */    
	private JTextField getTxtName() {
		if (jTxtName == null) {
			jTxtName = new UITextField();
			jTxtName.setBounds(82, 11, 334, 18);
			jTxtName.setBackground(java.awt.Color.white);
		}
		return jTxtName;
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new UICheckBox();
			jCheckBox.setName("");
			jCheckBox.setText(StringResource.getStringResource(CalcLanguageRes.STR_ROWCOL));
			jCheckBox.setBounds(306, 34, 107, 21);
			jCheckBox.setSelected(false);
			jCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getFormPane().setFormula(null);
					String[][] elements=getSelectedMembers(jCheckBox.isSelected());
					getFormPane().setMemberNames(elements);
					String strDim=getCalcValidDim();
					initCombMember(strDim);
				
				}});
		}
		return jCheckBox;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n miufopublic476=位置
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED), StringResource.getStringResource("miufopublic476"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanel.setBounds(82, 60, 334, 52);
			jPanel.add(getCombMem(), null);
			jPanel.add(getCombPos(), null);
		}
		return jPanel;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCombMem() {
		if (jCombMem == null) {
			jCombMem = new UIComboBox();
			jCombMem.setBounds(229, 18, 94, 27);
		}
		return jCombMem;
	}
	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCombPos() {
		if (jcombPos == null) {
			jcombPos = new UIComboBox();
			jcombPos.setBounds(31, 18, 94, 27);
			jcombPos.addItem(StringResource.getStringResource(CalcLanguageRes.STR_POS_LAST));
			jcombPos.addItem(StringResource.getStringResource(CalcLanguageRes.STR_POS_FIRST));
			jcombPos.addItem(StringResource.getStringResource(CalcLanguageRes.STR_POS_MEM_BEFORE));
			jcombPos.addItem(StringResource.getStringResource(CalcLanguageRes.STR_POS_MEM_LAST));
			jcombPos.addActionListener(this);
			
		}
		return jcombPos;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new UIPanel();
			jPanel1.setLayout(null);
			jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
			jPanel1.setBounds(0, 0, 428, 407);
			jPanel1.add(jLabel, null);
			jPanel1.add(getTxtName(), null);
			jPanel1.add(getJCheckBox(), null);
			jPanel1.add(getJPanel(), null);
			jPanel1.add(getFormPane(), null);
		}
		return jPanel1;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnOK() {
		if (jbtnOK == null) {
			jbtnOK = new nc.ui.pub.beans.UIButton();
			jbtnOK.setBounds(434, 10, 75, 22);
			jbtnOK.setText(StringResource.getStringResource("miufopublic246"));
			jbtnOK.addActionListener(this);
		
		}
		return jbtnOK;
	}
	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */    
	private JButton getBtnCancel() {
		if (jBtnCancel == null) {
			jBtnCancel = new nc.ui.pub.beans.UIButton();
			jBtnCancel.setBounds(434, 49, 75, 22);
			jBtnCancel.setText(StringResource.getStringResource("miufopublic247"));
			jBtnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(UfoDialog.ID_CANCEL);
					close();
				}
			});
		}
		return jBtnCancel;
	}
	/**
	 * This method initializes formPane	
	 * 	
	 * @return com.ufida.report.multidimension.calc.DefFormulaPane	
	 */    
	private DefFormulaPane getFormPane() {
		if (formPane == null) {
			formPane = new DefFormulaPane();
			formPane.setBounds(0, 115, 418, 287);
		}
		return formPane;
	}
            	public static void main(String[] args) {
	}
	/**
	 * This is the default constructor
	 */
	public MultiDimCalcDlg(Container parent, SelDimModel seldimModel,MultiDimCalcService calcSrv,DimMemberCombinationVO[] fields,CalcMemberVO calcMember) {
		super(parent);

		if(calcSrv==null)
			throw new IllegalArgumentException("calcSrv must be not null");
		m_calcSrv=calcSrv;
		if(seldimModel==null)
			throw new IllegalArgumentException("seldimModel must be not null");
		m_seldimModel=seldimModel;

		m_calcMem=calcMember==null?null:calcMember.copy();
		m_fields=fields;

		initialize();
	}
	
	public MultiDimCalcDlg(Container parent, SelDimModel seldimModel,MultiDimCalcService calcSrv,DimMemberCombinationVO[] fields,String strDimPK,int iCombineType) {
		super(parent);

		if(calcSrv==null)
			throw new IllegalArgumentException("calcSrv must be not null");
		m_calcSrv=calcSrv;
		if(seldimModel==null)
			throw new IllegalArgumentException("seldimModel must be not null");
		m_seldimModel=seldimModel;

		m_fields=fields;
		m_strDimPK=strDimPK;
		m_combineType=iCombineType;

		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(510, 432);
		this.setContentPane(getJContentPane());
		
		boolean bRowCol=false;
		String strUserDefForm=null;
		String strNeighborId=null;
		
		if(m_calcMem!=null){
			bRowCol=m_calcMem.isRowColCalc();
			getJCheckBox().setSelected(m_calcMem.isRowColCalc());
			getTxtName().setText(m_calcMem.getName());
			m_strDimPK=m_calcSrv.getCalcMemValidDim(m_calcMem.isRowDirect(),m_calcMem.isRowColCalc(),m_calcMem.getDimID());
			m_combineType=m_calcMem.getCombineType();
			
			//neighbor
			strNeighborId=m_calcMem.getNeighborName();
			
			if(m_calcMem.getPosType()>0){
				getCombPos().setSelectedIndex(m_calcMem.getPosType()-1);
			}

			strUserDefForm=m_calcSrv.getUserDefFormula(m_calcMem);
			
		}
		initCombMember(m_strDimPK);
		if(strNeighborId!=null){
			IMember neighborMem=m_calcSrv.getMemberById(m_strDimPK,strNeighborId);
			getCombMem().setSelectedItem(neighborMem);
		}
		setCombMemEnable();
		
		String[][] elements=getSelectedMembers(bRowCol);
		getFormPane().initData(new MultiDimCalcElements(elements),strUserDefForm,StringResource.getStringResource(NAME_LABEL_MEMBER));
			
	}
	/**
	 * 获得公式可选择的成员名集合.
	 * @param bRowCol
	 * @return
	 */
	private String[][] getSelectedMembers(boolean bRowCol){
		String[][] strNames=null;
		if(bRowCol==true){
			if(m_fields!=null && m_fields.length>0){
				strNames=new String[m_fields.length][2];
				for(int i=0,size=m_fields.length;i<size;i++){
					strNames[i][0]=MultiDimCalcUtil.getUserName(m_fields[i]);
					strNames[i][1]=MultiDimCalcUtil.getFormulaName(m_fields[i]);
				}
			}
		}else{
			IMember[] members=getAllMemberByDim(m_strDimPK);
			strNames=getMemberNames(members);
		}
		return strNames;
	}
	
	private String[][] getMemberNames(IMember[] vos){
		String[][] strNames=null;
		if(vos!=null &&  vos.length>0){
			int iLen=vos.length;
			strNames=new String[iLen][2];
			for(int i=0;i<iLen;i++){
				strNames[i][0]=vos[i].getName();
				strNames[i][1]="['"+vos[i].getName()+"']";
			}
		}
		return strNames;
	}
	/**
	 * 获得维度的所有成员
	 * @param strDimId
	 * @return
	 */
	private  IMember[] getAllMemberByDim(String strDimId){
		if(strDimId==null)
			return null;
		SelDimensionVO selDim=m_seldimModel.getSelDimension(strDimId);
		IMember[] dimMembers=null;
		if(selDim!=null){
			dimMembers= selDim.getAllMembers();
		}

		//可用计算列

		CalcMemberVO[] calcMembers=m_calcSrv.getValidCalcMemByDim(strDimId,isOnlyEnableCalc());
		ArrayList<IMember> listAll=new ArrayList<IMember>();
		if( calcMembers!=null && calcMembers.length>0){
			for(int i=0,size=calcMembers.length;i<size;i++){
				if(calcMembers[i]==null)
					continue;
				
				if(m_calcMem!=null && calcMembers[i].getID().equals(m_calcMem.getID()))
					continue;
				listAll.add(calcMembers[i]);
			}
		}

		if(dimMembers!=null && dimMembers.length>0){
			listAll.addAll(Arrays.asList(dimMembers));
		}
		IMember[] members=null;
		if(listAll.size()>0){
			 members=new IMember[listAll.size()];
			 listAll.toArray(members);
		}

		return members;
		
	}
	private boolean isOnlyEnableCalc(){
		if(m_calcMem==null)
			return true;
		return m_calcMem.isEnabled();
		
	}

	private void initCombMember(String strDimId){
		if(strDimId==null)
			return;
		
		getCombMem().removeAllItems();
		
		IMember[] members=getAllMemberByDim(strDimId);
		if( members.length>0){
			for(int i=0,size=members.length;i<size;i++){
				getCombMem().addItem(members[i]);
			}
		}
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jLabel = new nc.ui.pub.beans.UILabel();
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jLabel.setText(StringResource.getStringResource(CalcLanguageRes.LABEL_NAME));//名称
			jLabel.setBounds(16, 11, 59, 18);
			jLabel.setBackground(new java.awt.Color(204,204,204));
			jLabel.setForeground(java.awt.Color.BLACK);
			jContentPane.add(getJPanel1(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getBtnCancel(), null);
		}
		return jContentPane;
	}
	private String getCalcValidDim(){
		boolean bRowCol=getJCheckBox().isSelected();
		String strDimPK=m_strDimPK;
		
		//对于行/列方式,按照第一维度检查名称
		boolean bRow=m_combineType==CalcMemberVO.POS_COLUMN?false:true;
		if(bRowCol){
			strDimPK=m_calcSrv.getCalcMemValidDim(bRow,bRowCol,m_strDimPK);
		}
		return strDimPK;
	}
	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==getBtnOK()){
			String strName=getTxtName().getText();
			if(strName==null || strName.trim().equals("")){
				JOptionPane.showMessageDialog(this,StringResource.getStringResource(CalcLanguageRes.STR_MSG_NAME));
				getTxtName().requestFocus();
				return;
			}
			String strCalcId=m_calcMem==null?null:m_calcMem.getID();
			
			boolean bRowCol=getJCheckBox().isSelected();
			String strDimPK=m_strDimPK;
			boolean bRow=m_combineType==CalcMemberVO.POS_COLUMN?false:true;
			
			//对于行/列方式,按照第一维度检查名称
			if(bRowCol){
				strDimPK=m_calcSrv.getCalcMemValidDim(bRow,bRowCol,m_strDimPK);
			}

			String strErrMsg=m_calcSrv.checkName(m_strDimPK,strCalcId,strName);
			if(strErrMsg!=null){
				JOptionPane.showMessageDialog(this,StringResource.getStringResource(CalcLanguageRes.STR_MSG_NAME));
				getTxtName().requestFocus();
				return;
			}
			String strForm=getFormPane().getFormula();
			String strDbForm=null;
			
			//对于行/列方式,维度由iPos确定.
			if(bRowCol)
				strDimPK=null;
			
			boolean bValid=true;
			if(m_calcMem!=null)
				bValid=m_calcMem.isValid();
			try {
				
				strDbForm=m_calcSrv.checkFormula(strDimPK,strCalcId,strForm,bRow,bRowCol,isOnlyEnableCalc(),bValid);
			} catch (ParseException e1) {
				AppDebug.debug(e1);
				JOptionPane.showMessageDialog(this,e1.getMessage());
				return;
			}
			if(m_calcMem==null){
				m_calcMem=new CalcMemberVO(m_combineType);
				m_calcMem.setID(IDMaker.makeID(20));
			}
			m_calcMem.setDimid(strDimPK);
			m_calcMem.setRowOrCol(bRowCol);
			
			m_calcMem.setNeighborName(((IMember) getCombMem().getSelectedItem()).getMemberID());
			m_calcMem.setPosType(getCombPos().getSelectedIndex()+1);
			m_calcMem.setFormula(strDbForm);
			m_calcMem.setName(strName);

			//当新建时，未设置计算顺序。
			
			setResult(UfoDialog.ID_OK);
			close();
		}
		else if(e.getSource()==getCombPos()){
			setCombMemEnable();
		}
		
	}
	private void setCombMemEnable(){
		if(getCombPos().getSelectedIndex()==0 || getCombPos().getSelectedIndex()==1){
			getCombMem().setEnabled(false);
		}
		else
			getCombMem().setEnabled(true);
	}
	public CalcMemberVO getCalcMember(){
	
		return m_calcMem;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"

 