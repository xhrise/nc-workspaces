package com.ufsoft.iufo.fmtplugin.businessquery;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIFrame;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.component.ObjectTreeView;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QEEnvParamBean;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.QuerySimpleDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.querymodel.QueryModelDef;
import nc.vo.pub.querymodel.QueryModelNode;
import nc.vo.pub.querymodel.QueryModelTree;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.UfoPublic;
/**
@update 2003-11-07 15:16 liulp
   i)增加对ojbNode的null判断
   ii)将调用查询引擎的接口统一为调用工具类方法
@end
@update 2003-11-05 14:06 liulp
 增加报表查询修改时的校验（对查询引擎和报表指标/关键自的引用是否还有效）
@end
@update 2003-10-27 19:03 liulp
 1)修改选择非iufo的数据源，然后选择查询树上节点右面属性没值的错误
@end
@update 2003-10-23 13:21 liulp
 1)错误对话框应该是模态的
 2)修改查询的时候，修改前对象需要克隆
@end
@update 2003-10-22 15:34 liulp
	IUFO类型的数据源只能是当前名叫"iufo"的才能在数据源列表里列出
@end
@update 2003-10-21 20:39
  查询引擎添上所属数据源的属性，并在新建（/修改）已存在报表查询时，
    不能（/或可以）切换所属数据源，若有切换时，查询树也相应改变
@end
@update 2003-1014 17:01
  修改查询时，当前修改查询在右面的属性没有展现的错误更正.
@end
* 说明：选择业务查询
* 作者：王少松
* 创建日期：(2003-4-2 20:22:34)
* 版本：
*/
public class SelectBQueryPage
    extends nc.ui.pub.beans.UIPanel
    implements TreeSelectionListener, ItemListener, ActionListener {
    private JLabel ivjJLabelcode = null;
    private JLabel ivjJLabelname = null;
    private JLabel ivjJLabelnote = null;
    private JPanel ivjPage = null;
    private JScrollPane ivjJScrollPane1 = null;
    private ObjectTreeView ivjObjectTreeView1 = null;
    private JTextField ivjJTextFieldCode = null;
    private JTextField ivjJTextFieldName = null;
    private JScrollPane ivjJScrollPane2 = null;
    private JTextArea ivjJTextAreaNote = null;
    private JLabel ivjJLabelDataSource = null;
    private JComboBox ivjJComboBoxDataSource = null;
    private JComboBox ivjJComboBoxQEDS = null;
    private JLabel ivjJLabelQEDS = null;
    private JTable table = null;
    private JButton ivjBtnRefresh = null;
    /**
     * 查询定义信息
     */
    private ReportQueryVO m_ReportQueryVO = null;

    /**
     * 选中的业务查询信息_备份
     */
    private QuerySimpleDef m_oQueryDefBak = null;
    /**
     * 系统数据源信息
     */
    private String[] m_strDataSouces = null;
    /**
     * 父对话框
     */
    BQueryPropertyDlg m_ParentDlg = null;
    /**
     * 修改报表查询，所引用查询引擎的查询所在数据源
     */
    private String m_strQEDsName = null;
    /**IUFO数据源的标记。与类DataManageObjectIufo中的一致。
     * wupeng2005-2-22修改，原因是DataManageObjectIufo的父类在bs包下*/
    public static String IUFO_DATASOURCE=DataManageObjectIufo.IUFO_DATASOURCE;
/**
 * SelectBQueryPage 构造子注解。
 */
public SelectBQueryPage() {
	super();
	initialize();
}
/**
 * SelectBQueryPage 构造子注解。
 */
public SelectBQueryPage(BQueryPropertyDlg parentDlg) {
	super();
	initialize();
	this.m_ParentDlg = parentDlg;
}
/**
 * SelectBQueryPage 构造子注解。
 */
public SelectBQueryPage(BQueryPropertyDlg parentDlg, String strQEDsName) {
    super();
    this.m_ParentDlg = parentDlg;
    this.m_strQEDsName = strQEDsName;
    initialize();
}
/**
 * SelectBQueryPage 构造子注解。
 * @param layout java.awt.LayoutManager
 */
public SelectBQueryPage(java.awt.LayoutManager layout) {
	super(layout);
	initialize();
}
/**
 * SelectBQueryPage 构造子注解。
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public SelectBQueryPage(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
	initialize();
}
/**
 * SelectBQueryPage 构造子注解。
 * @param isDoubleBuffered boolean
 */
public SelectBQueryPage(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
	initialize();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-15 17:15:19)
 * @author：刘良萍
 * @return java.lang.String
 */
public String getDSName() {
    return (String) getJComboBoxDataSource().getSelectedItem();
}
/**
 * 返回 JComboBox1 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getJComboBoxDataSource() {
	if (ivjJComboBoxDataSource == null) {
		try {
			ivjJComboBoxDataSource = new UIComboBox();
			ivjJComboBoxDataSource.setName("JComboBoxDataSource");
			ivjJComboBoxDataSource.setBounds(68, 78, 199, 20);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJComboBoxDataSource;
}
/**
 * 返回 JComboBoxQEDS 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getJComboBoxQEDS() {
	if (ivjJComboBoxQEDS == null) {
		try {
			ivjJComboBoxQEDS = new UIComboBox();
			ivjJComboBoxQEDS.setName("JComboBoxQEDS");
			ivjJComboBoxQEDS.setBounds(105, 23, 175, 20);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJComboBoxQEDS;
}
/**
 * 返回 JLabelcode 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelcode() {
	if (ivjJLabelcode == null) {
		try {
			ivjJLabelcode = new nc.ui.pub.beans.UILabel();
			ivjJLabelcode.setName("JLabelcode");
	//		ivjJLabelcode.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabelcode.setText("Code:");
			ivjJLabelcode.setBounds(14, 46, 47, 16);
			ivjJLabelcode.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strCode = StringResource.getStringResource("miufo1001421");  //"编码"
			ivjJLabelcode.setText(strCode);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelcode;
}
/**
 * 返回 JLabelDataSource 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelDataSource() {
	if (ivjJLabelDataSource == null) {
		try {
			ivjJLabelDataSource = new nc.ui.pub.beans.UILabel();
			ivjJLabelDataSource.setName("JLabelDataSource");
	//		ivjJLabelDataSource.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabelDataSource.setText("DataSource");
			ivjJLabelDataSource.setBounds(14, 78, 47, 16);
			ivjJLabelDataSource.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strDataSource = StringResource.getStringResource("miufo1001422");  //"数据源"
			ivjJLabelDataSource.setText(strDataSource);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelDataSource;
}
/**
 * 返回 JLabelname 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelname() {
	if (ivjJLabelname == null) {
		try {
			ivjJLabelname = new nc.ui.pub.beans.UILabel();
			ivjJLabelname.setName("JLabelname");
	//		ivjJLabelname.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabelname.setText("Name:");
			ivjJLabelname.setBounds(14, 13, 47, 16);
			ivjJLabelname.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strName = StringResource.getStringResource("miufo1001423");  //"名称"
			ivjJLabelname.setText(strName);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelname;
}
/**
 * 返回 JLabelnote 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelnote() {
	if (ivjJLabelnote == null) {
		try {
			ivjJLabelnote = new nc.ui.pub.beans.UILabel();
			ivjJLabelnote.setName("JLabelnote");
	//		ivjJLabelnote.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabelnote.setText("Note");
			ivjJLabelnote.setBounds(14, 122, 47, 16);
			ivjJLabelnote.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strNote = StringResource.getStringResource("miufo1001424");  //"描述"
			ivjJLabelnote.setText(strNote);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelnote;
}
/**
 * 返回 JLabelQEDS 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelQEDS() {
	if (ivjJLabelQEDS == null) {
		try {
			ivjJLabelQEDS = new nc.ui.pub.beans.UILabel();
			ivjJLabelQEDS.setName("JLabelQEDS");
	//		ivjJLabelQEDS.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabelQEDS.setText("BeOwnedDataSource:");
			ivjJLabelQEDS.setBounds(19, 23, 79, 16);
			ivjJLabelQEDS.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strBeOwnedDS = StringResource.getStringResource("miufo1001425");  //"所属数据源"
			ivjJLabelQEDS.setText(strBeOwnedDS);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelQEDS;
}
/**
 * 返回 JScrollPane1 特性值。
 * @return javax.swing.JScrollPane
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JScrollPane getJScrollPane1() {
	if (ivjJScrollPane1 == null) {
		try {
			ivjJScrollPane1 = new UIScrollPane();
			ivjJScrollPane1.setName("JScrollPane1");
			ivjJScrollPane1.setBounds(16, 52, 265, 259);
			getJScrollPane1().setViewportView(getObjectTreeView1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane1;
}
/**
 * 返回 JScrollPane2 特性值。
 * @return javax.swing.JScrollPane
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JScrollPane getJScrollPane2() {
	if (ivjJScrollPane2 == null) {
		try {
			ivjJScrollPane2 = new UIScrollPane();
			ivjJScrollPane2.setName("JScrollPane2");
			ivjJScrollPane2.setBounds(67, 122, 199, 150);
			getJScrollPane2().setViewportView(getJTextAreaNote());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane2;
}
/**
 * 返回 JTextArea1 特性值。
 * @return javax.swing.JTextArea
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTextArea getJTextAreaNote() {
	if (ivjJTextAreaNote == null) {
		try {
			ivjJTextAreaNote = new UITextArea();
			ivjJTextAreaNote.setName("JTextAreaNote");
			ivjJTextAreaNote.setBounds(67, 122, 199, 150);
			ivjJTextAreaNote.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJTextAreaNote;
}
/**
 * 返回 JTextField2 特性值。
 * @return javax.swing.JTextField
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTextField getJTextFieldCode() {
	if (ivjJTextFieldCode == null) {
		try {
			ivjJTextFieldCode = new UITextField();
			ivjJTextFieldCode.setName("JTextFieldCode");
			ivjJTextFieldCode.setBounds(68, 46, 199, 20);
			ivjJTextFieldCode.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJTextFieldCode;
}
/**
 * 返回 JTextField1 特性值。
 * @return javax.swing.JTextField
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTextField getJTextFieldName() {
	if (ivjJTextFieldName == null) {
		try {
			ivjJTextFieldName = new UITextField();
			ivjJTextFieldName.setName("JTextFieldName");
			ivjJTextFieldName.setBounds(68, 13, 199, 20);
			ivjJTextFieldName.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJTextFieldName;
}
/**
 * 返回 ObjectTreeView1 特性值。
 * @return nc.ui.com.component.ObjectTreeView
 */
/* 警告：此方法将重新生成。 */
private ObjectTreeView getObjectTreeView1() {
	if (ivjObjectTreeView1 == null) {
		try {
			ivjObjectTreeView1 = new ObjectTreeView();
			ivjObjectTreeView1.setName("ObjectTreeView1");
			ivjObjectTreeView1.setBounds(0, 0, 78, 72);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjObjectTreeView1;
}
/**
 * 返回 Page 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getPage() {
	if (ivjPage == null) {
		try {
			ivjPage = new UIPanel();
			ivjPage.setName("Page");
			ivjPage.setLayout(null);
			ivjPage.setBounds(292, 20, 303, 317);
			getPage().add(getJTextFieldName(), getJTextFieldName().getName());
			getPage().add(getJLabelname(), getJLabelname().getName());
			getPage().add(getJLabelcode(), getJLabelcode().getName());
			getPage().add(getJTextFieldCode(), getJTextFieldCode().getName());
			getPage().add(getJLabelnote(), getJLabelnote().getName());
			getPage().add(getJScrollPane2(), getJScrollPane2().getName());
			getPage().add(getJLabelDataSource(), getJLabelDataSource().getName());
			getPage().add(getJComboBoxDataSource(), getJComboBoxDataSource().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPage;
}
/**
 * 获得“刷新业务查询树”按钮对象
 * @return
 */
private javax.swing.JButton getBtnRefresh() {
	if (ivjBtnRefresh == null) {
		try {
			ivjBtnRefresh = new nc.ui.pub.beans.UIButton();
			ivjBtnRefresh.setName("BtnNext");
	//		ivjBtnNext.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnRefresh.setText("Refresh");
			ivjBtnRefresh.setBounds(66,318, 75, 22);//16, 52, 265, 259
			// user code begin {1}
			String strRefresh = StringResource.getStringResource("uiuforepqe00001");  //"刷新业务查询树"
//			strRefresh = "Refresh business query tree";//Englist version debug 
			ivjBtnRefresh.setText(strRefresh);
			ivjBtnRefresh.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnRefresh;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-10-21 20:20:36)
 * @author：刘良萍
 * @return java.lang.String
 */
public String getQEDSName() {
    return (String) getJComboBoxQEDS().getSelectedItem();
}
/**
 * 得到指定查询引擎定义树的节点对应的QuerySimpleDed对象。
 * 
 * 创建日期：(2003-11-5 13:29:58)
 * @author：刘良萍
 * @return nc.vo.iuforeport.businessquery.QuerySimpleDef
 * @param objNode nc.vo.pub.core.ObjectNode
 */
private QuerySimpleDef getQuerySimpleDef(ObjectNode objNode) {
    QuerySimpleDef querySimpleDef = null;
    try {
        ////debug
        //objNode =
        //QueryModelTree
        //.getInstance(m_strQEDsName != null ? m_strQEDsName : getQEDSName())
        //.findObjectNodeByGUID(objNode.getGUID());
        ////end
        if (objNode == null) {
            return null;
        }
        QueryModelDef queryModelDef = (QueryModelDef) objNode.getObject();
        if (queryModelDef == null) {
            return null;
        }
        QueryBaseDef queryDef = (QueryBaseDef) queryModelDef.getQueryBaseDef();
        querySimpleDef = //QueryUtil.
                          createDef(queryDef);

    } catch (Exception exception) {
        AppDebug.debug(exception.getMessage());//@devTools System.out.println(exception.getMessage());
    }

    return querySimpleDef;
}
/**
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-9 10:49:08)
 * 参数： @param：<|>
 * 返回值：@return：
 * 
 * @return ReportQueryVO
 */
public ReportQueryVO getReportQueryVO() {
	return m_ReportQueryVO;
}
/**
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-4 9:39:22)
 * 参数： @param：<|>
 * 返回值：@return：
 * 
 * @return javax.swing.JTable
 */
private JTable getTable() {
	table = new nc.ui.pub.beans.UITable();
	//table.getTableHeader().setFont(new Font("dialog",0,12));
	//table.setFont(new Font("dialog",0,12));
	
	//表模型
	DefaultTableModel tm =
		new DefaultTableModel(new Object[] { 
				StringResource.getStringResource("miufopublic115"),  //"名称"
				StringResource.getStringResource("miufopublic264")   //"说明"
				}, 0) {
		public int getColumnCount() {
			return 2;
		}
		public Class getColumnClass(int col) {		
				return String.class;		
		}
		public boolean isCellEditable(int row, int col) {
			return false;		
		}
	};

	table.setModel(tm);

	
	//设置表属性
	table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	table.sizeColumnsToFit(-1);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
	return table;	

}
/**
 * 获得表格
 * 创建日期：(2003-4-2 13:48:52)
 * @return nc.ui.pub.beans.UITable
 */
private DefaultTableModel getTM() {
	return (DefaultTableModel) table.getModel();
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// System.out.println("--------- 未捕捉到的异常 ---------");
	AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
    try {
        // user code begin {1}
        // user code end
        setName("SelectBQueryPage");
        setLayout(null);
        setSize(611, 350);
        add(getJScrollPane1(), getJScrollPane1().getName());
        add(getPage(), getPage().getName());
        add(getJLabelQEDS(), getJLabelQEDS().getName());
        add(getJComboBoxQEDS(), getJComboBoxQEDS().getName());
        add(getBtnRefresh(), getBtnRefresh().getName());//added by liulp,2005-06-09
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    String strDefDsName = m_strQEDsName;
    if (m_strQEDsName == null) {
        strDefDsName = IUFO_DATASOURCE;
    } else {
        ivjJComboBoxQEDS.setEnabled(false);
    }
    String[][] strDsnDbtypes = null;
    try {
        //m_strDataSouces = AccountBO_Client.findDataSource();
//    	IQEDataSourceInfo info = (IQEDataSourceInfo) NCLocator.getInstance().lookup(IQEDataSourceInfo.class.getName());
//        strDsnDbtypes =info.getDsnDbtype();
        
        strDsnDbtypes = QEEnvParamBean.getDefaultInstance().getDsnDbType();//TODO TO debug this new interface
    } catch (Exception ex) {
AppDebug.debug(ex);//@devTools         ex.printStackTrace(System.out);
    }

    int iDsnDbLen = strDsnDbtypes != null ? strDsnDbtypes.length : 0;
    if (iDsnDbLen > 0) {
        m_strDataSouces = new String[iDsnDbLen];
        for (int i = 0; i < iDsnDbLen; i++) {
            m_strDataSouces[i] = strDsnDbtypes[i][0];
        }
    }

    int iLen = m_strDataSouces != null ? m_strDataSouces.length : 0;
    int iQESelIndex = -1;
    for (int i = 0; i < iLen; i++) {
        if (strDefDsName.equals(m_strDataSouces[i])) {
            iQESelIndex = i;
        }
        ivjJComboBoxDataSource.addItem(m_strDataSouces[i]);
        ivjJComboBoxQEDS.addItem(m_strDataSouces[i]);
    }
    ivjJComboBoxDataSource.addItem("");
    //设置焦点在最后空项目
    ivjJComboBoxDataSource.setSelectedIndex(iLen);
    ivjJComboBoxDataSource.setEnabled(false);
    ivjJComboBoxQEDS.setSelectedIndex(iQESelIndex);
    ivjJComboBoxQEDS.addItemListener(this);
    //ivjJComboBoxQEDS.addActionListener(this);
    //    ListDataListener
    //添加tree的监听
    getObjectTreeView1().addTreeSelectionListener(this);
    //初始化树
    getObjectTreeView1().setObjectTree(
        new ObjectTree[] { QueryModelTree.getInstance(strDefDsName)});
    //QueryModelTree.getInstance(DataManageObjectIufo.IUFO_DATASOURCE)});
    // user code end
}
/**
 * 是否为创建报表查询。
 * 
 * 创建日期：(2003-11-5 11:58:09)
 * @author：刘良萍
 * @return boolean
 */
protected boolean isCreateReportQuery() {
    return getJComboBoxQEDS().isEnabled();
}
/**
 * Invoked when an item has been selected or deselected.
 * The code written for this method performs the operations
 * that need to occur when an item is selected (or deselected).
 */
public void itemStateChanged(java.awt.event.ItemEvent e) {
    String strQEDs = (String) getJComboBoxQEDS().getSelectedItem();
    if (strQEDs != null) {
        getObjectTreeView1().setObjectTree(
            new ObjectTree[] { QueryModelTree.getInstance(strQEDs)});
    }
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new UIFrame();
		SelectBQueryPage aSelectBQueryPage;
		aSelectBQueryPage = new SelectBQueryPage();
		frame.setContentPane(aSelectBQueryPage);
		frame.setSize(aSelectBQueryPage.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoPanel's main() :Exception");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * 设置当前查询的数据源的选中状态。
 * 	如果有，则使其处于选中状态，否则选中最后的那个空项目
 *
 * 创建日期：(2003-9-10 09:37:27)
 * @param strDbName java.lang.String
 */
private void setDBNameIndex(String strDbName) {
    ivjJComboBoxDataSource.setEnabled(true);
    if (strDbName != null) {
        boolean bSetted = false;
        int iLen = m_strDataSouces != null ? m_strDataSouces.length : 0;
        for (int i = 0; i < iLen; i++) {
            if (strDbName.trim().equals(m_strDataSouces[i].trim())) {
                ivjJComboBoxDataSource.setSelectedIndex(i);
                bSetted = true;
                break;
            }
        }
        if (!bSetted) {
            ivjJComboBoxDataSource.setSelectedIndex(iLen);
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001426") + strDbName,this);  //"当前服务器没有数据源："
        }
    }
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-10-21 20:33:18)
 * @author：刘良萍
 * @param strQEDbName java.lang.String
 */
private void setQEDBNameIndex(String strQEDbName) {
    ivjJComboBoxQEDS.setEnabled(false);
    if (strQEDbName != null) {
        boolean bSetted = false;
        int iLen = m_strDataSouces != null ? m_strDataSouces.length : 0;
        int iIufoIndex = 0;
        for (int i = 0; i < iLen; i++) {
            if (strQEDbName.trim().equals(m_strDataSouces[i].trim())) {
                ivjJComboBoxQEDS.setSelectedIndex(i);
                bSetted = true;
                //break;
            }
            if (m_strDataSouces[i].equals(IUFO_DATASOURCE)) {
                iIufoIndex = i;
            }
        }
        if (!bSetted) {
            ivjJComboBoxQEDS.setSelectedIndex(iIufoIndex);
            UfoPublic.sendWarningMessage(
            		StringResource.getStringResource("miufo1001426") +   //"当前服务器没有数据源："
					strQEDbName,this);
        }
    }
}
/**
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-9 10:49:08)
 * 参数： @param：<|>
 * 返回值：@return：
 * 
 * @param newReportQueryVO ReportQueryVO
 * @param bInitRightPanel boolean - 是否初始化右边面扳的值
 */
public void setReportQueryVO(
    ReportQueryVO newReportQueryVO,
    boolean bInitRightPanel) {
    m_ReportQueryVO = newReportQueryVO;
    if (m_ReportQueryVO != null) {
    	//added by liulp,2005-06-09 begin
    	QuerySimpleDef queryDef = m_ReportQueryVO.getQuerydef();
    	if( queryDef == null){
    		if(m_oQueryDefBak!=null){
    		  queryDef = m_oQueryDefBak;
    		  m_ReportQueryVO.setQuerydef(queryDef);
    		}else{
    			return;
    		}
    	}
    	//added by liulp,2005-06-09 end
        String strGUID = queryDef.getGUID();
        ObjectNode objNode =
            ReportQueryUtil.getQueryEngineObjectNode(
                strGUID,
                m_strQEDsName != null ? m_strQEDsName : getQEDSName());
        if (objNode == null) {
            return;
        }
        ivjObjectTreeView1.selectNode(objNode);

        if (bInitRightPanel) {
            //设置查询引擎的基础查询定义为当前系统最新的
            if (objNode != null && QueryModelNode.MODEL_KIND.equals(objNode.getKind())) {
                QuerySimpleDef querySimpleDef = getQuerySimpleDef(objNode);
            	//changed by liulp,2005-06-09
                updateQueryDef(m_ReportQueryVO,querySimpleDef);

            }
            //设置右边面板对应的值
            valueChanged(null);
        }
    }
}
/** 
  * Called whenever the value of the selection changes.
  * @param e the event that characterizes the change.
  */
public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
    try {
        ObjectNode objNode = ivjObjectTreeView1.getSelectedObjectNode();
        if (objNode != null && QueryModelNode.MODEL_KIND.equals(objNode.getKind())) {
	        //得到指定查询引擎定义树的节点对应的QuerySimpleDed对象
            QuerySimpleDef querySimpleDef = getQuerySimpleDef(objNode);
            if (querySimpleDef == null) {
                return;
            }

            //如果已有相应的报表查询，则得到该报表查询，否则计算得到相应的QuerySimpleDef
            String strGUID = querySimpleDef.getGUID();
            ReportBusinessQuery reportBusinessQuery =
                m_ReportQueryVO.getReportBusinessQuery();
            ReportQueryVO oldRQVO = reportBusinessQuery.getReportQuery(strGUID);
            String strDSName = null;
            String strQEDsName = null;
            if (oldRQVO != null) { //克隆的同时其所属的报表查询管理对象必须进行设置
                oldRQVO =
                    (ReportQueryVO) ReportQueryUtil.cloneReportQueryVO(
                        oldRQVO,
                        reportBusinessQuery);
                //！对应的查询引擎所使用的基础查询定义必须使用当前系统最新定义
            	//changed by liulp,2005-06-09
                updateQueryDef(oldRQVO,querySimpleDef);
                m_ReportQueryVO = oldRQVO;
                strDSName = m_ReportQueryVO.getDSName();
                strQEDsName = m_ReportQueryVO.getQEDSName();
                setQEDBNameIndex(strQEDsName);
            } else {
                m_ReportQueryVO = new ReportQueryVO(reportBusinessQuery);
            	//changed by liulp,2005-06-09
                updateQueryDef(m_ReportQueryVO,querySimpleDef);
                strDSName = m_ReportQueryVO.getQuerydef().getDsName();
                strQEDsName = IUFO_DATASOURCE;
                ivjJComboBoxQEDS.setEnabled(true);
            }

            m_ParentDlg.setReportQueryVO(m_ReportQueryVO, false);
            getJTextFieldName().setText(m_ReportQueryVO.getQuerydef().getDisplayName());
            getJTextFieldCode().setText(m_ReportQueryVO.getQuerydef().getID());
            getJTextAreaNote().setText(m_ReportQueryVO.getQuerydef().getNote());
            //选中的哪个数据源，当前prop里没有则提示错误！
            setDBNameIndex(strDSName);
        } else {
            getJTextFieldName().setText("");
            getJTextFieldCode().setText("");
            getJTextAreaNote().setText("");
        	//added by liulp,2005-06-09
            updateQueryDef(m_ReportQueryVO,m_ReportQueryVO.getQuerydef());
            
            m_ReportQueryVO.setQuerydef(null);
            m_ParentDlg.setReportQueryVO(m_ReportQueryVO, false);
        }
    } catch (Exception exception) {
        AppDebug.debug(exception.getMessage());//@devTools System.out.println(exception.getMessage());
    }
}
/* （非 Javadoc）
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
public void actionPerformed(ActionEvent e) {
	//刷新业务查询树
    if (e.getSource() == getBtnRefresh()) {
        getObjectTreeView1().refreshTree();
    	
    	
//        getObjectTreeView1().expandRow(0);
//        getObjectTreeView1().setSelectionRow(0);
        getObjectTreeView1().addTreeSelectionListener(null);
        setReportQueryVO(getReportQueryVO(),true);
        getObjectTreeView1().addTreeSelectionListener(this);
    }	
}
/**
 * 刷新业务查询数的时候，处理无谓的TreeSelectNodeChanged事件
 * added by liulp,2005-06-09
 * @param repQueryVO
 * @param querySimpleDef
 */
private void updateQueryDef(ReportQueryVO repQueryVO,QuerySimpleDef querySimpleDef){
	if(repQueryVO != null){
		repQueryVO.setQuerydef(querySimpleDef);
	}
}

/**
 * 本方法uap暂时没有，uap修改后会加上，暂时放在这里。
 * 创建用于报表查询的查询定义 创建日期：(2003-4-8 9:28:59)
 *
 * @return nc.vo.iuforeport.businessquery.QueryBaseDef
 * @param qbd nc.vo.iuforeport.businessquery.QueryBaseDef
 * @throws Exception
 */
public static QuerySimpleDef createDef(QueryBaseDef qbd) throws Exception {
    QuerySimpleDef newQsd = null;
    if (qbd != null) {
        newQsd = new QuerySimpleDef();
        //获得VO
        FromTableVO[] fts = qbd.getFromTables();
        SelectFldVO[] sfs = qbd.getSelectFlds();
        //迁移表
        String tempTableName = qbd.getTemptablename();
        FromTableVO[] newFts = new FromTableVO[1];
        newFts[0] = new FromTableVO();
        newFts[0].setTablecode(tempTableName);
        newQsd.setFromTables(newFts[0]);
        //迁移字段
        int iLen = (sfs == null) ? 0 : sfs.length;
        if (iLen != 0) {
            SelectFldVO[] newSfs = new SelectFldVO[iLen];
            for (int i = 0; i < iLen; i++) {
                newSfs[i] = new SelectFldVO();
                newSfs[i].setExpression(tempTableName + "." +
                                        sfs[i].getFldalias());
                newSfs[i].setFldname(sfs[i].getFldname());
                newSfs[i].setFldalias(sfs[i].getFldalias());
                newSfs[i].setColtype(sfs[i].getColtype());
            }
            newQsd.setSelectFlds(newSfs);
        }
        //数据源
        newQsd.setDsName(qbd.getDsName());
        //获得节点
        QueryModelNode node = (QueryModelNode) qbd.getNode();
        //克窿节点
        newQsd.setID(node.getID());
        newQsd.setGUID(node.getGUID());
        newQsd.setDisplayName(node.getDisplayName());
    }
    return newQsd;
}

}


