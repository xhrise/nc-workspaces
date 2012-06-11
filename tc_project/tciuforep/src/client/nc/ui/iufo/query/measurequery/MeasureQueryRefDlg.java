package nc.ui.iufo.query.measurequery;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.query.measurequery.DBColumn;
import nc.vo.iufo.query.measurequery.MeasureQueryFuncVO;
import nc.vo.iufo.query.measurequery.MeasureQueryVO;
import nc.vo.iufo.query.measurequery.QueryElement;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;


//whtao edit 2002-7-11
public class MeasureQueryRefDlg extends com.ufsoft.report.dialog.UfoDialog implements ActionListener,ChangeListener,KeyListener,
IUfoContextKey
{
						//temp add
	private MeasureQueryFuncVO m_vo; 								//指标查询元素
	private MeasureQueryVO[] vos;
	// private MeasureQueryData mfd ;									//指标查询中表格中的数据存储
	private MeasureQueryTable mftTitle;								//指标查询中表格显示的数据(标题区)
	private MeasureQueryTable mftData;								//指标查询中表格显示的数据(数据区)
	private MeasureQueryTable mftCon;								//指标查询中表格显示的数据(条件区)
	private Hashtable mftTable;										//记录标题区和数据区的操作后的项目集
	private Vector mftVec;											//项目集
	private String[] logic = {"and","or"};							//逻辑运算符
	private String[] handel = {"=",">",">=","<","<=","!=","like"};	//操作符
	private String endLogic = new String();							//记录条件中的最后一条的逻辑运算符，如果又增加了一行记录，则把该条件显示出来
	private String lastQueryMeasure = "";							//记录上次选择的指标查询
	public int TITLE = 0;
	public int DATA = 1;
	public int CONDITION = 2;
	//Dialog
	private JPanel JDialogContentPane = null;		//面板
	private nc.ui.pub.beans.UITabbedPane jTabbedPane = null;			//面板
	private JButton CancelButton = null;			//按钮		取消
	private JButton OKButton = null;				//按钮		确认
	//“选择指标查询”面板
	private JPanel nameChoicePane = null;			//面板
	private JLabel queryDirectionLabel = null;		//标签		排列方向
	private JLabel queryNumLabel = null;			//标签		最大记录数
	private JLabel querySelectLabel = null;			//标签		可选择的指标查询
	private JRadioButton radioButtonH = null;		//单选按钮	横向
	private JRadioButton radioButtonV = null;		//单选按钮	纵向
	private JTextField queryNumTextField = null;	//文本框	最大记录数
	private JComboBox querySelectComboBox = null;	//复选框	可选择的指标查询
	//“选择标题区”面板
	private JPanel titleElmtPane = null;			//面板
	private JScrollPane titleScrollPane = null;		//滚动面板	支持表格
	private JTable titleTable = null;				//表格
	private JLabel titleAddressLabel = null;		//标签		标题位置
	private JLabel titleCompLabel = null;			//标签		排列方式
	private JLabel titleLable = null;				//标签		选择标题
	private JButton titleAddButton = null;			//按钮		增加
	private JButton titleDeleteButton = null;		//按钮		删除
	private JComboBox titleAddressComboBox = null;	//复选框	标题位置
	private JComboBox titleCompComboBox = null;		//复选框	排列方式
	private JComboBox titleComboBox = null;			//复选框	选择标题
	//“选择数据区”面板
	private JPanel dataElmtPane = null;				//面板
	private JScrollPane dataScrollPane = null;		//滚动面板	支持表格
	private JTable dataTable = null;				//表格
	private JLabel dataFunLabel = null;				//标签		统计函数
	private JLabel dataCompLabel = null;			//标签		排列方式
	private JLabel dataProLabel = null;				//标签		项目
	private JButton dataAddButton = null;			//按钮		增加
	private JButton dataDeleteButton = null;		//按钮		删除
	private JComboBox dataComboBoxFun = null;		//复选框	统计函数
	private JComboBox dataCompComboBox = null;		//复选框	排列方式
	private JComboBox dataProComboBox = null;		//复选框	项目
	//“选择条件”面板
	private JPanel conditionPane = null;			//面板
	private JScrollPane conScrollPane = null;		//滚动面板	支持表格
	private JTable conditionTable = null;			//表格
	private JLabel conHandelLabel = null;			//标签		操作符
	private JLabel conLogicLabel = null;			//标签		逻辑运算符
	private JLabel conProBeginLabel = null;			//标签		项目
	private JLabel conProEndLabel = null;			//标签		项目
	private JButton conAddButton = null;			//按钮		增加
	private JButton conDeleteButton = null;			//按钮		删除
	private JComboBox conHandelComboBox = null;		//复选框	操作符
	private JComboBox conLogicComboBox = null;		//复选框	逻辑运算符
	private JComboBox conProBeginComboBox = null;	//复选框	项目
	private JComboBox conProEndComboBox = null;		//复选框	项目
/**
 * MeasureQueryRefDlg constructor comment.
 */
public MeasureQueryRefDlg() {
	this(null,null);
}

public MeasureQueryRefDlg(Container objContainer,IContext context){
	super(objContainer);
	try{
		if(objContainer!=null && objContainer instanceof UfoReport){
			UfoContextVO contextVO=(UfoContextVO) context;
			String strCurUnitId = (String)contextVO.getAttribute(CUR_UNIT_ID);
			if(contextVO!=null && strCurUnitId!=null){
				vos = MeasureQueryBO_Client.loadQueryByUnit(strCurUnitId);
			}
		}

		if (vos.length!=0&&vos != null)
		{
			for (int i = 0; i < vos.length; i++) 
			{
				getQuerySelectComboBox().addItem(vos[i].getQueryName());
			}
		}
	}catch(Exception e)
	{
		AppDebug.debug(e);
		return;
	}
	initialize();
}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==OKButton)
		{
			setResult(ID_OK);
			String queryName = (String) querySelectComboBox.getSelectedItem();
			int queryCount = 10000;
			if(queryNumTextField.getText()!=null&&(!queryNumTextField.getText().equals("")))
				queryCount = Integer.parseInt(queryNumTextField.getText().trim());
			String radio = "";
			if(getRadioButtonH().isSelected())
				radio = "H";
			else if(getRadioButtonV().isSelected())
				radio = "V";
			String titleString = dataGeneration(((MeasureQueryTable)getTitleTable().getModel()).getMeasureQueryTable(),TITLE);
			String dataString = dataGeneration(((MeasureQueryTable)getDataTable().getModel()).getMeasureQueryTable(),DATA);
			String conString = dataGeneration(((MeasureQueryTable)getConditionTable().getModel()).getMeasureQueryTable(),CONDITION);
			
			//if(titleString.trim().equals("")||dataString.trim().equals(""))
			if(titleString.trim().equals(""))
			{
				UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1003105"),null);  //"标题区没有选择，请选择！"
				return;
			}
			m_vo = new MeasureQueryFuncVO();
			if( conString.trim().equals("") && queryCount==10000)
			{
				String[] voStr = {queryName.trim(),titleString.trim()+radio,dataString.trim(),null,null};
				m_vo.setContents(voStr);
			}
			else if(conString.trim().equals(""))
			{
				String[] voStr = {queryName.trim(),titleString.trim()+radio,dataString.trim(),null,""+queryCount};
				m_vo.setContents(voStr);
			}
			else if(queryCount==10000)
			{
				String[] voStr = {queryName.trim(),titleString.trim()+radio,dataString.trim(),conString,null};
				m_vo.setContents(voStr);
			}
			else 
			{
				String[] voStr = {queryName.trim(),titleString.trim()+radio,dataString.trim(),conString,""+queryCount};
				m_vo.setContents(voStr);
			}
			close();
		}else if(e.getSource()==CancelButton)
		{
			close();
		}else if(e.getSource()==querySelectComboBox)
		{
			if(lastQueryMeasure == null)
				lastQueryMeasure = (String)querySelectComboBox.getSelectedItem();
			if(!lastQueryMeasure.equals(querySelectComboBox.getSelectedItem()))
			{
				//清空标题区、数据区和条件面板的数据
				lastQueryMeasure = (String)querySelectComboBox.getSelectedItem();
				getTitleComboBox().removeAllItems();
				getDataProComboBox().removeAllItems();
				getConProBeginComboBox().removeAllItems();
				getConProEndComboBox().removeAllItems();
				((MeasureQueryTable)getTitleTable().getModel()).removeAllElements();
				getTitleTable().removeAll();
				((MeasureQueryTable)getDataTable().getModel()).removeAllElements();
				getDataTable().removeAll();
				((MeasureQueryTable)getConditionTable().getModel()).removeAllElements();
				getConditionTable().removeAll();
				if(mftVec!=null)
					mftVec.clear();
				if(mftTable!=null)
					mftTable.clear();
			}
		}else if(e.getSource() == conHandelComboBox)
		{
			if( ((String)conHandelComboBox.getSelectedItem()).equals("like") )
			{
				DBColumn db = (DBColumn)mftTable.get((String)conProBeginComboBox.getSelectedItem());
				if(db.getType()!=java.sql.Types.VARCHAR)
				{
					UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1003106"),null);  //"当项目是数值型时，不能定义“like”操作符！"
					conHandelComboBox.setSelectedIndex(0);
					return;
				}
			}
		}
	}
/**
 * 创建日期：(2002-7-15 17:36:05)
 * @author：王海涛
 * @version：最后修改日期
 * 
 *  说明
 * 
 * @return java.lang.String
 * @param mqty nc.ui.iufo.query.measurequery.MeasureQueryTable
 * @param type int
 */
public String dataGeneration(Vector data, int type) {
	String returnString = new String();
	if(type==TITLE)
	{
		for(int i = 0;i < data.size();i++)
		{
			MeasureQueryData mqd = (MeasureQueryData)data.elementAt(i);
			Vector daVec = mqd.getData();
			String str = "";
			for(int j = 0;j < daVec.size();j++)
			{
				// boolean isNotNoOrder = true;
				if(daVec.elementAt(j).equals(StringResource.getStringResource("miufo1003107")))  //"横标题"
					str = "Hor";
				else if(daVec.elementAt(j).equals(StringResource.getStringResource("miufo1003108")))  //"纵标题"
					str = "Ver";
				else if(daVec.elementAt(j).equals(StringResource.getStringResource("miufo1001305")))  //"升序"
					str = "ASC";
				else if(daVec.elementAt(j).equals(StringResource.getStringResource("miufo1001306")))  //"降序"
					str = "DESC";
				else if(daVec.elementAt(j).equals(" "))
					str = "@@NOORDER";
				else 
					str = (String)daVec.elementAt(j);	
				
				if(j!=daVec.size() - 1)
				{
					returnString = returnString + str +":";
				}else
				{
					if(i <= data.size() - 1)
						returnString = returnString + str +";";
				}
			}
		}
		while(true)
		{
			if(returnString.indexOf(":@@NOORDER") >= 0)
			{
				int Sindex = returnString.indexOf(":@@NOORDER");
				int Eindex = Sindex + 10;
				returnString = returnString.substring(0,Sindex) + returnString.substring(Eindex);
			}else
			{
				break;
			}
		}
		return returnString;
	}
	else if(type==DATA)
	{
		for(int i = 0;i < data.size();i++)
		{
			MeasureQueryData mqd = (MeasureQueryData)data.elementAt(i);
			Vector daVec = mqd.getData();
			String str = "";
			for(int j = 0;j < daVec.size();j++)
			{
				if(daVec.elementAt(j).equals(StringResource.getStringResource("miufo1001305")))  //"升序"
					str = "ASC";
				else if(daVec.elementAt(j).equals(StringResource.getStringResource("miufo1001306")))  //"降序"
					str = "DESC";
				else 
					str = (String)daVec.elementAt(j);	
				
				if( j==0 )
					returnString = returnString + str;
				else if(j==1&&!daVec.elementAt(0).equals(""))
					returnString = returnString + "(" + str +"):";
				else if(j==1&&daVec.elementAt(0).equals(""))
					returnString = returnString + str + ":";
				else
				{
					if(i < data.size()-1)
						returnString = returnString + str +";";
					else
						returnString = returnString + str ;
				}
			}
		}
		return returnString; 
	}
	else if(type == CONDITION)
	{
		for(int i = 0;i < data.size();i++)
		{
			MeasureQueryData mqd = (MeasureQueryData)data.elementAt(i);
			Vector daVec = mqd.getData();
			boolean isLike = false;
			String str = "";
			for(int j = 0;j < daVec.size();j++)
			{
				if(daVec.elementAt(j).equals("like"))
				{
					isLike = true;
					str = (String)daVec.elementAt(j);
				}
				else
					str = (String)daVec.elementAt(j);
				if(j==2&&isLike)
					returnString = returnString + str.substring(0,1) + "%"
						+ str.substring(1,str.length()-1) + "%" + str.substring(str.length()-1)+" ";
				else
					returnString = returnString + str +" ";

			}
		}
		return returnString;
	}
	return null;
}
/**
 * Insert the method's description here.
 * 创建时间: (2002-7-4 10:31:34)
 * @return JPanel
 */
protected JPanel getBtnsPane() {
	JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER));/*
	m_btnOK = new nc.ui.pub.beans.UIButton("确定");
	m_btnCancel = new nc.ui.pub.beans.UIButton("取消");
	pane.add(m_btnOK);
	pane.add(m_btnCancel);

	m_btnOK.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String qname = (String) m_names.getSelectedItem();
			m_vo.setQueryName(qname);
			if (qname == null && m_names.getItemCount() > 0) {
				JOptionPane.showMessageDialog(null, "必须选择指标查询名称");
				return;
			}
			dispose();
		}
	});

	m_btnCancel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	});*/
	return pane;
}
/**
 * 返回 CancelButton 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getCancelButton() {
	if (CancelButton == null) {
		try {
			CancelButton = new nc.ui.pub.beans.UIButton();
			CancelButton.setName("CancelButton");
			CancelButton.setText(StringResource.getStringResource("miufopublic247"));  //"取消"
			CancelButton.addActionListener(this);
			OKButton.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
			CancelButton.setBounds(251, 246, 75, 22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return CancelButton;
}
/**
 * 返回 conAddButton 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getConAddButton() {
	if (conAddButton == null) {
		try {
			conAddButton = new nc.ui.pub.beans.UIButton();
			conAddButton.setName("conAddButton");

			conAddButton.setText(StringResource.getStringResource("miufo1000080"));  //"增加"
			conAddButton.setBounds(323, 84, 75, 22);
			conAddButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MeasureQueryTable mft = (MeasureQueryTable)getConditionTable().getModel();
					String pro1 = (String) conProBeginComboBox.getSelectedItem();
					String handel = (String)conHandelComboBox.getSelectedItem();
					String pro2 = (String)conProEndComboBox.getSelectedItem();
					if(pro1 == null || pro2 == null)
						return;
					if(pro2.indexOf(" ")>0||pro2.indexOf("?")>0||pro2.indexOf("_")>0)
					{
						UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1003109"),null);//"右项目内容中不能含有\"空格\"、\"？\"和\"_\" ！"
						return;
					}
					if( ((DBColumn)mftTable.get(pro1)).getType()==java.sql.Types.VARCHAR ){
//						pro2 = "'"+pro2+"'";
						pro2 = "\""+pro2+"\"";
					}
					else if(((DBColumn)mftTable.get(pro1)).getType()!=java.sql.Types.VARCHAR&&handel.equals("like") )
					{
						UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1003106"),null);  //"当项目是数值型时，不能定义“like”操作符！"
						conHandelComboBox.setSelectedIndex(0);
						return;
					}
					String logic = (String)conLogicComboBox.getSelectedItem();
					if(pro1!=null&&pro2!=null&&(!pro1.equals(""))&&(!pro2.equals("")))
					{
						MeasureQueryData mfd = new MeasureQueryData();
						mfd.addElements(pro1);
						mfd.addElements(handel);
						mfd.addElements(pro2);
						mfd.addElements(logic);
						if(mft.getRowCount() > 0)
							mft.setValueAt(endLogic,mft.getRowCount()-1,mft.getColumnCount()-1);
						if(!mft.isExist(mfd,mft.getColumnCount()-1))
						{
							mfd.removeElementAt(3);
							mfd.addElements("");
							if(mft.getRowCount() > 0)
								mft.setValueAt(endLogic,mft.getRowCount()-1,mft.getColumnCount()-1);
							mft.AddEntry(mfd,mft.getColumnCount()-1);
							endLogic = logic;
						}else
						{
							mft.setValueAt("",mft.getRowCount()-1,mft.getColumnCount()-1);
						}
					}
					mft.fireTableDataChanged();//刷新显示
				}
			});
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return conAddButton;
}
/**
 * 返回 conDeleteButton 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getConDeleteButton() {
	if (conDeleteButton == null) {
		try {
			conDeleteButton = new nc.ui.pub.beans.UIButton();
			conDeleteButton.setName("conDeleteButton");
			conDeleteButton.setText(StringResource.getStringResource("miufopublic243"));  //"删除"
			conDeleteButton.setBounds(323, 135, 75, 22);
			conDeleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MeasureQueryTable mft = (MeasureQueryTable)getConditionTable().getModel();
					int[] Row = getConditionTable().getSelectedRows();
					if(Row.length!=0)
					{
						for(int i = 0;i < Row.length;i++)
							mft.DeleteEntry(Row[i]-i);
					}
					mft.fireTableDataChanged();//刷新显示
				}
			});
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conDeleteButton;
}
/**
 * 返回 conditionPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getConditionPane() {
	if (conditionPane == null) {
		try {
			conditionPane = new UIPanel();
			conditionPane.setName("conditionPane");
			conditionPane.setLayout(null);
			getConditionPane().add(getConProBeginLabel(), getConProBeginLabel().getName());
			getConditionPane().add(getConHandelLabel(), getConHandelLabel().getName());
			getConditionPane().add(getConProEndLabel(), getConProEndLabel().getName());
			getConditionPane().add(getConLogicLabel(), getConLogicLabel().getName());
			getConditionPane().add(getConProBeginComboBox(), getConProBeginComboBox().getName());
			getConditionPane().add(getConHandelComboBox(), getConHandelComboBox().getName());
			getConditionPane().add(getConProEndComboBox(), getConProEndComboBox().getName());
			getConditionPane().add(getConLogicComboBox(), getConLogicComboBox().getName());
			getConditionPane().add(getConScrollPane(), getConScrollPane().getName());
			getConditionPane().add(getConAddButton(), getConAddButton().getName());
			getConditionPane().add(getConDeleteButton(), getConDeleteButton().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conditionPane;
}
/**
 * 返回 conditionTable 特性值。
 * @return javax.swing.JTable
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTable getConditionTable() {
	if (conditionTable == null) {
		try {
			conditionTable = new nc.ui.pub.beans.UITable();
			conditionTable.setAutoCreateColumnsFromModel(true);
			mftCon = new MeasureQueryTable();
			String[] columnsName = {StringResource.getStringResource("miufopublic304"),StringResource.getStringResource("miufo1003111"),StringResource.getStringResource("miufopublic304"),StringResource.getStringResource("miufo1003112")};
			mftCon.setColumnNames(columnsName);
			conditionTable.setModel(mftCon); 
			conditionTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			conditionTable.setName("conditionTable");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conditionTable;
}
/**
 * 返回 conComboBox 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getConHandelComboBox() {
	if (conHandelComboBox == null) {
		try {
			conHandelComboBox = new UIComboBox();
			for(int i = 0;i < handel.length;i++)
				conHandelComboBox.addItem(handel[i]);
			conHandelComboBox.setName("conHandelComboBox");
//			conHandelComboBox.setFont(new java.awt.Font("dialog", 0, 12));
			conHandelComboBox.setBounds(129, 34, 81, 20);
			conHandelComboBox.addActionListener(this);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conHandelComboBox;
}
/**
 * 返回 JLabel2 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getConHandelLabel() {
	if (conHandelLabel == null) {
		try {
			conHandelLabel = new UILabel();
			conHandelLabel.setName("conHandelLabel");
//			conHandelLabel.setFont(new java.awt.Font("dialog", 0, 12));
			conHandelLabel.setText(StringResource.getStringResource("miufo1003111"));  //"操作符"
			conHandelLabel.setBounds(129, 10, 45, 16);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conHandelLabel;
}
/**
 * 返回 ComboBoxB 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getConLogicComboBox() {
	if (conLogicComboBox == null) {
		try {
			conLogicComboBox = new UIComboBox();
			for(int i = 0;i < logic.length;i++)
				conLogicComboBox.addItem(logic[i]);
			conLogicComboBox.setName("conLogicComboBox");
//			conLogicComboBox.setFont(new java.awt.Font("dialog", 0, 12));
			conLogicComboBox.setBounds(339, 34, 81, 20);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conLogicComboBox;
}
/**
 * 返回 JLabel4 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getConLogicLabel() {
	if (conLogicLabel == null) {
		try {
			conLogicLabel = new UILabel();
			conLogicLabel.setName("conLogicLabel");
//			conLogicLabel.setFont(new java.awt.Font("dialog", 0, 12));
			conLogicLabel.setText(StringResource.getStringResource("miufo1003112"));  //"逻辑运算符"
			conLogicLabel.setBounds(339, 10, 66, 16);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conLogicLabel;
}
/**
 * 返回 prComboBox 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getConProBeginComboBox() {
	if (conProBeginComboBox == null) {
		try {
			conProBeginComboBox = new UIComboBox();
			conProBeginComboBox.setName("conProBeginComboBox");
//			conProBeginComboBox.setFont(new java.awt.Font("dialog", 0, 12));
			conProBeginComboBox.setBounds(24, 34, 81, 20);
			//conProBeginComboBox.setEditable(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conProBeginComboBox;
}
/**
 * 返回 conProBeginLabel 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getConProBeginLabel() {
	if (conProBeginLabel == null) {
		try {
			conProBeginLabel = new UILabel();
			conProBeginLabel.setName("conProBeginLabel");
//			conProBeginLabel.setFont(new java.awt.Font("dialog", 0, 12));
			conProBeginLabel.setText(StringResource.getStringResource("miufo1003113"));  //"项目："
			conProBeginLabel.setBounds(24, 10, 45, 16);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conProBeginLabel;
}
/**
 * 返回 JComboBoxA 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getConProEndComboBox() {
	if (conProEndComboBox == null) {
		try {
			conProEndComboBox = new UIComboBox();
			conProEndComboBox.setName("conProEndComboBox");
//			conProEndComboBox.setFont(new java.awt.Font("dialog", 0, 12));
			conProEndComboBox.setBounds(234, 34, 81, 20);
			conProEndComboBox.setEditable(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conProEndComboBox;
}
/**
 * 返回 JLabel3 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getConProEndLabel() {
	if (conProEndLabel == null) {
		try {
			conProEndLabel = new UILabel();
			conProEndLabel.setName("conProEndLabel");
//			conProEndLabel.setFont(new java.awt.Font("dialog", 0, 12));
			conProEndLabel.setText(StringResource.getStringResource("miufopublic304"));  //"项目"
			conProEndLabel.setBounds(234, 10, 45, 16);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conProEndLabel;
}
/**
 * 返回 JScrollPane1 特性值。
 * @return javax.swing.JScrollPane
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JScrollPane getConScrollPane() {
	if (conScrollPane == null) {
		try {
			conScrollPane = new UIScrollPane(getConditionTable());
			conScrollPane.setName("conScrollPane");
			conScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			conScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			conScrollPane.setBounds(24, 70, 274, 123);
			getConScrollPane().setViewportView(getConditionTable());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return conScrollPane;
}
/**
 * 返回 dataAddButton 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getDataAddButton() {
	if (dataAddButton == null) {
		try {
			dataAddButton = new nc.ui.pub.beans.UIButton();
			dataAddButton.setName("dataAddButton");
			dataAddButton.setText(StringResource.getStringResource("miufo1000080"));  //"增加"
			dataAddButton.setBounds(323, 84, 75, 22);
			dataAddButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(mftVec == null || mftVec.size() == 0)
						return;
					MeasureQueryTable mft = (MeasureQueryTable)getDataTable().getModel();
					String fun = (String) dataComboBoxFun.getSelectedItem();
					String pro = (String) dataProComboBox.getSelectedItem();
					String comp = (String)dataCompComboBox.getSelectedItem();
					if(fun!=null&&pro!=null&&comp!=null
						&&(!fun.equals(""))&&(!pro.equals(""))&&(!comp.equals("")))
					{
						MeasureQueryData mfd = new MeasureQueryData();
						DBColumn db = (DBColumn)mftTable.get(pro);
						if( db.getType() == java.sql.Types.VARCHAR)
							mfd.addElements(QueryElement.COUNT);
						else
							mfd.addElements(fun);
						mfd.addElements(pro);
						mfd.addElements(comp);
						if(!mft.isExist(mfd,mft.getColumnCount()-1))
						{
							mft.AddEntry(mfd,mft.getColumnCount()-1);
							mftVec.removeElement(pro);
						}
					}
					mft.fireTableDataChanged();//刷新显示
				}
			});
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataAddButton;
}
/**
 * 返回 JComboBox3 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getDataComboBoxFun() {
	if (dataComboBoxFun == null) {
		try {
			dataComboBoxFun = new UIComboBox();
			dataComboBoxFun.addItem(QueryElement.SUM);
			dataComboBoxFun.addItem(QueryElement.MAX);
			dataComboBoxFun.addItem(QueryElement.MIN);
			dataComboBoxFun.addItem(QueryElement.COUNT);
			dataComboBoxFun.setName("dataComboBoxFun");
//			dataComboBoxFun.setFont(new java.awt.Font("dialog", 0, 12));
			dataComboBoxFun.setBounds(24, 34, 130, 25);
			dataComboBoxFun.setEditable(true);
			//dataComboBoxFun.setSelectedIndex(0);
			//dataComboBoxFun.setSelectedItem(SUM);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataComboBoxFun;
}
/**
 * 返回 ComboBoxComp 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getDataCompComboBox() {
	if (dataCompComboBox == null) {
		try {
			dataCompComboBox = new UIComboBox();
			//初始化赋值
			//dataCompComboBox.addItem(new String("无序"));
			dataCompComboBox.addItem(new String(StringResource.getStringResource("miufo1001305")));  //"升序"
			dataCompComboBox.addItem(new String(StringResource.getStringResource("miufo1001306")));  //"降序"
			dataCompComboBox.setName("dataCompComboBox");
//			dataCompComboBox.setFont(new java.awt.Font("dialog", 0, 12));
			dataCompComboBox.setBounds(302, 34, 115, 25);
			//dataCompComboBox.setSelectedIndex(0);
			//dataCompComboBox.setSelectedItem(无序,升序,降序);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataCompComboBox;
}
/**
 * 返回 label12 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getDataCompLabel() {
	if (dataCompLabel == null) {
		try {
			dataCompLabel = new UILabel();
			dataCompLabel.setName("dataCompLabel");
//			dataCompLabel.setFont(new java.awt.Font("dialog", 0, 12));
			dataCompLabel.setText(StringResource.getStringResource("miufo1003114"));  //"排列方式："
			dataCompLabel.setBounds(302, 10, 64, 16);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataCompLabel;
}
/**
 * 返回 dataDeleteButton 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getDataDeleteButton() {
	if (dataDeleteButton == null) {
		try {
			dataDeleteButton = new nc.ui.pub.beans.UIButton();
			dataDeleteButton.setName("dataDeleteButton");
			dataDeleteButton.setText(StringResource.getStringResource("miufopublic243"));  //"删除"
			dataDeleteButton.setBounds(323, 135, 75, 22);
			dataDeleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MeasureQueryTable mft = (MeasureQueryTable)getDataTable().getModel();
					int[] Row = getDataTable().getSelectedRows();
					if(Row.length!=0)
					{
						for(int i = 0;i < Row.length;i++)
						{
							String delStr = (String)mft.getValueAt(Row[i]-i,1);
							mft.DeleteEntry(Row[i]-i);
							if(!mft.containsElements(delStr))
								if(!mftVec.contains(delStr))
									if(mftTable.get(delStr)!=null)
										mftVec.addElement(delStr);
						}
					}
					mft.fireTableDataChanged();//刷新显示
				}
			});
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataDeleteButton;
}
/**
 * 返回 dataElmtPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getDataElmtPane() {
	if (dataElmtPane == null) {
		try {
			dataElmtPane = new UIPanel();
			dataElmtPane.setName("dataElmtPane");
			dataElmtPane.setLayout(null);
			getDataElmtPane().add(getDataFunLabel(), getDataFunLabel().getName());
			getDataElmtPane().add(getDataComboBoxFun(), getDataComboBoxFun().getName());
			getDataElmtPane().add(getDataScrollPane(), getDataScrollPane().getName());
			getDataElmtPane().add(getDataAddButton(), getDataAddButton().getName());
			getDataElmtPane().add(getDataDeleteButton(), getDataDeleteButton().getName());
			getDataElmtPane().add(getDataProLabel(), getDataProLabel().getName());
			getDataElmtPane().add(getDataProComboBox(), getDataProComboBox().getName());
			getDataElmtPane().add(getDataCompLabel(), getDataCompLabel().getName());
			getDataElmtPane().add(getDataCompComboBox(), getDataCompComboBox().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataElmtPane;
}
/**
 * 返回 JLabel5 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getDataFunLabel() {
	if (dataFunLabel == null) {
		try {
			dataFunLabel = new UILabel();
			dataFunLabel.setName("dataFunLabel");
//			dataFunLabel.setFont(new java.awt.Font("dialog", 0, 12));
			dataFunLabel.setText(StringResource.getStringResource("miufo1003115"));  //"统计函数："
			dataFunLabel.setBounds(24, 10, 72, 16);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataFunLabel;
}
/**
 * 返回 ComboBoxAddress 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getDataProComboBox() {
	if (dataProComboBox == null) {
		try {
			dataProComboBox = new UIComboBox();
			dataProComboBox.setName("dataProComboBox");
//			dataProComboBox.setFont(new java.awt.Font("dialog", 0, 12));
			dataProComboBox.setBounds(184, 34, 91, 25);
			dataProComboBox.setEditable(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataProComboBox;
}
/**
 * 返回 label10 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getDataProLabel() {
	if (dataProLabel == null) {
		try {
			dataProLabel = new UILabel();
			dataProLabel.setName("dataProLabel");
//			dataProLabel.setFont(new java.awt.Font("dialog", 0, 12));
			dataProLabel.setText(StringResource.getStringResource("miufo1003113"));  //"项目："
			dataProLabel.setBounds(184, 10, 64, 16);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataProLabel;
}
/**
 * 返回 JScrollPane2 特性值。
 * @return javax.swing.JScrollPane
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JScrollPane getDataScrollPane() {
	if (dataScrollPane == null) {
		try {
			dataScrollPane = new UIScrollPane(getDataTable());
			dataScrollPane.setName("dataScrollPane");
			dataScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			dataScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			dataScrollPane.setBounds(24, 70, 274, 124);
			getDataScrollPane().setViewportView(getDataTable());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataScrollPane;
}
/**
 * 返回 dataTable 特性值。
 * @return javax.swing.JTable
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTable getDataTable() {
	if (dataTable == null) {
		try {
			dataTable = new nc.ui.pub.beans.UITable();
			String[] columnNames = {StringResource.getStringResource("miufo1003116"),StringResource.getStringResource("miufopublic304"),StringResource.getStringResource("miufo1003117")};
			mftData = new MeasureQueryTable();
			mftData.setColumnNames(columnNames);
			dataTable.setAutoCreateColumnsFromModel(true);
			dataTable.setModel(mftData); 
			dataTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			dataTable.setName("dataTable");

			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return dataTable;
}
/**
 * 返回 JDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getJDialogContentPane() {
	if (JDialogContentPane == null) {
		try {
			JDialogContentPane = new UIPanel();
			JDialogContentPane.setName("JDialogContentPane");
			JDialogContentPane.setLayout(null);
			JDialogContentPane.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			getJDialogContentPane().add(getJTabbedPane(), getJTabbedPane().getName());
			getJDialogContentPane().add(getOKButton(), getOKButton().getName());
			getJDialogContentPane().add(getCancelButton(), getCancelButton().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable Exc) {
			// user code begin {2}
			// user code end
			handleException(Exc);
		}
	}
	return JDialogContentPane;
}
/**
 * 返回 nc.ui.pub.beans.UITabbedPane 特性值。
 * @return nc.ui.pub.beans.UITabbedPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITabbedPane getJTabbedPane() {
	if (jTabbedPane == null) {
		try {
			jTabbedPane = new nc.ui.pub.beans.UITabbedPane();
			jTabbedPane.setName("jTabbedPane");
//			jTabbedPane.setFont(new java.awt.Font("dialog", 0, 12));
			jTabbedPane.setBounds(6, 6, 451, 229);
			jTabbedPane.insertTab(StringResource.getStringResource("miufo1003118"), null, getNameChoicePane(), null, 0);  //"选择指标查询"
			jTabbedPane.insertTab(StringResource.getStringResource("miufo1003119"), null, getTitleElmtPane(), null, 1);  //"选择标题区"
			jTabbedPane.insertTab(StringResource.getStringResource("miufo1003120"), null, getDataElmtPane(), null, 2);  //"选择数据区"
			jTabbedPane.insertTab(StringResource.getStringResource("miufo1003121"), null, getConditionPane(), null, 3);  //"选择条件"
			//jTabbedPane.addFocusListener(this);
			jTabbedPane.addChangeListener(this);
			
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return jTabbedPane;
}
/**
 * 返回 nameChoicePane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getNameChoicePane() {
	if (nameChoicePane == null) {
		try {
			nameChoicePane = new UIPanel();
			nameChoicePane.setName("nameChoicePane");
			nameChoicePane.setLayout(null);
			getNameChoicePane().add(getQuerySelectComboBox(), getQuerySelectComboBox().getName());
			getNameChoicePane().add(getQuerySelectLabel(), getQuerySelectLabel().getName());
			getNameChoicePane().add(getQueryNumTextField(), getQueryNumTextField().getName());
			getNameChoicePane().add(getQueryNumLabel(), getQueryNumLabel().getName());
			getNameChoicePane().add(getQueryDirectionLabel(), getQueryDirectionLabel().getName());
			ButtonGroup bg = new ButtonGroup();
			bg.add(getRadioButtonH());
			bg.add(getRadioButtonV());
			getRadioButtonH().setSelected(true);
			getNameChoicePane().add(getRadioButtonV(), getRadioButtonV().getName());
			getNameChoicePane().add(getRadioButtonH(), getRadioButtonH().getName());
			//getNameChoicePane().addFocusListener(this);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return nameChoicePane;
}
/**
 * 返回 OKButton 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getOKButton() {
	if (OKButton == null) {
		try {
			OKButton = new nc.ui.pub.beans.UIButton();
			OKButton.setName("OKButton");
			OKButton.setText(StringResource.getStringResource("miufopublic246"));  //"确定"
			OKButton.addActionListener(this);
			OKButton.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
			OKButton.setBounds(105, 246, 75, 22);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return OKButton;
}
/**
 * 返回 label2 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getQueryDirectionLabel() {
	if (queryDirectionLabel == null) {
		try {
			queryDirectionLabel = new UILabel();
			queryDirectionLabel.setName("queryDirectionLabel");
//			queryDirectionLabel.setFont(new java.awt.Font("dialog", 0, 12));
			queryDirectionLabel.setText(StringResource.getStringResource("miufo1003122"));  //"排列方向："
			queryDirectionLabel.setBounds(15, 143, 63, 16);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return queryDirectionLabel;
}
/**
 * 返回 label1 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getQueryNumLabel() {
	if (queryNumLabel == null) {
		try {
			queryNumLabel = new UILabel();
			queryNumLabel.setName("queryNumLabel");
//			queryNumLabel.setFont(new java.awt.Font("dialog", 0, 12));
			queryNumLabel.setText(StringResource.getStringResource("miufo1003123"));  //"最多记录数量："
			queryNumLabel.setBounds(15, 83, 99, 16);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return queryNumLabel;
}
/**
 * 返回 textField 特性值。
 * @return javax.swing.JTextField
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTextField getQueryNumTextField() {
	if (queryNumTextField == null) {
		try {
			queryNumTextField = new UITextField();
			queryNumTextField.setName("queryNumTextField");
			queryNumTextField.setBounds(207, 83, 59, 20);
			queryNumTextField.setColumns(5);
			queryNumTextField.addKeyListener(this);
			//queryNumTextField.setKeymap();
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return queryNumTextField;
}
/**
 * 返回 querySelectComboBox 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getQuerySelectComboBox() {
	if (querySelectComboBox == null) {
		try {
			querySelectComboBox = new UIComboBox();
			
			querySelectComboBox.setName("querySelectComboBox");
			querySelectComboBox.setBounds(207, 28, 130, 25);
			//querySelectComboBox.setEditable(true);
			querySelectComboBox.addActionListener(this);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return querySelectComboBox;
}
/**
 * 返回 label 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getQuerySelectLabel() {
	if (querySelectLabel == null) {
		try {
			querySelectLabel = new UILabel();
			querySelectLabel.setName("querySelectLabel");
//			querySelectLabel.setFont(new java.awt.Font("dialog", 0, 12));
			querySelectLabel.setText(StringResource.getStringResource("miufo1003124"));  //"可选择的指标查询："
			querySelectLabel.setBounds(15, 28, 129, 16);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return querySelectLabel;
}
/**
 * 返回 radioButtonH 特性值。
 * @return javax.swing.JRadioButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JRadioButton getRadioButtonH() {
	if (radioButtonH == null) {
		try {
			radioButtonH = new UIRadioButton();
			radioButtonH.setName("radioButtonH");
//			radioButtonH.setFont(new java.awt.Font("dialog", 0, 12));
			radioButtonH.setText(StringResource.getStringResource("miufo1003125"));  //"横向（X方向）"
			radioButtonH.setBounds(207, 143, 105, 24);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return radioButtonH;
}
/**
 * 返回 radioButtonV 特性值。
 * @return javax.swing.JRadioButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JRadioButton getRadioButtonV() {
	if (radioButtonV == null) {
		try {
			radioButtonV = new UIRadioButton();
			radioButtonV.setName("radioButtonV");
//			radioButtonV.setFont(new java.awt.Font("dialog", 0, 12));
			radioButtonV.setText(StringResource.getStringResource("miufo1003126"));  //"纵向（Y方向）"
			radioButtonV.setBounds(314, 143, 123, 24);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return radioButtonV;
}
/**
 * 返回 addButton 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getTitleAddButton() {
	if (titleAddButton == null) {
		try {
			titleAddButton = new nc.ui.pub.beans.UIButton();
			titleAddButton.setName("titleAddButton");
			titleAddButton.setText(StringResource.getStringResource("miufo1000080"));  //"增加"
			titleAddButton.setBounds(323, 84, 75, 22);
			titleAddButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MeasureQueryTable mft = (MeasureQueryTable)getTitleTable().getModel();
					String title = (String) titleComboBox.getSelectedItem();
					String address = (String)titleAddressComboBox.getSelectedItem();
					String comp = (String)titleCompComboBox.getSelectedItem();
					if(title!=null&&address!=null&&comp!=null
						&&(!title.equals(""))&&(!address.equals(""))&&(!comp.equals("")))
					{
						MeasureQueryData mfd = new MeasureQueryData();
						mfd.addElements(title);
						mfd.addElements(address);
						mfd.addElements(comp);
						mft.AddEntry(mfd,mft.getColumnCount());
						mftVec.removeElement(title);
						getTitleComboBox().removeItem(title);
					}
					mft.fireTableDataChanged();//刷新显示
				}
			});
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleAddButton;
}
/**
 * 返回 JComboBox1 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getTitleAddressComboBox() {
	if (titleAddressComboBox == null) {
		try {
			titleAddressComboBox = new UIComboBox();
			//初始化赋值
			titleAddressComboBox.addItem(new String(StringResource.getStringResource("miufo1003107")));  //"横标题"
			titleAddressComboBox.addItem(new String(StringResource.getStringResource("miufo1003108")));  //"纵标题"
			titleAddressComboBox.setName("titleAddressComboBox");
			titleAddressComboBox.setBounds(184, 34, 91, 25);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleAddressComboBox;
}
/**
 * 返回 label4 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getTitleAddressLabel() {
	if (titleAddressLabel == null) {
		try {
			titleAddressLabel = new UILabel();
			titleAddressLabel.setName("titleAddressLabel");
//			titleAddressLabel.setFont(new java.awt.Font("dialog", 0, 12));
			titleAddressLabel.setText(StringResource.getStringResource("miufo1003127"));  //"标题位置："
			titleAddressLabel.setBounds(184, 10, 64, 16);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleAddressLabel;
}
/**
 * 返回 titleComboBox 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getTitleComboBox() {
	if (titleComboBox == null) {
		try {
			titleComboBox = new UIComboBox();
			titleComboBox.setName("titleComboBox");
//			titleComboBox.setFont(new java.awt.Font("dialog", 0, 12));
			titleComboBox.setBounds(24, 34, 130, 25);
		} catch (java.lang.Throwable Exc) {

			handleException(Exc);
		}
	}
	return titleComboBox;
}
/**
 * 返回 JComboBox2 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getTitleCompComboBox() {
	if (titleCompComboBox == null) {
		try {
			titleCompComboBox = new UIComboBox();
			//初始化赋值
			titleCompComboBox.addItem(new String(" "));
			titleCompComboBox.addItem(new String(StringResource.getStringResource("miufo1001305")));  //"升序"
			titleCompComboBox.addItem(new String(StringResource.getStringResource("miufo1001306")));  //"降序"
			titleCompComboBox.setName("titleCompComboBox");
			titleCompComboBox.setBounds(302, 34, 115, 25);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleCompComboBox;
}
/**
 * 返回 label5 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getTitleCompLabel() {
	if (titleCompLabel == null) {
		try {
			titleCompLabel = new UILabel();
			titleCompLabel.setName("titleCompLabel");
//			titleCompLabel.setFont(new java.awt.Font("dialog", 0, 12));
			titleCompLabel.setText(StringResource.getStringResource("miufo1003114"));  //"排列方式："
			titleCompLabel.setBounds(302, 10, 64, 16);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleCompLabel;
}
/**
 * 返回 deleteButton 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getTitleDeleteButton() {
	if (titleDeleteButton == null) {
		try {
			titleDeleteButton = new nc.ui.pub.beans.UIButton();
			titleDeleteButton.setName("titleDeleteButton");
			titleDeleteButton.setText(StringResource.getStringResource("miufopublic243"));  //"删除"
			titleDeleteButton.setBounds(323, 135, 75, 22);
			titleDeleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MeasureQueryTable mft = (MeasureQueryTable)getTitleTable().getModel();
					int[] Row = getTitleTable().getSelectedRows();
					int tableLength = getTitleTable().getRowCount();
					int loop = tableLength>Row.length?Row.length:tableLength;
					if(loop!=0)
					{
						for(int i = 0;i < loop;i++)
						{
							String delStr = (String)mft.getValueAt(Row[i]-i,0);
							mft.DeleteEntry(Row[i]-i);
							if(!mft.containsElements(delStr))
							{
								if(!mftVec.contains(delStr))
								{
									if(mftTable.get(delStr)!=null)
									{
										mftVec.addElement(delStr);
										getTitleComboBox().addItem(delStr);
									}
								}
							}
						}
					}
					
					mft.fireTableDataChanged();//刷新显示
				}
			});
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleDeleteButton;
}
/**
 * 返回 titleElmtPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getTitleElmtPane() {
	if (titleElmtPane == null) {
		try {
			titleElmtPane = new UIPanel();
			titleElmtPane.setName("titleElmtPane");
			titleElmtPane.setLayout(null);
			getTitleElmtPane().add(getTitleLable(), getTitleLable().getName());
			getTitleElmtPane().add(getTitleComboBox(), getTitleComboBox().getName());
			getTitleElmtPane().add(getTitleAddressLabel(), getTitleAddressLabel().getName());
			getTitleElmtPane().add(getTitleAddressComboBox(), getTitleAddressComboBox().getName());
			getTitleElmtPane().add(getTitleCompLabel(), getTitleCompLabel().getName());
			getTitleElmtPane().add(getTitleCompComboBox(), getTitleCompComboBox().getName());
			getTitleElmtPane().add(getTitleScrollPane(), getTitleScrollPane().getName());
			getTitleElmtPane().add(getTitleAddButton(), getTitleAddButton().getName());
			getTitleElmtPane().add(getTitleDeleteButton(), getTitleDeleteButton().getName());
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleElmtPane;
}
/**
 * 返回 titleLable 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getTitleLable() {
	if (titleLable == null) {
		try {
			titleLable = new UILabel();
			titleLable.setName("titleLable");
//			titleLable.setFont(new java.awt.Font("dialog", 0, 12));
			titleLable.setText(StringResource.getStringResource("miufo1003128"));  //"选择标题："
			titleLable.setBounds(24, 10, 61, 16);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleLable;
}
/**
 * 返回 scrollPane 特性值。
 * @return javax.swing.JScrollPane
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JScrollPane getTitleScrollPane() {
	if (titleScrollPane == null) {
		try {
			titleScrollPane = new UIScrollPane(getTitleTable());
			titleScrollPane.setName("titleScrollPane");
			titleScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			titleScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			titleScrollPane.setBounds(24, 70, 274, 124);
		} catch (java.lang.Throwable Exc) {
			handleException(Exc);
		}
	}
	return titleScrollPane;
}
/**
 * 返回 titleTable 特性值。
 * @return javax.swing.JTable
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTable getTitleTable() {
	if (titleTable == null) {
		try {
			titleTable = new nc.ui.pub.beans.UITable();
			String[] columnNames = {StringResource.getStringResource("miufo1003129"),StringResource.getStringResource("miufo1003130"),StringResource.getStringResource("miufo1003117")};
			mftTitle = new MeasureQueryTable();
			mftTitle.setColumnNames(columnNames);
			titleTable.setAutoCreateColumnsFromModel(true);
			titleTable.setModel(mftTitle); 
			titleTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			titleTable.setName("titleTable");

		} catch (java.lang.Throwable Exc) {

			handleException(Exc);
		}
	}
	return titleTable;
}
/**
 * Insert the method's description here.
 * 创建时间: (2002-7-4 10:31:34)
 * @return nc.vo.iufo.query.measurequery.MeasureQueryFuncVO
 */
public MeasureQueryFuncVO getVO() {
	return m_vo;
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// System.out.println("--------- 未捕捉到的异常 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * 创建日期：(2002-7-15 13:28:36)
 * @author：王海涛
 * @version：最后修改日期
 * 
 *  说明	当选择了一个指标查询之后，对标题、数据、条件相应位置进行初始化
 * 
 */
public void initCombBox() 
{
	if(vos.length == 0)
		return;
	MeasureQueryVO mqvo = vos[getQuerySelectComboBox().getSelectedIndex()];
	if(mftTable==null)
		mftTable = new Hashtable();
	if(mftVec==null)
		mftVec = new Vector();
	if(mqvo.getDbColumns() != null)
	{
		for(int i = 0;i < mqvo.getDbColumns().length;i++)
		{
			getTitleComboBox().addItem(mqvo.getDbColumns()[i].getName());
			getDataProComboBox().addItem(mqvo.getDbColumns()[i].getName());
			getConProBeginComboBox().addItem(mqvo.getDbColumns()[i].getName());
			getConProEndComboBox().addItem(mqvo.getDbColumns()[i].getName());
			mftVec.addElement(mqvo.getDbColumns()[i].getName());
			mftTable.put(mqvo.getDbColumns()[i].getName(),mqvo.getDbColumns()[i]);
			
		}
	}

}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		// user code begin {1}
		// user code end	
		setName("MeasureQueryRefDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setComponentOrientation(java.awt.ComponentOrientation.UNKNOWN);
		setSize(470, 315);
		setContentPane(getJDialogContentPane());
		setResizable(false);
	} catch (java.lang.Throwable Exc) {
		handleException(Exc);
	}
	// user code begin {2}
	// user code end
}
public void keyTyped(KeyEvent e)
{
	if(queryNumTextField.hasFocus())
	{
		if(e.getKeyChar()>'9'||e.getKeyChar()<'0')
		{
			queryNumTextField.setText(queryNumTextField.getText());
		}
		else
		{
			queryNumTextField.setText(queryNumTextField.getText()+e.getKeyChar());
		}
		e.consume();//销毁该按键事件
	}
}
/**
 * Insert the method's description here.
 * 创建时间: (2002-7-5 15:45:20)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	MeasureQueryRefDlg dlg = new MeasureQueryRefDlg();
	dlg.setModal(true);
	dlg.pack();
	dlg.setVisible(true);
	System.exit(0);
}
/**
 * 生成指标查询对象.
 * @return boolean 是否正确
 * 创建时间: (2002-7-5 14:50:09)
 */
protected boolean makeVO() {
	return true;
}
/**
 * Insert the method's description here.
 * 创建时间: (2002-7-4 10:31:34)
 * @param newM_vo nc.vo.iufo.query.measurequery.MeasureQueryFuncVO
 */
public void setVO(MeasureQueryFuncVO newVO) {
	m_vo = newVO;
}
/**
 * 创建日期：(2002-7-15 9:22:57)
 * @author：王海涛
 * @version：最后修改日期
 * 
 *  说明	当改变面板时，调用该方法
 * 			m_nTab表示面表标号：0：“选择指标查询”；1：“标题区”；2：“数据区”；3：“条件”；
 * @param ce javax.swing.event.ChangeEvent
 */
public void stateChanged(ChangeEvent ce) 
{
	nc.ui.pub.beans.UITabbedPane tabbedPane = (nc.ui.pub.beans.UITabbedPane) ce.getSource();
	//取到新选择的面板序号
	int m_nTab = tabbedPane.getSelectedIndex();				
	switch(m_nTab)
	{
		case 0:
			break;
		case 1:
			//如果是第一次从“选择指标查询”转到该面板，则进行初始化
			if(getTitleComboBox().getItemCount()==0&&getTitleTable().getRowCount()==0&&getDataProComboBox().getItemCount()==0&&getDataTable().getRowCount()==0)
				initCombBox();
			//如果是从其它面板转来，保证如果在数据区做过增加/删除操作，则相应的判断是否应该去掉/增加某些选择项
			getTitleComboBox().removeAllItems();
			if(mftVec != null &&mftVec.size() != 0)
			{
				for(int i = 0;i < mftVec.size();i++)
				{
					if( ((DBColumn)mftTable.get(mftVec.elementAt(i))).getType()==java.sql.Types.VARCHAR)
						getTitleComboBox().addItem(mftVec.elementAt(i));
				}
			}
			break;
		case 2:
			//如果是第一次从“选择指标查询”转到该面板，则进行初始化
			if(getTitleComboBox().getItemCount()==0&&getTitleTable().getRowCount()==0&&getDataProComboBox().getItemCount()==0&&getDataTable().getRowCount()==0)
				initCombBox();
			//如果是从其它面板转来，保证如果在标题区做过增加/删除操作，则相应的判断是否应该去掉/增加某些选择项
			getDataProComboBox().removeAllItems();
			if(mftVec != null &&mftVec.size() != 0 )
			{
				for(int i = 0;i < mftVec.size();i++)
				{
					getDataProComboBox().addItem(mftVec.elementAt(i));
				}
			}
			break;
		case 3:
			//如果是第一次从“选择指标查询”转到该面板，则进行初始化
			if( (getConProBeginComboBox().getItemCount()==0||getConProEndComboBox().getItemCount()==0)&&getConditionTable().getRowCount()==0 )
				initCombBox();
			break;
	}
}
}
