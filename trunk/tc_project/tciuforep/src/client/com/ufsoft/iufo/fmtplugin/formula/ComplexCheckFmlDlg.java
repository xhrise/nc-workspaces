/*
 * 创建日期 2006-6-5
 */
package com.ufsoft.iufo.fmtplugin.formula;


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.vo.iufo.pub.InputvalueCheck;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.fmtplugin.formula.CommonFmlEditDlg;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditor;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.script.function.UfoFuncInfo;
import com.ufsoft.table.CellsModel;
/**
 * @author ljhua
 */
public class ComplexCheckFmlDlg extends UfoDialog implements ActionListener {

	private static final int MAX_NAME_LENGHT = 64;
	private static String NEWLINE = "\n";
	

	
	private final static String[] strOper=new String[]{
			"+","-","*","/",
			">",">=","<","<=","<>",
			"and","or","like"	
	};
	
	public static int ID_WIZARDFORMULA = 6;
	private int m_iFormulaCaretPos=0;
	
	private class ListMouseListener implements MouseListener{
		public ListMouseListener(){}
		public void mouseClicked(MouseEvent e) {
			if((e.getSource() instanceof JList)==false)
				return;
			if ((e != null) && (e.getClickCount() >= 2)) {
				Object obj = ((JList) e.getSource()).getSelectedValue();
				if(obj ==null)
					return;
				String str=null;
				int iCaretPos=0;
				if(obj instanceof Object[]){
					Object[] temp=(Object[]) obj;
					if(temp.length>=2){
						if(temp[1] instanceof String){
							str=(String) temp[1];
						}
					}
					if(temp.length>=4 && temp[3] instanceof Integer){
						iCaretPos=((Integer) temp[3]).intValue();
					}
	
				}
				else if(obj instanceof String)
					str=(String) obj;
				
				if (str != null) {
					if(iCaretPos==0)
						iCaretPos=str.length();
					addString(str,iCaretPos);
				}
			}

		}
		private void addString(String str,int iRealtiveCaretPos) {
			if (str != null) {
				int insertPos =getTxAreaFml().getCaretPosition();
				try {
					getTxAreaFml().insert(str, insertPos);
				} catch (Exception e1) {
					getTxAreaFml().append(str);
				}
				getTxAreaFml().setCaretPosition(insertPos + iRealtiveCaretPos);
			}
		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}
	}
	
	private javax.swing.JPanel jContentPane = null;

	private JLabel jLabel1 = null;
	private JTextField txtName = null;
	private JLabel jLabel2 = null;
	private JTextArea txAreaFml = null;
	private nc.ui.pub.beans.UITabbedPane jTabbedPane = null;
	private JPanel jPanel = null;
	private JScrollPane jScrollPane = null;
	private JList listCheckCond = null;
	private JScrollPane jScrollPane1 = null;
	private JPanel jPanel1 = null;
	private JScrollPane jScrollPane2 = null;
	private JList listFuncModule = null;
	private JScrollPane jScrollPane3 = null;
	private JList listFunc = null;
	private JScrollPane jScrollPane4 = null;
	private JButton btnOK = null;
	private JButton btnCancel = null;
	private JPanel jPanel2 = null;
	private JScrollPane jScrollPane5 = null;
	private JList listOper = null;
	private JPanel jPanel3 = null;
	private JLabel lblErr = null;
	private JTextArea txtAreaCheckNote = null;
	
	private DefaultListModel model = new DefaultListModel();

//	private UfoReport m_report = null;

	private FuncListInst m_ufoFuncList = null;

	//经过语法解析后的公式信息
	private String m_strParsedCheckFm = null;
    /**
     * 审核公式名称,3.1增加
     */
    private String m_strName = null;

	private JScrollPane jScrollPane6 = null;
	private JTextArea txtFuncInfo = null;
	private JScrollPane jScrollPane7 = null;
	
	private CellsModel cellsModel;
	public ComplexCheckFmlDlg(Container report,CellsModel cellsModel){
	    super(report);
//	    m_report = report;
	    this.cellsModel = cellsModel;
	    setTitle(StringResource.getStringResource("miufo1000912"));  //"新建表内审核公式"
	    m_ufoFuncList=getFuncList();
	    
	    initialize();

	}
	
	public ComplexCheckFmlDlg(Container report, CellsModel cellsModel,RepCheckVO checkVO) {
		super(report);
		setTitle(StringResource.getStringResource("miufo1000913")); //"修改表内审核公式"
//		m_report = report;
		this.cellsModel = cellsModel;
		m_ufoFuncList = getFuncList();
		
		initialize();

		if (checkVO != null) {
			getTxtName().setText(checkVO.getName());
			String strFormula = checkVO.getFormula();
			try {
				strFormula = parseFormula(strFormula, false);
			} catch (Exception e) {
				AppDebug.debug(e);
			}
			if (strFormula != null) {
				//替换所有/r的字符串
				StringBuffer sb = new StringBuffer();
				char[] allChar = strFormula.toCharArray();
				for (int i = 0; i < allChar.length; i++) {
					if (allChar[i] == '\r') {
						continue;
					}
					sb.append(allChar[i]);
				}
				strFormula = sb.toString();
			}
			getTxAreaFml().setText(strFormula);
		}
	}
	private String parseFormula(String strFormula, boolean bUserDef) throws Exception{
		FormulaModel formulaModel = FormulaModel.getInstance(cellsModel);
		UfoFmlExecutor ufoFmlExecutor = formulaModel.getUfoFmlExecutor();
		
		return ufoFmlExecutor.parseRepCheckFormula(strFormula,bUserDef);
//        FormulaDefPlugin pi = (FormulaDefPlugin) m_report.getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//        return  pi.getFmlExecutor().parseRepCheckFormula(strFormula,bUserDef);

   }
	private boolean  parseCmd(String strFormula) {
		try{
//			FormulaDefPlugin pi = (FormulaDefPlugin) m_report.getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
			getFormulaExecutor().parseCheckCmd(strFormula);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	private UfoFmlExecutor getFormulaExecutor(){
		FormulaModel formulaModel = FormulaModel.getInstance(cellsModel);
		return formulaModel.getUfoFmlExecutor();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(625, 550);
		this.setContentPane(getJContentPane());
		this.setResizable(false);
		setLocation(0,0);
		setLocationRelativeTo(getParent());
		initCheckNote();
		initFunc((UfoSimpleObject) getListFuncModule().getSelectedValue());


	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			lblErr = new nc.ui.pub.beans.UILabel();
			lblErr.setForeground(Color.RED);
			jLabel2 = new nc.ui.pub.beans.UILabel();
			jLabel1 = new nc.ui.pub.beans.UILabel();
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jLabel1.setText(StringResource.getStringResource("uiufochk002"));
			jLabel1.setBounds(33, 12, 118, 21);
			jLabel2.setBounds(33, 40, 118, 19);
			jLabel2.setText(StringResource.getStringResource("miufo1000923"));
			lblErr.setText("");
			jContentPane.add(jLabel1, null);
			jContentPane.add(getTxtName(), null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(getJTabbedPane(), null);
			jContentPane.add(getJScrollPane4(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getBtnCancel(), null);
			jContentPane.add(getJPanel3(), null);
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
			txtName = new JTextField();
			txtName.setBounds(160, 12, 412, 21);
		}
		return txtName;
	}
	/**
	 * This method initializes txAreaFml	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getTxAreaFml() {
		if (txAreaFml == null) {
			txAreaFml = new FormulaEditor(getAllFuncName());
		}
		return txAreaFml;
	}		

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return nc.ui.pub.beans.UITabbedPane	
	 * @i18n miufo1000922=审核条件
	 * @i18n miufo1000177=函数
	 * @i18n miufo1000178=运算符
	 */    
	private nc.ui.pub.beans.UITabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new nc.ui.pub.beans.UITabbedPane();
			jTabbedPane.setBounds(33, 200, 544, 181);
			jTabbedPane.addTab(StringResource.getStringResource("miufo1000922"), null, getJPanel(), null);
			jTabbedPane.addTab(StringResource.getStringResource("miufo1000177"), null, getJPanel1(), null);
			jTabbedPane.addTab(StringResource.getStringResource("miufo1000178"), null, getJPanel2(), null);
		}
		return jTabbedPane;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.add(getJScrollPane(), null);
			jPanel.add(getJScrollPane1(), null);
		}
		return jPanel;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(2, 2, 177, 147);
			jScrollPane.setViewportView(getListCheckCond());
		}
		return jScrollPane;
	}
	private void initCheckNote(){
		Object[] values=(Object[]) getListCheckCond().getSelectedValue();
		getTxtAreaCheckNote().setText((String) values[2]);
	}
	/**
	 * This method initializes listCheckCond	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getListCheckCond() {
		if (listCheckCond == null) {
			listCheckCond = new UIList();
			
			String strCond=StringResource.getStringResource("miufopublic436");//条件
			String strNotPass=StringResource.getStringResource("miufo1000126");//"审核不通过"
			
			StringBuffer  strBuf1=new StringBuffer();
			strBuf1.append(StringResource.getStringResource("miufotasknew00052"));//单个条件，条件满足时提示
			strBuf1.append("\r\n");
			strBuf1.append("if(");
			strBuf1.append(strCond);
			strBuf1.append(")\r\n  return '2,");
			strBuf1.append(strNotPass);//"审核不通过"
			strBuf1.append("'\r\nend\r\n");
			strBuf1.append(StringResource.getStringResource("miufo1000181"));  //"说明：提示信息-0	警告信息-1	错误信息-2")
			
			StringBuffer strBuf2=new StringBuffer();
			strBuf2.append(StringResource.getStringResource("miufotasknew00053"));  //"单个条件，条件满足不满足时都给出提示信息"
			strBuf2.append("\r\nif(");
			strBuf2.append(StringResource.getStringResource("miufotasknew00057"));  //"条件1
			strBuf2.append(")\r\n  return ('0,");
			strBuf2.append(StringResource.getStringResource("miufotasknew00054"));//条件满足时的提示信息
			strBuf2.append("')\r\nelse\r\n  return ('2,");
			strBuf2.append(StringResource.getStringResource("miufotasknew00055"));//条件不满足时的提示信息
			strBuf2.append("')\r\nend\r\n");
			strBuf2.append(StringResource.getStringResource("miufotasknew00056"));//多个条件的审核，不满足条件1时继续审核条件2
			strBuf2.append("\r\nif(");
			strBuf2.append(StringResource.getStringResource("miufotasknew00057"));  //"条件1
			strBuf2.append(")\r\n  return ('2,");
			strBuf2.append(strNotPass);//"审核不通过"
			strBuf2.append("')\r\nelseif (");
			strBuf2.append(StringResource.getStringResource("miufotasknew00058"));//条件2
			strBuf2.append(")\r\n  return ('2,");
			strBuf2.append(strNotPass);//"审核不通过"
			strBuf2.append("')\r\nend\r\n");
			strBuf2.append(StringResource.getStringResource("miufo1000181"));  //"说明：提示信息-0	警告信息-1	错误信息-2")
			
			StringBuffer strBuf3=new StringBuffer();
			strBuf3.append("Table.check(");
			strBuf3.append(strCond);
			strBuf3.append(",");
			strBuf3.append(StringResource.getStringResource("miufotasknew00055"));//条件不满足时的提示信息
			strBuf3.append(",");
			strBuf3.append(StringResource.getStringResource("miufotasknew00054"));//条件满足时的提示信息
			strBuf3.append(")\r\n");
			strBuf3.append(StringResource.getStringResource("miufotasknew00061"));//参数可缺省,如:
			strBuf3.append("Table.check(");
			strBuf3.append(strCond);//条件
			strBuf3.append(",'2,");
			strBuf3.append(strNotPass);//"审核不通过"
			strBuf3.append("','0,");
			strBuf3.append(StringResource.getStringResource("miufo1000125"));//审核通过
			strBuf3.append("'),");
			strBuf3.append(StringResource.getStringResource("miufotasknew00059"));//条件正确或错误时都提示
			strBuf3.append("\r\n");
			strBuf3.append(StringResource.getStringResource("miufo1002116"));//或
			strBuf3.append("Table.check(");
			strBuf3.append(strCond);
			strBuf3.append(",'2,");
			strBuf3.append(strNotPass);//"审核不通过"
			strBuf3.append("'),");
			strBuf3.append(StringResource.getStringResource("miufotasknew00060"));//仅在错误时提示。
			
			
			 Object[][] strListCheckConds=new Object[][]{
					new Object[]{"If...Return","if()\n  return(',')\r\n",strBuf1.toString(),new Integer(3)},
					new Object[]{"Else...Return","else \n  return(',')\r\n",strBuf2.toString(),new Integer(16)},
					new Object[]{"Table.check","Table.check(,'2,' , '0,"+StringResource.getStringResource("miufo1000125")+"')\r\n",strBuf3.toString(),new Integer(12)}
			};
			 
			
			listCheckCond.setListData(strListCheckConds);
			listCheckCond.setSelectedIndex(0);
			listCheckCond.addMouseListener(new ListMouseListener());
			listCheckCond.addListSelectionListener(new ListSelectionListener(){

				public void valueChanged(ListSelectionEvent e) {
					initCheckNote();

				}
				
			});
			listCheckCond.setCellRenderer(new ListCellRenderer(){
				
			    protected  Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
			    private   Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
			    public Component getListCellRendererComponent(
					          JList list,
					          Object value,
					          int index,
					         boolean isSelected,
					          boolean cellHasFocus){
			    	if(value instanceof Object[]){
			    		Object[] temps=(Object[])value;
			    		if(temps!=null && temps.length>0 && temps[0] instanceof String){
			    			JLabel lb=new nc.ui.pub.beans.UILabel();
					    	lb.setText((String) temps[0]);
					    	if (isSelected) {
					    	    lb.setBackground(list.getSelectionBackground());
					    	    lb.setForeground(list.getSelectionForeground());
					    	}
					    	else {
					    	    lb.setBackground(list.getBackground());
					    	    lb.setForeground(list.getForeground());
					    	}
					    	
//					    	lb.setBackground(isSelected ? listCheckCond.getSelectionBackground()/*UIManager.getColor("Tree.selectionBackground")*/ : Color.white);
					    	
					    	lb.setOpaque(true);
					    	lb.setEnabled(list.isEnabled());
					    	lb.setFont(list.getFont());
					        Border border = null;
					        if (cellHasFocus) {
					            if (isSelected) {
					                border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
					            }
					            if (border == null) {
					                border = UIManager.getBorder("List.focusCellHighlightBorder");
					            }
					        } else {
					            border = getNoFocusBorder();
					        }
					        lb.setBorder(border);
					    	
					    	return lb;
			    		}
			    			
			    	}
					return null;
				}
			    private  Border getNoFocusBorder() {
			        if (System.getSecurityManager() != null) {
			            return SAFE_NO_FOCUS_BORDER;
			        } else {
			            return noFocusBorder;
			        }
			    }
			});
		}
		return listCheckCond;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setBounds(183, 2, 353, 146);
			jScrollPane1.setViewportView(getTxtAreaCheckNote());
		}
		return jScrollPane1;
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
			jPanel1.add(getJScrollPane2(), null);
			jPanel1.add(getJScrollPane3(), null);
			jPanel1.add(getJScrollPane6(), null);
		}
		return jPanel1;
	}
	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new UIScrollPane();
			jScrollPane2.setViewportView(getListFuncModule());
			jScrollPane2.setBounds(0, 2, 171, 148);
		}
		return jScrollPane2;
	}
	/**
	 * This method initializes listFuncModule	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getListFuncModule() {
		if (listFuncModule == null) {
			listFuncModule = new UIList();
			listFuncModule
					.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			listFuncModule.setListData(m_ufoFuncList.getCatList());
			listFuncModule.setSelectedIndex(0);
			listFuncModule
					.addListSelectionListener(new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent e) {
							UfoSimpleObject module = (UfoSimpleObject) listFuncModule.getSelectedValue();
							initFunc(module);
						}
					});
		}
	
		return listFuncModule;
	}
	private void initFunc(UfoSimpleObject module){
		model.removeAllElements();
		if (module != null) {
			UfoSimpleObject[] m_FuncNameList = m_ufoFuncList
					.getFuncList(module.getID());
			if (m_FuncNameList.length > 0) {
				for (int i = 0; i < m_FuncNameList.length; i++)
					model.addElement(m_FuncNameList[i]);
				getListFunc().setSelectedIndex(0);
			}
		}
	}
	
	/**
	 * This method initializes jScrollPane3	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new UIScrollPane();
			jScrollPane3.setBounds(175, 1, 174, 148);
			jScrollPane3.setViewportView(getListFunc());
		}
		return jScrollPane3;
	}
	/**
	 * This method initializes listFunc	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getListFunc() {
		if (listFunc == null) {
			listFunc = new UIList();
			listFunc.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			listFunc.setModel(model);
			listFunc.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					UfoSimpleObject module = (UfoSimpleObject) listFunc.getSelectedValue();
					if(module==null || m_ufoFuncList==null)
						return;
					
					String buf= IufoFormulalUtil.getFuncInfo(m_ufoFuncList,module);
					getTxtFuncInfo().setText(buf);
//					jScrollPane5.getHorizontalScrollBar().setValue(0);
				}
			});
			
			listFunc.addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent e) {
					if(getListFunc().getSelectedValue()!=null){
						if ((e != null) && (e.getClickCount() >= 2)) {
							UfoSimpleObject module = (UfoSimpleObject) listFunc.getSelectedValue();
							if( m_ufoFuncList==null)
								return;
							
							UfoFuncInfo funcInfo=m_ufoFuncList.getFuncInfo(module.getID(), module.getName());
							if(funcInfo!=null ){
								if(funcInfo.getParamLen()>0){
									m_iFormulaCaretPos=getTxAreaFml().getCaretPosition();
							        setResult(ID_WIZARDFORMULA);
							        close();
								}
								else{
									CommonFmlEditDlg.addString(funcInfo,getTxAreaFml());
								}
							}
						}
					}
					
				}

				public void mousePressed(MouseEvent e) {
				}

				public void mouseReleased(MouseEvent e) {
				
				}

				public void mouseEntered(MouseEvent e) {
				
				}

				public void mouseExited(MouseEvent e) {
				
				}
			
			});
		}
		return listFunc;
	}
	/**
	 * This method initializes jScrollPane4	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane4() {
		if (jScrollPane4 == null) {
			jScrollPane4 = new UIScrollPane();
			jScrollPane4.setBounds(33, 64, 542, 137);
			jScrollPane4.setViewportView(getTxAreaFml());
		}
		return jScrollPane4;
	}
	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003314=确定
	 */    
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new nc.ui.pub.beans.UIButton();
			btnOK.setBounds(191, 490, 75, 22);
			btnOK.setText(StringResource.getStringResource("miufo1003314"));
			btnOK.addActionListener(this);
		}
		return btnOK;
	}
	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003315=取消
	 */    
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new nc.ui.pub.beans.UIButton();
			btnCancel.setBounds(353, 490, 75, 22);
			btnCancel.setText(StringResource.getStringResource("miufo1003315"));
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new UIPanel();
			jPanel2.setLayout(null);
			jPanel2.add(getJScrollPane5(), null);
		}
		return jPanel2;
	}
	/**
	 * This method initializes jScrollPane5	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane5() {
		if (jScrollPane5 == null) {
			jScrollPane5 = new UIScrollPane();
			jScrollPane5.setBounds(3, 2, 537, 145);
			jScrollPane5.setViewportView(getListOper());
		}
		return jScrollPane5;
	}
	/**
	 * This method initializes listOper	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getListOper() {
		if (listOper == null) {
			listOper = new UIList();
			listOper.addMouseListener(new ListMouseListener());
			listOper.setListData(strOper);
			listOper.setSelectedIndex(0);
		}
		return listOper;
	}
	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n miufopublic384=提示信息
	 */    
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new UIPanel();
			jPanel3.setLayout(null);
			jPanel3.setBounds(34, 386, 543, 87);
			jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("miufopublic384"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanel3.add(getJScrollPane7(), null);
		}
		return jPanel3;
	}
	/**
	 * This method initializes txtAreaCheckNote	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getTxtAreaCheckNote() {
		if (txtAreaCheckNote == null) {
			txtAreaCheckNote = new UITextArea();
			txtAreaCheckNote.setLineWrap(true);
		}
		return txtAreaCheckNote;
	}
	
	public void setCheckName(String strName)
	{
		getTxtName().setText(strName);
	}
	public String getCheckName()
	{
	    return getTxtName().getText();
	}
	
	public String getUserCheckFormula() {
	    return getTxAreaFml().getText();
	}
	public void setUserCheckFormula(String checkFormula) {
		getTxAreaFml().setText(checkFormula);
	}
	

//	private FormulaDefPlugin getFormulaPI(){
//	    return (FormulaDefPlugin) m_report.getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//	}
	
	private FuncListInst getFuncList(){
		return getFormulaExecutor().getFuncListInst();
	}
	
	 public RepCheckVO getRepCheckVO()
	 {
	     RepCheckVO  repCheckVO = new RepCheckVO();
	     repCheckVO.setID(RepCheckVO.getValidID());
	     repCheckVO.setName(m_strName);
	     repCheckVO.setFormula(m_strParsedCheckFm);
	     return repCheckVO;
	 }
	 
	 private String checkCheckFormula(String formula){
	    try {
	        //调用批命令进行语法检查       
	        m_strParsedCheckFm = parseFormula(formula, true);
	    }catch(Exception e){
	    	AppDebug.debug(e);
	        return e.getMessage();
	    }
	    return "";
	 }
	 
	 public void actionPerformed(ActionEvent e) {
	 	if ( e.getSource() == getBtnOK()) {
	        String strMsg = checkName(getTxtName().getText());
	        if( strMsg != null ){
	            showmessage(strMsg);
	            return;
	        }else
	            m_strName = getTxtName().getText();
	        

	        String formula=getTxAreaFml().getText();
	        if (formula.trim().equals("")) {
	        	  showmessage(StringResource.getStringResource("miufo1000916"));  //"审核公式不能为空"
	        	  return;
		    } 
		    //一个公式中不允许定义两条审核公式内容
		    if(formula.trim().indexOf("Table.check",1) > 1){
		    	showmessage(StringResource.getStringResource("miufo1000918"));  //"check只允许定义一次"
		    	return;
		    }
		    boolean bSuccess=parseCmd(formula);
		    if(bSuccess==false){
//		    	如果检查没有通过，判断一下是否是由于缺少end标记的原因，如果是，那么自动添加。
		    	formula = formula+NEWLINE+"end";
		    }
	        String checkResult = checkCheckFormula(formula);

	        
//	        if(!checkResult.equals("")){
//	            formula = formula+NEWLINE+"end";
//	            String newCheckResult = checkCheckFormula(formula,true);
//	            if(newCheckResult.equals("")){
//	            	getTxAreaFml().setText(formula);
//	            }
//	            checkResult = newCheckResult;
//	        }

	        if (checkResult.equals("")) {
	        	getTxAreaFml().setText(formula);
	            setResult(ID_OK);
	            close();
	        } else {
	            showmessage(checkResult);
	            return;
	        }
	    }  else if (e.getSource() == getBtnCancel()) {
	        setResult(ID_CANCEL);
	        close();
	    } 
	 }
	 private void showmessage(String errs) {
//	    JOptionPane.showMessageDialog(this,errs,StringResource.getStringResource("miufo1000761"),JOptionPane.INFORMATION_MESSAGE);  //"用友软件集团"
	 	lblErr.setText(errs);
	}
	 
	 public static String  checkName(String strName){

	    //检查是否超长
	    if( strName!=null && strName.getBytes().length >MAX_NAME_LENGHT ){
	        return StringResource.getStringResource("miufochk002");
	    }
	  
	    //检查名称是否合法,
	    if( InputvalueCheck.isValidName(strName)==false ){
	        //名称中包含非法字符
	        return StringResource.getStringResource("miufochk004");
	    }

	    return null;
	}
	 public UfoSimpleObject getSelectedFunc(){
	 	return (UfoSimpleObject) getListFunc().getSelectedValue();
	 }
	 public UfoSimpleObject getSeletedModule(){
	 	return (UfoSimpleObject) getListFuncModule().getSelectedValue();
	 }
	
	 public void setSelectedFunc(UfoSimpleObject module,UfoSimpleObject func){
	 	getJTabbedPane().setSelectedIndex(1);
	 	getListFuncModule().setSelectedValue(module,true);
	 	initFunc(module);
	 	getListFunc().setSelectedValue(func,true);
	 }
	 public int getFormulaCaretPos(){
	 	return m_iFormulaCaretPos;
	 }

	 /**
		 * 获得所有函数的名称
		 * 
		 * @return
		 */
	public String[] getAllFuncName() {
		FuncListInst funcList = getFuncList();
		if (funcList == null)
			return null;
		UfoSimpleObject[] allModules = funcList.getCatList();
		Vector<String> funcNameVector = new Vector<String>();
		for (UfoSimpleObject module : allModules) {
			if (module == null)
				continue;
			UfoSimpleObject[] m_FuncNameList = funcList.getFuncList(module
					.getID());
			if (m_FuncNameList == null || m_FuncNameList.length == 0)
				continue;
			for (UfoSimpleObject simpleObj : m_FuncNameList) {
				if (simpleObj != null) {
					funcNameVector.addElement(simpleObj.getName());
				}
			}
		}
		return funcNameVector.toArray(new String[0]);
	}
		
	/**
	 * This method initializes jScrollPane6
	 * 
	 * @return javax.swing.JScrollPane
	 */    
	private JScrollPane getJScrollPane6() {
		if (jScrollPane6 == null) {
			jScrollPane6 = new UIScrollPane();
			jScrollPane6.setBounds(353, 2, 183, 146);
			jScrollPane6.setViewportView(getTxtFuncInfo());
		}
		return jScrollPane6;
	}
	/**
	 * This method initializes txtFuncInfo	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getTxtFuncInfo() {
		if (txtFuncInfo == null) {
			txtFuncInfo = new UITextArea();
			txtFuncInfo.setLineWrap(true);
		}
		return txtFuncInfo;
	}
	/**
	 * This method initializes jScrollPane7	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane7() {
		if (jScrollPane7 == null) {
			jScrollPane7 = new UIScrollPane();
			jScrollPane7.setBounds(7, 16, 533, 65);
			jScrollPane7.setViewportView(lblErr);
		}
		return jScrollPane7;
	}
    }  //  @jve:decl-index=0:visual-constraint="138,23"
 